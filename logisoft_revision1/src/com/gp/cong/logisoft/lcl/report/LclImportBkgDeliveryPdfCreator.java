/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceDetail;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingSegregationDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.directwebremoting.WebContextFactory;

/**
 *
 * @author Logiware
 */
public class LclImportBkgDeliveryPdfCreator extends LclReportFormatMethods {

    private LclBooking lclbooking;
    StringBuilder notifyName = new StringBuilder();
    StringBuilder polValues = new StringBuilder();
    StringBuilder podValues = new StringBuilder();
    String notifyAddress = "";
    String notifyPhone = "";
    String notifyFax = "";
    String consigneePhone = "";
    String subHouseBL = "";
    StringBuilder ambHouseBL = new StringBuilder();
    String consigneeFax = "";
    String shipperPhone = "";
    String shipperFax = "";
    String whsePhone = "";
    String whseFax = "";
    String dims = "";
    String itNumber = "";
    String unitsNumber = "";
    String vesselName = "";
    String sailDate = "";
    String arrivalDate = "";
    String lastFreeDay = "";
    String approximateDate = "";
    String strippingDate = "";
    String shipFromContact = "";
    String shipFromPhone = "";
    String shipFromFax = "";
    String shipToContact = "";
    String shipToPhone = "";
    String shipToFax = "";
    String shipToHours = "";
    String goDate = "";
    String specialInstructions = "";
    String commodityDescription = "";
    String pickupInstructions = "";
    String pickupNumber = "";
    String pickupReferenceNumber = "";
    String eta = "";
    String scac = "";
    String carrierName = "";
    String masterbl = "";
    StringBuilder consigneeName = new StringBuilder();
    StringBuilder fileNumber = new StringBuilder();
    StringBuilder shipperName = new StringBuilder();
    StringBuilder shipToDetails = new StringBuilder();
    StringBuilder notifyCity = new StringBuilder();
    StringBuilder whseDetails = new StringBuilder();

