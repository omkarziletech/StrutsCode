package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.util.Date;

public class ChargesNotes implements AuditLogRecord,Serializable{
    private Integer id;
    private String moduleId;
    private String moduleRefId;
    private Date updateDate;
    private String noteType;
    private String noteDesc;
    private String updatedBy;
    private String itemName;
    private String voidNote = "N";
    private Date followupDate;
    private String status;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleRefId() {
		return moduleRefId;
	}
	public void setModuleRefId(String moduleRefId) {
		this.moduleRefId = moduleRefId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getNoteType() {
		return noteType;
	}
	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}
	public String getNoteDesc() {
		return noteDesc;
	}
	public void setNoteDesc(String noteDesc) {
		this.noteDesc = noteDesc;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getVoidNote() {
		return voidNote;
	}
	public void setVoidNote(String voidNote) {
		this.voidNote = voidNote;
	}
	public Date getFollowupDate() {
		return followupDate;
	}
	public void setFollowupDate(Date followupDate) {
		this.followupDate = followupDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public AuditLogRecord getAuditLogRecordObject() {
		// TODO Auto-generated method stub
		return null;
	}
	public Integer getCodeTypeId() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getDate() {
		// TODO Auto-generated method stub
		return null;
	}
	public Auditable getEntity() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getEntityAttribute() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getEntityId() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getNewValue() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getNote() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getOldValue() {
		// TODO Auto-generated method stub
		return null;
	}
	public Date getUpdatedDate() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getVoided() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setCodeTypeId(Integer codeTypeId) {
		// TODO Auto-generated method stub
		
	}
	public void setDate(String date) {
		// TODO Auto-generated method stub
		
	}
	public void setEntity(Auditable entity) {
		// TODO Auto-generated method stub
		
	}
	public void setEntityAttribute(String entityAttribute) {
		// TODO Auto-generated method stub
		
	}
	public void setEntityId(String entityId) {
		// TODO Auto-generated method stub
		
	}
	public void setEntityName(String entityName) {
		// TODO Auto-generated method stub
		
	}
	public void setMessage(String message) {
		// TODO Auto-generated method stub
		
	}
	public void setNewValue(String newValue) {
		// TODO Auto-generated method stub
		
	}
	public void setNote(String note) {
		// TODO Auto-generated method stub
		
	}
	public void setOldValue(String oldValue) {
		// TODO Auto-generated method stub
		
	}
	public void setUpdatedDate(Date updatedDate) {
		// TODO Auto-generated method stub
		
	}
	public void setVoided(String voided) {
		// TODO Auto-generated method stub
		
	}
}
