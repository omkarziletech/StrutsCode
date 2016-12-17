package com.logiware.domestic.form;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;
import java.util.*;

public class RateQuoteForm extends LogiwareActionForm {

    private String fromZip;
    private String bookingNumber;
    private String shipmentId;
    private String quoteId;
    private String carrierName;
    private String toZip;
    private String shipDate;
    private String cubicFeet;
    private String unit;
    private String TSA;
    private String weight1;
    private String weight2;
    private String weight3;
    private String weight4;
    private String weight5;
    private String weight6;
    private String class1;
    private String class2;
    private String class3;
    private String class4;
    private String class5;
    private String class6;
     private Integer pallet1;
     private Integer pallet2;
     private Integer pallet3;
     private Integer pallet4;
     private Integer pallet5;
     private Integer pallet6;
     private Integer package1;
     private Integer package2;
     private Integer package3;
     private Integer package4;
     private Integer package5;
     private Integer package6;
     private Integer length1;
     private Integer length2;
     private Integer length3;
     private Integer length4;
     private Integer length5;
     private Integer length6;
     private Integer width1;
     private Integer width2;
     private Integer width3;
     private Integer width4;
     private Integer width5;
     private Integer width6;
     private Integer height1;
     private Integer height2;
     private Integer height3;
     private Integer height4;
     private Integer height5;
     private Integer height6;
     private Double cbmOrCft1;
     private Double cbmOrCft2;
     private Double cbmOrCft3;
     private Double cbmOrCft4;
     private Double cbmOrCft5;
     private Double cbmOrCft6;
     private String palletType1;
     private String palletType2;
     private String palletType3;
     private String palletType4;
     private String palletType5;
     private String palletType6;
     private String packageType1;
     private String packageType2;
     private String packageType3;
     private String packageType4;
     private String packageType5;
     private String packageType6;

    private String collectionFee;
    private String hourCharge;
    private String hourChargeNo;
    private String hourDelivery;
    private String appont;
    private String shipDelivery;
    private String pickUp;
    private String charge;
    private String customs;
    private String BOLFee;
    private String COD;
    private String codVal;
    private String consolidate;
    private String constSite;
    private String correctBOL;
    private String crossBorderFee;
    private String delayCharge;
    private String descInspection;
    private String detentFee;
    private String driverAssist;
    private String dryRunCharge;
    private String excessLength;
    private String tradeShow;
    private String extraLabor;
    private String freezeProtect;
    private String hazmat;
    private String homeLandSec;
    private String inbondFreight;
    private String insideDelivery;
    private String layOverFee;
    private String liftGateDelivery;
    private String liftGatePickup;
    private String limitAccess;
    private String loadUnload;
    private String lumperFee;
    private String markTag;
    private String militaryDelivery;
    private String notify;
    private String permitFee;
    private String pierCharge;
    private String portCharge;
    private String reconsignment;
    private String redelivery;
    private String removeDebris;
    private String residentDelivery;
    private String satDelivery;
    private String stopCharge;
    private String sortSegregate;
    private String tarpCharge;
    private String teamCharge;
    private String truckNotUsed;
    private String truckOrderedNotUsed;
    private String unPack;
    private String holidayDel;
    private String weightInspect;
    private String weightVerify;
    private String extraLaborVal;
    private String lumperFeeVal;
    private String satDeliveryVal;
    private String sortSegregateVal;
    private String holidayDelVal;
    private String markTagVal;
    private String action;
    private Integer userId;
    private String userName;
    private List userNameList;

    public String getBOLFee() {
        return BOLFee;
    }

    public void setBOLFee(String BOLFee) {
        this.BOLFee = BOLFee;
    }

    public String getCOD() {
        return COD;
    }

    public void setCOD(String COD) {
        this.COD = COD;
    }

    public String getTSA() {
        return TSA;
    }

    public void setTSA(String TSA) {
        this.TSA = TSA;
    }

    public String getAppont() {
        return appont;
    }

