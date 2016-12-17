package com.gp.cong.logisoft.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.struts.util.LabelValueBean;

import com.gp.cong.logisoft.beans.EdiBean;
import com.gp.cong.logisoft.domain.EdiAck;
import com.gp.cong.logisoft.domain.LogFileEdi;
import com.gp.cong.logisoft.domain.SedSchedulebDetails;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.SedSchedulebDetailsDAO;
import com.gp.cong.logisoft.struts.form.SedFilingForm;
import com.logiware.edi.entity.PackageDetails;
import com.logiware.edi.entity.Party;
import com.logiware.edi.entity.Shipment;
import com.logiware.edi.tracking.EdiSystemBean;
import com.logiware.edi.tracking.EdiTrackingSystem;
import com.logiware.edi.tracking.KnShippingInstruction;
import java.util.HashSet;
import java.util.Set;

public class EdiUtil {

    public List<LabelValueBean> getMessageTypeList() {
        List<LabelValueBean> messageTypeList = new ArrayList<LabelValueBean>();
        messageTypeList.add(new LabelValueBean("Select Message Type", ""));
        messageTypeList.add(new LabelValueBean("300", "300"));
        messageTypeList.add(new LabelValueBean("304", "304"));
        messageTypeList.add(new LabelValueBean("301", "301"));
        messageTypeList.add(new LabelValueBean("315", "315"));
        messageTypeList.add(new LabelValueBean("997", "997"));
        messageTypeList.add(new LabelValueBean("310", "310"));
        return messageTypeList;
    }

    public List<LabelValueBean> getCompanyTypeList() {
        List<LabelValueBean> companyTypeList = new ArrayList<LabelValueBean>();
        companyTypeList.add(new LabelValueBean("Select Company Type", ""));
        companyTypeList.add(new LabelValueBean("GTNEXUS", "G"));
        companyTypeList.add(new LabelValueBean("INTTRA", "I"));
        companyTypeList.add(new LabelValueBean("KUEHNENAGEL", "k"));
        return companyTypeList;
    }

    public List getEdiBeanList(List ediFileList) throws Exception {
        List ediBeanList = new ArrayList();
        for (Object ediFileList1 : ediFileList) {
            LogFileEdi logFile = (LogFileEdi) ediFileList1;
            if (logFile.getAckname() != null && logFile.getAckname().size() > 0) {
                Iterator itr = logFile.getAckname().iterator();
                if (itr.hasNext()) {
                    EdiBean ediBean = new EdiBean();
                    EdiAck ediAck = (EdiAck) itr.next();
                    ediBean.setId(logFile.getId());
                    ediBean.setDrNumber(logFile.getDrnumber());
                    ediBean.setMessageType(logFile.getMessageType());
                    ediBean.setDescription(logFile.getDescription());
                    if (logFile.getEdiCompany() != null) {
                        if (logFile.getEdiCompany().trim().equals("G")) {
                            ediBean.setEdiCompany("GTNEXUS");
                        }
                        if (logFile.getEdiCompany().trim().equals("I")) {
                            ediBean.setEdiCompany("INTTRA");
                        }
                        if (logFile.getEdiCompany().trim().equals("K")) {
                            ediBean.setEdiCompany("KIMBERLY CLARK");
                        }
                    }
                    ediBean.setFileName(logFile.getFilename());
                    ediBean.setProcessedDate(logFile.getProcessedDate());
                    ediBean.setStatus(logFile.getStatus());
                    if (ediBean.getStatus().equals("conditionaccepted")) {
                        ediBean.setStatus("conditionlly accepted");
                    }
                    ediBean.setAckRecievedDate(ediAck.getAckReceivedDate());
                    if (ediAck.getSeverity() != null) {
                        if (ediAck.getSeverity().trim().equals("1")) {
                            ediBean.setSeverity("ACCEPTED");
                        }
                        if (ediAck.getSeverity().trim().equals("2")) {
                            ediBean.setSeverity("WARNING");
                        }
                        if (ediAck.getSeverity().trim().equals("3")) {
                            ediBean.setSeverity("REJECTED");
                        }
                    }
                    ediBean.setBookingNumber(ediAck.getBookingNumber());
                    ediBean.setScacCode(ediAck.getCarrierScac());
                    ediBeanList.add(ediBean);
                }
            } else {
                EdiBean ediBean = new EdiBean();
                if ("997".equals(logFile.getMessageType())) {
                    if (logFile.getAckStatus() != null) {
                        ediBean.setSeverity(logFile.getAckStatus());
                    }
                    ediBean.setAckRecievedDate(logFile.getProcessedDate());
                }
                ediBean.setBookingNumber(logFile.getBookingNumber());
                ediBean.setId(logFile.getId());
                ediBean.setDrNumber(logFile.getDrnumber());
                ediBean.setMessageType(logFile.getMessageType());
                ediBean.setDescription(logFile.getDescription());
                if (logFile.getEdiCompany() != null) {
                    if (logFile.getEdiCompany().trim().equals("G")) {
                        ediBean.setEdiCompany("GTNEXUS");
                    }
                    if (logFile.getEdiCompany().trim().equals("I")) {
                        ediBean.setEdiCompany("INTTRA");
                    }
                    if (logFile.getEdiCompany().trim().equals("K")) {
                        ediBean.setEdiCompany("KIMBERLY CLARK");
                    }
                }
                ediBean.setFileName(logFile.getFilename());
                if ("315".equals(logFile.getMessageType())) {
                    ediBean.setEventName(logFile.getEventName());
                }
                ediBean.setProcessedDate(logFile.getProcessedDate());
                ediBean.setStatus(logFile.getStatus());
                if(ediBean.getStatus().equals("conditionaccepted")){
                    ediBean.setStatus("conditionally accepted");
                }
                ediBeanList.add(ediBean);
            }
        }
        return ediBeanList;
    }

