/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.hibernate;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Owner
 */
@Entity
@Table(name = "fcl_bl")
public class FclBlNew extends Domain {

    private static final long serialVersionUID = 1L;
    @Column(name = "BolId")
    private String bolId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Bol")
    private Integer bol;
    @Basic(optional = false)
    @Column(name = "Bol_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date boldate;
    @Column(name = "BookingNo")
    private String bookingNo;
    @Column(name = "QuuoteNo")
    private String quuoteNo;
    @Column(name = "Terminal")
    private String terminal;
    @Column(name = "Port")
    private String port;
    @Column(name = "PortofDischarge")
    private String portofDischarge;
    @JoinColumn(name = "Vessel", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private GenericCode vessel;
    @Column(name = "Voyages")
    private String voyages;
    @Column(name = "Shipper_No")
    private String shipperNo;
    @Column(name = "Forward_agent_No")
    private String forwardagentNo;
    @Column(name = "ramp_check")
    private String rampCheck;
    @Column(name = "Streamship_notify_party")
    private String streamshipnotifyparty;
    @Column(name = "House_Shipper_No")
    private String houseShipperNo;
    @Column(name = "House_Notify_Party")
    private String houseNotifyParty;
    @Column(name = "Steam_Ship_BL")
    private String streamShipBL;
    @Column(name = "House_BL")
    private String houseBL;
    @Column(name = "Import_Orgin_BLNo")
    private String importOrginBLNo;
    @Column(name = "Place_of_Reciept")
    private String placeofReciept;
    @Column(name = "Point_of_Country_and_Origin")
    private String pointofCountryandOrigin;
    @Column(name = "Forwarding_Agent")
    private String forwardingAgent;
    @Column(name = "Export_reference")
    private String exportreference;
    @Column(name = "Domestic_routing")
    private String domesticrouting;
    @Column(name = "Transhipment_To")
    private String transhipmentTo;
    @Column(name = "Onward_Inland_Routing")
    private String onwardInlandRouting;
    @Column(name = "Port_of_Loading")
    private String portofLoading;
    @Column(name = "consignee_No")
    private String consigneeNo;
    @Column(name = "houseConsignee")
    private String houseConsignee;
    @Column(name = "shipper_address")
    private String shipperAddress;
    @Column(name = "consignee_address")
    private String consigneeAddress;
    @Column(name = "house_shipper_address")
    private String houseShipperAddress;
    @Column(name = "house_consignee_address")
    private String houseConsigneeAddress;
    @Column(name = "streamShipLine")
    private String streamShipLine;
    @Column(name = "total")
    private Double total;
    @Column(name = "billTrdPrty")
    private String billTrdPrty;
    @Column(name = "total_costs")
    private Double totalCosts;
    @Column(name = "forwarder_no")
    private String forwarderNo;
    @Column(name = "rate_by_agent")
    private String rateByAgent;
    @Column(name = "notify_party")
    private String notifyParty;
    @Column(name = "house_notify_party_no")
    private String houseNotifyPartyNo;
    @Column(name = "bill_third_party_address")
    private String billThirdPartyAddress;
    @Column(name = "total_cost_code")
    private Double totalCostCode;
    @Column(name = "master_bl")
    private String masterBl;
    @Column(name = "container_no")
    private String containerNo;
    @Column(name = "ready_to_post")
    private String readyToPost;
    @Column(name = "status")
    private String status;
    @Column(name = "bl_terms")
    private String blTerms;
    @Column(name = "shipment_type")
    private String shipmentType;
    @Column(name = "agent_name")
    private String agentName;
    @Column(name = "house_shipper_name")
    private String houseShipperName;
    @Column(name = "shipper_name")
    private String shipperName;
    @Column(name = "consignee_name")
    private String consigneeName;
    @Column(name = "house_consignee_name")
    private String houseConsigneeName;
    @Column(name = "notify_party_name")
    private String notifyPartyName;
    @Column(name = "house_notify_party_name")
    private String houseNotifyPartyName;
    @Column(name = "forwarding_agent_name")
    private String forwardingAgentName;
    @Column(name = "original_terminal")
    private String originalTerminal;
    @Column(name = "billing_terminal")
    private String billingTerminal;
    @Column(name = "third_party_name")
    private String thirdPartyName;
    @Column(name = "agent_address")
    private String agentAddress;
    @Column(name = "sail_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sailDate;
    @Column(name = "book_no")
    private String bookNo;
    @Column(name = "original_printing")
    private String originalPrinting;
    @Column(name = "non_negotiable_printing")
    private String nonNegotiablePrinting;
    @Column(name = "express_bl_printing")
    private String expressBlPrinting;
    @Column(name = "no_of_originals")
    private String noOfOriginals;
    @Column(name = "pre_carriage")
    private String preCarriage;
    @Column(name = "pre_carriage_POR")
    private String precarriagePOR;
    @Column(name = "loading_pier")
    private String loadingPier;
    @Column(name = "shipper_source")
    private String shipperSource;
    @Column(name = "consignee_source")
    private String editAgentNameCheck;
    @Column(name = "editAgentName_Check")
    private String consigneeSource;
    @Column(name = "notifyParty_source")
    private String notifyPartysource;
    @Column(name = "master_bl_comments")
    private String masterBlComments;
    @Column(name = "master_bl_notes")
    private String masterBlNotes;
    @Column(name = "value_of_goods")
    private String valueOfGoods;
    @Column(name = "bl_by")
    private String blBy;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name="updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "bkg_no")
    private Integer bkgNo;
    @Column(name = "imports_freight_release")
    private String importsFreightRelease;
    @Column(name = "file_number")
    private String fileNumber;
    @Column(name = "inttra")
    private String inttra;
    @Column(name = "ready_to_edi")
    private String readyToEdi;
    @Column(name = "bl_audited")
    private String blAudited;
    @Column(name = "received_master")
    private String receivedMaster;
    @Column(name = "bl_closed")
    private String blClosed;
    @Column(name = "AES")
    private String aes;
    @Column(name = "filetype")
    private String filetype;
    @Column(name = "file_no")
    private String fileNo;
    @Column(name = "ssline_name")
    private String sslineName;
    @Column(name = "ssline_no")
    private String sslineNo;
    @Column(name = "original_bl_required")
    private String originalBlRequired;
    @Column(name = "fclbl_clause")
    private String fclblClause;
    @Column(name = "fclbl_clause_description")
    private String fclblClauseDescription;
    @Column(name = "print_containers_on_bl")
    private String printContainersOnBl;
    @Column(name = "shipper_loads_and_counts")
    private String shipperLoadsAndCounts;
    @Column(name = "print_phrase")
    private String printPhrase;
    @Column(name = "agents_for_carrier")
    private String agentsForCarrier;
    @Column(name = "alternate_pol")
    private String alternatePol;
    @Column(name = "manifest_print_report")
    private String manifestPrintReport;
    @Column(name = "default_agent")
    private String defaultAgent;
    @Column(name = "agent")
    private String agent;
    @Column(name = "agent_no")
    private String agentNo;
    @Column(name = "bill_to_party")
    private String billToParty;
    @Column(name = "ss_bldestinationChargesPreCol")
    private String ssbldestinationChargesPreCol;
    @Column(name = "dest_remarks")
    private String destRemarks;
    @Column(name = "move_type")
    private String moveType;
    @Column(name = "Zip")
    private String zip;
    @Column(name = "ramp_city")
    private String rampCity;
    @Column(name = "no_days")
    private String noDays;
    @Column(name = "routed_by_agent")
    private String routedByAgent;
    @Column(name = "routedby_agents_country")
    private String routedbyAgentsCountry;
    @Column(name = "VoyageInternal")
    private String voyageInternal;
    @Column(name = "rates_remarks")
    private String ratesRemarks;
    @Column(name = "door_of_origin")
    private String doorOfOrigin;
    @Column(name = "door_of_destination")
    private String doorOfDestination;
    @Column(name = "line_move")
    private String lineMove;
    @Column(name = "commodity_code")
    private String commodityCode;
    @Column(name = "commodity_desc")
    private String commodityDesc;
    @Column(name = "routedAgent_check")
    private String routedAgentcheck;
    @Column(name = "eta")
    @Temporal(TemporalType.DATE)
    private Date eta;
    @Column(name = "port_cut_off")
    @Temporal(TemporalType.TIMESTAMP)
    private Date portCutOff;
    @Column(name = "doc_cut_off")
    @Temporal(TemporalType.TIMESTAMP)
    private Date docCutOff;
    @Column(name = "earlier_PickUp_Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date earlierPickUpDate;
    @Column(name = "no_of_packages")
    private String noOfPackages;
    @Column(name = "auto_deduct_ffcomm")
    private String autoDeductFfcomm;
    @Column(name = "manifested_by")
    private String manifestedBy;
    @Column(name = "commissionsAdded_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commissionsAddeddate;
    @Column(name = "accrualConverted_by")
    private String accrualConvertedby;
    @Column(name = "accrualConverted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accrualConverteddate;
    @Column(name = "bill_to_code")
    private String billToCode;
    
    @Column(name = "manifested_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date manifestedDate;
    
    @Column(name = "receivedMaster_by")
    private String receivedMasterby;
    @Column(name = "receivedMaster_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedMasterdate;
    @Column(name = "audited_by")
    private String auditedBy;
    @Column(name = "audited_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date auditedDate;
    @Column(name = "closed_by")
    private String closedBy;
    @Column(name = "closed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedDate;
    @Column(name = "commissionsAdded_by")
    private String commissionsAddedby;
    @Column(name = "confirm_on_board")
    private String confirmOnBoard;
    @Column(name = "verfiy_ETA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verfiyETA;
    @Column(name = "confirm_by")
    private String confirmBy;
    @Column(name = "confirm_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmOn;
    @Column(name = "void")
    private String void1;
    @Column(name = "void_by")
    private String voidBy;
    @Column(name = "void_date")
    @Temporal(TemporalType.DATE)
    private Date voidDate;
    @Column(name = "conf_onboard_comments")
    private String confOnboardComments;
    @Column(name = "destination_charges_pre_col")
    private String destinationChargesPreCol;
    @Column(name = "total_containers")
    private String totalContainers;
    @Column(name = "country_of_origin")
    private String countryOfOrigin;
    @Column(name = "new_master_bL")
    private String newmasterbL;
    @Column(name = "proof")
    private String proof;
    @Column(name = "pre_alert")
    private String preAlert;
    @Column(name = "non_negotiable")
    private String nonNegotiable;
    @Column(name = "Import_AMS_House_Bl")
    private String importAMSHouseBl;
    @Column(name = "importFlag")
    private String importFlag;
    @Column(name = "import_release")
    private String importRelease;
    @Column(name = "import_verified_eta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date importVerifiedEta;
    @Column(name = "import_release_comments")
    private String importReleaseComments;
    @Column(name = "send_copy_to")
    private String sendCopyTo;
    @Column(name = "master_consignee_check")
    private String masterConsigneeCheck;
    @Column(name = "master_notify_check")
    private String masterNotifyCheck;
    @Column(name = "consignee_check")
    private String consigneeCheck;
    @Column(name = "notify_check")
    private String notifyCheck;
    @Column(name = "date_in_yard")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInYard;
    @Column(name = "date_out_yard")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOutYard;
    @Column(name = "manifest_rev")
    private String manifestRev;
    @Column(name = "print_rev")
    private String printRev;
    @Column(name = "inbond_number")
    private String inbondNumber;
    @Column(name = "inbond_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inbondDate;
    @Column(name = "inbond_port")
    private String inbondPort;
    @Column(name = "door_origin_as_plor")
    private String doorOriginAsPlor;
    @Column(name = "door_destination_as_final_delivery_house")
    private String doorDestinationAsFinalDeliveryHouse;
    @Column(name = "collect_third_party")
    private String collectThirdParty;
    @Column(name = "door_destination_as_final_delivery_master")
    private String doorDestinationAsFinalDeliveryMaster;
    @Column(name = "house_shipper_check")
    private String houseShipperCheck;
    @Column(name = "check_number")
    private String checkNumber;
    @Column(name = "paid_by")
    private String paidBy;
    @Column(name = "payment_release")
    private String paymentRelease;
    @Column(name = "payment_released_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentReleasedOn;
    @Column(name = "payment_release_comment")
    private String paymentReleaseComment;
    @Column(name = "payment_amount")
    private Double paymentAmount;
    @Column(name = "edi_created_by")
    private String ediCreatedBy;
    @Column(name = "edi_created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ediCreatedOn;
    @Column(name = "alternate_port")
    private String alternatePort;
    @Column(name = "edi_consignee_check")
    private String ediConsigneeCheck;
    @Column(name = "edi_shipper_check")
    private String ediShipperCheck;
    @Column(name = "edi_notifyparty_check")
    private String ediNotifypartyCheck;
    @Column(name = "print_alternate_port")
    private String printAlternatePort;
    @Column(name = "hbl_pol_override")
    private String hblPOLOverride;
    @Column(name = "hbl_pod_override")
    private String hblPODOverride;
    @Column(name = "hbl_fd_override")
    private String hblFDOverride;
    @Column(name = "hbl_pol")
    private String hblPOL;
    @Column(name = "hbl_pod")
    private String hblPOD;
    @Column(name = "hbl_fd")
    private String hblFD;
    @Column(name = "trim_trailing_zeros_for_qty")
    private String trimTrailingZerosForQty;
    @Column(name = "certified_true_copy")
    private String certifiedTrueCopy;
    @Column(name = "omit_term_and_port")
    private String omitTermAndPort;
    @Column(name = "service_Contract_No")
    private String serviceContractNo;
    @Column(name = "break_bulk")
    private String breakBulk;
    @Column(name = "hazmat")
    private String hazmat;
    @Column(name = "rates_non_rates")
    private String ratesNonRates;
    @Column(name = "corrected_after_manifest")
    private String correctedAfterManifest;
    @Column(name = "cost_of_goods")
    private Double costOfGoods;
    @Column(name = "edit_house_shipper_check")
    private String editHouseShipperCheck;
    @Column(name = "insurance")
    private String insurance;
    @Column(name = "insurance_rate")
    private Double insuranceRate;
    @Column(name = "directconsign_check")
    private String directconsignCheck;
    @Column(name = "over_paid_status")
    private Boolean overPaidStatus;
    @Column(name = "converted_to_ap")
    private Boolean convertedToAp;
    @Column(name = "import_warehouse_name")
    private String importWarehouseName;
    @Column(name = "import_warehouse_code")
    private String importWarehouseCode;
    @Column(name = "import_warehouse_address")
    private String importWarehouseAddress;
    @Column(name = "import_pickup_remarks")
    private String importPickupRemarks;
    @Column(name = "edit_house_notify_check")
    private String editHouseNotifyCheck;
    @Column(name = "edit_house_consignee_check")
    private String editHouseConsigneeCheck;
    @Column(name = "internal_remark")
    private String internalRemark;
    @Column(name = "local_drayage")
    private String localDrayage;
    @Column(name = "rated_manifest")
    private String ratedManifest;
    @Column(name = "omit_letter_countrycode")
    private String omit2LetterCountryCode;
    @Column(name = "door_origin_plor_house")
    private String doorOriginAsPlorHouse;
    @Column(name = "dock_receipt")
    private String dockReceipt;
    @Column(name = "alternate_no_of_packages")
    private String alternateNoOfPackages;
    @Column(name = "booking_contact")
    private String bookingContact;
    @Column(name = "hbl_place_receipt_override")
    private String hblPlaceReceiptOverride;
    @Column(name = "hbl_place_receipt")
    private String hblPlaceReceipt;
    @Column(name = "spot_rate")
    private String spotRate;
    @Column(name = "resend_cost_to_blue")
    private String resendCostToBlue = "No";
    @Column(name = "brand")
    private String brand;
    @Column(name = "express_release")
    private String expressRelease;
    @Column(name = "express_release_comment")
    private String expressReleaseComment;
    @Column(name = "express_released_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expressReleasedOn;
    @Column(name = "customs_clearance")
    private String customsClearance;
    @Column(name = "customs_clearance_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date customsClearanceOn;
    @Column(name = "customs_clearance_comment")
    private String customsClearanceComment;
    @Column(name = "delivery_order")
    private String deliveryOrder;
    @Column(name = "delivery_order_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryOrderOn;
    @Column(name = "delivery_order_comment")
    private String deliveryOrderComment;
    @OneToMany(mappedBy = "fclBl", fetch = FetchType.LAZY)
    private List<FclAesDetailsNew> fclAesDetailsList;
    
    
    @OneToMany(mappedBy = "fclBl", fetch = FetchType.LAZY)
    private List<Fclblcorrections> fclblcorrectionsList;
    @OneToMany(mappedBy = "fclBl", fetch = FetchType.LAZY)
    private List<AesHistoryNew> aesHistoryList;
    @OneToMany(mappedBy = "fclBl", fetch = FetchType.LAZY)
    private List<FclInbonddetails> fclInbondDetailsList;
    @OneToMany(mappedBy = "fclBl", fetch = FetchType.LAZY)
    private List<FclBlChargesNew> fclBlChargesList;
    @OneToMany(mappedBy = "fclBl", fetch = FetchType.LAZY)
    private List<FclBlCostcodes> fclBlCostcodesList;
    @OneToMany(mappedBy = "fclBl", fetch = FetchType.LAZY)
    private List<FclBlContainerDtls> fclBlContainerDtlsList;
    @OneToMany(mappedBy = "fclBl", fetch = FetchType.LAZY)
    private List<Sedfilings> sedFilingsList;

    public FclBlNew() {
    }

    public FclBlNew(Integer bol) {
        this.bol = bol;
    }

    public String getBolId() {
        return bolId;
    }

    public void setBolId(String bolId) {
        this.bolId = bolId;
    }

    public Integer getBol() {
        return bol;
    }

    public void setBol(Integer bol) {
        this.bol = bol;
    }

    public Date getBoldate() {
        return boldate;
    }

    public void setBoldate(Date boldate) {
        this.boldate = boldate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getQuuoteNo() {
        return quuoteNo;
    }

    public void setQuuoteNo(String quuoteNo) {
        this.quuoteNo = quuoteNo;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPortofDischarge() {
        return portofDischarge;
    }

    public void setPortofDischarge(String portofDischarge) {
        this.portofDischarge = portofDischarge;
    }

    public GenericCode getVessel() {
        return vessel;
    }

    public void setVessel(GenericCode vessel) {
        this.vessel = vessel;
    }

    public String getVoyages() {
        return null != voyages ? voyages.toUpperCase() : "";
    }

    public void setVoyages(String voyages) {
        this.voyages = null != voyages ? voyages.toUpperCase() : "";
    }

    public String getShipperNo() {
        return shipperNo;
    }

    public void setShipperNo(String shipperNo) {
        this.shipperNo = shipperNo;
    }

    public String getForwardagentNo() {
        return forwardagentNo;
    }

    public void setForwardagentNo(String forwardagentNo) {
        this.forwardagentNo = forwardagentNo;
    }

    public String getRampCheck() {
        return rampCheck;
    }

    public void setRampCheck(String rampCheck) {
        this.rampCheck = rampCheck;
    }

    public String getStreamshipnotifyparty() {
        return null != streamshipnotifyparty ? streamshipnotifyparty.toUpperCase() : "";
    }

    public void setStreamshipnotifyparty(String streamshipnotifyparty) {
        this.streamshipnotifyparty = null != streamshipnotifyparty ? streamshipnotifyparty.toUpperCase() : "";
    }

    public String getHouseShipperNo() {
        return houseShipperNo;
    }

    public void setHouseShipperNo(String houseShipperNo) {
        this.houseShipperNo = houseShipperNo;
    }

    public String getHouseNotifyParty() {
        return null != houseNotifyParty ? houseNotifyParty.toUpperCase() : "";
    }

    public void setHouseNotifyParty(String houseNotifyParty) {
        this.houseNotifyParty = null != houseNotifyParty ? houseNotifyParty.toUpperCase() : "";
    }

    public String getStreamShipBL() {
        if ("P-Prepaid".equalsIgnoreCase(streamShipBL)) {
            return "P";
        } else if ("C-Collect".equalsIgnoreCase(streamShipBL)) {
            return "C";
        } else if ("T-Third Party".equalsIgnoreCase(streamShipBL)) {
            return "T";
        }
        return null;
    }

    public void setStreamShipBL(String streamShipBL) {
        if ("P".equals(streamShipBL)) {
            this.streamShipBL = "P-Prepaid";
        } else if ("C".equals(streamShipBL)) {
            this.streamShipBL = "C-Collect";
        } else if ("T".equals(streamShipBL)) {
            this.streamShipBL = "T-Third Party";
        }
    }

    public String getHouseBL() {
        if ("P-Prepaid".equalsIgnoreCase(houseBL)) {
            return "P";
        } else if ("C-Collect".equalsIgnoreCase(houseBL)) {
            return "C";
        } else if ("B-Both".equalsIgnoreCase(houseBL)) {
            return "B";
        }
        return null;
    }

    public void setHouseBL(String houseBL) {
        if ("P".equals(houseBL)) {
            this.houseBL = "P-Prepaid";
        } else if ("C".equals(houseBL)) {
            this.houseBL = "C-Collect";
        } else if ("B".equals(houseBL)) {
            this.houseBL = "B-Both";
        }
    }

    public String getImportOrginBLNo() {
        return importOrginBLNo;
    }

    public void setImportOrginBLNo(String importOrginBLNo) {
        this.importOrginBLNo = importOrginBLNo;
    }

    public String getPlaceofReciept() {
        return placeofReciept;
    }

    public void setPlaceofReciept(String placeofReciept) {
        this.placeofReciept = placeofReciept;
    }

    public String getPointofCountryandOrigin() {
        return pointofCountryandOrigin;
    }

    public void setPointofCountryandOrigin(String pointofCountryandOrigin) {
        this.pointofCountryandOrigin = pointofCountryandOrigin;
    }

    public String getForwardingAgent() {
        return null != forwardingAgent ? forwardingAgent.toUpperCase() : "";
    }

    public void setForwardingAgent(String forwardingAgent) {
        this.forwardingAgent = null != forwardingAgent ? forwardingAgent.toUpperCase() : "";
    }

    public String getExportreference() {
        return null != exportreference ? exportreference.toUpperCase() : "";
    }

    public void setExportreference(String exportreference) {
        this.exportreference = null != exportreference ? exportreference.toUpperCase() : "";
    }

    public String getDomesticrouting() {
        return domesticrouting;
    }

    public void setDomesticrouting(String domesticrouting) {
        this.domesticrouting = domesticrouting;
    }

    public String getTranshipmentTo() {
        return transhipmentTo;
    }

    public void setTranshipmentTo(String transhipmentTo) {
        this.transhipmentTo = transhipmentTo;
    }

    public String getOnwardInlandRouting() {
        return null != onwardInlandRouting ? onwardInlandRouting.toUpperCase() : "";
    }

    public void setOnwardInlandRouting(String onwardInlandRouting) {
        this.onwardInlandRouting = null != onwardInlandRouting ? onwardInlandRouting.toUpperCase() : "";
    }

    public String getPortofLoading() {
        return portofLoading;
    }

    public void setPortofLoading(String portofLoading) {
        this.portofLoading = portofLoading;
    }

    public String getConsigneeNo() {
        return consigneeNo;
    }

    public void setConsigneeNo(String consigneeNo) {
        this.consigneeNo = consigneeNo;
    }

    public String getHouseConsignee() {
        return houseConsignee;
    }

    public void setHouseConsignee(String houseConsignee) {
        this.houseConsignee = houseConsignee;
    }

    public String getShipperAddress() {
        return null != shipperAddress ? shipperAddress.toUpperCase() : "";
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = null != shipperAddress ? shipperAddress.toUpperCase() : "";
    }

    public String getConsigneeAddress() {
        return null != consigneeAddress ? consigneeAddress.toUpperCase() : "";
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = null != consigneeAddress ? consigneeAddress.toUpperCase() : "";
    }

    public String getHouseShipperAddress() {
        return null != houseShipperAddress ? houseShipperAddress.toUpperCase() : "";
    }

    public void setHouseShipperAddress(String houseShipperAddress) {
        this.houseShipperAddress = null != houseShipperAddress ? houseShipperAddress.toUpperCase() : "";
    }

    public String getHouseConsigneeAddress() {
        return null != houseConsigneeAddress ? houseConsigneeAddress.toUpperCase() : "";
    }

    public void setHouseConsigneeAddress(String houseConsigneeAddress) {
        this.houseConsigneeAddress = null != houseConsigneeAddress ? houseConsigneeAddress.toUpperCase() : "";
    }

    public String getStreamShipLine() {
        return null != streamShipLine ? streamShipLine.toUpperCase() : "";
    }

    public void setStreamShipLine(String streamShipLine) {
        this.streamShipLine = null != streamShipLine ? streamShipLine.toUpperCase() : "";
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getBillTrdPrty() {
        return billTrdPrty;
    }

    public void setBillTrdPrty(String billTrdPrty) {
        this.billTrdPrty = billTrdPrty;
    }

    public Double getTotalCosts() {
        return totalCosts;
    }

    public void setTotalCosts(Double totalCosts) {
        this.totalCosts = totalCosts;
    }

    public String getForwarderNo() {
        return forwarderNo;
    }

    public void setForwarderNo(String forwarderNo) {
        this.forwarderNo = forwarderNo;
    }

    public String getRateByAgent() {
        return rateByAgent;
    }

    public void setRateByAgent(String rateByAgent) {
        this.rateByAgent = rateByAgent;
    }

    public String getNotifyParty() {
        return notifyParty;
    }

    public void setNotifyParty(String notifyParty) {
        this.notifyParty = notifyParty;
    }

    public String getHouseNotifyPartyNo() {
        return houseNotifyPartyNo;
    }

    public void setHouseNotifyPartyNo(String houseNotifyPartyNo) {
        this.houseNotifyPartyNo = houseNotifyPartyNo;
    }

    public String getBillThirdPartyAddress() {
        return billThirdPartyAddress;
    }

    public void setBillThirdPartyAddress(String billThirdPartyAddress) {
        this.billThirdPartyAddress = billThirdPartyAddress;
    }

    public Double getTotalCostCode() {
        return totalCostCode;
    }

    public void setTotalCostCode(Double totalCostCode) {
        this.totalCostCode = totalCostCode;
    }

    public String getMasterBl() {
        return masterBl;
    }

    public void setMasterBl(String masterBl) {
        this.masterBl = masterBl;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getReadyToPost() {
        return readyToPost;
    }

    public void setReadyToPost(String readyToPost) {
        this.readyToPost = readyToPost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBlTerms() {
        return blTerms;
    }

    public void setBlTerms(String blTerms) {
        this.blTerms = blTerms;
    }

    public String getShipmentType() {
        return shipmentType;
    }

    public void setShipmentType(String shipmentType) {
        this.shipmentType = shipmentType;
    }

    public String getAgentName() {
        return null != agentName ? agentName.toUpperCase() : "";
    }

    public void setAgentName(String agentName) {
        this.agentName = null != agentName ? agentName.toUpperCase() : "";
    }

    public String getHouseShipperName() {
        return null != houseShipperName ? houseShipperName.toUpperCase() : "";
    }

    public void setHouseShipperName(String houseShipperName) {
        this.houseShipperName = null != houseShipperName ? houseShipperName.toUpperCase() : "";
    }

    public String getShipperName() {
        return null != shipperName ? shipperName.toUpperCase() : "";
    }

    public void setShipperName(String shipperName) {
        this.shipperName = null != shipperName ? shipperName.toUpperCase() : "";
    }

    public String getConsigneeName() {
        return null != consigneeName ? consigneeName.toUpperCase() : "";
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = null != consigneeName ? consigneeName.toUpperCase() : "";
    }

    public String getHouseConsigneeName() {
        return null != houseConsigneeName ? houseConsigneeName.toUpperCase() : "";
    }

    public void setHouseConsigneeName(String houseConsigneeName) {
        this.houseConsigneeName = null != houseConsigneeName ? houseConsigneeName.toUpperCase() : "";
    }

    public String getNotifyPartyName() {
        return null != notifyPartyName ? notifyPartyName.toUpperCase() : "";
    }

    public void setNotifyPartyName(String notifyPartyName) {
        this.notifyPartyName = null != notifyPartyName ? notifyPartyName.toUpperCase() : "";
    }

    public String getHouseNotifyPartyName() {
        return null != houseNotifyPartyName ? houseNotifyPartyName.toUpperCase() : "";
    }

    public void setHouseNotifyPartyName(String houseNotifyPartyName) {
        this.houseNotifyPartyName = null != houseNotifyPartyName ? houseNotifyPartyName.toUpperCase() : "";
    }

    public String getForwardingAgentName() {
        return null != forwardingAgentName ? forwardingAgentName.toUpperCase() : "";
    }

    public void setForwardingAgentName(String forwardingAgentName) {
        this.forwardingAgentName = null != forwardingAgentName ? forwardingAgentName.toUpperCase() : "";
    }

    public String getOriginalTerminal() {
        return originalTerminal;
    }

    public void setOriginalTerminal(String originalTerminal) {
        this.originalTerminal = originalTerminal;
    }

    public String getBillingTerminal() {
        return billingTerminal;
    }

    public void setBillingTerminal(String billingTerminal) {
        this.billingTerminal = billingTerminal;
    }

    public String getThirdPartyName() {
        return null != thirdPartyName ? thirdPartyName.toUpperCase() : "";
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = null != thirdPartyName ? thirdPartyName.toUpperCase() : "";
    }

    public String getAgentAddress() {
        return null != agentAddress ? agentAddress.toUpperCase() : "";
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = null != agentAddress ? agentAddress.toUpperCase() : "";
    }

    public Date getSailDate() {
        return sailDate;
    }

    public void setSailDate(Date sailDate) {
        this.sailDate = sailDate;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getOriginalPrinting() {
        return originalPrinting;
    }

    public void setOriginalPrinting(String originalPrinting) {
        this.originalPrinting = originalPrinting;
    }

    public String getNonNegotiablePrinting() {
        return nonNegotiablePrinting;
    }

    public void setNonNegotiablePrinting(String nonNegotiablePrinting) {
        this.nonNegotiablePrinting = nonNegotiablePrinting;
    }

    public String getExpressBlPrinting() {
        return expressBlPrinting;
    }

    public void setExpressBlPrinting(String expressBlPrinting) {
        this.expressBlPrinting = expressBlPrinting;
    }

    public String getNoOfOriginals() {
        return noOfOriginals;
    }

    public void setNoOfOriginals(String noOfOriginals) {
        this.noOfOriginals = noOfOriginals;
    }

    public String getPreCarriage() {
        return preCarriage;
    }

    public void setPreCarriage(String preCarriage) {
        this.preCarriage = preCarriage;
    }

    public String getPrecarriagePOR() {
        return precarriagePOR;
    }

    public void setPrecarriagePOR(String precarriagePOR) {
        this.precarriagePOR = precarriagePOR;
    }

    public String getLoadingPier() {
        return loadingPier;
    }

    public void setLoadingPier(String loadingPier) {
        this.loadingPier = loadingPier;
    }

    public String getShipperSource() {
        return shipperSource;
    }

    public void setShipperSource(String shipperSource) {
        this.shipperSource = shipperSource;
    }

    public String getEditAgentNameCheck() {
        return editAgentNameCheck;
    }

    public void setEditAgentNameCheck(String editAgentNameCheck) {
        this.editAgentNameCheck = editAgentNameCheck;
    }

    public String getConsigneeSource() {
        return consigneeSource;
    }

    public void setConsigneeSource(String consigneeSource) {
        this.consigneeSource = consigneeSource;
    }

    public String getNotifyPartysource() {
        return notifyPartysource;
    }

    public void setNotifyPartysource(String notifyPartysource) {
        this.notifyPartysource = notifyPartysource;
    }

    public String getMasterBlComments() {
        return masterBlComments;
    }

    public void setMasterBlComments(String masterBlComments) {
        this.masterBlComments = masterBlComments;
    }

    public String getMasterBlNotes() {
        return masterBlNotes;
    }

    public void setMasterBlNotes(String masterBlNotes) {
        this.masterBlNotes = masterBlNotes;
    }

    public String getValueOfGoods() {
        return valueOfGoods;
    }

    public void setValueOfGoods(String valueOfGoods) {
        this.valueOfGoods = valueOfGoods;
    }

    public String getBlBy() {
        return blBy;
    }

    public void setBlBy(String blBy) {
        this.blBy = blBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getBkgNo() {
        return bkgNo;
    }

    public void setBkgNo(Integer bkgNo) {
        this.bkgNo = bkgNo;
    }

    public String getImportsFreightRelease() {
        if (CommonUtils.isEmpty(importsFreightRelease)) {
            return "No";
        }
        return importsFreightRelease;
    }

    public void setImportsFreightRelease(String importsFreightRelease) {
        this.importsFreightRelease = importsFreightRelease;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getInttra() {
        return inttra;
    }

    public void setInttra(String inttra) {
        this.inttra = inttra;
    }

    public String getReadyToEdi() {
        return readyToEdi;
    }

    public void setReadyToEdi(String readyToEdi) {
        this.readyToEdi = readyToEdi;
    }

    public String getBlAudited() {
        return blAudited;
    }

    public void setBlAudited(String blAudited) {
        this.blAudited = blAudited;
    }

    public String getReceivedMaster() {
        return receivedMaster;
    }

    public void setReceivedMaster(String receivedMaster) {
        this.receivedMaster = receivedMaster;
    }

    public String getBlClosed() {
        return blClosed;
    }

    public void setBlClosed(String blClosed) {
        this.blClosed = blClosed;
    }

    public String getAes() {
        return aes;
    }

    public void setAes(String aes) {
        this.aes = aes;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getSslineName() {
        return null != sslineName ? sslineName.toUpperCase() : "";
    }

    public void setSslineName(String sslineName) {
        this.sslineName = null != sslineName ? sslineName.toUpperCase() : "";
    }

    public String getSslineNo() {
        return sslineNo;
    }

    public void setSslineNo(String sslineNo) {
        this.sslineNo = sslineNo;
    }

    public String getOriginalBlRequired() {
        return originalBlRequired;
    }

    public void setOriginalBlRequired(String originalBlRequired) {
        this.originalBlRequired = originalBlRequired;
    }

    public String getFclblClause() {
        return fclblClause;
    }

    public void setFclblClause(String fclblClause) {
        this.fclblClause = fclblClause;
    }

    public String getFclblClauseDescription() {
        return fclblClauseDescription;
    }

    public void setFclblClauseDescription(String fclblClauseDescription) {
        this.fclblClauseDescription = fclblClauseDescription;
    }

    public String getPrintContainersOnBl() {
        return null != printContainersOnBl ? printContainersOnBl : "Yes";
    }

    public void setPrintContainersOnBl(String printContainersOnBl) {
        this.printContainersOnBl = printContainersOnBl;
    }

    public String getShipperLoadsAndCounts() {
        return null != shipperLoadsAndCounts ? shipperLoadsAndCounts : "Yes";
    }

    public void setShipperLoadsAndCounts(String shipperLoadsAndCounts) {
        this.shipperLoadsAndCounts = shipperLoadsAndCounts;
    }

    public String getPrintPhrase() {
        return printPhrase;
    }

    public void setPrintPhrase(String printPhrase) {
        this.printPhrase = printPhrase;
    }

    public String getAgentsForCarrier() {
        return null != agentsForCarrier ? agentsForCarrier : "No";
    }

    public void setAgentsForCarrier(String agentsForCarrier) {
        this.agentsForCarrier = agentsForCarrier;
    }

    public String getAlternatePol() {
        return alternatePol;
    }

    public void setAlternatePol(String alternatePol) {
        this.alternatePol = alternatePol;
    }

    public String getManifestPrintReport() {
        return manifestPrintReport;
    }

    public void setManifestPrintReport(String manifestPrintReport) {
        this.manifestPrintReport = manifestPrintReport;
    }

    public String getDefaultAgent() {
        return defaultAgent;
    }

    public void setDefaultAgent(String defaultAgent) {
        this.defaultAgent = defaultAgent;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(String billToParty) {
        this.billToParty = billToParty;
    }

    public String getSsbldestinationChargesPreCol() {
        return ssbldestinationChargesPreCol;
    }

    public void setSsbldestinationChargesPreCol(String ssbldestinationChargesPreCol) {
        this.ssbldestinationChargesPreCol = ssbldestinationChargesPreCol;
    }

    public String getDestRemarks() {
        return destRemarks;
    }

    public void setDestRemarks(String destRemarks) {
        this.destRemarks = destRemarks;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRampCity() {
        return rampCity;
    }

    public void setRampCity(String rampCity) {
        this.rampCity = rampCity;
    }

    public String getNoDays() {
        return noDays;
    }

    public void setNoDays(String noDays) {
        this.noDays = noDays;
    }

    public String getRoutedByAgent() {
        return routedByAgent;
    }

    public void setRoutedByAgent(String routedByAgent) {
        this.routedByAgent = routedByAgent;
    }

    public String getRoutedbyAgentsCountry() {
        return routedbyAgentsCountry;
    }

    public void setRoutedbyAgentsCountry(String routedbyAgentsCountry) {
        this.routedbyAgentsCountry = null != routedbyAgentsCountry ? routedbyAgentsCountry.toUpperCase() : "";
    }

    public String getVoyageInternal() {
        return voyageInternal;
    }

    public void setVoyageInternal(String voyageInternal) {
        this.voyageInternal = voyageInternal;
    }

    public String getRatesRemarks() {
        return ratesRemarks;
    }

    public void setRatesRemarks(String ratesRemarks) {
        this.ratesRemarks = ratesRemarks;
    }

    public String getDoorOfOrigin() {
        return doorOfOrigin;
    }

    public void setDoorOfOrigin(String doorOfOrigin) {
        this.doorOfOrigin = doorOfOrigin;
    }

    public String getDoorOfDestination() {
        return doorOfDestination;
    }

    public void setDoorOfDestination(String doorOfDestination) {
        this.doorOfDestination = doorOfDestination;
    }

    public String getLineMove() {
        return lineMove;
    }

    public void setLineMove(String lineMove) {
        this.lineMove = lineMove;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityDesc() {
        return commodityDesc;
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc;
    }

    public String getRoutedAgentcheck() {
        return routedAgentcheck;
    }

    public void setRoutedAgentcheck(String routedAgentcheck) {
        this.routedAgentcheck = routedAgentcheck;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getDocCutOff() {
        return docCutOff;
    }

    public void setDocCutOff(Date docCutOff) {
        this.docCutOff = docCutOff;
    }

    public Date getPortCutOff() {
        return portCutOff;
    }

    public void setPortCutOff(Date portCutOff) {
        this.portCutOff = portCutOff;
    }

    public Date getEarlierPickUpDate() {
        return earlierPickUpDate;
    }

    public void setEarlierPickUpDate(Date earlierPickUpDate) {
        this.earlierPickUpDate = earlierPickUpDate;
    }

    public String getNoOfPackages() {
        return null != noOfPackages ? noOfPackages : "Yes";
    }

    public void setNoOfPackages(String noOfPackages) {
        this.noOfPackages = noOfPackages;
    }

    public String getAutoDeductFfcomm() {
        return autoDeductFfcomm;
    }

    public void setAutoDeductFfcomm(String autoDeductFfcomm) {
        this.autoDeductFfcomm = autoDeductFfcomm;
    }

    public String getManifestedBy() {
        return manifestedBy;
    }

    public void setManifestedBy(String manifestedBy) {
        this.manifestedBy = manifestedBy;
    }

    public Date getCommissionsAddeddate() {
        return commissionsAddeddate;
    }

    public void setCommissionsAddeddate(Date commissionsAddeddate) {
        this.commissionsAddeddate = commissionsAddeddate;
    }

    public String getAccrualConvertedby() {
        return accrualConvertedby;
    }

    public void setAccrualConvertedby(String accrualConvertedby) {
        this.accrualConvertedby = accrualConvertedby;
    }

    public Date getAccrualConverteddate() {
        return accrualConverteddate;
    }

    public void setAccrualConverteddate(Date accrualConverteddate) {
        this.accrualConverteddate = accrualConverteddate;
    }

    public String getBillToCode() {
        return billToCode;
    }

    public void setBillToCode(String billToCode) {
        this.billToCode = billToCode;
    }

    public Date getManifestedDate() {
        return manifestedDate;
    }

    public void setManifestedDate(Date manifestedDate) {
        this.manifestedDate = manifestedDate;
    }

    public String getReceivedMasterby() {
        return receivedMasterby;
    }

    public void setReceivedMasterby(String receivedMasterby) {
        this.receivedMasterby = receivedMasterby;
    }

    public Date getReceivedMasterdate() {
        return receivedMasterdate;
    }

    public void setReceivedMasterdate(Date receivedMasterdate) {
        this.receivedMasterdate = receivedMasterdate;
    }

    public String getAuditedBy() {
        return auditedBy;
    }

    public void setAuditedBy(String auditedBy) {
        this.auditedBy = auditedBy;
    }

    public Date getAuditedDate() {
        return auditedDate;
    }

    public void setAuditedDate(Date auditedDate) {
        this.auditedDate = auditedDate;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public String getCommissionsAddedby() {
        return commissionsAddedby;
    }

    public void setCommissionsAddedby(String commissionsAddedby) {
        this.commissionsAddedby = commissionsAddedby;
    }

    public String getConfirmOnBoard() {
        return CommonUtils.isNotEmpty(confirmOnBoard) ? confirmOnBoard : "N";
    }

    public void setConfirmOnBoard(String confirmOnBoard) {
        this.confirmOnBoard = confirmOnBoard;
    }

    public Date getVerfiyETA() {
        return verfiyETA;
    }

    public void setVerfiyETA(Date verfiyETA) {
        this.verfiyETA = verfiyETA;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    public Date getConfirmOn() {
        return confirmOn;
    }

    public void setConfirmOn(Date confirmOn) {
        this.confirmOn = confirmOn;
    }

    public String getVoid1() {
        return void1;
    }

    public void setVoid1(String void1) {
        this.void1 = void1;
    }

    public String getVoidBy() {
        return voidBy;
    }

    public void setVoidBy(String voidBy) {
        this.voidBy = voidBy;
    }

    public Date getVoidDate() {
        return voidDate;
    }

    public void setVoidDate(Date voidDate) {
        this.voidDate = voidDate;
    }

    public String getConfOnboardComments() {
        return confOnboardComments;
    }

    public void setConfOnboardComments(String confOnboardComments) {
        this.confOnboardComments = confOnboardComments;
    }

    public String getDestinationChargesPreCol() {
        if ("P-Prepaid".equalsIgnoreCase(destinationChargesPreCol)) {
            return "P";
        } else {
            return "C";
        }
    }

    public void setDestinationChargesPreCol(String destinationChargesPreCol) {
        if ("P".equals(destinationChargesPreCol)) {
            this.destinationChargesPreCol = "P-Prepaid";
        } else if ("C".equals(destinationChargesPreCol)) {
            this.destinationChargesPreCol = "C-Collect";
        }
    }

    public String getTotalContainers() {
        return null != totalContainers ? totalContainers : "Yes";
    }

    public void setTotalContainers(String totalContainers) {
        this.totalContainers = totalContainers;
    }

    public String getCountryOfOrigin() {
        return null != countryOfOrigin ? countryOfOrigin : "Yes";
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getNewmasterbL() {
        return newmasterbL;
    }

    public void setNewmasterbL(String newmasterbL) {
        this.newmasterbL = newmasterbL;
    }

    public String getProof() {
        return null != proof ? proof : "Yes";
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getPreAlert() {
        return null != preAlert ? preAlert : "Yes";
    }

    public void setPreAlert(String preAlert) {
        this.preAlert = preAlert;
    }

    public String getNonNegotiable() {
        return nonNegotiable;
    }

    public void setNonNegotiable(String nonNegotiable) {
        this.nonNegotiable = nonNegotiable;
    }

    public String getImportAMSHouseBl() {
        return null != importAMSHouseBl ? importAMSHouseBl.toUpperCase() : "";
    }

    public void setImportAMSHouseBl(String importAMSHouseBl) {
        this.importAMSHouseBl = null != importAMSHouseBl ? importAMSHouseBl.toUpperCase() : "";
    }

    public String getImportFlag() {
        return importFlag;
    }

    public void setImportFlag(String importFlag) {
        this.importFlag = importFlag;
    }

    public String getImportRelease() {
        return null != importRelease ? importRelease.toUpperCase() : "";
    }

    public void setImportRelease(String importRelease) {
        this.importRelease = null != importRelease ? importRelease.toUpperCase() : "";
    }

    public Date getImportVerifiedEta() {
        return importVerifiedEta;
    }

    public void setImportVerifiedEta(Date importVerifiedEta) {
        this.importVerifiedEta = importVerifiedEta;
    }

    public String getImportReleaseComments() {
        return null != importReleaseComments ? importReleaseComments.toUpperCase() : "";
    }

    public void setImportReleaseComments(String importReleaseComments) {
        this.importReleaseComments = null != importReleaseComments ? importReleaseComments.toUpperCase() : "";
    }

    public String getSendCopyTo() {
        return sendCopyTo;
    }

    public void setSendCopyTo(String sendCopyTo) {
        this.sendCopyTo = sendCopyTo;
    }

    public String getMasterConsigneeCheck() {
        return masterConsigneeCheck;
    }

    public void setMasterConsigneeCheck(String masterConsigneeCheck) {
        this.masterConsigneeCheck = masterConsigneeCheck;
    }

    public String getMasterNotifyCheck() {
        return masterNotifyCheck;
    }

    public void setMasterNotifyCheck(String masterNotifyCheck) {
        this.masterNotifyCheck = masterNotifyCheck;
    }

    public String getConsigneeCheck() {
        return consigneeCheck;
    }

    public void setConsigneeCheck(String consigneeCheck) {
        this.consigneeCheck = consigneeCheck;
    }

    public String getNotifyCheck() {
        return notifyCheck;
    }

    public void setNotifyCheck(String notifyCheck) {
        this.notifyCheck = notifyCheck;
    }

    public Date getDateInYard() {
        return dateInYard;
    }

    public void setDateInYard(Date dateInYard) {
        this.dateInYard = dateInYard;
    }

    public Date getDateOutYard() {
        return dateOutYard;
    }

    public void setDateOutYard(Date dateOutYard) {
        this.dateOutYard = dateOutYard;
    }

    public String getManifestRev() {
        return manifestRev;
    }

    public void setManifestRev(String manifestRev) {
        this.manifestRev = CommonUtils.isNotEmpty(manifestRev) ? manifestRev : "0";
    }

    public String getPrintRev() {
        return null != printRev ? printRev : "Yes";
    }

    public void setPrintRev(String printRev) {
        this.printRev = printRev;
    }

    public String getInbondNumber() {
        return inbondNumber;
    }

    public void setInbondNumber(String inbondNumber) {
        this.inbondNumber = inbondNumber;
    }

    public Date getInbondDate() {
        return inbondDate;
    }

    public void setInbondDate(Date inbondDate) {
        this.inbondDate = inbondDate;
    }

    public String getInbondPort() {
        return inbondPort;
    }

    public void setInbondPort(String inbondPort) {
        this.inbondPort = inbondPort;
    }

    public String getDoorOriginAsPlor() {
        return null != doorOriginAsPlor ? doorOriginAsPlor : "Yes";
    }

    public void setDoorOriginAsPlor(String doorOriginAsPlor) {
        this.doorOriginAsPlor = doorOriginAsPlor;
    }

    public String getDoorDestinationAsFinalDeliveryHouse() {
        return null != doorDestinationAsFinalDeliveryHouse ? doorDestinationAsFinalDeliveryHouse : "No";
    }

    public void setDoorDestinationAsFinalDeliveryHouse(String doorDestinationAsFinalDeliveryHouse) {
        this.doorDestinationAsFinalDeliveryHouse = doorDestinationAsFinalDeliveryHouse;
    }

    public String getCollectThirdParty() {
        return null != collectThirdParty ? collectThirdParty : "No";
    }

    public void setCollectThirdParty(String collectThirdParty) {
        this.collectThirdParty = collectThirdParty;
    }

    public String getDoorDestinationAsFinalDeliveryMaster() {
        return null != doorDestinationAsFinalDeliveryMaster ? doorDestinationAsFinalDeliveryMaster : "No";
    }

    public void setDoorDestinationAsFinalDeliveryMaster(String doorDestinationAsFinalDeliveryMaster) {
        this.doorDestinationAsFinalDeliveryMaster = doorDestinationAsFinalDeliveryMaster;
    }

    public String getHouseShipperCheck() {
        return houseShipperCheck;
    }

    public void setHouseShipperCheck(String houseShipperCheck) {
        this.houseShipperCheck = houseShipperCheck;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getPaymentRelease() {
        return paymentRelease;
    }

    public void setPaymentRelease(String paymentRelease) {
        this.paymentRelease = paymentRelease;
    }

    public Date getPaymentReleasedOn() {
        return paymentReleasedOn;
    }

    public void setPaymentReleasedOn(Date paymentReleasedOn) {
        this.paymentReleasedOn = paymentReleasedOn;
    }

    public String getPaymentReleaseComment() {
        return paymentReleaseComment;
    }

    public void setPaymentReleaseComment(String paymentReleaseComment) {
        this.paymentReleaseComment = paymentReleaseComment;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getEdiCreatedBy() {
        return ediCreatedBy;
    }

    public void setEdiCreatedBy(String ediCreatedBy) {
        this.ediCreatedBy = ediCreatedBy;
    }

    public Date getEdiCreatedOn() {
        return ediCreatedOn;
    }

    public void setEdiCreatedOn(Date ediCreatedOn) {
        this.ediCreatedOn = ediCreatedOn;
    }

    public String getAlternatePort() {
        return alternatePort;
    }

    public void setAlternatePort(String alternatePort) {
        this.alternatePort = alternatePort;
    }

    public String getEdiConsigneeCheck() {
        return ediConsigneeCheck;
    }

    public void setEdiConsigneeCheck(String ediConsigneeCheck) {
        this.ediConsigneeCheck = ediConsigneeCheck;
    }

    public String getEdiShipperCheck() {
        return ediShipperCheck;
    }

    public void setEdiShipperCheck(String ediShipperCheck) {
        this.ediShipperCheck = ediShipperCheck;
    }

    public String getEdiNotifypartyCheck() {
        return ediNotifypartyCheck;
    }

    public void setEdiNotifypartyCheck(String ediNotifypartyCheck) {
        this.ediNotifypartyCheck = ediNotifypartyCheck;
    }

    public String getPrintAlternatePort() {
        return null != printAlternatePort ? printAlternatePort : "No";
    }

    public void setPrintAlternatePort(String printAlternatePort) {
        this.printAlternatePort = printAlternatePort;
    }

    public String getTrimTrailingZerosForQty() {
        return null != trimTrailingZerosForQty ? trimTrailingZerosForQty : "Yes";
    }

    public void setTrimTrailingZerosForQty(String trimTrailingZerosForQty) {
        this.trimTrailingZerosForQty = trimTrailingZerosForQty;
    }

    public String getBreakBulk() {
        return breakBulk;
    }

    public void setBreakBulk(String breakBulk) {
        this.breakBulk = breakBulk;
    }

    public String getHazmat() {
        return hazmat;
    }

    public void setHazmat(String hazmat) {
        this.hazmat = hazmat;
    }

    public String getRatesNonRates() {
        return ratesNonRates;
    }

    public void setRatesNonRates(String ratesNonRates) {
        this.ratesNonRates = ratesNonRates;
    }

    public String getCorrectedAfterManifest() {
        return correctedAfterManifest;
    }

    public void setCorrectedAfterManifest(String correctedAfterManifest) {
        this.correctedAfterManifest = correctedAfterManifest;
    }

    public Double getCostOfGoods() {
        return costOfGoods;
    }

    public void setCostOfGoods(Double costOfGoods) {
        this.costOfGoods = costOfGoods;
    }

    public String getEditHouseShipperCheck() {
        return editHouseShipperCheck;
    }

    public void setEditHouseShipperCheck(String editHouseShipperCheck) {
        this.editHouseShipperCheck = editHouseShipperCheck;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public Double getInsuranceRate() {
        return insuranceRate;
    }

    public void setInsuranceRate(Double insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public String getDirectconsignCheck() {
        return directconsignCheck;
    }

    public void setDirectconsignCheck(String directconsignCheck) {
        this.directconsignCheck = directconsignCheck;
    }

    public Boolean getOverPaidStatus() {
        return overPaidStatus;
    }

    public void setOverPaidStatus(Boolean overPaidStatus) {
        this.overPaidStatus = overPaidStatus;
    }

    public Boolean getConvertedToAp() {
        return convertedToAp;
    }

    public void setConvertedToAp(Boolean convertedToAp) {
        this.convertedToAp = convertedToAp;
    }

    public String getImportWarehouseName() {
        return importWarehouseName;
    }

    public void setImportWarehouseName(String importWarehouseName) {
        this.importWarehouseName = importWarehouseName;
    }

    public String getImportWarehouseCode() {
        return importWarehouseCode;
    }

    public void setImportWarehouseCode(String importWarehouseCode) {
        this.importWarehouseCode = importWarehouseCode;
    }

    public String getImportWarehouseAddress() {
        return importWarehouseAddress;
    }

    public void setImportWarehouseAddress(String importWarehouseAddress) {
        this.importWarehouseAddress = importWarehouseAddress;
    }

    public String getImportPickupRemarks() {
        return importPickupRemarks;
    }

    public void setImportPickupRemarks(String importPickupRemarks) {
        this.importPickupRemarks = importPickupRemarks;
    }

    public String getEditHouseNotifyCheck() {
        return editHouseNotifyCheck;
    }

    public void setEditHouseNotifyCheck(String editHouseNotifyCheck) {
        this.editHouseNotifyCheck = editHouseNotifyCheck;
    }

    public String getEditHouseConsigneeCheck() {
        return editHouseConsigneeCheck;
    }

    public void setEditHouseConsigneeCheck(String editHouseConsigneeCheck) {
        this.editHouseConsigneeCheck = editHouseConsigneeCheck;
    }

    public List<FclAesDetailsNew> getFclAesDetailsList() {
        return fclAesDetailsList;
    }

    public void setFclAesDetailsList(List<FclAesDetailsNew> fclAesDetailsList) {
        this.fclAesDetailsList = fclAesDetailsList;
    }

    public List<Fclblcorrections> getFclblcorrectionsList() {
        return fclblcorrectionsList;
    }

    public void setFclblcorrectionsList(List<Fclblcorrections> fclblcorrectionsList) {
        this.fclblcorrectionsList = fclblcorrectionsList;
    }

    public List<AesHistoryNew> getAesHistoryList() {
        return aesHistoryList;
    }

    public void setAesHistoryList(List<AesHistoryNew> aesHistoryList) {
        this.aesHistoryList = aesHistoryList;
    }

    public List<FclInbonddetails> getFclInbondDetailsList() {
        return fclInbondDetailsList;
    }

    public void setFclInbondDetailsList(List<FclInbonddetails> fclInbondDetailsList) {
        this.fclInbondDetailsList = fclInbondDetailsList;
    }

    public List<FclBlChargesNew> getFclBlChargesList() {
        return fclBlChargesList;
    }

    public void setFclBlChargesList(List<FclBlChargesNew> fclBlChargesList) {
        this.fclBlChargesList = fclBlChargesList;
    }

    public List<FclBlCostcodes> getFclBlCostcodesList() {
        return fclBlCostcodesList;
    }

    public void setFclBlCostcodesList(List<FclBlCostcodes> fclBlCostcodesList) {
        this.fclBlCostcodesList = fclBlCostcodesList;
    }

    public List<FclBlContainerDtls> getFclBlContainerDtlsList() {
        return fclBlContainerDtlsList;
    }

    public void setFclBlContainerDtlsList(List<FclBlContainerDtls> fclBlContainerDtlsList) {
        this.fclBlContainerDtlsList = fclBlContainerDtlsList;
    }

    public List<Sedfilings> getSedFilingsList() {
        return sedFilingsList;
    }

    public void setSedFilingsList(List<Sedfilings> sedFilingsList) {
        this.sedFilingsList = sedFilingsList;
    }

    public String getOmitTermAndPort() {
        return null != omitTermAndPort ? omitTermAndPort : "No";
    }

    public void setOmitTermAndPort(String omitTermAndPort) {
        this.omitTermAndPort = omitTermAndPort;
    }

    public String getServiceContractNo() {
        return null != serviceContractNo ? serviceContractNo : "Yes";
    }

    public void setServiceContractNo(String serviceContractNo) {
        this.serviceContractNo = serviceContractNo;
    }

    public String getHblFDOverride() {
        return null != hblFDOverride ? hblFDOverride : "No";
    }

    public void setHblFDOverride(String hblFDOverride) {
        this.hblFDOverride = hblFDOverride;
    }

    public String getHblPODOverride() {
        return null != hblPODOverride ? hblPODOverride : "No";
    }

    public void setHblPODOverride(String hblPODOverride) {
        this.hblPODOverride = hblPODOverride;
    }

    public String getHblPOLOverride() {
        return null != hblPOLOverride ? hblPOLOverride : "No";
    }

    public void setHblPOLOverride(String hblPOLOverride) {
        this.hblPOLOverride = hblPOLOverride;
    }

    public String getHblFD() {
        return hblFD;
    }

    public void setHblFD(String hblFD) {
        this.hblFD = hblFD;
    }

    public String getHblPOD() {
        return hblPOD;
    }

    public void setHblPOD(String hblPOD) {
        this.hblPOD = hblPOD;
    }

    public String getHblPOL() {
        return hblPOL;
    }

    public void setHblPOL(String hblPOL) {
        this.hblPOL = hblPOL;
    }

    public String getInternalRemark() {
        return internalRemark;
    }

    public void setInternalRemark(String internalRemark) {
        this.internalRemark = internalRemark;
    }

    public String getLocalDrayage() {
        return localDrayage;
    }

    public void setLocalDrayage(String localDrayage) {
        this.localDrayage = localDrayage;
    }

    public String getCertifiedTrueCopy() {
        return null != certifiedTrueCopy ? certifiedTrueCopy : "No";
    }

    public void setCertifiedTrueCopy(String certifiedTrueCopy) {
        this.certifiedTrueCopy = certifiedTrueCopy;
    }

    public String getRatedManifest() {
        return null != ratedManifest ? ratedManifest : "No";
    }

    public void setRatedManifest(String ratedManifest) {
        this.ratedManifest = ratedManifest;
    }

    public String getOmit2LetterCountryCode() {
        return null != omit2LetterCountryCode ? omit2LetterCountryCode : "No";
    }

    public void setOmit2LetterCountryCode(String omit2LetterCountryCode) {
        this.omit2LetterCountryCode = omit2LetterCountryCode;
    }

    public String getDoorOriginAsPlorHouse() {
        return null != doorOriginAsPlorHouse ? doorOriginAsPlorHouse : "Yes";
    }

    public void setDoorOriginAsPlorHouse(String doorOriginAsPlorHouse) {
        this.doorOriginAsPlorHouse = null != doorOriginAsPlorHouse ? doorOriginAsPlorHouse : "Yes";
    }

    public String getDockReceipt() {
        return null != dockReceipt ? dockReceipt : "No";
    }

    public void setDockReceipt(String dockReceipt) {
        this.dockReceipt = null != dockReceipt ? dockReceipt : "No";
    }

    public String getAlternateNoOfPackages() {
        return alternateNoOfPackages;
    }

    public void setAlternateNoOfPackages(String alternateNoOfPackages) {
        this.alternateNoOfPackages = alternateNoOfPackages;
    }

    public String getBookingContact() {
        return bookingContact;
    }

    public void setBookingContact(String bookingContact) {
        this.bookingContact = bookingContact;
    }

    public String getHblPlaceReceiptOverride() {
        return hblPlaceReceiptOverride;
    }

    public void setHblPlaceReceiptOverride(String hblPlaceReceiptOverride) {
        this.hblPlaceReceiptOverride = hblPlaceReceiptOverride;
    }

    public String getHblPlaceReceipt() {
        return hblPlaceReceipt;
    }

    public void setHblPlaceReceipt(String hblPlaceReceipt) {
        this.hblPlaceReceipt = hblPlaceReceipt;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(String spotRate) {
        this.spotRate = null != spotRate ? spotRate : "N";
    }

    public String getResendCostToBlue() {
        return resendCostToBlue;
    }
    
    public void setResendCostToBlue(String resendCostToBlue) {
        this.resendCostToBlue = resendCostToBlue;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getExpressRelease() {
        return expressRelease;
    }

    public void setExpressRelease(String expressRelease) {
        this.expressRelease = expressRelease;
    }

    public String getExpressReleaseComment() {
        return expressReleaseComment;
    }

    public void setExpressReleaseComment(String expressReleaseComment) {
        this.expressReleaseComment = expressReleaseComment;
    }

    public Date getExpressReleasedOn() {
        return expressReleasedOn;
    }

    public void setExpressReleasedOn(Date expressReleasedOn) {
        this.expressReleasedOn = expressReleasedOn;
    }

    public String getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(String deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

   

    public String getCustomsClearance() {
        return customsClearance;
    }

    public void setCustomsClearance(String customsClearance) {
        this.customsClearance = customsClearance;
    }

    public Date getCustomsClearanceOn() {
        return customsClearanceOn;
    }

    public void setCustomsClearanceOn(Date customsClearanceOn) {
        this.customsClearanceOn = customsClearanceOn;
    }

    public Date getDeliveryOrderOn() {
        return deliveryOrderOn;
    }

    public void setDeliveryOrderOn(Date deliveryOrderOn) {
        this.deliveryOrderOn = deliveryOrderOn;
    }

    public String getCustomsClearanceComment() {
        return customsClearanceComment;
    }

    public void setCustomsClearanceComment(String customsClearanceComment) {
        this.customsClearanceComment = customsClearanceComment;
    }

    public String getDeliveryOrderComment() {
        return deliveryOrderComment;
    }

    public void setDeliveryOrderComment(String deliveryOrderComment) {
        this.deliveryOrderComment = deliveryOrderComment;
    }

  
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bol != null ? bol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FclBlNew)) {
            return false;
        }
        FclBlNew other = (FclBlNew) object;
        if ((this.bol == null && other.bol != null) || (this.bol != null && !this.bol.equals(other.bol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.hibernate.FclBl[bol=" + bol + "]";
    }
}
