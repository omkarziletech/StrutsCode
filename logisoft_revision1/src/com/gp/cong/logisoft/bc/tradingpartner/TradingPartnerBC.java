package com.gp.cong.logisoft.bc.tradingpartner;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.CustGeneralDefault;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.GeneralInformation;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.CustGeneralDefaultDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.GeneralInformationDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.logiware.utils.AuditNotesUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;

public class TradingPartnerBC extends BaseHibernateDAO implements TradingPartnerConstants {

    /**
     * @param tradingPartnerForm
     * @return when user enter account name this method will generate account no
     * dynamically and add in DB...
     *
     */
    TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();

    public TradingPartner saveCustomer(TradingPartnerForm tradingPartnerForm) throws Exception {
        TradingPartner tradingPartner = null;
        List accountno = tradingPartnerDAO.findAccountNumber(tradingPartnerForm.getAccountName());
        if (accountno == null || accountno.isEmpty()) {
            tradingPartner = new TradingPartner();
            tradingPartner.setAccountName(tradingPartnerForm.getAccountName().toUpperCase());
            String stringTokenone = "";
            int stringTokenTwo = 0;
            int stringTokenThree = 0;
            String stringTokenFour = "";
            String accountNo = "";
            if (tradingPartnerForm.getAccountName() != null
                    && tradingPartnerForm.getAccountName().length() > 0) {
                for (int i = 0; i < tradingPartnerForm.getAccountName().length(); i++) {
                    if (tradingPartnerForm.getAccountName().charAt(i) == ' ') {
                        stringTokenTwo++;
                        if (stringTokenTwo == 1 && stringTokenThree == 0) {
                            if (stringTokenone.length() >= 3) {
                                stringTokenone = stringTokenone.substring(0, 3);
                            }
                        }
                    } else if (tradingPartnerForm.getAccountName().charAt(i) == '.') {
                        stringTokenThree++;
                    } else {
                        if (stringTokenTwo == 0) {
                            stringTokenone = stringTokenone.concat(String.valueOf(tradingPartnerForm.getAccountName().charAt(i)));
                        } else if (stringTokenTwo > 0) {
                            stringTokenFour = stringTokenFour.concat(String.valueOf(tradingPartnerForm.getAccountName().charAt(i)));
                        }
                    }
                    if (stringTokenTwo >= 2) {
                        break;
                    }
                }
                if (stringTokenFour.length() > 3) {
                    stringTokenFour = stringTokenFour.substring(0, 3);
                }
                stringTokenone = stringTokenone.concat(stringTokenFour);
                if (stringTokenTwo == 0 && stringTokenone.length() >= 3
                        && stringTokenThree == 0) {
                    stringTokenone = stringTokenone.substring(0, 3);
                }
            }
            if (stringTokenone.length() == 1) {
                stringTokenone += "AAAAA";
            } else if (stringTokenone.length() == 2) {
                stringTokenone += "AAAA";
            } else if (stringTokenone.length() == 3) {
                stringTokenone += "AAA";
            } else if (stringTokenone.length() == 4) {
                stringTokenone += "AA";
            } else if (stringTokenone.length() == 5) {
                stringTokenone += "A";
            }
            if (stringTokenone.length() > 6) {
                stringTokenone = stringTokenone.substring(0, 6);
            }

            if (stringTokenone != null) {
                if (tradingPartnerForm.getFromMaster() != null
                        && tradingPartnerForm.getFromMaster().equals("YES")) {
                    accountNo = stringTokenone + "0000";
                } else {
                    String acctno = "";
                    List customerList = tradingPartnerDAO.findAccountPrefix(stringTokenone);
                    if (customerList != null && customerList.size() > 0) {
                        TradingPartner c1 = (TradingPartner) customerList.get(0);
                        String acctNo = c1.getAccountno();
                        for (int i = acctNo.length() - 4; i < acctNo.length(); i++) {
                            boolean check = Character.isDigit(acctNo.charAt(i));
                            if (check) {
                                acctno += acctNo.charAt(i);
                            }
                        }
                        Integer a3 = Integer.parseInt(acctno);
                        a3 += 1;
                        if (a3.toString().length() == 1) {
                            accountNo = "000" + a3;
                        } else if (a3.toString().length() == 2) {
                            accountNo = "00" + a3;
                        } else if (a3.toString().length() == 3) {
                            accountNo = "0" + a3;
                        }
                        accountNo = stringTokenone + accountNo;
                    } else {
                        accountNo = stringTokenone + "0001";
                    }
                }
            }
            String accountPrefix = accountNo;
            tradingPartner.setAccountPrefix(accountPrefix);
            tradingPartner.setAccountno(accountPrefix.toUpperCase());
            if (tradingPartnerForm.getMaster() != null
                    && !tradingPartnerForm.getMaster().equals("0")) {
                TradingPartner tradingPartnerMaster = tradingPartnerDAO.findById(tradingPartnerForm.getMaster());
                tradingPartner.setMaster(tradingPartnerMaster.getAccountno().toUpperCase());
            }
            tradingPartner.setType(getType(tradingPartnerForm));
            List eciAcctNoList = new ArrayList();
            String eciAcctNo = "";
            do {
                String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                StringBuilder sb = new StringBuilder();
                Random r = new Random();
                int te = 0;
                for (int i = 1; i <= 4; i++) {
                    te = r.nextInt(52);
                    sb.append(str.charAt(te));
                }
                eciAcctNo = "X" + sb.toString();
                eciAcctNoList = tradingPartnerDAO.getEciAcctNo("X"
                        + sb.toString());
            } while (eciAcctNoList.size() > 0);
            tradingPartner.setEciAccountNo(eciAcctNo.toUpperCase());
            if (null != tradingPartnerForm.getAccountType4() && tradingPartnerForm.getAccountType4().equalsIgnoreCase("on")) {
                tradingPartner.setECIFWNO(eciAcctNo.toUpperCase());
            }
            if (null != tradingPartnerForm.getAccountType10() && tradingPartnerForm.getAccountType10().equalsIgnoreCase("on")) {
                tradingPartner.setECIVENDNO(eciAcctNo.toUpperCase());
            }
            if (null != tradingPartnerForm.getAccountType() && !tradingPartnerForm.getAccountType().trim().equals("")) {
                tradingPartner.setAcctType(tradingPartnerForm.getAccountType());
            }
            tradingPartnerDAO.save(tradingPartner);
        } else {
        }
        return tradingPartner;
    }

