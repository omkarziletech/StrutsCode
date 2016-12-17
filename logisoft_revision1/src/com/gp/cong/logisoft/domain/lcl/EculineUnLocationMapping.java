/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author vijaygupta.m
 */
@Entity
@Table(name = "eculine_un_location_mapping")
public class EculineUnLocationMapping extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "un_location_desc")
    private String unLocationDesc;
    @Basic(optional = false)
    @Column(name = "un_location_code")
    private String unLocationCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnLocationCode() {
        return unLocationCode;
    }

    public void setUnLocationCode(String unLocationCode) {
        this.unLocationCode = unLocationCode;
    }

    public String getUnLocationDesc() {
        return unLocationDesc;
    }

    public void setUnLocationDesc(String unLocationDesc) {
        this.unLocationDesc = unLocationDesc;
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
        if (!(object instanceof EculineUnLocationMapping)) {
            return false;
        }
        EculineUnLocationMapping other = (EculineUnLocationMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.EculineUnLocationMapping[id=" + id + "]";
    }
}
