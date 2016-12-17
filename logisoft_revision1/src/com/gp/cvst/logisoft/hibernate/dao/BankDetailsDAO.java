package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.logiware.common.model.OptionModel;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.struts.util.LabelValueBean;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class BankDetails.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.BankDetails
 * @author MyEclipse - Hibernate Tools
 */
public class BankDetailsDAO extends BaseHibernateDAO {

    //property constants
    public static final String BANK_ACCT_NO = "bankAcctNo";
    public static final String BANK_NAME = "bankName";
    public static final String BANK_ADDRESS = "bankAddress";
    public static final String GL_ACCOUNT_NO = "glAccountno";

    public void save(BankDetails transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(BankDetails persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public void update(BankDetails persistentInstance) throws Exception {
        getSession().update(persistentInstance);
        getSession().flush();
    }

    public BankDetails findById(java.lang.Integer id) throws Exception {
        BankDetails instance = (BankDetails) getSession().get("com.gp.cvst.logisoft.domain.BankDetails", id);
        return instance;
    }

    public List findByExample(BankDetails instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.BankDetails").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from BankDetails as model where model." + propertyName + " like ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public List findByBankAcctNo(Object bankAcctNo) throws Exception {
        return findByProperty(BANK_ACCT_NO, bankAcctNo);
    }

    public List findByBankName(Object bankName) throws Exception {
        return findByProperty(BANK_NAME, bankName);
    }

    public List findByBankAddress(Object bankAddress) throws Exception {
        return findByProperty(BANK_ADDRESS, bankAddress);
    }

    public List findByGLAccountNo(Object glAccountNo) throws Exception {
        return findByProperty(GL_ACCOUNT_NO, glAccountNo);
    }

    public BankDetails merge(BankDetails detachedInstance) throws Exception {
        BankDetails result = (BankDetails) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(BankDetails instance) throws Exception {
        getSession().saveOrUpdate(instance);
        getSession().flush();
    }

    public void attachClean(BankDetails instance) throws Exception {
        getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public String getGlAccountNo(String bankAccount) throws Exception {
        String query = "select distinct Gl_Account_No from bank_details where Bank_Acct_No='" + bankAccount.trim() + "'";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public String getGlAccountNoByBankAcctNumberAndBankName(String bankAcctNo, String bankName) throws Exception {
        String QueryString = "";
        QueryString = "select bp.glAccountno from BankDetails bp where bp.bankAcctNo='" + bankAcctNo.trim() + "'"
                + " and bp.bankName='" + bankName.trim() + "'";
        Object result = getCurrentSession().createQuery(QueryString).setMaxResults(1).uniqueResult();
        return null != result ? result.toString() : "";
    }

    public List getAccountNames(String accountName) throws Exception {
        String QueryString = "";
        QueryString = "select * from Bank_Details bp where bp.acct_Name like?0";
        Query queryObject = getCurrentSession().createSQLQuery(QueryString);
        queryObject.setParameter("0", accountName.trim() + "%", StringType.INSTANCE);

        List queryObjectList = queryObject.list();
        return queryObjectList;

    }

    public List getAccountNamesByAccountNumber(String accountNumber) throws Exception {
        String QueryString = "";
        accountNumber = accountNumber.trim();
        QueryString = " from BankDetails bp where bp.acctNo like '" + accountNumber + "%'";
        Query queryObject = getCurrentSession().createQuery(QueryString);
        //queryObject.setParameter(0, accountNumber.trim()+"%'", StringType.INSTANCE);

        List queryObjectList = queryObject.list();
        return queryObjectList;

    }

    public List findByBankAccountNumberAndBankName(String bankName, String bankAcctNo) throws Exception {
        bankAcctNo = null != bankAcctNo ? bankAcctNo.trim() : "";
        bankName = null != bankName ? bankName.trim() : "";
        String queryString = " from BankDetails  where bankName like '" + bankName + "%' and bankAcctNo like '" + bankAcctNo + "%'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        List list = queryObject.list();
        return list;
    }

    public List<BankDetails> getBankDetails(String bankName, String bankAcctNo) throws Exception {
        String queryString = " from BankDetails  where bankName = '" + bankName + "' and bankAcctNo = '" + bankAcctNo + "'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        List<BankDetails> bankDetailslist = queryObject.list();
        return bankDetailslist;
    }

    public List<String> getBankNameList(User loginUser) throws Exception {
        String queryString = "select DISTINCT(bp.bankName) from BankDetails bp where bp.loginName='" + loginUser.getLoginName() + "'";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List<BankDetails> getBankNamesByUserRole(User loginUser, String bankName) throws Exception {
        String queryString = "from BankDetails where loginName = :loginName and bankName like :bankName";
        Query query = getCurrentSession().createQuery(queryString);
        query.setString("loginName", loginUser.getLoginName());
        query.setString("bankName", bankName + "%");
        List<BankDetails> result = query.list();
        return result;
    }

    public List<String> getAllBankName() throws Exception {
        String queryString = "select DISTINCT(bankName) from BankDetails";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List<String> getAccountNumberListForVendor(String vendorName, String bankName) throws Exception {
        String queryString = "select bp.bankAcctNo from BankDetails bp where bp.acctName='" + vendorName + "' and bp.bankName='" + bankName + "'";
        return getCurrentSession().createQuery(queryString).list();
    }

    public List<String> getAccountNumberByBankName(String bankName) throws Exception {
        String queryString = "from BankDetails bp where bp.bankName='" + bankName + "'";
        return getCurrentSession().createQuery(queryString).list();
    }

    public String getNetSettlementGLAccountNumber() throws Exception {
        String glAccountNumber = "";
        if (LoadLogisoftProperties.getProperty("netSettlementGLAccount") != null) {
            glAccountNumber = LoadLogisoftProperties.getProperty("netSettlementGLAccount");
        }
        return glAccountNumber;
    }

    public String checkForValidDepositDate(String depositDate) throws Exception {
        String result = "false";
        DateUtils dateUtil = new DateUtils();
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        FiscalPeriod fiscalPeriod = fiscalPeriodDAO.getFiscalPeriodForDate(dateUtil.parseToDate(depositDate));
        if (fiscalPeriod != null) {
            if (fiscalPeriod.getStatus() != null && fiscalPeriod.getStatus().equalsIgnoreCase(AccountingConstants.GL_FISCALPERIOD_STATUS_OPEN)) {
                result = "true";
            }
        }
        return result;
    }

    public void updateStartingNumber(String bankName, String bankAccountNo, Integer startingNo) throws Exception {
        String queryString = "update BankDetails bp set bp.startingSerialNo='" + startingNo + "' where bp.bankName='" + bankName + "' and bp.bankAcctNo='" + bankAccountNo + "'";
        getSession().createQuery(queryString).executeUpdate();
        getSession().flush();
    }

    public Integer getAchDebitCount(String bankName, String bankAccountNo) {
        String queryString = "select distinct ach_debit_count from bank_details where Bank_Name='" + bankName + "' and bank_acct_no='" + bankAccountNo + "'";
        Integer achDebitCount = 1;
        Object row = getSession().createSQLQuery(queryString).uniqueResult();
        if (null != row) {
            achDebitCount = Integer.parseInt(row.toString());
        }
        return achDebitCount;
    }

    public void updateAchDebitCount(String bankName, String bankAccountNo) {
        String queryString = "update BankDetails  set achDebitCount=achDebitCount+1 where bankName='" + bankName + "' and bankAcctNo='" + bankAccountNo + "'";
        getSession().createQuery(queryString).executeUpdate();
        getSession().flush();
    }

    public Integer getCreditCardCount(String bankName, String bankAccountNo) {
        String queryString = "select distinct credit_card_count from bank_details where Bank_Name='" + bankName + "' and bank_acct_no='" + bankAccountNo + "'";
        Integer creditCardCount = 1;
        Object row = getSession().createSQLQuery(queryString).uniqueResult();
        if (null != row) {
            creditCardCount = Integer.parseInt(row.toString());
        }
        return creditCardCount;
    }

    public void updateCreditCardCount(String bankName, String bankAccountNo) {
        String queryString = "update BankDetails set creditCardCount=creditCardCount+1 where bankName='" + bankName + "' and bankAcctNo='" + bankAccountNo + "'";
        getSession().createQuery(queryString).executeUpdate();
        getSession().flush();
    }

    public String getBankRoutingNumber(String bankAccountNumber) {
        String queryString = "select distinct Bank_Routing_Number from bank_details where bank_acct_no='" + bankAccountNumber + "'";
        String bankRoutingNumber = null;
        Object row = getSession().createSQLQuery(queryString).uniqueResult();
        if (null != row) {
            bankRoutingNumber = row.toString();
        }
        return bankRoutingNumber;
    }

    public Integer getStartingNumber(String bankName, String bankAccountNo) {
        String queryString = "select distinct starting_serial_no from bank_details where Bank_Name='" + bankName + "' and bank_acct_no='" + bankAccountNo + "'";
        Integer startingNumber = 0;
        Object row = getSession().createSQLQuery(queryString).uniqueResult();
        if (null != row) {
            startingNumber = Integer.parseInt(row.toString());
        }
        return startingNumber;
    }

    public Iterator getbankAccountDetails(String bankIds) throws Exception {
        Iterator iterator = null;
        if (null != bankIds && !bankIds.trim().equals("")) {
            iterator = getSession().createQuery(" from BankDetails bd where bd.id in (" + bankIds + ")").list().iterator();
        }
        return iterator;
    }

    public List<LabelValueBean> getBankAccounts(Integer userId) throws Exception {
        List<LabelValueBean> bankAccounts = new ArrayList<LabelValueBean>();
        StringBuilder queryBuilder = new StringBuilder("select bd.bank_name,bd.acct_name,bd.bank_acct_no from bank_user bu,bank_details bd");
        queryBuilder.append(" where bd.id=bu.bank_id and bu.user_id=").append(userId);
        queryBuilder.append(" order by bd.acct_name");
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        bankAccounts.add(new LabelValueBean("Select", ""));
        for (Object row : result) {
            Object[] col = (Object[]) row;
            String bankName = (String) col[0];
            String accountName = (String) col[1];
            String bankAccount = (String) col[2];
            bankAccounts.add(new LabelValueBean(bankName + "-" + accountName, bankAccount));
        }
        return bankAccounts;
    }

    public List<OptionModel> getBankAccounts(String bankName, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  ucase(bd.`acct_name`) as label,");
        queryBuilder.append("  ucase(bd.`bank_acct_no`) as value ");
        queryBuilder.append("from");
        queryBuilder.append("  `bank_details` bd,");
        queryBuilder.append("  `bank_user` bu ");
        queryBuilder.append("where");
        queryBuilder.append("  bd.`bank_name` = :bankName");
        queryBuilder.append("  and bd.`id` = bu.`bank_id`");
        queryBuilder.append("  and bu.`user_id` = :userId ");
        queryBuilder.append("order by bd.acct_name, bd.acct_name");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("bankName", bankName);
        query.setInteger("userId", userId);
        query.addScalar("label", StringType.INSTANCE);
        query.addScalar("value", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(OptionModel.class));
        return query.list();
    }

    public List<String> getBankNames(Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  ucase(bd.`bank_name`) as bankName ");
        queryBuilder.append("from");
        queryBuilder.append("  `bank_user` bu,");
        queryBuilder.append("  `bank_details` bd ");
        queryBuilder.append("where");
        queryBuilder.append("  bu.`user_id` = :userId");
        queryBuilder.append("  and bd.`id` = bu.`bank_id` ");
        queryBuilder.append("group by bd.`bank_name`");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setInteger("userId", userId);
        query.addScalar("bankName", StringType.INSTANCE);
        return query.list();
    }

    public List<BankDetails> getBankAccounts(Integer userId, String bankName) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BankDetails.class);
        criteria.createAlias("users", "ud");
        criteria.add(Restrictions.eq("ud.userId", userId));
        criteria.add(Restrictions.eq("bankName", bankName));
        criteria.addOrder(Order.asc("acctName"));
        return criteria.list();
    }

    public Integer getId(String bankAcctNo, String bankName, String glAccountNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select id from bank_details");
        queryBuilder.append(" where bank_acct_no = '").append(bankAcctNo).append("'");
        queryBuilder.append(" and bank_name = '").append(bankName).append("'");
        queryBuilder.append(" and gl_account_no = '").append(glAccountNo).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result ? Integer.parseInt(result.toString()) : 0;
    }

    public BankDetails getBank(String bankAcctNo, String glAccount) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(BankDetails.class);
        criteria.add(Restrictions.eq("bankAcctNo", bankAcctNo));
        if (CommonUtils.isAllNotEmpty(glAccount)) {
            criteria.add(Restrictions.eq("glAccountno", glAccount));
        }
        criteria.setMaxResults(1);
        return (BankDetails) criteria.uniqueResult();

    }

    public String validatePrinters(String bankName, String bankAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(");
        queryBuilder.append("    bd.`check_printer` is null or trim(bd.`check_printer`) = '',");
        queryBuilder.append("    'No Check Printer is available for this bank account.',");
        queryBuilder.append("    if(");
        queryBuilder.append("      bd.`overflow_printer` is null or trim(bd.`overflow_printer`) = '',");
        queryBuilder.append("      'No Overflow Printer is available for this bank account. Overflow checks cannot be printed.',");
        queryBuilder.append("      'available'");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  `bank_details` bd ");
        queryBuilder.append("where");
        queryBuilder.append("  bd.`bank_name` = :bankName");
        queryBuilder.append("  and bd.`bank_acct_no` = :bankAccount");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("bankName", bankName);
        query.setString("bankAccount", bankAccount);
        query.addScalar("result", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public String getGlAccountNo(String bankName, String bankAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  bd.`gl_account_no` as glAccountNo ");
        queryBuilder.append("from");
        queryBuilder.append("  `bank_details` bd ");
        queryBuilder.append("where");
        queryBuilder.append("  bd.`bank_name` = :bankName");
        queryBuilder.append("  and bd.`bank_acct_no` = :bankAccount");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("bankName", bankName);
        query.setString("bankAccount", bankAccount);
        query.addScalar("glAccountNo", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.uniqueResult();
    }

    public String[] getPrinters(String bankName, String bankAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  bd.`check_printer` as checkPrinter,");
        queryBuilder.append("  bd.`overflow_printer` as overflowPrinter ");
        queryBuilder.append("from");
        queryBuilder.append("  `bank_details` bd ");
        queryBuilder.append("where");
        queryBuilder.append("  bd.`bank_name` = :bankName");
        queryBuilder.append("  and bd.`bank_acct_no` = :bankAccount");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("bankName", bankName);
        query.setString("bankAccount", bankAccount);
        query.addScalar("checkPrinter", StringType.INSTANCE);
        query.addScalar("overflowPrinter", StringType.INSTANCE);
        query.setMaxResults(1);
        Object result = query.uniqueResult();
        Object[] cols = (Object[]) result;
        return Arrays.copyOf(cols, cols.length, String[].class);
    }
}
