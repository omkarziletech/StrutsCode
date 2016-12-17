/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lclDataReference;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitTypeDAO;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cong.logisoft.struts.form.lclDataReference.LclUnitTypeForm;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Logiware
 */
public class LclUnitTypeAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitTypeForm lclUnitTypeForm = (LclUnitTypeForm) form;
        UnitTypeDAO unitTypeDAO = new UnitTypeDAO();
        List<UnitType> unitTypeList = unitTypeDAO.getAllUnitTypeList(lclUnitTypeForm.getDescription());
        request.setAttribute("unitTypeList", unitTypeList);
        return mapping.findForward("displayUnit");
    }

    public ActionForward saveUnitType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitTypeForm lclUnitTypeForm = (LclUnitTypeForm) form;
        UnitTypeDAO unitTypeDAO = new UnitTypeDAO();
        UnitType unitType = new UnitTypeDAO().findById(lclUnitTypeForm.getId());
        if (CommonUtils.isNotEmpty(lclUnitTypeForm.getDescription()) && CommonUtils.isNotEmpty(lclUnitTypeForm.getEliteType())) {
            if (unitType == null) {
                unitType = new UnitType();
                unitType.setEnteredDatetime(new Date());
                unitType.setEnteredByUserId(getCurrentUser(request));
            }
            unitType.setDescription(lclUnitTypeForm.getDescription());
            unitType.setRefrigerated(lclUnitTypeForm.isRefrigerated());
            unitType.setEliteType(lclUnitTypeForm.getEliteType().toUpperCase());
            unitType.setShortDesc(lclUnitTypeForm.getShortDesc().toUpperCase());
            unitType.setInteriorLengthImperial(new BigDecimal(lclUnitTypeForm.getInteriorLengthImperial()));
            unitType.setInteriorLengthMetric(new BigDecimal(lclUnitTypeForm.getInteriorLengthMetric()));
            unitType.setInteriorWidthImperial(new BigDecimal(lclUnitTypeForm.getInteriorWidthImperial()));
            unitType.setInteriorWidthMetric(new BigDecimal(lclUnitTypeForm.getInteriorWidthMetric()));
            unitType.setInteriorHeightImperial(new BigDecimal(lclUnitTypeForm.getInteriorHeightImperial()));
            unitType.setInteriorHeightMetric(new BigDecimal(lclUnitTypeForm.getInteriorHeightMetric()));
            unitType.setDoorHeightImperial(new BigDecimal(lclUnitTypeForm.getDoorHeightImperial()));
            unitType.setDoorHeightMetric(new BigDecimal(lclUnitTypeForm.getDoorHeightMetric()));
            unitType.setDoorWidthImperial(new BigDecimal(lclUnitTypeForm.getDoorWidthImperial()));
            unitType.setDoorWidthMetric(new BigDecimal(lclUnitTypeForm.getDoorWidthMetric()));
            unitType.setGrossWeightImperial(new BigDecimal(lclUnitTypeForm.getGrossWeightImperial()));
            unitType.setGrossWeightMetric(new BigDecimal(lclUnitTypeForm.getGrossWeightMetric()));
            unitType.setTareWeightImperial(new BigDecimal(lclUnitTypeForm.getTareWeightImperial()));
            unitType.setTareWeightMetric(new BigDecimal(lclUnitTypeForm.getTareWeightMetric()));
            unitType.setVolumeImperial(new BigDecimal(lclUnitTypeForm.getVolumeImperial()));
            unitType.setVolumeMetric(new BigDecimal(lclUnitTypeForm.getVolumeMetric()));
            unitType.setTargetVolumeImperial(new BigDecimal(lclUnitTypeForm.getTargetVolumeImperial()));
            unitType.setTargetVolumeMetric(new BigDecimal(lclUnitTypeForm.getTargetVolumeMetric()));
            unitType.setEnabledLclAir(lclUnitTypeForm.isEnabledLclAir());
            unitType.setEnabledLclExports(lclUnitTypeForm.isEnabledLclExports());
            unitType.setEnabledLclImports(lclUnitTypeForm.isEnabledLclImports());
            unitType.setRemarks(lclUnitTypeForm.getRemarks());
            unitType.setModifiedDatetime(new Date());
            unitType.setModifiedByUserId(getCurrentUser(request));
            unitTypeDAO.saveOrUpdate(unitType);
            List<UnitType> unitTypeList = unitTypeDAO.getAllUnitTypeList(String.valueOf(lclUnitTypeForm.getId()));
            request.setAttribute("unitTypeList", unitTypeList);
            String message = "Unit Type details saved successfully";
            request.setAttribute("message", message);
        }
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward editUnitType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitTypeForm lclUnitTypeForm = (LclUnitTypeForm) form;
        lclUnitTypeForm.setId(lclUnitTypeForm.getId());
        lclUnitTypeForm.setDescription(lclUnitTypeForm.getDescription());
        lclUnitTypeForm.setRefrigerated(lclUnitTypeForm.isRefrigerated());
        lclUnitTypeForm.setEliteType(lclUnitTypeForm.getEliteType());
        lclUnitTypeForm.setShortDesc(lclUnitTypeForm.getShortDesc());
        lclUnitTypeForm.setInteriorLengthImperial(lclUnitTypeForm.getInteriorLengthImperial());
        lclUnitTypeForm.setInteriorLengthMetric(lclUnitTypeForm.getInteriorLengthMetric());
        lclUnitTypeForm.setInteriorWidthImperial(lclUnitTypeForm.getInteriorWidthImperial());
        lclUnitTypeForm.setInteriorWidthMetric(lclUnitTypeForm.getInteriorWidthMetric());
        lclUnitTypeForm.setInteriorHeightImperial(lclUnitTypeForm.getInteriorHeightImperial());
        lclUnitTypeForm.setInteriorHeightMetric(lclUnitTypeForm.getInteriorHeightMetric());
        lclUnitTypeForm.setDoorHeightImperial(lclUnitTypeForm.getDoorHeightImperial());
        lclUnitTypeForm.setDoorHeightMetric(lclUnitTypeForm.getDoorHeightMetric());
        lclUnitTypeForm.setDoorWidthImperial(lclUnitTypeForm.getDoorWidthImperial());
        lclUnitTypeForm.setDoorWidthMetric(lclUnitTypeForm.getDoorWidthMetric());
        lclUnitTypeForm.setGrossWeightImperial(lclUnitTypeForm.getGrossWeightImperial());
        lclUnitTypeForm.setGrossWeightMetric(lclUnitTypeForm.getGrossWeightMetric());
        lclUnitTypeForm.setTareWeightImperial(lclUnitTypeForm.getTareWeightImperial());
        lclUnitTypeForm.setTareWeightMetric(lclUnitTypeForm.getTareWeightMetric());
        lclUnitTypeForm.setVolumeImperial(lclUnitTypeForm.getVolumeImperial());
        lclUnitTypeForm.setVolumeMetric(lclUnitTypeForm.getVolumeMetric());
        lclUnitTypeForm.setTargetVolumeImperial(lclUnitTypeForm.getTargetVolumeImperial());
        lclUnitTypeForm.setTargetVolumeMetric(lclUnitTypeForm.getTargetVolumeMetric());
        lclUnitTypeForm.setEnabledLclAir(lclUnitTypeForm.isEnabledLclAir());
        lclUnitTypeForm.setEnabledLclExports(lclUnitTypeForm.isEnabledLclExports());
        lclUnitTypeForm.setEnabledLclImports(lclUnitTypeForm.isEnabledLclImports());
        lclUnitTypeForm.setRemarks(lclUnitTypeForm.getRemarks());
        request.setAttribute("lclUnitTypeForm", lclUnitTypeForm);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward addUnitType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitTypeForm lclUnitTypeForm = (LclUnitTypeForm) form;
        lclUnitTypeForm.setRefrigerated(false);
        lclUnitTypeForm.setEnabledLclAir(false);
        lclUnitTypeForm.setEnabledLclExports(false);
        lclUnitTypeForm.setEnabledLclImports(false);
        request.setAttribute("lclUnitTypeForm", lclUnitTypeForm);
        return mapping.findForward("displayNewUnit");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitTypeForm lclUnitTypeForm = (LclUnitTypeForm) form;
        UnitType unitTypeList = new UnitTypeDAO().getByProperty("description", lclUnitTypeForm.getDescription());
        request.setAttribute("description", lclUnitTypeForm.getDescription());
        request.setAttribute("unitTypeList", unitTypeList);
        return mapping.findForward("displayUnit");
    }
}
