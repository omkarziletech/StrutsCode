package com.gp.cong.logisoft.bc.fcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.LogFileEdi;
import com.gp.cong.logisoft.domain.Shpsta;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import java.util.Date;

public class EdiTrackingBC implements LclCommonConstant {

    public String getEdiList(String drNumber) throws Exception {
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        String status = logFileEdiDAO.findDrNumberStatus(drNumber, "success");
        return status;
    }

    public void setEdiLogs(String filename, String processedDate, String status, String description,
            String ediCompany, String messageType, String drNumber, String bookingNumber,
            String ackStatus, Date ackReceivedDate) throws Exception {
        LogFileEdi logFileEdi = new LogFileEdi();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        logFileEdi.setFilename(null != filename ? filename : "");
        logFileEdi.setProcessedDate(null != processedDate ? processedDate : "");
        logFileEdi.setStatus(null != status ? status : "");
        logFileEdi.setDescription(null != description ? description : "");
        logFileEdi.setEdiCompany(null != ediCompany ? ediCompany : "");
        logFileEdi.setMessageType(null != messageType ? messageType : "");
        logFileEdi.setDrnumber(null != drNumber ? drNumber : "");
        logFileEdi.setBookingNumber(null != bookingNumber ? bookingNumber : "");
        logFileEdi.setAckStatus(null != ackStatus ? ackStatus : "");
        logFileEdi.setAckReceivedDate(ackReceivedDate);
        logFileEdi.setFileNo(drNumber);
        logFileEdiDAO.saveLogFileEdi(logFileEdi);
    }

    public void setEdiLog(String filename, String processedDate, String status,
            String description, String ediCompany, String messageType, String drNumber, String bookingNumber,
            String ackStatus, Date ackReceivedDate) throws Exception {
        LogFileEdi logFileEdi = new LogFileEdi();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        logFileEdi.setFilename(null != filename ? filename : "");
        logFileEdi.setProcessedDate(null != processedDate ? processedDate : "");
        logFileEdi.setStatus(null != status ? status : "");
        logFileEdi.setDescription(null != description ? description : "");
        logFileEdi.setEdiCompany(null != ediCompany ? ediCompany : "");
        logFileEdi.setMessageType(null != messageType ? messageType : "");
        logFileEdi.setDrnumber(null != drNumber ? "04" + drNumber : "");
        logFileEdi.setBookingNumber(null != bookingNumber ? bookingNumber : "");
        logFileEdi.setAckStatus(null != ackStatus ? ackStatus : "");
        logFileEdi.setAckReceivedDate(ackReceivedDate);
        logFileEdi.setFileNo(drNumber);
        logFileEdiDAO.saveLogFileEdi(logFileEdi);
    }

    public void setEdiLogForBooking(String filename, String processedDate, String status, String description, String ediCompany, String messageType, String drNumber, String bookingNumber, String ackStatus, Date ackReceivedDate) throws Exception {
        LogFileEdi logFileEdi = new LogFileEdi();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        logFileEdi.setFilename(null != filename ? filename : "");
        logFileEdi.setProcessedDate(null != processedDate ? processedDate : "");
        logFileEdi.setStatus(null != status ? status : "");
        logFileEdi.setDescription(null != description ? description : "");
        logFileEdi.setEdiCompany(null != ediCompany ? ediCompany : "");
        logFileEdi.setMessageType(null != messageType ? messageType : "");
        logFileEdi.setDrnumber(null != drNumber ? "04" + drNumber : "");
        logFileEdi.setBookingNumber(null != bookingNumber ? bookingNumber : "");
        logFileEdi.setAckStatus(null != ackStatus ? ackStatus : "");
        logFileEdi.setAckReceivedDate(ackReceivedDate);
        logFileEdi.setFileNo(drNumber);
        logFileEdi.setDocType("Booking");
        logFileEdiDAO.saveLogFileEdi(logFileEdi);
    }

    public void setEdiLogCts(String filename, String processedDate, String status, String description, String ediCompany, String messageType, String drNumber, String bookingNumber, String ackStatus, Date ackReceivedDate) throws Exception {
        LogFileEdi logFileEdi = new LogFileEdi();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        logFileEdi.setFilename(null != filename ? filename : "");
        logFileEdi.setProcessedDate(null != processedDate ? processedDate : "");
        logFileEdi.setStatus(null != status ? status : "");
        logFileEdi.setDescription(null != description ? description : "");
        logFileEdi.setEdiCompany(null != ediCompany ? ediCompany : "");
        logFileEdi.setMessageType(null != messageType ? messageType : "");
        logFileEdi.setDrnumber(null != drNumber ? "04" + drNumber : "");
        logFileEdi.setBookingNumber(null != bookingNumber ? bookingNumber : "");
        logFileEdi.setAckStatus(null != ackStatus ? ackStatus : "");
        logFileEdi.setAckReceivedDate(ackReceivedDate);
        logFileEdi.setFileNo(drNumber);
        logFileEdiDAO.saveLogFileEdi(logFileEdi);
    }

