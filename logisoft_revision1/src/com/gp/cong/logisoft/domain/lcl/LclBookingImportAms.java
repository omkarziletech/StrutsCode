package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Logiware
 */
@Entity
@Table(name = "lcl_booking_import_ams")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclBookingImportAms.findAll", query = "SELECT l FROM LclBookingImportAms l"),
    @NamedQuery(name = "LclBookingImportAms.findById", query = "SELECT l FROM LclBookingImportAms l WHERE l.id = :id"),
    @NamedQuery(name = "LclBookingImportAms.findByAmsNo", query = "SELECT l FROM LclBookingImportAms l WHERE l.amsNo = :amsNo"),
    @NamedQuery(name = "LclBookingImportAms.findByPieces", query = "SELECT l FROM LclBookingImportAms l WHERE l.pieces = :pieces"),
    @NamedQuery(name = "LclBookingImportAms.findByEnteredDatetime", query = "SELECT l FROM LclBookingImportAms l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclBookingImportAms.findByModifiedDatetime", query = "SELECT l FROM LclBookingImportAms l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclBookingImportAms extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "ams_no")
    private String amsNo;
    @Basic(optional = false)
    @Column(name = "pieces")
    private int pieces;
    @Column(name = "scac")
    private String scac;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedByUserId;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredByUserId;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @JoinColumn(name = "seg_file_number_id", referencedColumnName = "id")
    @ManyToOne
    private LclFileNumber segregationLclFileNumber;

    public LclBookingImportAms() {
    }

    public LclBookingImportAms(Long id) {
        this.id = id;
    }

    public LclBookingImportAms(Long id, String amsNo, int pieces, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.amsNo = amsNo;
        this.pieces = pieces;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmsNo() {
        return amsNo;
    }

    public void setAmsNo(String amsNo) {
        this.amsNo = amsNo;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
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

    public User getEnteredByUserId() {
        return enteredByUserId;
    }

    public void setEnteredByUserId(User enteredByUserId) {
        this.enteredByUserId = enteredByUserId;
    }

    public User getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(User modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }

    public LclFileNumber getLclFileNumber() {
        return lclFileNumber;
    }

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public String getScac() {
        return scac;
    }

    public void setScac(String scac) {
        this.scac = scac;
    }

    public LclFileNumber getSegregationLclFileNumber() {
        return segregationLclFileNumber;
    }

    public void setSegregationLclFileNumber(LclFileNumber segregationLclFileNumber) {
        this.segregationLclFileNumber = segregationLclFileNumber;
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
        if (!(object instanceof LclBookingImportAms)) {
            return false;
        }
        LclBookingImportAms other = (LclBookingImportAms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclBookingImportAms[id=" + id + "]";
    }
}
