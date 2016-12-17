package com.gp.cong.logisoft.beans;

import java.util.List;

import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.User;
import java.io.Serializable;

public class NoteBean implements Serializable {
private String buttonValue;
private AuditLogRecord auditLogRecord;
private User user;
private String noteId;
private List auditList;
private String itemName;
private String pageName;
private String voidednote;
private String referenceId;
public String getReferenceId() {
	return referenceId;
}
public void setReferenceId(String referenceId) {
	this.referenceId = referenceId;
}
public String getVoidednote() {
	return voidednote;
}
public void setVoidednote(String voidednote) {
	this.voidednote = voidednote;
}
public String getPageName() {
	return pageName;
}
public void setPageName(String pageName) {
	this.pageName = pageName;
}
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}
public AuditLogRecord getAuditLogRecord() {
	return auditLogRecord;
}
public void setAuditLogRecord(AuditLogRecord auditLogRecord) {
	this.auditLogRecord = auditLogRecord;
}
public String getButtonValue() {
	return buttonValue;
}
public void setButtonValue(String buttonValue) {
	this.buttonValue = buttonValue;
}

public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public String getNoteId() {
	return noteId;
}
public void setNoteId(String noteId) {
	this.noteId = noteId;
}
public List getAuditList() {
	return auditList;
}
public void setAuditList(List auditList) {
	this.auditList = auditList;
}

}
