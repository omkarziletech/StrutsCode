/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "lcl_quote_piece")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclQuotePiece extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
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
    @Column(name = "stdchg_rate_basis")
    private String stdchgRateBasis;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "lclQuotePiece")
    private List<LclQuoteAc> lclQuoteAcList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclQuotePiece")
    private List<LclQuoteHazmat> lclQuoteHazmatList;
    @OneToMany(mappedBy = "quotePiece")
    private List<LclQuotePieceUnit> lclQuotePieceUnitList;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "weight_verfied_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User weightVerfiedBy;
    @JoinColumn(name = "packaging_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PackageType packageType;
    @JoinColumn(name = "commodity_type_id", referencedColumnName = "id")
    @ManyToOne
    private CommodityType commodityType;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotePiece")
    private List<LclQuotePieceDetail> lclQuotePieceDetailList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclQuotePiece")
    private List<LclQuoteHsCode> lclQuoteHsCodeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclQuotePiece")
    private List<LclQuotePieceWhse> lclQuotePieceWhseList;
    @Transient
    private String perCftCbm;
    @Transient
    private String perLbsKgs;
    @Transient
    private String ofratemin;
    @Transient
    private String commNo;
    @Transient
    private String commName;
    @Transient
    private String ofrateamount;
    @Transient
    private String pkgName;
    @Transient
    private Long pkgNo;
    @Transient
    private String count;
    @Transient
    private String packagePlural;
    

    public LclQuotePiece() {
    }

    public LclQuotePiece(Long id) {
        this.id = id;
    }

    public LclQuotePiece(Long id, boolean hazmat, String personalEffects, boolean refrigerationRequired, int bookedPieceCount, boolean weightVerified, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.hazmat = hazmat;
        this.personalEffects = personalEffects;
        this.refrigerationRequired = refrigerationRequired;
        this.bookedPieceCount = bookedPieceCount;
        this.weightVerified = weightVerified;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public String getStdchgRateBasis() {
        return stdchgRateBasis;
    }

    public void setStdchgRateBasis(String stdchgRateBasis) {
        this.stdchgRateBasis = stdchgRateBasis;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
  
    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public Long getPkgNo() {
        return pkgNo;
    }

    public void setPkgNo(Long pkgNo) {
        this.pkgNo = pkgNo;
    }

    public String getCommNo() {
        return commNo;
    }

    public void setCommNo(String commNo) {
        this.commNo = commNo;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getOfrateamount() {
        return ofrateamount;
    }

    public void setOfrateamount(String ofrateamount) {
        this.ofrateamount = ofrateamount;
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

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
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

    public List<LclQuoteHazmat> getLclQuoteHazmatList() {
        return lclQuoteHazmatList;
    }

    public void setLclQuoteHazmatList(List<LclQuoteHazmat> lclQuoteHazmatList) {
        this.lclQuoteHazmatList = lclQuoteHazmatList;
    }

    public List<LclQuotePieceUnit> getLclQuotePieceUnitList() {
        return lclQuotePieceUnitList;
    }

    public void setLclQuotePieceUnitList(List<LclQuotePieceUnit> lclQuotePieceUnitList) {
        this.lclQuotePieceUnitList = lclQuotePieceUnitList;
    }

    public User getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public List<LclQuoteHsCode> getLclQuoteHsCodeList() {
        return lclQuoteHsCodeList;
    }

    public void setLclQuoteHsCodeList(List<LclQuoteHsCode> lclQuoteHsCodeList) {
        this.lclQuoteHsCodeList = lclQuoteHsCodeList;
    }

    
    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public User getWeightVerfiedBy() {
        return weightVerfiedBy;
    }

    public void setWeightVerfiedBy(User weightVerfiedBy) {
        this.weightVerfiedBy = weightVerfiedBy;
    }

    public List<LclQuotePieceDetail> getLclQuotePieceDetailList() {
        return lclQuotePieceDetailList;
    }

    public void setLclQuotePieceDetailList(List<LclQuotePieceDetail> lclQuotePieceDetailList) {
        this.lclQuotePieceDetailList = lclQuotePieceDetailList;
    }

    public List<LclQuoteAc> getLclQuoteAcList() {
        return lclQuoteAcList;
    }

    public void setLclQuoteAcList(List<LclQuoteAc> lclQuoteAcList) {
        this.lclQuoteAcList = lclQuoteAcList;
    }

     public boolean isIsBarrel() {
        return isBarrel;
    }

    public void setIsBarrel(boolean isBarrel) {
        this.isBarrel = isBarrel;
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

    public String getPackagePlural() {
        return packagePlural;
    }

    public void setPackagePlural(String packagePlural) {
        this.packagePlural = packagePlural;
    }

    public List<LclQuotePieceWhse> getLclQuotePieceWhseList() {
        return lclQuotePieceWhseList;
    }

    public void setLclQuotePieceWhseList(List<LclQuotePieceWhse> lclQuotePieceWhseList) {
        this.lclQuotePieceWhseList = lclQuotePieceWhseList;
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
        if (!(object instanceof LclQuotePiece)) {
            return false;
        }
        LclQuotePiece other = (LclQuotePiece) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclQuotePiece[id=" + id + "]";
    }
}
