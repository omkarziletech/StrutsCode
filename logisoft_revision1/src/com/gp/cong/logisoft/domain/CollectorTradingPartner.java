package com.gp.cong.logisoft.domain;

public class CollectorTradingPartner implements java.io.Serializable {

    private Integer id;
    private User user;
    private String startRange;
    private String endRange;
    private boolean applyToConsigneeOnlyAccounts;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getEndRange() {
	return endRange;
    }

    public void setEndRange(String endRange) {
	this.endRange = endRange;
    }

    public String getStartRange() {
	return startRange;
    }

    public void setStartRange(String startRange) {
	this.startRange = startRange;
    }

    public boolean isApplyToConsigneeOnlyAccounts() {
	return applyToConsigneeOnlyAccounts;
    }

    public void setApplyToConsigneeOnlyAccounts(boolean applyToConsigneeOnlyAccounts) {
	this.applyToConsigneeOnlyAccounts = applyToConsigneeOnlyAccounts;
    }
}
