package com.logiware.edi.xml;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
import com.gp.cong.logisoft.domain.CarriersOrLine;
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.edi.util.LogFileWriter;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.struts.LoadEdiProperties;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclAESDetails;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Lakshmi Narayanan
 */
public class InttraXmlCreator {

    private static final String ENCODING = "ISO-8859-1";
    private OutputStream outputStream = null;
    private XMLStreamWriter writer = null;
    private File file = null;
    private StringBuilder fileName = new StringBuilder();
    private String fullDate = null;
    private String dateTime = null;
    private String dateTimeSeconds = null;
    private String bookingNumber = "";
    private String documentIdentifier = null;
    private String referenceNumber = null;
    private final String contNoregex = "[^A-Za-z0-9]";
    private int docVersion = 0;
    private StringBuilder errors = new StringBuilder();
    private StringBuilder warnings = new StringBuilder();

    private void init(String fileNumber) throws Exception {
        //Date Time values
        Date date = new Date();
        fullDate = DateUtils.formatDate(date, "yyyyMMdd");
        dateTime = DateUtils.formatDate(date, "yyMMddHHmm");
        dateTimeSeconds = DateUtils.formatDate(date, "yyyyMMddHHmmss");
        String companyName = LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName").toUpperCase();

        referenceNumber = "04-" + fileNumber;
        documentIdentifier = "04" + fileNumber;

        String folderName = LoadEdiProperties.getProperty(CommonUtils.isLinux() ? "linuxInttraXmlOut" : "inttraXmlOut");
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        fileName.append("304_").append(companyName).append("_INTTRA_").append(documentIdentifier).append("_").append(dateTimeSeconds).append(".xml");
        docVersion = new EdiDAO().getDocVersion(fileNumber, "304");

        file = new File(folder, fileName.toString());
        outputStream = new FileOutputStream(file);
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        writer = outputFactory.createXMLStreamWriter(outputStream, ENCODING);
    }

