package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Set;

public class FTFMaster implements Serializable {

    private Integer id;
    private Double maxDocCharge;
    private Double ffCommission;
    private Double blBottomLine;
    private Set ftfDetailsSet;
    private Set ftfDocumentSet;
    private Set ftfStdChgSet;
    private Set ftfCommChgSet;
    private String orgTerminal;
    private String destPort;
    private String comCode;
// 
    private String orgTerminalName;
    private String destPortName;
    private String comCodeName;

    public Set getFtfCommChgSet() {
        return ftfCommChgSet;
    }

    public void setFtfCommChgSet(Set ftfCommChgSet) {
        this.ftfCommChgSet = ftfCommChgSet;
    }

    public Set getFtfDetailsSet() {
        return ftfDetailsSet;
    }

    public void setFtfDetailsSet(Set ftfDetailsSet) {
        this.ftfDetailsSet = ftfDetailsSet;
    }

    public Set getFtfDocumentSet() {
        return ftfDocumentSet;
    }

    public void setFtfDocumentSet(Set ftfDocumentSet) {
        this.ftfDocumentSet = ftfDocumentSet;
    }

    public Set getFtfStdChgSet() {
        return ftfStdChgSet;
    }

    public void setFtfStdChgSet(Set ftfStdChgSet) {
        this.ftfStdChgSet = ftfStdChgSet;
    }

    public Double getBlBottomLine() {
        return blBottomLine;
    }

    public void setBlBottomLine(Double blBottomLine) {
        this.blBottomLine = blBottomLine;
    }

    public Double getFfCommission() {
        return ffCommission;
    }

    public void setFfCommission(Double ffCommission) {
        this.ffCommission = ffCommission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMaxDocCharge() {
        return maxDocCharge;
    }

    public void setMaxDocCharge(Double maxDocCharge) {
        this.maxDocCharge = maxDocCharge;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getComCodeName() {
        return comCodeName;
    }

    public void setComCodeName(String comCodeName) {
        this.comCodeName = comCodeName;
    }

    public String getDestPort() {
        return destPort;
    }

    public void setDestPort(String destPort) {
        this.destPort = destPort;
    }

    public String getDestPortName() {
        return destPortName;
    }

    public void setDestPortName(String destPortName) {
        this.destPortName = destPortName;
    }

    public String getOrgTerminal() {
        return orgTerminal;
    }

    public void setOrgTerminal(String orgTerminal) {
        this.orgTerminal = orgTerminal;
    }

    public String getOrgTerminalName() {
        return orgTerminalName;
    }

    public void setOrgTerminalName(String orgTerminalName) {
        this.orgTerminalName = orgTerminalName;
    }
}
