package com.gp.cong.logisoft.struts.ratemangement.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.gp.cong.logisoft.domain.LCLColoadCommodityCharges;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.AddLclColoadCommodityForm;

public class AddLclColoadCommodityAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        AddLclColoadCommodityForm addLclColoadCommodityForm = (AddLclColoadCommodityForm) form;// TODO
        // Auto-generated
        // method
        // stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = addLclColoadCommodityForm.getButtonValue();
        LCLColoadCommodityCharges lCLColoadCommodityCharges = null;// new
        // LCLColoadCommodityCharges();
        String chargeType = addLclColoadCommodityForm.getChargeType();
        String chargeCode = addLclColoadCommodityForm.getCharge();
        String chargeCodeDesc = addLclColoadCommodityForm.getDesc();

        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode genObj = new GenericCode();
        String standard = addLclColoadCommodityForm.getStandard();
        String amtPerCft = addLclColoadCommodityForm.getAmtPerCft();
        String amtPer100lbs = addLclColoadCommodityForm.getAmtPer100lbs();
        String amtPerCbm = addLclColoadCommodityForm.getAmtPerCbm();
        String amtPer1000kg = addLclColoadCommodityForm.getAmtPer1000kg();
        String exclude = addLclColoadCommodityForm.getExclude();
        String amount = addLclColoadCommodityForm.getAmount();
        String percentage = addLclColoadCommodityForm.getPercentage();
        String minAmt = addLclColoadCommodityForm.getMinAmt();
        String effectiveDate = addLclColoadCommodityForm.getTxtItemcreatedon();
        String asFrfgted = addLclColoadCommodityForm.getAsFrfgted();
        String insuranceRate = addLclColoadCommodityForm.getInsuranceRate();
        String insuranceAmt = addLclColoadCommodityForm.getInsuranceAmt();
        GenericCode genericCodeObj = new GenericCode();
        List lclcoloadcssList = new ArrayList();
        AirRatesBean airRatesBean = new AirRatesBean();
        airRatesBean.setStandard(standard);
        airRatesBean.setAsFrfgted(asFrfgted);
        request.setAttribute("airRatesBean", airRatesBean);
        String forwardName = "";
        String user = "";
        if (request.getParameter("ind") != null) {
            LCLColoadCommodityCharges lclCoload = new LCLColoadCommodityCharges();
            int ind = Integer.parseInt(request.getParameter("ind"));
            List codeList1 = (List) session.getAttribute("lclcoloadcssList");
            lclCoload = (LCLColoadCommodityCharges) codeList1.get(ind);
            session.setAttribute("addlclcoloadcommodity", lclCoload);
            forwardName = "addccEdit";
        } else {
            if (session.getAttribute("addlclcoloadcommodity") != null) {
                lCLColoadCommodityCharges = (LCLColoadCommodityCharges) session.getAttribute("addlclcoloadcommodity");

            } else {
                lCLColoadCommodityCharges = new LCLColoadCommodityCharges();
            }
            if (chargeType != null && !chargeType.equals("0")) {
                genObj = genericCodeDAO.findById(Integer.parseInt(chargeType));
                lCLColoadCommodityCharges.setChargeType(genObj);
            }
            if (exclude != null) {
                if (exclude.equals("on")) {
                    exclude = "y";
                } else {
                    exclude = "N";
                }
                lCLColoadCommodityCharges.setExclude(exclude);
                GenericCode gen = null;
                lCLColoadCommodityCharges.setChargeType(gen);
            }

            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            if (userId != null) {
                lCLColoadCommodityCharges.setWhoChanged(userId.getLoginName());
            }
            lCLColoadCommodityCharges.setWhoChanged(user);

            if (standard != null && standard.equals("on")) {
                lCLColoadCommodityCharges.setStandard("Y");
            } else {
                lCLColoadCommodityCharges.setStandard("N");
            }
            if (asFrfgted != null && asFrfgted.equals("on")) {
                lCLColoadCommodityCharges.setAsFrfgted("X");
            } else {
                lCLColoadCommodityCharges.setAsFrfgted(null);
            }
            if (amtPerCft != null && !amtPerCft.equals("")) {
                lCLColoadCommodityCharges.setAmtPerCft(new Double(amtPerCft));
            } else {
                lCLColoadCommodityCharges.setAmtPerCft(0.0);
            }

            if (amtPer100lbs != null && !amtPer100lbs.equals("")) {
                lCLColoadCommodityCharges.setAmtPer100lbs(new Double(
                        amtPer100lbs));
            } else {
                lCLColoadCommodityCharges.setAmtPer100lbs(0.0);
            }
            if (amtPerCbm != null && !amtPerCbm.equals("")) {
                lCLColoadCommodityCharges.setAmtPerCbm(new Double(amtPerCbm));
            } else {
                lCLColoadCommodityCharges.setAmtPerCbm(0.0);
            }
            if (amtPer1000kg != null && !amtPer1000kg.equals("")) {
                lCLColoadCommodityCharges.setAmtPer1000kg(new Double(
                        amtPer1000kg));
            } else {
                lCLColoadCommodityCharges.setAmtPer1000kg(0.0);
            }
            if (amount != null && !amount.equals("")) {
                lCLColoadCommodityCharges.setAmount(new Double(amount));
            } else {
                lCLColoadCommodityCharges.setAmount(0.0);
            }
            if (percentage != null && !percentage.equals("")) {

                double d = Double.parseDouble(percentage);
                lCLColoadCommodityCharges.setPercentage(d / 1000);

            } else {
                lCLColoadCommodityCharges.setPercentage(0.0);
            }

            if (minAmt != null && !minAmt.equals("")) {
                lCLColoadCommodityCharges.setMinAmt(new Double(minAmt));
            } else {
                lCLColoadCommodityCharges.setMinAmt(0.0);
            }
            if (insuranceRate != null && !insuranceRate.equals("")) {
                lCLColoadCommodityCharges.setInsuranceRate(new Double(
                        insuranceRate));
            }
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            if (userId != null) {
                lCLColoadCommodityCharges.setWhoChanged(userId.getLoginName());
            } else {
                lCLColoadCommodityCharges.setInsuranceRate(0.0);
            }
            if (insuranceAmt != null && !insuranceAmt.equals("")) {
                lCLColoadCommodityCharges.setInsuranceAmt(new Double(
                        insuranceAmt));
            } else {
                lCLColoadCommodityCharges.setInsuranceAmt(0.0);
            }
            if (effectiveDate != null && effectiveDate != "") {
                Date javaDate = null;
                Calendar c1 = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "MM/dd/yyyy hh:mm aaa");
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aaa");
                javaDate = sdf.parse(effectiveDate + " "
                        + sdf1.format(new Date()));
                lCLColoadCommodityCharges.setEffectiveDate(javaDate);
            }
            session.setAttribute("addlclcoloadcommodity",
                    lCLColoadCommodityCharges);

            if (!buttonValue.equals("") && buttonValue.equals("add")) {

                if (lCLColoadCommodityCharges.getChargeCode() != null) {
                    if (session.getAttribute("lclcoloadcssList") != null) {
                        lclcoloadcssList = (List) session.getAttribute("lclcoloadcssList");
                    } else {
                        lclcoloadcssList = new ArrayList();

                    }
                    lclcoloadcssList.add(lCLColoadCommodityCharges);
                    session.setAttribute("lclcoloadcssList", lclcoloadcssList);

                    if (session.getAttribute("addlclcoloadcommodity") != null) {
                        session.removeAttribute("addlclcoloadcommodity");
                    }
                    if (session.getAttribute("listLclCoitem") != null) {
                        session.removeAttribute("listLclCoitem");
                    }
                } else {
                    String msg = "Please Select correct Charge Code... ";
                    request.setAttribute("exist", msg);

                }
            }
            if (buttonValue != null && buttonValue.equals("addItem")) {

                session.setAttribute("listLclCoitem", "listLclCoitem");
            }
            if (buttonValue != null && buttonValue.equals("delete")) {
                List csssList = new ArrayList();
                if (session.getAttribute("lclcoloadcssList") != null) {
                    csssList = (List) session.getAttribute("lclcoloadcssList");

                    for (int i = 0; i < csssList.size(); i++) {
                        LCLColoadCommodityCharges lCLColoadCommCharges = (LCLColoadCommodityCharges) csssList.get(i);
                        if (addLclColoadCommodityForm.getCommodityId() != null
                                && lCLColoadCommCharges.getChargeCode() != null
                                && addLclColoadCommodityForm.getCommodityId().equals(lCLColoadCommCharges.getChargeCode().getCode())) {
                            csssList.remove(lCLColoadCommCharges);
                        }
                    }
                }
                session.setAttribute("lclcoloadcssList", csssList);
            }
            if (buttonValue != null && buttonValue.equals("popupsearch")) {
                List list = new ArrayList();

                String msg = "Charge Code already exists... ";

                if (session.getAttribute("lclcoloadcssList") != null) {
                    list = (List) session.getAttribute("lclcoloadcssList");

                    for (int i = 0; i < list.size(); i++) {
                        lCLColoadCommodityCharges = (LCLColoadCommodityCharges) list.get(i);
                        if (lCLColoadCommodityCharges.getChargeCode().getCode().equals(chargeCode)) {
                            request.setAttribute("exist", msg);
                        }
                        if (lCLColoadCommodityCharges.getChargeCode().getCodedesc().equals(chargeCodeDesc)) {
                            request.setAttribute("exist", msg);
                        }
                    }
                }
                if (request.getAttribute("exist") == null
                        && (chargeCode != null || !chargeCode.equals(""))) {

                    LCLColoadCommodityCharges commodityCharges = new LCLColoadCommodityCharges();
                    GenericCodeDAO genericDAO = new GenericCodeDAO();
                    genericCodeObj = null;
                    List codeList = genericDAO.findGenericCode("2", chargeCode);

                    if (codeList.size() > 0) {
                        genericCodeObj = (GenericCode) codeList.get(0);
                    }
                    if (genericCodeObj != null) {
                        commodityCharges.setChargeCode(genericCodeObj);
                        session.setAttribute("addlclcoloadcommodity",
                                commodityCharges);
                    }
                }

                if (request.getAttribute("exist") == null
                        && (chargeCodeDesc != null || !chargeCodeDesc.equals(""))) {

                    LCLColoadCommodityCharges commodityCharges = new LCLColoadCommodityCharges();
                    GenericCodeDAO genericDAO = new GenericCodeDAO();
                    genericCodeObj = null;
                    List codeList = genericDAO.findByCodedesc(chargeCodeDesc);

                    if (codeList.size() > 0) {
                        genericCodeObj = (GenericCode) codeList.get(0);
                    }
                    if (genericCodeObj != null) {
                        commodityCharges.setChargeCode(genericCodeObj);
                        session.setAttribute("addlclcoloadcommodity",
                                commodityCharges);
                    }
                }
            }
            forwardName = "addlclcoloadcommodity";
        }

        return mapping.findForward(forwardName);
    }
}
