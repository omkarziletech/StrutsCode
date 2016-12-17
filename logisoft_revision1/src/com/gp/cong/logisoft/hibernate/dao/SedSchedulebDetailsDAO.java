package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.Domain;
import com.gp.cong.hibernate.BaseHibernateDAO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.domain.SedSchedulebDetails;

/**
 * A data access object (DAO) providing persistence and search support for
 * SedSchedulebDetails entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.gp.cong.logisoft.domain.SedSchedulebDetails
 * @author MyEclipse Persistence Tools
 */
public class SedSchedulebDetailsDAO extends BaseHibernateDAO {
    // property constants

    public static final String ENTNAM = "entnam";
    public static final String DOMESTIC_OR_FOREIGN = "domesticOrForeign";
    public static final String SCHEDULE_BNUMBER = "scheduleBNumber";
    public static final String SCHEDULE_BNAME = "scheduleBName";
    public static final String DESCRIPTION1 = "description1";
    public static final String DESCRIPTION2 = "description2";
    public static final String QUANTITIES1 = "quantities1";
    public static final String QUANTITIES2 = "quantities2";
    public static final String UNITS1 = "units1";
    public static final String UNITS2 = "units2";
    public static final String WEIGHT = "weight";
    public static final String WEIGHT_TYPE = "weightType";
    public static final String VALUE = "value";
    public static final String EXPORT_INFORMATION_CODE = "exportInformationCode";
    public static final String LICENSE_TYPE = "licenseType";
    public static final String USED_VEHICLE = "usedVehicle";
    public static final String EXPORT_LICENSE = "exportLicense";
    public static final String ECCN = "eccn";
    public static final String VEHICLE_ID_TYPE = "vehicleIdType";
    public static final String VEHICLE_ID_NUMBER = "vehicleIdNumber";
    public static final String VEHICLE_TITLE_NUMBER = "vehicleTitleNumber";
    public static final String VEHICLE_STATE = "vehicleState";

    public void save(SedSchedulebDetails transientInstance) throws Exception {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();
    }

    public void delete(SedSchedulebDetails persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public void update(SedSchedulebDetails persistanceInstance) throws Exception {
        getSession().update(persistanceInstance);
        getSession().flush();
    }

    public SedSchedulebDetails findById(java.lang.Integer id) throws Exception {
        SedSchedulebDetails instance = (SedSchedulebDetails) getSession().get("com.gp.cong.logisoft.domain.SedSchedulebDetails", id);
        return instance;
    }

    public List findByDr(String fileNo, String trnRef) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(SedSchedulebDetails.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (CommonUtils.isNotEmpty(trnRef)) {
            criteria.add(Restrictions.eq("trnref", trnRef));
            criteria.addOrder(Order.asc("trnref"));
            return criteria.list();
        }
        return null;
    }

    public List findByTrnref(String trnref) throws Exception {
        String queryString = "from SedSchedulebDetails where trnref = '" + trnref + "'";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List groupBySchedB(String trnref) throws Exception {
        StringBuilder querybBuilder=new StringBuilder();
        querybBuilder.append("select sum(quantities1),sum(quantities2),sum(weight),sum(value),exportinformationcode,description1,");
        querybBuilder.append("Units1,Units2,LicenseType,ExportLicense,DomesticOrForeign,Eccn,ScheduleB_Number,VehicleIdNumber,");
        querybBuilder.append("VehicleIdType,VehicleTitleNumber,VehicleState,UsedVehicle,total_license_value ");
        querybBuilder.append("from sed_scheduleb_details where transactionreference ='").append(trnref).append("' ");
        querybBuilder.append("group by scheduleb_number");
        return getCurrentSession().createSQLQuery(querybBuilder.toString()).list();
    }

    public Domain saveAndReturn(Domain instance) throws Exception {
        getCurrentSession().saveOrUpdate(instance);
        getCurrentSession().flush();
        getCurrentSession().clear();
        instance = (Domain) new BaseHibernateDAO().findByInstance(instance).get(0);
        return instance;
    }
}
