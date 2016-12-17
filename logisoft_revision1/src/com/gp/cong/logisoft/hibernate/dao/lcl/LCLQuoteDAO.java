/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.model.AddressDetailsBean;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Administrator
 */
public class LCLQuoteDAO extends BaseHibernateDAO<LclQuote> {

    public LCLQuoteDAO() {
        super(LclQuote.class);
    }

    public List<LclQuote> getAllQuotesByClient(String clientId) throws Exception {

        Criteria cri = getSession().createCriteria(LclQuote.class, "quote");
        cri.createAlias("quote.lclFileNumber", "lclFile");
        cri.createAlias("quote.clientAcct", "client");
        cri.add(Restrictions.eq("lclFile.state", "Q"));
        cri.add(Restrictions.eq("quote.quoteType", "E"));
        cri.add(Restrictions.eq("client.accountno", clientId));
        cri.addOrder(Order.desc("fileNumberId"));
        return cri.list();
    }

    public AddressDetailsBean getTradingAddressDetails(String queryString) throws Exception {
        Query query = getCurrentSession().createSQLQuery(queryString).setMaxResults(1);
        return (AddressDetailsBean) query.uniqueResult();
    }

    public Object[] getAllSpotRateValues(Long fileId) throws Exception {
        String queryString = "SELECT spot_rate,spot_wm_rate,spot_measure_rate,spot_rate_ofrate FROM lcl_quote WHERE  file_number_id=" + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        return (Object[]) queryObject.uniqueResult();
    }

    public String getPickupReadyDate(Long fileId) throws Exception {
        String pickupReadyDate = new String();
        String queryString = "select pickup_ready_date from lcl_quote_pad WHERE  file_number_id=" + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object obj = queryObject.uniqueResult();
        if (obj != null) {
            pickupReadyDate = obj.toString();
        }
        return pickupReadyDate;
    }

    public List<CustomerContact> getListOfLCLPartyDetails(String bookingId) throws Exception {
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        List<CustomerContact> custContactList = new ArrayList<CustomerContact>();
        Map<String, CustomerContact> custContactMap = new HashMap<String, CustomerContact>();
        if (CommonFunctions.isNotNull(bookingId)) {
            LclQuote lclQuote = new LCLQuoteDAO().getByProperty("lclFileNumber.id", Long.parseLong(bookingId));

            if (CommonFunctions.isNotNull(lclQuote.getAgentAcct()) && CommonFunctions.isNotNull(lclQuote.getAgentAcct().getAccountno())) {
                //agent
                tradingPartnerBC.getCustomerContactList(custContactMap, lclQuote.getAgentAcct().getAccountno());
            }
            if (CommonFunctions.isNotNull(lclQuote.getFwdAcct()) && CommonFunctions.isNotNull(lclQuote.getFwdAcct().getAccountno())) {
                //Forwarder
                tradingPartnerBC.getCustomerContactList(custContactMap, lclQuote.getFwdAcct().getAccountno());
            }
            if (CommonFunctions.isNotNull(lclQuote.getShipAcct()) && CommonFunctions.isNotNull(lclQuote.getShipAcct().getAccountno())) {
                //Shipper
                tradingPartnerBC.getCustomerContactList(custContactMap, lclQuote.getShipAcct().getAccountno());
            }
            if (CommonFunctions.isNotNull(lclQuote.getClientAcct()) && CommonFunctions.isNotNull(lclQuote.getClientAcct().getAccountno())) {
                //ThridParty
                tradingPartnerBC.getCustomerContactList(custContactMap, lclQuote.getClientAcct().getAccountno());
            }
            if (CommonFunctions.isNotNull(lclQuote.getConsAcct()) && CommonFunctions.isNotNull(lclQuote.getConsAcct().getAccountno())) {
                //conginee
                tradingPartnerBC.getCustomerContactList(custContactMap, lclQuote.getConsAcct().getAccountno());
            }
            Set<String> set = custContactMap.keySet();
            for (String keyValue : set) {
                custContactList.add(custContactMap.get(keyValue));
            }
            //Order contact by First name
            custContactMap = new HashMap<String, CustomerContact>();
            for (CustomerContact contact : custContactList) {
                custContactMap.put(contact.getAccountName() + "-" + contact.getFirstName() + "-" + contact.getEmail(), contact);
            }
            TreeSet<String> keys = new TreeSet<String>(custContactMap.keySet());
            custContactList = new ArrayList<CustomerContact>();
            for (String key : keys) {
                custContactList.add(custContactMap.get(key));
            }
        }
        return custContactList;
    }

