package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.ApReportsForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import com.logiware.bean.ReportBean;
import com.logiware.excel.ApReportsExcelCreator;
import com.logiware.hibernate.dao.ApReportsDAO;
import com.logiware.reports.ApReportsCreator;
import com.logiware.reports.ApStatementReportCreator;
import com.logiware.reports.ApStatementReportExcelCreator;
import com.oreilly.servlet.ServletUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

public class ApReportsAction extends DispatchAction implements ConstantsInterface {

    private String SUCCESS = "success";
    private String AGING = "Aging";
    private String ADJUSTEDACCRUALS = "AdjustedAccruals";
    private String VENDOR = "Vendor";
    private String ACTIVITY = "Activity";
    private String PAYMENT = "Payment";
    private String VOIDEDCHECK = "VoidedCheck";
    private String CHECKREGISTER = "CheckRegister";
    private String ACCOUNT = "Account";
    private String VOLUME = "Volume";
    private String DPO = "DPO";
    private String DISPUTEDITEMS = "DisputedItems";
    private String REJECTEDITEMS = "RejectedItems";
    private String TIMELAPSEBETWEENACCRUALENTRY = "TimeLapseBetweenAccrualEntry";
    private String STATEMENT = "Statement";

    public ActionForward showTabs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = (ApReportsForm) form;
        TreeMap<Integer, LabelValueBean> tabs = new TreeMap<Integer, LabelValueBean>();
        tabs.put(1, new LabelValueBean("Aging", "showAging"));
        tabs.put(2, new LabelValueBean("Adjusted Accruals", "showAdjustedAccruals"));
        tabs.put(3, new LabelValueBean("Vendor", "showVendor"));
        tabs.put(4, new LabelValueBean("Activity", "showActivity"));
        tabs.put(5, new LabelValueBean("Check Register", "showCheckRegister"));
        tabs.put(6, new LabelValueBean("Volume", "showVolume"));
        tabs.put(7, new LabelValueBean("DPO", "showDPO"));
        tabs.put(8, new LabelValueBean("Disputed Items", "showDisputedItems"));
        tabs.put(9, new LabelValueBean("Statement", "showStatement"));
        apReportsForm.setTabs(tabs);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward showAging(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        apReportsForm.setShowMaster(NO);
        apReportsForm.setShowDetailOrSummary(AP_DETAIL_REPORT);
        apReportsForm.setReportType(AP_AGING_REPORT);
        apReportsForm.setFilterByUser(NO);
        apReportsForm.setDivisionOrTerminal(DIVISION);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(AGING);
    }

