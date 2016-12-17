package com.gp.cong.logisoft.beans;

import java.io.Serializable;

public class DocRecieptBean implements Serializable {

    private Integer dockId;
    private Integer dockReceipt;
     private Integer piece;
     private String cuftWarehouse;
     private String remarks;
     private String hazmat;
     private String dateIn;
     private String eta;
     private String etd;
     private String status;
     private String consignee;
     private String hazNotes;
     private String genNotes;
     private String loadingInstr;
     private String weight;
     private String cft;
	public String getCft() {
		return cft;
	}
	public void setCft(String cft) {
		this.cft = cft;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getCuftWarehouse() {
		return cuftWarehouse;
	}
	public void setCuftWarehouse(String cuftWarehouse) {
		this.cuftWarehouse = cuftWarehouse;
	}
	public String getDateIn() {
		return dateIn;
	}
	public void setDateIn(String dateIn) {
		this.dateIn = dateIn;
	}
	public Integer getDockId() {
		return dockId;
	}
	public void setDockId(Integer dockId) {
		this.dockId = dockId;
	}
	public Integer getDockReceipt() {
		return dockReceipt;
	}
	public void setDockReceipt(Integer dockReceipt) {
		this.dockReceipt = dockReceipt;
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
	public String getGenNotes() {
		return genNotes;
	}
	public void setGenNotes(String genNotes) {
		this.genNotes = genNotes;
	}
	public String getHazmat() {
		return hazmat;
	}
	public void setHazmat(String hazmat) {
		this.hazmat = hazmat;
	}
	public String getHazNotes() {
		return hazNotes;
	}
	public void setHazNotes(String hazNotes) {
		this.hazNotes = hazNotes;
	}
	public String getLoadingInstr() {
		return loadingInstr;
	}
	public void setLoadingInstr(String loadingInstr) {
		this.loadingInstr = loadingInstr;
	}
	public Integer getPiece() {
		return piece;
	}
	public void setPiece(Integer piece) {
		this.piece = piece;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
