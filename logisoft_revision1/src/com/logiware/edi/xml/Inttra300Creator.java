package com.logiware.edi.xml;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
import com.gp.cong.logisoft.domain.CarriersOrLine;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.edi.util.LogFileWriter;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.struts.LoadEdiProperties;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.BookingfclUnits;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.BookingfclUnitsDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Balaji.E
 */
public class Inttra300Creator {

    private static final String ENCODING = "ISO-8859-1";
    private OutputStream outputStream = null;
    private XMLStreamWriter xmlWriter = null;
    private File file = null;
    private StringBuilder fileName = new StringBuilder();
    private String dateTimeSeconds = null;
    SimpleDateFormat xmldateFormat = null;
    private String xmlTimeSeconds = null;
    private String bookingNumber = "";
    private String referenceNumber = "";
    private String documentIdentifier = null;
    private final String reciverId = "INTTRA";
    private final String transactionType = "Booking";
    private final String requestMsgVersion = "1.0";
    private final String transactionVersion = "2.0";
    private String transactionStatus = "Original";
    private final String econocaribeConsolidatorsAcctno = "ECOCON0064";
    private StringBuilder errors = new StringBuilder();
    BookingFclDAO bookingFclDAO = new BookingFclDAO();
    EdiDAO ediDAO = new EdiDAO();

    private void init(String fileNumber) throws Exception {
        //Date Time values
        xmldateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        xmlTimeSeconds = xmldateFormat.format(date);
        dateTimeSeconds = DateUtils.formatDate(date, "yyyyMMddHHmmss");
        String companyName = LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName").toUpperCase();

        referenceNumber = "04-" + fileNumber;
        documentIdentifier = "04" + fileNumber;

        String folderName = LoadEdiProperties.getProperty(CommonUtils.isLinux() ? "linuxInttra300XmlOut" : "inttra300XmlOut");
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        fileName.append("300_").append(companyName).append("_INTTRA_").append(documentIdentifier).append("_").append(dateTimeSeconds).append(".xml");

        file = new File(folder, fileName.toString());
        outputStream = new FileOutputStream(file);
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        xmlWriter = outputFactory.createXMLStreamWriter(outputStream, ENCODING);
    }

