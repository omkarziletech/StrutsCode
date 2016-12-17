/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.form;

import com.gp.cong.common.DateUtils;
import com.gp.cong.hibernate.FclBlNew;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Shanmugam R
 */
public class FclBlForm extends EventForm {

    private FclBlNew fclBl;
    private Integer bol;
    private String methodName;
    private String billTo;
    private String comment;
    private String aesComment;
    private String size;
    private List moveTypeList;
    private List lineMoveList;
    private String bolDate;
    private String sailDate;
    private String portCutOff;
    private String docCutOff;
    private String earlierPickUpDate;
    private String eta;
    private String manifestedDate;
    private String receivedMasterdate;
    private String auditedDate;
    private String closedDate;
    private String auditedBy;
    private String blClosed;
    private String blAudited;
    private String closedBy;
    private String verfiyETA;
    private String confirmBy;
    private String confirmOn;
    private String voidDate;
    private String importVerifiedEta;
    private String dateInYard;
    private String dateOutYard;
    private String inbondDate;
    private String paymentReleasedOn;
    private String ediCreatedOn;
    private String manifestedBy;
    private String readyToPost;
    private String overPaidStatus;
    private String convertedToAp;
    private String action;
    private String selectedId;
    private String selectedTab;
    private String quoteBy;
    private String quoteDate;
    private String bookedBy;
    private String bookedDate;
    private String vesselName;
    private String vessel;
    private String fclSsblGoCollect;
    private String ratedManifest;
    private String omit2LetterCountryCode;
    private String doorOriginAsPlorHouse;
    private String dockReceipt;
// For Correction
    private String correctedShipperName;
    private String correctedShipperNo;
    private String correctedShipperAddress;
    private String correctedForwarderName;
    private String correctedForwarderNo;
    private String correctedForwarderAddress;
    private String correctedThirdPartyName;
    private String correctedThirdPartyNo;
    private String correctedThirdPartyAddress;
    private String freightInvoiceContacts;
    private String insuranceAllowed;
    private boolean isBulletRate;
    private String bookingContact;
    private String spotRate;
    private String resendCostToBlue;
    private String iIconBillToolTip;
    private String brand;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public FclBlForm() {
        this.fclBl = new FclBlNew();
    }

    public FclBlNew getFclBl() {
        return fclBl;
    }

    public void setFclBl(FclBlNew fclBl) {
        this.fclBl = fclBl;
    }

    public List getMoveTypeList() throws Exception {
        return new DBUtil().getGenericFCLforTypeOfMove(48, "yes", "yes");
    }

    public void setMoveTypeList(List moveTypeList) throws Exception {
        this.moveTypeList = moveTypeList;
    }

    public List getLineMoveList() throws Exception {
        return new DBUtil().getGenericFCLforTypeOfMovebooking(48, "yes", "yes");
    }

    public void setLineMoveList(List lineMoveList) throws Exception {
        this.lineMoveList = lineMoveList;
    }

    public String getAuditedDate() throws Exception {
        return DateUtils.formatDate(fclBl.getAuditedDate(), "dd-MMM-yyyy HH:mm:ss");
    }

    public void setAuditedDate(String auditedDate) throws Exception {
        fclBl.setAuditedDate(DateUtils.parseDate(auditedDate, "dd-MMM-yyyy HH:mm:ss"));
    }

    public String getBolDate() throws Exception {
        return DateUtils.formatDate(fclBl.getBoldate(), "dd-MMM-yyyy HH:mm");
    }

    public void setBolDate(String boldate) throws Exception {
        if (null != boldate) {
            fclBl.setBoldate(DateUtils.parseDate(boldate, "dd-MMM-yyyy HH:mm"));
        } else {
            fclBl.setBoldate(new Date());
        }
    }

    public String getClosedDate() throws Exception {
        return DateUtils.formatDate(fclBl.getClosedDate(), "dd-MMM-yyyy HH:mm:ss");
    }

    public void setClosedDate(String closedDate) throws Exception {
        fclBl.setClosedDate(DateUtils.parseDate(closedDate, "dd-MMM-yyyy HH:mm:ss"));
    }

    public String getConfirmBy() {
        return fclBl.getConfirmBy();
    }

    public void setConfirmBy(String confirmBy) {
        fclBl.setConfirmBy(null != confirmBy ? confirmBy.toUpperCase() : "");
    }

