/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.beans;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author saravanan
 */
public class BookingUnitsBean implements Serializable {

    private String fileNo;
    private Long fileId;
    private Long unitId;
    private Long unitssId;
    private String status = "";
    private Integer totalPieceCount;
    private Integer count;
    private BigDecimal totalWeightImperial;
    private BigDecimal totalVolumeImperial;
    private String remarks;
    private String hotCodes;
    private String whsloc;
    private String label1;
    private String hotCodeKey = "";
    private String pickVoyageNo;
    private String unitno;
    private String curLoc;
    private String curLocName;
    private String dispo;
    private String poo;
    private String pol;
    private String pod;
    private String fd;
    private String whseLoc;
    private String bookVoyageNo;
    private String transMode;
    private String unitStatus;
    private String bookingType;
    private String impPol;
    private String impPod;
    private String polUnCode;
    private String unitType;
    private BigDecimal unitCapacity;
    private String loadBy;
    private Long polId;
    private BigDecimal tareWeightImperial;
    private BigDecimal tareWeightMetric;
    private String enteredTime;
    private boolean haz;
    private String impol;
    private String impod;
    private String voyageNo;
    private String pickedunitNo;
    private String unitDesc;
    // verify weight lenth for container 
    private Integer dimsLength;
    private Integer dimsWidth;
    private Integer dimsHeight;
    private Integer dimsWeight;
    private String dimensionData;
    // declare for to pick date related issue
    private String polLrd;
    private String std;
    private String inBondNo;
    private String dimensionsToolTip;
    private String warehouseLine;
    private String detailLine;
    private String warehouseLineToolTip;
    private String consolidatesFiles;
    private String consolidatesFileId;
    private String bookingHazmatDetails;
    private String shortShipSequence;

    public BookingUnitsBean() {
    }

