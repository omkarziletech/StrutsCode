/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.WarehouseTemp;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cong.logisoft.struts.form.SearchWareHouseCodeForm;

/** 
 * MyEclipse Struts
 * Creation date: 07-31-2008
 * 
 * XDoclet definition:
 * @struts.action path="/searchWareHouseCode" name="searchWareHouseCodeForm" input="/jsps/datareference/searchWareHouseCode.jsp" scope="request" validate="true"
 */
public class SearchWareHouseCodeAction extends Action {
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
        SearchWareHouseCodeForm searchWareHouseCodeForm = (SearchWareHouseCodeForm) form;// TODO Auto-generated method stub
        HttpSession session = request.getSession();
        String wareHouseCode = searchWareHouseCodeForm.getWareHouseCode();
        String wareHouseName = searchWareHouseCodeForm.getWareHouseName();
        String city = searchWareHouseCodeForm.getCity();
        String index = searchWareHouseCodeForm.getIndex();
        String search1 = "";

        String n = "";
        if (session.getAttribute("search") != null) {
            search1 = (String) session.getAttribute("search");

        }

        WarehouseDAO wareHouse = new WarehouseDAO();
        if (session.getAttribute("search") != null) {
            search1 = (String) session.getAttribute("search");
        }

        if ((request.getParameter("index") != null && !request.getParameter("index").equals("")) || (index != null && !index.equals(""))) {
            int ind = Integer.parseInt(request.getParameter("index"));
            if (index != null) {
                ind = Integer.parseInt(index);
            }
            List wareHouseList = (List) session.getAttribute("wareHouse");
            WarehouseTemp warehouseTemp = (WarehouseTemp) wareHouseList.get(ind);
            session.setAttribute("wareHouseObj", warehouseTemp);
            request.setAttribute("checked", "checked");
            if (search1.equals("seachUnit")) {
                request.setAttribute("checked1", "checked1");
            } else {
                request.setAttribute("checked", "checked");
            }
            if (session.getAttribute("wareHouse") != null) {
                session.removeAttribute("wareHouse");
            }

            return mapping.findForward("searchWareHouseCode");

        } else {

            List wareHouse1 = wareHouse.findForSearchWarehouse(wareHouseCode, wareHouseName, city, n);
            session.setAttribute("wareHouse", wareHouse1);
            if (((List) session.getAttribute("wareHouse")).size() == 0) {
                session.setAttribute("Notfound", wareHouseCode);

            }
        }


            response.sendRedirect(request.getContextPath() + "/jsps/datareference/searchWareHouseCode.jsp");
        return null;
    }
}