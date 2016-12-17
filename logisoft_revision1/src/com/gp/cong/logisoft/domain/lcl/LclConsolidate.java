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
@Table(name = "lcl_consolidation")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclConsolidate extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "lcl_file_number_id_a", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumberA;
    @JoinColumn(name = "lcl_file_number_id_b", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumberB;
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

    public LclConsolidate() {
    }

    public LclConsolidate(Long id) {
        this.id = id;
    }

    public LclConsolidate(Long id, LclFileNumber lclFileNumberA, LclFileNumber lclFileNumberB) {
        this.id = id;
        this.lclFileNumberA = lclFileNumberA;
        this.lclFileNumberB = lclFileNumberB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LclFileNumber getLclFileNumberA() {
        return lclFileNumberA;
    }

    public void setLclFileNumberA(LclFileNumber lclFileNumberA) {
        this.lclFileNumberA = lclFileNumberA;
    }

    public LclFileNumber getLclFileNumberB() {
        return lclFileNumberB;
    }

    public void setLclFileNumberB(LclFileNumber lclFileNumberB) {
        this.lclFileNumberB = lclFileNumberB;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclConsolidate)) {
            return false;
        }
        LclConsolidate other = (LclConsolidate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclFileNumber[id=" + id + "]";
    }
}
