package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class TerminalBean implements Serializable {
    
    private String terminalId;
    private String terminalName;
    private String city;
    private String terminalType;
    private String contactName;
    private String contactEmail;
    private String chargeCode;
    private String brlChargeCode;
    private String ovr10kChgCode;
    private String ovr20kChgCode;
    private String docChargeCode;
    private String match;
    private String userName;
    private String managerName;
    private String managerEmail;
    private Integer terminalManagerId;
    private Integer userId;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getBrlChargeCode() {
        return brlChargeCode;
    }

    public void setBrlChargeCode(String brlChargeCode) {
        this.brlChargeCode = brlChargeCode;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getDocChargeCode() {
        return docChargeCode;
    }

    public void setDocChargeCode(String docChargeCode) {
        this.docChargeCode = docChargeCode;
    }

    public String getOvr10kChgCode() {
        return ovr10kChgCode;
    }

    public void setOvr10kChgCode(String ovr10kChgCode) {
        this.ovr10kChgCode = ovr10kChgCode;
    }

    public String getOvr20kChgCode() {
        return ovr20kChgCode;
    }

    public void setOvr20kChgCode(String ovr20kChgCode) {
        this.ovr20kChgCode = ovr20kChgCode;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {

        this.terminalType = terminalType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public Integer getTerminalManagerId() {
        return terminalManagerId;
    }

    public void setTerminalManagerId(Integer terminalManagerId) {
        this.terminalManagerId = terminalManagerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