    public List<EdiSystemBean> getEdiTrackingBeanList(List ediFileList) throws Exception {
        List ediBeanList = new ArrayList();
        for (int i = 0; i < ediFileList.size(); i++) {
            EdiTrackingSystem logFile = (EdiTrackingSystem) ediFileList.get(i);
            if (logFile.getAckname() != null && logFile.getAckname().size() > 0) {
                Iterator itr = logFile.getAckname().iterator();
                while (itr.hasNext()) {
                    EdiSystemBean ediBean = new EdiSystemBean();
                    EdiAck ediAck = (EdiAck) itr.next();
                    ediBean.setId(logFile.getId());
                    ediBean.setDrNumber(logFile.getDrnumber());
                    ediBean.setMessageType(logFile.getMessageType());
                    ediBean.setDescription(logFile.getDescription());
                    if (logFile.getEdiCompany() != null) {
                        if (logFile.getEdiCompany().trim().equals("G")) {
                            ediBean.setEdiCompany("GTNEXUS");
                        }
                        if (logFile.getEdiCompany().trim().equals("I")) {
                            ediBean.setEdiCompany("INTTRA");
                        }
                        if (logFile.getEdiCompany().trim().equals("K")) {
                            ediBean.setEdiCompany(new EdiDAO().getKNCompanyName(logFile.getBookingNo()));
                        }
                    }
                    ediBean.setFileName(logFile.getFilename());
                    ediBean.setProcessedDate(logFile.getProcessedDate());
                    ediBean.setStatus(logFile.getStatus());
                    ediBean.setAckRecievedDate(ediAck.getAckReceivedDate());
                    if (ediAck.getSeverity() != null) {
                        if (ediAck.getSeverity().trim().equals("1")) {
                            ediBean.setSeverity("ACCEPTED");
                        }
                        if (ediAck.getSeverity().trim().equals("2")) {
                            ediBean.setSeverity("WARNING");
                        }
                        if (ediAck.getSeverity().trim().equals("3")) {
                            ediBean.setSeverity("REJECTED");
                        }
                    }
                    ediBean.setBookingNumber(ediAck.getBookingNumber());
                    ediBean.setScacCode(ediAck.getCarrierScac());
                    ediBean.setEdiStatus(logFile.getEdiStatus());
                    ediBean.setTransportService(logFile.getTransportService());
                    ediBean.setTransactionStatus(logFile.getTransactionStatus());
                    ediBeanList.add(ediBean);
                }
            } else {
                EdiSystemBean ediBean = new EdiSystemBean();
                ediBean.setId(logFile.getId());
                ediBean.setDrNumber(logFile.getDrnumber());
                ediBean.setMessageType(logFile.getMessageType());
                ediBean.setDescription(logFile.getDescription());
                if (logFile.getEdiCompany() != null) {
                    if (logFile.getEdiCompany().trim().equals("G")) {
                        ediBean.setEdiCompany("GTNEXUS");
                    }
                    if (logFile.getEdiCompany().trim().equals("I")) {
                        ediBean.setEdiCompany("INTTRA");
                    }
                    if (logFile.getEdiCompany().trim().equals("K")) {
                        ediBean.setEdiCompany(new EdiDAO().getKNCompanyName(logFile.getBookingNo()));
                    }
                }
                ediBean.setFileName(logFile.getFilename());
                ediBean.setProcessedDate(logFile.getProcessedDate());
                ediBean.setStatus(logFile.getStatus());
                ediBean.setBookingNumber(logFile.getBookingNo());
                ediBean.setPortOfLoad(logFile.getPortOfLoad());
                ediBean.setPortOfDischarge(logFile.getPortOfDischarge());
                ediBean.setPlaceOfReceipt(logFile.getPlaceOfReceipt());
                ediBean.setPlaceOfDelivery(logFile.getPlaceOfDelivery());
                ediBean.setPortOfLoadCity(logFile.getPortOfLoadCity());
                ediBean.setPortOfDischargeCity(logFile.getPortOfDischargeCity());
                ediBean.setPlaceOfReceiptCity(logFile.getPlaceOfReceiptCity());
                ediBean.setPlaceOfDeliveryCity(logFile.getPlaceOfDeliveryCity());
                ediBean.setAckRecievedDate(logFile.getAckCreatedDate());
                ediBean.setAckStatus(logFile.getAckStatus());
                ediBean.setEdiStatus(logFile.getEdiStatus());
                ediBean.setTransportService(logFile.getTransportService());
                ediBean.setTransactionStatus(logFile.getTransactionStatus());
                ediBean.setRequestor(logFile.getRequestor());
                ediBeanList.add(ediBean);
            }
        }
        return ediBeanList;
    }

