/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.model.AddressDetailsBean;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.beans.LclInlandVoyageInfoBean;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.lcl.report.ConsolidationMiniManifestBean;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.ReleaseReportBean;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Owner
 */
public class LCLBookingDAO extends BaseHibernateDAO<LclBooking> {

    private static final Logger log = Logger.getLogger(LCLBookingDAO.class);

    public LCLBookingDAO() {
        super(LclBooking.class);
    }

    public List<LclBooking> getAllBookingsByClient(String clientId) throws Exception {

        Criteria cri = getSession().createCriteria(LclBooking.class, "booking");
        cri.createAlias("booking.clientAcct", "client");
        cri.addOrder(Order.desc("fileNumberId"));
        cri.add(Restrictions.eq("booking.bookingType", "E"));
        return cri.add(Restrictions.eq("client.accountno", clientId)).list();
    }

    public AddressDetailsBean getTradingAddressDetails(String queryString) throws Exception {
        AddressDetailsBean ad = null;
        Query query = getCurrentSession().createSQLQuery(queryString);
        List list = query.list();
        if (!list.isEmpty()) {
            Object row = list.get(0);
            ad = new AddressDetailsBean((Object[]) row);
        }
        return ad;
    }

    public Object[] getAllSpotRateValues(Long fileId) throws Exception {
        String queryString = "SELECT spot_rate,spot_wm_rate,spot_measure_rate,spot_rate_ofrate FROM lcl_booking WHERE  file_number_id=" + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        return (Object[]) queryObject.uniqueResult();
    }

    public String getPickupReadyDate(Long fileId) throws Exception {
        String pickupReadyDate = new String();
        String queryString = "select pickup_ready_date from lcl_booking_pad WHERE  file_number_id=" + fileId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object obj = queryObject.uniqueResult();
        if (obj != null) {
            pickupReadyDate = obj.toString();
            //DateUtils.formatDateAndParseToDate((Date)obj.toString());
        }
        return pickupReadyDate;
    }

