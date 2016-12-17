/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingAc;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclPickupInfoForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclPickupInfoForm.class);
    private Long fileNumberId;
    private String duplicateChargeAmount;
    private String pickupCost;
    private String chargeAmount;
    private Long lclBookingPadId;
    private String fileNumber;
    private String cityStateZip;
    private LclBookingPad lclBookingPad;
    private LclFileNumber lclFileNumber;
    private String issuingTerminal;
    private String trmnum;
    private String dupShipper;
    private String manualShipper;
    //Shipper Vendor Fields
    private String toAccountName;
    private String toAccountNo;
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
    private String manualCompanyName;
    //Delivery Contact Details
    private String whsecompanyName;
    private String whseAddress;
    private String whseCity;
    private String whseState;
    private String whseZip;
    private String whsePhone;
    private String whseFax;
    private String whseNo;
    private String whseId;
    //Import
    private String doorDeliveryStatus;
    private String podSigned;
    private String podDate;
    private String unitId;
    private String headerId;
    private String lastFreeDate;
    private String sendEdiToCtsFlag;
    private String originCityZip;
    private String scac;
    private String carrier;
    private String doorDeliveryEta;
    //module
    private String moduleName;
    private String costVendorAcct;//cost vendor Name
    private String costVendorNo;
    private String shipperCity;
    private String shipperState;
    private String shipperZip;
    private String estPickupDate;
    private String estimatedDeliveryDate;
    private String pickedupDatetime;
    private String deliveredDatetime;
    private String deliveryNotes;
    private boolean pickUpInfo;

    public LclPickupInfoForm() {
        if (lclBookingPad == null) {
            lclBookingPad = new LclBookingPad();
        }
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public String getToAccountNo() {
        return toAccountNo;
    }

    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
    }

    public Long getLclBookingPadId() {
        return lclBookingPadId;
    }

    public void setLclBookingPadId(Long lclBookingPadId) {
        this.lclBookingPadId = lclBookingPadId;
    }

    public LclBookingPad getLclBookingPad() {
        return lclBookingPad;
    }

    public void setLclBookingPad(LclBookingPad lclBookingPad) {
        this.lclBookingPad = lclBookingPad;
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

    public String getDuplicateChargeAmount() {
        return duplicateChargeAmount;
    }

    public void setDuplicateChargeAmount(String duplicateChargeAmount) {
        this.duplicateChargeAmount = duplicateChargeAmount;
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

    public String getCommodityDesc() {
        return lclBookingPad.getCommodityDesc();
    }

    public void setCommodityDesc(String commodityDesc) {
        lclBookingPad.setCommodityDesc(commodityDesc.toUpperCase());
    }

    public String getPickupHours() {
        return lclBookingPad.getPickupHours();
    }

    public void setPickupHours(String pickupHours) {
        lclBookingPad.setPickupHours(pickupHours);
    }

    public String getDeliveryReferenceNo() {
        return lclBookingPad.getDeliveryReferenceNo();
    }

    public void setDeliveryReferenceNo(String deliveryReferenceNo) {
        lclBookingPad.setDeliveryReferenceNo(deliveryReferenceNo);
    }

    public String getPickupCutoffDate() throws Exception {
        if (lclBookingPad.getPickupCutoffDate() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclBookingPad.getPickupCutoffDate());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPickupCutoffDate(String pickupCutoffDate) throws Exception {
        if (CommonUtils.isNotEmpty(pickupCutoffDate)) {
            lclBookingPad.setPickupCutoffDate(DateUtils.parseDate(pickupCutoffDate, "dd-MMM-yyyy"));
        }
    }

    public String getPickupReadyDate() throws Exception {
        if (lclBookingPad.getPickupReadyDate() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclBookingPad.getPickupReadyDate());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPickupReadyDate(String pickupReadyDate) throws Exception {
        if (CommonUtils.isNotEmpty(pickupReadyDate)) {
            lclBookingPad.setPickupReadyDate(DateUtils.parseDate(pickupReadyDate, "dd-MMM-yyyy"));
        }
    }

    public String getPickupReferenceNo() {
        return lclBookingPad.getPickupReferenceNo();
    }

    public void setPickupReferenceNo(String pickupReferenceNo) {
        lclBookingPad.setPickupReferenceNo(pickupReferenceNo);
    }

    public String getPickupInstructions() {
        return lclBookingPad.getPickupInstructions();
    }

    public void setPickupInstructions(String pickupInstructions) {
        lclBookingPad.setPickupInstructions(pickupInstructions.toUpperCase());
    }

    public String getPickupReadyNote() {
        return lclBookingPad.getPickupReadyNote();
    }

    public void setPickupReadyNote(String pickupReadyNote) {
        lclBookingPad.setPickupReadyNote(pickupReadyNote);
    }

    public String getDeliveryInstructions() {
        return lclBookingPad.getDeliveryInstructions();
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        lclBookingPad.setDeliveryInstructions(deliveryInstructions.toUpperCase());
    }

    public String getDeliveryReadyDate() throws Exception {
        if (lclBookingPad.getDeliveryReadyDate() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclBookingPad.getDeliveryReadyDate());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setDeliveryReadyDate(String deliveryReadyDate) throws Exception {
        if (CommonUtils.isNotEmpty(deliveryReadyDate)) {
            lclBookingPad.setDeliveryReadyDate(DateUtils.parseDate(deliveryReadyDate, "dd-MMM-yyyy"));
        }
    }

    public String getTermsOfService() {
        String moduleName = this.moduleName;
        if (lclBookingPad.getTermsOfService() == null && moduleName.equals("Imports")) {
            return "Prepaid 3rd Party";
        } else {
            return lclBookingPad.getTermsOfService();
        }
    }

    public void setTermsOfService(String termsOfService) {
        lclBookingPad.setTermsOfService(termsOfService.toUpperCase());
    }

    public LclBookingAc getLclBookingAc() {
        return lclBookingPad.getLclBookingAc();
    }

    public void setLclBookingAc(LclBookingAc bookingAc) {
        lclBookingPad.setLclBookingAc(bookingAc);
    }

    public User getEnteredBy() {
        return lclBookingPad.getEnteredBy();
    }

    public void setEnteredBy(User enteredBy) {
        lclBookingPad.setEnteredBy(enteredBy);
    }

    public User getModifiedBy() {
        return lclBookingPad.getModifiedBy();
    }

    public void setModifiedBy(User modifiedBy) {
        lclBookingPad.setModifiedBy(modifiedBy);
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getTrmnum() {
        if (lclBookingPad.getIssuingTerminal() != null) {
            return lclBookingPad.getIssuingTerminal().getTrmnum();
        }
        return null;
    }

    public void setTrmnum(String trmnum) throws Exception {
        if (CommonUtils.isNotEmpty(trmnum)) {
            RefTerminal terminal = new RefTerminalDAO().findById(trmnum);
            lclBookingPad.setIssuingTerminal(terminal);
        }
        this.trmnum = trmnum;
    }

    public String getDupShipper() {
        return dupShipper;
    }

    public void setDupShipper(String dupShipper) {
        this.dupShipper = dupShipper;
    }

    public String getManualShipper() {
        return manualShipper;
    }

    public void setManualShipper(String manualShipper) {
        this.manualShipper = manualShipper;
    }

    public String getScacCode() {
        return lclBookingPad.getScac();
    }

    public void setScacCode(String scacCode) {
        lclBookingPad.setScac(scacCode.toUpperCase());
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

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
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

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getManualCompanyName() {
        return manualCompanyName;
    }

    public void setManualCompanyName(String manualCompanyName) {
        this.manualCompanyName = manualCompanyName;
    }

    public String getCityStateZip() {
        return cityStateZip;
    }

    public void setCityStateZip(String cityStateZip) {
        this.cityStateZip = cityStateZip;
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

    public String getWhseFax() {
        return whseFax;
    }

    public void setWhseFax(String whseFax) {
        this.whseFax = whseFax;
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

    public String getWhseId() {
        return whseId;
    }

    public void setWhseId(String whseId) {
        this.whseId = whseId;
    }

    public String getLastFreeDate() {
        return lastFreeDate;
    }

    public void setLastFreeDate(String lastFreeDate) {
        this.lastFreeDate = lastFreeDate;
    }

    public String getSendEdiToCtsFlag() {
        return sendEdiToCtsFlag;
    }

    public void setSendEdiToCtsFlag(String sendEdiToCtsFlag) {
        this.sendEdiToCtsFlag = sendEdiToCtsFlag;
    }

    public String getOriginCityZip() {
        return originCityZip;
    }

    public void setOriginCityZip(String originCityZip) {
        this.originCityZip = originCityZip;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDoorDeliveryEta() {
        return doorDeliveryEta;
    }

    public void setDoorDeliveryEta(String doorDeliveryEta) {
        this.doorDeliveryEta = doorDeliveryEta;
    }

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
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

    public String getShipperZip() {
        return shipperZip;
    }

    public void setShipperZip(String shipperZip) {
        this.shipperZip = shipperZip;
    }

    public String getEstPickupDate() {
        return estPickupDate;
    }

    public void setEstPickupDate(String estPickupDate) {
        this.estPickupDate = estPickupDate;
    }

    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public String getPickedupDatetime() {
        return pickedupDatetime;
    }

    public void setPickedupDatetime(String pickedupDatetime) {
        this.pickedupDatetime = pickedupDatetime;
    }

    public String getDeliveredDatetime() {
        return deliveredDatetime;
    }

    public void setDeliveredDatetime(String deliveredDatetime) {
        this.deliveredDatetime = deliveredDatetime;
    }

    public boolean isPickUpInfo() {
        return pickUpInfo;
    }

    public void setPickUpInfo(boolean pickUpInfo) {
        this.pickUpInfo = pickUpInfo;
    }
    

    public String getDeliveryCommercial() {
        if (null != lclBookingPad.getDeliveryCommercial()) {
            if (lclBookingPad.getDeliveryCommercial() == TRUE) {
                return Y;
            } else {
                return N;
            }
        }
        return Y;
    }

    public void setDeliveryCommercial(String deliveryCommercial) {
        if (CommonUtils.isNotEmpty(deliveryCommercial)) {
            if ("Y".equalsIgnoreCase(deliveryCommercial)) {
                lclBookingPad.setDeliveryCommercial(TRUE);
            } else {
                lclBookingPad.setDeliveryCommercial(FALSE);
            }
        }
    }

    public String getDeliveryNotes() {
        return lclBookingPad.getDeliveryNotes();
    }

    public void setDeliveryNotes(String deliveryNotes) {
        if (CommonUtils.isNotEmpty(deliveryNotes)) {
            lclBookingPad.setDeliveryNotes(deliveryNotes.length() > 200 ? deliveryNotes.substring(0, 200) : deliveryNotes);
        } else {
            lclBookingPad.setDeliveryNotes(deliveryNotes);
        }
    }

    public String getLiftGate() {
        if (null != lclBookingPad.getLiftGate()) {
            if (lclBookingPad.getLiftGate() == TRUE) {
                return Y;
            }
        }
        return N;
    }

    public void setLiftGate(String liftGate) {
        if (CommonUtils.isNotEmpty(liftGate)) {
            if ("Y".equalsIgnoreCase(liftGate)) {
                lclBookingPad.setLiftGate(TRUE);
            } else {
                lclBookingPad.setLiftGate(FALSE);
            }
        }
    }

    public String getNeedPOD() {
        if(null != lclBookingPad.getNeedPOD()){
        if (lclBookingPad.getNeedPOD() == TRUE) {
            return Y;
        }
        }
        return N;
    }

    public void setNeedPOD(String needPOD) {
        if (CommonUtils.isNotEmpty(needPOD)) {
            if ("Y".equalsIgnoreCase(needPOD)) {
                lclBookingPad.setNeedPOD(TRUE);
            } else {
                lclBookingPad.setNeedPOD(FALSE);
            }
        }
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        String fileId = request.getParameter("fileNumberId");
        if (CommonUtils.isNotEmpty(fileId)) {
            try {
                lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(Long.parseLong(fileId));
            } catch (Exception ex) {
                log.info("reset()in LclPickupInfoForm failed on " + new Date(), ex);
            }
        }
        if (lclBookingPad == null) {
            lclBookingPad = new LclBookingPad();
        }
    }
}
