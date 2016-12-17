/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 12-27-2007
 * 
 * XDoclet definition:
 * @struts.form name="lclPortsConfigForm"
 */
public class LclPortsConfigForm extends ActionForm {
    /*
     * Generated Methods
     */

    private String buttonValue;
    private String terminalNo;
    private String terminalName;
    private String lineManager;
    private String alternatePortName;
    private String transhipment;
    private String ftfFee;
    private String defaultRate;
    private String autoCalLclLoad;
    private String lclOceanbl;
    private String srvcOcean;
    private String calLclFaeUnitVoyage;
    private String spanishDescOnBl;
    private String printOnSailingSch;
    private String includeLclDocChargesBl;
    private String persEffectBl;
    private String onCarriage;
    private String insChargesLclBl;
    private String protectDefaultRoute;
    private String collectChargeOnLclBls;
    private String lclSplRemarksinEnglish;
    private String lclSplRemarksinSpanish;
    private String defaultDomesticRoutingInstructions;
    private String defaultPortOfDischarge;
    private String laneField;
    private String intrmRemarks;
    private String frmRemarks;
    private String blNumbering;
    private String asetup;
    private String asetupAcct;
    private String asetupAcctName;
    private String acAccountPickup;
    private String acAccountPickupName;
    private String domestic;
    private String printOFdollars;
    private String hazAllowed;
    private Integer ftfWeight;
    private Integer ftfMeasure;
    private Integer ftfMinimum;
    private boolean expressRelease;
    private boolean doNotExpressRelease;
    private boolean memoHouseBillofLading;
    private boolean originalsRequired;
    private boolean originalsReleasedAtDestination;
    private boolean forceAgentReleasedDrLoading;
    private Boolean printImpOnMetric;
    private boolean billCollectsFdAgent;

    public String getDefaultPortOfDischarge() {
        return defaultPortOfDischarge;
    }

    public void setDefaultPortOfDischarge(String defaultPortOfDischarge) {
        this.defaultPortOfDischarge = defaultPortOfDischarge;
    }

    public String getAlternatePortName() {
        return alternatePortName;
    }

