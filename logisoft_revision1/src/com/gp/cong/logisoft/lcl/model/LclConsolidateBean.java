/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.model;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @author saravanan
 */
public class LclConsolidateBean {

    private String fileId;
    private String fileNo;
    private String originName;
    private String clientName;
    private String clientNameKey;
    private String shipperName;
    private String shipperNameKey;
    private String forwarderName;
    private String forwarderNameKey;
    private String consigneeName;
    private String consigneeNameKey;
    private String status;
    private Date etdSailingDate;
    private Integer totalPieceCount;
    private BigDecimal totalWeightImperial;
    private BigDecimal totalVolumeImperial;
    private Integer consolidateId;

    public LclConsolidateBean() {
    }

    public LclConsolidateBean(Object[] obj) throws Exception {
        int index = 0;
        fileId = null == obj[index] ? null : obj[index].toString();
        index++;
        fileNo = null == obj[index] ? null : obj[index].toString();
        index++;
        originName = null == obj[index] ? null : obj[index].toString().toUpperCase();
        index++;
        clientName = null == obj[index] ? null : obj[index].toString();
        clientNameKey = clientName;
        if (CommonUtils.isNotEmpty(clientName) && clientName.length()>10) {
            clientNameKey = clientNameKey.substring(0, 10);
        }
        index++;
        shipperName = null == obj[index] ? null : obj[index].toString();
        shipperNameKey = shipperName;
        if (CommonUtils.isNotEmpty(shipperName) && shipperName.length()>10) {
            shipperNameKey = shipperNameKey.substring(0, 10);
        }
        index++;
        forwarderName = null == obj[index] ? null : obj[index].toString();
        forwarderNameKey = forwarderName;
        if (CommonUtils.isNotEmpty(forwarderName) && forwarderName.length()>10) {
            forwarderNameKey = forwarderNameKey.substring(0, 10);
        }
        index++;
        consigneeName = null == obj[index] ? null : obj[index].toString();
        consigneeNameKey = consigneeName;
        if (CommonUtils.isNotEmpty(consigneeName) && consigneeName.length()>10) {
            consigneeNameKey = consigneeNameKey.substring(0, 10);
        }
        index++;
        status = null == obj[index] ? null : obj[index].toString();
        index++;
        String strDate = null == obj[index] ? null : obj[index].toString();
        if (null != strDate && !strDate.trim().equals("")) {
            etdSailingDate = DateUtils.parseStringToDateWithTime(strDate);
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
        if (null != obj[index] && !obj[index].toString().trim().equals("")) {
            consolidateId = Integer.parseInt(obj[index].toString());
        }

   }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public Date getEtdSailingDate() {
        return etdSailingDate;
    }

    public void setEtdSailingDate(Date etdSailingDate) {
        this.etdSailingDate = etdSailingDate;
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

    public Integer getConsolidateId() {
        return consolidateId;
    }

    public void setConsolidateId(Integer consolidateId) {
        this.consolidateId = consolidateId;
    }

    public String getClientNameKey() {
        return clientNameKey;
    }

    public void setClientNameKey(String clientNameKey) {
        this.clientNameKey = clientNameKey;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeNameKey() {
        return consigneeNameKey;
    }

    public void setConsigneeNameKey(String consigneeNameKey) {
        this.consigneeNameKey = consigneeNameKey;
    }

    public String getForwarderName() {
        return forwarderName;
    }

    public void setForwarderName(String forwarderName) {
        this.forwarderName = forwarderName;
    }

    public String getForwarderNameKey() {
        return forwarderNameKey;
    }

    public void setForwarderNameKey(String forwarderNameKey) {
        this.forwarderNameKey = forwarderNameKey;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperNameKey() {
        return shipperNameKey;
    }

    public void setShipperNameKey(String shipperNameKey) {
        this.shipperNameKey = shipperNameKey;
    }
}