    public void setShipmentStatusLog(String filename, String processedDate, String status, String description, String ediCompany, String messageType, String drNumber, String trackingStatus, Shpsta shpsta) throws Exception {
        String eventName = null;
        String containerStatus = null;
        LogFileEdi logFileEdi = new LogFileEdi();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        logFileEdi.setFilename(null != filename ? filename : "");
        logFileEdi.setProcessedDate(null != processedDate ? processedDate : "");
        logFileEdi.setStatus(null != status ? status : "");
        logFileEdi.setDescription(null != description ? description : "");
        logFileEdi.setEdiCompany(null != ediCompany ? ediCompany : "");
        logFileEdi.setMessageType(null != messageType ? messageType : "");
        logFileEdi.setDrnumber(null != drNumber ? "04" + drNumber : "");
        logFileEdi.setBookingNumber(null != shpsta.getBkgnum() ? shpsta.getBkgnum() : "");
        logFileEdi.setTrackingStatus(null != trackingStatus ? trackingStatus : "");
        logFileEdi.setFileNo(drNumber);
        logFileEdi.setEventCode(null != shpsta.getEvncod() ? shpsta.getEvncod() : "");
        containerStatus = null != shpsta.getCntsta() ? shpsta.getCntsta() : "";
        if (CommonUtils.isNotEmpty(shpsta.getCntsta())) {
            if ("Load/Full".equalsIgnoreCase(shpsta.getCntsta())) {
                containerStatus = "L";
            } else if ("Empty".equalsIgnoreCase(shpsta.getCntsta())) {
                containerStatus = "E";
            }
        }
        if ("I".equalsIgnoreCase(ediCompany)) {
            eventName = logFileEdiDAO.findTrackingStatus(shpsta.getEvncod(), "", "", ediCompany);
        } else {
            eventName = logFileEdiDAO.findTrackingStatus(shpsta.getEvncod(), containerStatus, shpsta.getEvnlfn(), ediCompany);
        }
        logFileEdi.setEventName(null != eventName ? eventName : "");
        logFileEdiDAO.saveLogFileEdi(logFileEdi);
    }

    public void setEdiLogCtsforLcl(String filename, String processedDate, String status, String description, String ediCompany, String messageType, String drNumber, String bookingNumber, String ackStatus, Date ackReceivedDate, String fileNo) throws Exception {
        LogFileEdi logFileEdi = new LogFileEdi();
        LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
        logFileEdi.setFilename(null != filename ? filename : "");
        logFileEdi.setProcessedDate(null != processedDate ? processedDate : "");
        logFileEdi.setStatus(null != status ? status : "");
        logFileEdi.setDescription(null != description ? description : "");
        logFileEdi.setEdiCompany(null != ediCompany ? ediCompany : "");
        logFileEdi.setMessageType(null != messageType ? messageType : "");
        logFileEdi.setDrnumber(null != drNumber ? drNumber : "");
        logFileEdi.setBookingNumber(null != bookingNumber ? bookingNumber : "");
        logFileEdi.setAckStatus(null != ackStatus ? ackStatus : "");
        logFileEdi.setAckReceivedDate(ackReceivedDate);
        logFileEdi.setFileNo(null != fileNo ? fileNo : "");
        logFileEdiDAO.saveLogFileEdi(logFileEdi);
    }

