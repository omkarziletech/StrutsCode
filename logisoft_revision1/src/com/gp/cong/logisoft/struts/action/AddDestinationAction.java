/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RelayDestination;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RelayDestinationDAO;
import com.gp.cong.logisoft.struts.form.AddDestinationForm;

/** 
 * MyEclipse Struts
 * Creation date: 12-15-2007
 * 
 * XDoclet definition:
 * @struts.action path="/addDestination" name="addDestinationForm" input="/jsps/datareference/addDestination.jsp" scope="request" validate="true"
 */
public class AddDestinationAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response) {
        AddDestinationForm addDestinationForm = (AddDestinationForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();

        String destinationCode = addDestinationForm.getDestinationCode();
        String destinationName = addDestinationForm.getDestinationName();
        String ttToPol = addDestinationForm.getTtToPol();
        String buttonValue = addDestinationForm.getButtonValue();
        String forwardName = "";

        RelayDestination relayDestination = null;
        if (!buttonValue.equals("cancel") && !buttonValue.equals("update") && !buttonValue.equals("delete")) {
            if (session.getAttribute("relayDestination") != null) {
                relayDestination = (RelayDestination) session.getAttribute("relayDestination");
            } else {
                relayDestination = new RelayDestination();
            }

            Ports destobj = null;
            RelayDestinationDAO relayDestinationDAO = new RelayDestinationDAO();
            PortsDAO portsDAO = new PortsDAO();


            if (ttToPol != null && !ttToPol.equals("")) {
                relayDestination.setTtFromPodToFd(Integer.parseInt(ttToPol));
            }

            session.setAttribute("relayDestination", relayDestination);
            forwardName = "adddestination";

        }
        if (buttonValue.equals("add")) {
            List destinationList = null;
            if (session.getAttribute("destinationList") != null) {
                destinationList = (List) session.getAttribute("destinationList");
            } else {
                destinationList = new ArrayList();
            }
            boolean flag = false;
            for (int i = 0; i < destinationList.size(); i++) {
                RelayDestination port1 = (RelayDestination) destinationList.get(i);
                if (port1.getDestinationId().getShedulenumber().equals(relayDestination.getDestinationId().getShedulenumber())) {
                    if (port1.getDestinationId().getControlNo().equals(relayDestination.getDestinationId().getControlNo())) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                String msg = "This port is already added";
                request.setAttribute("msg", msg);
                buttonValue = "";
            } else {
                destinationList.add(relayDestination);
                session.setAttribute("destinationList", destinationList);
                session.removeAttribute("relayDestination");
            }
        }
        if (buttonValue.equals("update")) {
            if (session.getAttribute("relayDestination") != null) {
                relayDestination = (RelayDestination) session.getAttribute("relayDestination");
                relayDestination.setDestinationId(relayDestination.getDestinationId());
                relayDestination.setTtFromPodToFd(Integer.parseInt(ttToPol));
            }
            forwardName = "adddestination";
            session.removeAttribute("relayDestination");
        }
        if (buttonValue.equals("cancel")) {
            forwardName = "cancel";
            session.removeAttribute("relayDestination");
        }
        if (buttonValue.equals("delete")) {
            if (session.getAttribute("relayDestination") != null) {
                relayDestination = (RelayDestination) session.getAttribute("relayDestination");
            }
            if (session.getAttribute("destinationList") != null) {
                List destinationList = (List) session.getAttribute("destinationList");
                destinationList.remove(relayDestination);
            }
            forwardName = "adddestination";
            session.removeAttribute("relayDestination");
        }


        request.setAttribute("buttonValue", buttonValue);

        return mapping.findForward(forwardName);
    }
}