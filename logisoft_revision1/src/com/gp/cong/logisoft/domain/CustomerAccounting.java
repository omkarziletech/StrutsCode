package com.gp.cong.logisoft.domain;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class CustomerAccounting implements Auditable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String fclApplyLateFee;
    private String lclApplyLateFee;
    private Integer id;
    private String accountNo;
    private String contact;
    private String arPhone;
    private String arFax;
    private String acctRecEmail;
    private GenericCode statements;
    private String statementType;
    private Double creditLimit;
    private String holdList;
    private String extendCredit;
    private String comment;
    private String company;
    private String attention;
    private String address1;
    private String address2;
    private Integer country;
    private GenericCode creditRate;
    private GenericCode creditStatus;
    private String state;
    private String zip;
    private String payCompany;
    private String payAttention;
    private String payAddress1;
    private String payAddress2;
    private Integer payCountry;
    private String payState;
    private GenericCode cuntry;
    private GenericCode payCuntry;
    private UnLocation city1;
    private UnLocation payCity1;
    private String payZip;
    private String city2;
    private String paycity2;
    private User arcode;
    private String companyMaster;
    private String addressMaster;
    private String holdComment;
    private String suspendCredit;
    private String legal;
    private String includeagent;
    private String creditbalance;
    private String creditinvoice;
    private String schedulestmt;
    private String lateFee;
    private String overLimit;
    private String pastDue;
    private String exemptCreditProcess;
    private String hhgPeAutosCredit;
    private String importCredit;
    private Date createdOn;
    private Integer createdBy;
    private Date updatedOn;
    private Integer updatedBy;
    private String creditStatusUpdateBy;
    private String collectorUpdatedBy;
    private Integer custAddressId;
    private String sendDebitCreditNotes;
    private Integer pastDueBuffer;

    public CustomerAccounting() {
    }

    public CustomerAccounting(TradingPartnerForm tradingPartnerForm) throws Exception {
        // private GenericCode statements;
        if (null != tradingPartnerForm.getCreditLimit() && !tradingPartnerForm.getCreditLimit().trim().equals("")) {
            creditLimit = Double.parseDouble(tradingPartnerForm.getCreditLimit().replaceAll(",", ""));
        }
        holdList = tradingPartnerForm.getHoldList();
        extendCredit = tradingPartnerForm.getExtendCredit();
        comment = tradingPartnerForm.getAcctReceive();
        attention = tradingPartnerForm.getAttention();
        address1 = tradingPartnerForm.getAddress1();
        address2 = tradingPartnerForm.getAddress2();
        if (tradingPartnerForm.getCountry() != null) {
            country = Integer.parseInt(tradingPartnerForm.getCountry());
        }

        companyMaster = tradingPartnerForm.getCompanyMaster();
        addressMaster = tradingPartnerForm.getAddressMaster();
        holdComment = tradingPartnerForm.getHoldComment();
        suspendCredit = tradingPartnerForm.getSuspendCredit();
        legal = tradingPartnerForm.getLegal();
        includeagent = tradingPartnerForm.getIncludeagent();
        creditbalance = tradingPartnerForm.getCreditbalance();
        creditinvoice = tradingPartnerForm.getCreditinvoice();
        schedulestmt = tradingPartnerForm.getSchedulestmt();
        overLimit = tradingPartnerForm.getOverLimitCheck() == null ? "off" : tradingPartnerForm.getOverLimitCheck();
        pastDue = tradingPartnerForm.getPastDueCheck() == null ? CommonConstants.OFF : tradingPartnerForm.getPastDueCheck();

        //setting Contact Details
        contact = tradingPartnerForm.getContact();
        arPhone = tradingPartnerForm.getArPhone();
        arFax = tradingPartnerForm.getArFax();
        acctRecEmail = tradingPartnerForm.getAcctRecEmail();

        //setting Invoice Address
        if (null != tradingPartnerForm.getCustAddressId() && !tradingPartnerForm.getCustAddressId().isEmpty()) {
            CustomerAddress customerAddress = new TradingPartnerDAO().findAddressById(Integer.parseInt(tradingPartnerForm.getCustAddressId()));
            custAddressId = customerAddress.getId();
            company = customerAddress.getCoName();
            address1 = customerAddress.getAddress1();
            city2 = customerAddress.getCity1().getUnLocationName();
            state = customerAddress.getState();
            zip = customerAddress.getZip();
        }

        //setting Payment Address
        payCompany = tradingPartnerForm.getPayCompany();
        payAddress1 = tradingPartnerForm.getPayAddress1();
        payAddress2 = tradingPartnerForm.getPayAddress2();
        payCountry = tradingPartnerForm.getPayCountry();
        payState = tradingPartnerForm.getPayState();
        paycity2 = tradingPartnerForm.getPaycity2();
        payZip = tradingPartnerForm.getPayZip();
        lateFee = tradingPartnerForm.getLateFee();
        //new fields
        lclApplyLateFee = tradingPartnerForm.getLclApplyLateFee();
        fclApplyLateFee = tradingPartnerForm.getFclApplyLateFee();
        exemptCreditProcess = CommonUtils.isEqual(tradingPartnerForm.getExemptCreditProcess(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO;
        hhgPeAutosCredit = CommonUtils.isEqual(tradingPartnerForm.getHhgPeAutosCredit(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO;
        importCredit = CommonUtils.isEqual(tradingPartnerForm.getImportCredit(), CommonConstants.ON) ? CommonConstants.YES : CommonConstants.NO;
    }

    /**
     * @param customerAccounting
     * @return
     */
    public TradingPartnerForm loadCustomerAccountingDetails(CustomerAccounting customerAccounting, TradingPartnerForm tradingPartnerForm) throws Exception {
        //set all select box values
        if (null != customerAccounting.getStatements()) {
            tradingPartnerForm.setStatements("" + customerAccounting.getStatements().getId());
        }
        tradingPartnerForm.setStatementType(customerAccounting.getStatementType());
        tradingPartnerForm.setSchedulestmt(customerAccounting.getSchedulestmt());
        if (null != customerAccounting.getCreditLimit()) {
            NumberFormat numberFormat = new DecimalFormat("##,###,##0.00");
            tradingPartnerForm.setCreditLimit(numberFormat.format(customerAccounting.getCreditLimit()));
        } else {
            tradingPartnerForm.setCreditLimit("0.00");
        }
        if (null != customerAccounting.getCreditStatus()) {
            tradingPartnerForm.setCreditStatus("" + customerAccounting.getCreditStatus().getId());
        } else {
            tradingPartnerForm.setCreditStatus("11844");
        }
        if (null != customerAccounting.getCreditRate()) {
            tradingPartnerForm.setCreditRate("" + customerAccounting.getCreditRate().getId());
        } else {
            tradingPartnerForm.setCreditRate("11344");
        }
        tradingPartnerForm.setAcctReceive(customerAccounting.getComment());
        if (null != customerAccounting.getArcode()) {
            tradingPartnerForm.setArCode("" + customerAccounting.getArcode().getId());
            tradingPartnerForm.setArCodeName(customerAccounting.getArcode().getLoginName());
        }
        tradingPartnerForm.setIndex(customerAccounting.getId().toString());//for notes modulereference id
        //set all check box values
        tradingPartnerForm.setCreditbalance(customerAccounting.getCreditbalance());
        if (customerAccounting.getExtendCredit() != null && customerAccounting.getExtendCredit().equals(CommonConstants.YES)) {
            tradingPartnerForm.setExtendCredit(CommonConstants.ON);
        } else {
            tradingPartnerForm.setExtendCredit(CommonConstants.OFF);
        }
        if (customerAccounting.getSendDebitCreditNotes().equalsIgnoreCase(CommonConstants.YES)) {
            tradingPartnerForm.setSendDebitCreditNotes(CommonConstants.ON);
        } else {
            tradingPartnerForm.setSendDebitCreditNotes(CommonConstants.OFF);
        }
        tradingPartnerForm.setHoldComment(customerAccounting.getHoldComment());
        tradingPartnerForm.setCreditinvoice(customerAccounting.getCreditinvoice());
        tradingPartnerForm.setIncludeagent(customerAccounting.getIncludeagent());
        tradingPartnerForm.setLateFee(customerAccounting.getLateFee());
        tradingPartnerForm.setOverLimitCheck(customerAccounting.getOverLimit());
        tradingPartnerForm.setPastDueCheck(customerAccounting.getPastDue());
        tradingPartnerForm.setPastDueBuffer(customerAccounting.getPastDueBuffer());
        //set Contact Details
        tradingPartnerForm.setContact(customerAccounting.getContact());
        tradingPartnerForm.setArPhone(customerAccounting.getArPhone());
        tradingPartnerForm.setArFax(customerAccounting.getArFax());
        tradingPartnerForm.setAcctRecEmail(customerAccounting.getAcctRecEmail());
        //set invoice address details
        if (null != customerAccounting.getCustAddressId()) {
            Integer id = customerAccounting.getCustAddressId();
            CustomerAddress customerAddress = new TradingPartnerDAO().findAddressById(id);
            if (null != customerAddress) {
                tradingPartnerForm.setCustAddressId(id.toString());
                tradingPartnerForm.setCoName(customerAddress.getCoName());
                tradingPartnerForm.setAddress1(customerAddress.getAddress1());
                tradingPartnerForm.setCity1(customerAddress.getCity2());
                tradingPartnerForm.setState(customerAddress.getState());
                tradingPartnerForm.setZip(customerAddress.getZip());
            }
        }
        //set payment address details
        tradingPartnerForm.setPayCompany(customerAccounting.getPayCompany());
        tradingPartnerForm.setPayAddress1(customerAccounting.getPayAddress1());
        tradingPartnerForm.setPaycity2(customerAccounting.getPaycity2());
        tradingPartnerForm.setPayState(customerAccounting.getPayState());
        tradingPartnerForm.setPayZip(customerAccounting.getPayZip());
        tradingPartnerForm.setFclApplyLateFee(customerAccounting.getFclApplyLateFee());
        tradingPartnerForm.setLclApplyLateFee(customerAccounting.getLclApplyLateFee());
        tradingPartnerForm.setExemptCreditProcess(CommonUtils.isEqual(customerAccounting.getExemptCreditProcess(), CommonConstants.YES) ? CommonConstants.ON : CommonConstants.OFF);
        tradingPartnerForm.setHhgPeAutosCredit(CommonUtils.isEqual(customerAccounting.getHhgPeAutosCredit(), CommonConstants.YES) ? CommonConstants.ON : CommonConstants.OFF);
        tradingPartnerForm.setImportCredit(CommonUtils.isEqual(customerAccounting.getImportCredit(), CommonConstants.YES) ? CommonConstants.ON : CommonConstants.OFF);
        return tradingPartnerForm;
    }

    public String getSchedulestmt() {
        return schedulestmt;
    }

    public void setSchedulestmt(String schedulestmt) {
        this.schedulestmt = schedulestmt;
    }

    public String getCreditbalance() {
        return creditbalance;
    }

    public void setCreditbalance(String creditbalance) {
        this.creditbalance = creditbalance;
    }

    public String getCreditinvoice() {
        return creditinvoice;
    }

    public void setCreditinvoice(String creditinvoice) {
        this.creditinvoice = creditinvoice;
    }

    public String getIncludeagent() {
        return includeagent;
    }

    public void setIncludeagent(String includeagent) {
        this.includeagent = includeagent;
    }

    public String getLegal() {
        return legal;
    }

    public void setLegal(String legal) {
        this.legal = legal;
    }

    public String getSuspendCredit() {
        return suspendCredit;
    }

    public void setSuspendCredit(String suspendCredit) {
        this.suspendCredit = suspendCredit;
    }

    public String getHoldComment() {
        return holdComment;
    }

    public void setHoldComment(String holdComment) {
        this.holdComment = holdComment;
    }

    public String getAddressMaster() {
        return addressMaster;
    }

    public void setAddressMaster(String addressMaster) {
        this.addressMaster = addressMaster;
    }

    public String getCompanyMaster() {
        return companyMaster;
    }

    public void setCompanyMaster(String companyMaster) {
        this.companyMaster = companyMaster;
    }

    public String getPayZip() {
        return payZip;
    }

    public void setPayZip(String payZip) {
        this.payZip = payZip;
    }

    public UnLocation getCity1() {
        return city1;
    }

    public void setCity1(UnLocation city1) {
        this.city1 = city1;
    }

    public GenericCode getCuntry() {
        return cuntry;
    }

    public void setCuntry(GenericCode cuntry) {
        this.cuntry = cuntry;
    }

    public UnLocation getPayCity1() {
        return payCity1;
    }

    public void setPayCity1(UnLocation payCity1) {
        this.payCity1 = payCity1;
    }

    public GenericCode getPayCuntry() {
        return payCuntry;
    }

    public void setPayCuntry(GenericCode payCuntry) {
        this.payCuntry = payCuntry;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAcctRecEmail() {
        return acctRecEmail;
    }

    public void setAcctRecEmail(String acctRecEmail) {
        this.acctRecEmail = acctRecEmail;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getArFax() {
        return arFax;
    }

    public void setArFax(String arFax) {
        this.arFax = arFax;
    }

    public String getArPhone() {
        return arPhone;
    }

    public void setArPhone(String arPhone) {
        this.arPhone = arPhone;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public String getExtendCredit() {
        return extendCredit;
    }

    public void setExtendCredit(String extendCredit) {
        this.extendCredit = extendCredit;
    }

    public String getHoldList() {
        return holdList;
    }

    public void setHoldList(String holdList) {
        this.holdList = holdList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPayAddress1() {
        return payAddress1;
    }

    public void setPayAddress1(String payAddress1) {
        this.payAddress1 = payAddress1;
    }

    public String getPayAddress2() {
        return payAddress2;
    }

    public void setPayAddress2(String payAddress2) {
        this.payAddress2 = payAddress2;
    }

    public String getPayAttention() {
        return payAttention;
    }

    public void setPayAttention(String payAttention) {
        this.payAttention = payAttention;
    }

    public String getPayCompany() {
        return payCompany;
    }

    public void setPayCompany(String payCompany) {
        this.payCompany = payCompany;
    }

    public Integer getPayCountry() {
        return payCountry;
    }

    public void setPayCountry(Integer payCountry) {
        this.payCountry = payCountry;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public GenericCode getStatements() {
        return statements;
    }

    public void setStatements(GenericCode statements) {
        this.statements = statements;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId1() {
        // TODO Auto-generated method stub
        return this.getId();
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public String getPaycity2() {
        return paycity2;
    }

    public void setPaycity2(String paycity2) {
        this.paycity2 = paycity2;
    }

    public GenericCode getCreditRate() {
        return creditRate;
    }

    public void setCreditRate(GenericCode creditRate) {
        this.creditRate = creditRate;
    }

    public GenericCode getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(GenericCode creditStatus) {
        this.creditStatus = creditStatus;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public User getArcode() {
        return arcode;
    }

    public void setArcode(User arcode) {
        this.arcode = arcode;
    }

    public String getOverLimit() {
        return overLimit;
    }

    public void setOverLimit(String overLimit) {
        this.overLimit = overLimit;
    }

    public String getPastDue() {
        return pastDue;
    }

    public void setPastDue(String pastDue) {
        this.pastDue = pastDue;
    }

    public String getFclApplyLateFee() {
        return fclApplyLateFee;
    }

    public void setFclApplyLateFee(String fclApplyLateFee) {
        this.fclApplyLateFee = fclApplyLateFee;
    }

    public String getLclApplyLateFee() {
        return lclApplyLateFee;
    }

    public void setLclApplyLateFee(String lclApplyLateFee) {
        this.lclApplyLateFee = lclApplyLateFee;
    }

    public String getExemptCreditProcess() {
        return exemptCreditProcess;
    }

    public void setExemptCreditProcess(String exemptCreditProcess) {
        this.exemptCreditProcess = exemptCreditProcess;
    }

    public String getHhgPeAutosCredit() {
        return hhgPeAutosCredit;
    }

    public void setHhgPeAutosCredit(String hhgPeAutosCredit) {
        this.hhgPeAutosCredit = hhgPeAutosCredit;
    }

    public String getImportCredit() {
        return importCredit;
    }

    public void setImportCredit(String importCredit) {
        this.importCredit = importCredit;
    }
    
    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreditStatusUpdateBy() {
        return creditStatusUpdateBy;
    }

    public void setCreditStatusUpdateBy(String creditStatusUpdateBy) {
        this.creditStatusUpdateBy = creditStatusUpdateBy;
    }

    public String getCollectorUpdatedBy() {
        return collectorUpdatedBy;
    }

    public void setCollectorUpdatedBy(String collectorUpdatedBy) {
        this.collectorUpdatedBy = collectorUpdatedBy;
    }

    public Integer getCustAddressId() {
        return custAddressId;
    }

    public void setCustAddressId(Integer custAddressId) {
        this.custAddressId = custAddressId;
    }

    public String getSendDebitCreditNotes() {
        return sendDebitCreditNotes;
    }

    public void setSendDebitCreditNotes(String sendDebitCreditNotes) {
        this.sendDebitCreditNotes = sendDebitCreditNotes;
    }

    public Integer getPastDueBuffer() {
        return pastDueBuffer;
    }

    public void setPastDueBuffer(Integer pastDueBuffer) {
        this.pastDueBuffer = pastDueBuffer;
    }
}
