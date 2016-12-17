package com.logiware.common.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Lakshmi Narayanan
 */
public class FileUploadForm extends BaseForm {

    private FormFile file;
    private String className;
    private String methodName;
    private String dataType;
    private String forward;
    private boolean setRequest;
    private HttpServletRequest request;
    private String customerNumber;

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public boolean isSetRequest() {
        return setRequest;
    }

    public void setSetRequest(boolean setRequest) {
        this.setRequest = setRequest;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

}
