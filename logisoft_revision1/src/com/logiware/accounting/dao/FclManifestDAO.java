package com.logiware.accounting.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.bc.fcl.FclBlConstants;
import com.gp.cong.logisoft.bc.notes.NotesBC;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.CustomerAccountingDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.struts.LoadApplicationProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlCostCodes;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.domain.TransactionLedger;
import com.gp.cvst.logisoft.hibernate.dao.FclBlCostCodesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.logiware.accounting.model.ChargeModel;
import com.logiware.accounting.model.CostModel;
import com.logiware.accounting.model.InvoiceModel;
import com.logiware.hibernate.dao.AccountingTransactionDAO;
import com.logiware.hibernate.dao.ArTransactionHistoryDAO;
import com.logiware.hibernate.domain.ArTransactionHistory;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Lakshmi Narayanan
 */
public class FclManifestDAO extends BaseHibernateDAO implements ConstantsInterface {

    private boolean isManifested;
    private boolean isCorrected;
    private String manifestFlag;
    private String correctionFlag;
    private String dockReceipt;
    private String sealNumber;
    private Integer blId;
    private String blNumber;
    private String cnNumber;
    private String containerNumber;
    private String terminal;
    private Date reportingDate;
    private Date postedDate;
    private String subledger;
    private String shipmentType;
    private String shipperNumber;
    private String shipperName;
    private String forwarderNumber;
    private String forwarderName;
    private String consigneeNumber;
    private String consigneeName;
    private String thirdPartyNumber;
    private String thirdPartyName;
    private String agentNumber;
    private String agentName;
    private String notifyPartyNumber;
    private String notifyPartyName;
    private String historyType;
    private User user;
    private FclBl bl;
    private final Map<String, InvoiceModel> ars = new HashMap<String, InvoiceModel>();
    private final Map<String, Boolean> creditDebits = new HashMap<String, Boolean>();
    private final AccrualsDAO accrualsDAO = new AccrualsDAO();
    private final SubledgerDAO subledgerDAO = new SubledgerDAO();
    private final GlMappingDAO glMappingDAO = new GlMappingDAO();
    private final FclBlCostCodesDAO fclBlCostCodesDAO = new FclBlCostCodesDAO();
    private final TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
    private final CustomerAccountingDAO accountingDAO = new CustomerAccountingDAO();
    private final ArTransactionHistoryDAO historyDAO = new ArTransactionHistoryDAO();
    private final AccountingTransactionDAO transactionDAO = new AccountingTransactionDAO();

    public FclManifestDAO() {
    }

    public FclManifestDAO(FclBl bl, User user) {
        this.user = user;
        this.bl = bl;
        this.blId = bl.getBol();
    }

