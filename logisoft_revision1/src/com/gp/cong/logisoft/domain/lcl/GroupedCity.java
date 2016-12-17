/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.UnLocation;
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
 * @author logiware
 */
@Entity
@Table(name = "grouped_city")
@DynamicUpdate(true)
@DynamicInsert(true)
@NamedQueries({
    @NamedQuery(name = "GroupedCity.findAll", query = "SELECT g FROM GroupedCity g"),
    @NamedQuery(name = "GroupedCity.findById", query = "SELECT g FROM GroupedCity g WHERE g.id = :id")})
public class GroupedCity extends Domain {
    private static final long serialVersionUID = 1L;
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "group_city", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private UnLocation groupCity;
    @JoinColumn(name = "city", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private UnLocation city;

    public GroupedCity() {
    }

    public GroupedCity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnLocation getCity() {
        return city;
    }

    public void setCity(UnLocation city) {
        this.city = city;
    }

    public UnLocation getGroupCity() {
        return groupCity;
    }

    public void setGroupCity(UnLocation groupCity) {
        this.groupCity = groupCity;
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
        if (!(object instanceof GroupedCity)) {
            return false;
        }
        GroupedCity other = (GroupedCity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.GroupedCity[id=" + id + "]";
    }

}
