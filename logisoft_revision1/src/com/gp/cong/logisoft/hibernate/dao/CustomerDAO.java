package com.gp.cong.logisoft.hibernate.dao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.beans.customerBean;
import com.gp.cong.logisoft.domain.Customer;
import com.gp.cong.logisoft.domain.CustomerTemp;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.reports.dto.SearchCustomerSampleDTO;

public class CustomerDAO extends BaseHibernateDAO {
    public List findCutomerAddresses(String acctno) throws Exception {
        List id = null;
            String queryString = "from Customer where accountNo=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", acctno);
            id = queryObject.list();
        return id;
    }

    public List findMasterAddresses(String accountNo, String type)throws Exception {
        List list = new ArrayList();
            String queryString = "from CustomerTemp where accountNo=?0 and type=?1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountNo);
            queryObject.setParameter("1", type);
            list = queryObject.list();
        return list;
    }

    public List findAccountNumber(String accountNo)throws Exception {
        List list = new ArrayList();
            String queryString = "from Customer where accountName=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountNo);
            list = queryObject.list();
        return list;
    }

    public List findAccountName(String accountName)throws Exception {
        List list = new ArrayList();
            String queryString = "from CustomerTemp where accountName =?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountName);
            list = queryObject.list();
        return list;
    }

    public List findAccountName1(String accountName) throws Exception {
        List list = new ArrayList();
            String queryString = "from TradingPartnerTemp where accountName =?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountName);
            list = queryObject.list();
        return list;
    }

    public List findAccountNo(String accountNo)throws Exception {
        List list = new ArrayList();
            String queryString = "from CustomerTemp where accountNo=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountNo);
            list = queryObject.list();
        return list;
    }

    public List findAccountNo1(String accountNo)throws Exception {
        List list = new ArrayList();
            String queryString = "from TradingPartnerTemp where accountno=?0";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountNo);
            list = queryObject.list();
        return list;
    }

    public List findmasterAccountNumber(String accountNo, String type)throws Exception {
        List list = new ArrayList();
            String queryString = "from CustomerTemp where accountNo=?0 and type=?1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountNo);
            queryObject.setParameter("1", type);
            list = queryObject.list();
        return list;
    }

    public List findmasterAccountName(String accountName, String type)throws Exception {
        List list = new ArrayList();
            String queryString = "from CustomerTemp where accountName=?0 and type=?1";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountName);
            queryObject.setParameter("1", type);
            list = queryObject.list();
        return list;
    }

    //query for retrieving id,name from cust_address based on Master type
    public Iterator master() throws Exception {
        Iterator list = null;
            String queryString = "select accountNo,accountName from CustomerTemp where type='master'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            list = queryObject.list().iterator();
        return list;
    }

    public Iterator master1()throws Exception {
        Iterator list = null;
            String queryString = "select accountno,accountName from TradingPartner where Type='master'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            list = queryObject.list().iterator();
        return list;
    }
    //end of query
    public List findAccountPrefix()throws Exception {
        List list = new ArrayList();
            String queryString = "from CustomerTemp where type='mb' order by accountNo desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setMaxResults(1);
            list = queryObject.list();
        return list;
    }

    public List findAccountPrefix(String accountNo)throws Exception {
        List list = new ArrayList();
            String queryString = "from CustomerTemp where accountNo like ?0 order by accountNo desc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", accountNo + "%");
            queryObject.setMaxResults(1);
            list = queryObject.list();
        return list;
    }

    public void save(Customer transientInstance, String userName)throws Exception {
            getSession().saveOrUpdate(transientInstance);
    }

    public List findAllCustomers(String type)throws Exception {
            String queryString = "from CustomerTemp where type=?0 order by accountNo asc";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", type);
            queryObject.setMaxResults(5000);
            return queryObject.list();
    }

    public List findForManagement1(String account, String name, String acctType, String type)throws Exception {
        List resultList = new ArrayList();
            String prime = "on";
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            Criterion accountNumbercriteria = null;
            Criterion accountNamecriteria = null;
            if (account != null && !account.trim().equals("")) {
                accountNumbercriteria = Restrictions.like("accountNo", account + "%");
                //accountNumbercriteria.addOrder(Order.asc("accountNo"));
            }
            if (name != null && !name.trim().equals("")) {
                accountNamecriteria = Restrictions.like("accountName", name + "%");
                //accountNamecriteria.addOrder(Order.asc("accountName"));
            }
            LogicalExpression orExp = Restrictions.or(accountNamecriteria, accountNumbercriteria);
            criteria.add(orExp);
            criteria.addOrder(Order.asc("accountName"));
            if (acctType == null) {
                String ty = "V";
                String tt = "SS";
                Criterion accttype1 = Restrictions.like("accountType", "%" + ty + "%");
                Criterion accttype2 = Restrictions.like("accountType", "%" + tt + "%");
                LogicalExpression orExp1 = Restrictions.or(accttype1, accttype2);
                criteria.add(orExp1);
            } else {
                criteria.add(Restrictions.like("accountType", acctType));
            }
            if (type != null && !type.trim().equals("")) {
                criteria.add(Restrictions.like("type", type));
                criteria.addOrder(Order.asc("type"));
            }
            if (prime != null && !prime.trim().equals("")) {
                criteria.add(Restrictions.like("primary", prime));
                criteria.addOrder(Order.asc("primary"));
            }
            criteria.setMaxResults(50);
            resultList = criteria.list();
        return resultList;
    }

    public List findForShipOrForOrCon(String account, String name, String acctType, String type, String find)throws Exception {
        List resultList = new ArrayList();
            String prime = "on";
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            Criterion accountNumbercriteria = null;
            Criterion accountNamecriteria = null;
            Criterion shipperType1 = null;
            Criterion shipperType2 = null;
            Criterion shipperType3 = null;
            Criterion shipperType4 = null;
            if (account != null && !account.trim().equals("")) {
                accountNumbercriteria = Restrictions.like("accountNo", account + "%");
                //accountNumbercriteria.addOrder(Order.asc("accountNo"));
            }
            if (name != null && !name.trim().equals("")) {
                accountNamecriteria = Restrictions.like("accountName", name + "%");
                //accountNamecriteria.addOrder(Order.asc("accountName"));
            }
            LogicalExpression orExp = Restrictions.or(accountNamecriteria, accountNumbercriteria);
            criteria.add(orExp);
            criteria.addOrder(Order.asc("accountName"));
            if (null != find && find.trim().equals("shipper")) {
                String type1 = "S";
                String type2 = "F";
                String type3 = "E";
                String type4 = "I";
                shipperType1 = Restrictions.like("accountType", "%" + type1 + "%");
                shipperType2 = Restrictions.like("accountType", "%" + type2 + "%");
                shipperType3 = Restrictions.like("accountType", "%" + type3 + "%");
                shipperType4 = Restrictions.like("accountType", "%" + type4 + "%");
                LogicalExpression orExp1 = Restrictions.or(shipperType1, shipperType2);
                LogicalExpression orExp2 = Restrictions.or(shipperType3, shipperType4);
                criteria.add(Restrictions.or(orExp1, orExp2));
            } else if (null != find && find.trim().equals("forwarder")) {
                criteria.add(Restrictions.like("accountType", "%V%"));
                criteria.add(Restrictions.eq("subType", "Forwarder"));
                //criteria.addOrder(Order.asc("accountType"));
            } else if (null != find && find.trim().equals("consignee")) {
                String type6 = "C";
                criteria.add(Restrictions.like("accountType", "%" + type6 + "%"));
                //criteria.addOrder(Order.asc("accountType"));
            } else if (type != null && !type.trim().equals("")) {
                criteria.add(Restrictions.like("type", type));
                criteria.addOrder(Order.asc("type"));
            }
            if (prime != null && !prime.trim().equals("")) {
                criteria.add(Restrictions.like("primary", prime));
                criteria.addOrder(Order.asc("primary"));
            }
            criteria.setMaxResults(50);
            resultList = criteria.list();
        return resultList;
    }

    public List findForManagement(String account, String name, String acctType, String type)throws Exception {
            String prime = "on";
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            Criterion accountNumbercriteria = null;
            Criterion accountNamecriteria = null;
            if (account != null && !account.trim().equals("")) {
                accountNumbercriteria = Restrictions.like("accountNo", account + "%");
                //accountNumbercriteria.addOrder(Order.asc("accountNo"));
            }
            if (name != null && !name.trim().equals("")) {
                accountNamecriteria = Restrictions.like("accountName", name + "%");
                //accountNamecriteria.addOrder(Order.asc("accountName"));
            }
            LogicalExpression orExp = Restrictions.or(accountNamecriteria, accountNumbercriteria);
            criteria.add(orExp);
            criteria.addOrder(Order.asc("accountName"));

            if (acctType != null && !acctType.trim().equals("")) {
                criteria.add(Restrictions.like("accountType", "%" + acctType + "%"));
                //criteria.addOrder(Order.asc("accountType"));
            }

            if (type != null && !type.trim().equals("")) {
                criteria.add(Restrictions.like("type", type));
                criteria.addOrder(Order.asc("type"));
            }
            if (prime != null && !prime.trim().equals("")) {
                criteria.add(Restrictions.like("primary", prime));
                criteria.addOrder(Order.asc("primary"));
            }
            criteria.setMaxResults(50);
            return criteria.list();
    }

    public List findForManagement2(String name, String acctType, String type)throws Exception {
            String prime = "on";
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            if (name != null && !name.trim().equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
                criteria.addOrder(Order.asc("accountName"));
            }
            if (acctType != null && !acctType.trim().equals("")) {
                criteria.add(Restrictions.like("accountType", "%" + acctType + "%"));
                //criteria.addOrder(Order.asc("accountType"));
            }

            if (type != null && !type.trim().equals("")) {
                criteria.add(Restrictions.like("type", type));
                criteria.addOrder(Order.asc("type"));
            }
            if (prime != null && !prime.trim().equals("")) {
                criteria.add(Restrictions.like("primary", prime));
                criteria.addOrder(Order.asc("primary"));
            }
            criteria.setMaxResults(50);
            return criteria.list();
    }

    public List findForManagementaction(String account, String name, String accountType, String type)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.ge("accountNo", account));
                criteria.addOrder(Order.asc("accountNo"));
            }
            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.ge("accountName", name));
                criteria.addOrder(Order.asc("accountName"));
            }
            if (accountType != null && !accountType.equals("")) {
                criteria.add(Restrictions.ge("accountType", "%" + accountType + "%"));
                criteria.addOrder(Order.asc("accountType"));
            }
            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.like("type", type));
                criteria.addOrder(Order.asc("type"));
            }
            criteria.setMaxResults(500);


            return criteria.list();
    }

    public List findForAccountNo(String account, String type)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountNo", account + "%"));
            }

            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.like("accountType", "%" + type + "%"));
            }
            criteria.setMaxResults(50);
            return criteria.list();
    }

    public List findForAgenttNo(String account, String name)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountNo", account + "%"));
            }
            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
            }
            criteria.add(Restrictions.like("primary", "on"));

            criteria.setMaxResults(50);
            return criteria.list();
    }
    public List findForAgentNumber(String account, String name)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountNo", account + "%"));
            }
            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
            }
          criteria.add(Restrictions.not(Restrictions.like("accountType", "%Z%")));
            criteria.add(Restrictions.like("primary", "on"));
            criteria.setMaxResults(50);
            return criteria.list();
    }

    public TradingPartner getCustTemp(String account)throws Exception {
            String query = "from TradingPartner where accountno = '" + account + "'";
            Object tpObect = getCurrentSession().createQuery(query).uniqueResult();
            return null != tpObect ? (TradingPartner)tpObect : null;
    }

    public List findForAgenttNo1(String account, String name)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(TradingPartnerTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountno", account + "%"));
            }
            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
            }
            criteria.setMaxResults(50);
            return criteria.list();
    }
    //search

    public List findForConsigneetNo(String account, String name, String type)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountNo", account + "%"));
                criteria.addOrder(Order.asc("accountNo"));
            }

            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
                criteria.addOrder(Order.asc("accountName"));
            }

            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.like("accountType", "%" + type + "%"));
            }
            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.not(Restrictions.like("accountType", "%Z%")));
            }
            criteria.setMaxResults(50);
            return criteria.list();
    }
     public List findForConsigneetNumber(String account, String name, String type)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(CustomerTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountNo", account + "%"));
                criteria.addOrder(Order.asc("accountNo"));
            }

            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
                criteria.addOrder(Order.asc("accountName"));
            }

            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.like("accountType", "%" + type + "%"));
                criteria.add(Restrictions.not(Restrictions.like("accountType", "%Z%")));
            }
            criteria.setMaxResults(50);
            return criteria.list();
    }

    public List findForConsigneetNo3(String account, String name, String type)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(TradingPartnerTemp.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountno", account + "%"));
                criteria.addOrder(Order.asc("accountno"));
            }
            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
                criteria.addOrder(Order.asc("accountName"));
            }
            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.like("acctType", "%" + type + "%"));
            }
            criteria.setMaxResults(50);
            return criteria.list();
    }

    public List findForConsigneetNo1(String account, String name, String type)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(CustAddress.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("acctNo", account + "%"));
            }
            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("acctName", name + "%"));
            }
            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.like("acctType", "%" + type + "%"));
            }
            criteria.setMaxResults(5000);
            return criteria.list();
    }

    public List findForConsigneetNo2(String account, String name, String type)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(Customer.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountNo", account + "%"));
            }
            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
            }
            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.like("accountType", "%" + type + "%"));
            }
            criteria.setMaxResults(5000);
            return criteria.list();
    }

    //code 4 search end
    public Iterator getAgentAcctNoForDisplay()throws Exception {
        Iterator results = null;
            results = getCurrentSession().createQuery(
                    "select accountNo from Customer").list().iterator();
        return results;
    }

    private static long printOutput(String type, Date invoiceDate, Date today, long result)throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return result;
    }

    public static long daysBetween(Calendar invoiceDate, Calendar today)throws Exception {
        Calendar date = (Calendar) invoiceDate.clone();
        long daysBetween = 0;
        while (date.before(today)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public TradingPartner findById(String accountNo)throws Exception {
            TradingPartner instance = (TradingPartner) getSession().get("com.gp.cong.logisoft.domain.TradingPartner", accountNo);
            return instance;
    }

    public CustomerTemp findById1(String accountNo)throws Exception {
            CustomerTemp instance = (CustomerTemp) getSession().get("com.gp.cong.logisoft.domain.CustomerTemp", accountNo);
            return instance;
    }

    public void update(Customer persistanceInstance, String userName)throws Exception {
            getSession().saveOrUpdate(persistanceInstance);
    }

    public void delete(Customer persistanceInstance, String userName)throws Exception {
            getSession().delete(persistanceInstance);
    }

    public List getSearchCustList(String accountNumber, Integer collection)throws Exception {
        NumberFormat num = new DecimalFormat("0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date today = new Date();
        customerBean custBean = null;
        List<customerBean> searchCustList = new ArrayList<customerBean>();
        String QueryString = "";
            if (!accountNumber.equals("")) {
                QueryString = "select t.custName,t.custNo,t.invoiceNumber,t.transactionDate,t.balance,t.transactionAmt,t.transactionId,t.transactionType from Transaction t where t.custNo like '" + accountNumber + "%' and t.transactionType='AR' and t.balance<>0.0";
                List QueryObject = getCurrentSession().createQuery(QueryString).list();
                Iterator itr = QueryObject.iterator();
                while (itr.hasNext()) {
                    custBean = new customerBean();
                    Object[] row = (Object[]) itr.next();
                    String custName = (String) row[0];
                    String custNo = (String) row[1];
                    String invoiceNumber = (String) row[2];
                    Date transactionDate = (Date) row[3];
                    Double balance = (Double) row[4];
                    Double transactionAmt = (Double) row[5];
                    Integer transactionId = (Integer) row[6];
                    String transType = (String) row[7];
                    if (balance == null) {
                        balance = 0.0;
                    }
                    String bal = (String.valueOf(balance));
                    if (bal.contains(",")) {
                        bal = bal.replace(",", "");
                    }
                    balance = (Double.parseDouble(bal));
                    String Date = "";
                    String aging = "";
                    if (transactionDate != null && !transactionDate.equals("")) {
                        Date = sdf.format(transactionDate);
                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTime(transactionDate);
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(today);
                        long inDate = printOutput("Calendar", transactionDate, today, daysBetween(cal1, cal2));
                        aging = (String.valueOf(inDate));
                    }
                    custBean.setAccountNo(custNo);
                    custBean.setName(custName);
                    custBean.setInvoiceNumber(null != invoiceNumber ? invoiceNumber : "");
                    custBean.setTransactionDate(Date);
                    custBean.setBalance(num.format(balance));
                    custBean.setAging(aging);
                    custBean.setTransAmount(String.valueOf(transactionAmt));
                    custBean.setTransactionId(transactionId);
                    custBean.setTransactionType(null != transType ? transType : "");
                    searchCustList.add(custBean);
                    custBean = null;
                }
            } else if (collection != 0) {
                String QueryString1 = "select ca.accountNo,ca.contact from CustomerAccounting ca where ca.arcode='" + collection + "'";
                List QueryObject1 = getCurrentSession().createQuery(QueryString1).list();

                Iterator itr = QueryObject1.iterator();
                while (itr.hasNext()) {
                    custBean = new customerBean();
                    Object[] row = (Object[]) itr.next();
                    String accountNo = (String) row[0];
                    String arcode = (String) row[1];
                    if (!accountNo.equals("")) {
                        QueryString = "select t.custName,t.custNo,t.invoiceNumber,t.transactionDate,t.balance,t.transactionAmt,t.transactionId,t.transactionType from Transaction t where t.custNo='" + accountNo + "'";
                        List QueryObject = getCurrentSession().createQuery(QueryString).list();
                        Iterator itr1 = QueryObject.iterator();
                        while (itr1.hasNext()) {
                            custBean = new customerBean();
                            Object[] row1 = (Object[]) itr1.next();
                            String custName = (String) row1[0];
                            String custNo = (String) row1[1];
                            String invoiceNumber = (String) row1[2];
                            Date transactionDate = (Date) row1[3];
                            Double balance = (Double) row1[4];
                            Double transactionAmt = (Double) row1[5];
                            Integer transactionId = (Integer) row[6];
                            String transType = (String) row[7];
                            if (transactionAmt == null) {
                                transactionAmt = 0.0;
                            }
                            String transAmt = (String.valueOf(transactionAmt));
                            if (transAmt.contains(",")) {
                                transAmt = transAmt.replace(",", "");
                            }
                            transactionAmt = (Double.parseDouble(transAmt));
                            if (balance == null) {
                                balance = 0.0;
                            }
                            String bal = (String.valueOf(balance));
                            if (bal.contains(",")) {
                                bal = bal.replace(",", "");
                            }
                            balance = (Double.parseDouble(bal));
                            String Date = "";
                            String aging = "";
                            if (transactionDate != null && !transactionDate.equals("")) {
                                Date = sdf.format(transactionDate);
                                Calendar cal1 = Calendar.getInstance();
                                cal1.setTime(transactionDate);
                                Calendar cal2 = Calendar.getInstance();
                                cal2.setTime(today);
                                long inDate = printOutput("Calendar", transactionDate, today, daysBetween(cal1, cal2));
                                aging = (String.valueOf(inDate));
                            }
                            custBean.setAccountNo(custNo);
                            custBean.setName(custName);
                            custBean.setInvoiceNumber(null != invoiceNumber ? invoiceNumber : "");
                            custBean.setTransactionDate(Date);
                            custBean.setBalance(num.format(balance));
                            custBean.setAging(aging);
                            custBean.setTransAmount(num.format(transactionAmt));
                            custBean.setTransactionId(transactionId);
                            custBean.setTransactionType(null != transType ? transType : "");
                            searchCustList.add(custBean);
                            custBean = null;
                        }
                    }
                }
            }
        return searchCustList;
    }

    /**
     *this is for search customer sample collection list in aging reports
     * @param collection
     * @param number
     * @param allCollectors
     * @return
     */
    public List getsearchCollectionList(Integer collection, String number, String allCollectors)throws Exception {
        SearchCustomerSampleDTO searchCustomerSampleDTO = null;
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date today = new Date();
        List<SearchCustomerSampleDTO> collectorList = new ArrayList<SearchCustomerSampleDTO>();
        String QueryString = "";
        String QueryString1 = "";
            if (collection != 0 && number.equals("")) {
                QueryString1 = "select distinct ca.accountNo from CustomerAccounting ca where ca.arcode='" + collection + "'";
            } else if (!number.equals("") && collection == 0) {
                QueryString1 = "select ca.accountNo,ca.arcode from CustomerAccounting ca where ca.accountNo ='" + number + "'";
            } else if (allCollectors.equals("yes") && number.equals("") && collection == 0) {
                QueryString1 = "select ca.accountNo,ca.arcode from CustomerAccounting ca ";
            }
            List QueryObject1 = getCurrentSession().createQuery(QueryString1).list();
            int size = QueryObject1.size();
            int count = 0;
            while (size > 0) {
                String accountNo = (String) QueryObject1.get(count);
                QueryString = "select t.custName,t.custNo,t.invoiceNumber,t.transactionDate,t.balance,t.transactionAmt,t.billLaddingNo from Transaction t where t.custNo='" + accountNo + "'";
                List QueryObject = getCurrentSession().createQuery(QueryString).list();
                Iterator itr1 = QueryObject.iterator();
                String custNo = "";
                String custName = "";
                int i = 0;
                while (itr1.hasNext()) {
                    searchCustomerSampleDTO = new SearchCustomerSampleDTO();
                    Object[] row1 = (Object[]) itr1.next();
                    custName = (String) row1[0];
                    custNo = (String) row1[1];
                    String invoiceNumber = (String) row1[2];
                    Date transactionDate = (Date) row1[3];
                    Double balance = (Double) row1[4];
                    Double transactionAmt = (Double) row1[5];
                    String billLaddingNo = (String) row1[6];
                    if (balance == null) {
                        balance = 0.0;
                    }
                    String bal = (String.valueOf(balance));
                    if (bal.contains(",")) {
                        bal = bal.replace(",", "");
                    }
                    balance = (Double.parseDouble(bal));
                    String aging = "";
                    String Date = "";
                    if (transactionDate != null && transactionDate.equals("")) {
                        Date = sdf.format(transactionDate);
                        Calendar cal1 = Calendar.getInstance();
                        cal1.setTime(transactionDate);
                        Calendar cal2 = Calendar.getInstance();
                        cal2.setTime(today);
                        long inDate = printOutput("Calendar", transactionDate, today, daysBetween(cal1, cal2));
                        aging = (String.valueOf(inDate));
                    }
                    if (balance != null && balance > 0.0) {
                        searchCustomerSampleDTO.setAcctNo(custNo);
                        searchCustomerSampleDTO.setAcctName(custName);
                        searchCustomerSampleDTO.setInvoiceNum(invoiceNumber);
                        searchCustomerSampleDTO.setInvoiceDate(Date);
                        searchCustomerSampleDTO.setBalance(numberFormat.format(balance));
                        searchCustomerSampleDTO.setAging(aging);
                        searchCustomerSampleDTO.setBillofladingno(billLaddingNo);
                        collectorList.add(searchCustomerSampleDTO);
                        searchCustomerSampleDTO = null;
                    }
                }
                count++;
                size--;
            }
        return collectorList;
    }

    /**
     * This is for search customer sample all collector reports in aging
     * @return
     */
    public List getAllCollectorsList()throws Exception {
        SearchCustomerSampleDTO searchCustomerSampleDTO = null;
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date today = new Date();
        List<SearchCustomerSampleDTO> allCollectorsList = new ArrayList<SearchCustomerSampleDTO>();
        String QueryString = "";
        String QueryString1 = "";
            QueryString1 = "select distinct ca.accountNo from CustomerAccounting ca where ca.arcode IS NOT NULL and ca.accountNo IS NOT NULL order by ca.accountNo asc ";
            List QueryObject1 = getCurrentSession().createQuery(QueryString1).list();
            Iterator itr = QueryObject1.iterator();
            int size = 0;
            while (itr.hasNext()) {
                String accountNo = (String) QueryObject1.get(size);
                if (accountNo != null && !accountNo.equals("")) {
                    QueryString = "select t.custName,t.custNo,t.invoiceNumber,t.transactionDate,t.balance,t.transactionAmt,t.billLaddingNo from Transaction t where t.custNo='" + accountNo + "' and t.custName IS NOT NULL order by t.custNo asc";
                    List QueryObject = getCurrentSession().createQuery(QueryString).list();
                    Iterator itr1 = QueryObject.iterator();
                    String custNo = "";
                    String custName = "";
                    int i = 0;
                    int j = 0;
                    while (itr1.hasNext()) {
                        searchCustomerSampleDTO = new SearchCustomerSampleDTO();
                        Object[] row1 = (Object[]) itr1.next();
                        custName = (String) row1[0];
                        custNo = (String) row1[1];
                        String invoiceNumber = (String) row1[2];
                        Date transactionDate = (Date) row1[3];
                        Double balance = (Double) row1[4];
                        Double transactionAmt = (Double) row1[5];
                        String billLaddingNo = (String) row1[6];
                        if (balance == null) {
                            balance = 0.0;
                        }
                        String bal = (String.valueOf(balance));
                        if (bal.contains(","));
                        {
                            bal = bal.replace(",", "");
                        }
                        balance = (Double.parseDouble(bal));
                        String Date = "";
                        String aging = "";
                        if (transactionDate != null && !transactionDate.equals("")) {
                            Date = sdf.format(transactionDate);
                            Calendar cal1 = Calendar.getInstance();
                            cal1.setTime(transactionDate);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTime(today);
                            long inDate = printOutput("Calendar", transactionDate, today, daysBetween(cal1, cal2));
                            aging = (String.valueOf(inDate));
                        }
                        if (balance != null && balance > 0.0) {
                            searchCustomerSampleDTO.setAcctNo(custNo);
                            searchCustomerSampleDTO.setAcctName(custName);
                            searchCustomerSampleDTO.setInvoiceNum(invoiceNumber);
                            searchCustomerSampleDTO.setInvoiceDate(Date);
                            searchCustomerSampleDTO.setBalance(numberFormat.format(balance));
                            searchCustomerSampleDTO.setAging(aging);
                            searchCustomerSampleDTO.setBillofladingno(billLaddingNo);
                            allCollectorsList.add(searchCustomerSampleDTO);
                            searchCustomerSampleDTO = null;
                        }
                        j++;
                    }
                }
                size++;
            }
        return allCollectorsList;
    }

//THIS IS FOR SEARCH CUSTOMER SAMPLE  FOR ALL CUSTOMERS IN AGING 
    public List getAllCustomerList()throws Exception {
        SearchCustomerSampleDTO searchCustomerSampleDTO = null;
        List<SearchCustomerSampleDTO> allCustomersList = new ArrayList<SearchCustomerSampleDTO>();
        String QueryString = "";
        String QueryString1 = "";
        NumberFormat numberFormat = new DecimalFormat("###,###,##0.00");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date today = new Date();
            QueryString1 = "select distinct tr.custName from Transaction tr order by tr.custName asc";
            List QueryObject1 = getCurrentSession().createQuery(QueryString1).list();
            int size = QueryObject1.size();
            int count = 0;
            while (size > 0) {
                String custName = (String) QueryObject1.get(count);
                if (custName != null && !custName.equals("")) {
                    QueryString = "select t.custNo,t.invoiceNumber,t.transactionDate,t.balance,t.transactionAmt,t.billLaddingNo from Transaction t where t.custName='" + custName + "'";
                    List QueryObject = getCurrentSession().createQuery(QueryString).list();
                    Iterator itr1 = QueryObject.iterator();
                    String custNo = "";
                    while (itr1.hasNext()) {
                        searchCustomerSampleDTO = new SearchCustomerSampleDTO();
                        Object[] row1 = (Object[]) itr1.next();
                        custNo = (String) row1[0];
                        String invoiceNumber = (String) row1[1];
                        Date transactionDate = (Date) row1[2];
                        Double balance = (Double) row1[3];
                        if (balance == null) {
                            balance = 0.0;
                        }
                        String bal = (String.valueOf(balance));
                        if (bal.contains(",")) {
                            bal = bal.replace(",", "");
                        }
                        balance = (Double.parseDouble(bal));
                        Double transactionAmt = (Double) row1[4];
                        String billLaddingNo = (String) row1[5];
                        String aging = "";
                        String Date = "";
                        if (transactionDate != null && !transactionDate.equals("")) {
                            Date = sdf.format(transactionDate);
                            Calendar cal1 = Calendar.getInstance();
                            cal1.setTime(transactionDate);
                            Calendar cal2 = Calendar.getInstance();
                            cal2.setTime(today);
                            long inDate = printOutput("Calendar", transactionDate, today, daysBetween(cal1, cal2));
                            aging = (String.valueOf(inDate));
                        }
                        if (balance != null && balance > 0.0) {
                            searchCustomerSampleDTO.setAcctNo(custNo);
                            searchCustomerSampleDTO.setAcctName(custName);
                            searchCustomerSampleDTO.setInvoiceNum(invoiceNumber);
                            searchCustomerSampleDTO.setInvoiceDate(Date);
                            searchCustomerSampleDTO.setBalance(numberFormat.format(balance));
                            searchCustomerSampleDTO.setAging(aging);
                            searchCustomerSampleDTO.setBillofladingno(billLaddingNo);
                            allCustomersList.add(searchCustomerSampleDTO);
                            searchCustomerSampleDTO = null;
                        }
                    }
                } else {
                }
                count++;
                size--;
            }
        return allCustomersList;
    }

    public String masterName(String name)throws Exception {
        String accountNo = "";
            String queryString = "select accountNo from CustomerTemp where accountName=?0 and type='master'";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", name);
            accountNo = queryObject.uniqueResult().toString();
        return accountNo;
    }

    public List findId()throws Exception {
        List id = null;
            String queryString = "select id from Customer where type='master'  ";
            Query queryObject = getCurrentSession().createQuery(queryString);
            //queryObject.setParameter(0, acctno);
            id = queryObject.list();
        return id;
    }

    public List find1(String acctno)throws Exception {
        List id = null;
            String queryString = "select id from Customer where accountNo=?0  ";
            Query queryObject = getCurrentSession().createQuery(queryString);
            queryObject.setParameter("0", acctno);
            id = queryObject.list();
        return id;
    }
//	 public List<AccountBalance> findAccountBalanceForYearAndPeriod(Integer year, Integer period){
//		 List <AccountBalance>  accountBalanceList = null;
//	       try
//	        {
//	       		String queryString = " from AccountBalance where year='"+year+"' and period='"+period+"'" ;
//	       		accountBalanceList = getCurrentSession().createQuery(queryString).list();
//	       		
//	       	} catch (RuntimeException re){
//	       		System.out.println("error in findAccountBalanceForYearAndPeriod" + re);
//	       	}
//	     
//	       	return accountBalanceList;
//	       }

    public Customer findById2(Integer id)throws Exception {
            Customer instance = (Customer) getSession().get("com.gp.cong.logisoft.domain.Customer", id);
            return instance;
    }

    public void delete(TradingPartner persistanceInstance, String userName) throws Exception {
            getSession().delete(persistanceInstance);
    }

    public void save(TradingPartner transientInstance, String userName)throws Exception {
            getSession().saveOrUpdate(transientInstance);
    }

    public void update(TradingPartner persistanceInstance, String userName)throws Exception {
            getSession().merge(persistanceInstance);
    }
     public List findForAgenttNoTrading(String account, String name)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(TradingPartner.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);


            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountno", account + "%"));

            }

            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
            }
         //   criteria.add(Restrictions.like("primary", "on"));

            criteria.setMaxResults(50);
            return criteria.list();
    }
     public List findForConsigneetNoTrading(String account, String name, String type)throws Exception {
            Criteria criteria = getCurrentSession().createCriteria(TradingPartner.class);
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            if (account != null && !account.equals("")) {
                criteria.add(Restrictions.like("accountno", account + "%"));
                criteria.addOrder(Order.asc("accountno"));
            }
            if (name != null && !name.equals("")) {
                criteria.add(Restrictions.like("accountName", name + "%"));
                criteria.addOrder(Order.asc("accountName"));
            }
            if (type != null && !type.equals("")) {
                criteria.add(Restrictions.like("acctType", "%" + type + "%"));
            }
            criteria.setMaxResults(50);
            return criteria.list();
    }
     public boolean isAccountDisabled(String acctNo)throws Exception{
         String q="select count(*) from trading_partner where acct_no='"+acctNo+"' and disabled='Y' limit 1";
         String result=getCurrentSession().createSQLQuery(q).uniqueResult().toString();
         return !result.equals("0");
     }
}
