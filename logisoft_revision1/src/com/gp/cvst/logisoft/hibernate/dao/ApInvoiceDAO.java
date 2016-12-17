package com.gp.cvst.logisoft.hibernate.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.accounting.domain.ApInvoice;
import com.gp.cvst.logisoft.struts.form.ApInquiryForm;
import org.hibernate.criterion.Order;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class ApInvoice.
 * @see com.gp.cvst.logisoft.hibernate.dao.ApInvoice
 * @author MyEclipse - Hibernate Tools
 */
public class ApInvoiceDAO extends BaseHibernateDAO {

    // property constants
    /**
     * @param transientInstance
     */
    public void save(ApInvoice apInvoice) throws Exception {
        getSession().save(apInvoice);
        getSession().flush();
    }

    /**
     * @param persistentInstance
     */
    public void delete(ApInvoice apInvoice) throws Exception {
        getSession().delete(apInvoice);
        getSession().flush();
    }

    /**
     *
     * @param updateInstance
     */
    public void saveOrUpdate(ApInvoice apInvoice) throws Exception {
        getSession().saveOrUpdate(apInvoice);
        getSession().flush();
    }

    /**
     * @param id
     * @return
     */
    public ApInvoice findById(Integer id) throws Exception {
        ApInvoice instance = (ApInvoice) getSession().get("com.logiware.accounting.domain.ApInvoice", id);
        return instance;
    }

    /**
     * @param instance
     * @return
     */
    public List findByExample(ApInvoice instance) throws Exception {
        List results = getSession().createCriteria("com.logiware.accounting.domain.ApInvoice").add(
                Example.create(instance)).list();
        return results;
    }

    /**
     * @return
     */
    public List findAll() throws Exception {
        String queryString = "from ApInvoice";
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    /**
     * @param arInvoice
     */
    public void update(ApInvoice apInvoice) throws Exception {
        getSession().update(apInvoice);
        getSession().flush();
    }

    /**
     * @param invoiceNumber
     * @param invoiceId
     * @return
     */
    public ApInvoice findByInvoiceNumber(String invoiceNumber, String invoiceId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ApInvoice.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (invoiceNumber != null && !invoiceNumber.trim().equals("")) {
            criteria.add(Restrictions.like("invoiceNumber", invoiceNumber));
        }
        if (invoiceId != null && !invoiceId.trim().equals("")) {
            criteria.add(Restrictions.like("id", new Integer(invoiceId)));
        }
        return (ApInvoice) criteria.setMaxResults(1).list();
    }

    public ApInvoice findInvoiceByInvoiceNumber(String invoiceNumber, String vendorNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder(" from ApInvoice");
        queryBuilder.append(" where invoiceNumber='").append(invoiceNumber).append("'");
        if (CommonUtils.isNotEmpty(vendorNumber)) {
            queryBuilder.append(" and accountNumber='").append(vendorNumber).append("'");
        }
        Query query = getCurrentSession().createQuery(queryBuilder.toString());
        query.setMaxResults(1);
        return (ApInvoice) query.uniqueResult();
    }

    /**
     * @param customerName
     * @param accountNumber
     * @return
     */
    public List<ApInvoice> getInvoices(String customerName, String accountNumber, String invoiceNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ApInvoice.class);
        if (CommonUtils.isNotEmpty(customerName)) {
            criteria.add(Restrictions.like("customerName", customerName.trim() + "%"));
        }
        if (CommonUtils.isNotEmpty(accountNumber)) {
            criteria.add(Restrictions.like("accountNumber", accountNumber.trim() + "%"));
        }
        if (CommonUtils.isNotEmpty(accountNumber)) {
            criteria.add(Restrictions.like("invoiceNumber", "%" + invoiceNumber.trim() + "%"));
        }
        return criteria.list();
    }

    public List<String> getAllInvoiceNumberByStatusAndAccNo(String status, String accountNumber, String invoiceNumber) {
        StringBuilder queryBuilder = new StringBuilder("SELECT invoice_number FROM ap_invoice");
        queryBuilder.append(" WHERE account_number like '").append(accountNumber).append("%'");
        if (invoiceNumber.contains("'")) {
            queryBuilder.append(" and invoice_number like \"").append(invoiceNumber).append("%\"");
        } else {
            queryBuilder.append(" and invoice_number like '").append(invoiceNumber).append("%'");
        }
        queryBuilder.append(" and (status = '").append(status).append("'");
        queryBuilder.append(" or status = '").append(CommonConstants.STATUS_DISPUTE).append("'");
        queryBuilder.append(" or status = '").append(CommonConstants.STATUS_IN_PROGRESS).append("')");
        Query queryObject = getSession().createSQLQuery(queryBuilder.toString());
        return queryObject.list();
    }

