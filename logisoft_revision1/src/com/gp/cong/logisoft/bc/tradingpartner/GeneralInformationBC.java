package com.gp.cong.logisoft.bc.tradingpartner;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.CustGeneralDefault;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.CorporateAccountDAO;
import com.gp.cong.logisoft.hibernate.dao.CustGeneralDefaultDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.logiware.utils.AuditNotesUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;

public class GeneralInformationBC extends BaseHibernateDAO {

    GeneralInformation generalInformation = null;
    CustGeneralDefault custGeneralDefault = null;
    TradingPartnerDAO tradingPartnerDAO = null;
    GeneralInformationDAO generalInformationDAO = new GeneralInformationDAO();
    CustGeneralDefaultDAO custGeneralDefaultDAO = new CustGeneralDefaultDAO();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public void saveAccountTypeWhileSavingMainSave(TradingPartnerForm tradingPartnerForm, HttpSession session) throws Exception {
        tradingPartnerDAO = new TradingPartnerDAO();
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        if (null != tradingPartnerForm.getMaster() && !tradingPartnerForm.getMaster().trim().equals("0")) {
            tradingPartner.setMaster(tradingPartnerForm.getMaster());
        }
        if (null != tradingPartnerForm.getSubType() && !tradingPartnerForm.getSubType().trim().equals("0")) {
            tradingPartner.setSubType(tradingPartnerForm.getSubType());
        }
        if (tradingPartner.getGeneralInformation() != null && !tradingPartner.getGeneralInformation().isEmpty()) {
            for (Iterator iter = tradingPartner.getGeneralInformation().iterator(); iter.hasNext();) {
                GeneralInformation generalInformation = (GeneralInformation) iter.next();
                if (tradingPartner.getAcctType() != null) {
                    generalInformation.setAccounttype(tradingPartner.getAcctType());
                }
                if (tradingPartner.getSubType() != null) {
                    generalInformation.setSubType(tradingPartner.getSubType());
                }
            }
        }
        session.setAttribute(TradingPartnerConstants.TRADINGPARTNER, tradingPartner);
    }

    public void saveGeneralInformation(TradingPartnerForm tradingPartnerForm, User loginUser) throws Exception {
        tradingPartnerDAO = new TradingPartnerDAO();
        int i = 0;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        generalInformation = generalInformationDAO.getGeneralInformationByAccountNumber(tradingPartner.getAccountno());
        custGeneralDefault = custGeneralDefaultDAO.getGeneralDefaultByAccountNumber(tradingPartner.getAccountno(), "");
        String accountType = StringUtils.removeStart(tradingPartnerForm.getAccountType(), ",");
        if (!StringUtils.contains(tradingPartner.getAcctType(), "V") && StringUtils.contains(accountType, "V")) {
            AuditNotesUtils.insertAuditNotes("Marking TP as Vendor by " + loginUser.getLoginName() + " on " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"),
                    NotesConstants.APCONFIGURATION, tradingPartner.getAccountno(), "TP as Vendor ", loginUser);
        } else if (StringUtils.contains(tradingPartner.getAcctType(), "V") && !StringUtils.contains(accountType, "V")) {
            AuditNotesUtils.insertAuditNotes("UnMarking TP as Vendor by " + loginUser.getLoginName() + " on " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"),
                    NotesConstants.APCONFIGURATION, tradingPartner.getAccountno(), "TP as Vendor ", loginUser);
        }
        if (CommonUtils.isNotEmpty(accountType)) {
            tradingPartner.setAcctType(accountType);
        }
        String subType = tradingPartner.getSubType();
        AuditNotesUtils.insertAuditNotes(subType, tradingPartnerForm.getSubType(), NotesConstants.APCONFIGURATION, tradingPartner.getAccountno(), "Sub Type ", loginUser);
        if (null != tradingPartnerForm.getSubType()) {
            tradingPartner.setSubType(tradingPartnerForm.getSubType());
        }
        if (CommonUtils.isNotEqual(tradingPartnerForm.getMaster(), "0")) {
            tradingPartner.setMaster(tradingPartnerForm.getMaster());
        } else {
            tradingPartner.setMaster(null);
        }
        if (CommonUtils.isEmpty(tradingPartnerForm.getEciAccountNo())) {
            tradingPartner.setEciAccountNo("");
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getEciAccountNo())) {
            tradingPartner.setEciAccountNo(tradingPartnerForm.getEciAccountNo().toUpperCase());
        } else if (StringUtils.contains(tradingPartner.getAcctType(), "S") && CommonUtils.isEmpty(tradingPartner.getEciAccountNo())) {
            String eciAcctNo = createEciAccount();
            tradingPartner.setEciAccountNo(eciAcctNo.toUpperCase());
        }

