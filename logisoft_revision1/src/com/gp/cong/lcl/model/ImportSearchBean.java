/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

import java.io.Serializable;

/**
 *
 * @author palraj
 */
public class ImportSearchBean implements Serializable {

    private long fileNumberId;
    private String fileNumber;
    private boolean shortShip;
    private String bookingType;
    private boolean quote;
    private boolean booking;
    private boolean transshipment;
    private String state;
    private String status;
    private String bookedBy;
    private String quoteBy;
    private boolean quoteComplete;
    private boolean hazmat;
    private boolean onHold;
    private boolean clientPwkReceived;
    private String eta;
    private String dispoCode;
    private String dispoDesc;
    private Integer piece;
    private String weight;
    private String volume;
    private String originUncode;
    private String origin;
    private String polUncode;
    private String podUncode;
    private String pol;
    private String pod;
    private String destinationUncode;
    private String destination;
    private boolean relayOverride;
    private boolean pickup;
    private String pickupCity;
    private String pickedUpDateTime;
    private String shipName;
    private String shipNo;
    private String shipAddress;
    private String shipCity;
    private String shipState;
    private String shipZip;
    private String consName;
    private String consNo;
    private String consAddress;
    private String consCity;
    private String consState;
    private String consZip;
    private String billingTerminal;
    private String hotCodes;
    private String strippedDate;

    public String getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public boolean isBooking() {
        return booking;
    }

    public void setBooking(boolean booking) {
        this.booking = booking;
    }

    public boolean isClientPwkReceived() {
        return clientPwkReceived;
    }

    public void setClientPwkReceived(boolean clientPwkReceived) {
        this.clientPwkReceived = clientPwkReceived;
    }

    public String getConsAddress() {
        return consAddress;
    }

    public void setConsAddress(String consAddress) {
        this.consAddress = consAddress;
    }

    public String getConsCity() {
        return consCity;
    }

    public void setConsCity(String consCity) {
        this.consCity = consCity;
    }

    public String getConsNo() {
        return consNo;
    }

    public void setConsNo(String consNo) {
        this.consNo = consNo;
    }

    public String getConsState() {
        return consState;
    }

    public void setConsState(String consState) {
        this.consState = consState;
    }

    public String getConsZip() {
        return consZip;
    }

    public void setConsZip(String consZip) {
        this.consZip = consZip;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationUncode() {
        return destinationUncode;
    }

    public void setDestinationUncode(String destinationUncode) {
        this.destinationUncode = destinationUncode;
    }

    public String getDispoCode() {
        return dispoCode;
    }

    public void setDispoCode(String dispoCode) {
        this.dispoCode = dispoCode;
    }

    public String getDispoDesc() {
        return dispoDesc;
    }

    public void setDispoDesc(String dispoDesc) {
        this.dispoDesc = dispoDesc;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public boolean isShortShip() {
        return shortShip;
    }

    public void setShortShip(boolean shortShip) {
        this.shortShip = shortShip;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(String hotCodes) {
        this.hotCodes = hotCodes;
    }

    public boolean isOnHold() {
        return onHold;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginUncode() {
        return originUncode;
    }

    public void setOriginUncode(String originUncode) {
        this.originUncode = originUncode;
    }

    public String getPickedUpDateTime() {
        return pickedUpDateTime;
    }

    public void setPickedUpDateTime(String pickedUpDateTime) {
        this.pickedUpDateTime = pickedUpDateTime;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPolUncode() {
        return polUncode;
    }

    public void setPolUncode(String polUncode) {
        this.polUncode = polUncode;
    }

    public String getPodUncode() {
        return podUncode;
    }

    public void setPodUncode(String podUncode) {
        this.podUncode = podUncode;
    }

    public boolean isQuote() {
        return quote;
    }

    public void setQuote(boolean quote) {
        this.quote = quote;
    }

    public String getQuoteBy() {
        return quoteBy;
    }

    public void setQuoteBy(String quoteBy) {
        this.quoteBy = quoteBy;
    }

    public boolean isQuoteComplete() {
        return quoteComplete;
    }

    public void setQuoteComplete(boolean quoteComplete) {
        this.quoteComplete = quoteComplete;
    }

    public boolean isRelayOverride() {
        return relayOverride;
    }

    public void setRelayOverride(boolean relayOverride) {
        this.relayOverride = relayOverride;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public String getShipState() {
        return shipState;
    }

    public void setShipState(String shipState) {
        this.shipState = shipState;
    }

    public String getShipZip() {
        return shipZip;
    }

    public void setShipZip(String shipZip) {
        this.shipZip = shipZip;
    }

    public String getConsName() {
        return consName;
    }

    public void setConsName(String consName) {
        this.consName = consName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTransshipment() {
        return transshipment;
    }

    public void setTransshipment(boolean transshipment) {
        this.transshipment = transshipment;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getStrippedDate() {
        return strippedDate;
    }

    public void setStrippedDate(String strippedDate) {
        this.strippedDate = strippedDate;
    }
}
