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
@Table(name = "lcl_unit_ss_dispo")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclUnitSsDispo.findAll", query = "SELECT l FROM LclUnitSsDispo l"),
    @NamedQuery(name = "LclUnitSsDispo.findById", query = "SELECT l FROM LclUnitSsDispo l WHERE l.id = :id"),
    @NamedQuery(name = "LclUnitSsDispo.findByDispositionDatetime", query = "SELECT l FROM LclUnitSsDispo l WHERE l.dispositionDatetime = :dispositionDatetime"),
    @NamedQuery(name = "LclUnitSsDispo.findByEnteredDatetime", query = "SELECT l FROM LclUnitSsDispo l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclUnitSsDispo.findByModifiedDatetime", query = "SELECT l FROM LclUnitSsDispo l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclUnitSsDispo extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "disposition_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dispositionDatetime;
    @Lob
    @Column(name = "remarks")
    private String remarks;
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
    @JoinColumn(name = "disposition_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Disposition disposition;
    @JoinColumn(name = "ss_detail_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclSsDetail lclSsDetail;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclUnit lclUnit;

    public LclUnitSsDispo() {
    }

    public LclUnitSsDispo(Long id) {
        this.id = id;
    }

    public LclUnitSsDispo(Long id, Date dispositionDatetime, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.dispositionDatetime = dispositionDatetime;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDispositionDatetime() {
        return dispositionDatetime;
    }

    public void setDispositionDatetime(Date dispositionDatetime) {
        this.dispositionDatetime = dispositionDatetime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public Disposition getDisposition() {
        return disposition;
    }

    public void setDisposition(Disposition disposition) {
        this.disposition = disposition;
    }

    public LclSsDetail getLclSsDetail() {
        return lclSsDetail;
    }

    public void setLclSsDetail(LclSsDetail lclSsDetail) {
        this.lclSsDetail = lclSsDetail;
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

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
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
        if (!(object instanceof LclUnitSsDispo)) {
            return false;
        }
        LclUnitSsDispo other = (LclUnitSsDispo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclUnitSsDispo[id=" + id + "]";
    }

}