    public void saveSed(SedFilingForm sedFilingForm) throws Exception {
        SedSchedulebDetails sedSchedulebDetails = new SedSchedulebDetails();
        SedSchedulebDetailsDAO sedSchedulebDetailsDAO = new SedSchedulebDetailsDAO();
        sedSchedulebDetails.setScheduleBName(sedFilingForm.getScheduleB_Name());
        sedSchedulebDetails.setScheduleBNumber(sedFilingForm.getScheduleB_Number());
        sedSchedulebDetails.setDomesticOrForeign(sedFilingForm.getDomesticOrForeign());
        sedSchedulebDetails.setDescription1(sedFilingForm.getDescription1());
        sedSchedulebDetails.setDescription2(sedFilingForm.getDescription2());
        sedSchedulebDetails.setQuantities1(sedFilingForm.getQuantities1());
        sedSchedulebDetails.setQuantities2(sedFilingForm.getQuantities2());
        sedSchedulebDetails.setUnits1(sedFilingForm.getUnits1());
        sedSchedulebDetails.setUnits2(sedFilingForm.getUnits2());
        sedSchedulebDetails.setWeight(sedFilingForm.getWeight());
        sedSchedulebDetails.setWeightType(sedFilingForm.getWeightType());
        sedSchedulebDetails.setValue(sedFilingForm.getValue());
        sedSchedulebDetails.setExportInformationCode(sedFilingForm.getExportInformationCode());
        sedSchedulebDetails.setExportLicense(sedFilingForm.getExportLicense());
        sedSchedulebDetails.setLicenseType(sedFilingForm.getLicenseType());
        sedSchedulebDetails.setEccn(sedFilingForm.getEccn());
        sedSchedulebDetails.setUsedVehicle(sedFilingForm.getUsedVehicle());
        sedSchedulebDetails.setVehicleIdNumber(sedFilingForm.getVehicleIdNumber());
        sedSchedulebDetails.setVehicleIdType(sedFilingForm.getVehicleIdType());
        sedSchedulebDetails.setVehicleState(sedFilingForm.getVehicleState());
        sedSchedulebDetails.setVehicleTitleNumber(sedFilingForm.getVehicleTitleNumber());
        sedSchedulebDetails.setShipment(sedFilingForm.getShpdr());
        sedSchedulebDetails.setEntnam(sedFilingForm.getEntnam());
        sedSchedulebDetailsDAO.save(sedSchedulebDetails);

    }
    
