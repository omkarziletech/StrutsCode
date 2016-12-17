/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.ratemangement.form;

import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;

/**
 *
 * @author logiware
 */
public class LclRatesForm extends LogiwareActionForm {

    private String methodName;
    private String comCode;
    private String comDesc;
    private String orgTrm;
    private String destTrm;
    private String uom;
    private String weight;
    private String eciportcode;
    private String trmnum;
    private String measure;
    private String prtMeasure;
    private String prtWeight;
    private String flatRate;
    private String minimum;
    private String chgCode;
    private String chgType;
    private String minChg;
    private String chargesCode;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getComDesc() {
        return comDesc;
    }

    public void setComDesc(String comDesc) {
        this.comDesc = comDesc;
    }

    public String getDestTrm() {
        return destTrm;
    }

    public void setDestTrm(String destTrm) {
        this.destTrm = destTrm;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getOrgTrm() {
        return orgTrm;
    }

    public void setOrgTrm(String orgTrm) {
        this.orgTrm = orgTrm;
    }

    public String getUom() {
        if(CommonUtils.isEmpty(uom)){
            return "M";
        }
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEciportcode() {
        return eciportcode;
    }

    public void setEciportcode(String eciportcode) {
        this.eciportcode = eciportcode;
    }

    public String getTrmnum() {
        return trmnum;
    }

    public void setTrmnum(String trmnum) {
        this.trmnum = trmnum;
    }

    public String getChgCode() {
        return chgCode;
    }

    public void setChgCode(String chgCode) {
        this.chgCode = chgCode;
    }

    public String getChgType() {
        return chgType;
    }

    public void setChgType(String chgType) {
        this.chgType = chgType;
    }

    public String getMinChg() {
        return minChg;
    }

    public void setMinChg(String minChg) {
        this.minChg = minChg;
    }

    public String getChargesCode() {
        return chargesCode;
    }

    public void setChargesCode(String chargesCode) {
        this.chargesCode = chargesCode;
    }

    public String getPrtMeasure() {
        return prtMeasure;
    }

    public void setPrtMeasure(String prtMeasure) {
        this.prtMeasure = prtMeasure;
    }

    public String getPrtWeight() {
        return prtWeight;
    }

    public void setPrtWeight(String prtWeight) {
        this.prtWeight = prtWeight;
    }

    public String getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(String flatRate) {
        this.flatRate = flatRate;
    }

}
