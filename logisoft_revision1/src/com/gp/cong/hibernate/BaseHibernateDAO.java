package com.gp.cong.hibernate;

import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.util.ClassUtil;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;
/**
 *
 * @author Sunil
 */
public class BaseHibernateDAO<T extends Domain> extends IBaseHibernateDAO {

    private static final Logger log = Logger.getLogger(BaseHibernateDAO.class);
    private Class<T> clazz;

    public BaseHibernateDAO() {
    }

    /**
     * This contruct is must for Domain class which requires to search result, execute query etc.
     * @param clazz - Domain Class
     */
    public BaseHibernateDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Save new records into the table.
     * @param instance
     */
    public void save(Domain instance)throws Exception{
        getSession().save(instance);
            getSession().flush();
            getSession().clear();
    }
     public void saveLclremarkCts(LclRemarks lclRemarks) throws Exception {
        try {
            getCurrentSession().save(lclRemarks);
            if (getCurrentSession().getTransaction().isActive()) {
                getCurrentSession().getTransaction().commit();
            }
            getCurrentSession().beginTransaction();
        } catch (Exception e) {
            log.info("My save Error:");
            e.printStackTrace();
        }
    }
    /**
     * Save or Update Domain into the table.
     * @param instance - Instance to be updated
     */
    public void saveOrUpdate(Domain instance)throws Exception{
            getCurrentSession().saveOrUpdate(instance);
            getCurrentSession().flush();
            getCurrentSession().clear();
    }

    /**
     * Synchronize the DB and instance Domain.
     * Update should be used to save the data when the session does not contain an
     * already persistent instance with the same identifier.
     * @param instance
     */
    public void update(Domain instance)throws Exception{
            getCurrentSession().update(instance);
            getCurrentSession().flush();
    }

    /**
     * Synchronize the DB and instance Domain.
     * Merge should be used to save the modifications at any time without knowing about the state of a session.
     * @param instance - Domain instance
     */
    public void merge(Domain instance)throws Exception{
            getCurrentSession().merge(instance);
            getCurrentSession().flush();
    }

    /**
     * Remove the instance from the database
     * @param instance - Domain instance
     */
    public void delete(Domain instance)throws Exception{
            getSession().delete(instance);
            getSession().flush();
            getSession().clear();
    }

    /**
     * Remove the instance from the database
     * @param instance - Domain Id
     */
    public void delete(Long id)throws Exception{
            getSession().delete(findById(id));
            getSession().flush();
    }

    /**
     * Flush the session. It will save all unsaved data.
     */
    public void flush()throws Exception{
        getSession().flush();
    }

    /**
     * Get all the records from the table.
     * @return all records for the table
     */
    public List findAll() throws Exception{
            Criteria criteria = getSession().createCriteria(clazz);
            return criteria.list();
    }

    /**
     * It will reattach the Domain of one session to current session.
     * @param domain - Domain
     */
    public void reattach(Domain domain)throws Exception{
        if (!getSession().isDirty()) {
            getSession().buildLockRequest(LockOptions.NONE).lock(domain);
        }
    }


