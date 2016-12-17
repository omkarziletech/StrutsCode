/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.form.lclDataReference;

import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;
import java.util.Date;

/**
 *
 * @author Meiyazhakan
 */
public class LclUnitTypeForm extends LogiwareActionForm {

    private Long id;
    private String description;
    private String eliteType;
    private String shortDesc;
    private double interiorLengthImperial;
    private double interiorLengthMetric;
    private double interiorWidthImperial;
    private double interiorWidthMetric;
    private double interiorHeightImperial;
    private double interiorHeightMetric;
    private double doorHeightImperial;
    private double doorHeightMetric;
    private double doorWidthImperial;
    private double doorWidthMetric;
    private double grossWeightImperial;
    private double grossWeightMetric;
    private double tareWeightImperial;
    private double tareWeightMetric;
    private double volumeImperial;
    private double volumeMetric;
    private double targetVolumeImperial;
    private double targetVolumeMetric;
    private boolean refrigerated;
    private String remarks;
    private boolean enabledLclAir;
    private boolean enabledLclExports;
    private boolean enabledLclImports;
    private Date enteredDatetime;
    private Date modifiedDatetime;
    private int enteredByUserId;
    private int modifiedByUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDoorHeightImperial() {
        return doorHeightImperial;
    }

    public void setDoorHeightImperial(double doorHeightImperial) {
        this.doorHeightImperial = doorHeightImperial;
    }

    public double getDoorHeightMetric() {
        return doorHeightMetric;
    }

    public void setDoorHeightMetric(double doorHeightMetric) {
        this.doorHeightMetric = doorHeightMetric;
    }

    public double getDoorWidthImperial() {
        return doorWidthImperial;
    }

    public void setDoorWidthImperial(double doorWidthImperial) {
        this.doorWidthImperial = doorWidthImperial;
    }

    public double getDoorWidthMetric() {
        return doorWidthMetric;
    }

    public void setDoorWidthMetric(double doorWidthMetric) {
        this.doorWidthMetric = doorWidthMetric;
    }

    public String getEliteType() {
        return eliteType;
    }

    public void setEliteType(String eliteType) {
        this.eliteType = eliteType;
    }

    public boolean isEnabledLclAir() {
        return enabledLclAir;
    }

    public void setEnabledLclAir(boolean enabledLclAir) {
        this.enabledLclAir = enabledLclAir;
    }

    public boolean isEnabledLclExports() {
        return enabledLclExports;
    }

    public void setEnabledLclExports(boolean enabledLclExports) {
        this.enabledLclExports = enabledLclExports;
    }

    public boolean isEnabledLclImports() {
        return enabledLclImports;
    }

    public void setEnabledLclImports(boolean enabledLclImports) {
        this.enabledLclImports = enabledLclImports;
    }

    public double getGrossWeightImperial() {
        return grossWeightImperial;
    }

    public void setGrossWeightImperial(double grossWeightImperial) {
        this.grossWeightImperial = grossWeightImperial;
    }

    public double getGrossWeightMetric() {
        return grossWeightMetric;
    }

    public void setGrossWeightMetric(double grossWeightMetric) {
        this.grossWeightMetric = grossWeightMetric;
    }

    public double getInteriorHeightImperial() {
        return interiorHeightImperial;
    }

    public void setInteriorHeightImperial(double interiorHeightImperial) {
        this.interiorHeightImperial = interiorHeightImperial;
    }

    public double getInteriorHeightMetric() {
        return interiorHeightMetric;
    }

    public void setInteriorHeightMetric(double interiorHeightMetric) {
        this.interiorHeightMetric = interiorHeightMetric;
    }

    public double getInteriorLengthImperial() {
        return interiorLengthImperial;
    }

    public void setInteriorLengthImperial(double interiorLengthImperial) {
        this.interiorLengthImperial = interiorLengthImperial;
    }

    public double getInteriorLengthMetric() {
        return interiorLengthMetric;
    }

    public void setInteriorLengthMetric(double interiorLengthMetric) {
        this.interiorLengthMetric = interiorLengthMetric;
    }

    public double getInteriorWidthImperial() {
        return interiorWidthImperial;
    }

    public void setInteriorWidthImperial(double interiorWidthImperial) {
        this.interiorWidthImperial = interiorWidthImperial;
    }

    public double getInteriorWidthMetric() {
        return interiorWidthMetric;
    }

    public void setInteriorWidthMetric(double interiorWidthMetric) {
        this.interiorWidthMetric = interiorWidthMetric;
    }

    public boolean isRefrigerated() {
        return refrigerated;
    }

    public void setRefrigerated(boolean refrigerated) {
        this.refrigerated = refrigerated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getTareWeightImperial() {
        return tareWeightImperial;
    }

    public void setTareWeightImperial(double tareWeightImperial) {
        this.tareWeightImperial = tareWeightImperial;
    }

    public double getTareWeightMetric() {
        return tareWeightMetric;
    }

    public void setTareWeightMetric(double tareWeightMetric) {
        this.tareWeightMetric = tareWeightMetric;
    }

    public double getTargetVolumeImperial() {
        return targetVolumeImperial;
    }

    public void setTargetVolumeImperial(double targetVolumeImperial) {
        this.targetVolumeImperial = targetVolumeImperial;
    }

    public double getTargetVolumeMetric() {
        return targetVolumeMetric;
    }

    public void setTargetVolumeMetric(double targetVolumeMetric) {
        this.targetVolumeMetric = targetVolumeMetric;
    }

    public double getVolumeImperial() {
        return volumeImperial;
    }

    public void setVolumeImperial(double volumeImperial) {
        this.volumeImperial = volumeImperial;
    }

    public double getVolumeMetric() {
        return volumeMetric;
    }

    public void setVolumeMetric(double volumeMetric) {
        this.volumeMetric = volumeMetric;
    }

    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public int getEnteredByUserId() {
        return enteredByUserId;
    }

    public void setEnteredByUserId(int enteredByUserId) {
        this.enteredByUserId = enteredByUserId;
    }

    public int getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(int modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

}
