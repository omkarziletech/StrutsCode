/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.lcl.model.LclHazmatModel;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.domain.lcl.LclBookingHazmat;
import com.gp.cong.logisoft.domain.lcl.LclBookingHotCode;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.bl.LclBlPiece;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cvst.logisoft.util.DBUtil;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBlHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclBookingHotCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclConsolidateDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclHazmatDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LclBLPieceDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.struts.form.lcl.LclBlHazmatForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Owner
 */
public class LclBlHazmatAction extends LogiwareDispatchAction {

    public ActionForward saveHazmat(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlHazmatForm blHazmatForm = (LclBlHazmatForm) form;
        LclHazmatDAO hazmatDAO = new LclHazmatDAO();
        LclBookingHazmat lclBookingHazmat = hazmatDAO.findById(blHazmatForm.getBkgHazmatId());
        String preClass = lclBookingHazmat.getImoPriClassCode();
        lclBookingHazmat.setProperShippingName(blHazmatForm.getProperShippingName().toUpperCase());
        lclBookingHazmat.setUnHazmatNo(blHazmatForm.getUnHazmatNo().toUpperCase());
        lclBookingHazmat.setImoPriClassCode(blHazmatForm.getImoPriClassCode());
        lclBookingHazmat.setTechnicalName(blHazmatForm.getTechnicalName());
        lclBookingHazmat.setPackingGroupCode(blHazmatForm.getPackingGroupCode());
        if (blHazmatForm.getFlashPoint() != null && !blHazmatForm.getFlashPoint().equals("")) {
            lclBookingHazmat.setFlashPoint(new BigDecimal(blHazmatForm.getFlashPoint()));
            if (lclBookingHazmat.getFlashPoint().doubleValue() > 0) {
                lclBookingHazmat.setFlashPointUom("C");
            }
        } else if (lclBookingHazmat.getId() != null) {
            lclBookingHazmat.setFlashPointUom(null);
        }
        if (blHazmatForm.getInnerPkgNwtPiece() != null && !blHazmatForm.getInnerPkgNwtPiece().equals("")) {
            lclBookingHazmat.setInnerPkgNwtPiece(new BigDecimal(blHazmatForm.getInnerPkgNwtPiece()));
        }
        if (blHazmatForm.getLiquidVolume() != null && !blHazmatForm.getLiquidVolume().equals("")) {
            lclBookingHazmat.setLiquidVolume(new BigDecimal(blHazmatForm.getLiquidVolume()));
            if (lclBookingHazmat.getLiquidVolume().doubleValue() > 0) {
                lclBookingHazmat.setLiquidVolumeUom("G");
            }
        } else if (null == blHazmatForm.getLiquidVolume() || CommonUtils.isEmpty(blHazmatForm.getLiquidVolume())) {
            lclBookingHazmat.setLiquidVolume(null);
        } else if (lclBookingHazmat.getId() != null) {
            lclBookingHazmat.setLiquidVolumeUom(null);
        } 
        if (blHazmatForm.getLiquidVolumeLitreorGals() != null && !blHazmatForm.getLiquidVolumeLitreorGals().equals("")) {
            lclBookingHazmat.setLiquidVolumeLitreorGals(blHazmatForm.getLiquidVolumeLitreorGals());
        } else {
            lclBookingHazmat.setLiquidVolumeLitreorGals(null);
        }
        if (blHazmatForm.getTotalNetWeight() != null && !blHazmatForm.getTotalNetWeight().equals("")) {
            lclBookingHazmat.setTotalNetWeight(new BigDecimal(blHazmatForm.getTotalNetWeight()));
        } else {
            lclBookingHazmat.setTotalNetWeight(null);
        }
        if (blHazmatForm.getTotalGrossWeight() != null && !blHazmatForm.getTotalGrossWeight().equals("")) {
            lclBookingHazmat.setTotalGrossWeight(new BigDecimal(blHazmatForm.getTotalGrossWeight()));
        } else {
            lclBookingHazmat.setTotalGrossWeight(null);
        }
        lclBookingHazmat.setOuterPkgNoPieces(blHazmatForm.getOuterPkgNoPieces());
        lclBookingHazmat.setReportableQuantity(blHazmatForm.getReportableQuantity());
        lclBookingHazmat.setMarinePollutant(blHazmatForm.getMarinePollutant());
        lclBookingHazmat.setExceptedQuantity(blHazmatForm.getExceptedQuantity());
        lclBookingHazmat.setLimitedQuantity(blHazmatForm.getLimitedQuantity());
        lclBookingHazmat.setResidue(blHazmatForm.getResidue());
        lclBookingHazmat.setPrintOnHouseBl(blHazmatForm.getPrintHouseBL());
        lclBookingHazmat.setPrintOnSsMasterBl(blHazmatForm.getPrintMasterBL());
        lclBookingHazmat.setSendWithEdiMaster(blHazmatForm.getSendEdiMaster());
        lclBookingHazmat.setInhalationHazard(blHazmatForm.getInhalationHazard());
        lclBookingHazmat.setOuterPkgType(blHazmatForm.getOuterPkgType());
        lclBookingHazmat.setOuterPkgComposition(blHazmatForm.getOuterPkgComposition());
        lclBookingHazmat.setInnerPkgNoPieces(blHazmatForm.getInnerPkgNoPieces());
        lclBookingHazmat.setInnerPkgType(blHazmatForm.getInnerPkgType());
        lclBookingHazmat.setInnerPkgComposition(blHazmatForm.getInnerPkgComposition());
        lclBookingHazmat.setInnerPkgUom(blHazmatForm.getInnerPkgUom());
        lclBookingHazmat.setEmsCode(blHazmatForm.getEmsCode());
        lclBookingHazmat.setImoPriSubClassCode(blHazmatForm.getImoPriSubClassCode());
        lclBookingHazmat.setImoSecSubClassCode(blHazmatForm.getImoSecSubClassCode());
        lclBookingHazmat.setHazmatDeclarations(blHazmatForm.getFreeFormValues());
        hazmatDAO.saveOrUpdate(lclBookingHazmat);
        String currentHotCode = hazmatDAO.getHotCodeByHazmatClassType(lclBookingHazmat.getImoPriClassCode());
        String preHotCode = hazmatDAO.getHotCodeByHazmatClassType(preClass);
        LclBookingHotCodeDAO lclBookingHotCodeDAO = new LclBookingHotCodeDAO();
        LclFileNumber fileNumber = lclBookingHazmat.getLclFileNumber();
        if (CommonUtils.isEmpty(preHotCode) && lclBookingHotCodeDAO.isHotCodeNotExist(currentHotCode, fileNumber.getId().toString())) {
            lclBookingHotCodeDAO.saveHotCode(blHazmatForm.getFileId(), currentHotCode, getCurrentUser(request).getUserId());
        } else if (CommonUtils.isNotEmpty(preHotCode) && lclBookingHotCodeDAO.isHotCodeNotExist(currentHotCode, fileNumber.getId().toString())) {
            LclBookingHotCode bookingHotCode = lclBookingHotCodeDAO.getHotCode(lclBookingHazmat.getLclFileNumber().getId(), preHotCode);
            if (bookingHotCode == null) {
                lclBookingHotCodeDAO.saveHotCode(fileNumber.getId(), currentHotCode, getCurrentUser(request).getUserId());
            } else {
                bookingHotCode.setLclFileNumber(fileNumber);
                bookingHotCode.setCode(currentHotCode);
                bookingHotCode.setModifiedBy(getCurrentUser(request));
                bookingHotCode.setModifiedDatetime(new Date());
                lclBookingHotCodeDAO.update(bookingHotCode);
            }
        }
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Primary ClassCodes");
        request.setAttribute("hazmatClassList", new DBUtil().scanScreenName(codeTypeId));
        request.setAttribute("packageGroupCodeList", new QuotationBC().getPackingGroupCode());
        request.setAttribute("refreshFlag", "refresh");
        return mapping.findForward("editHazmat");
    }

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlHazmatForm lclBlHazmatForm = (LclBlHazmatForm) form;
        LclBlPiece blPiece = new LclBLPieceDAO().findById(lclBlHazmatForm.getBlPieceId());
        lclBlHazmatForm.setCommodityNo(blPiece.getCommodityType().getCode());
        lclBlHazmatForm.setCommodityName(blPiece.getCommodityType().getDescEn());
        List<Long> conoslidatelist = new LclConsolidateDAO().getConsolidatesFiles(lclBlHazmatForm.getFileId());
        conoslidatelist.add(lclBlHazmatForm.getFileId());
        List<LclHazmatModel> bkgHazmatList = new LclBlHazmatDAO().getBkgHazmatList(conoslidatelist);
        request.setAttribute("bkgHazmatList", bkgHazmatList);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward addHazmat(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlHazmatForm lclBlHazmatForm = (LclBlHazmatForm) form;
        return mapping.findForward(SUCCESS);
    }

    public ActionForward editHazmat(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclBlHazmatForm blHazmatForm = (LclBlHazmatForm) form;
        LclBookingHazmat lclBookingHazmat = new LclHazmatDAO().findById(blHazmatForm.getBkgHazmatId());
        request.setAttribute("lclBookingHazmat", lclBookingHazmat);
        request.setAttribute("hazClassDesc", new LclUtils().appendHazmatClass());

        request.setAttribute("hazmatFileNo", request.getParameter("hazmatFileNo"));
        Integer codeTypeId = new CodetypeDAO().getCodeTypeId("Primary ClassCodes");
        request.setAttribute("hazmatClassList", new DBUtil().scanScreenName(codeTypeId));
        request.setAttribute("packageGroupCodeList", new QuotationBC().getPackingGroupCode());
        return mapping.findForward("editHazmat");
    }
}
