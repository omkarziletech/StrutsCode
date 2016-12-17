package com.logiware.common.domain;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.logiware.common.constants.ScheduleFrequency;
import com.logiware.common.constants.ReportType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 *
 * @author Lakshmi Narayanan
 */
@Entity
@Table(name = "report")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "report_name", nullable = false)
    private String reportName;
    @Basic(optional = false)
    @Column(name = "report_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "com.logiware.common.usertype.GenericEnumUserType",
    parameters = {
	@Parameter(
                    name = "enumClass",
	value = "com.logiware.common.constants.ReportType"),
	@Parameter(
                    name = "identifierMethod",
	value = "toString"),
	@Parameter(
                    name = "valueOfMethod",
	value = "fromString")
    })
    private ReportType reportType;
    @Basic(optional = false)
    @Column(name = "schedule_frequency", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "com.logiware.common.usertype.GenericEnumUserType",
    parameters = {
	@Parameter(
                    name = "enumClass",
	value = "com.logiware.common.constants.ScheduleFrequency"),
	@Parameter(
                    name = "identifierMethod",
	value = "toString"),
	@Parameter(
                    name = "valueOfMethod",
	value = "fromString")
    })
    private ScheduleFrequency scheduleFrequency;
    @Basic(optional = false)
    @Column(name = "schedule_day1", nullable = false)
    private int scheduleDay1 = 1;
    @Basic(optional = false)
    @Column(name = "schedule_day2", nullable = false)
    private int scheduleDay2 = 1;
    @Basic(optional = false)
    @Column(name = "schedule_time", nullable = false)
    private String scheduleTime;
    @Basic(optional = false)
    @Column(name = "header", nullable = false)
    private boolean header;
    @Basic(optional = false)
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Basic(optional = false)
    @Column(name = "sender", nullable = false)
    private Integer sender;
    @Column(name = "email_id")
    private String emailId;
    @Column(name = "email_body")
    private String emailBody;
    @Basic(optional = false)
    @Column(name = "query1", nullable = false)
    private String query1;
    @Column(name = "query2")
    private String query2;
    @Basic(optional = false)
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Basic(optional = false)
    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    public Report() {
    }

    public Report(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getReportName() {
	return null != reportName ? reportName.toUpperCase() : null;
    }

    public void setReportName(String reportName) {
	this.reportName = reportName;
    }

    public ReportType getReportType() {
	return reportType;
    }

    public void setReportType(ReportType reportType) {
	this.reportType = reportType;
    }

    public ScheduleFrequency getScheduleFrequency() {
	return scheduleFrequency;
    }

    public void setScheduleFrequency(ScheduleFrequency scheduleFrequency) {
	this.scheduleFrequency = scheduleFrequency;
    }

    public int getScheduleDay1() {
	return scheduleDay1;
    }

    public void setScheduleDay1(int scheduleDay1) {
	this.scheduleDay1 = scheduleDay1;
    }

    public int getScheduleDay2() {
	return scheduleDay2;
    }

    public void setScheduleDay2(int scheduleDay2) {
	this.scheduleDay2 = scheduleDay2;
    }

    public String getScheduleTime() {
	return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
	this.scheduleTime = scheduleTime;
    }

    public boolean isHeader() {
	return header;
    }

    public void setHeader(boolean header) {
	this.header = header;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    public Integer getSender() {
	return sender;
    }

    public void setSender(Integer sender) {
	this.sender = sender;
    }

    public String getEmailId() {
	return emailId;
    }

    public void setEmailId(String emailId) {
	this.emailId = emailId;
    }

    public String getEmailBody() {
	return emailBody;
    }

    public void setEmailBody(String emailBody) {
	this.emailBody = emailBody;
    }

    public String getQuery1() {
	return query1;
    }

    public void setQuery1(String query1) {
	this.query1 = query1;
    }

    public String getQuery2() {
	return query2;
    }

    public void setQuery2(String query2) {
	this.query2 = query2;
    }

    public Date getCreatedDate() {
	return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
    }

    public String getCreatedBy() {
	return null != createdBy ? createdBy.toUpperCase() : null;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Date getUpdatedDate() {
	return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
	this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
	return null != updatedBy ? updatedBy.toUpperCase() : null;
    }

    public void setUpdatedBy(String updatedBy) {
	this.updatedBy = updatedBy;
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
	if (!(object instanceof Report)) {
	    return false;
	}
	Report other = (Report) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.common.domain.Report[ id=" + id + " ]";
    }
    private transient String reportTypeValue;
    private transient String scheduleFrequencyValue;
    private transient String createdDateValue;
    private transient String senderName;

    public String getReportTypeValue() {
	return reportTypeValue;
    }

    public void setReportTypeValue(String reportTypeValue) {
	this.reportTypeValue = reportTypeValue;
	this.reportType = ReportType.fromString(reportTypeValue);
    }

    public String getScheduleFrequencyValue() {
	return scheduleFrequencyValue;
    }

    public void setScheduleFrequencyValue(String scheduleFrequencyValue) {
	this.scheduleFrequencyValue = scheduleFrequencyValue;
	this.scheduleFrequency = ScheduleFrequency.fromString(scheduleFrequencyValue);
    }

    public String getCreatedDateValue() {
	return createdDateValue;
    }

    public void setCreatedDateValue(String createdDateValue) throws Exception {
	this.createdDateValue = createdDateValue;
	this.createdDate = DateUtils.parseDate(createdDateValue, "MM/dd/yyyy hh:mm:ss a");
    }

    public String getSenderName() {
	if (CommonUtils.isNotEmpty(sender)) {
	    senderName = new UserDAO().getLoginName(sender);
	}
	return senderName;
    }

    public void setSenderName(String senderName) {
	this.senderName = senderName;
    }
}