    public String getConfirmOn() throws Exception {
        return DateUtils.formatDate(fclBl.getConfirmOn(), "MM/dd/yyyy kk:mm");
    }

    public void setConfirmOn(String confirmOn) throws Exception {
        fclBl.setConfirmOn(DateUtils.parseDate(confirmOn, "MM/dd/yyyy kk:mm"));
    }

    public String getDateInYard() throws Exception {
        return DateUtils.formatDate(fclBl.getDateInYard(), "MM/dd/yyyy");
    }

    public void setDateInYard(String dateInYard) throws Exception {
        fclBl.setDateInYard(DateUtils.parseToDate(dateInYard));
    }

    public String getDateOutYard() throws Exception {
        return DateUtils.formatDate(fclBl.getDateOutYard(), "MM/dd/yyyy");
    }

    public void setDateOutYard(String dateOutYard) throws Exception {
        fclBl.setDateOutYard(DateUtils.parseToDate(dateOutYard));
    }

    public String getEdiCreatedOn() throws Exception {
        return DateUtils.formatDate(fclBl.getEdiCreatedOn(), "dd-MMM-yyyy HH:mm");
    }

    public void setEdiCreatedOn(String ediCreatedOn) throws Exception {
        fclBl.setEdiCreatedOn(DateUtils.parseDate(ediCreatedOn, "dd-MMM-yyyy HH:mm"));
    }

    public String getEta() throws Exception {
        return DateUtils.parseDateToString(fclBl.getEta());
    }

    public void setEta(String eta) throws Exception {
        fclBl.setEta(DateUtils.parseToDate(eta));
    }

    public String getImportVerifiedEta() throws Exception {
        return DateUtils.formatDate(fclBl.getImportVerifiedEta(), "MM/dd/yyyy");
    }

    public void setImportVerifiedEta(String importVerifiedEta) throws Exception {
        fclBl.setImportVerifiedEta(DateUtils.parseToDate(importVerifiedEta));
    }

    public String getInbondDate() throws Exception {
        return DateUtils.formatDate(fclBl.getInbondDate(), "dd-MMM-yyyy HH:mm");
    }

    public void setInbondDate(String inbondDate) throws Exception {
        fclBl.setInbondDate(DateUtils.parseDate(inbondDate, "dd-MMM-yyyy HH:mm"));
    }

    public String getManifestedDate() throws Exception {
        return DateUtils.formatDate(fclBl.getManifestedDate(), "dd-MMM-yyyy HH:mm");
    }

    public void setManifestedDate(String manifestedDate) throws Exception {
        fclBl.setManifestedDate(DateUtils.parseDate(manifestedDate, "dd-MMM-yyyy HH:mm"));
    }

    public String getPaymentReleasedOn() throws Exception {
        return DateUtils.formatDate(fclBl.getPaymentReleasedOn(), "dd-MMM-yyyy");
    }

    public void setPaymentReleasedOn(String paymentReleasedOn) throws Exception {
        fclBl.setPaymentReleasedOn(DateUtils.parseToDateForMonthMMM(paymentReleasedOn));
    }

    public String getReceivedMasterdate() throws Exception {
        return DateUtils.formatDate(fclBl.getReceivedMasterdate(), "dd-MMM-yyyy HH:mm");
    }

    public void setReceivedMasterdate(String receivedMasterdate) throws Exception {
        fclBl.setReceivedMasterdate(DateUtils.parseDate(receivedMasterdate, "dd-MMM-yyyy HH:mm"));
    }

    public String getSailDate() throws Exception {
        return DateUtils.parseDateToString(fclBl.getSailDate());
    }

    public void setSailDate(String sailDate) throws Exception {
        fclBl.setSailDate(DateUtils.parseToDate(sailDate));
    }

    public String getVerfiyETA() throws Exception {
        return DateUtils.formatDate(fclBl.getVerfiyETA(), "MM/dd/yyyy");
    }

    public void setVerfiyETA(String verfiyETA) throws Exception {
        fclBl.setVerfiyETA(DateUtils.parseToDate(verfiyETA));
    }

    public String getVoidDate() throws Exception {
        return DateUtils.formatDate(fclBl.getVoidDate(), "dd-MMM-yyyy");
    }

    public void setVoidDate(String voidDate) throws Exception {
        fclBl.setVoidDate(DateUtils.parseToDateForMonthMMM(voidDate));
    }

    public String getConvertedToAp() {
        return convertedToAp;
    }

