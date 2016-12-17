/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

/**
 *
 * @author meiyazhakan.r
 */
public class ImportRoutingForm extends LogiwareActionForm {

    private String shipperHours;
    private String readyNote;
    private String specialInstructions;
    private String cutOff;
    private String fileId;
    private String fileNo;
    private String methodName;
    private String readyDate;
    private String termsOfSale;

    public String getTermsOfSale() {
        return termsOfSale;
    }

    public void setTermsOfSale(String termsOfSale) {
        this.termsOfSale = termsOfSale;
    }

    public String getReadyDate() {
        return readyDate;
    }

    public void setReadyDate(String readyDate) {
        this.readyDate = readyDate;
    }

    public String getShipperHours() {
        return shipperHours;
    }

    public void setShipperHours(String shipperHours) {
        this.shipperHours = shipperHours;
    }

    public String getReadyNote() {
        return readyNote;
    }

    public void setReadyNote(String readyNote) {
        this.readyNote = readyNote;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getCutOff() {
        return cutOff;
    }

    public void setCutOff(String cutOff) {
        this.cutOff = cutOff;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
