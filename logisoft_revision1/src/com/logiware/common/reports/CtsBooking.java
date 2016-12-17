/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.common.reports;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingImportAms;
import com.gp.cong.logisoft.domain.lcl.LclBookingPad;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceWhse;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.domain.lcl.LclRemarks;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclWarehsDAO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.struts.form.lcl.LclPickupInfoForm;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Ram
 */
public class CtsBooking {

    public synchronized String createCSV(String csvpath, String headerid,
            Long fileid, String moduleName, LclBooking lclBooking, User loginUser, LclPickupInfoForm lclPickupInfoForm) {
        String filename = null;
        try {
            File file = new File(csvpath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String carrierType = "pu";
            if (LclCommonConstant.LCL_IMPORT_TYPE.equalsIgnoreCase(lclBooking.getBookingType())) {
                carrierType = "dd";
            }

            StringBuilder fileFormat = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyHHmm");
            String companyName = LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic");
            String loginName = "";
            if (loginUser.getLoginName().length() > 8) {
                loginName = loginUser.getLoginName().substring(0, 8);
            } else {
                loginName = loginUser.getLoginName();
            }

            fileFormat.append(companyName.toLowerCase()).append(LclCommonConstant.EDI_CTS).append(carrierType).append(loginName);
            String filePath = csvpath + fileFormat.toString() + "" + sdf.format(new Date()) + ".csv";
            FileWriter out = new FileWriter(filePath);
            out.write(writeQuoteEdi(lclBooking, headerid, fileid, moduleName, loginUser, lclPickupInfoForm));
            filename = fileFormat.toString() + "" + sdf.format(new Date()) + ".csv";
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            //log.error("Error inside createCSV while Creating CSV  " + e);
        }
        return filename;
    }

    private String writeQuoteEdi(LclBooking lclBooking, String headerId,
            Long fileid, String moduleName, User loginUser, LclPickupInfoForm lclPickupInfoForm) throws Exception {
        LclBookingPad lclBookingPad = new LclBookingPadDAO().getLclBookingPadByFileNumber(lclBooking.getLclFileNumber().getId());
        LclBookingImportAms lclBookingImportAms = new LclBookingImportAmsDAO().getBkgAms(fileid);
        StringBuilder strBuilder = new StringBuilder();
        Boolean importBKG = false;
        if ("Imports".equals(moduleName)) {
            importBKG = true;
        }
        String PRSEFF = "N";
        BigDecimal cube = new BigDecimal(0);
        BigDecimal weight = new BigDecimal(0);
        BigDecimal cbm = new BigDecimal(0);
        BigDecimal kgs = new BigDecimal(0);
        List<Object[]> unitNoCreatedDate = null;
        String pckTyp = "";
        String piceDesc = "";
        String markNoDesc = "";
        String wareHouseNo = "";
        String bkgWhseLocation = "", bkgWhseNo = "", CarrierName = "";
        String city = "", state = "", zip = "";
        int pieces = 0;
        PortsDAO ports = new PortsDAO();
        LclUnitDAO lclUnitDAO = new LclUnitDAO();
        LclWarehsDAO lclWarehsDAO = new LclWarehsDAO();
        LclBookingPadDAO bookingPadDAO = new LclBookingPadDAO();
        String poo = "00000";
        String pol = "00000";
        String pod = "00000";
        String fd = "00000";
        String fdPort = "000";
        String PooUnloc = "";
        int lcl3pRefNosCount = 0;
        String dispo = "";
        if (null != lclBooking.getPortOfOrigin() && !lclBooking.getPortOfOrigin().getUnLocationCode().equals("") && !lclBooking.getPortOfOrigin().getUnLocationCode().equals("")) {
            String pooShednumber = ports.getPorts(lclBooking.getPortOfOrigin().getUnLocationCode()).getShedulenumber();
            poo = (null != pooShednumber ? pooShednumber : poo);
            PooUnloc = lclBooking.getPortOfOrigin().getUnLocationCode().substring(2, 5).toUpperCase();
        }
        if (null != lclBooking.getPortOfLoading()) {
            String polShednumber = ports.getPorts(lclBooking.getPortOfLoading().getUnLocationCode()).getShedulenumber();
            pol = (null != polShednumber ? polShednumber : pol);
        }
        if (null != lclBooking.getPortOfDestination()) {
            String podShednumber = ports.getPorts(lclBooking.getPortOfDestination().getUnLocationCode()).getShedulenumber();
            pod = (null != podShednumber ? podShednumber : pod);
        }
        if (null != lclBooking.getFinalDestination()) {
            Ports port = ports.getPorts(lclBooking.getFinalDestination().getUnLocationCode());
            fd = port.getShedulenumber();
            fdPort = port.getEciportcode();
        }
        if (null != fileid) {
            unitNoCreatedDate = lclUnitDAO.getUnitNoCreatedDate(fileid);
        }
        if (null != headerId && !headerId.equals("")) {
            wareHouseNo = lclWarehsDAO.getWarehouseNo(headerId);
        }
        if (null != lclBookingPad.getScac() && !lclBookingPad.getScac().equals("")) {
            String scac = lclBookingPad.getScac();
            CarrierName = bookingPadDAO.getCarrierNameUsingScac(scac);
        }
        if (null != lclBookingPad && null != lclBookingPad.getPickupContact().getTradingPartner()) {
            CustomerAddress custAdress = lclBookingPad.getPickupContact().getTradingPartner().getPrimaryCustAddr();
            if (null != custAdress.getCity2() && !custAdress.getCity2().equals("")) {
                city = custAdress.getCity2();
            }
            if (null != custAdress.getState() && !custAdress.getState().equals("")) {
                state = custAdress.getState();
            }
            if (null != custAdress.getZip() && !custAdress.getZip().equals("")) {
                zip = custAdress.getZip();
            }
        }
        if (CommonUtils.isNotEmpty(lclBooking.getFileNumberId())) {
            dispo = new LclBookingDispoDAO().getDispositionCode(lclBooking.getFileNumberId());
        }

        List<LclBookingPiece> lclCommodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclBooking.getLclFileNumber().getId());
        if (lclCommodityList != null && lclCommodityList.size() > 0) {
            LclBookingPiece lclBookingPiece = lclCommodityList.get(0);
            PRSEFF = lclBookingPiece.getPersonalEffects();
            if (null != lclBookingPiece.getPackageType()) {
                pckTyp = lclBookingPiece.getPackageType().getAbbr01();
            }
            if (null != lclBookingPiece.getBookedVolumeImperial()) {
                cube = lclBookingPiece.getBookedVolumeImperial().setScale(0, RoundingMode.HALF_UP);
            }

            if (null != lclBookingPiece.getBookedVolumeImperial()) {
                weight = lclBookingPiece.getBookedWeightImperial().setScale(0, RoundingMode.HALF_UP);
            }

            if (null != lclBookingPiece.getBookedVolumeMetric()) {
                cbm = lclBookingPiece.getBookedVolumeMetric().setScale(2, RoundingMode.UP);
            }

            if (null != lclBookingPiece.getBookedVolumeMetric()) {
                kgs = lclBookingPiece.getBookedWeightMetric().setScale(2, RoundingMode.UP);
            }
            if (null != lclBookingPiece.getPieceDesc()) {
                piceDesc = lclBookingPiece.getPieceDesc();
            }
            if (null != lclBookingPiece.getMarkNoDesc()) {
                markNoDesc = lclBookingPiece.getMarkNoDesc();
            }
            if (null != lclBookingPiece.getBookedPieceCount()) {
                pieces = lclBookingPiece.getBookedPieceCount();
            }
            List<LclBookingPieceWhse> lclBookingPieceWhseList = lclBookingPiece.getLclBookingPieceWhseList();
            if (!lclBookingPieceWhseList.isEmpty()) {
                Collections.reverse(lclBookingPieceWhseList);
                LclBookingPieceWhse lclBookingPieceWhse = lclBookingPieceWhseList.get(0);
                if (lclBookingPieceWhse.getWarehouse().getWarehouseNo() != null) {
                    bkgWhseNo = lclBookingPieceWhse.getWarehouse().getWarehouseNo();
                }
                if (lclBookingPieceWhse.getLocation() != null) {
                    bkgWhseLocation = lclBookingPieceWhse.getLocation();
                }
            }
        }
        LclRemarks lclRemarks = null == new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + lclBooking.getLclFileNumber().getId() + " AND type='Loading Remarks' ") ? new LclRemarks() : new LclRemarksDAO().executeUniqueQuery("from LclRemarks where lclFileNumber.id= " + lclBooking.getLclFileNumber().getId() + " AND type='Loading Remarks'");
        //1 term
        if (importBKG) {
            strBuilder.append("\"" + "IMP" + "\",");
        } else {
            strBuilder.append("\"" + PooUnloc + "\",");
        }
        //2 drcpt
        if (null != lclBooking.getFileNumberId() && !lclBooking.getFileNumberId().equals("")) {
            strBuilder.append("\"" + removeDoubleQuote(lclBooking.getLclFileNumber().getFileNumber()) + "\",");
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //3 commod
        if (lclCommodityList != null && lclCommodityList.size() > 0) {
            LclBookingPiece lclblpiece = lclCommodityList.get(0);
            if (null != lclblpiece.getCommodityType() && !lclblpiece.getCommodityType().equals("")) {
                strBuilder.append("\"" + removeDoubleQuote(lclblpiece.getCommodityType().getCode()) + "\",");
            } else {

                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //4 supnam
        strBuilder.append("\"" + "" + "\",");
        //5 fwdnum
        if (null != lclBooking.getFwdAcct() && !lclBooking.getFwdAcct().equals("")) {
            if (null != lclBooking.getFwdAcct().getAccountno()) {
                strBuilder.append("\"" + removeDoubleQuote(lclBooking.getFwdAcct().getEciAccountNo()) + "\",");
            }
        } else {
            strBuilder.append("\"" + "00000" + "\",");
        }
        //6 fwdnam
        if (null != lclBooking.getFwdAcct() && !lclBooking.getFwdAcct().equals("")) {
            if (null != lclBooking.getFwdAcct().getAccountName() && !lclBooking.getFwdAcct().getAccountName().equals("")) {
                strBuilder.append("\"" + removeDoubleQuote(lclBooking.getFwdAcct().getAccountName()) + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }

        //7 shpnum
        if (null != lclBooking.getShipAcct() && !lclBooking.getShipAcct().equals("")) {
            if (null != lclBooking.getShipAcct().getAccountno() && !lclBooking.getShipAcct().getAccountno().equals("")) {
                strBuilder.append("\"" + removeDoubleQuote(lclBooking.getShipAcct().getEciAccountNo()) + "\",");
            }
        } else {
            strBuilder.append("\"" + "00000" + "\",");
        }
        //8 shpnam
        if (null != lclBooking.getShipAcct() && !lclBooking.getShipAcct().equals("")) {
            if (null != lclBooking.getShipAcct().getAccountName() && !lclBooking.getShipAcct().getAccountName().equals("")) {
                strBuilder.append("\"" + removeDoubleQuote(lclBooking.getShipAcct().getAccountName().toUpperCase()) + "\",");
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //9 connum
        if (null != lclBooking.getConsAcct() && !lclBooking.getConsAcct().equals("")) {
            if (null != lclBooking.getConsAcct().getAccountno() && !lclBooking.getConsAcct().getAccountno().equals("")) {
                strBuilder.append("\"" + removeDoubleQuote(lclBooking.getConsAcct().getECIFWNO()) + "\",");
            }
        } else {
            strBuilder.append("\"" + "00000" + "\",");
        }
        //10 connam
        if (null != lclBooking.getConsAcct() && !lclBooking.getConsAcct().equals("")) {
            if (null != lclBooking.getConsAcct().getAccountName() && !lclBooking.getConsAcct().getAccountName().equals("")) {
                strBuilder.append("\"" + removeDoubleQuote(StringUtils.left(lclBooking.getConsAcct().getAccountName().toUpperCase(), 30)) + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //11 tline
        strBuilder.append("\"" + "00000" + "\",");
        //12 tlinam
        strBuilder.append("\"" + "" + "\",");
        //13 notnum
        if (null != lclBooking.getNotyAcct() && !lclBooking.getNotyAcct().equals("") && lclBooking.getNotyAcct().getECIFWNO() != null) {
            strBuilder.append("\"" + removeDoubleQuote(lclBooking.getNotyAcct().getECIFWNO().toUpperCase()) + "\",");
        } else {
            strBuilder.append("\"" + "00000" + "\",");
        }
        //14 notnam
        if (null != lclBooking.getNotyAcct() && !lclBooking.getNotyAcct().equals("")) {
            if (null != lclBooking.getNotyAcct().getAccountName() && !lclBooking.getNotyAcct().getAccountName().equals("")) {
                strBuilder.append("\"" + removeDoubleQuote(lclBooking.getNotyAcct().getAccountName().toUpperCase()) + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //15 warnum

        if (importBKG) {
            strBuilder.append("\"" + removeDoubleQuote(wareHouseNo) + "\",");
        } else {
            if (null != bkgWhseNo && !bkgWhseNo.equals("")) {
                if ("OBKG".equalsIgnoreCase(dispo)) {
                    strBuilder.append("\"" + removeDoubleQuote(bkgWhseNo.replace('W', 'B')) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(bkgWhseNo) + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        }

        //16 warelc
        if (importBKG) {
            if (null != bkgWhseLocation && !bkgWhseLocation.equals("") && null != bkgWhseNo && !bkgWhseNo.equals("")) {
                String bkgWhseLocationNo = bkgWhseNo + "" + bkgWhseLocation;
                if (bkgWhseLocationNo.length() > 7) {
                    strBuilder.append("\"" + removeDoubleQuote(bkgWhseLocationNo).substring(0, 7).toUpperCase() + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(bkgWhseLocationNo).toUpperCase() + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        /*   else {
         if (lclBookingPad != null && null != lclBookingPad.getDeliveryContact() && lclBookingPad.getDeliveryContact().getAddress() != null && lclBookingPad.getDeliveryContact().getAddress() != "") {
         if (lclBookingPad.getDeliveryContact().getAddress().length() > 7) {
         strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getDeliveryContact().getAddress().toUpperCase()).substring(0, 7) + "\",");
         } else {
         strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getDeliveryContact().getAddress().toUpperCase()) + "\",");
         }
         } else {
         strBuilder.append("\"" + "" + "\",");

         }
         }*/
        //17 pro
        strBuilder.append("\"" + "" + "\",");
        //18 pc
        strBuilder.append("\"" + lclBooking.getBillingType() + "\",");
        //19 colamt
        strBuilder.append("0,");
        //20 chknum
        strBuilder.append("\"" + "" + "\",");
        //21 pcs01
        strBuilder.append("\"" + pieces + "\",");
        //22 notif
        if (null != lclBooking.getConsAcct() && !lclBooking.getConsAcct().equals("")) {
            if (null != lclBooking.getConsAcct().getAccountno() && !lclBooking.getConsAcct().getAccountno().equals("")) {
                strBuilder.append("\"" + removeDoubleQuote(lclBooking.getConsAcct().getECIFWNO()) + "\",");
            }
        } else {
            strBuilder.append("\"" + "00000" + "\",");
        }
        //23 harm
        strBuilder.append("\"" + "" + "\",");
        //24 brlcod
        strBuilder.append("\"" + "N" + "\",");
        //25 brlsiz
        strBuilder.append("\"" + "" + "\",");
        //26 godate
        if (importBKG && null != lclBooking.getLclFileNumber().getLclBookingImport() && null != lclBooking.getLclFileNumber().getLclBookingImport().getGoDatetime()) {
            strBuilder.append("\"" + DateUtils.parseDateToString(lclBooking.getLclFileNumber().getLclBookingImport().getGoDatetime()).replaceAll("/", "") + "\",");
        } else {
            strBuilder.append("0,");
        }

        //27 dsc01
        if (null != piceDesc && !piceDesc.equals("")) {

            int Size = piceDesc.length();
            int tokenCount = 0;
            for (int i = 0; i < Size; i = i + 25) {
                if (tokenCount < 5) {
                    if (piceDesc.length() > (i + 25)) {
                        strBuilder.append("\"" + removeDoubleQuote(piceDesc.toUpperCase().substring(i, i + 25)) + "\",");
                    } else {
                        strBuilder.append("\"" + removeDoubleQuote(piceDesc.toUpperCase().substring(i)) + "\",");
                    }
                    tokenCount++;
                }
            }

            for (int i = tokenCount; i < 5; i++) {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");

            strBuilder.append("\"" + "" + "\",");
            //28 dsc02
            strBuilder.append("\"" + "" + "\",");
            //29 dsc03
            strBuilder.append("\"" + "" + "\",");
            //30 dsc04
            strBuilder.append("\"" + "" + "\",");
            //31 dsc05
        }
        //32 whsedr
        strBuilder.append("\"" + "" + "\",");
        //33 absorg
        strBuilder.append("\"" + poo + "\",");
        //34 marks1
        if (null != markNoDesc && !markNoDesc.equals("")) {
            int size = markNoDesc.length();
            int tokencount = 0;
            for (int i = 0; i < size; i = i + 12) {
                if (tokencount < 5) {
                    if (markNoDesc.length() > (i + 12)) {
                        strBuilder.append("\"" + removeDoubleQuote(markNoDesc.toUpperCase().substring(i, i + 12)) + "\",");
                    } else {
                        strBuilder.append("\"" + removeDoubleQuote(markNoDesc.toUpperCase().substring(i)) + "\",");
                    }
                    tokencount++;
                }
            }
            for (int i = tokencount; i < 5; i++) {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");

            strBuilder.append("\"" + "" + "\",");
            //35 marks2
            strBuilder.append("\"" + "" + "\",");
            //36 marks3
            strBuilder.append("\"" + "" + "\",");
            //37 marks4
            strBuilder.append("\"" + "" + "\",");
            //38 marks5
        }
        //39 unus02
        strBuilder.append("\"" + "" + "\",");
        //40 pol
        strBuilder.append("\"" + pol + "\",");
        //41 cub01
        strBuilder.append("\"" + cube + "\",");
        //42 notifn
        if (importBKG) {
            if (null != lclBooking.getNotyAcct() && !lclBooking.getNotyAcct().equals("")) {
                if (null != lclBooking.getNotyAcct().getAccountName() && !lclBooking.getNotyAcct().getAccountName().equals("")) {
                    strBuilder.append("\"" + removeDoubleQuote(lclBooking.getNotyAcct().getAccountName().toUpperCase()) + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //43 TOTWGT
        strBuilder.append("\"" + weight + "\",");
        //44 podsng
        if (importBKG && null != lclBooking.getLclFileNumber().getLclBookingImport().getPodSignedBy()) {
            strBuilder.append("\"" + removeDoubleQuote(lclBooking.getLclFileNumber().getLclBookingImport().getPodSignedBy().toUpperCase()) + "\",");
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //45 dordlv
        strBuilder.append("0,");
        //46 frtrel
        if (importBKG) {
            if (null != lclBooking.getLclFileNumber().getLclBookingImport().getFreightReleaseDateTime()) {
                strBuilder.append("\"" + "Y" + "\",");
            } else {
                strBuilder.append("\"" + "N" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //47 unus03
        strBuilder.append("\"" + "" + "\",");

        //48 unitkey
        if (unitNoCreatedDate != null && unitNoCreatedDate.size() > 0) {
            String unitno = "";
            for (Object[] unitObject : unitNoCreatedDate) {
                StringBuilder space = new StringBuilder();
                unitno = unitObject[0].toString().toUpperCase();
                if (unitObject[0].toString().length() < 20) {
                    Integer cal = 20 - unitObject[0].toString().length();
                    for (int j = 0; j < cal; j++) {
                        space.append(" ").toString();
                    }
                } else {
                    unitno = unitObject[0].toString().substring(0, 20);
                }
                Date formattedDate = DateUtils.parseDate(unitObject[1].toString(), "yyyy-MM-dd HH:mm:ss.SS");
                String unitDate = DateUtils.formatDate(formattedDate, "yyyyMMdd");

                strBuilder.append("\"" + removeDoubleQuote(unitno + space + unitDate) + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //49 itclass
        if (importBKG && null != lclBooking.getLclFileNumber().getLclBookingImport() && null != lclBooking.getLclFileNumber().getLclBookingImport().getItClass()) {
            strBuilder.append("\"" + lclBooking.getLclFileNumber().getLclBookingImport().getItClass().toUpperCase() + "\",");
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //50 entry class
        if (importBKG && null != lclBooking.getLclFileNumber().getLclBookingImport() && null != lclBooking.getLclFileNumber().getLclBookingImport().getEntryClass()) {
            strBuilder.append("\"" + lclBooking.getLclFileNumber().getLclBookingImport().getEntryClass().toUpperCase() + "\",");
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //51 tos
        if (null != lclBookingPad) {
            if (null != lclBookingPad.getTermsOfService() && lclBookingPad.getTermsOfService().length() > 20) {
                strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getTermsOfService().toUpperCase().substring(0, 19)) + "\",");
            } else {
                strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getTermsOfService().toUpperCase()) + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //52 WAREL2
        strBuilder.append("\"" + "" + "\",");
        //53 WAREL3
        strBuilder.append("\"" + "" + "\",");
        //54 WAREL4
        strBuilder.append("\"" + "" + "\",");
        //55 WAREL5
        strBuilder.append("\"" + "" + "\",");
        //56 WAREL6
        strBuilder.append("\"" + "" + "\",");
        //57 WAREL7
        strBuilder.append("\"" + "" + "\",");
        //58 WAREL8
        strBuilder.append("\"" + "" + "\",");
        //59 WAREL9
        strBuilder.append("\"" + "" + "\",");
        //60 WAREL0
        strBuilder.append("\"" + "" + "\",");
        //61 SECCD1
        strBuilder.append("\"" + "" + "\",");
        //62 SECCD2
        strBuilder.append("\"" + "" + "\",");
        //63 SECAM1
        strBuilder.append("0,");
        //64 SECAM2
        strBuilder.append("0,");
        //65 DRDATE
        if (lclBookingPad != null) {
            strBuilder.append(DateUtils.formatDateToYYYYMMDD(lclBookingPad.getEnteredDatetime()).replaceAll("/", "") + ",");
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //66 CMMNTS
        strBuilder.append("\"" + "" + "\",");
        //67 WEGHBY
        strBuilder.append("\"" + removeDoubleQuote("") + "\",");
        //68 ADDCOM
        if (loginUser != null && loginUser.getEmail() != null & !loginUser.getEmail().equals("")) {
            strBuilder.append("\"" + removeDoubleQuote(loginUser.getEmail().toUpperCase()) + "\",");
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //69 remarks for loading
        if (null != lclRemarks.getRemarks() && !lclRemarks.getRemarks().equals("")) {
            if (lclRemarks.getRemarks().length() > 20) {
                strBuilder.append("\"" + removeDoubleQuote(lclRemarks.getRemarks().substring(0, 19).toUpperCase()) + "\",");
            } else {
                strBuilder.append("\"" + removeDoubleQuote(lclRemarks.getRemarks().toUpperCase()) + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //70 RATQUT
        strBuilder.append("\"" + "" + "\",");
        //71 CODE
        strBuilder.append("\"" + "" + "\",");
        //72 DRINVC
        strBuilder.append("\"" + "" + "\",");
        //73 PAPRWK
        if (lclBooking.getClientPwkRecvd()) {
            strBuilder.append("\"" + "Y" + "\",");
        } else {
            strBuilder.append("\"" + "N" + "\",");
        }
        //74 BOKCFT
        strBuilder.append("\"" + cube + "\",");
        //75 USEROT
        strBuilder.append("\"" + "" + "\",");
        //76 TIMEIN
        strBuilder.append("0,");
        //77 TIMER
        strBuilder.append("0,");
        //78 DATERL
        strBuilder.append("0,");
        //79 USERRL
        strBuilder.append("\"" + "" + "\",");
        //HOT1 - HOT4 80 - 83
        if (!importBKG) {
            List<LclBookingHotCode> lclBookingHotCodes = new LclBookingHotCodeDAO().findByProperty("lclFileNumber.id", fileid);
            if (!lclBookingHotCodes.isEmpty()) {
                for (int i = 0; i < lclBookingHotCodes.size(); i++) {
                    if (lcl3pRefNosCount < 4) {
                        strBuilder.append("\"" + lclBookingHotCodes.get(i).getCode().substring(0, 3) + "\",");
                        lcl3pRefNosCount++;
                    }
                }
                for (int i = lcl3pRefNosCount; i < 4; i++) {
                    strBuilder.append("\"" + "" + "\",");
                    lcl3pRefNosCount++;
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
                strBuilder.append("\"" + "" + "\",");
                strBuilder.append("\"" + "" + "\",");
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
            strBuilder.append("\"" + "" + "\",");
            strBuilder.append("\"" + "" + "\",");
            strBuilder.append("\"" + "" + "\",");
        }
        //84 PCSOUT
        strBuilder.append("0,");
        //85 COMMNT
        strBuilder.append("\"" + "" + "\",");
        //86 USRENT
        if (lclBooking != null && null != lclBooking.getEnteredBy().getLoginName()) {
            String loginName = lclBooking.getEnteredBy().getLoginName();
            if (loginName.length() > 8) {
                strBuilder.append("\"" + lclBooking.getEnteredBy().getLoginName().toUpperCase().substring(0, 8) + "\",");
            } else {
                strBuilder.append("\"" + lclBooking.getEnteredBy().getLoginName().toUpperCase() + "\",");

            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //87 MESEBY
        strBuilder.append("\"" + "" + "\",");
        //88 DTOUT
        strBuilder.append("0,");
        //89 SECCD3
        strBuilder.append("\"" + "0000" + "\",");
        //90 SECAM3
        strBuilder.append("0,");
        //91 PRSEFF
        strBuilder.append("\"" + PRSEFF + "\",");
        //92 MESMAN
        strBuilder.append("\"" + "" + "\",");
        //93 INLOTM
        strBuilder.append("\"" + "" + "\",");
        //94 INLDTM
        strBuilder.append("\"" + "" + "\",");
        //95 INLVY
        strBuilder.append("\"" + "" + "\",");
        //96 INLTRL
        if (unitNoCreatedDate != null && unitNoCreatedDate.size() > 0) {
            for (Object[] unitObject : unitNoCreatedDate) {
                if (unitObject[0].toString().length() > 15) {

                    strBuilder.append("\"" + removeDoubleQuote(unitObject[0].toString().toUpperCase().substring(0, 15)) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(unitObject[0].toString().toUpperCase()) + "\",");
                }
            }
        } else {
            strBuilder.append("\"" + "" + "\",");
        }

        //97 INLADV
        strBuilder.append("\"" + "N" + "\",");
        //98 INLPD
        strBuilder.append("\"" + "N" + "\",");
        //99 LDTERM
        strBuilder.append("\"" + "" + "\",");
        //100 DESTN
        strBuilder.append("\"" + fdPort + "\",");//100
        //101 LDVOY
        strBuilder.append("\"" + "" + "\",");
        //102 INL2OT
        strBuilder.append("\"" + "" + "\",");
        //103 INL2DT
        strBuilder.append("\"" + "" + "\",");
        //104 INL2VY
        strBuilder.append("\"" + "" + "\",");
        //105 ERTACT
        strBuilder.append("\"" + "" + "\",");
        //106 UNUS08
        strBuilder.append("\"" + "L" + "\",");
        //107 DESTNM
        strBuilder.append("\"" + lclBooking.getFinalDestination().getUnLocationName().toUpperCase() + "\",");
        //108 INVCO
        strBuilder.append("\"" + "" + "\",");
        //109 INVTM
        strBuilder.append("\"" + "" + "\",");
        //110 INVCE
        strBuilder.append("\"" + "" + "\",");
        //111 BLTERM
        if (null != lclBooking.getTerminal().getTrmnum() && !lclBooking.getTerminal().getTrmnum().equals("")) {
            strBuilder.append("\"" + lclBooking.getTerminal().getTrmnum() + "\",");
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //112 UNKNOW
        strBuilder.append("\"" + "" + "\",");
        //113 VOYAGE
        strBuilder.append("\"" + "" + "\",");
        //114 HTI
        strBuilder.append("\"" + "000000000000000000000000000000000000000000000000000000000000" + "\",");
        //115 WGT,1
        strBuilder.append("\"" + "00000" + "\",");
        //116 WGT,2
        strBuilder.append("\"" + "00000" + "\",");
        //117 WGT,3
        strBuilder.append("\"" + "00000" + "\",");
        //118 WGT,4
        strBuilder.append("\"" + "00000" + "\",");
        //119 WGT,5
        strBuilder.append("\"" + "00000" + "\",");
        //120 WGT,6
        strBuilder.append("\"" + "00000" + "\",");
        //121 WDI
        strBuilder.append("\"" + "000000000000000000000000000000000000000000000000000000000000" + "\",");
        //122 WGT,7
        strBuilder.append("\"" + "00000" + "\",");
        //123 WGT,8
        strBuilder.append("\"" + "00000" + "\",");
        //124 WGT,9
        strBuilder.append("\"" + "00000" + "\",");
        //125 WGT,10
        strBuilder.append("\"" + "00000" + "\",");
        //126 WGT,11
        strBuilder.append("\"" + "00000" + "\",");
        //127 WGT,12
        strBuilder.append("\"" + "00000" + "\",");
        //128 LNI
        strBuilder.append("\"" + "000000000000000000000000000000000000000000000000000000000000" + "\",");
        //129 WGT,13
        strBuilder.append("\"" + "00000" + "\",");
        //130 WGT,14
        strBuilder.append("\"" + "00000" + "\",");
        //131 WGT,15
        strBuilder.append("\"" + "00000" + "\",");
        //132 WGT,16
        strBuilder.append("\"" + "00000" + "\",");
        //133 WGT,17
        strBuilder.append("\"" + "00000" + "\",");
        //134 WGT,18
        strBuilder.append("\"" + "00000" + "\",");
        //135 CNT
        strBuilder.append("\"" + "00000000000000000000000000000000000000000000000000000000000000000000000000000000" + "\",");
        //136 WGT,19
        strBuilder.append("\"" + "00000" + "\",");
        //137 WGT,20
        strBuilder.append("\"" + "00000" + "\",");
        //138 INL2KY
        strBuilder.append("\"" + "" + "\",");
        //139 UNUS04
        strBuilder.append("\"" + "" + "\",");
        //140 VSHPDT
        strBuilder.append("0,");
        //141 REFNUM
        strBuilder.append("\"" + "" + "\",");
        //142 BOKWGT
        strBuilder.append("\"" + weight + "\",");
        //143 TRNTIM
        strBuilder.append("\"" + "" + "\",");
        //144 TRNDAT
        strBuilder.append("\"" + "" + "\",");
        //145 TRNUSR
        strBuilder.append("\"" + "" + "\",");
        //146 OSDLIN
        strBuilder.append("\"" + "" + "\",");
        //147 DRBKD
        strBuilder.append(DateUtils.formatDateToYYYYMMDD(lclBooking.getEnteredDatetime()).replaceAll("/", "") + ",");
        //148 BKGTM
        strBuilder.append(DateUtils.formatStringTime(lclBooking.getEnteredDatetime()).replaceAll(":", "") + ",");
        //149 WGTVER
        strBuilder.append("\"" + "N" + "\",");
        //150 FRTREF
        strBuilder.append("\"" + "" + "\",");
        //151 SHPREF
        strBuilder.append("\"" + "" + "\",");
        //152 CONREF
        strBuilder.append("\"" + "" + "\",");
        //153 FAN
        strBuilder.append("\"" + "" + "\",");
        //154-159 INBOND1 - INBOND6
        strBuilder.append("\"" + "" + "\",");
        strBuilder.append("\"" + "" + "\",");
        strBuilder.append("\"" + "" + "\",");
        strBuilder.append("\"" + "" + "\",");
        strBuilder.append("\"" + "" + "\",");
        strBuilder.append("\"" + "" + "\",");
        //160 PFRMTM
        if (null != lclBooking.getTerminal().getTrmnum() && !lclBooking.getTerminal().getTrmnum().equals("")) {
            strBuilder.append("\"" + lclBooking.getTerminal().getTrmnum() + "\",");
        } else {
            strBuilder.append("\"" + "" + "\",");
        }
        //161 PYN
        if (importBKG) {
            strBuilder.append("\"" + "D" + "\",");
        } else {
            strBuilder.append("\"" + "Y" + "\",");
        }

        //162 PTO
        if (importBKG) {
            if (null != CarrierName && !CarrierName.equals("")) {
                if (CarrierName.length() > 48) {
                    String dataFilter = CarrierName.substring(0, 48);
                    strBuilder.append("\"" + removeDoubleQuote(dataFilter.toUpperCase()) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(CarrierName.toUpperCase()) + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            if (null != lclBookingPad && null != lclBookingPad.getPickUpTo()) {
                if (lclBookingPad.getPickUpTo().length() > 47) {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getPickUpTo().toUpperCase().substring(0, 47)) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getPickUpTo().toUpperCase()) + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        }
        //163 SCAC
        if (importBKG) {
            if (null != lclBookingImportAms && null != lclBookingImportAms.getScac()) {
                strBuilder.append("\"" + removeDoubleQuote(lclBookingImportAms.getScac().toUpperCase()) + "\",");
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            if (null != lclBookingPad && null != lclBookingPad.getScac()) {
                strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getScac().toUpperCase()) + "\",");
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        }
        //164 PSHIP
        if (importBKG) {
            if (lclBookingPad != null && null != lclBookingPad.getPickupContact() && null != lclBookingPad.getPickupContact().getCompanyName()) {
                String companyName = lclBookingPad.getPickupContact().getCompanyName();
                if (companyName.length() > 44) {
                    strBuilder.append("\"" + removeDoubleQuote(companyName.substring(0, 44).toUpperCase()) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(companyName.toUpperCase()) + "\",");

                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            if (lclBookingPad != null && null != lclBookingPad.getPickupContact()) {
                strBuilder.append("\"" + removeDoubleQuote(null != lclBookingPad.getPickupContact().getCompanyName() ? lclBookingPad.getPickupContact().getCompanyName().toUpperCase() : "") + "\",");
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        }
        //165 PADD1
        if (lclBookingPad != null) {
            if (null != lclBookingPad.getPickupContact() && null != lclBookingPad.getPickupContact().getAddress() && !lclBookingPad.getPickupContact().getAddress().equals("")) {
                String[] chars = lclBookingPad.getPickupContact().getAddress().split("\n");
                if (chars[0].length() > 52) {
                    strBuilder.append("\"" + removeDoubleQuote(chars[0].substring(0, 52).toUpperCase()) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(chars[0].toUpperCase()) + "\",");

                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            strBuilder.append("\"" + "" + "\",");

        }
        //166 PADD2
        if (importBKG) {
            String concatCityStateZip = city + state + zip;
//            String[] newCustomerAddress = lclBookingPad.getPickupContact().getAddress().split(",");
//            String[] citySplit = newCustomerAddress[0].split("\\r?\\n");
//            Pattern newCustomerZip = Pattern.compile("\\d{5}?");
//            Matcher zipMatcher = newCustomerZip.matcher(lclBookingPad.getPickupContact().getAddress());
            if (concatCityStateZip != null && !concatCityStateZip.equals("")) {
                city = StringUtils.left(StringUtils.rightPad(city, 41, " "), 41);
                state = StringUtils.left(StringUtils.rightPad(state, 2, " "), 2);
                zip = StringUtils.left(StringUtils.rightPad(zip, 9, " "), 9);
                concatCityStateZip = city + state + zip;
                strBuilder.append("\"" + removeDoubleQuote(concatCityStateZip.toUpperCase()) + "\",");
            } else if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperCity()) || CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperState()) || CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperZip())) {
                if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperCity())) {
                    city = StringUtils.left(StringUtils.rightPad(lclPickupInfoForm.getShipperCity().trim(), 41, " "), 41);
                } else {
                    city = StringUtils.rightPad(city, 41, " ");
                }
                if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperState())) {
                    state = StringUtils.left(StringUtils.rightPad(lclPickupInfoForm.getShipperState().trim(), 2, " "), 2);
                } else {
                    state = StringUtils.rightPad(state, 2, " ");
                }
                if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperZip())) {
                    zip = StringUtils.left(StringUtils.rightPad(lclPickupInfoForm.getShipperZip().trim(), 9, " "), 9);
                } else {
                    zip = StringUtils.rightPad(zip, 9, " ");
                }
                concatCityStateZip = city + state + zip;
                strBuilder.append("\"" + removeDoubleQuote(concatCityStateZip.toUpperCase()) + "\",");
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
        } else {
            String concatCityStateZip = city + state + zip;

            if (concatCityStateZip != null && !concatCityStateZip.equals("")) {
                city = StringUtils.left(StringUtils.rightPad(city, 41, " "), 41);
                state = StringUtils.left(StringUtils.rightPad(state, 2, " "), 2);
                zip = StringUtils.left(StringUtils.rightPad(zip, 9, " "), 9);
                concatCityStateZip = city + state + zip;
                strBuilder.append("\"" + removeDoubleQuote(concatCityStateZip.toUpperCase()) + "\",");
            } else if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperCity()) || CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperState()) || CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperZip())) {
                if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperCity())) {
                    city = StringUtils.left(StringUtils.rightPad(lclPickupInfoForm.getShipperCity().trim(), 41, " "), 41);
                } else {
                    city = StringUtils.rightPad(city, 41, " ");
                }
                if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperState())) {
                    state = StringUtils.left(StringUtils.rightPad(lclPickupInfoForm.getShipperState().trim(), 2, " "), 2);
                } else {
                    state = StringUtils.rightPad(state, 2, " ");
                }
                if (CommonUtils.isNotEmpty(lclPickupInfoForm.getShipperZip())) {
                    zip = StringUtils.left(StringUtils.rightPad(lclPickupInfoForm.getShipperZip().trim(), 9, " "), 9);
                } else {
                    zip = StringUtils.rightPad(zip, 9, " ");
                }
                concatCityStateZip = city + state + zip;
                strBuilder.append("\"" + removeDoubleQuote(concatCityStateZip.toUpperCase()) + "\",");
                
                //strBuilder.append("\"" + "" + "\",");
            
               // String concatExpCityStateZip = expCity + expState + expZip;
            }
        }

            //167 PPHONE
            if (lclBookingPad != null && null != lclBookingPad.getPickupContact()) {
                String contactName = "";
                String pickupPhoneFax = "";
                String phoneFaxTrim = "";
                StringBuilder spaceChar = new StringBuilder();

                if (lclBookingPad != null && null != lclBookingPad.getPickupContact().getContactName() && !lclBookingPad.getPickupContact().getContactName().equals("")) {
                    contactName += removeDoubleQuote(lclBookingPad.getPickupContact().getContactName());
                }
                if (lclBookingPad != null && null != lclBookingPad.getPickupContact().getPhone1() && !lclBookingPad.getPickupContact().getPhone1().equals("")) {
                    pickupPhoneFax += removeDoubleQuote(lclBookingPad.getPickupContact().getPhone1().replaceAll("-", ""));
                }
                if (lclBookingPad != null && null != lclBookingPad.getPickupContact().getFax1() && !lclBookingPad.getPickupContact().getFax1().equals("")) {
                    pickupPhoneFax += removeDoubleQuote(lclBookingPad.getPickupContact().getFax1().replaceAll("-", ""));
                }
                if (pickupPhoneFax.length() > 20) {
                    phoneFaxTrim = pickupPhoneFax.substring(0, 20);
                } else {
                    phoneFaxTrim = pickupPhoneFax;
                }
                if (contactName.length() < 25) {
                    Integer cal = 25 - contactName.length();
                    for (int j = 0; j < cal; j++) {
                        spaceChar.append(" ").toString();
                    }
                } else {
                    contactName = contactName.substring(0, 25);
                }
                String concatCPF = contactName + "" + spaceChar + "" + phoneFaxTrim;
                if (concatCPF.length() > 45) {
                    String contactDetails = concatCPF.substring(0, 45);
                    strBuilder.append("\"" + removeDoubleQuote(contactDetails.toUpperCase()) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(concatCPF.toUpperCase()) + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            //168 PHOURS
            if (null != lclBookingPad && null != lclBookingPad.getPickupHours()) {
                if (lclBookingPad.getPickupHours().length() > 27) {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getPickupHours().toUpperCase().substring(0, 27)) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getPickupHours().toUpperCase()) + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            //169 PREADY
            if (importBKG) {
                if (lclBookingPad != null && lclBookingPad.getDeliveryReadyDate() != null) {
                    StringBuffer emptySpaceChar = new StringBuffer();
                    String readyDate = DateUtils.parseDateToString(lclBookingPad.getDeliveryReadyDate()).replaceAll("/", "");
                    for (int j = 0; j < 8; j++) {
                        emptySpaceChar.append(" ").toString();
                    }
                    emptySpaceChar.append(readyDate);

                    strBuilder.append("\"" + emptySpaceChar + "\",");
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            } else {
                if (null != lclBookingPad) {
                    if (null != lclBookingPad.getPickupReadyDate() && null != lclBookingPad.getPickupReadyDate()) {
                        StringBuffer emptySpaceChar = new StringBuffer();
                        String pReady = DateUtils.parseDateToString(lclBookingPad.getPickupReadyDate()).replaceAll("/", "");
                        for (int j = 0; j < 8; j++) {
                            emptySpaceChar.append(" ").toString();
                        }
                        emptySpaceChar.append(pReady);
                        strBuilder.append("\"" + emptySpaceChar + "\",");
                    } else {
                        strBuilder.append("\"" + "" + "\",");
                    }
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            }
            //170 - 173 PINS1
            if (importBKG) {
                if (lclBookingPad != null && null != lclBookingPad.getDeliveryInstructions()
                        && !lclBookingPad.getDeliveryInstructions().equals("")) {
                    String[] chars = lclBookingPad.getDeliveryInstructions().split("\n");
                    if (chars.length > 1) {
                        if (chars[1].length() > 59) {
                            strBuilder.append("\"" + removeDoubleQuote(chars[1].substring(0, 59).toUpperCase()) + "\",");
                        } else {
                            strBuilder.append("\"" + removeDoubleQuote(chars[1].toUpperCase()) + "\",");
                        }
                    } else {
                        strBuilder.append("\"" + "" + "\",");
                    }
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
                if (null != lclBookingPad) {
                    if (null != lclBookingPad.getPickupInstructions() && !lclBookingPad.getPickupInstructions().equals("")) {
                        String[] li = lclBookingPad.getPickupInstructions().split("\r\n");
                        int count = 0;
                        for (String dataTemp : li) {
                            if (count < 3) {
                                if (dataTemp.length() > 59) {
                                    strBuilder.append("\"" + removeDoubleQuote(dataTemp.toUpperCase().substring(0, 59)) + "\",");
                                    count++;
                                } else {
                                    strBuilder.append("\"" + removeDoubleQuote(dataTemp.toUpperCase()) + "\",");
                                    count++;
                                }
                            }
                        }
                        for (int i = count; i < 3; i++) {
                            strBuilder.append("\"" + "" + "\",");
                        }
                    } else {
                        strBuilder.append("\"" + "" + "\",");
                        strBuilder.append("\"" + "" + "\",");
                        strBuilder.append("\"" + "" + "\",");
                    }
                } else {
                    strBuilder.append("\"" + "" + "\",");
                    strBuilder.append("\"" + "" + "\",");
                    strBuilder.append("\"" + "" + "\",");
                }
            } else {
                if (null != lclBookingPad && null != lclBookingPad.getPickupReferenceNo()) {
                    String[] refPro = lclBookingPad.getPickupReferenceNo().split("\r\n");
                    if (refPro.length > 0) {
                        if (refPro[0].length() > 59) {
                            strBuilder.append("\"" + removeDoubleQuote(refPro[0].toUpperCase().substring(0, 59)) + "\",");
                        } else {
                            strBuilder.append("\"" + removeDoubleQuote(refPro[0].toUpperCase()) + "\",");
                        }
                    } else {
                        strBuilder.append("\"" + "" + "\",");
                    }
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
                if (null != lclBookingPad) {
                    if (null != lclBookingPad.getPickupInstructions() && !lclBookingPad.getPickupInstructions().equals("")) {
                        String[] li = lclBookingPad.getPickupInstructions().split("\r\n");
                        int count = 0;
                        for (String dataTemp : li) {
                            if (count < 3) {
                                if (dataTemp.length() > 59) {
                                    strBuilder.append("\"" + removeDoubleQuote(dataTemp.toUpperCase().substring(0, 59)) + "\",");
                                    count++;
                                } else {
                                    strBuilder.append("\"" + removeDoubleQuote(dataTemp.toUpperCase()) + "\",");
                                    count++;
                                }
                            }
                        }
                        for (int i = count; i < 3; i++) {
                            strBuilder.append("\"" + "" + "\",");
                        }
                    } else {
                        strBuilder.append("\"" + "" + "\",");
                        strBuilder.append("\"" + "" + "\",");
                        strBuilder.append("\"" + "" + "\",");
                    }
                } else {
                    strBuilder.append("\"" + "" + "\",");
                    strBuilder.append("\"" + "" + "\",");
                    strBuilder.append("\"" + "" + "\",");
                }
            }
            //174 PCUTOF
            if (importBKG) {
                if (lclBooking != null && lclBooking.getLclFileNumber().getLclBookingImport().getLastFreeDateTime() != null) {
                    StringBuffer spaceChar = new StringBuffer();
                    String lastFreeDate = DateUtils.parseDateToString(lclBooking.getLclFileNumber().getLclBookingImport().getLastFreeDateTime()).replaceAll("/", "");
                    for (int j = 0; j < 46; j++) {
                        spaceChar.append(" ").toString();
                    }
                    spaceChar.append(lastFreeDate);

                    strBuilder.append("\"" + spaceChar + "\",");
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            } else {
                if (null != lclBookingPad) {
                    if (null != lclBookingPad.getPickupCutoffDate() && null != lclBookingPad.getPickupCutoffDate()) {
                        String pCutOff = DateUtils.parseDateToString(lclBookingPad.getPickupCutoffDate()).replaceAll("/", "");
                        strBuilder.append("\"");
                        if (null != lclBookingPad.getPickupReadyNote()) {
                            strBuilder.append(lclBookingPad.getPickupReadyNote().toUpperCase()).append(" ");
                        }
                        strBuilder.append(pCutOff).append("\",");
                    } else {
                        strBuilder.append("\"" + "" + "\",");
                    }
                }
            }
            //175 PWNUM

            if (importBKG) {
                if (lclBookingPad != null && null != lclBookingPad.getDeliveryContact() && null != lclBookingPad.getDeliveryContact().getWarehouse() && null != lclBookingPad.getDeliveryContact().getWarehouse().getWarehouseNo()) {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getDeliveryContact().getWarehouse().getWarehouseNo().replace('W', 'B')) + "\",");
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            } else {
                if (lclBookingPad != null && null != lclBookingPad.getDeliveryContact() && null != lclBookingPad.getDeliveryContact().getCompanyName()) {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getDeliveryContact().getCompanyName().substring(lclBookingPad.getDeliveryContact().getCompanyName().length() - 3)) + "\",");
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            }

            //176 PCOMDS
            if (null != lclBookingPad) {
                if (null != lclBookingPad.getCommodityDesc() && !lclBookingPad.getCommodityDesc().equals("")) {
                    if (lclBookingPad.getCommodityDesc().length() > 40) {
                        strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getCommodityDesc().toUpperCase().substring(0, 39)) + "\",");
                    } else {
                        strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getCommodityDesc().toUpperCase()) + "\",");
                    }
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            //177 LDTRLR
            strBuilder.append("\"" + "" + "\",");
            //178 LDTRDT
            strBuilder.append("\"" + "" + "\",");
            //179 ERTYN
            strBuilder.append("\"" + "N" + "\",");
            //180 PUPDYN
            strBuilder.append("\"" + "" + "\",");
            //181 PUAMT
            strBuilder.append("" + "0,");
            //182 PUCHK
            strBuilder.append("\"" + "" + "\",");
            //183 TYPE
            strBuilder.append("\"" + removeDoubleQuote(pckTyp.toUpperCase()) + "\",");
            //184 CFSTRM
            strBuilder.append("\"" + "00" + "\",");
            //185 FINDST
            strBuilder.append("\"" + fdPort + "\",");
            //186 CTSYN
            strBuilder.append("\"" + "Y" + "\",");
            //187 AIROCN
            strBuilder.append("\"" + "O" + "\",");
            //188 airsrv
            strBuilder.append("\"" + "" + "\",");
            //189 spotrt
            if (null != lclBooking.getSpotWmRate() && !lclBooking.getSpotWmRate().equals("")) {
                strBuilder.append(lclBooking.getSpotWmRate() + ",");
            } else {
                strBuilder.append("0,");
            }
            //190 vessel
            strBuilder.append("\"" + "" + "\",");
            //191 etd
            strBuilder.append("0,");
            //192 eta
            strBuilder.append("0,");
            //193 ssvoy
            strBuilder.append("\"" + "" + "\",");
            //194 amshbl
            strBuilder.append("\"" + "" + "\",");
            //195 it
            if (importBKG) {
                List<LclInbond> lclInbonds = lclBookingPad.getLclFileNumber().getLclInbondList();
                if (lclInbonds.size() > 0) {
                    LclInbond lclInbond = lclInbonds.get(0);
                    strBuilder.append("\"" + removeDoubleQuote(lclInbond.getInbondNo().toUpperCase()) + "\",");
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            //196
            strBuilder.append("\"" + "" + "\",");
            //197
            strBuilder.append("\"" + "0000" + "\",");
            //198
            strBuilder.append("\"" + "0000" + "\",");
            //199
            strBuilder.append("\"" + "0000" + "\",");
            //200
            strBuilder.append("\"" + "0000" + "\",");//100
            //201
            strBuilder.append("\"" + "0000" + "\",");
            //202
            strBuilder.append("\"" + "0000" + "\",");
            //203
            strBuilder.append("\"" + "0000" + "\",");
            //204
            strBuilder.append("\"" + "0000" + "\",");
            //205
            strBuilder.append("\"" + "0000" + "\",");
            //206
            strBuilder.append("\"" + "0000" + "\",");
            //207
            strBuilder.append("\"" + "0000" + "\",");
            //208
            strBuilder.append("\"" + "0000" + "\",");
            //209
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("0,");
            strBuilder.append("\"" + "" + "\",");
            strBuilder.append("0,");
            strBuilder.append("0,");
            //236
            strBuilder.append("\"" + "000000" + "\",");
            //237
            strBuilder.append("0,");
            strBuilder.append("\"" + "N" + "\",");
            strBuilder.append("\"" + "N" + "\",");
            strBuilder.append("\"" + "N" + "\",");
            //241
            strBuilder.append("\"" + "" + "\",");
            //242
            strBuilder.append("\"" + "" + "\",");
            //243
            strBuilder.append("\"" + "" + "\",");
            //244
            strBuilder.append("\"" + "N" + "\",");
            //245
            strBuilder.append("\"" + "" + "\",");
            //246
            strBuilder.append("\"" + "" + "\",");
            //247
            strBuilder.append("\"" + "Y" + "\",");
            //248 DRCBM
            strBuilder.append("\"" + cbm + "\",");
            //249 DRKLS
            strBuilder.append("\"" + kgs + "\",");
            //250 HOUSEN
            strBuilder.append("\"" + "" + "\",");
            //251 HOUSE
            strBuilder.append("\"" + "00000" + "\",");
            //252 STGEO
            if (lclBookingPad != null && lclBooking.getLclFileNumber().getLclBookingImport() != null && lclBooking.getLclFileNumber().getLclBookingImport().getIpiCfsAcctNo() != null) {
                if (lclBooking.getLclFileNumber().getLclBookingImport().getIpiCfsAcctNo().getEciAccountNo() != null) {
                    strBuilder.append("\"" + removeDoubleQuote(lclBooking.getLclFileNumber().getLclBookingImport().getIpiCfsAcctNo().getEciAccountNo().toUpperCase()) + "\",");
                } else {

                    strBuilder.append("\"" + "00000" + "\",");
                }
            } else {
                strBuilder.append("\"" + "00000" + "\",");
            }
            //253 SCHDST
            strBuilder.append("\"" + pod + "\",");
            //254 SCHFND
            strBuilder.append("\"" + fd + "\",");
            //255 SEDYN
            strBuilder.append("\"" + "" + "\",");
            //256 BOOKTM
            if (null != lclBooking.getTerminal()) {
                strBuilder.append("\"" + lclBooking.getTerminal().getTrmnum() + "\",");
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            LclSsHeader bookedVoyage = lclBooking.getBookedSsHeaderId();
            //257 BOOKVY
            if (importBKG) {
                if (null != bookedVoyage && null != bookedVoyage.getVesselSsDetail()
                        && CommonUtils.isNotEmpty(bookedVoyage.getVesselSsDetail().getSpReferenceNo())) {
                    strBuilder.append("\"" + bookedVoyage.getVesselSsDetail().getSpReferenceNo() + "\",");
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            //258 STRDTE
            strBuilder.append("0,");
            //259 TMCUDT
            if (importBKG) {
                if (null != lclBooking.getPooWhseLrdt()) {
                    strBuilder.append(DateUtils.parseDateToString(lclBooking.getPooWhseLrdt()).replaceAll("/", "") + ",");
                } else {
                    strBuilder.append("0,");
                }
            } else {
                if (null != lclBooking.getPooWhseLrdt()) {
                    strBuilder.append(DateUtils.formatDateToYYYYMMDD(lclBooking.getPooWhseLrdt()).replaceAll("/", "") + ",");
                } else {
                    strBuilder.append("0,");
                }
            }
            //260 APPDUE
            if (importBKG) {
                if (null != bookedVoyage && null != bookedVoyage.getVesselSsDetail()
                        && null != bookedVoyage.getVesselSsDetail().getStd()) {
                    strBuilder.append(DateUtils.parseDateToString(bookedVoyage.getVesselSsDetail().getStd()).replaceAll("/", "") + ",");
                } else {
                    strBuilder.append("0,");
                }
            } else {
                if (null != bookedVoyage && null != bookedVoyage.getVesselSsDetail()
                        && null != bookedVoyage.getVesselSsDetail().getStd()) {
                    strBuilder.append(DateUtils.formatDateToYYYYMMDD(bookedVoyage.getVesselSsDetail().getStd()).replaceAll("/", "") + ",");
                } else {
                    strBuilder.append("0,");
                }
            }
            //261 BRLWGT
            strBuilder.append("0,");
            //262 RELOVR
            strBuilder.append("\"" + "N" + "\",");
            //263 UNUS07
            strBuilder.append("\"" + "" + "\",");
            //264 STOWBY
            strBuilder.append("\"" + "" + "\",");
            //265 TMDOBL
            if (importBKG) {
                strBuilder.append("\"" + "" + "\",");
            } else {
                if (null != lclBooking.getTerminal() && null != lclBooking.getTerminal().getTrmnum() && !lclBooking.getTerminal().getTrmnum().equals("")) {
                    strBuilder.append("\"" + lclBooking.getTerminal().getTrmnum() + "\",");
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            }
            //266 PONUM
            strBuilder.append("\"" + "" + "\",");
            //267 BOOKNM
            if (null != lclBooking.getClientContact() && null != lclBooking.getClientContact().getContactName() && !lclBooking.getClientContact().getContactName().trim().equals("")) {
                strBuilder.append("\"" + lclBooking.getClientContact().getContactName().toUpperCase() + " " + lclBooking.getClientContact().getPhone1() + "\",");
            } else if (null != lclBooking.getShipContact() && null != lclBooking.getShipContact().getContactName() && !lclBooking.getShipContact().getContactName().trim().equals("")) {
                strBuilder.append("\"" + lclBooking.getShipContact().getContactName().toUpperCase() + " " + lclBooking.getShipContact().getPhone1() + "\",");
            } else if (null != lclBooking.getFwdContact() && null != lclBooking.getFwdContact().getContactName() && !lclBooking.getFwdContact().getContactName().trim().equals("")) {
                strBuilder.append("\"" + lclBooking.getFwdContact().getContactName().toUpperCase() + " " + lclBooking.getFwdContact().getPhone1() + "\",");
            } else if (null != lclBooking.getConsContact() && null != lclBooking.getConsContact().getContactName() && !lclBooking.getConsContact().getContactName().trim().equals("")) {
                strBuilder.append("\"" + lclBooking.getConsContact().getContactName().toUpperCase() + " " + lclBooking.getConsContact().getPhone1() + "\",");
            } else {

                strBuilder.append("\"" + "" + "\",");
            }
            //268 SECCD4
            strBuilder.append("\"" + "0000" + "\",");
            //269 SECAM4
            strBuilder.append("0,");
            //270 UPSYN
            strBuilder.append("\"" + "N" + "\",");
            //271 SUPNUM
            strBuilder.append("\"" + "00000" + "\",");
            //272 HARDCP
            if (lclBooking.getLclFileNumber().getLclBookingExport() != null && lclBooking.getLclFileNumber().getLclBookingExport().getAes()) {
                strBuilder.append("\"" + "Y" + "\",");
            } else {
                strBuilder.append("\"" + "N" + "\",");
            }
            //273 COMPAN
            strBuilder.append("\"" + "03" + "\",");
            //274 BKCEML
            if (null != lclBooking.getClientContact() && null != lclBooking.getClientContact().getContactName() && !lclBooking.getClientContact().getContactName().trim().equals("")) {
                strBuilder.append("\"" + lclBooking.getClientContact().getContactName().toUpperCase() + " " + lclBooking.getClientContact().getPhone1() + "\",");
            } else if (null != lclBooking.getShipContact() && null != lclBooking.getShipContact().getContactName() && !lclBooking.getShipContact().getContactName().trim().equals("")) {
                strBuilder.append("\"" + lclBooking.getShipContact().getContactName().toUpperCase() + " " + lclBooking.getShipContact().getPhone1() + "\",");
            } else if (null != lclBooking.getFwdContact() && null != lclBooking.getFwdContact().getContactName() && !lclBooking.getFwdContact().getContactName().trim().equals("")) {
                strBuilder.append("\"" + lclBooking.getFwdContact().getContactName().toUpperCase() + " " + lclBooking.getFwdContact().getPhone1() + "\",");
            } else if (null != lclBooking.getConsContact() && null != lclBooking.getConsContact().getContactName() && !lclBooking.getConsContact().getContactName().trim().equals("")) {
                strBuilder.append("\"" + lclBooking.getConsContact().getContactName().toUpperCase() + " " + lclBooking.getConsContact().getPhone1() + "\",");
            } else {

                strBuilder.append("\"" + "" + "\",");
            }
            //275 BKCNOT
            strBuilder.append("\"" + "P" + "\",");
            //276 PUCEML
            if (lclBookingPad != null) {
                strBuilder.append("\"" + lclBookingPad.getPickupContact().getEmail1().toUpperCase() + "\",");
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            //277 DDINST
            if (lclBookingPad != null && lclBookingPad.getDeliveryInstructions() != null && !"".equals(lclBookingPad.getDeliveryInstructions())) {
                String[] chars = lclBookingPad.getDeliveryInstructions().split("\n");
                if (chars[0].length() > 59) {
                    strBuilder.append("\"" + removeDoubleQuote(chars[0].substring(0, 59).toUpperCase()) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(chars[0].toUpperCase()) + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            //278
            strBuilder.append("\"" + "00000000" + "\",");
            //279 DDPUNO
            if (null != lclBookingPad && null != lclBookingPad.getDeliveryReferenceNo()) {
                if (lclBookingPad.getDeliveryReferenceNo().length() > 14) {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getDeliveryReferenceNo().toUpperCase().substring(0, 14)) + "\",");
                } else {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getDeliveryReferenceNo().toUpperCase()) + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }
            //280 DDSCAC
            if (null != lclBookingPad) {
                if (null != lclBookingPad.getScac() && !lclBookingPad.getScac().equals("")) {
                    strBuilder.append("\"" + removeDoubleQuote(lclBookingPad.getScac().toUpperCase()) + "\",");
                } else {
                    strBuilder.append("\"" + "" + "\",");
                }
            } else {
                strBuilder.append("\"" + "" + "\",");
            }

            strBuilder.append("\n");
            return strBuilder.toString();
        }

    

    private String removeDoubleQuote(String value) {
        value = (null != value) ? value.replaceAll("\n", "") : value;
        value = (null != value) ? value.replaceAll("\"", "") : value;
        value = (null != value) ? value.replaceAll("\t", "") : value;
        value = (null != value) ? value.replaceAll("\r", "") : value;
        value = (null != value) ? value.replaceAll("'", "") : value;
        return value;
    }

    public List<String> splitString(String string) {
        List<String> strings = new ArrayList<String>();
        Pattern wrapText = Pattern.compile(".{0,57}(?:\\S(?:-| |$)|$)");
        Matcher matcher = wrapText.matcher(string);
        while (matcher.find()) {
            if (CommonUtils.isNotEmpty(matcher.group())) {
                strings.add(matcher.group());
            }
        }
        return strings;
    }
}
