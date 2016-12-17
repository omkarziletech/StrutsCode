package com.logiware.common.model;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ProgressBar {

    private String message;
    private String status;
    private Integer percentage;

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Integer getPercentage() {
	return percentage;
    }

    public void setPercentage(Integer percentage) {
	this.percentage = percentage;
    }

}
