/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.hibernate.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Mei
 */
@Entity
@Table(name = "corporate_account_type")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CorporateAccountType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "description")
    private String acctTypeDescription;
    @Basic(optional = false)
    @Column(name = "disabled")
    private Boolean acctTypeDisabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "corporateAccountType")
    private List<CorporateAccount> corporateAccount;
    @Column(name = "updated_by")
    private String updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcctTypeDescription() {
        return acctTypeDescription;
    }

    public void setAcctTypeDescription(String acctTypeDescription) {
        this.acctTypeDescription = acctTypeDescription;
    }

    public Boolean getAcctTypeDisabled() {
        return acctTypeDisabled;
    }

    public void setAcctTypeDisabled(Boolean acctTypeDisabled) {
        this.acctTypeDisabled = acctTypeDisabled;
    }

    public List<CorporateAccount> getCorporateAccount() {
        return corporateAccount;
    }

    public void setCorporateAccount(List<CorporateAccount> corporateAccount) {
        this.corporateAccount = corporateAccount;
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
        if (!(object instanceof CorporateAccountType)) {
            return false;
        }
        CorporateAccountType other = (CorporateAccountType) object;
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
