/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.hibernate.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "accrual_migration_reprocess_log")
public class AccrualMigrationReprocessLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "log_type")
    private String logType;
    @Lob
    @Column(name = "error")
    private String error;
    @Column(name = "reported_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportedDate;
    @JoinColumn(name = "accrual_migration_log_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AccrualMigrationLog accrualMigrationLog;

    public AccrualMigrationReprocessLog() {
    }

    public AccrualMigrationReprocessLog(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
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

    public AccrualMigrationLog getAccrualMigrationLog() {
	return accrualMigrationLog;
    }

    public void setAccrualMigrationLog(AccrualMigrationLog accrualMigrationLog) {
	this.accrualMigrationLog = accrualMigrationLog;
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
	if (!(object instanceof AccrualMigrationReprocessLog)) {
	    return false;
	}
	AccrualMigrationReprocessLog other = (AccrualMigrationReprocessLog) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.luckyboy.AccrualMigrationReprocessLog[ id=" + id + " ]";
    }
    
}
