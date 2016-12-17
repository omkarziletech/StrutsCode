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
 * @author Lakshmi Narayanan
 */
@Entity
@Table(name = "edi_invoice_container")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EdiInvoiceContainer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "container_number")
    private String containerNumber;
    @JoinColumn(name = "edi_invoice_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EdiInvoice ediInvoice;

    public EdiInvoiceContainer() {
    }

    public EdiInvoiceContainer(String containerNumber, EdiInvoice ediInvoice) {
	this.containerNumber = containerNumber;
	this.ediInvoice = ediInvoice;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getContainerNumber() {
	return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
	this.containerNumber = containerNumber;
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
	if (!(object instanceof EdiInvoiceContainer)) {
	    return false;
	}
	EdiInvoiceContainer other = (EdiInvoiceContainer) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "lucky.EdiInvoiceContainer[ id=" + id + " ]";
    }
}
