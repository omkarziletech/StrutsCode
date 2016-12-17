package com.logiware.fcl.form;

import com.logiware.fcl.model.ResultModel;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class SsMasterDisputedBlForm extends BaseForm {

    private String origin;
    private String destination;
    private String pol;
    private String pod;
    private String sslineName;
    private String sslineNumber;
    private String eta;
    private String etd;
    private String sslBl;
    private List<ResultModel> results;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getSslineName() {
        return sslineName;
    }

    public void setSslineName(String sslineName) {
        this.sslineName = sslineName;
    }

    public String getSslineNumber() {
        return sslineNumber;
    }

    public void setSslineNumber(String sslineNumber) {
        this.sslineNumber = sslineNumber;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public List<ResultModel> getResults() {
        return results;
    }

    public void setResults(List<ResultModel> results) {
        this.results = results;
    }

    public String getSslBl() {
        return sslBl;
    }

    public void setSslBl(String sslBl) {
        this.sslBl = sslBl;
    }
}
