/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import static com.gp.cong.common.CommonUtils.log;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.lcl.model.ExportVoyageSearchModel;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.edi.inttra.HelperClass;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.lcl.ExportVoyageHblBatchForm;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Logiware
 */
public class LclConsolidationMiniManifestPdfCreator extends LclReportFormatMethods {

    private LclBl bl;
    private String ruleName = "";
    protected PdfPTable headerTable;
    protected PdfPTable consigneeTable;
    protected PdfPTable consolidateDrTable;
    protected PdfPTable consolidateTable;
    protected PdfPCell headerCell;
    protected PdfPCell consigneeCell;
    protected PdfPCell consolidateCell;
    private PdfWriter pdfWriter;
    private Document document;
    private StringBuilder voyageNumber = new StringBuilder();
    private StringBuilder vesselDetails = new StringBuilder();
    private String blNumber = "";
    private String fileId;
    HelperClass helperClass = new HelperClass();
    int ADDRESS_SIZE = 0;
    private boolean documentOpenFlag= false;
    private boolean isNew = false;

    class LclConsolidationMiniManifestPageEvent extends PdfPageEventHelper {

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                document.add(headerTable(pdfWriter));
                Rectangle rect = new Rectangle(975, 582, 30, 25);
                rect.setBorder(Rectangle.BOX);
                rect.setBorderWidth(0.6f);
                document.add(rect);
               
            } catch (Exception ex) {
                log.info("Exception on class LclConsolidationMiniManifestPdfCreator in method onStartPage" + new Date(), ex);
            }
        }
    }

    public void createReport(String realPath, String reportLocation, String documentName, String fileId)
            throws FileNotFoundException, DocumentException, Exception {
        LCLBlDAO lCLBlDAO = new LCLBlDAO();
        document = new Document(PageSize.LEGAL.rotate());
        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(reportLocation));
        bl = lCLBlDAO.getByProperty("lclFileNumber.id", Long.parseLong(fileId));
        lCLBlDAO.getCurrentSession().evict(bl);
        ruleName = bl.getLclFileNumber().getBusinessUnit();
        Long fileid=Long.parseLong(fileId);
        setValues(bl,fileid);
        LclConsolidationMiniManifestPdfCreator.LclConsolidationMiniManifestPageEvent event = new LclConsolidationMiniManifestPdfCreator.LclConsolidationMiniManifestPageEvent();
        pdfWriter.setPageEvent(event);
        document.open();
        document.add(consigneeTable());
        document.add(consolidateTable(fileId));
        document.close();
    }
    
    private Element consolidateTable(String fileId) throws DocumentException, Exception {
        consolidateTable = new PdfPTable(8);
        consolidateTable.setWidthPercentage(99f);
        consolidateTable.setWidths(new float[]{1f, 2f, 1f, 1f, 1f, 1f, 2f, 2f});
        int pieceTotal = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        Double cftTotal = 0.0;
        Double kgsToatl = 0.0;
        Paragraph p = null;
        Font blackStarSize = new Font(Font.FontFamily.COURIER, 9f, Font.BOLD);
        if (CommonUtils.isNotEmpty(fileId)) {
            LclConsolidateDAO consolidateDAO = new LclConsolidateDAO();
            LCLBookingDAO lCLBookingDAO = new LCLBookingDAO();
            long fileIDd = Long.parseLong(fileId);
            List consolidateFileList = consolidateDAO.getConsolidatesFiles(fileIDd);
            List newList = new ArrayList();
            if (null != consolidateFileList && CommonUtils.isNotEmpty(consolidateFileList)) {
                newList.add(Long.parseLong(fileId));
                newList.addAll(consolidateFileList);
                List<ConsolidationMiniManifestBean> minimanifestBean = lCLBookingDAO.getConsolidateDr(newList);
                for (ConsolidationMiniManifestBean value : minimanifestBean) {
                    consolidateTable.addCell(makeCellNoBorderFont("", 1f, 8, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("", 1f, 8, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("", 1f, 8, blackNormalCourierFont10f));

                    consolidateTable.addCell(makeCellNoBorderFont("D/R", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("Supplier Name", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("PCS", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("Type", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("CFT ", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("Weight", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("DESCRIPTION ", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont("PURCHASE ORDER/INVOICE", 1f, 0, blackNormalCourierFont10f));

                    consigneeCell = new PdfPCell();
                    consigneeCell.setBorder(0);
                    consigneeCell.setPadding(0f);
                    consigneeCell.setPaddingLeft(-3f);
                    consigneeCell.setPaddingRight(-7f);
                    consigneeCell.setPaddingBottom(4f);
                    consigneeCell.setColspan(19);
                    
                    p = new Paragraph(8f, " ------------- ---------------------------- --------- ---------"
                            + "----------- ------------- ------------- -------------"
                            + "--------------------- -----------------------------", blackStarSize);
                    consigneeCell.addElement(p);
                    consolidateTable.addCell(consigneeCell);
                    
                    
                    String customerpoValue = value.getCustomerPo();
                    List l = null;
                    if (null != customerpoValue && CommonUtils.isNotEmpty(customerpoValue)) {
                        l = helperClass.wrapAddress(customerpoValue);
                        if (l.isEmpty() || l.size() < 3) {
                            ADDRESS_SIZE += 3;
                        } else {
                            ADDRESS_SIZE += l.size();
                        }
                    }
                   
                    consolidateTable.addCell(makeCellNoBorderFont(value.getFileNumber(), 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont(value.getSupplierName(), 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont(String.valueOf(value.getPiece()), 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont(value.getPackageName(), 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont(null != value.getCft() ? String.valueOf(df.format(value.getCft())) : "", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont(null != value.getCft() ? String.valueOf(df.format(value.getKgs())) : "", 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont(value.getComDescrption().toUpperCase(), 1f, 0, blackNormalCourierFont10f));
                    consolidateTable.addCell(makeCellNoBorderFont(null != customerpoValue ? customerpoValue : "" , 1f, 0, blackNormalCourierFont10f));

                    consigneeCell = new PdfPCell();
                    consigneeCell.setBorder(0);
                    consigneeCell.setPadding(0f);
                    consigneeCell.setPaddingLeft(-3f);
                    consigneeCell.setPaddingRight(-7f);
                    consigneeCell.setPaddingBottom(4f);
                    consigneeCell.setColspan(19);
                    
                    p = new Paragraph(8f, " ***********************************************************"
                            + "**************************************************"
                            + "***********************************************************", blackStarSize);
                    consigneeCell.addElement(p);
                    consolidateTable.addCell(consigneeCell);

                    pieceTotal = pieceTotal + value.getPiece();
                    if (null != value.getCft()) {
                        cftTotal = (cftTotal + value.getCft());
                        df.format(cftTotal.doubleValue());
                    }
                    if (null != value.getKgs()) {
                        kgsToatl = kgsToatl + value.getKgs();
                        df.format(cftTotal.doubleValue());
                    }

                }

            }
            consolidateTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
            consolidateTable.addCell(makeCellNoBorderFont("Grand Totals", 2f, 0, blackNormalCourierFont10f));
            consolidateTable.addCell(makeCellNoBorderFont(String.valueOf(pieceTotal), 2f, 0, blackNormalCourierFont10f));
            consolidateTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
            consolidateTable.addCell(makeCellNoBorderFont(String.valueOf(df.format(cftTotal)), 2f, 0, blackNormalCourierFont10f));
            consolidateTable.addCell(makeCellNoBorderFont(String.valueOf(df.format(kgsToatl)), 2f, 0, blackNormalCourierFont10f));
            consolidateTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
            consolidateTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
            
        }
        return consolidateTable;
    }

    private Element consigneeTable() throws DocumentException {
        String consigneeName = "";
        if (CommonFunctions.isNotNull(bl.getConsContact().getId())
                && CommonFunctions.isNotNull(bl.getConsContact().getCompanyName())) {
            consigneeName = bl.getConsContact().getCompanyName();
        }

        consigneeTable = new PdfPTable(3);
        consigneeTable.setWidthPercentage(100);
        consigneeTable.setWidths(new float[]{40, 40, 30});

        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));

        consigneeTable.addCell(makeCellNoBorderFont("Consignee - " + consigneeName, 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("Voyage  # - " + voyageNumber, 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("Vessel  # - " + vesselDetails, 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        consigneeTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));

        return consigneeTable;
    }

    private PdfPTable headerTable(PdfWriter pdfWriter) throws Exception {
        String companyName = LoadLogisoftProperties.getProperty(this.ruleName.equalsIgnoreCase("ECU") ? "application.ECU.companyname"
                : this.ruleName.equalsIgnoreCase("OTI") ? "application.OTI.companyname" : "application.Econo.companyname");
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a ");
        Paragraph ph = null;
        headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{40, 35, 25});

        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont( "       "+companyName, 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont(" DATE " + sd.format(new Date()), 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("Page # -       " + pdfWriter.getCurrentPageNumber(), 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("Consolidation Mini Manifest", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("TIME " +sdf.format(new Date()), 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("Requested By - " + bl.getEnteredBy().getLoginName(), 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont(null!= blNumber ? "B/L " + blNumber : "B/L " + "", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        
        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
         headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));
        headerTable.addCell(makeCellNoBorderFont("", 2f, 0, blackNormalCourierFont10f));

        return headerTable;
    }

    private void setValues(LclBl bl,Long fileId) throws Exception {
        this.bl = bl;
        LclUnitSsDAO unitSsDAO = new LclUnitSsDAO();
        LCLBookingDAO bookingDAO = new LCLBookingDAO();
        ExportVoyageSearchModel pickedDetails = unitSsDAO.getPickedVoyageByVessel(bl.getFileNumberId(), "E");
        LclBooking lclBooking = bookingDAO.getByProperty("lclFileNumber.id", fileId);
        bookingDAO.getCurrentSession().evict(lclBooking);
        LclSsHeader bookedHeader = lclBooking.getBookedSsHeaderId();
        LclUnitSs lclUnitSs = null;
        if (pickedDetails != null && CommonUtils.isNotEmpty(pickedDetails.getUnitSsId())) {
            lclUnitSs = unitSsDAO.findById(Long.parseLong(pickedDetails.getUnitSsId()));
        }
        if (lclUnitSs != null) {
            LclSsDetail lclSsDetail = lclUnitSs.getLclSsHeader().getVesselSsDetail();
            LclSsHeader lclSsHeader = lclUnitSs.getLclSsHeader();
            if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceName())) {
                if(CommonUtils.isNotEmpty(vesselDetails)){
                   vesselDetails.setLength(0);
                }
                vesselDetails.append(lclSsDetail.getSpReferenceName());
            }
//            if (CommonFunctions.isNotNull(lclSsDetail.getTransMode())) {
//                vesselDetails.append(lclSsDetail.getTransMode()).append(".");
//            }
//            if (CommonFunctions.isNotNull(lclSsDetail.getSpReferenceNo())) {
//                vesselDetails.append(lclSsDetail.getSpReferenceNo());
//            }
            
            
            if (CommonFunctions.isNotNull(lclSsHeader.getScheduleNo())) {
                if(CommonUtils.isNotEmpty(voyageNumber)){
                    voyageNumber.setLength(0);
                }
                voyageNumber.append(lclSsHeader.getScheduleNo());
            }
        } else {
            if(null != bookedHeader && null != bookedHeader.getVesselSsDetail()){
            LclSsDetail bookedSsDetail = bookedHeader.getVesselSsDetail();
                if (CommonUtils.isNotEmpty(voyageNumber)) {
                    voyageNumber.setLength(0);
                }
                if (CommonUtils.isNotEmpty(vesselDetails)) {
                    vesselDetails.setLength(0);
                }
            vesselDetails.append(bookedSsDetail.getSpReferenceName());
            voyageNumber.append(bookedHeader.getScheduleNo());
            }
        }
            blNumber = new LCLBlDAO().getExportBlNumbering(String.valueOf(bl.getFileNumberId()));
        }
    public String createEmailReport(String realPath, String outputFileName, ExportVoyageHblBatchForm batchForm) throws Exception {
        List<Long> actualDrList = new ArrayList<Long>();
        boolean isConsolidate = false;
        if (CommonUtils.isNotEmpty(batchForm.getFileNumberId())) {
            actualDrList.add(Long.parseLong(batchForm.getFileNumberId()));
        } else {
            actualDrList = new ExportUnitQueryUtils().getAllPickedCargoBkg(Long.parseLong(batchForm.getHeaderId()),
                    Long.parseLong(batchForm.getUnitSSId()));
        }
        if (CommonUtils.isNotEmpty(actualDrList)) {
            for (Long fileId : actualDrList) {
                isConsolidate = new LclConsolidateDAO().isConsoildateFile(String.valueOf(fileId));
                if (isConsolidate) {
                    LCLBlDAO lCLBlDAO = new LCLBlDAO();
                    bl = lCLBlDAO.getByProperty("lclFileNumber.id", fileId);
                    lCLBlDAO.getCurrentSession().evict(bl);
                    ruleName = bl.getLclFileNumber().getBusinessUnit();
                    setValues(bl, fileId);
                    if (!isNew) {
                        document = new Document(PageSize.LEGAL.rotate());
                        pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outputFileName));
                        LclConsolidationMiniManifestPdfCreator.LclConsolidationMiniManifestPageEvent event = new LclConsolidationMiniManifestPdfCreator.LclConsolidationMiniManifestPageEvent();
                        pdfWriter.setPageEvent(event);
                        document.open();
                    }
                    if (!documentOpenFlag) {
                        documentOpenFlag = true;
                        isNew = true;
                        document.add(consigneeTable());
                        document.add(consolidateTable(String.valueOf(fileId)));
                    } else {
                        document.newPage();
                        document.add(consigneeTable());
                        document.add(consolidateTable(String.valueOf(fileId)));
                    }
                }
            }
        }
        if (documentOpenFlag) {
            document.close();
        } else {
            outputFileName = null;
        }
        return outputFileName;

    }

}
