package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.logiware.bean.ReportBean;
import com.logiware.form.GlReportsForm;
import java.util.Calendar;
import java.util.List;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Lakshmi Naryanan
 */
public class GlReportsDAO extends BaseHibernateDAO implements ConstantsInterface {

    private String buildChargeCodeQuery(GlReportsForm glReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select tp.acct_name as customerName,");
        queryBuilder.append("tp.acct_no as customerNumber,");
        queryBuilder.append("upper(if(tl.subledger_source_code like 'AR-%',tl.bill_ladding_no,tl.invoice_number)) as invoiceNumber,");
        queryBuilder.append("if(tl.bill_ladding_no!='',tl.bill_ladding_no,'') as blNumber,");
        queryBuilder.append("if(tl.voyage_no!='' and tl.voyage_no!='00000',tl.voyage_no,'') as voyageNumber,");
        queryBuilder.append("if(tl.drcpt!='',tl.drcpt,'') as dockReceipt,");
        queryBuilder.append("if(tl.created_on!='',date_format(tl.created_on,'%m/%d/%Y'),'') as createdDate,");
        queryBuilder.append("date_format(tl.sailing_date,'%m/%d/%Y') as reportingDate,");
        queryBuilder.append("date_format(tl.posted_date,'%m/%d/%Y') as postedDate,");
        queryBuilder.append("format(if(tl.subledger_source_code like 'AR-%',transaction_amt,0),2) as revenue,");
        queryBuilder.append("format(if(tl.subledger_source_code in ('PJ','ACC'),transaction_amt,0),2) as cost,");
        queryBuilder.append("upper(ud.login_name) as createdBy,");
        queryBuilder.append("tl.subledger_source_code as type");
        queryBuilder.append(" from transaction_ledger tl");
        queryBuilder.append(" join trading_partner tp on tl.cust_no=tp.acct_no");
        queryBuilder.append(" left join user_details ud on tl.created_by=ud.user_id");
        queryBuilder.append(" where (tl.subledger_source_code like 'AR-%'");
        queryBuilder.append(" or (tl.subledger_source_code='ACC' and tl.status!='AS') or tl.subledger_source_code='PJ')");
        queryBuilder.append(" and tl.charge_code='").append(glReportsForm.getChargeCode()).append("'");
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(glReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(glReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        queryBuilder.append(" and tl.transaction_date between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append(" order by tl.transaction_date");
        return queryBuilder.toString();
    }

    public List<ReportBean> getChargeCodes(GlReportsForm glReportsForm) throws Exception {
        String query = buildChargeCodeQuery(glReportsForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    private String buildGlCodeQuery(GlReportsForm glReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        if (glReportsForm.isShowPosted()) {
            queryBuilder.append("select ad.account as account,");
            queryBuilder.append("if(tp.acct_name!='',tp.acct_name,'') as customerName,");
            queryBuilder.append("if(tp.acct_no!='',tp.acct_no,'') as customerNumber,");
            queryBuilder.append("upper(if(tl.subledger_source_code like 'AR-%',tl.bill_ladding_no,tl.invoice_number)) as invoiceNumber,");
            queryBuilder.append("if(tl.bill_ladding_no!='',tl.bill_ladding_no,'') as blNumber,");
            queryBuilder.append("if(tl.voyage_no!='' and tl.voyage_no!='00000',tl.voyage_no,'') as voyageNumber,");
            queryBuilder.append("if(tl.drcpt!='',tl.drcpt,'') as dockReceipt,");
            queryBuilder.append("if(tl.Transaction_date!='' or je.Je_Date!='',");
            queryBuilder.append("date_format(if(tl.Transaction_date!='',tl.Transaction_date,je.Je_Date),'%m/%d/%Y'),'') as createdDate,");
            queryBuilder.append("if(tl.sailing_date!='' or tl.Transaction_date!='',");
            queryBuilder.append("date_format(if(tl.sailing_date!='',tl.sailing_date,tl.Transaction_date),'%m/%d/%Y'),'') as reportingDate,");
            queryBuilder.append("if(tl.posted_date!='' or tl.Transaction_date!='',");
            queryBuilder.append("date_format(if(tl.posted_date!='',tl.posted_date,tl.Transaction_date),'%m/%d/%Y'),je.period) as postedDate,");
            queryBuilder.append("format(if(tlh.amount is null,(li.debit-li.credit),if(tlh.subledger like 'AR-%',-tlh.amount,tlh.amount)),2) as amount,");
            queryBuilder.append("upper(if(ud.login_name!='',ud.login_name,'')) as createdBy,");
            queryBuilder.append("upper(if(tlh.subledger!='',tlh.subledger,je.journal_entry_desc)) as type,");
            queryBuilder.append("je.Journal_Entry_Id as jeBatch");
            queryBuilder.append(" from account_details ad");
            queryBuilder.append(" join line_item li on li.Account=ad.Account");
            queryBuilder.append(" join journal_entry je on je.Journal_Entry_Id=li.Journal_Entry_Id");
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(glReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyyMM");
            String toDate = DateUtils.formatDate(DateUtils.parseDate(glReportsForm.getToDate(), "MM/dd/yyyy"), "yyyyMM");
            queryBuilder.append(" and je.Period between '").append(fromDate).append("' and '").append(toDate).append("'");
            queryBuilder.append(" join batch b on b.batch_id=je.Journal_Entry_Id");
            queryBuilder.append(" left join transaction_ledger_history tlh on tlh.line_item_id=li.Line_Item_Id and tlh.gl_account=ad.Account");
            queryBuilder.append(" left join transaction_ledger tl on tl.transaction_id=tlh.transaction_ledger_id");
            queryBuilder.append(" left join trading_partner tp on tp.acct_no=tlh.cust_no");
            queryBuilder.append(" left join user_details ud on tl.Created_By=ud.user_id");
            if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "ecuMapping")) {
                queryBuilder.append(" where ad.report_category='").append(glReportsForm.getReportCategory()).append("'");
            }else{
                if (glReportsForm.getGlAccount().endsWith("ALL")) {
                    queryBuilder.append(" where ad.Account like '").append(glReportsForm.getGlAccount().replace("ALL", "")).append("%'");
                } else {
                    queryBuilder.append(" where ad.Account='").append(glReportsForm.getGlAccount()).append("'");
                }
            }
            queryBuilder.append(" order by je.Journal_Entry_Id");
        } else {
            queryBuilder.append("select ad.account as account,");
            queryBuilder.append("if(tp.acct_name!='',tp.acct_name,'') as customerName,");
            queryBuilder.append("if(tp.acct_no!='',tp.acct_no,'') as customerNumber,");
            queryBuilder.append("upper(if(tl.subledger_source_code like 'AR-%',tl.bill_ladding_no,tl.invoice_number)) as invoiceNumber,");
            queryBuilder.append("if(tl.bill_ladding_no!='',tl.bill_ladding_no,'') as blNumber,");
            queryBuilder.append("if(tl.voyage_no!='' and tl.voyage_no!='00000',tl.voyage_no,'') as voyageNumber,");
            queryBuilder.append("if(tl.drcpt!='',tl.drcpt,'') as dockReceipt,");
            queryBuilder.append("if(tl.Created_On!='',date_format(tl.Created_On,'%m/%d/%Y'),'') as createdDate,");
            queryBuilder.append("if(tl.sailing_date!='' or tl.Transaction_date!='',");
            queryBuilder.append("date_format(if(tl.sailing_date!='',tl.sailing_date,tl.Transaction_date),'%m/%d/%Y'),'') as reportingDate,");
            queryBuilder.append("if(tl.posted_date!='' or tl.Transaction_date!='',");
            queryBuilder.append("date_format(if(tl.posted_date!='',tl.posted_date,tl.Transaction_date),'%m/%d/%Y'),'') as postedDate,");
            queryBuilder.append("format(if(tl.Subledger_Source_code like 'AR-%',-tl.Transaction_amt,tl.Transaction_amt),2) as amount,");
            queryBuilder.append("upper(if(ud.login_name!='',ud.login_name,'')) as createdBy,");
            queryBuilder.append("upper(tl.Subledger_Source_code) as type");
            queryBuilder.append(" from account_details ad");
            queryBuilder.append(" join transaction_ledger tl on ad.Account=tl.GL_account_number");
            queryBuilder.append(" and tl.Subledger_Source_code!='' and (tl.Status='Open' or tl.Status='PY')");
            String fromDate = DateUtils.formatDate(DateUtils.parseDate(glReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
            String toDate = DateUtils.formatDate(DateUtils.parseDate(glReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
            queryBuilder.append(" and tl.transaction_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            queryBuilder.append(" left join trading_partner tp on tp.acct_no=tl.cust_no");
            queryBuilder.append(" left join user_details ud on tl.Created_By=ud.user_id");
            if (CommonUtils.isEqualIgnoreCase(glReportsForm.getTabName(), "ecuMapping")) {
                queryBuilder.append(" where ad.report_category='").append(glReportsForm.getReportCategory()).append("'");
            }else{
                if (glReportsForm.getGlAccount().endsWith("ALL")) {
                    queryBuilder.append(" where ad.Account like '").append(glReportsForm.getGlAccount().replace("ALL", "")).append("%'");
                } else {
                    queryBuilder.append(" where ad.Account='").append(glReportsForm.getGlAccount()).append("'");
                }
            }
            queryBuilder.append(" order by tl.transaction_date");
        }
        return queryBuilder.toString();
    }

    public List<ReportBean> getGlCodes(GlReportsForm glReportsForm) throws Exception {
        String query = buildGlCodeQuery(glReportsForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    private String buildCashAccountsQuery(String reportingDate) throws Exception {
        String date = DateUtils.formatDate(DateUtils.parseDate(reportingDate, "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        String cashReportAccount = LoadLogisoftProperties.getProperty("cash.report.account");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select cash.gl_account_no as glAccountNo,");
        queryBuilder.append("upper(cash.acct_name) as accountName,");
        queryBuilder.append("cash.last_reconciled_date as lastReconciledDate,");
        queryBuilder.append("cash.bank_balance as bankBalance");
        queryBuilder.append(" from (");
        queryBuilder.append("(select bank.gl_account_no as gl_account_no,");
        queryBuilder.append("bank.acct_name as acct_name,");
        queryBuilder.append("date_format(bank.reconcile_date,'%m/%d/%Y') as last_reconciled_date,");
        queryBuilder.append("format(bank.bank_balance,2) as bank_balance");
        queryBuilder.append(" from (");
        queryBuilder.append("select bank.gl_account_no as gl_account_no,");
        queryBuilder.append("bank.acct_name as acct_name,");
        queryBuilder.append("reconcile.reconcile_date as reconcile_date,");
        queryBuilder.append("reconcile.balance as bank_balance,");
        queryBuilder.append("bank.id as id");
        queryBuilder.append(" from bank_details bank");
        queryBuilder.append(" left join reconcile_log reconcile");
        queryBuilder.append(" on (bank.id = reconcile.bank_id");
        queryBuilder.append(" and reconcile.reconcile_date <= '").append(date).append("')");
        queryBuilder.append(" where bank.gl_account_no != '03-1116-01'");
        queryBuilder.append(" order by bank.gl_account_no,reconcile.reconcile_date desc");
        queryBuilder.append(") as bank");
        queryBuilder.append(" group by bank.id");
        queryBuilder.append(")");
        queryBuilder.append(" union ");
        queryBuilder.append("(select account.account as gl_account_no,");
        queryBuilder.append("account.acct_desc as acct_name,");
        queryBuilder.append("null as last_reconciled_date,");
        queryBuilder.append("'0.00' as bank_balance");
        queryBuilder.append(" from account_details account");
        queryBuilder.append(" where account.account = '").append(cashReportAccount).append("')");
        queryBuilder.append(") as cash");
        queryBuilder.append(" order by cash.acct_name");
        return queryBuilder.toString();
    }

    public List<ReportBean> getCashAccounts(GlReportsForm glReportsForm) throws Exception {
        Calendar reportingDate = Calendar.getInstance();
        reportingDate.setTime(DateUtils.parseDate(glReportsForm.getReportingDate(), "MM/dd/yyyy"));
        Integer reportingPeriod = reportingDate.get(Calendar.MONTH) + 1;
        String period = DateUtils.formatDate(reportingDate.getTime(), "yyyyMM");
        FiscalPeriod fiscalPeriod = new FiscalPeriodDAO().getLastClosedPeriod(period);
        Integer lastClosedPeriod = Integer.parseInt(fiscalPeriod.getPeriod());
        Integer lastClosedYear = fiscalPeriod.getYear();
        String query = buildCashAccountsQuery(glReportsForm.getReportingDate());
        List<ReportBean> cashAccounts = getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
        Calendar lastClosedDate = Calendar.getInstance();
        lastClosedDate.set(lastClosedYear, lastClosedPeriod, 1);
        String startPeriod = DateUtils.formatDate(lastClosedDate.getTime(), "yyyyMM");
        String endPeriod = DateUtils.formatDate(reportingDate.getTime(), "yyyyMM");
        String startDate = DateUtils.formatDate(lastClosedDate.getTime(), "yyyy-MM-dd 00:00:00");
        String endDate = DateUtils.formatDate(reportingDate.getTime(), "yyyy-MM-dd 23:59:59");
        ReconcileDAO reconcileDAO = new ReconcileDAO();
        AccountBalanceDAO accountBalanceDAO = new AccountBalanceDAO();
        if (CommonUtils.isNotEqual(reportingPeriod, lastClosedPeriod)) {
            for (ReportBean cashAccount : cashAccounts) {
                double glBalance = accountBalanceDAO.getGLBalance(cashAccount.getGlAccountNo(), fiscalPeriod.getPeriodDis());
                glBalance += reconcileDAO.getGlBalanceInJE(cashAccount.getGlAccountNo(), startPeriod, endPeriod);
                glBalance += reconcileDAO.getGlBalanceInSubledger(cashAccount.getGlAccountNo(), startDate, endDate);
                cashAccount.setGlBalance(NumberUtils.formatNumber(glBalance));
            }
        }else{
            for (ReportBean cashAccount : cashAccounts) {
                double glBalance = accountBalanceDAO.getGLBalance(cashAccount.getGlAccountNo(), fiscalPeriod.getPeriodDis());
                cashAccount.setGlBalance(NumberUtils.formatNumber(glBalance));
            }
        }
        return cashAccounts;
    }

    private String buildFclPlQuery(GlReportsForm glReportsForm) throws Exception {
        String fromDate = DateUtils.formatDate(DateUtils.parseDate(glReportsForm.getFromDate(), "MM/dd/yyyy"), "yyyy-MM-dd 00:00:00");
        String toDate = DateUtils.formatDate(DateUtils.parseDate(glReportsForm.getToDate(), "MM/dd/yyyy"), "yyyy-MM-dd 23:59:59");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select file as file,");
        queryBuilder.append("date_format(reporting_date,'%m/%d/%Y') as reportingDate,");
        queryBuilder.append("format(sum(sales),2) as sales,");
        queryBuilder.append("format(sum(costs),2) as costs,");
        queryBuilder.append("format(sum(sales+costs),2) as profits,");
        queryBuilder.append("format(sum(ocnfrt_revenue),2) as oceanfreightRevenue,");
        queryBuilder.append("format(sum(ocnfrt_deferral),2) as oceanfreightDeferral,");
        queryBuilder.append("format(sum(ocnfrt_cost),2) as oceanfreightCost,");
        queryBuilder.append("format(sum(ocnfrt_ovrsold),2) as oceanfreightOversold,");
        queryBuilder.append("format(sum(dray_revenue),2) as drayRevenue,");
        queryBuilder.append("format(sum(dray_cost),2) as drayCost,");
        queryBuilder.append("format(sum(warehouse_revenue),2) as warehouseRevenue,");
        queryBuilder.append("format(sum(warehouse_cost),2) as warehouseCost,");
        queryBuilder.append("format(sum(document_revenue),2) as documentRevenue,");
        queryBuilder.append("format(sum(ffcom_cost),2) as ffcomCost,");
        queryBuilder.append("format(sum(fae_cost),2) as faeCost,");
        queryBuilder.append("format(sum(pass_revenue),2) as passRevenue,");
        queryBuilder.append("format(sum(pass_cost),2) as passCost,");
        queryBuilder.append("format(sum(other_revenue),2) as otherRevenue,");
        queryBuilder.append("format(sum(other_cost),2) as otherCost");
        queryBuilder.append(" from (");

        StringBuilder filters = new StringBuilder();
        if (CommonUtils.isNotEmpty(glReportsForm.getFileType())) {
            filters.append(" and file_type = '").append(glReportsForm.getFileType()).append("'");
        }
        if (CommonUtils.isNotEmpty(glReportsForm.getIssuingTerminal())) {
            filters.append(" and issuing_terminal = '").append(glReportsForm.getIssuingTerminal()).append("'");
        }
        if (CommonUtils.isNotEmpty(glReportsForm.getPod())) {
            filters.append(" and pod = '").append(glReportsForm.getPod().replaceAll("'", "\\'")).append("'");
        }
        if (CommonUtils.isEqualIgnoreCase(glReportsForm.getReportType(), "Show only items posted in the current period")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(glReportsForm.getToDate(), "MM/dd/yyyy"));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            String startDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd 00:00:00");
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd 23:59:59");
            queryBuilder.append("(select * from fcl_pl_acc_subledger_view");
            queryBuilder.append(" where (status in ('Open','Pending','EDI In Progress','EDI Dispute','Dispute','I')");
            queryBuilder.append(" or (status in ('AS','EDI Assigned') and posted_date > '").append(toDate).append("'))");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(" and reporting_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select * from fcl_pl_pj_subledger_view");
            queryBuilder.append(" where posted_date between '").append(startDate).append("' and '").append(endDate).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select * from fcl_pl_ar_subledger_view");
            queryBuilder.append(" where posted_date between '").append(startDate).append("' and '").append(endDate).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select * from fcl_pl_ar_inv_subledger_view");
            queryBuilder.append(" where posted_date between '").append(startDate).append("' and '").append(endDate).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
        } else if (CommonUtils.isEqualIgnoreCase(glReportsForm.getReportType(), "Show all items regardless of period")) {
            queryBuilder.append("(select * from fcl_pl_acc_subledger_view");
            queryBuilder.append(" where reporting_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select * from fcl_pl_ar_subledger_view");
            queryBuilder.append(" where reporting_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select * from fcl_pl_ar_inv_subledger_view");
            queryBuilder.append(" where reporting_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
        } else {
            queryBuilder.append("(select * from fcl_pl_acc_subledger_gl_view");
            queryBuilder.append(" where posted_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            queryBuilder.append(" and gl_period = '").append(glReportsForm.getGlPeriod()).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select * from fcl_pl_pj_subledger_gl_view");
            queryBuilder.append(" where posted_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            queryBuilder.append(" and gl_period = '").append(glReportsForm.getGlPeriod()).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select * from fcl_pl_ar_subledger_gl_view");
            queryBuilder.append(" where posted_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            queryBuilder.append(" and gl_period = '").append(glReportsForm.getGlPeriod()).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
            queryBuilder.append(" union ");
            queryBuilder.append("(select * from fcl_pl_ar_inv_subledger_gl_view");
            queryBuilder.append(" where posted_date between '").append(fromDate).append("' and '").append(toDate).append("'");
            queryBuilder.append(" and gl_period = '").append(glReportsForm.getGlPeriod()).append("'");
            if (CommonUtils.isNotEmpty(filters)) {
                queryBuilder.append(filters);
            }
            queryBuilder.append(")");
        }
        queryBuilder.append(" ) as tl");
        queryBuilder.append(" group by file");
        queryBuilder.append(" order by file");
        return queryBuilder.toString();
    }

    public List<ReportBean> getFclPlFiles(GlReportsForm glReportsForm) throws Exception {
        String query = buildFclPlQuery(glReportsForm);
        return getCurrentSession().createSQLQuery(query).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }

    public List<ReportBean> getEcuMappings(GlReportsForm glReportsForm) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select");
        queryBuilder.append("  account as glAccountNo,");
        queryBuilder.append("  acct_desc as description,");
        queryBuilder.append("  report_category as reportCategory ");
        queryBuilder.append("from");
        queryBuilder.append("  account_details");
        return getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(ReportBean.class)).list();
    }
}
