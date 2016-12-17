/**
 * 
 */
package com.gp.cong.logisoft.struts.form;

import org.apache.struts.action.ActionForm;

/**
 * @author Rohith
 *
 */
public class ConfigurationForm extends ActionForm {
	/*
	 * Generated Methods
	 */
    private String usecase;
    private String name;
    private String testFlowFrom;
    private String dataExchange;
	private String buttonValue;
	private Integer usecaseId;
	private Integer index;	
	public Integer getUsecaseId() {
		return usecaseId;
	}
	public void setUsecaseId(Integer usecaseId) {
		this.usecaseId = usecaseId;
	}
	public String getButtonValue() {
		return buttonValue;
	}
	public void setButtonValue(String buttonValue) {
		this.buttonValue = buttonValue;
	}
	public String getDataExchange() {
		return dataExchange;
	}
	public void setDataExchange(String dataExchange) {
		this.dataExchange = dataExchange;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsecase() {
		return usecase;
	}
	public void setUsecase(String usecase) {
		this.usecase = usecase;
	}
	public String getTestFlowFrom() {
		return testFlowFrom;
	}
	public void setTestFlowFrom(String testFlowFrom) {
		this.testFlowFrom = testFlowFrom;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	

}
