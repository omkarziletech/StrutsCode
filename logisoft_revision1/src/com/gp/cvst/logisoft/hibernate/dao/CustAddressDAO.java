package com.gp.cvst.logisoft.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.domain.CustAddress;
import org.hibernate.LockOptions;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class CustAddress.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.CustAddress
 * @author MyEclipse - Hibernate Tools
 *
 *
 *
 */
public class CustAddressDAO extends BaseHibernateDAO {

    SQLQuery creditQueryObject = null;
    SQLQuery insureQueryObject = null;
    List creditList = new ArrayList();
    List insureList = new ArrayList();
    Object[] creditObject = null;
    Object[] insureObject = null;

    public void save(CustAddress transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void save(CustomerAddress instance) throws Exception {
	getSession().save(instance);
	getSession().flush();
	getSession().clear();
    }

    public void update(CustomerAddress transientInstance) throws Exception {
	getSession().clear();
	getSession().update(transientInstance);
	getSession().flush();
    }

    public void delete(CustAddress persistentInstance) throws Exception {
	getSession().delete(persistentInstance);
	getSession().flush();
    }

    public void deleteByCustomerAddress(CustomerAddress persistentInstance) throws Exception {
	getSession().delete(persistentInstance);
	getSession().flush();
    }

    public CustAddress findById(java.lang.String id) throws Exception {
	CustAddress instance = (CustAddress) getSession().get(
		"com.gp.cvst.logisoft.domain.CustAddress", id);
	return instance;
    }

    public CustomerAddress findByID(java.lang.String id) throws Exception {
	int Id = Integer.parseInt(id);
	CustomerAddress instance = (CustomerAddress) getSession().get(
		"com.gp.cong.logisoft.domain.CustomerAddress", Id);
	return instance;
    }

    public List findByExample(CustAddress instance) throws Exception {
	List results = getSession().createCriteria(
		"com.gp.cvst.logisoft.domain.CustAddress").add(
		Example.create(instance)).list();
	return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
	String queryString = "from CustAddress as model where model." + propertyName + "= ?0";
	Query queryObject = getSession().createQuery(queryString);
	queryObject.setParameter("0", value);
	return queryObject.list();
    }

    public CustAddress merge(CustAddress detachedInstance) throws Exception {
	CustAddress result = (CustAddress) getSession().merge(
		detachedInstance);
	return result;
    }

    public void attachDirty(CustAddress instance) throws Exception {
	getSession().saveOrUpdate(instance);
	getSession().flush();
    }

    public void attachClean(CustAddress instance) throws Exception {
	getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public List findByAccountNo(String acctName, String acctNumber,
	    String accountType, String primeAddress) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	if (acctName != null && !acctName.equals("")) {
	    criteria.add(Restrictions.like("acctName", acctName + "%"));
	}
	if (acctNumber != null && !acctNumber.equals("")) {
	    criteria.add(Restrictions.eq("acctNo", acctNumber));
	}
	if (accountType != null && !accountType.equals("")) {
	    criteria.add(Restrictions.like("acctType", accountType + "%"));
	}
	criteria.setMaxResults(50);
	return criteria.list();
    }

    public CustAddress findByAccountNo(String acctNo) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	criteria.add(Restrictions.eq("acctNo", acctNo));
	return (CustAddress)criteria.setMaxResults(1).uniqueResult();
    }

    public CustAddress findByAccountNoAndPrime(String acctNo) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	criteria.add(Restrictions.eq("acctNo", acctNo));
	criteria.add(Restrictions.like("primeAddress", "on"));
	return (CustAddress)criteria.setMaxResults(1).uniqueResult();
    }

    public List findBy(String acctName, String acctNumber, String address1,
	    String accountType) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

	if (acctName != null && !acctName.equals("")) {
	    criteria.add(Restrictions.like("acctName", acctName.trim() + "%"));
	}
	if (acctNumber != null && !acctNumber.equals("")) {
	    criteria.add(Restrictions.like("acctNo", acctNumber.trim() + "%"));
	}
	if (address1 != null && !address1.equals("")) {
	    criteria.add(Restrictions.like("address1", address1.trim() + "%"));
	}
	if (accountType != null && !accountType.equals("")) {
	    criteria.add(Restrictions.like("acctType", accountType.trim() + "%"));
	}
	criteria.add(Restrictions.like("primeAddress", "on"));
	criteria.setMaxResults(50);
	return criteria.list();
    }