    public void write(String fileNumber, String action, HttpServletRequest request) throws Exception {
        String companyCode = LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase();
        String partnerName = LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName").toUpperCase();
        String excludeCharcter = LoadLogisoftProperties.getProperty("edi.exclude.character");
        EdiDAO ediDAO = new EdiDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        HazmatMaterialDAO hazmatMaterialDAO = new HazmatMaterialDAO();
        FclInbondDetailsDAO inbondDetailsDAO = new FclInbondDetailsDAO();
        User user = (User) request.getSession().getAttribute("loginuser");

        //Start of Document
        writer.writeStartDocument(ENCODING, "1.0");

        //Start of Message
        writer.writeStartElement("Message");

        //Start of Header
        writer.writeStartElement("Header");

        //Start of MessageType
        writer.writeStartElement("MessageType");
        writer.writeAttribute("MessageVersion", "1.0");
        writer.writeCharacters("ShippingInstruction");
        writer.writeEndElement();
	//End of MessageType

        //Start of DocumentIdentifier
        writer.writeStartElement("DocumentIdentifier");
        writer.writeCharacters(documentIdentifier);
        writer.writeEndElement();
	//End of DocumentIdentifier

        //Start of DateTime - Document
        writer.writeStartElement("DateTime");
        writer.writeAttribute("DateType", "Document");
        writer.writeCharacters(dateTime);
        writer.writeEndElement();
	//End of DateTime - Document

        //Start of Parties
        writer.writeStartElement("Parties");

        //Start of PartnerInformation - Sender
        writer.writeStartElement("PartnerInformation");
        writer.writeAttribute("PartnerRole", "Sender");

        //Start of PartnerIdentifier
        writer.writeStartElement("PartnerIdentifier");
        writer.writeAttribute("Agency", "AssignedBySender");
        writer.writeCharacters(companyCode);
        writer.writeEndElement();
        //End of PartnerIdentifier

        writer.writeEndElement();
	//End of PartnerInformation - Sender

        //Start of PartnerInformation - Recipient
        writer.writeStartElement("PartnerInformation");
        writer.writeAttribute("PartnerRole", "Recipient");

        //Start of PartnerIdentifier
        writer.writeStartElement("PartnerIdentifier");
        writer.writeAttribute("Agency", "AssignedByRecipient");
        writer.writeCharacters("INTTRA");
        writer.writeEndElement();
        //End of PartnerIdentifier

        writer.writeEndElement();
        //End of PartnerInformation - Recipient

        writer.writeEndElement();
        //End of Parties

        writer.writeEndElement();
	//End of Header

        //Start of MessageBody
        writer.writeStartElement("MessageBody");

        //Start of MessageProperties
        writer.writeStartElement("MessageProperties");

        //Start of ShipmentID
        writer.writeStartElement("ShipmentID");

        //Start of ShipmentIdentifier
        writer.writeStartElement("ShipmentIdentifier");
        String messageStatus = docVersion > 1 ? "Amendment" : "Original";
        writer.writeAttribute("MessageStatus", messageStatus);
//        String shipmentId = documentIdentifier + "_" + dateTimeSeconds;
        String shipmentId = documentIdentifier;
        writer.writeCharacters(shipmentId);
        writer.writeEndElement();
	//End of ShipmentIdentifier

        //Start of DocumentVersion
        writer.writeStartElement("DocumentVersion");
        String documentVersion = StringUtils.leftPad("" + docVersion, 6, "0");
        writer.writeCharacters(documentVersion);
        writer.writeEndElement();
        //End of DocumentVersion

        writer.writeEndElement();
	//End of ShipmentID

        //Start of DateTime - Message
        writer.writeStartElement("DateTime");
        writer.writeAttribute("DateType", "Message");
        writer.writeCharacters(fullDate);
        writer.writeEndElement();
        //End of DateTime - Message

        FclBl bl = new FclBlDAO().getOriginalBl(fileNumber);
        if (CommonUtils.isNotEmpty(bl.getStreamShipBl()) && CommonUtils.isNotEmpty(bl.getDestinationChargesPreCol())) {
            //Start of ChargeCategory
            writer.writeStartElement("ChargeCategory");
            String prepaidorCollectIndicator = StringUtils.substring(bl.getStreamShipBl(), 2);
            writer.writeAttribute("PrepaidorCollectIndicator", prepaidorCollectIndicator);
            String chargeType = CommonUtils.isEqualIgnoreCase(bl.getStreamShipBl(), bl.getDestinationChargesPreCol()) ? "AllCharges" : "BasicFreight";
            writer.writeAttribute("ChargeType", chargeType);
            writer.writeEndElement();
            //End of ChargeCategory

            if (CommonUtils.isEqualIgnoreCase(chargeType, "BasicFreight")) {
                //Start of ChargeCategory - AdditionalCharges
                writer.writeStartElement("ChargeCategory");
                prepaidorCollectIndicator = CommonUtils.isEqualIgnoreCase(prepaidorCollectIndicator, "Prepaid") ? "Collect" : "Prepaid";
                writer.writeAttribute("PrepaidorCollectIndicator", prepaidorCollectIndicator);
                writer.writeAttribute("ChargeType", "AdditionalCharges");
                writer.writeEndElement();
                //End of ChargeCategory - AdditionalCharges
            }
        } else {
            if (CommonUtils.isEmpty(bl.getStreamShipBl()) && CommonUtils.isNotEqualIgnoreCase(bl.getImportFlag(), "I")) {
                errors.append("Please choose SSL BL Prepaid/Collect");
            }
            if (CommonUtils.isEmpty(bl.getDestinationChargesPreCol())) {
                errors.append("Please choose SSL BL Destination Charges");
            }
        }

        if (CommonUtils.isNotEmpty(bl.getBookingNo())) {
            bookingNumber = bl.getBookingNo();
            //Start of ReferenceInformation - BookingNumber
            writer.writeStartElement("ReferenceInformation");
            writer.writeAttribute("ReferenceType", "BookingNumber");
            writer.writeCharacters(bl.getBookingNo().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of ReferenceInformation - BookingNumber
        } else {
            errors.append("--> Booking Number is invalid<br/>");
        }

        CarriersOrLine carriersOrLine = null;
        if (CommonUtils.isNotEmpty(bl.getSslineNo())) {
            carriersOrLine = new CarriersOrLineDAO().getScacAndContractNumber(bl.getSslineNo());
        }

        if (null != carriersOrLine && CommonUtils.isNotEmpty(carriersOrLine.getFclContactNumber())) {
            //Start of ReferenceInformation - ContractNumber
            writer.writeStartElement("ReferenceInformation");
            writer.writeAttribute("ReferenceType", "ContractNumber");
            writer.writeCharacters(carriersOrLine.getFclContactNumber().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of ReferenceInformation - ContractNumber
        }

        //Start of ReferenceInformation - ExportersReferenceNumber
        writer.writeStartElement("ReferenceInformation");
        writer.writeAttribute("ReferenceType", "ExportersReferenceNumber");
        writer.writeCharacters(companyCode + " Ref " + referenceNumber);
        writer.writeEndElement();
        //End of ReferenceInformation - ExportersReferenceNumber

        if (CommonUtils.isNotEmpty(bl.getExportReference())) {
            if (bl.getExportReference().length() > 30) {
                errors.append("--> Export Reference length must be less than 30 characters<br/>");
            } else {
                for (String exportersReferenceNumber : CommonUtils.splitString(bl.getExportReference())) {
                    //Start of ReferenceInformation - ExportersReferenceNumber
                    writer.writeStartElement("ReferenceInformation");
                    writer.writeAttribute("ReferenceType", "ExportersReferenceNumber");
                    writer.writeCharacters(exportersReferenceNumber.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                    writer.writeEndElement();
                    //End of ReferenceInformation - ExportersReferenceNumber
                }
            }
        }

        List<FclInbondDetails> inbondDetailses = inbondDetailsDAO.findByProperty("bolId", bl.getBol());
        if (CommonUtils.isNotEmpty(inbondDetailses)) {
            for (FclInbondDetails inbondDetails : inbondDetailses) {
                if (CommonUtils.isNotEmpty(inbondDetails.getInbondNumber())) {
                    //Start of ReferenceInformation - ExportersReferenceNumber
                    writer.writeStartElement("ReferenceInformation");
                    writer.writeAttribute("ReferenceType", "ExportersReferenceNumber");
                    writer.writeCharacters(inbondDetails.getInbondNumber().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                    writer.writeEndElement();
                    //End of ReferenceInformation - ExportersReferenceNumber
                }
            }
        }

        List<FclAESDetails> aesDetailses = inbondDetailsDAO.findAesdetails("fileNo", bl.getFileNo());
        if (CommonUtils.isNotEmpty(aesDetailses)) {
            for (FclAESDetails aesDetails : aesDetailses) {
                if (CommonUtils.isNotEmpty(aesDetails.getAesDetails())) {
                    //Start of ReferenceInformation - TransactionReferenceNumber
                    writer.writeStartElement("ReferenceInformation");
                    writer.writeAttribute("ReferenceType", "TransactionReferenceNumber");
                    writer.writeCharacters(aesDetails.getAesDetails().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                    writer.writeEndElement();
                    //End of ReferenceInformation - TransactionReferenceNumber
                }
                if (CommonUtils.isNotEmpty(aesDetails.getException())) {
                    //Start of ReferenceInformation - TransactionReferenceNumber
                    writer.writeStartElement("ReferenceInformation");
                    writer.writeAttribute("ReferenceType", "TransactionReferenceNumber");
                    writer.writeCharacters(aesDetails.getException().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                    writer.writeEndElement();
                    //End of ReferenceInformation - TransactionReferenceNumber
                }
            }
        } else if (CommonUtils.isNotEqualIgnoreCase(bl.getImportFlag(), "I")) {
            warnings.append("--> Please Enter AES/ITN details<br/>");
        }
        String routingInstructions = null;
        if (CommonUtils.isNotEmpty(bl.getFclBLClause())) {
            routingInstructions = ediDAO.getRoutingInstruction(bl.getFclBLClause());
        }

        if (CommonUtils.isNotEmpty(bl.getDestinationChargesPreCol()) || CommonUtils.isNotEmpty(routingInstructions)) {
            //Start of Instructions
            writer.writeStartElement("Instructions");

            if (CommonUtils.isNotEmpty(bl.getDestinationChargesPreCol())) {
                //Start of ShipmentComments - General
                writer.writeStartElement("ShipmentComments");
                writer.writeAttribute("CommentType", "General");
                writer.writeCharacters("All Destination Charges " + StringUtils.substring(bl.getDestinationChargesPreCol().replaceAll("\\p{Cntrl}", "").replace("\\r", ""), 2));
                writer.writeEndElement();
                //End of ShipmentComments - General
            }

            if (CommonUtils.isNotEmpty(routingInstructions)) {
                //Start of ShipmentComments - RoutingInstructions
                writer.writeStartElement("ShipmentComments");
                writer.writeAttribute("CommentType", "RoutingInstructions");
                writer.writeCharacters(routingInstructions.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                writer.writeEndElement();
                //End of ShipmentComments - RoutingInstructions
            }

            writer.writeEndElement();
            //End of Instructions
        }
        String movementType = null;
        if (CommonUtils.isNotEqualIgnoreEmpty(bl.getLineMove(), "00")) {
            movementType = ediDAO.getMoveType(bl.getLineMove(), "field4");
            //Start of HaulageDetails
            writer.writeStartElement("HaulageDetails");
            writer.writeAttribute("MovementType", movementType);
            writer.writeAttribute("ServiceType", "FullLoad");
            writer.writeEndElement();
            //End of HaulageDetails
        } else {
            warnings.append("--> Please Select LineMove<br/>");
        }
        //Start of TransportationDetails
        writer.writeStartElement("TransportationDetails");
        writer.writeAttribute("TransportStage", "Main");
        writer.writeAttribute("TransportMode", "Maritime");

        //Start of ConveyanceInformation
        writer.writeStartElement("ConveyanceInformation");

        if (null != bl.getVessel() && CommonUtils.isNotEmpty(bl.getVessel().getCodedesc())) {
            //Start of ConveyanceName
            writer.writeStartElement("ConveyanceName");
            writer.writeCharacters(bl.getVessel().getCodedesc().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of ConveyanceName
        } else {
            warnings.append("--> Please Enter Vessel Name<br/>");
        }

        if (CommonUtils.isNotEmpty(bl.getVoyages())) {
            //Start of VoyageTripNumber
            writer.writeStartElement("VoyageTripNumber");
            writer.writeCharacters(bl.getVoyages().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of VoyageTripNumber
        } else {
            warnings.append("--> Please Enter Voyage Number<br/>");
        }

        if (null != carriersOrLine && CommonUtils.isNotEmpty(carriersOrLine.getSCAC())) {
            if (carriersOrLine.getSCAC().length() >= 2 && carriersOrLine.getSCAC().length() <= 4) {
                //Start of CarrierSCAC
                writer.writeStartElement("CarrierSCAC");
                writer.writeCharacters(carriersOrLine.getSCAC().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                writer.writeEndElement();
                //End of CarrierSCAC
            } else {
                errors.append("--> Carrier Scac Code(SSLINE) length must be between 2 & 4<br/>");
            }
        } else {
            errors.append("--> Carrier Scac Code(SSLINE) is not matching<br/>");
        }

        writer.writeEndElement();
        //End of ConveyanceInformation

        String placeOfReceiptCode = null;
        String placeOfReceiptName = null;
        if (StringUtils.contains(bl.getTerminal(), "/")) {
            String terminal = bl.getTerminal();
            placeOfReceiptCode = StringUtils.substring(terminal, StringUtils.indexOf(terminal, "(") + 1, StringUtils.indexOf(terminal, ")"));
            placeOfReceiptName = StringUtils.substring(StringUtils.substring(terminal, 0, StringUtils.indexOf(terminal, "/")), 0, 24);
        }

        if (CommonUtils.isNotEmpty(placeOfReceiptCode)) {
            //Start of Location - PlaceOfReceipt
            writer.writeStartElement("Location");
            writer.writeAttribute("LocationType", "PlaceOfReceipt");

            //Start of LocationCode
            writer.writeStartElement("LocationCode");
            writer.writeAttribute("Agency", "UN");
            writer.writeCharacters(placeOfReceiptCode.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
	    //End of LocationCode

            //Start of LocationName
            writer.writeStartElement("LocationName");
            writer.writeCharacters(placeOfReceiptName.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of LocationName

            writer.writeEndElement();
            //End of Location - PlaceOfReceipt
        } else if (CommonUtils.in(movementType, "DOOR TO DOOR", "DOOR TO PORT", "DOOR TO RAIL")) {
            warnings.append("--> Please Enter Place Of Receipt<br/>");
        }

        String portOfLoadingCode = null;
        String portOfLoadingName = null;
        if (StringUtils.contains(bl.getPortOfLoading(), "/")) {
            String pol = bl.getPortOfLoading();
            portOfLoadingCode = StringUtils.substring(pol, StringUtils.indexOf(pol, "(") + 1, StringUtils.indexOf(pol, ")"));
            portOfLoadingName = StringUtils.substring(StringUtils.substring(pol, 0, StringUtils.indexOf(pol, "/")), 0, 24);
        }

        if (CommonUtils.isNotEmpty(portOfLoadingCode)) {
            //Start of Location - PortOfLoading
            writer.writeStartElement("Location");
            writer.writeAttribute("LocationType", "PortOfLoading");

            //Start of LocationCode
            writer.writeStartElement("LocationCode");
            writer.writeAttribute("Agency", "UN");
            writer.writeCharacters(portOfLoadingCode.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
	    //End of LocationCode

            //Start of LocationName
            writer.writeStartElement("LocationName");
            writer.writeCharacters(portOfLoadingName.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of LocationName

            writer.writeEndElement();
            //End of Location - PortOfLoading
        } else {
            warnings.append("--> Please Enter Port Of Loading<br/>");
        }

        String portOfDischargeCode = null;
        String portOfDischargeName = null;
        if (StringUtils.contains(bl.getPortofDischarge(), "/")) {
            String pod = bl.getPortofDischarge();
            int index = pod.lastIndexOf("(");
            if (index != -1) {
                portOfDischargeCode = StringUtils.substring(pod, StringUtils.lastIndexOf(pod, "(") + 1, StringUtils.lastIndexOf(pod, ")"));
            }
            portOfDischargeName = StringUtils.substring(StringUtils.substring(pod, 0, StringUtils.indexOf(pod, "/")), 0, 24);
        }

        if (CommonUtils.isNotEmpty(portOfDischargeCode)) {
            //Start of Location - PortOfDischarge
            writer.writeStartElement("Location");
            writer.writeAttribute("LocationType", "PortOfDischarge");

            //Start of LocationCode
            writer.writeStartElement("LocationCode");
            writer.writeAttribute("Agency", "UN");
            writer.writeCharacters(portOfDischargeCode.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
	    //End of LocationCode

            //Start of LocationName
            writer.writeStartElement("LocationName");
            writer.writeCharacters(portOfDischargeName.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of LocationName

            writer.writeEndElement();
            //End of Location - PortOfDischarge
        } else {
            warnings.append("--> Please Enter Port Of Discharge<br/>");
        }

        String placeOfDeliveryCode = null;
        String placeOfDeliveryName = null;
        if (StringUtils.contains(bl.getFinalDestination(), "/")) {
            String plod = bl.getFinalDestination();
            int index = plod.lastIndexOf("(");
            if (index != -1) {
                placeOfDeliveryCode = StringUtils.substring(plod, StringUtils.lastIndexOf(plod, "(") + 1, StringUtils.lastIndexOf(plod, ")"));
            }
            placeOfDeliveryName = StringUtils.substring(StringUtils.substring(plod, 0, StringUtils.indexOf(plod, "/")), 0, 24);
        }

        if (CommonUtils.isNotEmpty(placeOfDeliveryCode)) {
            //Start of Location - PlaceOfDelivery
            writer.writeStartElement("Location");
            writer.writeAttribute("LocationType", "PlaceOfDelivery");

            //Start of LocationCode
            writer.writeStartElement("LocationCode");
            writer.writeAttribute("Agency", "UN");
            writer.writeCharacters(placeOfDeliveryCode.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
	    //End of LocationCode

            //Start of LocationName
            writer.writeStartElement("LocationName");
            writer.writeCharacters(placeOfDeliveryName.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of LocationName

            writer.writeEndElement();
            //End of Location - PlaceOfDelivery
        } else if (CommonUtils.in(movementType, "DOOR TO DOOR", "DOOR TO PORT", "DOOR TO RAIL")) {
            warnings.append("--> Please Enter Place Of Delivery<br/>");
        }

        writer.writeEndElement();
	//End of TransportationDetails

        //Start of Parties
        writer.writeStartElement("Parties");

        if (CommonUtils.isNotEmpty(bl.getHouseShipperName())) {
            if (bl.getHouseShipperName().length() > 35) {
                errors.append("--> Master Shipper Name length must be less than 35 characters<br/>");
            } else {
                //Start of PartnerInformation - Shipper
                writer.writeStartElement("PartnerInformation");
                writer.writeAttribute("PartnerRole", "Shipper");

                //Start of PartnerName
                writer.writeStartElement("PartnerName");
                writer.writeCharacters(bl.getHouseShipperName().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                writer.writeEndElement();
                //End of PartnerName

                List<String> shipperAddresses = CommonUtils.splitString(bl.getHouseShipperAddress(), "\\n");
                if (CommonUtils.isNotEmpty(shipperAddresses)) {
                    //Start of AddressInformation
                    writer.writeStartElement("AddressInformation");
                    int count = 0;
                    for (String address : shipperAddresses) {
                        String addressLine = address.replaceAll("\\p{Cntrl}", "").replace("\\r", "").replace("|", " ");
                        if (CommonUtils.isNotEmpty(addressLine)) {
                            if (addressLine.length() > 35) {
                                errors.append("--> Master Shipper Address can not be greater than 35 characters per line<br/>");
                                break;
                            } else {
                                //Start of AddressLine
                                writer.writeStartElement("AddressLine");
                                writer.writeCharacters(addressLine);
                                writer.writeEndElement();
                                //End of AddressLine
                            }
                            count++;
                        }
                        if (count > 4) {
                            errors.append("--> Master Shipper Address can not be greater than 4 lines<br/>");
                            break;
                        }
                        if (CommonUtils.isExcludeEdiCharacter(addressLine, excludeCharcter)) {
                            errors.append("--> Please Remove the following Special Characters ").append(excludeCharcter).append(" from Master Shipper Address ");
                            break;
                        }
                    }
                    writer.writeEndElement();
                    //End of PartnerInformation - AddressInformation
                }

                writer.writeEndElement();
                //End of PartnerInformation - Shipper
            }
        } else {
            warnings.append("--> Please Enter Master Shipper Name<br/>");
        }

        if (null != carriersOrLine && CommonUtils.isNotEmpty(carriersOrLine.getSCAC())
                && carriersOrLine.getSCAC().length() >= 2 && carriersOrLine.getSCAC().length() <= 4) {
            //Start of PartnerInformation - Carrier
            writer.writeStartElement("PartnerInformation");
            writer.writeAttribute("PartnerRole", "Carrier");

            //Start of PartnerIdentifier
            writer.writeStartElement("PartnerIdentifier");
            writer.writeAttribute("Agency", "AssignedBySender");
            writer.writeCharacters(carriersOrLine.getSCAC().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of PartnerIdentifier

            writer.writeEndElement();
            //End of PartnerInformation - Carrier
        }

        if (CommonUtils.isNotEmpty(bl.getHouseConsigneeName())) {
            if (bl.getHouseConsigneeName().length() > 35) {
                errors.append("--> Master Consignee Name length must be less than 35 characters<br/>");
            } else {
                //Start of PartnerInformation - Consignee
                writer.writeStartElement("PartnerInformation");
                writer.writeAttribute("PartnerRole", "Consignee");

                //Start of PartnerName
                writer.writeStartElement("PartnerName");
                writer.writeCharacters(bl.getHouseConsigneeName().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                writer.writeEndElement();
                //End of PartnerName

                List<String> consigneeAddresses = CommonUtils.splitString(bl.getHouseConsigneeAddress(), "\\n");
                if (CommonUtils.isNotEmpty(consigneeAddresses)) {
                    //Start of AddressInformation
                    writer.writeStartElement("AddressInformation");
                    int count = 0;
                    for (String address : consigneeAddresses) {
                        String addressLine = address.replaceAll("\\p{Cntrl}", "").replace("\\r", "").replace("|", " ");
                        if (CommonUtils.isNotEmpty(addressLine)) {
                            if (addressLine.length() > 35) {
                                errors.append("--> Master Consignee Address can not be greater than 35 characters per line<br/>");
                                break;
                            } else {
                                //Start of AddressLine
                                writer.writeStartElement("AddressLine");
                                writer.writeCharacters(addressLine);
                                writer.writeEndElement();
                                //End of AddressLine
                            }
                            count++;
                        }
                        if (count > 4) {
                            errors.append("--> Master Consignee Address can not be greater than 4 lines<br/>");
                            break;
                        }
                        if (CommonUtils.isExcludeEdiCharacter(addressLine, excludeCharcter)) {
                            errors.append("--> Please Remove the following Special Characters ").append(excludeCharcter).append(" from Master Consignee Address ");
                            break;
                        }
                    }
                    writer.writeEndElement();
                    //End of PartnerInformation - AddressInformation
                }

                writer.writeEndElement();
                //End of PartnerInformation - Consignee
            }
        } else {
            warnings.append("--> Please Enter Master Consignee Name<br/>");
        }

        if (CommonUtils.isNotEmpty(bl.getHouseNotifyPartyName())) {
            if (bl.getHouseNotifyPartyName().length() > 35) {
                errors.append("--> Master Notify Party Name length must be less than 35 characters<br/>");
            } else {
                //Start of PartnerInformation - NotifyParty
                writer.writeStartElement("PartnerInformation");
                writer.writeAttribute("PartnerRole", "NotifyParty");

                //Start of PartnerName
                writer.writeStartElement("PartnerName");
                writer.writeCharacters(bl.getHouseNotifyPartyName().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                writer.writeEndElement();
                //End of PartnerName

                List<String> notifyPartyAddresses = CommonUtils.splitString(bl.getHouseNotifyParty(), "\\n");
                if (CommonUtils.isNotEmpty(notifyPartyAddresses)) {
                    //Start of AddressInformation
                    writer.writeStartElement("AddressInformation");
                    int count = 0;
                    for (String address : notifyPartyAddresses) {
                        String addressLine = address.replaceAll("\\p{Cntrl}", "").replace("\\r", "").replace("|", " ");
                        if (CommonUtils.isNotEmpty(addressLine)) {
                            if (addressLine.length() > 35) {
                                errors.append("--> Master Notify Party Address can not be greater than 35 characters per line<br/>");
                                break;
                            } else {
                                //Start of AddressLine
                                writer.writeStartElement("AddressLine");
                                writer.writeCharacters(addressLine);
                                writer.writeEndElement();
                                //End of AddressLine
                            }
                            count++;
                        }
                        if (count > 4) {
                            errors.append("--> Master Notify Party Address can not be greater than 4 lines<br/>");
                            break;
                        }
                        if (CommonUtils.isExcludeEdiCharacter(addressLine, excludeCharcter)) {
                            errors.append("-->Please Remove the following Special Characters ").append(excludeCharcter).append(" from Master Notify Address ");
                            break;
                        }
                    }
                    writer.writeEndElement();
                    //End of PartnerInformation - AddressInformation
                }

                writer.writeEndElement();
                //End of PartnerInformation - NotifyParty
            }
        }

        //Start of PartnerInformation - Requestor
        writer.writeStartElement("PartnerInformation");
        writer.writeAttribute("PartnerRole", "Requestor");

        //Start of PartnerIdentifier
        writer.writeStartElement("PartnerIdentifier");
        writer.writeAttribute("Agency", "AssignedBySender");
        writer.writeCharacters(companyCode);
        writer.writeEndElement();
	//End of PartnerIdentifier

        //Start of PartnerName
        writer.writeStartElement("PartnerName");
        writer.writeCharacters(partnerName);
        writer.writeEndElement();
	//End of PartnerName

        //Start of ContactInformation
        writer.writeStartElement("ContactInformation");

        //Start of ContactName
        writer.writeStartElement("ContactName");
        writer.writeAttribute("ContactType", "Informational");
        writer.writeCharacters(user.getFirstName().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
        writer.writeEndElement();
        //End of ContactName

        if (CommonUtils.isNotEmpty(user.getTelephone())) {
            //Start of CommunicationValue - Telephone
            writer.writeStartElement("CommunicationValue");
            writer.writeAttribute("CommunicationType", "Telephone");
            writer.writeCharacters(user.getTelephone().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of CommunicationValue - Telephone
        }

        if (CommonUtils.isNotEmpty(user.getFax())) {
            //Start of CommunicationValue - Fax
            writer.writeStartElement("CommunicationValue");
            writer.writeAttribute("CommunicationType", "Fax");
            writer.writeCharacters(user.getFax().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of CommunicationValue - Fax
        }

        String email = user.getEmail();

        RefTerminal terminal = null;
        if (CommonUtils.isNotEmpty(bl.getBillingTerminal())) {
            String terminalNumber = StringUtils.substring(bl.getBillingTerminal(), StringUtils.lastIndexOf(bl.getBillingTerminal(), "-") + 1);
            terminal = new RefTerminalDAO().findById(terminalNumber);
            if (null != terminal) {
                String terminalEmail = ediDAO.getUserEmail(terminalNumber);
                if (CommonUtils.isEmpty(email) && CommonUtils.isNotEmpty(terminalEmail)) {
                    email = terminalEmail;
                }
            }
        }

        if (CommonUtils.isNotEmpty(email)) {
            //Start of CommunicationValue - Email
            writer.writeStartElement("CommunicationValue");
            writer.writeAttribute("CommunicationType", "Email");
            writer.writeCharacters(email.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            writer.writeEndElement();
            //End of CommunicationValue - Email
        }

        writer.writeEndElement();
        //End of ContactInformation

        if (null != terminal) {
            List<String> addresses = new ArrayList<String>();
            if (CommonUtils.isNotEmpty(terminal.getAddres1())) {
                addresses.add(terminal.getAddres1().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            }
            if (CommonUtils.isNotEmpty(terminal.getAddres2())) {
                addresses.add(terminal.getAddres2().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            }
            if (CommonUtils.isAtLeastOneNotEmpty(terminal.getCity1(), terminal.getState())) {
                addresses.add(CommonUtils.concatenate(terminal.getCity1(), terminal.getState()).replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            }
            if (null != terminal.getCountry() && CommonUtils.isNotEmpty(terminal.getCountry().getCodedesc())) {
                addresses.add(CommonUtils.concatenate(terminal.getCountry().getCodedesc(), terminal.getZipcde()).replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
            }
            if (CommonUtils.isNotEmpty(addresses)) {
                //Start of AddressInformation
                writer.writeStartElement("AddressInformation");

                for (String address : addresses) {
                    //Start of AddressLine
                    writer.writeStartElement("AddressLine");
                    writer.writeCharacters(address);
                    writer.writeEndElement();
                    //End of AddressLine
                }

                writer.writeEndElement();
                //End of AddressInformation
            }
        } else {
            warnings.append("--> Please Enter Issuing Terminal<br/>");
        }

        //Start of DocumentationRequirements
        writer.writeStartElement("DocumentationRequirements");

        //Start of Documents
        writer.writeStartElement("Documents");
        if (CommonUtils.isNotEmpty(bl.getFclBLClause()) && bl.getFclBLClause().equals("2")) {
            writer.writeAttribute("DocumentType", "BillOfLadingOriginal");
        } else {
            writer.writeAttribute("DocumentType", "SeaWaybill");
        }
        writer.writeAttribute("Freighted", "True");
        writer.writeEndElement();
	//End of Documents

        //Start of Quantity
        writer.writeStartElement("Quantity");
        writer.writeCharacters("1");
        writer.writeEndElement();
        //End of Quantity

        writer.writeEndElement();
        //End of DocumentationRequirements

        writer.writeEndElement();
        //End of PartnerInformation - Requestor

        writer.writeEndElement();
        //End of Parties

        writer.writeEndElement();
	//End of MessageProperties

        //Start of MessageDetails
        writer.writeStartElement("MessageDetails");

        List<FclBlContainer> containers = new FclBlContainerDAO().getAllContainers(bl.getBol().toString());
        if (CommonUtils.isNotEmpty(containers)) {
            int lineNumber = 0;
            List<FclBlContainer> validContainers = new ArrayList<FclBlContainer>();
            for (FclBlContainer container : containers) {
                if (CommonUtils.isNotEqualIgnoreCase(container.getDisabledFlag(), "D")) {
                    if (CommonUtils.isNotEmpty(container.getTrailerNo())) {
                        String containerNumber = container.getTrailerNo().replaceAll("[^A-Za-z0-9]", "");
                        if (containerNumber.length() >= 11) {
                            lineNumber++;
                            validContainers.add(container);
                            //Start of EquipmentDetails
                            writer.writeStartElement("EquipmentDetails");

                            //Start of LineNumber
                            writer.writeStartElement("LineNumber");
                            writer.writeCharacters(String.valueOf(lineNumber));
                            writer.writeEndElement();
			    //End of LineNumber

                            //Start of EquipmentIdentifier
                            writer.writeStartElement("EquipmentIdentifier");
                            writer.writeAttribute("EquipmentSupplier", "Carrier");
                            writer.writeCharacters(containerNumber.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                            writer.writeEndElement();
                            //End of EquipmentIdentifier

                            String equipmentTypeCode = container.getSizeLegend().getField1();
                            //Start of EquipmentType
                            writer.writeStartElement("EquipmentType");

                            //Start of EquipmentTypeCode
                            writer.writeStartElement("EquipmentTypeCode");
                            writer.writeCharacters(equipmentTypeCode.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                            writer.writeEndElement();
                            //End of EquipmentTypeCode

                            writer.writeEndElement();
                            //End of EquipmentType

                            if (CommonUtils.isNotEmpty(container.getSealNo())) {
                                //Start of EquipmentGrossVolume
                                writer.writeStartElement("EquipmentSeal");
                                writer.writeAttribute("SealingParty", "Carrier");
                                writer.writeCharacters(container.getSealNo().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                                writer.writeEndElement();
                                //End of EquipmentGrossVolume
                            } else {
                                warnings.append("--> Please Enter Seal Number for container ").append(container.getTrailerNo()).append("<br/>");
                            }

                            writer.writeEndElement();
                            //End of EquipmentDetails
                        } else {
                            warnings.append("--> Container Number ").append(container.getTrailerNo()).append(" is not valid<br/>");
                        }
                    } else {
                        warnings.append("--> Please Enter Unit Number for all containers<br>");
                    }
                }
            }
            if (CommonUtils.isNotEmpty(validContainers)) {
                lineNumber = 0;
                for (FclBlContainer container : validContainers) {
                    String containerNumber = container.getTrailerNo().replaceAll("[^A-Za-z0-9]", "");
                    List<String> hazmatComments = new ArrayList<String>();
                    List<FclBlMarks> containerPackages = ediDAO.findFclBlMarks(container.getTrailerNoId());
                    List<HazmatMaterial> hazmatMaterials = hazmatMaterialDAO.findbydoctypeid("Fclbl", container.getTrailerNoId().toString());
                    for (HazmatMaterial hazmatMaterial : hazmatMaterials) {
                        if (CommonUtils.isEqualIgnoreCase(hazmatMaterial.getFreeFormat(), "Y")) {
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getLine1())) {
                                hazmatComments.add(hazmatMaterial.getLine1());
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getLine2())) {
                                hazmatComments.add(hazmatMaterial.getLine2());
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getLine3())) {
                                hazmatComments.add(hazmatMaterial.getLine3());
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getLine4())) {
                                hazmatComments.add(hazmatMaterial.getLine4());
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getLine5())) {
                                hazmatComments.add(hazmatMaterial.getLine5());
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getLine6())) {
                                hazmatComments.add(hazmatMaterial.getLine6());
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getLine7())) {
                                hazmatComments.add(hazmatMaterial.getLine7());
                            }
                        } else {
                            StringBuilder comments = new StringBuilder();
                            comments.append(CommonUtils.isEqualIgnoreCase(hazmatMaterial.getReportableQuantity(), "Y") ? "REPORTABLE QUANTITY, " : "");
                            comments.append(CommonUtils.isNotEmpty(hazmatMaterial.getUnNumber()) ? "UN " + hazmatMaterial.getUnNumber() : "");
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getPropShipingNumber())) {
                                comments.append(", ").append(hazmatMaterial.getPropShipingNumber());
                                if (CommonUtils.isNotEmpty(hazmatMaterial.getTechnicalName())) {
                                    comments.append(", (").append(hazmatMaterial.getTechnicalName()).append(")");
                                }
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getImoClssCode())) {
                                comments.append(", CLASS ").append(hazmatMaterial.getImoClssCode());
                                if (CommonUtils.isNotEmpty(hazmatMaterial.getImoSubsidiaryClassCode())) {
                                    comments.append(" (").append(hazmatMaterial.getImoSubsidiaryClassCode()).append(")");
                                }
                                if (CommonUtils.isNotEmpty(hazmatMaterial.getImoSecondarySubClass())) {
                                    comments.append(" (").append(hazmatMaterial.getImoSecondarySubClass()).append(")");
                                }
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getPackingGroupCode())) {
                                comments.append(", PG ").append(hazmatMaterial.getPackingGroupCode());
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getFlashPointUMO())) {
                                comments.append(", FLASH POINT (").append(hazmatMaterial.getFlashPointUMO()).append(")");
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getOuterPackingPieces())) {
                                comments.append(", ").append(hazmatMaterial.getOuterPackingPieces());
                                if (CommonUtils.isNotEmpty(hazmatMaterial.getOuterPackComposition())) {
                                    comments.append(" ").append(hazmatMaterial.getOuterPackComposition());
                                }
                                if (CommonUtils.isNotEmpty(hazmatMaterial.getOuterPackagingType())) {
                                    comments.append(" ").append(hazmatMaterial.getOuterPackagingType());
                                }
                                if (CommonUtils.isNotEmpty(hazmatMaterial.getInnerPackingPieces())) {
                                    comments.append(", ").append(hazmatMaterial.getInnerPackingPieces());
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getInnerPackComposition())) {
                                        comments.append(" ").append(hazmatMaterial.getInnerPackComposition());
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getInnerPackagingType())) {
                                        comments.append(" ").append(hazmatMaterial.getInnerPackagingType());
                                    }
                                }
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getNetWeight())) {
                                comments.append(" @ ").append(NumberUtils.formatNumber(hazmatMaterial.getNetWeight(), "0.00"));
                                comments.append(" ").append(hazmatMaterial.getNetWeightUMO()).append(" EACH");
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getGrossWeight())) {
                                comments.append(", TOTAL GROSS WT ");
                                comments.append(NumberUtils.formatNumber(hazmatMaterial.getGrossWeight(), "0.00")).append(" KGS");
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getTotalNetWeight())) {
                                comments.append(", TOTAL NET WT ");
                                comments.append(NumberUtils.formatNumber(hazmatMaterial.getTotalNetWeight(), "0.00")).append(" KGS");
                            }
                            if (CommonUtils.isNotEmpty(hazmatMaterial.getVolume())) {
                                comments.append(NumberUtils.formatNumber(hazmatMaterial.getVolume(), "0.00"));
                                comments.append(hazmatMaterial.getVolume() > 1d ? " LITERS" : " LITER");
                            }
                            comments.append(CommonUtils.isEqualIgnoreCase(hazmatMaterial.getMarinePollutant(), "Y") ? ", MARINE POLLUTANT" : "");
                            comments.append(CommonUtils.isEqualIgnoreCase(hazmatMaterial.getExceptedQuantity(), "Y") ? ", EXCEPTED QUANTITY" : "");
                            comments.append(CommonUtils.isEqualIgnoreCase(hazmatMaterial.getLimitedQuantity(), "Y") ? ", LIMITED QUANTITY" : "");
                            comments.append(CommonUtils.isEqualIgnoreCase(hazmatMaterial.getInhalationHazard(), "Y") ? ", INHALATION HAZARD" : "");
                            comments.append(CommonUtils.isEqualIgnoreCase(hazmatMaterial.getResidue(), "Y") ? ", RESIDUE" : "");
                            comments.append(CommonUtils.isNotEmpty(hazmatMaterial.getEmsCode()) ? ", EMS " + hazmatMaterial.getEmsCode() : "");
                            comments.append(CommonUtils.isNotEmpty(hazmatMaterial.getContactName()) ? ", " + hazmatMaterial.getContactName() : "");
                            comments.append(CommonUtils.isNotEmpty(hazmatMaterial.getEmerreprsNum()) ? ", " + hazmatMaterial.getEmerreprsNum() : "");
                            hazmatComments.addAll(CommonUtils.splitForContainerString(comments.toString(), ".{0,48}(?:\\S(?:-| |$)|$)"));
                        }
                    }
                    List<String> packageMarks = CommonUtils.splitString(container.getMarks(), "\\n");
                    if (CommonUtils.isNotEmpty(containerPackages)) {
                        for (FclBlMarks containerPackage : containerPackages) {
                            lineNumber++;
                            //Start of GoodsDetails
                            writer.writeStartElement("GoodsDetails");

                            //Start of LineNumber
                            writer.writeStartElement("LineNumber");
                            writer.writeCharacters(String.valueOf(lineNumber));
                            writer.writeEndElement();
			    //End of LineNumber

                            //Start of PackageDetail
                            writer.writeStartElement("PackageDetail");
                            writer.writeAttribute("Level", "Outer");

                            StringBuilder stc = new StringBuilder();
                            if (CommonUtils.isNotEmpty(containerPackage.getNoOfPkgs())) {
                                //Start of NumberOfPackages
                                writer.writeStartElement("NumberOfPackages");
                                writer.writeCharacters(String.valueOf(containerPackage.getNoOfPkgs()));
                                writer.writeEndElement();
                                //End of NumberOfPackages
                                stc.append("STC: ").append(containerPackage.getNoOfPkgs());
                            } else {
                                warnings.append("--> Please Enter No of Pieces for  container ").append(container.getTrailerNo()).append("<br/>");
                            }

                            if (CommonUtils.isNotEmpty(containerPackage.getUom())) {
                                String packageTypeDescription = genericCodeDAO.getPackageType("Package Type", containerPackage.getUom().trim());
                                packageTypeDescription = CommonUtils.isNotEmpty(packageTypeDescription) ? packageTypeDescription : containerPackage.getUom().trim();
                                stc.append(" ").append(packageTypeDescription);
                                //Start of PackageTypeDescription
                                writer.writeStartElement("PackageTypeDescription");
                                writer.writeCharacters(packageTypeDescription.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                                writer.writeEndElement();
                                //End of PackageTypeDescription
                            } else {
                                warnings.append("--> Please Enter Package Type for container ").append(container.getTrailerNo()).append("<br/>");
                            }

                            writer.writeEndElement();
                            //End of PackageDetail

                            if (CommonUtils.isNotEmpty(stc)) {
                                //Start of PackageDetailComments
                                writer.writeStartElement("PackageDetailComments");
                                writer.writeAttribute("CommentType", "GoodsDescription");
                                writer.writeCharacters(stc.toString().replaceAll("[" + excludeCharcter + "]", "").replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                                writer.writeEndElement();
                                //End of PackageDetailComments
                            }

                            if (CommonUtils.isEqualIgnoreCase(containerPackage.getCopyDescription(), "Y")) {
                                List<String> packageDetailComments = CommonUtils.splitString(containerPackage.getDescPckgs(), "\\n");
                                if (CommonUtils.isNotEmpty(packageDetailComments)) {
                                    for (String description : packageDetailComments) {
                                        //Start of PackageDetailComments
                                        writer.writeStartElement("PackageDetailComments");
                                        writer.writeAttribute("CommentType", "GoodsDescription");
                                        if (CommonUtils.isExcludeEdiCharacter(description, excludeCharcter)) {
                                            errors.append("--> Please Remove the following Special Characters ").append(excludeCharcter).append(" from House BL Description for Container ");
                                            errors.append(container.getTrailerNo());
                                        } else {
                                            writer.writeCharacters(description.replaceAll("[" + excludeCharcter + "]", "").replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                                        }
                                        writer.writeEndElement();
                                        //End of PackageDetailComments
                                    }
                                } else {
                                    warnings.append("--> CopyDescription is 'Y' and House BL Description is not entered for container ");
                                    warnings.append(container.getTrailerNo()).append("<br/>");
                                }
                            } else {
                                List<String> packageDetailComments = CommonUtils.splitString(containerPackage.getDescForMasterBl(), "\\n");
                                if (CommonUtils.isNotEmpty(packageDetailComments)) {
                                    for (String description : packageDetailComments) {
                                        //Start of PackageDetailComments
                                        writer.writeStartElement("PackageDetailComments");
                                        writer.writeAttribute("CommentType", "GoodsDescription");
                                        if (CommonUtils.isExcludeEdiCharacter(description, excludeCharcter)) {
                                            errors.append("--> Please Remove the following Special Characters ").append(excludeCharcter).append(" from Master BL Description for Container ");
                                            errors.append(container.getTrailerNo());
                                        } else {
                                            writer.writeCharacters(description.replaceAll("[" + excludeCharcter + "]", "").replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                                        }
                                        writer.writeEndElement();
                                        //End of PackageDetailComments
                                    }
                                } else {
                                    warnings.append("--> CopyDescription is 'N' and Master BL Description is not entered for container ");
                                    warnings.append(container.getTrailerNo()).append("<br/>");
                                }
                            }

                            if (CommonUtils.isNotEmpty(hazmatComments)) {
                                for (String description : hazmatComments) {
                                    //Start of PackageDetailComments
                                    writer.writeStartElement("PackageDetailComments");
                                    writer.writeAttribute("CommentType", "GoodsDescription");
                                    writer.writeCharacters(description.replaceAll("[" + excludeCharcter + "]", "").replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                                    writer.writeEndElement();
                                    //End of PackageDetailComments
                                }
                            }

                            if (CommonUtils.isNotEmpty(containerPackage.getNetweightKgs())) {
                                if (CommonUtils.isNotEmpty(containerPackage.getMeasureCbm())) {
                                    //Start of PackageDetailGrossVolume
                                    writer.writeStartElement("PackageDetailGrossVolume");
                                    writer.writeAttribute("UOM", "MTQ");
                                    writer.writeCharacters(NumberUtils.formatNumber(containerPackage.getMeasureCbm(), "0.000"));
                                    writer.writeEndElement();
                                    //End of PackageDetailGrossVolume
                                }

                                //Start of PackageDetailGrossWeight
                                writer.writeStartElement("PackageDetailGrossWeight");
                                writer.writeAttribute("UOM", "KGM");
                                writer.writeCharacters(NumberUtils.formatNumber(containerPackage.getNetweightKgs(), "0.000"));
                                writer.writeEndElement();
                                //End of PackageDetailGrossWeight
                            } else if (CommonUtils.isNotEmpty(containerPackage.getNetweightLbs())) {
                                if (CommonUtils.isNotEmpty(containerPackage.getMeasureCft())) {
                                    //Start of PackageDetailGrossVolume
                                    writer.writeStartElement("PackageDetailGrossVolume");
                                    writer.writeAttribute("UOM", "FTQ");
                                    writer.writeCharacters(NumberUtils.formatNumber(containerPackage.getMeasureCft(), "0.000"));
                                    writer.writeEndElement();
                                    //End of PackageDetailGrossVolume
                                }

                                //Start of PackageDetailGrossWeight
                                writer.writeStartElement("PackageDetailGrossWeight");
                                writer.writeAttribute("UOM", "LBS");
                                writer.writeCharacters(NumberUtils.formatNumber(containerPackage.getNetweightLbs(), "0.000"));
                                writer.writeEndElement();
                                //End of PackageDetailGrossWeight
                            } else if (CommonUtils.isEmpty(containerPackage.getNetweightKgs())
                                    && CommonUtils.isEmpty(containerPackage.getNetweightLbs())) {
                                warnings.append("--> Weights are not entered for container ").append(container.getTrailerNo()).append("<br/>");
                            }

                            //Start of PackageMarks
                            writer.writeStartElement("PackageMarks");

                            if (CommonUtils.isNotEmpty(packageMarks)) {
                                for (String marks : packageMarks) {
                                    if (CommonUtils.isNotEmpty(marks)) {
                                        //Start of Marks
                                        writer.writeStartElement("Marks");
                                        writer.writeCharacters(marks.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                                        writer.writeEndElement();
                                        //End of Marks
                                    }
                                }
                            }
                            //Start of Marks
                            writer.writeStartElement("Marks");
                            writer.writeCharacters(containerNumber.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                            writer.writeEndElement();
                            //End of Marks

                            if (CommonUtils.isNotEmpty(container.getSealNo())) {
                                //Start of Marks
                                writer.writeStartElement("Marks");
                                writer.writeCharacters("SEAL: " + container.getSealNo().replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                                writer.writeEndElement();
                                //End of Marks
                            }

                            writer.writeEndElement();
			    //End of PackageMarks

                            //Start of SplitGoodsDetails
                            writer.writeStartElement("SplitGoodsDetails");

                            //Start of EquipmentIdentifier
                            writer.writeStartElement("EquipmentIdentifier");
                            writer.writeAttribute("EquipmentSupplier", "Carrier");
                            writer.writeCharacters(containerNumber.replaceAll("\\p{Cntrl}", "").replace("\\r", ""));
                            writer.writeEndElement();
			    //End of EquipmentIdentifier

                            //Start of SplitGoodsNumberOfPackages
                            writer.writeStartElement("SplitGoodsNumberOfPackages");
                            writer.writeCharacters(String.valueOf(containerPackage.getNoOfPkgs()));
                            writer.writeEndElement();
                            //End of SplitGoodsNumberOfPackages

                            if (CommonUtils.isNotEmpty(containerPackage.getNetweightKgs())) {
                                if (CommonUtils.isNotEmpty(containerPackage.getMeasureCbm())) {
                                    //Start of SplitGoodsGrossVolume
                                    writer.writeStartElement("SplitGoodsGrossVolume");
                                    writer.writeAttribute("UOM", "MTQ");
                                    writer.writeCharacters(NumberUtils.formatNumber(containerPackage.getMeasureCbm(), "0.000"));
                                    writer.writeEndElement();
                                    //End of SplitGoodsGrossVolume
                                }

                                //Start of SplitGoodsGrossWeight
                                writer.writeStartElement("SplitGoodsGrossWeight");
                                writer.writeAttribute("UOM", "KGM");
                                writer.writeCharacters(NumberUtils.formatNumber(containerPackage.getNetweightKgs(), "0.000"));
                                writer.writeEndElement();
                                //End of SplitGoodsGrossWeight
                            } else if (CommonUtils.isNotEmpty(containerPackage.getNetweightLbs())) {
                                if (CommonUtils.isNotEmpty(containerPackage.getMeasureCft())) {
                                    //Start of SplitGoodsGrossVolume
                                    writer.writeStartElement("SplitGoodsGrossVolume");
                                    writer.writeAttribute("UOM", "FTQ");
                                    writer.writeCharacters(NumberUtils.formatNumber(containerPackage.getMeasureCft(), "0.000"));
                                    writer.writeEndElement();
                                    //End of SplitGoodsGrossVolume
                                }

                                //Start of SplitGoodsGrossWeight
                                writer.writeStartElement("SplitGoodsGrossWeight");
                                writer.writeAttribute("UOM", "LBS");
                                writer.writeCharacters(NumberUtils.formatNumber(containerPackage.getNetweightLbs(), "0.000"));
                                writer.writeEndElement();
                                //End of SplitGoodsGrossWeight
                            }

                            writer.writeEndElement();
                            //End of SplitGoodsDetails

                            writer.writeEndElement();
                            //End of GoodsDetails
                        }
                    } else {
                        warnings.append("--> Please Enter Package Details for container ").append(container.getTrailerNo()).append("<br/>");
                    }
                }
            }
        } else {
            warnings.append("--> Atleast one container should be enabled<br/>");
        }
        writer.writeEndElement();
        //End of MessageDetails

        writer.writeEndElement();
        //End of MessageBody

        writer.writeEndElement();
        //End of Message

        writer.writeEndDocument();
        //End of Document
    }

    public void exit() throws Exception {
        if (null != writer) {
            writer.flush();
            writer.close();
        }
        if (null != outputStream) {
            outputStream.close();
        }
    }

    public void logErrors(String fileNumber, Exception e) throws Exception {
        String osName = System.getProperty("os.name").toLowerCase();
        StringBuilder errorFileName = new StringBuilder();
        errorFileName.append("error_logfile_").append(fileName.toString()).append("_").append(dateTimeSeconds).append(".txt");
        new EdiTrackingBC().setEdiLog(fileName.toString(), dateTimeSeconds, "failure", errors.toString(), "I", "304", fileNumber, bookingNumber, "", null);
        String errorMessage = null != e ? "Type of Error is---" + e.toString() : errors.toString();
        new LogFileWriter().doAppend(errorMessage, errorFileName.toString(), "I", osName, "304");
    }

    public void deleteFile() throws Exception {
        if (null != file && file.exists()) {
            if (null != writer) {
                writer.close();
                writer = null;
            }
            if (null != outputStream) {
                outputStream.close();
                outputStream = null;
            }
            file.delete();
        }
    }

    public String create(String fileNumber, String action, HttpServletRequest request) throws Exception {
        try {
            init(fileNumber);
            write(fileNumber, action, request);
            if (CommonUtils.isNotEmpty(errors)) {
                if (CommonUtils.isNotEqualIgnoreCase(action, "validate")) {
                    logErrors(fileNumber, null);
                }
                deleteFile();
                return "<span color: #000080;font-size: 10px;>Error Message</span><br/>" + errors.toString();
            } else if (CommonUtils.isNotEmpty(warnings)) {
                deleteFile();
                return warnings.toString();
            } else {
                if (CommonUtils.isNotEqualIgnoreCase(action, "validate")) {
                    new EdiTrackingBC().setEdiLog(fileName.toString(), dateTimeSeconds, "success", "No Error", "I", "304", fileNumber, bookingNumber, "", null);
                }
                return "XML generated successfully";
            }
        } catch (Exception e) {
            if (CommonUtils.isNotEqualIgnoreCase(action, "validate")) {
                logErrors(fileNumber, e);
            }
            deleteFile();
            return e.toString();
        } finally {
            exit();
        }
    }
}
