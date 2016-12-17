/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.form;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Ramasamy D
 */
public class EventForm extends ActionForm {
    private String moduleId;
    private String moduleRefId;
    private String eventCode;
    private String eventDate;
    private String eventDesc;
    private String eventBy;

    public String getEventBy() {
        return eventBy;
    }

    public void setEventBy(String eventBy) {
        this.eventBy = eventBy;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleRefId() {
        return moduleRefId;
    }

    public void setModuleRefId(String moduleRefId) {
        this.moduleRefId = moduleRefId;
    }

    public String getEventCode() {
        return this.eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}
}
