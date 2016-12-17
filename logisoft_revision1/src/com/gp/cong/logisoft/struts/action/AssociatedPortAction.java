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
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.struts.form.AssociatedPortForm;

/** 
 * MyEclipse Struts
 * Creation date: 02-04-2008
 * 
 * XDoclet definition:
 * @struts.action path="/associatedPort" name="associatedPortForm" input="/jsps/datareference/AssociatedPort.jsp" scope="request" validate="true"
 */
public class AssociatedPortAction extends Action {
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
        AssociatedPortForm associatedPortForm = (AssociatedPortForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();

        String buttonValue = associatedPortForm.getButtonvalue();
        String portcode = associatedPortForm.getShedulecode();
        PortsTemp portsobj = new PortsTemp();
        ;
        List portsList = null;


        if (buttonValue.equals("add")) {
            if (session.getAttribute("porttempobj") != null) {
                portsobj = (PortsTemp) session.getAttribute("porttempobj");
            }
            if (session.getAttribute("portList1") != null) {
                portsList = (List) session.getAttribute("portList1");
            } else {
                portsList = new ArrayList();
            }
            boolean flag = false;
            for (int i = 0; i < portsList.size(); i++) {
                PortsTemp port1 = (PortsTemp) portsList.get(i);
                if (port1.getShedulenumber().equals(portsobj.getShedulenumber())) {
                    if (port1.getControlNo().equals(portsobj.getControlNo())) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                String msg = "This port is already added";
                request.setAttribute("msg", msg);
            } else {
                portsList.add(portsobj);
                session.setAttribute("portList1", portsList);
            }

            request.setAttribute("buttonValue", buttonValue);
        }
        if (buttonValue.equals("delete")) {
            if (session.getAttribute("portList1") == null) {
                portsList = new ArrayList();
            } else {
                portsList = (List) session.getAttribute("portList1");
            }
            int index = associatedPortForm.getIndex();
            PortsTemp portstoRemove = (PortsTemp) portsList.get(index);
            portsList.remove(index);
            request.setAttribute("buttonValue", buttonValue);
        }

        return mapping.findForward("associatedException");
    }
}