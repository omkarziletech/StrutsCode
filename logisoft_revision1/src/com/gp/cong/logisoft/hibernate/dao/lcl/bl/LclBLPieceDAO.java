/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl.bl;

import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.lcl.report.OceanManifestBean;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Thamizh
 */
public class LclBLPieceDAO extends BaseHibernateDAO<LclBlPiece> {

    public LclBLPieceDAO() {
        super(LclBlPiece.class);
    }

    public void saveAndReturn(Domain instance) throws Exception {
        getCurrentSession().saveOrUpdate(instance);
        getCurrentSession().flush();
        getCurrentSession().clear();
        //return (LclBlPiece)(getCurrentSession().createQuery("FROM LclBlPiece ORDER by id DESC").setMaxResults(1)).uniqueResult();
    }

    public BigInteger getPieceCountByFileId(Long fileId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM lcl_bl_piece WHERE file_number_id = ").append(fileId);
        return (BigInteger) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public void deleteBlPieceByFileNumber(Long fileNumberId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete LclBlPiece where lclFileNumber.id = ").append(fileNumberId);
        getSession().createQuery(queryBuilder.toString()).executeUpdate();
    }

    public List<OceanManifestBean> getPickedDRLclBlData(Long unitSsId, Long headerId, boolean manifestFlag) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT bkp.fileNumber, lb.file_number_id AS fileId,");
        sb.append(" blpiece.`id` AS blId,lb.`poo_id` AS unLocId, ");
        sb.append(" IF(lb.fd_id IS NOT NULL,lb.fd_id,lb.`pod_id`) AS podId, ");
        sb.append(" lb.`ship_contact_id` AS shipperId,lb.`cons_contact_id` AS consigneeId, ");
        sb.append(" lb.`noty_contact_id` AS notifyId, ");
        sb.append(" blpiece.marksDesc, blpiece.piece, blpiece.cft,blpiece.cbm,blpiece.lbs,blpiece.kgs, blpiece.piece,");
        sb.append(" com.`desc_en` AS commodityType, pac.`abbr01` AS packageType,pac.description AS packageName, ");
        sb.append(" blpiece.comDescrption  ");
        sb.append(" FROM ( SELECT lc.`file_number` AS fileNumber, bp.file_number_id AS fileId, bu.`booking_piece_id` AS bookingPiece  ");
        sb.append(" FROM lcl_booking_piece bp JOIN lcl_booking_piece_unit bu ON bu.booking_piece_id = bp.id  ");
        sb.append(" JOIN lcl_file_number lc  ON  lc.`id` = bp.`file_number_id`  ");
        sb.append(" JOIN lcl_unit_ss luss ON bu.`lcl_unit_ss_id` = luss.`id`   ");
        sb.append(" WHERE bu.`lcl_unit_ss_id` =:unitSsId AND luss.`ss_header_id` =:headerId  ");
        if (manifestFlag) {
            sb.append(" and lc.status='M' ");
        }
        sb.append(" group by  lc.`id` ) as bkp ");
        sb.append(" JOIN lcl_bl lb ON lb.`file_number_id` = bkp.fileId ");
        sb.append(" JOIN (SELECT lblp.id AS id,lblp.`mark_no_desc` AS marksDesc,  ");
        sb.append(" lblp.`piece_desc` AS comDescrption,lblp.`packaging_type_id` AS package_id, ");
        sb.append(" lblp.`commodity_type_id` AS commodity_id,lblp.file_number_id, ");
        sb.append(" SUM(IF(lblp.`actual_piece_count` = 0 OR ISNULL(lblp.`actual_piece_count`), ");
        sb.append(" lblp.`booked_piece_count`,lblp.`actual_piece_count`) ) AS piece, ");
        sb.append(" SUM(IF(lblp.`actual_volume_imperial` = 0 OR ISNULL(lblp.`actual_volume_imperial`), ");
        sb.append(" lblp.`booked_volume_imperial`,lblp.`actual_volume_imperial`)) AS cft, ");
        sb.append(" SUM(IF( lblp.`actual_volume_metric` = 0 OR ISNULL(lblp.`actual_volume_metric`), ");
        sb.append(" lblp.`booked_volume_metric`,lblp.`actual_volume_metric`)) AS cbm, ");
        sb.append(" SUM(IF( lblp.`actual_weight_imperial` = 0 OR ISNULL(lblp.`actual_weight_imperial`), ");
        sb.append(" lblp.`booked_weight_imperial`,lblp.`actual_weight_imperial`)) AS lbs, ");
        sb.append(" SUM(IF( lblp.`actual_weight_metric` = 0 OR ISNULL(lblp.`actual_weight_metric`), ");
        sb.append(" lblp.`booked_weight_metric`,lblp.`actual_weight_metric`)) AS kgs ");
        sb.append(" FROM lcl_bl_piece lblp GROUP BY lblp.file_number_id,lblp.`commodity_type_id`,lblp.`packaging_type_id`) blpiece  ");
        sb.append(" ON blpiece.`file_number_id` = bkp.fileId ");
        sb.append(" JOIN package_type pac ON pac.id = blpiece.`package_id` ");
        sb.append(" JOIN commodity_type com ON com.`id` = blpiece.`commodity_id` group by bkp.fileNumber");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setLong("unitSsId", unitSsId);
        query.setLong("headerId", headerId);
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("marksDesc", StringType.INSTANCE);
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("blId", LongType.INSTANCE);
        query.addScalar("unLocId", LongType.INSTANCE);
        query.addScalar("podId", LongType.INSTANCE);
        query.addScalar("consigneeId", LongType.INSTANCE);
        query.addScalar("notifyId", LongType.INSTANCE);
        query.addScalar("shipperId", LongType.INSTANCE);
        query.addScalar("piece", IntegerType.INSTANCE);
        query.addScalar("cft", DoubleType.INSTANCE);
        query.addScalar("kgs", DoubleType.INSTANCE);
        query.addScalar("lbs", DoubleType.INSTANCE);
        query.addScalar("cbm", DoubleType.INSTANCE);
        query.addScalar("commodityType", StringType.INSTANCE);
        query.addScalar("packageType", StringType.INSTANCE);
        query.addScalar("packageName", StringType.INSTANCE);
        query.addScalar("comDescrption", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(OceanManifestBean.class));
        return query.list();
    }
    
