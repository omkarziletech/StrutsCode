/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.bl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_bl_import")
public class LclBlImport extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "file_number_id")
    private Long fileNumberId;
    @Column(name = "scac")
    private String scac;
    @Basic(optional = false)
    @Column(name = "release_cargo")
    private boolean releaseCargo;
    @Column(name = "it_class")
    private String itClass;
    @Column(name = "it_no")
    private String itNo;
    @Column(name = "it_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date itDatetime;
    @Column(name = "entry_class")
    private String entryClass;
    @Column(name = "entry_no")
    private String entryNo;
    @Column(name = "bill_no")
    private String billNo;
    @Column(name = "customs_release_no")
    private String customsReleaseNo;
    @Column(name = "sub_house_bl")
    private String subHouseBl;
    @Column(name = "declared_value")
    private BigDecimal declaredValue;
    @Column(name = "declared_value_estimated")
    private Boolean declaredValueEstimated;
    @Column(name = "final_dest_clause")
    private Boolean finalDestClause;
    @Column(name = "express_release_clause")
    private Boolean expressReleaseClause;
    @Column(name = "go_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date goDatetime;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "dest_whse_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Warehouse destWhse;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private LclFileNumber lclFileNumber;

    public LclBlImport() {
    }

    public LclBlImport(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public LclBlImport(Long fileNumberId, boolean releaseCargo, Date enteredDatetime, Date modifiedDatetime) {
        this.fileNumberId = fileNumberId;
        this.releaseCargo = releaseCargo;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public boolean getReleaseCargo() {
        return releaseCargo;
    }

    public void setReleaseCargo(boolean releaseCargo) {
        this.releaseCargo = releaseCargo;
    }

    public String getItClass() {
        return itClass;
    }

    public void setItClass(String itClass) {
        this.itClass = itClass;
    }

    public String getItNo() {
        return itNo;
    }

    public void setItNo(String itNo) {
        this.itNo = itNo;
    }

    public Date getItDatetime() {
        return itDatetime;
    }

    public void setItDatetime(Date itDatetime) {
        this.itDatetime = itDatetime;
    }

    public String getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(String entryClass) {
        this.entryClass = entryClass;
    }

    public String getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(String entryNo) {
        this.entryNo = entryNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCustomsReleaseNo() {
        return customsReleaseNo;
    }

    public void setCustomsReleaseNo(String customsReleaseNo) {
        this.customsReleaseNo = customsReleaseNo;
    }

    public String getSubHouseBl() {
        return subHouseBl;
    }

    public void setSubHouseBl(String subHouseBl) {
        this.subHouseBl = subHouseBl;
    }

    public BigDecimal getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(BigDecimal declaredValue) {
        this.declaredValue = declaredValue;
    }

    public Boolean getDeclaredValueEstimated() {
        return declaredValueEstimated;
    }

    public void setDeclaredValueEstimated(Boolean declaredValueEstimated) {
        this.declaredValueEstimated = declaredValueEstimated;
    }

    public Boolean getFinalDestClause() {
        return finalDestClause;
    }

    public void setFinalDestClause(Boolean finalDestClause) {
        this.finalDestClause = finalDestClause;
    }

    public Boolean getExpressReleaseClause() {
        return expressReleaseClause;
    }

    public void setExpressReleaseClause(Boolean expressReleaseClause) {
        this.expressReleaseClause = expressReleaseClause;
    }

    public Date getGoDatetime() {
        return goDatetime;
    }

    public void setGoDatetime(Date goDatetime) {
        this.goDatetime = goDatetime;
    }

    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public User getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Warehouse getDestWhse() {
        return destWhse;
    }

    public void setDestWhse(Warehouse destWhse) {
        this.destWhse = destWhse;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileNumberId != null ? fileNumberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclBlImport)) {
            return false;
        }
        LclBlImport other = (LclBlImport) object;
        if ((this.fileNumberId == null && other.fileNumberId != null) || (this.fileNumberId != null && !this.fileNumberId.equals(other.fileNumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclBlImport[fileNumberId=" + fileNumberId + "]";
    }
}
