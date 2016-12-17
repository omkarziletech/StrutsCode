package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.beans.TransactionBean;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.struts.form.AccrualsForm;
import com.gp.cvst.logisoft.struts.form.ApReportsForm;
import com.gp.cvst.logisoft.struts.form.RecieptsLedgerForm;
import com.gp.cvst.logisoft.struts.form.ReconcileForm;
import com.logiware.accounting.model.CostModel;
import com.logiware.accounting.model.SubledgerModel;
import com.logiware.bean.AccountingBean;
import com.logiware.hibernate.domain.TransactionLedgerHistory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class TransactionLedger(for
 * Accounting Module only).
 *
 * @see com.gp.cvst.logisoft.domain.TransactionLedger
 * @author Lakshminarayanan
 */
public class AccountingLedgerDAO extends BaseHibernateDAO implements Serializable, ConstantsInterface {

    private static final long serialVersionUID = 6081690778650932783L;

    public void save(TransactionLedger transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public void saveHistory(TransactionLedgerHistory transientInstance) throws Exception {
        getSession().save(transientInstance);
        getSession().flush();
    }

    public Integer saveAndReturnId(TransactionLedger transientInstance) throws Exception {
        getSession().save(transientInstance);
        return transientInstance.getTransactionId();
    }

    public void update(TransactionLedger transientInstance) throws Exception {
        getSession().update(transientInstance);
        getSession().flush();
        getSession().clear();
    }

    public void delete(TransactionLedger persistentInstance) throws Exception {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public TransactionLedger findById(java.lang.Integer id) throws Exception {
        getSession().flush();
        TransactionLedger instance = (TransactionLedger) getSession().get("com.gp.cvst.logisoft.domain.TransactionLedger", id);
        return instance;
    }

    public List findByExample(TransactionLedger instance) throws Exception {
        List results = getSession().createCriteria("com.gp.cvst.logisoft.hibernate.dao.TransactionLedger").add(Example.create(instance)).list();
        return results;
    }

    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from TransactionLedger as model where model." + propertyName + "= ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public TransactionLedger merge(TransactionLedger detachedInstance) throws Exception {
        TransactionLedger result = (TransactionLedger) getSession().merge(detachedInstance);
        return result;
    }

    public void attachDirty(TransactionLedger instance) throws Exception {
        getSession().saveOrUpdate(instance);
    }

    public Integer inactivateOrActivateAccruals(Integer days, boolean canInactivate) throws Exception {
        StringBuilder queryBuilder = new StringBuilder(" update TransactionLedger set status='");
        if (canInactivate) {
            queryBuilder.append(STATUS_INACTIVE).append("'");
        } else {
            queryBuilder.append(STATUS_OPEN).append("'");
        }
        queryBuilder.append(" where datediff(sysdate(),sailingDate)<").append(days);
        queryBuilder.append(" and transactionType='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append("  and accrualsCorrectionFlag is null");
        queryBuilder.append(" and status='");
        if (canInactivate) {
            queryBuilder.append(STATUS_OPEN).append("'");
        } else {
            queryBuilder.append(STATUS_INACTIVE).append("'");
        }
        int result = getCurrentSession().createQuery(queryBuilder.toString()).executeUpdate();
        return result;
    }

    /**
     * Get accruals for aging
     *
     * @param apReportsForm
     * @return accruals
     * @throws Exception
     */
    public List<TransactionBean> getAccrualsForAging(ApReportsForm apReportsForm) throws Exception {
        List<TransactionBean> apReportList = new ArrayList<TransactionBean>();
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getCutOffDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tr.Cust_no,tr.Cust_name,tr.transaction_type,tr.invoice_number,tr.Transaction_date,tr.due_date,");
        queryBuilder.append("if(tr.drcpt!='',tr.drcpt,tr.Bill_Ladding_No) as custRefNo,");
        queryBuilder.append("tr.Cheque_number,tr.ap_batch_id,tr.ar_batch_id,tr.Transaction_ID,");
        queryBuilder.append("(if(datediff('").append(cutOffDate).append("',tr.Transaction_date)>=0 AND datediff('").append(cutOffDate).append("',tr.Transaction_date)<=30 ,tr.Transaction_amt,'0')) as 30days,");
        queryBuilder.append("(if(datediff('").append(cutOffDate).append("',tr.Transaction_date)>30 AND datediff('").append(cutOffDate).append("',tr.Transaction_date)<=60 ,tr.Transaction_amt,'0')) as 60days,");
        queryBuilder.append("(if(datediff('").append(cutOffDate).append("',tr.Transaction_date)>60 AND datediff('").append(cutOffDate).append("',tr.Transaction_date)<=90 ,tr.Transaction_amt,'0')) as 90days,");
        queryBuilder.append("(if(datediff('").append(cutOffDate).append("',tr.Transaction_date)>90,tr.Transaction_amt,'0')) as 91days");
        queryBuilder.append(" from transaction_ledger tr ");
        queryBuilder.append(" where tr.Transaction_date<='").append(cutOffDate).append("'");
        queryBuilder.append(" and tr.Subledger_Source_Code='" + SUB_LEDGER_CODE_ACCRUALS + "'");
        queryBuilder.append(" and tr.Transaction_type='" + TRANSACTION_TYPE_ACCRUALS + "'");
        queryBuilder.append("  and tr.accruals_correction_flag is null");
        queryBuilder.append(" and (tr.status='" + STATUS_OPEN + "'");
        queryBuilder.append(" or tr.status='" + STATUS_DISPUTE + "'");
        queryBuilder.append(" or tr.status='" + STATUS_IN_PROGRESS + "')");
        if (CommonUtils.isNotEmpty(apReportsForm.getGlAccount())) {
            queryBuilder.append(" and tr.gl_account_number like '%").append(apReportsForm.getGlAccount().trim()).append("%'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getCostCode())) {
            queryBuilder.append(" and tr.charge_Code like '%").append(apReportsForm.getCostCode().trim()).append("%'");
        }
        if (CommonUtils.isNotEqual(apReportsForm.getShowAllCustomer(), YES)) {
            if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
                queryBuilder.append(" and tr.Cust_no='").append(apReportsForm.getVendorNumber().trim()).append("'");
            }
        } else {
            queryBuilder.append(" and tr.Cust_no is not null and  tr.Cust_no!=''");
        }
        queryBuilder.append(" order by tr.Cust_no,tr.Transaction_date");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setCustomerNo(null != col[0] ? col[0].toString() : "");
            transactionBean.setCustomer(null != col[1] ? col[1].toString() : "");
            transactionBean.setRecordType(null != col[2] ? col[2].toString() : "");
            transactionBean.setInvoiceOrBl(null != col[3] ? col[3].toString() : "");
            transactionBean.setInvoiceDate(null != col[4] ? DateUtils.formatDate(DateUtils.parseDate(col[4].toString(), "yyyy-MM-dd"), "MM/dd/yyyy") : "");
            transactionBean.setDuedate(null != col[5] ? DateUtils.formatDate(DateUtils.parseDate(col[5].toString(), "yyyy-MM-dd"), "MM/dd/yyyy") : "");
            transactionBean.setCustomerReference(null != col[6] ? col[6].toString() : "");
            transactionBean.setChequenumber(null != col[7] ? col[7].toString() : "");
            transactionBean.setApBatchId(null != col[8] ? Integer.parseInt(col[8].toString()) : null);
            transactionBean.setArBatchId(null != col[9] ? Integer.parseInt(col[9].toString()) : null);
            String transactionId = null != col[10] ? col[10].toString() : "";
            transactionBean.setTransactionId(StringUtils.removeEnd(transactionId, ","));
            transactionBean.setAge0_30Balance(null != col[11] ? NumberUtils.formatNumber(Double.parseDouble(col[11].toString().trim()), "###,###,##0.00") : "");
            transactionBean.setAge31_60Balance(null != col[12] ? NumberUtils.formatNumber(Double.parseDouble(col[12].toString().trim()), "###,###,##0.00") : "");
            transactionBean.setAge61_90Balance(null != col[13] ? NumberUtils.formatNumber(Double.parseDouble(col[13].toString().trim()), "###,###,##0.00") : "");
            transactionBean.setAge91Balance(null != col[14] ? NumberUtils.formatNumber(Double.parseDouble(col[14].toString().trim()), "###,###,##0.00") : "");
            Double totalBalance = 0d;
            totalBalance += null != transactionBean.getAge0_30Balance() ? Double.parseDouble(transactionBean.getAge0_30Balance().replaceAll(",", "")) : 0d;
            totalBalance += null != transactionBean.getAge31_60Balance() ? Double.parseDouble(transactionBean.getAge31_60Balance().replaceAll(",", "")) : 0d;
            totalBalance += null != transactionBean.getAge61_90Balance() ? Double.parseDouble(transactionBean.getAge61_90Balance().replaceAll(",", "")) : 0d;
            totalBalance += null != transactionBean.getAge91Balance() ? Double.parseDouble(transactionBean.getAge91Balance().replaceAll(",", "")) : 0d;
            if (totalBalance != 0d) {
                apReportList.add(transactionBean);
            }
        }
        return apReportList;
    }

    /**
     * Get inactive accruals for aging
     *
     * @param apReportsForm
     * @return inactive accruals
     * @throws Exception
     */
    public List<TransactionBean> getInactiveAccrualsForAging(ApReportsForm apReportsForm) throws Exception {
        List<TransactionBean> apReportList = new ArrayList<TransactionBean>();
        String cutOffDate = DateUtils.formatDate(DateUtils.parseDate(apReportsForm.getCutOffDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tr.Cust_no,tr.Cust_name,tr.transaction_type,tr.invoice_number,tr.Transaction_date,tr.due_date,");
        queryBuilder.append("if(tr.drcpt!='',tr.drcpt,tr.Bill_Ladding_No) as custRefNo,");
        queryBuilder.append("tr.Cheque_number,tr.ap_batch_id,tr.ar_batch_id,tr.Transaction_ID,");
        queryBuilder.append("(if(datediff('").append(cutOffDate).append("',tr.Transaction_date)>=0 AND datediff('").append(cutOffDate).append("',tr.Transaction_date)<=30 ,tr.Transaction_amt,'0')) as 30days,");
        queryBuilder.append("(if(datediff('").append(cutOffDate).append("',tr.Transaction_date)>30 AND datediff('").append(cutOffDate).append("',tr.Transaction_date)<=60 ,tr.Transaction_amt,'0')) as 60days,");
        queryBuilder.append("(if(datediff('").append(cutOffDate).append("',tr.Transaction_date)>60 AND datediff('").append(cutOffDate).append("',tr.Transaction_date)<=90 ,tr.Transaction_amt,'0')) as 90days,");
        queryBuilder.append("(if(datediff('").append(cutOffDate).append("',tr.Transaction_date)>90,tr.Transaction_amt,'0')) as 91days");
        queryBuilder.append(" from transaction_ledger tr ");
        queryBuilder.append(" where tr.Transaction_date<='").append(cutOffDate).append("'");
        queryBuilder.append(" and tr.Subledger_Source_Code='" + SUB_LEDGER_CODE_ACCRUALS + "'");
        queryBuilder.append(" and tr.Transaction_type='" + TRANSACTION_TYPE_ACCRUALS + "'");
        queryBuilder.append(" and tr.status='" + STATUS_INACTIVE + "'");
        queryBuilder.append("  and tr.accruals_correction_flag is null");
        if (CommonUtils.isNotEmpty(apReportsForm.getGlAccount())) {
            queryBuilder.append(" and tr.gl_account_number like '%").append(apReportsForm.getGlAccount().trim()).append("%'");
        }
        if (CommonUtils.isNotEmpty(apReportsForm.getCostCode())) {
            queryBuilder.append(" and tr.charge_Code like '%").append(apReportsForm.getCostCode().trim()).append("%'");
        }
        if (CommonUtils.isNotEqual(apReportsForm.getShowAllCustomer(), YES)) {
            if (CommonUtils.isNotEmpty(apReportsForm.getVendorNumber())) {
                queryBuilder.append(" and tr.Cust_no='").append(apReportsForm.getVendorNumber().trim()).append("'");
            }
        } else {
            queryBuilder.append(" and tr.Cust_no is not null and  tr.Cust_no!=''");
        }
        queryBuilder.append(" order by tr.Cust_no,tr.Transaction_date");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setCustomerNo(null != col[0] ? col[0].toString() : "");
            transactionBean.setCustomer(null != col[1] ? col[1].toString() : "");
            transactionBean.setRecordType(null != col[2] ? col[2].toString() : "");
            transactionBean.setInvoiceOrBl(null != col[3] ? col[3].toString() : "");
            transactionBean.setInvoiceDate(null != col[4] ? DateUtils.formatDate(DateUtils.parseDate(col[4].toString(), "yyyy-MM-dd"), "MM/dd/yyyy") : "");
            transactionBean.setDuedate(null != col[5] ? DateUtils.formatDate(DateUtils.parseDate(col[5].toString(), "yyyy-MM-dd"), "MM/dd/yyyy") : "");
            transactionBean.setCustomerReference(null != col[6] ? col[6].toString() : "");
            transactionBean.setChequenumber(null != col[7] ? col[7].toString() : "");
            transactionBean.setApBatchId(null != col[8] ? Integer.parseInt(col[8].toString()) : null);
            transactionBean.setArBatchId(null != col[9] ? Integer.parseInt(col[9].toString()) : null);
            String transactionId = null != col[10] ? col[10].toString() : "";
            transactionBean.setTransactionId(StringUtils.removeEnd(transactionId, ","));
            transactionBean.setAge0_30Balance(null != col[11] ? NumberUtils.formatNumber(Double.parseDouble(col[11].toString().trim()), "###,###,##0.00") : "");
            transactionBean.setAge31_60Balance(null != col[12] ? NumberUtils.formatNumber(Double.parseDouble(col[12].toString().trim()), "###,###,##0.00") : "");
            transactionBean.setAge61_90Balance(null != col[13] ? NumberUtils.formatNumber(Double.parseDouble(col[13].toString().trim()), "###,###,##0.00") : "");
            transactionBean.setAge91Balance(null != col[14] ? NumberUtils.formatNumber(Double.parseDouble(col[14].toString().trim()), "###,###,##0.00") : "");
            Double totalBalance = 0d;
            totalBalance += null != transactionBean.getAge0_30Balance() ? Double.parseDouble(transactionBean.getAge0_30Balance().replaceAll(",", "")) : 0d;
            totalBalance += null != transactionBean.getAge31_60Balance() ? Double.parseDouble(transactionBean.getAge31_60Balance().replaceAll(",", "")) : 0d;
            totalBalance += null != transactionBean.getAge61_90Balance() ? Double.parseDouble(transactionBean.getAge61_90Balance().replaceAll(",", "")) : 0d;
            totalBalance += null != transactionBean.getAge91Balance() ? Double.parseDouble(transactionBean.getAge91Balance().replaceAll(",", "")) : 0d;
            if (totalBalance != 0d) {
                apReportList.add(transactionBean);
            }
        }
        return apReportList;
    }

    /**
     * Get invoice or bl details
     *
     * @param vendorNumber param invoiceNumber param blNumber param
     * transactionType
     * @return details of invoice or bl
     * @throws Exception
     */
    public List<TransactionBean> getInvoiceOrBlDetails(String vendorNumber, String invoiceNumber, String blNumber, String transactionType) throws Exception {
        List<TransactionBean> invoiceOrBlDetails = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder("select tl.Bill_Ladding_No, tl.drcpt, tl.Voyage_No, tl.Container_No, tl.Transaction_date,");
        queryBuilder.append("tl.posted_date,tl.Transaction_amt, tl.Charge_Code,tl.gl_account_number from transaction_ledger tl");
        queryBuilder.append(" where tl.Transaction_type = 'AC'");
        if (CommonUtils.isEqualIgnoreCase(transactionType, TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
            queryBuilder.append(" and (tl.status='").append(STATUS_ASSIGN).append("'");
            queryBuilder.append(" or tl.status='").append(STATUS_EDI_ASSIGNED).append("')");
        }
        if (CommonUtils.isNotEmpty(invoiceNumber)) {
            queryBuilder.append(" and tl.invoice_number ='").append(invoiceNumber).append("'");
        } else if (CommonUtils.isNotEmpty(blNumber)) {
            queryBuilder.append(" and tl.Bill_Ladding_No ='").append(blNumber).append("'");
        }
        if (CommonUtils.isNotEmpty(vendorNumber)) {
            queryBuilder.append(" and tl.cust_no ='").append(vendorNumber).append("'");
        }
        List result = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setBillofLadding((String) col[0]);
            transactionBean.setDocReceipt((String) col[1]);
            transactionBean.setVoyagenumber((String) col[2]);
            transactionBean.setContainerNo((String) col[3]);
            transactionBean.setTransactionDate((Date) col[4]);
            transactionBean.setPostedDate((Date) col[5]);
            transactionBean.setTransactionAmount(null != col[6] ? Double.parseDouble(col[6].toString()) : null);
            transactionBean.setChargeCode((String) col[7]);
            transactionBean.setGlAcctNo((String) col[8]);
            invoiceOrBlDetails.add(transactionBean);
        }
        return invoiceOrBlDetails;
    }

    public List<TransactionBean> getFclBlAccruals(String billOfLadding) {
        getSession().flush();
        List<TransactionBean> accruals = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tl.Cust_name,tl.cust_no,tl.Bill_Ladding_No,tl.Charge_Code,tl.Transaction_amt,tl.Invoice_number,");
        queryBuilder.append("tl.Transaction_Id, tl.Container_No, tl.Voyage_No, ve.company, tl.status, tl.sailing_date, tl.Cheque_number,");
        queryBuilder.append("tl.drcpt,tl.correction_notice,cast(tl.cost_id as char),tl.description");
        queryBuilder.append(" from transaction_ledger tl left join vendor_info ve on ve.cust_accno=tl.cust_no");
        queryBuilder.append(" where tl.cust_no is not null  and tl.accruals_correction_flag is null");
        queryBuilder.append(" and tl.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Bill_Ladding_No = '").append(billOfLadding).append("'");
        queryBuilder.append(" and tl.status = '").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and tl.Transaction_amt<>0 and tl.Balance<>0");
        queryBuilder.append(" order by tl.cust_no,tl.Transaction_Id");
        List result = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean tbean = new TransactionBean();
            tbean.setCustomer((String) col[0]);
            tbean.setCustomerNo((String) col[1]);
            tbean.setBillofLadding((String) col[2]);
            tbean.setChargeCode((String) col[3]);
            tbean.setTransactionAmount(Double.parseDouble(col[4].toString().trim()));
            tbean.setInvoiceOrBl((String) col[5]);
            tbean.setTransactionId(col[6].toString());
            tbean.setContainerNo((String) col[7]);
            tbean.setVoyage((String) col[8]);
            tbean.setContact((String) col[9]);
            tbean.setStatus((String) col[10]);
            tbean.setSailingDate((Date) col[11]);
            if (col[12] != null && !col[12].toString().trim().equals("") && Integer.parseInt(col[12].toString()) > 0) {
                tbean.setChequenumber((String) col[12]);
            }
            tbean.setDocReceipt((String) col[13]);
            tbean.setCorrectionNotice(null != col[14] && CommonUtils.isNotEqual(col[14].toString(), FclBlConstants.CNA0) ? col[14].toString() : "");
            tbean.setCostId((String) col[15]);
            String desc = (String) col[16];
            tbean.setDescription(null != desc ? desc.replace("\r\n", "<br>").replace("\n", "<br>") : "");
            accruals.add(tbean);
        }
        return accruals;
    }

    public String buildSearchAccrualsQuery(AccrualsForm accrualsForm) {
        StringBuilder queryBuilder = new StringBuilder("from transaction_ledger tl");
        queryBuilder.append(" where tl.cust_no!='' and tl.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        if (CommonUtils.isNotEqual(accrualsForm.getCategory(), "0")) {
            String propertyName = null;
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getCategory(), SEARCH_BY_INVOICE_NUMBER)) {
                propertyName = "Invoice_number";
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getCategory(), SEARCH_BY_CONTAINER)) {
                propertyName = "Container_No";
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getCategory(), SEARCH_BY_DOCK_RECEIPT)) {
                propertyName = "drcpt";
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getCategory(), SEARCH_BY_HOUSE_BILL)) {
                propertyName = "Bill_Ladding_No";
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getCategory(), SEARCH_BY_SUB_HOUSE_BILL)) {
                propertyName = "Sub_House_BL";
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getCategory(), SEARCH_BY_VOYAGE)) {
                if (CommonUtils.isNotEmpty(accrualsForm.getDocNumber())) {
                    queryBuilder.append(" and (tl.Voyage_No like '%").append(accrualsForm.getDocNumber().trim()).append("%'");
                    String drcpts = getDocReceiptsForVoyage(accrualsForm.getDocNumber().trim());
                    if (CommonUtils.isNotEmpty(drcpts)) {
                        queryBuilder.append(" or tl.drcpt in ").append(drcpts);
                    }
                    queryBuilder.append(")");
                } else {
                    queryBuilder.append(" and (tl.Voyage_No is null or tl.Voyage_No like '%')");
                }
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getCategory(), SEARCH_BY_MASTER_BILL)) {
                propertyName = "Master_BL";
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getCategory(), SEARCH_BY_BOOKING_NUMBER)) {
                propertyName = "booking_no";
            }
            if (null != propertyName) {
                if (CommonUtils.isNotEmpty(accrualsForm.getDocNumber())) {
                    queryBuilder.append(" and tl.").append(propertyName).append(" like '%").append(accrualsForm.getDocNumber().trim()).append("%'");
                } else {
                    queryBuilder.append(" and (tl.").append(propertyName).append(" is null");
                    queryBuilder.append(" or tl.").append(propertyName).append(" like '%')");
                }
            }
        } else if (CommonUtils.isNotEmpty(accrualsForm.getVendornumber())) {
            queryBuilder.append(" and tl.cust_no = '").append(accrualsForm.getVendornumber().trim()).append("'");
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getInvoicenumber()) && CommonUtils.isEqual(accrualsForm.getButtonValue(), "searchAccrualsByInvoice")) {
            queryBuilder.append(" and tl.invoice_number='").append(accrualsForm.getInvoicenumber()).append("'");
            if (CommonUtils.isNotEmpty(accrualsForm.getStatus())) {
                queryBuilder.append(" and tl.status='").append(accrualsForm.getStatus()).append("'");
            } else {
                queryBuilder.append(" and (tl.status!='").append(STATUS_ASSIGN).append("'");
                queryBuilder.append(" and tl.status!='").append(STATUS_EDI_ASSIGNED).append("'");
                queryBuilder.append(" and tl.status!='").append(STATUS_VOID).append("')");
            }
        } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getShowAccruals(), YES)) {
            queryBuilder.append(" and tl.status = '").append(STATUS_OPEN).append("'");
        } else {
            queryBuilder.append(" and tl.status != '").append(STATUS_OPEN).append("'");
            if (CommonUtils.isNotEqual(accrualsForm.getCategory(), "0")) {
                queryBuilder.append(" and tl.status != '").append(STATUS_IN_PROGRESS).append("'");
                queryBuilder.append(" and tl.status != '").append(STATUS_DISPUTE).append("'");
            }
        }
        if (accrualsForm.isHideAccruals()) {
            queryBuilder.append(" and datediff(sysdate(),tl.sailing_date)<").append(accrualsForm.getAccrualsLimit().trim());
        }
        queryBuilder.append(" and tl.Transaction_amt!=0");
        String accrualIds = accrualsForm.getAccrualIds();
        if (CommonUtils.isNotEmpty(accrualIds)) {
            accrualIds = StringUtils.removeEnd(StringUtils.removeStart(accrualIds, ","), ",");
            queryBuilder.append(" and tl.Transaction_Id not in (").append(accrualIds).append(")");
        }
        queryBuilder.append(" and tl.status!='").append(STATUS_PENDING).append("'");
        return queryBuilder.toString();
    }

    public Integer getTotalAccruals(String conditions) throws Exception {
        StringBuilder queryBuilder = new StringBuilder(" select count(tl.Transaction_Id) ");
        queryBuilder.append(conditions);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {

            return Integer.parseInt(result.toString());
        }
        return 0;
    }

    public List<TransactionBean> searchAccruals(AccrualsForm accrualsForm, String conditions) {
        List<TransactionBean> accruals = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tl.Cust_name,tl.cust_no,tl.Bill_Ladding_No,tl.Charge_Code,tl.Transaction_amt,tl.Invoice_number,");
        queryBuilder.append("tl.Transaction_Id, tl.Container_No, tl.Voyage_No, ve.company, tl.status, tl.sailing_date, tl.Cheque_number,");
        queryBuilder.append("tl.drcpt,tl.correction_notice,tl.cost_id,tl.description from");
        queryBuilder.append(" (select tl.Cust_name,tl.cust_no,tl.Bill_Ladding_No,tl.Charge_Code,tl.Transaction_amt,tl.Invoice_number,");
        queryBuilder.append("tl.Transaction_Id, tl.Container_No, tl.Voyage_No,tl.status, tl.sailing_date, tl.Cheque_number,");
        queryBuilder.append("tl.drcpt,tl.correction_notice,cast(tl.cost_id as char) as cost_id,tl.description ");
        queryBuilder.append(conditions);
        if ((CommonUtils.isEqualIgnoreCase(accrualsForm.getButtonValue(), "gotoPage")
                || CommonUtils.isEqualIgnoreCase(accrualsForm.getButtonValue(), "doSort"))
                && CommonUtils.isNotEmpty(accrualsForm.getSortBy()) && CommonUtils.isNotEmpty(accrualsForm.getSortOrder())) {
            queryBuilder.append(" order by ").append(accrualsForm.getSortBy()).append(" ").append(accrualsForm.getSortOrder());
        } else {
            queryBuilder.append(" order by tl.cust_no,tl.Transaction_Id");
        }
        if (accrualsForm.getPageNo() > 0 && (CommonUtils.isEqualIgnoreCase(accrualsForm.getButtonValue(), "searchAccruals")
                || CommonUtils.isEqualIgnoreCase(accrualsForm.getButtonValue(), "gotoPage")
                || CommonUtils.isEqualIgnoreCase(accrualsForm.getButtonValue(), "doSort"))) {
            int start = (accrualsForm.getCurrentPageSize() * (accrualsForm.getPageNo() - 1));
            int end = accrualsForm.getCurrentPageSize();
            queryBuilder.append(" limit ").append(start).append(",").append(end);
        }
        queryBuilder.append(") as tl left join vendor_info ve on ve.cust_accno=tl.cust_no");
        List result = getSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean tbean = new TransactionBean();
            tbean.setCustomer((String) col[0]);
            tbean.setCustomerNo((String) col[1]);
            tbean.setBillofLadding((String) col[2]);
            tbean.setChargeCode((String) col[3]);
            tbean.setTransactionAmount(Double.parseDouble(col[4].toString().trim()));
            tbean.setInvoiceOrBl((String) col[5]);
            tbean.setTransactionId(col[6].toString());
            tbean.setContainerNo((String) col[7]);
            tbean.setVoyage((String) col[8]);
            tbean.setContact((String) col[9]);
            tbean.setStatus((String) col[10]);
            tbean.setSailingDate((Date) col[11]);
            if (col[12] != null && !col[12].toString().trim().equals("") && Integer.parseInt(col[12].toString()) > 0) {
                tbean.setChequenumber((String) col[12]);
            }
            tbean.setDocReceipt((String) col[13]);
            tbean.setCorrectionNotice(null != col[14] && CommonUtils.isNotEqual(col[14].toString(), FclBlConstants.CNA0) ? col[14].toString() : "");
            tbean.setCostId((String) col[15]);
            String desc = (String) col[16];
            tbean.setDescription(null != desc ? desc.replace("\r\n", "<br>").replace("\n", "<br>") : "");
            accruals.add(tbean);
        }
        return accruals;
    }

    public List<TransactionBean> getSubledgers(RecieptsLedgerForm recieptsLedgerForm) throws Exception {
        List<TransactionBean> subledgerTransactions = new ArrayList<TransactionBean>();
        Criteria criteria = this.createCriteriaForSubledgers(recieptsLedgerForm);
        if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getButtonValue(), "summary")) {
            criteria.addOrder(Order.asc("glAccountNumber"));
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), SUB_LEDGER_CODE_ACCRUALS)) {
            criteria.addOrder(Order.asc("glAccountNumber"));
            criteria.addOrder(Order.asc("custNo"));
            criteria.addOrder(Order.asc("sailingDate"));
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_GL_ACCOUNT)) {
            criteria.addOrder(Order.asc("glAccountNumber"));
            criteria.addOrder(Order.asc("custNo"));
            criteria.addOrder(Order.asc("transactionDate"));
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_VENDOR)) {
            criteria.addOrder(Order.asc("custNo"));
            criteria.addOrder(Order.asc("transactionDate"));
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_CHARGECODE)) {
            criteria.addOrder(Order.asc("chargeCode"));
            criteria.addOrder(Order.asc("custNo"));
            criteria.addOrder(Order.asc("transactionDate"));
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_SUB_LEDGER)) {
            criteria.addOrder(Order.asc("subledgerSourceCode"));
            criteria.addOrder(Order.asc("custNo"));
            criteria.addOrder(Order.asc("transactionDate"));
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_AR_BATCH_ID)) {
            criteria.addOrder(Order.desc("arBatchId"));
            criteria.addOrder(Order.asc("custNo"));
            criteria.addOrder(Order.asc("transactionDate"));
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_TRANSACTION_DATE)) {
            criteria.addOrder(Order.asc("transactionDate"));
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_BILL_OF_LADDING)) {
            criteria.addOrder(Order.asc("billLaddingNo"));
            criteria.addOrder(Order.asc("custNo"));
            criteria.addOrder(Order.asc("transactionDate"));
        } else {
            criteria.addOrder(Order.desc("apBatchId"));
            criteria.addOrder(Order.desc("arBatchId"));
        }
        List<TransactionLedger> result = criteria.list();
        if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getButtonValue(), "summary")) {
            Double creditAmount = 0d;
            Double debitAmount = 0d;
            Boolean canAdd = false;
            int index = 0;
            for (TransactionLedger transactionLedger : result) {
                if ((index + 1) < result.size()) {
                    TransactionLedger nextTransactionLedger = (TransactionLedger) result.get(index + 1);
                    if (CommonUtils.isNotEqual(transactionLedger.getGlAccountNumber(), nextTransactionLedger.getGlAccountNumber())) {
                        canAdd = true;
                    } else {
                        canAdd = false;
                    }
                } else {
                    canAdd = true;
                }
                if (CommonUtils.isEqualIgnoreCase(transactionLedger.getSubledgerSourceCode(), AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE)
                        || CommonUtils.isEqualIgnoreCase(transactionLedger.getSubledgerSourceCode(), AccountingConstants.SUBLEDGER_CODE_NETSETT)) {
                    if (transactionLedger.getTransactionAmt() < 0) {
                        creditAmount += Math.abs(transactionLedger.getTransactionAmt());
                    } else {
                        debitAmount += transactionLedger.getTransactionAmt();
                    }
                } else if ((CommonUtils.isNotEmpty(transactionLedger.getSubledgerSourceCode())
                        && (transactionLedger.getSubledgerSourceCode().startsWith(AccountingConstants.TRANSACTION_TYPE_AR)))) {
                    if (transactionLedger.getTransactionAmt() < 0) {
                        debitAmount += Math.abs(transactionLedger.getTransactionAmt());
                    } else {
                        creditAmount += transactionLedger.getTransactionAmt();
                    }
                } else if (CommonUtils.isEqualIgnoreCase(transactionLedger.getSubledgerSourceCode(), SUB_LEDGER_CODE_PURCHASE_JOURNAL)
                        || CommonUtils.isEqualIgnoreCase(transactionLedger.getSubledgerSourceCode(), SUB_LEDGER_CODE_ACCRUALS)) {
                    if (transactionLedger.getTransactionAmt() < 0) {
                        creditAmount += Math.abs(transactionLedger.getTransactionAmt());
                    } else {
                        debitAmount += transactionLedger.getTransactionAmt();
                    }
                } else if (CommonUtils.isNotEmpty(transactionLedger.getGlAccountNumber())) {
                    AccountDetails accountDetails = new AccountDetailsDAO().findById(transactionLedger.getGlAccountNumber());
                    if (null != accountDetails) {
                        if (CommonUtils.isEqualIgnoreCase(accountDetails.getNormalBalance(), AccountingConstants.NORMAL_BALANCE_DEBIT)) {
                            if (transactionLedger.getTransactionAmt() < 0) {
                                creditAmount += Math.abs(transactionLedger.getTransactionAmt());
                            } else {
                                debitAmount += transactionLedger.getTransactionAmt();
                            }
                        } else {
                            if (transactionLedger.getTransactionAmt() < 0) {
                                debitAmount += Math.abs(transactionLedger.getTransactionAmt());
                            } else {
                                creditAmount += transactionLedger.getTransactionAmt();
                            }
                        }
                    }
                }
                if (canAdd) {
                    TransactionBean transactionBean = new TransactionBean();
                    transactionBean.setGlAcctNo(transactionLedger.getGlAccountNumber());
                    transactionBean.setDebit(NumberUtils.formatNumber(debitAmount, "###,###,##0.00"));
                    transactionBean.setDebitAmount(debitAmount);
                    transactionBean.setCredit(NumberUtils.formatNumber(creditAmount, "###,###,##0.00"));
                    transactionBean.setCreditAmount(creditAmount);
                    Double totalAmount = debitAmount - creditAmount;
                    transactionBean.setAmount(NumberUtils.formatNumber(totalAmount, "###,###,##0.00"));
                    transactionBean.setTransactionAmount(totalAmount);
                    subledgerTransactions.add(transactionBean);
                    debitAmount = 0d;
                    creditAmount = 0d;
                    canAdd = false;
                }
                index++;
            }
        } else {
            for (TransactionLedger transactionLedger : result) {
                TransactionBean transactionBean = new TransactionBean(transactionLedger);
                transactionBean.setInvoiceOrBl(transactionLedger.getInvoiceNumber());
                transactionBean.setBillofLadding(transactionLedger.getBillLaddingNo());
                if (CommonUtils.isEqualIgnoreCase(transactionLedger.getSubledgerSourceCode(), AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE)
                        || CommonUtils.isEqualIgnoreCase(transactionLedger.getSubledgerSourceCode(), AccountingConstants.SUBLEDGER_CODE_NETSETT)) {
                    if (transactionBean.getAmount().contains("-")) {
                        transactionBean.setCredit(transactionBean.getAmount().replaceAll("-", ""));
                        transactionBean.setCreditAmount((-1) * transactionBean.getTransactionAmount());
                        transactionBean.setDebit(null);
                        transactionBean.setDebitAmount(null);
                    } else {
                        transactionBean.setDebit(transactionBean.getAmount());
                        transactionBean.setDebitAmount(transactionBean.getTransactionAmount());
                        transactionBean.setCredit(null);
                        transactionBean.setCreditAmount(null);
                    }
                } else if ((CommonUtils.isNotEmpty(transactionLedger.getSubledgerSourceCode())
                        && (transactionLedger.getSubledgerSourceCode().startsWith(AccountingConstants.TRANSACTION_TYPE_AR)))) {
                    if (transactionBean.getAmount().contains("-")) {
                        transactionBean.setDebit(transactionBean.getAmount().replaceAll("-", ""));
                        transactionBean.setDebitAmount((-1) * transactionBean.getTransactionAmount());
                        transactionBean.setCredit(null);
                        transactionBean.setCreditAmount(null);
                    } else {
                        transactionBean.setCredit(transactionBean.getAmount());
                        transactionBean.setCreditAmount(transactionBean.getTransactionAmount());
                        transactionBean.setDebit(null);
                        transactionBean.setDebitAmount(null);
                    }
                } else if (CommonUtils.isEqualIgnoreCase(transactionBean.getSubLedgerCode(), SUB_LEDGER_CODE_PURCHASE_JOURNAL)
                        || CommonUtils.isEqualIgnoreCase(transactionBean.getSubLedgerCode(), SUB_LEDGER_CODE_ACCRUALS)) {
                    if (transactionBean.getAmount().contains("-")) {
                        transactionBean.setCredit(transactionBean.getAmount().replaceAll("-", ""));
                        transactionBean.setCreditAmount((-1) * transactionBean.getTransactionAmount());
                        transactionBean.setDebit(null);
                        transactionBean.setDebitAmount(null);
                    } else {
                        transactionBean.setDebit(transactionBean.getAmount());
                        transactionBean.setDebitAmount(transactionBean.getTransactionAmount());
                        transactionBean.setCredit(null);
                        transactionBean.setCreditAmount(null);
                    }
                } else if (CommonUtils.isNotEmpty(transactionBean.getGlAcctNo())) {
                    AccountDetails accountDetails = new AccountDetailsDAO().findById(transactionBean.getGlAcctNo());
                    if (null != accountDetails) {
                        if (CommonUtils.isEqualIgnoreCase(accountDetails.getNormalBalance(), AccountingConstants.NORMAL_BALANCE_DEBIT)) {
                            if (transactionBean.getAmount().contains("-")) {
                                transactionBean.setCredit(transactionBean.getAmount().replaceAll("-", ""));
                                transactionBean.setCreditAmount((-1) * transactionBean.getTransactionAmount());
                                transactionBean.setDebit(null);
                                transactionBean.setDebitAmount(null);
                            } else {
                                transactionBean.setDebit(transactionBean.getAmount());
                                transactionBean.setDebitAmount(transactionBean.getTransactionAmount());
                                transactionBean.setCredit(null);
                                transactionBean.setCreditAmount(null);
                            }
                        } else {
                            if (transactionBean.getAmount().contains("-")) {
                                transactionBean.setDebit(transactionBean.getAmount().replaceAll("-", ""));
                                transactionBean.setDebitAmount((-1) * transactionBean.getTransactionAmount());
                                transactionBean.setCredit(null);
                                transactionBean.setCreditAmount(null);
                            } else {
                                transactionBean.setCredit(transactionBean.getAmount());
                                transactionBean.setCreditAmount(transactionBean.getTransactionAmount());
                                transactionBean.setDebit(null);
                                transactionBean.setDebitAmount(null);
                            }
                        }
                    }
                }
                subledgerTransactions.add(transactionBean);
            }
        }
        return subledgerTransactions;
    }

    public Criteria createCriteriaForSubledgers(RecieptsLedgerForm recieptsLedgerForm) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        Conjunction conditions = Restrictions.conjunction();
        if (CommonUtils.isEqual(recieptsLedgerForm.getButtonValue(), "getRecordsOfNullReportingDate")
                || CommonUtils.isEqual(recieptsLedgerForm.getButtonValue(), "exportRecordsOfNullReportingDate")) {
            conditions.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
            conditions.add(Restrictions.isNull("accrualsCorrectionFlag"));
            conditions.add(Restrictions.eq("subledgerSourceCode", SUB_LEDGER_CODE_ACCRUALS));
            conditions.add(Restrictions.isNull("sailingDate"));
        } else {
            Calendar startCal = new GregorianCalendar();
            startCal.setTime(DateUtils.parseDate(recieptsLedgerForm.getStartDate(), "MM/dd/yyyy"));
            startCal.set(Calendar.HOUR, 0);
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);
            Date startDate = startCal.getTime();
            Calendar endCal = new GregorianCalendar();
            endCal.setTime(DateUtils.parseDate(recieptsLedgerForm.getEndDate(), "MM/dd/yyyy"));
            endCal.set(Calendar.HOUR, 23);
            endCal.set(Calendar.MINUTE, 59);
            endCal.set(Calendar.SECOND, 59);
            Date endDate = endCal.getTime();
            if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), SUB_LEDGER_CODE_ACCRUALS)) {
                conditions.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
                conditions.add(Restrictions.isNull("accrualsCorrectionFlag"));
                conditions.add(Restrictions.eq("subledgerSourceCode", SUB_LEDGER_CODE_ACCRUALS));
                conditions.add(Restrictions.between("sailingDate", startDate, endDate));
                Disjunction statusDisjunction = Restrictions.disjunction();
                statusDisjunction.add(Restrictions.eq("status", STATUS_OPEN));
                statusDisjunction.add(Restrictions.eq("status", STATUS_PENDING));
                statusDisjunction.add(Restrictions.eq("status", STATUS_EDI_IN_PROGRESS));
                statusDisjunction.add(Restrictions.eq("status", STATUS_EDI_DISPUTE));
                statusDisjunction.add(Restrictions.eq("status", STATUS_DISPUTE));
                statusDisjunction.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
                Conjunction assignConjunction = Restrictions.conjunction();
                assignConjunction.add(Restrictions.or(Restrictions.eq("status", STATUS_ASSIGN),
                        Restrictions.eq("status", STATUS_EDI_ASSIGNED)));
                assignConjunction.add(Restrictions.gt("postedDate", endDate));
                statusDisjunction.add(assignConjunction);
                conditions.add(statusDisjunction);
            } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), SUB_LEDGER_CODE_PURCHASE_JOURNAL)
                    || CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), SUB_LEDGER_CODE_CASH_DEPOSIT)
                    || CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE)
                    || CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), AccountingConstants.SUBLEDGER_CODE_NETSETT)
                    || recieptsLedgerForm.getSubLedgerType().startsWith(AccountingConstants.TRANSACTION_TYPE_AR)) {
                conditions.add(Restrictions.eq("subledgerSourceCode", recieptsLedgerForm.getSubLedgerType()));
                Disjunction dateDisjunction = Restrictions.disjunction();
                dateDisjunction.add(Restrictions.between("postedDate", startDate, endDate));
                Conjunction txnDateConjunction = Restrictions.conjunction();
                txnDateConjunction.add(Restrictions.isNull("postedDate"));
                txnDateConjunction.add(Restrictions.between("transactionDate", startDate, endDate));
                dateDisjunction.add(txnDateConjunction);
                conditions.add(dateDisjunction);
                Disjunction statusDisjunction = Restrictions.disjunction();
                if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getPosted(), AccountingConstants.NO)) {
                    statusDisjunction.add(Restrictions.eq("status", STATUS_OPEN));
                    statusDisjunction.add(Restrictions.eq("status", STATUS_CHARGE_CODE));
                    if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), SUB_LEDGER_CODE_CASH_DEPOSIT)) {
                        statusDisjunction.add(Restrictions.eq("status", STATUS_PAID));
                    }
                } else {
                    conditions.add(Restrictions.ne("journalEntryNumber", ""));
                    conditions.add(Restrictions.ne("lineItemNumber", ""));
                    statusDisjunction.add(Restrictions.eq("status", STATUS_POSTED_TO_GL));
                    statusDisjunction.add(Restrictions.eq("status", STATUS_CHARGE_CODE_POSTED));
                }
                conditions.add(statusDisjunction);
            } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSubLedgerType(), ALL)) {
                Disjunction subledgerDisjunction = Restrictions.disjunction();
                Conjunction accSubledger = Restrictions.conjunction();
                accSubledger.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
                conditions.add(Restrictions.isNull("accrualsCorrectionFlag"));
                accSubledger.add(Restrictions.eq("subledgerSourceCode", SUB_LEDGER_CODE_ACCRUALS));
                if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getButtonValue(), "exportToExcel")) {
                    endCal.add(Calendar.MONTH, -12);
                    Date startDateForACC = endCal.getTime();
                    accSubledger.add(Restrictions.between("sailingDate", startDateForACC, endDate));
                } else {
                    accSubledger.add(Restrictions.between("sailingDate", startDate, endDate));
                }
                Disjunction accStatusDisjunction = Restrictions.disjunction();
                accStatusDisjunction.add(Restrictions.eq("status", STATUS_OPEN));
                accStatusDisjunction.add(Restrictions.eq("status", STATUS_PENDING));
                accStatusDisjunction.add(Restrictions.eq("status", STATUS_EDI_IN_PROGRESS));
                accStatusDisjunction.add(Restrictions.eq("status", STATUS_EDI_DISPUTE));
                accStatusDisjunction.add(Restrictions.eq("status", STATUS_DISPUTE));
                accStatusDisjunction.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
                Conjunction accAssignConjunction = Restrictions.conjunction();
                accAssignConjunction.add(Restrictions.or(Restrictions.eq("status", STATUS_ASSIGN),
                        Restrictions.eq("status", STATUS_EDI_ASSIGNED)));
                accAssignConjunction.add(Restrictions.gt("postedDate", endDate));
                accStatusDisjunction.add(accAssignConjunction);
                accSubledger.add(accStatusDisjunction);
                subledgerDisjunction.add(accSubledger);
                Conjunction otherSubledgers = Restrictions.conjunction();
                otherSubledgers.add(Restrictions.ne("subledgerSourceCode", ""));
                Disjunction othersDateDisjunction = Restrictions.disjunction();
                othersDateDisjunction.add(Restrictions.between("postedDate", startDate, endDate));
                Conjunction othersTxnDateConjunction = Restrictions.conjunction();
                othersTxnDateConjunction.add(Restrictions.isNull("postedDate"));
                othersTxnDateConjunction.add(Restrictions.between("transactionDate", startDate, endDate));
                othersDateDisjunction.add(othersTxnDateConjunction);
                otherSubledgers.add(othersDateDisjunction);
                Disjunction otherStatusDisjunction = Restrictions.disjunction();
                if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getPosted(), AccountingConstants.NO)) {
                    otherStatusDisjunction.add(Restrictions.eq("status", STATUS_OPEN));
                    otherStatusDisjunction.add(Restrictions.eq("status", STATUS_CHARGE_CODE));
                    otherStatusDisjunction.add(Restrictions.eq("status", STATUS_PAID));
                } else {
                    otherSubledgers.add(Restrictions.ne("journalEntryNumber", ""));
                    otherSubledgers.add(Restrictions.ne("lineItemNumber", ""));
                    otherStatusDisjunction.add(Restrictions.eq("status", STATUS_POSTED_TO_GL));
                    otherStatusDisjunction.add(Restrictions.eq("status", STATUS_CHARGE_CODE_POSTED));
                }
                otherSubledgers.add(otherStatusDisjunction);
                subledgerDisjunction.add(otherSubledgers);
                conditions.add(subledgerDisjunction);
            } else {
                conditions.add(Restrictions.or(Restrictions.eq("subledgerSourceCode", ""), Restrictions.isNull("subledgerSourceCode")));
            }
        }
        criteria.add(conditions);
        return criteria;
    }

    public boolean checkMissingGLAccountsForSubLedger(String sdate, String edate, String subLedgerType) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        Calendar startCal = new GregorianCalendar();
        startCal.setTime(DateUtils.parseDate(sdate, "MM/dd/yyyy"));
        startCal.set(Calendar.HOUR, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        Date startDate = startCal.getTime();
        Calendar endCal = new GregorianCalendar();
        endCal.setTime(DateUtils.parseDate(edate, "MM/dd/yyyy"));
        endCal.set(Calendar.HOUR, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        Date endDate = endCal.getTime();
        Conjunction conditions = Restrictions.conjunction();
        if (CommonUtils.isEqualIgnoreCase(subLedgerType, SUB_LEDGER_CODE_ACCRUALS)) {
            conditions.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
            conditions.add(Restrictions.isNull("accrualsCorrectionFlag"));
            conditions.add(Restrictions.eq("subledgerSourceCode", SUB_LEDGER_CODE_ACCRUALS));
            conditions.add(Restrictions.between("sailingDate", startDate, endDate));
            Disjunction statusDisjunction = Restrictions.disjunction();
            statusDisjunction.add(Restrictions.eq("status", STATUS_OPEN));
            statusDisjunction.add(Restrictions.eq("status", STATUS_PENDING));
            statusDisjunction.add(Restrictions.eq("status", STATUS_EDI_IN_PROGRESS));
            statusDisjunction.add(Restrictions.eq("status", STATUS_EDI_DISPUTE));
            statusDisjunction.add(Restrictions.eq("status", STATUS_DISPUTE));
            statusDisjunction.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
            Conjunction assignConjunction = Restrictions.conjunction();
            assignConjunction.add(Restrictions.or(Restrictions.eq("status", STATUS_ASSIGN),
                    Restrictions.eq("status", STATUS_EDI_ASSIGNED)));
            assignConjunction.add(Restrictions.gt("postedDate", endDate));
            statusDisjunction.add(assignConjunction);
            conditions.add(statusDisjunction);
        } else if (CommonUtils.isEqualIgnoreCase(subLedgerType, SUB_LEDGER_CODE_PURCHASE_JOURNAL)
                || CommonUtils.isEqualIgnoreCase(subLedgerType, SUB_LEDGER_CODE_CASH_DEPOSIT)
                || CommonUtils.isEqualIgnoreCase(subLedgerType, AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE)
                || CommonUtils.isEqualIgnoreCase(subLedgerType, AccountingConstants.SUBLEDGER_CODE_NETSETT)
                || subLedgerType.startsWith(AccountingConstants.TRANSACTION_TYPE_AR)) {
            conditions.add(Restrictions.eq("subledgerSourceCode", subLedgerType));
            Disjunction dateDisjunction = Restrictions.disjunction();
            dateDisjunction.add(Restrictions.between("postedDate", startDate, endDate));
            Conjunction txnDateConjunction = Restrictions.conjunction();
            txnDateConjunction.add(Restrictions.isNull("postedDate"));
            txnDateConjunction.add(Restrictions.between("transactionDate", startDate, endDate));
            dateDisjunction.add(txnDateConjunction);
            conditions.add(dateDisjunction);
            Disjunction statusDisjunction = Restrictions.disjunction();
            statusDisjunction.add(Restrictions.eq("status", STATUS_OPEN));
            statusDisjunction.add(Restrictions.eq("status", STATUS_CHARGE_CODE));
            if (CommonUtils.isEqualIgnoreCase(subLedgerType, SUB_LEDGER_CODE_CASH_DEPOSIT)) {
                statusDisjunction.add(Restrictions.eq("status", STATUS_PAID));
            }
            conditions.add(statusDisjunction);
        } else if (CommonUtils.isEqualIgnoreCase(subLedgerType, ALL)) {
            Disjunction subledgerDisjunction = Restrictions.disjunction();
            Conjunction accSubledger = Restrictions.conjunction();
            accSubledger.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
            conditions.add(Restrictions.isNull("accrualsCorrectionFlag"));
            accSubledger.add(Restrictions.eq("subledgerSourceCode", SUB_LEDGER_CODE_ACCRUALS));
            accSubledger.add(Restrictions.ne("glAccountNumber", ""));
            accSubledger.add(Restrictions.between("sailingDate", startDate, endDate));
            Disjunction accStatusDisjunction = Restrictions.disjunction();
            accStatusDisjunction.add(Restrictions.eq("status", STATUS_OPEN));
            accStatusDisjunction.add(Restrictions.eq("status", STATUS_PENDING));
            accStatusDisjunction.add(Restrictions.eq("status", STATUS_EDI_IN_PROGRESS));
            accStatusDisjunction.add(Restrictions.eq("status", STATUS_EDI_DISPUTE));
            accStatusDisjunction.add(Restrictions.eq("status", STATUS_DISPUTE));
            accStatusDisjunction.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
            Conjunction accAssignConjunction = Restrictions.conjunction();
            accAssignConjunction.add(Restrictions.or(Restrictions.eq("status", STATUS_ASSIGN),
                    Restrictions.eq("status", STATUS_EDI_ASSIGNED)));
            accAssignConjunction.add(Restrictions.gt("postedDate", endDate));
            accStatusDisjunction.add(accAssignConjunction);
            accSubledger.add(accStatusDisjunction);
            subledgerDisjunction.add(accSubledger);

            Conjunction otherSubledgers = Restrictions.conjunction();
            otherSubledgers.add(Restrictions.ne("subledgerSourceCode", ""));
            Disjunction othersDateDisjunction = Restrictions.disjunction();
            othersDateDisjunction.add(Restrictions.between("postedDate", startDate, endDate));
            Conjunction othersTxnDateConjunction = Restrictions.conjunction();
            othersTxnDateConjunction.add(Restrictions.isNull("postedDate"));
            othersTxnDateConjunction.add(Restrictions.between("transactionDate", startDate, endDate));
            othersDateDisjunction.add(othersTxnDateConjunction);
            otherSubledgers.add(othersDateDisjunction);
            Disjunction otherStatusDisjunction = Restrictions.disjunction();
            otherStatusDisjunction.add(Restrictions.eq("status", STATUS_OPEN));
            otherStatusDisjunction.add(Restrictions.eq("status", STATUS_CHARGE_CODE));
            otherStatusDisjunction.add(Restrictions.eq("status", STATUS_PAID));
            otherSubledgers.add(otherStatusDisjunction);
            subledgerDisjunction.add(otherSubledgers);
            conditions.add(subledgerDisjunction);
        } else {
            conditions.add(Restrictions.or(Restrictions.eq("subledgerSourceCode", ""), Restrictions.isNull("subledgerSourceCode")));
        }
        conditions.add(Restrictions.or(Restrictions.eq("glAccountNumber", ""), Restrictions.isNull("glAccountNumber")));
        criteria.add(conditions);
        List<TransactionLedger> result = criteria.list();
        if (CommonUtils.isNotEmpty(result)) {
            return true;
        }
        return false;
    }

    public List<TransactionBean> getPJSubLedgers(RecieptsLedgerForm recieptsLedgerForm) throws Exception {
        List<TransactionBean> pjSubledgers = new ArrayList<TransactionBean>();
        Calendar startCal = new GregorianCalendar();
        startCal.setTime(DateUtils.parseDate(recieptsLedgerForm.getStartDate(), "MM/dd/yyyy"));
        startCal.set(Calendar.HOUR, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        String startDate = DateUtils.formatDate(startCal.getTime(), "yyyy-MM-dd HH:mm:ss");
        Calendar endCal = new GregorianCalendar();
        endCal.setTime(DateUtils.parseDate(recieptsLedgerForm.getEndDate(), "MM/dd/yyyy"));
        endCal.set(Calendar.HOUR, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        String endDate = DateUtils.formatDate(endCal.getTime(), "yyyy-MM-dd HH:mm:ss");
        StringBuilder queryBuilder = new StringBuilder("");
        queryBuilder.append("select tbl.Cust_name,tbl.Cust_no,tbl.Ap_Batch_Id,tbl.Ar_Batch_Id,tbl.GL_account_number,tbl.Bill_Ladding_No,");
        queryBuilder.append("tbl.Invoice_number,tbl.Voyage_No,tbl.Charge_Code,tbl.Transaction_type,tbl.Transaction_date,tbl.posted_date,");
        if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_CHARGECODE)) {
            queryBuilder.append("tbl.Transaction_amt,");
        } else {
            queryBuilder.append("sum(tbl.Transaction_amt),");
        }
        queryBuilder.append("tbl.notes,tbl.posted_to_gl_date,tbl.sailing_date from");
        queryBuilder.append("(select tl.Cust_name,tl.Cust_no,tl.Ap_Batch_Id,tl.Ar_Batch_Id,tl.GL_account_number,");
        queryBuilder.append("tl.Bill_Ladding_No,tl.Invoice_number,tl.Voyage_No,tl.Charge_Code,tl.Transaction_type,tl.Transaction_date,tl.posted_date,");
        queryBuilder.append("tl.Transaction_amt,api.notes,tl.posted_to_gl_date,tl.sailing_date from transaction_ledger tl");
        queryBuilder.append(" left join ap_invoice api on api.invoice_number=tl.Invoice_number and api.notes is not null");
        queryBuilder.append(" and api.account_number=tl.Cust_no and api.status='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append(" where ((posted_date between '").append(startDate).append("' and '").append(endDate).append("')");
        queryBuilder.append(" or (posted_date is null and transaction_date between '").append(startDate).append("' and '").append(endDate).append("'))");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(recieptsLedgerForm.getSubLedgerType()).append("'");
        if (recieptsLedgerForm.getPosted().equals("no")) {
            queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_CHARGE_CODE).append("')");
        } else {
            queryBuilder.append(" and (tl.Status='").append(STATUS_POSTED_TO_GL).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_CHARGE_CODE_POSTED).append("')");
            queryBuilder.append(" and journal_Entry_Number is not null and line_Item_Number is not null");
        }
        queryBuilder.append(" group by tl.Transaction_Id order by tl.Transaction_Id) as tbl");
        if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_CHARGECODE)) {
            queryBuilder.append(" order by tbl.Charge_Code,tbl.Transaction_date");
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_GL_ACCOUNT)) {
            queryBuilder.append(" group by tbl.GL_account_number,tbl.Invoice_number order by tbl.GL_account_number,tbl.Cust_no,tbl.Transaction_date");
        } else if (CommonUtils.isEqualIgnoreCase(recieptsLedgerForm.getSortBy(), SORT_BY_VENDOR)) {
            queryBuilder.append(" group by tbl.Cust_no,tbl.Invoice_number order by tbl.Cust_no,tbl.Transaction_date");
        } else {
            queryBuilder.append(" group by tbl.GL_account_number,tbl.Invoice_number order by tbl.GL_account_number,tbl.Cust_no,tbl.Transaction_date");
        }
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            TransactionBean tbean = new TransactionBean();
            tbean.setCustomer(null != col[0] ? col[0].toString() : "");
            tbean.setCustomerNo(null != col[1] ? col[1].toString() : "");
            if (null != col[2] && !col[2].toString().trim().equals("")
                    && !col[2].toString().trim().equals("0")) {
                tbean.setApBatchId(Integer.parseInt(col[2].toString()));
            }
            if (null != col[3] && !col[3].toString().trim().equals("")) {
                tbean.setArBatchId(Integer.parseInt(col[3].toString()));
            }
            tbean.setGlAcctNo(null != col[4] ? col[4].toString() : "");
            tbean.setBillofLadding(null != col[5] ? col[5].toString() : "");
            tbean.setInvoiceOrBl(null != col[6] ? col[6].toString() : "");
            tbean.setVoyagenumber(null != col[7] ? col[7].toString() : "");
            tbean.setChargeCode(null != col[8] ? col[8].toString() : "");
            tbean.setRecordType(null != col[9] ? col[9].toString() : "");
            tbean.setTransDate(null != col[10] ? DateUtils.formatDate(DateUtils.parseDate(col[10].toString(), "yyyy-MM-dd"), "MM/dd/yyyy") : "");
            tbean.setPostedDate((Date) col[11]);
            tbean.setAmount(null != col[12] ? NumberUtils.formatNumber(Double.parseDouble(col[12].toString()), "###,###,##0.00") : "");
            tbean.setInvoiceNotes(null != col[13] ? col[13].toString() : "");
            tbean.setPostedToGlDate((Date) col[14]);
            tbean.setSailingDate((Date) col[15]);
            tbean.setSubLedgerCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
            pjSubledgers.add(tbean);
        }
        return pjSubledgers;
    }

    public List<TransactionBean> getSubledgersForJournalEntry(String journalEntryId) throws Exception {
        List<TransactionBean> subledgers = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder("select tlh from TransactionLedgerHistory tlh,JournalEntry je,LineItem li");
        queryBuilder.append(" where tlh.lineItemId=li.lineItemId and li.journalEntryId=je.journalEntryId");
        queryBuilder.append(" and je.journalEntryId ='").append(journalEntryId).append("'");
        List<TransactionLedgerHistory> result = getCurrentSession().createQuery(queryBuilder.toString()).list();
        for (TransactionLedgerHistory transactionLedgerHistory : result) {
            TransactionBean transactionBean = new TransactionBean(transactionLedgerHistory);
            transactionBean.setJournalentryNo(journalEntryId);
            if (CommonUtils.isEqualIgnoreCase(transactionLedgerHistory.getSubledger(), AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE)
                    || CommonUtils.isEqualIgnoreCase(transactionLedgerHistory.getSubledger(), AccountingConstants.SUBLEDGER_CODE_NETSETT)) {
                if (transactionBean.getAmount().contains("-")) {
                    transactionBean.setCredit(transactionBean.getAmount().replaceAll("-", ""));
                    transactionBean.setCreditAmount((-1) * transactionBean.getTransactionAmount());
                    transactionBean.setDebit(null);
                    transactionBean.setDebitAmount(null);
                } else {
                    transactionBean.setDebit(transactionBean.getAmount());
                    transactionBean.setDebitAmount(transactionBean.getTransactionAmount());
                    transactionBean.setCredit(null);
                    transactionBean.setCreditAmount(null);
                }
            } else if ((CommonUtils.isNotEmpty(transactionLedgerHistory.getSubledger())
                    && (transactionLedgerHistory.getSubledger().startsWith(AccountingConstants.TRANSACTION_TYPE_AR)))) {
                if (transactionBean.getAmount().contains("-")) {
                    transactionBean.setDebit(transactionBean.getAmount().replaceAll("-", ""));
                    transactionBean.setDebitAmount((-1) * transactionBean.getTransactionAmount());
                    transactionBean.setCredit(null);
                    transactionBean.setCreditAmount(null);
                } else {
                    transactionBean.setCredit(transactionBean.getAmount());
                    transactionBean.setCreditAmount(transactionBean.getTransactionAmount());
                    transactionBean.setDebit(null);
                    transactionBean.setDebitAmount(null);
                }
            } else if (CommonUtils.isEqualIgnoreCase(transactionBean.getSubLedgerCode(), SUB_LEDGER_CODE_PURCHASE_JOURNAL)
                    || CommonUtils.isEqualIgnoreCase(transactionBean.getSubLedgerCode(), SUB_LEDGER_CODE_ACCRUALS)) {
                if (transactionBean.getAmount().contains("-")) {
                    transactionBean.setCredit(transactionBean.getAmount().replaceAll("-", ""));
                    transactionBean.setCreditAmount((-1) * transactionBean.getTransactionAmount());
                    transactionBean.setDebit(null);
                    transactionBean.setDebitAmount(null);
                } else {
                    transactionBean.setDebit(transactionBean.getAmount());
                    transactionBean.setDebitAmount(transactionBean.getTransactionAmount());
                    transactionBean.setCredit(null);
                    transactionBean.setCreditAmount(null);
                }
            } else if (CommonUtils.isNotEmpty(transactionBean.getGlAcctNo())) {
                AccountDetails accountDetails = new AccountDetailsDAO().findById(transactionBean.getGlAcctNo());
                if (null != accountDetails) {
                    if (CommonUtils.isEqualIgnoreCase(accountDetails.getNormalBalance(), AccountingConstants.NORMAL_BALANCE_DEBIT)) {
                        if (transactionBean.getAmount().contains("-")) {
                            transactionBean.setCredit(transactionBean.getAmount().replaceAll("-", ""));
                            transactionBean.setCreditAmount((-1) * transactionBean.getTransactionAmount());
                            transactionBean.setDebit(null);
                            transactionBean.setDebitAmount(null);
                        } else {
                            transactionBean.setDebit(transactionBean.getAmount());
                            transactionBean.setDebitAmount(transactionBean.getTransactionAmount());
                            transactionBean.setCredit(null);
                            transactionBean.setCreditAmount(null);
                        }
                    } else {
                        if (transactionBean.getAmount().contains("-")) {
                            transactionBean.setDebit(transactionBean.getAmount().replaceAll("-", ""));
                            transactionBean.setDebitAmount((-1) * transactionBean.getTransactionAmount());
                            transactionBean.setCredit(null);
                            transactionBean.setCreditAmount(null);
                        } else {
                            transactionBean.setCredit(transactionBean.getAmount());
                            transactionBean.setCreditAmount(transactionBean.getTransactionAmount());
                            transactionBean.setDebit(null);
                            transactionBean.setDebitAmount(null);
                        }
                    }
                }
            }
            subledgers.add(transactionBean);
        }
        return subledgers;
    }

    public List<AccountingBean> getInvalidSubledgersForPosting(String subledgerType, String startDate, String endDate) throws Exception {
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(startDate, "MM/dd/yyyy"), "yyyy-MM-dd");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(endDate, "MM/dd/yyyy"), "yyyy-MM-dd");
        List<AccountingBean> transactions = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tl.Transaction_Id,tl.Subledger_Source_code,tl.Bill_Ladding_No,tl.Invoice_number,tl.Charge_Code,tl.Voyage_No,");
        queryBuilder.append("date_format(tl.Transaction_date,'%m/%d/%Y'),date_format(tl.sailing_date,'%m/%d/%Y'),date_format(tl.posted_date,'%m/%d/%Y'),");
        queryBuilder.append("format(tl.Transaction_amt,2) as amount,format(if(tl.Transaction_amt>0,tl.Transaction_amt,0),2) as debit,");
        queryBuilder.append("format(if(tl.Transaction_amt<0,-tl.Transaction_amt,0),2) as credit,tl.Transaction_type");
        queryBuilder.append(" from transaction_ledger tl where (tl.GL_account_number not in (select account from account_details)");
        queryBuilder.append(" or tl.GL_account_number is null or tl.GL_account_number='')");
        if (CommonUtils.isEqualIgnoreCase(subledgerType, ALL)) {
            queryBuilder.append(" and tl.Subledger_Source_code is not null");
        } else {
            queryBuilder.append(" and tl.Subledger_Source_code='").append(subledgerType).append("'");
        }
        if (StringUtils.equalsIgnoreCase(subledgerType, SUB_LEDGER_CODE_ACCRUALS)) {
            queryBuilder.append(" and tl.accruals_correction_flag is null and tl.Transaction_amt<>0");
            queryBuilder.append(" and tl.Transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
            queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_PENDING).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_DISPUTE).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_EDI_IN_PROGRESS).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_EDI_DISPUTE).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_IN_PROGRESS).append("'");
            queryBuilder.append(" or ((tl.Status='").append(STATUS_ASSIGN).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_EDI_ASSIGNED).append("')");
            queryBuilder.append(" and date_format(tl.posted_date,'%Y-%m-%d') > '").append(toDate).append("'))");
            queryBuilder.append(" and date_format(tl.sailing_date,'%Y-%m-%d') between '").append(fromDate).append("' and '").append(toDate).append("'");
        } else {
            queryBuilder.append(" and ((date_format(tl.posted_date,'%Y-%m-%d') between '").append(fromDate).append("' and '").append(toDate).append("')");
            queryBuilder.append(" or (tl.posted_date is null and (date_format(tl.Transaction_date,'%Y-%m-%d') between '").append(fromDate).append("'");
            queryBuilder.append(" and '").append(toDate).append("'))) and tl.Transaction_amt<>0");
            queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
            queryBuilder.append(" or tl.Status='").append(STATUS_CHARGE_CODE).append("'");
            if (StringUtils.equalsIgnoreCase(subledgerType, SUB_LEDGER_CODE_CASH_DEPOSIT)) {
                queryBuilder.append(" or tl.Status='").append(STATUS_PAID).append("'");
            }
            queryBuilder.append(")");
        }
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean transaction = new AccountingBean();
            transaction.setTransactionId(col[0].toString());
            transaction.setSubledgerCode((String) col[1]);
            transaction.setBillOfLadding((String) col[2]);
            transaction.setInvoiceNumber((String) col[3]);
            transaction.setChargeCode((String) col[4]);
            transaction.setVoyage((String) col[5]);
            transaction.setFormattedDate((String) col[6]);
            transaction.setFormattedReportingDate((String) col[7]);
            transaction.setFormattedPostedDate((String) col[8]);
            transaction.setFormattedAmount((String) col[9]);
            transaction.setFormattedDebit((String) col[10]);
            transaction.setFormattedCredit((String) col[11]);
            transaction.setTransactionType((String) col[12]);
            transactions.add(transaction);
        }
        return transactions;
    }

    public List<SubledgerModel> getARSubledgers(String subledgerCode, String startDate, String endDate) {
        List<SubledgerModel> subledgers = new ArrayList<SubledgerModel>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(sum(tl.Transaction_amt)<0,-sum(tl.Transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.Transaction_amt)>0,sum(tl.Transaction_amt),0) as credit,ad.Account,ad.Acct_Desc");
        queryBuilder.append(" from transaction_ledger tl,account_details ad where tl.GL_account_number=ad.Account");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(subledgerCode).append("'");
        queryBuilder.append(" and tl.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and date_format(tl.posted_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" group by tl.GL_account_number order by tl.GL_account_number");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            SubledgerModel subledger = new SubledgerModel();
            subledger.setDebit(Double.parseDouble(col[0].toString()));
            subledger.setCredit(Double.parseDouble(col[1].toString()));
            subledger.setGlAccount(col[2].toString());
            subledger.setGlAccountDesc(col[3].toString());
            subledgers.add(subledger);
        }
        return subledgers;
    }

    private List<TransactionLedger> getARTransactionledgers(String subledgerCode, String glAccount, String startDate, String endDate) {
        StringBuilder queryBuilder = new StringBuilder("from TransactionLedger tl");
        queryBuilder.append(" where tl.glAccountNumber='").append(glAccount).append("'");
        queryBuilder.append(" and tl.subledgerSourceCode='").append(subledgerCode).append("'");
        queryBuilder.append(" and tl.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and date_format(tl.postedDate,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" order by tl.glAccountNumber");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<SubledgerModel> getPJSubledgers(String startDate, String endDate) {
        List<SubledgerModel> subledgers = new ArrayList<SubledgerModel>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(sum(tl.Transaction_amt)>0,sum(tl.Transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.Transaction_amt)<0,-sum(tl.Transaction_amt),0) as credit,ad.Account,ad.Acct_Desc");
        queryBuilder.append(" from transaction_ledger tl,account_details ad where tl.GL_account_number=ad.Account");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(SUB_LEDGER_CODE_PURCHASE_JOURNAL).append("'");
        queryBuilder.append(" and tl.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and ((date_format(tl.posted_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("')");
        queryBuilder.append(" or (tl.posted_date is null and (date_format(tl.Transaction_date,'%Y-%m-%d') between '").append(startDate).append("'");
        queryBuilder.append(" and '").append(endDate).append("')))");
        queryBuilder.append(" group by tl.GL_account_number order by tl.GL_account_number");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            SubledgerModel subledger = new SubledgerModel();
            subledger.setDebit(Double.parseDouble(col[0].toString()));
            subledger.setCredit(Double.parseDouble(col[1].toString()));
            subledger.setGlAccount(col[2].toString());
            subledger.setGlAccountDesc(col[3].toString());
            subledgers.add(subledger);
        }
        return subledgers;
    }

    private List<TransactionLedger> getPJTransactionledgers(String glAccount, String startDate, String endDate) {
        StringBuilder queryBuilder = new StringBuilder("from TransactionLedger tl");
        queryBuilder.append(" where tl.glAccountNumber='").append(glAccount).append("'");
        queryBuilder.append(" and tl.subledgerSourceCode='").append(SUB_LEDGER_CODE_PURCHASE_JOURNAL).append("'");
        queryBuilder.append(" and tl.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and ((date_format(tl.postedDate,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("')");
        queryBuilder.append(" or (tl.postedDate is null and (date_format(tl.transactionDate,'%Y-%m-%d') between '").append(startDate).append("'");
        queryBuilder.append(" and '").append(endDate).append("')))");
        queryBuilder.append(" order by tl.glAccountNumber");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<SubledgerModel> getACCSubledgers(String startDate, String endDate) {
        List<SubledgerModel> subledgers = new ArrayList<SubledgerModel>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(sum(tl.Transaction_amt)>0,sum(tl.Transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.Transaction_amt)<0,-sum(tl.Transaction_amt),0) as credit,ad.Account,ad.Acct_Desc");
        queryBuilder.append(" from transaction_ledger tl,account_details ad where tl.GL_account_number=ad.Account");
        queryBuilder.append(" and tl.accruals_correction_flag is null");
        queryBuilder.append(" and tl.Transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(SUB_LEDGER_CODE_ACCRUALS).append("'");
        queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_PENDING).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_EDI_IN_PROGRESS).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_EDI_DISPUTE).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_DISPUTE).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_IN_PROGRESS).append("'");
        queryBuilder.append(" or ((tl.Status='").append(STATUS_ASSIGN).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_EDI_ASSIGNED).append("')");
        queryBuilder.append(" and date_format(tl.posted_date,'%Y-%m-%d') > '").append(endDate).append("'))");
        queryBuilder.append(" and date_format(tl.sailing_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" group by tl.GL_account_number order by tl.GL_account_number");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            SubledgerModel subledger = new SubledgerModel();
            subledger.setDebit(Double.parseDouble(col[0].toString()));
            subledger.setCredit(Double.parseDouble(col[1].toString()));
            subledger.setGlAccount(col[2].toString());
            subledger.setGlAccountDesc(col[3].toString());
            subledgers.add(subledger);
        }
        return subledgers;
    }

    private List<TransactionLedger> getACCTransactionledgers(String glAccount, String startDate, String endDate) {
        StringBuilder queryBuilder = new StringBuilder("from TransactionLedger tl");
        queryBuilder.append(" where tl.glAccountNumber='").append(glAccount).append("'");
        queryBuilder.append(" and tl.accrualsCorrectionFlag is null");
        queryBuilder.append(" and tl.transactionType='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.subledgerSourceCode='").append(SUB_LEDGER_CODE_ACCRUALS).append("'");
        queryBuilder.append(" and (tl.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_PENDING).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_EDI_IN_PROGRESS).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_EDI_DISPUTE).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_DISPUTE).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_IN_PROGRESS).append("'");
        queryBuilder.append(" or ((tl.status='").append(STATUS_ASSIGN).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_EDI_ASSIGNED).append("')");
        queryBuilder.append(" and date_format(tl.postedDate,'%Y-%m-%d') > '").append(endDate).append("'))");
        queryBuilder.append(" and date_format(tl.sailingDate,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" order by tl.glAccountNumber");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<SubledgerModel> getRCTSubledgers(String startDate, String endDate) {
        List<SubledgerModel> subledgers = new ArrayList<SubledgerModel>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(sum(tl.Transaction_amt)<0,-sum(tl.Transaction_amt),0) as credit,");
        queryBuilder.append("if(sum(tl.Transaction_amt)>0,sum(tl.Transaction_amt),0) as debit,ad.Account,ad.Acct_Desc");
        queryBuilder.append(" from transaction_ledger tl,account_details ad where tl.GL_account_number=ad.Account");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE).append("'");
        queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_CHARGE_CODE).append("')");
        queryBuilder.append(" and date_format(tl.posted_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" group by tl.GL_account_number order by tl.GL_account_number");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            SubledgerModel subledger = new SubledgerModel();
            subledger.setCredit(Double.parseDouble(col[0].toString()));
            subledger.setDebit(Double.parseDouble(col[1].toString()));
            subledger.setGlAccount(col[2].toString());
            subledger.setGlAccountDesc(col[3].toString());
            subledgers.add(subledger);
        }
        return subledgers;
    }

    private List<TransactionLedger> getRCTTransactionledgers(String glAccount, String startDate, String endDate) {
        StringBuilder queryBuilder = new StringBuilder("from TransactionLedger tl");
        queryBuilder.append(" where tl.glAccountNumber='").append(glAccount).append("'");
        queryBuilder.append(" and tl.subledgerSourceCode='").append(AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE).append("'");
        queryBuilder.append(" and (tl.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_CHARGE_CODE).append("')");
        queryBuilder.append(" and date_format(tl.postedDate,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" order by tl.glAccountNumber");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<SubledgerModel> getCDSubledgers(String startDate, String endDate) {
        List<SubledgerModel> subledgers = new ArrayList<SubledgerModel>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("(select if(sum(tl.Transaction_amt)>0,sum(tl.Transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.Transaction_amt)<0,-sum(tl.Transaction_amt),0) as credit,ad.Account,ad.Acct_Desc");
        queryBuilder.append(" from transaction_ledger tl,account_details ad where tl.GL_account_number=ad.Account and ad.Normal_Balance='Debit'");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(SUB_LEDGER_CODE_CASH_DEPOSIT).append("'");
        queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_PAID).append("')");
        queryBuilder.append(" and date_format(tl.posted_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" group by tl.GL_account_number order by tl.GL_account_number)");
        queryBuilder.append(" union ");
        queryBuilder.append("(select if(sum(tl.Transaction_amt)<0,-sum(tl.Transaction_amt),0) as debit,");
        queryBuilder.append("if(sum(tl.Transaction_amt)>0,sum(tl.Transaction_amt),0) as credit,ad.Account,ad.Acct_Desc");
        queryBuilder.append(" from transaction_ledger tl,account_details ad where tl.GL_account_number=ad.Account and ad.Normal_Balance='Credit'");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(SUB_LEDGER_CODE_CASH_DEPOSIT).append("'");
        queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_PAID).append("')");
        queryBuilder.append(" and date_format(tl.posted_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" group by tl.GL_account_number order by tl.GL_account_number)");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            SubledgerModel subledger = new SubledgerModel();
            subledger.setDebit(Double.parseDouble(col[0].toString()));
            subledger.setCredit(Double.parseDouble(col[1].toString()));
            subledger.setGlAccount(col[2].toString());
            subledger.setGlAccountDesc(col[3].toString());
            subledgers.add(subledger);
        }
        return subledgers;
    }

    private List<TransactionLedger> getCDTransactionledgers(String glAccount, String startDate, String endDate) {
        StringBuilder queryBuilder = new StringBuilder("from TransactionLedger tl");
        queryBuilder.append(" where tl.glAccountNumber='").append(glAccount).append("'");
        queryBuilder.append(" and tl.subledgerSourceCode='").append(SUB_LEDGER_CODE_CASH_DEPOSIT).append("'");
        queryBuilder.append(" and (tl.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_PAID).append("')");
        queryBuilder.append(" and date_format(tl.postedDate,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" order by tl.glAccountNumber");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<SubledgerModel> getNetSettSubledgers(String startDate, String endDate) {
        List<SubledgerModel> subledgers = new ArrayList<SubledgerModel>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select if(sum(tl.Transaction_amt)<0,-sum(tl.Transaction_amt),0) as credit,");
        queryBuilder.append("if(sum(tl.Transaction_amt)>0,sum(tl.Transaction_amt),0) as debit,ad.Account,ad.Acct_Desc");
        queryBuilder.append(" from transaction_ledger tl,account_details ad where tl.GL_account_number=ad.Account");
        queryBuilder.append(" and tl.Subledger_Source_code='").append(AccountingConstants.SUBLEDGER_CODE_NETSETT).append("'");
        queryBuilder.append(" and (tl.Status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.Status='").append(STATUS_CHARGE_CODE).append("')");
        queryBuilder.append(" and date_format(tl.posted_date,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" group by tl.GL_account_number order by tl.GL_account_number");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            SubledgerModel subledger = new SubledgerModel();
            subledger.setCredit(Double.parseDouble(col[0].toString()));
            subledger.setDebit(Double.parseDouble(col[1].toString()));
            subledger.setGlAccount(col[2].toString());
            subledger.setGlAccountDesc(col[3].toString());
            subledgers.add(subledger);
        }
        return subledgers;
    }

    private List<TransactionLedger> getNetSettTransactionledgers(String glAccount, String startDate, String endDate) {
        StringBuilder queryBuilder = new StringBuilder("from TransactionLedger tl");
        queryBuilder.append(" where tl.glAccountNumber='").append(glAccount).append("'");
        queryBuilder.append(" and tl.subledgerSourceCode='").append(AccountingConstants.SUBLEDGER_CODE_NETSETT).append("'");
        queryBuilder.append(" and (tl.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" or tl.status='").append(STATUS_CHARGE_CODE).append("')");
        queryBuilder.append(" and date_format(tl.postedDate,'%Y-%m-%d') between '").append(startDate).append("' and '").append(endDate).append("'");
        queryBuilder.append(" order by tl.glAccountNumber");
        return getCurrentSession().createQuery(queryBuilder.toString()).list();
    }

    public List<TransactionLedger> getTransactionledgers(String subledgerCode, String glAccount, String startDate, String endDate) {
        if (subledgerCode.startsWith(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE)) {
            return getARTransactionledgers(subledgerCode, glAccount, startDate, endDate);
        } else if (StringUtils.equalsIgnoreCase(subledgerCode, SUB_LEDGER_CODE_PURCHASE_JOURNAL)) {
            return getPJTransactionledgers(glAccount, startDate, endDate);
        } else if (StringUtils.equalsIgnoreCase(subledgerCode, SUB_LEDGER_CODE_ACCRUALS)) {
            return getACCTransactionledgers(glAccount, startDate, endDate);
        } else if (StringUtils.equalsIgnoreCase(subledgerCode, SUB_LEDGER_CODE_CASH_DEPOSIT)) {
            return getCDTransactionledgers(glAccount, startDate, endDate);
        } else if (StringUtils.equalsIgnoreCase(subledgerCode, AccountingConstants.AR_BATCH_CLOSING_SUBLEDGER_CODE)) {
            return getRCTTransactionledgers(glAccount, startDate, endDate);
        } else if (StringUtils.equalsIgnoreCase(subledgerCode, AccountingConstants.SUBLEDGER_CODE_NETSETT)) {
            return getNetSettTransactionledgers(glAccount, startDate, endDate);
        } else {
            return null;
        }
    }

    public List<TransactionBean> getCostsAgainstDirectGlForReconcile(ReconcileForm reconcileForm) throws Exception {
        String date = DateUtils.formatDate(DateUtils.parseDate(reconcileForm.getBankReconcileDate(), "MM/dd/yyyy"), "yyyy-MM-dd");
        List<TransactionBean> reconcileList = new ArrayList<TransactionBean>();
        StringBuilder queryBuilder = new StringBuilder("select cast(group_concat(tl.Transaction_Id)as char) as transactionIds,");
        queryBuilder.append("tl.Invoice_number as reference,");
        queryBuilder.append("format(if(sum(tl.Transaction_amt)>0,sum(tl.Transaction_amt),0),2) as debit,");
        queryBuilder.append("format(if(sum(tl.Transaction_amt)<0,-sum(tl.Transaction_amt),0),2) as credit,");
        queryBuilder.append("date_format(tl.Transaction_date,'%m/%d/%Y') as date,");
        queryBuilder.append("if(tl.reconciled='").append(STATUS_IN_PROGRESS).append("','");
        queryBuilder.append(STATUS_RECONCILE_IN_PROGRESS).append("',tl.Status) as status");
        queryBuilder.append(" from transaction_ledger tl where tl.GL_account_number='").append(reconcileForm.getGlAccountNumber()).append("'");
        queryBuilder.append(" and date_format(tl.Transaction_date,'%Y-%m-%d')<='").append(date).append("'");
        queryBuilder.append(" and tl.Transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and tl.Status='").append(STATUS_ASSIGN).append("' and tl.direct_gl_account=1");
        queryBuilder.append(" and (tl.reconciled='").append(STATUS_IN_PROGRESS).append("'");
        queryBuilder.append(" or tl.reconciled='").append(NO).append("' or tl.reconciled is null)");
        queryBuilder.append(" and (tl.cleared='").append(NO).append("' or tl.cleared is null)");
        queryBuilder.append(" group by tl.cust_no,tl.Invoice_number order by tl.cust_no,tl.Invoice_number");
        getCurrentSession().flush();
        List<Object> resultList = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : resultList) {
            Object[] col = (Object[]) row;
            TransactionBean transactionBean = new TransactionBean();
            String transactionId = col[0].toString();
            transactionId = StringUtils.replace(transactionId, ",", "@" + MODULE_TRANSACTION_LEDGER + ",");
            transactionId += "@" + MODULE_TRANSACTION_LEDGER;
            transactionBean.setTransactionId(transactionId);
            transactionBean.setPaymentMethod("AP Invoice");
            transactionBean.setCustomerReference(col[1].toString());
            transactionBean.setBatchId("");
            transactionBean.setDebit(col[2].toString());
            transactionBean.setCredit(col[3].toString());
            transactionBean.setTransDate(col[4].toString());
            transactionBean.setStatus(col[5].toString());
            reconcileList.add(transactionBean);
        }
        return reconcileList;
    }

    public List<AccountingBean> getInvoiceOrBlDetails(String customerNumber, String invoiceOrBl) {
        List<AccountingBean> charges = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder("select tl.cust_no,tl.Cust_name,");
        queryBuilder.append("if(tl.Bill_Ladding_No!='',tl.Bill_Ladding_No,tl.Invoice_number) as invoiceOrBl,tl.Transaction_type,");
        queryBuilder.append("date_format(tl.Transaction_date,'%m/%d/%Y') as invoiceDate,date_format(tl.posted_date,'%m/%d/%Y') as postedDate,");
        queryBuilder.append("tl.Charge_Code,tl.Gl_account_number,tl.Subledger_Source_code,");
        if (invoiceOrBl.startsWith(AccountingConstants.SUBLEDGER_CODE_NETSETT)) {
            queryBuilder.append("format(tl.Transaction_amt,2) as amount,tl.description");
            queryBuilder.append(" from transaction_ledger tl join account_details ad on tl.GL_account_number=ad.Account");
            String arBatchId = invoiceOrBl.replaceAll(AccountingConstants.SUBLEDGER_CODE_NETSETT, "");
            queryBuilder.append(" where tl.Subledger_Source_code='").append(AccountingConstants.SUBLEDGER_CODE_NETSETT).append("'");
            queryBuilder.append(" and tl.ar_batch_id=").append(arBatchId).append(" and tl.invoice_number!='").append(invoiceOrBl).append("'");
        } else {
            queryBuilder.append("format(tl.Transaction_amt,2) as amount,tl.description from transaction_ledger tl ");
            queryBuilder.append(" where tl.cust_no='").append(customerNumber).append("'");
            queryBuilder.append(" and tl.Subledger_Source_code like '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("%'");
            queryBuilder.append(" and (tl.bill_ladding_no='").append(invoiceOrBl).append("' or tl.invoice_number='").append(invoiceOrBl).append("')");
        }
        queryBuilder.append(" order by tl.transaction_id,tl.transaction_date");
        List<Object> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean accountingBean = new AccountingBean();
            accountingBean.setCustomerNumber((String) col[0]);
            accountingBean.setCustomerName((String) col[1]);
            accountingBean.setInvoiceOrBl((String) col[2]);
            accountingBean.setTransactionType((String) col[3]);
            accountingBean.setFormattedDate((String) col[4]);
            accountingBean.setFormattedPostedDate((String) col[5]);
            accountingBean.setChargeCode((String) col[6]);
            accountingBean.setGlAccountNumber((String) col[7]);
            accountingBean.setSubledgerCode((String) col[8]);
            accountingBean.setFormattedAmount((String) col[9]);
            accountingBean.setVoyage((String) col[10]);
            charges.add(accountingBean);
        }
        return charges;
    }

    public String getDocReceiptsForVoyage(String voyageNo) {
        StringBuilder queryBuilder = new StringBuilder("select distinct(concat(\"'\",drcpt,\"'\")) from transaction_ledger");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and drcpt != ''");
        queryBuilder.append(" and voyage_no like '%").append(voyageNo).append("%'");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            return result.toString().replace("[", "(").replace("]", ")");
        }
        return null;
    }

    /**
     * Get Paid Accruals
     *
     * @param vendorNumber param invoiceNumber
     */
    public void setPaidDateForAccrualCost(String vendorNumber, String invoiceNumber, String paidDate) throws Exception {
        StringBuilder queryBuilder = new StringBuilder("select cost_id from transaction_ledger");
        queryBuilder.append(" where cust_no = '").append(vendorNumber).append("'");
        queryBuilder.append(" and Invoice_number = '").append(invoiceNumber).append("'");
        queryBuilder.append(" and Transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and (status = '").append(STATUS_ASSIGN).append("'");
        queryBuilder.append(" or status = '").append(STATUS_EDI_ASSIGNED).append("')");
        queryBuilder.append(" and cost_id is not null");

        queryBuilder.append(" and shipment_type in ('FCLE','FCLI')");
        List<Integer> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            String costIds = result.toString().replace("[", "(").replace("]", ")");
            String updateQuery = "update fcl_bl_costcodes set date_paid='" + paidDate + "' where code_id in " + costIds;
            getCurrentSession().createSQLQuery(updateQuery).executeUpdate();
        }
        queryBuilder = new StringBuilder("select cost_id from transaction_ledger");
        queryBuilder.append(" where cust_no = '").append(vendorNumber).append("'");
        queryBuilder.append(" and Invoice_number = '").append(invoiceNumber).append("'");
        queryBuilder.append(" and Transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and (status = '").append(STATUS_ASSIGN).append("'");
        queryBuilder.append(" or status = '").append(STATUS_EDI_ASSIGNED).append("')");
        queryBuilder.append(" and cost_id is not null");
        queryBuilder.append(" and shipment_type in ('LCLE','LCLI')");
        result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        if (CommonUtils.isNotEmpty(result)) {
            String costIds = result.toString().replace("[", "(").replace("]", ")");
            queryBuilder = new StringBuilder();
            queryBuilder.append("update");
            queryBuilder.append("  lcl_booking_ac b");
            queryBuilder.append("  set b.posted_datetime = '").append(paidDate).append("'");
            queryBuilder.append("  where b.id in ").append(costIds);
            getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
        }
    }

    public String validateGlAccount(String transactionIds) {
        StringBuilder queryString = new StringBuilder("select count(*) from (select tl.transaction_id,ad.account from transaction_ledger tl");
        queryString.append(" left join account_details ad on ad.account=tl.gl_account_number");
        queryString.append(" where tl.transaction_id in (").append(transactionIds).append(")) as t where isnull(t.account)");
        Object result = getSession().createSQLQuery(queryString.toString()).uniqueResult();
        return (null == result || Integer.parseInt(result.toString()) > 0) ? "inValid" : "valid";
    }

    public List<TransactionLedger> getDisputedAccruals(String vendorNumber, String invoiceNumber) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.add(Restrictions.eq("status", STATUS_DISPUTE));
        return criteria.list();
    }

    public List<TransactionLedger> getInProgressAccruals(String vendorNumber, String invoiceNumber) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
        return criteria.list();
    }

    public void setConvertToApFlagInSSMasterApprovedBL(List<String> ids) {
        StringBuilder query = new StringBuilder("update fcl_bl bl,transaction_ledger tl");
        query.append(" set bl.converted_to_ap=1");
        query.append(" where bl.converted_to_ap=0 and tl.Bill_Ladding_No=bl.BolId");
        query.append(" and tl.Charge_Code='OCNFRT'");
        query.append(" and tl.transaction_id in ").append(ids.toString().replace("[", "(").replace("]", ")"));
        getCurrentSession().flush();
        getCurrentSession().createSQLQuery(query.toString()).executeUpdate();
    }

    public List<TransactionLedger> getInvalidGLAccountAccruals(String transactionIds) {
        List<TransactionLedger> list = new ArrayList<TransactionLedger>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("from TransactionLedger where glAccountNumber not in (select account from AccountDetails)");
        queryBuilder.append(" and transactionId in (").append(transactionIds).append(")");
        List<TransactionLedger> resultList = getSession().createQuery(queryBuilder.toString()).list();
        for (TransactionLedger accruals : resultList) {
            list.add(accruals);
        }
        return list;
    }

    public String getShipmentTypeForAR(String invoiceOrBl) {
        StringBuilder query = new StringBuilder("select shipment_type");
        query.append(" from transaction_ledger");
        query.append(" where (bill_ladding_no='").append(invoiceOrBl).append("'");
        query.append(" or invoice_number='").append(invoiceOrBl).append("')");
        query.append(" and transaction_type = 'AR'");
        query.append(" and subledger_source_code <> 'NET SETT' and shipment_type!='CN'");
        query.append(" limit 1");
        return (String) getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
    }

    public List<CostModel> getCostsForInvoices(String vendorNo, List<String> invoiceNumbers) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  `invoice_number` as invoiceNumber,");
        queryBuilder.append("  date_format(`transaction_date`, '%m/%d/%Y') as costDate,");
        queryBuilder.append("  `charge_code` as costCode,");
        queryBuilder.append("  format(sum(`transaction_amt`), 2) as costAmount ");
        queryBuilder.append("from");
        queryBuilder.append("  `transaction_ledger` ");
        queryBuilder.append("where");
        queryBuilder.append(" `cust_no` = :vendorNo");
        queryBuilder.append("  and `invoice_number` in (:invoiceNumbers)");
        queryBuilder.append("  and `transaction_type` = '").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
        queryBuilder.append("  and `subledger_source_code` = '").append(SUB_LEDGER_CODE_PURCHASE_JOURNAL).append("' ");
        queryBuilder.append("group by `invoice_number`, `charge_code`");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("vendorNo", vendorNo);
        query.setParameterList("invoiceNumbers", invoiceNumbers);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("costDate", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("costAmount", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CostModel.class));
        return query.list();
    }
}
