/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "lcl_unit_ss_imports")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclUnitSsImports.findAll", query = "SELECT l FROM LclUnitSsImports l"),
    @NamedQuery(name = "LclUnitSsImports.findById", query = "SELECT l FROM LclUnitSsImports l WHERE l.id = :id"),
    @NamedQuery(name = "LclUnitSsImports.findByInvoiceStatus", query = "SELECT l FROM LclUnitSsImports l WHERE l.invoiceStatus = :invoiceStatus"),
    @NamedQuery(name = "LclUnitSsImports.findByInvoiceDatetime", query = "SELECT l FROM LclUnitSsImports l WHERE l.invoiceDatetime = :invoiceDatetime"),
    @NamedQuery(name = "LclUnitSsImports.findByItDatetime", query = "SELECT l FROM LclUnitSsImports l WHERE l.itDatetime = :itDatetime"),
    @NamedQuery(name = "LclUnitSsImports.findByEnteredDatetime", query = "SELECT l FROM LclUnitSsImports l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclUnitSsImports.findByModifiedDatetime", query = "SELECT l FROM LclUnitSsImports l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclUnitSsImports extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "last_free_date")
    @Temporal(TemporalType.DATE)
    private Date lastFreeDate;
    @Column(name = "go_date")
    @Temporal(TemporalType.DATE)
    private Date goDate;
    @Column(name = "it_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date itDatetime;
    @Column(name = "it_no")
    private String itNo;
    @Column(name = "invoice_status")
    private String invoiceStatus;
    @Column(name = "invoice_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceDatetime;
    @Column(name = "approx_due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approxDueDate;
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
    @JoinColumn(name = "invoice_by_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User invoiceUser;
    @JoinColumn(name = "unit_warehouse_id", referencedColumnName = "id")
    @ManyToOne
    private Warehouse unitWareHouseId;
    @JoinColumn(name = "cfs_warehouse_id", referencedColumnName = "id")
    @ManyToOne
    private Warehouse cfsWarehouseId;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclSsHeader lclSsHeader;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclUnit lclUnit;
    @JoinColumn(name = "it_port_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation itPortId;
    @JoinColumn(name = "origin_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner originAcctNo;
    @JoinColumn(name = "coloader_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner coloaderAcctNo;
    @JoinColumn(name = "coloader_devanning_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner coloaderDevanningAcctNo;

    public LclUnitSsImports() {
    }

    public LclUnitSsImports(Long id) {
        this.id = id;
    }

    public LclUnitSsImports(Long id, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGoDate() {
        return goDate;
    }

    public void setGoDate(Date goDate) {
        this.goDate = goDate;
    }

    public String getItNo() {
        return itNo;
    }

    public void setItNo(String itNo) {
        this.itNo = itNo;
    }

    public Date getLastFreeDate() {
        return lastFreeDate;
    }

    public void setLastFreeDate(Date lastFreeDate) {
        this.lastFreeDate = lastFreeDate;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Date getInvoiceDatetime() {
        return invoiceDatetime;
    }

    public void setInvoiceDatetime(Date invoiceDatetime) {
        this.invoiceDatetime = invoiceDatetime;
    }

    public Date getItDatetime() {
        return itDatetime;
    }

    public void setItDatetime(Date itDatetime) {
        this.itDatetime = itDatetime;
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

    public User getInvoiceUser() {
        return invoiceUser;
    }

    public void setInvoiceUser(User invoiceUser) {
        this.invoiceUser = invoiceUser;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Warehouse getCfsWarehouseId() {
        return cfsWarehouseId;
    }

    public void setCfsWarehouseId(Warehouse cfsWarehouseId) {
        this.cfsWarehouseId = cfsWarehouseId;
    }

    public Warehouse getUnitWareHouseId() {
        return unitWareHouseId;
    }

    public void setUnitWareHouseId(Warehouse unitWareHouseId) {
        this.unitWareHouseId = unitWareHouseId;
    }

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
    }

    public LclUnit getLclUnit() {
        return lclUnit;
    }

    public void setLclUnit(LclUnit lclUnit) {
        this.lclUnit = lclUnit;
    }

    public UnLocation getItPortId() {
        return itPortId;
    }

    public void setItPortId(UnLocation itPortId) {
        this.itPortId = itPortId;
    }

    public TradingPartner getColoaderAcctNo() {
        return coloaderAcctNo;
    }

    public void setColoaderAcctNo(TradingPartner coloaderAcctNo) {
        this.coloaderAcctNo = coloaderAcctNo;
    }

    public TradingPartner getOriginAcctNo() {
        return originAcctNo;
    }

    public void setOriginAcctNo(TradingPartner originAcctNo) {
        this.originAcctNo = originAcctNo;
    }

    public Date getApproxDueDate() {
        return approxDueDate;
    }

    public void setApproxDueDate(Date approxDueDate) {
        this.approxDueDate = approxDueDate;
    }

    public TradingPartner getColoaderDevanningAcctNo() {
        return coloaderDevanningAcctNo;
    }

    public void setColoaderDevanningAcctNo(TradingPartner coloaderDevanningAcctNo) {
        this.coloaderDevanningAcctNo = coloaderDevanningAcctNo;
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
        if (!(object instanceof LclUnitSsImports)) {
            return false;
        }
        LclUnitSsImports other = (LclUnitSsImports) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclUnitSsImports[id=" + id + "]";
    }
}
