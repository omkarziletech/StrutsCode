package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author Rajesh
 */
public class LclEculineEdiBlInfoForm extends LogiwareActionForm {

    private String id;
    private String shipperCode;
    private String shipperNad;
    private String polUncode;
    private String podUncode;
    private String poddeliveryUncode;
    private String consCode;
    private String consNad;
    private String polDesc;
    private String podDesc;
    private String poddeliveryDesc;
    private String notify1Code;
    private String notify1Nad;
    private String precarriageBy;
    private String notify2Nad;
    private String notify2Code;
    private String placeOfReceipt;
    private String porUncode;
    private String[] packageAmount;
    private String[] packageDesc;
    private String[] packageId;
    private String[] ecuPackageDesc;
    private String[] goodDesc;
    private String[] dischargeInstruction;
    private String[] weightValues;
    private String[] volumeValues;
    private String[] commercialValue;
    private String[] currency;
    private String[] marksNo;
    private String manualShipper;
    private String manualCons;
    private String manualNotify1;
    private String manualNotify2;
    private boolean chkManualSh;
    private boolean chkManualCo;
    private boolean chkManualNo1;
    private boolean chkManualNo2;
    private String unitApproved;
    private String methodName;
    private String ecuShipperAdd;
    private String ecuConsigneeAdd;
    private String ecuNotify1Add;
    private String ecuNotify2Add;
    private String ecuPOLDesc;
    private String ecuPODDesc;
    private String ecuDeliverDesc;
    private String ecuPolUncode;
    private String ecuPodUncode;
    private String ecuPoddeliveryUncode;
    private String ecuShipCode;
    private String ecuConCode;
    private String ecuNotify1Code;
    private String ecuNotify2Code;
    private String houseBlNo;
    private String message;
    private String containerNo;
    private String voyNo;
    private String fileNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsNad() {
        return consNad;
    }

    public void setConsNad(String consNad) {
        this.consNad = consNad;
    }

    public String getNotify1Nad() {
        return notify1Nad;
    }

    public void setNotify1Nad(String notify1Nad) {
        this.notify1Nad = notify1Nad;
    }

    public String getNotify2Nad() {
        return notify2Nad;
    }

    public void setNotify2Nad(String notify2Nad) {
        this.notify2Nad = notify2Nad;
    }

    public String getPlaceOfReceipt() {
        return placeOfReceipt;
    }

    public void setPlaceOfReceipt(String placeOfReceipt) {
        this.placeOfReceipt = placeOfReceipt;
    }

    public String getPodDesc() {
        return podDesc;
    }

    public void setPodDesc(String podDesc) {
        this.podDesc = podDesc;
    }

    public String getPodUncode() {
        return podUncode;
    }

    public void setPodUncode(String podUncode) {
        this.podUncode = podUncode;
    }

    public String getPoddeliveryDesc() {
        return poddeliveryDesc;
    }

    public void setPoddeliveryDesc(String poddeliveryDesc) {
        this.poddeliveryDesc = poddeliveryDesc;
    }

    public String getPoddeliveryUncode() {
        return poddeliveryUncode;
    }

    public void setPoddeliveryUncode(String poddeliveryUncode) {
        this.poddeliveryUncode = poddeliveryUncode;
    }

    public String getPolDesc() {
        return polDesc;
    }

    public void setPolDesc(String polDesc) {
        this.polDesc = polDesc;
    }

    public String getPolUncode() {
        return polUncode;
    }

    public void setPolUncode(String polUncode) {
        this.polUncode = polUncode;
    }

    public String getPrecarriageBy() {
        return precarriageBy;
    }

    public void setPrecarriageBy(String precarriageBy) {
        this.precarriageBy = precarriageBy;
    }

    public String getShipperNad() {
        return shipperNad;
    }

    public void setShipperNad(String shipperNad) {
        this.shipperNad = shipperNad;
    }

    public String[] getCommercialValue() {
        return commercialValue;
    }

    public void setCommercialValue(String[] commercialValue) {
        this.commercialValue = commercialValue;
    }

    public String[] getCurrency() {
        return currency;
    }

    public void setCurrency(String[] currency) {
        this.currency = currency;
    }

    public String[] getDischargeInstruction() {
        return dischargeInstruction;
    }

    public void setDischargeInstruction(String[] dischargeInstruction) {
        this.dischargeInstruction = dischargeInstruction;
    }

    public String[] getGoodDesc() {
        return goodDesc;
    }

    public void setGoodDesc(String[] goodDesc) {
        this.goodDesc = goodDesc;
    }

