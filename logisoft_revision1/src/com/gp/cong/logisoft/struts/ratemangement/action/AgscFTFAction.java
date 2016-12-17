/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

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

import com.gp.cong.logisoft.beans.AirRatesBean;
import com.gp.cong.logisoft.domain.FTFStandardCharges;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.AgscFTFForm;

/**
 * MyEclipse Struts Creation date: 06-25-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/agscFTF" name="agscFTFForm"
 *                input="/jsps/ratemanagement/agscFTF.jsp" scope="request"
 *                validate="true"
 */
public class AgscFTFAction extends Action {

    java.util.Date javaDate = null;

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
        AgscFTFForm agscFTFForm = (AgscFTFForm) form;// TODO Auto-generated
        // method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String forwardName = "";

        FTFStandardCharges ftfStandardCharges = new FTFStandardCharges();

        GenericCode genericCodeObj = new GenericCode();

        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        String code = agscFTFForm.getCharge();
        String codeDesc = agscFTFForm.getDesc();
        String buttonValue = agscFTFForm.getButtonValue();
        String standard = agscFTFForm.getStandard();
        String asFreighted = agscFTFForm.getAsFrfgted();
        int index = 0;
        if (request.getParameter("ind") != null) {
            FTFStandardCharges ftfStandard = new FTFStandardCharges();
            int ind = Integer.parseInt(request.getParameter("ind"));
            List codeList1 = (List) session.getAttribute("ftfagssAdd");
            ftfStandard = (FTFStandardCharges) codeList1.get(ind);
            session.setAttribute("ftfStandardCharges", ftfStandard);
            forwardName = "ftfagssEdit";

        } else {
            if (session.getAttribute("ftfStandardCharges") != null) {
                ftfStandardCharges = (FTFStandardCharges) session.getAttribute("ftfStandardCharges");
            } else {
                ftfStandardCharges = new FTFStandardCharges();
            }
            AirRatesBean airRatesBean = new AirRatesBean();
            airRatesBean.setStandard(agscFTFForm.getStandard());
            airRatesBean.setAsFrfgted(agscFTFForm.getAsFrfgted());

            request.setAttribute("airRatesBean", airRatesBean);
            if (asFreighted != null && asFreighted.equalsIgnoreCase("on")) {
                ftfStandardCharges.setAsFrfgted("X");
            } else if (asFreighted == null) {
                ftfStandardCharges.setAsFrfgted(null);
            }
            List agssList = new ArrayList();

            GenericCode genObj = new GenericCode();
            String type = agscFTFForm.getChargeType();

            forwardName = "ftfagssAdd";

            if (type != null && !type.equals("0")) {
                genObj = (GenericCode) genericCodeDAO.findById(Integer.parseInt(type));
                ftfStandardCharges.setChargeType(genObj);
            }

            if (standard != null && standard.equalsIgnoreCase("on")) {
                ftfStandardCharges.setStandard("Y");
            } else if (standard == null) {
                ftfStandardCharges.setStandard("N");
            }
            if (agscFTFForm.getInsuranceRate() != null && !agscFTFForm.getInsuranceRate().equals("")) {
                ftfStandardCharges.setInsuranceRate(agscFTFForm.getInsuranceRate());
            } else {
                ftfStandardCharges.setInsuranceRate(0.0);
            }
            if (agscFTFForm.getInsuranceAmt() != null && !agscFTFForm.getInsuranceAmt().equals("")) {
                ftfStandardCharges.setInsuranceAmt(agscFTFForm.getInsuranceAmt());
            } else {
                ftfStandardCharges.setInsuranceAmt(0.0);
            }
            if (agscFTFForm.getAmtPerCft() != null && !agscFTFForm.getAmtPerCft().equals("")) {
                ftfStandardCharges.setAmtPerCft(agscFTFForm.getAmtPerCft());
            } else {
                ftfStandardCharges.setAmtPerCft(0.0);
            }
            if (agscFTFForm.getAmtPer100lbs() != null && !agscFTFForm.getAmtPer100lbs().equals("")) {
                ftfStandardCharges.setAmtPer100lbs(agscFTFForm.getAmtPer100lbs());
            } else {
                ftfStandardCharges.setAmtPer100lbs(0.0);
            }
            if (agscFTFForm.getAmtPerCbm() != null && !agscFTFForm.getAmtPerCbm().equals("")) {
                ftfStandardCharges.setAmtPerCbm(agscFTFForm.getAmtPerCbm());
            } else {
                ftfStandardCharges.setAmtPerCbm(0.0);
            }
            if (agscFTFForm.getAmtPer1000kg() != null && !agscFTFForm.getAmtPer1000kg().equals("")) {
                ftfStandardCharges.setAmtPer1000Kg(agscFTFForm.getAmtPer1000kg());
            } else {
                ftfStandardCharges.setAmtPer1000Kg(0.0);
            }
            if (agscFTFForm.getAmount() != null && !agscFTFForm.getAmount().equals("")) {
                ftfStandardCharges.setAmount(agscFTFForm.getAmount());
            } else {
                ftfStandardCharges.setAmount(0.0);
            }
            if (agscFTFForm.getPercentage() != null && !agscFTFForm.getPercentage().equals("")) {
                double d = Double.parseDouble(agscFTFForm.getPercentage());
                ftfStandardCharges.setPercentage(d / 1000);
            } else {
                ftfStandardCharges.setPercentage(0.0);
            }

            if (agscFTFForm.getMinAmt() != null && !agscFTFForm.getMinAmt().equals("")) {
                ftfStandardCharges.setMinAmt(agscFTFForm.getMinAmt());
            } else {
                ftfStandardCharges.setMinAmt(0.0);
            }
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            if (userId != null) {
                ftfStandardCharges.setWhoChanged(userId.getLoginName());
            }
            if (agscFTFForm.getTxtItemcreatedon() != null && agscFTFForm.getTxtItemcreatedon() != "") {

                SimpleDateFormat sdf = new SimpleDateFormat(
                        "MM/dd/yyyy hh:mm aaa");
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aaa");
                javaDate = sdf.parse(agscFTFForm.getTxtItemcreatedon() + " " + sdf1.format(new Date()));
                ftfStandardCharges.setEffectiveDate(javaDate);
            }

            session.setAttribute("ftfStandardCharges", ftfStandardCharges);

            if (buttonValue != null && buttonValue.equals("add")) {

                if (ftfStandardCharges.getChargeCode() != null) {
                    if (session.getAttribute("ftfagssAdd") != null) {
                        agssList = (List) session.getAttribute("ftfagssAdd");
                        for (int i = 0; i < agssList.size(); i++) {
                            FTFStandardCharges ftfStandard = (FTFStandardCharges) agssList.get(i);
                            if (ftfStandard.getIndex() != null) {
                                if (ftfStandard.getIndex() > index) {
                                    index = ftfStandard.getIndex();
                                }
                            } else {
                                index = agssList.size() - 1;
                            }
                        }
                        index++;
                    } else {
                        agssList = new ArrayList();
                        index++;
                    }
                    ftfStandardCharges.setIndex(index);

                    agssList.add(ftfStandardCharges);
                    session.setAttribute("ftfagssAdd", agssList);
                    if (session.getAttribute("ftfStandardCharges") != null) {
                        session.removeAttribute("ftfStandardCharges");
                    }
                    if (session.getAttribute("listAgsCoitem") != null) {
                        session.removeAttribute("listAgsCoitem");
                    }
                } else {

                    String msg = "Please Select correct Charge Code... ";
                    request.setAttribute("exist", msg);
                }
                forwardName = "ftfagssAdd";
            }

        }
        if (buttonValue.equals("delete")) {
            List agssList = new ArrayList();
            if (session.getAttribute("ftfagssAdd") != null) {
                agssList = (List) session.getAttribute("ftfagssAdd");
            }
            for (int i = 0; i < agssList.size(); i++) {
                FTFStandardCharges ftfStandard = (FTFStandardCharges) agssList.get(i);
                if (ftfStandard.getChargeCode() != null && agscFTFForm.getStandardId() != null && ftfStandard.getChargeCode().getCode().equals(agscFTFForm.getStandardId())) {
                    agssList.remove(ftfStandard);
                }
                session.setAttribute("ftfagssAdd", agssList);
            }
            if (session.getAttribute("ftfStandardCharges") != null) {
                session.removeAttribute("ftfStandardCharges");
            }
            forwardName = "ftfagssAdd";
        }
        if (buttonValue != null && buttonValue.equals("chargeCode")) {
            List list = new ArrayList();
            String msg = "Charge Code already exists... ";

            if (session.getAttribute("ftfagssAdd") != null) {
                list = (List) session.getAttribute("ftfagssAdd");

                for (int i = 0; i < list.size(); i++) {
                    ftfStandardCharges = (FTFStandardCharges) list.get(i);
                    if (ftfStandardCharges.getChargeCode().getCode().equals(
                            code)) {
                        request.setAttribute("exist", msg);
                    }
                    if (ftfStandardCharges.getChargeCode().getCodedesc().equalsIgnoreCase(codeDesc)) {
                        request.setAttribute("exist", msg);
                    }
                }
            }
            if (request.getAttribute("exist") == null && (code != null || !code.equals(""))) {
                FTFStandardCharges standardCharges = new FTFStandardCharges();
                genericCodeObj = null;
                GenericCodeDAO genericDAO = new GenericCodeDAO();
                List codeList = genericDAO.findGenericCode("2", code);

                if (codeList.size() > 0) {
                    genericCodeObj = (GenericCode) codeList.get(0);
                }
                if (genericCodeObj != null) {
                    standardCharges.setChargeCode(genericCodeObj);
                    session.setAttribute("ftfStandardCharges", standardCharges);
                }
            }

            if (request.getAttribute("exist") == null && (codeDesc != null || !codeDesc.equals(""))) {
                FTFStandardCharges standardCharges = new FTFStandardCharges();
                genericCodeObj = null;
                GenericCodeDAO genericDAO = new GenericCodeDAO();
                List codeList = genericDAO.findByCodedesc(codeDesc);

                if (codeList.size() > 0) {
                    genericCodeObj = (GenericCode) codeList.get(0);
                }
                if (genericCodeObj != null) {
                    standardCharges.setChargeCode(genericCodeObj);
                    session.setAttribute("ftfStandardCharges", standardCharges);
                }
            }

            forwardName = "ftfagssAdd";
        }

        if (buttonValue != null && buttonValue.equals("addItem")) {

            session.setAttribute("listAgsCoitem", "listLclCoitem");
            forwardName = "ftfagssAdd";
        }

        request.setAttribute("buttonValue", buttonValue);

        return mapping.findForward(forwardName);

    }
}
