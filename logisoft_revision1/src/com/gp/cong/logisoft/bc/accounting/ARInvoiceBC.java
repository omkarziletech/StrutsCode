package com.gp.cong.logisoft.bc.accounting;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;

import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.reports.ArInvoicePdfCreator;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.domain.ArInvoice;
import com.gp.cvst.logisoft.domain.ArinvoiceCharges;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.ArInvoiceChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.ArInvoiceDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.gp.cvst.logisoft.struts.form.ARInvoiceForm;
import org.apache.commons.beanutils.PropertyUtils;

public class ARInvoiceBC {

    ArInvoiceDAO arInvoiceDAO = new ArInvoiceDAO();
    EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
    GlMappingDAO glmappDAO = new GlMappingDAO();
    TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
    TransactionDAO transactionDAO = new TransactionDAO();
    ArInvoiceChargesDAO arInvoiceChargesDAO = new ArInvoiceChargesDAO();

    /**
     * @param aRInvoiceForm
     * @return
     */
    public ArInvoice saveInvoice(ARInvoiceForm aRInvoiceForm)throws Exception {
	Set<ArinvoiceCharges> arinvoiceChargesSet = new HashSet<ArinvoiceCharges>();
	aRInvoiceForm.setInvoiceNumber(arInvoiceDAO.generateInvoiceNumber());
	ArInvoice arInvoice = new ArInvoice(aRInvoiceForm);
	ArinvoiceCharges arinvoiceCharges = new ArinvoiceCharges(aRInvoiceForm);
	arinvoiceChargesSet.add(arinvoiceCharges);
	arInvoice.setArinvoiceCharges(arinvoiceChargesSet);
	arInvoiceDAO.save(arInvoice);
	return arInvoice;
    }

    /**
     * @param aRInvoiceForm
     * @return
     */
    public ArInvoice updateInvoice(ARInvoiceForm aRInvoiceForm, String updateArInvoiceCharges)throws Exception {
	ArInvoice arInvoice = arInvoiceDAO.findById(new Integer(aRInvoiceForm.getArInvoiceId()));
	arInvoice.updateValues(aRInvoiceForm);
	if (updateArInvoiceCharges != null) {
	    ArinvoiceCharges arinvoiceCharges = new ArinvoiceCharges(aRInvoiceForm);

	    arInvoice.getArinvoiceCharges().add(arinvoiceCharges);
	}
	arInvoiceDAO.update(arInvoice);
	return arInvoice;
    }

    /**
     * @param invoiceId
     * @return
     */
    public ArInvoice getInvoice(Integer invoiceId)throws Exception {
	ArInvoice arInvoice = arInvoiceDAO.findById(invoiceId);
	return arInvoice;
    }

    /**
     * @return
     */
    public List<ArInvoice> getInvoices()throws Exception {
	List<ArInvoice> arInvoiceList = arInvoiceDAO.findAll();
	return arInvoiceList;
    }

    /**
     * @param invoiceId
     */
    public void deleteInvoice(Integer invoiceId)throws Exception {
	ArInvoice arInvoice = arInvoiceDAO.findById(invoiceId);
	arInvoiceDAO.delete(arInvoice);
    }

    /**
     * @param aRInvoiceForm
     * @return
     */
    public ArInvoice getCustomerInfo(ARInvoiceForm aRInvoiceForm)throws Exception {
	CustAddressDAO custAddressDAO = new CustAddressDAO();
	CustAddress custAddress = null;
	ArInvoice arInvoice = new ArInvoice();
	List customerDetail = custAddressDAO.getCustomeress(aRInvoiceForm.getCusName());
	if (customerDetail != null && !customerDetail.isEmpty()) {
	    custAddress = (CustAddress) customerDetail.get(0);
	    arInvoice.getCustometDetails(custAddress);
	}
	return arInvoice;
    }

    /**
     * @param invoiceId
     * @param invoiceChargesId
     */
    public void deleteInvoiceCharge(Integer invoiceId, Integer invoiceChargesId)throws Exception {
	ArInvoice arInvoice = arInvoiceDAO.findById(invoiceId);
	Hibernate.initialize(arInvoice.getArinvoiceCharges());
	Set<ArinvoiceCharges> arinvoiceChargesSet = new HashSet();
	arinvoiceChargesSet.addAll(arInvoice.getArinvoiceCharges());
	for (Iterator iter = arinvoiceChargesSet.iterator(); iter.hasNext();) {
	    ArinvoiceCharges arinvoiceCharges = (ArinvoiceCharges) iter.next();
	    if (arinvoiceCharges != null && arinvoiceCharges.getId().equals(invoiceChargesId)) {
		arInvoice.getArinvoiceCharges().remove(arinvoiceCharges);
		arInvoiceDAO.update(arInvoice);
	    }
	}
    }

    /**
     * @param invoiceNumber
     * @param inovoiceId
     * @return
     */
    public ArInvoice getInvoiceForEdit(String invoiceNumber, String invoiceId)throws Exception {
	ArInvoice arInvoice = arInvoiceDAO.findByInvoiceNumber(invoiceNumber, invoiceId);
	return arInvoice;
    }

