/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Table;

/**
 *
 * @author Mei
 */
@Entity
@Table(name = "corporate_account")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CorporateAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    @Basic(optional = false)
    private String corporateName;
    @Column(name = "elite_comcde")
    private String eliteCommodityCode;
    @JoinColumn(name = "corporate_account_type_id", referencedColumnName = "id")
    @ManyToOne
    private CorporateAccountType corporateAccountType;
    @Column(name = "updated_by")
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CorporateAccountType getCorporateAccountType() {
        return corporateAccountType;
    }

    public void setCorporateAccountType(CorporateAccountType corporateAccountType) {
        this.corporateAccountType = corporateAccountType;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getEliteCommodityCode() {
        return eliteCommodityCode;
    }

    public void setEliteCommodityCode(String eliteCommodityCode) {
        this.eliteCommodityCode = eliteCommodityCode;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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
        if (!(object instanceof CorporateAccount)) {
            return false;
        }
        CorporateAccount other = (CorporateAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.hibernate.dao.CorporateAccount[id=" + id + "]";
    }
}