    public List<CustomerContact> getCodeJContactList(String bol) throws Exception {
        List<CustomerContact> list = getListOfLCLPartyDetails(bol);
        List<CustomerContact> contactList = new ArrayList<CustomerContact>();
        for (CustomerContact contact : list) {
            if (null != contact.getCodej() && CommonUtils.isNotEmpty(contact.getCodej().getCode())) {
                contactList.add(contact);
            }
        }
        return contactList;
    }

    public void updateBillToParty(Long fileId, String billingType, String billToParty, String fieldName, String acctNo, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_quote set billing_type = '");
        queryBuilder.append(billingType);
        queryBuilder.append("',bill_to_party = '").append(billToParty).append("'");
        if (!fieldName.equals("")) {
            queryBuilder.append(",").append(fieldName).append(" = '").append(acctNo).append("'");

        }
        queryBuilder.append(",modified_by_user_id = ").append(userId);
        queryBuilder.append(",modified_datetime = SYSDATE() where file_number_id = ");
        queryBuilder.append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public List getQTCommodityNumber(String fileId) throws Exception {
        Query object = getCurrentSession().createSQLQuery("SELECT c.`code` FROM lcl_quote_piece b JOIN commodity_type c ON "
                + "c.`id` = b.`commodity_type_id` WHERE b.`file_number_id`=:fileId ");
        object.setString("fileId", fileId);
        return object.list();
    }

    public boolean checkEculineCommInExport(String comm, String unloc, String fileId) throws Exception {
        List commNo = new ArrayList();
        if (!"".equalsIgnoreCase(fileId)) {
            commNo = getQTCommodityNumber(fileId);
            commNo.add(comm);
        } else {
            commNo.add(comm);
        }
        boolean flag = false;
        String eciDBName = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ofcom.`ecucom` AS CODE   FROM ").append(eciDBName).append(" .`ofcomm` ofcom  WHERE ofcom.`comcde` IN (:comm) ");
        sb.append(" union SELECT ofcel.`eblyn`  AS CODE  FROM ").append(eciDBName).append(".`ofcebl` ofcel JOIN ports p ON p.`eciportcode` = ofcel.`prtnum` ");
        sb.append(" AND  p.`unlocationcode` =:unloc WHERE ofcel.`comcde` IN (:comm) ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setString("unloc", unloc);
        queryObject.setParameterList("comm", commNo);
        List li = queryObject.list();
        for (Object obj : li) {
            if (obj != null && obj.toString().equalsIgnoreCase("Y")) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }
    
     public void updateCIF(Long fileId,String CIF) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append ("update lcl_quote set insurance_cif = '").append(CIF).append("' where file_number_id= '").append(fileId).append("'");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
            }
     
     public boolean isClassOfcom(String fileId,String comm,String oldComm) throws Exception {
        boolean flag = false;
         List commNo = new ArrayList();
        if (!"".equalsIgnoreCase(fileId)) {
            commNo = getCommodityNumber(fileId);
            commNo.add(comm);
        } else {
            commNo.add(comm);
        }
        String eciDBName = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ofcom.`class` AS CODE FROM ").append(eciDBName).append(" .`ofcomm` ofcom  WHERE ofcom.`comcde` IN (:comm) and ofcom.`comcde`!=(:oldComm)");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameterList("comm", commNo);
        queryObject.setString("oldComm", oldComm);
        List li = queryObject.list();
        if(li.contains("Y") || li.contains("1")){
        flag = true;
        }else{
        for (Object obj : li) {
            if (obj != null && (obj.toString().equalsIgnoreCase("Y") || obj.toString().equals("1"))) {
                flag = true;
            } else {
                flag = false;
            }
        }
        }
        return flag;
    }
     
      public List getCommodityNumber(String fileId) throws Exception {
        Query object = getCurrentSession().createSQLQuery("SELECT c.`code` FROM lcl_quote_piece b JOIN commodity_type c ON "
                + "c.`id` = b.`commodity_type_id` WHERE b.`file_number_id`=:fileId ");
        object.setString("fileId", fileId);
        return object.list();
    }
}
