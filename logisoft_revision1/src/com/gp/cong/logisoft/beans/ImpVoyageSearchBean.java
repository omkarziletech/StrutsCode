/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import java.io.Serializable;

/**
 *
 * @author Logiware
 */
public class ImpVoyageSearchBean implements Serializable {

    private String ssHeaderId;
    private String ssDetailId;
    private String scheduleNo;
    private String unitId;
    private String unitSsId;
    private String unitNo;
    private String dataSource;
    private String originUnCode;
    private String originName;
    private String fdUnCode;
    private String fdName;
    private String createdBy;
    private String voyOwner;
    private String terminal;
    private String carrierName;
    private String carrierAcctNo;
    private String vesselName;
    private String ssVoyage;
    private String departPierUnloc;
    private String departPier;
    private String arrivalPierUnloc;
    private String arrivalPier;
    private String etaSailDate;
    private String etaPodDate;
    private String totaltransPod;
    private String agentName;
    private String agentAcct;
    private String closedStatus;
    private String auditedStatus;
    private String closedBy;
    private String auditedBy;
    private String closedOn;
    private String auditedOn;
    private String closedRemarks;
    private String auditedRemarks;
    private String dispoCode;
    private String dispoDesc;
    private String eculineVoyage;
    private String strippeddate;
    private String hazmatPermitted;

    public ImpVoyageSearchBean() {
    }

    public String getAgentAcct() {
        return agentAcct;
    }

    public void setAgentAcct(String agentAcct) {
        this.agentAcct = agentAcct;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getArrivalPier() {
        return arrivalPier;
    }

    public void setArrivalPier(String arrivalPier) {
        this.arrivalPier = arrivalPier;
    }

    public String getArrivalPierUnloc() {
        return arrivalPierUnloc;
    }

    public void setArrivalPierUnloc(String arrivalPierUnloc) {
        this.arrivalPierUnloc = arrivalPierUnloc;
    }

    public String getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(String auditedBy) {
        this.auditedBy = auditedBy;
    }

    public String getAuditedRemarks() {
        return auditedRemarks;
    }

    public void setAuditedRemarks(String auditedRemarks) {
        this.auditedRemarks = auditedRemarks;
    }

    public String getAuditedStatus() {
        return auditedStatus;
    }

    public void setAuditedStatus(String auditedStatus) {
        this.auditedStatus = auditedStatus;
    }

    public String getCarrierAcctNo() {
        return carrierAcctNo;
    }

    public void setCarrierAcctNo(String carrierAcctNo) {
        this.carrierAcctNo = carrierAcctNo;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public String getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(String closedOn) {
        this.closedOn = closedOn;
    }

    public String getClosedRemarks() {
        return closedRemarks;
    }

    public void setClosedRemarks(String closedRemarks) {
        this.closedRemarks = closedRemarks;
    }

    public String getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(String closedStatus) {
        this.closedStatus = closedStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDepartPier() {
        return departPier;
    }

    public void setDepartPier(String departPier) {
        this.departPier = departPier;
    }

    public String getDepartPierUnloc() {
        return departPierUnloc;
    }

    public void setDepartPierUnloc(String departPierUnloc) {
        this.departPierUnloc = departPierUnloc;
    }

    public String getDispoCode() {
        return dispoCode;
    }

    public void setDispoCode(String dispoCode) {
        this.dispoCode = dispoCode;
    }

    public String getDispoDesc() {
        return dispoDesc;
    }

    public void setDispoDesc(String dispoDesc) {
        this.dispoDesc = dispoDesc;
    }

    public String getEculineVoyage() {
        return eculineVoyage;
    }

    public void setEculineVoyage(String eculineVoyage) {
        this.eculineVoyage = eculineVoyage;
    }

    public String getEtaPodDate() {
        return etaPodDate;
    }

    public void setEtaPodDate(String etaPodDate) {
        this.etaPodDate = etaPodDate;
    }

    public String getEtaSailDate() {
        return etaSailDate;
    }

    public void setEtaSailDate(String etaSailDate) {
        this.etaSailDate = etaSailDate;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public String getFdUnCode() {
        return fdUnCode;
    }

    public void setFdUnCode(String fdUnCode) {
        this.fdUnCode = fdUnCode;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginUnCode() {
        return originUnCode;
    }

    public void setOriginUnCode(String originUnCode) {
        this.originUnCode = originUnCode;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getSsDetailId() {
        return ssDetailId;
    }

    public void setSsDetailId(String ssDetailId) {
        this.ssDetailId = ssDetailId;
    }

    public String getSsHeaderId() {
        return ssHeaderId;
    }

    public void setSsHeaderId(String ssHeaderId) {
        this.ssHeaderId = ssHeaderId;
    }

    public String getSsVoyage() {
        return ssVoyage;
    }

    public void setSsVoyage(String ssVoyage) {
        this.ssVoyage = ssVoyage;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getTotaltransPod() {
        return totaltransPod;
    }

    public void setTotaltransPod(String totaltransPod) {
        this.totaltransPod = totaltransPod;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getVoyOwner() {
        return voyOwner;
    }

    public void setVoyOwner(String voyOwner) {
        this.voyOwner = voyOwner;
    }

    public String getAuditedOn() {
        return auditedOn;
    }

    public void setAuditedOn(String auditedOn) {
        this.auditedOn = auditedOn;
    }

    public String getStrippeddate() {
        return strippeddate;
    }

    public void setStrippeddate(String strippeddate) {
        this.strippeddate = strippeddate;
    }

    public String getHazmatPermitted() {
        return hazmatPermitted;
    }

    public void setHazmatPermitted(String hazmatPermitted) {
        this.hazmatPermitted = hazmatPermitted;
    }    
}