    public List<CustomerContact> getListOfLCLPartyDetails(String bookingId) throws Exception {
        TradingPartnerBC tradingPartnerBC = new TradingPartnerBC();
        List<CustomerContact> custContactList = new ArrayList<CustomerContact>();
        Map<String, CustomerContact> custContactMap = new HashMap<String, CustomerContact>();
        if (CommonFunctions.isNotNull(bookingId)) {
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(bookingId));
            new LCLBookingDAO().getCurrentSession().evict(lclBooking);
            if (!"E".equalsIgnoreCase(lclBooking.getBookingType()) && CommonFunctions.isNotNull(lclBooking.getSupAcct())
                    && CommonFunctions.isNotNull(lclBooking.getSupAcct().getAccountno())) {
                tradingPartnerBC.getCustomerContactList(custContactMap, lclBooking.getSupAcct().getAccountno());
            } else if ("E".equalsIgnoreCase(lclBooking.getBookingType())) {
                if (CommonFunctions.isNotNull(lclBooking.getAgentAcct()) && CommonFunctions.isNotNull(lclBooking.getAgentAcct().getAccountno())) {
                    //agent
                    tradingPartnerBC.getCustomerContactList(custContactMap, lclBooking.getAgentAcct().getAccountno());
                }
                if (CommonFunctions.isNotNull(lclBooking.getFwdAcct()) && CommonFunctions.isNotNull(lclBooking.getFwdAcct().getAccountno())) {
                    //Forwarder
                    tradingPartnerBC.getCustomerContactList(custContactMap, lclBooking.getFwdAcct().getAccountno());
                }
            }
            if (CommonFunctions.isNotNull(lclBooking.getShipAcct()) && CommonFunctions.isNotNull(lclBooking.getShipAcct().getAccountno())) {
                //Shipper
                tradingPartnerBC.getCustomerContactList(custContactMap, lclBooking.getShipAcct().getAccountno());
            }
            if (CommonFunctions.isNotNull(lclBooking.getClientAcct()) && CommonFunctions.isNotNull(lclBooking.getClientAcct().getAccountno())) {
                //ThridParty
                tradingPartnerBC.getCustomerContactList(custContactMap, lclBooking.getClientAcct().getAccountno());
            }
            if (CommonFunctions.isNotNull(lclBooking.getConsAcct()) && CommonFunctions.isNotNull(lclBooking.getConsAcct().getAccountno())) {
                //conginee
                tradingPartnerBC.getCustomerContactList(custContactMap, lclBooking.getConsAcct().getAccountno());
            }
            if (CommonFunctions.isNotNull(lclBooking.getNotyAcct()) && CommonFunctions.isNotNull(lclBooking.getNotyAcct().getAccountno())) {
                //notify
                tradingPartnerBC.getCustomerContactList(custContactMap, lclBooking.getNotyAcct().getAccountno());
            }
            if (CommonFunctions.isNotNull(lclBooking.getNotify2Contact()) && CommonFunctions.isNotNull(lclBooking.getNotify2Contact().getId())
                    && CommonFunctions.isNotNull(lclBooking.getNotify2Contact().getTradingPartner()) && CommonFunctions.isNotNull(lclBooking.getNotify2Contact().getTradingPartner().getAccountno())) {
                tradingPartnerBC.getCustomerContactList(custContactMap, lclBooking.getNotify2Contact().getTradingPartner().getAccountno());                //notify2
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

    public List<CustomerContact> getCodeFContactList(String bol) throws Exception {
        List<CustomerContact> list = getListOfLCLPartyDetails(bol);
        List<CustomerContact> contactList = new ArrayList<CustomerContact>();
        for (CustomerContact contact : list) {
            if (null != contact.getCodef() && CommonUtils.isNotEmpty(contact.getCodef().getCode())) {
                contactList.add(contact);
            }
        }
        return contactList;
    }

    public List<CustomerContact> getCodeFEmailContactList(String bol) throws Exception {
        List<CustomerContact> list = getListOfLCLPartyDetails(bol);
        List<CustomerContact> contactList = new ArrayList<CustomerContact>();
        for (CustomerContact contact : list) {
            if (null != contact.getCodef() && contact.getCodef().getCode().contains("E")) {
                contactList.add(contact);
            }
        }
        return contactList;
    }

    public void updateBillToParty(Long fileId, String billingType, String billToParty, String fieldName, String acctNo, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking set billing_type = '");
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

    public BigInteger checkDisposition(Long fileId, int dispoId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT id FROM lcl_booking_dispo WHERE file_number_id=").append(fileId).append(" AND disposition_id=").append(dispoId).append(" limit 1");
        Object checkDisposition = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != checkDisposition ? (BigInteger) checkDisposition : null;
    }

    public List<LclInlandVoyageInfoBean> getInlandVoyageInfo(String fileId) throws Exception {
        List<LclInlandVoyageInfoBean> inlandVoyList = new ArrayList<LclInlandVoyageInfoBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lcl.scheduleNo,lcl.sailDate,lcl.ssLine,lcl.vesselName,lcl.ssVoy,lcl.departure,lcl.unitNo,lcl.size FROM ");
        queryBuilder.append("(SELECT lsh.schedule_no AS scheduleNo,lsd.std AS sailDate,tp.acct_name AS ssLine,");
        queryBuilder.append("lsd.sp_reference_name AS vesselName,lsd.sp_reference_no AS ssVoy,un.un_loc_name AS departure,");
        queryBuilder.append("lu.unit_no AS unitNo,ut.description AS size FROM lcl_file_number lfn ");
        queryBuilder.append("LEFT JOIN lcl_booking_piece lbp ON lfn.id = lbp.file_number_id ");
        queryBuilder.append("LEFT JOIN lcl_booking_piece_unit lbpu ON lbp.id = lbpu.booking_piece_id ");
        queryBuilder.append("LEFT JOIN lcl_unit_ss lus ON lbpu.lcl_unit_ss_id = lus.id ");
        queryBuilder.append("LEFT JOIN lcl_unit lu ON lus.unit_id =lu.id ");
        queryBuilder.append("LEFT JOIN unit_type ut ON lu.unit_type_id=ut.id ");
        queryBuilder.append("LEFT JOIN lcl_ss_header lsh ON lus.ss_header_id = lsh.id ");
        queryBuilder.append("LEFT JOIN lcl_ss_detail lsd ON lsh.id = lsd.ss_header_id ");
        queryBuilder.append("LEFT JOIN trading_partner tp ON lsd.sp_acct_no = tp.acct_no ");
        queryBuilder.append("LEFT JOIN un_location un ON lsd.departure_id = un.id WHERE lsd.trans_mode = 'T' AND lus.status = 'C' ");
        queryBuilder.append("AND lfn.id =").append(fileId).append(" )lcl");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclInlandVoyageInfoBean.class));
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        List li = queryObject.list();
        for (Object obj : li) {
            Object[] row = (Object[]) obj;
            LclInlandVoyageInfoBean inlandVoy = new LclInlandVoyageInfoBean(row);
            inlandVoyList.add(inlandVoy);
        }
        return inlandVoyList;
    }

    public String bkgIsLoadedInExport(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(id USING utf8) as fileId FROM ");
        sb.append("lcl_file_number  where id=").append(fileId).append(" AND status IN ('R','L')");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String getCustomerByFileIdImports(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(book.bill_to_party = 'A',agent.acct_no,IF(book.bill_to_party = 'C',cons.acct_no,IF(book.bill_to_party = 'N',notify.acct_no,");
        sb.append("tprty.acct_no ))) AS customerNumber FROM lcl_file_number FILE JOIN lcl_booking book  ON file.id = book.file_number_id LEFT JOIN ");
        sb.append("trading_partner tprty ON book.third_party_acct_no = tprty.acct_no LEFT JOIN trading_partner agent ON book.sup_acct_no = agent.acct_no ");
        sb.append("LEFT JOIN trading_partner notify ON book.noty_acct_no = notify.acct_no LEFT JOIN trading_partner cons ON book.cons_acct_no = cons.acct_no ");
        sb.append(" WHERE file.id = ");
        sb.append(fileId);
        sb.append(" GROUP BY file.id");
        return (String) getSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public Map<String, String> getAllDrEmailsByUnit(String emailType, String faxType, boolean isVoyage, Long unitSsId) throws Exception {
        Map<String, String> emailMap = new HashMap<String, String>();
        List<ImportsManifestBean> emaiE1F1lList = new LclUnitSsDAO().getAllDrEmailsByUnit(emailType, faxType, isVoyage, unitSsId);
        if (emaiE1F1lList != null) {
            for (ImportsManifestBean emailValues : emaiE1F1lList) {
                if (emailValues.getShipEmail() != null) {
                    emailMap.put(emailValues.getShipAcct(), emailValues.getShipEmail());
                }
                if (emailValues.getNotifyEmail() != null) {
                    emailMap.put(emailValues.getNotifyAcct(), emailValues.getNotifyEmail());
                }
                if (emailValues.getConsEmail() != null) {
                    emailMap.put(emailValues.getConsAcct(), emailValues.getConsEmail());
                }
            }
        }
        return emailMap;
    }

    public void updateModifiedDateTime(Long fileId, Integer userId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE lcl_booking  lb SET lb.modified_datetime = SYSDATE(),lb.modified_by_user_id=").append(userId).append(" WHERE  lb.file_number_id= ").append(fileId);
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void deleteBkgByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBooking where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public List<Object> getExpiredFileNumberList(String expiryCount) throws Exception {
        SQLQuery query = getSession().createSQLQuery("select f.id from lcl_file_number f JOIN lcl_booking b ON f.id=b.file_number_id"
                + " WHERE DATEDIFF(SYSDATE(),b.entered_datetime) >= :expiryCount"
                + " AND b.booking_type='E' AND f.state='B' AND f.status='B'");
        query.setParameter("expiryCount", expiryCount);
        return query.list();
    }

    public String getTerminalNumberByFileNumber(String fileNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select billing_terminal from lcl_booking lb");
        queryBuilder.append(" join lcl_file_number lf on lf.id = lb.file_number_id");
        queryBuilder.append(" where lf.file_number = '").append(fileNumber).append("'");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
    }

    public String checkAcctNoAvailable(String fileId, String acctType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String returnValue = null;
        if (acctType.equalsIgnoreCase("A")) {
            returnValue = "agent_acct_no";
        } else if (acctType.equalsIgnoreCase("C")) {
            returnValue = "cons_acct_no";
        } else if (acctType.equalsIgnoreCase("T")) {
            returnValue = "third_party_acct_no";
        } else if (acctType.equalsIgnoreCase("N")) {
            returnValue = "noty_acct_no";
        }
        queryBuilder.append("select ").append(returnValue).append("  from lcl_booking where ").append("  file_number_id =").append(fileId);
        queryBuilder.append("  and  ").append(returnValue).append(" is not null ").append(" and ").append(returnValue).append(" <> '' ");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getLogoStatus(String originAgentAcct) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  tp.`ecu_logo` ");
        sb.append("FROM");
        sb.append("  `trading_partner` tp ");
        sb.append("WHERE tp.`acct_no` = :originAgentAcct");
        sb.append(" LIMIT 1 ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setString("originAgentAcct", originAgentAcct);
        return (String) queryObject.uniqueResult();
    }

    public List getCommodityNumber(String fileId) throws Exception {
        Query object = getCurrentSession().createSQLQuery("SELECT c.`code` FROM lcl_booking_piece b JOIN commodity_type c ON "
                + "c.`id` = b.`commodity_type_id` WHERE b.`file_number_id`=:fileId ");
        object.setString("fileId", fileId);
        return object.list();
    }

    public boolean checkEculineCommInExport(String comm, String unloc, String fileId) throws Exception {
        List commNo = new ArrayList();
        if (!"".equalsIgnoreCase(fileId)) {
            commNo = getCommodityNumber(fileId);
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

    public String updateTerminal(String trmnum, String fileNumberId, HttpServletRequest request) {
        String blNumber = "";
        try {
            StringBuilder query = new StringBuilder();
            User user = (User) request.getSession().getAttribute("loginuser");
            query.append("update lcl_booking set billing_terminal =:trmnum,modified_by_user_id=:user where file_number_id =:fileNumberId");
            SQLQuery queryObject = getSession().createSQLQuery(query.toString());
            queryObject.setParameter("trmnum", trmnum);
            queryObject.setParameter("fileNumberId", fileNumberId);
            queryObject.setParameter("user", user.getUserId());
            queryObject.executeUpdate();
            getCurrentSession().flush();
            query = new StringBuilder();
            query.append("update lcl_bl set billing_terminal=:trmnum,modified_by_user_id=:user,modified_datetime = SYSDATE() where file_number_id=:fileId");
            queryObject = getSession().createSQLQuery(query.toString());
            queryObject.setParameter("trmnum", trmnum);
            queryObject.setParameter("fileId", fileNumberId);
            queryObject.setParameter("user", user.getUserId());
            queryObject.executeUpdate();
        } catch (Exception exec) {
            log.info("Error in Update Terminal  method. " + new Date() + " for ", exec);
        } finally {
            try {
                String fileNo = new LclFileNumberDAO().getFileNumberByFileId(fileNumberId);
                blNumber = new LCLBlDAO().getExportBlNumbering(fileNumberId);
                String concantedblNumber = blNumber.substring(5);
                new TransactionDAO().updateLclEBlNumber(concantedblNumber, fileNo);
                new TransactionLedgerDAO().updateLclEBlNumber(concantedblNumber, fileNo);
                new ArTransactionHistoryDAO().updateLclEBlNumber(concantedblNumber, fileNo);
            } catch (Exception exec) {
                log.info("Error in Update BlNumber in Transaction . " + new Date() + " for ", exec);
            }
        }
        return blNumber;
    }

    public String[] getHotCodePoa(String fileId) throws Exception {
        String[] bkgDetails = new String[3];
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT le.aes,lb.cons_acct_no,lb.ship_acct_no FROM lcl_booking lb ");
        queryStr.append(" JOIN lcl_booking_export le ON le.file_number_id=lb.file_number_id ");
        queryStr.append(" WHERE lb.file_number_id=:fileId ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("fileId", fileId);
        List bkgDetailsList = queryObject.list();
        if (bkgDetailsList != null && bkgDetailsList.size() > 0) {
            Object[] fdRemarksObj = (Object[]) bkgDetailsList.get(0);
            if (fdRemarksObj[0] != null && !fdRemarksObj[0].toString().trim().equals("")) {
                bkgDetails[0] = fdRemarksObj[0].toString();
            } else {
                bkgDetails[0] = "";
            }
            if (fdRemarksObj[1] != null && !fdRemarksObj[1].toString().trim().equals("")) {
                bkgDetails[1] = fdRemarksObj[1].toString();
            } else {
                bkgDetails[1] = "";
            }
            if (fdRemarksObj[2] != null && !fdRemarksObj[2].toString().trim().equals("")) {
                bkgDetails[2] = fdRemarksObj[2].toString();
            } else {
                bkgDetails[2] = "";
            }
        }
        return (String[]) bkgDetails;
    }

    public List<LclInlandVoyageInfoBean> getPickedVoyageInfo(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT fn.unitSsId AS unitSsId , lsh.id AS headerId , lsh.`schedule_no` AS scheduleNo, lu.unit_no AS unitNo, ");
        sb.append(" UnLocationGetCodeByID(lsh.`origin_id`) AS origin , UnLocationGetCodeByID(lsh.`destination_id`) AS destination ");
        sb.append(" FROM ( SELECT DISTINCT lbu.`lcl_unit_ss_id`  AS unitSsId FROM  lcl_file_number lfn  ");
        sb.append(" JOIN lcl_booking_piece lbp ON lbp.`file_number_id` = lfn.`id`  ");
        sb.append(" JOIN lcl_booking_piece_unit lbu ON lbu.`booking_piece_id` =   ");
        sb.append(" (SELECT lb.id FROM lcl_booking_piece  lb WHERE lb.file_number_id = lfn.`id`  ORDER BY lb.id ASC LIMIT 1) ");
        sb.append(" WHERE lfn.id = :fileId ) fn  JOIN  lcl_unit_ss  lus ON lus.`id`  = fn.unitSsId ");
        sb.append(" JOIN lcl_unit lu ON lu.`id` = lus.`unit_id`  JOIN unit_type ut ON ut.`id` = lu.`unit_type_id` ");
        sb.append(" JOIN lcl_ss_header lsh ON lsh.`id` = lus.`ss_header_id` and lsh.service_type = 'N'");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(LclInlandVoyageInfoBean.class));
        query.addScalar("unitSsId", LongType.INSTANCE);
        query.addScalar("headerId", LongType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("origin", StringType.INSTANCE);
        query.addScalar("destination", StringType.INSTANCE);
        return query.list();
    }

    public String[] isValidatePoa(String fileId, String constAcctNo, String shipAcctNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        if (!"".equalsIgnoreCase(constAcctNo)) {
            queryStr.append("  SELECT IF(poa <> '' AND  poa IS NOT NULL AND poa <> 'N' , 'Y' , 'N') ");
            queryStr.append("  FROM cust_general_info WHERE acct_no=:constAcctNo ");
        }
        if (!"".equalsIgnoreCase(constAcctNo) && !"".equalsIgnoreCase(shipAcctNo)) {
            queryStr.append(" UNION ALL ");
        }
        if (!"".equalsIgnoreCase(shipAcctNo)) {
            queryStr.append(" SELECT IF(poa <> '' AND  poa IS NOT NULL AND poa <> 'N' , 'Y' , 'N')  ");
            queryStr.append(" FROM cust_general_info WHERE acct_no=:shipAcctNo ");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        if (!"".equalsIgnoreCase(constAcctNo)) {
            query.setString("constAcctNo", constAcctNo);
        }
        if (!"".equalsIgnoreCase(shipAcctNo)) {
            query.setString("shipAcctNo", shipAcctNo);
        }
        List fileList = query.list();
        String file[] = new String[2];
        for (int j = 0; j < fileList.size(); j++) {
            file[j] = fileList.get(j).toString();
        }
        return file;
    }

    public String getOldBillToParty(Long fileId) throws Exception {
        String query = "SELECT bill_to_party FROM `lcl_booking` WHERE file_number_id=:fileId limit 1";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.addScalar("bill_to_party", StringType.INSTANCE);
        return (String) queryObject.uniqueResult();
    }

    public String getBookingType(String fileId) throws Exception {
        String query = "SELECT booking_type as bookingType FROM `lcl_booking` WHERE file_number_id='" + fileId + "'";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.addScalar("bookingType", StringType.INSTANCE);
        return (String) queryObject.uniqueResult();
    }

    public List<ConsolidationMiniManifestBean> getConsolidateDr(List fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT lfn.id AS fileId, ");
        sb.append("lfn.file_number AS fileNumber, ");
        sb.append("lc.lcl_file_number_id_b AS consolidateFile, ");
        sb.append("IF((SELECT lc.company_name FROM lcl_contact lc WHERE lc.id =lb.sup_contact_id) <> '' AND  ");
        sb.append("(SELECT lc.company_name FROM lcl_contact lc WHERE lc.id =lb.sup_contact_id) IS NOT NULL, ");
        sb.append("(SELECT lc.company_name FROM lcl_contact lc WHERE lc.id =lb.sup_contact_id),");
        sb.append("(SELECT lc.company_name FROM lcl_contact lc WHERE lc.id =lb.client_contact_id)) AS supplierName,");
        sb.append(" COALESCE(SUM(lbp.actual_piece_count),SUM(lbp.booked_piece_count)) AS piece, ");
        sb.append(" IF(pac1.description <> '' AND pac1.description IS NOT NULL,pac1.description,pac.`description`) AS packageName, ");
        sb.append(" COALESCE(SUM(lbp.actual_volume_imperial),SUM(lbp.booked_volume_imperial)) AS cft, ");
        sb.append(" COALESCE(SUM(lbp.actual_weight_imperial),SUM(lbp.booked_weight_imperial)) AS kgs, ");
        sb.append(" lbp.`piece_desc` AS comDescrption, ");
        sb.append("(SELECT GROUP_CONCAT(lclr.reference) FROM lcl_3p_ref_no lclr WHERE lclr.file_number_id = lfn.id AND lclr.`type`='CP')AS customerPo ");
        sb.append(" FROM lcl_file_number lfn JOIN lcl_booking lb ON lb.file_number_id = lfn.id ");
        sb.append(" JOIN lcl_booking_piece lbp ON lbp.file_number_id = lfn.id  ");
        sb.append(" JOIN lcl_consolidation lc ON lc.lcl_file_number_id_a = lfn.id  ");
        sb.append(" JOIN package_type pac ON pac.id = lbp.`booked_package_type_id` ");
        sb.append(" LEFT JOIN package_type pac1 ON pac1.id = lbp.`actual_package_type_id` ");
        sb.append(" JOIN commodity_type com ON com.`id` = lbp.`commodity_type_id` ");
        sb.append(" LEFT JOIN lcl_booking_export lbe ON lbe.`file_number_id` = lfn.id  ");
        sb.append("WHERE lfn.id IN(:fileId) ");
        sb.append("GROUP BY lbp.file_number_id ");
        sb.append("ORDER BY FIELD(lfn.id,fileId);");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameterList("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(ConsolidationMiniManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("consolidateFile", LongType.INSTANCE);
        query.addScalar("supplierName", StringType.INSTANCE);
        query.addScalar("piece", IntegerType.INSTANCE);
        query.addScalar("packageName", StringType.INSTANCE);
        query.addScalar("cft", DoubleType.INSTANCE);
        query.addScalar("kgs", DoubleType.INSTANCE);
        query.addScalar("comDescrption", StringType.INSTANCE);
        query.addScalar("customerPo", StringType.INSTANCE);
        return query.list();
    }

    public String getAgentAccountNo(String fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT agent_acct_no FROM lcl_booking WHERE  file_number_id='").append(fileId).append("'");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        String keyValue = (String) queryObject.uniqueResult();
        return CommonUtils.isNotEmpty(keyValue) ? keyValue : "";
    }

    public void deleteOsd(Long fileId) throws Exception {
        String queryString = "update  lcl_booking set over_short_damaged = false where file_number_id=?0 ";
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.setParameter("0", fileId);
        queryObject.executeUpdate();
    }

    public boolean isClassOfcom(String fileId, String comm, String oldComm) throws Exception {
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
        if (li.contains("Y") || li.contains("1")) {
            flag = true;
        } else {
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

    public boolean isvalOfcom(String comm) throws Exception {
        boolean flag = false;
        String eciDBName = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ofcom.`class` AS CODE FROM ").append(eciDBName).append(" .`ofcomm` ofcom  WHERE ofcom.`comcde`=(:comm)");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("comm", comm);
        List li = queryObject.list();
        for (Object obj : li) {
            if (obj != null && (obj.toString().equalsIgnoreCase("Y") || obj.toString().equals("1"))) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    public String getExportBookingColumnValue(String columnName, String fileId) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("select " + columnName + " from lcl_booking where file_number_id=:fileId");
        query.setParameter("fileId", fileId);
        Object result = query.uniqueResult();
        return result != null ? result.toString() : "";
    }

    public boolean getBothBillToCodeFlag(Long fileNumberId) throws Exception {
        String query = "SELECT IF(COUNT(DISTINCT bkc.`ar_bill_to_party`) > 1,TRUE,FALSE) AS flag FROM `lcl_booking_ac` bkc WHERE bkc.`file_number_id`=:fileNumberId and  bkc.`ar_amount` <> 0.00 AND bkc.`ar_bill_to_party` NOT IN('S')";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileNumberId", fileNumberId);
        queryObject.addScalar("flag", BooleanType.INSTANCE);
        return (boolean) queryObject.uniqueResult();
    }

    public void updateTerminateDesc(String fileId, String desc) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE lcl_booking  lb SET lb.terminate_desc = '").append(desc).append("' WHERE  lb.file_number_id IN (").append(fileId).append(")");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public List<ReleaseReportBean> getBookingDetails(String polId) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT UnLocationGetNameStateCntryByID(fn.pod) AS unLocation,SUM(fn.piececount) AS pieceCount,SUM(fn.weight) AS weight,SUM(fn.cube) AS cube,COUNT(DISTINCT(fn.fileId)) AS totalDr ");
        builder.append("FROM (SELECT lf.id AS fileId, lb.pod_id AS pod,IFNULL(bpl.actual_piece_count,bpl.`booked_piece_count`)AS piececount,IFNULL(bpl.`actual_weight_metric`+bpl.`actual_weight_imperial`,bpl.`booked_weight_metric`+bpl.`booked_weight_imperial`)AS weight, ");
        builder.append("IFNULL(bpl.`actual_volume_imperial`+bpl.`actual_volume_metric`,bpl.`booked_volume_imperial`+bpl.`booked_volume_metric`) AS CUBE FROM lcl_booking lb JOIN lcl_file_number lf ON lf.`id` = lb.`file_number_id` ");
        builder.append("JOIN lcl_booking_piece bpl ON bpl.`file_number_id` = lf.`id` WHERE lb.`pol_id` =:polId AND lb.booking_type <> 'I' ");
        builder.append("UNION ");
        builder.append("SELECT lf.id AS fileId,lbm.foreign_port_of_discharge_id AS pod,IFNULL(bp.actual_piece_count,bp.booked_piece_count) AS piececount,IFNULL(bp.`actual_weight_metric`+bp.`actual_weight_imperial`,bp.`booked_weight_metric`+bp.`booked_weight_imperial`)AS weight, ");
        builder.append("IFNULL(bp.`actual_volume_imperial`+bp.`actual_volume_metric`,bp.`booked_volume_imperial`+bp.`booked_volume_metric`) AS CUBE  FROM lcl_booking_import lbm JOIN lcl_file_number lf ON lf.`id` = lbm.file_number_id  ");
        builder.append("JOIN lcl_booking b ON b.file_number_id = lf.`id` JOIN lcl_booking_piece bp ON bp.`file_number_id` = lf.`id` WHERE lbm.`usa_port_of_exit_id` =:polId AND b.booking_type <> 'I') AS fn  ");
        builder.append("JOIN lcl_booking_export lbx ON lbx.file_number_id = fn.fileId AND lbx.`released_datetime` IS NOT NULL  GROUP BY fn.pod ");
        SQLQuery query = getCurrentSession().createSQLQuery(builder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ReleaseReportBean.class));
        query.addScalar("unLocation", StringType.INSTANCE);
        query.addScalar("pieceCount", IntegerType.INSTANCE);
        query.addScalar("weight", StringType.INSTANCE);
        query.addScalar("cube", StringType.INSTANCE);
        query.addScalar("totalDr", IntegerType.INSTANCE);
        query.setParameter("polId", polId);
        return query.list();
    }

    public void updateCIF(Long fileId, String CIF) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("update  lcl_booking set insurance_cif = '").append(CIF).append("' where file_number_id= '").append(fileId).append("'");
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void updatePooWhseLrdt(Date originLrdDate, Long fileId, boolean hazmatFlag) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (hazmatFlag) {
            sb.append(" UPDATE  lcl_booking SET poo_whse_lrdt=(SELECT CASE WHEN DAYOFWEEK(:vesselDate) = 1 THEN    ");
            sb.append(" DATE_ADD(:vesselDate, INTERVAL -2 DAY) WHEN DAYOFWEEK(:vesselDate) = 7 THEN   ");
            sb.append(" DATE_ADD(:vesselDate, INTERVAL -1 DAY) ELSE :vesselDate END )  ");
            sb.append(" where file_number_id=:file_Id ");
        } else {
            sb.append(" update lcl_booking set poo_whse_lrdt=:vesselDate  ");
            sb.append(" where file_number_id=:file_Id ");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("vesselDate", originLrdDate);
        query.setParameter("file_Id", fileId);
        query.executeUpdate();
    }

    public BigInteger checkDispositionForTranshipment(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT id FROM lcl_booking_dispo WHERE file_number_id=").append(fileId).append(" limit 1");
        Object checkDisposition = getSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != checkDisposition ? (BigInteger) checkDisposition : null;
    }

    public String getBookingTypeScanOrAttach(String fileNumber) throws Exception {
        String query = "SELECT booking_type as bookingType FROM `lcl_booking` WHERE file_number_id IN (select id from lcl_file_number where file_number='" + fileNumber + "') ";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.addScalar("bookingType", StringType.INSTANCE);
        return (String) queryObject.setMaxResults(1).uniqueResult();
    }

    public void updateUserDetails(Long fileId, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking set modified_by_user_id = ").append(userId);
        queryBuilder.append(",modified_datetime = SYSDATE() where file_number_id = ");
        queryBuilder.append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }
}
