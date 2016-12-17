/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPad;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlPadDAO;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclBlPickupInfoForm extends LogiwareActionForm {
private static final Logger log = Logger.getLogger(LclBlPickupInfoForm.class);
    private Long fileNumberId;
    private Long lclBookingPadId;
    private String fileNumber;
    private String doorOriginCityZip;
    private String cityStateZip;
    private String lclContactId;
    private String whsecompanyName;
    private String whsePhone;
    private String whseCity;
    private String whseState;
    private String whseZip;
    private String whseAddress;
    private LclBlPad lclBlPad;
    private LclFileNumber lclFileNumber;
    private String issuingTerminal;
    private String trmnum;
    private String dupShipper;
    private String sailDate;
    private String origin;
    private String scacCode;
    private String rateType;
    private String to;

    public LclBlPickupInfoForm() {

        if (lclBlPad == null) {
            lclBlPad = new LclBlPad();
        }
    }

    public Long getLclBookingPadId() {
        return lclBookingPadId;
    }

    public void setLclBookingPadId(Long lclBookingPadId) {
        this.lclBookingPadId = lclBookingPadId;
    }

    public LclBlPad getLclBlPad() {
        return lclBlPad;
    }

    public void setLclBlPad(LclBlPad lclBlPad) {
        this.lclBlPad = lclBlPad;
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

    public String getContactName() {
        return lclBlPad.getPickupContact().getContactName();
    }

    public void setContactName(String contactName) {
        lclBlPad.getPickupContact().setContactName(contactName);
    }

    public String getCompanyName() {
        return lclBlPad.getPickupContact().getCompanyName();
    }

    public void setCompanyName(String companyName) {
        lclBlPad.getPickupContact().setCompanyName(companyName);
    }

    public String getAddress() {
        return lclBlPad.getPickupContact().getAddress();
    }

    public void setAddress(String address) {
        lclBlPad.getPickupContact().setAddress(address);
    }

    public String getCityStateZip() {
        return lclBlPad.getPickupContact().getCity();
    }

    public void setCityStateZip(String cityStateZip) {
        lclBlPad.getPickupContact().setCity(cityStateZip);
    }

    public String getWhsecompanyName() {
        return lclBlPad.getDeliveryContact().getCompanyName();
    }

    public void setWhsecompanyName(String whsecompanyName) {
        lclBlPad.getDeliveryContact().setCompanyName(whsecompanyName);
    }

    public String getWhseAddress() {
        return lclBlPad.getDeliveryContact().getAddress();
    }

    public void setWhseAddress(String whseAddress) {
        lclBlPad.getDeliveryContact().setAddress(whseAddress);
    }

    public String getWhseCity() {
        return lclBlPad.getDeliveryContact().getCity();
    }

    public void setWhseCity(String whseCity) {
        lclBlPad.getDeliveryContact().setCity(whseCity);
    }

    public String getWhsePhone() {
        return lclBlPad.getDeliveryContact().getPhone1();
    }

    public void setWhsePhone(String whsePhone) {
        lclBlPad.getDeliveryContact().setPhone1(whsePhone);
    }

    public String getWhseState() {
        return lclBlPad.getDeliveryContact().getState();
    }

    public void setWhseState(String whseState) {
        lclBlPad.getDeliveryContact().setState(whseState);
    }

    public String getWhseZip() {
        return lclBlPad.getDeliveryContact().getZip();
    }

    public void setWhseZip(String whseZip) {
        lclBlPad.getDeliveryContact().setZip(whseZip);
    }

    public String getCity() {
        return lclBlPad.getPickupContact().getCity();
    }

    public void setCity(String city) {
        lclBlPad.getPickupContact().setCity(city);
    }

    public String getState() {
        return lclBlPad.getPickupContact().getState();
    }

    public void setState(String state) {
        lclBlPad.getPickupContact().setState(state);
    }

    public String getZip() {
        return lclBlPad.getPickupContact().getZip();
    }

    public void setZip(String zip) {
        lclBlPad.getPickupContact().setZip(zip);
    }

    public String getCountry() {
        return lclBlPad.getPickupContact().getCountry();
    }

    public void setCountry(String country) {
        lclBlPad.getPickupContact().setCountry(country);
    }

    public String getPhone1() {
        return lclBlPad.getPickupContact().getPhone1();
    }

    public void setPhone1(String phone1) {
        lclBlPad.getPickupContact().setPhone1(phone1);
    }

    public String getFax1() {
        return lclBlPad.getPickupContact().getFax1();
    }

    public void setFax1(String fax1) {
        lclBlPad.getPickupContact().setFax1(fax1);
    }

    public String getEmail1() {
        return lclBlPad.getPickupContact().getEmail1();
    }

    public void setEmail1(String email1) {
        lclBlPad.getPickupContact().setEmail1(email1);
    }

    public String getRemarks() {
        return lclBlPad.getPickupContact().getRemarks();
    }

    public void setRemarks(String remarks) {
        lclBlPad.getPickupContact().setRemarks(remarks);
    }

    public String getDoorOriginCityZip() {
        return doorOriginCityZip;
    }

    public void setDoorOriginCityZip(String doorOriginCityZip) {
        this.doorOriginCityZip = doorOriginCityZip;
    }

    public String getLclContactId() {
        return lclContactId;
    }

    public void setLclContactId(String lclContactId) {
        this.lclContactId = lclContactId;
    }

    public String getCommodityDesc() {
        return lclBlPad.getCommodityDesc();
    }

    public void setCommodityDesc(String commodityDesc) {
        lclBlPad.setCommodityDesc(commodityDesc.toUpperCase());
    }

    public String getSpReferenceNo() {
        return lclBlPad.getSpReferenceNo();
    }

    public void setSpReferenceNo(String spReferenceNo) {
        lclBlPad.setSpReferenceNo(spReferenceNo);
    }

    public String getSpAccrualAmount() {
        if (lclBlPad.getSpAccrualAmount() != null) {
            return "" + lclBlPad.getSpAccrualAmount();
        }
        return null;
    }

    public void setSpAccrualAmount(String spAccrualAmount) {
        if (CommonUtils.isNotEmpty(spAccrualAmount)) {
            lclBlPad.setSpAccrualAmount(new BigDecimal(spAccrualAmount));
        }
    }

    public String getChargeAmount() {
        if (lclBlPad.getSpAccrualAmount() != null) {
            return "" + lclBlPad.getChargeAmount();
        }
        return null;
    }

    public void setChargeAmount(String chargeAmount) {
        if (CommonUtils.isNotEmpty(chargeAmount)) {
            lclBlPad.setChargeAmount(new BigDecimal(chargeAmount));
        }
    }

    public String getPickupHours() {
        return lclBlPad.getPickupHours();
    }

    public void setPickupHours(String pickupHours) {
        lclBlPad.setPickupHours(pickupHours);
    }

    public String getPickupCutoffDate()throws Exception {
        if (lclBlPad.getPickupCutoffDate() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclBlPad.getPickupCutoffDate());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPickupCutoffDate(String pickupCutoffDate)throws Exception {
        if (CommonUtils.isNotEmpty(pickupCutoffDate)) {
            lclBlPad.setPickupCutoffDate(DateUtils.parseDate(pickupCutoffDate, "dd-MMM-yyyy"));
        }
    }

    public String getPickupReadyDate()throws Exception {
        if (lclBlPad.getPickupReadyDate() != null) {
            String d = DateUtils.formatStringDateToAppFormatMMM(lclBlPad.getPickupReadyDate());
            return null == d ? "" : d;
        }
        return "";
    }

    public void setPickupReadyDate(String pickupReadyDate)throws Exception {
        if (CommonUtils.isNotEmpty(pickupReadyDate)) {
            lclBlPad.setPickupReadyDate(DateUtils.parseDate(pickupReadyDate, "dd-MMM-yyyy"));
        }
    }

    public String getPickupReferenceNo() {
        return lclBlPad.getPickupReferenceNo();
    }

    public void setPickupReferenceNo(String pickupReferenceNo) {
        lclBlPad.setPickupReferenceNo(pickupReferenceNo);
    }

    public String getPickupInstructions() {
        return lclBlPad.getPickupInstructions();
    }

    public void setPickupInstructions(String pickupInstructions) {
        lclBlPad.setPickupInstructions(pickupInstructions.toUpperCase());
    }

    public String getPickupReadyNote() {
        return lclBlPad.getPickupReadyNote();
    }

    public void setPickupReadyNote(String pickupReadyNote) {
        lclBlPad.setPickupReadyNote(pickupReadyNote);
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getPickedupDatetime() {
        return lclBlPad.getPickedupDatetime();
    }

    public void setPickedupDatetime(Date pickedupDatetime) {
        lclBlPad.setPickedupDatetime(pickedupDatetime);
    }

    public String getDeliveryInstructions() {
        return lclBlPad.getDeliveryInstructions();
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        lclBlPad.setDeliveryInstructions(deliveryInstructions);
    }

    public Date getDeliveredDatetime() {
        return lclBlPad.getDeliveredDatetime();
    }

    public void setDeliveredDatetime(Date deliveredDatetime) {
        lclBlPad.setDeliveredDatetime(deliveredDatetime);
    }

    public String getTermsOfService() {
        return lclBlPad.getTermsOfService();
    }

    public void setTermsOfService(String termsOfService) {
        lclBlPad.setTermsOfService(termsOfService.toUpperCase());
    }

    public LclContact getPickupContact() {
        return lclBlPad.getPickupContact();
    }

    public void setPickupContact(LclContact pickupContact) {
        lclBlPad.setPickupContact(pickupContact);
    }

    public LclContact getDeliverContact() {
        return lclBlPad.getDeliveryContact();
    }

    public void setDeliveryContact(LclContact deliveryContact) {
        lclBlPad.setDeliveryContact(deliveryContact);
    }

    public String getSpAcct() {
        if (lclBlPad.getSpAcct() != null) {
            return lclBlPad.getSpAcct().getAccountno();
        }
        return null;
    }

    public void setSpAcct(String spAcct)throws Exception {
        if (CommonUtils.isNotEmpty(spAcct)) {
            lclBlPad.setSpAcct(new TradingPartnerDAO().findById(spAcct));
        }
    }

    public User getEnteredBy() {
        return lclBlPad.getEnteredBy();
    }

    public void setEnteredBy(User enteredBy) {
        lclBlPad.setEnteredBy(enteredBy);
    }

    public User getModifiedBy() {
        return lclBlPad.getModifiedBy();
    }

    public void setModifiedBy(User modifiedBy) {
        lclBlPad.setModifiedBy(modifiedBy);
    }

    public String getIssuingTerminal() {
        if (lclBlPad.getIssuingTerminal() != null) {
            StringBuilder builder = new StringBuilder();
            if (CommonUtils.isNotEmpty(lclBlPad.getIssuingTerminal().getTerminalLocation()) && CommonUtils.isNotEmpty(lclBlPad.getIssuingTerminal().getTrmnum())) {
                builder.append(lclBlPad.getIssuingTerminal().getTerminalLocation() + "/" + lclBlPad.getIssuingTerminal().getTrmnum());
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getTrmnum() {
        if (lclBlPad.getIssuingTerminal() != null) {
            return lclBlPad.getIssuingTerminal().getTrmnum();
        }
        return null;
    }

    public void setTrmnum(String trmnum)throws Exception {
        if (CommonUtils.isNotEmpty(trmnum)) {
            RefTerminal terminal = new RefTerminalDAO().findById(trmnum);
            lclBlPad.setIssuingTerminal(terminal);
        }
    }

    public String getDupShipper() {
        return dupShipper;
    }

    public void setDupShipper(String dupShipper) {
        this.dupShipper = dupShipper;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getScacCode() {
        return lclBlPad.getScac();
    }

    public void setScacCode(String scacCode) {
       lclBlPad.setScac(scacCode.toUpperCase());
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        String fileId = request.getParameter("fileNumberId");
        if (CommonUtils.isNotEmpty(fileId)) {
            try {
                lclBlPad = new LclBlPadDAO().executeUniqueQuery("from LclBlPad where lclFileNumber.id=" + Long.parseLong(fileId));
            } catch (Exception ex) {
                log.info("reset()in LclBlPickupInfoForm failed on " + new Date(),ex);
            }
        }
        if (lclBlPad == null) {
            lclBlPad = new LclBlPad();
        }
    }
}