    /**
     * Get the object by property. It works for Unique column only.
     * @param propertyName - Property Name
     * @param value - Value for the property
     * @return Domain instance
     */
    public T getByProperty(String propertyName, Object value)throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(clazz);
            criteria.add(Restrictions.eq(propertyName, value));
            return (T) criteria.setMaxResults(1).uniqueResult();
    }

    /**
     * Get the List of Domain by matching a property of Domain and value.
     * @param propertyName - Property of Domain <T>
     * @param value - value
     * @return - List of Type Domain
     */
    public List findByProperty(String propertyName, Object value)throws Exception{
            Criteria criteria = getCurrentSession().createCriteria(clazz);
            criteria.add(Restrictions.eq(propertyName, value));
            //added by Mahalakshmi
            criteria.addOrder(Order.desc("id"));//end
            return criteria.list();
    }

    /**
     * Find the result from the list of properties.
     * @param Properties   
     * @return Search result
     */
    public List find(Map<String, Object> propertyMap)throws Exception{
        return find(propertyMap, LogicalOperator.AND);
    }

    /**
     * Find the result from the list of properties.
     * @param Properties
     * @return Search result
     */
    public List find(List<Property> Properties)throws Exception{
        return find(Properties, LogicalOperator.AND);
    }

    /**
     * Get Domain object
     * @param Properties - Properties to be matched.
     * @return Domain
     */
    public T get(List<Property> Properties)throws Exception{
        List<T> list = find(Properties);
        if (list == null || list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException("Result returns more than one record");
        }
    }

    /**
     * Find the result
     * @param propertyMap - Map contains the property and corresponding value.     
     * @param lo - Logical Operator AND or OR
     * @return - List of <code>Domain</code>
     */
    public List find(Map<String, Object> propertyMap, LogicalOperator lo)throws Exception{
        return find(propertyMap, lo, null);
    }

    /**
     * Find the result from the list of properties which contains the propery name and value.
     * @param Properties
     * @param lo
     * @return
     */
    public List find(List<Property> Properties, LogicalOperator lo)throws Exception{
        return find(Properties, lo, null);
    }

    /**
     * Find the result
     * @param propertyMap - Map contains the property and corresponding value.     
     * @param lo - Logical Operator AND or OR
     * @param appendAtEnd - Any string needs to be appended at the end of Query.
     * @return - List of <code>Domain</code>
     */
    public List find(Map<String, Object> propertyMap, LogicalOperator lo, String appendAtEnd)throws Exception{
        List<Property> list = new ArrayList<Property>();
        Set<String> keys = propertyMap.keySet();
        for (String key : keys) {
            list.add(Property.getInstance(key, propertyMap.get(key)));
        }
        return find(list, lo, appendAtEnd);
    }

    /**
     * Find the result from the list of properties which contains the propery name and value.
     * @param Properties
     * @param lo - Logical Operator AND or OR
     * @param appendAtEnd any valid syntax used at the end of query like 'ORDER BY ', 'GROUP BY', 'LIMIT 10' etc.
     * @return
     */
    public List find(List<Property> Properties, LogicalOperator lo, String appendAtEnd)throws Exception{
        StringBuilder buffer = new StringBuilder("FROM ");
        buffer.append(clazz.getSimpleName());
        //If the QueryOpertor is LIKE then prepand and append % to property
        buffer.append(generateCondition(Properties, lo));

        if (appendAtEnd != null) {
            buffer.append(appendAtEnd);
        }
        Query queryObject = getSession().createQuery(buffer.toString());
        for (Property property : Properties) {
            String ext = (property.getQueryOperator() == QueryOperator.LIKE) ? "%" : "";
            Object value = property.getValue();
            if (value instanceof Boolean) {
                queryObject.setBoolean(property.getProxy(), (Boolean) value);
            } else if (value instanceof Date) {
                queryObject.setTimestamp(property.getProxy(), (Date) value);
            } else if (value instanceof Integer) {
                queryObject.setInteger(property.getProxy(), (Integer) value);
            } else if (value instanceof Double) {
                queryObject.setDouble(property.getProxy(), (Double) value);
            } else if (value instanceof Long) {
                queryObject.setLong(property.getProxy(), (Long) value);
            } else if (value instanceof Float) {
                queryObject.setFloat(property.getProxy(), (Float) value);
            } else {
                queryObject.setParameter(property.getProxy(), value + ext);
            }
        }
        List list = queryObject.list();
        return (List<T>) list;
    }

    /**
     * Generate the criteria for the query.
     * @param Properties - List contains Property and Value.
     * @param lo - Logical Operator
     * @return condition
     */
    private StringBuilder generateCondition(List<Property> Properties, LogicalOperator lo)throws Exception{
        StringBuilder buffer = new StringBuilder();
        List<Property> removeList = new ArrayList<Property>();
        boolean isFirst = true;
        for (Property property : Properties) {
            Object value = property.getValue();
            String operatorOrWhere = null;
            if (isFirst) {
                operatorOrWhere = " WHERE ";
                isFirst = false;
            } else {
                operatorOrWhere = " " + lo.toString() + " ";
            }
            String stringProperty = property.getName();
            if (value == null) {
                buffer.append(operatorOrWhere).append(stringProperty).append(" is null ");
                removeList.add(property);
            } else {
                // for accepting integer, double and other type of fields.
                buffer.append(operatorOrWhere).append(" ").append(stringProperty).append(" ").append(property.getQueryOperator()).append(" :").append(property.getProxy());
            }
        }
        Properties.removeAll(removeList);
        return buffer;
    }

    /**
     * Find domain by id
     * @param id
     * @return Domain
     */
    public T findById(Serializable id)throws Exception{
        T t = null;
            Object o =  getSession().get(clazz.getName(), id);
            if (o != null) {
                t = (T) o;
            }
            return t;
    }

    /**
     * Find by Instance, it will exclude the zeroes.
     * @param domain - Domain instance
     * @param enableLike - Enable Like operator
     * @param ignoreCase - Case insensitive
     * @return - List of type Domain
     */
    public List<T> findByInstance(T domain, boolean enableLike, boolean ignoreCase)throws Exception{
        Session session = getCurrentSession();
        Criteria criteria = session.createCriteria(domain.getClass());
        Example example = Example.create(domain);
        if (enableLike) {
            example.enableLike();
        }
        if (ignoreCase) {
            example.ignoreCase();
        }
        example.excludeZeroes();
        criteria.add(example);
        return criteria.list();
    }

    /**
     * Find by Instance
     * @param domain - Domain instance
     * @return - List of type Domain
     */
    public List<T> findByInstance(T domain)throws Exception{
        return findByInstance(domain, false, false);
    }

    /**
     * Find by Instance
     * @param domain - Domain instance
     * @param enableLike - Enable Like operator
     * @return - List of type Domain
     */
    public List<T> findByInstance(T domain, boolean enableLike)throws Exception{
        return findByInstance(domain, false, false);
    }

    /**
     * Search by properties of Domain
     * @param properties - List of properties
     * @param instance - get the properties value from.
     * @return - List of Type Domain
     * @throws NoSuchMethodException - If the property mentioned the list <code> properties </code> does not match with
     * the property of instance
     * @throws IllegalAccessException - if the property of instance is private.
     * @throws IllegalArgumentException - if the argument type doesn't not match.
     * @throws InvocationTargetException
     */
    public List<T> find(List<String> properties, T instance) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception {
        Map<String, Object> propertyMap = ClassUtil.getPropertyValueMap(properties, instance);
        return find(propertyMap);
    }

    /**
     * Execute the hibernate query which returns List of type T
     * @param queryString - Query to be executed
     * @return - List of Domain
     */
    public List<T> executeQuery(String queryString)throws Exception{
            Query query = getCurrentSession().createQuery(queryString);
            return query.list();
    }
    public List<T> executeQuery(String queryString,int limit)throws Exception{
            Query query = getCurrentSession().createQuery(queryString);
            query.setMaxResults(limit);
            return query.list();
    }

    public void executeUpdate(String queryString)throws Exception{
            Query query = getCurrentSession().createQuery(queryString);
            query.executeUpdate();
    }
    public void executeUpdateSQL(String queryString)throws Exception{
            Query query = getCurrentSession().createSQLQuery(queryString);
            query.executeUpdate();
    }

    /**
     * Execute the hibernate query which returns List of type T
     * @param queryString - Query to be executed
     * @return - List of Domain
     */
    public List executeSQLQuery(String queryString)throws Exception{
            Query query = getCurrentSession().createSQLQuery(queryString);
            return query.list();
    }

    /**
     * Execute the hibernate query which returns single instance of type T
     * @param queryString - Query to be executed.
     * @return
     */
    public T executeUniqueQuery(String queryString)throws Exception{
            Query query = getCurrentSession().createQuery(queryString);
            query.setMaxResults(1);
            Object o = query.uniqueResult();
            if (o != null) {
                return (T) o;
            } else {
                return null;
            }
    }

    /**
     * Set the Domain Class
     * @param clazz - Domain Class
     */
    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Get the maximum id from the table
     * @return - Maximum Id
     */
    public Long getMaximumId()throws Exception{
            Object o = getCurrentSession().createQuery("SELECT MAX(id) FROM " + clazz.getName());
            return Long.parseLong(o.toString());
    }
    
    public List findByPropertyValues(String propertyName, Object value) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(clazz);
        criteria.add(Restrictions.eq(propertyName, value));
        return criteria.list();
    }
}
