package com.gp.cong.logisoft.domain;

import java.util.Date;
import com.gp.cong.hibernate.Domain;

/**
 * SedSchedulebDetails entity.
 *
 * @author MyEclipse Persistence Tools
 */
public class SedSchedulebDetails extends Domain implements java.io.Serializable {

    // Fields
    private Integer id;
    private Date entrdt;
    private String entnam;
    private String domesticOrForeign;
    private String scheduleBNumber;
    private String scheduleBName;
    private String description1;
    private String description2;
    private Integer quantities1;
    private Integer quantities2;
    private String units1;
    private String uom2;
    private String uom1;
    private String units2;
    private Integer weight;
    private String weightType;
    private Integer value;
    private String exportInformationCode;
    private String licenseType;
    private String usedVehicle;
    private String exportLicense;
    private String eccn;
    private String vehicleIdType;
    private String vehicleIdNumber;
    private String vehicleTitleNumber;
    private String vehicleState;
    private String shipment;
    private String trnref;
    private String totalLicenseValue;

    // Constructors
    /**
     * default constructor
     */
    public SedSchedulebDetails() {
    }

    // Property accessors
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getEntrdt() {
        return this.entrdt;
    }

    public void setEntrdt(Date entrdt) {
        this.entrdt = entrdt;
    }

    public String getEntnam() {
        return this.entnam;
    }

    public void setEntnam(String entnam) {
        this.entnam = entnam;
    }

    public String getDomesticOrForeign() {
        return this.domesticOrForeign;
    }

    public void setDomesticOrForeign(String domesticOrForeign) {
        this.domesticOrForeign = domesticOrForeign;
    }

    public String getScheduleBNumber() {
        return this.scheduleBNumber;
    }

    public void setScheduleBNumber(String scheduleBNumber) {
        this.scheduleBNumber = scheduleBNumber;
    }

    public String getScheduleBName() {
        return this.scheduleBName;
    }

    public void setScheduleBName(String scheduleBName) {
        this.scheduleBName = scheduleBName;
    }

    public String getDescription1() {
        return this.description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return this.description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getWeightType() {
        return this.weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public Integer getQuantities1() {
        return quantities1;
    }

    public String getUnits1() {
        return units1;
    }

    public void setUnits1(String units1) {
        this.units1 = units1;
    }

    public String getUnits2() {
        return units2;
    }

    public void setUnits2(String units2) {
        this.units2 = units2;
    }

    public void setQuantities1(Integer quantities1) {
        this.quantities1 = quantities1;
    }

    public Integer getQuantities2() {
        return quantities2;
    }

    public void setQuantities2(Integer quantities2) {
        this.quantities2 = quantities2;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getExportInformationCode() {
        return this.exportInformationCode;
    }

    public void setExportInformationCode(String exportInformationCode) {
        this.exportInformationCode = exportInformationCode;
    }

    public String getLicenseType() {
        return this.licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getUsedVehicle() {
        return this.usedVehicle;
    }

    public void setUsedVehicle(String usedVehicle) {
        this.usedVehicle = usedVehicle;
    }

    public String getExportLicense() {
        return this.exportLicense;
    }

    public void setExportLicense(String exportLicense) {
        this.exportLicense = exportLicense;
    }

    public String getEccn() {
        return this.eccn;
    }

    public void setEccn(String eccn) {
        this.eccn = eccn;
    }

    public String getVehicleIdType() {
        return this.vehicleIdType;
    }

    public void setVehicleIdType(String vehicleIdType) {
        this.vehicleIdType = vehicleIdType;
    }

    public String getVehicleIdNumber() {
        return this.vehicleIdNumber;
    }

    public void setVehicleIdNumber(String vehicleIdNumber) {
        this.vehicleIdNumber = vehicleIdNumber;
    }

    public String getVehicleTitleNumber() {
        return this.vehicleTitleNumber;
    }

    public void setVehicleTitleNumber(String vehicleTitleNumber) {
        this.vehicleTitleNumber = vehicleTitleNumber;
    }

    public String getVehicleState() {
        return this.vehicleState;
    }

    public void setVehicleState(String vehicleState) {
        this.vehicleState = vehicleState;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getTrnref() {
        return trnref;
    }

    public void setTrnref(String trnref) {
        this.trnref = trnref;
    }

    public String getUom1() {
        return uom1;
    }

    public void setUom1(String uom1) {
        this.uom1 = uom1;
    }

    public String getUom2() {
        return uom2;
    }

    public void setUom2(String uom2) {
        this.uom2 = uom2;
    }

    public String getTotalLicenseValue() {
        return totalLicenseValue;
    }

    public void setTotalLicenseValue(String totalLicenseValue) {
        this.totalLicenseValue = totalLicenseValue;
    }
}