    public ActionForward printAging(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, AGING);
    }

    public ActionForward exportAgingToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, AGING);
    }

    public ActionForward showAdjustedAccruals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_ADJUSTED_ACCRUALS_REPORT);
        apReportsForm.setFilterByUser(NO);
        apReportsForm.setDivisionOrTerminal(NO);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(ADJUSTEDACCRUALS);
    }

    public ActionForward printAdjustedAccruals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, ADJUSTEDACCRUALS);
    }

    public ActionForward exportAdjustedAccrualsToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, ADJUSTEDACCRUALS);
    }

    public ActionForward showVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_VENDOR_REPORT);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(VENDOR);
    }

    public ActionForward printVendor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, VENDOR);
    }

    public ActionForward exportVendorToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, VENDOR);
    }

    public ActionForward showActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_ACTIVITY_REPORT);
        apReportsForm.setFilterByUser(NO);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(ACTIVITY);
    }

    public ActionForward printActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, ACTIVITY);
    }

    public ActionForward exportActivityToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, ACTIVITY);
    }

    public ActionForward showPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_PAYMENT_REPORT);
        apReportsForm.setFilterByUser(NO);
        apReportsForm.setPaymentMethod(PAYMENT_METHOD_CHECK);
        apReportsForm.setDivisionOrTerminal(NO);
        apReportsForm.setVoidedCheck(NO);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(PAYMENT);
    }

    public ActionForward printPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, PAYMENT);
    }

    public ActionForward exportPaymentToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, PAYMENT);
    }

    public ActionForward showVoidedCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_VOIDED_CHECK_REPORT);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(VOIDEDCHECK);
    }

    public ActionForward printVoidedCheck(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, VOIDEDCHECK);
    }

    public ActionForward exportVoidedCheckToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, VOIDEDCHECK);
    }

    public ActionForward showCheckRegister(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_CHECK_REGISTER_REPORT);
        apReportsForm.setShowDetailOrSummary(AP_SUMMARY_REPORT);
        apReportsForm.setPaymentMethod(PAYMENT_METHOD_CHECK);
        apReportsForm.setCheckStatus(ALL);
        apReportsForm.setBankAccount(LoadLogisoftProperties.getProperty("main.bankAccount"));
        apReportsForm.setGlAccount(LoadLogisoftProperties.getProperty("main.glAccount"));
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(CHECKREGISTER);
    }

    public ActionForward printCheckRegister(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, CHECKREGISTER);
    }

    public ActionForward exportCheckRegisterToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, CHECKREGISTER);
    }

    public ActionForward showAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_ACCOUNT_REPORT);
        apReportsForm.setAccountFilterType("30days");
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(ACCOUNT);
    }

    public ActionForward printAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, ACCOUNT);
    }

    public ActionForward exportAccountToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, ACCOUNT);
    }

    public ActionForward showVolume(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setPaymentMethod(TRANSACTION_TYPE_PAYMENT_WITH_ACCOUNT_PAYABLE);
        apReportsForm.setReportType(AP_VOLUME_REPORT);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(VOLUME);
    }

    public ActionForward printVolume(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, VOLUME);
    }

    public ActionForward exportVolumeToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, VOLUME);
    }

    public ActionForward showDPO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_DPO_REPORT);
        apReportsForm.setFilterByUser(NO);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(DPO);
    }

    public ActionForward printDPO(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, DPO);
    }

    public ActionForward exportDPOToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, DPO);
    }

    public ActionForward showDisputedItems(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_DISPUTED_ITEMS_REPORT);
        apReportsForm.setFilterByUser(NO);
        apReportsForm.setDivisionOrTerminal(NO);
        apReportsForm.setShowInvoices(STATUS_OPEN);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(DISPUTEDITEMS);
    }

    public ActionForward showStatement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        request.setAttribute("UserList", new ApReportsDAO().getApSpecialists());
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(STATEMENT);
    }

    public ActionForward printDisputedItems(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, DISPUTEDITEMS);
    }

    public ActionForward exportDisputedItemsToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, DISPUTEDITEMS);
    }

    public ActionForward showRejectedItems(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_REJECTED_ITEMS_REPORT);
        apReportsForm.setFilterByUser(NO);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(REJECTEDITEMS);
    }

    public ActionForward printRejectedItems(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, REJECTEDITEMS);
    }

    public ActionForward exportRejectedItemsToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, REJECTEDITEMS);
    }

    public ActionForward showTimeLapseBetweenAccrualEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = new ApReportsForm();
        apReportsForm.setReportType(AP_TIME_LAPSE_REPORT);
        apReportsForm.setFilterByUser(NO);
        apReportsForm.setDivisionOrTerminal(NO);
        request.setAttribute("apReportsForm", apReportsForm);
        return mapping.findForward(TIMELAPSEBETWEENACCRUALENTRY);
    }

    public ActionForward printTimeLapseBetweenAccrualEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.printReport(mapping, form, request, TIMELAPSEBETWEENACCRUALENTRY);
    }

    public ActionForward exportTimeLapseBetweenAccrualEntryToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.exportToExcel(mapping, form, response, TIMELAPSEBETWEENACCRUALENTRY);
    }

    public ActionForward printStatement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = (ApReportsForm) form;
        ApReportsDAO apReportsDAO = new ApReportsDAO();
        Map<String, String> queries = apReportsDAO.buildQueries(apReportsForm);
        List<AccountingBean> transactions = apReportsDAO.getTransactions(apReportsForm, queries);
        String apQuery = queries.get("apQuery");
        String arQuery = queries.get("arQuery");
        String acQuery = queries.get("acQuery");
        CustomerBean apAgingBuckets = apReportsDAO.getPayableAgingBuckets(apQuery, acQuery);
        CustomerBean arAgingBuckets = null;
        if (null != arQuery) {
            arAgingBuckets = apReportsDAO.getReceivableAgingBuckets(arQuery);
        }
        Map<String, CustomerBean> agingBuckets = new HashMap<String, CustomerBean>();
        agingBuckets.put("arAgingBuckets", arAgingBuckets);
        agingBuckets.put("apAgingBuckets", apAgingBuckets);
        CustomerBean vendorDetails = null;
        if (apReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(apReportsForm.getApSpecialist())) {
        } else if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
            vendorDetails = apReportsDAO.getVendorDetails(apReportsForm.getVendorNumber());
        }
        String realPath = this.getServlet().getServletContext().getRealPath("/");
        request.setAttribute("reportFileName", new ApStatementReportCreator().createReport(realPath, transactions, vendorDetails, agingBuckets, apReportsForm));
        request.setAttribute("UserList", new ApReportsDAO().getApSpecialists());
        return mapping.findForward(STATEMENT);
    }

    public ActionForward exportStatementToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = (ApReportsForm) form;
        ApReportsDAO apReportsDAO = new ApReportsDAO();
        Map<String, String> queries = apReportsDAO.buildQueries(apReportsForm);
        List<AccountingBean> transactions = apReportsDAO.getTransactions(apReportsForm, queries);
        String apQuery = queries.get("apQuery");
        String arQuery = queries.get("arQuery");
        String acQuery = queries.get("acQuery");
        CustomerBean apAgingBuckets = apReportsDAO.getPayableAgingBuckets(apQuery, acQuery);
        CustomerBean arAgingBuckets = null;
        if (null != arQuery) {
            arAgingBuckets = apReportsDAO.getReceivableAgingBuckets(arQuery);
        }
        Map<String, CustomerBean> agingBuckets = new HashMap<String, CustomerBean>();
        agingBuckets.put("arAgingBuckets", arAgingBuckets);
        agingBuckets.put("apAgingBuckets", apAgingBuckets);
        CustomerBean vendorDetails = null;
        if (apReportsForm.isAllCustomers() || CommonUtils.isNotEmpty(apReportsForm.getApSpecialist())) {
        } else if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
            vendorDetails = apReportsDAO.getVendorDetails(apReportsForm.getVendorNumber());
        }
        request.setAttribute("UserList", new ApReportsDAO().getApSpecialists());
        String excelFileName = new ApStatementReportExcelCreator(apReportsForm).createExcel(transactions, vendorDetails, agingBuckets);
        if (CommonUtils.isNotEmpty(excelFileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(excelFileName, response.getOutputStream());
            return null;
        }
        return mapping.findForward(STATEMENT);
    }

    private ActionForward printReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, String forwardName) throws Exception {
        ApReportsForm apReportsForm = (ApReportsForm) form;
        String realPath = this.getServlet().getServletContext().getRealPath("/");
        request.setAttribute("reportFileName", new ApReportsCreator(apReportsForm, realPath).createReport());
        return mapping.findForward(forwardName);
    }

    private ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletResponse response, String forwardName) throws Exception {
        ApReportsForm apReportsForm = (ApReportsForm) form;
        String excelFileName = new ApReportsExcelCreator(apReportsForm).createExcel();
        if (CommonUtils.isNotEmpty(excelFileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(excelFileName, response.getOutputStream());
            return null;
        }
        return mapping.findForward(forwardName);
    }

    public ActionForward getForDisputedEmailLog(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = (ApReportsForm) form;
        ApReportsDAO apReportsDAO = new ApReportsDAO();
        String conditions = apReportsDAO.buildDisputedEmailLogQuery(apReportsForm);
        Integer totalPageSize = apReportsDAO.getTotalDisputedEmailLogs(conditions);
        int noOfPages = totalPageSize / apReportsForm.getCurrentPageSize();
        int remainSize = totalPageSize % apReportsForm.getCurrentPageSize();
        if (remainSize > 0) {
            noOfPages += 1;
        }
        int start = (apReportsForm.getCurrentPageSize() * (apReportsForm.getPageNo() - 1));
        int end = apReportsForm.getCurrentPageSize();
        apReportsForm.setNoOfPages(noOfPages);
        apReportsForm.setTotalPageSize(totalPageSize);
        List<ReportBean> disputedEmailLogs = apReportsDAO.getDisputedEmailLogs(conditions, start, end, true);
        apReportsForm.setNoOfRecords(disputedEmailLogs.size());
        apReportsForm.setDisputedEmailLogList(disputedEmailLogs);
        return mapping.findForward(DISPUTEDITEMS);
    }

    public ActionForward exportEmailLogToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ApReportsForm apReportsForm = (ApReportsForm) form;
        apReportsForm.setReportType(AP_DISPUTED_EMAIL_LOG_REPORT);
        String excelFileName = new ApReportsExcelCreator(apReportsForm).createExcel();
        if (CommonUtils.isNotEmpty(excelFileName)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(excelFileName, response.getOutputStream());
            return null;
        }
        return mapping.findForward(DISPUTEDITEMS);
    }
}
