/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.AirRatesBean;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.RetailCommodityCharges;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.RetailEditCSSCForm;

/**
 * MyEclipse Struts Creation date: 05-19-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/retailEditCSSC" name="retailEditCSSCForm"
 *                input="/jsps/ratemanagement/retailEditCSSC.jsp"
 *                scope="request" validate="true"
 */
public class RetailEditCSSCAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response)throws Exception{
        RetailEditCSSCForm retailEditCSSCForm = (RetailEditCSSCForm) form;// TODO
        // Auto-generated
        // method
        // stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String forwardName = "";
        RetailCommodityCharges retailCommodityCharges = new RetailCommodityCharges();

        String buttonValue = retailEditCSSCForm.getButtonValue();
        String standard = retailEditCSSCForm.getStandard();
        String exclude = retailEditCSSCForm.getExclude();
        String asFreighted = retailEditCSSCForm.getAsFreightedCheckBox();
        int index = 0;
        AirRatesBean airRatesBean = new AirRatesBean();
        airRatesBean.setStandard(retailEditCSSCForm.getStandard());
        airRatesBean.setAsFrfgted(retailEditCSSCForm.getAsFreightedCheckBox());
        request.setAttribute("airRatesBean", airRatesBean);
        if (session.getAttribute("retailCommodityCharges") != null) {
            retailCommodityCharges = (RetailCommodityCharges) session.getAttribute("retailCommodityCharges");
        } else {
            retailCommodityCharges = new RetailCommodityCharges();
        }

        if (asFreighted != null && asFreighted.equalsIgnoreCase("on")) {
            retailCommodityCharges.setAsFreightedCheckBox("X");
        } else if (asFreighted == null) {
            retailCommodityCharges.setAsFreightedCheckBox(null);
        }

        List csssList = new ArrayList();

        GenericCode genObj = null;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        String type = retailEditCSSCForm.getChargeType();

        forwardName = "csssEdit";
        if (exclude != null && exclude.equalsIgnoreCase("on")) {
            retailCommodityCharges.setExclude("Y");
        } else if (exclude == null) {
            retailCommodityCharges.setExclude("N");
        }
        if (standard != null && standard.equalsIgnoreCase("on")) {
            retailCommodityCharges.setStandard("Y");
        } else if (standard == null) {
            retailCommodityCharges.setStandard("N");
        }
        if (retailEditCSSCForm.getAmtPerCft() != null && !retailEditCSSCForm.getAmtPerCft().equals("")) {
            retailCommodityCharges.setAmtPerCft(retailEditCSSCForm.getAmtPerCft());
        } else {
            retailCommodityCharges.setAmtPerCft(0.0);
        }
        if (retailEditCSSCForm.getInsuranceAmt() != null && !retailEditCSSCForm.getInsuranceAmt().equals("")) {
            retailCommodityCharges.setInsuranceAmt(new Double(
                    retailEditCSSCForm.getInsuranceAmt()));
        } else {
            retailCommodityCharges.setInsuranceAmt(0.0);
        }
        if (retailEditCSSCForm.getInsuranceRate() != null && !retailEditCSSCForm.getInsuranceRate().equals("")) {
            retailCommodityCharges.setInsuranceRate(new Double(
                    retailEditCSSCForm.getInsuranceRate()));
        } else {
            retailCommodityCharges.setInsuranceRate(0.0);
        }
        if (retailEditCSSCForm.getAmtPer100lbs() != null && !retailEditCSSCForm.getAmtPer100lbs().equals("")) {
            retailCommodityCharges.setAmtPer100lbs(retailEditCSSCForm.getAmtPer100lbs());
        } else {
            retailCommodityCharges.setAmtPer100lbs(0.0);
        }
        if (retailEditCSSCForm.getAmtPerCbm() != null && !retailEditCSSCForm.getAmtPerCbm().equals("")) {
            retailCommodityCharges.setAmtPerCbm(retailEditCSSCForm.getAmtPerCbm());
        } else {
            retailCommodityCharges.setAmtPerCbm(0.0);
        }
        if (retailEditCSSCForm.getAmtPer1000kg() != null && !retailEditCSSCForm.getAmtPer1000kg().equals("")) {
            retailCommodityCharges.setAmtPer1000kg(retailEditCSSCForm.getAmtPer1000kg());
        } else {
            retailCommodityCharges.setAmtPer1000kg(0.0);
        }
        if (retailEditCSSCForm.getAmount() != null && !retailEditCSSCForm.getAmount().equals("")) {
            retailCommodityCharges.setAmount(retailEditCSSCForm.getAmount());
        } else {
            retailCommodityCharges.setAmount(0.0);
        }
        if (retailEditCSSCForm.getPercentage() != null && !retailEditCSSCForm.getPercentage().equals("")) {
            double d = Double.parseDouble(retailEditCSSCForm.getPercentage());
            retailCommodityCharges.setPercentage(d / 1000);
        } else {
            retailCommodityCharges.setPercentage(0.0);
        }
        if (retailEditCSSCForm.getMinAmt() != null && !retailEditCSSCForm.getMinAmt().equals("")) {
            retailCommodityCharges.setMinAmt(retailEditCSSCForm.getMinAmt());
        } else {
            retailCommodityCharges.setMinAmt(0.0);
        }
        if (retailEditCSSCForm.getTxtItemcreatedon() != null && retailEditCSSCForm.getTxtItemcreatedon() != "") {
            java.util.Date javaDate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                javaDate = sdf.parse(retailEditCSSCForm.getTxtItemcreatedon());
            retailCommodityCharges.setEffectiveDate(javaDate);
        }

        session.setAttribute("retailCommodityCharges", retailCommodityCharges);
        if (buttonValue != null && buttonValue.equals("add")) {

            if (session.getAttribute("retailcssList") != null) {

                csssList = (List) session.getAttribute("retailcssList");
                for (int i = 0; i < csssList.size(); i++) {

                    RetailCommodityCharges retailComm = (RetailCommodityCharges) csssList.get(i);

                    if (retailComm.getChargeType() != null && retailComm.getChargeType().equals("")) {
                        if (retailComm.getChargeType().getId().equals(
                                retailCommodityCharges.getChargeType().getId()) && retailComm.getChargeCode().getId().equals(
                                retailCommodityCharges.getChargeCode().getId())) {

                            retailCommodityCharges.setIndex(retailComm.getIndex());

                            csssList.remove(retailComm);
                            csssList.add(retailCommodityCharges);
                            break;
                        }
                    }
                }

            }

            session.setAttribute("retailcssList", csssList);

            forwardName = "csssAdd";
            request.setAttribute("buttonValue", buttonValue);
            if (session.getAttribute("retailCommodityCharges") != null) {
                session.removeAttribute("retailCommodityCharges");
            }
        } else if (buttonValue != null && buttonValue.equals("cancel")) {
            if (session.getAttribute("retailCommodityCharges") != null) {
                session.removeAttribute("retailCommodityCharges");
            }
            forwardName = "csssAdd";
        }
        return mapping.findForward(forwardName);

    }
}