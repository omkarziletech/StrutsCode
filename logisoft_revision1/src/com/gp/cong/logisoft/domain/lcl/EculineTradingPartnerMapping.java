/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author vijaygupta.m
 */
@Entity
@Table(name = "eculine_trading_partner_mapping")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "EculineTradingPartnerMapping.findAll", query = "SELECT e FROM EculineTradingPartnerMapping e"),
    @NamedQuery(name = "EculineTradingPartnerMapping.findById", query = "SELECT e FROM EculineTradingPartnerMapping e WHERE e.id = :id"),
    @NamedQuery(name = "EculineTradingPartnerMapping.findByAddress", query = "SELECT e FROM EculineTradingPartnerMapping e WHERE e.address = :address")})
public class EculineTradingPartnerMapping extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @JoinColumn(name = "trading_partner_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner tradingPartner;

    public EculineTradingPartnerMapping() {
    }

    public EculineTradingPartnerMapping(Integer id) {
        this.id = id;
    }

    public EculineTradingPartnerMapping(Integer id, String address) {
        this.id = id;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TradingPartner getTradingPartner() {
        return tradingPartner;
    }

    public void setTradingPartner(TradingPartner tradingPartner) {
        this.tradingPartner = tradingPartner;
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
        if (!(object instanceof EculineTradingPartnerMapping)) {
            return false;
        }
        EculineTradingPartnerMapping other = (EculineTradingPartnerMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.EculineTradingPartnerMapping[id=" + id + "]";
    }
}
