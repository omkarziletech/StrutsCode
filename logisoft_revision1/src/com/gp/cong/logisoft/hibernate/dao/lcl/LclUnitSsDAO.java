/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.beans.BlueScreenChargesBean;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.beans.LclImpProfitLossBean;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

public class LclUnitSsDAO extends BaseHibernateDAO<LclUnitSs> implements ConstantsInterface {

    private static final Log log = LogFactory.getLog(LclUnitSs.class);
    private Double totalAmount = 0.00;
    private String bookingAcIds = "";
    private Map<String, List<ImportsManifestBean>> fileMap;
    private Map<String, List<ImportsManifestBean>> unitMap;

    public Map getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map fileMap) {
        this.fileMap = fileMap;
    }

    public Map<String, List<ImportsManifestBean>> getUnitMap() {
        return unitMap;
    }

    public void setUnitMap(Map<String, List<ImportsManifestBean>> unitMap) {
        this.unitMap = unitMap;
    }

    public String getBookingAcIds() {
        return bookingAcIds;
    }

    public void setBookingAcIds(String bookingAcIds) {
        this.bookingAcIds = bookingAcIds;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LclUnitSsDAO() {
        super(LclUnitSs.class);
    }

    public LclUnitSs getLclUnitSSByLclUnitHeader(Long unitId, Long headerId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitSs.class, "lclUnitSs");
        criteria.createAlias("lclUnitSs.lclUnit", "lclUnit");
        criteria.createAlias("lclUnitSs.lclSsHeader", "lclSsHeader");
        if (!CommonUtils.isEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnit.id", unitId));
        }
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("lclSsHeader.id", headerId));
        }
        return (LclUnitSs) criteria.setMaxResults(1).uniqueResult();
    }

    public List<ImportsManifestBean> getDRSForAgentInvoice(Long unitSSId, String invoiceFlag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, ImportsManifestBean> manifestMap = new LinkedHashMap();
        if (CommonUtils.isEmpty(fileMap)) {
            fileMap = new LinkedHashMap();
        }
        String key = null;
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String ruleName = systemRulesDAO.getSystemRulesByCode(COMPANY_CODE);
        queryBuilder.append("SELECT fileId AS fileId,fileNo AS fileNo,finalDestination AS finalDestination,shipmentType AS shipmentType,agentNo AS agentNo,agentName AS agentName,");
        queryBuilder.append("agentAcctType AS agentAcctType,glmap.id AS chargeId,glmap.Charge_code as chargeCode,(chg.ar_amount + chg.adjustment_amount) ");
        queryBuilder.append("AS totalCharges,chg.invoice_number AS invoiceNo,billing_terminal AS billingTerminal,");
        queryBuilder.append("IF(ad.Account IS NOT NULL,ad.account,'') AS glAccount,chg.id AS ");
        queryBuilder.append("bookingAcId,unitNumber AS unitNumber,subHouseBl as subHouseBl,pickupCity as pickupCity FROM (SELECT file.id AS fileId,file.file_number AS fileNo,CONCAT(' (',unloc.un_loc_name,', ',IF(gd.code='US',gd.code,gdcountfd.codedesc),')') AS finalDestination,");
        queryBuilder.append("agent.acct_no AS agentNo,agent.acct_name AS agentName,agent.acct_type AS agentAcctType,");
        queryBuilder.append("'LCLI' AS shipmentType,b.billing_terminal AS billing_terminal,unit.unit_no AS unitNumber,lbi.sub_house_bl as subHouseBl,lbp.pickup_city as pickupCity FROM lcl_unit_ss unit_ss JOIN ");
        queryBuilder.append("lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id JOIN lcl_booking_piece book_piece ON ");
        queryBuilder.append("unit_pieces.booking_piece_id = book_piece.id JOIN lcl_unit unit ON unit_ss.unit_id = unit.id JOIN lcl_file_number FILE ");
        queryBuilder.append("ON book_piece.file_number_id = file.id JOIN lcl_booking b ON file.id = b.file_number_id LEFT JOIN un_location unloc ON b.fd_id = unloc.id LEFT JOIN genericcode_dup gd ON unloc.statecode = gd.id ");
        queryBuilder.append(" JOIN lcl_unit_ss_imports lussi ON lussi.unit_id = unit.id  LEFT JOIN genericcode_dup gdcountfd ON gdcountfd.id=unloc.countrycode ");
        queryBuilder.append(" JOIN trading_partner agent ON b.sup_acct_no = agent.acct_no LEFT JOIN lcl_booking_import lbi ON file.id = lbi.file_number_id LEFT JOIN lcl_booking_pad lbp ON file.id = lbp.file_number_id  WHERE unit_ss.id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" GROUP BY file.id) f JOIN lcl_booking_ac chg ON f.fileId = chg.file_number_id JOIN gl_mapping glmap ON glmap.id = ");
        queryBuilder.append("ar_gl_mapping_id LEFT JOIN terminal_gl_mapping term ON f.billing_terminal = term.terminal LEFT JOIN account_details ad ON ");
        queryBuilder.append("ad.Account=CONCAT_WS('-','");
        queryBuilder.append(ruleName);
        queryBuilder.append("',glmap.GL_Acct,LPAD(IF(glmap.derive_yn != 'N' AND glmap.derive_yn != 'F' AND glmap.suffix_value ");
        queryBuilder.append("IN ('B', 'L', 'D', 'F'),IF(glmap.suffix_value = 'B',term.lcl_import_billing,IF(glmap.suffix_value = 'L',term.");
        queryBuilder.append("lcl_import_loading,'')),glmap.suffix_value), 2, '0')) WHERE chg.ar_amount > 0.00 AND ");
        queryBuilder.append("chg.ar_bill_to_party = 'A' AND chg.rels_to_inv = 1 AND (chg.sp_reference_no IS NULL OR chg.sp_reference_no='') ORDER BY fileNo DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("chargeId", LongType.INSTANCE);
        query.addScalar("bookingAcId", LongType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("finalDestination", StringType.INSTANCE);
        query.addScalar("shipmentType", StringType.INSTANCE);
        query.addScalar("totalCharges", DoubleType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("agentAcctType", StringType.INSTANCE);
        query.addScalar("invoiceNo", StringType.INSTANCE);
        query.addScalar("billingTerminal", StringType.INSTANCE);
        query.addScalar("unitNumber", StringType.INSTANCE);
        query.addScalar("subHouseBl", StringType.INSTANCE);
        query.addScalar("pickupCity", StringType.INSTANCE);
        List<ImportsManifestBean> drList = query.list();
        List fileList = null;
        if (drList != null && drList.size() > 0) {
            for (ImportsManifestBean manifestBean : drList) {
                totalAmount += manifestBean.getTotalCharges();
                bookingAcIds += manifestBean.getBookingAcId() + ",";
                key = "IMP-" + manifestBean.getFileNo().toString();
                if (!fileMap.containsKey(key)) {
                    fileList = new ArrayList();
                    ImportsManifestBean impManifestBean = new ImportsManifestBean();
                    impManifestBean.setChargeCode(manifestBean.getChargeCode());
                    if (manifestBean.getChargeCode().equalsIgnoreCase("DOORDEL") && manifestBean.getPickupCity() != null) {
                        impManifestBean.setChargeCode(manifestBean.getChargeCode() + " ( " + manifestBean.getPickupCity() + " )");
                    }
                    impManifestBean.setFinalDestination(manifestBean.getFinalDestination());
                    impManifestBean.setTotalCharges(manifestBean.getTotalCharges());
                    impManifestBean.setSubHouseBl(manifestBean.getSubHouseBl());
                    fileList.add(impManifestBean);
                    fileMap.put(key, fileList);
                } else {
                    List<ImportsManifestBean> manifestListFromMap = (List) fileMap.get(key);
                    ImportsManifestBean impManifestBean = new ImportsManifestBean();
                    impManifestBean.setChargeCode(manifestBean.getChargeCode());
                    if (manifestBean.getChargeCode().equalsIgnoreCase("DOORDEL") && manifestBean.getPickupCity() != null) {
                        impManifestBean.setChargeCode(manifestBean.getChargeCode() + " ( " + manifestBean.getPickupCity() + " )");
                    }
                    impManifestBean.setFinalDestination(manifestBean.getFinalDestination());
                    impManifestBean.setTotalCharges(manifestBean.getTotalCharges());
                    impManifestBean.setSubHouseBl(manifestBean.getSubHouseBl());
                    manifestListFromMap.add(impManifestBean);
                    fileMap.put(key, manifestListFromMap);
                }
                key = manifestBean.getChargeCode().toString();
                if (!manifestMap.containsKey(key)) {
                    manifestBean.setConcatenatedFileNos(manifestBean.getFileNo());
                    manifestMap.put(key, manifestBean);
                } else {
                    ImportsManifestBean manifestBeanFromMap = (ImportsManifestBean) manifestMap.get(key);
                    manifestBeanFromMap.setTotalCharges(manifestBeanFromMap.getTotalCharges() + manifestBean.getTotalCharges());
                    manifestBeanFromMap.setConcatenatedFileNos(manifestBeanFromMap.getConcatenatedFileNos() + "," + manifestBean.getFileNo());
                    manifestBeanFromMap.setSubHouseBl(manifestBeanFromMap.getSubHouseBl());
                    manifestMap.put(key, manifestBeanFromMap);
                }
            }
        }
        if (CommonUtils.isNotEmpty(bookingAcIds)) {
            bookingAcIds = bookingAcIds.substring(0, bookingAcIds.length() - 1);
        }
        return new ArrayList(manifestMap.values());
    }

    public ImportsManifestBean getAgentDetailsByRelsToInv(Long unitSSId, String agentAcctNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT agentNo AS agentNo,agentName AS agentName,");
        queryBuilder.append("SUM(IF(chg.ar_bill_to_party='A' AND chg.rels_to_inv=1,chg.ar_amount,0))AS agentrelInv,");
        queryBuilder.append("SUM(IF(chg.ar_bill_to_party='A' AND chg.rels_to_inv=0,chg.ar_amount,0))AS agentrelnotInv ");
        queryBuilder.append("FROM (SELECT file.id AS fileId,file.file_number AS fileNo,agent.acct_no AS agentNo,agent.acct_name AS agentName FROM ");
        queryBuilder.append("lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id JOIN ");
        queryBuilder.append("lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_unit unit ON unit_ss.unit_id = ");
        queryBuilder.append("unit.id JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id JOIN lcl_booking b ON file.id = b.file_number_id ");
        queryBuilder.append("JOIN lcl_unit_ss_imports lussi ON lussi.unit_id = unit.id JOIN trading_partner agent ON b.sup_acct_no = ");
        queryBuilder.append("agent.acct_no  WHERE unit_ss.id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" AND b.sup_acct_no='").append(agentAcctNo).append("'");
        queryBuilder.append(" GROUP BY file.id) f JOIN lcl_booking_ac chg ON f.fileId = chg.file_number_id WHERE chg.ar_amount > 0.00 AND ");
        queryBuilder.append("chg.ar_bill_to_party = 'A' AND (chg.sp_reference_no IS NULL OR chg.sp_reference_no='')");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("agentrelInv", StringType.INSTANCE);
        query.addScalar("agentrelnotInv", StringType.INSTANCE);
        return (ImportsManifestBean) query.uniqueResult();
    }

    public List<ImportsManifestBean> getChargesBillingType(Long unitSSId, String selectedMenu, String postFlag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT fileId AS fileId,fileNo AS fileNo,chg.ar_bill_to_party AS billToParty,gm.Charge_code AS chargeCode,");
        queryBuilder.append("IF(chg.ar_bill_to_party = 'A',agentNo,IF(chg.ar_bill_to_party = 'F',forwarderNo,IF(chg.ar_bill_to_party = 'S',");
        queryBuilder.append("shipperNo,IF(chg.ar_bill_to_party = 'C',consigneeNo,IF(chg.ar_bill_to_party = 'N',notifyNo,thirdPartyNo))))) AS ");
        queryBuilder.append("customerNumber FROM (SELECT file.id AS fileId,file.file_number AS fileNo,ship.acct_no AS shipperNo,");
        queryBuilder.append("noty.acct_no AS notifyNo,cons.acct_no AS consigneeNo,fwd.acct_no AS forwarderNo,tprty.acct_no AS thirdPartyNo,");
        queryBuilder.append("agent.acct_no AS agentNo FROM  lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id ");
        queryBuilder.append(" = unit_ss.id JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_file_number FILE ");
        queryBuilder.append("ON book_piece.file_number_id = file.id JOIN ");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("lcl_booking ");
        } else {
            queryBuilder.append("lcl_bl ");
        }
        queryBuilder.append("b ON file.id = b.file_number_id LEFT JOIN trading_partner agent ON ");
        queryBuilder.append("b.agent_acct_no = agent.acct_no LEFT JOIN trading_partner ship ON b.ship_acct_no = ship.acct_no LEFT JOIN trading_partner ");
        queryBuilder.append("cons ON b.cons_acct_no = cons.acct_no LEFT JOIN trading_partner fwd ON b.fwd_acct_no = fwd.acct_no LEFT JOIN trading_partner ");
        queryBuilder.append("tprty ON b.third_party_acct_no = tprty.acct_no LEFT JOIN trading_partner noty ON b.noty_acct_no = noty.acct_no  WHERE ");
        if (postFlag.equalsIgnoreCase("Y")) {
            queryBuilder.append("IF(unit_ss.`cob` = 0,file.status <> '',file.status <> 'M')");
        } else {
            queryBuilder.append("file.status = 'M' ");
        }
        if ("Imports".equalsIgnoreCase(selectedMenu)) {
            queryBuilder.append(" AND b.booking_type = 'I' ");
        }
        queryBuilder.append("AND unit_ss.id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" GROUP BY file.id) AS f JOIN ");
        if (selectedMenu.equalsIgnoreCase("Imports")) {
            queryBuilder.append("lcl_booking_ac ");
        } else {
            queryBuilder.append("lcl_bl_ac ");
        }
        queryBuilder.append("chg ON f.fileId = chg.file_number_id JOIN gl_mapping gm ON gm.id =  ar_gl_mapping_id ");
        queryBuilder.append("WHERE chg.ar_amount > 0.00 ORDER BY f.fileId DESC");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        query.addScalar("customerNumber", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        return query.list();
    }

    public List<ImportsManifestBean> getDRSBillingType(Long unitSSId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT file.id AS fileId,file.file_number AS fileNo,IF(b.bill_to_party = 'C',");
        queryBuilder.append("cons.acct_no,IF(b.bill_to_party = 'N',noty.acct_no,tprty.acct_no)) AS customerNumber,b.bill_to_party AS billToParty ");
        queryBuilder.append("FROM  lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id ");
        queryBuilder.append("JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_file_number FILE ");
        queryBuilder.append("ON book_piece.file_number_id = file.id JOIN lcl_booking b ON file.id = b.file_number_id LEFT JOIN trading_partner cons ON ");
        queryBuilder.append("b.cons_acct_no = cons.acct_no LEFT JOIN trading_partner tprty ON b.third_party_acct_no = tprty.acct_no LEFT JOIN ");
        queryBuilder.append("trading_partner noty ON b.noty_acct_no = noty.acct_no  WHERE unit_ss.id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" and file.status='M' and b.bill_to_party!='A' GROUP BY file.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("billToParty", StringType.INSTANCE);
        query.addScalar("customerNumber", StringType.INSTANCE);
        return query.list();
    }

    public Object[] getVoyageUnitValues(String unitSSId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT CONCAT(ss_head.billing_trmnum,'-',headorg.un_loc_code,'-',headdest.un_loc_code,'-',ss_head.schedule_no),unit.unit_no,");
        queryBuilder.append("vessel.code,vessel.codedesc,ussm.masterbl AS masterBl,ss_detail.sta AS sailDate,ss_head.schedule_no  FROM lcl_unit_ss unit_ss LEFT JOIN lcl_unit unit ON ");
        queryBuilder.append("unit_ss.unit_id = unit.id LEFT JOIN lcl_ss_header ss_head ON unit_ss.ss_header_id = ss_head.id LEFT JOIN lcl_ss_detail ");
        queryBuilder.append("ss_detail ON ss_head.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V' LEFT JOIN trading_partner steamshipline ");
        queryBuilder.append("ON steamshipline.acct_no = ss_detail.sp_acct_no LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = ");
        queryBuilder.append("vessel.codedesc AND vessel.codetypeid LEFT JOIN codetype vessel_type ON vessel.codetypeid = vessel_type.codetypeid ");
        queryBuilder.append("AND vessel_type.description = 'Vessel Codes'  LEFT JOIN lcl_unit_ss_manifest ussm ON (unit.id = ussm.unit_id AND ");
        queryBuilder.append("ss_head.id = ussm.ss_header_id) JOIN un_location headorg ON headorg.id = ss_head.origin_id JOIN un_location headdest ON headdest.id = ");
        queryBuilder.append("ss_head.destination_id ");
        queryBuilder.append(" WHERE unit_ss.id = ");
        queryBuilder.append(unitSSId);
        queryBuilder.append(" GROUP BY unit_ss.id");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return (Object[]) query.setMaxResults(1).uniqueResult();
    }

    public String[] getTotalBkgInUnits(String unitSsId) throws Exception {
        String[] fileDetails = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(GROUP_CONCAT(DISTINCT fn.id) USING utf8)as fileId,CONVERT(GROUP_CONCAT(DISTINCT CONCAT('IMP-',fn.file_number)) USING utf8)as fileNumber FROM lcl_booking_piece_unit bpu LEFT JOIN lcl_booking_piece bp ON bpu.booking_piece_id=bp.id");
        sb.append(" LEFT JOIN lcl_file_number fn ON bp.file_number_id=fn.id LEFT JOIN lcl_booking lb ON lb.file_number_id = fn.id WHERE bpu.lcl_unit_ss_id=").append(unitSsId).append(" AND lb.booking_type!='T'");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                fileDetails[0] = agentObj[0].toString();
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                fileDetails[1] = agentObj[1].toString();
            }
        }
        return fileDetails;
    }

    public String[] getTotalBookingCount(String lclSsHeaderId) throws Exception {
        String[] fileDetails = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(GROUP_CONCAT(DISTINCT fn.id) USING utf8)as fileId,CONVERT(GROUP_CONCAT(DISTINCT CONCAT('',fn.file_number)) USING utf8)as fileNumber FROM lcl_booking_piece_unit bpu   LEFT JOIN lcl_booking_piece bp     ON bpu.booking_piece_id = bp.id   LEFT JOIN lcl_file_number fn ");
        sb.append(" ON bp.file_number_id = fn.id  LEFT JOIN lcl_unit_ss lsu  ON lsu.id = bpu.`lcl_unit_ss_id`  WHERE lsu.`ss_header_id` =").append(lclSsHeaderId);
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                fileDetails[0] = agentObj[0].toString();
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                fileDetails[1] = agentObj[1].toString();
            }
        }
        return fileDetails;
    }

    public String[] getTotalBkgInUnitsExports(String unitSsId) throws Exception {
        String[] fileDetails = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(GROUP_CONCAT(DISTINCT fn.id) USING utf8)as fileId,CONVERT(GROUP_CONCAT(DISTINCT CONCAT(fn.file_number)) ");
        sb.append("USING utf8)as fileNumber FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp ON bpu.booking_piece_id = bp.id ");
        sb.append("JOIN lcl_file_number fn ON bp.file_number_id = fn.id JOIN lcl_bl lb ON lb.file_number_id = fn.id WHERE bpu.lcl_unit_ss_id = ");
        sb.append(unitSsId);
        sb.append(" AND lb.posted_by_user_id IS NOT NULL");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                fileDetails[0] = agentObj[0].toString();
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                fileDetails[1] = agentObj[1].toString();
            }
        }
        return fileDetails;
    }

    public Integer getTotalCntBkgInUnitsExports(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        BigInteger count = new BigInteger("0");
        sb.append("SELECT COUNT(*) FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp ON bpu.booking_piece_id = bp.id ");
        sb.append("JOIN lcl_file_number fn ON bp.file_number_id = fn.id JOIN lcl_bl lb ON lb.file_number_id = fn.id WHERE bpu.lcl_unit_ss_id = ");
        sb.append(unitSsId);
        sb.append(" AND lb.posted_by_user_id IS NOT NULL");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        Object o = query.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public String getConcatenatedFileIds(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(GROUP_CONCAT(DISTINCT fn.id) USING utf8)as fileId FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp ON bpu.booking_piece_id = bp.id ");
        sb.append("JOIN lcl_file_number fn ON bp.file_number_id=fn.id JOIN lcl_booking lb ON lb.file_number_id = fn.id WHERE bpu.lcl_unit_ss_id = ");
        sb.append(unitSsId).append(" AND lb.booking_type='I' ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        return (String) queryObject.uniqueResult();
    }

    public String getPickedBlByPosted(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(DISTINCT fn.id) as fileId FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp ");
        sb.append(" ON bpu.booking_piece_id = bp.id JOIN lcl_file_number fn ON bp.file_number_id = fn.id ");
        sb.append(" JOIN lcl_bl lb ON lb.file_number_id = getHouseBLForConsolidateDr(fn.id) ");
        sb.append(" WHERE bpu.lcl_unit_ss_id =:unitSsId ");
        sb.append(" AND lb.posted_by_user_id IS NOT NULL");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("unitSsId", unitSsId);
        queryObject.addScalar("fileId", StringType.INSTANCE);
        return (String) queryObject.uniqueResult();
    }

    public String getPrepaidCollect(String spBookingNo) throws Exception {
        String queryString = "SELECT prepaid_collect FROM lcl_ss_masterbl mbl LEFT JOIN lcl_unit_ss luss ON luss.sp_booking_no=mbl.sp_booking_no WHERE luss.sp_booking_no='" + spBookingNo + "'";
        return getCurrentSession().createSQLQuery(queryString).uniqueResult().toString();
    }

    public Character getStatusById(String unitSsId) throws Exception {
        String query = "SELECT STATUS FROM lcl_unit_ss WHERE id = " + unitSsId;
        Character ch = (Character) getSession().createSQLQuery(query).uniqueResult();
        return ch;
    }

    public LclUnitSs getUnitSs(Long headerId, String UnitNo) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("FROM LclUnitSs us JOIN us.lclUnit u WHERE u.unitNo = '").append(UnitNo).append("'");
        query.append(" AND us.lclSsHeader.id = ").append(headerId);
        Object[] obj = (Object[]) getCurrentSession().createQuery(query.toString()).uniqueResult();
        return (LclUnitSs) (null != obj ? obj[0] : null);
    }

    public List<ImportsManifestBean> getAgentInvoiceCharges(String unitSsId) throws Exception {
        LclUnitSs lclUnitSs = new LclUnitSsDAO().findById(Long.parseLong(unitSsId));
        String eliteCode = lclUnitSs.getLclUnit().getUnitType().getEliteType();
        LclUnitSsImports lclUnitSsImports = lclUnitSs.getLclSsHeader().getLclUnitSsImportsList().get(0);
        Ports polId = new PortsDAO().getByProperty("unLocationCode", lclUnitSs.getLclSsHeader().getOrigin().getUnLocationCode());
        Ports podId = new PortsDAO().getByProperty("unLocationCode", lclUnitSs.getLclSsHeader().getDestination().getUnLocationCode());
        String polSchNum = "";
        String podSchNum = "";
        if (polId != null) {
            polSchNum = polId.getShedulenumber();
        }
        if (podId != null) {
            podSchNum = podId.getShedulenumber();
        }
        String blueScreenDb = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder sb = new StringBuilder();
        List<ImportsManifestBean> importsManifestBeansList = new ArrayList<ImportsManifestBean>();
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String ruleName = systemRulesDAO.getSystemRulesByCode(COMPANY_CODE);
        sb.append("SELECT gm.charge_code,c.chgdsc,untsza,untszb,untszc,untszd,untsze,untszf,untszg,untszh,untszv,untszw,untszx,untszy,untszz,");
        sb.append("IF(ad.Account IS NOT NULL,ad.account,'') AS glAccount,ss_head.billing_trmnum AS terminal,");
        sb.append("unit.unit_no AS unitNumber,ut.description AS unitSize FROM ").append(blueScreenDb).append(".rates r LEFT JOIN ").append(blueScreenDb).append(".chgs c ON r.chgcod=c.chgcde ");
        sb.append("LEFT JOIN cust_general_info cgi ON cgi.acct_no='").append(lclUnitSsImports.getOriginAcctNo().getAccountno()).append("' ");
        sb.append("JOIN genericcode_dup gd ON cgi.imp_comm_no=gd.id JOIN commodity_type ct ON gd.code=ct.code ");
        sb.append("JOIN lcl_unit_ss unit_ss ON unit_ss.id = ").append(unitSsId).append(" JOIN lcl_ss_header ss_head ON ");
        sb.append("unit_ss.ss_header_id = ss_head.id JOIN lcl_unit unit ON unit.id = unit_ss.unit_id LEFT JOIN unit_type ut ON unit.unit_type_id = ut.id ");
        sb.append("LEFT JOIN gl_mapping glmap ON glmap.bluescreen_chargecode = c.chgcde AND glmap.shipment_type='LCLI' AND glmap.transaction_type='AR' ");
        sb.append("JOIN gl_mapping gm ON r.chgcod=gm.bluescreen_chargecode AND gm.shipment_type='LCLI' ");
        sb.append("LEFT JOIN terminal_gl_mapping term ON ss_head.billing_trmnum= term.terminal LEFT JOIN account_details ad ON ad.Account=CONCAT_WS('-','");
        sb.append(ruleName);
        sb.append("',glmap.GL_Acct,LPAD(IF(glmap.derive_yn != 'N' AND glmap.derive_yn != 'F' AND ");
        sb.append("glmap.suffix_value IN ('B', 'L', 'D', 'F'),IF(glmap.suffix_value = 'B',term.lcl_import_billing,IF(glmap.suffix_value = 'L',term.");
        sb.append("lcl_import_loading,'')),glmap.suffix_value), 2, '0')) WHERE r.imppc='P' AND r.orgnum=");
        sb.append(polSchNum);
        sb.append(" AND r.dstnum=");
        sb.append(podSchNum);
        sb.append(" AND r.comnum=ct.code ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List agentList = queryObject.list();
        Double agentAmount = 0.00;
        if (agentList != null && !agentList.isEmpty()) {
            for (int i = 0; i < agentList.size(); i++) {
                Object[] agentObj = (Object[]) agentList.get(i);
                ImportsManifestBean importsManifestBean = new ImportsManifestBean();
                if (agentObj[2] != null && !agentObj[2].toString().trim().equals("0.00") && "A".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[2].toString());
                }
                if (agentObj[3] != null && !agentObj[3].toString().trim().equals("0.00") && "B".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[3].toString());
                }
                if (agentObj[4] != null && !agentObj[4].toString().trim().equals("0.00") && "C".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[4].toString());
                }
                if (agentObj[5] != null && !agentObj[5].toString().trim().equals("0.00") && "D".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[5].toString());
                }
                if (agentObj[6] != null && !agentObj[6].toString().trim().equals("0.00") && "E".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[6].toString());
                }
                if (agentObj[7] != null && !agentObj[7].toString().trim().equals("0.00") && "F".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[7].toString());
                }
                if (agentObj[8] != null && !agentObj[8].toString().trim().equals("0.00") && "G".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[8].toString());
                }
                if (agentObj[9] != null && !agentObj[9].toString().trim().equals("0.00") && "H".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[9].toString());
                }
                if (agentObj[9] != null && !agentObj[9].toString().trim().equals("0.00") && "V".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[9].toString());
                }
                if (agentObj[10] != null && !agentObj[10].toString().trim().equals("0.00") && "W".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[10].toString());
                }
                if (agentObj[11] != null && !agentObj[11].toString().trim().equals("0.00") && "X".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[11].toString());
                }
                if (agentObj[12] != null && !agentObj[12].toString().trim().equals("0.00") && "Y".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[12].toString());
                }
                if (agentObj[13] != null && !agentObj[13].toString().trim().equals("0.00") && "Z".equalsIgnoreCase(eliteCode)) {
                    agentAmount = Double.parseDouble(agentObj[13].toString());
                }
                if (agentAmount > 0.00) {
                    importsManifestBean.setChargeCode(agentObj[0].toString());
                    importsManifestBean.setTotalCharges(agentAmount);
                    totalAmount += agentAmount;
                    importsManifestBean.setGlAccount(agentObj[15].toString());
                    importsManifestBean.setFileNo("");
                    importsManifestBean.setBillingTerminal(agentObj[16].toString());
                    importsManifestBean.setConcatenatedFileNos("");
                    importsManifestBean.setShipmentType(LclCommonConstant.LCL_SHIPMENT_TYPE_IMPORT);
                    importsManifestBean.setAgentName(lclUnitSsImports.getOriginAcctNo().getAccountName());
                    importsManifestBean.setAgentNo(lclUnitSsImports.getOriginAcctNo().getAccountno());
                    importsManifestBean.setAgentAcctType(lclUnitSsImports.getOriginAcctNo().getAcctType());
                    importsManifestBean.setUnitNumber(agentObj[17].toString());
                    importsManifestBean.setFinalDestination(agentObj[18].toString());
                    importsManifestBeansList.add(importsManifestBean);
                    String key = importsManifestBean.getUnitNumber();
                    if (CommonUtils.isEmpty(unitMap)) {
                        unitMap = new LinkedHashMap();
                    }
                    unitMap.put(key, importsManifestBeansList);
                }
            }
        }
        return importsManifestBeansList;
    }

    /*
     * @param type - it is Email type like E1, E2, E3 etc.,
     * @param isVoyageNotification - lcl_voyage_notification table to be joined or not to be joined
     * @param unitSsId - unitSsId
     */
    public List<ImportsManifestBean> getEmails(final String emailType, final String faxType, final boolean isVoyageNotification) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(appendJobEmailSubQuery());
        sb.append(groupConcat(emailType, faxType, isVoyageNotification));
        sb.append(appendEmailSubQuery(isVoyageNotification));
        sb.append("WHERE lvf.");
        if ("E2".equalsIgnoreCase(emailType)) {
            sb.append("minute");
        } else {
            sb.append("daily");
        }
        sb.append("='pending' AND lbm.transshipment!=1 GROUP BY f.id ORDER BY lvf.id DESC");
        sb.append(")emailE WHERE emailE.email!='' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);//fileId
        query.addScalar("fileNo", StringType.INSTANCE);//fileNo
        query.addScalar("chargeId", LongType.INSTANCE);//unitssId
        query.addScalar("unitId", LongType.INSTANCE);//unitId
        query.addScalar("email", StringType.INSTANCE);//EmailId
        query.addScalar("agentNo", StringType.INSTANCE);//Pod--UnlocationCode
        query.addScalar("agentName", StringType.INSTANCE);//Pod--Terminal-terminaltName
        query.addScalar("agentAcctType", StringType.INSTANCE);//Pod-Terminal-imp terminal email
        query.addScalar("destination", StringType.INSTANCE);//FD--UnlocationCode
        query.addScalar("destinationName", StringType.INSTANCE);//FD--Terminal-terminaltName
        query.addScalar("destinationCountry", StringType.INSTANCE);//FD-Terminal-imp terminal email
        return query.list();
    }

    private StringBuilder groupConcat(final String emailType, final String faxType, boolean isVoyageNotification) {
        StringBuilder groupConcatBuilder = new StringBuilder();
        groupConcatBuilder.append("GROUP_CONCAT(DISTINCT CONCAT_WS(',',");
        groupConcatBuilder.append("IF(noty_code_f.code IN ('").append(emailType).append("'),LOWER(noty_ct.email),IF(noty_code_f.code IN ('").append(faxType).append("'),noty_ct.fax,NULL)),");
        groupConcatBuilder.append("IF(noty2_code_f.code IN ('").append(emailType).append("'),LOWER(noty2_ct.email),IF(noty2_code_f.code IN ('").append(faxType).append("'),noty2_ct.fax,NULL)),");
        groupConcatBuilder.append("IF(cons_code_f.code IN ('").append(emailType).append("'),LOWER(cons_ct.email),IF(cons_code_f.code IN ('").append(faxType).append("'),cons_ct.fax,NULL)),");
        groupConcatBuilder.append("IF(ship_code_f.code IN ('").append(emailType).append("'),LOWER(ship_ct.email),IF(ship_code_f.code IN ('").append(faxType).append("'),ship_ct.fax,NULL))");
        if (isVoyageNotification) {
            groupConcatBuilder.append(",IF(consNotifi.send_notification=1,consNotifi.email1,NULL) ");

        }
        groupConcatBuilder.append(")) AS email  ");
        return groupConcatBuilder;
    }

    public String appendJobEmailSubQuery() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT emailE.fileId as fileId,emailE.fileNo AS fileNo,emailE.unitSsId as chargeId,emailE.unitId,emailE.email as email,");
        sb.append("emailE.podUnloc AS agentNo,emailE.tmPodName AS agentName,LOWER(emailE.tmPodEmail) AS agentAcctType,");
        sb.append("emailE.fdUnloc AS destination,emailE.tmFdName AS destinationName,LOWER(emailE.tmFdEmail) AS destinationCountry");
        sb.append(" FROM (SELECT ");
        sb.append("f.id AS fileId,f.file_number AS fileNo,unit_ss.unit_id AS unitId,unit_ss.id as unitSsId,");
        sb.append("termPod.trmnam AS tmPodName,termPod.imports_contact_email AS tmPodEmail,termPod.unlocationcode1 AS podUnloc,");
        sb.append("termFd.trmnam AS tmFdName,termFd.imports_contact_email AS tmFdEmail,termFd.unlocationCode1 AS fdUnloc,");
        return sb.toString();
    }

    public String appendEmailSubQuery(final boolean isVoyageNotification) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("FROM lcl_unit_ss unit_ss ");
        if (isVoyageNotification) {
            sb.append("JOIN lcl_voyage_notification lvf ON lvf.unit_ss_id=unit_ss.id ");
        }
        sb.append("JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id ");
        sb.append("JOIN lcl_file_number f ON book_piece.file_number_id = f.id JOIN lcl_booking lb ON f.id=lb.file_number_id ");
        sb.append("LEFT JOIN cust_contact ship_ct ON ship_ct.acct_no = lb.ship_acct_no LEFT JOIN genericcode_dup ship_code_f ON ship_ct.code_f = ship_code_f.id ");
        sb.append("LEFT JOIN cust_contact cons_ct ON cons_ct.acct_no = lb.cons_acct_no LEFT JOIN genericcode_dup cons_code_f ON cons_ct.code_f = cons_code_f.id ");
        sb.append("LEFT JOIN cust_contact noty_ct ON noty_ct.acct_no = lb.noty_acct_no LEFT JOIN genericcode_dup noty_code_f ON noty_ct.code_f = noty_code_f.id ");
        sb.append(" LEFT JOIN lcl_contact noty2 ON (noty2.id=lb.noty2_contact_id)  LEFT JOIN cust_contact noty2_ct ON (noty2_ct.acct_no=noty2.tp_acct_no) ");
        sb.append(" LEFT JOIN genericcode_dup noty2_code_f ON (noty2_code_f.id=noty2_ct.code_f)  ");
        if (isVoyageNotification) {
            sb.append("JOIN user_details users ON users.user_id=lb.entered_by_user_id ");
            sb.append("LEFT JOIN import_port_configuration impPod ON impPod.schnum=lb.pod_id LEFT JOIN terminal termPod ON termPod.trmnum=impPod.trm_num ");
            sb.append("LEFT JOIN import_port_configuration impFd ON impFd.schnum=lb.fd_id LEFT JOIN terminal termFd ON termFd.trmnum=impFd.trm_num ");
            sb.append("LEFT JOIN lcl_booking_import lbm ON f.id=lbm.file_number_id ");
            sb.append("LEFT JOIN lcl_contact consNotifi ON consNotifi.id=lb.cons_contact_id ");
        }
        return sb.toString();
    }

    public void updateSchedulerStatus(String columnName, Long unitSsId, String status) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE lcl_voyage_notification SET  ").append(columnName).append("='").append(status).append("',");
        if ("MINUTE".equalsIgnoreCase(columnName)) {
            sb.append(" minutescheduler_datetime=SYSDATE() ");
        } else {
            sb.append(" dailyscheduler_datetime=SYSDATE() ");
        }
        //if (unitSsId != null) {
        sb.append("WHERE unit_ss_id=").append(unitSsId);
        //}
        getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public List<ImportsManifestBean> getAllDrEmailsByUnit(final String emailType, final String faxType, boolean isVoyageNotification, final Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT emailE.notyacctNo as notifyAcct,emailE.notyemail as notifyEmail,emailE.consAcctNo as consAcct,emailE.consemail as consEmail, ");
        sb.append("emailE.shipAcctNo as shipAcct,emailE.shipemail as shipEmail FROM ");
        sb.append(" (SELECT ");
        sb.append("noty_ct.acct_no AS notyacctNo,IF(noty_code_f.code IN('E1','E2','E3') ,LOWER(noty_ct.email),IF(noty_code_f.code IN('F1','F2','F3') ,noty_ct.fax,NULL))AS notyemail,");
        sb.append("cons_ct.acct_no AS consAcctNo,IF(cons_code_f.code IN ('E1','E2','E3'),LOWER(cons_ct.email),IF(cons_code_f.code IN ('F1','F2','F3'),cons_ct.fax,NULL))AS consemail,");
        sb.append("ship_ct.acct_no AS shipAcctNo,IF(ship_code_f.code IN ('E1','E2','E3'),LOWER(ship_ct.email),IF(ship_code_f.code IN ('F1','F2','F3'),ship_ct.fax,NULL))AS shipemail ");
        sb.append(appendEmailSubQuery(isVoyageNotification));
        sb.append("WHERE unit_ss.id = ").append(unitSsId).append(" GROUP BY f.id ");
        sb.append(")emailE WHERE emailE.notyemail IS NOT NULL OR emailE.consemail IS NOT NULL OR emailE.shipemail IS NOT NULL");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("notifyAcct", StringType.INSTANCE);
        query.addScalar("notifyEmail", StringType.INSTANCE);
        query.addScalar("consAcct", StringType.INSTANCE);
        query.addScalar("consEmail", StringType.INSTANCE);
        query.addScalar("shipAcct", StringType.INSTANCE);
        query.addScalar("shipEmail", StringType.INSTANCE);
        return query.list();
    }

    public String getNoOfContactFromDR(final Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT GROUP_CONCAT(fn.file_number) AS fileNumber FROM ");
        sb.append("(SELECT  fn.`file_number`,  b.`cons_acct_no`,  b.`noty_acct_no`,");
        sb.append("SUM(IF((cnge.code IN ('E1', 'E2', 'E3') AND cnct.`email` <> '') OR (cnge.code IN ('F1', 'F2', 'F3') AND cnct.`fax` <> ''), 1, 0)) AS cn_count,");
        sb.append("SUM(IF((ntge.code IN ('E1', 'E2', 'E3') AND ntct.`email` <> '') OR (ntge.code IN ('F1', 'F2', 'F3') AND ntct.`fax` <> ''), 1, 0)) AS nt_count");
        sb.append(" FROM lcl_unit_ss uss");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.lcl_unit_ss_id = uss.id ");
        sb.append(" JOIN lcl_booking_piece bp ON bpu.booking_piece_id = bp.id ");
        sb.append(" JOIN lcl_file_number fn ON bp.file_number_id = fn.id ");
        sb.append(" JOIN lcl_booking b ON fn.id = b.file_number_id ");
        sb.append(" LEFT JOIN cust_contact cnct ON (b.cons_acct_no = cnct.acct_no) ");
        sb.append(" LEFT JOIN genericcode_dup cnge ON (cnct.`code_f` = cnge.`id`) ");
        sb.append(" LEFT JOIN cust_contact ntct ON (b.`noty_acct_no` = ntct.`acct_no`)   ");
        sb.append(" LEFT JOIN genericcode_dup ntge ON (ntct.`code_f` = ntge.`id`) WHERE uss.id =").append(unitSsId);
        sb.append(" AND b.booking_type = 'I' GROUP BY fn.`id` )  AS fn ");
        sb.append(" WHERE fn.cn_count = 0 AND fn.nt_count = 0");
        return (String) getCurrentSession().createSQLQuery(sb.toString()).addScalar("fileNumber", StringType.INSTANCE).uniqueResult();
    }

    public String getNonInvoicedCharges(Long unitSsId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  group_concat(distinct fn.`file_number`) as fileNumber ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_unit_ss` uss ");
        queryBuilder.append("  join `lcl_booking_piece_unit` bpu ");
        queryBuilder.append("    on uss.`id` = bpu.`lcl_unit_ss_id`");
        queryBuilder.append("  join `lcl_booking_piece` bp ");
        queryBuilder.append("    on bpu.`booking_piece_id` = bp.id ");
        queryBuilder.append("  join `lcl_file_number` fn ");
        queryBuilder.append("    on bp.`file_number_id` = fn.`id` ");
        queryBuilder.append(" join lcl_booking b ");
        queryBuilder.append(" on b.file_number_id = fn.id ");
        queryBuilder.append("  join `lcl_booking_ac` ac ");
        queryBuilder.append("    on fn.`id` = ac.`file_number_id` ");
        queryBuilder.append("where uss.`id` = :unitSsId ");
        queryBuilder.append(" and b.booking_type != 'T' ");
        queryBuilder.append("  and ac.`ar_bill_to_party` = 'A' ");
        queryBuilder.append("  and ac.`ar_amount` <> 0.00 ");
        queryBuilder.append("  and (");
        queryBuilder.append("    ac.`rels_to_inv` = 0 ");
        queryBuilder.append("    or ac.`sp_reference_no` is null ");
        queryBuilder.append("    or ac.`sp_reference_no` = ''");
        queryBuilder.append("  ) ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("unitSsId", unitSsId);
        query.addScalar("fileNumber", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public String checkTerminalForPodFd(final Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(fn.file_number) AS fileNumber FROM ");
        sb.append(" (SELECT fn.file_number,");
        sb.append(" IF(termPod.trmnum IS NOT NULL AND termPod.trmnum != '', 1, 0 ) AS podTermNo,");
        sb.append(" IF(termFd.trmnum IS NOT NULL AND termFd.trmnum <> '',1,0) AS fdTermNo, ");
        sb.append(" IF(termPod.imports_contact_email IS NOT NULL AND termPod.imports_contact_email <> '',1,0) AS podTermEmail, ");
        sb.append(" IF(termFd.imports_contact_email IS NOT NULL AND termFd.imports_contact_email <> '',1,0) AS fdTermEmail ");
        sb.append(" FROM lcl_unit_ss uss");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.lcl_unit_ss_id = uss.id ");
        sb.append(" JOIN lcl_booking_piece bp ON bpu.booking_piece_id = bp.id ");
        sb.append(" JOIN lcl_file_number fn ON bp.file_number_id = fn.id ");
        sb.append(" JOIN lcl_booking b ON fn.id = b.file_number_id ");
        sb.append(" LEFT JOIN import_port_configuration impPod ON impPod.schnum = b.pod_id  ");
        sb.append(" LEFT JOIN terminal termPod ON termPod.trmnum = impPod.trm_num ");
        sb.append(" LEFT JOIN import_port_configuration impFd ON impFd.schnum = b.fd_id ");
        sb.append(" LEFT JOIN terminal termFd ON termFd.trmnum = impFd.trm_num WHERE uss.id =").append(unitSsId);
        sb.append(" AND b.booking_type = 'I' GROUP BY fn.`id`) AS fn ");
        sb.append(" WHERE (fn.podTermNo = 0 AND fn.podTermNo = 0) OR (fdTermEmail =0 AND podTermEmail = 0) ");
        return (String) getCurrentSession().createSQLQuery(sb.toString()).addScalar("fileNumber", StringType.INSTANCE).uniqueResult();
    }

    public List<ImportsManifestBean> getListOFContactFromDrs(final Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT fn.fileNo as fileNo,");
        sb.append("fn.notifyName,fn.notifyAcct,fn.consigneeName ,fn.consAcct FROM");
        sb.append(" (SELECT  fn.`file_number` as fileNo, b.cons_acct_no as consAcct, cons.acct_name AS consigneeName, b.`noty_acct_no` as notifyAcct,noty.acct_name AS notifyName,");
        sb.append("SUM(IF((cnge.code IN ('E1', 'E2', 'E3') AND cnct.`email` <> '') OR (cnge.code IN ('F1', 'F2', 'F3') AND cnct.`fax` <> ''), 1, 0)) AS cn_count,");
        sb.append("SUM(IF((ntge.code IN ('E1', 'E2', 'E3') AND ntct.`email` <> '') OR (ntge.code IN ('F1', 'F2', 'F3') AND ntct.`fax` <> ''), 1, 0)) AS nt_count");
        sb.append(" FROM lcl_unit_ss uss");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.lcl_unit_ss_id = uss.id ");
        sb.append(" JOIN lcl_booking_piece bp ON bpu.booking_piece_id = bp.id ");
        sb.append(" JOIN lcl_file_number fn ON bp.file_number_id = fn.id ");
        sb.append(" JOIN lcl_booking b ON fn.id = b.file_number_id ");
        sb.append(" LEFT JOIN trading_partner noty ON (b.noty_acct_no = noty.acct_no) ");
        sb.append(" LEFT JOIN cust_contact cnct ON (b.cons_acct_no = cnct.acct_no) ");
        sb.append(" LEFT JOIN genericcode_dup cnge ON (cnct.`code_f` = cnge.`id`) ");
        sb.append(" LEFT JOIN trading_partner cons ON (b.cons_acct_no = cons.acct_no) ");
        sb.append(" LEFT JOIN cust_contact ntct ON (b.`noty_acct_no` = ntct.`acct_no`)   ");
        sb.append(" LEFT JOIN genericcode_dup ntge ON (ntct.`code_f` = ntge.`id`) WHERE uss.id =").append(unitSsId);
        sb.append(" AND b.booking_type = 'I' GROUP BY fn.`id` )  AS fn ");
        sb.append(" WHERE fn.cn_count = 0 AND fn.nt_count = 0");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("notifyName", StringType.INSTANCE);
        query.addScalar("notifyAcct", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("consAcct", StringType.INSTANCE);
        return query.list();
    }

    public Integer getlastDispoUserId(Long unitId) throws Exception {
        String sqlQuery = "SELECT ud.user_id FROM lcl_unit_ss_dispo lusd JOIN user_details ud ON ud.user_id=lusd.entered_by_user_id WHERE unit_id='" + unitId + "' ORDER BY id DESC LIMIT 1";
        Object dispId = getSession().createSQLQuery(sqlQuery).uniqueResult();
        return null != dispId ? (Integer) dispId : null;
    }

    public String buildUnitCostQuery(final Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT ");
        sb.append(" NULL AS blueChargeCode,NULL AS keyChargeCode,null AS chargeCode,0.00 AS arAmount, ");
        sb.append(" NULL AS chargeVendorNo,NULL AS chargeVendorName, ");
        sb.append(" cost.bluescreen_chargecode AS blueCostCode,cost.charge_code AS keyCostCode,");
        sb.append("IF((cost.charge_description IS NULL OR cost.charge_description=''),cost.charge_code,cost.charge_description)  AS costCode,");
        sb.append("lac.ap_amount AS apAmount,tp.acct_no AS costVendorNo,tp.acct_name AS costVendorName ,NULL AS fileStatus, NULL AS bookingType,NULL AS agentBilledFlag ");
        sb.append(" FROM lcl_unit_ss lus ");
        sb.append(" JOIN lcl_ss_ac lac ON (lus.ss_header_id=lac.ss_header_id) ");
        sb.append(" LEFT JOIN gl_mapping cost ON (lac.ap_gl_mapping_id = cost.id) ");
        sb.append(" LEFT JOIN trading_partner tp ON (tp.acct_no=lac.ap_acct_no) ");
        sb.append(" WHERE lus.id = ").append(unitSsId).append(" ) ");
        return sb.toString();
    }

    public List<LclImpProfitLossBean> getUnitCostDetails(final Long unitSsId, String unitFlag) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(buildArInvoiceQuery(String.valueOf(unitSsId), "", unitFlag));
        sb.append("UNION ALL");
        sb.append(buildUnitCostQuery(unitSsId));
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclImpProfitLossBean.class));
        query.addScalar("blueChargeCode", StringType.INSTANCE);
        query.addScalar("keyChargeCode", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("arAmount", DoubleType.INSTANCE);
        query.addScalar("agentBilledFlag", StringType.INSTANCE);
        query.addScalar("chargeVendorNo", StringType.INSTANCE);
        query.addScalar("chargeVendorName", StringType.INSTANCE);
        query.addScalar("blueCostCode", StringType.INSTANCE);
        query.addScalar("keyCostCode", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("apAmount", DoubleType.INSTANCE);
        query.addScalar("costVendorNo", StringType.INSTANCE);
        query.addScalar("costVendorName", StringType.INSTANCE);
        query.addScalar("fileStatus", StringType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        return query.list();
    }

    public List<LclImpProfitLossBean> getDRChargeDetails(final Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(buildArInvoiceQuery("", String.valueOf(fileId), ""));
        sb.append(" UNION ALL ");
        sb.append(buildDrQuery(fileId));
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclImpProfitLossBean.class));
        query.addScalar("blueChargeCode", StringType.INSTANCE);
        query.addScalar("keyChargeCode", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("agentBilledFlag", StringType.INSTANCE);
        query.addScalar("agentFlag", StringType.INSTANCE);
        query.addScalar("arAmount", DoubleType.INSTANCE);
        query.addScalar("chargeVendorNo", StringType.INSTANCE);
        query.addScalar("chargeVendorName", StringType.INSTANCE);
        query.addScalar("blueCostCode", StringType.INSTANCE);
        query.addScalar("keyCostCode", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("apAmount", DoubleType.INSTANCE);
        query.addScalar("costVendorNo", StringType.INSTANCE);
        query.addScalar("costVendorName", StringType.INSTANCE);
        query.addScalar("fileStatus", StringType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("billtoParty", StringType.INSTANCE);
        return query.list();
    }

    public List<LclImpProfitLossBean> getSummaryOfPL(final Long unitSsId, String fileId, String summaryFlag) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(buildArInvoiceQuery(String.valueOf(unitSsId), fileId, summaryFlag));
        sb.append(" UNION ALL ");
        sb.append(buildUnitCostQuery(unitSsId));
        sb.append(" UNION ALL");
        sb.append(buildBookingCostQuery(unitSsId));
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(LclImpProfitLossBean.class));
        query.addScalar("blueChargeCode", StringType.INSTANCE);
        query.addScalar("agentBilledFlag", StringType.INSTANCE);
        query.addScalar("keyChargeCode", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("arAmount", DoubleType.INSTANCE);
        query.addScalar("chargeVendorNo", StringType.INSTANCE);
        query.addScalar("chargeVendorName", StringType.INSTANCE);
        query.addScalar("blueCostCode", StringType.INSTANCE);
        query.addScalar("keyCostCode", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("apAmount", DoubleType.INSTANCE);
        query.addScalar("costVendorNo", StringType.INSTANCE);
        query.addScalar("fileStatus", StringType.INSTANCE);
        query.addScalar("bookingType", StringType.INSTANCE);
        query.addScalar("costVendorName", StringType.INSTANCE);
        return query.list();
    }

    public String buildArInvoiceQuery(String unitSsId, String fileId, String summaryFlag) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("( SELECT ");
        sb.append(" gl.blueCode AS blueChargeCode,gl.chargeCode AS keyChargeCode,gl.chargedesc AS chargeCode, ");
        sb.append(" ared.arAmount , ared.charVendorNo AS chargeVendorNo,ared.charVendorName AS chargeVendorName,");
        sb.append("NULL AS blueCostCode,NULL AS keyCostCode,NULL AS costCode,0.00 AS apAmount, ");
        sb.append("NULL AS costVendorNo,NULL AS costVendorName ,NULL AS fileStatus, NULL AS bookingType ,NULL as agentBilledFlag ");
        if ("".equals(summaryFlag)) {
            sb.append(", 'true' AS agentFlag,");
            sb.append(" NULL AS billtoParty ");
        }

        sb.append(" FROM ");
        sb.append("( SELECT ");
        sb.append("arc.charge_code AS archargeCode,arc.amount AS arAmount,");
        sb.append("ar.customer_number AS charVendorNo,ar.customer_name AS charVendorName  ");
        sb.append(" FROM ar_red_invoice ar ");
        sb.append(" JOIN ar_red_invoice_charges arc ON (arc.ar_red_invoice_id = ar.id) ");
        sb.append(" WHERE ");
        if ("".equals(summaryFlag)) {
            sb.append("ar.file_no IN(").append(fileId).append(") AND ar.screen_name = 'LCLI DR' AND ar.ready_to_post = 'M' ");
        } else if ("Summary".equalsIgnoreCase(summaryFlag)) {
            sb.append("ar.file_no IN(").append(fileId).append(") AND ar.screen_name = 'LCLI DR' AND ar.ready_to_post = 'M' ");
            sb.append("OR (ar.file_no=").append(unitSsId).append(" AND (arc.lcl_dr_number='' OR arc.lcl_dr_number IS NULL) AND ar.screen_name='IMP VOYAGE')");
        } else if ("unitFlag".equalsIgnoreCase(summaryFlag)) {
            sb.append(" ar.file_no=").append(unitSsId).append(" AND ar.ready_to_post = 'M' ");
            sb.append(" AND (arc.lcl_dr_number='' OR arc.lcl_dr_number IS NULL) AND ar.screen_name='IMP VOYAGE' ");

        }
        sb.append(" AND ar.ready_to_post='M') ared ");
        sb.append(" JOIN (SELECT bluescreen_chargecode AS blueCode,charge_code AS chargeCode ,");
        sb.append("IF((charge_description IS NULL OR charge_description=''),charge_code,charge_description)  AS chargedesc  ");
        sb.append(" FROM  gl_mapping  WHERE ");
        sb.append(" transaction_type = 'AR' AND shipment_type = 'LCLI'  GROUP BY charge_code");
        sb.append(") gl ON gl.chargeCode = ared.archargeCode  ) ");
        return sb.toString();
    }

    public String buildDrQuery(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("(SELECT ");
        sb.append(" charge.bluescreen_chargecode AS blueChargeCode,charge.charge_code AS keyChargeCode,");
        sb.append("IF((charge.charge_description IS NULL  OR charge.charge_description = ''), charge.charge_code, charge.charge_description) AS chargeCode,");
        sb.append("lba.ar_amount AS arAmount,");
        sb.append("IF(lba.ar_bill_to_party = 'C',cons.acct_no,IF(lba.ar_bill_to_party = 'A',agent.acct_no,IF(lba.ar_bill_to_party = 'N',noty.acct_no,tprty.acct_no))) AS chargeVendorNo,");
        sb.append("IF(lba.ar_bill_to_party = 'C',cons.acct_name,IF(lba.ar_bill_to_party = 'A',agent.acct_name,IF(lba.ar_bill_to_party = 'N',noty.acct_name,tprty.acct_name))) AS chargeVendorName,");
        sb.append("cost.bluescreen_chargecode AS blueCostCode,cost.charge_code AS keyCostCode,");
        sb.append("IF((cost.charge_description IS NULL  OR cost.charge_description = ''),cost.charge_code,cost.charge_description) AS costCode,");
        sb.append("IF(lba.ap_amount IS NULL,0.00,lba.ap_amount) AS apAmount, ");
        sb.append("tpcost.acct_no AS costVendorNo,tpcost.acct_name AS costVendorName ,NULL AS fileStatus, NULL AS bookingType ,  ");
        sb.append(" IF( (lba.`sp_reference_no` IS NOT NULL AND lba.`ar_bill_to_party`='A' AND agent.ready_to_post !='M' ) OR ( lba.`sp_reference_no` IS  NULL AND lba.`ar_bill_to_party` = 'A'),'true',NULL) AS agentBilledFlag,");
        sb.append(" IF( (lba.`sp_reference_no` IS NOT NULL AND lba.`ar_bill_to_party`='A'), 'true',NULL ) AS agentFlag, lba.ar_bill_to_party AS billtoParty ");
        sb.append(" FROM lcl_booking_ac lba ");
        sb.append(" JOIN lcl_booking lb ON lb.file_number_id=lba.file_number_id LEFT JOIN lcl_file_number lfn ON lb.file_number_id=lfn.id");
        sb.append(" LEFT JOIN gl_mapping cost ON (lba.ap_gl_mapping_id = cost.id) LEFT JOIN gl_mapping charge ON (lba.ar_gl_mapping_id = charge.id) ");
        sb.append(" LEFT JOIN trading_partner tpcost ON tpcost.acct_no=lba.sp_acct_no  LEFT JOIN trading_partner cons ON lb.cons_acct_no = cons.acct_no ");
        sb.append(" LEFT JOIN trading_partner tprty ON lb.third_party_acct_no = tprty.acct_no  LEFT JOIN trading_partner noty ON lb.noty_acct_no = noty.acct_no ");
        sb.append(" LEFT JOIN trading_partner agent ON lb.sup_acct_no = agent.acct_no ");
        sb.append(" LEFT JOIN (SELECT ar.invoice_number,arc.lcl_dr_number,IF(ar.`ready_to_post` IS NULL, '',ar.`ready_to_post` )AS ready_to_post FROM ar_red_invoice ar LEFT JOIN ar_red_invoice_charges arc");
        sb.append(" ON arc.ar_red_invoice_id = ar.id GROUP BY ar.invoice_number ) AS agent ON agent.invoice_number=lba.sp_reference_no AND agent.lcl_dr_number LIKE CONCAT('%',lfn.file_number,'%')");
        sb.append(" WHERE lba.file_number_id  = ").append(fileId);
        sb.append(" AND ( lba.ap_amount !=0.00 OR lba.ar_amount !=0.00)) ");
        return sb.toString();
    }

    public String buildBookingCostQuery(final Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" (SELECT ");
        sb.append(" charge.bluescreen_chargecode AS blueChargeCode,charge.charge_code as keyChargeCode,");
        sb.append("IF((charge.charge_description IS NULL OR charge.charge_description=''),charge.charge_code,charge.charge_description)  AS chargeCode,");
        sb.append("SUM(IF( lfn.`status` !='M'  OR (lba.`sp_reference_no` IS NOT NULL AND lba.`ar_bill_to_party` = 'A'  AND agent.ready_to_post != 'M') OR ( lba.`sp_reference_no` IS  NULL AND lba.`ar_bill_to_party` = 'A') ,0.0,lba.ar_amount)) AS arAmount,NULL AS chargeVendorNo,NULL AS chargeVendorName, ");
        sb.append(" cost.bluescreen_chargecode AS blueCostCode,cost.charge_code AS keyCostCode,");
        sb.append("IF((cost.charge_description IS NULL OR cost.charge_description=''),cost.charge_code,cost.charge_description)  AS costCode,");
        sb.append("SUM(IF(lba.ap_amount IS NULL,0.00,lba.ap_amount)) AS apAmount,NULL AS costVendorNo,NULL AS costVendorName ,lfn.`status` AS fileStatus ,lb.`booking_type` AS bookingType,NULL AS agentBilledFlag ");
        sb.append(" FROM lcl_unit_ss lus ");
        sb.append(" JOIN lcl_booking_piece_unit lbpu ON (lbpu.lcl_unit_ss_id=lus.id) JOIN lcl_booking_piece lbp ON (lbp.id=lbpu.booking_piece_id) ");
        sb.append(" JOIN lcl_booking_ac lba ON (lba.file_number_id=lbp.file_number_id) ");
        sb.append("  LEFT JOIN lcl_booking lb ON lb.`file_number_id`=lbp.file_number_id");
        sb.append("  LEFT JOIN lcl_file_number lfn ON (lbp.file_number_id=lfn.`id`) ");
        sb.append("  LEFT JOIN gl_mapping cost ON (lba.ap_gl_mapping_id = cost.id)");
        sb.append(" LEFT JOIN gl_mapping charge ON (lba.ar_gl_mapping_id = charge.id) ");
        sb.append(" LEFT JOIN (SELECT ar.invoice_number,arc.lcl_dr_number,IF(ar.`ready_to_post` IS NULL,'',ar.`ready_to_post`) AS ready_to_post ");
        sb.append(" FROM ar_red_invoice ar LEFT JOIN ar_red_invoice_charges arc ON arc.ar_red_invoice_id = ar.id GROUP BY ar.invoice_number ) AS agent ");
        sb.append(" ON agent.invoice_number = lba.sp_reference_no AND agent.lcl_dr_number LIKE CONCAT('%',lfn.file_number,'%')");
        sb.append(" WHERE lus.id = ").append(unitSsId).append(" AND( lba.ap_amount IS NOT NULL OR lba.ar_amount IS NOT NULL ");
        sb.append(" OR lba.ap_amount <> 0.00 OR  lba.ar_amount <> 0.00) GROUP BY charge.charge_code,cost.charge_code) ");
        return sb.toString();
    }

    public Integer findNoOfTranshipmentFilesInVoyage(Long unitSSId) throws Exception {
        BigInteger count = new BigInteger("0");
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(*) FROM lcl_booking_piece_unit lbpu LEFT JOIN lcl_booking_piece lbp ON lbpu.`booking_piece_id`=lbp.`id`");
        sb.append(" LEFT JOIN lcl_booking lb ON lbp.`file_number_id`=lb.`file_number_id`");
        sb.append(" WHERE lcl_unit_ss_id  = ").append(unitSSId);
        sb.append(" AND lb.`booking_type`='T'");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        Object o = query.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public Boolean getUnitNoCount(Long unitId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT IF(COUNT(*) > 1, 'true', 'false') as result FROM lcl_unit_ss lus ");
        sb.append(" WHERE lus.unit_id  = :unitId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setLong("unitId", unitId);
        String result = (String) query.uniqueResult();
        return null != result ? Boolean.valueOf(result) : false;
    }

    public List<BlueScreenChargesBean> getDRChargeFRomBlueScreenDB(String fileNo, String fileNoList) throws Exception {
        String blueScreenDb = LoadLogisoftProperties.getProperty("elite.database.name");
        String fileTemp = null;
        if (fileNo == null) {
            fileTemp = fileNoList;
        } else if (fileNoList == null) {
            fileTemp = fileNo;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT g1.Charge_code AS chg01,g2.Charge_code AS chg02 ,g3.Charge_code AS chg03,g4.Charge_code AS chg04,");
        sb.append(" g5.Charge_code AS chg05,g6.Charge_code AS chg06,g7.Charge_code AS chg07,g8.Charge_code AS chg08,");
        sb.append(" g9.Charge_code AS chg09,g10.Charge_code AS chg10,g11.Charge_code AS chg11,g12.Charge_code AS chg12,");
        sb.append(" g1.`bluescreen_chargecode` AS bchg01,g2.`bluescreen_chargecode` AS bchg02,g3.`bluescreen_chargecode` AS bchg03,");
        sb.append(" g4.`bluescreen_chargecode` AS bchg04,g5.`bluescreen_chargecode` AS bchg05,g6.`bluescreen_chargecode` AS bchg06,");
        sb.append(" g7.`bluescreen_chargecode` AS bchg07,g8.`bluescreen_chargecode` AS bchg08,g9.`bluescreen_chargecode` AS bchg09,");
        sb.append(" g10.`bluescreen_chargecode` AS bchg10,g11.`bluescreen_chargecode` AS bchg11,g12.`bluescreen_chargecode` AS bchg12,");
        sb.append(" amt01,amt02,amt03,amt04,amt05,amt06,amt07,amt08,amt09,amt10,amt11,amt12");
        sb.append(" FROM  ").append(blueScreenDb).append(" .histry h JOIN ").append(blueScreenDb).append(".bldr b ");
        sb.append(" ON b.`drnum`=h.`drnum` AND b.`blterm`=h.`blterm` AND b.`drterm`=h.`drterm` AND b.`port`=h.`port`");
        sb.append(" LEFT JOIN lcl_file_number l ON l.`file_number` = b.`drnum` ");
        sb.append(" LEFT JOIN lcl_booking lb ON l.id=lb.`file_number_id` ");
        sb.append(" LEFT JOIN gl_mapping g1 ON g1.bluescreen_chargecode=chg01 AND g1.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g2 ON g2.bluescreen_chargecode=chg02 AND g2.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g3 ON g3.bluescreen_chargecode=chg03 AND g3.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g4 ON g4.bluescreen_chargecode=chg04 AND g4.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g5 ON g5.bluescreen_chargecode=chg05 AND g5.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g6 ON g6.bluescreen_chargecode=chg06 AND g6.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g7 ON g7.bluescreen_chargecode=chg07 AND g7.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g8 ON g8.bluescreen_chargecode=chg08 AND g8.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g9 ON g9.bluescreen_chargecode=chg09 AND g9.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g10 ON g10.bluescreen_chargecode=chg10 AND g10.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g11 ON g11.bluescreen_chargecode=chg11 AND g11.`subLedger_code`='AR-LCLI'");
        sb.append(" LEFT JOIN gl_mapping g12 ON g12.bluescreen_chargecode=chg12 AND g12.`subLedger_code`='AR-LCLI'");
        sb.append(" WHERE l.id IN ( ").append(fileTemp);
        sb.append(" ) AND lb.`booking_type`='T' GROUP BY b.drnum");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setResultTransformer(Transformers.aliasToBean(BlueScreenChargesBean.class));
        query.addScalar("chg01", StringType.INSTANCE);
        query.addScalar("chg02", StringType.INSTANCE);
        query.addScalar("chg03", StringType.INSTANCE);
        query.addScalar("chg04", StringType.INSTANCE);
        query.addScalar("chg05", StringType.INSTANCE);
        query.addScalar("chg06", StringType.INSTANCE);
        query.addScalar("chg07", StringType.INSTANCE);
        query.addScalar("chg08", StringType.INSTANCE);
        query.addScalar("chg09", StringType.INSTANCE);
        query.addScalar("chg10", StringType.INSTANCE);
        query.addScalar("chg11", StringType.INSTANCE);
        query.addScalar("chg12", StringType.INSTANCE);
        query.addScalar("amt01", StringType.INSTANCE);
        query.addScalar("amt02", StringType.INSTANCE);
        query.addScalar("amt03", StringType.INSTANCE);
        query.addScalar("amt04", StringType.INSTANCE);
        query.addScalar("amt05", StringType.INSTANCE);
        query.addScalar("amt06", StringType.INSTANCE);
        query.addScalar("amt07", StringType.INSTANCE);
        query.addScalar("amt08", StringType.INSTANCE);
        query.addScalar("amt09", StringType.INSTANCE);
        query.addScalar("amt10", StringType.INSTANCE);
        query.addScalar("amt11", StringType.INSTANCE);
        query.addScalar("amt12", StringType.INSTANCE);
        query.addScalar("bchg01", StringType.INSTANCE);
        query.addScalar("bchg02", StringType.INSTANCE);
        query.addScalar("bchg03", StringType.INSTANCE);
        query.addScalar("bchg04", StringType.INSTANCE);
        query.addScalar("bchg05", StringType.INSTANCE);
        query.addScalar("bchg06", StringType.INSTANCE);
        query.addScalar("bchg07", StringType.INSTANCE);
        query.addScalar("bchg08", StringType.INSTANCE);
        query.addScalar("bchg09", StringType.INSTANCE);
        query.addScalar("bchg10", StringType.INSTANCE);
        query.addScalar("bchg11", StringType.INSTANCE);
        query.addScalar("bchg12", StringType.INSTANCE);
        return (List<BlueScreenChargesBean>) query.list();
    }

    public String showAgentFileNo() {
        String Query = "SELECT file.id AS fileId, file.file_number AS fileNo, agent.acct_no AS agentNo,agent.acct_name AS agentName "
                + "FROM lcl_unit_ss unit_ss JOIN lcl_booking_piece_unit unit_pieces ON unit_pieces.lcl_unit_ss_id = unit_ss.id "
                + "JOIN lcl_booking_piece book_piece ON unit_pieces.booking_piece_id = book_piece.id JOIN lcl_unit unit "
                + "  ON unit_ss.unit_id = unit.id JOIN lcl_file_number FILE ON book_piece.file_number_id = file.id JOIN lcl_booking b "
                + "  ON file.id = b.file_number_id JOIN lcl_unit_ss_imports lussi ON lussi.unit_id = unit.id JOIN trading_partner agent "
                + "  ON lussi.origin_acct_no = agent.acct_no WHERE unit_ss.id =";
        return Query;
    }

    public List<ImportsManifestBean> showDrLevelAgentInvoce(String unitssId) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append(" SELECT chg.id As fileId,fileNo AS fileno,gl.Charge_code AS chargeCode,  agentNo AS agentNo,  agentName AS agentName,");
        qBuilder.append(" IF(chg.ar_bill_to_party = 'A' AND chg.rels_to_inv = 1, chg.ar_amount, 0 )AS agentrelInv,");
        qBuilder.append(" IF(chg.ar_bill_to_party = 'A' AND chg.rels_to_inv = 0, chg.ar_amount, 0 )AS agentrelnotInv ");
        qBuilder.append(" FROM ( ").append(showAgentFileNo()).append(unitssId).append(") f");
        qBuilder.append(" JOIN lcl_booking_ac chg ON f.fileId = chg.file_number_id JOIN gl_mapping gl ON gl.id=chg.ar_gl_mapping_id ");
        qBuilder.append(" WHERE chg.ar_amount > 0.00 AND chg.ar_bill_to_party = 'A' AND chg.rels_to_inv = 1");
        qBuilder.append(" AND (chg.sp_reference_no IS NULL  OR chg.sp_reference_no = '' ) ");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("agentrelInv", StringType.INSTANCE);
        query.addScalar("agentrelnotInv", StringType.INSTANCE);
        return query.list();
    }

    public List<ImportsManifestBean> drAgentInvoiceCharges(String chargeId) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        SystemRulesDAO systemRulesDAO = new SystemRulesDAO();
        String ruleName = systemRulesDAO.getSystemRulesByCode(COMPANY_CODE);
        qBuilder.append(" SELECT lc.id AS chargeId,  lf.file_number AS fileNo,  lb.sup_acct_no AS agentNo,   glmap.Charge_code AS chargeCode, ");
        qBuilder.append(" lc.ar_amount AS totalCharges ,lb.billing_terminal As billingTerminal, IF( ad.Account IS NOT NULL,    ad.account, '') AS glAccount  ");
        qBuilder.append(" FROM  lcl_booking_ac lc JOIN gl_mapping glmap  ON glmap.id = lc.ar_gl_mapping_id ");
        qBuilder.append(" JOIN lcl_file_number lf ON lf.id=lc.file_number_id  JOIN lcl_booking lb ON lb.file_number_id=lf.id ");
        qBuilder.append(" LEFT JOIN terminal_gl_mapping term  ON  term.terminal =lb.billing_terminal LEFT JOIN account_details ad ");
        qBuilder.append(" ON ad.Account = CONCAT_WS('-','").append(ruleName).append("', glmap.GL_Acct,LPAD(IF(glmap.derive_yn != 'N' AND glmap.derive_yn != 'F' ");
        qBuilder.append(" AND glmap.suffix_value IN ('B', 'L', 'D', 'F'),IF(glmap.suffix_value = 'B',term.lcl_import_billing,");
        qBuilder.append(" IF(glmap.suffix_value = 'L',term.lcl_import_loading,'')),glmap.suffix_value), 2,'0')) ");
        qBuilder.append(" WHERE lc.id IN(").append(chargeId).append(")");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("chargeId", LongType.INSTANCE);
        query.addScalar("fileNo", StringType.INSTANCE);
        query.addScalar("agentNo", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("totalCharges", DoubleType.INSTANCE);
        query.addScalar("billingTerminal", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        return query.list();
    }

    public Object[] getBlManifestDetailsByFileId(String fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT lu.unit_no,lclss.schedule_no,luss.modified_datetime,user_det.login_name,luss.unit_id,luss.ss_header_id,luss.su_heading_note,lsd.sp_reference_no,lsd.std,luss.cob,u.id FROM lcl_unit_ss luss");
        queryBuilder.append(" JOIN lcl_unit lu ON luss.unit_id = lu.id").append(" JOIN lcl_ss_header lclss ON lclss.id = luss.ss_header_id").append(" JOIN lcl_ss_detail lsd  ON  lsd.ss_header_id = lclss.id ");
        queryBuilder.append(" LEFT JOIN lcl_booking_piece_unit u ON u.lcl_unit_ss_id = luss.id");
        queryBuilder.append(" LEFT JOIN lcl_booking_piece p ON u.booking_piece_id = p.id");
        queryBuilder.append(" LEFT JOIN lcl_file_number FILE ON p.file_number_id = FILE.id");
        queryBuilder.append(" JOIN user_details user_det ON luss.modified_by_user_id = user_det.user_id");
        queryBuilder.append(" WHERE file.id =").append(fileNumberId);
        queryBuilder.append(" ORDER BY lclss.schedule_no DESC LIMIT 1");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object[] obj = (Object[]) queryObject.uniqueResult();
        return obj;
    }

    public String validateDrHasConsigneeOrNot(Long unitSsId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  group_concat(fn.`file_number`) as fileNumbers ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_unit_ss` us");
        queryBuilder.append("   join `lcl_booking_piece_unit` bpu");
        queryBuilder.append("     on (us.`id` = bpu.`lcl_unit_ss_id`)");
        queryBuilder.append("   join `lcl_booking_piece` bp");
        queryBuilder.append("     on (bpu.`booking_piece_id` = bp.`id`)");
        queryBuilder.append("   join `lcl_file_number` fn");
        queryBuilder.append("     on (bp.`file_number_id` = fn.`id`)");
        queryBuilder.append("   join `lcl_booking` bk");
        queryBuilder.append("     on (");
        queryBuilder.append("       fn.`id` = bk.`file_number_id`");
        queryBuilder.append("       and bk.`cons_acct_no` is null");
        queryBuilder.append("     ) ");
        queryBuilder.append("where us.`id` = :unitSsId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("unitSsId", unitSsId);
        query.addScalar("fileNumbers", StringType.INSTANCE);
        return (String) query.uniqueResult();
    }

    public List getConcatenatedFileIdsForExport(String unitSsId, String bktype) throws Exception {
        String[] returnValues = new String[3];
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT fn.id as fileId, ");
        sb.append(" UnLocationGetNameStateCntryByID(lb.`pod_id`) AS destination ,lb.`pod_id` AS curLocId ");
        sb.append(" FROM lcl_booking_piece_unit bpu JOIN lcl_booking_piece bp ON bpu.booking_piece_id = bp.id ");
        sb.append(" JOIN lcl_file_number fn ON bp.file_number_id=fn.id JOIN lcl_booking lb ON lb.file_number_id = fn.id ");
        sb.append(" JOIN lcl_booking_dispo bd ON bd.`file_number_id` = fn.`id` ");
        sb.append(" JOIN disposition d ON d.`id` = (SELECT bd.`disposition_id` FROM  lcl_booking_dispo  bd  ");
        sb.append(" WHERE  bd.`file_number_id` = fn.`id` ORDER BY bd.`id` DESC LIMIT 1 ) AND d.`elite_code` = 'OBKG'");
        sb.append(" WHERE bpu.lcl_unit_ss_id =:unitSsId  ");
        sb.append(" AND lb.booking_type=:bktype ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setString("unitSsId", unitSsId);
        queryObject.setString("bktype", bktype);
        return queryObject.list();
    }

    public List getFileNumberForUnit(final Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT fn.`file_number` FROM lcl_unit_ss uss JOIN lcl_booking_piece_unit bpu ON uss.id =bpu.lcl_unit_ss_id  ");
        sb.append(" JOIN lcl_booking_piece bp ON bp.id  = bpu.booking_piece_id JOIN lcl_file_number fn ON fn.id  = bp.file_number_id ");
        sb.append("JOIN lcl_booking b ON fn.id = b.file_number_id WHERE uss.id = ").append(unitSsId);
        sb.append(" AND b.booking_type = 'I' ORDER BY fn.`file_number` DESC");
        return (getCurrentSession().createSQLQuery(sb.toString())).list();
    }

    public String getChargeCodeNotExistInGlForFileNumber(String fileNumber, String transactionType) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT GROUP_CONCAT(DISTINCT gl.charge_code) AS charge_Code ");
        sb.append(" FROM lcl_booking_ac bc JOIN lcl_file_number fn ON fn.id= bc.file_number_id ");
        sb.append(" LEFT JOIN gl_mapping gl ON gl.id = bc.ar_gl_mapping_id ");
        sb.append(" WHERE bc.ar_amount > 0.0 AND (gl.transaction_type != '").append(transactionType).append("' ");
        sb.append(" OR gl.shipment_type != 'LCLI' ) AND bc.ar_bill_to_party !='A' AND fn.file_number = '");
        sb.append(fileNumber).append("' ");
        return (String) (getCurrentSession().createSQLQuery(sb.toString())).uniqueResult();
    }

    public List getAccountForChargeCodeFromGlAndTerminal(String fileNumber) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT  CONCAT(sr.`rule_name`, '-', gl.`GL_Acct`, '-',LPAD( IF( gl.derive_yn = 'N', gl.suffix_value, if(gl.suffix_value = 'B', tm.`lcl_import_billing`, IF( ");
        sb.append(" gl.`suffix_value` = 'L',tm.`lcl_import_loading`,tm.terminal))),2,'0')) AS account,gl.charge_code FROM lcl_booking_ac bc ");
        sb.append(" JOIN lcl_file_number fn ON fn.id = bc.file_number_id JOIN gl_mapping gl ON gl.id = bc.ar_gl_mapping_id JOIN lcl_booking lcl ");
        sb.append(" ON fn.id = lcl.file_number_id JOIN terminal_gl_mapping tm ON tm.terminal = lcl.billing_terminal, `system_rules` sr ");
        sb.append(" WHERE bc.ar_amount > 0.0 AND bc.ar_bill_to_party != 'A' AND fn.file_number = '").append(fileNumber);
        sb.append("' AND sr.`rule_code` = 'CompanyCode'");
        return getCurrentSession().createSQLQuery(sb.toString()).list();
    }

    public boolean checkAccountExistInAccountDetailsForAccount(String account) throws Exception {
        StringBuilder sb = new StringBuilder();
        BigInteger count = new BigInteger("0");
        sb.append("SELECT COUNT(*) FROM account_details ac WHERE ac.account ='").append(account).append("'");
        Object object = getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        if (object != null) {
            count = (BigInteger) object;
        }
        return count.intValue() > 0 ? true : false;
    }

    public Object[] getBillingTypeByUnit(String unitSsId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT lus.prepaid_collect,w.commodityno FROM lcl_unit_ss lus  ");
        queryBuilder.append(" JOIN lcl_unit_ss_imports lusm ON ");
        queryBuilder.append(" (lusm.unit_id=lus.unit_id AND lusm.ss_header_id=lus.ss_header_id) ");
        queryBuilder.append(" LEFT JOIN warehouse w ON w.id=lusm.cfs_warehouse_id ");
        queryBuilder.append("WHERE lus.id=:unitSsId ");
        SQLQuery queryObject = getSession().createSQLQuery(queryBuilder.toString());
        queryObject.setParameter("unitSsId", unitSsId);
        Object[] obj = (Object[]) queryObject.uniqueResult();
        return obj;
    }

    public String[] getBkgLinkedByDetails(String fileId) throws Exception {
        String[] voyageDetails = new String[2];
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lsh.schedule_no,lu.unit_no FROM lcl_booking_piece lbp ");
        queryStr.append(" JOIN lcl_booking_piece_unit lbpu ON lbpu.booking_piece_id=lbp.id ");
        queryStr.append("  JOIN lcl_unit_ss lus ON lus.id=lbpu.lcl_unit_ss_id ");
        queryStr.append(" JOIN lcl_unit lu ON lu.id=lus.unit_id ");
        queryStr.append(" JOIN lcl_ss_header lsh ON lsh.id=lus.ss_header_id and lsh.service_type <> 'I'");
        queryStr.append(" WHERE lbp.file_number_id=:fileId");
        queryStr.append(" GROUP BY lbp.file_number_id");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setString("fileId", fileId);
        List voyageDetailsList = queryObject.list();
        if (voyageDetailsList != null && voyageDetailsList.size() > 0) {
            Object[] fdRemarksObj = (Object[]) voyageDetailsList.get(0);
            if (fdRemarksObj[0] != null && !fdRemarksObj[0].toString().trim().equals("")) {
                voyageDetails[0] = fdRemarksObj[0].toString();
            } else {
                voyageDetails[0] = "";
            }
            if (fdRemarksObj[1] != null && !fdRemarksObj[1].toString().trim().equals("")) {
                voyageDetails[1] = fdRemarksObj[1].toString();
            } else {
                voyageDetails[1] = "";
            }
        }
        return (String[]) voyageDetails;
    }

    public String[] isValidateManifestFiles(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(IF(lf.state <> 'BL' ,lf.`file_number`,null)) AS data1, ");
        sb.append(" GROUP_CONCAT(IF(lf.state = 'BL' AND bl.`posted_by_user_id` IS NULL ,lf.`file_number`,null)) AS data2 ");
        sb.append(" FROM (SELECT DISTINCT bp.`file_number_id`  AS fileId FROM lcl_booking_piece bp ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id` WHERE bpu.`lcl_unit_ss_id` =:unitSsId) f ");
        sb.append(" JOIN lcl_file_number lf ON lf.id =  f.fileId LEFT JOIN lcl_bl bl  ");
        sb.append(" ON bl.`file_number_id` = getHouseBLForConsolidateDr(f.fileId)  ");
        sb.append(" WHERE lf.state <> 'BL' OR bl.`posted_by_user_id` IS NULL ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("unitSsId", unitSsId);
        List fileNumberList = query.list();
        String[] data = new String[2];
        if (fileNumberList != null && fileNumberList.size() > 0) {
            Object[] fileNumber = (Object[]) fileNumberList.get(0);
            if (fileNumber[0] != null && !fileNumber[0].toString().trim().equals("")) {
                data[0] = fileNumber[0].toString();
            }
            if (fileNumber[1] != null && !fileNumber[1].toString().trim().equals("")) {
                data[1] = fileNumber[1].toString();
            }
        }
        return data;
    }

    public String[] isValidateTermsToDoBLAndRateType(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  GROUP_CONCAT(fn.fileNo)AS FileNum  FROM   ");
        sb.append(" (SELECT  DISTINCT f.id AS fileId,  f.file_number AS fileNo, bl.`billing_terminal`  AS blterminal, bl.rates_from_terminal AS  ratesterminal, IF(bl.`rate_type` = 'R','Y',bl.`rate_type`) AS rateType, ");
        sb.append("  (SELECT actyon FROM terminal WHERE trmnum=bl.billing_terminal) AS billingTerminal_actyon, ");
        sb.append(" (SELECT actyon FROM terminal  WHERE trmnum=bl.rates_from_terminal) AS ratesfrom_actyon   ");
        sb.append("   FROM  lcl_booking_piece bp  JOIN lcl_file_number f ON (bp.file_number_id = f.id AND f.state = 'BL') ");
        sb.append("   JOIN lcl_bl bl ON bl.file_number_id = getHouseBLForConsolidateDr(f.id) ");
        sb.append("   JOIN lcl_booking_piece_unit bpu    ON bpu.`booking_piece_id` = bp.`id`   ");
        sb.append(" WHERE bpu.`lcl_unit_ss_id` =:unitSsId ) AS fn  ");
        sb.append("  WHERE  IF (fn.rateType='F' , fn.rateType NOT IN (fn.billingTerminal_actyon,fn.ratesfrom_actyon), fn.rateType NOT IN (fn.billingTerminal_actyon) )  ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("unitSsId", unitSsId);
        List fileNumberList = query.list();
        String[] data = new String[3];
        if (fileNumberList != null && fileNumberList.size() > 0 && fileNumberList.get(0) != null) {
            data[0] = fileNumberList.get(0).toString();
            data[1] = "alert";
        }
        return data;
    }

    public void updateStopOffsForDomestic(String headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" UPDATE lcl_unit_ss lus JOIN lcl_ss_header lsh  ON lsh.id = lus.`ss_header_id`  ");
        sb.append(" JOIN lcl_unit lu ON lu.id = lus.`unit_id` SET stop_off = 0 WHERE lsh.id =:headerId ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setParameter("headerId", headerId);
        query.executeUpdate();
    }

    public LclUnitSs getUnitSsByVoyageUnitNo(String unitNo, String voyageNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" From LclUnitSs lus where lus.lclUnit.unitNo =?0 and lus.lclSsHeader.scheduleNo =?1");
        Query query = getCurrentSession().createQuery(sb.toString());
        query.setParameter("0", unitNo);
        query.setParameter("1", voyageNo);
        return (LclUnitSs) query.setMaxResults(1).uniqueResult();
    }

    public Object[] exportVoyageUnitValues(String headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT CONCAT(lsh.billing_trmnum, '-',headorg.un_loc_code, '-', headdest.un_loc_code, '-',lsh.schedule_no), ");
        sb.append(" lu.unit_no,  vessel.code,  vessel.codedesc,  ussm.masterbl AS masterBl,  ss_detail.sta AS sailDate, ");
        sb.append(" CONCAT(headorg.un_loc_code,'-',headdest.un_loc_code,'-',lsh.schedule_no) FROM lcl_ss_header lsh  LEFT JOIN lcl_unit_ss luss ON  luss.ss_header_id = lsh.id ");
        sb.append(" LEFT JOIN lcl_unit lu ON lu.`id` = luss.`unit_id` LEFT JOIN lcl_ss_detail ss_detail ");
        sb.append(" ON lsh.id = ss_detail.ss_header_id AND ss_detail.trans_mode = 'V'  ");
        sb.append(" LEFT JOIN trading_partner steamshipline ON steamshipline.acct_no = ss_detail.sp_acct_no  ");
        sb.append(" LEFT JOIN genericcode_dup vessel ON ss_detail.sp_reference_name = vessel.codedesc  ");
        sb.append(" AND vessel.codetypeid LEFT JOIN codetype vessel_type ON vessel.codetypeid = vessel_type.codetypeid   ");
        sb.append(" AND vessel_type.description = 'Vessel Codes'  LEFT JOIN lcl_unit_ss_manifest ussm ON lsh.id = ussm.ss_header_id  ");
        sb.append(" JOIN un_location headorg ON headorg.id = lsh.origin_id  JOIN un_location headdest ON headdest.id = lsh.destination_id   ");
        sb.append("  WHERE lsh.id =").append(headerId);
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        return (Object[]) query.setMaxResults(1).uniqueResult();
    }

    public String getUnitNameCount(String lclHeaderUnit) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT   GROUP_CONCAT(unit.unit_no) AS unit FROM lcl_unit unit LEFT JOIN lcl_unit_ss lss ON lss.`unit_id` = unit.id ");
        sb.append(" LEFT JOIN  lcl_ss_header lsh ON lsh.id= lss.`ss_header_id` WHERE lsh.`id`=").append(lclHeaderUnit);
        return (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public boolean isUnitContainFile(String unitSsId) throws Exception {
        String sb = " select if(count(*)>0,true,false) as result  from lcl_unit_ss lus join lcl_booking_piece_unit bu on bu.lcl_unit_ss_id = lus.id join lcl_booking_piece b "
                + " on b.id= bu.booking_piece_id join lcl_file_number f on f.id = b.file_number_id where lus.id =:unitSsId";
        SQLQuery query = getCurrentSession().createSQLQuery(sb);
        query.setParameter("unitSsId", unitSsId);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public ExportVoyageSearchModel getPickedVoyageByVessel(Long fileId, String moduleName) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT lus.id as unitSsId,DATE_FORMAT(lsd.std ,'%d-%b-%Y')AS etaSailDate,lsh.schedule_no as scheduleNo FROM lcl_booking_piece lbp ");
        queryStr.append(" JOIN lcl_booking_piece_unit lbpu ON lbpu.booking_piece_id=lbp.id ");
        queryStr.append(" JOIN lcl_unit_ss lus ON lus.id=lbpu.lcl_unit_ss_id");
        queryStr.append(" JOIN lcl_ss_header lsh ON lsh.id=lus.ss_header_id");
        queryStr.append(" JOIN lcl_ss_detail lsd on lsd.ss_header_id=lsh.id ");
        queryStr.append(" and lsd.id=(select ls.id from lcl_ss_detail ls where ls.ss_header_id = lsh.id order by ls.id desc limit 1)");
        queryStr.append(" WHERE lbp.file_number_id=:fileId");
        if ("E".equalsIgnoreCase(moduleName)) {
            queryStr.append(" AND lsh.service_type IN ('E','C') ");
        } else {
            queryStr.append(" AND lsh.service_type='I'");
        }
        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("fileId", fileId);
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("unitSsId", StringType.INSTANCE);
        query.addScalar("etaSailDate", StringType.INSTANCE);
        query.addScalar("scheduleNo", StringType.INSTANCE);
        List<ExportVoyageSearchModel> pickedList = query.list();
        if (pickedList != null && !pickedList.isEmpty()) {
            return pickedList.get(0);
        }
        return null;
    }

    public List getFileNumberForExportVoyage(final Long unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct fn.id FROM lcl_unit_ss uss JOIN lcl_booking_piece_unit bpu ON uss.id =bpu.lcl_unit_ss_id  ");
        sb.append(" JOIN lcl_booking_piece bp ON bp.id  = bpu.booking_piece_id JOIN lcl_file_number fn ON fn.id  = bp.file_number_id ");
        sb.append("JOIN lcl_booking b ON fn.id = b.file_number_id WHERE uss.id = ").append(unitSsId);
        sb.append(" AND b.booking_type in ('E','T') ORDER BY fn.`file_number` DESC");
        return (getCurrentSession().createSQLQuery(sb.toString())).list();
    }

    public String getPreviousDispoOfFileNumber(Long fileId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT distinct (SELECT d.elite_code FROM lcl_booking_dispo bd JOIN disposition d ON d.id = bd.disposition_id  ");
        sb.append(" WHERE bd.file_number_id = lb.file_number_id order by bd.id desc limit 1,1) FROM lcl_unit_ss lu   ");
        sb.append(" JOIN lcl_booking_piece_unit bu ON bu.lcl_unit_ss_id = lu.id JOIN lcl_booking_piece lb ON lb.id = bu.booking_piece_id  ");
        sb.append(" WHERE lb.file_number_id = ").append(fileId);
        return (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String[] getWareHouseFileId(String unitSsId) throws Exception {
        String[] fileDetails = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(GROUP_CONCAT(DISTINCT fn.id) USING utf8)as fileId,CONVERT(GROUP_CONCAT(DISTINCT CONCAT('IMP-',fn.file_number)) USING utf8)as fileNumber FROM lcl_booking_piece_unit bpu LEFT JOIN lcl_booking_piece bp ON bpu.booking_piece_id=bp.id");
        sb.append(" LEFT JOIN lcl_file_number fn ON bp.file_number_id=fn.id LEFT JOIN lcl_booking lb ON lb.file_number_id = fn.id");
        sb.append(" JOIN lcl_booking_ac lac ON lac.file_number_id = fn.id WHERE bpu.lcl_unit_ss_id=").append(unitSsId).append(" AND lac.ar_bill_to_party='W' AND lb.booking_type!='T'");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {
                fileDetails[0] = agentObj[0].toString();
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                fileDetails[1] = agentObj[1].toString();
            }
        }
        return fileDetails;
    }

    public void updateUnitId(Long ssHeaderId, Long oldUnitId, Long newUnitId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_unit_ss set unit_id =:newUnitId, ");
        queryStr.append(" modified_datetime=:dateTime,modified_by_user_id=:userId ");
        queryStr.append(" WHERE ss_header_id =:ssHeaderId and unit_id=:oldUnitId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("ssHeaderId", ssHeaderId);
        queryObject.setParameter("oldUnitId", oldUnitId);
        queryObject.setParameter("newUnitId", newUnitId);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("dateTime", new Date());
        queryObject.executeUpdate();
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
    }

    public List getAccountForInvoiceChargeCodeFromGlAndTerminal(String arRedInvoiceId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  CONCAT_WS('-', ");
        sb.append("    (SELECT sr.`rule_name` FROM `system_rules` sr WHERE sr.`rule_code` = 'CompanyCode'), ");
        sb.append("    gl.`gl_acct`, ");
        sb.append("    LPAD(COALESCE(IF(gl.`derive_yn` <> 'Y', gl.`suffix_value`, (SELECT IF(gl.`suffix_value` = 'B', tm.`lcl_import_billing`, IF(gl.`suffix_value` = 'L', tm.`lcl_import_loading`, tm.`terminal`)) FROM `terminal_gl_mapping` tm WHERE tm.`terminal` = ar.`terminal` LIMIT 1))), 2, '0')");
        sb.append("  ) AS account,");
        sb.append("   gl.`charge_code`");
        sb.append("  FROM");
        sb.append("  ar_red_invoice_charges ar ");
        sb.append("  JOIN gl_mapping gl ");
        sb.append("    ON ar.`charge_code` = gl.`Charge_code` ");
        sb.append("    AND gl.`Shipment_Type` = 'LCLI' ");
        sb.append("    AND gl.`Transaction_Type` = 'AR'");
        sb.append(" WHERE ar.`ar_red_invoice_id` =").append(arRedInvoiceId);
        sb.append("  AND ar.`amount` > 0.0");
        sb.append(" GROUP BY gl.`Charge_code` ");
        return getCurrentSession().createSQLQuery(sb.toString()).list();
    }

    public List getAccountForDistChargeCodeFromGlAndTerminal(String chargeCode, String terminalNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  CONCAT_WS('-', ");
        sb.append("    (SELECT sr.`rule_name` FROM `system_rules` sr WHERE sr.`rule_code` = 'CompanyCode'), ");
        sb.append("    gl.`gl_acct`, ");
        sb.append("    LPAD(COALESCE(IF(gl.`derive_yn` <> 'Y', gl.`suffix_value`, (SELECT IF(gl.`suffix_value` = 'B', tm.`lcl_import_billing`, IF(gl.`suffix_value` = 'L', tm.`lcl_import_loading`, tm.`terminal`)) FROM `terminal_gl_mapping` tm WHERE tm.`terminal` =:terminalNo LIMIT 1))), 2, '0')");
        sb.append("  ) AS account,");
        sb.append("   gl.`charge_code`");
        sb.append("  FROM gl_mapping gl");
        sb.append("   ");
        sb.append(" WHERE gl.charge_code = :chargeCode");
        sb.append("   AND gl.`Shipment_Type` = 'LCLI' ");
        sb.append("   AND gl.`Transaction_Type` = 'AR'");
        sb.append(" GROUP BY gl.`Charge_code` ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("chargeCode", chargeCode);
        queryObject.setParameter("terminalNo", terminalNo);
        return queryObject.list();
    }

    public String getBkgNo(String id) throws Exception {
        String bkgNo = new String();
        String queryString = "SELECT sp_booking_no FROM lcl_unit_ss WHERE id='" + id + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object obj = queryObject.uniqueResult();
        if (obj != null) {
            bkgNo = obj.toString();
        }
        return bkgNo;
    }

    public List<ExportVoyageSearchModel> getBlSumOfCommodityVal(Long unitSsId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT  ");
        queryStr.append(" ROUND(SUM(bp.actual_weight_metric), 2) AS totalWeightMetric, ");
        queryStr.append(" ROUND(SUM(bp.actual_volume_metric), 2) AS totalVolumeMetric  ");
        queryStr.append(" FROM (SELECT DISTINCT lf.id AS fileNumberId  ");
        queryStr.append("  FROM (SELECT DISTINCT bp.`file_number_id` AS fileId  ");
        queryStr.append(" FROM lcl_booking_piece_unit bpu  ");
        queryStr.append("   JOIN lcl_booking_piece bp ON bp.id = bpu.booking_piece_id  ");
        queryStr.append(" WHERE bpu.lcl_unit_ss_id =:unitSsId) AS fn  ");
        queryStr.append(" JOIN lcl_file_number lf ON lf.id = getHouseBLForConsolidateDr (fn.fileId)  ");
        queryStr.append("   AND lf.status IN ('M')) bl  ");
        queryStr.append(" JOIN lcl_bl_piece bp ON bp.`file_number_id` = bl.fileNumberId  ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("unitSsId", unitSsId);
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("totalWeightMetric", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeMetric", BigDecimalType.INSTANCE);
        return query.list();
    }

    public List<ExportVoyageSearchModel> showSsMasterDetails(Long headerId, String spBookingNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();

        queryStr.append(" SELECT lus.id as unitSsId,lus.ss_header_id as ssHeaderId ,lu.unit_no as unitNo,upper(lus.bl_body) as blBody,");
        queryStr.append("  lus.`total_pieces` as totalPiece, lus.volume_imperial as totalVolumeImperial, lus.volume_metric as totalVolumeMetric, ");
        queryStr.append("  lus.weight_imperial as totalWeightImperial, lus.weight_metric as totalWeightMetric ");
        queryStr.append("  from lcl_unit_ss lus JOIN lcl_unit lu ON lu.id = lus.unit_id  ");
        queryStr.append(" WHERE lus.`ss_header_id` =:headerId AND lus.`sp_booking_no` =:spBookingNo");

        SQLQuery query = getSession().createSQLQuery(queryStr.toString());
        query.setParameter("headerId", headerId);
        query.setParameter("spBookingNo", spBookingNo);
        query.setResultTransformer(Transformers.aliasToBean(ExportVoyageSearchModel.class));
        query.addScalar("unitSsId", StringType.INSTANCE);
        query.addScalar("ssHeaderId", StringType.INSTANCE);
        query.addScalar("unitNo", StringType.INSTANCE);
        query.addScalar("blBody", StringType.INSTANCE);
        query.addScalar("totalPiece", IntegerType.INSTANCE);
        query.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalVolumeMetric", BigDecimalType.INSTANCE);
        query.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        query.addScalar("totalWeightMetric", BigDecimalType.INSTANCE);
        return query.list();
    }

    public String getUnitOriginAcct(String unitSSId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  lusi.`origin_acct_no` ");
        sb.append(" FROM");
        sb.append("  lcl_unit_ss lus ");
        sb.append("  JOIN lcl_unit_ss_imports lusi ");
        sb.append("    ON (");
        sb.append("      lus.`ss_header_id` = lusi.`ss_header_id`");
        sb.append("    ) ");
        sb.append(" WHERE lus.`id` = :unitSSId ");
        sb.append(" LIMIT 1 ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("unitSSId", unitSSId);
        return (String) queryObject.uniqueResult();
    }

    public List getAccountForCostCodeFromGlAndTerminal(String costCode, String terminalNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  CONCAT_WS('-', ");
        sb.append("    (SELECT sr.`rule_name` FROM `system_rules` sr WHERE sr.`rule_code` = 'CompanyCode'), ");
        sb.append("    gl.`gl_acct`, ");
        sb.append("    LPAD(COALESCE(IF(gl.`derive_yn` <> 'Y', gl.`suffix_value`, (SELECT IF(gl.`suffix_value` = 'B', tm.`lcl_import_billing`, IF(gl.`suffix_value` = 'L', tm.`lcl_import_loading`, tm.`terminal`)) FROM `terminal_gl_mapping` tm WHERE tm.`terminal` =:terminalNo LIMIT 1))), 2, '0')");
        sb.append("  ) AS account,");
        sb.append("   gl.`charge_code`");
        sb.append("  FROM gl_mapping gl");
        sb.append("   ");
        sb.append(" WHERE gl.charge_code = :chargeCode");
        sb.append("   AND gl.`Shipment_Type` = 'LCLI' ");
        sb.append("   AND gl.`Transaction_Type` = 'AC'");
        sb.append(" GROUP BY gl.`Charge_code` ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("chargeCode", costCode);
        queryObject.setParameter("terminalNo", terminalNo);
        return queryObject.list();
    }

    public String getWareHouseForSubHouse(String fileNumberId) throws Exception {
        StringBuilder sb = new StringBuilder();
        String queryObject = "";
        sb.append("SELECT CONVERT(GROUP_CONCAT(DISTINCT IF(lbi.sub_house_bl IS NULl,'',lbi.sub_house_bl)) USING utf8) FROM lcl_booking_import lbi where lbi.file_number_id='" + fileNumberId + "'");
        queryObject = (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        return null != queryObject ? queryObject : "";
    }

    public Boolean getCOBStatus(String columnName, String fileNumberId) throws Exception {
        String queryString = "SELECT " + columnName + " FROM lcl_unit_ss where id=" + fileNumberId;
        return (Boolean) getSession().createSQLQuery(queryString).uniqueResult();
    }

    public String getUnitNo(Long Unitid) throws Exception {
        String queryString = "SELECT `unit_no` FROM lcl_unit WHERE  id=" + Unitid;
        return (String) getSession().createSQLQuery(queryString).uniqueResult();

    }

    public String[] getLclUnitId(Long unitSsId) throws Exception {
        String[] fileDetails = new String[2];
        String queryString = "SELECT ut.short_desc as shortDesc,ut.description as Descrip FROM lcl_unit_ss ls JOIN lcl_unit lu ON lu.id=ls.unit_id "
                + " JOIN unit_type ut ON ut.id=lu.unit_type_id WHERE ls.id =:unitSsId";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setLong("unitSsId", unitSsId);
        List unitType = query.list();
        if (unitType != null && !unitType.isEmpty()) {
            Object[] unitTypeObj = (Object[]) unitType.get(0);
            fileDetails[0] = unitTypeObj[0] != null ? unitTypeObj[0].toString() : "";
            fileDetails[1] = unitTypeObj[1].toString();
        }

        return fileDetails;
    }
// mantis item: 14545

    public String validateForwardAccount(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" GROUP_CONCAT(IF(bl.fwd_acct_no IS NULL AND lf.state='BL',lf.`file_number`,NULL)) AS data1 ");
        sb.append(" FROM (SELECT bp.`file_number_id`  AS fileId FROM lcl_booking_piece bp ");
        sb.append(" JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id` ");
        sb.append(" WHERE bpu.`lcl_unit_ss_id` =:unitSsId GROUP BY bp.`file_number_id`) f  ");
        sb.append(" JOIN lcl_file_number lf ON lf.id =  f.fileId LEFT JOIN lcl_bl bl  ");
        sb.append(" ON bl.`file_number_id` = getHouseBLForConsolidateDr(f.fileId)  ");
        sb.append(" WHERE bl.fwd_acct_no IS NULL");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("unitSsId", unitSsId);
        return (String) queryObject.uniqueResult();
    }
// mantis Item:14117

    public String[] isValidateLclBlTerms(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT GROUP_CONCAT(DISTINCT f.data1) AS data1, ");
        sb.append(" GROUP_CONCAT(DISTINCT f.data2)AS data2 FROM ");
        sb.append(" (SELECT IF(lpc.lcl_ocean_bl <> 'Y' AND bl.billing_type = 'C',lf.`file_number`,NULL) AS data1, ");
        sb.append(" IF(lpc.lcl_ocean_bl <> 'Y' AND bl.billing_type = 'B' AND lba.`ar_bill_to_party` = 'A', ");
        sb.append(" lf.`file_number`,NULL)AS data2 FROM(SELECT  bp.file_number_id AS fileId ");
        sb.append(" FROM lcl_booking_piece bp JOIN lcl_booking_piece_unit bpu  ");
        sb.append(" ON bpu.`booking_piece_id` = bp.`id` WHERE bpu.`lcl_unit_ss_id` =:unitSsId) f ");
        sb.append(" JOIN lcl_file_number lf ON lf.id = f.fileId JOIN lcl_bl bl   ");
        sb.append(" ON bl.`file_number_id` = getHouseBLForConsolidateDr (f.fileId) ");
        sb.append(" JOIN lcl_bl_ac lba ON lba.`file_number_id` = bl.file_number_id  ");
        sb.append(" JOIN `un_location` un ON (bl.`pod_id` = un.`id`)JOIN lcl_port_configuration lpc ");
        sb.append(" ON (lpc.schnum=(SELECT p.id FROM ports p WHERE un.un_loc_code = p.unlocationcode))  ");
        sb.append(" WHERE (lba.`ar_bill_to_party` = 'C' OR lba.`ar_bill_to_party` = 'A') ");
        sb.append(" AND lpc.lcl_ocean_bl <>'Y') AS f");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("unitSsId", unitSsId);
        List fileNumberList = query.list();
        String[] data = new String[2];
        if (fileNumberList != null && fileNumberList.size() > 0) {
            Object[] fileNumber = (Object[]) fileNumberList.get(0);
            if (fileNumber[0] != null && !fileNumber[0].toString().trim().equals("")) {
                data[0] = fileNumber[0].toString();
            }
            if (fileNumber[1] != null && !fileNumber[1].toString().trim().equals("")) {
                data[1] = fileNumber[1].toString();
            }
        }
        return data;
    }

    public String getCompleteValidation(String headerId) throws Exception {
        StringBuilder append = new StringBuilder();
        append.append("SELECT GROUP_CONCAT(lus.status)");
        append.append(" FROM lcl_ss_header lsh  ");
        append.append("   JOIN lcl_unit_ss lus ON lsh.id = lus.ss_header_id  ");
        append.append("   JOIN lcl_unit lu ON lu.id = lus.unit_id ");
        append.append(" WHERE lsh.id =:headerId AND lus.`status`='E' ");
        SQLQuery query = getCurrentSession().createSQLQuery(append.toString());
        query.setString("headerId", headerId);
        return (String) query.uniqueResult();

    }

    public String[] checkNoBlRequired(String unitSsId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT GROUP_CONCAT(IF(lf.state <> 'BL' ,lf.`file_number`,NULL)) AS data1 ");
        sb.append("  FROM (SELECT DISTINCT bp.`file_number_id`  AS fileId FROM lcl_booking_piece bp ");
        sb.append("  JOIN lcl_booking_piece_unit bpu ON bpu.`booking_piece_id` = bp.`id` WHERE bpu.`lcl_unit_ss_id` =:unitSsId) f  ");
        sb.append("  JOIN lcl_file_number lf ON lf.id =  f.fileId   LEFT JOIN `lcl_booking_export` be ");
        sb.append("  ON be.`file_number_id`=f.fileId   ");
        sb.append("  WHERE lf.state <> 'BL'  AND  be.`no_bl_required` !='1' ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("unitSsId", unitSsId);
        List fileNumberList = query.list();
        String[] data = new String[1];
        if (fileNumberList != null && fileNumberList.size() > 0) {
            if (fileNumberList.get(0) != null) {
                data[0] = fileNumberList.get(0).toString();
            }
        }
        return data;
    }

    public String[] getPickedVoyage(String fileId) throws Exception {
        StringBuilder str = new StringBuilder();
        String data[] = new String[2];
        str.append("SELECT GROUP_CONCAT(lcf.id),GROUP_CONCAT(lcf.file_number) FROM lcl_booking_piece_unit lbu ");
        str.append("JOIN lcl_booking_piece lbp ON lbu.booking_piece_id=lbp.id ");
        str.append("JOIN lcl_file_number lcf ON lcf.id=lbp.file_number_id ");
        str.append("WHERE lcf.id IN(").append(fileId).append(")");
        SQLQuery query = getCurrentSession().createSQLQuery(str.toString());
        List result = query.list();
        if (CommonUtils.isNotEmpty(result)) {
            for (Object obj : result) {
                Object[] row = (Object[]) obj;
                data[0] = null != row[0] ? row[0].toString() : "";
                data[1] = null != row[1] ? row[1].toString() : "";
            }
        }
        return data;
    }

}
