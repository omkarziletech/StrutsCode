
package com.gp.cong.logisoft.hibernate.dao;

import com.gp.cong.hibernate.Domain;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name = "cust_contact_reports")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CustContactReport extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "freq_min")
    private String freqMin;
    @Basic(optional = false)
    @Column(name = "freq_hour")
    private String freqHour;
    @Basic(optional = false)
    @Column(name = "freq_dom")
    private String freqDom;
    @Basic(optional = false)
    @Column(name = "freq_month")
    private String freqMonth;
    @Basic(optional = false)
    @Column(name = "freq_dow")
    private String freqDow;
    @Column(name = "from_email")
    private String fromEmail;
    @Column(name = "disabled_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date disabledAt;
    @Column(name = "lastran_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastranAt;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "reports_opts_id", referencedColumnName = "id")
    @ManyToOne
    private CustReportOption reportsOptsId;
    @JoinColumn(name = "reports_def_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CustReportDefine reportsDefId;

    public CustContactReport() {
    }

    public CustContactReport(Long id) {
        this.id = id;
    }

    public CustContactReport(Long id, String freqMin, String freqHour, String freqDom, String freqMonth, String freqDow, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.freqMin = freqMin;
        this.freqHour = freqHour;
        this.freqDom = freqDom;
        this.freqMonth = freqMonth;
        this.freqDow = freqDow;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFreqMin() {
        return freqMin;
    }

    public void setFreqMin(String freqMin) {
        this.freqMin = freqMin;
    }

    public String getFreqHour() {
        return freqHour;
    }

    public void setFreqHour(String freqHour) {
        this.freqHour = freqHour;
    }

    public String getFreqDom() {
        return freqDom;
    }

    public void setFreqDom(String freqDom) {
        this.freqDom = freqDom;
    }

    public String getFreqMonth() {
        return freqMonth;
    }

    public void setFreqMonth(String freqMonth) {
        this.freqMonth = freqMonth;
    }

    public String getFreqDow() {
        return freqDow;
    }

    public void setFreqDow(String freqDow) {
        this.freqDow = freqDow;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public Date getDisabledAt() {
        return disabledAt;
    }

    public void setDisabledAt(Date disabledAt) {
        this.disabledAt = disabledAt;
    }

    public Date getLastranAt() {
        return lastranAt;
    }

    public void setLastranAt(Date lastranAt) {
        this.lastranAt = lastranAt;
    }

    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public CustReportOption getReportsOptsId() {
        return reportsOptsId;
    }

    public void setReportsOptsId(CustReportOption reportsOptsId) {
        this.reportsOptsId = reportsOptsId;
    }

    public CustReportDefine getReportsDefId() {
        return reportsDefId;
    }

    public void setReportsDefId(CustReportDefine reportsDefId) {
        this.reportsDefId = reportsDefId;
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
        if (!(object instanceof CustContactReport)) {
            return false;
        }
        CustContactReport other = (CustContactReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.hibernate.dao.CustContactReport[ id=" + id + " ]";
    }
    
}
