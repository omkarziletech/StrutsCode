package com.logiware.accounting.domain;

import com.logiware.common.constants.Company;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "edi_invoice_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EdiInvoiceLog implements Serializable {

    private static final long serialVersionUID = -5126678036250609853L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "com.logiware.common.usertype.GenericEnumUserType",
    parameters = {
	@Parameter(
                    name = "enumClass",
	value = "com.logiware.common.constants.Company"),
	@Parameter(
                    name = "identifierMethod",
	value = "toString"),
	@Parameter(
                    name = "valueOfMethod",
	value = "fromString")
    })
    private Company company;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "file_name")
    private String fileName;
    @Basic(optional = false)
    @Lob
    @Column(name = "file")
    private byte[] file;
    @Lob
    @Column(name = "error")
    private String error;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ediInvoiceLog", fetch = FetchType.LAZY)
    private EdiInvoice ediInvoice;

    public EdiInvoiceLog() {
    }

    public EdiInvoiceLog(Integer id) {
	this.id = id;
    }

    public EdiInvoiceLog(Integer id, String type, String fileName, byte[] file) {
	this.id = id;
	this.type = type;
	this.fileName = fileName;
	this.file = file;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Company getCompany() {
	return company;
    }

    public void setCompany(Company company) {
	this.company = company;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public byte[] getFile() {
	return file;
    }

    public void setFile(byte[] file) {
	this.file = file;
    }

    public String getError() {
	return error;
    }

    public void setError(String error) {
	this.error = error;
    }

    public EdiInvoice getEdiInvoice() {
	return ediInvoice;
    }

    public void setEdiInvoice(EdiInvoice ediInvoice) {
	this.ediInvoice = ediInvoice;
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
	if (!(object instanceof EdiInvoiceLog)) {
	    return false;
	}
	EdiInvoiceLog other = (EdiInvoiceLog) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.logiware.accounting.domain.EdiInvoiceLog[ id=" + id + " ]";
    }
}