        if (CommonUtils.isEmpty(tradingPartnerForm.getEciAccountNo2())) {
            tradingPartner.setECIFWNO("");
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getEciAccountNo2())) {
            tradingPartner.setECIFWNO(tradingPartnerForm.getEciAccountNo2().toUpperCase());
        } else if (StringUtils.contains(tradingPartner.getAcctType(), "C") && CommonUtils.isEmpty(tradingPartner.getECIFWNO())) {
            String eciFwNo = createEciConsigneeAccount();
            tradingPartner.setECIFWNO(eciFwNo.toUpperCase());
        }

        if (CommonUtils.isEmpty(tradingPartnerForm.getEciAccountNo3())) {
            tradingPartner.setECIVENDNO("");
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getEciAccountNo3())) {
            tradingPartner.setECIVENDNO(tradingPartnerForm.getEciAccountNo3().toUpperCase());
        } else if (StringUtils.contains(tradingPartner.getAcctType(), "V") && CommonUtils.isEmpty(tradingPartner.getEciAccountNo())) {
            String eciAcctNo = createEciAccount();
            if (StringUtils.contains(tradingPartner.getSubType(), "Forwarder")) {
                tradingPartner.setEciAccountNo(eciAcctNo.toUpperCase());
            } else {
                String eciVendNo = createEciVendorAccount();
                tradingPartner.setECIVENDNO(eciVendNo.toUpperCase());
            }
        }
        if (StringUtils.contains(tradingPartner.getAcctType(), "V")) {
            tradingPartner.setSslineNumber(tradingPartnerForm.getSslineNumber());
        }
        if ((CommonUtils.in(tradingPartnerForm.getEcuDesignation(), "AG", "IC", "AA"))
                && (!tradingPartner.getEcuDesignation().equalsIgnoreCase(tradingPartnerForm.getEcuDesignation()))) {
            new CustomerAccountingDAO().updateCreditStaus(tradingPartnerForm.getAccountNo(), loginUser);
        }
        tradingPartner.setEcuDesignation(tradingPartnerForm.getEcuDesignation());
        tradingPartner.setBrandPreference(tradingPartnerForm.getBrandPreference());

        if (CommonUtils.isNotEmpty(tradingPartnerForm.getCorporateAcctId())) {
            if (tradingPartnerForm.getCorporateAcctName() != null && tradingPartner.getCorporateAccount() == null) {
                tradingPartner.setCorporateAccount(new CorporateAccountDAO().findById(tradingPartnerForm.getCorporateAcctId()));
                AuditNotesUtils.insertAuditNotes("INSERTED -> Main Account Name ->" + tradingPartnerForm.getCorporateAcctName(), NotesConstants.TRADINGPARTNER, tradingPartner.getAccountno(), "TRADING_PARTNER ", loginUser);
                AuditNotesUtils.insertAuditNotes("INSERTED -> ECU Reporting Type ->" + tradingPartner.getCorporateAccount().getCorporateAccountType().getAcctTypeDescription(), NotesConstants.TRADINGPARTNER, tradingPartner.getAccountno(), "TRADING_PARTNER ", loginUser);
            } else if (tradingPartnerForm.getCorporateAcctName() != null && !tradingPartnerForm.getCorporateAcctId().equals(tradingPartner.getCorporateAccount().getId())) {
                tradingPartner.setCorporateAccount(new CorporateAccountDAO().findById(tradingPartnerForm.getCorporateAcctId()));
                AuditNotesUtils.insertAuditNotes("UPDATED -> Main Account Name ->" + tradingPartnerForm.getCorporateAcctName(), NotesConstants.TRADINGPARTNER, tradingPartner.getAccountno(), "TRADING_PARTNER ", loginUser);
                if (!tradingPartnerForm.getEcuReportingType().equalsIgnoreCase(tradingPartner.getEcuReportingType()) || tradingPartner.getEcuReportingType().equalsIgnoreCase("sa") || tradingPartner.getEcuReportingType().equalsIgnoreCase("")) {
                    AuditNotesUtils.insertAuditNotes("UPDATED -> ECU Reporting Type ->" + tradingPartner.getCorporateAccount().getCorporateAccountType().getAcctTypeDescription(), NotesConstants.TRADINGPARTNER, tradingPartner.getAccountno(), "TRADING_PARTNER ", loginUser);
                }
            }
        } else {
            if ("".equals(tradingPartnerForm.getCorporateAcctName()) && tradingPartner.getCorporateAccount() != null && (!tradingPartnerForm.getCorporateAcctId().equals(tradingPartner.getCorporateAccount()))) {
                AuditNotesUtils.insertAuditNotes("REMOVED -> Main Account Name ->" + tradingPartner.getCorporateAccount().getCorporateName(), NotesConstants.TRADINGPARTNER, tradingPartner.getAccountno(), "TRADING_PARTNER ", loginUser);
                AuditNotesUtils.insertAuditNotes("REMOVED -> ECU Reporting Type ->" + tradingPartner.getCorporateAccount().getCorporateAccountType().getAcctTypeDescription(), NotesConstants.TRADINGPARTNER, tradingPartner.getAccountno(), "TRADING_PARTNER ", loginUser);
                tradingPartner.setCorporateAccount(null);
            } else {
                tradingPartner.setCorporateAccount(null);

            }
        }
        tradingPartner.setEcuReportingType(tradingPartnerForm.getEcuReportingType());
        tradingPartner.setEcuLogo(tradingPartnerForm.getEcuLogo());
        tradingPartner.setFirmsCode(!tradingPartnerForm.getFirmsCode().trim().equals("") ? tradingPartnerForm.getFirmsCode() : null);
        if (null != loginUser) {
            tradingPartner.setUpdateBy(loginUser.getLoginName());
        }

        if (CommonUtils.isEqual(tradingPartnerForm.getActive(), CommonConstants.YES)) {
            tradingPartner.setDisabled(CommonConstants.YES);
            tradingPartner.setDisabledTime(new Date());
        }
        if (CommonUtils.isNotEqualIgnoreCase(tradingPartner.getAccountName(), tradingPartnerForm.getAccountName())) {
            tradingPartner.setAccountName(tradingPartnerForm.getAccountName().trim());
            new TradingPartnerDAO().updateTradingPartnerName(tradingPartnerForm.getAccountNo(), tradingPartnerForm.getAccountName().trim());
        }
        tradingPartnerForm.setDisabled(tradingPartner.getDisabled());
        tradingPartnerForm.setEciAccountNo(tradingPartner.getEciAccountNo());
        tradingPartnerForm.setEciAccountNo2(tradingPartner.getECIFWNO());
        tradingPartnerForm.setEciAccountNo3(tradingPartner.getECIVENDNO());
        tradingPartnerForm.setSslineNumber(tradingPartner.getSslineNumber());
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getForwardAccountName()) && CommonUtils.isNotEmpty(tradingPartner.getForwardAccount())) {
            tradingPartnerForm.setForwardAccount(tradingPartner.getForwardAccount());
            tradingPartnerForm.setForwardAccountName(tradingPartnerDAO.getAccountName(tradingPartner.getForwardAccount()));
        }
        tradingPartnerForm.setAccountType(tradingPartner.getAcctType());
        //--to save account type and subtype while saving from Generalinfo tab---
        custGeneralDefault = new CustGeneralDefault().SetGeneralDefaultInformation(custGeneralDefault, tradingPartnerForm);
        if (tradingPartnerForm.getButtonValue().equals("saveGeneralInformation")) {
            generalInformation = new GeneralInformation().SetGeneralInformation(generalInformation, tradingPartnerForm);
            if (tradingPartner.getAcctType() != null) {
                generalInformation.setAccounttype(tradingPartner.getAcctType());
            }
            if (tradingPartner.getSubType() != null) {
                generalInformation.setSubType(tradingPartner.getSubType());
            }
            generalInformation.setPassword(tradingPartnerForm.getPassword());
        } else {
            generalInformation = new GeneralInformation(tradingPartnerForm);
        }
        Integer commCodeTypeId = new CodetypeDAO().getCodeTypeId("Commodity Codes");
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getCommodity())) {
            GenericCode commodity = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getCommodity(), commCodeTypeId);
            generalInformation.setGenericCode(commodity);
        } else {
            generalInformation.setGenericCode(null);
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getImpCommodity())) {
            GenericCode commodity = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getImpCommodity(), commCodeTypeId);
            generalInformation.setImpCommodity(commodity);
        } else {
            generalInformation.setImpCommodity(null);
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getRetailCommodity())) {
            GenericCode commodity = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getRetailCommodity(), commCodeTypeId);
            generalInformation.setRetailCommodity(commodity);
        } else {
            generalInformation.setRetailCommodity(null);
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getFclCommodity())) {
            GenericCode commodity = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getFclCommodity(), commCodeTypeId);
            generalInformation.setFclCommodity(commodity);
        } else {
            generalInformation.setFclCommodity(null);
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getConsColoadCommodity())) {
            GenericCode commodity = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getConsColoadCommodity(), commCodeTypeId);
            generalInformation.setConsColoadCommodity(commodity);
        } else {
            generalInformation.setConsColoadCommodity(null);
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getConsRetailCommodity())) {
            GenericCode commodity = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getConsRetailCommodity(), commCodeTypeId);
            generalInformation.setConsRetailCommodity(commodity);
        } else {
            generalInformation.setConsRetailCommodity(null);
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getConsFclCommodity())) {
            GenericCode commodity = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getConsFclCommodity(), commCodeTypeId);
            generalInformation.setConsFclCommodity(commodity);
        } else {
            generalInformation.setConsFclCommodity(null);
        }
        Integer salesCodeTypeId = new CodetypeDAO().getCodeTypeId("Sales Code");
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getSalesCode())) {
            GenericCode salesCode = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getSalesCode(), salesCodeTypeId);
            generalInformation.setSalescode(salesCode);
        } else {
            generalInformation.setSalescode(null);
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getConsSalesCode())) {
            GenericCode salesCode = genericCodeDAO.getGenericCodeByCode(tradingPartnerForm.getConsSalesCode(), salesCodeTypeId);
            generalInformation.setConsSalesCode(salesCode);
        } else {
            generalInformation.setConsSalesCode(null);
        }

        //updating type and masterAcctnumber information in cust address table
        Hibernate.initialize(tradingPartner.getCustomerAddressSet());
        Set<CustomerAddress> customerAddressSet = new HashSet();
        if (tradingPartner.getCustomerAddressSet() != null) {
            customerAddressSet.addAll(tradingPartner.getCustomerAddressSet());
        }
        for (Iterator iter = customerAddressSet.iterator(); iter.hasNext();) {
            CustomerAddress customerAddressOld = (CustomerAddress) iter.next();
            CustomerAddress customerAddressNew = customerAddressOld;
            tradingPartner.getCustomerAddressSet().remove(customerAddressOld);
            customerAddressNew.setAccounttype(generalInformation.getAccounttype());
            if (tradingPartnerForm.getAccountType4() != null && tradingPartnerForm.getAccountType4().equalsIgnoreCase("on")) {
            } else {
                customerAddressNew.setNotifyParty("off");
            }
            customerAddressNew.setSubType(generalInformation.getSubType());
            customerAddressNew.setMasteracctno(tradingPartner.getMaster());
            tradingPartner.getCustomerAddressSet().add(customerAddressNew);
        }
        tradingPartner.setPassword(generalInformation.getPassword());
        tradingPartner.setVendorShipperFrtfwdNo(tradingPartnerForm.getVendorShipperFrtfwdNo());
        //saving EciCarrier and EnterDate
        //tradingPartner.setEciCarrier(tradingPartnerForm.getCarrierCode());
        generalInformation.setUpdateBy(null != loginUser ? loginUser.getLoginName() : "");
        generalInformation.setAccountNo(tradingPartner.getAccountno());
        if (tradingPartnerForm.getCFCL() != null && tradingPartnerForm.getCFCL().equalsIgnoreCase("on")) {
            generalInformation.setCFCL("Y");
        } else {
            generalInformation.setCFCL("N");
        }
        generalInformation.setConsUserName(tradingPartnerForm.getConsUserName());
        generalInformation.setConsPassword(tradingPartnerForm.getConsPassword());
        generalInformation.setConsAllowLclWebQuotes(tradingPartnerForm.isConsAllowLclWebQuotes());
        generalInformation.setConsAllowFclWebQuotes(tradingPartnerForm.isConsAllowFclWebQuotes());
        generalInformation.setConsImportTrackingScreen(tradingPartnerForm.isConsImportTrackingScreen());
        generalInformation.setConsActivatePwdQuotes(tradingPartnerForm.isConsActivatePwdQuotes());
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getConsLastPwdActivatedDate())) {
            generalInformation.setConsLastPwdActivatedDate(DateUtils.parseDate(tradingPartnerForm.getConsLastPwdActivatedDate(), "MM/dd/yyyy"));
        } else {
            generalInformation.setConsLastPwdActivatedDate(null);
        }
        generalInformation.setConsFclWebQuoteUseCommodity(tradingPartnerForm.getConsFclWebQuoteUseCommodity());
        generalInformation.setConsLclRateSheet(tradingPartnerForm.getConsLclRateSheet());
        generalInformation.setConsImportWebDapDdp(tradingPartnerForm.getConsImportWebDapDdp());
        generalInformation.setConsLclImportQuoting(tradingPartnerForm.getConsLclImportQuoting());
        generalInformation.setShipffCustControlLogin(tradingPartnerForm.getShipffCustControlLogin());
        generalInformation.setConsCustControlLogin(tradingPartnerForm.getConsCustControlLogin());
        generalInformation.setConsImportFreightRelease(tradingPartnerForm.getConsImportFreightRelease());
        generalInformation.setShipffImportFreightRelease(tradingPartnerForm.getShipffImportFreightRelease());
        generalInformation.setShipffSalesAgencyBrokerageAgreement(tradingPartnerForm.getShipffSalesAgencyBrokerageAgreement());
        generalInformation.setShipffReceiveLclExports315Status(tradingPartnerForm.getShipffReceiveLclExports315Status());
        generalInformation.setShipffInttraAccountNumber(tradingPartnerForm.getShipffInttraAccountNumber());
        generalInformation.setShipffSendLclDocsToWebsite(tradingPartnerForm.getShipffSendLclDocsToWebsite());
        generalInformation.setShipffAllowCFCLWebBooking(tradingPartnerForm.getShipffAllowCFCLWebBooking());
        generalInformation.setConsSalesAgencyBrokerageAgreement(tradingPartnerForm.getConsSalesAgencyBrokerageAgreement());
        generalInformation.setConsReceiveLclExports315Status(tradingPartnerForm.getConsReceiveLclExports315Status());
        generalInformation.setConsInttraAccountNumber(tradingPartnerForm.getConsInttraAccountNumber());
        generalInformation.setConsSendLclDocsToWebsite(tradingPartnerForm.getConsSendLclDocsToWebsite());
        generalInformation.setConsAllowCFCLWebBooking(tradingPartnerForm.getConsAllowCFCLWebBooking());
        generalInformation.setApplyCustomerCommodityRates(tradingPartnerForm.getApplyCustomerCommodityRates());
        custGeneralDefaultDAO.save(custGeneralDefault);
        generalInformationDAO.save(generalInformation);
