
package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.hibernate.Domain;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "cust_report_opts")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CustReportOption extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "option")
    private String option;
    @JoinColumn(name = "reports_def_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CustReportDefine reportsDefId;
    @OneToMany(mappedBy = "reportsOptsId")
    private Collection<CustContactReport> custContactReportCollection;

    public CustReportOption() {
    }

    public CustReportOption(Long id) {
        this.id = id;
    }

    public CustReportOption(Long id, String option) {
        this.id = id;
        this.option = option;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public CustReportDefine getReportsDefId() {
        return reportsDefId;
    }

    public void setReportsDefId(CustReportDefine reportsDefId) {
        this.reportsDefId = reportsDefId;
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
        if (!(object instanceof CustReportOption)) {
            return false;
        }
        CustReportOption other = (CustReportOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.hibernate.dao.CustReportOption[ id=" + id + " ]";
    }
    
}
