package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.model.LclDoorDeliverySearchBean;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.struts.form.lcl.lclDoorDeliverySearchForm;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author PALRAJ
 */
public class LclDoorDeliverySearchDAO extends BaseHibernateDAO {

    StringBuilder bulidQuery = new StringBuilder();
    private String whereCondition = "";

    public List<LclDoorDeliverySearchBean> search(lclDoorDeliverySearchForm lclDoorDeliverySearchForm) throws Exception {
        this.buildSelectQuery();
        this.buildDrUnitSearchQuery(lclDoorDeliverySearchForm);
        SQLQuery queryObject = getCurrentSession().createSQLQuery(bulidQuery.toString());
        queryObject = this.setValues(queryObject, lclDoorDeliverySearchForm);
        queryObject.setResultTransformer(Transformers.aliasToBean(LclDoorDeliverySearchBean.class));
        queryObject.addScalar("voyageNo", StringType.INSTANCE);
        queryObject.addScalar("polUnCode", StringType.INSTANCE);
        queryObject.addScalar("pol", StringType.INSTANCE);
        queryObject.addScalar("podUnCode", StringType.INSTANCE);
        queryObject.addScalar("pod", StringType.INSTANCE);
        queryObject.addScalar("etaDate", StringType.INSTANCE);
        queryObject.addScalar("dispoCode", StringType.INSTANCE);
        queryObject.addScalar("dispoDesc", StringType.INSTANCE);
        queryObject.addScalar("lastFreeDate", StringType.INSTANCE);
        queryObject.addScalar("fileNumberId", LongType.INSTANCE);
        queryObject.addScalar("fileNumber", StringType.INSTANCE);
        queryObject.addScalar("houseBl", StringType.INSTANCE);
        queryObject.addScalar("customsClearanceReceived", StringType.INSTANCE);
        queryObject.addScalar("deliveryOrderReceived", StringType.INSTANCE);
        queryObject.addScalar("deliveryCommercial", StringType.INSTANCE);
        queryObject.addScalar("liftGate", StringType.INSTANCE);
        queryObject.addScalar("hazmat", BooleanType.INSTANCE);
        queryObject.addScalar("pickupEstDate", StringType.INSTANCE);
        queryObject.addScalar("pickedUpDateTime", StringType.INSTANCE);
        queryObject.addScalar("deliveryEstDate", StringType.INSTANCE);
        queryObject.addScalar("deliveredDateTime", StringType.INSTANCE);
        queryObject.addScalar("buy", DoubleType.INSTANCE);
        queryObject.addScalar("sell", DoubleType.INSTANCE);
        queryObject.addScalar("doorDeliveryStatus", StringType.INSTANCE);
        queryObject.addScalar("doorDeliveryDesc", StringType.INSTANCE);
        queryObject.addScalar("scacCode", StringType.INSTANCE);
        queryObject.addScalar("carrierName", StringType.INSTANCE);
        queryObject.addScalar("pickupReferenceNo", StringType.INSTANCE);
        queryObject.addScalar("needPod", StringType.INSTANCE);
        queryObject.addScalar("state", StringType.INSTANCE);
        queryObject.addScalar("transshipment", BooleanType.INSTANCE);
        queryObject.addScalar("ZipCode", StringType.INSTANCE);
        queryObject.addScalar("City", StringType.INSTANCE);
        return queryObject.list();
    }