//        generalInformationDAO.getSession().flush();
    }

    public String createEciAccount() throws Exception {
        List eciAcctNoList = new ArrayList();
        String eciAcctNo = "";
        do {
            String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            StringBuilder eciAcctNoBuilder = new StringBuilder();
            Random random = new Random();
            int te = 0;
            for (int i = 1; i <= 4; i++) {
                te = random.nextInt(52);
                eciAcctNoBuilder.append(str.charAt(te));
            }
            eciAcctNo = "X" + eciAcctNoBuilder.toString();
            eciAcctNoList = new TradingPartnerDAO().getEciAcctNo("X" + eciAcctNoBuilder.toString());
        } while (eciAcctNoList.size() > 0);
        return eciAcctNo;
    }

    public String createEciConsigneeAccount() throws Exception {
        List eciFwNoList = new ArrayList();
        String ecifwno = "";
        do {
            String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            StringBuilder eciFwNoBuilder = new StringBuilder();
            Random random = new Random();
            int te = 0;
            for (int i = 1; i <= 4; i++) {
                te = random.nextInt(52);
                eciFwNoBuilder.append(str.charAt(te));
            }
            ecifwno = "X" + eciFwNoBuilder.toString();
            eciFwNoList = new TradingPartnerDAO().getEciConsigneeNo("X" + eciFwNoBuilder.toString());
        } while (eciFwNoList.size() > 0);
        return ecifwno;
    }

    public String createEciVendorAccount() throws Exception {
        List eciVendNoList = new ArrayList();
        String eciVendNo = "";
        do {
            String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            StringBuilder eciVendNoBuilder = new StringBuilder();
            Random random = new Random();
            int te = 0;
            for (int i = 1; i <= 4; i++) {
                te = random.nextInt(52);
                eciVendNoBuilder.append(str.charAt(te));
            }
            eciVendNo = "X" + eciVendNoBuilder.toString();
            eciVendNoList = new TradingPartnerDAO().getEciVendNo("X" + eciVendNoBuilder.toString());
        } while (eciVendNoList.size() > 0);
        return eciVendNo;
    }
}
