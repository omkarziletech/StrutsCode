package com.gp.cong.logisoft.struts.form;

import java.util.Date;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.directwebremoting.dwrp.FileUpload;

import com.gp.cong.common.CommonUtils;

public class ScanForm extends ActionForm {
	private Long id;
	private String screenName;
	private String documentType;
	private String documentName;
	private String fileLocation;
	private String pageAction;
	private String fileNumber;
	private String fileNo;
	private String systemRule;
	private String operation;
	private String fileName;
	private Date receivedDate;
	private String receivedDateString;
	private FormFile theFile;
	private String comments;
	private String ssMasterStatus;
	private String ssMasterBl;
	private String houseBl;
	private String status;
	private String fileSize;
        private String unitNo;
	
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public FormFile getTheFile() {
		return theFile;
	}
	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}
	public String getReceivedDateString() {
		return receivedDateString;
	}
	public void setReceivedDateString(String receivedDateString) {
		this.receivedDateString = receivedDateString;
	}
	private Date operationDate;
	
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Date getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}
	public String getFileNumber() {
		if(CommonUtils.isEqual(screenName, "FCLFILE") && -1 != fileNumber.indexOf("-")){
			fileNumber =fileNumber.substring(0,fileNumber.indexOf("-"));
		}
		return fileNumber;
	}
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

        public String getSsMasterBl() {
            return ssMasterBl;
        }

        public void setSsMasterBl(String ssMasterBl) {
            this.ssMasterBl = ssMasterBl;
        }

    public String getHouseBl() {
        return houseBl;
    }

    public void setHouseBl(String houseBl) {
        this.houseBl = houseBl;
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
	public String getFileLocation() {
		return fileLocation;
	}
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	public String getPageAction() {
		return pageAction;
	}
	public void setPageAction(String pageAction) {
		this.pageAction = pageAction;
	}
	public String getSystemRule() {
		return systemRule;
	}
	public void setSystemRule(String systemRule) {
		this.systemRule = systemRule;
	}

    public String getSsMasterStatus() {
        return ssMasterStatus;
    }

    public void setSsMasterStatus(String ssMasterStatus) {
        this.ssMasterStatus = ssMasterStatus;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }


}