    public void setConvertedToAp(String convertedToAp) {
        this.convertedToAp = convertedToAp;
    }

    public String getOverPaidStatus() {
        return overPaidStatus;
    }

    public void setOverPaidStatus(String overPaidStatus) {
        this.overPaidStatus = overPaidStatus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getBol() {
        return getFclBl().getBol();
    }

    public void setBol(Integer bol) {
        this.bol = bol;
    }

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getManifestedBy() {
        return fclBl.getManifestedBy();
    }

    public void setManifestedBy(String manifestedBy) {
        fclBl.setManifestedBy(null != manifestedBy ? manifestedBy.toUpperCase() : "");
    }

    public String getReadyToPost() {
        return "M".equalsIgnoreCase(fclBl.getReadyToPost()) ? "on" : "off";

    }

    public void setReadyToPost(String readyToPost) {
        fclBl.setReadyToPost("on".equalsIgnoreCase(readyToPost) ? "M" : "");
    }

    public String getAuditedBy() {
        return fclBl.getAuditedBy();
    }

    public void setAuditedBy(String auditedBy) {
        fclBl.setAuditedBy(null != auditedBy ? auditedBy.toUpperCase() : null);
    }

    public String getClosedBy() {
        return fclBl.getClosedBy();
    }

    public void setClosedBy(String closedBy) {
        fclBl.setClosedBy(null != closedBy ? closedBy.toUpperCase() : null);
    }

    public String getBlAudited() {
        return fclBl.getBlAudited();
    }

    public void setBlAudited(String blAudited) {
        fclBl.setBlAudited(blAudited);
    }

    public String getBlClosed() {
        return fclBl.getBlClosed();
    }

    public void setBlClosed(String blClosed) {
        fclBl.setBlClosed(blClosed);
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getQuoteBy() {
        return quoteBy;
    }

    public void setQuoteBy(String quoteBy) {
        this.quoteBy = quoteBy;
    }

    public String getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(String quoteDate) {
        this.quoteDate = quoteDate;
    }

    public String getVessel() {
        return null != fclBl.getVessel() ? fclBl.getVessel().getCode() : "";
    }

    public void setVessel(String vessel) throws Exception {
        fclBl.setVessel(new GenericCodeDAO().getGenericCodeId("14", vessel));
    }

    public String getVesselName() {
        return null != fclBl.getVessel() ? fclBl.getVessel().getCodedesc() : "";
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getFclSsblGoCollect() {
        return fclSsblGoCollect;
    }

    public void setFclSsblGoCollect(String fclSsblGoCollect) {
        this.fclSsblGoCollect = fclSsblGoCollect;
    }

    public String getCorrectedForwarderAddress() {
        return correctedForwarderAddress;
    }

    public void setCorrectedForwarderAddress(String correctedForwarderAddress) {
        this.correctedForwarderAddress = correctedForwarderAddress;
    }

    public String getCorrectedForwarderName() {
        return correctedForwarderName;
    }

    public void setCorrectedForwarderName(String correctedForwarderName) {
        this.correctedForwarderName = correctedForwarderName;
    }

    public String getCorrectedForwarderNo() {
        return correctedForwarderNo;
    }

    public void setCorrectedForwarderNo(String correctedForwarderNo) {
        this.correctedForwarderNo = correctedForwarderNo;
    }

    public String getCorrectedShipperAddress() {
        return correctedShipperAddress;
    }

    public void setCorrectedShipperAddress(String correctedShipperAddress) {
        this.correctedShipperAddress = correctedShipperAddress;
    }

    public String getCorrectedShipperName() {
        return correctedShipperName;
    }

    public void setCorrectedShipperName(String correctedShipperName) {
        this.correctedShipperName = correctedShipperName;
    }

    public String getCorrectedShipperNo() {
        return correctedShipperNo;
    }

    public void setCorrectedShipperNo(String correctedShipperNo) {
        this.correctedShipperNo = correctedShipperNo;
    }

    public String getCorrectedThirdPartyAddress() {
        return correctedThirdPartyAddress;
    }

    public void setCorrectedThirdPartyAddress(String correctedThirdPartyAddress) {
        this.correctedThirdPartyAddress = correctedThirdPartyAddress;
    }

    public String getCorrectedThirdPartyName() {
        return correctedThirdPartyName;
    }

    public void setCorrectedThirdPartyName(String correctedThirdPartyName) {
        this.correctedThirdPartyName = correctedThirdPartyName;
    }

    public String getCorrectedThirdPartyNo() {
        return correctedThirdPartyNo;
    }

    public void setCorrectedThirdPartyNo(String correctedThirdPartyNo) {
        this.correctedThirdPartyNo = correctedThirdPartyNo;
    }

    public String getDocCutOff() throws Exception {
        return DateUtils.formatDate(fclBl.getDocCutOff(), "MM/dd/yyyy HH:mm a");
    }

    public void setDocCutOff(String docCutOff) throws Exception {
        fclBl.setDocCutOff(DateUtils.parseDate(docCutOff, "MM/dd/yyyy HH:mm a"));
    }

    public String getPortCutOff() throws Exception {
        return DateUtils.formatDate(fclBl.getPortCutOff(), "MM/dd/yyyy HH:mm a");
    }

    public void setPortCutOff(String portCutOff) throws Exception {
        fclBl.setPortCutOff(DateUtils.parseDate(portCutOff, "MM/dd/yyyy HH:mm a"));
    }

    public String getEarlierPickUpDate() throws Exception {
        return DateUtils.formatDate(fclBl.getEarlierPickUpDate(), "MM/dd/yyyy HH:mm a");
    }

    public void setEarlierPickUpDate(String earlierPickUpDate) throws Exception {
        fclBl.setEarlierPickUpDate(DateUtils.parseDate(earlierPickUpDate, "MM/dd/yyyy HH:mm a"));
    }

    public String getRatedManifest() {
        return ratedManifest;
    }

    public void setRatedManifest(String ratedManifest) {
        this.ratedManifest = ratedManifest;
    }

    public String getFreightInvoiceContacts() {
        return freightInvoiceContacts;
    }

    public void setFreightInvoiceContacts(String freightInvoiceContacts) {
        this.freightInvoiceContacts = freightInvoiceContacts;
    }

    public String getAesComment() {
        return aesComment;
    }

    public void setAesComment(String aesComment) {
        this.aesComment = aesComment;
    }

    public String getOmit2LetterCountryCode() {
        return omit2LetterCountryCode;
    }

    public void setOmit2LetterCountryCode(String omit2LetterCountryCode) {
        this.omit2LetterCountryCode = omit2LetterCountryCode;
    }

    public String getDoorOriginAsPlorHouse() {
        return null != doorOriginAsPlorHouse ? doorOriginAsPlorHouse : "Yes";
    }

    public void setDoorOriginAsPlorHouse(String doorOriginAsPlorHouse) {
        this.doorOriginAsPlorHouse = doorOriginAsPlorHouse;
    }

    public String getDockReceipt() {
        return null != dockReceipt ? dockReceipt : "No";
    }

    public void setDockReceipt(String dockReceipt) {
        this.dockReceipt = null != dockReceipt ? dockReceipt : "No";
    }

    public String getInsuranceAllowed() throws Exception {
        return new PortsDAO().getInsuranceAllowed();
    }

    public void setInsuranceAllowed(String insuranceAllowed) throws Exception {
        this.insuranceAllowed = insuranceAllowed;
    }

    public boolean isIsBulletRate() throws Exception {
        String fileNo = getFclBl().getFileNo();
        fileNo = fileNo.contains("-") ? fileNo.substring(0, -1 != fileNo.indexOf("-") ? fileNo.indexOf("-") : fileNo.length()) : fileNo;
        return null != getFclBl() && null != getFclBl().getFileNo() ? new QuotationDAO().isBulletRateByFileNo(fileNo) : false;
    }

    public void setIsBulletRate(boolean isBulletRate) {
        this.isBulletRate = isBulletRate;
    }

    public String getBookingContact() {
        return bookingContact;
    }

    public void setBookingContact(String bookingContact) {
        this.bookingContact = bookingContact;
    }

    public String getSpotRate() {
        return null != spotRate ? spotRate : "No";
    }

    public void setSpotRate(String spotRate) {
        this.spotRate = null != spotRate ? spotRate : "No";
    }

    public String getResendCostToBlue() {
        return resendCostToBlue;
    }

    public void setResendCostToBlue(String resendCostToBlue) {
        this.resendCostToBlue = resendCostToBlue;
    }
  public String getiIconBillToolTip() {
        return iIconBillToolTip;
    }

    public void setiIconBillToolTip(String iIconBillToolTip) {
        this.iIconBillToolTip = iIconBillToolTip;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    
}
