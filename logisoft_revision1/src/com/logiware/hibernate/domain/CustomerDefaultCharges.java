package com.logiware.hibernate.domain;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.form.CustomerDefaultChargesForm;


public class CustomerDefaultCharges implements java.io.Serializable {


    // Fields    
    private Integer id;
    private String acctNo;
    private GenericCode unitType;
    private String chargeCode;
    private GenericCode chargeCodeDesc;
    private GenericCode costType;
    private Double sell;
    private Double cost;
    private String vendorName;
    private String vendorNumber;
    private String currency;
    private String comment;
    private String defaultCarrier;
    public CustomerDefaultCharges(){

    }
    public CustomerDefaultCharges(CustomerDefaultChargesForm customerDefaultChargesForm)throws Exception {
        this.chargeCode = customerDefaultChargesForm.getChargeCode();
        this.chargeCodeDesc = CommonUtils.isNotEmpty(customerDefaultChargesForm.getChargeCodeDesc())?new GenericCodeDAO().findById(Integer.parseInt(customerDefaultChargesForm.getChargeCodeDesc())):null;
        this.costType = CommonUtils.isNotEmpty(customerDefaultChargesForm.getCostType())?new GenericCodeDAO().findById(Integer.parseInt(customerDefaultChargesForm.getCostType())):null;
        this.unitType = CommonUtils.isNotEmpty(customerDefaultChargesForm.getUnitType()) && !"0".equalsIgnoreCase(customerDefaultChargesForm.getUnitType())?new GenericCodeDAO().findById(Integer.parseInt(customerDefaultChargesForm.getUnitType())):null;
        this.vendorName = customerDefaultChargesForm.getVendorName();
        this.vendorNumber = customerDefaultChargesForm.getVendorNumber();
        this.comment = customerDefaultChargesForm.getCurrency();
        this.defaultCarrier = customerDefaultChargesForm.getDefaultCarrier();
        this.comment = customerDefaultChargesForm.getComment();
        this.cost = CommonUtils.isNotEmpty(customerDefaultChargesForm.getCost())?Double.parseDouble(customerDefaultChargesForm.getCost()):0d;
        this.sell = CommonUtils.isNotEmpty(customerDefaultChargesForm.getSell())?Double.parseDouble(customerDefaultChargesForm.getSell()):0d;
        this.acctNo = customerDefaultChargesForm.getAcctNo();

    }
    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public GenericCode getChargeCodeDesc() {
        return chargeCodeDesc;
    }

    public void setChargeCodeDesc(GenericCode chargeCodeDesc) {
        this.chargeCodeDesc = chargeCodeDesc;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public GenericCode getCostType() {
        return costType;
    }

    public void setCostType(GenericCode costType) {
        this.costType = costType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    public GenericCode getUnitType() {
        return unitType;
    }

    public void setUnitType(GenericCode unitType) {
        this.unitType = unitType;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getComment() {
        return null != comment?comment.toUpperCase():"";
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefaultCarrier() {
        return defaultCarrier;
    }

    public void setDefaultCarrier(String defaultCarrier) {
        this.defaultCarrier = defaultCarrier;
    }
    

    
}
   