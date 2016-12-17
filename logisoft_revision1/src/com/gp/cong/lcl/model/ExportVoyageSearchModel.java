/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.lcl.model;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cvst.logisoft.struts.form.lcl.LclUnitsScheduleForm;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Mei
 */
public class ExportVoyageSearchModel {

    private String ssHeaderId;
    private String unitSsId;
    private String unitId;
    private String scheduleNo;
    private String serviceType;
    private String createdBy;
    private String voyOwner;
    private String unitcount;
    private String unitNo;
    private String departPierUnloc;
    private String departPier;
    private String arrivalPierUnloc;
    private String arrivalPier;
    private String carrierName;
    private String carrierAcctNo;
    private String vesselName;
    private String ssVoyage;
    private String lrdOverride;
    private String lrdOverrideDays;
    private String polLrdDate;//loading date
    private String etaSailDate;//sailing date
    private String etaPodDate;//pod date
    private String etaFd;//pod date
    private String totaltransPod;//poltopod TT
    private String totaltransFd;//pootoFD
    private String dataSource;
    private String pooName;
    private String pooUnLocCode;
    private String fdUnLocCode;
    private String fdName;
    private String dispoDesc;
    private String dispoCode;
    private String isInbond;
    private String isHazmat;
    private String ssDetailId;
    private String transMode;
    private String pooId;
    private String fdId;
    private String unitTrackingNotes;
    private String unitSize;
    private String closedStatus;
    private String auditedStatus;
    private String closedBy;
    private String auditedBy;
    private String closedOn;
    private String auditedOn;
    private String closedRemarks;
    private String auditedRemarks;
    private Date originLrd;//origin
    private Date polLrd;//pol
    private Date etaPod;//POD
    private Date etafdDate;//fd date
    private String warehouseName;
    private String warehouseNo;
    private BigDecimal tareWeightImperial;
    private BigDecimal tareWeightMetric;
    private BigDecimal totalVolumeImperial;
    private BigDecimal totalWeightImperial;
    private String createdDate;
    private String etaSailDates;
    private String sealNo;
    private String loadedBy;
    private String numberDrs;
    private String loadingDeadLineDate;
    private String doorLocation;
    private String voyageStatus;
    private BigDecimal totalVolumeMetric;
    private BigDecimal totalWeightMetric;
    private String blBody;
    private Integer totalPiece;
    private String comments;
    private String bookingNo;
    private String manifestUnitCount;
    private String cobUnitCount;
    private String unitSizeShortDesc;
    private Integer verifiedEta;

    public ExportVoyageSearchModel() {
    }

