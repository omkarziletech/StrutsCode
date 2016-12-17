/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.SearchCarriersBean;
import com.gp.cong.logisoft.domain.ClaimDetails;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.struts.form.ClaimDetailsForm;

/** 
 * MyEclipse Struts
 * Creation date: 03-13-2008
 * 
 * XDoclet definition:
 * @struts.action path="/claimDetails" name="claimDetailsForm" input="/jsps/datareference/ClaimDetails.jsp" scope="request" validate="true"
 */
public class ClaimDetailsAction extends Action {
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
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ClaimDetailsForm claimDetailsForm = (ClaimDetailsForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = claimDetailsForm.getButtonValue();
        String masterawb1 = claimDetailsForm.getMasterAwb2();
        String masterawb2 = claimDetailsForm.getMasterAwb3();
        String masterawb3 = claimDetailsForm.getMasterAwb1();
        String portNumber = claimDetailsForm.getPortNo();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String flightDate = claimDetailsForm.getTxtcal();
        String hazardous = claimDetailsForm.getHazardous();
        String masterawb = "";
        masterawb = masterawb3 + "-" + masterawb1 + "-" + masterawb2;
        ClaimDetails claimDetails = new ClaimDetails();
        SearchCarriersBean searchCarriers = new SearchCarriersBean();
        searchCarriers.setMasterAwb2(masterawb1);
        searchCarriers.setMasterAwb3(masterawb2);
        searchCarriers.setHazardous(hazardous);
        searchCarriers.setTxtcal(flightDate);
        session.setAttribute("searchCarriers", searchCarriers);
        List claimdetailslist = null;
        int index = 0;

        if (request.getParameter("ind") != null) {
            List claimdetailslist1 = null;
            ClaimDetails cld = null;
            int ind = Integer.parseInt(request.getParameter("ind"));
            if (session.getAttribute("claimdetailslist") != null) {
                claimdetailslist1 = (List) session.getAttribute("claimdetailslist");
                cld = (ClaimDetails) claimdetailslist1.get(ind);
            }
            session.setAttribute("cld", cld);
            request.setAttribute("edit", "edit");
        } else {
            if (buttonValue.equals("save")) {
                if (session.getAttribute("claimdetailslist") != null) {
                    claimdetailslist = (List) session.getAttribute("claimdetailslist");
                    for (int i = 0; i < claimdetailslist.size(); i++) {
                        ClaimDetails claim1 = (ClaimDetails) claimdetailslist.get(i);
                        if (claim1.getIndex() != null) {
                            if (claim1.getIndex() > index) {
                                index = claim1.getIndex();
                            }
                        }
                    }
                    index++;
                } else {
                    claimdetailslist = new ArrayList();
                    index++;
                }
                claimDetails.setIndex(index);
                claimDetails.setMasterAwbNo(masterawb);
                claimDetails.setPortNumber(portNumber);
                if (flightDate != null && flightDate != "") {
                    java.util.Date javaDate = null;
                    javaDate = sdf.parse(flightDate);
                    claimDetails.setFlightDate(javaDate);
                }
                if (hazardous != null && hazardous.equalsIgnoreCase("on")) {
                    claimDetails.setHazardous("Y");
                } else if (hazardous == null || hazardous.equalsIgnoreCase("off")) {
                    claimDetails.setHazardous("N");
                }
                claimDetails.setClaim("unclaimed");
                session.setAttribute("claimDetails", claimDetails);
                claimdetailslist.add(claimDetails);
                session.setAttribute("claimdetailslist", claimdetailslist);
            }
        }
        return mapping.findForward("claimdetail");
    }
}