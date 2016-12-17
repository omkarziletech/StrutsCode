package com.gp.cong.logisoft.lcl.kn.xml;

/**
 *
 * @author Rajesh
 */
public class ObjectFactory {

    public void ObjectFactory() {
    }

    public BookingConfirmation.ConfirmationDetails.CFSDestination createBookingConfirmationConfirmationDetailsCFSDestination() {
        return new BookingConfirmation.ConfirmationDetails.CFSDestination();
    }

    public BookingConfirmation.ConfirmationDetails.CFSOrigin createBookingConfirmationConfirmationDetailsCFSOrigin() {
        return new BookingConfirmation.ConfirmationDetails.CFSOrigin();
    }

    public BookingConfirmation.ConfirmationEnvelope createBookingConfirmationConfirmationEnvelope() {
        return new BookingConfirmation.ConfirmationEnvelope();
    }

    public BookingConfirmation.ConfirmationDetails createBookingConfirmationConfirmationDetails() {
        return new BookingConfirmation.ConfirmationDetails();
    }

    public BookingConfirmation createBookingConfirmation() {
        return new BookingConfirmation();
    }
}