    public LclImportBkgDeliveryPdfCreator(LclBooking lclbooking) throws Exception {
        this.lclbooking = lclbooking;
        LclContact lclContact = new LclContact();
        LCLContactDAO lclContactDAO = new LCLContactDAO();
        List<LclBookingImportAms> lclBookingImportAmsList = null;
        LclBookingImport lclBookingImport = new LclBookingImportDAO().getByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
        LclBookingPad lclBookingPad = new LclBookingPadDAO().getByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
        Boolean isSegregationFlag = new LclBookingSegregationDao().isCheckedSegregationDr(lclbooking.getLclFileNumber().getId());
        if (isSegregationFlag) {
            lclBookingImportAmsList = new LclBookingImportAmsDAO().findBysSegFileNumberId(lclbooking.getLclFileNumber().getId());
        } else {
            lclBookingImportAmsList = new LclBookingImportAmsDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
        }
        Collections.sort(lclBookingImportAmsList, new Comparator<LclBookingImportAms>() {

            public int compare(LclBookingImportAms o1, LclBookingImportAms o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
        if (lclbooking.getPooPickup() && lclBookingPad != null && CommonFunctions.isNotNull(lclBookingPad.getLastFreeDate())) {
            lastFreeDay = DateUtils.formatDate(lclBookingPad.getLastFreeDate(), "dd-MMM-yyyy");
        } else if (lclBookingImport != null && CommonFunctions.isNotNull(lclBookingImport.getLastFreeDateTime())) {
            lastFreeDay = DateUtils.formatDate(lclBookingImport.getLastFreeDateTime(), "dd-MMM-yyyy");
        }
       
        int count = 0;
        if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() > 0) {
            for (int j = 0; j < lclBookingImportAmsList.size(); j++) {
                if (count <= 4) {
                    LclBookingImportAms lclBookingImportAms = (LclBookingImportAms) lclBookingImportAmsList.get(j);
                    if (CommonFunctions.isNotNull(lclBookingImportAms.getAmsNo())) {
                        ambHouseBL.append(lclBookingImportAms.getAmsNo());
                        count++;
                        if (count != lclBookingImportAmsList.size() && count != 5) {
                            ambHouseBL.append(",");
                        }
                    }
                }
            }
        }
        if (CommonFunctions.isNotNull(lclBookingPad)) {
            if (CommonFunctions.isNotNull(lclBookingPad.getScac())) {
                scac = lclBookingPad.getScac();
//                carrierName = new LclBookingPadDAO().getCarrierNameUsingScac(lclBookingPad.getScac());
                carrierName = new LclBookingPadDAO().getCarrierNameUsingScac(lclBookingPad.getScac());
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryReferenceNo())) {
                pickupNumber = lclBookingPad.getDeliveryReferenceNo();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupReferenceNo())) {
                pickupReferenceNumber = lclBookingPad.getPickupReferenceNo();
            }
            if (lclBookingImport != null && CommonFunctions.isNotNull(lclBookingImport.getSubHouseBl())) {
                subHouseBL = lclBookingImport.getSubHouseBl();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryInstructions())) {
                specialInstructions = lclBookingPad.getDeliveryInstructions().toUpperCase();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getCommodityDesc())) {
                commodityDescription = lclBookingPad.getCommodityDesc().toUpperCase();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact())
                    && CommonFunctions.isNotNull(lclBookingPad.getPickupContact().getCompanyName())) {
                shipToDetails.append(lclBookingPad.getPickupContact().getCompanyName()).append("\n");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact())
                    && CommonFunctions.isNotNull(lclBookingPad.getPickupContact().getAddress())) {
                shipToDetails.append(lclBookingPad.getPickupContact().getAddress()).append("\n");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact())
                    && CommonFunctions.isNotNull(lclBookingPad.getPickupContact().getCity())) {
                shipToDetails.append(lclBookingPad.getPickupContact().getCity()).append("   ");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact())
                    && CommonFunctions.isNotNull(lclBookingPad.getPickupContact().getState())) {
                shipToDetails.append(lclBookingPad.getPickupContact().getState()).append("   ");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact())
                    && CommonFunctions.isNotNull(lclBookingPad.getPickupContact().getZip())) {
                shipToDetails.append(lclBookingPad.getPickupContact().getZip()).append("   ");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact()) && CommonFunctions.isNotNull(lclBookingPad.getPickupContact().getContactName())) {
                shipToContact = lclBookingPad.getPickupContact().getContactName();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact()) && CommonFunctions.isNotNull(lclBookingPad.getPickupContact().getPhone1())) {
                shipToPhone = lclBookingPad.getPickupContact().getPhone1();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact()) && CommonFunctions.isNotNull(lclBookingPad.getPickupContact().getFax1())) {
                shipToFax = lclBookingPad.getPickupContact().getFax1();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupContact()) && CommonFunctions.isNotNull(lclBookingPad.getPickupHours())) {
                shipToHours = lclBookingPad.getPickupHours();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getPickupInstructions())) {
                pickupInstructions = lclBookingPad.getPickupInstructions();
            }
        }
        if (CommonFunctions.isNotNull(lclbooking.getPortOfOrigin()) && CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getUnLocationCode())) {
            fileNumber.append(lclbooking.getPortOfOrigin().getUnLocationCode().substring(2)).append("-");
        }
