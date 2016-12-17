package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Rajesh
 */
public class EculineEdiBlBean implements Serializable {

    private BigInteger id;
    private String blNo;
    private String invoiceNo;
    private String amsScac;
    private String arGlMapping;
    private String apGlMapping;
    private String chargesPostedStatus;
    private BigInteger invoiceStatus;
    private String shipper;
    private String shipperNo;
    private String consignee;
    private String consigneeNo;
    private String notify;
    private String notifyNo;
    private BigDecimal pieces;
    private BigDecimal weight;
    private String weightUnit;
    private BigDecimal volume;
    private String volumeUnit;
    private String pol;
    private String pod;
    private String por;
    private String delivery;
    private String destination;
    private BigInteger fileId;
    private String fileNo;
    private String shipTerms;
    private String expressRelease;
    private String packType;
    private String packageId;
    private String origin;
    private String destn;
    private boolean adjudicated;
    private String preCarriageBy;
    private boolean approved;
    private static List<BigInteger> blIds;
    private boolean error;
    private String unPolCount;
    private String unPodCount;
    private String unDelCount;
    private String cntrId;

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigDecimal getPieces() {
        return pieces;
    }

    public void setPieces(BigDecimal pieces) {
        this.pieces = pieces;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getVolumeUnit() {
        return volumeUnit;
    }

    public void setVolumeUnit(String volumeUnit) {
        this.volumeUnit = volumeUnit;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
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

    public String getPor() {
        return por;
    }

    public void setPor(String por) {
        this.por = por;
    }

    public String getConsigneeNo() {
        return consigneeNo;
    }

    public void setConsigneeNo(String consigneeNo) {
        this.consigneeNo = consigneeNo;
    }

    public String getShipperNo() {
        return shipperNo;
    }

    public void setShipperNo(String shipperNo) {
        this.shipperNo = shipperNo;
    }

    public BigInteger getFileId() {
        return fileId;
    }

    public void setFileId(BigInteger fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getExpressRelease() {
        return expressRelease;
    }

    public void setExpressRelease(String expressRelease) {
        this.expressRelease = expressRelease;
    }

    public String getShipTerms() {
        return shipTerms;
    }

    public void setShipTerms(String shipTerms) {
        this.shipTerms = shipTerms;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public static List<BigInteger> getBlIds() {
        return blIds;
    }

    public static void setBlIds(List<BigInteger> blIds) {
        EculineEdiBlBean.blIds = blIds;
    }

    public boolean isError() {
        return blIds.contains(id);
    }

    public BigInteger getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(BigInteger invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getDestn() {
        return destn;
    }

    public void setDestn(String destn) {
        this.destn = destn;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public boolean isAdjudicated() {
        return adjudicated;
    }

    public void setAdjudicated(boolean adjudicated) {
        this.adjudicated = adjudicated;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getNotifyNo() {
        return notifyNo;
    }

    public void setNotifyNo(String notifyNo) {
        this.notifyNo = notifyNo;
    }

    public String getPreCarriageBy() {
        return preCarriageBy;
    }

    public void setPreCarriageBy(String preCarriageBy) {
        this.preCarriageBy = preCarriageBy;
    }

    public String getApGlMapping() {
        return apGlMapping;
    }

    public void setApGlMapping(String apGlMapping) {
        this.apGlMapping = apGlMapping;
    }

    public String getArGlMapping() {
        return arGlMapping;
    }

    public void setArGlMapping(String arGlMapping) {
        this.arGlMapping = arGlMapping;
    }

    public String getChargesPostedStatus() {
        return chargesPostedStatus;
    }

    public void setChargesPostedStatus(String chargesPostedStatus) {
        this.chargesPostedStatus = chargesPostedStatus;
    }

    public String getAmsScac() {
        return amsScac;
    }

    public void setAmsScac(String amsScac) {
        this.amsScac = amsScac;
    }

    public String getUnPolCount() {
        return unPolCount;
    }

    public void setUnPolCount(String unPolCount) {
        this.unPolCount = unPolCount;
    }

    public String getUnPodCount() {
        return unPodCount;
    }

    public void setUnPodCount(String unPodCount) {
        this.unPodCount = unPodCount;
    }

    public String getUnDelCount() {
        return unDelCount;
    }

    public void setUnDelCount(String unDelCount) {
        this.unDelCount = unDelCount;
    }

    public String getCntrId() {
        return cntrId;
    }

    public void setCntrId(String cntrId) {
        this.cntrId = cntrId;
    }
}
