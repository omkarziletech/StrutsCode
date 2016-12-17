/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain;

import com.gp.cong.common.CommonConstants;
import java.io.Serializable;

/**
 *
 * @author Vinay
 */
public class FCLData implements Serializable {

    private String desPort;
    private String origTerm;
    private String sslineName;
    private Integer daysInTransit;
    private Integer destinationPort;
    private Integer id;
    private Integer originTerminal;
    private String portOfExit;
    private String sslineNo;
    private String remarks;
    private String hazardous;
    private boolean haz;
    private String localDrayage;
    private boolean locD;

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

    public String getHazardous() {
        return hazardous;
    }

    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    public String getLocalDrayage() {
        return localDrayage;
    }

    public void setHaz() {
        if (this.hazardous.equals(CommonConstants.YES)) {
            haz = true;
        } else {
            haz = false;
        }
    }

    public boolean isHaz() {
        return haz;
    }

    public void setLocalDrayage(String localDrayage) {
        this.localDrayage = localDrayage;
    }

    public boolean isLocD() {
        return locD;
    }

    public void setLocD() {
        if (this.localDrayage.equals(CommonConstants.YES)) {
            locD = true;
        } else {
            locD = false;
        }
    }

    public String toString() {
        String s = "DP=" + destinationPort + "\nOT=" + originTerminal + "\nPoE=" + portOfExit
                + "\nDiT=" + daysInTransit + "\nSSL=" + sslineNo;
        return s;
    }
}
