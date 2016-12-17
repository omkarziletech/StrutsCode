/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import com.gp.cong.logisoft.domain.lcl.LclBookingPiece;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingPieceWhseDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclCostChargeDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclHazmatForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Owner
 */
public class LclHazmatAction extends LogiwareDispatchAction {

    public ActionForward saveorUpdateBkgHazmat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclHazmatForm lclHazmatForm = (LclHazmatForm) form;
        //HttpSession session = request.getSession();
        LclHazmatDAO lclHazmatDAO = new LclHazmatDAO();
        //   LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        //LclBookingHazmat lclBookingHazmat = lclHazmatForm.getLclBookingHazmat();
        Date now = new Date();
        User loginUser = getCurrentUser(request);
        LclBookingHazmat lclBookingHazmat = lclHazmatDAO.findById(lclHazmatForm.getHazmatId());
        if (lclBookingHazmat == null) {
            lclBookingHazmat = new LclBookingHazmat();
            lclBookingHazmat.setEnteredBy(loginUser);
            lclBookingHazmat.setEnteredDatetime(now);
        }
        String preClass = lclBookingHazmat.getImoPriClassCode();
        lclBookingHazmat.setModifiedBy(loginUser);
        lclBookingHazmat.setModifiedDatetime(now);
        LclFileNumber lclFileNumber = new LclFileNumber(lclHazmatForm.getFileNumberId());
        if (lclBookingHazmat.getEmergencyContact() == null) {
            LclContact lclContact = new LclContact();
            lclContact.setEnteredDatetime(now);
            lclContact.setEnteredBy(loginUser);
            lclBookingHazmat.setEmergencyContact(lclContact);
        }
        lclBookingHazmat.getEmergencyContact().setLclFileNumber(lclFileNumber);
        lclBookingHazmat.getEmergencyContact().setModifiedBy(loginUser);
        lclBookingHazmat.getEmergencyContact().setModifiedDatetime(now);
        lclBookingHazmat.getEmergencyContact().setContactName(lclHazmatForm.getEmergencyContact());
        lclBookingHazmat.getEmergencyContact().setPhone1(lclHazmatForm.getEmergencyPhone());
        lclBookingHazmat.setPrintOnHouseBl(lclHazmatForm.getPrintHouseBL());
        lclBookingHazmat.setPrintOnSsMasterBl(lclHazmatForm.getPrintMasterBL());
        lclBookingHazmat.setSendWithEdiMaster(lclHazmatForm.getSendEdiMaster());
        lclBookingHazmat.setProperShippingName(lclHazmatForm.getProperShippingName().toUpperCase());
        lclBookingHazmat.setUnHazmatNo(lclHazmatForm.getUnHazmatNo().toUpperCase());
        lclBookingHazmat.setImoPriClassCode(lclHazmatForm.getImoPriClassCode());
        lclBookingHazmat.setTechnicalName(lclHazmatForm.getTechnicalName());
        lclBookingHazmat.setPackingGroupCode(lclHazmatForm.getPackingGroupCode());
        if (lclHazmatForm.getFlashPoint() != null && !lclHazmatForm.getFlashPoint().equals("")) {
            lclBookingHazmat.setFlashPoint(new BigDecimal(lclHazmatForm.getFlashPoint()));
            if (lclBookingHazmat.getFlashPoint().doubleValue() > 0) {
                lclBookingHazmat.setFlashPointUom("C");
            }
        } else if (lclBookingHazmat.getId() != null) {
            lclBookingHazmat.setFlashPointUom(null);
        }
        if (lclHazmatForm.getInnerPkgNwtPiece() != null && !lclHazmatForm.getInnerPkgNwtPiece().equals("")) {
            lclBookingHazmat.setInnerPkgNwtPiece(new BigDecimal(lclHazmatForm.getInnerPkgNwtPiece()));
        }
        if (lclHazmatForm.getLiquidVolume() != null && !lclHazmatForm.getLiquidVolume().equals("")) {
            lclBookingHazmat.setLiquidVolume(new BigDecimal(lclHazmatForm.getLiquidVolume()));
            if (lclBookingHazmat.getLiquidVolume().doubleValue() > 0) {
                lclBookingHazmat.setLiquidVolumeUom("G");
            }
        } else if (null == lclHazmatForm.getLiquidVolume() || CommonUtils.isEmpty(lclHazmatForm.getLiquidVolume())) {
            lclBookingHazmat.setLiquidVolume(null);
        } else if (lclBookingHazmat.getId() != null) {
            lclBookingHazmat.setLiquidVolumeUom(null);
        }
        if (lclHazmatForm.getLiquidVolumeLitreorGals() != null && !lclHazmatForm.getLiquidVolumeLitreorGals().equals("")) {
            lclBookingHazmat.setLiquidVolumeLitreorGals(lclHazmatForm.getLiquidVolumeLitreorGals());
        } else {
            lclBookingHazmat.setLiquidVolumeLitreorGals(null);
        }
        if (lclHazmatForm.getTotalNetWeight() != null && !lclHazmatForm.getTotalNetWeight().equals("")) {
            lclBookingHazmat.setTotalNetWeight(new BigDecimal(lclHazmatForm.getTotalNetWeight()));
        } else {
            lclBookingHazmat.setTotalNetWeight(null);
        }
        if (lclHazmatForm.getTotalGrossWeight() != null && !lclHazmatForm.getTotalGrossWeight().equals("")) {
            lclBookingHazmat.setTotalGrossWeight(new BigDecimal(lclHazmatForm.getTotalGrossWeight()));
        } else {
            lclBookingHazmat.setTotalGrossWeight(null);
        }
        lclBookingHazmat.setOuterPkgNoPieces(lclHazmatForm.getOuterPkgNoPieces());
        lclBookingHazmat.setReportableQuantity(lclHazmatForm.getReportableQuantity());
        lclBookingHazmat.setMarinePollutant(lclHazmatForm.getMarinePollutant());
        lclBookingHazmat.setExceptedQuantity(lclHazmatForm.getExceptedQuantity());
        lclBookingHazmat.setLimitedQuantity(lclHazmatForm.getLimitedQuantity());
        lclBookingHazmat.setResidue(lclHazmatForm.getResidue());
        lclBookingHazmat.setInhalationHazard(lclHazmatForm.getInhalationHazard());
        lclBookingHazmat.setOuterPkgType(lclHazmatForm.getOuterPkgType());
        lclBookingHazmat.setOuterPkgComposition(lclHazmatForm.getOuterPkgComposition());
        lclBookingHazmat.setInnerPkgNoPieces(lclHazmatForm.getInnerPkgNoPieces());
        lclBookingHazmat.setInnerPkgType(lclHazmatForm.getInnerPkgType());
        lclBookingHazmat.setInnerPkgComposition(lclHazmatForm.getInnerPkgComposition());
        lclBookingHazmat.setInnerPkgUom(lclHazmatForm.getInnerPkgUom());
        /* if (CommonUtils.isNotEmpty(lclHazmatForm.getInnerPkgNoPieces())) {
        lclBookingHazmat.setInnerPkgUom(lclHazmatForm.getInnerPkgUom());
        } else {
        lclBookingHazmat.setInnerPkgUom(null);
        }*/
        lclBookingHazmat.setEmsCode(lclHazmatForm.getEmsCode());
        lclBookingHazmat.setImoPriSubClassCode(lclHazmatForm.getImoPriSubClassCode());
        lclBookingHazmat.setImoSecSubClassCode(lclHazmatForm.getImoSecSubClassCode());
        lclBookingHazmat.setHazmatDeclarations(lclHazmatForm.getFreeFormValues());
        lclBookingHazmat.setLclBookingPiece(new LclBookingPiece(lclHazmatForm.getBookingPieceId()));
        lclBookingHazmat.setLclFileNumber(lclFileNumber);
        lclHazmatDAO.saveOrUpdate(lclBookingHazmat);
        String currentHotCode = lclHazmatDAO.getHotCodeByHazmatClassType(lclBookingHazmat.getImoPriClassCode());
        String preHotCode = lclHazmatDAO.getHotCodeByHazmatClassType(preClass);
        LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
        if (CommonUtils.isEmpty(preHotCode) && lclBookingHotCodeDAO.isHotCodeNotExist(currentHotCode, lclFileNumber.getId().toString())) {
            lclBookingHotCodeDAO.saveHotCode(lclHazmatForm.getFileNumberId(), currentHotCode, getCurrentUser(request).getUserId());
        } else if (CommonUtils.isNotEmpty(preHotCode) && lclBookingHotCodeDAO.isHotCodeNotExist(currentHotCode, lclFileNumber.getId().toString())) {
            LclBookingHotCode bookingHotCode = lclBookingHotCodeDAO.getHotCode(lclBookingHazmat.getLclFileNumber().getId(), preHotCode);
            if (bookingHotCode == null) {
                lclBookingHotCodeDAO.saveHotCode(lclHazmatForm.getFileNumberId(), currentHotCode, getCurrentUser(request).getUserId());
            } else {
                bookingHotCode.setLclFileNumber(lclFileNumber);
                bookingHotCode.setCode(currentHotCode);
                bookingHotCode.setModifiedBy(loginUser);
                bookingHotCode.setModifiedDatetime(now);
                lclBookingHotCodeDAO.update(bookingHotCode);
            }
        }
        if ("Exports".equalsIgnoreCase(lclHazmatForm.getModuleName())) {
            new LCLBookingDAO().updateModifiedDateTime(lclFileNumber.getId(),loginUser.getUserId());
        }
        request.setAttribute("lcl3pRefNo", lclBookingHotCodeDAO.getHotCodeList(lclBookingHazmat.getLclFileNumber().getId()));
        lclHazmatForm.setButtonFlag("display");
        lclHazmatForm.setHazmatId(0l);
        setHazmatDetails(lclHazmatForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclHazmatForm lclHazmatForm = (LclHazmatForm) form;
        LclBookingPiece bookingPiece = new LclBookingPieceDAO().findById(lclHazmatForm.getBookingPieceId());
        lclHazmatForm.setCommodityCode(bookingPiece.getCommodityType().getCode());
        lclHazmatForm.setCommodityDesc(bookingPiece.getCommodityType().getDescEn());
        lclHazmatForm.setButtonFlag("display");
        lclHazmatForm.setHazClassDesc(new LclUtils().appendHazmatClass());
        setHazmatDetails(lclHazmatForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward editBkgHazmat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclHazmatForm lclHazmatForm = (LclHazmatForm) form;
        LclBookingHazmat lclHazmat = new LclHazmatDAO().findById(lclHazmatForm.getHazmatId());
        request.setAttribute("lclBookingHazmat", lclHazmat);
        setHazmatDetails(lclHazmatForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteBkgHazmat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclHazmatForm lclHazmatForm = (LclHazmatForm) form;
        new LclHazmatDAO().deleteBookingHazmatByFileId(lclHazmatForm.getHazmatId());
        setHazmatDetails(lclHazmatForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward closeHazmat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclHazmatForm lclHazmatForm = (LclHazmatForm) form;
        List<LclBookingPiece> commodityList = new LclBookingPieceDAO().findByProperty("lclFileNumber.id", lclHazmatForm.getFileNumberId());
        for (LclBookingPiece lbp : commodityList) {
            lbp.setLclBookingHazmatList(new LclHazmatDAO().findByFileAndCommodityList(lclHazmatForm.getFileNumberId(), lbp.getId()));
            lbp.setLclBookingAcList(new LclCostChargeDAO().findByFileAndCommodityList(lclHazmatForm.getFileNumberId(), lbp.getId()));
            lbp.setLclBookingPieceWhseList(new LclBookingPieceWhseDAO().findByProperty("lclBookingPiece.id", lbp.getId()));
        }
        if (commodityList != null && !commodityList.isEmpty()) {
            LclBookingPiece lbp = commodityList.get(0);
            request.setAttribute("ofspotrate", lbp.getLclFileNumber().getLclBooking().getSpotRate());
        }
        request.setAttribute("lclCommodityList", commodityList);
        return mapping.findForward("commodityDesc");
    }

    public void setHazmatDetails(LclHazmatForm lclHazmatForm) throws Exception {
        /* LCL Import ITem 852 */
//        List<PackageType> packageTypeList = lclHazmatDAO.getAllPackages();
//        request.setAttribute("packageTypeList", packageTypeList);
//        List<GenericCode> packageCompositionList = lclHazmatDAO.getAllPackageComposition();
//        request.setAttribute("packageCompositionList", packageCompositionList);
        List<LclBookingHazmat> hazmatList = new LclHazmatDAO().findByFileAndCommodityList(lclHazmatForm.getFileNumberId(), lclHazmatForm.getBookingPieceId());
        lclHazmatForm.setHazmatList(hazmatList);
    }
}
