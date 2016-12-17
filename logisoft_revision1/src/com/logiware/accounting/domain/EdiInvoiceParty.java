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
@Table(name = "edi_invoice_party")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EdiInvoiceParty implements Serializable {

    private static final long serialVersionUID = -2145550168240291224L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "street")
    private String street;
    @Column(name = "zip")
    private String zip;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;
    @Column(name = "vat_number")
    private String vatNumber;
    @Column(name = "registration_number")
    private String registrationNumber;
    @Column(name = "license_number")
    private String licenseNumber;
    @Basic(optional = false)
    @Column(name = "type", nullable = false)
    private String type;
    @JoinColumn(name = "edi_invoice_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EdiInvoice ediInvoice;

    public EdiInvoiceParty() {
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

    public String getStreet() {
	return street;
    }

    public void setStreet(String street) {
	this.street = street;
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

    public String getVatNumber() {
	return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
	this.vatNumber = vatNumber;
    }

    public String getRegistrationNumber() {
	return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
	this.registrationNumber = registrationNumber;
    }

    public String getLicenseNumber() {
	return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
	this.licenseNumber = licenseNumber;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
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
	if (!(object instanceof EdiInvoiceParty)) {
	    return false;
	}
	EdiInvoiceParty other = (EdiInvoiceParty) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.accounting.domain.EdiInvoiceParty[ id=" + id + " ]";
    }
}
