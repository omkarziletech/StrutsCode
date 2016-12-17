package com.logiware.accounting.model;

import com.gp.cong.common.NumberUtils;
import com.logiware.accounting.domain.ApInvoiceLineItem;

/**
 *
 * @author Lakshmi Naryanan
 */
public class LineItemModel {

    private String id;
    private String glAccount;
    private String description;
    private String amount = "0.00";

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getGlAccount() {
	return glAccount;
    }

    public void setGlAccount(String glAccount) {
	this.glAccount = glAccount;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getAmount() {
	return amount;
    }

    public void setAmount(String amount) {
	this.amount = amount;
    }

    public LineItemModel() {
    }

    public LineItemModel(ApInvoiceLineItem lineItem) {
	this.id = lineItem.getId().toString();
	this.glAccount = lineItem.getGlAccount();
	this.description = lineItem.getDescription();
	this.amount = NumberUtils.formatNumber(lineItem.getAmount());
    }
}
