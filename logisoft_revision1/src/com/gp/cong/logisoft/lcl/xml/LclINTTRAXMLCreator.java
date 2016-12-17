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
//import com.gp.cong.common.DateUtils;
//import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
//import com.gp.cong.logisoft.domain.CarriersOrLine;
//import com.gp.cong.logisoft.domain.CustomerAddress;
//import com.gp.cong.logisoft.domain.RefTerminal;
//import com.gp.cong.logisoft.domain.User;
//import com.gp.cong.logisoft.domain.lcl.LclBooking;
//import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
//import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
//import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
//import com.gp.cong.logisoft.domain.lcl.LclInbond;
//import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
//import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
//import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
//import com.gp.cong.logisoft.domain.lcl.LclUnit;
//import com.gp.cong.logisoft.edi.gtnexus.HelperClass;
//import com.gp.cong.logisoft.edi.util.LogFileWriter;
//import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
//import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
//import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
//import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
//import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
//import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
//import com.gp.cong.struts.LoadLogisoftProperties;
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
//import org.apache.commons.lang3.StringUtils;
//
//public class LclINTTRAXMLCreator {
//
//    private static final String ENCODING = "ISO-8859-1";
//    private OutputStream outputStream = null;
//    private XMLStreamWriter writer = null;
//    private File file = null;
//    private StringBuilder fileName = new StringBuilder();
//    private String fullDate = null;
//    private String dateTime = null;
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
//    private LclSsDetail lclSsDetail = new LclSsDetail();
//    private LclSsHeader lclSsHeader = new LclSsHeader();
//    private LclSSMasterBl lclSSMasterBl = new LclSSMasterBl();
//    HelperClass helperClass = null;
//
//    private void init(LclBooking lclBooking) throws Exception {
//        //Date Time values
//        Date date = new Date();
//        fullDate = DateUtils.formatDate(date, "yyyyMMdd");
//        dateTime = DateUtils.formatDate(date, "yyMMddHHmm");
//        dateTimeSeconds = DateUtils.formatDate(date, "yyyyMMddHHmmss");
//        String folderName = LoadLogisoftProperties.getProperty("lcl.inttra.xmlLocation");
//        File folder = new File(folderName);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        fileName.append("A00555").append("_").append(companyName).append("_INTTRA_").append(dateTimeSeconds).append(".xml");
//        docVersionCount = new EdiDAO().getDocVersion(fileName.toString());
//
//        file = new File(folder, fileName.toString());
//        outputStream = new FileOutputStream(file);
//        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
//        writer = outputFactory.createXMLStreamWriter(outputStream, ENCODING);
//    }
//
//    public void write(List<LclBooking> lclBookingList, LclUnit lclUnit, LclBooking lclBooking, HttpServletRequest request) throws Exception {
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
//        writer.writeStartElement("Message");
//
//        //Start of Header
//        writer.writeStartElement("Header");
//
//        //Start of MessageType
//        writer.writeStartElement("MessageType");
//        writer.writeAttribute("MessageVersion", "1.0");
//        writer.writeCharacters("ShippingInstruction");
//        writer.writeEndElement();
//        //End of MessageType
//
//        //Start of DocumentIdentifier
//        writer.writeStartElement("DocumentIdentifier");
//        writer.writeCharacters(documentIdentifier);
//        writer.writeEndElement();
//        //End of DocumentIdentifier
//
//        //Start of DateTime - Document
//        writer.writeStartElement("DateTime");
//        writer.writeAttribute("DateType", "Document");
//        writer.writeCharacters(dateTime);
//        writer.writeEndElement();
//        //End of DateTime - Document
//
//        //Start of Parties
//        writer.writeStartElement("Parties");
//
//        //Start of PartnerInformation - Sender
//        writer.writeStartElement("PartnerInformation");
//        writer.writeAttribute("PartnerRole", "Sender");
//
//        //Start of PartnerIdentifier
//        writer.writeStartElement("PartnerIdentifier");
//        writer.writeAttribute("Agency", "AssignedBySender");
//        writer.writeCharacters(companyCode);
//        writer.writeEndElement();
//        //End of PartnerIdentifier
//
//        writer.writeEndElement();
//        //End of PartnerInformation - Sender
//
//        //Start of PartnerInformation - Recipient
//        writer.writeStartElement("PartnerInformation");
//        writer.writeAttribute("PartnerRole", "Recipient");
//
//        //Start of PartnerIdentifier
//        writer.writeStartElement("PartnerIdentifier");
//        writer.writeAttribute("Agency", "AssignedByRecipient");
//        writer.writeCharacters("INTTRA");
//        writer.writeEndElement();
//        //End of PartnerIdentifier
//
//        writer.writeEndElement();
//        //End of PartnerInformation - Recipient
//
//        writer.writeEndElement();
//        //End of Parties
//
//        writer.writeEndElement();
//        //End of Header
//
//        //Start of MessageBody
//        writer.writeStartElement("MessageBody");
//
//        //Start of MessageProperties
//        writer.writeStartElement("MessageProperties");
//
//        //Start of ShipmentID
//        writer.writeStartElement("ShipmentID");
//
//        //Start of ShipmentIdentifier
//        writer.writeStartElement("ShipmentIdentifier");
//        String messageStatus = docVersionCount > 0 ? "Amendment" : "Original";
//        writer.writeAttribute("MessageStatus", messageStatus);
//        String shipmentId = documentIdentifier + "_" + dateTimeSeconds;
//        writer.writeCharacters(shipmentId);
//        writer.writeEndElement();
//        //End of ShipmentIdentifier
//
//        //Start of DocumentVersion
//        writer.writeStartElement("DocumentVersion");
//        String documentVersion = StringUtils.leftPad("" + (docVersionCount > 0 ? docVersionCount : 1), 6, "0");
//        writer.writeCharacters(documentVersion);
//        writer.writeEndElement();
//        //End of DocumentVersion
//
//        writer.writeEndElement();
//        //End of ShipmentID
//
//        //Start of DateTime - Message
//        writer.writeStartElement("DateTime");
//        writer.writeAttribute("DateType", "Message");
//        writer.writeCharacters(fullDate);
//        writer.writeEndElement();
//        //End of DateTime - Message
//
//        //FclBl bl = new FclBlDAO().getOriginalBl("276240");
//        //Start of ChargeCategory
//        writer.writeStartElement("ChargeCategory");
//        String prepaidorCollectIndicator = "Prepaid";//need Clarifiction
//        writer.writeAttribute("PrepaidorCollectIndicator", prepaidorCollectIndicator);//need Clarification
//        String chargeType = "BasicFreight";
//        writer.writeAttribute("ChargeType", chargeType);
//        writer.writeEndElement();
//        //End of ChargeCategory
//
//        if (CommonUtils.isEqualIgnoreCase(chargeType, "BasicFreight")) {
//            //Start of ChargeCategory - AdditionalCharges
//            writer.writeStartElement("ChargeCategory");
//            prepaidorCollectIndicator = CommonUtils.isEqualIgnoreCase(prepaidorCollectIndicator, "Prepaid") ? "Collect" : "Prepaid";
//            writer.writeAttribute("PrepaidorCollectIndicator", prepaidorCollectIndicator);
//            writer.writeAttribute("ChargeType", "AdditionalCharges");
//            writer.writeEndElement();
//            //End of ChargeCategory - AdditionalCharges
//        }
//
//        if (CommonUtils.isNotEmpty(lclBooking.getLclFileNumber().getFileNumber())) {
//            bookingNumber = lclBooking.getLclFileNumber().getFileNumber();
//            //Start of ReferenceInformation - BookingNumber
//            writer.writeStartElement("ReferenceInformation");
//            writer.writeAttribute("ReferenceType", "BookingNumber");
//            writer.writeCharacters(lclBooking.getLclFileNumber().getFileNumber());
//            writer.writeEndElement();
//            //End of ReferenceInformation - BookingNumber
//        } else {
//            errors.append("--> Booking Number is invalid<br/>");
//        }
//
//        CarriersOrLine carriersOrLine = null;
//        if (CommonUtils.isNotEmpty(lclBooking.getAgentAcct().getAccountno())) {
//            carriersOrLine = new CarriersOrLineDAO().getScacAndContractNumber(lclBooking.getAgentAcct().getAccountno());
//        }
//
//        if (null != carriersOrLine && CommonUtils.isNotEmpty(carriersOrLine.getFclContactNumber())) {
//            //Start of ReferenceInformation - ContractNumber
//            writer.writeStartElement("ReferenceInformation");
//            writer.writeAttribute("ReferenceType", "ContractNumber");
//            writer.writeCharacters(carriersOrLine.getFclContactNumber());
//            writer.writeEndElement();
//            //End of ReferenceInformation - ContractNumber
//        }
//
//        //Start of ReferenceInformation - ExportersReferenceNumber
//        writer.writeStartElement("ReferenceInformation");
//        writer.writeAttribute("ReferenceType", "ExportersReferenceNumber");
//        writer.writeCharacters(companyCode + " Ref " + referenceNumber);
//        writer.writeEndElement();
//        //End of ReferenceInformation - ExportersReferenceNumber
//
//        if (CommonUtils.isNotEmpty(lclBooking.getSupReference())) {
//            if (lclBooking.getSupReference().length() > 30) {
//                errors.append("--> Export Reference length must be less than 30 characters<br/>");
//            } else {
//                for (String exportersReferenceNumber : CommonUtils.splitString(lclBooking.getSupReference())) {
//                    //Start of ReferenceInformation - ExportersReferenceNumber
//                    writer.writeStartElement("ReferenceInformation");
//                    writer.writeAttribute("ReferenceType", "ExportersReferenceNumber");
//                    writer.writeCharacters(exportersReferenceNumber);
//                    writer.writeEndElement();
//                    //End of ReferenceInformation - ExportersReferenceNumber
//                }
//            }
//        }
//
//        List<LclInbond> inbondDetailList = new LclInbondsDAO().findByProperty("lclFileNumber", lclBooking.getLclFileNumber().getId());
//        if (CommonUtils.isNotEmpty(inbondDetailList)) {
//            for (LclInbond inbondDetails : inbondDetailList) {
//                if (CommonUtils.isNotEmpty(inbondDetails.getInbondNo())) {
//                    //Start of ReferenceInformation - ExportersReferenceNumber
//                    writer.writeStartElement("ReferenceInformation");
//                    writer.writeAttribute("ReferenceType", "ExportersReferenceNumber");
//                    writer.writeCharacters(inbondDetails.getInbondNo());
//                    writer.writeEndElement();
//                    //End of ReferenceInformation - ExportersReferenceNumber
//                }
//            }
//        }
//
//        //need clarification for AES Details...
//        if (CommonUtils.isNotEmpty(inbondDetailList)) {
//            for (LclInbond aesDetails : inbondDetailList) {
//                if (CommonUtils.isNotEmpty(aesDetails.getInbondNo())) {
//                    //Start of ReferenceInformation - TransactionReferenceNumber
//                    writer.writeStartElement("ReferenceInformation");
//                    writer.writeAttribute("ReferenceType", "TransactionReferenceNumber");
//                    writer.writeCharacters(aesDetails.getInbondNo());
//                    writer.writeEndElement();
//                    //End of ReferenceInformation - TransactionReferenceNumber
//                }
//                if (CommonUtils.isNotEmpty(aesDetails.getInbondType())) {
//                    //Start of ReferenceInformation - TransactionReferenceNumber
//                    writer.writeStartElement("ReferenceInformation");
//                    writer.writeAttribute("ReferenceType", "TransactionReferenceNumber");
//                    writer.writeCharacters(aesDetails.getInbondType());
//                    writer.writeEndElement();
//                    //End of ReferenceInformation - TransactionReferenceNumber
//                }
//            }
//        }
//        String routingInstructions = null;
//        routingInstructions = ediDAO.getRoutingInstruction("1");//need to remove the hard code and pass actual clause
//
//        if (CommonUtils.isNotEmpty(routingInstructions)) {
//            //Start of Instructions
//            writer.writeStartElement("Instructions");
//            if (CommonUtils.isNotEmpty(routingInstructions)) {
//                //Start of ShipmentComments - RoutingInstructions
//                writer.writeStartElement("ShipmentComments");
//                writer.writeAttribute("CommentType", "RoutingInstructions");
//                writer.writeCharacters(routingInstructions);
//                writer.writeEndElement();
//                //End of ShipmentComments - RoutingInstructions
//            }
//
//            writer.writeEndElement();
//            //End of Instructions
//        }
//        String movementType = null;
//        //Start of HaulageDetails
//        writer.writeStartElement("HaulageDetails");
//        writer.writeAttribute("MovementType", "PortToDoor");
//        writer.writeAttribute("ServiceType", "FullLoad");
//        writer.writeEndElement();
//        //End of HaulageDetails
//        //Start of TransportationDetails
//        writer.writeStartElement("TransportationDetails");
//        writer.writeAttribute("TransportStage", "Main");
//        writer.writeAttribute("TransportMode", "Maritime");
//
//        //Start of ConveyanceInformation
//        writer.writeStartElement("ConveyanceInformation");
//        if (CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList()) && lclUnit.getLclUnitSsList().get(0).getLclSsHeader() != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsDetailList())) {
//            lclSsDetail = lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsDetailList().get(0);
//        }
//        String conveyanceName = "";
//        if (null != lclSsDetail && lclSsDetail.getSpReferenceName() != null) {
//            //Start of ConveyanceName
//            conveyanceName = lclSsDetail.getSpReferenceName();
//            writer.writeStartElement("ConveyanceName");
//            writer.writeCharacters(conveyanceName);
//            writer.writeEndElement();
//            //End of ConveyanceName
//        } else {
//            warnings.append("--> Please Enter Vessal Name<br/>");
//        }
//        String voyageTripNumber = "";
//        if (null != lclSsDetail && lclSsDetail.getSpReferenceNo() != null) {
//            //Start of VoyageTripNumber
//            writer.writeStartElement("VoyageTripNumber");
//            writer.writeCharacters(voyageTripNumber);
//            writer.writeEndElement();
//            //End of VoyageTripNumber
//        } else {
//            warnings.append("--> Please Enter Voyage Number<br/>");
//        }
//
//        if (null != carriersOrLine && CommonUtils.isNotEmpty(carriersOrLine.getSCAC())) {
//            if (carriersOrLine.getSCAC().length() >= 2 && carriersOrLine.getSCAC().length() <= 4) {
//                //Start of CarrierSCAC
//                writer.writeStartElement("CarrierSCAC");
//                writer.writeCharacters(carriersOrLine.getSCAC());
//                writer.writeEndElement();
//                //End of CarrierSCAC
//            } else {
//                errors.append("--> Carrier Scac Code(SSLINE) length must be between 2 & 4<br/>");
//            }
//        } else {
//            errors.append("--> Carrier Scac Code(SSLINE) is not matching<br/>");
//        }
//
//        writer.writeEndElement();
//        //End of ConveyanceInformation
//
//        if (lclUnit != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList()) && lclUnit.getLclUnitSsList().get(0) != null) {
//            lclSsHeader = lclUnit.getLclUnitSsList().get(0).getLclSsHeader();
//        }
//        String placeOfReceiptCode = "";
//        String placeOfReceiptName = "";
//        if (lclSsHeader != null && lclSsHeader.getOrigin() != null) {
//            //Start of Location - PlaceOfReceipt
//            placeOfReceiptCode = lclSsHeader.getOrigin().getUnLocationCode();
//            writer.writeStartElement("Location");
//            writer.writeAttribute("LocationType", "PlaceOfReceipt");
//
//            //Start of LocationCode
//            writer.writeStartElement("LocationCode");
//            writer.writeAttribute("Agency", "UN");
//            writer.writeCharacters(placeOfReceiptCode);
//            writer.writeEndElement();
//            //End of LocationCode
//
//            //Start of LocationName
//            placeOfReceiptName = lclSsHeader.getOrigin().getUnLocationName();
//            writer.writeStartElement("LocationName");
//            writer.writeCharacters(placeOfReceiptName);
//            writer.writeEndElement();
//            //End of LocationName
//
//            writer.writeEndElement();
//            //End of Location - PlaceOfReceipt
//        } else if (CommonUtils.in(movementType, "DOOR TO DOOR", "DOOR TO PORT", "DOOR TO RAIL")) {
//            warnings.append("--> Please Enter Place Of Receipt<br/>");
//        }
//
//        String portOfLoadingCode = null;
//        String portOfLoadingName = null;
//        if (lclSsHeader != null && lclSsHeader.getOrigin() != null) {
//            //Start of Location - PortOfLoading
//            portOfLoadingCode = lclSsHeader.getOrigin().getUnLocationCode();
//            portOfLoadingName = lclSsHeader.getOrigin().getUnLocationName();
//            writer.writeStartElement("Location");
//            writer.writeAttribute("LocationType", "PortOfLoading");
//
//            //Start of LocationCode
//            writer.writeStartElement("LocationCode");
//            writer.writeAttribute("Agency", "UN");
//            writer.writeCharacters(portOfLoadingCode);
//            writer.writeEndElement();
//            //End of LocationCode
//
//            //Start of LocationName
//            writer.writeStartElement("LocationName");
//            writer.writeCharacters(portOfLoadingName);
//            writer.writeEndElement();
//            //End of LocationName
//
//            writer.writeEndElement();
//            //End of Location - PortOfLoading
//        } else {
//            warnings.append("--> Please Enter Port Of Loading<br/>");
//        }
//
//        String portOfDischargeCode = null;
//        String portOfDischargeName = null;
//        if (lclSsHeader != null && lclSsHeader.getDestination() != null) {
//            portOfDischargeCode = lclSsHeader.getDestination().getUnLocationCode();
//            portOfDischargeName = lclSsHeader.getDestination().getUnLocationName();
//            //Start of Location - PortOfDischarge
//            writer.writeStartElement("Location");
//            writer.writeAttribute("LocationType", "PortOfDischarge");
//
//            //Start of LocationCode
//            writer.writeStartElement("LocationCode");
//            writer.writeAttribute("Agency", "UN");
//            writer.writeCharacters(portOfDischargeCode);
//            writer.writeEndElement();
//            //End of LocationCode
//
//            //Start of LocationName
//            writer.writeStartElement("LocationName");
//            writer.writeCharacters(portOfDischargeName);
//            writer.writeEndElement();
//            //End of LocationName
//
//            writer.writeEndElement();
//            //End of Location - PortOfDischarge
//        } else {
//            warnings.append("--> Please Enter Port Of Discharge<br/>");
//        }
//
//        String placeOfDeliveryCode = null;
//        String placeOfDeliveryName = null;
//        if (lclSsHeader != null && lclSsHeader.getDestination() != null) {
//            placeOfDeliveryCode = lclSsHeader.getDestination().getUnLocationCode();
//            placeOfDeliveryName = lclSsHeader.getDestination().getUnLocationName();
//            //Start of Location - PlaceOfDelivery
//            writer.writeStartElement("Location");
//            writer.writeAttribute("LocationType", "PlaceOfDelivery");
//
//            //Start of LocationCode
//            writer.writeStartElement("LocationCode");
//            writer.writeAttribute("Agency", "UN");
//            writer.writeCharacters(placeOfDeliveryCode);
//            writer.writeEndElement();
//            //End of LocationCode
//
//            //Start of LocationName
//            writer.writeStartElement("LocationName");
//            writer.writeCharacters(placeOfDeliveryName);
//            writer.writeEndElement();
//            //End of LocationName
//
//            writer.writeEndElement();
//            //End of Location - PlaceOfDelivery
//        } else if (CommonUtils.in(movementType, "DOOR TO DOOR", "DOOR TO PORT", "DOOR TO RAIL")) {
//            warnings.append("--> Please Enter Place Of Delivery<br/>");
//        }
//
//        writer.writeEndElement();
//        //End of TransportationDetails
//
//        //Start of Parties
//        writer.writeStartElement("Parties");
//        if (lclUnit != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList()) && lclUnit.getLclUnitSsList().get(0).getLclSsHeader() != null && CommonUtils.isNotEmpty(lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsMasterBlList())) {
//            lclSSMasterBl = lclUnit.getLclUnitSsList().get(0).getLclSsHeader().getLclSsMasterBlList().get(0);
//        }
//        if (lclSSMasterBl != null && lclSSMasterBl.getShipAcct() != null) {
//            //Start of PartnerInformation - Shipper
//            writer.writeStartElement("PartnerInformation");
//            writer.writeAttribute("PartnerRole", "Shipper");
//
//            //Start of PartnerName
//            writer.writeStartElement("PartnerName");
//            writer.writeCharacters(lclSSMasterBl.getShipAcct().getAccountName());
//            writer.writeEndElement();
//            //End of PartnerName
//
//            if (CommonUtils.isNotEmpty(lclSSMasterBl.getShipAcct().getCustomerAddressSet())) {
//                //Start of AddressInformation
//                writer.writeStartElement("AddressInformation");
//                int count = 0;
//                List<String> shipAddList = getAddressList(lclSSMasterBl.getShipAcct().getCustomerAddressSet().iterator().next());
//                for (String address : shipAddList) {
//                    String addressLine = address.replaceAll("\\p{Cntrl}", "");
//                    if (CommonUtils.isNotEmpty(addressLine)) {
//                        if (addressLine.length() > 35) {
//                            errors.append("--> Master Shipper Address can not be greater than 35 characters per line<br/>");
//                            break;
//                        } else {
//                            //Start of AddressLine
//                            writer.writeStartElement("AddressLine");
//                            writer.writeCharacters(addressLine);
//                            writer.writeEndElement();
//                            //End of AddressLine
//                        }
//                        count++;
//                    }
//                    if (count > 4) {
//                        errors.append("--> Master Shipper Address can not be greater than 4 lines<br/>");
//                        break;
//                    }
//                }
//                writer.writeEndElement();
//                //End of PartnerInformation - AddressInformation
//            }
//
//            writer.writeEndElement();
//            //End of PartnerInformation - Shipper
//        } else {
//            warnings.append("--> Please Enter Master Shipper Name<br/>");
//        }
//
//        if (null != carriersOrLine && CommonUtils.isNotEmpty(carriersOrLine.getSCAC())
//                && carriersOrLine.getSCAC().length() >= 2 && carriersOrLine.getSCAC().length() <= 4) {
//            //Start of PartnerInformation - Carrier
//            writer.writeStartElement("PartnerInformation");
//            writer.writeAttribute("PartnerRole", "Carrier");
//
//            //Start of PartnerIdentifier
//            writer.writeStartElement("PartnerIdentifier");
//            writer.writeAttribute("Agency", "AssignedBySender");
//            writer.writeCharacters(carriersOrLine.getSCAC());
//            writer.writeEndElement();
//            //End of PartnerIdentifier
//
//            writer.writeEndElement();
//            //End of PartnerInformation - Carrier
//        }
//
//        if (lclSSMasterBl != null && lclSSMasterBl.getConsAcct() != null) {
//            //Start of PartnerInformation - Consignee
//            writer.writeStartElement("PartnerInformation");
//            writer.writeAttribute("PartnerRole", "Consignee");
//
//            //Start of PartnerName
//            writer.writeStartElement("PartnerName");
//            writer.writeCharacters(lclSSMasterBl.getConsAcct().getAccountName());
//            writer.writeEndElement();
//            //End of PartnerName
//
//            if (CommonUtils.isNotEmpty(lclSSMasterBl.getConsAcct().getCustomerAddressSet())) {
//                //Start of AddressInformation
//                List<String> consAddList = getAddressList(lclSSMasterBl.getConsAcct().getCustomerAddressSet().iterator().next());
//                writer.writeStartElement("AddressInformation");
//                int count = 0;
//                for (String address : consAddList) {
//                    String addressLine = address.replaceAll("\\p{Cntrl}", "");
//                    if (CommonUtils.isNotEmpty(addressLine)) {
//                        if (addressLine.length() > 35) {
//                            // errors.append("--> Master Consignee Address can not be greater than 35 characters per line<br/>");
//                            // break;
//                        } else {
//                            //Start of AddressLine
//                            writer.writeStartElement("AddressLine");
//                            writer.writeCharacters(addressLine);
//                            writer.writeEndElement();
//                            //End of AddressLine
//                        }
//                        count++;
//                    }
//                    if (count > 14) {
//                        errors.append("--> Master Consignee Address can not be greater than 4 lines<br/>");
//                        break;
//                    }
//                }
//                writer.writeEndElement();
//                //End of PartnerInformation - AddressInformation
//            }
//
//            writer.writeEndElement();
//            //End of PartnerInformation - Consignee
//        } else {
//            warnings.append("--> Please Enter Master Consignee Name<br/>");
//        }
//
//        if (lclSSMasterBl != null && lclSSMasterBl.getNotyAcct() != null) {
//            //Start of PartnerInformation - NotifyParty
//            writer.writeStartElement("PartnerInformation");
//            writer.writeAttribute("PartnerRole", "NotifyParty");
//
//            //Start of PartnerName
//            writer.writeStartElement("PartnerName");
//            writer.writeCharacters(lclSSMasterBl.getNotyAcct().getAccountName());
//            writer.writeEndElement();
//            //End of PartnerName
//            if (CommonUtils.isNotEmpty(lclSSMasterBl.getNotyAcct().getCustomerAddressSet())) {
//                List<String> notyAddList = getAddressList(lclSSMasterBl.getNotyAcct().getCustomerAddressSet().iterator().next());
//                //Start of AddressInformation
//                writer.writeStartElement("AddressInformation");
//                int count = 0;
//                for (String address : notyAddList) {
//                    String addressLine = address.replaceAll("\\p{Cntrl}", "");
//                    if (CommonUtils.isNotEmpty(addressLine)) {
//                        if (addressLine.length() > 35) {
//                            errors.append("--> Master Notify Party Address can not be greater than 35 characters per line<br/>");
//                            break;
//                        } else {
//                            //Start of AddressLine
//                            writer.writeStartElement("AddressLine");
//                            writer.writeCharacters(addressLine);
//                            writer.writeEndElement();
//                            //End of AddressLine
//                        }
//                        count++;
//                    }
//                    if (count > 4) {
//                        errors.append("--> Master Notify Party Address can not be greater than 4 lines<br/>");
//                        break;
//                    }
//                }
//                writer.writeEndElement();
//                //End of PartnerInformation - AddressInformation
//            }
//
//            writer.writeEndElement();
//            //End of PartnerInformation - NotifyParty
//        }
//
//        //Start of PartnerInformation - Requestor
//        writer.writeStartElement("PartnerInformation");
//        writer.writeAttribute("PartnerRole", "Requestor");
//
//        //Start of PartnerIdentifier
//        writer.writeStartElement("PartnerIdentifier");
//        writer.writeAttribute("Agency", "AssignedBySender");
//        writer.writeCharacters(companyCode);
//        writer.writeEndElement();
//        //End of PartnerIdentifier
//
//        //Start of PartnerName
//        writer.writeStartElement("PartnerName");
//        writer.writeCharacters(partnerName);
//        writer.writeEndElement();
//        //End of PartnerName
//
//        //Start of ContactInformation
//        writer.writeStartElement("ContactInformation");
//
//        //Start of ContactName
//        writer.writeStartElement("ContactName");
//        writer.writeAttribute("ContactType", "Informational");
//        writer.writeCharacters(user.getFirstName());
//        writer.writeEndElement();
//        //End of ContactName
//
//        if (CommonUtils.isNotEmpty(user.getTelephone())) {
//            //Start of CommunicationValue - Telephone
//            writer.writeStartElement("CommunicationValue");
//            writer.writeAttribute("CommunicationType", "Telephone");
//            writer.writeCharacters(user.getTelephone());
//            writer.writeEndElement();
//            //End of CommunicationValue - Telephone
//        }
//
//        if (CommonUtils.isNotEmpty(user.getFax())) {
//            //Start of CommunicationValue - Fax
//            writer.writeStartElement("CommunicationValue");
//            writer.writeAttribute("CommunicationType", "Fax");
//            writer.writeCharacters(user.getFax());
//            writer.writeEndElement();
//            //End of CommunicationValue - Fax
//        }
//
//        String email = user.getEmail();
//
//        RefTerminal terminal = null;
//        String billingTerminal = "ATLANTA, GA-17";//need to clarify
//        if (CommonUtils.isNotEmpty(billingTerminal)) {
//            String terminalNumber = StringUtils.substring(billingTerminal, StringUtils.lastIndexOf(billingTerminal, "-") + 1);
//            terminal = new RefTerminalDAO().findById(terminalNumber);
//            if (null != terminal) {
//                String terminalEmail = ediDAO.getUserEmail(terminalNumber);
//                if (CommonUtils.isEmpty(email) && CommonUtils.isNotEmpty(terminalEmail)) {
//                    email = terminalEmail;
//                }
//            }
//        }
//
//        if (CommonUtils.isNotEmpty(email)) {
//            //Start of CommunicationValue - Email
//            writer.writeStartElement("CommunicationValue");
//            writer.writeAttribute("CommunicationType", "Email");
//            writer.writeCharacters(email);
//            writer.writeEndElement();
//            //End of CommunicationValue - Email
//        }
//
//        writer.writeEndElement();
//        //End of ContactInformation
//
//        if (null != terminal) {
//            List<String> addresses = new ArrayList<String>();
//            if (CommonUtils.isNotEmpty(terminal.getAddres1())) {
//                addresses.add(terminal.getAddres1().replaceAll("\\p{Cntrl}", ""));
//            }
//            if (CommonUtils.isNotEmpty(terminal.getAddres2())) {
//                addresses.add(terminal.getAddres2().replaceAll("\\p{Cntrl}", ""));
//            }
//            if (CommonUtils.isAtLeastOneNotEmpty(terminal.getCity1(), terminal.getState())) {
//                addresses.add(CommonUtils.concatenate(terminal.getCity1(), terminal.getState()).replaceAll("\\p{Cntrl}", ""));
//            }
//            if (null != terminal.getCountry() && CommonUtils.isNotEmpty(terminal.getCountry().getCodedesc())) {
//                addresses.add(CommonUtils.concatenate(terminal.getCountry().getCodedesc(), terminal.getZipcde()).replaceAll("\\p{Cntrl}", ""));
//            }
//            if (CommonUtils.isNotEmpty(addresses)) {
//                //Start of AddressInformation
//                writer.writeStartElement("AddressInformation");
//
//                for (String address : addresses) {
//                    //Start of AddressLine
//                    writer.writeStartElement("AddressLine");
//                    writer.writeCharacters(address);
//                    writer.writeEndElement();
//                    //End of AddressLine
//                }
//
//                writer.writeEndElement();
//                //End of AddressInformation
//            }
//        } else {
//            warnings.append("--> Please Enter Issuing Terminal<br/>");
//        }
//
//        //Start of DocumentationRequirements
//        writer.writeStartElement("DocumentationRequirements");
//
//        //Start of Documents
//        writer.writeStartElement("Documents");
//        writer.writeAttribute("DocumentType", "SeaWaybill");
//        writer.writeAttribute("Freighted", "True");
//        writer.writeEndElement();
//        //End of Documents
//
//        //Start of Quantity
//        writer.writeStartElement("Quantity");
//        writer.writeCharacters("1");
//        writer.writeEndElement();
//        //End of Quantity
//
//        writer.writeEndElement();
//        //End of DocumentationRequirements
//
//        writer.writeEndElement();
//        //End of PartnerInformation - Requestor
//
//        writer.writeEndElement();
//        //End of Parties
//
//        writer.writeEndElement();
//        //End of MessageProperties
//
//        //Start of MessageDetails
//        writer.writeStartElement("MessageDetails");
//
//        //Start of EquipmentDetails
//        writer.writeStartElement("EquipmentDetails");
//
//        //Start of LineNumber
//        writer.writeStartElement("LineNumber");
//        writer.writeCharacters("1");
//        writer.writeEndElement();
//        //End of LineNumber
//
//        //Start of EquipmentIdentifier
//        writer.writeStartElement("EquipmentIdentifier");
//        writer.writeAttribute("EquipmentSupplier", "Carrier");
//        writer.writeCharacters(lclUnit.getUnitNo());
//        writer.writeEndElement();
//        //End of EquipmentIdentifier
//
//        String equipmentTypeCode = "45G0";//need to retrivegenericCode.getField1();
//        //Start of EquipmentType
//        writer.writeStartElement("EquipmentType");
//
//        //Start of EquipmentTypeCode
//        writer.writeStartElement("EquipmentTypeCode");
//        writer.writeCharacters(equipmentTypeCode);
//        writer.writeEndElement();
//        //End of EquipmentTypeCode
//
//        writer.writeEndElement();
//        //End of EquipmentType
//
//        //Start of EquipmentGrossVolume
//        writer.writeStartElement("EquipmentSeal");
//        writer.writeAttribute("SealingParty", "Carrier");
//        writer.writeCharacters("928341");
//        writer.writeEndElement();
//        //End of EquipmentGrossVolume
//
//        writer.writeEndElement();
//        //End of EquipmentDetails
//        List<String> hazmatComments = new ArrayList<String>();
//        List<LclBookingHazmat> lclBookingHazmatList = new ArrayList<LclBookingHazmat>();
//        for (LclBooking lclBkg : lclBookingList) {
//            LclFileNumber lclFileNumber = lclBkg.getLclFileNumber();
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
//        if (CommonUtils.isNotEmpty(hazmatComments)) {
//            for (String description : hazmatComments) {
//                //Start of hazmat DescriptionLine
//                writer.writeStartElement("PackageDetailComments");
//                writer.writeAttribute("CommentType", "GoodsDescription");
//                writer.writeCharacters(description);
//                writer.writeEndElement();
//                //End of hazmat DescriptionLine
//            }
//        }
//        writer.writeEndElement();
//        //End of MessageDetails
//
//        writer.writeEndElement();
//        //End of MessageBody
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
//        new EdiTrackingBC().setEdiLog(fileName.toString(), dateTimeSeconds, "failure", errors.toString(), "I", "304", fileNumber, bookingNumber, "", null);
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
//    public String create(List<LclBooking> lclBookingList, LclUnit lclUnit, LclBooking lclBooking, HttpServletRequest request) throws Exception {
//        try {
//            init(lclBooking);
//            write(lclBookingList, lclUnit, lclBooking, request);
//            if (CommonUtils.isNotEmpty(errors)) {
//                deleteFile();
//                return "<span color: #000080;font-size: 10px;>Error Message</span><br/>" + errors.toString();
//            } else {
//                return "XML generated successfully";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
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
