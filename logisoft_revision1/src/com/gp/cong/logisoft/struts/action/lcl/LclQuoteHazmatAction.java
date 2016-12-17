/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclQuoteUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclContact;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclQuoteHazmat;
import com.gp.cong.logisoft.domain.lcl.LclQuotePiece;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuoteHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclQuotePieceDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.struts.form.lcl.LclQuoteHazmatForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Owner
 */
public class LclQuoteHazmatAction extends LogiwareDispatchAction {

    public ActionForward saveOrUpdateQtHazmat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclHazmatDAO lclHazmatDAO = new LclHazmatDAO();
        LclQuoteHazmatForm lclQuoteHazmatForm = (LclQuoteHazmatForm) form;
        LclQuoteHazmatDAO lclQuoteHazmatDAO = new LclQuoteHazmatDAO();
        User loginUser = getCurrentUser(request);
        Date now = new Date();
        LclQuoteHazmat lclQuoteHazmat = lclQuoteHazmatDAO.findById(lclQuoteHazmatForm.getHazmatId());
        if (lclQuoteHazmat == null) {
            lclQuoteHazmat = new LclQuoteHazmat();
            lclQuoteHazmat.setEnteredDatetime(now);
            lclQuoteHazmat.setEnteredBy(loginUser);
        }
        lclQuoteHazmat.setModifiedBy(loginUser);
        lclQuoteHazmat.setModifiedDatetime(now);
        LclFileNumber lclFileNumber = new LclFileNumber(lclQuoteHazmatForm.getFileNumberId());

        if (lclQuoteHazmat.getEmergencyContact() == null) {
            LclContact lclContact = new LclContact();
            lclContact.setEnteredDatetime(now);
            lclContact.setEnteredBy(loginUser);
            lclQuoteHazmat.setEmergencyContact(lclContact);
        }
        lclQuoteHazmat.getEmergencyContact().setLclFileNumber(lclFileNumber);
        lclQuoteHazmat.getEmergencyContact().setModifiedBy(loginUser);
        lclQuoteHazmat.getEmergencyContact().setModifiedDatetime(now);
        lclQuoteHazmat.getEmergencyContact().setContactName(lclQuoteHazmatForm.getEmergencyContact());
        lclQuoteHazmat.getEmergencyContact().setPhone1(lclQuoteHazmatForm.getEmergencyPhone());

