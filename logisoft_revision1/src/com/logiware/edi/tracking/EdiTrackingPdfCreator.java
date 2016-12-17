/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.edi.tracking;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.reports.ReportFormatMethods;
import com.logiware.edi.entity.BlClause;
import com.logiware.edi.entity.ChargeCategory;
import com.logiware.edi.entity.CustomsClearanceInfo;
import com.logiware.edi.entity.EquipmentDetail;
import com.logiware.edi.entity.HazardousGoods;
import com.logiware.edi.entity.PackageDetails;
import com.logiware.edi.entity.Party;
import com.logiware.edi.entity.Shipment;
import com.logiware.edi.entity.SplitgoodsDetails;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author vellaisamy
 */
public class EdiTrackingPdfCreator extends ReportFormatMethods {

    Document document = null;

    private void initialize(Shipment shipment, String fileName) throws FileNotFoundException, DocumentException {
        document = new Document(PageSize.A4);
        document.setMargins(10, 10, 10, 10);
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
    }

    private void createBody(Shipment shipment, String ediCompany) throws DocumentException, IOException, Exception {
        EdiReportServlet ediReportServlet = new EdiReportServlet();
        final String UNITED_STATES = "UNITED STATES";
        Party carrier = null;
        Party consignee = null;
        Party consolidator = null;
        Party shipper = null;
        Party exporter = null;
        Party contractParty = null;
        Party freightPayer = null;
        Party forwarder = null;
        Party ultimateConsignee = null;
        Party requestor = null;
        Party importer = null;
        Party mainNotifyParty = null;
        Party firstAdditionalNotifyParty = null;
        Party secondAdditionalNotifyParty = null;
        Party supplier = null;
        Party shipToDoor = null;
        String city = null;
        int index = 0;

//        PdfPTable table = new PdfPTable(4);
//        table.setWidthPercentage(100);

        for (Party party : shipment.getPartySet()) {
            if ("Carrier".equalsIgnoreCase(party.getRole())) {
                carrier = party;
            } else if ("Consignee".equalsIgnoreCase(party.getRole())) {
                consignee = party;
            } else if ("Consolidator".equalsIgnoreCase(party.getRole())) {
                consolidator = party;
            } else if ("Shipper".equalsIgnoreCase(party.getRole())) {
                shipper = party;
            } else if ("Exporter".equalsIgnoreCase(party.getRole())) {
                exporter = party;
            } else if ("ContractParty".equalsIgnoreCase(party.getRole())) {
                contractParty = party;
            } else if ("FreightPayer".equalsIgnoreCase(party.getRole())) {
                freightPayer = party;
            } else if ("Forwarder".equalsIgnoreCase(party.getRole())) {
                forwarder = party;
            } else if ("UltimateConsignee".equalsIgnoreCase(party.getRole())) {
                ultimateConsignee = party;
            } else if ("Requestor".equalsIgnoreCase(party.getRole())) {
                requestor = party;
            } else if ("Importer".equalsIgnoreCase(party.getRole())) {
                importer = party;
            } else if ("MainNotifyParty".equalsIgnoreCase(party.getRole())) {
                mainNotifyParty = party;
            } else if ("FirstAdditionalNotifyParty".equalsIgnoreCase(party.getRole())) {
                firstAdditionalNotifyParty = party;
            } else if ("SecondAdditionalNotifyParty".equalsIgnoreCase(party.getRole())) {
                secondAdditionalNotifyParty = party;
            } else if ("Supplier".equalsIgnoreCase(party.getRole())) {
                supplier = party;
            } else if ("ShipToDoor".equalsIgnoreCase(party.getRole())) {
                shipToDoor = party;
            }
        }
//        ReportFormatMethods reportFormatMethods = new ReportFormatMethods();
//        PdfPTable table = reportFormatMethods.makeTable(4);
//        table.setWidths(new float[]{76,25});

        PdfPTable headTable = new PdfPTable(1);
        headTable.setWidthPercentage(100);
        Phrase phraseHead;
        if("K".equals(ediCompany)) {
        phraseHead = new Phrase("KUEHNENAGEL SHIPPING INSTRUCTION", blackBoldFont8);
        } else {
        phraseHead = new Phrase("INTTRA SHIPPING INSTRUCTION", blackBoldFont8);
        }
        PdfPCell cellHead = makeCell(phraseHead, Element.ALIGN_CENTER);
        headTable.addCell(cellHead);

        document.add(headTable);
// Adding Empty Table
        PdfPTable emptyTable = new PdfPTable(1);
        emptyTable.setWidthPercentage(100);
        PdfPCell emptyCell = makeCellLeftNoBorder("\n\n");
        emptyTable.addCell(emptyCell);
        document.add(emptyTable);

        PdfPTable mainTable = new PdfPTable(4);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new float[]{25, 25, 25, 25});
        StringBuffer info = new StringBuffer();
        //shipper
        if (null != shipper) {
            info.append(shipper.getName() + "\n");
            if (CommonUtils.isNotEmpty(shipper.getAddress())) {
                info.append(shipper.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("SHIPPER", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("SHIPPER", ""));
        }
        info.delete(0, info.length());
        //forwarding agent
        if (null != forwarder) {
            info.append(forwarder.getName() + "\n");
            if (CommonUtils.isNotEmpty(forwarder.getAddress())) {
                info.append(forwarder.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("FORWARDING AGENT", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("FORWARDING AGENT", ""));
        }

        PdfPTable subTable1 = new PdfPTable(1);
        subTable1.setWidthPercentage(101);
        info.delete(0, info.length());
        if(null != carrier){
            subTable1.addCell(makeCellWithHeaderWithoutBorder("CARRIER", carrier.getName()));
        }else{
            subTable1.addCell(makeCellWithHeaderWithoutBorder("CARRIER", ""));
        }
        subTable1.addCell(makeCellWithHeaderWithBottomBorder("CARRIER BOOKING NO.", shipment.getBookingNo() + "\n\n"));
        info.delete(0, info.length());
        if (CommonUtils.isNotEmpty(shipment.getPurchaseOrderNo())) {
            info.append("Purchase Order: " + shipment.getPurchaseOrderNo() + "\n");
        }
        if (CommonUtils.isNotEmpty(shipment.getShipperRefNo())) {
            info.append("Shipper Reference: " + shipment.getShipperRefNo() + "\n");
        }
        if (CommonUtils.isNotEmpty(shipment.getFrtfwdRefNo())) {
            info.append("Forwarder Reference: " + shipment.getFrtfwdRefNo() + "\n");
        }
        if (CommonUtils.isNotEmpty(shipment.getBlNo())) {
            info.append("Bill Of Lading Reference: " + shipment.getBlNo() + "\n");
        }
        if (CommonUtils.isNotEmpty(shipment.getConsigOrderNo())) {
            info.append("Consignee Order Reference: " + shipment.getConsigOrderNo() + "\n");
        }
        if (CommonUtils.isNotEmpty(shipment.getExportRefNo())) {
            info.append("Export Reference: " + shipment.getExportRefNo() + "\n");
        }
        if (CommonUtils.isNotEmpty(shipment.getInternalTransactionNo())) {
            info.append("InternalTransaction: " + shipment.getInternalTransactionNo() + "\n");
        }
        subTable1.addCell(makeCellWithHeaderWithoutBorder("EXPORT REFERENCES", info.toString()));
        mainTable.addCell(subTable1);
        PdfPTable subTable2 = new PdfPTable(1);
        subTable2.addCell(makeCellWithHeaderWithBottomBorder("SHIPPING INSTRUCTION NO. / REVISION NO.\n", shipment.getShipmentId()));
        subTable2.addCell(makeCellWithHeaderWithoutBorder("CUSTOMER SUBMISSION DATE\n", null !=shipment.getDate() ? shipment.getDate().toString() : ""));
        mainTable.addCell(subTable2);

        info.delete(0, info.length());
        //consignee info
        if (null != consignee) {
            info.append(consignee.getName() + "\n");
            if (CommonUtils.isNotEmpty(consignee.getAddress())) {
                info.append(consignee.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("CONSIGNEE", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("CONSIGNEE", ""));
        }

        info.delete(0, info.length());
        //mainNotifyParty party info
        if (null != mainNotifyParty) {
            info.append(mainNotifyParty.getName() + "\n");
            if (CommonUtils.isNotEmpty(mainNotifyParty.getAddress())) {
                info.append(mainNotifyParty.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("NOTIFY PARTY", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("NOTIFY PARTY", ""));
        }

        info.delete(0, info.length());
        //firstAdditionalNotifyParty party info
        if (null != firstAdditionalNotifyParty) {
            info.append(firstAdditionalNotifyParty.getName() + "\n");
            if (CommonUtils.isNotEmpty(firstAdditionalNotifyParty.getAddress())) {
                info.append(firstAdditionalNotifyParty.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("1st ADDITIONAL NOTIFY PARTY", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("1st ADDITIONAL NOTIFY PARTY", ""));
        }

        info.delete(0, info.length());
        //firstAdditionalNotifyParty party info
        if (null != secondAdditionalNotifyParty) {
            info.append(secondAdditionalNotifyParty.getName() + "\n");
            if (CommonUtils.isNotEmpty(secondAdditionalNotifyParty.getAddress())) {
                info.append(secondAdditionalNotifyParty.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("2nd ADDITIONAL NOTIFY PARTY", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("2nd ADDITIONAL NOTIFY PARTY", ""));
        }

        info.delete(0, info.length());
        //contractParty party info
        if (null != contractParty) {
            info.append(contractParty.getName() + "\n");
            if (CommonUtils.isNotEmpty(contractParty.getAddress())) {
                info.append(contractParty.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("CONTRACT PARTY", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("CONTRACT PARTY", ""));
        }
        info.delete(0, info.length());
        //freightPayer party info
        if (null != freightPayer) {
            info.append(freightPayer.getName() + "\n");
            if (CommonUtils.isNotEmpty(freightPayer.getAddress())) {
                info.append(freightPayer.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("FREIGHT PAYER", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("FREIGHT PAYER", ""));
        }
        info.delete(0, info.length());
        //exporter party info
        if (null != exporter) {
            info.append(exporter.getName() + "\n");
            if (CommonUtils.isNotEmpty(exporter.getAddress())) {
                info.append(exporter.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("EXPORTER/SELLER", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("EXPORTER/SELLER", ""));
        }

        mainTable.addCell(makeCellWithHeader("GOODS OWNER/BUYER", ""));
        info.delete(0, info.length());
        //exporter party info
        if (null != supplier) {
            info.append(supplier.getName() + "\n");
            if (CommonUtils.isNotEmpty(supplier.getAddress())) {
                info.append(supplier.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("MANUFACTURER/SUPPLIER", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("MANUFACTURER/SUPPLIER", ""));
        }

        info.delete(0, info.length());
        //exporter party info
        if (null != shipToDoor) {
            info.append(shipToDoor.getName() + "\n");
            if (CommonUtils.isNotEmpty(shipToDoor.getAddress())) {
                info.append(shipToDoor.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("SHIP TO", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("SHIP TO", ""));
        }

        info.delete(0, info.length());
        //consolidator party info
        if (null != consolidator) {
            info.append(consolidator.getName() + "\n");
            if (CommonUtils.isNotEmpty(consolidator.getAddress())) {
                info.append(consolidator.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("CONSOLIDATOR/STUFFER", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("CONSOLIDATOR/STUFFER", ""));
        }
        info.delete(0, info.length());
        //importer party info
        if (null != importer) {
            info.append(importer.getName() + "\n");
            if (CommonUtils.isNotEmpty(importer.getAddress())) {
                info.append(importer.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            mainTable.addCell(makeCellWithHeader("", info.toString()));
        } else {
            mainTable.addCell(makeCellWithHeader("IMPORTER", ""));
        }
        info.delete(0, info.length());
        Paragraph p = new Paragraph();
        if (CommonUtils.isNotEmpty(shipment.getBlOrginCity())) {
            p.add(new Chunk("city: ", blackBoldFontItalic));
            p.add(new Chunk(shipment.getBlOrginCity(), blackFontForAR));
        } else {
            p.add(new Chunk("city: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getBlOrginCountry())) {
            p.add(new Chunk("\ncountry: ", blackBoldFontItalic));
            p.add(new Chunk(shipment.getBlOrginCountry(), blackFontForAR));
        } else {
            p.add(new Chunk("\ncountry: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getBlOrigin())) {
            p.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
            p.add(new Chunk(shipment.getBlOrigin(), blackFontForAR));
        } else {
            p.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
        }
        PdfPCell cell = makeCellWithHeaderWithLabelItalic("POINT AND COUNTRY OF ORIGIN OF GOODS", "");
        cell.addElement(p);
        mainTable.addCell(cell);
        mainTable.addCell(makeCellWithHeader("TYPE OF MOVE", shipment.getMovementType()));
        info.delete(0, info.length());
        info.append(null != shipment.getTransportService() ? shipment.getTransportService() : "");
        mainTable.addCell(makeCellWithHeader("SHIPMENT TYPE", info.toString()));
        info.delete(0, info.length());
        if (CommonUtils.isNotEmpty(shipment.getVesselName())) {
            info.append(shipment.getVesselName());
        }
        if (CommonUtils.isNotEmpty(shipment.getVoyageNumber())) {
            info.append(", " + shipment.getVoyageNumber());
        }
        mainTable.addCell(makeCellWithHeader("VESSEL, VOYAGE", info.toString()));
        info.delete(0, info.length());
        Paragraph p1 = new Paragraph();
        if (CommonUtils.isNotEmpty(shipment.getPlaceOfReceiptCity())) {
            p1.add(new Chunk("city: ", blackBoldFontItalic));
            p1.add(new Chunk(shipment.getPlaceOfReceiptCity(), blackFontForAR));
        } else {
            p1.add(new Chunk("city: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPlaceOfReceiptCountry())) {
            p1.add(new Chunk("\ncountry: ", blackBoldFontItalic));
            p.add(new Chunk(shipment.getPlaceOfReceiptCountry(), blackFontForAR));
        } else {
            p1.add(new Chunk("\ncountry: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPlaceOfReceipt())) {
            p1.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
            p1.add(new Chunk(shipment.getPlaceOfReceipt(), blackFontForAR));
        } else {
            p1.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
        }
        cell = makeCellWithHeaderWithLabelItalic("PLACE OF INITIAL RECEIPT", "");
        cell.addElement(p1);
        mainTable.addCell(cell);
        info.delete(0, info.length());
        Paragraph p2 = new Paragraph();
        if (CommonUtils.isNotEmpty(shipment.getPortOLoadCity())) {
            p2.add(new Chunk("city: ", blackBoldFontItalic));
            p2.add(new Chunk(shipment.getPortOLoadCity(), blackFontForAR));
        } else {
            p2.add(new Chunk("city: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPortOLoadCountry())) {
            p2.add(new Chunk("\ncountry: ", blackBoldFontItalic));
            p2.add(new Chunk(shipment.getPortOLoadCountry(), blackFontForAR));
        } else {
            p2.add(new Chunk("\ncountry: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPortOfLoad())) {
            p2.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
            p2.add(new Chunk(shipment.getPortOfLoad(), blackFontForAR));
        } else {
            p2.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
        }
        cell = makeCellWithHeaderWithLabelItalic("PORT OF LOAD", "");
        cell.addElement(p2);
        mainTable.addCell(cell);
        info.delete(0, info.length());
        Paragraph p3 = new Paragraph();
        if (CommonUtils.isNotEmpty(shipment.getPortOfDischargeCity())) {
            p3.add(new Chunk("city: ", blackBoldFontItalic));
            p3.add(new Chunk(shipment.getPortOfDischargeCity(), blackFontForAR));
        } else {
            p3.add(new Chunk("city: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPortOfDischargeCountry())) {
            p3.add(new Chunk("\ncountry: ", blackBoldFontItalic));
            p3.add(new Chunk(shipment.getPortOfDischargeCountry(), blackFontForAR));
        } else {
            p3.add(new Chunk("\ncountry: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPlaceOfDischarge())) {
            p3.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
            p3.add(new Chunk(shipment.getPlaceOfDischarge(), blackFontForAR));
        } else {
            p3.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
        }
        cell = makeCellWithHeaderWithLabelItalic("PORT OF DISCHARGE", "");
        cell.addElement(p3);
        mainTable.addCell(cell);
        info.delete(0, info.length());
        Paragraph p4 = new Paragraph();
        if (CommonUtils.isNotEmpty(shipment.getPlaceOfDeliveryCity())) {
            p4.add(new Chunk("city: ", blackBoldFontItalic));
            p4.add(new Chunk(shipment.getPlaceOfDeliveryCity(), blackFontForAR));
        } else {
            p4.add(new Chunk("city: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPlaceOfDeliveryCountry())) {
            p4.add(new Chunk("\ncountry: ", blackBoldFontItalic));
            p.add(new Chunk(shipment.getPlaceOfDeliveryCountry(), blackFontForAR));
        } else {
            p4.add(new Chunk("\ncountry: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPlaceOfDelivery())) {
            p4.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
            p4.add(new Chunk(shipment.getPlaceOfDelivery(), blackFontForAR));
        } else {
            p4.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
        }
        cell = makeCellWithHeaderWithLabelItalic("PLACE OF DELIVERY", "");
        cell.addElement(p4);
        mainTable.addCell(cell);
        document.add(mainTable);

// Adding Empty Table

        document.add(emptyTable);
        int outer = 1;
        PdfPTable partTable = null;
        PdfPTable containerHeader = new PdfPTable(7);
        containerHeader.setWidthPercentage(100);
        containerHeader.setWidths(new float[]{12, 10, 13, 9, 34, 11, 11});
        PdfPTable containerTable = new PdfPTable(2);
        containerTable.setWidthPercentage(100);
        containerTable.setWidths(new float[]{12, 88});
        Phrase headingPhrase2 = new Phrase("PARTICULARS", blackBoldFont8);
        PdfPCell pCell = new PdfPCell(headingPhrase2);
        pCell.setColspan(2);
        containerTable.addCell((pCell));
        containerHeader.addCell(makeCellWithHeader("CONTAINER", ""));
        containerHeader.addCell(makeCellWithHeader("GID Seq. / Packing Level", ""));
        containerHeader.addCell(makeCellWithHeader("MARKS & NUMBERS", ""));
        containerHeader.addCell(makeCellWithHeader("NO OF PKGS", ""));
        containerHeader.addCell(makeCellWithHeader("DESCRIPTION", ""));
        containerHeader.addCell(makeCellWithHeader("GROSS WEIGHT", ""));
        containerHeader.addCell(makeCellWithHeader("VOLUME", ""));
        PdfPCell headerCell = new PdfPCell(containerHeader);
        headerCell.setColspan(2);
        containerTable.addCell(headerCell);
        int totalEqup = 0;
        String packageCode = "";
        if ("FCL".equalsIgnoreCase(shipment.getTransportService())) {
            for (EquipmentDetail equipmentDetail : shipment.getEquipmentDetailSet()) {
                totalEqup++;
                String seal = null == equipmentDetail.getSeal() ? "" : "\n" + equipmentDetail.getSeal();
                if (null != equipmentDetail.getTypeCode()) {
                    String codedesc = new GenericCodeDAO().getFieldByCodeAndCodetypeId("INTTRA Container Type Code", equipmentDetail.getTypeCode(), "codedesc");
                    if (CommonUtils.isNotEmpty(codedesc)) {
                        packageCode = "\nContainer Type: " + codedesc;
                    } else {
                        packageCode = "\nContainer Type: " + equipmentDetail.getTypeCode();
                    }
                } else {
                    packageCode = "\nContainer Type: " + "";
                }
                String references = null == equipmentDetail.getReferenceInfo() ? "" : "\nContainer References: " + equipmentDetail.getReferenceInfo();
                String airFlow = null == equipmentDetail.getAirFlow() ? "" : "\nAir Flow: " + equipmentDetail.getAirFlow() + " " + equipmentDetail.getAirFlowType();
                String comments = null == equipmentDetail.getComments() ? "" : "\n" + equipmentDetail.getComments();
                String temp = null == equipmentDetail.getTemperature() ? "" : "\nTemperature: " + equipmentDetail.getTemperature() + " " + equipmentDetail.getTemperatureType();
                String containerSupplier = null == equipmentDetail.getContainerSupplier() ? "" : "\nContainer Supplier: " + equipmentDetail.getContainerSupplier();
                String containerNetWeightType = "";
                if ("KGM".equalsIgnoreCase(equipmentDetail.getGrossWeightType())) {
                    containerNetWeightType = "KGS";
                } else {
                    containerNetWeightType = null == equipmentDetail.getGrossWeightType() ? "" : equipmentDetail.getGrossWeightType();
                }
                String containerNetWeight = null == equipmentDetail.getGrossWeight() ? "" : "\nContainer Net Weight: " + equipmentDetail.getGrossWeight() + " " + containerNetWeightType;
                PdfPCell pdfCell = makeCellWithNormalWithLeftAndBottomBorder(equipmentDetail.getIdentifier() + packageCode + references + seal + temp + airFlow + comments + containerNetWeight + containerSupplier);
                containerTable.addCell(pdfCell);
                outer = 1;
                PdfPTable markerTable = new PdfPTable(1);
                markerTable.getDefaultCell().setBorder(0);
                markerTable.setKeepTogether(true);
                markerTable.setWidthPercentage(100.5f);
                boolean isFirst = true;
                List<PackageDetails> packSet = shipment.getPackageByEquipment(equipmentDetail.getIdentifier());
                for (PackageDetails pack : packSet) {
                    if (null == pack.getParentId()) {
                        //equip details
                        partTable = getContainerTable();
                        partTable = getPackageDetails(partTable, info, pack, outer, equipmentDetail, isFirst);
                        cell = makeCellWithNormalWithoutBorder("");
                        partTable.setExtendLastRow(true);
                        cell.addElement(partTable);
                        cell.setColspan(0);
                        cell.setPaddingBottom(0);
                        cell.setPaddingTop(0);
                        cell.setBorderWidthBottom(0);
                        markerTable.addCell(cell);
                        isFirst = false;
                        outer++;
                        int innerCount = 1;
                        for (PackageDetails inner : shipment.getChildPackage(pack.getId())) {
                            partTable = getContainerTable();
                            partTable = getPackageDetails(partTable, info, inner, innerCount, null, false);
                            cell = makeCellWithNormalWithoutBorder("");
                            partTable.setExtendLastRow(true);
                            cell.addElement(partTable);
                            cell.setColspan(0);
                            cell.setPaddingBottom(0);
                            cell.setPaddingTop(0);
                            cell.setBorderWidthBottom(0);
                            markerTable.addCell(cell);
                            innerCount++;
                            int innerInnerCount = 1;
                            for (PackageDetails innerInner : shipment.getChildPackage(inner.getId())) {
                                partTable = getContainerTable();
                                partTable = getPackageDetails(partTable, info, innerInner, innerInnerCount, null, false);
                                cell = makeCellWithNormalWithoutBorder("");
                                partTable.setExtendLastRow(true);
                                cell.addElement(partTable);
                                cell.setColspan(0);
                                cell.setPaddingBottom(0);
                                cell.setPaddingTop(0);
                                cell.setBorderWidthBottom(0);
                                markerTable.addCell(cell);
                                innerInnerCount++;
                            }
                        }
                    }
                }
                cell = makeCellWithNormalWithoutBorder("");
                cell.setBorderWidthBottom(0);
                cell.setBorderWidthRight(0.6f);
                markerTable.setExtendLastRow(true);
                cell.addElement(markerTable);
                cell.setPaddingBottom(0);
                cell.setPaddingTop(0);
                containerTable.addCell(cell);
            }
            document.add(containerTable);
        } else if ("LCL".equalsIgnoreCase(shipment.getTransportService())) {
            for (PackageDetails pack : shipment.getPackageDetailsSet()) {
                PdfPTable markerTable = new PdfPTable(1);
                markerTable.getDefaultCell().setBorder(0);
                markerTable.setWidthPercentage(101);
                markerTable.setKeepTogether(true);
                containerTable.addCell(makeCellWithNormalWithLeftAndBottomBorder(""));
                if (null == pack.getParentId()) {
                    //equip details
                    partTable = getContainerTable();
                    partTable = getPackageDetails(partTable, info, pack, outer, null, false);
                    cell = makeCellWithNormalWithoutBorder("");
                    partTable.setExtendLastRow(true);
                    cell.addElement(partTable);
                    cell.setColspan(0);
                    cell.setPaddingBottom(0);
                    cell.setPaddingTop(0);
                    cell.setBorderWidthBottom(0);
                    markerTable.addCell(cell);
                    outer++;
                    int innerCount = 1;
                    for (PackageDetails inner : shipment.getChildPackage(pack.getId())) {
                        partTable = getContainerTable();
                        partTable = getPackageDetails(partTable, info, inner, innerCount, null, false);
                        cell = makeCellWithNormalWithoutBorder("");
                        partTable.setExtendLastRow(true);
                        cell.addElement(partTable);
                        cell.setColspan(0);
                        cell.setPaddingBottom(0);
                        cell.setPaddingTop(0);
                        cell.setBorderWidthBottom(0);
                        markerTable.addCell(cell);
                        innerCount++;
                        int innerInnerCount = 1;
                        for (PackageDetails innerInner : shipment.getChildPackage(inner.getId())) {
                            partTable = getContainerTable();
                            partTable = getPackageDetails(partTable, info, innerInner, innerInnerCount, null, false);
                            cell = makeCellWithNormalWithoutBorder("");
                            partTable.setExtendLastRow(true);
                            cell.addElement(partTable);
                            cell.setColspan(0);
                            cell.setPaddingBottom(0);
                            cell.setPaddingTop(0);
                            cell.setBorderWidthBottom(0);
                            markerTable.addCell(cell);
                            innerInnerCount++;
                        }
                    }
                }
                cell = makeCellWithNormalWithoutBorder("");
                cell.setBorderWidthBottom(0);
                cell.setBorderWidthRight(0.6f);
                markerTable.setExtendLastRow(true);
                cell.addElement(markerTable);
                cell.setColspan(0);
                cell.setPaddingBottom(0);
                cell.setPaddingTop(0);
                containerTable.addCell(cell);
            }
            document.add(containerTable);
        }
        document.add(emptyTable);
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(100);
        totalTable.setWidths(new float[]{40, 60});

        totalTable.addCell(makeCellWithBoldWithoutRightAndBottomBorder("CONTROL TOTALS:"));
        totalTable.addCell(makeCellWithNormalWithoutLeftAndBottomBorder("As provided"));
        totalTable.addCell(makeCellWithBoldWithLeftBorder("TOTAL CONTAINER COUNT:"));
        String numberOEquip = null == shipment.getNumberOfEquipment() ? "" + totalEqup : shipment.getNumberOfEquipment().toString();
        totalTable.addCell(makeCellWithNormalWithRightBorder(numberOEquip));
        totalTable.addCell(makeCellWithBoldWithLeftBorder("TOTAL PACKAGE COUNT:"));
        String numberOfPack = null == shipment.getNumberOfPackage() ? "0" : shipment.getNumberOfPackage().toString();
        totalTable.addCell(makeCellWithNormalWithRightBorder(numberOfPack));
        totalTable.addCell(makeCellWithBoldWithLeftBorder("TOTAL WEIGHT:"));
        String grossWeight = null == shipment.getGrossWeight() ? "0.000" : shipment.getGrossWeight().toString();
        info.delete(0, info.length());
        String grossWeightType = "";
        if ("KGM".equalsIgnoreCase(shipment.getGrossWeightType())) {
            grossWeightType = "KGS";
        } else {
            grossWeightType = null == shipment.getGrossWeightType() ? "" : shipment.getGrossWeightType();
        }
        info.append(grossWeight + " " + grossWeightType);
        totalTable.addCell(makeCellWithNormalWithRightBorder(info.toString()));
        totalTable.addCell(makeCellWithBoldWithLeftAndBottomBorder("TOTAL VOLUME:"));
        String grossVolume = null == shipment.getGrossVolume() ? "0.000" : shipment.getGrossVolume().toString();
        info.delete(0, info.length());
        String grossVolumeType = "";
        if ("MTQ".equalsIgnoreCase(shipment.getGrossVolumeType())) {
            grossVolumeType = "CBM";
        } else if ("FTQ".equalsIgnoreCase(shipment.getGrossVolumeType())) {
            grossVolumeType = "CFT";
        } else {
            grossVolumeType = null == shipment.getGrossVolumeType() ? "" : shipment.getGrossVolumeType();
        }
        info.append(grossVolume + " " + grossVolumeType);
        totalTable.addCell(makeCellWithNormalWithRightAndBottomBorder(info.toString()));

        document.add(totalTable);

// Adding Empty Table

        document.add(emptyTable);

        /*( PdfPTable houseBillTable = new PdfPTable(5);
        houseBillTable.setWidthPercentage(100);
        houseBillTable.setWidths(new float[]{20, 20, 20, 20, 20});

        Phrase houseBillPhrase = new Phrase("HOUSE BILL DETAILS",blackFontForAR);
        PdfPCell houseBillCell = new PdfPCell(houseBillPhrase);
        houseBillCell.setColspan(5);
        houseBillTable.addCell(houseBillCell);

        houseBillTable.addCell(makeCellWithNormal("REFERENCES"));
        houseBillTable.addCell(makeCellWithNormal("MANIFEST FILING"));
        houseBillTable.addCell(makeCellWithNormal("ORIGINAL SHIPPER"));
        houseBillTable.addCell(makeCellWithNormal("ULTIMATE CONSIGNEE"));
        houseBillTable.addCell(makeCellWithNormal("CARGO ID"));

        document.add(houseBillTable);

        // Adding Empty Table

        document.add(emptyTable);*/

        PdfPTable shippersTable = new PdfPTable(4);
        shippersTable.setWidthPercentage(100);
        shippersTable.setWidths(new float[]{25, 25, 25, 25});
        String declaredValue = null == shipment.getShipmentDeclaredValue() ? "" : shipment.getShipmentDeclaredValue();
        shippersTable.addCell(makeCellWithHeader("SHIPPERS DECLARED VALUE:\n", declaredValue));
        info.delete(0, info.length());
        Paragraph para = new Paragraph();
        if (CommonUtils.isNotEmpty(shipment.getReleaseOfficeCity())) {
            para.add(new Chunk("city: ", blackBoldFontItalic));
            para.add(new Chunk(shipment.getReleaseOfficeCity(), blackFontForAR));

        } else {
            para.add(new Chunk("city: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getReleaseOfficeCountry())) {
            para.add(new Chunk("\ncountry: ", blackBoldFontItalic));
            para.add(new Chunk(shipment.getReleaseOfficeCountry(), blackFontForAR));
        } else {
            para.add(new Chunk("\ncountry: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getReleaseOfficeValue())) {
            para.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
            para.add(new Chunk(shipment.getReleaseOfficeValue(), blackFontForAR));
        } else {
            para.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
        }
        cell = makeCellWithHeaderWithLabelItalic("PLACE OF ISSUE", "");
        cell.addElement(para);
        shippersTable.addCell(cell);
        String releaseDate = null == shipment.getReleaseDate() ? "" : shipment.getReleaseDate().toString();
        shippersTable.addCell(makeCellWithHeader("DATE OF ISSUE: \n", releaseDate));
        info.delete(0, info.length());
        Paragraph paragraph = new Paragraph();
        if (CommonUtils.isNotEmpty(shipment.getPayCity())) {
            paragraph.add(new Chunk("city: ", blackBoldFontItalic));
            paragraph.add(new Chunk(shipment.getPayCity(), blackFontForAR));
        } else {
            paragraph.add(new Chunk("city: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPayCountry())) {
            paragraph.add(new Chunk("\ncountry: ", blackBoldFontItalic));
            paragraph.add(new Chunk(shipment.getPayCountry(), blackFontForAR));
        } else {
            paragraph.add(new Chunk("\ncountry: ", blackBoldFontItalic));
        }
        if (CommonUtils.isNotEmpty(shipment.getPayIdentifierValue())) {
            paragraph.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
            paragraph.add(new Chunk(shipment.getPayIdentifierValue(), blackFontForAR));
        } else {
            paragraph.add(new Chunk("\nlocation id: ", blackBoldFontItalic));
        }
        cell = makeCellWithHeaderWithLabelItalic("FREIGHT PAYABLE AT", "");
        cell.addElement(paragraph);
        shippersTable.addCell(cell);
        document.add(shippersTable);

        // Adding Empty Table
        document.add(emptyTable);

        PdfPTable manifestTable = new PdfPTable(3);
        manifestTable.setWidthPercentage(100);
        manifestTable.setWidths(new float[]{40, 35, 35});

        Phrase manifestPhrase = new Phrase("MANIFEST FILING INFORMATION", blackFontForAR);
        PdfPCell manifestCell = new PdfPCell(manifestPhrase);
        manifestCell.setColspan(3);
        manifestTable.addCell(manifestCell);
        boolean isCustomExists = false;
        for (CustomsClearanceInfo custom : shipment.getCustomsClearanceInfoSet()) {
            manifestTable.addCell(makeCellWithoutRightBorder("FILING STATUS: " + custom.getFiller()));
            manifestTable.addCell(makeCellWithoutRightAndLeftBorder("FILING COUNTRY: " + custom.getFillingCountry()));
            manifestTable.addCell(makeCellWithoutLeftBorder("FILER ID: " + custom.getFillerId()));
            isCustomExists = true;
        }
        if (isCustomExists) {
            document.add(manifestTable);
            // Adding Empty Table
            document.add(emptyTable);
        }
        PdfPTable chargesTable = new PdfPTable(1);
        chargesTable.setWidthPercentage(100);
        chargesTable.setWidths(new float[]{100});
        info.delete(0, info.length());
        for (ChargeCategory charge : shipment.getChargeCategorySet()) {
            info.append(charge.getChargeType() + "\t\t\t\t\t" + charge.getPrepaidCollector() + "\n");
        }
        chargesTable.addCell(makeCellWithHeader("CHARGES AS PROVIDED:", info.toString()));

        document.add(chargesTable);

// Adding Empty Table (CLAUSES)
        document.add(emptyTable);
        PdfPTable clausesTable = new PdfPTable(2);
        clausesTable.setWidthPercentage(100);
        clausesTable.setWidths(new float[]{40, 60});
        clausesTable.addCell(makeCellWithBoldWithoutRightAndBottomBorder("CLAUSES:"));
        info.delete(0, info.length());
        for (BlClause blClause : shipment.getBlClauseList()) {
            info.append("\n" + blClause.getText());
        }
        if (info.length() == 0) {
            info.append("");
        }
        clausesTable.addCell(makeCellWithNormalWithoutLeftAndBottomBorder(info.toString()));
        clausesTable.addCell(makeCellWithBoldWithLeftBorder("COMMENT:"));

        String generalInfoComment = null == shipment.getGeneralInfo() ? "" : shipment.getGeneralInfo().toString();
        clausesTable.addCell(makeCellWithNormalWithRightBorder(generalInfoComment));

        clausesTable.addCell(makeCellWithBoldWithLeftBorder("MEASURE FOR B/L PRODUCTION:"));
        clausesTable.addCell(makeCellWithNormalWithRightBorder(""));
        clausesTable.addCell(makeCellWithNormalWithLeftBorder("Documentation parameters as requested by submitting party"));
        clausesTable.addCell(makeCellWithNormalWithRightBorder(""));
        clausesTable.addCell(makeCellWithBoldWithLeftAndBottomBorder("LETTER OF CREDIT:"));
        info.delete(0, info.length());
        String letterOfCredit = null == shipment.getLetterOfCredit() ? "" : shipment.getLetterOfCredit().toString();
        if (CommonUtils.isNotEmpty(shipment.getLetterOfCredit())) {
            info.append(letterOfCredit);
        } else {
            info.append("");
        }
        clausesTable.addCell(makeCellWithNormalWithRightAndBottomBorder(info.toString()));
        document.add(clausesTable);
        //End of CLAUSES

        String statusCode = "";
        com.logiware.edi.entity.Document billOfLadingOriginal = null;
        com.logiware.edi.entity.Document billOfLadingCopy = null;
        com.logiware.edi.entity.Document seaWaybill = null;
        com.logiware.edi.entity.Document houseBillOfLading = null;
        if (null != requestor) {
            for (com.logiware.edi.entity.Document document : requestor.getDocumentSet()) {
                if ("BillOfLadingOriginal".equalsIgnoreCase(document.getNameCode())) {
                    billOfLadingOriginal = document;
                } else if ("BillOfLadingCopy".equalsIgnoreCase(document.getNameCode())) {
                    billOfLadingCopy = document;
                } else if ("SeaWaybill".equalsIgnoreCase(document.getNameCode())) {
                    seaWaybill = document;
                } else if ("HouseBillOfLading".equalsIgnoreCase(document.getNameCode())) {
                    houseBillOfLading = document;
                }
            }
        }

// Adding Empty Table (B/L type)
        document.add(emptyTable);
        PdfPTable blTypeTable = new PdfPTable(4);
        blTypeTable.setWidthPercentage(100);
        blTypeTable.setWidths(new float[]{15, 15, 15, 55});
        blTypeTable.addCell(makeCellWithHeaderWithTopandLeftBorder("B/L Type", ""));
        blTypeTable.addCell(makeCellWithHeaderWithTopBorder("Freighted", ""));
        blTypeTable.addCell(makeCellWithHeaderWithTopBorder("Unfreighted", ""));
        String generalInfo = null == shipment.getGeneralInfo() ? "" : shipment.getGeneralInfo();
        blTypeTable.addCell(makeCellWithHeaderWithoutBottomBorder("B/L Comments", generalInfo));
        if (null != billOfLadingOriginal) {
            blTypeTable.addCell(makeCellWithNormalWithLeftBorder("Original:"));
            if ("DocumentFreight".equalsIgnoreCase(billOfLadingOriginal.getStatusCode())) {
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(billOfLadingOriginal.getQuantity()));
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(""));
            } else {
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(""));
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(billOfLadingOriginal.getQuantity()));
            }
            blTypeTable.addCell(makeCellWithNormalWithoutTopandBottomBorder(""));
        }

        if (null != billOfLadingCopy) {
            blTypeTable.addCell(makeCellWithNormalWithLeftAndBottomBorder("Non-Negotiable:"));
            if ("DocumentFreight".equalsIgnoreCase(billOfLadingCopy.getStatusCode())) {
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(billOfLadingCopy.getQuantity()));
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(""));
            } else {
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(""));
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(billOfLadingCopy.getQuantity()));
            }
            blTypeTable.addCell(makeCellWithNormalWithoutTopBorder(""));
        }

        if (null != seaWaybill) {
            blTypeTable.addCell(makeCellWithNormalWithLeftAndBottomBorder("SeaWay Bill:"));
            if ("DocumentFreight".equalsIgnoreCase(seaWaybill.getStatusCode())) {
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(seaWaybill.getQuantity()));
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(""));
            } else {
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(""));
                blTypeTable.addCell(makeCellWithNormalWithoutBorder(seaWaybill.getQuantity()));
            }
            blTypeTable.addCell(makeCellWithNormalWithoutTopBorder(""));
        }

        String houseBill = null != houseBillOfLading ? "Yes" : "No";
        Phrase blTypePhrase = new Phrase("This is a Standalone House Bill: " + houseBill, blackFontForAR);

        PdfPCell blTypeCell = new PdfPCell(blTypePhrase);
        blTypeCell.setColspan(4);
        blTypeTable.addCell((blTypeCell));
        document.add(blTypeTable);

        //Adding Empty Table (the Partys,  Role = Requestor, and Role = Carrier)
        document.add(emptyTable);
        info.delete(0, info.length());
        PdfPTable partyAddTable = new PdfPTable(2);
        partyAddTable.setWidthPercentage(100);
        partyAddTable.setWidths(new float[]{50, 50});
        //requestor
        if (null != requestor) {
            info.append(requestor.getName() + "\n");
            if (CommonUtils.isNotEmpty(requestor.getAddress())) {
                info.append(requestor.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            partyAddTable.addCell(makeCellWithHeader("REQUESTOR", info.toString()));
        } else {
            partyAddTable.addCell(makeCellWithHeader("REQUESTOR", ""));
        }
        info.delete(0, info.length());
        //carrier
        if (null != carrier) {
            info.append(carrier.getName() + "\n");
            if (CommonUtils.isNotEmpty(carrier.getAddress())) {
                info.append(carrier.getAddress() + "\n\n\n");
            } else {
                info.append("\n\n\n");
            }
            partyAddTable.addCell(makeCellWithHeader("CARRIER", info.toString()));
        }else{
            partyAddTable.addCell(makeCellWithHeader("CARRIER", ""));
        }
        document.add(partyAddTable);
    }

    public void destroy() {
        document.close();
    }

    private PdfPTable getPackageDetails(PdfPTable partTable, StringBuffer info, PackageDetails pack, int count,
            EquipmentDetail equipDetail, Boolean isFirst) {
        info.delete(0, info.length());
        String packType = null == pack.getPackageType() ? "" : pack.getPackageType();
        info.append("(" + count + ")" + packType);
        PdfPCell cell = makeCellWithNormal(info.toString());
        partTable.addCell(cell);
        String marksAndNumber = null == pack.getMarksAndNo() ? "" : pack.getMarksAndNo();
        partTable.addCell(makeCellWithNormal(marksAndNumber));
        info.delete(0, info.length());
        String packTypeDesc = null == pack.getPackageTypeDesc() ? null == pack.getPackageTypeCode() ? "" : pack.getPackageTypeCode() : pack.getPackageTypeDesc();
        //get the split goods details
        SplitgoodsDetails splitGoodsDetails = null;
        if (null != equipDetail && null != equipDetail.getIdentifier()) {
            for (SplitgoodsDetails splitGoods : pack.getSplitgoodsDetailsSet()) {
                if (equipDetail.getIdentifier().equalsIgnoreCase(splitGoods.getEquipmentIdentifier())) {
                    splitGoodsDetails = splitGoods;
                    break;
                }
            }

        }
        if (null != splitGoodsDetails && null != splitGoodsDetails.getNoOfPackage()) {
            info.append(splitGoodsDetails.getNoOfPackage() + " " + packTypeDesc);
        } else {
            info.append(pack.getNoOfPackage() + " " + packTypeDesc);
        }
        partTable.addCell(makeCellWithNormal(info.toString()));
        Paragraph pg = new Paragraph();
        if (null == pack.getGoodsDesc()) {
            pg.add(new Chunk(""));
        } else {
            pg.add(new Chunk(pack.getGoodsDesc(), blackFontForAR));
        }
        if (null != pack.getCommodity() && null != pack.getGoodsDesc()) {
            pg.add(new Chunk("\n" + pack.getCommodity(), blackFontForAR));
        } else {
            pg.add(new Chunk("", blackFontForAR));
        }
        if (null != pack.getReferenceInfo()) {
            pg.add(new Chunk("\n" + pack.getReferenceInfo(), blackFontForAR));
        }
        //get Hazardous details
        for (HazardousGoods haz : pack.getHazardousGoodsSet()) {
            if (null != haz.getImoClassCode()) {
                pg.add(new Chunk("\nIMO CLass ", italicLabelForPrint));
                pg.add(new Chunk(haz.getImoClassCode(), blackFontForAR));
            }
            if (null != haz.getImdgPageNo()) {
                pg.add(new Chunk("\nIMDG Code Page ", italicLabelForPrint));
                pg.add(new Chunk(haz.getImdgPageNo(), blackFontForAR));
            }
            if (null != haz.getUndgNo()) {
                pg.add(new Chunk("\nUNDG Number ", italicLabelForPrint));
                pg.add(new Chunk(haz.getUndgNo(), blackFontForAR));
            }
            if (null != haz.getFlashPointTemperature()) {
                pg.add(new Chunk("\nFlash Point ", italicLabelForPrint));
                pg.add(new Chunk(haz.getFlashPointTemperature() + "" + haz.getFlashPointTemperatureType(), blackFontForAR));
            }
            if (null != haz.getPackagingGroupCode()) {
                pg.add(new Chunk("\nPacking Group ", italicLabelForPrint));
                pg.add(new Chunk(haz.getPackagingGroupCode(), blackFontForAR));
            }
            if (null != haz.getEmsNo()) {
                pg.add(new Chunk("\nEMS Number ", italicLabelForPrint));
                pg.add(new Chunk(haz.getEmsNo(), blackFontForAR));
            }
            if (null != haz.getGoodsComments()) {
                pg.add(new Chunk("\n" + haz.getGoodsComments(), blackFontForAR));
            }
            if (null != haz.getProperShippingName()) {
                pg.add(new Chunk("\n" + haz.getProperShippingName(), blackFontForAR));
            }
            if (null != haz.getEmergencyResponseContact()) {
                pg.add(new Chunk("\nEmergency Contact Name ", italicLabelForPrint));
                pg.add(new Chunk(haz.getEmergencyResponseContact(), blackFontForAR));
            }
            if (null != haz.getCommunicationDetails()) {
                pg.add(new Chunk("\nEmergency Contact Number ", italicLabelForPrint));
                pg.add(new Chunk(haz.getCommunicationDetails(), blackFontForAR));
            }
            break;
        }
        PdfPCell descCell = makeCellWithNormal(pg.getContent().toString());
        //descCell.addElement(pg);
        partTable.addCell(descCell);
        info.delete(0, info.length());
        String goodgrossWeightType = "";
        String goodgrossVolumeType = "";

        if (null != splitGoodsDetails && pack.getSplitgoodsDetailsSet().size() > 1) {
            if ("KGM".equalsIgnoreCase(splitGoodsDetails.getGrossWeightType())) {
                goodgrossWeightType = "KGS";
            } else {
                goodgrossWeightType = null == splitGoodsDetails.getGrossWeightType() ? "" : splitGoodsDetails.getGrossWeightType();
            }
            info.append("Per Container:\n" + splitGoodsDetails.getGrossWeight() + " " + goodgrossWeightType);
            partTable.addCell(makeCellWithNormal(info.toString()));
            info.delete(0, info.length());
            if (null != splitGoodsDetails.getGrossVolume()) {
                if ("MTQ".equalsIgnoreCase(splitGoodsDetails.getGrossVolumeType())) {
                    goodgrossVolumeType = "CBM";
                } else if ("FTQ".equalsIgnoreCase(splitGoodsDetails.getGrossVolumeType())) {
                    goodgrossVolumeType = "CFT";
                } else {
                    goodgrossVolumeType = null == splitGoodsDetails.getGrossVolumeType() ? "" : splitGoodsDetails.getGrossVolumeType();
                }
                info.append("Per Container:\n" + splitGoodsDetails.getGrossVolume() + " " + goodgrossVolumeType);
            } else {
                info.append("");
            }
        } else {
            if ("KGM".equalsIgnoreCase(pack.getGoodgrossWeightType())) {
                goodgrossWeightType = "KGS";
            } else {
                goodgrossWeightType = null == pack.getGoodgrossWeightType() ? "" : pack.getGoodgrossWeightType();
            }
            info.append("Per Container:\n" + pack.getGoodgrossWeight() + " " + goodgrossWeightType);
            partTable.addCell(makeCellWithNormal(info.toString()));
            info.delete(0, info.length());
            if ("MTQ".equalsIgnoreCase(pack.getGoodgrossVolumeType())) {
                goodgrossVolumeType = "CBM";
            } else if ("FTQ".equalsIgnoreCase(pack.getGoodgrossVolumeType())) {
                goodgrossVolumeType = "CFT";
            } else {
                goodgrossVolumeType = null == pack.getGoodgrossVolumeType() ? "" : pack.getGoodgrossVolumeType();
            }
            info.append("Per Container:\n" + pack.getGoodcrossVolume() + " " + goodgrossVolumeType);
        }
        cell = makeCellWithNormal(info.toString());
        partTable.addCell(cell);
        return partTable;
    }

    public String createReport(Shipment shipment, String fileName, String ediCompany)throws Exception {
            this.initialize(shipment, fileName);
            this.createBody(shipment, ediCompany);
            this.destroy();
        return fileName;
    }

    private StringBuffer removeLiteral(StringBuffer info, String literal) {
        int index = info.indexOf(literal);
        if (index > 0) {
            info.delete(index - 1, index + 13);
        }
        return info;
    }

    private PdfPTable getContainerTable() throws DocumentException {
        PdfPTable partTable = new PdfPTable(6);
        partTable.setWidthPercentage(101);
        partTable.getDefaultCell().setBorder(0);
        partTable.setWidths(new float[]{11.2f, 14.5f, 10.2f, 38.3f, 12.5f, 12.4f});
        return partTable;
    }
}
