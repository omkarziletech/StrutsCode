package com.logiware.common.form;

import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.logiware.common.domain.Report;
import com.logiware.utils.ListUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ReportForm extends BaseForm {

    private Report report;
    private List<Report> reports;
    private String id;
    private String fileName;
    private String fromDate;
    private String toDate;
    private String billingTerminal;
    private String destinationRegions;
    private String dateRange="sailDate";
    private String includeBookings;


    public Report getReport() {
        if (null == report) {
            report = new Report();
        }
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public String getDestinationRegions() throws Exception {
        return destinationRegions;
    }

    public void setDestinationRegions(String destinationRegions) {
        this.destinationRegions = destinationRegions;
    }

    public List<String> getTerminals() {
        return new RefTerminalDAO().getAllTerminalsForCOBReport();
    }

    public List<LabelValueBean> getRegions() throws Exception {
        return new GenericCodeDAO().getRegions();
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getIncludeBookings() {
        return includeBookings;
    }

    public void setIncludeBookings(String includeBookings) {
        this.includeBookings = includeBookings;
    }
    

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        getReport().setHeader(false);
        getReport().setEnabled(false);
    }
}
