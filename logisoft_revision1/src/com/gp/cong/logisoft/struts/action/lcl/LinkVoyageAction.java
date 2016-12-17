package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.dwr.LCLImportChargeCalc;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclBookingPieceUnit;
import com.gp.cong.logisoft.domain.lcl.LclSsDetail;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceUnitDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsDetailDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDispoDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsImportsDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.struts.form.lcl.LinkVoyageForm;
import com.logiware.accounting.dao.LclManifestDAO;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Rajesh
 */
public class LinkVoyageAction extends LogiwareDispatchAction {

    public ActionForward doLink(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkVoyageForm voyageForm = (LinkVoyageForm) form;
        Long fileId = Long.parseLong(voyageForm.getFileId());
        Long unitSsId = Long.parseLong(voyageForm.getUnitId());
        Long voyageId = CommonUtils.isNotEmpty(voyageForm.getVoyageId()) ? Long.parseLong(voyageForm.getVoyageId()) : 0;
        LclUnitSsDAO unitSsDao = new LclUnitSsDAO();
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        LclUnitSs unitSs = new LclUnitSs();
        LclSsDetail lclSsDetail = null;
        LclUtils util = new LclUtils();
        LclBookingPieceDAO lclBookingPieceDAO = new LclBookingPieceDAO();
        User user = (User) request.getSession().getAttribute("loginuser");
        Date now = new Date();
        List<LclBookingPiece> bkgPieces = lclBookingPieceDAO.findByProperty("lclFileNumber.id", fileId);
        LclBookingPieceUnitDAO bkgPieceUnitDao = new LclBookingPieceUnitDAO();
        LclBookingPieceUnit bkgPieceUnit;
        if (CommonUtils.isNotEmpty(unitSsId)) {
            unitSs = unitSsDao.findById(unitSsId);
        }
        if (null != unitSs) {
            StringBuilder remarks = new StringBuilder("Linked to Unit# ");
            remarks.append(unitSs.getLclUnit().getUnitNo());
            remarks.append(" of Voyage# ");
            remarks.append(unitSs.getLclSsHeader().getScheduleNo()).append(" ");
            // make an entry in piece unit table
            for (LclBookingPiece bkgPiece : bkgPieces) {
                bkgPieceUnit = new LclBookingPieceUnit();
                bkgPieceUnit.setLclBookingPiece(bkgPiece);
                bkgPieceUnit.setLclUnitSs(unitSs);
                bkgPieceUnit.setLoadedDatetime(now);
                bkgPieceUnit.setEnteredDatetime(now);
                bkgPieceUnit.setModifiedDatetime(now);
                bkgPieceUnit.setModifiedBy(user);
                bkgPieceUnit.setEnteredBy(user);
                bkgPieceUnitDao.save(bkgPieceUnit);
                // make a notes on this
                util.insertLCLRemarks(fileId, remarks.toString(), "T", user);
            }
            LclBooking lclBooking = new LCLBookingDAO().getByProperty("lclFileNumber.id", fileId);
            lclSsDetail = new LclSsDetailDAO().findByTransMode(voyageId, "V");
            String transhipment = lclBooking.getBookingType().equalsIgnoreCase("T") ? "Y" : "N";
            if ("N".equalsIgnoreCase(transhipment)) {
                this.lclImportsRatesCalculation(lclBooking, unitSs, fileId, bkgPieces, transhipment, request);
            }
            if (unitSs.getLclUnit() != null && unitSs.getLclUnit().getId() != null && lclSsDetail != null && lclSsDetail.getId() != null) {
                String disposition = new LclUnitSsDispoDAO().getDispositionByDetailId(unitSs.getLclUnit().getId(), lclSsDetail.getId());
                if (disposition.equalsIgnoreCase("PORT")) {
                    if (lclBooking.getBookingType().equalsIgnoreCase("I")) {
                        String realPath = request.getSession().getServletContext().getRealPath("/");
                        new LclManifestDAO().getAllManifestImportsBookingsByUnitSS(unitSsId, null, null,
                                getCurrentUser(request), true, realPath, true, voyageForm.getFileId());
                    } else {
                        lclFileNumberDAO.updateLclFileNumbersStatus(voyageForm.getFileId(), "M");
                    }
                }
            }
        }
        return null;
    }
    
    public void lclImportsRatesCalculation(LclBooking lclBooking, LclUnitSs unitSs, Long fileId, List<LclBookingPiece> bkgPieces, String transhipment, HttpServletRequest request) throws Exception {
        LCLImportChargeCalc lclImportChargeCalc = new LCLImportChargeCalc();
        String polUnCode = "";
        String podUnCode = "";
        String fdUnCode = "";
        String agentNo = "";
        String originCode = "";
        String impCfsId = "";
        Long unitId = null != unitSs.getLclUnit() ? unitSs.getLclUnit().getId() : null;
        Long ssHeaderId = null != unitSs.getLclSsHeader() ? unitSs.getLclSsHeader().getId() : null;
        User user = getCurrentUser(request);
        if (CommonFunctions.isNotNull(lclBooking)) {
            if (CommonFunctions.isNotNull(lclBooking.getPortOfOrigin()) && CommonFunctions.isNotNull(lclBooking.getPortOfOrigin().getUnLocationName())) {
                originCode = lclBooking.getPortOfOrigin().getUnLocationCode();
            }
            if (CommonFunctions.isNotNull(lclBooking.getPortOfLoading()) && CommonFunctions.isNotNull(lclBooking.getPortOfLoading().getUnLocationName())) {
                polUnCode = lclBooking.getPortOfLoading().getUnLocationCode();
            }
            if (CommonFunctions.isNotNull(lclBooking.getPortOfDestination()) && CommonFunctions.isNotNull(lclBooking.getPortOfDestination().getUnLocationName())) {
                podUnCode = lclBooking.getPortOfDestination().getUnLocationCode();
            }
            if (CommonFunctions.isNotNull(lclBooking.getFinalDestination()) && CommonFunctions.isNotNull(lclBooking.getFinalDestination().getUnLocationName())) {
                fdUnCode = lclBooking.getFinalDestination().getUnLocationCode();
            }
            if (CommonFunctions.isNotNull(lclBooking.getSupAcct())
                    && CommonFunctions.isNotNull(lclBooking.getSupAcct().getAccountno())) {
                agentNo = lclBooking.getSupAcct().getAccountno();
            }
            impCfsId = new LclUnitSsImportsDAO().getimpCfsId(unitId, ssHeaderId);
        }
        lclImportChargeCalc.ImportRateCalculation(originCode, polUnCode, podUnCode, fdUnCode, transhipment,
                lclBooking.getBillingType(), lclBooking.getBillToParty(), agentNo, impCfsId,
                fileId, bkgPieces, request, user, String.valueOf(unitSs.getId()));
    }
}