    public void write(String fileNumber, HttpServletRequest request, String createOrCancel) throws Exception {
        String companyCode = LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase();
        String excludeCharcter = LoadLogisoftProperties.getProperty("edi.exclude.character");
        BookingFcl booking = bookingFclDAO.findbyFileNo(fileNumber);
        User user = (User) request.getSession().getAttribute("loginuser");
        String shipmentId = documentIdentifier + "_" + dateTimeSeconds;
        String placeOfReceiptCode = "";
        String placeOfDeliveryCode = "";
        String portOfLoadCode = "";
        String portOfDischargeCode = "";
        String moveMentType = "";
        String issuingTerminalNo = booking.getIssuingTerminal();
        String moveType = booking.getLineMove();
        String intraBookerId = "";
        String terminalNo = booking.getIssuingTerminal().substring(booking.getIssuingTerminal().lastIndexOf("-") + 1);
        intraBookerId = new TerminalDAO().getIntraBookerId(terminalNo);
        if (createOrCancel.equals("cancel")) {
            if (ediDAO.isValidForCancelOrAmendment(fileNumber, "997")) {
                errors.append("--> Cannot Cancel Booking Request Before Booking Acknowledgement (997)<br>");
            } else {
                shipmentId = ediDAO.getShpmentIdForCancelBooking(fileNumber, "997");
                if (shipmentId.isEmpty()) {
                    errors.append("--> Cannot Cancel Booking Request Before Booking Acknowledgement (997)<br>");
                } else {
                    bookingNumber = shipmentId.substring(shipmentId.indexOf(",") + 1);
                    shipmentId = shipmentId.substring(0, shipmentId.indexOf(","));
                    transactionStatus = "Cancel";
                }
            }
        } else if (createOrCancel.equals("change")) {
            shipmentId = ediDAO.getShpmentIdForCancelBooking(fileNumber, "301");
            if (shipmentId.contains(",")) {
                bookingNumber = shipmentId.substring(shipmentId.indexOf(",") + 1);
                shipmentId = shipmentId.substring(0, shipmentId.indexOf(","));
            }
            transactionStatus = "Change";
            if (shipmentId.isEmpty() || bookingNumber.isEmpty()) {
                errors.append("--> Cannot Send Amendment Booking Request Before Booking Confirmation(301)<br>");
            }
        }
        //Start of Document
        xmlWriter.writeStartDocument(ENCODING, "1.0");

        //Start of Message
        xmlWriter.writeStartElement("n1", "Message", "http://xml.inttra.com/shared/services/01");
        xmlWriter.writeAttribute("xmlns:ns1", "http://xml.inttra.com/shared/services/01");
        xmlWriter.writeAttribute("xmlns:n1", "http://xml.inttra.com/booking/services/01");

        //Start of Header
        xmlWriter.writeStartElement("Header");

        //Start of SenderId - Message
        xmlWriter.writeStartElement("SenderId");
        xmlWriter.writeCharacters(companyCode);
        //End of SenderId - Message
        xmlWriter.writeEndElement();

        //Start of ReceiverId - Message
        xmlWriter.writeStartElement("ReceiverId");
        xmlWriter.writeCharacters(reciverId);
        //End of ReceiverId - Message
        xmlWriter.writeEndElement();

        //Start of RequestDateTimeStamp - Message
        xmlWriter.writeStartElement("RequestDateTimeStamp");
        xmlWriter.writeCharacters(xmlTimeSeconds);
        //End of RequestDateTimeStamp - Message
        xmlWriter.writeEndElement();

        //Start of RequestMessageVersion - Message
        xmlWriter.writeStartElement("RequestMessageVersion");
        xmlWriter.writeCharacters(requestMsgVersion);
        //End of RequestMessageVersion - Message
        xmlWriter.writeEndElement();

        //Start of TransactionType - Message
        xmlWriter.writeStartElement("TransactionType");
        xmlWriter.writeCharacters(transactionType);
        //End of TransactionType - Message
        xmlWriter.writeEndElement();

        //Start of TransactionType - Message
        xmlWriter.writeStartElement("TransactionVersion");
        xmlWriter.writeCharacters(transactionVersion);
        //End of TransactionVersion - Message
        xmlWriter.writeEndElement();

        //Start of DocumentIdentifier - Message
        xmlWriter.writeStartElement("DocumentIdentifier");
        xmlWriter.writeCharacters(documentIdentifier);
        //End of DocumentIdentifier - Message
        xmlWriter.writeEndElement();

        //Start of TransactionStatus - Message
        xmlWriter.writeStartElement("TransactionStatus");
        xmlWriter.writeCharacters(transactionStatus);
        //End of TransactionStatus - Message
        xmlWriter.writeEndElement();

        //End of Header
        xmlWriter.writeEndElement();

        //Start of MessageBody
        xmlWriter.writeStartElement("MessageBody");

        //Start of MessageProperties
        xmlWriter.writeStartElement("MessageProperties");

        //Start of ShipmentID
        xmlWriter.writeStartElement("ShipmentID");
        xmlWriter.writeCharacters(shipmentId);
        //End of ShipmentID
        xmlWriter.writeEndElement();

        //Start of ContactInformation
        xmlWriter.writeStartElement("ContactInformation");

        //Start of Type
        xmlWriter.writeStartElement("Type");
        xmlWriter.writeCharacters("InformationContact");
        //End of Type
        xmlWriter.writeEndElement();

        //Start of Name
        xmlWriter.writeStartElement("Name");
        xmlWriter.writeCharacters(user.getLoginName());
        //End of Name
        xmlWriter.writeEndElement();

        //Start of CommunicationDetails
        xmlWriter.writeStartElement("CommunicationDetails");
        if (isNotNull(user.getTelephone())) {

            //Start of Phone
            xmlWriter.writeStartElement("Phone");
            xmlWriter.writeCharacters(user.getTelephone());
            //End of Phone
            xmlWriter.writeEndElement();

        }
        if (isNotNull(user.getFax())) {
            //Start of Fax
            xmlWriter.writeStartElement("Fax");
            xmlWriter.writeCharacters(user.getFax());
            //End of Fax
            xmlWriter.writeEndElement();
        }
        if (isNotNull(user.getEmail())) {

            //Start of Email
            xmlWriter.writeStartElement("Email");
            xmlWriter.writeCharacters(user.getEmail());
            //End of Email
            xmlWriter.writeEndElement();

        }
        //End of CommunicationDetails
        xmlWriter.writeEndElement();

        //End of ContactInformation
        xmlWriter.writeEndElement();

        //Start of DateTime
        xmlWriter.writeStartElement("DateTime");
        xmlWriter.writeAttribute("Type", "Date");
        xmlWriter.writeCharacters(xmlTimeSeconds);
        //End of DateTime
        xmlWriter.writeEndElement();

        if (isNotNull(booking.getLineMove()) && !"00".equals(booking.getLineMove())) {
            moveMentType = ediDAO.getMoveType(booking.getLineMove(), "field4");
            //Start of MovementType - Message
            xmlWriter.writeStartElement("MovementType");
            xmlWriter.writeCharacters(moveMentType);
            //End of MovementType - Message
            xmlWriter.writeEndElement();

        } else {
            errors.append("--> Please Select LineMove<br>");
        }
        if (isNotNull(booking.getCargoReadyDate())) {
            //Start of GeneralInformation - Message
            xmlWriter.writeStartElement("GeneralInformation");
            //End of Text - Message
            xmlWriter.writeStartElement("Text");
            StringBuilder generalInfo = new StringBuilder();
            generalInfo.append("Cargo Ready Date : ").append(xmldateFormat.format(booking.getCargoReadyDate())).append(".");
            if (CommonUtils.isNotEmpty(booking.getBookingComments())) {
                generalInfo.append(" Booking Comments : ").append(booking.getBookingComments()).append(".");
            }
            if (CommonUtils.isNotEmpty(booking.getLoadRemarks())) {
                generalInfo.append(" Spotting Remarks : ").append(booking.getLoadRemarks()).append(".");
            }
            xmlWriter.writeCharacters(generalInfo.toString());

            //End of Text - Message
            xmlWriter.writeEndElement();
            //End of GeneralInformation - Message
            xmlWriter.writeEndElement();

        } else {
            errors.append("--> Please Enter Cargo Ready Date<br>");
        }
        if (createOrCancel.equals("change")) {
            if (isNotNull(booking.getReasonForAmending())) {

                //Start of AmendmentJustification - Message
                xmlWriter.writeStartElement("AmendmentJustification");
                xmlWriter.writeStartElement("Text");
                xmlWriter.writeCharacters(booking.getReasonForAmending());

                //End of Text - Message
                xmlWriter.writeEndElement();
                //End of AmendmentJustification - Message
                xmlWriter.writeEndElement();

            } else {
                errors.append("--> Please Enter ReasonFor Amendment<br>");
            }

        }

        if (isNotNull(booking.getPortofDischarge()) && booking.getPortofDischarge().contains("/")) {
            if (booking.getPortofDischarge().lastIndexOf("(") != -1 && booking.getPortofDischarge().lastIndexOf(")") != -1) {
                placeOfDeliveryCode = booking.getPortofDischarge().substring(booking.getPortofDischarge().lastIndexOf("(") + 1, booking.getPortofDischarge().lastIndexOf(")"));
            }
        }
        Date etd = null;
        if (moveType.equals("PORT TO PORT")) {
            if (null != booking.getEtd()) {
                etd = booking.getEtd();
            } else {
                etd = getWeekDays(booking.getCargoReadyDate());
            }
        } else {
            if (null != booking.getEtd()) {
                etd = booking.getEtd();
            }
        }
        if (null == etd) {
            errors.append("--> Please Select ETD <br>");
        }
        if (isNotNull(placeOfDeliveryCode)) {

            //Start of DateTime - Location
            xmlWriter.writeStartElement("Location");

            //Start of PlaceOfDelivery - Location
            xmlWriter.writeStartElement("Type");
            xmlWriter.writeCharacters("PlaceOfDelivery");
            xmlWriter.writeEndElement();
            //End of PlaceOfDelivery - Location

            //Start of Identifier
            xmlWriter.writeStartElement("Identifier");
            xmlWriter.writeAttribute("Type", "UNLOC");
            xmlWriter.writeCharacters(placeOfDeliveryCode);
            //End of Identifier
            xmlWriter.writeEndElement();

            String[] locationDetails = bookingFclDAO.getCountryDetails(placeOfDeliveryCode);
            String cityName = locationDetails[0];
            String countryName = locationDetails[1];
            int str = countryName.length();
            if(str > 35){
                countryName = countryName.substring(0, 35);
                 }
            String countryCode = locationDetails[2];

            //Start of Name - Message
            xmlWriter.writeStartElement("Name");
            xmlWriter.writeCharacters(cityName);
            //End of Name - Message
            xmlWriter.writeEndElement();

            //Start of CountryName - Message
            xmlWriter.writeStartElement("CountryName");
            xmlWriter.writeCharacters(countryName);
            //End of CountryName - Message
            xmlWriter.writeEndElement();

            //Start of CountryCode - Message
            xmlWriter.writeStartElement("CountryCode");
            xmlWriter.writeCharacters(countryCode);
            //End of CountryCode - Message
            xmlWriter.writeEndElement();

            if (isNotNull(booking.getEta())) {

                //Start of DateTime
                xmlWriter.writeStartElement("DateTime");
                xmlWriter.writeAttribute("DateType", "EstimatedArrival");
                xmlWriter.writeAttribute("Type", "Date");
                xmlWriter.writeCharacters(xmldateFormat.format(booking.getEta()));
                //End of DateTime
                xmlWriter.writeEndElement();

            }

            //End of Location
            xmlWriter.writeEndElement();

        } else if (moveType.equals("DOOR TO DOOR") || moveType.equals("PORT TO DOOR") || moveType.equals("RAIL TO DOOR")) {
            if (!isNotNull(placeOfDeliveryCode)) {
                errors.append("--> Please Enter Place Of Delivery<br>");
            }
        }

        if (isNotNull(booking.getOriginTerminal()) && booking.getOriginTerminal().contains("/")) {
            if (booking.getOriginTerminal().lastIndexOf("(") != -1 && booking.getOriginTerminal().lastIndexOf(")") != -1) {
                placeOfReceiptCode = booking.getOriginTerminal().substring(booking.getOriginTerminal().lastIndexOf("(") + 1, booking.getOriginTerminal().lastIndexOf(")"));
            }
        }
        if (isNotNull(placeOfReceiptCode)) {

            //Start of DateTime - Location
            xmlWriter.writeStartElement("Location");

            //Start of PlaceOfDelivery - Location
            xmlWriter.writeStartElement("Type");
            xmlWriter.writeCharacters("PlaceOfReceipt");
            xmlWriter.writeEndElement();
            //End of PlaceOfDelivery - Location

            //Start of Identifier
            xmlWriter.writeStartElement("Identifier");
            xmlWriter.writeAttribute("Type", "UNLOC");
            xmlWriter.writeCharacters(placeOfReceiptCode);
            //End of Identifier
            xmlWriter.writeEndElement();

            String[] locationDetails = bookingFclDAO.getCountryDetails(placeOfReceiptCode);
            String cityName = locationDetails[0];
            String countryName = locationDetails[1];
            String countryCode = locationDetails[2];

            //Start of Name - Message
            xmlWriter.writeStartElement("Name");
            xmlWriter.writeCharacters(cityName);
            //End of Name - Message
            xmlWriter.writeEndElement();

            //Start of CountryName - Message
            xmlWriter.writeStartElement("CountryName");
            xmlWriter.writeCharacters(countryName);
            //End of CountryName - Message
            xmlWriter.writeEndElement();

            //Start of CountryCode - Message
            xmlWriter.writeStartElement("CountryCode");
            xmlWriter.writeCharacters(countryCode);
            //End of CountryCode - Message
            xmlWriter.writeEndElement();

            if (null != etd) {

                //Start of DateTime
                xmlWriter.writeStartElement("DateTime");
                xmlWriter.writeAttribute("DateType", "EarliestDeparture");
                xmlWriter.writeAttribute("Type", "Date");
                xmlWriter.writeCharacters(xmldateFormat.format(etd));
                //End of DateTime
                xmlWriter.writeEndElement();

            }

            //End of Location
            xmlWriter.writeEndElement();

        } else if (moveType.equals("DOOR TO DOOR") || moveType.equals("PORT TO DOOR") || moveType.equals("RAIL TO DOOR")) {
            if (!isNotNull(placeOfReceiptCode)) {
                errors.append("--> Please Enter Place Of Delivery<br>");
            }
        }
        UnLocation unLocation = new UnLocation();
        if (isNotNull(issuingTerminalNo) && issuingTerminalNo.contains("-")) {
            int k = issuingTerminalNo.lastIndexOf("-");
            issuingTerminalNo = issuingTerminalNo.substring(k + 1);
            String unLoCode = "";
            unLoCode = new RefTerminalDAO().getReferenceLocation(issuingTerminalNo);
            unLocation = new UnLocationDAO().getUnlocation(unLoCode);

        }
        if (null != unLocation) {

            //Start of DateTime - Location
            xmlWriter.writeStartElement("Location");

            //Start of PlaceOfDelivery - Location
            xmlWriter.writeStartElement("Type");
            xmlWriter.writeCharacters("BookingOffice");
            xmlWriter.writeEndElement();
            //End of PlaceOfDelivery - Location

            if (isNotNull(unLocation.getUnLocationCode())) {
                //Start of Identifier
                xmlWriter.writeStartElement("Identifier");
                xmlWriter.writeAttribute("Type", "UNLOC");
                xmlWriter.writeCharacters(unLocation.getUnLocationCode());
                //End of Identifier
                xmlWriter.writeEndElement();
            }

            if (isNotNull(unLocation.getUnLocationName())) {
                //Start of Name - Message
                xmlWriter.writeStartElement("Name");
                xmlWriter.writeCharacters(unLocation.getUnLocationName());
                //End of Name - Message
                xmlWriter.writeEndElement();
            }

            if (isNotNull(unLocation.getCountryId()) && isNotNull(unLocation.getCountryId().getCodedesc())) {
                //Start of CountryName - Message
                xmlWriter.writeStartElement("CountryName");
                xmlWriter.writeCharacters(unLocation.getCountryId().getCodedesc());
                //End of CountryName - Message
                xmlWriter.writeEndElement();
            }

            if (isNotNull(unLocation.getCountryId()) && isNotNull(unLocation.getCountryId().getCode())) {
                //Start of CountryCode - Message
                xmlWriter.writeStartElement("CountryCode");
                xmlWriter.writeCharacters(unLocation.getCountryId().getCode());
                //End of CountryCode - Message
                xmlWriter.writeEndElement();
            }

            //End of Location
            xmlWriter.writeEndElement();
        }

        if ((createOrCancel.equals("change") || createOrCancel.equals("cancel")) && !bookingNumber.isEmpty()) {
            //Start of ReferenceInformation

            xmlWriter.writeStartElement("ReferenceInformation");
            xmlWriter.writeAttribute("Type", "BookingNumber");
            //Start of Value
            xmlWriter.writeStartElement("Value");
            xmlWriter.writeCharacters(bookingNumber);
            xmlWriter.writeEndElement();
                //End of Value

            //End of ReferenceInformation
            xmlWriter.writeEndElement();
        }
        CarriersOrLine carriersOrLine = null;
        if (CommonUtils.isNotEmpty(booking.getSSLine())) {
            carriersOrLine = new CarriersOrLineDAO().getScacAndContractNumber(booking.getSSLine());
        }

        if (null != carriersOrLine && isNotNull(carriersOrLine.getFclContactNumber())) {

            //Start of ReferenceInformation
            xmlWriter.writeStartElement("ReferenceInformation");
            xmlWriter.writeAttribute("Type", "ContractNumber");

            //Start of Value
            xmlWriter.writeStartElement("Value");
            xmlWriter.writeCharacters(carriersOrLine.getFclContactNumber());
            xmlWriter.writeEndElement();
            //End of Value

            //End of ReferenceInformation
            xmlWriter.writeEndElement();

        }

        //Start of TransportationDetails
        xmlWriter.writeStartElement("TransportationDetails");
        xmlWriter.writeAttribute("TransportMode", "MaritimeTransport");
        xmlWriter.writeAttribute("TransportStage", "Main");
//        //Start of ConveyanceInformation
//        xmlWriter.writeStartElement("ConveyanceInformation");
//
//        //Start of Type
//        xmlWriter.writeStartElement("Type");
//        xmlWriter.writeCharacters("ContainerShip");
//        //End of ContainerShip
//        xmlWriter.writeEndElement();
//
//        //Start of Identifier
//        xmlWriter.writeStartElement("Identifier");
//        xmlWriter.writeAttribute("Type", "VesselName");
//        xmlWriter.writeCharacters(booking.getVessel());
//        //End of Identifier
//        xmlWriter.writeEndElement();
//
//        if (isNotNull(booking.getVoyageCarrier())) {
//
//            //Start of Identifier
//            xmlWriter.writeStartElement("Identifier");
//            xmlWriter.writeAttribute("Type", "VoyageNumber");
//            xmlWriter.writeCharacters(booking.getVoyageCarrier());
//            //End of Identifier
//            xmlWriter.writeEndElement();
//
//        } else {
//            errors.append("--> Please Enter Voyage Number<br>");
//        }
//
//        if (null != carriersOrLine && isNotNull(carriersOrLine.getSCAC())) {
//            if (carriersOrLine.getSCAC().length() >= 2 && carriersOrLine.getSCAC().length() <= 4) {
//
//                //Start of OperatorIdentifier
//                xmlWriter.writeStartElement("OperatorIdentifier");
//                xmlWriter.writeAttribute("Type", "SCACCode");
//                xmlWriter.writeCharacters(carriersOrLine.getSCAC());
//                //End of OperatorIdentifier
//                xmlWriter.writeEndElement();
//            } else {
//                errors.append("--> Carrier Scac Code(SSLINE) length must be between 2 & 4<br/>");
//            }
//
//        } else {
//            errors.append("--> Carrier Scac Code(SSLINE) is not matching<br/>");
//        }
//
//        //End of ConveyanceInformation
//        xmlWriter.writeEndElement();

        if (isNotNull(booking.getPortofOrgin()) && booking.getPortofOrgin().contains("/")) {
            if (booking.getPortofOrgin().lastIndexOf("(") != -1 && booking.getPortofOrgin().lastIndexOf(")") != -1) {
                portOfLoadCode = booking.getPortofOrgin().substring(booking.getPortofOrgin().lastIndexOf("(") + 1, booking.getPortofOrgin().lastIndexOf(")"));
            }
        }

        if (isNotNull(portOfLoadCode)) {

            //Start of Location
            xmlWriter.writeStartElement("Location");
            //Start of Type
            xmlWriter.writeStartElement("Type");
            xmlWriter.writeCharacters("PortOfLoad");
            //End of Type
            xmlWriter.writeEndElement();

            //Start of Identifier
            xmlWriter.writeStartElement("Identifier");

            xmlWriter.writeAttribute("Type", "UNLOC");
            xmlWriter.writeCharacters(portOfLoadCode);

            //End of Identifier
            xmlWriter.writeEndElement();

            String[] locationDetails = bookingFclDAO.getCountryDetails(portOfLoadCode);
            String cityName = locationDetails[0];
            String countryName = locationDetails[1];
            String countryCode = locationDetails[2];

            //Start of Name
            xmlWriter.writeStartElement("Name");
            xmlWriter.writeCharacters(cityName);
            //End of Name
            xmlWriter.writeEndElement();

            //Start of CountryName
            xmlWriter.writeStartElement("CountryName");
            xmlWriter.writeCharacters(countryName);
            //End of CountryName
            xmlWriter.writeEndElement();

            //Start of CountryCode
            xmlWriter.writeStartElement("CountryCode");
            xmlWriter.writeCharacters(countryCode);
            //End of CountryCode
            xmlWriter.writeEndElement();

            if (null != etd) {
                //Start of DateTime
                xmlWriter.writeStartElement("DateTime");
                xmlWriter.writeAttribute("DateType", "EarliestDeparture");
                xmlWriter.writeAttribute("Type", "Date");
                xmlWriter.writeCharacters(xmldateFormat.format(etd));
                //End of DateTime
                xmlWriter.writeEndElement();
            }

            //End of Location
            xmlWriter.writeEndElement();
        }
        if (isNotNull(booking.getDestination()) && booking.getDestination().contains("/")) {
            if (booking.getDestination().lastIndexOf("(") != -1 && booking.getDestination().lastIndexOf(")") != -1) {
                portOfDischargeCode = booking.getDestination().substring(booking.getDestination().lastIndexOf("(") + 1, booking.getDestination().lastIndexOf(")"));
            }
        }
        if (isNotNull(portOfDischargeCode)) {

            //Start of Location
            xmlWriter.writeStartElement("Location");

            //Start of Type
            xmlWriter.writeStartElement("Type");
            xmlWriter.writeCharacters("PortOfDischarge");
            xmlWriter.writeEndElement();
            //End of Type

            //Start of Identifier
            xmlWriter.writeStartElement("Identifier");

            xmlWriter.writeAttribute("Type", "UNLOC");
            xmlWriter.writeCharacters(portOfDischargeCode);

            //End of Identifier
            xmlWriter.writeEndElement();

            String[] locationDetails = bookingFclDAO.getCountryDetails(portOfDischargeCode);
            String cityName = locationDetails[0];
            String countryName = locationDetails[1];
            int str = countryName.length();
            if(str > 35){
                countryName = countryName.substring(0, 35);
                 }
            String countryCode = locationDetails[2];

            //Start of Name
            xmlWriter.writeStartElement("Name");
            xmlWriter.writeCharacters(cityName);
            //End of Name
            xmlWriter.writeEndElement();

            //Start of CountryName
            xmlWriter.writeStartElement("CountryName");
            xmlWriter.writeCharacters(countryName);
            //End of CountryName
            xmlWriter.writeEndElement();

            //Start of CountryCode
            xmlWriter.writeStartElement("CountryCode");
            xmlWriter.writeCharacters(countryCode);
            //End of CountryCode
            xmlWriter.writeEndElement();

            if (isNotNull(booking.getEta())) {

                //Start of DateTime
                xmlWriter.writeStartElement("DateTime");
                xmlWriter.writeAttribute("DateType", "EstimatedArrival");
                xmlWriter.writeAttribute("Type", "Date");
                xmlWriter.writeCharacters(xmldateFormat.format(booking.getEta()));
                //End of DateTime
                xmlWriter.writeEndElement();
            }

            //End of Location
            xmlWriter.writeEndElement();
        }

        //End of TransportationDetails
        xmlWriter.writeEndElement();

        //Start of Party
        xmlWriter.writeStartElement("Party");

        //Start of Role
        xmlWriter.writeStartElement("Role");
        xmlWriter.writeCharacters("Carrier");
        //End of Role
        xmlWriter.writeEndElement();

        if (isNotNull(booking.getSslname())) {

            //Start of Name
            xmlWriter.writeStartElement("Name");
            String acctName = booking.getSslname();
            int k = 0;
            if (acctName.contains("//")) {
                k = acctName.indexOf("//");
            } else if (acctName.contains("/")) {
                k = acctName.indexOf("/");
            }
            acctName = acctName.substring(0, k);

            //End of Name
            int str = acctName.length();
            if(str>35){
            acctName = acctName.substring(0, 35);
            }
            xmlWriter.writeCharacters(acctName);
            xmlWriter.writeEndElement();
        }

        if (null != carriersOrLine && null != carriersOrLine.getSCAC()) {

            //Start of PartnerAlias
            xmlWriter.writeStartElement("Identifier");
            xmlWriter.writeAttribute("Type", "PartnerAlias");
            // xmlWriter.writeCharacters("CA20"); Teting purpose only
            xmlWriter.writeCharacters(carriersOrLine.getSCAC());
            //End of PartnerAlias
            xmlWriter.writeEndElement();
        }

        //End of Party
        xmlWriter.writeEndElement();

        //Start of Party
        xmlWriter.writeStartElement("Party");

        CustAddress custAddress = new CustAddressDAO().findByAccountNo(econocaribeConsolidatorsAcctno);
        //Start of Role
        xmlWriter.writeStartElement("Role");
        xmlWriter.writeCharacters("Booker");
        //End of Role
        xmlWriter.writeEndElement();

        if (isNotNull(custAddress.getAcctName())) {
            //Start of Name
            xmlWriter.writeStartElement("Name");
            xmlWriter.writeCharacters(custAddress.getAcctName());
            //End of Name
            xmlWriter.writeEndElement();
        }

        if (isNotNull(companyCode) || isNotNull(intraBookerId)) {
            //Start of Identifier
            xmlWriter.writeStartElement("Identifier");

            xmlWriter.writeAttribute("Type", "PartnerAlias");
            if(CommonUtils.isNotEmpty(intraBookerId)){
            xmlWriter.writeCharacters(intraBookerId); 
           }else{
            xmlWriter.writeCharacters(companyCode);
        }
            //End of Identifier
            xmlWriter.writeEndElement();
        }

        //Start of Address
        xmlWriter.writeStartElement("Address");

        if (isNotNull(custAddress.getAddress1())) {
            //Start of StreetAddress
            xmlWriter.writeStartElement("StreetAddress");
            xmlWriter.writeCharacters(custAddress.getAddress1());
            //End of StreetAddress
            xmlWriter.writeEndElement();
        }

        if (isNotNull(custAddress.getState())) {
            //Start of Subdivision
            xmlWriter.writeStartElement("Subdivision");
            xmlWriter.writeCharacters(custAddress.getState());
            //End of Subdivision
            xmlWriter.writeEndElement();
        }

        if (isNotNull(custAddress.getZip())) {
            //Start of PostalCode
            xmlWriter.writeStartElement("PostalCode");
            xmlWriter.writeCharacters(custAddress.getZip());
            //End of PostalCode
            xmlWriter.writeEndElement();
        }

        if (isNotNull(custAddress.getCuntry()) && isNotNull(custAddress.getCuntry().getCode())) {
            //Start of CountryCode
            xmlWriter.writeStartElement("CountryCode");
            xmlWriter.writeCharacters(custAddress.getCuntry().getCode());
            //End of CountryCode
            xmlWriter.writeEndElement();
        }
        if (isNotNull(custAddress.getCuntry()) && isNotNull(custAddress.getCuntry().getCodedesc())) {
            //Start of CountryName
            xmlWriter.writeStartElement("CountryName");
            xmlWriter.writeCharacters(custAddress.getCuntry().getCodedesc());
            //End of CountryName
            xmlWriter.writeEndElement();
        }

        //End of Address
        xmlWriter.writeEndElement();

        //End of Party
        xmlWriter.writeEndElement();

        //Start of Party
        xmlWriter.writeStartElement("Party");

        //Start of Role
        xmlWriter.writeStartElement("Role");
        xmlWriter.writeCharacters("Shipper");
        //Start of Role
        xmlWriter.writeEndElement();

        if (isNotNull(custAddress.getAcctName())) {
            //Start of Name
            xmlWriter.writeStartElement("Name");
            xmlWriter.writeCharacters(custAddress.getAcctName());
            //End of Name
            xmlWriter.writeEndElement();
        }

        if (isNotNull(companyCode)) {
            //Start of Identifier
            xmlWriter.writeStartElement("Identifier");

            xmlWriter.writeAttribute("Type", "PartnerAlias");
            xmlWriter.writeCharacters(companyCode);
            //End of Identifier
            xmlWriter.writeEndElement();
        }

        //Start of Address
        xmlWriter.writeStartElement("Address");

        if (isNotNull(custAddress.getAddress1())) {
            //Start of StreetAddress
            xmlWriter.writeStartElement("StreetAddress");
            xmlWriter.writeCharacters(custAddress.getAddress1());
            //End of StreetAddress
            xmlWriter.writeEndElement();
        }

        if (isNotNull(custAddress.getState())) {
            //Start of Subdivision
            xmlWriter.writeStartElement("Subdivision");
            xmlWriter.writeCharacters(custAddress.getState());
            //End of Subdivision
            xmlWriter.writeEndElement();
        }

        if (isNotNull(custAddress.getZip())) {
            //Start of PostalCode
            xmlWriter.writeStartElement("PostalCode");
            xmlWriter.writeCharacters(custAddress.getZip());
            //End of PostalCode
            xmlWriter.writeEndElement();
        }

        if (isNotNull(custAddress.getCuntry()) && isNotNull(custAddress.getCuntry().getCode())) {
            //Start of CountryCode
            xmlWriter.writeStartElement("CountryCode");
            xmlWriter.writeCharacters(custAddress.getCuntry().getCode());
            //End of CountryCode
            xmlWriter.writeEndElement();
        }

        if (isNotNull(custAddress.getCuntry()) && isNotNull(custAddress.getCuntry().getCodedesc())) {
            //Start of CountryName
            xmlWriter.writeStartElement("CountryName");
            xmlWriter.writeCharacters(custAddress.getCuntry().getCodedesc());
            //End of CountryName
            xmlWriter.writeEndElement();
        }

        //End of Address
        xmlWriter.writeEndElement();

        //End of Party
        xmlWriter.writeEndElement();

        //End of MessageProperties
        xmlWriter.writeEndElement();

        //Start of MessageDetails
        xmlWriter.writeStartElement("MessageDetails");

        if (CommonUtils.isExcludeEdiCharacter(booking.getGoodsDescription(), excludeCharcter)) {
            errors.append("--> Please Remove the following Special Characters ").append(excludeCharcter).append(" from Good Description<br>");
        } else if (isNotNull(booking.getGoodsDescription())) {

            //Start of GoodsDetails
            xmlWriter.writeStartElement("GoodsDetails");

            //Start of LineNumber
            xmlWriter.writeStartElement("LineNumber");
            xmlWriter.writeCharacters("1");
            //End of LineNumber
            xmlWriter.writeEndElement();

            //Start of PackageDetail
            xmlWriter.writeStartElement("PackageDetail");
            //Start of OuterPack
            xmlWriter.writeStartElement("OuterPack");

            //Start of NumberOfPackages
            xmlWriter.writeStartElement("NumberOfPackages");
            xmlWriter.writeCharacters("100");
            //End of NumberOfPackages
            xmlWriter.writeEndElement();

            //Start of PackageTypeCode
            xmlWriter.writeStartElement("PackageTypeCode");
            xmlWriter.writeCharacters("PK");
            //End of PackageTypeCode
            xmlWriter.writeEndElement();

            //End of OuterPack
            xmlWriter.writeEndElement();

            //End of PackageDetail
            xmlWriter.writeEndElement();

            //Start of GoodDescription
            xmlWriter.writeStartElement("GoodDescription");
            xmlWriter.writeCharacters(booking.getGoodsDescription().replaceAll("[" + excludeCharcter + "]", ""));

            //End of GoodDescription
            xmlWriter.writeEndElement();

        } else {
            errors.append("--> Please Enter Goods Description<br>");
        }
        List<HazmatMaterial> hazmatMaterialList = new HazmatMaterialDAO().findbydoctypeid1("Booking", booking.getBookingId());
        if (booking.getHazmat().equalsIgnoreCase("Y") && hazmatMaterialList.isEmpty()) {
            errors.append("--> Please Enter Un Number,Shipping Name,IMOClassCode for Hazmat <br>");
        }
        for (HazmatMaterial hazmat : hazmatMaterialList) {
            //Start of HazardousGoods
            xmlWriter.writeStartElement("HazardousGoods");

            //Start of IMOClassCode
            xmlWriter.writeStartElement("IMOClassCode");
            xmlWriter.writeCharacters(hazmat.getImoClssCode());
            xmlWriter.writeEndElement();
            //End of IMOClassCode

            //Start of UNDGNumber
            xmlWriter.writeStartElement("UNDGNumber");
            xmlWriter.writeCharacters(hazmat.getUnNumber());
            xmlWriter.writeEndElement();
            //End of UNDGNumber

            //Start of ProperShippingName
            xmlWriter.writeStartElement("ProperShippingName");
            xmlWriter.writeCharacters(hazmat.getPropShipingNumber());
            xmlWriter.writeEndElement();
            //End of ProperShippingName

            //End of HazardousGoods
            xmlWriter.writeEndElement();
        }
        //End of GoodsDetails
        xmlWriter.writeEndElement();

        String haulageArrangements = "";

//        if (moveType.equalsIgnoreCase("DOOR TO DOOR")) {
//            haulageArrangements = "CarrierExportHaulageCarrierImportHaulage";
//        } else if (moveType.equalsIgnoreCase("DOOR TO PORT")) {
//            haulageArrangements = "CarrierExportHaulageMerchantImportHaulage";
//        } else if (moveType.equalsIgnoreCase("PORT TO PORT") || moveType.equalsIgnoreCase("RAIL TO PORT") || moveType.equalsIgnoreCase("RAMP TO PORT")) {
//            haulageArrangements = "MerchantExportHaulageMerchantImportHaulage";
//        } else if (moveType.equalsIgnoreCase("PORT TO DOOR")) {
//            haulageArrangements = "MerchantExportHaulageCarrierImportHaulage";
//        } else {
        if (moveType.equalsIgnoreCase("DOOR TO PORT")) {
            haulageArrangements = "CarrierExportHaulageMerchantImportHaulage";
        } else if (moveType.equalsIgnoreCase("PORT TO PORT") || moveType.equalsIgnoreCase("RAIL TO PORT")) {
            haulageArrangements = "MerchantExportHaulageMerchantImportHaulage";
        } else {
            errors.append("--> EDI Allowed only for PORT TO PORT, RAIL TO PORT, DOOR TO PORT Line Move Types<br>");
        }

        List<BookingfclUnits> bookingUnitsList = new BookingfclUnitsDAO().findByPropertyForApprovedUnits(booking.getBookingNumber());
        boolean unitTypePrecence = true;
        for (BookingfclUnits bookingUnits : bookingUnitsList) {
            unitTypePrecence = false;
            //Start of EquipmentDetails
            xmlWriter.writeStartElement("EquipmentDetails");
            xmlWriter.writeAttribute("FullEmptyIndicator", "Empty");
            xmlWriter.writeAttribute("EquipmentSupplier", "CarrierSupplied");

            //Start of EquipmentType
            xmlWriter.writeStartElement("EquipmentType");

            //Start of EquipmentTypeCode
            xmlWriter.writeStartElement("EquipmentTypeCode");
            xmlWriter.writeCharacters(bookingUnits.getUnitType().getField1());

            //End of EquipmentTypeCode
            xmlWriter.writeEndElement();

            //End of EquipmentType
            xmlWriter.writeEndElement();

            //Start of NumberOfEquipment
            xmlWriter.writeStartElement("NumberOfEquipment");
            xmlWriter.writeCharacters(bookingUnits.getNumbers());

            //End of NumberOfEquipment
            xmlWriter.writeEndElement();

            //Start of ImportExportHaulage
            xmlWriter.writeStartElement("ImportExportHaulage");

            //Start of CargoMovementType
            xmlWriter.writeStartElement("CargoMovementType");
            xmlWriter.writeCharacters("FCL/FCL");

            //End of CargoMovementType
            xmlWriter.writeEndElement();

            if (isNotNull(haulageArrangements)) {

                //Start of HaulageArrangements
                xmlWriter.writeStartElement("HaulageArrangements");
                xmlWriter.writeCharacters(haulageArrangements);

                //End of HaulageArrangements
                xmlWriter.writeEndElement();
            }
            //End of ImportExportHaulage
            xmlWriter.writeEndElement();

            if (moveMentType.startsWith("Door")) {
                //Start of EquipmentParty
                xmlWriter.writeStartElement("EquipmentParty");

                xmlWriter.writeStartElement("Role");
                xmlWriter.writeCharacters("ShipFromDoor");
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("Name");
                xmlWriter.writeCharacters(booking.getSpottingAccountName());
                xmlWriter.writeEndElement();

                //Start of Address
                xmlWriter.writeStartElement("Address");

                xmlWriter.writeStartElement("StreetAddress");
                xmlWriter.writeCharacters(booking.getAddressForExpPositioning());
                xmlWriter.writeEndElement();

                if (isNotNull(booking.getSpotAddrCity())) {
                    xmlWriter.writeStartElement("CityName");
                    xmlWriter.writeCharacters(booking.getSpotAddrCity());
                    xmlWriter.writeEndElement();
                }

                if (isNotNull(booking.getSpotAddrState())) {
                    xmlWriter.writeStartElement("Subdivision");
                    xmlWriter.writeCharacters(booking.getSpotAddrState());
                    xmlWriter.writeEndElement();
                }

                if (isNotNull(booking.getSpotAddrZip())) {
                    xmlWriter.writeStartElement("PostalCode");
                    xmlWriter.writeCharacters(booking.getSpotAddrZip());
                    xmlWriter.writeEndElement();
                }

                //End of Address
                xmlWriter.writeEndElement();

                //Start of Contacts
                xmlWriter.writeStartElement("Contacts");

                xmlWriter.writeStartElement("Type");
                xmlWriter.writeCharacters("InformationContact");
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("Name");
                xmlWriter.writeCharacters(booking.getLoadcontact());
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("CommunicationDetails");

                xmlWriter.writeStartElement("Phone");
                xmlWriter.writeCharacters(booking.getLoadphone());
                xmlWriter.writeEndElement();

                //End of CommunicationDetails
                xmlWriter.writeEndElement();

                //End of Contacts
                xmlWriter.writeEndElement();

                //End of EquipmentParty
                xmlWriter.writeEndElement();
            }

            if (bookingUnits.getUnitType().getField1().equals("42R1")) {
                //Start of NonActiveReefer
                xmlWriter.writeStartElement("NonActiveReefer");

                xmlWriter.writeCharacters("true");

                //End of NonActiveReefer
                xmlWriter.writeEndElement();
            }
            //End of EquipmentDetails
            xmlWriter.writeEndElement();

        }
        if (unitTypePrecence) {
            errors.append("--> Please select at least one Container in Cost & Charges Tab<br>");
        }
        //End of MessageDetails
        xmlWriter.writeEndElement();

        //End of MessageBody
        xmlWriter.writeEndElement();

        //End of Message
        xmlWriter.writeEndElement();

        //End of Document
        xmlWriter.writeEndDocument();
    }