    public void updateMaster(TradingPartnerForm tradingPartnerForm) throws Exception {
        TradingPartner tradingPartner = new TradingPartner();
        if (tradingPartnerForm.getTradingPartnerId() != null
                && !tradingPartnerForm.getTradingPartnerId().equals("")) {
            tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
            tradingPartner.setMaster(tradingPartnerForm.getMaster());
        }
        tradingPartnerDAO.save(tradingPartner);
    }

    public List getCustomerMatchList(TradingPartnerForm tradingPartnerForm) throws Exception {
        String accountType = "";
        if (tradingPartnerForm.getAccountType1() != null
                && !tradingPartnerForm.getAccountType1().trim().equals("")) {
            accountType = "S,";
        }
        if (tradingPartnerForm.getAccountType2() != null
                && !tradingPartnerForm.getAccountType2().trim().equals("")) {
            accountType += "F,";
        }
        if (tradingPartnerForm.getAccountType3() != null
                && !tradingPartnerForm.getAccountType3().trim().equals("")) {
            accountType += "N,";
        }
        if (tradingPartnerForm.getAccountType4() != null
                && !tradingPartnerForm.getAccountType4().trim().equals("")) {
            accountType += "C,";
        }
        if (tradingPartnerForm.getAccountType5() != null
                && !tradingPartnerForm.getAccountType5().trim().equals("")) {
            accountType += "SS,";
        }
        if (tradingPartnerForm.getAccountType6() != null
                && !tradingPartnerForm.getAccountType6().trim().equals("")) {
            accountType += "T,";
        }
        if (tradingPartnerForm.getAccountType7() != null
                && !tradingPartnerForm.getAccountType7().trim().equals("")) {
            accountType += "A,";
        }
        if (tradingPartnerForm.getAccountType8() != null
                && !tradingPartnerForm.getAccountType8().trim().equals("")) {
            accountType += "I,";
        }
        if (tradingPartnerForm.getAccountType9() != null
                && !tradingPartnerForm.getAccountType9().trim().equals("")) {
            accountType += "E,";
        }
        if (tradingPartnerForm.getAccountType10() != null
                && !tradingPartnerForm.getAccountType10().trim().equals("")) {
            accountType += "V,";
        }
        if (tradingPartnerForm.getAccountType11() != null
                && !tradingPartnerForm.getAccountType11().trim().equals("")) {
            accountType += "O,";
        }
        if (tradingPartnerForm.getAccountType12() != null
                && !tradingPartnerForm.getAccountType12().trim().equals("")) {
            accountType += "L,";
        }
        if (tradingPartnerForm.getAccountType13() != null
                && !tradingPartnerForm.getAccountType13().trim().equals("")) {
            accountType += "Z,";
        }
        if (accountType != null && accountType.length() > 0) {
            accountType = accountType.substring(0, accountType.length() - 1);
        }
        String type = "", acctType = "";
        type = getType(tradingPartnerForm);
        String acctTypeToken[] = StringUtils.splitPreserveAllTokens(
                accountType, ",");
        for (int i = 0; i < acctTypeToken.length; i++) {
            acctType = acctType + "%" + acctTypeToken[i];
        }

        if (!acctType.trim().equals("")) {
            acctType += "%";
        }
        String name = tradingPartnerForm.getName() != null ? tradingPartnerForm.getName().trim() : "";
        String limit = tradingPartnerForm.getLimit() != null ? tradingPartnerForm.getLimit() : "250";
        return tradingPartnerDAO.searchCustomer(tradingPartnerForm.getAccount(), name, acctType, type, tradingPartnerForm.getSortBy(), tradingPartnerForm.getSearchAddress(), limit, tradingPartnerForm.getSearchBySubType(), tradingPartnerForm.getBlueScreenAccount(), tradingPartnerForm);
    }

