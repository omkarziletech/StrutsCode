/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import java.util.List;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "lcl_ss_header")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclSsHeader.findAll", query = "SELECT l FROM LclSsHeader l"),
    @NamedQuery(name = "LclSsHeader.findById", query = "SELECT l FROM LclSsHeader l WHERE l.id = :id"),
    @NamedQuery(name = "LclSsHeader.findByTransMode", query = "SELECT l FROM LclSsHeader l WHERE l.transMode = :transMode"),
    @NamedQuery(name = "LclSsHeader.findByServiceType", query = "SELECT l FROM LclSsHeader l WHERE l.serviceType = :serviceType"),
    @NamedQuery(name = "LclSsHeader.findByScheduleNo", query = "SELECT l FROM LclSsHeader l WHERE l.scheduleNo = :scheduleNo"),
    @NamedQuery(name = "LclSsHeader.findByStatus", query = "SELECT l FROM LclSsHeader l WHERE l.status = :status"),
    @NamedQuery(name = "LclSsHeader.findByHazmatPermitted", query = "SELECT l FROM LclSsHeader l WHERE l.hazmatPermitted = :hazmatPermitted"),
    @NamedQuery(name = "LclSsHeader.findByRefrigerationPermitted", query = "SELECT l FROM LclSsHeader l WHERE l.refrigerationPermitted = :refrigerationPermitted"),
    @NamedQuery(name = "LclSsHeader.findByClosedDatetime", query = "SELECT l FROM LclSsHeader l WHERE l.closedDatetime = :closedDatetime"),
    @NamedQuery(name = "LclSsHeader.findByAuditedDatetime", query = "SELECT l FROM LclSsHeader l WHERE l.auditedDatetime = :auditedDatetime"),
    @NamedQuery(name = "LclSsHeader.findByOptPrintSsc", query = "SELECT l FROM LclSsHeader l WHERE l.optPrintSsc = :optPrintSsc"),
    @NamedQuery(name = "LclSsHeader.findByEliteVoynum", query = "SELECT l FROM LclSsHeader l WHERE l.eliteVoynum = :eliteVoynum"),
    @NamedQuery(name = "LclSsHeader.findByDatasource", query = "SELECT l FROM LclSsHeader l WHERE l.datasource = :datasource"),
    @NamedQuery(name = "LclSsHeader.findByEnteredDatetime", query = "SELECT l FROM LclSsHeader l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclSsHeader.findByModifiedDatetime", query = "SELECT l FROM LclSsHeader l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclSsHeader extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "trans_mode")
    private String transMode;
    @Basic(optional = false)
    @Column(name = "service_type")
    private String serviceType;
    @Basic(optional = false)
    @Column(name = "schedule_no")
    private String scheduleNo;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "hazmat_permitted")
    private Boolean hazmatPermitted;
    @Column(name = "refrigeration_permitted")
    private Boolean refrigerationPermitted;
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "closed_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedDatetime;
    @Lob
    @Column(name = "closed_remarks")
    private String closedRemarks;
    @Column(name = "reopen_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reopenedDatetime;
    @Lob
    @Column(name = "reopen_remarks")
    private String reopenedRemarks;
    @Column(name = "audited_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date auditedDatetime;
    @Lob
    @Column(name = "audited_remarks")
    private String auditedRemarks;
    @Column(name = "opt_print_ssc")
    private Boolean optPrintSsc;
    @Column(name = "suffix")
    private String voyageSuffix;
    @Column(name = "elite_voynum")
    private String eliteVoynum;
    @Column(name = "datasource")
    private String datasource;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
     @Column(name = "bl_seq_num")
    private Integer sequenceNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclUnitSsRemarks> lclUnitSsRemarksList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclUnitSsManifest> lclUnitSsManifestList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclUnitSsImports> lclUnitSsImportsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclSsDetail> lclSsDetailList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclSSMasterBl> lclSsMasterBlList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclUnitSs> lclUnitSsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclSsContact> lclSsContactList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclSsRemarks> lclSsRemarksList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private List<LclSsAc> lclSsAcList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "lclSsHeader")
    private LclSsExports lclSsExports;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "audited_by_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User auditedBy;
    @JoinColumn(name = "closed_by_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User closedBy;
    @JoinColumn(name = "reopen_by_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User reopenedBy;
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation destination;
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation origin;
    @JoinColumn(name = "billing_trmnum", referencedColumnName = "trmnum")
    @ManyToOne
    private RefTerminal billingTerminal;
    @JoinColumn(name = "owner_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User ownerUserId;
    @Transient
    private String displayhazmatPermitted;
    @Transient
    private String displayrefrigerationPermitted;
    @Transient
    private Integer totalTransitTime;
    @Transient
    private Integer cutOffDays;

    public LclSsHeader() {
    }

    public LclSsHeader(Long id) {
        this.id = id;
    }

    public LclSsHeader(Long id, String transMode, String serviceType, String scheduleNo, String status, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.transMode = transMode;
        this.serviceType = serviceType;
        this.scheduleNo = scheduleNo;
        this.status = status;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getHazmatPermitted() {
        if (null == hazmatPermitted) {
            return false;
        }
        return hazmatPermitted;
    }

    public void setHazmatPermitted(Boolean hazmatPermitted) {
        this.hazmatPermitted = hazmatPermitted;
    }

    public Boolean getRefrigerationPermitted() {
        if (null == refrigerationPermitted) {
            return false;
        }
        return refrigerationPermitted;
    }

    public void setRefrigerationPermitted(Boolean refrigerationPermitted) {
        this.refrigerationPermitted = refrigerationPermitted;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getClosedDatetime() {
        return closedDatetime;
    }

    public void setClosedDatetime(Date closedDatetime) {
        this.closedDatetime = closedDatetime;
    }

    public String getClosedRemarks() {
        return closedRemarks;
    }

    public void setClosedRemarks(String closedRemarks) {
        this.closedRemarks = closedRemarks;
    }

    public Date getAuditedDatetime() {
        return auditedDatetime;
    }

    public void setAuditedDatetime(Date auditedDatetime) {
        this.auditedDatetime = auditedDatetime;
    }

    public String getAuditedRemarks() {
        return auditedRemarks;
    }

    public void setAuditedRemarks(String auditedRemarks) {
        this.auditedRemarks = auditedRemarks;
    }

    public Boolean getOptPrintSsc() {
        return optPrintSsc;
    }

    public void setOptPrintSsc(Boolean optPrintSsc) {
        this.optPrintSsc = optPrintSsc;
    }

    public String getVoyageSuffix() {
        return voyageSuffix;
    }

    public void setVoyageSuffix(String voyageSuffix) {
        this.voyageSuffix = voyageSuffix;
    }

    public String getEliteVoynum() {
        return eliteVoynum;
    }

    public void setEliteVoynum(String eliteVoynum) {
        this.eliteVoynum = eliteVoynum;
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

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
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

    public List<LclSsDetail> getLclSsDetailList() {
        return lclSsDetailList;
    }

    public void setLclSsDetailList(List<LclSsDetail> lclSsDetailList) {
        this.lclSsDetailList = lclSsDetailList;
    }

    public List<LclUnitSsImports> getLclUnitSsImportsList() {
        return lclUnitSsImportsList;
    }

    public void setLclUnitSsImportsList(List<LclUnitSsImports> lclUnitSsImportsList) {
        this.lclUnitSsImportsList = lclUnitSsImportsList;
    }

    public List<LclUnitSs> getLclUnitSsList() {
        return lclUnitSsList;
    }

    public void setLclUnitSsList(List<LclUnitSs> lclUnitSsList) {
        this.lclUnitSsList = lclUnitSsList;
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

    public User getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(User auditedBy) {
        this.auditedBy = auditedBy;
    }

    public User getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(User closedBy) {
        this.closedBy = closedBy;
    }

    public UnLocation getDestination() {
        return destination;
    }

    public void setDestination(UnLocation destination) {
        this.destination = destination;
    }

    public UnLocation getOrigin() {
        return origin;
    }

    public void setOrigin(UnLocation origin) {
        this.origin = origin;
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

    public Integer getCutOffDays() {
        return cutOffDays;
    }

    public void setCutOffDays(Integer cutOffDays) {
        this.cutOffDays = cutOffDays;
    }

    public Integer getTotalTransitTime() {
        return totalTransitTime;
    }

    public void setTotalTransitTime(Integer totalTransitTime) {
        this.totalTransitTime = totalTransitTime;
    }

    public List<LclSSMasterBl> getLclSsMasterBlList() {
        return lclSsMasterBlList;
    }

    public void setLclSsMasterBlList(List<LclSSMasterBl> lclSsMasterBlList) {
        this.lclSsMasterBlList = lclSsMasterBlList;
    }

    public RefTerminal getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(RefTerminal billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public List<LclSsContact> getLclSsContactList() {
        return lclSsContactList;
    }

    public void setLclSsContactList(List<LclSsContact> lclSsContactList) {
        this.lclSsContactList = lclSsContactList;
    }

    public List<LclSsRemarks> getLclSsRemarksList() {
        return lclSsRemarksList;
    }

    public void setLclSsRemarksList(List<LclSsRemarks> lclSsRemarksList) {
        this.lclSsRemarksList = lclSsRemarksList;
    }

    public User getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(User ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public LclSsDetail getVesselSsDetail() throws Exception {
        if (null != this.id) {
            return new LclSsDetailDAO().findByTransMode(this.id, "");
//            for (LclSsDetail detail : this.lclSsDetailList) {
//                if ("V".equals(detail.getTransMode())) {
//                    return detail;
//                }
//            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public List<LclSsAc> getLclSsAcList() {
        return lclSsAcList;
    }

    public void setLclSsAcList(List<LclSsAc> lclSsAcList) {
        this.lclSsAcList = lclSsAcList;
    }

    public LclSsExports getLclSsExports() {
        return lclSsExports;
    }

    public void setLclSsExports(LclSsExports lclSsExports) {
        this.lclSsExports = lclSsExports;
    }

    public Date getReopenedDatetime() {
        return reopenedDatetime;
    }

    public void setReopenedDatetime(Date reopenedDatetime) {
        this.reopenedDatetime = reopenedDatetime;
    }

    public String getReopenedRemarks() {
        return reopenedRemarks;
    }

    public void setReopenedRemarks(String reopenedRemarks) {
        this.reopenedRemarks = reopenedRemarks;
    }

    public User getReopenedBy() {
        return reopenedBy;
    }

    public void setReopenedBy(User reopenedBy) {
        this.reopenedBy = reopenedBy;
    }
    
    public LclUnitSsManifest getlclUnitSsManifestByUnitId(Long unitId){
        for(LclUnitSsManifest sSManifest : lclUnitSsManifestList){
            if(sSManifest.getLclUnit().getId().equals(unitId)){
                return sSManifest;
            }
        }
        return null;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclSsHeader)) {
            return false;
        }
        LclSsHeader other = (LclSsHeader) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclSsHeader[id=" + id + "]";
    }
}
