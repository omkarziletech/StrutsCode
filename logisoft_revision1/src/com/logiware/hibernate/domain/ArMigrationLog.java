package com.logiware.hibernate.domain;

import com.logiware.hibernate.dao.ArMigrationLogDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Lakshmi Narayanan
 */
@Entity()
@Table(name = "ar_migration_log")
@org.hibernate.annotations.DynamicInsert(true) 
@org.hibernate.annotations.DynamicUpdate(true)
public class ArMigrationLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "blue_screen_account", length = 10)
    private String blueScreenAccount;
    @Column(name = "customer_number", length = 10)
    private String customerNumber;
    @Column(name = "bl_number", length = 100)
    private String blNumber;
    @Column(name = "invoice_number", length = 100)
    private String invoiceNumber;
    @Column(name = "log_type", length = 20)
    private String logType;
    @Lob
    @Column(name = "error", length = 2147483647)
    private String error;
    @Column(name = "reported_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportedDate;
    @Column(name = "file_name", length = 100)
    private String fileName;
    @Column(name = "file_line_number")
    private Integer fileLineNumber;
    @Column(name = "no_of_reprocess")
    private Integer noOfReprocess;
    @Column(name = "ar_loaded")
    private boolean arLoaded;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arMigrationLog")
    private List<ArMigrationReprocessLog> arMigrationReprocessLogs;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getBlueScreenAccount() {
	return blueScreenAccount;
    }

    public void setBlueScreenAccount(String blueScreenAccount) {
	this.blueScreenAccount = blueScreenAccount;
    }

    public String getCustomerNumber() {
	return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
	this.customerNumber = customerNumber;
    }

    public String getBlNumber() {
	return blNumber;
    }

    public void setBlNumber(String blNumber) {
	this.blNumber = blNumber;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public String getLogType() {
	return logType;
    }

    public void setLogType(String logType) {
	this.logType = logType;
    }

    public String getError() {
	return error;
    }

    public void setError(String error) {
	this.error = error;
    }

    public Date getReportedDate() {
	return reportedDate;
    }

    public void setReportedDate(Date reportedDate) {
	this.reportedDate = reportedDate;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public Integer getFileLineNumber() {
	return fileLineNumber;
    }

    public void setFileLineNumber(Integer fileLineNumber) {
	this.fileLineNumber = fileLineNumber;
    }

    public Integer getNoOfReprocess() {
	return noOfReprocess;
    }

    public void setNoOfReprocess(Integer noOfReprocess) {
	this.noOfReprocess = noOfReprocess;
    }

    public boolean isArLoaded() {
	return arLoaded;
    }

    public void setArLoaded(boolean arLoaded) {
	this.arLoaded = arLoaded;
    }

    public List<ArMigrationReprocessLog> getArMigrationReprocessLogs() {
	return arMigrationReprocessLogs;
    }

    public void setArMigrationReprocessLogs(List<ArMigrationReprocessLog> arMigrationReprocessLogs) {
	this.arMigrationReprocessLogs = arMigrationReprocessLogs;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof ArMigrationLog)) {
	    return false;
	}
	ArMigrationLog other = (ArMigrationLog) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.hibernate.domain.ArMigrationLog[ id=" + id + " ]";
    }

    public void save() throws Exception {
	new ArMigrationLogDAO().save(this);
    }

    public void update() throws Exception {
	new ArMigrationLogDAO().update(this);
    }

    public void delete() throws Exception {
	new ArMigrationLogDAO().delete(this);
    }
}
