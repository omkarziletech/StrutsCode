/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.UniverseFlatRate;
import com.gp.cong.logisoft.struts.ratemangement.form.EditUniverFlateRateForm;

/** 
 * MyEclipse Struts
 * Creation date: 07-31-2008
 * 
 * XDoclet definition:
 * @struts.action path="/editUniverFlateRate" name="editUniverFlateRateForm" input="jsps/ratemanagement/editUniverFlateRate.jsp" scope="request"
 */
public class EditUniverFlateRateAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        EditUniverFlateRateForm editUniverFlateRateForm = (EditUniverFlateRateForm) form;// TODO Auto-generated method stub
        String amount = editUniverFlateRateForm.getAmount();
        String button = editUniverFlateRateForm.getButtonValue();
        HttpSession session = ((HttpServletRequest) request).getSession();
        //GenericCode genObj=new GenericCode();
        //GenericCodeDAO genericCodeDAO=new GenericCodeDAO();
        List airList = new ArrayList();
        String FORWORD = "";
        List rangeList = new ArrayList();
        UniverseFlatRate universeAirFreightobject = new UniverseFlatRate();


        if (button != null && button.equals("add")) {
            if (session.getAttribute("edituniflatrate") != null) {
                universeAirFreightobject = (UniverseFlatRate) session.getAttribute("edituniflatrate");
            }
            if (amount != null && !amount.equals("")) {
                universeAirFreightobject.setAmount(Double.parseDouble(amount));
            }
            if (session.getAttribute("unifaltratelist") != null) {
                airList = (List) session.getAttribute("unifaltratelist");
                for (int i = 0; i < airList.size(); i++) {
                    UniverseFlatRate uniairList = (UniverseFlatRate) airList.get(i);
                    if (uniairList != null && universeAirFreightobject != null) {
                        if (uniairList.getUnitType().getId() == universeAirFreightobject.getUnitType().getId()) {
                            airList.set(i, universeAirFreightobject);
                            break;
                        }
                    }
                }

                session.setAttribute("unifaltratelist", airList);
            }

        }
        if (button != null && button.equals("delete")) {
            if (session.getAttribute("edituniflatrate") != null) {
                universeAirFreightobject = (UniverseFlatRate) session.getAttribute("edituniflatrate");
            }

            if (session.getAttribute("unifaltratelist") != null) {
                airList = (List) session.getAttribute("unifaltratelist");
                for (int i = 0; i < airList.size(); i++) {
                    UniverseFlatRate uniairList = (UniverseFlatRate) airList.get(i);
                    if (uniairList != null && universeAirFreightobject != null) {
                        if (uniairList.getUnitType().getId() == universeAirFreightobject.getUnitType().getId()) {

                            airList.remove(universeAirFreightobject);
                            break;
                        }
                    }
                }

                session.setAttribute("unifaltratelist", airList);
            }

        }
        if (button != null && button.equals("cancel")) {
            if (session.getAttribute("edituniflatrate") != null) {
                session.removeAttribute("edituniflatrate");
            }
        }

        FORWORD = "unifaltAdd";
        return mapping.findForward(FORWORD);
    }
}