package com.logiware.ims.client;

import java.util.List;

/**
 *
 * @author Balaji.E(Logiware)
 */
public class ImsModel {

    private String location;
    private IMSQuote lowestQuote;
    private List<IMSQuote> additionalQuotes;
    private boolean hazardous;

    public List<IMSQuote> getAdditionalQuotes() {
        return additionalQuotes;
    }

    public void setAdditionalQuotes(List<IMSQuote> additionalQuotes) {
        this.additionalQuotes = additionalQuotes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public IMSQuote getLowestQuote() {
        return lowestQuote;
    }

    public void setLowestQuote(IMSQuote lowestQuote) {
        this.lowestQuote = lowestQuote;
    }

    public boolean isHazardous() {
        return hazardous;
    }

    public void setHazardous(boolean hazardous) {
        this.hazardous = hazardous;
    }
}
