package com.logiware.form;

import com.logiware.bean.AccountingBean;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ControlReportForm extends org.apache.struts.action.ActionForm {

    private static final long serialVersionUID = -5606327893208814402L;
    private String action;
    private String createdDate;
    private String fromDate;
    private String toDate;
    private String reportType = "AC";
    private List<AccountingBean> logiwareAccruals;
    private List<AccountingBean> blueScreenAccruals;
    private List<AccountingBean> logiwareAccountReceivables;
    private List<AccountingBean> blueScreenAccountReceivables;
    private Double totalAmountInLogiware;
    private Double totalAmountInBlueScreen;
    private Integer numberOfLogiwareRecords;
    private Integer numberOfBluScreenRecords;
    private String fileName;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<AccountingBean> getBlueScreenAccountReceivables() {
        return blueScreenAccountReceivables;
    }

    public void setBlueScreenAccountReceivables(List<AccountingBean> blueScreenAccountReceivables) {
        this.blueScreenAccountReceivables = blueScreenAccountReceivables;
    }

    public List<AccountingBean> getBlueScreenAccruals() {
        return blueScreenAccruals;
    }

    public void setBlueScreenAccruals(List<AccountingBean> blueScreenAccruals) {
        this.blueScreenAccruals = blueScreenAccruals;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<AccountingBean> getLogiwareAccountReceivables() {
        return logiwareAccountReceivables;
    }

    public void setLogiwareAccountReceivables(List<AccountingBean> logiwareAccountReceivables) {
        this.logiwareAccountReceivables = logiwareAccountReceivables;
    }

    public List<AccountingBean> getLogiwareAccruals() {
        return logiwareAccruals;
    }

    public void setLogiwareAccruals(List<AccountingBean> logiwareAccruals) {
        this.logiwareAccruals = logiwareAccruals;
    }

    public Integer getNumberOfBluScreenRecords() {
        return numberOfBluScreenRecords;
    }

    public void setNumberOfBluScreenRecords(Integer numberOfBluScreenRecords) {
        this.numberOfBluScreenRecords = numberOfBluScreenRecords;
    }

    public Integer getNumberOfLogiwareRecords() {
        return numberOfLogiwareRecords;
    }

    public void setNumberOfLogiwareRecords(Integer numberOfLogiwareRecords) {
        this.numberOfLogiwareRecords = numberOfLogiwareRecords;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Double getTotalAmountInBlueScreen() {
        return totalAmountInBlueScreen;
    }

    public void setTotalAmountInBlueScreen(Double totalAmountInBlueScreen) {
        this.totalAmountInBlueScreen = totalAmountInBlueScreen;
    }

    public Double getTotalAmountInLogiware() {
        return totalAmountInLogiware;
    }

    public void setTotalAmountInLogiware(Double totalAmountInLogiware) {
        this.totalAmountInLogiware = totalAmountInLogiware;
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
}
