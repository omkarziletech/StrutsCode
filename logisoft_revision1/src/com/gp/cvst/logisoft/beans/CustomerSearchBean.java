package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class CustomerSearchBean implements Serializable {

	private String accountNameBean;
	private String accountNumBean;
	public String getAccountNameBean() {
		return accountNameBean;
	}
	public void setAccountNameBean(String accountNameBean) {
		this.accountNameBean = accountNameBean;
	}
	public String getAccountNumBean() {
		return accountNumBean;
	}
	public void setAccountNumBean(String accountNumBean) {
		this.accountNumBean = accountNumBean;
	}
	
	
	
}
