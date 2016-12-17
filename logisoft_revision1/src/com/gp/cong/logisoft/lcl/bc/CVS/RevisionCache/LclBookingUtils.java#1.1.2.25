package com.gp.cong.logisoft.lcl.bc;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.ConstantsInterface;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import static com.gp.cong.lcl.common.constant.LclCommonConstant.LCL_IMPORT;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHsCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportAmsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingImportDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPadDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclRemarksDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LCLBookingForm;
import com.logiware.accounting.dao.LclManifestDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 *
 * @author meiyazhakan.r
 */
public class LclBookingUtils implements LclCommonConstant, ConstantsInterface {

    private static final Logger log = Logger.getLogger(LclBookingUtils.class);

    public String getImpUnitDispostionDesc(Long unitId, Long detailId) throws Exception {
        LclUnitSsDispoDAO lclUnitSsDispoDAO = new LclUnitSsDispoDAO();
        List dispositionList = lclUnitSsDispoDAO.getUnitDispoDetails(String.valueOf(unitId), detailId);
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><BODY>");
        sb.append("<table width=\"100%\">");
        sb.append("<tr>");
        sb.append("<td colspan='2' align='left'>");
        sb.append("<FONT size='2' COLOR=#008000>");
        sb.append("<b>");
        sb.append("Disposition");
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append("<td align='left' width=\"70%\">");
        sb.append("<FONT size='2' COLOR=#F01E1E>");
        sb.append("<b>");
        sb.append("Description");
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("<td align='left'>");
        sb.append("<FONT size='2' COLOR=#F01E1E>");
        sb.append("<b>");
        sb.append("Date/Time");
        sb.append("</b>");
        sb.append("</FONT>");
        sb.append("</td>");
        sb.append("</tr>");
        if (!dispositionList.isEmpty()) {
            for (int i = 0; i < dispositionList.size(); i++) {
                Object[] description = (Object[]) dispositionList.get(i);
                sb.append("<tr>");
                sb.append("<td>");
                sb.append("<b>");
                sb.append(description[0]);
                sb.append("</b>");
                sb.append("</td>");
                sb.append("<td>");
                sb.append("<b>");
                //  Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(description[1].toString());
                sb.append(description[1]);
                sb.append("</b>");
                sb.append("</td>");
                sb.append("</tr>");
            }
        }
        sb.append("</table>");
        sb.append("</BODY></HTML>");
        return sb.toString();
    }

    public String[] validateForChargeandCost(String chargeCode, String shipmentType) throws Exception {
        String data[] = new String[2];
        GlMappingDAO glMappingDAO = new GlMappingDAO();
        if (glMappingDAO.isChargeCodeFound(chargeCode, TRANSACTION_TYPE_ACCOUNT_RECEIVABLE, shipmentType)) {
            data[0] = "Y";
        } else {
            data[0] = "N";
        }
        if (glMappingDAO.isChargeCodeFound(chargeCode, TRANSACTION_TYPE_ACCRUALS, shipmentType)) {
            data[1] = "Y";
        } else {
            data[1] = "N";
        }
        return data;
    }

