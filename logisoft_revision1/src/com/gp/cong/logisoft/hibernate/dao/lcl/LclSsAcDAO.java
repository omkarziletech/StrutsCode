/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.lcl.comparator.LclImportManifestBeanInvoiceNoComparator;
import com.gp.cong.logisoft.struts.action.lcl.LclUnitsRates;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.logiware.accounting.dao.LclManifestDAO;
import com.logiware.common.dao.ChainedFieldsTransformer;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author i3
 */
public class LclSsAcDAO extends BaseHibernateDAO<LclSsAc> {

    public LclSsAcDAO() {
        super(LclSsAc.class);
    }

    public List<LclSsAc> getCostList(Long unitSsId, Long ssHeaderId,
            Boolean manualEntry, String apType) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append(" FROM LclSsAc lba LEFT JOIN fetch lba.apGlMappingId gm WHERE lba.lclUnitSs.id=:unitSsId AND lba.lclSsHeader.id=:ssHeaderId ");
        query.append(" AND gm.chargeCode <> 'HDLG-MIA' AND lba.manualEntry =:manualEntry AND lba.apTransType=:type ");
        Query hqlQuery = getSession().createQuery(query.toString());
        hqlQuery.setParameter("unitSsId", unitSsId);
        hqlQuery.setParameter("ssHeaderId", ssHeaderId);
        hqlQuery.setParameter("manualEntry", manualEntry);
        hqlQuery.setParameter("type", apType);
        List list = hqlQuery.list();
        return list;
    }

    public void insertSSAcCharge(Long headerId, Long detailId, Long unitSSId, Integer manualEntry, String apAcctNo, String apReferenceNo,
            String apAmount, Integer apGlMappingId, String arAcctNo, String arReferenceNo, String arAmount, Integer arGlMappingId,
            Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO lcl_ss_ac (ss_header_id,ss_detail_id,unit_ss_id,manual_entry,trans_datetime,ap_acct_no,ap_reference_no,");
        queryBuilder.append("ap_amount,ap_gl_mapping_id,ar_acct_no,ar_reference_no,ar_amount,ar_gl_mapping_id,entered_datetime,entered_by_user_id,");
        queryBuilder.append("modified_datetime,modified_by_user_id) VALUES (");
        queryBuilder.append(headerId).append(",").append(detailId).append(",").append(unitSSId).append(",").append(manualEntry).append(",SYSDATE(),'");
        queryBuilder.append(apAcctNo).append("','").append(apReferenceNo).append("',").append(apAmount).append(",").append(apGlMappingId).append(",'");
        queryBuilder.append(arAcctNo).append("','").append(arReferenceNo).append("',").append(arAmount).append(",").append(arGlMappingId).append(",");
        queryBuilder.append("SYSDATE(),").append(userId).append(",SYSDATE(),").append(userId).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void calculateUnitCostByLCLE(LclSsHeader lclssheader, LclUnitSs lclunitss, String ssLineNo, int departId,
            User loginUser, Date now, String hazFlag) throws Exception {
        LclUnitSsRemarksDAO unitSsRemarksDAO = new LclUnitSsRemarksDAO();
        //LclSsAcDAO ssAcDAO = new LclSsAcDAO();
        LclManifestDAO manifestDAO = new LclManifestDAO();
        // delete Auto cost
//        List<LclSsAc> lclSsAcAutoList = ssAcDAO.getCostList(lclunitss.getId(), lclssheader.getId(), false, "AC");
//        //Delete Notes By Individual Cost
//        if (!lclSsAcAutoList.isEmpty()) {
//            String concatenatedVoyageNumber = lclssheader.getOrigin().getUnLocationCode() + "-"
//                    + lclssheader.getDestination().getUnLocationCode() + "-"
//                    + lclssheader.getScheduleNo();
//            for (LclSsAc lclSsAc : lclSsAcAutoList) {
//                String remarks = "DELETED -> Cost Code -> " + lclSsAc.getApGlMappingId().getChargeCode() + " Cost Amount -> " + lclSsAc.getApAmount();
//                unitSsRemarksDAO.insertLclunitRemarks(lclssheader.getId(), lclunitss.getLclUnit().getId(), "auto",
//                        remarks, loginUser.getUserId());
//                manifestDAO.deleteLclUnitAccruals(lclSsAc.getId().intValue(), concatenatedVoyageNumber,
//                        lclunitss.getLclUnit().getUnitNo());
//                new LclSsAcDAO().delete(lclSsAc);
//            }
//        }
        //delete Auto Cost
        //   deleteAutoRates(lclssheader.getId(), lclunitss.getId(), false);
        if (CommonUtils.isNotEmpty(lclunitss.getSpBookingNo())) {
            List<ImportsManifestBean> autoCostList = new LclUnitsRates().findAutoCostsByLCLE(
                    lclssheader.getOrigin().getId(), lclssheader.getDestination().getId(),
                    lclunitss.getLclUnit().getUnitType().getId(), ssLineNo, departId, hazFlag);
            List<LclSsAc> lclSsAcList = new ArrayList<LclSsAc>();
            if (null != autoCostList && !autoCostList.isEmpty()) {

                Map<String, ImportsManifestBean> bundleCostMap = new HashMap<String, ImportsManifestBean>();
                for (ImportsManifestBean autoCost : autoCostList) {
                    if ("HAZFEE".equalsIgnoreCase(autoCost.getChargeCode()) || "DEFER".equalsIgnoreCase(autoCost.getChargeCode())) {
                        bundleCostMap.put(autoCost.getChargeCode(), autoCost);
                    } else {
                        if (bundleCostMap.containsKey("Other")) {
                            ImportsManifestBean bundle = bundleCostMap.get("Other");
                            if ("HAZFEE".equalsIgnoreCase(autoCost.getChargeCode())) {
                                bundle.setChargeId(autoCost.getChargeId());
                            }
                            Double total = bundle.getTotalIPI().doubleValue() + autoCost.getTotalIPI().doubleValue();
                            bundle.setTotalIPI(new BigDecimal(total));
                            bundleCostMap.put("Other", bundle);
                        } else {
                            bundleCostMap.put("Other", autoCost);
                        }
                    }
                }

                TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
                GlMappingDAO glMappingDAO = new GlMappingDAO();
                unitSsRemarksDAO.insertLclunitRemarks(lclssheader.getId(),
                        lclunitss.getLclUnit().getId(), "auto", "Auto Generated at COB time", loginUser.getUserId());
                TradingPartner vendorAcct = null;
                String prepaidOrCollect = new LclSSMasterBlDAO().getMasterDetails(lclunitss.getId(), lclunitss.getSpBookingNo());
                if (null != prepaidOrCollect && "C".equalsIgnoreCase(prepaidOrCollect)) {
                    String[] consigneeDetails = new LclSsDetailDAO().getAgentAddress(lclssheader.getDestination().getUnLocationCode());
                    if (CommonUtils.isNotEmpty(consigneeDetails)) {
                        vendorAcct = tradingPartnerDAO.findById(consigneeDetails[1]);
                    }
                } else {
                    vendorAcct = tradingPartnerDAO.findById(ssLineNo);
                }
                for (Map.Entry<String, ImportsManifestBean> cost : bundleCostMap.entrySet()) {
                    ImportsManifestBean autoCost = cost.getValue();
                    String chargeCode = cost.getKey().equalsIgnoreCase("Other") ? "OCNFRT" : cost.getKey();
                    if (autoCost.getTotalIPI() != null && null != vendorAcct) {
                        GlMapping glmapping = glMappingDAO.findByChargeCode(chargeCode, "LCLE", "AC");
                        LclSsAc lclSsAc = getCost(lclunitss.getId(), lclssheader.getId(), glmapping.getId(), false, "AC");
                        if (lclSsAc == null) {
                            lclSsAc = new LclSsAc();
                            lclSsAc.setLclUnitSs(lclunitss);
                            lclSsAc.setLclSsHeader(lclssheader);
                            lclSsAc.setEnteredDatetime(now);
                            lclSsAc.setManualEntry(false);//auto cost
                            lclSsAc.setTransDatetime(now);
                            lclSsAc.setArAmount(BigDecimal.ZERO);
                            lclSsAc.setEnteredByUserId(loginUser);
                        }
                        lclSsAc.setApAmount(autoCost.getTotalIPI().divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                        lclSsAc.setApTransType(ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
                        lclSsAc.setApAcctNo(vendorAcct);
                        lclSsAc.setArAcctNo(vendorAcct);
                        lclSsAc.setApGlMappingId(glmapping);
                        lclSsAc.setArGlMappingId(glmapping);
                        lclSsAc.setModifiedByUserId(loginUser);
                        lclSsAc.setModifiedDatetime(now);
                        new LclSsAcDAO().saveOrUpdate(lclSsAc);
                        lclSsAcList.add(lclSsAc);
                    }
                }
            }
            if (null != lclSsAcList && !lclSsAcList.isEmpty()) {
                manifestDAO.createLclAccrualsforAutoCosting(lclSsAcList);
            }
        }
    }

    public void calculateUnitRates(LclSsHeader lclssheader, LclUnitSs lclunitss, int originId, int fdId, int cfsWareHsId, Long unitTypeId, Date d, HttpServletRequest request) throws Exception {
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        // delete Auto cost
        List<LclSsAc> lclSsAcAutoList = new LclSsAcDAO().displayRates(lclunitss.getId(), lclssheader.getId(), false);
        if (lclSsAcAutoList.isEmpty()) {
            new LclUnitSsRemarksDAO().insertLclunitRemarks(lclssheader.getId(), lclunitss.getLclUnit().getId(), "auto", "Auto Cost Calculated", loginUser.getUserId());
        } else {
            new LclUnitSsRemarksDAO().insertLclunitRemarks(lclssheader.getId(), lclunitss.getLclUnit().getId(), "auto", "Auto Cost Recalculated", loginUser.getUserId());
        }
        //Delete Notes By Individual Cost
        if (!lclSsAcAutoList.isEmpty()) {
            for (LclSsAc lclSsAc : lclSsAcAutoList) {
                String remarks = "DELETED -> Cost Code -> " + lclSsAc.getApGlMappingId().getChargeCode() + " Cost Amount -> " + lclSsAc.getApAmount();
                new LclUnitSsRemarksDAO().insertLclunitRemarks(lclssheader.getId(), lclunitss.getLclUnit().getId(), "auto", remarks, loginUser.getUserId());
            }
        }
        //delete Auto Cost
        deleteAutoRates(lclssheader.getId(), lclunitss.getId(), false);
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        LclManifestDAO manifestDAO = new LclManifestDAO();
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        List<LclSsAc> lclSsAcList = new ArrayList<LclSsAc>();
        List<ImportsManifestBean> autoCostList = new LclUnitsRates().findAutoCosts(originId, fdId, cfsWareHsId, unitTypeId);
        if (null != autoCostList && !autoCostList.isEmpty()) {
            for (ImportsManifestBean autoCost : autoCostList) {

                if (CommonUtils.isNotEmpty(autoCost.getChargeId()) && CommonUtils.isNotEmpty(autoCost.getAgentNo()) && autoCost.getTotalIPI() != null && lclssheader != null) {
                    // set cost values
                    LclSsAc lclSsAc = new LclSsAc();
                    lclSsAc.setApAmount(autoCost.getTotalIPI().divide(new BigDecimal(1.00), 2, BigDecimal.ROUND_HALF_UP));
                    lclSsAc.setApTransType(ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
                    lclSsAc.setApAcctNo(tradingPartnerDAO.findById(autoCost.getAgentNo()));
                    GlMapping glmapping = glMappingDAO.findById(Integer.valueOf(autoCost.getChargeId().intValue()));
                    lclSsAc.setApGlMappingId(glmapping);
                    lclSsAc.setArGlMappingId(glmapping);
                    lclSsAc.setArAmount(BigDecimal.ZERO);
                    lclSsAc.setArAcctNo(tradingPartnerDAO.findById(autoCost.getAgentNo()));
                    //auto cost
                    lclSsAc.setManualEntry(false);
                    lclSsAc.setTransDatetime(d);
                    lclSsAc.setLclSsHeader(lclssheader);
                    lclSsAc.setLclUnitSs(lclunitss);
                    lclSsAc.setModifiedByUserId(loginUser);
                    lclSsAc.setEnteredByUserId(loginUser);
                    lclSsAc.setModifiedDatetime(d);
                    lclSsAc.setEnteredDatetime(d);
                    new LclSsAcDAO().save(lclSsAc);
                    lclSsAcList.add(lclSsAc);
                }
            }
        }
        manifestDAO.createLclAccrualsforAutoCosting(lclSsAcList);
    }

    public List<LclSsAc> displayRates(Long unitSsId, Long headerId, boolean manualEntry) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  gm.charge_code as 'apGlMappingId.chargeCode',");
        queryBuilder.append("  if(gm.charge_description <> '', gm.charge_description, ge.codedesc) as 'apGlMappingId.chargeDescriptions',");
        queryBuilder.append("  ac.manual_entry as 'manualEntry',ac.distributed as 'distributed',");
        queryBuilder.append("  ac.ap_amount as 'apAmount',");
        queryBuilder.append("  ac.ap_reference_no as 'apReferenceNo',");
        queryBuilder.append("  ac.ap_acct_no as 'apAcctNo.accountno',");
        queryBuilder.append("  ac.ap_trans_type as 'apTransType',");
        queryBuilder.append("  ac.modified_datetime as 'modifiedDatetime',");
        queryBuilder.append("  ud.login_name as 'modifiedByUserId.loginName',");
        queryBuilder.append("  ac.id as 'id' ");
        queryBuilder.append("from");
        queryBuilder.append("  lcl_ss_ac ac ");
        queryBuilder.append("  join gl_mapping gm ");
        queryBuilder.append("    on (ac.ap_gl_mapping_id = gm.id)");
        queryBuilder.append("  join genericcode_dup ge ");
        queryBuilder.append("    on (");
        queryBuilder.append("      gm.charge_code = ge.code");
        queryBuilder.append("      and ge.codetypeid = (select codetypeid from codetype where description = 'Cost Code')");
        queryBuilder.append("    )");
        queryBuilder.append("  join user_details ud");
        queryBuilder.append("    on (ac.modified_by_user_id = ud.user_id) ");
        queryBuilder.append("where");
        queryBuilder.append("  ac.unit_ss_id = ").append(unitSsId);
        queryBuilder.append("  and ac.ss_header_id = ").append(headerId);
        queryBuilder.append("  and ac.manual_entry = ").append(manualEntry);
        queryBuilder.append("  and ac.deleted = false");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", LongType.INSTANCE);
        query.addScalar("manualEntry", BooleanType.INSTANCE);
        query.addScalar("apAmount", BigDecimalType.INSTANCE);
        query.addScalar("apReferenceNo", StringType.INSTANCE);
        query.addScalar("apTransType", StringType.INSTANCE);
        query.addScalar("modifiedDatetime", DateType.INSTANCE);
        query.addScalar("apGlMappingId.chargeCode", StringType.INSTANCE);
        query.addScalar("apGlMappingId.chargeDescriptions", StringType.INSTANCE);
        query.addScalar("apAcctNo.accountno", StringType.INSTANCE);
        query.addScalar("modifiedByUserId.loginName", StringType.INSTANCE);
        query.addScalar("distributed", BooleanType.INSTANCE);
        query.setResultTransformer(new ChainedFieldsTransformer(LclSsAc.class, null));
        return query.list();
//        Criteria criteria = getSession().createCriteria(LclSsAc.class, "lclSsAc");
//        criteria.createAlias("lclSsAc.lclUnitSs", "lclUnitSs");
//        criteria.createAlias("lclSsAc.lclSsHeader", "lclSsHeader");
//        if (!CommonUtils.isEmpty(unitSsId)) {
//            criteria.add(Restrictions.eq("lclUnitSs.id", unitSsId));
//        }
//        if (!CommonUtils.isEmpty(headerId)) {
//            criteria.add(Restrictions.eq("lclSsHeader.id", headerId));
//        }
//        criteria.add(Restrictions.eq("manualEntry", manualEntry));
//        criteria.add(Restrictions.ne("deleted", true));
//        return criteria.list();
    }

    public void deleteAutoRates(Long headerId, Long unitSsId, boolean manualEntry) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM lcl_ss_ac WHERE ss_header_id =").append(headerId).append(" AND unit_ss_id=").append(unitSsId).append(" AND manual_entry=").append(manualEntry);
        Query queryObject = getSession().createSQLQuery(sb.toString());
        queryObject.executeUpdate();
    }

    public List<LclSsAc> getAllLclUnitCostAsc(Long headerId, Long unitSsId, String groupByInvoiceFlag) throws Exception {
        List autoChargesList = displayRates(unitSsId, headerId, false);
        List manualChargesList = displayRates(unitSsId, headerId, true);
        autoChargesList.addAll(manualChargesList);
        List<LclSsAc> UnitCostList = null;
        if (groupByInvoiceFlag.equalsIgnoreCase("true")) {
        } else {
            UnitCostList = autoChargesList;
        }
        return UnitCostList;
    }

    public BigDecimal getTotalLclUnitCostByUnitSSId(Long unitSSId) throws Exception {
        BigDecimal total = new BigDecimal(0.00);
        String queryString = "SELECT SUM(ap_amount) FROM lcl_ss_ac WHERE unit_ss_id =" + unitSSId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            total = (BigDecimal) o;
        }
        return total;
    }

    public List<ImportsManifestBean> getAllDrCost(Long headerId, Long unitSsId, String groupByInvoiceFlag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  fn.file_number as fileNo,");
        queryBuilder.append("  if(gm.charge_description <> '', gm.charge_description, ge.codedesc) as agentNo,");
        queryBuilder.append("  gm.charge_code as chargeCode,");
        queryBuilder.append("  ba.ap_amount as apAmount,");
        queryBuilder.append("  ba.invoice_number as invoiceNo,");
        queryBuilder.append("  ba.sp_acct_no as vandorAcct,");
        queryBuilder.append("  batr.trans_type as transType ,ba.id AS bookingAcId ");
        queryBuilder.append("from");
        queryBuilder.append("  lcl_booking_piece_unit bpu ");
        queryBuilder.append("  join lcl_booking_piece bp ");
        queryBuilder.append("    on (bpu.booking_piece_id = bp.id)");
        queryBuilder.append("  join lcl_booking_ac ba ");
        queryBuilder.append("    on (bp.file_number_id = ba.file_number_id)");
        queryBuilder.append("  join lcl_file_number fn ");
        queryBuilder.append("    on (bp.file_number_id = fn.id)");
        queryBuilder.append("  join gl_mapping gm ");
        queryBuilder.append("    on (ba.ap_gl_mapping_id = gm.id)");
        queryBuilder.append("  join genericcode_dup ge ");
        queryBuilder.append("    on (");
        queryBuilder.append("      ge.code = gm.charge_code");
        queryBuilder.append("      and ge.codetypeid = (select codetypeid from codetype where description = 'Cost Code')");
        queryBuilder.append("    )");
        queryBuilder.append("  left join lcl_booking_ac_ta bat ");
        queryBuilder.append("    on (ba.id = bat.lcl_booking_ac_id)");
        queryBuilder.append("  left join lcl_booking_ac_trans batr ");
        queryBuilder.append("    on (bat.lcl_booking_ac_trans_id = batr.id) ");
        queryBuilder.append("where");
        queryBuilder.append("  bpu.lcl_unit_ss_id = ").append(unitSsId);
        queryBuilder.append("  and ba.ap_amount <> 0.00 ");
        queryBuilder.append("group by fn.id, ba.id ");
        if ("true".equalsIgnoreCase(groupByInvoiceFlag)) {
            queryBuilder.append("order by ba.invoice_number");
        } else {
            queryBuilder.append("order by fn.id");
        }
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT cost.*,trans.transtype AS transType FROM (");
//        sb.append("SELECT lfn.file_number as fileNo,IF(gm.charge_description <> '',gm.charge_description, ge.codedesc) AS agentNo,gm.charge_code as chargeCode,");
//        sb.append("lba.ap_amount as billingTerminal,lba.invoice_number as invoiceNo,lba.sp_acct_no AS vandorAcct,lba.id AS bkgacid  ");
//        sb.append("FROM lcl_booking_piece_unit lbpu ");
//        sb.append("JOIN lcl_booking_piece lbp ON lbp.id=lbpu.booking_piece_id JOIN lcl_booking_ac lba ON lba.file_number_id=lbp.file_number_id ");
//        sb.append("JOIN lcl_file_number lfn ON lfn.id=lbp.file_number_id JOIN gl_mapping gm ON gm.id=lba.ap_gl_mapping_id ");
//        sb.append(" JOIN genericcode_dup ge ON gm.charge_code = ge.code AND ge.codetypeid = (SELECT  codetypeid FROM codetype WHERE description = 'Cost Code') ");
//        sb.append("WHERE lba.ap_amount IS NOT NULL AND lba.ap_amount!=0.00 AND lbpu.lcl_unit_ss_id= ").append(unitSsId);
//        if (groupByInvoiceFlag.equalsIgnoreCase("true")) {
//            sb.append(" ORDER BY invoiceNo  ");
//        } else {
//            sb.append(" ORDER BY lfn.id DESC ");
//        }
//        sb.append(" )cost LEFT JOIN (SELECT lcbat.lcl_booking_ac_id AS bkgacId,lcbatr.trans_type AS transtype FROM lcl_booking_ac_ta lcbat ");
//        sb.append(" LEFT JOIN lcl_booking_ac_trans lcbatr ON lcbatr.id = lcbat.lcl_booking_ac_trans_id  ");
//        sb.append(" GROUP BY lcbat.lcl_booking_ac_id) trans ON trans.bkgacId=cost.bkgacid ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ImportsManifestBean.class));
        query.addScalar("fileNo", StringType.INSTANCE);//file Number
        query.addScalar("agentNo", StringType.INSTANCE);//Cost Charge Description
        query.addScalar("chargeCode", StringType.INSTANCE);//cost charge code
        query.addScalar("apAmount", BigDecimalType.INSTANCE);//Cost Amount
        query.addScalar("invoiceNo", StringType.INSTANCE);//Invoice Number
        query.addScalar("vandorAcct", StringType.INSTANCE);//Invoice Number
        query.addScalar("transType", StringType.INSTANCE);//Invoice Number
        query.addScalar("bookingAcId", LongType.INSTANCE);//Invoice Number
        List<ImportsManifestBean> drCostList = query.list();
        if (groupByInvoiceFlag.equalsIgnoreCase("true")) {
            drCostList = getAllgroupByInvoice(drCostList, headerId, unitSsId, groupByInvoiceFlag);
        }
        return drCostList;
    }

    public List<ImportsManifestBean> getAllgroupByInvoice(List<ImportsManifestBean> drCostList, Long headerId, Long unitSsId, String groupByInvoiceFlag) throws Exception {
        //Getting Unit Cost*****************************************************
        List autoChargesList = displayRates(unitSsId, headerId, false);
        List manualChargesList = displayRates(unitSsId, headerId, true);
        autoChargesList.addAll(manualChargesList);
        //Adding unitcosts to drCosts*******************************************
        List<ImportsManifestBean> unitDrCostList = new ArrayList();
        for (int i = 0; i < (autoChargesList.size()); i++) {
            ImportsManifestBean imfb = new ImportsManifestBean();
            LclSsAc lsa = (LclSsAc) autoChargesList.get(i);
            imfb.setInvoiceNo(lsa.getApReferenceNo());
            imfb.setApAmount(lsa.getApAmount());
            imfb.setChargeCode(lsa.getApGlMappingId().getChargeCode());
            imfb.setAgentNo(lsa.getApGlMappingId().getChargeDescriptions());
            imfb.setVandorAcct(lsa.getApAcctNo().getAccountno());
            imfb.setTransType(lsa.getApTransType());
            unitDrCostList.add(imfb);
        }
        drCostList.addAll(unitDrCostList);
        //Removing null invoice contained drList********************************
        String oldInvoiceNO = "";
        double cost = 0.00;
        List<ImportsManifestBean> drCostWithNullInvoiceList = new ArrayList<ImportsManifestBean>();
        for (int i = drCostList.size() - 1; i >= 0; i--) {
            ImportsManifestBean drCostWithNullInvoive = drCostList.get(i);
            if (drCostWithNullInvoive.getInvoiceNo() == null || "".equals(drCostWithNullInvoive.getInvoiceNo().trim())) {
                drCostList.remove(i);
                drCostWithNullInvoiceList.add(drCostWithNullInvoive);
            }
        }
        Collections.sort(drCostList, new LclImportManifestBeanInvoiceNoComparator());
        //Calculating Total Cost according to invoice***************************
        int index = 0;
        for (ImportsManifestBean drCost : drCostList) {
            String invoiceNo = drCost.getInvoiceNo();
            if (invoiceNo.equalsIgnoreCase(oldInvoiceNO)) {
                cost = cost + drCost.getApAmount().doubleValue();
                drCost.setTotalCostByInvoiceNo("");
            } else if (oldInvoiceNO.equals("")) {
                drCost.setTotalCostByInvoiceNo("");
                cost = drCost.getApAmount().doubleValue();
            } else {
                drCostList.get(index - 1).setTotalCostByInvoiceNo("(Total$:" + String.valueOf(NumberUtils.convertToTwoDecimal(cost)) + ")");
                cost = drCost.getApAmount().doubleValue();
            }
            oldInvoiceNO = drCost.getInvoiceNo();
            index++;
        }
        if (drCostList.size() > 0) {
            ImportsManifestBean lastDrCostObj = drCostList.get((drCostList.size() - 1));
            lastDrCostObj.setTotalCostByInvoiceNo("(Total$:" + String.valueOf(NumberUtils.convertToTwoDecimal(cost)) + ")");
        }
        drCostList.addAll(drCostWithNullInvoiceList);
        return drCostList;
    }

    public String getConcatenatedIds(Long unitSSId) throws Exception {
        String ids = null;
        String queryString = "SELECT GROUP_CONCAT(id) FROM lcl_ss_ac WHERE unit_ss_id =" + unitSSId;
        Query queryObject = getSession().createSQLQuery(queryString);
        Object o = queryObject.uniqueResult();
        if (o != null) {
            ids = (String) o;
        }
        return ids;
    }

    public LclSsAc getLclSsAcByChargeCode(long unissid, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclSsAc.class, "lclSsAc");
        criteria.add(Restrictions.eq("lclSsAc.lclUnitSs.id", unissid));
        criteria.createAlias("lclSsAc.arGlMappingId", "glMapping");
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        return (LclSsAc) criteria.setMaxResults(1).uniqueResult();
    }

    public List<LclSsAc> getLclSsAcListByChargeCode(long unissid, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(LclSsAc.class, "lclSsAc");
        criteria.add(Restrictions.eq("lclSsAc.lclUnitSs.id", unissid));
        criteria.createAlias("lclSsAc.arGlMappingId", "glMapping");
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("glMapping.chargeCode", chargeCode));
        }
        return (List<LclSsAc>) criteria.list();
    }

    public List<ArRedInvoiceCharges> getArRedInvoiceChargesCode(String unitNo, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(ArRedInvoiceCharges.class, "ArRedInvoiceCharges");
        criteria.add(Restrictions.eq("ArRedInvoiceCharges.blDrNumber", unitNo));
        criteria.add(Restrictions.ne("ArRedInvoiceCharges.amount", 0.00));
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("ArRedInvoiceCharges.chargeCode", chargeCode));
        }
        return (List<ArRedInvoiceCharges>) criteria.list();
    }

    public List<ArRedInvoiceCharges> getDRLevelArRedInvoiceChargesCode(String fileNumber, String chargeCode) throws Exception {
        Criteria criteria = getSession().createCriteria(ArRedInvoiceCharges.class, "ArRedInvoiceCharges");
        criteria.add(Restrictions.eq("ArRedInvoiceCharges.blDrNumber", fileNumber));
        criteria.add(Restrictions.ne("ArRedInvoiceCharges.amount", 0.00));
        if (CommonUtils.isNotEmpty(chargeCode)) {
            criteria.add(Restrictions.eq("ArRedInvoiceCharges.chargeCode", chargeCode));
        }
        return (List<ArRedInvoiceCharges>) criteria.list();
    }

    public void updateLclSsAc(String unitSSAcId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE lcl_ss_ac set distributed=1 WHERE id IN (").append(unitSSAcId).append(");");
        Query queryObject = getSession().createSQLQuery(sb.toString());
        queryObject.executeUpdate();
    }

    public Long findByUnitSsId(Long unitSsAcId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("select unit_ss_id as unitSsId from lcl_ss_ac  ");
        queryStr.append(" WHERE ss_header_id= ");
        queryStr.append(" (SELECT ss_header_id FROM lcl_ss_ac WHERE id=:unitSsAcId) ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("unitSsAcId", unitSsAcId);
        query.addScalar("unitSsId", LongType.INSTANCE);
        Long id = (Long) query.setMaxResults(1).uniqueResult();
        return id != null ? id : 0L;
    }

    public Boolean isCheckedRates(Long headerId, Long unitSsId, boolean delete) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(COUNT(*)>0,TRUE,FALSE) AS result FROM lcl_ss_ac ");
        queryStr.append("WHERE ss_header_id=:headerId AND unit_ss_id=:unitSsId  ");
        queryStr.append(" AND deleted=:deleted ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("headerId", headerId);
        query.setLong("unitSsId", unitSsId);
        query.setBoolean("deleted", delete);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public Boolean isCheckedRates(Long headerId, Long unitSsId, boolean manualEntry, boolean delete) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT IF(COUNT(*)>0,TRUE,FALSE) AS result FROM lcl_ss_ac ");
        queryStr.append("WHERE ss_header_id=:headerId AND unit_ss_id=:unitSsId  ");
        queryStr.append(" and manual_entry=:manualEntry AND deleted=:deleted ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("headerId", headerId);
        query.setLong("unitSsId", unitSsId);
        query.setBoolean("manualEntry", manualEntry);
        query.setBoolean("deleted", delete);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

   public List getCfsCharge(Long headerId) throws Exception {
        String queryString = "SELECT ap_gl_mapping_id AS chargecode, SUM(ap_amount) AS ApTotal FROM `lcl_ss_ac` lac LEFT JOIN `gl_mapping` gl "
                + " ON lac.ap_gl_mapping_id=gl.id  WHERE lac.ss_header_id =:headerId  "
//                + "AND lac.ap_acct_no =:vendorNo"
                + " AND  gl.`charge_code` IN('DRAY','STRIP') AND gl.`Shipment_Type`='LCLI' AND lac.ap_amount <>0.00  GROUP BY chargecode";
        Query query = getSession().createSQLQuery(queryString);
        query.setParameter("headerId", headerId);
//        query.setParameter("vendorNo", vendorNo);
        return query.list();
    }

    public void updateCostByVendor(String unitSsId, String vendorNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" UPDATE lcl_ss_ac set ap_acct_no=:vendorNo where ");
        queryStr.append(" unit_ss_id in(").append(unitSsId).append(") and manual_entry=:manual and ap_trans_type=:apStatus ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("vendorNo", vendorNo);
        query.setBoolean("manual", false);
        query.setParameter("apStatus", "AC");
        query.executeUpdate();
    }

    public String getSsAcIds(String unitSsId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" SELECT GROUP_CONCAT(DISTINCT id) AS ssAcId FROM ");
        queryStr.append(" lcl_ss_Ac WHERE unit_ss_id IN(").append(unitSsId).append(") AND manual_entry=FALSE ");
        queryStr.append(" AND ap_trans_type='AC' ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.addScalar("ssAcId", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }

    public void updateTransacationByVendor(String ssAcId, String vendorNo) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" UPDATE transaction_ledger SET cust_no=:vendorNo, ");
        queryStr.append(" cust_name=(select acct_name from trading_partner where acct_no=:vendorNo) ");
        queryStr.append(" WHERE cost_id in(").append(ssAcId).append(") AND drcpt is null ");
        queryStr.append("  AND transaction_type='AC' AND shipment_type='LCLE' ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setParameter("vendorNo", vendorNo);
        query.executeUpdate();
    }

    public LclSsAc createInstance(LclUnitSs unitSs, Double baseAutoCostAmt, Integer costCodeId,
            String shipmentType, String vendorNo, User loginUser, Date now, String invNo) throws Exception {

        LclSsAc lclSsAc = getCost(unitSs.getId(), unitSs.getLclSsHeader().getId(), costCodeId, false, "AC");
        if (lclSsAc == null) {
            lclSsAc = new LclSsAc();
            lclSsAc.setLclUnitSs(unitSs);
            lclSsAc.setEnteredByUserId(loginUser);
            lclSsAc.setEnteredDatetime(now);
            lclSsAc.setLclSsHeader(unitSs.getLclSsHeader());
            lclSsAc.setManualEntry(false);//auto cost
            lclSsAc.setTransDatetime(now);
            lclSsAc.setApTransType(ConstantsInterface.TRANSACTION_TYPE_ACCRUALS);
            lclSsAc.setArAmount(BigDecimal.ZERO);
        }
        lclSsAc.setApAcctNo(new TradingPartnerDAO().findById(vendorNo));
        lclSsAc.setApAmount(new BigDecimal(baseAutoCostAmt));
        GlMapping glmapping = new GlMappingDAO().findById(costCodeId);
        lclSsAc.setApGlMappingId(glmapping);
        lclSsAc.setArGlMappingId(glmapping);
        lclSsAc.setApReferenceNo(invNo);
        lclSsAc.setModifiedByUserId(loginUser);
        lclSsAc.setModifiedDatetime(now);
        new LclSsAcDAO().saveOrUpdate(lclSsAc);
        return lclSsAc;
    }

    public LclSsAc getCost(Long unitSsId, Long ssHeaderId, Integer costCodeId,
            Boolean manualEntry, String apType) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append(" FROM LclSsAc lba LEFT JOIN fetch lba.apGlMappingId gm WHERE lba.lclUnitSs.id=:unitSsId AND lba.lclSsHeader.id=:ssHeaderId ");
        query.append(" AND gm.id=:costCodeId AND lba.manualEntry =:manualEntry AND lba.apTransType=:type and lba.deleted=:deleteFlag");
        Query hqlQuery = getSession().createQuery(query.toString());
        hqlQuery.setParameter("unitSsId", unitSsId);
        hqlQuery.setParameter("ssHeaderId", ssHeaderId);
        hqlQuery.setParameter("costCodeId", costCodeId);
        hqlQuery.setParameter("manualEntry", manualEntry);
        hqlQuery.setParameter("type", apType);
        hqlQuery.setParameter("deleteFlag", false);
        LclSsAc list = (LclSsAc) hqlQuery.setMaxResults(1).uniqueResult();
        return list;
    }

    public void updateSsHeaderId(Long oldSsHeaderId, Long newSsHeaderId, Long unitSsId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_ss_ac set ss_header_id =:newSsHeaderId, ");
        queryStr.append(" modified_datetime=:dateTime,modified_by_user_id=:userId ");
        queryStr.append(" WHERE ss_header_id =:oldSsHeaderId and unit_ss_id=:unitSsId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("newSsHeaderId", newSsHeaderId);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("dateTime", new Date());
        queryObject.setParameter("oldSsHeaderId", oldSsHeaderId);
        queryObject.setParameter("unitSsId", unitSsId);
        queryObject.executeUpdate();
    }

    public Boolean isFAECostExists(Long headerId, String chargeCode) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append(" select if(count(*)> 0,TRUE,FALSE) as result from lcl_ss_Ac ac join lcl_ss_header lsh on lsh.id = ac.ss_header_id ");
        queryStr.append(" join gl_mapping gl on gl.id = ac.ap_gl_mapping_id and gl.charge_code =:chargeCode ");
        queryStr.append(" where ac.ss_header_id =:headerId ");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("headerId", headerId);
        queryObject.setParameter("chargeCode", chargeCode);
        queryObject.addScalar("result",BooleanType.INSTANCE);
        return (Boolean) queryObject.uniqueResult();
    }
    
    
}
