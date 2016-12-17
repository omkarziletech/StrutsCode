/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Rajesh
 */
@Entity
@Table(name = "bl_info")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "BlInfo.findAll", query = "SELECT b FROM BlInfo b"),
    @NamedQuery(name = "BlInfo.findById", query = "SELECT b FROM BlInfo b WHERE b.id = :id"),
    @NamedQuery(name = "BlInfo.findByBlNo", query = "SELECT b FROM BlInfo b WHERE b.blNo = :blNo"),
    @NamedQuery(name = "BlInfo.findByThirdpartyBl", query = "SELECT b FROM BlInfo b WHERE b.thirdpartyBl = :thirdpartyBl"),
    @NamedQuery(name = "BlInfo.findByShipperNad", query = "SELECT b FROM BlInfo b WHERE b.shipperNad = :shipperNad"),
    @NamedQuery(name = "BlInfo.findByConsRef", query = "SELECT b FROM BlInfo b WHERE b.consRef = :consRef"),
    @NamedQuery(name = "BlInfo.findByConsNad", query = "SELECT b FROM BlInfo b WHERE b.consNad = :consNad"),
    @NamedQuery(name = "BlInfo.findByNotify1Nad", query = "SELECT b FROM BlInfo b WHERE b.notify1Nad = :notify1Nad"),
    @NamedQuery(name = "BlInfo.findByNotify2Nad", query = "SELECT b FROM BlInfo b WHERE b.notify2Nad = :notify2Nad"),
    @NamedQuery(name = "BlInfo.findByPrecarriageBy", query = "SELECT b FROM BlInfo b WHERE b.precarriageBy = :precarriageBy"),
    @NamedQuery(name = "BlInfo.findByPlaceOfReceipt", query = "SELECT b FROM BlInfo b WHERE b.placeOfReceipt = :placeOfReceipt"),
    @NamedQuery(name = "BlInfo.findByPolDesc", query = "SELECT b FROM BlInfo b WHERE b.polDesc = :polDesc"),
    @NamedQuery(name = "BlInfo.findByPolUncode", query = "SELECT b FROM BlInfo b WHERE b.polUncode = :polUncode"),
    @NamedQuery(name = "BlInfo.findByPodDesc", query = "SELECT b FROM BlInfo b WHERE b.podDesc = :podDesc"),
    @NamedQuery(name = "BlInfo.findByPodUncode", query = "SELECT b FROM BlInfo b WHERE b.podUncode = :podUncode"),
    @NamedQuery(name = "BlInfo.findByPoddeliveryDesc", query = "SELECT b FROM BlInfo b WHERE b.poddeliveryDesc = :poddeliveryDesc"),
    @NamedQuery(name = "BlInfo.findByPoddeliveryUncode", query = "SELECT b FROM BlInfo b WHERE b.poddeliveryUncode = :poddeliveryUncode"),
    @NamedQuery(name = "BlInfo.findByShipBoardDate", query = "SELECT b FROM BlInfo b WHERE b.shipBoardDate = :shipBoardDate"),
    @NamedQuery(name = "BlInfo.findByPrepaidCollect", query = "SELECT b FROM BlInfo b WHERE b.prepaidCollect = :prepaidCollect"),
    @NamedQuery(name = "BlInfo.findByDocsPlace", query = "SELECT b FROM BlInfo b WHERE b.docsPlace = :docsPlace"),
    @NamedQuery(name = "BlInfo.findByDocsDate", query = "SELECT b FROM BlInfo b WHERE b.docsDate = :docsDate"),
    @NamedQuery(name = "BlInfo.findByNoDocsOriginal", query = "SELECT b FROM BlInfo b WHERE b.noDocsOriginal = :noDocsOriginal"),
    @NamedQuery(name = "BlInfo.findByNoDocsCopies", query = "SELECT b FROM BlInfo b WHERE b.noDocsCopies = :noDocsCopies"),
    @NamedQuery(name = "BlInfo.findByAmsScac", query = "SELECT b FROM BlInfo b WHERE b.amsScac = :amsScac"),
    @NamedQuery(name = "BlInfo.findByAmsRef", query = "SELECT b FROM BlInfo b WHERE b.amsRef = :amsRef")})
