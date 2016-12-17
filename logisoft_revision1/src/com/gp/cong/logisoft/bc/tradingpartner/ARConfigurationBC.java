package com.gp.cong.logisoft.bc.tradingpartner;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.gp.cvst.logisoft.domain.Transaction;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.utils.AuditNotesUtils;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

public class ARConfigurationBC {

    TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    UserDAO userDAO = new UserDAO();

    public Set<CustomerContact> getCustomerContacts(String tradingPartnerId) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerId);
        if (null != tradingPartner) {
            Set<CustomerContact> customerContacts = tradingPartner.getCustomerContact();
            return customerContacts;
        }
        return null;
    }

    public Set<CustomerAddress> getCustomerAddress(String tradingPartnerId) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerId);
        if (null != tradingPartner) {
            Set<CustomerAddress> customerContacts = tradingPartner.getCustomerAddressSet();
            //replace unicode object with city name
            for (CustomerAddress address : customerContacts) {
                if (null != address.getCity1()) {
                    address.setCity2(address.getCity1().getUnLocationName());
                }
            }
            return customerContacts;
        }
        return null;
    }

    public List getMasterCustomerAddress() throws Exception {
        List addressList = tradingPartnerDAO.findMasterAddressess();
        return addressList;
    }

    public TradingPartnerForm setContactDetails(TradingPartnerForm tradingPartnerForm, String tradingPartnerId, Integer selectedContactId) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerId);
        Set<CustomerContact> list = tradingPartner.getCustomerContact();
        if (null != list) {
            for (CustomerContact customerContact : list) {
                if (null != selectedContactId && selectedContactId.equals(customerContact.getId())) {
                    tradingPartnerForm.setContact(customerContact.getFirstName() + " " + customerContact.getLastName());
                    tradingPartnerForm.setArPhone(customerContact.getPhone());
                    if (null != customerContact.getExtension() && !customerContact.getExtension().trim().equals("") && null != customerContact.getPhone() && !customerContact.getPhone().trim().equals("")) {
                        tradingPartnerForm.setArPhone(customerContact.getPhone()+" x"+customerContact.getExtension());
                    }
                    tradingPartnerForm.setArFax(customerContact.getFax());
                    tradingPartnerForm.setAcctRecEmail(customerContact.getEmail());
                    return tradingPartnerForm;
                }
            }
        }
        return tradingPartnerForm;
    }

    public TradingPartnerForm setInvoiceAddressDetails(TradingPartnerForm tradingPartnerForm, String tradingPartnerId, Integer selectedContactId) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerId);
        Set<CustomerAddress> list = tradingPartner.getCustomerAddressSet();
        if (null != list) {
            for (CustomerAddress customerAddress : list) {
                if (null != selectedContactId && selectedContactId.equals(customerAddress.getId())) {
                    tradingPartnerForm.setCoName(customerAddress.getCoName());
                    tradingPartnerForm.setAddress1(customerAddress.getAddress1());
                    tradingPartnerForm.setCity1(customerAddress.getCity2());
                    tradingPartnerForm.setState(customerAddress.getState());
                    tradingPartnerForm.setZip(customerAddress.getZip());
                    tradingPartnerForm.setCustAddressId(customerAddress.getId().toString());
                    return tradingPartnerForm;
                }

            }
        }
        return tradingPartnerForm;
    }

    public TradingPartnerForm setMasterAddressDetails(TradingPartnerForm tradingPartnerForm, Integer selectedContactId) throws Exception {
        List addressList = tradingPartnerDAO.findMasterAddressess();
        for (Iterator iter = addressList.iterator(); iter.hasNext();) {
            CustomerAddress customerAddress = (CustomerAddress) iter.next();
            if (selectedContactId != null && selectedContactId.equals((customerAddress.getId()))) {
                tradingPartnerForm.setPayCompany(customerAddress.getCoName());
                tradingPartnerForm.setPayAddress1(customerAddress.getAddress1());
                if (null != customerAddress.getCity1()) {
                    tradingPartnerForm.setPaycity2(customerAddress.getCity1().getUnLocationName());
                }
                tradingPartnerForm.setPayState(customerAddress.getState());
                tradingPartnerForm.setPayZip(customerAddress.getZip());
                return tradingPartnerForm;
            }
        }
        return tradingPartnerForm;
    }

    public TradingPartnerForm setPaymentAddressDetails(TradingPartnerForm tradingPartnerForm, String tradingPartnerId, Integer selectedContactId) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerId);
        Set<CustomerAddress> list = tradingPartner.getCustomerAddressSet();
        if (null != list) {
            for (CustomerAddress customerAddress : list) {
                if (null != selectedContactId && selectedContactId.equals(customerAddress.getId())) {
                    tradingPartnerForm.setPayCompany(customerAddress.getCoName());
                    tradingPartnerForm.setPayAddress1(customerAddress.getAddress1());
                    if (null != customerAddress.getCity1()) {
                        tradingPartnerForm.setPaycity2(customerAddress.getCity1().getUnLocationName());
                    }
                    tradingPartnerForm.setPayState(customerAddress.getState());
                    tradingPartnerForm.setPayZip(customerAddress.getPhone());
                    return tradingPartnerForm;
                }
            }
        }
        return tradingPartnerForm;
    }

    public void saveCustomerAccounting(TradingPartnerForm tradingPartnerForm, User loginUser, HttpServletRequest request) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        boolean noArConfig = true;
        if (CommonUtils.isNotEmpty(tradingPartner.getAccounting())) {
            for (CustomerAccounting customerAccounting : tradingPartner.getAccounting()) {
                noArConfig = false;
                customerAccounting.setHoldList(tradingPartnerForm.getHoldList());
                customerAccounting.setComment(tradingPartnerForm.getAcctReceive());
                customerAccounting.setAttention(tradingPartnerForm.getAttention());
                customerAccounting.setAddress1(tradingPartnerForm.getAddress1());
                customerAccounting.setAddress2(tradingPartnerForm.getAddress2());
                if (CommonUtils.isNotEmpty(tradingPartnerForm.getCountry())) {
                    customerAccounting.setCountry(Integer.parseInt(tradingPartnerForm.getCountry()));
                }
                customerAccounting.setCompanyMaster(tradingPartnerForm.getCompanyMaster());
                customerAccounting.setAddressMaster(tradingPartnerForm.getAddressMaster());
                customerAccounting.setHoldComment(tradingPartnerForm.getHoldComment());
                customerAccounting.setSuspendCredit(tradingPartnerForm.getSuspendCredit());
                customerAccounting.setLegal(tradingPartnerForm.getLegal());
                customerAccounting.setIncludeagent(CommonUtils.isEqual(tradingPartnerForm.getIncludeagent(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
                customerAccounting.setExtendCredit(CommonUtils.isEqual(tradingPartnerForm.getExtendCredit(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
                customerAccounting.setOverLimit(tradingPartnerForm.getOverLimitCheck() == null ? CommonConstants.OFF : tradingPartnerForm.getOverLimitCheck());
                customerAccounting.setPastDue(tradingPartnerForm.getPastDueCheck() == null ? CommonConstants.OFF : tradingPartnerForm.getPastDueCheck());

                //setting Contact Details
                customerAccounting.setContact(tradingPartnerForm.getContact());
                customerAccounting.setArPhone(tradingPartnerForm.getArPhone());
                customerAccounting.setArFax(tradingPartnerForm.getArFax());
                customerAccounting.setAcctRecEmail(tradingPartnerForm.getAcctRecEmail());

                //setting Invoice Address
                CustomerAddress customerAddress = null;
                if (CommonUtils.isNotEmpty(tradingPartnerForm.getCustAddressId())) {
                    Integer id = Integer.parseInt(tradingPartnerForm.getCustAddressId());
                    customerAddress = new TradingPartnerDAO().findAddressById(id);
                }
                if (null == customerAddress) {
                    customerAddress = new TradingPartnerDAO().getDefaultAddress(tradingPartner.getAccountno());
                }
                if (null != customerAddress) {
                    customerAccounting.setCustAddressId(customerAddress.getId());
                    tradingPartnerForm.setCustAddressId(customerAddress.getId().toString());
                    if (CommonUtils.isNotEmpty(customerAddress.getCoName())) {
                        customerAccounting.setCompany(customerAddress.getCoName());
                        tradingPartnerForm.setCoName(customerAddress.getCoName());
                    }
                    customerAccounting.setAddress1(customerAddress.getAddress1());
                    tradingPartnerForm.setAddress1(customerAddress.getAddress1());
                    customerAccounting.setCity1(customerAddress.getCity1());
                    customerAccounting.setCity2(customerAddress.getCity2());
                    tradingPartnerForm.setCity1(customerAddress.getCity2());
                    customerAccounting.setState(customerAddress.getState());
                    tradingPartnerForm.setState(customerAddress.getState());
                    customerAccounting.setZip(customerAddress.getZip());
                    tradingPartnerForm.setZip(customerAddress.getZip());
                } else {
                    customerAccounting.setCustAddressId(null);
                    tradingPartnerForm.setCustAddressId(null);
                    customerAccounting.setCompany(null);
                    tradingPartnerForm.setCoName(null);
                    customerAccounting.setAddress1(null);
                    tradingPartnerForm.setAddress1(null);
                    customerAccounting.setCity1(null);
                    customerAccounting.setCity2(null);
                    tradingPartnerForm.setCity1(null);
                    customerAccounting.setState(null);
                    tradingPartnerForm.setState(null);
                    customerAccounting.setZip(null);
                    tradingPartnerForm.setZip(null);
                }

                //setting Payment Address
                customerAccounting.setPayCompany(tradingPartnerForm.getPayCompany());
                customerAccounting.setPayAddress1(tradingPartnerForm.getPayAddress1());
                customerAccounting.setPayAddress2(tradingPartnerForm.getPayAddress2());
                customerAccounting.setPayCountry(tradingPartnerForm.getPayCountry());
                customerAccounting.setPaycity2(tradingPartnerForm.getPaycity2());
                customerAccounting.setPayState(tradingPartnerForm.getPayState());
                customerAccounting.setPayZip(tradingPartnerForm.getPayZip());
                customerAccounting.setLateFee(tradingPartnerForm.getLateFee());
                customerAccounting.setExemptCreditProcess(CommonUtils.isEqual(tradingPartnerForm.getExemptCreditProcess(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
                customerAccounting.setHhgPeAutosCredit(CommonUtils.isEqual(tradingPartnerForm.getHhgPeAutosCredit(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
                customerAccounting.setImportCredit(CommonUtils.isEqual(tradingPartnerForm.getImportCredit(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);

                //FCL/LCL Apply Late Fee fields
                customerAccounting.setLclApplyLateFee(tradingPartnerForm.getLclApplyLateFee());
                customerAccounting.setFclApplyLateFee(tradingPartnerForm.getFclApplyLateFee());
                //Past Due Buffer
                customerAccounting.setPastDueBuffer(tradingPartnerForm.getPastDueBuffer());
                String statementFromDomain = null;
                String statementFromForm = null;
                if (null != customerAccounting.getStatements()) {
                    statementFromDomain = customerAccounting.getStatements().getCodedesc();
                }
                if (CommonUtils.isNotEqual(tradingPartnerForm.getStatements(), "0")) {
                    GenericCode statements = genericCodeDAO.findById(Integer.parseInt(tradingPartnerForm.getStatements()));
                    customerAccounting.setStatements(statements);
                    statementFromForm = statements.getCodedesc();
                }
                customerAccounting.setStatementType(tradingPartnerForm.getStatementType());
                AuditNotesUtils.insertAuditNotes(statementFromDomain, statementFromForm,
                        NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Statements", loginUser);

                AuditNotesUtils.insertAuditNotes(customerAccounting.getSchedulestmt(), tradingPartnerForm.getSchedulestmt(),
                        NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Schedule Statements", loginUser);
                AuditNotesUtils.insertAuditNotes(statementFromDomain, statementFromForm,
                        NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Statements", loginUser);

                AuditNotesUtils.insertAuditNotes(customerAccounting.getSchedulestmt(), tradingPartnerForm.getSchedulestmt(),
                        NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Schedule Statements", loginUser);
                customerAccounting.setSchedulestmt(tradingPartnerForm.getSchedulestmt());

                String creditBalanceFromForm = CommonUtils.isEqual(tradingPartnerForm.getCreditbalance(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO;
                if (CommonUtils.isNotEqual(customerAccounting.getCreditbalance(), creditBalanceFromForm)) {
                    if (CommonUtils.isEqualIgnoreCase(creditBalanceFromForm, CommonConstants.YES)) {
                        StringBuilder desc = new StringBuilder("Send Statements with Credit Balance check marked by ").append(loginUser.getLoginName());
                        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Send Statements with Credit Balance", loginUser);
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Send Statements with Credit Balance", loginUser);
                    } else if (CommonUtils.isEqualIgnoreCase(customerAccounting.getCreditbalance(), CommonConstants.YES)) {
                        StringBuilder desc = new StringBuilder("Send Statements with Credit Balance check unmarked by ").append(loginUser.getLoginName());
                        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Send Statements with Credit Balance", loginUser);
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Send Statements with Credit Balance", loginUser);
                    }
                    customerAccounting.setCreditbalance(creditBalanceFromForm);
                }
                String creditInvoiceFromForm = CommonUtils.isEqual(tradingPartnerForm.getCreditinvoice(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO;
                if (CommonUtils.isNotEqual(customerAccounting.getCreditinvoice(), creditInvoiceFromForm)) {
                    if (CommonUtils.isEqualIgnoreCase(creditInvoiceFromForm, CommonConstants.YES)) {
                        StringBuilder desc = new StringBuilder("Send Statements with Credit Invoice marked by ").append(loginUser.getLoginName());
                        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Send Statements with Credit Invoice", loginUser);
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Send Statements with Credit Invoice", loginUser);
                    } else if (CommonUtils.isEqualIgnoreCase(customerAccounting.getCreditinvoice(), CommonConstants.YES)) {
                        StringBuilder desc = new StringBuilder("Send Statements with Credit Invoice unmarked by ").append(loginUser.getLoginName());
                        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Send Statements with Credit Invoice", loginUser);
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Send Statements with Credit Invoice", loginUser);
                    }
                    customerAccounting.setCreditinvoice(creditInvoiceFromForm);
                }
                String sendDebitCreditNotes = CommonUtils.isEqual(tradingPartnerForm.getSendDebitCreditNotes(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO;
                customerAccounting.setSendDebitCreditNotes(sendDebitCreditNotes);
                String creditTermFromDomain = null;
                String creditTermFromForm = null;
                if (null != customerAccounting.getCreditRate()) {
                    creditTermFromDomain = customerAccounting.getCreditRate().getCodedesc();
                }
                if (null != tradingPartnerForm.getCreditRate() && !tradingPartnerForm.getCreditRate().equals("0")) {
                    GenericCode creditRate = genericCodeDAO.findById(Integer.parseInt(tradingPartnerForm.getCreditRate()));
                    customerAccounting.setCreditRate(creditRate);
                    creditTermFromForm = creditRate.getCodedesc();
                }
                AuditNotesUtils.insertAuditNotes(creditTermFromDomain, creditTermFromForm,
                        NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Credit Term", loginUser);
                AuditNotesUtils.insertAuditNotes(creditTermFromDomain, creditTermFromForm,
                        NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Credit Term", loginUser);

                String creditStatusFromDomain = null;
                String creditStatusFromForm = null;
                if (null != customerAccounting.getCreditStatus()) {
                    creditStatusFromDomain = customerAccounting.getCreditStatus().getCodedesc();
                }
                if (null != tradingPartnerForm.getCreditStatus()) {
                    GenericCode creditStatus = genericCodeDAO.findById(Integer.parseInt(tradingPartnerForm.getCreditStatus()));
                    customerAccounting.setCreditStatus(creditStatus);
                    customerAccounting.setCreditStatusUpdateBy(loginUser.getLoginName());
                    creditStatusFromForm = creditStatus.getCodedesc();
                }
                if (CommonUtils.isEqualIgnoreCase(creditStatusFromDomain, TradingPartnerConstants.CREDITHOLD)
                        && CommonUtils.isEqualIgnoreCase(creditStatusFromForm, TradingPartnerConstants.IN_GOOD_STANDING)) {
                    List<Transaction> arTransactions = new AccountingTransactionDAO().getArTransactionsWithCreditHold(customerAccounting.getAccountNo());
                    for (Transaction transaction : arTransactions) {
                        String invoiceOrBl = "";
                        if (CommonUtils.isNotEmpty(transaction.getBillLaddingNo())) {
                            invoiceOrBl = transaction.getBillLaddingNo();
                        } else {
                            invoiceOrBl = transaction.getInvoiceNumber();
                        }
                        transaction.setEmailed(false);
                        //ArCreditHoldUtils.sendEmail(transaction, loginUser, false, request);
                        transaction.setCreditHold(CommonConstants.NO);
                        StringBuilder desc = new StringBuilder("AR Invoice/BL '").append(invoiceOrBl).append("' of '");
                        desc.append(transaction.getCustName()).append("(").append(transaction.getCustNo()).append(")'");
                        desc.append("' taken off credit hold by ").append(loginUser.getLoginName()).append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AR_INVOICE, transaction.getCustNo() + "-" + invoiceOrBl,
                                "AR Credit Hold", loginUser);
                    }
                }
                AuditNotesUtils.insertAuditNotes(creditStatusFromDomain, creditStatusFromForm,
                        NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Credit Status", loginUser);
                AuditNotesUtils.insertAuditNotes(creditStatusFromDomain, creditStatusFromForm,
                        NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Credit Status", loginUser);
                String creditLimitFromDomain = null;
                String creditLimitFromForm = null;
                if (CommonUtils.isNotEmpty(customerAccounting.getCreditLimit())) {
                    creditLimitFromDomain = NumberUtils.formatNumber(customerAccounting.getCreditLimit(), "###,###,##0.00");
                } else {
                    creditLimitFromDomain = NumberUtils.formatNumber(0d, "###,###,##0.00");
                }
                if (CommonUtils.isNotEmpty(tradingPartnerForm.getCreditLimit())) {
                    Double creditLimit = Double.parseDouble(tradingPartnerForm.getCreditLimit().replaceAll(",", ""));
                    creditLimitFromForm = NumberUtils.formatNumber(creditLimit, "###,###,##0.00");
                    customerAccounting.setCreditLimit(creditLimit);
                } else {
                    customerAccounting.setCreditLimit(null);
                }
                AuditNotesUtils.insertAuditNotes(creditLimitFromDomain, creditLimitFromForm,
                        NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Credit Limit", loginUser);
                AuditNotesUtils.insertAuditNotes(creditLimitFromDomain, creditLimitFromForm,
                        NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Credit Limit", loginUser);
                String collectorFromDomain = null;
                String collectorFromForm = null;
                if (null != customerAccounting.getArcode()) {
                    collectorFromDomain = customerAccounting.getArcode().getLoginName();
                }
                if (CommonUtils.isNotEmpty(tradingPartnerForm.getArCode())) {
                    User collector = userDAO.findById(new Integer(tradingPartnerForm.getArCode()));
                    customerAccounting.setArcode(collector);
                    customerAccounting.setCollectorUpdatedBy(loginUser.getLoginName());
                    collectorFromForm = collector.getLoginName();
                } else {
                    customerAccounting.setArcode(null);
                    customerAccounting.setCollectorUpdatedBy(loginUser.getLoginName());
                }
                AuditNotesUtils.insertAuditNotes(collectorFromDomain, collectorFromForm,
                        NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "Collector", loginUser);
                AuditNotesUtils.insertAuditNotes(collectorFromDomain, collectorFromForm,
                        NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "Collector", loginUser);

                String importCreditValue= null;
                String impvalueForm = null;
                String impvaluefrmDatabase = null;
                if(null != customerAccounting.getImportCredit()){
                   importCreditValue =customerAccounting.getImportCredit();
                }
             impvalueForm = CommonUtils.isEqual(tradingPartnerForm.getImportCredit(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO;
             impvaluefrmDatabase=tradingPartnerDAO.getImportcreditValueForNotes(tradingPartnerForm.getTradingPartnerId());

                 if (CommonUtils.isNotEqual(impvaluefrmDatabase, impvalueForm)){
                AuditNotesUtils.insertAuditNotes(impvaluefrmDatabase,importCreditValue,
                        NotesConstants.ARCONFIGURATION, tradingPartnerForm.getTradingPartnerId(), "ImportCredit", loginUser);
                AuditNotesUtils.insertAuditNotes(impvaluefrmDatabase, importCreditValue,
                        NotesConstants.TRADINGPARTNER, tradingPartnerForm.getTradingPartnerId(), "ImportCredit", loginUser);
                }
                customerAccounting.setUpdatedBy(loginUser.getUserId());
                customerAccounting.setUpdatedOn(new Date());

            }
        }
        if (noArConfig) {
            CustomerAccounting customerAccounting = new CustomerAccounting(tradingPartnerForm);
            if (null != tradingPartnerForm.getStatements() && !tradingPartnerForm.getStatements().equals("0") && !tradingPartnerForm.getStatements().equals("")) {
                customerAccounting.setStatements(genericCodeDAO.findById(new Integer(tradingPartnerForm.getStatements())));
            }
            if (null != tradingPartnerForm.getCreditStatus() && !tradingPartnerForm.getCreditStatus().equals("0") && !tradingPartnerForm.getCreditStatus().equals("")) {
                customerAccounting.setCreditStatus(genericCodeDAO.findById(new Integer(tradingPartnerForm.getCreditStatus())));
                customerAccounting.setCreditStatusUpdateBy(loginUser.getLoginName());
            }
            if (null != tradingPartnerForm.getArCode() && !tradingPartnerForm.getArCode().equals("0") && !tradingPartnerForm.getArCode().equals("")) {
                customerAccounting.setArcode(userDAO.findById(new Integer(tradingPartnerForm.getArCode())));
                customerAccounting.setCollectorUpdatedBy(loginUser.getLoginName());
            }
            if (null != tradingPartnerForm.getCreditRate() && !tradingPartnerForm.getCreditRate().equals("0") && !tradingPartnerForm.getCreditRate().equals("")) {
                customerAccounting.setCreditRate(genericCodeDAO.findById(new Integer(tradingPartnerForm.getCreditRate())));
            }
            customerAccounting.setStatementType(tradingPartnerForm.getStatementType());
            customerAccounting.setCreditbalance(CommonUtils.isEqual(tradingPartnerForm.getCreditbalance(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
            customerAccounting.setCreditinvoice(CommonUtils.isEqual(tradingPartnerForm.getCreditinvoice(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
            customerAccounting.setIncludeagent(CommonUtils.isEqual(tradingPartnerForm.getIncludeagent(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
            customerAccounting.setExtendCredit(CommonUtils.isEqual(tradingPartnerForm.getExtendCredit(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
            customerAccounting.setExemptCreditProcess(CommonUtils.isEqual(tradingPartnerForm.getExemptCreditProcess(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO);
            customerAccounting.setCreatedBy(loginUser.getUserId());
            customerAccounting.setCreatedOn(new Date());
            tradingPartner.getAccounting().add(customerAccounting);
        }

    }
}
