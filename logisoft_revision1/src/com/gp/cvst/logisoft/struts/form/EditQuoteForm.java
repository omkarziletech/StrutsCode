package com.gp.cvst.logisoft.struts.form;

import com.logiware.form.EventForm;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

public class EditQuoteForm extends EventForm {

    private String customerName1;
    private String quotationNo;
    private String quotationDate;
    private String ratesRemarks;
    private String contactName;
    private String otherinclude;
    private String noOfDays;
    private String routedAgent;
    private String adjestment;
    private String otherprint;
    private String[] otherchargeCddesc;
    private String specialEqpmt;
    private String[] currecny;
    private String[] othercurrecny;
    private String[] otherNewRate;
    private String commodityPrint;
    private String[] newrate;
    private String[] chargeCddesc;
    private String carrierPrint;
    private String transitTime;
    private String agent;
    private String remarks;
    private String remarks1;
    private String phone;
    private String from;
    private String issuingTerminal;
    private String defaultAgent;
    private String fax;
    private String zip;
    private String originCheck;
    private String polCheck;
    private String podCheck;
    private String destinationCheck;
    private String poe;
    private String soc;
    private String finalized;
    private String directConsignmntCheck;
    private String[] perChargeCode;
    private String[] perChargeCodeDesc;
    private String[] perCostType;
    private String[] perActiveAmt;
    private String[] perMinimum;
    private String[] percurrecny;
    private String check2;
    private String printDesc;
    private String[] otherEffectiveDate;
    private String[] effectiveDate;
    private String[] retail1;
    private String baht;
    private String bdt;
    private String cyp;
    private String eur;
    private String hkd;
    private String lkr;
    private String nt;
    private String prs;
    private String rmb;
    private String won;
    private String yen;
    private String myr;
    private String nht;
    private String pkr;
    private String rm;
    private String spo;
    private String vnd;
    private String inr;
    private String check3;
    private String portofLading;
    private String portofDischarge;
    private String stateofOrigin;
    //private String clientNumber;
    private String buttonValue;
    private String index;
    private String commcode;
    private String description;
    private String sslcode;
    private String sslDescription;
    private String isTerminal;
    private String finalDestination;
    private String placeofDelivery;
    private String placeofReceipt;
//	 for hidden values
    private String originNumber;
    private String dischargeNo;
    private String commdityId;
    private String numbIdx;

//	 added today
    private String[] costType;
    private String customerName;
    private String clientNumber;
    private String specialequipment = "N";
    private String goodsdesc;
    private String alternateagent;
    private String hazmat = "N";
    private String localdryage = "N";
    private String insurance = "N";
    private String customertoprovideSED = "N";
    private String deductFFcomm = "N";
    private String amount;
    private String intermodel = "N";
    private String amount1;
    private String costofgoods;
    private String outofgate = "N";
    private String insurancamt;
    private String typeofMove;
    private String sizeType;
    private String routedAgentCheck;
    private String ldprint;
    private String idinclude;
    private String idprint;
    private String insureinclude;
    private String insureprint;
    private String routedbymsg;
    private String clienttype;
    private String carrier;
    private String[] unitType;
    private String[] numbers;
    private String[] chargeCodes;
    private String[] chargeAmount;
    private String[] chargeMarkUp;
    private String markUp;
    private String unitType1;
    private String numbers1;
    private String chargeCodes1;
    private Double chargeAmount1;
    private String unitType2;
    private String numbers2;
    private String chargeCodes2;
    private Double chargeAmount2;
    private String unitType3;
    private String numbers3;
    private String chargeCodes3;
    private Double chargeAmount3;
    private String unitType4;
    private String numbers4;
    private String chargeCodes4;
    private Double chargeAmount4;
    private String unitType5;
    private String numbers5;
    private String chargeCodes5;
    private Double chargeAmount5;
    private String unitType6;
    private String numbers6;
    private String chargeCodes6;
    private Double chargeAmount6;
    private String unitType7;
    private String numbers7;
    private String chargeCodes7;
    private Double chargeAmount7;
    private String email;
    private String comment;
    private String[] include;
    private String[] print;
    private String check;
    private String[] chargeCd;
    private String[] costType1;
    private String[] retail;
    private String[] CTC;
    private String[] ftf;
    private String[] minimum;
    private String check1;
    private String doorOrigin;
    private String doorDestination;
    private String clientConsigneeCheck;
    private String specialEqpmtUnit;
    private String bulletRatesCheck;
    private String greenDollarUseTrueCost;
    private String brand;
    private String chassisCharge;

