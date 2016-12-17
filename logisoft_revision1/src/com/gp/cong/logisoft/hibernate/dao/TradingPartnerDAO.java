package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerInfo;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.PaymentMethod;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.accounting.model.TradingPartnerModel;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.tradingpartner.SortByEnumforTP;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;
import com.logiware.utils.AuditNotesUtils;

public class TradingPartnerDAO extends BaseHibernateDAO {

    public List findAccountNumber(String accountNo) throws Exception {
        List list = new ArrayList();
        String queryString = "from TradingPartner where accountName=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", accountNo);
        list = queryObject.list();
        return list;
    }

    public TradingPartner findAccountNumberByPassingAccountName(
            String accountName) throws Exception {
        String queryString = "from TradingPartner where accountName=?0";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", accountName);
        queryObject.setMaxResults(1);
        return (TradingPartner) queryObject.uniqueResult();
    }

    public List getAccoutNumber(String acctno) throws Exception {
        List list = new ArrayList();
        String queryString = "SELECT t.acct_name,t.acct_no,t.acct_type,c.address1,c.city1,c.state,c.zip,t.disabled,t.sub_type,t.ssline_number,c.fax,c.email1,c.email2 ,t.eci_acct_no, t.ecifwno,s.code "
                + "FROM  (SELECT t.acct_no,t.acct_name,t.acct_type,t.disabled,t.sub_type,t.ssline_number, t.eci_acct_no, t.ecifwno FROM trading_partner t  "
                + "WHERE (t.search_acct_name LIKE '" + acctno + "%' OR ((length('" + acctno + "') < 10 and t.acct_no LIKE '" + acctno + "%') or (length('" + acctno + "') = 10 and t.acct_no = '" + acctno + "')) OR t.acct_name LIKE '" + acctno + "%' )  AND (t.acct_type LIKE '%S%' OR (t.acct_type LIKE '%V%' AND t.sub_type='Forwarder')"
                + "OR t.acct_type LIKE '%E%' OR t.acct_type LIKE '%I%')  ORDER BY t.search_acct_name LIMIT 50) AS t LEFT JOIN cust_address c ON t.acct_no = c.acct_no AND c.prime = 'on' "
                + "LEFT JOIN cust_general_info g ON (t.acct_no = g.acct_no) LEFT JOIN genericcode_dup s ON (g.sales_code = s.id)";
        Query queryObject = getCurrentSession().createSQLQuery(queryString);
        list = queryObject.list();
        return list;
    }

    public List getEciAcctNo(String eciAcctNo) throws Exception {
        List list = new ArrayList();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select eci_acct_no from trading_partner ");
        queryBuilder.append(" where eci_acct_no='").append(eciAcctNo).append("'");
        Query queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        list = queryObject.list();
        return list;
    }

    public List getEciConsigneeNo(String eciFwNo) throws Exception {
        List list = new ArrayList();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select ecifwno from trading_partner ");
        queryBuilder.append(" where ecifwno='").append(eciFwNo).append("'");
        Query queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        list = queryObject.list();
        return list;
    }

    public List getEciVendNo(String eciVendNo) throws Exception {
        List list = new ArrayList();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" select ecivendno from trading_partner ");
        queryBuilder.append(" where ecivendno='").append(eciVendNo).append("'");
        Query queryObject = getCurrentSession().createSQLQuery(queryBuilder.toString());
        list = queryObject.list();
        return list;
    }

    public void save(TradingPartner transientInstance) throws Exception {
        getSession().saveOrUpdate(transientInstance);
        getSession().flush();
    }

    public void update(TradingPartner persistanceInstance, String userName) throws Exception {
        getSession().saveOrUpdate(persistanceInstance);
        getSession().flush();
    }

    /**
     * @param accountNo
     * @return
     */
    public TradingPartner findById(String accountNo) throws Exception {
        getCurrentSession().clear();
        getCurrentSession().flush();
        TradingPartner instance = (TradingPartner) getSession().get("com.gp.cong.logisoft.domain.TradingPartner", accountNo);
        return instance;
    }

    public CustomerAddress findAddressById(Integer id) throws Exception {
        getCurrentSession().flush();
        CustomerAddress instance = (CustomerAddress) getSession().get(
                "com.gp.cong.logisoft.domain.CustomerAddress", id);
        return instance;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from TradingPartner as model where model." + propertyName + " like ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public TradingPartnerTemp findById1(String accountNo) throws Exception {
        TradingPartnerTemp instance = (TradingPartnerTemp) getSession().get("com.gp.cong.logisoft.domain.TradingPartnerTemp",
                accountNo);
        return instance;
    }

    /**
     * @param accountNo
     * @return
     */
    public List findAccountPrefix(String accountNumber) throws Exception {
        List list = new ArrayList();
        String queryString = "from TradingPartner where accountno like ?0 order by accountno desc";
        Query queryObject = getCurrentSession().createQuery(queryString);
        queryObject.setParameter("0", accountNumber + "%");
        queryObject.setMaxResults(1);
        list = queryObject.list();
        return list;
    }

    public void delete(TradingPartner tradingPartner) throws Exception {
        getSession().delete(tradingPartner);
        getSession().flush();
    }

    public CustomerAccounting findCustomerAccountingByTradingId(
            Integer tradingId) throws Exception {
        CustomerAccounting customerAccounting = null;
        customerAccounting = (CustomerAccounting) getSession().createCriteria(CustomerAccounting.class).add(
                Restrictions.eq("", tradingId)).setMaxResults(1).uniqueResult();
        return customerAccounting;
    }

    /**
     * @param account
     * @param name
     * @param acctType
     * @param type
     * @return search customer
     */
    public List findCustomer(String account, String name, String acctType,
            String type) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(
                TradingPartner.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        if (account != null && !account.trim().equals("")) {
            criteria.add(Restrictions.like("accountno", account + "%"));
            criteria.addOrder(Order.asc("accountno"));
        }
        if (name != null && !name.trim().equals("")) {

            criteria.add(Restrictions.eq("accountName", name));
            criteria.addOrder(Order.asc("accountName"));
        }

        if (acctType != null && !acctType.trim().equals("")) {
            criteria.add(Restrictions.like("acctType", acctType));
            criteria.addOrder(Order.asc("acctType"));
        }
        if (type != null && !type.trim().equals("")) {
            criteria.add(Restrictions.like("Type", type));
            criteria.addOrder(Order.asc("Type"));
        }
        criteria.setMaxResults(50);
        return criteria.list();

    }

    public List<TradingPartnerInfo> searchCustomer(String account, String name,
            String acctType, String type, String sortBy, String searchAddress,
            String limit, String subType, String blueScreenAccount,TradingPartnerForm tradingPartnerForm) throws Exception {
        if (CommonUtils.isNotEmpty(name)) {
            name = name.replaceAll("[^a-zA-Z0-9]+", ""); //--Replace double quotes String with escape String--//
            limit= "1000";
        }
        if (CommonUtils.isNotEmpty(account)) {
            account = account.replace("\"", "\\\"").replace(":", "").replace("'", "''").replace("\"", "");
        }
        if (CommonUtils.isNotEmpty(blueScreenAccount)) {
            blueScreenAccount = blueScreenAccount.replace("\"", "\\\"").replace(":", "").replace("'", "''").replace("\"", "");
        }
        if (CommonUtils.isNotEmpty(searchAddress)) {
            searchAddress = searchAddress.replace("\"", "\\\"").replace(":", "").replace("'", "''");
        }
        StringBuilder queryBuilder = new StringBuilder();
        
        queryBuilder.append("SELECT");
        queryBuilder.append(" t.*,");
        queryBuilder.append("ca.address1 AS address,");
        queryBuilder.append("ca.contact_name AS coName,");
        queryBuilder.append("ca.city1 AS city,ca.state AS state,");
        queryBuilder.append("CONCAT_WS(',',t.`eciAcctNo`, GROUP_CONCAT(DISTINCT (IF(ft.`eci_acct_no` <> '' and ft.`eci_acct_no` <> t.`eciAcctNo`, ft.`eci_acct_no`, NULL)))) AS eciAcctNoList,");
        queryBuilder.append("CONCAT_WS(',',t.`eciAcctNo2`, GROUP_CONCAT(DISTINCT (IF(ft.`ecifwno` <> '' and ft.`ecifwno` <> t.`eciAcctNo2`, ft.`ecifwno`, NULL)))) AS eciAcctNo2List,");
        queryBuilder.append("CONCAT_WS(',',t.`eciAcctNo3`, GROUP_CONCAT(DISTINCT (IF(ft.`ecivendno` <> '' and ft.`ecivendno` <> t.`eciAcctNo3`, ft.`ecivendno`, NULL)))) AS eciAcctNo3List,");
        queryBuilder.append("cacc.`credit_limit` AS creditLimit,");
        queryBuilder.append("`CodeGetDescById`(cacc.`credit_status`) AS creditStatus,");
        queryBuilder.append("cacc.`import_credit` AS impCr,");
        queryBuilder.append("cacc.`extend_credit` AS creditStatus,");
        queryBuilder.append("(SELECT gi.`insure` FROM `cust_general_info` gi WHERE gi.`acct_no` = t.`accountNumber`) AS insurance,");
        queryBuilder.append("`UserDetailsGetLoginNameByID`(cacc.`ar_contact_code`) AS colleCtor ");
        queryBuilder.append(" FROM");
        queryBuilder.append(" (SELECT ");
        queryBuilder.append(" tp.acct_name AS accountName,");
        queryBuilder.append(" tp.acct_no AS accountNumber,");
        queryBuilder.append(" tp.acct_type AS accountType,");
        queryBuilder.append("tp.sub_type AS subType,");
        queryBuilder.append("tp.eci_acct_no AS eciAcctNo,");
        queryBuilder.append("tp.ecifwno AS eciAcctNo2,");
        queryBuilder.append("tp.ecivendno AS eciAcctNo3,");
        queryBuilder.append("tp.disabled AS disabled,");
        queryBuilder.append("tp.forward_account AS forwardAccount,");
        queryBuilder.append("tp.masteracct_no AS masterAccount ");
        queryBuilder.append(" FROM");
        queryBuilder.append(" trading_partner tp ");
        if (CommonUtils.isNotEmpty(searchAddress)) {
            queryBuilder.append("join ");
            queryBuilder.append("(select c.acct_no from cust_address c ");
            queryBuilder.append("where c.address1 like '").append(searchAddress).append("%') as c ");
            queryBuilder.append("on (tp.acct_no = c.acct_no) ");
        }
        queryBuilder.append("WHERE ");
        queryBuilder.append("tp.type = '").append(type).append("' ");
        if (CommonUtils.isNotEmpty(account)) {
            if (account.length() < 10) {
                queryBuilder.append("and tp.acct_no like '").append(account).append("%' ");
            } else {
                queryBuilder.append("and tp.acct_no = '").append(account).append("' ");
            }
        }
        if (CommonUtils.isNotEmpty(name)) {
            queryBuilder.append("and trim(tp.search_acct_name) like '").append(name).append("%' ");
        }
        if (CommonUtils.isNotEmpty(acctType)) {
            queryBuilder.append("and tp.acct_type like '").append(acctType).append("%' ");
        }
        if (CommonUtils.isNotEmpty(subType)) {
            queryBuilder.append("and tp.sub_type = '").append(subType).append("' ");
        }
        if (CommonUtils.isNotEmpty(blueScreenAccount)) {
            queryBuilder.append("and (tp.eci_acct_no like '").append(blueScreenAccount).append("%' or ");
            queryBuilder.append(" tp.ecifwno like '").append(blueScreenAccount).append("%') ");
        }
        queryBuilder.append("limit ").append(limit).append(") as t ");
        queryBuilder.append("LEFT JOIN `cust_address` ca ON (ca.`acct_no` = t.`accountNumber` AND ca.prime = 'on') ");
        queryBuilder.append("LEFT JOIN `cust_accounting` cacc ON (cacc.`acct_no` = t.`accountNumber`)");
        queryBuilder.append("LEFT JOIN `trading_partner` ft ON (ft.`forward_account` = t.`accountNumber`)");
        queryBuilder.append(" group by t.accountNumber");
        queryBuilder.append(getSortBy(tradingPartnerForm));
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(TradingPartnerInfo.class));
        
        query.addScalar("accountName", StringType.INSTANCE);
        query.addScalar("accountNumber", StringType.INSTANCE);
        query.addScalar("accountType", StringType.INSTANCE);
        query.addScalar("subType", StringType.INSTANCE);
        
        query.addScalar("eciAcctNo", StringType.INSTANCE);
        query.addScalar("eciAcctNo2", StringType.INSTANCE);
        query.addScalar("eciAcctNo3", StringType.INSTANCE);
        
        query.addScalar("disabled", StringType.INSTANCE);
        query.addScalar("forwardAccount", StringType.INSTANCE);
        query.addScalar("masterAccount", StringType.INSTANCE);
        
        query.addScalar("address", StringType.INSTANCE);
        query.addScalar("coName", StringType.INSTANCE);
        query.addScalar("city", StringType.INSTANCE);
        query.addScalar("state", StringType.INSTANCE);
        
        query.addScalar("eciAcctNoList", StringType.INSTANCE);
        query.addScalar("eciAcctNo2List", StringType.INSTANCE);
        query.addScalar("eciAcctNo3List", StringType.INSTANCE);
        query.addScalar("creditLimit", StringType.INSTANCE);
        query.addScalar("creditStatus", StringType.INSTANCE);
        query.addScalar("impCr", StringType.INSTANCE);
        query.addScalar("creditStatus", StringType.INSTANCE);
        query.addScalar("insurance", StringType.INSTANCE);
        query.addScalar("colleCtor", StringType.INSTANCE);
        
       return query.list();
      
    }

    public List getUpdatedCustomerRecord(String accountNo) {
        String sqlQuery = "SELECT ca.acct_name,ca.acct_no,ca.acct_type,ca.address1, ca.city1, ca.state, ca.prime from cust_address ca where ca.acct_no='"
                + accountNo + "'";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sqlQuery);
        List list = queryObject.list();
        return list;
    }

    public List getAccountName(String account, String name, String acctType,
            String type) throws Exception {
        String sqlQuery = "SELECT tp.acct_name from trading_partner tp where tp.acct_name like ?0 and tp.type like '"
                + type + "' limit 50";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sqlQuery);
        queryObject.setParameter("0", name + "%", StringType.INSTANCE);
        List list = queryObject.list();
        return list;
    }

    public List getTradinPartnerAccountName(String account, String importNumber, String acctType,
            String type) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TradingPartner.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (importNumber != null && !importNumber.trim().equals("")) {
            criteria.add(Restrictions.like("accountno", importNumber + "%"));
            criteria.addOrder(Order.asc("accountno"));
        }
        return criteria.setMaxResults(20).list();
    }

    public List getAccountNo(String account, String name, String acctType,
            String type) throws Exception {
        String sqlQuery = "SELECT tp.acct_no from trading_partner tp where tp.acct_no like '"
                + account + "%' and tp.type like '" + type + "' limit 50";
        SQLQuery queryObject = getCurrentSession().createSQLQuery(sqlQuery).addScalar("acct_no", StringType.INSTANCE);
        List list = queryObject.list();
        return list;
    }

    public List getYearClosingAmountAndAccount(int year) throws Exception {
        String queryString = "select sum(period_balance)+closing_balance as year_closing_amount,account.account from account_details  account left join account_year_end_balance yearend on(account.account=yearend.account), account_balance balance where yearend.year='"
                + year
                + "' "
                + " and account.acct_type='Income Statement' and balance.year='"
                + (++year)
                + "' and balance.account=account.Account and balance.period!='CL' group by account ";

        SQLQuery queryObject = getCurrentSession().createSQLQuery(
                queryString).addScalar("year_closing_amount",
                        DoubleType.INSTANCE).addScalar("account", StringType.INSTANCE);
        List list = queryObject.list();
        return list;
    }

    public void updating(TradingPartner tradingPartner) throws Exception {
        getSession().update(tradingPartner);
        getSession().flush();
    }

    public List findMasterAddressess() throws Exception {
        List addressList = null;
        String queryString = " from CustomerAddress where type='master'";
        Query queryObject = getCurrentSession().createQuery(queryString);
        addressList = queryObject.list();
        return addressList;
    }

    public List getSubsidiariesAccountForMaster(String accountNumber) throws Exception {
        String queryString = "select tp.acct_no, tp.acct_name, tp.acct_type,tp.masteracct_no from trading_partner tp where tp.masteracct_no = '"
                + accountNumber + "'";
        String getAddress = null;
        SQLQuery queryObject = getCurrentSession().createSQLQuery(
                queryString);
        List tradingPartnerList = queryObject.list();
        TradingPartnerInfo tradingPartnerInfo = null;
        List<TradingPartnerInfo> tradingPartnerInfoList = new ArrayList<TradingPartnerInfo>();
        Object[] tradingPartner = null;
        Object[] customerAddress = null;
        List addressList = null;
        for (int i = 0; i < tradingPartnerList.size(); i++) {
            SQLQuery addressQueryObject = null;
            tradingPartnerInfo = new TradingPartnerInfo();
            tradingPartner = (Object[]) tradingPartnerList.get(i);
            getAddress = "select ca.address1, ca.city1, ca.state, ca.prime from cust_address ca where ca.acct_no='"
                    + tradingPartner[0] + "'";
            addressQueryObject = getCurrentSession().createSQLQuery(
                    getAddress).addScalar("ca.address1", StringType.INSTANCE).addScalar("ca.city1", StringType.INSTANCE).addScalar(
                            "ca.state", StringType.INSTANCE).addScalar(
                            "ca.prime", StringType.INSTANCE);
            addressList = addressQueryObject.list();
            if (null != addressList && addressList.size() >= 1) {
                customerAddress = (Object[]) addressList.get(0);

            } else if (null != addressList && addressList.size() > 1) {
                Object[] address = null;
                for (int j = 0; j < addressList.size(); j++) {
                    address = (Object[]) addressList.get(j);
                    if (null != address[3]
                            && address[3].toString().equals("on")) {
                        customerAddress = address;
                        break;
                    }

                }
            }

            if (null != tradingPartner) {
                if (null != tradingPartner[0]) {
                    tradingPartnerInfo.setAccountNumber(tradingPartner[0].toString());
                }

                if (null != tradingPartner[1]) {
                    tradingPartnerInfo.setAccountName(tradingPartner[1].toString());
                }

                if (null != tradingPartner[2]) {
                    tradingPartnerInfo.setAccountType(tradingPartner[2].toString());
                }
                if (null != tradingPartner[3]) {
                    tradingPartnerInfo.setMasterAccountNumber(tradingPartner[3].toString());
                }
            }

            if (null != customerAddress) {
                if (null != customerAddress[0]) {
                    tradingPartnerInfo.setAddress(customerAddress[0].toString());
                }

                if (null != customerAddress[1]) {
                    tradingPartnerInfo.setCity(customerAddress[1].toString());
                }

                if (null != customerAddress[2]) {
                    tradingPartnerInfo.setState(customerAddress[2].toString());
                }
            }
            tradingPartnerInfoList.add(tradingPartnerInfo);

        }
        return tradingPartnerInfoList;
    }

    private int getSearchLimit() throws Exception {
        LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();
        String limit = loadLogisoftProperties.getProperty("search_limit");
        return Integer.parseInt(limit);
    }

    public Vendor getApInvoiceByCustomerNumber(String accountNumber) throws Exception {
        Vendor vendor = null;
        Criteria criteria = getCurrentSession().createCriteria(Vendor.class);
        criteria.add(Restrictions.eq("accountno", accountNumber));
        vendor = (Vendor) criteria.setMaxResults(1).uniqueResult();
        return vendor;
    }

    public Vendor getVendorByCustomerNumber(String accountNumber) throws Exception {
        Vendor vendor = null;
        Criteria criteria = getCurrentSession().createCriteria(Vendor.class);
        criteria.add(Restrictions.eq("accountno", accountNumber));
        vendor = (Vendor) criteria.setMaxResults(1).uniqueResult();
        return vendor;
    }

    public List getPaymentListByVendorName(String vendorName) throws Exception {
        List paymentList = new ArrayList();
        String query = "select distinct pm.default_pay_method,pm.pay_method from payment_method pm,transaction tr where pm.pay_accno= tr.cust_no and tr.cust_name=?0";
        Query queryObject = getCurrentSession().createSQLQuery(query);
        queryObject.setParameter("0", vendorName, StringType.INSTANCE);
        List result = queryObject.list();
        int i = 0;
        for (int j = 0; j < result.size(); j++) {
            Object row[] = (Object[]) result.get(j);
            if (null != row[0] && row[0].toString().equals("Y")) {
                if (null != row[1]) {
                    paymentList.add(0, row[1]);
                }
            } else if (null == row[0] || !row[0].toString().equals("Y")) {
                if (null != row[1]) {
                    paymentList.add(i, row[1]);
                }
            }
            i++;
        }
        return paymentList;
    }
    // ------------------------------------------------------------------------------------------

    public void updateDefaultPaymentMethod(String payAcctNumber,
            String paymentMethod) throws Exception {
        String queryString = "update payment_method set default_pay_method='N' where pay_accno='"
                + payAcctNumber
                + "' and pay_method !='"
                + paymentMethod
                + "'";
        getCurrentSession().createSQLQuery(queryString).executeUpdate();
        queryString = "update payment_method set default_pay_method='Y' where pay_accno='"
                + payAcctNumber
                + "' and pay_method ='"
                + paymentMethod
                + "'";
        getCurrentSession().createSQLQuery(queryString).executeUpdate();
    }

    public String getAbaRoutingNoByPayAcctNoandPayMethod(String payAcctNo, String payMethod) {
        String abaRoutingNumber = null;
        String queryString = "select aba from payment_method where pay_method='"
                + payMethod + "' and pay_accno='" + payAcctNo + "'";
        Object row = getCurrentSession().createSQLQuery(queryString).setMaxResults(1).uniqueResult();
        if (null != row) {
            abaRoutingNumber = row.toString();
        }
        return abaRoutingNumber;
    }

    public String getBankAcctNumberByPayAcctNoandPayMethod(String payAcctNo, String payMethod) throws Exception {
        String bankAcctNumber = null;
        String queryString = "select acct_no from payment_method where pay_method='"
                + payMethod + "' and pay_accno='" + payAcctNo + "'";
        Object row = getCurrentSession().createSQLQuery(queryString).setMaxResults(1).uniqueResult();
        if (null != row) {
            bankAcctNumber = row.toString();
        }
        return bankAcctNumber;
    }

    public String getCustomerWithAccountTypeZ(String accountType) throws Exception {
        String queryString = "select count(*) from TradingPartner where acctType='" + accountType + "'";
        String customerCount = getCurrentSession().createQuery(queryString).uniqueResult().toString();
        return customerCount.equals("0") ? "" : "Company with account Type Z exists";
    }

    public TradingPartner getNvoCompanyDetails() throws Exception {
        String acctType = "Z";
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append(" t.acct_no as accountno,");
        queryBuilder.append(" t.acct_name as accountName ");
        queryBuilder.append(" from");
        queryBuilder.append(" trading_partner t");
        queryBuilder.append(" where");
        queryBuilder.append(" t.acct_type='").append(acctType).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("accountno", StringType.INSTANCE);
        query.addScalar("accountName", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(TradingPartner.class));
        query.setMaxResults(1);
        return (TradingPartner) query.uniqueResult();
    }

    public String getContractNo(String ssLineNo) throws Exception {
        String queryString = "select l.contra from trading_partner t,tlines l where t.acct_no = '"
                + ssLineNo + "' and l.linnum = t.ssline_number";
        Object obj = getCurrentSession().createSQLQuery(queryString).setMaxResults(1).uniqueResult();
        return null != obj ? obj.toString() : "";
    }

    public String getRemitEmailForPaymentMethod(String paymentMethod,
            String vendorNo) throws Exception {
        String queryString = "select distinct pm.remit_email from payment_method pm where pm.pay_accno ='"
                + vendorNo + "' and pay_method='" + paymentMethod + "'";
        Object obj = getCurrentSession().createSQLQuery(queryString).setMaxResults(1).uniqueResult();
        return null != obj ? obj.toString() : null;
    }

    public TradingPartner getDisabledRecord(String accountNo) throws Exception {
        String queryString = "from TradingPartner where accountno = '" + accountNo + "'";
        Object tpObject = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        return null != tpObject ? (TradingPartner) tpObject : null;
    }

    public List getTPListForSearchCriteria(String acctName, User user, String customerState, String customerCountry) throws Exception {
        acctName = acctName.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.acct_no,t.acct_name,t.acct_type,c.address1,c.city1,c.state,c.zip,t.disabled,");
        queryBuilder.append("t.sub_type,t.ssline_number,eci_acct_no,ecifwno from trading_partner t ");
        queryBuilder.append("left join cust_address c on t.acct_no = c.acct_no and c.prime = 'on'");
        queryBuilder.append(" left join genericcode_dup g ON g.id=c.country  ");
        queryBuilder.append(" where (t.search_acct_name like '").append(acctName);
        queryBuilder.append("%' or t.acct_no like '").append(acctName).append("%')");
        if (CommonUtils.isNotEmpty(customerState)) {
            queryBuilder.append(" and (c.state like '").append(customerState).append("%')");
        }
        if (CommonUtils.isNotEmpty(customerCountry)) {
            queryBuilder.append(" and(g.codedesc like '").append(customerCountry).append("%')");
        }
        if (null != user && null != user.getRole() && !user.getRole().getRoleDesc().equalsIgnoreCase(CommonConstants.ROLE_NAME_ADMIN)) {
            queryBuilder.append(" and t.acct_type not like '%Z%' ");
        }
        queryBuilder.append(" group by t.acct_name,t.acct_no ");
        queryBuilder.append(" limit 100");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List getForwardAccounts(String acctName, User user, String tradingPartnerAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.acct_no,t.acct_name,t.acct_type,c.address1,c.city1,c.state,c.zip,");
        queryBuilder.append("t.disabled,t.sub_type,t.ssline_number");
        queryBuilder.append(" from trading_partner t");
        queryBuilder.append(" left join cust_address c");
        queryBuilder.append(" on t.acct_no = c.acct_no and c.prime = 'on'");
        queryBuilder.append(" where t.search_acct_name like '").append(acctName).append("%'");
        queryBuilder.append(" or t.acct_no like '").append(acctName).append("%'");
        queryBuilder.append(" and (t.disabled != 'Y' or t.disabled is null))");
        if (null != user && null != user.getRole() && !user.getRole().getRoleDesc().equalsIgnoreCase(CommonConstants.ROLE_NAME_ADMIN)) {
            queryBuilder.append(" and t.acct_type not like '%Z%'");
        }
        queryBuilder.append(" and t.acct_no != '").append(tradingPartnerAccount).append("' LIMIT 100");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List getTPForQuotBookBL(String acctName, String acctTyp) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        acctName = acctName.replace(" ", "").replace(":", "");
        stringBuilder.append("select t.acct_no,t.acct_name,t.acct_type,c.address1,c.city1,c.state,c.zip,t.disabled,");
        stringBuilder.append("t.sub_type,t.ssline_number,c.fax,c.email1,c.email2 ");
        stringBuilder.append(",t.eci_acct_no, t.ecifwno,s.code from ");
        stringBuilder.append(" (select t.acct_no,t.acct_name,t.acct_type,t.disabled,t.sub_type,t.ssline_number,");
        stringBuilder.append(" t.eci_acct_no, t.ecifwno");
        stringBuilder.append(" from trading_partner t   where (t.search_acct_name like '").append(acctName).append("%'");
        stringBuilder.append(" or t.acct_no like '").append(acctName).append("%' or t.acct_name like '").append(acctName).append("%' ) ");
        stringBuilder.append(" and t.type <> 'master'");
        if (null != acctTyp && acctTyp.equals("S")) {
            stringBuilder.append(" and (t.acct_type like '%S%' or (t.acct_type like '%V%' and t.sub_type='Forwarder') or t.acct_type like '%E%' or t.acct_type like '%I%') ");
        } else if (null != acctTyp && acctTyp.equals("HS")) {
            stringBuilder.append(" and (t.acct_type like '%S%' or (t.acct_type like '%V%' and t.sub_type='Forwarder') or t.acct_type like '%E%' or t.acct_type like '%I%') and t.acct_type not like '%Z%' ");
        } else if (null != acctTyp && acctTyp.equals("F")) {
            stringBuilder.append(" and t.acct_type like '%V%' and t.acct_type not like '%Z%' and t.sub_type='Forwarder' ");
        } else if (null != acctTyp && acctTyp.equals("C")) {
            stringBuilder.append(" and t.acct_type like '%C%' ");
        } else if (null != acctTyp && acctTyp.equals("HC")) {
            stringBuilder.append(" and t.acct_type like '%C%' and t.acct_type not like '%Z%' ");
        } else if (null != acctTyp && acctTyp.equals("V")) {
            stringBuilder.append(" and t.acct_type like '%V%' and t.acct_type not like '%Z%' ");
        } else if (null != acctTyp && acctTyp.equals("T")) {
            stringBuilder.append(" and (t.acct_type like '%S%' or (t.acct_type like '%V%' and t.sub_type='Forwarder') or t.acct_type like '%O%') and t.acct_type not like '%Z%' ");
        } else if (null != acctTyp && !acctTyp.equals("")) {
            stringBuilder.append(" and t.acct_type like '%").append(acctTyp).append("%'");
        }
        stringBuilder.append(" order by t.search_acct_name limit 100)");
        stringBuilder.append(" as t left join cust_address c on t.acct_no = c.acct_no and c.prime = 'on' left join cust_general_info g on (t.acct_no = g.acct_no) left join genericcode_dup s on (g.sales_code = s.id)");
        return getCurrentSession().createSQLQuery(stringBuilder.toString()).list();
    }

    public List getZAccounts(String acctName, String acctTyp) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        acctName = acctName.replace(" ", "").replace(":", "");
        stringBuilder.append("select t.acct_name,t.acct_no,t.acct_type,c.address1,c.city1,c.state,c.zip,t.disabled,");
        stringBuilder.append("t.sub_type,t.ssline_number,c.fax,c.email1,c.email2 ");
        stringBuilder.append(",t.eci_acct_no, t.ecifwno,s.code from ");
        stringBuilder.append(" (select t.acct_name,t.acct_no,t.acct_type,t.disabled,t.sub_type,t.ssline_number,");
        stringBuilder.append(" t.eci_acct_no, t.ecifwno");
        stringBuilder.append(" from trading_partner t   where (t.search_acct_name like '").append(acctName).append("%'");
        stringBuilder.append(" or t.acct_no like '").append(acctName).append("%' or t.acct_name like '").append(acctName).append("%' ) ");
        stringBuilder.append(" and t.type <> 'master'");
        if (null != acctTyp && acctTyp.equals("Z")) {
            stringBuilder.append(" and t.acct_type like '%Z%' ");
        }
        stringBuilder.append(" order by t.search_acct_name limit 100)");
        stringBuilder.append(" as t left join cust_address c on t.acct_no = c.acct_no and c.prime = 'on' left join cust_general_info g on (t.acct_no = g.acct_no) left join genericcode_dup s on (g.sales_code = s.id)");
        return getCurrentSession().createSQLQuery(stringBuilder.toString()).list();
    }

    public List getTradingPartner(String acctName, String acctTyp) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        acctName = acctName.replace("'", "");
        queryBuilder.append("select t.acct_no,t.acct_name,t.acct_type,c.address1,c.city1,c.state,c.zip,t.disabled,");
        queryBuilder.append("t.sub_type,t.ssline_number from  trading_partner t left join cust_address c on ");
        queryBuilder.append("t.acct_no = c.acct_no and c.prime = 'on' where t.search_acct_name like '");
        queryBuilder.append(acctName).append("%' and c.acct_type like '%").append(acctTyp).append("%' and ");
        queryBuilder.append("t.sub_type in('Steamship Line','Air Line') limit 100");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List getTPForClientOtherThanConsignee(String acctName, String acctTyp) throws Exception {
        acctName = acctName.replace("'", "");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.acct_no,t.acct_name,t.acct_type,c.address1,c.city1,c.state,c.zip,t.disabled,t.sub_type,t.ssline_number,gd.code ");
        queryBuilder.append("from trading_partner t left join cust_address c on t.acct_no = c.acct_no and c.prime = 'on' left join cust_general_info cg ");
        queryBuilder.append("on t.acct_no = cg.acct_no left join genericcode_dup gd on cg.sales_code = gd.id where t.search_acct_name like '");
        queryBuilder.append(acctName).append("%' and c.acct_type !='").append(acctTyp).append("'  order by t.acct_name limit 100");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public List getTPForSSL(String acctName, String acctTyp) throws Exception {
        List list = new ArrayList();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select t.acct_no,t.acct_name,t.acct_type,c.address1,c.city1,c.state,c.zip,t.disabled,t.sub_type,t.ssline_number");
        queryBuilder.append(" from trading_partner t left join cust_address c on t.acct_no = c.acct_no and c.prime = 'on'");
        queryBuilder.append(" where (t.search_acct_name like '").append(acctName.trim()).append("%' or");
        queryBuilder.append(" t.acct_no LIKE '").append(acctName.trim()).append("%' or t.acct_name like '").append(acctName.trim()).append("%' ) ");
        queryBuilder.append(" and c.acct_type like '%").append(acctTyp).append("%' and t.sub_type = 'Steamship Line' limit 50");
        list = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return list;
    }

    public List getTPAGentListForQuotBookBL(String unLocCode, String agentName, boolean flag) throws Exception {
        String type = null;
        StringBuilder stringBuilder = new StringBuilder();
        type = flag ? "I" : "F";
        agentName = agentName.replaceAll("[^a-zA-Z0-9]+", "");
        stringBuilder.append("select t.acct_no,t.acct_name,t.acct_type,c.address1,c.city1,c.state,c.zip,");
        stringBuilder.append("t.disabled,t.sub_type,t.ssline_number from trading_partner t join cust_address c ");
        stringBuilder.append("on t.acct_no = c.acct_no and c.prime = 'on', agency_info a ");
        stringBuilder.append("join ports p on p.unlocationcode = '").append(unLocCode).append("' and p.id = a.schnum and a.type = '");
        stringBuilder.append(type).append("' where t.acct_no = a.agentid and (t.search_acct_name like '");
        stringBuilder.append(agentName).append("%' or t.acct_no like '");
        stringBuilder.append(agentName).append("%') and t.acct_type not like '%Z%' limit 100");
        return getCurrentSession().createSQLQuery(stringBuilder.toString()).list();
    }

    public void updateCorrespondingTables(String query) throws Exception {
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public String chekForDisable(String accountNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(disabled='Y',concat('This customer is disabled and merged with ',if(forward_account!='',forward_account,'')),'')");
        queryBuilder.append(" from trading_partner");
        queryBuilder.append(" where acct_no= '").append(accountNo).append("'");
        queryBuilder.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public boolean checkDisabledAccount(String accountNo) throws Exception {
        String query = "select count(*) from trading_partner t where t.acct_no = '" + accountNo + "' and t.disabled = 'Y'";
        String count = getCurrentSession().createSQLQuery(query).uniqueResult().toString();
        return !count.equals("0");
    }

    public boolean checkBlueScreenAccount(String accountNo) throws Exception {
        String query = "select if(count(*)>0,'true','false') as result from trading_partner where acct_no = '" + accountNo + "' and (eci_acct_no != '' or ecifwno != '' or ecivendno != '')";
        String result = (String) getCurrentSession().createSQLQuery(query).addScalar("result", StringType.INSTANCE).uniqueResult();
        return Boolean.valueOf(result);
    }

    @SuppressWarnings("unchecked")
    public TradingPartner getMaster(String accountNo) throws Exception {
        String queryString = "from TradingPartner where accountno in (select master from TradingPartner where accountno='" + accountNo + "')";
        Object masterList = getSession().createQuery(queryString).setMaxResults(1).uniqueResult();
        return null != masterList ? (TradingPartner) masterList : null;
    }

    /**
     * @param accountNo
     * @return
     */
    public String getAccountPrefix(String accountNumber) throws Exception {
        Query queryObject = getCurrentSession().createQuery("select accountno from TradingPartner where accountno like ?0 order by accountno desc");
        queryObject.setParameter("0", accountNumber + "%");
        queryObject.setMaxResults(1);
        String result = (String) queryObject.uniqueResult();
        if (CommonUtils.isNotEmpty(result)) {
            return result.substring(result.length() - 4, result.length());
        }
        return "0000";
    }

    public String getPaymentMethod(String accountNumber) throws Exception {
        String payMethod = null;
        Criteria criteria = getCurrentSession().createCriteria(PaymentMethod.class);
        criteria.add(Restrictions.eq("payaccno", accountNumber));
        criteria.addOrder(Order.asc("id"));
        List<PaymentMethod> result = criteria.list();
        if (CommonUtils.isNotEmpty(result)) {
            boolean isFirst = true;
            for (PaymentMethod paymentMethod : result) {
                if (isFirst) {
                    payMethod = paymentMethod.getPaymethod();
                }
                if (CommonUtils.isEqual(paymentMethod.getDefaultpaymethod(), CommonConstants.YES)) {
                    payMethod = paymentMethod.getPaymethod();
                }
            }
        }
        return payMethod;
    }

    public String getChilds(String accountNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select concat(\"'\",acct_no,\"'\")");
        queryBuilder.append(" from trading_partner where masteracct_no='").append(accountNo).append("'");
        queryBuilder.append(" order by acct_name,acct_no");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            return result.toString().replace("[", "").replace("]", "");
        }
        return null;
    }

    public List getTradingPartners(String account, String accountType, boolean disabled) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select tp.acct_name,tp.acct_no,tp.acct_type,tp.sub_type,tp.type,ca.address1,");
        queryBuilder.append(" ca.city1,ca.state,gen.codedesc from trading_partner tp");
        queryBuilder.append(" left join cust_address ca on (ca.acct_no=tp.acct_no and ca.prime='on')");
        queryBuilder.append(" left join genericcode_dup gen on gen.id=ca.country");
        queryBuilder.append(" where tp.acct_no in (SELECT acct_no FROM trading_partner WHERE (acct_no like ?0 or acct_name like ?1)");
        queryBuilder.append(" and tp.type <> 'master'");
        if (CommonUtils.isEqualIgnoreCase(accountType, "NoConsignee")) {
            queryBuilder.append(" and acct_type!='C'");
        } else if (CommonUtils.isEqualIgnoreCase(accountType, "Consignee")) {
            queryBuilder.append(" and acct_type='C'");
        } else if (CommonUtils.isEqualIgnoreCase(accountType, "SteamShipline")) {
            queryBuilder.append(" and acct_type like '%V%' and sub_type='Steamship Line'");
        } else if (CommonUtils.isNotEmpty(accountType)) {
            queryBuilder.append(" and acct_type like '%").append(accountType).append("%'");
        }
        if (disabled) {
            queryBuilder.append(" and disabled = 'Y'");
        } else {
            queryBuilder.append(" and (disabled != 'Y' or disabled is null)");
        }
        queryBuilder.append(") and tp.acct_type not like '%Z%' group by tp.acct_name,tp.acct_no");
        if (CommonUtils.isEqualIgnoreCase(accountType, "V") || CommonUtils.isEqualIgnoreCase(accountType, "SteamshipLine")) {
            queryBuilder.append(" order by tp.acct_no,tp.acct_name limit 50");
        } else {
            queryBuilder.append(" order by tp.acct_name,tp.acct_no limit 50");
        }
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", account + "%");
        query.setParameter("1", account + "%");
        return query.list();
    }

    public List getTradingPartners(String account, boolean disabled, boolean openInvoice) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name,tp.acct_no,tp.acct_type,");
        queryBuilder.append("tp.sub_type,tp.type,ca.address1,ca.city1,ca.state,gen.codedesc");
        queryBuilder.append(" from");
        if (openInvoice) {
            queryBuilder.append(" (select tp.acct_no,tp.acct_name,tp.acct_type,tp.sub_type,tp.type");
            queryBuilder.append(" from (");
            queryBuilder.append("(select tp.acct_no,tp.acct_name,tp.acct_type,tp.sub_type,tp.type");
            queryBuilder.append(" from trading_partner tp");
            queryBuilder.append(" where (tp.acct_no like ?2 or tp.acct_name like ?3)");
            queryBuilder.append(" and tp.type = 'master'");
            if (disabled) {
                queryBuilder.append(" and tp.disabled = 'Y'");
            } else {
                queryBuilder.append(" and (tp.disabled != 'Y' or tp.disabled is null)");
            }
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select tp.acct_no,tp.acct_name,tp.acct_type,tp.sub_type,tp.type");
            queryBuilder.append(" from");
        }
        queryBuilder.append(" (select tp.acct_no,tp.acct_name,tp.acct_type,tp.sub_type,tp.type");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" where (tp.acct_no like ?0 or tp.acct_name like ?1)");
        if (disabled) {
            queryBuilder.append(" and tp.disabled = 'Y'");
        } else {
            queryBuilder.append(" and (tp.disabled != 'Y' or tp.disabled is null)");
        }
        if (openInvoice) {
            queryBuilder.append(") as tp");
            queryBuilder.append(" join transaction tr");
            queryBuilder.append(" where (tr.transaction_type='AR' and tr.balance<>0 and tp.acct_no=tr.cust_no)");
            queryBuilder.append(" group by tp.acct_no limit 50");
            queryBuilder.append(")");
            queryBuilder.append(") as tp");
        } else {
            queryBuilder.append(" limit 50");
        }
        queryBuilder.append(") as tp");
        queryBuilder.append(" left join cust_address ca on (ca.acct_no=tp.acct_no and ca.prime='on')");
        queryBuilder.append(" left join genericcode_dup gen on gen.id=ca.country");
        queryBuilder.append(" group by tp.acct_name,tp.acct_no");
        queryBuilder.append(" order by tp.acct_name,tp.acct_no limit 50");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", account + "%");
        query.setParameter("1", account + "%");
        if (openInvoice) {
            query.setParameter("2", account + "%");
            query.setParameter("3", account + "%");
        }
        return query.list();
    }

    public List getTradingPartnersForMaster(String account, boolean disabled, boolean openInvoice) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name,tp.acct_no,tp.acct_type,");
        queryBuilder.append("tp.sub_type,tp.type,ca.address1,ca.city1,ca.state,gen.codedesc");
        queryBuilder.append(" from");

        queryBuilder.append(" (select tp.acct_no,tp.acct_name,tp.acct_type,tp.sub_type,tp.type");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" where (tp.acct_no like ?0 or tp.acct_name like ?1)");
        queryBuilder.append(" and tp.type = 'master'");
        if (disabled) {
            queryBuilder.append(" and tp.disabled = 'Y'");
        } else {
            queryBuilder.append(" and (tp.disabled != 'Y' or tp.disabled is null)");
        }

        queryBuilder.append(" limit 50");

        queryBuilder.append(") as tp");
        queryBuilder.append(" left join cust_address ca on (ca.acct_no=tp.acct_no and ca.prime='on')");
        queryBuilder.append(" left join genericcode_dup gen on gen.id=ca.country");
        queryBuilder.append(" group by tp.acct_name,tp.acct_no");
        queryBuilder.append(" order by tp.acct_name,tp.acct_no limit 50");
        Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setParameter("0", account + "%");
        query.setParameter("1", account + "%");

        return query.list();
    }

    public void unlockTradingPartnerForArBatch(String exceptionAccountNo, Integer userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("update trading_partner");
        queryBuilder.append(" set ar_batch_lock_user=null where ar_batch_lock_user=").append(userId);
        if (CommonUtils.isNotEmpty(exceptionAccountNo)) {
            queryBuilder.append(" and acct_no!='").append(exceptionAccountNo).append("'");
        }
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public Vendor getVendor(String acctNo) throws Exception {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(Vendor.class);
        criteria.add(Restrictions.eq("accountno", acctNo));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (Vendor) criteria.uniqueResult();
    }

    public boolean isMaster(String acctNo) {
        String queryString = "select if(count(*)>0,'true','false') as result from trading_partner where type='master' and acct_no='" + acctNo + "'";
        String result = (String) getCurrentSession().createSQLQuery(queryString).addScalar("result", StringType.INSTANCE).uniqueResult();
        return Boolean.valueOf(result);
    }

    public void save(Vendor transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        getCurrentSession().flush();
    }

    public void update(Vendor persistentInstance) throws Exception {
        getCurrentSession().update(persistentInstance);
        getCurrentSession().flush();
    }

    public String getSubType(String acctNo) throws Exception {
        getCurrentSession().flush();
        String query = "select sub_type from trading_partner where acct_no='" + acctNo + "' limit 1";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public void updateSubType(String acctNo, String subType) throws Exception {
        getCurrentSession().flush();
        String query = "update trading_partner set sub_type='" + subType + "' where acct_no='" + acctNo + "'";
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public boolean isFirstPaymentMethod(String acctNo) throws Exception {
        getCurrentSession().flush();
        String query = "select count(*) from payment_method where pay_accno='" + acctNo + "'";
        Object count = getCurrentSession().createSQLQuery(query).uniqueResult();
        return null != count && Integer.parseInt(count.toString()) > 0 ? false : true;
    }

    public PaymentMethod getPaymentMethod(String acctNo, String paymentMethod) {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(PaymentMethod.class);
        criteria.add(Restrictions.eq("payaccno", acctNo));
        criteria.add(Restrictions.eq("paymethod", paymentMethod));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (PaymentMethod) criteria.uniqueResult();
    }

    public Set<PaymentMethod> getPaymentMethods(String acctNo) {
        getCurrentSession().flush();
        Criteria criteria = getCurrentSession().createCriteria(PaymentMethod.class);
        criteria.add(Restrictions.eq("payaccno", acctNo));
        criteria.addOrder(Order.desc("defaultpaymethod"));
        List<PaymentMethod> result = criteria.list();
        Set<PaymentMethod> paymentMethods = new HashSet<PaymentMethod>();
        if (null != result) {
            paymentMethods.addAll(result);
        }
        return paymentMethods;
    }

    public void save(PaymentMethod transientInstance) throws Exception {
        getCurrentSession().save(transientInstance);
        if (getCurrentSession().getTransaction().isActive()) {
            getCurrentSession().getTransaction().commit();
        }
        getCurrentSession().beginTransaction();
    }

    public void update(PaymentMethod persistentInstance) throws Exception {
        getCurrentSession().update(persistentInstance);
        getCurrentSession().flush();
    }

    public void updateDefaultPaymentMethod(String acctNo) throws Exception {
        String query = "update payment_method set default_pay_method='" + CommonConstants.NO + "' where pay_accno='" + acctNo + "'";
        getCurrentSession().createSQLQuery(query).executeUpdate();
        getCurrentSession().flush();
    }

    public void deletePaymentMethod(String acctNo, String paymentMethod) throws Exception {
        String query = "delete from payment_method where pay_accno='" + acctNo + "' and pay_method='" + paymentMethod + "'";
        getCurrentSession().createSQLQuery(query).executeUpdate();
        getCurrentSession().flush();
    }

    public String getTradingpatnerAccName(String accNo) throws Exception {
        String result = "";
        if (accNo != null && !accNo.equals("")) {
            String queryString = "SELECT acct_name FROM trading_partner WHERE acct_no = '" + accNo + "'";
            Query queryObject = getSession().createSQLQuery(queryString);
            result = null != queryObject.uniqueResult() ? (String) queryObject.uniqueResult().toString() : "";
        }
        return result;
    }

    public String getTradingpatnerAccNo(String acctName) throws Exception {
        String result = "";
        if (acctName != null && !acctName.equals("")) {
            String queryString = "SELECT acct_no FROM trading_partner WHERE acct_name = '" + acctName + "' and (disabled is null or disabled = '') limit 1";
            Query queryObject = getSession().createSQLQuery(queryString);
            result = null != queryObject.uniqueResult() ? (String) queryObject.uniqueResult().toString() : "";
        }
        return result;
    }

    public String getEciAcct(String accNo) throws Exception {
        String result = "";
        String queryString = "select eci_acct_no from trading_partner where acct_no='" + accNo + "'";
        Query queryObject = getSession().createSQLQuery(queryString);
        result = null != queryObject.uniqueResult() ? queryObject.uniqueResult().toString() : "";
        return result;
    }

    public List<TradingPartner> existingNewCustomerList(String accountName, String unLocCode) throws Exception {
        List<TradingPartner> queryObject = new ArrayList<TradingPartner>();
        String firstWord = "";
        firstWord = accountName.replaceAll("[^a-zA-Z0-9]", "");
        String queryString = "FROM TradingPartner tp INNER JOIN fetch tp.customerLocation ul WHERE tp.searchAcctName LIKE '" + firstWord + "%' AND ul.unLocationCode ='" + unLocCode + "'";
        queryObject = getSession().createQuery(queryString).list();
        return queryObject;
    }

    public boolean isAnyOpenInvoices(String customerNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(sum(t.open_invoice)>0,'true','false') as open_invoice");
        queryBuilder.append(" from (");
        queryBuilder.append(" (select count(*) as open_invoice from transaction ar");
        queryBuilder.append(" where ar.Transaction_type='AR' and ar.Balance!=0 and ar.cust_no='").append(customerNumber).append("')");
        queryBuilder.append(" union");
        queryBuilder.append(" (select count(*) as open_invoice from transaction ap");
        queryBuilder.append(" where ap.Transaction_type='AP' and ap.Balance!=0 and ap.status='Open'");
        queryBuilder.append(" and ap.cust_no='").append(customerNumber).append("')");
        queryBuilder.append(" union");
        queryBuilder.append(" (select count(*) as open_invoice from transaction_ledger ac");
        queryBuilder.append(" where ac.Transaction_type='AC' and ac.balance!=0 and ac.status!='AS' and ac.status!='EDI Assigned'");
        queryBuilder.append(" and ac.cust_no='").append(customerNumber).append("')");
        queryBuilder.append(" ) as t");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Boolean.valueOf(result);
    }

    public void updateFclbl(String tableName, String accountNumberColumn, String forwardedAccount, String accountNameColumn,
            String accountNameValue, String accountAddressColumn, String accountAddressValue, String disabledAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ").append(tableName);
        queryBuilder.append(" set ").append(accountNumberColumn).append(" = '").append(forwardedAccount).append("',");
        queryBuilder.append(accountNameColumn).append(" = '").append(accountNameValue.replace("'", "\\'")).append("',");
        queryBuilder.append(accountAddressColumn).append(" = '").append(accountAddressValue.replace("'", "\\'")).append("'");
        queryBuilder.append(" where ").append(accountNumberColumn).append(" = '").append(disabledAccount).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public String getAccountAddress(String accountNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select concat(");
        queryBuilder.append(" if(c.co_name != '',concat(c.co_name,'\\n'),''),");
        queryBuilder.append(" if(c.address1 != '',c.address1,''),' \\n',");
        queryBuilder.append(" if(c.city1 != '',concat(c.city1,','),''),");
        queryBuilder.append(" if(c.state != '',concat(c.state,' '),''),");
        queryBuilder.append(" if(gd.codedesc != '' and gd.codedesc != 'UNITED STATES',concat(gd.codedesc,'\\n'),''),");
        queryBuilder.append(" if(c.phone != '',concat('PHONE ',c.phone),'')");
        queryBuilder.append(" ) as address");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" left join cust_address c on tp.acct_no = c.acct_no");
        queryBuilder.append(" left join genericcode_dup gd on gd.id = c.country");
        queryBuilder.append(" where tp.acct_no = '").append(accountNumber).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
        return result.toString();
    }

    public TradingPartnerModel getTradingPartnerDetails(String accountNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_no as number,tp.acct_name as name,");
        queryBuilder.append("if(c.address1 != '',c.address1,'') as address,");
        queryBuilder.append("if(c.city1 != '',c.city1,'') as city,");
        queryBuilder.append("if(c.state != '',c.state,'') as state,");
        queryBuilder.append("if(c.zip != '',c.zip,'') as zip,");
        queryBuilder.append("if(gd.codedesc != '',gd.codedesc,'') as country,");
        queryBuilder.append("if(c.phone != '',c.phone,'') as phone,");
        queryBuilder.append("if(c.fax != '',c.fax,'') as fax,");
        queryBuilder.append("if(c.email1 != '',c.email1,'') as email");
        queryBuilder.append(" from  trading_partner tp");
        queryBuilder.append(" left join cust_address c on tp.acct_no = c.acct_no");
        queryBuilder.append(" left join genericcode_dup gd on gd.id = c.country");
        queryBuilder.append(" where tp.acct_no = '").append(accountNumber).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(TradingPartnerModel.class));
        query.setMaxResults(1);
        return (TradingPartnerModel) query.uniqueResult();
    }

    public void updateBookingfcl(String tableName, String accountNumberColumn, String accountNameColumn, String addressColumn,
            String cityColumn, String stateColumn, String zipColumn, String countryColumn, String phoneColumn,
            String faxColumn, String email, TradingPartnerModel tradingPartnerModel, String disabledAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ").append(tableName);
        queryBuilder.append(" set ").append(accountNumberColumn).append(" = '").append(tradingPartnerModel.getNumber()).append("',");
        queryBuilder.append(accountNameColumn).append(" = '").append(tradingPartnerModel.getName().replace("'", "\\'")).append("',");
        queryBuilder.append(addressColumn).append(" = '").append(tradingPartnerModel.getAddress()).append("',");
        queryBuilder.append(cityColumn).append(" = '").append(tradingPartnerModel.getCity().replace("'", "\\'")).append("',");
        queryBuilder.append(stateColumn).append(" = '").append(tradingPartnerModel.getState()).append("',");
        queryBuilder.append(zipColumn).append(" = '").append(tradingPartnerModel.getZip()).append("',");
        queryBuilder.append(countryColumn).append(" = '").append(tradingPartnerModel.getCountry()).append("',");
        queryBuilder.append(phoneColumn).append(" = '").append(tradingPartnerModel.getPhone()).append("',");
        queryBuilder.append(faxColumn).append(" = '").append(tradingPartnerModel.getFax()).append("',");
        queryBuilder.append(email).append(" = '").append(tradingPartnerModel.getEmail()).append("'");
        queryBuilder.append(" where ").append(accountNumberColumn).append(" = '").append(disabledAccount).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateAccount(String tableName, String accountNumberColumn, String forwardedAccount, String disabledAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ").append(tableName);
        queryBuilder.append(" set ").append(accountNumberColumn).append(" = '").append(forwardedAccount).append("'");
        queryBuilder.append(" where ").append(accountNumberColumn).append(" = '").append(disabledAccount).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateAccount(String tableName, String accountNumberColumn, String forwardedAccount,
            String accountNameColumn, String accountNameValue, String disabledAccount) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ").append(tableName);
        queryBuilder.append(" set ").append(accountNumberColumn).append(" = '").append(forwardedAccount).append("',");
        queryBuilder.append(accountNameColumn).append(" = '").append(accountNameValue.replace("'", "\\'")).append("'");
        queryBuilder.append(" where ").append(accountNumberColumn).append(" = '").append(disabledAccount).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public TradingPartnerModel getAccountDetails(String accountNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_no as number,tp.acct_name as name,c.phone as phone,c.fax as fax,");
        queryBuilder.append("c.email1 as email,c.contact_name as contactName,c.sub_type as subType,");
        queryBuilder.append("concat(tp.acct_type,if(tp.acct_type like '%v%' and tp.sub_type != '',");
        queryBuilder.append("concat('-(',tp.sub_type,if(tp.sub_type = 'steamship line' and tp.ssline_number != '',");
        queryBuilder.append("concat(' : ',tp.ssline_number),''),')'),'')) as clientType");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" left join cust_address c on tp.acct_no = c.acct_no");
        queryBuilder.append(" left join genericcode_dup gd on gd.id = c.country");
        queryBuilder.append(" where tp.acct_no = '").append(accountNumber).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(TradingPartnerModel.class));
        query.setMaxResults(1);
        return (TradingPartnerModel) query.uniqueResult();
    }

    public void updateQuotation(String tableName, String accountNumberColumn, String forwardedAccount, String accountNameColumn, String contactName,
            String email, String phone, String fax, String clientType, TradingPartnerModel tradingPartnerModel,
            String disabledAccount) throws Exception {
        StringBuilder accountType = new StringBuilder();
        String type = tradingPartnerModel.getClientType();
        if (type.contains("S")) {
            accountType.append("Shipper,");
        }
        if (type.contains("F")) {
            accountType.append("Forwarder,");
        }
        if (type.contains("C")) {
            accountType.append("Consignee,");
        }
        if (type.contains("N")) {
            accountType.append("Notify Party,");
        }
        if (type.contains("SS")) {
            accountType.append("Stream ShipeLine,");
        }
        if (type.contains("T")) {
            accountType.append("Truck Line,");
        }
        if (type.contains("A")) {
            accountType.append("Agent,");
        }
        if (type.contains("I")) {
            accountType.append("Import Agent,");
        }
        if (type.contains("E")) {
            accountType.append("Export Agent,");
        }
        if (type.contains("V")) {
            if (CommonUtils.isNotEmpty(tradingPartnerModel.getSubType()) && !tradingPartnerModel.getSubType().equals("0")) {
                accountType.append("Vendor(").append(tradingPartnerModel.getSubType()).append("),");
            } else {
                accountType.append("Vendor,");
            }
        }
        if (type.contains("O")) {
            accountType.append("Others,");
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ").append(tableName);
        queryBuilder.append(" set ").append(accountNumberColumn).append(" = '").append(tradingPartnerModel.getNumber()).append("',");
        queryBuilder.append(accountNameColumn).append(" = '").append(tradingPartnerModel.getName().replace("'", "\\'")).append("',");
        queryBuilder.append(contactName).append(" = '").append(tradingPartnerModel.getContactName()).append("',");
        queryBuilder.append(email).append(" = '").append(tradingPartnerModel.getEmail()).append("',");
        queryBuilder.append(phone).append(" = '").append(tradingPartnerModel.getPhone()).append("',");
        queryBuilder.append(fax).append(" = '").append(tradingPartnerModel.getFax()).append("',");
        queryBuilder.append(clientType).append(" = '").append(accountType.toString()).append("'");
        queryBuilder.append(" where ").append(accountNumberColumn).append(" = '").append(disabledAccount).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void disableTradingPartner(String acctNo, String forwardAcctNo, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  trading_partner tp ");
        queryBuilder.append("set");
        queryBuilder.append("  tp.disabled = 'Y',");
        queryBuilder.append("  tp.disabled_time = now(),");
        queryBuilder.append("  tp.update_by = '").append(user.getLoginName()).append("', ");
        queryBuilder.append("  tp.forward_account = '").append(forwardAcctNo).append("' ");
        queryBuilder.append("where");
        queryBuilder.append("  tp.acct_no = '").append(acctNo).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();

        queryBuilder.delete(0, queryBuilder.length());
        queryBuilder.append("call MergeTradingPartner ('").append(acctNo).append("', ").append(user.getUserId()).append(")");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().flush();

        String description = "Account is disabled and merged with " + forwardAcctNo;
        String moduleId = "TRADING_PARTNER";
        AuditNotesUtils.insertAuditNotes(description, moduleId, acctNo, null, user);
    }

    public void enableTradingPartner(String acctNo, HttpServletRequest request) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  trading_partner tp ");
        queryBuilder.append("set");
        queryBuilder.append("  tp.disabled = null,");
        queryBuilder.append("  tp.disabled_time = null,");
        queryBuilder.append("  tp.forward_account = null ");
        queryBuilder.append("where");
        queryBuilder.append("  tp.acct_no = '").append(acctNo).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

        User user = (User) request.getSession().getAttribute("loginuser");
        String description = "Account is enabled ";
        String moduleId = "TRADING_PARTNER";
        AuditNotesUtils.insertAuditNotes(description, moduleId, acctNo, null, user);

    }

    public String getAccountName(String accountNumber) {
        String query = "select acct_name from trading_partner where acct_no='" + accountNumber + "'";
        Object result = getCurrentSession().createSQLQuery(query).setMaxResults(1).uniqueResult();
        String acctName;
        if (result == null) {
            acctName = "";
        } else {
            acctName = result.toString();
        }
        return acctName;
    }

    public List<TradingPartnerModel> getClientResults(String input, String value, boolean isConsignee, String state, String zipCode, String salesCode,boolean importFlag) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.name as name,");
        queryBuilder.append("tp.number as number,");
        queryBuilder.append("tp.type as type,");
        queryBuilder.append("tp.disabled as disabled,");
        if (CommonUtils.isNotEmpty(state) || CommonUtils.isNotEmpty(zipCode)) {
            queryBuilder.append("tp.address as address,");
        } else {
            queryBuilder.append("cast(if(ca.address1 != '',concat(ca.address1,");
            queryBuilder.append("if(un.un_loc_name != '',concat(', ',un.un_loc_name),''),");
            queryBuilder.append("if(ca.state != '',concat(', ',ca.state),''),");
            if (isConsignee) {
                queryBuilder.append("if(gen.codedesc != '',concat(', ',gen.codedesc),''),");
            }
            queryBuilder.append("if(ca.zip != '',concat(', ',ca.zip),'')),'') as char character set latin1) as address,");
        }
        if (CommonUtils.isNotEmpty(salesCode)) {
            queryBuilder.append("tp.salesCode as salesCode");
        } else {
            queryBuilder.append("if(ge.code != '', ge.code,'') as salesCode");
        }
        if (CommonUtils.isEqualIgnoreCase(input, "Contact") || CommonUtils.isEqualIgnoreCase(input, "Email")) {
            queryBuilder.append(",tp.contact as contact,");
            queryBuilder.append("tp.email as email");
        }
        queryBuilder.append(",tp.eci_acct_no as bs_ship_fwd_no, tp.ecifwno as bs_cons_no ");
        queryBuilder.append(" from (");
        queryBuilder.append("select tp.acct_name as name,tp.search_acct_name,");
        queryBuilder.append("tp.acct_no as number,");
        queryBuilder.append("concat(tp.acct_type,");
        queryBuilder.append("if(tp.acct_type like '%V%' and tp.sub_type != '',concat('-(',tp.sub_type,");
        queryBuilder.append("if(tp.sub_type = 'Steamship Line' and tp.ssline_number != '',concat(' : ',tp.ssline_number),''),')'),'')) as type,");
        queryBuilder.append("if(tp.disabled = 'Y','true','false') as disabled,tp.sub_type,tp.eci_acct_no,tp.ecifwno,tp.ssline_number ");
        if (CommonUtils.isNotEmpty(state) || CommonUtils.isNotEmpty(zipCode)) {
            queryBuilder.append(",cast(if(ca.address1 != '',concat(ca.address1,");
            queryBuilder.append("if(un.un_loc_name != '',concat(', ',un.un_loc_name),''),");
            queryBuilder.append("if(ca.state != '',concat(', ',ca.state),''),");
            if (isConsignee) {
                queryBuilder.append("if(gen.codedesc != '',concat(', ',gen.codedesc),''),");
            }
            queryBuilder.append("if(ca.zip != '',concat(', ',ca.zip),'')),'') as char character set latin1) as address");
        }
        if (CommonUtils.isNotEmpty(salesCode)) {
            queryBuilder.append(",if(ge.code != '', ge.code,'') as salesCode");
        }
        if (CommonUtils.isEqualIgnoreCase(input, "Contact") || CommonUtils.isEqualIgnoreCase(input, "Email")) {
            queryBuilder.append(",concat(cc.first_name,' ',cc.last_name) as contact,");
            queryBuilder.append("if(cc.email != '', cc.email,'') as email");
        }
        queryBuilder.append(" from trading_partner tp");
        if (CommonUtils.isNotEmpty(state) || CommonUtils.isNotEmpty(zipCode)) {
            queryBuilder.append(" join cust_address ca");
            queryBuilder.append(" on (tp.acct_no = ca.acct_no");
            queryBuilder.append(" and ca.prime = 'on'");
            if (CommonUtils.isNotEmpty(state)) {
                queryBuilder.append(" and ca.state = '").append(state).append("'");
            }
            if (CommonUtils.isNotEmpty(zipCode)) {
                queryBuilder.append(" and ca.zip = '").append(zipCode).append("'");
            }
            queryBuilder.append(")");
            queryBuilder.append(" join un_location un");
            queryBuilder.append(" on (ca.city = un.id)");
            queryBuilder.append(" join genericcode_dup gen");
            queryBuilder.append(" on gen.id = ca.country");
        }
        if (CommonUtils.isNotEmpty(salesCode)) {
            queryBuilder.append(" join cust_general_info cgi");
            queryBuilder.append(" on (tp.acct_no = cgi.acct_no)");
            queryBuilder.append(" join genericcode_dup ge");
            queryBuilder.append(" on (cgi.sales_code = ge.id");
            queryBuilder.append(" and ge.code = '").append(salesCode).append("')");
        }
        if (CommonUtils.isEqualIgnoreCase(input, "Contact") || CommonUtils.isEqualIgnoreCase(input, "Email")) {
            queryBuilder.append(" join cust_contact cc");
            queryBuilder.append(" on (tp.acct_no = cc.acct_no");
            if (CommonUtils.isEqualIgnoreCase(input, "Contact")) {
                queryBuilder.append(" and replace(concat(cc.first_name,cc.last_name),' ','') like '").append(value.replace(" ", "")).append("%'");
            } else {
                queryBuilder.append(" and cc.email like '").append(value).append("%'");
            }
            queryBuilder.append(")");
        }
        if (CommonUtils.isEqualIgnoreCase(input, "Client")) {
            value = value.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
            queryBuilder.append(" where (tp.search_acct_name like '").append(value).append("%'");
            queryBuilder.append(" or tp.acct_no like '").append(value).append("%'");
            queryBuilder.append(" or tp.eci_acct_no like '").append(value).append("%'");
            queryBuilder.append(" or tp.ecifwno like '").append(value).append("%'");
            queryBuilder.append(" or tp.ecivendno like '").append(value).append("%')");
            queryBuilder.append(" and tp.type <> 'master'");
        } else {
            queryBuilder.append(" where tp.search_acct_name != ''");
        }
        if (isConsignee && !importFlag) {
            queryBuilder.append(" and (tp.acct_type LIKE '%S%' OR (tp.acct_type LIKE '%V%' AND tp.sub_type='Forwarder')"
                    + "OR tp.acct_type LIKE '%E%' OR tp.acct_type LIKE '%I%' OR tp.acct_type = 'C')");
        } else if (isConsignee && importFlag) {
            queryBuilder.append(" and tp.acct_type = 'C'");
        } else {
            queryBuilder.append(" and tp.acct_type != 'C'");
        }
        queryBuilder.append(" group by tp.acct_no");
        queryBuilder.append(" order by tp.search_acct_name,tp.acct_no");
        queryBuilder.append(" limit 50");
        queryBuilder.append(") as tp");
        if (CommonUtils.isEmpty(state) && CommonUtils.isEmpty(zipCode)) {
            queryBuilder.append(" left join cust_address ca");
            queryBuilder.append(" on (tp.number = ca.acct_no");
            queryBuilder.append(" and ca.prime = 'on')");
            queryBuilder.append(" left join un_location un");
            queryBuilder.append(" on (ca.city = un.id)");
        }
        if (CommonUtils.isEmpty(salesCode)) {
            queryBuilder.append(" left join cust_general_info cgi");
            queryBuilder.append(" on (tp.number = cgi.acct_no)");
            queryBuilder.append(" left join genericcode_dup ge");
            queryBuilder.append(" on (cgi.sales_code = ge.id)");
        }
        if (CommonUtils.isEmpty(state) && CommonUtils.isEmpty(zipCode) && isConsignee) {
            queryBuilder.append(" left join genericcode_dup gen");
            queryBuilder.append(" on gen.id = ca.country");
        }
        queryBuilder.append(" group by tp.number");
        queryBuilder.append(" order by tp.search_acct_name,tp.number");
        queryBuilder.append(" limit 50");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(TradingPartnerModel.class));
        return query.list();
    }

    public String getAccountingEmail(String accountNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ca.acct_rec_email as accounting_email");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" join cust_accounting ca on tp.acct_no = ca.acct_no and ca.acct_rec_email != ''");
        queryBuilder.append(" where tp.acct_no = '").append(accountNo).append("'");
        queryBuilder.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getAccountingFax(String accountNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ca.ar_fax as fax");
        queryBuilder.append(" from trading_partner tp");
        queryBuilder.append(" join cust_accounting ca on tp.acct_no = ca.acct_no and ca.ar_fax != ''");
        queryBuilder.append(" where tp.acct_no = '").append(accountNo).append("'");
        queryBuilder.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    public String getExemptInactivateAccounts() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select concat(\"'\",cust_accno,\"'\")");
        queryBuilder.append(" from vendor_info");
        queryBuilder.append(" where exempt_inactivate=true");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
    }

    public CustomerAddress getDefaultAddress(String accountNo) {
        Criteria criteria = getSession().createCriteria(CustomerAddress.class);
        criteria.add(Restrictions.eq("accountNo", accountNo));
        criteria.addOrder(Order.desc("primary"));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (CustomerAddress) criteria.uniqueResult();
    }

    public void updateTable(String tableName, String updateColumnName, String updateColumnValue, String whereColumnName, String whereColumnValue) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update ").append(tableName);
        queryBuilder.append(" set ").append(updateColumnName).append(" = '").append(updateColumnValue.replace("'", "\\'")).append("'");
        queryBuilder.append(" where ").append(whereColumnName).append(" = '").append(whereColumnValue).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateTradingPartnerName(String accountNo, String accountName) throws Exception {
        //update for transaction
        updateTable("transaction", "cust_name", accountName, "cust_no", accountNo);
        //update for transaction_ledger
        updateTable("transaction_ledger", "cust_name", accountName, "cust_no", accountNo);

        //update for quotation
        updateTable("quotation", "clientname", accountName, "clientnumber", accountNo);
        updateTable("quotation", "sslname", accountName, "ssline", accountNo);
        updateTable("quotation", "agent", accountName, "agent_no", accountNo);
        //update for booking_fcl
        updateTable("booking_fcl", "shipper", accountName, "shipno", accountNo);
        updateTable("booking_fcl", "forward", accountName, "forwardnumber", accountNo);
        updateTable("booking_fcl", "consignee", accountName, "consigneenumber", accountNo);
        updateTable("booking_fcl", "sslname", accountName, "ssline", accountNo);
        updateTable("booking_fcl", "agent", accountName, "agent_no", accountNo);
        //update for fcl_bl
        updateTable("fcl_bl", "agent", accountName, "agent_no", accountNo);
        updateTable("fcl_bl", "shipper_name", accountName, "shipper_no", accountNo);
        updateTable("fcl_bl", "house_shipper_name", accountName, "house_shipper_no", accountNo);
        updateTable("fcl_bl", "consignee_name", accountName, "consignee_no", accountNo);
        updateTable("fcl_bl", "house_consignee_name", accountName, "houseconsignee", accountNo);
        updateTable("fcl_bl", "notify_party_name", accountName, "notify_party", accountNo);
        updateTable("fcl_bl", "house_notify_party_name", accountName, "house_notify_party_no", accountNo);
        updateTable("fcl_bl", "forwarding_agent_name", accountName, "forward_agent_no", accountNo);
        updateTable("fcl_bl", "ssline_name", accountName, "ssline_no", accountNo);
        updateTable("fcl_bl", "third_party_name", accountName, "billtrdprty", accountNo);
        //update for fcl_bl_costcodes
        updateTable("fcl_bl_costcodes", "account_name", accountName, "account_no", accountNo);
        //update for ap_invoice
        updateTable("ap_invoice", "customer_name", accountName, "account_number", accountNo);
        //update for ar_red_invoice
        updateTable("ar_red_invoice", "customer_name", accountName, "customer_number", accountNo);
        //update for bookingfcl_units
        updateTable("bookingfcl_units", "account_name", accountName, "account_no", accountNo);
        //update for charges
        updateTable("charges", "account_name", accountName, "account_no", accountNo);
        //update for credit_debit_note
        updateTable("credit_debit_note", "customer_name", accountName, "customer_number", accountNo);
        //update for cust_address
        updateTable("cust_address", "acct_name", accountName, "acct_no", accountNo);
        //update for cust_general_default
        updateTable("cust_general_default", "shipper_name", accountName, "shipper_no", accountNo);
        updateTable("cust_general_default", "consignee_name", accountName, "consignee_no", accountNo);
        updateTable("cust_general_default", "forwarder_name", accountName, "forwarder_no", accountNo);
        //update for customer_default_charges
        updateTable("customer_default_charges", "vendor_name", accountName, "vendor_number", accountNo);
        //update for fclblcorrections
        updateTable("fclblcorrections", "account_name", accountName, "account_number", accountNo);
        //update for sed_filings
        updateTable("sed_filings", "expnam", accountName, "expnum", accountNo);
        updateTable("sed_filings", "connam", accountName, "connum", accountNo);
    }

    // get trading partner based on scac in carriers_or_line table
    public TradingPartner getTpBySsl(String scac) throws Exception {
        Query query = getSession().createQuery("FROM TradingPartner tp WHERE tp.sslineNumber IN (SELECT c.carriercode FROM CarriersOrLineTemp c WHERE c.SCAC = :scac)").setString("scac", scac).setMaxResults(1);
        return (TradingPartner) query.uniqueResult();
    }

    public Integer getE1E3F1F3CountByAcctNo(String accountNo) throws Exception {
        BigInteger count = new BigInteger("0");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM  trading_partner tp JOIN cust_contact cc ON tp.acct_no = cc.acct_no JOIN ");
        queryBuilder.append("genericcode_dup gp ON gp.id = cc.code_j WHERE tp.acct_no = '");
        queryBuilder.append(accountNo);
        queryBuilder.append("' AND (gp.code = '");
        queryBuilder.append(TradingPartnerConstants.E1);
        queryBuilder.append("' OR gp.code = '");
        queryBuilder.append(TradingPartnerConstants.E3);
        queryBuilder.append("' OR gp.code = '");
        queryBuilder.append(TradingPartnerConstants.F1);
        queryBuilder.append("' OR gp.code = '");
        queryBuilder.append(TradingPartnerConstants.F3);
        queryBuilder.append("')");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        Object o = queryObject.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public TradingPartner getTradingPartner(String accountno) {
        PersistentClass persistentClass = HibernateSessionFactory.getConfiguration().getClassMapping(TradingPartner.class.getName());
        Iterator<Property> properties = persistentClass.getPropertyIterator();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append(" t.acct_no as accountno,");
        while (properties.hasNext()) {
            Property property = properties.next();
            if (CommonUtils.in(property.getType().getName(), "string", "integer", "date", "double", "timestamp")) {
                queryBuilder.append(" t.").append(((Column) property.getColumnIterator().next()).getName()).append(" as ").append(property.getName()).append(",");
            }
        }
        queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());
        queryBuilder.append(" ");
        queryBuilder.append("from");
        queryBuilder.append(" trading_partner t ");
        queryBuilder.append("where");
        queryBuilder.append(" t.acct_no=:accountno");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("accountno", accountno);
        query.addScalar("accountno", StringType.INSTANCE);
        properties = persistentClass.getPropertyIterator();
        while (properties.hasNext()) {
            Property property = properties.next();
            if (CommonUtils.in(property.getType().getName(), "string", "integer", "date", "double", "timestamp")) {
                query.addScalar(property.getName(), property.getType());
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(TradingPartner.class));
        query.setMaxResults(1);
        return (TradingPartner) query.uniqueResult();
    }

    public boolean isImportCreadit(String acctNo) throws Exception {
        String q = "select count(*) from cust_accounting where acct_no='" + acctNo + "' and import_credit='Y'";
        String result = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
        return !result.equals("0");
    }

    public boolean checkApHistory(String accountNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select count(*) from transaction where transaction_type = 'AP'");
        queryBuilder.append(" and cust_no = '").append(accountNo).append("'");
        String count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult().toString();
        return !count.equals("0");
    }

    public String checkDuplicateAccountNumbers(String acctNo, String eciAcctNo, String eciFwNo, String eciVendorNo, String ssLineNo) {
        StringBuilder qb = new StringBuilder();
        boolean flag = false;
        if (CommonUtils.isAtLeastOneNotEmpty(eciAcctNo, eciFwNo, eciVendorNo, ssLineNo)) {
            qb.append("SELECT GROUP_CONCAT(result SEPARATOR '\\n') AS result FROM (");
            if (CommonUtils.isNotEmpty(eciAcctNo)) {
                qb.append(" (SELECT IF(COUNT(*) > 0, CONCAT('ECI Shpr/FF# - ").append(eciAcctNo).append(" is already assigned to ', acct_no), NULL) AS result ");
                qb.append(" from trading_partner where eci_acct_no = '").append(eciAcctNo.trim()).append("' and (disabled != 'Y' or disabled is NULL) and acct_no <> '").append(acctNo).append("')");
                flag = true;
            }
            if (CommonUtils.isNotEmpty(eciFwNo)) {
                if (flag) {
                    qb.append(" UNION");
                }
                qb.append(" (SELECT IF(COUNT(*) > 0, CONCAT('ECI Consignee - ").append(eciFwNo).append(" is already assigned to ', acct_no), NULL) AS result ");
                qb.append(" FROM trading_partner WHERE ECIFWNO = '").append(eciFwNo.trim()).append("' and (disabled != 'Y' or disabled is NULL) and acct_no <> '").append(acctNo).append("')");
                flag = true;
            }
            if (CommonUtils.isNotEmpty(eciVendorNo)) {
                if (flag) {
                    qb.append(" UNION");
                }
                qb.append(" (SELECT IF(COUNT(*) > 0, CONCAT('ECI Vendor - ").append(eciVendorNo).append(" is already assigned to ', acct_no), NULL) AS result ");
                qb.append(" FROM trading_partner WHERE ECIVENDNO = '").append(eciVendorNo.trim()).append("' and (disabled != 'Y' or disabled is NULL) and acct_no <> '").append(acctNo).append("')");
                flag = true;
            }
            if (CommonUtils.isNotEmpty(ssLineNo) && !ssLineNo.equals("00000")) {
                if (flag) {
                    qb.append(" UNION");
                }
                qb.append(" (SELECT IF(COUNT(*) > 0, CONCAT('SSLine Number - ").append(ssLineNo).append(" is already assigned to ', acct_no), NULL) AS result ");
                qb.append(" FROM trading_partner WHERE ssline_number = '").append(ssLineNo.trim()).append("' and (disabled != 'Y' or disabled is NULL) AND acct_no <> '").append(acctNo).append("')");
            }
            qb.append(") AS t");
            SQLQuery query = getCurrentSession().createSQLQuery(qb.toString());
            query.addScalar("result", StringType.INSTANCE);
            return (String) query.uniqueResult();
        }
        return null;
    }

    public String checkDuplicateUserName(String acctNo, String userName, String consUserName) {
        StringBuilder qb = new StringBuilder();
        boolean flag = false;
        if (CommonUtils.isAtLeastOneNotEmpty(userName, consUserName)) {
            qb.append("select group_concat(result separator '\\n') as result from (");
            if (CommonUtils.isNotEmpty(userName)) {
                qb.append(" (select if(count(*) > 0, concat('Shpr/FF# Username - ").append(userName).append(" is already assigned to ', cgi.acct_no), null) as result ");
                qb.append(" from cust_general_info cgi join trading_partner tp on tp.acct_no = cgi.acct_no ");
                qb.append(" where (cgi.user_name = '").append(userName.trim()).append("'");
                qb.append(" or cgi.cons_user_name = '").append(userName.trim()).append("')");
                qb.append(" and (tp.disabled != 'Y' or tp.disabled is null) and cgi.acct_no <> '").append(acctNo).append("')");
                flag = true;
            }
            if (CommonUtils.isNotEmpty(consUserName)) {
                if (flag) {
                    qb.append(" UNION");
                }
                qb.append(" (select if(count(*) > 0, concat('Cnee Username - ").append(consUserName).append(" is already assigned to ', cgi.acct_no), null) as result ");
                qb.append(" from cust_general_info cgi join trading_partner tp on tp.acct_no = cgi.acct_no ");
                qb.append(" where (cgi.cons_user_name = '").append(consUserName.trim()).append("'");
                qb.append(" or cgi.user_name = '").append(consUserName.trim()).append("')");
                qb.append(" and (tp.disabled != 'Y' or tp.disabled is null) and cgi.acct_no <> '").append(acctNo).append("')");
            }
            qb.append(") AS t");
            SQLQuery query = getCurrentSession().createSQLQuery(qb.toString());
            query.addScalar("result", StringType.INSTANCE);
            return (String) query.uniqueResult();
        }
        return null;
    }

    public String checkDuplicateFirmsCode(String acctNo, String firmsCode) {
        if (CommonUtils.isNotEmpty(firmsCode)) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" select if(count(*) > 0, concat('account ',tp.acct_no,' already has this firms code ',tp.firms_code), null) as result ");
            queryBuilder.append(" from trading_partner tp ");
            queryBuilder.append(" where tp.firms_code = '").append(firmsCode.trim()).append("'");
            queryBuilder.append(" and (tp.disabled != 'Y' or tp.disabled is null) and tp.acct_no <> '").append(acctNo).append("'");
            SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
            return (String) query.uniqueResult();
        }
        return null;
    }

    public List<TradingPartner> getSubsidiaryAccounts(String master) {
        PersistentClass persistentClass = HibernateSessionFactory.getConfiguration().getClassMapping(TradingPartner.class.getName());
        Iterator<Property> properties = persistentClass.getPropertyIterator();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append(" t.acct_no as accountno,");
        while (properties.hasNext()) {
            Property property = properties.next();
            if (CommonUtils.in(property.getType().getName(), "string", "integer", "date", "double", "timestamp")) {
                queryBuilder.append(" t.").append(((Column) property.getColumnIterator().next()).getName()).append(" as ").append(property.getName()).append(",");
            }
        }
        queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());
        queryBuilder.append(" ");
        queryBuilder.append("from");
        queryBuilder.append(" trading_partner t ");
        queryBuilder.append("where");
        queryBuilder.append(" t.masteracct_no = :master");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("master", master);
        query.addScalar("accountno", StringType.INSTANCE);
        properties = persistentClass.getPropertyIterator();
        while (properties.hasNext()) {
            Property property = properties.next();
            if (CommonUtils.in(property.getType().getName(), "string", "integer", "date", "double", "timestamp")) {
                query.addScalar(property.getName(), property.getType());
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(TradingPartner.class));
        return query.list();
    }

    public void addSubsidiaryAccount(String accountno, String master) {
        String queryString = "update trading_partner set masteracct_no = :master where acct_no = :accountno";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("master", master);
        query.setString("accountno", accountno);
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public void saveMasterAccountNo(String accountNo, String masteraccNo) {
        String queryString = "update trading_partner set masteracct_no = :masteraccNo where acct_no = :accountNo";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("masteraccNo", masteraccNo);
        query.setString("accountNo", accountNo);
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public void saveImportCredit(String accNo, String importcreditvalue,HttpServletRequest request) {
        String importcreditvalueA = "";

        if (importcreditvalue.equalsIgnoreCase("false")) {
            importcreditvalueA = ("N");
        } else{
            importcreditvalueA = ("Y");
    }
        User loginuser = (User) request.getSession().getAttribute("loginuser");
        String queryString = "update cust_accounting set import_credit = :importcreditvalueA where acct_no = :accNo";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("importcreditvalueA", importcreditvalueA);
        query.setString("accNo", accNo);
        query.executeUpdate();
        savetoNotes(importcreditvalueA,loginuser,accNo);
        getCurrentSession().flush();
    }

    public void savetoNotes( String importcreditvalue,User loginuser,String accNo){
        try {
           
            String impvalueForm = null;
            AuditNotesUtils.insertAuditNotes(impvalueForm, importcreditvalue, NotesConstants.ARCONFIGURATION, accNo, "ImportCredit", loginuser);
            AuditNotesUtils.insertAuditNotes(impvalueForm, importcreditvalue, NotesConstants.TRADINGPARTNER, accNo, "ImportCredit", loginuser);
        } catch (Exception ex) {
            Logger.getLogger(TradingPartnerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getImportcreditValue(String acctNo){
    String q = "select import_credit from cust_accounting where acct_no='" + acctNo + "'";
    String result = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
    return null != result ? result : "";
 }
      public String getImportcreditValueForNotes(String acctNo){
    String q = "select import_credit from cust_accounting where acct_no='" + acctNo + "'";
    String result = getCurrentSession().createSQLQuery(q).uniqueResult().toString();
    return null != result ? result : "";
 }
    public void removeSubsidiaryAccount(String accountno) {
        String queryString = "update trading_partner set masteracct_no = null where acct_no = :accountno";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("accountno", accountno);
        query.executeUpdate();
        getCurrentSession().flush();
    }

    public String validateDisabling(String acctNo, String forwardAcctNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(t.`vendor` <> '' or t.`not_identical` <> '', concat('<div>Cannot disable the account due to following reasons.<ul>', if(t.`vendor` <> '', t.`vendor`, ''), if(t.`not_identical` <> '', concat('<li>Not Identical - ', t.`not_identical`, '.</li>'), ''), '</ul></div>'), '') as result ");
        queryBuilder.append("from ");
        queryBuilder.append("  (select ");
        queryBuilder.append("    if(fgi.`vendor` <> dgi.`vendor` and dgi.`vendor` <> '', concat('<li>Account being disabled is ', dgi.`vendor`, ' but forward to account is not.</li>'), '') as vendor,");
        queryBuilder.append("    concat_ws(', ',");
        queryBuilder.append("      if(fgi.`ship_ff_coload_commodity` <> dgi.`ship_ff_coload_commodity`, 'Ship/FF Coload Commodity', null),");
        queryBuilder.append("      if(fgi.`ship_ff_retail_commodity` <> dgi.`ship_ff_retail_commodity`, 'Ship/FF Retail Commodity', null),");
        queryBuilder.append("      if(fgi.`ship_ff_fcl_commodity` <> dgi.`ship_ff_fcl_commodity`, 'Ship/FF Fcl Commodity', null),");
        queryBuilder.append("      if(fgi.`cons_coload_commodity` <> dgi.`cons_coload_commodity`, 'Consignee Coload Commodity', null),");
        queryBuilder.append("      if(fgi.`cons_retail_commodity` <> dgi.`cons_retail_commodity`, 'Cosignee Retail Commodity', null),");
        queryBuilder.append("      if(fgi.`cons_fcl_commodity` <> dgi.`cons_fcl_commodity`, 'Cosignee Fcl Commodity', null),");
        queryBuilder.append("      if(fgi.`origin_agent_import_commodity` <> dgi.`origin_agent_import_commodity`, 'Origin Agent Import Commodity', null),");
        queryBuilder.append("      if(fgi.`ship_ff_sales_code` <> dgi.`ship_ff_sales_code`, 'Ship/FF Sales Code', null),");
        queryBuilder.append("      if(fgi.`cons_sales_code` <> dgi.`cons_sales_code`, 'Consignee Sales Code', null),");
        queryBuilder.append("      if(fgi.`ship_ff_user_name` <> dgi.`ship_ff_user_name` and dgi.`ship_ff_user_name` <> '', 'Ship/FF Username', null),");
        queryBuilder.append("      if(fgi.`cons_user_name` <> dgi.`cons_user_name` and dgi.`cons_user_name` <> '', 'Consignee Username', null)");
        queryBuilder.append("    ) as not_identical");
        queryBuilder.append("  from ");
        queryBuilder.append("    (select ");
        queryBuilder.append("      if(tp.`acct_type` like '%V%', if(tp.`sub_type` = 'Forwarder', 'Forwarder', 'Vendor'), '') as vendor,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`commodity_no`) as ship_ff_coload_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`retail_commodity`) as ship_ff_retail_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`fcl_commodity`) as ship_ff_fcl_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`cons_coload_commodity`) as cons_coload_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`cons_retail_commodity`) as cons_retail_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`cons_fcl_commodity`) as cons_fcl_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`imp_comm_no`) as origin_agent_import_commodity,");
        queryBuilder.append("      (select if(sc.`code` <> '', sc.`code`, null) from `genericcode_dup` sc where sc.`id` = gi.`sales_code`) as ship_ff_sales_code,");
        queryBuilder.append("      (select if(sc.`code` <> '', sc.`code`, null) from `genericcode_dup` sc where sc.`id` = gi.`cons_sales_code`) as cons_sales_code,");
        queryBuilder.append("      if(gi.`user_name` <> '', gi.`user_name`, null) as ship_ff_user_name,");
        queryBuilder.append("      if(gi.`cons_user_name` <> '', gi.`cons_user_name`, null) as cons_user_name");
        queryBuilder.append("    from");
        queryBuilder.append("      `trading_partner` tp,");
        queryBuilder.append("      `cust_general_info` gi   ");
        queryBuilder.append("    where tp.`acct_no` = '").append(forwardAcctNo).append("' ");
        queryBuilder.append("      and tp.`acct_no` = gi.`acct_no`) as fgi,");
        queryBuilder.append("    (select ");
        queryBuilder.append("      if(tp.`acct_type` like '%V%', if(tp.`sub_type` = 'Forwarder', 'Forwarder', 'Vendor'), '') as vendor,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`commodity_no`) as ship_ff_coload_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`retail_commodity`) as ship_ff_retail_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`fcl_commodity`) as ship_ff_fcl_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`cons_coload_commodity`) as cons_coload_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`cons_retail_commodity`) as cons_retail_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`cons_fcl_commodity`) as cons_fcl_commodity,");
        queryBuilder.append("      (select if(co.`code` <> '000000' and co.`code` <> '', co.`code`, null) from `genericcode_dup` co where co.`id` = gi.`imp_comm_no`) as origin_agent_import_commodity,");
        queryBuilder.append("      (select if(sc.`code` <> '', sc.`code`, null) from `genericcode_dup` sc where sc.`id` = gi.`sales_code`) as ship_ff_sales_code,");
        queryBuilder.append("      (select if(sc.`code` <> '', sc.`code`, null) from `genericcode_dup` sc where sc.`id` = gi.`cons_sales_code`) as cons_sales_code,");
        queryBuilder.append("      if(gi.`user_name` <> '', gi.`user_name`, null) as ship_ff_user_name,");
        queryBuilder.append("      if(gi.`cons_user_name` <> '', gi.`cons_user_name`, null) as cons_user_name");
        queryBuilder.append("    from");
        queryBuilder.append("      `trading_partner` tp,");
        queryBuilder.append("      `cust_general_info` gi   ");
        queryBuilder.append("    where tp.`acct_no` = '").append(acctNo).append("' ");
        queryBuilder.append("      and tp.`acct_no` = gi.`acct_no`) as dgi) as t");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", StringType.INSTANCE);
        String result = (String) query.uniqueResult();
        if (CommonUtils.isEmpty(result)) {
            queryBuilder.delete(0, queryBuilder.length());
            queryBuilder.append("select ");
            queryBuilder.append("  if(");
            queryBuilder.append("    result <> '',");
            queryBuilder.append("    concat(");
            queryBuilder.append("      '<div>This account has<ul>',");
            queryBuilder.append("      result,");
            queryBuilder.append("      '</ul>Do you still want to continue?</div>'");
            queryBuilder.append("    ),");
            queryBuilder.append("    null");
            queryBuilder.append("  ) as result ");
            queryBuilder.append("from");
            queryBuilder.append("  (select ");
            queryBuilder.append("    group_concat(result separator '') as result ");
            queryBuilder.append("  from");
            queryBuilder.append("    (");
            queryBuilder.append("      (select ");
            queryBuilder.append("        if(");
            queryBuilder.append("          count(*) > 0,");
            queryBuilder.append("          '<li>AP history. Disabling this account increases the risk of double paying invoices.</li>',");
            queryBuilder.append("          null");
            queryBuilder.append("        ) as result ");
            queryBuilder.append("      from");
            queryBuilder.append("        transaction ");
            queryBuilder.append("      where cust_no = '").append(acctNo).append("' ");
            queryBuilder.append("        and transaction_type = 'AP') ");
            queryBuilder.append("      union");
            queryBuilder.append("      (select ");
            queryBuilder.append("        if(");
            queryBuilder.append("          count(*) > 0,");
            queryBuilder.append("          concat(");
            queryBuilder.append("            '<li>',");
            queryBuilder.append("            count(*),");
            queryBuilder.append("            ' AR Invoices still open.</li>'");
            queryBuilder.append("          ),");
            queryBuilder.append("          null");
            queryBuilder.append("        ) as result ");
            queryBuilder.append("      from");
            queryBuilder.append("        transaction ar ");
            queryBuilder.append("      where ar.cust_no = '").append(acctNo).append("' ");
            queryBuilder.append("        and ar.balance <> 0.00 ");
            queryBuilder.append("        and ar.transaction_type = 'AR') ");
            queryBuilder.append("      union");
            queryBuilder.append("      (select ");
            queryBuilder.append("        if(");
            queryBuilder.append("          count(*) > 0,");
            queryBuilder.append("          concat(");
            queryBuilder.append("            '<li>',");
            queryBuilder.append("            count(*),");
            queryBuilder.append("            ' AP Invoices still open.</li>'");
            queryBuilder.append("          ),");
            queryBuilder.append("          null");
            queryBuilder.append("        ) as result ");
            queryBuilder.append("      from");
            queryBuilder.append("        transaction ap ");
            queryBuilder.append("      where ap.cust_no = '").append(acctNo).append("' ");
            queryBuilder.append("        and ap.balance <> 0.00 ");
            queryBuilder.append("        and ap.transaction_type = 'AP' ");
            queryBuilder.append("        and ap.status = 'Open') ");
            queryBuilder.append("      union");
            queryBuilder.append("      (select ");
            queryBuilder.append("        if(");
            queryBuilder.append("          count(*) > 0,");
            queryBuilder.append("          concat(");
            queryBuilder.append("            '<li>',");
            queryBuilder.append("            count(*),");
            queryBuilder.append("            ' Accruals still open.</li>'");
            queryBuilder.append("          ),");
            queryBuilder.append("          null");
            queryBuilder.append("        ) as result ");
            queryBuilder.append("      from");
            queryBuilder.append("        transaction_ledger ac ");
            queryBuilder.append("      where ac.cust_no = '").append(acctNo).append("' ");
            queryBuilder.append("        and ac.balance <> 0.00 ");
            queryBuilder.append("        and ac.transaction_type = 'AC' ");
            queryBuilder.append("        and ac.status <> 'AS' ");
            queryBuilder.append("        and ac.status <> 'EDI Assigned')");
            queryBuilder.append("    ) as t) as t ");
            query = getCurrentSession().createSQLQuery(queryBuilder.toString());
            query.addScalar("result", StringType.INSTANCE);
            return (String) query.uniqueResult();
        } else {
            return result;
        }
    }

    public String getLogoStatus(String originAgentAcct) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  tp.`ecu_logo` ");
        sb.append("FROM");
        sb.append("  `trading_partner` tp ");
        sb.append("WHERE tp.`acct_no` = :originAgentAcct");
        sb.append(" LIMIT 1 ");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setString("originAgentAcct", originAgentAcct);
        return (String) queryObject.uniqueResult();
    }
    public String getTradingpatnerAccType(String accNo) throws Exception {
        String result = "";
        if (accNo != null && !accNo.equals("")) {
            String queryString = "SELECT acct_type FROM trading_partner WHERE acct_no = '" + accNo + "'";
            Query queryObject = getSession().createSQLQuery(queryString);
            result = null != queryObject.uniqueResult() ? (String) queryObject.uniqueResult().toString() : "";
        }
        return result;
    }
       
      public void saveEciConsigneeAccounting(String accountNo,String ecibluescrrenAct,HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser"); 
        String username = user.getLoginName();
        String queryString = "update trading_partner set ecifwno = :accountNo, update_by = :username where acct_no = :ecibluescrrenAct";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("accountNo", accountNo);
        query.setString("username", username);
        query.setString("ecibluescrrenAct", ecibluescrrenAct);
        query.executeUpdate(); 
//        String description = "ECI Consignee "+accountNo;
//        String moduleId = "TRADING_PARTNER";
//        AuditNotesUtils.insertAuditNotes(description, moduleId, ecibluescrrenAct, null, user);
        getCurrentSession().flush();
      }
       public void saveEciShipperAccounting(String accountNo,String ecibluescrrenAct,HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser"); 
        String username = user.getLoginName();
        String queryString = "update trading_partner set eci_acct_no = :accountNo,update_by = :username where acct_no = :ecibluescrrenAct";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("ecibluescrrenAct", ecibluescrrenAct);
        query.setString("accountNo", accountNo);
        query.setString("username", username);
        query.executeUpdate();
//        String description = "ECI SHPR/FF# "+accountNo;
//        String moduleId = "TRADING_PARTNER";
//        AuditNotesUtils.insertAuditNotes(description, moduleId, ecibluescrrenAct, null, user);
        getCurrentSession().flush();
      }
        public void saveEciVendorAccounting(String accountNo,String ecibluescrrenAct,HttpServletRequest request)throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        String username = user.getLoginName();
        String queryString = "update trading_partner set ecivendno = :accountNo,update_by = :username where acct_no = :ecibluescrrenAct";
        SQLQuery query = getCurrentSession().createSQLQuery(queryString);
        query.setString("ecibluescrrenAct", ecibluescrrenAct);
        query.setString("accountNo", accountNo);
        query.setString("username", username);
        query.executeUpdate();
//        String description = "ECI vendor "+accountNo;
//        String moduleId = "TRADING_PARTNER";
//        AuditNotesUtils.insertAuditNotes(description, moduleId, ecibluescrrenAct, null, user);
        getCurrentSession().flush();
      }
          public String brandFieldForClient(String acctNo){
          String queryString = "select brand_preference from trading_partner where acct_no='"+acctNo+"'";
          SQLQuery query = getSession().createSQLQuery(queryString);
          Object brandField = query.uniqueResult();
          return null != brandField ? brandField.toString() : "";
      }
    public String getBusinessUnit(String originAgentAcct) {
        String strQuery = "SELECT IF(brand_preference='Ecu Worldwide' OR brand_preference = 'none','ECU',IF(brand_preference='Econo','ECI',brand_preference)) AS brand FROM `trading_partner`  WHERE `acct_no`=:originAgentAcct LIMIT 1";
        Query queryObject = getCurrentSession().createSQLQuery(strQuery);
        queryObject.setParameter("originAgentAcct", originAgentAcct);
        return queryObject.uniqueResult().toString();
    }
      private StringBuilder getSortBy(TradingPartnerForm tradingPartnerForm) {
        StringBuilder queryBuilder = new StringBuilder();
        if (CommonUtils.isNotEmpty(tradingPartnerForm.getSortByValueForTp())) {
            if ("up".equals(tradingPartnerForm.getSearchTypeForTp())) {
                queryBuilder.append(" order by ").append(SortByEnumforTP.valueOf(tradingPartnerForm.getSortByValueForTp()).getField()).append(" asc");
            } else {
                queryBuilder.append(" order by ").append(SortByEnumforTP.valueOf(tradingPartnerForm.getSortByValueForTp()).getField()).append(" desc");
            }
        } else {
            queryBuilder.append("   order by t.accountNumber");
        }
        return queryBuilder;
    }
      public String getOriginAgentAcctNo(String unitSsId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  lusi.`origin_acct_no` AS originAcctNo ");
        sb.append(" FROM");
        sb.append("  `lcl_unit_ss` lus ");
        sb.append("  JOIN `lcl_unit_ss_imports` lusi ");
        sb.append("    ON (lus.`unit_id` = lusi.`unit_id`) ");
        sb.append(" WHERE lus.`id` = :unitssId ");
        sb.append(" LIMIT 1 ");
        Query queryObject =getCurrentSession().createSQLQuery(sb.toString());
        queryObject.setParameter("unitssId", unitSsId);
        return (String) queryObject.uniqueResult();
    }
      public String[] getTradingPartnerValues(String acctNo) throws Exception {
         String queryString = "SELECT t.tax_exempt,t.federal_id FROM trading_partner t WHERE t.acct_no= :acctNo";
         SQLQuery queryObject =getCurrentSession().createSQLQuery(queryString);
         queryObject.setParameter("acctNo", acctNo);
         List values = queryObject.list();
         String[] data = new String[2];
         if (values != null && values.size() > 0) {
            Object[] value = (Object[]) values.get(0);
            if (value[0] != null && !value[0].toString().trim().equals("")) {
                data[0] = value[0].toString();
            }
            if (value[1] != null && !value[1].toString().trim().equals("")) {
                data[1] = value[1].toString();
            }
        }
     return data;
      }
      public String getUnlocationCodeFromTradingPartner(String accountName, String city) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT u.un_loc_code FROM trading_partner tp ");
        queryBuilder.append(" JOIN un_location u ON (u.id = tp.customer_location) WHERE tp.acct_name =:accountName ");
        queryBuilder.append(" AND u.un_loc_name =:city ");
        queryBuilder.append(" limit 1");
        Query query = getSession().createSQLQuery(queryBuilder.toString());
        query.setString("accountName", accountName);
        query.setString("city", city);
        return query.uniqueResult() != null ? query.uniqueResult().toString() : "";
    }
}
