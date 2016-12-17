/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vijaygupta.m
 */
@Entity
@Table(name = "eculine_unit_type_mapping")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "EculineUnitTypeMapping.findAll", query = "SELECT e FROM EculineUnitTypeMapping e"),
    @NamedQuery(name = "EculineUnitTypeMapping.findById", query = "SELECT e FROM EculineUnitTypeMapping e WHERE e.id = :id"),
    @NamedQuery(name = "EculineUnitTypeMapping.findByCntrType", query = "SELECT e FROM EculineUnitTypeMapping e WHERE e.cntrType = :cntrType")})
public class EculineUnitTypeMapping extends Domain  {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cntr_type")
    private String cntrType;
    @JoinColumn(name = "unit_type_id", referencedColumnName = "id")
    @ManyToOne
    private UnitType unitType;

    public EculineUnitTypeMapping() {
    }

    public EculineUnitTypeMapping(Integer id) {
        this.id = id;
    }

    public EculineUnitTypeMapping(Integer id, String cntrType) {
        this.id = id;
        this.cntrType = cntrType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCntrType() {
        return cntrType;
    }

    public void setCntrType(String cntrType) {
        this.cntrType = cntrType;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
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
        if (!(object instanceof EculineUnitTypeMapping)) {
            return false;
        }
        EculineUnitTypeMapping other = (EculineUnitTypeMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.EculineUnitTypeMapping[id=" + id + "]";
    }
}
