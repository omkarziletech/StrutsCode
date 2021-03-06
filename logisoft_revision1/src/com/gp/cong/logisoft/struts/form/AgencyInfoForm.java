/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.form;

import com.gp.cong.common.CommonUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 01-08-2008
 * 
 * XDoclet definition:
 * @struts.form name="agencyInfoForm"
 */
public class AgencyInfoForm extends ActionForm {
    /*
     * Generated Methods
     */

    private String agentAcountNo;
    private String agentName;
    private String consigneeAcctNo;
    private String consigneeName;
    private String defValue;
    private String buttonValue;
    private String defaultValue;
    private Integer index;
    private Integer schedNum;
    private String lclAgentLevelBrand;
    private boolean editUpdate;
    private String portOfDischarge;
    private String finalDeliveryTo;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getConsigneeAcctNo() {
        return consigneeAcctNo;
    }

    public void setConsigneeAcctNo(String consigneeAcctNo) {
        this.consigneeAcctNo = consigneeAcctNo;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getAgentAcountNo() {
        return agentAcountNo;
    }

    public void setAgentAcountNo(String agentAcountNo) {
        this.agentAcountNo = agentAcountNo;
    }

    public Integer getSchedNum() {
        return schedNum;
    }

    public void setSchedNum(Integer schedNum) {
        this.schedNum = schedNum;
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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getLclAgentLevelBrand() {
        return lclAgentLevelBrand;
    }

    public void setLclAgentLevelBrand(String lclAgentLevelBrand) {
        this.lclAgentLevelBrand = lclAgentLevelBrand;
    }

    public boolean isEditUpdate() {
        return editUpdate;
    }

    public void setEditUpdate(boolean editUpdate) {
        this.editUpdate = editUpdate;
    }

    public String getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(String portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public String getFinalDeliveryTo() {
        return finalDeliveryTo;
    }

    public void setFinalDeliveryTo(String finalDeliveryTo) {
        this.finalDeliveryTo = finalDeliveryTo;
    }
}
