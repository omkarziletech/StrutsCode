/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.struts.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Vinay
 */
public class FCLSearchForm extends ActionForm {
    private Integer id;
    private String action;
    private Integer destinationPort;
    private Integer originTerminal;
    private String portOfExit;
    private String sslineNo;
    private Integer daysInTransit;
    private String hazardous;
    private boolean haz;
    private String localDrayage;
    private boolean locD;
    private String remarks;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(Integer destinationPort) {
        this.destinationPort = destinationPort;
    }

    public Integer getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(Integer originTerminal) {
        this.originTerminal = originTerminal;
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

    public Integer getDaysInTransit() {
        return daysInTransit;
    }

    public void setDaysInTransit(Integer daysInTransit) {
        this.daysInTransit = daysInTransit;
    }

    public String getHazardous() {
        return hazardous;
    }

    public void setHazardous(String hazardous) {
        this.hazardous = hazardous;
    }

    public boolean isHaz() {
        return haz;
    }

    public void setHaz(boolean haz) {
        this.haz = haz;
    }

    public String getLocalDrayage() {
        return localDrayage;
    }

    public void setLocalDrayage(String localDrayage) {
        this.localDrayage = localDrayage;
    }

    public boolean isLocD() {
        return locD;
    }

    public void setLocD(boolean locD) {
        this.locD = locD;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.setHaz(false);
        this.setLocD(false);
    }

}
