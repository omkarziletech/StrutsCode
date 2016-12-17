package com.gp.cvst.logisoft.hibernate.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.domain.ArInvoice;
import org.hibernate.type.IntegerType;

/**
 * A data access object (DAO) providing persistence and search support for
 * ArInvoice entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see dd.ArInvoice
 * @author MyEclipse Persistence Tools
 */
public class ArInvoiceDAO extends BaseHibernateDAO {

    /**
     * @param arInvoice
     */
    public void save(ArInvoice arInvoice) throws Exception {
        getSession().saveOrUpdate(arInvoice);
        getSession().flush();
    }

    /**
     * @param arInvoice
     */
    public void delete(ArInvoice arInvoice) throws Exception {
        getSession().delete(arInvoice);
        getSession().flush();
    }

    /**
     * @param arInvoice
     */
    public void update(ArInvoice arInvoice) throws Exception {
        getSession().update(arInvoice);
        getSession().flush();
    }

    /**
     * @param id
     * @return
     */
    public ArInvoice findById(Integer id) throws Exception {
        ArInvoice instance = (ArInvoice) getSession().get("com.gp.cvst.logisoft.domain.ArInvoice",
                id);
        return instance;
    }

    /**
     * @param customerName
     * @param accountNumber
     * @return
     */
    public List findByCustomerAndAccount(String customerName, String accountNumber, String invoiceNumber, String invoiceAmount, String fromDate, String toDate) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ArInvoice.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (customerName != null && !customerName.equals("")) {
            criteria.add(Restrictions.like("customerName", customerName + "%"));
        }
        if (accountNumber != null && !accountNumber.equals("")) {
            criteria.add(Restrictions.like("accountNumber", accountNumber + "%"));
        }
        if (invoiceNumber != null && !invoiceNumber.equals("")) {
            criteria.add(Restrictions.like("invoiceNumber", "%" + invoiceNumber + "%"));
        }

        if (fromDate != null && !fromDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date soStartDate = (Date) dateFormat.parse(fromDate);
            criteria.add(Restrictions.ge("date", soStartDate));
            criteria.addOrder(Order.asc("date"));
        }
        if (toDate != null && !toDate.equals("")) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date soEndDate = (Date) dateFormat.parse(toDate);
            criteria.add(Restrictions.le("date", soEndDate));
            criteria.addOrder(Order.asc("date"));
        }
        criteria.addOrder(Order.desc("id"));
        return criteria.list();
    }

    /**
     * @return
     */
    public List findAll() throws Exception {
        String queryString = "from ArInvoice";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    /**
     * @param invoiceNumber
     * @param invoiceId
     * @return
     */
    public ArInvoice findByInvoiceNumber(String invoiceNumber, String invoiceId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ArInvoice.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (invoiceNumber != null && !invoiceNumber.equals("")) {
            criteria.add(Restrictions.like("invoiceNumber", invoiceNumber + "%"));
        }
        if (invoiceId != null && !invoiceId.equals("")) {
            criteria.add(Restrictions.like("id", new Integer(invoiceId)));
        }
        return (ArInvoice)criteria.setMaxResults(1).uniqueResult();
    }

    public String generateInvoiceNumber() throws Exception {
        String invoice = AccountingConstants.INVOICE_STARTING_NUMBER;
        Integer inv;
        String query = "select max(cast(invoice_number as decimal))as invoice from ar_invoice";
        Query queryString = getCurrentSession().createSQLQuery(query).addScalar("invoice", IntegerType.INSTANCE);
        inv = (Integer) queryString.uniqueResult();
        if (inv == null) {
            invoice = AccountingConstants.INVOICE_STARTING_NUMBER;
        } else {
            inv = inv + 1;
            invoice = inv.toString();
        }
        return invoice;
    }
}