    public void setAlternatePortName(String alternatePortName) {
        this.alternatePortName = alternatePortName;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getDefaultDomesticRoutingInstructions() {
        return defaultDomesticRoutingInstructions;
    }

    public void setDefaultDomesticRoutingInstructions(
            String defaultDomesticRoutingInstructions) {
        this.defaultDomesticRoutingInstructions = defaultDomesticRoutingInstructions;
    }

    public String getFtfFee() {
        return ftfFee;
    }

    public void setFtfFee(String ftfFee) {
        this.ftfFee = ftfFee;
    }

    public String getLclSplRemarksinEnglish() {
        return lclSplRemarksinEnglish;
    }

    public void setLclSplRemarksinEnglish(String lclSplRemarksinEnglish) {
        this.lclSplRemarksinEnglish = lclSplRemarksinEnglish;
    }

    public String getLclSplRemarksinSpanish() {
        return lclSplRemarksinSpanish;
    }

    public void setLclSplRemarksinSpanish(String lclSplRemarksinSpanish) {
        this.lclSplRemarksinSpanish = lclSplRemarksinSpanish;
    }

    public String getLineManager() {
        return lineManager;
    }

    public void setLineManager(String lineManager) {
        this.lineManager = lineManager;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getTranshipment() {
        return transhipment;
    }

    public void setTranshipment(String transhipment) {
        this.transhipment = transhipment;
    }

    /**
     * Method validate
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Method reset
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // TODO Auto-generated method stub
    }

    public String getAutoCalLclLoad() {
        return autoCalLclLoad;
    }

    public void setAutoCalLclLoad(String autoCalLclLoad) {
        this.autoCalLclLoad = autoCalLclLoad;
    }

    public String getCalLclFaeUnitVoyage() {
        return calLclFaeUnitVoyage;
    }

    public void setCalLclFaeUnitVoyage(String calLclFaeUnitVoyage) {
        this.calLclFaeUnitVoyage = calLclFaeUnitVoyage;
    }

    public String getCollectChargeOnLclBls() {
        return collectChargeOnLclBls;
    }

    public void setCollectChargeOnLclBls(String collectChargeOnLclBls) {
        this.collectChargeOnLclBls = collectChargeOnLclBls;
    }

    public String getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(String defaultRate) {
        this.defaultRate = defaultRate;
    }

    public String getIncludeLclDocChargesBl() {
        return includeLclDocChargesBl;
    }

    public void setIncludeLclDocChargesBl(String includeLclDocChargesBl) {
        this.includeLclDocChargesBl = includeLclDocChargesBl;
    }

    public String getInsChargesLclBl() {
        return insChargesLclBl;
    }

    public void setInsChargesLclBl(String insChargesLclBl) {
        this.insChargesLclBl = insChargesLclBl;
    }

    public String getLclOceanbl() {
        return lclOceanbl;
    }

    public void setLclOceanbl(String lclOceanbl) {
        this.lclOceanbl = lclOceanbl;
    }

    public String getOnCarriage() {
        return onCarriage;
    }

    public void setOnCarriage(String onCarriage) {
        this.onCarriage = onCarriage;
    }

    public String getPersEffectBl() {
        return persEffectBl;
    }

    public void setPersEffectBl(String persEffectBl) {
        this.persEffectBl = persEffectBl;
    }

    public String getPrintOnSailingSch() {
        return printOnSailingSch;
    }

    public void setPrintOnSailingSch(String printOnSailingSch) {
        this.printOnSailingSch = printOnSailingSch;
    }

    public String getProtectDefaultRoute() {
        return protectDefaultRoute;
    }

    public void setProtectDefaultRoute(String protectDefaultRoute) {
        this.protectDefaultRoute = protectDefaultRoute;
    }

    public String getSpanishDescOnBl() {
        return spanishDescOnBl;
    }

    public void setSpanishDescOnBl(String spanishDescOnBl) {
        this.spanishDescOnBl = spanishDescOnBl;
    }

    public String getSrvcOcean() {
        return srvcOcean;
    }

    public void setSrvcOcean(String srvcOcean) {
        this.srvcOcean = srvcOcean;
    }

    public String getLaneField() {
        return laneField;
    }

    public void setLaneField(String laneField) {
        this.laneField = laneField;
    }

    public String getFrmRemarks() {
        return frmRemarks;
    }

    public void setFrmRemarks(String frmRemarks) {
        this.frmRemarks = frmRemarks;
    }

    public String getIntrmRemarks() {
        return intrmRemarks;
    }

    public void setIntrmRemarks(String intrmRemarks) {
        this.intrmRemarks = intrmRemarks;
    }

    public boolean isExpressRelease() {
        return expressRelease;
    }

    public void setExpressRelease(boolean expressRelease) {
        this.expressRelease = expressRelease;
    }

    public boolean isDoNotExpressRelease() {
        return doNotExpressRelease;
    }

    public void setDoNotExpressRelease(boolean doNotExpressRelease) {
        this.doNotExpressRelease = doNotExpressRelease;
    }
    
    public boolean isOriginalsReleasedAtDestination() {
        return originalsReleasedAtDestination;
    }

    public void setOriginalsReleasedAtDestination(boolean originalsReleasedAtDestination) {
        this.originalsReleasedAtDestination = originalsReleasedAtDestination;
    }

    public boolean isOriginalsRequired() {
        return originalsRequired;
    }

    public void setOriginalsRequired(boolean originalsRequired) {
        this.originalsRequired = originalsRequired;
    }

    public boolean isMemoHouseBillofLading() {
        return memoHouseBillofLading;
    }

    public void setMemoHouseBillofLading(boolean memoHouseBillofLading) {
        this.memoHouseBillofLading = memoHouseBillofLading;
    }

    public String getBlNumbering() {
        return blNumbering;
    }

    public void setBlNumbering(String blNumbering) {
        this.blNumbering = blNumbering;
    }

    public String getAsetup() {
        return asetup;
    }

    public void setAsetup(String asetup) {
        this.asetup = asetup;
    }

    public String getAsetupAcct() {
        return asetupAcct;
    }

    public void setAsetupAcct(String asetupAcct) {
        this.asetupAcct = asetupAcct;
    }

    public String getAcAccountPickup() {
        return acAccountPickup;
    }

    public void setAcAccountPickup(String acAccountPickup) {
        this.acAccountPickup = acAccountPickup;
    }

    public String getDomestic() {
        return domestic;
    }

    public void setDomestic(String domestic) {
        this.domestic = domestic;
    }

    public String getHazAllowed() {
        return hazAllowed;
    }

    public void setHazAllowed(String hazAllowed) {
        this.hazAllowed = hazAllowed;
    }

    public Integer getFtfWeight() {
        return ftfWeight;
    }

    public void setFtfWeight(Integer ftfWeight) {
        this.ftfWeight = ftfWeight;
    }

    public Integer getFtfMeasure() {
        return ftfMeasure;
    }

    public void setFtfMeasure(Integer ftfMeasure) {
        this.ftfMeasure = ftfMeasure;
    }

    public Integer getFtfMinimum() {
        return ftfMinimum;
    }

    public void setFtfMinimum(Integer ftfMinimum) {
        this.ftfMinimum = ftfMinimum;
    }

    public String getAsetupAcctName() {
        return asetupAcctName;
    }

    public void setAsetupAcctName(String asetupAcctName) {
        this.asetupAcctName = asetupAcctName;
    }

    public String getAcAccountPickupName() {
        return acAccountPickupName;
    }

    public void setAcAccountPickupName(String acAccountPickupName) {
        this.acAccountPickupName = acAccountPickupName;
    }

    public boolean isForceAgentReleasedDrLoading() {
        return forceAgentReleasedDrLoading;
    }

    public void setForceAgentReleasedDrLoading(boolean forceAgentReleasedDrLoading) {
        this.forceAgentReleasedDrLoading = forceAgentReleasedDrLoading;
    }

    public Boolean getPrintImpOnMetric() {
        return printImpOnMetric;
    }

    public void setPrintImpOnMetric(Boolean printImpOnMetric) {
        this.printImpOnMetric = printImpOnMetric;
    }

    public String getPrintOFdollars() {
        return printOFdollars;
    }

    public void setPrintOFdollars(String printOFdollars) {
        this.printOFdollars = printOFdollars;
    }

    public boolean isBillCollectsFdAgent() {
        return billCollectsFdAgent;
    }

    public void setBillCollectsFdAgent(boolean billCollectsFdAgent) {
        this.billCollectsFdAgent = billCollectsFdAgent;
    }
}