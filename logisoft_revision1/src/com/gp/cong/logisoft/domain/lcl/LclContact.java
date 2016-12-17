/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.Domain;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Warehouse;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_contact")
@DynamicInsert(true)
@DynamicUpdate(true)
public class LclContact extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "contact_name")
    private String contactName;
    @Column(name = "company_name")
    private String companyName;
    @Lob
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zip")
    private String zip;
    @Column(name = "country")
    private String country;
    @Column(name = "phone1")
    private String phone1;
    @Column(name = "phone2")
    private String phone2;
    @Column(name = "fax1")
    private String fax1;
    @Column(name = "email1")
    private String email1;
    @Column(name = "email2")
    private String email2;
    @Column(name = "sales_person_code")
    private String salesPersonCode;
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
    @OneToMany(mappedBy = "spContact")
    private List<LclBookingAc> spContactAcList;
    @OneToMany(mappedBy = "clientContact")
    private List<LclBooking> clientContactBookingList;
    @OneToMany(mappedBy = "supContact")
    private List<LclBooking> supContactBookingList;
    @OneToMany(mappedBy = "shipContact")
    private List<LclBooking> shipContactBookingList;
    @OneToMany(mappedBy = "fwdContact")
    private List<LclBooking> fwdContactBookingList;
    @OneToMany(mappedBy = "consContact")
    private List<LclBooking> consContactBookingList;
    @OneToMany(mappedBy = "notyContact")
    private List<LclBooking> notyContactBookingList;
    @OneToMany(mappedBy = "notify2Contact")
    private List<LclBooking> notify2ContactBookingList;
    @OneToMany(mappedBy = "pooWhseContact")
    private List<LclBooking> pooWhseContactList;
    @OneToMany(mappedBy = "rtAgentContact")
    private List<LclBooking> rtAgentContactList;
    @OneToMany(mappedBy = "agentContact")
    private List<LclBooking> agentContactList;
    @OneToMany(mappedBy = "emergencyContact")
    private List<LclBookingHazmat> lclBookingHazmatList;
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User modifiedBy;
    @JoinColumn(name = "entered_by_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false)
    private User enteredBy;
    @JoinColumn(name = "file_number_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LclFileNumber lclFileNumber;
    @OneToMany(mappedBy = "spContact")
    private List<LclBookingPlan> spContactBookingPlanList;
    @OneToMany(mappedBy = "deliveryContact")
    private List<LclBookingPad> deliveryContactBookingList;
    @OneToMany(mappedBy = "pickupContact")
    private List<LclBookingPad> pickupContactList;
    @OneToMany(mappedBy = "thirdPartyContact")
    private List<LclBooking> thirdPartyContactList;
    //Quotation mapping
    @OneToMany(mappedBy = "clientContact")
    private List<LclQuote> lclQuoteClientContact;
    @OneToMany(mappedBy = "supContact")
    private List<LclQuote> lclQuoteSupContact;
    @OneToMany(mappedBy = "rtdAgentContact")
    private List<LclQuote> lclQuoteRtdAgentContact;
    @OneToMany(mappedBy = "agentContact")
    private List<LclQuote> lclQuoteAgentContact;
    @OneToMany(mappedBy = "fdDoorContact")
    private List<LclQuote> lclQuoteFdDoorContact;
    @OneToMany(mappedBy = "pooDoorContact")
    private List<LclQuote> lclQuotePooDoorContact;
    @OneToMany(mappedBy = "pooWhseContact")
    private List<LclQuote> lclQuotePooWhseContact;
    @OneToMany(mappedBy = "shipContact")
    private List<LclQuote> lclQuoteShipContact;
    @OneToMany(mappedBy = "fwdContact")
    private List<LclQuote> lclQuoteFwdContact;
    @OneToMany(mappedBy = "consContact")
    private List<LclQuote> lclQuoteConsContact;
    @OneToMany(mappedBy = "notyContact")
    private List<LclQuote> lclQuoteNotyContact;
    @OneToMany(mappedBy = "spContact")
    private List<LclQuoteAc> lclQuoteAcSpContact;
    @OneToMany(mappedBy = "emergencyContact")
    private List<LclQuoteHazmat> lclQuoteHazmatEmergencyContact;
    @OneToMany(mappedBy = "spContact")
    private List<LclQuotePlan> lclQuotePlanSpContact;
    @OneToMany(mappedBy = "notify2Contact")
    private List<LclQuote> lclQuoteNotify2Contact;
    //New DB Changes
    @ManyToOne
    @JoinColumn(name = "tp_acct_no", referencedColumnName = "acct_no")
    private TradingPartner tradingPartner;
    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;
    @Column(name = "send_notification")
    private boolean sendNotification;
    // @Column(name = "import_Quote_Coload_Retail")
    //  private String importQuoteColoadRetail;

    public LclContact() {
        if (contactName == null) {
            contactName = "";
        }
    }

    public LclContact(Long id) {
        this.id = id;
    }

    public LclContact(Long id, String contactName, Date enteredDatetime, Date modifiedDatetime, User modifiedBy, User enteredBy, LclFileNumber lclFileNumber) {
        this.id = id;
        this.contactName = contactName;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
        this.modifiedBy = modifiedBy;
        this.enteredBy = enteredBy;
        this.lclFileNumber = lclFileNumber;
    }

    public LclContact(Long id, String contactName, String companyName, Date enteredDatetime, Date modifiedDatetime, User modifiedBy, User enteredBy, LclFileNumber lclFileNumber) {
        this.id = id;
        this.contactName = contactName;
        this.companyName = companyName;
        this.enteredDatetime = enteredDatetime;
        this.modifiedDatetime = modifiedDatetime;
        this.modifiedBy = modifiedBy;
        this.enteredBy = enteredBy;
        this.lclFileNumber = lclFileNumber;
    }


    public String getValidLclContact() {
        String appendContactString = "";
        appendContactString += CommonUtils.isNotEmpty(this.contactName) ? this.contactName : CommonUtils.isNotEmpty(this.companyName)
                ? this.companyName : CommonUtils.isNotEmpty(this.email1) ? this.email1 : CommonUtils.isNotEmpty(this.email2)
                ? this.email2 : CommonUtils.isNotEmpty(this.fax1) ? this.fax1 : CommonUtils.isNotEmpty(this.address)
                ? this.address : CommonUtils.isNotEmpty(this.state) ? this.state : CommonUtils.isNotEmpty(this.zip)
                ? this.zip : CommonUtils.isNotEmpty(this.phone1) ? this.phone1 : CommonUtils.isNotEmpty(this.phone2)
                ? this.phone2 : CommonUtils.isNotEmpty(this.country) ? this.country : "";
        return appendContactString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getFax1() {
        return fax1;
    }

    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getSalesPersonCode() {
        return salesPersonCode;
    }

    public void setSalesPersonCode(String salesPersonCode) {
        this.salesPersonCode = salesPersonCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public void setLclFileNumber(LclFileNumber lclFileNumber) {
        this.lclFileNumber = lclFileNumber;
    }

    public List<LclBooking> getClientContactBookingList() {
        return clientContactBookingList;
    }

    public void setClientContactBookingList(List<LclBooking> clientContactBookingList) {
        this.clientContactBookingList = clientContactBookingList;
    }

    public List<LclBooking> getConsContactBookingList() {
        return consContactBookingList;
    }

    public void setConsContactBookingList(List<LclBooking> consContactBookingList) {
        this.consContactBookingList = consContactBookingList;
    }

    public List<LclBooking> getFwdContactBookingList() {
        return fwdContactBookingList;
    }

    public void setFwdContactBookingList(List<LclBooking> fwdContactBookingList) {
        this.fwdContactBookingList = fwdContactBookingList;
    }

    public List<LclBookingHazmat> getLclBookingHazmatList() {
        return lclBookingHazmatList;
    }

    public void setLclBookingHazmatList(List<LclBookingHazmat> lclBookingHazmatList) {
        this.lclBookingHazmatList = lclBookingHazmatList;
    }

    public List<LclBooking> getNotyContactBookingList() {
        return notyContactBookingList;
    }

    public void setNotyContactBookingList(List<LclBooking> notyContactBookingList) {
        this.notyContactBookingList = notyContactBookingList;
    }

    public List<LclBooking> getShipContactBookingList() {
        return shipContactBookingList;
    }

    public void setShipContactBookingList(List<LclBooking> shipContactBookingList) {
        this.shipContactBookingList = shipContactBookingList;
    }

    public List<LclBookingAc> getSpContactAcList() {
        return spContactAcList;
    }

    public void setSpContactAcList(List<LclBookingAc> spContactAcList) {
        this.spContactAcList = spContactAcList;
    }

    public List<LclBookingPlan> getSpContactBookingPlanList() {
        return spContactBookingPlanList;
    }

    public void setSpContactBookingPlanList(List<LclBookingPlan> spContactBookingPlanList) {
        this.spContactBookingPlanList = spContactBookingPlanList;
    }

    public List<LclBooking> getSupContactBookingList() {
        return supContactBookingList;
    }

    public void setSupContactBookingList(List<LclBooking> supContactBookingList) {
        this.supContactBookingList = supContactBookingList;
    }

    public List<LclBooking> getPooWhseContactList() {
        return pooWhseContactList;
    }

    public void setPooWhseContactList(List<LclBooking> pooWhseContactList) {
        this.pooWhseContactList = pooWhseContactList;
    }

    public List<LclBooking> getRtAgentContactList() {
        return rtAgentContactList;
    }

    public void setRtAgentContactList(List<LclBooking> rtAgentContactList) {
        this.rtAgentContactList = rtAgentContactList;
    }

    public List<LclBookingPad> getDeliveryContactBookingList() {
        return deliveryContactBookingList;
    }

    public void setDeliveryContactBookingList(List<LclBookingPad> deliveryContactBookingList) {
        this.deliveryContactBookingList = deliveryContactBookingList;
    }

    public List<LclBookingPad> getPickupContactList() {
        return pickupContactList;
    }

    public void setPickupContactList(List<LclBookingPad> pickupContactList) {
        this.pickupContactList = pickupContactList;
    }

    public List<LclBooking> getThirdPartyContactList() {
        return thirdPartyContactList;
    }

    public void setThirdPartyContactList(List<LclBooking> thirdPartyContactList) {
        this.thirdPartyContactList = thirdPartyContactList;
    }

    public List<LclQuoteAc> getLclQuoteAcSpContact() {
        return lclQuoteAcSpContact;
    }

    public void setLclQuoteAcSpContact(List<LclQuoteAc> lclQuoteAcSpContact) {
        this.lclQuoteAcSpContact = lclQuoteAcSpContact;
    }

    public List<LclQuote> getLclQuoteAgentContact() {
        return lclQuoteAgentContact;
    }

    public void setLclQuoteAgentContact(List<LclQuote> lclQuoteAgentContact) {
        this.lclQuoteAgentContact = lclQuoteAgentContact;
    }

    public List<LclQuote> getLclQuoteClientContact() {
        return lclQuoteClientContact;
    }

    public void setLclQuoteClientContact(List<LclQuote> lclQuoteClientContact) {
        this.lclQuoteClientContact = lclQuoteClientContact;
    }

    public List<LclQuote> getLclQuoteConsContact() {
        return lclQuoteConsContact;
    }

    public void setLclQuoteConsContact(List<LclQuote> lclQuoteConsContact) {
        this.lclQuoteConsContact = lclQuoteConsContact;
    }

    public List<LclQuote> getLclQuoteFdDoorContact() {
        return lclQuoteFdDoorContact;
    }

    public void setLclQuoteFdDoorContact(List<LclQuote> lclQuoteFdDoorContact) {
        this.lclQuoteFdDoorContact = lclQuoteFdDoorContact;
    }

    public List<LclQuote> getLclQuoteFwdContact() {
        return lclQuoteFwdContact;
    }

    public void setLclQuoteFwdContact(List<LclQuote> lclQuoteFwdContact) {
        this.lclQuoteFwdContact = lclQuoteFwdContact;
    }

    public List<LclQuoteHazmat> getLclQuoteHazmatEmergencyContact() {
        return lclQuoteHazmatEmergencyContact;
    }

    public void setLclQuoteHazmatEmergencyContact(List<LclQuoteHazmat> lclQuoteHazmatEmergencyContact) {
        this.lclQuoteHazmatEmergencyContact = lclQuoteHazmatEmergencyContact;
    }

    public List<LclQuote> getLclQuoteNotyContact() {
        return lclQuoteNotyContact;
    }

    public void setLclQuoteNotyContact(List<LclQuote> lclQuoteNotyContact) {
        this.lclQuoteNotyContact = lclQuoteNotyContact;
    }

    public List<LclQuotePlan> getLclQuotePlanSpContact() {
        return lclQuotePlanSpContact;
    }

    public void setLclQuotePlanSpContact(List<LclQuotePlan> lclQuotePlanSpContact) {
        this.lclQuotePlanSpContact = lclQuotePlanSpContact;
    }

    public List<LclQuote> getLclQuotePooDoorContact() {
        return lclQuotePooDoorContact;
    }

    public void setLclQuotePooDoorContact(List<LclQuote> lclQuotePooDoorContact) {
        this.lclQuotePooDoorContact = lclQuotePooDoorContact;
    }

    public List<LclQuote> getLclQuotePooWhseContact() {
        return lclQuotePooWhseContact;
    }

    public void setLclQuotePooWhseContact(List<LclQuote> lclQuotePooWhseContact) {
        this.lclQuotePooWhseContact = lclQuotePooWhseContact;
    }

    public List<LclQuote> getLclQuoteRtdAgentContact() {
        return lclQuoteRtdAgentContact;
    }

    public void setLclQuoteRtdAgentContact(List<LclQuote> lclQuoteRtdAgentContact) {
        this.lclQuoteRtdAgentContact = lclQuoteRtdAgentContact;
    }

    public List<LclQuote> getLclQuoteShipContact() {
        return lclQuoteShipContact;
    }

    public void setLclQuoteShipContact(List<LclQuote> lclQuoteShipContact) {
        this.lclQuoteShipContact = lclQuoteShipContact;
    }

    public List<LclQuote> getLclQuoteSupContact() {
        return lclQuoteSupContact;
    }

    public void setLclQuoteSupContact(List<LclQuote> lclQuoteSupContact) {
        this.lclQuoteSupContact = lclQuoteSupContact;
    }

    public List<LclBooking> getAgentContactList() {
        return agentContactList;
    }

    public void setAgentContactList(List<LclBooking> agentContactList) {
        this.agentContactList = agentContactList;
    }

    public List<LclBooking> getNotify2ContactBookingList() {
        return notify2ContactBookingList;
    }

    public void setNotify2ContactBookingList(List<LclBooking> notify2ContactBookingList) {
        this.notify2ContactBookingList = notify2ContactBookingList;
    }

    public TradingPartner getTradingPartner() {
        return tradingPartner;
    }

    public void setTradingPartner(TradingPartner tradingPartner) {
        this.tradingPartner = tradingPartner;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public List<LclQuote> getLclQuoteNotify2Contact() {
        return lclQuoteNotify2Contact;
    }

    public void setLclQuoteNotify2Contact(List<LclQuote> lclQuoteNotify2Contact) {
        this.lclQuoteNotify2Contact = lclQuoteNotify2Contact;
    }

    public boolean isSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

//    public String getImportQuoteColoadRetail() {
//        return importQuoteColoadRetail;
//    }
//
//    public void setImportQuoteColoadRetail(String importQuoteColoadRetail) {
//        this.importQuoteColoadRetail = importQuoteColoadRetail;
//    }
    public void clear() {
        setId(null);
        setClientContactBookingList(null);
        setSupContactBookingList(null);
        setShipContactBookingList(null);
        setConsContactBookingList(null);
        setFwdContactBookingList(null);
        setNotyContactBookingList(null);
        setNotify2ContactBookingList(null);
        setThirdPartyContactList(null);
        setDeliveryContactBookingList(null);
        setLclBookingHazmatList(null);
        setPickupContactList(null);
        setSpContactAcList(null);
        setSpContactBookingPlanList(null);
        setPooWhseContactList(null);
        setAgentContactList(null);
        setRtAgentContactList(null);
        setLclQuoteAcSpContact(null);
        setLclQuoteAgentContact(null);
        setLclQuoteClientContact(null);
        setLclQuoteConsContact(null);
        setLclQuoteFdDoorContact(null);
        setLclQuoteFwdContact(null);
        setLclQuoteHazmatEmergencyContact(null);
        setLclQuoteNotyContact(null);
        setLclQuotePlanSpContact(null);
        setLclQuotePooDoorContact(null);
        setLclQuotePooWhseContact(null);
        setLclQuoteRtdAgentContact(null);
        setLclQuoteShipContact(null);
        setLclQuoteSupContact(null);
        setLclQuoteNotify2Contact(null);
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
        if (!(object instanceof LclContact)) {
            return false;
        }
        LclContact other = (LclContact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclContact[id=" + id + "]";
    }
}
