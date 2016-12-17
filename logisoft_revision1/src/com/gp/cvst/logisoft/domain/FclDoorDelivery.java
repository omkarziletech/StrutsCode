/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.domain;

import com.gp.cong.logisoft.domain.TradingPartner;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author User
 */
@Entity
@Table(name = "fcl_door_delivery")
@DynamicInsert(true)
@DynamicUpdate(true)
public class FclDoorDelivery implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "bol_id")
    private Integer bolId;
    @Column(name = "delivery_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    @Column(name = "free_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date freeDate;
    @Column(name = "local_delivery_or_transferby")
    private String localDeliveryOrTransferBy;
    @Column(name = "delivery_to")
    private String deliveryTo;
    @Column(name = "delivery_acct_no")
    private String deliveryToAcctNo;
    @Column(name = "delivery_email")
    private String deliveryEmail;
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Column(name = "delivery_contact_name")
    private String deliveryContactName;
    @Column(name = "delivery_city")
    private String deliveryCity;
    @Column(name = "delivery_state")
    private String deliveryState;
    @Column(name = "delivery_zip")
    private String deliveryZip;
    @Column(name = "delivery_phone")
    private String deliveryPhone;
    @Column(name = "delivery_fax")
    private String deliveryFax;
    @Column(name = "billing")
    private String billing;
    @Column(name = "manual_delivery_to")
    private String manualDeliveryTo;
    @Column(name = "po")
    private String po;
    @Column(name = "reference_numbers")
    private String referenceNumbers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBolId() {
        return bolId;
    }

    public void setBolId(Integer bolId) {
        this.bolId = bolId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getFreeDate() {
        return freeDate;
    }

    public void setFreeDate(Date freeDate) {
        this.freeDate = freeDate;
    }

    public String getLocalDeliveryOrTransferBy() {
        return localDeliveryOrTransferBy;
    }

    public void setLocalDeliveryOrTransferBy(String localDeliveryOrTransferBy) {
        this.localDeliveryOrTransferBy = localDeliveryOrTransferBy;
    }

    public String getDeliveryTo() {
        return deliveryTo;
    }

    public void setDeliveryTo(String deliveryTo) {
        this.deliveryTo = deliveryTo;
    }

    public String getDeliveryToAcctNo() {
        return deliveryToAcctNo;
    }

    public void setDeliveryToAcctNo(String deliveryToAcctNo) {
        this.deliveryToAcctNo = deliveryToAcctNo;
    }

    public String getDeliveryEmail() {
        return deliveryEmail;
    }

    public void setDeliveryEmail(String deliveryEmail) {
        this.deliveryEmail = deliveryEmail;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryContactName() {
        return deliveryContactName;
    }

    public void setDeliveryContactName(String deliveryContactName) {
        this.deliveryContactName = deliveryContactName;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryZip() {
        return deliveryZip;
    }

    public void setDeliveryZip(String deliveryZip) {
        this.deliveryZip = deliveryZip;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryFax() {
        return deliveryFax;
    }

    public void setDeliveryFax(String deliveryFax) {
        this.deliveryFax = deliveryFax;
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public String getManualDeliveryTo() {
        return manualDeliveryTo;
    }

    public void setManualDeliveryTo(String manualDeliveryTo) {
        this.manualDeliveryTo = manualDeliveryTo;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getReferenceNumbers() {
        return referenceNumbers;
    }

    public void setReferenceNumbers(String referenceNumbers) {
        this.referenceNumbers = referenceNumbers;
    }
}
