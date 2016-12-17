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
import javax.persistence.Lob;
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
@Table(name = "lcl_relay_plan")
@DynamicUpdate(true)
@DynamicInsert(true)
public class LclRelayPlan extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "seg_no")
    private int segNo;
    @Basic(optional = false)
    @Column(name = "trans_mode")
    private String transMode;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @Column(name = "co_dbd")
    private Integer coDbd;
    @Column(name = "co_dow")
    private Integer coDow;
    @Column(name = "co_tod")
    @Temporal(TemporalType.TIME)
    private Date coTod;
    @Basic(optional = false)
    @Column(name = "tt_published")
    private int ttPublished;
    @Column(name = "tt_avg_30")
    private Integer ttAvg30;
    @Column(name = "tt_avg_60")
    private Integer ttAvg60;
    @Column(name = "tt_avg_90")
    private Integer ttAvg90;
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
    @JoinColumn(name = "seg_end_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation segEnd;
    @JoinColumn(name = "seg_start_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation segStart;
    @JoinColumn(name = "fd_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation fd;
    @JoinColumn(name = "pod_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation pod;
    @JoinColumn(name = "pol_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation pol;
    @JoinColumn(name = "poo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation poo;

    public LclRelayPlan() {
    }

    public LclRelayPlan(Long id) {
        this.id = id;
    }

    public LclRelayPlan(Long id, int segNo, String transMode, boolean active, int ttPublished, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.segNo = segNo;
        this.transMode = transMode;
        this.active = active;
        this.ttPublished = ttPublished;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSegNo() {
        return segNo;
    }

    public void setSegNo(int segNo) {
        this.segNo = segNo;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getCoDbd() {
        return coDbd;
    }

    public void setCoDbd(Integer coDbd) {
        this.coDbd = coDbd;
    }

    public Integer getCoDow() {
        return coDow;
    }

    public void setCoDow(Integer coDow) {
        this.coDow = coDow;
    }

    public Date getCoTod() {
        return coTod;
    }

    public void setCoTod(Date coTod) {
        this.coTod = coTod;
    }

    public int getTtPublished() {
        return ttPublished;
    }

    public void setTtPublished(int ttPublished) {
        this.ttPublished = ttPublished;
    }

    public Integer getTtAvg30() {
        return ttAvg30;
    }

    public void setTtAvg30(Integer ttAvg30) {
        this.ttAvg30 = ttAvg30;
    }

    public Integer getTtAvg60() {
        return ttAvg60;
    }

    public void setTtAvg60(Integer ttAvg60) {
        this.ttAvg60 = ttAvg60;
    }

    public Integer getTtAvg90() {
        return ttAvg90;
    }

    public void setTtAvg90(Integer ttAvg90) {
        this.ttAvg90 = ttAvg90;
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

    public UnLocation getFd() {
        return fd;
    }

    public void setFd(UnLocation fd) {
        this.fd = fd;
    }

    public UnLocation getPod() {
        return pod;
    }

    public void setPod(UnLocation pod) {
        this.pod = pod;
    }

    public UnLocation getPol() {
        return pol;
    }

    public void setPol(UnLocation pol) {
        this.pol = pol;
    }

    public UnLocation getPoo() {
        return poo;
    }

    public void setPoo(UnLocation poo) {
        this.poo = poo;
    }

    public UnLocation getSegEnd() {
        return segEnd;
    }

    public void setSegEnd(UnLocation segEnd) {
        this.segEnd = segEnd;
    }

    public UnLocation getSegStart() {
        return segStart;
    }

    public void setSegStart(UnLocation segStart) {
        this.segStart = segStart;
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
        if (!(object instanceof LclRelayPlan)) {
            return false;
        }
        LclRelayPlan other = (LclRelayPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclRelayPlan[id=" + id + "]";
    }
}
