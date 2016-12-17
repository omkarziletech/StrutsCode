/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Vandhana
 */
@Entity
@Table(name = "warehs")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "Warehs.findAll", query = "SELECT w FROM Warehs w"),
    @NamedQuery(name = "Warehs.findByWarnum", query = "SELECT w FROM Warehs w WHERE w.warnum = :warnum"),
    @NamedQuery(name = "Warehs.findByWarnam", query = "SELECT w FROM Warehs w WHERE w.warnam = :warnam"),
    @NamedQuery(name = "Warehs.findByAddres", query = "SELECT w FROM Warehs w WHERE w.addres = :addres"),
    @NamedQuery(name = "Warehs.findByCity", query = "SELECT w FROM Warehs w WHERE w.city = :city"),
    @NamedQuery(name = "Warehs.findByState", query = "SELECT w FROM Warehs w WHERE w.state = :state"),
    @NamedQuery(name = "Warehs.findByZipcod", query = "SELECT w FROM Warehs w WHERE w.zipcod = :zipcod"),
    @NamedQuery(name = "Warehs.findByPhone", query = "SELECT w FROM Warehs w WHERE w.phone = :phone"),
    @NamedQuery(name = "Warehs.findByManage", query = "SELECT w FROM Warehs w WHERE w.manage = :manage"),
    @NamedQuery(name = "Warehs.findByUpddte", query = "SELECT w FROM Warehs w WHERE w.upddte = :upddte"),
    @NamedQuery(name = "Warehs.findByUpdtim", query = "SELECT w FROM Warehs w WHERE w.updtim = :updtim"),
    @NamedQuery(name = "Warehs.findByUpdtby", query = "SELECT w FROM Warehs w WHERE w.updtby = :updtby"),
    @NamedQuery(name = "Warehs.findByWhsfax", query = "SELECT w FROM Warehs w WHERE w.whsfax = :whsfax"),
    @NamedQuery(name = "Warehs.findByAgname", query = "SELECT w FROM Warehs w WHERE w.agname = :agname"),
    @NamedQuery(name = "Warehs.findByAgaddr", query = "SELECT w FROM Warehs w WHERE w.agaddr = :agaddr"),
    @NamedQuery(name = "Warehs.findByAgcity", query = "SELECT w FROM Warehs w WHERE w.agcity = :agcity"),
    @NamedQuery(name = "Warehs.findByAgstat", query = "SELECT w FROM Warehs w WHERE w.agstat = :agstat"),
    @NamedQuery(name = "Warehs.findByAgzcod", query = "SELECT w FROM Warehs w WHERE w.agzcod = :agzcod"),
    @NamedQuery(name = "Warehs.findByIpivnd", query = "SELECT w FROM Warehs w WHERE w.ipivnd = :ipivnd"),
    @NamedQuery(name = "Warehs.findByIpicmd", query = "SELECT w FROM Warehs w WHERE w.ipicmd = :ipicmd")})
public class Warehs extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "warnum")
    private String warnum;
    @Column(name = "warnam")
    private String warnam;
    @Column(name = "addres")
    private String addres;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zipcod")
    private String zipcod;
    @Column(name = "phone")
    private String phone;
    @Column(name = "manage")
    private String manage;
    @Column(name = "upddte")
    private Integer upddte;
    @Column(name = "updtim")
    private Integer updtim;
    @Column(name = "updtby")
    private String updtby;
    @Column(name = "whsfax")
    private String whsfax;
    @Column(name = "agname")
    private String agname;
    @Column(name = "agaddr")
    private String agaddr;
    @Column(name = "agcity")
    private String agcity;
    @Column(name = "agstat")
    private String agstat;
    @Column(name = "agzcod")
    private String agzcod;
    @Column(name = "ipivnd")
    private String ipivnd;
    @Column(name = "ipicmd")
    private String ipicmd;

    public Warehs() {
    }

    public Warehs(String warnum) {
        this.warnum = warnum;
    }

    public String getWarnum() {
        return warnum;
    }

    public void setWarnum(String warnum) {
        this.warnum = warnum;
    }

    public String getWarnam() {
        return warnam;
    }

    public void setWarnam(String warnam) {
        this.warnam = warnam;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcod() {
        return zipcod;
    }

    public void setZipcod(String zipcod) {
        this.zipcod = zipcod;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getManage() {
        return manage;
    }

    public void setManage(String manage) {
        this.manage = manage;
    }

    public Integer getUpddte() {
        return upddte;
    }

    public void setUpddte(Integer upddte) {
        this.upddte = upddte;
    }

    public Integer getUpdtim() {
        return updtim;
    }

    public void setUpdtim(Integer updtim) {
        this.updtim = updtim;
    }

    public String getUpdtby() {
        return updtby;
    }

    public void setUpdtby(String updtby) {
        this.updtby = updtby;
    }

    public String getWhsfax() {
        return whsfax;
    }

    public void setWhsfax(String whsfax) {
        this.whsfax = whsfax;
    }

    public String getAgname() {
        return agname;
    }

    public void setAgname(String agname) {
        this.agname = agname;
    }

    public String getAgaddr() {
        return agaddr;
    }

    public void setAgaddr(String agaddr) {
        this.agaddr = agaddr;
    }

    public String getAgcity() {
        return agcity;
    }

    public void setAgcity(String agcity) {
        this.agcity = agcity;
    }

    public String getAgstat() {
        return agstat;
    }

    public void setAgstat(String agstat) {
        this.agstat = agstat;
    }

    public String getAgzcod() {
        return agzcod;
    }

    public void setAgzcod(String agzcod) {
        this.agzcod = agzcod;
    }

    public String getIpivnd() {
        return ipivnd;
    }

    public void setIpivnd(String ipivnd) {
        this.ipivnd = ipivnd;
    }

    public String getIpicmd() {
        return ipicmd;
    }

    public void setIpicmd(String ipicmd) {
        this.ipicmd = ipicmd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (warnum != null ? warnum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Warehs)) {
            return false;
        }
        Warehs other = (Warehs) object;
        if ((this.warnum == null && other.warnum != null) || (this.warnum != null && !this.warnum.equals(other.warnum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.Warehs[warnum=" + warnum + "]";
    }
}
