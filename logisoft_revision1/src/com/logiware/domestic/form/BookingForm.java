/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.domestic.form;

import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;

/**
 *
 * @author Shanmugam
 */
public class BookingForm extends LogiwareActionForm {

    private Integer userId;
    private String userName;
    private String userNameList;
    private String fromZip;
    private String toZip;
    private String bookingNumber;
    private String carrierName;
    private String bookingId;
    private String purchaseOrderId;
    private String scac;
    private String shipperName;
    private String shipperAddress;
    private String shipperCity;
    private String shipperState;
    private String shipperZip;
    private String shipperContactName;
    private String shipperPhone;
    private String shipperFax;
    private String shipperEmail;
    private String consigneeName;
    private String consigneeAddress;
    private String consigneeCity;
    private String consigneeState;
    private String consigneeZip;
    private String consigneeContactName;
    private String consigneePhone;
    private String consigneeFax;
    private String consigneeEmail;
    private String billtoName;
    private String billtoAddress;
    private String billtoCity;
    private String billtoState;
    private String billtoZip;
    private String originCode;
    private String originName;
    private String originAddress;
    private String originCity;
    private String originState;
    private String originZip;
    private String originPhone;
    private String originFax;
    private String destinationCode;
    private String destinationName;
    private String destinationAddress;
    private String destinationCity;
    private String destinationState;
    private String destinationZip;
    private String destinationPhone;
    private String destinationFax;
    // Purchase Order
    private String purchaseOrderNo;
    private String packageType;
    private Integer packageQuantity;
    private String weight;
//    private boolean palletSlip;
    private String extraInfo;
    private String productName;
    private String description;
//    private boolean hazmat;
    private String hazmatNumber;
    private String nmfc;
    private String classes;
    private String handlingUnitType;
    private String consigneeReference;
    private String shipperReference;
    private Integer handlingUnitQuantity;
    private Integer length;
    private Integer width;
    private Integer height;
    private String carrierNemonic;
    private String proNumber;
//    private double cube;

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getFromZip() {
        return fromZip;
    }

    public void setFromZip(String fromZip) {
        this.fromZip = fromZip;
    }

    public String getToZip() {
        return toZip;
    }

    public void setToZip(String toZip) {
        this.toZip = toZip;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public String getConsigneeContactName() {
        return consigneeContactName;
    }

    public void setConsigneeContactName(String consigneeContactName) {
        this.consigneeContactName = consigneeContactName;
    }

    public String getConsigneeEmail() {
        return consigneeEmail;
    }

    public void setConsigneeEmail(String consigneeEmail) {
        this.consigneeEmail = consigneeEmail;
    }

    public String getConsigneeFax() {
        return consigneeFax;
    }

    public void setConsigneeFax(String consigneeFax) {
        this.consigneeFax = consigneeFax;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeState() {
        return consigneeState;
    }

    public void setConsigneeState(String consigneeState) {
        this.consigneeState = consigneeState;
    }

    public String getConsigneeZip() {
        return consigneeZip;
    }

    public void setConsigneeZip(String consigneeZip) {
        this.consigneeZip = consigneeZip;
    }

    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    public String getShipperCity() {
        return shipperCity;
    }

    public void setShipperCity(String shipperCity) {
        this.shipperCity = shipperCity;
    }

    public String getShipperContactName() {
        return shipperContactName;
    }

    public void setShipperContactName(String shipperContactName) {
        this.shipperContactName = shipperContactName;
    }

    public String getShipperEmail() {
        return shipperEmail;
    }

    public void setShipperEmail(String shipperEmail) {
        this.shipperEmail = shipperEmail;
    }

    public String getShipperFax() {
        return shipperFax;
    }

    public void setShipperFax(String shipperFax) {
        this.shipperFax = shipperFax;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public String getShipperState() {
        return shipperState;
    }

    public void setShipperState(String shipperState) {
        this.shipperState = shipperState;
    }

    public String getShipperZip() {
        return shipperZip;
    }

    public void setShipperZip(String shipperZip) {
        this.shipperZip = shipperZip;
    }

    public String getBilltoAddress() {
        return billtoAddress;
    }

    public void setBilltoAddress(String billtoAddress) {
        this.billtoAddress = billtoAddress;
    }

    public String getBilltoCity() {
        return billtoCity;
    }

    public void setBilltoCity(String billtoCity) {
        this.billtoCity = billtoCity;
    }

    public String getBilltoName() {
        return billtoName;
    }

    public void setBilltoName(String billtoName) {
        this.billtoName = billtoName;
    }

    public String getBilltoState() {
        return billtoState;
    }

    public void setBilltoState(String billtoState) {
        this.billtoState = billtoState;
    }

    public String getBilltoZip() {
        return billtoZip;
    }

    public void setBilltoZip(String billtoZip) {
        this.billtoZip = billtoZip;
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

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Integer getHandlingUnitQuantity() {
        return handlingUnitQuantity;
    }

    public void setHandlingUnitQuantity(Integer handlingUnitQuantity) {
        this.handlingUnitQuantity = handlingUnitQuantity;
    }

    public String getHandlingUnitType() {
        return handlingUnitType;
    }

    public void setHandlingUnitType(String handlingUnitType) {
        this.handlingUnitType = handlingUnitType;
    }

    public String getHazmatNumber() {
        return hazmatNumber;
    }

    public void setHazmatNumber(String hazmatNumber) {
        this.hazmatNumber = hazmatNumber;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getNmfc() {
        return nmfc;
    }

    public void setNmfc(String nmfc) {
        this.nmfc = nmfc;
    }

    public Integer getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(Integer packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getConsigneeReference() {
        return consigneeReference;
    }

    public void setConsigneeReference(String consigneeReference) {
        this.consigneeReference = consigneeReference;
    }

    public String getShipperReference() {
        return shipperReference;
    }

    public void setShipperReference(String shipperReference) {
        this.shipperReference = shipperReference;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(String userNameList) {
        this.userNameList = userNameList;
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
    
}
