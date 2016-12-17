/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author HOME
 */
@Entity
@Table(name = "lcl_user_defaults", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_details_id"})})
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclUserDefaults.findAll", query = "SELECT l FROM LclUserDefaults l"),
    @NamedQuery(name = "LclUserDefaults.findById", query = "SELECT l FROM LclUserDefaults l WHERE l.id = :id")})
public class LclUserDefaults extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @JoinColumn(name = "cuur_loc_id", referencedColumnName = "id")
    @ManyToOne
    private UnLocation currentLocation;
    @JoinColumn(name = "user_details_id", referencedColumnName = "user_id")
    @OneToOne
    private User userDetails;

    public LclUserDefaults() {
    }

    public LclUserDefaults(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public User getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
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
        if (!(object instanceof LclUserDefaults)) {
            return false;
        }
        LclUserDefaults other = (LclUserDefaults) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.lcl.model.LclUserDefaults[id=" + id + "]";
    }
}
