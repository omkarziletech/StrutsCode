/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logiware.form;

/**
 *
 * @author Lakshminarayanan
 */
public class OnlineUsersForm extends org.apache.struts.action.ActionForm {
    private static final long serialVersionUID = 2735084847097605L;
    private String buttonValue;
    private Integer userId;

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}
