package com.gp.cong.logisoft.hibernate.dao;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import java.util.ArrayList;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.User;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.type.BooleanType;

public class CustomerAccountingDAO extends BaseHibernateDAO {

    public void save(CustomerAccounting transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void update(CustomerAccounting transientInstance) throws Exception {
        getSession().update(transientInstance);
        getSession().flush();

    }

    public CustomerAccounting findByProperty(String property, Object value) throws Exception {
        Criteria criteria = getSession().createCriteria(CustomerAccounting.class);
        criteria.add(Restrictions.eq(property, value));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (CustomerAccounting) criteria.uniqueResult();
    }

    public void auditsave(CustomerAccounting transientInstance, String userName) throws Exception {
        getSession().save(transientInstance);
    }

    public List arListForCreditHold(int loginuserId) throws Exception {
        List list = new ArrayList();
        String queryString = "from CustomerAccounting  where arcode.userId=?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", loginuserId);
        list = queryObject.list();
        return list;
    }

    public double getCreditLimitForCustomer(String accountNumber) throws Exception {
        String queryString = "Select ca.credit_limit from cust_accounting ca where ca.acct_no='" + accountNumber + "'";
        Object creditLimit = getCurrentSession().createSQLQuery(queryString).uniqueResult();
        return null != creditLimit ? (Double) creditLimit : new Double(0.0d);
    }

    public String getEmailIdsOfAccountingUsersForArCreditHold(String accountNumber) throws Exception {
        String queryString = "Select u.email from User u,CustomerAccounting ca where ca.arcode.id=u.userId and ca.accountNo='" + accountNumber + "'";
        Object emailid = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult().toString();
        return null != emailid ? emailid.toString() : "";
    }

    public CustomerAccounting findByAccountNumber(String accountNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(CustomerAccounting.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (accountNumber != null && !accountNumber.equals("")) {
            criteria.add(Restrictions.eq("accountNo", accountNumber));
        }
        criteria.setMaxResults(1);
        return (CustomerAccounting) criteria.uniqueResult();
    }

    public List getCreditDetails(String acctNumber) throws Exception {
        List result = new ArrayList();
        String queryString = "select distinct ve.c_limit,ge.codedesc from vendor_info ve,genericcode_dup ge where ge.Codetypeid = '29' and ge.id=ve.credit_terms and ve.cust_accno = '" + acctNumber + "'";
        List queryObject = new ArrayList();
        queryObject = getCurrentSession().createSQLQuery(queryString).list();
        for (Iterator iterator = queryObject.iterator(); iterator.hasNext();) {
            Object[] row = (Object[]) iterator.next();

            if (row[0] != null) {
                result.add(row[0]);
            } else {
                result.add("");
            }
            if (row[1] != null) {
                result.add(row[1]);
            } else {
                result.add("");
            }
        }
        return result;

    }

    public List getCreditDetailsByCreditTerms(String acctNumber, Integer creditTerms) throws Exception {
        List result = new ArrayList();
        String queryString = "select distinct ve.c_limit,ge.codedesc from vendor_info ve,genericcode_dup ge where ge.Codetypeid = '29' and ge.id=ve.credit_terms and ve.cust_accno = '" + acctNumber + "' and ge.id=" + creditTerms;
        List queryObject = new ArrayList();
        queryObject = getCurrentSession().createSQLQuery(queryString).list();
        for (Iterator iterator = queryObject.iterator(); iterator.hasNext();) {
            Object[] row = (Object[]) iterator.next();
            if (row[0] != null) {
                result.add(row[0]);
            } else {
                result.add("");
            }
            if (row[1] != null) {
                result.add(row[1]);
            } else {
                result.add("");
            }
        }
        return result;
    }

    public String getCreditStatus(String accountNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(gen.codedesc!='',gen.codedesc,'No Credit') as credit_status");
        queryBuilder.append(" from cust_accounting ca");
        queryBuilder.append(" left join genericcode_dup gen on ca.credit_status=gen.id");
        queryBuilder.append(" where ca.acct_no='").append(accountNumber).append("'");
        queryBuilder.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getAutosCredit(String accountNumber) {
        String queryString = "select if(hhg_pe_autoscredit = 'Y','Yes','No') as hhgPeAutosCredit from cust_accounting where acct_no='" + accountNumber + "' limit 1";
        return (String) getCurrentSession().createSQLQuery(queryString.toString()).uniqueResult();

    }

    public boolean isMailFax(String accountNumber) {
        Object[] instance = getMailFax(accountNumber);
        if (instance != null && instance.length > 0) {
            if (instance[0] != null && !instance[0].toString().trim().equals("")) {
                return true;
            }
            if (instance[1] != null && !instance[1].toString().trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    public Object[] getMailFax(String accountNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ar_fax,acct_rec_email FROM cust_accounting WHERE acct_no = '");
        queryBuilder.append(accountNumber);
        queryBuilder.append("'");
        return (Object[]) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Object[] getMailFaxContact(String accountNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ar_fax,acct_rec_email,contact FROM cust_accounting WHERE acct_no = '");
        queryBuilder.append(accountNumber);
        queryBuilder.append("'");
        return (Object[]) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public boolean isCreditDebitNote(String accountNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(send_debit_credit_notes = 'Y', true, false) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  cust_accounting ");
        queryBuilder.append("where");
        queryBuilder.append("  acct_no = '").append(accountNumber).append("' ");
        queryBuilder.append("limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return null != query.uniqueResult() ? (Boolean) query.uniqueResult() : false;
    }

    public Object getStringFieldByAcctno(String acctNo, String fieldName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append(fieldName);
        queryBuilder.append(" from cust_accounting where acct_no = '");
        queryBuilder.append(acctNo);
        queryBuilder.append("'");
        return (Object) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public Boolean isNoCreditAccount(String acctNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select `CustomerGetIsNoCredit` (:acctNo) as noCredit");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("acctNo", acctNo);
        query.addScalar("noCredit", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public void updateCreditStaus(String acctNo, User loginUser) {
        StringBuilder query = new StringBuilder();
        query.append("update cust_accounting set credit_status =");
        query.append("(SELECT id FROM `genericcode_dup` WHERE codedesc='In Good Standing'),");
        query.append("credit_status_update_by =:loginUser, exempt_credit_process ='Y', credit_limit =1000000.00,");
        query.append("credit_rate =(SELECT id FROM `genericcode_dup` WHERE codedesc = 'Net 30 Days')");
        query.append(" where acct_no =:acctNo");
        SQLQuery queryObject = getSession().createSQLQuery(query.toString());
        queryObject.setParameter("loginUser", loginUser);
        queryObject.setParameter("acctNo", acctNo);
        queryObject.executeUpdate();
    }
}
