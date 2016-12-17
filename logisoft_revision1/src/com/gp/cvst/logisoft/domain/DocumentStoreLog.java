package com.gp.cvst.logisoft.domain;

import java.io.Serializable;
import java.util.Date;




/**
 * ArInvoice entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class DocumentStoreLog implements Serializable {

	// Fields

	private Integer id;
	private String screenName;
	private String documentType;
	private String documentName;
	private String documentID;
	private String fileLocation;
	private String fileName;
	private String operation;
	private Date dateOprDone;
	private Date dateRecd;
	private String comments;
	private String status;
        private String fileSize;
        private String ackComments;

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentID() {
		return documentID;
	}

	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Date getDateOprDone() {
		return dateOprDone;
	}

	public void setDateOprDone(Date dateOprDone) {
		this.dateOprDone = dateOprDone;
	}

	public Date getDateRecd() {
		return dateRecd;
	}

	public void setDateRecd(Date dateRecd) {
		this.dateRecd = dateRecd;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAckComments() {
        return ackComments;
    }

    public void setAckComments(String ackComments) {
        this.ackComments = ackComments;
    }
        
}
	
	
	