        lclQuoteHazmat.setProperShippingName(lclQuoteHazmatForm.getProperShippingName().toUpperCase());
        if (lclQuoteHazmatForm.getFlashPoint() != null && !lclQuoteHazmatForm.getFlashPoint().equals("")) {
            lclQuoteHazmat.setFlashPoint(new BigDecimal(lclQuoteHazmatForm.getFlashPoint()));
            lclQuoteHazmat.setFlashPointUom("C");
        } else {
            lclQuoteHazmat.setFlashPoint(null);
            lclQuoteHazmat.setFlashPointUom(null);
        }
        if (lclQuoteHazmatForm.getInnerPkgNwtPiece() != null && !lclQuoteHazmatForm.getInnerPkgNwtPiece().equals("")) {
            lclQuoteHazmat.setInnerPkgNwtPiece(new BigDecimal(lclQuoteHazmatForm.getInnerPkgNwtPiece()));
        }
        if (lclQuoteHazmatForm.getLiquidVolume() != null && !lclQuoteHazmatForm.getLiquidVolume().equals("")) {
            lclQuoteHazmat.setLiquidVolume(new BigDecimal(lclQuoteHazmatForm.getLiquidVolume()));
            if (lclQuoteHazmat.getLiquidVolume().doubleValue() > 0) {
                lclQuoteHazmat.setLiquidVolumeUom("G");
            }
        } else {
            lclQuoteHazmat.setLiquidVolumeUom(null);
            lclQuoteHazmat.setLiquidVolume(null);
        }
        if (lclQuoteHazmatForm.getTotalNetWeight() != null && !lclQuoteHazmatForm.getTotalNetWeight().equals("")) {
            lclQuoteHazmat.setTotalNetWeight(new BigDecimal(lclQuoteHazmatForm.getTotalNetWeight()));
        }
        if (lclQuoteHazmatForm.getTotalGrossWeight() != null && !lclQuoteHazmatForm.getTotalGrossWeight().equals("")) {
            lclQuoteHazmat.setTotalGrossWeight(new BigDecimal(lclQuoteHazmatForm.getTotalGrossWeight()));
        }
        lclQuoteHazmat.setUnHazmatNo(lclQuoteHazmatForm.getUnHazmatNo().toUpperCase());
        lclQuoteHazmat.setImoPriClassCode(lclQuoteHazmatForm.getImoPriClassCode());
        lclQuoteHazmat.setEmsCode(lclQuoteHazmatForm.getEmsCode().toUpperCase());
        lclQuoteHazmat.setTechnicalName(lclQuoteHazmatForm.getTechnicalName().toUpperCase());
        lclQuoteHazmat.setPackingGroupCode(lclQuoteHazmatForm.getPackingGroupCode());
        lclQuoteHazmat.setOuterPkgNoPieces(lclQuoteHazmatForm.getOuterPkgNoPieces());
        lclQuoteHazmat.setOuterPkgType(lclQuoteHazmatForm.getOuterPkgType());
        lclQuoteHazmat.setOuterPkgComposition(lclQuoteHazmatForm.getOuterPkgComposition());
        lclQuoteHazmat.setInnerPkgNoPieces(lclQuoteHazmatForm.getInnerPkgNoPieces());
        lclQuoteHazmat.setInnerPkgType(lclQuoteHazmatForm.getInnerPkgType());
        lclQuoteHazmat.setInnerPkgComposition(lclQuoteHazmatForm.getInnerPkgComposition());
        lclQuoteHazmat.setInnerPkgUom(lclQuoteHazmatForm.getInnerPkgUom());
        /* if (CommonUtils.isNotEmpty(lclQuoteHazmatForm.getInnerPkgNoPieces())) {
        lclQuoteHazmat.setInnerPkgUom(lclQuoteHazmatForm.getInnerPkgUom());
        } else {
        lclQuoteHazmat.setInnerPkgUom(null);
        }*/
        lclQuoteHazmat.setImoPriSubClassCode(lclQuoteHazmatForm.getImoPriSubClassCode().toUpperCase());
        lclQuoteHazmat.setImoSecSubClassCode(lclQuoteHazmatForm.getImoSecSubClassCode().toUpperCase());
        lclQuoteHazmat.setHazmatDeclarations(lclQuoteHazmatForm.getFreeFormValues().toUpperCase());
        lclQuoteHazmat.setOuterPkgNoPieces(lclQuoteHazmatForm.getOuterPkgNoPieces());
        lclQuoteHazmat.setReportableQuantity(lclQuoteHazmatForm.getReportableQuantity());
        lclQuoteHazmat.setMarinePollutant(lclQuoteHazmatForm.getMarinePollutant());
        lclQuoteHazmat.setExceptedQuantity(lclQuoteHazmatForm.getExceptedQuantity());
        lclQuoteHazmat.setLimitedQuantity(lclQuoteHazmatForm.getLimitedQuantity());
        lclQuoteHazmat.setResidue(lclQuoteHazmatForm.getResidue());
        lclQuoteHazmat.setPrintOnHouseBl(lclQuoteHazmatForm.getPrintHouseBL());
        lclQuoteHazmat.setPrintOnSsMasterBl(lclQuoteHazmatForm.getPrintMasterBL());
        lclQuoteHazmat.setSendWithEdiMaster(lclQuoteHazmatForm.getSendEdiMaster());
        lclQuoteHazmat.setHazmatDeclarations(lclQuoteHazmatForm.getFreeFormValues());
        lclQuoteHazmat.setInhalationHazard(lclQuoteHazmatForm.getInhalationHazard());
        lclQuoteHazmat.setLclQuotePiece(new LclQuotePiece(lclQuoteHazmatForm.getQtPieceId()));
        lclQuoteHazmat.setLclFileNumber(lclFileNumber);
        lclQuoteHazmatDAO.saveOrUpdate(lclQuoteHazmat);
        String hotCode = lclHazmatDAO.getHotCodeByHazmatClassType(lclQuoteHazmat.getImoPriClassCode());
        if (CommonUtils.isNotEmpty(hotCode)) {
            LclQuoteHotCodeDAO hotCodeDAO = new LclQuoteHotCodeDAO();
            if (hotCodeDAO.isHotCodeExist(hotCode, lclFileNumber.getId().toString())) {
                hotCodeDAO.insertQuery(lclFileNumber.getId().toString(), hotCode, getCurrentUser(request).getUserId());
            }
        }
        setHazmatValues(lclQuoteHazmatForm, lclQuoteHazmatDAO, request);
        lclQuoteHazmatForm.setHazmatId(0l);
        lclQuoteHazmatForm.setFreeFormValues("");
        request.setAttribute("lclQuoteHazmatForm", lclQuoteHazmatForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward displayQuoteHazmat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteHazmatForm lclQuoteHazmatForm = (LclQuoteHazmatForm) form;
        LclQuotePiece bookingPiece = new LclQuotePieceDAO().findById(lclQuoteHazmatForm.getQtPieceId());
        lclQuoteHazmatForm.setCommodityNo(bookingPiece.getCommodityType().getCode());
        lclQuoteHazmatForm.setCommodityDesc(bookingPiece.getCommodityType().getDescEn());
        LclQuoteHazmatDAO lclQuoteHazmatDAO = new LclQuoteHazmatDAO();
        setHazmatValues(lclQuoteHazmatForm, lclQuoteHazmatDAO, request);
        lclQuoteHazmatForm.setHazClassDesc(new LclUtils().appendHazmatClass());
        lclQuoteHazmatForm.setButtonFlag("display");
        request.setAttribute("lclQuoteHazmatForm", lclQuoteHazmatForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward editQtHazmat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteHazmatForm lclQuoteHazmatForm = (LclQuoteHazmatForm) form;
        LclQuoteHazmatDAO lclQuoteHazmatDAO = new LclQuoteHazmatDAO();
        LclQuoteHazmat lclQuoteHazmat = lclQuoteHazmatDAO.findById(lclQuoteHazmatForm.getHazmatId());
        request.setAttribute("lclQuoteHazmat", lclQuoteHazmat);
        setHazmatValues(lclQuoteHazmatForm, lclQuoteHazmatDAO, request);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward deleteQtHazmat(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteHazmatForm lclQuoteHazmatForm = (LclQuoteHazmatForm) form;
        LclQuoteHazmatDAO lclQuoteHazmatDAO = new LclQuoteHazmatDAO();
        LclQuoteHazmat lclQuoteHazmat = lclQuoteHazmatDAO.findById(lclQuoteHazmatForm.getHazmatId());
        lclQuoteHazmatDAO.delete(lclQuoteHazmat);
        setHazmatValues(lclQuoteHazmatForm, lclQuoteHazmatDAO, request);
        lclQuoteHazmatForm.setHazmatId(0l);
        request.setAttribute("lclHazmatForm", lclQuoteHazmatForm);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward refreshQtCommodity(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclQuoteHazmatForm lclQuoteHazmatForm = (LclQuoteHazmatForm) form;
        List<LclQuotePiece> lclQuotePiecesList = new LclQuoteUtils().setCommodityList(lclQuoteHazmatForm.getFileNumberId(), request);
        return mapping.findForward("commodityDesc");
    }

    public void setHazmatValues(LclQuoteHazmatForm lclQuoteHazmatForm, LclQuoteHazmatDAO lclQuoteHazmatDAO, HttpServletRequest req) throws Exception {
        List<LclQuoteHazmat> hazmatQuoteList = lclQuoteHazmatDAO.findByFileAndCommodityList(lclQuoteHazmatForm.getFileNumberId(), lclQuoteHazmatForm.getQtPieceId());
        req.setAttribute("hazmatQuoteList", hazmatQuoteList);
    }
}
