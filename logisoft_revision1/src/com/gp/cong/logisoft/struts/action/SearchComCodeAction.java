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

import com.gp.cong.logisoft.beans.CodeBean;
import com.gp.cong.logisoft.domain.AirRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchComCodeForm;

/**
 * MyEclipse Struts Creation date: 03-04-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/searchComCode" name="searchComCodeForm"
 *                input="/jsps/ratemanagement/searchComCode.jsp" scope="request"
 *                validate="true"
 */
public class SearchComCodeAction extends Action {
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
        SearchComCodeForm searchComCodeForm = (SearchComCodeForm) form;// TODO
        // Auto-generated
        // method
        // stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = searchComCodeForm.getButtonValue();
        String code = searchComCodeForm.getCode();
        String codeDesc = searchComCodeForm.getCodeDescription();
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        GenericCode genericCodeObj = new GenericCode();
        String search = "";
        String path1 = "";
        AirRates airrates = null;
        if (request.getParameter("index") != null) {
            int ind = Integer.parseInt(request.getParameter("index"));
            List codeList1 = (List) session.getAttribute("codeList");
            if (session.getAttribute("searchCode") != null) {
                search = (String) session.getAttribute("searchCode");
            }
            if (search.equals("searchpcomcode") || search.equals("addcomcode")) {
                if (search.equals("searchpcomcode")) {
                    if (session.getAttribute("searchComCode") == null) {
                        airrates = new AirRates();
                    } else {
                        airrates = (AirRates) session.getAttribute("searchComCode");
                    }
                }
                if (search.equals("addcomcode")) {
                    if (session.getAttribute("searchComCode") == null) {
                        airrates = new AirRates();
                    } else {
                        airrates = (AirRates) session.getAttribute("searchComCode");
                    }
                }

                genericCodeObj = (GenericCode) codeList1.get(ind);
                airrates.setCommodityCode(genericCodeObj);
                if (search.equals("searchpcomcode")) {
                    session.setAttribute("searchComCode", airrates);
                    path1 = "jsps/ratemanagement/manageAirRates.jsp";
                } else if (search.equals("addcomcode")) {
                    session.setAttribute("addComCode", airrates);
                    path1 = "jsps/ratemanagement/addAirRatesPopup.jsp";
                }
                if (session.getAttribute("codeList") != null) {
                    session.removeAttribute("codeList");
                }
            } else if (search.equals("genericode")) {
                CodeBean codeBean = new CodeBean();
                genericCodeObj = (GenericCode) codeList1.get(ind);
                codeBean.setCodeDesc(genericCodeObj.getCode());
                codeBean.setCodeValue(genericCodeObj.getCodedesc());
                session.setAttribute("codeBean", codeBean);
                path1 = "jsps/datareference/GenericCodeMaintenance.jsp";
            }
            request.setAttribute("path1", path1);
            request.setAttribute("buttonValue", "completed");
        } else {
            if (buttonValue.equals("search")) {
                List codeList = genericDAO.findForAirRates(code, codeDesc);
                session.setAttribute("codeList", codeList);
            }
        }
        return mapping.findForward("searchcomcode");
    }
}