package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.accounting.form.CheckRegisterForm;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.accounting.model.PaymentModel;
import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class CheckRegisterDAO extends BaseHibernateDAO implements ConstantsInterface {

    public List<PaymentModel> getPaymentList(CheckRegisterForm form) throws Exception {
        getCurrentSession().flush();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  py.`ap_batch_id` as batchId,");
        queryBuilder.append("  if(py.`pay_method` = '").append(PAYMENT_METHOD_ACH).append("', lpad(py.`ach_batch_sequence`, 7, '0'), py.`cheque_number`) as checkNumber,");
        queryBuilder.append("  (select tp.`acct_name` from `trading_partner` tp where tp.`acct_no` = py.`cust_no`) as vendorName,");
        queryBuilder.append("  py.`cust_no` as vendorNumber,");
        queryBuilder.append("  py.`pay_method` as paymentMethod,");
        queryBuilder.append("  format(sum(py.`transaction_amt`), 2) as paymentAmount,");
        queryBuilder.append("  date_format(py.`transaction_date`, '%m/%d/%Y') as paymentDate,");
        queryBuilder.append("  py.`gl_account_number` as glAccount,");
        queryBuilder.append("  py.`bank_name` as bankName,");
        queryBuilder.append("  py.`bank_account_no` as bankAccount,");
        queryBuilder.append("  if(py.`cleared` = '").append(YES).append("', true, false) as cleared,");
        queryBuilder.append("  date_format(py.`cleared_date`, '%m/%d/%Y') as clearedDate,");
        queryBuilder.append("  if(py.`void` = '").append(YES).append("', true, false) as voided,");
        queryBuilder.append("  if(py.`reprint` = '").append(YES).append("', true, false) as reprinted,");
        queryBuilder.append("  (select if(count(nt.id) > 0, true, false) from `notes` nt where nt.`module_id` = '").append(NotesConstants.AP_PAYMENT).append("' and nt.`module_ref_id` = if(py.`pay_method` = '").append(PAYMENT_METHOD_CHECK).append("', py.`cheque_number`, concat(py.`cust_no`, '-', py.`ap_batch_id`)) and nt.`note_type` = 'Manual') as manualNotes,");
        queryBuilder.append("  '").append(NotesConstants.AP_PAYMENT).append("' as noteModuleId,");
        queryBuilder.append("  if(py.`pay_method` = '").append(PAYMENT_METHOD_CHECK).append("', py.`cheque_number`, concat(py.`cust_no`, '-', py.`ap_batch_id`)) as noteModuleRefId,");
        queryBuilder.append("  group_concat(distinct py.`transaction_id` order by py.`transaction_id`) as ids ");
        queryBuilder.append("from");
        queryBuilder.append("  `transaction` py ");
        queryBuilder.append("where");
        queryBuilder.append("  py.`transaction_type` = 'PY'");
        if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
            queryBuilder.append("  and py.`cust_no` = :vendorNumber");
        }
        boolean apply = true;
        if (CommonUtils.isNotEmpty(form.getFromCheckNumber()) && CommonUtils.isNotEmpty(form.getToCheckNumber())) {
            apply = false;
            queryBuilder.append("  and (");
            queryBuilder.append("    (");
            queryBuilder.append("      cast(py.`cheque_number` as signed integer) >= :fromCheckNumber");
            queryBuilder.append("      and cast(py.`cheque_number` as signed integer) <= :toCheckNumber");
            queryBuilder.append("    )");
            queryBuilder.append("    or (");
            queryBuilder.append("      cast(py.`ach_batch_sequence` as signed integer) >= :fromCheckNumber");
            queryBuilder.append("      and cast(py.`ach_batch_sequence` as signed integer) <= :toCheckNumber");
            queryBuilder.append("    )");
            queryBuilder.append("  )");
        } else if (CommonUtils.isNotEmpty(form.getFromCheckNumber())) {
            apply = false;
            queryBuilder.append("  and (");
            queryBuilder.append("    cast(py.`cheque_number` as signed integer) >= :fromCheckNumber");
            queryBuilder.append("    or cast(py.`ach_batch_sequence` as signed integer) >= :fromCheckNumber");
            queryBuilder.append("  )");
        } else if (CommonUtils.isNotEmpty(form.getToCheckNumber())) {
            apply = false;
            queryBuilder.append("  and (");
            queryBuilder.append("    cast(py.`cheque_number` as signed integer) <= :toCheckNumber");
            queryBuilder.append("    or cast(py.`ach_batch_sequence` as signed integer) <= :toCheckNumber");
            queryBuilder.append("  )");
        }
        if (CommonUtils.isNotEmpty(form.getBatchId())) {
            apply = false;
            queryBuilder.append("  and py.`ap_batch_id` like :batchId");
        } else {
            queryBuilder.append("  and py.`ap_batch_id` is not null");
        }
        if (CommonUtils.isNotEmpty(form.getGlAccount()) && apply) {
            if (CommonUtils.isNotEmpty(form.getFromDate())) {
                queryBuilder.append(" and py.`transaction_date` >= :fromDate");
            }
            if (CommonUtils.isNotEmpty(form.getToDate())) {
                queryBuilder.append(" and py.`transaction_date` <= :toDate");
            }
            queryBuilder.append("  and py.`gl_account_number` = :glAccount");
            if (CommonUtils.isNotEmpty(form.getBankAccount())) {
                queryBuilder.append("  and py.`bank_account_no` = :bankAccount");
            }
            if (CommonUtils.isEqualIgnoreCase(form.getStatus(), "Cleared")) {
                queryBuilder.append("  and py.`cleared` = '").append(YES).append("'");
                queryBuilder.append("  and py.`status` = '").append(CLEAR).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(form.getStatus(), "Not Cleared")) {
                queryBuilder.append("  and py.`cleared` = '").append(NO).append("'");
                queryBuilder.append("  and py.`status` = '").append(STATUS_RECONCILE_IN_PROGRESS).append("'");
            } else if (CommonUtils.isEqualIgnoreCase(form.getStatus(), "Voided")) {
                queryBuilder.append("  and py.`void` = '").append(YES).append("'");
                queryBuilder.append("  and py.`status` in ('").append(STATUS_PAID).append("', '").append(STATUS_SENT).append("', '").append(STATUS_READY_TO_SEND).append("')");
            }
            if (CommonUtils.isNotEmpty(form.getPaymentMethod())) {
                queryBuilder.append("  and py.`pay_method` = :paymentMethod");
            } else {
                queryBuilder.append("  and py.`pay_method` <> ''");
            }
        }
        if (CommonUtils.isNotEmpty(form.getPaymentAmount())) {
            queryBuilder.append("  and py.`transaction_amt` ").append(form.getAmountOperator()).append(" :paymentAmount");
        }
        if (CommonUtils.isNotEmpty(form.getReconcileDate())) {
            queryBuilder.append("  and reconciled_date = :reconcileDate");
        }
        queryBuilder.append("  group by py.`cheque_number`, py.`ap_batch_id`, py.`cust_no`");
        queryBuilder.append("  order by py.`transaction_date` desc, py.`cheque_number` desc, py.`ap_batch_id` desc, py.`cust_no` asc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setMaxResults(form.getLimit());
        if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
            query.setString("vendorNumber", form.getVendorNumber());
        }
        if (CommonUtils.isNotEmpty(form.getFromCheckNumber()) && CommonUtils.isNotEmpty(form.getToCheckNumber())) {
            query.setInteger("fromCheckNumber", Integer.parseInt(form.getFromCheckNumber()));
            query.setInteger("toCheckNumber", Integer.parseInt(form.getToCheckNumber()));
        } else if (CommonUtils.isNotEmpty(form.getFromCheckNumber())) {
            query.setInteger("fromCheckNumber", Integer.parseInt(form.getFromCheckNumber()));
        } else if (CommonUtils.isNotEmpty(form.getToCheckNumber())) {
            query.setInteger("toCheckNumber", Integer.parseInt(form.getToCheckNumber()));
        }
        if (CommonUtils.isNotEmpty(form.getBatchId())) {
            query.setString("batchId", form.getBatchId() + "%");
        }
        if (CommonUtils.isNotEmpty(form.getGlAccount()) && apply) {
            if (CommonUtils.isNotEmpty(form.getFromDate())) {
                String fromDate = DateUtils.formatDate(DateUtils.parseDate(form.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
                query.setString("fromDate", fromDate);
            }
            if (CommonUtils.isNotEmpty(form.getToDate())) {
                String toDate = DateUtils.formatDate(DateUtils.parseDate(form.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
                query.setString("toDate", toDate);
            }
            query.setString("glAccount", form.getGlAccount());
            if (CommonUtils.isNotEmpty(form.getBankAccount())) {
                query.setString("bankAccount", form.getBankAccount());
            }
            if (CommonUtils.isNotEmpty(form.getPaymentMethod())) {
                query.setString("paymentMethod", form.getPaymentMethod());
            }
        }
        if (CommonUtils.isNotEmpty(form.getPaymentAmount())) {
            query.setDouble("paymentAmount", Double.parseDouble(form.getPaymentAmount()));
        }
        if (CommonUtils.isNotEmpty(form.getReconcileDate())) {
            query.setString("reconcileDate", form.getReconcileDate());
        }
        query.addScalar("batchId", StringType.INSTANCE);
        query.addScalar("checkNumber", StringType.INSTANCE);
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("vendorNumber", StringType.INSTANCE);
        query.addScalar("paymentMethod", StringType.INSTANCE);
        query.addScalar("paymentAmount", StringType.INSTANCE);
        query.addScalar("paymentDate", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("bankName", StringType.INSTANCE);
        query.addScalar("bankAccount", StringType.INSTANCE);
        query.addScalar("cleared", BooleanType.INSTANCE);
        query.addScalar("clearedDate", StringType.INSTANCE);
        query.addScalar("voided", BooleanType.INSTANCE);
        query.addScalar("reprinted", BooleanType.INSTANCE);
        query.addScalar("manualNotes", BooleanType.INSTANCE);
        query.addScalar("noteModuleId", StringType.INSTANCE);
        query.addScalar("noteModuleRefId", StringType.INSTANCE);
        query.addScalar("ids", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(PaymentModel.class));
        return query.list();
    }

    public void search(CheckRegisterForm form) throws Exception {
        try {
            org.hibernate.Transaction transaction = getCurrentSession().getTransaction();
            transaction.commit();
            transaction = getCurrentSession().getTransaction();
            transaction.begin();
            form.setPaymentList(getPaymentList(form));
        } catch (Exception e) {
            throw e;
        }
    }

    public void setInvoiceList(CheckRegisterForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  ucase(if(py.`bill_ladding_no` <> '', py.`bill_ladding_no`, py.`invoice_number`)) as invoiceOrBl,");
        queryBuilder.append("  format(py.`transaction_amt`, 2) as invoiceAmount ");
        queryBuilder.append("from");
        queryBuilder.append("  `transaction` py ");
        queryBuilder.append("where");
        queryBuilder.append("  py.`transaction_id` in (:invoiceIds) ");
        queryBuilder.append("order by py.`transaction_date` desc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        String[] ids = form.getIds().split(",");
        Integer[] invoiceIds = new Integer[ids.length];
        for (int index = 0; index < invoiceIds.length; index++) {
            invoiceIds[index] = Integer.parseInt(ids[index]);
        }
        query.setParameterList("invoiceIds", invoiceIds);
        query.addScalar("invoiceOrBl", StringType.INSTANCE);
        query.addScalar("invoiceAmount", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(InvoiceModel.class));
        form.setInvoiceList(query.list());
    }
}
