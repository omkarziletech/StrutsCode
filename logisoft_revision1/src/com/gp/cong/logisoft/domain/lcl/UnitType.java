/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.logisoft.domain.User;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.gp.cong.hibernate.Domain;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "unit_type")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "UnitType.findAll", query = "SELECT u FROM UnitType u"),
    @NamedQuery(name = "UnitType.findById", query = "SELECT u FROM UnitType u WHERE u.id = :id"),
    @NamedQuery(name = "UnitType.findByDescription", query = "SELECT u FROM UnitType u WHERE u.description = :description"),
    @NamedQuery(name = "UnitType.findByEliteType", query = "SELECT u FROM UnitType u WHERE u.eliteType = :eliteType"),
    @NamedQuery(name = "UnitType.findByInteriorLengthImperial", query = "SELECT u FROM UnitType u WHERE u.interiorLengthImperial = :interiorLengthImperial"),
    @NamedQuery(name = "UnitType.findByInteriorLengthMetric", query = "SELECT u FROM UnitType u WHERE u.interiorLengthMetric = :interiorLengthMetric"),
    @NamedQuery(name = "UnitType.findByInteriorWidthImperial", query = "SELECT u FROM UnitType u WHERE u.interiorWidthImperial = :interiorWidthImperial"),
    @NamedQuery(name = "UnitType.findByInteriorWidthMetric", query = "SELECT u FROM UnitType u WHERE u.interiorWidthMetric = :interiorWidthMetric"),
    @NamedQuery(name = "UnitType.findByInteriorHeightImperial", query = "SELECT u FROM UnitType u WHERE u.interiorHeightImperial = :interiorHeightImperial"),
    @NamedQuery(name = "UnitType.findByInteriorHeightMetric", query = "SELECT u FROM UnitType u WHERE u.interiorHeightMetric = :interiorHeightMetric"),
    @NamedQuery(name = "UnitType.findByDoorHeightImperial", query = "SELECT u FROM UnitType u WHERE u.doorHeightImperial = :doorHeightImperial"),
    @NamedQuery(name = "UnitType.findByDoorHeightMetric", query = "SELECT u FROM UnitType u WHERE u.doorHeightMetric = :doorHeightMetric"),
    @NamedQuery(name = "UnitType.findByDoorWidthImperial", query = "SELECT u FROM UnitType u WHERE u.doorWidthImperial = :doorWidthImperial"),
    @NamedQuery(name = "UnitType.findByDoorWidthMetric", query = "SELECT u FROM UnitType u WHERE u.doorWidthMetric = :doorWidthMetric"),
    @NamedQuery(name = "UnitType.findByGrossWeightImperial", query = "SELECT u FROM UnitType u WHERE u.grossWeightImperial = :grossWeightImperial"),
    @NamedQuery(name = "UnitType.findByGrossWeightMetric", query = "SELECT u FROM UnitType u WHERE u.grossWeightMetric = :grossWeightMetric"),
    @NamedQuery(name = "UnitType.findByTareWeightImperial", query = "SELECT u FROM UnitType u WHERE u.tareWeightImperial = :tareWeightImperial"),
    @NamedQuery(name = "UnitType.findByTareWeightMetric", query = "SELECT u FROM UnitType u WHERE u.tareWeightMetric = :tareWeightMetric"),
    @NamedQuery(name = "UnitType.findByVolumeImperial", query = "SELECT u FROM UnitType u WHERE u.volumeImperial = :volumeImperial"),
    @NamedQuery(name = "UnitType.findByVolumeMetric", query = "SELECT u FROM UnitType u WHERE u.volumeMetric = :volumeMetric"),
    @NamedQuery(name = "UnitType.findByTargetVolumeImperial", query = "SELECT u FROM UnitType u WHERE u.targetVolumeImperial = :targetVolumeImperial"),
    @NamedQuery(name = "UnitType.findByTargetVolumeMetric", query = "SELECT u FROM UnitType u WHERE u.targetVolumeMetric = :targetVolumeMetric"),
    @NamedQuery(name = "UnitType.findByRefrigerated", query = "SELECT u FROM UnitType u WHERE u.refrigerated = :refrigerated"),
    @NamedQuery(name = "UnitType.findByEnteredDatetime", query = "SELECT u FROM UnitType u WHERE u.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "UnitType.findByModifiedDatetime", query = "SELECT u FROM UnitType u WHERE u.modifiedDatetime = :modifiedDatetime"),
    @NamedQuery(name = "UnitType.findByEnteredByUserId", query = "SELECT u FROM UnitType u WHERE u.enteredByUserId = :enteredByUserId"),
    @NamedQuery(name = "UnitType.findByModifieldByUserId", query = "SELECT u FROM UnitType u WHERE u.modifiedByUserId = :modifiedByUserId")})
