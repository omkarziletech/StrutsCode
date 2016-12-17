/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.hibernate.Domain;
import static com.gp.cong.lcl.common.constant.CommonConstant.TRUE;
import com.gp.cong.lcl.dwr.LclBlChargesCalculation;
import com.gp.cong.lcl.dwr.LclChargesCalculation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import com.gp.cong.logisoft.lcl.report.OceanManifestBean;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Thamizh
 */
public class LclBookingPieceDAO extends BaseHibernateDAO<LclBookingPiece> {

    public LclBookingPieceDAO() {
        super(LclBookingPiece.class);
    }

    public Domain saveAndReturn(Domain instance) throws Exception {
        LclBookingPiece lclBookingPiece = (LclBookingPiece) instance;
        getCurrentSession().saveOrUpdate(lclBookingPiece);
        getCurrentSession().flush();
        getCurrentSession().clear();
        return lclBookingPiece;
//        String queryString = "from LclBookingPiece where lclFileNumber.id='" + lclBookingPiece.getLclFileNumber().getId() + "' order by id desc";
//        return (LclBookingPiece) (getCurrentSession().createQuery(queryString).setMaxResults(1)).uniqueResult();
    }

    public List<LclBookingPiece> getLclBkgPieceList(Long fileId) throws Exception {
        String queryString = "from LclBookingPiece where lclFileNumber.id='" + fileId + "' order by id asc";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public List<LclBookingPiece> getLclBkgPieceList(String ids, Long fileId) throws Exception {
        String queryString = "from LclBookingPiece where lclFileNumber.id='" + fileId + "' and id NOT IN(" + ids + ") order by id asc";
        Query query = getSession().createQuery(queryString);
        List list = query.list();
        return list;
    }

    public String getWarehouseLocationByFileId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT CONCAT(w.warehsno,lbpw.location) FROM lcl_booking_piece lbp JOIN lcl_booking_piece_whse lbpw ON lbp.id = ").append("lbpw.booking_piece_id JOIN warehouse w ON w.id = lbpw.warehouse_id WHERE lbp.file_number_id = ").append(fileId).append(" ORDER BY lbp.id,lbpw.id DESC LIMIT 1");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            return o.toString();
        }
        return "";
    }

    public void deleteBkgPieceByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBookingPiece where lclFileNumber = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public void deleteBkgPieceById(Long id) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DELETE FROM lcl_booking_piece where id = ").append(id);
        getSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void getUnitssId(Long id) throws Exception {
        StringBuilder unitssId = new StringBuilder();
        unitssId.append("SELECT lbu.`lcl_unit_ss_id` FROM lcl_booking_piece_unit lbu WHERE booking_piece_id='");
        unitssId.append(id).append("'");
        BigInteger unitSSId = (BigInteger) getCurrentSession().createSQLQuery(unitssId.toString()).uniqueResult();
        deleteBkgPieceById(id);
        deleteBookingPiece(unitSSId);
    }

    public void deleteBookingPiece(BigInteger unitSSId) throws Exception {
        String updatePiece = "CALL UpdateUnitWMValue (?)";
        Query query = getCurrentSession().createSQLQuery(updatePiece);
        query.setParameter(0, unitSSId);
        query.executeUpdate();
    }

    public BigInteger getPieceCountByFileId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM lcl_booking_piece WHERE file_number_id = ").append(fileId);
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public LclBookingPiece getlclBkgPieceValues(Long fileId, String tariffNo) throws Exception {
        Criteria criteria = getSession().createCriteria(LclBookingPiece.class, "lclBkgPiece");
        criteria.createAlias("lclBkgPiece.commodityType", "commodity");
        if (CommonUtils.isNotEmpty(fileId)) {
            criteria.add(Restrictions.eq("lclFileNumber.id", fileId));
        }
        if (CommonUtils.isNotEmpty(tariffNo)) {
            criteria.add(Restrictions.eq("commodity.code", tariffNo));
        }
        return (LclBookingPiece) criteria.uniqueResult();
    }

    public String hasBookingPiece(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT IF(COUNT(*) > 0, 'true', 'false') FROM lcl_booking_piece");
        queryBuilder.append(" WHERE file_number_id = ").append(fileId);
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String isCommodityValidation(String fileId, String tariffCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT IF(COUNT(*) > 0, 'true', 'false') FROM lcl_booking_piece");
        sb.append(" WHERE file_number_id = ").append(fileId);
        sb.append(" AND ct.code=").append(tariffCode);
        return (String) getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
    }

    public String noOfDrBylclUnitSSId(String unitSSId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT COUNT(*) FROM lcl_booking_piece b JOIN `lcl_booking_piece_unit` u ");
        sb.append(" ON b.id=u.`booking_piece_id` WHERE u.lcl_unit_ss_id= ").append(unitSSId);
        Object count = getCurrentSession().createSQLQuery(sb.toString()).uniqueResult();
        return count.toString();
    }

    public Boolean isCommodityExists(Long fileNumberId, String commodityCode) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, true, false) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_booking_piece` bp,");
        queryBuilder.append("  `commodity_type` ct ");
        queryBuilder.append("where");
        queryBuilder.append("  bp.`file_number_id` = :fileNumberId");
        queryBuilder.append("  and bp.`commodity_type_id` = ct.`id`");
        queryBuilder.append("  and ct.`code` = :commodityCode");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setLong("fileNumberId", fileNumberId);
        query.setString("commodityCode", commodityCode);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public void bookedValueToActualValue(Long fileld) throws Exception {
        SQLQuery query = getCurrentSession().createSQLQuery("call insertLclBookingAcActualValue(:fileld)");
        query.setLong("fileld", fileld);
        query.executeUpdate();
    }

    public List<OceanManifestBean> getTotalWeightForComm(List fileId) throws Exception {
        String queryString = "SELECT "
                + " SUM(IF(b.`actual_piece_count` <> NULL OR  b.`actual_piece_count` <> 0.00, b.`actual_piece_count`,b.`booked_piece_count`)) AS piece, "
                + " SUM(IF(b.`actual_volume_imperial` <> NULL  OR   b.`actual_volume_imperial` <> 0.00, b.`actual_volume_imperial`,b.`booked_volume_imperial`)) AS cft, "
                + " SUM(IF(b.`actual_weight_imperial` <> NULL OR  b.`actual_weight_imperial` <> 0.00, b.`actual_weight_imperial`, b.`booked_weight_imperial`)) AS lbs, "
                + " SUM(IF(b.`actual_volume_metric` <> NULL OR  b.`actual_volume_metric` <> 0.00, b.`actual_volume_metric`,b.`booked_volume_metric`)) AS cbm, "
                + " SUM(IF(b.`actual_weight_metric` <> NULL OR  b.`actual_weight_metric` <> 0.00, b.`actual_weight_metric`,b.`booked_weight_metric`)) AS kgs "
                + " from lcl_booking_piece b where b.file_number_id IN (:fileId)";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setParameterList("fileId", fileId);
        query.addScalar("piece", IntegerType.INSTANCE);
        query.addScalar("cft", DoubleType.INSTANCE);
        query.addScalar("kgs", DoubleType.INSTANCE);
        query.addScalar("lbs", DoubleType.INSTANCE);
        query.addScalar("cbm", DoubleType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(OceanManifestBean.class));
        return query.list();
    }

    public void insertBookingPieceWhse(Long fileId, Integer userId, Integer wareHouseId) throws Exception {
        List<LclBookingPiece> picelist = new LclBookingPieceDAO().getLclBkgPieceList(fileId);
        if (CommonUtils.isNotEmpty(picelist)) {
            if (wareHouseId != null) {
                for (LclBookingPiece piece : picelist) {
                    new LclBookingPieceWhseDAO().insertLclBookingPieceWhse(piece.getId(), wareHouseId, userId);
                }
            }
        }
    }
    public void insertExportBookingPieceWhse(Long fileId, Integer userId, Integer wareHouseId) throws Exception {
        List<LclBookingPiece> picelist = new LclBookingPieceDAO().getLclBkgPieceList(fileId);
        if (CommonUtils.isNotEmpty(picelist)) {
            LclBookingPieceWhseDAO lclBookingPieceWhseDAO = new LclBookingPieceWhseDAO();
            if (wareHouseId != null) {
                for (LclBookingPiece piece : picelist) {
                    Integer warehouseId = lclBookingPieceWhseDAO.getWarehouseId(piece.getId());
                         
                    if(!CommonUtils.isEqual(warehouseId, wareHouseId)){
                    lclBookingPieceWhseDAO.insertLclBookingPieceWhse(piece.getId(), wareHouseId, userId);
                    }
                }
            }
        }
    }

    public String[] getTotalCommodity(String drNos) throws Exception {
        String[] fileDetails = new String[4];
        String queryString = "SELECT  SUM(booked_volume_metric) AS cbm, SUM(booked_weight_metric) AS kgs, SUM(booked_volume_imperial) AS cft, SUM(booked_weight_imperial) AS lbs  FROM lcl_booking_piece  WHERE file_number_id IN(" + drNos + ")";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);

        List agentList = queryObject.list();
        if (agentList != null && !agentList.isEmpty()) {
            Object[] agentObj = (Object[]) agentList.get(0);
            if (agentObj[0] != null && !agentObj[0].toString().trim().equals("")) {

                fileDetails[0] = agentObj[0].toString();
            }
            if (agentObj[1] != null && !agentObj[1].toString().trim().equals("")) {
                fileDetails[1] = agentObj[1].toString();
            }
            if (agentObj[2] != null && !agentObj[2].toString().trim().equals("")) {
                fileDetails[2] = agentObj[2].toString();
            }
            if (agentObj[3] != null && !agentObj[3].toString().trim().equals("")) {
                fileDetails[3] = agentObj[3].toString();
            }
        }
        return fileDetails;
    }

    public List<LclBookingPiece> getConsolidatePieceCount(List fileId) throws Exception {
        Query query = getSession().createQuery(" FROM  LclBookingPiece WHERE lclFileNumber.id IN(:fileId)");
        query.setParameterList("fileId", fileId);
        return query.list();
    }

    public void actualValueCopyToBl(String fileNumberId, String commodityId, String piece, String actualPackageTypeId, String cft, String cbm,
            String lbs, String kgs,String bookingStatus, HttpServletRequest request) throws Exception {
        String queryString = " UPDATE lcl_bl_piece "
                + " SET actual_piece_count = " + piece + ",packaging_type_id=" + actualPackageTypeId + ",actual_volume_imperial = " + cft + ",actual_volume_metric = " + cbm + ", "
                + " actual_weight_imperial = " + lbs + ", actual_weight_metric = " + kgs + " WHERE file_number_id = " + fileNumberId + " and"
                + " commodity_type_id=" + commodityId;
        Query queryObject = getSession().createSQLQuery(queryString);
        queryObject.executeUpdate();
        if("WV".equalsIgnoreCase(bookingStatus)){
            LclBl lclBl = new LCLBlDAO().getByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            List<LclBlPiece> lclBlPiecesList = new LclBLPieceDAO().findByProperty("lclFileNumber.id", Long.parseLong(fileNumberId));
            User user = (User) request.getSession().getAttribute("loginuser");
            String pcBoth = null != lclBl.getBillingType() ? lclBl.getBillingType() : "P";
            String Insurance = lclBl.getInsurance() == TRUE ? "Y" : "N";
            String rateType = "R".equalsIgnoreCase(lclBl.getRateType()) ? "Y" : lclBl.getRateType();
            LclBlChargesCalculation lclBlChargesCalculation = new LclBlChargesCalculation();
            lclBlChargesCalculation.calculateRates(lclBl.getPortOfOrigin().getUnLocationCode(), lclBl.getFinalDestination().getUnLocationCode(),
                    lclBl.getPortOfLoading().getUnLocationCode(), lclBl.getPortOfDestination().getUnLocationCode(), lclBl.getFileNumberId(),
                    lclBlPiecesList, user, null, Insurance, new BigDecimal(0.00), rateType,
                    "C", null, null, null, null, null, lclBl.getDeliveryMetro(), pcBoth, null, lclBl.getBillToParty(), request,false, false);
        }
    }
    /*
     * Update Values in OriginalDR by ShortShip and rates also recalculated
     *    @TicketNo 11577
     */

    public void updateBkgPiece(String fileNumber, Integer actualPiece, BigDecimal actualWeiImp, BigDecimal actualWeiMet,
            BigDecimal actualVolImp, BigDecimal actualVolMet, User loginUser, HttpServletRequest request) throws Exception {
        Long shortShipFileId = new LclFileNumberDAO().getFileId(fileNumber);
        List<LclBookingPiece> lclCommodityList = this.findByPropertyValues("lclFileNumber.id", shortShipFileId);
        boolean updateFlag = Boolean.FALSE;
        actualPiece = CommonUtils.isNotEmpty(actualPiece) ? actualPiece : 0;
        actualWeiImp = null != actualWeiImp ? actualWeiImp : new BigDecimal(BigInteger.ZERO);
        actualVolImp = null != actualVolImp ? actualVolImp : new BigDecimal(BigInteger.ZERO);
        actualWeiMet = null != actualWeiMet ? actualWeiMet : new BigDecimal(BigInteger.ZERO);
        actualVolMet = null != actualVolMet ? actualVolMet : new BigDecimal(BigInteger.ZERO);
        for (LclBookingPiece bookingPiece : lclCommodityList) {
            if (bookingPiece.getActualPieceCount() >= actualPiece && (bookingPiece.getActualWeightImperial().doubleValue() >= actualWeiImp.doubleValue()
                    && bookingPiece.getActualVolumeImperial().doubleValue() >= actualVolImp.doubleValue())) {

                bookingPiece.setActualPieceCount(bookingPiece.getActualPieceCount() - actualPiece);// piece

                bookingPiece.setActualWeightMetric(bookingPiece.getActualWeightMetric().subtract(actualWeiMet));//kgs
                bookingPiece.setActualWeightImperial(bookingPiece.getActualWeightImperial().subtract(actualWeiImp));//lbs

                bookingPiece.setActualVolumeMetric(bookingPiece.getActualVolumeMetric().subtract(actualVolMet));//cbm
                bookingPiece.setActualVolumeImperial(bookingPiece.getActualVolumeImperial().subtract(actualVolImp));//cft
                
                bookingPiece.setActualWeightMetric(bookingPiece.getActualWeightMetric().doubleValue() > 0.001 ? bookingPiece.getActualWeightMetric() : BigDecimal.ZERO);//KGS
                bookingPiece.setActualVolumeMetric(bookingPiece.getActualVolumeMetric().doubleValue() > 0.001 ? bookingPiece.getActualVolumeMetric() : BigDecimal.ZERO);//CBM
                
                updateFlag = Boolean.TRUE;
            }
            if (updateFlag) {
                bookingPiece.setModifiedBy(loginUser);
                bookingPiece.setModifiedDatetime(new Date());
                this.update(bookingPiece);
                LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
                LclBooking booking = lclBookingDAO.getByProperty("lclFileNumber.id", shortShipFileId);
                lclBookingDAO.getCurrentSession().evict(booking);
                List<LclBookingPiece> bookingPieceList = this.findByProperty("lclFileNumber.id", shortShipFileId);
                String rateType = "R".equalsIgnoreCase(booking.getRateType()) ? "Y" : booking.getRateType();
                String insurance = booking.getInsurance() ? "Y" : "N";
                if ("T".equalsIgnoreCase(booking.getBookingType())) {
                    new LclChargesCalculation().calculateRates(booking.getPortOfDestination().getUnLocationCode(),
                            booking.getFinalDestination().getUnLocationCode(), booking.getPortOfDestination().getUnLocationCode(),
                            booking.getFinalDestination().getUnLocationCode(), shortShipFileId, bookingPieceList, loginUser,
                            "N", insurance, booking.getValueOfGoods(), rateType, "C", null, null, null, null, false,
                            booking.getDeliveryMetro(), booking.getBillingType(), null, "", request, booking.getBillToParty());
                } else {
                    new LclChargesCalculation().calculateRates(booking.getPortOfOrigin().getUnLocationCode(),
                            booking.getFinalDestination().getUnLocationCode(), booking.getPortOfLoading().getUnLocationCode(),
                            booking.getPortOfDestination().getUnLocationCode(), shortShipFileId, bookingPieceList, loginUser,
                            "N", insurance, booking.getValueOfGoods(), rateType, "C", null, null, null, null, false,
                            booking.getDeliveryMetro(), booking.getBillingType(), null, "", request, booking.getBillToParty());
                }
                break;
            }
        }
    }

    public Object[] getBkgPieceOldValues(Long bkgPieceId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT ");
        queryBuilder.append(" actual_piece_count,actual_weight_imperial,actual_weight_metric,  ");
        queryBuilder.append(" actual_volume_imperial,actual_volume_metric ");
        queryBuilder.append(" FROM lcl_booking_piece WHERE id =:bkgPieceId ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("bkgPieceId", bkgPieceId);
        return (Object[]) query.uniqueResult();
    }
    /*
     * comparing OriginalDR and child DR values by ShortShip
     *    @TicketNo 11577
     */

    public Boolean getBkgPieceOldValues(String fileNumber, String pieceCount,
            String weightImp, String volumeImp) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean returnFlag = Boolean.TRUE;
        Integer actualPieceCount;
        boolean pieceFlag, weightImpFlag, volumeImpFlag;
        BigDecimal weightImpOrg = BigDecimal.ZERO, volumeImpOrg = BigDecimal.ZERO;
        List bkgPieceList = new ArrayList();
        sb.append(" SELECT ");
        sb.append("  bp.actual_piece_count,");
        sb.append("  bp.actual_weight_imperial,");
        sb.append("  bp.actual_weight_metric,");
        sb.append("  bp.actual_volume_imperial,");
        sb.append("  bp.actual_volume_metric ");
        sb.append(" FROM");
        sb.append("  lcl_booking_piece AS bp ");
        sb.append("  JOIN lcl_file_number AS fn ");
        sb.append("    ON fn.id = bp.file_number_id ");
        sb.append(" WHERE fn.file_number = :fileNumber");
        sb.append("  AND short_ship = :short ");
        sb.append("  AND (bp.actual_piece_count > 0 ");
        sb.append("  OR bp.actual_weight_imperial > 0.00 ");
        sb.append("  OR bp.actual_weight_metric > 0.00 ");
        sb.append("  OR bp.actual_volume_imperial > 0.00 ");
        sb.append("  OR bp.actual_volume_metric > 0.00) ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setString("fileNumber", fileNumber);
        query.setBoolean("short", false);
        bkgPieceList = query.list();
        for (int i = 0; i < bkgPieceList.size(); i++) {
            Object[] bkgPiece = (Object[]) bkgPieceList.get(i);
            if (bkgPieceList.size() >= 2) {
                actualPieceCount = Integer.parseInt(bkgPiece[0].toString());
                weightImpOrg = (BigDecimal) bkgPiece[1];
                volumeImpOrg = (BigDecimal) bkgPiece[3];
                pieceFlag = actualPieceCount - Integer.parseInt(pieceCount) >= 0;
                weightImpFlag = weightImpOrg.subtract(new BigDecimal(weightImp.isEmpty() ? "0.00" : weightImp)).doubleValue() >= 0.0;//lbs
                volumeImpFlag = volumeImpOrg.subtract(new BigDecimal(volumeImp.isEmpty() ? "0.00" : volumeImp)).doubleValue() >= 0.0;//cft
                if (pieceFlag && weightImpFlag && volumeImpFlag) {
                    returnFlag = Boolean.FALSE;
                    break;
                }
            } else {
                actualPieceCount = Integer.parseInt(bkgPiece[0].toString());
                weightImpOrg = (BigDecimal) bkgPiece[1];
                volumeImpOrg = (BigDecimal) bkgPiece[3];
                pieceFlag = actualPieceCount - Integer.parseInt(pieceCount) > 0;
                weightImpFlag = weightImpOrg.subtract(new BigDecimal(weightImp.isEmpty() ? "0.00" : weightImp)).doubleValue() > 0.0;//lbs
                volumeImpFlag = volumeImpOrg.subtract(new BigDecimal(volumeImp.isEmpty() ? "0.00" : volumeImp)).doubleValue() > 0.0;//cft
                if (pieceFlag && weightImpFlag && volumeImpFlag) {
                    returnFlag = Boolean.FALSE;
                    break;
                }
            }
        }
        return returnFlag;
    }
    
    public void updatePersonalEffects(Long fileId, String fieldName, String value, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking_piece set ").append(fieldName).append(" = '").append(value).append("',modified_by_user_id = ").append(userId).append(",modified_datetime = SYSDATE() where file_number_id = ").append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public boolean hasDojoList(String origin, String destination) throws Exception {
        String blueScreen_db = LoadLogisoftProperties.getProperty("elite.database.name");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT  IF(COUNT(*) > 0, 'true', 'false') as result FROM ");
        queryBuilder.append(blueScreen_db).append(".ofrate  ofr left JOIN  commodity_type ct ON ofr.comcde = ct.code WHERE ");
        queryBuilder.append(" ofr.oabcod!='A' and ofr.trmnum = '").append(origin);
        queryBuilder.append("' AND ofr.prtnum = '").append(destination);
        queryBuilder.append("' and (ct.desc_en like '%' or ct.code like '%') ORDER BY ct.desc_en ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }

    public boolean hasEmptyOriginDojoList() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*) > 0, 'true', 'false') as result FROM commodity_type ct ");
        queryBuilder.append(" WHERE (ct.desc_en LIKE '%' OR ct.code LIKE '%') ");
        queryBuilder.append(" ORDER BY ct.desc_en ");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }
    public boolean isBarrelComboShipment(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT IF(COUNT(*) > 0, 'true', 'false') as result FROM lcl_booking_piece ct ");
        queryBuilder.append(" WHERE ct.is_barrel = '1' and ct.file_number_id =").append(fileId);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }
    
     public void deleteNotesForCommodity(Long fileId, Integer userId) throws Exception {
        String query = "call delete_notes_for_commodity(:fileId,:userId)";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("userId", userId);
        queryObject.executeUpdate();
    }

    public void updateHazMatValue(String fileId, String fieldName, String value, String userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update lcl_booking_piece set ").append(fieldName).append(" = '").append(value).append("',modified_by_user_id = ").append(userId).append(",modified_datetime = SYSDATE() where file_number_id = ").append(fileId);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public String getWeightVolume(String fileId,String dispo) throws Exception {
        StringBuilder sb = new StringBuilder();
        if(dispo.equalsIgnoreCase("OBKG") || dispo.equalsIgnoreCase("RUNV")){
        sb.append("SELECT GROUP_CONCAT(IF(booked_weight_imperial IS NULL OR booked_weight_imperial =0.00,TRUE,'') ,");
        sb.append("IF (booked_volume_imperial IS NULL OR booked_volume_imperial =0.00,TRUE,''),");
        sb.append("IF(booked_weight_metric IS NULL OR booked_weight_metric=0.00,TRUE,''),");
        sb.append("IF(booked_volume_metric IS NULL OR booked_volume_metric=0.00,TRUE,''),");
        sb.append("IF(booked_piece_count IS NULL OR booked_piece_count = 0, TRUE, '')) AS WeightVolume  ");
        }else{
        sb.append("SELECT GROUP_CONCAT(IF(actual_weight_imperial IS NULL OR actual_weight_imperial =0.00,TRUE,'') ,");
        sb.append("IF (actual_volume_imperial IS NULL OR actual_volume_imperial =0.00,TRUE,''),");
        sb.append("IF(actual_weight_metric IS NULL OR actual_weight_metric=0.00,TRUE,''),");
        sb.append("IF(actual_volume_metric IS NULL OR actual_volume_metric=0.00,TRUE,''),");
        sb.append("IF(actual_piece_count IS NULL OR actual_piece_count = 0, TRUE, '')) AS WeightVolume  ");   
        }
        sb.append("FROM lcl_booking_piece  WHERE file_number_id=:fileId ");
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.addScalar("WeightVolume", StringType.INSTANCE);
        queryObject.setParameter("fileId", fileId);
        return (String) queryObject.uniqueResult();
    }
}