    private void buildSelectQuery() {
        bulidQuery.append("SELECT ");
        bulidQuery.append("   fn.`schedule_no` AS voyageNo,");
        bulidQuery.append("  `UnLocationGetCodeByID` (fn.`pol_id`) AS polUncode,");
        bulidQuery.append("  `UnLocationGetNameStateCntryByID` (fn.`pol_id`) AS pol,");
        bulidQuery.append("  `UnLocationGetCodeByID` (fn.`pod_id`) AS podUncode,");
        bulidQuery.append("  `UnLocationGetNameStateCntryByID` (fn.`pod_id`) AS pod,");
        bulidQuery.append("  DATE_FORMAT(fn.etaDate, '%d-%b-%Y') AS etaDate,");
        bulidQuery.append("  fn.`state` AS state,");
        bulidQuery.append("  fn.`transshipment` AS transshipment,");
        bulidQuery.append("  d.`elite_code` AS dispoCode, ");
        bulidQuery.append("  d.`description` AS dispoDesc, ");
        bulidQuery.append(" DATE_FORMAT(fn.last_free_date, '%d-%b-%Y') AS lastFreeDate,");
        bulidQuery.append(" fn.`fileId` AS fileNumberId,");
        bulidQuery.append(" fn.`file_number` AS fileNumber,");
        bulidQuery.append(" fn.sub_house_bl AS houseBl,");
        bulidQuery.append(" COALESCE(fn.`hazmat`, 0) AS hazmat, ");
        bulidQuery.append(" DATE_FORMAT(fn.`picked_up_datetime`,'%d-%b-%Y') AS pickedUpDateTime, ");
        bulidQuery.append("  DATE_FORMAT(fn.`customs_clearance_received`,'%d-%b-%Y') AS customsClearanceReceived, ");
        bulidQuery.append(" DATE_FORMAT(fn.`delivery_order_received`,'%d-%b-%Y') AS deliveryOrderReceived,");
        bulidQuery.append(" IF(fn.delivery_commercial IS TRUE,'Y','N') AS deliveryCommercial, ");
        bulidQuery.append(" IF(fn.lift_gate IS TRUE, 'Y', 'N') AS liftGate, ");
        bulidQuery.append(" DATE_FORMAT(fn.pickup_est_date, '%d-%b-%Y') AS pickupEstDate, ");
        bulidQuery.append(" DATE_FORMAT(fn.`delivery_est_date`,'%d-%b-%Y') AS deliveryEstDate, ");
        bulidQuery.append(" DATE_FORMAT(fn.`delivered_datetime`,'%d-%b-%Y') AS deliveredDateTime, ");
        bulidQuery.append(" `doorDeliveryBuyAmount` (fn.`fileId`) AS buy, ");
        bulidQuery.append(" `doorDeliverySellAmount` (fn.`fileId`) AS sell, ");
        bulidQuery.append(" fn.door_delivery_status AS doorDeliveryStatus, ");
        bulidQuery.append(" `GenericcodeDupForDoorDelivery` (fn.`door_delivery_status`) AS doorDeliveryDesc, ");
        bulidQuery.append(" fn.scac AS scacCode, ");
        bulidQuery.append(" `doorDeliveryForCarrier`(fn.`scac`) AS carrierName, ");
        bulidQuery.append(" fn.pickup_reference_no AS pickupReferenceNo, ");
        bulidQuery.append(" IF(fn.delivery_need_proof IS TRUE,'Y','N') AS needPod, ");
        bulidQuery.append( "  fn.zipCode AS zipCode,  ");
        bulidQuery.append("  fn.city AS city" );

    }

