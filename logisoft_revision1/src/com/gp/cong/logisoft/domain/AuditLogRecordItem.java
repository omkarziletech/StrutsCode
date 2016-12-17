package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class AuditLogRecordItem implements AuditLogRecord, Serializable {

    private Integer id;
    private String entityName;
    private String entityAttribute;
    private String message;
    private String updatedBy;
    private java.util.Date updatedDate;
    private String newValue;
    private String oldValue;
    private String entityId;
    private Auditable entity;
    private String noteType;
    private String date;
    private String voided;
    private String note;

    public String getDate() {
        if (updatedDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            return sdf.format(updatedDate);
        }
        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public Auditable getEntity() {
        return entity;
    }

    public void setEntity(Auditable entity) {
        this.entity = entity;
    }

    public String getEntityAttribute() {
        return entityAttribute;
    }

    public void setEntityAttribute(String entityAttribute) {
        this.entityAttribute = entityAttribute;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public java.util.Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(java.util.Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getVoided() {
        return voided;
    }

    public void setVoided(String voided) {
        this.voided = voided;
    }

    public AuditLogRecord getAuditLogRecordObject() {
        // TODO Auto-generated method stub
        return new AuditLogRecordItem();
    }

    public Integer getCodeTypeId() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setCodeTypeId(Integer codeTypeId) {
        // TODO Auto-generated method stub
    }
}
