package com.gp.cong.logisoft.edi.gtnexus;

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
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CreateGtnexusXml {

    private static final Logger log = Logger.getLogger(CreateGtnexusXml.class);

    public String writeXml(String fileNo, String action, HttpServletRequest request) throws Exception {
        EdiDAO ediDAO = new EdiDAO();
        LogFileWriter logFileWriter = new LogFileWriter();
        EdiTrackingBC ediTrackingBC = new EdiTrackingBC();
        String unlocRegex = "[\\W][A-Z]{5}";
        String unctcdRegex = "[^A-Z]";
        String contNoRegex = "[^A-Za-z0-9]";
        String regex3 = "[0-9]";
        String regex4 = "[A-Za-z]";
        String company = "G";
        String messageType = "304";
        String displayMessage = "";
        String errorMessage = "";
        String errorFileName = "";
        String allDestCharges = "";
        String payment = "";
        String chargeType = "";
        String additionalPayment = "";
        String additionalChargeType = "";
        String aes = "";
        String bookingNo = "";
        String routingInstruction = "";
        String shipperName = "";
        String consigneeName = "";
        String notifyPartyName = "";
        String polName = "";
        String polCode = "";
        String porName = "";
        String porCode = "";
        String podName = "";
        String podCode = "";
        String plodName = "";
        String plodCode = "";
        String hazardous = "";
        String voyageNo = "";
        String vesselName = "";
        String comnts = "";
        String path = "";
        String moveType = "";
        String moveTypeMethod = "";
        String serviceTypeMethod = "";
        String docIdentifier = "";
        String exporterref1 = "";
        String submitterNo = "";
        String issTerm = "";
        String submitterPhone = "";
        String submitterFax = "";
        String contractNumber = "";
        String scac = "";
        String carrierScac = "";
        String userEmail = "";
        String userName = "";
        String filename = "";
        String equipPrefix = "";
        String equipNo = "";
        String exportReference = "";
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date dt = new Date();
        String currentDate = dateFormat.format(dt);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        boolean xmlcreated = false;
        boolean hasWeight = false;
        boolean hasPackage = true;
        boolean hasContainer = false;
        OutputStreamWriter out = null;
        String curdate = sdf.format(dt);
        List<String> exporterrefList = new ArrayList<String>();
        List<String> shipperAddressList = new ArrayList<String>();
        List<String> consigAddressList = new ArrayList<String>();
        List<String> notifyPartyAddressList = new ArrayList<String>();
        List<String> contNoList = new ArrayList<String>();
        List<String> equipmentTypeList = new ArrayList<String>();
        List<String> quantLineList = new ArrayList<String>();
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
        List<String> equipPrefixList = new ArrayList<String>();
        List<String> equipNoList = new ArrayList<String>();
        Map<String, List> hazmatMap = new HashMap<String, List>();

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

        try {
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
            FclBl fclbl = new FclBlDAO().getOriginalBl(fileNo);
            HelperClass helperClass = new HelperClass();
            String excludeCharcter = LoadLogisoftProperties.getProperty("edi.exclude.character");
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(null != fileNo ? fileNo.indexOf("-") > -1 ? fileNo.substring(0, fileNo.indexOf("-")) : fileNo : "0");
            List fclInbondList = new FclInbondDetailsDAO().findByProperty("bolId", fclbl.getBol());
            if (isNotNull(fclbl.getLineMove()) && !"00".equals(fclbl.getLineMove())) {
                moveType = ediDAO.getMoveType(fclbl.getLineMove(), "field1");
                moveTypeMethod = ediDAO.getMoveType(fclbl.getLineMove(), "field2");
                serviceTypeMethod = ediDAO.getMoveType(fclbl.getLineMove(), "field3");
            } else {
                displayMessage = displayMessage + "--> Please Select LineMove<br>";
            }
            docIdentifier = "04" + fileNo;
            exporterref1 = "04-" + fileNo;
            String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName").toUpperCase() : "";
            filename = "304_" + companyName + "_GTN_" + docIdentifier + "_" + currentDate + ".xml";
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
            if (isNotNull(fclbl.getTerminal()) && fclbl.getTerminal().contains("/")) {
                String[] location = fclbl.getTerminal().split("/");
                if (fclbl.getTerminal().lastIndexOf("(") != -1 && fclbl.getTerminal().lastIndexOf(")") != -1) {
                    porCode = fclbl.getTerminal().substring(fclbl.getTerminal().lastIndexOf("(") + 1, fclbl.getTerminal().lastIndexOf(")"));;
                }
//                porCode = replaceSpecialChars(isMatched(fclbl.getTerminal(), unlocRegex), unctcdRegex);
                porName = location[0];
                if (porName.length() > 24) {
                    porName = porName.substring(0, 24);
                }
            }
            if (moveType.equals("01") || moveType.equals("02") || moveType.equals("05")
                    || moveType.equals("06") || moveType.equals("07") || moveType.equals("09")) {
                if (!isNotNull(porCode)) {
                    displayMessage = displayMessage + "--> Please Enter Place Of Receipt<br>";
                }
            }
            if (isNotNull(fclbl.getPortOfLoading()) && fclbl.getPortOfLoading().contains("/")) {
                String[] location = fclbl.getPortOfLoading().split("/");
                if (fclbl.getPortOfLoading().lastIndexOf("(") != -1 && fclbl.getPortOfLoading().lastIndexOf(")") != -1) {
                    polCode = fclbl.getPortOfLoading().substring(fclbl.getPortOfLoading().lastIndexOf("(") + 1, fclbl.getPortOfLoading().lastIndexOf(")"));
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
            if (moveType.equals("01") || moveType.equals("03") || moveType.equals("06")
                    || moveType.equals("07") || moveType.equals("08") || moveType.equals("09")) {
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
            if (isNotNull(fclbl.getBookingNo())) {
                bookingNo = fclbl.getBookingNo();
            } else {
                errorMessage = errorMessage + "--> Booking Number(BOOKN) is invalid<br>";
            }
            if (isNotNull(fclbl.getExportReference())) {
                exporterrefList = helperClass.splitString(fclbl.getExportReference(), 30);
            }
            hazardous = null != bookingFcl ? bookingFcl.getHazmat() : "";
            if ("Y".equalsIgnoreCase(hazardous)) {
                hazardous = "true";
            } else {
                hazardous = "false";
            }

            issTerm = fclbl.getBillingTerminal();
            if (isNotNull(issTerm)) {
                if (issTerm.contains(",")) {
                    String[] str = issTerm.split(",");
                    submitterNo = str[1].trim().substring(str[1].lastIndexOf("-"));
                    userEmail = ediDAO.getUserEmail(submitterNo);
                    if (userEmail != null && !userEmail.equals("") && userEmail.contains("@")) {
                        int indexname = userEmail.indexOf("@");
                        userName = userEmail.substring(0, indexname);
                    }
                    RefTerminal terminal = ediDAO.findByTerminal(submitterNo);
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
            List fclBlContainerList = new FclBlContainerDAO().getAllContainers(fclbl.getBol().toString());
            for (Object object : fclBlContainerList) {
                FclBlContainer fclBlContainer = (FclBlContainer) object;
                String contNo = "";
                if (!"D".equalsIgnoreCase(fclBlContainer.getDisabledFlag())) {
                    hasContainer = true;
                    if (isNotNull(fclBlContainer.getTrailerNo())) {
                        contNo = replaceSpecialChars(fclBlContainer.getTrailerNo(), contNoRegex);
                        equipPrefix = contNo.substring(0, 4);
                        if (!isAlpha(contNo.substring(0, 4), regex3)) {
                            equipPrefix = contNo.substring(0, 4);
                        } else {
                            errorMessage = "Equipment prefix should contains 4 Characters";
                        }
                        if (!isNumeric(contNo.substring(4, contNo.length()), regex4)) {
                            equipNo = contNo.substring(4, contNo.length());
                        } else {
                            errorMessage = "Equipment Number should contains 6 0r 7 digit Numeric";
                        }
                        equipPrefixList.add(equipPrefix);
                        equipNoList.add(equipNo);
                        if (contNo.length() >= 11) {
                            contNoList.add(contNo);
                        } else {
                            displayMessage = displayMessage + "--> Container Number 'AAAA-NNNNNN-N' in format <br>";
                        }
                        sealNoList.add(fclBlContainer.getSealNo());
                        equipmentTypeList.add(ediDAO.getEquipmantType("" + fclBlContainer.getSizeLegend().getId(), "field5"));
                        quantLineList.add(ediDAO.getEquipmantType("" + fclBlContainer.getSizeLegend().getId(), "field2"));
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
                                FclBlMarks fclBlMarks = (FclBlMarks) object1;
                                String stc = "";
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
                                if (CommonFunctions.isNotNull(fclBlMarks.getUom())) {
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
                                if (CommonFunctions.isNotNull(fclBlMarks.getCopyDescription())) {
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
            String shipmentId = "304_" + companyName + "_GTN_" + docIdentifier;
            File file = null;
            if (osName().contains("linux")) {
                file = new File(prop.getProperty("linuxGtnexusXmlOut"));
                path = prop.getProperty("linuxGtnexusXmlOut") + filename;
            } else {
                file = new File(prop.getProperty("gtnexusXmlOut"));
                path = prop.getProperty("gtnexusXmlOut") + filename;
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fout = new FileOutputStream(path);
            BufferedOutputStream bout = new BufferedOutputStream(fout);
            out = new OutputStreamWriter(bout, "8859_1");
            xmlcreated = true;

            out.write("<?xml version=\"1.0\" ");
            out.write("encoding=\"ISO-8859-1\"?>");
            out.write("<BlMessage>");
            out.write("<TransactionInfo AcknowledgementRequested='true'>");
            out.write("<MessageSender>");
            out.write(null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "");
            out.write("</MessageSender>");
            out.write("<MessageRecipient>");
            out.write("GTNEXUS");
            out.write("</MessageRecipient>");
            out.write("<MessageID>");
            out.write(fileNo);
            out.write("</MessageID>");
            out.write("<Created>");
            out.write(curdate);
            out.write("</Created>");
            out.write("<FileName>");
            out.write(filename);
            out.write("</FileName>");
            out.write("</TransactionInfo>");
            if (!isNotNull(scac)) {
                errorMessage = errorMessage + "--> Carrier Scac Code(SSLINE) is not matching<br>";
            } else if (scac.length() < 2 || scac.length() > 4) {
                errorMessage = errorMessage + "--> Carrier Scac Code(SSLINE) length must be between 2 & 10<br>";
            }
            out.write("<BL Sequence='1' BillType='Express'  CarrierScac='" + scac + "' ServiceType='" + serviceTypeMethod + "' MoveType='" + moveTypeMethod + "' ShipmentId='" + shipmentId + "' Payment='" + payment + "' HazardousShipment='" + hazardous + "' >");
            out.write("<References>");
            if (isNotNull(bookingNo) && bookingNo.length() > 30) {
                errorMessage = errorMessage + "--> Booking Number(BOOKN) length is more than 30 characters<br>";
            }
            out.write("<Reference referenceType='BookingNumber'>");
            out.write(escapeXml(bookingNo));
            out.write("</Reference>");
            if (isNotNull(contractNumber)) {
                if (contractNumber.length() > 30) {
                    errorMessage = errorMessage + "-->Contract Number length is more than 30 characters<br>";
                }
                out.write("<Reference referenceType='ContractNumber'>");
                out.write(escapeXml(contractNumber));
                out.write("</Reference>");
            }
            if (isNotNull(exporterref1)) {
                if (exporterref1.length() > 30) {
                    errorMessage = errorMessage + "--> ExportReferenceNumber length is more than 30 characters<br>";
                }
                String companyCode = null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "";
                out.write("<Reference referenceType='ExportReferenceNumber'>");
                out.write(companyCode + " Ref " + escapeXml(exporterref1));
                out.write("</Reference>");
            }

            if (CommonUtils.isNotEmpty(exporterrefList)) {
                for (String string : exporterrefList) {
                    out.write("<Reference referenceType='ExportReferenceNumber'>");
                    out.write(escapeXml(string));
                    out.write("</Reference>");
                }
            }
            if (CommonUtils.isNotEmpty(fclInbondList)) {
                for (Object object : fclInbondList) {
                    FclInbondDetails fclInbondDetails = (FclInbondDetails) object;
                    if (CommonUtils.isNotEmpty(fclInbondDetails.getInbondNumber())) {
                        out.write("<Reference referenceType='ExportReferenceNumber'>");
                        out.write(escapeXml(fclInbondDetails.getInbondNumber()));
                        out.write("</Reference>");
                    }
                }
            }
            out.write("</References>");
            out.write("<Voyage>");
            if (voyageNo.trim().length() < 2 || voyageNo.trim().length() > 10) {
                errorMessage = errorMessage + "--> Voyage Number(voyages) length must be between 2 & 10<br>";
            }
            out.write("<VoyageNumber>");
            out.write(escapeXml(voyageNo.trim()));
            out.write("</VoyageNumber>");
            out.write("<Vessel>");
            out.write("<Name>");
            out.write(escapeXml(vesselName.trim()));
            out.write("</Name>");
            out.write("</Vessel>");
            out.write("</Voyage>");
            out.write("<Parties>");
            out.write("<Party Type='Shipper'>");
            if (isNotNull(shipperName)) {
                if (shipperName.length() > 35) {
                    errorMessage = errorMessage + "--> Master Shipper Name length must be less than 35 characters<br>";
                }
                out.write("<Name>");
                out.write(escapeXml(shipperName));
                out.write("</Name>");
                int count = 0;
                if (CommonUtils.isNotEmpty(shipperAddressList)) {
                    out.write("<Address>");
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
                    out.write("</Address>");
                }
            }
            out.write("</Party>");
            out.write("<Party Type='Consignee'>");
            if (isNotNull(consigneeName)) {
                if (consigneeName.length() > 35) {
                    errorMessage = errorMessage + "--> Master Consignee Name length must be less than 35 characters<br>";
                }
                out.write("<Name>");
                out.write(escapeXml(consigneeName));
                out.write("</Name>");
                int count = 0;
                if (CommonUtils.isNotEmpty(consigAddressList)) {
                    out.write("<Address>");
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
                    out.write("</Address>");
                }
            }
            out.write("</Party>");
            if (isNotNull(notifyPartyName)) {
                out.write("<Party Type='Notify'>");
                if (notifyPartyName.length() > 35) {
                    errorMessage = errorMessage + "--> Master NotifyParty Name length must be less than 35 characters<br>";
                }
                out.write("<Name>");
                out.write(escapeXml(notifyPartyName));
                out.write("</Name>");
                int count = 0;

                if (CommonUtils.isNotEmpty(notifyPartyAddressList)) {
                    out.write("<Address>");
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
                    out.write("</Address>");
                }
                out.write("</Party>");
            }
            if (isNotNull(exportReference)) {
                if (exportReference.length() > 30) {
                    errorMessage = errorMessage + "--> Export Reference length must be less than 30 characters<br>";
                }
            }
            if (!isNotNull(submitterNo)) {
                displayMessage = displayMessage + "-->Please select Issuing Terminal<br>";
            }
            out.write("<Party Type='Submitter' Code='" + submitterNo + "'>");
            out.write("<Name>");
            out.write(null != LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.partnerName").toUpperCase() : "");
            out.write("</Name>");
            if (CommonUtils.isNotEmpty(subAddressList)) {
                out.write("<Address>");
                for (String address : subAddressList) {
                    String escapedAddress = escapeXml(address).replaceAll("\\p{Cntrl}", "");
                    if (CommonUtils.isNotEmpty(escapedAddress)) {
                        out.write("<AddressLine>");
                        out.write(escapedAddress);
                        out.write("</AddressLine>");
                    }
                }
                out.write("</Address>");
            }
            if (isNotNull(userName)) {
                if (isNotNull(submitterPhone) && isNotNull(submitterFax)) {
                    out.write("<Contact Type='Information' Phone='" + submitterPhone + "' Fax='" + submitterFax + "' EMail='" + userEmail + "'>");
                    out.write(userName);
                    out.write("</Contact>");
                } else if (isNotNull(submitterFax)) {
                    out.write("<Contact Type='Information'  Fax='" + submitterFax + "' EMail='" + userEmail + "'>");
                    out.write(userName);
                    out.write("</Contact>");
                } else if (isNotNull(submitterPhone)) {
                    out.write("<Contact Type='Information' Phone='" + submitterPhone + "' EMail='" + userEmail + "'>");
                    out.write(userName);
                    out.write("</Contact>");
                } else {
                    out.write("<Contact Type='Information'  EMail='" + userEmail + "'>");
                    out.write(userName);
                    out.write("</Contact>");
                }
            }
            out.write("</Party>");
            out.write("</Parties>");
            out.write("<Locations>");
            if (isNotNull(porCode)) {
                out.write("<Location Function='ContractualPlaceOfReceipt' Qualifier='UNLOCODE'  Identifier='" + porCode + "'>");
                if (isNotNull(porName)) {
                    out.write("<Name>");
                    out.write(porName);
                    out.write("</Name>");
                }
                out.write("</Location>");
            }
            out.write("<Location Function='OperationalPortOfLoading' Qualifier='UNLOCODE'  Identifier='" + polCode + "'>");
            if (isNotNull(polName)) {
                out.write("<Name>");
                out.write(polName);
                out.write("</Name>");
            }
            out.write("</Location>");
            out.write("<Location Function='OperationalPortOfDischarge' Qualifier='UNLOCODE'  Identifier='" + podCode + "'>");
            if (isNotNull(podName)) {
                out.write("<Name>");
                out.write(podName);
                out.write("</Name>");
            }
            out.write("</Location>");
            if (isNotNull(plodCode)) {
                out.write("<Location Function='ContractualPlaceOfDelivery' Qualifier='UNLOCODE'  Identifier='" + plodCode + "' >");
                if (isNotNull(plodName)) {
                    out.write("<Name>");
                    out.write(plodName);
                    out.write("</Name>");
                }
            }
            out.write("</Location>");
            out.write("</Locations>");
//            for (int d = 0; d < comntsList.size(); d++) {
//                if (isNotNull(comntsList.get(d).toString())) {
//                    out.write("<Comment>");
//                    out.write(escapeXml(comntsList.get(d).toString().trim()));
//                    out.write("</Comment>");
//                }
//            }
            if (isNotNull(chargeType)) {
                out.write("<Comment >");
                out.write(chargeType + " " + payment);
                out.write("</Comment>");
            }
            if (isNotNull(additionalChargeType)) {
                out.write("<Comment >");
                out.write(additionalChargeType + " " + additionalPayment);
                out.write("</Comment>");
            }
            List aesList = new FclInbondDetailsDAO().findAesdetails("fileNo", fclbl.getFileNo());
            if (CommonUtils.isNotEmpty(aesList)) {
                for (Object object : aesList) {
                    FclAESDetails fclAESDetails = (FclAESDetails) object;
                    if (isNotNull(fclAESDetails.getAesDetails())) {
                        out.write("<Clause Type='US SED Flag'>");
                        out.write(escapeXml(fclAESDetails.getAesDetails()));
                        out.write("</Clause>");
                    }
                    if (isNotNull(fclAESDetails.getException())) {
                        out.write("<Clause Type='US SED Flag'>");
                        out.write(escapeXml(fclAESDetails.getException()));
                        out.write("</Clause>");
                    }
                }
            } else {
                if (!"I".equalsIgnoreCase(fclbl.getImportFlag())) {
                    displayMessage = displayMessage + "--> Please Enter AES/ITN details <br>";
                }
            }
            if (isNotNull(routingInstruction)) {
                out.write("<Clause Type='Route'>");
                out.write(escapeXml(routingInstruction));
                out.write("</Clause>");
            }
            if (isNotNull(allDestCharges)) {
                out.write("<Clause Type='Route'>");
                out.write("All Destination Charges " + allDestCharges);
                out.write("</Clause>");
            }

            out.write("<ContainerGroups>");
            out.write("<ContainerGroup>");
            for (int x = 0; x < contNoList.size(); x++) {
                String eqno = "";
                String container = contNoList.get(x);
                if (CommonUtils.isNotEmpty(container) && container.length() == 11) {
                    container = container.substring(0, 4) + "-" + container.substring(4, 10) + "-" + container.substring(10, 11);
                }
                eqno = equipNoList.get(x).toString();
                if (eqno.length() < 1 || eqno.length() > 10) {
                    errorMessage = errorMessage + "--> Equipment Number length must be between 1 & 10<br>";
                }
                out.write("<Container EquipmentPrefix='" + equipPrefixList.get(x) + "' EquipmentNumber='" + eqno + "'  Type='" + equipmentTypeList.get(x) + "'>");
                if (CommonUtils.isNotEmpty(sealNoList)) {
                    String slno = "";
                    slno = sealNoList.get(x).toString();
                    if (isNotNull(sealNoList.get(x).toString())) {
                        if (slno.length() < 2 || slno.length() > 15) {
                            errorMessage = errorMessage + "--> Seal Number length must be, between 2 & 15<br>";
                        }
                        out.write("<SealNumber>");
                        out.write(escapeXml(slno));
                        out.write("</SealNumber>");
                    } else {
                        displayMessage = displayMessage + "--> Please Enter Seal Number for container " + container + "<br>";
                    }
                }
                out.write("</Container>");
            }
            for (int w = 0; w < contNoList.size(); w++) {
                boolean weightBoolean = false;
                String weightType = "";
                String containerNo = contNoList.get(w).toString();
                if (CommonUtils.isNotEmpty(containerNo) && containerNo.length() == 11) {
                    containerNo = containerNo.substring(0, 4) + "-" + containerNo.substring(4, 10) + "-" + containerNo.substring(10, 11);
                }
                quantityList = quantityMap.get(contNoList.get(w));
                weightKGSList = weightKGSMap.get(contNoList.get(w));
                weightLBSList = weightLBSMap.get(contNoList.get(w));
                volumeCFTList = volumeCFTMap.get(contNoList.get(w));
                volumeCBMList = volumeCBMMap.get(contNoList.get(w));
                packageFormList = packageFormMap.get(contNoList.get(w));
                copyDescList = copyDescMap.get(contNoList.get(w));
                stdRemarksList = stdRemarksMap.get(contNoList.get(w));
                descriptionsList = descriptionsMap.get(contNoList.get(w));
                List hazmatMaterialList = hazmatMap.get(contNoList.get(w));
                if (CommonUtils.isNotEmpty(quantityList)) {
                    for (int i = 0; i < quantityList.size(); i++) {
                        String quantity = quantityList.get(i).toString();
                        int noOfPack = 0;
                        if (isNotNull(quantity)) {
                            noOfPack = Integer.parseInt(quantity);
                        }
                        out.write("<Commodity  Quantity='" + String.valueOf(noOfPack) + "' PackagingForm='" + packageFormList.get(i).trim() + "'>");
                        if (weightKGSList != null && weightKGSList.size() > i) {
                            String strWeight = weightKGSList.get(i);
                            if (isNotNull(strWeight)) {
                                float weight = Float.parseFloat(strWeight);
                                if (weight != 0) {
                                    out.write("<Weight Qualifier='Gross'  Units='Kilograms'>");
                                    out.write(String.valueOf(weight));
                                    out.write("</Weight>");
                                    weightBoolean = true;
                                    weightType = "KGS";
                                }
                            }
                        }
                        if (weightLBSList != null && weightLBSList.size() > i) {
                            String strWeight = weightLBSList.get(i);
                            if (isNotNull(strWeight)) {
                                float weight = Float.parseFloat(strWeight);
                                if (weight != 0 && !weightBoolean) {
                                    out.write("<Weight Qualifier='Gross' Units='Pounds'>");
                                    out.write(String.valueOf(weight));
                                    out.write("</Weight>");
                                    weightBoolean = true;
                                    weightType = "LBS";
                                }
                            }
                        }
                        if (!weightBoolean && hasPackage) {
                            displayMessage = displayMessage + "--> Weights are not entered for container " + containerNo + "<br>";
                        }
                        boolean volumeBoolean = false;
                        if (volumeCFTList != null && volumeCFTList.size() > i && !weightType.equals("KGS")) {
                            String strVolume = volumeCFTList.get(i);
                            if (isNotNull(strVolume)) {
                                float volume = Float.parseFloat(strVolume);
                                if (volume != 0) {
                                    out.write("<Volume Units='CubicFeet'>");
                                    out.write(String.valueOf(volume));
                                    out.write("</Volume>");
                                    volumeBoolean = true;
                                }
                            }
                        }
                        if (volumeCBMList != null && volumeCBMList.size() > i && !weightType.equals("LBS")) {
                            String strVolume = volumeCBMList.get(i);
                            if (isNotNull(strVolume)) {
                                float volume = Float.parseFloat(strVolume);
                                if (volume != 0 && !volumeBoolean) {
                                    out.write("<Volume  Units='CubicMeters'>");
                                    out.write(String.valueOf(volume));
                                    out.write("</Volume>");
                                }
                            }
                        }
                        if (CommonUtils.isNotEmpty(copyDescList)) {
                            if ("N".equalsIgnoreCase(copyDescList.get(i))) {
                                List<String> stdRemarkList = (List<String>) new ArrayList();
                                if (!stdRemarksList.isEmpty() && stdRemarksList.size() > i && CommonUtils.isExcludeEdiCharacter(stdRemarksList.get(i), excludeCharcter)) {
                                    errorMessage = errorMessage + "-->Please Remove the following Special Characters " + excludeCharcter + " from Streamship Description for Container " + containerNo;
                                } else if (!stdRemarksList.isEmpty() && stdRemarksList.size() > i) {
                                    stdRemarkList = helperClass.wrapAddress(stdRemarksList.get(i).replaceAll("[" + excludeCharcter + "]", ""));
                                }
                                if (CommonUtils.isEmpty(stdRemarkList)) {
                                    displayMessage = displayMessage + "-->Column Use House Bl is 'N' and Streamship Description is not entered for container " + containerNo + "<br>";
                                }
                                for (int j = 0; j < stdRemarkList.size(); j++) {
                                    if (CommonUtils.isNotEmpty(stdRemarkList.get(j).toString())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(stdRemarkList.get(j).toString()));
                                        out.write("</DescriptionLine>");
                                    }
                                }
                            } else if ("Y".equalsIgnoreCase(copyDescList.get(i))) {
                                List<String> descList = (List<String>) new ArrayList();
                                if (!descriptionsList.isEmpty() && descriptionsList.size() > i && CommonUtils.isExcludeEdiCharacter(descriptionsList.get(i), excludeCharcter)) {
                                    errorMessage = errorMessage + "-->Please Remove the following Special Characters " + excludeCharcter + " from GoodsDescription for Container " + containerNo;
                                } else if (!descriptionsList.isEmpty() && descriptionsList.size() > i) {
                                    descList = helperClass.wrapAddress(descriptionsList.get(i).replaceAll("[" + excludeCharcter + "]", ""));
                                }
                                if (CommonUtils.isEmpty(descList)) {
                                    displayMessage = displayMessage + "-->Column Use House Bl is 'Y' and GoodsDescription is not entered for container " + containerNo + "<br>";
                                }
                                for (int j = 0; j < descList.size(); j++) {
                                    if (CommonUtils.isNotEmpty(descList.get(j).toString())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(descList.get(j).toString()));
                                        out.write("</DescriptionLine>");
                                    }
                                }
                            }
                        }
                        //Hazmat Starts here
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
                                    List hazmatList = new ArrayList();
                                    hazmatList.addAll(CommonUtils.splitForContainerString(buffer.toString(), ".{0,48}(?:\\S(?:-| |$)|$)"));
                                    for (Object hazmatList1 : hazmatList) {
                                        if (isNotNull(hazmatList1.toString())) {
                                            out.write("<DescriptionLine>");
                                            out.write(escapeXml(hazmatList1.toString()));
                                            out.write("</DescriptionLine>");
                                        }
                                    }
                                } else {
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine1())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(hazmatMaterial.getLine1()));
                                        out.write("</DescriptionLine>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine2())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(hazmatMaterial.getLine2()));
                                        out.write("</DescriptionLine>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine3())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(hazmatMaterial.getLine3()));
                                        out.write("</DescriptionLine>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine4())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(hazmatMaterial.getLine4()));
                                        out.write("</DescriptionLine>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine5())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(hazmatMaterial.getLine5()));
                                        out.write("</DescriptionLine>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine6())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(hazmatMaterial.getLine6()));
                                        out.write("</DescriptionLine>");
                                    }
                                    if (CommonUtils.isNotEmpty(hazmatMaterial.getLine7())) {
                                        out.write("<DescriptionLine>");
                                        out.write(escapeXml(hazmatMaterial.getLine7()));
                                        out.write("</DescriptionLine>");
                                    }
                                }

                            }
                        }
                        //Hazmat ends here
                        List<String> markList = (List<String>) marksMap.get(contNoList.get(w));
                        if (CommonUtils.isNotEmpty(markList)) {
                            for (String markList1 : markList) {
                                out.write("<MarksAndNumbersLine>");
                                out.write(escapeXml(markList1));
                                out.write("</MarksAndNumbersLine>");
                            }
                        } else {
                            out.write("<MarksAndNumbersLine>");
                            out.write(escapeXml(contNoList.get(w)));
                            out.write("</MarksAndNumbersLine>");
                            if (CommonUtils.isNotEmpty(sealNoList)) {
                                String slno = sealNoList.get(w);
                                if (isNotNull(sealNoList.get(w))) {
                                    out.write("<MarksAndNumbersLine>");
                                    out.write(escapeXml("SEAL: " + slno));
                                    out.write("</MarksAndNumbersLine>");
                                }
                            }
                        }
                        if (CommonUtils.isNotEmpty(quantLineList)) {
                            String quantLn = "";
                            quantLn = quantLineList.get(w);
                            if (quantLn.length() > 8) {
                                errorMessage = errorMessage + "Commodity QuantityLine length must be min 1 & max 8";
                            }
                            out.write("<QuantityLine>");
                            out.write(escapeXml(quantLn));
                            out.write("</QuantityLine>");
                        }
                        String cntNo = "";
                        cntNo = contNoList.get(w);
                        if (cntNo.length() > 14) {
                            errorMessage = errorMessage + "Commodity ContainerNumber length must be less than 14 ";
                        }
                        out.write("<ContainerNumber>");
                        out.write(cntNo);
                        out.write("</ContainerNumber>");
                        out.write("</Commodity>");
                    }
                }
            }
            out.write("</ContainerGroup>");
            out.write("</ContainerGroups>");
            if (isNotNull(fclbl.getFclBLClause()) && fclbl.getFclBLClause().equals("2")) {
                out.write("<Paperwork Type='Original' Rated='true' Quantity='1'/>");
            } else {
                out.write("<Paperwork Type='SeaWaybill' Rated='true' Quantity='1'/>");
            }
            out.write("</BL>");
            out.write("</BlMessage>");
            out.flush();  // Don't forget to flush!
            out.close();
            if (isNotNull(errorMessage)) {
                errorFileName = "error_logfile_" + filename + "_" + currentDate + ".txt";
                if (!"validate".equalsIgnoreCase(action)) {
                    ediTrackingBC.setEdiLog(filename, currentDate, "failure", errorMessage, company, messageType, fclbl.getFileNo(), fclbl.getBookingNo(), "", null);
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
                    ediTrackingBC.setEdiLog(filename, currentDate, "success", "No Error", company, messageType, fclbl.getFileNo(), fclbl.getBookingNo(), "", null);
                }
                return "XML generated successfully";
            }
        } catch (Exception e) {
            log.info("writeXml failed on " + new Date(), e);
            if (!"validate".equalsIgnoreCase(action)) {
                errorFileName = "error_logfile_" + filename + "_" + currentDate + ".txt";
                ediTrackingBC.setEdiLog(filename, currentDate, "failure", displayMessage, company, messageType, fileNo, "", "", null);
                logFileWriter.doAppend(errorMessage.replaceAll("<br>", "\n"), errorFileName, company, osName(), messageType);
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
            return errorMessage;
        }
    }

    public String escapeXml(String str) {
        str = replaceString(str, "&", "&amp;");
        str = replaceString(str, "<", "&lt;");
        str = replaceString(str, ">", "&gt;");
        str = replaceString(str, "\"", "&quot;");
        str = replaceString(str, "'", "&apos;");
//        str = replaceString(str, "+", "?+");
//        str = replaceString(str, "/", "?/");
//        str = replaceString(str, "~", "?~");
        return str.replaceAll("\\p{Cntrl}","").replace("\\r", "");
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

    private String isMatched(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
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

    private static boolean isAlpha(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
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

    private String osName() {
        return System.getProperty("os.name").toLowerCase();
    }
}