    public TradingPartner getCustomer(TradingPartnerForm tradingPartnerForm) throws Exception {
        TradingPartner tradingPartner = null;
        if (tradingPartnerForm.getTradingPartnerId() != null
                && !tradingPartnerForm.getTradingPartnerId().equals("")) {
            tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        }
        return tradingPartner;
    }

    /**
     * @param accountNumber
     * @return
     */
    public TradingPartner findTradingPartnerById(String accountNumber) throws Exception {
        return tradingPartnerDAO.findById(accountNumber);
    }

    public List getCustomerUpdatedList(TradingPartnerForm tradingPartnerForm) throws Exception {
        List updatedList = null;
        if (tradingPartnerForm.getTradingPartnerId() != null
                && !tradingPartnerForm.getTradingPartnerId().equals("")) {
            updatedList = tradingPartnerDAO.getUpdatedCustomerRecord(tradingPartnerForm.getTradingPartnerId());
        }
        return updatedList;
    }

    public TradingPartnerForm setFormValue(TradingPartner tradingPartner) throws Exception {
        TradingPartnerForm tradingPartnerForm = new TradingPartnerForm();
        // setting the value from generalinformation domain to form object
        if (null != tradingPartner.getGeneralInformation()
                && tradingPartner.getGeneralInformation().size() > 0) {
            Iterator iterator = tradingPartner.getGeneralInformation().iterator();
            while (iterator.hasNext()) {
                GeneralInformation generalInformation = (GeneralInformation) iterator.next();
                tradingPartnerForm = generalInformation.loadGeneralInformation(
                        generalInformation, tradingPartnerForm);
            }
        }
        CustGeneralDefault custGeneralDefault = new CustGeneralDefaultDAO().getGeneralDefaultByAccountNumber(tradingPartner.getAccountno(), "");
        if (null != custGeneralDefault) {
            tradingPartnerForm = custGeneralDefault.loadCustGeneralDefaultInformation(
                    custGeneralDefault, tradingPartnerForm);
        } else {
            tradingPartnerForm.setPrepaidOrCollect("P");
            tradingPartnerForm.setBillTo("F");
            tradingPartnerForm.setFfComm("N");
            tradingPartnerForm.setDocumentCharge("N");
            tradingPartnerForm.setApplyDefaultValues("N");
        }
        // setting the value from CustomerAccounting domain to form object
        if (null != tradingPartner.getAccounting()
                && tradingPartner.getAccounting().size() > 0) {
            Iterator iterator = tradingPartner.getAccounting().iterator();
            while (iterator.hasNext()) {
                CustomerAccounting customerAccounting = (CustomerAccounting) iterator.next();
                tradingPartnerForm = customerAccounting.loadCustomerAccountingDetails(customerAccounting,
                        tradingPartnerForm);
            }
        }
        // setting the value from Vendor domain to form object
        if (null != tradingPartner.getVendorset()
                && tradingPartner.getVendorset().size() > 0) {
            Iterator iterator = tradingPartner.getVendorset().iterator();
            while (iterator.hasNext()) {
                Vendor vendor = (Vendor) iterator.next();
                tradingPartnerForm = vendor.loadAPConfiguration(vendor,
                        tradingPartnerForm);
                tradingPartnerForm.setSubType(tradingPartner.getSubType());
            }
        } else {
            tradingPartnerForm.setClimit("0.00");
            tradingPartnerForm.setCterms("11344");
        }

        // set value for ECI Account number
        tradingPartnerForm.setEciAccountNo(tradingPartner.getEciAccountNo());
        tradingPartnerForm.setEciAccountNo2(tradingPartner.getECIFWNO());
        tradingPartnerForm.setEciAccountNo3(tradingPartner.getECIVENDNO());
        // setting sslineNumber and vendorShipperFrtfwdNumber
        tradingPartnerForm.setSslineNumber(tradingPartner.getSslineNumber());
        tradingPartnerForm.setVendorShipperFrtfwdNo(tradingPartner.getVendorShipperFrtfwdNo());
        tradingPartnerForm.setSubType(tradingPartner.getSubType());

        //Consignee Info
        tradingPartnerForm.setPortNumber(tradingPartner.getPortNumber());
        tradingPartnerForm.setTaxExempt(tradingPartner.getTaxExempt());
        tradingPartnerForm.setFederalId(tradingPartner.getFederalId());
        tradingPartnerForm.setConsigneeNotifyParty(tradingPartner.getNotifyParty());
        tradingPartnerForm.setNotifyPartyAddress(tradingPartner.getNotifyPartyAddress());
        tradingPartnerForm.setNotifyPartyCity(tradingPartner.getNotifyPartyCity());
        tradingPartnerForm.setNotifyPartyState(tradingPartner.getNotifyPartyState());
        tradingPartnerForm.setNotifyPartyCountry(tradingPartner.getNotifyPartyCountry());
        tradingPartnerForm.setNotifyPartyPostalCode(tradingPartner.getNotifyPartyPostalCode());

        tradingPartnerForm.setAccountName(tradingPartner.getAccountName());
        tradingPartnerForm.setAccountNo(tradingPartner.getAccountno());
        tradingPartnerForm.setForwardAccount(tradingPartner.getForwardAccount());
        String forwardAccountName = "";
        if (CommonUtils.isNotEmpty(tradingPartner.getForwardAccount())) {
            forwardAccountName = tradingPartnerDAO.getAccountName(tradingPartner.getForwardAccount());
        }
        tradingPartnerForm.setForwardAccountName(forwardAccountName);
        tradingPartnerForm.setEcuDesignation(tradingPartner.getEcuDesignation());
        tradingPartnerForm.setEcuReportingType(tradingPartner.getEcuReportingType());
        tradingPartnerForm.setFirmsCode(tradingPartner.getFirmsCode());
        tradingPartnerForm.setEcuLogo(tradingPartner.getEcuLogo());
        tradingPartnerForm.setBrandPreference(tradingPartner.getBrandPreference());
        if (null != tradingPartner.getCorporateAccount()) {
            tradingPartnerForm.setCorporateAcctName(tradingPartner.getCorporateAccount().getCorporateName());
            tradingPartnerForm.setCorporateAcctId(tradingPartner.getCorporateAccount().getId());
        }
        return tradingPartnerForm;
    }

