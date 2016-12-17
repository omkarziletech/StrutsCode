package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class CostBean implements Serializable {

    private Integer fclCostTypeId;
    private Integer fclCostId;
    private String unitType;
    private String activeAmt;
    private Double ctcAmt;
    private Double ftfAmt;
    private Double minimumAmt;
    private Double ratAmount;
    private String standard;
    private String markup;
    private String costCode;
    private String[] unitTypes;
    private String costType;
    private String costId;
    private String setA;
    private String[] title;
    private String[] cost;
    private String[] markUp;
    private String unitTypeName;
    private String transitTime;
    private String accountNo;
    private String accountName;
    private String currency;
    private String retailFuture;
    private String futureRateA;
    private String effectiveDate;
    private String otherEffectiveDate;
    private String futureRateB;
    private String futureRateC;
    private String futureRateD;
    private String futureRateE;
    private String futureRateF;
    private String futureRateG;
    private String futureRateH;
    private String futureRateI;
    private String futureRateJ;
    private String futureRateK;
    private String futureRateL;
    private String futureRateM;
    private String futureRateN;
    private String futureRateO;
    private String futureRateP;
    private String futureRateQ;
    private String futureRateR;
    private String setB;
    private String setC;
    private String setD;
    private String setE;
    private String setF;
    private String chargeCodedesc;
    private String setG;
    private String setH;
    private String setI;
    private String setJ;
    private String setK;
    private String setL;
    private String setM;
    private String setN;
    private String setO;
    private String setP;
    private String setQ;
    private String setR;
    private String markUpA;
    private String markUpB;
    private String markUpC;
    private String markUpD;
    private String markUpE;
    private String markUpF;
    private String markUpG;
    private String markUpH;
    private String markUpI;
    private String markUpJ;
    private String markUpK;
    private String markUpL;
    private String markUpM;
    private String markUpN;
    private String markUpO;
    private String markUpP;
    private String markUpQ;
    private String markUpR;
    private String retailA;
    private String retailB;
    private String retailC;
    private String retailD;
    private String retailE;
    private String retailF;
    private String retailG;
    private String retailH;
    private String retailI;
    private String retailJ;
    private String retailK;
    private String retailL;
    private String retailM;
    private String retailN;
    private String retailO;
    private String retailP;
    private String retailQ;
    private String retailR;
    private String ssLineNumber;
    private String chargecode;
    private String amount;
    private String number;
    private String include;
    private String print;
	private String retail;
	private String CTC;
	private String ftf;
	private String minimum;
	private String otherinclude;
	private String otherprint;
	private Double totalCharges;
	private String chargeFlag;
	public String getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(String chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public Double getTotalCharges() {
		return totalCharges;
	}
	public void setTotalCharges(Double totalCharges) {
		this.totalCharges = totalCharges;
	}
	public String getCTC() {
		return CTC;
	}
	public void setCTC(String ctc) {
		CTC = ctc;
	}
	public String getFtf() {
		return ftf;
	}
	public void setFtf(String ftf) {
		this.ftf = ftf;
	}
	public String getMinimum() {
		return minimum;
	}
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	public String getRetail() {
		return retail;
	}
	public void setRetail(String retail) {
		this.retail = retail;
	}
	public String getInclude() {
		return include;
	}
	public void setInclude(String include) {
		this.include = include;
	}
	public String getPrint() {
		return print;
	}
	public void setPrint(String print) {
		this.print = print;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSsLineNumber() {
		return ssLineNumber;
	}
	public void setSsLineNumber(String ssLineNumber) {
		this.ssLineNumber = ssLineNumber;
	}
	public String getSetG() {
		return setG;
	}
	public void setSetG(String setG) {
		this.setG = setG;
	}
	public String getSetA() {
		return setA;
	}
	public void setSetA(String setA) {
		this.setA = setA;
	}
	public String getSetB() {
		return setB;
	}
	public void setSetB(String setB) {
		this.setB = setB;
	}
	public String getSetC() {
		return setC;
	}
	public void setSetC(String setC) {
		this.setC = setC;
	}
	public String getSetD() {
		return setD;
	}
	public void setSetD(String setD) {
		this.setD = setD;
	}
	public String getSetE() {
		return setE;
	}
	public void setSetE(String setE) {
		this.setE = setE;
	}
	public String getSetF() {
		return setF;
	}
	public void setSetF(String setF) {
		this.setF = setF;
	}
	public String getActiveAmt() {
		return activeAmt;
	}
	public void setActiveAmt(String activeAmt) {
		this.activeAmt = activeAmt;
	}
	public String getCostCode() {
		return costCode;
	}
	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}
	public String getCostId() {
		return costId;
	}
	public void setCostId(String costId) {
		this.costId = costId;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public Double getCtcAmt() {
		return ctcAmt;
	}
	public void setCtcAmt(Double ctcAmt) {
		this.ctcAmt = ctcAmt;
	}
	public Integer getFclCostId() {
		return fclCostId;
	}
	public void setFclCostId(Integer fclCostId) {
		this.fclCostId = fclCostId;
	}
	public Integer getFclCostTypeId() {
		return fclCostTypeId;
	}
	public void setFclCostTypeId(Integer fclCostTypeId) {
		this.fclCostTypeId = fclCostTypeId;
	}
	public Double getFtfAmt() {
		return ftfAmt;
	}
	public void setFtfAmt(Double ftfAmt) {
		this.ftfAmt = ftfAmt;
	}
	public String getMarkup() {
		return markup;
	}
	public void setMarkup(String markup) {
		this.markup = markup;
	}
	public Double getMinimumAmt() {
		return minimumAmt;
	}
	public void setMinimumAmt(Double minimumAmt) {
		this.minimumAmt = minimumAmt;
	}
	public Double getRatAmount() {
		return ratAmount;
	}
	public void setRatAmount(Double ratAmount) {
		this.ratAmount = ratAmount;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getChargecode() {
		return chargecode;
	}
	public void setChargecode(String chargecode) {
		this.chargecode = chargecode;
	}
	public String getSetH() {
		return setH;
	}
	public void setSetH(String setH) {
		this.setH = setH;
	}
	public String getSetI() {
		return setI;
	}
	public void setSetI(String setI) {
		this.setI = setI;
	}
	public String getSetJ() {
		return setJ;
	}
	public void setSetJ(String setJ) {
		this.setJ = setJ;
	}
	public String getSetK() {
		return setK;
	}
	public void setSetK(String setK) {
		this.setK = setK;
	}
	public String getSetL() {
		return setL;
	}
	public void setSetL(String setL) {
		this.setL = setL;
	}
	public String getSetM() {
		return setM;
	}
	public void setSetM(String setM) {
		this.setM = setM;
	}
	public String getSetN() {
		return setN;
	}
	public void setSetN(String setN) {
		this.setN = setN;
	}
	public String getSetO() {
		return setO;
	}
	public void setSetO(String setO) {
		this.setO = setO;
	}
	public String getSetP() {
		return setP;
	}
	public void setSetP(String setP) {
		this.setP = setP;
	}
	public String getSetQ() {
		return setQ;
	}
	public void setSetQ(String setQ) {
		this.setQ = setQ;
	}
	public String getSetR() {
		return setR;
	}
	public void setSetR(String setR) {
		this.setR = setR;
	}
	public String getMarkUpA() {
		return markUpA;
	}
	public void setMarkUpA(String markUpA) {
		this.markUpA = markUpA;
	}
	public String getMarkUpB() {
		return markUpB;
	}
	public void setMarkUpB(String markUpB) {
		this.markUpB = markUpB;
	}
	public String getMarkUpC() {
		return markUpC;
	}
	public void setMarkUpC(String markUpC) {
		this.markUpC = markUpC;
	}
	public String getMarkUpD() {
		return markUpD;
	}
	public void setMarkUpD(String markUpD) {
		this.markUpD = markUpD;
	}
	public String getMarkUpE() {
		return markUpE;
	}
	public void setMarkUpE(String markUpE) {
		this.markUpE = markUpE;
	}
	public String getMarkUpF() {
		return markUpF;
	}
	public void setMarkUpF(String markUpF) {
		this.markUpF = markUpF;
	}
	public String getMarkUpG() {
		return markUpG;
	}
	public void setMarkUpG(String markUpG) {
		this.markUpG = markUpG;
	}
	public String getMarkUpH() {
		return markUpH;
	}
	public void setMarkUpH(String markUpH) {
		this.markUpH = markUpH;
	}
	public String getMarkUpI() {
		return markUpI;
	}
	public void setMarkUpI(String markUpI) {
		this.markUpI = markUpI;
	}
	public String getMarkUpJ() {
		return markUpJ;
	}
	public void setMarkUpJ(String markUpJ) {
		this.markUpJ = markUpJ;
	}
	public String getMarkUpK() {
		return markUpK;
	}
	public void setMarkUpK(String markUpK) {
		this.markUpK = markUpK;
	}
	public String getMarkUpL() {
		return markUpL;
	}
	public void setMarkUpL(String markUpL) {
		this.markUpL = markUpL;
	}
	public String getMarkUpM() {
		return markUpM;
	}
	public void setMarkUpM(String markUpM) {
		this.markUpM = markUpM;
	}
	public String getMarkUpN() {
		return markUpN;
	}
	public void setMarkUpN(String markUpN) {
		this.markUpN = markUpN;
	}
	public String getMarkUpO() {
		return markUpO;
	}
	public void setMarkUpO(String markUpO) {
		this.markUpO = markUpO;
	}
	public String getMarkUpP() {
		return markUpP;
	}
	public void setMarkUpP(String markUpP) {
		this.markUpP = markUpP;
	}
	public String getMarkUpQ() {
		return markUpQ;
	}
	public void setMarkUpQ(String markUpQ) {
		this.markUpQ = markUpQ;
	}
	public String getMarkUpR() {
		return markUpR;
	}
	public void setMarkUpR(String markUpR) {
		this.markUpR = markUpR;
	}
	public String getOtherinclude() {
		return otherinclude;
	}
	public void setOtherinclude(String otherinclude) {
		this.otherinclude = otherinclude;
	}
	public String getOtherprint() {
		return otherprint;
	}
	public void setOtherprint(String otherprint) {
		this.otherprint = otherprint;
	}
	public String getRetailA() {
		return retailA;
	}
	public void setRetailA(String retailA) {
		this.retailA = retailA;
	}
	public String getRetailB() {
		return retailB;
	}
	public void setRetailB(String retailB) {
		this.retailB = retailB;
	}
	public String getRetailC() {
		return retailC;
	}
	public void setRetailC(String retailC) {
		this.retailC = retailC;
	}
	public String getRetailD() {
		return retailD;
	}
	public void setRetailD(String retailD) {
		this.retailD = retailD;
	}
	public String getRetailE() {
		return retailE;
	}
	public void setRetailE(String retailE) {
		this.retailE = retailE;
	}
	public String getRetailF() {
		return retailF;
	}
	public void setRetailF(String retailF) {
		this.retailF = retailF;
	}
	public String getRetailG() {
		return retailG;
	}
	public void setRetailG(String retailG) {
		this.retailG = retailG;
	}
	public String getRetailH() {
		return retailH;
	}
	public void setRetailH(String retailH) {
		this.retailH = retailH;
	}
	public String getRetailI() {
		return retailI;
	}
	public void setRetailI(String retailI) {
		this.retailI = retailI;
	}
	public String getRetailJ() {
		return retailJ;
	}
	public void setRetailJ(String retailJ) {
		this.retailJ = retailJ;
	}
	public String getRetailK() {
		return retailK;
	}
	public void setRetailK(String retailK) {
		this.retailK = retailK;
	}
	public String getRetailL() {
		return retailL;
	}
	public void setRetailL(String retailL) {
		this.retailL = retailL;
	}
	public String getRetailM() {
		return retailM;
	}
	public void setRetailM(String retailM) {
		this.retailM = retailM;
	}
	public String getRetailN() {
		return retailN;
	}
	public void setRetailN(String retailN) {
		this.retailN = retailN;
	}
	public String getRetailO() {
		return retailO;
	}
	public void setRetailO(String retailO) {
		this.retailO = retailO;
	}
	public String getRetailP() {
		return retailP;
	}
	public void setRetailP(String retailP) {
		this.retailP = retailP;
	}
	public String getRetailQ() {
		return retailQ;
	}
	public void setRetailQ(String retailQ) {
		this.retailQ = retailQ;
	}
	public String getRetailR() {
		return retailR;
	}
	public void setRetailR(String retailR) {
		this.retailR = retailR;
	}
	public String getChargeCodedesc() {
		return chargeCodedesc;
	}
	public void setChargeCodedesc(String chargeCodedesc) {
		this.chargeCodedesc = chargeCodedesc;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getFutureRateA() {
		return futureRateA;
	}
	public void setFutureRateA(String futureRateA) {
		this.futureRateA = futureRateA;
	}
	public String getFutureRateB() {
		return futureRateB;
	}
	public void setFutureRateB(String futureRateB) {
		this.futureRateB = futureRateB;
	}
	public String getFutureRateC() {
		return futureRateC;
	}
	public void setFutureRateC(String futureRateC) {
		this.futureRateC = futureRateC;
	}
	public String getFutureRateD() {
		return futureRateD;
	}
	public void setFutureRateD(String futureRateD) {
		this.futureRateD = futureRateD;
	}
	public String getFutureRateE() {
		return futureRateE;
	}
	public void setFutureRateE(String futureRateE) {
		this.futureRateE = futureRateE;
	}
	public String getFutureRateF() {
		return futureRateF;
	}
	public void setFutureRateF(String futureRateF) {
		this.futureRateF = futureRateF;
	}
	public String getFutureRateG() {
		return futureRateG;
	}
	public void setFutureRateG(String futureRateG) {
		this.futureRateG = futureRateG;
	}
	public String getFutureRateH() {
		return futureRateH;
	}
	public void setFutureRateH(String futureRateH) {
		this.futureRateH = futureRateH;
	}
	public String getFutureRateI() {
		return futureRateI;
	}
	public void setFutureRateI(String futureRateI) {
		this.futureRateI = futureRateI;
	}
	public String getFutureRateJ() {
		return futureRateJ;
	}
	public void setFutureRateJ(String futureRateJ) {
		this.futureRateJ = futureRateJ;
	}
	public String getFutureRateK() {
		return futureRateK;
	}
	public void setFutureRateK(String futureRateK) {
		this.futureRateK = futureRateK;
	}
	public String getFutureRateL() {
		return futureRateL;
	}
	public void setFutureRateL(String futureRateL) {
		this.futureRateL = futureRateL;
	}
	public String getFutureRateM() {
		return futureRateM;
	}
	public void setFutureRateM(String futureRateM) {
		this.futureRateM = futureRateM;
	}
	public String getFutureRateN() {
		return futureRateN;
	}
	public void setFutureRateN(String futureRateN) {
		this.futureRateN = futureRateN;
	}
	public String getFutureRateO() {
		return futureRateO;
	}
	public void setFutureRateO(String futureRateO) {
		this.futureRateO = futureRateO;
	}
	public String getFutureRateP() {
		return futureRateP;
	}
	public void setFutureRateP(String futureRateP) {
		this.futureRateP = futureRateP;
	}
	public String getFutureRateQ() {
		return futureRateQ;
	}
	public void setFutureRateQ(String futureRateQ) {
		this.futureRateQ = futureRateQ;
	}
	public String getFutureRateR() {
		return futureRateR;
	}
	public void setFutureRateR(String futureRateR) {
		this.futureRateR = futureRateR;
	}
	public String getRetailFuture() {
		return retailFuture;
	}
	public void setRetailFuture(String retailFuture) {
		this.retailFuture = retailFuture;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getOtherEffectiveDate() {
		return otherEffectiveDate;
	}
	public void setOtherEffectiveDate(String otherEffectiveDate) {
		this.otherEffectiveDate = otherEffectiveDate;
	}
	public String getUnitTypeName() {
		return unitTypeName;
	}
	public void setUnitTypeName(String unitTypeName) {
		this.unitTypeName = unitTypeName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
	public String[] getUnitTypes() {
		return unitTypes;
	}
	public void setUnitTypes(String[] unitTypes) {
		this.unitTypes = unitTypes;
	}
	public String getTransitTime() {
		return transitTime;
	}
	public void setTransitTime(String transitTime) {
		this.transitTime = transitTime;
	}
	public String[] getCost() {
		return cost;
	}
	public void setCost(String[] cost) {
		this.cost = cost;
	}
	public String[] getMarkUp() {
		return markUp;
	}
	public void setMarkUp(String[] markUp) {
		this.markUp = markUp;
	}
	
	
	
	
	 
    
}
