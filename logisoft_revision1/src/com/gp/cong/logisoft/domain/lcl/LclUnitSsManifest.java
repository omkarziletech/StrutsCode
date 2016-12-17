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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "lcl_unit_ss_manifest")
@DynamicInsert(true)
@DynamicUpdate(true)

public class LclUnitSsManifest extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "masterbl")
    private String masterbl;
    @Basic(optional = false)
    @Column(name = "calculated_weight_imperial")
    private BigDecimal calculatedWeightImperial;
    @Basic(optional = false)
    @Column(name = "calculated_weight_metric")
    private BigDecimal calculatedWeightMetric;
    @Basic(optional = false)
    @Column(name = "calculated_volume_imperial")
    private BigDecimal calculatedVolumeImperial;
    @Basic(optional = false)
    @Column(name = "calculated_volume_metric")
    private BigDecimal calculatedVolumeMetric;
    @Basic(optional = false)
    @Column(name = "calculated_total_pieces")
    private int calculatedTotalPieces;
    @Basic(optional = false)
    @Column(name = "calculated_bl_count")
    private int calculatedBlCount;
    @Basic(optional = false)
    @Column(name = "calculated_dr_count")
    private int calculatedDrCount;
    @Column(name = "override_weight_imperial")
    private BigDecimal overrideWeightImperial;
    @Column(name = "override_weight_metric")
    private BigDecimal overrideWeightMetric;
    @Column(name = "override_volume_imperial")
    private BigDecimal overrideVolumeImperial;
    @Column(name = "override_volume_metric")
    private BigDecimal overrideVolumeMetric;
    @Column(name = "override_total_pieces")
    private Integer overrideTotalPieces;
    @Column(name = "override_bl_count")
    private Integer overrideBlCount;
    @Column(name = "override_dr_count")
    private Integer overrideDrCount;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @Column(name = "manifested_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date manifestedDatetime;
    @JoinColumn(name = "manifested_by_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User manifestedByUser;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedByUser;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredByUser;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclSsHeader lclSsHeader;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclUnit lclUnit;

    public LclUnitSsManifest() {
        calculatedWeightImperial = BigDecimal.ZERO;
    }

    public LclUnitSsManifest(Long id) {
        this.id = id;
    }

    public LclUnitSsManifest(Long id, BigDecimal calculatedWeightImperial, BigDecimal calculatedWeightMetric, BigDecimal calculatedVolumeImperial, BigDecimal calculatedVolumeMetric, int calculatedTotalPieces, int calculatedBlCount, int calculatedDrCount, Date enteredDatetime, Date modifiedDatetime, Date manifestedDatetime, User manifestedByUser) {
        this.id = id;
        this.calculatedWeightImperial = calculatedWeightImperial;
        this.calculatedWeightMetric = calculatedWeightMetric;
        this.calculatedVolumeImperial = calculatedVolumeImperial;
        this.calculatedVolumeMetric = calculatedVolumeMetric;
        this.calculatedTotalPieces = calculatedTotalPieces;
        this.calculatedBlCount = calculatedBlCount;
        this.calculatedDrCount = calculatedDrCount;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
        this.manifestedDatetime = manifestedDatetime;
        this.manifestedByUser = manifestedByUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMasterbl() {
        return masterbl;
    }

    public void setMasterbl(String masterbl) {
        this.masterbl = masterbl;
    }

    public BigDecimal getCalculatedWeightImperial() {
        return calculatedWeightImperial;
    }

    public void setCalculatedWeightImperial(BigDecimal calculatedWeightImperial) {
        this.calculatedWeightImperial = calculatedWeightImperial;
    }

    public BigDecimal getCalculatedWeightMetric() {
        return calculatedWeightMetric;
    }

    public void setCalculatedWeightMetric(BigDecimal calculatedWeightMetric) {
        this.calculatedWeightMetric = calculatedWeightMetric;
    }

    public BigDecimal getCalculatedVolumeImperial() {
        return calculatedVolumeImperial;
    }

    public void setCalculatedVolumeImperial(BigDecimal calculatedVolumeImperial) {
        this.calculatedVolumeImperial = calculatedVolumeImperial;
    }

    public BigDecimal getCalculatedVolumeMetric() {
        return calculatedVolumeMetric;
    }

    public void setCalculatedVolumeMetric(BigDecimal calculatedVolumeMetric) {
        this.calculatedVolumeMetric = calculatedVolumeMetric;
    }

    public int getCalculatedTotalPieces() {
        return calculatedTotalPieces;
    }

    public void setCalculatedTotalPieces(int calculatedTotalPieces) {
        this.calculatedTotalPieces = calculatedTotalPieces;
    }

    public int getCalculatedBlCount() {
        return calculatedBlCount;
    }

    public void setCalculatedBlCount(int calculatedBlCount) {
        this.calculatedBlCount = calculatedBlCount;
    }

    public int getCalculatedDrCount() {
        return calculatedDrCount;
    }

    public void setCalculatedDrCount(int calculatedDrCount) {
        this.calculatedDrCount = calculatedDrCount;
    }

    public BigDecimal getOverrideWeightImperial() {
        return overrideWeightImperial;
    }

    public void setOverrideWeightImperial(BigDecimal overrideWeightImperial) {
        this.overrideWeightImperial = overrideWeightImperial;
    }

    public BigDecimal getOverrideWeightMetric() {
        return overrideWeightMetric;
    }

    public void setOverrideWeightMetric(BigDecimal overrideWeightMetric) {
        this.overrideWeightMetric = overrideWeightMetric;
    }

    public BigDecimal getOverrideVolumeImperial() {
        return overrideVolumeImperial;
    }

    public void setOverrideVolumeImperial(BigDecimal overrideVolumeImperial) {
        this.overrideVolumeImperial = overrideVolumeImperial;
    }

    public BigDecimal getOverrideVolumeMetric() {
        return overrideVolumeMetric;
    }

    public void setOverrideVolumeMetric(BigDecimal overrideVolumeMetric) {
        this.overrideVolumeMetric = overrideVolumeMetric;
    }

    public Integer getOverrideTotalPieces() {
        return overrideTotalPieces;
    }

    public void setOverrideTotalPieces(Integer overrideTotalPieces) {
        this.overrideTotalPieces = overrideTotalPieces;
    }

    public Integer getOverrideBlCount() {
        return overrideBlCount;
    }

    public void setOverrideBlCount(Integer overrideBlCount) {
        this.overrideBlCount = overrideBlCount;
    }

    public Integer getOverrideDrCount() {
        return overrideDrCount;
    }

    public void setOverrideDrCount(Integer overrideDrCount) {
        this.overrideDrCount = overrideDrCount;
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

    public User getEnteredByUser() {
        return enteredByUser;
    }

    public void setEnteredByUser(User enteredByUser) {
        this.enteredByUser = enteredByUser;
    }

    public User getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(User modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
    }

    public LclUnit getLclUnit() {
        return lclUnit;
    }

    public void setLclUnit(LclUnit lclUnit) {
        this.lclUnit = lclUnit;
    }

    public User getManifestedByUser() {
        return manifestedByUser;
    }

    public void setManifestedByUser(User manifestedByUser) {
        this.manifestedByUser = manifestedByUser;
    }

    public Date getManifestedDatetime() {
        return manifestedDatetime;
    }

    public void setManifestedDatetime(Date manifestedDatetime) {
        this.manifestedDatetime = manifestedDatetime;
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
        if (!(object instanceof LclUnitSsManifest)) {
            return false;
        }
        LclUnitSsManifest other = (LclUnitSsManifest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest[id=" + id + "]";
    }
}
