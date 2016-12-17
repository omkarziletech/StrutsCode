package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.beans.FclCfclChargeBean;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import java.math.BigInteger;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class FclBl.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.FclBl
 * @author MyEclipse Persistence Tools
 */
public class FclBlContainerDAO extends BaseHibernateDAO {

    public void save(FclBlContainer transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(FclBlContainer persistanceInstance) throws Exception {
        getSession().update(persistanceInstance);
        getSession().flush();
    }

    public void delete(FclBlContainer persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public FclBlContainer findById(java.lang.Integer id) throws Exception {
        FclBlContainer instance = (FclBlContainer) getSession().get(
                "com.gp.cvst.logisoft.domain.FclBlContainer", id);
        return instance;
    }

    public List findByExample(FclBlContainer instance) throws Exception {
        List results = getSession().createCriteria(
                "com.gp.cvst.logisoft.hibernate.dao.FclBlCharges").add(
                        Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from FclBlContainer as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public FclBlContainer findByUnitNo(String value) throws Exception {
        Iterator results = null;
        FclBlContainer fclBlContainer = null;
        Criteria criteria = getCurrentSession().createCriteria(FclBlContainer.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (value != null && !value.equals("")) {
            criteria.add(Restrictions.like("trailerNo", value + "%"));
        }
        results = criteria.list().iterator();
        while (results.hasNext()) {
            fclBlContainer = (FclBlContainer) results.next();
        }
        return fclBlContainer;
    }

    public boolean checkTrailerNoAvailability(String trailerNo, String bol) throws Exception {
        boolean isAvailable = false;
        String queryString = "from FclBlContainer where trailerNo = ?0 and bolId = ?1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", trailerNo);
        queryObject.setParameter("1", CommonFunctions.isNotNull(bol) ? Integer.parseInt(bol) : 0);
        if (CommonUtils.isNotEmpty(queryObject.list())) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public List findAll() throws Exception {
        String queryString = "from FclBlContainer";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public void attachDirty(FclBlContainer instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(FclBlContainer instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List getAllContainers(String bol) throws Exception {
        String queryString = "from FclBlContainer where bolId='" + bol + "' order by trailerNoId";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List<FclBlMarks> getPakagesDetails(Integer trailerId) throws Exception {
        String queryString = "FROM FclBlMarks WHERE trailerNoId ='" + trailerId + "' order by trailerNoId";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public FclBlMarks getPackageDetails(Integer trailerId) throws Exception {
        String queryString = "FROM FclBlMarks WHERE trailerNoId ='" + trailerId + "' order by trailerNoId";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return (FclBlMarks) queryObject.setMaxResults(1).uniqueResult();
    }

    public void deleteContainerDetails(int trailerNoId) throws Exception {
        String queryString = "delete from fcl_bl_container_dtls where trailer_no_id = " + trailerNoId;
        getCurrentSession().createSQLQuery(queryString).executeUpdate();
        getCurrentSession().flush();
    }

    public List<FclBlMarks> getPkgDetailsPdf(String trailerId) throws Exception {
        trailerId = trailerId.substring(0, trailerId.length() - 1);
        String queryString = "FROM FclBlMarks   WHERE  trailerNoId IN(" + trailerId + ")";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();

    }

    public List<FclCfclChargeBean> getAllUnitsPkgs(BigInteger ssHeaderId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  IF(");
        sb.append("    v.total_piece_count IS NOT NULL,");
        sb.append("    SUM(total_piece_count),");
        sb.append("    0.00");
        sb.append("  ) AS totalPieceCount,");
        sb.append("  IF(");
        sb.append("    v.total_volume_imperial IS NOT NULL,");
        sb.append("    SUM(total_volume_imperial),");
        sb.append("    0.00");
        sb.append("  ) AS totalVolumeImperial,");
        sb.append("  IF(");
        sb.append("    v.total_weight_imperial IS NOT NULL,");
        sb.append("    SUM(v.total_weight_imperial),");
        sb.append("    0.00");
        sb.append("  ) AS totalWeightImperial,");
        sb.append("  v.containerSize AS containerSize,");
        sb.append("  v.fileId AS fileId,");
        sb.append("  (SELECT GROUP_CONCAT(reference) FROM `lcl_3p_ref_no` WHERE `file_number_id` =fileId ) AS ncm,");
        sb.append("  (SELECT GROUP_CONCAT(codes) FROM lcl_booking_hs_code  WHERE file_number_id =fileId) AS hsCode");
        sb.append("  ");
        sb.append(" FROM");
        sb.append("  (SELECT ");
        sb.append("    lu.unit_no,");
        sb.append("    SUM(");
        sb.append("      IF(");
        sb.append("        p.actual_piece_count IS NOT NULL ");
        sb.append("        AND p.actual_piece_count != 0,");
        sb.append("        p.actual_piece_count,");
        sb.append("        p.booked_piece_count");
        sb.append("      )");
        sb.append("    ) AS total_piece_count,");
        sb.append("    SUM(");
        sb.append("      IF(");
        sb.append("        p.actual_weight_imperial IS NOT NULL ");
        sb.append("        AND p.actual_weight_imperial != 0.000,");
        sb.append("        p.actual_weight_imperial,");
        sb.append("        p.booked_weight_imperial");
        sb.append("      )");
        sb.append("    ) AS total_weight_imperial,");
        sb.append("    SUM(");
        sb.append("      IF(");
        sb.append("        p.actual_volume_imperial IS NOT NULL ");
        sb.append("        AND p.actual_volume_imperial != 0.000,");
        sb.append("        p.actual_volume_imperial,");
        sb.append("        p.booked_volume_imperial");
        sb.append("      )");
        sb.append("    ) AS total_volume_imperial,");
        sb.append("    ut.volume_imperial AS volumeImperial,");
        sb.append("    GROUP_CONCAT(ut.`elite_type`, '=', LEFT(ut.short_desc, 2)) AS containerSize,");
        sb.append("    b.file_number_id AS fileId");
        sb.append("  FROM");
        sb.append("    lcl_unit_ss luss ");
        sb.append("    JOIN lcl_unit lu ");
        sb.append("      ON luss.unit_id = lu.id ");
        sb.append("    LEFT JOIN lcl_booking_piece_unit u ");
        sb.append("      ON u.lcl_unit_ss_id = luss.id ");
        sb.append("    LEFT JOIN lcl_booking_piece p ");
        sb.append("      ON u.booking_piece_id = p.id ");
        sb.append("    LEFT JOIN lcl_booking b ");
        sb.append("      ON p.file_number_id = b.file_number_id ");
        sb.append("    LEFT JOIN unit_type ut ");
        sb.append("      ON ut.id = lu.unit_type_id ");
        sb.append("  WHERE luss.ss_header_id = :ssHeaderId ");
        sb.append("  GROUP BY luss.unit_id,");
        sb.append("    b.file_number_id) v ");
        sb.append(" GROUP BY v.unit_no ");
        SQLQuery querObject = getCurrentSession().createSQLQuery(sb.toString());
        querObject.setParameter("ssHeaderId", ssHeaderId);
        querObject.setResultTransformer(Transformers.aliasToBean(FclCfclChargeBean.class));
        querObject.addScalar("totalPieceCount", IntegerType.INSTANCE);
        querObject.addScalar("totalVolumeImperial", BigDecimalType.INSTANCE);
        querObject.addScalar("totalWeightImperial", BigDecimalType.INSTANCE);
        querObject.addScalar("containerSize", StringType.INSTANCE);
        querObject.addScalar("ncm", StringType.INSTANCE);
        querObject.addScalar("hsCode", StringType.INSTANCE);
        querObject.addScalar("fileId", LongType.INSTANCE);
        return querObject.list();
    }

    public void saveMark(FclBlMarks FclBlMarks) throws Exception {
        getSession().save(FclBlMarks);
        getSession().flush();

    }
}
