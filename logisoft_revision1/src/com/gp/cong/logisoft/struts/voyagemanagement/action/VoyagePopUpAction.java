/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.voyagemanagement.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.VoyageExport;
import com.gp.cong.logisoft.domain.VoyageMaster;
import com.gp.cong.logisoft.hibernate.dao.CarriersOrLineDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.struts.voyagemanagement.form.VoyagePopUpForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 08-13-2008
 * 
 * XDoclet definition:
 * @struts.action path="/voyagePopUp" name="voyagePopUpForm" input="/jsps/voyagemanagement/voyagePopUp.jsp" scope="request" validate="true"
 */
public class VoyagePopUpAction extends Action {
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
        VoyagePopUpForm voyagePopUpForm = (VoyagePopUpForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = voyagePopUpForm.getButtonValue();
        VoyageExport voyageExport = new VoyageExport();
        String trmNum = voyagePopUpForm.getTerminalNumber();
        String tername = voyagePopUpForm.getTerminalName();
        String portNum = voyagePopUpForm.getDestSheduleNumber();
        String portName = voyagePopUpForm.getDestAirportname();
        String comCode = voyagePopUpForm.getComCode();
        String sslineNO = voyagePopUpForm.getSslinenumber();
        String ssName = voyagePopUpForm.getSslinename();
        CarriersOrLineDAO carriersOrLineDAO = new CarriersOrLineDAO();
        CarriersOrLineTemp carriersOrLineTemp = new CarriersOrLineTemp();
        CarriersOrLineTemp carries = null;
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        RefTerminalTemp refTerminal1 = null;
        PortsTemp destObj = null;
        PortsDAO portsDAO = new PortsDAO();
        DBUtil dbutil = new DBUtil();
        List list = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();


        if (request.getAttribute("voyageExport") != null) {
            request.removeAttribute("voyageExport");
        }
        if (session.getAttribute("voyageExport1") != null) {
            session.removeAttribute("voyageExport1");
        }

        if (session.getAttribute("exportvoyageport") != null) {
            session.removeAttribute("exportvoyageport");
        }
        if (session.getAttribute("voyagerate") != null) {
            session.removeAttribute("voyagerate");
        }

        if (session.getAttribute("voyageExport1") != null) {
            session.removeAttribute("voyageExport1");
        }
        if (session.getAttribute("searchvoyageList") != null) {

            session.removeAttribute("searchvoyageList");

        }
        if (session.getAttribute("addvoyagerecords") != null) {

            session.removeAttribute("addvoyagerecords");

        }
        if (session.getAttribute("addvesselrecords") != null) {

            session.removeAttribute("addvesselrecords");

        }
        if (session.getAttribute("voyagepopup1") != null) {
            session.removeAttribute("voyagepopup1");
        }
        if (session.getAttribute("voyageExportPopup") != null) {

            session.removeAttribute("voyageExportPopup");
        }
        if (session.getAttribute("voyageExportPopup") != null) {

            session.removeAttribute("voyageExportPopup");
        }
        if (session.getAttribute("exportvoyages") != null) {

            session.removeAttribute("exportvoyages");
        }
        if (session.getAttribute("addvoyagerecords") != null) {
            voyageExport = (VoyageExport) session.getAttribute("addvoyagerecords");
        }

        if (trmNum != null && !trmNum.equals("")) {
            RefTerminalTemp refTerminal = refTerminalDAO.findById1(trmNum);
            if (refTerminal != null) {
                voyageExport.setOriginTerminal(refTerminal);
                session.setAttribute("addvoyagerecords", voyageExport);
            } else {

                request.setAttribute("message", "Please enter proper terminal number");
                RefTerminalTemp r1 = null;
                voyageExport.setOriginTerminal(r1);
                session.setAttribute("addvoyagerecords", voyageExport);
            }
        } else {
            RefTerminalTemp r1 = null;
            voyageExport.setOriginTerminal(r1);
            session.setAttribute("addvoyagerecords", voyageExport);
        }

        if (portNum != null && !portNum.equals("")) {
            List portsList = portsDAO.findports(portNum);
            if (portsList != null && portsList.size() > 0) {
                PortsTemp ports = (PortsTemp) portsList.get(0);
                voyageExport.setDestinationPort(ports);
                session.setAttribute("addvoyagerecords", voyageExport);
            } else {

                request.setAttribute("message", "Please enter proper Destination Port");
                PortsTemp p1 = null;
                voyageExport.setDestinationPort(p1);
                session.setAttribute("addvoyagerecords", voyageExport);
            }
        } else {
            PortsTemp p1 = null;
            voyageExport.setDestinationPort(p1);
            session.setAttribute("addvoyagerecords", voyageExport);
        }

        if (comCode != null && !comCode.equals("")) {
            List comList = genericCodeDAO.findForGenericCode(comCode);
            if (comList != null && comList.size() > 0) {
                GenericCode gen = (GenericCode) comList.get(0);
                voyageExport.setComNum(gen);
                session.setAttribute("addvoyagerecords", voyageExport);
            } else {

                request.setAttribute("message", "Please enter proper Commodity Code");
                GenericCode g1 = null;
                voyageExport.setComNum(g1);
                session.setAttribute("addvoyagerecords", voyageExport);
            }
        } else {
            GenericCode g1 = null;
            voyageExport.setComNum(g1);
            session.setAttribute("addvoyagerecords", voyageExport);
        }
        if (sslineNO != null && !sslineNO.equals("") || (ssName != null && !ssName.equals(""))) {

            List SSNo = carriersOrLineDAO.findForSSLine1(sslineNO);

            if (SSNo != null && SSNo.size() > 0) {
                carriersOrLineTemp = (CarriersOrLineTemp) SSNo.get(0);
                voyageExport.setLineNo(carriersOrLineTemp);
                session.setAttribute("addvoyagerecords", voyageExport);
            } else {

                request.setAttribute("message", "Please enter proper SS LINE number Code");
                CarriersOrLineTemp c1 = null;
                voyageExport.setLineNo(c1);
                session.setAttribute("addvoyagerecords", voyageExport);
            }
        } else {
            CarriersOrLineTemp c1 = null;
            voyageExport.setLineNo(c1);
            session.setAttribute("addvoyagerecords", voyageExport);
        }
        //-----------------------------------------
        if (buttonValue != null && buttonValue.equals("search")) {
            if (request.getAttribute("update") == null) {
                list = dbutil.getExportDetails(voyageExport.getOriginTerminal(), voyageExport.getDestinationPort(), voyageExport.getLineNo());

                if (list != null && list.size() > 0) {

                    request.setAttribute("update", "This Combination is already exist");

                }
            }

            if (session.getAttribute("exportvoyageport") != null) {
                session.removeAttribute("exportvoyageport");
            }
            if (session.getAttribute("voyageExport1") != null) {

                session.removeAttribute("voyageExport1");
            }
            if (session.getAttribute("voyageeditrecord") != null) {
                session.removeAttribute("voyageeditrecord");
            }
            session.setAttribute("addvoyagerecords", voyageExport);
            request.setAttribute("sendfclcontrol", "sendfclcontrol");
            session.setAttribute("voyagerate", "save");


        /*if(session.getAttribute("addlclColoadMaster")!=null){
        fclBuy=(FclBuy)session.getAttribute("addlclColoadMaster");
        }
        list=dbutil.getCoLoadDetails(lCLColoadMaster.getOriginTerminal(),lCLColoadMaster.getDestinationPort(),lCLColoadMaster.getCommodityCode());

        if(list!=null&&list.size()>0){

        request.setAttribute("update", "This COmbination is already exist");

        }*/
        }

        if (buttonValue != null && buttonValue.equals("popupsearch")) {

            if (trmNum != null && !trmNum.equals("")) {

                refTerminal1 = refTerminalDAO.findById1(trmNum);
                if (refTerminal1 != null) {
                    voyageExport.setOriginTerminal(refTerminal1);
                    session.setAttribute("addvoyagerecords", voyageExport);
                }

            }

            if (tername != null && !tername.equals("")) {

                RefTerminalTemp refTerminalobj = null;
                List terminal = refTerminalDAO.findForManagement(null, tername, null, null);
                Iterator iter = terminal.iterator();
                if (terminal.size() > 0) {
                    refTerminalobj = (RefTerminalTemp) terminal.get(0);
                }
                if (refTerminalobj != null) {
                    voyageExport.setOriginTerminal(refTerminalobj);
                    session.setAttribute("addvoyagerecords", voyageExport);
                }

            }

            if (portNum != null && !portNum.equals("")) {

                List portsList = portsDAO.findPortCode(portNum, "0001");
                if (portsList != null && portsList.size() > 0) {
                    destObj = (PortsTemp) portsList.get(0);
                    voyageExport.setDestinationPort(destObj);
                    session.setAttribute("addvoyagerecords", voyageExport);
                }

            }
            if (portName != null && !portName.equals("")) {

                List portsList = portsDAO.findPierCode(null, portName);
                if (portsList != null && portsList.size() > 0) {

                    destObj = (PortsTemp) portsList.get(0);
                    voyageExport.setDestinationPort(destObj);
                    session.setAttribute("addvoyagerecords", voyageExport);
                }
            }

            if (sslineNO != null && !sslineNO.equals("")) {

                List codeList = carriersOrLineDAO.findForSSLine1(sslineNO);
                if (codeList != null && codeList.size() > 0) {
                    carries = (CarriersOrLineTemp) codeList.get(0);
                    voyageExport.setLineNo(carries);
                    session.setAttribute("addvoyagerecords", voyageExport);
                }
            }

            if (ssName != null && !ssName.equals("")) {

                List codeList = carriersOrLineDAO.findForSSLine(null, ssName);
                if (codeList != null && codeList.size() > 0) {
                    carries = (CarriersOrLineTemp) codeList.get(0);
                    voyageExport.setLineNo(carries);
                    session.setAttribute("addvoyagerecords", voyageExport);
                }
            }
        }

        return mapping.findForward("addvoyagepopup");
    }
}