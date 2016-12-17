/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import java.math.BigDecimal;
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
 * @author logiware
 */
@Entity
@Table(name = "lcl_quote_hs_code")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclQuoteHsCode.findAll", query = "SELECT l FROM LclQuoteHsCode l"),
    @NamedQuery(name = "LclQuoteHsCode.findById", query = "SELECT l FROM LclQuoteHsCode l WHERE l.id = :id"),
    @NamedQuery(name = "LclQuoteHsCode.findByCodes", query = "SELECT l FROM LclQuoteHsCode l WHERE l.codes = :codes"),
    @NamedQuery(name = "LclQuoteHsCode.findByNoPieces", query = "SELECT l FROM LclQuoteHsCode l WHERE l.noPieces = :noPieces"),
    @NamedQuery(name = "LclQuoteHsCode.findByWeightMetric", query = "SELECT l FROM LclQuoteHsCode l WHERE l.weightMetric = :weightMetric"),
    @NamedQuery(name = "LclQuoteHsCode.findByEnteredDatetime", query = "SELECT l FROM LclQuoteHsCode l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclQuoteHsCode.findByModifiedDatetime", query = "SELECT l FROM LclQuoteHsCode l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclQuoteHsCode extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "codes")
    private String codes;
    @Column(name = "no_pieces")
    private Integer noPieces;
    @Column(name = "weight_metric")
    private BigDecimal weightMetric;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "quote_piece_id", referencedColumnName = "id")
    @ManyToOne
    private LclQuotePiece lclQuotePiece;
    @JoinColumn(name = "package_type_id", referencedColumnName = "id")
    @ManyToOne
    private PackageType packageType;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;

    public LclQuoteHsCode() {
    }

    public LclQuoteHsCode(Long id) {
        this.id = id;
    }

    public LclQuoteHsCode(Long id, String codes, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.codes = codes;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public Integer getNoPieces() {
        return noPieces;
    }

    public void setNoPieces(Integer noPieces) {
        this.noPieces = noPieces;
    }

    public BigDecimal getWeightMetric() {
        return weightMetric;
    }

    public void setWeightMetric(BigDecimal weightMetric) {
        this.weightMetric = weightMetric;
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

    public LclQuotePiece getLclQuotePiece() {
        return lclQuotePiece;
    }

    public void setLclQuotePiece(LclQuotePiece lclQuotePiece) {
        this.lclQuotePiece = lclQuotePiece;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

     public User getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
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
        if (!(object instanceof LclQuoteHsCode)) {
            return false;
        }
        LclQuoteHsCode other = (LclQuoteHsCode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclQuoteHsCode[id=" + id + "]";
    }

}
