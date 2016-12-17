package com.gp.cvst.logisoft.hibernate.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.invoiceBean;
import com.gp.cvst.logisoft.domain.Invoice;
import org.hibernate.LockOptions;

/**
 * Data access object (DAO) for domain model class Invoice.
 * @see com.gp.cvst.logisoft.hibernate.dao.Invoice
 * @author MyEclipse - Hibernate Tools
 */
public class InvoiceDAO extends BaseHibernateDAO {

    public void save(Invoice transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void delete(Invoice persistentInstance) throws Exception{
            getSession().delete(persistentInstance);
            getSession().flush();
    }

    public Invoice findById(java.lang.Integer id)  throws Exception{
            Invoice instance = (Invoice) getSession().get("com.gp.cvst.logisoft.hibernate.dao.Invoice", id);
            return instance;
    }

    public List findByExample(Invoice instance)  throws Exception{
            List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.Invoice").add(Example.create(instance)).list();
            return results;
    }

    public List findByProperty(String propertyName, Object value)  throws Exception{
            String queryString = "from Invoice as model where model." + propertyName + "= ?0";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter("0", value);
            return queryObject.list();
    }

    public Invoice merge(Invoice detachedInstance)  throws Exception{
            Invoice result = (Invoice) getSession().merge(detachedInstance);
            return result;
    }

    public void attachDirty(Invoice instance) throws Exception{
            getSession().saveOrUpdate(instance);
            getSession().flush();
    }

    public void attachClean(Invoice instance) throws Exception{
            getSession().buildLockRequest(LockOptions.NONE).lock(instance);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public List findforshowall(String invoiceno) throws Exception{

        List<invoiceBean> genericList = new ArrayList<invoiceBean>();
        invoiceBean invoicebean = null;
        String queryString = "";
        queryString = "select i.id,i.custName,i.custNo,i.custType,i.billOfLadingNo,i.invoiceNo,i.dueDate,ic.chargeCode,ic.transactionAmount from Invoice i, InvoiceCharges ic where i.invoiceNo = '" + invoiceno + "' and ic.invoiceNo='" + invoiceno + "'";
        List queryObject = getCurrentSession().createQuery(queryString).list();
            Iterator itr = queryObject.iterator();
            while (itr.hasNext()) {
                invoicebean = new invoiceBean();
                Object[] row = (Object[]) itr.next();
                Integer id = (Integer) (row[0]);
                String custName = (String) (row[1]);
                String custNum = (String) (row[2]);
                String custType = (String) (row[3]);
                String billlanding = (String) (row[4]);
                String invoice = (String) (row[5]);
                Date duedate = (Date) (row[6]);

                String chargecode = (String) (row[7]);
                Double amount = (Double) (row[8]);
                //  Date date = new Date();
                String invDate = sdf.format(duedate);

                invoicebean.setId(id);
                invoicebean.setCustName(custName);
                invoicebean.setCustNumber(custNum);
                invoicebean.setCustomerType(custType);
                invoicebean.setBlNumber(billlanding);
                invoicebean.setInvoiceNumber(invoice);
                invoicebean.setDueDate(invDate);
                invoicebean.setChargecode(chargecode);
                invoicebean.setAmount(amount);
                genericList.add(invoicebean);

                invoicebean = null;

            }
        return genericList;
    }
}
