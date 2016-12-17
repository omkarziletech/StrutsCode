/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.lcl.LclBookingPlan;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;

/**
 *
 * @author lakshh
 */
public class LclBookingPlanForm extends LogiwareActionForm {

    private LclBookingPlan lclBookingPlan;
    private String fileNumber;
    private Integer fromId;
    private Integer toId;
    private String schedule;

    public LclBookingPlanForm() {
        if (lclBookingPlan == null) {
            lclBookingPlan = new LclBookingPlan();
        }
        if (lclBookingPlan.getLclFileNumber() == null) {
            lclBookingPlan.setLclFileNumber(new LclFileNumber());
        }
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Long getFileNumberId() {
        return lclBookingPlan.getLclFileNumber().getId();
    }

    public void setFileNumberId(Long fileNumberId) {
        lclBookingPlan.getLclFileNumber().setId(fileNumberId);
    }

    public String getFromAtd() {
        String date = "";
        if (lclBookingPlan.getFromAtd() != null) {
            date = DateUtils.parseDateToString(lclBookingPlan.getFromAtd());
        }
        return date;
    }

    public void setFromAtd(String fromAtd) throws Exception {
        lclBookingPlan.setFromAtd(DateUtils.parseDate(fromAtd, "dd-MMM-yyyy"));
    }

    public String getFromEtd() throws Exception {
        String date = "";
        if (lclBookingPlan.getFromEtd() != null) {
            date = DateUtils.parseDateToString(lclBookingPlan.getFromEtd());
        }
        return date;
    }

    public void setFromEtd(String fromEtd) throws Exception {
        lclBookingPlan.setFromAtd(DateUtils.parseDate(fromEtd, "dd-MMM-yyyy"));
    }

    public LclBookingPlan getLclBookingPlan() {
        return lclBookingPlan;
    }

    public void setLclBookingPlan(LclBookingPlan lclBookingPlan) {
        this.lclBookingPlan = lclBookingPlan;
    }

    public String getRelayOverride() {
        if (lclBookingPlan.getRelayOverride() == TRUE) {
            return Y;
        }
        return N;
    }

    public void setRelayOverride(String relayOverride) {
        if (relayOverride.equals(Y)) {
            lclBookingPlan.setRelayOverride(TRUE);
        } else {
            lclBookingPlan.setRelayOverride(FALSE);
        }
    }

    public String getSegNo() {
        if (lclBookingPlan.getSegNo() != null) {
            return "" + lclBookingPlan.getSegNo();
        }
        return "";
    }

    public void setSegNo(String segNo) {
        if (CommonUtils.isNotEmpty(segNo)) {
            lclBookingPlan.setSegNo(new Integer(segNo));
        } else {
            lclBookingPlan.setSegNo(0);
        }

    }

    public String getSpRef() {
        return lclBookingPlan.getSpReference();
    }

    public void setSpRef(String spRef) {
        lclBookingPlan.setSpReference(spRef);
    }

    public String getSpVessel() {
        return lclBookingPlan.getSpVessel();
    }

    public void setSpVessel(String spVessel) {
        lclBookingPlan.setSpVessel(spVessel);
    }

    public String getToAta() {
        String date = "";
        if (lclBookingPlan.getToAta() != null) {
            date = DateUtils.parseDateToString(lclBookingPlan.getToAta());
        }
        return date;
    }

    public void setToAtd(String toAta) throws Exception {
        lclBookingPlan.setFromAtd(DateUtils.parseDate(toAta, "dd-MMM-yyyy"));
    }

    public String getToEta() {
        String date = "";
        if (lclBookingPlan.getToEta() != null) {
            date = DateUtils.parseDateToString(lclBookingPlan.getToEta());
        }
        return date;
    }

    public void setToEtd(String toEta) throws Exception {
        lclBookingPlan.setFromAtd(DateUtils.parseDate(toEta, "dd-MMM-yyyy"));
    }

    public String getTransMode() {
        if (CommonUtils.isEmpty(lclBookingPlan.getTransMode())) {
            return "T";
        }
        return lclBookingPlan.getTransMode();
    }

    public void setTransMode(String transMode) {
        lclBookingPlan.setTransMode(transMode);
    }

    public String getSpAcct() {
        if (lclBookingPlan.getSpAcct() != null) {
            return lclBookingPlan.getSpAcct().getAccountno();
        }
        return "";
    }

    public void setSpAcct(String spAcct) {
        if (CommonUtils.isNotEmpty(spAcct)) {
            lclBookingPlan.setSpAcct(new TradingPartner(spAcct));
        }
    }

    public String getSpContact() {
        if (lclBookingPlan.getSpContact() != null) {
            return lclBookingPlan.getSpContact().getContactName();
        }
        return "";
    }

    public void setSpContact(String spContact) {
        lclBookingPlan.setSpContact(new LclContact());
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
