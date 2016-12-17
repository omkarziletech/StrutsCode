/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.edi.tracking;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PALRAJ
 */
@Entity
@Table(name = "kn_shipping_instruction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnShippingInstruction.findAll", query = "SELECT k FROM KnShippingInstruction k")})
public class KnShippingInstruction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "sender_id")
    private String senderId;
    @Basic(optional = false)
    @Column(name = "receiver_id")
    private String receiverId;
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "version")
    private String version;
    @Basic(optional = false)
    @Column(name = "envelope_id")
    private String envelopeId;
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
    @Column(name = "transport_type")
    private String transportType;
    @Column(name = "obl_type")
    private String oblType;
    @Column(name = "obl_number")
    private String oblNumber;
    @Column(name = "number_of_originals")
    private String numberOfOriginals;
    @Column(name = "number_of_copies")
    private String numberOfCopies;
    @Column(name = "fpi")
    private String fpi;
    @Column(name = "place_of_issue")
    private String placeOfIssue;
    @Column(name = "date_of_issue")
    @Temporal(TemporalType.DATE)
    private Date dateOfIssue;
    @Column(name = "ff_customer_name")
    private String ffCustomerName;
    @Lob
    @Column(name = "ff_address")
    private String ffAddress;
    @Column(name = "sh_customer_name")
    private String shCustomerName;
    @Lob
    @Column(name = "sh_address")
    private String shAddress;
    @Column(name = "cn_customer_name")
    private String cnCustomerName;
    @Lob
    @Column(name = "cn_address")
    private String cnAddress;
    @Column(name = "ni_customer_name")
    private String niCustomerName;
    @Lob
    @Column(name = "ni_address")
    private String niAddress;
    @Column(name = "vessel_voyage_id")
    private String vesselVoyageId;
    @Basic(optional = false)
    @Column(name = "vessel_name")
    private String vesselName;
    @Basic(optional = false)
    @Column(name = "etd")
    @Temporal(TemporalType.DATE)
    private Date etd;
    @Basic(optional = false)
    @Column(name = "eta")
    @Temporal(TemporalType.DATE)
    private Date eta;
    @Basic(optional = false)
    @Column(name = "voyage")
    private String voyage;
    @Column(name = "place_of_receipt")
    private String placeOfReceipt;
    @Basic(optional = false)
    @Column(name = "cfs_origin")
    private String cfsOrigin;
    @Column(name = "port_of_loading")
    private String portOfLoading;
    @Column(name = "port_of_discharge")
    private String portOfDischarge;
    @Basic(optional = false)
    @Column(name = "cfs_dtination")
    private String cfsDtination;
    @Column(name = "place_of_delivery")
    private String placeOfDelivery;
    @Column(name = "cargo_id")
    private String cargoId;
    @Column(name = "level")
    private String level;
    @Column(name = "package_count")
    private Integer packageCount;
    @Column(name = "packag_type")
    private String packagType;
    @Basic(optional = false)
    @Column(name = "uom")
    private String uom;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "volume")
    private BigDecimal volume;
    @Lob
    @Column(name = "commodity")
    private String commodity;
    @Lob
    @Column(name = "marks")
    private String marks;
    @Basic(optional = false)
    @Column(name = "hazardous_flag")
    private String hazardousFlag;
    @Column(name = "obl_request_file_name")
    private String oblRequestFileName;
    @Lob
    @Column(name = "obl_request_file")
    private byte[] oblRequestFile;

    public KnShippingInstruction() {
    }

    public KnShippingInstruction(Long id) {
        this.id = id;
    }

    public KnShippingInstruction(Long id, String senderId, String receiverId, String version, String envelopeId, String vesselName, Date etd, Date eta, String voyage, String cfsOrigin, String cfsDtination, String uom, String hazardousFlag) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.version = version;
        this.envelopeId = envelopeId;
        this.vesselName = vesselName;
        this.etd = etd;
        this.eta = eta;
        this.voyage = voyage;
        this.cfsOrigin = cfsOrigin;
        this.cfsDtination = cfsDtination;
        this.uom = uom;
        this.hazardousFlag = hazardousFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(String envelopeId) {
        this.envelopeId = envelopeId;
    }

    public String getBkgNumber() {
        return bkgNumber;
    }

    public void setBkgNumber(String bkgNumber) {
        this.bkgNumber = bkgNumber;
    }

    public String getCustomerControlCode() {
        return customerControlCode;
    }

    public void setCustomerControlCode(String customerControlCode) {
        this.customerControlCode = customerControlCode;
    }

    public String getCommunicationReference() {
        return communicationReference;
    }

    public void setCommunicationReference(String communicationReference) {
        this.communicationReference = communicationReference;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getOblType() {
        return oblType;
    }

    public void setOblType(String oblType) {
        this.oblType = oblType;
    }

    public String getOblNumber() {
        return oblNumber;
    }

    public void setOblNumber(String oblNumber) {
        this.oblNumber = oblNumber;
    }

    public String getNumberOfOriginals() {
        return numberOfOriginals;
    }

    public void setNumberOfOriginals(String numberOfOriginals) {
        this.numberOfOriginals = numberOfOriginals;
    }

    public String getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(String numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public String getFpi() {
        return fpi;
    }

    public void setFpi(String fpi) {
        this.fpi = fpi;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getFfCustomerName() {
        return ffCustomerName;
    }

    public void setFfCustomerName(String ffCustomerName) {
        this.ffCustomerName = ffCustomerName;
    }

    public String getFfAddress() {
        return ffAddress;
    }

    public void setFfAddress(String ffAddress) {
        this.ffAddress = ffAddress;
    }

    public String getShCustomerName() {
        return shCustomerName;
    }

    public void setShCustomerName(String shCustomerName) {
        this.shCustomerName = shCustomerName;
    }

    public String getShAddress() {
        return shAddress;
    }

    public void setShAddress(String shAddress) {
        this.shAddress = shAddress;
    }

    public String getCnCustomerName() {
        return cnCustomerName;
    }

    public void setCnCustomerName(String cnCustomerName) {
        this.cnCustomerName = cnCustomerName;
    }

    public String getCnAddress() {
        return cnAddress;
    }

    public void setCnAddress(String cnAddress) {
        this.cnAddress = cnAddress;
    }

    public String getNiCustomerName() {
        return niCustomerName;
    }

    public void setNiCustomerName(String niCustomerName) {
        this.niCustomerName = niCustomerName;
    }

    public String getNiAddress() {
        return niAddress;
    }

    public void setNiAddress(String niAddress) {
        this.niAddress = niAddress;
    }

    public String getVesselVoyageId() {
        return vesselVoyageId;
    }

    public void setVesselVoyageId(String vesselVoyageId) {
        this.vesselVoyageId = vesselVoyageId;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public String getPlaceOfReceipt() {
        return placeOfReceipt;
    }

    public void setPlaceOfReceipt(String placeOfReceipt) {
        this.placeOfReceipt = placeOfReceipt;
    }

    public String getCfsOrigin() {
        return cfsOrigin;
    }

    public void setCfsOrigin(String cfsOrigin) {
        this.cfsOrigin = cfsOrigin;
    }

    public String getPortOfLoading() {
        return portOfLoading;
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public String getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(String portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public String getCfsDtination() {
        return cfsDtination;
    }

    public void setCfsDtination(String cfsDtination) {
        this.cfsDtination = cfsDtination;
    }

    public String getPlaceOfDelivery() {
        return placeOfDelivery;
    }

    public void setPlaceOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
    }

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(Integer packageCount) {
        this.packageCount = packageCount;
    }

    public String getPackagType() {
        return packagType;
    }

    public void setPackagType(String packagType) {
        this.packagType = packagType;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getHazardousFlag() {
        return hazardousFlag;
    }

    public void setHazardousFlag(String hazardousFlag) {
        this.hazardousFlag = hazardousFlag;
    }

    public String getOblRequestFileName() {
        return oblRequestFileName;
    }

    public void setOblRequestFileName(String oblRequestFileName) {
        this.oblRequestFileName = oblRequestFileName;
    }

    public byte[] getOblRequestFile() {
        return oblRequestFile;
    }

    public void setOblRequestFile(byte[] oblRequestFile) {
        this.oblRequestFile = oblRequestFile;
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
        if (!(object instanceof KnShippingInstruction)) {
            return false;
        }
        KnShippingInstruction other = (KnShippingInstruction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.logiware.obl.kn.persistence.model.KnShippingInstruction[ id=" + id + " ]";
    }
    
}
