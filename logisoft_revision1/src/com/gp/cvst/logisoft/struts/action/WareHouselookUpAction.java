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

import com.gp.cong.logisoft.domain.Warehouse;
import com.gp.cong.logisoft.hibernate.dao.WarehouseDAO;
import com.gp.cvst.logisoft.struts.form.WareHouselookUpForm;

/**
 * MyEclipse Struts Creation date: 04-13-2009
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/wareHouselookUp" name="wareHouselookUpForm"
 *                input="/jsps/fclQuotes/wareHouselookUp.jsp" scope="request"
 *                validate="true"
 */
public class WareHouselookUpAction extends Action {
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

        WareHouselookUpForm wareHouselookUpForm = (WareHouselookUpForm) form;// TODO
        // Auto-generated
        // method
        // stub

        String button = wareHouselookUpForm.getButton();
        String wareHouse = wareHouselookUpForm.getWareHouseName();
        HttpSession session = ((HttpServletRequest) request).getSession();
        String warehouseName = "";
        String address = "";
        List wareHouseList = null;
        WarehouseDAO warehouseDAO = new WarehouseDAO();
        if (request.getParameter("buttonValue") != null && (request.getParameter("buttonValue").equals("Quotation") || request.getParameter("buttonValue").equalsIgnoreCase("BookingPositionLoc") || request.getParameter("buttonValue").equalsIgnoreCase("BookingLoadLoc") || request.getParameter("buttonValue").equalsIgnoreCase(
                "BookingEquipReturn"))) {

            warehouseName = request.getParameter("wareHouse");
            if (warehouseName != null && warehouseName.equals("percent")) {
                warehouseName = "%";
            }
            if (warehouseName != null && !warehouseName.equals("")) {
                warehouseName = warehouseName.replace("amp;", "&");

                wareHouseList = warehouseDAO.findForWarehousenameAndAddress(
                        warehouseName, address);

            }
            session.setAttribute("buttonValue", request.getParameter("buttonValue"));
            session.setAttribute("WareHouseList", wareHouseList);

        } else if (request.getParameter("paramId") != null) {
            if (session.getAttribute("WareHouseList") != null) {
                wareHouseList = (List) session.getAttribute("WareHouseList");
                Warehouse warehouse = (Warehouse) wareHouseList.get(Integer.parseInt(request.getParameter("paramId")));
                List wareHouseNewList = new ArrayList();
                wareHouseNewList.add(warehouse.getWarehouseName());
                wareHouseNewList.add(warehouse.getAddress());
                wareHouseNewList.add(warehouse.getCity());
                wareHouseNewList.add(warehouse.getZipCode());
                wareHouseNewList.add(warehouse.getState());
                request.setAttribute("wareHouseNewList", wareHouseNewList);
                if (request.getParameter("button").equals("Quotation")) {
                    request.setAttribute("buttonValue", "QuotationWarehouse");
                } else if (request.getParameter("button").equals(
                        "BookingPositionLoc")) {
                    request.setAttribute("buttonValue", "BookingPos");
                } else if (request.getParameter("button").equals(
                        "BookingLoadLoc")) {
                    request.setAttribute("buttonValue", "BookingLoad");
                } else if (request.getParameter("button").equals(
                        "BookingEquipReturn")) {
                    request.setAttribute("buttonValue", "BookingEquipReturn");
                }
                if (session.getAttribute("WareHouseList") != null) {
                    session.removeAttribute("WareHouseList");
                }
                if (session.getAttribute("buttonValue") != null) {
                    session.removeAttribute("buttonValue");
                }
            }
        }
        if (button != null && button.equals("Go")) {
            if (session.getAttribute("buttonValue").equals("Quotation") || session.getAttribute("buttonValue").equals(
                    "BookingPositionLoc") || session.getAttribute("buttonValue").equals(
                    "BookingLoadLoc") || session.getAttribute("buttonValue").equals(
                    "BookingEquipReturn")) {
                wareHouseList = warehouseDAO.findForWarehousenameByType(
                        wareHouse, "FCL");
            }
            session.setAttribute("WareHouseList", wareHouseList);
        }

        return mapping.findForward("wareHouseName");
    }
}