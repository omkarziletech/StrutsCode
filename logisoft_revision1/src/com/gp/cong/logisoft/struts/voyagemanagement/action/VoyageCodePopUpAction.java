/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.voyagemanagement.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.ChangeVoyage;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.voyagemanagement.form.VoyageCodePopUpForm;

/** 
 * MyEclipse Struts
 * Creation date: 10-25-2008
 * 
 * XDoclet definition:
 * @struts.action path="/voyageCodePopUp" name="voyageCodePopUpForm" input="/jsps/voyagemanagement/voyageCodePopUp.jsp" scope="request" validate="true"
 */
public class VoyageCodePopUpAction extends Action {
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
        VoyageCodePopUpForm voyageCodePopUpForm = (VoyageCodePopUpForm) form;// TODO Auto-generated method stub
        HttpSession session = (HttpSession) request.getSession();
        String code;
        String codeDescription;
        String buttonValue;
        //	String index="";

        code = voyageCodePopUpForm.getCode();
        codeDescription = voyageCodePopUpForm.getCodeDescription();
        buttonValue = voyageCodePopUpForm.getButtonValue();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        String searchReason = "";
        String path1 = "";
        int ind = -1;


        if (voyageCodePopUpForm.getIndex() != null && !voyageCodePopUpForm.getIndex().equals("")) {
            ind = Integer.parseInt(voyageCodePopUpForm.getIndex());
        }

        if ((request.getParameter("index") != null && !request.getParameter("index").equals("")) || (voyageCodePopUpForm.getIndex() != null && !voyageCodePopUpForm.getIndex().equals(""))) {
            int index = Integer.parseInt(request.getParameter("index"));
            if (ind >= 0) {
                index = ind;
            }
            List VoyageReasonList = (List) session.getAttribute("reasonPopList");
            if (session.getAttribute("searchReason") != null) {

                searchReason = (String) session.getAttribute("searchReason");
            }
            if (searchReason != null && !searchReason.equals("") && searchReason.equals("addreason")) {

                ChangeVoyage changevoy = null;
                if (session.getAttribute("VoyageReason") != null) {

                    changevoy = (ChangeVoyage) session.getAttribute("VoyageReason");
                } else {
                    changevoy = new ChangeVoyage();

                }
                GenericCode gen = new GenericCode();
                gen = (GenericCode) VoyageReasonList.get(index);
                changevoy.setCodetypeid(Integer.parseInt(gen.getCode()));
                changevoy.setCodeDescription(gen.getCodedesc());
                session.setAttribute("VoyageReason", changevoy);
                path1 = "jsps/voyagemanagement/exportVoyages.jsp";
            }
            request.setAttribute("path1", path1);
            request.setAttribute("buttonValue", "completed");
        } else if (buttonValue != null && buttonValue.equals("search")) {

            //List VoyageList=genericCodeDAO.findforTableData(15, code, codeDescription);
            List VoyageList = genericCodeDAO.findForGenericAction(15, code, codeDescription);

            /*GenericCode ayo=(GenericCode)VoyageList.get(0);


*/


            session.setAttribute("reasonPopList", VoyageList);
        }

        return mapping.findForward("searchVoyageReason");
    }
}