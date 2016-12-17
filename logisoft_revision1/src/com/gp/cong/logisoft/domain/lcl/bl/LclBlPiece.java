/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.bl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.CommodityType;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.PackageType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_bl_piece")
public class LclBlPiece extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Lob
    @Column(name = "piece_desc")
    private String pieceDesc;
    @Column(name = "mark_no_desc")
    private String markNoDesc;
    @Basic(optional = false)
    @Column(name = "hazmat")
    private boolean hazmat;
    @Basic(optional = false)
    @Column(name = "personal_effects")
    private String personalEffects;
    @Basic(optional = false)
    @Column(name = "refrigeration_required")
    private boolean refrigerationRequired;
    @Column(name = "stdchg_rate_basis")
    private String stdchgRateBasis;
    @Basic(optional = false)
    @Column(name = "is_barrel")
    private boolean isBarrel;
    @Column(name = "harmonized_code")
    private String harmonizedCode;
    @Column(name = "booked_piece_count")
    private Integer bookedPieceCount;
    @Column(name = "booked_weight_imperial")
    private BigDecimal bookedWeightImperial;
    @Column(name = "booked_weight_metric")
    private BigDecimal bookedWeightMetric;
    @Column(name = "booked_volume_imperial")
    private BigDecimal bookedVolumeImperial;
    @Column(name = "booked_volume_metric")
    private BigDecimal bookedVolumeMetric;
    @Column(name = "actual_piece_count")
    private Integer actualPieceCount;
    @Column(name = "actual_weight_imperial")
    private BigDecimal actualWeightImperial;
    @Column(name = "actual_weight_metric")
    private BigDecimal actualWeightMetric;
    @Column(name = "actual_volume_imperial")
    private BigDecimal actualVolumeImperial;
    @Column(name = "actual_volume_metric")
    private BigDecimal actualVolumeMetric;
    @Basic(optional = false)
    @Column(name = "weight_verified")
    private boolean weightVerified;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "weight_verfied_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User weightVerifiedBy;
    @JoinColumn(name = "packaging_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PackageType packageType;
    @JoinColumn(name = "commodity_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CommodityType commodityType;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclBlPiece")
    private List<LclBlHazmat> LclBlHazmatList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclBlPiece")
//    private List<LclBlPieceUnit> lclBlPieceUnitList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclBlPiece")
    private List<LclBlPieceDetail> lclBlPieceDetailList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclBlPiece")
    private List<LclBlAc> lclBlAcList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclBlPiece")
    private List<LclBlPieceWhse> lclBlPieceWhseList;
    @Transient
    private String perCftCbm;
    @Transient
    private String commName;
    @Transient
    private String commNo;
    @Transient
    private String pkgName;
    @Transient
    private Long pkgNo;
    @Transient
    private String perLbsKgs;
    @Transient
    private String ofratemin;
    @Transient
    private String ofrateamount;

    public LclBlPiece() {
    }

    public LclBlPiece(Long id) {
        this.id = id;
    }

    public LclBlPiece(Long id, boolean hazmat, String personalEffects, boolean refrigerationRequired, boolean isBarrel, Integer bookedPieceCount, boolean weightVerified, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.hazmat = hazmat;
        this.personalEffects = personalEffects;
        this.personalEffects = personalEffects;
        this.isBarrel = isBarrel;
        this.bookedPieceCount = bookedPieceCount;
        this.weightVerified = weightVerified;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPieceDesc() {
        return pieceDesc;
    }

    public void setPieceDesc(String pieceDesc) {
        this.pieceDesc = pieceDesc;
    }

    public String getMarkNoDesc() {
        return markNoDesc;
    }

    public void setMarkNoDesc(String markNoDesc) {
        this.markNoDesc = markNoDesc;
    }

    public boolean getHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getStdchgRateBasis() {
        return stdchgRateBasis;
    }

    public void setStdchgRateBasis(String stdchgRateBasis) {
        this.stdchgRateBasis = stdchgRateBasis;
    }

    public boolean isIsBarrel() {
        return isBarrel;
    }

    public void setIsBarrel(boolean isBarrel) {
        this.isBarrel = isBarrel;
    }

    public String getPersonalEffects() {
        return personalEffects;
    }

    public void setPersonalEffects(String personalEffects) {
        this.personalEffects = personalEffects;
    }

    public boolean getRefrigerationRequired() {
        return refrigerationRequired;
    }

    public void setRefrigerationRequired(boolean refrigerationRequired) {
        this.refrigerationRequired = refrigerationRequired;
    }

    public String getHarmonizedCode() {
        return harmonizedCode;
    }

    public void setHarmonizedCode(String harmonizedCode) {
        this.harmonizedCode = harmonizedCode;
    }

    public Integer getBookedPieceCount() {
        return bookedPieceCount;
    }

    public void setBookedPieceCount(Integer bookedPieceCount) {
        this.bookedPieceCount = bookedPieceCount;
    }

    public BigDecimal getBookedWeightImperial() {
        return bookedWeightImperial;
    }

    public void setBookedWeightImperial(BigDecimal bookedWeightImperial) {
        this.bookedWeightImperial = bookedWeightImperial;
    }

    public BigDecimal getBookedWeightMetric() {
        return bookedWeightMetric;
    }

    public void setBookedWeightMetric(BigDecimal bookedWeightMetric) {
        this.bookedWeightMetric = bookedWeightMetric;
    }

    public BigDecimal getBookedVolumeImperial() {
        return bookedVolumeImperial;
    }

    public void setBookedVolumeImperial(BigDecimal bookedVolumeImperial) {
        this.bookedVolumeImperial = bookedVolumeImperial;
    }

    public BigDecimal getBookedVolumeMetric() {
        return bookedVolumeMetric;
    }

    public void setBookedVolumeMetric(BigDecimal bookedVolumeMetric) {
        this.bookedVolumeMetric = bookedVolumeMetric;
    }

    public Integer getActualPieceCount() {
        return actualPieceCount;
    }

    public void setActualPieceCount(Integer actualPieceCount) {
        this.actualPieceCount = actualPieceCount;
    }

    public BigDecimal getActualWeightImperial() {
        return actualWeightImperial;
    }

    public void setActualWeightImperial(BigDecimal actualWeightImperial) {
        this.actualWeightImperial = actualWeightImperial;
    }

    public BigDecimal getActualWeightMetric() {
        return actualWeightMetric;
    }

    public void setActualWeightMetric(BigDecimal actualWeightMetric) {
        this.actualWeightMetric = actualWeightMetric;
    }

    public BigDecimal getActualVolumeImperial() {
        return actualVolumeImperial;
    }

    public void setActualVolumeImperial(BigDecimal actualVolumeImperial) {
        this.actualVolumeImperial = actualVolumeImperial;
    }

    public BigDecimal getActualVolumeMetric() {
        return actualVolumeMetric;
    }

    public void setActualVolumeMetric(BigDecimal actualVolumeMetric) {
        this.actualVolumeMetric = actualVolumeMetric;
    }

    public boolean getWeightVerified() {
        return weightVerified;
    }

    public void setWeightVerified(boolean weightVerified) {
        this.weightVerified = weightVerified;
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

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public List<LclBlHazmat> getLclBlHazmatList() {
        return LclBlHazmatList;
    }

    public void setLclBlHazmatList(List<LclBlHazmat> LclBlHazmatList) {
        this.LclBlHazmatList = LclBlHazmatList;
    }

    public List<LclBlPieceDetail> getLclBlPieceDetailList() {
        return lclBlPieceDetailList;
    }

    public void setLclBlPieceDetailList(List<LclBlPieceDetail> lclBlPieceDetailList) {
        this.lclBlPieceDetailList = lclBlPieceDetailList;
    }

    //
//    public List<LclBlPieceUnit> getLclBlPieceUnitList() {
//        return lclBlPieceUnitList;
//    }
//
//    public void setLclBlPieceUnitList(List<LclBlPieceUnit> lclBlPieceUnitList) {
//        this.lclBlPieceUnitList = lclBlPieceUnitList;
//    }
    public User getWeightVerifiedBy() {
        return weightVerifiedBy;
    }

    public void setWeightVerifiedBy(User weightVerifiedBy) {
        this.weightVerifiedBy = weightVerifiedBy;
    }

    public String getPerCftCbm() {
        return perCftCbm;
    }

    public void setPerCftCbm(String perCftCbm) {
        this.perCftCbm = perCftCbm;
    }

    public String getPerLbsKgs() {
        return perLbsKgs;
    }

    public void setPerLbsKgs(String perLbsKgs) {
        this.perLbsKgs = perLbsKgs;
    }

    public String getOfratemin() {
        return ofratemin;
    }

    public void setOfratemin(String ofratemin) {
        this.ofratemin = ofratemin;
    }

    public List<LclBlAc> getLclBlAcList() {
        return lclBlAcList;
    }

    public void setLclBlAcList(List<LclBlAc> lclBlAcList) {
        this.lclBlAcList = lclBlAcList;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getCommNo() {
        return commNo;
    }

    public void setCommNo(String commNo) {
        this.commNo = commNo;
    }

    public Long getPkgNo() {
        return pkgNo;
    }

    public void setPkgNo(Long pkgNo) {
        this.pkgNo = pkgNo;
    }

    public String getOfrateamount() {
        return ofrateamount;
    }

    public void setOfrateamount(String ofrateamount) {
        this.ofrateamount = ofrateamount;
    }

    public List<LclBlPieceWhse> getLclBlPieceWhseList() {
        return lclBlPieceWhseList;
    }

    public void setLclBlPieceWhseList(List<LclBlPieceWhse> lclBlPieceWhseList) {
        this.lclBlPieceWhseList = lclBlPieceWhseList;
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
        if (!(object instanceof LclBlPiece)) {
            return false;
        }
        LclBlPiece other = (LclBlPiece) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece[id=" + id + "]";
    }
}