    private void buildDrUnitSearchQuery(lclDoorDeliverySearchForm lclDoorDeliverySearchForm) throws Exception {
        bulidQuery.append(" FROM ");
        bulidQuery.append("  (SELECT ");
        bulidQuery.append("    fn.`id` AS fileId,");
        bulidQuery.append("    fn.`created_datetime` AS entered_datetime,");
        bulidQuery.append("    sd.`sta` AS etaDate,");
        bulidQuery.append("    fn.`file_number`,");
        bulidQuery.append("    fn.`state`,");
        bulidQuery.append("    COALESCE(bki.`transshipment` AND bk.`booking_type` = 'T', 0) AS transshipment,");
        bulidQuery.append("    MAX(bkp.`hazmat`) AS hazmat,");
        bulidQuery.append("    bkp.`id` AS booking_piece_id,");
        bulidQuery.append("    bk.`pol_id` AS pol_id,");
        bulidQuery.append("    bk.`pod_id` AS pod_id,");
        bulidQuery.append("    bk.`poo_pickup` AS poo_pickup,");
        bulidQuery.append("    lbp.pickedup_datetime AS picked_up_datetime,");
        bulidQuery.append("    lbp.last_free_date AS last_free_date,");
        bulidQuery.append("    bki.sub_house_bl AS sub_house_bl,");
        bulidQuery.append("    bki.customs_clearance_received AS customs_clearance_received,");
        bulidQuery.append("    bki.delivery_order_received AS delivery_order_received,");
        bulidQuery.append("    lbp.delivery_commercial AS delivery_commercial,");
        bulidQuery.append("    lbp.lift_gate AS lift_gate,");
        bulidQuery.append("    lbp.pickup_est_date AS pickup_est_date,");
        bulidQuery.append("    lbp.delivery_est_date AS delivery_est_date,");
        bulidQuery.append("    lbp.delivered_datetime AS delivered_datetime,");
        bulidQuery.append("    lbp.scac AS scac,");
        bulidQuery.append("    lbp.pickup_reference_no AS pickup_reference_no,");
        bulidQuery.append("    lbp.delivery_need_proof AS delivery_need_proof,");
        bulidQuery.append("    bki.door_delivery_status AS door_delivery_status,");
        bulidQuery.append("    sd.id AS sdId,");
        bulidQuery.append("    lsh.`schedule_no` AS schedule_no, ");
        bulidQuery.append("    MAX(usd.`id`) AS usd_id, ");
        bulidQuery.append("    SUBSTRING_INDEX(lbp.`pickup_city`,'-',1) AS zipCode, " );
        bulidQuery.append("   SUBSTRING_INDEX(lbp.`pickup_city`,'-',-1) AS city ");
        bulidQuery.append("   FROM");
        bulidQuery.append("    `lcl_ss_detail` sd ");
        bulidQuery.append("     JOIN `lcl_unit_ss` us ON (sd.`ss_header_id` = us.`ss_header_id`)");
        bulidQuery.append("     JOIN lcl_unit_ss_dispo usd ON (sd.`id` = usd.`ss_detail_id`) ");
        bulidQuery.append("    JOIN `lcl_booking_piece_unit` bpu ON (us.`id` = bpu.`lcl_unit_ss_id`) ");
        bulidQuery.append("    JOIN `lcl_ss_header` lsh ON (us.`ss_header_id` = lsh.`id`) and lsh.service_type IN ('I') ");
        bulidQuery.append("    JOIN `lcl_booking_piece` bkp ON (bkp.`id` = bpu.`booking_piece_id`)");
        bulidQuery.append("    JOIN `lcl_booking` bk ON (bk.`file_number_id` = bkp.`file_number_id` AND bk.`booking_type` IN ('I', 'T') AND bk.`poo_pickup` IS TRUE)");
        bulidQuery.append("    JOIN `lcl_booking_import` bki ON (bki.`file_number_id` = bk.`file_number_id`) ");
        bulidQuery.append("    JOIN `lcl_booking_pad` lbp ON (bk.`file_number_id` = lbp.`file_number_id`)");
        bulidQuery.append("    JOIN `lcl_file_number` fn ON (fn.`id` = bk.`file_number_id` AND fn.`status` <> 'X') ");
        bulidQuery.append("");
        this.buildFilterqueryNew(lclDoorDeliverySearchForm);
        bulidQuery.append(" ) AS fn");
        bulidQuery.append("  JOIN `lcl_unit_ss_dispo` usd ");
        bulidQuery.append("  ON (fn.`usd_id` = usd.`id`) ");
        bulidQuery.append("  JOIN `disposition` d ");
        bulidQuery.append("  ON (usd.`disposition_id` = d.`id`)");
        bulidQuery.append(this.whereCondition);
        bulidQuery.append(getSort(lclDoorDeliverySearchForm));
    }

