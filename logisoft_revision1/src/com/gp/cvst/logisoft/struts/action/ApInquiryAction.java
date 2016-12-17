package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cvst.logisoft.struts.form.ApInquiryForm;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.bean.AccountingBean;
import com.logiware.excel.ExportApInquiryToExcel;
import com.logiware.hibernate.dao.ApInquiryDAO;
import com.logiware.reports.ApInquiryReportCreator;
import com.logiware.utils.ArBatchUtils;
import com.oreilly.servlet.ServletUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class ApInquiryAction extends DispatchAction implements ConstantsInterface {

    public ActionForward getForApInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        ApInquiryForm apInquiryForm = (ApInquiryForm) form;
        ApInquiryDAO apInquiryDAO = new ApInquiryDAO();
        boolean noAccruals = true;
        boolean noInvoices = true;
        if (CommonUtils.isEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isEmpty(apInquiryForm.getSearchValue())
                && (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), ONLY))) {
            noAccruals = false;
        }
        if (CommonUtils.isEqual(apInquiryForm.getSearchBy(), "0") && CommonUtils.isEmpty(apInquiryForm.getSearchValue())
                && CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), ONLY)) {
            noInvoices = false;
        }
        String apQuery = null;
        String arQuery = null;
        String acQuery = null;
        String apInvoiceQuery = null;
        String ediInvoiceQuery = null;
        if (noAccruals && noInvoices) {
            apQuery = apInquiryDAO.buildApQuery(apInquiryForm);
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAR(), YES)
                    || CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), YES)
                    || CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
                arQuery = apInquiryDAO.buildArQuery(apInquiryForm);
            }
        }
        if (((CommonUtils.isNotEqual(apInquiryForm.getSearchBy(), "0")
                && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())
                && CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), NO))
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), ONLY))
                && CommonUtils.isNotEqual(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
            acQuery = apInquiryDAO.buildAcQuery(apInquiryForm);
        }
        if (((CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)
                && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue()))
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowRejectedInvoices(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowRejectedInvoices(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), ONLY))) {
            apInvoiceQuery = apInquiryDAO.buildApInvoiceQuery(apInquiryForm);
        }
        if ((((CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)
                && CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER))
                && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())
                && CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), NO))
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY))
                && CommonUtils.isNotEqual(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
            ediInvoiceQuery = apInquiryDAO.buildEdiInvoiceQuery(apInquiryForm);
        }
        if (null != apQuery || null != acQuery || null != arQuery || null != apInvoiceQuery) {
            Integer totalPageSize = apInquiryDAO.getTotalPageSize(apInquiryForm, arQuery, apQuery, acQuery, apInvoiceQuery, ediInvoiceQuery);
            int noOfPages = totalPageSize / apInquiryForm.getCurrentPageSize();
            int remainSize = totalPageSize % apInquiryForm.getCurrentPageSize();
            if (remainSize > 0) {
                noOfPages += 1;
            }
            apInquiryForm.setNoOfPages(noOfPages);
            apInquiryForm.setTotalPageSize(totalPageSize);
            List<AccountingBean> apInquiryList = apInquiryDAO.search(apInquiryForm, apQuery, acQuery, arQuery, apInvoiceQuery, ediInvoiceQuery);
            apInquiryForm.setNoOfRecords(apInquiryList.size());
            apInquiryForm.setApInquiryList(apInquiryList);
        }
        apInquiryForm.setVendor(apInquiryDAO.getCustomerDetails(apInquiryForm, arQuery, apQuery, acQuery, apInvoiceQuery, ediInvoiceQuery));
        return mapping.findForward("success");
    }

    public ActionForward getInvoiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInquiryForm apInquiryForm = (ApInquiryForm) form;
        String vendorNumber = apInquiryForm.getVendorNumber();
        String invoiceNumber = apInquiryForm.getInvoiceNumber();
        String blNumber = apInquiryForm.getBlNumber();
        String transactionType = apInquiryForm.getTransactionType();
        request.setAttribute("invoiceDetails", new AccrualsDAO().getInvoiceDetails(vendorNumber, invoiceNumber, blNumber, transactionType));
        return mapping.findForward("invoiceDetails");
    }

    public ActionForward printApInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        ApInquiryForm apInquiryForm = (ApInquiryForm) form;
        ApInquiryDAO apInquiryDAO = new ApInquiryDAO();
        boolean noAccruals = true;
        boolean noInvoices = true;
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), ONLY)) {
            noAccruals = false;
        }
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), ONLY)) {
            noInvoices = false;
        }
        String apQuery = null;
        String arQuery = null;
        String acQuery = null;
        String apInvoiceQuery = null;
        String ediInvoiceQuery = null;
        if (noAccruals && noInvoices) {
            apQuery = apInquiryDAO.buildApQuery(apInquiryForm);
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAR(), YES)) {
                arQuery = apInquiryDAO.buildArQuery(apInquiryForm);
            }
        }
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), ONLY)) {
            acQuery = apInquiryDAO.buildAcQuery(apInquiryForm);
        }
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), ONLY)) {
            apInvoiceQuery = apInquiryDAO.buildApInvoiceQuery(apInquiryForm);
        }
        if ((((CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)
                && CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER))
                && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())
                && CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), NO))
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY))
                && CommonUtils.isNotEqual(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
            ediInvoiceQuery = apInquiryDAO.buildEdiInvoiceQuery(apInquiryForm);
        }
        if (null != apQuery || null != acQuery || null != arQuery || null != apInvoiceQuery) {
            List<AccountingBean> apInquiryList = apInquiryDAO.search(apInquiryForm, apQuery, acQuery, arQuery, apInvoiceQuery, ediInvoiceQuery);
            apInquiryForm.setApInquiryList(apInquiryList);
            apInquiryForm.setVendor(apInquiryDAO.getCustomerDetails(apInquiryForm, arQuery, apQuery, acQuery, apInvoiceQuery, ediInvoiceQuery));
            apInquiryForm.setFileName(new ApInquiryReportCreator().createReport(apInquiryForm, this.servlet.getServletContext().getRealPath("/")));
            Integer totalPageSize = apInquiryDAO.getTotalPageSize(apInquiryForm, arQuery, apQuery, acQuery, apInvoiceQuery, ediInvoiceQuery);
            int noOfPages = totalPageSize / apInquiryForm.getCurrentPageSize();
            int remainSize = totalPageSize % apInquiryForm.getCurrentPageSize();
            if (remainSize > 0) {
                noOfPages += 1;
            }
            apInquiryForm.setNoOfPages(noOfPages);
            apInquiryForm.setTotalPageSize(totalPageSize);
            apInquiryList = apInquiryDAO.search(apInquiryForm, apQuery, acQuery, arQuery, apInvoiceQuery, ediInvoiceQuery);
            apInquiryForm.setNoOfRecords(apInquiryList.size());
            apInquiryForm.setApInquiryList(apInquiryList);
        }
        return mapping.findForward("success");
    }

    public ActionForward exportToExcelApInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInquiryForm apInquiryForm = (ApInquiryForm) form;
        ApInquiryDAO apInquiryDAO = new ApInquiryDAO();
        boolean noAccruals = true;
        boolean noInvoices = true;
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), ONLY)) {
            noAccruals = false;
        }
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), ONLY)) {
            noInvoices = false;
        }
        String apQuery = null;
        String arQuery = null;
        String acQuery = null;
        String apInvoiceQuery = null;
        String ediInvoiceQuery = null;
        if (noAccruals && noInvoices) {
            apQuery = apInquiryDAO.buildApQuery(apInquiryForm);
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAR(), YES)) {
                arQuery = apInquiryDAO.buildArQuery(apInquiryForm);
            }
        }
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowAssignedAccruals(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInactiveAccruals(), ONLY)) {
            acQuery = apInquiryDAO.buildAcQuery(apInquiryForm);
        }
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), ONLY)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), ONLY)) {
            apInvoiceQuery = apInquiryDAO.buildApInvoiceQuery(apInquiryForm);
        }

        if ((((CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER)
                && CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), SEARCH_BY_INVOICE_NUMBER))
                && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())
                && CommonUtils.isEqual(apInquiryForm.getShowRejectedInvoices(), NO))
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), YES)
                || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), ONLY))
                && CommonUtils.isNotEqual(apInquiryForm.getShowRejectedInvoices(), ONLY)) {
            ediInvoiceQuery = apInquiryDAO.buildEdiInvoiceQuery(apInquiryForm);
        }
        if (null != apQuery || null != acQuery || null != arQuery || null != apInvoiceQuery) {
            List<AccountingBean> apInquiryList = apInquiryDAO.search(apInquiryForm, apQuery, acQuery, arQuery, apInvoiceQuery, ediInvoiceQuery);
            apInquiryForm.setApInquiryList(apInquiryList);
            apInquiryForm.setVendor(apInquiryDAO.getCustomerDetails(apInquiryForm, arQuery, apQuery, acQuery, apInvoiceQuery, ediInvoiceQuery));
            String fileName = new ExportApInquiryToExcel().exportToExcel(apInquiryForm);
            if (CommonUtils.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
                response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
                ServletUtils.returnFile(fileName, response.getOutputStream());
                return null;
            } else {
                apInquiryForm.setMessage("Ap Inquiry Sheet export failed");
            }
        }
        return mapping.findForward("success");
    }

    public ActionForward refreshApInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInquiryForm apInquiryForm = new ApInquiryForm();
        request.setAttribute("apInquiryForm", apInquiryForm);
        return mapping.findForward("success");
    }

    public ActionForward resetApInquiry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInquiryForm apInquiryForm = (ApInquiryForm) form;
        ApInquiryForm newApInquiryForm = new ApInquiryForm();
        newApInquiryForm.setVendorName(apInquiryForm.getVendorName());
        newApInquiryForm.setVendorNumber(apInquiryForm.getVendorNumber());
        request.setAttribute("apInquiryForm", newApInquiryForm);
        return mapping.findForward("success");
    }

    public ActionForward exportNSInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInquiryForm apInquiryForm = (ApInquiryForm) form;
        ArBatchUtils.exportNSInvoice(apInquiryForm.getBatchId(), response);
        return getForApInquiry(mapping, form, request, response);
    }

    public ActionForward printNSInvoice(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApInquiryForm apInquiryForm = (ApInquiryForm) form;
        String contextPath = this.getServlet().getServletContext().getRealPath("/");
        ArBatchUtils.printNSInvoice(apInquiryForm.getBatchId(), contextPath, request);
        return getForApInquiry(mapping, form, request, response);
    }
}
