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
import net.sf.acegisecurity.UserDetails;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_relay_exception")
public class LclRelayException extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
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

    public LclRelayException() {
    }

    public LclRelayException(Long id) {
        this.id = id;
    }

    public LclRelayException(Long id, boolean active, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.active = active;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclRelayException)) {
            return false;
        }
        LclRelayException other = (LclRelayException) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclRelayException[id=" + id + "]";
    }
}
