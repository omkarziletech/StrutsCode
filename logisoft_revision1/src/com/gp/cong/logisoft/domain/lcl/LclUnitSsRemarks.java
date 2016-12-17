/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "lcl_unit_ss_remarks")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclUnitSsRemarks.findAll", query = "SELECT l FROM LclUnitSsRemarks l"),
    @NamedQuery(name = "LclUnitSsRemarks.findById", query = "SELECT l FROM LclUnitSsRemarks l WHERE l.id = :id"),
    @NamedQuery(name = "LclUnitSsRemarks.findByType", query = "SELECT l FROM LclUnitSsRemarks l WHERE l.type = :type"),
    @NamedQuery(name = "LclUnitSsRemarks.findByEnteredDatetime", query = "SELECT l FROM LclUnitSsRemarks l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclUnitSsRemarks.findByModifiedDatetime", query = "SELECT l FROM LclUnitSsRemarks l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclUnitSsRemarks extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "followup_datetime")
    private Date followupDateTime;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclSsHeader lclSsHeader;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclUnit lclUnit;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedby;
    @JoinColumn(name = "followup_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User followUpUserId;
    @Column(name = "followup_email")
    private String followupEmail;

    public LclUnitSsRemarks() {
    }

    public LclUnitSsRemarks(Long id) {
        this.id = id;
    }

    public LclUnitSsRemarks(Long id, String type, String remarks, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.type = type;
        this.remarks = remarks;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getFollowupDateTime() {
        return followupDateTime;
    }

    public void setFollowupDateTime(Date followupDateTime) {
        this.followupDateTime = followupDateTime;
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

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
    }

    public LclUnit getLclUnit() {
        return lclUnit;
    }

    public void setLclUnit(LclUnit lclUnit) {
        this.lclUnit = lclUnit;
    }

    public User getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    public User getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(User modifiedby) {
        this.modifiedby = modifiedby;
    }

    public User getFollowUpUserId() {
        return followUpUserId;
    }

    public void setFollowUpUserId(User followUpUserId) {
        this.followUpUserId = followUpUserId;
    }

    public String getFollowupEmail() {
        return followupEmail;
    }

    public void setFollowupEmail(String followupEmail) {
        this.followupEmail = followupEmail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclUnitSsRemarks)) {
            return false;
        }
        LclUnitSsRemarks other = (LclUnitSsRemarks) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclUnitSsRemarks[id=" + id + "]";
    }
}
