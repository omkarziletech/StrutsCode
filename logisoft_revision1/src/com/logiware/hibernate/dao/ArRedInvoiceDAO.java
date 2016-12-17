package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionLedgerDAO;
import com.logiware.accounting.dao.AccrualsDAO;
import com.logiware.form.ARRedInvoiceForm;
import com.logiware.hibernate.domain.ArRedInvoice;
import com.logiware.hibernate.domain.ArRedInvoiceCharges;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;

/**
 * Data access object (DAO) for domain model class ApInvoice.
 *
 * @see com.gp.cvst.logisoft.hibernate.dao.ApInvoice
 * @author MyEclipse - Hibernate Tools
 */
public class ArRedInvoiceDAO extends BaseHibernateDAO implements ConstantsInterface {
    // property constants

    /**
     * @param transientInstance
     */
    public void save(ArRedInvoice arRedInvoice) throws Exception {
        getSession().save(arRedInvoice);
        getSession().flush();
    }

    /**
     * @param persistentInstance
     */
    public void delete(ArRedInvoice arRedInvoice) throws Exception {
        getSession().delete(arRedInvoice);
        getSession().flush();
    }

    /**
     *
     * @param updateInstance
     */
    public void saveOrUpdate(ArRedInvoice arRedInvoice) throws Exception {
        getSession().saveOrUpdate(arRedInvoice);
        getSession().flush();
    }

    public ArRedInvoice saveAndReturn(ArRedInvoice arRedInvoice) throws Exception {
        getSession().saveOrUpdate(arRedInvoice);
        getSession().flush();
        return arRedInvoice;
    }

    /**
     * @param id
     * @return
     */
    public ArRedInvoice findById(Integer id) throws Exception {
        ArRedInvoice instance = (ArRedInvoice) getSession().get("com.logiware.hibernate.domain.ArRedInvoice", id);
        return instance;
    }

    /**
     * @param instance
     * @return
     */
    public List findByProperty(String propertyName, Object value) throws Exception {
        String queryString = "from ArRedInvoice as model where model." + propertyName + " = ?0";
        Query queryObject = getSession().createQuery(queryString);
        queryObject.setParameter("0", value);
        return queryObject.list();
    }

    public void update(ArRedInvoice arRedInvoice) throws Exception {
        getSession().update(arRedInvoice);
        getSession().flush();
    }

