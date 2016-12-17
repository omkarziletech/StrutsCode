package com.gp.cvst.logisoft.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.domain.PaymentOnAccount;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class PaymentOnAccount.
 * @see com.gp.cvst.logisoft.hibernate.dao.PaymentOnAccount
 * @author MyEclipse - Hibernate Tools
 */
public class PaymentOnAccountDAO extends BaseHibernateDAO {

    //property constants
    public static final String BATCH_ID = "batchId";
    public static final String CUST_ID = "custId";
    public static final String CHECK_NO = "checkNo";
    public static final String ON_ACCOUNT_AMT = "onAccountAmt";

    public void save(PaymentOnAccount transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(PaymentOnAccount persistentInstance) throws Exception{
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public PaymentOnAccount findById(java.lang.Integer id) throws Exception{
            PaymentOnAccount instance = (PaymentOnAccount) getSession().get("com.gp.cvst.logisoft.hibernate.dao.PaymentOnAccount", id);
            return instance;
    }

    public List findByExample(PaymentOnAccount instance) throws Exception{
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.PaymentOnAccount").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception{
            String queryString = "from PaymentOnAccount as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public List findByBatchId(Object batchId) throws Exception{
        return findByProperty(BATCH_ID, batchId);
    }

    public List findByCustId(Object custId)  throws Exception{
        return findByProperty(CUST_ID, custId);
    }

    public List findByCheckNo(Object checkNo)  throws Exception{
        return findByProperty(CHECK_NO, checkNo);
    }

    public List findByOnAccountAmt(Object onAccountAmt) throws Exception{
        return findByProperty(ON_ACCOUNT_AMT, onAccountAmt);
    }

    public PaymentOnAccount merge(PaymentOnAccount detachedInstance) throws Exception{
            PaymentOnAccount result = (PaymentOnAccount) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(PaymentOnAccount instance)  throws Exception{
            getSession().saveOrUpdate(instance);
    }

    public void attachClean(PaymentOnAccount instance)  throws Exception{
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }

    public double getOnAccountBalance(String accountno) throws Exception{
        double bal = 0.0;
        String queryString = "select sum(poa.onAccountAmt) from PaymentOnAccount poa where poa.custId='" + accountno + "' ";
            Iterator it = getCurrentSession().createQuery(queryString).list().iterator();
            Object o = it.next();
            if (o != null) {
                bal = (Double) o;
            }
        return bal;
    }
    //Nag
    /*
     *private Integer id;
    private Integer batchId;
    private Date batchDate;
    private String custId;
    private String checkNo;
    private Date checkDate;
    private Double onAccountAmt;
     */
    /* static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    public List showforinvoice(String value)
    {
    //search is performed based on Transaction Id ,Not based on Invoice Number
    try
    {

    List <PayOnAccountsBean> payonaccountsList = new ArrayList<PayOnAccountsBean>();
    PayOnAccountsBean  payonaccountsbean = null;
    String queryString ="";

    queryString = "select px.custId,px.checkNo,px.checkDate,px.checkDate,px.onAccountAmt from PaymentOnAccount px where px.custId='"+value+"'";
    
    List queryObject = getCurrentSession().createQuery(queryString)
    .list();
    Iterator itr = queryObject.iterator();
    while (itr.hasNext())
    {
    payonaccountsbean = new PayOnAccountsBean();
    Object[] row =  (Object[]) itr.next();
    //Double payamount = (Double)(row[0]);
    String custId = (String)(row[0]);
    String checkNo=(String)(row[1]);
    Date checkDate=(Date)(row[2]);
    Double onAccountAmt=(Double)(row[3]);

    payonaccountsbean.setCheckNo(checkNo);
    payonaccountsbean.setCustId(custId);
    payonaccountsbean.setCheckDate(checkDate);
    payonaccountsbean.setOnAccountAmt(onAccountAmt);

    payonaccountsList.add(payonaccountsbean);
    payonaccountsbean= null;

    }
    
    return payonaccountsList;

    }catch(Exception e)
    {

    return null;
    }
    }// end for the list
     */
}
