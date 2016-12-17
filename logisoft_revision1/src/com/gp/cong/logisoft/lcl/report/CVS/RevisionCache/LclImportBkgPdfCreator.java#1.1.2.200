/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.beans.BookingChargesBean;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingImport;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsDispo;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsImports;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.ImportPortConfigurationDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.TerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingSegregationDao;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsManifestDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Logiware
 */
public class LclImportBkgPdfCreator extends LclReportFormatMethods {

    private static final Logger log = Logger.getLogger(LclImportBkgPdfCreator.class);
    private String fileNumberId;
    private String unitSsId;
    private String documentName;
    private String realPath;
    protected BaseFont helv;
    private String companyName;
    private String imagePath = "";
    private String brandWebService = "";

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(String fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public LclImportBkgPdfCreator() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void createPdf(String realPath, String unitSsId, String fileId, String fileNumber, String outputFileName, String documentName, String voyNotiemailId, User loginUser) throws Exception {
        createImportBLPdf(realPath, unitSsId, fileId, fileNumber, outputFileName, documentName, voyNotiemailId, loginUser);
        document.close();
    }

    private void init(String outputFileName) throws Exception {
        document = new Document(PageSize.A4);
        document.setMargins(8, 8, 15, 20);
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
        LclImportBkgPdfCreator.TableHeader event = new LclImportBkgPdfCreator.TableHeader();
        pdfWriter.setPageEvent(event);
        document.open();

    }

    class TableHeader extends PdfPageEventHelper {

        String header;
        PdfTemplate total;

        public void setHeader(String header) {
            this.header = header;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(100, 100);
            total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
            try {
                helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
                        BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                log.info("onOpenDocument failed on " + new Date(), e);
                throw new ExceptionConverter(e);
            }
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            total.beginText();
            total.setFontAndSize(helv, 7);
            total.setTextMatrix(0, 0);
            total.showText(String.valueOf(writer.getPageNumber() - 1));
            total.endText();
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                String docName = LclImportBkgPdfCreator.this.documentName;
                String filenumberId = LclImportBkgPdfCreator.this.fileNumberId;
                String unitSsId = LclImportBkgPdfCreator.this.unitSsId;
                String path = LclImportBkgPdfCreator.this.realPath;
                document.add(headerTable(docName, unitSsId, filenumberId, path));
            } catch (Exception ex) {
                log.info("Exception on class LclImportBkgPdfCreator in method onStartPage" + new Date(), ex);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                PdfPTable footerTable = footerTable();
                footerTable.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
                footerTable.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
                PdfContentByte cb = writer.getDirectContent();
                cb.saveState();
                String text = "Page " + writer.getPageNumber() + " of ";
                float textBase = document.bottom() - (document.bottomMargin() - 30);
                float textSize = helv.getWidthPoint(text, 12);
                cb.beginText();
                cb.setFontAndSize(helv, 7);
                cb.setTextMatrix(document.left() + 280, textBase);
                cb.showText(text);
                cb.endText();
                cb.addTemplate(total, document.left() + 260 + textSize, textBase);
                cb.restoreState();
            } catch (Exception e) {
                log.info("Exception on class LclImportBkgPdfCreator in method onStartPage" + new Date(), e);
            }
        }
    }

