package com.logiware.accounting.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "edi_invoice_shipping_details")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EdiInvoiceShippingDetails implements Serializable {

    private static final long serialVersionUID = -5410762499170751162L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "vessel", nullable = false)
    private String vessel;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "routing")
    private String routing;
    @Column(name = "package_quantity")
    private String packageQuantity;
    @Column(name = "package_description")
    private String packageDescription;
    @Column(name = "weight")
    private String weight;
    @Column(name = "volume")
    private String volume;
    @JoinColumn(name = "edi_invoice_id", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private EdiInvoice ediInvoice;

    public EdiInvoiceShippingDetails() {
    }

    public EdiInvoiceShippingDetails(Integer id) {
	this.id = id;
    }

    public EdiInvoiceShippingDetails(Integer id, String vessel) {
	this.id = id;
	this.vessel = vessel;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getVessel() {
	return vessel;
    }

    public void setVessel(String vessel) {
	this.vessel = vessel;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public String getRouting() {
	return routing;
    }

    public void setRouting(String routing) {
	this.routing = routing;
    }

    public String getPackageQuantity() {
	return packageQuantity;
    }

    public void setPackageQuantity(String packageQuantity) {
	this.packageQuantity = packageQuantity;
    }

    public String getPackageDescription() {
	return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
	this.packageDescription = packageDescription;
    }

    public String getWeight() {
	return weight;
    }

    public void setWeight(String weight) {
	this.weight = weight;
    }

    public String getVolume() {
	return volume;
    }

    public void setVolume(String volume) {
	this.volume = volume;
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
	if (!(object instanceof EdiInvoiceShippingDetails)) {
	    return false;
	}
	EdiInvoiceShippingDetails other = (EdiInvoiceShippingDetails) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.accounting.domain.EdiInvoiceShippingDetails[ id=" + id + " ]";
    }
}