public class BlInfo extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "bl_no")
    private String blNo;
    @Column(name = "thirdparty_bl")
    private String thirdpartyBl;
    @Column(name = "manual_shipper")
    private boolean manualShipper;
    @Column(name = "shipper_code")
    private String shipperCode;
    @Column(name = "shipper_nad")
    private String shipperNad;
    @Column(name = "cons_ref")
    private String consRef;
    @Column(name = "manual_cons")
    private boolean manualCons;
    @Column(name = "cons_code")
    private String consCode;
    @Column(name = "cons_nad")
    private String consNad;
    @Column(name = "manual_notify1")
    private boolean manualNotify1;
    @Column(name = "notify1_code")
    private String notify1Code;
    @Column(name = "notify1_nad")
    private String notify1Nad;
    @Column(name = "manual_notify2")
    private boolean manualNotify2;
    @Column(name = "notify2_code")
    private String notify2Code;
    @Column(name = "notify2_nad")
    private String notify2Nad;
    @Column(name = "precarriage_by")
    private String precarriageBy;
    @Column(name = "por_uncode")
    private String porUncode;
    @Column(name = "place_of_receipt")
    private String placeOfReceipt;
    @Column(name = "pol_desc")
    private String polDesc;
    @Column(name = "pol_uncode")
    private String polUncode;
    @Column(name = "pod_desc")
    private String podDesc;
    @Column(name = "pod_uncode")
    private String podUncode;
    @Column(name = "poddelivery_desc")
    private String poddeliveryDesc;
    @Column(name = "poddelivery_uncode")
    private String poddeliveryUncode;
    @Lob
    @Column(name = "body_bl")
    private String bodyBl;
    @Lob
    @Column(name = "special_instructions")
    private String specialInstructions;
    @Lob
    @Column(name = "freight_details")
    private String freightDetails;
    @Column(name = "ship_board_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shipBoardDate;
    @Basic(optional = false)
    @Column(name = "prepaid_collect")
    private String prepaidCollect;
    @Column(name = "docs_place")
    private String docsPlace;
    @Column(name = "docs_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date docsDate;
    @Column(name = "no_docs_original")
    private Integer noDocsOriginal;
    @Column(name = "no_docs_copies")
    private Integer noDocsCopies;
    @Column(name = "ams_scac")
    private String amsScac;
    @Column(name = "ams_ref")
    private String amsRef;
    @JoinColumn(name = "container_info_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ContainerInfo containerInfo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "blInfo", fetch = FetchType.EAGER)
    private List<CargoDetails> cargoDetailsCollection;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne
    private LclFileNumber fileNo;
    @Column(name = "is_approved")
    private boolean approved;
    @Column(name = "updated_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id")
    @ManyToOne
    private User updatedByUser;
    @Column(name = "created_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    @ManyToOne
    private User createdByUser;
    @Basic(optional = false)
    @Column(name = "adjudicated")
    private boolean adjudicated = false;

    
    public BlInfo() {
    }

    public BlInfo(Long id) {
        this.id = id;
    }

    public BlInfo(Long id, String prepaidCollect) {
        this.id = id;
        this.prepaidCollect = prepaidCollect;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlNo() {
        return blNo;
    }

    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    public String getThirdpartyBl() {
        return thirdpartyBl;
    }

    public void setThirdpartyBl(String thirdpartyBl) {
        this.thirdpartyBl = thirdpartyBl;
    }

    public String getShipperNad() {
        return shipperNad;
    }

    public void setShipperNad(String shipperNad) {
        this.shipperNad = shipperNad;
    }

    public String getConsRef() {
        return consRef;
    }

    public void setConsRef(String consRef) {
        this.consRef = consRef;
    }

    public String getConsNad() {
        return consNad;
    }

    public void setConsNad(String consNad) {
        this.consNad = consNad;
    }

    public String getNotify1Nad() {
        return notify1Nad;
    }

    public void setNotify1Nad(String notify1Nad) {
        this.notify1Nad = notify1Nad;
    }

    public String getNotify2Nad() {
        return notify2Nad;
    }

    public void setNotify2Nad(String notify2Nad) {
        this.notify2Nad = notify2Nad;
    }

    public String getPrecarriageBy() {
        return precarriageBy;
    }

    public void setPrecarriageBy(String precarriageBy) {
        this.precarriageBy = precarriageBy;
    }

    public String getPlaceOfReceipt() {
        return placeOfReceipt;
    }

    public void setPlaceOfReceipt(String placeOfReceipt) {
        this.placeOfReceipt = placeOfReceipt;
    }

    public String getPolDesc() {
        return polDesc;
    }

    public void setPolDesc(String polDesc) {
        this.polDesc = polDesc;
    }

    public String getPolUncode() {
        return polUncode;
    }

    public void setPolUncode(String polUncode) {
        this.polUncode = polUncode;
    }

    public String getPodDesc() {
        return podDesc;
    }

    public void setPodDesc(String podDesc) {
        this.podDesc = podDesc;
    }

    public String getPodUncode() {
        return podUncode;
    }

    public void setPodUncode(String podUncode) {
        this.podUncode = podUncode;
    }

    public String getPoddeliveryDesc() {
        return poddeliveryDesc;
    }

    public void setPoddeliveryDesc(String poddeliveryDesc) {
        this.poddeliveryDesc = poddeliveryDesc;
    }

    public String getPoddeliveryUncode() {
        return poddeliveryUncode;
    }

    public void setPoddeliveryUncode(String poddeliveryUncode) {
        this.poddeliveryUncode = poddeliveryUncode;
    }

    public String getBodyBl() {
        return bodyBl;
    }

    public void setBodyBl(String bodyBl) {
        this.bodyBl = bodyBl;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getFreightDetails() {
        return freightDetails;
    }

    public void setFreightDetails(String freightDetails) {
        this.freightDetails = freightDetails;
    }

    public Date getShipBoardDate() {
        return shipBoardDate;
    }

    public void setShipBoardDate(Date shipBoardDate) {
        this.shipBoardDate = shipBoardDate;
    }

    public String getPrepaidCollect() {
        return prepaidCollect;
    }

    public void setPrepaidCollect(String prepaidCollect) {
        this.prepaidCollect = prepaidCollect;
    }

    public String getDocsPlace() {
        return docsPlace;
    }

    public void setDocsPlace(String docsPlace) {
        this.docsPlace = docsPlace;
    }

    public Date getDocsDate() {
        return docsDate;
    }

    public void setDocsDate(Date docsDate) {
        this.docsDate = docsDate;
    }

    public Integer getNoDocsOriginal() {
        return noDocsOriginal;
    }

    public void setNoDocsOriginal(Integer noDocsOriginal) {
        this.noDocsOriginal = noDocsOriginal;
    }

    public Integer getNoDocsCopies() {
        return noDocsCopies;
    }

    public void setNoDocsCopies(Integer noDocsCopies) {
        this.noDocsCopies = noDocsCopies;
    }

    public String getAmsScac() {
        return amsScac;
    }

    public void setAmsScac(String amsScac) {
        this.amsScac = amsScac;
    }

    public String getAmsRef() {
        return amsRef;
    }

    public void setAmsRef(String amsRef) {
        this.amsRef = amsRef;
    }

    public ContainerInfo getContainerInfo() {
        return containerInfo;
    }

    public void setContainerInfo(ContainerInfo containerInfo) {
        this.containerInfo = containerInfo;
    }

    public List<CargoDetails> getCargoDetailsCollection() {
        return cargoDetailsCollection;
    }

    public void setCargoDetailsCollection(List<CargoDetails> cargoDetailsCollection) {
        this.cargoDetailsCollection = cargoDetailsCollection;
    }

    public String getConsCode() {
        return consCode;
    }

    public void setConsCode(String consCode) {
        this.consCode = consCode;
    }

    public String getNotify1Code() {
        return notify1Code;
    }

    public void setNotify1Code(String notify1Code) {
        this.notify1Code = notify1Code;
    }

    public String getNotify2Code() {
        return notify2Code;
    }

    public void setNotify2Code(String notify2Code) {
        this.notify2Code = notify2Code;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public boolean isManualCons() {
        return manualCons;
    }

    public void setManualCons(boolean manualCons) {
        this.manualCons = manualCons;
    }

    public boolean isManualNotify1() {
        return manualNotify1;
    }

    public void setManualNotify1(boolean manualNotify1) {
        this.manualNotify1 = manualNotify1;
    }

    public boolean isManualNotify2() {
        return manualNotify2;
    }

    public void setManualNotify2(boolean manualNotify2) {
        this.manualNotify2 = manualNotify2;
    }

    public boolean isManualShipper() {
        return manualShipper;
    }

    public void setManualShipper(boolean manualShipper) {
        this.manualShipper = manualShipper;
    }

    public LclFileNumber getFileNo() {
        return fileNo;
    }

    public void setFileNo(LclFileNumber fileNo) {
        this.fileNo = fileNo;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getPorUncode() {
        return porUncode;
    }

    public void setPorUncode(String porUncode) {
        this.porUncode = porUncode;
    }

    public boolean isAdjudicated() {
        return adjudicated;
    }

    public void setAdjudicated(boolean adjudicated) {
        this.adjudicated = adjudicated;
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
        if (!(object instanceof BlInfo)) {
            return false;
        }
        BlInfo other = (BlInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ecuedi.model.BlInfo[id=" + id + "]";
    }
}
