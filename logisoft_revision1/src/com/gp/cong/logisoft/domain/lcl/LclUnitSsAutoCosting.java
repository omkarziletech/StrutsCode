/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.domain.GlMapping;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Mei
 */
@Entity
@Table(name = "lcl_ss_unit_auto_costing")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclUnitSsAutoCosting extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Column(name = "sp_reference")
    private String spReference;
    @Basic(optional = false)
    @Column(name = "invoice_use_schedule_no")
    private boolean invoiceUseScheduleNo;
    @Basic(optional = false)
    @Column(name = "rate_uom")
    private String rateUom;
    @Column(name = "rate_per_uom")
    private BigDecimal ratePerUom;
    @Column(name = "rate_min_amount")
    private BigDecimal rateMinAmount;
    @Column(name = "rate_max_amount")
    private BigDecimal rateMaxAmount;
    @Column(name = "rate_action")
    private String rateAction;
    @Column(name = "rate_condition")
    private String rateCondition;
    @Column(name = "rate_condition_qty")
    private BigDecimal rateConditionQty;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredByUser;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedByUser;
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation origin;
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnLocation destination;
    @JoinColumn(name = "unit_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnitType unitType;
    @JoinColumn(name = "gl_mapping_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GlMapping glMapping;
    @JoinColumn(name = "sp_acct_no", referencedColumnName = "acct_no")
    @ManyToOne(optional = false)
    private TradingPartner vendor;

    public LclUnitSsAutoCosting() {
    }

    public LclUnitSsAutoCosting(Long id) {
        this.id = id;
    }

    public LclUnitSsAutoCosting(Long id, String type, boolean invoiceUseScheduleNo,
            String rateUom, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.type = type;
        this.invoiceUseScheduleNo = invoiceUseScheduleNo;
        this.rateUom = rateUom;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpReference() {
        return spReference;
    }

    public void setSpReference(String spReference) {
        this.spReference = spReference;
    }

    public boolean getInvoiceUseScheduleNo() {
        return invoiceUseScheduleNo;
    }

    public void setInvoiceUseScheduleNo(boolean invoiceUseScheduleNo) {
        this.invoiceUseScheduleNo = invoiceUseScheduleNo;
    }

    public String getRateUom() {
        return rateUom;
    }

    public void setRateUom(String rateUom) {
        this.rateUom = rateUom;
    }

    public BigDecimal getRatePerUom() {
        return ratePerUom;
    }

    public void setRatePerUom(BigDecimal ratePerUom) {
        this.ratePerUom = ratePerUom;
    }

    public BigDecimal getRateMinAmount() {
        return rateMinAmount;
    }

    public void setRateMinAmount(BigDecimal rateMinAmount) {
        this.rateMinAmount = rateMinAmount;
    }

    public BigDecimal getRateMaxAmount() {
        return rateMaxAmount;
    }

    public void setRateMaxAmount(BigDecimal rateMaxAmount) {
        this.rateMaxAmount = rateMaxAmount;
    }

    public String getRateAction() {
        return rateAction;
    }

    public void setRateAction(String rateAction) {
        this.rateAction = rateAction;
    }

    public String getRateCondition() {
        return rateCondition;
    }

    public void setRateCondition(String rateCondition) {
        this.rateCondition = rateCondition;
    }

    public BigDecimal getRateConditionQty() {
        return rateConditionQty;
    }

    public void setRateConditionQty(BigDecimal rateConditionQty) {
        this.rateConditionQty = rateConditionQty;
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

    public UnLocation getDestination() {
        return destination;
    }

    public void setDestination(UnLocation destination) {
        this.destination = destination;
    }

    public User getEnteredByUser() {
        return enteredByUser;
    }

    public void setEnteredByUser(User enteredByUser) {
        this.enteredByUser = enteredByUser;
    }

    public GlMapping getGlMapping() {
        return glMapping;
    }

    public void setGlMapping(GlMapping glMapping) {
        this.glMapping = glMapping;
    }

    public User getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(User modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public UnLocation getOrigin() {
        return origin;
    }

    public void setOrigin(UnLocation origin) {
        this.origin = origin;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public TradingPartner getVendor() {
        return vendor;
    }

    public void setVendor(TradingPartner vendor) {
        this.vendor = vendor;
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
        if (!(object instanceof LclUnitSsAutoCosting)) {
            return false;
        }
        LclUnitSsAutoCosting other = (LclUnitSsAutoCosting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclUnitSsAutoCosting[ id=" + id + " ]";
    }
}
