package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.Zipcode;
import com.logiware.form.ZipCodeForm;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class ZipCodeDAO extends BaseHibernateDAO {

    public void save(Zipcode transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }
         public Zipcode findById(Integer id) throws Exception {
                Zipcode instance = (Zipcode) getCurrentSession().get("com.gp.cvst.logisoft.domain.Zipcode", id);
                return instance;
        }
         public void delete(Zipcode persistentInstance) throws Exception {
            getSession().delete(persistentInstance);
            getSession().flush();
    }
    public List<Zipcode> findZipCode(String city, String zip) throws Exception {
        List list = new ArrayList();
        boolean flag = false;
        Zipcode zipcode = new Zipcode();
        boolean queryFlag = false;
            String queryString = "select distinct city,zip,state from Zipcode where";
            if("zipOrCity".equals(zip)){
              queryString = "SELECT z.city,z.zip,z.state,CONCAT(z.city,'/',IF(z.state IS NOT NULL,z.state,'')) AS country " +
                      "FROM zip_code z WHERE (zip like '"+city+"%' or city like'"+city+"%' OR CONCAT(z.city,', ',z.state) LIKE '"+city+"%' OR CONCAT(z.city,',',z.state) LIKE '"+city+"%')";
              queryFlag = true;
            }else{
                if (city != null && !city.equals("")) {
                    queryString = queryString + " city like '" + city + "%'";
                    flag = true;
                }

                if (zip != null && !zip.equals("")) {
                    if (flag) {
                        queryString += " and ";
                    }
                    queryString += " zip like '" + zip + "%'";
                }
            }
            Query queryObject = null;
            if(queryFlag){
                queryObject = getCurrentSession().createSQLQuery(queryString);
                queryObject.setMaxResults(200);
            }else{
                queryObject = getCurrentSession().createQuery(queryString);
                queryObject.setMaxResults(100);
            }
            Iterator iter = queryObject.list().iterator();
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                zipcode = new Zipcode();
                String tempcity = (String) row[0];
                String zipCode = (String) row[1];
                String state = (String) row[2];
                if(queryFlag){
                     String unCode = (String) row[3];
                     zipcode.setCountry_code(unCode);
                }
                zipcode.setCity(tempcity);
                zipcode.setZip(zipCode);
                zipcode.setState(state);
                list.add(zipcode);
            }
        return list;
    }

    public List findZipCodeByState(String city, String unlocstate)throws Exception {
        List list = new ArrayList();
        boolean flag = false;
        Zipcode zipcode = new Zipcode();
            String queryString = "select distinct city,zip,state from Zipcode where";
            if (city != null && !city.equals("")) {
                queryString = queryString + " city like '" + city + "%'";
                flag = true;
            }
            if (unlocstate != null && !unlocstate.equals("")) {
                if (flag) {
                    queryString += " and ";
                }
                queryString += " state like '" + unlocstate + "%'";
            }
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setMaxResults(100);
            Iterator iter = queryObject.list().iterator();
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                zipcode = new Zipcode();
                String tempcity = (String) row[0];
                String zipCode = (String) row[1];
                String state = (String) row[2];
                zipcode.setCity(tempcity);
                zipcode.setZip(zipCode);
                zipcode.setState(state);
                list.add(zipcode);
            }
        return list;
    }
    public List<Zipcode> findZipCodeByCity(String city) throws Exception {
        List list = new ArrayList();
        Zipcode zipcode = new Zipcode();
            String queryString = "select distinct city,zip,state from Zipcode where city like '" + city + "%'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setMaxResults(100);
            Iterator iter = queryObject.list().iterator();
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                zipcode = new Zipcode();
                String tempcity = (String) row[0];
                String zipCode = (String) row[1];
                String state = (String) row[2];
                zipcode.setCity(tempcity);
                zipcode.setZip(zipCode);
                zipcode.setState(state);
                list.add(zipcode);
            }
        return list;
    }
    public List<Zipcode> getZipCodeList(ZipCodeForm zipCodeForm) throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Zipcode.class);
            if (CommonUtils.isNotEmpty(zipCodeForm.getZip())) {
                criteria.add(Restrictions.like("zip", zipCodeForm.getZip() + "%"));
            }
            if (CommonUtils.isNotEmpty(zipCodeForm.getCity())) {
                criteria.add(Restrictions.like("city", zipCodeForm.getCity() + "%"));
            }
            if (CommonUtils.isNotEmpty(zipCodeForm.getState())) {
                criteria.add(Restrictions.like("state", zipCodeForm.getState() + "%"));
            }
            return criteria.setMaxResults(100).list();
    }
    
    public Zipcode findByZip(String zip) throws Exception {
                Criteria criteria = getCurrentSession().createCriteria(Zipcode.class);
                criteria.add(Restrictions.eq("zip", zip));
                return (Zipcode)criteria.setMaxResults(1).uniqueResult();
        }
}
