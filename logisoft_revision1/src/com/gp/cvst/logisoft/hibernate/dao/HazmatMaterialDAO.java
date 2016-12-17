package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.beans.SearchQuotationBean;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import java.util.Collections;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class HazmatMaterial.
 * 
 * @see com.gp.cvst.logisoft.hibernate.dao.HazmatMaterial
 * @author MyEclipse Persistence Tools
 */
public class HazmatMaterialDAO extends BaseHibernateDAO {

    public void save(HazmatMaterial transientInstance) throws Exception {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();
    }

    public void update(HazmatMaterial persistanceInstance) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }

    public void delete(HazmatMaterial persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public HazmatMaterial findById(java.lang.Integer id) throws Exception {
        HazmatMaterial instance = (HazmatMaterial) getSession().get(
                "com.gp.cvst.logisoft.domain.HazmatMaterial", id);
        return instance;
    }

    public List getHazmatByBolId(java.lang.Integer bolid) throws Exception {
        List list = new ArrayList();
        String queryString = "from HazmatMaterial where bolId='" + bolid + "'";
        Query queryObject = getSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public List getHazmatForBlPrint(java.lang.Integer bolid, String screenName) throws Exception {
        List list = new ArrayList();
        String queryString = "from HazmatMaterial where bolId='" + bolid + "' and docTypeCode ='" + screenName + "'";
        Query queryObject = getSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public List findByExample(HazmatMaterial instance) throws Exception {
        List results = getSession().createCriteria(
                "com.gp.cvst.logisoft.hibernate.dao.HazmatMaterial").add(
                Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from HazmatMaterial as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findAll() throws Exception {
        String queryString = "from HazmatMaterial";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findbydoctypeid(String docTypeCode, String quotationNo) throws Exception {
        String queryString = "from HazmatMaterial where docTypeCode=?0 and docTypeId=?1";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", docTypeCode);
        queryObject.setParameter("1", quotationNo);
        return queryObject.list();
    }

    public List findbydoctypeid1(String docTypeCode, Integer quotationNo) throws Exception {
        String queryString = "from HazmatMaterial where docTypeCode='" + docTypeCode + "' and bolId='" + quotationNo + "'";
        Query queryObject = getSession().createQuery(queryString);
        return null != queryObject ? queryObject.list() : Collections.EMPTY_LIST;
    }

    public List findListByDocTypeCode(String docTypeCode) throws Exception {
        String queryString = "from HazmatMaterial where docTypeCode='" + docTypeCode + "'";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public int getCount(String docTypeCode, Integer quotationNo) throws Exception {
        String queryString = "from HazmatMaterial where docTypeCode='" + docTypeCode + "' and bolId='" + quotationNo + "'";
        return getSession().createQuery(queryString).list().size();
    }

    public HazmatMaterial merge(HazmatMaterial detachedInstance) throws Exception {
        HazmatMaterial result = (HazmatMaterial) getSession().merge(
                detachedInstance);
        return result;
    }

    public void attachDirty(HazmatMaterial instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(HazmatMaterial instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
        getSession().flush();
    }

    public List hazmatList(int quotationNo) throws Exception {
        List hazmatList = new ArrayList();
        String query = "from HazmatMaterial g where g.bolId = '" + quotationNo + "'";
        hazmatList = getCurrentSession().createQuery(query).list();
        return hazmatList;
    }

    public List Hazmatlist(int quotationNo) throws Exception {

        SearchQuotationBean objsearchQuotationBean = null;
        List<SearchQuotationBean> hazmatinfo = new ArrayList<SearchQuotationBean>();
        String QueryString = "select h.unNumber,h.propShipingNumber,h.hazardClass,h.emerreprsNum from HazmatMaterial h where h.bolId='" + quotationNo + "'";
        List queryObject = getCurrentSession().createQuery(QueryString).list();
        Iterator iter = queryObject.iterator();
        GenericCodeDAO gcDAO = new GenericCodeDAO();
        GenericCode gc = new GenericCode();
        while (iter.hasNext()) {
            objsearchQuotationBean = new SearchQuotationBean();
            Object[] row = (Object[]) iter.next();
            String unNumber = (String) (row[0]);
            String propShipingNumber = (String) (row[1]);
            String hazardClass = (String) (row[2]);
            if (hazardClass != null && !hazardClass.equals("")) {
                Integer id = Integer.parseInt(hazardClass);
                gc = gcDAO.findById(id);
                if (gc != null) {
                    hazardClass = gc.getCodedesc();
                }
            }

            String emerreprsNum = (String) (row[3]);
            objsearchQuotationBean.setUnNo1(unNumber);
            objsearchQuotationBean.setPropsName1(propShipingNumber);
            objsearchQuotationBean.setHazardClass1(hazardClass);
            objsearchQuotationBean.setEmRespTellNo1(emerreprsNum);
            hazmatinfo.add(objsearchQuotationBean);
            objsearchQuotationBean = null;

        }
        return hazmatinfo;

    }

    public List Hazmatlist1(Integer quotationNo) {
        String QueryString = "from HazmatMaterial h where h.bolId='" + quotationNo + "'";
        List queryObject = getCurrentSession().createQuery(QueryString).list();
        return queryObject;

    }

    public void deleteHazmat(String quoteNo) {
        String query = "delete from hazmat_material  where BolId = '" + Integer.parseInt(quoteNo) + "' and DocType_code = 'Quote'";
        Query queryObj = getCurrentSession().createSQLQuery(query);
        queryObj.executeUpdate();
    }

    public List getQuoteHamMat(Integer quoteNo) {
        String query = "select * from hazmat_material  where BolId = '" + quoteNo + "' and DocType_code = 'Quote'";
        Query queryObj = getCurrentSession().createSQLQuery(query);
        return queryObj.list();
    }

    public List getAssignedHazMat(Integer containerId) throws Exception {
        List reList = null;
        String query = "from HazmatMaterial  where bolId = '" + containerId + "' and flag is NOT NULL";
        reList = getCurrentSession().createQuery(query).list();
        return reList;
    }
}
