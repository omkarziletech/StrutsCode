/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.kn;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author palraj.p
 */
@Entity
@Table(name = "kn_haz_details")
@NamedQueries({
    @NamedQuery(name = "HazDetail.findAll", query = "SELECT h FROM HazDetail h")})
public class HazDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "class")
    private BigDecimal class1;
    @Column(name = "flash_point_flag")
    private String flashPointFlag;
    @Column(name = "flash_point")
    private String flashPoint;
    @Column(name = "shipping_name")
    private String shippingName;
    @Column(name = "un_number")
    private String unNumber;
    @Column(name = "pack_group")
    private String packGroup;
    @JoinColumn(name = "cargo_id", referencedColumnName = "id")
    @ManyToOne
    private CargoDetail cargoId;

    public HazDetail() {
    }

    public HazDetail(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getClass1() {
        return class1;
    }

    public void setClass1(BigDecimal class1) {
        this.class1 = class1;
    }

    public String getFlashPointFlag() {
        return flashPointFlag;
    }

    public void setFlashPointFlag(String flashPointFlag) {
        this.flashPointFlag = flashPointFlag;
    }

    public String getFlashPoint() {
        return flashPoint;
    }

    public void setFlashPoint(String flashPoint) {
        this.flashPoint = flashPoint;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getUnNumber() {
        return unNumber;
    }

    public void setUnNumber(String unNumber) {
        this.unNumber = unNumber;
    }

    public String getPackGroup() {
        return packGroup;
    }

    public void setPackGroup(String packGroup) {
        this.packGroup = packGroup;
    }

    public CargoDetail getCargoId() {
        return cargoId;
    }

    public void setCargoId(CargoDetail cargoId) {
        this.cargoId = cargoId;
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
        if (!(object instanceof HazDetail)) {
            return false;
        }
        HazDetail other = (HazDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.kn.HazDetail[ id=" + id + " ]";
    }
    
}