    public String[] getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String[] packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String[] getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String[] packageDesc) {
        this.packageDesc = packageDesc;
    }

    public String[] getVolumeValues() {
        return volumeValues;
    }

    public void setVolumeValues(String[] volumeValues) {
        this.volumeValues = volumeValues;
    }

    public String[] getWeightValues() {
        return weightValues;
    }

    public void setWeightValues(String[] weightValues) {
        this.weightValues = weightValues;
    }

    public String getConsCode() {
        return consCode;
    }

    public void setConsCode(String consCode) {
        this.consCode = consCode;
    }

    public String getNotify1Code() {
        return notify1Code;
    }

    public void setNotify1Code(String notify1Code) {
        this.notify1Code = notify1Code;
    }

    public String getNotify2Code() {
        return notify2Code;
    }

    public void setNotify2Code(String notify2Code) {
        this.notify2Code = notify2Code;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public boolean isChkManualCo() {
        return chkManualCo;
    }

    public void setChkManualCo(boolean chkManualCo) {
        this.chkManualCo = chkManualCo;
    }

    public boolean isChkManualNo1() {
        return chkManualNo1;
    }

    public void setChkManualNo1(boolean chkManualNo1) {
        this.chkManualNo1 = chkManualNo1;
    }

    public boolean isChkManualNo2() {
        return chkManualNo2;
    }

    public void setChkManualNo2(boolean chkManualNo2) {
        this.chkManualNo2 = chkManualNo2;
    }

    public boolean isChkManualSh() {
        return chkManualSh;
    }

    public void setChkManualSh(boolean chkManualSh) {
        this.chkManualSh = chkManualSh;
    }

    public String getManualCons() {
        return manualCons;
    }

    public void setManualCons(String manualCons) {
        this.manualCons = manualCons;
    }

    public String getManualNotify1() {
        return manualNotify1;
    }

    public void setManualNotify1(String manualNotify1) {
        this.manualNotify1 = manualNotify1;
    }

    public String getManualNotify2() {
        return manualNotify2;
    }

    public void setManualNotify2(String manualNotify2) {
        this.manualNotify2 = manualNotify2;
    }

    public String getManualShipper() {
        return manualShipper;
    }

    public void setManualShipper(String manualShipper) {
        this.manualShipper = manualShipper;
    }

    public String[] getMarksNo() {
        return marksNo;
    }

    public void setMarksNo(String[] marksNo) {
        this.marksNo = marksNo;
    }

    public String getUnitApproved() {
        return unitApproved;
    }

    public void setUnitApproved(String unitApproved) {
        this.unitApproved = unitApproved;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPorUncode() {
        return porUncode;
    }

    public void setPorUncode(String porUncode) {
        this.porUncode = porUncode;
    }

    public String[] getEcuPackageDesc() {
        return ecuPackageDesc;
    }

    public void setEcuPackageDesc(String[] ecuPackageDesc) {
        this.ecuPackageDesc = ecuPackageDesc;
    }

    public String[] getPackageId() {
        return packageId;
    }

    public void setPackageId(String[] packageId) {
        this.packageId = packageId;
    }

    public String getEcuConsigneeAdd() {
        return ecuConsigneeAdd;
    }

    public void setEcuConsigneeAdd(String ecuConsigneeAdd) {
        this.ecuConsigneeAdd = ecuConsigneeAdd;
    }

    public String getEcuNotify1Add() {
        return ecuNotify1Add;
    }

    public void setEcuNotify1Add(String ecuNotify1Add) {
        this.ecuNotify1Add = ecuNotify1Add;
    }

    public String getEcuNotify2Add() {
        return ecuNotify2Add;
    }

    public void setEcuNotify2Add(String ecuNotify2Add) {
        this.ecuNotify2Add = ecuNotify2Add;
    }

    public String getEcuShipperAdd() {
        return ecuShipperAdd;
    }

    public void setEcuShipperAdd(String ecuShipperAdd) {
        this.ecuShipperAdd = ecuShipperAdd;
    }

    public String getEcuDeliverDesc() {
        return ecuDeliverDesc;
    }

    public void setEcuDeliverDesc(String ecuDeliverDesc) {
        this.ecuDeliverDesc = ecuDeliverDesc;
    }

    public String getEcuPODDesc() {
        return ecuPODDesc;
    }

    public void setEcuPODDesc(String ecuPODDesc) {
        this.ecuPODDesc = ecuPODDesc;
    }

    public String getEcuPOLDesc() {
        return ecuPOLDesc;
    }

    public void setEcuPOLDesc(String ecuPOLDesc) {
        this.ecuPOLDesc = ecuPOLDesc;
    }

    public String getEcuPodUncode() {
        return ecuPodUncode;
    }

    public void setEcuPodUncode(String ecuPodUncode) {
        this.ecuPodUncode = ecuPodUncode;
    }

    public String getEcuPoddeliveryUncode() {
        return ecuPoddeliveryUncode;
    }

    public void setEcuPoddeliveryUncode(String ecuPoddeliveryUncode) {
        this.ecuPoddeliveryUncode = ecuPoddeliveryUncode;
    }

    public String getEcuPolUncode() {
        return ecuPolUncode;
    }

    public void setEcuPolUncode(String ecuPolUncode) {
        this.ecuPolUncode = ecuPolUncode;
    }

    public String getEcuConCode() {
        return ecuConCode;
    }

    public void setEcuConCode(String ecuConCode) {
        this.ecuConCode = ecuConCode;
    }

   

    public String getEcuNotify1Code() {
        return ecuNotify1Code;
    }

    public void setEcuNotify1Code(String ecuNotify1Code) {
        this.ecuNotify1Code = ecuNotify1Code;
    }

    public String getEcuNotify2Code() {
        return ecuNotify2Code;
    }

    public void setEcuNotify2Code(String ecuNotify2Code) {
        this.ecuNotify2Code = ecuNotify2Code;
    }

    public String getEcuShipCode() {
        return ecuShipCode;
    }

    public void setEcuShipCode(String ecuShipCode) {
        this.ecuShipCode = ecuShipCode;
    }

    public String getHouseBlNo() {
        return houseBlNo;
    }

    public void setHouseBlNo(String houseBlNo) {
        this.houseBlNo = houseBlNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getVoyNo() {
        return voyNo;
    }

    public void setVoyNo(String voyNo) {
        this.voyNo = voyNo;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }
    
}
