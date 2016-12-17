/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author aravindhan.v
 */
@Entity
@Table(name = "lcl_ss_exports")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclSsExports.findAll", query = "SELECT l FROM LclSsExports l"),
    @NamedQuery(name = "LclSsExports.findBySsHeaderId", query = "SELECT l FROM LclSsExports l WHERE l.ssHeaderId = :ssHeaderId"),
    @NamedQuery(name = "LclSsExports.findByLandExitDate", query = "SELECT l FROM LclSsExports l WHERE l.landExitDate = :landExitDate"),
    @NamedQuery(name = "LclSsExports.findByEnteredDatetime", query = "SELECT l FROM LclSsExports l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclSsExports.findByModifiedDatetime", query = "SELECT l FROM LclSsExports l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclSsExports extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ss_header_id")
    private Long ssHeaderId;
    @Column(name = "land_exit_date")
    @Temporal(TemporalType.DATE)
    private Date landExitDate;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @Column(name = "print_via_masterbl")
    private boolean printViaMasterbl;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedUserId;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredUserId;
    @JoinColumn(name = "export_agent_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner exportAgentAcctoNo;
    @JoinColumn(name = "land_exit_city_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation landExitCity;
    @JoinColumn(name = "land_exit_carrier_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner landCarrierAcctNo;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private LclSsHeader lclSsHeader;
    @Column(name = "lcl_voyage_level_brand")
    private String lclVoyageLevelBrand;

    public LclSsExports() {
    }

    public LclSsExports(Long ssHeaderId) {
        this.ssHeaderId = ssHeaderId;
    }

    public LclSsExports(Long ssHeaderId, Date enteredDatetime, Date modifiedDatetime) {
        this.ssHeaderId = ssHeaderId;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getSsHeaderId() {
        return ssHeaderId;
    }

    public void setSsHeaderId(Long ssHeaderId) {
        this.ssHeaderId = ssHeaderId;
    }

    public Date getLandExitDate() {
        return landExitDate;
    }

    public void setLandExitDate(Date landExitDate) {
        this.landExitDate = landExitDate;
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

    public User getEnteredUserId() {
        return enteredUserId;
    }

    public void setEnteredUserId(User enteredUserId) {
        this.enteredUserId = enteredUserId;
    }

    public User getModifiedUserId() {
        return modifiedUserId;
    }

    public void setModifiedUserId(User modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public TradingPartner getExportAgentAcctoNo() {
        return exportAgentAcctoNo;
    }

    public void setExportAgentAcctoNo(TradingPartner exportAgentAcctoNo) {
        this.exportAgentAcctoNo = exportAgentAcctoNo;
    }

    public TradingPartner getLandCarrierAcctNo() {
        return landCarrierAcctNo;
    }

    public void setLandCarrierAcctNo(TradingPartner landCarrierAcctNo) {
        this.landCarrierAcctNo = landCarrierAcctNo;
    }

    public UnLocation getLandExitCity() {
        return landExitCity;
    }

    public void setLandExitCity(UnLocation landExitCity) {
        this.landExitCity = landExitCity;
    }

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
    }

    public boolean isPrintViaMasterbl() {
        return printViaMasterbl;
    }

    public void setPrintViaMasterbl(boolean printViaMasterbl) {
        this.printViaMasterbl = printViaMasterbl;
    }

    public String getLclVoyageLevelBrand() {
        return lclVoyageLevelBrand;
    }

    public void setLclVoyageLevelBrand(String lclVoyageLevelBrand) {
        this.lclVoyageLevelBrand = lclVoyageLevelBrand;
    }   
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ssHeaderId != null ? ssHeaderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclSsExports)) {
            return false;
        }
        LclSsExports other = (LclSsExports) object;
        if ((this.ssHeaderId == null && other.ssHeaderId != null) || (this.ssHeaderId != null && !this.ssHeaderId.equals(other.ssHeaderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclSsExports[ssHeaderId=" + ssHeaderId + "]";
    }
}
