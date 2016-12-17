/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.bl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.CascadeType;
/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_bl_hazmat")
public class LclBlHazmat extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "un_hazmat_no")
    private String unHazmatNo;
    @Basic(optional = false)
    @Column(name = "proper_shipping_name")
    private String properShippingName;
    @Basic(optional = false)
    @Column(name = "technical_name")
    private String technicalName;
    @Column(name = "inner_pkg_uom")
    private String innerPkgUom;
    @Column(name = "inner_pkg_no_pieces")
    private Integer innerPkgNoPieces;
    @Column(name = "inner_pkg_nwt_piece")
    private BigDecimal innerPkgNwtPiece;
    @Column(name = "inner_pkg_type")
    private String innerPkgType;
    @Column(name = "inner_pkg_composition")
    private String innerPkgComposition;
    @Column(name = "outer_pkg_no_pieces")
    private Integer outerPkgNoPieces;
    @Column(name = "outer_pkg_type")
    private String outerPkgType;
    @Column(name = "outer_pkg_composition")
    private String outerPkgComposition;
    @Column(name = "liquid_volume")
    private BigDecimal liquidVolume;
    @Column(name = "liquid_volume_uom")
    private String liquidVolumeUom;
    @Column(name = "total_net_weight")
    private BigDecimal totalNetWeight;
    @Column(name = "total_gross_weight")
    private BigDecimal totalGrossWeight;
    @Column(name = "flash_point")
    private BigDecimal flashPoint;
    @Column(name = "flash_point_uom")
    private String flashPointUom;
    @Column(name = "imo_pri_class_code")
    private String imoPriClassCode;
    @Column(name = "imo_pri_sub_class_code")
    private String imoPriSubClassCode;
    @Column(name = "imo_sec_sub_class_code")
    private String imoSecSubClassCode;
    @Column(name = "marine_pollutant")
    private Boolean marinePollutant;
    @Column(name = "residue")
    private Boolean residue;
    @Column(name = "inhalation_hazard")
    private Boolean inhalationHazard;
    @Column(name = "excepted_quantity")
    private Boolean exceptedQuantity;
    @Column(name = "limited_quantity")
    private Boolean limitedQuantity;
    @Column(name = "reportable_quantity")
    private Boolean reportableQuantity;
    @Column(name = "ems_code")
    private String emsCode;
    @Column(name = "packing_group_code")
    private String packingGroupCode;
    @Lob
    @Column(name = "hazmat_declarations")
    private String hazmatDeclarations;
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
    @JoinColumn(name = "emergency_contact_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL)
    private LclContact emergencyContact;
    @JoinColumn(name = "booking_piece_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclBlPiece lclBlPiece;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @Column(name="liquid_volume_liters_gallons")
    private String liquidVolumeLitreorGals;


    public LclBlHazmat() {
    }

    public LclBlHazmat(Long id) {
        this.id = id;
    }

    public LclBlHazmat(Long id, String unHazmatNo, String properShippingName, String technicalName, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.unHazmatNo = unHazmatNo;
        this.properShippingName = properShippingName;
        this.technicalName = technicalName;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnHazmatNo() {
        return unHazmatNo;
    }

    public void setUnHazmatNo(String unHazmatNo) {
        this.unHazmatNo = unHazmatNo;
    }

    public String getProperShippingName() {
        return properShippingName;
    }

    public void setProperShippingName(String properShippingName) {
        this.properShippingName = properShippingName;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    public String getInnerPkgUom() {
        return innerPkgUom;
    }

    public void setInnerPkgUom(String innerPkgUom) {
        this.innerPkgUom = innerPkgUom;
    }

    public Integer getInnerPkgNoPieces() {
        return innerPkgNoPieces;
    }

    public void setInnerPkgNoPieces(Integer innerPkgNoPieces) {
        this.innerPkgNoPieces = innerPkgNoPieces;
    }

    public BigDecimal getInnerPkgNwtPiece() {
        return innerPkgNwtPiece;
    }

    public void setInnerPkgNwtPiece(BigDecimal innerPkgNwtPiece) {
        this.innerPkgNwtPiece = innerPkgNwtPiece;
    }

    public String getInnerPkgType() {
        return innerPkgType;
    }

    public void setInnerPkgType(String innerPkgType) {
        this.innerPkgType = innerPkgType;
    }

    public String getInnerPkgComposition() {
        return innerPkgComposition;
    }

    public void setInnerPkgComposition(String innerPkgComposition) {
        this.innerPkgComposition = innerPkgComposition;
    }

    public Integer getOuterPkgNoPieces() {
        return outerPkgNoPieces;
    }

    public void setOuterPkgNoPieces(Integer outerPkgNoPieces) {
        this.outerPkgNoPieces = outerPkgNoPieces;
    }

    public String getOuterPkgType() {
        return outerPkgType;
    }

    public void setOuterPkgType(String outerPkgType) {
        this.outerPkgType = outerPkgType;
    }

    public String getOuterPkgComposition() {
        return outerPkgComposition;
    }

    public void setOuterPkgComposition(String outerPkgComposition) {
        this.outerPkgComposition = outerPkgComposition;
    }

    public BigDecimal getLiquidVolume() {
        return liquidVolume;
    }

    public void setLiquidVolume(BigDecimal liquidVolume) {
        this.liquidVolume = liquidVolume;
    }

    public String getLiquidVolumeUom() {
        return liquidVolumeUom;
    }

    public void setLiquidVolumeUom(String liquidVolumeUom) {
        this.liquidVolumeUom = liquidVolumeUom;
    }

    public BigDecimal getFlashPoint() {
        return flashPoint;
    }

    public void setFlashPoint(BigDecimal flashPoint) {
        this.flashPoint = flashPoint;
    }

    public String getFlashPointUom() {
        return flashPointUom;
    }

    public void setFlashPointUom(String flashPointUom) {
        this.flashPointUom = flashPointUom;
    }

    public String getImoPriClassCode() {
        return imoPriClassCode;
    }

    public void setImoPriClassCode(String imoPriClassCode) {
        this.imoPriClassCode = imoPriClassCode;
    }

    public String getImoPriSubClassCode() {
        return imoPriSubClassCode;
    }

    public void setImoPriSubClassCode(String imoPriSubClassCode) {
        this.imoPriSubClassCode = imoPriSubClassCode;
    }

    public String getImoSecSubClassCode() {
        return imoSecSubClassCode;
    }

    public void setImoSecSubClassCode(String imoSecSubClassCode) {
        this.imoSecSubClassCode = imoSecSubClassCode;
    }

    public Boolean getMarinePollutant() {
        return marinePollutant;
    }

    public void setMarinePollutant(Boolean marinePollutant) {
        this.marinePollutant = marinePollutant;
    }

    public Boolean getResidue() {
        return residue;
    }

    public void setResidue(Boolean residue) {
        this.residue = residue;
    }

    public Boolean getInhalationHazard() {
        return inhalationHazard;
    }

    public void setInhalationHazard(Boolean inhalationHazard) {
        this.inhalationHazard = inhalationHazard;
    }

    public Boolean getExceptedQuantity() {
        return exceptedQuantity;
    }

    public void setExceptedQuantity(Boolean exceptedQuantity) {
        this.exceptedQuantity = exceptedQuantity;
    }

    public Boolean getLimitedQuantity() {
        return limitedQuantity;
    }

    public void setLimitedQuantity(Boolean limitedQuantity) {
        this.limitedQuantity = limitedQuantity;
    }

    public Boolean getReportableQuantity() {
        return reportableQuantity;
    }

    public void setReportableQuantity(Boolean reportableQuantity) {
        this.reportableQuantity = reportableQuantity;
    }

    public String getEmsCode() {
        return emsCode;
    }

    public void setEmsCode(String emsCode) {
        this.emsCode = emsCode;
    }

    public String getPackingGroupCode() {
        return packingGroupCode;
    }

    public void setPackingGroupCode(String packingGroupCode) {
        this.packingGroupCode = packingGroupCode;
    }

    public String getHazmatDeclarations() {
        return hazmatDeclarations;
    }

    public void setHazmatDeclarations(String hazmatDeclarations) {
        this.hazmatDeclarations = hazmatDeclarations;
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

    public LclContact getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(LclContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public LclBlPiece getLclBlPiece() {
        return lclBlPiece;
    }

    public void setLclBlPiece(LclBlPiece lclBlPiece) {
        this.lclBlPiece = lclBlPiece;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public BigDecimal getTotalGrossWeight() {
        return totalGrossWeight;
    }

    public void setTotalGrossWeight(BigDecimal totalGrossWeight) {
        this.totalGrossWeight = totalGrossWeight;
    }

    public BigDecimal getTotalNetWeight() {
        return totalNetWeight;
    }

    public void setTotalNetWeight(BigDecimal totalNetWeight) {
        this.totalNetWeight = totalNetWeight;
    }

    public String getLiquidVolumeLitreorGals() {
        return liquidVolumeLitreorGals;
    }

    public void setLiquidVolumeLitreorGals(String liquidVolumeLitreorGals) {
        this.liquidVolumeLitreorGals = liquidVolumeLitreorGals;
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
        if (!(object instanceof LclBlHazmat)) {
            return false;
        }
        LclBlHazmat other = (LclBlHazmat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclBlHazmat[id=" + id + "]";
    }
}