    public ExportVoyageSearchModel(Object[] obj, LclUnitsScheduleForm lclUnitsScheduleForm) throws Exception {
        int index = 0;
        int objLength = obj.length;
        ssHeaderId = null == obj[index] ? null : obj[index].toString();
        index++;
        serviceType = null == obj[index] ? null : obj[index].toString();
        index++;
        ssDetailId = null == obj[index] ? null : obj[index].toString();
        index++;
        scheduleNo = null == obj[index] ? null : obj[index].toString();
        index++;
        carrierAcctNo = null == obj[index] ? null : obj[index].toString();
        index++;
        carrierName = null == obj[index] ? null : obj[index].toString();
        index++;
        unitcount = null == obj[index] ? null : obj[index].toString();
        index++;
        manifestUnitCount = null == obj[index] ? null : obj[index].toString();
        index++;
        cobUnitCount = null == obj[index] ? null : obj[index].toString();
        index++;
        unitNo = null == obj[index] ? null : obj[index].toString();
        index++;
        vesselName = null == obj[index] ? null : obj[index].toString();
        index++;
        ssVoyage = null == obj[index] ? null : obj[index].toString();
        index++;
        departPierUnloc = null == obj[index] ? null : obj[index].toString();
        index++;
        departPier = null == obj[index] ? null : obj[index].toString();
        index++;
        arrivalPierUnloc = null == obj[index] ? null : obj[index].toString();
        index++;
        arrivalPier = null == obj[index] ? null : obj[index].toString();
        index++;
        lrdOverride = null == obj[index] ? null : obj[index].toString();
        index++;
        lrdOverrideDays = null == obj[index] ? null : obj[index].toString();
        index++;
        polLrdDate = null == obj[index] ? null : obj[index].toString();
        index++;
        String strDate = null == obj[index] ? null : obj[index].toString();
        if (null != strDate && !strDate.equals("0")) {
            polLrd = DateUtils.parseStringToDateWithTime(strDate);
        }
        index++;
        etaSailDate = null == obj[index] ? null : obj[index].toString();
        index++;
        etaPodDate = null == obj[index] ? null : obj[index].toString();
        index++;
        strDate = null == obj[index] ? null : obj[index].toString();
        if (null != strDate && !strDate.equals("0")) {
            etaPod = DateUtils.parseStringToDateWithTime(strDate);
        }
        index++;
        totaltransPod = null == obj[index] ? null : obj[index].toString();
        index++;
        dataSource = null == obj[index] ? null : obj[index].toString();
        index++;
        createdBy = null == obj[index] ? null : obj[index].toString();
        index++;
        voyOwner = null == obj[index] ? null : obj[index].toString();
        index++;
        closedStatus = null == obj[index] ? null : obj[index].toString();
        index++;
        auditedStatus = null == obj[index] ? null : obj[index].toString();
        index++;
        closedBy = null == obj[index] ? null : obj[index].toString();
        index++;
        auditedBy = null == obj[index] ? null : obj[index].toString();
        index++;
        closedOn = null == obj[index] ? null : obj[index].toString();
        index++;
        auditedOn = null == obj[index] ? null : obj[index].toString();
        index++;
        closedRemarks = null == obj[index] ? null : obj[index].toString();
        index++;
        auditedRemarks = null == obj[index] ? null : obj[index].toString();
        index++;
        etaSailDates = null == obj[index] ? null : obj[index].toString();
        index++;
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPortOfOriginId()) && CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPolId())
                && polLrd != null && lclUnitsScheduleForm.getPortOfOriginId().intValue() != lclUnitsScheduleForm.getPolId().intValue()) {
            if (obj[index] != null && !obj[index].toString().trim().equals("") && !obj[index].toString().trim().equals("0")) {
                int transittime = Integer.parseInt(obj[index].toString());
                index++;
                Calendar cal = Calendar.getInstance();
                cal.setTime(polLrd);
                cal.add(Calendar.DATE, -transittime); // Substract days in Dates in Calendar
                originLrd = cal.getTime();
                if (originLrd.getDay() == 0) {
                    cal.add(Calendar.DATE, -2); // Substract 2 days in Dates in Calendar if it falls on sunday
                } else if (originLrd.getDay() == 6) {
                    cal.add(Calendar.DATE, -1); // Substract 1 day in Dates in Calendar if it falls on saturday
                }
                if (obj[index] != null && !obj[index].toString().trim().equals("") && !obj[index].toString().trim().equals("0")) {
                    int co_dow = Integer.parseInt(obj[index].toString());
                    if (originLrd.getDay() != co_dow) {
                        boolean flag = true;
                        while (flag) {
                            cal.add(Calendar.DATE, -1);
                            originLrd = cal.getTime();
                            if (originLrd.getDay() == co_dow) {
                                flag = false;
                            }
                        }
                        originLrd = cal.getTime();
                    }
                }
            } else {
                originLrd = polLrd;
            }
        } else {
            originLrd = polLrd;
        }
        if (CommonUtils.isNotEmpty(lclUnitsScheduleForm.getFinalDestinationId()) && CommonUtils.isNotEmpty(lclUnitsScheduleForm.getPodId())
                && etaPod != null && lclUnitsScheduleForm.getFinalDestinationId().intValue() != lclUnitsScheduleForm.getPodId().intValue()
                && obj[objLength - 1] != null && !obj[objLength - 1].toString().trim().equals("") && !obj[objLength - 1].toString().trim().equals("0")) {
            int transittime = Integer.parseInt(obj[objLength - 1].toString());
            Calendar cal = Calendar.getInstance();
            cal.set(etaPod.getYear() + 1900, etaPod.getMonth(), etaPod.getDate(),
                    etaPod.getHours(), etaPod.getMinutes(), etaPod.getSeconds());
            // Addition of date
            cal.add(Calendar.DATE, transittime);
            etafdDate = cal.getTime();
        } else {
            etafdDate = etaPod;
        }
        if (etafdDate != null && originLrd != null) {
            long daysBetween = DateUtils.daysBetween(originLrd, etafdDate);
            totaltransFd = String.valueOf(daysBetween);
        }
        verifiedEta = null == obj[39] ? null : (Integer) obj[39];
        index++;

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getLrdOverride() {
        return lrdOverride;
    }

    public void setLrdOverride(String lrdOverride) {
        this.lrdOverride = lrdOverride;
    }

    public String getLrdOverrideDays() {
        return lrdOverrideDays;
    }

    public void setLrdOverrideDays(String lrdOverrideDays) {
        this.lrdOverrideDays = lrdOverrideDays;
    }

    public String getPolLrdDate() {
        return polLrdDate;
    }

    public void setPolLrdDate(String polLrdDate) {
        this.polLrdDate = polLrdDate;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public String getUnitcount() {
        return unitcount;
    }

    public void setUnitcount(String unitcount) {
        this.unitcount = unitcount;
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

    public String getTotaltransFd() {
        return totaltransFd;
    }

    public void setTotaltransFd(String totaltransFd) {
        this.totaltransFd = totaltransFd;
    }

    public String getTotaltransPod() {
        return totaltransPod;
    }

    public void setTotaltransPod(String totaltransPod) {
        this.totaltransPod = totaltransPod;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public String getIsHazmat() {
        return isHazmat;
    }

    public void setIsHazmat(String isHazmat) {
        this.isHazmat = isHazmat;
    }

    public BigDecimal getTotalVolumeImperial() {
        return totalVolumeImperial;
    }

    public void setTotalVolumeImperial(BigDecimal totalVolumeImperial) {
        this.totalVolumeImperial = totalVolumeImperial;
    }

    public BigDecimal getTotalWeightImperial() {
        return totalWeightImperial;
    }

    public void setTotalWeightImperial(BigDecimal totalWeightImperial) {
        this.totalWeightImperial = totalWeightImperial;
    }

    public String getIsInbond() {
        return isInbond;
    }

    public void setIsInbond(String isInbond) {
        this.isInbond = isInbond;
    }

    public String getPooName() {
        return pooName;
    }

    public void setPooName(String pooName) {
        this.pooName = pooName;
    }

    public String getSsDetailId() {
        return ssDetailId;
    }

    public void setSsDetailId(String ssDetailId) {
        this.ssDetailId = ssDetailId;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getFdId() {
        return fdId;
    }

    public void setFdId(String fdId) {
        this.fdId = fdId;
    }

    public String getPooId() {
        return pooId;
    }

    public void setPooId(String pooId) {
        this.pooId = pooId;
    }

    public String getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(String unitSize) {
        this.unitSize = unitSize;
    }

    public String getUnitSizeShortDesc() {
        return unitSizeShortDesc;
    }

    public void setUnitSizeShortDesc(String unitSizeShortDesc) {
        this.unitSizeShortDesc = unitSizeShortDesc;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getUnitTrackingNotes() {
        return unitTrackingNotes;
    }

    public void setUnitTrackingNotes(String unitTrackingNotes) {
        this.unitTrackingNotes = unitTrackingNotes;
    }

    public String getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(String auditedBy) {
        this.auditedBy = auditedBy;
    }

    public String getAuditedOn() {
        return auditedOn;
    }

    public void setAuditedOn(String auditedOn) {
        this.auditedOn = auditedOn;
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

    public String getEtaFd() {
        return etaFd;
    }

    public void setEtaFd(String etaFd) {
        this.etaFd = etaFd;
    }

    public Date getEtaPod() {
        return etaPod;
    }

    public void setEtaPod(Date etaPod) {
        this.etaPod = etaPod;
    }

    public Date getEtafdDate() {
        return etafdDate;
    }

    public void setEtafdDate(Date etafdDate) {
        this.etafdDate = etafdDate;
    }

    public Date getOriginLrd() {
        return originLrd;
    }

    public void setOriginLrd(Date originLrd) {
        this.originLrd = originLrd;
    }

    public Date getPolLrd() {
        return polLrd;
    }

    public void setPolLrd(Date polLrd) {
        this.polLrd = polLrd;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getTareWeightImperial() {
        return tareWeightImperial;
    }

    public void setTareWeightImperial(BigDecimal tareWeightImperial) {
        this.tareWeightImperial = tareWeightImperial;
    }

    public BigDecimal getTareWeightMetric() {
        return tareWeightMetric;
    }

    public void setTareWeightMetric(BigDecimal tareWeightMetric) {
        this.tareWeightMetric = tareWeightMetric;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getEtaSailDates() {
        return etaSailDates;
    }

    public void setEtaSailDates(String etaSailDates) {
        this.etaSailDates = etaSailDates;
    }

    public String getDoorLocation() {
        return doorLocation;
    }

    public void setDoorLocation(String doorLocation) {
        this.doorLocation = doorLocation;
    }

    public String getFdUnLocCode() {
        return fdUnLocCode;
    }

    public void setFdUnLocCode(String fdUnLocCode) {
        this.fdUnLocCode = fdUnLocCode;
    }

    public String getLoadedBy() {
        return loadedBy;
    }

    public void setLoadedBy(String loadedBy) {
        this.loadedBy = loadedBy;
    }

    public String getLoadingDeadLineDate() {
        return loadingDeadLineDate;
    }

    public void setLoadingDeadLineDate(String loadingDeadLineDate) {
        this.loadingDeadLineDate = loadingDeadLineDate;
    }

    public String getNumberDrs() {
        return numberDrs;
    }

    public void setNumberDrs(String numberDrs) {
        this.numberDrs = numberDrs;
    }

    public String getPooUnLocCode() {
        return pooUnLocCode;
    }

    public void setPooUnLocCode(String pooUnLocCode) {
        this.pooUnLocCode = pooUnLocCode;
    }

    public String getSealNo() {
        return sealNo;
    }

    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    public String getVoyageStatus() {
        return voyageStatus;
    }

    public void setVoyageStatus(String voyageStatus) {
        this.voyageStatus = voyageStatus;
    }

    public BigDecimal getTotalVolumeMetric() {
        return totalVolumeMetric;
    }

    public void setTotalVolumeMetric(BigDecimal totalVolumeMetric) {
        this.totalVolumeMetric = totalVolumeMetric;
    }

    public BigDecimal getTotalWeightMetric() {
        return totalWeightMetric;
    }

    public void setTotalWeightMetric(BigDecimal totalWeightMetric) {
        this.totalWeightMetric = totalWeightMetric;
    }

    public String getBlBody() {
        return blBody;
    }

    public void setBlBody(String blBody) {
        this.blBody = blBody;
    }

    public Integer getTotalPiece() {
        return totalPiece;
    }

    public void setTotalPiece(Integer totalPiece) {
        this.totalPiece = totalPiece;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getManifestUnitCount() {
        return manifestUnitCount;
    }

    public void setManifestUnitCount(String manifestUnitCount) {
        this.manifestUnitCount = manifestUnitCount;
    }

    public String getCobUnitCount() {
        return cobUnitCount;
    }

    public void setCobUnitCount(String cobUnitCount) {
        this.cobUnitCount = cobUnitCount;
    }

    public Integer getVerifiedEta() {
        return verifiedEta;
    }

    public void setVerifiedEta(Integer verifiedEta) {
        this.verifiedEta = verifiedEta;
    }

}
