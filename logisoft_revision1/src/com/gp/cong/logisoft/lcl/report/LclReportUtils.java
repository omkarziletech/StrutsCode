/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.lcl.report;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.NumberUtils;
import com.gp.cong.logisoft.domain.lcl.Lcl3pRefNo;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclBookingHsCode;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.hibernate.dao.lcl.Lcl3pRefNoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclInbondsDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import com.logiware.hibernate.domain.ArRedInvoice;
import java.util.List;

/**
 *
 * @author Mei
 */
public class LclReportUtils {

    public String concateHazmatDetails(LclBookingHazmat lclBookingHazmat) throws Exception {
        StringBuilder hazmatValue = new StringBuilder();
        if (CommonFunctions.isNotNull(lclBookingHazmat) && CommonFunctions.isNotNull(lclBookingHazmat.getHazmatDeclarations())) {
            hazmatValue.append(lclBookingHazmat.getHazmatDeclarations().replaceAll("[\n\r]", ""));
        } else {
            if (lclBookingHazmat.getReportableQuantity()) {
                hazmatValue.append("REPORTABLE QUANTITY, ");
            }
            if (CommonFunctions.isNotNull(lclBookingHazmat.getUnHazmatNo())) {
                hazmatValue.append("UN ").append(lclBookingHazmat.getUnHazmatNo());
            }
            if (CommonFunctions.isNotNull(lclBookingHazmat.getProperShippingName())) {
                hazmatValue.append(" ").append(lclBookingHazmat.getProperShippingName());
                hazmatValue.append(CommonUtils.isNotEmpty(lclBookingHazmat.getTechnicalName()) ? "(" + lclBookingHazmat.getTechnicalName().toUpperCase() + ")" : "");
            }
            if (CommonFunctions.isNotNull(lclBookingHazmat.getImoPriClassCode())) {
                hazmatValue.append(" ");
                hazmatValue.append("CLASS ").append(lclBookingHazmat.getImoPriClassCode());
                hazmatValue.append(CommonUtils.isNotEmpty(lclBookingHazmat.getImoPriSubClassCode()) ? "(" + lclBookingHazmat.getImoPriSubClassCode() + ") " : "");
                hazmatValue.append(CommonUtils.isNotEmpty(lclBookingHazmat.getImoSecSubClassCode()) ? "(" + lclBookingHazmat.getImoSecSubClassCode() + ") " : "");
            }
            if (CommonFunctions.isNotNull(lclBookingHazmat.getPackingGroupCode())) {
                hazmatValue.append(" ").append("PG ").append(lclBookingHazmat.getPackingGroupCode());
            }
            if (CommonFunctions.isNotNull(lclBookingHazmat.getFlashPoint())) {
                hazmatValue.append(" ").append("FLASH POINT (").append(lclBookingHazmat.getFlashPoint()).append(" DEG C)");
            }
            if (CommonUtils.isNotEmpty(lclBookingHazmat.getOuterPkgNoPieces())) {
                hazmatValue.append(" ").append(lclBookingHazmat.getOuterPkgNoPieces()).append(" ");
                hazmatValue.append(CommonUtils.isNotEmpty(lclBookingHazmat.getOuterPkgComposition()) ? lclBookingHazmat.getOuterPkgComposition() : " ").append(" ");
                double outerPkg = lclBookingHazmat.getOuterPkgNoPieces().doubleValue();
                String pluralValue = "";
                LclHazmatDAO lclHazmatDAO = new LclHazmatDAO();
                if ((int) outerPkg > 1) {
                    pluralValue = lclHazmatDAO.getPluralByPkgType(lclBookingHazmat.getOuterPkgType());
                    if (CommonUtils.isNotEmpty(pluralValue)) {
                        hazmatValue.append(lclBookingHazmat.getOuterPkgType().toUpperCase()).append(pluralValue);
                    }
                } else {
                    hazmatValue.append(lclBookingHazmat.getOuterPkgType().toUpperCase());
                }
                if (CommonUtils.isNotEmpty(lclBookingHazmat.getInnerPkgNoPieces())) {
                    hazmatValue.append(" ").append(" CONTAINING ").append(lclBookingHazmat.getInnerPkgNoPieces()).append(" ");
                    hazmatValue.append(CommonUtils.isNotEmpty(lclBookingHazmat.getInnerPkgComposition()) ? lclBookingHazmat.getInnerPkgComposition() : " ").append(" ");
                    double innerPkg = lclBookingHazmat.getInnerPkgNoPieces().doubleValue();
                    pluralValue = "";
                    if ((int) innerPkg > 1) {
                        pluralValue = lclHazmatDAO.getPluralByPkgType(lclBookingHazmat.getInnerPkgType());
                        if (CommonUtils.isNotEmpty(pluralValue)) {
                            hazmatValue.append(lclBookingHazmat.getInnerPkgType().toUpperCase()).append(pluralValue);
                        }
                    } else {
                        hazmatValue.append(lclBookingHazmat.getInnerPkgType().toUpperCase()).append(" ");
                    }
                    if (CommonFunctions.isNotNull(lclBookingHazmat.getInnerPkgNwtPiece())) {
                        hazmatValue.append(" @ ").append(lclBookingHazmat.getInnerPkgNwtPiece()).append(" ");
                    }
                    hazmatValue.append(CommonUtils.isNotEmpty(lclBookingHazmat.getInnerPkgUom()) ? lclBookingHazmat.getInnerPkgUom() + " EACH " : " ").append(" ");
                }
            }
            if (CommonFunctions.isNotNull(lclBookingHazmat.getTotalNetWeight())) {
                hazmatValue.append(" ").append("TOTAL NET WEIGHT ");
                hazmatValue.append(NumberUtils.truncateTwoDecimalWithoutRoundUp(lclBookingHazmat.getTotalNetWeight().doubleValue())).append(" KGS");
            }
            if (CommonFunctions.isNotNull(lclBookingHazmat.getTotalGrossWeight())) {
                hazmatValue.append(" ").append("TOTAL GROSS WEIGHT ");
                hazmatValue.append(NumberUtils.truncateTwoDecimalWithoutRoundUp(lclBookingHazmat.getTotalGrossWeight().doubleValue())).append(" KGS");
            }
            if (CommonFunctions.isNotNull(lclBookingHazmat.getLiquidVolume()) && (!(lclBookingHazmat.getLiquidVolume().doubleValue() == 0.000))) {
                hazmatValue.append(" ").append("TOTAL VOLUME ").append(NumberUtils.truncateTwoDecimalWithoutRoundUp(lclBookingHazmat.getLiquidVolume().doubleValue())).append(" ");
//                double volume = lclBookingHazmat.getLiquidVolume().doubleValue();
                hazmatValue.append(lclBookingHazmat.getLiquidVolumeLitreorGals());
            }
            if (lclBookingHazmat.getMarinePollutant()) {
                hazmatValue.append(" ").append("MARINE POLLUTANT");
            }
            if (lclBookingHazmat.getExceptedQuantity()) {
                hazmatValue.append(" ").append("EXCEPTED QUANTITY");
            }
            if (lclBookingHazmat.getLimitedQuantity()) {
                hazmatValue.append(" ").append("LIMITED QUANTITY");
            }
            if (lclBookingHazmat.getInhalationHazard()) {
                hazmatValue.append(" ").append("INHALATION HAZARD");
            }
            if (lclBookingHazmat.getResidue()) {
                hazmatValue.append(" ").append("RESIDUE");
            }
            if (CommonUtils.isNotEmpty(lclBookingHazmat.getEmsCode())) {
                hazmatValue.append(" ").append("EMS ").append(lclBookingHazmat.getEmsCode().toUpperCase()).append(" ");
            }
//            if (null != lclBookingHazmat.getEmergencyContact()) {
//                if (CommonUtils.isNotEmpty(lclBookingHazmat.getEmergencyContact().getContactName())) {
//                    hazmatValue.append(lclBookingHazmat.getEmergencyContact().getContactName()).append(", ");
//                }
//                if (CommonUtils.isNotEmpty(lclBookingHazmat.getEmergencyContact().getPhone1())) {
//                    hazmatValue.append(lclBookingHazmat.getEmergencyContact().getPhone1());
//                }
//            }
        }
        return hazmatValue.toString();
    }

