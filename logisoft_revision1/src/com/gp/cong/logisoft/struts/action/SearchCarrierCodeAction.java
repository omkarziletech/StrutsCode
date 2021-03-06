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

import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.struts.form.SearchCarrierCodeForm;

/** 
 * MyEclipse Struts
 * Creation date: 07-29-2008
 * 
 * XDoclet definition:
 * @struts.action path="/searchCarrierCode" name="searchCarrierCodeForm" input="/jsps/datareference/searchCarrierCode.jsp" scope="request" validate="true"
 */
public class SearchCarrierCodeAction extends Action {
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
        SearchCarrierCodeForm searchCarrierCodeForm = (SearchCarrierCodeForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String carrierCode = searchCarrierCodeForm.getCarrierCode();
        String carrierName = searchCarrierCodeForm.getCarrierName();
        String buttonValue = searchCarrierCodeForm.getButtonValue();
        CarriersOrLineTemp carrierobj = new CarriersOrLineTemp();
        String index = searchCarrierCodeForm.getIndex();
        CarriersOrLineDAO carriersOrLineDAO = new CarriersOrLineDAO();

        if ((request.getParameter("index") != null && !request.getParameter("index").equals("")) || (index != null && !index.equals(""))) {

            int ind = Integer.parseInt(request.getParameter("index"));
            if (index != null) {
                ind = Integer.parseInt(index);
            }
            List carrierlist = (List) session.getAttribute("carrierinfo");
            carrierobj = (CarriersOrLineTemp) carrierlist.get(ind);
            session.setAttribute("carrierobj", carrierobj);
            request.setAttribute("checked", "checked");

        } else {
            List carrierinfo = carriersOrLineDAO.findCarrierCode1(carrierCode, carrierName);
            session.setAttribute("carrierinfo", carrierinfo);
            if (((List) session.getAttribute("carrierinfo")).size() == 0) {
                session.setAttribute("Notfound", carrierCode);

            }
        }


        return mapping.findForward("searchCarrierCode");
    }
}