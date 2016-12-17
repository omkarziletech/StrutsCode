package com.logiware.hibernate.dao;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.logiware.bean.AccountingBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ControlReportDAO extends BaseHibernateDAO {

    private static Properties properties = null;

    public ControlReportDAO() throws IOException {
        if (null == properties) {
            properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/hibernate.properties"));
        }
    }

    public Integer getNumberOfBlueScreenAccruals(String query) {
        StringBuilder queryBuilder = new StringBuilder("select count(*)");
        queryBuilder.append(query);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Integer.parseInt(result.toString());
        }
        return 0;
    }

    public Integer getNumberOfLogiwareAccruals(String query) {
        StringBuilder queryBuilder = new StringBuilder("select count(*)");
        queryBuilder.append(query);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Integer.parseInt(result.toString());
        }
        return 0;
    }

    public Double getTotalAmountInBlueScreenAccruals(String query) {
        StringBuilder queryBuilder = new StringBuilder("select sum(ac.amount)");
        queryBuilder.append(query);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Double.parseDouble(result.toString());
        }
        return 0d;
    }

    public Double getTotalAmountInLogiwareAccruals(String query) {
        StringBuilder queryBuilder = new StringBuilder("select sum(tl.Transaction_amt)");
        queryBuilder.append(query);
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Double.parseDouble(result.toString());
        }
        return 0d;
    }

    public List<AccountingBean> getBlueScreenAccruals(String query) {
        List<AccountingBean> blueScreenAccruals = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder("select t.custName,t.custNo,t.invoiceOrBl,t.amount,t.bluescreenkey");
        queryBuilder.append(query).append(" as t where t.control='bluescreen'");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean blueScreenAccrual = new AccountingBean();
            blueScreenAccrual.setVendorName((String) col[0]);
            blueScreenAccrual.setVendorNumber((String) col[1]);
            blueScreenAccrual.setInvoiceOrBl((String) col[2]);
            blueScreenAccrual.setAmount(Double.parseDouble(col[3].toString()));
            blueScreenAccrual.setApCostKey((String) col[4]);
            blueScreenAccruals.add(blueScreenAccrual);
        }
        return blueScreenAccruals;
    }

    public List<AccountingBean> getLogiwareAccruals(String query) {
        List<AccountingBean> logiwareAccruals = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder("select t.custName,t.custNo,t.invoiceOrBl,t.amount,t.bluescreenkey");
        queryBuilder.append(query).append(" as t where t.control='logiware'");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean logiwareAccrual = new AccountingBean();
            logiwareAccrual.setVendorName((String) col[0]);
            logiwareAccrual.setVendorNumber((String) col[1]);
            logiwareAccrual.setInvoiceOrBl((String) col[2]);
            logiwareAccrual.setAmount(Double.parseDouble(col[3].toString()));
            logiwareAccrual.setApCostKey((String) col[4]);
            logiwareAccruals.add(logiwareAccrual);
        }
        return logiwareAccruals;
    }

    public String buildQueryForBlueAccruals(String createdDate) {
        String loadingDB = properties.getProperty("hibernate.database.loading");
        String applicationDB = properties.getProperty("hibernate.database.application");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from ").append(loadingDB).append(".apcost ac join ").append(applicationDB).append(".trading_partner tp");
        queryBuilder.append(" where tp.ECIVENDNO = if(ac.eacode = 'A',ac.agtvn,ac.vend)");
        queryBuilder.append(" and ac.posted <> 'P' and ac.posted <> 'R' and ac.posted <> '*'");
        queryBuilder.append(" and ac.entdat=date_format('").append(createdDate).append("','%Y%m%d')");
        return queryBuilder.toString();
    }

    public String buildQueryForLogiwareAccruals(String createdDate) {
        String applicationDB = properties.getProperty("hibernate.database.application");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from ").append(applicationDB).append(".transaction_ledger tl,").append(applicationDB).append(".trading_partner tp");
        queryBuilder.append(" where tp.acct_no=tl.cust_no and date_format(tl.Created_On,'%Y-%m-%d')='").append(createdDate).append("'");
        queryBuilder.append(" and isnull(tl.Created_By) and tl.transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCRUALS).append("'");
        return queryBuilder.toString();
    }

    public String buildQueryForMissingAccruals(String createdDate) {
        String loadingDB = properties.getProperty("hibernate.database.loading");
        String applicationDB = properties.getProperty("hibernate.database.application");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" from (select tbl.custName,tbl.custNo,tbl.invoiceOrBl,tbl.amount,tbl.bluescreenkey,tbl.control");
        queryBuilder.append(" from ((select tp.acct_name as custName,tp.acct_no as custNo,");
        queryBuilder.append(" if(tl.Invoice_number<>'',tl.Invoice_number,tl.Bill_Ladding_No) as invoiceOrBl,");
        queryBuilder.append(" tl.Transaction_amt as amount,tl.apcostkey as bluescreenkey,'logiware' as control");
        queryBuilder.append(" from ").append(applicationDB).append(".transaction_ledger tl,").append(applicationDB).append(".trading_partner tp");
        queryBuilder.append(" where tp.acct_no=tl.cust_no and date_format(tl.Created_On,'%Y-%m-%d')='").append(createdDate).append("'");
        queryBuilder.append(" and isnull(tl.Created_By) and tl.transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCRUALS).append("')");
        queryBuilder.append(" union");
        queryBuilder.append(" (select tp.acct_name as custName,tp.acct_no as custNo,ac.invnum as invoiceOrBl,ac.amount as amount,");
        queryBuilder.append(" concat(ac.type,ac.askfld,ac.cntrl) as bluescreenkey,'bluescreen' as control");
        queryBuilder.append(" from ").append(loadingDB).append(".apcost ac join ").append(applicationDB).append(".trading_partner tp");
        queryBuilder.append(" where tp.ECIVENDNO = if(ac.cstcde='012' or ac.cstcde='112' or ac.eacode = 'E',ac.vend,ac.agtvn)");
        queryBuilder.append(" and ac.posted <> 'P' and ac.posted <> 'R' and ac.posted <> '*'");
        queryBuilder.append(" and ac.entdat=date_format('").append(createdDate).append("','%Y%m%d'))) as tbl");
        queryBuilder.append(" group by concat(tbl.custNo,tbl.bluescreenkey,tbl.amount)");
        queryBuilder.append(" having count(concat(tbl.custNo,tbl.bluescreenkey,tbl.amount))=1");
        queryBuilder.append(" order by tbl.custNo,tbl.invoiceOrBl)");
        return queryBuilder.toString();
    }

    public Integer getNumberOfBlueScreenAccountReceivables(String fromDate, String toDate) {
        String loadingDB = properties.getProperty("hibernate.database.loading");
        StringBuilder queryBuilder = new StringBuilder("select count(*) from ").append(loadingDB).append(".openar ar");
        queryBuilder.append(" where ar.bldate between date_format('").append(fromDate).append("','%Y%m%d') and  date_format('").append(toDate).append("','%Y%m%d')");
        queryBuilder.append(" and ar.type1!=2 and ar.type1!=5 and ar.type1!=7 and ar.cmpnum!='01'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        return null != result?Integer.parseInt(result.toString()):0;
    }

    public Integer getNumberOfLogiwareAccountReceivables(String fromDate, String toDate) {
        String applicationDB = properties.getProperty("hibernate.database.application");
        StringBuilder queryBuilder = new StringBuilder("select count(*) from ").append(applicationDB).append(".ar_transaction_history arth");
        queryBuilder.append(" join ").append(applicationDB).append(".transaction ar");
        queryBuilder.append(" on ar.Transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and ar.Bill_Ladding_No=arth.bl_number");
        queryBuilder.append(" and ar.Invoice_number=arth.invoice_number");
        queryBuilder.append(" and ar.cust_no=arth.customer_number");
        queryBuilder.append(" and date_format(arth.transaction_date,'%Y-%m-%d') between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append(" and arth.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Integer.parseInt(result.toString());
        }
        return 0;
    }

    public Double getTotalAmountInBlueScreenAccountReceivables(String fromDate, String toDate) {
        String loadingDB = properties.getProperty("hibernate.database.loading");
        StringBuilder queryBuilder = new StringBuilder("select sum(ar.totdue) from ").append(loadingDB).append(".openar ar");
        queryBuilder.append(" where ar.bldate between date_format('").append(fromDate).append("','%Y%m%d') and  date_format('").append(toDate).append("','%Y%m%d')");
        queryBuilder.append(" and ar.type1!=2 and ar.type1!=5 and ar.type1!=7 and ar.cmpnum!='01'");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Double.parseDouble(result.toString());
        }
        return 0d;
    }

    public Double getTotalAmountInLogiwareAccountReceivables(String fromDate, String toDate) {
        String applicationDB = properties.getProperty("hibernate.database.application");
        StringBuilder queryBuilder = new StringBuilder("select sum(arth.transaction_amount) from ");
        queryBuilder.append(applicationDB).append(".ar_transaction_history arth,");
        queryBuilder.append(applicationDB).append(".transaction ar");
        queryBuilder.append(" where ar.Transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and ar.Bill_Ladding_No=arth.bl_number");
        queryBuilder.append(" and ar.Invoice_number=arth.invoice_number");
        queryBuilder.append(" and ar.cust_no=arth.customer_number");
        queryBuilder.append(" and date_format(arth.transaction_date,'%Y-%m-%d') between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append(" and arth.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        Object result = getCurrentSession().createSQLQuery(queryBuilder.toString()).uniqueResult();
        if (null != result) {
            return Double.parseDouble(result.toString());
        }
        return 0d;
    }

    public List<AccountingBean> getBlueScreenAccountReceivables(String fromDate, String toDate) {
        String loadingDB = properties.getProperty("hibernate.database.loading");
        String applicationDB = properties.getProperty("hibernate.database.application");
        List<AccountingBean> blueScreenAccountReceivables = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder("select tp.acct_name,tp.acct_no,");
        queryBuilder.append("if(ar.hstkey='' or ar.hstkey is null,ar.invkey,ar.hstkey) as invoiceOrBl,ar.totdue");
        queryBuilder.append(" from ").append(loadingDB).append(".openar ar,");
        queryBuilder.append(applicationDB).append(".trading_partner tp where tp.eci_acct_no=ar.actnum");
        queryBuilder.append(" and ar.bldate between date_format('").append(fromDate).append("','%Y%m%d') and  date_format('").append(toDate).append("','%Y%m%d')");
        queryBuilder.append(" and ar.type1!=2 and ar.type1!=5 and ar.type1!=7 and ar.cmpnum!='01'");
        queryBuilder.append(" and concat(tp.acct_no,if(ar.hstkey='' or ar.hstkey is null,ar.invkey,ar.hstkey))");
        queryBuilder.append(" not in (select concat(arth.customer_number,if(arth.bl_number!='',arth.bl_number,arth.invoice_number)) from ");
        queryBuilder.append(applicationDB).append(".ar_transaction_history arth,");
        queryBuilder.append(applicationDB).append(".transaction ar,trading_partner tp");
        queryBuilder.append(" where ar.Transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and ar.Bill_Ladding_No=arth.bl_number");
        queryBuilder.append(" and ar.Invoice_number=arth.invoice_number");
        queryBuilder.append(" and ar.cust_no=arth.customer_number");
        queryBuilder.append(" and tp.acct_no=arth.customer_number");
        queryBuilder.append(" and arth.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        queryBuilder.append(" and date_format(arth.transaction_date,'%Y-%m-%d') between '").append(fromDate).append("' and '").append(toDate).append("')");
        queryBuilder.append(" order by concat(tp.acct_no,invoiceOrBl)");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean blueScreenAccountReceivable = new AccountingBean();
            blueScreenAccountReceivable.setVendorName((String) col[0]);
            blueScreenAccountReceivable.setVendorNumber((String) col[1]);
            blueScreenAccountReceivable.setInvoiceOrBl((String) col[2]);
            blueScreenAccountReceivable.setAmount(Double.parseDouble(col[3].toString()));
            blueScreenAccountReceivables.add(blueScreenAccountReceivable);
        }
        return blueScreenAccountReceivables;
    }

    public List<AccountingBean> getLogiwareAccountReceivables(String fromDate, String toDate) {
        String loadingDB = properties.getProperty("hibernate.database.loading");
        String applicationDB = properties.getProperty("hibernate.database.application");
        List<AccountingBean> logiwareAccountReceivables = new ArrayList<AccountingBean>();
        StringBuilder queryBuilder = new StringBuilder("select tp.acct_name,tp.acct_no,if(arth.bl_number!='',arth.bl_number,arth.invoice_number),");
        queryBuilder.append("arth.transaction_amount from ").append(applicationDB).append(".ar_transaction_history arth,");
        queryBuilder.append(applicationDB).append(".transaction ar,");
        queryBuilder.append(applicationDB).append(".trading_partner tp");
        queryBuilder.append(" where ar.Transaction_type='").append(CommonConstants.TRANSACTION_TYPE_ACCOUNT_RECEIVABLE).append("'");
        queryBuilder.append(" and (ar.Bill_Ladding_No=arth.bl_number");
        queryBuilder.append(" and ar.Invoice_number=arth.invoice_number)");
        queryBuilder.append(" and ar.cust_no=arth.customer_number");
        queryBuilder.append(" and tp.acct_no=arth.customer_number");
        queryBuilder.append(" and arth.transaction_type not in ('AP PY','AR PY','PP INV','OA INV','NS INV','ADJ')");
        queryBuilder.append(" and date_format(arth.transaction_date,'%Y-%m-%d') between '").append(fromDate).append("' and '").append(toDate).append("'");
        queryBuilder.append("  and concat(arth.customer_number,if(arth.bl_number!='',arth.bl_number,arth.invoice_number)) not in");
        queryBuilder.append(" (select concat(tp.acct_no,if(ar.hstkey='' or ar.hstkey is null,ar.invkey,ar.hstkey))");
        queryBuilder.append(" from ").append(loadingDB).append(".openar ar,");
        queryBuilder.append(applicationDB).append(".trading_partner tp where tp.eci_acct_no=ar.actnum");
        queryBuilder.append(" and ar.type1!=2 and ar.type1!=5 and ar.type1!=7 and ar.cmpnum!='01'");
        queryBuilder.append(" and ar.bldate between date_format('").append(fromDate).append("','%Y%m%d') and  date_format('").append(toDate).append("','%Y%m%d'))");
        queryBuilder.append(" order by concat(arth.customer_number,if(arth.bl_number!='',arth.bl_number,arth.invoice_number))");
        List result = getCurrentSession().createSQLQuery(queryBuilder.toString()).list();
        for (Object row : result) {
            Object[] col = (Object[]) row;
            AccountingBean logiwareAccountReceivable = new AccountingBean();
            logiwareAccountReceivable.setVendorName((String) col[0]);
            logiwareAccountReceivable.setVendorNumber((String) col[1]);
            logiwareAccountReceivable.setInvoiceOrBl((String) col[2]);
            logiwareAccountReceivable.setAmount(Double.parseDouble(col[3].toString()));
            logiwareAccountReceivables.add(logiwareAccountReceivable);
        }
        return logiwareAccountReceivables;
    }
}
