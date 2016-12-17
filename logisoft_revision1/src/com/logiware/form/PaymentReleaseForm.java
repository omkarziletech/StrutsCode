/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.form;

import java.util.Date;
import org.apache.struts.action.ActionForm;


public class PaymentReleaseForm extends ActionForm {
    // Fields
    private String action;
    private Integer id;
    private Integer bolId;
    private String paymentRelease = "N";
    private String importRelease = "N";
    private String comment;
    private String importReleaseComments;
    private String paidBy;
    private String checkNumber;
    private String paidDate;
    private String releasedOn;
    private String importReleaseOn;
    private String amount;
    private String collectAmount;
    private String fileNumber;
    private String overPaid;
    private String expressRelease="N"; 
    private String expressReleasedOn;
    private String expressReleaseComment;
    private String deliveryOrder="N";
    private String deliveryOrderOn;
    private String customsClearance="N";
    private String customsClearanceOn;
    private String customsClearanceComment;
    private String deliveryOrderComment;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getComment() {
        return null != comment?comment.toUpperCase():"";
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getPaymentRelease() {
        return paymentRelease;
    }

    public void setPaymentRelease(String paymentRelease) {
        this.paymentRelease = paymentRelease;
    }

    public String getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(String releasedOn) {
        this.releasedOn = releasedOn;
    }

    public String getImportRelease() {
        return importRelease;
    }

    public void setImportRelease(String importRelease) {
        this.importRelease = importRelease;
    }

    public String getImportReleaseComments() {
        return null != importReleaseComments?importReleaseComments.toUpperCase():"";
    }

    public void setImportReleaseComments(String importReleaseComments) {
        this.importReleaseComments = importReleaseComments;
    }

    public String getImportReleaseOn() {
        return importReleaseOn;
    }

    public void setImportReleaseOn(String importReleaseOn) {
        this.importReleaseOn = importReleaseOn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getBolId() {
        return bolId;
    }

    public void setBolId(Integer bolId) {
        this.bolId = bolId;
    }

    public String getCollectAmount() {
        return collectAmount;
    }

    public void setCollectAmount(String collectAmount) {
        this.collectAmount = collectAmount;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getOverPaid() {
        return overPaid;
    }

    public void setOverPaid(String overPaid) {
        this.overPaid = overPaid;
    }

    public String getExpressRelease() {
        return expressRelease;
    }

    public void setExpressRelease(String expressRelease) {
        this.expressRelease = expressRelease;
    }

    public String getExpressReleasedOn() {
        return expressReleasedOn;
    }

    public void setExpressReleasedOn(String expressReleasedOn) {
        this.expressReleasedOn = expressReleasedOn;
    }

    public String getExpressReleaseComment() {
        return expressReleaseComment;
    }

    public void setExpressReleaseComment(String expressReleaseComment) {
        this.expressReleaseComment = expressReleaseComment;
    }

    public String getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(String deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public String getDeliveryOrderOn() {
        return deliveryOrderOn;
    }

    public void setDeliveryOrderOn(String deliveryOrderOn) {
        this.deliveryOrderOn = deliveryOrderOn;
    }

    public String getCustomsClearance() {
        return customsClearance;
    }

    public void setCustomsClearance(String customsClearance) {
        this.customsClearance = customsClearance;
    }

    public String getCustomsClearanceOn() {
        return customsClearanceOn;
    }

    public void setCustomsClearanceOn(String customsClearanceOn) {
        this.customsClearanceOn = customsClearanceOn;
    }

    public String getCustomsClearanceComment() {
        return customsClearanceComment;
    }

    public void setCustomsClearanceComment(String customsClearanceComment) {
        this.customsClearanceComment = customsClearanceComment;
    }

    public String getDeliveryOrderComment() {
        return deliveryOrderComment;
    }

    public void setDeliveryOrderComment(String deliveryOrderComment) {
        this.deliveryOrderComment = deliveryOrderComment;
    }
    
}
