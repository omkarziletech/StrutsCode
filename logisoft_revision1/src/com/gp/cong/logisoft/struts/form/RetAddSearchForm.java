/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.form;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Vinay
 */
public class RetAddSearchForm extends ActionForm {

    private String originId;
    private String code;
    private String origTerm;
    private String destination;
    private String userName;
    private String action;
    private String email;
    private Integer retAddId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toUpperCase();
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrigTerm() {
        return origTerm;
    }

    public void setOrigTerm(String origTerm) {
        this.origTerm = origTerm;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsrnam(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getRetAddId() {
        return retAddId;
    }

    public void setRetAddId(Integer retAddId) {
        this.retAddId = retAddId;
    }
}
