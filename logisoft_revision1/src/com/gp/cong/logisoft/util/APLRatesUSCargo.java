package com.gp.cong.logisoft.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class APLRatesUSCargo {

    private String originCode;
    private String origin;
    private String POL;
    private String destinationCode;
    private String destination;
    private String commodityCode;
    private String steamshipLine;
    private String contractNo;
    private Date startDate;
    private Date endDate;
    private ArrayList<String> costCodes;

    public APLRatesUSCargo() {
        super();
        if (costCodes == null) {
            costCodes = new ArrayList<String>();
        }
    }

    public APLRatesUSCargo(String originCode, String origin, String pol, String destinationCode, String destination, String commodityCode, String steamshipLine, String contractNo, Date startDate, Date endDate) {
        super();
        if (costCodes == null) {
            costCodes = new ArrayList<String>();
        }
        this.originCode = originCode;
        this.origin = origin;
        this.POL = pol;
        this.destinationCode = destinationCode;
        this.destination = destination;
        this.commodityCode = commodityCode;
        this.steamshipLine = steamshipLine;
        this.contractNo = contractNo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addCostCode(String costCode) {
        this.costCodes.add(costCode);
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public ArrayList<String> getCostCodes() {
        return costCodes;
    }

    public void setCostCodes(ArrayList<String> costCodes) {
        this.costCodes = costCodes;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getPOL() {
        return POL;
    }

    public void setPOL(String pol) {
        POL = pol;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getSteamshipLine() {
        return steamshipLine;
    }

    public void setSteamshipLine(String steamshipLine) {
        this.steamshipLine = steamshipLine;
    }

    public void trace() {













        for (Iterator iter = costCodes.iterator(); iter.hasNext();) {

        }

    }
}