    private String newClient;
    private boolean aesFilling;

    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getOtherinclude() {
        return otherinclude;
    }

    public void setOtherinclude(String otherinclude) {
        this.otherinclude = otherinclude;
    }

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getOtherprint() {
        return otherprint;
    }

    public void setOtherprint(String otherprint) {
        this.otherprint = otherprint;
    }

    public String[] getOtherchargeCddesc() {
        return otherchargeCddesc;
    }

    public void setOtherchargeCddesc(String[] otherchargeCddesc) {
        this.otherchargeCddesc = otherchargeCddesc;
    }

    public String getSpecialEqpmt() {
        return specialEqpmt;
    }

    public void setSpecialEqpmt(String specialEqpmt) {
        this.specialEqpmt = specialEqpmt;
    }

    public String[] getCurrecny() {
        return currecny;
    }

    public void setCurrecny(String[] currecny) {
        this.currecny = currecny;
    }

    public String[] getOthercurrecny() {
        return othercurrecny;
    }

    public void setOthercurrecny(String[] othercurrecny) {
        this.othercurrecny = othercurrecny;
    }

    public String[] getOtherNewRate() {
        return otherNewRate;
    }

    public void setOtherNewRate(String[] otherNewRate) {
        this.otherNewRate = otherNewRate;
    }

    public String getCommodityPrint() {
        return commodityPrint;
    }

    public void setCommodityPrint(String commodityPrint) {
        this.commodityPrint = commodityPrint;
    }

    public String[] getNewrate() {
        return newrate;
    }

    public void setNewrate(String[] newrate) {
        this.newrate = newrate;
    }

    public String[] getChargeCddesc() {
        return chargeCddesc;
    }

    public void setChargeCddesc(String[] chargeCddesc) {
        this.chargeCddesc = chargeCddesc;
    }

    public String getCarrierPrint() {
        return carrierPrint;
    }

    public void setCarrierPrint(String carrierPrint) {
        this.carrierPrint = carrierPrint;
    }