    private StringBuilder getSort(lclDoorDeliverySearchForm lclDoorDeliverySearchForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getOrderBy())) {
            if ("up".equalsIgnoreCase(lclDoorDeliverySearchForm.getSortBy())) {
                if (lclDoorDeliverySearchForm.getOrderBy().equals("etaDate")) {
                    queryBuilder.append("").append(" order by fn.").append(lclDoorDeliverySearchForm.getOrderBy()).append(" asc").append(" limit :limit ");
                } else {
                    queryBuilder.append("").append(" order by ").append(lclDoorDeliverySearchForm.getOrderBy()).append(" asc").append(" limit :limit ");
                }
            } else {
                if (("etaDate").equals(lclDoorDeliverySearchForm.getOrderBy())) {
                    queryBuilder.append("").append(" order by fn.").append(lclDoorDeliverySearchForm.getOrderBy()).append(" desc").append(" limit :limit ");
                } else {
                    queryBuilder.append("").append(" order by ").append(lclDoorDeliverySearchForm.getOrderBy()).append(" desc").append(" limit :limit ");
                }
            }
        } else {
            queryBuilder.append(" order by fn.etaDate ASC, fn.`fileId` ASC").append(" limit :limit ");
        }
        return queryBuilder;
    }

    private SQLQuery setValues(SQLQuery queryObject, lclDoorDeliverySearchForm lclDoorDeliverySearchForm) throws Exception {
        String dateFormateChanged = "";
        queryObject.setInteger("limit", lclDoorDeliverySearchForm.getLimit());
        if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getVoyageNo())) {
            queryObject.setString("voyageNo", lclDoorDeliverySearchForm.getVoyageNo() + "%");
        } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getFileNumber())) {
            queryObject.setString("fileNumber", lclDoorDeliverySearchForm.getFileNumber() + "%");
        } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getHblNo())) {
            queryObject.setString("bkSubHouseBl", lclDoorDeliverySearchForm.getHblNo());
        } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getProNo())) {
            queryObject.setString("bkProNo", lclDoorDeliverySearchForm.getProNo());
        } else {
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPolCountryCode()) || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPodCountryCode())) {
                if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPolCountryCode())) {
                    queryObject.setString("bkPol", lclDoorDeliverySearchForm.getPolCountryCode());
                }
                if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPodCountryCode())) {
                    queryObject.setString("bkPod", lclDoorDeliverySearchForm.getPodCountryCode());
                }
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDispositionId())) {
                queryObject.setInteger("dispositionId", lclDoorDeliverySearchForm.getDispositionId());
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getScacCode())) {
                queryObject.setString("scacCode", lclDoorDeliverySearchForm.getScacCode());
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDoorDeliveryStatus())) {
                queryObject.setString("doorDeliveryStatus", lclDoorDeliverySearchForm.getDoorDeliveryStatus());
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getActualPickupDate())) {
                dateFormateChanged = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getActualPickupDate());
                queryObject.setString("actualPickupDate", dateFormateChanged + "%");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEstimatedDeliveryDate())) {
                dateFormateChanged = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getEstimatedDeliveryDate());
                queryObject.setString("estimateDeliveryDate", dateFormateChanged + "%");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getActualDeliveryDate())) {
                dateFormateChanged = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getActualDeliveryDate());
                queryObject.setString("actualDeliveryDate", dateFormateChanged + "%");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEstimatePickupDate())) {
                dateFormateChanged = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getEstimatePickupDate());
                queryObject.setString("estimatePickupDate", dateFormateChanged + "%");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getLastFreeDate())) {
                dateFormateChanged = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getLastFreeDate());
                queryObject.setString("lastFreeDate", dateFormateChanged + "%");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDeliveryOrder())) {
                dateFormateChanged = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getDeliveryOrder());
                queryObject.setString("deliveryOrderReceived", dateFormateChanged + "%");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getCustomsClearanceReceived())) {
                dateFormateChanged = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getCustomsClearanceReceived());
                queryObject.setString("customsClearanceRec", dateFormateChanged + "%");
            }
            
        }
        return queryObject;
    }

    private void buildFilterqueryNew(lclDoorDeliverySearchForm lclDoorDeliverySearchForm) throws Exception {
        boolean flag = Boolean.TRUE;
        boolean flagForAndCondition = Boolean.FALSE;
        if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getVoyageNo())) {
            bulidQuery.append(" WHERE lsh.`schedule_no` like :voyageNo ");
            flag = Boolean.FALSE;
        } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getFileNumber())) {
            bulidQuery.append(" WHERE fn.`file_number` LIKE :fileNumber ");
            flag = Boolean.FALSE;
        } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getHblNo())) {
            bulidQuery.append(" WHERE bki.`sub_house_bl` LIKE :bkSubHouseBl ");
            flag = Boolean.FALSE;
        } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getProNo())) {
            bulidQuery.append(" WHERE lbp.`pickup_reference_no` LIKE :bkProNo  ");
            flag = Boolean.FALSE;
        }  else {
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDoorDeliveryStatus()) || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getHazmat())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDeliveryProof()) || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getLiftGate())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getCommercialDelivery()) || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getActualPickupDate())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEstimatedDeliveryDate()) || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getActualDeliveryDate())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEstimatePickupDate()) || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getLastFreeDate())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDeliveryOrder()) || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getCustomsClearanceReceived())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getScacCode())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPolCountryCode())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPodCountryCode())
                    || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDispositionId())) {
                bulidQuery.append(" where");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPolCountryCode()) || CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPodCountryCode())) {
                boolean flag1 = Boolean.FALSE;
                if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPolCountryCode())) {
                    bulidQuery.append(" bk.`pol_id` = :bkPol ").append(" AND");
                    flag1 = Boolean.TRUE;
                }
                if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPodCountryCode()) && flag1) {
                    bulidQuery.append("  bk.`pod_id` = :bkPod").append(" AND");;
                } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getPodCountryCode())) {
                    bulidQuery.append(" bk.`pod_id` = :bkPod ").append(" AND");
                }
              flagForAndCondition=Boolean.TRUE;
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDispositionId())) {
                this.whereCondition = " where usd.`disposition_id` =:dispositionId ";
                flagForAndCondition=Boolean.TRUE;
            }
            
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDoorDeliveryStatus())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  bki.`door_delivery_status` = :doorDeliveryStatus ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getHazmat())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  ").append("Y".equalsIgnoreCase(lclDoorDeliverySearchForm.getHazmat()) ? "bkp.`hazmat` IS TRUE" : "bkp.`hazmat` IS FALSE ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDeliveryProof())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append(" ").append("Y".equalsIgnoreCase(lclDoorDeliverySearchForm.getDeliveryProof()) ? "lbp.`delivery_need_proof` IS TRUE" : "lbp.`delivery_need_proof` IS FALSE ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getLiftGate())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  ").append("Y".equalsIgnoreCase(lclDoorDeliverySearchForm.getLiftGate()) ? "lbp.`lift_gate` IS TRUE" : "lbp.`lift_gate` IS FALSE ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getCommercialDelivery())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append(" ").append("Y".equalsIgnoreCase(lclDoorDeliverySearchForm.getCommercialDelivery()) ? "lbp.`delivery_commercial` IS TRUE" : "lbp.`delivery_commercial` IS FALSE ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getActualPickupDate())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  lbp.`pickedup_datetime` LIKE :actualPickupDate ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEstimatedDeliveryDate())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  lbp.`delivery_est_date` LIKE :estimateDeliveryDate ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getActualDeliveryDate())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  lbp.`delivered_datetime` LIKE :actualDeliveryDate ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEstimatePickupDate())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  lbp.`pickup_est_date` LIKE :estimatePickupDate ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getLastFreeDate())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  lbp.`last_free_date` LIKE :lastFreeDate ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDeliveryOrder())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("   bki.`delivery_order_received` LIKE :deliveryOrderReceived  ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getCustomsClearanceReceived())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("  bki.`customs_clearance_received` LIKE :customsClearanceRec ").append(" AND ");
            }
            if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getScacCode())) {
                flagForAndCondition = Boolean.TRUE;
                bulidQuery.append("    lbp.`scac` =(SELECT scac FROM carriers_or_line WHERE scac = :scacCode OR carrier_name = :scacCode LIMIT 1)").append(" AND ");
            }
        }
       if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEtaDate()) && CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEtaToDate()) && flag && flagForAndCondition && CommonUtils.isEmpty(lclDoorDeliverySearchForm.getDoorDeliveryStatus())) {
            String startDate = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getEtaDate());
            String endDate = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getEtaToDate());
            bulidQuery.append("  bki.`door_delivery_status` NOT IN ('D','F') ");
            bulidQuery.append(" AND date(sd.`sta`) BETWEEN '").append(startDate).append("' AND '").append(endDate).append("'");

        } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEtaDate()) && CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEtaToDate()) && flag && CommonUtils.isEmpty(lclDoorDeliverySearchForm.getDoorDeliveryStatus())) {
            String startDate = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getEtaDate());
            String endDate = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getEtaToDate());
            bulidQuery.append(" WHERE bki.`door_delivery_status` NOT IN ('D','F') ");
            bulidQuery.append(" AND date(sd.`sta`) BETWEEN '").append(startDate).append("' AND '").append(endDate).append("'");

        } else if (CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEtaDate()) && CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getEtaToDate()) && flag && flagForAndCondition && CommonUtils.isNotEmpty(lclDoorDeliverySearchForm.getDoorDeliveryStatus())) {
            String startDate = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getEtaDate());
            String endDate = DateUtils.parseDateToMYSQLFormat(lclDoorDeliverySearchForm.getEtaToDate());
            bulidQuery.append(" date(sd.`sta`) BETWEEN '").append(startDate).append("' AND '").append(endDate).append("'");

        }
        bulidQuery.append(" GROUP BY fileId   ");
        bulidQuery.append(" order by etaDate ASC, fileId ASC ");
        
    }

}
