package com.gp.cong.logisoft.bc.accounting;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.ExcelGenerator.ExportArStatementToExcel;
import com.gp.cong.logisoft.reports.ManualStatementCreator;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.CustomerStatementForm;
import com.logiware.accounting.excel.AutoStatementExcelCreator;
import com.logiware.accounting.model.CompanyModel;
import com.logiware.bean.AccountingBean;
import com.logiware.bean.CustomerBean;
import com.logiware.excel.StatementConfigurationExcelCreator;
import com.logiware.hibernate.dao.ArStatementDAO;
import com.logiware.reports.AutoStatementCreator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class CustomerStatementBC implements ConstantsInterface {

    public String createStatement(CustomerStatementForm customerStatementForm, String contextPath) throws Exception {
        ArStatementDAO arStatementDAO = new ArStatementDAO();
        Map<String, String> queries = arStatementDAO.buildQueries(customerStatementForm);
        String button = customerStatementForm.getButtonValue();
        List<AccountingBean> transactions = arStatementDAO.getTransactions(button, queries);
        String arQuery = queries.get("arQuery");
        String apQuery = queries.get("apQuery");
        String acQuery = queries.get("acQuery");
        CustomerBean arAgingBuckets = arStatementDAO.getReceivableAgingBuckets(arQuery);
        CustomerBean apAgingBuckets = null;
        if (null != apQuery || null != acQuery) {
            apAgingBuckets = arStatementDAO.getPayableAgingBuckets(apQuery, acQuery);
        }
        Map<String, CustomerBean> agingBuckets = new HashMap<String, CustomerBean>();
        agingBuckets.put("arAgingBuckets", arAgingBuckets);
        agingBuckets.put("apAgingBuckets", apAgingBuckets);
        CustomerBean customerDetails = null;
        if (customerStatementForm.isAllCustomers()
                || CommonUtils.isNotEmpty(customerStatementForm.getCollector())) {
        } else if (CommonUtils.isNotEmpty(customerStatementForm.getCustomerNumber())) {
            customerDetails = arStatementDAO.getCustomerDetails(customerStatementForm.getCustomerNumber());
        }
        return new ManualStatementCreator().createReport(contextPath, transactions, customerDetails, agingBuckets, customerStatementForm);
    }

    public String configurationReport() throws Exception {
        return new StatementConfigurationExcelCreator().create(true);
    }

    public String createReport(CustomerBean customerDetails, String contextPath) throws Exception {
        ArStatementDAO arStatementDAO = new ArStatementDAO();
        CompanyModel company = new SystemRulesDAO().getCompanyDetails();
        String query = arStatementDAO.buildArQuery(customerDetails);
        CustomerBean agingBuckets = arStatementDAO.getAgingBuckets(query);
        boolean canSend = true;
        if (StringUtils.contains(agingBuckets.getTotal(), "-") && CommonUtils.isEqualIgnoreCase(customerDetails.getCreditBalance(), NO)) {
            canSend = false;
        }
        if (canSend) {
            List<AccountingBean> transactions = arStatementDAO.getTransactions(query);
            if (CommonUtils.isNotEmpty(transactions)) {
                if (customerDetails.getStatementPdfOrExcel().equalsIgnoreCase("PDF")) {
                    return new AutoStatementCreator().createReport(contextPath, transactions, customerDetails, agingBuckets);
                } else {
                    return new AutoStatementExcelCreator(transactions, customerDetails, company, agingBuckets).create();
                }
            }
        }
        return null;
    }

    public String exportToExcel(CustomerStatementForm customerStatementForm, String contextPath) throws Exception {
        ArStatementDAO arStatementDAO = new ArStatementDAO();
        Map<String, String> queries = arStatementDAO.buildQueries(customerStatementForm);
        String button = customerStatementForm.getButtonValue();
        List<AccountingBean> transactions = arStatementDAO.getTransactions(button, queries);
        String arQuery = queries.get("arQuery");
        String apQuery = queries.get("apQuery");
        String acQuery = queries.get("acQuery");
        CustomerBean arAgingBuckets = arStatementDAO.getReceivableAgingBuckets(arQuery);
        CustomerBean apAgingBuckets = null;
        if (null != apQuery || null != acQuery) {
            apAgingBuckets = arStatementDAO.getPayableAgingBuckets(apQuery, acQuery);
        }
        Map<String, CustomerBean> agingBuckets = new HashMap<String, CustomerBean>();
        agingBuckets.put("arAgingBuckets", arAgingBuckets);
        agingBuckets.put("apAgingBuckets", apAgingBuckets);
        CustomerBean customerDetails = null;
        if (customerStatementForm.isAllCustomers()
                || CommonUtils.isNotEmpty(customerStatementForm.getCollector())) {
        } else if (CommonUtils.isNotEmpty(customerStatementForm.getCustomerNumber())) {
            customerDetails = arStatementDAO.getCustomerDetails(customerStatementForm.getCustomerNumber());
        }
        return new ExportArStatementToExcel().exportToExcel(transactions, customerDetails, agingBuckets, customerStatementForm);
    }
}
