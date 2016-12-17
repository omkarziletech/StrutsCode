package com.gp.cong.logisoft.bc.tradingpartner;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PaymentMethod;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.logiware.utils.AuditNotesUtils;
import java.sql.Blob;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;

public class APConfigurationBC {

    /**
     * @param accountNo
     * @return
     */
    public List getCustometAddresses(TradingPartnerForm tradingPartnerForm) throws Exception {
	CustAddressDAO custAddressDAO = new CustAddressDAO();
	return custAddressDAO.findCustomerAddresses(tradingPartnerForm.getTradingPartnerId(), null);

    }

    /**
     * @param accountNo
     * @param type
     * @return
     */
    public List getMasterAddresses(String accountNo, String type) throws Exception {
	CustAddressDAO custAddressDAO = new CustAddressDAO();
	return custAddressDAO.findCustomerAddresses(accountNo, type);

    }

    /**
     * @param tradingPartnerForm
     * @return
     */
    public TradingPartnerForm saveAPConfigurationAddress(TradingPartnerForm tradingPartnerForm) throws Exception {
	TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
	TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
	Set<CustomerContact> customerContactSet = tradingPartner.getCustomerContact();
	if (null != customerContactSet) {
	    for (CustomerContact customerContact : customerContactSet) {
		if (null != tradingPartnerForm.getContactId() && tradingPartnerForm.getContactId().equals(customerContact.getId().toString())) {
		    tradingPartnerForm = customerContact.setValuesToForm(customerContact, tradingPartnerForm);
		}
	    }
	}
	if (tradingPartner.getPassword() != null) {
	    tradingPartnerForm.setPassword(tradingPartner.getPassword());
	}
	tradingPartnerForm.setPaymentSet(tradingPartner.getPaymentset());
	return tradingPartnerForm;
    }

    public void saveAPconfiguration(TradingPartnerForm tradingPartnerForm, User loginUser) throws Exception {
	TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
	String acctNo = tradingPartnerForm.getTradingPartnerId();
	Vendor vendor = tradingPartnerDAO.getVendor(acctNo);
	if (null != vendor) {
	    vendor.setExemptInactivate(tradingPartnerForm.isExemptInactivate());
	    vendor.setEdiCode(tradingPartnerForm.getEdiCode());
	    AuditNotesUtils.insertAuditNotes(vendor.getTin(), tradingPartnerForm.getTin(), NotesConstants.APCONFIGURATION,
		    acctNo, "TIN", loginUser);
	    vendor.setTin(tradingPartnerForm.getTin());

	    AuditNotesUtils.insertAuditNotes(vendor.getLegalname(), tradingPartnerForm.getLegalname(), NotesConstants.APCONFIGURATION,
		    acctNo, "Legal Name", loginUser);
	    vendor.setLegalname(tradingPartnerForm.getLegalname());

	    AuditNotesUtils.insertAuditNotes(vendor.getDba(), tradingPartnerForm.getDba(), NotesConstants.APCONFIGURATION,
		    acctNo, "DBA", loginUser);
	    vendor.setDba(tradingPartnerForm.getDba());

	    if (CommonUtils.isNotEqual(vendor.getCredit(), tradingPartnerForm.getCredit())) {
		if (CommonUtils.isEqualIgnoreCase(tradingPartnerForm.getCredit(), CommonConstants.ON)) {
		    StringBuilder desc = new StringBuilder("Credit marked by ").append(loginUser.getLoginName());
		    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.APCONFIGURATION, acctNo, "W-9 form", loginUser);
		} else if (CommonUtils.isEqualIgnoreCase(vendor.getCredit(), CommonConstants.ON)) {
		    StringBuilder desc = new StringBuilder("Credit unmarked by ").append(loginUser.getLoginName());
		    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.APCONFIGURATION, acctNo, "W-9 form", loginUser);
		}
		vendor.setCredit(tradingPartnerForm.getCredit());
	    }
	    String creditLimitFromDomain = null;
	    String creditLimitFromForm = null;
	    if (CommonUtils.isNotEmpty(vendor.getClimit())) {
		creditLimitFromDomain = NumberUtils.formatNumber(vendor.getClimit(), "###,###,##0.00");
	    } else {
		creditLimitFromDomain = NumberUtils.formatNumber(0d, "###,###,##0.00");
	    }
	    if (CommonUtils.isNotEmpty(tradingPartnerForm.getClimit())) {
		Double creditLimit = Double.parseDouble(tradingPartnerForm.getClimit().replaceAll(",", ""));
		creditLimitFromForm = NumberUtils.formatNumber(creditLimit, "###,###,##0.00");
		vendor.setClimit(creditLimit);
	    } else {
		vendor.setClimit(null);
	    }
	    AuditNotesUtils.insertAuditNotes(creditLimitFromDomain, creditLimitFromForm,
		    NotesConstants.APCONFIGURATION, acctNo, "Credit Limit", loginUser);

