package com.gp.cvst.logisoft.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.itextpdf.text.ExceptionConverter;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.oreilly.servlet.ServletUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class AccrualsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        AccrualsForm accrualsForm = (AccrualsForm) form;
        String buttonValue = accrualsForm.getButtonValue();
        String forwardName = "success";
        try {
            if (null == buttonValue || buttonValue.trim().equals("")) {
                forwardName = "success";
            } else if (buttonValue.trim().equals("addOrUpdateAccruals")) {
                String vendorNumber = request.getParameter("vendorNumber");
                if (CommonUtils.isNotEmpty(vendorNumber)) {
                    TradingPartner tradingPartner = new TradingPartnerDAO().findById(vendorNumber);
                    if (tradingPartner.getAccountName().contains("\"")) {
                        request.setAttribute("specialCharacter", "doubleQuotes");
                    }
                    if (tradingPartner.getAccountName().contains("'")) {
                        request.setAttribute("specialCharacter", "singleQuotes");
                    }
                    request.setAttribute("vendorName", tradingPartner.getAccountName());
                }
                DBUtil dbUtil = new DBUtil();
                List<LabelValueBean> shipmentTypeList = dbUtil.getShipmentType();
                request.setAttribute("shipmentTypeList", shipmentTypeList);
                request.setAttribute("error", CommonConstants.ERROR);
                request.setAttribute("errorLogin", CommonConstants.ERROR_LOGIN);
                forwardName = "addOrUpdateAccruals";
            } else if (buttonValue.trim().equals("validateGlAccount")) {
                forwardName = "addOrUpdateAccruals";
            } else if (buttonValue.trim().equals("exportAccrualsToExcel")) {
                String fileName = accrualsForm.getFileName();
                if (CommonUtils.isNotEmpty(fileName)) {
                    response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
                    response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
                    ServletUtils.returnFile(fileName, response.getOutputStream());
                    return null;
                }
            } else if (buttonValue.trim().equals("addValidGLAccountTemplate")) {
                String transactionIdsForValidation = request.getParameter("transactionIdsForValidation");
                if (null != transactionIdsForValidation && !transactionIdsForValidation.trim().equals("")) {
                    AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
                    DBUtil dbUtil = new DBUtil();
                    List<LabelValueBean> shipmentTypeList = dbUtil.getShipmentType();
                    request.setAttribute("shipmentTypeList", shipmentTypeList);
                    String ids = StringUtils.removeEnd(StringUtils.removeStart(transactionIdsForValidation.replaceAll("AC", ""), ","), ",");
                    request.setAttribute("accruals", accountingLedgerDAO.getInvalidGLAccountAccruals(ids));
                    forwardName = "validGLAccount";
                }
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        DBUtil dBUtil = new DBUtil();
        request.setAttribute("termDesc", "Due Upon Receipt");
        request.setAttribute("docTypeList", dBUtil.getSearchType(CommonConstants.PAGE_ACCRUALS));
        return mapping.findForward(forwardName);
    }
}
