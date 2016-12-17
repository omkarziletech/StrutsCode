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
import com.gp.cong.logisoft.domain.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.AirRatesBean;
import com.gp.cong.logisoft.domain.AirCommodityCharges;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.CSSSForm;

/** 
 * MyEclipse Struts
 * Creation date: 03-05-2008
 * 
 * XDoclet definition:
 * @struts.action path="/cSSS" name="cSSSForm" input="/jsps/ratemanagment/cSSS.jsp" scope="request" validate="true"
 */
public class CSSSAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    java.util.Date javaDate = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)throws Exception {
        CSSSForm cSSSForm = (CSSSForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String forwardName = "";
        AirCommodityCharges airCommodityCharges = new AirCommodityCharges();

        String buttonValue = cSSSForm.getButtonValue();
        String code = cSSSForm.getCharge();
        String codeDesc = cSSSForm.getDesc();
        String standard = cSSSForm.getStandard();
        String asFreighted = cSSSForm.getAsFrfgted();
        String percentage = cSSSForm.getPercentage();
        String exclude = cSSSForm.getExclude();
        int index = 0;
        User user = null;
        if (request.getParameter("ind") != null) {
            AirCommodityCharges airCommodity = new AirCommodityCharges();
            int ind = Integer.parseInt(request.getParameter("ind"));
            List codeList1 = (List) session.getAttribute("cssList");
            airCommodity = (AirCommodityCharges) codeList1.get(ind);
            session.setAttribute("airCommodityCharges", airCommodity);
            forwardName = "csssEdit";
        } else {
            if (session.getAttribute("airCommodityCharges") != null) {
                airCommodityCharges = (AirCommodityCharges) session.getAttribute("airCommodityCharges");
            } else {
                airCommodityCharges = new AirCommodityCharges();
            }
            AirRatesBean airRatesBean = new AirRatesBean();
            airRatesBean.setStandard(cSSSForm.getStandard());
            airRatesBean.setAsFrfgted(cSSSForm.getAsFrfgted());
            request.setAttribute("airRatesBean", airRatesBean);
            if (asFreighted != null && asFreighted.equalsIgnoreCase("on")) {
                airCommodityCharges.setAsFrfgted("X");
            } else if (asFreighted == null) {
                airCommodityCharges.setAsFrfgted(null);
            }


            List csssList = new ArrayList();

            GenericCode genObj = null;
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            String type = cSSSForm.getChargeType();

            if (type != null && !type.equals("0")) {
                genObj = (GenericCode) genericCodeDAO.findById(Integer.parseInt(type));
                airCommodityCharges.setChargeType(genObj);
            }
            forwardName = "csssAdd";

            if (exclude != null) {
                if (exclude.equals("on")) {
                    exclude = "y";
                    GenericCode gen = null;
                    airCommodityCharges.setChargeType(gen);
                } else {
                    exclude = "N";
                }
                airCommodityCharges.setExclude(exclude);
            }
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            if (userId != null) {
                airCommodityCharges.setWhoChanged(userId.getLoginName());
            }
            if (standard != null && standard.equalsIgnoreCase("on")) {
                airCommodityCharges.setStandard("Y");
            } else if (standard == null) {
                airCommodityCharges.setStandard("N");
            }

            if (session.getAttribute("loginuser") != null) {
                user = (User) session.getAttribute("loginuser");
            }
            if (cSSSForm.getAmtPerCft() != null) {
                airCommodityCharges.setAmtPerCft(cSSSForm.getAmtPerCft());
            } else {
                airCommodityCharges.setAmtPerCft(0.0);
            }
            if (cSSSForm.getAmtPer100lbs() != null) {
                airCommodityCharges.setAmtPer100lbs(cSSSForm.getAmtPer100lbs());
            } else {
                airCommodityCharges.setAmtPer100lbs(0.0);
            }
            if (cSSSForm.getAmtPerCbm() != null) {
                airCommodityCharges.setAmtPerCbm(cSSSForm.getAmtPerCbm());
            } else {
                airCommodityCharges.setAmtPerCbm(0.0);
            }

            if (cSSSForm.getAmtPer1000kg() != null) {
                airCommodityCharges.setAmtPer1000kg(cSSSForm.getAmtPer1000kg());
            } else {
                airCommodityCharges.setAmtPer1000kg(0.0);
            }
            if (cSSSForm.getAmount() != null) {
                airCommodityCharges.setAmount(cSSSForm.getAmount());
            } else {
                airCommodityCharges.setAmount(0.0);
            }
            if (percentage != null && !percentage.equals("")) {
                double d = Double.parseDouble(percentage);
                airCommodityCharges.setPercentage(d / 1000);

            } else {
                airCommodityCharges.setPercentage(0.0);
            }
            if (cSSSForm.getMinAmt() != null) {
                airCommodityCharges.setMinAmt(cSSSForm.getMinAmt());
            } else {
                airCommodityCharges.setMinAmt(0.0);
            }
            if (cSSSForm.getTxtItemcreatedon() != null && cSSSForm.getTxtItemcreatedon() != "") {
                java.util.Date javaDate = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aaa");
                    javaDate = sdf.parse(cSSSForm.getTxtItemcreatedon() + " " + sdf1.format(new Date()));
                airCommodityCharges.setEffectiveDate(javaDate);
            }

            session.setAttribute("airCommodityCharges", airCommodityCharges);
            if (buttonValue != null && buttonValue.equals("add") && !buttonValue.equals("")) {
                if (session.getAttribute("cssList") != null) {
                    csssList = (List) session.getAttribute("cssList");
                    for (int i = 0; i < csssList.size(); i++) {
                        AirCommodityCharges airComm = (AirCommodityCharges) csssList.get(i);
                        if (airComm.getIndex() != null) {
                            if (airComm.getIndex() > index) {
                                index = airComm.getIndex();
                            }
                        } else {
                            index = csssList.size() - 1;
                        }
                    }
                    index++;
                } else {
                    csssList = new ArrayList();
                    index++;
                }
                airCommodityCharges.setIndex(index);
                if (airCommodityCharges.getChargeCode() != null) {
                    csssList.add(airCommodityCharges);
                } else {
                    String msg = "Please enter the proper data...";
                    request.setAttribute("exist", msg);
                }

                session.setAttribute("cssList", csssList);

                forwardName = "csssAdd";
                request.setAttribute("buttonValue", buttonValue);
                if (session.getAttribute("airCommodityCharges") != null) {
                    session.removeAttribute("airCommodityCharges");
                }
                if (session.getAttribute("airaddcsslist") != null) {
                    session.removeAttribute("airaddcsslist");
                }
            }
            if (buttonValue != null && buttonValue.equals("addItem") && !buttonValue.equals("")) {
                session.setAttribute("airaddcsslist", "airaddcsslist");

            }
            if (buttonValue != null && buttonValue.equals("chargeCode")) {
                List list = new ArrayList();
                String msg = "Charge Code already exists... ";

                if (session.getAttribute("cssList") != null) {
                    list = (List) session.getAttribute("cssList");

                    for (int i = 0; i < list.size(); i++) {
                        airCommodityCharges = (AirCommodityCharges) list.get(i);
                        if (airCommodityCharges.getChargeCode().getCode().equals(code)) {
                            request.setAttribute("exist", msg);
                        }

                        if (airCommodityCharges.getChargeCode().getCodedesc().equalsIgnoreCase(codeDesc)) {
                            request.setAttribute("exist", msg);
                        }
                    }
                }
                if (request.getAttribute("exist") == null && (code != null || !code.equals(""))) {
                    AirCommodityCharges airCommodityCharges2 = new AirCommodityCharges();
                    genObj = null;
                    GenericCodeDAO genericDAO = new GenericCodeDAO();
                    List codeList = genericDAO.findGenericCode("2", code);

                    if (codeList.size() > 0) {
                        genObj = (GenericCode) codeList.get(0);
                    }
                    if (genObj != null) {
                        airCommodityCharges2.setChargeCode(genObj);
                        session.setAttribute("airCommodityCharges", airCommodityCharges2);
                    }
                }

                if (request.getAttribute("exist") == null && (codeDesc != null || !codeDesc.equals(""))) {
                    AirCommodityCharges airCommodityCharges2 = new AirCommodityCharges();
                    genObj = null;
                    GenericCodeDAO genericDAO = new GenericCodeDAO();
                    List codeList = genericDAO.findByCodedesc(codeDesc);

                    if (codeList.size() > 0) {
                        genObj = (GenericCode) codeList.get(0);
                    }
                    if (genObj != null) {
                        airCommodityCharges2.setChargeCode(genObj);
                        session.setAttribute("airCommodityCharges", airCommodityCharges2);
                    }
                }

                forwardName = "csssAdd";
            }
        }
        return mapping.findForward(forwardName);
    }
}