/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.hibernate;

import com.gp.cong.logisoft.domain.GenericCode;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Owner
 */
@Entity
@Table(name = "fcl_bl_container_dtls")
public class FclBlContainerDtls extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "trailer_no_id")
    private Integer trailerNoId;
    @Column(name = "trailer_no")
    private String trailerNo;
    @Column(name = "trailer_no_old")
    private String trailerNoOld;
    @Column(name = "seal_no")
    private String sealNo;
    @JoinColumn(name = "size_legend", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private GenericCode sizeLegend;
    @Column(name = "total_no_trailers")
    private String totalNoTrailers;
    @Column(name = "last_update")
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;
    @Column(name = "marks_no")
    private String marksNo;
    @Column(name = "username")
    private String username;
    @Column(name = "container_comments")
    private String containerComments;
    @Column(name = "manual_flag")
    private String manualFlag;
    @Column(name = "disabled_flag")
    private String disabledFlag;
    @Column(name = "special_equipment")
    private String specialEquipment;
    @Column (name = "process_status")
    private String processStatus;
    
    @JoinColumn(name = "BolId", referencedColumnName = "Bol")
    @ManyToOne(fetch = FetchType.LAZY)
    private FclBlNew fclBl;

    public FclBlContainerDtls() {
    }

    public FclBlContainerDtls(Integer trailerNoId) {
        this.trailerNoId = trailerNoId;
    }

    public Integer getTrailerNoId() {
        return trailerNoId;
    }

    public void setTrailerNoId(Integer trailerNoId) {
        this.trailerNoId = trailerNoId;
    }

    public String getTrailerNo() {
        return null != trailerNo?trailerNo.toUpperCase():"";
    }

    public void setTrailerNo(String trailerNo) {
        this.trailerNo =  null != trailerNo?trailerNo.toUpperCase():"";
    }

    public String getSealNo() {
        return  null != sealNo?sealNo.toUpperCase():"";
    }

    public void setSealNo(String sealNo) {
        this.sealNo = null != sealNo?sealNo.toUpperCase():"";
    }

    public GenericCode getSizeLegend() {
        return sizeLegend;
    }

    public void setSizeLegend(GenericCode sizeLegend) {
        this.sizeLegend = sizeLegend;
    }

    public String getTotalNoTrailers() {
        return totalNoTrailers;
    }

    public void setTotalNoTrailers(String totalNoTrailers) {
        this.totalNoTrailers = totalNoTrailers;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getMarksNo() {
        return null != marksNo?marksNo.toUpperCase():"";
    }

    public void setMarksNo(String marksNo) {
        this.marksNo = null != marksNo?marksNo.toUpperCase():"";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContainerComments() {
        return containerComments;
    }

    public void setContainerComments(String containerComments) {
        this.containerComments = containerComments;
    }

    public String getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(String manualFlag) {
        this.manualFlag = manualFlag;
    }

    public String getDisabledFlag() {
        return disabledFlag;
    }

    public void setDisabledFlag(String disabledFlag) {
        this.disabledFlag = disabledFlag;
    }

    public String getSpecialEquipment() {
        return specialEquipment;
    }

    public void setSpecialEquipment(String specialEquipment) {
        this.specialEquipment = specialEquipment;
    }

    public FclBlNew getFclBl() {
        return fclBl;
    }

    public void setFclBl(FclBlNew fclBl) {
        this.fclBl = fclBl;
    }

    public String getTrailerNoOld() {
        return trailerNoOld;
    }

    public void setTrailerNoOld(String trailerNoOld) {
        this.trailerNoOld = trailerNoOld;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trailerNoId != null ? trailerNoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FclBlContainerDtls)) {
            return false;
        }
        FclBlContainerDtls other = (FclBlContainerDtls) object;
        if ((this.trailerNoId == null && other.trailerNoId != null) || (this.trailerNoId != null && !this.trailerNoId.equals(other.trailerNoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.hibernate.FclBlContainerDtls[trailerNoId=" + trailerNoId + "]";
    }

}
