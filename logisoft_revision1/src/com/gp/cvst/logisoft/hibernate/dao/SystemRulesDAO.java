package com.gp.cvst.logisoft.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.SystemRules;
import com.logiware.accounting.model.CompanyModel;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

/**
 * Data access object (DAO) for domain model class SystemRules.
 * @see com.gp.cvst.logisoft.hibernate.dao.SystemRules
 * @author MyEclipse - Hibernate Tools
 */
public class SystemRulesDAO extends BaseHibernateDAO {

    public void save(SystemRules transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(SystemRules persistentInstance) throws Exception {
	getSession().delete(persistentInstance);
	getSession().flush();
    }

    public SystemRules findById(java.lang.Integer id) throws Exception {
	SystemRules instance = (SystemRules) getSession().get("com.gp.cvst.logisoft.domain.SystemRules", id);
	return instance;
    }

    public List findByExample(SystemRules instance) throws Exception {
	List results = getSession().createCriteria("com.gp.cvst.logisoft.domain.SystemRules").add(Example.create(instance)).list();
	return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
	String queryString = "from SystemRules as model where model." + propertyName + "= ?0";
	Query queryObject = getSession().createQuery(queryString);
	queryObject.setParameter("0", value);
	return queryObject.list();
    }

    public SystemRules merge(SystemRules detachedInstance) throws Exception {
	SystemRules result = (SystemRules) getSession().merge(detachedInstance);
	return result;
    }

    public void attachDirty(SystemRules instance) throws Exception {
	getSession().saveOrUpdate(instance);
	getSession().flush();
    }

    public void attachClean(SystemRules instance) throws Exception {
	getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public String getRuleCode(String rulename) throws Exception {//To get Company code(Rule Code) Based on Rule Name
	String queryString = "Select sr.ruleCode From SystemRules sr where sr.ruleName='" + rulename + "'";
	Object result = (String)getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
	return null!=result?result.toString():"";
    }

    public List getCompanyAddress() throws Exception {
	String queryString = "Select sr.ruleName from SystemRules sr where sr.ruleCode='AddrL1' or sr.ruleCode='City' or sr.ruleCode='State' or sr.ruleCode='Phone' or sr.ruleCode='Email' or sr.ruleCode='CompanyName' or sr.ruleCode='Fax' ";
	return getCurrentSession().createQuery(queryString).list();
    }

    public List getSystemRulesRecords() throws Exception {
	String queryString = "Select sr.ruleName from SystemRules sr";
	return getCurrentSession().createQuery(queryString).list();
    }

    public String getSystemRulesByCode(String ruleCode) throws Exception {
	String queryString = "select sr.ruleName from SystemRules sr where sr.ruleCode = '" + ruleCode + "'";
	return (String) getCurrentSession().createQuery(queryString).uniqueResult();
    }

    public String getSystemRules(String ruleCode) throws Exception {
	String queryString = "select sr.ruleName from SystemRules sr where sr.ruleCode = '" + ruleCode + "'";
	return (String) getCurrentSession().createQuery(queryString).uniqueResult();
    }

    public String getSystemRuleNameByRuleCode(String ruleCode) throws Exception {
	String queryString = "select sr.ruleName from SystemRules sr where sr.ruleCode='" + ruleCode + "'";
	String systemRules = null;
	systemRules = (String) getCurrentSession().createQuery(queryString).uniqueResult();
	return systemRules;
    }

    public List<SystemRules> getAllRuleRecords() throws Exception {
	List<SystemRules> resultList = new ArrayList();
	String queryString = "from SystemRules";
	Query queryObj = (Query) getCurrentSession().createQuery(queryString);
	resultList = queryObj.list();
	return resultList;
    }

    public CompanyModel getCompanyDetails() throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select group_concat(if(rule_code='CompanyName',rule_name,'') separator '') as name,");
	queryBuilder.append("group_concat(if(rule_code='CompanyAddress',rule_name,'') separator '') as address,");
	queryBuilder.append("group_concat(if(rule_code='CompanyPhone',rule_name,'') separator '') as phone,");
	queryBuilder.append("group_concat(if(rule_code='CompanyFax',rule_name,'') separator '') as fax,");
	queryBuilder.append("group_concat(if(rule_code='EFTBank',rule_name,'') separator '') as bankName,");
	queryBuilder.append("group_concat(if(rule_code='EFTAcctName',rule_name,'') separator '') as bankAccountName,");
	queryBuilder.append("group_concat(if(rule_code='EFTAccountNo',rule_name,'') separator '') as bankAccountNumber,");
	queryBuilder.append("group_concat(if(rule_code='EFTABANo',rule_name,'') separator '') as bankAbaNumber,");
	queryBuilder.append("group_concat(if(rule_code='EFTSwiftCode',rule_name,'') separator '') as bankSwiftCode,");
	queryBuilder.append("group_concat(if(rule_code='EFTBankAddress',rule_name,'') separator '') as bankAddress,");
	queryBuilder.append("group_concat(if(rule_code='CreditCardWeb',rule_name,'') separator '') as bankCreditCard,");
	queryBuilder.append("group_concat(if(rule_code='CompanyWebsite',rule_name,'') separator '') as webSite");
	queryBuilder.append(" from system_rules");
	queryBuilder.append(" where rule_code='CompanyName'");
	queryBuilder.append(" or rule_code='CompanyAddress'");
	queryBuilder.append(" or rule_code='CompanyPhone'");
	queryBuilder.append(" or rule_code='CompanyFax'");
	queryBuilder.append(" or rule_code='EFTBank'");
	queryBuilder.append(" or rule_code='EFTAcctName'");
	queryBuilder.append(" or rule_code='EFTAccountNo'");
	queryBuilder.append(" or rule_code='EFTABANo'");
	queryBuilder.append(" or rule_code='EFTSwiftCode'");
	queryBuilder.append(" or rule_code='EFTBankAddress'");
	queryBuilder.append(" or rule_code='CreditCardWeb'");
	queryBuilder.append(" or rule_code='CompanyWebsite'");
	SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
	query.setResultTransformer(Transformers.aliasToBean(CompanyModel.class));
	return (CompanyModel) query.uniqueResult();
    }
}
