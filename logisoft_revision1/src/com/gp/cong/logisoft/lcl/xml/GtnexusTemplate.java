/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.xml;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.logisoft.Constants.EDIConstant;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
import com.gp.cong.logisoft.edi.gtnexus.HelperClass;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mei
 */
public class GtnexusTemplate extends CommonUtils implements EDIConstant {

    public String createXML(LclSSMasterBl ssMasterBl, LclSsDetail ssDetail, User user) throws Exception {
        String errorMsg = "";

        String carrierScac = new CarriersOrLineDAO().getCarrierScacCode(ssDetail.getSpAcctNo().getSslineNumber(), "");
        if (isEmpty(carrierScac)) {
            errorMsg = errorMsg + "--> Carrier Scac Code(SSLINE) is not matching<br>";
        } else if (carrierScac.length() < 2 || carrierScac.length() > 4) {
            errorMsg = errorMsg + "--> Carrier Scac Code(SSLINE) length must be between 2 & 10<br>";
        }

        String shipperName = "";
        if (ssMasterBl.getShipSsContactId() != null && ssMasterBl.getShipSsContactId().getCompanyName() != null) {
            shipperName = ssMasterBl.getShipSsContactId().getCompanyName();
            if (shipperName.length() > 35) {
                errorMsg = errorMsg + "--> Master Shipper Name length must be less than 35 characters<br>";
            }
        } else {
            errorMsg = errorMsg + "--> Please Enter Master Shipper Name<br>";
        }

        List<String> shipperAddress = new ArrayList<String>();
        int count = 0;
        if (isNotNull(ssMasterBl.getShipEdi())) {
            for (String shipAddr : ssMasterBl.getShipEdi().split("\n")) {
                count++;
                if (shipAddr.trim().length() > 35) {
                    errorMsg = errorMsg + "--> Master Shipper Address can not be greater than 35 characters per line<br>";
                    break;
                }
                if (count > 4) {
                    errorMsg = errorMsg + "--> Master Shipper Address can not be greater than 4 lines<br>";
                    break;
                }
                shipperAddress.add(shipAddr.trim());
            }
        }
        String consigneeName = "";
        if (ssMasterBl.getConsSsContactId() != null && ssMasterBl.getConsSsContactId().getTradingPartner() != null) {
            consigneeName = ssMasterBl.getConsSsContactId().getTradingPartner().getAccountName();
            if (consigneeName.length() > 35) {
                errorMsg = errorMsg + "--> Master Consignee Name length must be less than 35 characters<br>";
            }
        } else {
            errorMsg = errorMsg + "--> Please Enter Master Consignee Name<br>";
        }
        List<String> consigneeAddress = new ArrayList<String>();
        count = 0;
        if (isNotNull(ssMasterBl.getConsEdi())) {
            for (String consgAddr : ssMasterBl.getConsEdi().split("\n")) {
                count++;
                if (consgAddr.trim().length() > 35) {
                    errorMsg = errorMsg + "--> Master Consignee Address can not be greater than 35 characters per line<br>";
                    break;
                }
                if (count > 4) {
                    errorMsg = errorMsg + "--> Master Consignee Address can not be greater than 4 lines<br>";
                    break;
                }
                consigneeAddress.add(consgAddr.trim());
            }
        }
        String notifyName = "";
        if (ssMasterBl.getNotySsContactId() != null && isNotNull(ssMasterBl.getNotySsContactId().getCompanyName())) {
            notifyName = ssMasterBl.getNotySsContactId().getCompanyName();
            if (notifyName.trim().length() > 35) {
                errorMsg = errorMsg + "--> Master NotifyParty Name length must be less than 35 characters<br>";
            }
        } else {
            errorMsg = errorMsg + "--> Please Enter Master Notify Name<br>";
        }
        count = 0;
        List<String> notifyAddress = new ArrayList<String>();
        if (isNotNull(ssMasterBl.getNotyEdi())) {
            for (String consgAddr : ssMasterBl.getNotyEdi().split("\n")) {
                count++;
                if (consgAddr.trim().length() > 35) {
                    errorMsg = errorMsg + "--> Master Notify Address can not be greater than 35 characters per line<br>";
                    break;
                }
                if (count > 4) {
                    errorMsg = errorMsg + "--> Master Notify Address can not be greater than 4 lines<br>";
                    break;
                }
                notifyAddress.add(consgAddr.trim());
            }
        }

        if (isNotNull(ssMasterBl.getSpContractNo()) && ssMasterBl.getSpContractNo().length() > 30) {
            errorMsg = errorMsg + "Contract Number length is more than 30 characters<br>";
        }

        Boolean hazadousFlag = false;
        List<Long> fileList = new ArrayList<Long>();
        Map<String, List<Long>> fileIdMap = new HashMap<String, List<Long>>();
        List<LclUnitSs> unitSsList = new LclSSMasterBlDAO().getUnitSsListLinkWithMaster(ssMasterBl.getLclSsHeader().getId(), ssMasterBl.getSpBookingNo());
        for (LclUnitSs unitSs : unitSsList) {
            String containerNo = replaceSplChars(unitSs.getLclUnit().getUnitNo(), "[^A-Za-z0-9]+");
            if (containerNo.length() != 11) {
                errorMsg = errorMsg + "--> Container Number : <span style=color:blue>" + unitSs.getLclUnit().getUnitNo() + "</span> Format Should Be 'AAAA-NNNNNN-N'.<br>";
            } else {
                if (isAlpha(containerNo.substring(0, 4), "[0-9]")) {
                    errorMsg = errorMsg + "--> Equipment prefix should contains 4 Characters.<br>";
                }
                if (isNumeric(containerNo.substring(4, containerNo.length()), "[A-Za-z]")) {
                    errorMsg = errorMsg + "--> Equipment Number should contains 6 0r 7 digit Numeric.<br>";
                }
            }

            List<Long> fileIds = new ExportUnitQueryUtils().getPickedFileId(unitSs.getId());
            if (fileIds.isEmpty()) {
                errorMsg = errorMsg + "--> DR's are Not Linked into this Container Number <span style=color:blue>" + unitSs.getLclUnit().getUnitNo() + "</span> <br>";
            }
            fileList.addAll(fileIds);
            fileIdMap.put(unitSs.getLclUnit().getUnitNo(), fileIds);
            if (!hazadousFlag && unitSs.getHazmatPermitted() != null && unitSs.getHazmatPermitted()) {
                hazadousFlag = true;
            }

            if (isNotNull(unitSs.getSUHeadingNote())) {
                if (unitSs.getSUHeadingNote().length() < 2 || unitSs.getSUHeadingNote().length() > 15) {
                    errorMsg = errorMsg + "--> Seal Number length must be, between 2 & 15 for containerNo:" + unitSs.getLclUnit().getUnitNo() + "<br>";
                }
            } else {
                errorMsg = errorMsg + "--> Please Enter Seal Number for container " + unitSs.getLclUnit().getUnitNo() + "<br>";
            }
        }
        String aesItnValues = "";
        String aesExcValues = "";
        String commonAesValues = "";
        if (!fileList.isEmpty()) {
            Lcl3pRefNoDAO _3RefNoDAO = new Lcl3pRefNoDAO();
            aesItnValues = _3RefNoDAO.getAesNo(fileList, "AES_ITNNUMBER");
            aesExcValues = _3RefNoDAO.getAesNo(fileList, "AES_EXCEPTION");
            commonAesValues = CommonUtils.isNotEmpty(aesItnValues) ? aesItnValues : aesExcValues;
            if (isEmpty(commonAesValues)) {
                errorMsg = errorMsg + "--> Please Enter AES/ITN Details <br>";
            }
        }
        if (isNotNull(errorMsg)) {
            return "<span style=color:red;font-size: 10px;>Error Message</span><br>" + errorMsg;
        }
        String companyName = LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName").toUpperCase();
        StringBuilder fileName = new StringBuilder();
        String fileNo = replaceSplChars(ssMasterBl.getLclSsHeader().getScheduleNo() + "_" + ssMasterBl.getSpBookingNo(), "[^A-Za-z0-9_]+");
        String companyCode = LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase();
        String ref = companyCode;
        if("ECI".equals(companyCode)){
            ref = "ECU WW";
        }
        String shipmentId = FILE_304 + "_" + companyName + "_GTN_" + fileNo;
        String partnerName = LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName").toUpperCase();
        String cocode = null != companyCode && companyCode.equalsIgnoreCase("ECI") ? "01" : "02";
        Date now = new Date();
        File file = null;
        String contNoRegex = "[^A-Za-z0-9]";
        String regex3 = "[0-9]";
        String regex4 = "[A-Za-z]";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDate = sdf1.format(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String formatDate = sdf.format(now);
        String xmlFileName = shipmentId + "_" + currentDate;
        String path = LoadLogisoftProperties.getProperty("lcl.gtnexus.xmlLocation") + "/";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        fileName.append(xmlFileName).append(".xml");
        file = new File(folder, fileName.toString());
        OutputStream fout = new FileOutputStream(file);
        OutputStream bout = new BufferedOutputStream(fout);
        OutputStreamWriter out = new OutputStreamWriter(bout, "8859_1");
        String chargeType = "";
        String additionalPayment = "";
        String additionalCharge = "";
        String allDestCharges = "P".equalsIgnoreCase(ssMasterBl.getDestPrepaidCollect()) ? "Prepaid" : "Collect";
        String payment = ssMasterBl.getPrepaidCollect();
        if (payment.equals("P") && allDestCharges.equals("Prepaid")) {
            payment = "Prepaid";
            chargeType = "AllCharges";
        } else if (payment.equals("C") && allDestCharges.equals("Collect")) {
            payment = "Collect";
            chargeType = "AllCharges";
        } else if (payment.equals("P") && allDestCharges.equals("Collect")) {
            payment = "Prepaid";
            chargeType = "BasicFreight";
            additionalPayment = "Collect";
            additionalCharge = "AdditionalCharges";
        } else if (payment.equals("C") && allDestCharges.equals("Prepaid")) {
            payment = "Collect";
            chargeType = "BasicFreight";
            additionalPayment = "Prepaid";
            additionalCharge = "AdditionalCharges";
        }
        out.write("<?xml version=\"1.0\" ");
        out.write("encoding=\"ISO-8859-1\"?>");
        out.write("<BlMessage>");
        out.write("<TransactionInfo AcknowledgementRequested='true'>");

        out.write("<MessageSender>");
        out.write(companyCode);
        out.write("</MessageSender>");
        out.write("<MessageRecipient>");
        out.write("GTNEXUS");
        out.write("</MessageRecipient>");
        out.write("<MessageID>");
        out.write(escapeXml(ssMasterBl.getLclSsHeader().getScheduleNo() + "_" + ssMasterBl.getId()));
        out.write("</MessageID>");
        //erroredTag = "Created Tag";
        out.write("<Created>");
        out.write(formatDate);
        out.write("</Created>");
        //erroredTag = "FileName Tag";
        out.write("<FileName>");
        out.write(escapeXml(fileName.toString()));
        out.write("</FileName>");
        out.write("</TransactionInfo>");
        GenericCode moveTypeCode = new GenericCodeDAO().findByCodeDescName(ssMasterBl.getMoveType(), 48);
        out.write("<BL Sequence='1' BillType='Express'  CarrierScac='" + escapeXml(carrierScac) + "'"
                + " ServiceType='" + escapeXml(moveTypeCode.getField3()) + "' MoveType='"
                + escapeXml(moveTypeCode.getField2()) + "' ShipmentId='" + escapeXml(shipmentId) + "'"
                + " Payment='" + escapeXml(payment) + "' HazardousShipment='" + escapeXml(hazadousFlag ? "true" : "false") + "' >");
        out.write("<References>");
        out.write("<Reference referenceType='BookingNumber'>");
        out.write(escapeXml(ssMasterBl.getSpBookingNo()));
        out.write("</Reference>");
        if (isNotNull(ssMasterBl.getSpContractNo())) {
            out.write("<Reference referenceType='ContractNumber'>");
            out.write(escapeXml(ssMasterBl.getSpContractNo()));
            out.write("</Reference>");
        }

        out.write("<Reference referenceType='ExportReferenceNumber'>");
        out.write(ref + " REF " + escapeXml(ssMasterBl.getLclSsHeader().getScheduleNo()));
        out.write("</Reference>");
        HelperClass helperClass = new HelperClass();
        List<String> exporterrefList = new ArrayList<String>();
        if (isNotNull(ssMasterBl.getExportRefEdi())) {
            for (String expRef : ssMasterBl.getExportRefEdi().split("\n")) {
                exporterrefList = helperClass.splitString(expRef, 30);
            }
        }
        if (CommonUtils.isNotEmpty(exporterrefList)) {
            for (String string : exporterrefList) {
                out.write("<Reference referenceType='ExportReferenceNumber'>");
                out.write(escapeXml(string.toUpperCase()));
                out.write("</Reference>");
            }
        }

        LclInbondsDAO inbondsDAO = new LclInbondsDAO();

        out.write("</References>");


        out.write("<Voyage>");
        out.write("<VoyageNumber>");
        out.write(escapeXml(ssDetail.getSpReferenceNo()));
        out.write("</VoyageNumber>");
        out.write("<Vessel>");
        out.write("<Name>");
        out.write(escapeXml(ssDetail.getSpReferenceName()));
        out.write("</Name>");
        out.write("</Vessel>");
        out.write("</Voyage>");

        out.write("<Parties>");
        out.write("<Party Type='Shipper'>");
        out.write("<Name>");
        out.write(escapeXml(shipperName));
        out.write("</Name>");
        if(shipperAddress.size() > 0) {
            out.write("<Address>");
            for (String shipAddress : shipperAddress) {
                out.write("<AddressLine>");
                out.write(escapeXml(shipAddress).replaceAll("\\p{Cntrl}", ""));
                out.write("</AddressLine>");
            }
            out.write("</Address>");
        }
        out.write("</Party>");

        out.write("<Party Type='Consignee'>");
        out.write("<Name>");
        out.write(escapeXml(consigneeName));
        out.write("</Name>");
        if(consigneeAddress.size() > 0){
            out.write("<Address>");
            for (String consAddress : consigneeAddress) {
                out.write("<AddressLine>");
                out.write(escapeXml(consAddress).replaceAll("\\p{Cntrl}", ""));
                out.write("</AddressLine>");
            }
            out.write("</Address>");
        }
        out.write("</Party>");

        out.write("<Party Type='Notify'>");
        out.write("<Name>");
        out.write(escapeXml(notifyName));
        out.write("</Name>");
        if(notifyAddress.size() > 0) {
            out.write("<Address>");
            for (String notyAddress : notifyAddress) {
                out.write("<AddressLine>");
                out.write(escapeXml(notyAddress).replaceAll("\\p{Cntrl}", ""));
                out.write("</AddressLine>");
            }
            out.write("</Address>");
        }
        out.write("</Party>");
        out.write("<Party Type='Submitter' Code='" + escapeXml(cocode) + "'>");
        out.write("<Name>");
        out.write(escapeXml(partnerName));
        out.write("</Name>");
        if(shipperAddress.size() > 0) {
            out.write("<Address>");
            for (String add : shipperAddress) {
                out.write("<AddressLine>");
                out.write(escapeXml(add));
                out.write("</AddressLine>");
            }
            out.write("</Address>");
        }
        if (null != user) {
            StringBuilder userBuilder = new StringBuilder();
            userBuilder.append("<Contact Type='Information' ");
            if (isNotNull(user.getEmail())) {
                userBuilder.append(" EMail='").append(user.getEmail()).append("'");
            }
            if (isNotNull(user.getFax())) {
                userBuilder.append(" Fax='").append(user.getFax()).append("'");
            }
            if (isNotNull(user.getTelephone())) {
                userBuilder.append(" Phone='").append(user.getTelephone()).append("'");
            }
            userBuilder.append(">").append(escapeXml(user.getFirstName() + " " + user.getLastName()));
            userBuilder.append("</Contact>");
            out.write(userBuilder.toString());
        }
        out.write("</Party>");
        out.write("</Parties>");

        out.write("<Locations>");
        if (null != ssDetail.getDeparture()) {
            out.write("<Location Function='ContractualPlaceOfReceipt' Qualifier='UNLOCODE'  Identifier='" + escapeXml(ssMasterBl.getLclSsHeader().getOrigin().getUnLocationCode()) + "'>");
            out.write("<Name>");
            out.write(escapeXml(ssMasterBl.getLclSsHeader().getOrigin().getUnLocationName().toUpperCase()));
            out.write("</Name>");
            out.write("</Location>");
        }
        if (null != ssMasterBl.getLclSsHeader().getOrigin()) {
            out.write("<Location Function='OperationalPortOfLoading' Qualifier='UNLOCODE'  Identifier='" + escapeXml(ssDetail.getDeparture().getUnLocationCode()) + "'>");
            out.write("<Name>");
            out.write(escapeXml(ssDetail.getDeparture().getUnLocationName().toUpperCase()));
            out.write("</Name>");
            out.write("</Location>");
        }
        if (null != ssMasterBl.getLclSsHeader().getDestination()) {
            out.write("<Location Function='OperationalPortOfDischarge' Qualifier='UNLOCODE'  Identifier='" + escapeXml(ssDetail.getArrival().getUnLocationCode()) + "'>");
            out.write("<Name>");
            out.write(escapeXml(ssDetail.getArrival().getUnLocationName().toUpperCase()));
            out.write("</Name>");
            out.write("</Location>");
        }
        if (ssDetail.getArrival() != null) {
            out.write("<Location Function='ContractualPlaceOfDelivery' Qualifier='UNLOCODE'  Identifier='" + escapeXml(ssMasterBl.getLclSsHeader().getDestination().getUnLocationCode()) + "' >");
            out.write("<Name>");
            out.write(escapeXml(ssMasterBl.getLclSsHeader().getDestination().getUnLocationName().toUpperCase()));
            out.write("</Name>");
            out.write("</Location>");
        }
        //}
        out.write("</Locations>");

        out.write("<Comment >");
        out.write(escapeXml(chargeType + " " + payment));
        out.write("</Comment>");
        out.write("<Comment >");
        out.write(escapeXml(additionalCharge + " " + additionalPayment));
        out.write("</Comment>");
        out.write("<Clause Type='Route'>");
        out.write("All Destination Charges " + escapeXml(allDestCharges));
        out.write("</Clause>");
        if (null != ssMasterBl.getReleaseClause() && isNotNull(ssMasterBl.getReleaseClause().getCodedesc())) {
            out.write("<Clause Type='Route'>");
            out.write(escapeXml(ssMasterBl.getReleaseClause().getCodedesc()));
            out.write("</Clause>");
        }
        for (String aes : commonAesValues.split(",")) {
            out.write("<Clause Type='US SED Flag'>");
            out.write(escapeXml(aes));
            out.write("</Clause>");
        }

        out.write("<ContainerGroups>");
        out.write("<ContainerGroup>");
        StringBuilder commodityString = new StringBuilder();
        LclHazmatDAO hazmatDAO = new LclHazmatDAO();
        EdiDAO ediDAO = new EdiDAO();
        Lcl3pRefNoDAO _3RefNoDAO = new Lcl3pRefNoDAO();
        LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifest();
        for (LclUnitSs unitSs : unitSsList) {
            String equipPrefix = "", equipNoSuffix = "";
            List<Long> fileIdList = fileIdMap.get(unitSs.getLclUnit().getUnitNo());
            String containerNo = replaceSplChars(unitSs.getLclUnit().getUnitNo(), contNoRegex);
            String containerSize = ediDAO.getEquipmentType(unitSs.getLclUnit().getUnitType().getEliteType(), "field5");
            lclUnitSsManifest = ssMasterBl.getLclSsHeader().getlclUnitSsManifestByUnitId(unitSs.getLclUnit().getId());
            if (!isAlpha(containerNo.substring(0, 4), regex3)) {
                equipPrefix = containerNo.substring(0, 4);
            }
            if (!isNumeric(containerNo.substring(4, containerNo.length()), regex4)) {
                equipNoSuffix = containerNo.substring(4, containerNo.length());
            }

            out.write("<Container EquipmentPrefix='" + equipPrefix + "' EquipmentNumber='" + equipNoSuffix + "'  Type='" + containerSize + "'>");
            String sealNo = unitSs.getSUHeadingNote();
            out.write("<SealNumber>");
            out.write(escapeXml(sealNo));
            out.write("</SealNumber>");

            out.write("</Container>");


            commodityString.append("<Commodity Quantity='").append(unitSs.getTotalPieces()).append("' PackagingForm='PACKAGES'>");
            commodityString.append("<Weight Qualifier='Gross' Units='Kilograms'>");
            commodityString.append(String.valueOf(unitSs.getWeightMetric()));
            commodityString.append("</Weight>");
            commodityString.append("<Volume  Units='CubicMeters'>");
            commodityString.append(String.valueOf(unitSs.getVolumeMetric()));
            commodityString.append("</Volume>");
           /* commodityString.append("<DescriptionLine>");
            commodityString.append("STC: ").append(unitSs.getTotalPieces()).append(" PACKAGES");
            commodityString.append("</DescriptionLine>");*/
            if (isNotNull(unitSs.getBlBody())) {
                for (String stdRemark : unitSs.getBlBody().split("\n")) {
                    if(isNotEmpty(stdRemark)){
                    commodityString.append("<DescriptionLine>");
                    commodityString.append(escapeXml(stdRemark.toUpperCase()));
                    commodityString.append("</DescriptionLine>");
                    }
                }
            }





            if (fileIdList != null && !fileIdList.isEmpty()) {
                List<LclBookingHazmat> hazmatList = hazmatDAO.findByFileId(fileIdList);
                if (hazmatList != null && !hazmatList.isEmpty()) {
                    for (LclBookingHazmat bookingHazmat : hazmatList) {
                        if (null == bookingHazmat.getHazmatDeclarations() || "".equalsIgnoreCase(bookingHazmat.getHazmatDeclarations())) {
                            StringBuilder buffer = new StringBuilder();
                            if (bookingHazmat.getReportableQuantity()) {
                                buffer.append("REPORTABLE QUANTITY, ");
                            }
                            if (isNotNull(bookingHazmat.getUnHazmatNo())) {
                                buffer.append("UN ").append(bookingHazmat.getUnHazmatNo());
                            }
                            if (CommonFunctions.isNotNull(bookingHazmat.getProperShippingName())) {
                                buffer.append(", ").append(bookingHazmat.getProperShippingName());
                                buffer.append(CommonUtils.isNotEmpty(bookingHazmat.getTechnicalName()) ? "(" + bookingHazmat.getTechnicalName().toUpperCase() + ")" : "");
                            }
                            if (CommonFunctions.isNotNull(bookingHazmat.getImoPriClassCode())) {
                                buffer.append(", CLASS ").append(bookingHazmat.getImoPriClassCode());
                                buffer.append(CommonUtils.isNotEmpty(bookingHazmat.getImoPriSubClassCode()) ? "(" + bookingHazmat.getImoPriSubClassCode() + ") " : "");
                                buffer.append(CommonUtils.isNotEmpty(bookingHazmat.getImoSecSubClassCode()) ? "(" + bookingHazmat.getImoSecSubClassCode() + ") " : "");
                            }

                            if (isNotNull(bookingHazmat.getPackingGroupCode())) {
                                buffer.append(", PG ").append(bookingHazmat.getPackingGroupCode());
                            }
                            if (CommonFunctions.isNotNull(bookingHazmat.getFlashPoint())) {
                                buffer.append(", ").append("FLASH POINT (").append(bookingHazmat.getFlashPoint()).append(" DEG C)");
                            }
                            if (CommonUtils.isNotEmpty(bookingHazmat.getOuterPkgNoPieces())) {
                                buffer.append(", ").append(bookingHazmat.getOuterPkgNoPieces()).append(" ");
                                buffer.append(CommonUtils.isNotEmpty(bookingHazmat.getOuterPkgComposition()) ? bookingHazmat.getOuterPkgComposition() : " ").append(" ");
                                double outerPkg = bookingHazmat.getOuterPkgNoPieces().doubleValue();
                                String pluralValue = "";

                                if ((int) outerPkg > 1) {
                                    pluralValue = hazmatDAO.getPluralByPkgType(bookingHazmat.getOuterPkgType());
                                    if (CommonUtils.isNotEmpty(pluralValue)) {
                                        buffer.append(bookingHazmat.getOuterPkgType().toUpperCase()).append(pluralValue);
                                    }
                                } else {
                                    buffer.append(bookingHazmat.getOuterPkgType().toUpperCase());
                                }
                                if (CommonUtils.isNotEmpty(bookingHazmat.getInnerPkgNoPieces())) {
                                    buffer.append(", ").append(" CONTAINING ").append(bookingHazmat.getInnerPkgNoPieces()).append(" ");
                                    buffer.append(CommonUtils.isNotEmpty(bookingHazmat.getInnerPkgComposition()) ? bookingHazmat.getInnerPkgComposition() : " ").append(" ");
                                    double innerPkg = bookingHazmat.getInnerPkgNoPieces().doubleValue();
                                    pluralValue = "";
                                    if ((int) innerPkg > 1) {
                                        pluralValue = hazmatDAO.getPluralByPkgType(bookingHazmat.getInnerPkgType());
                                        if (CommonUtils.isNotEmpty(pluralValue)) {
                                            buffer.append(bookingHazmat.getInnerPkgType().toUpperCase()).append(pluralValue);
                                        }
                                    } else {
                                        buffer.append(bookingHazmat.getInnerPkgType().toUpperCase()).append(" ");
                                    }
                                    if (CommonFunctions.isNotNull(bookingHazmat.getInnerPkgNwtPiece())) {
                                        buffer.append(" @ ").append(bookingHazmat.getInnerPkgNwtPiece()).append(" ");
                                    }
                                    buffer.append(CommonUtils.isNotEmpty(bookingHazmat.getInnerPkgUom()) ? bookingHazmat.getInnerPkgUom() + " EACH " : " ").append(" ");
                                }
                            }
                            if (CommonFunctions.isNotNull(bookingHazmat.getTotalGrossWeight())) {
                                buffer.append(", TOTAL GROSS WT ").append(bookingHazmat.getTotalGrossWeight().toString()).append(" KGS");
                            }
                            if (CommonFunctions.isNotNull(bookingHazmat.getTotalNetWeight())) {
                                buffer.append(", TOTAL NET WT ").append(bookingHazmat.getTotalNetWeight().toString()).append(" KGS");
                            }
                            if (CommonFunctions.isNotNull(bookingHazmat.getLiquidVolume()) && (!(bookingHazmat.getLiquidVolume().doubleValue() == 0.000))) {
                                buffer.append(", TOTAL VOLUME ").append(bookingHazmat.getLiquidVolume().toString()).append(" ");
                                float volume = Float.valueOf(bookingHazmat.getLiquidVolume().toString());
                                buffer.append(bookingHazmat.getLiquidVolumeLitreorGals());
                                
                            }
                            if (bookingHazmat.getMarinePollutant()) {
                                buffer.append(", MARINE POLLUTANT");
                            }
                            if (bookingHazmat.getExceptedQuantity()) {
                                buffer.append(", EXCEPTED QUANTITY");
                            }
                            if (bookingHazmat.getLimitedQuantity()) {
                                buffer.append(", LIMITED QUANTITY");
                            }
                            if (bookingHazmat.getInhalationHazard()) {
                                buffer.append(", INHALATION HAZARD");
                            }
                            if (bookingHazmat.getResidue()) {
                                buffer.append(", RESIDUE");
                            }
                            if (CommonUtils.isNotEmpty(bookingHazmat.getEmsCode())) {
                                buffer.append(", EMS ").append(bookingHazmat.getEmsCode());
                            }
                            if (null != bookingHazmat.getEmergencyContact()) {
                                if (CommonUtils.isNotEmpty(bookingHazmat.getEmergencyContact().getContactName())) {
                                    buffer.append(", ").append(bookingHazmat.getEmergencyContact().getContactName()).append(" ");
                                }
                                if (CommonUtils.isNotEmpty(bookingHazmat.getEmergencyContact().getPhone1())) {
                                    buffer.append(", ").append(bookingHazmat.getEmergencyContact().getPhone1()).append("");
                                }
                            }
                            //List<String> hazmatDescList =hc.splitString(buffer.toString(),35);
                            Pattern wrapText = Pattern.compile(".{0,48}(?:\\S(?:-| |$)|$)");
                            List hazmatLists = new ArrayList();
                            Matcher matcher = wrapText.matcher(buffer.toString());
                            while (matcher.find()) {
                                hazmatLists.add(matcher.group());
                            }
                            for (int j = 0; j < hazmatLists.size(); j++) {
                                if (isNotNull(hazmatLists.get(j).toString())) {
                                    commodityString.append("<DescriptionLine>");
                                    commodityString.append(escapeXml(hazmatLists.get(j).toString()));
                                    commodityString.append("</DescriptionLine>");
                                }
                            }
                        } else {
                            if (CommonUtils.isNotEmpty(bookingHazmat.getHazmatDeclarations())) {
                                for (String lineValues : bookingHazmat.getHazmatDeclarations().split("\n")) {
                                    if (isNotNull(lineValues)) {
                                        commodityString.append("<DescriptionLine>");
                                        commodityString.append(escapeXml(lineValues));
                                        commodityString.append("</DescriptionLine>");
                                    }
                                }
                            }

                        }
                    }
                }
            }
            int marksCount = 0;
            String hsCode = new LclBookingHsCodeDAO().getHsCode(fileIdList);
            if (isNotNull(hsCode)) {
                for (String hs : hsCode.split(",")) {
                    commodityString.append("<DescriptionLine>");
                    commodityString.append(marksCount == 0 ? "HS CODE: " : "").append(escapeXml(hs));
                    commodityString.append("</DescriptionLine>");
                    marksCount++;
                }
            }
            marksCount = 0;
            String ncmValues = _3RefNoDAO.getAesNo(fileIdList, "NCM");
            if (isNotNull(ncmValues)) {
                for (String ncm : ncmValues.split(",")) {
                    commodityString.append("<DescriptionLine>");
                    commodityString.append(marksCount == 0 ? "NCM: " : "").append(escapeXml(ncm));
                    commodityString.append("</DescriptionLine>");
                    marksCount++;
                }
            }
            marksCount = 0;
            String inbond = inbondsDAO.getInbond(fileIdList);
            if (isNotNull(inbond)) {
                for (String inbon : inbond.split(",")) {
                    commodityString.append("<MarksAndNumbersLine>");
                    commodityString.append(marksCount == 0 ? "INBOND: " : "").append(escapeXml(inbon));
                    commodityString.append("</MarksAndNumbersLine>");
                }
            }
            marksCount = 0;
            commodityString.append("<MarksAndNumbersLine>");
            commodityString.append(escapeXml(containerNo));
            commodityString.append("</MarksAndNumbersLine>");
            if (isNotNull(sealNo)) {
                commodityString.append("<MarksAndNumbersLine>");
                commodityString.append(escapeXml("SEAL: " + sealNo));
                commodityString.append("</MarksAndNumbersLine>");
            }



            String quantitySize = ediDAO.getEquipmentType(unitSs.getLclUnit().getUnitType().getEliteType(), "field2");

            if (isNotNull(quantitySize)) {
                commodityString.append("<QuantityLine>");
                commodityString.append(escapeXml(quantitySize));
                commodityString.append("</QuantityLine>");
            }



            commodityString.append("<ContainerNumber>");
            commodityString.append(containerNo);
            commodityString.append("</ContainerNumber>");
            commodityString.append("</Commodity>");
        }

        out.write(commodityString.toString());
        out.write("</ContainerGroup>");
        out.write("</ContainerGroups>");
        if (null != ssMasterBl.getReleaseClause() && "2".equalsIgnoreCase(ssMasterBl.getReleaseClause().getCode())) {
            out.write("<Paperwork Type='Original' Rated='true' Quantity='1'/>");
        } else {
            out.write("<Paperwork Type='SeaWaybill' Rated='true' Quantity='1'/>");
        }
        out.write("</BL>");
        out.write("</BlMessage>");
        out.flush();  // Don't forget to flush!
        out.close();

        new LogFileEdiDAO().insertEdi(fileName.toString(), ssMasterBl.getLclSsHeader().getScheduleNo(), ssMasterBl.getSpBookingNo(),
                currentDate, "success", EDI_COMPANY_GTNEXUS, ssMasterBl.getLclSsHeader().getScheduleNo(), FILE_304);
        return "EDI Submited Successfully";
    }

    private boolean isNotNull(String field) {
        if (null != field && !field.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private String replaceSplChars(String str, String pattern) {
        return (str != null ? str.replaceAll(pattern, "").toUpperCase().trim() : "");
    }

    private static boolean isNumeric(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
}