    /**
     * @param invoiceId
     * @return
     */
    public ArInvoice getInvoice(String invoiceId)throws Exception {
	return getInvoiceForEdit(null, invoiceId);
    }

    /**
     * @param invoiceNumber
     * @return
     */
    public ArInvoice getInvoiceByNumber(String invoiceNumber)throws Exception {
	return getInvoiceForEdit(invoiceNumber, null);
    }

    /**
     * @param aRInvoiceForm
     * @return
     */
    public List<ArInvoice> getInvoices(ARInvoiceForm aRInvoiceForm)throws Exception {
	List<ArInvoice> arInvoiceList = arInvoiceDAO.findByCustomerAndAccount(aRInvoiceForm.getCusName(), aRInvoiceForm.getAccountNumber(), aRInvoiceForm.getInvoiceNumber(), aRInvoiceForm.getInvoiceAmount(), aRInvoiceForm.getFromDate(), aRInvoiceForm.getToDate());
	return arInvoiceList;
    }

    /**
     * @return
     */
    public List getCreditTermList() throws Exception{
	DBUtil dbUtil = new DBUtil();
	return dbUtil.getGenericCodeList(29, "yes", "Select Credit Terms");

    }

    public void createArInvoiceReport(String arInvoiceId, String fileName, String contextPath) throws Exception {
	ArInvoice arInvoice = arInvoiceDAO.findById(Integer.parseInt(arInvoiceId));
	ArInvoicePdfCreator arInvoicePdfCreator = new ArInvoicePdfCreator();
	SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
	List systemRulesList = systemRulesDAO.getSystemRulesRecords();
	if (systemRulesList != null && systemRulesList.size() > 8) {
	    arInvoice.setCompanyAddress((String) systemRulesList.get(0));
	    arInvoice.setCompanyCity((String) systemRulesList.get(2));
	    arInvoice.setCompanyFax((String) systemRulesList.get(7));
	    arInvoice.setCompanyPhone((String) systemRulesList.get(4));
	    arInvoice.setCompanyState((String) systemRulesList.get(3));
	    arInvoice.setCompanyName((String) systemRulesList.get(6));
	    arInvoice.setCompanyZip((String) systemRulesList.get(8));
	}
	CustAddressDAO custAddressDAO = new CustAddressDAO();
	CustomerAddress custAddress = null;
	List customerDetail = custAddressDAO.getCustomerAddress(arInvoice.getCustomerName());
	if (customerDetail != null && !customerDetail.isEmpty()) {
	    custAddress = (CustomerAddress) customerDetail.get(0);

	    if (custAddress.getCity1() != null) {
		arInvoice.setInvoiceCity(custAddress.getCity1().getUnLocationName());
	    }
	    arInvoice.setZip(custAddress.getZip());
	    arInvoice.setState(custAddress.getState());
	}
	if (arInvoice.getTerm() != null && !arInvoice.getTerm().equals("")) {
	    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
	    GenericCode genericCode = genericCodeDAO.findById(new Integer(arInvoice.getTerm()));
	    if (genericCode != null && genericCode.getCodedesc() != null) {
		arInvoice.setTermForPrint(genericCode.getCodedesc());
	    }
	}
	arInvoicePdfCreator.createReport(arInvoice, fileName, contextPath);
    }

    public void save(EmailSchedulerVO emailSchedulerVO) throws Exception{
	emailschedulerDAO.save(emailSchedulerVO);
    }

