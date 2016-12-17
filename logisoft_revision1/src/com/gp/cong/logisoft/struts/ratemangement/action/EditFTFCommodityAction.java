/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.FTFCommodityCharges;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.EditFTFCommodityForm;

/**
 * MyEclipse Struts Creation date: 06-25-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/editFTFCommodity" name="editFTFCommodityForm"
 *                input="/jsps/ratemanagement/editFTFCommodity.jsp"
 *                scope="request" validate="true"
 */
public class EditFTFCommodityAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        EditFTFCommodityForm editFTFCommodityForm = (EditFTFCommodityForm) form;// TODO
        // Auto-generated
        // method
        // stub
        String buttonValue = editFTFCommodityForm.getButtonValue();
        HttpSession session = ((HttpServletRequest) request).getSession();
        FTFCommodityCharges ftfCommodityCharges = new FTFCommodityCharges();
        double amt = 0;
        List csssList = new ArrayList();
        String FORWARD = null;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode genObj = new GenericCode();
        if (session.getAttribute("addftfcommodity") != null) {
            ftfCommodityCharges = (FTFCommodityCharges) session.getAttribute("addftfcommodity");
        }
        String standard = editFTFCommodityForm.getStandard();
        String chargeType = editFTFCommodityForm.getChargeType();
        String amtPerCft = editFTFCommodityForm.getAmtPerCft();
        String amtPer100lbs = editFTFCommodityForm.getAmtPer100lbs();
        String amtPerCbm = editFTFCommodityForm.getAmtPerCbm();
        String amtPer1000kg = editFTFCommodityForm.getAmtPer1000kg();
        String amount = editFTFCommodityForm.getAmount();
        String percentage = editFTFCommodityForm.getPercentage();
        String minAmt = editFTFCommodityForm.getMinAmt();
        String effectiveDate = editFTFCommodityForm.getTxtItemcreatedon();
        String asFrfgted = editFTFCommodityForm.getAsFrfgted();
        String insuranceRate = editFTFCommodityForm.getInsuranceRate();
        String insuranceAmt = editFTFCommodityForm.getInsuranceAmt();

        ftfCommodityCharges.setStandard(editFTFCommodityForm.getStandard());
        if (editFTFCommodityForm.getAmtPerCft() != null) {
            amt = Double.parseDouble(editFTFCommodityForm.getAmtPerCft());
        }

        if (standard != null && standard.equals("on")) {
            ftfCommodityCharges.setStandard("Y");
        } else {
            ftfCommodityCharges.setStandard("N");
        }
        if (asFrfgted != null && asFrfgted.equals("on")) {
            ftfCommodityCharges.setAsFrfgted("X");
        }
        /*
         * else { ftfCommodityCharges.setAsFrfgted("off"); }
         */
        if (amtPerCft != null && !amtPerCft.equals("")) {
            ftfCommodityCharges.setAmtPerCft(new Double(amtPerCft));
        }
        if (amtPer100lbs != null && !amtPer100lbs.equals("")) {
            ftfCommodityCharges.setAmtPer100lbs(new Double(amtPer100lbs));
        }
        if (amtPerCbm != null && !amtPerCbm.equals("")) {
            ftfCommodityCharges.setAmtPerCbm(new Double(amtPerCbm));
        }
        if (amtPer1000kg != null && !amtPer1000kg.equals("")) {
            ftfCommodityCharges.setAmtPer1000kg(new Double(amtPer1000kg));
        }
        if (amount != null && !amount.equals("")) {
            ftfCommodityCharges.setAmount(new Double(amount));
        }
        if (percentage != null && !percentage.equals("")) {
            double d = Double.parseDouble(editFTFCommodityForm.getPercentage());
            ftfCommodityCharges.setPercentage(d / 1000);
        }
        if (minAmt != null && !minAmt.equals("")) {
            ftfCommodityCharges.setMinAmt(new Double(minAmt));
        }
        if (insuranceRate != null && !insuranceRate.equals("")) {
            ftfCommodityCharges.setInsuranceRate(new Double(insuranceRate));
        }
        if (insuranceAmt != null && !insuranceAmt.equals("")) {
            ftfCommodityCharges.setInsuranceAmt(new Double(insuranceAmt));
        }
        if (effectiveDate != null && effectiveDate != "") {
            Date javaDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                javaDate = sdf.parse(effectiveDate);
            ftfCommodityCharges.setEffectiveDate(javaDate);

        }
        // --------------------------------------------------------------------
        if (buttonValue != null && buttonValue.equals("add")) {

            if (session.getAttribute("ftfcssList") != null) {
                csssList = (List) session.getAttribute("ftfcssList");

                for (int i = 0; i < csssList.size(); i++) {
                    FTFCommodityCharges ftfCommCharges = (FTFCommodityCharges) csssList.get(i);
                    if (ftfCommCharges != null && ftfCommodityCharges != null) {
                        if (ftfCommodityCharges.getChargeCode().getId() == ftfCommCharges.getChargeCode().getId()) {

                            ftfCommodityCharges.setId(ftfCommCharges.getId());
                            csssList.remove(ftfCommCharges);
                        }

                    }
                }
            }
            csssList.add(ftfCommodityCharges);
            session.setAttribute("ftfcssList", csssList);
            FORWARD = "addftfcommodity";
            if (session.getAttribute("addftfcommodity") != null) {
                session.removeAttribute("addftfcommodity");
            }
        }
        // ------------------------------------------------------------------
        if (session.getAttribute("addftfcommodity") != null) {
            ftfCommodityCharges = (FTFCommodityCharges) session.getAttribute("addftfcommodity");
        } else {
            ftfCommodityCharges = new FTFCommodityCharges();
        }
        if (buttonValue.equals("delete")) {
            if (session.getAttribute("ftfcssList") != null) {
                csssList = (List) session.getAttribute("ftfcssList");
                for (int i = 0; i < csssList.size(); i++) {
                    FTFCommodityCharges ftfCommCharges = (FTFCommodityCharges) csssList.get(i);
                    if (ftfCommodityCharges.getChargeCode() != null && ftfCommCharges.getChargeCode() != null) {

                        if (ftfCommodityCharges.getChargeCode().getId() == ftfCommCharges.getChargeCode().getId()) {
                            csssList.remove(ftfCommCharges);
                        }
                    }
                }
                session.setAttribute("ftfcssList", csssList);
                FORWARD = "addftfcommodity";
            }
        } else if (buttonValue.equals("cancel")) {
            if (session.getAttribute("addftfcommodity") != null) {
                session.removeAttribute("addftfcommodity");
            }
        }
        FORWARD = "addftfcommodity";
        return mapping.findForward(FORWARD);

    }
}