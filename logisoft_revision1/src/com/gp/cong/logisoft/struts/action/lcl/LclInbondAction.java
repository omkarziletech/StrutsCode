package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclImportUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclInbondForm;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclQuoteImport;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclInbondAction extends LogiwareDispatchAction {

    /**
     * ********** action methods starts ***********
     */
    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclInbondForm lclInbondForm = (LclInbondForm) form;
        request.setAttribute("inbondList", new LclInbondsDAO().findByProperty("lclFileNumber.id", lclInbondForm.getFileNumberId()));
        request.setAttribute("lclInbondForm", lclInbondForm);
        inbondDetails(lclInbondForm, request);
        if (lclInbondForm.getStatus() != null && !lclInbondForm.getStatus().equals("")) {
            new LclUtils().insertLCLRemarks(lclInbondForm.getFileNumberId(), "Status Update Notification -> "+lclInbondForm.getStatus(), LclCommonConstant.REMARKS_DR_AUTO_NOTES, getCurrentUser(request));
        }
        return mapping.findForward("newInBond");
    }

    public ActionForward addInbonds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclInbondForm lclInbondForm = (LclInbondForm) form;
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        User user = getCurrentUser(request);
        LclSsHeader lclssheader = null;
        Integer dispCount =0;
        if (null != lclInbondForm.getLclInbond().getInbondId() && lclInbondForm.getLclInbond().getInbondId() != 0) {
            LclInbond lclInbond = new LclInbondsDAO().findById(lclInbondForm.getId());
            lclInbond.setInbondNo(lclInbondForm.getLclInbond().getInbondNo().toUpperCase());
            lclInbond.setInbondType(lclInbondForm.getLclInbond().getInbondType());
            if (CommonFunctions.isNotNull(lclInbondForm.getInbondPortId())) {
                lclInbond.setInbondPort(new UnLocationDAO().findById(lclInbondForm.getInbondPortId()));
            } else {
                lclInbond.setInbondPort(null);
            }
            if (CommonFunctions.isNotNull(lclInbondForm.getLclInbond().getInbondDatetime())) {
                lclInbond.setInbondDatetime(lclInbondForm.getLclInbond().getInbondDatetime());
            }
            if (lclInbondForm.getModuleName().equalsIgnoreCase("Exports")) {
                if (lclInbondForm.getEciBond().equalsIgnoreCase("Y")) {
                    lclInbond.setEciBond(true);
                } else {
                    lclInbond.setEciBond(false);
                }
                if (lclInbondForm.getInbondOpenClose().equalsIgnoreCase("Y")) {
                    lclInbond.setInbondOpenClose(true);
                } else {
                    lclInbond.setInbondOpenClose(false);
                }
                }
        } else {
            lclInbondForm.getLclInbond().setEnteredBy(user);
            lclInbondForm.getLclInbond().setModifiedBy(user);
            lclInbondForm.getLclInbond().setEnteredDatetime(new Date());
            lclInbondForm.getLclInbond().setModifiedDatetime(new Date());
            if(lclInbondForm.getModuleName().equalsIgnoreCase("Exports")){
            if(lclInbondForm.getEciBond().equalsIgnoreCase("Y")){
                    lclInbondForm.getLclInbond().setEciBond(true);
                } else {
                    lclInbondForm.getLclInbond().setEciBond(false);
                }
            if(lclInbondForm.getInbondOpenClose().equalsIgnoreCase("Y")){
                    lclInbondForm.getLclInbond().setInbondOpenClose(true);
                } else {
                    lclInbondForm.getLclInbond().setInbondOpenClose(false);
                }
            }
            new LclInbondsDAO().save(lclInbondForm.getLclInbond());
        }
        if(CommonUtils.isNotEmpty(lclInbondForm.getHeaderId())) {
            lclssheader = lclSsHeaderDAO.findById(lclInbondForm.getHeaderId());
            dispCount = lclSsHeaderDAO.getDispCount(lclInbondForm.getHeaderId(), "AVAL");
           if(dispCount == 0 && (lclssheader.getClosedBy() == null && lclssheader.getAuditedBy() == null)) {
                out.print("true");
            }
        }
        lclBookingDAO.updateModifiedDateTime(lclInbondForm.getLclInbond().getLclFileNumber().getId(),user.getUserId());
        request.setAttribute("inbondList", new LclInbondsDAO().findByProperty("lclFileNumber.id", lclInbondForm.getLclInbond().getLclFileNumber().getId()));
        request.setAttribute("fileNumberId", lclInbondForm.getLclInbond().getLclFileNumber().getId());
        request.setAttribute("fileNumber", lclInbondForm.getLclInbond().getLclFileNumber().getFileNumber());
        inbondDetails(lclInbondForm, request);
        request.setAttribute("lclInbondForm", lclInbondForm);
        request.setAttribute("insertInbondFlag", "true");
        if("IT".equalsIgnoreCase(lclInbondForm.getInbondType()) && dispCount == 0 && ((!lclInbondForm.getFileState().equals("Q") && lclInbondForm.getModuleName().equalsIgnoreCase("Imports")))) {
            return null;
        } else {
            return mapping.findForward("newInBond");
        }
    }

    public ActionForward closeInbond(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclInbondForm lclInbondForm = (LclInbondForm) form;
        LclUtils lclUtils = new LclUtils();
        User loginUser = getCurrentUser(request);
        lclUtils.insertLCLRemarks(lclInbondForm.getLclInbond().getLclFileNumber().getId(), "Deleted -> Inbond # -> " + lclInbondForm.getLclInbond().getInbondNo().toUpperCase(), "auto", loginUser);
        new LclInbondsDAO().delete(lclInbondForm.getId());
        request.setAttribute("inbondList", new LclInbondsDAO().findByProperty("lclFileNumber.id", lclInbondForm.getLclInbond().getLclFileNumber().getId()));
        request.setAttribute("fileNumberId", lclInbondForm.getLclInbond().getLclFileNumber().getId());
        request.setAttribute("fileNumber", lclInbondForm.getLclInbond().getLclFileNumber().getFileNumber());
        inbondDetails(lclInbondForm, request);
        return mapping.findForward("newInBond");
    }

    public ActionForward saveInbond(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclInbondForm inbondForm = (LclInbondForm) form;
        LclBookingImportDAO importBkgDao = new LclBookingImportDAO();
        LclQuoteImportDAO importQuoteDao = new LclQuoteImportDAO();
        LclFileNumberDAO fileNoDao = new LclFileNumberDAO();
        WarehouseDAO whDao = new WarehouseDAO();
        Long fileId = inbondForm.getLclInbond().getLclFileNumber().getId();
        LclFileNumber fileNo = fileNoDao.findById(fileId);
        User user = getCurrentUser(request);
        String value = inbondForm.getCustomReleaseValue();
        if (null != fileNo && "Q".equals(fileNo.getState())) { // for quote file
            LclQuoteImport importQuote = importQuoteDao.findById(fileId);
            if (null == importQuote) {
                importQuote = new LclQuoteImport();
                importQuote.setLclFileNumber(fileNoDao.findById(fileId));
                importQuote.setFileNumberId(fileId);
                importQuote.setDestWhse(whDao.findById(17));
                importQuote.setExpressReleaseClause(false);
                importQuote.setEnteredBy(user);
                importQuote.setModifiedBy(user);
                importQuote.setEnteredDatetime(new Date());
                importQuote.setModifiedDatetime(new Date());
            }
            // set quote values
            importQuote.setInbondVia(inbondForm.getInbondVia().toUpperCase());
            importQuote.setDeclaredValueEstimated("Y".equals(inbondForm.getValueEstimated()));
            importQuote.setDeclaredWeightEstimated("Y".equals(inbondForm.getWeightEstimated()));
            importQuote.setEntryClass(inbondForm.getEntryClass().toUpperCase());
            importQuote.setItClass(inbondForm.getItClass().toUpperCase());
            if (CommonUtils.isNotEmpty(value)) {
                importQuote.setCustomsReleaseValue(new BigDecimal(value));
            } else {
                importQuote.setCustomsReleaseValue(null);
            }
            importQuoteDao.saveOrUpdate(importQuote);
            setInbond(inbondForm, request);
            request.setAttribute("importInbond", importQuote);
        } else { // for booking file
            LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
            LclBookingImport importBkg = importBkgDao.findById(fileId);
            if (null == importBkg) {
                importBkg = new LclBookingImport();
                importBkg.setLclFileNumber(fileNoDao.findById(fileId));
                importBkg.setFileNumberId(fileId);
                importBkg.setDestWhse(whDao.findById(17));
                importBkg.setExpressReleaseClause(false);
                importBkg.setEnteredBy(user);
                importBkg.setModifiedBy(user);
                importBkg.setEnteredDatetime(new Date());
                importBkg.setModifiedDatetime(new Date());
            }
            // set booking values
            importBkg.setInbondVia(inbondForm.getInbondVia().toUpperCase());
            importBkg.setDeclaredValueEstimated("Y".equals(inbondForm.getValueEstimated()));
            importBkg.setDeclaredWeightEstimated("Y".equals(inbondForm.getWeightEstimated()));
            importBkg.setEntryClass(inbondForm.getEntryClass().toUpperCase());
            importBkg.setItClass(inbondForm.getItClass().toUpperCase());
            importBkg.setPrintEntry7512(inbondForm.getPrintEntry7512());
            if (CommonUtils.isNotEmpty(value)) {
                importBkg.setCustomsReleaseValue(new BigDecimal(value));
            } else {
                importBkg.setCustomsReleaseValue(null);
            }
            importBkgDao.saveOrUpdate(importBkg);
            lclBookingDAO.updateModifiedDateTime(fileId,user.getUserId());
            setInbond(inbondForm, request);
            request.setAttribute("importInbond", importBkg);
        }
        request.setAttribute("lclInbondForm", inbondForm);
        return mapping.findForward("newInBond");
    }

    /**
     * ********** action methods ends ***********
     */
    /**
     * ********** other methods starts ***********
     */
    private void setInbond(LclInbondForm inbondForm, HttpServletRequest request) throws Exception {
        LclInbondsDAO inbondsDao = new LclInbondsDAO();
        request.setAttribute("inbondList", inbondsDao.findByProperty("lclFileNumber.id", inbondForm.getFileNumberId()));
        request.setAttribute("lclInbondForm", inbondForm);
    }

    private void inbondDetails(LclInbondForm inbondForm, HttpServletRequest request) throws Exception {
        LclBookingImportDAO importBkgDao = new LclBookingImportDAO();
        LclQuoteImportDAO importQuoteDao = new LclQuoteImportDAO();
        LclFileNumberDAO fileNoDao = new LclFileNumberDAO();
        Long fileId = inbondForm.getLclInbond().getLclFileNumber().getId();
        LclFileNumber fileNo = fileNoDao.findById(fileId);
        if (null != fileNo && "Q".equals(fileNo.getState())) { // for quote file
            LclQuoteImport importQuote = importQuoteDao.findById(fileId);
            request.setAttribute("importInbond", importQuote);
        } else {
            LclBookingImport importBkg = importBkgDao.findById(fileId);
            request.setAttribute("importInbond", importBkg);
        }
    }

    public ActionForward validateCodeFList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclImportUtils lclImportUtils = new LclImportUtils();
        List<ImportsManifestBean> codeFEmailList = lclImportUtils.getDrCodeFEmailList(request);
        String errorMsg = "";
        if (codeFEmailList.isEmpty()) {
            errorMsg = "Select Customer Type As Code_F Format and Add Email,Fax in Customer Contact";
        }
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(errorMsg);
        return null;
    }
}
