package com.gp.cong.logisoft.edi.gtnexus;

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
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ValidateGtnexusXml {

    public String writeXml(String fileNo, String action, HttpServletRequest request) {
        EdiDAO ediDAO = new EdiDAO();
        String contNoRegex = "[^A-Za-z0-9]";
        String regex3 = "[0-9]";
        String regex4 = "[A-Za-z]";
        String displayMessage = "";
        String errorMessage = "";
        String vesselName = "";
        String allDestCharges = "";
        String bookingNo = "";
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
        String comnts = "";
        String moveType = "";
        String docIdentifier = "";
        String exporterref1 = "";
        String submitterNo = "";
        String issTerm = "";
        String contractNumber = "";
        String scac = "";
        String carrierScac = "";
        String userEmail = "";
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
            HelperClass helperClass = new HelperClass();
            FclBl fclbl = new FclBlDAO().getOriginalBl(fileNo);
            String excludeCharcter = LoadLogisoftProperties.getProperty("edi.exclude.character");
            BookingFcl bookingFcl = new BookingFclDAO().getFileNoObject(null != fileNo ? fileNo.indexOf("-") > -1 ? fileNo.substring(0, fileNo.indexOf("-")) : fileNo : "0");
            Quotation quotation = new QuotationDAO().getFileNoObject(null != fileNo ? fileNo.indexOf("-") > -1 ? fileNo.substring(0, fileNo.indexOf("-")) : fileNo : "0");
            List fclInbondList = new FclInbondDetailsDAO().findByProperty("bolId", fclbl.getBol());
            if (isNotNull(fclbl.getLineMove())) {
                moveType = ediDAO.getMoveType(fclbl.getLineMove(), "field1");
            }
            docIdentifier = "04" + fileNo;
            exporterref1 = "04-" + fileNo;
            String companyName = null != LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.companyName").toUpperCase() : "";
            if (isNotNull(fclbl.getDestinationChargesPreCol())) {
                if (fclbl.getDestinationChargesPreCol().substring(0, 1).equals("P")) {
                    allDestCharges = "Prepaid";
                } else if (fclbl.getDestinationChargesPreCol().substring(0, 1).equals("C")) {
                    allDestCharges = "Collect";
                }
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
            if (isNotNull(fclbl.getLineMove()) && !"00".equals(fclbl.getLineMove())) {
            } else {
                displayMessage = displayMessage + "--> Please Select LineMove<br>";
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
            
            if (CommonUtils.isEmpty(fclbl.getStreamShipBl()) && CommonUtils.isNotEqualIgnoreCase(fclbl.getImportFlag(), "I")) {
                displayMessage = displayMessage + "Please choose SSL BL Prepaid/Collect<br>";
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
            comnts = null != quotation ? quotation.getComment1() : "";
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
                        equipmentTypeList.add(ediDAO.getEquipmantType("" + fclBlContainer.getSizeLegend().getId(), "field1"));
                        quantLineList.add(ediDAO.getEquipmantType("" + fclBlContainer.getSizeLegend().getId(), "field2"));
                        if (isNotNull(fclBlContainer.getMarks())) {
                            if (fclBlContainer.getMarks().contains("?")) {
                                errorMessage = "-->Question marks must be removed from Description of Goods before submitting EDI For Container : ".concat(contNo).concat("<br>");
                            }
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
            if (!isNotNull(scac)) {
                errorMessage = errorMessage + "--> Carrier Scac Code(SSLINE) is not matching<br>";
            } else if (scac.length() < 2 || scac.length() > 4) {
                errorMessage = errorMessage + "--> Carrier Scac Code(SSLINE) length must be between 2 & 10<br>";
            }
            if (isNotNull(bookingNo) && bookingNo.length() > 30) {
                errorMessage = errorMessage + "--> Booking Number(BOOKN) length is more than 30 characters<br>";
            }
            if (isNotNull(contractNumber)) {
                if (contractNumber.length() > 30) {
                    errorMessage = errorMessage + "-->Contract Number length is more than 30 characters<br>";
                }
            }
            if (isNotNull(exporterref1)) {
                if (exporterref1.length() > 30) {
                    errorMessage = errorMessage + "--> ExportReferenceNumber length is more than 30 characters<br>";
                }
            }
            if (voyageNo.trim().length() < 2 || voyageNo.trim().length() > 10) {
                errorMessage = errorMessage + "--> Voyage Number(voyages) length must be between 2 & 10<br>";
            }
            if (isNotNull(shipperName)) {
                if (shipperName.length() > 35) {
                    errorMessage = errorMessage + "--> Master Shipper Name length must be less than 35 characters<br>";
                }
                int count = 0;
                if (CommonUtils.isNotEmpty(shipperAddressList)) {
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
                    }
                }
            }
            if (isNotNull(consigneeName)) {
                if (consigneeName.length() > 35) {
                    errorMessage = errorMessage + "--> Master Consignee Name length must be less than 35 characters<br>";
                }
                int count = 0;
                if (CommonUtils.isNotEmpty(consigAddressList)) {
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
                    }
                }
            }
            if (isNotNull(notifyPartyName)) {
                if (notifyPartyName.length() > 35) {
                    errorMessage = errorMessage + "--> Master NotifyParty Name length must be less than 35 characters<br>";
                }
                int count = 0;
                if (CommonUtils.isNotEmpty(notifyPartyAddressList)) {
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
                    }
                }
            }
            if (isNotNull(exportReference)) {
                if (exportReference.length() > 30) {
                    errorMessage = errorMessage + "--> Export Reference length must be less than 30 characters<br>";
                }
            }
            if (!isNotNull(submitterNo)) {
                displayMessage = displayMessage + "-->Please select Issuing Terminal<br>";
            }
            List aesList = new FclInbondDetailsDAO().findAesdetails("fileNo", fclbl.getFileNo());
            if (CommonUtils.isEmpty(aesList)) {
                if (!"I".equalsIgnoreCase(fclbl.getImportFlag())) {
                    displayMessage = displayMessage + "--> Please Enter AES/ITN details <br>";
                }
            }
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
                if (CommonUtils.isNotEmpty(sealNoList)) {
                    String slno = "";
                    slno = sealNoList.get(x).toString();
                    if (isNotNull(sealNoList.get(x).toString())) {
                        if (slno.length() < 2 || slno.length() > 15) {
                            errorMessage = errorMessage + "--> Seal Number length must be, between 2 & 15<br>";
                        }
                    } else {
                        displayMessage = displayMessage + "--> Please Enter Seal Number for container " + container + "<br>";
                    }
                }
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
                if (CommonUtils.isNotEmpty(quantityList)) {
                    for (int i = 0; i < quantityList.size(); i++) {
                        String quantity = quantityList.get(i).toString();
                        if (weightKGSList != null && weightKGSList.size() > i) {
                            String strWeight = weightKGSList.get(i);
                            if (isNotNull(strWeight)) {
                                float weight = Float.parseFloat(strWeight);
                                if (weight != 0) {
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
                                    volumeBoolean = true;
                                }
                            }
                        }
                        if (CommonUtils.isNotEmpty(copyDescList)) {
                            if ("N".equalsIgnoreCase(copyDescList.get(i))) {
                                List<String> stdRemarkList = (List<String>) new ArrayList();
                                if (!stdRemarksList.isEmpty() && stdRemarksList.size() > i && CommonUtils.isExcludeEdiCharacter(stdRemarksList.get(i), excludeCharcter)) {
                                    errorMessage = errorMessage + "-->Please Remove the following Special Characters " + excludeCharcter + " from Streamship Description for Container " + containerNo;
                                } else if (!stdRemarksList.isEmpty() && stdRemarksList.size() > i) {
                                    stdRemarkList = helperClass.wrapAddress(stdRemarksList.get(i));
                                }
                                if (CommonUtils.isEmpty(stdRemarkList)) {
                                    displayMessage = displayMessage + "-->Column Use House Bl is 'N' and Streamship Description is not Entered for Container " + containerNo + "<br>";
                                }
                            } else if ("Y".equalsIgnoreCase(copyDescList.get(i))) {
                                List<String> descList = (List<String>) new ArrayList();
                                if (!descriptionsList.isEmpty() && descriptionsList.size() > i && CommonUtils.isExcludeEdiCharacter(descriptionsList.get(i), excludeCharcter)) {
                                    errorMessage = errorMessage + "-->Please Remove the following Special Characters " + excludeCharcter + " from GoodsDescription for Container " + containerNo;
                                } else if (!descriptionsList.isEmpty() && descriptionsList.size() > i) {
                                    descList = helperClass.wrapAddress(descriptionsList.get(i));
                                }
                                if (CommonUtils.isEmpty(descList)) {
                                    displayMessage = displayMessage + "-->Column Use House Bl is 'Y' and GoodsDescription is not entered for container " + containerNo + "<br>";
                                }
                            }
                        }
                        if (CommonUtils.isNotEmpty(quantLineList)) {
                            String quantLn = "";
                            if (null != quantLineList.get(w)) {
                                quantLn = quantLineList.get(w).toString();
                            }
                            if (quantLn.length() > 8) {
                                errorMessage = errorMessage + "Commodity QuantityLine length must be min 1 & max 8";
                            }
                        }
                        String cntNo = "";
                        cntNo = contNoList.get(w).toString();
                        if (cntNo.length() > 14) {
                            errorMessage = errorMessage + "Commodity ContainerNumber length must be less than 14 ";
                        }
                    }
                }
            }

            if (isNotNull(errorMessage)) {
                return "<span color: #000080;font-size: 10px;>Error Message</span><br>" + errorMessage;
            } else if (isNotNull(displayMessage)) {
                return displayMessage;
            } else {
                return "No Error";
            }

        } catch (Exception e) {
            return errorMessage;
        }
    }

    private boolean isNotNull(String field) {
        if (null != field && !field.trim().equals("")) {
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

}
