///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.gp.cong.logisoft.lcl.xml;
//
///**
// *
// * @author Congruence
// */
//import com.gp.cong.common.CommonUtils;
//
//import com.gp.cong.common.DateUtils;
//import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
//import com.gp.cong.logisoft.domain.CarriersOrLine;
//import com.gp.cong.logisoft.domain.CustomerAddress;
//import com.gp.cong.logisoft.domain.GenericCode;
//import com.gp.cong.logisoft.domain.User;
//import com.gp.cong.logisoft.domain.lcl.LclBooking;
//import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
//import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
//import com.gp.cong.logisoft.domain.lcl.LclBookingPlan;
//import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
//import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
//import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
//import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
//import com.gp.cong.logisoft.domain.lcl.LclUnit;
//import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
//import com.gp.cong.logisoft.edi.gtnexus.HelperClass;
//import com.gp.cong.logisoft.edi.util.LogFileWriter;
//import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
//import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
//import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
//import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPlanDAO;
//import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
//import com.gp.cong.logisoft.lcl.model.LclBookingPlanBean;
//import com.gp.cong.struts.LoadLogisoftProperties;
//import com.gp.cvst.logisoft.domain.CustAddress;
//import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.servlet.http.HttpServletRequest;
//import javax.xml.stream.XMLOutputFactory;
//import javax.xml.stream.XMLStreamWriter;
//
///**
// *
// * @author Congruence
// */
//public class LclGTNExusXMLCreator {
//
//    private static final String ENCODING = "ISO-8859-1";
//    private OutputStream outputStream = null;
//    private XMLStreamWriter writer = null;
//    private File file = null;
//    private StringBuilder fileName = new StringBuilder();
//    private String fullDate = null;
//    private String dateTime = null;
//    private String dateTime1 = null;
//    private String dateTimeSeconds = null;
//    private String bookingNumber = "";
//    private String documentIdentifier = null;
//    private String referenceNumber = null;
//    private String companyName = LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName").toUpperCase();
//    private String companyCode = LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase();
//    private String partnerName = LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName").toUpperCase();
//    private int docVersionCount = 0;
//    private StringBuilder errors = new StringBuilder();
//    private StringBuilder warnings = new StringBuilder();
//    HelperClass helperClass = null;
//    //have to check for actual object
//    private LclSsDetail lclSsDetail = new LclSsDetail();
//    private LclSsHeader lclSsHeader = new LclSsHeader();
//    private LclSSMasterBl lclSSMasterBl = new LclSSMasterBl();
//    private LclUnitSs lclUnitSs = new LclUnitSs();
//    private LclUnit lclUnit = new LclUnit();
//    private CarriersOrLine carriersOrLine = new CarriersOrLine();//CarrierScac=lclssdetail(spref)--tradingpratner(sslinenumber)--carrierOrLine(scac)
//    private CustAddress custAddress = new CustAddress();
//    private GenericCode genericCode = new GenericCode();
//    private LclSsDetailDAO lclDetailDAO = new LclSsDetailDAO();
//
//    private void init(List<LclBooking> lclBookingList) throws Exception {
//        //Date Time values
//        Date date = new Date();
//        fullDate = DateUtils.formatDate(date, "yyyyMMdd");
//        dateTime = DateUtils.formatDate(date, "yyMMddHHmm");
//        dateTime1 = DateUtils.formatDate(date, "yyyy-MM-dd hh:mm:ss");
//        dateTimeSeconds = DateUtils.formatDate(date, "yyyyMMddHHmmss");
//        String folderName = LoadLogisoftProperties.getProperty("lcl.gtnexus.xmlLocation");
//        File folder = new File(folderName);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        fileName.append(lclBookingList.get(0).getLclFileNumber().getFileNumber()).append("_").append(companyName).append("_GTN_").append(dateTimeSeconds).append(".xml");
//        docVersionCount = new EdiDAO().getDocVersion(fileName.toString());
//
//        file = new File(folder, fileName.toString());
//        outputStream = new FileOutputStream(file);
//        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
//        writer = outputFactory.createXMLStreamWriter(outputStream, ENCODING);
//    }
//
//    public void write(List<LclBooking> lclBookingList, LclUnit lclUnit, HttpServletRequest request) throws Exception {
//        EdiDAO ediDAO = new EdiDAO();
//        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
//        HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
//        FclInbondDetailsDAO inbondDetailsDAO = new FclInbondDetailsDAO();
//        User user = (User) request.getSession().getAttribute("loginuser");
//
//        //Start of Document
//        writer.writeStartDocument(ENCODING, "1.0");
//
//        //Start of Message
//        writer.writeStartElement("BookingMessage");
//
//        //Start of Header
//        writer.writeStartElement("TransactionInfo");
//        writer.writeAttribute("AcknowledmentRequested", "true");
//
//        //Start of MessageType
//        writer.writeStartElement("MessageSender");
//        writer.writeCharacters("ECI");
//        writer.writeEndElement();
//        //End of MessageType
//
//        //Start of DocumentIdentifier
//        writer.writeStartElement("MessageRecipient");
//        writer.writeCharacters("GTNEXUS");
//        writer.writeEndElement();
//        //End of DocumentIdentifier
//
//        //Start of DocumentIdentifier
//        writer.writeStartElement("MessageID");
//        writer.writeCharacters(lclBookingList.get(0).getLclFileNumber().getFileNumber());
//        writer.writeEndElement();
//        //End of DocumentIdentifier
//
//        //Start of DateTime - Document
//        writer.writeStartElement("DateTime");
//        writer.writeCharacters(dateTime1);
//        writer.writeEndElement();
//        //End of DateTime - Document
//
//        //Start of fileName - Document
//        writer.writeStartElement("FileName");
//        writer.writeCharacters(fileName.toString());
//        writer.writeEndElement();
//        //End of fileName - Document
//
//        writer.writeEndElement();
//        //End of Header
//
//        //Start of shipmentdetails with hazardous ########### second parent
//        writer.writeStartElement("Booking");
//        String hazShipment = "false";
//        List<LclBookingHazmat> lclBookingHazList = lclBookingList.get(0).getLclFileNumber().getLclBookingHazmatList();
//        if (CommonUtils.isNotEmpty(lclBookingHazList)) {
//            hazShipment = "true";
//        }
//        writer.writeAttribute("HazardousShipment", hazShipment);
//        String payment = "";
//        if (lclBookingList.get(0).getBillingType().equalsIgnoreCase("P")) {
//            payment = "Prepaid";
//        } else if (lclBookingList.get(0).getBillingType().equalsIgnoreCase("C")) {
//            payment = "Collect";
//        } else if (lclBookingList.get(0).getBillingType().equalsIgnoreCase("B")) {
//            payment = "Both";
//        }
//
//        writer.writeAttribute("Payment", payment);
//        writer.writeAttribute("ShipmentId", "ECONOCARIBE_GTN_" + lclBookingList.get(0).getLclFileNumber().getFileNumber());
//        String moveType = "";
//        //retrive the lclSsDeatailsObject
//        if (CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList()) && lclUnit.getLclUnitSsList().get(0).getLclSsHeader() != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsDetailList())) {
//            if (CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList()) && lclUnit.getLclUnitSsList().get(0).getLclSsHeader() != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsDetailList())) {
//                lclSsDetail = lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsDetailList().get(0);
//            }
//        }
//        if (lclSsDetail != null && lclSsDetail.getTransMode() != null && lclSsDetail.getTransMode().equalsIgnoreCase("A")) {
//            moveType = "AirToDoor";//in database A,R,T,V and ? enum for the transmode.
//        } else if (lclSsDetail != null && lclSsDetail.getTransMode() != null && lclSsDetail.getTransMode().equalsIgnoreCase("R")) {
//            moveType = "RailToDoor";
//        }
//        writer.writeAttribute("MoveType", moveType);
//        String serviceType = "";
//        if (lclBookingList.get(0).getLclFileNumber() != null && lclBookingList.get(0).getLclFileNumber().getLclBlAir() != null && lclBookingList.get(0).getLclFileNumber().getLclBlAir().getServiceType().equals("D")) {
//            serviceType = "NoCFS";//need to check
//        }
//        writer.writeAttribute("ServiceType", serviceType);
//        String carrierScac = "";
//        if (lclSsDetail != null && lclSsDetail.getSpAcctNo() != null) {
//            carrierScac = lclDetailDAO.findCarrierScac(lclSsDetail.getSpAcctNo().getAccountno(), lclSsDetail.getId());
//        }
//        writer.writeAttribute("CarrierScac", carrierScac);
//        writer.writeAttribute("BillType", "Exress");//need to check
//        writer.writeAttribute("Sequence", "1");//need to check
//        //1- start of booking component References
//        writer.writeStartElement("References");
//
//        //A- start of reference component1
//        if (lclUnit != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList()) && lclUnit.getLclUnitSsList().get(0).getLclSsHeader() != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsMasterBlList())) {
//            lclSSMasterBl = lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsMasterBlList().get(0);
//        }
//        writer.writeStartElement("Reference");
//        writer.writeAttribute("referenceType", "BookingNumber");
//        String spBookingNo = "";
//        if (lclSSMasterBl != null) {
//            spBookingNo = lclSSMasterBl.getSpBookingNo();
//        }
//        writer.writeCharacters(spBookingNo);
//        writer.writeEndElement();
//        //A- end of reference component1
//
//        //B- start of reference component2
//        writer.writeStartElement("Reference");
//        writer.writeAttribute("referenceType", "ContractNumber");
//        String spContractNo = "";
//        if (lclSSMasterBl != null) {
//            spContractNo = lclSSMasterBl.getSpContractNo();
//        }
//        writer.writeCharacters(spContractNo);
//        writer.writeEndElement();
//        //B- end of reference component2
//
//        //C- start of reference component3
//        writer.writeStartElement("Reference");
//        writer.writeAttribute("referenceType", "ExportReferenceNumber");
//        String exportRegEdi = "";
//        if (lclSSMasterBl != null) {
//            exportRegEdi = lclSSMasterBl.getExportRefEdi();
//        }
//        writer.writeCharacters(exportRegEdi);
//        writer.writeEndElement();
//        //C- end of reference component3
//
//        writer.writeEndElement();
//        //1- end of booking component References
//
//        //2- start of booking component Voyage
//        writer.writeStartElement("Voyage");//need to check
//        //A- start of Voyage component1
//        writer.writeStartElement("VoyageNumber");
//        if (lclUnit != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList()) && lclUnit.getLclUnitSsList().get(0) != null) {
//            lclSsHeader = lclUnit.getLclUnitSsList().get(0).getLclSsHeader();
//        }
//        String scheduleNo = "";
//        if (lclSsHeader != null) {
//            scheduleNo = lclSsHeader.getScheduleNo();
//        }
//        writer.writeCharacters(scheduleNo);
//        writer.writeEndElement();
//        //A- end of Voyage component1
//
//        //B- start of Voyage component2
//        writer.writeStartElement("Vessel");
//
//        //X- start of vessel component1
//        writer.writeStartElement("Name");
//        String spReferenceName = "";
//        if (lclSsDetail != null) {
//            spReferenceName = lclSsDetail.getSpReferenceName();
//        }
//        writer.writeCharacters(spReferenceName);
//        writer.writeEndElement();
//        //X- end of vessel component1
//
//        writer.writeEndElement();
//        //B- end of Voyage component2
//        writer.writeEndElement();
//        //2- end of booking component Voyage
//
//        //3- start of booking component Parties
//        writer.writeStartElement("Parties");
//
//        //A- start of Parties component1
//        if (lclSSMasterBl != null && lclSSMasterBl.getShipAcct() != null) {
//            writer.writeStartElement("Party");
//            writer.writeAttribute("Type", "Shipper");
//
//            //X- start of Shipper component1
//            writer.writeStartElement("Name");
//            writer.writeCharacters(lclSSMasterBl.getShipAcct().getAccountName());
//
//            writer.writeEndElement();
//            //X- end of Shipper component1
//
//            //Y- start of Shipper component2
//            writer.writeStartElement("Address");
//
//            //I- start of Address component1
//            if (CommonUtils.isNotEmpty(lclSSMasterBl.getShipAcct().getCustomerAddressSet())) {
//                List<String> shipAddList = getAddressList(lclSSMasterBl.getShipAcct().getCustomerAddressSet().iterator().next());
//                for (String addStr : shipAddList) {
//                    writer.writeStartElement("AddressLine");
//                    writer.writeCharacters(addStr);
//                    writer.writeEndElement();
//                }
//            }
//            //I- end of Address component1
//
//            writer.writeEndElement();
//            //Y- end of Shipper component2
//
//            writer.writeEndElement();
//        }
//        //A- end of Parties component1
//
//        //B- start of Parties component2
//        if (lclSSMasterBl != null && lclSSMasterBl.getConsAcct() != null) {
//            writer.writeStartElement("Party");
//            writer.writeAttribute("Type", "Consignee");
//
//            //X- start of Consignee component1
//            writer.writeStartElement("Name");
//            writer.writeCharacters(lclSSMasterBl.getConsAcct().getAccountName());
//            writer.writeEndElement();
//            //X- end of Consignee component1
//
//            //Y- start of Consignee component2
//            writer.writeStartElement("Address");
//
//            //I- start of Address component1
//            if (CommonUtils.isNotEmpty(lclSSMasterBl.getConsAcct().getCustomerAddressSet())) {
//                List<String> consAddList = getAddressList(lclSSMasterBl.getConsAcct().getCustomerAddressSet().iterator().next());
//                for (String addStr : consAddList) {
//                    writer.writeStartElement("AddressLine");
//                    writer.writeCharacters(addStr);
//                    writer.writeEndElement();
//                }
//            }
//            //I- end of Address component1
//
//            writer.writeEndElement();
//            //Y- end of Consignee component2
//
//            writer.writeEndElement();
//        }
//        //B- end of Parties component2
//
//        //C- start of Parties component3
//        if (lclSSMasterBl != null && lclSSMasterBl.getNotyAcct() != null) {
//            writer.writeStartElement("Party");
//            writer.writeAttribute("Type", "Notify");
//
//            //X- start of notify component1
//            writer.writeStartElement("Name");
//            writer.writeCharacters(lclSSMasterBl.getNotyAcct().getAccountName());
//            writer.writeEndElement();
//            //X- end of notify component1
//
//            //Y- start of notify component2
//            writer.writeStartElement("Address");
//
//            //I- start of Address component1
//            if (CommonUtils.isNotEmpty(lclSSMasterBl.getNotyAcct().getCustomerAddressSet())) {
//                List<String> notyAddList = getAddressList(lclSSMasterBl.getNotyAcct().getCustomerAddressSet().iterator().next());
//                for (String addStr : notyAddList) {
//                    writer.writeStartElement("AddressLine");
//                    writer.writeCharacters(addStr);
//                    writer.writeEndElement();
//                }
//            }
//            //I- end of Address component1
//
//            writer.writeEndElement();
//            //Y- end of notify component2
//
//            writer.writeEndElement();
//        }
//        //C- end of Parties component3
//
//        //D- start of Parties component4
//        if (lclSSMasterBl != null && lclSSMasterBl.getShipAcct() != null) {
//            writer.writeStartElement("Party");
//            writer.writeAttribute("Type", "Submitter");
//            writer.writeAttribute("Code", "08");
//
//            //X- start of Submitter component1
//            writer.writeStartElement("Name");
//            writer.writeCharacters(lclSSMasterBl.getShipAcct().getAccountName());
//            writer.writeEndElement();
//            //X- end of Submitter component1
//
//            //Y- start of Submitter component2
//            writer.writeStartElement("Address");
//            //I- start of Address component1
//            if (CommonUtils.isNotEmpty(lclSSMasterBl.getShipAcct().getCustomerAddressSet())) {
//                List<String> notyAddList = getAddressList(lclSSMasterBl.getShipAcct().getCustomerAddressSet().iterator().next());
//                for (String addStr : notyAddList) {
//                    writer.writeStartElement("AddressLine");
//                    writer.writeCharacters(addStr);
//                    writer.writeEndElement();
//                }
//                //I- end of Address component1
//                writer.writeEndElement();
//                //Y- end of Submitter component2
//                //Z- start of Submitter component3
//                writer.writeStartElement("Contact");//need to check
//                writer.writeAttribute("Type", "Information");
//                writer.writeAttribute("Fax", lclSSMasterBl.getShipAcct().getCustomerAddressSet().iterator().next().getFax());
//                writer.writeAttribute("Email", lclSSMasterBl.getShipAcct().getCustomerAddressSet().iterator().next().getEmail1());
//                writer.writeAttribute("phone", lclSSMasterBl.getShipAcct().getCustomerAddressSet().iterator().next().getPhone());
//                writer.writeCharacters("dcarbo");
//                writer.writeEndElement();
//            }
//            //Z- end of Submitter component3
//
//            writer.writeEndElement();
//        }
//        //D- end of Parties component4
//
//        writer.writeEndElement();
//        //3- end of booking component Parties
//
//        //4- start of booking component Locations
//        writer.writeStartElement("Locations");
//
//        //A -start of locations component1//This is the port of origin
//        writer.writeStartElement("Location");//lclHeader(origin des)--need to check
//        String pooUnLoc = "";
//        if (lclSsHeader != null && lclSsHeader.getOrigin() != null) {
//            pooUnLoc = lclSsHeader.getOrigin().getUnLocationCode();
//        }
//        writer.writeAttribute("Identifier", pooUnLoc);
//        writer.writeAttribute("Qualifier", "UNLOCODE");
//        writer.writeAttribute("Function", "ContractualPlaceOfReceipt");
//        //X -start of location component1
//        writer.writeStartElement("Name");
//        String pooUnLocName = "";
//        if (lclSsHeader != null && lclSsHeader.getOrigin() != null) {
//            pooUnLocName = lclSsHeader.getOrigin().getUnLocationName();
//        }
//        writer.writeCharacters(pooUnLocName);
//        writer.writeEndElement();
//        //X -end of location component1
//        writer.writeEndElement();
//        //A -end of locations component1
//
//        //B -start of locations component2//This is the port of Loading
//        writer.writeStartElement("Location");
//        String polUnLocCode = "";
//        if (lclSsHeader != null && lclSsHeader.getOrigin() != null) {
//            polUnLocCode = lclSsHeader.getOrigin().getUnLocationCode();
//        }
//        writer.writeAttribute("Identifier", polUnLocCode);
//        writer.writeAttribute("Qualifier", "UNLOCODE");
//        writer.writeAttribute("Function", "OperationalPortOfLoading");
//        //X -start of location component1
//        writer.writeStartElement("Name");
//        String polUnLocName = "";
//        if (lclSsHeader != null && lclSsHeader.getOrigin() != null) {
//            polUnLocName = lclSsHeader.getOrigin().getUnLocationName();
//        }
//        writer.writeCharacters(polUnLocName);
//        writer.writeEndElement();
//        //X -end of location component1
//        writer.writeEndElement();
//        //B -end of locations component2
//
//        //C -start of locations component3//Port of destination
//        writer.writeStartElement("Location");
//        String podUnLocCode = "";
//        if (lclSsHeader != null && lclSsHeader.getDestination() != null) {
//            podUnLocCode = lclSsHeader.getDestination().getUnLocationCode();
//        }
//        writer.writeAttribute("Identifier", podUnLocCode);
//        writer.writeAttribute("Qualifier", "UNLOCODE");
//        writer.writeAttribute("Function", "OperationalPortOfDischarge");
//        //X -start of location component1
//        writer.writeStartElement("Name");
//        String podUnLocName = "";
//        if (lclSsHeader != null && lclSsHeader.getDestination() != null) {
//            podUnLocName = lclSsHeader.getDestination().getUnLocationName();
//        }
//        writer.writeCharacters(podUnLocName);
//        writer.writeEndElement();
//        //X -end of location component1
//        writer.writeEndElement();
//        //C -end of locations component3
//
//        //D -start of locations component4//final Destination
//        writer.writeStartElement("Location");
//        String fdUnLocCode = "";
//        if (lclSsHeader != null && lclSsHeader.getDestination() != null) {
//            fdUnLocCode = lclSsHeader.getDestination().getUnLocationCode();
//        }
//        writer.writeAttribute("Identifier", fdUnLocCode);
//        writer.writeAttribute("Qualifier", "UNLOCODE");
//        writer.writeAttribute("Function", "ContractualPlaceOfDelivery");
//        //X -start of location component1
//        writer.writeStartElement("Name");
//        String fdUnLocName = "";
//        if (lclSsHeader != null && lclSsHeader.getDestination() != null) {
//            fdUnLocName = lclSsHeader.getDestination().getUnLocationName();
//        }
//        writer.writeCharacters(fdUnLocName);
//        writer.writeEndElement();
//        //X -end of location component1
//        writer.writeEndElement();
//        //D -end of locations component4
//
//        writer.writeEndElement();
//        //4- end of booking component Locations
//
//        //remove start
//        //5- start of booking component Comment1
//        writer.writeStartElement("Comment");
//        writer.writeEndElement();
//        //5- end of booking component Comment1
//
//        //6- start of booking component Comment2
//        writer.writeStartElement("Comment");
//        writer.writeEndElement();
//        //7- end of booking component Comment2
//
//        //8- start of booking component Clause1
//        writer.writeStartElement("Clause");
//        writer.writeAttribute("Type", "Route");
//        writer.writeCharacters("All Destination Charges Collect");
//        writer.writeEndElement();
//        //8- end of booking component Clause1
//
//        //9- start of booking component Clause2
//        writer.writeStartElement("Clause");
//        writer.writeAttribute("Type", "Route");
//        writer.writeCharacters("**EXPRESS RELEASE**");
//        writer.writeEndElement();
//        //9- end of booking component Clause2
//
//        //10- start of booking component Clause3
//        writer.writeStartElement("Clause");
//        writer.writeAttribute("Type", "Route");
//        writer.writeCharacters("DELIVER TO MAHER TERMINAL FOR");
//        writer.writeEndElement();
//        //10- end of booking component Clause3
//
//        //11- start of booking component Clause4
//        writer.writeStartElement("Clause");
//        writer.writeAttribute("Type", "Route");
//        writer.writeCharacters("CUT OFF 10/11 4PM");
//        writer.writeEndElement();
//        //11- end of booking component Clause4
//        //remove end
//
//
//        //12- start of component ContainerGroups
//        writer.writeStartElement("ContainerGroups");
//        //A- start of ContainerGroups component1
//        writer.writeStartElement("ContainerGroup");
//        //X- start of ContainerGroup component1
//        writer.writeStartElement("Container");
//        writer.writeAttribute("Type", genericCode.getField1());//lclssheader-- lclunitss-- unittype(elite_type)--generic_code(codetypeid=38)--code--field1
//        writer.writeAttribute("EquipmentNumber", lclUnit.getUnitNo().substring(5, 11));
//        writer.writeAttribute("EquipmentPrefix", lclUnit.getUnitNo().substring(0, 4));
//        //I- start of Container component1
//        writer.writeStartElement("SealNumber");
//        writer.writeCharacters("928341");
//        writer.writeEndElement();
//        //I- end of Container component1
//        writer.writeEndElement();
//        //X- end of ContainerGroup component1
//
//        //Y- start of ContainerGroup component2
//        writer.writeStartElement("Commodity");
//        writer.writeAttribute("PackagingForm", "Package");
//        writer.writeAttribute("Quantity", "25");
//        // need to add here for hazmatList
//        List<String> hazmatComments = new ArrayList<String>();
//        List<LclBookingHazmat> lclBookingHazmatList = new ArrayList<LclBookingHazmat>();
//        for (LclBooking lclBooking : lclBookingList) {
//            LclFileNumber lclFileNumber = lclBooking.getLclFileNumber();
//            List<LclBookingPiece> lclBookingPieceList = lclFileNumber.getLclBookingPieceList();
//            for (LclBookingPiece lclBookingPiece : lclBookingPieceList) {
//                lclBookingHazmatList.addAll(lclBookingPiece.getLclBookingHazmatList());
//            }
//            for (LclBookingHazmat lclBookingHazmat : lclBookingHazmatList) {
//                if (CommonUtils.isNotEmpty(lclBookingHazmat.getHazmatDeclarations())) {
//                    hazmatComments.add(lclBookingHazmat.getHazmatDeclarations());
//                } else {
//                    StringBuilder comments = new StringBuilder();
//                    comments.append(lclBookingHazmat.getReportableQuantity() ? "REPORTABLE QUANTITY, " : "");
//                    comments.append(CommonUtils.isNotEmpty(lclBookingHazmat.getUnHazmatNo()) ? "UN " + lclBookingHazmat.getUnHazmatNo() : "");
//                    if (CommonUtils.isNotEmpty(lclBookingHazmat.getProperShippingName())) {
//                        comments.append(", ").append(lclBookingHazmat.getProperShippingName());
//                        if (CommonUtils.isNotEmpty(lclBookingHazmat.getTechnicalName())) {
//                            comments.append(", (").append(lclBookingHazmat.getTechnicalName()).append(")");
//                        }
//                    }
//                    if (CommonUtils.isNotEmpty(lclBookingHazmat.getImoPriClassCode())) {
//                        comments.append(", CLASS ").append(lclBookingHazmat.getImoPriClassCode());
//                        if (CommonUtils.isNotEmpty(lclBookingHazmat.getImoSecSubClassCode())) {
//                            comments.append(" (").append(lclBookingHazmat.getImoSecSubClassCode()).append(")");
//                        }
//                        if (CommonUtils.isNotEmpty(lclBookingHazmat.getImoPriSubClassCode())) {
//                            comments.append(" (").append(lclBookingHazmat.getImoPriSubClassCode()).append(")");
//                        }
//                    }
//                    if (CommonUtils.isNotEmpty(lclBookingHazmat.getPackingGroupCode())) {
//                        comments.append(", PG ").append(lclBookingHazmat.getPackingGroupCode());
//                    }
//                    if (CommonUtils.isNotEmpty(lclBookingHazmat.getFlashPointUom())) {
//                        comments.append(", FLASH POINT (").append(lclBookingHazmat.getFlashPointUom()).append(")");
//                    }
//                    if (CommonUtils.isNotEmpty(lclBookingHazmat.getOuterPkgNoPieces())) {
//                        comments.append(", ").append(lclBookingHazmat.getOuterPkgNoPieces());
//                        if (CommonUtils.isNotEmpty(lclBookingHazmat.getOuterPkgComposition())) {
//                            comments.append(" ").append(lclBookingHazmat.getOuterPkgComposition());
//                        }
//                        if (CommonUtils.isNotEmpty(lclBookingHazmat.getOuterPkgType())) {
//                            comments.append(" ").append(lclBookingHazmat.getOuterPkgType());
//                        }
//                        if (CommonUtils.isNotEmpty(lclBookingHazmat.getInnerPkgNoPieces())) {
//                            comments.append(", ").append(lclBookingHazmat.getInnerPkgNoPieces());
//                            if (CommonUtils.isNotEmpty(lclBookingHazmat.getInnerPkgComposition())) {
//                                comments.append(" ").append(lclBookingHazmat.getInnerPkgComposition());
//                            }
//                            if (CommonUtils.isNotEmpty(lclBookingHazmat.getInnerPkgType())) {
//                                comments.append(" ").append(lclBookingHazmat.getInnerPkgType());
//                            }
//                        }
//                    }
//                    /* if (CommonUtils.isNotEmpty(lclBookingHazmat.get)) {
//                    comments.append(" @ ").append(NumberUtils.formatNumber(lclBookingHazmat.getNetWeight(), "0.00"));
//                    comments.append(" ").append(lclBookingHazmat.getNetWeightUMO()).append(" EACH");
//                    }
//                    if (CommonUtils.isNotEmpty(lclBookingHazmat.getGrossWeight())) {
//                    comments.append(", TOTAL GROSS WT ");
//                    comments.append(NumberUtils.formatNumber(lclBookingHazmat.getGrossWeight(), "0.00")).append(" KGS");
//                    }
//                    if (CommonUtils.isNotEmpty(lclBookingHazmat.getTotalNetWeight())) {
//                    comments.append(", TOTAL NET WT ");
//                    comments.append(NumberUtils.formatNumber(lclBookingHazmat.getTotalNetWeight(), "0.00")).append(" KGS");
//                    }
//                    if (CommonUtils.isNotEmpty(lclBookingHazmat.getLiquidVolume())) {
//                    comments.append(NumberUtils.formatNumber(lclBookingHazmat.getVolume(), "0.00"));
//                    comments.append(lclBookingHazmat.getVolume() > 1d ? " LITERS" : " LITER");
//                    }*/
//                    comments.append(lclBookingHazmat.getMarinePollutant() ? ", MARINE POLLUTANT" : "");
//                    comments.append(lclBookingHazmat.getExceptedQuantity() ? ", EXCEPTED QUANTITY" : "");
//                    comments.append(lclBookingHazmat.getLimitedQuantity() ? ", LIMITED QUANTITY" : "");
//                    comments.append(lclBookingHazmat.getInhalationHazard() ? ", INHALATION HAZARD" : "");
//                    comments.append(lclBookingHazmat.getResidue() ? ", RESIDUE" : "");
//                    comments.append(CommonUtils.isNotEmpty(lclBookingHazmat.getEmsCode()) ? ", EMS " + lclBookingHazmat.getEmsCode() : "");
////			    comments.append(CommonUtils.isNotEmpty(lclBookingHazmat.getContactName()) ? ", " + lclBookingHazmat.getContactName() : "");
////			    comments.append(CommonUtils.isNotEmpty(lclBookingHazmat.getEmerreprsNum()) ? ", " + lclBookingHazmat.getEmerreprsNum() : "");
//                    hazmatComments.addAll(CommonUtils.splitString(comments.toString(), ".{0,48}(?:\\S(?:-| |$)|$)"));
//                }
//            }
//        }
//        //end of the hazmat list
//        //have to add weight_qualifier and unit_volume
//        if (CommonUtils.isNotEmpty(hazmatComments)) {
//            for (String description : hazmatComments) {
//                //Start of hazmat DescriptionLine
//                writer.writeStartElement("DescriptionLine");
//                writer.writeCharacters(description);
//                writer.writeEndElement();
//                //End of hazmat DescriptionLine
//            }
//        }
//        writer.writeEndElement();
//        //Y- end of ContainerGroup component2
//
//        writer.writeEndElement();
//        //A- end of ContainerGroups component1
//        writer.writeEndElement();
//        //12- end of component ContainerGroups
//
//        //13- start of booking component Paperwork
//        writer.writeStartElement("Paperwork");
//        writer.writeAttribute("Type", "SeaWaybill");
//        writer.writeAttribute("Quantity", "1");
//        writer.writeAttribute("Rated", "true");
//        writer.writeEndElement();
//        //13- end of booking component Paperwork
//
//        writer.writeEndElement();
//        //End of shipmentdetails with hazardous############ second parent
//
//        writer.writeEndElement();
//        //End of Message
//
//        writer.writeEndDocument();
//        //End of Document
//    }
//
//    public void exit() throws Exception {
//        if (null != writer) {
//            writer.flush();
//            writer.close();
//        }
//        if (null != outputStream) {
//            outputStream.close();
//        }
//    }
//
//    public void logErrors(String fileNumber, Exception e) throws Exception {
//        String osName = System.getProperty("os.name").toLowerCase();
//        StringBuilder errorFileName = new StringBuilder();
//        errorFileName.append("error_logfile_").append(fileName.toString()).append("_").append(dateTimeSeconds).append(".txt");
//        new EdiTrackingBC().setEdiLog(fileName.toString(), dateTimeSeconds, "failure", errors.toString(), "I", "", fileNumber, bookingNumber, "", null);
//        String errorMessage = null != e ? "Type of Error is---" + e.toString() : errors.toString();
//        new LogFileWriter().doAppend(errorMessage, errorFileName.toString(), "I", osName, "304");
//    }
//
//    public void deleteFile() throws Exception {
//        if (null != file && file.exists()) {
//            if (null != writer) {
//                writer.close();
//                writer = null;
//            }
//            if (null != outputStream) {
//                outputStream.close();
//                outputStream = null;
//            }
//            file.delete();
//        }
//    }
//
//    public String create(List<LclBooking> lclBookingList, LclUnit lclUnit, HttpServletRequest request) throws Exception {
//        try {
//            init(lclBookingList);
//            write(lclBookingList, lclUnit, request);
//            if (CommonUtils.isNotEmpty(errors)) {
//                deleteFile();
//                return "<span color: #000080;font-size: 10px;>Error Message</span><br/>" + errors.toString();
//            } //            else if (CommonUtils.isNotEmpty(warnings)) {
//            //		deleteFile();
//            //		return warnings.toString();
//            //	    }
//            else {
//                return "XML generated successfully";
//            }
//        } catch (Exception e) {
//            deleteFile();
//            return e.toString();
//        } finally {
//            exit();
//        }
//    }
//
//    public List<String> getAddressList(CustomerAddress customerAddress) throws Exception {
//        helperClass = new HelperClass();
//        List<String> list = new ArrayList<String>();
//        StringBuffer buffer = new StringBuffer();
//        if (customerAddress != null) {
//            buffer.append(customerAddress.getAddress1());
//            buffer.append(customerAddress.getAddress2());
//            buffer.append(customerAddress.getCity1());
//            buffer.append(customerAddress.getCity2());
//            buffer.append(",");
//            buffer.append(customerAddress.getState());
//            buffer.append(",");
//            buffer.append(customerAddress.getCuntry());
//            buffer.append(",");
//            buffer.append(customerAddress.getZip());
//        }
//        if (CommonUtils.isNotEmpty(buffer.toString())) {
//            list = helperClass.splitString(buffer.toString(), 35);
//        }
//        return list;
//    }
//}
