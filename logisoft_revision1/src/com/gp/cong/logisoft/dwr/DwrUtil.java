package com.gp.cong.logisoft.dwr;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.LabelValueBean;
import org.directwebremoting.WebContextFactory;
import org.json.JSONObject;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.accounting.APPaymentBC;
import com.gp.cong.logisoft.bc.accounting.AccountPayableBC;
import com.gp.cong.logisoft.bc.accounting.CheckRegisterBC;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.scan.ScanBC;
import com.gp.cong.logisoft.bc.tradingpartner.APConfigurationBC;
import com.gp.cong.logisoft.domain.APSpecialistTradingPartner;
import com.gp.cong.logisoft.domain.CollectorTradingPartner;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PaymentMethod;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.ScanConfig;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.UserAgentInformation;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ScanDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import com.gp.cong.logisoft.reports.ApPaymentPdfCreator;
import com.gp.cong.logisoft.struts.form.ScanForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.util.StringFormatter;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.logiware.accounting.domain.ApInvoice;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.FileNumberForQuotaionBLBooking;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.ApInvoiceDAO;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCorrectionsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.PaymentsDAO;
import com.gp.cvst.logisoft.hibernate.dao.SubledgerDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.action.RemarksLookUpAction;
import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import com.gp.cvst.logisoft.struts.form.CheckRegisterForm;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.excel.ExportAccrualsToExcel;
import com.logiware.hibernate.dao.AccountingLedgerDAO;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ApTransactionHistoryDAO;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.reports.AccrualsReportCreator;
import com.logiware.utils.AuditNotesUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;
import org.json.JSONTokener;

public class DwrUtil {

    DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    DBUtil dbUtil = new DBUtil();
    TransactionDAO transactionDAO = new TransactionDAO();
    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
    FclBlDAO fclBlDAO = new FclBlDAO();
    UserDAO userDAO = new UserDAO();
    DocumentStoreLog documentStoreLog = new DocumentStoreLog();
    TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
    APPaymentBC appPaymentBC = new APPaymentBC();
    CustAddressDAO custAddressDAO = new CustAddressDAO();
    CustAddress custAddress = new CustAddress();

    /**
     * @param scanForm
     * @return Document List for Scan and Attach. Forward to
     * DocumentListTemplate.jsp
     */
    public String getDocumentStoreLog(ScanForm scanForm) throws Exception {

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        List<DocumentStoreLog> documentList = documentStoreLogDAO.getDocumentStoreLog(scanForm.getFileNumber(), scanForm.getScreenName(), scanForm.getDocumentName());
        request.setAttribute("documentList", documentList);
        return WebContextFactory.get().forwardToString(
                "/jsps/Scan/DocumentListTemplate.jsp");
    }