	    String creditTermsFromDomain = null;
	    String creditTermsFromForm = null;
	    if (null != vendor.getCterms()) {
		creditTermsFromDomain = vendor.getCterms().getCodedesc();
	    }
	    if (!CommonUtils.isEqual(tradingPartnerForm.getCterms(), "0")) {
		GenericCode creditTerms = new GenericCodeDAO().findById(Integer.parseInt(tradingPartnerForm.getCterms()));
		creditTermsFromForm = creditTerms.getCodedesc();
		vendor.setCterms(creditTerms);
	    } else {
		vendor.setCterms(null);
	    }
	    AuditNotesUtils.insertAuditNotes(creditTermsFromDomain, creditTermsFromForm,
		    NotesConstants.APCONFIGURATION, acctNo, "Credit Terms", loginUser);

	    String apSpecialistFromDomain = null;
	    String apSpecialistFromForm = null;
	    if (null != vendor.getApSpecialist()) {
		apSpecialistFromDomain = vendor.getApSpecialist().getLoginName();
	    }
	    if (CommonUtils.isNotEmpty(tradingPartnerForm.getApSpecialistId())) {
		User apSpecialist = new UserDAO().findById(Integer.parseInt(tradingPartnerForm.getApSpecialistId()));
		apSpecialistFromForm = apSpecialist.getLoginName();
		vendor.setApSpecialist(apSpecialist);
		vendor.setApSpecialistUpdatedBy(loginUser.getLoginName());
	    } else {
		vendor.setApSpecialistUpdatedBy(null);
		vendor.setApSpecialist(null);
	    }
	    AuditNotesUtils.insertAuditNotes(apSpecialistFromDomain, apSpecialistFromForm,
		    NotesConstants.APCONFIGURATION, acctNo, "AP Specialist", loginUser);
	    if (CommonUtils.isNotEqual(vendor.getWfile(), tradingPartnerForm.getWfile())) {
		if (CommonUtils.isEqualIgnoreCase(tradingPartnerForm.getWfile(), CommonConstants.ON)) {
		    StringBuilder desc = new StringBuilder("W-9 form added by ").append(loginUser.getLoginName());
		    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.APCONFIGURATION, acctNo, "W-9 form", loginUser);
		} else if (CommonUtils.isEqualIgnoreCase(vendor.getWfile(), CommonConstants.ON)) {
		    StringBuilder desc = new StringBuilder("W-9 form removed by ").append(loginUser.getLoginName());
		    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
		    AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.APCONFIGURATION, acctNo, "W-9 form", loginUser);
		    tradingPartnerForm.setFileLocation(null);
		}
	    }
	    vendor.setFileLocation(tradingPartnerForm.getFileLocation());
	    vendor.setWfile(tradingPartnerForm.getWfile());
	    AuditNotesUtils.insertAuditNotes(vendor.getCompany(), tradingPartnerForm.getCompanyName(),
		    NotesConstants.APCONFIGURATION, acctNo, "AP contact - C/O Name ", loginUser);
	    vendor.setCompany(tradingPartnerForm.getCompanyName());

	    AuditNotesUtils.insertAuditNotes(vendor.getPhone(), tradingPartnerForm.getPhone(),
		    NotesConstants.APCONFIGURATION, acctNo, "AP contact - Phone ", loginUser);
	    vendor.setPhone(tradingPartnerForm.getPhone());

	    AuditNotesUtils.insertAuditNotes(vendor.getFax(), tradingPartnerForm.getFax(),
		    NotesConstants.APCONFIGURATION, acctNo, "AP contact - Fax ", loginUser);
	    vendor.setFax(tradingPartnerForm.getFax());

	    AuditNotesUtils.insertAuditNotes(vendor.getEmail(), tradingPartnerForm.getEmail(),
		    NotesConstants.APCONFIGURATION, acctNo, "AP contact - Email ", loginUser);
	    vendor.setEmail(tradingPartnerForm.getEmail());
