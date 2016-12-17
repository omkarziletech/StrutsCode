package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.TRANSACTION_TYPE_IN_PROGRESS;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.jobscheduler.JobScheduler;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.DocumentStoreLog;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.struts.form.lcl.EculineInvoiceForm;
import com.logiware.accounting.domain.EdiInvoice;
import com.logiware.accounting.domain.EdiInvoiceBank;
import com.logiware.accounting.domain.EdiInvoiceContainer;
import com.logiware.accounting.domain.EdiInvoiceDetail;
import com.logiware.accounting.domain.EdiInvoiceLog;
import com.logiware.accounting.domain.EdiInvoiceParty;
import com.logiware.accounting.domain.EdiInvoiceShippingDetails;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.accounting.form.EdiInvoiceForm;
import com.logiware.accounting.model.AccrualModel;
import com.logiware.accounting.model.EdiInvoiceCharges;
import com.logiware.accounting.model.EdiInvoiceLogModel;
import com.logiware.accounting.model.EdiInvoiceModel;
import com.logiware.accounting.model.VendorModel;
import com.logiware.accounting.reports.EdiInvoiceCreator;
import com.logiware.accounting.utils.BeanUtils;
import com.logiware.utils.AuditNotesUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Naryanan
 */
public class EdiInvoiceDAO extends BaseHibernateDAO implements ConstantsInterface {

    private static final Logger log = Logger.getLogger(EdiInvoiceDAO.class);

    /**
     * Save record into the edi_invoice_log table.
     *
     * @param instance
     */
    public void save(EdiInvoiceLog instance) throws Exception {
        getCurrentSession().save(instance);
        getCurrentSession().flush();
    }

    /**
     * Save record into the edi_invoice table.
     *
     * @param instance
     */
    public void save(EdiInvoice instance) throws Exception {
        getCurrentSession().save(instance);
        getCurrentSession().flush();
    }

    /**
     * Update record in the edi_invoice table.
     *
     * @param instance
     */
    public void update(EdiInvoice instance) throws Exception {
        getCurrentSession().update(instance);
        getCurrentSession().flush();
    }

    /**
     * delete record from the edi_invoice table.
     *
     * @param instance
     */
    public void delete(EdiInvoice instance) throws Exception {
        getCurrentSession().delete(instance);
        getCurrentSession().flush();
    }

    /**
     * find record from the edi_invoice table by id.
     *
     * @param id
     * @return ediInvoice
     */
    public EdiInvoice findById(Integer id) throws Exception {
        getCurrentSession().flush();
        return (EdiInvoice) getCurrentSession().get(EdiInvoice.class, id);
    }

    public EdiInvoice findById(String id) throws Exception {
        return findById(Integer.parseInt(id));
    }

    /**
     * find record from the edi_invoice_log table by id.
     *
     * @param id
     * @return ediInvoiceLog
     */
    public EdiInvoiceLog getEdiInvoiceLog(Integer id) throws Exception {
        getCurrentSession().flush();
        return (EdiInvoiceLog) getCurrentSession().get(EdiInvoiceLog.class, id);
    }

