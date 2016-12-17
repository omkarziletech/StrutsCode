package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclVoyageArInvoiceForm;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.form.ARRedInvoiceForm;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.thread.ArInvoiceNumberThread;
import com.logiware.utils.AuditNotesUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class ArRedInvoiceBC {

    ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
    GlMappingDAO glmappDAO = new GlMappingDAO();
    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
    TransactionDAO transactionDAO = new TransactionDAO();

    public ArRedInvoice updateArRedInvoice(ARRedInvoiceForm arRedInvoiceForm, User loginuser) throws Exception {
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(new Integer(arRedInvoiceForm.getArRedInvoiceId()));
        if (arRedInvoice.getCustomerNumber().equals(arRedInvoiceForm.getAccountNumber())) {
            StringBuilder desc = new StringBuilder("Updated Invoice '").append(arRedInvoice.getInvoiceNumber()).append("'");
            desc.append(" of '").append(arRedInvoice.getCustomerName()).append("'");
            desc.append(" by ").append(loginuser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            String moduleRefId = "";
            if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
                moduleRefId = arRedInvoiceForm.getFileNo();
            } else {
                moduleRefId = arRedInvoice.getInvoiceNumber();
            }
            new NotesBC().saveNotes(moduleRefId, loginuser.getLoginName(), desc.toString());
        }
        arRedInvoice.updateValues(arRedInvoiceForm);
        arRedInvoice.setInvoiceNumber(arRedInvoiceForm.getInvoiceNumber());
        arRedInvoice.setInvoiceBy(loginuser.getFirstName() + " " + loginuser.getLastName());
        arRedInvoiceDAO.update(arRedInvoice);
        return arRedInvoice;
    }

    public ArRedInvoice getArRedInvoice(Integer invoiceId) throws Exception {
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.findById(invoiceId);
        return arRedInvoice;
    }

    public ArRedInvoice saveArRedInvoice(ARRedInvoiceForm arRedInvoiceForm, User loginUser) throws Exception {
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.getInvoice(arRedInvoiceForm.getAccountNumber(), arRedInvoiceForm.getInvoiceNumber());
        if (null == arRedInvoice) {
            arRedInvoice = new ArRedInvoice(arRedInvoiceForm);
        } else {
            arRedInvoice.updateValues(arRedInvoiceForm);
        }
        arRedInvoice.setInvoiceBy(loginUser.getFirstName() + " " + loginUser.getLastName());
        arRedInvoice.setStatus(CommonConstants.STATUS_IN_PROGRESS);
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
            arRedInvoice.setFileNo(arRedInvoiceForm.getFileNo());
        } else if (CommonUtils.isNotEmpty(arRedInvoiceForm.getBl_drNumber())) {
            arRedInvoice.setFileNo(arRedInvoiceForm.getBl_drNumber().replace("04-", ""));
        }
        arRedInvoice.setScreenName(arRedInvoiceForm.getScreenName());
        if (null != arRedInvoice.getId() && arRedInvoice.getId() != 0) {
            arRedInvoice.setInvoiceNumber(arRedInvoiceForm.getInvoiceNumber());
            arRedInvoiceDAO.update(arRedInvoice);
        } else {
            ArInvoiceNumberThread thread = new ArInvoiceNumberThread();
            arRedInvoice.setInvoiceNumber(thread.getInvoiceNumber());
            StringBuilder desc = new StringBuilder("Invoice '").append(arRedInvoice.getInvoiceNumber()).append("'");
            desc.append(" of '").append(arRedInvoice.getCustomerName()).append("'");
            desc.append(" created by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_RED_INVOICE, arRedInvoice.getCustomerNumber() + "-" + arRedInvoice.getInvoiceNumber(), NotesConstants.AP_INVOICE, loginUser);
            arRedInvoiceDAO.save(arRedInvoice);
        }
        return arRedInvoice;
    }

    public ArRedInvoice saveArRedInvoiceDetails(ARRedInvoiceForm arRedInvoiceForm, User loginUser) throws Exception {
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.getInvoice(arRedInvoiceForm.getAccountNumber(), arRedInvoiceForm.getInvoiceNumber());
        if (null == arRedInvoice) {
            arRedInvoice = new ArRedInvoice(arRedInvoiceForm);
        } else {
            arRedInvoice.updateValues(arRedInvoiceForm);
        }
        arRedInvoice.setInvoiceBy(loginUser.getFirstName() + " " + loginUser.getLastName());
        if (null != arRedInvoice.getId() && arRedInvoice.getId() != 0) {
            arRedInvoice.setInvoiceNumber(arRedInvoiceForm.getInvoiceNumber());
            arRedInvoiceDAO.update(arRedInvoice);
        } else {
            arRedInvoice.setStatus(CommonConstants.STATUS_OPEN);
            arRedInvoice.setFileNo(arRedInvoiceForm.getFileNo());
            arRedInvoice.setScreenName(arRedInvoiceForm.getScreenName());
            ArInvoiceNumberThread thread = new ArInvoiceNumberThread();
            arRedInvoice.setInvoiceNumber(thread.getInvoiceNumber());
            StringBuilder desc = new StringBuilder("Created Invoice '").append(arRedInvoice.getInvoiceNumber()).append("'");
            desc.append(" of '").append(arRedInvoice.getCustomerName()).append("'");
            desc.append(" created by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            String moduleRefId = "";
            if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
                moduleRefId = arRedInvoiceForm.getFileNo();
            } else {
                moduleRefId = arRedInvoice.getInvoiceNumber();
            }
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.FILE, moduleRefId, null, loginUser);
            arRedInvoiceDAO.save(arRedInvoice);
        }
        return arRedInvoice;
    }

    public ArRedInvoice saveAgentInvoice(List<ImportsManifestBean> agentList, User loginUser, Double invoiceAmount, String unitSSId,
            boolean save, LclVoyageArInvoiceForm lclVoyageArInvoiceForm) throws Exception {
        ArRedInvoice arRedInvoice = null;
        if (CommonUtils.isNotEmpty(agentList)) {
            ImportsManifestBean manifestBean = agentList.get(0);
            arRedInvoice = new ArRedInvoice();
            arRedInvoice.setInvoiceBy(loginUser.getFirstName() + " " + loginUser.getLastName());
            arRedInvoice.setStatus(CommonConstants.STATUS_OPEN);
            arRedInvoice.setFileNo(unitSSId);
            arRedInvoice.setCustomerName(manifestBean.getAgentName());
            arRedInvoice.setCustomerNumber(manifestBean.getAgentNo());
            arRedInvoice.setCustomerType(manifestBean.getAgentAcctType());
            if (invoiceAmount != 0.0) {
                arRedInvoice.setInvoiceAmount(invoiceAmount);
            } else {
                arRedInvoice.setInvoiceAmount(manifestBean.getTotalCharges());
            }
            arRedInvoice.setDate(new Date());
            arRedInvoice.setPrintOnDr(Boolean.FALSE);
            if (unitSSId != null && !"".equals(unitSSId) && save) {
                ArInvoiceNumberThread thread = new ArInvoiceNumberThread();
                arRedInvoice.setInvoiceNumber(thread.getInvoiceNumber());
                if ("Yes".equalsIgnoreCase(lclVoyageArInvoiceForm.getPrintOnDrFlag())) {
                    arRedInvoice.setPrintOnDr(Boolean.TRUE);
                }
                StringBuilder desc = new StringBuilder("Created Invoice '").append(arRedInvoice.getInvoiceNumber()).append("'");
                desc.append(" of '").append(arRedInvoice.getCustomerName()).append("'");
                desc.append(" created by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                if (lclVoyageArInvoiceForm != null && "Imports".equalsIgnoreCase(lclVoyageArInvoiceForm.getSelectedMenu())) {
                    arRedInvoice.setFileType("LCLI");
                    arRedInvoice.setScreenName("IMP VOYAGE");
                } else {
                    arRedInvoice.setFileType("LCLE");
                }
                LclUnitSs lclunitss = new LclUnitSsDAO().findById(Long.parseLong(unitSSId));
                arRedInvoice.setBlNumber(lclunitss.getLclUnit().getUnitNo());
                if (lclunitss != null) {
                    new LclUnitSsRemarksDAO().insertUnitSsRemarks(lclunitss.getLclSsHeader(), lclunitss.getLclUnit(), "auto", null, null, desc.toString(), loginUser);
                }
                arRedInvoiceDAO.save(arRedInvoice);
            }
        }
        return arRedInvoice;
    }

    public List<ArRedInvoice> getInvoices(ARRedInvoiceForm arRedInvoiceForm) throws Exception {
        List<ArRedInvoice> apInvoiceList = new ArrayList<ArRedInvoice>();
        apInvoiceList = arRedInvoiceDAO.getInvoices(arRedInvoiceForm.getCusName(), arRedInvoiceForm.getAccountNumber(), arRedInvoiceForm.getInvoiceNumber());
        return apInvoiceList;
    }

    public ArRedInvoice getInvoiceForEdit(String invoiceNumber, String invoiceId) throws Exception {
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.findByInvoiceNumber(invoiceNumber, invoiceId);
        return arRedInvoice;
    }

    public ArRedInvoice getInvoiceForEditByInvoiceNumber(String invoiceNumber) throws Exception {
        ArRedInvoice arRedInvoice = arRedInvoiceDAO.findByInvoiceNumber(invoiceNumber, null);
        return arRedInvoice;
    }

    public String getCreditTermDesc(String creditTerm) throws Exception {
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        return genericCodeDAO.getCodeDescById(creditTerm, "29");
    }

    public ArRedInvoice getAPInvoice(String invoiceId) throws Exception {
        return getInvoiceForEdit(null, invoiceId);
    }

    public void unManifestAccruals(ArRedInvoice arRedInvoice, List lineItemList, String userName) throws Exception {
        ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
        arTransactionHistory.setInvoiceDate(arRedInvoice.getDate());
        arTransactionHistory.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
        Date postedDate = new AccrualsDAO().getPostedDate(arRedInvoice.getDate());
        arTransactionHistory.setPostedDate(postedDate);
        arTransactionHistory.setTransactionDate(arRedInvoice.getDate());
        arTransactionHistory.setTransactionAmount(0 - arRedInvoice.getInvoiceAmount());
        arTransactionHistory.setCustomerNumber(arRedInvoice.getCustomerNumber());
        arTransactionHistory.setCreatedBy(userName);
        arTransactionHistory.setCreatedDate(new Date());
        arTransactionHistory.setTransactionType("VOID");
        new ArTransactionHistoryDAO().save(arTransactionHistory);
        Transaction transaction = new TransactionDAO().findByTransactionByInvoiceNoAndCustomer(arRedInvoice.getInvoiceNumber(), arRedInvoice.getCustomerNumber());
        if (null != transaction) {
            transaction.setBalance(transaction.getBalance() - arRedInvoice.getInvoiceAmount());
            transaction.setBalanceInProcess(transaction.getBalanceInProcess() - arRedInvoice.getInvoiceAmount());
            transaction.setTransactionAmt(transaction.getTransactionAmt() - arRedInvoice.getInvoiceAmount());
            transaction.setManifestFlag("N");
        }
        FclBlDAO fclBlDAO = new FclBlDAO();
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        QuotationDAO quotationDAO = new QuotationDAO();
        for (Object object : lineItemList) {
            TransactionLedger transactionLedger = new TransactionLedger();
            ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
            String fileNo = StringUtils.removeStart(arRedInvoiceCharges.getBlDrNumber(), "04-");
            FclBl bl = fclBlDAO.getFileNoObject(fileNo);
            BookingFcl booking = null;
            Quotation quotation = null;
            if (bl == null) {
                booking = bookingFclDAO.getFileNoObject(fileNo);
            } else if (booking == null) {
                quotation = quotationDAO.getFileNoObject(fileNo);
            }
            if (bl != null) {
                transactionLedger.setBillLaddingNo(bl.getBolId());
                transactionLedger.setVoyageNo(String.valueOf(bl.getVoyages()));
            }
            transactionLedger.setChargeCode(arRedInvoiceCharges.getChargeCode());
            transactionLedger.setTransactionDate(arTransactionHistory.getTransactionDate());
            transactionLedger.setPostedDate(postedDate);
            transactionLedger.setGlAccountNumber(arRedInvoiceCharges.getGlAccount());
            transactionLedger.setTransactionAmt(0 - arRedInvoiceCharges.getAmount());
            transactionLedger.setCurrencyCode("USD");
            transactionLedger.setSubledgerSourceCode("AR-" + arRedInvoiceCharges.getShipmentType());
            transactionLedger.setShipmentType(arRedInvoiceCharges.getShipmentType());
            transactionLedger.setCustName(arRedInvoice.getCustomerName());
            transactionLedger.setCustNo(arRedInvoice.getCustomerNumber());
            transactionLedger.setTransactionType("AR");
            transactionLedger.setBalance(0 - arRedInvoiceCharges.getAmount());
            transactionLedger.setBalanceInProcess(0 - arRedInvoiceCharges.getAmount());
            transactionLedger.setStatus("Open");
            transactionLedger.setBillTo("Y");
            transactionLedger.setManifestFlag("N");
            transactionLedger.setInvoiceNumber(arRedInvoiceCharges.getInvoiceNumber());
            if (fileNo.contains("-")) {
                transactionLedger.setDocReceipt(StringUtils.substringBefore(fileNo, "-"));
            } else {
                transactionLedger.setDocReceipt(fileNo);
            }   
            if (bl != null) {
                transactionLedger.setSailingDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
            } else if (booking != null) {
                transactionLedger.setSailingDate("I".equalsIgnoreCase(booking.getImportFlag()) ? booking.getEta() : booking.getEtd());
            } else if (quotation != null) {
                transactionLedger.setSailingDate(quotation.getQuoteDate());
            }
            new TransactionLedgerDAO().save(transactionLedger);

        }
    }

    public void manifestAccruals(ArRedInvoice arRedInvoice, List lineItemList, String userName) throws Exception {
        Date postedDate = new AccrualsDAO().getPostedDate(arRedInvoice.getDate());
        Transaction transaction = new TransactionDAO().findByTransactionByInvoiceNoAndCustomer(arRedInvoice.getInvoiceNumber(), arRedInvoice.getCustomerNumber());
        if (null != transaction) {
            transaction.setTransactionAmt(transaction.getTransactionAmt() + arRedInvoice.getInvoiceAmount());
            transaction.setBalance(transaction.getBalance() + arRedInvoice.getInvoiceAmount());
            transaction.setBalanceInProcess(transaction.getBalanceInProcess() + arRedInvoice.getInvoiceAmount());
            transaction.setManifestFlag("R");
            if (arRedInvoice.getFileNo().contains("-")) {
                transaction.setDocReceipt(StringUtils.substringBefore(arRedInvoice.getFileNo(), "-"));
            } else {
                transaction.setDocReceipt(arRedInvoice.getFileNo());
            }
            transactionDAO.update(transaction);
        } else {
            transaction = new Transaction();
            transaction.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
            transaction.setTransactionDate(arRedInvoice.getDate());
            transaction.setDueDate(arRedInvoice.getDueDate());
            transaction.setPostedDate(postedDate);
            transaction.setTransactionAmt(arRedInvoice.getInvoiceAmount());
            transaction.setCustName(arRedInvoice.getCustomerName());
            transaction.setCustNo(arRedInvoice.getCustomerNumber());
            transaction.setTransactionType("AR");
            transaction.setBalance(arRedInvoice.getInvoiceAmount());
            transaction.setBalanceInProcess(arRedInvoice.getInvoiceAmount());
            transaction.setStatus("Open");
            transaction.setBillTo("Y");
            String creditStatus = new CustomerAccountingDAO().getCreditStatus(transaction.getCustNo());
            transaction.setCreditHold(CommonUtils.isEqualIgnoreCase(creditStatus, "In Good Standing") ? "N" : "Y");
            transaction.setManifestFlag("R");
            transaction.setEmailed(false);
            if (arRedInvoice.getFileNo().contains("-")) {
                transaction.setDocReceipt(StringUtils.substringBefore(arRedInvoice.getFileNo(), "-"));
            } else {
                transaction.setDocReceipt(arRedInvoice.getFileNo());
            }
            new TransactionDAO().save(transaction);
        }
        ArRedInvoiceCharges charge = (ArRedInvoiceCharges) lineItemList.get(0);
        ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
        arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
        arTransactionHistory.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
        arTransactionHistory.setPostedDate(postedDate);
        arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
        arTransactionHistory.setTransactionAmount(arRedInvoice.getInvoiceAmount());
        arTransactionHistory.setCustomerNumber(transaction.getCustNo());
        arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
        arTransactionHistory.setCreatedBy(userName);
        arTransactionHistory.setCreatedDate(new Date());
        arTransactionHistory.setTransactionType(charge.getShipmentType());
        new ArTransactionHistoryDAO().save(arTransactionHistory);
        FclBlDAO fclBlDAO = new FclBlDAO();
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        QuotationDAO quotationDAO = new QuotationDAO();
        for (Object object : lineItemList) {
            ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
            TransactionLedger transactionLedger = new TransactionLedger();
            String fileNo = StringUtils.removeStart(arRedInvoiceCharges.getBlDrNumber(), "04-");
            FclBl bl = fclBlDAO.getFileNoObject(fileNo);
            BookingFcl booking = null;
            Quotation quotation = null;
            if (bl == null) {
                booking = bookingFclDAO.getFileNoObject(fileNo);
            } else if (booking == null) {
                quotation = quotationDAO.getFileNoObject(fileNo);
            }
            if (bl != null) {
                transactionLedger.setBillLaddingNo(bl.getBolId());
                transactionLedger.setVoyageNo(String.valueOf(bl.getVoyages()));
            }
            transactionLedger.setChargeCode(arRedInvoiceCharges.getChargeCode());
            transactionLedger.setTransactionDate(transaction.getTransactionDate());
            transactionLedger.setPostedDate(postedDate);
            transactionLedger.setGlAccountNumber(arRedInvoiceCharges.getGlAccount());
            transactionLedger.setTransactionAmt(arRedInvoiceCharges.getAmount());
            transactionLedger.setCurrencyCode("USD");
            transactionLedger.setSubledgerSourceCode("AR-" + charge.getShipmentType());
            transactionLedger.setShipmentType(arRedInvoiceCharges.getShipmentType());
            transactionLedger.setCustName(arRedInvoice.getCustomerName());
            transactionLedger.setCustNo(arRedInvoice.getCustomerNumber());
            transactionLedger.setTransactionType("AR");
            transactionLedger.setBalance(arRedInvoiceCharges.getAmount());
            transactionLedger.setBalanceInProcess(arRedInvoiceCharges.getAmount());
            transactionLedger.setStatus("Open");
            transactionLedger.setBillTo("Y");
            transactionLedger.setManifestFlag("R");
            transactionLedger.setInvoiceNumber(arRedInvoiceCharges.getInvoiceNumber());
            if (fileNo.contains("-")) {
                transactionLedger.setDocReceipt(StringUtils.substringBefore(fileNo, "-"));
            } else {
                transactionLedger.setDocReceipt(fileNo);
            }         
            if (bl != null) {
                transactionLedger.setSailingDate("I".equalsIgnoreCase(bl.getImportFlag()) ? bl.getEta() : bl.getSailDate());
            } else if (booking != null) {
                transactionLedger.setSailingDate("I".equalsIgnoreCase(booking.getImportFlag()) ? booking.getEta() : booking.getEtd());
            } else if (quotation != null) {
                transactionLedger.setSailingDate(quotation.getQuoteDate());
            }
            transactionLedgerDAO.save(transactionLedger);
        }
    }

    public List<TradingPartner> getEMailFaxCustAcct(String accountNo) throws Exception {
        List resultList = new ArrayList();
        TradingPartner tradingPartner = new TradingPartner();
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        if (null != accountNo) {
            tradingPartner = tradingPartnerDAO.findById(accountNo);
            if (CommonUtils.isNotEmpty(tradingPartner.getAccounting())) {
                for (Iterator iter = tradingPartner.getAccounting().iterator(); iter.hasNext();) {
                    CustomerAccounting customerAccounting = (CustomerAccounting) iter.next();
                    tradingPartner.setContact(customerAccounting.getContact());
                    tradingPartner.setEmail(customerAccounting.getAcctRecEmail());
                    tradingPartner.setFax(customerAccounting.getArFax());
                }
            }
            resultList.add(tradingPartner);
        }
        return resultList;
    }
}
