/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.form;
/**
 *
 * @author Lakshminarayanan
 */
public class SystemRulesForm extends org.apache.struts.action.ActionForm {
    private static final long serialVersionUID = 2735084847737097602L;
    private String[] ruleId;
    private String[] ruleCode;
    private String[] ruleName;
    private String fieldType;
    private String buttonValue;

    public String[] getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String[] ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String[] getRuleId() {
        return ruleId;
    }

    public void setRuleId(String[] ruleId) {
        this.ruleId = ruleId;
    }

    public String[] getRuleName() {
        return ruleName;
    }

    public void setRuleName(String[] ruleName) {
        this.ruleName = ruleName;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
   
}
