package com.logiware.accounting.domain;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.accounting.dao.EdiInvoiceDAO;
import com.logiware.accounting.exception.AccountingException;
import com.logiware.accounting.model.CompanyModel;
import com.logiware.accounting.model.VendorModel;
import com.logiware.common.constants.Company;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 *
 * @author Lakshmi Naryanan
 */
@Entity
@Table(name = "edi_invoice")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EdiInvoice implements Serializable {

    private static final Logger log = Logger.getLogger(EdiInvoice.class);
    private static final long serialVersionUID = -4224068953523950335L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "company", nullable = false)
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
    @Column(name = "edi_reference")
    private String ediReference;
    @Column(name = "edi_code")
    private String ediCode;
    @Column(name = "vendor_name")
    private String vendorName;
    @Column(name = "vendor_number")
    private String vendorNumber;
    @Basic(optional = false)
    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;
    @Basic(optional = false)
    @Column(name = "search_invoice_number", nullable = false)
    private String searchInvoiceNumber;
    @Basic(optional = false)
    @Column(name = "invoice_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date invoiceDate;
    @Basic(optional = false)
    @Column(name = "invoice_amount", nullable = false)
    private Double invoiceAmount;
    @Column(name = "bl_number")
    private String blNumber;
    @Column(name = "our_reference")
    private String ourReference;
    @Column(name = "your_reference1")
    private String yourReference1;
    @Column(name = "your_reference2")
    private String yourReference2;
    @Column(name = "payment_terms")
    private String paymentTerms;
    @Column(name = "vat_percentage")
    private String vatPercentage;
    @Column(name = "vat_amount")
    private String vatAmount;
    @Column(name = "status")
    private String status;
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "disputed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date disputedDate;
    @Column(name = "resolved_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvedDate;
    @Column(name = "from_address")
    private String fromAddress;
    @Column(name = "to_address")
    private String toAddress;
    @Lob
    @Column(name = "bill_to_party")
    private String billToParty;
    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Column(name = "port_of_loading")
    private String portOfLoading;
    @Column(name = "place_of_receipt")
    private String placeOfReceipt;
    @Column(name = "port_of_discharge")
    private String portOfDischarge;
    @Column(name = "place_of_delivery")
    private String placeOfDelivery;
    @Column(name = "vessel_name")
    private String vesselName;
    @Column(name = "booking_number")
    private String bookingNumber;
    @Column(name = "master_bl")
    private String masterBl;
    @Column(name = "currency")
    private String currency;
    @Column(name = "voyage_number")
    private String voyageNumber;
    @Column(name = "etd")
    @Temporal(TemporalType.DATE)
    private Date etd;
    @Column(name = "eta")
    @Temporal(TemporalType.DATE)
    private Date eta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ediInvoice", fetch = FetchType.LAZY)
    private List<EdiInvoiceBank> ediInvoiceBanks;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ediInvoice", fetch = FetchType.LAZY)
    private List<EdiInvoiceParty> ediInvoiceParties;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ediInvoice", fetch = FetchType.LAZY)
    private List<EdiInvoiceDetail> ediInvoiceDetails;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "ediInvoice", fetch = FetchType.LAZY)
    private EdiInvoiceShippingDetails ediInvoiceShippingDetails;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ediInvoice", fetch = FetchType.LAZY)
    private List<EdiInvoiceContainer> ediInvoiceContainers;
    @JoinColumn(name = "edi_invoice_log_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private EdiInvoiceLog ediInvoiceLog;

    public EdiInvoice() {
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

    public String getEdiReference() {
        return ediReference;
    }

    public void setEdiReference(String ediReference) {
        this.ediReference = ediReference;
    }

    public String getEdiCode() {
        return ediCode;
    }

    public void setEdiCode(String ediCode) {
        this.ediCode = ediCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSearchInvoiceNumber() {
        return searchInvoiceNumber;
    }

    public void setSearchInvoiceNumber(String searchInvoiceNumber) {
        this.searchInvoiceNumber = searchInvoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getBlNumber() {
        return blNumber;
    }

    public void setBlNumber(String blNumber) {
        this.blNumber = blNumber;
    }

    public String getOurReference() {
        return ourReference;
    }

    public void setOurReference(String ourReference) {
        this.ourReference = ourReference;
    }

    public String getYourReference1() {
        return yourReference1;
    }

    public void setYourReference1(String yourReference1) {
        this.yourReference1 = yourReference1;
    }

    public String getYourReference2() {
        return yourReference2;
    }

    public void setYourReference2(String yourReference2) {
        this.yourReference2 = yourReference2;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(String vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public String getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(String vatAmount) {
        this.vatAmount = vatAmount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getDisputedDate() {
        return disputedDate;
    }

    public void setDisputedDate(Date disputedDate) {
        this.disputedDate = disputedDate;
    }

    public Date getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPortOfLoading() {
        return portOfLoading;
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public String getPlaceOfReceipt() {
        return placeOfReceipt;
    }

    public void setPlaceOfReceipt(String placeOfReceipt) {
        this.placeOfReceipt = placeOfReceipt;
    }

    public String getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(String portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public String getPlaceOfDelivery() {
        return placeOfDelivery;
    }

    public void setPlaceOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVoyageNumber() {
        return voyageNumber;
    }

    public void setVoyageNumber(String voyageNumber) {
        this.voyageNumber = voyageNumber;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public List<EdiInvoiceBank> getEdiInvoiceBanks() {
        return ediInvoiceBanks;
    }

    public void setEdiInvoiceBanks(List<EdiInvoiceBank> ediInvoiceBanks) {
        this.ediInvoiceBanks = ediInvoiceBanks;
    }

    public List<EdiInvoiceDetail> getEdiInvoiceDetails() {
        return ediInvoiceDetails;
    }

    public void setEdiInvoiceDetails(List<EdiInvoiceDetail> ediInvoiceDetails) {
        this.ediInvoiceDetails = ediInvoiceDetails;
    }

    public List<EdiInvoiceParty> getEdiInvoiceParties() {
        return ediInvoiceParties;
    }

    public void setEdiInvoiceParties(List<EdiInvoiceParty> ediInvoiceParties) {
        this.ediInvoiceParties = ediInvoiceParties;
    }

    public EdiInvoiceShippingDetails getEdiInvoiceShippingDetails() {
        return ediInvoiceShippingDetails;
    }

    public void setEdiInvoiceShippingDetails(EdiInvoiceShippingDetails ediInvoiceShippingDetails) {
        this.ediInvoiceShippingDetails = ediInvoiceShippingDetails;
    }

    public List<EdiInvoiceContainer> getEdiInvoiceContainers() {
        return ediInvoiceContainers;
    }

    public void setEdiInvoiceContainers(List<EdiInvoiceContainer> ediInvoiceContainers) {
        this.ediInvoiceContainers = ediInvoiceContainers;
    }

    public EdiInvoiceLog getEdiInvoiceLog() {
        return ediInvoiceLog;
    }

    public void setEdiInvoiceLog(EdiInvoiceLog ediInvoiceLog) {
        this.ediInvoiceLog = ediInvoiceLog;
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
        if (!(object instanceof EdiInvoice)) {
            return false;
        }
        EdiInvoice other = (EdiInvoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.logiware.accounting.domain.EdiInvoice[ id=" + id + " ]";
    }
    //Temp variables
    private transient boolean isHeader;
    private transient boolean isBody;
    private transient boolean isInformation;
    private transient boolean isDetails;
    private transient boolean isSummary;
    private transient boolean isBank;
    private transient String elementType = null;
    private transient String characterType = null;
    private transient EdiInvoiceParty party = null;
    private transient EdiInvoiceBank bank = null;
    private transient EdiInvoiceDetail detail = null;
    private transient Set<String> elements = new HashSet<String>();

    private void setElementType(StartElement startElement) {
        if (isHeader) {
            if ("Applicationreference".equalsIgnoreCase(startElement.getName().toString())) {
                elementType = "Applicationreference";
                elements.add("Applicationreference");
            } else if ("Reference".equalsIgnoreCase(startElement.getName().toString())) {
                elementType = "Reference";
                elements.add("Reference");
            } else if ("Sender".equalsIgnoreCase(startElement.getName().toString())) {
                elementType = "Sender";
                elements.add("Sender");
            }
        } else if (isBody) {
            if (isInformation) {
                if ("Invoice".equalsIgnoreCase(startElement.getName().toString())) {
                    elementType = "Invoice";
                    elements.add("Invoice");
                } else if ("RelatedReferences".equalsIgnoreCase(startElement.getName().toString())) {
                    elementType = "RelatedReferences";
                    elements.add("RelatedReferences");
                } else if ("Parties".equalsIgnoreCase(startElement.getName().toString())) {
                    Iterator attributes = startElement.getAttributes();
                    if (attributes.hasNext()) {
                        Attribute attribute = (Attribute) (attributes.next());
                        if ("Qualifier".equals(attribute.getName().toString())) {
                            if ("BY".equals(attribute.getValue())) {
                                elementType = "Company";
                                elements.add("BY");
                            } else if ("SU".equals(attribute.getValue())) {
                                elementType = "Vendor";
                                elements.add("SU");
                            }
                        }
                    }
                } else if ("PaymentTerms".equalsIgnoreCase(startElement.getName().toString())) {
                    elementType = "PaymentTerms";
                    elements.add("PaymentTerms");
                } else if ("ShipmentInformation".equalsIgnoreCase(startElement.getName().toString())) {
                    elementType = "ShipmentInformation";
                    elements.add("ShipmentInformation");
                }
            } else if (isDetails) {
                if ("Detail".equalsIgnoreCase(startElement.getName().toString())) {
                    elementType = "Detail";
                    elements.add("Detail");
                }
            } else if (isSummary) {
                if ("TotalMonetaryAmount".equalsIgnoreCase(startElement.getName().toString())) {
                    elementType = "TotalMonetaryAmount";
                    elements.add("TotalMonetaryAmount");
                } else if ("TotalMonetaryAmountGroupByVAT".equalsIgnoreCase(startElement.getName().toString())) {
                    elementType = "TotalMonetaryAmountGroupByVAT";
                    elements.add("TotalMonetaryAmountGroupByVAT");
                }
            }
        }
    }

    private void setCharacterType(StartElement startElement) {
        if (isHeader) {
            if ("Sender".equals(elementType) && "Code".equalsIgnoreCase(startElement.getName().toString())) {
                characterType = "Code";
                elements.add("Code");
            }
        } else if (isBody) {
            if (isInformation) {
                if ("Invoice".equals(elementType)) {
                    characterType = startElement.getName().toString();
                } else if ("RelatedReferences".equalsIgnoreCase(elementType)) {
                    if ("Reference".equalsIgnoreCase(startElement.getName().toString())) {
                        Iterator attributes = startElement.getAttributes();
                        if (attributes.hasNext()) {
                            Attribute attribute = (Attribute) (attributes.next());
                            if ("Qualifier".equals(attribute.getName().toString())) {
                                if ("EFR".equals(attribute.getValue())) {
                                    characterType = "EFR";
                                } else if ("BLR".equals(attribute.getValue())) {
                                    characterType = "BLR";
                                } else if ("CR".equals(attribute.getValue())) {
                                    characterType = "CR";
                                } else if ("TID".equals(attribute.getValue())) {
                                    characterType = "TID";
                                }
                            }
                        }
                    }
                } else if ("Company".equalsIgnoreCase(elementType)) {
                    if (!"NAD".equalsIgnoreCase(startElement.getName().toString())
                            && !"Location".equalsIgnoreCase(startElement.getName().toString())) {
                        characterType = startElement.getName().toString();
                    }
                } else if ("Vendor".equalsIgnoreCase(elementType)) {
                    if ("Bank".equalsIgnoreCase(startElement.getName().toString())) {
                        isBank = true;
                    } else if (!"NAD".equalsIgnoreCase(startElement.getName().toString())
                            && !"Location".equalsIgnoreCase(startElement.getName().toString())) {
                        characterType = startElement.getName().toString();
                    }
                } else if ("PaymentTerms".equalsIgnoreCase(elementType)) {
                    characterType = startElement.getName().toString();
                } else if ("ShipmentInformation".equalsIgnoreCase(elementType)) {
                    if (!"VoyageInformation".equalsIgnoreCase(startElement.getName().toString())
                            && !"Details".equalsIgnoreCase(startElement.getName().toString())
                            && !"Package".equalsIgnoreCase(startElement.getName().toString())) {
                        characterType = startElement.getName().toString();
                    }
                }
            } else if (isDetails) {
                if ("Detail".equalsIgnoreCase(elementType)) {
                    characterType = startElement.getName().toString();
                }
            } else if (isSummary) {
                if ("TotalMonetaryAmount".equalsIgnoreCase(elementType)) {
                    if ("TotalVATIncl".equalsIgnoreCase(startElement.getName().toString())) {
                        characterType = "TotalVATIncl";
                    }
                } else if ("TotalMonetaryAmountGroupByVAT".equalsIgnoreCase(elementType)) {
                    if ("TotalVAT".equalsIgnoreCase(startElement.getName().toString())) {
                        characterType = "TotalVAT";
                    } else if ("VATPercentage".equalsIgnoreCase(startElement.getName().toString())) {
                        characterType = "VATPercentage";
                    }
                }
            }
        }
    }

    private void setValue(Characters text) throws Exception {
        if (!text.isWhiteSpace()) {
            if (isHeader) {
                if ("Applicationreference".equals(elementType) && !"INVOICE".equalsIgnoreCase(text.getData())) {
                    throw new AccountingException("Bad file. Not an Invoice.");
                } else if ("Reference".equals(elementType)) {
                    ediReference = text.getData();
                } else if ("Sender".equals(elementType) && "Code".equals(characterType)) {
                    ediCode = text.getData();
                    VendorModel vendor = new EdiInvoiceDAO().getVendor(ediCode);
                    if (null != vendor && CommonUtils.isNotEmpty(vendor.getVendorNumber())) {
                        vendorNumber = vendor.getVendorNumber();
                        vendorName = vendor.getVendorName();
                    }
                }
            } else if (isBody) {
                if (isInformation) {
                    if ("Invoice".equals(elementType)) {
                        if ("Number".equals(characterType)) {
                            invoiceNumber = text.getData();
                            searchInvoiceNumber = invoiceNumber.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
                        } else if ("Date".equals(characterType)) {
                            invoiceDate = DateUtils.parseDate(text.getData(), "yyyy-MM-dd");
                        }
                    } else if ("RelatedReferences".equals(elementType)) {
                        if ("EFR".equals(characterType)) {
                            ourReference = text.getData();
                        } else if ("BLR".equals(characterType)) {
                            blNumber = text.getData();
                        } else if ("CR".equals(characterType)) {
                            yourReference1 = text.getData();
                        } else if ("TID".equals(characterType)) {
                            yourReference2 = text.getData();
                        }
                    } else if ("Company".equals(elementType)) {
                        if ("Name".equals(characterType)) {
                            party = new EdiInvoiceParty();
                            party.setEdiInvoice(this);
                            party.setType("Company");
                            party.setName(text.getData());
                        } else if ("Street".equals(characterType)) {
                            party.setStreet(text.getData());
                        } else if ("Zip".equals(characterType)) {
                            party.setZip(text.getData());
                        } else if ("City".equals(characterType)) {
                            party.setCity(text.getData());
                        } else if ("Country".equals(characterType)) {
                            party.setCountry(text.getData());
                        } else if ("VATRegistrationNumber".equalsIgnoreCase(characterType)) {
                            party.setVatNumber(text.getData());
                        }
                    } else if ("Vendor".equals(elementType)) {
                        if (isBank) {
                            if ("Name".equals(characterType)) {
                                bank = new EdiInvoiceBank();
                                bank.setEdiInvoice(this);
                                bank.setName(text.getData());
                            } else if ("Street".equals(characterType)) {
                                if (null == bank.getStreet1()) {
                                    bank.setStreet1(text.getData());
                                } else {
                                    bank.setStreet2(text.getData());
                                }
                            } else if ("Zip".equals(characterType)) {
                                bank.setZip(text.getData());
                            } else if ("City".equals(characterType)) {
                                bank.setCity(text.getData());
                            } else if ("Country".equals(characterType)) {
                                bank.setCountry(text.getData());
                            } else if ("UnCode".equals(characterType)) {
                                bank.setUnCode(text.getData());
                            } else if ("AccountNumber".equalsIgnoreCase(characterType)) {
                                bank.setAccount(text.getData());
                            } else if ("IBAN".equalsIgnoreCase(characterType)) {
                                bank.setIban(text.getData());
                            } else if ("BIC".equalsIgnoreCase(characterType)) {
                                bank.setBic(text.getData());
                            }
                        } else {
                            if ("Name".equals(characterType)) {
                                party = new EdiInvoiceParty();
                                party.setEdiInvoice(this);
                                party.setType("Vendor");
                                party.setName(text.getData());
                            } else if ("Street".equals(characterType)) {
                                party.setStreet(text.getData());
                            } else if ("Zip".equals(characterType)) {
                                party.setZip(text.getData());
                            } else if ("City".equals(characterType)) {
                                party.setCity(text.getData());
                            } else if ("Country".equals(characterType)) {
                                party.setCountry(text.getData());
                            } else if ("VATRegistrationNumber".equalsIgnoreCase(characterType)) {
                                party.setVatNumber(text.getData());
                            } else if ("CompanyRegistrationNumber".equalsIgnoreCase(characterType)) {
                                party.setRegistrationNumber(text.getData());
                            } else if ("CompanyLicenseNumber".equalsIgnoreCase(characterType)) {
                                party.setLicenseNumber(text.getData());
                            }
                        }
                    } else if ("PaymentTerms".equalsIgnoreCase(elementType)) {
                        if ("Description".equalsIgnoreCase(characterType)) {
                            paymentTerms = text.getData();
                        }
                    } else if ("ShipmentInformation".equalsIgnoreCase(elementType)) {
                        if ("Vessel".equalsIgnoreCase(characterType)) {
                            ediInvoiceShippingDetails = new EdiInvoiceShippingDetails();
                            ediInvoiceShippingDetails.setEdiInvoice(this);
                            ediInvoiceShippingDetails.setVessel(text.getData());
                        } else if ("Date".equalsIgnoreCase(characterType)) {
                            ediInvoiceShippingDetails.setDate(DateUtils.parseDate(text.getData(), "yyyy-MM-dd"));
                        } else if ("Routing".equalsIgnoreCase(characterType)) {
                            String routing = (null != ediInvoiceShippingDetails.getRouting() ? ediInvoiceShippingDetails.getRouting() : "") + text.getData();
                            ediInvoiceShippingDetails.setRouting(routing);
                        } else if ("Quantity".equalsIgnoreCase(characterType)) {
                            ediInvoiceShippingDetails.setPackageQuantity(text.getData());
                        } else if ("Description".equalsIgnoreCase(characterType)) {
                            ediInvoiceShippingDetails.setPackageDescription(text.getData());
                        } else if ("Weigth".equalsIgnoreCase(characterType)) {
                            ediInvoiceShippingDetails.setWeight(text.getData());
                        } else if ("Volume".equalsIgnoreCase(characterType)) {
                            ediInvoiceShippingDetails.setVolume(text.getData());
                        }
                    }
                } else if (isDetails) {
                    if ("Detail".equalsIgnoreCase(elementType)) {
                        if ("ItemDescription".equals(characterType)) {
                            detail = new EdiInvoiceDetail();
                            detail.setEdiInvoice(this);
                            detail.setDescription(text.getData());
                        } else if ("Quantity".equals(characterType)) {
                            detail.setQuantity(text.getData());
                        } else if ("CalculationCode".equals(characterType)) {
                            detail.setCalculationCode(text.getData());
                        } else if ("Price".equals(characterType)) {
                            detail.setPrice(text.getData());
                        } else if ("Rate".equals(characterType)) {
                            detail.setRate(text.getData());
                        } else if ("Currency".equals(characterType)) {
                            detail.setCurrency(text.getData());
                        } else if ("AmountVATExcl".equals(characterType)) {
                            detail.setVatExcludedAmount(text.getData());
                            detail.setApAmount(text.getData());
                            detail.setArAmount(text.getData());
                        } else if ("AmountVATIncl".equals(characterType)) {
                            detail.setVatIncludedAmount(text.getData());
                        } else if ("AmountVAT".equals(characterType)) {
                            detail.setVatAmount(text.getData());
                        } else if ("VATPercentage".equals(characterType)) {
                            detail.setVatPercentage(text.getData());
                        } else if ("BLReference".equalsIgnoreCase(characterType)) {
                            detail.setBlReference(text.getData());
                        }
                    }
                } else if (isSummary) {
                    if ("TotalMonetaryAmount".equalsIgnoreCase(elementType)) {
                        if ("TotalVATIncl".equals(characterType)) {
                            invoiceAmount = NumberUtils.parseNumber(text.getData());
                        }
                    } else if ("TotalMonetaryAmountGroupByVAT".equalsIgnoreCase(elementType)) {
                        if ("TotalVAT".equals(characterType)) {
                            vatAmount = text.getData();
                        } else if ("VATPercentage".equals(characterType)) {
                            vatPercentage = text.getData();
                        }
                    }
                }
            }
        }
    }

    private void removeCharacterType() {
        characterType = null;
    }

    private void removeElementType(EndElement endElement) throws Exception {
        if (isHeader) {
            if ("Applicationreference".equalsIgnoreCase(endElement.getName().toString())) {
                elementType = null;
            } else if ("Reference".equalsIgnoreCase(endElement.getName().toString())) {
                elementType = null;
            } else if ("Sender".equalsIgnoreCase(endElement.getName().toString())) {
                elementType = null;
            }
        } else if (isBody) {
            User user = new UserDAO().findUserName("system");
            if (isInformation) {
                if ("Invoice".equalsIgnoreCase(endElement.getName().toString())) {
                    elementType = null;
                } else if ("RelatedReferences".equalsIgnoreCase(endElement.getName().toString())) {
                    elementType = null;
                } else if ("Parties".equalsIgnoreCase(endElement.getName().toString())) {
                    if (elementType.equalsIgnoreCase("Vendor")) {
                        isBank = false;
                    }
                    if (null == ediInvoiceParties) {
                        ediInvoiceParties = new ArrayList<EdiInvoiceParty>();
                    }
                    ediInvoiceParties.add(party);
                    elementType = null;
                } else if ("PaymentTerms".equalsIgnoreCase(endElement.getName().toString())) {
                    elementType = null;
                } else if ("Bank".equalsIgnoreCase(endElement.getName().toString())) {
                    if (null == ediInvoiceBanks) {
                        ediInvoiceBanks = new ArrayList<EdiInvoiceBank>();
                    }
                    ediInvoiceBanks.add(bank);
                } else if ("ShipmentInformation".equalsIgnoreCase(endElement.getName().toString())) {
                    elementType = null;
                }
            } else if (isDetails) {
                if ("Detail".equalsIgnoreCase(endElement.getName().toString())) {
                    if (null == ediInvoiceDetails) {
                        ediInvoiceDetails = new ArrayList<EdiInvoiceDetail>();
                    }
                    detail.setInvoiceStatus("0");
                    detail.setChargeStatus("");
                    detail.setUpdatedBy(user);
                    detail.setUpdatedDate(new Date());
                    ediInvoiceDetails.add(detail);
                    elementType = null;
                }
            } else if (isSummary) {
                if ("TotalMonetaryAmount".equalsIgnoreCase(endElement.getName().toString())) {
                    elementType = null;
                } else if ("TotalMonetaryAmountGroupByVAT".equalsIgnoreCase(endElement.getName().toString())) {
                    elementType = null;
                }
            }
        }
    }

    private void createEcuLineInvoice(File file) throws Exception {
        InputStream inputStream = null;
        XMLEventReader eventReader = null;
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputStream = new FileInputStream(file);
            eventReader = inputFactory.createXMLEventReader(inputStream);
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if ("Header".equalsIgnoreCase(startElement.getName().toString())) {
                        isHeader = true;
                        elements.add("Header");
                    } else if ("Body".equalsIgnoreCase(startElement.getName().toString())) {
                        isBody = true;
                        elements.add("Body");
                    } else if (isBody && "Information".equalsIgnoreCase(startElement.getName().toString())) {
                        isInformation = true;
                        elements.add("Information");
                    } else if (isBody && !isInformation && "Details".equalsIgnoreCase(startElement.getName().toString())) {
                        isDetails = true;
                        elements.add("Details");
                    } else if (isBody && !isInformation && !isDetails && "Summary".equalsIgnoreCase(startElement.getName().toString())) {
                        isSummary = true;
                        elements.add("Summary");
                    } else if (null == elementType) {
                        setElementType(startElement);
                    } else if (null != elementType && null == characterType) {
                        setCharacterType(startElement);
                    }
                } else if (event.isCharacters()) {
                    setValue(event.asCharacters());
                } else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (null != characterType && null != elementType) {
                        removeCharacterType();
                    } else if (null != elementType) {
                        removeElementType(endElement);
                    } else if (isSummary && "Summary".equalsIgnoreCase(endElement.getName().toString())) {
                        isSummary = false;
                    } else if (isDetails && "Details".equalsIgnoreCase(endElement.getName().toString())) {
                        isDetails = false;
                    } else if (isBody && "Information".equalsIgnoreCase(endElement.getName().toString())) {
                        isInformation = false;
                    } else if ("Body".equalsIgnoreCase(endElement.getName().toString())) {
                        isBody = false;
                    } else if ("Header".equalsIgnoreCase(endElement.getName().toString())) {
                        isHeader = false;
                    }
                }
            }
            this.company = Company.ECU_LINE;
            status = new EdiInvoiceDAO().getStatus(vendorNumber, invoiceNumber);
            if (!elements.contains("Header")) {
                throw new AccountingException("Bad File. <Header> element missing");
            } else if (!elements.contains("Body")) {
                throw new AccountingException("Bad File. <Body> missing");
            } else if (!elements.contains("Information")) {
                throw new AccountingException("Bad File. <Information> element under <Body> missing");
            } else if (!elements.contains("Details")) {
                throw new AccountingException("Bad File. <Details> element under <Body> missing");
            } else if (!elements.contains("Summary")) {
                throw new AccountingException("Bad File. <Summary> element under <Body> missing");
            } else if (!elements.contains("Applicationreference")) {
                throw new AccountingException("Bad File. <Applicationreference> element under <Header> missing");
            } else if (!elements.contains("Reference")) {
                throw new AccountingException("Bad File. <Reference> element under <Header> missing");
            } else if (!elements.contains("Sender")) {
                throw new AccountingException("Bad File. <Sender> element under <Header> missing");
            } else if (!elements.contains("Code")) {
                throw new AccountingException("Bad File. <Code> element under <Sender> of <Header> missing");
            } else if (!elements.contains("Invoice")) {
                throw new AccountingException("Bad File. <Invoice> element under <Information> element of <Body> missing");
            } else if (!elements.contains("RelatedReferences")) {
                throw new AccountingException("Bad File. <RelatedReferences> element under <Information> element of <Body> missing");
            } else if (!elements.contains("BY")) {
                throw new AccountingException("Bad File. <Parties Qualifier=\"BY\"> under <Information> element of <Body> missing");
            } else if (!elements.contains("SU")) {
                throw new AccountingException("Bad File. <Parties Qualifier=\"SU\"> under <Information> element of <Body> missing");
            } else if (!elements.contains("PaymentTerms")) {
                throw new AccountingException("Bad File. <PaymentTerms> element under <Information> element of <Body> missing");
            } else if (!elements.contains("ShipmentInformation")) {
                throw new AccountingException("Bad File. <ShipmentInformation> element under <Information> element of <Body> missing");
            } else if (!elements.contains("Detail")) {
                throw new AccountingException("Bad File. <Detail> element under <Details> element of <Body> missing");
            } else if (!elements.contains("TotalMonetaryAmount")) {
                throw new AccountingException("Bad File. <TotalMonetaryAmount> element under <Summary> element of <Body> missing");
            } else if (!elements.contains("TotalMonetaryAmountGroupByVAT")) {
                throw new AccountingException("Bad File. <TotalMonetaryAmountGroupByVAT> element under <Summary> element of <Body> missing");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != eventReader) {
                eventReader.close();
            }
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }

    private void createMaerskLineInvoice(File file) throws Exception {
        InputStream inputStream = null;
        try {
            TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
            vendorNumber = "MAEINC0001";
            vendorName = tradingPartnerDAO.getAccountName(this.vendorNumber);
            status = ConstantsInterface.STATUS_EDI_OPEN;
            company = Company.MAERSK_LINE;
            CompanyModel companyModel = new SystemRulesDAO().getCompanyDetails();
            this.billToParty = companyModel.getName() + "\n" + companyModel.getAddress();
            inputStream = new FileInputStream(file);
            List<String> lines = IOUtils.readLines(inputStream);
            ediInvoiceContainers = new ArrayList<EdiInvoiceContainer>();
            ediInvoiceDetails = new ArrayList<EdiInvoiceDetail>();
            for (String line : lines) {
                if (CommonUtils.isStartsWith(line, "B3")) {
                    String[] values = line.split("\\*");
                    invoiceNumber = values[2];	//Invoice Number
                    searchInvoiceNumber = invoiceNumber.replaceAll("[^\\p{Alpha}\\p{Digit}]+", "");
                    etd = DateUtils.parseDate(values[6], "yyyyMMdd");	//ETD
                    invoiceAmount = (Double.parseDouble(values[7].replaceAll("[^0-9.]", "")) / 100);  //Invoice Amount
                    eta = DateUtils.parseDate(values[9], "yyyyMMdd");	//ETA
                    invoiceDate = DateUtils.parseDate(values[12], "yyyyMMdd");	//Invoice Date
                    Vendor vendor = tradingPartnerDAO.getVendor(vendorNumber);
                    Integer termValue = 0;
                    String termDesc = "Due Upon Receipt";
                    if (null != vendor && null != vendor.getCterms()) {
                        termValue = Integer.parseInt(vendor.getCterms().getCode());
                        termDesc = vendor.getCterms().getCodedesc();
                    }
                    dueDate = DateUtils.addDays(invoiceDate, termValue);
                    paymentTerms = termDesc;
                } else if (CommonUtils.isStartsWith(line, "N9")) {
                    String[] values = line.split("\\*");
                    if (StringUtils.equalsIgnoreCase(values[1], "BN")) {
                        bookingNumber = values[2];    //Booking Number
                    } else if (StringUtils.equalsIgnoreCase(values[1], "MB")) {
                        masterBl = values[2];	//Master BL
                    } else if (StringUtils.equalsIgnoreCase(values[1], "SI")) {
                        yourReference1 = values[2];	// Your Reference
                    }
                } else if (CommonUtils.isStartsWith(line, "V1")) {
                    String[] values = line.split("\\*");
                    vesselName = values[2];	//Vessel Name
                    voyageNumber = values[4];	//Voyage Number
                } else if (CommonUtils.isStartsWith(line, "L11")) {
                    String[] values = line.split("\\*");
                    if (StringUtils.equalsIgnoreCase(values[2], "BM")) {
                        blNumber = values[1];	//Bl Number
                    }
                } else if (CommonUtils.isStartsWith(line, "C3")) {
                    String[] values = line.split("\\*");
                    currency = values[1];	//Currency
                } else if (CommonUtils.isStartsWith(line, "R4")) {
                    String[] values = line.split("\\*");
                    if (StringUtils.equalsIgnoreCase(values[1], "D")) {
                        portOfDischarge = values[4];	//Port of Discharge
                    } else if (StringUtils.equalsIgnoreCase(values[1], "E")) {
                        placeOfDelivery = values[4];	//Place of Delivery
                    } else if (StringUtils.equalsIgnoreCase(values[1], "L")) {
                        portOfLoading = values[4];	//Port of Loading
                    } else if (StringUtils.equalsIgnoreCase(values[1], "R")) {
                        placeOfReceipt = values[4];	//Place of Receipt
                    }
                } else if (CommonUtils.isStartsWith(line, "L1")) {
                    String[] values = line.split("\\*");
                    EdiInvoiceDetail ediInvoiceDetail = new EdiInvoiceDetail();
                    ediInvoiceDetail.setEdiInvoice(this);
                    ediInvoiceDetail.setCurrency(currency);
                    ediInvoiceDetail.setDescription(values[12]);	//Description of Charges
                    ediInvoiceDetail.setQuantity(values[17]);	//Qty
                    ediInvoiceDetail.setUom(values[8]);		//UoM
                    ediInvoiceDetail.setPrice(values[2]);	//Unit Price
                    ediInvoiceDetails.add(ediInvoiceDetail);
                } else if (CommonUtils.isStartsWith(line, "N7")) {
                    String[] values = line.split("\\*");
                    ediInvoiceContainers.add(new EdiInvoiceContainer(values[1] + "-" + values[2] + "-" + values[3], this));  //Container No
                }
            }
        } catch (Exception e) {
            log.info("createMaerskLineInvoice failed on " + new Date(), e);
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }
    }

    public EdiInvoice(File file, Company company) throws Exception {
        if (company.equals(Company.ECU_LINE)) {
            createEcuLineInvoice(file);
        } else if (company.equals(Company.MAERSK_LINE)) {
            createMaerskLineInvoice(file);
        }
    }
}
