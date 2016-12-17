package com.logiware.bean;

/**
 *
 * @author Lakshmi Narayanan
 */
public class AgingBucketBean implements java.io.Serializable {

    private Double agingBucket30;
    private Double agingBucket60;
    private Double agingBucket90;
    private Double agingBucket91;
    private Double apBalance;
    private Double arBalance;
    private Double acBalance;
    private Double receivables;
    private Double payables;
    private Double total;

    public Double getAcBalance() {
        return acBalance;
    }

    public void setAcBalance(Double acBalance) {
        this.acBalance = acBalance;
    }

    public Double getAgingBucket30() {
        return agingBucket30;
    }

    public void setAgingBucket30(Double agingBucket30) {
        this.agingBucket30 = agingBucket30;
    }

    public Double getAgingBucket60() {
        return agingBucket60;
    }

    public void setAgingBucket60(Double agingBucket60) {
        this.agingBucket60 = agingBucket60;
    }

    public Double getAgingBucket90() {
        return agingBucket90;
    }

    public void setAgingBucket90(Double agingBucket90) {
        this.agingBucket90 = agingBucket90;
    }

    public Double getAgingBucket91() {
        return agingBucket91;
    }

    public void setAgingBucket91(Double agingBucket91) {
        this.agingBucket91 = agingBucket91;
    }

    public Double getApBalance() {
        return apBalance;
    }

    public void setApBalance(Double apBalance) {
        this.apBalance = apBalance;
    }

    public Double getArBalance() {
        return arBalance;
    }

    public void setArBalance(Double arBalance) {
        this.arBalance = arBalance;
    }

    public Double getPayables() {
        return payables;
    }

    public void setPayables(Double payables) {
        this.payables = payables;
    }

    public Double getReceivables() {
        return receivables;
    }

    public void setReceivables(Double receivables) {
        this.receivables = receivables;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