    public void setPickupDetails(long fileNumberId, String doorDeliveryStatus, String ProNumber,
            String PODSigned, String PODDateTime, User user, String status, String doorDelivery) throws Exception {
        LclBookingImport lclBookingImport = new LclBookingImportDAO().findById(fileNumberId);
        LclFileNumberDAO fileDao = new LclFileNumberDAO();
        LclFileNumber lclFileNumber = fileDao.findById(fileNumberId);
        LclBookingPadDAO bookingPadDAO = new LclBookingPadDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        StringBuilder remarks = new StringBuilder();
        StringBuilder remarks1 = new StringBuilder();
        if (CommonUtils.isNotEmpty(doorDeliveryStatus) || CommonUtils.isNotEmpty(PODSigned) || CommonUtils.isNotEmpty(PODDateTime)) {
            if (CommonUtils.isNotEmpty(doorDeliveryStatus)) {
                if (CommonUtils.isNotEmpty(lclBookingImport.getDoorDeliveryStatus())) {
                    if (lclBookingImport.getDoorDeliveryStatus().equalsIgnoreCase("P") && !lclBookingImport.getDoorDeliveryStatus().equals(doorDeliveryStatus)) {
                        remarks.append("Door Delivery Status -> ").append("Pending(Cargo at CFS) to ").append(doorDelivery).append("\\N");
                    } else if (lclBookingImport.getDoorDeliveryStatus().equalsIgnoreCase("O") && !lclBookingImport.getDoorDeliveryStatus().equals(doorDeliveryStatus)) {
                        remarks.append("Door Delivery Status -> ").append("Out For Delivery to ").append(doorDelivery).append("\\N");

                    } else if (lclBookingImport.getDoorDeliveryStatus().equalsIgnoreCase("D") && !lclBookingImport.getDoorDeliveryStatus().equals(doorDeliveryStatus)) {
                        remarks.append("Door Delivery Status -> ").append("Delivered to ").append(doorDelivery).append("\\N");

                    } else if (lclBookingImport.getDoorDeliveryStatus().equalsIgnoreCase("F") && !lclBookingImport.getDoorDeliveryStatus().equals(doorDeliveryStatus)) {
                        remarks.append("Door Delivery Status -> ").append("Final/Closed to ").append(doorDelivery).append("\\N");
                    }

                } else {
                    remarks.append("Door Delivery Status -> ").append(" to ").append(doorDelivery).append("\\N");

                }
            }
            lclBookingImport.setDoorDeliveryStatus(doorDeliveryStatus);

            if (!"N".equalsIgnoreCase(status)) {
                if (CommonUtils.isEmpty(lclBookingImport.getPodSignedBy())) {
                    if (CommonUtils.isNotEmpty(PODSigned)) {
                        lclBookingImport.setPodSignedBy(PODSigned);
                        remarks.append("POD Signed ->").append(PODSigned).append("\\N");
                    }
                }
                if (lclBookingImport.getPodSignedDatetime() == null) {
                    if (CommonUtils.isNotEmpty(PODDateTime)) {
                        lclBookingImport.setPodSignedDatetime(DateUtils.parseStringToDateWithTime(PODDateTime));
                        remarks.append("POD Date ->").append(PODDateTime).append("\\N");
                    }
                }
            }
            lclBookingImport.setModifiedBy(user);
            lclBookingImport.setEnteredBy(user);
            lclBookingImport.setModifiedDatetime(new Date());
            lclBookingImport.setEnteredDatetime(new Date());
            if (CommonUtils.isNotEmpty(remarks)) {
                lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_DR_AUTO_NOTES, remarks.toString(), user);
            }
            new LclBookingImportDAO().saveOrUpdate(lclBookingImport);
        }
        if (CommonUtils.isNotEmpty(ProNumber) && !"N".equalsIgnoreCase(status)) {
            LclBookingPad lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(fileNumberId);
            Date now = new Date();
            if (lclBookingPad == null) {
                lclBookingPad = new LclBookingPad();
                lclBookingPad.setDeliveryContact(new LclContact(null, "", now, now, user, user, lclFileNumber));
                lclBookingPad.setPickupContact(new LclContact(null, "", now, now, user, user, lclFileNumber));
                lclBookingPad.setLclFileNumber(lclFileNumber);
                lclBookingPad.setIssuingTerminal(user.getTerminal());
            }
            if (!lclBookingPad.getPickupReferenceNo().equals(ProNumber)) {
                remarks1.append("ProNumber -> ").append(lclBookingPad.getPickupReferenceNo()).append(" to ").append(ProNumber);
                lclRemarksDAO.insertLclRemarks(lclFileNumber, REMARKS_DR_AUTO_NOTES, remarks1.toString(), user);
            }
            lclBookingPad.setPickupReferenceNo(ProNumber);
            lclBookingPad.setEnteredBy(user);
            lclBookingPad.setEnteredDatetime(new Date());
            lclBookingPad.setModifiedBy(user);
            lclBookingPad.setModifiedDatetime(new Date());
            if (lclBookingPad.getId() != null) {
                bookingPadDAO.saveOrUpdate(lclBookingPad);
            } else {
                bookingPadDAO.save(lclBookingPad);
            }
        }
    }
}
