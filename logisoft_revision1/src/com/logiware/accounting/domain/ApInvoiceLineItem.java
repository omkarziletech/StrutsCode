package com.logiware.accounting.domain;

import com.logiware.accounting.model.LineItemModel;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "ap_invoice_line_item")
public class ApInvoiceLineItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "gl_account")
    private String glAccount;
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private double amount;
    @JoinColumn(name = "ap_invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ApInvoice apInvoice;

    public ApInvoiceLineItem() {
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
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

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public ApInvoice getApInvoice() {
	return apInvoice;
    }

    public void setApInvoice(ApInvoice apInvoice) {
	this.apInvoice = apInvoice;
    }

    public ApInvoiceLineItem(LineItemModel lineItem) {
	this.glAccount = lineItem.getGlAccount();
	this.description = lineItem.getDescription();
	this.amount = Double.parseDouble(lineItem.getAmount().replace(",", ""));
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
	if (!(object instanceof ApInvoiceLineItem)) {
	    return false;
	}
	ApInvoiceLineItem other = (ApInvoiceLineItem) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.accounting.domain.ApInvoiceLineItem[ id=" + id + " ]";
    }
}
