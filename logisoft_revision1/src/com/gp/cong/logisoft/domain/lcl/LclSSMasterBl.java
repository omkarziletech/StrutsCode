/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_ss_masterbl")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclSSMasterBl extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "sp_booking_no")
    private String spBookingNo;
    @Column(name = "master_bl")
    private String masterBl;
    @Column(name = "sp_contract_no")
    private String spContractNo;
    @Basic(optional = false)
    @Column(name = "prepaid_collect")
    private String prepaidCollect;
    @Basic(optional = false)
    @Column(name = "dest_prepaid_collect")
    private String destPrepaidCollect;
    @Basic(optional = false)
    @Column(name = "move_type")
    private String moveType;
    @Column(name = "print_dock_recipt")
    private boolean printDockRecipt;
    @Lob
    @Column(name = "ship_edi")
    private String shipEdi;
    @Lob
    @Column(name = "cons_edi")
    private String consEdi;
    @Lob
    @Column(name = "noty_edi")
    private String notyEdi;
    @Lob
    @Column(name = "export_ref_edi")
    private String exportRefEdi;
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
    @Column(name = "converted_to_ap")
    private boolean convertedToAp;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "ss_header_id", referencedColumnName = "id")
    @ManyToOne
    private LclSsHeader lclSsHeader;
    @JoinColumn(name = "ship_ss_contact_id", referencedColumnName = "id")
    @ManyToOne
    private LclSsContact shipSsContactId;
    @JoinColumn(name = "cons_ss_contact_id", referencedColumnName = "id")
    @ManyToOne
    private LclSsContact consSsContactId;
    @JoinColumn(name = "noty_ss_contact_id", referencedColumnName = "id")
    @ManyToOne
    private LclSsContact notySsContactId;
    @JoinColumn(name = "release_clause", referencedColumnName = "id")
    @ManyToOne
    private GenericCode releaseClause;
    @Column(name = "invoice_value")
    private boolean invoiceValue;
    @Transient
    private String edi;
    @Transient
    private String statusSendEdi;
    @Transient
    private boolean scanAttach;
    @Transient
    private String masterBlApproveStatus;

    public LclSSMasterBl() {
    }

    public LclSSMasterBl(Long id) {
        this.id = id;
    }

    public String getConsEdi() {
        return consEdi;
    }

    public void setConsEdi(String consEdi) {
        this.consEdi = consEdi;
    }

    public String getExportRefEdi() {
        return exportRefEdi;
    }

    public void setExportRefEdi(String exportRefEdi) {
        this.exportRefEdi = exportRefEdi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LclSsHeader getLclSsHeader() {
        return lclSsHeader;
    }

    public void setLclSsHeader(LclSsHeader lclSsHeader) {
        this.lclSsHeader = lclSsHeader;
    }

    public String getNotyEdi() {
        return notyEdi;
    }

    public void setNotyEdi(String notyEdi) {
        this.notyEdi = notyEdi;
    }

    public String getPrepaidCollect() {
        return prepaidCollect;
    }

    public void setPrepaidCollect(String prepaidCollect) {
        this.prepaidCollect = prepaidCollect;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getShipEdi() {
        return shipEdi;
    }

    public void setShipEdi(String shipEdi) {
        this.shipEdi = shipEdi;
    }

    public String getSpBookingNo() {
        return spBookingNo;
    }

    public void setSpBookingNo(String spBookingNo) {
        this.spBookingNo = spBookingNo;
    }

    public String getSpContractNo() {
        return spContractNo;
    }

    public void setSpContractNo(String spContractNo) {
        this.spContractNo = spContractNo;
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

    public LclSsContact getConsSsContactId() {
        return consSsContactId;
    }

    public void setConsSsContactId(LclSsContact consSsContactId) {
        this.consSsContactId = consSsContactId;
    }

    public LclSsContact getNotySsContactId() {
        return notySsContactId;
    }

    public void setNotySsContactId(LclSsContact notySsContactId) {
        this.notySsContactId = notySsContactId;
    }

    public LclSsContact getShipSsContactId() {
        return shipSsContactId;
    }

    public void setShipSsContactId(LclSsContact shipSsContactId) {
        this.shipSsContactId = shipSsContactId;
    }

    public String getEdi() {
        return edi;
    }

    public void setEdi(String edi) {
        this.edi = edi;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public List<LclUnitSs> getUnits() {
        List<LclUnitSs> unitSsList = new ArrayList<LclUnitSs>();
        for (LclUnitSs unitSs : this.lclSsHeader.getLclUnitSsList()) {
            if (this.spBookingNo.equals(unitSs.getSpBookingNo())) {
                unitSsList.add(unitSs);
            }
        }
        return unitSsList;
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
        if (!(object instanceof LclSSMasterBl)) {
            return false;
        }
        LclSSMasterBl other = (LclSSMasterBl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.bl.LclSSMasterBl[id=" + id + "]";
    }

    public String getStatusSendEdi() {
        return statusSendEdi;
    }

    public void setStatusSendEdi(String statusSendEdi) {
        this.statusSendEdi = statusSendEdi;
    }

    public GenericCode getReleaseClause() {
        return releaseClause;
    }

    public void setReleaseClause(GenericCode releaseClause) {
        this.releaseClause = releaseClause;
    }

    public boolean isScanAttach() {
        return scanAttach;
    }

    public void setScanAttach(boolean scanAttach) {
        this.scanAttach = scanAttach;
    }

    public String getMasterBlApproveStatus() {
        return masterBlApproveStatus;
    }

    public void setMasterBlApproveStatus(String masterBlApproveStatus) {
        this.masterBlApproveStatus = masterBlApproveStatus;
    }

    public String getDestPrepaidCollect() {
        return destPrepaidCollect;
    }

    public void setDestPrepaidCollect(String destPrepaidCollect) {
        this.destPrepaidCollect = destPrepaidCollect;
    }

    public boolean isPrintDockRecipt() {
        return printDockRecipt;
    }

    public void setPrintDockRecipt(boolean printDockRecipt) {
        this.printDockRecipt = printDockRecipt;
    }

    public boolean isConvertedToAp() {
        return convertedToAp;
    }

    public void setConvertedToAp(boolean convertedToAp) {
        this.convertedToAp = convertedToAp;
    }

    public boolean isInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(boolean invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

}
