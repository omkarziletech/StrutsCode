/**
 *
 */
package com.gp.cong.logisoft.domain;

import java.io.Serializable;

/**
 * @author Rohith
 *
 */
public class RelayInquiryTemp implements Auditable, Serializable {

    private static final long serialVersionUID = 1L;
    private Integer relayId;
    private PortsTemp polCode;
    private PortsTemp podCode;
    private String status;
    private Integer ttFromPolToPod;
    private Integer cutOffDays;
    private Integer hourOfDay;
    private String printOnSs;
    private String match;
    private String comments;
    private String destCheck;
    private Integer noOfOrigins;
    private Integer noOfDestns;
    private Integer noOfPortExcptns;
    private String polName;
    private String podName;

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getPolName() {
        return polName;
    }

    public void setPolName(String polName) {
        this.polName = polName;
    }

    public Integer getNoOfDestns() {
        return noOfDestns;
    }

    public void setNoOfDestns(Integer noOfDestns) {
        this.noOfDestns = noOfDestns;
    }

    public Integer getNoOfOrigins() {
        return noOfOrigins;
    }

    public void setNoOfOrigins(Integer noOfOrigins) {
        this.noOfOrigins = noOfOrigins;
    }

    public Integer getNoOfPortExcptns() {
        return noOfPortExcptns;
    }

    public void setNoOfPortExcptns(Integer noOfPortExcptns) {
        this.noOfPortExcptns = noOfPortExcptns;
    }

    public String getDestCheck() {
        return destCheck;
    }

    public void setDestCheck(String destCheck) {
        this.destCheck = destCheck;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getCutOffDays() {
        return cutOffDays;
    }

    public void setCutOffDays(Integer cutOffDays) {
        this.cutOffDays = cutOffDays;
    }

    public Integer getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(Integer hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public PortsTemp getPodCode() {
        return podCode;
    }

    public void setPodCode(PortsTemp podCode) {
        this.podCode = podCode;
    }

    public PortsTemp getPolCode() {
        return polCode;
    }

    public void setPolCode(PortsTemp polCode) {
        this.polCode = polCode;
    }

    public String getPrintOnSs() {
        return printOnSs;
    }

    public void setPrintOnSs(String printOnSs) {
        this.printOnSs = printOnSs;
    }

    public Integer getRelayId() {
        return relayId;
    }

    public void setRelayId(Integer relayId) {
        this.relayId = relayId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTtFromPolToPod() {
        return ttFromPolToPod;
    }

    public void setTtFromPolToPod(Integer ttFromPolToPod) {
        this.ttFromPolToPod = ttFromPolToPod;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId() {
        // TODO Auto-generated method stub
        return this.getRelayId();
    }

    /**
     * @return the match
     */
    public String getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(String match) {
        this.match = match;
    }
}
