package com.logiware.common.form;

import com.logiware.common.dao.JobDAO;
import com.logiware.common.domain.Job;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class JobForm extends BaseForm {

    private Long id;
    private String frequency;
    private int day1;
    private int day2;
    private int hour;
    private int minute;
    private boolean enabled;
    private String fromDate;
    private String toDate;
    private String status;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getFrequency() {
	return frequency;
    }

    public void setFrequency(String frequency) {
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

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public List<Job> getJobs() {
	return new JobDAO().getAllJobs();
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	this.enabled = false;
    }
}
