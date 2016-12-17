package com.gp.cvst.logisoft.struts.form.lcl;

public class LclTerminatePopupForm extends LogiwareActionForm {

    private String fileId;
    private String fileNumber;
    private String methodName;
    private String terminate;
    private String originCityName;
    private String destination;
    private String moduleName;
    private String consoTerminate;
    
    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getTerminate() {
        return terminate;
    }

    public void setTerminate(String terminate) {

        this.terminate = terminate;
    }

    public String getOriginCityName() {
        return originCityName;
    }

    public void setOriginCityName(String originCityName) {
        this.originCityName = originCityName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getConsoTerminate() {
        return consoTerminate;
    }

    public void setConsoTerminate(String consoTerminate) {
        this.consoTerminate = consoTerminate;
    }

}
