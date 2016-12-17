/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuoteAc;
import com.gp.cong.logisoft.domain.lcl.LclQuotePad;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePadDAO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclQuotePickupInfoForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclQuotePickupInfoForm.class);
    private Long fileNumberId;
    private Long lclQuotePadId;
    private String fileNumber;
    private String pickupCost;
    private String chargeAmount;
    private String cityStateZip;
    private String lclContactId;
    private String manualShipper;
    private LclQuotePad lclQuotePad;
    private LclFileNumber lclFileNumber;
    private String issuingTerminal;
    private String trmnum;
    private String dupShipper;
    private String newWhse;
    private String whseAccountNo;
    private String scacCode;
    //Shipper Vendors
    private String toVendorName;
    private String toVendorNo;
    private String duplicateChargeAmount;
    //PickUp Contact Details
    private String companyName;
    private String shipperAccountNo;
    private String companyNameDup;
    private String newCompanyName;
    private String address;
    private String fax1;
    private String email1;
    private String contactName;
    private String phone1;
    private String accountNo;
    private String zip;
    private String manualCompanyName;
    private String manualWhseName;
    //Delivery Contact Details
    private String whsecompanyName;
    private String whseAddress;
    private String whseCity;
    private String whseState;
    private String whseZip;
    private String whsePhone;
    private String whseNo;
    private String whseId;
    //Import
    private String doorDeliveryStatus;
    private String podSigned;
    private String podDate;
    private String moduleName;//moduleName
    private String costVendorAcct;//cost vendor Name
    private String costVendorNo;
    private String doorDeliveryEta;
    private String shipperCity;
    private String shipperState;
    private boolean pickUpInfo;

    public LclQuotePickupInfoForm() {
        if (lclQuotePad == null) {
            lclQuotePad = new LclQuotePad();
        }
    }

    public LclQuoteAc getLclQuoteAc() {
        return lclQuotePad.getLclQuoteAc();
    }

    public void setLclQuoteAc(LclQuoteAc quoteAc) {
        lclQuotePad.setLclQuoteAc(quoteAc);
    }

    public String getDuplicateChargeAmount() {
        return duplicateChargeAmount;
    }

    public void setDuplicateChargeAmount(String duplicateChargeAmount) {
        this.duplicateChargeAmount = duplicateChargeAmount;
    }

    public Long getLclQuotePadId() {
        return lclQuotePadId;
    }

    public void setLclQuotePadId(Long lclQuotePadId) {
        this.lclQuotePadId = lclQuotePadId;
    }

    public LclQuotePad getLclQuotePad() {
        return lclQuotePad;
    }

    public void setLclQuotePad(LclQuotePad lclQuotePad) {
        this.lclQuotePad = lclQuotePad;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getLclContactId() {
        return lclContactId;
    }

    public void setLclContactId(String lclContactId) {
        this.lclContactId = lclContactId;
    }

    public String getCommodityDesc() {
        return lclQuotePad.getCommodityDesc();
    }

    public void setCommodityDesc(String commodityDesc) {
        lclQuotePad.setCommodityDesc(commodityDesc.toUpperCase());
    }

    public String getPickupCost() {
        return pickupCost;
    }

    public void setPickupCost(String pickupCost) {
        this.pickupCost = pickupCost;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getPickupHours() {
        return lclQuotePad.getPickupHours();
    }

    public void setPickupHours(String pickupHours) {
        lclQuotePad.setPickupHours(pickupHours);
    }

    public String getPickupCutoffDate() throws Exception {
        if (lclQuotePad.getPickupCutoffDate() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclQuotePad.getPickupCutoffDate());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPickupCutoffDate(String pickupCutoffDate) throws Exception {
        if (CommonUtils.isNotEmpty(pickupCutoffDate)) {
            lclQuotePad.setPickupCutoffDate(DateUtils.parseDate(pickupCutoffDate, "dd-MMM-yyyy"));
        }
    }

    public String getPickupReadyDate() throws Exception {
        if (lclQuotePad.getPickupReadyDate() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclQuotePad.getPickupReadyDate());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPickupReadyDate(String pickupReadyDate) throws Exception {
        if (CommonUtils.isNotEmpty(pickupReadyDate)) {
            lclQuotePad.setPickupReadyDate(DateUtils.parseDate(pickupReadyDate, "dd-MMM-yyyy"));
        }
    }

    public String getPickupReferenceNo() {
        return lclQuotePad.getPickupReferenceNo();
    }

    public void setPickupReferenceNo(String pickupReferenceNo) {
        lclQuotePad.setPickupReferenceNo(pickupReferenceNo.toUpperCase());
    }

    public String getPickupInstructions() {
        return lclQuotePad.getPickupInstructions();
    }

    public void setPickupInstructions(String pickupInstructions) {
        lclQuotePad.setPickupInstructions(pickupInstructions.toUpperCase());
    }

    public String getPickupReadyNote() {
        return lclQuotePad.getPickupReadyNote();
    }

    public void setPickupReadyNote(String pickupReadyNote) {
        lclQuotePad.setPickupReadyNote(pickupReadyNote.toUpperCase());
    }

    public Date getPickedupDatetime() {
        return lclQuotePad.getPickedupDatetime();
    }

    public void setPickedupDatetime(Date pickedupDatetime) {
        lclQuotePad.setPickedupDatetime(pickedupDatetime);
    }

    public String getDeliveryReadyDate() throws Exception {
        if (lclQuotePad.getDeliveryReadyDate() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclQuotePad.getDeliveryReadyDate());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setDeliveryReadyDate(String deliveryReadyDate) throws Exception {
        if (CommonUtils.isNotEmpty(deliveryReadyDate)) {
            lclQuotePad.setDeliveryReadyDate(DateUtils.parseDate(deliveryReadyDate, "dd-MMM-yyyy"));
        }
    }

    public String getDeliveryInstructions() {
        return lclQuotePad.getDeliveryInstructions();
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        lclQuotePad.setDeliveryInstructions(deliveryInstructions.toUpperCase());
    }

    public Date getDeliveredDatetime() {
        return lclQuotePad.getDeliveredDatetime();
    }

    public void setDeliveredDatetime(Date deliveredDatetime) {
        lclQuotePad.setDeliveredDatetime(deliveredDatetime);
    }

    public String getTermsOfService() {
        return lclQuotePad.getTermsOfService();
    }

    public void setTermsOfService(String termsOfService) {
        lclQuotePad.setTermsOfService(termsOfService.toUpperCase());
    }

    public User getEnteredBy() {
        return lclQuotePad.getEnteredBy();
    }

    public void setEnteredBy(User enteredBy) {
        lclQuotePad.setEnteredBy(enteredBy);
    }

    public User getModifiedBy() {
        return lclQuotePad.getModifiedBy();
    }

    public void setModifiedBy(User modifiedBy) {
        lclQuotePad.setModifiedBy(modifiedBy);
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getTrmnum() {
        if (lclQuotePad.getIssuingTerminal() != null) {
            return lclQuotePad.getIssuingTerminal().getTrmnum();
        }
        return null;
    }

    public void setTrmnum(String trmnum) throws Exception {
        if (CommonUtils.isNotEmpty(trmnum)) {
            RefTerminal terminal = new RefTerminalDAO().findById(trmnum);
            lclQuotePad.setIssuingTerminal(terminal);
        }
    }

    public String getDupShipper() {
        return dupShipper;
    }

    public void setDupShipper(String dupShipper) {
        this.dupShipper = dupShipper;
    }

    public String getScacCode() {
        return lclQuotePad.getScac();
    }

    public void setScacCode(String scacCode) {
        lclQuotePad.setScac(scacCode.toUpperCase());
    }

    public String getWhseAddress() {
        return whseAddress;
    }

    public void setWhseAddress(String whseAddress) {
        this.whseAddress = whseAddress;
    }

    public String getWhseCity() {
        return whseCity;
    }

    public void setWhseCity(String whseCity) {
        this.whseCity = whseCity;
    }

    public String getWhseId() {
        return whseId;
    }

    public void setWhseId(String whseId) {
        this.whseId = whseId;
    }

    public String getWhseNo() {
        return whseNo;
    }

    public void setWhseNo(String whseNo) {
        this.whseNo = whseNo;
    }

    public String getWhsePhone() {
        return whsePhone;
    }

    public void setWhsePhone(String whsePhone) {
        this.whsePhone = whsePhone;
    }

    public String getWhseState() {
        return whseState;
    }

    public void setWhseState(String whseState) {
        this.whseState = whseState;
    }

    public String getWhseZip() {
        return whseZip;
    }

    public void setWhseZip(String whseZip) {
        this.whseZip = whseZip;
    }

    public String getWhsecompanyName() {
        return whsecompanyName;
    }

    public void setWhsecompanyName(String whsecompanyName) {
        this.whsecompanyName = whsecompanyName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityStateZip() {
        return cityStateZip;
    }

    public void setCityStateZip(String cityStateZip) {
        this.cityStateZip = cityStateZip;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getShipperAccountNo() {
        return shipperAccountNo;
    }

    public void setShipperAccountNo(String shipperAccountNo) {
        this.shipperAccountNo = shipperAccountNo;
    }

    public String getCompanyNameDup() {
        return companyNameDup;
    }

    public void setCompanyNameDup(String companyNameDup) {
        this.companyNameDup = companyNameDup;
    }

    public String getNewCompanyName() {
        return newCompanyName;
    }

    public void setNewCompanyName(String newCompanyName) {
        this.newCompanyName = newCompanyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getFax1() {
        return fax1;
    }

    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    public String getManualCompanyName() {
        return manualCompanyName;
    }

    public void setManualCompanyName(String manualCompanyName) {
        this.manualCompanyName = manualCompanyName;
    }

    public String getManualShipper() {
        return manualShipper;
    }

    public void setManualShipper(String manualShipper) {
        this.manualShipper = manualShipper;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDoorDeliveryStatus() {
        return doorDeliveryStatus;
    }

    public void setDoorDeliveryStatus(String doorDeliveryStatus) {
        this.doorDeliveryStatus = doorDeliveryStatus;
    }

    public String getPodDate() {
        return podDate;
    }

    public void setPodDate(String podDate) {
        this.podDate = podDate;
    }

    public String getPodSigned() {
        return podSigned;
    }

    public void setPodSigned(String podSigned) {
        this.podSigned = podSigned;
    }

    public String getToVendorName() {
        return lclQuotePad.getPickUpTo();
    }

    public void setToVendorName(String toVendorName) {
        lclQuotePad.setPickUpTo(toVendorName.toUpperCase());
    }

    public String getToVendorNo() {
        return toVendorNo;
    }

    public void setToVendorNo(String toVendorNo) {
        this.toVendorNo = toVendorNo;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getNewWhse() {
        return newWhse;
    }

    public void setNewWhse(String newWhse) {
        this.newWhse = newWhse;
    }

    public String getWhseAccountNo() {
        return whseAccountNo;
    }

    public void setWhseAccountNo(String whseAccountNo) {
        this.whseAccountNo = whseAccountNo;
    }

    public String getManualWhseName() {
        return manualWhseName;
    }

    public void setManualWhseName(String manualWhseName) {
        this.manualWhseName = manualWhseName;
    }

    public String getCostVendorAcct() {
        return costVendorAcct;
    }

    public void setCostVendorAcct(String costVendorAcct) {
        this.costVendorAcct = costVendorAcct;
    }

    public String getCostVendorNo() {
        return costVendorNo;
    }

    public void setCostVendorNo(String costVendorNo) {
        this.costVendorNo = costVendorNo;
    }

    public String getDoorDeliveryEta() {
        return doorDeliveryEta;
    }

    public void setDoorDeliveryEta(String doorDeliveryEta) {
        this.doorDeliveryEta = doorDeliveryEta;
    }

    public String getShipperCity() {
        return shipperCity;
    }

    public void setShipperCity(String shipperCity) {
        this.shipperCity = shipperCity;
    }

    public String getShipperState() {
        return shipperState;
    }

    public void setShipperState(String shipperState) {
        this.shipperState = shipperState;
    }

    public boolean isPickUpInfo() {
        return pickUpInfo;
    }

    public void setPickUpInfo(boolean pickUpInfo) {
        this.pickUpInfo = pickUpInfo;
    }

    

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        String fileId = request.getParameter("fileNumberId");
        if (CommonUtils.isNotEmpty(fileId)) {
            try {
                lclQuotePad = new LclQuotePadDAO().executeUniqueQuery("from LclQuotePad where lclFileNumber.id=" + Long.parseLong(fileId));
            } catch (Exception ex) {
                log.info("reset()in LclQuotePickupInfoForm failed on " + new Date(), ex);
            }
        }
        if (lclQuotePad == null) {
            lclQuotePad = new LclQuotePad();
        }
    }
}
