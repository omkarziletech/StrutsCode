package com.logiware.accounting.dao;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.accounting.domain.EcuAccountMapping;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class EcuAccountMappingDAO extends BaseHibernateDAO {

    /**
     * Save record into the ecu_account_mapping table.
     *
     * @param instance
     * @throws java.lang.Exception
     */
    public void save(EcuAccountMapping instance) throws Exception {
        getCurrentSession().save(instance);
        getCurrentSession().flush();
    }

    /**
     * Update record in the ecu_account_mapping table.
     *
     * @param instance
     * @throws java.lang.Exception
     */
    public void update(EcuAccountMapping instance) throws Exception {
        getCurrentSession().update(instance);
        getCurrentSession().flush();
    }

    /**
     * Delete record from the ecu_account_mapping table.
     *
     * @throws java.lang.Exception
     * @param instance
     */
    public void delete(EcuAccountMapping instance) throws Exception {
        getCurrentSession().delete(instance);
        getCurrentSession().flush();
    }

    /**
     * Find record from the ecu_account_mapping table by id.
     *
     * @param id
     * @return instance of EcuAccountMapping
     * @throws java.lang.Exception
     */
    public EcuAccountMapping findById(Integer id) throws Exception {
        getCurrentSession().flush();
        return (EcuAccountMapping) getCurrentSession().get(EcuAccountMapping.class, id);
    }

    /**
     * Find all records from the ecu_account_mapping table by id.
     *
     * @return all instances of EcuAccountMapping
     * @throws java.lang.Exception
     */
    public List<EcuAccountMapping> findAll() throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(EcuAccountMapping.class);
        return criteria.list();
    }

    public boolean verifyNoGLAccountMapped(String reportCategory) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, 1, 0) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  account_details ");
        queryBuilder.append("where");
        queryBuilder.append("  report_category = ?0");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", reportCategory);
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }
}
