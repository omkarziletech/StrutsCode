/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.xml;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.ExportUnitQueryUtils;
import com.gp.cong.logisoft.Constants.EDIConstant;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclSSMasterBl;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsManifest;
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
public class InttraTemplate extends CommonUtils implements EDIConstant {

    public String createXML(LclSSMasterBl ssMasterBl, LclSsDetail ssDetail, User user) throws Exception {
        String errorMsg = "";
        String displayMsg = "";
        EdiDAO ediDAO = new EdiDAO();

        if (!ediDAO.validate997(ssMasterBl.getLclSsHeader().getScheduleNo(), ssMasterBl.getSpBookingNo())) {
            displayMsg = "--> Cannot send Amendment before 997 is received<br>";
        }
        if (isNotNull(displayMsg)) {
            return displayMsg;
        }

        String carrierScac = new CarriersOrLineDAO().getCarrierScacCode(ssDetail.getSpAcctNo().getSslineNumber(), "");
        if (!isNotNull(carrierScac)) {
            errorMsg = errorMsg + "--> Carrier Scac Code(SSLINE) is not matching<br>";
        } else if (carrierScac.length() < 2 || carrierScac.length() > 4) {
            errorMsg = errorMsg + "--> Carrier Scac Code(SSLINE) length must be between 2 & 10<br>";
        }

        String shipperName = "";
        if (ssMasterBl.getShipSsContactId() != null && ssMasterBl.getShipSsContactId().getCompanyName() != null) {
            shipperName = ssMasterBl.getShipSsContactId().getCompanyName();
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
        Lcl3pRefNoDAO _3RefNoDAO = new Lcl3pRefNoDAO();
        LclInbondsDAO inbondsDAO = new LclInbondsDAO();
        Map<String, List<Long>> fileIdMap = new HashMap<String, List<Long>>();
        List<LclUnitSs> unitSsList = new LclSSMasterBlDAO().getUnitSsListLinkWithMaster(ssMasterBl.getLclSsHeader().getId(), ssMasterBl.getSpBookingNo());
        StringBuilder aesAppend = new StringBuilder();
        StringBuilder inbondAppend = new StringBuilder();
        List<Long> fileList = new ArrayList<Long>();

        for (LclUnitSs unitSs : unitSsList) {
            String containerNo = replaceSplChars(unitSs.getLclUnit().getUnitNo(), "[^A-Za-z0-9]+");
            if (containerNo.length() != 11) {
                errorMsg = errorMsg + "--> Container Number : <span style=color:blue>" + unitSs.getLclUnit().getUnitNo() + "</span> Format Should Be 'AAAA-NNNNNN-N'.<br>";
            }
            if (isNotNull(unitSs.getSUHeadingNote())) {
                if (unitSs.getSUHeadingNote().length() < 2 || unitSs.getSUHeadingNote().length() > 15) {
                    errorMsg = errorMsg + "--> Seal Number length must be, between 2 & 15 for containerNo:" + unitSs.getLclUnit().getUnitNo() + "<br>";
                }
            } else {
                errorMsg = errorMsg + "--> Please Enter Seal Number for container " + unitSs.getLclUnit().getUnitNo() + "<br>";
            }
            List<Long> fileIds = new ExportUnitQueryUtils().getPickedFileId(unitSs.getId());
            if (fileIds.isEmpty()) {
                errorMsg = errorMsg + "--> DR's are Not Linked into this Container Number <span style=color:blue>" + unitSs.getLclUnit().getUnitNo() + "</span> <br>";
            }
            fileIdMap.put(unitSs.getLclUnit().getUnitNo(), fileIds);
            fileList.addAll(fileIds);
        }
        String aesItnValues = "";
        String aesExcValues = "";
        //String commonAesValues = "";
        if (!fileList.isEmpty()) {
            aesItnValues = _3RefNoDAO.getAesNo(fileList, "AES_ITNNUMBER");
            aesExcValues = _3RefNoDAO.getAesNo(fileList, "AES_EXCEPTION");
            //commonAesValues = CommonUtils.isNotEmpty(aesItnValues) ? aesItnValues : aesExcValues;

            if (!isNotNull(aesItnValues) && !isNotNull(aesExcValues)) {
                errorMsg = errorMsg + "--> Please Enter AES/ITN Details <br>";
            }
        }
        if (isNotNull(errorMsg)) {
            return "<span style=color:red;font-size: 10px;>Error Message</span><br>" + errorMsg;
        }


        StringBuilder fileName = new StringBuilder();
        String companyName = LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName").toUpperCase();
        String companyCode = LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase();
        String ref = companyCode;
        if("ECI".equals(companyCode)){
            ref = "ECU WW";
        }
        String partnerName = LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName").toUpperCase();
        String fileNo = ssMasterBl.getLclSsHeader().getScheduleNo() + "_" + ssMasterBl.getId();
        String fileNo1 = replaceSplChars(ssMasterBl.getLclSsHeader().getScheduleNo() + "_" + ssMasterBl.getSpBookingNo(), "[^A-Za-z0-9_]+");
        Date date = new Date();
        String dateTimeSeconds = DateUtils.formatDate(date, "yyyyMMddHHmmss");
        String folderName = LoadLogisoftProperties.getProperty("lcl.inttra.xmlLocation") + "/";
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        fileName.append(FILE_304).append("_").append(companyName).append("_").append(INTTRA).append("_").append(fileNo1).append("_").append(dateTimeSeconds).append(".xml");
        File file = new File(folder, fileName.toString());
        OutputStream fout = new FileOutputStream(file);
        OutputStream bout = new BufferedOutputStream(fout);
        OutputStreamWriter out = new OutputStreamWriter(bout, "8859_1");

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
        SimpleDateFormat shipmentDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String shipmentDate = shipmentDateFormat.format(now);
        String curdate = sdf.format(now);
        String docVersion = "000001";
        int beginIndex = 1;
        int docVersionCount = ediDAO.getDocVersion(fileName.toString());
        if (docVersionCount > 0) {
            docVersionCount++;
            String docVerString = "" + docVersionCount;
            String prefix = "00000";
            for (int i = 0; i < docVerString.length(); i++) {
                docVersion = prefix + docVersionCount;
                prefix = prefix.substring(beginIndex);
            }
        }

        String msgStatusDoc = "000001".equalsIgnoreCase(docVersion) ? "Original" : "Amendment";
        String allDestCharges = "P".equalsIgnoreCase(ssMasterBl.getDestPrepaidCollect()) ? "Prepaid" : "Collect";
        String paymentType = ssMasterBl.getPrepaidCollect();
        String payment = "";
        String chargeType = "";
        String additionalPayment = "";
        String additionalCharge = "";
        if (paymentType.equals("P") && allDestCharges.equals("Prepaid")) {
            payment = "Prepaid";
            chargeType = "AllCharges";
        } else if (paymentType.equals("C") && allDestCharges.equals("Collect")) {
            payment = "Collect";
            chargeType = "AllCharges";
        } else if (paymentType.equals("P") && allDestCharges.equals("Collect")) {
            payment = "Prepaid";
            chargeType = "BasicFreight";
            additionalPayment = "Collect";
            additionalCharge = "AdditionalCharges";
        } else if (paymentType.equals("C") && allDestCharges.equals("Prepaid")) {
            payment = "Collect";
            chargeType = "BasicFreight";
            additionalPayment = "Prepaid";
            additionalCharge = "AdditionalCharges";
        }
        out.write("<?xml version=\"1.0\" ");
        out.write("encoding=\"ISO-8859-1\"?>");
        out.write("<Message>");
        out.write("<Header>");
        out.write("<MessageType MessageVersion='1.0'>");
        out.write("ShippingInstruction");
        out.write("</MessageType>");
        out.write("<DocumentIdentifier>");
        out.write(ssMasterBl.getSpBookingNo());
        out.write("</DocumentIdentifier>");
        out.write("<DateTime DateType='Document'>");
        out.write(curdate);
        out.write("</DateTime>");
        out.write("<Parties>");
        out.write("<PartnerInformation PartnerRole='Sender'>");
        out.write("<PartnerIdentifier Agency='AssignedBySender'>");
        out.write(companyCode);
        out.write("</PartnerIdentifier>");
        out.write("</PartnerInformation>");
        out.write("<PartnerInformation PartnerRole='Recipient'>");
        out.write("<PartnerIdentifier Agency='AssignedByRecipient'>");
        out.write("INTTRA");
        out.write("</PartnerIdentifier>");
        out.write("</PartnerInformation>");
        out.write("</Parties>");
        out.write("</Header>");
        out.write("<MessageBody>");


        out.write("<MessageProperties>");
        out.write("<ShipmentID>");
        out.write("<ShipmentIdentifier  MessageStatus='" + msgStatusDoc + "'>");
        out.write(ssMasterBl.getLclSsHeader().getScheduleNo() + "_" + ssMasterBl.getId());
        out.write("</ShipmentIdentifier>");
        out.write("<DocumentVersion>");
        out.write(docVersion);
        out.write("</DocumentVersion>");
        out.write("</ShipmentID>");
        out.write("<DateTime DateType='Message'>");
        out.write(shipmentDate);
        out.write("</DateTime>");
        out.write("<ChargeCategory PrepaidorCollectIndicator='" + payment + "' ChargeType='" + chargeType + "'/>");
        if (isNotNull(additionalPayment) && isNotNull(additionalCharge)) {
            out.write("<ChargeCategory PrepaidorCollectIndicator='" + additionalPayment + "' ChargeType='" + additionalCharge + "'/>");
        }
        out.write("<ReferenceInformation ReferenceType='BookingNumber'>");
        out.write(escapeXml(ssMasterBl.getSpBookingNo()));
        out.write("</ReferenceInformation>");
        if (isNotNull(ssMasterBl.getSpContractNo())) {
            out.write("<ReferenceInformation ReferenceType='ContractNumber'>");
            out.write(escapeXml(ssMasterBl.getSpContractNo()));
            out.write("</ReferenceInformation>");
        }
        out.write("<ReferenceInformation ReferenceType='ExportersReferenceNumber'>");
        out.write(ref + " REF#" + escapeXml(ssMasterBl.getLclSsHeader().getScheduleNo()));
        out.write("</ReferenceInformation>");
        if (isNotNull(aesItnValues)) {
            count = 0;
            for (String aesS : aesItnValues.split(",")) {
                aesAppend.append("<ReferenceInformation ReferenceType='TransactionReferenceNumber'>");
                if (count == 0) {
                    aesAppend.append("AES:");
                }
                aesAppend.append(aesS);
                aesAppend.append("</ReferenceInformation>");
                count++;
            }
        }
        count = 0;

        out.write(inbondAppend.toString());
        out.write(aesAppend.toString());
        out.write("<Instructions>");
        out.write("<ShipmentComments CommentType='General'>");
        out.write("All Destination Charges " + escapeXml(allDestCharges));
        out.write("</ShipmentComments>");
        if (null != ssMasterBl.getReleaseClause() && isNotNull(ssMasterBl.getReleaseClause().getCodedesc())) {
            out.write("<ShipmentComments CommentType='General'>");
            out.write(escapeXml(ssMasterBl.getReleaseClause().getCodedesc()));
            out.write("</ShipmentComments>");
        }
        if (isNotNull(ssMasterBl.getExportRefEdi())) {
            for (String expRef : ssMasterBl.getExportRefEdi().split("\n")) {
                out.write("<ShipmentComments CommentType='General'>");
                out.write(escapeXml(expRef.toUpperCase()));
                out.write("</ShipmentComments>");
            }
        }
        out.write("</Instructions>");
        GenericCode moveTypeCode = new GenericCodeDAO().findByCodeDescName(ssMasterBl.getMoveType(), 48);
        if (ssMasterBl.getMoveType() != null) {
            out.write("<HaulageDetails ServiceType='FullLoad' MovementType='" + escapeXml(moveTypeCode.getField4()) + "'/>");
        }
        out.write("<TransportationDetails TransportMode='Maritime' TransportStage='Main'>");
        out.write("<ConveyanceInformation>");

        if (ssDetail.getSpReferenceName() != null) {
            out.write("<ConveyanceName>");
            out.write(escapeXml(ssDetail.getSpReferenceName()));
            out.write("</ConveyanceName>");
        }
        if (ssDetail.getSpReferenceNo() != null) {
            out.write("<VoyageTripNumber>");
            out.write(escapeXml(ssDetail.getSpReferenceNo()));
            out.write("</VoyageTripNumber>");
        }

        if (carrierScac != null) {
            out.write("<CarrierSCAC>");
            out.write(escapeXml(carrierScac));
            out.write("</CarrierSCAC>");
        }
        out.write("</ConveyanceInformation>");
        //if (lclhed.getMovtyp().equals("01") || lclhed.getMovtyp().equals("02") || lclhed.getMovtyp().equals("09")) {
        if (ssMasterBl.getLclSsHeader().getOrigin() != null) {
            out.write("<Location LocationType='PlaceOfReceipt'>");
            out.write("<LocationCode Agency='UN'>");
            out.write(escapeXml(ssMasterBl.getLclSsHeader().getOrigin().getUnLocationCode()));
            out.write("</LocationCode>");
            out.write("<LocationName>");
            out.write(escapeXml(ssMasterBl.getLclSsHeader().getOrigin().getUnLocationName().toUpperCase()));
            out.write("</LocationName>");
            out.write("</Location>");
        }
        if (ssDetail.getDeparture() != null) {
            out.write("<Location LocationType='PortOfLoading'>");
            out.write("<LocationCode Agency='UN'>");
            out.write(escapeXml(ssDetail.getDeparture().getUnLocationCode()));
            out.write("</LocationCode>");
            out.write("<LocationName>");
            out.write(escapeXml(ssDetail.getDeparture().getUnLocationName().toUpperCase()));
            out.write("</LocationName>");
            out.write("</Location>");
        }
        if (ssDetail.getArrival() != null) {
            out.write("<Location LocationType='PortOfDischarge'>");
            out.write("<LocationCode Agency='UN'>");
            out.write(escapeXml(ssDetail.getArrival().getUnLocationCode()));
            out.write("</LocationCode>");
            out.write("<LocationName>");
            out.write(escapeXml(ssDetail.getArrival().getUnLocationName().toUpperCase()));
            out.write("</LocationName>");
            out.write("</Location>");
        }
        if (ssMasterBl.getLclSsHeader().getDestination() != null) {
            out.write("<Location LocationType='PlaceOfDelivery'>");
            out.write("<LocationCode Agency='UN'>");
            out.write(escapeXml(ssMasterBl.getLclSsHeader().getDestination().getUnLocationCode()));
            out.write("</LocationCode>");
            out.write("<LocationName>");
            out.write(escapeXml(ssMasterBl.getLclSsHeader().getDestination().getUnLocationName().toUpperCase()));
            out.write("</LocationName>");
            out.write("</Location>");
        }
        out.write("</TransportationDetails>");
        out.write("<Parties>");
        out.write("<PartnerInformation PartnerRole='Shipper'>");
        out.write("<PartnerName>");
        out.write(escapeXml(shipperName));
        out.write("</PartnerName>");
        if(shipperAddress.size() > 0){
            out.write("<AddressInformation>");

            for (String ship : shipperAddress) {
                out.write("<AddressLine>");
                out.write(escapeXml(ship));
                out.write("</AddressLine>");
            }

            out.write("</AddressInformation>");
        }
        out.write("</PartnerInformation>");
        out.write("<PartnerInformation PartnerRole='Carrier'>");
        out.write("<PartnerIdentifier Agency='AssignedBySender'>");
        if (carrierScac != null && !carrierScac.trim().equals("")) {
            out.write(escapeXml(carrierScac));
        }
        out.write("</PartnerIdentifier>");
        out.write("</PartnerInformation>");
        out.write("<PartnerInformation PartnerRole='Consignee'>");
        out.write("<PartnerName>");
        out.write(escapeXml(consigneeName));
        out.write("</PartnerName>");
        if(consigneeAddress.size() > 0){
            out.write("<AddressInformation>");
            for (String add : consigneeAddress) {
                out.write("<AddressLine>");
                out.write(escapeXml(add));
                out.write("</AddressLine>");
            }
            out.write("</AddressInformation>");
        }
        out.write("</PartnerInformation>");
        out.write("<PartnerInformation PartnerRole='NotifyParty'>");
        out.write("<PartnerName>");
        out.write(escapeXml(notifyName));
        out.write("</PartnerName>");
        if(notifyAddress.size() > 0){
            out.write("<AddressInformation>");
            for (String notify : notifyAddress) {
                out.write("<AddressLine>");
                out.write(escapeXml(notify));
                out.write("</AddressLine>");
            }
            out.write("</AddressInformation>");
        }
        out.write("</PartnerInformation>");
        out.write("<PartnerInformation PartnerRole='Requestor'>");
        out.write("<PartnerIdentifier Agency='AssignedBySender'>");
        out.write(companyCode);
        out.write("</PartnerIdentifier>");
        out.write("<PartnerName>");
        out.write(partnerName);
        out.write("</PartnerName>");
        if (user != null) {
            out.write("<ContactInformation>");
            out.write("<ContactName ContactType='Informational'>");
            out.write(escapeXml(user.getFirstName() + " " + user.getLastName()));
            out.write("</ContactName>");
            if (user.getTelephone() != null) {
                out.write("<CommunicationValue CommunicationType='Telephone'>");
                out.write(escapeXml(user.getTelephone()));
                out.write("</CommunicationValue>");
            }
            if (user.getFax() != null) {
                out.write("<CommunicationValue CommunicationType='Fax'>");
                out.write(escapeXml(user.getFax()));
                out.write("</CommunicationValue>");
            }
            if (user.getEmail() != null) {
                out.write("<CommunicationValue CommunicationType='Email'>");
                out.write(escapeXml(user.getEmail()));
                out.write("</CommunicationValue>");
            }
            out.write("</ContactInformation>");
        }
        if(shipperAddress.size() > 0) {
            out.write("<AddressInformation>");
            for (String add : shipperAddress) {
                out.write("<AddressLine>");
                out.write(escapeXml(add));
                out.write("</AddressLine>");
            }
            out.write("</AddressInformation>");
        }
        out.write("<DocumentationRequirements>");
        if (null != ssMasterBl.getReleaseClause() && "2".equalsIgnoreCase(ssMasterBl.getReleaseClause().getCode())) {
            out.write("<Documents DocumentType='BillOfLadingOriginal' Freighted='True'/>");
        } else {
            out.write("<Documents DocumentType='SeaWaybill' Freighted='True'/>");
        }
        out.write("<Quantity>");
        out.write("1");
        out.write("</Quantity>");
        out.write("</DocumentationRequirements>");
        out.write("</PartnerInformation>");
        out.write("</Parties>");
        out.write("</MessageProperties>");



        out.write("<MessageDetails>");
        StringBuilder goodsDetails = new StringBuilder();
        LclHazmatDAO lclHazmatDAO = new LclHazmatDAO();
        LclUnitSsManifest lclUnitSsManifest = new LclUnitSsManifest();
        for (LclUnitSs unitSs : unitSsList) {
            count++;
            List<Long> fileIdList = fileIdMap.get(unitSs.getLclUnit().getUnitNo());
            List<LclBookingHazmat> hazmatList = new LclHazmatDAO().findByFileId(fileIdList);
            String unitNo = replaceSplChars(unitSs.getLclUnit().getUnitNo(), "[^A-Za-z0-9]+");
            String hsCode = new LclBookingHsCodeDAO().getHsCode(fileIdList);
            lclUnitSsManifest = ssMasterBl.getLclSsHeader().getlclUnitSsManifestByUnitId(unitSs.getLclUnit().getId());
            out.write("<EquipmentDetails>");
            out.write("<LineNumber>");
            out.write(String.valueOf(count));
            out.write("</LineNumber>");
            out.write("<EquipmentIdentifier EquipmentSupplier='Carrier'>");
            out.write(unitNo);
            out.write("</EquipmentIdentifier>");
            String equipType = new GenericCodeDAO().getRegionRemarks(38, unitSs.getLclUnit().getUnitType().getEliteType(), null);
            if (isNotNull(equipType)) {
                out.write("<EquipmentType>");
                out.write("<EquipmentTypeCode>");
                out.write(escapeXml(equipType));
                out.write("</EquipmentTypeCode>");
                out.write("</EquipmentType>");
            }
            out.write("<EquipmentGrossWeight UOM='KGM'>");
            out.write(String.valueOf(unitSs.getWeightMetric()));
            out.write("</EquipmentGrossWeight>");
            out.write("<EquipmentGrossVolume UOM='MTQ'>");
            out.write(String.valueOf(unitSs.getVolumeMetric()));
            out.write("</EquipmentGrossVolume>");
            if (isNotNull(unitSs.getSUHeadingNote())) {
                out.write("<EquipmentSeal SealingParty='Carrier'>");
                out.write(escapeXml(unitSs.getSUHeadingNote()));
                out.write("</EquipmentSeal>");
            }
            out.write("</EquipmentDetails>");
            goodsDetails.append("<GoodsDetails>");
            goodsDetails.append("<LineNumber>");
            goodsDetails.append(String.valueOf(count));
            goodsDetails.append("</LineNumber>");
            goodsDetails.append("<PackageDetail Level='Outer'>");
            goodsDetails.append("<NumberOfPackages>");
            goodsDetails.append(unitSs.getTotalPieces());
            goodsDetails.append("</NumberOfPackages>");
            goodsDetails.append("<PackageTypeDescription>").append("PACKAGES").append("</PackageTypeDescription>");
            goodsDetails.append("</PackageDetail>");
            //goodsDetails.append("<PackageDetailComments CommentType='GoodsDescription'>");
            //goodsDetails.append("STC: ").append(unitSs.getTotalPieces()).append(" PACKAGES</PackageDetailComments>");
            if (isNotNull(unitSs.getBlBody())) {
                for (String stdRemark : unitSs.getBlBody().split("\n")) {
                    if(isNotEmpty(stdRemark)){
                    goodsDetails.append("<PackageDetailComments CommentType='GoodsDescription'>");
                    goodsDetails.append(escapeXml(stdRemark.toUpperCase()));
                    goodsDetails.append("</PackageDetailComments>");
                    }
                }
            }


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
                                pluralValue = lclHazmatDAO.getPluralByPkgType(bookingHazmat.getOuterPkgType());
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
                                    pluralValue = lclHazmatDAO.getPluralByPkgType(bookingHazmat.getInnerPkgType());
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
                                goodsDetails.append("<PackageDetailComments CommentType='GoodsDescription'>");
                                goodsDetails.append(escapeXml(hazmatLists.get(j).toString()));
                                goodsDetails.append("</PackageDetailComments>");
                            }
                        }
                    } else {
                        if (CommonUtils.isNotEmpty(bookingHazmat.getHazmatDeclarations())) {
                            for (String lineValues : bookingHazmat.getHazmatDeclarations().split("\n")) {
                                if (isNotNull(lineValues)) {
                                    goodsDetails.append("<PackageDetailComments CommentType='GoodsDescription'>");
                                    goodsDetails.append(escapeXml(lineValues));
                                    goodsDetails.append("</PackageDetailComments>");
                                }
                            }
                        }

                    }
                }
            }
            if (isNotNull(hsCode)) {
                for (String hs : hsCode.split(",")) {
                    goodsDetails.append("<PackageDetailComments CommentType='GoodsDescription'>");
                    goodsDetails.append(escapeXml("HS CODE: " + hs));
                    goodsDetails.append("</PackageDetailComments>");
                }
            }


            String ncmValues = _3RefNoDAO.getAesNo(fileIdList, "NCM");
            if (isNotNull(ncmValues)) {
                ncmValues = "NCM CODE:" + ncmValues;
                if (ncmValues.length() <= 450) {
                    goodsDetails.append("<PackageDetailComments CommentType='GoodsDescription'>");
                    goodsDetails.append(escapeXml(ncmValues));
                    goodsDetails.append("</PackageDetailComments>");
                }
            }


            goodsDetails.append("<PackageDetailGrossVolume UOM='MTQ'>");
            goodsDetails.append(String.valueOf(unitSs.getVolumeMetric()));
            goodsDetails.append("</PackageDetailGrossVolume>");

            goodsDetails.append("<PackageDetailGrossWeight UOM='KGM'>");
            goodsDetails.append(String.valueOf(unitSs.getWeightMetric()));
            goodsDetails.append("</PackageDetailGrossWeight>");

            goodsDetails.append("<PackageMarks>");
            goodsDetails.append("<Marks>");
            goodsDetails.append(unitNo);
            goodsDetails.append("</Marks>");
            String inbond = inbondsDAO.getInbond(fileIdList);
            if (isNotNull(inbond)) {
                int inbondCount = 0;
                for (String inbon : inbond.split(",")) {
                    inbondAppend.append("<Marks>");
                    inbondAppend.append(inbondCount == 0 ? "INBOND:" : "").append(inbon);
                    inbondAppend.append("</Marks>");
                    inbondCount++;
                }
            }
            goodsDetails.append("</PackageMarks>");
            goodsDetails.append("<SplitGoodsDetails>");
            goodsDetails.append("<EquipmentIdentifier>");
            goodsDetails.append(unitNo);
            goodsDetails.append("</EquipmentIdentifier>");
            goodsDetails.append("<SplitGoodsNumberOfPackages>");
            goodsDetails.append(unitSs.getTotalPieces());
            goodsDetails.append("</SplitGoodsNumberOfPackages>");
            goodsDetails.append("<SplitGoodsGrossVolume UOM='MTQ'>");
            goodsDetails.append(String.valueOf(unitSs.getVolumeMetric()));
            goodsDetails.append("</SplitGoodsGrossVolume>");
            goodsDetails.append("<SplitGoodsGrossWeight UOM='KGM'>");
            goodsDetails.append(String.valueOf(unitSs.getWeightMetric()));
            goodsDetails.append("</SplitGoodsGrossWeight>");
            goodsDetails.append("</SplitGoodsDetails>");
            goodsDetails.append("</GoodsDetails>");
        }
        out.write(goodsDetails.toString());
        out.write("</MessageDetails>");
        out.write("</MessageBody>");


        out.write("</Message>");
        out.flush();  // Don't forget to flush!
        out.close();

        new LogFileEdiDAO().insertEdi(fileName.toString(), ssMasterBl.getLclSsHeader().getScheduleNo(), ssMasterBl.getSpBookingNo(),
                dateTimeSeconds, "success", EDI_COMPANY_INTTRA, ssMasterBl.getLclSsHeader().getScheduleNo(), FILE_304);
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
}
