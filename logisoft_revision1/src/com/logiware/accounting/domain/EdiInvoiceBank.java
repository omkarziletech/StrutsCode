package com.logiware.accounting.domain;

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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "edi_invoice_bank")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EdiInvoiceBank implements Serializable {

    private static final long serialVersionUID = 4882864806559183092L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "street1")
    private String street1;
    @Column(name = "street2")
    private String street2;
    @Column(name = "zip")
    private String zip;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "un_code")
    private String unCode;
    @Column(name = "account")
    private String account;
    @Column(name = "iban")
    private String iban;
    @Column(name = "bic")
    private String bic;
    @JoinColumn(name = "edi_invoice_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EdiInvoice ediInvoice;

    public EdiInvoiceBank() {
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getStreet1() {
	return street1;
    }

    public void setStreet1(String street1) {
	this.street1 = street1;
    }

    public String getStreet2() {
	return street2;
    }

    public void setStreet2(String street2) {
	this.street2 = street2;
    }

    public String getZip() {
	return zip;
    }

    public void setZip(String zip) {
	this.zip = zip;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getUnCode() {
	return unCode;
    }

    public void setUnCode(String unCode) {
	this.unCode = unCode;
    }

    public String getAccount() {
	return account;
    }

    public void setAccount(String account) {
	this.account = account;
    }

    public String getIban() {
	return iban;
    }

    public void setIban(String iban) {
	this.iban = iban;
    }

    public String getBic() {
	return bic;
    }

    public void setBic(String bic) {
	this.bic = bic;
    }

    public EdiInvoice getEdiInvoice() {
	return ediInvoice;
    }

    public void setEdiInvoice(EdiInvoice ediInvoice) {
	this.ediInvoice = ediInvoice;
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
	if (!(object instanceof EdiInvoiceBank)) {
	    return false;
	}
	EdiInvoiceBank other = (EdiInvoiceBank) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.accounting.domain.EdiInvoiceBank[ id=" + id + " ]";
    }
}