    public String getDocumentStoreLogForViewAll(ScanForm scanForm) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        List<DocumentStoreLog> documentList = documentStoreLogDAO.getDocumentStoreLogForViewAll(scanForm.getFileNumber(), scanForm.getScreenName());
        request.setAttribute("documentList", documentList);
        return WebContextFactory.get().forwardToString(
                "/jsps/Scan/DocumentListTemplate.jsp");
    }

    public String getQuickRates() throws Exception {
        return WebContextFactory.get().forwardToString(
                "/jsps/fclQuotes/QuickRates.jsp");
    }

    public void getRemarksLookUp(String desc, String currentPageNo,String importFlag, HttpServletRequest request) throws Exception {
        Integer totalSize = 0;
        Integer currentPageSize = 10;
         int totalPages= 0;
        new RemarksLookUpAction();
        List remarksList = new ArrayList();
        String remarks = "", codeType = "63", codeDesc = "";//for imports
        String codeTypeE ="53";
        codeDesc = CommonUtils.isNotEmpty(desc) ? desc : "";
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        if(importFlag.equalsIgnoreCase("true")){
        totalSize = genericDAO.importRemarksSize(remarks, codeDesc, codeType);
        remarksList = genericDAO.findForChargeCodesForAirRates(remarks, codeDesc, codeType, Integer.parseInt(currentPageNo), currentPageSize);
        totalPages = totalSize / currentPageSize;
        int remainSize = totalSize % currentPageSize;
        if (remainSize > 0) {
            totalPages += 1;
        }
        }else{
         totalSize = genericDAO.importRemarksSize(remarks, codeDesc, codeTypeE);
        remarksList = genericDAO.findForChargeCodesForAirRates(remarks, codeDesc, codeTypeE, Integer.parseInt(currentPageNo), currentPageSize);
        totalPages = totalSize / currentPageSize;
        int remainSize = totalSize % currentPageSize;
        if (remainSize > 0) {
            totalPages += 1;
        }
        }
        request.setAttribute("totalSize", totalSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPageNo", currentPageNo);
        request.setAttribute("currentPageSize", currentPageSize);
        request.getSession().setAttribute("remarksList", remarksList);//going to form           
    }

    public String saveImportRemarks(String desc) throws Exception {
        String codeDesc = CommonUtils.isNotEmpty(desc) ? desc.toUpperCase() : "";
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        List newList = genericDAO.getLastRecords("63");
        String code = null;
        if (CommonFunctions.isNotNullOrNotEmpty(newList)) {
            Integer genericCodeID = (Integer) newList.get(0);
            if (genericCodeID != null) {
                GenericCode generic = genericDAO.findById(genericCodeID);
                if (generic.getCode() != null) {
                    String[] values = generic.getCode().split("PR0");
                    if (values != null && values.length > 1) {
                        Integer number = new Integer(values[1]);
                        number = number + 1;
                        code = values[0] + "PR0" + number;
                    } else {
                        code = "IPR050";
                    }
                }
            }
        } else {
            code = "IPR050";
        }
        GenericCode genericCode = new GenericCode();
        genericCode.setCodedesc(codeDesc);
        genericCode.setCodetypeid(63);
        genericCode.setCode(code);
        genericDAO.save(genericCode);
        return "Saved";
    }

    public void addRemarks(String desc) throws Exception {
    }

    public String getSsMasterAtachmentList(String fileNo) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        List<DocumentStoreLog> documentList = documentStoreLogDAO.getDocumentStoreLog(fileNo, CommonConstants.FCLFILE, CommonConstants.SS_MASTER_BL);
        request.setAttribute("documentList", documentList);
        return WebContextFactory.get().forwardToString(
                "/jsps/Scan/DocumentListTemplate.jsp");
    }

    /**
     * @param scanForm
     * @return Document List for given FileNumber,ScreenName,DocumentType
     */
    public List getDocumentByDocumentIdAndScreenName(ScanForm scanForm) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        return documentStoreLogDAO.getDocumentStoreLog(scanForm.getFileNumber(), scanForm.getScreenName(), scanForm.getDocumentName());
    }

    /**
     * @param action
     * @param index
     * @return Forward to ScanCommentsTemplate.jsp for getting comments and file
     * upload based on action
     */
    public void getCommentsForScanOrAttach(String action, String index, String documentName, String fileNumber, String screenName, HttpServletRequest request) throws Exception {
        request.setAttribute("action", action);
        request.setAttribute("index", index);
        request.setAttribute("documentName", documentName);
        request.setAttribute("fileNumber", fileNumber);
        request.setAttribute("screenName", screenName);
    }

    public String getAttachmentList() throws Exception {

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        String path = LoadLogisoftProperties.getProperty("attachFileLocation");
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        List<File> attachmentList = Arrays.asList(directory.listFiles());
        request.setAttribute("attachmentList", attachmentList);
        return WebContextFactory.get().forwardToString(
                "/jsps/Scan/AttachFileListTemplate.jsp");
    }

    public String getCostCodebyCostCode(String costCode) throws Exception {
        String result = genericCodeDAO.getCostCodebyCostCode(costCode);
        return result;
    }

    public String getBlNumberbyBLNumber(String blNumber) throws Exception {
        String result = fclBlDAO.getBlNumberbyBLNumber(blNumber);
        return result;
    }

    public Boolean openPDF(String file) throws Exception {
        Boolean success = false;
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file);   //open the file chart.pdf
        success = true;
        return success;
    }

    public void findUser(String firstName, String lastName, String email, String role, HttpServletRequest request) throws Exception {
        List<User> userList = userDAO.findUser(firstName, lastName, email);
        if (null != role && role.trim().equals("Collector")) {
            request.setAttribute("collectorList", userList);
        } else {
            request.setAttribute("apSpecialistList", userList);
        }
    }

    public void showAssignWindow(String userId, String role, HttpServletRequest request) throws Exception {
        User user = userDAO.findById(Integer.parseInt(userId));
        if (null != role && role.trim().equals("Collector")) {
            request.setAttribute("collectorTradingPartners", user.getCollectorTradingPartners());
        } else {
            TreeSet<String> subTypes = new TreeSet<String>();
            for (LabelValueBean labelValueBean : dbUtil.getSubTypeList()) {
                if (CommonUtils.isNotEqual(labelValueBean.getLabel(), "Select One")) {
                    subTypes.add(labelValueBean.getValue());
                }
            }
            String subType = userDAO.getAllAssignedSubTypes(Integer.parseInt(userId));
            subTypes.removeAll(Arrays.asList(StringUtils.split(subType, ",")));
            for (APSpecialistTradingPartner aPSpecialistTradingPartner : user.getApSpecialistTradingPartners()) {
                subTypes.remove(aPSpecialistTradingPartner.getSubType());
            }
            request.setAttribute("assignedsubTypes", user.getApSpecialistTradingPartners());
            request.setAttribute("availableSubTypes", subTypes);
        }
        request.setAttribute("userId", userId);
    }

    public boolean assignCollectorToTradingPartner(String collectorTradingPartnerId, String userId, String startRange, String endRange) throws Exception {
        if (userDAO.checkRangeForCollector(Integer.parseInt(userId), startRange.toUpperCase(), endRange.toUpperCase())) {
            User user = userDAO.findById(Integer.parseInt(userId));
            if (CommonUtils.isNotEmpty(collectorTradingPartnerId)) {
                for (CollectorTradingPartner collectorTradingPartner : user.getCollectorTradingPartners()) {
                    if (CommonUtils.isEqual(collectorTradingPartner.getId().toString(), collectorTradingPartnerId)) {
                        collectorTradingPartner.setStartRange(startRange.toUpperCase());
                        collectorTradingPartner.setEndRange(endRange.toUpperCase());
                        collectorTradingPartner.setUser(user);
                    }
                }
            } else {
                CollectorTradingPartner collectorTradingPartner = new CollectorTradingPartner();
                collectorTradingPartner.setStartRange(startRange.toUpperCase());
                collectorTradingPartner.setEndRange(endRange.toUpperCase());
                collectorTradingPartner.setUser(user);
                user.getCollectorTradingPartners().add(collectorTradingPartner);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean applyCollectorToConsigneeOnlyAccounts(String collectorTradingPartnerId, String userId, String applyToConsigneeOnlyAccounts) throws Exception {
        if (userDAO.isCollectorAlreadyAppliedToConsigneeOnlyAccounts(Integer.parseInt(userId))) {
            return false;
        } else {
            User user = userDAO.findById(Integer.parseInt(userId));
            if (CommonUtils.isNotEmpty(collectorTradingPartnerId) && !"true".equals(applyToConsigneeOnlyAccounts)) {
                CollectorTradingPartner collectorTradingPartnerFromDB = null;
                for (CollectorTradingPartner collectorTradingPartner : user.getCollectorTradingPartners()) {
                    if (CommonUtils.isEqual(collectorTradingPartner.getId().toString(), collectorTradingPartnerId)) {
                        collectorTradingPartnerFromDB = collectorTradingPartner;
                    }
                }
                user.getCollectorTradingPartners().remove(collectorTradingPartnerFromDB);
            } else if (CommonUtils.isEmpty(collectorTradingPartnerId) && "true".equals(applyToConsigneeOnlyAccounts)) {
                CollectorTradingPartner collectorTradingPartner = new CollectorTradingPartner();
                collectorTradingPartner.setApplyToConsigneeOnlyAccounts(Boolean.valueOf(applyToConsigneeOnlyAccounts));
                collectorTradingPartner.setUser(user);
                user.getCollectorTradingPartners().add(collectorTradingPartner);
            }
        }
        return true;
    }

    public boolean unAssignCollectorFromTradingPartner(String userId) throws Exception {
        userDAO.unAssignCollectorFromTradingPartner(Integer.parseInt(userId));
        return true;
    }

    public boolean assignAPSpecialistToTradingPartner(String userId, String selectedSubTypes) throws Exception {
        User user = userDAO.findById(Integer.parseInt(userId));
        String[] subTypes = StringUtils.split(StringUtils.removeEnd(selectedSubTypes, ","), ",");
        if (userDAO.checkSubTypesForApSpecialist(Integer.parseInt(userId), subTypes)) {
            user.getApSpecialistTradingPartners().clear();
            for (String subType : subTypes) {
                APSpecialistTradingPartner apSpecialistTradingPartner = new APSpecialistTradingPartner();
                apSpecialistTradingPartner.setSubType(subType);
                apSpecialistTradingPartner.setUser(user);
                user.getApSpecialistTradingPartners().add(apSpecialistTradingPartner);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean unAssignAPSpecialistFromTradingPartner(String userId) throws Exception {
        userDAO.unAssignAPSpecialistFromTradingPartner(Integer.parseInt(userId));
        return true;
    }

    public String getCustDetails(String custNumber) throws Exception {
        APConfigurationBC apConfigurationBC = new APConfigurationBC();
        TradingPartner tradingPartner = tradingPartnerDAO.findById(custNumber);
        Vendor vendor = null;
        String term = null;
        vendor = apConfigurationBC.getVendorByCustomerNumber(custNumber);
        GenericCode code = null;
        if (null != vendor) {
            code = vendor.getCterms();
        }
        if (null != code && null != code.getId() && null != code.getCodedesc()) {
            term = code.getId() + "," + code.getCodedesc();
        } else {
            term = "11344,Due Upon Receipt";
        }
        if (tradingPartner.getAcctType().contains("V") && null != tradingPartner.getSubType() && tradingPartner.getSubType().equalsIgnoreCase("Overhead")) {
            term += "<-->Y";
        } else {
            term += "<-->N";
        }
        return term;
    }

    /**
     * @param term
     * @param date
     * @return Due date calculated from given creditTerm and invoiceDate
     * @throws Exception
     */
    public String dueDateCalculation(String term, String date) throws Exception {
        JSONObject jsonObj = new JSONObject();
        if (CommonUtils.isNotEmpty(date)) {
            int daysToAdd = 0;
            if (CommonUtils.isEqualIgnoreCase(term, "NET 15 DAYS")) {
                daysToAdd = 15;
            } else if (CommonUtils.isEqualIgnoreCase(term, "NET 30 DAYS")) {
                daysToAdd = 30;
            } else if (CommonUtils.isEqualIgnoreCase(term, "NET 45 DAYS")) {
                daysToAdd = 45;
            } else if (CommonUtils.isEqualIgnoreCase(term, "NET 60 DAYS")) {
                daysToAdd = 60;
            } else if (CommonUtils.isEqualIgnoreCase(term, "NET 7 DAYS")) {
                daysToAdd = 7;
            } else {
                daysToAdd = 0;
            }
            if (daysToAdd != 0) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(DateUtils.parseDate(date, "MM/dd/yyyy HH:mm"));
                calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
                Date invoiceDate = calendar.getTime();
                jsonObj.put("newDate", DateUtils.formatDate(invoiceDate, "MM/dd/yyyy HH:mm"));
            } else {
                jsonObj.put("newDate", date);
            }
        } else {
            throw new Exception("Invalid Date");
        }
        return jsonObj.toString();
    }

    /**
     * @param chequeNumber
     * @param vendorNumber
     * @param batchNumber
     * @return CheckRegisterDetailsList forwarded to
     * CheckRegisterDetailsTemplate.jsp for given
     * checkNumber,VendorNumber,BatchNumber
     */
    public String getCheckRegisterDetailsList(String transactionIds, boolean canEdit) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        List<TransactionBean> checkRegisterDetailsList = transactionDAO.getTransactionListByTransacionIds(transactionIds);
        request.setAttribute("checkRegisterDetailsList", checkRegisterDetailsList);
        return WebContextFactory.get().forwardToString("/jsps/AccountsPayable/CheckRegisterDetailsTemplate.jsp");
    }

    public String getCheckRegisterList(CheckRegisterForm checkRegisterForm, boolean canEdit) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        AccountingTransactionDAO accountingTransactionDAO = new AccountingTransactionDAO();
        String queryConditions = accountingTransactionDAO.buildCheckRegisterQuery(checkRegisterForm);
        int totalPageSize = accountingTransactionDAO.getTotalCheckRegisters(queryConditions);
        if (totalPageSize > 0) {
            checkRegisterForm.setTotalPageSize(totalPageSize);
            List<TransactionBean> checkRegisters = new ArrayList<TransactionBean>();
            if (CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "gotoPage")
                    || CommonUtils.isEqualIgnoreCase(checkRegisterForm.getButtonValue(), "doSort")) {
                if (CommonUtils.isNotEmpty(checkRegisterForm.getVoidIds())) {
                    String[] tempVoid = StringUtils.splitByWholeSeparator(StringUtils.removeStart(StringUtils.removeEnd(checkRegisterForm.getVoidIds(), ":-"), ":-"), ":-");
                    for (String voidId : tempVoid) {
                        String[] ids = StringUtils.splitByWholeSeparator(voidId, ",");
                        Double amount = 0d;
                        TransactionBean transactionBean = null;
                        for (String id : ids) {
                            Transaction transaction = transactionDAO.findById(Integer.parseInt(id));
                            transactionBean = new TransactionBean(transaction);
                            amount += transaction.getTransactionAmt();
                        }
                        if (null != transactionBean && amount != 0d) {
                            transactionBean.setAmount(NumberUtils.formatNumber(amount, "###,###,##0.00"));
                            transactionBean.setTransactionId(voidId);
                            transactionBean.setStatus("tempVoid");
                            checkRegisters.add(transactionBean);
                        }
                    }
                }
                if (CommonUtils.isNotEmpty(checkRegisterForm.getReprintIds())) {
                    String[] tempReprint = StringUtils.splitByWholeSeparator(StringUtils.removeStart(StringUtils.removeEnd(checkRegisterForm.getReprintIds(), ":-"), ":-"), ":-");
                    for (String reprintId : tempReprint) {
                        String[] ids = StringUtils.splitByWholeSeparator(reprintId, ",");
                        Double amount = 0d;
                        TransactionBean transactionBean = null;
                        for (String id : ids) {
                            Transaction transaction = transactionDAO.findById(Integer.parseInt(id));
                            transactionBean = new TransactionBean(transaction);
                            amount += transaction.getTransactionAmt();
                        }
                        if (null != transactionBean && amount != 0d) {
                            transactionBean.setAmount(NumberUtils.formatNumber(amount, "###,###,##0.00"));
                            transactionBean.setTransactionId(reprintId);
                            transactionBean.setStatus("tempReprint");
                            checkRegisters.add(transactionBean);
                        }
                    }
                }
            }
            List<TransactionBean> subList = accountingTransactionDAO.getCheckRegisters(checkRegisterForm, queryConditions);
            if (CommonUtils.isNotEmpty(subList)) {
                checkRegisters.addAll(subList);
            }
            if (CommonUtils.isNotEmpty(checkRegisters)) {
                int totalPages = totalPageSize / checkRegisterForm.getCurrentPageSize();
                int remainSize = totalPageSize % checkRegisterForm.getCurrentPageSize();
                if (remainSize > 0) {
                    totalPages += 1;
                }
                request.setAttribute("currentPageNo", checkRegisterForm.getPageNo());
                request.setAttribute("currentPageSize", checkRegisters.size());
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("totalPageSize", totalPageSize);
                request.setAttribute("checkRegisterList", checkRegisters);
                request.setAttribute("canEdit", canEdit);
            }
        }
        return WebContextFactory.get().forwardToString("/jsps/AccountsPayable/CheckRegisterListTemplate.jsp");
    }

    public CustAddress getCustInfoForCustNo(String clientNo) throws Exception {
        List clientlist = custAddressDAO.findBy(null, clientNo, null, null);
        String bankName = "";
        String bankAddress = "";
        if (clientlist != null && clientlist.size() > 0) {
            custAddress = (CustAddress) clientlist.get(0);
            CustAddress custAddress1 = (CustAddress) clientlist.get(0);
            if (custAddress1.getPrimeAddress() != null && custAddress1.getPrimeAddress().equals("on")) {
                Vendor vendor = custAddressDAO.getVendorAddress(custAddress1.getAcctNo());
                if (vendor != null) {
                    bankName = vendor.getDba();
                    if (vendor.getAddress1() != null) {
                        bankAddress += vendor.getAddress1() + ",";
                    }
                    if (vendor.getCuntry() != null) {
                        bankAddress += vendor.getCuntry() + ",";
                    }
                    if (vendor.getState() != null) {
                        bankAddress += vendor.getState() + ",";
                    }
                    if (vendor.getCity2() != null) {
                        bankAddress += vendor.getCity2() + ",";
                    }
                    if (vendor.getZip() != null) {
                        bankAddress += vendor.getZip();
                    }

                }
            }
        }
        custAddress.setBankName(bankName);
        custAddress.setBankAddress(bankAddress);
        return custAddress;
    }

    public String searchAccurals(AccrualsForm accrualsForm) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ApInvoice apInvoice = null;
        List<TransactionBean> accruals = new ArrayList<TransactionBean>();
        List<TransactionBean> selectedAccruals = new ArrayList<TransactionBean>();
        Double invoiceAmount = null != accrualsForm.getInvoiceamount() && !"".equals(accrualsForm.getInvoiceamount()) ? Double.parseDouble(accrualsForm.getInvoiceamount().replaceAll(",", "")) : 0d;
        Double allocatedAmount = 0d;
        Double remainingAmount = 0d;
        String accrualIds = null != accrualsForm.getAccrualIds() ? accrualsForm.getAccrualIds() : "";
        String tempAssignIds = null != accrualsForm.getTempAssignIds() ? accrualsForm.getTempAssignIds() : "";
        String tempDisputeIds = null != accrualsForm.getTempDisputeIds() ? accrualsForm.getTempDisputeIds() : "";
        String tempInactiveIds = null != accrualsForm.getTempVoidIds() ? accrualsForm.getTempVoidIds() : "";
        TreeSet<String> ids = new TreeSet<String>(Arrays.asList(StringUtils.split(accrualIds, ",")));
        TreeSet<String> assignIds = new TreeSet<String>(Arrays.asList(StringUtils.split(tempAssignIds, ",")));
        TreeSet<String> disputeIds = new TreeSet<String>(Arrays.asList(StringUtils.split(tempDisputeIds, ",")));
        TreeSet<String> inactiveIds = new TreeSet<String>(Arrays.asList(StringUtils.split(tempInactiveIds, ",")));
        if (CommonUtils.isNotEmpty(accrualsForm.getNewAccrualId()) && CommonUtils.isEqual(accrualsForm.getOldAccrualId(), "0")) {
            TransactionLedger newAccrual = transactionLedgerDAO.findById(Integer.parseInt(accrualsForm.getNewAccrualId()));
            accruals.add(new TransactionBean(newAccrual));
            ids.add(accrualsForm.getNewAccrualId());
        } else if (CommonUtils.isNotEmpty(accrualsForm.getNewAccrualId()) && CommonUtils.isNotEmpty(accrualsForm.getOldAccrualId())) {
            if (CommonUtils.isNotEqual(accrualsForm.getNewAccrualId(), "0")) {
                TransactionLedger newAccrual = transactionLedgerDAO.findById(Integer.parseInt(accrualsForm.getNewAccrualId()));
                accruals.add(new TransactionBean(newAccrual));
                ids.add(accrualsForm.getNewAccrualId());
            }
            TransactionLedger oldAccrual = transactionLedgerDAO.findById(Integer.parseInt(accrualsForm.getOldAccrualId()));
            TransactionBean transactionBean = new TransactionBean(oldAccrual);
            if (assignIds.contains(accrualsForm.getOldAccrualId())) {
                transactionBean.setStatus("tempAssign");
                allocatedAmount += oldAccrual.getTransactionAmt();
            }
            if (disputeIds.contains(accrualsForm.getOldAccrualId())) {
                transactionBean.setStatus("tempDispute");
            }
            ids.add(accrualsForm.getOldAccrualId());
            assignIds.remove(accrualsForm.getOldAccrualId());
            disputeIds.remove(accrualsForm.getOldAccrualId());
            inactiveIds.remove(accrualsForm.getOldAccrualId());
            accruals.add(transactionBean);
        } else if (CommonUtils.isNotEmpty(accrualsForm.getOldAccrualId())) {
            if (CommonUtils.isNotEqual(accrualsForm.getOldAccrualId(), "0")) {
                TransactionLedger oldAccrual = transactionLedgerDAO.findById(Integer.parseInt(accrualsForm.getOldAccrualId()));
                TransactionBean transactionBean = new TransactionBean(oldAccrual);
                if (assignIds.contains(accrualsForm.getOldAccrualId())) {
                    transactionBean.setStatus("tempAssign");
                    allocatedAmount += oldAccrual.getTransactionAmt();
                }
                if (disputeIds.contains(accrualsForm.getOldAccrualId())) {
                    transactionBean.setStatus("tempDispute");
                }
                ids.add(accrualsForm.getOldAccrualId());
                assignIds.remove(accrualsForm.getOldAccrualId());
                disputeIds.remove(accrualsForm.getOldAccrualId());
                inactiveIds.remove(accrualsForm.getOldAccrualId());
                accruals.add(transactionBean);
            }
        }
        accrualIds = StringUtils.join(ids.toArray(), ",");
        accrualsForm.setAccrualIds(accrualIds);
        tempAssignIds = StringUtils.join(assignIds.toArray(), ",");
        tempDisputeIds = StringUtils.join(disputeIds.toArray(), ",");
        tempInactiveIds = StringUtils.join(inactiveIds.toArray(), ",");
        if (CommonUtils.isNotEmpty(tempAssignIds)) {
            List<TransactionBean> tempAssignList = transactionLedgerDAO.findByTransactionIds(StringUtils.removeEnd(StringUtils.removeStart(tempAssignIds, ","), ","));
            for (TransactionBean transactionBean : tempAssignList) {
                if (CommonUtils.isEqualIgnoreCase(transactionBean.getStatus(), CommonConstants.STATUS_OPEN)
                        || CommonUtils.isEqualIgnoreCase(transactionBean.getStatus(), CommonConstants.STATUS_DISPUTE)) {
                    transactionBean.setStatus("tempAssign");
                }
                allocatedAmount += transactionBean.getTransactionAmount();
            }
            selectedAccruals.addAll(tempAssignList);
        }
        if (CommonUtils.isNotEmpty(tempDisputeIds)) {
            List<TransactionBean> tempDisputeList = transactionLedgerDAO.findByTransactionIds(StringUtils.removeEnd(StringUtils.removeStart(tempDisputeIds, ","), ","));
            for (TransactionBean transactionBean : tempDisputeList) {
                transactionBean.setStatus("tempDispute");
            }
            selectedAccruals.addAll(tempDisputeList);
        }
        if (CommonUtils.isNotEmpty(tempInactiveIds)) {
            List<TransactionBean> tempInactiveList = transactionLedgerDAO.findByTransactionIds(StringUtils.removeEnd(StringUtils.removeStart(tempInactiveIds, ","), ","));
            for (TransactionBean transactionBean : tempInactiveList) {
                transactionBean.setStatus("tempInactive");
            }
            selectedAccruals.addAll(tempInactiveList);
        }
        if (CommonUtils.isEqual(accrualsForm.getButtonValue(), "searchAccrualsByInvoice") && CommonUtils.isNotEmpty(accrualsForm.getInvoicenumber())) {
            String vendorName = accrualsForm.getVendor();
            String vendorNumber = accrualsForm.getVendornumber();
            accrualsForm.setStatus(null);
            apInvoice = new ApInvoiceDAO().findInvoiceByInvoiceNumber(accrualsForm.getInvoicenumber(), accrualsForm.getVendornumber());
            if (null != apInvoice && CommonUtils.isEqualIgnoreCase(apInvoice.getStatus(), CommonConstants.STATUS_IN_PROGRESS)) {
                accrualsForm.setVendor(null);
                accrualsForm.setVendornumber(null);
                accrualsForm.setStatus(CommonConstants.STATUS_IN_PROGRESS);
                String invoiceConditions = accountingLedgerDAO.buildSearchAccrualsQuery(accrualsForm);
                List<TransactionBean> subList = accountingLedgerDAO.searchAccruals(accrualsForm, invoiceConditions);
                if (CommonUtils.isNotEmpty(subList)) {
                    for (TransactionBean transactionBean : subList) {
                        accrualIds += transactionBean.getTransactionId() + ",";
                        allocatedAmount += transactionBean.getTransactionAmount();
                    }
                    selectedAccruals.addAll(subList);
                }
            } else if (null != apInvoice) {
                accrualsForm.setVendor(null);
                accrualsForm.setVendornumber(null);
                accrualsForm.setStatus(null);
                String invoiceConditions = accountingLedgerDAO.buildSearchAccrualsQuery(accrualsForm);
                List<TransactionBean> subList = accountingLedgerDAO.searchAccruals(accrualsForm, invoiceConditions);
                if (CommonUtils.isNotEmpty(subList)) {
                    for (TransactionBean transactionBean : subList) {
                        accrualIds += transactionBean.getTransactionId() + ",";
                        if (CommonUtils.isEqualIgnoreCase(transactionBean.getStatus(), CommonConstants.STATUS_IN_PROGRESS)) {
                            allocatedAmount += transactionBean.getTransactionAmount();
                        }
                    }
                    selectedAccruals.addAll(subList);
                }
            }
            accrualsForm.setVendor(vendorName);
            accrualsForm.setVendornumber(vendorNumber);
            accrualsForm.setAccrualIds(accrualIds);
            accrualsForm.setButtonValue("searchAccruals");
        }
        int selectSize = accruals.size() + selectedAccruals.size();
        String conditions = accountingLedgerDAO.buildSearchAccrualsQuery(accrualsForm);
        int totalPageSize = accountingLedgerDAO.getTotalAccruals(conditions);
        accrualsForm.setTotalPageSize(totalPageSize + selectSize);
        if (totalPageSize > 0) {
            List<TransactionBean> subList = accountingLedgerDAO.searchAccruals(accrualsForm, conditions);
            if (CommonUtils.isNotEmpty(subList)) {
                accruals.addAll(subList);
            }
        }
        accruals.addAll(selectedAccruals);
        if (CommonUtils.isNotEmpty(accruals)) {
            totalPageSize += selectSize;
            int totalPages = totalPageSize / accrualsForm.getCurrentPageSize();
            int remainSize = totalPageSize % accrualsForm.getCurrentPageSize();
            if (remainSize > 0) {
                totalPages += 1;
            }
            request.setAttribute("totalRecords", accruals.size());
            request.setAttribute("currentPageNo", accrualsForm.getPageNo());
            request.setAttribute("currentPageSize", accrualsForm.getCurrentPageSize());
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalPageSize", totalPageSize);
            request.setAttribute("accruals", accruals);
        }
        if (null != apInvoice) {
            request.setAttribute("invoiceFlag", true);
            request.setAttribute("invoiceStatus", apInvoice.getStatus());
            invoiceAmount = apInvoice.getInvoiceAmount();
            if (CommonUtils.isNotEmpty(apInvoice.getTerm())) {
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(apInvoice.getTerm()));
                if (null != genericCode && null != apInvoice.getDate() && CommonUtils.isNotEmpty(genericCode.getCodedesc())) {
                    String invoiceDate = DateUtils.formatDate(apInvoice.getDate(), "MM/dd/yyyy HH:mm");
                    String dueDate = dueDateCalculation(genericCode.getCodedesc(), invoiceDate);
                    request.setAttribute("termDesc", genericCode.getCodedesc());
                    request.setAttribute("term", apInvoice.getTerm());
                    request.setAttribute("invoiceDate", invoiceDate);
                    if (CommonUtils.isNotEmpty(dueDate)) {
                        JSONTokener jsonTokener = new JSONTokener(dueDate);
                        JSONObject jsonObject = new JSONObject(jsonTokener);
                        request.setAttribute("dueDate", jsonObject.get("newDate"));
                    }
                }
            }
        } else {
            request.setAttribute("invoiceFlag", false);
        }
        remainingAmount = invoiceAmount - allocatedAmount;
        request.setAttribute("invoiceAmount", invoiceAmount);
        request.setAttribute("totalAllocatedAmount", allocatedAmount);
        request.setAttribute("totalRemainingAmount", remainingAmount);
        request.setAttribute("accrualsForm", accrualsForm);
        return WebContextFactory.get().forwardToString("/jsps/AccountsPayable/AccrualsListTemplate.jsp");
    }

    public String createReport(AccrualsForm accrualsForm, String reportType) throws Exception {
        AccountingLedgerDAO accountingLedgerDAO = new AccountingLedgerDAO();
        ApInvoice apInvoice = null;
        List<TransactionBean> accruals = new ArrayList<TransactionBean>();
        List<TransactionBean> selectedAccruals = new ArrayList<TransactionBean>();
        String fileName = "";
        String accrualIds = null != accrualsForm.getAccrualIds() ? accrualsForm.getAccrualIds() : "";
        String tempAssignIds = null != accrualsForm.getTempAssignIds() ? accrualsForm.getTempAssignIds() : "";
        String tempDisputeIds = null != accrualsForm.getTempDisputeIds() ? accrualsForm.getTempDisputeIds() : "";
        String tempInactiveIds = null != accrualsForm.getTempVoidIds() ? accrualsForm.getTempVoidIds() : "";
        TreeSet<String> ids = new TreeSet<String>(Arrays.asList(StringUtils.split(accrualIds, ",")));
        TreeSet<String> assignIds = new TreeSet<String>(Arrays.asList(StringUtils.split(tempAssignIds, ",")));
        TreeSet<String> disputeIds = new TreeSet<String>(Arrays.asList(StringUtils.split(tempDisputeIds, ",")));
        TreeSet<String> inactiveIds = new TreeSet<String>(Arrays.asList(StringUtils.split(tempInactiveIds, ",")));

        accrualIds = StringUtils.join(ids.toArray(), ",");
        accrualsForm.setAccrualIds(accrualIds);
        tempAssignIds = StringUtils.join(assignIds.toArray(), ",");
        tempDisputeIds = StringUtils.join(disputeIds.toArray(), ",");
        tempInactiveIds = StringUtils.join(inactiveIds.toArray(), ",");
        if (CommonUtils.isNotEmpty(tempAssignIds)) {
            List<TransactionBean> tempAssignList = transactionLedgerDAO.findByTransactionIds(StringUtils.removeEnd(StringUtils.removeStart(tempAssignIds, ","), ","));
            selectedAccruals.addAll(tempAssignList);
        }
        if (CommonUtils.isNotEmpty(tempDisputeIds)) {
            List<TransactionBean> tempDisputeList = transactionLedgerDAO.findByTransactionIds(StringUtils.removeEnd(StringUtils.removeStart(tempDisputeIds, ","), ","));
            selectedAccruals.addAll(tempDisputeList);
        }
        if (CommonUtils.isNotEmpty(tempInactiveIds)) {
            List<TransactionBean> tempInactiveList = transactionLedgerDAO.findByTransactionIds(StringUtils.removeEnd(StringUtils.removeStart(tempInactiveIds, ","), ","));
            selectedAccruals.addAll(tempInactiveList);
        }
        if (CommonUtils.isEqual(accrualsForm.getButtonValue(), "searchAccrualsByInvoice") && CommonUtils.isNotEmpty(accrualsForm.getInvoicenumber())) {
            String vendorName = accrualsForm.getVendor();
            String vendorNumber = accrualsForm.getVendornumber();
            accrualsForm.setStatus(null);
            apInvoice = new ApInvoiceDAO().findInvoiceByInvoiceNumber(accrualsForm.getInvoicenumber(), accrualsForm.getVendornumber());
            if (null != apInvoice && CommonUtils.isEqualIgnoreCase(apInvoice.getStatus(), CommonConstants.STATUS_IN_PROGRESS)) {
                accrualsForm.setVendor(null);
                accrualsForm.setVendornumber(null);
                accrualsForm.setStatus(CommonConstants.STATUS_IN_PROGRESS);
                String invoiceConditions = accountingLedgerDAO.buildSearchAccrualsQuery(accrualsForm);
                List<TransactionBean> subList = accountingLedgerDAO.searchAccruals(accrualsForm, invoiceConditions);
                if (CommonUtils.isNotEmpty(subList)) {
                    for (TransactionBean transactionBean : subList) {
                        accrualIds += transactionBean.getTransactionId() + ",";
                    }
                    selectedAccruals.addAll(subList);
                }
            } else if (null != apInvoice) {
                accrualsForm.setVendor(null);
                accrualsForm.setVendornumber(null);
                accrualsForm.setStatus(null);
                String invoiceConditions = accountingLedgerDAO.buildSearchAccrualsQuery(accrualsForm);
                List<TransactionBean> subList = accountingLedgerDAO.searchAccruals(accrualsForm, invoiceConditions);
                if (CommonUtils.isNotEmpty(subList)) {
                    for (TransactionBean transactionBean : subList) {
                        accrualIds += transactionBean.getTransactionId() + ",";
                    }
                    selectedAccruals.addAll(subList);
                }
            }
            accrualsForm.setVendor(vendorName);
            accrualsForm.setVendornumber(vendorNumber);
            accrualsForm.setAccrualIds(accrualIds);
            accrualsForm.setButtonValue("searchAccruals");
        }
        accrualsForm.setPageNo(0);
        String conditions = accountingLedgerDAO.buildSearchAccrualsQuery(accrualsForm);
        List<TransactionBean> subList = accountingLedgerDAO.searchAccruals(accrualsForm, conditions);
        if (CommonUtils.isNotEmpty(subList)) {
            accruals.addAll(subList);
        }
        accruals.addAll(selectedAccruals);
        if (CommonUtils.isEqualIgnoreCase(reportType, "pdf")) {
            String contextPath = WebContextFactory.get().getServletContext().getRealPath("/");
            fileName = new AccrualsReportCreator().createReport(accrualsForm, accruals, contextPath);
        } else {
            fileName = new ExportAccrualsToExcel().exportToExcel(accrualsForm, accruals);
        }
        return fileName;
    }

    public String searchAccrualsById(AccrualsForm accrualsForm) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        String[] accrualIds = StringUtils.splitByWholeSeparator(accrualsForm.getAccrualIds(), ",");
        String[] tempAssignIds = StringUtils.splitByWholeSeparator(accrualsForm.getTempAssignIds(), ",");
        String[] tempDisputeIds = StringUtils.splitByWholeSeparator(accrualsForm.getTempDisputeIds(), ",");
        List<String> assignIds = new ArrayList<String>();
        List<String> disputeIds = new ArrayList<String>();
        //List<String> voidIds = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        if (tempAssignIds.length > 0) {
            assignIds.addAll(Arrays.asList(tempAssignIds));
            ids.addAll(Arrays.asList(tempAssignIds));
        }
        if (tempDisputeIds.length > 0) {
            disputeIds.addAll(Arrays.asList(tempDisputeIds));
            ids.addAll(Arrays.asList(tempDisputeIds));
        }
        List<TransactionBean> accrualsList = new ArrayList<TransactionBean>();
        TransactionLedger transactionLedger = null;
        TransactionBean transactionBean = null;
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        Double invoiceAmount = 0d;
        Double totalAllocatedAmount = 0d;
        Double totalRemainingAmount = 0d;
        if (null != accrualsForm.getInvoiceamount() && !accrualsForm.getInvoiceamount().trim().equals("")) {
            invoiceAmount = Double.parseDouble(accrualsForm.getInvoiceamount().replaceAll(",", ""));
            totalRemainingAmount = invoiceAmount;
        }
        if (null != accrualsForm.getNewAccrualId() && !accrualsForm.getNewAccrualId().equals("0") && null != accrualsForm.getOldAccrualId() && accrualsForm.getOldAccrualId().trim().equals("0")) {
            transactionLedger = transactionLedgerDAO.findById(Integer.parseInt(accrualsForm.getNewAccrualId()));
            transactionBean = new TransactionBean(transactionLedger);
            accrualsList.add(transactionBean);
        }
        if (null != accrualsForm.getAccrualIds() && !accrualsForm.getAccrualIds().trim().equals("")) {
            for (String accrualId : accrualIds) {
                if (null != accrualsForm.getOldAccrualId() && accrualsForm.getOldAccrualId().trim().equals(accrualId)) {
                    transactionLedger = transactionLedgerDAO.findById(Integer.parseInt(accrualId));
                    transactionBean = new TransactionBean(transactionLedger);
                    if (null != ids && !ids.isEmpty() && ids.contains(accrualId)) {
                        totalAllocatedAmount += Double.parseDouble(transactionBean.getAmount().replaceAll(",", ""));
                        totalRemainingAmount = invoiceAmount - totalAllocatedAmount;
                    }
                    if (null != assignIds && !assignIds.isEmpty()
                            && assignIds.contains(accrualId) && !transactionBean.getStatus().trim().equals(CommonConstants.STATUS_IN_PROGRESS)) {
                        transactionBean.setStatus("tempAssign");
                    }
                    if (null != disputeIds && !disputeIds.isEmpty() && disputeIds.contains(accrualId)) {
                        transactionBean.setStatus("tempDispute");
                    }
                    accrualsList.add(transactionBean);
                    if (null != accrualsForm.getNewAccrualId() && !accrualsForm.getNewAccrualId().equals("0")) {
                        transactionLedger = transactionLedgerDAO.findById(Integer.parseInt(accrualsForm.getNewAccrualId()));
                        transactionBean = new TransactionBean(transactionLedger);
                        accrualsList.add(transactionBean);
                    }
                } else if (null != accrualsForm.getOldAccrualId() && !accrualsForm.getOldAccrualId().trim().equals(accrualId)) {
                    transactionLedger = transactionLedgerDAO.findById(Integer.parseInt(accrualId));
                    transactionBean = new TransactionBean(transactionLedger);
                    if (transactionBean.getStatus().trim().equals(CommonConstants.STATUS_IN_PROGRESS)) {
                        totalAllocatedAmount += Double.parseDouble(transactionBean.getAmount().replaceAll(",", ""));
                        totalRemainingAmount = invoiceAmount - totalAllocatedAmount;
                    } else if (null != ids && !ids.isEmpty() && ids.contains(accrualId)) {
                        totalAllocatedAmount += Double.parseDouble(transactionBean.getAmount().replaceAll(",", ""));
                        totalRemainingAmount = invoiceAmount - totalAllocatedAmount;
                    }
                    if (null != assignIds && !assignIds.isEmpty()
                            && assignIds.contains(accrualId) && !transactionBean.getStatus().trim().equals(CommonConstants.STATUS_IN_PROGRESS)) {
                        transactionBean.setStatus("tempAssign");
                    }
                    if (null != disputeIds && !disputeIds.isEmpty() && disputeIds.contains(accrualId)) {
                        transactionBean.setStatus("tempDispute");
                    }
                    accrualsList.add(transactionBean);
                }
            }
        }
        request.setAttribute("accrualsList", accrualsList);
        request.setAttribute("invoiceAmount", number.format(invoiceAmount));
        request.setAttribute("totalAllocatedAmount", number.format(totalAllocatedAmount));
        request.setAttribute("totalRemainingAmount", number.format(totalRemainingAmount));
        return WebContextFactory.get().forwardToString("/jsps/AccountsPayable/AccrualsListTemplate.jsp");
    }

    public String validateApInvoiceForVendor(String vendorNumber, String invoiceNumber) throws Exception {
        ApInvoice apInvoice = new ApInvoiceDAO().findInvoiceByInvoiceNumber(invoiceNumber, vendorNumber);
        if (null != apInvoice) {
            if (null != apInvoice.getStatus() && apInvoice.getStatus().trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                return CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE;
            } else {
                return "Available";
            }
        } else {
            boolean isAvailable = new AccountingTransactionDAO().isApInvoiceAvailable(invoiceNumber, vendorNumber);
            if (isAvailable) {
                if (new EdiInvoiceDAO().isInvoiceAvailable(vendorNumber, invoiceNumber)) {
                    return "Available";
                } else {
                    return "EDI Invoice";
                }
            } else {
                return CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE;
            }
        }
    }

    public String validateARRedInvoiceForCustomer(String vendorNumber, String invoiceNumber) throws Exception {
        ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.findInvoiceByInvoiceNumber(invoiceNumber, vendorNumber);
        if (null != arRedInvoice) {
            if (null != arRedInvoice.getStatus() && arRedInvoice.getStatus().trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                return CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE;
            } else {
                return "Available";
            }
        } else {
            return "Available";
        }
    }

    public String getEmailStatus(String moduleId, String vendorNumber) throws Exception {
        return new EmailschedulerDAO().getDisputeEmailStatus(moduleId);
    }

    public String getScanList(ScanForm scanForm) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        ScanBC scanBC = new ScanBC();
        List<ScanConfig> scanList = scanBC.findScanConfigByScreenName(scanForm.getScreenName(), scanForm.getFileNumber());
        if (CommonUtils.isNotEmpty(scanList)) {
            int listSize = scanList.size();
            List<ScanConfig> scanList1 = scanList.subList(0, listSize % 2 == 0 ? listSize / 2 : listSize / 2 + 1);
            List<ScanConfig> scanList2 = scanList.subList(listSize % 2 == 0 ? listSize / 2 : listSize / 2 + 1, listSize);
            request.setAttribute(CommonConstants.SCAN_SUB_LIST1, scanList1);
            request.setAttribute(CommonConstants.SCAN_SUB_LIST2, scanList2);
            request.setAttribute(CommonConstants.SCAN_SCREEN_NAME_LIST, scanBC.getScreenNameList());
            request.setAttribute("scanForm", scanForm);
            return WebContextFactory.get().forwardToString("/jsps/Scan/ScanListTemplate.jsp");
        } else {
            return "";
        }
    }

    public String saveCheckRegister(String transactionsToBeVoid, String transactionsToBeReprint) throws Exception {
        Transaction paidTransaction = null;
        String message = "";
        int totalCheck = 0;
        int totalAch = 0;
        int totalWire = 0;
        int totalVoid = 0;
        boolean hasNoCheckPrinter = false;
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (CommonUtils.isNotEmpty(transactionsToBeVoid)) {
            ApTransactionHistoryDAO apTransactionHistoryDAO = new ApTransactionHistoryDAO();
            ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
            String[] voidTransactions = StringUtils.splitByWholeSeparator(StringUtils.removeStart(StringUtils.removeEnd(transactionsToBeVoid, ":-"), ":-"), ":-");
            for (int i = 0; i < voidTransactions.length; i++) {
                String[] voidIds = StringUtils.splitByWholeSeparator(voidTransactions[i], ",");
                Double checkAmount = 0d;
                for (int j = 0; j < voidIds.length; j++) {
                    paidTransaction = transactionDAO.findById(Integer.parseInt(voidIds[j]));
                    if (CommonUtils.isEqualIgnoreCase(paidTransaction.getTransactionType(), CommonConstants.TRANSACTION_TYPE_PAYAMENT)
                            && (CommonUtils.isEqualIgnoreCase(paidTransaction.getStatus(), CommonConstants.STATUS_PAID)
                            || CommonUtils.isEqualIgnoreCase(paidTransaction.getStatus(), CommonConstants.STATUS_RECONCILE_IN_PROGRESS)
                            || CommonUtils.isEqualIgnoreCase(paidTransaction.getStatus(), "clear")
                            || CommonUtils.in(paidTransaction.getStatus(), CommonConstants.STATUS_SENT, CommonConstants.STATUS_READY_TO_SEND))) {
                        String originalTransactionIds = transactionDAO.getTransactionIdsForVoid(paidTransaction);
                        if (CommonUtils.isNotEmpty(originalTransactionIds)) {
                            String[] ids = StringUtils.splitByWholeSeparator(StringUtils.removeStart(StringUtils.removeEnd(originalTransactionIds, ","), ","), ",");
                            for (String id : ids) {
                                Transaction originalTransaction = transactionDAO.findById(Integer.parseInt(id));
                                originalTransaction.setStatus(CommonConstants.STATUS_READY_TO_PAY);
                                originalTransaction.setChequeNumber(null);
                                originalTransaction.setBankName(null);
                                originalTransaction.setBankAccountNumber(null);
                                originalTransaction.setApBatchId(null);
                                originalTransaction.setPaymentMethod(null);
                                originalTransaction.setApprovedBy(null);
                                originalTransaction.setPaidBy(null);
                                originalTransaction.setUpdatedOn(new Date());
                                originalTransaction.setUpdatedBy(loginUser.getUserId());
                                //insert new transaction ledger record for all payment entry
                                if (CommonUtils.isEqualIgnoreCase(originalTransaction.getTransactionType(), CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                                    TransactionLedger transactionLedger = new TransactionLedger(originalTransaction);
                                    transactionLedger.setTransactionAmt(-paidTransaction.getTransactionAmt());
                                    transactionLedger.setBalance(new Double(0));
                                    transactionLedger.setBalanceInProcess(new Double(0));
                                    transactionLedger.setTransactionType(CommonConstants.TRANSACTION_TYPE_CASH_DEPOSIT);
                                    transactionLedger.setStatus(CommonConstants.STATUS_PAID);
                                    transactionLedger.setCleared(CommonConstants.NO);
                                    transactionLedger.setBankAccountNumber(paidTransaction.getBankAccountNumber());
                                    transactionLedger.setBankName(paidTransaction.getBankName());
                                    transactionLedger.setChequeNumber(paidTransaction.getChequeNumber());
                                    transactionLedger.setTransactionDate(new Date());
                                    transactionLedger.setInvoiceNumber(null);
                                    transactionLedger.setCustomerReferenceNo(paidTransaction.getApBatchId().toString());
                                    transactionLedger.setPostedDate(new Date());
                                    transactionLedger.setSubledgerSourceCode(CommonConstants.SUB_LEDGER_CODE_CASH_DEPOSIT);
                                    transactionLedger.setApBatchId(paidTransaction.getApBatchId());
                                    transactionLedger.setCreatedOn(new Date());
                                    transactionLedger.setCreatedBy(loginUser.getUserId());
                                    transactionLedgerDAO.save(transactionLedger);
                                    //Save into history
                                    ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                                    arTransactionHistory.setCustomerNumber(paidTransaction.getCustNo());
                                    arTransactionHistory.setBlNumber(paidTransaction.getBillLaddingNo());
                                    arTransactionHistory.setInvoiceNumber(paidTransaction.getInvoiceNumber());
                                    arTransactionHistory.setInvoiceDate(originalTransaction.getTransactionDate());
                                    arTransactionHistory.setTransactionDate(new Date());
                                    arTransactionHistory.setPostedDate(new Date());
                                    arTransactionHistory.setTransactionAmount(-paidTransaction.getTransactionAmt());
                                    arTransactionHistory.setCustomerReferenceNumber(paidTransaction.getCustomerReferenceNo());
                                    arTransactionHistory.setVoyageNumber(paidTransaction.getVoyageNo());
                                    arTransactionHistory.setCheckNumber(paidTransaction.getChequeNumber());
                                    arTransactionHistory.setArBatchId(null);
                                    arTransactionHistory.setApBatchId(paidTransaction.getApBatchId());
                                    arTransactionHistory.setTransactionType("AP PY");
                                    arTransactionHistory.setCreatedBy(loginUser.getLoginName());
                                    arTransactionHistory.setCreatedDate(new Date());
                                    arTransactionHistoryDAO.save(arTransactionHistory);
                                    originalTransaction.setBalance(-paidTransaction.getTransactionAmt());
                                    originalTransaction.setBalanceInProcess(-paidTransaction.getTransactionAmt());
                                    originalTransaction.setClosedDate(null);
                                    checkAmount += paidTransaction.getTransactionAmt();
                                } else if (CommonUtils.isEqualIgnoreCase(originalTransaction.getTransactionType(), CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                                    StringBuilder desc = new StringBuilder(paidTransaction.getPaymentMethod()).append(" Payment for Invoice '");
                                    desc.append(originalTransaction.getInvoiceNumber()).append("' of Vendor '");
                                    desc.append(originalTransaction.getCustName()).append("(").append(originalTransaction.getCustNo()).append(")'");
                                    desc.append(" voided by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                                    String key = originalTransaction.getCustNo() + "-" + originalTransaction.getInvoiceNumber();
                                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, loginUser);
                                    originalTransaction.setBalance(paidTransaction.getTransactionAmt());
                                    originalTransaction.setBalanceInProcess(paidTransaction.getTransactionAmt());
                                    checkAmount += paidTransaction.getTransactionAmt();
                                    ApTransactionHistory apTransactionHistory = new ApTransactionHistory(paidTransaction);
                                    apTransactionHistory.setInvoiceDate(originalTransaction.getTransactionDate());
                                    apTransactionHistory.setPostedDate(new Date());
                                    apTransactionHistory.setTransactionDate(new Date());
                                    apTransactionHistory.setAmount(paidTransaction.getTransactionAmt());
                                    apTransactionHistory.setCreatedBy(loginUser.getLoginName());
                                    apTransactionHistoryDAO.save(apTransactionHistory);
                                }
                            }
                            paidTransaction.setVoidDate(new Date());
                            paidTransaction.setVoidTransaction(CommonConstants.YES);
                            paidTransaction.setUpdatedOn(new Date());
                            paidTransaction.setUpdatedBy(loginUser.getUserId());
                        }
                    }
                }
                if (null != checkAmount && checkAmount != 0d && paidTransaction != null) {
                    TransactionLedger transactionLedger = new TransactionLedger(paidTransaction);
                    transactionLedger.setTransactionAmt(checkAmount);
                    BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
                    String bankGLAccountNumber = bankDetailsDAO.getGlAccountNoByBankAcctNumberAndBankName(paidTransaction.getBankAccountNumber(), paidTransaction.getBankName());
                    transactionLedger.setGlAccountNumber(bankGLAccountNumber);
                    transactionLedger.setTransactionType(CommonConstants.TRANSACTION_TYPE_CASH_DEPOSIT);
                    transactionLedger.setStatus(CommonConstants.STATUS_PAID);
                    transactionLedger.setCleared(CommonConstants.NO);
                    transactionLedger.setBankAccountNumber(paidTransaction.getBankAccountNumber());
                    transactionLedger.setBankName(paidTransaction.getBankName());
                    transactionLedger.setChequeNumber(paidTransaction.getChequeNumber());
                    transactionLedger.setTransactionDate(new Date());
                    transactionLedger.setInvoiceNumber(null);
                    transactionLedger.setCustomerReferenceNo(paidTransaction.getApBatchId().toString());
                    transactionLedger.setPostedDate(new Date());
                    transactionLedger.setSubledgerSourceCode(CommonConstants.SUB_LEDGER_CODE_CASH_DEPOSIT);
                    transactionLedger.setApBatchId(paidTransaction.getApBatchId());
                    transactionLedger.setCreatedOn(new Date());
                    transactionLedger.setCreatedBy(loginUser.getUserId());
                    transactionLedgerDAO.save(transactionLedger);
                    checkAmount = 0d;
                    StringBuilder desc = new StringBuilder(paidTransaction.getPaymentMethod()).append(" Payment of Vendor '");
                    desc.append(paidTransaction.getCustName()).append("(").append(paidTransaction.getCustNo()).append(")'");
                    desc.append(" voided by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                    String key = null;
                    if (CommonUtils.isEqualIgnoreCase(paidTransaction.getPaymentMethod(), CommonConstants.PAYMENT_METHOD_CHECK)) {
                        key = paidTransaction.getChequeNumber();
                    } else {
                        key = paidTransaction.getCustNo() + "-" + paidTransaction.getApBatchId().toString();
                    }
                    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_PAYMENT, key, NotesConstants.AP_PAYMENT, loginUser);
                }
                totalVoid++;
                paidTransaction = null;
            }
        }
        if (CommonUtils.isNotEmpty(transactionsToBeReprint)) {
            String[] reprintTransactionIds = StringUtils.splitByWholeSeparator(StringUtils.removeStart(StringUtils.removeEnd(transactionsToBeReprint, ":-"), ":-"), ":-");
            for (int i = 0; i < reprintTransactionIds.length; i++) {
                List<TransactionBean> reprintList = transactionDAO.findByTransactionIds(reprintTransactionIds[i]);
                String paymentMethod = null;
                String chequeNumber = null;
                String paymentDate = null;
                String bankName = null;
                String bankAccountNumber = null;
                String vendorNumber = null;
                int test = 0;
                for (TransactionBean transactionBean : reprintList) {
                    if (CommonUtils.isEqualIgnoreCase(transactionBean.getRecordType(), CommonConstants.TRANSACTION_TYPE_PAYAMENT)
                            && (CommonUtils.isEqualIgnoreCase(transactionBean.getStatus(), CommonConstants.STATUS_PAID)
                            || CommonUtils.isEqualIgnoreCase(transactionBean.getStatus(), CommonConstants.STATUS_RECONCILE_IN_PROGRESS)
                            || CommonUtils.in(transactionBean.getStatus(), CommonConstants.STATUS_SENT, CommonConstants.STATUS_READY_TO_SEND))) {
                        chequeNumber = transactionBean.getChequenumber();
                        paymentMethod = transactionBean.getPaymentMethod();
                        paymentDate = transactionBean.getTransDate();
                        test++;
                        bankName = transactionBean.getBankName();
                        bankAccountNumber = transactionBean.getBankAccountNumber();
                        vendorNumber = transactionBean.getCustomerNo();
                    }
                }
                if (null != paymentMethod && test > 0 && null != bankName && null != bankAccountNumber && null != vendorNumber) {
                    if (CommonUtils.isEqualIgnoreCase(paymentMethod, CommonConstants.PAYMENT_METHOD_CHECK)) {
                        if (CommonUtils.isNotEqual(chequeNumber, "0")) {
                            String result = this.printCheck(vendorNumber, chequeNumber, reprintList, paymentDate, bankName, bankAccountNumber);
                            if (result.trim().equals("success")) {
                                Transaction updateTransaction = null;
                                if (null != reprintList && !reprintList.isEmpty()) {
                                    for (TransactionBean transactionBean : reprintList) {
                                        updateTransaction = transactionDAO.findById(Integer.parseInt(transactionBean.getTransactionId()));
                                        updateTransaction.setReprint(CommonConstants.YES);
                                        updateTransaction.setReprintDate(new Date());
                                        updateTransaction.setUpdatedOn(new Date());
                                        updateTransaction.setUpdatedBy(loginUser.getUserId());
                                    }
                                }
                                totalCheck++;
                            } else if (result.trim().equals("checkPrinterError")) {
                                hasNoCheckPrinter = true;
                            }
                        }
                    } else if (CommonUtils.isEqualIgnoreCase(paymentMethod, CommonConstants.PAYMENT_METHOD_ACH)) {
                        if (CommonUtils.isNotEmpty(reprintList)) {
                            for (TransactionBean transactionBean : reprintList) {
                                Transaction updateTransaction = transactionDAO.findById(Integer.parseInt(transactionBean.getTransactionId()));
                                updateTransaction.setReprint(CommonConstants.YES);
                                updateTransaction.setReprintDate(new Date());
                                updateTransaction.setUpdatedOn(new Date());
                                updateTransaction.setUpdatedBy(loginUser.getUserId());
                            }
                            totalAch++;
                        }
                    }
                } else if (CommonUtils.isEqualIgnoreCase(paymentMethod, CommonConstants.PAYMENT_METHOD_WIRE)) {
                    if (CommonUtils.isNotEmpty(reprintList)) {
                        for (TransactionBean transactionBean : reprintList) {
                            Transaction updateTransaction = transactionDAO.findById(Integer.parseInt(transactionBean.getTransactionId()));
                            updateTransaction.setReprint(CommonConstants.YES);
                            updateTransaction.setReprintDate(new Date());
                            updateTransaction.setUpdatedOn(new Date());
                            updateTransaction.setUpdatedBy(loginUser.getUserId());
                        }
                    }
                    totalWire++;
                }
            }
        }
        if (totalVoid == 1) {
            message = message + totalVoid + " transaction voided";
        } else if (totalVoid > 1) {
            message = message + totalVoid + " transactions voided";
        }
        if (totalAch > 0 || totalCheck > 0 || totalWire > 0) {
            if (totalCheck == 1) {
                if (totalVoid > 0) {
                    message = message + " and " + totalCheck + " Check";
                } else {
                    message = totalCheck + " Check";
                }
            } else if (totalCheck > 1) {
                if (totalVoid > 0) {
                    message = message + " and " + totalCheck + " Checks";
                } else {
                    message = totalCheck + " Checks";
                }
            }
            if (totalAch == 1) {
                if (totalVoid > 0 || totalCheck > 0) {
                    message = message + " and " + totalAch + " Ach";
                } else {
                    message = totalAch + " Ach";
                }
            } else if (totalAch > 1) {
                if (totalVoid > 0 || totalCheck > 0) {
                    message = message + " and " + totalAch + " Achs";
                } else {
                    message = totalAch + " Achs";
                }
            }
            if (totalWire == 1) {
                if (totalVoid > 0 || totalCheck > 0 || totalAch > 0) {
                    message = message + " and " + totalWire + " Wire";
                } else {
                    message = totalWire + " Wire";
                }
            } else if (totalWire > 1) {
                if (totalVoid > 0 || totalCheck > 0 || totalAch > 0) {
                    message = message + " and " + totalWire + " Wires";
                } else {
                    message = totalWire + " Wires";
                }
            }
            message += " reprinted";
        }
        if (!message.trim().equals("")) {
            if (hasNoCheckPrinter) {
                message += " and No Check Printer Available to reprint the Check";
            }
        }
        return message;
    }

    private String printCheck(String vendorNumber, String startingNumber, List<TransactionBean> reprintList, String paymentDate, String bankName, String bankAccountNo) throws Exception {
        String outputFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/ApPayment" + "/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(outputFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        outputFileName = outputFileName + startingNumber.toString() + ".pdf";
        String checkFileName = null;
        String overflowFileName = null;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        ApPaymentPdfCreator apPaymentPdfCreator = new ApPaymentPdfCreator();
        String fileNames = apPaymentPdfCreator.createReport(startingNumber, vendorNumber, outputFileName, paymentDate, reprintList);
        if (null != fileNames && fileNames.contains(":-")) {
            String[] fileName = StringUtils.splitByWholeSeparator(fileNames, ":-");
            checkFileName = fileName[0];
            overflowFileName = fileName[1];
        } else if (null != fileNames) {
            checkFileName = fileNames;
        }
        BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
        List<BankDetails> bankList = bankDetailsDAO.getBankDetails(bankName, bankAccountNo);
        String checkPrinter = "";
        String overflowPrinter = "";
        for (BankDetails bankDetails : bankList) {
            checkPrinter = bankDetails.getCheckPrinter();
            overflowPrinter = bankDetails.getOverflowPrinter();
        }
        if (null != checkFileName && !checkFileName.trim().equals("")) {
            if (null != checkPrinter && !checkPrinter.trim().equals("")) {
                this.savePrintTemplate(checkFileName, startingNumber.toString(), loginUser, checkPrinter);
            } else {
                return "checkPrinterError";
            }
        }
        if (null != overflowFileName && !overflowFileName.trim().equals("")) {
            if (null != overflowPrinter && !overflowPrinter.trim().equals("")) {
                this.savePrintTemplate(overflowFileName, startingNumber.toString(), loginUser, overflowPrinter);
            }
        }
        return "success";
    }

    private void savePrintTemplate(String fileLocation, String moduleId, User loginUser, String printerName) throws Exception {
        EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
        EmailSchedulerVO emailSchedulerVO = new EmailSchedulerVO();
        emailSchedulerVO.setFileLocation(fileLocation);
        emailSchedulerVO.setStatus(CommonConstants.EMAIL_STATUS_PENDING);
        emailSchedulerVO.setModuleName("APPAYMENT");
        emailSchedulerVO.setModuleId(moduleId);
        emailSchedulerVO.setType(CommonConstants.CONTACT_MODE_PRINT);
        emailSchedulerVO.setHtmlMessage("Check Printing");
        emailSchedulerVO.setSubject("Check Printing");
        emailSchedulerVO.setUserName(loginUser.getLoginName());
        emailSchedulerVO.setEmailDate(new Date());
        emailSchedulerVO.setPrinterName(printerName);
        emailSchedulerVO.setPrintCopy(1);
        emailschedulerDAO.save(emailSchedulerVO);
    }

    public String getCodeDescById(String id, String codeTypeId) throws Exception {
        return genericCodeDAO.getCodeDescById(id, codeTypeId);
    }

    public String getMoreInfo(TransactionBean transactionBean) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        List<TransactionBean> moreInfoList = transactionDAO.getMoreInfoList(transactionBean);
        request.setAttribute("moreInfoList", moreInfoList);
        return WebContextFactory.get().forwardToString("/jsps/AccountsPayable/ApInquiryMoreInfoTemplate.jsp");
    }

    public String getShowPaymentTemplate(String transactionId) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        NumberFormat number = new DecimalFormat("###,###,##0.00");
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        List<TransactionBean> paymentList = new ArrayList<TransactionBean>();
        Transaction transaction = transactionDAO.findById(Integer.parseInt(transactionId));
        if (null != transaction && null != transaction.getTransactionType()) {
            if (null != transaction.getStatus() && transaction.getStatus().trim().equals(CommonConstants.STATUS_PAID)) {
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setChequenumber(transaction.getChequeNumber());
                transactionBean.setAmount(null != transaction.getTransactionAmt() ? number.format(transaction.getTransactionAmt()) : "");
                transactionBean.setCheckDate(null != transaction.getCheckDate() ? DateUtils.parseDateToString(transaction.getCheckDate()) : "");
                paymentList.add(transactionBean);
            } else if (transaction.getTransactionType().trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                List<TransactionBean> tempList = paymentsDAO.getPaymentDetailsForApInquiry(transaction.getTransactionId().toString());
                if (null != tempList && !tempList.isEmpty()) {
                    paymentList.addAll(tempList);
                }
            }
        }
        request.setAttribute("paymentList", paymentList);
        return WebContextFactory.get().forwardToString("/jsps/AccountsPayable/ShowPaymentsTemplate.jsp");
    }

    public void addUserNameForBankAccount(HttpServletRequest request) throws Exception {
    }

    public List getBankName(String bankName) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        List bankList = new ArrayList();
        List<BankDetails> bankNameList = new ArrayList<BankDetails>();
        if (bankName != null && !bankName.trim().equals("")) {
            if (null != loginUser && null != loginUser.getRole() && null != loginUser.getRole().getRoleDesc() && (loginUser.getRole().getRoleDesc().trim().equalsIgnoreCase(CommonConstants.ROLE_NAME_SUPERVISOR) || loginUser.getRole().getRoleDesc().trim().equalsIgnoreCase(CommonConstants.ROLE_NAME_ADMIN))) {
                APPaymentBC aPPaymentBC = new APPaymentBC();
                bankNameList = aPPaymentBC.getBankNameList(loginUser, bankName);
            }
            for (BankDetails bankDetails : bankNameList) {
                if (null != bankDetails && null != bankDetails.getBankAcctNo() && !bankDetails.getBankAcctNo().trim().equals("") && null != bankDetails.getBankAddress() && !bankDetails.getBankAddress().trim().equals("")) {
                    bankList.add(bankDetails.getBankName() + "<-->" + bankDetails.getBankAcctNo() + ";  " + bankDetails.getBankAddress());
                } else if (null != bankDetails && null != bankDetails.getBankAcctNo() && !bankDetails.getBankAcctNo().trim().equals("")) {
                    bankList.add(bankDetails.getBankName() + "<-->" + bankDetails.getBankAcctNo());
                } else if (null != bankDetails && null != bankDetails.getBankAddress() && !bankDetails.getBankAddress().trim().equals("")) {
                    bankList.add(bankDetails.getBankName() + "<-->" + bankDetails.getBankAddress());
                }
            }
        }
        return bankList;
    }

    public static Object toObject(byte[] bytes) throws Exception {
        Object object = null;
        object = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
        return object;
    }

    public String validateAmountForAP(String paidTransaction) throws Exception {
        Transaction transaction = null;
        double totalPaidAmount = 0;
        Set<String> set = new HashSet<String>();
        String[] transactionIds = StringUtils.split(paidTransaction, ",");
        String vendorNames = "";
        for (String id : transactionIds) {
            transaction = transactionDAO.findById(Integer.parseInt(id));
            if (null != transaction) {
                if (null != transaction.getTransactionType() && transaction.getTransactionType().trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                    totalPaidAmount += transaction.getBalance();
                } else if (null != transaction.getTransactionType() && transaction.getTransactionType().trim().equals(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
                    totalPaidAmount -= transaction.getBalance();
                }
                if (set.add(transaction.getCustName().trim())) {
                    vendorNames = vendorNames + transaction.getCustName() + ",";
                }

            }
        }
        if (totalPaidAmount <= 0d) {
            vendorNames = StringUtils.substring(vendorNames, 0, vendorNames.length() - 1);
            return "Balance on selected items must be positive - [" + vendorNames + "]";
        } else {
            return "Success";
        }
    }

    public String drillDownForJE(String glAccountNumber, String lineItemNumber) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        List<TransactionBean> drillDownList = transactionLedgerDAO.getDrillDownForJE(glAccountNumber, lineItemNumber);
        request.setAttribute("drillDownList", drillDownList);
        return WebContextFactory.get().forwardToString("/jsps/Accounting/DrillDownTemplate.jsp");
    }

    public String getApSpecialist(String apSpecialist) throws Exception {
        UserDAO userDAO = new UserDAO();
        List<User> userList = userDAO.findUserForApSpecialist(apSpecialist, CommonConstants.ROLE_NAME_APSPECIALIST);
        if (null != userList && !userList.isEmpty()) {
            for (User user : userList) {
                if (null != user.getRole() && null != user.getRole().getRoleDesc() && user.getRole().getRoleDesc().trim().equals(CommonConstants.ROLE_NAME_APSPECIALIST)) {
                    apSpecialist = user.getLoginName();
                } else {
                    apSpecialist = "Not apSpecialist";
                }
            }
        } else {
            apSpecialist = "Not apSpecialist";
        }
        return apSpecialist;
    }

    public String validateBlNumber(String blNumber) throws Exception {
        FclBlDAO fclBlDAO = new FclBlDAO();
        FclBl fclBl = fclBlDAO.findById(blNumber);
        if (null != fclBl && null != fclBl.getBolId() && !fclBl.getBolId().trim().equals("")) {
            return fclBl.getBolId();
        } else {
            return "Error";
        }
    }

    public String saveAccruals(AccrualsForm accrualsForm) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        ServletContext servletContext = WebContextFactory.get().getServletContext();
        AccountPayableBC accountPayableBC = new AccountPayableBC();
        return accountPayableBC.saveAccruals(accrualsForm, request, servletContext, false);
    }

    public String addScanConfig(ScanForm scanForm) throws Exception {
        ScanDAO scanDAO = new ScanDAO();
        if (null != scanForm.getPageAction() && scanForm.getPageAction().equals("delete")) {
            ScanConfig scanConfig = scanDAO.findById(scanForm.getId());
            scanDAO.delete(scanConfig);
            return "Added Successfully";
        } else if (!scanDAO.validateScanConfig(scanForm)) {
            if (null != scanForm.getPageAction() && scanForm.getPageAction().equals("add")) {
                ScanConfig scanConfig = new ScanConfig(scanForm);
                scanDAO.save(scanConfig);
            } else if (null != scanForm.getPageAction() && scanForm.getPageAction().equals("update")) {
                ScanConfig scanConfig = scanDAO.findById(scanForm.getId());
                scanConfig.setDocumentName(scanForm.getDocumentName().toUpperCase());
//                scanConfig.setDocumentType(scanForm.getDocumentType().toUpperCase());
            }
            return "Added Successfully";
        } else {
            return "The Screen name and Document type already exists";
        }
    }

    public String deleteScanConfig(ScanForm scanForm) throws Exception {
        ScanDAO scanDAO = new ScanDAO();
        if (null != scanForm.getPageAction() && scanForm.getPageAction().equals("delete")) {
            ScanConfig scanConfig = scanDAO.findById(scanForm.getId());
            scanDAO.delete(scanConfig);
        }
        return "Deleted Successfully";
    }

    @SuppressWarnings("unchecked")
    public String updateSubLedgerCodeForSubLedgers(String subLedgerCode, String transactionId) throws Exception {
        String result = "";
        SubledgerDAO subledgerDAO = new SubledgerDAO();
        List list = subledgerDAO.findByProperty("subLedgerCode", subLedgerCode);
        if (null != list && !list.isEmpty()) {
            TransactionLedgerDAO transLedgerDAO = new TransactionLedgerDAO();
            TransactionLedger transLedger = transLedgerDAO.findById(Integer.parseInt(transactionId));
            transLedger.setSubledgerSourceCode(subLedgerCode);
            transLedgerDAO.update(transLedger);
            result = "SubLedgerCode is Successfully Updated";
        } else {
            result = "Invalid SubLedgerCode entered.  Please enter a valid SubLedgerCode";
        }
        return result;
    }

    public String validateEndDateForSubLedger(String glPeriod, String endDate) throws Exception {
        Date endPeriodDate = DateUtils.parseToDate(endDate);
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        FiscalPeriod fiscalPeriod = fiscalPeriodDAO.findById(Integer.parseInt(glPeriod));
        if (fiscalPeriod.getEndDate().compareTo(endPeriodDate) < 0) {
            return "The end date entered extends beyond the period. Please enter an end date within the period or less than the period";
        } else {
            return CommonConstants.MESSAGE_AVAILABLE;
        }
    }

    public String checkMissingGLAccountsForSubLedger(String startDate, String endDate, String subLedgerType) throws Exception {
        boolean result = new AccountingLedgerDAO().checkMissingGLAccountsForSubLedger(startDate, endDate, subLedgerType);
        if (result) {
            return "Some records are missing GL Accounts - they will not be posted to the GL - Do you want to continue?";
        } else {
            return CommonConstants.MESSAGE_AVAILABLE;
        }
    }

    public PaymentMethod getPaymentMethod(String paymentMethodId, String tradingPartnerId) throws Exception {
        PaymentMethod paymentMethod = null;
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerId);
        Set<PaymentMethod> paymentSet = tradingPartner.getPaymentset();
        for (PaymentMethod paymentMethods : paymentSet) {
            if (null != paymentMethodId && paymentMethodId.equals(paymentMethods.getId().toString())) {
                paymentMethod = paymentMethods;
            }
        }
        return paymentMethod;
    }

    public boolean tabProcessInfo(int userid) throws Exception {
        return dbUtil.tabProcessInfo(userid);
    }

    public String printCheckRegistgerDetails(CheckRegisterForm checkRegisterForm) throws Exception {
        String realPath = WebContextFactory.get().getServletContext().getRealPath("/");
        return new CheckRegisterBC().createReport(checkRegisterForm, realPath);
    }

    public String setFileList(String bolId, HttpServletRequest request) throws Exception {
        String result = "";
        FclBl fclBl = new FclBlDAO().findById((null != bolId && !bolId.equals("")) ? Integer.parseInt(bolId) : 0);
        if (null != fclBl) {
            LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
            HttpSession session = request.getSession();
            if (session.getAttribute("SearchListByfileNumber") != null) {
                List getFileList = (List) session.getAttribute("SearchListByfileNumber");
                for (int i = 0; i < getFileList.size(); i++) {
                    FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking = (FileNumberForQuotaionBLBooking) getFileList.get(i);
                    if (fileNumberForQuotaionBLBooking.getFileNo() != null && fileNumberForQuotaionBLBooking.getFileNo().equals(fclBl.getFileNo())) {
                        if (fileNumberForQuotaionBLBooking.getFclBlId() != null) {
                            if (fclBl.getBolId() != null) {
                                FclBlCorrectionsDAO fbcdao = new FclBlCorrectionsDAO();

                                if (null != fbcdao.getLatestUnPostedNotice(fclBl.getBolId(), null)) {
                                    fileNumberForQuotaionBLBooking.setCorrectionsPresent("Corrections Exist");
                                    result = "Corrections Exist";
                                } else {
                                    fileNumberForQuotaionBLBooking.setCorrectionsPresent(null);
                                }
                                String status = "";
                                if (null != fileNumberForQuotaionBLBooking.getFclBlStatus()) {
                                    status = fileNumberForQuotaionBLBooking.getFclBlStatus().replaceAll("null", "");
                                }
                                status = new StringFormatter().compareStatus(status, fclBl);
                                QuotationBC quotationBC = new QuotationBC();
                                if (fclBl.getFclcontainer() != null) {
                                    Iterator iter = fclBl.getFclcontainer().iterator();
                                    while (iter.hasNext()) {
                                        FclBlContainer fclBlConatiner = (FclBlContainer) iter.next();
                                        List hazmatList = quotationBC.getHazmatList("FclBl", fclBlConatiner.getTrailerNoId().toString());
                                        if (hazmatList.size() > 0) {
                                            fileNumberForQuotaionBLBooking.setHazmat("H");
                                            break;
                                        }
                                    }
                                }
                                fileNumberForQuotaionBLBooking.setFclBlStatus(CommonFunctions.isNotNull(status) ? status.replaceAll(",,", ",") : "");
                                fileNumberForQuotaionBLBooking.setManifest(null != fclBl.getReadyToPost() ? fclBl.getReadyToPost() : "");
                                fileNumberForQuotaionBLBooking.setDoorOrigin(fclBl.getDoorOfOrigin());
                                fileNumberForQuotaionBLBooking.setBlClosed(fclBl.getBlClosed());
                                fileNumberForQuotaionBLBooking.setBlAudit(fclBl.getBlAudit());
                                Integer aesStatus = logFileEdiDAO.getSedCount(fclBl.getFileNo());
                                String masterstatus = logFileEdiDAO.findMasterStatusFileNo(fclBl.getFileNo());
                                String dockReceipt = "04" + fclBl.getFileNo();
                                String _304Succcess = logFileEdiDAO.findDrNumberStatus(dockReceipt, "success");
                                String _304Failure = logFileEdiDAO.findDrNumberStatus(dockReceipt, "failure");
                                Integer _997Succcess = logFileEdiDAO.find997Status(fclBl.getFileNo());
                                fileNumberForQuotaionBLBooking.setAesCount(aesStatus);
                                fileNumberForQuotaionBLBooking.setDocumentStatus(masterstatus);
                                fileNumberForQuotaionBLBooking.set304Success(_304Succcess);
                                fileNumberForQuotaionBLBooking.set304Failure(_304Failure);
                                fileNumberForQuotaionBLBooking.set997Success(null != _997Succcess ? "" + _997Succcess : "");
                            }
                        }
                        getFileList.set(i, fileNumberForQuotaionBLBooking);
                        break;
                    }
                }
                Collections.sort(getFileList, new FileNumberForQuotaionBLBooking());
                session.setAttribute(QuotationConstants.FILESEARCHLIST, getFileList);
            }
        }
        return result;
    }

    public List<LabelValueBean> getChargeCode(String shipmentType , String transactionType) throws Exception {
        return new GlMappingDAO().getChargeCodeByShipmentType(shipmentType, transactionType);
    }

    public void submitRemarksLookUp(String remarks) {
        //String[] selectedChecks = remarksLookUpForm.getRcheck();
    }

    public void deleteOrAddFFCommission(String bolId, String action, String acctName, String acctNo, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(bolId)) {
            List<FclBlCostCodes> costCodesList = new FclBlCostCodesDAO().findByParentIdAndCostCode(new Integer(bolId), FclBlConstants.FFCODE);
            if ("delete".equalsIgnoreCase(action)) {
                for (FclBlCostCodes costCode : costCodesList) {
                    new FclBlCostCodesDAO().delete(costCode);
                }
            } else {
                if (("true").equalsIgnoreCase(new FclBlCostCodesDAO().IsFFCommissionRegionCode(bolId))) {
                    if (CommonUtils.isEmpty(costCodesList) && CommonUtils.isNotEmpty(acctNo)) {
                        HttpSession session = request.getSession();
                        User loginUser = (User) session.getAttribute("loginuser");
                        FclBlCostCodes fclBlCostCodes = new FclBlCostCodes();
                        String ffCommissionRates[] = LoadLogisoftProperties.getProperty("ffcommissionrates").split(",");
                        double amount = 0d;
                        FclBl fclBl = new FclBlDAO().findById(new Integer(bolId));
                        Set containerSet = fclBl.getFclcontainer();
                        for (Object object : containerSet) {
                            FclBlContainer container = (FclBlContainer) object;
                            if ("A".equalsIgnoreCase(container.getSizeLegend().getCode())) {
                                amount += Double.parseDouble(ffCommissionRates[0].replace("-", ""));
                            } else {
                                amount += Double.parseDouble(ffCommissionRates[1].replace("-", ""));
                            }
                        }
                        fclBlCostCodes.setBolId(new Integer(bolId));
                        fclBlCostCodes.setCostCode(FclBlConstants.FFCODE);
                        fclBlCostCodes.setCostCodeDesc(FclBlConstants.FFCODEDESC);
                        fclBlCostCodes.setAmount(amount);
                        fclBlCostCodes.setCurrencyCode("USD");
                        fclBlCostCodes.setBookingFlag("M");
                        fclBlCostCodes.setReadOnlyFlag("on");
                        fclBlCostCodes.setManifestModifyFlag("yes");
                        fclBlCostCodes.setAccName(acctName);
                        fclBlCostCodes.setAccNo(acctNo);
                        fclBlCostCodes.setAccrualsCreatedDate(new Date());
                        fclBlCostCodes.setAccrualsCreatedBy(loginUser.getLoginName());
                        new FclBlCostCodesDAO().save(fclBlCostCodes);
                    }
                }
            }
        }
        //String[] selectedChecks = remarksLookUpForm.getRcheck();
    }

    public void checkUnCheckRemrks(String id, String event, HttpServletRequest request) {
        Set s = null;
        if (null != request.getSession().getAttribute("checkSet")) {
            s = (HashSet) request.getSession().getAttribute("checkSet");
        } else {
            s = new HashSet();
        }
        if ("check".equals(event)) {
            s.add(id);
        } else {
            s.remove(id);
        }
        request.getSession().setAttribute("checkSet", s);
    }

    public String checkUnLoc(String code) throws Exception {
        String result = "";
        UnLocationDAO dAO = new UnLocationDAO();
        result = dAO.noDuplicateunLocationCode(code);
        return result;
    }

    public Boolean checkDescription(String description) throws Exception {
        UnitType unitType = null;
        String desc = description.trim();
        if (CommonUtils.isNotEmpty(desc)) {
            unitType = new UnitTypeDAO().getByProperty("description", desc);
            if (null != unitType && desc.equalsIgnoreCase(unitType.getDescription())) {
                return true;
            }
        }
        return false;
    }

    public Integer resendToBlueScreen(String fileNo, HttpServletRequest request) throws Exception {
        FclBl fclBl = new FclBlDAO().getFileNoObject(fileNo);
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        if (null != fclBl) {
            new NotesBC().saveNotes(fileNo, loginUser.getLoginName(), "Bl Resent to Bluescreen");
            return new FclBlDAO().resendToBlueScreen(fclBl, loginUser);
        }
        return 0;
    }

    public boolean checkAgentDestinationExist(String destination, String userId) throws Exception {
        User user = new UserDAO().findById(Integer.parseInt(userId));
        String unLocCode = "";
        if (destination.lastIndexOf("(") != -1 && destination.lastIndexOf(")") != -1) {
            unLocCode = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
            Ports ports = new PortsDAO().getPorts(unLocCode);
            List l = new UserDAO().findByUserAgentProperties("portId.id", ports.getId(), "userId.userId", user.getUserId());
            if (!l.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void addAgentInformation(String destination, String userId, HttpServletRequest request) throws Exception {
        User user = new UserDAO().findById(Integer.parseInt(userId));
        UserAgentInformation agent = new UserAgentInformation();
        String unLocCode = "";
        if (destination.lastIndexOf("(") != -1 && destination.lastIndexOf(")") != -1) {
            unLocCode = destination.substring(destination.lastIndexOf("(") + 1, destination.lastIndexOf(")"));
        }
        if (CommonUtils.isNotEmpty(unLocCode)) {
            agent.setUserId(user);
            agent.setPortId(new PortsDAO().getPorts(unLocCode));
            new UserDAO().saveAgentInformation(agent);
        }
        user = new UserDAO().findById(Integer.parseInt(userId));
        request.setAttribute("agentInformation", new UserDAO().findByUserAgentProperty("userId.userId", user.getUserId()));
    }

    public void deleteAgentInformation(String agentId, String userId, HttpServletRequest request) throws Exception {
        UserAgentInformation agent = new UserDAO().findAgentUserId(Integer.parseInt(agentId));
        if (null != agent) {
            new UserDAO().delete(agent);
        }
        User user = new UserDAO().findById(Integer.parseInt(userId));
        request.setAttribute("agentInformation", new UserDAO().findByUserAgentProperty("userId.userId", user.getUserId()));
    }

    public List deleteAttachedDocuments(String id) throws Exception {
        DocumentStoreLog document = documentStoreLogDAO.findById(Integer.parseInt(id));
        String fileName = document.getFileName();
        List arrayList = new ArrayList();
        File file = new File(document.getFileLocation(), fileName);
        file.delete();
        documentStoreLogDAO.delete(document);
        List<DocumentStoreLog> ssMasterDoc = documentStoreLogDAO.getSSMasterDocuments(document.getDocumentID(), document.getScreenName());
        arrayList.add("Document - " + fileName + " is deleted sucessfully");
        if ("SS LINE MASTER BL".equalsIgnoreCase(document.getDocumentName()) && CommonUtils.isEmpty(ssMasterDoc)) {
            new ScanDAO().updateMasterReceived(document.getDocumentID(), "No");
        }
        String getStatus = documentStoreLogDAO.getSsMasterStatus(document.getDocumentID(), document.getScreenName(), document.getDocumentName());
        arrayList.add(getStatus);
        List<DocumentStoreLog> docList = documentStoreLogDAO.getDocumentStoreLogForViewAll(document.getDocumentID(), document.getScreenName());
        arrayList.add(docList.size());
        arrayList.add(document.getDocumentName());
        return arrayList;
    }
}