    public StringBuilder appendHazmat(List consFileIdList, LclHazmatDAO hazmatDAO) throws Exception {
        StringBuilder hazmatDec = new StringBuilder();
        List<LclBookingHazmat> hazmatList = hazmatDAO.findByFileId(consFileIdList);
        if (hazmatList != null && !hazmatList.isEmpty()) {
            int i = 0;
            for (LclBookingHazmat hazmat : hazmatList) {
                i++;
                hazmatDec.append(concateHazmatDetails(hazmat)).append(i != hazmatList.size() ? "\n\n" : "");
            }
        }
        return hazmatDec;
    }

    public StringBuilder appendHazmatForFreightInvoice(List consFileIdList, LclHazmatDAO hazmatDAO) throws Exception {
        StringBuilder hazmatDec = new StringBuilder();
        List<LclBookingHazmat> hazmatList = hazmatDAO.findByFileId(consFileIdList);
        if (hazmatList != null && !hazmatList.isEmpty()) {
            int i = 0;
            for (LclBookingHazmat hazmat : hazmatList) {
                i++;
                if (hazmat.getPrintOnHouseBl()) {
                    hazmatDec.append(concateHazmatDetails(hazmat)).append(i != hazmatList.size() ? "\n\n" : "");
                }
            }
        }
        return hazmatDec;
    }

