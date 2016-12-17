package com.logiware.bean;

import java.io.Serializable;

/**
 *
 * @author Lakshminarayanan
 */
public class AchProcessInfo implements Serializable {

    private static final long serialVersionUID = -5287305259895847122L;
    private String status;
    private String message;
    private Integer progressCount;

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

    public Integer getProgressCount() {
	return progressCount;
    }

    public void setProgressCount(Integer progressCount) {
	this.progressCount = progressCount;
    }

}
