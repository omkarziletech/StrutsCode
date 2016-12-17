package com.gp.cvst.logisoft.beans;

import java.io.Serializable;

public class BatchesBean implements Serializable {
    
	private String batchno;
	private String desc;
	private String sourceLedger;
	private String totalDebit;
	private String totalCredit;
	private String post;
	private String type;
	private String status;
	private String reverse;
	private String copy;
	private String readyToPost;
	

	public String getReadyToPost() {
		return readyToPost;
	}
	public void setReadyToPost(String readyToPost) {
		this.readyToPost = readyToPost;
	}
	public String getCopy() {
		return copy;
	}
	public void setCopy(String copy) {
		this.copy = copy;
	}
	public String getReverse() {
		return reverse;
	}
	public void setReverse(String reverse) {
		this.reverse = reverse;
	}
	public String getBatchno() {
		return batchno;
	}
	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getSourceLedger() {
		return sourceLedger;
	}
	public void setSourceLedger(String sourceLedger) {
		this.sourceLedger = sourceLedger;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(String totalCredit) {
		this.totalCredit = totalCredit;
	}
	public String getTotalDebit() {
		return totalDebit;
	}
	public void setTotalDebit(String totalDebit) {
		this.totalDebit = totalDebit;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
