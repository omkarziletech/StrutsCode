package com.logiware.common.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Lakshmi Narayanan
 */
@Entity
@Table(name = "online_user")
@DynamicInsert(true)
@DynamicUpdate(true)
public class OnlineUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "logged_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date loggedOn;
    @Basic(optional = false)
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;
    @Basic(optional = false)
    @Column(name = "session_id", nullable = false)
    private String sessionId;
    @Basic(optional = false)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public OnlineUser() {
    }

    public OnlineUser(Integer id) {
        this.id = id;
    }

    public OnlineUser(Integer userId, Date loggedOn, String ipAddress, String sessionId) {
        this.userId = userId;
        this.loggedOn = loggedOn;
        this.ipAddress = ipAddress;
        this.sessionId = sessionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLoggedOn() {
        return loggedOn;
    }

    public void setLoggedOn(Date loggedOn) {
        this.loggedOn = loggedOn;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        if (!(object instanceof OnlineUser)) {
            return false;
        }
        OnlineUser other = (OnlineUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.logiware.common.domain.OnlineUser[ id=" + id + " ]";
    }

}