    public String getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(String transitTime) {
        this.transitTime = transitTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getOriginCheck() {
        return originCheck;
    }

    public void setOriginCheck(String originCheck) {
        this.originCheck = originCheck;
    }

    public String getPolCheck() {
        return polCheck;
    }

    public void setPolCheck(String polCheck) {
        this.polCheck = polCheck;
    }

    public String getPodCheck() {
        return podCheck;
    }

    public void setPodCheck(String podCheck) {
        this.podCheck = podCheck;
    }

    public String getDestinationCheck() {
        return destinationCheck;
    }

    public void setDestinationCheck(String destinationCheck) {
        this.destinationCheck = destinationCheck;
    }

    public String getPoe() {
        return poe;
    }

    public void setPoe(String poe) {
        this.poe = poe;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getFinalized() {
        return finalized;
    }

    public void setFinalized(String finalized) {
        this.finalized = finalized;
    }

    public String[] getPerChargeCode() {
        return perChargeCode;
    }

    public void setPerChargeCode(String[] perChargeCode) {
        this.perChargeCode = perChargeCode;
    }

    public String[] getPerChargeCodeDesc() {
        return perChargeCodeDesc;
    }

    public void setPerChargeCodeDesc(String[] perChargeCodeDesc) {
        this.perChargeCodeDesc = perChargeCodeDesc;
    }

    public String[] getPerCostType() {
        return perCostType;
    }

    public void setPerCostType(String[] perCostType) {
        this.perCostType = perCostType;
    }

    public String[] getPerActiveAmt() {
        return perActiveAmt;
    }

    public void setPerActiveAmt(String[] perActiveAmt) {
        this.perActiveAmt = perActiveAmt;
    }

    public String[] getPerMinimum() {
        return perMinimum;
    }

    public void setPerMinimum(String[] perMinimum) {
        this.perMinimum = perMinimum;
    }

    public String[] getPercurrecny() {
        return percurrecny;
    }

    public void setPercurrecny(String[] percurrecny) {
        this.percurrecny = percurrecny;
    }

    public String getCheck2() {
        return check2;
    }

    public void setCheck2(String check2) {
        this.check2 = check2;
    }

    public String getPrintDesc() {
        return printDesc;
    }

    public void setPrintDesc(String printDesc) {
        this.printDesc = printDesc;
    }

    public String[] getOtherEffectiveDate() {
        return otherEffectiveDate;
    }

    public void setOtherEffectiveDate(String[] otherEffectiveDate) {
        this.otherEffectiveDate = otherEffectiveDate;
    }

    public String[] getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String[] effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String[] getRetail1() {
        return retail1;
    }

    public void setRetail1(String[] retail1) {
        this.retail1 = retail1;
    }

    public String getBaht() {
        return baht;
    }

    public void setBaht(String baht) {
        this.baht = baht;
    }

    public String getBdt() {
        return bdt;
    }

    public void setBdt(String bdt) {
        this.bdt = bdt;
    }

    public String getCyp() {
        return cyp;
    }

    public void setCyp(String cyp) {
        this.cyp = cyp;
    }

    public String getEur() {
        return eur;
    }

    public void setEur(String eur) {
        this.eur = eur;
    }

    public String getHkd() {
        return hkd;
    }

    public void setHkd(String hkd) {
        this.hkd = hkd;
    }

    public String getLkr() {
        return lkr;
    }

    public void setLkr(String lkr) {
        this.lkr = lkr;
    }

    public String getNt() {
        return nt;
    }

    public void setNt(String nt) {
        this.nt = nt;
    }

    public String getPrs() {
        return prs;
    }

    public void setPrs(String prs) {
        this.prs = prs;
    }

    public String getRmb() {
        return rmb;
    }

    public void setRmb(String rmb) {
        this.rmb = rmb;
    }

    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }

    public String getYen() {
        return yen;
    }

    public void setYen(String yen) {
        this.yen = yen;
    }

    public String getMyr() {
        return myr;
    }

    public void setMyr(String myr) {
        this.myr = myr;
    }

    public String getNht() {
        return nht;
    }

    public void setNht(String nht) {
        this.nht = nht;
    }

    public String getPkr() {
        return pkr;
    }

    public void setPkr(String pkr) {
        this.pkr = pkr;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public String getSpo() {
        return spo;
    }

    public void setSpo(String spo) {
        this.spo = spo;
    }

    public String getVnd() {
        return vnd;
    }

    public void setVnd(String vnd) {
        this.vnd = vnd;
    }

    public String getInr() {
        return inr;
    }

    public void setInr(String inr) {
        this.inr = inr;
    }

    public String getCheck3() {
        return check3;
    }

    public void setCheck3(String check3) {
        this.check3 = check3;
    }

    public String getPortofLading() {
        return portofLading;
    }

    public void setPortofLading(String portofLading) {
        this.portofLading = portofLading;
    }

    public String getPortofDischarge() {
        return portofDischarge;
    }

    public void setPortofDischarge(String portofDischarge) {
        this.portofDischarge = portofDischarge;
    }

    public String getStateofOrigin() {
        return stateofOrigin;
    }

    public void setStateofOrigin(String stateofOrigin) {
        this.stateofOrigin = stateofOrigin;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCommcode() {
        return commcode;
    }

    public void setCommcode(String commcode) {
        this.commcode = commcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSslcode() {
        return sslcode;
    }

    public void setSslcode(String sslcode) {
        this.sslcode = sslcode;
    }

    public String getSslDescription() {
        return sslDescription;
    }

    public void setSslDescription(String sslDescription) {
        this.sslDescription = sslDescription;
    }

    public String getIsTerminal() {
        return isTerminal;
    }

    public void setIsTerminal(String isTerminal) {
        this.isTerminal = isTerminal;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getPlaceofDelivery() {
        return placeofDelivery;
    }

    public void setPlaceofDelivery(String placeofDelivery) {
        this.placeofDelivery = placeofDelivery;
    }

    public String getPlaceofReceipt() {
        return placeofReceipt;
    }

    public void setPlaceofReceipt(String placeofReceipt) {
        this.placeofReceipt = placeofReceipt;
    }

    public String getOriginNumber() {
        return originNumber;
    }

    public void setOriginNumber(String originNumber) {
        this.originNumber = originNumber;
    }

    public String getDischargeNo() {
        return dischargeNo;
    }

    public void setDischargeNo(String dischargeNo) {
        this.dischargeNo = dischargeNo;
    }

    public String getCommdityId() {
        return commdityId;
    }

    public void setCommdityId(String commdityId) {
        this.commdityId = commdityId;
    }

    public String getNumbIdx() {
        return numbIdx;
    }

    public void setNumbIdx(String numbIdx) {
        this.numbIdx = numbIdx;
    }

    public String[] getCostType() {
        return costType;
    }

    public void setCostType(String[] costType) {
        this.costType = costType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getSpecialequipment() {
        return specialequipment;
    }

    public void setSpecialequipment(String specialequipment) {
        this.specialequipment = specialequipment;
    }

    public String getGoodsdesc() {
        return goodsdesc;
    }

    public void setGoodsdesc(String goodsdesc) {
        this.goodsdesc = goodsdesc;
    }

    public String getAlternateagent() {
        return alternateagent;
    }

    public void setAlternateagent(String alternateagent) {
        this.alternateagent = alternateagent;
    }

    public String getHazmat() {
        return hazmat;
    }

    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public String getLocaldryage() {
        return localdryage;
    }

    public void setLocaldryage(String localdryage) {
        this.localdryage = localdryage;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getCustomertoprovideSED() {
        return customertoprovideSED;
    }

    public void setCustomertoprovideSED(String customertoprovideSED) {
        this.customertoprovideSED = customertoprovideSED;
    }

    public String getDeductFFcomm() {
        return deductFFcomm;
    }

    public void setDeductFFcomm(String deductFFcomm) {
        this.deductFFcomm = deductFFcomm;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIntermodel() {
        return intermodel;
    }

    public void setIntermodel(String intermodel) {
        this.intermodel = intermodel;
    }

    public String getAmount1() {
        return amount1;
    }

    public void setAmount1(String amount1) {
        this.amount1 = amount1;
    }

    public String getCostofgoods() {
        return costofgoods;
    }

    public void setCostofgoods(String costofgoods) {
        this.costofgoods = costofgoods;
    }

    public String getOutofgate() {
        return outofgate;
    }

    public void setOutofgate(String outofgate) {
        this.outofgate = outofgate;
    }

    public String getInsurancamt() {
        return insurancamt;
    }

    public void setInsurancamt(String insurancamt) {
        this.insurancamt = insurancamt;
    }

    public String getTypeofMove() {
        return typeofMove;
    }

    public void setTypeofMove(String typeofMove) {
        this.typeofMove = typeofMove;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public String getLdprint() {
        return ldprint;
    }

    public String getRoutedAgentCheck() {
        return routedAgentCheck;
    }

    public void setRoutedAgentCheck(String routedAgentCheck) {
        this.routedAgentCheck = routedAgentCheck;
    }

    public void setLdprint(String ldprint) {
        this.ldprint = ldprint;
    }

    public String getIdinclude() {
        return idinclude;
    }

    public void setIdinclude(String idinclude) {
        this.idinclude = idinclude;
    }

    public String getIdprint() {
        return idprint;
    }

    public void setIdprint(String idprint) {
        this.idprint = idprint;
    }

    public String getInsureinclude() {
        return insureinclude;
    }

    public void setInsureinclude(String insureinclude) {
        this.insureinclude = insureinclude;
    }

    public String getInsureprint() {
        return insureprint;
    }

    public void setInsureprint(String insureprint) {
        this.insureprint = insureprint;
    }

    public String getRoutedbymsg() {
        return routedbymsg;
    }

    public void setRoutedbymsg(String routedbymsg) {
        this.routedbymsg = routedbymsg;
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String[] getUnitType() {
        return unitType;
    }

    public void setUnitType(String[] unitType) {
        this.unitType = unitType;
    }

    public String[] getNumbers() {
        return numbers;
    }

    public void setNumbers(String[] numbers) {
        this.numbers = numbers;
    }

    public String[] getChargeCodes() {
        return chargeCodes;
    }

    public void setChargeCodes(String[] chargeCodes) {
        this.chargeCodes = chargeCodes;
    }

    public String[] getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String[] chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String[] getChargeMarkUp() {
        return chargeMarkUp;
    }

    public void setChargeMarkUp(String[] chargeMarkUp) {
        this.chargeMarkUp = chargeMarkUp;
    }

    public String getMarkUp() {
        return markUp;
    }

    public void setMarkUp(String markUp) {
        this.markUp = markUp;
    }

    public String getUnitType1() {
        return unitType1;
    }

    public void setUnitType1(String unitType1) {
        this.unitType1 = unitType1;
    }

    public String getNumbers1() {
        return numbers1;
    }

    public void setNumbers1(String numbers1) {
        this.numbers1 = numbers1;
    }

    public String getChargeCodes1() {
        return chargeCodes1;
    }

    public void setChargeCodes1(String chargeCodes1) {
        this.chargeCodes1 = chargeCodes1;
    }

    public Double getChargeAmount1() {
        return chargeAmount1;
    }

    public void setChargeAmount1(Double chargeAmount1) {
        this.chargeAmount1 = chargeAmount1;
    }

    public String getUnitType2() {
        return unitType2;
    }

    public void setUnitType2(String unitType2) {
        this.unitType2 = unitType2;
    }

    public String getNumbers2() {
        return numbers2;
    }

    public void setNumbers2(String numbers2) {
        this.numbers2 = numbers2;
    }

    public String getChargeCodes2() {
        return chargeCodes2;
    }

    public void setChargeCodes2(String chargeCodes2) {
        this.chargeCodes2 = chargeCodes2;
    }

    public Double getChargeAmount2() {
        return chargeAmount2;
    }

    public void setChargeAmount2(Double chargeAmount2) {
        this.chargeAmount2 = chargeAmount2;
    }

    public String getUnitType3() {
        return unitType3;
    }

    public void setUnitType3(String unitType3) {
        this.unitType3 = unitType3;
    }

    public String getNumbers3() {
        return numbers3;
    }

    public void setNumbers3(String numbers3) {
        this.numbers3 = numbers3;
    }

    public String getChargeCodes3() {
        return chargeCodes3;
    }

    public void setChargeCodes3(String chargeCodes3) {
        this.chargeCodes3 = chargeCodes3;
    }

    public Double getChargeAmount3() {
        return chargeAmount3;
    }

    public void setChargeAmount3(Double chargeAmount3) {
        this.chargeAmount3 = chargeAmount3;
    }

    public String getUnitType4() {
        return unitType4;
    }

    public void setUnitType4(String unitType4) {
        this.unitType4 = unitType4;
    }

    public String getNumbers4() {
        return numbers4;
    }

    public void setNumbers4(String numbers4) {
        this.numbers4 = numbers4;
    }

    public String getChargeCodes4() {
        return chargeCodes4;
    }

    public void setChargeCodes4(String chargeCodes4) {
        this.chargeCodes4 = chargeCodes4;
    }

    public Double getChargeAmount4() {
        return chargeAmount4;
    }

    public void setChargeAmount4(Double chargeAmount4) {
        this.chargeAmount4 = chargeAmount4;
    }

    public String getUnitType5() {
        return unitType5;
    }

    public void setUnitType5(String unitType5) {
        this.unitType5 = unitType5;
    }

    public String getNumbers5() {
        return numbers5;
    }

    public void setNumbers5(String numbers5) {
        this.numbers5 = numbers5;
    }

    public String getChargeCodes5() {
        return chargeCodes5;
    }

    public void setChargeCodes5(String chargeCodes5) {
        this.chargeCodes5 = chargeCodes5;
    }

    public Double getChargeAmount5() {
        return chargeAmount5;
    }

    public void setChargeAmount5(Double chargeAmount5) {
        this.chargeAmount5 = chargeAmount5;
    }

    public String getUnitType6() {
        return unitType6;
    }

    public void setUnitType6(String unitType6) {
        this.unitType6 = unitType6;
    }

    public String getNumbers6() {
        return numbers6;
    }

    public void setNumbers6(String numbers6) {
        this.numbers6 = numbers6;
    }

    public String getChargeCodes6() {
        return chargeCodes6;
    }

    public void setChargeCodes6(String chargeCodes6) {
        this.chargeCodes6 = chargeCodes6;
    }

    public Double getChargeAmount6() {
        return chargeAmount6;
    }

    public void setChargeAmount6(Double chargeAmount6) {
        this.chargeAmount6 = chargeAmount6;
    }

    public String getUnitType7() {
        return unitType7;
    }

    public void setUnitType7(String unitType7) {
        this.unitType7 = unitType7;
    }

    public String getNumbers7() {
        return numbers7;
    }

    public void setNumbers7(String numbers7) {
        this.numbers7 = numbers7;
    }

    public String getChargeCodes7() {
        return chargeCodes7;
    }

    public void setChargeCodes7(String chargeCodes7) {
        this.chargeCodes7 = chargeCodes7;
    }

    public Double getChargeAmount7() {
        return chargeAmount7;
    }

    public void setChargeAmount7(Double chargeAmount7) {
        this.chargeAmount7 = chargeAmount7;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String[] getInclude() {
        return include;
    }

    public void setInclude(String[] include) {
        this.include = include;
    }

    public String[] getPrint() {
        return print;
    }

    public void setPrint(String[] print) {
        this.print = print;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String[] getChargeCd() {
        return chargeCd;
    }

    public void setChargeCd(String[] chargeCd) {
        this.chargeCd = chargeCd;
    }

    public String[] getCostType1() {
        return costType1;
    }

    public void setCostType1(String[] costType1) {
        this.costType1 = costType1;
    }

    public String[] getRetail() {
        return retail;
    }

    public void setRetail(String[] retail) {
        this.retail = retail;
    }

    public String[] getCTC() {
        return CTC;
    }

    public void setCTC(String[] ctc) {
        CTC = ctc;
    }

    public String[] getFtf() {
        return ftf;
    }

    public void setFtf(String[] ftf) {
        this.ftf = ftf;
    }

    public String[] getMinimum() {
        return minimum;
    }

    public void setMinimum(String[] minimum) {
        this.minimum = minimum;
    }

    public String getCheck1() {
        return check1;
    }

    public void setCheck1(String check1) {
        this.check1 = check1;
    }

    public String getIssuingTerminal() {
        return issuingTerminal;
    }

    public void setIssuingTerminal(String issuingTerminal) {
        this.issuingTerminal = issuingTerminal;
    }

    public String getDefaultAgent() {
        return defaultAgent;
    }

    public void setDefaultAgent(String defaultAgent) {
        this.defaultAgent = defaultAgent;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getNewClient() {
        return newClient;
    }

    public void setNewClient(String newClient) {
        this.newClient = newClient;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getRatesRemarks() {
        return ratesRemarks;
    }

    public void setRatesRemarks(String ratesRemarks) {
        this.ratesRemarks = ratesRemarks;
    }

    public String getCustomerName1() {
        return customerName1;
    }

    public void setCustomerName1(String customerName1) {
        this.customerName1 = customerName1;
    }

    public String getRoutedAgent() {
        return routedAgent;
    }

    public void setRoutedAgent(String routedAgent) {
        this.routedAgent = routedAgent;
    }

    public String getAdjestment() {
        return adjestment;
    }

    public void setAdjestment(String adjestment) {
        this.adjestment = adjestment;
    }

    public String getDoorOrigin() {
        return doorOrigin;
    }

    public void setDoorOrigin(String doorOrigin) {
        this.doorOrigin = doorOrigin;
    }

    public String getDoorDestination() {
        return doorDestination;
    }

    public void setDoorDestination(String doorDestination) {
        this.doorDestination = doorDestination;
    }

    public boolean isAesFilling() {
        return aesFilling;
    }

    public void setAesFilling(boolean aesFilling) {
        this.aesFilling = aesFilling;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.aesFilling = false;
    }

    public String getClientConsigneeCheck() {
        return clientConsigneeCheck;
    }

    public void setClientConsigneeCheck(String clientConsigneeCheck) {
        this.clientConsigneeCheck = clientConsigneeCheck;
    }

    public String getSpecialEqpmtUnit() {
        return specialEqpmtUnit;
    }

    public void setSpecialEqpmtUnit(String specialEqpmtUnit) {
        this.specialEqpmtUnit = specialEqpmtUnit;
    }

    public String getDirectConsignmntCheck() {
        return directConsignmntCheck;
    }

    public void setDirectConsignmntCheck(String directConsignmntCheck) {
        this.directConsignmntCheck = directConsignmntCheck;
    }

    public String getBulletRatesCheck() {
        return bulletRatesCheck;
    }

    public void setBulletRatesCheck(String bulletRatesCheck) {
        this.bulletRatesCheck = bulletRatesCheck;
    }

    public String getGreenDollarUseTrueCost() {
        return greenDollarUseTrueCost;
    }

    public void setGreenDollarUseTrueCost(String greenDollarUseTrueCost) {
        this.greenDollarUseTrueCost = greenDollarUseTrueCost;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getChassisCharge() {
        return chassisCharge;
    }

    public void setChassisCharge(String chassisCharge) {
        this.chassisCharge = chassisCharge;
    }
    
}
