package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.jobscheduler.Fax;
import com.gp.cvst.logisoft.AccountingConstants;
import com.gp.cvst.logisoft.struts.form.CustomerStatementForm;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ArStatementDAO extends BaseHibernateDAO implements ConstantsInterface {

    public List<CustomerBean> getAccountsForArStatement(String scheduleStmt) {
	List<CustomerBean> accounts = new ArrayList<CustomerBean>();
	StringBuilder queryBuilder = new StringBuilder("select tp.acct_name,tp.acct_no,tp.acct_type,");
	queryBuilder.append("concat(if(cust_addr.address1!='',concat(cust_addr.address1,'\n'),''),");
	queryBuilder.append("if(cust_addr.city1!='' && cust_addr.state!='',concat(cust_addr.city1,', ',cust_addr.state),");
	queryBuilder.append("if(cust_addr.city1!='',cust_addr.city1,if(cust_addr.state!='',cust_addr.state,''))),");
	queryBuilder.append("if(cust_addr.zip!='',concat(' ',cust_addr.zip),'')) as address,");
	queryBuilder.append("ca.ar_phone,ca.acct_rec_email,ca.ar_fax,");
	queryBuilder.append("if(ca.credit_balance='Y','Y','N'),if(ca.credit_invoice='Y','Y','N'),");
	queryBuilder.append("concat(ud.first_name,' ',ud.last_name) as collector,ud.email,ud.fax,gc.codedesc as statementType,");
	queryBuilder.append(" ca.statement_type as statementPdfOrExcel");
	queryBuilder.append(" from cust_accounting ca");
	queryBuilder.append(" join trading_partner tp on (tp.acct_no=ca.acct_no and tp.type!='master')");
	queryBuilder.append(" left join cust_address cust_addr on ca.cust_address_id=cust_addr.id");
	queryBuilder.append(" join genericcode_dup gc on (gc.id=ca.statements and (gc.codedesc='Email' or gc.codedesc='Fax'))");
	queryBuilder.append(" join user_details ud on ud.user_id=ca.ar_contact_code");
	queryBuilder.append(" where (ca.acct_rec_email!='' or ca.ar_fax!='')");
	queryBuilder.append(" and (ca.schedule_stmt='Both' or ").append(scheduleStmt).append(")");
	queryBuilder.append(" group by tp.acct_no");
	queryBuilder.append(" order by ca.acct_no asc");
	List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	for (Object row : result) {
	    Object[] col = (Object[]) row;
	    CustomerBean account = new CustomerBean();
	    account.setCustomerName((String) col[0]);
	    account.setCustomerNumber((String) col[1]);
	    account.setAccountType((String) col[2]);
	    account.setAddress((String) col[3]);
	    account.setPhone((String) col[4]);
	    account.setEmail((String) col[5]);
	    account.setFax((String) col[6]);
	    account.setCreditBalance(col[7].toString());
	    account.setCreditInvoice(col[8].toString());
	    account.setCollector((String) col[9]);
	    account.setCollectorEmail((String) col[10]);
	    account.setCollectorFax((String) col[11]);
	    account.setStatementType((String) col[12]);
	    account.setStatementPdfOrExcel((String) col[13]);
	    accounts.add(account);
	}
	return accounts;
    }

    public List<CustomerBean> getAccountsConfiguredForStatements() throws Exception {
	String query = "select cast(date_format(max(m.email_date),'%Y-%m-%d') as char) from mail_transactions m where m.module_name='AR Statement'";
	String lastStatementDate = (String) getCurrentSession().createSQLQuery(query).uniqueResult();
	EmailschedulerDAO emailschedulerDAO = new EmailschedulerDAO();
	List<EmailSchedulerVO> faxes = emailschedulerDAO.getFaxes("AR Statement", "Completed", lastStatementDate);
	for (EmailSchedulerVO fax : faxes) {
	    fax.setStatus(Fax.checkStatus(fax.getResponseCode()));
	    emailschedulerDAO.update(fax);
	}
	getCurrentSession().flush();
	List<CustomerBean> accounts = new ArrayList<CustomerBean>();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select tp.acct_name,tp.acct_no,ca.address1 as address,");
	queryBuilder.append("ca.city1 as city,if(ca.state!='',ca.state,'') as state,ca.zip as zip_code,tp.acct_type as acct_type,");
	queryBuilder.append("ud.login_name as collector,if(isnull(ge.codedesc),'No',ge.codedesc) as statement_type,ca.schedule_stmt as statement_frequency,");
	queryBuilder.append("if(ca.credit_balance='Y','Yes','No') as credit_balance,if(ca.credit_invoice='Y','Yes','No') as credit_invoice,");
	queryBuilder.append("ca.acct_rec_email as email,ca.ar_fax as fax,ca.ar_phone as phone,if(mt.status='Completed','Success',if(mt.status='Failed','Fail','')) as attempt,");
	queryBuilder.append("format(tr.current,2) as current,format(tr.31_60days,2) as 31_60days,");
	queryBuilder.append("format(61_90days,2) as 61_90days,format(91days,2) as 91days,format(total,2) as total from");
	queryBuilder.append(" (select tr.cust_no,sum(if(datediff(sysdate(),tr.Transaction_date)<=30,tr.Balance,0)) as current,");
	queryBuilder.append("sum(if(datediff(sysdate(),tr.Transaction_date)>=31 and datediff(sysdate(),tr.Transaction_date)<=60,tr.Balance,0)) as 31_60days,");
	queryBuilder.append("sum(if(datediff(sysdate(),tr.Transaction_date)>=61 and datediff(sysdate(),tr.Transaction_date)<=90,tr.Balance,0)) as 61_90days,");
	queryBuilder.append("sum(if(datediff(sysdate(),tr.Transaction_date)>=91,tr.Balance,0)) as 91days,sum(tr.Balance) as total");
	queryBuilder.append("  from transaction tr where  tr.cust_no!=''");
	queryBuilder.append(" and tr.Transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	queryBuilder.append("group by tr.cust_no) as tr ");
	queryBuilder.append(" join trading_partner tp on (tr.cust_no=tp.acct_no)");
	queryBuilder.append(" join cust_accounting ca on (ca.acct_no=tp.acct_no)");
	queryBuilder.append(" left join user_details ud on (ud.user_id=ca.ar_contact_code)");
	queryBuilder.append(" left join genericcode_dup ge on (ge.id=ca.statements)");
	queryBuilder.append(" left join mail_transactions mt on (mt.module_name='AR Statement' and mt.module_id=tp.acct_no");
	queryBuilder.append(" and date_format(mt.email_date,'%Y-%m-%d')>='").append(lastStatementDate).append("')");
	queryBuilder.append(" where tr.total<>0");
	List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	for (Object row : result) {
	    Object[] col = (Object[]) row;
	    CustomerBean account = new CustomerBean();
	    account.setCustomerName((String) col[0]);
	    account.setCustomerNumber((String) col[1]);
	    account.setAddress((String) col[2]);
	    account.setCity((String) col[3]);
	    account.setState((String) col[4]);
	    account.setZipCode((String) col[5]);
	    account.setAccountType((String) col[6]);
	    account.setCollector((String) col[7]);
	    account.setStatementType((String) col[8]);
	    account.setStatementFrequency((String) col[9]);
	    account.setCreditBalance((String) col[10]);
	    account.setCreditInvoice((String) col[11]);
	    account.setEmail((String) col[12]);
	    account.setFax((String) col[13]);
	    account.setPhone((String) col[14]);
	    account.setAttempt((String) col[15]);
	    account.setCurrent((String) col[16]);
	    account.setThirtyOneToSixtyDays((String) col[17]);
	    account.setSixtyOneToNintyDays((String) col[18]);
	    account.setGreaterThanNintyDays((String) col[19]);
	    account.setTotal((String) col[20]);
	    accounts.add(account);
	}
	return accounts;
    }

    public String buildArQuery(CustomerBean customerBean) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select ar.invoice_date as invoice_date,");
	queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
	queryBuilder.append("ar.booking_number as booking_number,");
	queryBuilder.append("ar.customer_reference as customer_reference,");
	queryBuilder.append("ar.consignee as consignee,");
	queryBuilder.append("ar.voyage as voyage,");
	queryBuilder.append("sum(if(th.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ'),");
	queryBuilder.append("th.transaction_amount,0)) as invoice_amount,");
	queryBuilder.append("sum(if(th.transaction_type in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ'),");
	queryBuilder.append("th.transaction_amount+th.adjustment_amount,0)) as payment,");
	queryBuilder.append("sum(th.transaction_amount+th.adjustment_amount) as balance");
	queryBuilder.append(" from (");
	queryBuilder.append("select tp.acct_no as customer_number,");
	queryBuilder.append("min(tr.transaction_date) as invoice_date,");
	queryBuilder.append("trim(if(tr.bill_ladding_no!='',upper(tr.bill_ladding_no),upper(tr.invoice_number))) as invoice_or_bl,");
	queryBuilder.append("trim(tr.booking_no) as booking_number,");
	queryBuilder.append("trim(tr.customer_reference_no) as customer_reference,");
	queryBuilder.append("trim(tr.cons_name) as consignee,");
	queryBuilder.append("trim(tr.voyage_no) as voyage");
	queryBuilder.append(" from transaction tr");
	queryBuilder.append(" join trading_partner tp");
	queryBuilder.append(" on (tp.acct_no=tr.cust_no");
	queryBuilder.append(" and tp.acct_no='").append(customerBean.getCustomerNumber()).append("')");
	queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	if (CommonUtils.isEqualIgnoreCase(customerBean.getCreditInvoice(), NO)) {
	    queryBuilder.append(" and tr.Balance>0");
	} else {
	    queryBuilder.append(" and tr.Balance<>0");
	}
	queryBuilder.append(" group by tr.cust_no,invoice_or_bl");
	queryBuilder.append(" order by tr.Transaction_date");
	queryBuilder.append(") as ar");
	queryBuilder.append(" join ar_transaction_history th");
	queryBuilder.append(" on (ar.customer_number=th.customer_number and ar.invoice_or_bl=th.invoice_or_bl)");
	queryBuilder.append(" group by ar.customer_number,ar.invoice_or_bl");
	return queryBuilder.toString();
    }

    public CustomerBean getAgingBuckets(String conditions) throws Exception {
	CustomerBean agingBucket = new CustomerBean();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select format(sum(if(datediff(sysdate(),t.invoice_date)<=30,t.balance,0)),2) as 0_30days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=31");
	queryBuilder.append("  and datediff(sysdate(),t.invoice_date)<=60,t.balance,0)),2) as 31_60days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=61");
	queryBuilder.append(" and datediff(sysdate(),t.invoice_date)<=90,t.balance,0)),2) as 61_90days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=91,t.balance,0)),2) as 91days,");
	queryBuilder.append("format(sum(t.balance),2) as total");
	queryBuilder.append(" from (");
	queryBuilder.append(conditions);
	queryBuilder.append(" ) as t");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	if (null != result) {
	    Object[] col = (Object[]) result;
	    agingBucket.setCurrent((String) col[0]);
	    agingBucket.setThirtyOneToSixtyDays((String) col[1]);
	    agingBucket.setSixtyOneToNintyDays((String) col[2]);
	    agingBucket.setGreaterThanNintyDays((String) col[3]);
	    agingBucket.setTotal((String) col[4]);
	}
	return agingBucket;
    }

    public List<AccountingBean> getTransactions(String conditions) throws Exception {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select date_format(t.invoice_date,'%m/%d/%Y') as formattedDate,");
	queryBuilder.append("left(if(t.invoice_or_bl like '%04-%',");
	queryBuilder.append("replace(replace(t.invoice_or_bl,substring_index(t.invoice_or_bl,'04-',1),''),'-',''),t.invoice_or_bl),20) as invoiceOrBl,");
	queryBuilder.append("left(t.booking_number,12) as bookingNumber,");
	queryBuilder.append("left(t.customer_reference,25) as customerReference,");
	queryBuilder.append("left(t.consignee,10) as consignee,");
	queryBuilder.append("left(t.voyage,10) as voyage,");
	queryBuilder.append("format(t.invoice_amount,2) as formattedAmount,");
	queryBuilder.append("format(t.payment,2) as formattedPayment,");
	queryBuilder.append("format(t.balance,2) as formattedBalance");
	queryBuilder.append(" from (");
	queryBuilder.append(conditions);
	queryBuilder.append(" ) as t");
	queryBuilder.append(" where t.balance!=0");
	queryBuilder.append(" order by t.invoice_date");
	return getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(AccountingBean.class)).list();
    }

    public CustomerBean getCustomerDetails(String customerNumber) {
	StringBuilder queryBuilder = new StringBuilder("select tp.acct_name,tp.acct_no,tp.acct_type,");
	queryBuilder.append("concat(if(cust_addr.address1!='',concat(cust_addr.address1,'\n'),''),");
	queryBuilder.append("if(cust_addr.city1!='' && cust_addr.state!='',concat(cust_addr.city1,', ',cust_addr.state),");
	queryBuilder.append("if(cust_addr.city1e!='',cust_addr.city1,if(cust_addr.state!='',cust_addr.state,''))),");
	queryBuilder.append("if(cust_addr.zip!='',concat(' ',cust_addr.zip),'')) as address,");
	queryBuilder.append("ca.ar_phone,ca.acct_rec_email,ca.ar_fax,");
	queryBuilder.append("if(ca.credit_balance='Y','Y','N'),if(ca.credit_invoice='Y','Y','N'),");
	queryBuilder.append("concat(ud.first_name,' ',ud.last_name) as collector,ud.email");
	queryBuilder.append(" from trading_partner tp left join cust_accounting ca on ca.acct_no=tp.acct_no");
	queryBuilder.append(" left join user_details ud on ud.user_id=ca.ar_contact_code");
	queryBuilder.append(" left join cust_address cust_addr on ca.cust_address_id = cust_addr.id");
	queryBuilder.append(" where tp.acct_no = '").append(customerNumber).append("' limit 1");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	if (null != result) {
	    CustomerBean customerDetails = new CustomerBean();
	    Object[] col = (Object[]) result;
	    customerDetails.setCustomerName((String) col[0]);
	    customerDetails.setCustomerNumber((String) col[1]);
	    customerDetails.setAccountType((String) col[2]);
	    customerDetails.setAddress((String) col[3]);
	    customerDetails.setPhone((String) col[4]);
	    customerDetails.setEmail((String) col[5]);
	    customerDetails.setFax((String) col[6]);
	    customerDetails.setCreditBalance(col[7].toString());
	    customerDetails.setCreditInvoice(col[8].toString());
	    customerDetails.setCollector((String) col[9]);
	    customerDetails.setCollectorEmail((String) col[10]);
	    return customerDetails;
	}
	return null;
    }

    private String getArCustomerWithCredit() {
	StringBuilder queryBuilder = new StringBuilder("select concat(\"'\",tbl.custNo,\"'\") from (select sum(Balance) as balance,cust_no as custNo");
	queryBuilder.append(" from transaction where transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	queryBuilder.append(" group by cust_no) as tbl where tbl.balance<0");
	List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	if (CommonUtils.isNotEmpty(result)) {
	    return result.toString().replace("[", "").replace("]", "");
	}
	return null;
    }

    private String getArCustomerForCollector(String collectorId) {
	StringBuilder queryBuilder = new StringBuilder("select concat(\"'\",acct_no,\"'\") from cust_accounting");
	if (null != collectorId) {
	    queryBuilder.append(" where ar_contact_code=").append(collectorId);
	} else {
	    queryBuilder.append(" where ar_contact_code is not null");
	}
	queryBuilder.append(" order by acct_no");
	List<String> result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
	if (CommonUtils.isNotEmpty(result)) {
	    return result.toString().replace("[", "").replace("]", "");
	}
	return null;
    }

    private String buildArQuery(CustomerStatementForm customerStatementForm, String customersForCollector, String customersWithCredit, boolean isMaster) {
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("(select ar.invoice_date as invoice_date,");
	queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
	queryBuilder.append("ar.booking_number as booking_number,");
	queryBuilder.append("ar.customer_reference as customer_reference,");
	queryBuilder.append("ar.consignee as consignee,");
	queryBuilder.append("ar.voyage as voyage,");
	queryBuilder.append("sum(if(th.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ'),");
	queryBuilder.append("th.transaction_amount,0)) as invoice_amount,");
	queryBuilder.append("sum(if(th.transaction_type in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ'),");
	queryBuilder.append("th.transaction_amount+th.adjustment_amount,0)) as payment,");
	queryBuilder.append("sum(th.transaction_amount+th.adjustment_amount) as balance,");
	queryBuilder.append("'AR' as transaction_type,");
	queryBuilder.append("cast('AR_INVOICE' as char) as note_module_id,");
	queryBuilder.append("concat(ar.customer_number,'-',ar.invoice_or_bl) as note_ref_id,");
	queryBuilder.append("ar.customer_number as customer_number");
	queryBuilder.append(" from (");
	queryBuilder.append("select tp.acct_no as customer_number,");
	queryBuilder.append("min(tr.transaction_date) as invoice_date,");
	queryBuilder.append("trim(if(tr.bill_ladding_no!='',upper(tr.bill_ladding_no),upper(tr.invoice_number))) as invoice_or_bl,");
	queryBuilder.append("trim(tr.booking_no) as booking_number,");
	queryBuilder.append("trim(tr.customer_reference_no) as customer_reference,");
	queryBuilder.append("trim(tr.cons_name) as consignee,");
	queryBuilder.append("trim(tr.voyage_no) as voyage");
	queryBuilder.append(" from transaction tr");
	queryBuilder.append(" join trading_partner tp");
	queryBuilder.append(" on (tp.acct_no=tr.cust_no");
	if (customerStatementForm.isAllCustomers() || CommonUtils.isNotEmpty(customersForCollector)) {
	    if (customerStatementForm.isStmtWithCredit()) {
		queryBuilder.append(" and tp.acct_no in (").append(customersWithCredit).append(")");
	    } else {
		queryBuilder.append(" and tp.acct_no not in (").append(customersWithCredit).append(")");
	    }
	}
	if (customerStatementForm.isAllCustomers()) {
	    queryBuilder.append(" and tp.acct_no is not null");
	} else if (CommonUtils.isNotEmpty(customersForCollector)) {
	    queryBuilder.append(" and tp.acct_no in (").append(customersForCollector).append(")");
	} else if (CommonUtils.isNotEmpty(customerStatementForm.getCustomerNumber())) {
	    if (isMaster) {
		queryBuilder.append(" and (tp.acct_no='").append(customerStatementForm.getCustomerNumber()).append("'");
		queryBuilder.append(" or tp.masteracct_no='").append(customerStatementForm.getCustomerNumber()).append("')");
	    } else {
		queryBuilder.append(" and tp.acct_no='").append(customerStatementForm.getCustomerNumber()).append("'");
	    }
	}
	if (customerStatementForm.isAllCustomers()
		|| CommonUtils.isNotEmpty(customersForCollector)
		|| CommonUtils.isEmpty(customerStatementForm.getCustomerNumber())) {
	    if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getAgentsInclude(), "onlyAgents")) {
		queryBuilder.append(" and (tp.sub_type='Export Agent' or tp.sub_type='Import Agent')");
	    } else if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getAgentsInclude(), "agentsNotInclude")) {
		queryBuilder.append(" and (tp.sub_type is null or (tp.sub_type != 'Export Agent' and tp.sub_type != 'Import Agent'))");
	    }
	}
	queryBuilder.append(")");
	queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
	if (customerStatementForm.isIncludeInvoiceCredit()) {
	    queryBuilder.append(" and tr.balance<>0");
	} else {
	    queryBuilder.append(" and tr.balance>0");
	}
	if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getIncludeNetSettlement(), ONLY)) {
	    queryBuilder.append(" and tr.invoice_number like '").append(AccountingConstants.SUBLEDGER_CODE_NETSETT).append("%'");
	} else if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getIncludePrepayment(), ONLY)) {
	    queryBuilder.append(" and tr.invoice_number='").append(AccountingConstants.PRE_PAYMENT).append("'");
	} else if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getIncludeNetSettlement(), AccountingConstants.YES)
		&& CommonUtils.isEqualIgnoreCase(customerStatementForm.getIncludePrepayment(), AccountingConstants.YES)) {
	    //Nothing required
	} else if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getIncludeNetSettlement(), AccountingConstants.YES)) {
	    queryBuilder.append(" and (tr.invoice_number is null");
	    queryBuilder.append(" or tr.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("'");
	    queryBuilder.append(" or tr.invoice_number like '").append(AccountingConstants.SUBLEDGER_CODE_NETSETT).append("%')");
	} else if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getIncludePrepayment(), AccountingConstants.YES)) {
	    queryBuilder.append(" and (tr.invoice_number is null");
	    queryBuilder.append(" or tr.invoice_number='").append(AccountingConstants.PRE_PAYMENT).append("'");
	    queryBuilder.append(" or tr.invoice_number not like '").append(AccountingConstants.SUBLEDGER_CODE_NETSETT).append("%')");
	} else {
	    queryBuilder.append(" and (tr.invoice_number is null");
	    queryBuilder.append(" or (tr.invoice_number not like '").append(AccountingConstants.SUBLEDGER_CODE_NETSETT).append("%'");
	    queryBuilder.append(" and tr.invoice_number!='").append(AccountingConstants.PRE_PAYMENT).append("'))");
	}
	if (CommonUtils.isNotEmpty(customerStatementForm.getExcludeIds())) {
	    queryBuilder.append(" and tr.transaction_id not in (").append(customerStatementForm.getExcludeIds()).append(")");
	}
	queryBuilder.append(" group by tr.cust_no,invoice_or_bl");
	queryBuilder.append(" order by tr.Transaction_date");
	queryBuilder.append(") as ar");
	queryBuilder.append(" join ar_transaction_history th");
	queryBuilder.append(" on (ar.customer_number=th.customer_number and ar.invoice_or_bl=th.invoice_or_bl)");
	queryBuilder.append(" group by ar.customer_number,ar.invoice_or_bl)");
	return queryBuilder.toString();
    }

    private String buildApQuery(CustomerStatementForm customerStatementForm, String customersForCollector, String customersWithCredit, boolean isMaster) {
	if (customerStatementForm.isIncludeAP()) {
	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append("(select tr.transaction_date as invoice_date,");
	    queryBuilder.append("trim(if(tr.invoice_number!='',upper(tr.invoice_number),upper(tr.bill_ladding_no))) as invoice_or_bl,");
	    queryBuilder.append("trim(tr.booking_no) as booking_number,");
	    queryBuilder.append("trim(tr.customer_reference_no) as customer_reference,");
	    queryBuilder.append("trim(tr.cons_name) as consignee,");
	    queryBuilder.append("trim(tr.voyage_no) as voyage,");
	    queryBuilder.append("-tr.transaction_amt as invoice_amount,");
	    queryBuilder.append("-(tr.transaction_amt-tr.balance) as payment,");
	    queryBuilder.append("-tr.balance as balance,");
	    queryBuilder.append("'AP' as transaction_type,");
	    queryBuilder.append("null as note_module_id,");
	    queryBuilder.append("null as note_ref_id,");
	    queryBuilder.append("null as prepayment_notes");
	    queryBuilder.append(" from transaction tr");
	    queryBuilder.append(" join trading_partner tp");
	    queryBuilder.append(" on (tp.acct_no=tr.cust_no");
	    if (customerStatementForm.isAllCustomers() || CommonUtils.isNotEmpty(customersForCollector)) {
		if (customerStatementForm.isStmtWithCredit()) {
		    queryBuilder.append(" and tp.acct_no in (").append(customersWithCredit).append(")");
		} else {
		    queryBuilder.append(" and tp.acct_no not in (").append(customersWithCredit).append(")");
		}
	    }
	    if (customerStatementForm.isAllCustomers()) {
		queryBuilder.append(" and tp.acct_no is not null");
	    } else if (CommonUtils.isNotEmpty(customersForCollector)) {
		queryBuilder.append(" and tp.acct_no in (").append(customersForCollector).append(")");
	    } else if (CommonUtils.isNotEmpty(customerStatementForm.getCustomerNumber())) {
		if (isMaster) {
		    queryBuilder.append(" and (tp.acct_no='").append(customerStatementForm.getCustomerNumber()).append("'");
		    queryBuilder.append(" or tp.masteracct_no='").append(customerStatementForm.getCustomerNumber()).append("')");
		} else {
		    queryBuilder.append(" and tp.acct_no='").append(customerStatementForm.getCustomerNumber()).append("'");
		}
	    }
	    if (customerStatementForm.isAllCustomers()
		    || CommonUtils.isNotEmpty(customersForCollector)
		    || CommonUtils.isEmpty(customerStatementForm.getCustomerNumber())) {
		if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getAgentsInclude(), "onlyAgents")) {
		    queryBuilder.append(" and (tp.sub_type='Export Agent' or tp.sub_type='Import Agent')");
		} else if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getAgentsInclude(), "agentsNotInclude")) {
		    queryBuilder.append(" and (tp.sub_type is null or (tp.sub_type != 'Export Agent' and tp.sub_type != 'Import Agent'))");
		}
	    }
	    queryBuilder.append(")");
	    queryBuilder.append(" where tr.transaction_type='").append(TRANSACTION_TYPE_ACCOUNT_PAYABLE).append("'");
	    queryBuilder.append(" and tr.status='").append(STATUS_OPEN).append("'");
	    queryBuilder.append(" and tr.balance<>0)");
	    return queryBuilder.toString();
	} else {
	    return null;
	}
    }

    private String buildAcQuery(CustomerStatementForm customerStatementForm, String customersForCollector, String customersWithCredit, boolean isMaster) {
	if (customerStatementForm.isIncludeAccruals()) {
	    StringBuilder queryBuilder = new StringBuilder();
	    queryBuilder.append("(select tl.transaction_date as invoice_date,");
	    queryBuilder.append("trim(if(tl.invoice_number!='',upper(tl.invoice_number),upper(tl.bill_ladding_no))) as invoice_or_bl,");
	    queryBuilder.append("trim(tl.booking_no) as booking_number,");
	    queryBuilder.append("trim(tl.customer_reference_no) as customer_reference,");
	    queryBuilder.append("trim(tl.cons_name) as consignee,");
	    queryBuilder.append("trim(tl.voyage_no) as voyage,");
	    queryBuilder.append("-tl.transaction_amt as invoice_amount,");
	    queryBuilder.append("-(tl.transaction_amt-tl.balance) as payment,");
	    queryBuilder.append("-tl.balance as balance,");
	    queryBuilder.append("'AC' as transaction_type,");
	    queryBuilder.append("null as note_module_id,");
	    queryBuilder.append("null as note_ref_id,");
	    queryBuilder.append("null as prepayment_notes");
	    queryBuilder.append(" from transaction_ledger tl");
	    queryBuilder.append(" join trading_partner tp");
	    queryBuilder.append(" on (tp.acct_no=tl.cust_no");
	    if (customerStatementForm.isAllCustomers() || CommonUtils.isNotEmpty(customersForCollector)) {
		if (customerStatementForm.isStmtWithCredit()) {
		    queryBuilder.append(" and tp.acct_no in (").append(customersWithCredit).append(")");
		} else {
		    queryBuilder.append(" and tp.acct_no not in (").append(customersWithCredit).append(")");
		}
	    }
	    if (customerStatementForm.isAllCustomers()) {
		queryBuilder.append(" and tp.acct_no is not null");
	    } else if (CommonUtils.isNotEmpty(customersForCollector)) {
		queryBuilder.append(" and tp.acct_no in (").append(customersForCollector).append(")");
	    } else if (CommonUtils.isNotEmpty(customerStatementForm.getCustomerNumber())) {
		if (isMaster) {
		    queryBuilder.append(" and (tp.acct_no='").append(customerStatementForm.getCustomerNumber()).append("'");
		    queryBuilder.append(" or tp.masteracct_no='").append(customerStatementForm.getCustomerNumber()).append("')");
		} else {
		    queryBuilder.append(" and tp.acct_no='").append(customerStatementForm.getCustomerNumber()).append("'");
		}
	    }
	    if (customerStatementForm.isAllCustomers()
		    || CommonUtils.isNotEmpty(customersForCollector)
		    || CommonUtils.isEmpty(customerStatementForm.getCustomerNumber())) {
		if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getAgentsInclude(), "onlyAgents")) {
		    queryBuilder.append(" and (tp.sub_type='Export Agent' or tp.sub_type='Import Agent')");
		} else if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getAgentsInclude(), "agentsNotInclude")) {
		    queryBuilder.append(" and (tp.sub_type is null or (tp.sub_type != 'Export Agent' and tp.sub_type != 'Import Agent'))");
		}
	    }
	    queryBuilder.append(")");
	    queryBuilder.append(" where tl.transaction_type='").append(TRANSACTION_TYPE_ACCRUALS).append("'");
	    queryBuilder.append(" and tl.status!='").append(STATUS_ASSIGN).append("'");
	    queryBuilder.append(" and tl.status!='").append(STATUS_EDI_ASSIGNED).append("'");
	    queryBuilder.append(" and tl.status!='").append(STATUS_INACTIVE).append("'");
	    queryBuilder.append(" and tl.balance<>0)");
	    return queryBuilder.toString();
	} else {
	    return null;
	}
    }

    public Map<String, String> buildQueries(CustomerStatementForm customerStatementForm) {
	String customersWithCredit = getArCustomerWithCredit();
	String customersForCollector = null;
	if (CommonUtils.isEqualIgnoreCase(customerStatementForm.getAllcollectors(), AccountingConstants.YES)
		|| CommonUtils.isEqualIgnoreCase(customerStatementForm.getCollector(), ALL)) {
	    customersForCollector = getArCustomerForCollector(null);
	} else if (CommonUtils.isNotEmpty(customerStatementForm.getCollector())) {
	    customersForCollector = getArCustomerForCollector(customerStatementForm.getCollector());
	}
	boolean isMaster = false;
	if (CommonUtils.isNotEmpty(customerStatementForm.getCustomerNumber())) {
	    isMaster = new TradingPartnerDAO().isMaster(customerStatementForm.getCustomerNumber());
	}
	String arQuery = buildArQuery(customerStatementForm, customersForCollector, customersWithCredit, isMaster);
	String apQuery = buildApQuery(customerStatementForm, customersForCollector, customersWithCredit, isMaster);
	String acQuery = buildAcQuery(customerStatementForm, customersForCollector, customersWithCredit, isMaster);
	Map<String, String> queries = new HashMap<String, String>();
	queries.put("arQuery", arQuery);
	queries.put("apQuery", apQuery);
	queries.put("acQuery", acQuery);
	return queries;
    }

    public CustomerBean getReceivableAgingBuckets(String arQuery) throws Exception {
	CustomerBean customerBean = new CustomerBean();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select format(sum(if(datediff(sysdate(),t.invoice_date)<=30,t.balance,0)),2) as 0_30days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=31");
	queryBuilder.append("  and datediff(sysdate(),t.invoice_date)<=60,t.balance,0)),2) as 31_60days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=61");
	queryBuilder.append(" and datediff(sysdate(),t.invoice_date)<=90,t.balance,0)),2) as 61_90days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=91,t.balance,0)),2) as 91days,");
	queryBuilder.append("format(sum(t.balance),2) as total");
	queryBuilder.append(" from (");
	queryBuilder.append(arQuery);
	queryBuilder.append(" ) as t");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	if (null != result) {
	    Object[] col = (Object[]) result;
	    customerBean.setCurrent(null != col[0] ? (String) col[0] : "0.00");
	    customerBean.setThirtyOneToSixtyDays(null != col[1] ? (String) col[1] : "0.00");
	    customerBean.setSixtyOneToNintyDays(null != col[2] ? (String) col[2] : "0.00");
	    customerBean.setGreaterThanNintyDays(null != col[3] ? (String) col[3] : "0.00");
	    customerBean.setTotal(null != col[4] ? (String) col[4] : "0.00");
	} else {
	    customerBean.setCurrent("0.00");
	    customerBean.setThirtyOneToSixtyDays("0.00");
	    customerBean.setSixtyOneToNintyDays("0.00");
	    customerBean.setGreaterThanNintyDays("0.00");
	    customerBean.setTotal("0.00");
	}
	return customerBean;
    }

    public CustomerBean getPayableAgingBuckets(String apQuery, String acQuery) throws Exception {
	CustomerBean customerBean = new CustomerBean();
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select format(sum(if(datediff(sysdate(),t.invoice_date)<=30,t.balance,0)),2) as 0_30days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=31");
	queryBuilder.append("  and datediff(sysdate(),t.invoice_date)<=60,t.balance,0)),2) as 31_60days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=61");
	queryBuilder.append(" and datediff(sysdate(),t.invoice_date)<=90,t.balance,0)),2) as 61_90days,");
	queryBuilder.append("format(sum(if(datediff(sysdate(),t.invoice_date)>=91,t.balance,0)),2) as 91days,");
	queryBuilder.append("format(sum(t.balance),2) as total,");
	queryBuilder.append("format(sum(if(t.transaction_type='AP',t.balance,0)),2) as ap_balance,");
	queryBuilder.append("format(sum(if(t.transaction_type='AC',t.balance,0)),2) as ac_balance");
	queryBuilder.append(" from (");
	if (null != apQuery) {
	    queryBuilder.append(apQuery);
	}
	if (null != acQuery) {
	    queryBuilder.append(null != apQuery ? " union " : "").append(acQuery);
	}
	queryBuilder.append(" ) as t");
	Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
	if (null != result) {
	    Object[] col = (Object[]) result;
	    customerBean.setCurrent(null != col[0] ? (String) col[0] : "0.00");
	    customerBean.setThirtyOneToSixtyDays(null != col[1] ? (String) col[1] : "0.00");
	    customerBean.setSixtyOneToNintyDays(null != col[2] ? (String) col[2] : "0.00");
	    customerBean.setGreaterThanNintyDays(null != col[3] ? (String) col[3] : "0.00");
	    customerBean.setTotal(null != col[4] ? (String) col[4] : "0.00");
	    customerBean.setOutstandingPayables(null != col[5] ? (String) col[5] : "0.00");
	    customerBean.setOutstandingAccruals(null != col[6] ? (String) col[6] : "0.00");
	} else {
	    customerBean.setCurrent("0.00");
	    customerBean.setThirtyOneToSixtyDays("0.00");
	    customerBean.setSixtyOneToNintyDays("0.00");
	    customerBean.setGreaterThanNintyDays("0.00");
	    customerBean.setTotal("0.00");
	}
	return customerBean;
    }

    public List<AccountingBean> getTransactions(String button, Map<String, String> queries) throws Exception {
	String arQuery = queries.get("arQuery");
	String apQuery = queries.get("apQuery");
	String acQuery = queries.get("acQuery");
	StringBuilder queryBuilder = new StringBuilder();
	queryBuilder.append("select date_format(t.invoice_date,'%m/%d/%Y') as formattedDate,");
	if (ESTATEMENT_TO_EXCEL.equals(button)) {
	    queryBuilder.append("if(t.invoice_or_bl like '%04-%',");
	    queryBuilder.append("replace(replace(t.invoice_or_bl,substring_index(t.invoice_or_bl,'04-',1),''),'-',''),t.invoice_or_bl) as invoiceOrBl,");
	    queryBuilder.append("t.booking_number as bookingNumber,");
	    queryBuilder.append("t.customer_reference as customerReference,");
	    queryBuilder.append("t.consignee as consignee,");
	    queryBuilder.append("t.voyage as voyage,");
	} else {
	    queryBuilder.append("left(if(t.invoice_or_bl like '%04-%',");
	    queryBuilder.append("replace(replace(t.invoice_or_bl,substring_index(t.invoice_or_bl,'04-',1),''),'-',''),t.invoice_or_bl),20) as invoiceOrBl,");
	    queryBuilder.append("left(t.booking_number,12) as bookingNumber,");
	    queryBuilder.append("left(t.customer_reference,25) as customerReference,");
	    queryBuilder.append("left(t.consignee,10) as consignee,");
	    queryBuilder.append("left(t.voyage,10) as voyage,");
	}
	queryBuilder.append("format(t.invoice_amount,2) as formattedAmount,");
	queryBuilder.append("format(t.payment,2) as formattedPayment,");
	queryBuilder.append("format(t.balance,2) as formattedBalance,");
	queryBuilder.append("if(t.prepayment_notes!='' && group_concat(n.note_desc separator '<--->')!='',");
	queryBuilder.append("concat(t.prepayment_notes,'<--->',group_concat(n.note_desc separator '<--->')!=''),");
	queryBuilder.append("if(t.prepayment_notes!='',t.prepayment_notes,");
	queryBuilder.append("if(group_concat(n.note_desc separator '<--->')!='',group_concat(n.note_desc separator '<--->'),''))) as invoiceNotes");
	queryBuilder.append(" from ( ");
	queryBuilder.append("(select ar.invoice_date as invoice_date,");
	queryBuilder.append("ar.invoice_or_bl as invoice_or_bl,");
	queryBuilder.append("ar.booking_number as booking_number,");
	queryBuilder.append("ar.customer_reference as customer_reference,");
	queryBuilder.append("ar.consignee as consignee,");
	queryBuilder.append("ar.voyage as voyage,");
	queryBuilder.append("ar.invoice_amount as invoice_amount,");
	queryBuilder.append("ar.payment as payment,");
	queryBuilder.append("ar.balance as balance,");
	queryBuilder.append("ar.transaction_type as transaction_type,");
	queryBuilder.append("ar.note_module_id as note_module_id,");
	queryBuilder.append("ar.note_ref_id as note_ref_id,");
	queryBuilder.append("group_concat(p.notes separator '<--->') as prepayment_notes");
	queryBuilder.append(" from ").append(arQuery).append(" as ar");
	queryBuilder.append(" left join payments p");
	queryBuilder.append(" on (p.payment_type='Check' and p.invoice_no='PRE PAYMENT'");
	queryBuilder.append(" and p.Bill_Ladding_No=ar.invoice_or_bl and p.Cust_No=ar.customer_number)");
	queryBuilder.append(" group by ar.invoice_or_bl)");
	if (null != apQuery) {
	    queryBuilder.append(" union ").append(apQuery);
	}
	if (null != acQuery) {
	    queryBuilder.append(" union ").append(acQuery);
	}
	queryBuilder.append(" ) as t");
	queryBuilder.append(" left join notes n");
	queryBuilder.append(" on (t.note_module_id=n.module_id");
	queryBuilder.append(" and t.note_ref_id=n.module_ref_id");
	queryBuilder.append(" and n.print_on_report = 1)");
	queryBuilder.append(" where t.balance!=0");
	queryBuilder.append(" group by t.invoice_or_bl,t.transaction_type");
	queryBuilder.append(" order by t.invoice_date");
	return getCurrentSession().createSQLQuery(queryBuilder.toString()).setResultTransformer(Transformers.aliasToBean(AccountingBean.class)).list();
    }
}
