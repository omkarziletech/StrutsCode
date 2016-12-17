package com.gp.cvst.logisoft.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.struts.form.FclBillLaddingForm;

/**
 * FclBl generated by MyEclipse Persistence Tools
 */

public class FclBlContainer implements java.io.Serializable {

	// Fields

	private Integer trailerNoId;
	
		
	private String trailerNo;
	private String trailerNoOld;
	private String userName;
	private Set hazmatSet;
	private String sealNo;
	private GenericCode sizeLegend;
	private String totalNoTrailers;
	private Integer bolId;
	private String marks;
	private Date lastUpdate;
	private Set fclBlMarks;
	//private Set fclAesDetails;
	private List fclMarksList;
	private Set HazmatMaterialSet;
	private String containerComments;
	private String manuallyAddedFlag;
	private String disabledFlag;
	private String specialEquipment;
	private boolean hazmat;
	

	public Set getHazmatMaterialSet() {
		return HazmatMaterialSet;
	}

	public void setHazmatMaterialSet(Set hazmatMaterialSet) {
		HazmatMaterialSet = hazmatMaterialSet;
	}


	public Integer getBolId() {
            return bolId;
        }

        public void setBolId(Integer bolId) {
                this.bolId = bolId;
        }

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public GenericCode getSizeLegend() {
		return sizeLegend;
	}

	public void setSizeLegend(GenericCode sizeLegend) {
		this.sizeLegend = sizeLegend;
	}

	public String getTotalNoTrailers() {
		return totalNoTrailers;
	}

	public void setTotalNoTrailers(String totalNoTrailers) {
		this.totalNoTrailers = totalNoTrailers;
	}

	

	public String getSealNo() {
		return sealNo;
	}

	public void setSealNo(String sealNo) {
		this.sealNo = sealNo;
	}

	public String getTrailerNo() {
		return trailerNo;
	}

	public void setTrailerNo(String trailerNo) {
		this.trailerNo = trailerNo;
	}

	public Integer getTrailerNoId() {
		return trailerNoId;
	}

	public void setTrailerNoId(Integer trailerNoId) {
		this.trailerNoId = trailerNoId;
	}

	public Set getFclBlMarks() {
		return fclBlMarks;
	}

	public void setFclBlMarks(Set fclBlMarks) {
		this.fclBlMarks = fclBlMarks;
	}

	public Set getHazmatSet() {
		return hazmatSet;
	}

	public void setHazmatSet(Set hazmatSet) {
		this.hazmatSet = hazmatSet;
	}

	public String getMarks() {
		return marks;
	}

	public void setMarks(String marks) {
		this.marks = marks;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public FclBlContainer(){
		
	}
	public FclBlContainer(FclBillLaddingForm fclBillLaddingForm)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		GenericCodeDAO genericCodeDAO=new GenericCodeDAO();
		this.trailerNo = fclBillLaddingForm.getTrailerNo().toUpperCase();
		this.sealNo = fclBillLaddingForm.getSealNo().toUpperCase();
		this.bolId = Integer.parseInt(fclBillLaddingForm.getBol());
		this.sizeLegend = genericCodeDAO.findById(Integer.parseInt(fclBillLaddingForm.getSizeLegend()));
                this.lastUpdate = new Date();
		this.marks = fclBillLaddingForm.getMarksNo().toUpperCase();
		this.userName = fclBillLaddingForm.getUserName();
	}

	public List getFclMarksList() {
		return fclMarksList;
	}

	public void setFclMarksList(List fclMarksList) {
		this.fclMarksList = fclMarksList;
	}

	public String getContainerComments() {
		return containerComments;
	}

	public void setContainerComments(String containerComments) {
		this.containerComments = containerComments;
	}

	public String getManuallyAddedFlag() {
		return manuallyAddedFlag;
	}

	public void setManuallyAddedFlag(String manuallyAddedFlag) {
		this.manuallyAddedFlag = manuallyAddedFlag;
	}

	public String getDisabledFlag() {
		return disabledFlag;
	}

	public void setDisabledFlag(String disabledFlag) {
		this.disabledFlag = disabledFlag;
	}

    public String getSpecialEquipment() {
        return specialEquipment;
    }

    public void setSpecialEquipment(String specialEquipment) {
        this.specialEquipment = specialEquipment;
    }

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getTrailerNoOld() {
        return trailerNoOld;
    }

    public void setTrailerNoOld(String trailerNoOld) {
        this.trailerNoOld = trailerNoOld;
    }

}