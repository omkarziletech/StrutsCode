/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.reports.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mei
 */
public class FclRatesReportDTO {

    private String regionOrigin;
    private String regionDestination;
    private String originName;
    private String originSchnum;
    private String polSchnum;
    private String podSchnum;
    private String fdName;
    private String fdSchnum;
    private String carrierName;
    private String ssLineNum;
    private String costCodeDesc;
    private String a20Amt;
    private String b40Amt;
    private String c40hcAmt;
    private String d45Amt;
    private String e48Amt;
    private String f40norAmt;
    private String g45102Amt;
    private String hazamtAmt;
    private String containerSize;
    private List<FclRatesReportDTO> chargeList;
    private Map<String, FclRatesReportDTO> map = new HashMap();
    private String transitDays;
    private String remarks;
    private String mrkupa;
    private String mrkupb;
    private String mrkupc;
    private String mrkupd;
    private String mrkupe;
    private String mrkupf;
    private String mrkupg;

    public String getA20Amt() {
        return a20Amt;
    }

    public void setA20Amt(String a20Amt) {
        this.a20Amt = a20Amt;
    }

    public String getB40Amt() {
        return b40Amt;
    }

    public void setB40Amt(String b40Amt) {
        this.b40Amt = b40Amt;
    }

    public String getC40hcAmt() {
        return c40hcAmt;
    }

    public void setC40hcAmt(String c40hcAmt) {
        this.c40hcAmt = c40hcAmt;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCostCodeDesc() {
        return costCodeDesc;
    }

    public void setCostCodeDesc(String costCodeDesc) {
        this.costCodeDesc = costCodeDesc;
    }

    public String getD45Amt() {
        return d45Amt;
    }

    public void setD45Amt(String d45Amt) {
        this.d45Amt = d45Amt;
    }

    public String getE48Amt() {
        return e48Amt;
    }

    public void setE48Amt(String e48Amt) {
        this.e48Amt = e48Amt;
    }

    public String getF40norAmt() {
        return f40norAmt;
    }

    public void setF40norAmt(String f40norAmt) {
        this.f40norAmt = f40norAmt;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public String getFdSchnum() {
        return fdSchnum;
    }

    public void setFdSchnum(String fdSchnum) {
        this.fdSchnum = fdSchnum;
    }

    public String getG45102Amt() {
        return g45102Amt;
    }

    public void setG45102Amt(String g45102Amt) {
        this.g45102Amt = g45102Amt;
    }

    public String getHazamtAmt() {
        return hazamtAmt;
    }

    public void setHazamtAmt(String hazamtAmt) {
        this.hazamtAmt = hazamtAmt;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginSchnum() {
        return originSchnum;
    }

    public void setOriginSchnum(String originSchnum) {
        this.originSchnum = originSchnum;
    }

    public String getPodSchnum() {
        return podSchnum;
    }

    public void setPodSchnum(String podSchnum) {
        this.podSchnum = podSchnum;
    }

    public String getPolSchnum() {
        return polSchnum;
    }

    public void setPolSchnum(String polSchnum) {
        this.polSchnum = polSchnum;
    }

    public String getRegionDestination() {
        return regionDestination;
    }

    public void setRegionDestination(String regionDestination) {
        this.regionDestination = regionDestination;
    }

    public String getRegionOrigin() {
        return regionOrigin;
    }

    public void setRegionOrigin(String regionOrigin) {
        this.regionOrigin = regionOrigin;
    }

    public String getSsLineNum() {
        return ssLineNum;
    }

    public void setSsLineNum(String ssLineNum) {
        this.ssLineNum = ssLineNum;
    }

    public String getContainerSize() {
        return containerSize;
    }

    public void setContainerSize(String containerSize) {
        this.containerSize = containerSize;
    }

    public List<FclRatesReportDTO> getChargeList() {
        if (chargeList == null) {
            chargeList = new ArrayList<FclRatesReportDTO>();
        }
        return chargeList;
    }

    public FclRatesReportDTO addCharge(FclRatesReportDTO dto) {
        getChargeList().add(dto);
        return dto;
    }

    public void setChargeList(List<FclRatesReportDTO> chargeList) {
        this.chargeList = chargeList;
    }

    public Map<String, FclRatesReportDTO> getMap() {
        return map;
    }

    public void setMap(Map<String, FclRatesReportDTO> map) {
        this.map = map;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransitDays() {
        return transitDays;
    }

    public void setTransitDays(String transitDays) {
        this.transitDays = transitDays;
    }

    public String getMrkupa() {
        return mrkupa;
    }

    public void setMrkupa(String mrkupa) {
        this.mrkupa = mrkupa;
    }

    public String getMrkupb() {
        return mrkupb;
    }

    public void setMrkupb(String mrkupb) {
        this.mrkupb = mrkupb;
    }

    public String getMrkupc() {
        return mrkupc;
    }

    public void setMrkupc(String mrkupc) {
        this.mrkupc = mrkupc;
    }

    public String getMrkupd() {
        return mrkupd;
    }

    public void setMrkupd(String mrkupd) {
        this.mrkupd = mrkupd;
    }

    public String getMrkupe() {
        return mrkupe;
    }

    public void setMrkupe(String mrkupe) {
        this.mrkupe = mrkupe;
    }

    public String getMrkupf() {
        return mrkupf;
    }

    public void setMrkupf(String mrkupf) {
        this.mrkupf = mrkupf;
    }

    public String getMrkupg() {
        return mrkupg;
    }

    public void setMrkupg(String mrkupg) {
        this.mrkupg = mrkupg;
    }
}
