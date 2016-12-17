package com.gp.cong.logisoft.domain;

import java.util.*;

public class FclBuy implements java.io.Serializable {

    // Fields    
    private Integer fclStdId;
    private UnLocation originTerminal;
    private UnLocation destinationPort;
    private String originalRegion;
    private String destinationRegion;
    private TradingPartnerTemp sslineNo;
    private GenericCode comNum;
    private Set fclBuyCostsSet;
    private Date startDate;
    private Date endDate;
    private String contract;
    private List displaySellList;
    private String polCode;
    private String podCode;

    // Constructors
    public List getDisplaySellList() {
        return displaySellList;
    }

    public void setDisplaySellList(List displaySellList) {
        this.displaySellList = displaySellList;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * default constructor
     */
    public FclBuy() {
    }

    /**
     * minimal constructor
     */
    public FclBuy(Integer fclStdId) {
        this.fclStdId = fclStdId;
    }

    // Property accessors
    public Integer getFclStdId() {
        return this.fclStdId;
    }

    public void setFclStdId(Integer fclStdId) {
        this.fclStdId = fclStdId;
    }

    public UnLocation getOriginTerminal() {
        return originTerminal;
    }

    public void setOriginTerminal(UnLocation originTerminal) {
        this.originTerminal = originTerminal;
    }

    public UnLocation getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(UnLocation destinationPort) {
        this.destinationPort = destinationPort;
    }

    public GenericCode getComNum() {
        return comNum;
    }

    public TradingPartnerTemp getSslineNo() {
        return sslineNo;
    }

    public void setSslineNo(TradingPartnerTemp sslineNo) {
        this.sslineNo = sslineNo;
    }

    public void setComNum(GenericCode comNum) {
        this.comNum = comNum;
    }

    public Set getFclBuyCostsSet() {
        return fclBuyCostsSet;
    }

    public void setFclBuyCostsSet(Set fclBuyCostsSet) {
        this.fclBuyCostsSet = fclBuyCostsSet;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getOriginalRegion() {
        return originalRegion;
    }

    public void setOriginalRegion(String originalRegion) {
        this.originalRegion = originalRegion;
    }

    public String getDestinationRegion() {
        return destinationRegion;
    }

    public void setDestinationRegion(String destinationRegion) {
        this.destinationRegion = destinationRegion;
    }

    public String getPolCode() {
        return polCode;
    }

    public void setPolCode(String polCode) {
        this.polCode = polCode;
    }

    public String getPodCode() {
        return podCode;
    }

    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }
 
    public int hashCode() { 
        int hash = 0; 
        hash += (fclStdId != null ? fclStdId.hashCode() : 0); 
        return hash; 
    } 

    @Override 
    public boolean equals(Object object) { 
        // TODO: Warning - this method won't work in the case the id fields are not set 
        if (!(object instanceof FclBuy)) { 
            return false; 
        } 
        FclBuy other = (FclBuy) object; 
        return !((this.fclStdId == null && other.fclStdId != null) || (this.fclStdId != null && !this.fclStdId.equals(other.fclStdId))); 
    } 

    @Override 
    public String toString() { 
        return "com.gp.cong.logisoft.domain.FclBuy[id=" + fclStdId + "]"; 
       
    } 
}