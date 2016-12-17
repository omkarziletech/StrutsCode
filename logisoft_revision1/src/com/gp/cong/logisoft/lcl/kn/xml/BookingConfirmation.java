package com.gp.cong.logisoft.lcl.kn.xml;

/**
 *
 * @author Rajesh
 */
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "confirmationEnvelope",
    "confirmationDetails"
})
@XmlRootElement(name = "BookingConfirmation")
public class BookingConfirmation {

    @XmlElement(name = "ConfirmationEnvelope", required = true)
    protected BookingConfirmation.ConfirmationEnvelope confirmationEnvelope;
    @XmlElement(name = "ConfirmationDetails", required = true)
    protected List<BookingConfirmation.ConfirmationDetails> confirmationDetails;

    public BookingConfirmation.ConfirmationEnvelope getConfirmationEnvelope() {
        return confirmationEnvelope;
    }

    public void setConfirmationEnvelope(BookingConfirmation.ConfirmationEnvelope value) {
        this.confirmationEnvelope = value;
    }

    public List<BookingConfirmation.ConfirmationDetails> getConfirmationDetails() {
        if (confirmationDetails == null) {
            confirmationDetails = new ArrayList<BookingConfirmation.ConfirmationDetails>();
        }
        return this.confirmationDetails;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "bookingNumber",
        "vesselVoyageID",
        "communicationReference",
        "customerReference",
        "remarks",
        "cfsOrigin",
        "cfsDestination"
    })
    public static class ConfirmationDetails {

        @XmlElement(name = "BookingNumber", required = true)
        protected String bookingNumber;
        @XmlElement(name = "VesselVoyageID", required = true)
        protected String vesselVoyageID;
        @XmlElement(name = "CommunicationReference", required = true)
        protected String communicationReference;
        @XmlElement(name = "CustomerReference", required = true)
        protected String customerReference;
        @XmlElement(name = "Remarks")
        protected String remarks;
        @XmlElement(name = "CFSOrigin")
        protected BookingConfirmation.ConfirmationDetails.CFSOrigin cfsOrigin;
        @XmlElement(name = "CFSDestination")
        protected BookingConfirmation.ConfirmationDetails.CFSDestination cfsDestination;

        public String getBookingNumber() {
            return bookingNumber;
        }

        public void setBookingNumber(String value) {
            this.bookingNumber = value;
        }

        public String getVesselVoyageID() {
            return vesselVoyageID;
        }

        public void setVesselVoyageID(String value) {
            this.vesselVoyageID = value;
        }

        public String getCommunicationReference() {
            return communicationReference;
        }

        public void setCommunicationReference(String value) {
            this.communicationReference = value;
        }

        public String getCustomerReference() {
            return customerReference;
        }

        public void setCustomerReference(String value) {
            this.customerReference = value;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String value) {
            this.remarks = value;
        }

        public BookingConfirmation.ConfirmationDetails.CFSOrigin getCFSOrigin() {
            return cfsOrigin;
        }

        public void setCFSOrigin(BookingConfirmation.ConfirmationDetails.CFSOrigin value) {
            this.cfsOrigin = value;
        }

        public BookingConfirmation.ConfirmationDetails.CFSDestination getCFSDestination() {
            return cfsDestination;
        }

        public void setCFSDestination(BookingConfirmation.ConfirmationDetails.CFSDestination value) {
            this.cfsDestination = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "combinedCompanyNameandAddress",
            "companyName",
            "address",
            "city",
            "postalCode",
            "stateProvince",
            "country",
            "contact",
            "phone",
            "email",
            "open",
            "close"
        })
        public static class CFSDestination {

            @XmlElement(name = "CombinedCompanyNameandAddress")
            protected String combinedCompanyNameandAddress;
            @XmlElement(name = "CompanyName")
            protected String companyName;
            @XmlElement(name = "Address")
            protected String address;
            @XmlElement(name = "City")
            protected String city;
            @XmlElement(name = "PostalCode")
            protected String postalCode;
            @XmlElement(name = "StateProvince")
            protected String stateProvince;
            @XmlElement(name = "Country")
            protected String country;
            @XmlElement(name = "Contact")
            protected String contact;
            @XmlElement(name = "Phone")
            protected String phone;
            @XmlElement(name = "Email")
            protected String email;
            @XmlElement(name = "Open")
            @XmlSchemaType(name = "time")
            protected XMLGregorianCalendar open;
            @XmlElement(name = "Close")
            @XmlSchemaType(name = "time")
            protected XMLGregorianCalendar close;

            public String getCombinedCompanyNameandAddress() {
                return combinedCompanyNameandAddress;
            }

            public void setCombinedCompanyNameandAddress(String value) {
                this.combinedCompanyNameandAddress = value;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String value) {
                this.companyName = value;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String value) {
                this.address = value;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String value) {
                this.city = value;
            }

            public String getPostalCode() {
                return postalCode;
            }

            public void setPostalCode(String value) {
                this.postalCode = value;
            }

            public String getStateProvince() {
                return stateProvince;
            }

            public void setStateProvince(String value) {
                this.stateProvince = value;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String value) {
                this.country = value;
            }

            public String getContact() {
                return contact;
            }

            public void setContact(String value) {
                this.contact = value;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String value) {
                this.phone = value;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String value) {
                this.email = value;
            }

            public XMLGregorianCalendar getOpen() {
                return open;
            }

            public void setOpen(XMLGregorianCalendar value) {
                this.open = value;
            }

            public XMLGregorianCalendar getClose() {
                return close;
            }

            public void setClose(XMLGregorianCalendar value) {
                this.close = value;
            }
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "combinedCompanyNameandAddress",
            "companyName",
            "address",
            "city",
            "postalCode",
            "stateProvince",
            "country",
            "contact",
            "phone",
            "email",
            "open",
            "close"
        })
        public static class CFSOrigin {

            @XmlElement(name = "CombinedCompanyNameandAddress")
            protected String combinedCompanyNameandAddress;
            @XmlElement(name = "CompanyName")
            protected String companyName;
            @XmlElement(name = "Address")
            protected String address;
            @XmlElement(name = "City")
            protected String city;
            @XmlElement(name = "PostalCode")
            protected String postalCode;
            @XmlElement(name = "StateProvince")
            protected String stateProvince;
            @XmlElement(name = "Country")
            protected String country;
            @XmlElement(name = "Contact")
            protected String contact;
            @XmlElement(name = "Phone")
            protected String phone;
            @XmlElement(name = "Email")
            protected String email;
            @XmlElement(name = "Open")
            @XmlSchemaType(name = "time")
            protected XMLGregorianCalendar open;
            @XmlElement(name = "Close")
            @XmlSchemaType(name = "time")
            protected XMLGregorianCalendar close;

            public String getCombinedCompanyNameandAddress() {
                return combinedCompanyNameandAddress;
            }

            public void setCombinedCompanyNameandAddress(String value) {
                this.combinedCompanyNameandAddress = value;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String value) {
                this.companyName = value;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String value) {
                this.address = value;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String value) {
                this.city = value;
            }

            public String getPostalCode() {
                return postalCode;
            }

            public void setPostalCode(String value) {
                this.postalCode = value;
            }

            public String getStateProvince() {
                return stateProvince;
            }

            public void setStateProvince(String value) {
                this.stateProvince = value;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String value) {
                this.country = value;
            }

            public String getContact() {
                return contact;
            }

            public void setContact(String value) {
                this.contact = value;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String value) {
                this.phone = value;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String value) {
                this.email = value;
            }

            public XMLGregorianCalendar getOpen() {
                return open;
            }

            public void setOpen(XMLGregorianCalendar value) {
                this.open = value;
            }

            public XMLGregorianCalendar getClose() {
                return close;
            }

            public void setClose(XMLGregorianCalendar value) {
                this.close = value;
            }
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "senderID",
        "receiverID",
        "password",
        "type",
        "version",
        "envelopeID"
    })
    public static class ConfirmationEnvelope {

        @XmlElement(name = "SenderID", required = true)
        protected String senderID;
        @XmlElement(name = "ReceiverID", required = true)
        protected String receiverID;
        @XmlElement(name = "Password")
        protected String password;
        @XmlElement(name = "Type", required = true)
        protected String type;
        @XmlElement(name = "Version", required = true)
        protected String version;
        @XmlElement(name = "EnvelopeID", required = true)
        protected String envelopeID;

        public String getSenderID() {
            return senderID;
        }

        public void setSenderID(String value) {
            this.senderID = value;
        }

        public String getReceiverID() {
            return receiverID;
        }

        public void setReceiverID(String value) {
            this.receiverID = value;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String value) {
            this.password = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String value) {
            this.type = value;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String value) {
            this.version = value;
        }

        public String getEnvelopeID() {
            return envelopeID;
        }

        public void setEnvelopeID(String value) {
            this.envelopeID = value;
        }
    }
}