     public List<OceanManifestBean> getPickedDRList(Long unitSsId, Long headerId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT bkp.fileNumber, lb.file_number_id AS fileId,");
        sb.append(" blpiece.`id` AS blId,lb.`poo_id` AS unLocId, ");
        sb.append(" IF(lb.fd_id IS NOT NULL,lb.fd_id,lb.`pod_id`) AS podId, ");
        sb.append(" lb.`ship_contact_id` AS shipperId,lb.`cons_contact_id` AS consigneeId, ");
        sb.append(" lb.`noty_contact_id` AS notifyId, ");
        sb.append(" blpiece.marksDesc, blpiece.piece, blpiece.cft,blpiece.cbm,blpiece.lbs,blpiece.kgs, blpiece.piece,");
        sb.append(" com.`desc_en` AS commodityType, pac.`abbr01` AS packageType,pac.description AS packageName, ");
        sb.append(" blpiece.comDescrption,(SELECT COUNT(*) FROM lcl_bl_piece WHERE file_number_id=lb.file_number_id) AS totalFile  ");
        sb.append(" FROM ( SELECT lc.`file_number` AS fileNumber, bp.file_number_id AS fileId, bu.`booking_piece_id` AS bookingPiece  ");
        sb.append(" FROM lcl_booking_piece bp JOIN lcl_booking_piece_unit bu ON bu.booking_piece_id = bp.id  ");
        sb.append(" JOIN lcl_file_number lc  ON  lc.`id` = bp.`file_number_id`  ");
        sb.append(" JOIN lcl_unit_ss luss ON bu.`lcl_unit_ss_id` = luss.`id`   ");
        sb.append(" WHERE bu.`lcl_unit_ss_id` =:unitSsId AND luss.`ss_header_id` =:headerId  ");
        sb.append(" group by  lc.`id` ) as bkp ");
        sb.append(" JOIN lcl_bl lb ON lb.`file_number_id` = bkp.fileId ");
        sb.append(" JOIN (SELECT lblp.id AS id,lblp.`mark_no_desc` AS marksDesc,  ");
        sb.append(" lblp.`piece_desc` AS comDescrption,lblp.`packaging_type_id` AS package_id, ");
        sb.append(" lblp.`commodity_type_id` AS commodity_id,lblp.file_number_id, ");
        sb.append(" SUM(IF(lblp.`actual_piece_count` = 0 OR ISNULL(lblp.`actual_piece_count`), ");
        sb.append(" lblp.`booked_piece_count`,lblp.`actual_piece_count`) ) AS piece, ");
        sb.append(" SUM(IF(lblp.`actual_volume_imperial` = 0 OR ISNULL(lblp.`actual_volume_imperial`), ");
        sb.append(" lblp.`booked_volume_imperial`,lblp.`actual_volume_imperial`)) AS cft, ");
        sb.append(" SUM(IF( lblp.`actual_volume_metric` = 0 OR ISNULL(lblp.`actual_volume_metric`), ");
        sb.append(" lblp.`booked_volume_metric`,lblp.`actual_volume_metric`)) AS cbm, ");
        sb.append(" SUM(IF( lblp.`actual_weight_imperial` = 0 OR ISNULL(lblp.`actual_weight_imperial`), ");
        sb.append(" lblp.`booked_weight_imperial`,lblp.`actual_weight_imperial`)) AS lbs, ");
        sb.append(" SUM(IF( lblp.`actual_weight_metric` = 0 OR ISNULL(lblp.`actual_weight_metric`), ");
        sb.append(" lblp.`booked_weight_metric`,lblp.`actual_weight_metric`)) AS kgs ");
        sb.append(" FROM lcl_bl_piece lblp GROUP BY lblp.id) blpiece  ");
        sb.append(" ON blpiece.`file_number_id` = bkp.fileId ");
        sb.append(" JOIN package_type pac ON pac.id = blpiece.`package_id` ");
        sb.append(" JOIN commodity_type com ON com.`id` = blpiece.`commodity_id` ");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        query.setLong("unitSsId", unitSsId);
        query.setLong("headerId", headerId);
        query.addScalar("fileNumber", StringType.INSTANCE);
        query.addScalar("marksDesc", StringType.INSTANCE);
        query.addScalar("fileId", LongType.INSTANCE);
        query.addScalar("blId", LongType.INSTANCE);
        query.addScalar("unLocId", LongType.INSTANCE);
        query.addScalar("podId", LongType.INSTANCE);
        query.addScalar("consigneeId", LongType.INSTANCE);
        query.addScalar("notifyId", LongType.INSTANCE);
        query.addScalar("shipperId", LongType.INSTANCE);
        query.addScalar("piece", IntegerType.INSTANCE);
        query.addScalar("cft", DoubleType.INSTANCE);
        query.addScalar("kgs", DoubleType.INSTANCE);
        query.addScalar("lbs", DoubleType.INSTANCE);
        query.addScalar("cbm", DoubleType.INSTANCE);
        query.addScalar("commodityType", StringType.INSTANCE);
        query.addScalar("packageType", StringType.INSTANCE);
        query.addScalar("packageName", StringType.INSTANCE);
        query.addScalar("comDescrption", StringType.INSTANCE);
        query.addScalar("totalFile", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(OceanManifestBean.class));
        return query.list();
    }
    public boolean checkCommodityExist(String fileNumberId,String commodityTypeId,boolean isBarrel) throws Exception {
        String queryString = null;
        if(isBarrel){
            queryString = " SELECT IF(COUNT(*) > 0, 'true', 'false') as result FROM lcl_bl_piece  WHERE file_number_id = " + fileNumberId + " and"
                    + " commodity_type_id=" + commodityTypeId+" and is_barrel = 1";
        }else{
            queryString = " SELECT IF(COUNT(*) > 0, 'true', 'false') as result FROM lcl_bl_piece  WHERE file_number_id = " + fileNumberId + " and"
                    + " commodity_type_id=" + commodityTypeId+" and is_barrel = 0";
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        return (boolean) query.addScalar("result", BooleanType.INSTANCE).uniqueResult();
    }
    
    public void deleteNotesForCommodity(Long fileId, Integer userId) throws Exception {
        String query = "call delete_notes_for_commodity(:fileId,:userId)";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("fileId", fileId);
        queryObject.setParameter("userId", userId);
        queryObject.executeUpdate();
    }
}