    public BookingUnitsBean(Object[] obj) throws Exception {
        LclUtils lclUtils = new LclUtils();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        int index = 0;
        fileNo = null == obj[index] ? null : obj[index].toString();
        index++;
        if (obj[index] != null && obj[index].toString().contains("1")) {
            status = "HAZ,";
        }
        index++;
        bookingType = null == obj[index] ? null : obj[index].toString();
        index++;
        impPol = null == obj[index] ? null : obj[index].toString();
        index++;
        impPod = null == obj[index] ? null : obj[index].toString();
        index++;
        String podTrans = "";
        if (null != obj[index] && !obj[index].toString().trim().equals("") && LclCommonConstant.LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(bookingType)) {
            podTrans = (obj[index].toString());
            pod = impPod;
        } else {
            pod = (obj[index].toString());
        }
        index++;
        if (obj[index] != null && obj[index].toString().equalsIgnoreCase("PICKED")) {
            status += obj[index].toString();
        } else if (obj[index] != null && obj[index].toString().equalsIgnoreCase("PRE")) {
            status += obj[index].toString();
        } else if (status.contains("HAZ,")) {
            status = status.substring(0, status.length() - 1);
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            totalPieceCount = Integer.parseInt(obj[index].toString());
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            totalWeightImperial = new BigDecimal(obj[index].toString());
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            totalVolumeImperial = new BigDecimal(obj[index].toString());
        }
        index++;
        curLoc = null == obj[index] ? null : obj[index].toString();
        index++;
        curLocName = null == obj[index] ? null : obj[index].toString();
        index++;
        dispo = null == obj[index] ? null : obj[index].toString();
        index++;
        remarks = null == obj[index] ? null : obj[index].toString();
        index++;
        whseLoc = null == obj[index] ? null : obj[index].toString();
        index++;
        hotCodes = null == obj[index] ? "" : obj[index].toString();
        if (CommonUtils.isNotEmpty(hotCodes)) {
            String[] hotCodeArr = hotCodes.split("<br>");
            for (int i = 0; i < hotCodeArr.length; i++) {
                if (CommonUtils.isNotEmpty(hotCodeArr[i]) && hotCodeArr[i].contains("/")) {
                    hotCodeKey += hotCodeArr[i].substring(0, 3) + ",";
                }
            }
            int occurrence = lclUtils.getCharOccurrence(hotCodeKey);
            if (CommonUtils.isNotEmpty(hotCodeKey)) {
                if (occurrence > 3 && hotCodeKey.length() > 12) {
                    hotCodeKey = hotCodeKey.substring(0, 11);
                }

                hotCodeKey = hotCodeKey.substring(0, hotCodeKey.length() - 1);
            } else {
                hotCodeKey = hotCodes;
            }
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            pickVoyageNo = (obj[index].toString());
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            unitno = (obj[index].toString());
        }
        index++;
        if (LclCommonConstant.LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(bookingType)) {
            poo = podTrans;
        } else {
            poo = (obj[index].toString());
        }
        index++;
        if (LclCommonConstant.LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(bookingType)) {
            pol = impPol;
        } else {
            pol = (obj[index].toString());
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            fd = (obj[index].toString());
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            bookVoyageNo = (obj[index].toString());
        }
        index++;
        transMode = null == obj[index] ? null : obj[index].toString();
        index++;
        unitStatus = null == obj[index] ? null : obj[index].toString();
        index++;
        polUnCode = null == obj[index] ? null : obj[index].toString();
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            count = Integer.parseInt(obj[index].toString());
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            fileId = Long.parseLong((obj[index].toString()));
            whsloc = lclBookingPieceDAO.getWarehouseLocationByFileId(fileId);
            if (CommonUtils.isNotEmpty(whsloc)) {
                whsloc = whsloc.toUpperCase();
            }
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            polId = Long.parseLong((obj[index].toString()));
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            tareWeightImperial = new BigDecimal((obj[index].toString()));
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            tareWeightMetric = new BigDecimal((obj[index].toString()));
        }
        index++;
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            enteredTime = (obj[index].toString());
        }

    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalPieceCount() {
        return totalPieceCount;
    }

    public void setTotalPieceCount(Integer totalPieceCount) {
        this.totalPieceCount = totalPieceCount;
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

    public String getHotCodeKey() {
        return hotCodeKey;
    }

    public void setHotCodeKey(String hotCodeKey) {
        if (!"".equalsIgnoreCase(hotCodeKey)) {
            String[] value = hotCodeKey.split(",");
            if (value.length >= 3) {
                this.hotCodeKey = value[0] + "/" + value[1] + "/" + value[2];
            } else {
                this.hotCodeKey = hotCodeKey.replace(",", "/");
            }
        } else {
            this.hotCodeKey = hotCodeKey;
        }
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDispo() {
        return dispo;
    }

    public void setDispo(String dispo) {
        this.dispo = dispo;
    }

    public String getWhsloc() {
        return whsloc;
    }

    public void setWhsloc(String whsloc) {
        this.whsloc = whsloc;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(String hotCodes) {
        this.hotCodes = hotCodes;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public Long getUnitssId() {
        return unitssId;
    }

    public void setUnitssId(Long unitssId) {
        this.unitssId = unitssId;
    }

    public String getBookVoyageNo() {
        return bookVoyageNo;
    }

    public void setBookVoyageNo(String bookVoyageNo) {
        this.bookVoyageNo = bookVoyageNo;
    }

    public String getFd() {
        return fd;
    }

    public void setFd(String fd) {
        this.fd = fd;
    }

    public String getPickVoyageNo() {
        return pickVoyageNo;
    }

    public void setPickVoyageNo(String pickVoyageNo) {
        this.pickVoyageNo = pickVoyageNo;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPoo() {
        return poo;
    }

    public void setPoo(String poo) {
        this.poo = poo;
    }

    public String getCurLoc() {
        return curLoc;
    }

    public void setCurLoc(String curLoc) {
        this.curLoc = curLoc;
    }

    public String getCurLocName() {
        return curLocName;
    }

    public void setCurLocName(String curLocName) {
        this.curLocName = curLocName;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getUnitStatus() {
        return unitStatus;
    }

    public void setUnitStatus(String unitStatus) {
        this.unitStatus = unitStatus;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getImpPod() {
        return impPod;
    }

    public void setImpPod(String impPod) {
        this.impPod = impPod;
    }

    public String getImpPol() {
        return impPol;
    }

    public void setImpPol(String impPol) {
        this.impPol = impPol;
    }

    public String getPolUnCode() {
        return polUnCode;
    }

    public void setPolUnCode(String polUnCode) {
        this.polUnCode = polUnCode;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getLoadBy() {
        return loadBy;
    }

    public void setLoadBy(String loadBy) {
        this.loadBy = loadBy;
    }

    public BigDecimal getUnitCapacity() {
        return unitCapacity;
    }

    public void setUnitCapacity(BigDecimal unitCapacity) {
        this.unitCapacity = unitCapacity;
    }

    public Long getPolId() {
        return polId;
    }

    public void setPolId(Long polId) {
        this.polId = polId;
    }

    public String getEnteredTime() {
        return enteredTime;
    }

    public void setEnteredTime(String enteredTime) {
        this.enteredTime = enteredTime;
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

    public boolean isHaz() {
        return haz;
    }

    public void setHaz(boolean haz) {
        this.haz = haz;
    }

    public String getImpol() {
        return impol;
    }

    public void setImpol(String impol) {
        this.impol = impol;
    }

    public String getImpod() {
        return impod;
    }

    public void setImpod(String impod) {
        this.impod = impod;
    }

    public String getVoyageNo() {
        return voyageNo;
    }

    public void setVoyageNo(String voyageNo) {
        this.voyageNo = voyageNo;
    }

    public String getPickedunitNo() {
        return pickedunitNo;
    }

    public void setPickedunitNo(String pickedunitNo) {
        this.pickedunitNo = pickedunitNo;
    }

    public Integer getDimsLength() {
        return dimsLength;
    }

    public void setDimsLength(Integer dimsLength) {
        this.dimsLength = dimsLength;
    }

    public Integer getDimsWidth() {
        return dimsWidth;
    }

    public void setDimsWidth(Integer dimsWidth) {
        this.dimsWidth = dimsWidth;
    }

    public Integer getDimsHeight() {
        return dimsHeight;
    }

    public void setDimsHeight(Integer dimsHeight) {
        this.dimsHeight = dimsHeight;
    }

    public Integer getDimsWeight() {
        return dimsWeight;
    }

    public void setDimsWeight(Integer dimsWeight) {
        this.dimsWeight = dimsWeight;
    }

    public String getDimensionData() {
        return dimensionData;
    }

    public void setDimensionData(String dimensionData) {
        this.dimensionData = dimensionData;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public String getPolLrd() {
        return polLrd;
    }

    public void setPolLrd(String polLrd) {
        this.polLrd = polLrd;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getInBondNo() {
        return inBondNo;
    }

    public void setInBondNo(String inBondNo) {
        this.inBondNo = inBondNo;
    }

    public String getWhseLoc() {
        return whseLoc;
    }

    public void setWhseLoc(String whseLoc) {
        this.whseLoc = whseLoc;
    }

    public String getDetailLine() {
        return detailLine;
    }

    public void setDetailLine(String detailLine) {
        this.detailLine = detailLine;
    }

    public String getWarehouseLine() {
        return warehouseLine;
    }

    public void setWarehouseLine(String warehouseLine) {
        warehouseLine = null != warehouseLine ? warehouseLine + "," : "";
        if (CommonUtils.isNotEmpty(warehouseLine)) {
            String[] value = warehouseLine.split(",");
            this.warehouseLine = value.length > 0 ? value[0] : "";
            warehouseLine = warehouseLine.substring(0, warehouseLine.length() - 1);
            this.setWarehouseLineToolTip(warehouseLine);
        } else {
            this.warehouseLine = warehouseLine;
        }
    }

    public String getWarehouseLineToolTip() {
        return warehouseLineToolTip;
    }

    public void setWarehouseLineToolTip(String warehouseLineToolTip) {
        this.warehouseLineToolTip = warehouseLineToolTip;
    }

    public String getDimensionsToolTip() {
        StringBuilder tootTip = new StringBuilder();
        tootTip.append("<table><tr><td colspan='2'><font size='2' color=#008000><b>Released Booking Details</b></font></td>");
        tootTip.append("<td><font size='2' color=#008000><b>Dimensions Details</b></font></td>");
        tootTip.append("</tr><tr><td align=right><font color=blue><b>File No #:<b></font></td>");
        tootTip.append("<td>").append(this.fileNo).append("</td>");
        tootTip.append("<td rowspan='8' valign='top'><table>").append(this.dimensionData).append("</table></td></tr>");
        tootTip.append("<tr><td align=right><font color=red>Port Of Origin :</font></td><td>");
        tootTip.append(null != this.poo ? this.poo : "").append("</td></tr>");
        tootTip.append("<tr><td align=right><font color=red>Port Of Loading :</font></td><td>");
        tootTip.append(null != this.pol ? this.pol : "").append("</td></tr>");
        tootTip.append("<tr><td align=right><font color=red>Port Of Discharge :</font></td><td>");
        tootTip.append(null != this.pod ? this.pod : "").append("</td></tr>");
        tootTip.append("<tr><td align=right><font color=red>Final Destination :</font></td><td>");
        tootTip.append(null != this.fd ? this.fd : "").append("</td></tr>");
        tootTip.append("<tr><td align=right><font color=red>Booked For Voyage :</font></td><td>");
        tootTip.append(null != this.bookVoyageNo ? this.bookVoyageNo : "").append("</td></tr>");
        tootTip.append(" <tr><td align=right><font color=red>Picked on voyage# :</font></td><td>");
        tootTip.append(null != this.voyageNo ? this.voyageNo : "").append("</td></tr>");
        tootTip.append("<tr><td align=right><font color=red>Picked on Unit# :</font></td><td>");
        tootTip.append(null != this.pickedunitNo ? this.pickedunitNo : "").append("</td></tr>");
        tootTip.append("</table>");
        return tootTip.toString();
    }

    public void setDimensionsToolTip(String dimensionsToolTip) {
        this.dimensionsToolTip = dimensionsToolTip;
    }

    public String getConsolidatesFiles() {
        return consolidatesFiles;
    }

    public void setConsolidatesFiles(String consolidatesFiles) {
        this.consolidatesFiles = consolidatesFiles;
    }

    public String getConsolidatesFileId() {
        return consolidatesFileId;
    }

    public void setConsolidatesFileId(String consolidatesFileId) {
        this.consolidatesFileId = consolidatesFileId;
    }

    public String getBookingHazmatDetails() {
        return bookingHazmatDetails;
    }

    public void setBookingHazmatDetails(String bookingHazmatDetails) {
        this.bookingHazmatDetails = bookingHazmatDetails;
    }

    public String getShortShipSequence() {
        return shortShipSequence;
    }

    public void setShortShipSequence(String shortShipSequence) {
        this.shortShipSequence = shortShipSequence;
    }
}
