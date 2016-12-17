package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.logiware.accounting.form.ApPaymentForm;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.accounting.model.PaymentModel;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.utils.AuditNotesUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ApPaymentDAO extends BaseHibernateDAO {

    public List<PaymentModel> getPaymentList(ApPaymentForm form) throws Exception {
        RoleDuty roleDuty = new RoleDAO().getRoleDuty(form.getUser().getRole().getRoleId());
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  tp.`acct_name` as vendorName,");
        queryBuilder.append("  tp.`acct_no` as vendorNumber,");
        queryBuilder.append("  ucase((select cs.`codedesc` from `cust_accounting` ca, `genericcode_dup` cs where ca.`acct_no` = tr.`cust_no` and ca.`credit_status` = cs.`id`)) as creditHold,");
        queryBuilder.append("  format(sum(if(tr.`transaction_type` = 'AP', tr.`balance`, -tr.`balance`)), 2) as paymentAmount,");
        queryBuilder.append("  date_format(current_date(), '%m/%d/%Y') as paymentDate,");
        queryBuilder.append("  coalesce(tr.`pay_method`, (select pm.`pay_method` from `payment_method` pm where pm.`pay_accno` = vi.`cust_accno` order by field(pm.`default_pay_method`, 'Y') desc, pm.`id` asc limit 1), 'CHECK') as paymentMethod,");
        queryBuilder.append("  coalesce(if(tr.`status` = 'WFA', tr.`pay_method`, null), (select group_concat(distinct pm.`pay_method` order by field(pm.`default_pay_method`, 'Y') desc, pm.`id` asc) from `payment_method` pm where pm.`pay_accno` = vi.`cust_accno`), 'CHECK') as paymentMethods,");
        queryBuilder.append("  tr.`status` as status,");
        queryBuilder.append("  group_concat(distinct tr.`transaction_id` order by tr.`transaction_id`) as ids ");
        queryBuilder.append("from");
        queryBuilder.append("  `transaction` tr");
        queryBuilder.append("  join `trading_partner` tp");
        queryBuilder.append("    on (tr.`cust_no` = tp.`acct_no`)");
        queryBuilder.append("  join `vendor_info` vi");
        queryBuilder.append("    on (tp.`acct_no` = vi.`cust_accno`) ");
        queryBuilder.append("where");
        queryBuilder.append("  tr.`transaction_type` in ('AP', 'AR')");
        if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
            queryBuilder.append("  and tr.`cust_no` = :vendorNumber");
        }
        queryBuilder.append("  and tr.`balance` <> 0.00");
        queryBuilder.append("  and tr.`balance_in_process` <> 0.00");
        queryBuilder.append("  and tr.`status` in ('RP', 'WFA') ");
        if (!roleDuty.isApPayment()) {
            queryBuilder.append(" and (vi.`ap_specialist` = :userId or tr.`owner` = :userId or tr.`created_by` = :userId) ");
        }
        queryBuilder.append("group by tr.`cust_no`, tr.`status`, tr.`pay_method`");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        if (CommonUtils.isNotEmpty(form.getVendorNumber())) {
            query.setString("vendorNumber", form.getVendorNumber());
        }
        if (!roleDuty.isApPayment()) {
            query.setInteger("userId", form.getUser().getUserId());
        }
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("vendorNumber", StringType.INSTANCE);
        query.addScalar("creditHold", StringType.INSTANCE);
        query.addScalar("paymentAmount", StringType.INSTANCE);
        query.addScalar("paymentDate", StringType.INSTANCE);
        query.addScalar("paymentMethod", StringType.INSTANCE);
        query.addScalar("paymentMethods", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("ids", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(PaymentModel.class));
        return query.list();
    }

    public void search(ApPaymentForm form) throws Exception {
        try {
            org.hibernate.Transaction transaction = getCurrentSession().getTransaction();
            transaction.commit();
            transaction = getCurrentSession().getTransaction();
            transaction.begin();
            form.setPaymentList(getPaymentList(form));
            form.setPaymentDate(DateUtils.formatDate(new Date(), "MM/dd/yyyy"));
            BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
            if (CommonUtils.isNotEmpty(form.getBankName())) {
                form.setBankAccountList(bankDetailsDAO.getBankAccounts(form.getBankName(), form.getUser().getUserId()));
                if (CommonUtils.isNotEmpty(form.getBankAccount())) {
                    form.setStartingNumber(bankDetailsDAO.getStartingNumber(form.getBankName(), form.getBankAccount()));
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void setInvoiceList(ApPaymentForm form) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  ucase(if(tr.`transaction_type` = 'AP', tr.`invoice_number`, if(tr.`bill_ladding_no` <> '', tr.`bill_ladding_no`, tr.`invoice_number`))) as invoiceOrBl,");
        queryBuilder.append("  format(if(tr.`transaction_type` = 'AP', tr.`balance`, -tr.`balance`), 2) as invoiceAmount,");
        queryBuilder.append("  date_format(tr.`transaction_date`, '%m/%d/%Y') as invoiceDate,");
        queryBuilder.append("  date_format(if(tr.`due_date` is not null, tr.`due_date`, date_add(tr.`transaction_date`, interval if(ct.`code` <> '', ct.`code`, 0) day)), '%m/%d/%Y') as dueDate,");
        queryBuilder.append("  datediff(sysdate(), tr.`transaction_date`) as age,");
        queryBuilder.append("  if(ct.`codedesc` <> '', ucase(ct.`codedesc`), 'DUE UPON RECEIPT') as creditTerms,");
        queryBuilder.append("  (select if(count(nt.id) > 0, true, false) from `notes` nt where nt.`module_id` = if(tr.`transaction_type` = 'AP', 'AP_INVOICE', 'AR_INVOICE') and nt.`module_ref_id` = concat(tr.`cust_no`, '-', if(tr.`transaction_type` = 'AP', tr.`invoice_number`, if(tr.`bill_ladding_no` <> '', tr.`bill_ladding_no`, tr.`invoice_number`))) and nt.`note_type` = 'Manual') as manualNotes,");
        queryBuilder.append("  if(tr.`transaction_type` = 'AP', 'AP_INVOICE', 'AR_INVOICE') as noteModuleId,");
        queryBuilder.append("  concat(tr.`cust_no`, '-', if(tr.`transaction_type` = 'AP', tr.`invoice_number`, if(tr.`bill_ladding_no` <> '', tr.`bill_ladding_no`, tr.`invoice_number`))) as noteModuleRefId,");
        queryBuilder.append("  tr.`transaction_id` as id ");
        queryBuilder.append("from");
        queryBuilder.append("  `transaction` tr");
        queryBuilder.append("  join `vendor_info` vi");
        queryBuilder.append("    on (tr.`cust_no` = vi.`cust_accno`) ");
        queryBuilder.append("  left join `genericcode_dup` ct");
        queryBuilder.append("    on (vi.`credit_terms` = ct.`id`)");
        queryBuilder.append("where");
        queryBuilder.append("  tr.`transaction_id` in (:invoiceIds) ");
        queryBuilder.append("order by tr.`transaction_date` desc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        String[] ids = form.getIds().split(",");
        Integer[] invoiceIds = new Integer[ids.length];
        for (int index = 0; index < invoiceIds.length; index++) {
            invoiceIds[index] = Integer.parseInt(ids[index]);
        }
        query.setParameterList("invoiceIds", invoiceIds);
        query.addScalar("invoiceOrBl", StringType.INSTANCE);
        query.addScalar("invoiceAmount", StringType.INSTANCE);
        query.addScalar("invoiceDate", StringType.INSTANCE);
        query.addScalar("dueDate", StringType.INSTANCE);
        query.addScalar("age", IntegerType.INSTANCE);
        query.addScalar("creditTerms", StringType.INSTANCE);
        query.addScalar("manualNotes", BooleanType.INSTANCE);
        query.addScalar("noteModuleId", StringType.INSTANCE);
        query.addScalar("noteModuleRefId", StringType.INSTANCE);
        query.addScalar("id", IntegerType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(InvoiceModel.class));
        form.setInvoiceList(query.list());
    }

    public void removeInvoice(ApPaymentForm form) throws Exception {
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        Transaction invoice = transactionDAO.findById(form.getId());
        User user = form.getUser();
        invoice.setStatus(ConstantsInterface.STATUS_OPEN);
        invoice.setGlAccountNumber(null);
        invoice.setPaymentMethod(null);
        invoice.setBankAccountNumber(null);
        invoice.setBankName(null);
        invoice.setPaidBy(null);
        invoice.setPaidOn(null);
        invoice.setApBatchId(null);
        invoice.setArBatchId(null);
        invoice.setUpdatedOn(new Date());
        invoice.setOwner(null);
        invoice.setUpdatedBy(user.getUserId());
        String moduleId;
        Double amount;
        String invoiceOrBl;
        StringBuilder noteDesc = new StringBuilder();
        if (CommonUtils.isEqualIgnoreCase(invoice.getTransactionType(), ConstantsInterface.TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
            moduleId = NotesConstants.AP_INVOICE;
            amount = invoice.getTransactionAmt();
            invoiceOrBl = invoice.getInvoiceNumber();
            noteDesc.append("Invoice '");
        } else {
            moduleId = NotesConstants.AR_INVOICE;
            invoiceOrBl = CommonUtils.isNotEmpty(invoice.getBillLaddingNo()) ? invoice.getBillLaddingNo() : invoice.getInvoiceNumber();
            amount = -invoice.getBalance();
            noteDesc.append("Invoice/BL '");
        }
        String moduleRefId = invoice.getCustNo() + "-" + invoiceOrBl;
        noteDesc.append(invoiceOrBl).append("'");
        noteDesc.append(" of '").append(invoice.getCustName()).append("'");
        noteDesc.append(" for amount '").append(NumberUtils.formatNumber(amount)).append("'");
        noteDesc.append(" is released from ready to pay by ").append(user.getLoginName());
        noteDesc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(noteDesc.toString(), moduleId, moduleRefId, moduleId, user);
        List<String> ids = new ArrayList<String>();
        ids.addAll(Arrays.asList(form.getIds().split(",")));
        ids.remove(String.valueOf(form.getId()));
        form.setIds(StringUtils.join(ids, ","));
    }

    public Double getPaymentAmount(PaymentModel paymentModel) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  sum(if(`transaction_type` = 'AP', `balance`, -`balance`)) as paymentAmount ");
        queryBuilder.append("from");
        queryBuilder.append("  `transaction` ");
        queryBuilder.append("where");
        queryBuilder.append("  `transaction_id` in (:ids)");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        String[] ids = paymentModel.getIds().split(",");
        Integer[] invoiceIds = new Integer[ids.length];
        for (int index = 0; index < invoiceIds.length; index++) {
            invoiceIds[index] = Integer.parseInt(ids[index]);
        }
        query.setParameterList("ids", invoiceIds);
        query.addScalar("paymentAmount", DoubleType.INSTANCE);
        return (Double) query.uniqueResult();
    }
}