    private void getPreviousBlFields() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  bol as blId,");
        queryBuilder.append("  shp.acct_no as shipperNumber,");
        queryBuilder.append("  shp.acct_name as shipperName,");
        queryBuilder.append("  fwd.acct_no as forwarderNumber,");
        queryBuilder.append("  fwd.acct_name as forwarderName,");
        queryBuilder.append("  con.acct_no as consigneeNumber,");
        queryBuilder.append("  con.acct_name as consigneeName,");
        queryBuilder.append("  agt.acct_no as agentNumber,");
        queryBuilder.append("  agt.acct_name as agentName,");
        queryBuilder.append("  tpy.acct_no as thirdPartyNumber,");
        queryBuilder.append("  tpy.acct_name as thirdPartyName,");
        queryBuilder.append("  npy.acct_no as notifyPartyNumber,");
        queryBuilder.append("  npy.acct_name as notifyPartyName ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_bl bl");
        queryBuilder.append("  left join trading_partner shp");
        queryBuilder.append("    on (bl.house_shipper_no = shp.acct_no)");
        queryBuilder.append("  left join trading_partner fwd");
        queryBuilder.append("    on (bl.forward_agent_no = fwd.acct_no)");
        queryBuilder.append("  left join trading_partner con");
        queryBuilder.append("    on (bl.houseconsignee = con.acct_no)");
        queryBuilder.append("  left join trading_partner agt");
        queryBuilder.append("    on (bl.agent_no = agt.acct_no)");
        queryBuilder.append("  left join trading_partner tpy");
        queryBuilder.append("    on (bl.billtrdprty = tpy.acct_no)");
        queryBuilder.append("  left join trading_partner npy");
        queryBuilder.append("    on (bl.notify_party = npy.acct_no) ");
        queryBuilder.append("where");
        queryBuilder.append("  file_no = '").append(bl.getFileNo()).append("'");
        queryBuilder.append("  and bol <> ").append(blId).append(" ");
        queryBuilder.append("order by bol desc limit 1");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("blId", IntegerType.INSTANCE);
        query.addScalar("shipperNumber", StringType.INSTANCE);
        query.addScalar("shipperName", StringType.INSTANCE);
        query.addScalar("forwarderNumber", StringType.INSTANCE);
        query.addScalar("forwarderName", StringType.INSTANCE);
        query.addScalar("consigneeNumber", StringType.INSTANCE);
        query.addScalar("consigneeName", StringType.INSTANCE);
        query.addScalar("agentNumber", StringType.INSTANCE);
        query.addScalar("agentName", StringType.INSTANCE);
        query.addScalar("thirdPartyNumber", StringType.INSTANCE);
        query.addScalar("thirdPartyName", StringType.INSTANCE);
        query.addScalar("notifyPartyNumber", StringType.INSTANCE);
        query.addScalar("notifyPartyName", StringType.INSTANCE);
        Object result = query.uniqueResult();
        Object[] col = (Object[]) result;
        blId = (Integer) col[0];
        shipperNumber = (String) col[1];
        shipperName = (String) col[2];
        forwarderNumber = (String) col[3];
        forwarderName = (String) col[4];
        consigneeNumber = (String) col[5];
        consigneeName = (String) col[6];
        agentNumber = (String) col[7];
        agentName = (String) col[8];
        thirdPartyNumber = (String) col[9];
        thirdPartyName = (String) col[10];
        notifyPartyNumber = (String) col[11];
        notifyPartyName = (String) col[12];
    }

    private List<ChargeModel> getCharges() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  c.charges_id as id,");
        queryBuilder.append("  c.bill_to as billTo,");
        queryBuilder.append("  c.charge_code as chargeCode,");
        queryBuilder.append("  ").append(CommonUtils.isEqual(bl.getBol(), blId) ? "" : "-").append("c.amount as amount,");
        queryBuilder.append("  c.currency_code as currency,");
        queryBuilder.append("  c.charges_remarks as comments,");
        queryBuilder.append("  c.booking_flag as bookingFlag,");
        queryBuilder.append("  c.read_only_flag as readOnlyFlag,");
        queryBuilder.append("  if(b.bolid like '%==%', substring_index(b.bolid, '==', -1), null) as cnNumber ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_bl_charges c ");
        queryBuilder.append("  join fcl_bl b");
        queryBuilder.append("    on (c.bolid = b.bol) ");
        queryBuilder.append("where");
        queryBuilder.append("  c.bolid = ").append(blId).append(" ");
        queryBuilder.append("order by field(c.charge_code, 'OCNFRT','OFIMP') desc, c.charge_code asc, c.charges_id asc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("billTo", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("amount", DoubleType.INSTANCE);
        query.addScalar("currency", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("bookingFlag", StringType.INSTANCE);
        query.addScalar("readOnlyFlag", StringType.INSTANCE);
        query.addScalar("cnNumber", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ChargeModel.class));
        List<ChargeModel> result = query.list();
        //  String[] chargeCodes = LoadApplicationProperties.getProperty("OceanFreight").split(",");
        String importFlag = null != bl.getImportFlag() ? bl.getImportFlag() : "";
        String consolidator = "";
        if (!importFlag.equals("I")) {
            consolidator = LoadApplicationProperties.getProperty("OceanFreight");
        } else {
            consolidator = LoadApplicationProperties.getProperty("OceanFreightImports");
        }
        String chargeCodes[] = new String[5];
        if (consolidator.contains(",")) {
            chargeCodes = consolidator.split(",");
        }
        LinkedList<ChargeModel> charges = new LinkedList<ChargeModel>();
        boolean noAutoOfr = true;
        for (ChargeModel charge : result) {
            if ((CommonUtils.isEqual(charge.getChargeCode(), LoadApplicationProperties.getProperty("oceanfreightcharge"))
                    || CommonUtils.isEqual(charge.getChargeCode(), LoadApplicationProperties.getProperty("oceanfreightImpcharge")))
                    && CommonUtils.isNotEmpty(charge.getReadOnlyFlag())) {
                noAutoOfr = false;
            }
        }
        int index = 0;
        for (ChargeModel charge : result) {
            if (CommonUtils.in(charge.getChargeCode(), chargeCodes)
                    || CommonUtils.isEmpty(charge.getReadOnlyFlag())
                    || CommonUtils.in(charge.getChargeCode(), "DRAY", "INSURE")
                    || (CommonUtils.in(charge.getBookingFlag(), "new", "PP") && CommonUtils.isNotEqual(charge.getChargeCode(), FclBlConstants.FFCODE))) {
                if (CommonUtils.isEqual(charge.getChargeCode(), LoadApplicationProperties.getProperty("oceanfreightcharge")) || CommonUtils.isEqual(charge.getChargeCode(), LoadApplicationProperties.getProperty("oceanfreightImpcharge"))) {
                    charges.add(index, charge);
                    index++;
                } else {
                    charges.add(charge);
                }
            } else {
                boolean notAdded = true;
                for (ChargeModel ocnfrt : charges) {
                    if (CommonUtils.isEqual(ocnfrt.getChargeCode(), LoadApplicationProperties.getProperty("oceanfreightcharge"))
                            || CommonUtils.isEqual(ocnfrt.getChargeCode(), LoadApplicationProperties.getProperty("oceanfreightImpcharge")) && (CommonUtils.isNotEmpty(ocnfrt.getReadOnlyFlag()) || noAutoOfr)) {
                        ocnfrt.setAmount(ocnfrt.getAmount() + charge.getAmount());
                        notAdded = false;
                        break;
                    }
                }
                if (notAdded) {
                    charges.add(charge);
                }
            }
        }
        return charges;
    }

    private List<CostModel> getCosts() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  c.code_id as id,");
        queryBuilder.append("  tp.acct_no as vendorNumber,");
        queryBuilder.append("  tp.acct_name as vendorName,");
        queryBuilder.append("  c.cost_code as costCode,");
        queryBuilder.append("  c.amount as amount,");
        queryBuilder.append("  c.invoice_number as invoiceNumber,");
        queryBuilder.append("  c.currency_code as currency,");
        queryBuilder.append("  c.cost_comments as comments,");
        queryBuilder.append("  c.booking_flag as bookingFlag,");
        queryBuilder.append("  c.read_only_flag as readOnlyFlag ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_bl_costcodes c ");
        queryBuilder.append("  join trading_partner tp");
        queryBuilder.append("    on (c.account_no = tp.acct_no)");
        queryBuilder.append("where");
        queryBuilder.append("  c.bolid = ").append(blId);
        queryBuilder.append("  and (");
        queryBuilder.append("    c.transaction_type = ''");
        queryBuilder.append("    or c.transaction_type is null");
        queryBuilder.append("  )");
        queryBuilder.append("  and c.delete_flag = 'no' ");
        queryBuilder.append("order by field(c.cost_code, 'OCNFRT','OFIMP') desc, c.cost_code asc, c.code_id asc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("vendorNumber", StringType.INSTANCE);
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("amount", DoubleType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("currency", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("bookingFlag", StringType.INSTANCE);
        query.addScalar("readOnlyFlag", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CostModel.class));
        List<CostModel> result = query.list();
        String importFlag = null != bl.getImportFlag() ? bl.getImportFlag() : "";
        String consolidator = "";
        if (!importFlag.equals("I")) {
            consolidator = LoadApplicationProperties.getProperty("OceanFreight");
        } else {
            consolidator = LoadApplicationProperties.getProperty("OceanFreightImports");
        }
        String costCodes[] = new String[5];
        if (consolidator.contains(",")) {
            costCodes = consolidator.split(",");
        }
        LinkedList<CostModel> costs = new LinkedList<CostModel>();
        int index = 0;
        for (CostModel cost : result) {
            if (CommonUtils.in(cost.getCostCode(), costCodes)
                    || CommonUtils.isEmpty(cost.getReadOnlyFlag())
                    || CommonUtils.in(cost.getBookingFlag(), "new", "PP")
                    || CommonUtils.isStartsWith(cost.getCostCode(), "FAE")
                    || CommonUtils.in(cost.getCostCode(), "DRAY", "INSURE", "DEFER", FclBlConstants.FFCODE)) {
                if (CommonUtils.isEqual(cost.getCostCode(), LoadApplicationProperties.getProperty("oceanfreightcharge")) || CommonUtils.isEqual(cost.getCostCode(), LoadApplicationProperties.getProperty("oceanfreightImpcharge"))) {
                    costs.add(index, cost);
                    index++;
                } else {
                    costs.add(cost);
                }
            } else {
                for (CostModel ocnfrt : costs) {
                    if (CommonUtils.isEqual(ocnfrt.getCostCode(), LoadApplicationProperties.getProperty("oceanfreightcharge"))
                            || CommonUtils.isEqual(ocnfrt.getCostCode(), LoadApplicationProperties.getProperty("oceanfreightImpcharge")) && CommonUtils.isEqual(ocnfrt.getReadOnlyFlag(), "on")) {
                        ocnfrt.setAmount(ocnfrt.getAmount() + cost.getAmount());
                        break;
                    }
                }
            }
        }
        return costs;
    }

    private List<CostModel> getCostsWithAcStatus() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  c.code_id as id,");
        queryBuilder.append("  tp.acct_no as vendorNumber,");
        queryBuilder.append("  tp.acct_name as vendorName,");
        queryBuilder.append("  c.cost_code as costCode,");
        queryBuilder.append("  c.amount as amount,");
        queryBuilder.append("  c.invoice_number as invoiceNumber,");
        queryBuilder.append("  c.currency_code as currency,");
        queryBuilder.append("  c.cost_comments as comments,");
        queryBuilder.append("  c.booking_flag as bookingFlag,");
        queryBuilder.append("  c.read_only_flag as readOnlyFlag ");
        queryBuilder.append("from");
        queryBuilder.append("  fcl_bl_costcodes c ");
        queryBuilder.append("  join trading_partner tp");
        queryBuilder.append("    on (c.account_no = tp.acct_no)");
        queryBuilder.append("where");
        queryBuilder.append("  c.bolid = ").append(blId);
        queryBuilder.append("  and (");
        queryBuilder.append("    c.transaction_type = ''");
        queryBuilder.append("    or c.transaction_type is null");
        queryBuilder.append("    or c.transaction_type ='AC'");
        queryBuilder.append("  )");
        queryBuilder.append("  and c.delete_flag = 'no' ");
        queryBuilder.append("order by field(c.cost_code, 'OCNFRT','OFIMP') desc, c.cost_code asc, c.code_id asc");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("vendorNumber", StringType.INSTANCE);
        query.addScalar("vendorName", StringType.INSTANCE);
        query.addScalar("costCode", StringType.INSTANCE);
        query.addScalar("amount", DoubleType.INSTANCE);
        query.addScalar("invoiceNumber", StringType.INSTANCE);
        query.addScalar("currency", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("bookingFlag", StringType.INSTANCE);
        query.addScalar("readOnlyFlag", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(CostModel.class));
        List<CostModel> result = query.list();
        String[] costCodes = LoadApplicationProperties.getProperty("OceanFreight").split(",");
        LinkedList<CostModel> costs = new LinkedList<CostModel>();
        int index = 0;
        for (CostModel cost : result) {
            if (CommonUtils.in(cost.getCostCode(), costCodes)
                    || CommonUtils.isEmpty(cost.getReadOnlyFlag())
                    || CommonUtils.isEqual(cost.getBookingFlag(), "new")
                    || CommonUtils.isStartsWith(cost.getCostCode(), "FAE")
                    || CommonUtils.in(cost.getCostCode(), "DRAY", "INSURE", "DEFER", FclBlConstants.FFCODE)) {
                if (CommonUtils.isEqual(cost.getCostCode(), LoadApplicationProperties.getProperty("oceanfreightcharge")) || CommonUtils.isEqual(cost.getCostCode(), LoadApplicationProperties.getProperty("oceanfreightImpcharge"))) {
                    costs.add(index, cost);
                    index++;
                } else {
                    costs.add(cost);
                }
            } else {
                for (CostModel ocnfrt : costs) {
                    if (CommonUtils.isEqual(ocnfrt.getCostCode(), LoadApplicationProperties.getProperty("oceanfreightcharge")) || CommonUtils.isEqual(ocnfrt.getCostCode(), LoadApplicationProperties.getProperty("oceanfreightImpcharge"))
                            && CommonUtils.isEqual(ocnfrt.getReadOnlyFlag(), "on")) {
                        ocnfrt.setAmount(ocnfrt.getAmount() + cost.getAmount());
                        break;
                    }
                }
            }
        }
        return costs;
    }

    private List<ChargeModel> getCorrections() throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        queryBuilder.append("  c.id as id,");
        queryBuilder.append("  c.bill_to as billTo,");
        queryBuilder.append("  c.charge_code as chargeCode,");
        queryBuilder.append("  replace(format(c.amount, 2), ',', '') as amount,");
        queryBuilder.append("  'USD' as currency,");
        queryBuilder.append("  c.comments as comments,");
        queryBuilder.append("  c.notice_no as cnNumber ");
        queryBuilder.append("from");
        queryBuilder.append("  (");
        queryBuilder.append("    (");
        queryBuilder.append("      select");
        queryBuilder.append("        c.id as id,");
        queryBuilder.append("        c.bill_to_party as bill_to,");
        queryBuilder.append("        c.charge_code as charge_code,");
        queryBuilder.append("          c.difference_amount as amount,");
        queryBuilder.append("        c.comments as comments,");
        queryBuilder.append("        c.notice_no as notice_no");
        queryBuilder.append("      from");
        queryBuilder.append("        fclblcorrections c");
        queryBuilder.append("        join genericcode_dup ct");
        queryBuilder.append("          on (c.correction_type = ct.id)");
        queryBuilder.append("      where");
        queryBuilder.append("         c.new_bl_number = '").append(bl.getBolId()).append("'");
        queryBuilder.append("    )");
        queryBuilder.append("    union");
        queryBuilder.append("    (");
        queryBuilder.append("      select");
        queryBuilder.append("        c.id as id,");
        queryBuilder.append("        c.original_bill_to_party_correction_type_y as bill_to,");
        queryBuilder.append("        c.charge_code as charge_code,");
        queryBuilder.append("        -c.original_amount_correction_type_y as amount,");
        queryBuilder.append("        c.comments as comments,");
        queryBuilder.append("        c.notice_no as notice_no");
        queryBuilder.append("      from");
        queryBuilder.append("        fclblcorrections c");
        queryBuilder.append("        join genericcode_dup ct");
        queryBuilder.append("          on (");
        queryBuilder.append("            c.correction_type = ct.id");
        queryBuilder.append("            and ct.code = 'Y'");
        queryBuilder.append("          )");
        queryBuilder.append("      where");
        queryBuilder.append("        c.new_bl_number = '").append(bl.getBolId()).append("'");
        queryBuilder.append("        and c.original_bill_to_party_correction_type_y <> ''");
        queryBuilder.append("        and c.original_amount_correction_type_y <> 0.00");
        queryBuilder.append("    )");
        queryBuilder.append("  ) as c");
        SQLQuery query = getCurrentSession().createSQLQuery(queryBuilder.toString());
        query.addScalar("id", IntegerType.INSTANCE);
        query.addScalar("billTo", StringType.INSTANCE);
        query.addScalar("chargeCode", StringType.INSTANCE);
        query.addScalar("amount", DoubleType.INSTANCE);
        query.addScalar("currency", StringType.INSTANCE);
        query.addScalar("comments", StringType.INSTANCE);
        query.addScalar("cnNumber", StringType.INSTANCE);
        query.setResultTransformer(Transformers.aliasToBean(ChargeModel.class));
        return query.list();
    }

    private List<TransactionLedger> getManifestedSubledgers() throws Exception {
        Criteria criteria = getCurrentSession().createCriteria(TransactionLedger.class);
        if (dockReceipt.contains("-")) {
            criteria.add(Restrictions.eq("docReceipt", StringUtils.substringBefore(dockReceipt, "-")));
        } else {
            criteria.add(Restrictions.eq("docReceipt", dockReceipt));
        }
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(Restrictions.eq("billLaddingNo", blNumber));
        disjunction.add(Restrictions.like("billLaddingNo", dockReceipt, MatchMode.END));
        criteria.add(disjunction);
        criteria.add(Restrictions.like("subledgerSourceCode", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "-%"));
        criteria.add(Restrictions.eq("transactionType", TRANSACTION_TYPE_ACCOUNT_RECEIVABLE));
        criteria.add(Restrictions.eq("manifestFlag", YES));
        return criteria.list();
    }

    private void createArHistory(Transaction ar, double amount) throws Exception {
        ArTransactionHistory history = new ArTransactionHistory();
        history.setCustomerNumber(ar.getCustNo());
        history.setBlNumber(ar.getBillLaddingNo());
        history.setInvoiceNumber(ar.getInvoiceNumber());
        history.setInvoiceDate(reportingDate);
        history.setTransactionDate(reportingDate);
        history.setPostedDate(postedDate);
        history.setTransactionAmount(amount);
        history.setCustomerReferenceNumber(ar.getCustomerReferenceNo());
        history.setVoyageNumber(ar.getVoyageNo());
        history.setTransactionType(historyType);
        history.setCreatedBy(user.getLoginName());
        history.setCreatedDate(new Date());
        history.setCorrectionNotice(ar.getCorrectionNotice());
        historyDAO.save(history);
    }

    private void createArInvoice() throws Exception {
        if (isManifested) {
            for (String key : ars.keySet()) {
                InvoiceModel invoice = ars.get(key);
                String drcpt = dockReceipt;
                if (drcpt.contains("-")) {
                    drcpt = StringUtils.substringBefore(drcpt, "-");
                }
                String invoiceBl = invoice.getBlNumber();
                if (invoiceBl.contains("-04-")) {
                    invoiceBl = StringUtils.substringAfter(invoiceBl, "-04-");
                }
                Transaction ar = transactionDAO.getArInvoiceOnlyFcl(invoice.getCustomerNumber(), invoiceBl, drcpt);
                if (null != ar && !invoice.isCreditDebit()) {
                    if (CommonUtils.isEqualIgnoreCase(ar.getInvoiceNumber(), "PRE PAYMENT")) {
                        ar.setPostedDate(postedDate);
                        StringBuilder queryBuilder = new StringBuilder();
                        queryBuilder.append("update");
                        queryBuilder.append("  ar_transaction_history ");
                        queryBuilder.append("set");
                        queryBuilder.append("  bl_number = '").append(invoice.getBlNumber()).append("',");
                        queryBuilder.append("  invoice_number = '").append(invoice.getBlNumber()).append("',");
                        queryBuilder.append("  invoice_or_bl = '").append(invoice.getBlNumber()).append("' ");
                        queryBuilder.append("where");
                        queryBuilder.append("  customer_number='").append(invoice.getCustomerNumber()).append("'");
                        queryBuilder.append("  and invoice_or_bl like '%").append(invoiceBl).append("'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

                        queryBuilder.delete(0, queryBuilder.length());
                        queryBuilder.append("update");
                        queryBuilder.append("  document_store_log ");
                        queryBuilder.append("set");
                        queryBuilder.append("  document_id = '").append(invoice.getCustomerNumber()).append("-").append(invoice.getBlNumber()).append("'");
                        queryBuilder.append("where");
                        queryBuilder.append("   document_id = '").append(invoice.getCustomerNumber()).append("-04").append(dockReceipt).append("'");
                        queryBuilder.append("   and screen_name = 'INVOICE'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

                        queryBuilder.delete(0, queryBuilder.length());
                        queryBuilder.append("update");
                        queryBuilder.append("  notes ");
                        queryBuilder.append("set");
                        queryBuilder.append("  module_ref_id = '").append(invoice.getCustomerNumber()).append("-").append(invoice.getBlNumber()).append("'");
                        queryBuilder.append("where");
                        queryBuilder.append("  module_ref_id = '").append(invoice.getCustomerNumber()).append("-04").append(dockReceipt).append("'");
                        queryBuilder.append("  and module_id = '").append(NotesConstants.AR_INVOICE).append("'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

                        ar.setBillLaddingNo(invoice.getBlNumber());
                    }
                    ar.setTransactionAmt(ar.getTransactionAmt() + invoice.getAmount());
                    ar.setBalance(ar.getBalance() + invoice.getAmount());
                    ar.setBalanceInProcess(ar.getBalanceInProcess() + invoice.getAmount());
                    ar.setUpdatedOn(new Date());
                    ar.setUpdatedBy(user.getUserId());
                } else {
                    ar = new Transaction();
                    ar.setStatus(STATUS_OPEN);
                    ar.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                    ar.setCustNo(invoice.getCustomerNumber());
                    ar.setCustName(tradingPartnerDAO.getAccountName(invoice.getCustomerNumber()));
                    ar.setTransactionAmt(invoice.getAmount());
                    ar.setBalance(invoice.getAmount());
                    ar.setBalanceInProcess(invoice.getAmount());
                    ar.setPostedDate(postedDate);
                    ar.setBillLaddingNo(invoice.getBlNumber());
                    String creditStatus = accountingDAO.getCreditStatus(invoice.getCustomerNumber());
                    ar.setCreditHold(CommonUtils.isEqualIgnoreCase(creditStatus, "In Good Standing") ? NO : YES);
                    ar.setCreatedOn(new Date());
                    ar.setCreatedBy(user.getUserId());
                }
                ar.setTransactionDate(reportingDate);
                ar.setSailingDate(reportingDate);
                ar.setEta(bl.getEta());
                ar.setSteamShipLine(bl.getStreamShipLine());
                ar.setBookingNo(bl.getBookingNo());
                if (dockReceipt.contains("-")) {
                    ar.setDocReceipt(StringUtils.substringBefore(dockReceipt, "-"));
                } else {
                    ar.setDocReceipt(dockReceipt);
                }
                ar.setVoyageNo(String.valueOf(bl.getVoyages()));
                ar.setSealNo(sealNumber);
                ar.setContainerNo(containerNumber);
                ar.setMasterBl(bl.getStreamShipBl());
                ar.setSubHouseBl(bl.getHouseBl());
                ar.setBillTo(invoice.getBillTo());
                ar.setBlTerms(bl.getHouseBl());
                ar.setOrgTerminal(bl.getOriginalTerminal());
                ar.setDestination(bl.getPortofDischarge());
                ar.setShipperNo(shipperNumber);
                ar.setShipperName(shipperName);
                ar.setConsNo(consigneeNumber);
                ar.setConsName(consigneeName);
                ar.setFwdNo(forwarderNumber);
                ar.setFwdName(forwarderName);
                ar.setThirdptyNo(thirdPartyNumber);
                ar.setThirdptyName(thirdPartyName);
                ar.setAgentNo(agentNumber);
                ar.setAgentName(agentName);
                ar.setCustomerReferenceNo(bl.getExportReference());
                ar.setVesselNo(null != bl.getVessel() ? bl.getVessel().getCode() : "");
                ar.setVesselName(null != bl.getVessel() ? bl.getVessel().getCodedesc() : "");
                ar.setManifestFlag(manifestFlag);
                if (isCorrected) {
                    ar.setCorrectionNotice(cnNumber);
                } else {
                    ar.setCorrectionNotice(null);
                }
                ar.setCorrectionFlag(correctionFlag);
                transactionDAO.saveOrUpdate(ar);
                createArHistory(ar, invoice.getAmount());
            }
        } else {
            for (String key : ars.keySet()) {
                InvoiceModel invoice = ars.get(key);
                String invoiceBl = invoice.getBlNumber();
                if (invoiceBl.contains("-04-")) {
                    invoiceBl = StringUtils.substringAfter(invoiceBl, "-04-");
                }
                Transaction ar = transactionDAO.getArInvoiceOnlyFcl(invoice.getCustomerNumber(), invoiceBl, null);
                if (null != ar) {
                    if (CommonUtils.isEqualIgnoreCase(ar.getInvoiceNumber(), "PRE PAYMENT")) {
                        ar.setTransactionDate(reportingDate);
                        ar.setPostedDate(postedDate);

                        StringBuilder queryBuilder = new StringBuilder();
                        queryBuilder.append("update");
                        queryBuilder.append("  ar_transaction_history ");
                        queryBuilder.append("set");
                        queryBuilder.append("  bl_number = '").append(invoice.getBlNumber()).append("',");
                        queryBuilder.append("  invoice_number = '").append(invoice.getBlNumber()).append("',");
                        queryBuilder.append("  invoice_or_bl = '").append(invoice.getBlNumber()).append("' ");
                        queryBuilder.append("where");
                        queryBuilder.append("  customer_number='").append(invoice.getCustomerNumber()).append("'");
                        queryBuilder.append("  and invoice_or_bl like '%").append(invoiceBl).append("'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

                        queryBuilder.delete(0, queryBuilder.length());
                        queryBuilder.append("update");
                        queryBuilder.append("  document_store_log ");
                        queryBuilder.append("set");
                        queryBuilder.append("  document_id = '").append(invoice.getCustomerNumber()).append("-").append(invoice.getBlNumber()).append("'");
                        queryBuilder.append("where");
                        queryBuilder.append("   document_id = '").append(invoice.getCustomerNumber()).append("-04").append(dockReceipt).append("'");
                        queryBuilder.append("   and screen_name = 'INVOICE'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

                        queryBuilder.delete(0, queryBuilder.length());
                        queryBuilder.append("update");
                        queryBuilder.append("  notes ");
                        queryBuilder.append("set");
                        queryBuilder.append("  module_ref_id = '").append(invoice.getCustomerNumber()).append("-").append(invoice.getBlNumber()).append("'");
                        queryBuilder.append("where");
                        queryBuilder.append("  module_ref_id = '").append(invoice.getCustomerNumber()).append("-04").append(dockReceipt).append("'");
                        queryBuilder.append("  and module_id = '").append(NotesConstants.AR_INVOICE).append("'");
                        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

                        ar.setBillLaddingNo(invoice.getBlNumber());
                    }
                    ar.setTransactionAmt(ar.getTransactionAmt() + invoice.getAmount());
                    ar.setBalance(ar.getBalance() + invoice.getAmount());
                    ar.setBalanceInProcess(ar.getBalanceInProcess() + invoice.getAmount());
                    ar.setUpdatedOn(new Date());
                    ar.setUpdatedBy(user.getUserId());
                } else {
                    ar = new Transaction();
                    ar.setStatus(STATUS_OPEN);
                    ar.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
                    ar.setCustNo(invoice.getCustomerNumber());
                    ar.setCustName(tradingPartnerDAO.getAccountName(invoice.getCustomerNumber()));
                    ar.setTransactionAmt(invoice.getAmount());
                    ar.setBalance(invoice.getAmount());
                    ar.setBalanceInProcess(invoice.getAmount());
                    ar.setTransactionDate(reportingDate);
                    ar.setPostedDate(postedDate);
                    ar.setBillLaddingNo(invoice.getBlNumber());
                    String creditStatus = accountingDAO.getCreditStatus(invoice.getCustomerNumber());
                    ar.setCreditHold(CommonUtils.isEqualIgnoreCase(creditStatus, "In Good Standing") ? NO : YES);
                    ar.setCreatedOn(new Date());
                    ar.setCreatedBy(user.getUserId());
                }
                ar.setSailingDate(reportingDate);
                ar.setEta(bl.getEta());
                ar.setSteamShipLine(bl.getStreamShipLine());
                ar.setBookingNo(bl.getBookingNo());
                if (dockReceipt.contains("-")) {
                    ar.setDocReceipt(StringUtils.substringBefore(dockReceipt, "-"));
                } else {
                    ar.setDocReceipt(dockReceipt);
                }
                ar.setVoyageNo(String.valueOf(bl.getVoyages()));
                ar.setSealNo(sealNumber);
                ar.setContainerNo(containerNumber);
                ar.setMasterBl(bl.getStreamShipBl());
                ar.setSubHouseBl(bl.getHouseBl());
                ar.setBillTo(invoice.getBillTo());
                ar.setBlTerms(bl.getHouseBl());
                ar.setOrgTerminal(bl.getOriginalTerminal());
                ar.setDestination(bl.getPortofDischarge());
                ar.setShipperNo(shipperNumber);
                ar.setShipperName(shipperName);
                ar.setConsNo(consigneeNumber);
                ar.setConsName(consigneeName);
                ar.setFwdNo(forwarderNumber);
                ar.setFwdName(forwarderName);
                ar.setThirdptyNo(thirdPartyNumber);
                ar.setThirdptyName(thirdPartyName);
                ar.setAgentNo(agentNumber);
                ar.setAgentName(agentName);
                ar.setCustomerReferenceNo(bl.getExportReference());
                ar.setVesselNo(null != bl.getVessel() ? bl.getVessel().getCode() : "");
                ar.setVesselName(null != bl.getVessel() ? bl.getVessel().getCodedesc() : "");
                ar.setManifestFlag(NO);
                createArHistory(ar, invoice.getAmount());
                ar.setCorrectionNotice(null);
                ar.setCorrectionFlag(NO);
                transactionDAO.saveOrUpdate(ar);
            }
        }
    }

    private void createArSubledgers(List<ChargeModel> charges) throws Exception {
        for (ChargeModel charge : charges) {
            TransactionLedger arSubledger = new TransactionLedger();
            arSubledger.setTransactionDate(reportingDate);
            arSubledger.setSailingDate(reportingDate);
            arSubledger.setPostedDate(postedDate);
            double amount = 0d;
            if (null != charge.getAmount()) {
                amount = isManifested ? charge.getAmount() : -charge.getAmount();
            }
            arSubledger.setTransactionAmt(amount);
            arSubledger.setBalance(amount);
            arSubledger.setBalanceInProcess(amount);
            arSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            arSubledger.setSubledgerSourceCode(subledger);
            arSubledger.setStatus(STATUS_OPEN);
            String customerNumber;
            String customerName;
            String billTo;
            if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "Shipper")) {
                customerNumber = shipperNumber;
                customerName = shipperName;
                billTo = "S";
            } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "Forwarder")) {
                customerNumber = forwarderNumber;
                customerName = forwarderName;
                billTo = "F";
            } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "Consignee")) {
                customerNumber = consigneeNumber;
                customerName = consigneeName;
                billTo = "C";
            } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "ThirdParty")) {
                customerNumber = thirdPartyNumber;
                customerName = thirdPartyName;
                billTo = "T";
            } else if (CommonUtils.isEqualIgnoreCase(charge.getBillTo(), "Agent")) {
                customerNumber = agentNumber;
                customerName = agentName;
                billTo = "A";
            } else {
                customerNumber = notifyPartyNumber;
                customerName = notifyPartyName;
                billTo = "N";
            }
            arSubledger.setCustNo(customerNumber);
            arSubledger.setCustName(customerName);
            arSubledger.setBillTo(billTo);
            StringBuilder key = new StringBuilder();
            key.append(customerNumber);
            key.append("==");
            key.append(blNumber);
            if (isCorrected) {
                if (!creditDebits.containsKey(customerNumber)) {
                    creditDebits.put(customerNumber, accountingDAO.isCreditDebitNote(customerNumber));
                }
                if (creditDebits.get(customerNumber)) {
                    key.append("==");
                    key.append(cnNumber);
                }
            }
            if (ars.containsKey(key.toString())) {
                InvoiceModel invoice = ars.get(key.toString());
                invoice.setAmount(invoice.getAmount() + amount);
                ars.put(key.toString(), invoice);
            } else {
                InvoiceModel invoice = new InvoiceModel();
                invoice.setCustomerName(customerName);
                invoice.setCustomerNumber(customerNumber);
                invoice.setBillTo(billTo);
                invoice.setAmount(amount);
                if (isCorrected && creditDebits.get(customerNumber)) {
                    invoice.setBlNumber(blNumber + "CN" + cnNumber);
                } else {
                    invoice.setBlNumber(blNumber);
                }
                if (isCorrected) {
                    invoice.setCreditDebit(creditDebits.get(customerNumber));
                }
                ars.put(key.toString(), invoice);
            }
            if (isCorrected && creditDebits.get(customerNumber)) {
                arSubledger.setBillLaddingNo(blNumber + "CN" + cnNumber);
            } else {
                arSubledger.setBillLaddingNo(blNumber);
            }
            arSubledger.setBookingNo(bl.getBookingNo());
            if (dockReceipt.contains("-")) {
                arSubledger.setDocReceipt(StringUtils.substringBefore(dockReceipt, "-"));
            } else {
                arSubledger.setDocReceipt(dockReceipt);
            }
            arSubledger.setVoyageNo(String.valueOf(bl.getVoyages()));
            arSubledger.setContainerNo(containerNumber);
            arSubledger.setMasterBl(bl.getStreamShipBl());
            arSubledger.setSubHouseBl(bl.getHouseBl());
            arSubledger.setShipmentType(shipmentType);
            if (CommonUtils.isEqualIgnoreCase(charge.getChargeCode(), FclBlConstants.INTRAMP)) {
                arSubledger.setChargeCode(FclBlConstants.INTMDL);
            } else {
                arSubledger.setChargeCode(charge.getChargeCode());
            }
            String bluescreenChargeCode = glMappingDAO.getBlueScreenChargeCode(shipmentType, arSubledger.getChargeCode(), "AR", "R");
            arSubledger.setBlueScreenChargeCode(bluescreenChargeCode);
            String glAccountNumber = glMappingDAO.dervieGlAccount(arSubledger.getChargeCode(), shipmentType, terminal, "R");
            arSubledger.setGlAccountNumber(glAccountNumber);
            arSubledger.setReadyToPost(ON);
            arSubledger.setCurrencyCode(charge.getCurrency());
            arSubledger.setDescription(charge.getComments());
            arSubledger.setBlTerms(bl.getHouseBl());
            arSubledger.setOrgTerminal(bl.getOriginalTerminal());
            arSubledger.setDestination(bl.getPortofDischarge());
            arSubledger.setShipNo(shipperNumber);
            arSubledger.setShipName(shipperName);
            arSubledger.setConsNo(consigneeNumber);
            arSubledger.setConsName(consigneeName);
            arSubledger.setFwdNo(forwarderNumber);
            arSubledger.setFwdName(forwarderName);
            arSubledger.setThirdptyNo(thirdPartyNumber);
            arSubledger.setThirdptyName(thirdPartyName);
            arSubledger.setAgentNo(agentNumber);
            arSubledger.setAgentName(agentName);
            arSubledger.setBillingTerminal(bl.getBillingTerminal());
            arSubledger.setCustomerReferenceNo(bl.getExportReference());
            arSubledger.setVesselNo(null != bl.getVessel() ? bl.getVessel().getCode() : "");
            arSubledger.setCorrectionFlag(NO);
            arSubledger.setChargeId(charge.getId());
            arSubledger.setManifestFlag(manifestFlag);
            if (null != cnNumber) {
                arSubledger.setCorrectionNotice(cnNumber + (CommonUtils.isEqual(bl.getBol(), blId) ? FclBlConstants.CNA : FclBlConstants.CNS));
            }
            arSubledger.setNoticeNumber(cnNumber);
            arSubledger.setCorrectionFlag(correctionFlag);
            arSubledger.setCreatedOn(new Date());
            arSubledger.setCreatedBy(user.getUserId());
            subledgerDAO.save(arSubledger);
        }
    }

    private void reverseArSubledgers(List<TransactionLedger> subledgers) throws Exception {
        for (TransactionLedger oldSubledger : subledgers) {
            TransactionLedger newSubledger = new TransactionLedger();
            newSubledger.setTransactionDate(reportingDate);
            newSubledger.setSailingDate(reportingDate);
            newSubledger.setPostedDate(postedDate);
            double amount = -oldSubledger.getTransactionAmt();
            newSubledger.setTransactionAmt(amount);
            newSubledger.setBalance(amount);
            newSubledger.setBalanceInProcess(amount);
            newSubledger.setTransactionType(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE);
            newSubledger.setSubledgerSourceCode(oldSubledger.getSubledgerSourceCode());
            newSubledger.setStatus(STATUS_OPEN);
            String customerNumber = oldSubledger.getCustNo();
            String customerName = oldSubledger.getCustName();
            String billTo = oldSubledger.getBillTo();
            newSubledger.setCustNo(customerNumber);
            newSubledger.setCustName(customerName);
            newSubledger.setBillTo(billTo);
            StringBuilder key = new StringBuilder();
            key.append(customerNumber);
            key.append("==");
            key.append(oldSubledger.getBillLaddingNo());
            if (ars.containsKey(key.toString())) {
                InvoiceModel invoice = ars.get(key.toString());
                invoice.setAmount(invoice.getAmount() + amount);
                ars.put(key.toString(), invoice);
            } else {
                InvoiceModel invoice = new InvoiceModel();
                invoice.setCustomerName(customerName);
                invoice.setCustomerNumber(customerNumber);
                invoice.setBillTo(billTo);
                invoice.setAmount(amount);
                invoice.setBlNumber(oldSubledger.getBillLaddingNo());
                ars.put(key.toString(), invoice);
            }
            newSubledger.setBillLaddingNo(oldSubledger.getBillLaddingNo());
            newSubledger.setBookingNo(bl.getBookingNo());
            if (dockReceipt.contains("-")) {
                newSubledger.setDocReceipt(StringUtils.substringBefore(dockReceipt, "-"));
            } else {
                newSubledger.setDocReceipt(dockReceipt);
            }
            newSubledger.setVoyageNo(String.valueOf(bl.getVoyages()));
            newSubledger.setContainerNo(containerNumber);
            newSubledger.setMasterBl(bl.getStreamShipBl());
            newSubledger.setSubHouseBl(bl.getHouseBl());
            newSubledger.setShipmentType(shipmentType);
            newSubledger.setChargeCode(oldSubledger.getChargeCode());
            String bluescreenChargeCode = glMappingDAO.getBlueScreenChargeCode(shipmentType, newSubledger.getChargeCode(), "AR", "R");
            newSubledger.setBlueScreenChargeCode(bluescreenChargeCode);
            String glAccountNumber = glMappingDAO.dervieGlAccount(newSubledger.getChargeCode(), shipmentType, terminal, "R");
            newSubledger.setGlAccountNumber(glAccountNumber);
            newSubledger.setReadyToPost(ON);
            newSubledger.setCurrencyCode(oldSubledger.getCurrencyCode());
            newSubledger.setDescription(oldSubledger.getDescription());
            newSubledger.setBlTerms(bl.getHouseBl());
            newSubledger.setOrgTerminal(bl.getOriginalTerminal());
            newSubledger.setDestination(bl.getPortofDischarge());
            newSubledger.setShipNo(shipperNumber);
            newSubledger.setShipName(shipperName);
            newSubledger.setConsNo(consigneeNumber);
            newSubledger.setConsName(consigneeName);
            newSubledger.setFwdNo(forwarderNumber);
            newSubledger.setFwdName(forwarderName);
            newSubledger.setThirdptyNo(thirdPartyNumber);
            newSubledger.setThirdptyName(thirdPartyName);
            newSubledger.setAgentNo(agentNumber);
            newSubledger.setAgentName(agentName);
            newSubledger.setBillingTerminal(bl.getBillingTerminal());
            newSubledger.setCustomerReferenceNo(bl.getExportReference());
            newSubledger.setVesselNo(null != bl.getVessel() ? bl.getVessel().getCode() : "");
            newSubledger.setCorrectionFlag(NO);
            newSubledger.setChargeId(oldSubledger.getChargeId());
            newSubledger.setManifestFlag(NO);
            newSubledger.setCorrectionNotice(oldSubledger.getCorrectionNotice());
            newSubledger.setNoticeNumber(oldSubledger.getNoticeNumber());
            newSubledger.setCorrectionFlag(NO);
            newSubledger.setCreatedOn(new Date());
            newSubledger.setCreatedBy(user.getUserId());
            subledgerDAO.save(newSubledger);
            oldSubledger.setManifestFlag(NO);
            oldSubledger.setUpdatedOn(new Date());
            oldSubledger.setUpdatedBy(user.getUserId());
            subledgerDAO.update(oldSubledger);
        }
    }

    private void createAccruals(List<CostModel> costs) throws Exception {
        for (CostModel cost : costs) {
            TransactionLedger accrual = new TransactionLedger();
            accrual.setTransactionDate(reportingDate);
            accrual.setSailingDate(reportingDate);
            accrual.setTransactionAmt(cost.getAmount());
            accrual.setBalance(cost.getAmount());
            accrual.setBalanceInProcess(cost.getAmount());
            accrual.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
            accrual.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
            accrual.setStatus(STATUS_OPEN);
            accrual.setCustNo(cost.getVendorNumber());
            accrual.setCustName(cost.getVendorName());
            accrual.setBillLaddingNo(blNumber);
            accrual.setInvoiceNumber(cost.getInvoiceNumber());
            accrual.setBookingNo(bl.getBookingNo());
            if (dockReceipt.contains("-")) {
                accrual.setDocReceipt(StringUtils.substringBefore(dockReceipt, "-"));
            } else {
                accrual.setDocReceipt(dockReceipt);
            }
            accrual.setVoyageNo(String.valueOf(bl.getVoyages()));
            accrual.setContainerNo(containerNumber);
            accrual.setMasterBl(bl.getStreamShipBl());
            accrual.setSubHouseBl(bl.getHouseBl());
            accrual.setShipmentType(shipmentType);
            if (CommonUtils.isEqualIgnoreCase(cost.getCostCode(), FclBlConstants.INTRAMP)) {
                accrual.setChargeCode(FclBlConstants.INTMDL);
            } else {
                accrual.setChargeCode(cost.getCostCode());
            }
            String bluescreenChargeCode = glMappingDAO.getBlueScreenChargeCode(shipmentType, accrual.getChargeCode(), "AC", "E");
            accrual.setBlueScreenChargeCode(bluescreenChargeCode);
            String glAccountNumber = glMappingDAO.dervieGlAccount(accrual.getChargeCode(), shipmentType, terminal, "E");
            accrual.setGlAccountNumber(glAccountNumber);
            accrual.setTerminal(terminal);
            accrual.setBillTo(YES);
            accrual.setReadyToPost(ON);
            accrual.setCurrencyCode(cost.getCurrency());
            accrual.setDescription(cost.getComments());
            accrual.setBlTerms(bl.getStreamShipBl());
            accrual.setOrgTerminal(bl.getOriginalTerminal());
            accrual.setDestination(bl.getPortofDischarge());
            accrual.setShipNo(shipperNumber);
            accrual.setShipName(shipperName);
            accrual.setConsNo(consigneeNumber);
            accrual.setConsName(consigneeName);
            accrual.setFwdNo(forwarderNumber);
            accrual.setFwdName(forwarderName);
            accrual.setThirdptyNo(thirdPartyNumber);
            accrual.setThirdptyName(thirdPartyName);
            accrual.setAgentNo(agentNumber);
            accrual.setAgentName(agentName);
            accrual.setBillingTerminal(bl.getBillingTerminal());
            accrual.setCustomerReferenceNo(bl.getExportReference());
            accrual.setVesselNo(null != bl.getVessel() ? bl.getVessel().getCode() : "");
            accrual.setCostId(cost.getId());
            accrual.setManifestFlag(YES);
            accrual.setCreatedOn(new Date());
            accrual.setCreatedBy(user.getUserId());
            accrualsDAO.save(accrual);
        }
    }

    private void createAccrualsForAcStatus(List<CostModel> costs, String fileNo) throws Exception {
        for (CostModel cost : costs) {
            if (null != accrualsDAO.getAccrualsByCostIdAndDR(cost.getId(), fileNo)) {
                continue;
            }
            TransactionLedger accrual = new TransactionLedger();
            accrual.setTransactionDate(reportingDate);
            accrual.setSailingDate(reportingDate);
            accrual.setTransactionAmt(cost.getAmount());
            accrual.setBalance(cost.getAmount());
            accrual.setBalanceInProcess(cost.getAmount());
            accrual.setTransactionType(TRANSACTION_TYPE_ACCRUALS);
            accrual.setSubledgerSourceCode(SUB_LEDGER_CODE_ACCRUALS);
            accrual.setStatus(STATUS_OPEN);
            accrual.setCustNo(cost.getVendorNumber());
            accrual.setCustName(cost.getVendorName());
            accrual.setBillLaddingNo(blNumber);
            accrual.setInvoiceNumber(cost.getInvoiceNumber());
            accrual.setBookingNo(bl.getBookingNo());
            if (dockReceipt.contains("-")) {
                accrual.setDocReceipt(StringUtils.substringBefore(dockReceipt, "-"));
            } else {
                accrual.setDocReceipt(dockReceipt);
            }
            accrual.setVoyageNo(String.valueOf(bl.getVoyages()));
            accrual.setContainerNo(containerNumber);
            accrual.setMasterBl(bl.getStreamShipBl());
            accrual.setSubHouseBl(bl.getHouseBl());
            accrual.setShipmentType(shipmentType);
            if (CommonUtils.isEqualIgnoreCase(cost.getCostCode(), FclBlConstants.INTRAMP)) {
                accrual.setChargeCode(FclBlConstants.INTMDL);
            } else {
                accrual.setChargeCode(cost.getCostCode());
            }
            String bluescreenChargeCode = glMappingDAO.getBlueScreenChargeCode(shipmentType, accrual.getChargeCode(), "AC", "E");
            accrual.setBlueScreenChargeCode(bluescreenChargeCode);
            String glAccountNumber = glMappingDAO.dervieGlAccount(accrual.getChargeCode(), shipmentType, terminal, "E");
            accrual.setGlAccountNumber(glAccountNumber);
            accrual.setTerminal(terminal);
            accrual.setBillTo(YES);
            accrual.setReadyToPost(ON);
            accrual.setCurrencyCode(cost.getCurrency());
            accrual.setDescription(cost.getComments());
            accrual.setBlTerms(bl.getStreamShipBl());
            accrual.setOrgTerminal(bl.getOriginalTerminal());
            accrual.setDestination(bl.getPortofDischarge());
            accrual.setShipNo(shipperNumber);
            accrual.setShipName(shipperName);
            accrual.setConsNo(consigneeNumber);
            accrual.setConsName(consigneeName);
            accrual.setFwdNo(forwarderNumber);
            accrual.setFwdName(forwarderName);
            accrual.setThirdptyNo(thirdPartyNumber);
            accrual.setThirdptyName(thirdPartyName);
            accrual.setAgentNo(agentNumber);
            accrual.setAgentName(agentName);
            accrual.setBillingTerminal(bl.getBillingTerminal());
            accrual.setCustomerReferenceNo(bl.getExportReference());
            accrual.setVesselNo(null != bl.getVessel() ? bl.getVessel().getCode() : "");
            accrual.setCostId(cost.getId());
            accrual.setManifestFlag(YES);
            accrual.setCreatedOn(new Date());
            accrual.setCreatedBy(user.getUserId());
            accrualsDAO.save(accrual);
        }
    }

    private void setTradingPartners() {

        if (bl.getFileType().equalsIgnoreCase("I") && CommonUtils.isNotEmpty(bl.getHouseShipper())) {
            shipperNumber = bl.getHouseShipper();
            shipperName = tradingPartnerDAO.getAccountName(shipperNumber);
        } else {
            if (CommonUtils.isNotEmpty(bl.getShipperNo())) {
                shipperNumber = bl.getShipperNo();
                shipperName = tradingPartnerDAO.getAccountName(shipperNumber);
            }
        }
        if (CommonUtils.isNotEmpty(bl.getForwardAgentNo())) {
            forwarderNumber = bl.getForwardAgentNo();
            forwarderName = tradingPartnerDAO.getAccountName(forwarderNumber);
        }
        if (CommonUtils.isNotEmpty(bl.getConsigneeNo())) {
            consigneeNumber = bl.getConsigneeNo();
            consigneeName = tradingPartnerDAO.getAccountName(consigneeNumber);
        }
        if (CommonUtils.isNotEmpty(bl.getBillTrdPrty())) {
            thirdPartyNumber = bl.getBillTrdPrty();
            thirdPartyName = tradingPartnerDAO.getAccountName(thirdPartyNumber);
        }
        if (CommonUtils.isNotEmpty(bl.getAgentNo())) {
            agentNumber = bl.getAgentNo();
            agentName = tradingPartnerDAO.getAccountName(agentNumber);
        }
        if (CommonUtils.isNotEmpty(bl.getNotifyParty())) {
            notifyPartyNumber = bl.getNotifyParty();
            notifyPartyName = tradingPartnerDAO.getAccountName(notifyPartyNumber);
        }
    }

    private void setCommonFields() throws Exception {
        Set<String> sealNumbers = new HashSet<String>();
        Set<String> containerNumbers = new HashSet<String>();
        if (CommonUtils.isNotEmpty(bl.getFclcontainer())) {
            for (FclBlContainer container : (Set<FclBlContainer>) bl.getFclcontainer()) {
                if (CommonUtils.isNotEmpty(container.getSealNo())) {
                    sealNumbers.add(container.getSealNo());
                }
                if (CommonUtils.isNotEmpty(container.getTrailerNo())) {
                    containerNumbers.add(container.getTrailerNo());
                }
            }
        }
        sealNumber = sealNumbers.toString().replace("[", "").replace("]", "");
        containerNumber = containerNumbers.toString().replace("[", "").replace("]", "");
        terminal = bl.getBillingTerminal().substring(bl.getBillingTerminal().lastIndexOf("-") + 1);
        if (CommonUtils.isEqualIgnoreCase(bl.getImportFlag(), "I")) {
            shipmentType = "FCLI";
            reportingDate = bl.getEta();
        } else {
            shipmentType = "FCLE";
            reportingDate = bl.getSailDate();
        }
        postedDate = new AccrualsDAO().getPostedDate(reportingDate);
    }

    public void postAccrualsBeforeManifest() throws Exception {
        dockReceipt = bl.getFileNo();
        blNumber = bl.getBolId();
        setCommonFields();
        setTradingPartners();
        List<CostModel> costs = getCosts();
        if (CommonUtils.isNotEmpty(costs)) {
            createAccruals(costs);
            new NotesBC().saveNotes(bl.getFileNo(), user.getLoginName(), "Accruals Posted Before Manifesting");
            if (bl.getFclblcostcodes() != null) {
                Iterator iter = bl.getFclblcostcodes().iterator();
                while (iter.hasNext()) {
                    FclBlCostCodes cost = (FclBlCostCodes) iter.next();
                    if (CommonUtils.isEmpty(cost.getTransactionType())) {
                        cost.setReadyToPost("M");
                        cost.setTransactionType("AC");
                        cost.setManifestModifyFlag("yes");
                    }
                }
            }
            getCurrentSession().flush();
            getCurrentSession().clear();
        }
        List<CostModel> costModels = getCostsWithAcStatus();
        if (CommonUtils.isNotEmpty(costModels)) {
            createAccrualsForAcStatus(costModels, bl.getFileNo());
        }
        getCurrentSession().flush();
        getCurrentSession().clear();
    }

    public void postAccrualsForContainerUpdation() throws Exception {
        dockReceipt = bl.getFileNo();
        blNumber = bl.getBolId();
        setCommonFields();
        setTradingPartners();
        List<CostModel> costs = getCosts();
        if (CommonUtils.isNotEmpty(costs)) {
            createAccruals(costs);
            if (bl.getFclblcostcodes() != null) {
                Iterator iter = bl.getFclblcostcodes().iterator();
                while (iter.hasNext()) {
                    FclBlCostCodes cost = (FclBlCostCodes) iter.next();
                    if (CommonUtils.isEmpty(cost.getTransactionType())) {
                        cost.setReadyToPost("M");
                        cost.setTransactionType("AC");
                        cost.setManifestModifyFlag("yes");
                        fclBlCostCodesDAO.attachDirty(cost);
                    }
                }
            }
            getCurrentSession().flush();
            getCurrentSession().clear();
        }
    }

    public void postAccruals(List<CostModel> costs) throws Exception {
        if (CommonUtils.isNotEmpty(costs)) {
            dockReceipt = bl.getFileNo();
            blNumber = bl.getBolId();
            setCommonFields();
            setTradingPartners();
            createAccruals(costs);
        }
    }

    public void manifest() throws Exception {
        isManifested = true;
        historyType = "FCL BL";
        manifestFlag = YES;
        correctionFlag = NO;
        dockReceipt = bl.getFileNo();
        blNumber = bl.getBolId();
        setCommonFields();
        setTradingPartners();
        subledger = TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "-" + shipmentType;
        List<ChargeModel> charges = getCharges();
        List<CostModel> costs = getCosts();
        if (CommonUtils.isNotEmpty(charges)) {
            createArSubledgers(charges);
            createArInvoice();
        }
        if (CommonUtils.isNotEmpty(costs)) {
            createAccruals(costs);
        }
    }

    public void deleteCorrections() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("update");
        queryBuilder.append("  fclblcorrections c ");
        queryBuilder.append("set");
        queryBuilder.append("  c.status = '").append(FclBlConstants.DISABLE).append("',");
        queryBuilder.append("  c.manifest = null,");
        queryBuilder.append("  c.who_posted = '").append(user.getLoginName()).append("',");
        queryBuilder.append("  c.posted_date = now() ");
        queryBuilder.append("where");
        queryBuilder.append("  bl_number = '").append(blNumber).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();

        queryBuilder.delete(0, queryBuilder.length());
        queryBuilder.append("delete");
        queryBuilder.append("  from credit_debit_note ");
        queryBuilder.append("where");
        queryBuilder.append("  bolid = '").append(blNumber).append("'");
        getCurrentSession().createSQLQuery(queryBuilder.toString()).executeUpdate();
    }

    public void unmanifest() throws Exception {
        isManifested = false;
        isCorrected = false;
        historyType = "FCL VOID";
        manifestFlag = NO;
        correctionFlag = NO;
        dockReceipt = bl.getFileNo();
        blNumber = bl.getBolId();
        setCommonFields();
        setTradingPartners();
        List<TransactionLedger> subledgers = getManifestedSubledgers();
        if (CommonUtils.isNotEmpty(subledgers)) {
            reverseArSubledgers(subledgers);
            createArInvoice();
        }
        FclBlDAO fclBlDAO = new FclBlDAO();
        List<FclBl> correctedBls = fclBlDAO.getAllBls(blNumber + FclBlConstants.DELIMITER);
        if (CommonUtils.isNotEmpty(correctedBls)) {
            for (FclBl correctedBl : correctedBls) {
                fclBlDAO.delete(correctedBl);
            }
        }
        deleteCorrections();
        accrualsDAO.deleteAccruals(blId);
    }

    public void postCorrection() throws Exception {
        isManifested = true;
        isCorrected = true;
        historyType = "FCL CN";
        manifestFlag = YES;
        correctionFlag = YES;
        dockReceipt = bl.getFileNo();
        blNumber = bl.getBolId().substring(0, bl.getBolId().indexOf("=="));
        cnNumber = bl.getBolId().substring(bl.getBolId().indexOf("==") + 2);
        setCommonFields();
        subledger = TRANSACTION_TYPE_ACCOUNT_RECEIVABLE + "-CN";
        List<ChargeModel> corrections = getCorrections();
        ChargeModel correction = corrections.get(0);
        if (CommonUtils.isEmpty(correction.getChargeCode())) {
            Integer correctedBlId = blId;
            getPreviousBlFields();
            List<ChargeModel> fromCharges = getCharges();
            createArSubledgers(fromCharges);
            blId = correctedBlId;
            setTradingPartners();
            List<ChargeModel> toCharges = getCharges();
            createArSubledgers(toCharges);
        } else {
            setTradingPartners();
            createArSubledgers(corrections);
        }
        createArInvoice();
    }
}
