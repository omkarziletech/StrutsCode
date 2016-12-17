/**
 * 
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Yogesh
 *
 */
public class LCLPortConfiguration implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private RefTerminalTemp trmnum;
    private String lineManager;
    private Integer shedulenumber;
    private Integer drAbbr;
    private String altPortName;
    private String transhipment;
    private Double ftfFee;
    private String routingInstr;
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
    private String lclSplRemarksInEnglish;
    private String lclSplRemarksInSpanish;
    private String defaultPortOfDischarge;
    private String laneField;
    private Set associatedPort;
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
    private String defaultTrashipmentUnloc;
    private String defaultPortUnloc;
    private boolean printInvoiceValue;
    private boolean lockport;
    private boolean billCollectsFdAgent;
    

    public String getLaneField() {
        return laneField;
    }

    public void setLaneField(String laneField) {
        this.laneField = laneField;
    }

    public String getDefaultPortOfDischarge() {
        return defaultPortOfDischarge;
    }

    public void setDefaultPortOfDischarge(String defaultPortOfDischarge) {
        this.defaultPortOfDischarge = defaultPortOfDischarge;
    }

    public String getAltPortName() {
        return altPortName;
    }

    public void setAltPortName(String altPortName) {
        this.altPortName = altPortName;
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

    public Integer getDrAbbr() {
        return drAbbr;
    }

    public void setDrAbbr(Integer drAbbr) {
        this.drAbbr = drAbbr;
    }

    public Double getFtfFee() {
        return ftfFee;
    }

    public void setFtfFee(Double ftfFee) {
        this.ftfFee = ftfFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getLclSplRemarksInEnglish() {
        return lclSplRemarksInEnglish;
    }

    public void setLclSplRemarksInEnglish(String lclSplRemarksInEnglish) {
        this.lclSplRemarksInEnglish = lclSplRemarksInEnglish;
    }

    public String getLclSplRemarksInSpanish() {
        return lclSplRemarksInSpanish;
    }

    public void setLclSplRemarksInSpanish(String lclSplRemarksInSpanish) {
        this.lclSplRemarksInSpanish = lclSplRemarksInSpanish;
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

    public String getRoutingInstr() {
        return routingInstr;
    }

    public void setRoutingInstr(String routingInstr) {
        this.routingInstr = routingInstr;
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

    public String getLineManager() {
        return lineManager;
    }

    public void setLineManager(String lineManager) {
        this.lineManager = lineManager;
    }

    public RefTerminalTemp getTrmnum() {
        return trmnum;
    }

    public void setTrmnum(RefTerminalTemp trmnum) {
        this.trmnum = trmnum;
    }

    public String getProtectDefaultRoute() {
        return protectDefaultRoute;
    }

    public void setProtectDefaultRoute(String protectDefaultRoute) {
        this.protectDefaultRoute = protectDefaultRoute;
    }

    public Set getAssociatedPort() {
        return associatedPort;
    }

    public void setAssociatedPort(Set associatedPort) {
        this.associatedPort = associatedPort;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer getShedulenumber() {
        return shedulenumber;
    }

    public void setShedulenumber(Integer shedulenumber) {
        this.shedulenumber = shedulenumber;
    }

    public String getTranshipment() {
        return transhipment;
    }

    public void setTranshipment(String transhipment) {
        this.transhipment = transhipment;
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

    public boolean isDoNotExpressRelease() {
        return doNotExpressRelease;
    }

    public void setDoNotExpressRelease(boolean doNotExpressRelease) {
        this.doNotExpressRelease = doNotExpressRelease;
    }

    public boolean isMemoHouseBillofLading() {
        return memoHouseBillofLading;
    }

    public void setMemoHouseBillofLading(boolean memoHouseBillofLading) {
        this.memoHouseBillofLading = memoHouseBillofLading;
    }

    public String getPrintOFdollars() {
        return printOFdollars;
    }

    public void setPrintOFdollars(String printOFdollars) {
        this.printOFdollars = printOFdollars;
    }

    public void setDefaultTrashipmentUnloc(String defaultTrashipmentUnloc) {
        this.defaultTrashipmentUnloc = defaultTrashipmentUnloc;
    }

    public void setDefaultPortUnloc(String defaultPortUnloc) {
        this.defaultPortUnloc = defaultPortUnloc;
    }

    public String getDefaultTrashipmentUnloc() {
        return defaultTrashipmentUnloc;
    }

    public String getDefaultPortUnloc() {
        return defaultPortUnloc;
    }

    public boolean isPrintInvoiceValue() {
        return printInvoiceValue;
    }

    public void setPrintInvoiceValue(boolean printInvoiceValue) {
        this.printInvoiceValue = printInvoiceValue;
    }

    public boolean isLockport() {
        return lockport;
    }

    public void setLockport(boolean lockport) {
        this.lockport = lockport;
    }

    public boolean isBillCollectsFdAgent() {
        return billCollectsFdAgent;
    }

    public void setBillCollectsFdAgent(boolean billCollectsFdAgent) {
        this.billCollectsFdAgent = billCollectsFdAgent;
    }
}
