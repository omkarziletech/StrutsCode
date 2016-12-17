package com.logiware.domestic;

import com.gp.cong.hibernate.Domain;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Shanmugam
 */
@Entity
@Table(name = "domestic_charges")
public class DomesticCharges extends Domain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "charge_code")
    private String chargeCode;
    @Column(name = "charge_amount")
    private double chargeAmount;
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DomesticBooking bookingId;
    @JoinColumn(name = "rate_carrier_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DomesticRateCarrier domesticRateCarrier;

    public DomesticBooking getBookingId() {
        return bookingId;
    }

    public void setBookingId(DomesticBooking bookingId) {
        this.bookingId = bookingId;
    }

    public double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DomesticRateCarrier getDomesticRateCarrier() {
        return domesticRateCarrier;
    }

    public void setDomesticRateCarrier(DomesticRateCarrier domesticRateCarrier) {
        this.domesticRateCarrier = domesticRateCarrier;
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
	if (!(object instanceof DomesticCharges)) {
	    return false;
	}
	DomesticCharges other = (DomesticCharges) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }
}
