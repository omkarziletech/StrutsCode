/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.domain;

import com.gp.cong.hibernate.Domain;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author 
 */
@Entity
@Table(name = "multi_quote")
@DynamicInsert(true)
@DynamicUpdate(true)
public class MultiQuote extends Domain implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "origin")
    private String origin;
    @Column(name = "destination")
    private String destination;
    @Column(name = "commodity")
    private String commodity;
    @Column(name = "carrier")
    private String carrier;
     @Column(name = "carrier_no")
    private String carrierNo;
    @Column(name = "origin_code")
    private String originCode;  
    @Column(name = "desti_code")
    private String destinationCode;
    @Column(name = "selected_units")
    private String selected_Units;
    @Column(name = "hazmat")
    private String hazmat;
    @Column(name = "bullet_rates")
    private String bulletRates;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "multiQuote")
    private List<MultiQuoteCharges> multiQuoteCharges;
    @JoinColumn(name = "quote_id", referencedColumnName = "Quote_ID")
    @ManyToOne(optional = false)
    private Quotation quotation;
    
    

    public MultiQuote() {
    }

    public MultiQuote(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public List<MultiQuoteCharges> getMultiQuoteCharges() {
        return multiQuoteCharges;
    }

    public void setMultiQuoteChargesCollection(List<MultiQuoteCharges> multiQuoteCharges) {
        this.multiQuoteCharges = multiQuoteCharges;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public void setCarrierNo(String carrierNo) {
        this.carrierNo = carrierNo;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getSelected_Units() {
        return selected_Units;
    }

    public void setSelected_Units(String selected_Units) {
        this.selected_Units = selected_Units;
    }

    public String getHazmat() {
        return hazmat;
    }

    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public String getBulletRates() {
        return bulletRates;
    }

    public void setBulletRates(String bulletRates) {
        this.bulletRates = bulletRates;
    }

    
    
//    public Collection<MultiQuoteCharges> getMultiQuoteChargesCollection() {
//        return multiQuoteChargesCollection;
//    }
//
//    public void setMultiQuoteChargesCollection(Collection<MultiQuoteCharges> multiQuoteChargesCollection) {
//        this.multiQuoteChargesCollection = multiQuoteChargesCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MultiQuote)) {
            return false;
        }
        MultiQuote other = (MultiQuote) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cvst.logisoft.domain.MultiQuote[ id=" + id + " ]";
    }
    
}
