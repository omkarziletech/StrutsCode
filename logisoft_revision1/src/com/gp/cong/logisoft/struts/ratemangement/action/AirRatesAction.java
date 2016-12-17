package com.gp.cong.logisoft.struts.ratemangement.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.AirRatesBean;


import com.gp.cong.logisoft.domain.AirFreightDocumentCharges;

import com.gp.cong.logisoft.domain.AirFreightFlightShedules;
import com.gp.cong.logisoft.domain.AirStandardCharges;

import com.gp.cong.logisoft.domain.GenericCode;



import com.gp.cong.logisoft.domain.ProcessInfo;

import com.gp.cong.logisoft.domain.StandardCharges;
import com.gp.cong.logisoft.domain.AirCommodityCharges;
import com.gp.cong.logisoft.domain.User;

import com.gp.cong.logisoft.domain.AirWeightRangesRates;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;

import com.gp.cong.logisoft.hibernate.dao.StandardChargesDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.AirRatesForm;
import com.gp.cong.logisoft.util.DBUtil;

public class AirRatesAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
        AirRatesForm airRatesForm = (AirRatesForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = airRatesForm.getButtonValue();
        String forwardName = "";
        String search = airRatesForm.getSearch();
        String match = airRatesForm.getMatch();
        String common = airRatesForm.getCommon();
        AirRatesBean srBean = new AirRatesBean();
        String terminalNumber = airRatesForm.getTerminalNumber();
        String destPort = airRatesForm.getDestSheduleNumber();
        String portName = airRatesForm.getDestAirportname();
        String comCode = airRatesForm.getComCode();
        String comCodedesc = airRatesForm.getComDescription();
        srBean.setMatch(match);
        DBUtil dbutil = new DBUtil();
        String terminalName = airRatesForm.getTerminalName();
        List commonList = new ArrayList();
        List noncommonList = new ArrayList();
        GenericCode terminalObj = null;
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        PortsDAO portsDAO = new PortsDAO();
        GenericCode portObj = null;
        GenericCode genObj = null;
        boolean com = false;
        boolean flag = false;
        String desName = airRatesForm.getDestAirportname();
        List airRatesList = new ArrayList();
        List addCommon = new ArrayList();
        StandardChargesDAO standardChargesDAO = new StandardChargesDAO();
        StandardCharges airRatesObj1 = null;
        String loginName = null;
        String msg = "";
        String message = "";
        StandardCharges scharge = null;
        StandardChargesDAO sChargesDAO = new StandardChargesDAO();
        if (session.getAttribute("manageairrates") != null) {
            airRatesObj1 = (StandardCharges) session.getAttribute("manageairrates");
            terminalObj = airRatesObj1.getOrgTerminal();
            portObj = airRatesObj1.getDestPort();
            genObj = airRatesObj1.getComCode();

        } else {

            airRatesObj1 = new StandardCharges();
        }
        if (session.getAttribute("message") != null) {
            session.removeAttribute("message");
        }

        if (request.getParameter("paramid") != null || (buttonValue != null && buttonValue.equals("paramid")) || (buttonValue != null && buttonValue.equals("paramidDoc"))) {

            if (request.getParameter("paramid") != null && !request.getParameter("paramid").equals("")) {
                scharge = sChargesDAO.findById(Integer.parseInt(request.getParameter("paramid")));
            }

            if (buttonValue != null && buttonValue.equals("paramid")) {
                if (session.getAttribute("manageairrates") != null) {
                    List list = new ArrayList();
                    genObj = genericCodeDAO.findById(11292);

                    StandardCharges retailRatesObj1 = (StandardCharges) session.getAttribute("manageairrates");
                    list = standardChargesDAO.findAllDetails(retailRatesObj1.getOrgTerminal(), retailRatesObj1.getDestPort(), genObj);

                    if (list != null && list.size() > 0) {
                        scharge = (StandardCharges) list.get(0);
                    }
                    session.setAttribute("getAirAgsss", "Doc");
                }
                if (session.getAttribute("airStandardCharges") != null) {
                    session.removeAttribute("airStandardCharges");
                }
            }
            // paramidDoc Button functionality :--------------------------------------
            if (buttonValue != null && buttonValue.equals("paramidDoc")) {
                if (session.getAttribute("manageairrates") != null) {
                    List list = new ArrayList();
                    genObj = genericCodeDAO.findById(11292);

                    StandardCharges retailRatesObj1 = (StandardCharges) session.getAttribute("manageairrates");
                    list = standardChargesDAO.findAllDetails(retailRatesObj1.getOrgTerminal(), retailRatesObj1.getDestPort(), genObj);
                    if (list != null && list.size() > 0) {
                        scharge = (StandardCharges) list.get(0);
                    }
                    session.setAttribute("getDocFreight", "Doc");
                }
                if (session.getAttribute("airStandardCharges") != null) {
                    session.removeAttribute("airStandardCharges");
                }
            // paramidDoc Button functionality :--------------------------------------
            }
            if (request.getParameter("ind") != null) {

                AirStandardCharges retailStandard = new AirStandardCharges();
                int ind = Integer.parseInt(request.getParameter("ind"));
                List codeList1 = (List) session.getAttribute("commonList");
                retailStandard = (AirStandardCharges) codeList1.get(ind);
                session.setAttribute("airStandardCharges", retailStandard);
                session.setAttribute("getAirEdit", "getEdit");

            }
            User userid = null;
            UserDAO user1 = new UserDAO();
            if (session.getAttribute("loginuser") != null) {
                userid = (User) session.getAttribute("loginuser");
            }
            ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
            ProcessInfo pi = new ProcessInfo();
            String programid = null;
            programid = (String) session.getAttribute("processinfoforairRates");
            String recordid = "";
            if (scharge.getId() != null) {
                recordid = scharge.getId().toString();
            }
            String editstatus = "startedited";
            String deletestatus = "startdeleted";
            ProcessInfo processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus, editstatus);
            if (processinfoobj != null) {
                String view = "3";
                User loginuser = user1.findById(processinfoobj.getUserid());
                loginName = loginuser.getLoginName();
                msg = "This record is being used by ";
                message = msg + loginName;
                session.setAttribute("message", message);
                session.setAttribute("view", view);
                forwardName = "airRatesFrame";
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

            }
            session.setAttribute("editrecord", "edit");
            session.setAttribute("standardCharges", scharge);
            forwardName = "airRatesFrame";
            if (session.getAttribute("setTabEnable") != null) {
                session.removeAttribute("setTabEnable");
            }

            if (scharge.getComCode() != null && scharge.getComCode().getCode() != null) {
                if (scharge.getComCode().getCode().equals("000000")) {
                    session.setAttribute("setTabEnable", "enable");
                }
            }
            List airStdList = new ArrayList();
            List airStdList1 = new ArrayList();
            List airStdList2 = new ArrayList();
            List airStdList3 = new ArrayList();
            List airStdList4 = new ArrayList();

            if (scharge.getAirStandardCharges() != null) {
                Iterator iter = (Iterator) scharge.getAirStandardCharges().iterator();
                while (iter.hasNext()) {
                    AirStandardCharges airStd = (AirStandardCharges) iter.next();
                    airStdList.add(airStd);
                }
            }
            if (scharge.getAirDocumentCharges() != null) {
                Iterator iter = (Iterator) scharge.getAirDocumentCharges().iterator();
                while (iter.hasNext()) {
                    AirFreightDocumentCharges airdoc = (AirFreightDocumentCharges) iter.next();
                    airStdList1.add(airdoc);
                }
            }
            if (scharge.getAirFlightSchedules() != null) {
                Iterator iter = (Iterator) scharge.getAirFlightSchedules().iterator();
                while (iter.hasNext()) {
                    AirFreightFlightShedules airff = (AirFreightFlightShedules) iter.next();
                    airStdList2.add(airff);
                }
            }
            if (scharge.getAirWeightRangeSet() != null) {
                Iterator iter = (Iterator) scharge.getAirWeightRangeSet().iterator();
                while (iter.hasNext()) {
                    AirWeightRangesRates airff = (AirWeightRangesRates) iter.next();
                    airStdList3.add(airff);
                }
            }
            if (scharge.getAirCommoditySet() != null) {
                Iterator iter = (Iterator) scharge.getAirCommoditySet().iterator();
                while (iter.hasNext()) {
                    AirCommodityCharges airff = (AirCommodityCharges) iter.next();
                    airStdList4.add(airff);
                }
            }

            session.setAttribute("cssList", airStdList4);
            session.setAttribute("airdetailsAdd", airStdList3);
            session.setAttribute("flightShedulesAdd", airStdList2);
            session.setAttribute("docChargesAdd", airStdList1);
            session.setAttribute("agssAdd", airStdList);
            session.setAttribute("standardCharges", scharge);

            if (session.getAttribute("applyGeneralStandardList") != null) {
                session.removeAttribute("applyGeneralStandardList");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            forwardName = "airRatesFrame";
        } //-----------------BOTTOM LINE RATES_---------------------
        else if (request.getParameter("rates") != null && !request.getParameter("rates").equals("")) {
            //TODO
        } else if (request.getParameter("param") != null) {
            scharge = sChargesDAO.findById(Integer.parseInt(request.getParameter("param")));
            String view = "3";
            session.setAttribute("view", view);
            session.setAttribute("standardCharges", scharge);
            if (session.getAttribute("setTabEnable") != null) {
                session.removeAttribute("setTabEnable");
            }
            if (scharge.getComCode() != null && scharge.getComCode().getCode() != null) {
                if (scharge.getComCode().getCode().equals("000000")) {
                    session.setAttribute("setTabEnable", "enable");
                } else {
                    session.setAttribute("setTabEnable", "able");
                }
            }
            if (request.getParameter("ind") != null) {

                AirStandardCharges retailStandard = new AirStandardCharges();
                int ind = Integer.parseInt(request.getParameter("ind"));
                List codeList1 = (List) session.getAttribute("commonList");
                retailStandard = (AirStandardCharges) codeList1.get(ind);
                session.setAttribute("airStandardCharges", retailStandard);
                session.setAttribute("getAirEdit", "getEdit");

            }
            List airStdList = new ArrayList();
            List airStdList1 = new ArrayList();
            List airStdList2 = new ArrayList();
            List airStdList3 = new ArrayList();
            List airStdList4 = new ArrayList();
            if (scharge.getAirStandardCharges() != null) {
                Iterator iter = (Iterator) scharge.getAirStandardCharges().iterator();
                while (iter.hasNext()) {
                    AirStandardCharges airStd = (AirStandardCharges) iter.next();
                    airStdList.add(airStd);
                }
            }
            if (scharge.getAirDocumentCharges() != null) {
                Iterator iter = (Iterator) scharge.getAirDocumentCharges().iterator();
                while (iter.hasNext()) {
                    AirFreightDocumentCharges airdoc = (AirFreightDocumentCharges) iter.next();
                    airStdList1.add(airdoc);
                }
            }
            if (scharge.getAirFlightSchedules() != null) {
                Iterator iter = (Iterator) scharge.getAirFlightSchedules().iterator();
                while (iter.hasNext()) {
                    AirFreightFlightShedules airff = (AirFreightFlightShedules) iter.next();
                    airStdList2.add(airff);
                }
            }
            if (scharge.getAirWeightRangeSet() != null) {
                Iterator iter = (Iterator) scharge.getAirWeightRangeSet().iterator();
                while (iter.hasNext()) {
                    AirWeightRangesRates airff = (AirWeightRangesRates) iter.next();
                    airStdList3.add(airff);
                }
            }
            if (scharge.getAirCommoditySet() != null) {
                Iterator iter = (Iterator) scharge.getAirCommoditySet().iterator();
                while (iter.hasNext()) {
                    AirCommodityCharges airff = (AirCommodityCharges) iter.next();
                    airStdList4.add(airff);
                }
            }
            session.setAttribute("cssList", airStdList4);
            session.setAttribute("airdetailsAdd", airStdList3);
            session.setAttribute("flightShedulesAdd", airStdList2);
            session.setAttribute("docChargesAdd", airStdList1);
            session.setAttribute("agssAdd", airStdList);
            session.setAttribute("editrecord", "edit");
            forwardName = "airRatesFrame";
        } else {
            if (buttonValue != null && buttonValue.equals("search")) {

                if (session.getAttribute("manageairrates") != null) {
                    airRatesObj1 = (StandardCharges) session.getAttribute("manageairrates");
                    terminalObj = airRatesObj1.getOrgTerminal();
                    portObj = airRatesObj1.getDestPort();
                    genObj = airRatesObj1.getComCode();
                } else {
                    airRatesObj1 = new StandardCharges();
                }
                if (terminalNumber != null && !terminalNumber.equals("") && !terminalNumber.equals("%")) {
                    //RefTerminalTemp refTerminal=refTerminalDAO.findById1(terminalNumber);
                    //	List TrnameList =refTerminalDAO.findByName(terminalName);
                    List codeList = genericCodeDAO.findGenericCode("1", terminalNumber);
                    if (codeList.size() > 0) {
                        genObj = (GenericCode) codeList.get(0);
                    }
                    if (genObj != null) {

                        airRatesObj1.setOrgTerminal(genObj);
                        session.setAttribute("manageairrates", airRatesObj1);
                    } else {
                        request.setAttribute("message", "Please enter proper Terminal");
                    }
                }

                if (destPort != null && !destPort.equals("") && !destPort.equals("%")) {
                    List portsList = genericCodeDAO.findGenericCode("1", destPort);
                    if (portsList != null && portsList.size() > 0) {
                        GenericCode ports = (GenericCode) portsList.get(0);
                        airRatesObj1.setDestPort(ports);
                        session.setAttribute("manageairrates", airRatesObj1);
                    } else {
                        request.setAttribute("message", "Please enter proper Destination Port");
                    }
                }
                if (comCode != null && !comCode.equals("")) {
                    List comList = genericCodeDAO.findForGenericCode(comCode);
                    if (comList != null && comList.size() > 0) {
                        GenericCode gen = (GenericCode) comList.get(0);
                        airRatesObj1.setComCode(gen);
                        session.setAttribute("manageairrates", airRatesObj1);
                        if (gen.getCode().equals("000000")) {
                            session.setAttribute("setTabEnable", "enable");
                        } else {
                            session.removeAttribute("setTabEnable");
                        }
                    } else {
                        request.setAttribute("message", "Please enter proper Commodity Code");
                    }
                }

                terminalObj = airRatesObj1.getOrgTerminal();
                portObj = airRatesObj1.getDestPort();
                genObj = airRatesObj1.getComCode();
                if (request.getAttribute("message") == null && (search != null && search.equals("get"))) {
                    if (terminalNumber != null && destPort != null && (comCode == null || comCode.equals(""))) {

                        airRatesList = standardChargesDAO.findForSearchAirRatesAction(terminalNumber, destPort, comCode, match);
                        for (int i = 0; i < airRatesList.size(); i++) {
                            StandardCharges aStandardCharges = (StandardCharges) airRatesList.get(i);
                            if (aStandardCharges.getComCode() != null && aStandardCharges.getComCode().getCode() != null && aStandardCharges.getComCode().getCode().equals("000000")) {
                                addCommon.add(aStandardCharges);
                                airRatesList.remove(i);
                                com = true;

                            }

                        }
                        if (!com) {
                            addCommon = standardChargesDAO.findForSearchAirRatesAction(terminalNumber, destPort, "000000", match);
                        }
                        for (int i = 0; i < addCommon.size(); i++) {
                            StandardCharges aStandardCharges = (StandardCharges) addCommon.get(i);
                            if (aStandardCharges.getComCode() != null && aStandardCharges.getComCode().getCode() != null && aStandardCharges.getComCode().getCode().equals("000000")) {
                                //Set s=aStandardCharges.getAirStandardCharges();
                                Iterator it = aStandardCharges.getAirStandardCharges().iterator();
                                while (it.hasNext()) {
                                    AirStandardCharges airStandardCharges = (AirStandardCharges) it.next();
                                    //if(airStandardCharges.getStandard()!=null && airStandardCharges.getStandard().equals("Y")){
                                    commonList.add(airStandardCharges);
                                    flag = true;
                                //}
                                }

                            }
                            if (!flag) {

                                session.setAttribute("airRatescaptionCaps", "No Accessorial Add");
                            } else {

                                session.setAttribute("airRatescaptionCaps", "Common Accessorial Charges (Standard Only)");
                            }
                            break;



                        }
                    } else if (terminalNumber != null && destPort != null && comCode != null) {


                        boolean b = false;
                        airRatesList = standardChargesDAO.findForSearchAirRatesn(terminalNumber, destPort, comCode, match);
                        for (int i = 0; i < airRatesList.size(); i++) {
                            StandardCharges aStandardCharges = (StandardCharges) airRatesList.get(i);
                            if (aStandardCharges.getComCode() != null && aStandardCharges.getComCode().getCode() != null && aStandardCharges.getComCode().getCode().equals("000000")) {
                                addCommon.add(aStandardCharges);
                                airRatesList.remove(i);
                                com = true;

                            }

                        }
                        if (!com) {
                            addCommon = standardChargesDAO.findForSearchAirRatesAction(terminalNumber, destPort, "000000", match);
                        }
                        for (int i = 0; i < addCommon.size(); i++) {
                            StandardCharges aStandardCharges = (StandardCharges) addCommon.get(i);
                            if (aStandardCharges.getComCode() != null && aStandardCharges.getComCode().getCode() != null && aStandardCharges.getComCode().getCode().equals("000000")) {
                                //Set s=aStandardCharges.getAirStandardCharges();
                                Iterator it = aStandardCharges.getAirStandardCharges().iterator();
                                while (it.hasNext()) {
                                    AirStandardCharges airStandardCharges = (AirStandardCharges) it.next();
                                    if (airStandardCharges.getStandard() != null && airStandardCharges.getStandard().equals("Y")) {
                                        commonList.add(airStandardCharges);
                                        flag = true;
                                    }
                                }

                            }
                            if (!flag) {

                                session.setAttribute("airRatescaptionCaps", "No Accessorial Add");
                            } else {

                                session.setAttribute("airRatescaptionCaps", "Common Accessorial Charges (Standard Only)");
                            }
                            break;
                        }

                    }
                    session.setAttribute("commonList", commonList);
                    session.setAttribute("noncommonList", airRatesList);
                    session.setAttribute("airRatescaption", "Air Rates List");
                    session.setAttribute("AirCollaps", "Collaps");

                }//if

                forwardName = "searchairrates";
            }//else closed
            if (buttonValue != null && buttonValue.equals("searchall")) {

                if (terminalObj != null && portObj != null) {
                    terminalNumber = terminalObj.getCode();
                    destPort = portObj.getCode();
                    addCommon = standardChargesDAO.findForSearchAirRatesAction(terminalNumber, destPort, "000000", match);
                    for (int i = 0; i < addCommon.size(); i++) {
                        StandardCharges aStandardCharges = (StandardCharges) addCommon.get(i);
                        if (aStandardCharges.getComCode() != null && aStandardCharges.getComCode().getCode() != null && aStandardCharges.getComCode().getCode().equals("000000")) {
                            //Set s=aStandardCharges.getAirStandardCharges();
                            Iterator it = aStandardCharges.getAirStandardCharges().iterator();
                            while (it.hasNext()) {
                                AirStandardCharges airStandardCharges = (AirStandardCharges) it.next();
                                commonList.add(airStandardCharges);
                            }

                        }
                    }
                    session.setAttribute("commonList", commonList);
                    session.setAttribute("airRatescaptionCaps", "Common Accessorial Charges (All)");
                }
                forwardName = "searchairrates";
            }//SEARCH ALLL
            if (buttonValue != null && buttonValue.equals("searchStarndard")) {

                if (terminalObj != null && portObj != null) {

                    terminalNumber = terminalObj.getCode();
                    destPort = portObj.getCode();
                    addCommon = standardChargesDAO.findForSearchAirRatesAction(terminalNumber, destPort, "000000", match);
                    for (int i = 0; i < addCommon.size(); i++) {
                        StandardCharges aStandardCharges = (StandardCharges) addCommon.get(i);
                        if (aStandardCharges.getComCode() != null && aStandardCharges.getComCode().getCode() != null && aStandardCharges.getComCode().getCode().equals("000000")) {
                            //Set s=aStandardCharges.getAirStandardCharges();
                            Iterator it = aStandardCharges.getAirStandardCharges().iterator();
                            while (it.hasNext()) {
                                AirStandardCharges airStandardCharges = (AirStandardCharges) it.next();
                                if (airStandardCharges.getStandard() != null && airStandardCharges.getStandard().equals("Y")) {
                                    commonList.add(airStandardCharges);
                                }
                            }

                        }
                    }
                    session.setAttribute("commonList", commonList);
                    session.setAttribute("airRatescaptionCaps", "Common Accessorial Charges (Standard Only)");
                }
                forwardName = "searchairrates";
            }
            if (buttonValue != null && buttonValue.equals("clear")) {
                if (session.getAttribute("AirCollaps") != null) {
                    session.removeAttribute("AirCollaps");
                }
                if (session.getAttribute("airRatescaption") != null) {
                    session.removeAttribute("airRatescaption");
                }
                if (session.getAttribute("airRatescaptionCaps") != null) {
                    session.removeAttribute("airRatescaptionCaps");
                }
                if (session.getAttribute("AircommonList") != null) {
                    session.removeAttribute("AircommonList");
                }
                if (session.getAttribute("noncommonList") != null) {
                    session.removeAttribute("noncommonList");
                }
                if (session.getAttribute("commonList") != null) {
                    session.removeAttribute("commonList");
                }
                if (session.getAttribute("airRatesList") != null) {
                    session.removeAttribute("airRatesList");
                }
                if (session.getAttribute("records") != null) {
                    session.removeAttribute("records");
                }
                if (session.getAttribute("manageairrates") != null) {
                    session.removeAttribute("manageairrates");
                }
                if (session.getAttribute("standardCharges") != null) {
                    session.removeAttribute("standardCharges");
                }
                if (session.getAttribute("getUpdateAss") != null) {
                    session.removeAttribute("getUpdateAss");
                }
                if (session.getAttribute("AirUpdateRecords") != null) {
                    session.removeAttribute("AirUpdateRecords");
                }
                forwardName = "searchairrates";
            }

            if (buttonValue != null && buttonValue.equals("popupsearch")) {
                if (session.getAttribute("manageairrates") != null) {
                    airRatesObj1 = (StandardCharges) session.getAttribute("manageairrates");
                } else {
                    airRatesObj1 = new StandardCharges();
                }
                if (terminalNumber != null && !terminalNumber.equals("")) {


                    List codeList = genericCodeDAO.findGenericCode("1", terminalNumber);

                    if (codeList.size() > 0) {
                        genObj = (GenericCode) codeList.get(0);
                    }
                    if (genObj != null) {
                        airRatesObj1.setOrgTerminal(genObj);
                        session.setAttribute("manageairrates", airRatesObj1);
                    }
                }

                if (terminalName != null && !terminalName.equals("")) {

                    List codeList = genericCodeDAO.findByCodedesc(terminalName);
                    if (codeList.size() > 0) {
                        genObj = (GenericCode) codeList.get(0);
                    }
                    if (genObj != null) {
                        airRatesObj1.setOrgTerminal(genObj);
                        session.setAttribute("manageairrates", airRatesObj1);
                    }

                }

                if (destPort != null && !destPort.equals("")) {

                    List portsList = genericCodeDAO.findGenericCode("1", destPort);
                    if (portsList != null && portsList.size() > 0) {

                        portObj = (GenericCode) portsList.get(0);
                        airRatesObj1.setDestPort(portObj);
                        session.setAttribute("manageairrates", airRatesObj1);
                    }
                }
                if (portName != null && !portName.equals("")) {
                    List portsList = genericCodeDAO.findByCodedesc(portName);
                    if (portsList != null && portsList.size() > 0) {

                        portObj = (GenericCode) portsList.get(0);
                        airRatesObj1.setDestPort(portObj);
                        session.setAttribute("manageairrates", airRatesObj1);
                    }
                }
                if (comCode != null && !comCode.equals("") && !comCode.equals("%")) {

                    List comList = genericCodeDAO.findForGenericCode(comCode);
                    if (comList != null && comList.size() > 0) {

                        genObj = (GenericCode) comList.get(0);

                        airRatesObj1.setComCode(genObj);
                        session.setAttribute("manageairrates", airRatesObj1);
                    }
                }

                if (comCodedesc != null && !comCodedesc.equals("")) {

                    List codeList = genericCodeDAO.findForAirRates(null, comCodedesc);
                    if (codeList != null && codeList.size() > 0) {
                        genObj = (GenericCode) codeList.get(0);
                        airRatesObj1.setComCode(genObj);
                        session.setAttribute("manageairrates", airRatesObj1);
                    }
                }

                forwardName = "searchairrates";
            }

            request.setAttribute("AirbuttonValue", buttonValue);
        }

        return mapping.findForward(forwardName);

    }
}