    public void createImportBLPdf(String realPath, String unitSsId, String fileId, String fileNumber, String outputFileName, String documentName, String voyNotiemailId, User loginUser) throws Exception {
        ImportPortConfigurationDAO importPortConfigurationDAO = new ImportPortConfigurationDAO();
        LclUnitSsDispoDAO lclUnitSsDispoDAO = new LclUnitSsDispoDAO();
        LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
        LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        String[] fileValues = new String[2];
        if (CommonUtils.isNotEmpty(unitSsId)) {
            fileValues = new LclUnitSsDAO().getTotalBkgInUnits(unitSsId);
        }
        if (fileValues == null || fileValues[0] == null || "".equals(fileValues[0])) {
            fileValues[0] = fileId;
        }
        String[] sFileId = fileValues[0].split(",");
        for (int i = 0; i < sFileId.length; i++) {
            if (i == 0) {
                fileNumberId = sFileId[i];
                init(outputFileName);
            } else {
                fileNumberId = sFileId[i];
                document.newPage();
            }
            LclBookingPad lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(Long.parseLong(sFileId[i]));
            String SUHeadingNote = "";
            LclRemarks lclRemarks = lclRemarksDAO.getLclRemarksByType(sFileId[i], "SU");
            if (lclRemarks != null && lclRemarks.getRemarks() != null) {
                SUHeadingNote = lclRemarks.getRemarks();
            }
            LclUnitSs lclUnitSs = null;
            StringBuilder polValues = new StringBuilder();
            StringBuilder placeOfReceipt = new StringBuilder();
            StringBuilder podValues = new StringBuilder();
            StringBuilder placeOfDelivery = new StringBuilder();
            String notifyPhone = "";
            String notifyFax = "";
            String exportReference = "";
            String consigneePhone = "";
            String subHouseBL = "";
            String ambHouseBL = "";
            String consigneeFax = "";
            String shipperPhone = "";
            String shipperFax = "";
            String DoorDelivery = "";
            String headingName = "";
            String deliveryName = "";
            StringBuilder deliveryAddress = new StringBuilder();
            String deliveryCity = "";
            String deliveryState = "";
            String deliveryZip = "";
            String deliveryEmail = "";
            String whsePhone = "";
            String whseFax = "";
            String unitsNumber = "";
            String vesselName = "";
            String ssVoyageNo = "";
            String bookingNumber = "";
            String sailDate = "";
            String arrivalDate = "";
            String lastFreeDay = "";
            String approximateDate = "";
            String strippingDate = "";
            String goDate = "";
            String eta = "";
            String etaFd = "";
            String deliveryPhone = "";
            String deliveryFax = "";
            String agentPhone = "";
            String agentFax = "";
            String externalComments = "";
            Double total = 0.00;
            StringBuilder consigneeCity = new StringBuilder();
            StringBuilder shipperCity = new StringBuilder();
            StringBuilder agentCity = new StringBuilder();
            StringBuilder conFileNumber = new StringBuilder();
            String voyageNumber = "";
            String terminalNumber = "";
            String polValue = "";
            String podValue = "";
            StringBuilder notifyCity = new StringBuilder();
            StringBuilder whseDetails = new StringBuilder();
            StringBuilder itDetails = new StringBuilder();
            String ipiName = "";

            String[] billToParty;
            if (documentName.equalsIgnoreCase("Third Party Invoice")) {
                headingName = "THIRD PARTY INVOICE";
                billToParty = new String[]{"T"};
            } else {
                billToParty = new String[]{"C","N"};

            }
            LclBookingImport lclBookingImport = null;
            List<BookingChargesBean> lclBookingAcList = null;
            List<LclBookingImportAms> lclBookingImportAmsList = null;
            List<LclInbond> lclInbondList = null;
            LclBooking lclbooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", Long.parseLong(sFileId[i]));
            if (lclbooking != null) {
                List<String> billToPartyList = Arrays.asList(billToParty);
                lclBookingAcList = new LclCostChargeDAO().findBybookingAcId(lclbooking.getLclFileNumber().getId().toString(), billToPartyList);
                for (int j = 0; j < lclBookingAcList.size(); j++) {
                    BookingChargesBean lclBookingAc = (BookingChargesBean) lclBookingAcList.get(j);
                    if (headingName.equalsIgnoreCase("Third Party Invoice")) {
                        if (lclBookingAc.getArBillToParty().equalsIgnoreCase("T")) {
                            if (lclBookingAc.getBalanceAmt() != null && lclBookingAc.getBalanceAmt().doubleValue() != 0.00 || lclBookingAc.getPaidAmt() == null) {
                                if (lclBookingAc.getPaidAmt() == null && lclBookingAc.getTotalAmt() != null) {
                                    total = total + lclBookingAc.getTotalAmt().doubleValue();
                                } else {
                                    total = total + lclBookingAc.getBalanceAmt().doubleValue();
                                }
                            }
                        }
                    } else if (documentName.equalsIgnoreCase("Freight Invoice")) {
                        total = total + lclBookingAc.getTotalAmt().doubleValue();
                    } else {
                        if (!(lclBookingAc.getArBillToParty().equalsIgnoreCase("T"))) {
                            if (lclBookingAc.getBalanceAmt() != null && lclBookingAc.getBalanceAmt().doubleValue() != 0.00 || lclBookingAc.getPaidAmt() == null) {
                                if (lclBookingAc.getPaidAmt() == null && lclBookingAc.getTotalAmt() != null) {
                                    total = total + lclBookingAc.getTotalAmt().doubleValue();
                                } else {
                                    total = total + lclBookingAc.getBalanceAmt().doubleValue();
                                }
                            }
                        }
                    }
                }
                externalComments = lclRemarksDAO.getLclRemarksByTypeSQL(lclbooking.getLclFileNumber().getId().toString(), "E");
                lclBookingImport = lclbooking.getLclFileNumber().getLclBookingImport();
                if (lclBookingImport != null) {
                    if (lclBookingImport.getFdEta() != null) {
                        etaFd = DateUtils.formatDate(lclBookingImport.getFdEta(), "dd-MMM-yyyy");
                    }
                    if (lclBookingImport.getGoDatetime() != null) {
                        goDate = DateUtils.formatDate(lclBookingImport.getGoDatetime(), "dd-MMM-yyyy");
                    }
                    if (CommonFunctions.isNotNull(lclBookingImport.getSubHouseBl())) {
                        subHouseBL = lclBookingImport.getSubHouseBl();
                    }
                }
                Boolean isSegregationFlag = new LclBookingSegregationDao().isCheckedSegregationDr(lclbooking.getLclFileNumber().getId());
                if (isSegregationFlag) {
                    lclBookingImportAmsList = new LclBookingImportAmsDAO().findBysSegFileNumberId(lclbooking.getLclFileNumber().getId());
                } else {
                    lclBookingImportAmsList = new LclBookingImportAmsDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
                }
                if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() > 0 && lclBookingImportAmsList.size() == 1) {
                    for (int j = 0; j < lclBookingImportAmsList.size(); j++) {
                        LclBookingImportAms lclBookingImportAms = (LclBookingImportAms) lclBookingImportAmsList.get(j);
                        if (CommonFunctions.isNotNull(lclBookingImportAms) && CommonFunctions.isNotNull(lclBookingImportAms.getAmsNo())) {
                            ambHouseBL = lclBookingImportAms.getScac() + "" + lclBookingImportAms.getAmsNo();
                        }
                        break;
                    }
                } else if (lclBookingImportAmsList != null && lclBookingImportAmsList.size() > 0 && lclBookingImportAmsList.size() > 1) {
                    ambHouseBL = "MULTIPLE AMS-SEE BELOW";
                }
                Collections.reverse(lclBookingImportAmsList);
                if (CommonFunctions.isNotNull(lclbooking) && CommonFunctions.isNotNull(lclbooking.getTerminal())
                        && CommonFunctions.isNotNull(lclbooking.getTerminal().getTrmnum())) {
                    terminalNumber = lclbooking.getTerminal().getTrmnum();
                }
                RefTerminal terminal = null;
                if (lclbooking.getPortOfDestination() != null && lclbooking.getPortOfDestination().getUnLocationCode() != null) {
                    if (lclbooking.getPortOfDestination() == lclbooking.getFinalDestination()) {
                        terminal = refTerminalDAO.findById(importPortConfigurationDAO.getImportTerminal(lclbooking.getPortOfDestination().getId()));
                    } else if (lclbooking.getPortOfDestination() != lclbooking.getFinalDestination()) {
                        terminal = refTerminalDAO.findById(importPortConfigurationDAO.getImportTerminal(lclbooking.getFinalDestination().getId()));
                        if (terminal == null) {
                            terminal = refTerminalDAO.findById(importPortConfigurationDAO.getImportTerminal(lclbooking.getPortOfDestination().getId()));
                        }
                    }
                }
                if (terminal != null) {
                    if (CommonFunctions.isNotNull(terminal.getTrmnam())) {
                        deliveryName = terminal.getTrmnam();
                    }
                    if (CommonFunctions.isNotNull(terminal.getAddres1())) {
                        deliveryAddress.append(terminal.getAddres1());
                    }
                    if (CommonFunctions.isNotNull(terminal.getAddres1())) {
                        deliveryAddress.append("\n").append(terminal.getAddres2());
                    }
                    if (CommonFunctions.isNotNull(terminal.getCity1())) {
                        deliveryCity = terminal.getCity1();
                    }
                    if (CommonFunctions.isNotNull(terminal.getState())) {
                        deliveryState = terminal.getState();
                    }
                    if (CommonFunctions.isNotNull(terminal.getZipcde())) {
                        deliveryZip = terminal.getZipcde();
                    }
                    if (CommonFunctions.isNotNull(terminal.getPhnnum1())) {
                        deliveryPhone = terminal.getPhnnum1();
                    }
                    if (CommonFunctions.isNotNull(terminal.getFaxnum1())) {
                        deliveryFax = terminal.getFaxnum1();
                    }
                    if (CommonFunctions.isNotNull(terminal.getImportsContactEmail())) {
                        deliveryEmail = terminal.getImportsContactEmail();
                    }
                }
                if (null != lclbooking.getPooPickup() && lclbooking.getPooPickup() && lclBookingPad != null
                        && CommonUtils.isNotEmpty(lclBookingPad.getPickUpCity())) {
                    DoorDelivery = lclBookingPad.getPickUpCity();
                }
                if (CommonFunctions.isNotNull(lclbooking)) {
                    if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading())
                            && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getUnLocationCode())) {
                        polValue = lclbooking.getPortOfLoading().getUnLocationCode();
                    }
                    if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination())
                            && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationCode())) {
                        podValue = lclbooking.getPortOfDestination().getUnLocationCode();
                    }
                    if (CommonFunctions.isNotNull(lclbooking.getPortOfOrigin())) {
                        if (CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getUnLocationName())) {
                            placeOfReceipt.append(lclbooking.getPortOfOrigin().getUnLocationName());
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfOrigin().getCountryId().getCodedesc())) {
                            placeOfReceipt.append(",").append(lclbooking.getPortOfOrigin().getCountryId().getCodedesc());
                        }
                    } else {
                        if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getUnLocationName())) {
                            placeOfReceipt.append(lclbooking.getPortOfLoading().getUnLocationName());
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId().getCodedesc())) {
                            placeOfReceipt.append(",").append(lclbooking.getPortOfLoading().getCountryId().getCodedesc());
                        }
                    }
                    if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getUnLocationName())) {
                        polValues.append(lclbooking.getPortOfLoading().getUnLocationName());
                    }
                    if (CommonFunctions.isNotNull(lclbooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId()) && CommonFunctions.isNotNull(lclbooking.getPortOfLoading().getCountryId().getCodedesc())) {
                        polValues.append(",").append(lclbooking.getPortOfLoading().getCountryId().getCodedesc());
                    }
                    if (CommonFunctions.isNotNull(lclbooking.getFinalDestination()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getUnLocationName())) {
                        placeOfDelivery.append(lclbooking.getFinalDestination().getUnLocationName());
                    }
                    if (CommonFunctions.isNotNull(lclbooking.getFinalDestination()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getStateId()) && CommonFunctions.isNotNull(lclbooking.getFinalDestination().getStateId().getCode())) {
                        placeOfDelivery.append(",").append(lclbooking.getFinalDestination().getStateId().getCode());
                    }
                    if (lclbooking.getPortOfDestination() != null && lclbooking.getFinalDestination() != null
                            && lclbooking.getPortOfDestination() != lclbooking.getFinalDestination()) {
                        if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo())) {
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getAccountName())) {
                                whseDetails.append(lclBookingImport.getIpiCfsAcctNo().getAccountName()).append("\n");
                            }
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getFirmsCode())) {
                                whseDetails.append("FIRMS CODE:").append(lclBookingImport.getIpiCfsAcctNo().getFirmsCode()).append("\n");
                            }
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getCoName())) {
                                whseDetails.append(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getCoName()).append("\n");
                            }
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getAddress1())) {
                                whseDetails.append(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getAddress1()).append("\n");
                            }
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getCity2())) {
                                whseDetails.append(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getCity2()).append(" ");
                            }
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getState())) {
                                whseDetails.append(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getState()).append(" ");
                            }
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getZip())) {
                                whseDetails.append(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getZip());
                            }
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getPhone())) {
                                whsePhone = lclBookingImport.getIpiCfsAcctNo().getCustAddr().getPhone();
                            }
                            if (CommonFunctions.isNotNull(lclBookingImport.getIpiCfsAcctNo().getCustAddr().getFax())) {
                                whseFax = lclBookingImport.getIpiCfsAcctNo().getCustAddr().getFax();
                            }
                        }
                    }

                    if (CommonFunctions.isNotNull(lclbooking.getNotyAcct()) && CommonFunctions.isNotNull(lclbooking.getNotyAcct().getPrimaryCustAddr())) {
                        if (CommonFunctions.isNotNull(lclbooking.getNotyAcct().getPrimaryCustAddr().getPhone())) {
                            notifyPhone = lclbooking.getNotyAcct().getPrimaryCustAddr().getPhone();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyAcct().getPrimaryCustAddr().getFax())) {
                            notifyFax = lclbooking.getNotyAcct().getPrimaryCustAddr().getFax();
                        }
                        if (lclbooking.getNotyContact() != null && CommonFunctions.isNotNull(lclbooking.getNotyContact().getCompanyName())) {
                            notifyCity.append(lclbooking.getNotyContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyAcct().getPrimaryCustAddr().getAddress1())) {
                            notifyCity.append(lclbooking.getNotyAcct().getPrimaryCustAddr().getAddress1()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyAcct().getPrimaryCustAddr().getCity2())) {
                            notifyCity.append(lclbooking.getNotyAcct().getPrimaryCustAddr().getCity2()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyAcct().getPrimaryCustAddr().getState())) {
                            notifyCity.append(lclbooking.getNotyAcct().getPrimaryCustAddr().getState()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyAcct().getPrimaryCustAddr().getZip())) {
                            notifyCity.append(lclbooking.getNotyAcct().getPrimaryCustAddr().getZip());
                        }
                    } else {
                        if (CommonFunctions.isNotNull(lclbooking.getNotyContact().getPhone1())) {
                            notifyPhone = lclbooking.getNotyContact().getPhone1();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyContact().getFax1())) {
                            notifyFax = lclbooking.getNotyContact().getFax1();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyContact().getCompanyName())) {
                            notifyCity.append(lclbooking.getNotyContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyContact().getAddress())) {
                            notifyCity.append(lclbooking.getNotyContact().getAddress()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyContact().getCity())) {
                            notifyCity.append(lclbooking.getNotyContact().getCity()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyContact().getState())) {
                            notifyCity.append(lclbooking.getNotyContact().getState()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getNotyContact().getZip())) {
                            notifyCity.append(lclbooking.getNotyContact().getZip());
                        }
                    }

                    if (CommonFunctions.isNotNull(lclbooking.getConsAcct()) && CommonFunctions.isNotNull(lclbooking.getConsAcct().getPrimaryCustAddr())) {
                        if (CommonFunctions.isNotNull(lclbooking.getConsAcct().getPrimaryCustAddr().getPhone())) {
                            consigneePhone = lclbooking.getConsAcct().getPrimaryCustAddr().getPhone();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsAcct().getPrimaryCustAddr().getFax())) {
                            consigneeFax = lclbooking.getConsAcct().getPrimaryCustAddr().getFax();
                        }
                        if (lclbooking.getConsContact() != null && CommonFunctions.isNotNull(lclbooking.getConsContact().getCompanyName())) {
                            consigneeCity.append("").append(lclbooking.getConsContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsAcct().getPrimaryCustAddr().getAddress1())) {
                            consigneeCity.append("").append(lclbooking.getConsAcct().getPrimaryCustAddr().getAddress1()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsAcct().getPrimaryCustAddr().getCity2())) {
                            consigneeCity.append("").append(lclbooking.getConsAcct().getPrimaryCustAddr().getCity2()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsAcct().getPrimaryCustAddr().getState())) {
                            consigneeCity.append(lclbooking.getConsAcct().getPrimaryCustAddr().getState()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsAcct().getPrimaryCustAddr().getZip())) {
                            consigneeCity.append(lclbooking.getConsAcct().getPrimaryCustAddr().getZip());
                        }
                    } else {
                        if (CommonFunctions.isNotNull(lclbooking.getConsContact().getPhone1())) {
                            consigneePhone = lclbooking.getConsContact().getPhone1();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsContact().getFax1())) {
                            consigneeFax = lclbooking.getConsContact().getFax1();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsContact().getCompanyName())) {
                            consigneeCity.append("").append(lclbooking.getConsContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsContact().getAddress())) {
                            consigneeCity.append("").append(lclbooking.getConsContact().getAddress()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsContact().getCity())) {
                            consigneeCity.append("").append(lclbooking.getConsContact().getCity()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsContact().getState())) {
                            consigneeCity.append(lclbooking.getConsContact().getState()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getConsContact().getZip())) {
                            consigneeCity.append(lclbooking.getConsContact().getZip());
                        }
                    }

                    if (CommonFunctions.isNotNull(lclbooking.getShipAcct()) && CommonFunctions.isNotNull(lclbooking.getShipAcct().getPrimaryCustAddr())) {
                        if (CommonFunctions.isNotNull(lclbooking.getShipAcct().getPrimaryCustAddr().getPhone())) {
                            shipperPhone = lclbooking.getShipAcct().getPrimaryCustAddr().getPhone();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipAcct().getPrimaryCustAddr().getFax())) {
                            shipperFax = lclbooking.getShipAcct().getPrimaryCustAddr().getFax();
                        }
                        if (lclbooking.getShipContact() != null && CommonFunctions.isNotNull(lclbooking.getShipContact().getCompanyName())) {
                            shipperCity.append("").append(lclbooking.getShipContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipAcct().getPrimaryCustAddr().getAddress1())) {
                            shipperCity.append("").append(lclbooking.getShipAcct().getPrimaryCustAddr().getAddress1()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipAcct().getPrimaryCustAddr().getCity2())) {
                            shipperCity.append("").append(lclbooking.getShipAcct().getPrimaryCustAddr().getCity2()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipAcct().getPrimaryCustAddr().getState())) {
                            shipperCity.append(lclbooking.getShipAcct().getPrimaryCustAddr().getState()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipAcct().getPrimaryCustAddr().getZip())) {
                            shipperCity.append(lclbooking.getShipAcct().getPrimaryCustAddr().getZip());
                        }
                    } else {
                        if (CommonFunctions.isNotNull(lclbooking.getShipContact().getPhone1())) {
                            shipperPhone = lclbooking.getShipContact().getPhone1();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipContact().getFax1())) {
                            shipperFax = lclbooking.getShipContact().getFax1();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipContact().getCompanyName())) {
                            shipperCity.append("").append(lclbooking.getShipContact().getCompanyName()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipContact().getAddress())) {
                            shipperCity.append("").append(lclbooking.getShipContact().getAddress()).append("\n");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipContact().getCity())) {
                            shipperCity.append("").append(lclbooking.getShipContact().getCity()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipContact().getState())) {
                            shipperCity.append(lclbooking.getShipContact().getState()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getShipContact().getZip())) {
                            shipperCity.append(lclbooking.getShipContact().getZip());
                        }
                    }

                    if (CommonFunctions.isNotNull(lclbooking.getSupAcct()) && CommonFunctions.isNotNull(lclbooking.getSupAcct().getAccountno())) {
                        if (CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr()) && CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr().getPhone())) {
                            agentPhone = lclbooking.getSupAcct().getPrimaryCustAddr().getPhone();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr()) && CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr().getFax())) {
                            agentFax = lclbooking.getSupAcct().getPrimaryCustAddr().getFax();
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr()) && CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr().getAddress1())) {
                            agentCity.append("").append(lclbooking.getSupAcct().getPrimaryCustAddr().getAddress1()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr()) && CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr().getState())) {
                            agentCity.append(lclbooking.getSupAcct().getPrimaryCustAddr().getState()).append(" ");
                        }
                        if (CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr()) && CommonFunctions.isNotNull(lclbooking.getSupAcct().getPrimaryCustAddr().getZip())) {
                            agentCity.append(lclbooking.getSupAcct().getPrimaryCustAddr().getZip());
                        }
                    }
                    lclInbondList = new LclInbondsDAO().executeQuery("from LclInbond where lclFileNumber.id= " + lclbooking.getLclFileNumber().getId() + " order by id desc");
                    if (lclInbondList != null && !lclInbondList.isEmpty() && lclInbondList.size() > 0 && lclInbondList.size() == 1) {
                        LclInbond lclInbond = lclInbondList.get(0);
                        if (CommonFunctions.isNotNull(lclInbond.getInbondNo())) {
                            itDetails.append(" ").append(lclInbond.getInbondNo()).append("  ");
                        }
                        if (CommonFunctions.isNotNull(lclInbond.getInbondDatetime())) {
                            itDetails.append(" ").append(DateUtils.formatDate(lclInbond.getInbondDatetime(), "dd-MMM-yyyy")).append("  ");
                        }
                        if (CommonFunctions.isNotNull(lclInbond.getInbondPort()) && CommonFunctions.isNotNull(lclInbond.getInbondPort().getUnLocationName())) {
                            itDetails.append(" ").append(lclInbond.getInbondPort().getUnLocationName()).append(", ");
                        }
                        if (CommonFunctions.isNotNull(lclInbond.getInbondPort()) && CommonFunctions.isNotNull(lclInbond.getInbondPort().getStateId()) && CommonFunctions.isNotNull(lclInbond.getInbondPort().getStateId().getCode())) {
                            itDetails.append(lclInbond.getInbondPort().getStateId().getCode());
                        }
                    } else if (lclInbondList != null && !lclInbondList.isEmpty() && lclInbondList.size() > 1) {
                        itDetails.append("MULTIPLE IT-SEE BELOW");
                    }
                }
                List<LclBookingPiece> lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
                if (!lclBookingPiecesList.isEmpty()) {
                    for (LclBookingPiece lclbookingPiece : lclBookingPiecesList) {
                        if (!lclbookingPiece.getLclBookingPieceUnitList().isEmpty()) {
                            LclBookingPieceUnit lclBookingPieceUnits = lclbookingPiece.getLclBookingPieceUnitList().get(0);
                            if (lclBookingPieceUnits != null) {
                                lclUnitSs = lclBookingPieceUnits.getLclUnitSs();
                                if (SUHeadingNote.equals("") && lclUnitSs != null && lclUnitSs.getSUHeadingNote() != null) {
                                    SUHeadingNote = lclUnitSs.getSUHeadingNote();
                                }
                                if (CommonFunctions.isNotNull(lclUnitSs) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader()) && CommonFunctions.isNotNull(lclUnitSs.getLclSsHeader().getScheduleNo())) {
                                    voyageNumber = lclUnitSs.getLclSsHeader().getScheduleNo();
                                }
                                if (CommonFunctions.isNotNull(lclUnitSs) && CommonFunctions.isNotNull(lclUnitSs.getLclUnit()) && CommonFunctions.isNotNull(lclUnitSs.getLclUnit().getUnitNo())) {
                                    unitsNumber = lclUnitSs.getLclUnit().getUnitNo();
                                }
                                LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifestDAO().getLclUnitSSManifestByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
                                if (CommonFunctions.isNotNull(lclUnitSsManifest) && CommonFunctions.isNotNull(lclUnitSsManifest.getMasterbl())) {
                                    bookingNumber = lclUnitSsManifest.getMasterbl();
                                }

                                if (lclUnitSs != null && lclUnitSs.getLclUnit() != null && lclUnitSs.getLclSsHeader() != null) {
                                    LclUnitSsImports lclUnitSsImports = new LclUnitSsImportsDAO().getLclUnitSSImportsByHeader(lclUnitSs.getLclUnit().getId(), lclUnitSs.getLclSsHeader().getId());
                                    if (lclUnitSsImports != null) {
                                        if (CommonFunctions.isNotNull(lclUnitSsImports.getLastFreeDate())) {
                                            lastFreeDay = DateUtils.formatDate(lclUnitSsImports.getLastFreeDate(), "dd-MMM-yyyy");
                                        }
                                        if (CommonUtils.isEmpty(itDetails)) {
                                            if (CommonFunctions.isNotNull(lclUnitSsImports.getItNo())) {
                                                itDetails.append(" ").append(lclUnitSsImports.getItNo()).append("  ");
                                            }
                                            if (CommonFunctions.isNotNull(lclUnitSsImports.getItDatetime())) {
                                                itDetails.append(DateUtils.formatDate(lclUnitSsImports.getItDatetime(), "dd-MMM-yyyy")).append("  ");
                                            }
                                            if (CommonFunctions.isNotNull(lclUnitSsImports.getItPortId()) && CommonFunctions.isNotNull(lclUnitSsImports.getItPortId().getUnLocationName())) {
                                                itDetails.append(lclUnitSsImports.getItPortId().getUnLocationName()).append(", ");
                                            }
                                            if (CommonFunctions.isNotNull(lclUnitSsImports.getItPortId()) && CommonFunctions.isNotNull(lclUnitSsImports.getItPortId().getStateId())
                                                    && CommonFunctions.isNotNull(lclUnitSsImports.getItPortId().getStateId().getCode())) {
                                                itDetails.append(lclUnitSsImports.getItPortId().getStateId().getCode());
                                            }
                                        }
                                        if (CommonFunctions.isNotNull(lclUnitSsImports.getApproxDueDate())) {
                                            approximateDate = DateUtils.formatDate(lclUnitSsImports.getApproxDueDate(), "dd-MMM-yyyy");
                                        }
                                        if (whseDetails.toString().equals("") && "COLOAD".equalsIgnoreCase(lclUnitSs.getLclUnit().getUnitType().getDescription().toUpperCase())) {
                                            if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo())) {
                                                if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName())) {
                                                    whseDetails.append(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountName()).append("(");
                                                    whseDetails.append(lclUnitSsImports.getColoaderDevanningAcctNo().getAccountno()).append(")").append("\n");
                                                }
                                                if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getFirmsCode())) {
                                                    whseDetails.append("FIRMS CODE:").append(lclUnitSsImports.getColoaderDevanningAcctNo().getFirmsCode()).append("\n");
                                                }
                                                if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr())) {
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getCoName())) {
                                                        whseDetails.append(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getCoName()).append("\n");
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getAddress1())) {
                                                        whseDetails.append(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getAddress1()).append("\n");
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getCity2())) {
                                                        whseDetails.append(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getCity2()).append(" ");
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getState())) {
                                                        whseDetails.append(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getState()).append(" ");
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getZip())) {
                                                        whseDetails.append(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getZip());
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getPhone())) {
                                                        whsePhone = lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getPhone();
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getFax())) {
                                                        whseFax = lclUnitSsImports.getColoaderDevanningAcctNo().getCustAddr().getFax();
                                                    }
                                                }
                                            }
                                        } else {
                                            if (whseDetails.toString().equals("")) {
                                                if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId())) {
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getWarehouseName())) {
                                                        whseDetails.append(lclUnitSsImports.getCfsWarehouseId().getWarehouseName()).append("\n");
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getAddress())) {
                                                        whseDetails.append(lclUnitSsImports.getCfsWarehouseId().getAddress()).append("\n");
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getCity())) {
                                                        whseDetails.append(lclUnitSsImports.getCfsWarehouseId().getCity()).append(" ");
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getState())) {
                                                        whseDetails.append(lclUnitSsImports.getCfsWarehouseId().getState()).append(" ");
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getZipCode())) {
                                                        whseDetails.append(lclUnitSsImports.getCfsWarehouseId().getZipCode());
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getPhone())) {
                                                        whsePhone = lclUnitSsImports.getCfsWarehouseId().getPhone();
                                                    }
                                                    if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getFax())) {
                                                        whseFax = lclUnitSsImports.getCfsWarehouseId().getFax();
                                                    }
                                                }
                                            }
                                        }
                                        if (CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId())
                                                && CommonFunctions.isNotNull(lclUnitSsImports.getCfsWarehouseId().getWarehouseName())) {
                                            if (lclUnitSsImports.getCfsWarehouseId().getWarehouseName().startsWith("CFS/")) {
                                                ipiName = lclUnitSsImports.getCfsWarehouseId().getWarehouseName().substring(10);
                                            } else {
                                                ipiName = lclUnitSsImports.getCfsWarehouseId().getWarehouseName();
                                            }
                                        }
                                    }
                                }
                                if (!lclUnitSs.getLclSsHeader().getLclSsMasterBlList().isEmpty()) {
                                    LclSSMasterBl lclSSMasterBl = lclUnitSs.getLclSsHeader().getLclSsMasterBlList().get(0);
                                    if (lclSSMasterBl != null && lclSSMasterBl.getExportRefEdi() != null) {
                                        exportReference = lclSSMasterBl.getExportRefEdi();
                                    }
                                }
                                LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getLclSsDetailList().get(0);
                                if (lclSsDetail != null) {
                                    if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
                                        vesselName = lclSsDetail.getSpReferenceName();
                                    }
                                    if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
                                        ssVoyageNo = lclSsDetail.getSpReferenceNo();
                                    }
                                    if (CommonFunctions.isNotNull(lclSsDetail.getStd())) {
                                        sailDate = DateUtils.formatDate(lclSsDetail.getStd(), "dd-MMM-yyyy");
                                    }
                                    if (CommonFunctions.isNotNull(lclSsDetail.getSta())) {
                                        eta = DateUtils.formatDate(lclSsDetail.getSta(), "dd-MMM-yyyy");
                                    }
                                    if (CommonFunctions.isNotNull(lclSsDetail.getArrival()) && CommonFunctions.isNotNull(lclSsDetail.getArrival().getUnLocationName())) {
                                        podValues.append(lclSsDetail.getArrival().getUnLocationName());
                                        if (CommonFunctions.isNotNull(lclSsDetail.getArrival()) && CommonFunctions.isNotNull(lclSsDetail.getArrival().getStateId()) && CommonFunctions.isNotNull(lclSsDetail.getArrival().getStateId().getCode())) {
                                            podValues.append(",").append(lclSsDetail.getArrival().getStateId().getCode());
                                        }
                                    }
                                }
                                Integer dispCount = lclUnitSsDispoDAO.getUnitDispoCountByDispDesc(lclUnitSs.getLclUnit().getId().toString(), "PORT", lclSsDetail.getId());
                                if (documentName.equalsIgnoreCase("Third Party Invoice")) {
                                    headingName = "THIRD PARTY INVOICE";
                                } else {
                                    if (dispCount == 0) {
                                        headingName = "PRE ADVICE";
                                    } else if (dispCount > 0) {
                                        String disposition = lclUnitSsDispoDAO.getDispositionByDetailId(lclUnitSs.getLclUnit().getId(), lclSsDetail.getId());
                                        if (disposition.equalsIgnoreCase("PORT")) {
                                            headingName = "ARRIVAL NOTICE";
                                        } else {
                                            headingName = "STATUS UPDATE";
                                        }
                                    }
                                }
                                if (voyNotiemailId != null) {//Voyage Notification Scheduler
                                    String[] splitEmails = voyNotiemailId.split(",");
                                    for (int m = 0; m < splitEmails.length; m++) {
                                        if (!"".equals(splitEmails[m].toString())) {
                                            int userId = lclUnitSsDAO.getlastDispoUserId(lclUnitSs.getLclUnit().getId());
                                            String remarks = headingName + " Sent to " + splitEmails[m].toString();
                                            lclRemarksDAO.insertLclRemarks(Long.parseLong(sFileId[i].toString()), LclCommonConstant.REMARKS_DR_AUTO_NOTES, remarks, userId);
                                        }
                                    }
                                }
                                if (!lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().isEmpty()) {
                                    Collections.sort(lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList(), new Comparator<LclUnitWhse>() {

                                        public int compare(LclUnitWhse o1, LclUnitWhse o2) {
                                            return o2.getId().compareTo(o1.getId());
                                        }
                                    });
                                }
                                if (lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList() != null && lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().size() > 0) {
                                    LclUnitWhse lclUnitWhse = lclBookingPieceUnits.getLclUnitSs().getLclUnit().getLclUnitWhseList().get(0);
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getArrivedDatetime())) {
                                        arrivalDate = DateUtils.formatDate(lclUnitWhse.getArrivedDatetime(), "dd-MMM-yyyy");
                                    }
                                    if (CommonFunctions.isNotNull(lclUnitWhse.getDestuffedDatetime())) {
                                        strippingDate = DateUtils.formatDate(lclUnitWhse.getDestuffedDatetime(), "dd-MMM-yyyy");
                                    }
                                }

                            }
                        } else {
                            if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getUnLocationName())) {
                                podValues.append(lclbooking.getPortOfDestination().getUnLocationName());
                            }
                            if (CommonFunctions.isNotNull(lclbooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId()) && CommonFunctions.isNotNull(lclbooking.getPortOfDestination().getStateId().getCode())) {
                                podValues.append(",").append(lclbooking.getPortOfDestination().getStateId().getCode());
                            }
                        }
                    }
                }
            }
            conFileNumber.append(terminalNumber).append("-").append(polValue).append("-").append(podValue).append("-").append(voyageNumber);
            if (lclbooking != null) {
                document.add(contentTable(documentName, lclbooking, shipperCity.toString(), bookingNumber, itDetails.toString(), ambHouseBL, exportReference, notifyCity.toString(),
                        subHouseBL, DoorDelivery, notifyFax, shipperFax, consigneeCity.toString(), consigneePhone, consigneeFax, shipperPhone, notifyPhone, whseDetails.toString(),
                        deliveryName, deliveryAddress.toString(), deliveryCity, deliveryState, deliveryZip, deliveryEmail, whsePhone, whseFax, deliveryPhone, deliveryFax, polValues.toString(), placeOfReceipt.toString(), unitsNumber, conFileNumber.toString(), vesselName, ssVoyageNo, approximateDate, strippingDate, lastFreeDay,
                        podValues.toString(), eta, sailDate, arrivalDate, goDate, placeOfDelivery.toString(), etaFd, ipiName, agentCity.toString(), agentPhone, agentFax, headingName, SUHeadingNote));
                document.add(commodityTable(documentName, lclbooking, externalComments, lclBookingImportAmsList, lclBookingImport, lclInbondList));
            } else {
                document.add(contentTable(documentName, null, shipperCity.toString(), bookingNumber, itDetails.toString(), ambHouseBL, exportReference, notifyCity.toString(),
                        subHouseBL, DoorDelivery, notifyFax, shipperFax, consigneeCity.toString(), consigneePhone, consigneeFax, shipperPhone, notifyPhone, whseDetails.toString(),
                        deliveryName, deliveryAddress.toString(), deliveryCity, deliveryState, deliveryZip, deliveryEmail, whsePhone, whseFax, deliveryPhone, deliveryFax, polValues.toString(), placeOfReceipt.toString(), unitsNumber, conFileNumber.toString(), vesselName, ssVoyageNo, approximateDate, strippingDate, lastFreeDay,
                        podValues.toString(), eta, sailDate, arrivalDate, goDate, placeOfDelivery.toString(), etaFd, ipiName, agentCity.toString(), agentPhone, agentFax, headingName, SUHeadingNote));
                document.add(commodityTable(documentName, null, externalComments, lclBookingImportAmsList, lclBookingImport, lclInbondList));
            }
            if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLRELEASEORDER)) {
                if (lclUnitSs != null) {
                    document.add(trackingContainerTable(documentName, lclUnitSs, lclBookingImport.getExpressReleaseClause()));
                    document.add(trackingContainerDetailsTable(documentName, lclUnitSs, lclBookingImport.getExpressReleaseClause(), lclBookingImport, lclBookingAcList));
                } else {
                    document.add(trackingContainerTable(documentName, null, lclBookingImport.getExpressReleaseClause()));
                    document.add(trackingContainerDetailsTable(documentName, null, lclBookingImport.getExpressReleaseClause(), lclBookingImport, lclBookingAcList));
                }
                if (lclbooking != null) {
                    document.add(commentsTable(documentName, lclbooking, total, headingName, lclBookingAcList, lclBookingImport.getExpressReleaseClause(), lclBookingImport.getDoorDeliveryComment() ));
                } else {
                    document.add(commentsTable(documentName, null, total, headingName, lclBookingAcList, lclBookingImport.getExpressReleaseClause(), lclBookingImport.getDoorDeliveryComment()));
                }
            } else {
                document.add(contentTable(loginUser));
            }
        }
    }

    public PdfPTable headerTable(String documentName, String unitSsId, String fileNumberId, String path) throws Exception {
        Font blackNormalFont11 = FontFactory.getFont("Arial", 10f, Font.NORMAL);
        Paragraph p = null;
        Phrase pEmpty = null;
        this.setCompanyLogo(fileNumberId);
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100f);
        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLGENERALORDERALERT)) {
            headerTable.setWidths(new float[]{2f, 3f, 1.35f});
        } else {
            headerTable.setWidths(new float[]{2f, 3f, 1.5f});
        }
        cell = new PdfPCell();
        cell.setBorder(0);
        pEmpty = new Phrase("");
        pEmpty.setLeading(30f);
        cell.addElement(pEmpty);
        headerTable.addCell(cell);
        cell = new PdfPCell();
        cell.setRowspan(3);
        cell.setBorder(0);
        cell.setPadding(0f);
        Image img = Image.getInstance(path + imagePath);
        img.scalePercent(60);
        img.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(img);
        headerTable.addCell(cell);
        //empty Cell
        headerTable.addCell(createEmptyCell(0, 0f, 0));

        cell = new PdfPCell();
        cell.setPaddingTop(0.5f);
        cell.setPaddingBottom(2.5f);
        cell.setBorder(0);
        String quote = "\"";
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLRELEASEORDER)) {
            cell.setBorderColor(new BaseColor(178, 00, 00));
            cell.setBorderWidthLeft(1f);
            cell.setBorderWidthBottom(1f);
            cell.setBorderWidthTop(1f);
            cell.setBorderWidthRight(1f);
            p = new Paragraph(10f, "For Online Shipment Tracking go to", blackNormalFont11);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
            p = new Paragraph(10f, brandWebService, voyageBlueFont);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
            p = new Paragraph(10f, "and Click " + quote + "Cargo Tracking" + quote, blackNormalFont11);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
        }
        headerTable.addCell(cell);

        cell = new PdfPCell();
        cell.setPaddingTop(0.5f);
        cell.setPaddingBottom(0.5f);
        cell.setBorder(0);
        PdfPTable nTable = new PdfPTable(1);
        nTable.setWidthPercentage(100f);
        nTable.setWidths(new float[]{1f});
        PdfPCell nCell = null;
        nCell = new PdfPCell();
        nCell.setBorder(0);
        nTable.addCell(nCell);
        nCell = new PdfPCell();
        nCell.setBorder(0);
        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLGENERALORDERALERT)) {
            nCell.setBorderColor(new BaseColor(178, 00, 00));
            nCell.setBorderWidthRight(1f);
            nCell.setBorderWidthLeft(1f);
            nCell.setBorderWidthTop(1f);
            nCell.setBorderWidthBottom(1f);
            nCell.setPaddingBottom(5f);
            p = new Paragraph(10f, "CARGO SCHEDULED TO", redBoldFont10);
            p.setAlignment(Element.ALIGN_CENTER);
            nCell.addElement(p);
            p = new Paragraph(10f, "GO TO GENERAL ORDER.", redBoldFont10);
            p.setAlignment(Element.ALIGN_CENTER);
            nCell.addElement(p);
        } else {
            p = new Paragraph(10f, "" + DateUtils.formatStringDateToAppFormat(new Date()), blueNormalFont13);
            p.setAlignment(Element.ALIGN_LEFT);
            cell.addElement(p);
        }
        nTable.addCell(nCell);
        cell.addElement(nTable);
        headerTable.addCell(cell);

        headerTable.addCell(createEmptyCell(0, 0f, 0));
        headerTable.addCell(createEmptyCell(0, 0f, 0));
        return headerTable;
    }

    public PdfPTable contentTable(String documentName, LclBooking lclbooking, String shipperCity, String bookingNumber, String itDetails,
            String ambHouseBL, String exportReference, String notifyCity, String subHouseBL, String DoorDelivery, String notifyFax, String shipperFax,
            String consigneeCity, String consigneePhone, String consigneeFax, String shipperPhone, String notifyPhone, String whseDetails,
            String deliveryName, String deliveryAddress, String deliveryCity, String deliveryState, String deliveryZip, String deliveryEmail,
            String whsePhone, String whseFax, String deliveryPhone, String deliveryFax, String polValues, String placeOfReceipt, String unitsNumber,
            String conFileNumber, String vesselName, String ssVoyageNo, String approximateDate, String strippingDate, String lastFreeDay,
            String podValues, String eta, String sailDate, String arrivalDate, String goDate, String placeOfDelivery, String etaFd,
            String ipiName, String agentCity, String agentPhone, String agentFax, String headingName, String SUHeadingNote) throws Exception {
        Phrase p = null;
        Font boldHeadingFont = FontFactory.getFont("Arial", 7f, Font.BOLD);
        Font blueArialBoldFont15 = FontFactory.getFont("Arial", 15f, Font.BOLD, new BaseColor(38, 38, 255));
        contentTable = new PdfPTable(8);
        contentTable.setWidthPercentage(100f);
        contentTable.setWidths(new float[]{1f, 2f, 1f, 2.5f, 2f, 2f, 2f, 2f});
        Paragraph p1 = null;
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(8);
        PdfPTable pHeading = new PdfPTable(3);
        pHeading.setWidthPercentage(100f);
        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLGENERALORDERALERT)) {
            pHeading.setWidths(new float[]{2f, 3f, 1.35f});
        } else {
            pHeading.setWidths(new float[]{2f, 3f, 1.5f});
        }
        PdfPCell pCell = new PdfPCell();
        pCell.setBorder(0);
        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLGENERALORDERALERT)) {
            p1 = new Paragraph(10f, "" + DateUtils.formatStringDateToAppFormat(new Date()), blueNormalFont13);
            p1.setAlignment(Element.ALIGN_LEFT);
        } else {
            p1 = new Paragraph(10f, "", blueNormalFont13);
        }
        pCell.addElement(p1);
        pHeading.addCell(pCell);

        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENT_LCL_ARRIVAL_ADVICE_STATUS)) {
            PdfPCell pCell1 = new PdfPCell();
            pCell1.setBorder(0);
            p1 = new Paragraph(10f, "" + SUHeadingNote, blackNormalFont13);
            p1.setAlignment(Element.ALIGN_CENTER);
            pCell1.addElement(p1);
            pHeading.addCell(pCell1);
        }

        PdfPCell pCell2 = new PdfPCell();
        pCell2.setBorder(0);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENT_LCL_ARRIVAL_ADVICE_STATUS)) {
            pCell2.setColspan(2);
        }
        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p1 = new Paragraph(10f, "BILL OF LADING", blueNormalFont14);
        } else if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLRELEASEORDER)) {
            p1 = new Paragraph(10f, "RELEASE ORDER", blueNormalFont14);
        } else if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLGENERALORDERALERT)) {
            p1 = new Paragraph(10f, "GENERAL ORDER ALERT", blueNormalFont14);
        } else if (CommonUtils.isEqualIgnoreCase(documentName, "Freight Invoice")) {
            p1 = new Paragraph(10f, "FREIGHT INVOICE", blueNormalFont14);
        } else {
            p1 = new Paragraph(10f, "" + headingName, blueNormalFont14);
        }
        p1.setAlignment(Element.ALIGN_RIGHT);
        pCell2.addElement(p1);
        pHeading.addCell(pCell2);
        cell.addElement(pHeading);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(8);
        cell.setBorderWidthTop(0.6f);
        contentTable.addCell(cell);
        //empty
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(8);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthTop(0.6f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Paragraph(5f, "NOTIFY PARTY:", boldHeadingFont);
        } else {
            p = new Phrase(5f, "SHIPPER:", boldHeadingFont);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        cell.setBorderWidthRight(0.6f);
        p = new Phrase(5f, "MASTER BILL OF LADING #", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthTop(0.6f);
        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase(5f, "BILL OF LADING#", boldHeadingFont);
        } else {
            p = new Phrase(5f, "INVOICE #", boldHeadingFont);
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        LclUtils lclUtils = new LclUtils();
        //2
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setRowspan(3);
        cell.setPaddingTop(-2f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase("" + notifyCity.toString().toUpperCase(), blackNormalCourierFont9f);
        } else {
            p = new Phrase("" + shipperCity.toString().toUpperCase(), blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthRight(0.6f);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingBottom(5f);
        p = new Phrase(5f, "" + bookingNumber.toUpperCase(), blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setPaddingTop(3f);
        cell.setPaddingBottom(5f);
        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            cell.setPaddingLeft(60f);
        } else {
            cell.setPaddingLeft(40f);
        }
        cell.setBorderWidthBottom(0.6f);
        if (lclbooking != null && lclbooking.getLclFileNumber() != null && lclbooking.getLclFileNumber().getFileNumber() != null) {
            p = new Phrase(5f, "IMP-" + lclbooking.getLclFileNumber().getFileNumber(), blueArialBoldFont15);
        } else {
            p = new Paragraph("");
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        //3
//        cell = new PdfPCell();
//        //cell.setBorder(0);
//        cell.setColspan(4);
//        cell.setRowspan(2);
//        p = new Phrase(5f, "" , blackNormalCourierFont9f);
//        cell.addElement(p);
//        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingBottom(5f);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(5f, "IT #:", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-60f);
        cell.setColspan(3);
        if (CommonUtils.isNotEmpty(itDetails)) {
            p = new Phrase(5f, "" + itDetails.toString().toUpperCase(), blackNormalCourierFont9f);
        } else {
            p = new Phrase(5f, "", blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        //4
//        cell = new PdfPCell();
//    ///    cell.setBorder(0);
//        cell.setColspan(4);
//        p = new Phrase(5f, "", blackNormalCourierFont9f);
//        cell.addElement(p);
//        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase(5f, "AMS House BL #:", boldHeadingFont);
            cell.setPaddingRight(-5f);
        } else {
            p = new Phrase(5f, "Export Reference:", boldHeadingFont);
            cell.setPaddingRight(-20f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-22f);
        cell.setPaddingRight(-30f);
//        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
        p = new Phrase(5f, " " + ambHouseBL.toUpperCase(), blackCourierFont8);
//        } else {
//            p = new Phrase(5f, "" + exportReference.toUpperCase(), blackNormalCourierFont9f);
//        }
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setRowspan(4);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            PdfPTable nTable = new PdfPTable(4);
            nTable.setWidthPercentage(100f);
            nTable.setWidths(new float[]{0.4f, 0.45f, 1.3f, 1f});
            PdfPCell nCell = null;
            nCell = new PdfPCell();
            nCell.setBorder(0);
            nTable.addCell(nCell);
            nCell = new PdfPCell();
            nCell.setBorder(0);
            nCell.setBorderWidthLeft(1f);
            nCell.setBorderWidthTop(1f);
            nCell.setBorderWidthBottom(1f);
            nCell.setBorderColor(new BaseColor(178, 00, 00));
            p = new Phrase(6f, "Note: ", boldHeadingFont);
            nCell.addElement(p);
            nTable.addCell(nCell);

            nCell = new PdfPCell();
            nCell.setBorder(0);
            nCell.setPadding(0f);
            nCell.setPaddingLeft(-3f);
            nCell.setPaddingBottom(2f);
            nCell.setBorderWidthRight(1f);
            nCell.setBorderWidthTop(1f);
            nCell.setBorderWidthBottom(1f);
            nCell.setBorderColor(new BaseColor(178, 00, 00));
            p = new Phrase(8f, " AMS House BL# must be shown on the Customs Entry", boldHeadingFont);
            nCell.addElement(p);
            nTable.addCell(nCell);
            nCell = new PdfPCell();
            nCell.setBorder(0);
            nTable.addCell(nCell);
            cell.addElement(nTable);
        } else {
            p = new Phrase(8f, "", blackNormalCourierFont9f);
        }
        contentTable.addCell(cell);

        //5
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(0f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase(2f, "", blackNormalCourierFont9f);
        } else {
            p = new Phrase(2f, "", blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        cell.setPadding(0f);
        p = new Phrase(2f, "", blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(2f, "", blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

        //6
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase(5f, "", blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(8f, "Sub House BL #:", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-23f);
        p = new Paragraph(8f, " " + subHouseBL.toUpperCase(), blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);
        //7
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase(5f, "", blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(8f, "Door Delivery:", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);
        Font blackContentBoldFont9 = FontFactory.getFont("Arial", 8f, Font.BOLD, BaseColor.BLACK);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-23f);
        p = new Paragraph(8f);
        if (null != DoorDelivery && !"".equalsIgnoreCase(DoorDelivery)) {
            Chunk dl = new Chunk(" " + DoorDelivery.toUpperCase(), blackContentBoldFont9);
            dl.setBackground(BaseColor.YELLOW, 1f, 0.05f, 1f, 1f);
            p.add(dl);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        //8
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingTop(-3f);
        p = new Phrase(5f, "Phone: ", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingLeft(-12f);
        cell.setPaddingTop(-3f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase(5f, "" + notifyPhone, blackNormalCourierFont9f);
        } else {
            p = new Phrase(5f, "" + shipperPhone, blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingTop(-3f);
        p = new Phrase(5f, "Fax: ", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingLeft(-20f);
        cell.setPaddingTop(-3f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase(5f, "" + notifyFax, blackNormalCourierFont9f);
        } else {
            p = new Phrase(5f, "" + shipperFax, blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        if (CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            cell.setBorderWidthBottom(0.6f);
        }
//        p = new Phrase(3f, "", blackNormalCourierFont9f);
//        cell.addElement(p);
        contentTable.addCell(cell);
        //consignee and shipper table
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Phrase(5f, "CONSIGNEE:", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase(5f, "SHIPPER:", boldHeadingFont);
        } else {
            p = new Phrase(5f, "NOTIFY PARTY:", boldHeadingFont);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        //consignee values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(2f);
        cell.setPaddingTop(-1f);
        if (CommonUtils.isNotEmpty(consigneeCity.toString())) {
            p = new Phrase("" + consigneeCity.toString().toUpperCase(), blackNormalCourierFont9f);
        } else {
            p = new Phrase(10f, "", blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setPadding(2f);
        cell.setPaddingTop(-1f);
        cell.setBorderWidthLeft(0.6f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase("" + shipperCity.toString().toUpperCase(), blackNormalCourierFont9f);
        } else {
            p = new Phrase("" + notifyCity.toString().toUpperCase(), blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        //phone
        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(15f, "Phone :", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-10f);
        p = new Phrase(15f, consigneePhone, blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(15f, "Fax :", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-20f);
        p = new Phrase(15f, consigneeFax, blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(15f, "Phone :", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-30f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase(15f, "" + shipperPhone, blackNormalCourierFont9f);
        } else {
            p = new Phrase(15f, "" + notifyPhone, blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Phrase(15f, "Fax :", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingLeft(-60f);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLBILLOFLADING)) {
            p = new Phrase(15f, "" + shipperFax, blackNormalCourierFont9f);
        } else {
            p = new Phrase(15f, "" + notifyFax, blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        //cargo and delivery
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthTop(0.6f);
        p = new Phrase(5f, "CARGO LOCATED AT:", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        cell.setBorderWidthTop(0.6f);
        p = new Phrase(5f, "FOR DELIVERY INFO CALL OR EMAIL:", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);
        //consignee values
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        if (CommonUtils.isNotEmpty(whseDetails)) {
            p = new Phrase(10f, "" + whseDetails.toString().toUpperCase(), blackNormalCourierFont9f);
        } else {
            p = new Phrase(10f, "", blackNormalCourierFont9f);
        }
        cell.addElement(p);
        contentTable.addCell(cell);
        Font blackContentBoldFont8 = FontFactory.getFont("Arial", 8f, Font.BOLD, BaseColor.BLUE);
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(10f, "" + deliveryName.toUpperCase(), blackNormalCourierFont9f);
        cell.addElement(p);
        p = new Phrase(10f, "" + deliveryAddress.toUpperCase(), blackNormalCourierFont9f);
        cell.addElement(p);
        p = new Phrase(10f, "" + deliveryCity.toUpperCase() + " " + deliveryState.toUpperCase() + " " + deliveryZip, blackNormalCourierFont9f);
        cell.addElement(p);
//        String deliveryEmail = "";
//        if (lclbooking != null) {
//            RefTerminal terminal = new RefTerminalDAO().getTerminalByUnLocation(lclbooking.getFinalDestination().getUnLocationCode(), "Y");
//            if (terminal != null && CommonFunctions.isNotNull(terminal.getImportsContactEmail())) {
//                deliveryEmail = terminal.getImportsContactEmail();
//            }
//        }
        p = new Phrase(10f, "(Please Contact:Imports customer service Dept).\n", blackBoldFontSize7);
        p.add(new Chunk("EMAIL:", blackContentBoldFont));
        Chunk c1 = new Chunk("" + deliveryEmail, blackContentBoldFont8);
        c1.setBackground(BaseColor.YELLOW, 1f, 0.05f, 1f, 1f);
        p.add(c1);
        cell.addElement(p);
        contentTable.addCell(cell);
        //phone

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        p = new Phrase(7f, "Phone: ", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingLeft(-12f);
        p = new Phrase(7f, "" + whsePhone, blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

//        cell = new PdfPCell();
//        cell.setBorder(0);
//        cell.setColspan(2);
//        cell.setBorderWidthBottom(0.6f);
//        p = new Phrase("Phone: ", boldHeadingFont);
//        p.add(new Phrase("" + whsePhone, blackNormalCourierFont9f));
//        cell.addElement(p);
//        contentTable.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(0);
//        cell.setPaddingTop(-0.2f);
        cell.setBorderWidthBottom(0.6f);
        p = new Phrase(7f, "Fax: ", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingLeft(-23f);
//        cell.setPaddingTop(-0.2f);
        p = new Phrase(7f, "" + whseFax, blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

//        cell = new PdfPCell();
//        cell.setBorder(0);
//        cell.setColspan(2);
//        cell.setBorderWidthBottom(0.6f);
//        p = new Phrase("Fax: ", boldHeadingFont);
//        p.add(new Phrase("" + whseFax, blackNormalCourierFont9f));
//        cell.addElement(p);
//        contentTable.addCell(cell);q12
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setBorderWidthLeft(0.6f);
        p = new Phrase(7f, "Phone: ", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingLeft(-50f);
        p = new Phrase(7f, "" + deliveryPhone, blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        p = new Phrase(7f, "Fax:", boldHeadingFont);
        cell.addElement(p);
        contentTable.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6f);
        cell.setPaddingLeft(-60f);
        p = new Phrase(7f, "" + deliveryFax, blackNormalCourierFont9f);
        cell.addElement(p);
        contentTable.addCell(cell);
        //receipt
        if (lclbooking != null && lclbooking.getFinalDestination() != null && lclbooking.getPortOfDestination() != null && lclbooking.getFinalDestination() != lclbooking.getPortOfDestination()) {
            contentTable.addCell(makeCellNoBorderValue("DEVANNING CFS", 2, 0.5f, 1f));
        } else {
            contentTable.addCell(makeCellNoBorderValue("", 2, 0.5f, 1f));
        }
        contentTable.addCell(makeCellLeftBorderValue("PLACE OF RECEIPT", 2));
        contentTable.addCell(makeCellLeftBorderValue("CONTAINER #", 2));
        contentTable.addCell(makeCellNoBorderValue("FILE #", 2, 0.5f, 1f));

        //values receipt
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(0.6f);
        cell.setPaddingBottom(2f);
        cell.setBorderWidthBottom(0.6f);
        if (lclbooking != null && lclbooking.getFinalDestination() != null && lclbooking.getPortOfDestination() != null && lclbooking.getFinalDestination() != lclbooking.getPortOfDestination()) {
            contentTable.addCell(makeCellBottomBorderValue("" + ipiName.toUpperCase(), 2, 0.5f, 0.6f, blackNormalCourierFont9f));
        } else {
            contentTable.addCell(makeCellBottomBorderValue("", 2, 0.5f, 0.6f, blackNormalCourierFont9f));
        }
        contentTable.addCell(makeCellLeftBottomBorderValue("" + placeOfReceipt.toString().toUpperCase(), 2, 0.5f, 2f, blackNormalCourierFont9f));
        contentTable.addCell(makeCellLeftBottomBorderValue("" + unitsNumber, 2, 0.5f, 2f, blackNormalCourierFont9f));
        contentTable.addCell(makeCellBottomBorderValue("" + conFileNumber.toString().toUpperCase(), 2, 0.5f, 0.6f, blackNormalCourierFont9f));
        //vessel table details
        contentTable.addCell(makeCellNoBorderValue("VESSEL", 2, 0.5f, 1f));
        contentTable.addCell(makeCellLeftBorderValue("PORT OF LOADING ", 2));
        contentTable.addCell(makeCellLeftBorderValue("APPROXIMATE DUE AT WHSE ", 2));
        contentTable.addCell(makeCellLeftBorderValue("STRIPPING DATE", 0));
        contentTable.addCell(makeCellLeftBorderValue("LFD @ POD", 0));
        //vessel Values details
        if (CommonUtils.isNotEmpty(vesselName)) {
            contentTable.addCell(makeCellBottomBorderValue("" + vesselName + "/" + ssVoyageNo, 2, 0.5f, 0.6f, blackNormalCourierFont9f));
        } else {
            contentTable.addCell(makeCellBottomBorderValue("", 2, 0.5f, 0.6f, blackNormalCourierFont9f));
        }
        if (CommonFunctions.isNotNull(polValues)) {
            contentTable.addCell(makeCellLeftBottomBorderValue("" + polValues.toString().toUpperCase(), 2, 0.5f, 2f, blackNormalCourierFont9f));
        } else {
            contentTable.addCell(makeCellLeftBottomBorderValue("", 2, 0.5f, 2f, blackNormalCourierFont9f));
        }
        contentTable.addCell(makeCellLeftBottomBorderValue("" + approximateDate, 2, 0.5f, 2f, blackNormalCourierFont9f));
        contentTable.addCell(makeCellLeftBottomBorderValue("" + strippingDate, 0, 0.5f, 2f, blackNormalCourierFont9f));
        contentTable.addCell(makeCellLeftBottomBorderValue("" + lastFreeDay, 0, 0.5f, 2f, blackNormalCourierFont9f));
        //pod table details
        contentTable.addCell(makeCellNoBorderValue("PORT OF DISCHARGE", 2, 0.5f, 1f));
        contentTable.addCell(makeCellLeftBorderValue("PLACE OF DELIVERY / ETA @ FD", 2));
        contentTable.addCell(makeCellLeftBorderValue("SAIL DATE", 0));
        contentTable.addCell(makeCellLeftBorderValue("ETA at POD", 0));
        contentTable.addCell(makeCellLeftBorderValue("G.O.DATE", 2));
        //pod table values
        contentTable.addCell(makeCellBottomBorderValue("" + podValues.toString().toUpperCase(), 2, 0.5f, 0.6f, blackNormalCourierFont9f));
        if (placeOfDelivery.toString().length() >= 14) {
            if (!etaFd.equalsIgnoreCase("")) {
                etaFd = etaFd.substring(0, 6);
            }
            contentTable.addCell(makeCellLeftBottomBorderValue("" + placeOfDelivery.toString().toUpperCase() + "/" + etaFd, 2, 0.5f, 3f, blackNormalCourierFont9f));
        } else {
            contentTable.addCell(makeCellLeftBottomBorderValue("" + placeOfDelivery.toString().toUpperCase() + "/" + etaFd, 2, 0.5f, 3f, blackNormalCourierFont9f));
        }
        contentTable.addCell(makeCellLeftBottomBorderValue("" + sailDate, 0, 0.5f, 3f, blackNormalCourierFont9f));
        contentTable.addCell(makeCellLeftBottomBorderValue("" + eta, 0, 0.5f, 3f, blackNormalCourierFont9f));
        contentTable.addCell(makeCellLeftBottomBorderValue("" + goDate, 2, 0.5f, 3f, blackNormalCourierFont9f));
        //empty cell
        contentTable.addCell(makeCellBottomBorderValue("", 8, 0.5f, 0.6f, blackNormalCourierFont9f));
        return contentTable;
    }

    public PdfPTable commodityTable(String documentName, LclBooking lclbooking, String externalComments, List<LclBookingImportAms> lclBookingImportAmsList, LclBookingImport lclBookingImport, List<LclInbond> lclInbondList) throws DocumentException, Exception {
        Paragraph p = null;
        Phrase pValues = null;
        int bookingPieceCount = 0;
        Font boldHeadingFont = FontFactory.getFont("Arial", 7f, Font.BOLD);
        table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 1f, 4f, 1.3f, 1.3f});
        //empty cell
        table.addCell(createEmptyCell(0, 3f, 5));
        table.addCell(makeCellBottomBorderValue("", 5, 0.5f, 0.6f, blackNormalCourierFont9f));
        //marks
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        p = new Paragraph(7f, "MARKS AND NUMBERS", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //no of pkgs
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "NO.OF.PKGS", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //desc
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "DESCRIPTION OF PACKAGES AND GOODS", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //grossweight
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "GROSS WEIGHT", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        //measure
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.6F);
        cell.setBorderWidthLeft(0.6f);
        p = new Paragraph(7f, "MEASURE", boldHeadingFont);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        List<LclBookingPiece> lclBookingPiecesList = null;
        Lcl3pRefNo lcl3pRefNoaes = null;
        Lcl3pRefNo lcl3pRefNoNcm = null;
        List<Lcl3pRefNo> lcl3pRefNoaesList, lcl3pRefNoNcmList = null;
        if (lclbooking != null) {
            String fileId = lclbooking.getFileNumberId().toString();
            lcl3pRefNoNcmList = new Lcl3pRefNoDAO().getLclHscCodeListByType(fileId, LclReportConstants.NCM_REF);
            if (!lcl3pRefNoNcmList.isEmpty()) {
                lcl3pRefNoNcm = lcl3pRefNoNcmList.get(0);
            }
            lcl3pRefNoaesList = new Lcl3pRefNoDAO().getLclHscCodeListByType(fileId, LclReportConstants.AES_NUMBER);
            if (lcl3pRefNoaesList.isEmpty()) {
                lcl3pRefNoaesList = new Lcl3pRefNoDAO().getLclHscCodeListByType(fileId, LclReportConstants.AES_NUMBER_EXCEPT);
            } else {
                lcl3pRefNoaes = lcl3pRefNoaesList.get(0);
            }
            //values
            lclBookingPiecesList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclbooking.getLclFileNumber().getId());
        }
        boolean flag = false;
        if (lclBookingPiecesList != null && lclBookingPiecesList.size() > 0) {
            for (LclBookingPiece lclBookingPiece : lclBookingPiecesList) {
                bookingPieceCount++;
                //marks
                cell = new PdfPCell();
//                cell.setFixedHeight(25f);
                cell.setBorder(0);
                if (lclBookingPiece != null && lclBookingPiece.getMarkNoDesc() != null && !lclBookingPiece.getMarkNoDesc().equals("")) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getMarkNoDesc().toUpperCase(), blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                if (lcl3pRefNoNcm != null && lcl3pRefNoNcm.getReference() != null) {
                    p = new Paragraph(7f, "NCM: " + lcl3pRefNoNcm.getReference().toUpperCase(), blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(7f, " ", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                if (lcl3pRefNoaes != null && lcl3pRefNoaes.getReference() != null) {
                    p = new Paragraph(7f, "AES: " + lcl3pRefNoaes.getReference().toUpperCase(), blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(7f, " ", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                cell.addElement(p);
                table.addCell(cell);
                //no of pkgs
                cell = new PdfPCell();
                cell.setBorder(0);
//                cell.setFixedHeight(25f);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPiece != null && lclBookingPiece.getBookedPieceCount() != null && lclBookingPiece.getPackageType().getAbbr01() != null) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getBookedPieceCount() + " " + lclBookingPiece.getPackageType().getAbbr01(), blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                //desc
                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                PdfPTable amsTable = new PdfPTable(3);
                amsTable.setWidthPercentage(99f);
                amsTable.setWidths(new float[]{.6f, 4.1f, .6f});
                PdfPCell amsCell = null;
                amsCell = new PdfPCell();
                amsCell.setBorder(0);
                amsCell.setColspan(3);
                amsCell.setPaddingBottom(3f);
                if (lclBookingPiece != null && lclBookingPiece.getPieceDesc() != null) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getPieceDesc().toUpperCase(), blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_LEFT);
                amsCell.addElement(p);
                amsTable.addCell(amsCell);
                if (bookingPieceCount == lclBookingPiecesList.size()) {
                    if (lclBookingImportAmsList != null && !lclBookingImportAmsList.isEmpty() && lclBookingImportAmsList.size() > 1) {
                        amsCell = new PdfPCell();
                        p = new Paragraph(6f, "SCAC", boldHeadingFont);
                        amsCell.addElement(p);
                        amsTable.addCell(amsCell);
                        amsCell = new PdfPCell();
                        p = new Paragraph(6f, "AMS No", boldHeadingFont);
                        p.setAlignment(Element.ALIGN_CENTER);
                        amsCell.addElement(p);
                        amsTable.addCell(amsCell);
                        amsCell = new PdfPCell();
                        p = new Paragraph(6f, "Pcs", boldHeadingFont);
                        amsCell.addElement(p);
                        amsTable.addCell(amsCell);
                        int count = 0;
                        for (LclBookingImportAms lclBookingImportAms : lclBookingImportAmsList) {
                            amsCell = new PdfPCell();
                            if (count == 0) {
                                if (CommonUtils.isNotEmpty(lclBookingImportAms.getScac())) {
                                    p = new Paragraph(6f, "" + lclBookingImportAms.getScac().toUpperCase(), blackNormalFont7);
                                } else {
                                    p = new Paragraph(6f, "");
                                }
                            } else {
                                if (CommonUtils.isNotEmpty(lclBookingImportAms.getScac())) {
                                    p = new Paragraph(6f, "" + lclBookingImportAms.getScac().toUpperCase(), blackNormalFont7);
                                } else {
                                    p = new Paragraph(6f, "");
                                }
                            }
                            amsCell.addElement(p);
                            amsTable.addCell(amsCell);
                            amsCell = new PdfPCell();
                            p = new Paragraph(6f, "" + lclBookingImportAms.getAmsNo(), blackNormalFont7);
                            amsCell.addElement(p);
                            amsTable.addCell(amsCell);
                            amsCell = new PdfPCell();
                            p = new Paragraph(6f, "" + lclBookingImportAms.getPieces(), blackNormalFont7);
                            amsCell.addElement(p);
                            amsTable.addCell(amsCell);
                            count++;
                        }
                    }
                }
                cell.addElement(amsTable);
                table.addCell(cell);

                //grossweight
                cell = new PdfPCell();
                cell.setBorder(0);
//                cell.setFixedHeight(25f);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPiece != null && lclBookingPiece.getBookedWeightMetric() != null) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getBookedWeightMetric() + " KGS", blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                //measure
                cell = new PdfPCell();
                cell.setBorder(0);
//                cell.setFixedHeight(25f);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPiece != null && lclBookingPiece.getBookedVolumeMetric() != null) {
                    p = new Paragraph(7f, "" + lclBookingPiece.getBookedVolumeMetric() + " CBM", blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(7f, "", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                //2nd cell
                flag = true;

                if (bookingPieceCount == lclBookingPiecesList.size()) {
                    cell = new PdfPCell();
                    cell.setBorder(0);
                    p = new Paragraph(0f, "", blackNormalCourierFont9f);
                    p.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(p);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.6f);
                    p = new Paragraph(1f, "", blackNormalCourierFont9f);
                    p.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(p);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.6f);
                    PdfPTable itTable = new PdfPTable(3);
                    itTable.setWidthPercentage(99f);
                    itTable.setWidths(new float[]{1.6f, 1.4f,1.9f});
                    PdfPCell itCell = null;
                    if (lclInbondList != null && !lclInbondList.isEmpty() && lclInbondList.size() > 1) {
                        itCell = new PdfPCell();
                        p = new Paragraph(6f, "Inbond", boldHeadingFont);
                        itCell.addElement(p);
                        itTable.addCell(itCell);
                        itCell = new PdfPCell();
                        p = new Paragraph(6f, "IT Date", boldHeadingFont);
                        itCell.addElement(p);
                        itTable.addCell(itCell);
                        itCell = new PdfPCell();
                        p = new Paragraph(6f, "Port", boldHeadingFont);
                        itCell.addElement(p);
                        itTable.addCell(itCell);

                        for (Iterator iter = lclInbondList.iterator(); iter.hasNext();) {
                            itCell = new PdfPCell();
                            LclInbond lclInbond = (LclInbond) iter.next();

                            if (CommonUtils.isNotEmpty(lclInbond.getInbondNo())) {
                                p = new Paragraph(6f, "" + lclInbond.getInbondNo().toUpperCase(), blackNormalFont7);
                            } else {
                                p = new Paragraph(6f, "");
                            }
                            itCell.addElement(p);
                            itTable.addCell(itCell);
                            itCell = new PdfPCell();
                            p = new Paragraph(6f, "" + (null != lclInbond.getInbondDatetime() ? (DateUtils.formatDate(lclInbond.getInbondDatetime(), "dd-MMM-yyyy")) : ""), blackNormalFont7);
                            itCell.addElement(p);
                            itTable.addCell(itCell);
                            itCell = new PdfPCell();
                            p = new Paragraph(6f, "" + (null != lclInbond.getInbondPort() ? (lclInbond.getInbondPort().getUnLocationName()) : ""), blackNormalFont7);
                            itCell.addElement(p);
                            itTable.addCell(itCell);

                        }
                    }
                    cell.addElement(itTable);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.6f);
                    p = new Paragraph(1f, "", blackNormalCourierFont9f);
                    p.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(p);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.6f);
                    p = new Paragraph(1f, "", blackNormalCourierFont9f);
                    p.setAlignment(Element.ALIGN_CENTER);
                    cell.addElement(p);
                    table.addCell(cell);
                }
                cell = new PdfPCell();
                cell.setBorder(0);
                p = new Paragraph(1f, "", blackNormalCourierFont9f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(1f, "", blackNormalCourierFont9f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                p = new Paragraph(1f, "", blackNormalCourierFont9f);
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPiece != null && lclBookingPiece.getBookedWeightImperial() != null) {
                    p = new Paragraph(1f, "" + lclBookingPiece.getBookedWeightImperial() + " LBS", blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(1f, "", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                cell.setBorderWidthLeft(0.6f);
                if (lclBookingPiece != null && lclBookingPiece.getBookedVolumeImperial() != null) {
                    p = new Paragraph(1f, "" + lclBookingPiece.getBookedVolumeImperial() + " CFT", blackNormalCourierFont9f);
                } else {
                    p = new Paragraph(1f, "", blackNormalCourierFont9f);
                }
                p.setAlignment(Element.ALIGN_CENTER);
                cell.addElement(p);
                table.addCell(cell);
                
                if (lclBookingPiecesList.size() == 1) {
                    cell = new PdfPCell();
                    cell.setFixedHeight(35f);
                    cell.setBorder(0);
//                    cell.setBorderWidthRight(0.6f);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setFixedHeight(35f);
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.6f);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setFixedHeight(35f);
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.6f);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setFixedHeight(35f);
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.6f);
                    table.addCell(cell);
                    cell = new PdfPCell();
                    cell.setFixedHeight(35f);
                    cell.setBorder(0);
                    cell.setBorderWidthLeft(0.6f);
                    table.addCell(cell);
                }
            }
        }
        cell = new PdfPCell();
        cell.setColspan(5);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.6f);
        table.addCell(cell);
        //emptycell
        table.addCell(createEmptyCell(0, 0.001f, 5));
        //customer comments
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLRELEASEORDER)) {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            pValues = new Phrase(8f, "COMMENTS:", ratessubHeadingQuote);
            cell.addElement(pValues);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setBorderWidthTop(0.6f);
            cell.setBorderWidthBottom(0.6f);
            cell.setPaddingLeft(-60f);
            if (!externalComments.equals("")) {
                pValues = new Phrase(8f, "" + externalComments.toUpperCase(), blueNormalFontArial9);
            } else {
                pValues = new Phrase(8f, "", blueNormalFontArial9);
            }
            cell.addElement(pValues);
            table.addCell(cell);
            //emptycell
            table.addCell(createEmptyCell(0, 1f, 5));
        }
        return table;
    }

    public PdfPTable trackingContainerTable(String documentName, LclUnitSs lclUnitSs, Boolean expressRelease) throws DocumentException, Exception {
        table = new PdfPTable(8);
        Paragraph p = null;
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{2f, 0.25f, 1.5f, 0.25f, 4.5f, 1.5f, 5f, .5f});
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        Chunk c1 = new Chunk("DATE", greenCourierFont9);
        cell.addElement(c1);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, .5f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        c1 = new Chunk("TIME", greenCourierFont9);
        cell.addElement(c1);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, .5f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        c1 = new Chunk("CONTAINER STATUS", greenCourierFont9);
        cell.addElement(c1);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, .5f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(2);
        if (expressRelease) {
            c1 = new Chunk("EXPRESS RELEASE (*)", totalFontQuote);
        } else {
            c1 = new Chunk("ORIGINAL B/L REQUIRED", totalFontQuote);
        }
        c1.setBackground(BaseColor.YELLOW, 1f, 1f, 1f, 0.05f);
        cell.addElement(c1);
        table.addCell(cell);
        //table.addCell(createEmptyCell(0, .5f, 0));
        return table;
    }

    public PdfPTable trackingContainerDetailsTable(String documentName, LclUnitSs lclUnitSs, Boolean expressRelease,
            LclBookingImport lclBookingImport, List<BookingChargesBean> lclBookingAcList) throws DocumentException, Exception {
        Paragraph p = null;

        table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3.25f, .05f, 2.65f});
        cell = new PdfPCell();
        cell.setBorder(0);
        PdfPTable ntable = new PdfPTable(5);
        ntable.setWidthPercentage(103f);
        ntable.setWidths(new float[]{.75f, .10f, .5f, .10f, 1.7f});
        PdfPCell ncell = new PdfPCell();
        boolean flag = false;
        if (lclBookingImport != null && lclBookingImport.getFdEta() != null) {
            Date etaFd = DateUtils.formatDateAndParseToDate(lclBookingImport.getFdEta());
            Date nowDate = DateUtils.formatDateAndParseToDate(new Date());
            flag = nowDate.before(etaFd);
        }
        if (lclUnitSs != null && lclUnitSs.getLclUnit() != null) {
            LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
            List<LclUnitSsDispo> dispositionList = new LclUnitSsDispoDAO().getUnitDispoDetailsWithoutData(lclUnitSs.getLclUnit().getId(), lclSsDetail.getId());
            if (!dispositionList.isEmpty()) {
                Collections.reverse(dispositionList);
                int i;
                for (i = 0; i < dispositionList.size(); i++) {
                    LclUnitSsDispo lclUnitSsDispo = (LclUnitSsDispo) dispositionList.get(i);
                    if ("AVAL".equalsIgnoreCase(lclUnitSsDispo.getDisposition().getEliteCode()) && flag) {
                        break;
                    }
                    String dateTimeV = DateUtils.formatDate(lclUnitSsDispo.getDispositionDatetime(), "dd-MMM-yyyy hh:mm a");
                    String[] dateTimeArray1 = dateTimeV.split(" ");

                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + dateTimeArray1[0], greenCourierFont9);
                    ncell.addElement(p);
                    ntable.addCell(ncell);

                    ntable.addCell(createEmptyCell(0, .5f, 0));

                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + dateTimeArray1[1] + " " + dateTimeArray1[2], greenCourierFont9);
                    ncell.addElement(p);
                    ntable.addCell(ncell);

                    ntable.addCell(createEmptyCell(0, .5f, 0));

                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    if (lclUnitSsDispo != null && lclUnitSsDispo.getDisposition() != null && lclUnitSsDispo.getDisposition().getDescription() != null) {
                        p = new Paragraph(5f, "" + lclUnitSsDispo.getDisposition().getDescription(), greenCourierFont9);
                    } else {
                        p = new Paragraph(5f, "", greenCourierFont9);
                    }
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                }
                cell.addElement(ntable);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(0);
                ntable = new PdfPTable(2);
                ntable.setWidthPercentage(103f);
                ntable.setTotalWidth(new float[]{2.5f, 2f});

                ncell = new PdfPCell();
                ncell.setBorder(0);
                ncell.setColspan(2);
                ncell.setPaddingTop(-10f);
                ncell.setPaddingRight(50f);
                ncell.setPaddingBottom(5f);
                Paragraph p1 = new Paragraph();
                if (expressRelease) {
                    p1.add(new Chunk(" BILL OF LADING NOT REQUIRED", totalFontQuote).setBackground(BaseColor.YELLOW, 1f, 1f, 1f, 0.05f));
                    p1.setAlignment(Element.ALIGN_CENTER);
                } else {
                    p1.add("");
                }
                ncell.addElement(p1);
                ntable.addCell(ncell);

                if (lclBookingImport.getOriginalBlReceived() != null) {
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + "ORIGINAL B/L RECEIVED", totalFontQuote);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + DateUtils.formatDateToDateTimeString(lclBookingImport.getOriginalBlReceived()), totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                }
                if (lclBookingImport.getCustomsClearanceReceived() != null) {
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + "CUSTOMS CLEARANCE RECEIVED", totalFontQuote);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + DateUtils.formatDateToDateTimeString(lclBookingImport.getCustomsClearanceReceived()), totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                }
                if (lclBookingImport.getDeliveryOrderReceived() != null) {
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + "DELIVERY ORDER RECEIVED", totalFontQuote);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + DateUtils.formatDateToDateTimeString(lclBookingImport.getDeliveryOrderReceived()), totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                }
                if (lclBookingImport.getReleaseOrderReceived() != null) {
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + "RELEASE ORDER RECEIVED", totalFontQuote);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + DateUtils.formatDateToDateTimeString(lclBookingImport.getReleaseOrderReceived()), totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                }
                if (lclBookingImport.getFreightReleaseDateTime() != null) {
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + "FREIGHT RELEASE", totalFontQuote);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + DateUtils.formatDateToDateTimeString(lclBookingImport.getFreightReleaseDateTime()), totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                }
                if (lclBookingImport.getPaymentReleaseReceived() != null) {
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + "PAYMENT RELEASE", totalFontQuote);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                    ncell = new PdfPCell();
                    ncell.setBorder(0);
                    p = new Paragraph(5f, "" + DateUtils.formatDateToDateTimeString(lclBookingImport.getPaymentReleaseReceived()), totalFontQuote);
                    p.setAlignment(Element.ALIGN_LEFT);
                    ncell.addElement(p);
                    ntable.addCell(ncell);
                }
                cell.addElement(ntable);
                table.addCell(cell);
            }
        }
        cell = new PdfPCell();
        cell.setFixedHeight(3f);
        cell.setColspan(3);
        cell.setBorder(0);
        cell.setBorderWidthTop(0.06f);
        p = new Paragraph(" ", blackBoldFontSize7);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public PdfPTable commentsTable(String documentName, LclBooking lclbooking, Double total, String headingName, List<BookingChargesBean> lclBookingAcList, Boolean expressReleaseClause, Boolean doorDeliveryComment) throws DocumentException, Exception {
        Paragraph p = null;
        PdfPTable ntable = null;
        PdfPCell ncell = null;
        Font blueNormalFontArial7 = FontFactory.getFont("Arial", 7f, Font.NORMAL, new BaseColor(0, 0, 128));
        table = new PdfPTable(4);
        table.setKeepTogether(true);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.70f, 1f, .10f, 4.5f});
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        p = new Paragraph(7f, "FREIGHT CHARGES", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(3f, " ", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        ntable = new PdfPTable(1);
        ntable.setWidthPercentage(103f);
        ncell = new PdfPCell();
        ncell.setBorder(0);
        ncell.setBorderWidthTop(0.06f);
        if (lclBookingAcList != null) {
            for (int i = 0; i < lclBookingAcList.size(); i++) {
                BookingChargesBean lclBookingAc = (BookingChargesBean) lclBookingAcList.get(i);
                if (headingName.equalsIgnoreCase("Third Party Invoice")) {
                    if (lclBookingAc.getArBillToParty().equalsIgnoreCase("T")) {
                        if (lclBookingAc != null && lclBookingAc.getBalanceAmt() != null && lclBookingAc.getBalanceAmt().doubleValue() != 0.00 || (lclBookingAc.getPaidAmt().doubleValue() == 0.00)) {
                            p = new Paragraph(10f, "" + lclBookingAc.getChargeCode().trim().toUpperCase(), blueNormalFontArial7);
                            ncell.addElement(p);
                        }
                    }
                } else if (documentName.equalsIgnoreCase("Freight Invoice")) {
                    p = new Paragraph(10f, "" + lclBookingAc.getChargeCode().trim().toUpperCase(), blueNormalFontArial7);
                    ncell.addElement(p);
                } else {
                    if (!(lclBookingAc.getArBillToParty().equalsIgnoreCase("T"))) {
                        if (lclBookingAc != null && lclBookingAc.getBalanceAmt() != null && lclBookingAc.getBalanceAmt().doubleValue() != 0.00 || (lclBookingAc.getPaidAmt().doubleValue() == 0.00)) {
                            p = new Paragraph(10f, "" + lclBookingAc.getChargeCode().trim().toUpperCase(), blueNormalFontArial7);
                            ncell.addElement(p);
                        }
                    }
                }
            }
        } else {
            p = new Paragraph(10f, "", blueNormalFontArial7);
            ncell.addElement(p);
        }
        ntable.addCell(ncell);
        cell.addElement(ntable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        cell.setBorderWidthRight(0.06f);
        p = new Paragraph(7f, "AMOUNT", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        p = new Paragraph(3f, " ", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        ntable = new PdfPTable(2);
        ntable.setWidthPercentage(103f);
        ntable.setWidths(new float[]{.75f, .25f});
        ncell = new PdfPCell();
        ncell.setBorder(0);
        ncell.setBorderWidthTop(0.06f);
        if (lclBookingAcList != null) {
            for (int i = 0; i < lclBookingAcList.size(); i++) {
                BookingChargesBean lclBookingAc = (BookingChargesBean) lclBookingAcList.get(i);
                if (headingName.equalsIgnoreCase("Third Party Invoice")) {
                    if (lclBookingAc.getArBillToParty().equalsIgnoreCase("T")) {
                        if (lclBookingAc != null && lclBookingAc.getBalanceAmt() != null && lclBookingAc.getBalanceAmt().doubleValue() != 0.00) {
                            p = new Paragraph(10f, "" + lclBookingAc.getBalanceAmt(), blueNormalFontArial7);
                            p.setAlignment(Element.ALIGN_RIGHT);
                            ncell.addElement(p);
                        } else if (lclBookingAc != null && lclBookingAc.getPaidAmt().doubleValue() == 0.00) {
                            p = new Paragraph(10f, "" + lclBookingAc.getTotalAmt(), blueNormalFontArial7);
                            p.setAlignment(Element.ALIGN_RIGHT);
                            ncell.addElement(p);
                        }
                    }
                } else if (documentName.equalsIgnoreCase("Freight Invoice")) {
                    p = new Paragraph(10f, "" + lclBookingAc.getTotalAmt(), blueNormalFontArial7);
                    p.setAlignment(Element.ALIGN_RIGHT);
                    ncell.addElement(p);
                } else {
                    if (!(lclBookingAc.getArBillToParty().equalsIgnoreCase("T"))) {
                        if (lclBookingAc != null && lclBookingAc.getBalanceAmt() != null && lclBookingAc.getBalanceAmt().doubleValue() != 0.00) {
                            p = new Paragraph(10f, "" + lclBookingAc.getBalanceAmt(), blueNormalFontArial7);
                            p.setAlignment(Element.ALIGN_RIGHT);
                            ncell.addElement(p);
                        } else if (lclBookingAc != null && lclBookingAc.getPaidAmt().doubleValue() == 0.00) {
                            p = new Paragraph(10f, "" + lclBookingAc.getTotalAmt(), blueNormalFontArial7);
                            p.setAlignment(Element.ALIGN_RIGHT);
                            ncell.addElement(p);
                        }
                    }

                }
            }
        } else {
            p = new Paragraph(10f, "", blueNormalFontArial7);
            p.setAlignment(Element.ALIGN_RIGHT);
            ncell.addElement(p);
        }
        this.setCompanyLogo(String.valueOf(lclbooking.getFileNumberId()));
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        ncell.setBorderWidthTop(0.06f);
        ntable.addCell(ncell);
        cell.addElement(ntable);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        ntable = new PdfPTable(1);
        ntable.setWidthPercentage(100f);
        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(0f, "ATTENTION", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        p = new Paragraph(8f, "PLEASE SEND PAYMENT TO " + companyName.toUpperCase() + ". IN THE AMOUNT OF\n $", blackNormalFont7);
        p.add(new Chunk(" " + NumberUtils.convertToTwoDecimal(total)).setUnderline(0.1f, -2f));
        ncell.addElement(p);
        p = new Paragraph(8f, " NO PERSONAL CHECKS. VISA AND MASTERCARD ACCEPTED.\n CERTIFIED CHECK OR MONEY ORDER REQUIRED IF THE PAYMENT AMOUNT EXCEEDS $2500.\n PLEASE WRITE THE FOLLOWING REFERENCE # ON YOUR CHECK: ", blackNormalFont7);
        if (lclbooking != null && lclbooking.getLclFileNumber() != null && lclbooking.getLclFileNumber().getFileNumber() != null) {
            p.add(new Chunk("IMP-" + lclbooking.getLclFileNumber().getFileNumber()).setUnderline(0.1f, -2f));
        }
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        ncell.setFixedHeight(18f);
        p = new Paragraph(6f, "PRIOR TO CARGO PICK UP, CONSIGNEE MUST CLEAR CUSTOMS AND CONFIRM FREIGHT RELEASE WITH THE PIER/WAREHOUSE.", blackBoldFontSize7);
        ncell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        if (expressReleaseClause) {
            p = new Paragraph(7f, "EXPRESS RELEASE,NO ORIGINAL BILL OF LADING REQUIRED,BUT\n*CHARGES MUST BE SURRENDERED TO ECI BEFORE RELEASE OF CARGO CAN BE OBTAINED.", blackNormalFont7);
        } else {
            p = new Paragraph(7f, "CHARGES MUST BE SURRENDERED TO ECI BEFORE RELEASE OF CARGO CAN BE OBTAINED.", blackNormalFont7);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        ncell.setPaddingTop(5f);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        if (!CommonUtils.isEqual(documentName, LclReportConstants.DOCUMENTLCLCFSINSTRUCTIONS)) {
            p = new Paragraph(7f, "PLEASE ALLOW 48 HOURS FROM THE TIME THAT ECI RECEIVES YOUR PAYMENT AND/OR ORIGINAL BILL OF LADING FOR PROCESSING OF FREIGHT RELEASE.", blackBoldFontSize7);
        } else {
            p = new Paragraph(7f, " ", blackBoldFontSize7);
        }
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(7f, "WE DO NOT ACCEPT FAXED COPIES OF THE ORIGINALS,FAXED COPIES OF CHECK,OR FAXED COPIES OF CUSTOMS CLEARANCE,FOR RELEASE.", blackNormalFont7);
        p.setAlignment(Element.ALIGN_LEFT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        ncell.setPaddingTop(3f);
        ntable.addCell(ncell);
        cell.addElement(ntable);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(2);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthTop(0.06f);
        p = new Paragraph(7f, "5 Calendar Days Free Storage", labelRatesQuote);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setRowspan(2);
        cell.setBorderWidthRight(0.06f);
        cell.setBorderWidthBottom(0.06f);
        cell.setBorderWidthTop(0.06f);
        ntable = new PdfPTable(2);
        ntable.setWidthPercentage(100f);
        ntable.setWidths(new float[]{.50f, .25f});
        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(7f, "TOTAL ", blackBoldFontSize7);
        p.setAlignment(Element.ALIGN_CENTER);
        ncell.addElement(p);
        p = new Paragraph("" + NumberUtils.convertToTwoDecimal(total), blueNormalFontArial7);
        p.setAlignment(Element.ALIGN_RIGHT);
        ncell.addElement(p);
        ntable.addCell(ncell);

        ncell = new PdfPCell();
        ncell.setBorder(0);
        p = new Paragraph(" ", blueNormalFontArial7);
        p.setAlignment(Element.ALIGN_RIGHT);
        ncell.addElement(p);
        ntable.addCell(ncell);
        cell.addElement(ntable);
        table.addCell(cell);

        table.addCell(createEmptyCell(0, 0f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "ADDITIONAL CHARGES RESULTING FROM U.S.CUSTOMS SECURITY EXAMINATION MAY BE ADDED\nTO OUR INVOICE PRIOR TO RELEASE AND PICK UP OF YOUR CARGO. THESE CHARGES, IF BILLED,\nWILL BE DETAILED AS *CUSTOMS EXAM* CHARGES ON OUR INVOICE.", blackNormalFont7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
       
        table.addCell(createEmptyCell(0, 0f, 0));

        cell = new PdfPCell();
        cell.setBorder(0);
        p = new Paragraph(7f, "** DESTINATION CFS AND D/O CHARGES FOR CONSIGNEES ACCOUNT **", blackNormalFont7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        
        if(doorDeliveryComment){
        String Comment = LoadLogisoftProperties.getProperty("DoorDeliveryComment");
        String importsDoorDeliveryEmail = new TerminalDAO().getImportsDoorDeliveryEmail(lclbooking.getTerminal().getTrmnum());
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        Paragraph para = new Paragraph(8f, "", blackContentBoldFont);
        para.setAlignment(Element.ALIGN_LEFT);
       
        Chunk c1 = new Chunk(Comment,blackContentBoldFont);
        c1.setBackground(BaseColor.YELLOW);
        para.add(c1);

        Chunk c2 = new Chunk(" " +importsDoorDeliveryEmail,blackContentBoldFont);
        c2.setBackground(BaseColor.YELLOW);
        Anchor anchor = new Anchor(c2);
        anchor.setReference("mailto:"+importsDoorDeliveryEmail);
        Phrase ph = new Phrase();
        ph.add(anchor);
        para.add(ph);
        cell.addElement(para);
        table.addCell(cell);
      }else{
        table.addCell(createEmptyCell(0, 0f, 4));  
      }
        
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setColspan(4);
        p = new Paragraph(7f, "**PLEASE PICK UP YOUR CARGO AS SOON AS POSSIBLE\nTO AVOID ANY UNNECESSARY CHARGES.", blackNormalFont7);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        table.addCell(cell);
        
        if (headingName.equalsIgnoreCase("Third Party Invoice")) {
            cell = new PdfPCell();
            cell.setBorder(0);
            cell.setColspan(4);
            cell.setPaddingLeft(-150f);
            p = new Paragraph(10f, "FREIGHT PAYABLE BY : ", blackBoldFontSize8);
            Chunk c1 = new Chunk("" + lclbooking.getThirdPartyAcct().getAccountName() + "," + lclbooking.getThirdPartyAcct().getAccountno(), blackNormalCourierFont9f);
            p.add(c1);
            p.setAlignment(Element.ALIGN_CENTER);
            cell.addElement(p);
            table.addCell(cell);
        }
        return table;
    }

    public PdfPTable contentTable(User loginUser) throws DocumentException, Exception {
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        //empty space
        table.addCell(createEmptyCell(0, 5f, 0));
        table.addCell(makeCellNoBorderFontalign("Date:" + DateUtils.formatDate(new Date(), "dd-MMM-yyyy"), -1f, Element.ALIGN_LEFT, 0, blackBoldFont));
        //empty space
        table.addCell(createEmptyCell(0, 5f, 0));
        table.addCell(makeCellNoBorderFontalign("This is to authorize the release of the above-mentioned freight to the \n above-mentioned consignee or representative.", -1f, Element.ALIGN_LEFT, 0, blackBoldFont));
        //empty space
        table.addCell(createEmptyCell(0, 5f, 0));
        table.addCell(makeCellNoBorderFontalign("Storage,demurrage or per diem charges,are the responsibility of the consignee.", -1f, Element.ALIGN_LEFT, 0, blackBoldFont));
        //empty space
        table.addCell(createEmptyCell(0, 5f, 0));
        table.addCell(makeCellNoBorderFontalign("Please confirm availability with warehouse prior to dispatching your driver. Econocaribe is not responsible for driver waiting time or attempted pick-ups.", -1f, Element.ALIGN_LEFT, 0, blackBoldFont));
        //empty space
        table.addCell(createEmptyCell(0, 7f, 0));
        table.addCell(makeCellNoBorderFontalign("Released By", -1f, Element.ALIGN_LEFT, 0, blackBoldFont));
        // table.addCell(makeCellNoBorderFontalign("Released By", -1f, Element.ALIGN_LEFT, 0, blackBoldFont10));
        Chunk underline = null;
        cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPaddingTop(-2f);
        StringBuilder loginName = new StringBuilder();
        if (loginUser.getFirstName() != null) {
            loginName.append(loginUser.getFirstName());
        }
        if (loginUser.getLastName() != null) {
            loginName.append(loginUser.getLastName());
        }
        underline = new Chunk("" + loginName, blackBoldFont);
        underline.setUnderline(0.6f, -2f);
        cell.addElement(underline);
        table.addCell(cell);
        return table;
    }

    public PdfPTable footerTable() throws DocumentException, Exception {
        cell = new PdfPCell();
        cell.setBorder(0);
        table = new PdfPTable(1);
        table.setWidthPercentage(100f);
        Paragraph p = new Paragraph(companyName, labelRatesQuote);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        table.addCell(cell);
        return table;
    }

    public void setCompanyLogo(String fileId) throws Exception {
        String brand = new LclFileNumberDAO().getBusinessUnit(fileId);
        if (CommonUtils.isNotEmpty(brand) && "ECI".equalsIgnoreCase(brand)) {
            this.imagePath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            this.brandWebService = LoadLogisoftProperties.getProperty("application.Econo.website");
            this.companyName = LoadLogisoftProperties.getProperty("application.Econo.companyname");
        } else if (CommonUtils.isNotEmpty(brand) && "OTI".equalsIgnoreCase(brand)) {
            this.imagePath = LoadLogisoftProperties.getProperty("application.image.econo.logo");
            this.brandWebService = LoadLogisoftProperties.getProperty("application.OTI.website");
            this.companyName = LoadLogisoftProperties.getProperty("application.OTI.companyname");
        } else {
            this.imagePath = LoadLogisoftProperties.getProperty("application.image.logo");
            this.brandWebService = LoadLogisoftProperties.getProperty("application.ECU.website");
            this.companyName = LoadLogisoftProperties.getProperty("application.ECU.companyname");
        }

    }
}
