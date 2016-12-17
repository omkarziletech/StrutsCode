/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.kn;

import com.gp.cong.hibernate.Domain;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author palraj.p
 */
@Entity
@Table(name = "kn_bkg_details")
@NamedQueries({
    @NamedQuery(name = "BookingDetail.findAll", query = "SELECT b FROM BookingDetail b")})
public class BookingDetail extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "bkg_type")
    private String bkgType;
    @Basic(optional = false)
    @Column(name = "bkg_date")
    @Temporal(TemporalType.DATE)
    private Date bkgDate;
    @Basic(optional = false)
    @Column(name = "request_type")
    private String requestType;
    @Column(name = "bkg_number")
    private String bkgNumber;
    @Column(name = "customer_control_code")
    private String customerControlCode;
    @Column(name = "communication_reference")
    private String communicationReference;
    @Column(name = "customer_reference")
    private String customerReference;
    @Column(name = "customer_contact")
    private String customerContact;
    @Column(name = "customer_phone")
    private String customerPhone;
    @Column(name = "customer_email")
    private String customerEmail;
    @Basic(optional = false)
    @Column(name = "cfs_origin")
    private String cfsOrigin;
    @Basic(optional = false)
    @Column(name = "cfs_destination")
    private String cfsDestination;
    @Basic(optional = false)
    @Column(name = "ams_flag")
    private String amsFlag;
    @Basic(optional = false)
    @Column(name = "aes_flag")
    private String aesFlag;
    @Column(name = "co_load_commodity")
    private String coLoadCommodity;
    @Lob
    @Column(name = "remarks1")
    private String remarks1;
    @Lob
    @Column(name = "remarks2")
    private String remarks2;
    @Lob
    @Column(name = "remarks3")
    private String remarks3;
    @Lob
    @Column(name = "remarks4")
    private String remarks4;
    @Lob
    @Column(name = "remarks5")
    private String remarks5;
    @Column(name = "pickup_flag")
    private String pickupFlag;
    @Column(name = "oncarriage_flag")
    private String oncarriageFlag;
    @JoinColumn(name = "envelope_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BookingEnvelope envelopeId;
    @OneToMany(mappedBy = "bkgId")
    private List<SailingDetail> sailingDetailList;
    @OneToMany(mappedBy = "bkgId")
    private List<PickupDetail> pickupDetailList;
    @OneToMany(mappedBy = "bkgId")
    private List<CargoDetail> cargoDetailList;

    public BookingDetail() {
    }

    public BookingDetail(Long id) {
        this.id = id;
    }

    public BookingDetail(Long id, String bkgType, Date bkgDate, String requestType, String cfsOrigin, String cfsDestination, String amsFlag, String aesFlag) {
        this.id = id;
        this.bkgType = bkgType;
        this.bkgDate = bkgDate;
        this.requestType = requestType;
        this.cfsOrigin = cfsOrigin;
        this.cfsDestination = cfsDestination;
        this.amsFlag = amsFlag;
        this.aesFlag = aesFlag;
    }

    public String getAesFlag() {
        return aesFlag;
    }

    public void setAesFlag(String aesFlag) {
        this.aesFlag = aesFlag;
    }

    public String getAmsFlag() {
        return amsFlag;
    }

    public void setAmsFlag(String amsFlag) {
        this.amsFlag = amsFlag;
    }

    public Date getBkgDate() {
        return bkgDate;
    }

    public void setBkgDate(Date bkgDate) {
        this.bkgDate = bkgDate;
    }

    public String getBkgNumber() {
        return bkgNumber;
    }

    public void setBkgNumber(String bkgNumber) {
        this.bkgNumber = bkgNumber;
    }

    public String getBkgType() {
        return bkgType;
    }

    public void setBkgType(String bkgType) {
        this.bkgType = bkgType;
    }

    public String getCfsDestination() {
        return cfsDestination;
    }

    public void setCfsDestination(String cfsDestination) {
        this.cfsDestination = cfsDestination;
    }

    public String getCfsOrigin() {
        return cfsOrigin;
    }

    public void setCfsOrigin(String cfsOrigin) {
        this.cfsOrigin = cfsOrigin;
    }

    public String getCoLoadCommodity() {
        return coLoadCommodity;
    }

    public void setCoLoadCommodity(String coLoadCommodity) {
        this.coLoadCommodity = coLoadCommodity;
    }

    public String getCommunicationReference() {
        return communicationReference;
    }

    public void setCommunicationReference(String communicationReference) {
        this.communicationReference = communicationReference;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerControlCode() {
        return customerControlCode;
    }

    public void setCustomerControlCode(String customerControlCode) {
        this.customerControlCode = customerControlCode;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public BookingEnvelope getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(BookingEnvelope envelopeId) {
        this.envelopeId = envelopeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOncarriageFlag() {
        return oncarriageFlag;
    }

    public void setOncarriageFlag(String oncarriageFlag) {
        this.oncarriageFlag = oncarriageFlag;
    }

    public String getPickupFlag() {
        return pickupFlag;
    }

    public void setPickupFlag(String pickupFlag) {
        this.pickupFlag = pickupFlag;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getRemarks3() {
        return remarks3;
    }

    public void setRemarks3(String remarks3) {
        this.remarks3 = remarks3;
    }

    public String getRemarks4() {
        return remarks4;
    }

    public void setRemarks4(String remarks4) {
        this.remarks4 = remarks4;
    }

    public String getRemarks5() {
        return remarks5;
    }

    public void setRemarks5(String remarks5) {
        this.remarks5 = remarks5;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<SailingDetail> getSailingDetailList() {
        return sailingDetailList;
    }

    public void setSailingDetailList(List<SailingDetail> sailingDetailList) {
        this.sailingDetailList = sailingDetailList;
    }

    public List<PickupDetail> getPickupDetailList() {
        return pickupDetailList;
    }

    public void setPickupDetailList(List<PickupDetail> pickupDetailList) {
        this.pickupDetailList = pickupDetailList;
    }

    public List<CargoDetail> getCargoDetailList() {
        return cargoDetailList;
    }

    public void setCargoDetailList(List<CargoDetail> cargoDetailList) {
        this.cargoDetailList = cargoDetailList;
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
        if (!(object instanceof BookingDetail)) {
            return false;
        }
        BookingDetail other = (BookingDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.kn.BookingDetail[ id=" + id + " ]";
    }
}
