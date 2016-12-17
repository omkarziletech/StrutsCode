package com.logiware.hibernate.domain;

import com.logiware.hibernate.dao.AccrualMigrationLogDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "accrual_migration_log")
public class AccrualMigrationLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "bluescreen_vendor")
    private String bluescreenVendor;
    @Column(name = "vendor_number")
    private String vendorNumber;
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "bl_number")
    private String blNumber;
    @Column(name = "amount")
    private Double amount = 0d;
    @Column(name = "dock_receipt")
    private String dockReceipt;
    @Column(name = "voyage_number")
    private String voyageNumber;
    @Column(name = "container_number")
    private String containerNumber;
    @Column(name = "bluescreen_key")
    private String bluescreenKey;
    @Column(name = "log_type")
    private String logType;
    @Lob
    @Column(name = "error")
    private String error;
    @Column(name = "reported_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportedDate;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "no_of_reprocess")
    private Integer noOfReprocess=0;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accrualMigrationLog", fetch = FetchType.LAZY)
    private List<AccrualMigrationReprocessLog> accrualMigrationReprocessLogs;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accrualMigrationLog", fetch = FetchType.LAZY)
    private AccrualMigrationErrorFile accrualMigrationErrorFile;

    public AccrualMigrationLog() {
    }

    public AccrualMigrationLog(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getBluescreenVendor() {
	return bluescreenVendor;
    }

    public void setBluescreenVendor(String bluescreenVendor) {
	this.bluescreenVendor = bluescreenVendor;
    }

    public String getVendorNumber() {
	return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
	this.vendorNumber = vendorNumber;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public String getBlNumber() {
	return blNumber;
    }

    public void setBlNumber(String blNumber) {
	this.blNumber = blNumber;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

    public String getDockReceipt() {
	return dockReceipt;
    }

    public void setDockReceipt(String dockReceipt) {
	this.dockReceipt = dockReceipt;
    }

    public String getVoyageNumber() {
	return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
	this.voyageNumber = voyageNumber;
    }

    public String getContainerNumber() {
	return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
	this.containerNumber = containerNumber;
    }

    public String getBluescreenKey() {
	return bluescreenKey;
    }

    public void setBluescreenKey(String bluescreenKey) {
	this.bluescreenKey = bluescreenKey;
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

    public Integer getNoOfReprocess() {
	return noOfReprocess;
    }

    public void setNoOfReprocess(Integer noOfReprocess) {
	this.noOfReprocess = noOfReprocess;
    }

    public List<AccrualMigrationReprocessLog> getAccrualMigrationReprocessLogs() {
	return accrualMigrationReprocessLogs;
    }

    public void setAccrualMigrationReprocessLogs(List<AccrualMigrationReprocessLog> accrualMigrationReprocessLogs) {
	this.accrualMigrationReprocessLogs = accrualMigrationReprocessLogs;
    }

    public AccrualMigrationErrorFile getAccrualMigrationErrorFile() {
	return accrualMigrationErrorFile;
    }

    public void setAccrualMigrationErrorFile(AccrualMigrationErrorFile accrualMigrationErrorFile) {
	this.accrualMigrationErrorFile = accrualMigrationErrorFile;
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
	if (!(object instanceof AccrualMigrationLog)) {
	    return false;
	}
	AccrualMigrationLog other = (AccrualMigrationLog) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.luckyboy.AccrualMigrationLog[ id=" + id + " ]";
    }

    public void save() throws Exception {
	new AccrualMigrationLogDAO().save(this);
    }

    public void update() throws Exception {
	new AccrualMigrationLogDAO().update(this);
    }

    public void delete() throws Exception {
	new AccrualMigrationLogDAO().delete(this);
    }
}
