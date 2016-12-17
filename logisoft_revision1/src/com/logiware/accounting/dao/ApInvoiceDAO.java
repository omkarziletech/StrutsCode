package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.accounting.domain.ApInvoice;
import com.logiware.accounting.domain.ApInvoiceLineItem;
import com.logiware.accounting.form.AccrualsForm;
import com.logiware.accounting.form.ApInvoiceForm;
import com.logiware.accounting.model.ApInvoiceModel;
import com.logiware.accounting.model.LineItemModel;
import com.logiware.utils.AuditNotesUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ApInvoiceDAO extends BaseHibernateDAO implements ConstantsInterface {

    /**
     * Save record into the ap_invoice table.
     *
     * @param instance
     */
    public void save(ApInvoice instance) throws Exception {
        getCurrentSession().save(instance);
        getCurrentSession().flush();
    }

    /**
     * Update record in the ap_invoice table.
     *
     * @param instance
     */
    public void update(ApInvoice instance) throws Exception {
        getCurrentSession().update(instance);
        getCurrentSession().flush();
    }

    /**
     * delete record from the ap_invoice table.
     *
     * @param instance
     */
    public void delete(ApInvoice instance) throws Exception {
        getCurrentSession().delete(instance);
        getCurrentSession().flush();
    }

    /**
     * find record from the ap_invoice table by id.
     *
     * @param id
     * @return apInvoice
     */
    public ApInvoice findById(Integer id) throws Exception {
        getCurrentSession().flush();
        return (ApInvoice) getCurrentSession().get(ApInvoice.class, id);
    }

    public ApInvoice findById(String id) throws Exception {
        return findById(Integer.parseInt(id));
    }

    /**
     * Find duplicates
     *
     * @param vendorNumber
     * @param invoiceNumber
     * @return result
     * @throws java.lang.Exception
     */
    public List<ApInvoiceModel> findDuplicates(String vendorNumber, String invoiceNumber) throws Exception {
        String searchInvoiceNumber = invoiceNumber.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  i.`invoice_number` as invoiceNumber,");
        queryBuilder.append("  i.`status` as status,");
        queryBuilder.append("  if(i.`invoice_number` = '").append(invoiceNumber.replace("'", "\\'")).append("', 'true', 'false') as exactMatch ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    (select");
        queryBuilder.append("      i.`invoice_number`,");
        queryBuilder.append("      i.`search_invoice_number`,");
        queryBuilder.append("      if(i.`status` = '").append(STATUS_REJECT).append("', 'Reject',");
        queryBuilder.append("        if(i.`status` in ('").append(STATUS_IN_PROGRESS).append("', '").append(STATUS_OPEN).append("'),");
        queryBuilder.append("          'In Progress',");
        queryBuilder.append("          i.`status`");
        queryBuilder.append("        )");
        queryBuilder.append("      ) as status ");
        queryBuilder.append("    from");
        queryBuilder.append("      `ap_invoice` i ");
        queryBuilder.append("    where");
        queryBuilder.append("      i.`account_number` = '").append(vendorNumber).append("'");
        queryBuilder.append("      and i.`search_invoice_number` like '").append(searchInvoiceNumber).append("%'");
        queryBuilder.append("      and i.`status` <> '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append("    )");
        queryBuilder.append("    union");
        queryBuilder.append("    (select");
        queryBuilder.append("      i.`invoice_number`,");
        queryBuilder.append("      i.`search_invoice_number`,");
        queryBuilder.append("      if(i.`status` = '").append(STATUS_EDI_OPEN).append("',");
        queryBuilder.append("        '").append(STATUS_EDI_IN_PROGRESS).append("',");
        queryBuilder.append("        i.`status`");
        queryBuilder.append("      ) as status ");
        queryBuilder.append("    from");
        queryBuilder.append("      `edi_invoice` i ");
        queryBuilder.append("    where");
        queryBuilder.append("      i.`vendor_number` = '").append(vendorNumber).append("'");
        queryBuilder.append("      and i.`search_invoice_number` like '").append(searchInvoiceNumber).append("%'");
        queryBuilder.append("      and i.`status` not in (");
        queryBuilder.append("        '").append(STATUS_EDI_DUPLICATE).append("'");
        queryBuilder.append("        '").append(STATUS_EDI_ARCHIVE).append("'");
        queryBuilder.append("        '").append(STATUS_EDI_POSTED_TO_AP).append("'");
        queryBuilder.append("      )");
        queryBuilder.append("    )");
        queryBuilder.append("    union");
        queryBuilder.append("    (select");
        queryBuilder.append("      i.`invoice_number`,");
        queryBuilder.append("      i.`search_invoice_number`,");
        queryBuilder.append("      'Posted to AP' as status ");
        queryBuilder.append("    from");
        queryBuilder.append("      `transaction` i ");
        queryBuilder.append("    where");
        queryBuilder.append("      i.`cust_no` = '").append(vendorNumber).append("'");
        queryBuilder.append("      and i.`search_invoice_number` like '").append(searchInvoiceNumber).append("%'");
        queryBuilder.append("      and i.`transaction_type` = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append("      and i.`status` <> '").append(STATUS_REJECT).append("'");
        queryBuilder.append("    )");
        queryBuilder.append("    union");
        queryBuilder.append("    (select");
        queryBuilder.append("      i.`invoice_number`,");
        queryBuilder.append("      i.`search_invoice_number`,");
        queryBuilder.append("      'Posted to Negative AP' as status ");
        queryBuilder.append("    from");
        queryBuilder.append("      `transaction` i ");
        queryBuilder.append("    where");
        queryBuilder.append("      i.`cust_no` = '").append(vendorNumber).append("'");
        queryBuilder.append("      and i.`search_invoice_number` like '").append(searchInvoiceNumber).append("%'");
        queryBuilder.append("      and i.`transaction_type` = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append("      and i.`balance` <> 0.00");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as i ");
        queryBuilder.append("where");
        queryBuilder.append("  length('").append(searchInvoiceNumber).append("') / length(i.`search_invoice_number`) * 100 >= 80");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ApInvoiceModel.class));
        return query.list();
    }

    /**
     * Edit an invoice
     *
     * @param apInvoiceForm
     */
    public void editInvoice(ApInvoiceForm apInvoiceForm) throws Exception {
        if (null != apInvoiceForm) {
            ApInvoice apInvoice = findById(apInvoiceForm.getId());
            apInvoiceForm.setVendorName(apInvoice.getCustomerName());
            apInvoiceForm.setVendorNumber(apInvoice.getAccountNumber());
            apInvoiceForm.setRecurring(apInvoice.isRecurring());
            if (CommonUtils.isNotEmpty(apInvoice.getTerm())) {
                GenericCode credit = new GenericCodeDAO().findById(Integer.parseInt(apInvoice.getTerm()));
                apInvoiceForm.setCreditTerm(Integer.parseInt(credit.getCode()));
                apInvoiceForm.setCreditDesc(credit.getCodedesc());
                apInvoiceForm.setCreditId(credit.getId());
            } else {
                GenericCode credit = new GenericCodeDAO().findByCodeDesc("Due Upon Receipt");
                apInvoiceForm.setCreditTerm(Integer.parseInt(credit.getCode()));
                apInvoiceForm.setCreditDesc(credit.getCodedesc());
                apInvoiceForm.setCreditId(credit.getId());
            }
            if (!apInvoice.isRecurring()) {
                apInvoiceForm.setInvoiceNumber(apInvoice.getInvoiceNumber());
                apInvoiceForm.setInvoiceDate(DateUtils.formatDate(apInvoice.getDate(), "MM/dd/yyyy"));
                apInvoiceForm.setDueDate(calculateDueDate(apInvoiceForm.getInvoiceDate(), apInvoiceForm.getCreditTerm()));
                apInvoiceForm.setForComments(apInvoice.getNotes());
            }
            apInvoiceForm.setId(apInvoice.getId());
            apInvoiceForm.setStatus(apInvoice.getStatus());
            apInvoiceForm.setInvoiceAmount(NumberUtils.formatNumber(apInvoice.getInvoiceAmount()));
            List<LineItemModel> lineItems = new ArrayList<LineItemModel>();
            if (null != apInvoice.getLineItems()) {
                for (ApInvoiceLineItem lineItem : apInvoice.getLineItems()) {
                    lineItems.add(new LineItemModel(lineItem));
                }
            }
            apInvoiceForm.setLineItems(lineItems);
            apInvoiceForm.setLineItem(new LineItemModel());
            if (CommonUtils.isEqualIgnoreCase(apInvoice.getStatus(), TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
                apInvoiceForm.setMode(VIEW_MODE);
            } else {
                apInvoiceForm.setMode(PARTIAL_MODE);
            }
            if (CommonUtils.isEqualIgnoreCase(apInvoice.getStatus(), STATUS_REJECT)) {
                apInvoiceForm.setReject(true);
            }
        }
    }

    public ApInvoice save(ApInvoiceForm apInvoiceForm, User user) throws Exception {
        ApInvoice apInvoice = new ApInvoice();
        apInvoice.setCustomerName(apInvoiceForm.getVendorName());
        apInvoice.setAccountNumber(apInvoiceForm.getVendorNumber());
        if (!apInvoiceForm.isRecurring()) {
            apInvoice.setInvoiceNumber(apInvoiceForm.getInvoiceNumber().trim());
            apInvoice.setDate(DateUtils.parseDate(apInvoiceForm.getInvoiceDate(), "MM/dd/yyyy"));
            apInvoice.setDueDate(DateUtils.parseDate(apInvoiceForm.getDueDate(), "MM/dd/yyyy"));
            apInvoice.setTerm(null != apInvoiceForm.getCreditId() ? apInvoiceForm.getCreditId().toString() : null);
            apInvoice.setNotes(apInvoiceForm.getForComments().trim());
            StringBuilder desc = new StringBuilder();
            desc.append("Invoice - ").append(apInvoiceForm.getInvoiceNumber().trim());
            desc.append(" of ").append(apInvoiceForm.getVendorName());
            desc.append(" (").append(apInvoiceForm.getVendorNumber()).append(")");
            desc.append(" created by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            String moduleId = NotesConstants.AP_INVOICE;
            String moduleRefId = apInvoiceForm.getVendorNumber() + "-" + apInvoiceForm.getInvoiceNumber().trim();
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
            if (CommonUtils.isNotEmpty(apInvoiceForm.getForComments().trim())) {
                AuditNotesUtils.insertAuditNotes("For : " + apInvoiceForm.getForComments(), moduleId, moduleRefId, moduleId, user);
            }
        }
        apInvoice.setRecurring(apInvoiceForm.isRecurring());
        apInvoice.setStatus(STATUS_IN_PROGRESS);
        apInvoice.setDirectGlAccount(true);
        double invoiceAmount = 0d;
        if (CommonUtils.isNotEmpty(apInvoiceForm.getLineItem().getGlAccount())) {
            ApInvoiceLineItem lineItem = new ApInvoiceLineItem(apInvoiceForm.getLineItem());
            lineItem.setApInvoice(apInvoice);
            List<ApInvoiceLineItem> lineItems = new ArrayList<ApInvoiceLineItem>();
            lineItems.add(lineItem);
            apInvoice.setLineItems(lineItems);
            invoiceAmount += lineItem.getAmount();
        }
        apInvoice.setInvoiceAmount(invoiceAmount);
        save(apInvoice);
        return apInvoice;
    }

    public ApInvoice update(ApInvoiceForm apInvoiceForm, User user) throws Exception {
        ApInvoice apInvoice = findById(apInvoiceForm.getId());
        if (!apInvoiceForm.isRecurring()) {
            apInvoice.setInvoiceNumber(apInvoiceForm.getInvoiceNumber().trim());
            apInvoice.setDate(DateUtils.parseDate(apInvoiceForm.getInvoiceDate(), "MM/dd/yyyy"));
            apInvoice.setDueDate(DateUtils.parseDate(apInvoiceForm.getDueDate(), "MM/dd/yyyy"));
            apInvoice.setTerm(null != apInvoiceForm.getCreditId() ? apInvoiceForm.getCreditId().toString() : null);
            apInvoice.setNotes(apInvoiceForm.getForComments().trim());
            if (CommonUtils.isNotEqual(apInvoice.getNotes(), apInvoiceForm.getForComments().trim())
                    && CommonUtils.isNotEmpty(apInvoiceForm.getForComments().trim())) {
                String moduleId = NotesConstants.AP_INVOICE;
                String moduleRefId = apInvoiceForm.getVendorNumber() + "-" + apInvoiceForm.getInvoiceNumber().trim();
                AuditNotesUtils.insertAuditNotes("For : " + apInvoiceForm.getForComments(), moduleId, moduleRefId, moduleId, user);
            }
        }
        apInvoice.setRecurring(apInvoiceForm.isRecurring());
        apInvoice.setStatus(STATUS_IN_PROGRESS);
        apInvoice.setDirectGlAccount(true);
        double invoiceAmount = 0d;
        if (null != apInvoice.getLineItems()) {
            for (ApInvoiceLineItem lineItem : apInvoice.getLineItems()) {
                invoiceAmount += lineItem.getAmount();
            }
        }
        if (CommonUtils.isNotEmpty(apInvoiceForm.getLineItem().getGlAccount())) {
            ApInvoiceLineItem lineItem = new ApInvoiceLineItem(apInvoiceForm.getLineItem());
            lineItem.setApInvoice(apInvoice);
            if (null != apInvoice.getLineItems()) {
                apInvoice.getLineItems().add(lineItem);
            } else {
                List<ApInvoiceLineItem> lineItems = new ArrayList<ApInvoiceLineItem>();
                lineItems.add(lineItem);
                apInvoice.setLineItems(lineItems);
            }
            invoiceAmount += lineItem.getAmount();
        }
        apInvoice.setInvoiceAmount(invoiceAmount);
        update(apInvoice);
        return apInvoice;
    }

    /**
     * Save or update ApInvoice object
     *
     * @param apInvoiceForm
     * @param request
     */
    public void saveOrUpdate(ApInvoiceForm apInvoiceForm, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute(LOGIN_USER);
        if (CommonUtils.isEmpty(apInvoiceForm.getId())) {
            ApInvoice apInvoice = save(apInvoiceForm, user);
            apInvoiceForm.setId(apInvoice.getId());
        } else {
            update(apInvoiceForm, user);
        }
        getCurrentSession().flush();
        editInvoice(apInvoiceForm);
    }

    /**
     * Remove line item
     *
     * @param apInvoiceForm
     * @param request
     */
    public void removeLineItem(ApInvoiceForm apInvoiceForm, HttpServletRequest request) throws Exception {
        ApInvoice apInvoice = findById(apInvoiceForm.getId());
        apInvoice.setStatus(STATUS_IN_PROGRESS);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete from ApInvoiceLineItem");
        queryBuilder.append(" where id=").append(apInvoiceForm.getLineItemId());
        getCurrentSession().createQuery(queryBuilder.toString()).executeUpdate();
        getCurrentSession().refresh(apInvoice);
        double invoiceAmount = 0d;
        for (Iterator<ApInvoiceLineItem> iterator = apInvoice.getLineItems().iterator(); iterator.hasNext();) {
            ApInvoiceLineItem lineItem = iterator.next();
            invoiceAmount += lineItem.getAmount();
        }
        apInvoice.setInvoiceAmount(invoiceAmount);
        update(apInvoice);
        getCurrentSession().flush();
        editInvoice(apInvoiceForm);
    }

    /**
     * Delete an invoice
     *
     * @param id
     */
    public void delete(Integer id) throws Exception {
        ApInvoice apInvoice = findById(id);
        delete(apInvoice);
        getCurrentSession().flush();
    }

    /**
     * Reject an invoice
     *
     * @param apInvoiceForm
     * @param user
     */
    public void reject(ApInvoiceForm apInvoiceForm, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute(LOGIN_USER);
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = apInvoiceForm.getVendorNumber() + "-" + apInvoiceForm.getInvoiceNumber();
        ApInvoice apInvoice;
        if (CommonUtils.isEmpty(apInvoiceForm.getId())) {
            apInvoice = save(apInvoiceForm, user);
        } else {
            apInvoice = update(apInvoiceForm, user);
        }
        apInvoice.setStatus(STATUS_REJECT);
        apInvoice.setDirectGlAccount(true);
        apInvoice.setInvoiceAmount(0d);
        apInvoice.getLineItems().clear();
        update(apInvoice);
        getCurrentSession().flush();

        StringBuilder desc = new StringBuilder();
        desc.append("Invoice - ").append(apInvoiceForm.getInvoiceNumber());
        desc.append(" of ").append(apInvoiceForm.getVendorName());
        desc.append(" (").append(apInvoiceForm.getVendorNumber()).append(")");
        desc.append(" rejected by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
    }

    /**
     * Unreject an invoice
     *
     * @param apInvoiceForm
     * @param user
     */
    public void unreject(ApInvoiceForm apInvoiceForm, User user) throws Exception {
        ApInvoice apInvoice = update(apInvoiceForm, user);
        apInvoice.setStatus(STATUS_IN_PROGRESS);
        update(apInvoice);

        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = apInvoiceForm.getVendorNumber() + "-" + apInvoiceForm.getInvoiceNumber();

        StringBuilder desc = new StringBuilder();
        desc.append("Invoice - ").append(apInvoiceForm.getInvoiceNumber());
        desc.append(" of ").append(apInvoiceForm.getVendorName());
        desc.append(" (").append(apInvoiceForm.getVendorNumber()).append(")");
        desc.append(" unrejected by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
    }

    public void duplicate(ApInvoiceForm apInvoiceForm, ApInvoice originalInvoice) throws Exception {
        ApInvoice apInvoice = new ApInvoice();
        apInvoice.setCustomerName(originalInvoice.getCustomerName());
        apInvoice.setAccountNumber(originalInvoice.getAccountNumber());
        apInvoice.setRecurring(true);
        apInvoice.setStatus(STATUS_IN_PROGRESS);
        apInvoice.setDirectGlAccount(true);
        double invoiceAmount = 0d;
        if (CommonUtils.isNotEmpty(originalInvoice.getLineItems())) {
            List<ApInvoiceLineItem> lineItems = new ArrayList<ApInvoiceLineItem>();
            for (ApInvoiceLineItem originalLineItem : originalInvoice.getLineItems()) {
                ApInvoiceLineItem lineItem = (ApInvoiceLineItem) BeanUtils.cloneBean(originalLineItem);
                lineItem.setId(null);
                lineItem.setApInvoice(apInvoice);
                lineItems.add(lineItem);
                invoiceAmount += lineItem.getAmount();
            }
            apInvoice.setLineItems(lineItems);
        }
        apInvoice.setInvoiceAmount(invoiceAmount);
        save(apInvoice);
        apInvoiceForm.setId(apInvoice.getId());
    }

    /**
     * Post an invoice
     *
     * @param apInvoiceForm
     * @param user
     */
    public void post(ApInvoiceForm apInvoiceForm, User user) throws Exception {
        boolean isRecurring = apInvoiceForm.isRecurring();
        apInvoiceForm.setRecurring(false);
        ApInvoice apInvoice;
        if (CommonUtils.isEmpty(apInvoiceForm.getId())) {
            apInvoice = save(apInvoiceForm, user);
        } else {
            apInvoice = update(apInvoiceForm, user);
        }
        apInvoice.setStatus(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        update(apInvoice);
        new AccrualsDAO().postToAp(apInvoice, user);
        if (isRecurring) {
            duplicate(apInvoiceForm, apInvoice);
            apInvoiceForm.setRecurring(true);
        }
    }

    /**
     * Build and return the query conditions for searching invoices
     *
     * @param apInvoiceForm
     * @return conditions
     */
    private String buildConditions(ApInvoiceForm apInvoiceForm) {
        StringBuilder conditionsBuilder = new StringBuilder();
        conditionsBuilder.append(" from ap_invoice inv");
        conditionsBuilder.append(" join trading_partner tp");
        conditionsBuilder.append(" on (inv.account_number = tp.acct_no");
        if (CommonUtils.isNotEmpty(apInvoiceForm.getVendorNumber())) {
            conditionsBuilder.append(" and tp.acct_no = '").append(apInvoiceForm.getVendorNumber()).append("'");
        }
        conditionsBuilder.append(")");
        conditionsBuilder.append(" where inv.direct_gl_account = true");
        if (apInvoiceForm.isRecurring()) {
            conditionsBuilder.append(" and inv.recurring = true");
            if (null != apInvoiceForm.getInvoiceAmount()
                    && CommonUtils.isNotEqual(apInvoiceForm.getInvoiceAmount().replace(",", ""), "0.00")) {
                conditionsBuilder.append(" and inv.invoice_amount = ").append(apInvoiceForm.getInvoiceAmount().replace(",", ""));
            }
            conditionsBuilder.append(" and (inv.invoice_number = '' or inv.invoice_number is null)");
            conditionsBuilder.append(" and inv.status = '").append(STATUS_IN_PROGRESS).append("'");
        } else {
            conditionsBuilder.append(" and inv.recurring = false");
            conditionsBuilder.append(" and inv.date is not null");
            if (CommonUtils.isNotEmpty(apInvoiceForm.getInvoiceNumber())) {
                conditionsBuilder.append(" and inv.invoice_number = '").append(apInvoiceForm.getInvoiceNumber()).append("'");
            } else if (CommonUtils.isNotEmpty(apInvoiceForm.getStatus())) {
                conditionsBuilder.append(" and inv.status = '").append(apInvoiceForm.getStatus()).append("'");
            } else {
                conditionsBuilder.append(" and inv.status = '").append(STATUS_IN_PROGRESS).append("'");
            }
        }
        return conditionsBuilder.toString();
    }

    /**
     * Get total number of rows
     *
     * @param conditions
     * @return total number of rows
     */
    private Integer getTotalNumberOfRows(String conditions) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*)");
        queryBuilder.append(conditions);
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    /**
     * Return ApInvoiceModel objects of invoices
     *
     * @param conditions
     * @param sortBy
     * @param orderBy
     * @param start
     * @param limit
     * @return invoices
     */
    public List<ApInvoiceModel> getInvoices(String conditions, String sortBy, String orderBy, Integer start, Integer limit) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as vendorName,");
        queryBuilder.append("tp.acct_no as vendorNumber,");
        queryBuilder.append("upper(inv.invoice_number) as invoiceNumber,");
        queryBuilder.append("date_format(inv.date,'%m/%d/%Y') as invoiceDate,");
        queryBuilder.append("format(inv.invoice_amount,2) as invoiceAmount,");
        queryBuilder.append("if(inv.status='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("','Posted to AP',");
        queryBuilder.append("if(inv.status='").append(STATUS_REJECT).append("','Rejected','In Progress')) as status,");
        queryBuilder.append("cast(inv.id as char character set latin1) as id");
        queryBuilder.append(conditions);
        queryBuilder.append(" order by ").append(sortBy).append(" ").append(orderBy);
        queryBuilder.append(" limit ").append(start).append(" , ").append(limit);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ApInvoiceModel.class));
        return query.list();
    }

    /**
     * Search invoices
     *
     * @param apInvoiceForm
     */
    public void search(ApInvoiceForm apInvoiceForm) {
        String conditions = buildConditions(apInvoiceForm);
        int totalRows = getTotalNumberOfRows(conditions);
        if (totalRows > 0) {
            apInvoiceForm.setTotalRows(totalRows);
            Integer limit = apInvoiceForm.getLimit();
            Integer totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            apInvoiceForm.setTotalPages(totalPages);
            Integer start = limit * (apInvoiceForm.getSelectedPage() - 1);
            List<ApInvoiceModel> invoices = getInvoices(conditions, apInvoiceForm.getSortBy(), apInvoiceForm.getOrderBy(), start, limit);
            apInvoiceForm.setInvoices(invoices);
            apInvoiceForm.setSelectedRows(invoices.size());
        }
    }

    /**
     * Return due date calculated from the invoice date and credit term
     *
     * @param invoiceDate
     * @param creditTerm
     * @return due date
     */
    public String calculateDueDate(String invoiceDate, Integer creditTerm) throws Exception {
        return DateUtils.formatDate(DateUtils.addDays(DateUtils.parseDate(invoiceDate, "MM/dd/yyyy"), creditTerm), "MM/dd/yyyy");
    }

    /**
     * Return boolean of document uploaded for the given vendor and it's invoice
     *
     * @param vendorNumber
     * @param invoiceNumber
     * @return true or false
     */
    public boolean validateUploads(String vendorNumber, String invoiceNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,'true','false') as result");
        queryBuilder.append(" from document_store_log");
        queryBuilder.append(" where screen_name='INVOICE'");
        queryBuilder.append(" and document_name='INVOICE'");
        queryBuilder.append(" and document_id='").append(vendorNumber).append("-").append(invoiceNumber).append("'");
        String result = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Boolean.valueOf(result);
    }

    public ApInvoice getInvoice(String vendorNumber, String invoiceNumber, Boolean directGlAccount, boolean notPostedToAp) {
        Criteria criteria = getCurrentSession().createCriteria(ApInvoice.class);
        criteria.add(Restrictions.eq("accountNumber", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        if (directGlAccount != null) {
            criteria.add(Restrictions.eq("directGlAccount", directGlAccount));
        }
        if (notPostedToAp) {
            Disjunction disjunction = Restrictions.disjunction();
            disjunction.add(Restrictions.eq("status", STATUS_OPEN));
            disjunction.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
            disjunction.add(Restrictions.eq("status", STATUS_DISPUTE));
            disjunction.add(Restrictions.eq("status", STATUS_REJECT));
            criteria.add(disjunction);
        }
        return (ApInvoice) criteria.uniqueResult();
    }

    public ApInvoice getInvoice(String vendorNumber, String invoiceNumber, String status) {
        Criteria criteria = getCurrentSession().createCriteria(ApInvoice.class);
        criteria.add(Restrictions.eq("accountNumber", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.add(Restrictions.eq("status", status));
        return (ApInvoice) criteria.uniqueResult();
    }

    public boolean isOpenInvoice(String vendorNumber, String invoiceNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, 0, 1) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction ");
        queryBuilder.append("where cust_no = '").append(vendorNumber).append("'");
        queryBuilder.append("  and invoice_number = '").append(invoiceNumber).append("'");
        queryBuilder.append("  and transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append("  and status != '").append(STATUS_REJECT).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public ApInvoice createInvoice(AccrualsForm accrualsForm, User user) throws Exception {
        ApInvoice apInvoice = new ApInvoice();
        apInvoice.setCustomerName(accrualsForm.getVendorName());
        apInvoice.setAccountNumber(accrualsForm.getVendorNumber());
        apInvoice.setInvoiceNumber(accrualsForm.getInvoiceNumber());
        apInvoice.setStatus(STATUS_OPEN);
        apInvoice.setDirectGlAccount(false);
        apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
        apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDueDate(), "MM/dd/yyyy"));
        apInvoice.setTerm(null != accrualsForm.getCreditId() ? accrualsForm.getCreditId().toString() : null);
        apInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        save(apInvoice);
        getCurrentSession().flush();
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
        StringBuilder desc = new StringBuilder();
        desc.append("Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
        desc.append(" of ").append(accrualsForm.getVendorName());
        desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
        desc.append(" created by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        return apInvoice;
    }
}
