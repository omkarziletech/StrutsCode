/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.VoyageExport;
import com.gp.cong.logisoft.hibernate.dao.VoyageExportDAO;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.struts.form.VoyagesearchForm;

/** 
 * MyEclipse Struts
 * Creation date: 09-19-2008
 * 
 * XDoclet definition:
 * @struts.action path="/voyagesearch" name="voyagesearchForm" input="/jsps/fclQuotes/voyagesearch.jsp" scope="request" validate="true"
 */
public class VoyagesearchAction extends Action {
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
        VoyagesearchForm voyagesearchForm = (VoyagesearchForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = voyagesearchForm.getButtonValue();
        String voyageName = voyagesearchForm.getVoyageName();
        String voyageNumber = voyagesearchForm.getVoyageNumber();


        VoyageExportDAO voyageExportDAO = new VoyageExportDAO();

        VoyageExport voyageExport = new VoyageExport();
        String searchVoyage = "";
        String path1 = "";

        int ind = -1;
        if (voyagesearchForm.getIndex() != null && !voyagesearchForm.getIndex().equals("")) {
            ind = Integer.parseInt(voyagesearchForm.getIndex());
        }

        if ((request.getParameter("index") != null && !request.getParameter("index").equals("")) || (voyagesearchForm.getIndex() != null && !voyagesearchForm.getIndex().equals(""))) {
            int index = Integer.parseInt(request.getParameter("index"));
            if (ind >= 0) {
                index = ind;
            }
            List voyageList1 = (List) session.getAttribute("voyagepopList");
            if (session.getAttribute("searchVoyage") != null) {

                searchVoyage = (String) session.getAttribute("searchVoyage");
            }

            if (searchVoyage != null && searchVoyage.equals("NewFCLBL")) {
                FclBl fclbl = null;
                if (session.getAttribute("fclBillValues") != null || session.getAttribute("EditfclBillValues") != null) {
                    if (session.getAttribute("EditfclBillValues") != null) {

                        fclbl = (FclBl) session.getAttribute("EditfclBillValues");
                    } else if (session.getAttribute("fclBillValues") != null) {

                        fclbl = (FclBl) session.getAttribute("fclBillValues");
                    }

                } else {
                    fclbl = new FclBl();
                }
                voyageExport = (VoyageExport) voyageList1.get(index);
                //fclbl.setVoyages(voyageExport.getId());
                session.setAttribute("voyageObject", fclbl);
                path1 = "jsps/fclQuotes/FclBillLadding.jsp";

            }


            request.setAttribute("path1", path1);
            request.setAttribute("buttonValue", "completed");
        } else if (buttonValue != null && buttonValue.equals("search")) {

            List VoyageList = voyageExportDAO.getVoyageRecords(Integer.parseInt(voyageNumber));

            session.setAttribute("voyagepopList", VoyageList);
        }
        return mapping.findForward("searchVoyage");

    }
}