/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.hibernate;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Owner
 */
@Entity
@Table(name = "fcl_aes_details")
public class FclAesDetailsNew implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "aes_details")
    private String aesDetails;
    @Column(name = "exception")
    private String exception;
    @Column(name = "file_no")
    private String fileNo;
    @JoinColumn(name = "BolId", referencedColumnName = "Bol")
    @ManyToOne(fetch = FetchType.LAZY)
    private FclBlNew fclBl;

    public FclAesDetailsNew() {
    }

    public FclAesDetailsNew(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAesDetails() {
        return aesDetails;
    }

    public void setAesDetails(String aesDetails) {
        this.aesDetails = aesDetails;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public FclBlNew getFclBl() {
        return fclBl;
    }

    public void setFclBl(FclBlNew fclBl) {
        this.fclBl = fclBl;
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
        if (!(object instanceof FclAesDetailsNew)) {
            return false;
        }
        FclAesDetailsNew other = (FclAesDetailsNew) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.hibernate.FclAesDetails[id=" + id + "]";
    }

}
