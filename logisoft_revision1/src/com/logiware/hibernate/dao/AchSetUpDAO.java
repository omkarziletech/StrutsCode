package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import static com.gp.cong.common.ConstantsInterface.STATUS_READY_TO_SEND;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.logiware.hibernate.domain.AchSetUp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Data access object (DAO) for domain model class AchSetUp.
 *
 * @see com.logiware.hibernate.domain.AchSetUp
 * @author Lakshminarayanan
 */
public class AchSetUpDAO extends BaseHibernateDAO implements Serializable, ConstantsInterface {

    private static final long serialVersionUID = -6416993010394778974L;

    public AchSetUp findById(Integer id) throws Exception {
        AchSetUp instance = (AchSetUp) getSession().get(AchSetUp.class, id);
        return instance;
    }

    public List<BankDetails> getBankDetailsForAch() throws Exception {
        List<BankDetails> bankDetailsList = new ArrayList<BankDetails>();
        Criteria criteria = getCurrentSession().createCriteria(BankDetails.class);
        criteria.add(Restrictions.isNotNull("bankName"));
        criteria.add(Restrictions.isNotNull("bankAcctNo"));
        criteria.add(Restrictions.isNotNull("bankRoutingNumber"));
        criteria.add(Restrictions.isNotNull("achSetUp"));
        List<BankDetails> resultList = criteria.list();
        if (CommonUtils.isNotEmpty(resultList)) {
            bankDetailsList.addAll(resultList);
        }
        return bankDetailsList;
    }

    public List<TransactionBean> getAchPayments(String bankName, String bankAccountNo) throws Exception {
        List<TransactionBean> achPayments = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("   format(py.paid_amount, 2) as paid_amount,");
        queryBuilder.append("   date_format(py.paid_date,'%m/%d/%Y') as paid_date,");
        queryBuilder.append("   py.vendor_name as vendor_name,");
        queryBuilder.append("   py.vendor_number as vendor_number ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    select");
        queryBuilder.append("      sum(py.transaction_amt) as paid_amount,");
        queryBuilder.append("      py.transaction_date as paid_date,");
        queryBuilder.append("      py.cust_name as vendor_name,");
        queryBuilder.append("      py.cust_no as vendor_number");
        queryBuilder.append("    from");
        queryBuilder.append("      transaction py");
        queryBuilder.append("    where py.pay_method = '").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append("      and py.transaction_type = '").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        queryBuilder.append("      and py.status = '").append(STATUS_PAID).append("'");
        queryBuilder.append("      and py.bank_name = '").append(bankName).append("'");
        queryBuilder.append("      and py.bank_account_no = '").append(bankAccountNo).append("'");
        queryBuilder.append("      and (py.void = '").append(NO).append("' or py.void is null)");
        queryBuilder.append("      and py.ach_batch_sequence is null");
        queryBuilder.append("      and py.ap_batch_id <> 0");
        queryBuilder.append("      and py.ar_batch_id is null");
        queryBuilder.append("    group by py.cust_no, py.ap_batch_id");
        queryBuilder.append("  ) as py ");
        queryBuilder.append("where");
        queryBuilder.append("   py.paid_amount <> 0");
        List list = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : list) {
            Object[] col = (Object[]) row;
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setAmount((String) col[0]);
            transactionBean.setTransDate((String) col[1]);
            transactionBean.setCustomer((String) col[2]);
            transactionBean.setCustomerNo((String) col[3]);
            achPayments.add(transactionBean);
        }
        return achPayments;
    }

    public void updateAchPayments(String bankName, String bankAccountNo, Integer achBatchSequence, String status) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  transaction py ");
        queryBuilder.append("set");
        if (CommonUtils.isEqualIgnoreCase(status, STATUS_READY_TO_SEND)) {
            queryBuilder.append("  py.ach_batch_sequence = ").append(achBatchSequence).append(",");
        }
        queryBuilder.append("  py.status = '").append(status).append("' ");
        queryBuilder.append("where");
        queryBuilder.append("  py.pay_method = '").append(PAYMENT_METHOD_ACH).append("'");
        queryBuilder.append("  and py.transaction_type = '").append(TRANSACTION_TYPE_PAYAMENT).append("'");
        if (CommonUtils.isEqualIgnoreCase(status, STATUS_READY_TO_SEND)) {
            queryBuilder.append("  and py.status = '").append(STATUS_PAID).append("'");
        } else {
            queryBuilder.append("  and py.status = '").append(STATUS_READY_TO_SEND).append("'");
        }
        queryBuilder.append("  and py.bank_name = '").append(bankName).append("'");
        queryBuilder.append("  and py.bank_account_no = '").append(bankAccountNo).append("'");
        queryBuilder.append("  and (py.void = '").append(NO).append("' or py.void is null)");
        if (CommonUtils.isEqualIgnoreCase(status, STATUS_READY_TO_SEND)) {
            queryBuilder.append("  and py.ach_batch_sequence is null");
        } else {
            queryBuilder.append("  and py.ach_batch_sequence = ").append(achBatchSequence);
        }
        queryBuilder.append("  and py.ap_batch_id <> 0");
        queryBuilder.append("  and py.ar_batch_id is null");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

        if (CommonUtils.isEqualIgnoreCase(status, STATUS_READY_TO_SEND)) {
            queryBuilder.delete(0, queryBuilder.length());

            queryBuilder.append("update");
            queryBuilder.append("  transaction tr ");
            queryBuilder.append("set");
            queryBuilder.append("  tr.ach_batch_sequence = ").append(achBatchSequence).append(" ");
            queryBuilder.append("where");
            queryBuilder.append("  tr.pay_method = '").append(PAYMENT_METHOD_ACH).append("'");
            queryBuilder.append("  and tr.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
            queryBuilder.append("  and tr.status = '").append(STATUS_PAID).append("'");
            queryBuilder.append("  and tr.bank_name = '").append(bankName).append("'");
            queryBuilder.append("  and tr.bank_account_no = '").append(bankAccountNo).append("'");
            queryBuilder.append("  and (tr.void = '").append(NO).append("' or tr.void is null)");
            queryBuilder.append("  and tr.ach_batch_sequence is null");
            queryBuilder.append("  and tr.ap_batch_id <> 0");
            queryBuilder.append("  and tr.ar_batch_id is null");
            getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        }
    }
}
