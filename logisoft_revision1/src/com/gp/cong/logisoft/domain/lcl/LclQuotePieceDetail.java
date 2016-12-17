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
 * @author Administrator
 */
@Entity
@Table(name = "lcl_quote_piece_detail")
@DynamicInsert(true)
@DynamicUpdate(true)
@NamedQueries({
    @NamedQuery(name = "LclQuotePieceDetail.findAll", query = "SELECT l FROM LclQuotePieceDetail l"),
    @NamedQuery(name = "LclQuotePieceDetail.findById", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.id = :id"),
    @NamedQuery(name = "LclQuotePieceDetail.findByPieceCount", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.pieceCount = :pieceCount"),
    @NamedQuery(name = "LclQuotePieceDetail.findByActualUom", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.actualUom = :actualUom"),
    @NamedQuery(name = "LclQuotePieceDetail.findByActualLength", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.actualLength = :actualLength"),
    @NamedQuery(name = "LclQuotePieceDetail.findByActualWidth", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.actualWidth = :actualWidth"),
    @NamedQuery(name = "LclQuotePieceDetail.findByActualHeight", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.actualHeight = :actualHeight"),
    @NamedQuery(name = "LclQuotePieceDetail.findByActualWeight", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.actualWeight = :actualWeight"),
    @NamedQuery(name = "LclQuotePieceDetail.findByStowedLocation", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.stowedLocation = :stowedLocation"),
    @NamedQuery(name = "LclQuotePieceDetail.findByMarkNoDesc", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.markNoDesc = :markNoDesc"),
    @NamedQuery(name = "LclQuotePieceDetail.findByEnteredDatetime", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.enteredDatetime = :enteredDatetime"),
    @NamedQuery(name = "LclQuotePieceDetail.findByModifiedDatetime", query = "SELECT l FROM LclQuotePieceDetail l WHERE l.modifiedDatetime = :modifiedDatetime")})
public class LclQuotePieceDetail extends Domain {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "piece_count")
    private Integer pieceCount;
    @Basic(optional = false)
    @Column(name = "actual_uom")
    private String actualUom;
    @Column(name = "actual_length")
    private BigDecimal actualLength;
    @Column(name = "actual_width")
    private BigDecimal actualWidth;
    @Column(name = "actual_height")
    private BigDecimal actualHeight;
    @Column(name = "actual_weight")
    private BigDecimal actualWeight;
    @Column(name = "stowed_location")
    private String stowedLocation;
    @Column(name = "mark_no_desc")
    private String markNoDesc;
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
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "quote_piece_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclQuotePiece quotePiece;

    public LclQuotePieceDetail() {
    }

    public LclQuotePieceDetail(Long id) {
        this.id = id;
    }

    public LclQuotePieceDetail(Long id, int pieceCount, String actualUom, Date enteredDatetime, Date modifiedDatetime) {
        this.id = id;
        this.pieceCount = pieceCount;
        this.actualUom = actualUom;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPieceCount() {
        return pieceCount;
    }

    public void setPieceCount(Integer pieceCount) {
        this.pieceCount = pieceCount;
    }

    public String getActualUom() {
        return actualUom;
    }

    public void setActualUom(String actualUom) {
        this.actualUom = actualUom;
    }

    public BigDecimal getActualLength() {
        return actualLength;
    }

    public void setActualLength(BigDecimal actualLength) {
        this.actualLength = actualLength;
    }

    public BigDecimal getActualWidth() {
        return actualWidth;
    }

    public void setActualWidth(BigDecimal actualWidth) {
        this.actualWidth = actualWidth;
    }

    public BigDecimal getActualHeight() {
        return actualHeight;
    }

    public void setActualHeight(BigDecimal actualHeight) {
        this.actualHeight = actualHeight;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getStowedLocation() {
        return stowedLocation;
    }

    public void setStowedLocation(String stowedLocation) {
        this.stowedLocation = stowedLocation;
    }

    public String getMarkNoDesc() {
        return markNoDesc;
    }

    public void setMarkNoDesc(String markNoDesc) {
        this.markNoDesc = markNoDesc;
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

    public LclQuotePiece getQuotePiece() {
        return quotePiece;
    }

    public void setQuotePiece(LclQuotePiece quotePiece) {
        this.quotePiece = quotePiece;
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
        if (!(object instanceof LclQuotePieceDetail)) {
            return false;
        }
        LclQuotePieceDetail other = (LclQuotePieceDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclQuotePieceDetail[id=" + id + "]";
    }

}
