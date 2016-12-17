/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.text.DecimalFormat;
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
import com.gp.cong.logisoft.domain.CarrierOceanEqptRates;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.form.OceanExceptionForm;

/** 
 * MyEclipse Struts
 * Creation date: 12-11-2007
 * 
 * XDoclet definition:
 * @struts.action path="/oceanException" name="oceanExceptionForm" input="/jsps/datareference/oceanException.jsp" scope="request" validate="true"
 */
public class OceanExceptionAction extends Action {
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
        OceanExceptionForm oceanExceptionForm = (OceanExceptionForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = oceanExceptionForm.getButtonvalue();
        String eqttype = oceanExceptionForm.getEqpttype();
        Double specialrate = oceanExceptionForm.getSpecialrate();
        DecimalFormat myFormatter = new DecimalFormat("0.00");
        Double specialrate1 = new Double(myFormatter.format(specialrate));
        SearchCarriersBean scBean = new SearchCarriersBean();
        scBean.setEqptType("select Equipment Type");
        scBean.setSpclRate("");
        List eqptList = null;
        CarrierOceanEqptRates oceanEqptobj = new CarrierOceanEqptRates();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        if (!buttonValue.equals("delete")) {

            oceanEqptobj.setEqpttype(genericCodeDAO.findById(new Integer(eqttype)));
            oceanEqptobj.setSpecialrate(specialrate1);
        }


        if (buttonValue.equals("add")) {
            if (session.getAttribute("eqptList") != null) {
                eqptList = (List) session.getAttribute("eqptList");
            } else {
                eqptList = new ArrayList();
            }
            eqptList.add(oceanEqptobj);
            request.setAttribute("scBean", scBean);
            session.setAttribute("eqptList", eqptList);
            oceanEqptobj.setSpecialrate(specialrate1);
        }

        if (buttonValue.equals("delete")) {
            if (session.getAttribute("eqptList") == null) {
                eqptList = new ArrayList();
            } else {
                eqptList = (List) session.getAttribute("eqptList");
            }
            int index = oceanExceptionForm.getIndex();
            eqptList.remove(index);
            request.setAttribute("buttonValue", buttonValue);
        }

        return mapping.findForward("oceanEqpt");
    }
}