    public String revertToQuote(Long fileId, String fileNo, String moduleName, User loginUser) throws Exception {
        String errorMessage = "";
        try {
            if (CommonUtils.isNotEmpty(fileId)) {
                LclBookingImportDAO lclBookingImportDAO = new LclBookingImportDAO();
                LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
                LclCostChargeDAO lclCostChargeDAO = new LclCostChargeDAO();
                LclBookingPadDAO lclBookingPadDAO = new LclBookingPadDAO();
                LclBookingHsCodeDAO lclBookingHsCodeDAO = new LclBookingHsCodeDAO();
                LclBookingImportAmsDAO lclBookingImportAmsDAO = new LclBookingImportAmsDAO();
                LclHazmatDAO lclHazmatDAO = new LclHazmatDAO();
                LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
                LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
                LclRemarksDAO lclRemarksDAO = new LclRemarksDAO();
                LclManifestDAO lclManifestDAO = new LclManifestDAO();
                lclManifestDAO.deleteLclAccruals(fileNo);
                lclBookingDAO.deleteBkgByFileNumber(fileId);//Booking
                lclBookingImportDAO.deleteBkgImportByFileNumber(fileId);//BookingImport
                lclCostChargeDAO.deleteBkgAcByFileNumber(fileId);//BookingAc
                lclBookingImportAmsDAO.deleteBkgAmsByFileNumber(fileId);//BookingAms
                lclBookingPadDAO.deleteBkgPadByFileNumber(fileId);//BookingPad
                lclBookingHsCodeDAO.deleteBkgHsCodeByFileNumber(fileId);//BookingHsCode
                new LclBookingDispoDAO().deleteDisposition(fileId);
                lclHazmatDAO.deleteBkgHazmetByFileNumber(fileId);//BookingHazmet
                lclBookingPieceDAO.deleteBkgPieceByFileNumber(fileId);//BookingPiece,Whse,Detail
                new LclBookingHotCodeDAO().deleteHotCodeByFileId(fileId);//delete BookingHotCode
                lclFileNumberDAO.updateFileStatus(fileId, FILE_STATE_QUOTE, FILE_STATE_BOOKING);
                lclRemarksDAO.insertLclRemarks(fileId, REMARKS_TYPE_T, "ReverseToQuote", loginUser.getUserId());
                errorMessage = "revertToQuote";
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.info("Error on revertToQuote method in LclBookingUtils", e);
        }
        return errorMessage;
    }

    public String getExpDispoDesc(Long fileId, String fileStatus, String bookingType) throws Exception {
        String dispoToolValues[] = new String[4];
        StringBuilder sb = new StringBuilder();
        List dispoBookList = new LclBookingDispoDAO().bookDispoStatus(fileId, bookingType);
        if ("B".equalsIgnoreCase(fileStatus) || "WV".equalsIgnoreCase(fileStatus)
                || "WU".equalsIgnoreCase(fileStatus) || "W".equalsIgnoreCase(fileStatus) || "R".equalsIgnoreCase(fileStatus) || "L".equalsIgnoreCase(fileStatus)
                || "M".equalsIgnoreCase(fileStatus) || "PR".equalsIgnoreCase(fileStatus)) {
            sb.append("<HTML><BODY>");
            sb.append("<table width=\"100%\">");
            sb.append("<tr>");
            sb.append("<td colspan='2' align='left'>");
            sb.append("<FONT size='2' COLOR=#008000>");
            sb.append("<b>");
            sb.append("Disposition");
            sb.append("</b>");
            sb.append("</FONT>");
            sb.append("</td>");
            sb.append("</tr>");
            sb.append("<tr>");
            sb.append("<td align='left' width=\"32%\">");
            sb.append("<FONT size='2' COLOR=#F01E1E>");
            sb.append("<b>");
            sb.append("Description");
            sb.append("</b>");
            sb.append("</FONT>");
            sb.append("</td>");
            sb.append("<td align='left' width=\"36%\">");
            sb.append("<FONT size='2' COLOR=#F01E1E>");
            sb.append("<b>");
            sb.append("City");
            sb.append("</b>");
            sb.append("</FONT>");
            sb.append("</td>");
            sb.append("<td align='left' width=\"32%\">");
            sb.append("<FONT size='2' COLOR=#F01E1E>");
            sb.append("<b>");
            sb.append("Date/Time");
            sb.append("</b>");
            sb.append("</FONT>");
            sb.append("</td>");
            sb.append("<td align='left' width=\"32%\">");
            sb.append("<FONT size='2' COLOR=#F01E1E>");
            sb.append("<b>");
            sb.append("Warehouse");
            sb.append("</b>");
            sb.append("</FONT>");
            sb.append("</td>");
            sb.append("</tr>");
            if (!dispoBookList.isEmpty()) {
                for (int i = dispoBookList.size() - 1; i >= 0; i--) {
                    Object[] dispoObj = (Object[]) dispoBookList.get(i);
                    if (dispoObj[0] != null && !dispoObj[0].toString().trim().equals("")) {
                        dispoToolValues[0] = dispoObj[0].toString();
                    }
                    if (dispoObj[1] != null && !dispoObj[1].toString().trim().equals("")) {
                        dispoToolValues[1] = dispoObj[1].toString();
                    }
                    if (dispoObj[2] != null && !dispoObj[2].toString().trim().equals("")) {
                        dispoToolValues[2] = (dispoObj[0].equals("INTR"))?"":dispoObj[2].toString();
                    } else {
                        dispoToolValues[2] = "";
                    }
                    if (dispoObj[3] != null && !dispoObj[3].toString().trim().equals("")) {
                        dispoToolValues[3] = dispoObj[3].toString();
                    } else {
                        dispoToolValues[3] = "";
                    }
                    sb.append("<tr>");
                    sb.append("<td>");
                    sb.append("<b>");
                    sb.append(dispoToolValues[0]);
                    sb.append("</b>");
                    sb.append("</td>");
                    sb.append("<td>");
                    sb.append("<b>");
                    sb.append(dispoToolValues[2]);
                    sb.append("</b>");
                    sb.append("</td>");
                    sb.append("<td>");
                    sb.append("<b>");
                    // Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(dispoToolValues[1].toString());
                    sb.append(dispoToolValues[1]);
                    sb.append("</b>");
                    sb.append("</td>");
                    sb.append("<td align='center'>");
                    sb.append("<b>");
                    sb.append(dispoToolValues[3]);
                    sb.append("</b>");
                    sb.append("</td>");
                    sb.append("</tr>");
                }
            }
            sb.append("</table>");
            sb.append("</BODY></HTML>");
        }
        return sb.toString();
    }

    public void getRemarks(LCLBookingForm bookingForm, HttpServletRequest request, Long fileId, String[] remarkTypes,
            LclRemarksDAO lclRemarksDAO) throws Exception {
        List remarks = lclRemarksDAO.getRemarksByTypes(fileId, remarkTypes);
        Map<String, String> remarksMap = new HashMap<String, String>();
        for (Object row : remarks) {
            String remarksStr = "";
            Object[] col = (Object[]) row;
            if (remarksMap.containsKey(col[1].toString())) {
                remarksStr = remarksMap.get(col[1].toString()) + " , " + col[0].toString();
            } else {
                remarksStr = col[0].toString();
            }
            remarksMap.put(col[1].toString(), remarksStr);
        }
        for (Map.Entry<String, String> entry : remarksMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("Priority View")) {
                request.setAttribute("lclRemarksPriority", entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("AutoRates")) {
                request.setAttribute("highVolumeMessage", entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Special Remarks")) {
                bookingForm.setSpecialRemarks(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Special Remarks Pod")) {
                bookingForm.setSpecialRemarksPod(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Internal Remarks")) {
                bookingForm.setInternalRemarks(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Internal Remarks Pod")) {
                bookingForm.setInternalRemarksPod(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("G")) {
                bookingForm.setPortGriRemarks(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Gri Remarks Pod")) {
                bookingForm.setPortGriRemarksPod(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("Loading Remarks")) {
                bookingForm.setRemarksForLoading(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("OSD")) {
                bookingForm.setOsdRemarks(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("E")) {
                bookingForm.setExternalComment(entry.getValue());
            }
            if (entry.getKey().equalsIgnoreCase("SU")) {
                bookingForm.setSuHeadingNote(entry.getValue());
            }
        }
    }
    
    public String[] getFormatBillToCodeImpAndExp(Long fileNumberId, String billToParty, String billingType, String moduleName) throws Exception {
        String[] billToCode = new String[2];
        if (LCL_IMPORT.equalsIgnoreCase(moduleName)) {
            billToCode[0] = CommonUtils.in(billToParty, "F", "S", "A") ? "A" : billToParty;
            billToCode[1] = CommonUtils.in(billToParty, "F", "S", "A") ? "P" : (billingType.isEmpty() && new LCLBookingDAO().getBothBillToCodeFlag(fileNumberId)) || "T".equalsIgnoreCase(billToParty)  ? "C" : billingType;
        } else {
            billToCode[0] = CommonUtils.in(billToParty, "W", "A") ? "S" : CommonUtils.in(billToParty, "C", "N") ? "A" : billToParty;
            billToCode[1] = new LCLBookingDAO().getBothBillToCodeFlag(fileNumberId) ? "B" : CommonUtils.in(billToParty, "W", "A", "T") ? "P"
                    : CommonUtils.in(billToParty, "A", "N") ? "C" : billingType;
        }
        return billToCode;
    }
}
