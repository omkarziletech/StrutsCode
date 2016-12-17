/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.referencedata.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclUtils;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclUnitSsAutoCosting;
import com.gp.cong.logisoft.domain.lcl.UnitType;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.UnitSsAutoCostingDAO;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;
import com.logiware.referencedata.form.LclUnitSsAutoCostingForm;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mei
 */
public class LclUnitSsAutoCostingAction extends BaseAction {

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsAutoCostingForm autoCostingForm = (LclUnitSsAutoCostingForm) form;
        return mapping.findForward("success");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsAutoCostingForm autoCostingForm = (LclUnitSsAutoCostingForm) form;
        UnitSsAutoCostingDAO autoCostingDAO = new UnitSsAutoCostingDAO();
        request.setAttribute("autoCostList", autoCostingDAO.search(autoCostingForm));
        return mapping.findForward("success");
    }

    public ActionForward addOrEditCost(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsAutoCostingForm autoCostingForm = (LclUnitSsAutoCostingForm) form;
        if (CommonUtils.isNotEmpty(autoCostingForm.getUnitSsCostId())) {
            UnitSsAutoCostingDAO autoCostingDAO = new UnitSsAutoCostingDAO();
            LclUtils utils = new LclUtils();
            LclUnitSsAutoCosting autoCosting = autoCostingDAO.findById(autoCostingForm.getUnitSsCostId());
            String origin = utils.getConcatenatedOriginByUnlocation(autoCosting.getOrigin());
            String destination = utils.getConcatenatedOriginByUnlocation(autoCosting.getDestination());
            autoCostingForm.setOriginName(origin);
            autoCostingForm.setOriginId(autoCosting.getOrigin().getId());
            autoCostingForm.setFdId(autoCosting.getDestination().getId());
            autoCostingForm.setFdName(destination);

            autoCostingForm.setCostType(autoCosting.getType());
            autoCostingForm.setAddVendorName(autoCosting.getVendor().getAccountName());
            autoCostingForm.setAddVendorNo(autoCosting.getVendor().getAccountno());
            autoCostingForm.setRateUom(autoCosting.getRateUom());
            autoCostingForm.setRatePerUomAmt(String.valueOf(autoCosting.getRatePerUom()));

            autoCostingForm.setAddUnitTypeId(autoCosting.getUnitType().getId());
            autoCostingForm.setAddGlMapCode(autoCosting.getGlMapping().getChargeCode());
            autoCostingForm.setAddGlMappingId(String.valueOf(autoCosting.getGlMapping().getId()));

            autoCostingForm.setRateAction(CommonUtils.isNotEmpty(autoCosting.getRateAction())
                    ? autoCosting.getRateAction() : "");
            autoCostingForm.setRateCondition(CommonUtils.isNotEmpty(autoCosting.getRateCondition())
                    ? autoCosting.getRateCondition() : "");
            autoCostingForm.setRateConditionQty(null != autoCosting.getRateConditionQty()
                    ? String.valueOf(autoCosting.getRateConditionQty()) : "");

            request.setAttribute("unitSsAutoCosting", autoCosting);
        }

        return mapping.findForward("addOrEdit");
    }

    public ActionForward saveCost(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclUnitSsAutoCostingForm autoCostingForm = (LclUnitSsAutoCostingForm) form;
        User user = (User) request.getSession().getAttribute("loginuser");
        Date now = new Date();
        UnitSsAutoCostingDAO autoCostingDAO = new UnitSsAutoCostingDAO();
        LclUnitSsAutoCosting unitSsAutoCosting = null;
        if (CommonUtils.isNotEmpty(autoCostingForm.getUnitSsCostId())) {
            unitSsAutoCosting = autoCostingDAO.findById(autoCostingForm.getUnitSsCostId());
        } else {
            unitSsAutoCosting = new LclUnitSsAutoCosting();
            unitSsAutoCosting.setEnteredByUser(user);
            unitSsAutoCosting.setEnteredDatetime(now);
        }
        unitSsAutoCosting.setOrigin(new UnLocation(autoCostingForm.getOriginId()));
        unitSsAutoCosting.setDestination(new UnLocation(autoCostingForm.getFdId()));
        unitSsAutoCosting.setUnitType(new UnitType(autoCostingForm.getAddUnitTypeId()));
        unitSsAutoCosting.setType(autoCostingForm.getCostType().toUpperCase());
        unitSsAutoCosting.setGlMapping(new GlMappingDAO().findById(Integer.parseInt(autoCostingForm.getAddGlMappingId())));
        unitSsAutoCosting.setRateUom(autoCostingForm.getRateUom().toUpperCase());
        unitSsAutoCosting.setRatePerUom(new BigDecimal(autoCostingForm.getRatePerUomAmt()));
        unitSsAutoCosting.setVendor(new TradingPartnerDAO().findById(autoCostingForm.getAddVendorNo()));
        unitSsAutoCosting.setRateAction(CommonUtils.isNotEmpty(autoCostingForm.getRateAction())
                ? autoCostingForm.getRateAction() : null);
        unitSsAutoCosting.setRateCondition(CommonUtils.isNotEmpty(autoCostingForm.getRateCondition())
                ? autoCostingForm.getRateCondition() : null);
        unitSsAutoCosting.setRateConditionQty(CommonUtils.isNotEmpty(autoCostingForm.getRateConditionQty())
                ? new BigDecimal(autoCostingForm.getRateConditionQty()) : null);
        unitSsAutoCosting.setModifiedByUser(user);
        unitSsAutoCosting.setModifiedDatetime(now);
        autoCostingDAO.saveOrUpdate(unitSsAutoCosting);
        return mapping.findForward("addOrEdit");
    }

    public ActionForward copyCost(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        UnitSsAutoCostingDAO autoCostingDAO = new UnitSsAutoCostingDAO();
        LclUnitSsAutoCostingForm autoCostingForm = (LclUnitSsAutoCostingForm) form;
        List<String> costIdList = Arrays.asList(autoCostingForm.getCopyCostId().split(","));
        UnLocation origin = new UnLocationDAO().findById(autoCostingForm.getCopyOriginId());
        UnLocation destination = new UnLocationDAO().findById(autoCostingForm.getCopyDestinationId());
        if (autoCostingForm.getDuplicateCostCode().equalsIgnoreCase("true")) {
            for (String unitcostId : costIdList) {
                LclUnitSsAutoCosting existCosting = autoCostingDAO.findById(Long.parseLong(unitcostId));
                autoCostingDAO.deleteUnitSsCostByOrgDest(autoCostingForm.getCopyOriginId(),
                        autoCostingForm.getCopyDestinationId(), existCosting.getGlMapping().getChargeCode(),
                        existCosting.getUnitType().getId(),existCosting.getRateUom());
            }
        }
        for (String unitcostId : costIdList) {
            LclUnitSsAutoCosting existCosting = autoCostingDAO.findById(Long.parseLong(unitcostId));
            LclUnitSsAutoCosting newCosting = new LclUnitSsAutoCosting();
            PropertyUtils.copyProperties(newCosting, existCosting);
            newCosting.setId(null);
            newCosting.setOrigin(origin);
            newCosting.setDestination(destination);
            autoCostingDAO.saveOrUpdate(newCosting);
        }
        return mapping.findForward("");
    }

}
