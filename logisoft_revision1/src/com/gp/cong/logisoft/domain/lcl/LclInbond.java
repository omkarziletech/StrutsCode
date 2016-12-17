/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_inbond")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclInbond extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "inbond_id")
    private Long inbondId;
    @Basic(optional = false)
    @Column(name = "inbond_no")
    private String inbondNo;
    @Column(name = "inbond_type")
    private String inbondType;
    @Column(name = "inbond_open_close")
    private boolean inbondOpenClose;
    @Column(name = "eci_bond")
    private boolean eciBond;
    @Column(name = "inbond_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inbondDatetime;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "inbond_port", referencedColumnName = "id")
    @ManyToOne
    private UnLocation inbondPort;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;

    public LclInbond() {
    }

    public LclInbond(Long inbondId) {
        this.inbondId = inbondId;
    }

    public LclInbond(Long inbondId, String inbondNo, String inbondType, Date inbondDatetime, Date enteredDatetime, Date modifiedDatetime) {
        this.inbondId = inbondId;
        this.inbondNo = inbondNo;
        this.inbondType = inbondType;
        this.inbondDatetime = inbondDatetime;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getInbondId() {
        return inbondId;
    }

    public void setInbondId(Long inbondId) {
        this.inbondId = inbondId;
    }

    public String getInbondNo() {
        return inbondNo;
    }

    public void setInbondNo(String inbondNo) {
        this.inbondNo = inbondNo;
    }

    public String getInbondType() {
        return inbondType;
    }

    public void setInbondType(String inbondType) {
        this.inbondType = inbondType;
    }

    public Date getInbondDatetime() {
        return inbondDatetime;
    }

    public void setInbondDatetime(Date inbondDatetime) {
        this.inbondDatetime = inbondDatetime;
    }

    public boolean isInbondOpenClose() {
        return inbondOpenClose;
    }

    public void setInbondOpenClose(boolean inbondOpenClose) {
        this.inbondOpenClose = inbondOpenClose;
    }

    public boolean isEciBond() {
        return eciBond;
    }

    public void setEciBond(boolean eciBond) {
        this.eciBond = eciBond;
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

    public UnLocation getInbondPort() {
        return inbondPort;
    }

    public void setInbondPort(UnLocation inbondPort) {
        this.inbondPort = inbondPort;
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
        hash += (inbondId != null ? inbondId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclInbond)) {
            return false;
        }
        LclInbond other = (LclInbond) object;
        if ((this.inbondId == null && other.inbondId != null) || (this.inbondId != null && !this.inbondId.equals(other.inbondId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclInbond[inbondId=" + inbondId + "]";
    }
}
