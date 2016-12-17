package com.gp.cong.lcl.common.constant;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.beans.ImportBookingBean;
import com.gp.cong.logisoft.beans.ImportBookingUnitsBean;
import com.gp.cong.logisoft.beans.ImportUnitsBean;
import com.gp.cong.logisoft.beans.ImportsManifestBean;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclSsHeader;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.LclUnitWhse;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.DispositionDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm;
import com.logiware.hibernate.dao.ArRedInvoiceDAO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 * * @author Meiyazhakan
 */
public class LclImportUtils implements LclCommonConstant {

    public void setVoyageAndUnitValues(LclAddVoyageForm lclAddVoyageForm, HttpServletRequest request) throws Exception {
        if (CommonUtils.isNotEmpty(lclAddVoyageForm.getHeaderId())) {
            LclSsHeaderDAO lclSsHeaderDAO = new LclSsHeaderDAO();
            LclUnitSsDAO lclUnitSsDAO = new LclUnitSsDAO();
            LclSsHeader lclssheader = lclSsHeaderDAO.findById(lclAddVoyageForm.getHeaderId());
            LclSsDetail lclSsDetail = lclssheader.getVesselSsDetail();
            LclUnitSs lclUnitSs = null;
            if (CommonUtils.isNotEmpty(lclssheader.getId())) {
                lclUnitSs = lclUnitSsDAO.getLclUnitSSByLclUnitHeader(null, lclssheader.getId());
                LclUnitWhse lclUnitWhse = lclUnitSs.getLclUnit().getLclUnitWhseList().get(0);
                lclAddVoyageForm.setUnitssId(lclUnitSs.getId());
                lclAddVoyageForm.setUnitId(lclUnitSs.getLclUnit().getId());
                request.setAttribute("lclUnitSS", lclUnitSs);
                request.setAttribute("lclUnit", lclUnitSs.getLclUnit());
                request.setAttribute("lclUnitWhse", lclUnitWhse);
            }
            if (lclSsDetail != null && CommonFunctions.isNotNull(lclSsDetail.getStd())
                    && CommonFunctions.isNotNull(lclSsDetail.getSta())) {
                Long totalTTDays = DateUtils.getDateDiffByTotalDays(lclSsDetail.getStd(), lclSsDetail.getSta());
                lclSsDetail.setTotalTTDays(totalTTDays);
            }
            request.setAttribute("lclUnitSSList", formatImpVoyageList(lclUnitSs, lclSsDetail, request));
            LclUtils lclUtils = new LclUtils();
            String origin = lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getOrigin());
            String destination = lclUtils.getConcatenatedOriginByUnlocation(lclssheader.getDestination());
            request.setAttribute("originValue", origin);
            request.setAttribute("destinationValue", destination);
            request.setAttribute("polUnlocationCode", origin.substring(origin.indexOf("(") + 1, origin.indexOf(")")));
            request.setAttribute("originalOriginName", origin);
            request.setAttribute("originalDestinationName", destination);

            request.setAttribute("originId", lclssheader.getOrigin().getId());
            request.setAttribute("destinationId", lclssheader.getDestination().getId());
            request.setAttribute("originalOriginId", lclssheader.getOrigin().getId());
            request.setAttribute("originalDestinationId", lclssheader.getDestination().getId());
            request.setAttribute("lclssheader", lclssheader);
            List<LclSsDetail> lclSsDetails = new ArrayList<LclSsDetail>();
            lclSsDetails.add(lclSsDetail);
            request.setAttribute("lclSsDetail", lclSsDetails);
        }
        request.setAttribute("lclAddVoyageForm", lclAddVoyageForm);
        request.setAttribute("reportPreviewLocation", LoadLogisoftProperties.getProperty("report.preview.location"));
    }

    public LclUnitSs formatImpVoyageList(LclUnitSs lclUnitSs, LclSsDetail lclSsDetail,
            HttpServletRequest request) throws Exception {
        if (lclUnitSs != null) {
            ArRedInvoiceDAO arRedInvoiceDAO = new ArRedInvoiceDAO();
            DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
            String dispoCode = new LclUnitSsDispoDAO().getDispositionByDetailId(lclUnitSs.getLclUnit().getId(), lclSsDetail.getId());
            if (CommonUtils.isNotEmpty(dispoCode)) {
                lclUnitSs.setDispoDesc(new DispositionDAO().findByEliteCode(dispoCode).getDescription());
            }
            lclUnitSs.setDispoCode(dispoCode);
            request.setAttribute("dispoCode", dispoCode);
            lclUnitSs.setScanAttachStatus(documentStoreLogDAO.isScanCountChecked(LCL_UNITSIMP_SCREENNAME,
                    lclUnitSs.getLclUnit().getUnitNo() + "--" + lclUnitSs.getLclSsHeader().getScheduleNo()));
            String arCount[] = arRedInvoiceDAO.isLclARInvoice(lclUnitSs.getId().toString(),
                    AR_RED_INVOICE_SCREENNAME_VOYAGE);
            if (!arCount[0].equalsIgnoreCase("0") || !arCount[1].equalsIgnoreCase("0")) {
                if (Integer.parseInt(arCount[0]) > Integer.parseInt(arCount[1])) {
                    lclUnitSs.setStatusSendEdi("Posted");
                } else {
                    lclUnitSs.setStatusSendEdi("Open");
                }
            } else {
                lclUnitSs.setStatusSendEdi("Empty");
            }
        }
        return lclUnitSs;
    }

    public void setMultiUnitTabHtmlDesign(HttpServletRequest request, ImportBookingUnitsBean importBookingUnitsBean) throws Exception {
        StringBuilder sb = new StringBuilder();
        StringBuilder tabBox = new StringBuilder();
        StringBuilder tab = new StringBuilder();
        String path = request.getContextPath();
        RoleDuty roleDuty = (RoleDuty) request.getSession().getAttribute("roleDuty");
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        tab.append("<div class=\"tab-block\">");
        int count = 1;
        tabBox.append("<div class=\"tab-box\">");
        for (ImportUnitsBean iub : importBookingUnitsBean) {
            LclSsHeader lclssheader = new LclSsHeaderDAO().findById(Long.parseLong(iub.getHeaderId()));
            if (count == 1) {
                tabBox.append("<a href=\"javascript:;\" id=\"cont-").append(count).append("\" class=\"tabLink ").append("activeLink\">");
            } else {
                tabBox.append("<a href=\"javascript:;\" id=\"cont-").append(count).append("\" class=\"tabLink\">");
            }
            tabBox.append(iub.getUnitNo());
            tabBox.append("</a>");
            if (count > 1) {
                sb.append("<div class=\"tabcontent ").append("hide\" id=\"cont-").append(count).append("-1\">");
            } else {
                sb.append("<div class=\"tabcontent \" id=\"cont-").append(count).append("-1\">");
            }
            sb.append("<table class=\"tabTable\" border=\"1\"  style=\"border:1px solid #dcdcdc;width:100%\">");
            sb.append("<tr class=\"importjavaDesign\" >");
            sb.append("<td>Unit#</td>");
            sb.append("<td>Edi</td>");
            sb.append("<td>Status</td>");
            sb.append("<td>Dispo</td>");
            sb.append("<td>Size</td>");
            sb.append("<td>CFT</td>");
            sb.append("<td>CBM</td>");
            sb.append("<td>LBS</td>");
            sb.append("<td>KGS</td>");
            sb.append("<td>MasterBL#</td>");
            sb.append("<td>Coloader</td>");
            sb.append("<td>CFS Dev</td>");
            sb.append("<td>Unit Tm</td>");
            sb.append("<td>IT#</td>");
            sb.append("<td>Action</td>");
            sb.append("</tr>");
            if (count % 2 == 0) {
                sb.append("<tr class=\"even\">");
            } else {
                sb.append("<tr class=\"odd\">");
            }
            sb.append("<td>");
            sb.append(iub.getUnitNo());
            sb.append("</td>");
            sb.append("<td>");
            String ediTemp = "";
            if (iub.getEdi().equalsIgnoreCase("1")) {
                ediTemp = "Y";
            } else {
                ediTemp = "N";
            }
            sb.append(ediTemp);
            sb.append("</td>");
            sb.append("<td>");
            String unitStatus = "";
            if (iub.getUnitstatus().equalsIgnoreCase("E")) {
                unitStatus = "Active";
            } else if (iub.getUnitstatus().equalsIgnoreCase("C")) {
                unitStatus = "Completed";
            } else if (iub.getUnitstatus().equalsIgnoreCase("M")) {
                unitStatus = "Manifest";
            }
            sb.append(unitStatus);
            sb.append("</td>");
            sb.append("<td>");
            if (iub.getDispoCode() != null) {
                if (iub.getDispoCode().equalsIgnoreCase("DATA") || iub.getDispoCode().equalsIgnoreCase("WATR")) {
                    sb.append("<a href=\"#\"><span class=\"blueBold\" onclick=\"unitsEdit('").append(request.getContextPath()).append("','").append(iub.getUnitId()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getOriginUnlocCode()).append("')\" title=\"").append(iub.getDispoDesc()).append("\">").append(iub.getDispoCode()).append("</span></a>");
                } else if (iub.getDispoCode().equalsIgnoreCase("AVAL")) {
                    sb.append("<a href=\"#\"><span class=\"greenBold\" style=\"cursor:pointer;\" onclick=\"unitsEdit('").append(request.getContextPath()).append("','").append(iub.getUnitId()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getOriginUnlocCode()).append("')\" title=\"").append(iub.getDispoDesc()).append("\">").append(iub.getDispoCode()).append("</span></a>");
                } else {
                    sb.append("<a href=\"#\"><span class=\"orangeBold11px\" style=\"cursor:pointer;\" onclick=\"unitsEdit('").append(request.getContextPath()).append("','").append(iub.getUnitId()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getOriginUnlocCode()).append("')\" title=\"").append(iub.getDispoDesc()).append("\">").append(iub.getDispoCode()).append("</span></a>");
                }
            }
            sb.append("</td>");
            sb.append("<td>");
            sb.append(" <span style=\"cursor:pointer;\" title=\"").append(iub.getDescription()).append("\">");
            if (iub.getDescription().length() >= 7) {
                sb.append(iub.getDescription().substring(0, 7));
            } else {
                sb.append(iub.getDescription());
            }
            sb.append("</span>");
            sb.append("</td>");
            sb.append("<td></td>");
            sb.append("<td></td>");
            sb.append("<td></td>");
            sb.append("<td></td>");
            sb.append("<td>");
            sb.append(iub.getMasterBl().toUpperCase());
            sb.append("</td>");
            sb.append("<td>");
            if (iub.getColoaderAcctNo() != null) {
                sb.append(" <span style=\"cursor:pointer;\" title=\"<font size='1' color=#008000><b>Name : </b></font>").append(iub.getColoaderAcctName()).append("/").append(iub.getColoaderAcctNo()).append("<br/><font size='1' color=#008000><b>Address : </b></font>").append(iub.getColoaderAddress()).append("<br/><font size='1' color=#008000><b>City : </b></font>").append(iub.getColoaderCity()).append("\">");
                if (iub.getColoaderAcctName().length() >= 12) {
                    sb.append(iub.getColoaderAcctName().substring(0, 12));
                } else {
                    sb.append(iub.getColoaderAcctName());
                }
                sb.append("</span>");
            } else {
                sb.append("");
            }
            sb.append("</td>");
            sb.append("<td>");
            // *********** CFS DEV **********
            if (iub.getCfsWarehsName() != null) {
                sb.append(" <span style=\"cursor:pointer;\" title=\"<font size='1' color=#008000><b>Name : </b></font>").append(iub.getCfsWarehsName()).append("/").append(iub.getCfsWarehsNo()).append("<br/><font size='1' color=#008000><b>Address : </b></font>").append(iub.getCfsWarehsAddress()).append("<br/><font size='1' color=#008000><b>City : </b></font>").append(iub.getCfsWarehsCity()).append("\">");
                if (iub.getCfsWarehsName().length() >= 15) {
                    sb.append(iub.getCfsWarehsName().substring(0, 15));
                    sb.append("</span>");
                } else {
                    sb.append(iub.getCfsWarehsName());
                }
                // *********** Copy clipboard image **********
                sb.append("<img alt=\"Copy\" title=\"copy data to clipboard\" src=\"/logisoft/img/copy_icon.gif\" onclick=\"copyToClipboard(");
                sb.append("'Name: ").append(iub.getCfsWarehsName()).append("/").append(iub.getCfsWarehsNo());
                sb.append("\\nAddress : ").append(iub.getCfsWarehsAddress());
                sb.append("\\nCity : ").append(iub.getCfsWarehsCity());
                sb.append("')\"/>");
            } else {
                sb.append("");
            }
            sb.append("</td>");
            sb.append("<td>");
            if (iub.getUnitWarehsName() != null) {
                sb.append(" <span style=\"cursor:pointer;\" title=\"").append(iub.getUnitWarehsName()).append("/").append(iub.getUnitWarehsNo()).append("<br/>").append(iub.getUnitWarehsAddress()).append("<br/>").append(iub.getUnitWarehsCity()).append("\">");
                if (iub.getUnitWarehsName().length() >= 15) {
                    sb.append(iub.getUnitWarehsName().substring(0, 15));
                    sb.append("</span>");
                } else {
                    sb.append(iub.getUnitWarehsName());
                }
            } else {
                sb.append("");
            }
            sb.append("</td>");
            sb.append("<td>");
            if (iub.getItNumber() != null) {
                sb.append(" <span style=\"cursor:pointer;\" title=\"").append(iub.getItNumber().toUpperCase()).append("\">");
                if (iub.getItNumber().length() >= 15) {
                    sb.append(iub.getItNumber().toUpperCase());
                } else {
                    sb.append(iub.getItNumber().toUpperCase());
                }
            } else {
                sb.append("");
            }
            sb.append("</td>");
            sb.append("<td>");
            if (lclssheader.getClosedBy() == null) {
                sb.append("<input type=\"button\" class=\"button-style1\" value=\"New Dr\" onClick=\"importBooking('").append(request.getContextPath()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getUnitId()).append("','").append("')\">");
            } else {
                sb.append("<input type=\"button\" class=\"gray-background\" disabled=\"true\" value=\"New Dr\" onClick=\"importBooking('").append(request.getContextPath()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getUnitId()).append("','").append("')\">");
            }
            if (lclssheader.getClosedBy() == null) {
                sb.append("<input type=\"button\" class=\"button-style1\" value=\"Link DR\" onClick=\"linkDR('").append(request.getContextPath()).append("','").append(iub.getUnitId()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getUnitNo()).append("','").append(iub.getScheduleNo()).append("','").append(iub.getHeaderId()).append("','").append("')\">");
            } else {
                sb.append("<input type=\"button\" class=\"gray-background\" disabled=\"true\" value=\"Link DR\" onClick=\"linkDR('").append(request.getContextPath()).append("','").append(iub.getUnitId()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getUnitNo()).append("','").append(iub.getScheduleNo()).append("','").append(iub.getHeaderId()).append("','").append("')\">");
            }
            sb.append("<div id=\"arInvoice\" ");
            if (CommonUtils.isNotEmpty(iub.getArRedInvoiceId())) {
                sb.append("class=\"green-background\" ");
            } else {
                sb.append("class=\"button-style1\" ");
            }
            sb.append(" onclick=\"openLclVoyageArInvoice('").append(request.getContextPath()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getUnitNo()).append("','").append(true).append("','").append(iub.getHeaderId()).append("','").append("')\">");
            sb.append(" AR Invoice");
            sb.append("</div>");
            sb.append("<a href=\"/logisoft/lclImportUnitNotes.do?methodName=displayNotes&unitSsId=").append(iub.getUnitSsId()).append("&headerId=").append(iub.getHeaderId()).append("&unitId=").append(iub.getUnitId()).append("&unitNo=").append(iub.getUnitNo()).append("\" class=\"unitSsRemark\">");
            sb.append("<div  class=\"button-style1\" id=\"notes\">");
            sb.append("Notes");
            sb.append("</div>");
            sb.append("</a>");
            sb.append("<input type=\"button\" class=\"button-style1\" value=\"Costs\" onclick=\"lclImpCost('").append(request.getContextPath()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitId()).append("','").append(iub.getUnitNo()).append("')\">");
            sb.append("<input type=\"button\" class=\"button-style1\" value=\"Charges/Dist\" onclick=\"lclImpCharge('").append(request.getContextPath()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitId()).append("','").append(iub.getUnitNo()).append("')\">");
            sb.append("<input type=\"button\" class=\"button-style1\" value=\"Print/Fax/Email\" onClick=\"printreport('").append(iub.getUnitSsId()).append("','").append(iub.getUnitNo()).append("','").append("LCLImpUnits')\">");
            sb.append("<input type=\"button\" id=\"outsource").append(iub.getUnitId()).append("\"");
            if (CommonUtils.isNotEmpty(iub.getSsRemarks())) {
                sb.append("class=\"green-background\" ");
            } else {
                sb.append("class=\"button-style1\" ");
            }
            sb.append("value=\"Outsource\" onclick=\"openOutsourceEmail('").append(path).append("','").append(iub.getUnitId()).append("','").append(iub.getHeaderId()).append("')\">");
            sb.append("<span style=\"cursor:pointer\">");
            sb.append("<img src=\"/logisoft/images/edit.png\" alt=\"edit\" width:16px; height:16px;\" onclick=\"unitsEdit('").append(request.getContextPath()).append("','").append(iub.getUnitId()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitSsId()).append("','").append(iub.getOriginUnlocCode()).append("')\"></span>");
            sb.append("<img src=\"/logisoft/images/error.png\" style=\"cursor:pointer; width:16px; height:16px;\" onClick=\"deleteUnits(").append("'").append(iub.getUnitId()).append("')\">");
            sb.append("<img id=\"reportPreview\" src=\"/logisoft/img/icons/search_over.gif\" style=\"cursor:pointer; width:16px; height:16px;\" onClick=\"viewPreviewReport(").append("'").append(iub.getUnitSsId()).append("','").append(request.getContextPath()).append("')\">");
            if (roleDuty.isUnmanifestLclUnit() && iub.getUnitstatus().equals("M")) {
                sb.append("<img id=\"imgeditheader\" src=\"/logisoft/img/icons/unpost1.png\" style=\"cursor:pointer; width:16px; height:16px;\" onClick=\"unManifest(").append("'").append(iub.getUnitSsId()).append("','").append(request.getContextPath()).append("')\">");
            }
            sb.append("</td>");
            sb.append("</tr>");
            sb.append("</table>");
            sb.append("<br/>");
            sb.append("<table  id=\"bkgTable\" border=\"1\"  style=\"border:1px solid #dcdcdc;width:100%\">");
            sb.append("<tr class=\"tableHeadingNew\" style=\"background-color:#F5D5BC\">");
            sb.append("<td colspan=\"15\">Dock Receipt</td>");
            sb.append("</tr>");
            sb.append("<tr class=\"importjavaDesign\">");
            sb.append("<td style=\"cursor:pointer;width:1%\">FileNo</td>");
            sb.append("<td style=\"cursor:pointer;width:1%\">Exp</td>");
            sb.append("<td style=\"width:1%\">Dispo</td>");
            sb.append("<td style=\"width:1%\">Final Dest</td>");
            sb.append("<td style=\"width:3%\">AMS/HBL #</td>");
            sb.append("<td style=\"width:2%\">PCS</td>");
            sb.append("<td style=\"width:3%\">CBM</td>");
            sb.append("<td style=\"width:3%\">KGS</td>");
            sb.append("<td style=\"width:3%\">Collect Chgs</td>");
            sb.append("<td style=\"width:3%\">IPI Chgs</td>");
            sb.append("<td style=\"width:10%\">Shipper</td>");
            sb.append("<td style=\"width:10%\">Consignee</td>");
            sb.append("<td style=\"width:10%\">Notify</td>");
            sb.append("<td style=\"width:3%\">Booked By</td>");
            sb.append("<td style=\"width:3%\">SU</td>");
            sb.append("</tr>");
            int oddeven = 1;
            for (ImportBookingBean ibb : iub) {
                if (ibb.getFileNumber() != null) {
                    if (oddeven % 2 == 0) {
                        sb.append("<tr class=\"even\">");
                    } else {
                        sb.append("<tr class=\"odd\">");
                    }
                    sb.append("<td>");
                    sb.append("<u>");
                    sb.append("<div style=\"text-align:right;height: 10px; cursor:pointer;\" onClick=\"clickImportBooking('").append(request.getContextPath()).append("','").append(ibb.getFileId()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitId()).append("','").append(LCL_IMPORT).append("')\">");
                    sb.append(ibb.getFileNumber());
                    sb.append("</div>");
                    sb.append("</u>");
                    sb.append("</td>");
                    sb.append("<td>");
                    if (LCL_TRANSHIPMENT_TYPE.equalsIgnoreCase(ibb.getBookingType())) {
                        sb.append("<img src=\"/logisoft/img/icons/trigger.gif\" style=\"cursor:pointer; width:16px; height:16px;\" title=\"Export\"  onClick=\"clickImportBooking('").append(request.getContextPath()).append("','").append(ibb.getFileId()).append("','").append(iub.getHeaderId()).append("','").append(iub.getUnitId()).append("','").append(LCL_EXPORT).append("')\">");
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    sb.append("<span style=\"cursor:pointer;\" title=\"").append(ibb.getDispDesc()).append("\">");
                    sb.append(ibb.getDispCode());
                    sb.append("</span>");
                    sb.append("</td>");
                    sb.append("<td>");
                    sb.append(" <span title=\"").append(ibb.getFdName()).append(" / ").append(ibb.getFdCode()).append(" / ").append(ibb.getFdUnCode()).append("\">");
                    sb.append(ibb.getFdUnCode());
                    sb.append("</span>");
                    sb.append("</td>");
                    /**
                     * * Ams Hbl number **
                     */
                    sb.append("<td>");
                    sb.append(ibb.getAmsNo());
                    sb.append("</td>");
                    /**
                     * * pieces **
                     */
                    sb.append("<td>");
                    sb.append(ibb.getTotalPiece());
                    sb.append("</td>");
                    sb.append("<td>");
                    if (ibb.getTotalVolumeImperial() != 0.0) {
                        sb.append(ibb.getTotalVolumeImperial());
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    if (ibb.getTotalWeightImperial() != 0.0) {
                        sb.append(ibb.getTotalWeightImperial());
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    if (ibb.getTotalCollect() != 0.0) {
                        sb.append(ibb.getTotalCollect());
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    if (ibb.getTotalIPI() != 0.0) {
                        sb.append(ibb.getTotalIPI());
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    if (ibb.getShipName() != null) {
                        sb.append(" <span style=\"cursor:pointer;\" title=\"").append(ibb.getShipName().toUpperCase()).append("<br>").append(ibb.getShipAcct()).append("\">");
                        sb.append(ibb.getShipName().toUpperCase());
                        sb.append("</span>");
                    } else {
                        sb.append("");
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    if (ibb.getConsName() != null) {
                        sb.append(" <span title=\"").append(ibb.getConsName().toUpperCase()).append("<br>").append(ibb.getConsAcct()).append("\">");
                        sb.append(ibb.getConsName().toUpperCase());
                        sb.append("</span>");
                    } else {
                        sb.append("");
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    if (ibb.getNotyName() != null) {
                        sb.append(" <span title=\"").append(ibb.getNotyName().toUpperCase()).append("<br>").append(ibb.getNotyAcct()).append("\">");
                        sb.append(ibb.getNotyName().toUpperCase());
                        sb.append("</span>");
                    } else {
                        sb.append("");
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    if (ibb.getNotyName() != null) {
                        sb.append(ibb.getBookedBy().toUpperCase());
                    } else {
                        sb.append("");
                    }
                    sb.append("</td>");
                    sb.append("<td>");
                    if (LCL_IMPORT_TYPE.equalsIgnoreCase(ibb.getBookingType()) && ibb.getDispCode() != null && !"DATA".equalsIgnoreCase(ibb.getDispCode())) {
                        sb.append("<img src=\"/logisoft/img/icons/search_over.gif\" style=\"cursor:pointer; width:18px; height:18px;\" title=\"Preview\" onClick=\"viewPreviewReport(").append("'").append(ibb.getFileId()).append("','").append(ibb.getFileNumber()).append("','").append(request.getContextPath()).append("')\">");
                        sb.append("</td>");
                    }
                    sb.append("</tr>");
                    oddeven++;
                }
            }
            sb.append("</table>");
            sb.append("</div>");
            count++;
        }
        tabBox.append("</div>");
        tab.append(tabBox);
        tab.append(sb);
        tab.append("</div>");
        request.setAttribute("lclImportBookingUnit", tab.toString());
    }

    public List<ImportsManifestBean> getDrCodeFEmailList(HttpServletRequest request) throws Exception {
        CustomerContactDAO customerContactDao = new CustomerContactDAO();
        String accountNo = null, acctno = "";
        accountNo = request.getParameter("shipperCode") + "," + request.getParameter("consigneeCode") + ","
                + request.getParameter("notifyCode") + "," + request.getParameter("notify2Code");
        String[] accountNumbers = StringUtils.split(accountNo, ",");
        for (String acccountNumber : accountNumbers) {
            acctno += "'" + acccountNumber + "',";
        }
        return customerContactDao.getAllEMailIdsByCodeF(acctno.substring(0, acctno.length() - 1));
    }
}
