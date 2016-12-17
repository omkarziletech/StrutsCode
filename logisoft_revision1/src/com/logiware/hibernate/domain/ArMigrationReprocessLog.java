package com.logiware.hibernate.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Lakshmi Narayanan
 */
@Entity
@Table(name = "ar_migration_reprocess_log")
public class ArMigrationReprocessLog implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Integer id;
  @Column(name = "log_type", length = 20)
  private String logType;
  @Lob
  @Column(name = "error", length = 2147483647)
  private String error;
  @Column(name = "reported_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date reportedDate;
  @JoinColumn(name = "ar_migration_log_id", referencedColumnName = "id", nullable = false)
  @ManyToOne(optional = false)
  private ArMigrationLog arMigrationLog;

  public ArMigrationReprocessLog() {
  }

  public ArMigrationReprocessLog(Integer id) {
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

  public ArMigrationLog getArMigrationLog() {
    return arMigrationLog;
  }

  public void setArMigrationLog(ArMigrationLog arMigrationLog) {
    this.arMigrationLog = arMigrationLog;
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
    if (!(object instanceof ArMigrationReprocessLog)) {
      return false;
    }
    ArMigrationReprocessLog other = (ArMigrationReprocessLog) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.logiware.hibernate.domain.ArMigrationReprocessLog[ id=" + id + " ]";
  }
}
