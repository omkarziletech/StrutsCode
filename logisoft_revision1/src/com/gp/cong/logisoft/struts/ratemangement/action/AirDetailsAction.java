/*
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

import com.gp.cong.logisoft.domain.AirWeightRangesRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.AirDetailsForm;

/** 
 * MyEclipse Struts
 * Creation date: 03-05-2008
 * 
 * XDoclet definition:
 * @struts.action path="/airDetails" name="airDetailsForm" input="/jsps/ratemanagement/airDetails.jsp" scope="request" validate="true"
 */
public class AirDetailsAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        AirDetailsForm airDetailsForm = (AirDetailsForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String forwardName = "";
        AirWeightRangesRates airWeightRangeRatesObj = new AirWeightRangesRates();
        List airDetailsList = new ArrayList();
        String buttonValue = airDetailsForm.getButtonValue();
        int index = 0;
        String weightRange = "";

        Date dateStr = null;
        GenericCode genobj = null;
        GenericCodeDAO gencodeDAO = new GenericCodeDAO();
        forwardName = "airDetailsAdd";


        if (request.getParameter("indno") != null) {
            airWeightRangeRatesObj = new AirWeightRangesRates();
            int i = Integer.parseInt(request.getParameter("indno"));
            if (session.getAttribute("airdetailsAdd") != null) {
                airDetailsList = (List) session.getAttribute("airdetailsAdd");
            }
            airWeightRangeRatesObj = (AirWeightRangesRates) airDetailsList.get(i);
            session.setAttribute("airrangedetails", airWeightRangeRatesObj);
            forwardName = "editAriDetails";

        } else {
            if (session.getAttribute("airrangedetails") != null) {
                airWeightRangeRatesObj = (AirWeightRangesRates) session.getAttribute("airrangedetails");

            }
            if (!buttonValue.equals("delete") && !buttonValue.equals("cancel")) {

                if (airDetailsForm.getWeightRange() != null && !airDetailsForm.getWeightRange().equals("0")) {
                    genobj = gencodeDAO.findById(airDetailsForm.getWeightRange());
                }
                airWeightRangeRatesObj.setWeightRange(genobj);
                airWeightRangeRatesObj.setGeneralRate(airDetailsForm.getGeneralRate());
                airWeightRangeRatesObj.setGeneralMinAmt(airDetailsForm.getGeneralAmt());
                airWeightRangeRatesObj.setExpressRate(airDetailsForm.getExpressRate());
                airWeightRangeRatesObj.setExpressMinAmt(airDetailsForm.getExpressAmt());
                airWeightRangeRatesObj.setDeferredRate(airDetailsForm.getDeferredRate());
                airWeightRangeRatesObj.setDeferredMinAmt(airDetailsForm.getDeferredAmt());
                airWeightRangeRatesObj.setChangedDate(changeToDate(airDetailsForm.getChangedDate()));
                airWeightRangeRatesObj.setWhoChanged(airDetailsForm.getWhoChanged());
            }

            if (buttonValue.equals("delete")) {

                List arrList = new ArrayList();
                List applyGeneralStandardList = new ArrayList();
                if (session.getAttribute("airdetailsAdd") != null) {
                    arrList = (List) session.getAttribute("airdetailsAdd");
                }
                for (int i = 0; i < arrList.size(); i++) {
                    AirWeightRangesRates airWeight = (AirWeightRangesRates) arrList.get(i);
                    if (airWeight.getWeightRange().getId() == airWeightRangeRatesObj.getWeightRange().getId()) {
                        arrList.remove(airWeight);
                        break;
                    }

                }
                session.setAttribute("airdetailsAdd", arrList);

                if (session.getAttribute("airrangedetails") != null) {
                    session.removeAttribute("airrangedetails");
                }
                /*if(session.getAttribute("airdetailsAdd")!=null)
                {
                session.removeAttribute("airdetailsAdd");
                }*/
                forwardName = "airDetailsAdd";

            } else if (buttonValue.equals("cancel")) {
                if (session.getAttribute("airrangedetails") != null) {
                    session.removeAttribute("airrangedetails");
                }
                forwardName = "airDetailsAdd";
            } else if (buttonValue.equals("update")) {

                if (session.getAttribute("airdetailsAdd") != null) {
                    airDetailsList = (List) session.getAttribute("airdetailsAdd");
                    for (int i = 0; i < airDetailsList.size(); i++) {
                        AirWeightRangesRates airWeight = (AirWeightRangesRates) airDetailsList.get(i);
                        if (airWeight != null && airWeightRangeRatesObj != null && airWeightRangeRatesObj.getWeightRange() != null && airWeight.getWeightRange().getId() == airWeightRangeRatesObj.getWeightRange().getId()) {
                            airWeightRangeRatesObj.setIndex(airWeight.getIndex());
                            airDetailsList.remove(airWeight);
                        }
                    }
                }

                airDetailsList.add(airWeightRangeRatesObj);
                if (session.getAttribute("airrangedetails") != null) {
                    session.removeAttribute("airrangedetails");

                }
                session.setAttribute("airdetailsAdd", airDetailsList);
                forwardName = "airDetailsAdd";

            }

            if (buttonValue.equals("add")) {
                if (session.getAttribute("airdetailsAdd") != null) {
                    airDetailsList = (List) session.getAttribute("airdetailsAdd");
                    for (int i = 0; i < airDetailsList.size(); i++) {
                        AirWeightRangesRates airWeight = (AirWeightRangesRates) airDetailsList.get(i);
                        if (airWeight.getIndex() != null) {
                            if (airWeight.getIndex() > index) {
                                index = airWeight.getIndex();
                            }
                        } else {
                            index = airDetailsList.size() - 1;
                        }
                    }
                    index++;
                } else {
                    airDetailsList = new ArrayList();
                    index++;
                }
                airWeightRangeRatesObj.setIndex(index);
                airDetailsList.add(airWeightRangeRatesObj);
                if (session.getAttribute("airrangedetails") != null) {
                    session.removeAttribute("airrangedetails");

                }
                if (session.getAttribute("addDetailItem") != null) {
                    session.removeAttribute("addDetailItem");

                }
                session.setAttribute("airdetailsAdd", airDetailsList);
                forwardName = "airDetailsAdd";

            }
            if (buttonValue.equals("addItem")) {
                session.setAttribute("addDetailItem", "addDetailItem");

            }

            request.setAttribute("buttonValue", buttonValue);
            session.setAttribute("airWeightRangeRatesObj", airWeightRangeRatesObj);

        }
        return mapping.findForward(forwardName);
    }

    public Date changeToDate(String dateFromForm)throws Exception {

        if (dateFromForm != null && dateFromForm != "") {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            javaDate = sdf.parse(dateFromForm);
        }
        return javaDate;

    }
}
