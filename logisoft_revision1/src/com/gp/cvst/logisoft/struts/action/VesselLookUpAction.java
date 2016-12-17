/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.struts.form.VesselLookUpForm;

/** 
 * MyEclipse Struts
 * Creation date: 05-18-2009
 * 
 * XDoclet definition:
 * @struts.action path="/vesselLookUp" name="vesselLookUpForm" input="/jsps/fclQuotes/VesselLookUp.jsp" scope="request" validate="true"
 */
public class VesselLookUpAction extends Action {
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
        VesselLookUpForm vesselLookUpForm = (VesselLookUpForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String button = vesselLookUpForm.getButton();
        String vesselName = vesselLookUpForm.getVesselName();
        String vessel = "";
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List vesselList = null;

        if (request.getParameter("buttonValue") != null && (request.getParameter("buttonValue").equals("bookingVessel"))) {

            vessel = request.getParameter("clientName");
            if (vessel != null && vessel.equals("percent")) {
                vessel = "%";
            }
            vesselList = genericCodeDAO.findForGenericAction(14, null, vessel);
            session.setAttribute("buttonValue", request.getParameter("buttonValue"));
            session.setAttribute("vesselList", vesselList);

        } else if (request.getParameter("paramId") != null) {
            if (session.getAttribute("vesselList") != null) {
                vesselList = (List) session.getAttribute("vesselList");
                GenericCode genericCode = (GenericCode) vesselList.get(Integer.parseInt(request.getParameter("paramId")));
                List newList = new ArrayList();
                newList.add(genericCode.getCodedesc());
                request.setAttribute("NewVesselList", newList);

                if (request.getParameter("button") != null &&
                        request.getParameter("button").equals("bookingVessel")) {
                    request.setAttribute("buttonValue", "VesselInBooking");
                }

                if (session.getAttribute("buttonValue") != null) {
                    session.removeAttribute("buttonValue");
                }
                if (session.getAttribute("vesselList") != null) {
                    session.removeAttribute("vesselList");
                }
            }
        }
        if (button != null && button.equals("Go")) {
            if (request.getParameter("action") != null && request.getParameter("action").equals("bookingVessel")) {
                vesselList = genericCodeDAO.findForGenericAction(14, null, vesselName);
            }
            session.setAttribute("vesselList", vesselList);
        }
        return mapping.findForward("vesselLookUp");
    }
}