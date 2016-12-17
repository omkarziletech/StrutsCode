package com.gp.cong.logisoft.lcl.kn.bc;

import com.gp.cong.logisoft.domain.lcl.kn.BookingDetail;
import com.gp.cong.logisoft.domain.lcl.kn.BookingEnvelope;
import com.gp.cong.logisoft.domain.lcl.kn.PickupDetail;
import com.gp.cong.logisoft.domain.lcl.kn.SailingDetail;
import com.gp.cong.logisoft.lcl.kn.xml.BookingConfirmation;
import com.gp.cong.logisoft.lcl.kn.xml.ObjectFactory;
import com.gp.cong.logisoft.lcl.kn.xml.XMLConstants;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.io.BufferedInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;

/**
 *
 * @author Rajesh
 */
public class XMLUtil implements XMLConstants {

    private static Logger log = Logger.getLogger(XMLUtil.class);

    public File generateXML(BookingEnvelope logiBkg) throws Exception {
        String dispatchpath = LoadLogisoftProperties.getProperty(ACK_DISPATCH_PATH);
        File file = null;
        String fileName = dispatchpath + DISPATCH_XML_FILE_NAME + logiBkg.getEnvelopeId() + XML_FILE_FORMAT;
        try {
            file = new File(fileName);
            JAXBContext jAXBContext = JAXBContext.newInstance(BookingConfirmation.class);
            Marshaller marshaller = jAXBContext.createMarshaller();
            BookingConfirmation bookingConfirmation = buildBookingConfirmation(logiBkg);
            marshaller.marshal(bookingConfirmation, file);
        } catch (JAXBException exception) {
            log.error(exception);
        }
        return file;
    }

    public BookingConfirmation buildBookingConfirmation(BookingEnvelope logiBkg) {
        ObjectFactory objectFactory = new ObjectFactory();
        BookingConfirmation bookingConfirmation = objectFactory.createBookingConfirmation();
        BookingConfirmation.ConfirmationEnvelope envelope = objectFactory.createBookingConfirmationConfirmationEnvelope();
        List<BookingConfirmation.ConfirmationDetails> confirmationDetails = bookingConfirmation.getConfirmationDetails();
        BookingConfirmation.ConfirmationDetails confirmationDetail = objectFactory.createBookingConfirmationConfirmationDetails();
        BookingConfirmation.ConfirmationDetails.CFSOrigin cfsOrigin = objectFactory.createBookingConfirmationConfirmationDetailsCFSOrigin();
        BookingConfirmation.ConfirmationDetails.CFSDestination cfsDestination = objectFactory.createBookingConfirmationConfirmationDetailsCFSDestination();
        envelope.setSenderID(logiBkg.getReceiverId());
        envelope.setReceiverID(logiBkg.getSenderId());
        envelope.setPassword("");
        envelope.setType(CONFIRMATION_TYPE);
        envelope.setVersion(CONFIRMATION_VERSION);
        envelope.setEnvelopeID(logiBkg.getEnvelopeId());
        for (BookingDetail bkg : logiBkg.getBookingDetailList()) {
            confirmationDetail.setBookingNumber(bkg.getBkgNumber());
            confirmationDetail.setCommunicationReference(bkg.getCommunicationReference());
            confirmationDetail.setCustomerReference(bkg.getCustomerReference());
            confirmationDetail.setRemarks(bkg.getRemarks1());
            if (null != bkg.getSailingDetailList() && !bkg.getSailingDetailList().isEmpty()) {
                for (SailingDetail sailingDetail : bkg.getSailingDetailList()) {
                    confirmationDetail.setVesselVoyageID(sailingDetail.getVesselVoyageId());
                }
            }
            if (null != bkg.getPickupDetailList() && !bkg.getPickupDetailList().isEmpty()) {
                for (PickupDetail pickupDetail : bkg.getPickupDetailList()) {
                    cfsOrigin.setAddress(pickupDetail.getAddress());
                    cfsOrigin.setCity(pickupDetail.getCity());
                    cfsOrigin.setCombinedCompanyNameandAddress(pickupDetail.getCombinedAddress());
                    cfsOrigin.setCompanyName(pickupDetail.getCompanyName());
                    cfsOrigin.setContact(pickupDetail.getContact());
                    cfsOrigin.setCountry(pickupDetail.getCountry());
                    cfsOrigin.setEmail(pickupDetail.getEmail());
                    cfsOrigin.setPhone(pickupDetail.getPhone());
                    cfsOrigin.setPostalCode(pickupDetail.getZip());
                    cfsOrigin.setStateProvince(pickupDetail.getState());
                    cfsDestination.setAddress(pickupDetail.getAddress());
                    cfsDestination.setCity(pickupDetail.getCity());
                    cfsDestination.setCombinedCompanyNameandAddress(pickupDetail.getCombinedAddress());
                    cfsDestination.setCompanyName(pickupDetail.getCompanyName());
                    cfsDestination.setContact(pickupDetail.getContact());
                    cfsDestination.setCountry(pickupDetail.getCountry());
                    cfsDestination.setEmail(pickupDetail.getEmail());
                    cfsDestination.setPhone(pickupDetail.getPhone());
                    cfsDestination.setPostalCode(pickupDetail.getZip());
                    cfsDestination.setStateProvince(pickupDetail.getState());
                    confirmationDetail.setCFSOrigin(cfsOrigin);
                    confirmationDetail.setCFSDestination(cfsDestination);
                }
            }
            confirmationDetails.add(confirmationDetail);
        }
        bookingConfirmation.setConfirmationEnvelope(envelope);
        return bookingConfirmation;
    }

    public byte[] getConfirmation(File file) {
        byte[] bcBytes = new byte[1024];
        InputStream bookingConfirmationInput = null;
        ByteArrayOutputStream bookingConfirmationOutput = null;
        try {
            bookingConfirmationInput = new BufferedInputStream(new FileInputStream(file));
            bookingConfirmationOutput = new ByteArrayOutputStream();
            for (int nRead; (nRead = bookingConfirmationInput.read(bcBytes)) != -1;) {
                bookingConfirmationOutput.write(bcBytes, 0, nRead);
            }
        } catch (FileNotFoundException exception) {
            log.error(exception);
        } catch (IOException exception) {
            log.error(exception);
        }
        return bookingConfirmationOutput.toByteArray();
    }
}
