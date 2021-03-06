package com.gp.cong.logisoft.domain;



/**
 * FclBuyCost generated by MyEclipse - Hibernate Tools
 */
import java.util.*;
public class FclBuyCost  implements java.io.Serializable {


    // Fields    

     private Integer fclCostId;
     private String portOfExit;
     private Integer fclStdId;
     private GenericCode costId;
     private GenericCode contType;
     private Set fclBuyUnitTypesSet;
     private Set fclBuyAirFreightSet;
     private Set fclBuyFutureTypesSet;
     private  UnLocation orgTerminalName;
     private  UnLocation destinationPortName;
     private GenericCode commodityCode;
     private String commodityCodeDesc;
     private TradingPartnerTemp ssLineName;
     private List unitTypeList;
     private String costCode;
     private String costCodeDesc;
     private String costType;
     private String currency;
     private Double retail;
     private String transitTime;
     private Double futureRetail;
     private Date effectiveDate;
     private String tempRate;
     private String orgDestCarrierRemarks;
    // Constructors

   


	public String getOrgDestCarrierRemarks() {
		return orgDestCarrierRemarks;
	}




	public void setOrgDestCarrierRemarks(String orgDestCarrierRemarks) {
		this.orgDestCarrierRemarks = orgDestCarrierRemarks;
	}




	public String getTempRate() {
		return tempRate;
	}




	public void setTempRate(String tempRate) {
		this.tempRate = tempRate;
	}




	public Double getFutureRetail() {
		return futureRetail;
	}




	public void setFutureRetail(Double futureRetail) {
		this.futureRetail = futureRetail;
	}




	public String getTransitTime() {
		return transitTime;
	}




	public void setTransitTime(String transitTime) {
		this.transitTime = transitTime;
	}




	public String getCurrency() {
		return currency;
	}




	public void setCurrency(String currency) {
		this.currency = currency;
	}




	public Double getRetail() {
		return retail;
	}




	public void setRetail(Double retail) {
		this.retail = retail;
	}




	public String getCostCode() {
		return costCode;
	}




	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}




	public String getCostType() {
		return costType;
	}




	public void setCostType(String costType) {
		this.costType = costType;
	}




	public List getUnitTypeList() {
		return unitTypeList;
	}




	public void setUnitTypeList(List unitTypeList) {
		this.unitTypeList = unitTypeList;
	}




	




	public GenericCode getCommodityCode() {
		return commodityCode;
	}




	public void setCommodityCode(GenericCode commodityCode) {
		this.commodityCode = commodityCode;
	}




	public String getCommodityCodeDesc() {
		return commodityCodeDesc;
	}




	public void setCommodityCodeDesc(String commodityCodeDesc) {
		this.commodityCodeDesc = commodityCodeDesc;
	}




	



	




	



	




	public TradingPartnerTemp getSsLineName() {
		return ssLineName;
	}




	public void setSsLineName(TradingPartnerTemp ssLineName) {
		this.ssLineName = ssLineName;
	}




	public Set getFclBuyAirFreightSet() {
		return fclBuyAirFreightSet;
	}




	public void setFclBuyAirFreightSet(Set fclBuyAirFreightSet) {
		this.fclBuyAirFreightSet = fclBuyAirFreightSet;
	}




	/** default constructor */
    public FclBuyCost() {
    }

    

   
    // Property accessors

    public GenericCode getCostId() {
		return costId;
	}




	public void setCostId(GenericCode costId) {
		this.costId = costId;
	}




	public Integer getFclCostId() {
        return this.fclCostId;
    }
    
    public void setFclCostId(Integer fclCostId) {
        this.fclCostId = fclCostId;
    }

    public Integer getFclStdId() {
        return this.fclStdId;
    }
    
    public void setFclStdId(Integer fclStdId) {
        this.fclStdId = fclStdId;
    }

	public Set getFclBuyUnitTypesSet() {
		if(this.fclBuyUnitTypesSet==null){
			this.fclBuyUnitTypesSet = new HashSet();
		}
		return fclBuyUnitTypesSet;
	}




	public void setFclBuyUnitTypesSet(Set fclBuyUnitTypesSet) {
		this.fclBuyUnitTypesSet = fclBuyUnitTypesSet;
	}

	


	public GenericCode getContType() {
		return contType;
	}




	public void setContType(GenericCode contType) {
		this.contType = contType;
	}




	public Set getFclBuyFutureTypesSet() {
		return fclBuyFutureTypesSet;
	}




	public void setFclBuyFutureTypesSet(Set fclBuyFutureTypesSet) {
		this.fclBuyFutureTypesSet = fclBuyFutureTypesSet;
	}




	public UnLocation getOrgTerminalName() {
		return orgTerminalName;
	}




	public void setOrgTerminalName(UnLocation orgTerminalName) {
		this.orgTerminalName = orgTerminalName;
	}




	public UnLocation getDestinationPortName() {
		return destinationPortName;
	}




	public void setDestinationPortName(UnLocation destinationPortName) {
		this.destinationPortName = destinationPortName;
	}




	public String getCostCodeDesc() {
		return costCodeDesc;
	}




	public void setCostCodeDesc(String costCodeDesc) {
		this.costCodeDesc = costCodeDesc;
	}




	public Date getEffectiveDate() {
		return effectiveDate;
	}




	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}




	public String getPortOfExit() {
		return portOfExit;
	}




	public void setPortOfExit(String portOfExit) {
		this.portOfExit = portOfExit;
	}

   public int hashCode() { 
        int hash = 0; 
        hash += (fclCostId != null ? fclCostId.hashCode() : 0); 
        return hash; 
    } 

    @Override 
    public boolean equals(Object object) { 
        // TODO: Warning - this method won't work in the case the id fields are not set 
        if (!(object instanceof FclBuyCost)) { 
            return false; 
        } 
        FclBuyCost other = (FclBuyCost) object; 
        return !((this.fclCostId == null && other.fclCostId != null) || (this.fclCostId != null && !this.fclCostId.equals(other.fclCostId))); 
    } 

    @Override 
    public String toString() { 
        return "com.gp.cong.logisoft.domain.FclBuyCost[id=" + fclCostId + "]"; 
       
    }


	



	








}