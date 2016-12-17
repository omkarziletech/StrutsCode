package com.gp.cong.logisoft.domain.lcl.kn;

import com.gp.cong.hibernate.Domain;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author palraj.p
 */
@Entity
@Table(name = "kn_bkg_envelope")
@NamedQueries({
    @NamedQuery(name = "BookingEnvelope.findAll", query = "SELECT b FROM BookingEnvelope b")})
public class BookingEnvelope extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "sender_id")
    private String senderId;
    @Basic(optional = false)
    @Column(name = "receiver_id")
    private String receiverId;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "version")
    private String version;
    @Basic(optional = false)
    @Column(name = "envelope_id")
    private String envelopeId;
    @Column(name = "booking_request_file_name")
    private String bookingRequestFileName;
    @Lob
    @Column(name = "booking_request_file")
    private byte[] bookingRequestFile;
    @Lob
    @Column(name = "booking_confirmation_file")
    private byte[] bookingConfirmationFile;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "envelopeId")
    private List<BookingDetail> bookingDetailList;

    public BookingEnvelope() {
    }

    public BookingEnvelope(Long id) {
        this.id = id;
    }

    public BookingEnvelope(Long id, String senderId, String receiverId, String password, String version, String envelopeId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.password = password;
        this.version = version;
        this.envelopeId = envelopeId;
    }

    public String getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(String envelopeId) {
        this.envelopeId = envelopeId;
    }

    public byte[] getBookingConfirmationFile() {
        return bookingConfirmationFile;
    }

    public void setBookingConfirmationFile(byte[] bookingConfirmationFile) {
        this.bookingConfirmationFile = bookingConfirmationFile;
    }

    public byte[] getBookingRequestFile() {
        return bookingRequestFile;
    }

    public void setBookingRequestFile(byte[] bookingRequestFile) {
        this.bookingRequestFile = bookingRequestFile;
    }

    public String getBookingRequestFileName() {
        return bookingRequestFileName;
    }

    public void setBookingRequestFileName(String bookingRequestFileName) {
        this.bookingRequestFileName = bookingRequestFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<BookingDetail> getBookingDetailList() {
        return bookingDetailList;
    }

    public void setBookingDetailList(List<BookingDetail> bookingDetailList) {
        this.bookingDetailList = bookingDetailList;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        if (!(object instanceof BookingEnvelope)) {
            return false;
        }
        BookingEnvelope other = (BookingEnvelope) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.kn.BookingEnvelope[ id=" + id + " ]";
    }
}
