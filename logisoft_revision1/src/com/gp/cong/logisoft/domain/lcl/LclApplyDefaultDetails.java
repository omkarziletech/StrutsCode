/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import java.io.Serializable;
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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author aravindhan.v
 */
@Entity
@Table(name = "lcl_apply_default_details")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclApplyDefaultDetails extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "apply_default_name", length = 255)
    private String applyDefaultName;
    @JoinColumn(name = "fd_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation finalDestination;
    @JoinColumn(name = "pod_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation portOfDestination;
    @JoinColumn(name = "pol_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation portOfLoading;
    @JoinColumn(name = "poo_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation portOfOrigin;
    @JoinColumn(name = "cur_loc_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation currentLocation;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    @Basic(optional = false)
    private User user;

    public LclApplyDefaultDetails() {
    }

    public LclApplyDefaultDetails(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplyDefaultName() {
        return applyDefaultName;
    }

    public void setApplyDefaultName(String applyDefaultName) {
        this.applyDefaultName = applyDefaultName;
    }

    public UnLocation getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(UnLocation finalDestination) {
        this.finalDestination = finalDestination;
    }

    public UnLocation getPortOfDestination() {
        return portOfDestination;
    }

    public void setPortOfDestination(UnLocation portOfDestination) {
        this.portOfDestination = portOfDestination;
    }

    public UnLocation getPortOfLoading() {
        return portOfLoading;
    }

    public void setPortOfLoading(UnLocation portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public UnLocation getPortOfOrigin() {
        return portOfOrigin;
    }

    public void setPortOfOrigin(UnLocation portOfOrigin) {
        this.portOfOrigin = portOfOrigin;
    }

    public UnLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(UnLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        if (!(object instanceof LclApplyDefaultDetails)) {
            return false;
        }
        LclApplyDefaultDetails other = (LclApplyDefaultDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclApplyDefaultDetails[ id=" + id + " ]";
    }

}
