
package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.hibernate.Domain;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "cust_reports_def")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CustReportDefine extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "lob")
    private String lob;
    @Basic(optional = false)
    @Column(name = "pgmkey")
    private String pgmkey;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "desc")
    private String desc;
    @Column(name = "disabled")
    @Temporal(TemporalType.TIMESTAMP)
    private Date disabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportsDefId")
    private Collection<CustReportOption> custReportOptionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportsDefId")
    private Collection<CustContactReport> custContactReportCollection;

    public CustReportDefine() {
    }

    public CustReportDefine(Long id) {
        this.id = id;
    }

    public CustReportDefine(Long id, String lob, String pgmkey, String name) {
        this.id = id;
        this.lob = lob;
        this.pgmkey = pgmkey;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getPgmkey() {
        return pgmkey;
    }

    public void setPgmkey(String pgmkey) {
        this.pgmkey = pgmkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDisabled() {
        return disabled;
    }

    public void setDisabled(Date disabled) {
        this.disabled = disabled;
    }

    @XmlTransient
    public Collection<CustReportOption> getCustReportOptionCollection() {
        return custReportOptionCollection;
    }

    public void setCustReportOptionCollection(Collection<CustReportOption> custReportOptionCollection) {
        this.custReportOptionCollection = custReportOptionCollection;
    }

    @XmlTransient
    public Collection<CustContactReport> getCustContactReportCollection() {
        return custContactReportCollection;
    }

    public void setCustContactReportCollection(Collection<CustContactReport> custContactReportCollection) {
        this.custContactReportCollection = custContactReportCollection;
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
        if (!(object instanceof CustReportDefine)) {
            return false;
        }
        CustReportDefine other = (CustReportDefine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.hibernate.dao.CustReportsDefine[ id=" + id + " ]";
    }
    
}
