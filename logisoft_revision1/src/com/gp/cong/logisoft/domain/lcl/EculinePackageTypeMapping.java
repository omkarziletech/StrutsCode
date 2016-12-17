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
@Table(name = "eculine_package_type_mapping")
@DynamicUpdate(true)
@DynamicInsert(true)
@NamedQueries({
    @NamedQuery(name = "EculinePackageTypeMapping.findAll", query = "SELECT e FROM EculinePackageTypeMapping e"),
    @NamedQuery(name = "EculinePackageTypeMapping.findById", query = "SELECT e FROM EculinePackageTypeMapping e WHERE e.id = :id"),
    @NamedQuery(name = "EculinePackageTypeMapping.findByPackageDesc", query = "SELECT e FROM EculinePackageTypeMapping e WHERE e.packageDesc = :packageDesc")})
public class EculinePackageTypeMapping extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "package_desc")
    private String packageDesc;
    @JoinColumn(name = "package_type_id", referencedColumnName = "id")
    @ManyToOne
    private PackageType packageType;

    public EculinePackageTypeMapping() {
    }

    public EculinePackageTypeMapping(Integer id) {
        this.id = id;
    }

    public EculinePackageTypeMapping(Integer id, String packageDesc) {
        this.id = id;
        this.packageDesc = packageDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String packageDesc) {
        this.packageDesc = packageDesc;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
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
        if (!(object instanceof EculinePackageTypeMapping)) {
            return false;
        }
        EculinePackageTypeMapping other = (EculinePackageTypeMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.EculinePackageTypeMapping[id=" + id + "]";
    }

}
