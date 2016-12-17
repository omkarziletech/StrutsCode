package com.gp.cong.logisoft.struts.ratemangement.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.AirRatesBean;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.RetailCommodityCharges;
import com.gp.cong.logisoft.domain.RetailFreightDocumentCharges;
import com.gp.cong.logisoft.domain.RetailOceanDetailsRates;
import com.gp.cong.logisoft.domain.RetailStandardCharges;
import com.gp.cong.logisoft.domain.RetailStandardCharges1;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.StandardChargesDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.ManageRetailRatesForm;
import com.gp.cong.logisoft.util.DBUtil;

public class ManageRetailRatesAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ManageRetailRatesForm manageRetailRatesForm = (ManageRetailRatesForm) form;// TODO
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = manageRetailRatesForm.getButtonValue();
        // form  fields value
        String portName = manageRetailRatesForm.getDestAirportname();
        String portNum = manageRetailRatesForm.getDestSheduleNumber();
        String terminalNumber = manageRetailRatesForm.getTerminalNumber();
        String tername = manageRetailRatesForm.getTerminalName();
        String comCode = manageRetailRatesForm.getComCode();
        String match = manageRetailRatesForm.getMatch();
        String commodityCode = null;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        StandardChargesDAO standardChargesDAO = new StandardChargesDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        PortsDAO portsDAO = new PortsDAO();
        String search = manageRetailRatesForm.getSearch();
        List commonList = new ArrayList();
        List noncommonList = new ArrayList();
        List retailRatesList = new ArrayList();
        AirRatesBean srBean = new AirRatesBean();
        DBUtil dbutil = new DBUtil();
        srBean.setMatch(match);
        UnLocation unLocationForTerminal = null;
        UnLocation unLocationForPorts = null;
        GenericCode genObj = null;
        RetailStandardCharges retailRatesObj1 = new RetailStandardCharges();
        RetailStandardCharges scharge = null;
        String loginName = null;
        String msg = "";
        String message = "";
        String forwardName = "";
        if (session.getAttribute("message") != null) {
            session.removeAttribute("message");
        }
        // -------------------------------------GE
        // COMBITIONATION--------------------
        if (request.getParameter("paramid") != null || (buttonValue != null && buttonValue.equals("paramid")) || (buttonValue != null && buttonValue.equals("paramidDoc"))) {

            if (buttonValue != null && buttonValue.equals("paramid")) {
                genObj = genericCodeDAO.findById(11292);
                if (genObj != null) {
                    commodityCode = genObj.getCode();
                }
                if (session.getAttribute("retailmanage") != null) {
                    List list = new ArrayList();

                    retailRatesObj1 = (RetailStandardCharges) session.getAttribute("retailmanage");
                    list = dbutil.getRetailDetails(retailRatesObj1.getOrgTerminal(), retailRatesObj1.getDestPort(),
                            commodityCode);

                    if (list != null && list.size() > 0) {
                        scharge = (RetailStandardCharges) list.get(0);
                    }
                    session.setAttribute("getRetailsAgsss", "Doc");
                }
            }
            if (buttonValue != null && buttonValue.equals("paramidDoc")) {
                genObj = genericCodeDAO.findById(11292);
                if (genObj != null) {
                    commodityCode = genObj.getCode();
                }
                if (session.getAttribute("retailmanage") != null) {
                    List list = new ArrayList();

                    retailRatesObj1 = (RetailStandardCharges) session.getAttribute("retailmanage");
                    list = dbutil.getRetailDetails(retailRatesObj1.getOrgTerminal(), retailRatesObj1.getDestPort(),
                            commodityCode);

                    if (list != null && list.size() > 0) {
                        scharge = (RetailStandardCharges) list.get(0);
                    }
                    session.setAttribute("getRetailsDoc", "Doc");
                }
            }
            if (request.getParameter("paramid") != null && !request.getParameter("paramid").equals("") && !request.getParameter("paramid").equals("null")) {
                scharge = standardChargesDAO.findById1(Integer.parseInt(request.getParameter("paramid")));

            }
            if (scharge != null) {
                session.setAttribute("retailstandardCharges", scharge);
                User userid = null;
                UserDAO user1 = new UserDAO();
                if (session.getAttribute("loginuser") != null) {
                    userid = (User) session.getAttribute("loginuser");
                }

                ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
                ProcessInfo pi = new ProcessInfo();
                String programid = null;
                programid = (String) session.getAttribute("processinfoforretailRates");
                String recordid = "";
                if (scharge.getId() != null) {
                    recordid = scharge.getId().toString();
                }

                String editstatus = "startedited";
                String deletestatus = "startdeleted";
                ProcessInfo processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus,
                        editstatus);
                if (processinfoobj != null) {

                    String view = "3";
                    User loginuser = user1.findById(processinfoobj.getUserid());
                    loginName = loginuser.getLoginName();
                    msg = "This Record Is Being Used By ";
                    message = msg + loginName;
                    session.setAttribute("message", message);
                    session.setAttribute("view", view);
                    forwardName = "retailRatesFrame";
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
                session.setAttribute("retaileditrecord", "edit");
                session.setAttribute("retailstandardCharges", scharge);
                forwardName = "retailRatesFrame";
                if (session.getAttribute("retailsetTabEnable") != null) {
                    session.removeAttribute("retailsetTabEnable");
                }
                // ------------------------------------
                if (request.getParameter("ind") != null) {
                    RetailStandardCharges1 retailStandard = new RetailStandardCharges1();
                    int ind = Integer.parseInt(request.getParameter("ind"));
                    List codeList1 = (List) session.getAttribute("recommonList");
                    retailStandard = (RetailStandardCharges1) codeList1.get(ind);
                    session.setAttribute("retailStandardCharges",
                            retailStandard);
                    session.setAttribute("addaccess", "addaccess");
                // forwardName="agssEdit";

                }
                /*String defaultRate = "";
                if (session.getAttribute("retaildefaultRate") != null) {
                session.removeAttribute("retaildefaultRate");
                }
                portsDAO = new PortsDAO();
                Ports portstt = portsDAO
                .findById(scharge.getDestPort().getId());
                if (portstt.getLclPortConfigSet() != null) {
                Iterator iter = portstt.getLclPortConfigSet().iterator();
                while (iter.hasNext()) {
                LCLPortConfiguration lcl = (LCLPortConfiguration) iter
                .next();
                if (lcl.getDefaultRate() != null) {
                defaultRate = lcl.getDefaultRate();
                break;
                }
                }
                session.setAttribute("retaildefaultRate", defaultRate);
                }*/
                if (scharge.getComCode() != null && scharge.getComCode() != null) {

                    if (scharge.getComCode().trim().equals("000000")) {
                        session.setAttribute("retailsetTabEnable",
                                "retailenable");
                    }
                }
                List retailStdList = new ArrayList();
                List retailStdList1 = new ArrayList();
                List retailStdList2 = new ArrayList();
                List retailStdList3 = new ArrayList();
                List retailStdList4 = new ArrayList();

                if (scharge.getRetailStandardCharges() != null) {
                    Iterator iter = (Iterator) scharge.getRetailStandardCharges().iterator();
                    while (iter.hasNext()) {
                        RetailStandardCharges1 retailStd = (RetailStandardCharges1) iter.next();
                        retailStdList.add(retailStd);
                    // session.setAttribute("retailStandardCharges",
                    // retailStd);
                    }
                }

                if (scharge.getRetailDocumentCharges() != null) {
                    Iterator iter = (Iterator) scharge.getRetailDocumentCharges().iterator();
                    while (iter.hasNext()) {
                        RetailFreightDocumentCharges retaildoc = (RetailFreightDocumentCharges) iter.next();
                        retailStdList1.add(retaildoc);
                        session.setAttribute("retaildocumentCharges",
                                retaildoc);
                    }
                }

                if (scharge.getRetailWeightRangeSet() != null) {
                    Iterator iter = (Iterator) scharge.getRetailWeightRangeSet().iterator();
                    while (iter.hasNext()) {
                        RetailOceanDetailsRates retailff = (RetailOceanDetailsRates) iter.next();
                        retailStdList3.add(retailff);
                        session.setAttribute("retailDetails", retailff);

                    }
                }
                if (scharge.getRetailCommoditySet() != null) {
                    Iterator iter = (Iterator) scharge.getRetailCommoditySet().iterator();
                    while (iter.hasNext()) {
                        RetailCommodityCharges retailff = (RetailCommodityCharges) iter.next();
                        retailStdList4.add(retailff);
                        session.setAttribute("retailCommodityCharges",
                                retailff);
                    }
                }
                session.setAttribute("retailcssList", retailStdList4);
                session.setAttribute("retaildetailsAdd", retailStdList3);

                session.setAttribute("retaildocChargesAdd", retailStdList1);
                session.setAttribute("retailagssAdd", retailStdList);
                session.setAttribute("retailstandardCharges", scharge);
//				 set Origin name,destination name,commidity name
                if (scharge.getOrgTerminal() != null && !scharge.getOrgTerminal().equals("")) {
                    List unLocationList = unLocationDAO.findForManagement(scharge.getOrgTerminal(), null);
                    if (unLocationList != null && unLocationList.size() > 0) {
                        unLocationForTerminal = (UnLocation) unLocationList.get(0);
                        scharge.setOrgTerminalName(unLocationForTerminal.getUnLocationName());
                    }
                }
                if (scharge.getDestPort() != null && !scharge.getDestPort().equals("")) {
                    List portsList = unLocationDAO.findForManagement(scharge.getDestPort(), null);
                    if (portsList != null && portsList.size() > 0) {
                        unLocationForPorts = (UnLocation) portsList.get(0);
                        scharge.setDestPortName(unLocationForPorts.getUnLocationName());
                    }
                }
                if (scharge.getComCode() != null && !scharge.getComCode().equals("")) {
                    List comList = genericCodeDAO.findForGenericCode(scharge.getComCode());
                    if (comList != null && comList.size() > 0) {
                        genObj = (GenericCode) comList.get(0);
                        scharge.setComCodeName(genObj.getCodedesc());
                    }
                }

                if (session.getAttribute("retailapplyGeneralStandardList") != null) {
                    session.removeAttribute("retailapplyGeneralStandardList");
                }
                if (session.getAttribute("retailincludedList") != null) {
                    session.removeAttribute("retailincludedList");
                }
                if (session.getAttribute("retailStandardCharges") != null) {
                    // session.removeAttribute("retailStandardCharges");
                }
                if (session.getAttribute("retaildocumentCharges") != null) {
                    session.removeAttribute("retaildocumentCharges");
                }
                if (session.getAttribute("retailCommodityCharges") != null) {
                    session.removeAttribute("retailCommodityCharges");
                }
                if (session.getAttribute("url") != null) {
                    session.removeAttribute("url");
                }

                if (buttonValue == null) {

                    session.setAttribute("removebutton", "removebutton");
                }
            }

            forwardName = "retailRatesFrame";
        } // code for edit ends
        else if (request.getParameter("param") != null) {
            session.setAttribute("retaileditrecord", "edit");
            scharge = standardChargesDAO.findById1(Integer.parseInt(request.getParameter("param")));
            // set Origin name,destination name,commidity name
            if (scharge.getOrgTerminal() != null && !scharge.getOrgTerminal().equals("")) {
                List unLocationList = unLocationDAO.findForManagement(scharge.getOrgTerminal(), null);
                if (unLocationList != null && unLocationList.size() > 0) {
                    unLocationForTerminal = (UnLocation) unLocationList.get(0);
                    scharge.setOrgTerminalName(unLocationForTerminal.getUnLocationName());
                }
            }
            if (scharge.getDestPort() != null && !scharge.getDestPort().equals("")) {
                List portsList = unLocationDAO.findForManagement(scharge.getDestPort(), null);
                if (portsList != null && portsList.size() > 0) {
                    unLocationForPorts = (UnLocation) portsList.get(0);
                    scharge.setDestPortName(unLocationForPorts.getUnLocationName());
                }
            }
            if (scharge.getComCode() != null && !scharge.getComCode().equals("")) {
                List comList = genericCodeDAO.findForGenericCode(scharge.getComCode());
                if (comList != null && comList.size() > 0) {
                    genObj = (GenericCode) comList.get(0);
                    scharge.setComCodeName(genObj.getCodedesc());
                }
            }

            if (request.getParameter("ind") != null) {
                if (session.getAttribute("retailStandardCharges") != null) {
                    session.removeAttribute("retailStandardCharges");
                }
                RetailStandardCharges1 retailStandard = new RetailStandardCharges1();
                int ind = Integer.parseInt(request.getParameter("ind"));
                List codeList1 = (List) session.getAttribute("recommonList");
                retailStandard = (RetailStandardCharges1) codeList1.get(ind);
                session.setAttribute("retailStandardCharges", retailStandard);

                // forwardName="agssEdit";
                session.setAttribute("addaccess", "addaccess");
            }
            String view = "3";
            session.setAttribute("view", view);
            session.setAttribute("retailstandardCharges", scharge);

            if (session.getAttribute("retailsetTabEnable") != null) {
                session.removeAttribute("retailsetTabEnable");
            }
            /*
             * need to used later
             * String defaultRate = "";
            if (session.getAttribute("retaildefaultRate") != null) {
            session.removeAttribute("retaildefaultRate");
            }
            portsDAO = new PortsDAO();
            Ports portsttemp = portsDAO.findById(scharge.getDestPort().getId());
            if (portsttemp.getLclPortConfigSet() != null) {
            Iterator iter = portsttemp.getLclPortConfigSet().iterator();
            while (iter.hasNext()) {
            LCLPortConfiguration lcl = (LCLPortConfiguration) iter
            .next();
            if (lcl.getDefaultRate() != null) {
            defaultRate = lcl.getDefaultRate();
            session.setAttribute("retaildefaultRate", defaultRate);
            break;
            }
            }
            }*/
            if (scharge.getComCode() != null && scharge.getComCode() != null) {

                if (scharge.getComCode().equals("000000")) {
                    session.setAttribute("retailsetTabEnable", "retailenable");
                }
            }

            List retailStdList = new ArrayList();
            List retailStdList1 = new ArrayList();
            List retailStdList2 = new ArrayList();
            List retailStdList3 = new ArrayList();
            List retailStdList4 = new ArrayList();

            if (scharge.getRetailStandardCharges() != null) {
                Iterator iter = (Iterator) scharge.getRetailStandardCharges().iterator();
                while (iter.hasNext()) {
                    RetailStandardCharges1 retailStd = (RetailStandardCharges1) iter.next();
                    retailStdList.add(retailStd);
                // session.setAttribute("retailStandardCharges",retailStd );
                }
            }

            if (scharge.getRetailDocumentCharges() != null) {
                Iterator iter = (Iterator) scharge.getRetailDocumentCharges().iterator();
                while (iter.hasNext()) {
                    RetailFreightDocumentCharges retaildoc = (RetailFreightDocumentCharges) iter.next();
                    retailStdList1.add(retaildoc);
                    session.setAttribute("retaildocumentCharges", retaildoc);
                }
            }

            if (scharge.getRetailWeightRangeSet() != null) {
                Iterator iter = (Iterator) scharge.getRetailWeightRangeSet().iterator();
                while (iter.hasNext()) {
                    RetailOceanDetailsRates retailff = (RetailOceanDetailsRates) iter.next();
                    retailStdList3.add(retailff);
                    session.setAttribute("retailDetails", retailff);
                }
            }
            if (scharge.getRetailCommoditySet() != null) {
                Iterator iter = (Iterator) scharge.getRetailCommoditySet().iterator();
                while (iter.hasNext()) {
                    RetailCommodityCharges retailff = (RetailCommodityCharges) iter.next();
                    retailStdList4.add(retailff);
                    session.setAttribute("retailCommodityCharges", retailff);
                }
            }
            session.setAttribute("retailcssList", retailStdList4);
            session.setAttribute("retaildetailsAdd", retailStdList3);

            session.setAttribute("retaildocChargesAdd", retailStdList1);
            session.setAttribute("retailagssAdd", retailStdList);
            if (session.getAttribute("retailCommodityCharges") != null) {
                session.removeAttribute("retailCommodityCharges");
            }
            if (session.getAttribute("retailStandardCharges") != null) {
                // session.removeAttribute("retailStandardCharges");
            }
            if (session.getAttribute("retaildocumentCharges") != null) {
                session.removeAttribute("retaildocumentCharges");
            }
            if (session.getAttribute("url") != null) {
                session.removeAttribute("url");
            }

            session.setAttribute("removebutton", "removebutton");
            forwardName = "retailRatesFrame";
        }// code for More Info ends
        // delete standard charges
        else if (request.getParameter("standardId") != null && !request.getParameter("standardId").trim().equals("")) {
            if (request.getParameter("parentId") != null && !request.getParameter("parentId").equals("")) {
                if (session.getAttribute("recommonList") != null) {
                    session.removeAttribute("recommonList");

                }
                int standardid = Integer.parseInt(request.getParameter("standardId"));
                scharge = standardChargesDAO.findById1(Integer.parseInt(request.getParameter("parentId")));
                Set<RetailStandardCharges1> setRetailStandardCharges1 = new HashSet();
                setRetailStandardCharges1.addAll(scharge.getRetailStandardCharges());
                for (Iterator iter = setRetailStandardCharges1.iterator(); iter.hasNext();) {
                    RetailStandardCharges1 retailStandardCharges1 = (RetailStandardCharges1) iter.next();
                    if (retailStandardCharges1.getRetailStdId() != null && retailStandardCharges1.getRetailStdId().equals(standardid)) {
                        scharge.getRetailStandardCharges().remove(retailStandardCharges1);
                    }

                }
                forwardName = "searchretailrates";
                List commonStandardList = new ArrayList();
                commonStandardList.addAll(scharge.getRetailStandardCharges());
                session.setAttribute("recommonList", commonStandardList);
            }

        } //------------------------------
        else if (buttonValue != null && buttonValue.equals("search")) {
            if (session.getAttribute("retmessage") != null) {
                session.removeAttribute("retmessage");
            }

            if (terminalNumber != null && !terminalNumber.equals("")) {
                List unLocationList = unLocationDAO.findForManagement(terminalNumber, tername);
                if (unLocationList != null && unLocationList.size() > 0) {
                    unLocationForTerminal = (UnLocation) unLocationList.get(0);
                    retailRatesObj1.setOrgTerminal(unLocationForTerminal.getUnLocationCode());
                    retailRatesObj1.setOrgTerminalName(unLocationForTerminal.getUnLocationName());
                }
            } else if (tername != null && !tername.equals("")) {
                String stringTokens[] = StringUtils.splitPreserveAllTokens(tername, '-');
                if (stringTokens != null && stringTokens.length > 1) {
                    tername = stringTokens[0];
                    terminalNumber = stringTokens[1];
                }
                List terminal = unLocationDAO.findForManagement(terminalNumber, tername);
                if (terminal.size() > 0) {
                    unLocationForTerminal = (UnLocation) terminal.get(0);
                }
                if (unLocationForTerminal != null) {
                    retailRatesObj1.setOrgTerminal(unLocationForTerminal.getUnLocationCode());
                }
            }
            if (portNum != null && !portNum.equals("")) {
                List portsList = unLocationDAO.findForManagement(portNum, null);
                if (portsList != null && portsList.size() > 0) {
                    unLocationForPorts = (UnLocation) portsList.get(0);
                    retailRatesObj1.setDestPort(unLocationForPorts.getUnLocationCode());
                    retailRatesObj1.setDestPortName(unLocationForPorts.getUnLocationName());
                }
            } else if (portName != null && !portName.equals("")) {
                String stringTokens[] = StringUtils.splitPreserveAllTokens(portName, '-');
                if (stringTokens != null && stringTokens.length > 1) {
                    portName = stringTokens[0];
                    portNum = stringTokens[1];
                }
                List portsList = unLocationDAO.findForManagement(portNum, portName);
                if (portsList != null && portsList.size() > 0) {
                    unLocationForPorts = (UnLocation) portsList.get(0);
                    retailRatesObj1.setDestPort(unLocationForPorts.getUnLocationCode());
                }
            }
            if (comCode != null && !comCode.equals("") && !comCode.equals("%")) {
                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    genObj = (GenericCode) comList.get(0);
                    retailRatesObj1.setComCode(genObj.getCode());
                    retailRatesObj1.setComCodeName(genObj.getCodedesc());
                    session.setAttribute("retailmanage", retailRatesObj1);
                    if (genObj.getCode().equals("000000")) {
                        session.setAttribute("retailenable", "enable");
                    } else {
                        session.removeAttribute("retailenable");
                    }
                } else {
                    request.setAttribute("message",
                            "Please enter proper Commodity Code");
                }
            }
            session.setAttribute("retailmanage", retailRatesObj1);
            if (request.getAttribute("message") == null && (search != null && search.equals("get"))) {
                if (session.getAttribute("recommonList") != null) {
                    session.removeAttribute("recommonList");
                }
                if (session.getAttribute("rerecords") != null) {
                    session.removeAttribute("rerecords");
                }
                if (session.getAttribute("renoncommonList") != null) {
                    session.removeAttribute("renoncommonList");
                }
                if (session.getAttribute("retailRatesList") != null) {
                    session.removeAttribute("retailRatesList");
                }
                if ((unLocationForTerminal != null && unLocationForPorts != null) || genObj != null) {
                    if (session.getAttribute("reatilcommonList") != null) {
                        session.removeAttribute("reatilcommonList");
                    }
                    if (session.getAttribute("RetailRateCaption") != null) {
                        session.removeAttribute("RetailRateCaption");
                    }
                    if (session.getAttribute("retailsetTabEnable") != null) {
                        session.removeAttribute("retailsetTabEnable");
                    }
                    String defaultRate = "";
                    if (session.getAttribute("serachretaildefaultRate") != null) {
                        session.removeAttribute("serachretaildefaultRate");
                    }
                    // need to get clearification about port, (Matric or enlgish)
					/*if (ports != null) {
                    Ports portstt = portsDAO.findById(ports.getId());
                    + ports.getPortname());
                    if (portstt.getLclPortConfigSet() != null) {
                    Iterator iter = portstt.getLclPortConfigSet()
                    .iterator();
                    while (iter.hasNext()) {
                    LCLPortConfiguration lcl = (LCLPortConfiguration) iter
                    .next();
                    if (lcl.getDefaultRate() != null) {
                    defaultRate = lcl.getDefaultRate();
                    session.setAttribute(
                    "serachretaildefaultRate",
                    defaultRate);
                    break;
                    }
                    }
                    session.setAttribute("serachretaildefaultRate",
                    defaultRate);
                    }
                    }*/
                    if (terminalNumber != null && portNum != null && (comCode == null || comCode.equals(""))) {
                        retailRatesList = standardChargesDAO.findForSearchRetailRatesAction(terminalNumber,
                                portNum, comCode, match);

                        for (int i = 0; i < retailRatesList.size(); i++) {
                            RetailStandardCharges aStandardChargescom = (RetailStandardCharges) retailRatesList.get(i);
                            if (aStandardChargescom.getComCode() != null) {
                                if (aStandardChargescom.getComCode().equals("000000")) {
                                    comCode = "000000";
                                }
                            }
                        }
                        if (terminalNumber != null && portNum != null && comCode != null) {
                            if (comCode != null && !comCode.equals("") && !comCode.equals("000000")) {
                                genObj = genericCodeDAO.findById(11292);
                                if (genObj != null) {
                                    commodityCode = genObj.getCode();
                                }
                                List retailRatesListSecond = standardChargesDAO.getRecordsForCommRetail(terminalNumber,
                                        portNum, comCode);
                                for (int i = 0; i < retailRatesListSecond.size(); i++) {
                                    RetailStandardCharges aStandardCharges1 = (RetailStandardCharges) retailRatesListSecond.get(i);
                                    retailRatesList.add(aStandardCharges1);
                                }
                            }
                        }
                        for (int i = 0; i < retailRatesList.size(); i++) {
                            RetailStandardCharges retailRatesObject = (RetailStandardCharges) retailRatesList.get(i);
                            if (retailRatesObject.getComCode() != null && retailRatesObject.getComCode() != null && retailRatesObject.getComCode().equals("000000")) {
                                RetailStandardCharges1 retailStdChild = new RetailStandardCharges1();// for
                                // child
                                // records
                                boolean flag = false;
                                if (retailRatesObject.getRetailStandardCharges() != null) {
                                    Iterator iter = (Iterator) retailRatesObject.getRetailStandardCharges().iterator();
                                    while (iter.hasNext()) {
                                        retailStdChild = (RetailStandardCharges1) iter.next();
                                        if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals("Y")) {
                                            commonList.add(retailStdChild);
                                        }
                                        flag = true;
                                    }
                                }
                                if (!flag) {
                                    session.setAttribute("reatilcommonList",
                                            "No Accessorial Add");
                                } else {
                                    session.setAttribute("reatilcommonList",
                                            "Common Accessorial Charges (Standard Only)");
                                }
                                request.setAttribute("common", "common");
                                session.setAttribute("recommonList",
                                        commonList);
                                session.setAttribute("rerecords", "getRecords");

                            }// if
                            else {
                                noncommonList.add(retailRatesObject);
                                request.setAttribute("noncommon", "noncommon");
                                session.setAttribute("renoncommonList",
                                        noncommonList);

                            }// else

                        }// for

                        session.setAttribute("RetailRateCaption",
                                "Ocean Freight Rates");

                    } else if (terminalNumber != null && portNum != null && comCode != null) {
                        retailRatesList = standardChargesDAO.findForSearchRetailRatesn(terminalNumber,
                                portNum, comCode, match);
                        if (terminalNumber != null && portNum != null && comCode != null) {
                            if (comCode != null && !comCode.equals("") && !comCode.equals("000000")) {
                                genObj = genericCodeDAO.findById(11292);
                                List lii = standardChargesDAO.getRecordsForCommRetail(terminalNumber,
                                        portNum, comCode);
                                for (int i = 0; i < lii.size(); i++) {
                                    RetailStandardCharges aStandardCharges1 = (RetailStandardCharges) lii.get(i);
                                    retailRatesList.add(aStandardCharges1);
                                }// for
                            }// if
                        }// if
                        for (int i = 0; i < retailRatesList.size(); i++) {
                            RetailStandardCharges1 retailStdChild = new RetailStandardCharges1();
                            RetailStandardCharges retailRatesObject = (RetailStandardCharges) retailRatesList.get(i);
                            if (retailRatesObject.getComCode() != null && retailRatesObject.getComCode() != null && retailRatesObject.getComCode().equals("000000")) {
                                boolean flag = false;
                                if (retailRatesObject.getRetailStandardCharges() != null) {
                                    Iterator iter = (Iterator) retailRatesObject.getRetailStandardCharges().iterator();
                                    while (iter.hasNext()) {
                                        retailStdChild = (RetailStandardCharges1) iter.next();
                                        if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals("Y")) {
                                            commonList.add(retailStdChild);
                                        }// if
                                        flag = true;
                                    }// while
                                    session.setAttribute("reatilcommonList",
                                            "Common Accessorial Charges (Standard Only)");
                                }// if
                                // commonList.add(retailRatesObject);
                                request.setAttribute("common", "common");
                                session.setAttribute("recommonList",
                                        commonList);
                                session.setAttribute("rerecords", "getRecords");
                            }// if
                            else {
                                noncommonList.add(retailRatesObject);
                                request.setAttribute("noncommon", "noncommon");
                                session.setAttribute("renoncommonList",
                                        noncommonList);
                            }

                        }// for
                        session.setAttribute("RetailRateCaption",
                                "Ocean Freight Rates");
                    }// else

                }
                session.setAttribute("collaps", "Collaps");
            }
            session.setAttribute("retailstandardCharges", retailRatesObj1);

            forwardName = "searchretailrates";

        }// code for search ends
        else if (buttonValue != null && buttonValue.equals("searchall"))// --------CODE
        // FOR
        // 'SEARCH
        // ALL'
        // starts--------
        {
            if (session.getAttribute("reatilcommonList") != null) {

                session.removeAttribute("reatilcommonList");
            }
            if (session.getAttribute("RetailRateCaption") != null) {

                session.removeAttribute("RetailRateCaption");
            }
            request.setAttribute("collaps", "Collaps");

            if (session.getAttribute("addaccess") != null) {
                session.removeAttribute("addaccess");
            }
            if (session.getAttribute("retailmanage") != null) {
                retailRatesObj1 = (RetailStandardCharges) session.getAttribute("retailmanage");
                if (retailRatesObj1.getOrgTerminal() != null) {
                    terminalNumber = retailRatesObj1.getOrgTerminal();

                }
                if (retailRatesObj1.getDestPort() != null) {
                    portNum = retailRatesObj1.getDestPort();
                }
                if (retailRatesObj1.getComCode() != null) {
                    comCode = retailRatesObj1.getComCode();
                }
            }
            if (terminalNumber != null && portNum != null) {
                genObj = genericCodeDAO.findById(11292);
                retailRatesList = standardChargesDAO.findForSearchRetailRatesMatchOnly(terminalNumber,
                        portNum, "000000", match);
                for (int i = 0; i < retailRatesList.size(); i++) {
                    RetailStandardCharges1 retailStdChild = new RetailStandardCharges1();
                    RetailStandardCharges retailRatesObject = (RetailStandardCharges) retailRatesList.get(i);
                    if (retailRatesObject.getComCode() != null && retailRatesObject.getComCode() != null && retailRatesObject.getComCode().equals(
                            "000000")) {
                        if (retailRatesObject.getRetailStandardCharges() != null) {
                            Iterator iter = (Iterator) retailRatesObject.getRetailStandardCharges().iterator();
                            while (iter.hasNext()) {
                                retailStdChild = (RetailStandardCharges1) iter.next();
                                commonList.add(retailStdChild);
                            }
                        }
                        request.setAttribute("common", "common");
                        session.setAttribute("recommonList", commonList);
                        session.setAttribute("reatilcommonList",
                                "Common Accessorial Charges (All)");

                    } else {
                        // noncommonList.add(retailRatesObject);
                        // request.setAttribute("noncommon","noncommon");
                        // session.setAttribute("renoncommonList",noncommonList);
                    }

                }

            }
            forwardName = "searchretailrates";

        } // BUTTON VALUE IS searchStarndard
        else if (buttonValue != null && buttonValue.equals("searchStarndard"))// --------CODE
        // FOR
        // 'SEARCH
        // ALL'
        // starts--------
        {
            if (session.getAttribute("reatilcommonList") != null) {
                session.removeAttribute("reatilcommonList");
            }
            if (session.getAttribute("RetailRateCaption") != null) {
                session.removeAttribute("RetailRateCaption");
            }
            if (session.getAttribute("recommonList") != null) {
                session.removeAttribute("recommonList");
            }
            if (session.getAttribute("retailmanage") != null) {
                retailRatesObj1 = (RetailStandardCharges) session.getAttribute("retailmanage");
                if (retailRatesObj1.getOrgTerminal() != null) {
                    terminalNumber = retailRatesObj1.getOrgTerminal();

                }
                if (retailRatesObj1.getDestPort() != null) {
                    portNum = retailRatesObj1.getDestPort();
                }
                if (retailRatesObj1.getComCode() != null) {
                    comCode = retailRatesObj1.getComCode();
                }
            }
            if (terminalNumber != null && portNum != null) {
                retailRatesList = standardChargesDAO.findForSearchRetailRatesMatchOnly(terminalNumber,
                        portNum, "000000", match);
                for (int i = 0; i < retailRatesList.size(); i++) {
                    RetailStandardCharges1 retailStdChild = new RetailStandardCharges1();
                    RetailStandardCharges retailRatesObject = (RetailStandardCharges) retailRatesList.get(i);
                    if (retailRatesObject.getComCode() != null && retailRatesObject.getComCode() != null && retailRatesObject.getComCode().equals("000000")) {
                        if (retailRatesObject.getRetailStandardCharges() != null) {
                            Iterator iter = (Iterator) retailRatesObject.getRetailStandardCharges().iterator();
                            while (iter.hasNext()) {
                                retailStdChild = (RetailStandardCharges1) iter.next();
                                if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals("Y")) {
                                    commonList.add(retailStdChild);
                                }
                            }
                        }
                        request.setAttribute("common", "common");
                        session.setAttribute("recommonList", commonList);
                        session.setAttribute("rerecords", "getRecords");
                    } else {
                    }
                }
                session.setAttribute("RetailRateCaption", "Ocean Freight Rates");
                session.setAttribute("reatilcommonList", "Common Accessorial Charges (Standard Only)");
            }
            forwardName = "searchretailrates";
        } // --------------------------------------BUTTON
        // CLEAR--------------------------------------------
        else if (buttonValue != null && buttonValue.equals("clear"))// ------CODE
        {
            if (session.getAttribute("collaps") != null) {
                session.removeAttribute("collaps");
            }

            if (session.getAttribute("getchangedcolore") != null) {
                session.removeAttribute("getchangedcolore");
            }

            if (session.getAttribute("recommonList") != null) {
                session.removeAttribute("recommonList");
            }
            if (session.getAttribute("renoncommonList") != null) {
                session.removeAttribute("renoncommonList");
            }
            if (session.getAttribute("retailstandardCharges") != null) {
                session.removeAttribute("retailstandardCharges");
            }
            if (session.getAttribute("retailmanage") != null) {
                session.removeAttribute("retailmanage");
            }
            if (session.getAttribute("retmessage") != null) {
                session.removeAttribute("retmessage");
            }
            if (session.getAttribute("retailRatesList") != null) {
                session.removeAttribute("retailRatesList");
            }
            if (session.getAttribute("reatilcommonList") != null) {
                session.removeAttribute("reatilcommonList");
            }
            if (session.getAttribute("RetailRateCaption") != null) {
                session.removeAttribute("RetailRateCaption");
            }
            forwardName = "searchretailrates";
        }// ---clearscreen code ends-----
        else if (buttonValue != null && buttonValue.equals("standardpopup"))// ------CODE
        {

            String defaultRate = "";
            if (session.getAttribute("retaildefaultRate") != null) {
                session.removeAttribute("retaildefaultRate");
            }
            portsDAO = new PortsDAO();
            if (session.getAttribute("retailmanage") != null) {
                /*scharge = (RetailStandardCharges) session
                .getAttribute("retailmanage");
                //Ports portsget = portsDAO.findById(scharge.getDestPort()
                //.getId());
                if (portsget.getLclPortConfigSet() != null) {
                Iterator iter = portsget.getLclPortConfigSet().iterator();
                while (iter.hasNext()) {
                LCLPortConfiguration lcl = (LCLPortConfiguration) iter
                .next();
                if (lcl.getDefaultRate() != null) {
                defaultRate = lcl.getDefaultRate();
                // session.setAttribute("retaildefaultRate",defaultRate);
                break;
                }
                }
                session.setAttribute("retaildefaultRate", defaultRate);*/
            }
            genObj = genericCodeDAO.findById(11292);
            if (genObj != null) {
                commodityCode = genObj.getCode();
            }
            List li = standardChargesDAO.findAllDetails1(scharge.getOrgTerminal(), scharge.getDestPort(), commodityCode);
            RetailStandardCharges getSS = new RetailStandardCharges();
            for (int i = 0; i < li.size(); i++) {
                getSS = (RetailStandardCharges) li.get(i);
            }
            List retailStdList = new ArrayList();
            if (getSS.getRetailStandardCharges() != null) {
                Iterator iter = (Iterator) getSS.getRetailStandardCharges().iterator();
                while (iter.hasNext()) {
                    RetailStandardCharges1 retailStd = (RetailStandardCharges1) iter.next();
                    retailStdList.add(retailStd);

                }
                session.setAttribute("retailagssAdd", retailStdList);
            }
            forwardName = "searchretailrates";
        }

        request.setAttribute("buttonValue", buttonValue);
        session.getAttribute("retailDetails");
        return mapping.findForward(forwardName);
    }
}