    public void manifest(ArInvoice aRInvoice)throws Exception {
	List deleteList = transactionDAO.getTransactionforInvoiceNo(aRInvoice.getInvoiceNumber());
	for (Iterator iterator = deleteList.iterator(); iterator.hasNext();) {
	    Transaction transaction = (Transaction) iterator.next();
	    transactionDAO.delete(transaction);
	}
	if (aRInvoice.getArinvoiceCharges() != null) {
	    Iterator iter = aRInvoice.getArinvoiceCharges().iterator();
	    TransactionLedger transactionLedger = null;
	    while (iter.hasNext()) {
		ArinvoiceCharges arinvoiceCharges = (ArinvoiceCharges) iter.next();
		transactionLedger = new TransactionLedger();
		transactionLedger.setChargeCode(arinvoiceCharges.getChargesCode());
		transactionLedger.setBillTo("Y");
		transactionLedger.setTransactionAmt(arinvoiceCharges.getAmount());
		transactionLedger.setBalance(arinvoiceCharges.getAmount());
		transactionLedger.setTransactionDate(new Date());
		transactionLedger.setCustName(aRInvoice.getCustomerName());
		transactionLedger.setCustNo(aRInvoice.getAccountNumber());
		if (aRInvoice.getCustomerType().equals("S")) {
		    transactionLedger.setShipName(aRInvoice.getCustomerName());
		    transactionLedger.setShipNo(aRInvoice.getAccountNumber());
		}
		if (aRInvoice.getCustomerType().equals("T")) {
		    transactionLedger.setThirdptyName(aRInvoice.getCustomerName());
		    transactionLedger.setThirdptyNo(aRInvoice.getAccountNumber());
		}
		if (aRInvoice.getCustomerType().equals("C")) {
		    transactionLedger.setConsName(aRInvoice.getCustomerName());
		    transactionLedger.setConsNo(aRInvoice.getAccountNumber());
		}
		if (aRInvoice.getCustomerType().equals("F")) {
		    transactionLedger.setFwdName(aRInvoice.getCustomerName());
		    transactionLedger.setFwdNo(aRInvoice.getAccountNumber());
		}
		if (aRInvoice.getCustomerType().equals("A")) {
		    transactionLedger.setAgentName(aRInvoice.getCustomerName());
		    transactionLedger.setAgentNo(aRInvoice.getAccountNumber());
		}
		transactionLedger.setBillLaddingNo(null);
		transactionLedger.setInvoiceNumber(aRInvoice.getInvoiceNumber());
		transactionLedger.setDueDate(aRInvoice.getDueDate());
		transactionLedger.setCustomerReferenceNo(aRInvoice.getNotes());
		String glacctNo = glmappDAO.getGLAccountNo("FCLE", arinvoiceCharges.getChargesCode(), null, null, "R");
		transactionLedger.setGlAccountNumber(glacctNo);
		String subledgercode = glmappDAO.getGLAccountNo1("FCLE", arinvoiceCharges.getChargesCode(), null, null, "R");
		transactionLedger.setSubledgerSourceCode(subledgercode);
		transactionLedger.setTransactionType("AR");
		transactionLedger.setCorrectionFlag("Y");
		transactionLedger.setStatus("Open");
		transactionLedgerDAO.save(transactionLedger);
	    }

	    List customerList = transactionLedgerDAO.getTransactionValues3(aRInvoice.getInvoiceNumber());
	    for (Iterator iterator = customerList.iterator(); iterator.hasNext();) {
		String custNo = (String) iterator.next();
		List transactionType = transactionLedgerDAO.getTransactionValues4(aRInvoice.getInvoiceNumber(), custNo);
		for (int a = 0; a < transactionType.size(); a++) {
		    String transType = "";
		    if (transactionType != null && transactionType.size() > 0) {
			transType = (String) transactionType.get(a);
		    }

		    List tras = transactionLedgerDAO.getTransactionValues1(aRInvoice.getInvoiceNumber(), custNo, transType);
		    if (tras != null && tras.size() > 0) {
			TransactionLedger trans = (TransactionLedger) tras.get(0);
			Transaction transaction = new Transaction();
                        PropertyUtils.copyProperties(transaction, trans);
			transaction.setCorrectionFlag("N");
			transactionDAO.save(transaction);
		    }
		    Double transactionAmount = transactionLedgerDAO.gettransactionamount1(aRInvoice.getInvoiceNumber(), custNo, transType);
		    List transactionList = transactionDAO.getTransactionValues2(aRInvoice.getInvoiceNumber(), custNo, transactionAmount, transType);
		    transactionList = transactionDAO.getTransactionValues3(aRInvoice.getInvoiceNumber(), custNo, transactionAmount, transType);
		    transactionList = transactionDAO.updateBalanceInProcessInARInvoice(aRInvoice.getInvoiceNumber(), custNo, transactionAmount, transType);
		}
	    }
	}
    }

    public String getCreditTermID(ARInvoiceForm arInvoiceform) throws Exception{
	GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
	GenericCode code = genericCodeDAO.findByCodeDescName(arInvoiceform.getTerm(), 29);
	if (code != null) {
	    return code.getId().toString();
	}
	return null;

    }

    public void manifestWhileSaving(ArInvoice arInvoice) throws Exception{
	List deleteList = transactionDAO.getTransactionforInvoiceNo(arInvoice.getInvoiceNumber());
	for (Iterator iterator = deleteList.iterator(); iterator.hasNext();) {
	    Transaction transaction = (Transaction) iterator.next();
	    transactionDAO.delete(transaction);
	}
	String fclreadyTopOst = "";
	if (arInvoice.getManifest() != null) {
	    fclreadyTopOst = arInvoice.getManifest();
	}
	if (!fclreadyTopOst.equals("M")) {
	    List addList = arInvoiceChargesDAO.getInvoiceChargesList(arInvoice.getId());
	    for (int i = 0; i < addList.size(); i++) {
		ArinvoiceCharges arinvoiceCharges1 = (ArinvoiceCharges) addList.get(i);
		Transaction transaction = new Transaction();
		transaction.setInvoiceNumber(arInvoice.getInvoiceNumber());
		transaction.setTransactionAmt(arinvoiceCharges1.getAmount());
		transaction.setBalanceInProcess(arinvoiceCharges1.getAmount());
		transaction.setBalance(arinvoiceCharges1.getAmount());
		transaction.setTransactionType("AA");
		transaction.setSubledgerSourceCode("AR-FCLE");
		transaction.setStatus("Open");
		transactionDAO.save(transaction);
	    }
	}
    }
}
