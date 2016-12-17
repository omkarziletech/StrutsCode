package com.logiware.accounting.domain;

import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.domain.GlMapping;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "edi_invoice_detail")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EdiInvoiceDetail implements Serializable {

    private static final long serialVersionUID = 2085597337001879309L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "quantity")
    private String quantity;
    @Column(name = "uom")
    private String uom;
    @Column(name = "calculation_code")
    private String calculationCode;
    @Basic(optional = false)
    @Column(name = "price", nullable = false)
    private String price = "0.00";
    @Basic(optional = false)
    @Column(name = "rate", nullable = false)
    private String rate = "0.00";
    @Basic(optional = false)
    @Column(name = "currency", nullable = false)
    private String currency = "USD";
    @Basic(optional = false)
    @Column(name = "vat_excluded_amount", nullable = false)
    private String vatExcludedAmount = "0.00";
    @Basic(optional = false)
    @Column(name = "vat_included_amount", nullable = false)
    private String vatIncludedAmount = "0.00";
    @Basic(optional = false)
    @Column(name = "vat_amount", nullable = false)
    private String vatAmount = "0.00";
    @Basic(optional = false)
    @Column(name = "vat_percentage", nullable = false)
    private String vatPercentage = "0.00";
    @Column(name = "bl_reference")
    private String blReference;
    @JoinColumn(name = "edi_invoice_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EdiInvoice ediInvoice;
    @Column(name = "charge_status")
    private String chargeStatus;
    @Column(name = "invoice_status")
    private String invoiceStatus;
    @JoinColumn(name = "gl_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private GlMapping arGlMapping;
    @Column(name = "ar_amount")
    private String arAmount;
    @Column(name = "ap_amount")
    private String apAmount;
    @JoinColumn(name = "ap_gl_mapping_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private GlMapping apGlMapping;
    @JoinColumn(name = "updated_by", referencedColumnName = "user_id")
    @ManyToOne(optional = true)
    private User updatedBy;
    @Column(name = "updated_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Column(name = "ar_bill_to_party")
    private String arBillToParty;

    public EdiInvoiceDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCalculationCode() {
        return calculationCode;
    }

    public void setCalculationCode(String calculationCode) {
        this.calculationCode = calculationCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVatExcludedAmount() {
        return vatExcludedAmount;
    }

    public void setVatExcludedAmount(String vatExcludedAmount) {
        this.vatExcludedAmount = vatExcludedAmount;
    }

    public String getVatIncludedAmount() {
        return vatIncludedAmount;
    }

    public void setVatIncludedAmount(String vatIncludedAmount) {
        this.vatIncludedAmount = vatIncludedAmount;
    }

    public String getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(String vatAmount) {
        this.vatAmount = vatAmount;
    }

    public String getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(String vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public String getBlReference() {
        return blReference;
    }

    public void setBlReference(String blReference) {
        this.blReference = blReference;
    }

    public EdiInvoice getEdiInvoice() {
        return ediInvoice;
    }

    public void setEdiInvoice(EdiInvoice ediInvoice) {
        this.ediInvoice = ediInvoice;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public GlMapping getArGlMapping() {
        return arGlMapping;
    }

    public void setArGlMapping(GlMapping arGlMapping) {
        this.arGlMapping = arGlMapping;
    }

    public String getArAmount() {
        return arAmount;
    }

    public void setArAmount(String arAmount) {
        this.arAmount = arAmount;
    }

    public String getApAmount() {
        return apAmount;
    }

    public void setApAmount(String apAmount) {
        this.apAmount = apAmount;
    }

    public GlMapping getApGlMapping() {
        return apGlMapping;
    }

    public void setApGlMapping(GlMapping apGlMapping) {
        this.apGlMapping = apGlMapping;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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
        if (!(object instanceof EdiInvoiceDetail)) {
            return false;
        }
        EdiInvoiceDetail other = (EdiInvoiceDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.logiware.accounting.domain.EdiInvoiceDetail[ id=" + id + " ]";
    }

    public String getArBillToParty() {
        return arBillToParty;
    }

    public void setArBillToParty(String arBillToParty) {
        this.arBillToParty = arBillToParty;
    }

}
