/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.domain.lcl.bl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_bl_air")
public class LclBlAir extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "file_number_id")
    private Long fileNumberId;
    @Basic(optional = false)
    @Column(name = "service_type")
    private String serviceType;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private LclFileNumber lclFileNumber;

    public LclBlAir() {
    }

    public LclBlAir(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public LclBlAir(Long fileNumberId, String serviceType) {
        this.fileNumberId = fileNumberId;
        this.serviceType = serviceType;
    }

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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
        hash += (fileNumberId != null ? fileNumberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclBlAir)) {
            return false;
        }
        LclBlAir other = (LclBlAir) object;
        if ((this.fileNumberId == null && other.fileNumberId != null) || (this.fileNumberId != null && !this.fileNumberId.equals(other.fileNumberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclBlAir[fileNumberId=" + fileNumberId + "]";
    }

}