    public List<EdiInvoice> getAllInvoices() throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(EdiInvoice.class);
        criteria.add(Restrictions.ne("invoiceNumber", ""));
        return criteria.list();
    }

    public List<EdiInvoice> getEmptyEdiCodeInvoices() throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(EdiInvoice.class);
        criteria.add(Restrictions.or(Restrictions.eq("ediCode", ""), Restrictions.isNull("ediCode")));
        return criteria.list();
    }

    public EdiInvoice getInvoice(String vendorNumber, String invoiceNumber, String status) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(EdiInvoice.class);
        criteria.add(Restrictions.eq("vendorNumber", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        if (CommonUtils.isNotEmpty(status)) {
            criteria.add(Restrictions.eq("status", status));
        } else {
            criteria.add(Restrictions.ne("status", STATUS_EDI_ARCHIVE));
            criteria.add(Restrictions.ne("status", STATUS_EDI_DUPLICATE));
            criteria.add(Restrictions.ne("status", STATUS_EDI_POSTED_TO_AP));
        }
        criteria.setMaxResults(1);
        return (EdiInvoice) criteria.uniqueResult();
    }

    public boolean isOpenInvoice(String vendorNumber, String invoiceNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, 1, 0) as result ");
        queryBuilder.append("from");
        queryBuilder.append("  edi_invoice ");
        queryBuilder.append("where vendor_number = '").append(vendorNumber).append("' ");
        queryBuilder.append("  and invoice_number = '").append(invoiceNumber).append("'");
        queryBuilder.append("  and status != '").append(STATUS_EDI_POSTED_TO_AP).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("result", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    /**
     * get vendor from the vendor_info and trading_partner table by edi_code.
     *
     * @param ediCode
     * @return vendor
     */
    public VendorModel getVendor(String ediCode) throws Exception {
        getCurrentSession().flush();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as vendorName,");
        queryBuilder.append("tp.acct_no as vendorNumber");
        queryBuilder.append(" from vendor_info ve");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on tp.acct_no=ve.cust_accno");
        queryBuilder.append(" where ve.edi_code='").append(ediCode).append("'");
        queryBuilder.append(" order by ve.id limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(VendorModel.class));
        return (VendorModel) query.uniqueResult();
    }

    /**
     * get status for the invoice from transaction/ap_invoice tables
     *
     * @param vendorNumber
     * @param invoiceNumber
     * @return status
     */
    public String getStatus(String vendorNumber, String invoiceNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(if(sum(t.count)>0,");
        queryBuilder.append("'").append(STATUS_EDI_DUPLICATE).append("',");
        queryBuilder.append("'").append(STATUS_EDI_OPEN).append("') as char character set latin1) as status");
        queryBuilder.append(" from (");
        queryBuilder.append("(select count(*) as count");
        queryBuilder.append(" from transaction");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" and status !='").append(STATUS_REJECT).append("'");
        queryBuilder.append(" and cust_no='").append(vendorNumber).append("'");
        queryBuilder.append(" and invoice_number='").append(invoiceNumber).append("')");
        queryBuilder.append(" union ");
        queryBuilder.append("(select count(*) as count");
        queryBuilder.append(" from ap_invoice");
        queryBuilder.append(" where account_number='").append(vendorNumber).append("'");
        queryBuilder.append(" and invoice_number='").append(invoiceNumber).append("')");
        queryBuilder.append(" union ");
        queryBuilder.append("(select count(*) as count");
        queryBuilder.append(" from edi_invoice");
        queryBuilder.append(" where vendor_number='").append(vendorNumber).append("'");
        queryBuilder.append(" and invoice_number='").append(invoiceNumber).append("')");
        queryBuilder.append(") as t");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
    }

    /**
     * build conditions to search edi_invoice table
     *
     * @param ediInvoiceForm
     * @return conditions
     */
    private String buildConditions(EdiInvoiceForm ediInvoiceForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from edi_invoice edi");
        if (CommonUtils.isNotEmpty(ediInvoiceForm.getVendorNumber())) {
            queryBuilder.append(" where edi.vendor_number = '").append(ediInvoiceForm.getVendorNumber()).append("'");
        } else {
            queryBuilder.append(" where edi.invoice_number != ''");
        }
        if (CommonUtils.isNotEmpty(ediInvoiceForm.getInvoiceNumber())) {
            queryBuilder.append(" and edi.invoice_number = '").append(ediInvoiceForm.getInvoiceNumber()).append("'");
        }
        if (CommonUtils.isNotEmpty(ediInvoiceForm.getStatus())) {
            queryBuilder.append(" and edi.status = '").append(ediInvoiceForm.getStatus()).append("'");
        } else {
            queryBuilder.append(" and edi.status != '").append(STATUS_EDI_POSTED_TO_AP).append("'");
            queryBuilder.append(" and edi.status != '").append(STATUS_EDI_ARCHIVE).append("'");
        }
        return queryBuilder.toString();
    }

    /**
     * get total number of rows for the given condition from edi_invoice table
     *
     * @param condition
     * @return total number of rows
     */
    private Integer getTotalNumberOfRows(String condition) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*)");
        queryBuilder.append(condition);
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    /**
     * get invoices for the given condition and limit from edi_invoice table
     *
     * @param conditions
     * @param sortBy
     * @param orderBy
     * @param start
     * @param limit
     * @return invoices
     */
    private List<EdiInvoiceModel> getInvoices(String conditions, String sortBy, String orderBy, Integer start, Integer limit) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select edi.vendor_name as vendorName,");
        queryBuilder.append("edi.vendor_number as vendorNumber,");
        queryBuilder.append("edi.invoice_number as invoiceNumber,");
        queryBuilder.append("date_format(edi.invoice_date,'%m/%d/%Y') as invoiceDate,");
        queryBuilder.append("format(edi.invoice_amount,2) as invoiceAmount,");
        queryBuilder.append("edi.status as status,");
        queryBuilder.append("edi.id as id,");
        queryBuilder.append("edi.edi_invoice_log_id as logId");
        queryBuilder.append(conditions);
        queryBuilder.append(" order by edi.").append(sortBy).append(" ").append(orderBy);
        queryBuilder.append(" limit ").append(start).append(",").append(limit);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(EdiInvoiceModel.class));
        return query.list();
    }

    /**
     * search invoices in edi_invoice table
     *
     * @param form
     */
    public void search(EdiInvoiceForm form) throws Exception {
        String conditions = buildConditions(form);
        int totalRows = getTotalNumberOfRows(conditions);
        if (totalRows > 0) {
            form.setTotalRows(totalRows);
            Integer limit = form.getLimit();
            Integer totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            form.setTotalPages(totalPages);
            Integer start = limit * (form.getSelectedPage() - 1);
            List<EdiInvoiceModel> invoices = getInvoices(conditions, form.getSortBy(), form.getOrderBy(), start, limit);
            form.setInvoices(invoices);
            form.setSelectedRows(invoices.size());
        }
    }

    /**
     * Update vendor in edi_invoice and transaction_ledger table
     *
     * @param id
     * @param vendorNumber
     */
    public void updateVendor(EdiInvoice ediInvoice, String vendorNumber, String vendorName) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger");
        queryBuilder.append(" set cust_no='").append(vendorNumber).append("',");
        queryBuilder.append("cust_name='").append(vendorName).append("'");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and status='").append(STATUS_EDI_IN_PROGRESS).append("'");
        queryBuilder.append(" and cust_no='").append(ediInvoice.getVendorNumber()).append("'");
        queryBuilder.append(" and invoice_number='").append(ediInvoice.getInvoiceNumber()).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder("update document_store_log");
        queryBuilder.append(" set Document_ID='").append(vendorNumber).append("-").append(ediInvoice.getInvoiceNumber()).append("'");
        queryBuilder.append(" where Document_ID='").append(ediInvoice.getVendorNumber()).append("-").append(ediInvoice.getInvoiceNumber()).append("'");
        queryBuilder.append(" and Screen_Name='INVOICE'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        queryBuilder = new StringBuilder("update notes");
        queryBuilder.append(" set module_ref_id='").append(vendorNumber).append("-").append(ediInvoice.getInvoiceNumber()).append("'");
        queryBuilder.append(" where module_ref_id='").append(ediInvoice.getVendorNumber()).append("-").append(ediInvoice.getInvoiceNumber()).append("'");
        queryBuilder.append(" and module_id='").append(NotesConstants.AP_INVOICE).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public List<AccrualModel> getAccruals(String vendorNumber, String invoiceNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select charge_code as costCode,");
        queryBuilder.append("gl_account_number as glAccount,");
        queryBuilder.append("bill_ladding_no as blNumber,");
        queryBuilder.append("container_no as containerNumber,");
        queryBuilder.append("voyage_no as voyageNumber,");
        queryBuilder.append("drcpt as dockReceipt,");
        queryBuilder.append("date_format(sailing_date,'%m/%d/%Y') as reportingDate,");
        queryBuilder.append("format(transaction_amt,2) as amount,");
        queryBuilder.append("bluescreen_chargecode as bluescreenCostCode,");
        queryBuilder.append("shipment_type as shipmentType,");
        queryBuilder.append("transaction_id as id,");
        queryBuilder.append("terminal AS terminal");
        queryBuilder.append(" from transaction_ledger");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and (status='").append(STATUS_EDI_IN_PROGRESS).append("'");
        queryBuilder.append(" or status='").append(STATUS_EDI_DISPUTE).append("')");
        queryBuilder.append(" and cust_no='").append(vendorNumber).append("'");
        queryBuilder.append(" and invoice_number='").append(invoiceNumber).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(AccrualModel.class));
        return query.list();
    }

    public List<EdiInvoice> getAllOpenInvoices() throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(EdiInvoice.class);
        criteria.add(Restrictions.ne("invoiceNumber", ""));
        criteria.add(Restrictions.ne("status", STATUS_EDI_DISPUTE));
        criteria.add(Restrictions.ne("status", STATUS_EDI_DUPLICATE));
        criteria.add(Restrictions.ne("status", STATUS_EDI_READY_TO_POST_FULLY_MAPPED));
        criteria.add(Restrictions.ne("status", STATUS_EDI_POSTED_TO_AP));
        criteria.add(Restrictions.ne("status", STATUS_EDI_ARCHIVE));
        return criteria.list();
    }

    public List<Integer> getAllOpenInvoiceIds() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  id ");
        queryBuilder.append("from");
        queryBuilder.append("  edi_invoice ");
        queryBuilder.append("where invoice_number <> '' ");
        queryBuilder.append("  and status in (");
        queryBuilder.append("    'EDI Open',");
        queryBuilder.append("    'EDI In Progress',");
        queryBuilder.append("    'EDI Ready To Post'");
        queryBuilder.append("  )");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        return query.list();
    }

    public double getAccrualsAmount(EdiInvoice ediInvoice) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(transaction_amt) as invoiceAmount");
        queryBuilder.append(" from transaction_ledger");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and (status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or status='").append(STATUS_EDI_IN_PROGRESS).append("'");
        queryBuilder.append(" or status='").append(STATUS_EDI_DISPUTE).append("')");
        queryBuilder.append(" and cust_no='").append(ediInvoice.getVendorNumber()).append("'");
        if (CommonUtils.isNotEmpty(ediInvoice.getEdiInvoiceContainers())) {
            List<String> containerNumbers = new ArrayList<String>();
            for (EdiInvoiceContainer container : ediInvoice.getEdiInvoiceContainers()) {
                containerNumbers.add("'" + container.getContainerNumber() + "'");
            }
            String containerNumber = StringUtils.removeEnd(StringUtils.removeStart(containerNumbers.toString(), "["), "]");
            queryBuilder.append(" and (invoice_number='").append(ediInvoice.getInvoiceNumber()).append("'");
            queryBuilder.append(" or container_no in (").append(containerNumber).append("))");
        } else {
            queryBuilder.append(" and invoice_number='").append(ediInvoice.getInvoiceNumber()).append("'");
        }
        Object totalAmount = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != totalAmount ? Double.parseDouble(totalAmount.toString()) : 0d;
    }

    public boolean isAccrualsFullyMapped(EdiInvoice ediInvoice) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(count(*)>0,'false','true') as mapped");
        queryBuilder.append(" from transaction_ledger");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and (status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or status='").append(STATUS_EDI_IN_PROGRESS).append("'");
        queryBuilder.append(" or status='").append(STATUS_EDI_DISPUTE).append("')");
        queryBuilder.append(" and (gl_account_number is null");
        queryBuilder.append(" or gl_account_number not in (select account from account_details))");
        queryBuilder.append(" and cust_no='").append(ediInvoice.getVendorNumber()).append("'");
        if (CommonUtils.isNotEmpty(ediInvoice.getEdiInvoiceContainers())) {
            List<String> containerNumbers = new ArrayList<String>();
            for (EdiInvoiceContainer container : ediInvoice.getEdiInvoiceContainers()) {
                containerNumbers.add("'" + container.getContainerNumber() + "'");
            }
            String containerNumber = StringUtils.removeEnd(StringUtils.removeStart(containerNumbers.toString(), "["), "]");
            queryBuilder.append(" and (invoice_number='").append(ediInvoice.getInvoiceNumber()).append("'");
            queryBuilder.append(" or container_no in (").append(containerNumber).append("))");
        } else {
            queryBuilder.append(" and invoice_number='").append(ediInvoice.getInvoiceNumber()).append("'");
        }
        String mapped = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return Boolean.valueOf(mapped);
    }

    public void setAccrualsInProgress(EdiInvoice ediInvoice) throws Exception {
        List<TransactionLedger> accruals = new AccrualsDAO().getOpenAccruals(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber());
        if (CommonUtils.isNotEmpty(ediInvoice.getEdiInvoiceContainers())) {
            List<String> containerNumbers = new ArrayList<String>();
            for (EdiInvoiceContainer container : ediInvoice.getEdiInvoiceContainers()) {
                containerNumbers.add(container.getContainerNumber());
            }
            List<TransactionLedger> containerAccruals = new AccrualsDAO().getOpenAccruals(ediInvoice.getVendorNumber(), containerNumbers);
            if (CommonUtils.isNotEmpty(accruals) && CommonUtils.isNotEmpty(containerAccruals)) {
                accruals.addAll(containerAccruals);
            } else if (CommonUtils.isNotEmpty(containerAccruals)) {
                accruals = containerAccruals;
            }
        }
        User user = new UserDAO().getUserInfo("system");
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        for (TransactionLedger accrual : accruals) {
            accrual.setInvoiceNumber(ediInvoice.getInvoiceNumber());
            accrual.setStatus(STATUS_EDI_IN_PROGRESS);
            accrual.setUpdatedOn(new Date());
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("b.sp_acct_no", accrual.getCustNo());
                        columns.put("b.invoice_number", accrual.getInvoiceNumber());
                        columns.put("t.trans_type", TRANSACTION_TYPE_IN_PROGRESS);
                        columns.put("b.modified_by_user_id", user.getUserId());
                        accrualsDAO.updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_acct_no", accrual.getCustNo());
                        columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_IN_PROGRESS);
                        columns.put("lssac.modified_by_user_id", user.getUserId());
                        accrualsDAO.updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCost) {
                        fclBlCost.setAccName(accrual.getCustName());
                        fclBlCost.setAccNo(accrual.getCustNo());
                        fclBlCost.setInvoiceNumber(accrual.getInvoiceNumber());
                        fclBlCost.setTransactionType(TRANSACTION_TYPE_IN_PROGRESS);
                    }
                }
            }
            StringBuilder desc = new StringBuilder("Accrual of");
            boolean addAnd = false;
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                desc.append(" B/L - ").append(accrual.getBillLaddingNo());
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                desc.append(addAnd ? " and" : "").append(" Doc Receipt - ").append(accrual.getDocReceipt());
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                desc.append(addAnd ? " and" : "").append(" Voyage - ").append(accrual.getVoyageNo());
            }
            desc.append(" for amount ").append(NumberUtils.formatNumber(accrual.getTransactionAmt()));
            desc.append(" is marked as EDI In Progress");
            desc.append(" for Invoice - ").append(ediInvoice.getInvoiceNumber());
            desc.append(" by system");
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            String key = accrual.getTransactionId().toString();
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, null);
        }
    }

    public boolean isShipmentInWater(String voyageNo) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  if(count(*) > 0, true, false) as shipmentInWater ");
        queryBuilder.append("from");
        queryBuilder.append("  `lcl_ss_header` sh ");
        queryBuilder.append("  join `lcl_ss_detail` sd ");
        queryBuilder.append("    on (sh.`id` = sd.`ss_header_id`) ");
        queryBuilder.append("  join `lcl_unit_ss` us ");
        queryBuilder.append("    on (sh.`id` = us.`ss_header_id`) ");
        queryBuilder.append("  join `lcl_unit` u ");
        queryBuilder.append("    on (us.`unit_id` = u.`id`) ");
        queryBuilder.append("  join `lcl_unit_ss_dispo` usd ");
        queryBuilder.append("    on (");
        queryBuilder.append("      sd.`id` = usd.`ss_detail_id` ");
        queryBuilder.append("      and u.`id` = usd.`unit_id`");
        queryBuilder.append("    ) ");
        queryBuilder.append("  join `disposition` disp ");
        queryBuilder.append("    on (");
        queryBuilder.append("      usd.`disposition_id` = disp.`id` ");
        queryBuilder.append("      and disp.`elite_code` = 'WATR' ");
        queryBuilder.append("    ) ");
        queryBuilder.append("where");
        queryBuilder.append("  sh.`schedule_no` = :voyageNo");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("voyageNo", voyageNo);
        query.addScalar("shipmentInWater", BooleanType.INSTANCE);
        return (Boolean) query.uniqueResult();
    }

    public void updateStatus(EdiInvoice ediInvoice) throws Exception {
        List<TransactionLedger> accruals = new AccrualsDAO().getEdiInvoiceAccruals(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber());
        if (CommonUtils.isNotEmpty(ediInvoice.getEdiInvoiceContainers())) {
            List<String> containerNumbers = new ArrayList<String>();
            for (EdiInvoiceContainer container : ediInvoice.getEdiInvoiceContainers()) {
                containerNumbers.add(container.getContainerNumber());
            }
            List<TransactionLedger> containerAccruals = new AccrualsDAO().getEdiInvoiceAccruals(ediInvoice.getVendorNumber(), containerNumbers);
            if (CommonUtils.isNotEmpty(accruals) && CommonUtils.isNotEmpty(containerAccruals)) {
                accruals.addAll(containerAccruals);
            } else if (CommonUtils.isNotEmpty(containerAccruals)) {
                accruals = containerAccruals;
            }
        }
        User user = new UserDAO().getUserInfo("SYSTEM");
        AccrualsDAO accrualsDAO = new AccrualsDAO();
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        double accrualsAmount = 0d;
        Set<Integer> ids = new HashSet<Integer>();
        for (TransactionLedger accrual : accruals) {
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    if (CommonUtils.isNotEmpty(accrual.getVoyageNo()) && accrual.getVoyageNo().contains("-")) {
                        if (!isShipmentInWater(StringUtils.substringAfterLast(accrual.getVoyageNo(), "-"))) {
                            continue; //Skip the accrual if the shipment is not in water
                        }
                    }
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("b.sp_acct_no", accrual.getCustNo());
                        columns.put("b.invoice_number", ediInvoice.getInvoiceNumber());
                        columns.put("t.trans_type", TRANSACTION_TYPE_IN_PROGRESS);
                        columns.put("b.modified_by_user_id", user.getUserId());
                        accrualsDAO.updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_acct_no", accrual.getCustNo());
                        columns.put("lssac.ap_reference_no", ediInvoice.getInvoiceNumber());
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_IN_PROGRESS);
                        columns.put("lssac.modified_by_user_id", user.getUserId());
                        accrualsDAO.updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCost) {
                        fclBlCost.setAccName(accrual.getCustName());
                        fclBlCost.setAccNo(accrual.getCustNo());
                        fclBlCost.setInvoiceNumber(ediInvoice.getInvoiceNumber());
                        fclBlCost.setTransactionType(TRANSACTION_TYPE_IN_PROGRESS);
                    }
                }
            }
            if (CommonUtils.isNotEqualIgnoreCase(accrual.getStatus(), STATUS_EDI_IN_PROGRESS)) {
                accrual.setInvoiceNumber(ediInvoice.getInvoiceNumber());
                accrual.setStatus(STATUS_EDI_IN_PROGRESS);
                accrual.setUpdatedOn(new Date());
                StringBuilder desc = new StringBuilder("Accrual of");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    desc.append(" B/L - ").append(accrual.getBillLaddingNo());
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    desc.append(addAnd ? " and" : "").append(" Doc Receipt - ").append(accrual.getDocReceipt());
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    desc.append(addAnd ? " and" : "").append(" Voyage - ").append(accrual.getVoyageNo());
                }
                desc.append(" for amount ").append(NumberUtils.formatNumber(accrual.getTransactionAmt()));
                desc.append(" is marked as EDI In Progress");
                desc.append(" for Invoice - ").append(ediInvoice.getInvoiceNumber());
                desc.append(" by system");
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
                String key = accrual.getTransactionId().toString();
                AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, key, NotesConstants.ACCRUALS, null);
            }
            ids.add(accrual.getTransactionId());
            accrualsAmount += accrual.getTransactionAmt();
        }
        if (CommonUtils.isEqual(ediInvoice.getInvoiceAmount(), accrualsAmount)) {
            String notMappedIds = new AccrualsDAO().isAccrualsFullyMapped(ids.toString().replace("[", "").replace("]", ""));
            if (CommonUtils.isEmpty(notMappedIds)) {
                postToAp(ediInvoice);
            } else {
                ediInvoice.setStatus(STATUS_EDI_READY_TO_POST);
            }
        } else if (CommonUtils.isNotEmpty(accrualsAmount)) {
            ediInvoice.setStatus(STATUS_EDI_IN_PROGRESS);
        } else {
            ediInvoice.setStatus(STATUS_EDI_OPEN);
        }
    }

    public void postToAp(EdiInvoice ediInvoice) throws Exception {
        new AccrualsDAO().postToAp(ediInvoice, null);
        ediInvoice.setStatus(ConstantsInterface.STATUS_EDI_POSTED_TO_AP);
        String documentId = ediInvoice.getVendorNumber() + "-" + ediInvoice.getInvoiceNumber();
        String screenName = "INVOICE";
        String documentName = "INVOICE";
        if (!new DocumentStoreLogDAO().isUploaded(documentId, screenName, documentName)) {
            String contextPath = JobScheduler.servletContext.getRealPath("/");
            String fileName = new EdiInvoiceCreator(ediInvoice, contextPath).create();
            copyDocuments(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber(), fileName);
        }
        update(ediInvoice);
    }

    public void setInvoiceStatus(String contextPath) throws Exception {
        synchronized (this) {
            List<Integer> ids = getAllOpenInvoiceIds();
            Transaction transaction = getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
            for (Integer id : ids) {
                try {
                    transaction = getCurrentSession().getTransaction();
                    if (!transaction.isActive()) {
                        transaction.begin();
                    }
                    EdiInvoice ediInvoice = findById(id);
                    if (CommonUtils.isEmpty(ediInvoice.getVendorNumber()) && CommonUtils.isNotEmpty(ediInvoice.getEdiCode())) {
                        String ediCode = ediInvoice.getEdiCode();
                        VendorModel vendor = getVendor(ediCode);
                        if (null != vendor && CommonUtils.isNotEmpty(vendor.getVendorNumber())) {
                            ediInvoice.setVendorName(vendor.getVendorName());
                            ediInvoice.setVendorNumber(vendor.getVendorNumber());
                            String fileName = new EdiInvoiceCreator(ediInvoice, contextPath).create();
                            copyDocuments(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber(), fileName);
                            StringBuilder desc = new StringBuilder();
                            desc.append("EDI Invoice - ").append(ediInvoice.getInvoiceNumber());
                            desc.append(" initially created by system");
                            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                            String key = ediInvoice.getVendorNumber() + "-" + ediInvoice.getInvoiceNumber();
                            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, null);
                        }
                    }
                    if (CommonUtils.isNotEmpty(ediInvoice.getVendorNumber())) {
                        updateStatus(ediInvoice);
                    }
                    transaction = getCurrentSession().getTransaction();
                    transaction.commit();
                } catch (Exception e) {
                    log.info("Update status for EDI Invoice Id : " + id + " failed on " + new Date(), e);
                }
            }
            transaction = getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
            }
        }
    }

    public List<AccrualModel> getInvalidAccruals(String vendorNumber, String invoiceNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select charge_code as costCode,");
        queryBuilder.append("gl_account_number as glAccount,");
        queryBuilder.append("bill_ladding_no as blNumber,");
        queryBuilder.append("container_no as containerNumber,");
        queryBuilder.append("voyage_no as voyageNumber,");
        queryBuilder.append("drcpt as dockReceipt,");
        queryBuilder.append("date_format(sailing_date,'%m/%d/%Y') as reportingDate,");
        queryBuilder.append("format(transaction_amt,2) as amount,");
        queryBuilder.append("bluescreen_chargecode as bluescreenCostCode,");
        queryBuilder.append("shipment_type as shipmentType,");
        queryBuilder.append("transaction_id as id");
        queryBuilder.append(" from transaction_ledger");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and (status='").append(STATUS_EDI_IN_PROGRESS).append("'");
        queryBuilder.append(" or status='").append(STATUS_EDI_DISPUTE).append("')");
        queryBuilder.append(" and (gl_account_number is null");
        queryBuilder.append(" or gl_account_number not in (select account from account_details))");
        queryBuilder.append(" and cust_no='").append(vendorNumber).append("'");
        queryBuilder.append(" and invoice_number='").append(invoiceNumber).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(AccrualModel.class));
        return query.list();
    }

    public void copyDocuments(String vendorNumber, String invoiceNumber, String fileName) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        File originalFile = new File(fileName);
        if (originalFile.exists()) {
            StringBuilder copyFolderPath = new StringBuilder(LoadLogisoftProperties.getProperty("reportLocation"));
            copyFolderPath.append("/Documents/INVOICE/").append(DateUtils.formatDate(new Date(), "yyyy/MM/dd")).append("/");
            File copyFolder = new File(copyFolderPath.toString());
            if (!copyFolder.exists()) {
                copyFolder.mkdirs();
            }
            String copyFileName = DateUtils.formatDate(new Date(), "yyyyMMdd_kkmmssSSS") + "_edi_invoice_.pdf";
            File copyFile = new File(copyFolderPath.toString(), copyFileName);
            in = new FileInputStream(originalFile);
            out = new FileOutputStream(copyFile);
            byte[] content = IOUtils.toByteArray(in);
            IOUtils.write(content, out);
            DocumentStoreLog document = new DocumentStoreLog();
            document.setScreenName("INVOICE");
            document.setDocumentName("INVOICE");
            document.setDocumentID(vendorNumber + "-" + invoiceNumber);
            document.setFileLocation(copyFolderPath.toString());
            document.setFileName(copyFileName);
            document.setOperation(PAGE_ACTION_ATTACH);
            document.setDateOprDone(new Date());
            document.setComments("EDI Invoice Copy");
            document.setFileSize(CommonUtils.getFileSize(copyFile));
            documentStoreLogDAO.save(document);
        }
    }

    public boolean isInvoiceAvailable(String vendorNumber, String invoiceNumber) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) as count");
        queryBuilder.append(" from edi_invoice");
        queryBuilder.append(" where vendor_number='").append(vendorNumber).append("'");
        queryBuilder.append(" and invoice_number='").append(invoiceNumber).append("'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null == result || Integer.parseInt(result.toString()) <= 0;
    }

    public void setAccrualsOpen(String vendorNumber, String invoiceNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update transaction_ledger");
        queryBuilder.append(" set status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and (status='").append(STATUS_EDI_IN_PROGRESS).append("'");
        queryBuilder.append(" or status='").append(STATUS_EDI_DISPUTE).append("')");
        queryBuilder.append(" and cust_no='").append(vendorNumber).append("'");
        queryBuilder.append(" and invoice_number='").append(invoiceNumber).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public List<EdiInvoiceLogModel> getLogs(String type) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select file_name as fileName,");
        queryBuilder.append("error as error,");
        queryBuilder.append("id as id");
        queryBuilder.append(" from edi_invoice_log");
        queryBuilder.append(" where type='").append(type).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(EdiInvoiceLogModel.class));
        return query.list();
    }

    public void archiveInvoice(String vendorNumber, String invoiceNumber, String updatedBy) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update edi_invoice");
        queryBuilder.append(" set status = '").append(STATUS_EDI_ARCHIVE).append("',");
        queryBuilder.append(" updated_date = sysdate(),");
        queryBuilder.append(" updated_by = '").append(updatedBy).append("'");
        queryBuilder.append(" where vendor_number = '").append(vendorNumber).append("'");
        queryBuilder.append(" and invoice_number = '").append(invoiceNumber).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public EdiInvoice cloneInvoice(String vendorNumber, String oldInvoiceNumber, String newInvoiceNumber) throws Exception {
        EdiInvoice oldEdiInvoice = getInvoice(vendorNumber, oldInvoiceNumber, null);
        if (null == oldEdiInvoice) {
            throw new AccountingException("EDI Invoice not found");
        } else {
            EdiInvoice newEdiInvoice = new EdiInvoice();
            String[] excludes = new String[]{"id", "ediInvoiceLog", "ediInvoiceBanks", "ediInvoiceParties", "ediInvoiceDetails", "ediInvoiceShippingDetails", "ediInvoiceContainers"};
            BeanUtils.copyProperties(oldEdiInvoice, newEdiInvoice, excludes);
            newEdiInvoice.setInvoiceNumber(newInvoiceNumber);
            if (CommonUtils.isNotEmpty(oldEdiInvoice.getEdiInvoiceBanks())) {
                List<EdiInvoiceBank> newEdiInvoiceBanks = new ArrayList<EdiInvoiceBank>();
                for (EdiInvoiceBank oldEdiInvoiceBank : oldEdiInvoice.getEdiInvoiceBanks()) {
                    EdiInvoiceBank newEdiInvoiceBank = new EdiInvoiceBank();
                    BeanUtils.copyProperties(oldEdiInvoiceBank, newEdiInvoiceBank, "id", "ediInvoice");
                    newEdiInvoiceBank.setEdiInvoice(newEdiInvoice);
                    newEdiInvoiceBanks.add(newEdiInvoiceBank);
                }
                newEdiInvoice.setEdiInvoiceBanks(newEdiInvoiceBanks);
            }
            if (CommonUtils.isNotEmpty(oldEdiInvoice.getEdiInvoiceParties())) {
                List<EdiInvoiceParty> newEdiInvoiceParties = new ArrayList<EdiInvoiceParty>();
                for (EdiInvoiceParty oldEdiInvoiceParty : oldEdiInvoice.getEdiInvoiceParties()) {
                    EdiInvoiceParty newEdiInvoiceParty = new EdiInvoiceParty();
                    BeanUtils.copyProperties(oldEdiInvoiceParty, newEdiInvoiceParty, "id", "ediInvoice");
                    newEdiInvoiceParty.setEdiInvoice(newEdiInvoice);
                    newEdiInvoiceParties.add(newEdiInvoiceParty);
                }
                newEdiInvoice.setEdiInvoiceParties(newEdiInvoiceParties);
            }
            if (CommonUtils.isNotEmpty(oldEdiInvoice.getEdiInvoiceDetails())) {
                List<EdiInvoiceDetail> newEdiInvoiceDetails = new ArrayList<EdiInvoiceDetail>();
                for (EdiInvoiceDetail oldEdiInvoiceDetail : oldEdiInvoice.getEdiInvoiceDetails()) {
                    EdiInvoiceDetail newEdiInvoiceDetail = new EdiInvoiceDetail();
                    BeanUtils.copyProperties(oldEdiInvoiceDetail, newEdiInvoiceDetail, "id", "ediInvoice");
                    newEdiInvoiceDetail.setEdiInvoice(newEdiInvoice);
                    newEdiInvoiceDetails.add(newEdiInvoiceDetail);
                }
                newEdiInvoice.setEdiInvoiceDetails(newEdiInvoiceDetails);
            }
            if (null != oldEdiInvoice.getEdiInvoiceShippingDetails()) {
                EdiInvoiceShippingDetails newEdiInvoiceShippingDetails = new EdiInvoiceShippingDetails();
                BeanUtils.copyProperties(oldEdiInvoice.getEdiInvoiceShippingDetails(), newEdiInvoiceShippingDetails, "id", "ediInvoice");
                newEdiInvoiceShippingDetails.setEdiInvoice(newEdiInvoice);
                newEdiInvoice.setEdiInvoiceShippingDetails(newEdiInvoiceShippingDetails);
            }
            if (CommonUtils.isNotEmpty(oldEdiInvoice.getEdiInvoiceContainers())) {
                List<EdiInvoiceContainer> newEdiInvoiceContainers = new ArrayList<EdiInvoiceContainer>();
                for (EdiInvoiceContainer oldEdiInvoiceContainer : oldEdiInvoice.getEdiInvoiceContainers()) {
                    EdiInvoiceContainer newEdiInvoiceContainer = new EdiInvoiceContainer();
                    BeanUtils.copyProperties(oldEdiInvoiceContainer, newEdiInvoiceContainer, "id", "ediInvoice");
                    newEdiInvoiceContainer.setEdiInvoice(newEdiInvoice);
                    newEdiInvoiceContainers.add(newEdiInvoiceContainer);
                }
                newEdiInvoice.setEdiInvoiceContainers(newEdiInvoiceContainers);
            }
            return newEdiInvoice;
        }
    }

    // eculine invoice
    public List<EdiInvoiceCharges> getCharges(String blNo, String invoiceNo, String chargeId, String cntrId, String fileNumberId) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        query.append(" ediinv.id, ac.id AS lclBookingAcId, edichg.id AS arGlId, edichg.id AS apGlId,");
        query.append(" ecuchg.cstcod AS bluCostCode , ecuchg.chgcod AS bluChargeCode,");
        query.append(" edichg.rate AS rate, edichg.currency AS currency, bi.file_number_id AS fileId,");
        query.append(" edichg.id AS invoiceId, ediinv.vendor_number AS vendorNo, ediinv.vendor_name AS vendorName,");
        query.append(" ediinv.invoice_number AS invoiceNo, ediinv.created_date AS createdDate, edichg.description AS eculineDesc,");
        query.append(" edichg.charge_status AS chargeStatus, IF(COUNT(ac.id) = 0, 'false', 'true')AS fileContainChargesFlag , ");
        query.append(" edichg.invoice_status AS invoiceStatus, argl.charge_code AS chargeCode, argl.id AS chargeId,");
        query.append(" apgl.charge_code AS costCode, apgl.id AS costId, edichg.quantity AS quantity,");
        query.append(" IF(edichg.currency != 'USD' AND edichg.gl_id IS NULL AND edichg.ap_gl_mapping_id IS NULL, edichg.price*edichg.rate,edichg.price) AS price,");
        query.append(" IF(edichg.ar_amount < 0 OR archg.field10 = 'C',0.00,edichg.ar_amount) AS arAmount ,");
        query.append(" IF(edichg.ap_amount < 0 OR archg.field10 = 'S',0.00,edichg.ap_amount) AS apAmount,");
        query.append(" ecuedi.is_approved AS containerApproved, bi.is_approved AS blApproved ,");
        query.append(" IF(edichg.ar_bill_to_party IS NULL,'C',edichg.ar_bill_to_party) AS arBillToParty,fn.file_number AS fileNo,archg.field10 AS chargeflag ");
        query.append(" FROM eculine_edi ecuedi ");
        query.append(" JOIN container_info ci ON (ecuedi.id = ci.eculine_edi_id) ");
        query.append(" JOIN bl_info bi ON (ci.id = bi.container_info_id AND bi.bl_no =:blNo) ");
        query.append(" JOIN edi_invoice_detail edichg  ON (edichg.bl_reference = bi.bl_no ");
        if (CommonUtils.isNotEmpty(chargeId)) {
            query.append(" AND edichg.id = :chargeId ");
        }
        query.append(" )");
        query.append(" JOIN edi_invoice ediinv ON (ediinv.id = edichg.edi_invoice_id ");
        if (CommonUtils.isNotEmpty(invoiceNo)) {
            query.append(" AND ediinv.invoice_number = :invoiceNo ");
        }
        query.append(" )");
        query.append(" LEFT JOIN lcl_file_number fn ON (fn.id = bi.file_number_id AND fn.id = :fileNumberId) ");
        query.append(" LEFT JOIN lcl_booking_ac ac ON (ac.file_number_id = fn.id AND ac.ar_gl_mapping_id = edichg.gl_id AND ac.ap_gl_mapping_id = edichg.ap_gl_mapping_id)");
        query.append(" LEFT JOIN ecuicd ecuchg ON (ecuchg.chgdes = edichg.description)");
        query.append(" LEFT JOIN gl_mapping argl ON (argl.bluescreen_chargecode=ecuchg.chgcod AND argl.shipment_type = 'LCLI' AND argl.transaction_type = 'AR')");
        query.append(" LEFT JOIN gl_mapping apgl ON (apgl.bluescreen_chargecode=ecuchg.cstcod AND apgl.shipment_type = 'LCLI' AND apgl.transaction_type = 'AC')");
        query.append(" LEFT JOIN genericcode_dup archg ON (archg.code = argl.charge_code)");
        query.append(" WHERE ci.id = :cntrId AND ediinv.status <> 'EDI Duplicate' AND ediinv.status <> 'EDI Archive' GROUP BY edichg.id;");
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(query.toString());
        sqlQuery.setResultTransformer(Transformers.aliasToBean(EdiInvoiceCharges.class));
        sqlQuery.setString("blNo", blNo);
        if (CommonUtils.isNotEmpty(invoiceNo)) {
            sqlQuery.setString("invoiceNo", invoiceNo);
        }
        if (CommonUtils.isNotEmpty(chargeId)) {
            sqlQuery.setString("chargeId", chargeId);
        }
        sqlQuery.setString("cntrId", cntrId);
        sqlQuery.setString("fileNumberId", fileNumberId);
        sqlQuery.addScalar("id", IntegerType.INSTANCE);
        sqlQuery.addScalar("arGlId", IntegerType.INSTANCE);
        sqlQuery.addScalar("apGlId", IntegerType.INSTANCE);
        sqlQuery.addScalar("fileId", LongType.INSTANCE);
        sqlQuery.addScalar("invoiceId", IntegerType.INSTANCE);
        sqlQuery.addScalar("vendorNo", StringType.INSTANCE);
        sqlQuery.addScalar("rate", StringType.INSTANCE);
        sqlQuery.addScalar("arBillToParty", StringType.INSTANCE);
        sqlQuery.addScalar("currency", StringType.INSTANCE);
        sqlQuery.addScalar("vendorName", StringType.INSTANCE);
        sqlQuery.addScalar("invoiceNo", StringType.INSTANCE);
        // sqlQuery.addScalar("chargeCode", StringType.INSTANCE);
        sqlQuery.addScalar("eculineDesc", StringType.INSTANCE);
        sqlQuery.addScalar("chargeStatus", StringType.INSTANCE);
        sqlQuery.addScalar("invoiceStatus", StringType.INSTANCE);
        sqlQuery.addScalar("chargeCode", StringType.INSTANCE);
        sqlQuery.addScalar("bluCostCode", StringType.INSTANCE);
        sqlQuery.addScalar("bluChargeCode", StringType.INSTANCE);
        sqlQuery.addScalar("chargeId", IntegerType.INSTANCE);
        sqlQuery.addScalar("costCode", StringType.INSTANCE);
        sqlQuery.addScalar("costId", IntegerType.INSTANCE);
        sqlQuery.addScalar("quantity", StringType.INSTANCE);
        sqlQuery.addScalar("price", StringType.INSTANCE);
        sqlQuery.addScalar("fileNo", StringType.INSTANCE);
        sqlQuery.addScalar("arAmount", BigDecimalType.INSTANCE);
        sqlQuery.addScalar("apAmount", BigDecimalType.INSTANCE);
        sqlQuery.addScalar("createdDate", DateType.INSTANCE);
        sqlQuery.addScalar("containerApproved", BooleanType.INSTANCE);
        sqlQuery.addScalar("fileContainChargesFlag", StringType.INSTANCE);
        sqlQuery.addScalar("blApproved", BooleanType.INSTANCE);
        sqlQuery.addScalar("chargeFlag", StringType.INSTANCE);
        return sqlQuery.list();
    }

    public List<EdiInvoiceCharges> searchInvoiceMappingCharges(String description, int start, int limit) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  charge.description as eculineDesc,");
        queryBuilder.append("  case");
        queryBuilder.append("    when armap.charge_code is not null");
        queryBuilder.append("    then armap.charge_code");
        queryBuilder.append("    else null");
        queryBuilder.append("  end as chargeCode,");
        queryBuilder.append("  case");
        queryBuilder.append("    when apmap.charge_code is not null");
        queryBuilder.append("    then apmap.charge_code");
        queryBuilder.append("    else null");
        queryBuilder.append("  end as costCode,");
        queryBuilder.append("  ecuchg.cstcod as bluCostCode,");
        queryBuilder.append("  ecuchg.chgcod as bluChargeCode ");
        queryBuilder.append("from");
        queryBuilder.append("  (select");
        queryBuilder.append("    charge.description as description");
        queryBuilder.append("  from");
        queryBuilder.append("    edi_invoice_detail charge");
        if (CommonUtils.isNotEmpty(description)) {
            queryBuilder.append("  where charge.description = '").append(description).append("'");
        }
        queryBuilder.append("  group by charge.description");
        queryBuilder.append("  limit ").append(start).append(",").append(limit);
        queryBuilder.append("  ) as charge");
        queryBuilder.append("  left join ecuicd ecuchg");
        queryBuilder.append("    on ecuchg.chgdes = charge.description");
        queryBuilder.append("  left join gl_mapping armap");
        queryBuilder.append("    on armap.bluescreen_chargecode = ecuchg.chgcod");
        queryBuilder.append("    and armap.shipment_type = 'LCLI'");
        queryBuilder.append("    and armap.Transaction_Type = 'AR'");
        queryBuilder.append("  left join gl_mapping apmap");
        queryBuilder.append("    on apmap.bluescreen_chargecode = ecuchg.cstcod");
        queryBuilder.append("    and apmap.shipment_type = 'LCLI'");
        queryBuilder.append("    and apmap.Transaction_Type = 'AC' ");
        queryBuilder.append("group by charge.description ");
        queryBuilder.append("limit ").append(start).append(",").append(limit);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(EdiInvoiceCharges.class));
        query.addScalar("eculineDesc", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("bluCostCode", StringType.INSTANCE);
        query.addScalar("bluChargeCode", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        return query.list();
    }

    public int getTotalRowsForCodeMapping(String description) throws Exception {
        StringBuilder query = new StringBuilder();
        BigInteger count = new BigInteger("0");
        query.append("SELECT COUNT(DISTINCT description) FROM edi_invoice_detail");
        if (CommonUtils.isNotEmpty(description)) {
            query.append(" WHERE description = '").append(description).append("'");
        }
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(query.toString());
        Object o = sqlQuery.uniqueResult();
        if (o != null) {
            count = (BigInteger) o;
        }
        return count.intValue();
    }

    public void updateChargeStatus(Integer chargeId, String status, int userId) throws Exception {
        Query query = getCurrentSession().createQuery("update EdiInvoiceDetail set chargeStatus = :status where id = :id");
        query.setString("status", status);
        query.setInteger("id", chargeId);
        query.executeUpdate();
    }

    public void updateInvoiceStatus(String chargeIds, int userId) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append("update edi_invoice_detail set invoice_status = 1,updated_by=").append(userId);
        qBuilder.append(",updated_datetime=SYSDATE()");
        qBuilder.append(" where id in ( ").append(chargeIds).append(")");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        query.executeUpdate();
    }

    public void updateCharges(EculineInvoiceForm invoiceForm, int userId) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update EdiInvoiceDetail set ");
        queryBuilder.append("arAmount = '").append(invoiceForm.getChargeAmount()).append("', ");
        queryBuilder.append("arBillToParty = '").append(invoiceForm.getChargeBillToParty()).append("', ");
        queryBuilder.append("apAmount = '").append(invoiceForm.getCostAmount()).append("', ");
        queryBuilder.append("price = '").append(String.format("%.2f",
                Double.parseDouble(invoiceForm.getPrice()))).append("', ");
        queryBuilder.append("arGlMapping.id = ").append(invoiceForm.getArGlId()).append(", apGlMapping.id=").append(invoiceForm.getApGlId());
        queryBuilder.append(",updated_by=").append(userId);
        queryBuilder.append(",updated_datetime=SYSDATE() ");
        if ("yes".equalsIgnoreCase(invoiceForm.getDispute())) {
            queryBuilder.append(", chargeStatus = 'D' ");
        } else if ("P".equalsIgnoreCase(invoiceForm.getIsPosted())) {
            queryBuilder.append(", chargeStatus = 'P' ");
        }
        queryBuilder.append("where id = ").append(invoiceForm.getChargeId());
        Query query = getCurrentSession().createQuery(queryBuilder.toString());
        query.executeUpdate();
    }

    public void doDispute(EculineInvoiceForm invoiceForm, int userId) throws Exception {
        Query query = getCurrentSession().createQuery(
                "update EdiInvoiceDetail set chargeStatus = :chargeStatus,updatedBy=:updatedBy,updatedDate=:updatedDate where id = :chargeId");
        query.setInteger("chargeId", invoiceForm.getChargeId());
        query.setString("chargeStatus", "D");
        query.setInteger("updatedBy", userId);
        query.setDate("updatedDate", new Date());
        query.executeUpdate();
    }

    public Boolean isAtleastOneApproved(String hblNo) {
        StringBuilder query = new StringBuilder();
        query.append("select if(max(invoice_status) = 1, 'true', 'false') as is_atleast_one_invoice_approved");
        query.append(" from edi_invoice_detail where bl_reference = '").append(hblNo).append("'");
        SQLQuery sqlQuery = getCurrentSession().createSQLQuery(query.toString());
        return ("true".equals((String) sqlQuery.uniqueResult()));
    }

    public String getVoyageOwnerEmail(String invoiceNo, String blNo) throws Exception {
        StringBuilder qBuilder = new StringBuilder();
        qBuilder.append("select email FROM user_details where user_id =");
        qBuilder.append(" (select created_by from eculine_edi where id =");
        qBuilder.append(" (select eculine_edi_id from container_info where id =");
        qBuilder.append(" (select container_info_id from bl_info where bl_no = '");
        qBuilder.append(blNo).append("'))) and");
        qBuilder.append(" (select if(count(invoice_number) > 0, 1, 0)");
        qBuilder.append(" from edi_invoice where id in");
        qBuilder.append(" (select edi_invoice_id from edi_invoice_detail where bl_reference = '");
        qBuilder.append(blNo).append("') and invoice_number != '").append(invoiceNo).append("') = 1");
        SQLQuery query = getCurrentSession().createSQLQuery(qBuilder.toString());
        return (String) query.uniqueResult();
    }

    public String getEcuMappingId(String chargeDesc) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select chgdes from ecuicd where chgdes='").append(chargeDesc).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        return (String) query.uniqueResult();
    }

    public String getEcuMappingId(String chargeDesc, String chargeCode, String costCode) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(" select chgdes from ecuicd where chgdes='").append(chargeDesc).append("' and chgcod = '").append(chargeCode).append("' and cstcod = '").append(costCode).append("'");
        SQLQuery query = getCurrentSession().createSQLQuery(sb.toString());
        return (String) query.uniqueResult();
    }

    public void insertEcuicdMapping(String chrgCode, String costCode, String ecuChrgDesc) throws Exception {
        String ecuMappdesc = getEcuMappingId(ecuChrgDesc);
        if (CommonUtils.isEmpty(ecuMappdesc)) {
            StringBuilder sb = new StringBuilder();
            sb.append("insert into ecuicd(chgdes,cstcod,chgcod,hazyn)values('");
            sb.append(ecuChrgDesc).append("','").append(costCode).append("','").append(chrgCode).append("','").append('N').append("')");
            getCurrentSession().createSQLQuery(sb.toString()).executeUpdate();
        } else {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("update ecuicd set ");
            queryBuilder.append("chgcod = '").append(chrgCode).append("', ");
            queryBuilder.append("cstcod = '").append(costCode).append("' ");
            queryBuilder.append("where chgdes = '").append(ecuChrgDesc).append("' ");
            Query query = getCurrentSession().createSQLQuery(queryBuilder.toString());
            query.executeUpdate();
        }
    }
}
