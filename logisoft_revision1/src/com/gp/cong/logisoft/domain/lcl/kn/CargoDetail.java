/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.kn;

import com.gp.cong.hibernate.Domain;
import java.math.BigDecimal;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.print.DocFlavor.STRING;

/**
 *
 * @author palraj.p
 */
@Entity
@Table(name = "kn_cargo_details")
@NamedQueries({
    @NamedQuery(name = "CargoDetail.findAll", query = "SELECT c FROM CargoDetail c")})
public class CargoDetail extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "pieces")
    private String pieces;
    @Column(name = "package")
    private String package1;
    @Column(name = "cm1")
    private String cm1;
    @Column(name = "cm2")
    private String cm2;
    @Column(name = "cm3")
    private String cm3;
    @Column(name = "cm4")
    private String cm4;
    @Column(name = "hs_code")
    private String hsCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "volume")
    private BigDecimal volume;
    @Basic(optional = false)
    @Column(name = "uom")
    private String uom;
    @Basic(optional = false)
    @Column(name = "haz_flag")
    private String hazFlag;
    @Column(name = "cargo_id")
    private String cargoId;
    @Column(name = "shipping_marks1")
    private String shippingMarks1;
    @Column(name = "shipping_marks2")
    private String shippingMarks2;
    @Column(name = "shipping_marks3")
    private String shippingMarks3;
    @Column(name = "shipping_marks4")
    private String shippingMarks4;
    @Column(name = "shipping_marks5")
    private String shippingMarks5;
    @OneToMany(mappedBy = "cargoId")
    private List<HazDetail> hazDetailList;
    @JoinColumn(name = "bkg_id", referencedColumnName = "id")
    @ManyToOne
    private BookingDetail bkgId;

    public CargoDetail() {
    }

    public CargoDetail(Long id) {
        this.id = id;
    }

    public CargoDetail(Long id, String pieces, String uom, String hazFlag) {
        this.id = id;
        this.pieces = pieces;
        this.uom = uom;
        this.hazFlag = hazFlag;
    }

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getCm1() {
        return cm1;
    }

    public void setCm1(String cm1) {
        this.cm1 = cm1;
    }

    public String getCm2() {
        return cm2;
    }

    public void setCm2(String cm2) {
        this.cm2 = cm2;
    }

    public String getCm3() {
        return cm3;
    }

    public void setCm3(String cm3) {
        this.cm3 = cm3;
    }

    public String getCm4() {
        return cm4;
    }

    public void setCm4(String cm4) {
        this.cm4 = cm4;
    }

    public String getHazFlag() {
        return hazFlag;
    }

    public void setHazFlag(String hazFlag) {
        this.hazFlag = hazFlag;
    }

    public String getHsCode() {
        return hsCode;
    }

    public void setHsCode(String hsCode) {
        this.hsCode = hsCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackage1() {
        return package1;
    }

    public void setPackage1(String package1) {
        this.package1 = package1;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getShippingMarks1() {
        return shippingMarks1;
    }

    public void setShippingMarks1(String shippingMarks1) {
        this.shippingMarks1 = shippingMarks1;
    }

    public String getShippingMarks2() {
        return shippingMarks2;
    }

    public void setShippingMarks2(String shippingMarks2) {
        this.shippingMarks2 = shippingMarks2;
    }

    public String getShippingMarks3() {
        return shippingMarks3;
    }

    public void setShippingMarks3(String shippingMarks3) {
        this.shippingMarks3 = shippingMarks3;
    }

    public String getShippingMarks4() {
        return shippingMarks4;
    }

    public void setShippingMarks4(String shippingMarks4) {
        this.shippingMarks4 = shippingMarks4;
    }

    public String getShippingMarks5() {
        return shippingMarks5;
    }

    public void setShippingMarks5(String shippingMarks5) {
        this.shippingMarks5 = shippingMarks5;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public List<HazDetail> getHazDetailList() {
        return hazDetailList;
    }

    public void setHazDetailList(List<HazDetail> hazDetailList) {
        this.hazDetailList = hazDetailList;
    }

    public BookingDetail getBkgId() {
        return bkgId;
    }

    public void setBkgId(BookingDetail bkgId) {
        this.bkgId = bkgId;
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
        if (!(object instanceof CargoDetail)) {
            return false;
        }
        CargoDetail other = (CargoDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.kn.CargoDetail[ id=" + id + " ]";
    }
}
