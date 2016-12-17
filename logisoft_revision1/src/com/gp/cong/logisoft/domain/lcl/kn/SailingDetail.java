/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.kn;

import com.gp.cong.hibernate.Domain;
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

/**
 *
 * @author palraj.p
 */
@Entity
@Table(name = "kn_sailing_details")
@NamedQueries({
    @NamedQuery(name = "SailingDetail.findAll", query = "SELECT s FROM SailingDetail s")})
public class SailingDetail extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "vessel_voyage_id")
    private String vesselVoyageId;
    @Basic(optional = false)
    @Column(name = "vessel_name")
    private String vesselName;
    @Column(name = "imo_number")
    private String imoNumber;
    @Basic(optional = false)
    @Column(name = "voyage")
    private String voyage;
    @Basic(optional = false)
    @Column(name = "etd")
    @Temporal(TemporalType.DATE)
    private Date etd;
    @Basic(optional = false)
    @Column(name = "eta")
    @Temporal(TemporalType.DATE)
    private Date eta;
    @JoinColumn(name = "bkg_id", referencedColumnName = "id")
    @ManyToOne
    private BookingDetail bkgId;

    public SailingDetail() {
    }

    public SailingDetail(Long id) {
        this.id = id;
    }

    public SailingDetail(Long id, String vesselVoyageId, String vesselName, String voyage, Date etd, Date eta) {
        this.id = id;
        this.vesselVoyageId = vesselVoyageId;
        this.vesselName = vesselName;
        this.voyage = voyage;
        this.etd = etd;
        this.eta = eta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVesselVoyageId() {
        return vesselVoyageId;
    }

    public void setVesselVoyageId(String vesselVoyageId) {
        this.vesselVoyageId = vesselVoyageId;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getImoNumber() {
        return imoNumber;
    }

    public void setImoNumber(String imoNumber) {
        this.imoNumber = imoNumber;
    }

    public String getVoyage() {
        return voyage;
    }

    public void setVoyage(String voyage) {
        this.voyage = voyage;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public BookingDetail getBkgId() {
        return bkgId;
    }

    public void setBkgId(BookingDetail bkgId) {
        this.bkgId = bkgId;
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
        if (!(object instanceof SailingDetail)) {
            return false;
        }
        SailingDetail other = (SailingDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.kn.SailingDetail[ id=" + id + " ]";
    }
}
