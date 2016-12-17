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

import com.gp.cong.logisoft.beans.SearchCarriersBean;
import com.gp.cong.logisoft.domain.CarriersOrLine;
import com.gp.cong.logisoft.domain.ClaimDetails;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.ManagingCarriersOATForm;

/** 
 * MyEclipse Struts
 * Creation date: 12-05-2007
 * 
 * XDoclet definition:
 * @struts.action path="/managingCarriersOAT" name="managingCarriersOATForm" input="jsps/datareference/managingCarriersOAT.jsp" scope="request" validate="true"
 */
public class ManagingCarriersOATAction extends Action {
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
        ManagingCarriersOATForm managingCarriersOATForm = (ManagingCarriersOATForm) form;// TODO Auto-generated method stub
        HttpSession session = request.getSession();

        String buttonValue = managingCarriersOATForm.getButtonValue();
        String forwardName = "";
        String message = "";
        String loginname = null;
        String msg = "";
        String carriercode = managingCarriersOATForm.getCarriercode();
        String carriername = managingCarriersOATForm.getCarriername();
        String carriertype = managingCarriersOATForm.getCarriertype();
        String SCAC = managingCarriersOATForm.getSCAC();
        String match = managingCarriersOATForm.getMatch();
        SearchCarriersBean scBean = new SearchCarriersBean();
        scBean.setCarriercode(carriercode);
        scBean.setCarriername(carriername);
        scBean.setCarriertype(carriertype);
        scBean.setSCAC(SCAC);
        scBean.setMatch(match);


        ClaimDetails claim1 = new ClaimDetails();

        if (request.getParameter("paramid") != null) {
            CarriersOrLineDAO carriersDAO = new CarriersOrLineDAO();
            CarriersOrLine carriers = carriersDAO.findById((request.getParameter("paramid")));
            User userid = null;
            UserDAO user1 = new UserDAO();
            if (session.getAttribute("loginuser") != null) {
                userid = (User) session.getAttribute("loginuser");

            }
            ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
            ProcessInfo pi = new ProcessInfo();
            String programid = null;
            programid = (String) session.getAttribute("processinfoforcarriers");
            String recordid = carriers.getCarriercode();
            String editstatus = "startedited";
            String deletestatus = "startdeleted";

            ProcessInfo processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus, editstatus);
            if (processinfoobj != null) {
                String view = "3";
                User loginuser = user1.findById(processinfoobj.getUserid());
                loginname = loginuser.getLoginName();
                msg = "This record is being used by ";
                message = msg + loginname;
                request.setAttribute("message", message);
                session.setAttribute("view", view);
                forwardName = "editCarriersOAT";

            } else {
                pi.setUserid(userid.getUserId());
                pi.setProgramid(Integer.parseInt(programid));
                java.util.Date currdate = new java.util.Date();
                pi.setProcessinfodate(currdate);
                pi.setEditstatus(editstatus);
                pi.setRecordid(recordid);
                processinfoDAO.save(pi);
                if (session.getAttribute("view") != null) {
                    session.removeAttribute("view");
                }
                if (session.getAttribute("airocean") != null) {
                    session.removeAttribute("airocean");
                }
            }
            session.setAttribute("carriers", carriers);
            forwardName = "editCarriersOAT";

        } else if (request.getParameter("param") != null) {
            CarriersOrLineDAO carrier1 = new CarriersOrLineDAO();
            CarriersOrLine carrier = carrier1.findById(request.getParameter("param"));
            String view = "3";
            session.setAttribute("view", view);
            session.setAttribute("carriers", carrier);
            forwardName = "editCarriersOAT";
        } else {


            if (buttonValue.equals("search")) {
                request.setAttribute("scBean", scBean);
                CarriersOrLineDAO carriersDAO = new CarriersOrLineDAO();

                if (match.equals("match")) {
                    List carriersList = carriersDAO.findForSearchCarriersAction(carriercode, carriername, carriertype, SCAC);

                    if (carriersList.size() == 0) {
                        session.setAttribute("Notfound", carriercode);
                    }

                    session.setAttribute("carrierList", carriersList);
                    if (carriercode != null && !carriercode.equals("")) {
                        session.setAttribute("mangingcarrierCaption", "CarrierCode {Match Only}");

                    }
                    if (carriername != null && !carriername.equals("")) {
                        session.setAttribute("mangingcarrierCaption", "CarrierName {Match Only}");
                    }
                    if (carriertype != null && !carriertype.equals("0")) {
                        session.setAttribute("mangingcarrierCaption", "CarrierType {Match Only}");
                    }

                    if (SCAC != null && !SCAC.equals("")) {
                        session.setAttribute("mangingcarrierCaption", "SCAC{Match Only}");
                    }

                } else if (match.equals("starts")) {
                    List carriersList = carriersDAO.findForSearchCarriersStartAction(carriercode, carriername, carriertype, SCAC, match);

                    session.setAttribute("carrierList", carriersList);
                    if (carriercode != null && !carriercode.equals("")) {
                        session.setAttribute("mangingcarrierCaption", "CarrierCode{Start List At}");
                    }

                    if (carriername != null && !carriername.equals("0")) {
                        session.setAttribute("mangingcarrierCaption", "CarrierName{Start List At}");
                    }

                    if (carriertype != null && !carriertype.equals("0")) {
                        session.setAttribute("mangingcarrierCaption", "CarrierType{Start List At}");
                    }
                    if (SCAC != null && !SCAC.equals("")) {

                        session.setAttribute("mangingcarrierCaption", "SCAC{Start List At}");
                    }

                }
                forwardName = "managingCarriersOAT";
            }

            if (buttonValue.equals("searchall")) {
                forwardName = "managingCarriersOAT";
            }
            if (buttonValue.equals("add")) {
                forwardName = "addCarriersOAT";
            }
            request.setAttribute("buttonValue", buttonValue);

        }
        return mapping.findForward(forwardName);
    }

    public void redirectToReport(HttpServletRequest request, HttpServletResponse response) throws Exception{
            response.sendRedirect(request.getContextPath() + "/report");
    }
}
	

