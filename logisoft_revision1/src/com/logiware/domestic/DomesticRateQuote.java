package com.logiware.domestic;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Shanmugam
 */
@Entity
@Table(name = "domestic_rate_quote")
public class DomesticRateQuote extends Domain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "shipment_id")
    private String shipmentId;
    @Column(name = "ship_date")
    @Temporal(TemporalType.DATE)
    private Date shipDate;
    @Column(name = "origin_city")
    private String originCity;
    @Column(name = "destination_city")
    private String destinationCity;
    @Column(name = "origin_zip")
    private String originZip;
    @Column(name = "destination_zip")
    private String destinationZip;
    @Column(name = "origin_state")
    private String originState;
    @Column(name = "destination_state")
    private String destinationState;
    @Column(name = "miles")
    private String miles;
    @Column(name = "unit")
    private String unit;
    @Column(name = "cube")
    private Double cube;
    @Column(name = "rate_on")
    @Temporal(TemporalType.DATE)
    private Date rateOn;
    @JoinColumn(name = "rate_by", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User rateBy;
    @Column(name = "booked_on")
    @Temporal(TemporalType.DATE)
    private Date bookedOn;

    public Double getCube() {
        return cube;
    }

    public void setCube(Double cube) {
        this.cube = cube;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    public String getDestinationZip() {
        return destinationZip;
    }

    public void setDestinationZip(String destinationZip) {
        this.destinationZip = destinationZip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getOriginState() {
        return originState;
    }

    public void setOriginState(String originState) {
        this.originState = originState;
    }

    public String getOriginZip() {
        return originZip;
    }

    public void setOriginZip(String originZip) {
        this.originZip = originZip;
    }

    public User getRateBy() {
        return rateBy;
    }

    public void setRateBy(User rateBy) {
        this.rateBy = rateBy;
    }

    public Date getRateOn() {
        return rateOn;
    }

    public void setRateOn(Date rateOn) {
        this.rateOn = rateOn;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(Date bookedOn) {
        this.bookedOn = bookedOn;
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
	if (!(object instanceof DomesticRateQuote)) {
	    return false;
	}
	DomesticRateQuote other = (DomesticRateQuote) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }
}