//            String subTypeFromDomain = tradingPartnerDAO.getSubType(acctNo);
//            AuditNotesUtils.insertAuditNotes(subTypeFromDomain, tradingPartnerForm.getSubType(),
//                    NotesConstants.APCONFIGURATION, acctNo, "Sub Type ", loginUser);
//            tradingPartnerDAO.updateSubType(acctNo, tradingPartnerForm.getSubType());
	    tradingPartnerDAO.update(vendor);
	} else {
	    vendor = new Vendor(tradingPartnerForm);
	    if (tradingPartnerForm.getCterms() != null && !tradingPartnerForm.getCterms().trim().equals("0")) {
		GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
		vendor.setCterms(genericCodeDAO.findById(Integer.parseInt(tradingPartnerForm.getCterms())));
	    }
	    if (CommonUtils.isNotEmpty(tradingPartnerForm.getApSpecialistId())) {
		User apSpecialist = new UserDAO().findById(Integer.parseInt(tradingPartnerForm.getApSpecialistId()));
		vendor.setApSpecialist(apSpecialist);
		vendor.setApSpecialistUpdatedBy(loginUser.getLoginName());
	    }
	    if (CommonUtils.isNotEmpty(tradingPartnerForm.getFileLocation())) {
		vendor.setFileLocation(tradingPartnerForm.getFileLocation());
	    }
//            if (CommonUtils.isNotEmpty(tradingPartnerForm.getSubType())) {
//                tradingPartnerDAO.updateSubType(acctNo, tradingPartnerForm.getSubType());
//            }
	    AuditNotesUtils.insertAuditNotes("AP Config setted up", NotesConstants.APCONFIGURATION, acctNo, "Vendor", loginUser);
	    tradingPartnerDAO.save(vendor);
	}
    }

    /**
     * @description Save Or Update Payment Method
     * @param tradingPartnerForm
     * @param loginUser
     */
    public void saveOrUpdatePaymentMethod(TradingPartnerForm tradingPartnerForm, User loginUser) throws Exception {
	TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
	String acctNo = tradingPartnerForm.getTradingPartnerId();
	tradingPartnerDAO.updateDefaultPaymentMethod(acctNo);
	PaymentMethod paymentMethod = tradingPartnerDAO.getPaymentMethod(acctNo, tradingPartnerForm.getPaymethod());
	if (null != paymentMethod) {
	    paymentMethod.setDefaultpaymethod(tradingPartnerForm.getDefaultPaymentMethod());
	    paymentMethod.setPaymethod(StringUtils.upperCase(tradingPartnerForm.getPaymethod()));
	    paymentMethod.setBankname(StringUtils.upperCase(tradingPartnerForm.getBankname()));
	    paymentMethod.setBaddr(StringUtils.upperCase(tradingPartnerForm.getBaddr()));
	    paymentMethod.setVacctname(StringUtils.upperCase(tradingPartnerForm.getVacctname()));
	    paymentMethod.setVacctno(StringUtils.upperCase(tradingPartnerForm.getVacctno()));
	    paymentMethod.setAba(StringUtils.upperCase(tradingPartnerForm.getAba()));
	    paymentMethod.setRemail(tradingPartnerForm.getRemail());
	    paymentMethod.setRfax(StringUtils.upperCase(tradingPartnerForm.getRfax()));
	    paymentMethod.setPayacctno(StringUtils.upperCase(tradingPartnerForm.getTradingPartnerId()));
	    paymentMethod.setSwift(StringUtils.upperCase(tradingPartnerForm.getSwift()));
	    if (CommonUtils.isEqualIgnoreCase(paymentMethod.getPaymethod(), CommonConstants.PAYMENT_METHOD_ACH)) {
		if (null != tradingPartnerForm.getAchDocument() && tradingPartnerForm.getAchDocument().getFileSize() > 0) {
		    paymentMethod.setAchDocumentName(tradingPartnerForm.getAchDocument().getFileName());
		    paymentMethod.setAchDocumentContentType(tradingPartnerForm.getAchDocument().getContentType());
		    Blob blob = tradingPartnerDAO.getSession().getLobHelper().createBlob(tradingPartnerForm.getAchDocument().getFileData());
		    paymentMethod.setAchDocument(blob);
		}
	    }
	    String desc = "Payment Method '" + paymentMethod.getPaymethod() + "' updated by " + loginUser.getLoginName()
		    + " on " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
	    AuditNotesUtils.insertAuditNotes(desc, NotesConstants.APCONFIGURATION, tradingPartnerForm.getTradingPartnerId(),
		    "Payment Method", loginUser);
	    tradingPartnerDAO.update(paymentMethod);
	} else {
	    paymentMethod = new PaymentMethod(tradingPartnerForm);
	    if (tradingPartnerDAO.isFirstPaymentMethod(acctNo)) {
		paymentMethod.setDefaultpaymethod(CommonConstants.YES);
	    }
	    if (CommonUtils.isEqualIgnoreCase(paymentMethod.getPaymethod(), CommonConstants.PAYMENT_METHOD_ACH)) {
		if (null != tradingPartnerForm.getAchDocument()) {
		    paymentMethod.setAchDocumentName(tradingPartnerForm.getAchDocument().getFileName());
		    paymentMethod.setAchDocumentContentType(tradingPartnerForm.getAchDocument().getContentType());
		     Blob blob = tradingPartnerDAO.getSession().getLobHelper().createBlob(tradingPartnerForm.getAchDocument().getFileData());
		    paymentMethod.setAchDocument(blob);
		}
	    }
	    String desc = "Payment Method '" + paymentMethod.getPaymethod() + "' added by " + loginUser.getLoginName()
		    + " on " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
	    AuditNotesUtils.insertAuditNotes(desc, NotesConstants.APCONFIGURATION, tradingPartnerForm.getTradingPartnerId(),
		    "Payment Method", loginUser);
	    tradingPartnerDAO.save(paymentMethod);
	}
    }

    /**
     * @description Delete Payment Method
     * @param tradingPartnerForm
     * @param loginUser
     */
    public void deletePaymentMethod(TradingPartnerForm tradingPartnerForm, User loginUser) throws Exception {
	TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
	String acctNo = tradingPartnerForm.getTradingPartnerId();
	String desc = "Payment Method '" + tradingPartnerForm.getPayMethodId() + "' removed by " + loginUser.getLoginName()
		+ " on " + DateUtils.formatDate(new Date(), "MM/dd/yyyy");
	AuditNotesUtils.insertAuditNotes(desc, NotesConstants.APCONFIGURATION, acctNo, "Payment Method", loginUser);
	tradingPartnerDAO.deletePaymentMethod(acctNo, tradingPartnerForm.getPayMethodId());
    }

    public TradingPartnerForm loadAPConfiguration(TradingPartner tradingPartner, TradingPartnerForm tradingPartnerForm) throws Exception {
	if (null != tradingPartner.getVendorset() && tradingPartner.getVendorset().size() > 0) {
	    Iterator iterator = tradingPartner.getVendorset().iterator();


	    while (iterator.hasNext()) {
		Vendor vendor = (Vendor) iterator.next();
		tradingPartnerForm.setEdiCode(vendor.getEdiCode());
		tradingPartnerForm.setLegal(vendor.getLegalname());
		tradingPartnerForm.setLegalname(vendor.getLegalname());
		tradingPartnerForm.setDba(vendor.getDba());
		tradingPartnerForm.setTin(vendor.getTin());
		tradingPartnerForm.setWfile(vendor.getWfile());
		tradingPartnerForm.setApname(vendor.getApname());
		tradingPartnerForm.setWeb(vendor.getWeb());


		if (null != vendor.getEamanager()) {
		    tradingPartnerForm.setEamanager(vendor.getEamanager());


		}

		if (null != vendor.getApSpecialist()) {
		    tradingPartnerForm.setApSpecialistId(vendor.getApSpecialist().getLoginName());


		}

		tradingPartnerForm.setCredit(vendor.getCredit());


		if (null != vendor.getClimit()) {
		    tradingPartnerForm.setClimit(vendor.getClimit().toString());


		}
		if (null != vendor.getCterms()) {
		    tradingPartnerForm.setCterms(vendor.getCterms().getCode());


		}

		tradingPartnerForm.setBaccount(vendor.getBaccount());
		tradingPartnerForm.setBaddr(vendor.getAddress1());
		tradingPartnerForm.setRfax(vendor.getFax());
		tradingPartnerForm.setDeactivate(vendor.getDeactivate());


	    }
	}
	return tradingPartnerForm;


    }

    public Vendor getApInvoiceByCustomerNumber(String customerNumeber) throws Exception {
	TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();


	return tradingPartnerDAO.getApInvoiceByCustomerNumber(customerNumeber);


    }

    public Vendor getVendorByCustomerNumber(String customerNumeber) throws Exception {
	TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();


	return tradingPartnerDAO.getVendorByCustomerNumber(customerNumeber);

    }
}