    public void exit() throws Exception {
        if (null != xmlWriter) {
            xmlWriter.flush();
            xmlWriter.close();
        }
        if (null != outputStream) {
            outputStream.close();
        }
    }

    public void logErrors(String fileNumber, Exception e) throws Exception {
        String osName = System.getProperty("os.name").toLowerCase();
        StringBuilder errorFileName = new StringBuilder();
        errorFileName.append("error_logfile_").append(fileName.toString()).append("_").append(dateTimeSeconds).append(".txt");
        String errorMessage = null != e ? "Type of Error is---" + e.toString() : errors.toString();
        new LogFileWriter().doAppend(errorMessage, errorFileName.toString(), "I", osName, "300");
    }

    public void deleteFile() throws Exception {
        if (null != file && file.exists()) {
            if (null != xmlWriter) {
                xmlWriter.close();
                xmlWriter = null;
            }
            if (null != outputStream) {
                outputStream.close();
                outputStream = null;
            }
            file.delete();
        }
    }

    private boolean isNotNull(Object field) {
        return null != field && !field.toString().trim().equals("");
    }

    public String create(String fileNumber, HttpServletRequest request, String createOrCancel) throws Exception {
        try {
            init(fileNumber);
            write(fileNumber, request, createOrCancel);
            if (CommonUtils.isNotEmpty(errors)) {
                logErrors(fileNumber, null);
                deleteFile();
                return "<span color: #000080;font-size: 10px;>Error Message</span><br/>" + errors.toString();
            } else {
                if (createOrCancel.equals("create")) {
                    new EdiTrackingBC().setEdiLogForBooking(fileName.toString(), dateTimeSeconds, "success", "No Error", "I", "300", fileNumber, bookingNumber, "", null);
                    return "EDI Booking Request sent Successfully";
                } else if (createOrCancel.equals("change")) {
                    new EdiTrackingBC().setEdiLogForBooking(fileName.toString(), dateTimeSeconds, "change", "No Error", "I", "300", fileNumber, bookingNumber, "", null);
                    return "Amendment EDI Booking Request Sent successfully";
                } else {
                    new EdiTrackingBC().setEdiLogForBooking(fileName.toString(), dateTimeSeconds, "success", "No Error", "I", "300", fileNumber, bookingNumber, "", null);
                    return "Cancel EDI Booking Request Sent successfully";
                }
            }
        } catch (Exception e) {
            logErrors(fileNumber, e);
            deleteFile();
            return e.toString();
        } finally {
            exit();
            copyFileToArchive(file);
        }
    }

    private Date getWeekDays(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, +2); // Add 2 days in Dates in Calendar
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            cal.add(Calendar.DATE, +1);// Add 1 day if Given date is SUNDAY
        } else if (day == Calendar.SATURDAY) {
            cal.add(Calendar.DATE, +2);// Add 2 days if Given date is SATURDAY
        }
        return cal.getTime();
    }

    private void copyFileToArchive(File inputFilename) {
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
             String dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") .concat("/");
            String folderName = LoadEdiProperties.getProperty(CommonUtils.isLinux() ? "linuxInttra300Archive" : "inttra300Archive");
            File folder = new File(folderName+dateFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File outputfile = new File(folder,fileName.toString());

            inStream = new FileInputStream(inputFilename);
            outStream = new FileOutputStream(outputfile);
            byte[] buffer = new byte[1024];
            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}