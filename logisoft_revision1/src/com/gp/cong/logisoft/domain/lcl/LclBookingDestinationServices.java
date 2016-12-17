
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name = "lcl_booking_dstsvc")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclBookingDestinationServices extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @Column(name = "country")
    private String country;
    @Basic(optional = false)
    @Column(name = "oncarriage_tt")
    private int oncarriageTt;
    @Basic(optional = false)
    @Column(name = "oncarriage_tt_freq")
    private int oncarriageTtFreq;
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @JoinColumn(name = "booking_ac_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private LclBookingAc lclbookingAc;
    @JoinColumn(name = "oncarriage_acct_no", referencedColumnName = "acct_no")
    @ManyToOne
    private TradingPartner oncarriageAcctNo;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
   
    public LclBookingDestinationServices() {
    }

    public LclBookingDestinationServices(Long id) {
        this.id = id;
    }

    public LclBookingDestinationServices(Long id, String city, String country, int oncarriageTt, int oncarriageTtFreq, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.oncarriageTt = oncarriageTt;
        this.oncarriageTtFreq = oncarriageTtFreq;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getOncarriageTt() {
        return oncarriageTt;
    }

    public void setOncarriageTt(int oncarriageTt) {
        this.oncarriageTt = oncarriageTt;
    }

    public int getOncarriageTtFreq() {
        return oncarriageTtFreq;
    }

    public void setOncarriageTtFreq(int oncarriageTtFreq) {
        this.oncarriageTtFreq = oncarriageTtFreq;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public LclBookingAc getLclbookingAc() {
        return lclbookingAc;
    }

    public void setLclbookingAc(LclBookingAc lclbookingAc) {
        this.lclbookingAc = lclbookingAc;
    }

    public TradingPartner getOncarriageAcctNo() {
        return oncarriageAcctNo;
    }

    public void setOncarriageAcctNo(TradingPartner oncarriageAcctNo) {
        this.oncarriageAcctNo = oncarriageAcctNo;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public User getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
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
        if (!(object instanceof LclBookingDestinationServices)) {
            return false;
        }
        LclBookingDestinationServices other = (LclBookingDestinationServices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclBookingDestinationServices[ id=" + id + " ]";
    }
    
}
