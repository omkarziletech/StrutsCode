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

import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.AddFutureFclPopForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 09-24-2008
 * 
 * XDoclet definition:
 * @struts.action path="/addFutureFclPop" name="addFutureFclPopForm" input="/jsps/ratemanagement/addFutureFclPop.jsp" scope="request"
 */
public class AddFutureFclPopAction extends Action {
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
        AddFutureFclPopForm addFCLPopupForm = (AddFutureFclPopForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = addFCLPopupForm.getButtonValue();
        FclBuy fclBuy = new FclBuy();
        String trmNum = addFCLPopupForm.getTerminalNumber();
        String portNum = addFCLPopupForm.getDestSheduleNumber();
        String comCodedesc = addFCLPopupForm.getComDescription();
        String comCode = addFCLPopupForm.getComCode();
        String tername = addFCLPopupForm.getTerminalName();
        String comCodeDesc = addFCLPopupForm.getComDescription();
        String portName = addFCLPopupForm.getDestAirportname();
        String sslineNO = addFCLPopupForm.getSslinenumber();
        String ssName = addFCLPopupForm.getSslinename();
        CustomerDAO carriersOrLineDAO = new CustomerDAO();
        TradingPartnerTemp carriersOrLineTemp = new TradingPartnerTemp();
        UnLocationDAO portsDAO = new UnLocationDAO();
        DBUtil dbutil = new DBUtil();
        List list = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        UnLocation refTerminal1 = null;
        UnLocation portsTemp = null;
        GenericCode genericCode = null;


        if (buttonValue != null && buttonValue.equals("search")) {

            if (session.getAttribute("futureaddfclrecords") != null) {
                fclBuy = (FclBuy) session.getAttribute("futureaddfclrecords");
            }

            if (trmNum != null && !trmNum.equals("")) {
                List unLocationList = portsDAO.findForManagement(trmNum, null);
                if (unLocationList != null && unLocationList.size() > 0) {
                    UnLocation unLocation = (UnLocation) unLocationList.get(0);
                    fclBuy.setOriginTerminal(unLocation);

                } else {
                    request.setAttribute("message", "Please enter proper terminal number");
                    UnLocation r1 = null;
                    fclBuy.setOriginTerminal(r1);

                }
            } else {
                UnLocation r1 = null;
                fclBuy.setOriginTerminal(r1);

            }

            if (portNum != null && !portNum.equals("")) {
                List portsList = portsDAO.findForManagement(portNum, null);
                if (portsList != null && portsList.size() > 0) {
                    UnLocation ports = (UnLocation) portsList.get(0);
                    fclBuy.setDestinationPort(ports);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                } else {
                    request.setAttribute("message", "Please enter proper Destination Port");
                    UnLocation p1 = null;
                    fclBuy.setDestinationPort(p1);

                }
            } else {
                UnLocation p1 = null;
                fclBuy.setDestinationPort(p1);

            }

            if (comCode != null && !comCode.equals("")) {
                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    GenericCode gen = (GenericCode) comList.get(0);
                    fclBuy.setComNum(gen);

                } else {
                    request.setAttribute("message", "Please enter proper Commodity Code");
                    GenericCode g1 = null;
                    fclBuy.setComNum(g1);

                }
            }
            if (comCodedesc != null && !comCodedesc.equals("")) {
                List codeList = genericCodeDAO.findForAirRates(null,
                        comCodedesc);
                if (codeList != null && codeList.size() > 0) {
                    GenericCode gen = (GenericCode) codeList.get(0);
                    fclBuy.setComNum(gen);

                }

            }
            if (sslineNO != null && !sslineNO.equals("") || (ssName != null && !ssName.equals(""))) {

                List SSNo = carriersOrLineDAO.findForConsigneetNo3(sslineNO, ssName, null);

                if (SSNo != null && SSNo.size() > 0) {
                    carriersOrLineTemp = (TradingPartnerTemp) SSNo.get(0);
                    fclBuy.setSslineNo(carriersOrLineTemp);

                }

            }
            session.setAttribute("futureaddfclrecords", fclBuy);
            if (request.getAttribute("update") == null) {
                list = dbutil.getFCLDetails(fclBuy.getOriginTerminal(), fclBuy.getDestinationPort(), fclBuy.getComNum(), fclBuy.getSslineNo(), fclBuy.getOriginalRegion(), fclBuy.getDestinationRegion());
                if (list != null && list.size() > 0) {
                } else {

                    request.setAttribute("update", "This Combination is not exist");
                }
            }

            request.setAttribute("sendfclcontrol", "sendfclcontrol");


            session.setAttribute("editrecords", "save");



        }

        if (buttonValue != null && buttonValue.equals("popupsearch")) {
            if (session.getAttribute("futureaddfclrecords") != null) {
                fclBuy = (FclBuy) session.getAttribute("futureaddfclrecords");
            } else {
                fclBuy = new FclBuy();

            }


            if (trmNum != null && !trmNum.equals("")) {

                List unLocationList = portsDAO.findForManagement(trmNum, null);
                if (unLocationList != null && unLocationList.size() > 0) {
                    refTerminal1 = (UnLocation) unLocationList.get(0);
                    fclBuy.setOriginTerminal(refTerminal1);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                }

            }

            if (tername != null && !tername.equals("")) {

                UnLocation refTerminalobj = null;
                List terminal = portsDAO.findForManagement(null, tername);

                if (terminal.size() > 0) {
                    refTerminalobj = (UnLocation) terminal.get(0);
                }
                if (refTerminalobj != null) {
                    fclBuy.setOriginTerminal(refTerminalobj);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                }

            }

            if (portNum != null && !portNum.equals("")) {

                List portsList = portsDAO.findForManagement(portNum, null);
                if (portsList != null && portsList.size() > 0) {
                    portsTemp = (UnLocation) portsList.get(0);
                    fclBuy.setDestinationPort(portsTemp);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                }
            }

            if (portName != null && !portName.equals("")) {

                List portsList = portsDAO.findForManagement(null, portName);
                if (portsList != null && portsList.size() > 0) {

                    portsTemp = (UnLocation) portsList.get(0);
                    fclBuy.setDestinationPort(portsTemp);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                }

            }

            if (comCode != null && !comCode.equals("") && !comCode.equals("%")) {

                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    genericCode = (GenericCode) comList.get(0);
                    fclBuy.setComNum(genericCode);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                }
            }

            if (comCodeDesc != null && !comCodeDesc.equals("")) {

                List codeList = genericCodeDAO.findForAirRates(null, comCodeDesc);
                if (codeList != null && codeList.size() > 0) {
                    genericCode = (GenericCode) codeList.get(0);
                    fclBuy.setComNum(genericCode);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                }

            }

            if (sslineNO != null && !sslineNO.equals("")) {

                List codeList = carriersOrLineDAO.findAccountNo1(sslineNO);
                if (codeList != null && codeList.size() > 0) {
                    carriersOrLineTemp = (TradingPartnerTemp) codeList.get(0);
                    fclBuy.setSslineNo(carriersOrLineTemp);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                }
            }

            if (ssName != null && !ssName.equals("")) {

                List codeList = carriersOrLineDAO.findAccountName1(ssName);
                if (codeList != null && codeList.size() > 0) {
                    carriersOrLineTemp = (TradingPartnerTemp) codeList.get(0);
                    fclBuy.setSslineNo(carriersOrLineTemp);
                    session.setAttribute("futureaddfclrecords", fclBuy);
                }
            }


        }
        return mapping.findForward("addfclpopup");
    }
}