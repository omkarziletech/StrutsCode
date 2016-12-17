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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.gp.cong.hibernate.Domain;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "lcl_unit")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclUnit.findAll", query = "SELECT l FROM LclUnit l"),
    @NamedQuery(name = "LclUnit.findById", query = "SELECT l FROM LclUnit l WHERE l.id = :id"),
    @NamedQuery(name = "LclUnit.findByUnitNo", query = "SELECT l FROM LclUnit l WHERE l.unitNo = :unitNo"),
    @NamedQuery(name = "LclUnit.findByRefrigerated", query = "SELECT l FROM LclUnit l WHERE l.refrigerated = :refrigerated"),
    @NamedQuery(name = "LclUnit.findByHazmatPermitted", query = "SELECT l FROM LclUnit l WHERE l.hazmatPermitted = :hazmatPermitted"),
    @NamedQuery(name = "LclUnit.findByTareWeightImperial", query = "SELECT l FROM LclUnit l WHERE l.tareWeightImperial = :tareWeightImperial"),
    @NamedQuery(name = "LclUnit.findByTareWeightMetric", query = "SELECT l FROM LclUnit l WHERE l.tareWeightMetric = :tareWeightMetric"),
    @NamedQuery(name = "LclUnit.findByGrossWeightImperial", query = "SELECT l FROM LclUnit l WHERE l.grossWeightImperial = :grossWeightImperial"),
    @NamedQuery(name = "LclUnit.findByGrossWeightMetric", query = "SELECT l FROM LclUnit l WHERE l.grossWeightMetric = :grossWeightMetric"),
    @NamedQuery(name = "LclUnit.findByVolumeImperial", query = "SELECT l FROM LclUnit l WHERE l.volumeImperial = :volumeImperial"),
    @NamedQuery(name = "LclUnit.findByVolumeMetric", query = "SELECT l FROM LclUnit l WHERE l.volumeMetric = :volumeMetric"),
    @NamedQuery(name = "LclUnit.findByEnteredDatetime", query = "SELECT l FROM LclUnit l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclUnit.findByModifiedDatetime", query = "SELECT l FROM LclUnit l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclUnit extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "unit_no")
    private String unitNo;
    @Basic(optional = false)
    @Column(name = "refrigerated")
    private boolean refrigerated;
    @Basic(optional = false)
    @Column(name = "hazmat_permitted")
    private boolean hazmatPermitted;
    @Column(name = "tare_weight_imperial")
    private BigDecimal tareWeightImperial;
    @Column(name = "tare_weight_metric")
    private BigDecimal tareWeightMetric;
    @Column(name = "gross_weight_imperial")
    private BigDecimal grossWeightImperial;
    @Column(name = "gross_weight_metric")
    private BigDecimal grossWeightMetric;
    @Column(name = "volume_imperial")
    private BigDecimal volumeImperial;
    @Column(name = "volume_metric")
    private BigDecimal volumeMetric;
    @Column(name = "comments")
    private String comments;
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "unit_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnitType unitType;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnit")
    private List<LclUnitWhse> lclUnitWhseList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnit")
    private List<LclQuotePieceUnit> lclQuotePieceUnitList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnit")
    private List<LclUnitSs> lclUnitSsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnit")
    private List<LclUnitSsDispo> lclUnitSsDispoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnit")
    private List<LclUnitSsImports> lclUnitSsImportsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnit")
    private List<LclBookingDispo> lclBookingDispoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnit")
    private List<LclUnitSsManifest> lclUnitSsManifestList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclUnit")
    private List<LclUnitSsRemarks> lclUnitSsRemarksList;
    @Transient
    private String displayhazmatPermitted;
    @Transient
    private String displayrefrigerationPermitted;

    public LclUnit() {
    }

    public LclUnit(Long id) {
        this.id = id;
    }

    public LclUnit(Long id, String unitNo, boolean refrigerated, boolean hazmatPermitted, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.unitNo = unitNo;
        this.refrigerated = refrigerated;
        this.hazmatPermitted = hazmatPermitted;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public boolean getRefrigerated() {
        return refrigerated;
    }

    public void setRefrigerated(boolean refrigerated) {
        this.refrigerated = refrigerated;
    }

    public boolean getHazmatPermitted() {
        return hazmatPermitted;
    }

    public void setHazmatPermitted(boolean hazmatPermitted) {
        this.hazmatPermitted = hazmatPermitted;
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

    public List<LclQuotePieceUnit> getLclQuotePieceUnitList() {
        return lclQuotePieceUnitList;
    }

    public void setLclQuotePieceUnitList(List<LclQuotePieceUnit> lclQuotePieceUnitList) {
        this.lclQuotePieceUnitList = lclQuotePieceUnitList;
    }

    public List<LclUnitSs> getLclUnitSsList() {
        return lclUnitSsList;
    }

    public void setLclUnitSsList(List<LclUnitSs> lclUnitSsList) {
        this.lclUnitSsList = lclUnitSsList;
    }

    public List<LclUnitWhse> getLclUnitWhseList() {
        return lclUnitWhseList;
    }

    public void setLclUnitWhseList(List<LclUnitWhse> lclUnitWhseList) {
        this.lclUnitWhseList = lclUnitWhseList;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public String getDisplayhazmatPermitted() {
        return displayhazmatPermitted;
    }

    public void setDisplayhazmatPermitted(String displayhazmatPermitted) {
        this.displayhazmatPermitted = displayhazmatPermitted;
    }

    public String getDisplayrefrigerationPermitted() {
        return displayrefrigerationPermitted;
    }

    public void setDisplayrefrigerationPermitted(String displayrefrigerationPermitted) {
        this.displayrefrigerationPermitted = displayrefrigerationPermitted;
    }

    public List<LclUnitSsDispo> getLclUnitSsDispoList() {
        return lclUnitSsDispoList;
    }

    public void setLclUnitSsDispoList(List<LclUnitSsDispo> lclUnitSsDispoList) {
        this.lclUnitSsDispoList = lclUnitSsDispoList;
    }

    public List<LclUnitSsImports> getLclUnitSsImportsList() {
        return lclUnitSsImportsList;
    }

    public void setLclUnitSsImportsList(List<LclUnitSsImports> lclUnitSsImportsList) {
        this.lclUnitSsImportsList = lclUnitSsImportsList;
    }

    public List<LclBookingDispo> getLclBookingDispoList() {
        return lclBookingDispoList;
    }

    public void setLclBookingDispoList(List<LclBookingDispo> lclBookingDispoList) {
        this.lclBookingDispoList = lclBookingDispoList;
    }

    public List<LclUnitSsManifest> getLclUnitSsManifestList() {
        return lclUnitSsManifestList;
    }

    public void setLclUnitSsManifestList(List<LclUnitSsManifest> lclUnitSsManifestList) {
        this.lclUnitSsManifestList = lclUnitSsManifestList;
    }

    public List<LclUnitSsRemarks> getLclUnitSsRemarksList() {
        return lclUnitSsRemarksList;
    }

    public void setLclUnitSsRemarksList(List<LclUnitSsRemarks> lclUnitSsRemarksList) {
        this.lclUnitSsRemarksList = lclUnitSsRemarksList;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
        if (!(object instanceof LclUnit)) {
            return false;
        }
        LclUnit other = (LclUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    public void loadAllocatedVolumeAndWeight() {
//        for (LclBookingPieceUnit bookingPiece : lclBookingPieceUnitList) {
//            // bookingPiece.getLclBookingPiece().
//        }
//    }
    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclUnit[id=" + id + "]";
    }
}