public class UnitType extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Column(name = "elite_type")
    private String eliteType;
    @Column(name = "short_desc")
    private String shortDesc;
    @Column(name = "interior_length_imperial")
    private BigDecimal interiorLengthImperial;
    @Column(name = "interior_length_metric")
    private BigDecimal interiorLengthMetric;
    @Column(name = "interior_width_imperial")
    private BigDecimal interiorWidthImperial;
    @Column(name = "interior_width_metric")
    private BigDecimal interiorWidthMetric;
    @Column(name = "interior_height_imperial")
    private BigDecimal interiorHeightImperial;
    @Column(name = "interior_height_metric")
    private BigDecimal interiorHeightMetric;
    @Column(name = "door_height_imperial")
    private BigDecimal doorHeightImperial;
    @Column(name = "door_height_metric")
    private BigDecimal doorHeightMetric;
    @Column(name = "door_width_imperial")
    private BigDecimal doorWidthImperial;
    @Column(name = "door_width_metric")
    private BigDecimal doorWidthMetric;
    @Column(name = "gross_weight_imperial")
    private BigDecimal grossWeightImperial;
    @Column(name = "gross_weight_metric")
    private BigDecimal grossWeightMetric;
    @Column(name = "tare_weight_imperial")
    private BigDecimal tareWeightImperial;
    @Column(name = "tare_weight_metric")
    private BigDecimal tareWeightMetric;
    @Column(name = "volume_imperial")
    private BigDecimal volumeImperial;
    @Column(name = "volume_metric")
    private BigDecimal volumeMetric;
    @Column(name = "target_volume_imperial")
    private BigDecimal targetVolumeImperial;
    @Column(name = "target_volume_metric")
    private BigDecimal targetVolumeMetric;
    @Basic(optional = false)
    @Column(name = "refrigerated")
    private boolean refrigerated;
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @Basic(optional = false)
    @Column(name = "enabled_lcl_air")
    private boolean enabledLclAir;
    @Basic(optional = false)
    @Column(name = "enabled_lcl_exports")
    private boolean enabledLclExports;
    @Basic(optional = false)
    @Column(name = "enabled_lcl_imports")
    private boolean enabledLclImports;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredByUserId;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedByUserId;

    public UnitType() {
    }

    public UnitType(Long id) {
        this.id = id;
    }

    public UnitType(Long id, String description, boolean refrigerated, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.description = description;
        this.refrigerated = refrigerated;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEliteType() {
        return eliteType;
    }

    public void setEliteType(String eliteType) {
        this.eliteType = eliteType;
    }

    public BigDecimal getInteriorLengthImperial() {
        return interiorLengthImperial;
    }

    public void setInteriorLengthImperial(BigDecimal interiorLengthImperial) {
        this.interiorLengthImperial = interiorLengthImperial;
    }

    public BigDecimal getInteriorLengthMetric() {
        return interiorLengthMetric;
    }

    public void setInteriorLengthMetric(BigDecimal interiorLengthMetric) {
        this.interiorLengthMetric = interiorLengthMetric;
    }

    public BigDecimal getInteriorWidthImperial() {
        return interiorWidthImperial;
    }

    public void setInteriorWidthImperial(BigDecimal interiorWidthImperial) {
        this.interiorWidthImperial = interiorWidthImperial;
    }

    public BigDecimal getInteriorWidthMetric() {
        return interiorWidthMetric;
    }

    public void setInteriorWidthMetric(BigDecimal interiorWidthMetric) {
        this.interiorWidthMetric = interiorWidthMetric;
    }

    public BigDecimal getInteriorHeightImperial() {
        return interiorHeightImperial;
    }

    public void setInteriorHeightImperial(BigDecimal interiorHeightImperial) {
        this.interiorHeightImperial = interiorHeightImperial;
    }

    public BigDecimal getInteriorHeightMetric() {
        return interiorHeightMetric;
    }

    public void setInteriorHeightMetric(BigDecimal interiorHeightMetric) {
        this.interiorHeightMetric = interiorHeightMetric;
    }

    public BigDecimal getDoorHeightImperial() {
        return doorHeightImperial;
    }

    public void setDoorHeightImperial(BigDecimal doorHeightImperial) {
        this.doorHeightImperial = doorHeightImperial;
    }

    public BigDecimal getDoorHeightMetric() {
        return doorHeightMetric;
    }

    public void setDoorHeightMetric(BigDecimal doorHeightMetric) {
        this.doorHeightMetric = doorHeightMetric;
    }

    public BigDecimal getDoorWidthImperial() {
        return doorWidthImperial;
    }

    public void setDoorWidthImperial(BigDecimal doorWidthImperial) {
        this.doorWidthImperial = doorWidthImperial;
    }

    public BigDecimal getDoorWidthMetric() {
        return doorWidthMetric;
    }

    public void setDoorWidthMetric(BigDecimal doorWidthMetric) {
        this.doorWidthMetric = doorWidthMetric;
    }

    public BigDecimal getGrossWeightImperial() {
        return grossWeightImperial;
    }

    public void setGrossWeightImperial(BigDecimal grossWeightImperial) {
        this.grossWeightImperial = grossWeightImperial;
    }

    public BigDecimal getGrossWeightMetric() {
        return grossWeightMetric;
    }

    public void setGrossWeightMetric(BigDecimal grossWeightMetric) {
        this.grossWeightMetric = grossWeightMetric;
    }

    public BigDecimal getTareWeightImperial() {
        return tareWeightImperial;
    }

    public void setTareWeightImperial(BigDecimal tareWeightImperial) {
        this.tareWeightImperial = tareWeightImperial;
    }

    public BigDecimal getTareWeightMetric() {
        return tareWeightMetric;
    }

    public void setTareWeightMetric(BigDecimal tareWeightMetric) {
        this.tareWeightMetric = tareWeightMetric;
    }

    public BigDecimal getVolumeImperial() {
        return volumeImperial;
    }

    public void setVolumeImperial(BigDecimal volumeImperial) {
        this.volumeImperial = volumeImperial;
    }

    public BigDecimal getVolumeMetric() {
        return volumeMetric;
    }

    public void setVolumeMetric(BigDecimal volumeMetric) {
        this.volumeMetric = volumeMetric;
    }

    public BigDecimal getTargetVolumeImperial() {
        return targetVolumeImperial;
    }

    public void setTargetVolumeImperial(BigDecimal targetVolumeImperial) {
        this.targetVolumeImperial = targetVolumeImperial;
    }

    public BigDecimal getTargetVolumeMetric() {
        return targetVolumeMetric;
    }

    public void setTargetVolumeMetric(BigDecimal targetVolumeMetric) {
        this.targetVolumeMetric = targetVolumeMetric;
    }

    public boolean getRefrigerated() {
        return refrigerated;
    }

    public void setRefrigerated(boolean refrigerated) {
        this.refrigerated = refrigerated;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean getEnabledLclAir() {
        return enabledLclAir;
    }

    public void setEnabledLclAir(boolean enabledLclAir) {
        this.enabledLclAir = enabledLclAir;
    }

    public boolean getEnabledLclExports() {
        return enabledLclExports;
    }

    public void setEnabledLclExports(boolean enabledLclExports) {
        this.enabledLclExports = enabledLclExports;
    }

    public boolean getEnabledLclImports() {
        return enabledLclImports;
    }

    public void setEnabledLclImports(boolean enabledLclImports) {
        this.enabledLclImports = enabledLclImports;
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

    public User getEnteredByUserId() {
        return enteredByUserId;
    }

    public void setEnteredByUserId(User enteredByUserId) {
        this.enteredByUserId = enteredByUserId;
    }

    public User getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(User modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
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
        if (!(object instanceof UnitType)) {
            return false;
        }
        UnitType other = (UnitType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.UnitType[id=" + id + "]";
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

}
