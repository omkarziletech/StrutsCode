/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl.bl;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_bl_piece_detail")
public class LclBlPieceDetail extends Domain {

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
    @JoinColumn(name = "booking_piece_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclBlPiece lclBlPiece;

    public LclBlPieceDetail() {
    }

    public LclBlPieceDetail(Long id) {
        this.id = id;
    }

    public LclBlPieceDetail(Long id, int pieceCount, String actualUom, Date enteredDatetime, Date modifiedDatetime) {
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

    public LclBlPiece getLclBlPiece() {
        return lclBlPiece;
    }

    public void setLclBlPiece(LclBlPiece lclBlPiece) {
        this.lclBlPiece = lclBlPiece;
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
        if (!(object instanceof LclBlPieceDetail)) {
            return false;
        }
        LclBlPieceDetail other = (LclBlPieceDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclBlPieceDetail[id=" + id + "]";
    }
}
