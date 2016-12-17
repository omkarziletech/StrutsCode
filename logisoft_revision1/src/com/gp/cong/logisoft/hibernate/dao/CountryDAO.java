package com.gp.cong.logisoft.hibernate.dao;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.domain.Country;

/**
 * Data access object (DAO) for domain model class Country.
 * @see com.gerber.model.UserAuthorization
 * @author Rahul Gokulnath
 */
public class CountryDAO extends BaseHibernateDAO {

    public static final String COUNTRYCODE = "countrycode";
    public static final String COUNTRYNAME = "countryname";
    public static final String REGIONID = "regionid";

    public void save(Country transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void delete(Country persistentInstance)throws Exception{
            getCurrentSession().delete(persistentInstance);
            getCurrentSession().flush();
    }

    public Country findById(java.lang.Integer id)throws Exception{
            Country instance = (Country) getCurrentSession().get("com.gp.cong.logisoft.domain.Country", id);
            return instance;
    }

    public List findByExample(Country instance)throws Exception{
            List results = getCurrentSession().createCriteria("com.gerber.domain.Country").add(Example.create(instance)).list();
            return results;
    }

    public List getAllCountries() throws Exception{
            String queryString = "from Country";
            Query queryObject = getCurrentSession().createQuery(queryString);
            return queryObject.list();
    }

    public Iterator getAllCountriesForDisplay()throws Exception{
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select country.countrycode,country.countryname from Country country where country.countrycode is not null order by country.countryname ").list().iterator();
        return results;
    }

    public List findByProperty(String propertyName, Object value)throws Exception{
            String queryString = "from Country as model where model." + propertyName + "= ?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByCountryCode(Object countrycode)throws Exception{
        return findByProperty(COUNTRYCODE, countrycode);
    }

    public List findByCountryName(Object countryname)throws Exception{
        return findByProperty(COUNTRYNAME, countryname);
    }

    public List findByRegionid(Object regionid)throws Exception{
        return findByProperty(REGIONID, regionid);
    }

    public Country merge(Country detachedInstance)throws Exception {
            Country result = (Country) getCurrentSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(Country instance)throws Exception{
            getCurrentSession().saveOrUpdate(instance);
            getCurrentSession().flush();
    }

    public void attachClean(Country instance)throws Exception{
            getCurrentSession().lock(instance, LockMode.NONE);
    }

    public Iterator getAllcountriesForDisplay()throws Exception{
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select country.countrycode,country.countryname from Country country order by country.countryname").list().iterator();
        return results;
    }
}
