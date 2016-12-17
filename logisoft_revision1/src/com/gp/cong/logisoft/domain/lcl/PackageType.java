/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "package_type")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PackageType extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "abbr01")
    private String abbr01;
    @Column(name = "abbr02")
    private String abbr02;
    @Column(name = "plural")
    private String plural;
    @Basic(optional = false)
    @Column(name = "entered_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDatetime;
    @Basic(optional = false)
    @Column(name = "modified_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDatetime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "packageType")
    private List<LclBookingPiece> lclBookingPieceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "packageType")
    private List<LclQuotePiece> lclQuotePieceList;
     @OneToMany(cascade = CascadeType.ALL, mappedBy = "packageType")
    private List<LclBookingHsCode> lclBookingHsCodeList;
     @OneToMany(cascade = CascadeType.ALL, mappedBy = "packageType")
    private List<LclQuoteHsCode> lclQuoteHsCodeList;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;

    public PackageType() {
    }

    public PackageType(Long id) {
        this.id = id;
    }

    public PackageType(Long id, String description, String abbr01, Date enteredDatetime, Date modifiedDatetime,User modifiedBy, User enteredBy) {
        this.id = id;
        this.description = description;
        this.abbr01 = abbr01;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
        this.modifiedBy = modifiedBy;
        this.enteredBy = enteredBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbbr01() {
        return abbr01;
    }

    public void setAbbr01(String abbr01) {
        this.abbr01 = abbr01;
    }

    public String getAbbr02() {
        return abbr02;
    }

    public void setAbbr02(String abbr02) {
        this.abbr02 = abbr02;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public List<LclBookingHsCode> getLclBookingHsCodeList() {
        return lclBookingHsCodeList;
    }

    public void setLclBookingHsCodeList(List<LclBookingHsCode> lclBookingHsCodeList) {
        this.lclBookingHsCodeList = lclBookingHsCodeList;
    }

    public List<LclQuoteHsCode> getLclQuoteHsCodeList() {
        return lclQuoteHsCodeList;
    }

    public void setLclQuoteHsCodeList(List<LclQuoteHsCode> lclQuoteHsCodeList) {
        this.lclQuoteHsCodeList = lclQuoteHsCodeList;
    }


    @Override
    public Date getEnteredDatetime() {
        return enteredDatetime;
    }

    @Override
    public void setEnteredDatetime(Date enteredDatetime) {
        this.enteredDatetime = enteredDatetime;
    }

    @Override
    public Date getModifiedDatetime() {
        return modifiedDatetime;
    }

    @Override
    public void setModifiedDatetime(Date modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    @Override
    public User getEnteredBy() {
        return enteredBy;
    }

    @Override
    public void setEnteredBy(User enteredBy) {
        this.enteredBy = enteredBy;
    }

    @Override
    public User getModifiedBy() {
        return modifiedBy;
    }

    @Override
    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public List<LclBookingPiece> getLclBookingPieceList() {
        return lclBookingPieceList;
    }

    public void setLclBookingPieceList(List<LclBookingPiece> lclBookingPieceList) {
        this.lclBookingPieceList = lclBookingPieceList;
    }

    public List<LclQuotePiece> getLclQuotePieceList() {
        return lclQuotePieceList;
    }

    public void setLclQuotePieceList(List<LclQuotePiece> lclQuotePieceList) {
        this.lclQuotePieceList = lclQuotePieceList;
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
        if (!(object instanceof PackageType)) {
            return false;
        }
        PackageType other = (PackageType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.PackageType[id=" + id + "]";
    }
}
