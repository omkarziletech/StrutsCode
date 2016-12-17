package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.bc.fcl.FclBlUtil;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclSsAc;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsAcDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsRemarksDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.accounting.domain.ApInvoice;
import com.logiware.accounting.domain.ApInvoiceLineItem;
import com.logiware.accounting.domain.EdiInvoice;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.accounting.form.AccrualsForm;
import com.logiware.accounting.form.EdiInvoiceForm;
import com.logiware.accounting.model.AccrualModel;
import com.logiware.accounting.model.ManifestModel;
import com.logiware.accounting.model.ResultModel;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ApTransactionHistoryDAO;
import com.logiware.hibernate.dao.ArBatchDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ApTransactionHistory;
import com.logiware.hibernate.domain.ArTransactionHistory;
import com.logiware.utils.ArCreditHoldUtils;
import com.logiware.utils.AuditNotesUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Naryanan
 */
public class AccrualsDAO extends BaseHibernateDAO implements ConstantsInterface {

    private static final Logger log = Logger.getLogger(AccrualsDAO.class);

    public TransactionLedger findById(Integer id) {
        getCurrentSession().flush();
        return (TransactionLedger) getCurrentSession().get(TransactionLedger.class, id);
    }

    public TransactionLedger findById(String id) {
        return findById(Integer.parseInt(id));
    }

    public void save(TransactionLedger transientInstance) {
        getCurrentSession().save(transientInstance);
    }

    public void update(TransactionLedger persistentInstance) {
        getCurrentSession().update(persistentInstance);
    }

    public void delete(TransactionLedger persistentInstance) {
        getSession().delete(persistentInstance);
        getSession().flush();
    }

    public List<Object> getBlAutoCompleteResults(String blNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select bl.bolid as bl_number,bl.voyages as voyage_number,concat('04',bl.file_no) as dock_receipt");
        queryBuilder.append(" from fcl_bl bl");
        queryBuilder.append(" where bl.bolid like '%").append(blNumber).append("%'");
        queryBuilder.append(" and bolid not like '%-A' and bolid not like '%-B' and bolid not like '%-C' and bolid not like '%-D'");
        queryBuilder.append(" and bolid not like '%-E' and bolid not like '%-F' and bolid not like '%-G' and bolid not like '%-H'");
        queryBuilder.append(" and bolid not like '%-I' and bolid not like '%-J' and bolid not like '%-K' and bolid not like '%-L'");
        queryBuilder.append(" and bolid not like '%-M' and bolid not like '%-N' and bolid not like '%-O' and bolid not like '%-P'");
        queryBuilder.append(" and bolid not like '%-Q' and bolid not like '%-R' and bolid not like '%-S' and bolid not like '%-T'");
        queryBuilder.append(" and bolid not like '%-U' and bolid not like '%-V' and bolid not like '%-W' and bolid not like '%-X'");
        queryBuilder.append(" and bolid not like '%-Y' and bolid not like '%-Z' and bolid not like '%==%'");
        queryBuilder.append(" limit 10");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
    }

    public String deriveGlAccount(String account, String suffix, String shipmentType, String terminal) throws Exception {
        StringBuilder glAccount = new StringBuilder();
        String companyCode = new SystemRulesDAO().getSystemRulesByCode(SYSTEM_RULE_CODE_COMPANY_CODE);
        glAccount.append(companyCode).append("-");
        glAccount.append(account).append("-");
        String orginalSuffix = suffix;
          if (CommonUtils.in(suffix, "B", "L", "D")) { //LCLE-B,L,D	    FCLE-B,L,D	   AIRE-B,L,D	   LCLI-B,L	   FCLI-B,L     	AIRI-B,L	    INLE-L
            String columnName = //For Billing terminals
                    CommonUtils.isEqual(suffix, "B")
                            ? (CommonUtils.isEqual(shipmentType, "FCLE") ? "fcl_export_billing"
                                    : CommonUtils.isEqual(shipmentType, "FCLI") ? "fcl_import_billing"
                                            : CommonUtils.isEqual(shipmentType, "LCLE") ? "lcl_export_billing"
                                                    : CommonUtils.isEqual(shipmentType, "LCLI") ? "lcl_import_billing"
                                                     : CommonUtils.isEqual(shipmentType, "AIRI") ? "air_import_billing"
                                                            : "air_export_billing")

                                            //  B-FCLE,FCLI,LCLE,LCLI,AIRE,AIRI

                                           //   L- FCLE,FCLI,LCLE,LCLI,AIRE,AIRI,INLE

                                           //   D- FCLE,LCLE,AIRE

                            : //For Loading terminals
                            CommonUtils.isEqual(suffix, "L")
                                    ? (CommonUtils.isEqual(shipmentType, "FCLE") ? "fcl_export_loading"
                                         : CommonUtils.isEqual(shipmentType, "FCLI") ? "fcl_import_loading"
                                            : CommonUtils.isEqual(shipmentType, "LCLE") ? "lcl_export_loading"
                                                    : CommonUtils.isEqual(shipmentType, "LCLI") ? "lcl_import_loading"
                                                            : CommonUtils.isEqual(shipmentType, "AIRE") ? "air_export_loading"
                                                               : CommonUtils.isEqual(shipmentType, "AIRI") ? "air_import_loading"
                                                                    : "inland_export_loading")
                                    : //For Dock Receipt terminals
                                    (CommonUtils.isEqual(shipmentType, "FCLE") ? "fcl_export_dockreceipt"
                                            : CommonUtils.isEqual(shipmentType, "LCLE") ? "lcl_export_dockreceipt"
                                                    : "air_export_dockreceipt");
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select lpad(if(").append(columnName).append("!='',").append(columnName).append(",terminal),2,'00') as suffix");
            queryBuilder.append(" from terminal_gl_mapping");
            queryBuilder.append(" where terminal = '").append(terminal).append("'");
            suffix = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
        }
        StringBuilder error = new StringBuilder();
        if (CommonUtils.isEmpty(suffix)) {
            error.append(" Shipment Type - ").append(shipmentType);
            error.append(CommonUtils.in(orginalSuffix, "B", "L", "D") ? "," : " and").append(" Suffix - ").append(orginalSuffix);
            if (CommonUtils.in(orginalSuffix, "B", "L", "D")) {
                error.append(" and Terminal - ").append(terminal);
            }
            error.append(",<font color='red'> No mapping found</font>");
            return error.toString();
        } else {
            glAccount.append(suffix);
            error.append(" Shipment Type - ").append(shipmentType);
            error.append(CommonUtils.in(orginalSuffix, "B", "L", "D") ? "," : " and").append(" Suffix - ").append(orginalSuffix);
            if (CommonUtils.in(orginalSuffix, "B", "L", "D")) {
                error.append(" and Terminal - ").append(terminal);
            }
            error.append(",<font color='red'> GL Account ").append(glAccount.toString());
            error.append(" not found</font>");
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select count(*) as count");
            queryBuilder.append(" from account_details");
            queryBuilder.append(" where account='").append(glAccount.toString()).append("'");
            Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
            return (null != result && Integer.parseInt(result.toString()) > 0) ? glAccount.toString() : error.toString();
        }
    }

    public void updateAccrual(EdiInvoiceForm ediInvoiceForm, User user) throws Exception {
        String moduleId = NotesConstants.ACCRUALS;
        String moduleRefId = ediInvoiceForm.getId().toString();
        TransactionLedger accrual = findById(ediInvoiceForm.getId());
        String desc = "For Cost - " + accrual.getChargeCode() + ",";
        AuditNotesUtils.insertAuditNotes(desc, accrual.getChargeCode(), ediInvoiceForm.getCostCode(), moduleId, moduleRefId, "Cost Code", user);
        accrual.setChargeCode(ediInvoiceForm.getCostCode());
        desc = "For Cost - " + accrual.getChargeCode() + ",";
        AuditNotesUtils.insertAuditNotes(desc, accrual.getShipmentType(), ediInvoiceForm.getShipmentType(), moduleId, moduleRefId, "Shipment Type", user);
        accrual.setShipmentType(ediInvoiceForm.getShipmentType());
        AuditNotesUtils.insertAuditNotes(desc, accrual.getGlAccountNumber(), ediInvoiceForm.getGlAccount(), moduleId, moduleRefId, "GL Account", user);
        accrual.setGlAccountNumber(ediInvoiceForm.getGlAccount());
        AuditNotesUtils.insertAuditNotes(desc, accrual.getBlueScreenChargeCode(), ediInvoiceForm.getBluescreenCostCode(), moduleId, moduleRefId, "Blue Screen Cost Code", user);
        accrual.setBlueScreenChargeCode(ediInvoiceForm.getBluescreenCostCode());
        AuditNotesUtils.insertAuditNotes(desc, accrual.getBillLaddingNo(), ediInvoiceForm.getBlNumber(), moduleId, moduleRefId, "Bill of Lading", user);
        accrual.setBillLaddingNo(ediInvoiceForm.getBlNumber());
        AuditNotesUtils.insertAuditNotes(desc, accrual.getContainerNo(), ediInvoiceForm.getContainerNumber(), moduleId, moduleRefId, "Container", user);
        accrual.setContainerNo(ediInvoiceForm.getContainerNumber());
        AuditNotesUtils.insertAuditNotes(desc, accrual.getVoyageNo(), ediInvoiceForm.getVoyageNumber(), moduleId, moduleRefId, "Voyage", user);
        accrual.setVoyageNo(ediInvoiceForm.getVoyageNumber());
        AuditNotesUtils.insertAuditNotes(desc, accrual.getDocReceipt(), ediInvoiceForm.getDockReceipt(), moduleId, moduleRefId, "Dock Receipt", user);
        accrual.setDocReceipt(ediInvoiceForm.getDockReceipt());
        double newAmount = Double.parseDouble(ediInvoiceForm.getAmount().replace(",", ""));
        String fmtOldAmount = NumberUtils.formatNumber(accrual.getTransactionAmt());
        String fmtNewAmount = NumberUtils.formatNumber(newAmount);
        AuditNotesUtils.insertAuditNotes(desc, fmtOldAmount, fmtNewAmount, NotesConstants.ACCRUALS, moduleId, "Amount", user);
        accrual.setTransactionAmt(newAmount);
        accrual.setTerminal(ediInvoiceForm.getTerminal());
        update(accrual);
    }

