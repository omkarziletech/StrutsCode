package com.logiware.accounting.form;

import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalManagerDao;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.logiware.accounting.model.SalesModel;
import com.logiware.accounting.model.TerminalModel;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ArReportsForm extends BaseForm implements ConstantsInterface {

    private boolean allCustomers;
    private boolean top10Customers;
    private String collector;
    private String agents = NO;
    private boolean eculine;
    private boolean creditInvoice;
    private boolean creditStatement;
    private String message;
    private boolean coverLetter;
    private boolean ap;
    private boolean ac;
    private String netsett = YES;
    private String prepayments = YES;
    private String excludeIds;
    private String reportType;
    private String customerFromRange;
    private String customerToRange;
    private String cutOffDate;
    private boolean allPayments;
    private boolean notReleased;
    private String dsoFilter;
    private String dsoPeriod;
    private String numberOfDays;
    private String accountAssignedTo;
    private String notesEnteredBy;
    private TerminalModel terminalManager;
    private SalesModel salesManager;
    private String internationalCollector;

    public boolean isAllCustomers() {
        return allCustomers;
    }

    public void setAllCustomers(boolean allCustomers) {
        this.allCustomers = allCustomers;
    }

    public boolean isTop10Customers() {
        return top10Customers;
    }

    public void setTop10Customers(boolean top10Customers) {
        this.top10Customers = top10Customers;
    }

    public String getCollector() {
        return collector;
    }

    public void setCollector(String collector) {
        this.collector = collector;
    }

    public String getAgents() {
        return agents;
    }

    public void setAgents(String agents) {
        this.agents = agents;
    }

    public boolean isEculine() {
        return eculine;
    }

    public void setEculine(boolean eculine) {
        this.eculine = eculine;
    }

    public boolean isCreditInvoice() {
        return creditInvoice;
    }

    public void setCreditInvoice(boolean creditInvoice) {
        this.creditInvoice = creditInvoice;
    }

    public boolean isCreditStatement() {
        return creditStatement;
    }

    public void setCreditStatement(boolean creditStatement) {
        this.creditStatement = creditStatement;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(boolean coverLetter) {
        this.coverLetter = coverLetter;
    }

    public boolean isAp() {
        return ap;
    }

    public void setAp(boolean ap) {
        this.ap = ap;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public String getNetsett() {
        return netsett;
    }

    public void setNetsett(String netsett) {
        this.netsett = netsett;
    }

    public String getPrepayments() {
        return prepayments;
    }

    public void setPrepayments(String prepayments) {
        this.prepayments = prepayments;
    }

    public String getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(String excludeIds) {
        this.excludeIds = excludeIds;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getCustomerFromRange() {
        return customerFromRange;
    }

    public void setCustomerFromRange(String customerFromRange) {
        this.customerFromRange = customerFromRange;
    }

    public String getCustomerToRange() {
        return customerToRange;
    }

    public void setCustomerToRange(String customerToRange) {
        this.customerToRange = customerToRange;
    }

    public String getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public boolean isAllPayments() {
        return allPayments;
    }

    public void setAllPayments(boolean allPayments) {
        this.allPayments = allPayments;
    }

    public boolean isNotReleased() {
        return notReleased;
    }

    public void setNotReleased(boolean notReleased) {
        this.notReleased = notReleased;
    }

    public String getDsoFilter() {
        return dsoFilter;
    }

    public void setDsoFilter(String dsoFilter) {
        this.dsoFilter = dsoFilter;
    }

    public String getDsoPeriod() {
        return dsoPeriod;
    }

    public void setDsoPeriod(String dsoPeriod) {
        this.dsoPeriod = dsoPeriod;
    }

    public String getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getAccountAssignedTo() {
        return accountAssignedTo;
    }

    public void setAccountAssignedTo(String accountAssignedTo) {
        this.accountAssignedTo = accountAssignedTo;
    }

    public String getNotesEnteredBy() {
        return notesEnteredBy;
    }

    public void setNotesEnteredBy(String notesEnteredBy) {
        this.notesEnteredBy = notesEnteredBy;
    }

    public TerminalModel getTerminalManager() {
        if (null == terminalManager) {
            terminalManager = new TerminalModel();
        }
        return terminalManager;
    }

    public void setTerminalManager(TerminalModel terminalManager) {
        this.terminalManager = terminalManager;
    }

    public SalesModel getSalesManager() {
        if (null == salesManager) {
            salesManager = new SalesModel();
        }
        return salesManager;
    }

    public void setSalesManager(SalesModel salesManager) {
        this.salesManager = salesManager;
    }

    public List<LabelValueBean> getCollectors() {
        return new UserDAO().getCollectors();
    }

    public List<TerminalModel> getTerminalManagers() throws Exception {
        return new TerminalManagerDao().getTerminalManagers();
    }

    public List<SalesModel> getSalesManagers() {
        return new GenericCodeDAO().getSalesManagers();
    }

    public String getInternationalCollector() {
        return internationalCollector;
    }

    public void setInternationalCollector(String internationalCollector) {
        this.internationalCollector = internationalCollector;
    }

    
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.allCustomers = false;
        this.top10Customers = false;
        this.eculine = false;
        this.creditInvoice = false;
        this.creditStatement = false;
        this.coverLetter = false;
        this.ap = false;
        this.ac = false;
    }
}