    public void setAppont(String appont) {
        this.appont = appont;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getCollectionFee() {
        return collectionFee;
    }

    public void setCollectionFee(String collectionFee) {
        this.collectionFee = collectionFee;
    }

    public String getConsolidate() {
        return consolidate;
    }

    public void setConsolidate(String consolidate) {
        this.consolidate = consolidate;
    }

    public String getConstSite() {
        return constSite;
    }

    public void setConstSite(String constSite) {
        this.constSite = constSite;
    }

    public String getCorrectBOL() {
        return correctBOL;
    }

    public void setCorrectBOL(String correctBOL) {
        this.correctBOL = correctBOL;
    }

    public String getCrossBorderFee() {
        return crossBorderFee;
    }

    public void setCrossBorderFee(String crossBorderFee) {
        this.crossBorderFee = crossBorderFee;
    }

    public String getCubicFeet() {
        return cubicFeet;
    }

    public void setCubicFeet(String cubicFeet) {
        this.cubicFeet = cubicFeet;
    }

    public String getCustoms() {
        return customs;
    }

    public void setCustoms(String customs) {
        this.customs = customs;
    }

    public String getDelayCharge() {
        return delayCharge;
    }

    public void setDelayCharge(String delayCharge) {
        this.delayCharge = delayCharge;
    }

    public String getDescInspection() {
        return descInspection;
    }

    public void setDescInspection(String descInspection) {
        this.descInspection = descInspection;
    }

    public String getDetentFee() {
        return detentFee;
    }

    public void setDetentFee(String detentFee) {
        this.detentFee = detentFee;
    }

    public String getDriverAssist() {
        return driverAssist;
    }

    public void setDriverAssist(String driverAssist) {
        this.driverAssist = driverAssist;
    }

    public String getDryRunCharge() {
        return dryRunCharge;
    }

    public void setDryRunCharge(String dryRunCharge) {
        this.dryRunCharge = dryRunCharge;
    }

    public String getExcessLength() {
        return excessLength;
    }

    public void setExcessLength(String excessLength) {
        this.excessLength = excessLength;
    }

    public String getExtraLabor() {
        return extraLabor;
    }

    public void setExtraLabor(String extraLabor) {
        this.extraLabor = extraLabor;
    }

    public String getFreezeProtect() {
        return freezeProtect;
    }

    public void setFreezeProtect(String freezeProtect) {
        this.freezeProtect = freezeProtect;
    }

    public String getFromZip() {
        return null != fromZip && fromZip.length() > 5?fromZip.substring(0, 5):"";
    }

    public void setFromZip(String fromZip) {
        this.fromZip = fromZip;
    }

    public String getHazmat() {
        return hazmat;
    }

    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public String getHolidayDel() {
        return holidayDel;
    }

    public void setHolidayDel(String holidayDel) {
        this.holidayDel = holidayDel;
    }

    public String getHomeLandSec() {
        return homeLandSec;
    }

    public void setHomeLandSec(String homeLandSec) {
        this.homeLandSec = homeLandSec;
    }

    public String getHourCharge() {
        return hourCharge;
    }

    public void setHourCharge(String hourCharge) {
        this.hourCharge = hourCharge;
    }

    public String getHourDelivery() {
        return hourDelivery;
    }

    public void setHourDelivery(String hourDelivery) {
        this.hourDelivery = hourDelivery;
    }

    public String getInbondFreight() {
        return inbondFreight;
    }

    public void setInbondFreight(String inbondFreight) {
        this.inbondFreight = inbondFreight;
    }

    public String getInsideDelivery() {
        return insideDelivery;
    }

    public void setInsideDelivery(String insideDelivery) {
        this.insideDelivery = insideDelivery;
    }

    public String getLayOverFee() {
        return layOverFee;
    }

    public void setLayOverFee(String layOverFee) {
        this.layOverFee = layOverFee;
    }

    public String getLiftGateDelivery() {
        return liftGateDelivery;
    }

    public void setLiftGateDelivery(String liftGateDelivery) {
        this.liftGateDelivery = liftGateDelivery;
    }

    public String getLiftGatePickup() {
        return liftGatePickup;
    }

    public void setLiftGatePickup(String liftGatePickup) {
        this.liftGatePickup = liftGatePickup;
    }

    public String getLimitAccess() {
        return limitAccess;
    }

    public void setLimitAccess(String limitAccess) {
        this.limitAccess = limitAccess;
    }

    public String getLoadUnload() {
        return loadUnload;
    }

    public void setLoadUnload(String loadUnload) {
        this.loadUnload = loadUnload;
    }

    public String getLumperFee() {
        return lumperFee;
    }

    public void setLumperFee(String lumperFee) {
        this.lumperFee = lumperFee;
    }

    public String getMarkTag() {
        return markTag;
    }

    public void setMarkTag(String markTag) {
        this.markTag = markTag;
    }

    public String getMilitaryDelivery() {
        return militaryDelivery;
    }

    public void setMilitaryDelivery(String militaryDelivery) {
        this.militaryDelivery = militaryDelivery;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }


    public String getPermitFee() {
        return permitFee;
    }

    public void setPermitFee(String permitFee) {
        this.permitFee = permitFee;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getPierCharge() {
        return pierCharge;
    }

    public void setPierCharge(String pierCharge) {
        this.pierCharge = pierCharge;
    }

    public String getPortCharge() {
        return portCharge;
    }

    public void setPortCharge(String portCharge) {
        this.portCharge = portCharge;
    }

    public String getReconsignment() {
        return reconsignment;
    }

    public void setReconsignment(String reconsignment) {
        this.reconsignment = reconsignment;
    }

    public String getRedelivery() {
        return redelivery;
    }

    public void setRedelivery(String redelivery) {
        this.redelivery = redelivery;
    }

    public String getRemoveDebris() {
        return removeDebris;
    }

    public void setRemoveDebris(String removeDebris) {
        this.removeDebris = removeDebris;
    }

    public String getResidentDelivery() {
        return residentDelivery;
    }

    public void setResidentDelivery(String residentDelivery) {
        this.residentDelivery = residentDelivery;
    }

    public String getSatDelivery() {
        return satDelivery;
    }

    public void setSatDelivery(String satDelivery) {
        this.satDelivery = satDelivery;
    }

    public String getShipDate() {
        try {
            return DateUtils.formatDate(DateUtils.parseDate(shipDate, "MM/dd/yyyy"), "yyyy-MM-dd");
        } catch (Exception ex) {
           return "";
        }
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getShipDelivery() {
        return shipDelivery;
    }

    public void setShipDelivery(String shipDelivery) {
        this.shipDelivery = shipDelivery;
    }

    public String getSortSegregate() {
        return sortSegregate;
    }

    public void setSortSegregate(String sortSegregate) {
        this.sortSegregate = sortSegregate;
    }

    public String getStopCharge() {
        return stopCharge;
    }

    public void setStopCharge(String stopCharge) {
        this.stopCharge = stopCharge;
    }

    public String getTarpCharge() {
        return tarpCharge;
    }

    public void setTarpCharge(String tarpCharge) {
        this.tarpCharge = tarpCharge;
    }

    public String getTeamCharge() {
        return teamCharge;
    }

    public void setTeamCharge(String teamCharge) {
        this.teamCharge = teamCharge;
    }

    public String getToZip() {
        return null != toZip && toZip.length() > 5?toZip.substring(0, 5):"";
    }

    public void setToZip(String toZip) {
        this.toZip = toZip;
    }

    public String getTradeShow() {
        return tradeShow;
    }

    public void setTradeShow(String tradeShow) {
        this.tradeShow = tradeShow;
    }

    public String getTruckNotUsed() {
        return truckNotUsed;
    }

    public void setTruckNotUsed(String truckNotUsed) {
        this.truckNotUsed = truckNotUsed;
    }

    public String getTruckOrderedNotUsed() {
        return truckOrderedNotUsed;
    }

    public void setTruckOrderedNotUsed(String truckOrderedNotUsed) {
        this.truckOrderedNotUsed = truckOrderedNotUsed;
    }

    public String getUnPack() {
        return unPack;
    }

    public void setUnPack(String unPack) {
        this.unPack = unPack;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    public String getClass3() {
        return class3;
    }

    public void setClass3(String class3) {
        this.class3 = class3;
    }

    public String getClass4() {
        return class4;
    }

    public void setClass4(String class4) {
        this.class4 = class4;
    }

    public String getClass5() {
        return class5;
    }

    public void setClass5(String class5) {
        this.class5 = class5;
    }

    public String getClass6() {
        return class6;
    }

    public void setClass6(String class6) {
        this.class6 = class6;
    }

    public Integer getPallet1() {
        return pallet1;
    }

    public void setPallet1(Integer pallet1) {
        this.pallet1 = pallet1;
    }

    public Integer getPallet2() {
        return pallet2;
    }

    public void setPallet2(Integer pallet2) {
        this.pallet2 = pallet2;
    }

    public Integer getPallet3() {
        return pallet3;
    }

    public void setPallet3(Integer pallet3) {
        this.pallet3 = pallet3;
    }

    public Integer getPallet4() {
        return pallet4;
    }

    public void setPallet4(Integer pallet4) {
        this.pallet4 = pallet4;
    }

    public Integer getPallet5() {
        return pallet5;
    }

    public void setPallet5(Integer pallet5) {
        this.pallet5 = pallet5;
    }

    public Integer getPallet6() {
        return pallet6;
    }

    public void setPallet6(Integer pallet6) {
        this.pallet6 = pallet6;
    }

  

    public String getWeight1() {
        return weight1;
    }

    public void setWeight1(String weight1) {
        this.weight1 = weight1;
    }

    public String getWeight2() {
        return weight2;
    }

    public void setWeight2(String weight2) {
        this.weight2 = weight2;
    }

    public String getWeight3() {
        return weight3;
    }

    public void setWeight3(String weight3) {
        this.weight3 = weight3;
    }

    public String getWeight4() {
        return weight4;
    }

    public void setWeight4(String weight4) {
        this.weight4 = weight4;
    }

    public String getWeight5() {
        return weight5;
    }

    public void setWeight5(String weight5) {
        this.weight5 = weight5;
    }

    public String getWeight6() {
        return weight6;
    }

    public void setWeight6(String weight6) {
        this.weight6 = weight6;
    }

 

    public String getWeightInspect() {
        return weightInspect;
    }

    public void setWeightInspect(String weightInspect) {
        this.weightInspect = weightInspect;
    }

 

    public String getWeightVerify() {
        return weightVerify;
    }

    public void setWeightVerify(String weightVerify) {
        this.weightVerify = weightVerify;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getHourChargeNo() {
        return hourChargeNo;
    }

    public void setHourChargeNo(String hourChargeNo) {
        this.hourChargeNo = hourChargeNo;
    }

    public String getCodVal() {
        return codVal;
    }

    public void setCodVal(String codVal) {
        this.codVal = codVal;
    }

    public String getExtraLaborVal() {
        return extraLaborVal;
    }

    public void setExtraLaborVal(String extraLaborVal) {
        this.extraLaborVal = extraLaborVal;
    }

    public String getLumperFeeVal() {
        return lumperFeeVal;
    }

    public void setLumperFeeVal(String lumperFeeVal) {
        this.lumperFeeVal = lumperFeeVal;
    }

    public String getMarkTagVal() {
        return markTagVal;
    }

    public void setMarkTagVal(String markTagVal) {
        this.markTagVal = markTagVal;
    }

    public String getHolidayDelVal() {
        return holidayDelVal;
    }

    public void setHolidayDelVal(String holidayDelVal) {
        this.holidayDelVal = holidayDelVal;
    }

    public String getSatDeliveryVal() {
        return satDeliveryVal;
    }

    public void setSatDeliveryVal(String satDeliveryVal) {
        this.satDeliveryVal = satDeliveryVal;
    }

    public String getSortSegregateVal() {
        return sortSegregateVal;
    }

    public void setSortSegregateVal(String sortSegregateVal) {
        this.sortSegregateVal = sortSegregateVal;
    }

    public Integer getPackage1() {
        return package1;
    }

    public void setPackage1(Integer package1) {
        this.package1 = package1;
    }

    public Integer getPackage2() {
        return package2;
    }

    public void setPackage2(Integer package2) {
        this.package2 = package2;
    }

    public Integer getPackage3() {
        return package3;
    }

    public void setPackage3(Integer package3) {
        this.package3 = package3;
    }

    public Integer getPackage4() {
        return package4;
    }

    public void setPackage4(Integer package4) {
        this.package4 = package4;
    }

    public Integer getPackage5() {
        return package5;
    }

    public void setPackage5(Integer package5) {
        this.package5 = package5;
    }

    public Integer getPackage6() {
        return package6;
    }

    public void setPackage6(Integer package6) {
        this.package6 = package6;
    }


    public Integer getHeight1() {
        return height1;
    }

    public void setHeight1(Integer height1) {
        this.height1 = height1;
    }

    public Integer getHeight2() {
        return height2;
    }

    public void setHeight2(Integer height2) {
        this.height2 = height2;
    }

    public Integer getHeight3() {
        return height3;
    }

    public void setHeight3(Integer height3) {
        this.height3 = height3;
    }

    public Integer getHeight4() {
        return height4;
    }

    public void setHeight4(Integer height4) {
        this.height4 = height4;
    }

    public Integer getHeight5() {
        return height5;
    }

    public void setHeight5(Integer height5) {
        this.height5 = height5;
    }

    public Integer getHeight6() {
        return height6;
    }

    public void setHeight6(Integer height6) {
        this.height6 = height6;
    }

    public Integer getLength1() {
        return length1;
    }

    public void setLength1(Integer length1) {
        this.length1 = length1;
    }

    public Integer getLength2() {
        return length2;
    }

    public void setLength2(Integer length2) {
        this.length2 = length2;
    }

    public Integer getLength3() {
        return length3;
    }

    public void setLength3(Integer length3) {
        this.length3 = length3;
    }

    public Integer getLength4() {
        return length4;
    }

    public void setLength4(Integer length4) {
        this.length4 = length4;
    }

    public Integer getLength5() {
        return length5;
    }

    public void setLength5(Integer length5) {
        this.length5 = length5;
    }

    public Integer getLength6() {
        return length6;
    }

    public void setLength6(Integer length6) {
        this.length6 = length6;
    }

    public Integer getWidth1() {
        return width1;
    }

    public void setWidth1(Integer width1) {
        this.width1 = width1;
    }

    public Integer getWidth2() {
        return width2;
    }

    public void setWidth2(Integer width2) {
        this.width2 = width2;
    }

    public Integer getWidth3() {
        return width3;
    }

    public void setWidth3(Integer width3) {
        this.width3 = width3;
    }

    public Integer getWidth4() {
        return width4;
    }

    public void setWidth4(Integer width4) {
        this.width4 = width4;
    }

    public Integer getWidth5() {
        return width5;
    }

    public void setWidth5(Integer width5) {
        this.width5 = width5;
    }

    public Integer getWidth6() {
        return width6;
    }

    public void setWidth6(Integer width6) {
        this.width6 = width6;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public Double getCbmOrCft1() {
        return cbmOrCft1;
    }

    public void setCbmOrCft1(Double cbmOrCft1) {
        this.cbmOrCft1 = cbmOrCft1;
    }

    public Double getCbmOrCft2() {
        return cbmOrCft2;
    }

    public void setCbmOrCft2(Double cbmOrCft2) {
        this.cbmOrCft2 = cbmOrCft2;
    }

    public Double getCbmOrCft3() {
        return cbmOrCft3;
    }

    public void setCbmOrCft3(Double cbmOrCft3) {
        this.cbmOrCft3 = cbmOrCft3;
    }

    public Double getCbmOrCft4() {
        return cbmOrCft4;
    }

    public void setCbmOrCft4(Double cbmOrCft4) {
        this.cbmOrCft4 = cbmOrCft4;
    }

    public Double getCbmOrCft5() {
        return cbmOrCft5;
    }

    public void setCbmOrCft5(Double cbmOrCft5) {
        this.cbmOrCft5 = cbmOrCft5;
    }

    public Double getCbmOrCft6() {
        return cbmOrCft6;
    }

    public void setCbmOrCft6(Double cbmOrCft6) {
        this.cbmOrCft6 = cbmOrCft6;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List getUserNameList() {
        return new UserDAO().getDomesticUser();
    }

    public void setUserNameList(List userNameList) {
        this.userNameList = userNameList;
    }

    public String getPackageType1() {
        return packageType1;
    }

    public void setPackageType1(String packageType1) {
        this.packageType1 = packageType1;
    }

    public String getPackageType2() {
        return packageType2;
    }

    public void setPackageType2(String packageType2) {
        this.packageType2 = packageType2;
    }

    public String getPackageType3() {
        return packageType3;
    }

    public void setPackageType3(String packageType3) {
        this.packageType3 = packageType3;
    }

    public String getPackageType4() {
        return packageType4;
    }

    public void setPackageType4(String packageType4) {
        this.packageType4 = packageType4;
    }

    public String getPackageType5() {
        return packageType5;
    }

    public void setPackageType5(String packageType5) {
        this.packageType5 = packageType5;
    }

    public String getPackageType6() {
        return packageType6;
    }

    public void setPackageType6(String packageType6) {
        this.packageType6 = packageType6;
    }

    public String getPalletType1() {
        return palletType1;
    }

    public void setPalletType1(String palletType1) {
        this.palletType1 = palletType1;
    }

    public String getPalletType2() {
        return palletType2;
    }

    public void setPalletType2(String palletType2) {
        this.palletType2 = palletType2;
    }

    public String getPalletType3() {
        return palletType3;
    }

    public void setPalletType3(String palletType3) {
        this.palletType3 = palletType3;
    }

    public String getPalletType4() {
        return palletType4;
    }

    public void setPalletType4(String palletType4) {
        this.palletType4 = palletType4;
    }

    public String getPalletType5() {
        return palletType5;
    }

    public void setPalletType5(String palletType5) {
        this.palletType5 = palletType5;
    }

    public String getPalletType6() {
        return palletType6;
    }

    public void setPalletType6(String palletType6) {
        this.palletType6 = palletType6;
    }
    
}