    public List<TransactionLedger> getAccruals(String vendorNumber, String invoiceNumber, String status) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.add(Restrictions.eq("status", status));
        return criteria.list();
    }

    public List<TransactionLedger> getEdiAccrualsToAssign(String vendorNumber, String invoiceNumber) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        Disjunction status = Restrictions.disjunction();
        status.add(Restrictions.eq("status", STATUS_OPEN));
        status.add(Restrictions.eq("status", STATUS_EDI_IN_PROGRESS));
        status.add(Restrictions.eq("status", STATUS_EDI_DISPUTE));
        criteria.add(status);
        criteria.add(Restrictions.ne("glAccountNumber", ""));
        return criteria.list();
    }

    public String getVendorName(String vendorNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select acct_name as vendorName");
        queryBuilder.append(" from trading_partner");
        queryBuilder.append(" where acct_no='").append(vendorNumber).append("'");
        return (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
    }

    private boolean isOpenPeriod(String date) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select status");
        queryBuilder.append(" from fiscal_period");
        queryBuilder.append(" where start_date<='").append(date).append(" 00:00:00'");
        queryBuilder.append(" and end_date>='").append(date).append(" 23:59:59'");
        String status = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).setMaxResults(1).uniqueResult();
        return null != status && STATUS_OPEN.equalsIgnoreCase(status);
    }

    public Date getPostedDate(Date invoiceDate) throws Exception {
        String date = DateUtils.formatDate(invoiceDate, "yyyy-MM-dd");
        if (isOpenPeriod(date)) {
            return invoiceDate;
        } else {
            String periodDis = date.replaceAll("[^0-9]", "").substring(0, 6);
            StringBuilder query = new StringBuilder();
            query.append("select date_format(start_date,'%Y-%m-%d') as posted_date");
            query.append(" from fiscal_period");
            query.append(" where status='").append(STATUS_OPEN).append("'");
            query.append(" and period_dis rlike '[0-9]+$'");
            query.append(" and period_dis>'").append(periodDis).append("'");
            query.append(" order by year,period limit 1");
            String result = (String) getCurrentSession().createSQLQuery(query.toString()).uniqueResult();
            return DateUtils.parseDate(result, "yyyy-MM-dd");
        }
    }

    public void postToAp(EdiInvoice ediInvoice, User user) throws Exception {
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        List<TransactionLedger> accruals = getEdiAccrualsToAssign(ediInvoice.getVendorNumber(), ediInvoice.getInvoiceNumber());
        Date invoiceDate = ediInvoice.getInvoiceDate();
        Date postedDate = getPostedDate(invoiceDate);
        Set<String> customerReference = new HashSet<String>();
        User defaultUser = new UserDAO().getUserInfo("KLENGELER");
        User systemUser = new UserDAO().getUserInfo("SYSTEM");
        for (TransactionLedger accrual : accruals) {
            TransactionLedger pjSubledger = new TransactionLedger();
            PropertyUtils.copyProperties(pjSubledger, accrual);
            pjSubledger.setTransactionId(null);
            pjSubledger.setApCostKey(null);
            pjSubledger.setCustName(accrual.getCustName());
            pjSubledger.setCustNo(accrual.getCustNo());
            pjSubledger.setInvoiceNumber(accrual.getInvoiceNumber());
            pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
            pjSubledger.setStatus(STATUS_OPEN);
            pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
            pjSubledger.setTransactionDate(invoiceDate);
            pjSubledger.setPostedDate(postedDate);
            pjSubledger.setCreatedOn(new Date());
            pjSubledger.setCreatedBy(null != user ? user.getUserId() : defaultUser.getUserId());
            save(pjSubledger);
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("b.invoice_number", accrual.getInvoiceNumber());
                        columns.put("t.trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                        columns.put("b.modified_by_user_id", null != user ? user.getUserId() : systemUser.getUserId());
                        updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                        columns.put("lssac.modified_by_user_id", null != user ? user.getUserId() : systemUser.getUserId());
                        updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCost) {
                        fclBlCost.setInvoiceNumber(accrual.getInvoiceNumber());
                        fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                    }
                }
            }
            accrual.setBalance(0d);
            accrual.setBalanceInProcess(0d);
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(null != user ? user.getUserId() : defaultUser.getUserId());
            accrual.setStatus(STATUS_EDI_ASSIGNED);
            accrual.setTransactionDate(ediInvoice.getInvoiceDate());
            accrual.setPostedDate(postedDate);
            StringBuilder desc = new StringBuilder("Accrual of ");
            desc.append("Cost Code - '").append(accrual.getChargeCode().trim()).append("'");
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                desc.append(" and B/L - '").append(accrual.getBillLaddingNo().trim()).append("'");
            }
            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                desc.append(" and Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
            }
            if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                desc.append(" and Voyage - '").append(accrual.getVoyageNo()).append("'");
            }
            desc.append(" of '").append(accrual.getCustName()).append(" (").append(accrual.getCustNo()).append(")'");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
            desc.append(" is assigned to Invoice - '").append(accrual.getInvoiceNumber()).append("'");
            desc.append(" by ").append(null != user ? user.getLoginName() : "System");
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.ACCRUALS, accrual.getTransactionId().toString(), NotesConstants.ACCRUALS, user);
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                customerReference.add(accrual.getBillLaddingNo());
            }
            update(accrual);
        }
        String vendorName = getVendorName(ediInvoice.getVendorNumber());
        //create one invoice entry
        Transaction transaction = new Transaction();
        transaction.setCustName(vendorName);
        transaction.setCustNo(ediInvoice.getVendorNumber());
        transaction.setInvoiceNumber(ediInvoice.getInvoiceNumber());
        transaction.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        transaction.setStatus(STATUS_OPEN);
        transaction.setTransactionDate(invoiceDate);
        transaction.setPostedDate(postedDate);
        transaction.setTransactionAmt(ediInvoice.getInvoiceAmount());
        transaction.setBalance(ediInvoice.getInvoiceAmount());
        transaction.setBalanceInProcess(ediInvoice.getInvoiceAmount());
        transaction.setCustomerReferenceNo(StringUtils.left(customerReference.toString().replace("[", "").replace("]", ""), 500));
        transaction.setDueDate(postedDate);
        transaction.setBillTo(YES);
        transaction.setArBatchId(null);
        transaction.setApBatchId(null);
        transaction.setCreatedOn(new Date());
        transaction.setCreatedBy(null != user ? user.getUserId() : defaultUser.getUserId());
        new AccountingTransactionDAO().save(transaction);
        //History for invoice entry
        ApTransactionHistory apTransactionHistory = new ApTransactionHistory(transaction);
        apTransactionHistory.setCreatedBy(null != user ? user.getLoginName() : "System");
        new ApTransactionHistoryDAO().save(apTransactionHistory);
        String glPeriod = DateUtils.formatDate(postedDate, "yyyyyMM");
        String key = ediInvoice.getVendorNumber() + "-" + ediInvoice.getInvoiceNumber();
        StringBuilder desc = new StringBuilder();
        desc.append("EDI Invoice '").append(ediInvoice.getInvoiceNumber()).append("'");
        desc.append(" of '").append(ediInvoice.getVendorNumber()).append("'");
        desc.append(" posted to AP on GL Period '").append(glPeriod).append("'");
        desc.append(" by ").append(null != user ? user.getLoginName() : "System");
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), NotesConstants.AP_INVOICE, key, NotesConstants.AP_INVOICE, user);
    }

    public List<TransactionLedger> getOpenAccruals(String vendorNumber, String invoiceNumber) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.eq("status", STATUS_OPEN));
        return criteria.list();
    }

    public List<TransactionLedger> getOpenAccruals(String vendorNumber, List<String> containerNumbers) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.in("containerNo", containerNumbers));
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.eq("status", STATUS_OPEN));
        return criteria.list();
    }

    public List<TransactionLedger> getEdiInvoiceAccruals(String vendorNumber, String invoiceNumber) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.in("status", new String[]{STATUS_OPEN, STATUS_EDI_IN_PROGRESS, STATUS_EDI_DISPUTE}));
        return criteria.list();
    }

    public List<TransactionLedger> getEdiInvoiceAccruals(String vendorNumber, List<String> containerNumbers) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.in("containerNo", containerNumbers));
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.in("status", new String[]{STATUS_OPEN, STATUS_EDI_IN_PROGRESS, STATUS_EDI_DISPUTE}));
        return criteria.list();
    }

    public List<TransactionLedger> getAssignedAccruals(String vendorNumber, String invoiceNumber) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.or(Restrictions.eq("status", STATUS_ASSIGN), Restrictions.eq("status", STATUS_EDI_ASSIGNED)));
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        return criteria.list();
    }

    public List<TransactionLedger> getApInvoiceAccruals(String vendorNumber, String invoiceNumber) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        criteria.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
        criteria.add(Restrictions.eq("directGlAccount", true));
        criteria.add(Restrictions.or(Restrictions.isNull("chargeCode"), Restrictions.ne("chargeCode", "")));
        return criteria.list();
    }

    public void postToAp(ApInvoice apInvoice, User user) throws Exception {
        String vendorNumber = apInvoice.getAccountNumber();
        String vendorName = apInvoice.getCustomerName();
        String invoiceNumber = apInvoice.getInvoiceNumber().trim();
        Date invoiceDate = apInvoice.getDate();
        Date postedDate = getPostedDate(invoiceDate);

        for (ApInvoiceLineItem lineItem : apInvoice.getLineItems()) {
            double amount = lineItem.getAmount();
            TransactionLedger accrual = new TransactionLedger();
            accrual.setCustName(vendorName);
            accrual.setCustNo(vendorNumber);
            accrual.setInvoiceNumber(invoiceNumber);
            accrual.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
            accrual.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
            accrual.setStatus(STATUS_IN_PROGRESS);
            accrual.setSailingDate(apInvoice.getDate());
            accrual.setTransactionDate(new Date());
            accrual.setDueDate(apInvoice.getDueDate());
            accrual.setGlAccountNumber(lineItem.getGlAccount());
            accrual.setDescription(lineItem.getDescription());
            accrual.setTransactionAmt(amount);
            accrual.setBalance(amount);
            accrual.setBalanceInProcess(amount);
            accrual.setDirectGlAccount(true);
            accrual.setCreatedOn(new Date());
            accrual.setCreatedBy(user.getUserId());
            getCurrentSession().save(accrual);
            getCurrentSession().flush();

            StringBuilder desc = new StringBuilder();
            desc.append("Accrual of");
            desc.append(" GL account - ").append(lineItem.getGlAccount());
            desc.append(" and amount ").append(NumberUtils.formatNumber(amount));
            desc.append(" created for Invoice - ").append(invoiceNumber);
            desc.append(" of ").append(vendorName);
            desc.append(" (").append(vendorNumber).append(")");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            String moduleId = NotesConstants.ACCRUALS;
            String moduleRefId = accrual.getTransactionId().toString();
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
            //Create purchase journal
            TransactionLedger pjSubledger = new TransactionLedger();
            PropertyUtils.copyProperties(pjSubledger, accrual);
            pjSubledger.setTransactionId(null);
            pjSubledger.setApCostKey(null);
            pjSubledger.setCustName(vendorName);
            pjSubledger.setCustNo(vendorNumber);
            pjSubledger.setInvoiceNumber(invoiceNumber);
            pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
            pjSubledger.setStatus(STATUS_OPEN);
            pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
            pjSubledger.setTransactionDate(invoiceDate);
            pjSubledger.setPostedDate(postedDate);
            pjSubledger.setCreatedOn(new Date());
            pjSubledger.setCreatedBy(user.getUserId());
            getCurrentSession().save(pjSubledger);

            //Update accrual
            accrual.setBalance(0d);
            accrual.setBalanceInProcess(0d);
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(user.getUserId());
            accrual.setStatus(STATUS_ASSIGN);
            accrual.setTransactionDate(apInvoice.getDate());
            accrual.setPostedDate(postedDate);
            getCurrentSession().update(accrual);

            //Create the audit note for an accrual
            desc = new StringBuilder();
            desc.append("Accrual of");
            desc.append(" GL account - ").append(accrual.getGlAccountNumber());
            desc.append(" and amount ").append(NumberUtils.formatNumber(accrual.getTransactionAmt()));
            desc.append(" is assigned to Invoice - ").append(accrual.getInvoiceNumber());
            desc.append(" of ").append(accrual.getCustName());
            desc.append(" (").append(accrual.getCustNo()).append(")");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }

        //Create an invoice entry
        Transaction transaction = new Transaction();
        transaction.setCustName(vendorName);
        transaction.setCustNo(vendorNumber);
        transaction.setInvoiceNumber(invoiceNumber);
        transaction.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        transaction.setStatus(STATUS_READY_TO_PAY);
        transaction.setTransactionDate(invoiceDate);
        transaction.setPostedDate(postedDate);
        transaction.setTransactionAmt(apInvoice.getInvoiceAmount());
        transaction.setBalance(apInvoice.getInvoiceAmount());
        transaction.setBalanceInProcess(apInvoice.getInvoiceAmount());
        transaction.setDueDate(postedDate);
        transaction.setBillTo(YES);
        transaction.setArBatchId(null);
        transaction.setApBatchId(null);
        transaction.setCreatedOn(new Date());
        transaction.setCreatedBy(user.getUserId());
        transaction.setOwner(user.getUserId());
        new AccountingTransactionDAO().save(transaction);

        //Create History for an invoice entry
        ApTransactionHistory apTransactionHistory = new ApTransactionHistory(transaction);
        apTransactionHistory.setCreatedBy(user.getLoginName());
        new ApTransactionHistoryDAO().save(apTransactionHistory);

        //Create the audit note for an invoice entry
        String glPeriod = DateUtils.formatDate(postedDate, "yyyyyMM");
        StringBuilder desc = new StringBuilder();
        desc.append("Invoice - ").append(invoiceNumber);
        desc.append(" of ").append(vendorName);
        desc.append(" (").append(vendorNumber).append(")");
        desc.append(" posted to AP on GL Period - ").append(glPeriod);
        desc.append(" by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = vendorNumber + "-" + invoiceNumber;
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
    }

    public String getIncrementalApcostkey(String apcostkey) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select concat(replace(apcostkey,right(apcostkey,3),''),");
        queryBuilder.append("lpad(if(cast(right(apcostkey,3) as unsigned) between 500 and 999,");
        queryBuilder.append("cast(right(apcostkey,3) as unsigned)-1,999),3,'000')) as new_apcostkey");
        queryBuilder.append(" from transaction_ledger");
        queryBuilder.append(" where transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and apcostkey like '").append(apcostkey.substring(0, 39)).append("%'");
        queryBuilder.append("order by new_apcostkey asc limit 1");
        apcostkey = (String) getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return apcostkey;
    }

    public String getDocReceiptsForVoyage(String voyageNumber) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select distinct(concat('\'',drcpt,'\'')) from transaction_ledger");
        queryBuilder.append(" where transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and drcpt != ''");
        queryBuilder.append(" and voyage_no like '%").append(voyageNumber).append("%'");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : null;
    }

    private String buildAcQuery(AccrualsForm accrualsForm) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("(");
        queryBuilder.append("select tp.acct_name as vendorName,");
        queryBuilder.append("tp.acct_no as vendorNumber,");
        queryBuilder.append("ac.invoice_number as invoiceNumber,");
        queryBuilder.append("ac.bill_ladding_no as blNumber,");
        queryBuilder.append("ac.transaction_amt as accruedAmount,");
        queryBuilder.append("ac.bluescreen_chargecode as bluescreenCostCode,");
        queryBuilder.append("ac.charge_code as costCode,");
        queryBuilder.append("ac.gl_account_number as glAccount,");
        queryBuilder.append("ac.shipment_type as shipmentType,");
        queryBuilder.append("ac.container_no as container,");
        queryBuilder.append("ac.voyage_no as voyage,");
        queryBuilder.append("ac.drcpt as dockReceipt,");
        queryBuilder.append("ac.sailing_date as reportingDate,");
        queryBuilder.append("ac.transaction_type as transactionType,");
        queryBuilder.append("ac.status as status,");
        queryBuilder.append("ac.description as notes,");
        queryBuilder.append("cast(ac.cost_id as char character set latin1) as costId,");
        queryBuilder.append("cast(ac.transaction_id as char character set latin1) as id,");
        queryBuilder.append("ac.terminal as terminal");
        queryBuilder.append(" from transaction_ledger ac");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ac.cust_no = tp.acct_no)");
        queryBuilder.append(" where ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        if (CommonUtils.in(accrualsForm.getAction(), "createPdf", "createExcel")) {
            queryBuilder.append(" and ac.transaction_id in (").append(accrualsForm.getAccrualIds()).append(")");
        } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "addAccrual")
                || CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "updateAccrual")) {
            queryBuilder.append(" and ac.transaction_id in (").append(accrualsForm.getAccrualIds()).append(")");
        } else {
            if (CommonUtils.isNotEmpty(accrualsForm.getAccrualIds())) {
                queryBuilder.append(" and ac.transaction_id not in (").append(accrualsForm.getAccrualIds()).append(")");
            }
            queryBuilder.append(" and ac.transaction_amt <> 0.00");
            if (accrualsForm.isOpenAccruals()) {
                queryBuilder.append(" and ac.balance <> 0.00");
                queryBuilder.append(" and ac.balance_in_process <> 0.00");
            }
            queryBuilder.append(" and ac.status != '").append(STATUS_PENDING).append("'");
            if (CommonUtils.isNotEmpty(accrualsForm.getVendorNumber())
                    && CommonUtils.isNotEmpty(accrualsForm.getInvoiceNumber())
                    && CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByInvoice")) {
                queryBuilder.append(" and ac.cust_no = '").append(accrualsForm.getVendorNumber()).append("'");
                queryBuilder.append(" and ac.invoice_number = '").append(accrualsForm.getInvoiceNumber().trim()).append("'");
                queryBuilder.append(" and (ac.status = '").append(STATUS_IN_PROGRESS).append("'");
                if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "EDI")) {
                    queryBuilder.append(" or ac.status = '").append(STATUS_EDI_IN_PROGRESS).append("'");
                    queryBuilder.append(" or ac.status = '").append(STATUS_EDI_DISPUTE).append("'");
                }
                queryBuilder.append(" or ac.status = '").append(STATUS_DISPUTE).append("')");
            } else {
                if (CommonUtils.isEqual(accrualsForm.getSearchBy(), "voyage_no")) {
                    queryBuilder.append(" and (ac.voyage_no like '%").append(accrualsForm.getSearchValue().trim()).append("%'");
                    if (CommonUtils.isNotEmpty(accrualsForm.getSearchValue())) {
                        String dockReceipts = getDocReceiptsForVoyage(accrualsForm.getSearchValue().trim());
                        queryBuilder.append(" or ac.drcpt in (").append(dockReceipts).append(")");
                    }
                    queryBuilder.append(")");
                } else if (CommonUtils.isNotEmpty(accrualsForm.getSearchBy())) {
                    queryBuilder.append(" and ac.").append(accrualsForm.getSearchBy());
                    if (CommonUtils.isEqualIgnoreCase(accrualsForm.getSearchBy(), "drcpt")) {
                        if (accrualsForm.getSearchValue().trim().length() == 6 || accrualsForm.getSearchValue().trim().length() < 3) {
                            queryBuilder.append(" = '").append(accrualsForm.getSearchValue().trim()).append("'");
                        } else {
                            queryBuilder.append(" like '").append(accrualsForm.getSearchValue().trim()).append("%'");
                        } 
                    } else {
                        if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchBySSMasters")) {
                            queryBuilder.append(" = '04").append(accrualsForm.getSearchValue().trim()).append("'");
                        } else {
                            queryBuilder.append(" like '%").append(accrualsForm.getSearchValue().trim()).append("%'");
                        }
                    }
                }else if (CommonUtils.isNotEmpty(accrualsForm.getVendorNumber())
                        && CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByVendor")) {
                    queryBuilder.append(" and ac.cust_no = '").append(accrualsForm.getVendorNumber()).append("'");
                } else if (CommonUtils.isNotEmpty(accrualsForm.getSearchVendorNumber())
                        && CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByFilter")) {
                    queryBuilder.append(" and ac.cust_no = '").append(accrualsForm.getSearchVendorNumber()).append("'");
                }
                if (accrualsForm.isOpenAccruals()) {
                    queryBuilder.append(" and ac.status = '").append(STATUS_OPEN).append("'");
                } else {
                    queryBuilder.append(" and ac.status != '").append(STATUS_OPEN).append("'");
                    if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByVendor")
                            || CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByFilter")) {
                        queryBuilder.append(" and ac.status != '").append(STATUS_IN_PROGRESS).append("'");
                        queryBuilder.append(" and ac.status != '").append(STATUS_DISPUTE).append("'");
                        if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "EDI")) {
                            queryBuilder.append(" and ac.status != '").append(STATUS_EDI_IN_PROGRESS).append("'");
                            queryBuilder.append(" and ac.status != '").append(STATUS_EDI_DISPUTE).append("'");
                        }
                    }
                }
                if (CommonUtils.isNotEmpty(accrualsForm.getHideAccruals())) {
                    queryBuilder.append(" and datediff(sysdate(),ac.sailing_date) < ").append(accrualsForm.getHideAccruals().trim());
                }
            }
        }
        queryBuilder.append(" order by ").append(accrualsForm.getSortBy()).append(" ").append(accrualsForm.getOrderBy());
        queryBuilder.append(")");
        return queryBuilder.toString();
    }

    private String buildArQuery(AccrualsForm accrualsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("(");
        queryBuilder.append("select tp.acct_name as vendorName,");
        queryBuilder.append("tp.acct_no as vendorNumber,");
        queryBuilder.append("ar.invoice_number as invoiceNumber,");
        queryBuilder.append("ar.bill_ladding_no as blNumber,");
        queryBuilder.append("if(ar.ap_invoice_status = '").append(STATUS_IN_PROGRESS).append("',ar.ap_invoice_amount,-balance) as accruedAmount,");
        queryBuilder.append("null as bluescreenCostCode,");
        queryBuilder.append("null as costCode,");
        queryBuilder.append("null as glAccount,");
        queryBuilder.append("null as shipmentType,");
        queryBuilder.append("ar.container_no as container,");
        queryBuilder.append("ar.voyage_no as voyage,");
        queryBuilder.append("ar.drcpt as dockReceipt,");
        queryBuilder.append("ar.transaction_date as reportingDate,");
        queryBuilder.append("ar.transaction_type as transactionType,");
        StringBuilder status = new StringBuilder();
        status.append("('").append(STATUS_IN_PROGRESS).append("',");
        status.append("'").append(STATUS_EDI_IN_PROGRESS).append("',");
        status.append("'").append(STATUS_DISPUTE).append("',");
        status.append("'").append(STATUS_EDI_DISPUTE).append("')");
        queryBuilder.append("if(ar.ap_invoice_status in ").append(status).append(",ar.ap_invoice_status,'").append(STATUS_OPEN).append("') as status,");
        queryBuilder.append("null as notes,");
        queryBuilder.append("null as costId,");
        queryBuilder.append("cast(ar.transaction_id as char character set latin1) as id,");
        queryBuilder.append("'' as terminal");
        queryBuilder.append(" from transaction ar");
        queryBuilder.append(" join trading_partner tp");
        queryBuilder.append(" on (ar.cust_no = tp.acct_no)");
        queryBuilder.append(" where ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and ar.balance <> 0.00");
        if (CommonUtils.in(accrualsForm.getAction(), "createPdf", "createExcel")) {
            queryBuilder.append(" and ar.transaction_id in (").append(accrualsForm.getArIds()).append(")");
        } else {
            if (CommonUtils.isNotEmpty(accrualsForm.getArIds())) {
                queryBuilder.append(" and ar.transaction_id not in (").append(accrualsForm.getArIds()).append(")");
            }
            if (CommonUtils.isNotEmpty(accrualsForm.getVendorNumber())
                    && CommonUtils.isNotEmpty(accrualsForm.getInvoiceNumber())
                    && CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByInvoice")) {
                if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "EDI")) {
                    EdiInvoice ediInvoice = new EdiInvoiceDAO().getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), null);
                    if (null != ediInvoice) {
                        queryBuilder.append(" and ar.ap_invoice_id = ").append(ediInvoice.getId());
                        queryBuilder.append(" and ar.ap_invoice_status in ").append(status.toString());
                    } else {
                        return null;
                    }
                } else {
                    ApInvoice apInvoice = new ApInvoiceDAO().getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), false, true);
                    if (null != apInvoice) {
                        queryBuilder.append(" and ar.ap_invoice_id = ").append(apInvoice.getId());
                        queryBuilder.append(" and (ar.ap_invoice_status = '").append(STATUS_IN_PROGRESS).append("'");
                        queryBuilder.append(" or ar.ap_invoice_status = '").append(STATUS_DISPUTE).append("')");
                    } else {
                        return null;
                    }
                }
            } else {
                queryBuilder.append(" and ar.balance_in_process <> 0.00");
                if (CommonUtils.isEqual(accrualsForm.getSearchBy(), "voyage_no")) {
                    queryBuilder.append(" and (ar.voyage_no like '%").append(accrualsForm.getSearchValue().trim()).append("%'");
                    if (CommonUtils.isNotEmpty(accrualsForm.getSearchValue())) {
                        String dockReceipts = getDocReceiptsForVoyage(accrualsForm.getSearchValue().trim());
                        queryBuilder.append(" or ar.drcpt in (").append(dockReceipts).append(")");
                    }
                    queryBuilder.append(")");
                } else if (CommonUtils.isNotEmpty(accrualsForm.getSearchBy())) {
                    queryBuilder.append(" and ar.").append(accrualsForm.getSearchBy());
                    queryBuilder.append(" like '%").append(accrualsForm.getSearchValue().trim()).append("%'");
                } else if (CommonUtils.isNotEmpty(accrualsForm.getVendorNumber())
                        && CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByVendor")) {
                    queryBuilder.append(" and ar.cust_no = '").append(accrualsForm.getVendorNumber()).append("'");
                } else if (CommonUtils.isNotEmpty(accrualsForm.getSearchVendorNumber())
                        && CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByFilter")) {
                    queryBuilder.append(" and ar.cust_no = '").append(accrualsForm.getSearchVendorNumber()).append("'");
                }
                queryBuilder.append(" and (ar.ap_invoice_status is null");
                queryBuilder.append(" or ar.ap_invoice_status not in ").append(status.toString()).append(")");
            }
        }
        queryBuilder.append(" order by ").append(accrualsForm.getSortBy()).append(" ").append(accrualsForm.getOrderBy());
        queryBuilder.append(")");
        return queryBuilder.toString();
    }

    private Integer getTotalRows(String acQuery, String arQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(id)");
        queryBuilder.append(" from (");
        if (CommonUtils.isNotEmpty(acQuery)) {
            queryBuilder.append(acQuery);
        }
        if (CommonUtils.isNotEmpty(arQuery)) {
            queryBuilder.append(CommonUtils.isNotEmpty(acQuery) ? " union " : "").append(arQuery);
        }
        queryBuilder.append(") as count");
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }
    private Integer getTotalRowsForLcl(String acQuery) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(id)");
        queryBuilder.append(" from ");
        if (CommonUtils.isNotEmpty(acQuery)) {
            queryBuilder.append(acQuery);
        }
        queryBuilder.append(" as count");
        Object count = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != count ? Integer.parseInt(count.toString()) : 0;
    }

    private List<ResultModel> getAccruals(String acQuery, String arQuery, String sortBy, String orderBy, int start, int limit, String vendorNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(ac.vendorName) as vendorName,");
        queryBuilder.append("upper(ac.vendorNumber) as vendorNumber,");
        queryBuilder.append("upper(trim(ac.invoiceNumber)) as invoiceNumber,");
        queryBuilder.append("upper(trim(ac.blNumber)) as blNumber,");
        queryBuilder.append("upper(trim(ac.container)) as container,");
        queryBuilder.append("upper(trim(ac.voyage)) as voyage,");
        queryBuilder.append("upper(trim(ac.dockReceipt)) as dockReceipt,");
        queryBuilder.append("date_format(ac.reportingDate,'%m/%d/%Y') as reportingDate,");
        queryBuilder.append("format(ac.accruedAmount,2) as accruedAmount,");
        queryBuilder.append("ac.bluescreenCostCode as bluescreenCostCode,");
        queryBuilder.append("upper(ac.costCode) as costCode,");
        queryBuilder.append("upper(ac.shipmentType) as shipmentType,");
        queryBuilder.append("ac.glAccount as glAccount,");
        queryBuilder.append("upper(ac.transactionType) as transactionType,");
        queryBuilder.append("upper(if(ac.status='").append(STATUS_IN_PROGRESS).append("','In Progress',");
        queryBuilder.append("if(ac.status='").append(STATUS_ASSIGN).append("','Assigned',ac.status))) as status,");
        queryBuilder.append("upper(trim(ac.notes)) as notes,");
        queryBuilder.append("ac.costId as costId,");
        queryBuilder.append("ac.id as id,");
        if (CommonUtils.isNotEmpty(vendorNo)) {
            queryBuilder.append("if(ac.transactionType = 'AC' and ac.vendorNumber = '").append(vendorNo).append("', `BLGetIsDisputed`(ac.blNumber, '").append(vendorNo).append("'), false) as blDisputed,");
        }
        queryBuilder.append("ac.terminal as terminal");
        queryBuilder.append(" from (");
        if (CommonUtils.isNotEmpty(acQuery)) {
            queryBuilder.append(acQuery);
        }
        if (CommonUtils.isNotEmpty(arQuery)) {
            queryBuilder.append(CommonUtils.isNotEmpty(acQuery) ? " union " : "").append(arQuery);
        }
        queryBuilder.append(") as ac");
        queryBuilder.append(" order by ac.").append(sortBy).append(" ").append(orderBy);
        if (limit != 0) {
            queryBuilder.append(" limit ").append(start).append(",").append(limit);
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("vendorNumber", StringType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("container", StringType.INSTANCE);
        query.addScalar("voyage", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("reportingDate", StringType.INSTANCE);
        query.addScalar("accruedAmount", StringType.INSTANCE);
        query.addScalar("bluescreenCostCode", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("shipmentType", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("transactionType", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("notes", StringType.INSTANCE);
        query.addScalar("costId", StringType.INSTANCE);
        query.addScalar("id", StringType.INSTANCE);
        if (CommonUtils.isNotEmpty(vendorNo)) {
            query.addScalar("blDisputed", BooleanType.INSTANCE);
        }
        query.addScalar("terminal", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }
    private List<ResultModel> getLclSsAccruals(String acQuery,int start, int limit) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select upper(ac.vendorName) as vendorName,");
        queryBuilder.append("upper(ac.vendorNumber) as vendorNumber,");
        queryBuilder.append("upper(trim(ac.invoiceNumber)) as invoiceNumber,");
        queryBuilder.append("upper(trim(ac.blNumber)) as blNumber,");
        queryBuilder.append("upper(trim(ac.container)) as container,");
        queryBuilder.append("upper(trim(ac.voyage)) as voyage,");
        queryBuilder.append("upper(trim(ac.dockReceipt)) as dockReceipt,");
        queryBuilder.append("date_format(ac.reportingDate,'%m/%d/%Y') as reportingDate,");
        queryBuilder.append("format(ac.accruedAmount,2) as accruedAmount,");
        queryBuilder.append("ac.bluescreenCostCode as bluescreenCostCode,");
        queryBuilder.append("upper(ac.costCode) as costCode,");
        queryBuilder.append("upper(ac.shipmentType) as shipmentType,");
        queryBuilder.append("ac.glAccount as glAccount,");
        queryBuilder.append("upper(ac.transactionType) as transactionType,");
        queryBuilder.append("upper(if(ac.status='").append(STATUS_IN_PROGRESS).append("','In Progress',");
        queryBuilder.append("if(ac.status='").append(STATUS_ASSIGN).append("','Assigned',ac.status))) as status,");
        queryBuilder.append("upper(trim(ac.notes)) as notes,");
        queryBuilder.append("ac.costId as costId,");
        queryBuilder.append("ac.id as id,");
        queryBuilder.append("ac.terminal as terminal");
        queryBuilder.append(" from ");
        queryBuilder.append(acQuery);
        queryBuilder.append(" as ac");
        if (limit != 0) {
            queryBuilder.append(" limit ").append(start).append(",").append(limit);
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("vendorNumber", StringType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("container", StringType.INSTANCE);
        query.addScalar("voyage", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("reportingDate", StringType.INSTANCE);
        query.addScalar("accruedAmount", StringType.INSTANCE);
        query.addScalar("bluescreenCostCode", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("shipmentType", StringType.INSTANCE);
        query.addScalar("glAccount", StringType.INSTANCE);
        query.addScalar("transactionType", StringType.INSTANCE);
        query.addScalar("status", StringType.INSTANCE);
        query.addScalar("notes", StringType.INSTANCE);
        query.addScalar("costId", StringType.INSTANCE);
        query.addScalar("id", StringType.INSTANCE);
        query.addScalar("terminal", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }
     public void searchByLclSSMasters(AccrualsForm accrualsForm) throws Exception {
        List<ResultModel> results = new ArrayList<ResultModel>();
        int limit = accrualsForm.getLimit();
        String acQuery = buildAcLclSsQuery(accrualsForm);
        int totalRows = getTotalRowsForLcl(acQuery);
        if (totalRows > 0) {
            int start = limit * ((accrualsForm.getSelectedPage() == 0 ? 1 : accrualsForm.getSelectedPage()) - 1);
            results.addAll(getLclSsAccruals(acQuery, start, limit));
            accrualsForm.setSelectedRows(results.size() + accrualsForm.getRemainingRows());
            int totalPages = ((totalRows - accrualsForm.getRemainingRows()) / limit) + ((totalRows - accrualsForm.getRemainingRows()) % limit > 0 ? 1 : 0);
            accrualsForm.setTotalPages(totalPages);
            accrualsForm.setTotalRows(totalRows + accrualsForm.getRemainingRows());
        }
        accrualsForm.setResults(results);
     }
    public void search(AccrualsForm accrualsForm) throws Exception {
        List<ResultModel> results = new ArrayList<ResultModel>();
        int limit = accrualsForm.getLimit();
        if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "searchByInvoice")) {
            String acQuery = buildAcQuery(accrualsForm);
            String arQuery = buildArQuery(accrualsForm);
            results.addAll(getAccruals(acQuery, arQuery, accrualsForm.getSortBy(), accrualsForm.getOrderBy(), 0, 0, accrualsForm.getVendorNumber()));
            accrualsForm.setSelectedRows(accrualsForm.getRemainingRows() + results.size());
            accrualsForm.setTotalRows(accrualsForm.getTotalRows() - accrualsForm.getRemovedRows() + results.size());
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "EDI")) {
                accrualsForm.setTotalPages(1);
            }
        } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "addAccrual")
                || CommonUtils.isEqualIgnoreCase(accrualsForm.getAction(), "updateAccrual")) {
            String acQuery = buildAcQuery(accrualsForm);
            results.addAll(getAccruals(acQuery, null, accrualsForm.getSortBy(), accrualsForm.getOrderBy(), 0, 0, accrualsForm.getVendorNumber()));
            accrualsForm.setSelectedRows(accrualsForm.getSelectedRows() + results.size());
            accrualsForm.setTotalRows(accrualsForm.getTotalRows() + results.size());
        } else {
            String acQuery = null;
            String arQuery = null;
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAr(), ONLY)) {
                arQuery = buildArQuery(accrualsForm);
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAr(), YES)) {
                arQuery = buildArQuery(accrualsForm);
                acQuery = buildAcQuery(accrualsForm);
            } else {
                acQuery = buildAcQuery(accrualsForm);
            }
            int totalRows = getTotalRows(acQuery, arQuery);
            if (totalRows > 0) {
                int start = limit * ((accrualsForm.getSelectedPage() == 0 ? 1 : accrualsForm.getSelectedPage()) - 1);
                results.addAll(getAccruals(acQuery, arQuery, accrualsForm.getSortBy(), accrualsForm.getOrderBy(), start, limit, accrualsForm.getVendorNumber()));
                accrualsForm.setSelectedRows(results.size() + accrualsForm.getRemainingRows());
                int totalPages = ((totalRows - accrualsForm.getRemainingRows()) / limit) + ((totalRows - accrualsForm.getRemainingRows()) % limit > 0 ? 1 : 0);
                accrualsForm.setTotalPages(totalPages);
                accrualsForm.setTotalRows(totalRows + accrualsForm.getRemainingRows());
            }
        }
        accrualsForm.setResults(results);
    }

    private FclBl getFclBl(String blNumber) {
        Criteria criteria = getCurrentSession().createCriteria(FclBl.class);
        criteria.add(Restrictions.eq("bolId", blNumber));
        criteria.setMaxResults(1);
        return (FclBl) criteria.uniqueResult();
    }

    private String getCostCodeDescription(String costCode) {
        String query = "select codedesc from genericcode_dup where codetypeid=36 and code='" + costCode + "' limit 1";
        return (String) getCurrentSession().createSQLQuery(query).uniqueResult();
    }

    public void createFclBlCost(TransactionLedger accrual, String userName) throws Exception {
        FclBl fclBl = getFclBl(accrual.getBillLaddingNo());
        if (null != fclBl) {
            FclBlCostCodes fclBlCost = new FclBlCostCodes();
            fclBlCost.setAccName(accrual.getCustName());
            fclBlCost.setAccNo(accrual.getCustNo());
            fclBlCost.setAmount(accrual.getTransactionAmt());
            fclBlCost.setCostCode(accrual.getChargeCode());
            String costCodeDescription = getCostCodeDescription(accrual.getChargeCode());
            if (null != costCodeDescription) {
                fclBlCost.setCostCodeDesc(costCodeDescription);
            }
            if ("FAECOMM".equalsIgnoreCase(fclBlCost.getCostCode())) {
                fclBlCost.setBookingFlag("FAE");
            }
            fclBlCost.setInvoiceNumber(accrual.getInvoiceNumber());
            fclBlCost.setCurrencyCode("USD");
            fclBlCost.setBolId(fclBl.getBol());
            String status = accrual.getStatus();
            if (CommonUtils.isEqualIgnoreCase(status, STATUS_INACTIVE)) {
                fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
            } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_IN_PROGRESS)) {
                fclBlCost.setTransactionType(TRANSACTION_TYPE_IN_PROGRESS);
            } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_DISPUTE)) {
                fclBlCost.setTransactionType(TRANSACTION_TYPE_DISPUTE);
            } else if (CommonUtils.isEqualIgnoreCase(status, STATUS_OPEN)) {
                fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
            }
            fclBlCost.setReadyToPost("M");
            fclBlCost.setManifestModifyFlag("yes");
            fclBlCost.setDeleteFlag("no");
            fclBlCost.setAccrualsCreatedDate(new Date());
            fclBlCost.setAccrualsCreatedBy(userName);
            getCurrentSession().save(fclBlCost);
            new NotesBC().saveNotesWhileAddingFclBlCostCodes(fclBlCost, userName);
            accrual.setCostId(fclBlCost.getCodeId());
            StringBuilder containerNumber = new StringBuilder();
            boolean isFirst = true;
            for (FclBlContainer fclBlContainer : (Set<FclBlContainer>) fclBl.getFclcontainer()) {
                containerNumber.append(isFirst ? "" : ",").append(fclBlContainer.getTrailerNo());
                isFirst = false;
            }
            accrual.setContainerNo(containerNumber.toString());
            accrual.setBillLaddingNo(fclBl.getBolId());
            accrual.setThirdptyName(fclBl.getThirdPartyName());
            accrual.setThirdptyNo(fclBl.getBillTrdPrty());
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                accrual.setTransactionDate(fclBl.getEta());
            } else {
                accrual.setTransactionDate(fclBl.getSailDate());
            }
            accrual.setShipName(fclBl.getShipperName());
            accrual.setShipNo(fclBl.getShipperNo());
            accrual.setFwdName(fclBl.getForwardingAgentName());
            accrual.setFwdNo(fclBl.getForwardAgentNo());
            accrual.setBlTerms(fclBl.getHouseBl());
            accrual.setVoyageNo(fclBl.getVoyages());
            accrual.setMasterBl(fclBl.getMasterBl());
            accrual.setReadyToPost("on");
            accrual.setOrgTerminal(fclBl.getOriginalTerminal());
            accrual.setDestination(fclBl.getPortofDischarge());
            accrual.setConsName(fclBl.getConsigneeName());
            accrual.setConsNo(fclBl.getConsigneeNo());
            accrual.setBookingNo(fclBl.getBookingNo());
            accrual.setAgentName(fclBl.getAgent());
            accrual.setAgentNo(fclBl.getAgentNo());
            accrual.setBillingTerminal(fclBl.getBillingTerminal());
            if ("I".equalsIgnoreCase(fclBl.getImportFlag())) {
                accrual.setSailingDate(fclBl.getEta());
            } else {
                accrual.setSailingDate(fclBl.getSailDate());
            }
            accrual.setManifestFlag("Y");
            getCurrentSession().update(accrual);
        }
    }

    public TransactionLedger addUnitCost(TransactionLedger accrual, LclSsAc ssAc) throws Exception {
        Date now = new Date();
        StringBuilder voyageBuilder = new StringBuilder();
        LclManifestDAO lclManifestDAO = new LclManifestDAO();
        accrual.setTransactionAmt(ssAc.getApAmount().doubleValue());
        accrual.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
        accrual.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
        accrual.setStatus(STATUS_OPEN);
        LclSsHeader ssHeader = ssAc.getLclSsHeader();
        if (null != ssHeader) {
            voyageBuilder.append((null != ssHeader.getBillingTerminal() ? ssHeader.getBillingTerminal().getTrmnum() : "")).append("-");
            voyageBuilder.append((null != ssHeader.getOrigin() ? ssHeader.getOrigin().getUnLocationCode() : "")).append("-");
            voyageBuilder.append((null != ssHeader.getDestination() ? ssHeader.getDestination().getUnLocationCode() : "")).append("-");
            voyageBuilder.append(ssHeader.getScheduleNo());
        }
        if (null != ssAc.getLclUnitSs().getLclUnit()) {
            ManifestModel model = lclManifestDAO.getUnitAutoCostAccruals(ssAc.getLclUnitSs().getId(), ssAc.getApGlMappingId().getId());
            accrual.setBillLaddingNo(model.getBlNumber());
            accrual.setBookingNo(model.getBookingNumber());
            accrual.setVoyageNo(voyageBuilder.toString());
            accrual.setContainerNo(ssAc.getLclUnitSs().getLclUnit().getUnitNo());
            accrual.setOrgTerminal(model.getOrigin());
            accrual.setDestination(model.getDestination());
            accrual.setShipName(model.getShipperName());
            accrual.setShipNo(model.getShipperNo());
            accrual.setConsName(model.getConsigneeName());
            accrual.setConsNo(model.getConsigneeNo());
            accrual.setAgentName(model.getAgentName());
            accrual.setAgentNo(model.getAgentNo());
            accrual.setBillingTerminal(model.getTerminal());
            accrual.setVesselNo(model.getVesselNo());
            if (model.getSailDate() != null) {
                model.setReportingDate(model.getSailDate());
            } else {
                model.setReportingDate(now);
            }
            accrual.setTransactionDate(model.getReportingDate());
            accrual.setSailingDate(model.getReportingDate());
            accrual.setPostedDate(model.getReportingDate());
            accrual.setGlAccountNumber(model.getGlAccount());
        }
        accrual.setBlueScreenChargeCode(ssAc.getApGlMappingId().getBlueScreenChargeCode());
        accrual.setBillTo(YES);
        accrual.setReadyToPost(ON);
        accrual.setCurrencyCode(CURRENCY_CODE);
        accrual.setCostId(ssAc.getId().intValue());
        accrual.setManifestFlag(NO);
        return accrual;
    }

    public TransactionLedger getDrcptCost(String docReceiptNo, LclBookingAc bookingAc, String shipmentType) throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.eq("docReceipt", docReceiptNo));
        criteria.add(Restrictions.eq("costId", bookingAc.getId().intValue()));
        criteria.add(Restrictions.eq("shipmentType", shipmentType));
        return (TransactionLedger) criteria.uniqueResult();
    }

    public TransactionLedger createLclCost(AccrualsForm accrualsForm, User user) throws Exception {
        GlMappingDAO glDao = new GlMappingDAO();
        LclUtils utils = new LclUtils();
        BigDecimal apAmount = new BigDecimal(Double.parseDouble(accrualsForm.getNewAmount().replace(",", "")));
        Long fileId = accrualsForm.getFileId();
        GlMapping apGlmapping = glDao.findByChargeCode(accrualsForm.getNewCostCode(), accrualsForm.getNewShipmentType(), TRANSACTION_TYPE_ACCRUALS);
        GlMapping arGlmapping = glDao.findByChargeCode(accrualsForm.getNewCostCode(), accrualsForm.getNewShipmentType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        if (null == arGlmapping) {
            arGlmapping = apGlmapping;
        }
        LclBookingAc bookingAc = utils.saveBookingAc(fileId, apGlmapping, arGlmapping, apAmount, BigDecimal.ZERO, "C", "FL",
                BigDecimal.ZERO, BigDecimal.ZERO, accrualsForm.getNewInvoiceNumber(), accrualsForm.getNewVendorNumber(), user);
        getCurrentSession().getTransaction().commit();
        getCurrentSession().getTransaction().begin();
        TransactionLedger accrual = getDrcptCost(accrualsForm.getNewDockReceipt(), bookingAc, accrualsForm.getNewShipmentType());
        return accrual;
    }

    public TransactionLedger createLclUnitCost(AccrualsForm accrualsForm, TransactionLedger accrual, User user) throws Exception {
        GlMappingDAO glDao = new GlMappingDAO();
        LclUtils utils = new LclUtils();
        BigDecimal apAmount = new BigDecimal(accrual.getTransactionAmt());
        Long unitssId = accrualsForm.getUnitId();
        GlMapping apGlmapping = glDao.findByChargeCode(accrual.getChargeCode(), accrualsForm.getNewShipmentType(), TRANSACTION_TYPE_ACCRUALS);
        GlMapping arGlmapping = glDao.findByChargeCode(accrual.getChargeCode(), accrualsForm.getNewShipmentType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        if (null == arGlmapping) {
            arGlmapping = apGlmapping;
        }
        LclSsAc ssAc = utils.saveSsAc(unitssId, apGlmapping, arGlmapping, apAmount, BigDecimal.ZERO,
                accrual.getInvoiceNumber(), accrualsForm.getNewVendorNumber(), user);
        accrual = addUnitCost(accrual, ssAc);
        return accrual;
    }

    public void addAccrual(AccrualsForm accrualsForm, User user, HttpServletRequest request) throws Exception {
        TransactionLedger accrual;
        String shipmentType = accrualsForm.getNewShipmentType();
        if (shipmentType.contains("LCL") && CommonUtils.isNotEmpty(accrualsForm.getFileId())) {
            accrual = createLclCost(accrualsForm, user);
        } else {
            accrual = new TransactionLedger();
            accrual.setCustName(accrualsForm.getNewVendorName());
            accrual.setCustNo(accrualsForm.getNewVendorNumber());
            accrual.setInvoiceNumber(accrualsForm.getNewInvoiceNumber());
            accrual.setBillLaddingNo(accrualsForm.getNewBlNumber());
            accrual.setVoyageNo(accrualsForm.getNewVoyageNumber());
            accrual.setDocReceipt(accrualsForm.getNewDockReceipt());
            accrual.setTransactionDate(new Date());
            accrual.setSailingDate(new Date());
            accrual.setTransactionAmt(Double.parseDouble(accrualsForm.getNewAmount().replace(",", "")));
            accrual.setBalance(accrual.getTransactionAmt());
            accrual.setBalanceInProcess(accrual.getTransactionAmt());
            accrual.setChargeCode(accrualsForm.getNewCostCode());
            accrual.setBlueScreenChargeCode(accrualsForm.getNewBluescreenCostCode());
            accrual.setGlAccountNumber(accrualsForm.getNewGlAccount());
            accrual.setShipmentType(accrualsForm.getNewShipmentType());
            accrual.setTerminal(accrualsForm.getNewTerminal());
            accrual.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
            accrual.setStatus(STATUS_OPEN);
            accrual.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
            accrual.setCreatedOn(new Date());
            accrual.setCreatedBy(user.getUserId());
            save(accrual);
            getCurrentSession().flush();
            if (shipmentType.contains("FCL") && CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                createFclBlCost(accrual, user.getLoginName());
            } else if (shipmentType.contains("LCL") && CommonUtils.isNotEmpty(accrualsForm.getUnitId())) {
                accrual = createLclUnitCost(accrualsForm, accrual, user);
                update(accrual);
            }
        }
        boolean addAnd = false;
        StringBuilder desc = new StringBuilder("Accrual Created for");
        if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
            desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
            addAnd = true;
        }
        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
            desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
            addAnd = true;
        }
        if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
            desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
            addAnd = true;
        }
        desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
        desc.append(" and amount - '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        desc.append(" by ").append(user.getLoginName());
        String moduleId = NotesConstants.ACCRUALS;
        String moduleRefId = accrual.getTransactionId().toString();
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        accrualsForm.setAccrualIds(accrual.getTransactionId().toString());
        getCurrentSession().flush();
        if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "arBatch")) {
            request.setAttribute("transaction", new ArBatchDAO().getNewlyAddedAcTransaction(accrual.getTransactionId().toString()));
        } else {
            search(accrualsForm);
        }
    }

    public void updateAccrual(AccrualsForm accrualsForm, User user) throws Exception {
        TransactionLedger oldAccrual = findById(accrualsForm.getAccrualId());
        double oldAmount = oldAccrual.getTransactionAmt();
        double newAmount = Double.parseDouble(accrualsForm.getUpdateAmount().replace(",", ""));
        String moduleId = NotesConstants.ACCRUALS;
        String moduleRefId = oldAccrual.getTransactionId().toString();
        String cost = "For Cost - " + oldAccrual.getChargeCode() + ",";
        String oldCostCode = oldAccrual.getChargeCode();
        boolean isCostCodeChanged = CommonUtils.isNotEqualIgnoreCase(oldAccrual.getChargeCode(), accrualsForm.getUpdateCostCode());
        AuditNotesUtils.insertAuditNotes(cost, oldAccrual.getChargeCode(), accrualsForm.getUpdateCostCode(), moduleId, moduleRefId, "Cost Code", user);
        oldAccrual.setChargeCode(accrualsForm.getUpdateCostCode());
        cost = "For Cost - " + oldAccrual.getChargeCode() + ",";
        AuditNotesUtils.insertAuditNotes(cost, oldAccrual.getBlueScreenChargeCode(), accrualsForm.getUpdateBluescreenCostCode(), moduleId, moduleRefId, "Bluescreen Cost Code", user);
        oldAccrual.setBlueScreenChargeCode(accrualsForm.getUpdateBluescreenCostCode());
        AuditNotesUtils.insertAuditNotes(cost, oldAccrual.getGlAccountNumber(), accrualsForm.getUpdateGlAccount(), moduleId, moduleRefId, "GL Account", user);
        oldAccrual.setGlAccountNumber(accrualsForm.getUpdateGlAccount());
        oldAccrual.setTerminal(accrualsForm.getUpdateTerminal());
        AuditNotesUtils.insertAuditNotes(cost, oldAccrual.getShipmentType(), accrualsForm.getUpdateShipmentType(), moduleId, moduleRefId, "Shipment Type", user);
        oldAccrual.setShipmentType(accrualsForm.getUpdateShipmentType());
        if (CommonUtils.isNotEqual(oldAccrual.getInvoiceNumber(), accrualsForm.getUpdateInvoiceNumber())
                && CommonUtils.in(oldAccrual.getStatus(), STATUS_IN_PROGRESS, STATUS_DISPUTE)) {
            StringBuilder desc = new StringBuilder("Accrual of ");
            boolean addAnd = false;
            if (CommonUtils.isNotEmpty(oldAccrual.getBillLaddingNo())) {
                desc.append(" B/L - '").append(oldAccrual.getBillLaddingNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(oldAccrual.getDocReceipt())) {
                desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(oldAccrual.getDocReceipt()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(oldAccrual.getVoyageNo())) {
                desc.append(addAnd ? " and" : "").append(" Voyage - '").append(oldAccrual.getVoyageNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(oldAccrual.getChargeCode())) {
                desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(oldAccrual.getChargeCode()).append("'");
            }
            desc.append(" for amount '").append(NumberUtils.formatNumber(oldAccrual.getTransactionAmt())).append("'");
            desc.append(" is unmarked as ").append(CommonUtils.isEqual(oldAccrual.getStatus(), STATUS_IN_PROGRESS) ? "In Progress" : "Dispute").append(" for Invoice'");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, oldAccrual.getTransactionId().toString(), moduleId, user);
            oldAccrual.setStatus(STATUS_OPEN);
        }
        AuditNotesUtils.insertAuditNotes(cost, oldAccrual.getInvoiceNumber(), accrualsForm.getUpdateInvoiceNumber(), moduleId, moduleRefId, "Invoice Number", user);
        oldAccrual.setInvoiceNumber(accrualsForm.getUpdateInvoiceNumber());
        String amountOld = NumberUtils.formatNumber(oldAmount);
        String amountNew = NumberUtils.formatNumber(newAmount);
        AuditNotesUtils.insertAuditNotes(cost, amountOld, amountNew, moduleId, moduleRefId, "Amount", user);
        oldAccrual.setTransactionAmt(newAmount);
        oldAccrual.setBalance(newAmount);
        oldAccrual.setBalanceInProcess(newAmount);
        oldAccrual.setUpdatedOn(new Date());
        oldAccrual.setUpdatedBy(user.getUserId());
        update(oldAccrual);
        getCurrentSession().flush();
        if (CommonUtils.isNotEmpty(oldAccrual.getCostId())) {
            if (CommonUtils.in(oldAccrual.getShipmentType(), "LCLE", "LCLI")) {
                Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                if (CommonUtils.isNotEmpty(oldAccrual.getDocReceipt())) {
                    columns.put("b.invoice_number", oldAccrual.getInvoiceNumber());
                    columns.put("b.ap_amount", newAmount);
                    columns.put("b.cost_flatrate_amount", newAmount);
                    columns.put("b.modified_by_user_id", user.getUserId());
                    if (isCostCodeChanged) {
                        String notes = "UPDATED -> Cost Code ->" + oldCostCode + " to " + oldAccrual.getChargeCode();
                        new LclRemarksDAO().insertLclRemarks(oldAccrual.getCostId(), oldAccrual.getShipmentType(), "auto", notes, user);
                        columns.put("b.ap_gl_mapping_id", new GlMappingDAO().getGlMappingId(oldAccrual.getChargeCode(), "E", "AC", oldAccrual.getShipmentType()));
                    }
                    updateLclCost(oldAccrual.getCostId(), columns);
                } else {
                    columns.put("lssac.ap_reference_no", oldAccrual.getInvoiceNumber());
                    columns.put("lssac.ap_amount", newAmount);
                    columns.put("lssac.modified_by_user_id", user.getUserId());
                    if (isCostCodeChanged) {
                        columns.put("lssac.ap_gl_mapping_id", new GlMappingDAO().getGlMappingId(oldAccrual.getChargeCode(), "E", "AC", oldAccrual.getShipmentType()));
                    }
                    updateLclUnitCost(oldAccrual.getCostId(), columns);
                }

            } else {
                FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
                FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(oldAccrual.getCostId());
                if (null != fclBlCost) {
                    if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode())) {
                        fclBlCost.setAmount(newAmount - (oldAmount - fclBlCost.getAmount()));
                    } else {
                        fclBlCost.setAmount(newAmount);
                    }
                    fclBlCost.setCostCode(oldAccrual.getChargeCode());
                    String costCodeDescription = getCostCodeDescription(oldAccrual.getChargeCode());
                    if (null != costCodeDescription) {
                        fclBlCost.setCostCodeDesc(costCodeDescription);
                    }
                    fclBlCost.setInvoiceNumber(oldAccrual.getInvoiceNumber());
                    fclBlCost.setManifestModifyFlag("yes");
                    fclBlCost.setProcessedStatus("");
                    fclBlCost.setAccrualsUpdatedBy(user.getLoginName());
                    fclBlCost.setAccrualsUpdatedDate(new Date());
                    fclBlCostCodesDAO.update(fclBlCost);
                }
            }
        } else if (CommonUtils.isNotEmpty(oldAccrual.getBillLaddingNo())) {
            createFclBlCost(oldAccrual, user.getLoginName());
        }
        if (accrualsForm.isLeaveBalance() && newAmount < oldAmount) {
            //create new Accrual
            double difference = oldAmount - newAmount;
            TransactionLedger newAccrual = null;
            if (oldAccrual.getShipmentType().contains("LCL")
                    && CommonUtils.isNotEmpty(oldAccrual.getDocReceipt()) && CommonUtils.isNotEmpty(oldAccrual.getCostId())) {
                newAccrual = createLclCost(oldAccrual, difference, user);
            } else {
                newAccrual = new TransactionLedger();
                PropertyUtils.copyProperties(newAccrual, oldAccrual);
                newAccrual.setTransactionId(null);
                newAccrual.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                newAccrual.setStatus(STATUS_OPEN);
                newAccrual.setTransactionAmt(difference);
                newAccrual.setBalance(difference);
                newAccrual.setBalanceInProcess(difference);
                newAccrual.setTransactionDate(new Date());
                newAccrual.setCreatedOn(new Date());
                newAccrual.setCreatedBy(user.getUserId());
                newAccrual.setPostedDate(null);
                if (CommonUtils.isNotEmpty(newAccrual.getApCostKey()) && newAccrual.getApCostKey().length() >= 40) {
                    newAccrual.setApCostKey(getIncrementalApcostkey(newAccrual.getApCostKey()));
                } else {
                    newAccrual.setApCostKey(null);
                }
                save(newAccrual);
                if (newAccrual.getShipmentType().contains("LCL")
                        && CommonUtils.isEmpty(newAccrual.getBillLaddingNo()) && CommonUtils.isNotEmpty(oldAccrual.getCostId())) {
                    newAccrual = createLclUnitCost(newAccrual, user);
                    update(newAccrual);
                } else if (newAccrual.getShipmentType().contains("FCL")
                        && CommonUtils.isNotEmpty(newAccrual.getBillLaddingNo())) {
                    createFclBlCost(newAccrual, user.getLoginName());
                }
            }
            StringBuilder desc = new StringBuilder("New Accrual Created for remaining amount '");
            desc.append(NumberUtils.formatNumber(difference)).append("'");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
            desc = new StringBuilder("New Accrual Created for");
            desc.append(" Cost - '").append(newAccrual.getChargeCode()).append("'");
            if (CommonUtils.isNotEmpty(newAccrual.getBillLaddingNo())) {
                desc.append(" and B/L - '").append(newAccrual.getBillLaddingNo()).append("'");
            }
            if (CommonUtils.isNotEmpty(newAccrual.getDocReceipt())) {
                desc.append(" and Doc Receipt - '").append(newAccrual.getDocReceipt()).append("'");
            }
            if (CommonUtils.isNotEmpty(newAccrual.getVoyageNo())) {
                desc.append(" and Voyage - '").append(newAccrual.getVoyageNo()).append("'");
            }
            desc.append(" with amount - '").append(NumberUtils.formatNumber(newAccrual.getTransactionAmt())).append("'");
            desc.append(" remaining from amount '").append(NumberUtils.formatNumber(oldAmount)).append("'");
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            desc.append(" by ").append(user.getLoginName());
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, newAccrual.getTransactionId().toString(), moduleId, user);
            accrualsForm.setAccrualIds(newAccrual.getTransactionId().toString());
            getCurrentSession().flush();
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getFrom(), "arBatch")) {
                accrualsForm.setNewAccrualId(newAccrual.getTransactionId().toString());
            } else {
                search(accrualsForm);
            }
        }
    }

    public void unDisputeArs(List<Integer> ids, String vendorNumber, String invoiceNumber, Integer invoiceId, String from, User user) throws Exception {
        String moduleId = NotesConstants.AR_INVOICE;
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("apInvoiceStatus", CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_DISPUTE : STATUS_DISPUTE));
        criteria.add(Restrictions.eq("apInvoiceId", invoiceId));
        if (CommonUtils.isNotEmpty(ids)) {
            criteria.add(Restrictions.not(Restrictions.in("transactionId", ids)));
        }
        List<Transaction> ars = criteria.list();
        for (Transaction ar : ars) {
            String moduleRefId = ar.getCustNo() + "-" + (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
            ar.setApInvoiceId(null);
            ar.setApInvoiceStatus(null);
            ar.setBalanceInProcess(ar.getBalance());
            ar.setUpdatedOn(new Date());
            ar.setUpdatedBy(user.getUserId());
            String apInvoice = vendorNumber + "-" + invoiceNumber;
            StringBuilder desc = new StringBuilder("Unmarked as Dispute for ");
            desc.append(CommonUtils.isEqualIgnoreCase(from, "EDI") ? "EDI" : "AP");
            desc.append(" Invoice - '").append(apInvoice).append("'");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
    }

    private void disputeArs(AccrualsForm accrualsForm, Integer invoiceId, User user) throws Exception {
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        String vendorNumber = accrualsForm.getVendorNumber();
        String invoiceNumber = accrualsForm.getInvoiceNumber();
        String from = accrualsForm.getFrom();
        List<Integer> disputedIds = new ArrayList<Integer>();
        if (CommonUtils.isNotEmpty(accrualsForm.getDisputedArIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDisputedArIds(), ","));
            String moduleId = NotesConstants.AR_INVOICE;
            for (String id : ids) {
                Transaction ar = transactionDAO.findById(Integer.parseInt(id));
                disputedIds.add(ar.getTransactionId());
                if (CommonUtils.isNotEqualIgnoreCase(ar.getApInvoiceStatus(), STATUS_DISPUTE)
                        || (CommonUtils.isEqualIgnoreCase(from, "EDI")
                        && CommonUtils.isNotEqualIgnoreCase(ar.getApInvoiceStatus(), STATUS_EDI_DISPUTE))) {
                    String moduleRefId = ar.getCustNo() + "-" + (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
                    String apInvoice = vendorNumber + "-" + invoiceNumber;
                    StringBuilder desc = new StringBuilder("Marked as Dispute for ");
                    desc.append(CommonUtils.isEqualIgnoreCase(from, "EDI") ? "EDI" : "AP");
                    desc.append(" Invoice - '").append(apInvoice).append("'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);

                }
                ar.setApInvoiceId(invoiceId);
                ar.setApInvoiceStatus(CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_DISPUTE : STATUS_DISPUTE);
                ar.setBalanceInProcess(0d);
                ar.setUpdatedOn(new Date());
                ar.setUpdatedBy(user.getUserId());
            }
        }
        unDisputeArs(disputedIds, vendorNumber, invoiceNumber, invoiceId, from, user);
    }

    public void unDisputeAccruals(List<Integer> ids, String vendorNumber, String invoiceNumber, String from, User user) throws Exception {
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        String moduleId = NotesConstants.ACCRUALS;
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        criteria.add(Restrictions.eq("status", CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_DISPUTE : STATUS_DISPUTE));
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        if (CommonUtils.isNotEmpty(ids)) {
            criteria.add(Restrictions.not(Restrictions.in("transactionId", ids)));
        }
        List<TransactionLedger> accruals = criteria.list();
        for (TransactionLedger accrual : accruals) {
            accrual.setInvoiceNumber(invoiceNumber);
            accrual.setStatus(STATUS_OPEN);
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(user.getUserId());
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("b.invoice_number", accrual.getInvoiceNumber());
                        columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                        columns.put("b.modified_by_user_id", user.getUserId());
                        updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                        columns.put("lssac.modified_by_user_id", user.getUserId());
                        updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                    FclBlUtil fclBlutil = new FclBlUtil();
                    String importflagFor = "";
                    List<FclBlCostCodes> consolidatorList = new ArrayList();
                    if (null != fclBlCost) {
                        fclBlCost.setInvoiceNumber(invoiceNumber);
                        fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                        if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode())) {
                            String bolid = fclBlCostCodesDAO.getBolfromBillOfLadding(accrual.getBillLaddingNo());
                            List<FclBlCostCodes> costList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolid));
                            FclBl fclBl = getFclBl(accrual.getBillLaddingNo());
                            importflagFor = fclBl.getImportFlag();
                            if (importflagFor.equals("I")) {
                                consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, true);
                            } else {
                                consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, false);
                            }
                            List<FclBlCostCodes> newList = new ArrayList<FclBlCostCodes>();

                            for (FclBlCostCodes fclBlCostCodes : costList) {
                                if (!consolidatorList.contains(fclBlCostCodes)) {
                                    newList.add(fclBlCostCodes);
                                }
                            }
                            for (FclBlCostCodes fclBlCostCodes : newList) {
                                 if(CommonUtils.notIn(fclBlCostCodes.getCostCode(),"OCNFRT","OFIMP")){
                                    fclBlCost.setInvoiceNumber(invoiceNumber);
                                    fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                                }
                            }
                        }
                    }
                }
            }
            StringBuilder desc = new StringBuilder("Accrual of ");
            boolean addAnd = false;
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
            }
            desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
            desc.append(" is unmarked as Dispute for");
            desc.append(CommonUtils.isEqualIgnoreCase(from, "EDI") ? " EDI" : "");
            desc.append(" Invoice - '").append(invoiceNumber).append("'");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, accrual.getTransactionId().toString(), moduleId, user);
        }
    }

    private void disputeAccruals(AccrualsForm accrualsForm, User user) throws Exception {
        String vendorName = accrualsForm.getVendorName();
        String vendorNumber = accrualsForm.getVendorNumber();
        String invoiceNumber = accrualsForm.getInvoiceNumber();
        String from = accrualsForm.getFrom();
        List<Integer> disputedIds = new ArrayList<Integer>();
        if (CommonUtils.isNotEmpty(accrualsForm.getDisputedAccrualIds())) {
            FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
            String moduleId = NotesConstants.ACCRUALS;
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDisputedAccrualIds(), ","));
            for (String id : ids) {
                TransactionLedger accrual = findById(id);
                disputedIds.add(accrual.getTransactionId());
                if (CommonUtils.isNotEqualIgnoreCase(vendorNumber, accrual.getCustNo())) {
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    desc.append("'").append(accrual.getCustName()).append(" (").append(accrual.getCustNo()).append(")'");
                    desc.append(" changed to '").append(vendorName).append(" (").append(vendorNumber).append(")'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, accrual.getTransactionId().toString(), moduleId, user);
                    accrual.setCustName(vendorName);
                    accrual.setCustNo(vendorNumber);
                }
                if (CommonUtils.isNotEqualIgnoreCase(accrual.getStatus(), STATUS_DISPUTE)
                        || (CommonUtils.isEqualIgnoreCase(from, "EDI")
                        && CommonUtils.isNotEqualIgnoreCase(accrual.getStatus(), STATUS_EDI_DISPUTE))) {
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    boolean addAnd = false;
                    if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                        desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                        desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                        desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
                    }
                    desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                    desc.append(" is marked as Dispute for ");
                    desc.append(CommonUtils.isEqualIgnoreCase(from, " EDI") ? "EDI" : "");
                    desc.append(" Invoice - '").append(invoiceNumber).append("'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, id, moduleId, user);
                }
                accrual.setInvoiceNumber(invoiceNumber);
                accrual.setStatus(CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_DISPUTE : STATUS_DISPUTE);
                accrual.setUpdatedOn(new Date());
                accrual.setUpdatedBy(user.getUserId());
                if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                    if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                        Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                            columns.put("b.sp_acct_no", accrual.getCustNo());
                            columns.put("b.invoice_number", accrual.getInvoiceNumber());
                            columns.put("t.trans_type", TRANSACTION_TYPE_DISPUTE);
                            columns.put("b.modified_by_user_id", user.getUserId());
                            updateLclCost(accrual.getCostId(), columns);
                        } else {
                            columns.put("lssac.ap_acct_no", accrual.getCustNo());
                            columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                            columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_DISPUTE);
                            columns.put("lssac.modified_by_user_id", user.getUserId());
                            updateLclUnitCost(accrual.getCostId(), columns);
                        }
                    } else {
                        FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                        FclBlUtil fclBlutil = new FclBlUtil();
                        List<FclBlCostCodes> consolidatorList = new ArrayList();
                        String importflagFor = "";
                        if (null != fclBlCost) {
                            fclBlCost.setAccName(accrual.getCustName());
                            fclBlCost.setAccNo(accrual.getCustNo());
                            fclBlCost.setInvoiceNumber(invoiceNumber);
                            fclBlCost.setTransactionType(TRANSACTION_TYPE_DISPUTE);

                            if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode())) {
                                String bolid = fclBlCostCodesDAO.getBolfromBillOfLadding(accrual.getBillLaddingNo());
                                List<FclBlCostCodes> costList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolid));
                                FclBl fclBl = getFclBl(accrual.getBillLaddingNo());
                                importflagFor = fclBl.getImportFlag();
                                if (importflagFor.equals("I")) {
                                    consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, true);
                                } else {
                                    consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, false);
                                }
                                List<FclBlCostCodes> newList = new ArrayList<FclBlCostCodes>();

                                for (FclBlCostCodes fclBlCostCodes : costList) {
                                    if (!consolidatorList.contains(fclBlCostCodes)) {
                                        newList.add(fclBlCostCodes);
                                    }
                                }
                                for (FclBlCostCodes fclBlCostCodes : newList) {
                                     if(CommonUtils.notIn(fclBlCostCodes.getCostCode(),"OCNFRT","OFIMP")){
                                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_DISPUTE);
                                        fclBlCostCodes.setAccName(accrual.getCustName());
                                        fclBlCostCodes.setAccNo(accrual.getCustNo());
                                        fclBlCostCodes.setInvoiceNumber(invoiceNumber);
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        unDisputeAccruals(disputedIds, vendorNumber, invoiceNumber, from, user);
    }

    public void unInprogressArs(List<Integer> ids, String vendorNumber, String invoiceNumber, Integer invoiceId, String from, User user) throws Exception {
        String moduleId = NotesConstants.AR_INVOICE;
        Criteria criteria = getCurrentSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("apInvoiceStatus", CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_IN_PROGRESS : STATUS_IN_PROGRESS));
        criteria.add(Restrictions.eq("apInvoiceId", invoiceId));
        if (CommonUtils.isNotEmpty(ids)) {
            criteria.add(Restrictions.not(Restrictions.in("transactionId", ids)));
        }
        List<Transaction> ars = criteria.list();
        for (Transaction ar : ars) {
            String moduleRefId = ar.getCustNo() + "-" + (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
            ar.setApInvoiceAmount(0d);
            ar.setApInvoiceId(null);
            ar.setApInvoiceStatus(null);
            ar.setBalanceInProcess(ar.getBalance());
            ar.setUpdatedOn(new Date());
            ar.setUpdatedBy(user.getUserId());
            String apInvoice = vendorNumber + "-" + invoiceNumber;
            StringBuilder desc = new StringBuilder("Unmarked as In Progress for ");
            desc.append(CommonUtils.isEqualIgnoreCase(from, "EDI") ? "EDI" : "AP");
            desc.append(" Invoice - '").append(apInvoice).append("'");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
    }

    private void inprogressArs(AccrualsForm accrualsForm, Integer invoiceId, User user) throws Exception {
        AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
        String vendorNumber = accrualsForm.getVendorNumber();
        String invoiceNumber = accrualsForm.getInvoiceNumber();
        String from = accrualsForm.getFrom();
        List<Integer> inprogressIds = new ArrayList<Integer>();
        if (CommonUtils.isNotEmpty(accrualsForm.getAssignedArIds())) {
            String moduleId = NotesConstants.AR_INVOICE;
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getAssignedArIds(), ","));
            for (String id : ids) {
                String[] values = StringUtils.splitByWholeSeparator(id, ":");
                Transaction ar = transactionDAO.findById(Integer.parseInt(values[0]));
                inprogressIds.add(ar.getTransactionId());
                String moduleRefId = ar.getCustNo() + "-" + (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
                if (CommonUtils.isNotEqualIgnoreCase(ar.getApInvoiceStatus(), STATUS_IN_PROGRESS)
                        || (CommonUtils.isEqualIgnoreCase(from, "EDI")
                        && CommonUtils.isNotEqualIgnoreCase(ar.getApInvoiceStatus(), STATUS_EDI_IN_PROGRESS))) {
                    String apInvoice = vendorNumber + "-" + invoiceNumber;
                    StringBuilder desc = new StringBuilder("Marked as In Progress for ");
                    desc.append(CommonUtils.isEqualIgnoreCase(from, "EDI") ? "EDI" : "AP");
                    desc.append(" Invoice - '").append(apInvoice).append("'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
                }
                ar.setApInvoiceAmount(Double.parseDouble(values[1].replace(",", "")));
                ar.setApInvoiceId(invoiceId);
                ar.setApInvoiceStatus(CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_IN_PROGRESS : STATUS_IN_PROGRESS);
                ar.setBalanceInProcess(0d);
                ar.setUpdatedOn(new Date());
                ar.setUpdatedBy(user.getUserId());
            }
        }
        unInprogressArs(inprogressIds, vendorNumber, invoiceNumber, invoiceId, from, user);
    }

    private void unInprogressAccruals(List<Integer> ids, String vendorNumber, String invoiceNumber, String from, User user) throws Exception {
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        String moduleId = NotesConstants.ACCRUALS;
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCRUALS));
        if (CommonUtils.isEqualIgnoreCase(from, "EDI")) {
            criteria.add(Restrictions.eq("status", STATUS_EDI_IN_PROGRESS));
        } else {
            criteria.add(Restrictions.eq("status", STATUS_IN_PROGRESS));
        }
        criteria.add(Restrictions.eq("custNo", vendorNumber));
        criteria.add(Restrictions.eq("invoiceNumber", invoiceNumber));
        if (CommonUtils.isNotEmpty(ids)) {
            criteria.add(Restrictions.not(Restrictions.in("transactionId", ids)));
        }
        List<TransactionLedger> accruals = criteria.list();
        for (TransactionLedger accrual : accruals) {
            accrual.setInvoiceNumber(invoiceNumber);
            accrual.setStatus(STATUS_OPEN);
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(user.getUserId());
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("b.invoice_number", accrual.getInvoiceNumber());
                        columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                        columns.put("b.modified_by_user_id", user.getUserId());
                        updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                        columns.put("lssac.modified_by_user_id", user.getUserId());
                        updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                    FclBlUtil fclBlutil = new FclBlUtil();
                    String importflagFor = "";
                    List<FclBlCostCodes> consolidatorList = new ArrayList();
                    if (null != fclBlCost) {
                        fclBlCost.setInvoiceNumber(invoiceNumber);
                        fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                        if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode())) {
                            String bolid = fclBlCostCodesDAO.getBolfromBillOfLadding(accrual.getBillLaddingNo());
                            List<FclBlCostCodes> costList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolid));
                            FclBl fclBl = getFclBl(accrual.getBillLaddingNo());
                            importflagFor = fclBl.getImportFlag();
                            if (importflagFor.equals("I")) {
                                consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, true);
                            } else {
                                consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, false);
                            }
                            List<FclBlCostCodes> newList = new ArrayList<FclBlCostCodes>();

                            for (FclBlCostCodes fclBlCostCodes : costList) {
                                if (!consolidatorList.contains(fclBlCostCodes)) {
                                    newList.add(fclBlCostCodes);
                                }
                            }
                            for (FclBlCostCodes fclBlCostCodes : newList) {
                                 if(CommonUtils.notIn(fclBlCostCodes.getCostCode(),"OCNFRT","OFIMP")){    
                                fclBlCost.setInvoiceNumber(invoiceNumber);
                                    fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                                }
                            }
                        }
                    }
                }
            }
            StringBuilder desc = new StringBuilder("Accrual of ");
            boolean addAnd = false;
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
            }
            desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
            desc.append(" is unmarked as In Progress for Invoice - '").append(invoiceNumber).append("'");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, accrual.getTransactionId().toString(), moduleId, user);
        }
    }

    private void inprogressAccruals(AccrualsForm accrualsForm, User user) throws Exception {
        List<Integer> inprogressIds = new ArrayList<Integer>();
        String vendorName = accrualsForm.getVendorName();
        String vendorNumber = accrualsForm.getVendorNumber();
        String invoiceNumber = accrualsForm.getInvoiceNumber();
        String from = accrualsForm.getFrom();
        if (CommonUtils.isNotEmpty(accrualsForm.getAssignedAccrualIds())) {
            FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
            String moduleId = NotesConstants.ACCRUALS;
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getAssignedAccrualIds(), ","));
            for (String id : ids) {
                TransactionLedger accrual = findById(id);
                inprogressIds.add(accrual.getTransactionId());
                if (CommonUtils.isNotEqualIgnoreCase(vendorNumber, accrual.getCustNo())) {
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    desc.append("'").append(accrual.getCustName()).append(" (").append(accrual.getCustNo()).append(")'");
                    desc.append(" changed to '").append(vendorName).append(" (").append(vendorNumber).append(")'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, accrual.getTransactionId().toString(), moduleId, user);
                    accrual.setCustName(vendorName);
                    accrual.setCustNo(vendorNumber);
                }
                if (CommonUtils.isNotEqualIgnoreCase(accrual.getStatus(), STATUS_IN_PROGRESS)
                        || (CommonUtils.isEqualIgnoreCase(from, "EDI")
                        && CommonUtils.isNotEqualIgnoreCase(accrual.getStatus(), STATUS_EDI_IN_PROGRESS))) {
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    boolean addAnd = false;
                    if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                        desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                        desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                        desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
                    }
                    desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                    desc.append(" is marked as In Progress for");
                    desc.append(CommonUtils.isEqualIgnoreCase(from, "EDI") ? " EDI" : "");
                    desc.append(" Invoice - '").append(invoiceNumber).append("'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, id, moduleId, user);
                }
                accrual.setInvoiceNumber(invoiceNumber);
                accrual.setStatus(CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_IN_PROGRESS : STATUS_IN_PROGRESS);
                accrual.setUpdatedOn(new Date());
                accrual.setUpdatedBy(user.getUserId());
                if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                    if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                        Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                        if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                            columns.put("b.sp_acct_no", accrual.getCustNo());
                            columns.put("b.invoice_number", accrual.getInvoiceNumber());
                            columns.put("t.trans_type", TRANSACTION_TYPE_IN_PROGRESS);
                            columns.put("b.modified_by_user_id", user.getUserId());
                            updateLclCost(accrual.getCostId(), columns);
                        } else {
                            columns.put("lssac.ap_acct_no", accrual.getCustNo());
                            columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                            columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_IN_PROGRESS);
                            columns.put("lssac.modified_by_user_id", user.getUserId());
                            updateLclUnitCost(accrual.getCostId(), columns);
                        }
                    } else {
                        FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                        FclBlUtil fclBlutil = new FclBlUtil();
                        List<FclBlCostCodes> consolidatorList = new ArrayList();
                        String importflagFor = "";
                        if (null != fclBlCost) {
                            fclBlCost.setAccName(accrual.getCustName());
                            fclBlCost.setAccNo(accrual.getCustNo());
                            fclBlCost.setInvoiceNumber(invoiceNumber);
                            fclBlCost.setTransactionType(TRANSACTION_TYPE_IN_PROGRESS);
                            if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode())) {
                                String bolid = fclBlCostCodesDAO.getBolfromBillOfLadding(accrual.getBillLaddingNo());
                                List<FclBlCostCodes> costList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolid));
                                FclBl fclBl = getFclBl(accrual.getBillLaddingNo());
                                importflagFor = fclBl.getImportFlag();
                                if (importflagFor.equals("I")) {
                                    consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, true);
                                } else {
                                    consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, false);
                                }
                                List<FclBlCostCodes> newList = new ArrayList<FclBlCostCodes>();
                                for (FclBlCostCodes fclBlCostCodes : costList) {
                                    if (!consolidatorList.contains(fclBlCostCodes)) {
                                        newList.add(fclBlCostCodes);
                                    }
                                }
                                for (FclBlCostCodes fclBlCostCodes : newList) {
                                     if(CommonUtils.notIn(fclBlCostCodes.getCostCode(),"OCNFRT","OFIMP")){
                                        fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_IN_PROGRESS);
                                        fclBlCostCodes.setAccName(accrual.getCustName());
                                        fclBlCostCodes.setAccNo(accrual.getCustNo());
                                        fclBlCostCodes.setInvoiceNumber(invoiceNumber);
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        unInprogressAccruals(inprogressIds, vendorNumber, invoiceNumber, from, user);
    }

    private void activateAccruals(List<String> ids, User user) throws Exception {
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        String moduleId = NotesConstants.ACCRUALS;
        for (String id : ids) {
            TransactionLedger accrual = findById(id);
            accrual.setStatus(STATUS_OPEN);
            accrual.setBalance(accrual.getTransactionAmt());
            accrual.setBalanceInProcess(accrual.getTransactionAmt());
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(user.getUserId());
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                        columns.put("b.modified_by_user_id", user.getUserId());
                        updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                        columns.put("lssac.modified_by_user_id", user.getUserId());
                        updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCost) {
                        fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                    }
                }
            }
            StringBuilder desc = new StringBuilder("Accrual of ");
            boolean addAnd = false;
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
            }
            desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
            desc.append(" is unmarked as Inactive");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, id, moduleId, user);
        }
    }

    private void inactivateAccruals(List<String> ids, User user) throws Exception {
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        String moduleId = NotesConstants.ACCRUALS;
        for (String id : ids) {
            TransactionLedger accrual = findById(id);
            if (CommonUtils.isNotEqualIgnoreCase(accrual.getStatus(), STATUS_INACTIVE)) {
                StringBuilder desc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                    desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
                }
                desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                desc.append(" is marked as Inactive");
                desc.append(" by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, id, moduleId, user);
            }
            accrual.setStatus(STATUS_INACTIVE);
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(user.getUserId());
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("t.trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                        columns.put("b.modified_by_user_id", user.getUserId());
                        updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                        columns.put("lssac.modified_by_user_id", user.getUserId());
                        updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                    FclBlUtil fclBlutil = new FclBlUtil();
                    List<FclBlCostCodes> consolidatorList = new ArrayList();
                    String importflagFor = "";
                    if (null != fclBlCost) {
                        fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                        if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode())) {
                            String bolid = fclBlCostCodesDAO.getBolfromBillOfLadding(accrual.getBillLaddingNo());
                            List<FclBlCostCodes> costList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolid));
                            FclBl fclBl = getFclBl(accrual.getBillLaddingNo());
                            importflagFor = fclBl.getImportFlag();
                            if (importflagFor.equals("I")) {
                                consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, true);
                            } else {
                                consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, false);
                            }
                            List<FclBlCostCodes> newList = new ArrayList<FclBlCostCodes>();

                            for (FclBlCostCodes fclBlCostCodes : costList) {
                                if (!consolidatorList.contains(fclBlCostCodes)) {
                                    newList.add(fclBlCostCodes);
                                }
                            }
                            for (FclBlCostCodes fclBlCostCodes : newList) {
                                 if(CommonUtils.notIn(fclBlCostCodes.getCostCode(),"OCNFRT","OFIMP")){
                                fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void undeletedAccruals(List<String> ids, User user) throws Exception {
        String moduleId = NotesConstants.ACCRUALS;
        for (String id : ids) {
            TransactionLedger accrual = findById(id);
            accrual.setStatus(STATUS_OPEN);
            accrual.setBalance(accrual.getTransactionAmt());
            accrual.setBalanceInProcess(accrual.getTransactionAmt());
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(user.getUserId());
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                createFclBlCost(accrual, user.getLoginName());
            }
            StringBuilder desc = new StringBuilder("Accrual of ");
            boolean addAnd = false;
            if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                addAnd = true;
            }
            if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
            }
            desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
            desc.append(" is unmarked as Delete");
            desc.append(" by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, id, moduleId, user);
        }
    }

    private void deleteFclOceanFrieghtCosts(int bolid) {
        String otherCosts = "'HAZFEE','BKRSUR','INTMDL','INTFS','INTRAMP','DEFER','INSURE','FAEXP','FFCOMM','FAECOMM','NASLAN'";
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update fcl_bl_costcodes");
        queryBuilder.append(" set delete_flag='yes',transaction_type='AC'");
        queryBuilder.append(" where bolid = ").append(bolid);
        queryBuilder.append(" and cost_code not in (").append(otherCosts).append(")");
        queryBuilder.append(" and read_only_flag != ''");
        queryBuilder.append(" and booking_flag is null");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    private void deleteAccruals(List<String> ids, User user) throws Exception {
        FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
        LclCostChargeDAO acDao = new LclCostChargeDAO();
        String moduleId = NotesConstants.ACCRUALS;
        for (String id : ids) {
            TransactionLedger accrual = findById(id);
            if (CommonUtils.isNotEqualIgnoreCase(accrual.getStatus(), STATUS_DELETED)) {
                StringBuilder desc = new StringBuilder("Accrual of ");
                boolean addAnd = false;
                if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                    desc.append(" B/L - '").append(accrual.getBillLaddingNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                    desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                    desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNo()).append("'");
                    addAnd = true;
                }
                if (CommonUtils.isNotEmpty(accrual.getChargeCode())) {
                    desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getChargeCode()).append("'");
                }
                desc.append(" for amount '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                desc.append(" is marked as Delete");
                desc.append(" by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));

                if (CommonUtils.isNotEmpty(accrual.getCostId())
                        && CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")
                        && CommonUtils.isAtLeastOneNotEmpty(accrual.getDocReceipt(), accrual.getVoyageNo())) {
                    StringBuilder drDesc = new StringBuilder();
                    drDesc.append("DELETED -> Cost Code -> ").append(accrual.getChargeCode());
                    drDesc.append(" Cost Amount -> ").append(NumberUtils.formatNumber(accrual.getTransactionAmt()));
                    insertLclRemarks(accrual.getDocReceipt(), accrual.getVoyageNo(), drDesc.toString(), user);
                }
                AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, id, moduleId, user);
            }
            accrual.setStatus(STATUS_DELETED);
            accrual.setBalance(0d);
            accrual.setBalanceInProcess(0d);
            accrual.setUpdatedOn(new Date());
            accrual.setUpdatedBy(user.getUserId());
            if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                    Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        columns.put("b.ap_amount", 0d);
                        columns.put("b.cost_flatrate_amount", 0d);
                        columns.put("b.invoice_number", null);
                        columns.put("b.sp_acct_no", null);
                        columns.put("b.ap_gl_mapping_id", null);
                        columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                        columns.put("b.modified_by_user_id", user.getUserId());
                        if (acDao.getArAmount(accrual.getCostId()) == 0.00) {
                            columns.put("b.deleted", 1);
                        }
                        updateLclCost(accrual.getCostId(), columns);
                    } else {
                        columns.put("lssac.ap_amount", 0d);
                        columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                        columns.put("lssac.deleted", 1);
                        columns.put("lssac.modified_by_user_id", user.getUserId());
                        updateLclUnitCost(accrual.getCostId(), columns);
                    }
                } else {
                    FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                    if (null != fclBlCost) {
                        fclBlCost.setDeleteFlag("yes");
                        fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
                        fclBlCost.setManifestModifyFlag("yes");
                        fclBlCost.setProcessedStatus("");
                        if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode()) && "on".equalsIgnoreCase(fclBlCost.getReadOnlyFlag())) {
                            deleteFclOceanFrieghtCosts(fclBlCost.getBolId());
                        }
                    }
                }
                accrual.setCostId(null);
            }
        }
    }

    public void disputeInvoice(AccrualsForm accrualsForm, User user) throws Exception {
        accrualsForm.setFrom(null);
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), false, true);
        if (null == apInvoice) {
            apInvoice = apInvoiceDAO.createInvoice(accrualsForm, user);
        } else {
            apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
            apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDueDate(), "MM/dd/yyyy"));
            apInvoice.setTerm(null != accrualsForm.getCreditId() ? accrualsForm.getCreditId().toString() : null);
            apInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        }
        apInvoice.setDisputeDate(new Date());
        apInvoice.setResolvedDate(null);
        apInvoice.setFromAddress(user.getEmail());
        User userId = new UserDAO().findUserName(accrualsForm.getLoginName());
        apInvoice.setUserId(userId.getUserId());
        apInvoice.setToAddress(accrualsForm.getToAddress());
        if (CommonUtils.isNotEqualIgnoreCase(apInvoice.getStatus(), STATUS_DISPUTE)) {
            String moduleId = NotesConstants.AP_INVOICE;
            String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            StringBuilder desc = new StringBuilder();
            desc.append("Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
            desc.append(" of ").append(accrualsForm.getVendorName());
            desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(apInvoice.getInvoiceAmount())).append("'");
            desc.append(" marked as Dispute by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
        apInvoice.setStatus(STATUS_DISPUTE);
        apInvoiceDAO.update(apInvoice);
        disputeArs(accrualsForm, apInvoice.getId(), user);
        disputeAccruals(accrualsForm, user);
        inprogressArs(accrualsForm, apInvoice.getId(), user);
        inprogressAccruals(accrualsForm, user);
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
    }

    public void disputeEdiInvoice(AccrualsForm accrualsForm, User user) throws Exception {
        accrualsForm.setFrom("EDI");
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), null);
        if (null == ediInvoice) {
            if (CommonUtils.isNotEqualIgnoreCase(accrualsForm.getInvoiceNumber(), accrualsForm.getEdiInvoiceNumber())) {
                ediInvoice = ediInvoiceDAO.cloneInvoice(accrualsForm.getVendorNumber(), accrualsForm.getEdiInvoiceNumber(), accrualsForm.getInvoiceNumber());
                ediInvoice.setCreatedDate(new Date());
                ediInvoice.setCreatedBy(user.getLoginName());
                ediInvoiceDAO.save(ediInvoice);
                ediInvoiceDAO.archiveInvoice(accrualsForm.getVendorNumber(), accrualsForm.getEdiInvoiceNumber(), user.getLoginName());
                String oldDocumentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getEdiInvoiceNumber();
                String newDocumentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
                new DocumentStoreLogDAO().copyDocuments("INVOICE", "INVOICE", oldDocumentId, newDocumentId);
            } else {
                throw new AccountingException("EDI Invoice not found");
            }
        }
        ediInvoice.setUpdatedDate(new Date());
        ediInvoice.setUpdatedBy(user.getLoginName());
        ediInvoice.setInvoiceDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
        ediInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        ediInvoice.setDisputedDate(new Date());
        ediInvoice.setResolvedDate(null);
        ediInvoice.setFromAddress(user.getEmail());
        ediInvoice.setToAddress(accrualsForm.getToAddress());
        if (CommonUtils.isNotEqualIgnoreCase(ediInvoice.getStatus(), STATUS_EDI_DISPUTE)) {
            String moduleId = NotesConstants.AP_INVOICE;
            String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            StringBuilder desc = new StringBuilder();
            desc.append("EDI Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
            desc.append(" of ").append(accrualsForm.getVendorName());
            desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(ediInvoice.getInvoiceAmount())).append("'");
            desc.append(" marked as Dispute by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
        ediInvoice.setStatus(STATUS_EDI_DISPUTE);
        ediInvoiceDAO.update(ediInvoice);
        if (CommonUtils.isNotEmpty(accrualsForm.getDisputedArIds())) {
        }
        disputeArs(accrualsForm, ediInvoice.getId(), user);
        disputeAccruals(accrualsForm, user);
        inprogressArs(accrualsForm, ediInvoice.getId(), user);
        inprogressAccruals(accrualsForm, user);
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
    }

    public void unDisputeInvoice(AccrualsForm accrualsForm, User user) throws AccountingException, Exception {
        accrualsForm.setFrom(null);
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), false, true);
        if (null == apInvoice) {
            throw new AccountingException("Invoice not found");
        } else {
            apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
            apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDueDate(), "MM/dd/yyyy"));
            apInvoice.setTerm(null != accrualsForm.getCreditId() ? accrualsForm.getCreditId().toString() : null);
            apInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        }
        if (CommonUtils.isEqualIgnoreCase(apInvoice.getStatus(), STATUS_DISPUTE)) {
            String moduleId = NotesConstants.AP_INVOICE;
            String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            StringBuilder desc = new StringBuilder();
            desc.append("Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
            desc.append(" of ").append(accrualsForm.getVendorName());
            desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(apInvoice.getInvoiceAmount())).append("'");
            desc.append(" unmarked as Dispute by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
        apInvoice.setStatus(STATUS_IN_PROGRESS);
        apInvoice.setResolvedDate(new Date());
        apInvoiceDAO.update(apInvoice);
        disputeArs(accrualsForm, apInvoice.getId(), user);
        disputeAccruals(accrualsForm, user);
        inprogressArs(accrualsForm, apInvoice.getId(), user);
        inprogressAccruals(accrualsForm, user);
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
    }

    public void unDisputeEdiInvoice(AccrualsForm accrualsForm, User user) throws Exception {
        accrualsForm.setFrom("EDI");
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), null);
        if (null == ediInvoice) {
            if (CommonUtils.isNotEqualIgnoreCase(accrualsForm.getInvoiceNumber(), accrualsForm.getEdiInvoiceNumber())) {
                ediInvoice = ediInvoiceDAO.cloneInvoice(accrualsForm.getVendorNumber(), accrualsForm.getEdiInvoiceNumber(), accrualsForm.getInvoiceNumber());
                ediInvoice.setCreatedDate(new Date());
                ediInvoice.setCreatedBy(user.getLoginName());
                ediInvoiceDAO.save(ediInvoice);
                ediInvoiceDAO.archiveInvoice(accrualsForm.getVendorNumber(), accrualsForm.getEdiInvoiceNumber(), user.getLoginName());
                String oldDocumentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getEdiInvoiceNumber();
                String newDocumentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
                new DocumentStoreLogDAO().copyDocuments("INVOICE", "INVOICE", oldDocumentId, newDocumentId);
            } else {
                throw new AccountingException("EDI Invoice not found");
            }
        }
        ediInvoice.setUpdatedDate(new Date());
        ediInvoice.setUpdatedBy(user.getLoginName());
        ediInvoice.setInvoiceDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
        ediInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        ediInvoice.setResolvedDate(new Date());
        if (CommonUtils.isEqualIgnoreCase(ediInvoice.getStatus(), STATUS_EDI_DISPUTE)) {
            String moduleId = NotesConstants.AP_INVOICE;
            String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            StringBuilder desc = new StringBuilder();
            desc.append("EDI Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
            desc.append(" of ").append(accrualsForm.getVendorName());
            desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(ediInvoice.getInvoiceAmount())).append("'");
            desc.append(" unmarked as Dispute by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
        ediInvoice.setStatus(STATUS_EDI_IN_PROGRESS);
        ediInvoiceDAO.update(ediInvoice);
        disputeArs(accrualsForm, ediInvoice.getId(), user);
        disputeAccruals(accrualsForm, user);
        inprogressArs(accrualsForm, ediInvoice.getId(), user);
        inprogressAccruals(accrualsForm, user);
        if (CommonUtils.isNotEmpty(accrualsForm.getAssignedArIds())) {
            inprogressArs(accrualsForm, ediInvoice.getId(), user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getAssignedAccrualIds())) {
            inprogressAccruals(accrualsForm, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
    }

    public void rejectInvoice(AccrualsForm accrualsForm, User user) throws Exception {
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), false, true);
        if (null == apInvoice) {
            apInvoice = apInvoiceDAO.createInvoice(accrualsForm, user);
        } else {
            apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
            apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDueDate(), "MM/dd/yyyy"));
            apInvoice.setTerm(null != accrualsForm.getCreditId() ? accrualsForm.getCreditId().toString() : null);
            apInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        }
        apInvoice.setDisputeDate(null);
        apInvoice.setResolvedDate(null);
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
        if (CommonUtils.isNotEqualIgnoreCase(apInvoice.getStatus(), STATUS_REJECT)) {
            StringBuilder desc = new StringBuilder();
            desc.append("Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
            desc.append(" of ").append(accrualsForm.getVendorName());
            desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(apInvoice.getInvoiceAmount())).append("'");
            desc.append(" marked as Reject by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
        if (CommonUtils.isNotEqualIgnoreCase(apInvoice.getNotes(), accrualsForm.getComments())) {
            AuditNotesUtils.insertAuditNotes(accrualsForm.getComments(), moduleId, moduleRefId, moduleId, user);
        }
        apInvoice.setStatus(STATUS_REJECT);
        apInvoiceDAO.update(apInvoice);
    }

    public void unRejectInvoice(AccrualsForm accrualsForm, User user) throws AccountingException, Exception {
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), false, true);
        if (null == apInvoice) {
            throw new AccountingException("Invoice not found");
        } else {
            apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
            apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDueDate(), "MM/dd/yyyy"));
            apInvoice.setTerm(null != accrualsForm.getCreditId() ? accrualsForm.getCreditId().toString() : null);
            apInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        }
        if (CommonUtils.isEqualIgnoreCase(apInvoice.getStatus(), STATUS_REJECT)) {
            String moduleId = NotesConstants.AP_INVOICE;
            String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            StringBuilder desc = new StringBuilder();
            desc.append("Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
            desc.append(" of ").append(accrualsForm.getVendorName());
            desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(apInvoice.getInvoiceAmount())).append("'");
            desc.append(" unmarked as Reject by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
        apInvoice.setStatus(STATUS_IN_PROGRESS);
        apInvoice.setResolvedDate(null);
        apInvoiceDAO.update(apInvoice);
    }

    public void inprogressInvoice(AccrualsForm accrualsForm, User user) throws Exception {
        accrualsForm.setFrom(null);
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), false, true);
        if (null == apInvoice) {
            apInvoice = apInvoiceDAO.createInvoice(accrualsForm, user);
        } else {
            apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
            apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDueDate(), "MM/dd/yyyy"));
            apInvoice.setTerm(null != accrualsForm.getCreditId() ? accrualsForm.getCreditId().toString() : null);
            apInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        }
        if (CommonUtils.isNotEqualIgnoreCase(apInvoice.getStatus(), STATUS_IN_PROGRESS)) {
            String moduleId = NotesConstants.AP_INVOICE;
            String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            StringBuilder desc = new StringBuilder();
            desc.append("Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
            desc.append(" of ").append(accrualsForm.getVendorName());
            desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(apInvoice.getInvoiceAmount())).append("'");
            desc.append(" marked as In Progress by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
        apInvoice.setStatus(STATUS_IN_PROGRESS);
        apInvoice.setDisputeDate(null);
        apInvoice.setResolvedDate(null);
        apInvoiceDAO.update(apInvoice);
        disputeArs(accrualsForm, apInvoice.getId(), user);
        disputeAccruals(accrualsForm, user);
        inprogressArs(accrualsForm, apInvoice.getId(), user);
        inprogressAccruals(accrualsForm, user);
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
    }

    public void inprogressEdiInvoice(AccrualsForm accrualsForm, User user) throws Exception {
        accrualsForm.setFrom("EDI");
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), null);
        if (null == ediInvoice) {
            if (CommonUtils.isNotEqualIgnoreCase(accrualsForm.getInvoiceNumber(), accrualsForm.getEdiInvoiceNumber())) {
                ediInvoice = ediInvoiceDAO.cloneInvoice(accrualsForm.getVendorNumber(), accrualsForm.getEdiInvoiceNumber(), accrualsForm.getInvoiceNumber());
                ediInvoice.setCreatedDate(new Date());
                ediInvoice.setCreatedBy(user.getLoginName());
                ediInvoiceDAO.save(ediInvoice);
                ediInvoiceDAO.archiveInvoice(accrualsForm.getVendorNumber(), accrualsForm.getEdiInvoiceNumber(), user.getLoginName());
                String oldDocumentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getEdiInvoiceNumber();
                String newDocumentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
                new DocumentStoreLogDAO().copyDocuments("INVOICE", "INVOICE", oldDocumentId, newDocumentId);
            } else {
                throw new AccountingException("EDI Invoice not found");
            }
        }
        ediInvoice.setUpdatedDate(new Date());
        ediInvoice.setUpdatedBy(user.getLoginName());
        ediInvoice.setInvoiceDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
        ediInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        if (CommonUtils.isNotEqualIgnoreCase(ediInvoice.getStatus(), STATUS_EDI_IN_PROGRESS)) {
            String moduleId = NotesConstants.AP_INVOICE;
            String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
            StringBuilder desc = new StringBuilder();
            desc.append("EDI Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
            desc.append(" of ").append(accrualsForm.getVendorName());
            desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
            desc.append(" for amount - '").append(NumberUtils.formatNumber(ediInvoice.getInvoiceAmount())).append("'");
            desc.append(" marked as In Progress by ").append(user.getLoginName());
            desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
            AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        }
        ediInvoice.setStatus(STATUS_EDI_IN_PROGRESS);
        ediInvoice.setDisputedDate(null);
        ediInvoice.setResolvedDate(null);
        ediInvoiceDAO.update(ediInvoice);
        disputeArs(accrualsForm, ediInvoice.getId(), user);
        disputeAccruals(accrualsForm, user);
        inprogressArs(accrualsForm, ediInvoice.getId(), user);
        inprogressAccruals(accrualsForm, user);
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
    }

    public String isAccrualsFullyMapped(String ids) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cast(transaction_id as char character set latin1) as ids");
        queryBuilder.append(" from transaction_ledger");
        queryBuilder.append(" where transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and transaction_id in (").append(ids).append(")");
        queryBuilder.append(" and (gl_account_number is null");
        queryBuilder.append(" or gl_account_number not in (select account from account_details))");
        List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        return CommonUtils.isNotEmpty(result) ? result.toString().replace("[", "").replace("]", "") : "";
    }

    private void assignAccrualsAndArs(AccrualsForm accrualsForm, Integer invoiceId,
            Date invoiceDate, Date postedDate, User user, HttpServletRequest request) throws Exception {
        String vendorName = accrualsForm.getVendorName();
        String vendorNumber = accrualsForm.getVendorNumber();
        String invoiceNumber = accrualsForm.getInvoiceNumber();
        String from = accrualsForm.getFrom();
        if (CommonUtils.isNotEmpty(accrualsForm.getAssignedAccrualIds()) || CommonUtils.isNotEmpty(accrualsForm.getAssignedArIds())) {
            FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
            String moduleId;
            List<String> accrualIds = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getAssignedAccrualIds(), ","));
            Set<String> customerReference = new HashSet<String>();
            double invoiceAmount = 0d;
            if (CommonUtils.isNotEmpty(accrualsForm.getAssignedArIds())) {
                AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
                moduleId = NotesConstants.AR_INVOICE;
                ArTransactionHistoryDAO arTransactionHistoryDAO = new ArTransactionHistoryDAO();
                StringBuilder imagePath = new StringBuilder(request.getScheme()).append("://");
                imagePath.append(request.getServerName()).append(":").append(request.getServerPort());
                imagePath.append(request.getContextPath()).append(LoadLogisoftProperties.getProperty("application.image.logo"));
                List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getAssignedArIds(), ","));
                for (String id : ids) {
                    String[] values = StringUtils.splitByWholeSeparator(id, ":");
                    Transaction ar = transactionDAO.findById(Integer.parseInt(values[0]));
                    String invoiceOrBl = (CommonUtils.isNotEmpty(ar.getBillLaddingNo()) ? ar.getBillLaddingNo() : ar.getInvoiceNumber());
                    String moduleRefId = ar.getCustNo() + "-" + invoiceOrBl;
                    double appliedAmount = Double.parseDouble(values[1].replace(",", ""));
                    invoiceAmount += appliedAmount;
                    ar.setChequeNumber(vendorNumber + "-" + invoiceNumber);
                    ar.setBalance(ar.getBalance() + appliedAmount);
                    TransactionLedger pjSubledger = new TransactionLedger();
                    PropertyUtils.copyProperties(pjSubledger, ar);
                    pjSubledger.setTransactionId(null);
                    pjSubledger.setCustName(ar.getCustName());
                    pjSubledger.setCustNo(ar.getCustNo());
                    pjSubledger.setBillLaddingNo(ar.getBillLaddingNo());
                    pjSubledger.setGlAccountNumber(LoadLogisoftProperties.getProperty("arControlAccount"));
                    pjSubledger.setTransactionAmt(appliedAmount);
                    pjSubledger.setBalance(appliedAmount);
                    pjSubledger.setBalanceInProcess(appliedAmount);
                    pjSubledger.setInvoiceNumber(invoiceNumber);
                    pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                    pjSubledger.setStatus(STATUS_OPEN);
                    pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
                    pjSubledger.setTransactionDate(invoiceDate);
                    pjSubledger.setPostedDate(postedDate);
                    pjSubledger.setCreatedOn(new Date());
                    pjSubledger.setCreatedBy(user.getUserId());
                    pjSubledger.setUpdatedOn(null);
                    pjSubledger.setUpdatedBy(null);
                    save(pjSubledger);
                    ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                    arTransactionHistory.setCustomerNumber(ar.getCustNo());
                    arTransactionHistory.setBlNumber(ar.getBillLaddingNo());
                    arTransactionHistory.setInvoiceNumber(ar.getInvoiceNumber());
                    arTransactionHistory.setInvoiceDate(ar.getTransactionDate());
                    arTransactionHistory.setTransactionDate(new Date());
                    arTransactionHistory.setPostedDate(postedDate);
                    arTransactionHistory.setTransactionAmount(appliedAmount);
                    arTransactionHistory.setAdjustmentAmount(0d);
                    arTransactionHistory.setCustomerReferenceNumber(ar.getCustomerReferenceNo());
                    arTransactionHistory.setVoyageNumber(ar.getVoyageNo());
                    arTransactionHistory.setCheckNumber(vendorNumber + "-" + invoiceNumber);
                    arTransactionHistory.setArBatchId(null);
                    arTransactionHistory.setApBatchId(null);
                    arTransactionHistory.setTransactionType("AP INV");
                    arTransactionHistory.setCreatedBy(user.getLoginName());
                    arTransactionHistory.setCreatedDate(new Date());
                    arTransactionHistoryDAO.save(arTransactionHistory);
                    ar.setApInvoiceAmount(0d);
                    ar.setApInvoiceId(invoiceId);
                    ar.setApInvoiceStatus(CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_ASSIGNED : STATUS_ASSIGN);
                    ar.setBalanceInProcess(ar.getBalance());
                    ar.setUpdatedOn(new Date());
                    ar.setUpdatedBy(user.getUserId());
                    String apInvoice = vendorNumber + "-" + invoiceNumber;
                    StringBuilder desc = new StringBuilder();
                    desc.append("For Amount : ").append(NumberUtils.formatNumber(appliedAmount)).append(",");
                    desc.append(" applied against ");
                    desc.append(CommonUtils.isEqualIgnoreCase(from, "EDI") ? "EDI" : "AP");
                    desc.append(" Invoice - '").append(apInvoice).append("'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
                    if (CommonUtils.isEqualIgnoreCase(ar.getCreditHold(), YES)) {
                        if (ar.isEmailed()) {
                            ArCreditHoldUtils.sendEmail(ar, user, false, imagePath.toString());
                        }
                        ar.setRemovedFromHold(true);
                        ar.setEmailed(false);
                        ar.setCreditHold(NO);
                        desc = new StringBuilder("BL# - ");
                        desc.append(invoiceOrBl).append(" of ");
                        desc.append(ar.getCustName()).append("(").append(ar.getCustNo()).append(")");
                        desc.append(" taken off credit hold");
                        desc.append(" by ").append(user.getLoginName());
                        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, "AR Credit Hold", user);
                    }
                }
            }
            if (CommonUtils.isNotEmpty(accrualsForm.getAssignedAccrualIds())) {
                moduleId = NotesConstants.ACCRUALS;
                for (String id : accrualIds) {
                    TransactionLedger accrual = findById(id);
                    accrual.setInvoiceNumber(invoiceNumber);
                    if (CommonUtils.isNotEqualIgnoreCase(vendorNumber, accrual.getCustNo())) {
                        StringBuilder desc = new StringBuilder("Accrual of ");
                        desc.append("'").append(accrual.getCustName()).append(" (").append(accrual.getCustNo()).append(")'");
                        desc.append(" changed to '").append(vendorName).append(" (").append(vendorNumber).append(")'");
                        desc.append(" by ").append(user.getLoginName());
                        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, accrual.getTransactionId().toString(), moduleId, user);
                        accrual.setCustName(vendorName);
                        accrual.setCustNo(vendorNumber);
                    }
                    invoiceAmount += accrual.getTransactionAmt();
                    TransactionLedger pjSubledger = new TransactionLedger();
                    PropertyUtils.copyProperties(pjSubledger, accrual);
                    pjSubledger.setTransactionId(null);
                    pjSubledger.setApCostKey(null);
                    pjSubledger.setCustName(accrual.getCustName());
                    pjSubledger.setCustNo(accrual.getCustNo());
                    pjSubledger.setInvoiceNumber(accrual.getInvoiceNumber());
                    pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                    pjSubledger.setStatus(STATUS_OPEN);
                    pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
                    pjSubledger.setTransactionDate(invoiceDate);
                    pjSubledger.setPostedDate(postedDate);
                    pjSubledger.setCreatedOn(new Date());
                    pjSubledger.setCreatedBy(user.getUserId());
                    save(pjSubledger);
                    if (CommonUtils.isNotEmpty(accrual.getCostId())) {
                        if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                            Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                            if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                                columns.put("b.ap_amount", accrual.getTransactionAmt());
                                columns.put("b.cost_flatrate_amount", accrual.getTransactionAmt());
                                columns.put("b.invoice_number", accrual.getInvoiceNumber());
                                columns.put("b.sp_acct_no", accrual.getCustNo());
                                columns.put("t.trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                columns.put("b.modified_by_user_id", user.getUserId());
                                updateLclCost(accrual.getCostId(), columns);
                            } else {
                                columns.put("lssac.ap_amount", accrual.getTransactionAmt());
                                columns.put("lssac.ap_reference_no", accrual.getInvoiceNumber());
                                columns.put("lssac.ap_acct_no", accrual.getCustNo());
                                columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                columns.put("lssac.modified_by_user_id", user.getUserId());
                                updateLclUnitCost(accrual.getCostId(), columns);
                            }
                        } else {
                            FclBlCostCodes fclBlCost = fclBlCostCodesDAO.findById(accrual.getCostId());
                            FclBlUtil fclBlutil = new FclBlUtil();
                            List<FclBlCostCodes> consolidatorList = new ArrayList();
                            String importflagFor = "";

                            if (null != fclBlCost) {
                                fclBlCost.setAccName(accrual.getCustName());
                                fclBlCost.setAccNo(accrual.getCustNo());
                                fclBlCost.setInvoiceNumber(accrual.getInvoiceNumber());
                                fclBlCost.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                if (CommonUtils.isEqualIgnoreCase(accrual.getStatus(), STATUS_DELETED)) {
                                    if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode())) {
                                        fclBlCost.setAmount(accrual.getTransactionAmt() - fclBlCost.getAmount());
                                    } else {
                                        fclBlCost.setAmount(accrual.getTransactionAmt());
                                    }
                                }
                                if (CommonUtils.isEqualIgnoreCase("OCNFRT", fclBlCost.getCostCode()) || CommonUtils.isEqualIgnoreCase("OFIMP", fclBlCost.getCostCode())) {
                                    String bolid = fclBlCostCodesDAO.getBolfromBillOfLadding(accrual.getBillLaddingNo());
                                    List<FclBlCostCodes> costList = fclBlCostCodesDAO.findByParenId(Integer.parseInt(bolid));
                                    FclBl fclBl = getFclBl(accrual.getBillLaddingNo());
                                    importflagFor = fclBl.getImportFlag();
                                    if (importflagFor.equals("I")) {
                                        consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, true);
                                    } else {
                                        consolidatorList = fclBlCostCodesDAO.consolidateRatesForCosts(costList, fclBl, false);
                                    }
                                    List<FclBlCostCodes> newList = new ArrayList<FclBlCostCodes>();

                                    for (FclBlCostCodes fclBlCostCodes : costList) {
                                        if (!consolidatorList.contains(fclBlCostCodes)) {
                                            newList.add(fclBlCostCodes);
                                        }
                                    }
                                    for (FclBlCostCodes fclBlCostCodes : newList) {
                                        if(CommonUtils.notIn(fclBlCostCodes.getCostCode(),"OCNFRT","OFIMP")){
                                            fclBlCostCodes.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                            fclBlCost.setAccName(accrual.getCustName());
                                            fclBlCost.setAccNo(accrual.getCustNo());
                                            fclBlCost.setInvoiceNumber(accrual.getInvoiceNumber());
                                        }
                                    }    
                                }
                            }
                        }
                    }
                    accrual.setBalance(0d);
                    accrual.setBalanceInProcess(0d);
                    accrual.setUpdatedOn(new Date());
                    accrual.setUpdatedBy(user.getUserId());
                    accrual.setStatus(CommonUtils.isEqualIgnoreCase(from, "EDI") ? STATUS_EDI_ASSIGNED : STATUS_ASSIGN);
                    accrual.setTransactionDate(invoiceDate);
                    accrual.setPostedDate(postedDate);
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    desc.append("Cost Code - '").append(accrual.getChargeCode().trim()).append("'");
                    if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                        desc.append(" and B/L - '").append(accrual.getBillLaddingNo().trim()).append("'");
                    }
                    if (CommonUtils.isNotEmpty(accrual.getDocReceipt())) {
                        desc.append(" and Doc Receipt - '").append(accrual.getDocReceipt()).append("'");
                    }
                    if (CommonUtils.isNotEmpty(accrual.getVoyageNo())) {
                        desc.append(" and Voyage - '").append(accrual.getVoyageNo()).append("'");
                    }
                    desc.append(" of '").append(accrual.getCustName()).append(" (").append(accrual.getCustNo()).append(")'");
                    desc.append(" for amount - '").append(NumberUtils.formatNumber(accrual.getTransactionAmt())).append("'");
                    desc.append(" is assigned to");
                    desc.append(CommonUtils.isEqualIgnoreCase(from, "EDI") ? " EDI" : "");
                    desc.append(" Invoice - '").append(accrual.getInvoiceNumber()).append("'");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, id, moduleId, user);
                    if (CommonUtils.isNotEmpty(accrual.getBillLaddingNo())) {
                        customerReference.add(accrual.getBillLaddingNo());
                    }
                }
            }
            if (CommonUtils.isNotEqual(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")), invoiceAmount)) {
                throw new AccountingException("Invoice Amount is not matching with sum of assigned amount");
            }
            AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();
            String ecuDesignation = (String) transactionDAO.getFieldValue(TradingPartner.class, "accountno", vendorNumber, "ecuDesignation");
            if (invoiceAmount >= 0d || TradingPartnerConstants.ECU_DESIGNATION_LOCAL.equalsIgnoreCase(ecuDesignation)) {
                //create an ap invoice entry
                Transaction ap = new Transaction();
                ap.setCustName(vendorName);
                ap.setCustNo(vendorNumber);
                ap.setInvoiceNumber(invoiceNumber);
                ap.setTransactionType(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                ap.setStatus(STATUS_OPEN);
                ap.setTransactionDate(invoiceDate);
                ap.setPostedDate(postedDate);
                ap.setTransactionAmt(invoiceAmount);
                ap.setBalance(invoiceAmount);
                ap.setBalanceInProcess(invoiceAmount);
                ap.setCustomerReferenceNo(StringUtils.left(customerReference.toString().replace("[", "").replace("]", ""), 500));
                ap.setDueDate(postedDate);
                ap.setBillTo(YES);
                ap.setArBatchId(null);
                ap.setApBatchId(null);
                ap.setCreatedOn(new Date());
                ap.setCreatedBy(user.getUserId());
                transactionDAO.save(ap);
                //History for ap invoice entry
                ApTransactionHistory apTransactionHistory = new ApTransactionHistory(ap);
                apTransactionHistory.setCreatedBy(user.getLoginName());
                new ApTransactionHistoryDAO().save(apTransactionHistory);
            } else {
                //Create an ar invoice entry for the negative ap
                Transaction ar = transactionDAO.getArTransaction(vendorNumber, null, invoiceNumber);
                if (null == ar) {
                    ar = new Transaction();
                    ar.setCustNo(vendorNumber);
                    ar.setCustName(vendorName);
                    ar.setInvoiceNumber(invoiceNumber);
                    ar.setTransactionAmt(-invoiceAmount);
                    ar.setBalance(-invoiceAmount);
                    ar.setBalanceInProcess(-invoiceAmount);
                    ar.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                    ar.setBillTo(YES);
                    ar.setTransactionDate(invoiceDate);
                    ar.setPostedDate(postedDate);
                    ar.setArBatchId(null);
                    ar.setApBatchId(null);
                    ar.setStatus(STATUS_OPEN);
                    ar.setCreatedBy(user.getUserId());
                    ar.setCreatedOn(new Date());
                    transactionDAO.save(ar);
                } else {
                    ar.setTransactionAmt(ar.getTransactionAmt() - invoiceAmount);
                    ar.setBalance(ar.getBalance() - invoiceAmount);
                    ar.setBalanceInProcess(ar.getBalanceInProcess() - invoiceAmount);
                    ar.setUpdatedBy(user.getUserId());
                    ar.setUpdatedOn(new Date());
                }
                StringBuilder desc = new StringBuilder();
                desc.append("Created For Amount : ").append(NumberUtils.formatNumber(-invoiceAmount)).append(",");
                desc.append(" against Negative AP");
                desc.append(" by ").append(user.getLoginName());
                desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                moduleId = NotesConstants.AR_INVOICE;
                AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, vendorNumber + "-" + invoiceNumber, moduleId, user);
                //Ar Transaction History
                ArTransactionHistory arTransactionHistory = new ArTransactionHistory();
                arTransactionHistory.setCustomerNumber(vendorNumber);
                arTransactionHistory.setBlNumber(null);
                arTransactionHistory.setInvoiceNumber(invoiceNumber);
                arTransactionHistory.setInvoiceDate(invoiceDate);
                arTransactionHistory.setTransactionDate(invoiceDate);
                arTransactionHistory.setPostedDate(postedDate);
                arTransactionHistory.setTransactionAmount(-invoiceAmount);
                arTransactionHistory.setArBatchId(null);
                arTransactionHistory.setApBatchId(null);
                arTransactionHistory.setTransactionType("AP CN");
                arTransactionHistory.setCreatedBy(user.getLoginName());
                arTransactionHistory.setCreatedDate(new Date());
                new ArTransactionHistoryDAO().save(arTransactionHistory);
                //PJ Subledger
                TransactionLedger pjSubledger = new TransactionLedger();
                pjSubledger.setCustNo(vendorNumber);
                pjSubledger.setCustName(vendorName);
                pjSubledger.setInvoiceNumber(invoiceNumber);
                pjSubledger.setGlAccountNumber(LoadLogisoftProperties.getProperty("arControlAccount"));
                pjSubledger.setTransactionAmt(-invoiceAmount);
                pjSubledger.setBalance(-invoiceAmount);
                pjSubledger.setBalanceInProcess(-invoiceAmount);
                pjSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                pjSubledger.setStatus(STATUS_OPEN);
                pjSubledger.setSubledgerSourceCode(SUB_LEDGER_CODE_PURCHASE_JOURNAL);
                pjSubledger.setTransactionDate(invoiceDate);
                pjSubledger.setPostedDate(postedDate);
                pjSubledger.setCreatedOn(new Date());
                pjSubledger.setCreatedBy(user.getUserId());
                pjSubledger.setUpdatedOn(null);
                pjSubledger.setUpdatedBy(null);
                save(pjSubledger);
            }
        }
        unDisputeArs(null, vendorNumber, invoiceNumber, invoiceId, from, user);
        unInprogressArs(null, vendorNumber, invoiceNumber, invoiceId, from, user);
        unInprogressAccruals(null, vendorNumber, invoiceNumber, from, user);
        unDisputeAccruals(null, vendorNumber, invoiceNumber, from, user);
    }

    public void postInvoice(AccrualsForm accrualsForm, User user, HttpServletRequest request) throws Exception {
        accrualsForm.setFrom(null);
        ApInvoiceDAO apInvoiceDAO = new ApInvoiceDAO();
        ApInvoice apInvoice = apInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), false, true);
        if (null == apInvoice) {
            apInvoice = apInvoiceDAO.createInvoice(accrualsForm, user);
        } else {
            apInvoice.setDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
            apInvoice.setDueDate(DateUtils.parseDate(accrualsForm.getDueDate(), "MM/dd/yyyy"));
            apInvoice.setTerm(null != accrualsForm.getCreditId() ? accrualsForm.getCreditId().toString() : null);
            apInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        }
        if (null != apInvoice.getDisputeDate()) {
            apInvoice.setResolvedDate(new Date());
        } else {
            apInvoice.setDisputeDate(null);
            apInvoice.setResolvedDate(null);
        }
        apInvoice.setStatus(TRANSACTION_TYPE_ACCOUNT_PAYABLE);
        Date invoiceDate = apInvoice.getDate();
        Date postedDate = getPostedDate(invoiceDate);
        assignAccrualsAndArs(accrualsForm, apInvoice.getId(), invoiceDate, postedDate, user, request);
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
        String glPeriod = DateUtils.formatDate(postedDate, "yyyyyMM");
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
        StringBuilder desc = new StringBuilder();
        desc.append("Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
        desc.append(" of ").append(accrualsForm.getVendorName());
        desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
        desc.append(" for amount - '").append(NumberUtils.formatNumber(apInvoice.getInvoiceAmount())).append("'");
        desc.append(" posted to AP on GL Period '").append(glPeriod).append("'");
        desc.append(" by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        apInvoiceDAO.update(apInvoice);
    }

    public void postEdiInvoice(AccrualsForm accrualsForm, User user, HttpServletRequest request) throws Exception {
        accrualsForm.setFrom("EDI");
        EdiInvoiceDAO ediInvoiceDAO = new EdiInvoiceDAO();
        EdiInvoice ediInvoice = ediInvoiceDAO.getInvoice(accrualsForm.getVendorNumber(), accrualsForm.getInvoiceNumber(), null);
        if (null == ediInvoice) {
            if (CommonUtils.isNotEqualIgnoreCase(accrualsForm.getInvoiceNumber(), accrualsForm.getEdiInvoiceNumber())) {
                ediInvoice = ediInvoiceDAO.cloneInvoice(accrualsForm.getVendorNumber(), accrualsForm.getEdiInvoiceNumber(), accrualsForm.getInvoiceNumber());
                ediInvoice.setCreatedDate(new Date());
                ediInvoice.setCreatedBy(user.getLoginName());
                ediInvoiceDAO.save(ediInvoice);
                ediInvoiceDAO.archiveInvoice(accrualsForm.getVendorNumber(), accrualsForm.getEdiInvoiceNumber(), user.getLoginName());
                String oldDocumentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getEdiInvoiceNumber();
                String newDocumentId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
                new DocumentStoreLogDAO().copyDocuments("INVOICE", "INVOICE", oldDocumentId, newDocumentId);
            } else {
                throw new AccountingException("EDI Invoice not found");
            }
        }
        ediInvoice.setUpdatedDate(new Date());
        ediInvoice.setUpdatedBy(user.getLoginName());
        ediInvoice.setInvoiceDate(DateUtils.parseDate(accrualsForm.getInvoiceDate(), "MM/dd/yyyy"));
        ediInvoice.setInvoiceAmount(Double.parseDouble(accrualsForm.getInvoiceAmount().replace(",", "")));
        if (null != ediInvoice.getDisputedDate()) {
            ediInvoice.setResolvedDate(new Date());
        } else {
            ediInvoice.setDisputedDate(null);
            ediInvoice.setResolvedDate(null);
        }
        ediInvoice.setStatus(STATUS_EDI_POSTED_TO_AP);
        Date invoiceDate = ediInvoice.getInvoiceDate();
        Date postedDate = getPostedDate(invoiceDate);
        assignAccrualsAndArs(accrualsForm, ediInvoice.getId(), invoiceDate, postedDate, user, request);
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
        String glPeriod = DateUtils.formatDate(postedDate, "yyyyyMM");
        String moduleId = NotesConstants.AP_INVOICE;
        String moduleRefId = accrualsForm.getVendorNumber() + "-" + accrualsForm.getInvoiceNumber();
        StringBuilder desc = new StringBuilder();
        desc.append("EDI Invoice - ").append(accrualsForm.getInvoiceNumber().trim());
        desc.append(" of ").append(accrualsForm.getVendorName());
        desc.append(" (").append(accrualsForm.getVendorNumber()).append(")");
        desc.append(" for amount - '").append(NumberUtils.formatNumber(ediInvoice.getInvoiceAmount())).append("'");
        desc.append(" posted to AP on GL Period '").append(glPeriod).append("'");
        desc.append(" by ").append(user.getLoginName());
        desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
        AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, moduleRefId, moduleId, user);
        ediInvoiceDAO.update(ediInvoice);
    }

    public List<ResultModel> getInvoiceDetails(String vendorNumber, String invoiceNumber, String blNumber, String transactionType) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select blNumber as blNumber,");
        queryBuilder.append("dockReceipt as dockReceipt,");
        queryBuilder.append("voyage as voyage,");
        queryBuilder.append("container as container,");
        queryBuilder.append("reportingDate as reportingDate,");
        queryBuilder.append("postedDate as postedDate,");
        queryBuilder.append("accruedAmount as accruedAmount,");
        queryBuilder.append("costCode as costCode,");
        queryBuilder.append("glAccount as glAccount,");
        queryBuilder.append("transactionType as transactionType");
        queryBuilder.append(" from (");
        queryBuilder.append("select bill_ladding_no as blNumber,");
        queryBuilder.append("drcpt as dockReceipt,");
        queryBuilder.append("voyage_no as voyage,");
        queryBuilder.append("container_no as container,");
        queryBuilder.append("date_format(sailing_date,'%m/%d/%Y') as reportingDate,");
        queryBuilder.append("date_format(posted_date,'%m/%d/%Y') as postedDate,");
        queryBuilder.append("format(transaction_amt,2) as accruedAmount,");
        queryBuilder.append("charge_code as costCode,");
        queryBuilder.append("gl_account_number as glAccount,");
        queryBuilder.append("'").append(TRANSACTION_TYPE_ACCRUALS).append("' as transactionType,");
        queryBuilder.append("transaction_id as id");
        queryBuilder.append(" from transaction_ledger");
        queryBuilder.append(" where Transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" and cust_no = '").append(vendorNumber).append("'");
        if (CommonUtils.isNotEmpty(invoiceNumber)) {
            queryBuilder.append(" and invoice_number = '").append(invoiceNumber).append("'");
        } else if (CommonUtils.isNotEmpty(blNumber)) {
            queryBuilder.append(" and Bill_Ladding_No = '").append(blNumber).append("'");
        }
        if (CommonUtils.isEqualIgnoreCase(transactionType, TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
            queryBuilder.append(" and (status='").append(STATUS_ASSIGN).append("'");
            queryBuilder.append(" or status='").append(STATUS_EDI_ASSIGNED).append("')");
        }
        if (CommonUtils.isEqualIgnoreCase(transactionType, TRANSACTION_TYPE_ACCOUNT_PAYABLE)) {
            queryBuilder.append(" union ");
            queryBuilder.append("(select th.invoice_or_bl as blNumber,");
            queryBuilder.append("ar.drcpt as dockReceipt,");
            queryBuilder.append("ar.voyage_no as voyage,");
            queryBuilder.append("ar.container_no as container,");
            queryBuilder.append("date_format(ar.transaction_date,'%m/%d/%Y') as reportingDate,");
            queryBuilder.append("date_format(th.posted_date,'%m/%d/%Y') as posted_date,");
            queryBuilder.append("format(th.transaction_amount,2) as accruedAmount,");
            queryBuilder.append("'' as costCode,");
            queryBuilder.append("'' as glAccount,");
            queryBuilder.append("'").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("' as transactionType,");
            queryBuilder.append("th.id as id");
            queryBuilder.append(" from ar_transaction_history th");
            queryBuilder.append(" join transaction ar");
            queryBuilder.append(" on (th.customer_number = ar.cust_no");
            queryBuilder.append(" and (th.invoice_or_bl = ar.Bill_Ladding_No or th.invoice_or_bl = ar.invoice_number)");
            queryBuilder.append(" and ar.transaction_type = '").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("')");
            queryBuilder.append(" where th.transaction_type = 'AP INV'");
            queryBuilder.append(" and th.check_number = '").append(vendorNumber).append("-").append(invoiceNumber).append("')");
        }
        queryBuilder.append(") as t");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setResultTransformer(Transformers.aliasToBean(ResultModel.class));
        return query.list();
    }

    public Integer inactivateAccruals(AccrualsForm accrualsForm, User user) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  transaction_id as id,");
        queryBuilder.append("  cost_id as costId,");
        queryBuilder.append("  shipment_type as shipmentType,");
        queryBuilder.append("  drcpt as dockReceipt,");
        queryBuilder.append("  bill_ladding_no as blNumber,");
        queryBuilder.append("  voyage_no as voyageNumber,");
        queryBuilder.append("  charge_code as costCode,");
        queryBuilder.append("  transaction_amt as amount ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction_ledger ");
        queryBuilder.append("where transaction_type = :transactionType ");
        queryBuilder.append("  and status = :status ");
        if (CommonUtils.isEqualIgnoreCase(accrualsForm.getInactivateBy(), "dateRange")) {
            queryBuilder.append("  and sailing_date between :fromDate and :toDate");
        } else {
            queryBuilder.append("  and transaction_amt between :fromAmount and :toAmount");
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getInactivateVendorNumber())) {
            queryBuilder.append("  and cust_no = : vendorNumber");
        } else {
            queryBuilder.append("  and cust_no not in (select cust_accno from vendor_info where exempt_inactivate = true)");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("transactionType", TRANSACTION_TYPE_ACCRUALS);
        query.setString("status", STATUS_OPEN);
        if (CommonUtils.isEqualIgnoreCase(accrualsForm.getInactivateBy(), "dateRange")) {
            Date fromDate = DateUtils.parseDate(accrualsForm.getFromDate() + " 00:00:00", "MM/dd/yyyy kk:mm:ss");
            Date toDate = DateUtils.parseDate(accrualsForm.getToDate() + " 23:59:59", "MM/dd/yyyy kk:mm:ss");
            query.setDate("fromDate", fromDate);
            query.setDate("toDate", toDate);
        } else {
            Double fromAmount = Double.parseDouble(accrualsForm.getFromAmount().replace(",", ""));
            Double toAmount = Double.parseDouble(accrualsForm.getToAmount().replace(",", ""));
            query.setDouble("fromAmount", fromAmount);
            query.setDouble("toAmount", toAmount);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getInactivateVendorNumber())) {
            query.setString("vendorNumber", accrualsForm.getInactivateVendorNumber());
        }
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("costId", BigIntegerType.INSTANCE);
        query.addScalar("shipmentType", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("amount", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(AccrualModel.class));
        List<AccrualModel> accruals = query.list();
        String moduleId = NotesConstants.ACCRUALS;
        int count = 0;
        if (CommonUtils.isNotEmpty(accruals)) {
            org.hibernate.Transaction tx = getCurrentSession().getTransaction();
            for (AccrualModel accrual : accruals) {
                try {
                    tx = getCurrentSession().getTransaction();
                    if (!tx.isActive()) {
                        tx.begin();
                    }
                    updateAccrualStatus(accrual.getId(), STATUS_INACTIVE, user.getUserId());
                    if (NumberUtils.isNotZero(accrual.getCostId())) {
                        if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                            Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                            if (CommonUtils.isNotEmpty(accrual.getDockReceipt())) {
                                columns.put("t.trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                columns.put("b.modified_by_user_id", user.getUserId());
                                updateLclCost(accrual.getCostId(), columns);
                            } else {
                                columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                                columns.put("lssac.modified_by_user_id", user.getUserId());
                                updateLclUnitCost(accrual.getCostId(), columns);
                            }
                        } else {
                            updateFclCostTransType(accrual.getCostId(), TRANSACTION_TYPE_ACCOUNT_PAYABLE);
                        }
                    }
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    boolean addAnd = false;
                    if (CommonUtils.isNotEmpty(accrual.getBlNumber())) {
                        desc.append(" B/L - '").append(accrual.getBlNumber()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getDockReceipt())) {
                        desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDockReceipt()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getVoyageNumber())) {
                        desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNumber()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getCostCode())) {
                        desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getCostCode()).append("'");
                    }
                    desc.append(" for amount '").append(NumberUtils.formatNumber(NumberUtils.parseNumber(accrual.getAmount()))).append("'");
                    desc.append(" is marked as Inactive");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, accrual.getId().toString(), moduleId, user);
                    if (tx.isActive() && getCurrentSession().isConnected() && getCurrentSession().isOpen()) {
                        tx.commit();
                    }
                    count++;
                } catch (Exception e) {
                    log.info("Inactivating accrual with id : " + accrual.getId() + "failed on " + new Date(), e);
                } finally {
                    if (tx.isActive() && getCurrentSession().isConnected() && getCurrentSession().isOpen()) {
                        tx.commit();
                    }
                }
            }
            tx = getCurrentSession().getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
        }
        return count;
    }

    public Integer activateAccruals(AccrualsForm accrualsForm, User user) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  transaction_id as id,");
        queryBuilder.append("  cost_id as costId,");
        queryBuilder.append("  shipment_type as shipmentType,");
        queryBuilder.append("  drcpt as dockReceipt,");
        queryBuilder.append("  bill_ladding_no as blNumber,");
        queryBuilder.append("  voyage_no as voyageNumber,");
        queryBuilder.append("  charge_code as costCode,");
        queryBuilder.append("  transaction_amt as amount ");
        queryBuilder.append("from");
        queryBuilder.append("  transaction_ledger ");
        queryBuilder.append("where transaction_type = :transactionType ");
        queryBuilder.append("  and status = :status ");
        if (CommonUtils.isEqualIgnoreCase(accrualsForm.getInactivateBy(), "dateRange")) {
            queryBuilder.append("  and sailing_date between :fromDate and :toDate");
        } else {
            queryBuilder.append("  and transaction_amt between :fromAmount and :toAmount");
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getInactivateVendorNumber())) {
            queryBuilder.append("  and cust_no = : vendorNumber");
        } else {
            queryBuilder.append("  and cust_no not in (select cust_accno from vendor_info where exempt_inactivate = true)");
        }
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("transactionType", TRANSACTION_TYPE_ACCRUALS);
        query.setString("status", STATUS_INACTIVE);
        if (CommonUtils.isEqualIgnoreCase(accrualsForm.getInactivateBy(), "dateRange")) {
            Date fromDate = DateUtils.parseDate(accrualsForm.getFromDate() + " 00:00:00", "MM/dd/yyyy kk:mm:ss");
            Date toDate = DateUtils.parseDate(accrualsForm.getToDate() + " 23:59:59", "MM/dd/yyyy kk:mm:ss");
            query.setDate("fromDate", fromDate);
            query.setDate("toDate", toDate);
        } else {
            Double fromAmount = Double.parseDouble(accrualsForm.getFromAmount().replace(",", ""));
            Double toAmount = Double.parseDouble(accrualsForm.getToAmount().replace(",", ""));
            query.setDouble("fromAmount", fromAmount);
            query.setDouble("toAmount", toAmount);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getInactivateVendorNumber())) {
            query.setString("vendorNumber", accrualsForm.getInactivateVendorNumber());
        }
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("costId", BigIntegerType.INSTANCE);
        query.addScalar("shipmentType", StringType.INSTANCE);
        query.addScalar("dockReceipt", StringType.INSTANCE);
        query.addScalar("blNumber", StringType.INSTANCE);
        query.addScalar("voyageNumber", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("amount", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(AccrualModel.class));
        List<AccrualModel> accruals = query.list();
        String moduleId = NotesConstants.ACCRUALS;
        int count = 0;
        if (CommonUtils.isNotEmpty(accruals)) {
            org.hibernate.Transaction tx = getCurrentSession().getTransaction();
            for (AccrualModel accrual : accruals) {
                try {
                    tx = getCurrentSession().getTransaction();
                    if (!tx.isActive()) {
                        tx.begin();
                    }
                    updateAccrualStatus(accrual.getId(), STATUS_OPEN, user.getUserId());
                    if (NumberUtils.isNotZero(accrual.getCostId())) {
                        if (CommonUtils.in(accrual.getShipmentType(), "LCLE", "LCLI")) {
                            Map<Serializable, Serializable> columns = new HashMap<Serializable, Serializable>();
                            if (CommonUtils.isNotEmpty(accrual.getDockReceipt())) {
                                columns.put("t.trans_type", TRANSACTION_TYPE_ACCRUALS);
                                columns.put("b.modified_by_user_id", user.getUserId());
                                updateLclCost(accrual.getCostId(), columns);
                            } else {
                                columns.put("lssac.ap_trans_type", TRANSACTION_TYPE_ACCRUALS);
                                columns.put("lssac.modified_by_user_id", user.getUserId());
                                updateLclUnitCost(accrual.getCostId(), columns);
                            }
                        } else {
                            updateFclCostTransType(accrual.getCostId(), TRANSACTION_TYPE_ACCRUALS);
                        }
                    }
                    StringBuilder desc = new StringBuilder("Accrual of ");
                    boolean addAnd = false;
                    if (CommonUtils.isNotEmpty(accrual.getBlNumber())) {
                        desc.append(" B/L - '").append(accrual.getBlNumber()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getDockReceipt())) {
                        desc.append(addAnd ? " and" : "").append(" Doc Receipt - '").append(accrual.getDockReceipt()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getVoyageNumber())) {
                        desc.append(addAnd ? " and" : "").append(" Voyage - '").append(accrual.getVoyageNumber()).append("'");
                        addAnd = true;
                    }
                    if (CommonUtils.isNotEmpty(accrual.getCostCode())) {
                        desc.append(addAnd ? " and" : "").append(" Cost Code - '").append(accrual.getCostCode()).append("'");
                    }
                    desc.append(" for amount '").append(NumberUtils.formatNumber(NumberUtils.parseNumber(accrual.getAmount()))).append("'");
                    desc.append(" is unmarked as Inactive");
                    desc.append(" by ").append(user.getLoginName());
                    desc.append(" on ").append(DateUtils.formatDate(new Date(), "MM/dd/yyyy kk:mm:ss"));
                    AuditNotesUtils.insertAuditNotes(desc.toString(), moduleId, accrual.getId().toString(), moduleId, user);
                    if (tx.isActive() && getCurrentSession().isConnected() && getCurrentSession().isOpen()) {
                        tx.commit();
                    }
                    count++;
                } catch (Exception e) {
                    log.info("Activating inactive accrual with id : " + accrual.getId() + "failed on " + new Date(), e);
                } finally {
                    if (tx.isActive() && getCurrentSession().isConnected() && getCurrentSession().isOpen()) {
                        tx.commit();
                    }
                }
            }
            tx = getCurrentSession().getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
        }
        return count;
    }

    public void deleteOrInactivateAccruals(AccrualsForm accrualsForm, User user) throws Exception {
        if (CommonUtils.isNotEmpty(accrualsForm.getInactiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getInactiveAccrualIds(), ","));
            inactivateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getDeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getDeleteAccrualIds(), ","));
            deleteAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getActiveAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getActiveAccrualIds(), ","));
            activateAccruals(ids, user);
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getUndeleteAccrualIds())) {
            List<String> ids = Arrays.asList(StringUtils.splitByWholeSeparator(accrualsForm.getUndeleteAccrualIds(), ","));
            undeletedAccruals(ids, user);
        }
    }

    public List<ResultModel> searchForReport(AccrualsForm accrualsForm) throws Exception {
        List<ResultModel> accruals = new ArrayList<ResultModel>();
        String acQuery = null;
        String arQuery = null;
        if (CommonUtils.isNotEmpty(accrualsForm.getAccrualIds()) || CommonUtils.isNotEmpty(accrualsForm.getArIds())) {
            if (CommonUtils.isNotEmpty(accrualsForm.getAccrualIds())) {
                acQuery = buildAcQuery(accrualsForm);
            }
            if (CommonUtils.isNotEmpty(accrualsForm.getArIds())) {
                arQuery = buildArQuery(accrualsForm);
            }
            accruals.addAll(getAccruals(acQuery, arQuery, accrualsForm.getSortBy(), accrualsForm.getOrderBy(), 0, 0, accrualsForm.getVendorNumber()));
        }
        if (CommonUtils.isNotEmpty(accrualsForm.getVendorNumber())
                || CommonUtils.isNotEmpty(accrualsForm.getSearchBy())
                || CommonUtils.isNotEmpty(accrualsForm.getSearchVendorNumber())) {
            acQuery = null;
            arQuery = null;
            if (CommonUtils.isNotEmpty(accrualsForm.getSearchBy()) || CommonUtils.isNotEmpty(accrualsForm.getSearchVendorNumber())) {
                accrualsForm.setAction("searchByFilter");
            } else if (CommonUtils.isNotEmpty(accrualsForm.getVendorNumber())) {
                accrualsForm.setAction("searchByVendor");
            }
            if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAr(), ONLY)) {
                arQuery = buildArQuery(accrualsForm);
            } else if (CommonUtils.isEqualIgnoreCase(accrualsForm.getAr(), YES)) {
                arQuery = buildArQuery(accrualsForm);
                acQuery = buildAcQuery(accrualsForm);
            } else {
                acQuery = buildAcQuery(accrualsForm);
            }
            accruals.addAll(getAccruals(acQuery, arQuery, accrualsForm.getSortBy(), accrualsForm.getOrderBy(), 0, 0, accrualsForm.getVendorNumber()));
        }
        return accruals;
    }

    public TransactionLedger getAccrualsByCostIdAndDR(Integer costId, String dockReceipt) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("costId", costId));
        criteria.add(Restrictions.eq("docReceipt", dockReceipt));
        criteria.setMaxResults(1);
        return (TransactionLedger) criteria.uniqueResult();
    }

    public TransactionLedger getAccrualsByCostIdVnCn(Integer costId, String voyageNo, String containerNo) {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        criteria.add(Restrictions.eq("costId", costId));
        criteria.add(Restrictions.eq("voyageNo", voyageNo));
        criteria.add(Restrictions.eq("containerNo", containerNo));
        criteria.setMaxResults(1);
        return (TransactionLedger) criteria.uniqueResult();
    }

    public void updateAccrualStatus(Integer transactionId, String status, Integer updatedBy) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  transaction_ledger ");
        queryBuilder.append("set");
        if (CommonUtils.isEqual(status, STATUS_INACTIVE)) {
            queryBuilder.append("  balance = transaction_amt,");
            queryBuilder.append("  balance_in_process = transaction_amt,");
        }
        queryBuilder.append("  status = :status,");
        queryBuilder.append("  updated_on = :updatedOn,");
        queryBuilder.append("  updated_by = :updatedBy ");
        queryBuilder.append("where");
        queryBuilder.append("  transaction_id = :transactionId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("status", status);
        query.setDate("updatedOn", new Date());
        query.setInteger("updatedBy", updatedBy);
        query.setInteger("transactionId", transactionId);
        query.executeUpdate();
    }

    public void updateFclCostTransType(BigInteger costId, String transactionType) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  fcl_bl_costcodes ");
        queryBuilder.append("set");
        queryBuilder.append("  transaction_type = :transactionType ");
        queryBuilder.append("where");
        queryBuilder.append("  code_id = :costId");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.setString("transactionType", transactionType);
        query.setBigInteger("costId", costId);
        query.executeUpdate();
    }

    public void updateLclCost(Serializable id, Map<Serializable, Serializable> fields) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  lcl_booking_ac b");
        queryBuilder.append("  join lcl_booking_ac_ta bta");
        queryBuilder.append("    on (bta.lcl_booking_ac_id = b.id)");
        queryBuilder.append("  join lcl_booking_ac_trans t");
        queryBuilder.append("    on (");
        queryBuilder.append("      b.file_number_id =  t.file_number_id");
        queryBuilder.append("      and bta.lcl_booking_ac_trans_id = t.id");
        queryBuilder.append("    ) ");
        queryBuilder.append("set ");
        int rowCount = 0;
        for (Serializable key : fields.keySet()) {
            Serializable value = fields.get(key);
            queryBuilder.append(key);
            if (null == value) {
                queryBuilder.append(" = null");
            } else {
                queryBuilder.append(" = '").append(value).append("'");
            }
            if (rowCount < fields.size() - 1) {
                queryBuilder.append(",");
            }
            rowCount++;
        }
        queryBuilder.append("  where b.id = ").append(id);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateLclUnitCost(Serializable id, Map<Serializable, Serializable> fields) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  lcl_ss_ac lssac ");
        queryBuilder.append("set ");
        int rowCount = 0;
        for (Serializable key : fields.keySet()) {
            Serializable value = fields.get(key);
            queryBuilder.append(key);
            if (null == value) {
                queryBuilder.append(" = null");
            } else {
                queryBuilder.append(" = '").append(value).append("'");
            }
            if (rowCount < fields.size() - 1) {
                queryBuilder.append(",");
            }
            rowCount++;
        }
        queryBuilder.append("  where lssac.id = ").append(id);
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void updateBookingBillladdingNo(Integer bolId, String bookingNo) throws Exception {
        String query = "update fcl_bl_costcodes set bolid=" + bolId + " where booking_id='" + bookingNo + "'";
        getCurrentSession().createSQLQuery(query).executeUpdate();
    }

    public void deleteAccruals(Integer blId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete");
        queryBuilder.append("  ac ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_bl_costcodes cc");
        queryBuilder.append("  join transaction_ledger ac");
        queryBuilder.append("    on (");
        queryBuilder.append("      cc.code_id = ac.cost_id");
        queryBuilder.append("      and ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append("      and ac.status = '").append(STATUS_OPEN).append("'");
        queryBuilder.append("    ) ");
        queryBuilder.append("where");
        queryBuilder.append("  cc.bolid = ").append(blId);
        queryBuilder.append("  and (cc.booking_id is null or cc.booking_id = 0)");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.executeUpdate();
    }

    public List<String> getShipmentTypes() throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("select shipment_type from gl_mapping where transaction_type = 'AC' and");
        query.append(" rev_exp = 'E' group by shipment_type");
        return (List<String>) getCurrentSession().createSQLQuery(query.toString()).list();
    }

    public void updateBillLaddingNoInTransactionLedger(String bolId, String bookingNo) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append("update");
        query.append("  transaction_ledger ac,");
        query.append("  fcl_bl_costcodes cc ");
        query.append("set");
        query.append("  ac.bill_ladding_no = '").append(bolId).append("' ");
        query.append("where");
        query.append("  cc.booking_id = '").append(bookingNo).append("'");
        query.append("  and ac.cost_id = cc.code_id");
        query.append("  and ac.transaction_type = 'AC'");
        getCurrentSession().createSQLQuery(query.toString()).executeUpdate();
    }

    public void insertLclRemarks(String fileNumber, String voyagNo, String remarks, User user) throws Exception {
        if (CommonUtils.isNotEmpty(fileNumber)) {
            LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
            String fileId = lclRemarksDAO.getFileId(fileNumber);
            lclRemarksDAO.insertLclRemarks(Long.parseLong(fileId), "auto", remarks, user.getUserId());
        }
        if (CommonUtils.isNotEmpty(voyagNo)) {
            LclUnitSsRemarksDAO lclUnitSsRemarksDAO = new LclUnitSsRemarksDAO();
            String findVoyageNo = voyagNo.substring(voyagNo.lastIndexOf("-") + 1);
            List unitIdandHeaderId = lclUnitSsRemarksDAO.getUnitandHeaderId(findVoyageNo);
            if (CommonUtils.isNotEmpty(unitIdandHeaderId)) {
                Object[] objects = (Object[]) unitIdandHeaderId.get(0);
                lclUnitSsRemarksDAO.insertLclunitRemarks(Long.parseLong(objects[0].toString()), Long.parseLong(objects[1].toString()), "auto", remarks.trim(), user.getUserId());
            }
        }
    }

    public TransactionLedger createLclCost(TransactionLedger accrual, Double difference, User user) throws Exception {
        GlMappingDAO glDao = new GlMappingDAO();
        LclUtils utils = new LclUtils();
        BigDecimal apAmount = new BigDecimal(difference);
        GlMapping apGlmapping = glDao.findByChargeCode(accrual.getChargeCode(), accrual.getShipmentType(), TRANSACTION_TYPE_ACCRUALS);
        GlMapping arGlmapping = glDao.findByChargeCode(accrual.getChargeCode(), accrual.getShipmentType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        if (null == arGlmapping) {
            arGlmapping = apGlmapping;
        }
        Long fileId = new LclFileNumberDAO().getFileIdByFileNumber(accrual.getDocReceipt());
        if (CommonUtils.isNotEmpty(fileId)) {
            LclBookingAc lclBookingAc = utils.saveBookingAc(fileId, apGlmapping, arGlmapping, apAmount, BigDecimal.ZERO, "C", "FL",
                    BigDecimal.ZERO, BigDecimal.ZERO, accrual.getInvoiceNumber(), accrual.getCustNo(), user);
            getCurrentSession().getTransaction().commit();
            getCurrentSession().getTransaction().begin();
            accrual = getDrcptCost(accrual.getDocReceipt(), lclBookingAc, accrual.getShipmentType());
        }
        return accrual;
    }

    public TransactionLedger createLclUnitCost(TransactionLedger accrual, User user) throws Exception {
        GlMappingDAO glDao = new GlMappingDAO();
        LclUtils utils = new LclUtils();
        BigDecimal apAmount = new BigDecimal(accrual.getTransactionAmt());
        GlMapping apGlmapping = glDao.findByChargeCode(accrual.getChargeCode(), accrual.getShipmentType(), TRANSACTION_TYPE_ACCRUALS);
        GlMapping arGlmapping = glDao.findByChargeCode(accrual.getChargeCode(), accrual.getShipmentType(), TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
        if (null == arGlmapping) {
            arGlmapping = apGlmapping;
        }
        Long unitSsId = new LclSsAcDAO().findByUnitSsId(accrual.getCostId().longValue());
        if (CommonUtils.isNotEmpty(unitSsId)) {
            LclSsAc lclSsAc = utils.saveSsAc(unitSsId, apGlmapping, arGlmapping, apAmount, BigDecimal.ZERO,
                    accrual.getInvoiceNumber(), accrual.getCustNo(), user);
            accrual.setCostId(lclSsAc.getId().intValue());
        }
        return accrual;
    }
    // Fcl,Convert to Quotation
     public void deleteAccrualsByBookingNo(Integer bookingNo) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("delete");
        queryBuilder.append("  ac ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_bl_costcodes cc");
        queryBuilder.append("  join transaction_ledger ac");
        queryBuilder.append("    on (");
        queryBuilder.append("      cc.code_id = ac.cost_id");
        queryBuilder.append("      and ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append("      and ac.status = '").append(STATUS_OPEN).append("'");
        queryBuilder.append("    ) ");
        queryBuilder.append("where");
        queryBuilder.append("  ac.booking_no = ").append(bookingNo);
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.executeUpdate();
     }
     
     private String buildAcLclSsQuery(AccrualsForm accrualsForm) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("(");
        queryBuilder.append("select ac.cust_name as vendorName,");
        queryBuilder.append("ac.cust_no as vendorNumber,");
        queryBuilder.append("ac.invoice_number as invoiceNumber,");
        queryBuilder.append("ac.bill_ladding_no as blNumber,");
        queryBuilder.append("ac.transaction_amt as accruedAmount,");
        queryBuilder.append("ac.bluescreen_chargecode as bluescreenCostCode,");
        queryBuilder.append("ac.charge_code as costCode,");
        queryBuilder.append("ac.gl_account_number as glAccount,");
        queryBuilder.append("ac.shipment_type as shipmentType,");
        queryBuilder.append("ac.container_no as container,");
        queryBuilder.append("ac.voyage_no as voyage,");
        queryBuilder.append("ac.drcpt as dockReceipt,");
        queryBuilder.append("ac.sailing_date as reportingDate,");
        queryBuilder.append("ac.transaction_type as transactionType,");
        queryBuilder.append("ac.status as status,");
        queryBuilder.append("ac.description as notes,");
        queryBuilder.append("cast(ac.cost_id as char character set latin1) as costId,");
        queryBuilder.append("cast(ac.transaction_id as char character set latin1) as id,");
        queryBuilder.append("ac.terminal as terminal");
        queryBuilder.append(" FROM lcl_ss_header header");
        queryBuilder.append(" JOIN lcl_ss_masterbl ssmaster  ON header.id = ssmaster.ss_header_id ");
        queryBuilder.append(" JOIN lcl_unit_ss unitss ON header.id = unitss.ss_header_id ");
        queryBuilder.append(" JOIN lcl_unit unit ON unit.id = unitss.unit_id ");
        queryBuilder.append(" JOIN lcl_ss_ac ssac ON ssac.unit_ss_id = unitss.id ");
        queryBuilder.append(" JOIN transaction_ledger ac ON ac.Container_No = unit.unit_no ");
        queryBuilder.append(" where ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" AND ac.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and ac.transaction_amt <> 0.00");
        queryBuilder.append(" and ac.balance <> 0.00");
        queryBuilder.append(" AND unitss.sp_booking_no = ssmaster.sp_booking_no");
        queryBuilder.append(" AND ssac.id = ac.cost_id");
        queryBuilder.append(" AND ssmaster.sp_booking_no = '").append(accrualsForm.getNewDockReceipt()).append("'");
        queryBuilder.append(" AND header.id  =").append(accrualsForm.getUnitId());
//        queryBuilder.append(" AND ac.cust_no = '").append(accrualsForm.getNewVendorNumber()).append("'");
        queryBuilder.append(" AND ac.shipment_type = 'LCLE'");
        
        queryBuilder.append(" UNION ");
        
        queryBuilder.append("select ac.cust_name as vendorName,");
        queryBuilder.append("ac.cust_no as vendorNumber,");
        queryBuilder.append("ac.invoice_number as invoiceNumber,");
        queryBuilder.append("ac.bill_ladding_no as blNumber,");
        queryBuilder.append("ac.transaction_amt as accruedAmount,");
        queryBuilder.append("ac.bluescreen_chargecode as bluescreenCostCode,");
        queryBuilder.append("ac.charge_code as costCode,");
        queryBuilder.append("ac.gl_account_number as glAccount,");
        queryBuilder.append("ac.shipment_type as shipmentType,");
        queryBuilder.append("ac.container_no as container,");
        queryBuilder.append("ac.voyage_no as voyage,");
        queryBuilder.append("ac.drcpt as dockReceipt,");
        queryBuilder.append("ac.sailing_date as reportingDate,");
        queryBuilder.append("ac.transaction_type as transactionType,");
        queryBuilder.append("ac.status as status,");
        queryBuilder.append("ac.description as notes,");
        queryBuilder.append("cast(ac.cost_id as char character set latin1) as costId,");
        queryBuilder.append("cast(ac.transaction_id as char character set latin1) as id,");
        queryBuilder.append("ac.terminal as terminal");
        queryBuilder.append(" FROM lcl_ss_header header");
        queryBuilder.append(" JOIN lcl_ss_masterbl ssmaster  ON header.id = ssmaster.ss_header_id ");
        queryBuilder.append(" JOIN lcl_unit_ss unitss ON header.id = unitss.ss_header_id ");
        queryBuilder.append(" JOIN lcl_unit unit ON unit.id = unitss.unit_id ");
        queryBuilder.append(" JOIN lcl_ss_ac ssac ON ssac.unit_ss_id = unitss.id ");
        queryBuilder.append(" JOIN transaction_ledger ac ON ac.Container_No = unit.unit_no ");
        queryBuilder.append(" JOIN lcl_booking_piece_unit bpu ON bpu.lcl_unit_ss_id = unitss.id ");
        queryBuilder.append(" JOIN lcl_booking_piece bp ON (bpu.booking_piece_id = bp.id) ");
        queryBuilder.append(" JOIN lcl_booking_ac ba ON (bp.file_number_id = ba.file_number_id) ");
        queryBuilder.append(" where ac.transaction_type = '").append(TRANSACTION_TYPE_ACCRUALS).append("'");
        queryBuilder.append(" AND ac.status='").append(STATUS_OPEN).append("'");
        queryBuilder.append(" and ac.transaction_amt <> 0.00");
        queryBuilder.append(" and ac.balance <> 0.00");
        queryBuilder.append(" AND unitss.sp_booking_no = ssmaster.sp_booking_no");
        queryBuilder.append(" AND ssmaster.sp_booking_no = '").append(accrualsForm.getNewDockReceipt()).append("'");
        queryBuilder.append(" AND header.id  =").append(accrualsForm.getUnitId());
//        queryBuilder.append(" AND ac.cust_no = '").append(accrualsForm.getNewVendorNumber()).append("'");
        queryBuilder.append(" AND ac.shipment_type = 'LCLE'");
        queryBuilder.append(" AND ba.id = ac.cost_id GROUP BY ac.transaction_id");
        queryBuilder.append(")");
        return queryBuilder.toString();
    }
}