    public StringBuilder appendAes(List consFileIdList, Lcl3pRefNoDAO _3pRefNoDAO) throws Exception {
        StringBuilder aesDesc = new StringBuilder();
        List<Lcl3pRefNo> aesList = _3pRefNoDAO.getAesTypeList(consFileIdList, LclReportConstants.AES_NUMBER);
        if (aesList.isEmpty()) {
            aesList = _3pRefNoDAO.getAesTypeList(consFileIdList, LclReportConstants.AES_NUMBER_EXCEPT);
        }
        if (aesList != null && !aesList.isEmpty()) {
            StringBuilder aesAppend = new StringBuilder();
            for (Lcl3pRefNo lcl3pRefNoaes : aesList) {
                aesAppend.append("AES:").append(lcl3pRefNoaes.getReference()).append("\n");
            }
            aesDesc.append(aesAppend);
        }
        return aesDesc;
    }

    public StringBuilder appendNcm(List consFileIdList, Lcl3pRefNoDAO _3pRefNoDAO) throws Exception {
        StringBuilder ncmDesc = new StringBuilder();
        List<Lcl3pRefNo> ncmList = _3pRefNoDAO.getAesTypeList(consFileIdList, LclReportConstants.NCM_REF);
        if (ncmList != null && !ncmList.isEmpty()) {
            StringBuilder ncmAppend = new StringBuilder();
            for (Lcl3pRefNo ncm : ncmList) {
                ncmAppend.append("NCM:").append(ncm.getReference()).append("\n");
            }
            ncmDesc.append(ncmAppend);
        }
        return ncmDesc;
    }
    public StringBuilder appendCP(List consFileIdList, Lcl3pRefNoDAO _3pRefNoDAO) throws Exception {
        StringBuilder cpAppend = new StringBuilder();
        List<Lcl3pRefNo> cpList = _3pRefNoDAO.getAesTypeList(consFileIdList, LclReportConstants.CP_NO);
        if (cpList != null && !cpList.isEmpty()) {
        cpAppend.append("PO NO ").append("\n").append("").append("\n");
            for (Lcl3pRefNo cp : cpList) {
                cpAppend.append(cp.getReference()).append("\n").append("").append("\n");;
            }
        }
        return cpAppend;
    }

    public StringBuilder appendInvoiceNo(String conoslidateFileNumber, String blNumber) throws Exception {
        StringBuilder inVoiceNoAppend = new StringBuilder();
        if (CommonUtils.isNotEmpty(conoslidateFileNumber)) {
            conoslidateFileNumber = conoslidateFileNumber + "," + blNumber;
        } else {
            conoslidateFileNumber = blNumber;
        }
        List<String> arInvoiceNoList = new ArRedInvoiceDAO().getInvoiceNo(conoslidateFileNumber);
        if (arInvoiceNoList != null && !arInvoiceNoList.isEmpty()) {
        inVoiceNoAppend.append("INV NO ").append("\n").append("").append("\n");
            for (String arInvoiceNo : arInvoiceNoList) {
                inVoiceNoAppend.append(arInvoiceNo).append("\n");
            }
        }
        return inVoiceNoAppend;
    }

    public StringBuilder appendHsCode(List consFileIdList, LclBookingHsCodeDAO bkgHsCodeDAO) throws Exception {
        StringBuilder hsCodeDesc = new StringBuilder();
        List<LclBookingHsCode> lclBookingHsCodeList = bkgHsCodeDAO.getHsCodeList(consFileIdList);
        if (null != lclBookingHsCodeList && !lclBookingHsCodeList.isEmpty()) {
            StringBuilder hsCode = new StringBuilder();
            for (LclBookingHsCode lclBookingHsCode : lclBookingHsCodeList) {
                hsCode.append("HS:").append(lclBookingHsCode.getCodes()).append("\n");
            }
            hsCodeDesc.append(hsCode);
        }
        return hsCodeDesc;
    }

    public StringBuilder appendInbond(List consFileIdList, LclInbondsDAO inbondDAO) throws Exception {
        StringBuilder inbondDesc = new StringBuilder();
        List<LclInbond> inbondList = inbondDAO.getInbondList(consFileIdList);
        if (inbondList != null && !inbondList.isEmpty()) {
            StringBuilder inbondAppend = new StringBuilder();
            for (LclInbond inbond : inbondList) {
                if (CommonUtils.isNotEmpty(inbond.getInbondType())) {
                    inbondAppend.append(inbond.getInbondType()).append("/");
                }
                inbondAppend.append(inbond.getInbondNo()).append(" ");
            }
            inbondDesc.append(inbondAppend);
        }
        return inbondDesc;
    }
}
