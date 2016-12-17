/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.LCLColoadCommodityCharges;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.EditLclColoadCommodityForm;

/**
 * MyEclipse Struts Creation date: 05-30-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/editLclColoadCommodity"
 *                name="editLclColoadCommodityForm"
 *                input="/jsps/ratemanagement/EditLclColoadCommodity.jsp"
 *                scope="request" validate="true"
 */
public class EditLclColoadCommodityAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)throws Exception {
        EditLclColoadCommodityForm editLclColoadCommodityForm = (EditLclColoadCommodityForm) form;// TODO
        // Auto-generated
        // method
        // stub
        String buttonValue = editLclColoadCommodityForm.getButtonValue();
        HttpSession session = ((HttpServletRequest) request).getSession();
        LCLColoadCommodityCharges lCLColoadCommodityCharges = new LCLColoadCommodityCharges();
        double amt = 0;
        List csssList = new ArrayList();
        String FORWARD = null;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode genObj = new GenericCode();
        if (session.getAttribute("addlclcoloadcommodity") != null) {
            lCLColoadCommodityCharges = (LCLColoadCommodityCharges) session.getAttribute("addlclcoloadcommodity");
        }
        String standard = editLclColoadCommodityForm.getStandard();
        String chargeType = editLclColoadCommodityForm.getChargeType();
        String amtPerCft = editLclColoadCommodityForm.getAmtPerCft();
        String amtPer100lbs = editLclColoadCommodityForm.getAmtPer100lbs();
        String amtPerCbm = editLclColoadCommodityForm.getAmtPerCbm();
        String amtPer1000kg = editLclColoadCommodityForm.getAmtPer1000kg();
        String amount = editLclColoadCommodityForm.getAmount();
        String percentage = editLclColoadCommodityForm.getPercentage();
        String minAmt = editLclColoadCommodityForm.getMinAmt();
        String effectiveDate = editLclColoadCommodityForm.getTxtItemcreatedon();
        String asFrfgted = editLclColoadCommodityForm.getAsFrfgted();
        String insuranceRate = editLclColoadCommodityForm.getInsuranceRate();
        String insuranceAmt = editLclColoadCommodityForm.getInsuranceAmt();

        lCLColoadCommodityCharges.setStandard(editLclColoadCommodityForm.getStandard());
        if (editLclColoadCommodityForm.getAmtPerCft() != null) {
            amt = Double.parseDouble(editLclColoadCommodityForm.getAmtPerCft());
        }

        if (standard != null && standard.equals("on")) {
            lCLColoadCommodityCharges.setStandard("Y");
        } else {
            lCLColoadCommodityCharges.setStandard("N");
        }
        if (asFrfgted != null && asFrfgted.equals("on")) {
            lCLColoadCommodityCharges.setAsFrfgted("X");
        } else {
            lCLColoadCommodityCharges.setAsFrfgted("off");
        }
        if (amtPerCft != null && !amtPerCft.equals("")) {
            lCLColoadCommodityCharges.setAmtPerCft(new Double(amtPerCft));
        }
        if (amtPer100lbs != null && !amtPer100lbs.equals("")) {
            lCLColoadCommodityCharges.setAmtPer100lbs(new Double(amtPer100lbs));
        }
        if (amtPerCbm != null && !amtPerCbm.equals("")) {
            lCLColoadCommodityCharges.setAmtPerCbm(new Double(amtPerCbm));
        }
        if (amtPer1000kg != null && !amtPer1000kg.equals("")) {
            lCLColoadCommodityCharges.setAmtPer1000kg(new Double(amtPer1000kg));
        }
        if (amount != null && !amount.equals("")) {
            lCLColoadCommodityCharges.setAmount(new Double(amount));
        }
        if (percentage != null && !percentage.equals("")) {

            double d = Double.parseDouble(percentage);
            lCLColoadCommodityCharges.setPercentage(d / 1000);

        }
        if (minAmt != null && !minAmt.equals("")) {
            lCLColoadCommodityCharges.setMinAmt(new Double(minAmt));
        }
        if (insuranceRate != null && !insuranceRate.equals("")) {
            lCLColoadCommodityCharges.setInsuranceRate(new Double(insuranceRate));
        }
        if (insuranceAmt != null && !insuranceAmt.equals("")) {
            lCLColoadCommodityCharges.setInsuranceAmt(new Double(insuranceAmt));
        }
        if (effectiveDate != null && effectiveDate != "") {
            Date javaDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                javaDate = sdf.parse(effectiveDate);
            lCLColoadCommodityCharges.setEffectiveDate(javaDate);

        }
        // --------------------------------------------------------------------
        if (buttonValue != null && buttonValue.equals("add")) {
            if (session.getAttribute("lclcoloadcssList") != null) {
                csssList = (List) session.getAttribute("lclcoloadcssList");

                for (int i = 0; i < csssList.size(); i++) {
                    LCLColoadCommodityCharges lCLColoadCommCharges = (LCLColoadCommodityCharges) csssList.get(i);
                    if (lCLColoadCommCharges != null && lCLColoadCommodityCharges != null) {
                        if (lCLColoadCommodityCharges.getChargeCode().getId() == lCLColoadCommCharges.getChargeCode().getId()) {

                            lCLColoadCommodityCharges.setId(lCLColoadCommCharges.getId());
                            csssList.remove(lCLColoadCommCharges);
                        }

                    }
                }
            }
            csssList.add(lCLColoadCommodityCharges);
            session.setAttribute("lclcoloadcssList", csssList);
            FORWARD = "addlclcoloadcommodity";
            if (session.getAttribute("addlclcoloadcommodity") != null) {
                session.removeAttribute("addlclcoloadcommodity");
            }
        }
        // ------------------------------------------------------------------
        if (session.getAttribute("addlclcoloadcommodity") != null) {
            lCLColoadCommodityCharges = (LCLColoadCommodityCharges) session.getAttribute("addlclcoloadcommodity");
        } else {
            lCLColoadCommodityCharges = new LCLColoadCommodityCharges();
        }
        if (buttonValue != null && buttonValue.equals("delete")) {
            if (session.getAttribute("lclcoloadcssList") != null) {
                csssList = (List) session.getAttribute("lclcoloadcssList");

                for (int i = 0; i < csssList.size(); i++) {
                    LCLColoadCommodityCharges lCLColoadCommCharges = (LCLColoadCommodityCharges) csssList.get(i);
                    if (lCLColoadCommodityCharges.getChargeCode() != null && lCLColoadCommCharges.getChargeCode() != null) {
                        if (lCLColoadCommodityCharges.getChargeCode().getId() == lCLColoadCommCharges.getChargeCode().getId()) {
                            csssList.remove(lCLColoadCommCharges);
                        }
                    }
                }
            }
            session.setAttribute("lclcoloadcssList", csssList);
            FORWARD = "addlclcoloadcommodity";
        } else if (buttonValue != null && buttonValue.equals("cancel")) {
            if (session.getAttribute("addlclcoloadcommodity") != null) {
                session.removeAttribute("addlclcoloadcommodity");
            }
        }
        FORWARD = "addlclcoloadcommodity";
        return mapping.findForward(FORWARD);
    }
}