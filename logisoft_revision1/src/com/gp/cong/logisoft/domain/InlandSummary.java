/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.util.*;
import java.io.Serializable;

/**
 * @author Yogesh
 *
 */
public class InlandSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer inlandVoyageNo;
    private String unitNumber;
    private Date approxDueDate;
    private Date strippedDate;
    private Date itDate;
    private String Prepared;
    private String entryNo;
    private Integer pieces;
    private Double weight;
    private String deliverFrom;
    private String deliverTo;
    private String chLicenseNo;
    private String truckNo;
    private String carrierCode;
    private String carrierName;
    private Date unloadStartDate;
    private Date unloadFinishedDate;
    private String comments;

    public Date getApproxDueDate() {
        return approxDueDate;
    }

    public void setApproxDueDate(Date approxDueDate) {
        this.approxDueDate = approxDueDate;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getChLicenseNo() {
        return chLicenseNo;
    }

    public void setChLicenseNo(String chLicenseNo) {
        this.chLicenseNo = chLicenseNo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDeliverFrom() {
        return deliverFrom;
    }

    public void setDeliverFrom(String deliverFrom) {
        this.deliverFrom = deliverFrom;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public Integer getInlandVoyageNo() {
        return inlandVoyageNo;
    }

    public void setInlandVoyageNo(Integer inlandVoyageNo) {
        this.inlandVoyageNo = inlandVoyageNo;
    }

    public Date getItDate() {
        return itDate;
    }

    public void setItDate(Date itDate) {
        this.itDate = itDate;
    }

    public Integer getPieces() {
        return pieces;
    }

    public void setPieces(Integer pieces) {
        this.pieces = pieces;
    }

    public String getPrepared() {
        return Prepared;
    }

    public void setPrepared(String prepared) {
        Prepared = prepared;
    }

    public Date getStrippedDate() {
        return strippedDate;
    }

    public void setStrippedDate(Date strippedDate) {
        this.strippedDate = strippedDate;
    }

    public String getTruckNo() {
        return truckNo;
    }

    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public Date getUnloadFinishedDate() {
        return unloadFinishedDate;
    }

    public void setUnloadFinishedDate(Date unloadFinishedDate) {
        this.unloadFinishedDate = unloadFinishedDate;
    }

    public Date getUnloadStartDate() {
        return unloadStartDate;
    }

    public void setUnloadStartDate(Date unloadStartDate) {
        this.unloadStartDate = unloadStartDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
