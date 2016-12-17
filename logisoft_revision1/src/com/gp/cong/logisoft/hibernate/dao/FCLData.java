/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.hibernate.dao;

/**
 *
 * @author Vinay
 */
public class FCLData {
    private Integer daysInTransit;
    private Integer destinationPort;
    private String desPort;
    private Integer id;
    private Integer originTerminal;
    private String origTerm;
    private String portOfExit;
    private String sslineNo;
    private String sslineName;
    private String remarks;

    public Integer getDaysInTransit() {
        return daysInTransit;
    }

    public void setDaysInTransit(Integer daysInTransit) {
        this.daysInTransit = daysInTransit;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(Integer destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getDesPort() {
        return desPort;
    }

    public void setDesPort(String desPort) {
        this.desPort = desPort;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(Integer originTerminal) {
        this.originTerminal = originTerminal;
    }

    public String getOrigTerm() {
        return origTerm;
    }

    public void setOrigTerm(String origTerm) {
        this.origTerm = origTerm;
    }

    public String getPortOfExit() {
        return portOfExit;
    }

    public void setPortOfExit(String portOfExit) {
        this.portOfExit = portOfExit;
    }

    public String getSslineNo() {
        return sslineNo;
    }

    public void setSslineNo(String sslineNo) {
        this.sslineNo = sslineNo;
    }

    public String getSslineName() {
        return sslineName;
    }

    public void setSslineName(String sslineName) {
        this.sslineName = sslineName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String toString() {
        String s = "DP=" + destinationPort + "\nOT=" + originTerminal + "\nPoE=" + portOfExit +
                "\nDiT=" + daysInTransit + "\nSSL=" + sslineNo;
        return s;
    }
}
