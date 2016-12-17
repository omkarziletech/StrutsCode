package com.logiware.common.domain;

import com.logiware.common.constants.Frequency;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 *
 * @author Lakshmi Narayanan
 */
@Entity
@Table(name = "job")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @Basic(optional = false)
    @Column(name = "frequency", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "com.logiware.common.usertype.GenericEnumUserType",
	    parameters = {
	@Parameter(
		name = "enumClass",
		value = "com.logiware.common.constants.Frequency"),
	@Parameter(
		name = "identifierMethod",
		value = "toString"),
	@Parameter(
		name = "valueOfMethod",
		value = "fromString")
    })
    private Frequency frequency;
    @Basic(optional = false)
    @Column(name = "day1", nullable = false)
    private int day1 = 1;
    @Basic(optional = false)
    @Column(name = "day2", nullable = false)
    private int day2 = 1;
    @Basic(optional = false)
    @Column(name = "hour", nullable = false)
    private int hour = 1;
    @Basic(optional = false)
    @Column(name = "minute", nullable = false)
    private int minute = 1;
    @Basic(optional = false)
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Basic(optional = false)
    @Column(name = "class_name", nullable = false)
    private String className;

    public Job() {
    }

    public Job(Long id) {
	this.id = id;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Frequency getFrequency() {
	return frequency;
    }

    public void setFrequency(Frequency frequency) {
	this.frequency = frequency;
    }

    public int getDay1() {
	return day1;
    }

    public void setDay1(int day1) {
	this.day1 = day1;
    }

    public int getDay2() {
	return day2;
    }

    public void setDay2(int day2) {
	this.day2 = day2;
    }

    public int getHour() {
	return hour;
    }

    public void setHour(int hour) {
	this.hour = hour;
    }

    public int getMinute() {
	return minute;
    }

    public void setMinute(int minute) {
	this.minute = minute;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    public Date getStartTime() {
	return startTime;
    }

    public void setStartTime(Date startTime) {
	this.startTime = startTime;
    }

    public Date getEndTime() {
	return endTime;
    }

    public void setEndTime(Date endTime) {
	this.endTime = endTime;
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

    public String getClassName() {
	return className;
    }

    public void setClassName(String className) {
	this.className = className;
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
	if (!(object instanceof Job)) {
	    return false;
	}
	Job other = (Job) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.common.domain.Job[ id=" + id + " ]";
    }
    public static final String PRINT = "Print";
    public static final String FAX = "Fax";
    public static final String EMAIL = "Email";
    public static final String ACH = "ACH";
    public static final String REMOVE_LOCKS = "Remove Locks";
    public static final String FOLLOW_UP_NOTES = "Follow Up Notes";
}
