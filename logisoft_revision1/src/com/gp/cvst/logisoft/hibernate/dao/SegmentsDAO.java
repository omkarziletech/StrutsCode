package com.gp.cvst.logisoft.hibernate.dao;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.Segments;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class Segments.
 * @see com.gp.cvst.logisoft.domain.Segments
 * @author MyEclipse - Hibernate Tools
 */
public class SegmentsDAO extends BaseHibernateDAO {

    //property constants
    public static final String SEGMENT_TYPE = "segmentType";
    public static final String SEGMENT_VALUE = "segmentValue";

    public void save(Segments transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(Segments persistentInstance)throws Exception{
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public Segments findById(java.lang.Integer id)throws Exception{
            Segments instance = (Segments) getSession().get("com.gp.cvst.logisoft.hibernate.dao.Segments", id);
            return instance;
    }

    public List findByExample(Segments instance)throws Exception{
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.Segments").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception{
            String queryString = "from Segments as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findBySegmentType(Object segmentType) throws Exception{
        return findByProperty(SEGMENT_TYPE, segmentType);
    }

    public List findBySegmentValue(Object segmentValue) throws Exception{
        return findByProperty(SEGMENT_VALUE, segmentValue);
    }

    public Segments merge(Segments detachedInstance)throws Exception{
            Segments result = (Segments) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(Segments instance) throws Exception{
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(Segments instance) throws Exception{
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public Iterator getAllSegment1ForDisplay() throws Exception{
        Iterator results = null;
            results = getCurrentSession().createQuery("select segments.segmentValue from Segments segments where segments.segmentType='seg1'").list().iterator();
        return results;
    }

    public Iterator getAllSegment2ForDisplay() throws Exception{
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select segments.segmentValue from Segments segments where segments.segmentType='seg2'").list().iterator();
        return results;
    }

    public Iterator getAllSegment3ForDisplay() throws Exception{
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select segments.segmentValue from Segments segments where segments.segmentType='seg3'").list().iterator();
        return results;
    }
}
