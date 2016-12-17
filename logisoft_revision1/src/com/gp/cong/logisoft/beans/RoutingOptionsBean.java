package com.gp.cong.logisoft.beans;

import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class RoutingOptionsBean implements Serializable {

    private String origin;
    private String pooRelay;
    private String polRelay;
    private String podRelay;
    private String fdRelay;
    private String pooRelayName;
    private String polRelayName;
    private String podRelayName;
    private String fdRelayName;
    private String nextLrd;
    private String transitTime;
    private String ofrateAmount;
    private String totalAmount;
    private String hiddenTotalAmount;
    private String routingType;
    private String relayType;
    private String ctsAmount;
    private BigDecimal pickupCost;
    private String scac;
    private String fromZip;
    private String toZip;
    private String sailDate;
    private UnLocation portOfDestination;
    private UnLocation portOfLoading;
    private UnLocation finalDestination;
    private UnLocation portOfOrigin;
    private LclBookingVoyageBean voyageBean;
    private List<ChargesInfoBean> quoteRateList;
    private String miles;
    private String days;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public BigDecimal getPickupCost() {
        return pickupCost;
    }

    public void setPickupCost(BigDecimal pickupCost) {
        this.pickupCost = pickupCost;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public LclBookingVoyageBean getVoyageBean() {
        return voyageBean;
    }

    public void setVoyageBean(LclBookingVoyageBean voyageBean) {
        this.voyageBean = voyageBean;
    }

    public String getOfrateAmount() {
        return ofrateAmount;
    }

    public void setOfrateAmount(String ofrateAmount) {
        this.ofrateAmount = ofrateAmount;
    }

    public String getCtsAmount() {
        return ctsAmount;
    }

    public void setCtsAmount(String ctsAmount) {
        this.ctsAmount = ctsAmount;
    }

    public String getNextLrd() {
        return nextLrd;
    }

    public void setNextLrd(String nextLrd) {
        this.nextLrd = nextLrd;
    }

    public String getTransitTime() {
        return transitTime;
    }

    public void setTransitTime(String transitTime) {
        this.transitTime = transitTime;
    }

    public String getFromZip() {
        return fromZip;
    }

    public void setFromZip(String fromZip) {
        this.fromZip = fromZip;
    }

    public String getSailDate() {
        return sailDate;
    }

    public void setSailDate(String sailDate) {
        this.sailDate = sailDate;
    }

    public String getToZip() {
        return toZip;
    }

    public void setToZip(String toZip) {
        this.toZip = toZip;
    }

    public String getFdRelay() {
        return fdRelay;
    }

    public void setFdRelay(String fdRelay) {
        this.fdRelay = fdRelay;
    }

    public String getPodRelay() {
        return podRelay;
    }

    public void setPodRelay(String podRelay) {
        this.podRelay = podRelay;
    }

    public String getPolRelay() {
        return polRelay;
    }

    public void setPolRelay(String polRelay) {
        this.polRelay = polRelay;
    }

    public String getPooRelay() {
        return pooRelay;
    }

    public void setPooRelay(String pooRelay) {
        this.pooRelay = pooRelay;
    }

    public String getFdRelayName() {
        return fdRelayName;
    }

    public void setFdRelayName(String fdRelayName) {
        this.fdRelayName = fdRelayName;
    }

    public String getPodRelayName() {
        return podRelayName;
    }

    public void setPodRelayName(String podRelayName) {
        this.podRelayName = podRelayName;
    }

    public String getPolRelayName() {
        return polRelayName;
    }

    public void setPolRelayName(String polRelayName) {
        this.polRelayName = polRelayName;
    }

    public String getPooRelayName() {
        return pooRelayName;
    }

    public void setPooRelayName(String pooRelayName) {
        this.pooRelayName = pooRelayName;
    }

    public UnLocation getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(UnLocation finalDestination) {
        this.finalDestination = finalDestination;
    }

    public UnLocation getPortOfDestination() {
        return portOfDestination;
    }

    public void setPortOfDestination(UnLocation portOfDestination) {
        this.portOfDestination = portOfDestination;
    }

    public UnLocation getPortOfLoading() {
        return portOfLoading;
    }

    public void setPortOfLoading(UnLocation portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public UnLocation getPortOfOrigin() {
        return portOfOrigin;
    }

    public void setPortOfOrigin(UnLocation portOfOrigin) {
        this.portOfOrigin = portOfOrigin;
    }

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getHiddenTotalAmount() {
        return hiddenTotalAmount;
    }

    public void setHiddenTotalAmount(String hiddenTotalAmount) {
        this.hiddenTotalAmount = hiddenTotalAmount;
    }

    public String getRoutingType() {
        return routingType;
    }

    public void setRoutingType(String routingType) {
        this.routingType = routingType;
    }

    public String getRelayType() {
        return relayType;
    }

    public void setRelayType(String relayType) {
        this.relayType = relayType;
    }

    public List<ChargesInfoBean> getQuoteRateList() {
        return quoteRateList;
    }

    public void setQuoteRateList(List<ChargesInfoBean> quoteRateList) {
        this.quoteRateList = quoteRateList;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }
}
