package com.gp.cong.logisoft.edi.inttra;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.EdiDAO;
import com.gp.cong.logisoft.hibernate.dao.FclInbondDetailsDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.FclBlContainer;
import com.gp.cvst.logisoft.domain.FclBlMarks;
import com.gp.cvst.logisoft.hibernate.dao.FclBlContainerDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
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

public class ValidateInttraXml {

    public String writeXml(String fileNo, String action, HttpServletRequest request) {
        EdiDAO ediDAO = new EdiDAO();
        String contNoregex = "[^A-Za-z0-9]";
        String displayMessage = "";
        String errorMessage = "";
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
        String moveType = "";
        String docIdentifier = "";
        String scac = "";
        String carrierScac = "";
        String userEmail = "";
        String weightType = "";
        String vesselName = "";
        String voyageNo = "";
        String bookingNo = "";
        String exportReference = "";
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("loginuser");
        boolean hasWeight = true;
        boolean hasPackage = true;
        boolean hasContainer = false;
        Date dt = new Date();
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
            docIdentifier = "04" + fileNo;
            moveType = fclbl.getMoveType();
            RoleDuty roleDuty = (RoleDuty) request.getSession().getAttribute("roleDuty");
            if (!ediDAO.validate997(fileNo, "") && !roleDuty.isNo997EdiSubmission()) {
                displayMessage = displayMessage + "--> Cannot send amendment before 997 is received<br>";
            }
            if (isNotNull(fclbl.getTerminal()) && fclbl.getTerminal().contains("/")) {
                String[] location = fclbl.getTerminal().split("/");
                if (fclbl.getTerminal().lastIndexOf("(") != -1 && fclbl.getTerminal().lastIndexOf(")") != -1) {
                    porCode = fclbl.getTerminal().substring(fclbl.getTerminal().lastIndexOf("(") + 1, fclbl.getTerminal().lastIndexOf(")"));
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
                    podCode = fclbl.getPortofDischarge().substring(fclbl.getPortofDischarge().lastIndexOf("(") + 1, fclbl.getPortofDischarge().lastIndexOf(")"));
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
                    plodCode = fclbl.getFinalDestination().substring(fclbl.getFinalDestination().lastIndexOf("(") + 1, fclbl.getFinalDestination().lastIndexOf(")"));
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
            if (isNotNull(fclbl.getLineMove()) && !"00".equals(fclbl.getLineMove())) {
            } else {
                displayMessage = displayMessage + "--> Please Select LineMove<br>";
            }
            if (CommonUtils.isEmpty(fclbl.getStreamShipBl()) && CommonUtils.isNotEqualIgnoreCase(fclbl.getImportFlag(), "I")) {
                displayMessage = displayMessage + "Please choose SSL BL Prepaid/Collect<br>";
            }
            if (isNotNull(fclbl.getHouseShipperName())) {
                shipperName = fclbl.getHouseShipperName();
            } else {
                displayMessage = displayMessage + "--> Please Enter Master Shipper Name<br>";
            }
            if (isNotNull(fclbl.getExportReference())) {
                exportReference = fclbl.getExportReference();
            }
            if (isNotNull(fclbl.getHouseShipperAddress())) {
                shipperAddressList = helperClass.wrapAddress(fclbl.getHouseShipperAddress());
            }
            if (isNotNull(fclbl.getHouseConsigneeName())) {
                consigneeName = fclbl.getHouseConsigneeName();
            } else {
                displayMessage = displayMessage + "--> Please Enter Master Consignee Name<br>";
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

            if (isNotNull(fclbl.getBillingTerminal())) {
                if (fclbl.getBillingTerminal().contains(",")) {
                    String[] str = fclbl.getBillingTerminal().split(",");
                    String itmnum = str[1].trim().substring(str[1].lastIndexOf("-"));
                    userEmail = ediDAO.getUserEmail(itmnum);
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

            if (null != user.getEmail() && !user.getEmail().trim().equals("")) {
                userEmail = user.getEmail();
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
            List aesList = new FclInbondDetailsDAO().findAesdetails("fileNo", fclbl.getFileNo());
            if (CommonUtils.isEmpty(aesList)) {
                if (!"I".equalsIgnoreCase(fclbl.getImportFlag())) {
                    displayMessage = displayMessage + "--> Please Enter AES/ITN details <br>";
                }
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
                        if (CommonUtils.isExcludeEdiCharacter(address, excludeCharcter)) {
                            errorMessage = errorMessage + "-->  Please Remove the following Special Characters ".concat(excludeCharcter).concat(" from Master Shipper Address ");
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
                        if (CommonUtils.isExcludeEdiCharacter(address, excludeCharcter)) {
                            errorMessage = errorMessage + "--> Please Remove the following Special Characters ".concat(excludeCharcter).concat(" from Master Consignee Address ");
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
                        if (CommonUtils.isExcludeEdiCharacter(address, excludeCharcter)) {
                            errorMessage = errorMessage + "--> Please Remove the following Special Characters ".concat(excludeCharcter).concat(" from Master Notify Address ");
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
            if (contNoList != null && !contNoList.isEmpty()) {
                int count = 0;
                for (int x = 0; x < contNoList.size(); x++) {
                    boolean weightBoolean = false;
                    weightKGSList = weightKGSMap.get(contNoList.get(x));
                    weightLBSList = weightLBSMap.get(contNoList.get(x));
                    volumeCFTList = volumeCFTMap.get(contNoList.get(x));
                    volumeCBMList = volumeCBMMap.get(contNoList.get(x));
                    if (CommonUtils.isNotEmpty(weightKGSList) && CommonUtils.isNotEmpty(volumeCBMList)) {
                        for (int i = 0; i < weightKGSList.size(); i++) {
                            float weight = Float.parseFloat(weightKGSList.get(i));
                            if (weight != 0) {
                                weightBoolean = true;
                                weightType = "KGS";
                            }
                        }
                    }
                    if (CommonUtils.isNotEmpty(weightLBSList) && CommonUtils.isNotEmpty(volumeCFTList)) {
                        for (int i = 0; i < weightLBSList.size(); i++) {
                            float weight = Float.parseFloat(weightLBSList.get(i));
                            if (weight != 0 && !weightBoolean) {
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
                                volumeBoolean = true;
                            }
                        }
                    }
                    if (CommonUtils.isNotEmpty(sealNoList)
                            && isNotNull(sealNoList.get(x).toString())) {
                        String slno = sealNoList.get(x).toString();
                        if (slno.length() < 2 || slno.length() > 15) {
                            errorMessage = errorMessage + "--> Seal Number length must be, between 2 & 15<br>";
                        }
                    } else {
                        displayMessage = displayMessage + "--> Please Enter Seal Number for container " + contNoList.get(x) + "<br>";
                    }
                }

            }
            for (int w = 0; w < contNoList.size(); w++) {
                String containerNo = contNoList.get(w).toString();
                if (CommonUtils.isNotEmpty(containerNo) && containerNo.length() == 11) {
                    containerNo = containerNo.substring(0, 4) + "-" + containerNo.substring(4, 10) + "-" + containerNo.substring(10, 11);
                }
                quantityList = quantityMap.get(contNoList.get(w));
                copyDescList = copyDescMap.get(contNoList.get(w));
                stdRemarksList = stdRemarksMap.get(contNoList.get(w));
                descriptionsList = descriptionsMap.get(contNoList.get(w));
                if (CommonUtils.isNotEmpty(copyDescList)) {
                    for (int i = 0; i < copyDescList.size(); i++) {
                        if ("N".equalsIgnoreCase(copyDescList.get(i))) {
                            List<String> stdRemarkList = (List<String>) new ArrayList();
                            if (!stdRemarksList.isEmpty() && stdRemarksList.size() > i && CommonUtils.isExcludeEdiCharacter(stdRemarksList.get(i), excludeCharcter)) {
                                errorMessage = errorMessage + "--> Please Remove the following Special Characters ".concat(excludeCharcter).concat(" from Master BL Description for Container ").concat(containerNo);
                            } else if (!stdRemarksList.isEmpty() && stdRemarksList.size() > i) {
                                stdRemarkList = helperClass.wrapAddress(stdRemarksList.get(i));
                            }
                            if (CommonUtils.isEmpty(stdRemarkList)) {
                                displayMessage = displayMessage + "--> Column CopyDescription is 'N' and Master BL Description is not entered for Container " + containerNo + "<br>";
                            }
                        } else if ("Y".equalsIgnoreCase(copyDescList.get(i))) {
                            List<String> descList = (List<String>) new ArrayList();
                            if (!descriptionsList.isEmpty() && descriptionsList.size() > i && CommonUtils.isExcludeEdiCharacter(descriptionsList.get(i), excludeCharcter)) {
                                errorMessage = errorMessage + "--> Please Remove the following Special Characters ".concat(excludeCharcter).concat(" from House BL Description for Container ").concat(containerNo);
                            }
                            if (!descriptionsList.isEmpty() && descriptionsList.size() > i) {
                                descList = helperClass.wrapAddress(descriptionsList.get(i));
                            }
                            if (CommonUtils.isEmpty(descList)) {
                                displayMessage = displayMessage + "--> Column CopyDescription is 'Y' and House BL Description is not entered for Container " + containerNo + "<br>";
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
                if (CommonUtils.isNotEmpty(weightKGSList) && CommonUtils.isNotEmpty(volumeCBMList)) {
                    for (int i = 0; i < weightKGSList.size(); i++) {
                        float weight = Float.parseFloat(weightKGSList.get(i));
                        if (weight != 0) {
                            weightBoolean = true;
                            weightType = "KGS";
                        }
                    }
                }
                if (CommonUtils.isNotEmpty(weightLBSList) && CommonUtils.isNotEmpty(volumeCFTList)) {
                    for (int i = 0; i < weightLBSList.size(); i++) {
                        float weight = Float.parseFloat(weightLBSList.get(i));
                        if (weight != 0 && !weightBoolean) {
                            weightBoolean = true;
                            weightType = "LBS";
                        }
                    }
                }
                if (!weightBoolean) {
                    hasWeight = false;
                }
                if (CommonUtils.isNotEmpty(volumeCFTList) && !weightType.equals("KGS")) {
                    for (int i = 0; i < volumeCFTList.size(); i++) {
                        float volume = Float.parseFloat(volumeCFTList.get(i));
                        if (volume != 0) {
                            volumeBoolean = true;
                        }
                    }
                }
                quantityList = quantityMap.get(contNoList.get(w));
                if (CommonUtils.isNotEmpty(weightKGSList) && CommonUtils.isNotEmpty(volumeCBMList)) {
                    for (int i = 0; i < weightKGSList.size(); i++) {
                        float weight = Float.parseFloat(weightKGSList.get(i));
                        if (weight != 0) {
                            weightBoolean = true;
                            weightType = "KGS";
                        }
                    }
                }
                if (CommonUtils.isNotEmpty(weightLBSList) && CommonUtils.isNotEmpty(volumeCFTList)) {
                    for (int i = 0; i < weightLBSList.size(); i++) {
                        float weight = Float.parseFloat(weightLBSList.get(i));
                        if (weight != 0 && !weightBoolean) {
                            weightBoolean = true;
                            weightType = "LBS";
                        }
                    }
                }
                if (!weightBoolean && !hasWeight && hasPackage) {
                    displayMessage = displayMessage + "--> Weights are not entered for container " + containerNo + "<br>";
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
            e.printStackTrace();
            return displayMessage;
        }
    }

    public String escapeXml(String str) {
        str = replaceString(str, "&", "&amp;");
        str = replaceString(str, "<", "&lt;");
        str = replaceString(str, ">", "&gt;");
        str = replaceString(str, "\"", "&quot;");
        str = replaceString(str, "'", "&apos;");
        str = replaceString(str, "+", "?+");
        str = replaceString(str, "/", "?/");
        str = replaceString(str, "~", "?~");
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
