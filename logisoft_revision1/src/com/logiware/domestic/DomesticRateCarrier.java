package com.logiware.domestic;

import com.gp.cong.hibernate.Domain;
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
@Table(name = "domestic_rate_carrier")
public class DomesticRateCarrier extends Domain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "carrier_name")
    private String carrierName;
    @Column(name = "direct_interline")
    private String directInterline;
    @Column(name = "estimated_days")
    private Integer estimatedDays;
    @Column(name = "type")
    private String type;
    @Column(name = "line_hual")
    private Double lineHual;
    @Column(name = "fuel_charge")
    private Double fuelCharge;
    @Column(name = "extra_charge")
    private Double extraCharge;
    @Column(name = "final_charge")
    private Double finalCharge;
    @Column(name = "scac")
    private String scac;
    @Column(name = "origin_code")
    private String originCode;
    @Column(name = "origin_name")
    private String originName;
    @Column(name = "origin_address")
    private String originAddress;
    @Column(name = "origin_city")
    private String originCity;
    @Column(name = "origin_state")
    private String originState;
    @Column(name = "origin_zip")
    private String originZip;
    @Column(name = "origin_phone")
    private String originPhone;
    @Column(name = "origin_fax")
    private String originFax;
    @Column(name = "destination_code")
    private String destinationCode;
    @Column(name = "destination_name")
    private String destinationName;
    @Column(name = "destination_address")
    private String destinationAddress;
    @Column(name = "destination_city")
    private String destinationCity;
    @Column(name = "destination_state")
    private String destinationState;
    @Column(name = "destination_zip")
    private String destinationZip;
    @Column(name = "destination_phone")
    private String destinationPhone;
    @Column(name = "destination_fax")
    private String destinationFax;
    @Column(name = "rated")
    private boolean rated;
    @JoinColumn(name = "rate_quote_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DomesticRateQuote domesticRateQuote;
    @Column(name = "line_mark_up")
    private Double lineMarkUp;
    @Column(name = "fuel_mark_up")
    private Double fuelMarkUp;
    @Column(name = "line_mark_up_charge")
    private Double lineMarkUpCharge;
    @Column(name = "fuel_mark_up_charge")
    private Double fuelMarkUpCharge;
    @Column(name = "flat_fee")
    private Double flatFee;
    @Column(name = "min_amount")
    private Double minAmount;
    @Column(name = "line_amount")
    private Double lineAmount;
    @Column(name = "fuel_amount")
    private Double fuelAmount;
    @Column(name = "extra_amount")
    private Double extraAmount;
    @Column(name = "rate_type")
    private String rateType;


    public String getCarrierName() {
        return null != carrierName ? carrierName : "";
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getDirectInterline() {
        return directInterline;
    }

    public void setDirectInterline(String directInterline) {
        this.directInterline = directInterline;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public Double getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(Double extraCharge) {
        this.extraCharge = extraCharge;
    }

    public Double getFinalCharge() {
        return finalCharge;
    }

    public void setFinalCharge(Double finalCharge) {
        this.finalCharge = finalCharge;
    }

    public Double getFuelCharge() {
        return fuelCharge;
    }

    public void setFuelCharge(Double fuelCharge) {
        this.fuelCharge = fuelCharge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLineHual() {
        return lineHual;
    }

    public void setLineHual(Double lineHual) {
        this.lineHual = lineHual;
    }

    public String getScac() {
        return null != scac ? scac : "";
    }

    public void setScac(String scac) {
        this.scac = scac;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getDestinationFax() {
        return destinationFax;
    }

    public void setDestinationFax(String destinationFax) {
        this.destinationFax = destinationFax;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationPhone() {
        return destinationPhone;
    }

    public void setDestinationPhone(String destinationPhone) {
        this.destinationPhone = destinationPhone;
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

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getOriginFax() {
        return originFax;
    }

    public void setOriginFax(String originFax) {
        this.originFax = originFax;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginPhone() {
        return originPhone;
    }

    public void setOriginPhone(String originPhone) {
        this.originPhone = originPhone;
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

    public DomesticRateQuote getDomesticRateQuote() {
        return domesticRateQuote;
    }

    public void setDomesticRateQuote(DomesticRateQuote domesticRateQuote) {
        this.domesticRateQuote = domesticRateQuote;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public Double getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(Double extraAmount) {
        this.extraAmount = extraAmount;
    }

    public Double getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(Double flatFee) {
        this.flatFee = flatFee;
    }

    public Double getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(Double fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    public Double getFuelMarkUp() {
        return fuelMarkUp;
    }

    public void setFuelMarkUp(Double fuelMarkUp) {
        this.fuelMarkUp = fuelMarkUp;
    }

    public Double getLineAmount() {
        return lineAmount;
    }

    public void setLineAmount(Double lineAmount) {
        this.lineAmount = lineAmount;
    }

    public Double getLineMarkUp() {
        return lineMarkUp;
    }

    public void setLineMarkUp(Double lineMarkUp) {
        this.lineMarkUp = lineMarkUp;
    }

    public Double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public Double getFuelMarkUpCharge() {
        return fuelMarkUpCharge;
    }

    public void setFuelMarkUpCharge(Double fuelMarkUpCharge) {
        this.fuelMarkUpCharge = fuelMarkUpCharge;
    }

    public Double getLineMarkUpCharge() {
        return lineMarkUpCharge;
    }

    public void setLineMarkUpCharge(Double lineMarkUpCharge) {
        this.lineMarkUpCharge = lineMarkUpCharge;
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
        if (!(object instanceof DomesticRateCarrier)) {
            return false;
        }
        DomesticRateCarrier other = (DomesticRateCarrier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
