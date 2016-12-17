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
@Table(name = "domestic_booking")
public class DomesticBooking extends Domain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "booking_number")
    private String bookingNumber;
    @Column(name = "carrier_name")
    private String carrierName;
    @Column(name = "direct_interline")
    private String directInterline;
    @Column(name = "estimated_days")
    private Integer estimatedDays;
    @Column(name = "type")
    private String type;
    @Column(name = "line_hual")
    private double lineHual;
    @Column(name = "fuel_charge")
    private double fuelCharge;
    @Column(name = "extra_charge")
    private double extraCharge;
    @Column(name = "final_charge")
    private double finalCharge;
    @Column(name = "scac")
    private String scac;
    @Column(name = "ship_date")
    @Temporal(TemporalType.DATE)
    private Date shipDate;
    @Column(name = "origin")
    private String origin;
    @Column(name = "destination")
    private String destination;
    @Column(name = "from_zip")
    private String fromZip;
    @Column(name = "to_zip")
    private String toZip;
    @Column(name = "from_state")
    private String fromState;
    @Column(name = "to_state")
    private String toState;
    @Column(name = "distance")
    private String distance;
    @Column(name = "booked_on")
    @Temporal(TemporalType.DATE)
    private Date bookedOn;
    @JoinColumn(name = "booked_by", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User bookedBy;

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

     @Column(name = "shipper_name")
     private String shipperName;
     @Column(name = "shipper_address1")
     private String shipperAddress1;
     @Column(name = "shipper_address2")
     private String shipperAddress2;
     @Column(name = "shipper_city")
     private String shipperCity;
     @Column(name = "shipper_state")
     private String shipperState;
     @Column(name = "shipper_zipcode")
     private String shipperZipcode;
     @Column(name = "shipper_contact_name")
     private String shipperContactName;
     @Column(name = "shipper_contact_phone")
     private String shipperContactPhone;
     @Column(name = "shipper_contact_fax")
     private String shipperContactFax;
     @Column(name = "shipper_contact_email")
     private String shipperContactEmail;
     @Column(name = "shipper_hours")
     private String shipperHours;

     @Column(name = "consignee_name")
     private String consigneeName;
     @Column(name = "consignee_address1")
     private String consigneeAddress1;
     @Column(name = "consignee_address2")
     private String consigneeAddress2;
     @Column(name = "consignee_city")
     private String consigneeCity;
     @Column(name = "consignee_state")
     private String consigneeState;
     @Column(name = "consignee_zipcode")
     private String consigneeZipcode;
     @Column(name = "consignee_contact_name")
     private String consigneeContactName;
     @Column(name = "consignee_contact_phone")
     private String consigneeContactPhone;
     @Column(name = "consignee_contact_fax")
     private String consigneeContactFax;
     @Column(name = "consignee_contact_email")
     private String consigneeContactEmail;
     @Column(name = "consignee_hours")
     private String consigneeHours;

     @Column(name = "billto_name")
     private String billtoName;
     @Column(name = "billto_address1")
     private String billtoAddress1;
     @Column(name = "billto_address2")
     private String billtoAddress2;
     @Column(name = "billto_city")
     private String billtoCity;
     @Column(name = "billto_state")
     private String billtoState;
     @Column(name = "billto_zipcode")
     private String billtoZipcode;
     @Column(name = "bol_status")
     private String bolStatus;
     @Column(name = "shipper_reference")
     private String shipperReference;
     @Column(name = "consignee_reference")
     private String consigneeReference;
     @Column(name = "pro_number")
     private String proNumber;
     @Column(name = "status_message")
     private String statusMessage;
     @Column(name = "carrier_nemonic")
     private String carrierNemonic;
     
    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getCarrierName() {
        return null != carrierName?carrierName:"";
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDirectInterline() {
        return directInterline;
    }

    public void setDirectInterline(String directInterline) {
        this.directInterline = directInterline;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Integer getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(Integer estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public double getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(double extraCharge) {
        this.extraCharge = extraCharge;
    }

    public double getFinalCharge() {
        return finalCharge;
    }

    public void setFinalCharge(double finalCharge) {
        this.finalCharge = finalCharge;
    }

    public double getFuelCharge() {
        return fuelCharge;
    }

    public void setFuelCharge(double fuelCharge) {
        this.fuelCharge = fuelCharge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLineHual() {
        return lineHual;
    }

    public void setLineHual(double lineHual) {
        this.lineHual = lineHual;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getScac() {
        return null != scac?scac:"";
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(User bookedBy) {
        this.bookedBy = bookedBy;
    }

    public Date getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(Date bookedOn) {
        this.bookedOn = bookedOn;
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

    public String getBilltoAddress1() {
        return null != billtoAddress1?billtoAddress1:"";
    }

    public void setBilltoAddress1(String billtoAddress1) {
        this.billtoAddress1 = billtoAddress1;
    }

    public String getBilltoAddress2() {
        return null != billtoAddress2?billtoAddress2:"";
    }

    public void setBilltoAddress2(String billtoAddress2) {
        this.billtoAddress2 = billtoAddress2;
    }

    public String getBilltoCity() {
        return null != billtoCity?billtoCity:"";
    }

    public void setBilltoCity(String billtoCity) {
        this.billtoCity = billtoCity;
    }

    public String getBilltoName() {
        return null != billtoName?billtoName:"";
    }

    public void setBilltoName(String billtoName) {
        this.billtoName = billtoName;
    }

    public String getBilltoState() {
        return null != billtoState?billtoState:"";
    }

    public void setBilltoState(String billtoState) {
        this.billtoState = billtoState;
    }

    public String getBilltoZipcode() {
        return null != billtoZipcode?billtoZipcode:"";
    }

    public void setBilltoZipcode(String billtoZipcode) {
        this.billtoZipcode = billtoZipcode;
    }

    public String getConsigneeAddress1() {
        return null != consigneeAddress1?consigneeAddress1:"";
    }

    public void setConsigneeAddress1(String consigneeAddress1) {
        this.consigneeAddress1 = consigneeAddress1;
    }

    public String getConsigneeAddress2() {
        return null != consigneeAddress2?consigneeAddress2:"";
    }

    public void setConsigneeAddress2(String consigneeAddress2) {
        this.consigneeAddress2 = consigneeAddress2;
    }

    public String getConsigneeCity() {
        return null != consigneeCity?consigneeCity:"";
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public String getConsigneeContactEmail() {
        return null != consigneeContactEmail?consigneeContactEmail:"";
    }

    public void setConsigneeContactEmail(String consigneeContactEmail) {
        this.consigneeContactEmail = consigneeContactEmail;
    }

    public String getConsigneeContactFax() {
        return null != consigneeContactFax?consigneeContactFax:"";
    }

    public void setConsigneeContactFax(String consigneeContactFax) {
        this.consigneeContactFax = consigneeContactFax;
    }

    public String getConsigneeContactName() {
        return null != consigneeContactName?consigneeContactName:"";
    }

    public void setConsigneeContactName(String consigneeContactName) {
        this.consigneeContactName = consigneeContactName;
    }

    public String getConsigneeContactPhone() {
        return null != consigneeContactPhone?consigneeContactPhone:"";
    }

    public void setConsigneeContactPhone(String consigneeContactPhone) {
        this.consigneeContactPhone = consigneeContactPhone;
    }

    public String getConsigneeHours() {
        return null != consigneeHours?consigneeHours:"";
    }

    public void setConsigneeHours(String consigneeHours) {
        this.consigneeHours = consigneeHours;
    }

    public String getConsigneeName() {
        return null != consigneeName?consigneeName:"";
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeState() {
        return null != consigneeState?consigneeState:"";
    }

    public void setConsigneeState(String consigneeState) {
        this.consigneeState = consigneeState;
    }

    public String getConsigneeZipcode() {
        return null != consigneeZipcode?consigneeZipcode:"";
    }

    public void setConsigneeZipcode(String consigneeZipcode) {
        this.consigneeZipcode = consigneeZipcode;
    }

    public String getShipperAddress1() {
        return null != shipperAddress1?shipperAddress1:"";
    }

    public void setShipperAddress1(String shipperAddress1) {
        this.shipperAddress1 = shipperAddress1;
    }

    public String getShipperAddress2() {
        return null != shipperAddress2?shipperAddress2:"";
    }

    public void setShipperAddress2(String shipperAddress2) {
        this.shipperAddress2 = shipperAddress2;
    }

    public String getShipperCity() {
        return null != shipperCity?shipperCity:"";
    }

    public void setShipperCity(String shipperCity) {
        this.shipperCity = shipperCity;
    }

    public String getShipperContactEmail() {
        return null != shipperContactEmail?shipperContactEmail:"";
    }

    public void setShipperContactEmail(String shipperContactEmail) {
        this.shipperContactEmail = shipperContactEmail;
    }

    public String getShipperContactFax() {
        return null != shipperContactFax?shipperContactFax:"";
    }

    public void setShipperContactFax(String shipperContactFax) {
        this.shipperContactFax = shipperContactFax;
    }

    public String getShipperContactName() {
        return null != shipperContactName?shipperContactName:"";
    }

    public void setShipperContactName(String shipperContactName) {
        this.shipperContactName = shipperContactName;
    }

    public String getShipperContactPhone() {
        return null != shipperContactPhone?shipperContactPhone:"";
    }

    public void setShipperContactPhone(String shipperContactPhone) {
        this.shipperContactPhone = shipperContactPhone;
    }

    public String getShipperHours() {
        return null != shipperHours?shipperHours:"";
    }

    public void setShipperHours(String shipperHours) {
        this.shipperHours = shipperHours;
    }

    public String getShipperName() {
        return null != shipperName?shipperName:"";
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperState() {
        return null != shipperState?shipperState:"";
    }

    public void setShipperState(String shipperState) {
        this.shipperState = shipperState;
    }

    public String getShipperZipcode() {
        return null != shipperZipcode?shipperZipcode:"";
    }

    public void setShipperZipcode(String shipperZipcode) {
        this.shipperZipcode = shipperZipcode;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getFromZip() {
        return fromZip;
    }

    public void setFromZip(String fromZip) {
        this.fromZip = fromZip;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public String getToZip() {
        return toZip;
    }

    public void setToZip(String toZip) {
        this.toZip = toZip;
    }

    public String getBolStatus() {
        return bolStatus;
    }

    public void setBolStatus(String bolStatus) {
        this.bolStatus = bolStatus;
    }

    public String getConsigneeReference() {
       return null != consigneeReference?consigneeReference:"";
    }

    public void setConsigneeReference(String consigneeReference) {
        this.consigneeReference = consigneeReference;
    }

    public String getShipperReference() {
        return null != shipperReference?shipperReference:"";
    }

    public void setShipperReference(String shipperReference) {
        this.shipperReference = shipperReference;
    }

    public String getCarrierNemonic() {
        return carrierNemonic;
    }

    public void setCarrierNemonic(String carrierNemonic) {
        this.carrierNemonic = carrierNemonic;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
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
	if (!(object instanceof DomesticBooking)) {
	    return false;
	}
	DomesticBooking other = (DomesticBooking) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }
}