    private String getType(TradingPartnerForm tradingPartnerForm) throws Exception {
        if (CommonUtils.isEqualIgnoreCase(tradingPartnerForm.getFromMaster(), "YES")) {
            return "master";
        } else {
            return "mb";
        }
    }

    public List getSubsidiariesAccountForMaster(String accountNumber) throws Exception {
        return tradingPartnerDAO.getSubsidiariesAccountForMaster(accountNumber);
    }

    public String getCustomerOfAccountTypeZ() throws Exception {
        String accountType = "Z";
        return tradingPartnerDAO.getCustomerWithAccountTypeZ(accountType);
    }

    public String getAcounttype(String accountName) throws Exception {
        List tempList = tradingPartnerDAO.findAccountNumber(accountName);
        if (tempList != null) {
            TradingPartner tp = (TradingPartner) tempList.get(0);
            return tp.getAcctType();
        } else {
            return "";
        }

    }

    public TradingPartner findAccountNumberByPassingAccountName(
            String accountName) throws Exception {
        return tradingPartnerDAO.findAccountNumberByPassingAccountName(accountName);
    }

    public String getDisabledRecord(String accountNO) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.getDisabledRecord(accountNO);
        if (null != tradingPartner && null != tradingPartner.getDisabledTime() && "Y".equalsIgnoreCase(tradingPartner.getDisabled())) {
            return "This Trading Partner was disabled on " + new SimpleDateFormat("dd-MMM-yyyy 'at' hh:mm a").format(tradingPartner.getDisabledTime());
        }
        return "";
    }

    public String getSalesCodeInfoDWR(String code, String codeDesc) throws Exception {
        String codeType = "23";
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List codeList = genericCodeDAO.findForChargeCodesForAirRates(code,
                codeDesc, codeType);
        if (null != code && !code.equals("") && null != codeList
                && codeList.size() > 0) {
            return ((GenericCode) codeList.get(0)).getCodedesc();
        } else if (null != codeDesc && !codeDesc.equals("") && null != codeList
                && codeList.size() > 0) {
            return ((GenericCode) codeList.get(0)).getCode();
        } else {
            return "";
        }
    }

    public void updateTPCustAddGenInfo(String eciAcctNo, String eciFwNo,
            String eciVendNo, String master, String acctType,
            String sslineNumber, String subType, String acctNo, HttpServletRequest request) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.findById(acctNo);
        if (null != tradingPartner) {
            tradingPartner.setEciAccountNo(eciAcctNo);
            tradingPartner.setECIFWNO(eciFwNo);
            tradingPartner.setECIVENDNO(eciVendNo);
            tradingPartner.setMaster(master);
            tradingPartner.setSslineNumber(null != sslineNumber ? sslineNumber.toUpperCase() : null);
            if (CommonUtils.isNotEmpty(tradingPartner.getCustomerAddressSet())) {
                for (CustomerAddress custOmerAddress : tradingPartner.getCustomerAddressSet()) {
                    custOmerAddress.setAccounttype(acctType);
                }
            }

            if (CommonUtils.isNotEmpty(tradingPartner.getGeneralInformation())) {
                for (GeneralInformation generalInformation : tradingPartner.getGeneralInformation()) {
                    generalInformation.setAccounttype(acctType);
                }
            } else {
                GeneralInformation generalInformation = new GeneralInformation();
                generalInformation.setAccountNo(acctNo);
                generalInformation.setAccounttype(acctType);
                tradingPartner.getGeneralInformation().add(generalInformation);
            }
            User loginUser = (User) request.getSession().getAttribute("loginuser");
            if (!StringUtils.contains(tradingPartner.getAcctType(), "V") && StringUtils.contains(acctType, "V")) {
                AuditNotesUtils.insertAuditNotes("Marking TP as Vendor by " + loginUser.getLoginName() + " on " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"),
                        NotesConstants.APCONFIGURATION, acctNo, "TP as Vendor ", loginUser);
            } else if (StringUtils.contains(tradingPartner.getAcctType(), "V") && !StringUtils.contains(acctType, "V")) {
                AuditNotesUtils.insertAuditNotes("UnMarking TP as Vendor by " + loginUser.getLoginName() + " on " + DateUtils.formatDate(new Date(), "MM/dd/yyyy"),
                        NotesConstants.APCONFIGURATION, acctNo, "TP as Vendor ", loginUser);
            }
            tradingPartner.setAcctType(acctType);
        }
    }

    public void getCustomerContactList(Map<String, CustomerContact> custContactMap, String accountNumber) throws Exception {
        List<CustomerContact> returnList = new ArrayList<CustomerContact>();
        TradingPartner tradingPartner = findTradingPartnerById(accountNumber);
        if (tradingPartner != null && tradingPartner.getCustomerContact() != null) {
            for (Iterator iterator = tradingPartner.getCustomerContact().iterator(); iterator.hasNext();) {
                CustomerContact customerContact = (CustomerContact) iterator.next();
                customerContact.setAccountName(tradingPartner.getAccountName());
                customerContact.setAccountType(tradingPartner.getAcctType());
                customerContact.setSubType(tradingPartner.getSubType());
                custContactMap.put(customerContact.getAccountNo() + "-" + customerContact.getEmail(), customerContact);
            }
        }

    }

    public TradingPartner addNewTradingPartner(TradingPartnerForm tradingPartnerForm, User user) throws Exception {
        TradingPartner tradingPartner = new TradingPartner();
        tradingPartner.setAccountName(tradingPartnerForm.getAccountName().toUpperCase());
        StringBuilder accountNumber = new StringBuilder();
        int i = 0;
        for (String account : StringUtils.split(tradingPartnerForm.getAccountName())) {
            accountNumber.append(StringUtils.substring(account.replaceAll("[^a-zA-Z0-9]", ""), 0, 3));
            if (i == 1) {
                break;
            }
            i++;
        }
        accountNumber = new StringBuilder(StringUtils.rightPad(accountNumber.toString(), 6, "A"));
        Integer prefix = Integer.parseInt(tradingPartnerDAO.getAccountPrefix(accountNumber.toString()));
        prefix++;
        accountNumber.append(StringUtils.leftPad(prefix.toString(), 4, "0"));
        tradingPartner.setAccountPrefix(accountNumber.toString().toUpperCase());
        tradingPartner.setAccountno(accountNumber.toString().toUpperCase());
        tradingPartner.setAcctType(StringUtils.removeEnd(tradingPartnerForm.getAccountType(), ","));
        tradingPartner.setEnterDate(new Date());
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getMaster()) && CommonUtils.isNotEqual(tradingPartnerForm.getMaster(), "0")) {
            TradingPartner tradingPartnerMaster = tradingPartnerDAO.findById(tradingPartnerForm.getMaster());
            tradingPartner.setMaster(tradingPartnerMaster.getAccountno().toUpperCase());
        }
        tradingPartner.setCustomerLocation(new UnLocationDAO().getUnlocation(tradingPartnerForm.getUnLocCode()));
        tradingPartner.setType(getType(tradingPartnerForm));
        if (StringUtils.contains(tradingPartnerForm.getAccountType(), "S")) {
            String eciAcctNo = new GeneralInformationBC().createEciAccount();
            tradingPartner.setEciAccountNo(eciAcctNo.toUpperCase());
        }
        if (StringUtils.contains(tradingPartnerForm.getAccountType(), "C")) {
            String eciFwNo = "";
            eciFwNo = new GeneralInformationBC().createEciConsigneeAccount();
            tradingPartner.setECIFWNO(eciFwNo.toUpperCase());
        }
        if (StringUtils.contains(tradingPartnerForm.getAccountType(), "V")) {
            String eciAccountNo = "";
            if (CommonUtils.isEmpty(tradingPartner.getEciAccountNo()) && null != tradingPartnerForm.getSubType() && tradingPartnerForm.getSubType().contains("Forwarder")) {
                eciAccountNo = new GeneralInformationBC().createEciAccount();
                tradingPartner.setEciAccountNo(eciAccountNo.toUpperCase());
            } else if (null != tradingPartnerForm.getSubType() && !tradingPartnerForm.getSubType().contains("Forwarder")) {
                String VendNo = new GeneralInformationBC().createEciVendorAccount();
                tradingPartner.setECIVENDNO(VendNo.toUpperCase());
            }
            tradingPartner.setSubType(tradingPartnerForm.getSubType());
        }
        if (CommonUtils.isEqual(tradingPartnerForm.getActive(), CommonConstants.YES)) {
            tradingPartner.setDisabled(CommonConstants.YES);
            tradingPartner.setDisabledTime(new Date());
        }
        tradingPartner.setEcuDesignation(tradingPartnerForm.getEcuDesignation());
        tradingPartner.setBrandPreference(tradingPartnerForm.getBrandPreference());
        tradingPartnerDAO.save(tradingPartner);
        GeneralInformation generalInfo = new GeneralInformation();
        generalInfo.setAccountNo(tradingPartner.getAccountno());
        generalInfo.setAccounttype(tradingPartner.getAcctType());
        generalInfo.setSubType(tradingPartner.getSubType());
        generalInfo.setActive(tradingPartner.getDisabled());
        generalInfo.setPoa(CommonConstants.NO);
        generalInfo.setFclMailingList(CommonConstants.NO);
        generalInfo.setFaxSailingSchedule(CommonConstants.NO);
        generalInfo.setChristmasCard(CommonConstants.NO);
        generalInfo.setGoalAcct(CommonConstants.NO);
        generalInfo.setInsure(CommonConstants.NO);
        generalInfo.setPbaSurchrge(CommonConstants.NO);
        generalInfo.setCFCL(CommonConstants.NO);
        generalInfo.setImportsCfs("U");
        generalInfo.setAllwaysBillCoload(CommonConstants.NO);
        generalInfo.setAllowLclQuotes(CommonConstants.NO);
        generalInfo.setAllowFclQuotes(CommonConstants.NO);
        generalInfo.setImportTrackingScreen(CommonConstants.NO);
        generalInfo.setActivatePwdQuotes(CommonConstants.NO);
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getImportQuoteColoadRetail())) {
            generalInfo.setImportQuoteColoadRetail(tradingPartnerForm.getImportQuoteColoadRetail());
        }
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getFrieghtFmc())) {
            generalInfo.setFwFmcNo(tradingPartnerForm.getFrieghtFmc());
        }
        new GeneralInformationDAO().save(generalInfo);

        CustomerAccounting arConfig = new CustomerAccounting();
        arConfig.setAccountNo(tradingPartner.getAccountno());
        arConfig.setCreditLimit(0d);
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Credit Status");
        GenericCode creditStatus = new GenericCodeDAO().getGenericCode("No Credit", codeTypeId);
        arConfig.setCreditStatus(creditStatus);
        codeTypeId = new CodetypeDAO().getCodeTypeId("Credit Terms");
        GenericCode creditTerms = new GenericCodeDAO().getGenericCode("Due Upon Receipt", codeTypeId);
        arConfig.setCreditRate(creditTerms);
        if (StringUtils.contains(tradingPartner.getAcctType(), "V")
                && CommonUtils.in(tradingPartner.getSubType(), SUB_TYPE_IMPORT_AGENT, SUB_TYPE_EXPORT_AGENT)) {
            arConfig.setSchedulestmt(null);
            arConfig.setStatements(null);
        } else {
            arConfig.setSchedulestmt("Both");
            codeTypeId = new CodetypeDAO().getCodeTypeId("Statements");
            GenericCode statements = new GenericCodeDAO().getGenericCode("Email", codeTypeId);
            arConfig.setStatements(statements);
        }
        arConfig.setCreditbalance(CommonConstants.NO);
        arConfig.setCreditinvoice(CommonConstants.NO);
        arConfig.setPastDueBuffer(tradingPartnerForm.getPastDueBuffer());
        arConfig.setSendDebitCreditNotes(CommonConstants.NO);
        arConfig.setIncludeagent(CommonConstants.NO);
        arConfig.setExtendCredit(CommonConstants.NO);
        arConfig.setExemptCreditProcess(CommonConstants.NO);

        arConfig.setCreatedOn(new Date());
        arConfig.setCreatedBy(user.getUserId());
        new CustomerAccountingDAO().save(arConfig);
        arConfig.loadCustomerAccountingDetails(arConfig, tradingPartnerForm);

        NotesBC notesBC = new NotesBC();
        Notes note = new Notes();
        note.setModuleId(NotesConstants.TRADINGPARTNER);
        String accountNo = null != tradingPartner.getAccountno() ? tradingPartner.getAccountno() : "";
        note.setModuleRefId(accountNo);
        note.setUpdateDate(new Date());
        note.setUpdatedBy(user.getLoginName());
        note.setNoteType(NotesConstants.AUTO);
        if ((tradingPartner.getEciAccountNo() != null && !tradingPartner.getEciAccountNo().equalsIgnoreCase(""))
                && (tradingPartner.getECIFWNO() != null && !tradingPartner.getECIFWNO().equalsIgnoreCase(""))
                && (tradingPartner.getECIVENDNO() != null && !tradingPartner.getECIVENDNO().equalsIgnoreCase(""))) {
            note.setNoteDesc("New TP Account Opened " + tradingPartner.getAccountName() + "--->" + accountNo + " "
                    + "ECI Shpr/FF#: added --->" + tradingPartner.getEciAccountNo() + " "
                    + "ECI Consignee: added --->" + tradingPartner.getECIFWNO() + " "
                    + "ECI Vendor: added --->" + tradingPartner.getECIVENDNO());
        } else if ((tradingPartner.getEciAccountNo() == null || tradingPartner.getEciAccountNo().equalsIgnoreCase(""))
                && (tradingPartner.getECIFWNO() != null && !tradingPartner.getECIFWNO().equalsIgnoreCase(""))
                && (tradingPartner.getECIVENDNO() != null && !tradingPartner.getECIVENDNO().equalsIgnoreCase(""))) {
            note.setNoteDesc("New TP Account Opened " + tradingPartner.getAccountName() + "--->" + accountNo + " "
                    + "ECI Consignee: added --->" + tradingPartner.getECIFWNO() + " "
                    + "ECI Vendor: added --->" + tradingPartner.getECIVENDNO());
        } else if ((tradingPartner.getEciAccountNo() != null && !tradingPartner.getEciAccountNo().equalsIgnoreCase(""))
                && (tradingPartner.getECIFWNO() == null || tradingPartner.getECIFWNO().equalsIgnoreCase(""))
                && (tradingPartner.getECIVENDNO() != null && !tradingPartner.getECIVENDNO().equalsIgnoreCase(""))) {
            note.setNoteDesc("New TP Account Opened " + tradingPartner.getAccountName() + "--->" + accountNo + " "
                    + "ECI Shpr/FF#: added --->" + tradingPartner.getEciAccountNo() + " "
                    + "ECI Vendor: added --->" + tradingPartner.getECIVENDNO());
        } else if ((tradingPartner.getEciAccountNo() != null && !tradingPartner.getEciAccountNo().equalsIgnoreCase(""))
                && (tradingPartner.getECIFWNO() != null && !tradingPartner.getECIFWNO().equalsIgnoreCase(""))
                && (tradingPartner.getECIVENDNO() == null || tradingPartner.getECIVENDNO().equalsIgnoreCase(""))) {
            note.setNoteDesc("New TP Account Opened " + tradingPartner.getAccountName() + "--->" + accountNo + " "
                    + "ECI Shpr/FF#: added --->" + tradingPartner.getEciAccountNo() + " "
                    + "ECI Consignee: added --->" + tradingPartner.getECIFWNO());
        } else if (tradingPartner.getEciAccountNo() != null && !tradingPartner.getEciAccountNo().equalsIgnoreCase("")) {//shipper
            note.setNoteDesc("New TP Account Opened " + tradingPartner.getAccountName() + "--->" + accountNo + " "
                    + "ECI Shpr/FF#: added --->" + tradingPartner.getEciAccountNo());
        } else if (tradingPartner.getECIFWNO() != null && !tradingPartner.getECIFWNO().equalsIgnoreCase("")) {//consignee
            note.setNoteDesc("New TP Account Opened " + tradingPartner.getAccountName() + "--->" + accountNo + " "
                    + "ECI Consignee: added --->" + tradingPartner.getECIFWNO());
        } else if (tradingPartner.getECIVENDNO() != null && !tradingPartner.getECIVENDNO().equalsIgnoreCase("")) {//vendor
            note.setNoteDesc("New TP Account Opened " + tradingPartner.getAccountName() + "--->" + accountNo + " "
                    + "ECI Vendor: added --->" + tradingPartner.getECIVENDNO());
        } else {
            note.setNoteDesc("New TP Account Opened " + tradingPartner.getAccountName() + "--->" + accountNo);
        }
        notesBC.saveNotes(note);
        return tradingPartner;
    }

    public void setSubType(Object object) throws Exception {
        if (object instanceof CustAddress) {
            CustAddress custAddress = (CustAddress) object;
            if (CommonFunctions.isNotNull(custAddress.getAcctNo())) {
                TradingPartner tradingPartner = findTradingPartnerById(custAddress.getAcctNo());
                custAddress.setSubType(tradingPartner.getSubType());
                custAddress.setSslineNumber(tradingPartner.getSslineNumber());
            }
        }
    }

    public TradingPartner saveConsigneeInfo(TradingPartnerForm tradingPartnerForm) throws Exception {
        TradingPartner tradingPartner = new TradingPartnerDAO().findById(tradingPartnerForm.getTradingPartnerId());
        tradingPartner.setPortNumber(tradingPartnerForm.getPortNumber());
        tradingPartner.setTaxExempt(tradingPartnerForm.getTaxExempt());
        tradingPartner.setFederalId(tradingPartnerForm.getFederalId());
        tradingPartner.setNotifyParty(tradingPartnerForm.getConsigneeNotifyParty());
        tradingPartner.setNotifyPartyAddress(tradingPartnerForm.getNotifyPartyAddress());
        tradingPartner.setNotifyPartyCity(tradingPartnerForm.getNotifyPartyCity());
        tradingPartner.setNotifyPartyCountry(tradingPartnerForm.getNotifyPartyCountry());
        tradingPartner.setNotifyPartyPostalCode(tradingPartnerForm.getNotifyPartyPostalCode());
        tradingPartner.setNotifyPartyState(tradingPartnerForm.getNotifyPartyState());
        tradingPartnerDAO.updating(tradingPartner);
        return tradingPartner;
    }

    public TradingPartner saveCtsInfo(TradingPartnerForm tradingPartnerForm) throws Exception {
        TradingPartner tradingPartner = new TradingPartnerDAO().findById(tradingPartnerForm.getTradingPartnerId());
        tradingPartner.setCtsUID(tradingPartnerForm.getCtsUID());
        tradingPartner.setFuelMarkUp(tradingPartnerForm.getFuelMarkUp());
        tradingPartner.setLineMarkUp(tradingPartnerForm.getLineMarkUp());
        tradingPartner.setMinAmount(tradingPartnerForm.getMinAmount());
        tradingPartner.setFlatFee(tradingPartnerForm.getFlatFee());
        tradingPartnerDAO.updating(tradingPartner);
        return tradingPartner;
    }

    public void existingImpCustomerList(String accountName, String city,
            HttpServletRequest request) throws Exception {
        String unLocCode = tradingPartnerDAO.getUnlocationCodeFromTradingPartner(accountName, city);
        List<TradingPartner> customerList = tradingPartnerDAO.existingNewCustomerList(accountName, unLocCode);
        request.setAttribute("customerList", customerList);
        if (CommonUtils.isEmpty(customerList)) {
            request.setAttribute("noData", true);
        }
    }

    public void existingCustomerList(String accountName, String unLocCode, HttpServletRequest request) throws Exception {
        List<TradingPartner> customerList = tradingPartnerDAO.existingNewCustomerList(accountName, unLocCode);
        request.setAttribute("customerList", customerList);
        if(CommonUtils.isEmpty(customerList)){
            request.setAttribute("noData", true);
        }
    }

    public boolean isAnyOpenInvoices(String disabledAccount) throws Exception {
        return tradingPartnerDAO.isAnyOpenInvoices(disabledAccount);
    }

    public boolean checkApHistory(String disabledAccount) throws Exception {
        return tradingPartnerDAO.checkApHistory(disabledAccount);
    } 

    public boolean checkBlueScreenAccount(String accountNo) throws Exception {
        return tradingPartnerDAO.checkBlueScreenAccount(accountNo);
    }

    public String disableTradingPartner(String acctNo, String forwardAcctNo, String noValidate, HttpServletRequest request) throws Exception {
        String validate = "true".equalsIgnoreCase(noValidate) ? null : tradingPartnerDAO.validateDisabling(acctNo, forwardAcctNo);
        if (CommonUtils.isNotEmpty(validate)) {
            return validate;
        } else {
            tradingPartnerDAO.disableTradingPartner(acctNo, forwardAcctNo, request);
            return "Account is disabled successfully on " + DateUtils.formatDate(new Date(), "dd-MMM-yyyy h:mm a");
        }
    }

    public String enableTradingPartner(String acctNo, String eciShipNo, String eciConsNo, String eciVendorNo, String ssLineNo, String userName, String consUserName, String firmsCode, HttpServletRequest request) throws Exception {
        String validate = checkDuplicateAccountNumbers(acctNo, eciShipNo, eciConsNo, eciVendorNo, ssLineNo);
        if (CommonUtils.isNotEmpty(validate)) {
            return validate;
        } else {
            validate = checkDuplicateUserName(acctNo, userName, consUserName);
            if (CommonUtils.isNotEmpty(validate)) {
                return validate;
            } else {
                validate = checkDuplicateFirmsCode(acctNo, firmsCode);
                if (CommonUtils.isNotEmpty(validate)) {
                    return validate;
                } else {
                    tradingPartnerDAO.enableTradingPartner(acctNo, request);
                    return "Account is enabled successfully on " + DateUtils.formatDate(new Date(), "dd-MMM-yyyy h:mm a");
                }
            }
        }
    }

    public TradingPartner getTradingPartner(String accountno) {
        return tradingPartnerDAO.getTradingPartner(accountno);
    }

    public void showSubsidiaryAccounts(String master, HttpServletRequest request) throws Exception {
        List<TradingPartner> subsidiaryAccounts = tradingPartnerDAO.getSubsidiaryAccounts(master);
        request.setAttribute("subsidiaryAccounts", subsidiaryAccounts);
    }

    public void addSubsidiaryAccount(String accountno, String master, HttpServletRequest request) throws Exception {
        tradingPartnerDAO.addSubsidiaryAccount(accountno, master);
        showSubsidiaryAccounts(master, request);
    }

    public void removeSubsidiaryAccount(String accountno, String master, HttpServletRequest request) throws Exception {
        tradingPartnerDAO.removeSubsidiaryAccount(accountno);
        showSubsidiaryAccounts(master, request);
    }

    public String checkDuplicateAccountNumbers(String acctNo, String eciAcctNo, String eciFwNo, String eciVendorNo, String ssLineNo) {
        String data = new TradingPartnerDAO().checkDuplicateAccountNumbers(acctNo, eciAcctNo, eciFwNo, eciVendorNo, ssLineNo);
        return data != null ? data : "";
    }

    public String checkDuplicateUserName(String acctNo, String userName, String consUserName) {
        String data = new TradingPartnerDAO().checkDuplicateUserName(acctNo, userName, consUserName);
        return data != null ? data : "";
    }

    public String checkDuplicateFirmsCode(String acctNo, String firmsCode) {
        String data = new TradingPartnerDAO().checkDuplicateFirmsCode(acctNo, firmsCode);
        return data != null ? data : "";
    }
}
