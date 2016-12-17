/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;

/**
 *
 * @author Thamizh
 */
public class HsCodeForm extends LogiwareActionForm {

    private String hsCode;
    private Long id;
    private String refType;
    private String hsCodeFileNo;
    private String methodName;
    private String fileNumber;
    private Lcl3pRefNo lcl3pRefNo;

    public HsCodeForm() {
        if (null == lcl3pRefNo) {
            lcl3pRefNo = new Lcl3pRefNo();
        }

        if (null == lcl3pRefNo.getLclFileNumber()) {
            lcl3pRefNo.setLclFileNumber(new LclFileNumber());
        }
    }

    public Long getId() {
        return lcl3pRefNo.getId();
    }

    public void setId(Long id) {
        lcl3pRefNo.setId(id);
    }

    public Long getFileNumberId() {
        return lcl3pRefNo.getLclFileNumber().getId();
    }

    public void setFileNumberId(Long fileNumberId) {
        lcl3pRefNo.getLclFileNumber().setId(fileNumberId);
    }
    public String getHsCode() {
        return this.lcl3pRefNo.getReference();
    }

    public void setHsCode(String hsCode) {
        this.lcl3pRefNo.setReference(hsCode);
    }

    public String getHsCodeFileNo() {
        return hsCodeFileNo;
    }

    public void setHsCodeFileNo(String hsCodeFileNo) {
        this.hsCodeFileNo = hsCodeFileNo;
    }

    public String getRefType() {
        return this.lcl3pRefNo.getType();
    }

    public void setRefType(String refType) {
        this.lcl3pRefNo.setType(refType);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Lcl3pRefNo getLcl3pRefNo() {
        return lcl3pRefNo;
    }

    public void setLcl3pRefNo(Lcl3pRefNo lcl3pRefNo) {
        this.lcl3pRefNo = lcl3pRefNo;
    }

    public String getFileNumber() {
        return lcl3pRefNo.getLclFileNumber().getFileNumber();
    }

    public void setFileNumber(String fileNumber) {
        lcl3pRefNo.getLclFileNumber().setFileNumber(fileNumber);
    }

}
