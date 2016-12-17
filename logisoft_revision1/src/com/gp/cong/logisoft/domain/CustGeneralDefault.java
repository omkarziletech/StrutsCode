/**
 * 
 */
package com.gp.cong.logisoft.domain;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.struts.form.TradingPartnerForm;

/**
 * @author Shanmugam
 *
 */
public class CustGeneralDefault implements java.io.Serializable{
	private Integer id;
	private String destination;
	private String acctNo;
        private String origin;
        private String commodityCode;
        private String issuingTerminal;
        private String pol;
        private String pod;
        private String nvoMove;
        private String originZip;
        private String doorOrigin;
        private String ert;
        private String goodsDesc;
        private String importantNotes;
        private String ffComm;
        private String documentCharge;
        private String shipperCheck;
        private String consigneeCheck;
        private Double documentAmount;

        private String lineMove;
        private String prepaidOrCollect;
        private String billTo;
        private String shipper;
        private String shipperNo;
        private String forwarder;
        private String forwarderNo;
        private String consignee;
        private String consigneeNo;
        private String applyDefaultValues;

    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeNo() {
        return consigneeNo;
    }

    public void setConsigneeNo(String consigneeNo) {
        this.consigneeNo = consigneeNo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDocumentCharge() {
        return documentCharge;
    }

    public void setDocumentCharge(String documentCharge) {
        this.documentCharge = documentCharge;
    }

    public String getDoorOrigin() {
        return doorOrigin;
    }

    public void setDoorOrigin(String doorOrigin) {
        this.doorOrigin = doorOrigin;
    }

    public String getErt() {
        return ert;
    }

    public void setErt(String ert) {
        this.ert = ert;
    }

    public String getFfComm() {
        return ffComm;
    }

    public void setFfComm(String ffComm) {
        this.ffComm = ffComm;
    }

    public String getForwarder() {
        return forwarder;
    }

    public void setForwarder(String forwarder) {
        this.forwarder = forwarder;
    }

    public String getForwarderNo() {
        return forwarderNo;
    }

    public void setForwarderNo(String forwarderNo) {
        this.forwarderNo = forwarderNo;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getImportantNotes() {
        return importantNotes;
    }

    public void setImportantNotes(String importantNotes) {
        this.importantNotes = importantNotes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getLineMove() {
        return lineMove;
    }

    public void setLineMove(String lineMove) {
        this.lineMove = lineMove;
    }

    public String getNvoMove() {
        return nvoMove;
    }

    public void setNvoMove(String nvoMove) {
        this.nvoMove = nvoMove;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginZip() {
        return originZip;
    }

    public void setOriginZip(String originZip) {
        this.originZip = originZip;
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

    public String getPrepaidOrCollect() {
        return prepaidOrCollect;
    }

    public void setPrepaidOrCollect(String prepaidOrCollect) {
        this.prepaidOrCollect = prepaidOrCollect;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getShipperNo() {
        return shipperNo;
    }

    public void setShipperNo(String shipperNo) {
        this.shipperNo = shipperNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public Double getDocumentAmount() {
        return documentAmount;
    }

    public void setDocumentAmount(Double documentAmount) {
        this.documentAmount = documentAmount;
    }
    public String getShipperCheck() {
        return shipperCheck;
    }

    public void setShipperCheck(String shipperCheck) {
        this.shipperCheck = shipperCheck;
    }

    public String getConsigneeCheck() {
        return consigneeCheck;
    }

    public void setConsigneeCheck(String consigneeCheck) {
        this.consigneeCheck = consigneeCheck;
    }

    public String getApplyDefaultValues() {
        return applyDefaultValues;
    }

    public void setApplyDefaultValues(String applyDefaultValues) {
        this.applyDefaultValues = applyDefaultValues;
    }
    
     public CustGeneralDefault SetGeneralDefaultInformation(CustGeneralDefault generalInfo, TradingPartnerForm tradingPartnerForm) {
        if(null == generalInfo) {
            generalInfo = new CustGeneralDefault();
            generalInfo.acctNo = tradingPartnerForm.getTradingPartnerId();
        }
        generalInfo.applyDefaultValues =tradingPartnerForm.getApplyDefaultValues();
        generalInfo.destination =tradingPartnerForm.getDestination();
        generalInfo.origin = tradingPartnerForm.getOrigin();
        generalInfo.pol = tradingPartnerForm.getPol();
        generalInfo.pod = tradingPartnerForm.getPod();
        generalInfo.commodityCode = tradingPartnerForm.getCommodityCode();
        generalInfo.issuingTerminal = tradingPartnerForm.getIssuingTerminal();
        generalInfo.nvoMove = tradingPartnerForm.getNvoMove();
        generalInfo.originZip = tradingPartnerForm.getOriginZip();
        generalInfo.doorOrigin = tradingPartnerForm.getDoorOrigin();
        generalInfo.shipperCheck = tradingPartnerForm.getShipperCheck();
        generalInfo.consigneeCheck = tradingPartnerForm.getConsigneeCheck();
        generalInfo.ert = tradingPartnerForm.getErt();
        generalInfo.goodsDesc = tradingPartnerForm.getGoodsDesc();
        generalInfo.importantNotes = tradingPartnerForm.getImportantNotes();
        generalInfo.ffComm = tradingPartnerForm.getFfComm();
        generalInfo.documentCharge = tradingPartnerForm.getDocumentCharge();
        generalInfo.lineMove = tradingPartnerForm.getLineMove();
        generalInfo.prepaidOrCollect = tradingPartnerForm.getPrepaidOrCollect();
        generalInfo.billTo = tradingPartnerForm.getBillTo();
        generalInfo.shipper = tradingPartnerForm.getShipperName();
        generalInfo.shipperNo = tradingPartnerForm.getShipperNo();
        generalInfo.consignee = tradingPartnerForm.getConsigneename();
        generalInfo.consigneeNo = tradingPartnerForm.getConsigneeNo();
        generalInfo.forwarder = tradingPartnerForm.getFowardername();
        generalInfo.forwarderNo = tradingPartnerForm.getForwarderNo();
        generalInfo.documentAmount = CommonUtils.isNotEmpty(tradingPartnerForm.getDocumentChargeAmount())?Double.parseDouble(tradingPartnerForm.getDocumentChargeAmount().replace(",", "")):0d;
        return generalInfo;
     }
     public TradingPartnerForm loadCustGeneralDefaultInformation(CustGeneralDefault custGeneralDefault,TradingPartnerForm tradingPartnerForm){
         tradingPartnerForm.setFowardername(custGeneralDefault.getForwarder());
         tradingPartnerForm.setForwarderNo(custGeneralDefault.getForwarderNo());
         tradingPartnerForm.setConsigneeNo(custGeneralDefault.getConsigneeNo());
         tradingPartnerForm.setConsigneename(custGeneralDefault.getConsignee());
         tradingPartnerForm.setShipperNo(custGeneralDefault.getShipperNo());
         tradingPartnerForm.setShipperName(custGeneralDefault.getShipper());
         tradingPartnerForm.setBillTo(custGeneralDefault.getBillTo());
         tradingPartnerForm.setPrepaidOrCollect(custGeneralDefault.getPrepaidOrCollect());
         tradingPartnerForm.setLineMove(custGeneralDefault.getLineMove());
         tradingPartnerForm.setDocumentCharge(custGeneralDefault.getDocumentCharge());
         tradingPartnerForm.setFfComm(custGeneralDefault.getFfComm());
         tradingPartnerForm.setGoodsDesc(custGeneralDefault.getGoodsDesc());
         tradingPartnerForm.setImportantNotes(custGeneralDefault.getImportantNotes());
         tradingPartnerForm.setErt(custGeneralDefault.getErt());
         tradingPartnerForm.setDoorOrigin(custGeneralDefault.getDoorOrigin());
         tradingPartnerForm.setShipperCheck(custGeneralDefault.getShipperCheck());
         tradingPartnerForm.setConsigneeCheck(custGeneralDefault.getConsigneeCheck());
         tradingPartnerForm.setOriginZip(custGeneralDefault.getOriginZip());
         tradingPartnerForm.setNvoMove(custGeneralDefault.getNvoMove());
         tradingPartnerForm.setIssuingTerminal(custGeneralDefault.getIssuingTerminal());
         tradingPartnerForm.setCommodityCode(custGeneralDefault.getCommodityCode());
         tradingPartnerForm.setPol(custGeneralDefault.getPol());
         tradingPartnerForm.setPod(custGeneralDefault.getPod());
         tradingPartnerForm.setDestination(custGeneralDefault.getDestination());
         tradingPartnerForm.setOrigin(custGeneralDefault.getOrigin());
         tradingPartnerForm.setApplyDefaultValues(custGeneralDefault.getApplyDefaultValues());
         tradingPartnerForm.setDocumentChargeAmount(null != custGeneralDefault.getDocumentAmount() ? custGeneralDefault.getDocumentAmount().toString():"");
         return tradingPartnerForm;
     }

}
