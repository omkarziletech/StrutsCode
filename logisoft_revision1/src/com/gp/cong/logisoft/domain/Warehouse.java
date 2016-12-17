/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Rohith
 *
 */
public class Warehouse implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String warehouseName;
    private String warehouseNo;
    private String address;
    private GenericCode countryCode;
    private UnLocation cityCode;
    private String state;
    private String zipCode;
    private String phone;
    private String fax;
    private String manager;
    private String acWarehouseName;
    private String vendorNo;
    private String commodityNo;
    private String acAddress;
    private GenericCode acCountryCode;
    private UnLocation acCity;
    private String acState;
    private String acZipCode;
    private String acPhone;
    private String acFax;
    private String match;
    private String city;
    private String airCity;
    private String extension;
    private String acExtension;
    private String warehouseType;
    private String importsCFSDevanning;
    private String cfsDevanningEmail;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getAcAddress() {
        return acAddress;
    }

    public void setAcAddress(String acAddress) {
        this.acAddress = acAddress;
    }

    public UnLocation getAcCity() {
        return acCity;
    }

    public void setAcCity(UnLocation acCity) {
        this.acCity = acCity;
    }

    public GenericCode getAcCountryCode() {
        return acCountryCode;
    }

    public void setAcCountryCode(GenericCode acCountryCode) {
        this.acCountryCode = acCountryCode;
    }

    public String getAcFax() {
        return acFax;
    }

    public void setAcFax(String acFax) {
        this.acFax = acFax;
    }

    public String getAcPhone() {
        return acPhone;
    }

    public void setAcPhone(String acPhone) {
        this.acPhone = acPhone;
    }

    public String getAcState() {
        return acState;
    }

    public void setAcState(String acState) {
        this.acState = acState;
    }

    public String getAcWarehouseName() {
        return acWarehouseName;
    }

    public void setAcWarehouseName(String acWarehouseName) {
        this.acWarehouseName = acWarehouseName;
    }

    public String getAcZipCode() {
        return acZipCode;
    }

    public void setAcZipCode(String acZipCode) {
        this.acZipCode = acZipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UnLocation getCityCode() {
        return cityCode;
    }

    public void setCityCode(UnLocation cityCode) {
        this.cityCode = cityCode;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public GenericCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(GenericCode countryCode) {
        this.countryCode = countryCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVendorNo() {
        return vendorNo;
    }

    public void setVendorNo(String vendorNo) {
        this.vendorNo = vendorNo;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getAirCity() {
        return airCity;
    }

    public void setAirCity(String airCity) {
        this.airCity = airCity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setAcExtension(String acExtension) {
        this.acExtension = acExtension;
    }

    public String getAcExtension() {
        return acExtension;
    }

    public String getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(String warehouseType) {
        this.warehouseType = warehouseType;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getImportsCFSDevanning() {
        return importsCFSDevanning;
    }

    public void setImportsCFSDevanning(String importsCFSDevanning) {
        this.importsCFSDevanning = importsCFSDevanning;
    }

    public String getCfsDevanningEmail() {
        return cfsDevanningEmail;
    }

    public void setCfsDevanningEmail(String cfsDevanningEmail) {
        this.cfsDevanningEmail = cfsDevanningEmail;
    }
    
}
