/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.lcl.bl.LclCorrection;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Aravindhan
 */
@Entity
@Table(name = "lcl_correction_commodity")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CorrectionCommodity extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "totalCft")
    private BigDecimal totalCft;
    @Column(name = "totalCbm")
    private BigDecimal totalCbm;
    @Column(name = "totalLbs")
    private BigDecimal totalLbs;
    @Column(name = "totalKgs")
    private BigDecimal totalKgs;
    @JoinColumn(name = "correction_id", referencedColumnName = "id")
    @ManyToOne
    private LclCorrection lclCorrection;
    @JoinColumn(name = "commodityId", referencedColumnName = "id")
    @ManyToOne
    private CommodityType commodityType;

    public CommodityType getCommodityType() {
        return commodityType;
    }

    public void setCommodityType(CommodityType commodityType) {
        this.commodityType = commodityType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LclCorrection getLclCorrection() {
        return lclCorrection;
    }

    public void setLclCorrection(LclCorrection lclCorrection) {
        this.lclCorrection = lclCorrection;
    }

    public BigDecimal getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(BigDecimal totalCbm) {
        this.totalCbm = totalCbm;
    }

    public BigDecimal getTotalCft() {
        return totalCft;
    }

    public void setTotalCft(BigDecimal totalCft) {
        this.totalCft = totalCft;
    }

    public BigDecimal getTotalKgs() {
        return totalKgs;
    }

    public void setTotalKgs(BigDecimal totalKgs) {
        this.totalKgs = totalKgs;
    }

    public BigDecimal getTotalLbs() {
        return totalLbs;
    }

    public void setTotalLbs(BigDecimal totalLbs) {
        this.totalLbs = totalLbs;
    }
}