// new method created 

    public List findByCustomerAddressDomain(String acctName, String acctNumber, String address1,
	    String accountType) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustomerAddress.class);

	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

	if (acctName != null && !acctName.equals("")) {
	    Criterion accountName = Restrictions.like("acctname", acctName + "%");
	    Criterion accountNumber = Restrictions.like("accountNo", acctName + "%");
	    LogicalExpression orExp = Restrictions.or(accountName, accountNumber);
	    criteria.add(orExp);
	}
	if (acctNumber != null && !acctNumber.equals("")) {
	    criteria.add(Restrictions.like("accountNo", acctNumber + "%"));
	}
	if (address1 != null && !address1.equals("")) {
	    criteria.add(Restrictions.like("address1", address1 + "%"));
	}
	if (accountType != null && !accountType.equals("")) {
	    criteria.add(Restrictions.like("accounttype", accountType + "%"));
	}
	criteria.add(Restrictions.like("primary", "on"));
	criteria.setMaxResults(50);
	return criteria.list();
    }

    //for CustAddress domain class
    public List findByCustAddressDomain(String acctName, String acctNumber, String address1,
	    String accountType) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustAddress.class);

	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

	if (acctName != null && !acctName.equals("")) {
	    Criterion accountName = Restrictions.like("acctName", acctName + "%");
	    Criterion accountNumber = Restrictions.like("acctNo", acctName + "%");
	    LogicalExpression orExp = Restrictions.or(accountName, accountNumber);
	    criteria.add(orExp);
	}
	if (acctNumber != null && !acctNumber.equals("")) {
	    criteria.add(Restrictions.like("acctNo", acctNumber + "%"));
	}
	if (address1 != null && !address1.equals("")) {
	    criteria.add(Restrictions.like("address1", address1 + "%"));
	}
	if (accountType != null && !accountType.equals("")) {
	    criteria.add(Restrictions.like("acctType", accountType + "%"));
	}
	criteria.add(Restrictions.like("primeAddress", "on"));
	criteria.setMaxResults(50);
	return criteria.list();
    }

    public List findBy1(String acctName, String acctNumber, String address1,
	    String accountType) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	if (acctName != null && !acctName.equals("")) {
	    criteria.add(Restrictions.like("acctName", acctName + "%"));
	}
	if (acctNumber != null && !acctNumber.equals("")) {
	    criteria.add(Restrictions.like("acctNo", acctNumber + "%"));
	}
	if (address1 != null && !address1.equals("")) {
	    criteria.add(Restrictions.like("address1", address1 + "%"));
	}
	if (accountType != null && !accountType.equals("")) {
	    criteria.add(Restrictions.like("acctType", "%" + accountType + "%"));
	}
	criteria.add(Restrictions.like("primeAddress", "on"));
	criteria.setMaxResults(50);
	return criteria.list();
    }

    public List findBy3(String acctName, String acctNumber, String address1,
	    String accountType) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		TradingPartner.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

	if (acctName != null && !acctName.equals("")) {
	    criteria.add(Restrictions.like("accountName", acctName + "%"));
	}
	if (acctNumber != null && !acctNumber.equals("")) {
	    criteria.add(Restrictions.like("accountno", acctNumber + "%"));
	}
	if (accountType != null && !accountType.equals("")) {
	    criteria.add(Restrictions.like("acctType", "%" + accountType + "%"));
	}
	criteria.setMaxResults(50);
	return criteria.list();
    }

    public List findBy2(String acctName, String acctNumber, String address1,
	    String accountType) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	if (acctName != null && !acctName.equals("")) {
	    criteria.add(Restrictions.like("acctName", acctName + "%"));
	}
	if (acctNumber != null && !acctNumber.equals("")) {
	    criteria.add(Restrictions.like("acctNo", acctNumber + "%"));
	}
	if (address1 != null && !address1.equals("")) {
	    criteria.add(Restrictions.like("address1", address1 + "%"));
	}
	if (accountType != null && !accountType.equals("")) {
	    criteria.add(Restrictions.like("acctType", "%" + accountType + "%"));
	}
	criteria.setMaxResults(50);
	return criteria.list();
    }

    public List findAcctName(String accountNo) throws Exception {
	List searchResults = findByCustomerAddressDomain(null, accountNo, null, null);
	return searchResults;
    }

    public List getTansactionObj(String custId) throws Exception {
	List list = new ArrayList();
	String queryString = "select cust_no from transaction where cust_no='" + custId + "'";
	list = getCurrentSession().createQuery(queryString).list();
	return list;
    }

    public String getCustomerName(String custid) throws Exception {
	String result = "";
	String queryString = "select ca.acctName from CustAddress ca where ca.acctNo='" + custid + "'";
	Iterator it = getCurrentSession().createQuery(queryString).list().iterator();
	result = (String) it.next();
	return result;
    }

    public List getCustomerNamesList(String custName) throws Exception {
	List list = new ArrayList();
	String queryString = "select ca.acctName from CustAddress";
	Iterator it = getCurrentSession().createQuery(queryString).list().iterator();
	list.add(it.next());
	return list;
    }

    public List getCustomerNamesList1(String custName) throws Exception {
	String result = "";
	List list = new ArrayList();
	String queryString = "from CustAddress where acctNo='" + custName + "'";
	List customerList = getCurrentSession().createQuery(queryString).list();
	if (CommonUtils.isNotEmpty(customerList)) {
	    Iterator it = getCurrentSession().createQuery(queryString).list().iterator();
	    list.add(it.next());
	}
	return list;
    }

    public List getPrimaryCustomerAddress(String acctName) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

	if (acctName != null && !acctName.equals("")) {
	    criteria.add(Restrictions.like("acctName", acctName + "%"));
	}
	criteria.add(Restrictions.eq("primeAddress", "on"));
	criteria.setMaxResults(500);
	return criteria.list();
    }

    public List getCustomeress(String acctName) throws Exception {
	String queryString = " from CustAddress  where acctName='" + acctName + "'";
	List queryObject = getCurrentSession().createQuery(queryString).list();
	return queryObject;
    }

    public List findForCustomer(String sslId) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	if (sslId != null && !sslId.equals("")) {
	    criteria.add(Restrictions.like("acctNo", sslId + "%"));
	    criteria.addOrder(Order.asc("acctNo"));
	}
	return criteria.list();
    }

    public String parentacct(String parentNo) throws Exception {
	String result = "";
	String queryString = "select cx.acctName from CustAddress cx where cx.acctNo='" + parentNo + "'";
	Iterator itr = getCurrentSession().createQuery(queryString).list().iterator();
	if (itr.hasNext()) {
	    result = (String) itr.next();
	}
	return result;
    }

    public List master(String acctNumber) throws Exception {
	List result = new ArrayList();
	String QueryString = "";
	QueryString = "select ca.master,cb.creditLimit from CustAddress ca, CustomerAccounting cb where ca.acctNo=cb.accountNo and ca.acctNo='" + acctNumber + "'";
	List queryObject = getCurrentSession().createQuery(QueryString).list();
	Iterator itr = queryObject.iterator();

	if (itr.hasNext()) {
	    Object[] row = (Object[]) itr.next();
	    result.add((String) row[0]);
	    result.add((Integer) row[1]);
	}
	return result;
    }

    public String credit(String acctNo) throws Exception {
	String result = null;
	String queryString = " select c.hold from CustAddress c where c.acctNo='" + acctNo + "'";
	Iterator itr = getCurrentSession().createQuery(queryString).list().iterator();
	if (itr.hasNext()) {
	    result = (String) itr.next();
	}
	return result;
    }

    public CustomerAccounting creditLimit(String acctNo) throws Exception {
	String queryString = "from CustomerAccounting customerAccounting where customerAccounting.accountNo='" + acctNo + "'";
	return (CustomerAccounting)getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
    }

    public String getCustomerNumber(String custname) throws Exception {
	String queryString = "select ca.acctNo from CustAddress ca where (ca.acctName=?0 or ca.acctNo=?1)";
	Query query = getCurrentSession().createQuery(queryString);
	query.setParameter("0", custname);
	query.setParameter("1", custname);
	Object cusNo=query.setMaxResults(1).uniqueResult();
        return null!=cusNo?cusNo.toString():"";
    }

    public String getCustomerNumberByProperty(String Property, String value) throws Exception {
	String queryString = "select ca.acctName from CustAddress ca where ca." + Property + "='" + value + "'";
	Object result=getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
	return null!=result?result.toString():"";
    }

    public String getMasterAccountNo(String acctNumber) throws Exception {
	String queryString = "select tp.master from TradingPartner tp where tp.accountno='" + acctNumber + "'";
	return (String) getCurrentSession().createQuery(queryString).uniqueResult();
    }

    /**
     * Used in Account receivable Inquiry
     * @param acctNumber
     * @return
     */
    public String getMasterAcctNo(String acctNumber) throws Exception {
	String queryString = "select ca.master from CustAddress ca where (ca.acctNo='" + acctNumber + "' or ca.acctName='" + acctNumber + "')";
	Object result = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
	return null!=result?result.toString():"";
    }

    public List getchildAccountNo(String acctNumber) throws Exception {
	List result = null;
	String queryString = "from CustAddress ca where ca.master='" + acctNumber + "'";
	result = getCurrentSession().createQuery(queryString).list();
	return result;
    }

    public List getHold(String acctNumber) throws Exception {
	List result = new ArrayList();
	String queryString = "select ca.hold,ca.acctType from CustAddress ca where ca.acctNo='" + acctNumber + "'";
	result = getCurrentSession().createQuery(queryString).list();
	return result;

    }

    public List getVendorDetails(String acctNumber) throws Exception {
	List result = new ArrayList();
	String queryString = "select distinct address1,contact_name,phone,fax,email1 from cust_address where acct_no='" + acctNumber + "'";
	List queryObject = new ArrayList();
	queryObject = getCurrentSession().createSQLQuery(queryString).list();
	for (Iterator iterator = queryObject.iterator(); iterator.hasNext();) {
	    Object[] row = (Object[]) iterator.next();
	    CustAddress custaddress = new CustAddress();

	    if (row[0] != null) {
		custaddress.setAddress1((String) row[0]);
	    }
	    if (row[1] != null) {
		custaddress.setContactName((String) row[1]);
	    }
	    if (row[2] != null) {
		custaddress.setPhone((String) row[2]);
	    }
	    if (row[3] != null) {
		custaddress.setFax((String) row[3]);
	    }
	    if (row[4] != null) {
		custaddress.setEmail1((String) row[4]);
	    }
	    result.add(custaddress);
	}
	return result;
    }

    public List getCustomerForLookUp(String accountName, String accountType, String accountNo) throws Exception {
	List result = new ArrayList();
	String queryString = "select t.acct_name,t.acct_no,t.acct_type,c.contact_name,c.phone,c.fax,c.email1,c.address1,"
		+ "c.city1,c.zip,c.country,t.eci_acct_no,c.state,c.co_name from trading_partner t"
		+ " left join cust_address c on (c.acct_no=t.acct_no  and c.prime = 'on')"
		+ " where t.acct_name like \"" + accountName + "%\"";

	if (accountType != null && accountType.equals("V%SS")) {
	    String accountType1 = "V";
	    String accountType2 = "SS";
	    queryString = queryString + " and (t.acct_type like '%" + accountType1 + "%' or t.acct_type like '%" + accountType2 + "%')";
	} else if (accountType != null && !accountType.equals("")) {
	    queryString = queryString + " and t.acct_type like '%" + accountType + "%'";
	}
	if (accountNo != null && !accountNo.equals("")) {
	    queryString = queryString + "  and t.acct_no like '" + accountNo + "%'";
	}
	queryString += " order by t.acct_name";
	SQLQuery query = getCurrentSession().createSQLQuery(queryString);
	query.setMaxResults(50);
	List queryObject = new ArrayList();
	queryObject = query.list();
	for (Iterator iterator = queryObject.iterator(); iterator.hasNext();) {
	    Object[] row = (Object[]) iterator.next();
	    CustAddress custaddress = new CustAddress();
	    if (row[0] != null) {
		custaddress.setAcctName((String) row[0]);
	    }
	    if (row[1] != null) {
		custaddress.setAcctNo((String) row[1]);
		setCrOnholdInsure(custaddress);
	    }
	    if (row[2] != null) {
		custaddress.setAcctType((String) row[2]);
	    }
	    if (row[3] != null) {
		custaddress.setContactName((String) row[3]);
	    }
	    if (row[4] != null) {
		custaddress.setPhone((String) row[4]);
	    }
	    if (row[5] != null) {
		custaddress.setFax((String) row[5]);
	    }
	    if (row[6] != null) {
		custaddress.setEmail1((String) row[6]);
	    }
	    if (row[7] != null) {
		custaddress.setAddress1((String) row[7]);
	    }
	    if (row[8] != null) {
		custaddress.setCity1((String) row[8]);
	    }

	    if (row[9] != null) {
		custaddress.setZip((String) row[9]);
	    }
	    if (row[10] != null) {
		int id = (Integer) row[10];
		GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
		GenericCode genericCode = genericCodeDAO.findById(id);
		if (genericCode != null) {
		    custaddress.setCuntry(genericCode);
		}
	    }
	    if (row[11] != null) {
		custaddress.setEciAcctNo((String) row[11]);
	    }
	    if (row[12] != null) {
		custaddress.setState((String) row[12]);
	    }
	    if (row[13] != null) {
		custaddress.setCoName((String) row[13]);
	    }
	    result.add(custaddress);
	}
	return result;
    }

    /**
     * for customer address popup
     * @param custAddress
     */
    public void setCrOnholdInsure(CustAddress custAddress) throws Exception {
	//for CR and Hold in customer pop up
	String getCredit = "select g.credit_status,g.extend_credit from cust_accounting g where g.acct_no =\"" + custAddress.getAcctNo() + "\"";
	creditQueryObject = getCurrentSession().createSQLQuery(getCredit).addScalar("g.credit_status", StringType.INSTANCE).addScalar("g.extend_credit", StringType.INSTANCE);
	creditList = creditQueryObject.list();

	if (null != creditList && creditList.size() >= 1) {
	    creditObject = (Object[]) creditList.get(0);

	}
	if (null != creditObject) {
	    if (null != creditObject[0]) {
		String creditStatus = creditObject[0].toString();
		if (creditStatus != null && !creditStatus.equals("null")) {
		    custAddress.setOnHold("yes");
		} else {

		    custAddress.setOnHold("no");
		}
	    }
	    if (null != creditObject[1]) {
		String extendCredit = creditObject[1].toString();
		if (extendCredit != null && extendCredit.equals("Y")) {
		    custAddress.setCr("yes");
		} else {
		    custAddress.setCr("no");
		}
	    }
	}

	//for insure in customer address pop up
	String getInsure = "select h.insure from cust_general_info h where h.acct_no =\"" + custAddress.getAcctNo() + "\"";
	insureQueryObject = getCurrentSession().createSQLQuery(getInsure).addScalar("h.insure", StringType.INSTANCE);
        Object insure=(String)insureQueryObject.setMaxResults(1).uniqueResult();
        if (insure != null && insure.toString().equals("Y")) {
		custAddress.setInsure("yes");
	    } else {
		custAddress.setInsure("no");
        }
    }

    public String getHold1(String acctNumber) throws Exception {
	String queryString = "select ca.hold,ca.acctType from CustAddress ca where ca.acctNo='" + acctNumber + "'";
	Object result = getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
	return null!=result?result.toString():"" ;
    }

    public String getMaster(String acctNumber) throws Exception {
	String QueryString = "select ca.master from CustAddress ca  where ca.acctNo='" + acctNumber + "'";
	Object master = getCurrentSession().createQuery(QueryString).setMaxResults(1).uniqueResult();
	return null!=master?master.toString():"";
    }

    public String getCountry(Integer countryCode) throws Exception {
	String QueryString = "select gc.codedesc from GenericCode gc  where gc.id= " + countryCode;
	Object country = getCurrentSession().createQuery(QueryString).setMaxResults(1).uniqueResult();
	return null!=country?country.toString():"";

    }

    /**
     * @param masterAcctNumber
     * @return
     */
    public List<CustAddress> getChildrenForMaster(String masterAcctNumber) throws Exception {
	List<CustAddress> custAcctList = getCurrentSession().createQuery(" from  CustAddress ca where ca.master='" + masterAcctNumber + "'").list();
	return custAcctList;
    }

    public List findCustomerAddresses(String accountNumber, String type) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(
		CustAddress.class);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	if (accountNumber != null && !accountNumber.equals("")) {
	    criteria.add(Restrictions.like("acctNo", accountNumber + "%"));
	    criteria.addOrder(Order.asc("acctNo"));
	}
	if (type != null && !type.equals("")) {
	    criteria.add(Restrictions.like("type", type + "%"));
	    criteria.addOrder(Order.asc("type"));
	}
	return criteria.list();
    }

    public List getCustomerAddress(String acctName) throws Exception {
	String queryString = " from CustomerAddress  where acctname='" + acctName + "'";
	List queryObject = getCurrentSession().createQuery(queryString).list();
	return queryObject;
    }

    public Integer selectTermFromVendor(String custid) throws Exception {
	Integer result = 0;
	String queryString = "select credit_terms from vendor_info ven where cust_accno='" + custid + "'";
	Object creditTerms = getCurrentSession().createSQLQuery(queryString).uniqueResult();
	if (null != creditTerms) {
	    result = (Integer) creditTerms;
	}
	return result;
    }

