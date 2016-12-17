package com.logiware.common.form;

import com.logiware.common.model.DbMonitorModel;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class DbMonitorForm extends BaseForm {

    private String id;
    private List<DbMonitorModel> processes;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public List<DbMonitorModel> getProcesses() {
	return processes;
    }

    public void setProcesses(List<DbMonitorModel> processes) {
	this.processes = processes;
    }
}
