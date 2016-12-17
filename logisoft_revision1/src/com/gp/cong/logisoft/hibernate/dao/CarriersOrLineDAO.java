package com.gp.cong.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.beans.SearchCarriersBean;
import com.gp.cong.logisoft.domain.CarriersOrLine;
import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.GenericCode;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 * @author Yogesh
 *
 */
public class CarriersOrLineDAO extends BaseHibernateDAO {

    public void save(CarriersOrLine transientInstance, String userName) throws Exception {
        getCurrentSession().save(transientInstance);
    }

    public List findForSSLine2(String sslId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CarriersOrLineTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (sslId != null && !sslId.equals("")) {
            criteria.add(Restrictions.eq("carriercode", sslId));
            criteria.addOrder(Order.asc("carriercode"));
        }
        return criteria.list();
    }

    public void updateToAudit(CarriersOrLine persistanceInstance, String userName) throws Exception {
    }

    public void update(CarriersOrLine persistanceInstance, String userName) throws Exception {
        getSession().save(persistanceInstance);
    }

    public List findLoginName(String carriercode) throws Exception {
        List list = new ArrayList();
        String queryString = "from CarriersOrLine where carriercode=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", carriercode);
        list = queryObject.list();
        return list;
    }

    public void delete(CarriersOrLine persistanceInstance, String userName) throws Exception {
        getSession().delete(persistanceInstance);
    }

    public void delete1(CarriersOrLine persistanceInstance) throws Exception {
        getSession().delete(persistanceInstance);
        getSession().flush();
    }

    public List findForSearchCarriersAction(String carriercode, String carriername, String carriertype, String SCAC) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CarriersOrLineTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (carriercode != null && !carriercode.equals("")) {
            criteria.add(Restrictions.like("carriercode", carriercode + "%"));
        }
        if (carriername != null && !carriername.equals("")) {
            criteria.add(Restrictions.like("carriername", carriername + "%"));
        }
        if (carriertype != null && !carriertype.equals("0")) {
            GenericCodeDAO genericdao = new GenericCodeDAO();
            GenericCode genericobj = genericdao.findById(new Integer(carriertype));
            criteria.add(Restrictions.like("carriertype", genericobj));
        }
        if (SCAC != null && !SCAC.equals("")) {
            criteria.add(Restrictions.like("SCAC", SCAC + "%"));

        }
        criteria.addOrder(Order.asc("carriercode"));
        return criteria.list();

    }

    public List findForSearchCarriersStartAction(String carriercode, String carriername, String carriertype, String SCAC, String match) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CarriersOrLineTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (carriercode != null && !carriercode.equals("")) {
            criteria.add(Restrictions.ge("carriercode", carriercode));
            criteria.addOrder(Order.asc("carriercode"));
        }

        if (carriername != null && !carriername.equals("")) {
            criteria.add(Restrictions.ge("carriername", carriername));
            criteria.addOrder(Order.asc("carriername"));
        }

        if (carriertype != null && !carriertype.equals("0")) {
            GenericCodeDAO genericdao = new GenericCodeDAO();
            GenericCode genericobj = genericdao.findById(new Integer(carriertype));
            criteria.add(Restrictions.eq("carriertype", genericobj));
            criteria.addOrder(Order.asc("carriertype"));
        }
        if (SCAC != null && !SCAC.equals("")) {
            criteria.add(Restrictions.ge("SCAC", SCAC));
            criteria.addOrder(Order.asc("SCAC"));
        }
        return criteria.list();

    }

    public List findCarrierCode(String carriercode) throws Exception {
        List list = new ArrayList();
        String queryString = "from CarriersOrLine where carriercode=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", carriercode);
        list = queryObject.list();
        return list;
    }

    public List findCarrierCode1(String carriercode, String carriername) throws Exception {
        List list = new ArrayList();
        String queryString = "";
        if (carriercode != null && carriercode != "") {
            queryString = "from CarriersOrLineTemp where carriercode  like '" + carriercode + "%'";
        } else {
            queryString = "from CarriersOrLineTemp where carriername  like '" + carriername + "%'";
        }
        Query queryObject = getCurrentSession().createQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public CarriersOrLine findById(String id) throws Exception {
        CarriersOrLine instance = (CarriersOrLine) getCurrentSession().get("com.gp.cong.logisoft.domain.CarriersOrLine", id);
        return instance;
    }

    public List findAllCarriers() throws Exception {
        String queryString = "from CarriersOrLineTemp";
        Query queryObject = getCurrentSession().createQuery(queryString);
        return queryObject.list();
    }

    public List findForSSLine(String sslId, String sslName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CarriersOrLineTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (sslId != null && !sslId.equals("")) {
            criteria.add(Restrictions.like("carriercode", sslId + "%"));
            criteria.addOrder(Order.asc("carriercode"));
        }
        if (sslName != null && !sslName.equals("")) {
            criteria.add(Restrictions.like("carriername", sslName + "%"));
            criteria.addOrder(Order.asc("carriername"));
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findForFclSSLine(String sslId, String sslName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CarriersOrLineTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (sslId != null && !sslId.equals("")) {
            criteria.add(Restrictions.like("SCAC", sslId + "%"));
            criteria.addOrder(Order.asc("SCAC"));
        }
        if (sslName != null && !sslName.equals("")) {
            criteria.add(Restrictions.like("carriername", sslName + "%"));
            criteria.addOrder(Order.asc("carriername"));
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public List findForSSLine1(String sslId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CarriersOrLineTemp.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (sslId != null && !sslId.equals("")) {
            criteria.add(Restrictions.like("carriercode", sslId + "%"));
            criteria.addOrder(Order.asc("carriercode"));
        }
        return criteria.list();
    }
// this is for carrier Id

    public List getCarrierList(String carid, String carrierName, String carrierType) throws Exception {
        List searchBeanlist = new ArrayList();
        SearchCarriersBean searchBean = null;
        String Query = "";
        if (carid != null && !carid.equals("")) {
            Query = "select ct.carriercode,ct.carriername,ct.carriertype.codedesc,ct.SCAC,ct.ediCarrier from CarriersOrLine ct where ct.carriercode like '" + carid + "%' ";
        } else if (!carrierName.equals("")) {

            Query = "select ct.carriercode,ct.carriername,ct.carriertype.codedesc,ct.SCAC,ct.ediCarrier from CarriersOrLine ct where ct.carriername like '" + carrierName + "%' ";
        } else if (!carrierType.equals("")) {

            Query = "select ct.carriercode,ct.carriername,ct.carriertype.codedesc,ct.SCAC,ct.ediCarrier from CarriersOrLine ct where ct.carriertype = '" + carrierType + "' ";
        } /*if((carid!=null && !carid.equals(""))&&(!carrierName.equals(""))&&(!carrierType.equals("")))
        {
        Query ="select ct.carriercode,ct.carriername,ct.carriertype.codedesc,ct.SCAC,ct.ediCarrier from CarriersOrLine ct where ct.carriertype = '"+carrierType+"'and ct.carriername like '"+carrierName+"%'and ct.carriercode = '"+carid+"'";
        }*/ else if (carid != null && carrierName != null && carrierType.equals("")) {

            Query = "select ct.carriercode,ct.carriername,ct.carriertype.codedesc,ct.SCAC,ct.ediCarrier from CarriersOrLine ct where ct.carriername like '" + carrierName + "%' and ct.carriercode = '" + carid + "' ";
        }

        List QueryObject = getCurrentSession().createQuery(Query).list();

        Iterator itr = QueryObject.iterator();
        int j = 0;
        while (itr.hasNext()) {
            searchBean = new SearchCarriersBean();
            Object[] row = (Object[]) itr.next();
            String carriercode = (String) row[0];
            String carriername = (String) row[1];
            String carriertype = (String) row[2];
            String SCAC = (String) row[3];
            String ediCarrier = (String) row[4];
            if (SCAC == null) {
                SCAC = "";
            }
            if (ediCarrier == null) {
                ediCarrier = "";
            }
            searchBean.setCarriercode(carriercode);
            searchBean.setCarriername(carriername);
            searchBean.setCarriertype(carriertype);
            searchBean.setSCAC(SCAC);
            //searchBean.setEdiCarriers(ediCarrier);
            searchBeanlist.add(searchBean);
            searchBean = null;
        }
        return searchBeanlist;
    }
    // for carriernaem crieteria

    public String getCarrierName(String ssline) throws Exception {
        String result = null;
        String queryString = "select v.carriername from CarriersOrLine v  where v.carriercode='" + ssline.trim() + "'";
        Iterator itr = getCurrentSession().createQuery(queryString).list().iterator();
        result = (String) itr.next();
        return result;
    }

    public List getCarrierCode(String ssline) throws Exception {
        List result = new ArrayList();
        String queryString = "from CarriersOrLineTemp v  where v.carriername like'" + ssline.trim() + "%'";
        result = getCurrentSession().createQuery(queryString).list();
        return result;
    }

    public CarriersOrLineTemp findById1(String id) throws Exception {
        CarriersOrLineTemp instance = (CarriersOrLineTemp) getCurrentSession().get("com.gp.cong.logisoft.domain.CarriersOrLineTemp", id);
        return instance;
    }

    public List findByCarrierCode(String carrierCode) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CarriersOrLine.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (null != carrierCode && !carrierCode.trim().equals("")) {
            criteria.add(Restrictions.like("carriercode", carrierCode + "%"));
            criteria.addOrder(Order.asc("carriercode"));
        }
        criteria.setMaxResults(50);
        return criteria.list();
    }

    public CarriersOrLine getScacAndContractNumber(String accountNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select scac as SCAC,");
        queryBuilder.append("fcl_contract_number as fclContactNumber");
        queryBuilder.append(" from carriers_or_line");
        queryBuilder.append(" where carrier_code = (select ssline_number");
        queryBuilder.append(" from trading_partner");
        queryBuilder.append(" where acct_no = '").append(accountNumber).append("')");
        queryBuilder.append(" limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(CarriersOrLine.class));
        return (CarriersOrLine) query.uniqueResult();
    }

    public String getCarrierSCAC(String ssline) throws Exception {
        String result = null;
        String queryString = "SELECT v.SCAC FROM CarriersOrLine v WHERE v.carriercode='" + ssline.trim() + "'";
        Iterator itr = getCurrentSession().createQuery(queryString).list().iterator();
        result = (String) itr.next();
        return result;
    }

    public String getCarrierScacCode(String codeDesc, String codeTypeDesc) throws Exception {
        String queryStr = "SELECT scac as scacCode from carriers_or_line WHERE carrier_code=:ssLineNo ";
        SQLQuery query = getCurrentSession().createSQLQuery(queryStr);
        query.setString("ssLineNo", codeDesc);
        query.addScalar("scacCode", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }
}
