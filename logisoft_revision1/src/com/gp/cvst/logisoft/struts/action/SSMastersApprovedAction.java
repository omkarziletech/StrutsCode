/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.tradingpartner.APConfigurationBC;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.dwr.DwrUtil;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsRemarksDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.struts.form.SSMastersApprovedForm;
import com.logiware.accounting.dao.FclManifestDAO;
import java.util.List;
import com.logiware.bean.AccountingBean;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.SSMastersApprovedDAO;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.apache.struts.actions.DispatchAction;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Administrator
 */
public class SSMastersApprovedAction extends DispatchAction {

    private static String SS_MASTERS_APPROVED_FORM = "ssMastersApprovedForm";
    private static String FORWARD_TO_SS_MASTERS_APPROVED = "ssMastersApproved";
    private static String SS_MASTERS_APPROVED_SCREEN = "ssMastersApprovedScreen";
    private static String FORWARD_TO_ACCRUALS = "accruals";

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SSMastersApprovedForm ssMastersApprovedForm = new SSMastersApprovedForm();
        request.setAttribute(SS_MASTERS_APPROVED_FORM, ssMastersApprovedForm);
        return mapping.findForward(FORWARD_TO_SS_MASTERS_APPROVED);
    }

    public ActionForward searchSSMastersApproved(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SSMastersApprovedForm ssMastersApprovedForm = (SSMastersApprovedForm) form;
        SSMastersApprovedDAO ssMastersApprovedDAO = new SSMastersApprovedDAO();
        String conditions = ssMastersApprovedDAO.buildQuery(ssMastersApprovedForm);
        Integer totalPageSize = ssMastersApprovedDAO.getNoOfRecords(conditions);
        int noOfPages = totalPageSize / ssMastersApprovedForm.getCurrentPageSize();
        int remainSize = totalPageSize % ssMastersApprovedForm.getCurrentPageSize();
        if (remainSize > 0) {
            noOfPages++;
        }
        int start = ssMastersApprovedForm.getCurrentPageSize() * (ssMastersApprovedForm.getPageNo() - 1);
        int end = ssMastersApprovedForm.getCurrentPageSize();
        ssMastersApprovedForm.setNoOfPages(noOfPages);
        ssMastersApprovedForm.setTotalPageSize(totalPageSize);
        List<AccountingBean> ssMastersApprovedList = ssMastersApprovedDAO.search(conditions, ssMastersApprovedForm.getSortBy(), ssMastersApprovedForm.getOrderBy(), start, end);
        ssMastersApprovedForm.setNoOfRecords(ssMastersApprovedList.size());
        ssMastersApprovedForm.setSsMastersApprovedList(ssMastersApprovedList);
        return mapping.findForward(FORWARD_TO_SS_MASTERS_APPROVED);
    }
    public ActionForward searchLCLSSMastersApproved(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SSMastersApprovedForm ssMastersApprovedForm = (SSMastersApprovedForm) form;
        SSMastersApprovedDAO ssMastersApprovedDAO = new SSMastersApprovedDAO();
        String conditions = ssMastersApprovedDAO.buildLCLQuery(ssMastersApprovedForm);
        Integer totalPageSize = ssMastersApprovedDAO.getNoOfRecordsForLCL(conditions);
        int noOfPages = totalPageSize / ssMastersApprovedForm.getCurrentPageSize();
        int remainSize = totalPageSize % ssMastersApprovedForm.getCurrentPageSize();
        if (remainSize > 0) {
            noOfPages++;
        }
        int start = ssMastersApprovedForm.getCurrentPageSize() * (ssMastersApprovedForm.getPageNo() - 1);
        int end = ssMastersApprovedForm.getCurrentPageSize();
        ssMastersApprovedForm.setNoOfPages(noOfPages);
        ssMastersApprovedForm.setTotalPageSize(totalPageSize);
        if("bl.file_no".equalsIgnoreCase(ssMastersApprovedForm.getSortBy())){
            ssMastersApprovedForm.setSortBy("header.schedule_no");
        }
        List<AccountingBean> ssMastersApprovedList = ssMastersApprovedDAO.searchLCL(conditions, ssMastersApprovedForm.getSortBy(), ssMastersApprovedForm.getOrderBy(), start, end);
        ssMastersApprovedForm.setNoOfRecords(ssMastersApprovedList.size());
        ssMastersApprovedForm.setSsMastersApprovedList(ssMastersApprovedList);
        return mapping.findForward(FORWARD_TO_SS_MASTERS_APPROVED);
    }
    public ActionForward searchALLSSMastersApproved(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        SSMastersApprovedForm ssMastersApprovedForm = (SSMastersApprovedForm) form;
        SSMastersApprovedDAO ssMastersApprovedDAO = new SSMastersApprovedDAO();
        String conditions = ssMastersApprovedDAO.buildQuery(ssMastersApprovedForm);
        String conditions1 = ssMastersApprovedDAO.buildLCLQuery(ssMastersApprovedForm);
        Integer totalPageSize = ssMastersApprovedDAO.getNoOfRecords(conditions);
        totalPageSize = totalPageSize+ssMastersApprovedDAO.getNoOfRecordsForLCL(conditions1);
        int noOfPages = totalPageSize / ssMastersApprovedForm.getCurrentPageSize();
        int remainSize = totalPageSize % ssMastersApprovedForm.getCurrentPageSize();
        if (remainSize > 0) {
            noOfPages++;
        }
        int start = ssMastersApprovedForm.getCurrentPageSize() * (ssMastersApprovedForm.getPageNo() - 1);
        int end = ssMastersApprovedForm.getCurrentPageSize();
        ssMastersApprovedForm.setNoOfPages(noOfPages);
        ssMastersApprovedForm.setTotalPageSize(totalPageSize);
        if("bl.file_no".equalsIgnoreCase(ssMastersApprovedForm.getSortBy())){
            ssMastersApprovedForm.setSortBy("file_no");
        }
        List<AccountingBean> ssMastersApprovedList = ssMastersApprovedDAO.searchAll(conditions,conditions1, ssMastersApprovedForm.getSortBy(), ssMastersApprovedForm.getOrderBy(), start, end);
        ssMastersApprovedForm.setNoOfRecords(ssMastersApprovedList.size());
        ssMastersApprovedForm.setSsMastersApprovedList(ssMastersApprovedList);
        
        return mapping.findForward(FORWARD_TO_SS_MASTERS_APPROVED);
    }
    public ActionForward convertToAp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SSMastersApprovedForm ssMastersApprovedForm = (SSMastersApprovedForm) form;
        DBUtil dBUtil = new DBUtil();
        FclBlDAO fclBlDAO = new FclBlDAO();
        String fileNo = ssMastersApprovedForm.getSelectedFileNo();
        FclBl bl = fclBlDAO.getFileNoObject(fileNo);
        User user = (User) request.getSession().getAttribute("loginuser");
        new FclManifestDAO(bl, user).postAccrualsBeforeManifest();
        String vendorName = "C-Collect".equals(bl.getStreamShipBl()) ? bl.getAgent() : bl.getSslineName();
        String vendorNo = "C-Collect".equals(bl.getStreamShipBl()) ? bl.getAgentNo() : bl.getSslineNo();
        String blNumber = CommonUtils.isNotEmpty(bl.getNewMasterBL()) ? bl.getNewMasterBL() : bl.getBolId();
        new SSMastersApprovedDAO().copySSMasterApprovedDocumentsToAp(vendorNo, fileNo, blNumber);
        AccrualsForm accrualsForm = new AccrualsForm();
        accrualsForm.setCustname(vendorName);
        accrualsForm.setVendor(vendorName);
        accrualsForm.setVendornumber(vendorNo);
        accrualsForm.setNumber(vendorNo);
        accrualsForm.setInvoicenumber(blNumber);
        String invoiceDate = DateUtils.formatDate(bl.getSailDate(), "MM/dd/yyyy HH:mm");
        accrualsForm.setInvoicedate(invoiceDate);
        Vendor vendor = new APConfigurationBC().getVendorByCustomerNumber(vendorNo);
        String termDesc = null;
        if (null != vendor && null != vendor.getCterms()) {
            accrualsForm.setTerm(vendor.getCterms().getId().toString());
            termDesc = vendor.getCterms().getCodedesc();
        } else {
            accrualsForm.setTerm("11344");
            termDesc = "Due Upon Receipt";
        }
        String dueDate = new DwrUtil().dueDateCalculation(termDesc, invoiceDate);
        if (CommonUtils.isNotEmpty(dueDate)) {
            JSONTokener jsonTokener = new JSONTokener(dueDate);
            JSONObject jsonObject = new JSONObject(jsonTokener);
            accrualsForm.setDuedate((String) jsonObject.get("newDate"));
        }
        accrualsForm.setCategory(CommonConstants.SEARCH_BY_DOCK_RECEIPT);
        accrualsForm.setDocNumber(bl.getFileNo());
        List<TransactionBean> accruals = new AccountingLedgerDAO().getFclBlAccruals(bl.getBolId());
        if (CommonUtils.isNotEmpty(accruals)) {
            int totalPageSize = accruals.size();
            int totalPages = totalPageSize / 100;
            int remainSize = totalPageSize % 100;
            if (remainSize > 0) {
                totalPages += 1;
            }
            request.setAttribute("totalRecords", totalPageSize);
            request.setAttribute("currentPageNo", 1);
            request.setAttribute("currentPageSize", 100);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalPageSize", totalPageSize);
            request.setAttribute("accruals", accruals);
            accrualsForm.setTotalPageSize(totalPageSize);
        }
        request.setAttribute("invoiceFlag", false);
        request.setAttribute("from", SS_MASTERS_APPROVED_SCREEN);
        request.setAttribute("termDesc", termDesc);
        request.setAttribute("accrualsForm", accrualsForm);
        request.setAttribute("latestUploadedFileName", new DocumentStoreLogDAO().getLatestDocument("INVOICE", "INVOICE", vendorNo + "-" + blNumber));
        request.setAttribute("docTypeList", dBUtil.getSearchType(CommonConstants.PAGE_ACCRUALS));
        return mapping.findForward(FORWARD_TO_ACCRUALS);
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SSMastersApprovedForm ssMastersApprovedForm = (SSMastersApprovedForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        PrintWriter out = response.getWriter();
        try {
            FclBlDAO fclBlDAO = new FclBlDAO();
            String fileNo = ssMastersApprovedForm.getSelectedFileNo();
            FclBl fclBl = fclBlDAO.getOriginalBl(fileNo);
            fclBl.setConvertedToAp(true);
            fclBlDAO.update(fclBl);
            Notes notes = new Notes();
            notes.setModuleId("FILE");
            notes.setModuleRefId(fileNo);
            notes.setNoteType("manual");
            notes.setNoteDesc("Removed from SS Master Approved screen");
            notes.setUpdateDate(new Date());
            notes.setUpdatedBy(loginUser.getLoginName());
            new NotesDAO().save(notes);
            out.print("success");
        } catch (Exception e) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            printWriter.flush();
            result.flush();
            out.print(result.toString());
            printWriter.close();
            result.close();
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }
    public ActionForward removeLclSsMasterApproved(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SSMastersApprovedForm ssMastersApprovedForm = (SSMastersApprovedForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        PrintWriter out = response.getWriter();
        try {
            LclSSMasterBlDAO lclSSMasterBlDAO = new LclSSMasterBlDAO();
            if(null != ssMastersApprovedForm.getMasterNo()){
                LclSSMasterBl lclSSMasterBl = lclSSMasterBlDAO.findBkgNo(Long.parseLong(ssMastersApprovedForm.getMasterNo()), ssMastersApprovedForm.getSelectedFileNo());
                if(null != lclSSMasterBl){
                    lclSSMasterBl.setConvertedToAp(true);
                    lclSSMasterBlDAO.update(lclSSMasterBl);
                    new LclSsRemarksDAO().insertLclSSRemarks(Long.parseLong(ssMastersApprovedForm.getMasterNo()), "manual",null, "Removed from SS Master Approved screen", null, loginUser.getUserId());
                }
            }
            out.print("success");
        } catch (Exception e) {
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            printWriter.flush();
            result.flush();
            out.print(result.toString());
            printWriter.close();
            result.close();
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return null;
    }
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SSMastersApprovedForm ssMastersApprovedForm = new SSMastersApprovedForm();
        request.setAttribute("ssMastersApprovedForm", ssMastersApprovedForm);
        return clear(mapping, form, request, response);
    }
}