//        if (CommonFunctions.isNotNull(lclbooking.getFinalDestination()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getUnLocationCode())) {
//            fileNumber.append(lclbooking.getFinalDestination().getUnLocationCode()).append("-");
//        }
        if (CommonFunctions.isNotNull(lclbooking.getLclFileNumber()) && CommonFunctions.isNotNull(lclbooking.getLclFileNumber().getFileNumber())) {
            fileNumber.append(lclbooking.getLclFileNumber().getFileNumber());
        }
        if (CommonFunctions.isNotNull(lclbooking)) {
            if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getUnLocationName())) {
                polValues.append(lclbooking.getPortOfLoading().getUnLocationName());
            }
            if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId().getCodedesc())) {
                polValues.append(",").append(lclbooking.getPortOfLoading().getCountryId().getCodedesc());
            }
            if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationName())) {
                podValues.append(lclbooking.getPortOfDestination().getUnLocationName());
            }
            if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId().getCode())) {
                podValues.append(",").append(lclbooking.getPortOfDestination().getStateId().getCode());
            }
        }

        if (CommonFunctions.isNotNull(lclbooking.getNotyContact()) && CommonFunctions.isNotNull(lclbooking.getNotyContact().getId())) {
            lclContact = lclContactDAO.findById(lclbooking.getNotyContact().getId());

            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getPhone1())) {
                notifyPhone = lclContact.getPhone1();
            }
            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getFax1())) {
                notifyFax = lclContact.getFax1();
            }
            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getCity())) {
                notifyCity.append(lclContact.getCity()).append(" ");
            }
            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getState())) {
                notifyCity.append(lclContact.getState()).append(" ");
            }
            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getZip())) {
                notifyCity.append(lclContact.getZip());
            }
        }
        if (CommonFunctions.isNotNull(lclbooking.getConsContact()) && CommonFunctions.isNotNull(lclbooking.getConsContact().getId())) {
            lclContact = lclContactDAO.findById(lclbooking.getConsContact().getId());

            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getPhone1())) {
                consigneePhone = lclContact.getPhone1();
            }
            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getFax1())) {
                consigneeFax = lclContact.getFax1();
            }
        }
        if (CommonFunctions.isNotNull(lclBookingPad) && CommonFunctions.isNotNull(lclBookingPad.getDeliveryContact())) {
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryContact().getCompanyName())) {
                shipperName.append(lclBookingPad.getDeliveryContact().getCompanyName()).append("\n");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryContact().getAddress())) {
                shipperName.append(lclBookingPad.getDeliveryContact().getAddress()).append("\n");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryContact().getCity())) {
                shipperName.append(lclBookingPad.getDeliveryContact().getCity()).append(", ");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryContact().getState())) {
                shipperName.append(lclBookingPad.getDeliveryContact().getState()).append(" ");
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryContact().getZip())) {
                shipperName.append(lclBookingPad.getDeliveryContact().getZip()).append("\n");
            }
            if (CommonFunctions.isNotNull(lclContact) && CommonFunctions.isNotNull(lclContact.getContactName())) {
                shipFromContact = lclContact.getContactName();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryContact().getPhone1())) {
                shipFromPhone = lclBookingPad.getDeliveryContact().getPhone1();
            }
            if (CommonFunctions.isNotNull(lclBookingPad.getDeliveryContact().getFax1())) {
                shipFromFax = lclBookingPad.getDeliveryContact().getFax1();
            }
        }

        List<LclBookingPiece> lclBookingPiecesList = lclbooking.getLclFileNumber().getLclBookingPieceList();
        if (!lclBookingPiecesList.isEmpty()) {
            for (LclBookingPiece lclbookingPiece : lclBookingPiecesList) {
                if (lclbookingPiece.getLclBookingPieceUnitList() != null && !lclbookingPiece.getLclBookingPieceUnitList().isEmpty()) {
                    LclBookingPieceUnit lclBookingPieceUnits = lclbookingPiece.getLclBookingPieceUnitList().get(0);
                    if (lclBookingPieceUnits != null) {
                        LclUnitSs lclUnitSs = lclBookingPieceUnits.getLclUnitSs();
                        if (CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                            unitsNumber = lclUnitSs.getLclUnit().getUnitNo();
                        }
                        if (!lclUnitSs.getLclUnit().getLclUnitSsImportsList().isEmpty()) {
                            LclUnitSsImports lclUnitImport = lclUnitSs.getLclUnit().getLclUnitSsImportsList().get(0);
                            if (lclUnitImport != null) {
                                if (CommonFunctions.isNotNull(lclUnitImport.getGoDate())) {
                                    goDate = DateUtils.formatDate(lclUnitImport.getGoDate(), "dd-MMM-yyyy");
                                }
                            }
                        }
                        List<LclSsDetail> lclSsDetailList = lclUnitSs.getLclSsHeader().getLclSsDetailList();
                        if (lclSsDetailList != null && lclSsDetailList.size() > 0) {
                            for (Object lclSSDetail : lclSsDetailList) {
                                LclSsDetail lclssDetail = (LclSsDetail) lclSSDetail;
                                //pieces values
                                if (CommonFunctions.isNotNull(lclssDetail.getSpReferenceName())) {
                                    vesselName = lclssDetail.getSpAcctNo().getAccountName();
                                }
                                if (CommonFunctions.isNotNull(lclssDetail.getStd())) {
                                    sailDate = DateUtils.formatDate(lclssDetail.getStd(), "dd-MMM-yyyy");
                                }
                                if (CommonFunctions.isNotNull(lclssDetail.getSta())) {
                                    eta = DateUtils.formatDate(lclssDetail.getSta(), "dd/MM/yyyy");
                                }
                            }
                        }
                        LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
                        if (CommonFunctions.isNotNull(lclUnitSsManifest.getMasterbl())) {
                            masterbl = lclUnitSsManifest.getMasterbl();
                        }
                        List<LclInbond> lclInbondList = new LclInbondsDAO().executeQuery("from LclInbond where lclFileNumber.id= " + lclbooking.getLclFileNumber().getId() + " order by id desc");
                        if (!lclInbondList.isEmpty()) {
                            LclInbond lclInbond = lclInbondList.get(0);
                            if (CommonFunctions.isNotNull(lclInbond) && CommonFunctions.isNotNull(lclInbond.getInbondNo())) {
                                itNumber = lclInbond.getInbondNo();
                            }
                        } else {
                            if (!lclUnitSs.getLclUnit().getLclUnitSsImportsList().isEmpty()) {
                                LclUnitSsImports lclUnitSsImport = lclUnitSs.getLclUnit().getLclUnitSsImportsList().get(0);
                                if (CommonFunctions.isNotNull(lclUnitSsImport.getItNo())) {
                                    itNumber = lclUnitSsImport.getItNo();
                                }
                            }
                        }
                        if (!lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().isEmpty()) {
                            Collections.sort(lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList(), new Comparator<LclUnitWhse>() {

                                public int compare(LclUnitWhse o1, LclUnitWhse o2) {
                                    return o2.getId().compareTo(o1.getId());
                                }
                            });
                            if (lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList() != null && lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().size() > 0) {
                                for (int j = 0; j < lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().size(); j++) {
                                    LclUnitWhse lclUnitWhse = (LclUnitWhse) lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().get(0);
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getWarehouseNo())) {
                                        whseDetails.append(lclUnitWhse.getWarehouse().getWarehouseNo()).append("-").append(" ");
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getWarehouseName())) {
                                        whseDetails.append(lclUnitWhse.getWarehouse().getWarehouseName()).append("\n");
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getAddress())) {
                                        whseDetails.append(lclUnitWhse.getWarehouse().getAddress()).append("\n");
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getCity())) {
                                        whseDetails.append(lclUnitWhse.getWarehouse().getCity()).append(" ");
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getState())) {
                                        whseDetails.append(lclUnitWhse.getWarehouse().getState()).append(" ");
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getZipCode())) {
                                        whseDetails.append(lclUnitWhse.getWarehouse().getZipCode());
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getPhone())) {
                                        whsePhone = lclUnitWhse.getWarehouse().getPhone();
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getWarehouse()) && CommonFunctions.isNotNull(lclUnitWhse.getWarehouse().getFax())) {
                                        whseFax = lclUnitWhse.getWarehouse().getFax();
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getArrivedDatetime())) {
                                        arrivalDate = DateUtils.formatDate(lclUnitWhse.getArrivedDatetime(), "dd-MMM-yyyy");
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getDestuffedDatetime())) {
                                        strippingDate = DateUtils.formatDate(lclUnitWhse.getDestuffedDatetime(), "dd-MMM-yyyy");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                List<LclBookingPieceDetail> lclBookingPieceDetailList = lclbookingPiece.getLclBookingPieceDetailList();
                for (LclBookingPieceDetail lclBookingPieceDetail : lclBookingPieceDetailList) {
                    dims = dims + lclBookingPieceDetail.getActualLength().longValue() + " x " + lclBookingPieceDetail.getActualWidth().longValue() + " x " + lclBookingPieceDetail.getActualHeight().longValue() + " x " + lclBookingPieceDetail.getPieceCount() + ",";
                }

            }
        }
    }

    public void createDeliveryOrderPdf(String realPath, String outputFileName, String documentName, HttpServletRequest request) throws Exception {
        document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(15, 15, 15, 15);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        document.open();
        document.add(headerTable(realPath));
        document.add(contentTable());
        document.add(commodityTable());
        document.add(trackingContainerTable());
        document.add(commentsTable());
        document.add(footerTables(request));
        document.close();
    }

    public PdfPTable headerTable(String realPath) throws Exception {
        String imagePath = "";
        Font blackNormalFont11 = FontFactory.getFont("Arial", 10f, Font.NORMAL);
        PdfPTable headerTable = new PdfPTable(4);
        headerTable.setWidthPercentage(100f);
        headerTable.setWidths(new float[]{1f, 3f, 2.5f, 2f});
        cell = new PdfPCell();
        cell.setBorder(0);
        headerTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        if ((lclbooking != null && lclbooking.getLclFileNumber() != null && lclbooking.getLclFileNumber().getBusinessUnit() != null)) {
            if ("ECI".equalsIgnoreCase(lclbooking.getLclFileNumber().getBusinessUnit())) {
                imagePath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            } else if ("OTI".equalsIgnoreCase(lclbooking.getLclFileNumber().getBusinessUnit())) {
                imagePath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            } else {
                imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
            }
        } else {
                imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
        }
        Image img = Image.getInstance(realPath + imagePath);
        img.scalePercent(60);
        img.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(img);
        headerTable.addCell(cell);
        headerTable.addCell(makeCellNoBorderFontalign("DELIVERY ORDER", 10f, Element.ALIGN_LEFT, 0, blackBoldFont14));
        headerTable.addCell(makeCellNoBorderFontalign(DateUtils.formatStringDateToAppFormat(new Date()), 10f, Element.ALIGN_RIGHT, 0, blackNormalFont11));

        return headerTable;
    }

    public PdfPTable contentTable() throws Exception {
        Paragraph p = null;
        contentTable = new PdfPTable(4);
        contentTable.setWidthPercentage(100f);
        contentTable.setWidths(new float[]{5f, 1.25f, 0.09f, 6f});

        //1
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "Ship From", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setRowspan(2);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
//        cell.setPaddingBottom(5f);
        p = new Paragraph(7f, "Bill of Lading #:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setRowspan(2);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthBottom(0.6f);
//        cell.setPaddingBottom(5f);
        cell.setPaddingLeft(-5f);
        p = new Paragraph(7f, "" + fileNumber, blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //2
//        cell = new PdfPCell();
//        cell.setBorder(0);
//        p = new Paragraph(10f, "", blackBoldFontSize7);
//        cell.addElement(p);
//        contentTable.addCell(cell);
        //3 rowsapn
        cell = new PdfPCell();
        cell.setRowspan(7);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "" + shipperName, blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(7f, "Contact:" + shipFromContact, blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(7f, "Phone..:" + shipFromPhone, blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(7f, "Fax....:" + shipFromFax, blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

//        cell = new PdfPCell();
//        cell.setColspan(3);
//        cell.setBorder(0);
//        cell.setBorderWidthLeft(0.6f);
//        p = new Paragraph(10f, "", blackBoldFontSize7);
//        cell.addElement(p);
//        contentTable.addCell(cell);
        //4
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Carrier Name:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-8f);
        if (carrierName != null && !"".equals(carrierName)) {
            p = new Paragraph(7f, "" + carrierName, blackNormalFont7);
        } else {
            p = new Paragraph(7f, "", blackNormalFont7);
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        //5
        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(5f, "", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //6
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "SCAC:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setColspan(2);
        cell.setBorder(0);
        cell.setPaddingLeft(-25f);
        p = new Paragraph(7f, "" + scac, blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //7
        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(5f, "", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //8
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "PRO NUMBER:", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-4f);
        p = new Paragraph(7f, "" + pickupReferenceNumber, blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(5f, "", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "Ship To", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Freight Charge Terms", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(5f, "", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(5f, "", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        if (dims != "") {
            cell.setRowspan(12);
        } else {
            cell.setRowspan(10);
        }
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "" + shipToDetails, blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(7f, "Contact:" + shipToContact, blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(7f, "Phone..:" + shipToPhone, blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(7f, "Fax....:" + shipToFax, blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(7f, "Hours..:" + shipToHours, blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setRowspan(2);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "Prepaid 3rd Party", blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(5f, "", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setColspan(3);
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "Additional Information", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //dims cell
        if (dims != "") {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setPadding(0f);
            cell.setPaddingLeft(2f);
            cell.setBorderWidthLeft(0.6f);
            p = new Paragraph(7f, "Dims (Inches).....", blackNormalFont7);
            cell.addElement(p);
            contentTable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setPadding(0f);
            p = new Paragraph(7f, ":", blackNormalFont7);
            cell.addElement(p);
            contentTable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setPadding(0f);
            dims = dims.substring(0, dims.length() - 1);
            String[] dimsValues = dims.split(",");
            for (int i = 0; i < dimsValues.length; i++) {
                p = new Paragraph(7f, dimsValues[i] + " pcs", blackNormalFont7);
                cell.addElement(p);
            }
            contentTable.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(3);
            cell.setBorderWidthLeft(0.6f);
            p = new Paragraph(7f, "", blackNormalFont7);
            cell.addElement(p);
            contentTable.addCell(cell);
        }
        //it number cell
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "IT Number.....", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, ":", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, "" + itNumber.toUpperCase(), blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //empty row
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(3f);
        contentTable.addCell(cell);
        //masterbl
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        p = new Paragraph(7f, "Master BOL....", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, ":", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, "" + masterbl.toUpperCase(), blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //empty cell
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(3f);
        contentTable.addCell(cell);
        //ams housebl
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        p = new Paragraph(7f, "AMS House.....", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, ":", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        if (CommonFunctions.isNotNull(ambHouseBL)) {
            p = new Paragraph(7f, "" + ambHouseBL.toString().toUpperCase(), blackNormalFont7);
        } else {
            p = new Paragraph(7f, "", blackNormalFont7);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(2);
        cell.setBorderWidthBottom(0.6f);
        cell.setPadding(0f);
        cell.setPaddingTop(1f);
        cell.setPaddingBottom(1f);
        p = new Paragraph(7f, "Third Party Freight Charges Bill To", blackBoldFontSize7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setPadding(3f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "", blackBoldFontSize6);
        cell.addElement(p);
        contentTable.addCell(cell);
        //pickup number values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        p = new Paragraph(7f, "Pickup Number.", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, ":", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, "" + pickupNumber.toUpperCase(), blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(5);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "" + pickupInstructions.toUpperCase(), blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(3f);
        contentTable.addCell(cell);
        //last free values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        p = new Paragraph(7f, "Last Free Day.", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, ":", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, "" + lastFreeDay, blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //empty
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(3f);
        contentTable.addCell(cell);
        //container values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        cell.setPaddingLeft(2f);
        p = new Paragraph(7f, "Container #...", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, ":", blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(7f, "" + unitsNumber.toUpperCase(), blackNormalFont7);
        cell.addElement(p);
        contentTable.addCell(cell);
        //empty
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setPadding(3f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(5f, "", blackBoldFontSize6);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(9.5f, "SPECIAL INSTRUCTIONS", blackBoldFont14);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        if (specialInstructions != null && !specialInstructions.equals("")) {
            p = new Paragraph(12.5f, "" + specialInstructions, blackBoldFont14);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        } else {
            cell.setPadding(10f);
        }
        contentTable.addCell(cell);

        //  contentTable.addCell(createEmptyCell(0, 10f, 4));
        return contentTable;
    }

    public PdfPTable commodityTable() throws DocumentException, Exception {
        Paragraph p = null;
        table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{0.8f, 0.8f, 0.5f, 1.8f, 7.0f, 1.7f, 1.3f});
        //marks
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(7);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthTop(0.6F);
        cell.setBorderWidthLeft(0.6F);
        cell.setBorderWidthRight(0.6F);
        p = new Paragraph(7f, "CARRIER INFORMATION", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "Package", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //Hm
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "HM", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //grossweight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "WEIGHT", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "Commodity Description", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "LTL only", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "Qty", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "Type", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //Hm
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //grossweight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "LBS", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(7f, "", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "Cu/Ft", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(7f, "NMFC Class", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        //values
        List<LclBookingPiece> lclBookingPiecesList = lclbooking.getLclFileNumber().getLclBookingPieceList();
        if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
            for (LclBookingPiece lclBookingPiece : lclBookingPiecesList) {
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setFixedHeight(25f);
                cell.setBorderWidthLeft(0.6f);
                cell.setBorderWidthRight(0.6f);
                if (CommonFunctions.isNotNull(lclBookingPiece) && CommonFunctions.isNotNull(lclBookingPiece.getBookedPieceCount())) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getBookedPieceCount(), blackNormalFont9);
                    p.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(p);
                }
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.6f);
                if (CommonFunctions.isNotNull(lclBookingPiece) && CommonFunctions.isNotNull(lclBookingPiece.getPackageType().getAbbr01())) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getPackageType().getAbbr01(), blackNormalFont9);
                    p.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(p);
                }
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.6f);
                 if (CommonFunctions.isNotNull(lclBookingPiece) && CommonUtils.isNotEmpty(lclBookingPiece.getLclBookingHazmatList())) {
                p = new Paragraph(7f, "X", blackNormalFont9);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                 }
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.6f);
                if (CommonFunctions.isNotNull(lclBookingPiece) && CommonFunctions.isNotNull(lclBookingPiece.getBookedWeightImperial())) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getBookedWeightImperial(), blackNormalFont9);
                } else {
                    p = new Paragraph(7f, "", blackNormalFont9);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.6f);
                p = new Paragraph(7f, "" + commodityDescription, blackNormalFont9);
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);

                //grossweight
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.6f);
                if (CommonFunctions.isNotNull(lclBookingPiece) && CommonFunctions.isNotNull(lclBookingPiece.getBookedVolumeImperial())) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getBookedVolumeImperial(), blackNormalFont9);
                } else {
                    p = new Paragraph(7f, "", blackNormalFont9);
                }
                p.setAlignment(Element.ALIGN_CENTER);
//                p.setSpacingAfter(10f);
                cell.addElement(p);
                table.addCell(cell);
                //measure
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthRight(0.6f);
                p = new Paragraph(7f, "70", blackNormalFont9);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

            }
            cell = new PdfPCell();
            cell.setColspan(7);
            cell.setBorder(0);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthRight(0.6f);
            table.addCell(cell);
        } else {
            cell = new PdfPCell();
            cell.setColspan(7);
            cell.setBorder(0);
            cell.setFixedHeight(60f);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.setBorderWidthLeft(0.6f);
            cell.setBorderWidthRight(0.6f);
            table.addCell(cell);
        }
        return table;
    }

    public PdfPTable trackingContainerTable() throws DocumentException, Exception {
        Paragraph p = null;
        table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 2f});

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(5);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(10f, "where the rate is dependent on value,shippers are required\nto state specifically in writing the agreed or declared\n"
                + "value of the property as follows:\nThe agrees or declared value of the property is specifically\n"
                + "stated by the shipper to be not exceeding.\n\n _________________PER__________________", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(10f, "", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(10f, "   COD Amount:__________________________", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        PdfTemplate template = pdfWriter.getDirectContent().createTemplate(30, 30);
        template.setLineWidth(0.7f);
        template.rectangle(0, 0, 10f, 10f);
        template.stroke();

        Image img = Image.getInstance(template);
        Chunk chunk = new Chunk(img, 1f, 0f);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        Phrase p1 = new Phrase();
        p1.add(new Phrase(10f, "   Fee Terms: ", blackBoldFontSize7));
        p1.add(new Phrase(10f, "Collect   ", blackBoldFontSize7));
        p1.add(chunk);
        p1.add(new Phrase(10f, "    Prepaid    ", blackBoldFontSize7));
        p1.add(chunk);
        cell.addElement(p1);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(10f, "   Customer Check acceptable:                      ", blackBoldFontSize7);
        p.add(chunk);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(8f, "", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(8f, "", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        p = new Paragraph(8f, "", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(10f, "Note Liability Limitation for loss or damage in this shipment may be applicable.See 49 U.S.C.-14706[c][1][A]and[B].", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(10f, "Received , subject to individually determined rates or\ncontracts that have been agreed upon in writing between the\n"
                + "carrier and shipper.If applicable,otherwise to the rates,\nclassifications and rules that have been established by the\n"
                + "carrier and are available to the shipper on request, and to\nall applicable state and federal regulations.The Shipper\n"
                + "including those on the back thereof, and the said terms and\nconditions are hereby agreed to by the shipper and accepted\n"
                + "for himself/herself and his/her assigns. ", blackNormalFont7);
        cell.addElement(p);
        p = new Paragraph(10f, "Any additional delivery requests not listed herein, \nwill be the responsibility of the consignee.", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        //
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(10f, " The carrier shall not make delivery of this shipment without\npayment of freight and all other lawful charges.\n\n\n\n\n\n\n\n"
                + "_____________________________________________________Shipper Signature", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        table.addCell(cell);
//
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);

        return table;
    }

    public PdfPTable commentsTable() throws DocumentException, Exception {
        Paragraph p = null;
        PdfTemplate template = pdfWriter.getDirectContent().createTemplate(30, 30);
        template.setLineWidth(0.7f);
        template.rectangle(0, 0, 10f, 10f);
        template.stroke();

        Image img = Image.getInstance(template);
        Chunk chunk = new Chunk(img, 1f, 0f);
        table = new PdfPTable(9);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{4f, 1f, 0.5f, 1.3f, 0.5f, 1f, 1.5f, 1f, 4f});
        //1 cell
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setColspan(7);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);
        //2ncell
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(10f, "Shipper Signature/ Date", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Paragraph(10f, "Trailer Loaded:", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        p = new Paragraph(10f, "Freight Counted:", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(10f, "Carrier Signature / Pickup Date", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //3rcell
        cell = new PdfPCell();
        cell.setRowspan(3);
        cell.setBorder(0);
        p = new Paragraph(10f, "This is to certify that the above named materials are properly classified,packaged,marked and labeled,and "
                + "are in proper condition for transportation according to the applicable regulations of the DOT.", blackNormalFont7);
        cell.addElement(p);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        p = new Paragraph(chunk);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(10f);
        p = new Paragraph(10f, "By Shipper", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(chunk);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(10f);
        p = new Paragraph(10f, "By Shipper", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setRowspan(3);
        cell.setBorder(0);
        p = new Paragraph(10f, "Carrier acknowledges receipt of packages and required placards.Carrier certifies emergency response information was made"
                + " available and/or carrier has the DOT emergency response guidebook or equivalent documentation in the vehicle.Properly described "
                + "above is received in good order,except as noted.", blackNormalFont7);
        cell.addElement(p);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);
        //4cell
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        p = new Paragraph(chunk);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(10f);
        p = new Paragraph(10f, "By Driver", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(chunk);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(10f);
        p = new Paragraph(10f, "By Driver/Pallets\nsaid to contain", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        //5
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPadding(0f);
        cell.setBorder(0);
        p = new Paragraph("");
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(10f);
        p = new Paragraph(7f, "", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(chunk);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingTop(10f);
        p = new Paragraph(10f, "By Driver/Pieces", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        //6

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPaddingBottom(5f);
        p = new Paragraph(15f, "___________________________________", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(7);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(5f, "", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPaddingBottom(5f);
        cell.setBorderWidthRight(0.6f);
        p = new Paragraph(15f, "_________________________________", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);

        return table;
    }

    public PdfPTable footerTables(HttpServletRequest request) throws DocumentException, Exception {
        Paragraph p = null;
        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{0.3f, 0.09f, 6f});
        User user = (User) request.getSession().getAttribute("loginuser");
        //1
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);
        //2
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingLeft(1f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(10f, "Office", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(10f, ":", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.6f);
        RefTerminal refTerminal = user.getImportTerminal();
        if (refTerminal != null && refTerminal.getTrmnam() != null && !refTerminal.getTrmnam().equalsIgnoreCase("")) {
            p = new Paragraph(10f, "" + refTerminal.getTrmnam(), blackNormalFont7);
        } else {
            p = new Paragraph(10f, "", blackNormalFont7);
        }
        cell.addElement(p);
        table.addCell(cell);
        //2
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setPaddingLeft(1f);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(10f, "Name..", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(10f, ":", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.6f);
        StringBuilder loginName = new StringBuilder();
        if (user.getFirstName() != null) {
            loginName.append(user.getFirstName());
        }
        if (user.getLastName() != null) {
            loginName.append(user.getLastName());
        }
        p = new Paragraph(10f, "" + loginName, blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        //2
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        cell.setPaddingLeft(1f);
        p = new Paragraph(10f, "Phone.", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(10f, ":", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.6f);
        RefTerminal terminal = lclbooking.getTerminal();
        if (terminal != null && terminal.getPhnnum1() != null) {
            p = new Paragraph(10f, "" + terminal.getPhnnum1(), blackNormalFont7);
        } else {
            p = new Paragraph(10f, "", blackNormalFont7);
        }
        cell.addElement(p);
        table.addCell(cell);
        //2
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        cell.setPaddingLeft(1f);
        p = new Paragraph(10f, "Fax...", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(10f, ":", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.6f);
          if (terminal != null && terminal.getFaxnum1()!= null) {
            p = new Paragraph(10f, "" + terminal.getFaxnum1(), blackNormalFont7);
        } else {
            p = new Paragraph(10f, "", blackNormalFont7);
        }
        cell.addElement(p);
        table.addCell(cell);
        //2
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        cell.setPaddingLeft(1f);
        p = new Paragraph(10f, "Email.", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        p = new Paragraph(10f, ":", blackNormalFont7);
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPadding(0f);
        cell.setBorderWidthRight(0.6f);
        if (terminal != null && terminal.getImportsContactEmail()!= null) {
            p = new Paragraph(10f, "" + terminal.getImportsContactEmail(), blackNormalFont7);
        } else {
            p = new Paragraph(10f, "", blackNormalFont7);
        }
        cell.addElement(p);
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(3);
        cell.setPadding(3f);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthRight(0.6f);
        table.addCell(cell);
        return table;
    }
}