    public Shipment formatKnShippingInstructionToShipment(Shipment shipmentInstruction, KnShippingInstruction knShippingInstruction) {
        Shipment shipment = new Shipment();
        Set<Party> partys = new HashSet<>();
        Set<PackageDetails> packageDetailses = new HashSet<>();
        Party party = new Party();
        String[] placeOfCity;
        PackageDetails packageDetails =new PackageDetails();
        if (null != knShippingInstruction) {
            shipment.setBookingNo(knShippingInstruction.getBkgNumber());
            shipment.setSenderId(knShippingInstruction.getVesselVoyageId());
            shipment.setSenderId(knShippingInstruction.getSenderId());
            shipment.setReceiverId(knShippingInstruction.getReceiverId());
            shipment.setSchemaVersion(knShippingInstruction.getVersion());
            shipment.setVesselName(knShippingInstruction.getVesselName());
            shipment.setVoyageNumber(knShippingInstruction.getVoyage());
            shipment.setPlaceOfReceipt(knShippingInstruction.getPlaceOfReceipt());
            shipment.setPortOfLoad(knShippingInstruction.getPortOfLoading());
            shipment.setPlaceOfDelivery(knShippingInstruction.getPlaceOfDelivery());
            shipment.setBlOrigin(knShippingInstruction.getCfsOrigin());
            shipment.setExportRefNo(knShippingInstruction.getCustomerReference());
            shipment.setReleaseDate(knShippingInstruction.getDateOfIssue());
            shipment.setPlaceOfDischarge(knShippingInstruction.getPortOfDischarge());
            shipment.setGrossWeight(knShippingInstruction.getWeight());
            shipment.setGrossVolume(knShippingInstruction.getVolume());
            shipment.setNumberOfPackage(knShippingInstruction.getPackageCount());
            shipment.setNumberOfEquipment(1);
            shipment.setTransportService("LCL");
            //M -> CBM and KGS
            if("M".equals(knShippingInstruction.getUom())) {
            shipment.setGrossWeightType("KGM");
            shipment.setGrossVolumeType("MTQ");
            } else {
            shipment.setGrossVolumeType("FTQ");
            shipment.setGrossWeightType("LBS");
            }
            if(null !=knShippingInstruction.getPlaceOfIssue()) {
            placeOfCity =knShippingInstruction.getPlaceOfIssue().split(",");
            shipment.setReleaseOfficeCity(placeOfCity[0]);
            }
            if (null != knShippingInstruction.getCnCustomerName()) {
                party.setRole("Consignee");
                party.setName(knShippingInstruction.getCnCustomerName());
                party.setAddress(knShippingInstruction.getCnAddress());
                partys.add(party);
                party=new Party();
            }
            if (null != knShippingInstruction.getShCustomerName()) {
                party.setRole("Shipper");
                party.setName(knShippingInstruction.getShCustomerName());
                party.setAddress(knShippingInstruction.getShAddress());
                partys.add(party);
                party=new Party();
            }
            if (null != knShippingInstruction.getFfCustomerName()) {
                party.setRole("Forwarder");
                party.setName(knShippingInstruction.getFfCustomerName());
                party.setAddress(knShippingInstruction.getFfAddress());
                partys.add(party);
                party=new Party();
            }
            if (null != knShippingInstruction.getFfCustomerName()) {
                party.setRole("Forwarder");
                party.setName(knShippingInstruction.getFfCustomerName());
                party.setAddress(knShippingInstruction.getFfAddress());
                partys.add(party);
                party=new Party();
            }
            if (null != knShippingInstruction.getNiCustomerName()) {
                party.setRole("MainNotifyParty");
                party.setName(knShippingInstruction.getNiCustomerName());
                party.setAddress(knShippingInstruction.getNiAddress());
                partys.add(party);
            }
            packageDetails.setNoOfPackage(knShippingInstruction.getPackageCount());
            packageDetails.setPackageTypeDesc(knShippingInstruction.getPackagType());
            packageDetails.setGoodgrossVolume(knShippingInstruction.getVolume());
            packageDetails.setGoodgrossWeight(knShippingInstruction.getWeight());
            packageDetails.setCommodity(knShippingInstruction.getCommodity());
            packageDetails.setMarksAndNo(knShippingInstruction.getMarks());
            //M -> CBM and KGS
            if("M".equals(knShippingInstruction.getUom())) {
            packageDetails.setGoodgrossWeightType("KGM");
            packageDetails.setGoodgrossVolumeType("MTQ");
            } else {
            packageDetails.setGoodgrossVolumeType("FTQ");
            packageDetails.setGoodgrossWeightType("LBS");
            }
            packageDetailses.add(packageDetails);
            shipment.setPartySet(partys);
            shipment.setPackageDetailsSet(packageDetailses);
        }
        return shipment;
    }
}
