package com.logiware.accounting.model;

import com.logiware.bean.ReportBean;
import java.util.List;

/**
 *
 * @author Lakshmi Naryanan
 */
public class ReportModel {

    private ReportBean arBuckets;
    private ReportBean apBuckets;
    private List<ReportBean> transactions;

    public ReportBean getArBuckets() {
	return arBuckets;
    }

    public void setArBuckets(ReportBean arBuckets) {
	this.arBuckets = arBuckets;
    }

    public ReportBean getApBuckets() {
	return apBuckets;
    }

    public void setApBuckets(ReportBean apBuckets) {
	this.apBuckets = apBuckets;
    }

    public List<ReportBean> getTransactions() {
	return transactions;
    }

    public void setTransactions(List<ReportBean> transactions) {
	this.transactions = transactions;
    }

    public ReportModel() {
    }

    public ReportModel(ReportBean arBuckets, ReportBean apBuckets, List<ReportBean> transactions) {
	this.arBuckets = arBuckets;
	this.apBuckets = apBuckets;
	this.transactions = transactions;
    }
}
