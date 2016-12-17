/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import java.math.BigDecimal;
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
 * @author Rajesh
 */
@Entity
@Table(name = "cargo_details")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "CargoDetails.findAll", query = "SELECT c FROM CargoDetails c"),
    @NamedQuery(name = "CargoDetails.findById", query = "SELECT c FROM CargoDetails c WHERE c.id = :id"),
    @NamedQuery(name = "CargoDetails.findByMarksNoDesc", query = "SELECT c FROM CargoDetails c WHERE c.marksNoDesc = :marksNoDesc"),
    @NamedQuery(name = "CargoDetails.findByPackageAmount", query = "SELECT c FROM CargoDetails c WHERE c.packageAmount = :packageAmount"),
    @NamedQuery(name = "CargoDetails.findByPackageDesc", query = "SELECT c FROM CargoDetails c WHERE c.packageDesc = :packageDesc"),
    @NamedQuery(name = "CargoDetails.findByWeightValues", query = "SELECT c FROM CargoDetails c WHERE c.weightValues = :weightValues"),
    @NamedQuery(name = "CargoDetails.findByVolumeValues", query = "SELECT c FROM CargoDetails c WHERE c.volumeValues = :volumeValues"),
    @NamedQuery(name = "CargoDetails.findByDischargeInstruction", query = "SELECT c FROM CargoDetails c WHERE c.dischargeInstruction = :dischargeInstruction"),
    @NamedQuery(name = "CargoDetails.findByCommercialValue", query = "SELECT c FROM CargoDetails c WHERE c.commercialValue = :commercialValue"),
    @NamedQuery(name = "CargoDetails.findByCurrency", query = "SELECT c FROM CargoDetails c WHERE c.currency = :currency")})
public class CargoDetails extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "container_id")
    private String containerId;
    @Lob
    @Column(name = "marks_no_desc")
    private String marksNoDesc;
    @Basic(optional = false)
    @Column(name = "package_amount")
    private int packageAmount;
    @Column(name = "package_desc")
    private String packageDesc;
    @Lob
    @Column(name = "good_desc")
    private String goodDesc;
    @Column(name = "weight_values")
    private BigDecimal weightValues;
    @Column(name = "weight_unit")
    private String weightUnit;
    @Column(name = "volume_values")
    private BigDecimal volumeValues;
    @Column(name = "volume_unit")
    private String volumeUnit;
    @Column(name = "discharge_instruction")
    private String dischargeInstruction;
    @Column(name = "commercial_value")
    private Integer commercialValue;
    @Column(name = "currency")
    private String currency;
    @JoinColumn(name = "bl_info_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BlInfo blInfo;
    @Column(name = "updated_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id")
    @ManyToOne
    private User updatedByUser;
    @Column(name = "created_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    @ManyToOne
    private User createdByUser;

    public CargoDetails() {
    }

    public CargoDetails(Long id) {
        this.id = id;
    }

    public CargoDetails(Long id, int packageAmount) {
        this.id = id;
        this.packageAmount = packageAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getMarksNoDesc() {
        return marksNoDesc;
    }

    public void setMarksNoDesc(String marksNoDesc) {
        this.marksNoDesc = marksNoDesc;
    }

    public int getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(int packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String packageDesc) {
        this.packageDesc = packageDesc;
    }

    public String getGoodDesc() {
        return goodDesc;
    }

    public void setGoodDesc(String goodDesc) {
        this.goodDesc = goodDesc;
    }

    public BigDecimal getWeightValues() {
        return weightValues;
    }

    public void setWeightValues(BigDecimal weightValues) {
        this.weightValues = weightValues;
    }

    public BigDecimal getVolumeValues() {
        return volumeValues;
    }

    public void setVolumeValues(BigDecimal volumeValues) {
        this.volumeValues = volumeValues;
    }

    public String getDischargeInstruction() {
        return dischargeInstruction;
    }

    public void setDischargeInstruction(String dischargeInstruction) {
        this.dischargeInstruction = dischargeInstruction;
    }

    public Integer getCommercialValue() {
        return commercialValue;
    }

    public void setCommercialValue(Integer commercialValue) {
        this.commercialValue = commercialValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVolumeUnit() {
        return volumeUnit;
    }

    public void setVolumeUnit(String volumeUnit) {
        this.volumeUnit = volumeUnit;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public BlInfo getBlInfo() {
        return blInfo;
    }

    public void setBlInfo(BlInfo blInfo) {
        this.blInfo = blInfo;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
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
        if (!(object instanceof CargoDetails)) {
            return false;
        }
        CargoDetails other = (CargoDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ecuedi.model.CargoDetails[id=" + id + "]";
    }
}