    /**
     * @param invoiceNumber
     * @param invoiceId
     * @return
     */
    public ArRedInvoice findByInvoiceNumber(String invoiceNumber, String invoiceId) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ArRedInvoice.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (invoiceNumber != null && !invoiceNumber.trim().equals("")) {
            criteria.add(Restrictions.like("invoiceNumber", invoiceNumber));
        }
        if (invoiceId != null && !invoiceId.trim().equals("")) {
            criteria.add(Restrictions.like("id", new Integer(invoiceId)));
        }
        return (ArRedInvoice) criteria.setMaxResults(1).uniqueResult();
    }

    public String findingBolByFileNo(String fileNum) throws Exception {
        String qeury = "SELECT bol FROM fcl_bl WHERE file_no='" + fileNum + "' limit 1";
        Object bol = getCurrentSession().createSQLQuery(qeury).uniqueResult();
        return null != bol ? bol.toString() : "";
    }

    public ArRedInvoice findInvoiceByInvoiceNumber(String invoiceNumber, String vendorNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ArRedInvoice.class);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        if (invoiceNumber != null && !invoiceNumber.trim().equals("")) {
            criteria.add(Restrictions.like("invoiceNumber", invoiceNumber.trim()));
        }
        if (vendorNumber != null && !vendorNumber.trim().equals("")) {
            criteria.add(Restrictions.like("customerNumber", vendorNumber.trim()));
        }
        return (ArRedInvoice) criteria.setMaxResults(1).uniqueResult();
    }

    /**
     * @param customerName
     * @param accountNumber
     * @return
     */
    public List<ArRedInvoice> getInvoices(String customerName, String accountNumber, String invoiceNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ArRedInvoice.class);
        if (CommonUtils.isNotEmpty(customerName)) {
            criteria.add(Restrictions.like("customerName", customerName.trim() + "%"));
        }
        if (CommonUtils.isNotEmpty(accountNumber)) {
            criteria.add(Restrictions.like("customerNumber", accountNumber.trim() + "%"));
        }
        if (CommonUtils.isNotEmpty(invoiceNumber)) {
            criteria.add(Restrictions.like("invoiceNumber", "%" + invoiceNumber.trim() + "%"));
        }
        return criteria.list();
    }

    public String findMaxInvoiceNumber() throws Exception {
        String queryString = "select max(invoice_number) from ar_red_invoice";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object invoiceNumber = queryObject.uniqueResult();
        return null != invoiceNumber ? invoiceNumber.toString() : "";
    }

    public String findLastInvoiceNumber() throws Exception {
        String queryString = "select max(invoice_number) from ar_red_invoice";
        Query queryObject = getSession().createSQLQuery(queryString);
        Object invoiceNumber = queryObject.uniqueResult();
        return null != invoiceNumber ? invoiceNumber.toString() : "";
    }

    public List findInvoiceNumberByDrNumber(String drNumber) throws Exception {
        drNumber = "04-" + drNumber;
        String queryString = "select distinct(invoice_number) from ar_red_invoice_charges where bl_dr_number = '" + drNumber + "' order by invoice_number";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }
    
    public List searchAllInvoice(String drNumber) throws Exception {
        String queryString = "select distinct(invoice_number) from ar_red_invoice_charges where bl_dr_number like '%" + drNumber + "%'order by invoice_number";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }

    public List searchInvoice(ARRedInvoiceForm arRedInvoiceForm) throws Exception {
        String queryString = "from ArRedInvoice ";
        boolean where = false;
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getAccountNumber())) {
            where = true;
            queryString += "where customerNumber like '" + arRedInvoiceForm.getAccountNumber() + "%' ";
        }
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getInvoiceNumber())) {
            if (!where) {
                queryString += "where ";
                where = true;
            } else {
                queryString += "and ";
            }
            String invoiceNo = arRedInvoiceForm.getInvoiceNumber().trim().replace("'", "''");
            queryString += "invoiceNumber like '" + invoiceNo + "%' ";
        }
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getFileNo())) {
            if (!where) {
                queryString += "where ";
                where = true;
            } else {
                queryString += "and ";
            }
            String fileNo = arRedInvoiceForm.getFileNo().trim().replace("'", "''");
            queryString += "fileNo like '" + fileNo + "%' ";
        }
        if (CommonUtils.isNotEmpty(arRedInvoiceForm.getInvoiceStartdate()) && CommonUtils.isNotEmpty(arRedInvoiceForm.getInvoiceEnddate())) {
            if (!where) {
                queryString += "where ";
                where = true;
            } else {
                queryString += "and ";
            }
            String startDate = DateUtils.formatDate(DateUtils.parseDate(arRedInvoiceForm.getInvoiceStartdate(), "MM/dd/yyyy"), "yyyy-MM-dd");
            String endDate = DateUtils.formatDate(DateUtils.parseDate(arRedInvoiceForm.getInvoiceEnddate(), "MM/dd/yyyy"), "yyyy-MM-dd");
            queryString += "date BETWEEN '" + startDate + "' AND '" + endDate + "' ";
        }
        if ("Posted".equalsIgnoreCase(arRedInvoiceForm.getInvoices())) {
            if (!where) {
                queryString += "where ";
                where = true;
            } else {
                queryString += "and ";
            }
            queryString += "status='AR'";
        } else if ("UnPosted".equalsIgnoreCase(arRedInvoiceForm.getInvoices())) {
            if (!where) {
                queryString += "where ";
                where = true;
            } else {
                queryString += "and ";
            }
            queryString += "status IS NULL OR  status != 'AR' ";
        }
        if ("I".equalsIgnoreCase(arRedInvoiceForm.getFileType())) {
            if (!where) {
                queryString += "where ";
                where = true;
            } else {
                queryString += "and ";
            }
            queryString += "fileType = 'I' ";
        } else {
            if (!where) {
                queryString += "where ";
                where = true;
            } else {
                queryString += "and ";
            }
            queryString += "(fileType IS NULL OR  fileType != 'I') ";
        }
        if ("orderByNo".equalsIgnoreCase(arRedInvoiceForm.getOrderBy())) {
            queryString += "ORDER BY invoiceNumber DESC";
        } else {
            queryString += "ORDER BY date DESC";
        }
        Query queryObject = getSession().createQuery(queryString);
        return queryObject.list();
    }

    public void lclManifestAccruals(ArRedInvoice arRedInvoice, List lineItemList, String userName, Object[] voyageValues, String fileNo) throws Exception {
        Date invoiceDate;
        if (voyageValues != null && voyageValues[5] != null && !voyageValues[5].toString().trim().equals("")) {
            invoiceDate = DateUtils.parseStringToDateWithTime(voyageValues[5].toString());
        } else {
            invoiceDate = arRedInvoice.getDate();
        }
        Date postedDate = new AccrualsDAO().getPostedDate(invoiceDate);
        TransactionDAO transactionDAO = new TransactionDAO();
        TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
        Transaction transaction = transactionDAO.findByTransactionByInvoiceNoAndCustomer(arRedInvoice.getInvoiceNumber(), arRedInvoice.getCustomerNumber());
        double transactionAmount = 0d;
        double balance = 0d;
        double balanceInProcess = 0d;
        if (null != transaction) {
            transactionAmount = transaction.getTransactionAmt();
            balance = transaction.getBalance();
            balanceInProcess = transaction.getBalanceInProcess();
            transaction.setManifestFlag("R");
            setVoyageUnitValues(transaction, voyageValues);
        } else {
            transaction = new Transaction();
            transaction.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
            transaction.setTransactionDate(invoiceDate);
            transaction.setPostedDate(postedDate);
            transaction.setDueDate(arRedInvoice.getDueDate());
            transaction.setTransactionAmt(0d);
            transaction.setBalance(0d);
            transaction.setBalanceInProcess(0d);
            transaction.setCustName(arRedInvoice.getCustomerName());
            transaction.setCustNo(arRedInvoice.getCustomerNumber());
            transaction.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            transaction.setStatus(STATUS_OPEN);
            transaction.setBillTo("Y");
            String creditStatus = new CustomerAccountingDAO().getCreditStatus(transaction.getCustNo());
            transaction.setCreditHold(CommonUtils.isEqualIgnoreCase(creditStatus, "In Good Standing") ? "N" : "Y");
            transaction.setManifestFlag("R");
            transaction.setEmailed(false);
            transaction.setDocReceipt(fileNo);
            transaction.setBookingNo(fileNo);
            setVoyageUnitValues(transaction, voyageValues);
            transactionDAO.save(transaction);
        }
        double chargesAmount = 0d;
        for (Object object : lineItemList) {
            TransactionLedger transactionLedger = new TransactionLedger();
            ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
            chargesAmount += arRedInvoiceCharges.getAmount();
            transactionLedger.setChargeCode(arRedInvoiceCharges.getChargeCode());
            transactionLedger.setTransactionDate(transaction.getTransactionDate());
            transactionLedger.setPostedDate(postedDate);
            transactionLedger.setGlAccountNumber(arRedInvoiceCharges.getGlAccount());
            transactionLedger.setTransactionAmt(arRedInvoiceCharges.getAmount());
            transactionLedger.setBalance(arRedInvoiceCharges.getAmount());
            transactionLedger.setBalanceInProcess(arRedInvoiceCharges.getAmount());
            transactionLedger.setCurrencyCode(CURRENCY_CODE);
            transactionLedger.setSubledgerSourceCode(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "-" + arRedInvoiceCharges.getShipmentType());
            transactionLedger.setShipmentType(arRedInvoiceCharges.getShipmentType());
            transactionLedger.setCustName(arRedInvoice.getCustomerName());
            transactionLedger.setCustNo(arRedInvoice.getCustomerNumber());
            transactionLedger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            transactionLedger.setStatus(STATUS_OPEN);
            transactionLedger.setBillTo("Y");
            transactionLedger.setManifestFlag("R");
            transactionLedger.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
            transactionLedger.setVoyageNo(transaction.getVoyageNo());
            transactionLedger.setContainerNo(transaction.getContainerNo());
            transactionLedger.setVesselNo(transaction.getVesselNo());
            if (CommonUtils.isNotEmpty(arRedInvoiceCharges.getBlDrNumber())) {
                transactionLedger.setDocReceipt(arRedInvoiceCharges.getBlDrNumber().replace("-", ""));
            }
            transactionLedgerDAO.save(transactionLedger);
        }
        transaction.setTransactionAmt(transactionAmount + chargesAmount);
        transaction.setBalance(balance + chargesAmount);
        transaction.setBalanceInProcess(balanceInProcess + chargesAmount);
        transactionDAO.update(transaction);
        ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
        arTransactionHistory.setInvoiceDate(transaction.getTransactionDate());
        arTransactionHistory.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
        arTransactionHistory.setPostedDate(postedDate);
        arTransactionHistory.setTransactionDate(transaction.getTransactionDate());
        arTransactionHistory.setTransactionAmount(chargesAmount);
        arTransactionHistory.setCustomerNumber(transaction.getCustNo());
        arTransactionHistory.setVoyageNumber(transaction.getVoyageNo());
        arTransactionHistory.setCreatedBy(userName);
        arTransactionHistory.setCreatedDate(new Date());
        arTransactionHistory.setTransactionType(LclCommonConstant.LCL_SHIPMENT_TYPE_IMPORT);
        new ArTransactionHistoryDAO().save(arTransactionHistory);
    }

    public void lclUnManifestAccruals(ArRedInvoice arRedInvoice, List lineItemList, String userName, Object[] voyageValues) throws Exception {
        Date invoiceDate;
        if (voyageValues != null && voyageValues[5] != null && !voyageValues[5].toString().trim().equals("")) {
            invoiceDate = DateUtils.parseStringToDateWithTime(voyageValues[5].toString());
        } else {
            invoiceDate = arRedInvoice.getDate();
        }
        Date postedDate = new AccrualsDAO().getPostedDate(invoiceDate);
        TransactionDAO transactionDAO = new TransactionDAO();
        TransactionLedgerDAO transactionLedgerDAO = new TransactionLedgerDAO();
        Transaction transaction = transactionDAO.findByTransactionByInvoiceNoAndCustomer(arRedInvoice.getInvoiceNumber(), arRedInvoice.getCustomerNumber());
        double transactionAmount = 0d;
        double balance = 0d;
        double balanceInProcess = 0d;
        transactionAmount = transaction.getTransactionAmt();
        balance = transaction.getBalance();
        balanceInProcess = transaction.getBalanceInProcess();
        transaction.setManifestFlag("N");
        setVoyageUnitValues(transaction, voyageValues);
        double chargesAmount = 0d;
        for (Object object : lineItemList) {
            TransactionLedger transactionLedger = new TransactionLedger();
            ArRedInvoiceCharges arRedInvoiceCharges = (ArRedInvoiceCharges) object;
            chargesAmount += arRedInvoiceCharges.getAmount();
            transactionLedger.setChargeCode(arRedInvoiceCharges.getChargeCode());
            transactionLedger.setTransactionDate(transaction.getTransactionDate());
            transactionLedger.setPostedDate(postedDate);
            transactionLedger.setGlAccountNumber(arRedInvoiceCharges.getGlAccount());
            transactionLedger.setTransactionAmt(-arRedInvoiceCharges.getAmount());
            transactionLedger.setBalance(-arRedInvoiceCharges.getAmount());
            transactionLedger.setBalanceInProcess(-arRedInvoiceCharges.getAmount());
            transactionLedger.setCurrencyCode(CURRENCY_CODE);
            transactionLedger.setSubledgerSourceCode(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "-" + arRedInvoiceCharges.getShipmentType());
            transactionLedger.setShipmentType(arRedInvoiceCharges.getShipmentType());
            transactionLedger.setCustName(arRedInvoice.getCustomerName());
            transactionLedger.setCustNo(arRedInvoice.getCustomerNumber());
            transactionLedger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            transactionLedger.setStatus(STATUS_OPEN);
            transactionLedger.setBillTo("Y");
            transactionLedger.setManifestFlag("N");
            transactionLedger.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
            transactionLedger.setVoyageNo(transaction.getVoyageNo());
            transactionLedger.setContainerNo(transaction.getContainerNo());
            transactionLedger.setVesselNo(transaction.getVesselNo());
            transactionLedgerDAO.save(transactionLedger);
        }
        transaction.setTransactionAmt(transactionAmount - chargesAmount);
        transaction.setBalance(balance - chargesAmount);
        transaction.setBalanceInProcess(balanceInProcess - chargesAmount);
        transactionDAO.update(transaction);
        ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
        arTransactionHistory.setInvoiceDate(invoiceDate);
        arTransactionHistory.setInvoiceNumber(arRedInvoice.getInvoiceNumber());
        arTransactionHistory.setPostedDate(postedDate);
        arTransactionHistory.setTransactionDate(invoiceDate);
        arTransactionHistory.setTransactionAmount(-chargesAmount);
        arTransactionHistory.setCustomerNumber(arRedInvoice.getCustomerNumber());
        arTransactionHistory.setCreatedBy(userName);
        arTransactionHistory.setCreatedDate(new Date());
        arTransactionHistory.setTransactionType(STATUS_VOID);
        new ArTransactionHistoryDAO().save(arTransactionHistory);
    }

    public String[] isLclARInvoice(String fileNumber, String screenName) throws Exception {
        String countValues[] = new String[2];
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CONVERT(COUNT(*) using utf8) FROM ar_red_invoice WHERE file_no =").append(fileNumber).append(" AND ready_to_post='M' AND screen_name='").append(screenName).append("'");
        sb.append(" UNION ALL ");
        sb.append("SELECT CONVERT(COUNT(*) using utf8) FROM ar_red_invoice WHERE file_no = ").append(fileNumber).append(" AND ready_to_post IS NULL AND screen_name='").append(screenName).append("'");
        Query queryObject = getCurrentSession().createSQLQuery(sb.toString());
        List<String> countList = queryObject.list();
        Object[] obArray = countList.toArray();
        countValues = Arrays.copyOf(obArray, obArray.length, String[].class);
        return countValues;
    }

    private void setVoyageUnitValues(Transaction transaction, Object[] voyageValues) {
        if (voyageValues != null && voyageValues.length > 0) {
            if (voyageValues[0] != null && !voyageValues[0].toString().trim().equals("")) {
                transaction.setVoyageNo(voyageValues[0].toString());
            }else if(voyageValues[6] != null && !voyageValues[6].toString().trim().equals("")){
                 transaction.setVoyageNo(voyageValues[6].toString());
            }
            if (voyageValues[1] != null && !voyageValues[1].toString().trim().equals("")) {
                transaction.setContainerNo(voyageValues[1].toString());
                transaction.setCustomerReferenceNo(voyageValues[1].toString());
            }
            if (voyageValues[2] != null && !voyageValues[2].toString().trim().equals("")) {
                transaction.setVesselNo(voyageValues[2].toString());
            }
            if (voyageValues[3] != null && !voyageValues[3].toString().trim().equals("")) {
                transaction.setVesselName(voyageValues[3].toString());
            }
            if (voyageValues[4] != null && !voyageValues[4].toString().trim().equals("")) {
                transaction.setMasterBl(voyageValues[4].toString());
            }
        }
    }

    public ArRedInvoice getInvoice(String customerNumber, String invoiceNumber) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(ArRedInvoice.class);
        criteria.add(Restrictions.eq("customerNumber", customerNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.addOrder(Order.asc("id"));
        criteria.setMaxResults(1);
        return (ArRedInvoice) criteria.uniqueResult();
    }

    public boolean isArInvoiceByFileNo(String fileNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT if(COUNT(*)>0,'true','false') as result FROM ar_red_invoice WHERE bl_number='").append(fileNo).append("' ORDER BY id LIMIT 1 ");
        String count = (String) getCurrentSession().createSQLQuery(sb.toString()).addScalar("result", StringType.INSTANCE).uniqueResult();
        return Boolean.valueOf(count);
    }

    public List<ArRedInvoice> getArInvoiceDetails(String screenName, String fileId) throws Exception {
        Criteria criteria = getSession().createCriteria(ArRedInvoice.class, "arRedInvoice");
        criteria.add(Restrictions.eq("fileNo", fileId));
        criteria.add(Restrictions.eq("screenName", screenName));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
    
    public String getfileNumber(String groupFileNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" GROUP_CONCAT(arc.`bl_dr_number`) AS fileNumber");
        sb.append(" FROM");
        sb.append("  ar_red_invoice ar ");
        sb.append("  JOIN `ar_red_invoice_charges` arc ");
        sb.append("    ON (");
        sb.append("      ar.`id` = arc.`ar_red_invoice_id`");
        sb.append("    ) ");
        sb.append(" WHERE ar.`status` <> 'AR' ");
        sb.append("  AND arc.`bl_dr_number` IN (").append(groupFileNo).append(")");
        SQLQuery queryObject =getCurrentSession().createSQLQuery(sb.toString());
        queryObject.addScalar("fileNumber", StringType.INSTANCE);
        return (String) queryObject.uniqueResult();
    }
    public List<Integer> getArInvoiceId(String groupFileNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" ar.id AS arInvoiceId");
        sb.append(" FROM");
        sb.append("  ar_red_invoice ar ");
        sb.append("  JOIN `ar_red_invoice_charges` arc ");
        sb.append("    ON (");
        sb.append("      ar.`id` = arc.`ar_red_invoice_id`");
        sb.append("    ) ");
        sb.append(" WHERE ar.`status` = 'AR' ");
        sb.append("  AND arc.`bl_dr_number` IN (").append(groupFileNo).append(")");
        SQLQuery queryObject =getCurrentSession().createSQLQuery(sb.toString());
        return queryObject.list();
    }
    
    public List getArInvoiceDetails(List<Integer> arInvoiceId) throws Exception {
        String queryString = "from ArRedInvoice where id in (:arInvoiceId)";
        Query query = getSession().createQuery(queryString);
        query.setParameterList("arInvoiceId", arInvoiceId);
        List list = query.list();
        return list;
    }
    
    public List findLclInvoiceNumberByDrNumber(List<Integer> arInvoiceId) throws Exception {
        String queryString = "select distinct(invoice_number) from ar_red_invoice_charges where ar_red_invoice_id IN (" + arInvoiceId.toString().replace("[", "").replace("]", "") + ") order by invoice_number";
        Query queryObject = getSession().createSQLQuery(queryString);
        return queryObject.list();
    }
    public List<String> getInvoiceNo(String groupFileNo) throws Exception {
    String query = "SELECT invoice_number as invoiceNumber FROM `ar_red_invoice` WHERE bl_number IN (:fileNumber) AND file_type='LCLE'";
    SQLQuery queryObject =getCurrentSession().createSQLQuery(query);
    queryObject.setParameter("fileNumber", groupFileNo);
    queryObject.addScalar("invoiceNumber", StringType.INSTANCE);
    return queryObject.list();
    }
}
