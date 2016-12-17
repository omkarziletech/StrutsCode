package com.logiware.edi.xml;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import static com.gp.cong.common.ConstantsInterface.CONTACT_MODE_EMAIL;
import static com.gp.cong.common.ConstantsInterface.EMAIL_STATUS_PENDING;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.notes.NotesConstants;
import com.gp.cong.logisoft.domain.EmailSchedulerVO;
import com.gp.cong.logisoft.domain.LogFileEdi;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.edi.util.LogFileWriter;
import com.gp.cong.logisoft.hibernate.dao.EmailschedulerDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Balaji.E
 */
public class Inttra301Reader {

    private static final String osName = osName();
    NotesDAO notesDAO = new NotesDAO();
    private static final String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.
    private static String fileNo = "";

    public void Intra301Reader() throws Exception {
        StringBuilder logBuilder = new StringBuilder();
        Properties prop = new Properties();
        prop.load(Inttra301Reader.class.getResourceAsStream(CommonConstants.EDIPROPERTIES));
        String dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd").concat(File.separator);
        String archivePath = osName.contains("linux") ? "linuxInttra301Archive" : "inttra301Archive";
        String unprocessedPath = osName.contains("linux") ? "linuxInttra301Unprocessed" : "inttra301Unprocessed";
        File unProcessed = new File(prop.getProperty(unprocessedPath));
        File archive = new File(prop.getProperty(archivePath).concat(dateFolder));
        String sourcePath = osName.contains("linux") ? "linuxInttra301Directory" : "inttra301Directory";
        File sourceFile = new File(prop.getProperty(sourcePath));
        if (!archive.exists()) {
            archive.mkdirs();
        }
        if (!unProcessed.exists()) {
            unProcessed.mkdir();
        }
        for (File file : sourceFile.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".xml")) {
                try {
                       read301File(file);
                    //move the files to archive
                    if (file.exists()) {
                    file.renameTo(new File(archive.getPath().concat(File.separator).concat(file.getName())));
                    file.deleteOnExit();
                    }
                } catch (Exception e) {
                    //move the files to Un Proccessed
                    if (file.exists()) {
                    file.renameTo(new File(unProcessed.getPath().concat(File.separator).concat(file.getName())));
                    file.deleteOnExit();
                    }
                    logBuilder.append("Error In Reading Files ").append(file.getName()).append(" ").append(e.toString());
                    new LogFileWriter().doAppend(logBuilder.toString(), file.getName().concat(".txt"), "I", osName, "301");
                    setEdiBooking301Log(file.getName(), logBuilder.toString(), fileNo);
                    logBuilder.setLength(0);
                }
            }
        }


    }
    
    private void read301File(File file) throws Exception {
        InputStream inputStream = null;
        XMLEventReader eventReader = null;
        String dateTimeSeconds = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String shipmentId = "";
        String docCutOfDate = "";
        String bookingNumber = "";
        String etd = "";
        String eta = "";
        String vesselName = "";
        String voyageNumber = "";
        String equipmentPickupDate = "";
        String role = "";
        String equipmentReturnLocation = "";
        String equipmentPickupLocation = "";
        String equipmentPickupAddress = "";
        String equipmentReturnAddress = "";
        String equipmentReturnDate = "";
        String containerCutOffDate = "";
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        LogFileEdi logFileEdi = new LogFileEdi();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputStream = new FileInputStream(file);
            eventReader = inputFactory.createXMLEventReader(inputStream);
            String elementMap = "";
            fileNo = "";
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if ("Header".equals(startElement.getName().toString())) {
                        elementMap = "Header";
                    } else if ("Header".equals(elementMap) && "DocumentIdentifier".equals(startElement.getName().toString())) {
                        elementMap += "->DocumentIdentifier";
                    } else if ("Header".equals(elementMap) && "TransactionStatus".equals(startElement.getName().toString())) {
                        elementMap += "->TransactionStatus";
                    } else if ("MessageBody".equals(startElement.getName().toString())) {
                        elementMap = "MessageBody";
                    } else if ("MessageBody".equals(elementMap) && "MessageProperties".equals(startElement.getName().toString())) {
                        elementMap += "->MessageProperties";
                    } else if ("MessageBody->MessageProperties".equals(elementMap) && "ShipmentID".equals(startElement.getName().toString())) {
                        elementMap += "->ShipmentID";
                    } else if ("MessageBody->MessageProperties".equals(elementMap) && "DateTime".equals(startElement.getName().toString())) {
                        Iterator attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = (Attribute) (attributes.next());
                            if ("DateType".equals(attribute.getName().toString()) && CommonUtils.isEqual(attribute.getValue(), "ShipmentInstructionDueDate")) {
                                elementMap += "->DateTime->" + attribute.getValue();
                            }
                        }
                    } else if ("MessageBody->MessageProperties".equals(elementMap) && "ReferenceInformation".equals(startElement.getName().toString())) {
                        Iterator attributes = startElement.getAttributes();
                        if (attributes.hasNext()) {
                            Attribute attribute = (Attribute) (attributes.next());
                            if ("Type".equals(attribute.getName().toString()) && CommonUtils.in(attribute.getValue(), "INTTRAReferenceNumber", "BookingNumber")) {
                                elementMap += "->ReferenceInformation->" + attribute.getValue();
                            }
                        }
                    } else if ("MessageBody->MessageProperties->ReferenceInformation->INTTRAReferenceNumber".equals(elementMap) && "Value".equals(startElement.getName().toString())) {
                        elementMap += "->Value";
                    } else if ("MessageBody->MessageProperties->ReferenceInformation->BookingNumber".equals(elementMap) && "Value".equals(startElement.getName().toString())) {
                        elementMap += "->Value";
                    } else if ("MessageBody->MessageProperties".equals(elementMap) && "TransportationDetails".equals(startElement.getName().toString())) {
                        elementMap += "->TransportationDetails";
                    } else if ("MessageBody->MessageProperties->TransportationDetails".equals(elementMap) && "ConveyanceInformation".equals(startElement.getName().toString())) {
                        elementMap += "->ConveyanceInformation";
                    } else if ("MessageBody->MessageProperties->TransportationDetails->ConveyanceInformation".equals(elementMap) && "Identifier".equals(startElement.getName().toString())) {
                        Iterator attributes = startElement.getAttributes();
                        if (attributes.hasNext()) {
                            Attribute attribute = (Attribute) (attributes.next());
                            if ("Type".equals(attribute.getName().toString()) && CommonUtils.in(attribute.getValue(), "VesselName", "VoyageNumber")) {
                                elementMap += "->Identifier->" + attribute.getValue();
                            }
                        }
                    } else if ("MessageBody->MessageProperties->TransportationDetails".equals(elementMap) && "Location".equals(startElement.getName().toString())) {
                        elementMap += "->Location";
                    } else if ("MessageBody->MessageProperties->TransportationDetails->Location".equals(elementMap) && "DateTime".equals(startElement.getName().toString())) {
                        Iterator attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = (Attribute) (attributes.next());
                            if ("DateType".equals(attribute.getName().toString()) && CommonUtils.in(attribute.getValue(), "EarliestDeparture", "EstimatedArrival")) {
                                elementMap += "->DateTime->" + attribute.getValue();
                            }
                        }
                    } else if ("MessageBody".equals(elementMap) && "MessageDetails".equals(startElement.getName().toString())) {
                        elementMap += "->MessageDetails";
                    } else if ("MessageBody->MessageDetails".equals(elementMap) && "EquipmentDetails".equals(startElement.getName().toString())) {
                        elementMap += "->EquipmentDetails";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails".equals(elementMap) && "EquipmentParty".equals(startElement.getName().toString())) {
                        elementMap += "->EquipmentParty";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty".equals(elementMap) && "Role".equals(startElement.getName().toString())) {
                        elementMap += "->Role";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty".equals(elementMap) && "Name".equals(startElement.getName().toString())) {
                        elementMap += "->Name";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty".equals(elementMap) && "Address".equals(startElement.getName().toString())) {
                        elementMap += "->Address";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address".equals(elementMap) && "StreetAddress".equals(startElement.getName().toString())) {
                        elementMap += "->StreetAddress";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address".equals(elementMap) && "CityName".equals(startElement.getName().toString())) {
                        elementMap += "->CityName";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address".equals(elementMap) && "Subdivision".equals(startElement.getName().toString())) {
                        elementMap += "->Subdivision";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address".equals(elementMap) && "PostalCode".equals(startElement.getName().toString())) {
                        elementMap += "->PostalCode";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address".equals(elementMap) && "CountryCode".equals(startElement.getName().toString())) {
                        elementMap += "->CountryCode";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address".equals(elementMap) && "CountryName".equals(startElement.getName().toString())) {
                        elementMap += "->CountryName";
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty".equals(elementMap) && "DateTime".equals(startElement.getName().toString())) {
                        Iterator attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = (Attribute) (attributes.next());
                            if ("DateType".equals(attribute.getName().toString()) && CommonUtils.in(attribute.getValue(), "EarliestEmptyEquPickUp", "EarliestDropOffFullEquToCarrier", "LatestFullEquDeliveredToCarrier")) {
                                elementMap += "->DateTime->" + attribute.getValue();
                            }
                        }
                    }
                } else if (event.isCharacters()) {
                    if ("Header->DocumentIdentifier".equals(elementMap)) {
                        String documentId = event.asCharacters().getData();
                    } else if ("Header->TransactionStatus".equals(elementMap)) {
                        String transactionStatus = event.asCharacters().getData();
                        logFileEdi.setTransactionStatus(transactionStatus);
                        if (transactionStatus.equals("Confirmed")) {
                            logFileEdi.setStatus("success");
                            logFileEdi.setDescription("No Error");
                        }else if(transactionStatus.equals("Pending")){
                            logFileEdi.setStatus("pending");
                            logFileEdi.setDescription("No Error");
                        }else if(transactionStatus.equals("ConditionallyAccepted")){
                            logFileEdi.setStatus("conditionaccepted");
                            logFileEdi.setDescription("No Error");
                        }else if(transactionStatus.equals("Declined")){
                            logFileEdi.setStatus("declined");
                            logFileEdi.setDescription("No Error");
                        }else if(transactionStatus.equals("Replaced")){
                            logFileEdi.setStatus("replaced");
                            logFileEdi.setDescription("No Error");
                        }else if (transactionStatus.equals("Cancelled")) {
                            logFileEdi.setStatus("cancel");
                            logFileEdi.setDescription("No Error");
                        } else {
                            logFileEdi.setStatus("failure");
                            logFileEdi.setDescription("Error");
                        }
                    } else if ("MessageBody->MessageProperties->ShipmentID".equals(elementMap)) {
                        shipmentId = event.asCharacters().getData();
                        logFileEdi.setShipmentId(shipmentId);
                    } else if ("MessageBody->MessageProperties->DateTime->ShipmentInstructionDueDate".equals(elementMap)) {
                        docCutOfDate = event.asCharacters().getData();
                    } else if ("MessageBody->MessageProperties->ReferenceInformation->INTTRAReferenceNumber->Value".equals(elementMap)) {
                        String inttraReferenceNumber = event.asCharacters().getData();
                        logFileEdi.setReferenceNumber(inttraReferenceNumber);
                    } else if ("MessageBody->MessageProperties->ReferenceInformation->BookingNumber->Value".equals(elementMap)) {
                        bookingNumber = event.asCharacters().getData();
                        logFileEdi.setBookingNumber(bookingNumber);
                    } else if ("MessageBody->MessageProperties->TransportationDetails->ConveyanceInformation->Identifier->VesselName".equals(elementMap) && vesselName.isEmpty()) {
                        vesselName = event.asCharacters().getData();
                        logFileEdi.setVesselName(vesselName);
                    } else if ("MessageBody->MessageProperties->TransportationDetails->ConveyanceInformation->Identifier->VoyageNumber".equals(elementMap) && voyageNumber.isEmpty()) {
                        voyageNumber = event.asCharacters().getData();
                        logFileEdi.setVoyageNo(voyageNumber);
                    } else if ("MessageBody->MessageProperties->TransportationDetails->Location->DateTime->EarliestDeparture".equals(elementMap) && etd.isEmpty()) {
                        etd = event.asCharacters().getData();
                    } else if ("MessageBody->MessageProperties->TransportationDetails->Location->DateTime->EstimatedArrival".equals(elementMap)) {
                        eta = event.asCharacters().getData();
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->DateTime->EarliestEmptyEquPickUp".equals(elementMap)) {
                        equipmentPickupDate = event.asCharacters().getData();
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->DateTime->EarliestDropOffFullEquToCarrier".equals(elementMap)) {
                        equipmentReturnDate = event.asCharacters().getData();
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->DateTime->LatestFullEquDeliveredToCarrier".equals(elementMap)) {
                        containerCutOffDate = event.asCharacters().getData();
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Role".equals(elementMap)) {
                        role = event.asCharacters().getData();
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Name".equals(elementMap)) {
                        if (role.equals("EmptyPickUp")) {
                            equipmentPickupLocation = event.asCharacters().getData();
                        } else if (role.equals("FullDropOFF")) {
                            equipmentReturnLocation = event.asCharacters().getData();
                        }
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->StreetAddress".equals(elementMap)) {
                        if (role.equals("EmptyPickUp")) {
                            equipmentPickupAddress += event.asCharacters().getData().concat(newLine);
                        } else if (role.equals("FullDropOFF")) {
                            equipmentReturnAddress += event.asCharacters().getData().concat(newLine);
                        }
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->PostalCode".equals(elementMap)) {
                        if (role.equals("EmptyPickUp")) {
                            equipmentPickupAddress += event.asCharacters().getData().concat(newLine);
                        } else if (role.equals("FullDropOFF")) {
                            equipmentReturnAddress += event.asCharacters().getData().concat(newLine);
                        }
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->CountryCode".equals(elementMap)) {
                        if (role.equals("EmptyPickUp")) {
                            equipmentPickupAddress += event.asCharacters().getData().concat(newLine);
                        } else if (role.equals("FullDropOFF")) {
                            equipmentReturnAddress += event.asCharacters().getData().concat(newLine);
                        }
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->Subdivision".equals(elementMap)) {
                        if (role.equals("EmptyPickUp")) {
                            equipmentPickupAddress += event.asCharacters().getData().concat(newLine);
                        } else if (role.equals("FullDropOFF")) {
                            equipmentReturnAddress += event.asCharacters().getData().concat(newLine);
                        }
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->CityName".equals(elementMap)) {
                        if (role.equals("EmptyPickUp")) {
                            equipmentPickupAddress += event.asCharacters().getData().concat(newLine);
                        } else if (role.equals("FullDropOFF")) {
                            equipmentReturnAddress += event.asCharacters().getData().concat(newLine);
                        }
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->CountryName".equals(elementMap)) {
                        if (role.equals("EmptyPickUp")) {
                            equipmentPickupAddress += event.asCharacters().getData().concat(newLine);
                        } else if (role.equals("FullDropOFF")) {
                            equipmentReturnAddress += event.asCharacters().getData().concat(newLine);
                        }
                    }
                } else if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if ("Header".equals(endElement.getName().toString())) {
                        elementMap = "";
                    } else if ("Header->DocumentIdentifier".equals(elementMap) && "DocumentIdentifier".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->DocumentIdentifier", "");
                    } else if ("Header->TransactionStatus".equals(elementMap) && "TransactionStatus".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->TransactionStatus", "");
                    } else if ("MessageBody".equals(endElement.getName().toString())) {
                        elementMap = "";
                    } else if ("MessageBody->MessageProperties".equals(elementMap) && "MessageProperties".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->MessageProperties", "");
                    } else if ("MessageBody->MessageProperties->ShipmentID".equals(elementMap) && "ShipmentID".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->ShipmentID", "");
                    } else if ("MessageBody->MessageProperties->DateTime->ShipmentInstructionDueDate".equals(elementMap) && "DateTime".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->DateTime->ShipmentInstructionDueDate", "");
                    } else if ("MessageBody->MessageProperties->ReferenceInformation->INTTRAReferenceNumber".equals(elementMap) && "ReferenceInformation".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->ReferenceInformation->INTTRAReferenceNumber", "");
                    } else if ("MessageBody->MessageProperties->ReferenceInformation->BookingNumber".equals(elementMap) && "ReferenceInformation".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->ReferenceInformation->BookingNumber", "");
                    } else if ("MessageBody->MessageProperties->ReferenceInformation->INTTRAReferenceNumber->Value".equals(elementMap) && "Value".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Value", "");
                    } else if ("MessageBody->MessageProperties->ReferenceInformation->BookingNumber->Value".equals(elementMap) && "Value".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Value", "");
                    } else if ("MessageBody->MessageProperties->TransportationDetails".equals(elementMap) && "TransportationDetails".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->TransportationDetails", "");
                    } else if ("MessageBody->MessageProperties->TransportationDetails->ConveyanceInformation".equals(elementMap) && "ConveyanceInformation".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->ConveyanceInformation", "");
                    } else if ("MessageBody->MessageProperties->TransportationDetails->ConveyanceInformation->Identifier->VesselName".equals(elementMap) && "Identifier".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Identifier->VesselName", "");
                    } else if ("MessageBody->MessageProperties->TransportationDetails->ConveyanceInformation->Identifier->VoyageNumber".equals(elementMap) && "Identifier".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Identifier->VoyageNumber", "");
                    } else if ("MessageBody->MessageProperties->TransportationDetails->Location->DateTime->EarliestDeparture".equals(elementMap) && "DateTime".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Location->DateTime->EarliestDeparture", "");
                    } else if ("MessageBody->MessageProperties->TransportationDetails->Location".equals(elementMap) && "Location".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Location", "");
                    } else if ("MessageBody->MessageProperties->TransportationDetails->Location->DateTime->EstimatedArrival".equals(elementMap) && "DateTime".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->DateTime->EstimatedArrival", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty".equals(elementMap) && "EquipmentParty".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->EquipmentParty", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Role".equals(elementMap) && "Role".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Role", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Name".equals(elementMap) && "Name".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Name", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->StreetAddress".equals(elementMap) && "StreetAddress".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->StreetAddress", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->CityName".equals(elementMap) && "CityName".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->CityName", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->Subdivision".equals(elementMap) && "Subdivision".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Subdivision", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->PostalCode".equals(elementMap) && "PostalCode".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->PostalCode", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->CountryCode".equals(elementMap) && "CountryCode".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->CountryCode", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address->CountryName".equals(elementMap) && "CountryName".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->CountryName", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->DateTime->EarliestEmptyEquPickUp".equals(elementMap) && "DateTime".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->DateTime->EarliestEmptyEquPickUp", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->DateTime->EarliestDropOffFullEquToCarrier".equals(elementMap) && "DateTime".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->DateTime->EarliestDropOffFullEquToCarrier", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->DateTime->LatestFullEquDeliveredToCarrier".equals(elementMap) && "DateTime".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->DateTime->LatestFullEquDeliveredToCarrier", "");
                    } else if ("MessageBody->MessageDetails->EquipmentDetails->EquipmentParty->Address".equals(elementMap) && "Address".equals(endElement.getName().toString())) {
                        elementMap = elementMap.replace("->Address", "");
                    }
                }
            }
            boolean validFile=false;
            if (!shipmentId.isEmpty()) {
                String drNum = shipmentId.substring(0, shipmentId.indexOf("_"));
                fileNo = drNum.replaceFirst("04", "");
                if (!fileNo.isEmpty()) {

                    // Updating Booking Values Only if File in Booking (Not in BL).
                    BookingFcl bookingFcl = bookingFclDAO.findbyFileNo(fileNo);
                    if (CommonUtils.isNotEqual("on", bookingFcl.getBlFlag())) {
                        validFile = true;
                        if (CommonUtils.in(logFileEdi.getStatus(), "success", "pending", "conditionaccepted")) {
                            bookingFcl.setSSBookingNo(bookingNumber);
                            bookingFcl.setVessel(vesselName);
                            bookingFcl.setVoyageCarrier(voyageNumber);

                            // Making Notes in Booking
                            bookingEdiNotesForInsert(fileNo, "EDI Booking Confirmation(301) Received with ShipmentId - " + logFileEdi.getShipmentId() + "");
                            bookingEdiNotesForInsert(fileNo, "From EDI: SS Bkg # - " + bookingNumber + "");
                            bookingEdiNotesForInsert(fileNo, "From EDI: Vessel - " + vesselName + "");
                            bookingEdiNotesForInsert(fileNo, "From EDI: SS Voy - " + voyageNumber + "");

                            if (!containerCutOffDate.isEmpty()) {
                                Date date = DateUtils.parseDate(containerCutOffDate, "yyyy-MM-dd'T'HH:mm:ss");
                                bookingFcl.setPortCutOff(date);
                                containerCutOffDate = DateUtils.formatDate(date, "MM/dd/yyyy hh:mm a");
                                bookingEdiNotesForInsert(fileNo, "From EDI Container Cut Off - " + containerCutOffDate + "");
                            }
                            if (!etd.isEmpty()) {
                                Date date = DateUtils.parseDate(etd, "yyyy-MM-dd'T'HH:mm:ss");
                                etd = DateUtils.formatDate(date, "MM/dd/yyyy");
                                date = DateUtils.parseDate(etd, "MM/dd/yyyy");
                                bookingFcl.setEtd(date);
                                bookingEdiNotesForInsert(fileNo, "From EDI: ETD - " + etd + "");
                            }
                            if (!eta.isEmpty()) {
                                Date date = DateUtils.parseDate(eta, "yyyy-MM-dd'T'HH:mm:ss");
                                eta = DateUtils.formatDate(date, "MM/dd/yyyy");
                                date = DateUtils.parseDate(eta, "MM/dd/yyyy");
                                bookingFcl.setEta(date);
                                bookingEdiNotesForInsert(fileNo, "From EDI: ETA - " + eta + "");
                            }
                            if (!docCutOfDate.isEmpty()) {
                                Date date = DateUtils.parseDate(docCutOfDate, "yyyy-MM-dd'T'HH:mm:ss");
                                date = DateUtils.parseDate(getWeekDays(date) + " 12:00", "MM/dd/yyyy kk:mm");
                                docCutOfDate = DateUtils.formatDate(date, "MM/dd/yyyy kk:mm");
                                bookingFcl.setDocCutOff(date);
                                bookingEdiNotesForInsert(fileNo, "From EDI: Doc Cut Off - " + docCutOfDate + "");
                            }
                            if (!equipmentPickupDate.isEmpty()) {
                                Date date = DateUtils.parseDate(equipmentPickupDate, "yyyy-MM-dd'T'HH:mm:ss");
                                equipmentPickupDate = DateUtils.formatDate(date, "MM/dd/yyyy");
                                date = DateUtils.parseDate(equipmentPickupDate, "MM/dd/yyyy");
                                bookingFcl.setEarliestPickUpDate(date);
                                bookingEdiNotesForInsert(fileNo, "From EDI: Earliest Date - " + equipmentPickupDate + "");
                            }
                            if (!equipmentPickupLocation.isEmpty()) {
                                bookingFcl.setExportPositoningPickup(equipmentPickupLocation);
                                bookingEdiNotesForInsert(fileNo, "From EDI: Equipment Pickup Location/Name - ".concat(equipmentPickupLocation));
                            }
                            if (!equipmentPickupAddress.isEmpty()) {
                                bookingFcl.setEmptypickupaddress(equipmentPickupAddress);
                                bookingEdiNotesForInsert(fileNo, "From EDI: Equipment Pickup Name/Address - ".concat(equipmentPickupLocation.concat(newLine).concat(equipmentPickupAddress)));
                            }
                            if (!equipmentReturnLocation.isEmpty()) {
                                bookingFcl.setEquipmentReturnName(equipmentReturnLocation);
                                bookingEdiNotesForInsert(fileNo, "From EDI: Equipment Return Location/Name - ".concat(equipmentReturnLocation));
                            }
                            if (!equipmentReturnAddress.isEmpty()) {
                                bookingFcl.setAddessForExpDelivery(equipmentReturnAddress);
                                bookingEdiNotesForInsert(fileNo, "From EDI: Equipment Return Name/Address - ".concat(equipmentReturnLocation.concat(newLine).concat(equipmentReturnAddress)));
                            }
                            if (!equipmentReturnDate.isEmpty()) {
                                Date date = DateUtils.parseDate(equipmentReturnDate, "yyyy-MM-dd'T'HH:mm:ss");
                                equipmentReturnDate = DateUtils.formatDate(date, "MM/dd/yyyy");
                                bookingFcl.setReturnRemarks(bookingFcl.getReturnRemarks().concat(newLine).concat("Equipment Return Date : ").concat(equipmentReturnDate));
                                bookingEdiNotesForInsert(fileNo, "From EDI: Equipment Return Date - " + equipmentReturnDate + "");
                            }
                            bookingFclDAO.update(bookingFcl);
                        }
                    }

                    logFileEdi.setDrnumber(drNum);
                    logFileEdi.setFileNo(fileNo);
                    logFileEdi.setEdiCompany("I");
                    logFileEdi.setFilename(file.getName());
                    logFileEdi.setMessageType("301");
                    logFileEdi.setDocType("Booking");
                    logFileEdi.setProcessedDate(dateTimeSeconds);
                    logFileEdiDAO.saveLogFileEdi(logFileEdi);
                    
                    // Strat Sending Email To Edi Created User
                    String editCreatedUserEmail = bookingFclDAO.getEdiCreatedUserEmail(fileNo);
                    if (!editCreatedUserEmail.isEmpty() && validFile) {
                        String msg = emailNotifyMsg(bookingFcl, logFileEdi.getStatus(), containerCutOffDate, eta, etd,
                                docCutOfDate, equipmentPickupDate, equipmentReturnDate);
                        sendEmailNotifications(editCreatedUserEmail, bookingFcl, msg);
                    }
                    // End Sending Email To Edi Created User
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (XMLStreamException e) {
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

    private static String osName() {
        return System.getProperty("os.name").toLowerCase();
    }

    private String getWeekDays(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1); // Substract days in Dates in Calendar
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) {
            cal.add(Calendar.DATE, -2);// Substract 2 days if Given date is SUNDAY
        } else if (day == Calendar.SATURDAY) {
            cal.add(Calendar.DATE, -1);// Substract 2 days if Given date is SATURDAY
        }
        return DateUtils.parseDateToString(cal.getTime());
    }

    private String emailNotifyMsg(BookingFcl bookingFcl, String status, String containerCutOff,
            String etd, String eta, String docCutOffDate, String earliestDate, String equipmentReturnDate) throws Exception {
        StringBuilder msgBuilder = new StringBuilder();
        if (status.equals("failure")) {
            msgBuilder.append("EDI Booking Request Failed for file 04-").append(bookingFcl.getFileNo()).append(", Pleaes Refer Log For More Details.");
        } else {
            if (isNotNull(bookingFcl.getEdiCanceledBy()) && status.equals("cancel")) {
                msgBuilder.append("EDI Booking Cancellation Request Confirmed for file 04-").append(bookingFcl.getFileNo()).append(" .");
            } else if (isNotNull(bookingFcl.getReasonForAmending())) {
                msgBuilder.append("EDI Booking Amendment Request Confirmed for file 04-").append(bookingFcl.getFileNo()).append(".");
            }else if (isNotNull(status) && status.equals("pending")) {
                msgBuilder.append("EDI Booking Request Pending for file 04-").append(bookingFcl.getFileNo()).append(" .");
            }else if (isNotNull(status) && status.equals("conditionaccepted")) {
                msgBuilder.append("EDI Booking Request Conditionally Accepted for file 04-").append(bookingFcl.getFileNo()).append(" .");
            }else if (isNotNull(status) && status.equals("declined")) {
                msgBuilder.append("EDI Booking Request Declined for file 04-").append(bookingFcl.getFileNo()).append(" .");
            }else if (isNotNull(status) && status.equals("replaced")) {
                msgBuilder.append("EDI Booking Request Replaced for file 04-").append(bookingFcl.getFileNo()).append(" .");
            } else {
                msgBuilder.append("EDI Booking Request Confirmation for file 04-").append(bookingFcl.getFileNo()).append(" with the following details.");
            }
            msgBuilder.append(newLine);
            msgBuilder.append(newLine);
            if (CommonUtils.in(status.trim(),"success","pending","conditionaccepted","declined","replaced")) {

                msgBuilder.append("SS Bkg # : ").append(bookingFcl.getSSBookingNo());
                msgBuilder.append(newLine);

                msgBuilder.append("Vessel : ").append(bookingFcl.getVessel());
                msgBuilder.append(newLine);

                msgBuilder.append("SS Voy : ").append(bookingFcl.getVoyageCarrier());
                msgBuilder.append(newLine);
                if (isNotNull(etd)) {
                    msgBuilder.append("ETD : ").append(etd);
                    msgBuilder.append(newLine);
                }
                if (isNotNull(eta)) {
                    msgBuilder.append("ETA : ").append(eta);
                    msgBuilder.append(newLine);
                }
                if (isNotNull(docCutOffDate)) {
                    msgBuilder.append("Doc Cut Off : ").append(docCutOffDate);
                    msgBuilder.append(newLine);
                }
                if (isNotNull(containerCutOff)) {
                    msgBuilder.append("Container Cut Off : ").append(containerCutOff);
                    msgBuilder.append(newLine);
                }
                if (isNotNull(earliestDate)) {
                    msgBuilder.append("Earliest Date : ").append(earliestDate);
                    msgBuilder.append(newLine);
                }
                if (isNotNull(equipmentReturnDate)) {
                    msgBuilder.append("Equipment Return Date : ").append(equipmentReturnDate);
                    msgBuilder.append(newLine);
                }
            }
        }

        return msgBuilder.toString();
    }

    private void sendEmailNotifications(String toAddress, BookingFcl bookingFcl, String msg) throws Exception {
        EmailSchedulerVO mailTransaction = new EmailSchedulerVO();
        mailTransaction.setFromAddress(toAddress);
        mailTransaction.setToAddress(toAddress);
        if (msg.contains("Cancellation")) {
            mailTransaction.setSubject("EDI Booking Cancellation Confirmed for File 04-".concat(bookingFcl.getFileNo()));
        } else if (msg.contains("Pending")) {
            mailTransaction.setSubject("EDI Booking Request Pending for File 04-".concat(bookingFcl.getFileNo()));
        } else if (msg.contains("Conditionally Accepted")) {
            mailTransaction.setSubject("EDI Booking Request Conditionally Accepted for File 04-".concat(bookingFcl.getFileNo()));
        } else if (msg.contains("Amendment")) {
            mailTransaction.setSubject("EDI Booking Amendment Confirmed for File 04-".concat(bookingFcl.getFileNo()));
        } else if (msg.contains("Declined")) {
            mailTransaction.setSubject("EDI Booking Request Declined for File 04-".concat(bookingFcl.getFileNo()));
        } else if (msg.contains("Replaced")) {
            mailTransaction.setSubject("EDI Booking Request Replaced for File 04-".concat(bookingFcl.getFileNo()));
        } else {
            mailTransaction.setSubject("EDI Booking Request Confirmed for File 04-".concat(bookingFcl.getFileNo()));
        }
        mailTransaction.setHtmlMessage(msg);
        mailTransaction.setTextMessage(msg);
        mailTransaction.setName("Booking EDI");
        mailTransaction.setType(CONTACT_MODE_EMAIL);
        mailTransaction.setStatus(EMAIL_STATUS_PENDING);
        mailTransaction.setNoOfTries(0);
        mailTransaction.setEmailDate(new Date());
        mailTransaction.setModuleName("Booking EDI");
        mailTransaction.setModuleId("04-".concat(bookingFcl.getFileNo()));
        new EmailschedulerDAO().save(mailTransaction);
    }

    private void setEdiBooking301Log(String filename, String errorDescription, String fileNo) throws Exception {
        LogFileEdi logFileEdi = new LogFileEdi();
        logFileEdi.setFilename(null != filename ? filename : "");
        logFileEdi.setProcessedDate(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
        logFileEdi.setStatus("failure");
        logFileEdi.setDescription(null != errorDescription ? errorDescription : "");
        logFileEdi.setEdiCompany("I");
        logFileEdi.setMessageType("301");
        logFileEdi.setDrnumber(null != fileNo ? "04" + fileNo : "");
        logFileEdi.setFileNo(fileNo);
        logFileEdi.setDocType("Booking");
        new LogFileEdiDAO().saveLogFileEdi(logFileEdi);
    }

    private boolean isNotNull(Object field) throws Exception {
        return null != field && !field.toString().trim().equals("");
    }

    public void bookingEdiNotesForInsert(String fileNo, String message) throws Exception {
        Notes note = new Notes();
        note.setModuleId(NotesConstants.FILE);
        note.setModuleRefId(fileNo);
        note.setNoteType(NotesConstants.NOTES_TYPE_EVENT);
        note.setUpdateDate(new Date());
        note.setUpdatedBy("Bkg EDI");
        note.setNoteDesc(message);
        notesDAO.save(note);
    }
    public String  parseXmlToDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dateFormat = (Date) sdf.parse(date.trim());
        return DateUtils.parseDateToString(dateFormat);
    }
    public String  parseXmlToDateTime(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dateFormat = (Date) sdf.parse(date.trim());
        return DateUtils.parseDateToDateTimeString(dateFormat);
    }
}