    public ApInvoice getInvoiceByInvoiceNumber(String invoiceNumber) throws Exception {
        return (ApInvoice)getSession().createCriteria(ApInvoice.class).add(Restrictions.eq("invoiceNumber", invoiceNumber)).add(Restrictions.eq("status", CommonConstants.STATUS_OPEN)).setMaxResults(1).uniqueResult();
    }

    public void updateApInvoiceStatusByInvoiceNumber(String invoiceNumber, String vendorNumber, String status) throws Exception {
        String queryString = "update ap_invoice set status = '" + status + "' where invoice_number = '" + invoiceNumber + "' and account_number = '" + vendorNumber + "'";
        if (vendorNumber == null) {
            queryString = "update ap_invoice set status = '" + status + "' where invoice_number = '" + invoiceNumber + "'";
        }
        Query queryObject = getSession().createSQLQuery(queryString);

        queryObject.executeUpdate();
    }

    public String getApContactByInvoiceNumber(String invoiceNumber) throws Exception {
        String queryString = "select vendor.acc_manager from vendor_info vendor,ap_invoice apinvoice where vendor.cust_accno=apinvoice.account_number"
                + " and apinvoice.status='Open' and apinvoice.invoice_number='" + invoiceNumber + "' limit 1";
        Query queryObject = getSession().createSQLQuery(queryString).addScalar("vendor.acc_manager", StringType.INSTANCE);
        Object invoiceNo= queryObject.uniqueResult();
        return null!=invoiceNo?invoiceNo.toString():null;
    }

    public List<ApInvoice> getInvoicesForApInquiry(ApInquiryForm apInquiryForm, String vendors) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("from ApInvoice where");
        if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getSearchBy(), CommonConstants.SEARCH_BY_INVOICE_NUMBER)
                && CommonUtils.isNotEmpty(apInquiryForm.getSearchValue())) {
            queryBuilder.append(" invoiceNumber like '%").append(apInquiryForm.getSearchValue().trim()).append("%'");
            queryBuilder.append(" and (status = '").append(CommonConstants.STATUS_IN_PROGRESS).append("'");
            queryBuilder.append(" or status = '").append(CommonConstants.STATUS_OPEN).append("'");
            queryBuilder.append(" or status = '").append(CommonConstants.STATUS_DISPUTE).append("'");
            queryBuilder.append(" or status = '").append(CommonConstants.STATUS_VOID).append("')");
        } else {
            if (CommonUtils.isNotEmpty(vendors)) {
                queryBuilder.append(" accountNumber in (").append(vendors).append(")");
            } else {
                queryBuilder.append(" accountNumber is not null");
            }
            if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), CommonConstants.ONLY)) {
                queryBuilder.append(" and (status = '").append(CommonConstants.STATUS_IN_PROGRESS).append("' or status = '").append(CommonConstants.STATUS_OPEN).append("')");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowInProgress(), CommonConstants.YES)) {
                queryBuilder.append(" and ((status = '").append(CommonConstants.STATUS_IN_PROGRESS).append("' or status = '").append(CommonConstants.STATUS_OPEN).append("')");
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), CommonConstants.YES)) {
                    queryBuilder.append(" or (status = '").append(CommonConstants.STATUS_DISPUTE).append("')");
                }
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), CommonConstants.YES)) {
                    queryBuilder.append(" or (status = '").append(CommonConstants.STATUS_VOID).append("')");
                }
                queryBuilder.append(")");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), CommonConstants.ONLY)) {
                queryBuilder.append(" and status = '").append(CommonConstants.STATUS_DISPUTE).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowDisptuedInvoices(), CommonConstants.YES)) {
                queryBuilder.append(" and ((status = '").append(CommonConstants.STATUS_DISPUTE).append("')");
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowRejectedInvoices(), CommonConstants.YES)) {
                    queryBuilder.append(" or (status = '").append(CommonConstants.STATUS_REJECT).append("')");
                }
                if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), CommonConstants.YES)) {
                    queryBuilder.append(" or (status = '").append(CommonConstants.STATUS_VOID).append("')");
                }
                queryBuilder.append(")");
            } else if (CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), CommonConstants.YES)
                    || CommonUtils.isEqualIgnoreCase(apInquiryForm.getShowVoidedInvoices(), CommonConstants.ONLY)) {
                queryBuilder.append(" and status = '").append(CommonConstants.STATUS_VOID).append("'");
            }
        }
        queryBuilder.append("  order by accountNumber,status");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public ApInvoice getInvoice(String vendorNumber, String invoiceNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ApInvoice.class);
        criteria.add(Restrictions.eq("accountNumber", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (ApInvoice) criteria.uniqueResult();
    }
}
