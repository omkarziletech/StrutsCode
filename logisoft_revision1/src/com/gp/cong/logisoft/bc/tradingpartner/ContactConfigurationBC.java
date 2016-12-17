package com.gp.cong.logisoft.bc.tradingpartner;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;

public class ContactConfigurationBC implements TradingPartnerConstants {

    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
    CustomerContactDAO customerContactDAO = new CustomerContactDAO();

    public TradingPartner saveContactDetails(TradingPartnerForm tradingPartnerForm, User loginUser) throws Exception {
        CustomerContact customerContact = new CustomerContact(tradingPartnerForm);
        if (tradingPartnerForm.getCodea() != null && !tradingPartnerForm.getCodea().equals("")) {
            GenericCode codeA = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodea(), "A");
            customerContact.setCodea(codeA);
        }
        if (tradingPartnerForm.getCodeb() != null && !tradingPartnerForm.getCodeb().equals("")) {
            GenericCode codeB = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodeb(), "B");
            customerContact.setCodeb(codeB);
        }
        if (tradingPartnerForm.getCodec() != null && !tradingPartnerForm.getCodec().equals("")) {
            GenericCode codeC = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodec(), "C");
            customerContact.setCodec(codeC);
        }
        if (tradingPartnerForm.getCoded() != null && !tradingPartnerForm.getCoded().equals("")) {
            GenericCode codeD = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCoded(), "D");
            customerContact.setCoded(codeD);
        }
        if (tradingPartnerForm.getCodee() != null && !tradingPartnerForm.getCodee().equals("")) {
            GenericCode codeE = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodee(), "E");
            customerContact.setCodee(codeE);
        }
        if (tradingPartnerForm.getCodef() != null && !tradingPartnerForm.getCodef().equals("")) {
            GenericCode codeF = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodef(), "F");
            customerContact.setCodef(codeF);
        }
        if (tradingPartnerForm.getCodeg() != null && !tradingPartnerForm.getCodeg().equals("")) {
            GenericCode codeG = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodeg(), "G");
            customerContact.setCodeg(codeG);
        }
        if (tradingPartnerForm.getCodeh() != null && !tradingPartnerForm.getCodeh().equals("")) {
            GenericCode codeH = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodeh(), "H");
            customerContact.setCodeh(codeH);
        }
        if (tradingPartnerForm.getCodei() != null && !tradingPartnerForm.getCodei().equals("")) {
            GenericCode codeI = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodei(), "I");
            customerContact.setCodei(codeI);
        }
        if (tradingPartnerForm.getCodej() != null && !tradingPartnerForm.getCodej().equals("")) {
            GenericCode codeJ = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodej(), "J");
            customerContact.setCodej(codeJ);
        }
        if (tradingPartnerForm.getCodek() != null && !tradingPartnerForm.getCodek().equals("")) {
            GenericCode codeK = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodek(), "K");
            customerContact.setCodek(codeK);
        }
        customerContact.setCreatedDate(new Date());
        customerContact.setUpdatedDate(new Date());
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        if (tradingPartner != null) {
            customerContact.setAccountNo(tradingPartnerForm.getTradingPartnerId());
            customerContact.setUpdateBy(loginUser.getLoginName());
            tradingPartner.getCustomerContact().add(customerContact);
        }
        CustomerAccountingDAO arConfigDAO = new CustomerAccountingDAO();
        if (tradingPartner != null && null != tradingPartner.getAccountno()) {
            CustomerAccounting arConfig = arConfigDAO.findByProperty("accountNo", tradingPartner.getAccountno());
            if (null != arConfig
                    && CommonUtils.isEmpty(arConfig.getAcctRecEmail()) && CommonUtils.isEmpty(arConfig.getArFax())
                    && (CommonUtils.isNotEmpty(customerContact.getEmail()) || CommonUtils.isNotEmpty(customerContact.getFax()))) {
                StringBuilder contact = new StringBuilder();
                boolean noFirstName = true;
                if (CommonUtils.isNotEmpty(customerContact.getFirstName())) {
                    contact.append(customerContact.getFirstName());
                    noFirstName = false;
                }
                if (CommonUtils.isNotEmpty(customerContact.getLastName())) {
                    contact.append(noFirstName ? "" : " ").append(customerContact.getLastName());
                }
                arConfig.setContact(contact.toString());
                arConfig.setArPhone(customerContact.getPhone());
                arConfig.setAcctRecEmail(customerContact.getEmail());
                arConfig.setArFax(customerContact.getFax());
                if (StringUtils.contains(tradingPartner.getAcctType(), "V")
                        && CommonUtils.in(tradingPartner.getSubType(), SUB_TYPE_IMPORT_AGENT, SUB_TYPE_EXPORT_AGENT)) {
                    arConfig.setSchedulestmt(null);
                    arConfig.setStatements(null);
                } else {
                    Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Statements");
                    if (CommonUtils.isNotEmpty(customerContact.getEmail())) {
                        GenericCode statements = new GenericCodeDAO().getGenericCode("Email", codeTypeId);
                        arConfig.setStatements(statements);
                    } else if (CommonUtils.isNotEmpty(customerContact.getFax())) {
                        GenericCode statements = new GenericCodeDAO().getGenericCode("Fax", codeTypeId);
                        arConfig.setStatements(statements);
                    }
                }
                arConfigDAO.update(arConfig);
            }
        }
        tradingPartnerDAO.updating(tradingPartner);
        return tradingPartner;
    }

    /**
     * @param tradingPartnerForm
     * @return
     */
    public CustomerContact findEachCustomerRecord(TradingPartnerForm tradingPartnerForm) throws Exception {
        CustomerContact customerContact = customerContactDAO.findById(new Integer(tradingPartnerForm.getIndex()));
        return customerContact;
    }

    /**
     * @param tradingPartnerForm
     * @return
     */
    public TradingPartner getCustomerDetails(TradingPartnerForm tradingPartnerForm) throws Exception {
        TradingPartner tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
        return tradingPartner;
    }

    public TradingPartnerForm findCustomerRecordForSelect(TradingPartnerForm tradingPartnerForm) throws Exception {
        CustomerContact customerContact = customerContactDAO.findById(new Integer(tradingPartnerForm.getIndex()));
        if (customerContact != null) {
            tradingPartnerForm = customerContact.setValuesToForm(customerContact, tradingPartnerForm);
        }
        return tradingPartnerForm;
    }

    /**
     * @param tradingPartnerForm
     * @return
     */
    public TradingPartner updateContactDetails(TradingPartnerForm tradingPartnerForm, User loginUser) throws Exception {
        TradingPartner tradingPartner = new TradingPartner();
        if (CommonFunctions.isNotNull(tradingPartnerForm.getTradingPartnerId())) {
            tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
            if (tradingPartner.getCustomerContact() != null) {
                Hibernate.initialize(tradingPartner.getCustomerContact());
            }
            if (tradingPartnerForm.getTradingPartnerId() != null && null != tradingPartnerForm.getIndex() && !tradingPartnerForm.getIndex().equals("")) {
                CustomerContact customerContactNewObj = new CustomerContactDAO().findById(Integer.parseInt(tradingPartnerForm.getIndex()));

                customerContactNewObj.setFirstName(null != tradingPartnerForm.getFirstName() ? tradingPartnerForm.getFirstName().toUpperCase() : tradingPartnerForm.getFirstName());
                customerContactNewObj.setLastName(null != tradingPartnerForm.getLastName() ? tradingPartnerForm.getLastName().toUpperCase() : tradingPartnerForm.getLastName());
                customerContactNewObj.setPosition(null != tradingPartnerForm.getPosition() ? tradingPartnerForm.getPosition().toUpperCase() : tradingPartnerForm.getPosition());
                customerContactNewObj.setPhone(null != tradingPartnerForm.getPhone() ? tradingPartnerForm.getPhone().toUpperCase() : tradingPartnerForm.getPhone());
                customerContactNewObj.setFax(null != tradingPartnerForm.getFax() ? tradingPartnerForm.getFax().toUpperCase() : tradingPartnerForm.getFax());
                customerContactNewObj.setEmail(null != tradingPartnerForm.getEmail() ? tradingPartnerForm.getEmail().toUpperCase() : tradingPartnerForm.getEmail());
                customerContactNewObj.setComment(null != tradingPartnerForm.getComment() ? tradingPartnerForm.getComment().toUpperCase() : tradingPartnerForm.getComment());
                customerContactNewObj.setExtension(null != tradingPartnerForm.getExtension() ? tradingPartnerForm.getExtension().toUpperCase() : tradingPartnerForm.getExtension());

                if (tradingPartnerForm.getCodea() != null && !tradingPartnerForm.getCodea().equals("")) {
                    GenericCode codeA = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodea(), "A");
                    customerContactNewObj.setCodea(codeA);
                } else {
                    customerContactNewObj.setCodea(null);
                }
                if (tradingPartnerForm.getCodeb() != null && !tradingPartnerForm.getCodeb().equals("")) {
                    GenericCode codeB = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodeb(), "B");
                    customerContactNewObj.setCodeb(codeB);
                } else {
                    customerContactNewObj.setCodeb(null);
                }
                if (tradingPartnerForm.getCodec() != null && !tradingPartnerForm.getCodec().equals("")) {
                    GenericCode codeC = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodec(), "C");
                    customerContactNewObj.setCodec(codeC);
                } else {
                    customerContactNewObj.setCodec(null);
                }
                if (tradingPartnerForm.getCoded() != null && !tradingPartnerForm.getCoded().equals("")) {
                    GenericCode codeD = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCoded(), "D");
                    customerContactNewObj.setCoded(codeD);
                } else {
                    customerContactNewObj.setCoded(null);
                }
                if (tradingPartnerForm.getCodee() != null && !tradingPartnerForm.getCodee().equals("")) {
                    GenericCode codeE = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodee(), "E");
                    customerContactNewObj.setCodee(codeE);
                } else {
                    customerContactNewObj.setCodee(null);
                }
                if (tradingPartnerForm.getCodef() != null && !tradingPartnerForm.getCodef().equals("")) {
                    GenericCode codeF = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodef(), "F");
                    customerContactNewObj.setCodef(codeF);
                } else {
                    customerContactNewObj.setCodef(null);
                }
                if (tradingPartnerForm.getCodeg() != null && !tradingPartnerForm.getCodeg().equals("")) {
                    GenericCode codeG = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodeg(), "G");
                    customerContactNewObj.setCodeg(codeG);
                } else {
                    customerContactNewObj.setCodeg(null);
                }
                if (tradingPartnerForm.getCodeh() != null && !tradingPartnerForm.getCodeh().equals("")) {
                    GenericCode codeH = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodeh(), "H");
                    customerContactNewObj.setCodeh(codeH);
                } else {
                    customerContactNewObj.setCodeh(null);
                }
                if (tradingPartnerForm.getCodei() != null && !tradingPartnerForm.getCodei().equals("")) {
                    GenericCode codeI = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodei(), "I");
                    customerContactNewObj.setCodei(codeI);
                } else {
                    customerContactNewObj.setCodei(null);
                }
                if (tradingPartnerForm.getCodej() != null && !tradingPartnerForm.getCodej().equals("")) {
                    GenericCode codeJ = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodej(), "J");
                    customerContactNewObj.setCodej(codeJ);
                } else {
                    customerContactNewObj.setCodej(null);
                }
                if (tradingPartnerForm.getCodek() != null && !tradingPartnerForm.getCodek().equals("")) {
                    GenericCode codeK = genericCodeDAO.getSpecialCode(tradingPartnerForm.getCodek(), "K");
                    customerContactNewObj.setCodek(codeK);
                } else {
                    customerContactNewObj.setCodek(null);
                }
                if (null != loginUser) {
                    customerContactNewObj.setUpdateBy(loginUser.getLoginName());
                }
                customerContactNewObj.setAccountNo(tradingPartner.getAccountno());
//                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
                customerContactNewObj.setUpdatedDate(new Date());
                tradingPartnerDAO.updating(tradingPartner);
                tradingPartner = tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
            }
        }
        return tradingPartner;
    }

    public TradingPartner deleteContactDetails(TradingPartnerForm tradingPartnerForm,User loginUser) throws Exception {
        CustomerContact customerContact = customerContactDAO.findById(Integer.parseInt(tradingPartnerForm.getIndex()));
        customerContact.setUpdateBy(loginUser.getLoginName());
        customerContact.setUpdatedDate(new Date());
        customerContactDAO.saveOrUpdate(customerContact);
        customerContactDAO.getCurrentSession().flush();
        customerContactDAO.delete(customerContact);
        customerContactDAO.getCurrentSession().flush();
        return tradingPartnerDAO.findById(tradingPartnerForm.getTradingPartnerId());
    }
}
