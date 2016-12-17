package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.SegmentvalueBean;
import com.gp.cvst.logisoft.domain.SegmentValues;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class SegmentValues.
 * @see com.gp.cvst.logisoft.hibernate.dao.SegmentValues
 * @author MyEclipse - Hibernate Tools
 */
public class SegmentValuesDAO extends BaseHibernateDAO {

    public void saveSegmentValues(SegmentValues transientInstance) throws Exception{
            Session session = getSession();
            session.save(transientInstance);
            session.flush();
    }

    public void delete(SegmentValues persistentInstance) throws Exception{
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public SegmentValues findById(java.lang.Integer id)throws Exception{
            SegmentValues instance = (SegmentValues) getSession().get("com.gp.cvst.logisoft.domain.SegmentValues", id);
            return instance;
    }

    public List findByExample(SegmentValues instance) throws Exception{
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.SegmentValues").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception{
            String queryString = "from SegmentValues as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public SegmentValues merge(SegmentValues detachedInstance) throws Exception{
            SegmentValues result = (SegmentValues) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(SegmentValues instance) throws Exception{
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(SegmentValues instance) throws Exception{
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List countAcctStru(int acctdesc) throws Exception{
        List queryObject = null;
            String queryString = "select count (*) from SegmentValues seg  where seg.segmentCodeId='" + acctdesc + "'";
            queryObject = getCurrentSession().createQuery(queryString).list();
        return queryObject;
    }


    /* public List segListValue(String valueid)
    {
    List result = null;


    try{
    String query = "select segval.segmentValue,segval.segmentValueDesc from SegmentValues segval  where segval.id='"+valueid+"'";
    getCurrentSession().createQuery(query).list().iterator();
    System.out.println("from the segmentvaluesDAO------------>"+result);
    }catch(Exception e){
    System.out.println("from the segmetvaluesDAO-----------" +e);
    }
    return result;
    }

     */
//  this for getting the values for showvalues button
    public List findsegValues(String segmentValue, String segmentvaluedesc)throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(SegmentValues.class);

            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            if (segmentValue != null) {
                criteria.add(Restrictions.like("segmentValue", segmentValue));
            }
            if (segmentvaluedesc != null) {

                criteria.add(Restrictions.like("segmentValueDesc", segmentvaluedesc + "%"));
            }

            return criteria.list();
    }

    public List comparevalue(String segcodeid) throws Exception{
        List list = new ArrayList();
            String queryString = "SELECT seg.segmentValue from SegmentValues seg where seg.segmentCodeId='" + segcodeid + "'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            list = queryObject.list();
        return list;
    }

    public List segListValue(String valueid) throws Exception{
            SegmentvalueBean objSegmentvalueBean = null;
            List<SegmentvalueBean> lstGenericIfo = new ArrayList<SegmentvalueBean>();
            String queryString = "select segval.segmentValue,segval.segmentValueDesc,segval.id from SegmentValues segval  where segval.segmentCodeId='" + valueid + "'";
            List queryObject = getCurrentSession().createQuery(queryString).list();

            Iterator iter = queryObject.iterator();
            while (iter.hasNext()) {
                //objChartCodeBean = new ChartOfAccountBean();
                objSegmentvalueBean = new SegmentvalueBean();
                Object[] row = (Object[]) iter.next();


                /**
                 *id1 is local variable
                 * id is id in GenericCode table
                 * strCode is code in GenericCode table
                 * strDesc is codedesc in GenericCode table
                 * strCodedesc is description in Codetype table
                 */
                String scode = (String) (row[0]);
                String sdesc = (String) row[1];
                Integer id = (Integer) row[2];
                objSegmentvalueBean.setSegmentvalue(scode);
                objSegmentvalueBean.setSegmentdesc(sdesc);
                objSegmentvalueBean.setId(id);
                lstGenericIfo.add(objSegmentvalueBean);
                objSegmentvalueBean = null;

            }

            return lstGenericIfo;
    }

    public List listsegvalues(String valueid) throws Exception{
            SegmentvalueBean objSegmentvalueBean = null;

            List<SegmentvalueBean> lstGenericIfo = new ArrayList<SegmentvalueBean>();

            String queryString = "select segval.segmentValue,segval.segmentValueDesc from SegmentValues segval  where segval.segmentCodeId='" + valueid + "'";
            List queryObject = getCurrentSession().createQuery(queryString).list();

            Iterator iter = queryObject.iterator();
            while (iter.hasNext()) {

                objSegmentvalueBean = new SegmentvalueBean();
                Object[] row = (Object[]) iter.next();

                /**
                 *id1 is local variable
                 * id is id in GenericCode table
                 * strCode is code in GenericCode table
                 * strDesc is codedesc in GenericCode table
                 * strCodedesc is description in Codetype table
                 */
                String scode = (String) (row[0]);
                String sdesc = (String) row[1];

                objSegmentvalueBean.setSegmentvalue(scode);
                objSegmentvalueBean.setSegmentdesc(sdesc);

                lstGenericIfo.add(objSegmentvalueBean);
                objSegmentvalueBean = null;



            }

            return lstGenericIfo;
    }

    public Iterator getAllValuesForSegment(Integer id) throws Exception{
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select sv.segmentValue from SegmentValues sv where sv.segmentCodeId='" + id + "'").list().iterator();
        return results;
    }
}
