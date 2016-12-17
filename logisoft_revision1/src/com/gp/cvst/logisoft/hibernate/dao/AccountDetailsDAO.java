package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.ChartOfAccountBean;
import com.gp.cvst.logisoft.domain.AccountDetails;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class AccountDetails.
 * @see com.gp.cvst.logisoft.hibernate.dao.AccountDetails
 * @author MyEclipse - Hibernate Tools
 */
public class AccountDetailsDAO extends BaseHibernateDAO {

    public void save(AccountDetails transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

  public void delete(AccountDetails persistentInstance)throws Exception {
    getSession().delete(persistentInstance);
    getSession().flush();
  }

  public AccountDetails findById(java.lang.String id)throws Exception {
    AccountDetails instance = (AccountDetails) getSession().get("com.gp.cvst.logisoft.domain.AccountDetails", id);
      return instance;
  }

  public void update(AccountDetails persistanceInstance)throws Exception {
     //getSession().update(persistanceInstance);
  }

  public List findByExample(AccountDetails instance)throws Exception {
      List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.AccountDetails").add(Example.create(instance)).list();
      return results;
  }

  public List findByProperty(String propertyName, Object value)throws Exception {
      String queryString = "from AccountDetails as model where model." + propertyName + " like ?0";
      Query queryObject = getSession().createQuery(queryString);
      queryObject.setParameter("0", value);
      return queryObject.list();
  }

  public AccountDetails merge(AccountDetails detachedInstance)throws Exception {
      AccountDetails result = (AccountDetails) getSession().merge(detachedInstance);
      return result;
  }

  public void attachDirty(AccountDetails instance)throws Exception {
      getSession().saveOrUpdate(instance);
  }

  public void attachClean(AccountDetails instance)throws Exception {
      getSession().buildLockRequest(LockOptions.NONE).lock(instance);
  }

  public Iterator getAllAcctDetailsForDisplay()throws Exception {
    Iterator results = null;
    results = getCurrentSession().createQuery(
              "select acctsdetails.account from AccountDetails acctsdetails ").list().iterator();
    return results;
  }

  public Iterator getAllAcctGroupsForDisplay()throws Exception {
    Iterator results = null;
      results = getCurrentSession().createQuery(
              "select  acctsdetails.acctGroup from AccountDetails acctsdetails").list().iterator();
    return results;
  }

  public List findForShowAll()throws Exception {
      ChartOfAccountBean chartOfAccountBean = null;
      List<ChartOfAccountBean> chartOfAccountList = new ArrayList<ChartOfAccountBean>();
      String queryString = "select acctsdetails.Account,acctsdetails.Acct_Desc,acctsdetails.Acct_Status,acctsdetails.Normal_Balance,"
              + " acctsdetails.Multi_Currency,acctsdetails.Acct_Group,acctsdetails.Acct_Type,acctsdetails.Control_Acct,accountGroup.Group_Desc  "
              + " from account_details acctsdetails  left join account_group accountGroup on accountGroup.Acct_Group=acctsdetails.Acct_Group";
      Query query = getCurrentSession().createSQLQuery(queryString);
      List resultList = query.list();
      for (Object object : resultList) {
        chartOfAccountBean = new ChartOfAccountBean();
        Object[] row = (Object[]) object;
        String acct = (String) (row[0]);
        String desc = (String) row[1];
        String strstatus = (String) row[2];
        String strbalance = (String) row[3];
        String strCurrency = (String) row[4];
        String strgroup = (String) row[5];
        String acctType1 = (String) row[6];
        String controlAcct = (String) row[7];
        String groupDescription = (String) row[8];
        if (controlAcct == null || controlAcct.equals("")) {
          controlAcct = "No";
        } else {
          controlAcct = "Yes";
        }
        chartOfAccountBean.setAcct(acct);
        chartOfAccountBean.setDesc(desc);
        chartOfAccountBean.setMulticurrency(strCurrency);
        chartOfAccountBean.setNormalbalance(strbalance);
        chartOfAccountBean.setStatus(strstatus);
        chartOfAccountBean.setGroup(strgroup);
        chartOfAccountBean.setDescription(groupDescription);
        chartOfAccountBean.setAcctType(acctType1);
        chartOfAccountBean.setControlAcct(controlAcct);
        chartOfAccountList.add(chartOfAccountBean);
        chartOfAccountBean = null;
      }

      return chartOfAccountList;
  }

  public List findForSearch(String Acct, String acctStatus, String acctType, String Acct1, String acctGroup)throws Exception {
      if(Acct == null){
          Acct = "";
      }
      if(acctStatus == null){
          acctStatus = "Active";
      }
      if(acctType == null){
          acctType = "";
      }
      if(Acct1 == null){
          Acct1 = "";
      }
      if(acctGroup == null){
          acctGroup = "";
      }
      ChartOfAccountBean chartOfAccountBean = null;
      List<ChartOfAccountBean> chartOfAccountList = new ArrayList<ChartOfAccountBean>();
      String queryString = "";
      if (Acct == null || Acct.equals("")) {
        queryString = "select acctsdetails.Account,acctsdetails.Acct_Desc,acctsdetails.Acct_Status,acctsdetails.Normal_Balance,"
                + " acctsdetails.Multi_Currency,acctsdetails.Acct_Group,acctsdetails.Acct_Type,acctsdetails.Control_Acct,accountGroup.Group_Desc "
                + " from account_details acctsdetails  left join account_group accountGroup on accountGroup.Acct_Group=acctsdetails.Acct_Group"
                + " where  acctsdetails.Acct_Status like '" + acctStatus + "%' and acctsdetails.Acct_Type like '" + acctType + "%' and acctsdetails.Acct_Group like '" + acctGroup + "%'";
      } else if (Acct != null && !Acct.equals("") && acctType.equals("") && acctStatus.equals("Active")) {
        if (Acct1 == null || Acct1.equals("")) {
          queryString = "select acctsdetails.Account,acctsdetails.Acct_Desc,acctsdetails.Acct_Status,acctsdetails.Normal_Balance,"
                  + " acctsdetails.Multi_Currency,acctsdetails.Acct_Group,acctsdetails.Acct_Type,acctsdetails.Control_Acct,accountGroup.Group_Desc  "
                  + " from account_details acctsdetails left join account_group accountGroup on accountGroup.Acct_Group=acctsdetails.Acct_Group"
                  + " where acctsdetails.Account like '%" + Acct + "%'";
        } else {
          queryString = "select acctsdetails.Account,acctsdetails.Acct_Desc,acctsdetails.Acct_Status,acctsdetails.Normal_Balance,"
                  + " acctsdetails.Multi_Currency,acctsdetails.Acct_Group,acctsdetails.Acct_Type,acctsdetails.Control_Acct,accountGroup.Group_Desc  "
                  + " from account_details acctsdetails  left join account_group accountGroup on accountGroup.Acct_Group=acctsdetails.Acct_Group"
                  + " where acctsdetails.Account Between (select Account from account_details where Account like '%" + Acct + "%')  "
                  + " and (select Account from account_details where Account like '%" + Acct1 + "%')";
        }
      } else {
        if (Acct1 == null || Acct1.equals("") && !Acct.equals("")) {
          queryString = "select acctsdetails.Account,acctsdetails.Acct_Desc,acctsdetails.Acct_Status,acctsdetails.Normal_Balance,"
                  + " acctsdetails.Multi_Currency,acctsdetails.Acct_Group,acctsdetails.Acct_Type,acctsdetails.Control_Acct,accountGroup.Group_Desc  "
                  + " from account_details acctsdetails  left join account_group accountGroup on accountGroup.Acct_Group=acctsdetails.Acct_Group"
                  + " where acctsdetails.Account like '%" + Acct + "%' and acctsdetails.Acct_Status like '" + acctStatus + "%' and acctsdetails.Acct_Type like '" + acctType + "%' and acctsdetails.Acct_Group like '" + acctGroup + "%'";
        } else {
          queryString = "select acctsdetails.Account,acctsdetails.Acct_Desc,acctsdetails.Acct_Status,acctsdetails.Normal_Balance,"
                  + " acctsdetails.Multi_Currency,acctsdetails.Acct_Group,acctsdetails.Acct_Type,acctsdetails.Control_Acct,accountGroup.Group_Desc  "
                  + " from account_details acctsdetails  left join account_group accountGroup on accountGroup.Acct_Group=acctsdetails.Acct_Group"
                  + " where acctsdetails.Account Between (select Account from account_details where Account like '%" + Acct + "%')   and (select Account from account_details where Account like '%" + Acct1 + "%') and acctsdetails.Acct_Status like '" + acctStatus + "%' and acctsdetails.Acct_Type like '" + acctType + "%' and acctsdetails.Acct_Group like '" + acctGroup + "%'";
        }
      }
      List queryObject = getCurrentSession().createSQLQuery(queryString).list();
      Iterator iter = queryObject.iterator();
      while (iter.hasNext()) {
        chartOfAccountBean = new ChartOfAccountBean();
        Object[] row = (Object[]) iter.next();
        String acct = (String) (row[0]);
        String desc = (String) row[1];
        String strstatus = (String) row[2];
        String strbalance = (String) row[3];
        String strCurrency = (String) row[4];
        String strgroup = (String) row[5];
        String acctType1 = (String) row[6];
        String controlAcct = (String) row[7];
        String groupDescription = (String) row[8];
        if (controlAcct == null || controlAcct.equals("")) {
          controlAcct = "No";
        } else {
          controlAcct = "Yes";
        }
        chartOfAccountBean.setAcct(acct);
        chartOfAccountBean.setDesc(desc);
        chartOfAccountBean.setMulticurrency(strCurrency);
        chartOfAccountBean.setNormalbalance(strbalance);
        chartOfAccountBean.setStatus(strstatus);
        chartOfAccountBean.setGroup(strgroup);
        chartOfAccountBean.setDescription(groupDescription);
        chartOfAccountBean.setAcctType(acctType1);
        chartOfAccountBean.setControlAcct(controlAcct);
        chartOfAccountList.add(chartOfAccountBean);
        chartOfAccountBean = null;
      }

      return chartOfAccountList;
  }
//	 by Pradeep
  public Iterator getClosetoAcctList()throws Exception {
    Iterator results = null;
      results = getCurrentSession().createQuery(
              "select acctsdetails.account from AccountDetails acctsdetails where acctsdetails.acctType='Retained Earnings'").list().iterator();
    return results;
  }

  public AccountDetails getClosetoAcctList(String account)throws Exception {
      return (AccountDetails)getCurrentSession().createQuery(
              "from AccountDetails acctsdetails where acctsdetails.account='" + account + "'").setMaxResults(1).uniqueResult();
  }

  public String getDescforAccount(String account)throws Exception {
      return(String)getCurrentSession().
              createQuery("select ad.acctDesc from AccountDetails ad where ad.account='" + account + "'").setMaxResults(1).uniqueResult();
  }

  public List<AccountDetails> findAccountNo(String account, String acctDesc)throws Exception {
      String queryString = " from AccountDetails where account like ?0 or acctDesc like ?1";
      Query query = getCurrentSession().createQuery(queryString);
      query.setParameter("0", "%" + account + "%");
      query.setParameter("1", "%" + account + "%");
      query.setMaxResults(50);
      return query.list();
  }

  public String findAccountNo1(String account)throws Exception {
    String list = "";
    String output = "";
    account = account.replaceAll("-", "");

    if (account.length() < 3) {
      output = account;
    } else if (account.length() < 7) {
      output = account.substring(0, 2) + "-" + account.substring(2, account.length());
    } else if (account.length() < 9) {
      output = account.substring(0, 2) + "-" + account.substring(2, 6) + "-" + account.substring(6, account.length());
    }
      String queryString = "select account from AccountDetails where account=?0";
      Query queryObject = getCurrentSession().createQuery(queryString);
      queryObject.setParameter("0", output);
      list = queryObject.list().toString();

      if (queryObject.list().size() == 0) {
        list = account;
      }
    return list;
  }

  public List findAccoutnNo1(String account)throws Exception {
      Criteria criteria = getCurrentSession().createCriteria(AccountDetails.class);
      criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
      if (account != null && !account.equals("")) {
        criteria.add(Restrictions.eq("account", account));
      }
      return criteria.list();
  }

  public List findAccoutNo(String account)throws Exception {
    List list = new ArrayList();
      String queryString = "from AccountDetails where account=?0";
      Query queryObject = getCurrentSession().createQuery(queryString);
      queryObject.setParameter("0", account);
      list = queryObject.list();

    return list;
  }

  public Integer getAccountDetailsCountForAcctType() throws Exception {
      String queryString = "select count(*) from AccountDetails account where account.acctType='Income Statement' and account.closeToAcct is null";
      Query queryObject = getCurrentSession().createQuery(queryString);
      Integer count = (Integer)queryObject.uniqueResult();
    return null!=count?count:0;
  }

  public List<AccountDetails> getAllAccounts() throws Exception {
    List<AccountDetails> accountDetailsList = null;
      String queryString = "from AccountDetails";
      accountDetailsList = getCurrentSession().createQuery(queryString).list();
    return accountDetailsList;
  }

  public List<AccountDetails> getAccountsInTheRange(String startingAccount, String endingAccount)throws Exception {
    List<AccountDetails> accountDetailsList = null;
      String queryString = "from AccountDetails where account between '" + startingAccount + "' and '" + endingAccount + "'";
      accountDetailsList = getCurrentSession().createQuery(queryString).list();
    return accountDetailsList;
  }

  public boolean validateAccount(String account)throws Exception {
    String query = "select count(*) from account_details where account='" + account + "'";
    Object result = getCurrentSession().createSQLQuery(query).uniqueResult();
    return null != result && Integer.parseInt(result.toString()) > 0 ? true : false;
  }
}
