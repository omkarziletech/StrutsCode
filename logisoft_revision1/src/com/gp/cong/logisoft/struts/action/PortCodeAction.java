/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.struts.form.PortCodeForm;

/** 
 * MyEclipse Struts
 * Creation date: 02-02-2008
 * 
 * XDoclet definition:
 * @struts.action path="/portCode" name="portCodeForm" input="/jsps/datareference/PortCode.jsp" scope="request" validate="true"
 */
public class PortCodeAction extends Action {
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
        PortCodeForm portCodeForm = (PortCodeForm) form;// TODO Auto-generated method stub
        String buttonValue = portCodeForm.getButtonValue();
        HttpSession session = ((HttpServletRequest) request).getSession();
        String scheduleCode = portCodeForm.getScheduleCode();
        String controlNo = portCodeForm.getControlNo();
        PortsDAO portsDAO = new PortsDAO();
        if (buttonValue.equals("search")) {
            List portcode = portsDAO.findPortCode(scheduleCode, controlNo);
            if (portcode != null) {
                if (portcode.size() > 0) {
                    String msg = "Please enter different Schedule Code and Control Number, This Code already exists";
                    request.setAttribute("message", msg);
                } else {
                    if (session.getAttribute("lclPortConfigurationObj") != null) {
                        session.removeAttribute("lclPortConfigurationObj");
                    }
                    if (session.getAttribute("fclPortObjConfiguration") != null) {
                        session.removeAttribute("fclPortObjConfiguration");
                    }
                    if (session.getAttribute("fclPortobj") != null) {
                        session.removeAttribute("fclPortobj");
                    }
                    if (session.getAttribute("lclPortobj") != null) {
                        session.removeAttribute("lclPortobj");
                    }
                    if (session.getAttribute("airPortObjConfigConfiguration") != null) {
                        session.removeAttribute("airPortObjConfigConfiguration");
                    }
                    if (session.getAttribute("impPortObjConfiguration") != null) {
                        session.removeAttribute("impPortObjConfiguration");
                    }
                    if (session.getAttribute("importPortObj") != null) {
                        session.removeAttribute("importPortObj");
                    }
                    if (session.getAttribute("agencyInfoListForLCL") != null) {
                        session.removeAttribute("agencyInfoListForLCL");
                    }
                    if (session.getAttribute("agencyInfoListForFCL") != null) {
                        session.removeAttribute("agencyInfoListForFCL");
                    }
                    if (session.getAttribute("agencyInfoListForAir") != null) {
                        session.removeAttribute("agencyInfoListForAir");
                    }
                    if (session.getAttribute("agencyInfoListForImp") != null) {
                        session.removeAttribute("agencyInfoListForImp");
                    }
                    if (session.getAttribute("portobj") != null) {
                        session.removeAttribute("portobj");
                    }
                    if (session.getAttribute("portList1") != null) {
                        session.removeAttribute("portList1");
                    }
                    if (session.getAttribute("agencyInfoList") != null) {
                        session.removeAttribute("agencyInfoList");
                    }
                    if (session.getAttribute("agencyObj") != null) {
                        session.removeAttribute("agencyObj");
                    }
                    if (session.getAttribute("agencyInfoListForAirAdd") != null) {
                        session.removeAttribute("agencyInfoListForAirAdd");
                    }
                    if (session.getAttribute("agencyInfoListForFCLAdd") != null) {
                        session.removeAttribute("agencyInfoListForFCLAdd");
                    }
                    if (session.getAttribute("agencyInfoListForImpAdd") != null) {
                        session.removeAttribute("agencyInfoListForImpAdd");
                    }
                    if (session.getAttribute("terminal") != null) {
                        session.removeAttribute("terminal");
                    }
                    if (session.getAttribute("portobject") != null) {
                        session.removeAttribute("portobject");
                    }
                    if (session.getAttribute("pierList") != null) {
                        session.removeAttribute("pierList");
                    }
                    if (session.getAttribute("cityDetails") != null) {
                        session.removeAttribute("cityDetails");
                    }
                    Ports ports = new Ports();
                    ports.setShedulenumber(scheduleCode);
                    ports.setControlNo(controlNo);
                    session.setAttribute("portobj", ports);
                    request.setAttribute("portcode", "portcode");

                }
            }
        }
        return mapping.findForward("portcode");
    }
}