//Object of vendor
    public Vendor getVendorAddress(String acctName) throws Exception {
	String queryString = " from Vendor where accountno='" + acctName + "'";
	return (Vendor)getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
    }

    public CustomerAddress findByAgentName(String agentNo) throws Exception {
	agentNo = agentNo.replace("'", "'+'");
	String queryString = " from CustomerAddress  where accountNo='" + agentNo + "'";
	return (CustomerAddress)getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
    }

    public List<CustAddress> getCustAddress(String acctNumber) throws Exception {
	List<CustAddress> custAddressList = new ArrayList<CustAddress>();
	String queryString = "from CustAddress where acctNo='" + acctNumber + "'";
	List<CustAddress> resultList = new ArrayList<CustAddress>();
	resultList = getCurrentSession().createQuery(queryString).list();
	for (CustAddress custAddress : resultList) {
	    custAddressList.add(custAddress);
	}
	return custAddressList;
    }

    public CustAddress getCustAddressForCheck(String acctNumber) throws Exception {
	CustAddress custAddress = null;
	StringBuilder mainQuery = new StringBuilder("from CustAddress");
	mainQuery.append(" where acctNo='").append(acctNumber).append("'");
	StringBuilder queryBuilder = new StringBuilder(mainQuery.toString()).append(" and checkAddress=").append(true);
	Query query = getCurrentSession().createQuery(queryBuilder.toString());
	query.setMaxResults(1);
	custAddress = (CustAddress) query.uniqueResult();
	if (null == custAddress) {
	    queryBuilder = new StringBuilder(mainQuery.toString()).append(" and primeAddress='").append(CommonConstants.ON).append("'");
	    query = getCurrentSession().createQuery(queryBuilder.toString());
	    query.setMaxResults(1);
	    custAddress = (CustAddress) query.uniqueResult();
	    if (null == custAddress) {
		queryBuilder = new StringBuilder(mainQuery.toString()).append(" order by id");
		query = getCurrentSession().createQuery(queryBuilder.toString());
		query.setMaxResults(1);
		custAddress = (CustAddress) query.uniqueResult();
	    }
	}
	return custAddress;
    }

    public void updateAddrPrime(String acctNo) throws Exception {
	String queryString = "UPDATE cust_address ca SET ca.prime = 'OFF' WHERE ca.acct_no = '" + acctNo + "' and ca.prime = 'ON'";
	getSession().createSQLQuery(queryString).executeUpdate();
    }

    public void updateCheckAddress(String acctNo) throws Exception {
	String queryString = "UPDATE cust_address ca SET ca.check_address = '0' WHERE ca.acct_no = '" + acctNo + "' and ca.check_address = '1'";
	getSession().createSQLQuery(queryString).executeUpdate();
    }

    public CustAddress findPrimeContact(String custNo) throws Exception {
	String queryString = " from CustAddress  where acctNo='" + custNo + "' and primeAddress = 'ON'";
	return (CustAddress)getCurrentSession().createQuery(queryString).setMaxResults(1).uniqueResult();
    }

    public CustAddress getAddressAndPhone(String accountNumber) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(CustAddress.class);
	criteria.add(Restrictions.eq("acctNo", accountNumber));
	criteria.addOrder(Order.asc("primeAddress"));
	criteria.setMaxResults(1);
	return (CustAddress) criteria.uniqueResult();
    }

    public CustAddress getUniqueAddress(String acctNo) throws Exception {
	Criteria criteria = getCurrentSession().createCriteria(CustAddress.class);
	criteria.add(Restrictions.eq("acctNo", acctNo));
	criteria.addOrder(Order.asc("primeAddress"));
	criteria.setMaxResults(1);
	return (CustAddress) criteria.uniqueResult();
    }

    public String getAddress(String acctNo) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select");
	queryBuilder.append("  upper(");
	queryBuilder.append("    concat_ws(");
	queryBuilder.append("      '\\n',");
	queryBuilder.append("      if(ca.address1 <> '', ca.address1, null),");
	queryBuilder.append("      if(city.un_loc_name <> '' or ca.state <> '' or ca.zip <> '',");
	queryBuilder.append("        concat_ws(");
	queryBuilder.append("          ', ',");
	queryBuilder.append("          if(city.un_loc_name <> '', city.un_loc_name, null),");
	queryBuilder.append("          if(ca.state <> '', ca.state, null),");
	queryBuilder.append("          if(ca.zip <> '', ca.zip, null)");
	queryBuilder.append("        ),");
	queryBuilder.append("        null");
	queryBuilder.append("      ),");
	queryBuilder.append("      if(cntry.code <> 'US', cntry.codedesc, null),");
	queryBuilder.append("      if(ca.phone <> '', concat('PHONE: ', ca.phone), null),");
	queryBuilder.append("      if(ca.fax <> '', concat('FAX: ', ca.fax), null),");
	queryBuilder.append("      if(ca.email1 <> '' or ca.email2 <> '',");
	queryBuilder.append("        concat(");
	queryBuilder.append("          'EMAIL: ',");
	queryBuilder.append("          concat_ws(', ', if(ca.email1 <> '', ca.email1, null), if(ca.email2 <> '', ca.email2, null))");
	queryBuilder.append("        ),");
	queryBuilder.append("        null");
	queryBuilder.append("      )");
	queryBuilder.append("    )");
	queryBuilder.append("  ) as address ");
	queryBuilder.append("from");
	queryBuilder.append("  cust_address ca ");
	queryBuilder.append("  left join un_location city ");
	queryBuilder.append("    on (ca.city = city.id)");
	queryBuilder.append("  left join genericcode_dup cntry");
	queryBuilder.append("    on (ca.country = cntry.id) ");
	queryBuilder.append("where");
	queryBuilder.append("  ca.acct_no = '").append(acctNo).append("' ");
	queryBuilder.append("order by field(ca.prime, 'on') desc, ca.id asc limit 1");
	return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }
    
    public String getCoName(String accountNo) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select co_name as coName FROM cust_address ca where ca.`acct_no` = :accountNo");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setString("accountNo", accountNo);
        query.addScalar("coName", StringType.INSTANCE);
        return (String) query.setMaxResults(1).uniqueResult();
    }
    
    public String getCheckAddress(String accountNo) {
        StringBuilder queryString = new StringBuilder();
        queryString.append("select ");
        queryString.append("  concat(");
        queryString.append("    if(");
        queryString.append("      ca.`address1` <> '',");
        queryString.append("      concat(ca.`address1`, '\\n'),");
        queryString.append("      ''");
        queryString.append("    ),");
        queryString.append("    if(");
        queryString.append("      ca.`city1` <> '',");
        queryString.append("      concat(ca.`city1`, ', '),");
        queryString.append("      ''");
        queryString.append("    ),");
        queryString.append("    if(");
        queryString.append("      ca.`state` <> '',");
        queryString.append("      concat(ca.`state`, ' '),");
        queryString.append("      ''");
        queryString.append("    ),");
        queryString.append("    if(");
        queryString.append("      ca.`zip` <> '',");
        queryString.append("      concat(ca.`zip`, ' '),");
        queryString.append("      ''");
        queryString.append("    )");
        queryString.append("  ) as checkAddress ");
        queryString.append("from");
        queryString.append("  `cust_address` ca ");
        queryString.append("where ca.`acct_no` = :accountNo ");
        queryString.append("order by field(ca.`check_address`, true) desc, ca.`id` asc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryString.toString());
        query.setString("accountNo", accountNo);
        query.addScalar("checkAddress", StringType.INSTANCE);
        query.setMaxResults(1);
        return (String) query.setMaxResults(1).uniqueResult();
    }
}
