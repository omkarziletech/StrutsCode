/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAc;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlAir;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlHazmat;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlHotCode;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlImport;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPlan;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_file_number")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclFileNumber extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "file_number")
    private String fileNumber;
    @Basic(optional = false)
    @Column(name = "state")
    private String state;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "previous_status")
    private String previousStatus;
    @Basic(optional = false)
    @Column(name = "created_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDatetime;
    @Basic(optional = true)
    @Column(name = "short_ship")
    private boolean shortShip = false;
    @Column(name = "short_ship_sequence")
    private Integer shortShipSequence = 0;
    @Column(name = "datasource")
    private String datasource;
    @Column(name = "business_unit")
    private String businessUnit;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lclFileNumber")
    private List<LclBookingPiece> lclBookingPieceList;
    
    @OneToOne(mappedBy = "lclFileNumber")
    private LclBookingImport lclBookingImport;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<Lcl3pRefNo> lcl3pRefNoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingAc> lclBookingAcList;
    @OneToOne(mappedBy = "lclFileNumber", fetch = FetchType.LAZY)
    private LclBooking lclBooking;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private LclBookingExport lclBookingExport;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingHotCode> lclBookingHotCodeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingHazmat> lclBookingHazmatList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclInbond> lclInbondList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclContact> lclContactList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclRemarks> lclRemarksList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private LclBookingAir lclBookingAir;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingPlan> lclBookingPlanList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumberA")
    private List<LclConsolidate> lclConsolidatesAList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumberB")
    private List<LclConsolidate> lclConsolidatesBList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingPad> lclBookingPadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingImportAms> lclBookingImportAmsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingAcTrans> lclBookingAcTransList;
    //Quotation domain classes
    @OneToOne(mappedBy = "lclFileNumber", fetch = FetchType.LAZY)
    private LclQuote lclQuote;    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private LclQuoteAir lclQuoteAir;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclQuoteHotCode> lclQuoteHotCodeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclQuoteAc> lclQuoteAcList;
    @OneToOne(mappedBy = "lclFileNumber")
    private LclQuoteImport lclQuoteImport;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclContact> lclQuoteContactList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclQuoteHazmat> lclQuoteHazmatList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclQuotePiece> lclQuotePieceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclQuotePlan> lclQuotePlanList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclQuotePad> lclQuotePadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclQuoteImportAms> lclQuoteImportAmsList;
    //BL Mapping
    @OneToOne(mappedBy = "lclFileNumber", fetch = FetchType.LAZY)
    private LclBl lclBl;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private LclBlAir lclBlAir;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBlHotCode> lclBlHotCodeList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBlAc> lclBlAcList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private LclBlImport lclBlImport;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclContact> lclBlContactList;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lclFileNumber")
    private List<LclBlHazmat> lclBlHazmatList;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lclFileNumber")
    private List<LclBlPiece> lclBlPieceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBlPlan> lclBlPlanList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingHsCode> lclBookingHsCodeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclQuoteHsCode> lclQuoteHsCodeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclBookingDispo> lclBookingDispoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclFileNumber")
    private List<LclOptions> lclOptionsList;

    public LclFileNumber() {
    }

    public LclFileNumber(Long id) {
        this.id = id;
    }

    public LclFileNumber(Long id, String fileNumber, String state, String status, Date createdDatetime) {
        this.id = id;
        this.fileNumber = fileNumber;
        this.state = state;
        this.status = status;
        this.createdDatetime = createdDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equalsIgnoreCase(this.status)) {
            this.previousStatus = this.status;
        }
        this.status = status;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public LclBookingImport getLclBookingImport() {
        return lclBookingImport;
    }

    public void setLclBookingImport(LclBookingImport lclBookingImport) {
        this.lclBookingImport = lclBookingImport;
    }

    public LclBooking getLclBooking() {
        return lclBooking;
    }

    public void setLclBooking(LclBooking lclBooking) {
        this.lclBooking = lclBooking;
    }

    public List<Lcl3pRefNo> getLcl3pRefNoList() {
        return lcl3pRefNoList;
    }

    public void setLcl3pRefNoList(List<Lcl3pRefNo> lcl3pRefNoList) {
        this.lcl3pRefNoList = lcl3pRefNoList;
    }

    public List<LclBookingAc> getLclBookingAcList() {
        return lclBookingAcList;
    }

    public void setLclBookingAcList(List<LclBookingAc> lclBookingAcList) {
        this.lclBookingAcList = lclBookingAcList;
    }

    public LclBookingAir getLclBookingAir() {
        return lclBookingAir;
    }

    public void setLclBookingAir(LclBookingAir lclBookingAir) {
        this.lclBookingAir = lclBookingAir;
    }

    public List<LclBookingHazmat> getLclBookingHazmatList() {
        return lclBookingHazmatList;
    }

    public void setLclBookingHazmatList(List<LclBookingHazmat> lclBookingHazmatList) {
        this.lclBookingHazmatList = lclBookingHazmatList;
    }

    public List<LclBookingHotCode> getLclBookingHotCodeList() {
        return lclBookingHotCodeList;
    }

    public void setLclBookingHotCodeList(List<LclBookingHotCode> lclBookingHotCodeList) {
        this.lclBookingHotCodeList = lclBookingHotCodeList;
    }

    public List<LclBookingPiece> getLclBookingPieceList() {
        return lclBookingPieceList;
    }

    public void setLclBookingPieceList(List<LclBookingPiece> lclBookingPieceList) {
        this.lclBookingPieceList = lclBookingPieceList;
    }

    public List<LclBookingPlan> getLclBookingPlanList() {
        return lclBookingPlanList;
    }

    public void setLclBookingPlanList(List<LclBookingPlan> lclBookingPlanList) {
        this.lclBookingPlanList = lclBookingPlanList;
    }

    public List<LclContact> getLclContactList() {
        return lclContactList;
    }

    public void setLclContactList(List<LclContact> lclContactList) {
        this.lclContactList = lclContactList;
    }

    public List<LclInbond> getLclInbondList() {
        return lclInbondList;
    }

    public void setLclInbondList(List<LclInbond> lclInbondList) {
        this.lclInbondList = lclInbondList;
    }

    public List<LclRemarks> getLclRemarksList() {
        return lclRemarksList;
    }

    public void setLclRemarksList(List<LclRemarks> lclRemarksList) {
        this.lclRemarksList = lclRemarksList;
    }

    public List<LclConsolidate> getLclConsolidatesAList() {
        return lclConsolidatesAList;
    }

    public void setLclConsolidatesAList(List<LclConsolidate> lclConsolidatesAList) {
        this.lclConsolidatesAList = lclConsolidatesAList;
    }

    public List<LclConsolidate> getLclConsolidatesBList() {
        return lclConsolidatesBList;
    }

    public void setLclConsolidatesBList(List<LclConsolidate> lclConsolidatesBList) {
        this.lclConsolidatesBList = lclConsolidatesBList;
    }

    public List<LclBookingPad> getLclBookingPadList() {
        return lclBookingPadList;
    }

    public void setLclBookingPadList(List<LclBookingPad> lclBookingPadList) {
        this.lclBookingPadList = lclBookingPadList;
    }

    public List<LclBookingImportAms> getLclBookingImportAmsList() {
        return lclBookingImportAmsList;
    }

    public void setLclBookingImportAmsList(List<LclBookingImportAms> lclBookingImportAmsList) {
        this.lclBookingImportAmsList = lclBookingImportAmsList;
    }

    public List<LclQuotePad> getLclQuotePadList() {
        return lclQuotePadList;
    }

    public void setLclQuotePadList(List<LclQuotePad> lclQuotePadList) {
        this.lclQuotePadList = lclQuotePadList;
    }

    public List<LclQuotePiece> getLclQuotePieceList() {
        return lclQuotePieceList;
    }

    public void setLclQuotePieceList(List<LclQuotePiece> lclQuotePieceList) {
        this.lclQuotePieceList = lclQuotePieceList;
    }

    public LclQuote getLclQuote() {
        return lclQuote;
    }

    public void setLclQuote(LclQuote lclQuote) {
        this.lclQuote = lclQuote;
    }

    public List<LclQuoteAc> getLclQuoteAcList() {
        return lclQuoteAcList;
    }

    public void setLclQuoteAcList(List<LclQuoteAc> lclQuoteAcList) {
        this.lclQuoteAcList = lclQuoteAcList;
    }

    public LclQuoteAir getLclQuoteAir() {
        return lclQuoteAir;
    }

    public void setLclQuoteAir(LclQuoteAir lclQuoteAir) {
        this.lclQuoteAir = lclQuoteAir;
    }

    public List<LclContact> getLclQuoteContactList() {
        return lclQuoteContactList;
    }

    public void setLclQuoteContactList(List<LclContact> lclQuoteContactList) {
        this.lclQuoteContactList = lclQuoteContactList;
    }

    public List<LclQuoteHazmat> getLclQuoteHazmatList() {
        return lclQuoteHazmatList;
    }

    public void setLclQuoteHazmatList(List<LclQuoteHazmat> lclQuoteHazmatList) {
        this.lclQuoteHazmatList = lclQuoteHazmatList;
    }

    public List<LclQuoteHotCode> getLclQuoteHotCodeList() {
        return lclQuoteHotCodeList;
    }

    public void setLclQuoteHotCodeList(List<LclQuoteHotCode> lclQuoteHotCodeList) {
        this.lclQuoteHotCodeList = lclQuoteHotCodeList;
    }

    public List<LclQuoteHsCode> getLclQuoteHsCodeList() {
        return lclQuoteHsCodeList;
    }

    public void setLclQuoteHsCodeList(List<LclQuoteHsCode> lclQuoteHsCodeList) {
        this.lclQuoteHsCodeList = lclQuoteHsCodeList;
    }

    public LclQuoteImport getLclQuoteImport() {
        return lclQuoteImport;
    }

    public void setLclQuoteImport(LclQuoteImport lclQuoteImport) {
        this.lclQuoteImport = lclQuoteImport;
    }

    public List<LclQuotePlan> getLclQuotePlanList() {
        return lclQuotePlanList;
    }

    public void setLclQuotePlanList(List<LclQuotePlan> lclQuotePlanList) {
        this.lclQuotePlanList = lclQuotePlanList;
    }

    public List<LclQuoteImportAms> getLclQuoteImportAmsList() {
        return lclQuoteImportAmsList;
    }

    public void setLclQuoteImportAmsList(List<LclQuoteImportAms> lclQuoteImportAmsList) {
        this.lclQuoteImportAmsList = lclQuoteImportAmsList;
    }

    //BL Property
    public LclBl getLclBl() {
        return lclBl;
    }

    public void setLclBl(LclBl lclBl) {
        this.lclBl = lclBl;
    }

    public List<LclBlAc> getLclBlAcList() {
        return lclBlAcList;
    }

    public void setLclBlAcList(List<LclBlAc> lclBlAcList) {
        this.lclBlAcList = lclBlAcList;
    }

    public LclBlAir getLclBlAir() {
        return lclBlAir;
    }

    public void setLclBlAir(LclBlAir lclBlAir) {
        this.lclBlAir = lclBlAir;
    }

    public List<LclContact> getLclBlContactList() {
        return lclBlContactList;
    }

    public void setLclBlContactList(List<LclContact> lclBlContactList) {
        this.lclBlContactList = lclBlContactList;
    }

    public List<LclBlHazmat> getLclBlHazmatList() {
        return lclBlHazmatList;
    }

    public void setLclBlHazmatList(List<LclBlHazmat> lclBlHazmatList) {
        this.lclBlHazmatList = lclBlHazmatList;
    }

    public List<LclBlHotCode> getLclBlHotCodeList() {
        return lclBlHotCodeList;
    }

    public void setLclBlHotCodeList(List<LclBlHotCode> lclBlHotCodeList) {
        this.lclBlHotCodeList = lclBlHotCodeList;
    }

    public LclBlImport getLclBlImport() {
        return lclBlImport;
    }

    public void setLclBlImport(LclBlImport lclBlImport) {
        this.lclBlImport = lclBlImport;
    }

    public List<LclBlPiece> getLclBlPieceList() {
        return lclBlPieceList;
    }

    public void setLclBlPieceList(List<LclBlPiece> lclBlPieceList) {
        this.lclBlPieceList = lclBlPieceList;
    }

    public List<LclBlPlan> getLclBlPlanList() {
        return lclBlPlanList;
    }

    public void setLclBlPlanList(List<LclBlPlan> lclBlPlanList) {
        this.lclBlPlanList = lclBlPlanList;
    }

    public List<LclBookingHsCode> getLclBookingHsCodeList() {
        return lclBookingHsCodeList;
    }

    public void setLclBookingHsCodeList(List<LclBookingHsCode> lclBookingHsCodeList) {
        this.lclBookingHsCodeList = lclBookingHsCodeList;
    }

    public List<LclBookingDispo> getLclBookingDispoList() {
        return lclBookingDispoList;
    }

    public void setLclBookingDispoList(List<LclBookingDispo> lclBookingDispoList) {
        this.lclBookingDispoList = lclBookingDispoList;
    }

    public List<LclBookingAcTrans> getLclBookingAcTransList() {
        return lclBookingAcTransList;
    }

    public void setLclBookingAcTransList(List<LclBookingAcTrans> lclBookingAcTransList) {
        this.lclBookingAcTransList = lclBookingAcTransList;
    }

    public List<LclOptions> getLclOptionsList() {
        return lclOptionsList;
    }

    public void setLclOptionsList(List<LclOptions> lclOptionsList) {
        this.lclOptionsList = lclOptionsList;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclFileNumber[id=" + id + "]";
    }

    public Integer getShortShipSequence() {
        return shortShipSequence;
    }

    public void setShortShipSequence(Integer shortShipSequence) {
        this.shortShipSequence = shortShipSequence;
    }

    public boolean isShortShip() {
        return shortShip;
    }

    public void setShortShip(boolean shortShip) {
        this.shortShip = shortShip;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public LclBookingExport getLclBookingExport() {
        return lclBookingExport;
    }

    public void setLclBookingExport(LclBookingExport lclBookingExport) {
        this.lclBookingExport = lclBookingExport;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclFileNumber)) {
            return false;
        }
        LclFileNumber other = (LclFileNumber) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
