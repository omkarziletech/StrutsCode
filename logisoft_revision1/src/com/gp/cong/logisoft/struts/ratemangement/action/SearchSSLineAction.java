 /*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.InlandSummary;
import com.gp.cong.logisoft.domain.Unit;
import com.gp.cong.logisoft.domain.VoyageExport;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchSSLineForm;
import com.gp.cvst.logisoft.domain.BookingFcl;

/** 
 * MyEclipse Struts
 * Creation date: 06-24-2008
 * 
 * XDoclet definition:
 * @struts.action path="/searchSSLine" name="searchSSLineForm" input="/form/searchSSLine.jsp" scope="request"
 */
public class SearchSSLineAction extends Action {
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
        SearchSSLineForm searchSSLineForm = (SearchSSLineForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = searchSSLineForm.getButtonValue();
        String sslinenumber = searchSSLineForm.getSsLineNumber();
        String sslinename = searchSSLineForm.getSsLineName();
        CarriersOrLineDAO carriersOrLineDAO = new CarriersOrLineDAO();
        CarriersOrLineTemp carriersOrLineTemp = new CarriersOrLineTemp();

        String searchssline = "";
        String path1 = "";
        int ind = -1;
        if (searchSSLineForm.getIndex() != null && !searchSSLineForm.getIndex().equals("")) {
            ind = Integer.parseInt(searchSSLineForm.getIndex());
        }
        if ((request.getParameter("index") != null && !request.getParameter("index").equals("")) || (searchSSLineForm.getIndex() != null && !searchSSLineForm.getIndex().equals(""))) {
            int index = Integer.parseInt(request.getParameter("index"));
            if (ind >= 0) {
                index = ind;
            }
            List sslineList1 = (List) session.getAttribute("fclpopList");
            if (session.getAttribute("searchssline") != null) {

                searchssline = (String) session.getAttribute("searchssline");
            }
            if (searchssline != null && searchssline.equals("searchvoyage")) {
                VoyageExport voyageExport = null;
                if (session.getAttribute("searchvoyagerecords") != null) {
                    voyageExport = (VoyageExport) session.getAttribute("searchvoyagerecords");
                } else {
                    voyageExport = new VoyageExport();
                }
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                voyageExport.setLineNo(carriersOrLineTemp);
                session.setAttribute("searchvoyagerecords", voyageExport);
                path1 = "jsps/voyagemanagement/searchVoyage.jsp";

            }
            if (searchssline != null && searchssline.equals("searchvoyage1")) {
                VoyageExport voyageExport = null;
                if (session.getAttribute("searchvoyagerecords1") != null) {
                    voyageExport = (VoyageExport) session.getAttribute("searchvoyagerecords1");
                } else {
                    voyageExport = new VoyageExport();
                }
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                voyageExport.setLineNo(carriersOrLineTemp);
                session.setAttribute("searchvoyagerecords1", voyageExport);
                path1 = "jsps/voyagemanagement/exportVoyages.jsp";

            }
            if (searchssline != null && searchssline.equals("searchQuotation")) {


                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                String carrierName = carriersOrLineTemp.getCarriername();

                session.setAttribute("carriersOrLineTemp", carrierName);
                path1 = "jsps/fclQuotes/SearchQuotation.jsp";

            }
            if (searchssline != null && searchssline.equals("searchContainer")) {
                InlandSummary inlandSummary = null;
                if (session.getAttribute("searchinlandsummary") != null) {
                    inlandSummary = (InlandSummary) session.getAttribute("searchinlandsummary");
                } else {
                    inlandSummary = new InlandSummary();
                }
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);

                //voyageExport.setLineNo(carriersOrLineTemp);
                inlandSummary.setCarrierCode(carriersOrLineTemp.getCarriercode());
                inlandSummary.setCarrierName(carriersOrLineTemp.getCarriername());
                session.setAttribute("searchinlandsummary", inlandSummary);
                path1 = "jsps/Containermanagement/ContainerSummary.jsp";

            }



            if (searchssline != null && searchssline.equals("addUnit")) {
                Unit unit = null;
                if (session.getAttribute("unitt") != null) {
                    unit = (Unit) session.getAttribute("unitt");
                } else {
                    unit = new Unit();
                }
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                unit.setCarrierCode(carriersOrLineTemp);
                session.setAttribute("unitt", unit);
                path1 = "jsps/Containermanagement/CreateUnit.jsp";

            }
            //
            if (searchssline != null && searchssline.equals("lineNo")) {
                VoyageExport voyageExport = null;
                if (session.getAttribute("voyagerecords") != null) {
                    voyageExport = (VoyageExport) session.getAttribute("voyagerecords");
                } else {
                    voyageExport = new VoyageExport();
                }
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                voyageExport.setLineNo(carriersOrLineTemp);
                session.setAttribute("voyagerecords", voyageExport);
                session.setAttribute("exist", "records");
                path1 = "jsps/voyagemanagement/exportVoyage.jsp";

            }
            if (searchssline != null && searchssline.equals("addvoyage")) {
                VoyageExport voyageExport = null;
                if (session.getAttribute("addvoyagerecords") != null) {
                    voyageExport = (VoyageExport) session.getAttribute("addvoyagerecords");
                } else {
                    voyageExport = new VoyageExport();
                }
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                voyageExport.setLineNo(carriersOrLineTemp);
                session.setAttribute("addvoyagerecords", voyageExport);
                session.setAttribute("exist", "records");
                path1 = "jsps/voyagemanagement/voyagePopUp.jsp";

            } else if (searchssline != null && searchssline.equals("Quotessline")) {
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                session.setAttribute("sslineTypes", carriersOrLineTemp);
                path1 = "jsps/fclQuotes/Quote.jsp";
            } //added by chandu for new Booking quotes NewBookingFCLs
            else if (searchssline != null && searchssline.equals("NewBookingFCLs")) {
                BookingFcl bookingfcl = null;
                String sslinenumbera = searchSSLineForm.getSsLineNumber();
                String sslinenames = searchSSLineForm.getSsLineName();
                if (session.getAttribute("bookingValues") != null) {
                    bookingfcl = (BookingFcl) session.getAttribute("bookingValues");

                } else {
                    bookingfcl = new BookingFcl();
                }
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                bookingfcl.setSSLine(carriersOrLineTemp.getCarriercode());
                bookingfcl.setSslname(carriersOrLineTemp.getCarriername());
                session.setAttribute("bookingValues", bookingfcl);
                path1 = "jsps/fclQuotes/newBookings.jsp";
            } //added for Editpages
            else if (searchssline != null && searchssline.equals("EditNewBookingFCL")) {
                BookingFcl bookingfcl = null;
                String sslinenumbera = searchSSLineForm.getSsLineNumber();
                String sslinenames = searchSSLineForm.getSsLineName();
                if (session.getAttribute("EditbookingValues") != null) {
                    bookingfcl = (BookingFcl) session.getAttribute("EditbookingValues");

                } else {
                    bookingfcl = new BookingFcl();
                }
                carriersOrLineTemp = (CarriersOrLineTemp) sslineList1.get(index);
                bookingfcl.setCarrierName(carriersOrLineTemp);
                session.setAttribute("CarrierObjects", bookingfcl);
                path1 = "jsps/fclQuotes/EditBookings.jsp";
            }


            request.setAttribute("path1", path1);
            request.setAttribute("buttonValue", "completed");
        } else if (buttonValue != null && buttonValue.equals("search")) {
            List fclList = carriersOrLineDAO.findForSSLine(sslinenumber, sslinename);
            session.setAttribute("fclpopList", fclList);
        }

        return mapping.findForward("searchSSLine");

    }
}