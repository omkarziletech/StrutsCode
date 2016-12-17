/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.beans.ManifestBean;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnit;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import java.math.BigDecimal;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;

/**
 *
 * @author Logiware
 */
public class LclUnitSsManifestDAO extends BaseHibernateDAO<LclUnitSsManifest> {

    public LclUnitSsManifestDAO() {
        super(LclUnitSsManifest.class);
    }

    public LclUnitSsManifest getLclUnitSSManifestByHeader(Long unitId, Long headerId) throws Exception {
        Criteria criteria = getSession().createCriteria(LclUnitSsManifest.class, "lclUnitSsManifest");
        criteria.createAlias("lclUnitSsManifest.lclUnit", "lclUnit");
        criteria.createAlias("lclUnitSsManifest.lclSsHeader", "lclSsHeader");
        if (!CommonUtils.isEmpty(unitId)) {
            criteria.add(Restrictions.eq("lclUnit.id", unitId));
        }
        if (!CommonUtils.isEmpty(headerId)) {
            criteria.add(Restrictions.eq("lclSsHeader.id", headerId));
        }
        criteria.addOrder(Order.desc("id"));
        return (LclUnitSsManifest) criteria.setMaxResults(1).uniqueResult();
    }

    public LclUnitSsManifest insertUnitSsManifest(LclUnitSs lclUnitSs, LclUnit lclUnit, LclSsHeader lclssheader, User user, Date now, boolean isEntryManifest) throws Exception {
        LclUnitSsManifest lclunitssmanifest = this.getLclUnitSSManifestByHeader(lclUnit.getId(), lclssheader.getId());

        if (lclunitssmanifest == null) {
            lclunitssmanifest = new LclUnitSsManifest();
            lclunitssmanifest.setEnteredByUser(user);
            lclunitssmanifest.setEnteredDatetime(now);
        }
        if (isEntryManifest) {
            BigDecimal bigDecimal = new BigDecimal(0.00);
            ManifestBean manifestBean = this.entryManifestSSunit(lclUnitSs.getId());
            lclunitssmanifest.setCalculatedBlCount(CommonUtils.isNotEmpty(manifestBean.getBlCount()) ? manifestBean.getBlCount() : 0);
            lclunitssmanifest.setCalculatedDrCount(CommonUtils.isNotEmpty(manifestBean.getDrCount()) ? manifestBean.getDrCount() : 0);
            lclunitssmanifest.setCalculatedTotalPieces(CommonUtils.isNotEmpty(manifestBean.getTotalPieceCount()) ? manifestBean.getTotalPieceCount() : 0);
            lclunitssmanifest.setCalculatedVolumeImperial(CommonUtils.isNotEmpty(manifestBean.getBlCft()) ? manifestBean.getBlCft() : bigDecimal);
            lclunitssmanifest.setCalculatedVolumeMetric(CommonUtils.isNotEmpty(manifestBean.getBlCbm()) ? manifestBean.getBlCbm() : bigDecimal);
            lclunitssmanifest.setCalculatedWeightImperial(CommonUtils.isNotEmpty(manifestBean.getBlLbs()) ? manifestBean.getBlLbs() : bigDecimal);
            lclunitssmanifest.setCalculatedWeightMetric(CommonUtils.isNotEmpty(manifestBean.getBlKgs()) ? manifestBean.getBlKgs() : bigDecimal);
            lclunitssmanifest.setLclUnit(lclUnit);
            lclunitssmanifest.setLclSsHeader(lclssheader);
        }
        lclunitssmanifest.setModifiedByUser(user);
        lclunitssmanifest.setModifiedDatetime(now);
        return lclunitssmanifest;
    }

    public void updateSsHeaderId(Long oldSsHeaderId, Long newSsHeaderId, Long unitId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_unit_ss_manifest set ss_header_id =:newSsHeaderId, ");
        queryStr.append(" modified_datetime=:dateTime,modified_by_user_id=:userId ");
        queryStr.append(" WHERE ss_header_id =:oldSsHeaderId and unit_id=:unitId");
        SQLQuery queryObject = getSession().createSQLQuery(queryStr.toString());
        queryObject.setParameter("newSsHeaderId", newSsHeaderId);
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("dateTime", new Date());
        queryObject.setParameter("oldSsHeaderId", oldSsHeaderId);
        queryObject.setParameter("unitId", unitId);
        queryObject.executeUpdate();
    }

    public void updateUnitId(Long ssHeaderId, Long oldUnitId, Long newUnitId, Integer userId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("UPDATE lcl_unit_ss_manifest set unit_id =:newUnitId, ");
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

    public ManifestBean entryManifestSSunit(Long unitSSId) throws Exception {
        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT SUM(fn.KGS) AS blKgs,SUM(fn.LBS) AS blLbs,SUM(fn.CBM) AS blCbm,SUM(fn.CFT) AS blCft,SUM(fn.pieces) AS totalPieceCount,");
        queryStr.append("SUM(fn.BlCount) AS blCount, SUM(fn.DrCount) AS drCount FROM (SELECT lbp.actual_weight_metric AS KGS,");
        queryStr.append("lbp.actual_weight_imperial AS LBS,lbp.actual_volume_metric AS CBM,lbp.actual_volume_imperial AS CFT,");
        queryStr.append("lbp.actual_piece_count AS pieces,lcf.state ='BL' AS BlCount,lcf.state ='B' AS DrCount FROM ");
        queryStr.append("lcl_file_number lcf LEFT JOIN  lcl_booking_piece lbp ON lbp.file_number_id = lcf.id ");
        queryStr.append("LEFT JOIN lcl_booking_piece_unit lbpu ON lbpu.`booking_piece_id` = lbp.id ");
        queryStr.append("LEFT JOIN `lcl_unit_ss` lus ON lus.id = lbpu.`lcl_unit_ss_id` WHERE lus.id =:unitSSId) fn");
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr.toString());
        query.setLong("unitSSId", unitSSId);
        query.setResultTransformer(Transformers.aliasToBean(ManifestBean.class));
        query.addScalar("blKgs", BigDecimalType.INSTANCE);
        query.addScalar("blLbs", BigDecimalType.INSTANCE);
        query.addScalar("blCbm", BigDecimalType.INSTANCE);
        query.addScalar("blCft", BigDecimalType.INSTANCE);
        query.addScalar("totalPieceCount", IntegerType.INSTANCE);
        query.addScalar("blCount", IntegerType.INSTANCE);
        query.addScalar("drCount", IntegerType.INSTANCE);
        return (ManifestBean) query.uniqueResult();
    }
}
