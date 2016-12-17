package com.gp.cong.lcl.webservices.dom;
import java.util.*;

public class Carrier
{
	private String index;
	private String name;
	private String scac;
	private String originterminal;
	private String destinationterminal;
	private String relation;
	private String days;
	private Service service;
	private String initialcharges;
	private String fuelsurcharge;
	private AdditionalSC additionalsc;
	private String finalcharge;
        private boolean hazmat;

        private String originCode;
        private String originName;
        private String originAddress;
        private String originCity;
        private String originState;
        private String originZip;
        private String originPhone;
        private String originFax;

        private String destinationCode;
        private String destinationName;
        private String destinationAddress;
        private String destinationCity;
        private String destinationState;
        private String destinationZip;
        private String destinationPhone;
        private String destinationFax;
        Map<String,Double> chargeMap = new HashMap<String,Double>();
        
	
	public AdditionalSC getAdditionalsc() {
		return additionalsc;
	}
	public void setAdditionalsc(AdditionalSC additionalsc) {
		this.additionalsc = additionalsc;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDestinationterminal() {
		return destinationterminal;
	}
	public void setDestinationterminal(String destinationterminal) {
		this.destinationterminal = destinationterminal;
	}
	public String getFinalcharge() {
		return finalcharge;
	}
	public void setFinalcharge(String finalcharge) {
		this.finalcharge = finalcharge;
	}
	public String getFuelsurcharge() {
		return fuelsurcharge;
	}
	public void setFuelsurcharge(String fuelsurcharge) {
		this.fuelsurcharge = fuelsurcharge;
	}
	public String getInitialcharges() {
		return initialcharges;
	}
	public void setInitialcharges(String initialcharges) {
		this.initialcharges = initialcharges;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginterminal() {
		return originterminal;
	}
	public void setOriginterminal(String originterminal) {
		this.originterminal = originterminal;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getScac() {
		return scac;
	}
	public void setScac(String scac) {
		this.scac = scac;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getDestinationFax() {
        return destinationFax;
    }

    public void setDestinationFax(String destinationFax) {
        this.destinationFax = destinationFax;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationPhone() {
        return destinationPhone;
    }

    public void setDestinationPhone(String destinationPhone) {
        this.destinationPhone = destinationPhone;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    public String getDestinationZip() {
        return destinationZip;
    }

    public void setDestinationZip(String destinationZip) {
        this.destinationZip = destinationZip;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getOriginFax() {
        return originFax;
    }

    public void setOriginFax(String originFax) {
        this.originFax = originFax;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginPhone() {
        return originPhone;
    }

    public void setOriginPhone(String originPhone) {
        this.originPhone = originPhone;
    }

    public String getOriginState() {
        return originState;
    }

    public void setOriginState(String originState) {
        this.originState = originState;
    }

    public String getOriginZip() {
        return originZip;
    }

    public void setOriginZip(String originZip) {
        this.originZip = originZip;
    }

    public Map<String, Double> getChargeMap() {
        return chargeMap;
    }

    public void setChargeMap(Map<String, Double> chargeMap) {
        this.chargeMap = chargeMap;
    }

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }
	
}
