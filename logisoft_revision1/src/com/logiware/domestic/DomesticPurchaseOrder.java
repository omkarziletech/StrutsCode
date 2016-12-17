package com.logiware.domestic;

import com.gp.cong.hibernate.Domain;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Shanmugam
 */
@Entity
@Table(name = "domestic_purchase_order")
public class DomesticPurchaseOrder extends Domain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "purchase_order_no")
    private String purchaseOrderNo;
    @Column(name = "package_type")
    private String packageType;
    @Column(name = "package_quantity")
    private Integer packageQuantity;
    @Column(name = "weight")
    private double weight;
    @Column(name = "pallet_slip")
    private boolean palletSlip;
    @Column(name = "extra_info")
    private String extraInfo;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "description")
    private String description;
    @Column(name = "hazmat")
    private boolean hazmat;
    @Column(name = "hazmat_number")
    private String hazmatNumber;
    @Column(name = "nmfc")
    private String nmfc;
    @Column(name = "class")
    private String classes;
    @Column(name = "handling_unit_type")
    private String handlingUnitType;
    @Column(name = "handling_unit_quantity")
    private Integer handlingUnitQuantity;
    @Column(name = "line_length")
    private Integer length;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Column(name = "cube")
    private double cube;
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DomesticBooking bookingId;
    @JoinColumn(name = "rate_quote_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DomesticRateQuote domesticRateQuote;

    public DomesticBooking getBookingId() {
        return bookingId;
    }

    public void setBookingId(DomesticBooking bookingId) {
        this.bookingId = bookingId;
    }

    public String getClasses() {
        return null != classes?classes:"";
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public double getCube() {
        return cube;
    }

    public void setCube(double cube) {
        this.cube = cube;
    }

    public String getDescription() {
        return null != description?description:"";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Integer getHandlingUnitQuantity() {
        return null != handlingUnitQuantity?handlingUnitQuantity:0;
    }

    public void setHandlingUnitQuantity(Integer handlingUnitQuantity) {
        this.handlingUnitQuantity = handlingUnitQuantity;
    }

    public String getHandlingUnitType() {
        return null != handlingUnitType?handlingUnitType:"";
    }

    public void setHandlingUnitType(String handlingUnitType) {
        this.handlingUnitType = handlingUnitType;
    }

    public boolean isHazmat() {
        return hazmat;
    }

    public void setHazmat(boolean hazmat) {
        this.hazmat = hazmat;
    }

    public String getHazmatNumber() {
        return null != hazmatNumber?hazmatNumber:"";
    }

    public void setHazmatNumber(String hazmatNumber) {
        this.hazmatNumber = hazmatNumber;
    }

    public Integer getHeight() {
        return null != height?height:0;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLength() {
        return null != length?length:0;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getNmfc() {
        return null != nmfc?nmfc:"";
    }

    public void setNmfc(String nmfc) {
        this.nmfc = nmfc;
    }

    public Integer getPackageQuantity() {
        return null != packageQuantity?packageQuantity:0;
    }

    public void setPackageQuantity(Integer packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public String getPackageType() {
        return null != packageType?packageType:"";
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public boolean isPalletSlip() {
        return palletSlip;
    }

    public void setPalletSlip(boolean palletSlip) {
        this.palletSlip = palletSlip;
    }

    public String getProductName() {
        return null != productName?productName:"";
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Integer getWidth() {
        return null != width?width:0;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public DomesticRateQuote getDomesticRateQuote() {
        return domesticRateQuote;
    }

    public void setDomesticRateQuote(DomesticRateQuote domesticRateQuote) {
        this.domesticRateQuote = domesticRateQuote;
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
	if (!(object instanceof DomesticPurchaseOrder)) {
	    return false;
	}
	DomesticPurchaseOrder other = (DomesticPurchaseOrder) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }
}
