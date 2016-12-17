package com.gp.cong.logisoft.edi.inttra;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.fcl.EdiTrackingBC;
import com.gp.cong.logisoft.domain.FclInbondDetails;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.edi.util.LogFileWriter;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclAESDetails;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CreateInttraXml {

    private static final Logger log = Logger.getLogger(CreateInttraXml.class);

    public String writeXml(String fileNo, String action ,HttpServletRequest request) throws Exception {
        EdiDAO ediDAO = new EdiDAO();
        String unlocRegex = "[\\W][A-Z]{5}";
        String unctcdRegex = "[^A-Z]";
        String contNoregex = "[^A-Za-z0-9]";
        String company = "I";
        String messageType = "304";
        String displayMessage = "";
        String errorMessage = "";
        String logMessage = "";
        String allDestCharges = "";
        String payment = "";
        String chargeType = "";
        String additionalPayment = "";
        String additionalChargeType = "";
        String aes = "";
        String bookingNo = "";
        String routingInstruction = "";
        String polName = "";
        String polCode = "";
        String porName = "";
        String porCode = "";
        String podName = "";
        String podCode = "";
        String plodName = "";
        String plodCode = "";
        String shipperName = "";
        String consigneeName = "";
        String notifyPartyName = "";
        String exporterref = "";
        String hazardous = "";
        String voyageNo = "";
        String vesselName = "";
        String comnts = "";
        String path = "";
        String moveType = "";
        String moveTypeMethod = "";
        String docIdentifier = "";
        String exporterref1 = "";
        String copyDesc = "";
        String issTerm = "";
        String submitterPhone = "";
        String submitterFax = "";
        String contractNumber = "";
        String scac = "";
        String carrierScac = "";
        String userEmail = "";
        String userName = "";
        String msgStatusDoc = "";
        String weightType = "";
        String docVersion = "000001";
        int docVersionCount = 0;
        String filename = "";
        String exportReference = "";
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        LogFileWriter logFileWriter = new LogFileWriter();
        boolean hasWeight = true;
        boolean hasPackage = true;
        boolean hasContainer = false;
        EdiTrackingBC ediTrackingBC = new EdiTrackingBC();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date dt = new Date();
        String currentDate = dateFormat.format(dt);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
        String curDate = sdf.format(dt);
        SimpleDateFormat shipmentDateFormat = new SimpleDateFormat("yyyyMMdd");
        String shipmentDate = shipmentDateFormat.format(dt);
        boolean xmlcreated = false;
        OutputStreamWriter out = null;
        List<String> exporterrefList = new ArrayList<String>();
        List<String> shipperAddressList = new ArrayList<String>();
        List<String> consigAddressList = new ArrayList<String>();
        List<String> notifyPartyAddressList = new ArrayList<String>();
        List<String> contNoList = new ArrayList<String>();
        List<String> equipmentTypeList = new ArrayList<String>();
        List<String> descriptionsList = new ArrayList<String>();
        List<String> copyDescList = new ArrayList<String>();
        List<String> marksList = null;
        List<String> stdRemarksList = new ArrayList<String>();
        List<String> weightLBSList = new ArrayList<String>();
        List<String> volumeCFTList = new ArrayList<String>();
        List<String> weightKGSList = new ArrayList<String>();
        List<String> volumeCBMList = new ArrayList<String>();
        List<String> quantityList = new ArrayList<String>();
        List<String> packageFormList = new ArrayList<String>();
        List<String> sealNoList = new ArrayList<String>();
        List<String> comntsList = new ArrayList<String>();
        List<String> subAddressList = new ArrayList<String>();

        Map<String, List<String>> marksMap = new HashMap<String, List<String>>();
        Map<String, List<String>> stdRemarksMap = new HashMap<String, List<String>>();
        Map<String, List<String>> descriptionsMap = new HashMap<String, List<String>>();
        Map<String, List<String>> packageFormMap = new HashMap<String, List<String>>();
        Map<String, List<String>> copyDescMap = new HashMap<String, List<String>>();
        Map<String, List<String>> weightLBSMap = new HashMap<String, List<String>>();
        Map<String, List<String>> volumeCFTMap = new HashMap<String, List<String>>();
        Map<String, List<String>> weightKGSMap = new HashMap<String, List<String>>();
        Map<String, List<String>> volumeCBMMap = new HashMap<String, List<String>>();
        Map<String, List<String>> quantityMap = new HashMap<String, List<String>>();
        Map<String, List> hazmatMap = new HashMap<String, List>();
        try {
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
            HelperClass helperClass = new HelperClass();
            FclBl fclbl = new FclBlDAO().getOriginalBl(fileNo);
            String excludeCharcter = LoadLogisoftProperties.getProperty("edi.exclude.character");
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(null != fileNo ? fileNo.indexOf("-") > -1 ? fileNo.substring(0, fileNo.indexOf("-")) : fileNo : "0");
            Quotation quotation = new QuotationDAO().getFileNoObject(null != fileNo ? fileNo.indexOf("-") > -1 ? fileNo.substring(0, fileNo.indexOf("-")) : fileNo : "0");
            List fclInbondList = new FclInbondDetailsDAO().findByProperty("bolId", fclbl.getBol());
            docIdentifier = "04" + fileNo;
            exporterref1 = "04-" + fileNo;
            String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName").toUpperCase() : "";
            filename = "304_" + companyName + "_INTTRA_" + docIdentifier + "_" + currentDate + ".xml";
            if (isNotNull(fclbl.getDestinationChargesPreCol())) {
                if (fclbl.getDestinationChargesPreCol().substring(0, 1).equals("P")) {
                    allDestCharges = "Prepaid";
                } else if (fclbl.getDestinationChargesPreCol().substring(0, 1).equals("C")) {
                    allDestCharges = "Collect";
                }
            }
            if (isNotNull(fclbl.getStreamShipBl())) {
                if (fclbl.getStreamShipBl().substring(0, 1).equals("P") && allDestCharges.equals("Prepaid")) {
                    payment = "Prepaid";
                    chargeType = "AllCharges";
                } else if (fclbl.getStreamShipBl().substring(0, 1).equals("C") && allDestCharges.equals("Collect")) {
                    payment = "Collect";
                    chargeType = "AllCharges";
                } else if (fclbl.getStreamShipBl().substring(0, 1).equals("P") && allDestCharges.equals("Collect")) {
                    payment = "Prepaid";
                    chargeType = "BasicFreight";
                    additionalPayment = "Collect";
                    additionalChargeType = "AdditionalCharges";
                } else if (fclbl.getStreamShipBl().substring(0, 1).equals("C") && allDestCharges.equals("Prepaid")) {
                    payment = "Collect";
                    chargeType = "BasicFreight";
                    additionalPayment = "Prepaid";
                    additionalChargeType = "AdditionalCharges";
                }
            }else if(CommonUtils.isNotEqualIgnoreCase(fclbl.getImportFlag(), "I")) {
                displayMessage = displayMessage + "Please choose SSL BL Prepaid/Collect<br>";
            }
            if (isNotNull(fclbl.getFclBLClause())) {
                routingInstruction = ediDAO.getRoutingInstruction(fclbl.getFclBLClause());
            }
            if (isNotNull(fclbl.getLineMove()) && !"00".equals(fclbl.getLineMove())) {
                moveType = fclbl.getLineMove();
                moveTypeMethod = ediDAO.getMoveType(fclbl.getLineMove(), "field4");
            } else {
                displayMessage = displayMessage + "--> Please Select LineMove<br>";
            }
            if (isNotNull(fclbl.getTerminal()) && fclbl.getTerminal().contains("/")) {
                String[] location = fclbl.getTerminal().split("/");
                if (fclbl.getTerminal().lastIndexOf("(") != -1 && fclbl.getTerminal().lastIndexOf(")") != -1) {
                    porCode = fclbl.getTerminal().substring(fclbl.getTerminal().lastIndexOf("(") + 1, fclbl.getTerminal().lastIndexOf(")"));;
                }
                porName = location[0];
                if (porName.length() > 24) {
                    porName = porName.substring(0, 24);
                }
            }
            if (moveType.equals("DOOR TO DOOR") || moveType.equals("DOOR TO PORT") || moveType.equals("DOOR TO RAIL")) {
                if (!isNotNull(porCode)) {
                    displayMessage = displayMessage + "--> Please Enter Place Of Receipt<br>";
                }
            }
            if (isNotNull(fclbl.getPortOfLoading()) && fclbl.getPortOfLoading().contains("/")) {
                String[] location = fclbl.getPortOfLoading().split("/");
                if (fclbl.getPortOfLoading().lastIndexOf("(") != -1 && fclbl.getPortOfLoading().lastIndexOf(")") != -1) {
                    polCode = fclbl.getPortOfLoading().substring(fclbl.getPortOfLoading().lastIndexOf("(") + 1, fclbl.getPortOfLoading().lastIndexOf(")"));;
                }
                polName = location[0];
                if (polName.length() > 24) {
                    polName = polName.substring(0, 24);
                }
            }
            if (!isNotNull(polCode)) {
                displayMessage = displayMessage + "--> Please Enter Port Of Loading<br>";
            }
            if (isNotNull(fclbl.getPortofDischarge()) && fclbl.getPortofDischarge().contains("/")) {
                String[] location = fclbl.getPortofDischarge().split("/");
                if (fclbl.getPortofDischarge().lastIndexOf("(") != -1 && fclbl.getPortofDischarge().lastIndexOf(")") != -1) {
                    podCode = fclbl.getPortofDischarge().substring(fclbl.getPortofDischarge().lastIndexOf("(") + 1, fclbl.getPortofDischarge().lastIndexOf(")"));;
                }
                podName = location[0];
                if (podName.length() > 24) {
                    podName = podName.substring(0, 24);
                }
            }
            if (!isNotNull(podCode)) {
                displayMessage = displayMessage + "--> Please Enter Port Of Discharge<br>";
            }
            if (isNotNull(fclbl.getFinalDestination()) && fclbl.getFinalDestination().contains("/")) {
                String[] location = fclbl.getFinalDestination().split("/");
                if (fclbl.getFinalDestination().lastIndexOf("(") != -1 && fclbl.getFinalDestination().lastIndexOf(")") != -1) {
                    plodCode = fclbl.getFinalDestination().substring(fclbl.getFinalDestination().lastIndexOf("(") + 1, fclbl.getFinalDestination().lastIndexOf(")"));;
                }
                plodName = location[0];
                if (plodName.length() > 24) {
                    plodName = plodName.substring(0, 24);
                }
            }
            if (moveType.equals("DOOR TO DOOR") || moveType.equals("PORT TO DOOR") || moveType.equals("RAIL TO DOOR")) {
                if (!isNotNull(plodCode)) {
                    displayMessage = displayMessage + "--> Please Enter Place Of Delivery<br>";
                }
            }
            if (isNotNull(fclbl.getHouseShipperName())) {
                shipperName = fclbl.getHouseShipperName();
            } else {
                displayMessage = displayMessage + "--> Please Enter Master Shipper Name<br>";
            }
            if (isNotNull(fclbl.getHouseShipperAddress())) {
                shipperAddressList = helperClass.wrapAddress(fclbl.getHouseShipperAddress());
            }
            if (isNotNull(fclbl.getHouseConsigneeName())) {
                consigneeName = fclbl.getHouseConsigneeName();
            } else {
                displayMessage = displayMessage + "--> Please Enter Master Consignee Name<br>";
            }
            if (isNotNull(fclbl.getExportReference())) {
                exportReference = fclbl.getExportReference();
            }
            if (isNotNull(fclbl.getHouseConsigneeAddress())) {
                consigAddressList = helperClass.wrapAddress(fclbl.getHouseConsigneeAddress());
            }
            if (isNotNull(fclbl.getHouseNotifyPartyName())) {
                notifyPartyName = fclbl.getHouseNotifyPartyName();
                if (isNotNull(fclbl.getHouseNotifyParty())) {
                    notifyPartyAddressList = helperClass.wrapAddress(fclbl.getHouseNotifyParty());
                }
            }

            /* Need clarification*/
            if (isNotNull(fclbl.getBookingNo())) {
                bookingNo = fclbl.getBookingNo();
            } else {
                errorMessage = errorMessage + "--> Booking Number is invalid<br>";
            }
            if (isNotNull(fclbl.getExportReference())) {
                exporterref = fclbl.getExportReference();
                exporterrefList = helperClass.wrapText(exporterref);
            }
            hazardous = null != bookingFcl ? bookingFcl.getHazmat() : "";
            if ("Y".equalsIgnoreCase(hazardous)) {
                hazardous = "true";
            } else {
                hazardous = "false";
            }
            if (isNotNull(fclbl.getBillingTerminal())) {
                if (fclbl.getBillingTerminal().contains(",")) {
                    String[] str = fclbl.getBillingTerminal().split(",");
                    String itmnum = str[1].trim().substring(str[1].lastIndexOf("-"));
                    userEmail = ediDAO.getUserEmail(itmnum);
                    if (userEmail != null && !userEmail.equals("") && userEmail.contains("@")) {
                        int indexname = userEmail.indexOf("@");
                        userName = userEmail.substring(0, indexname);
                    }
                    RefTerminal terminal = ediDAO.findByTerminal(itmnum);
                    if (null != terminal) {
                        if (null != terminal.getAddres1() && !terminal.getAddres1().trim().equals("")) {
                            subAddressList.add(terminal.getAddres1());
                        }
                        if (null != terminal.getAddres2() && !terminal.getAddres2().trim().equals("")) {
                            subAddressList.add(terminal.getAddres2());
                        }
                        String addressLine1 = helperClass.conCat(terminal.getCity1(), terminal.getState());
                        if (null != addressLine1 && !addressLine1.trim().equals("")) {
                            subAddressList.add(addressLine1);
                        }
                        String addressLine2 = helperClass.conCat(ediDAO.getCountry(null != terminal.getCountry() ? terminal.getCountry().getCodedesc() : ""), terminal.getZipcde());
                        if (null != addressLine2 && !addressLine2.trim().equals("")) {
                            subAddressList.add(addressLine2);
                        }
                    }
                }
            } else {
                displayMessage = displayMessage + "--> Please Enter Issuing Terminal<br>";
            }
            userName = user.getFirstName();
            if (null != user.getFax() && !user.getFax().trim().equals("")) {
                submitterFax = user.getFax();
            }
            if (null != user.getEmail() && !user.getEmail().trim().equals("")) {
                userEmail = user.getEmail();
            }
            if (null != user.getTelephone() && !user.getTelephone().trim().equals("")) {
                submitterPhone = user.getTelephone();
            }
            if (null != fclbl.getVessel() && CommonFunctions.isNotNull(fclbl.getVessel().getCodedesc())) {
                vesselName = fclbl.getVessel().getCodedesc();
            } else {
                displayMessage = displayMessage + "--> Please Enter Vessel Name<br>";
            }
            if (isNotNull(fclbl.getVoyages())) {
                voyageNo = fclbl.getVoyages();
            } else {
                displayMessage = displayMessage + "--> Please Enter Voyage Number <br>";
            }
            if (isNotNull(fclbl.getSslineNo())) {
                carrierScac = ediDAO.getSsLine(fclbl.getSslineNo());
                if (null != carrierScac && !carrierScac.trim().equals("") && !carrierScac.trim().equals("00000")) {
                    scac = ediDAO.getScacOrContract(carrierScac, "SCAC");
                    contractNumber = ediDAO.getScacOrContract(carrierScac, "FCL_contract_number");
                }
            }
            if (!isNotNull(scac)) {
                errorMessage = errorMessage + "--> Carrier Scac Code(SSLINE) is not matching<br>";
            } else if (scac.length() < 2 || scac.length() > 4) {
                errorMessage = errorMessage + "--> Carrier Scac Code(SSLINE) length must be between 2 & 10<br>";
            }
            List fclBlContainerList = new FclBlContainerDAO().getAllContainers(fclbl.getBol().toString());
            for (Object object : fclBlContainerList) {
                FclBlContainer fclBlContainer = (FclBlContainer) object;
                String contNo = "";
                if (!"D".equalsIgnoreCase(fclBlContainer.getDisabledFlag())) {
                    hasContainer = true;
                    if (isNotNull(fclBlContainer.getTrailerNo())) {
                        contNo = replaceSpecialChars(fclBlContainer.getTrailerNo(), contNoregex);
                        if (contNo.length() >= 11) {
                            contNoList.add(contNo);
                        } else {
                            displayMessage = displayMessage + "--> Container Number is not valid <br>";
                        }
                        sealNoList.add(fclBlContainer.getSealNo());
                        equipmentTypeList.add(ediDAO.getEquipmantType("" + fclBlContainer.getSizeLegend().getId(), "field1"));
                        if (isNotNull(fclBlContainer.getMarks())) {
                            if (marksList == null) {
                                marksList = helperClass.wrapAddress(fclBlContainer.getMarks());
                            } else {
                                marksList.addAll(helperClass.wrapAddress(fclBlContainer.getMarks()));
                            }
                        }
                        List hazmatList = new HazmatMaterialDAO().findbydoctypeid("Fclbl", fclBlContainer.getTrailerNoId().toString());
                        List fclBlMarksList = ediDAO.findFclBlMarks(fclBlContainer.getTrailerNoId());
                        if (CommonUtils.isNotEmpty(fclBlMarksList)) {
                            for (Object object1 : fclBlMarksList) {
                                String stc = "";
                                FclBlMarks fclBlMarks = (FclBlMarks) object1;
                                if (CommonFunctions.isNotNull(fclBlMarks.getNoOfPkgs())) {
                                    quantityList.add(fclBlMarks.getNoOfPkgs().toString());
                                    stc = "STC: " + fclBlMarks.getNoOfPkgs().toString();
                                } else {
                                    displayMessage = displayMessage + "--> Please Enter No of Pieces for Container " + fclBlContainer.getTrailerNo() + "<br>";
                                }
                                if (CommonFunctions.isNotNull(fclBlMarks.getMeasureCbm())) {
                                    volumeCBMList.add(fclBlMarks.getMeasureCbm().toString());
                                }
                                if (CommonFunctions.isNotNull(fclBlMarks.getMeasureCft())) {
                                    volumeCFTList.add(fclBlMarks.getMeasureCft().toString());
                                }
                                if (CommonFunctions.isNotNull(fclBlMarks.getNetweightKgs())) {
                                    weightKGSList.add(fclBlMarks.getNetweightKgs().toString());
                                }
                                if (CommonFunctions.isNotNull(fclBlMarks.getNetweightLbs())) {
                                    weightLBSList.add(fclBlMarks.getNetweightLbs().toString());
                                }
                                if (isNotNull(fclBlMarks.getUom())) {
                                    String uom = new GenericCodeDAO().getPackageType("Package Type", fclBlMarks.getUom().trim());
                                    if (isNotNull(uom)) {
                                        stc += " " + uom;
                                        packageFormList.add(uom);
                                    } else {
                                        stc += " " + fclBlMarks.getUom();
                                        packageFormList.add(fclBlMarks.getUom());
                                    }
                                } else {
                                    packageFormList.add("");
                                    displayMessage = displayMessage + "--> Please Enter Package Type for Container " + fclBlContainer.getTrailerNo() + "<br>";
                                }
                                if (isNotNull(fclBlMarks.getCopyDescription())) {
                                    copyDescList.add(fclBlMarks.getCopyDescription());
                                }
                                if (isNotNull(fclBlMarks.getDescPckgs())) {
                                    descriptionsList.add(stc + " \n" + fclBlMarks.getDescPckgs());
                                }
                                if (isNotNull(fclBlMarks.getDescForMasterBl())) {
                                    stdRemarksList.add(stc + " \n" + fclBlMarks.getDescForMasterBl());
                                }
                            }
                        } else {
                            displayMessage = displayMessage + "--> Please Enter Package Details for Containers " + fclBlContainer.getTrailerNo() + "<br>";
                            hasPackage = false;
                        }
                        descriptionsMap.put(contNo, descriptionsList);
                        marksMap.put(contNo, marksList != null ? marksList : new ArrayList());
                        stdRemarksMap.put(contNo, stdRemarksList);
                        quantityMap.put(contNo, quantityList);
                        volumeCBMMap.put(contNo, volumeCBMList);
                        volumeCFTMap.put(contNo, volumeCFTList);
                        weightKGSMap.put(contNo, weightKGSList);
                        weightLBSMap.put(contNo, weightLBSList);
                        packageFormMap.put(contNo, packageFormList);
                        copyDescMap.put(contNo, copyDescList);
                        hazmatMap.put(contNo, hazmatList);
                        //reset all list values here
                        descriptionsList = new ArrayList();
                        marksList = null;
                        stdRemarksList = new ArrayList();
                        quantityList = new ArrayList();
                        volumeCBMList = new ArrayList();
                        volumeCFTList = new ArrayList();
                        weightLBSList = new ArrayList();
                        weightKGSList = new ArrayList();
                        packageFormList = new ArrayList();
                        copyDescList = new ArrayList();
                    } else {
                        displayMessage = displayMessage + "--> Please Enter Unit Number for all Containers<br>";
                        break;
                    }
                }
            }
            if (!hasContainer) {
                displayMessage = displayMessage + "--> Atleast one container should be enabled<br>";
            }
            //XML Creation Starts Here
            String shipmentId = docIdentifier + "_" + currentDate;

            File file = null;
            if (osName().contains("linux")) {
                file = new File(prop.getProperty("linuxInttraXmlOut"));
                path = prop.getProperty("linuxInttraXmlOut") + filename;
            } else {
                file = new File(prop.getProperty("inttraXmlOut"));
                path = prop.getProperty("inttraXmlOut") + filename;
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            out = new OutputStreamWriter(bout, "8859_1");
            xmlcreated = true;
            int beginIndex = 1;
            docVersionCount = ediDAO.getDocVersion(filename);
            if (docVersionCount > 0) {
                docVersionCount++;
                String docVerString = "" + docVersionCount;
                String prefix = "00000";
                for (int i = 0; i < docVerString.length(); i++) {
                    docVersion = prefix + docVersionCount;
                    prefix = prefix.substring(beginIndex);
                }
            }
            if (docVersion.equals("000001")) {
                msgStatusDoc = "Original";
            } else {
                msgStatusDoc = "Amendment";
            }
            out.write("<?xml version=\"1.0\" ");
            out.write("encoding=\"ISO-8859-1\"?>");
            out.write("<Message>");
            out.write("<Header>");
            out.write("<MessageType MessageVersion='1.0'>");
            out.write("ShippingInstruction");
            out.write("</MessageType>");
            out.write("<DocumentIdentifier>");
            out.write(docIdentifier);
            out.write("</DocumentIdentifier>");
            out.write("<DateTime DateType='Document'>");
            out.write(curDate);
            out.write("</DateTime>");
            out.write("<Parties>");
            out.write("<PartnerInformation PartnerRole='Sender'>");
            out.write("<PartnerIdentifier Agency='AssignedBySender'>");
            out.write(null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "");
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
            out.write(shipmentId);
            out.write("</ShipmentIdentifier>");
            out.write("<DocumentVersion>");
            out.write(docVersion);
            out.write("</DocumentVersion>");
            out.write("</ShipmentID>");
            out.write("<DateTime DateType='Message'>");
            out.write(shipmentDate);
            out.write("</DateTime>");
            out.write("<ChargeCategory PrepaidorCollectIndicator='" + payment + "' ChargeType='" + chargeType + "'/>");
            if (isNotNull(additionalPayment) && isNotNull(additionalChargeType)) {
                out.write("<ChargeCategory PrepaidorCollectIndicator='" + additionalPayment + "' ChargeType='" + additionalChargeType + "'/>");
            }
            if (isNotNull(bookingNo)) {
                out.write("<ReferenceInformation ReferenceType='BookingNumber'>");
                out.write(escapeXml(bookingNo));
                out.write("</ReferenceInformation>");
            }
            if (isNotNull(contractNumber)) {
                out.write("<ReferenceInformation ReferenceType='ContractNumber'>");
                out.write(escapeXml(contractNumber));
                out.write("</ReferenceInformation>");
            }
            String companyCode = null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "";
            if (isNotNull(exporterref1)) {
                out.write("<ReferenceInformation ReferenceType='ExportersReferenceNumber'>");
                out.write(companyCode + " Ref " + escapeXml(exporterref1));
                out.write("</ReferenceInformation>");
            }
            if (CommonUtils.isNotEmpty(exporterrefList)) {
                for (String string : exporterrefList) {
                    out.write("<ReferenceInformation ReferenceType='ExportersReferenceNumber'>");
                    out.write(escapeXml(string));
                    out.write("</ReferenceInformation>");
                }
            }
            if (CommonUtils.isNotEmpty(fclInbondList)) {
                for (Object object : fclInbondList) {
                    FclInbondDetails fclInbondDetails = (FclInbondDetails) object;
                    if (CommonUtils.isNotEmpty(fclInbondDetails.getInbondNumber())) {
                        out.write("<ReferenceInformation ReferenceType='ExportersReferenceNumber'>");
                        out.write(escapeXml(fclInbondDetails.getInbondNumber()));
                        out.write("</ReferenceInformation>");
                    }
                }
            }
            List aesList = new FclInbondDetailsDAO().findAesdetails("fileNo", fclbl.getFileNo());
            if (CommonUtils.isNotEmpty(aesList)) {
                for (Object object : aesList) {
                    FclAESDetails fclAESDetails = (FclAESDetails) object;
                    if (isNotNull(fclAESDetails.getAesDetails())) {
                        out.write("<ReferenceInformation ReferenceType='TransactionReferenceNumber'>");
                        out.write(escapeXml(fclAESDetails.getAesDetails()));
                        out.write("</ReferenceInformation>");
                    }
                    if (isNotNull(fclAESDetails.getException())) {
                        out.write("<ReferenceInformation ReferenceType='TransactionReferenceNumber'>");
                        out.write(escapeXml(fclAESDetails.getException()));
                        out.write("</ReferenceInformation>");
                    }
                }
            } else {
                if (!"I".equalsIgnoreCase(fclbl.getImportFlag())) {
                    displayMessage = displayMessage + "--> Please Enter AES/ITN details <br>";
                }
            }
            if (isNotNull(routingInstruction) || isNotNull(allDestCharges)) {
                out.write("<Instructions>");
                if (isNotNull(allDestCharges)) {
                    out.write("<ShipmentComments CommentType='General'>");
                    out.write("All Destination Charges " + allDestCharges);
                    out.write("</ShipmentComments>");
                }
                if (isNotNull(routingInstruction)) {
                    out.write("<ShipmentComments CommentType='General'>");
                    out.write(escapeXml(routingInstruction));
                    out.write("</ShipmentComments>");
                }
                out.write("</Instructions>");
            }
            out.write("<HaulageDetails MovementType='" + moveTypeMethod + "' ServiceType='FullLoad'/>");
            out.write("<TransportationDetails TransportStage='Main' TransportMode='Maritime'>");
            out.write("<ConveyanceInformation>");
            out.write("<ConveyanceName>");
            out.write(escapeXml(vesselName));
            out.write("</ConveyanceName>");
            out.write("<VoyageTripNumber>");
            out.write(escapeXml(voyageNo));
            out.write("</VoyageTripNumber>");
            if (isNotNull(scac)) {
                out.write("<CarrierSCAC>");
                out.write(escapeXml(scac));
                out.write("</CarrierSCAC>");
            }
            out.write("</ConveyanceInformation>");
            if (isNotNull(porCode)) {
                out.write("<Location LocationType='PlaceOfReceipt'>");
                out.write("<LocationCode Agency='UN'>");
                out.write(escapeXml(porCode));
                out.write("</LocationCode>");
                if (isNotNull(porName)) {
                    out.write("<LocationName>");
                    out.write(escapeXml(porName));
                    out.write("</LocationName>");
                }
                out.write("</Location>");
            }
            if (isNotNull(polCode)) {
                out.write("<Location LocationType='PortOfLoading'>");
                out.write("<LocationCode Agency='UN'>");
                out.write(escapeXml(polCode));
                out.write("</LocationCode>");
                if (isNotNull(polName)) {
                    out.write("<LocationName>");
                    out.write(escapeXml(polName));
                    out.write("</LocationName>");
                }
                out.write("</Location>");
            }
            if (isNotNull(podCode)) {
                out.write("<Location LocationType='PortOfDischarge'>");
                out.write("<LocationCode Agency='UN'>");
                out.write(escapeXml(podCode));
                out.write("</LocationCode>");
                if (isNotNull(podName)) {
                    out.write("<LocationName>");
                    out.write(escapeXml(podName));
                    out.write("</LocationName>");
                }
                out.write("</Location>");
            }
            if (isNotNull(plodCode)) {
                out.write("<Location LocationType='PlaceOfDelivery'>");
                out.write("<LocationCode Agency='UN'>");
                out.write(escapeXml(plodCode));
                out.write("</LocationCode>");
                if (isNotNull(plodName)) {
                    out.write("<LocationName>");
                    out.write(escapeXml(plodName));
                    out.write("</LocationName>");
                }
                out.write("</Location>");
            }
            out.write("</TransportationDetails>");
            out.write("<Parties>");
            out.write("<PartnerInformation PartnerRole='Shipper'>");
            if (isNotNull(shipperName)) {
                if (shipperName.length() > 35) {
                    errorMessage = errorMessage + "--> Master Shipper Name length must be less than 35 characters<br>";
                }
                out.write("<PartnerName>");
                out.write(escapeXml(shipperName));
                out.write("</PartnerName>");
                int count = 0;
                if (CommonUtils.isNotEmpty(shipperAddressList)) {
                    out.write("<AddressInformation>");
                    for (String address : shipperAddressList) {
                        count++;
                        if (address.length() > 35) {
                            errorMessage = errorMessage + "--> Master Shipper Address can not be greater than 35 characters per line<br>";
                            break;
                        }
                        if (count > 4) {
                            errorMessage = errorMessage + "--> Master Shipper Address can not be greater than 4 lines<br>";
                            break;
                        }
                        String escapedAddress = escapeXml(address).replaceAll("\\p{Cntrl}", "");
                        if (CommonUtils.isNotEmpty(escapedAddress)) {
                            out.write("<AddressLine>");
                            out.write(escapedAddress);
                            out.write("</AddressLine>");
                        }
                    }
                    out.write("</AddressInformation>");
                }
            }
            out.write("</PartnerInformation>");
            out.write("<PartnerInformation PartnerRole='Carrier'>");
            if (isNotNull(scac)) {
                out.write("<PartnerIdentifier Agency='AssignedBySender'>");
                out.write(escapeXml(scac));
                out.write("</PartnerIdentifier>");
            }
            out.write("</PartnerInformation>");
            out.write("<PartnerInformation PartnerRole='Consignee'>");
            if (isNotNull(consigneeName)) {
                if (consigneeName.length() > 35) {
                    errorMessage = errorMessage + "--> Master Consignee Name length must be less than 35 characters<br>";
                }
                out.write("<PartnerName>");
                out.write(escapeXml(consigneeName));
                out.write("</PartnerName>");
                int count = 0;
                if (CommonUtils.isNotEmpty(consigAddressList)) {
                    out.write("<AddressInformation>");
                    for (String address : consigAddressList) {
                        count++;
                        if (address.length() > 35) {
                            errorMessage = errorMessage + "--> Master Consignee Address can not be greater than 35 characters per line<br>";
                            break;
                        }
                        if (count > 4) {
                            errorMessage = errorMessage + "--> Master Consignee Address can not be greater than 4 lines<br>";
                            break;
                        }
                        String escapedAddress = escapeXml(address).replaceAll("\\p{Cntrl}", "");
                        if (CommonUtils.isNotEmpty(escapedAddress)) {
                            out.write("<AddressLine>");
                            out.write(escapedAddress);
                            out.write("</AddressLine>");
                        }
                    }
                    out.write("</AddressInformation>");
                }
            }
            out.write("</PartnerInformation>");
            if (isNotNull(notifyPartyName)) {
                if (notifyPartyName.length() > 35) {
                    errorMessage = errorMessage + "--> Master NotifyParty Name length must be less than 35 characters<br>";
                }
                out.write("<PartnerInformation PartnerRole='NotifyParty'>");
                out.write("<PartnerName>");
                out.write(escapeXml(notifyPartyName));
                out.write("</PartnerName>");
                int count = 0;
                if (CommonUtils.isNotEmpty(notifyPartyAddressList)) {
                    out.write("<AddressInformation>");
                    for (String address : notifyPartyAddressList) {
                        count++;
                        if (address.length() > 35) {
                            errorMessage = errorMessage + "--> Master NotifyParty Address can not be greater than 35 characters per line<br>";
                            break;
                        }
                        if (count > 4) {
                            errorMessage = errorMessage + "--> Master NotifyParty Address can not be greater than 4 lines<br>";
                            break;
                        }
                        String escapedAddress = escapeXml(address).replaceAll("\\p{Cntrl}", "");
                        if (CommonUtils.isNotEmpty(escapedAddress)) {
                            out.write("<AddressLine>");
                            out.write(escapedAddress);
                            out.write("</AddressLine>");
                        }
                    }
                    out.write("</AddressInformation>");
                }
                out.write("</PartnerInformation>");
            }
            if (isNotNull(exportReference)) {
                if (exportReference.length() > 30) {
                    errorMessage = errorMessage + "--> Export Reference length must be less than 30 characters<br>";
                }
            }
            out.write("<PartnerInformation PartnerRole='Requestor'>");
            out.write("<PartnerIdentifier Agency='AssignedBySender'>");
            out.write(null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "");
            out.write("</PartnerIdentifier>");
            out.write("<PartnerName>");
            out.write(null != LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName").toUpperCase() : "");
            out.write("</PartnerName>");
            out.write("<ContactInformation>");
            if (isNotNull(userName)) {
                out.write("<ContactName ContactType='Informational' >");
                out.write(escapeXml(userName));
                out.write("</ContactName>");
            }
            if (isNotNull(submitterPhone)) {
                out.write("<CommunicationValue CommunicationType='Telephone'>");
                out.write(escapeXml(submitterPhone));
                out.write("</CommunicationValue>");
            }
            if (isNotNull(submitterFax)) {
                out.write("<CommunicationValue CommunicationType='Fax'>");
                out.write(escapeXml(submitterFax));
                out.write("</CommunicationValue>");
            }
            if (isNotNull(userEmail)) {
                out.write("<CommunicationValue CommunicationType='Email'>");
                out.write(escapeXml(userEmail));
                out.write("</CommunicationValue>");
            }
            out.write("</ContactInformation>");
            if (subAddressList != null && !subAddressList.isEmpty()) {
                out.write("<AddressInformation>");
                for (String address : subAddressList) {
                    String escapedAddress = escapeXml(address).replaceAll("\\p{Cntrl}", "");
                    if (CommonUtils.isNotEmpty(escapedAddress)) {
                        out.write("<AddressLine>");
                        out.write(escapedAddress);
                        out.write("</AddressLine>");
                    }
                }
                out.write("</AddressInformation>");
            }
            out.write("<DocumentationRequirements>");
            if (isNotNull(fclbl.getFclBLClause()) && "2".equals(fclbl.getFclBLClause())) {
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
            if (contNoList != null && !contNoList.isEmpty()) {
                int count = 0;
                for (int x = 0; x < contNoList.size(); x++) {
                    count++;
                    out.write("<EquipmentDetails>");
                    out.write("<LineNumber>");
                    out.write(String.valueOf(count));
                    out.write("</LineNumber>");
                    out.write("<EquipmentIdentifier EquipmentSupplier='Carrier'>");
                    out.write(contNoList.get(x));
                    out.write("</EquipmentIdentifier>");
                    if (equipmentTypeList != null && !equipmentTypeList.isEmpty()
                            && null != equipmentTypeList.get(x) && !equipmentTypeList.get(x).trim().equals("")) {
                        out.write("<EquipmentType>");
                        out.write("<EquipmentTypeCode>");
                        out.write(escapeXml(equipmentTypeList.get(x)));
                        out.write("</EquipmentTypeCode>");
                        out.write("</EquipmentType>");
                    }
                    boolean weightBoolean = false;

                    weightKGSList = weightKGSMap.get(contNoList.get(x));
                    weightLBSList = weightLBSMap.get(contNoList.get(x));
                    volumeCFTList = volumeCFTMap.get(contNoList.get(x));
                    volumeCBMList = volumeCBMMap.get(contNoList.get(x));
                    if (CommonUtils.isNotEmpty(weightKGSList) && CommonUtils.isNotEmpty(volumeCBMList)) {
                        for (int i = 0; i < weightKGSList.size(); i++) {
                            float weight = Float.parseFloat(weightKGSList.get(i));
                            if (weight != 0) {
                                out.write("<EquipmentGrossWeight UOM='KGM'>");
                                out.write(String.valueOf(weight));
                                out.write("</EquipmentGrossWeight>");
                                weightBoolean = true;
                                weightType = "KGS";
                            }
                        }
                    }
                    if (CommonUtils.isNotEmpty(weightLBSList) && CommonUtils.isNotEmpty(volumeCFTList)) {
                        for (int i = 0; i < weightLBSList.size(); i++) {
                            float weight = Float.parseFloat(weightLBSList.get(i));
                            if (weight != 0 && !weightBoolean) {
                                out.write("<EquipmentGrossWeight UOM='LBS'>");
                                out.write(String.valueOf(weight));
                                out.write("</EquipmentGrossWeight>");
                                weightBoolean = true;
                                weightType = "LBS";
                            }
                        }
                    }
                    if (!weightBoolean) {
                        hasWeight = false;
                    }
                    boolean volumeBoolean = false;
                    if (CommonUtils.isNotEmpty(volumeCFTList) && !weightType.equals("KGS")) {
                        for (int i = 0; i < volumeCFTList.size(); i++) {
                            float volume = Float.parseFloat(volumeCFTList.get(i));
                            if (volume != 0) {
                                out.write("<EquipmentGrossVolume UOM='FTQ'>");
                                out.write(String.valueOf(volume));
                                out.write("</EquipmentGrossVolume>");
                                volumeBoolean = true;
                            }
                        }
                    }
                    if (CommonUtils.isNotEmpty(volumeCBMList) && !weightType.equals("LBS")) {
                        for (int i = 0; i < volumeCBMList.size(); i++) {
                            float volume = Float.parseFloat(volumeCBMList.get(i));
                            if (volume != 0 && !volumeBoolean) {
                                out.write("<EquipmentGrossVolume UOM='MTQ'>");
                                out.write(String.valueOf(volume));
                                out.write("</EquipmentGrossVolume>");
                            }
                        }
                    }
                    if (CommonUtils.isNotEmpty(sealNoList)
                            && isNotNull(sealNoList.get(x).toString())) {
                        out.write("<EquipmentSeal SealingParty='Carrier'>");
                        out.write(escapeXml(sealNoList.get(x)));
                        out.write("</EquipmentSeal>");
                    } else {
                        displayMessage = displayMessage + "--> Please Enter Seal Number for container " + contNoList.get(x) + "<br>";
                    }
                    out.write("</EquipmentDetails>");
                }

            }
            int countWeight = 0;
            for (int w = 0; w < contNoList.size(); w++) {
                String containerNo = contNoList.get(w).toString();
                if (CommonUtils.isNotEmpty(containerNo) && containerNo.length() == 11) {
                    containerNo = containerNo.substring(0, 4) + "-" + containerNo.substring(4, 10) + "-" + containerNo.substring(10, 11);
                }
                out.write("<GoodsDetails>");
                List hazmatMaterialList = hazmatMap.get(contNoList.get(w));
                quantityList = quantityMap.get(contNoList.get(w));
                packageFormList = packageFormMap.get(contNoList.get(w));
                copyDescList = copyDescMap.get(contNoList.get(w));
                stdRemarksList = stdRemarksMap.get(contNoList.get(w));
                descriptionsList = descriptionsMap.get(contNoList.get(w));
                if (CommonUtils.isNotEmpty(quantityList)) {
                    boolean isFirstContainer = true;
                    for (int i = 0; i < quantityList.size(); i++) {
                        String quantity = quantityList.get(i);
                        int noOfPack = 0;
                        if (isNotNull(quantity)) {
                            noOfPack = Integer.parseInt(quantity);
                        }
                        countWeight++;
                        out.write("<LineNumber>");
                        out.write(String.valueOf(countWeight));
                        out.write("</LineNumber>");
                        out.write("<PackageDetail Level='Outer'>");
                        out.write("<NumberOfPackages>");
                        out.write(String.valueOf(noOfPack));
                        out.write("</NumberOfPackages>");
                        if (CommonUtils.isNotEmpty(packageFormList) && packageFormList.size() > i) {
                            out.write("<PackageTypeDescription>");
                            out.write(escapeXml(packageFormList.get(i)));
                            out.write("</PackageTypeDescription>");
                        }
                        out.write("</PackageDetail>");
                        if (CommonUtils.isNotEmpty(copyDescList) && copyDescList.size() > i) {
                            if ("N".equalsIgnoreCase(copyDescList.get(i))) {
                                List<String> stdRemarkList = (List<String>) new ArrayList();
                                if (!stdRemarksList.isEmpty() && stdRemarksList.size() > i && CommonUtils.isExcludeEdiCharacter(stdRemarksList.get(i), excludeCharcter)) {
                                    errorMessage = errorMessage + "--> Please Remove the following Special Characters " + excludeCharcter + " from Master BL Description for Container " + containerNo;
                                } else if (!stdRemarksList.isEmpty() && stdRemarksList.size() > i) {
                                    stdRemarkList = helperClass.wrapAddress(stdRemarksList.get(i).replaceAll("["+excludeCharcter+"]", ""));
                                }
                                if (CommonUtils.isNotEmpty(stdRemarkList)) {
                                    for (int j = 0; j < stdRemarkList.size(); j++) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(stdRemarkList.get(j).toString()));
                                        out.write("</PackageDetailComments>");
                                    }
                                } else {
                                    displayMessage = displayMessage + "--> Column CopyDescription is 'N' and Master BL Description is not entered for Container " + containerNo + "<br>";
                                }
                            } else if ("Y".equalsIgnoreCase(copyDescList.get(i))) {
                                List<String> descList = (List<String>) new ArrayList();
                                if (!descriptionsList.isEmpty() && descriptionsList.size() > i && CommonUtils.isExcludeEdiCharacter(descriptionsList.get(i), excludeCharcter)) {
                                    errorMessage = errorMessage + "--> Please Remove the following Special Characters " + excludeCharcter + " from House BL Description for Container " + containerNo;
                                } else if (!descriptionsList.isEmpty() && descriptionsList.size() > i) {
                                    descList = helperClass.wrapAddress(descriptionsList.get(i).replaceAll("["+excludeCharcter+"]", ""));
                                }
                                if (CommonUtils.isNotEmpty(descList)) {
                                    for (int j = 0; j < descList.size(); j++) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(descList.get(j)));
                                        out.write("</PackageDetailComments>");
                                    }
                                } else {
                                    displayMessage = displayMessage + "--> Column CopyDescription is 'Y' and House BL Description is not entered for Container " + containerNo + "<br>";
                                }
                            }
                        }
                        if (CommonUtils.isNotEmpty(hazmatMaterialList)) {
                            for (Object object : hazmatMaterialList) {
                                HazmatMaterial hazmatMaterial = (HazmatMaterial) object;
                                if (!"Y".equalsIgnoreCase(hazmatMaterial.getFreeFormat())) {
                                    StringBuilder buffer = new StringBuilder();
                                    if ("Y".equalsIgnoreCase(hazmatMaterial.getReportableQuantity())) {
                                        buffer.append("REPORTABLE QUANTITY, ");
                                    }
                                    if (isNotNull(hazmatMaterial.getUnNumber())) {
                                        buffer.append("UN ").append(hazmatMaterial.getUnNumber());
                                    }
                                    if (isNotNull(hazmatMaterial.getPropShipingNumber())) {
                                        buffer.append(", ").append(hazmatMaterial.getPropShipingNumber()).append("").append(isNotNull(hazmatMaterial.getTechnicalName()) ? ", (" + hazmatMaterial.getTechnicalName() + ")" : "");
                                    }
                                    if (isNotNull(hazmatMaterial.getImoClssCode())) {
                                        buffer.append(", CLASS ").append(hazmatMaterial.getImoClssCode()).append(" ").append(isNotNull(hazmatMaterial.getImoSubsidiaryClassCode()) ? "(" + hazmatMaterial.getImoSubsidiaryClassCode() + ")" : "").append(isNotNull(hazmatMaterial.getImoSecondarySubClass()) ? "(" + hazmatMaterial.getImoSecondarySubClass() + ")" : "");
                                    }
                                    if (isNotNull(hazmatMaterial.getPackingGroupCode())) {
                                        buffer.append(", PG ").append(hazmatMaterial.getPackingGroupCode());
                                    }
                                    if (isNotNull(hazmatMaterial.getFlashPointUMO())) {
                                        buffer.append(", FLASH POINT (").append(hazmatMaterial.getFlashPointUMO()).append(" DEG C)");
                                    }
                                    if (isNotNull(hazmatMaterial.getOuterPackingPieces())) {
                                        buffer.append(", ").append(hazmatMaterial.getOuterPackingPieces()).append(" ").append(isNotNull(hazmatMaterial.getOuterPackComposition()) ? hazmatMaterial.getOuterPackComposition() : "").append(" ").append(isNotNull(hazmatMaterial.getOuterPackagingType()) ? hazmatMaterial.getOuterPackagingType() : "");

                                        if (isNotNull(hazmatMaterial.getInnerPackingPieces())) {
                                            buffer.append(", ").append(hazmatMaterial.getInnerPackingPieces()).append(" ").append(isNotNull(hazmatMaterial.getInnerPackComposition()) ? hazmatMaterial.getInnerPackComposition() : "").append(" ").append(isNotNull(hazmatMaterial.getInnerPackagingType()) ? hazmatMaterial.getInnerPackagingType() : "");
                                        }
                                        if (CommonUtils.isNotEmpty(hazmatMaterial.getNetWeight())) {
                                            buffer.append(" @ ").append(hazmatMaterial.getNetWeight().toString()).append(" ").append(hazmatMaterial.getNetWeightUMO()).append(" EACH");
                                        }
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getGrossWeight())) {
                                        buffer.append(", TOTAL GROSS WT ").append(hazmatMaterial.getGrossWeight().toString()).append(" KGS");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getTotalNetWeight())) {
                                        buffer.append(", TOTAL NET WT ").append(hazmatMaterial.getTotalNetWeight().toString()).append(" KGS");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getVolume())) {
                                        buffer.append(", TOTAL VOLUME ").append(hazmatMaterial.getVolume().toString()).append(" ");
                                        float volume = Float.valueOf(hazmatMaterial.getVolume().toString());
                                        if (volume > 1d) {
                                            buffer.append("LITERS");
                                        } else {
                                            buffer.append("LITER");
                                        }
                                    }
                                    if ("Y".equalsIgnoreCase(hazmatMaterial.getMarinePollutant())) {
                                        buffer.append(", MARINE POLLUTANT");
                                    }
                                    if ("Y".equalsIgnoreCase(hazmatMaterial.getExceptedQuantity())) {
                                        buffer.append(", EXCEPTED QUANTITY");
                                    }
                                    if ("Y".equalsIgnoreCase(hazmatMaterial.getLimitedQuantity())) {
                                        buffer.append(", LIMITED QUANTITY");
                                    }
                                    if ("Y".equalsIgnoreCase(hazmatMaterial.getInhalationHazard())) {
                                        buffer.append(", INHALATION HAZARD");
                                    }
                                    if ("Y".equalsIgnoreCase(hazmatMaterial.getResidue())) {
                                        buffer.append(", RESIDUE");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getEmsCode())) {
                                        buffer.append(", EMS ").append(hazmatMaterial.getEmsCode());
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getContactName())) {
                                        buffer.append(", ").append(hazmatMaterial.getContactName()).append(" ");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getEmerreprsNum())) {
                                        buffer.append(", ").append(hazmatMaterial.getEmerreprsNum()).append("");
                                    }
                                    //List<String> hazmatDescList =hc.splitString(buffer.toString(),35);
                                    Pattern wrapText = Pattern.compile(".{0,48}(?:\\S(?:-| |$)|$)");
                                    List hazmatList = new ArrayList();
                                    Matcher matcher = wrapText.matcher(buffer.toString());
                                    while (matcher.find()) {
                                        hazmatList.add(matcher.group());
                                    }
                                    for (int j = 0; j < hazmatList.size(); j++) {
                                        if (isNotNull(hazmatList.get(j).toString())) {
                                            out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                            out.write(escapeXml(hazmatList.get(j).toString()));
                                            out.write("</PackageDetailComments>");
                                        }
                                    }
                                } else {
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine1())) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(hazmatMaterial.getLine1()));
                                        out.write("</PackageDetailComments>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine2())) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(hazmatMaterial.getLine2()));
                                        out.write("</PackageDetailComments>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine3())) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(hazmatMaterial.getLine3()));
                                        out.write("</PackageDetailComments>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine4())) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(hazmatMaterial.getLine4()));
                                        out.write("</PackageDetailComments>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine5())) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(hazmatMaterial.getLine5()));
                                        out.write("</PackageDetailComments>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine6())) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(hazmatMaterial.getLine6()));
                                        out.write("</PackageDetailComments>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine7())) {
                                        out.write("<PackageDetailComments CommentType='GoodsDescription'>");
                                        out.write(escapeXml(hazmatMaterial.getLine7()));
                                        out.write("</PackageDetailComments>");
                                    }
                                }
                            }
                        }
                        boolean volumeBoolean = false;
                        boolean weightBoolean = false;
                        volumeCFTList = volumeCFTMap.get(contNoList.get(w));
                        weightKGSList = weightKGSMap.get(contNoList.get(w));
                        volumeCBMList = volumeCBMMap.get(contNoList.get(w));
                        weightLBSList = weightLBSMap.get(contNoList.get(w));
                        if (CommonUtils.isEmpty(weightKGSList)) {
                            hasWeight = false;
                        }
                        if (CommonUtils.isNotEmpty(weightKGSList) && weightKGSList.size() > i) {
                            float weight = Float.parseFloat(weightKGSList.get(i));
                            if (weight != 0) {
                                out.write("<PackageDetailGrossWeight UOM='KGM'>");
                                out.write(String.valueOf(weight));
                                out.write("</PackageDetailGrossWeight>");
                                weightBoolean = true;
                                weightType = "KGS";
                            }
                        }
                        if (CommonUtils.isNotEmpty(weightLBSList) && weightLBSList.size() > i) {
                            float weight = Float.parseFloat(weightLBSList.get(i));
                            if (weight != 0 && !weightBoolean) {
                                out.write("<PackageDetailGrossWeight UOM='LBS'>");
                                out.write(String.valueOf(weight));
                                out.write("</PackageDetailGrossWeight>");
                                weightBoolean = true;
                                weightType = "LBS";
                            }
                        }
                        if (!weightBoolean) {
                            hasWeight = false;
                        }
                        if (CommonUtils.isNotEmpty(volumeCFTList) && !weightType.equals("KGS") && volumeCFTList.size() > i) {
                            float volume = Float.parseFloat(volumeCFTList.get(i));
                            if (volume != 0) {
                                out.write("<PackageDetailGrossVolume UOM='FTQ'>");
                                out.write(String.valueOf(volume));
                                out.write("</PackageDetailGrossVolume>");
                                volumeBoolean = true;
                            }
                        }
                        if (CommonUtils.isNotEmpty(volumeCBMList) && !weightType.equals("LBS") && volumeCBMList.size() > i) {
                            float volume = Float.parseFloat(volumeCBMList.get(i));
                            if (volume != 0 && !volumeBoolean) {
                                out.write("<PackageDetailGrossVolume UOM='MTQ'>");
                                out.write(String.valueOf(volume));
                                out.write("</PackageDetailGrossVolume>");
                            }
                        }
                        out.write("<SplitGoodsDetails>");
                        if (contNoList != null && !contNoList.isEmpty() && isFirstContainer) {
                            out.write("<EquipmentIdentifier>");
                            out.write(contNoList.get(w));
                            out.write("</EquipmentIdentifier>");
                            isFirstContainer = false;
                        }
                        out.write("<SplitGoodsNumberOfPackages>");
                        out.write(String.valueOf(noOfPack));
                        out.write("</SplitGoodsNumberOfPackages>");
                        if (CommonUtils.isNotEmpty(weightKGSList) && weightKGSList.size() > i) {
                            float weight = Float.parseFloat(weightKGSList.get(i));
                            if (weight != 0) {
                                out.write("<SplitGoodsGrossWeight UOM='KGM'>");
                                out.write(String.valueOf(weight));
                                out.write("</SplitGoodsGrossWeight>");
                                weightBoolean = true;
                                weightType = "KGS";
                            }
                        }
                        if (CommonUtils.isNotEmpty(weightLBSList) && volumeCFTList.size() > i) {
                            float weight = Float.parseFloat(weightLBSList.get(i));
                            if (weight != 0 && !weightBoolean) {
                                out.write("<SplitGoodsGrossWeight UOM='LBS'>");
                                out.write(String.valueOf(weight));
                                out.write("</SplitGoodsGrossWeight>");
                                weightBoolean = true;
                                weightType = "LBS";
                            }
                        }
                        if (!weightBoolean && !hasWeight && hasPackage) {
                            displayMessage = displayMessage + "--> Weights are not entered for container " + containerNo + "<br>";
                        }
                        if (CommonUtils.isNotEmpty(volumeCFTList) && volumeCFTList.size() > i && !weightType.equals("KGS")) {
                            float volume = Float.parseFloat(volumeCFTList.get(i));
                            if (volume != 0) {
                                out.write("<SplitGoodsGrossVolume UOM='FTQ'>");
                                out.write(String.valueOf(volume));
                                out.write("</SplitGoodsGrossVolume>");
                                volumeBoolean = true;
                            }
                        }
                        if (CommonUtils.isNotEmpty(volumeCBMList) && volumeCBMList.size() > i && !weightType.equals("LBS")) {
                            float volume = Float.parseFloat(volumeCBMList.get(i));
                            if (volume != 0 && !volumeBoolean) {
                                out.write("<SplitGoodsGrossVolume UOM='MTQ'>");
                                out.write(String.valueOf(volume));
                                out.write("</SplitGoodsGrossVolume>");
                            }
                        }
                        out.write("</SplitGoodsDetails>");
                    }
                    List<String> markList = (List<String>) marksMap.get(contNoList.get(w));
                    if (CommonUtils.isNotEmpty(markList)) {
                        out.write("<PackageMarks>");
                        for (int j = 0; j < markList.size(); j++) {
                            out.write("<Marks>");
                            out.write(escapeXml(markList.get(j)));
                            out.write("</Marks>");
                        }
                        out.write("</PackageMarks>");
                    } else {
                        out.write("<PackageMarks>");
                        out.write("<Marks>");
                        out.write(escapeXml(contNoList.get(w).toString()));
                        out.write("</Marks>");
                        if (CommonUtils.isNotEmpty(sealNoList)) {
                            String slno = sealNoList.get(w).toString();
                            if (isNotNull(sealNoList.get(w).toString())) {
                                out.write("<Marks>");
                                out.write(escapeXml("SEAL: " + slno));
                                out.write("</Marks>");
                            }
                        }
                        out.write("</PackageMarks>");

                    }
                }
                out.write("</GoodsDetails>");
            }
            out.write("</MessageDetails>");
            out.write("</MessageBody>");
            out.write("</Message>");
            out.flush();  // Don't forget to flush!
            out.close();
            String errorFileName = "error_logfile_" + filename + "_" + currentDate + ".txt";
            if (isNotNull(errorMessage)) {
                if (!"validate".equalsIgnoreCase(action)) {
                    ediTrackingBC.setEdiLog(filename, currentDate, "failure", errorMessage, company, messageType, fileNo, fclbl.getBookingNo(), "", null);
                    logFileWriter.doAppend(errorMessage.replaceAll("<br>", "\n"), errorFileName, company, osName(), messageType);
                }
                File deleteFile = new File(path);
                deleteFile.delete();
                return "<span color: #000080;font-size: 10px;>Error Message</span><br>" + errorMessage;
            } else if (isNotNull(displayMessage)) {
                File deleteFile = new File(path);
                deleteFile.delete();
                return displayMessage;
            } else {
                if (!"validate".equalsIgnoreCase(action)) {
                    ediTrackingBC.setEdiLog(filename, currentDate, "success", "No Error", company, messageType, fileNo, fclbl.getBookingNo(), "", null);
                }
                return "XML generated successfully";
            }
        } catch (Exception e) {
            log.info("writeXml failed on " + new Date(), e);
            if (!"validate".equalsIgnoreCase(action)) {
                String errorFileName = "error_logfile_" + filename + "_" + currentDate + ".txt";
                ediTrackingBC.setEdiLog(filename, currentDate, "failure", displayMessage, company, messageType, fileNo, "", "", null);
                logFileWriter.doAppend("Type of Error is---" + e.toString(), errorFileName, company, osName(), messageType);
            }
            try {
                if (out != null && xmlcreated) {
                    out.flush();  // Don't forget to flush!
                    out.close();
                    File deleteFile = new File(path);
                    if (deleteFile.delete()) {
                    }
                }
            } catch (Exception ee) {
                log.info("writeXml failed on " + new Date(), e);
            }
            return displayMessage;
        }
    }

    public String escapeXml(String str) {
        str = replaceString(str, "&", "&amp;");
        str = replaceString(str, "<", "&lt;");
        str = replaceString(str, ">", "&gt;");
        str = replaceString(str, "\"", "&quot;");
        str = replaceString(str, "'", "&apos;");
        return str;
    }

    public String replaceString(String text, String repl, String with) {
        return replaceStringTo(text, repl, with, -1);
    }

    public String replaceStringTo(String text, String repl, String with, int max) {
        if (text == null) {
            return null;
        }

        StringBuffer buffer = new StringBuffer(text.length());
        int start = 0;
        int end = 0;
        while ((end = text.indexOf(repl, start)) != -1) {
            buffer.append(text.substring(start, end)).append(with);
            start = end + repl.length();
            if (--max == 0) {
                break;
            }
        }
        buffer.append(text.substring(start));
        return buffer.toString();
    }

    private boolean isNotNull(String field) {
        if (null != field && !field.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isMatched(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    private String replaceSpecialChars(String text, String regex) {
        if (null != text) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.replaceAll("");
        } else {
            return null;
        }
    }

    private String osName() {
        return System.getProperty("os.name").toLowerCase();
    }
}
