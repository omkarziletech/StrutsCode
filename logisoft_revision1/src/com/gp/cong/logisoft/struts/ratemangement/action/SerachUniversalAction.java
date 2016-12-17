package com.gp.cong.logisoft.struts.ratemangement.action;

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

import com.gp.cong.logisoft.beans.AirRatesBean;
import com.gp.cong.logisoft.domain.GenericCode;


import com.gp.cong.logisoft.domain.LCLPortConfiguration;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.UniversalMaster;
import com.gp.cong.logisoft.domain.UniverseAirFreight;
import com.gp.cong.logisoft.domain.UniverseCommodityChrg;
import com.gp.cong.logisoft.domain.UniverseFlatRate;
import com.gp.cong.logisoft.domain.UniverseInsuranceChrg;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UniversalMasterDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.SerachUniversalForm;

public class SerachUniversalAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        SerachUniversalForm searchLCLColoadForm = (SerachUniversalForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = searchLCLColoadForm.getButtonValue();
        String match = searchLCLColoadForm.getMatch();
        String common = searchLCLColoadForm.getCommon();
        String search = searchLCLColoadForm.getSearch();
        String defaultRate = "";
        String get = searchLCLColoadForm.getIndex();
        PortsDAO portDAO = new PortsDAO();
        List commonList = new ArrayList();
        List noncommonList = new ArrayList();
        RefTerminalTemp refObj = null;
        RefTerminalTemp refTerminal1 = null;
        PortsTemp destObj = null;
        GenericCode comObj = null;
        GenericCodeDAO genericCodeDAO1 = new GenericCodeDAO();
        String loginName = "";
        String message = "";
        String msg = "";
        String forwardName = "";
        List airStdList = new ArrayList();
        List airStdList1 = new ArrayList();
        List airStdList2 = new ArrayList();
        List airStdList3 = new ArrayList();
        List airStdList4 = new ArrayList();
        String terminalNumber = searchLCLColoadForm.getTerminalNumber();
        String tername = searchLCLColoadForm.getTerminalName();
        String destPort = searchLCLColoadForm.getDestSheduleNumber();
        String comCode = searchLCLColoadForm.getComCode();
        String portName = searchLCLColoadForm.getDestAirportname();
        String comCodeDesc = searchLCLColoadForm.getComDescription();
        AirRatesBean airRatesBean = new AirRatesBean();
        UniversalMasterDAO universalMasterDAO = new UniversalMasterDAO();
        RefTerminalDAO refTerminalDAO1 = new RefTerminalDAO();
        UniversalMaster universalMaster = new UniversalMaster();
        airRatesBean.setCommon(common);
        request.setAttribute("airRatesBean", airRatesBean);

        if (buttonValue != null && buttonValue.equals("search")) {
            if (session.getAttribute("airRatesBean") != null) {
                session.removeAttribute("airRatesBean");

            }
            airRatesBean.setMatch(match);
            airRatesBean.setCommon(common);
            List searchLCLColoadList = new ArrayList();
            if (session.getAttribute("searchuniMaster") != null) {
                universalMaster = (UniversalMaster) session.getAttribute("searchuniMaster");
                if (universalMaster.getOriginTerminal() != null) {
                    refObj = universalMaster.getOriginTerminal();
                }
                if (universalMaster.getDestinationPort() != null) {
                    destObj = universalMaster.getDestinationPort();
                }
                if (universalMaster.getCommodityCode() != null) {
                    comObj = universalMaster.getCommodityCode();
                /*if(comObj.getCode().equals("000000"))
                {
                airRatesBean.setCommon("common");
                }*/
                }
            } else {
                universalMaster = new UniversalMaster();
            }
            RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
            if (terminalNumber != null && !terminalNumber.equals("")) {
                RefTerminalTemp refTerminal = refTerminalDAO.findById1(terminalNumber);
                if (refTerminal != null) {
                    universalMaster.setOriginTerminal(refTerminal);
                    session.setAttribute("searchuniMaster", universalMaster);
                } else {
                    //request.setAttribute("message", "Please enter proper terminal number");
                }
            } else {
                universalMaster.setOriginTerminal(null);

            }

            PortsDAO portsDAO = new PortsDAO();
            if (destPort != null && !destPort.equals("")) {
                List portsList = portsDAO.findports(destPort);
                if (portsList != null && portsList.size() > 0) {
                    PortsTemp ports = (PortsTemp) portsList.get(0);
                    universalMaster.setDestinationPort(ports);
                    session.setAttribute("searchuniMaster", universalMaster);
                } else {
                    //request.setAttribute("message", "Please enter proper Destination Port");
                }
            } else {
                universalMaster.setDestinationPort(null);

            }

            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            if (comCode != null && !comCode.equals("")) {
                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    GenericCode gen = (GenericCode) comList.get(0);
                    universalMaster.setCommodityCode(gen);
                    session.setAttribute("searchuniMaster", universalMaster);

                } else {
                    //request.setAttribute("message", "Please enter proper Commodity Code");
                }
            } else {
                universalMaster.setCommodityCode(null);

            }

            refObj = universalMaster.getOriginTerminal();
            destObj = universalMaster.getDestinationPort();
            comObj = universalMaster.getCommodityCode();

            //session.setAttribute("lCLColoadMaster",lCLColoadMaster);

            //session.setAttribute("addlclColoadMaster", lCLColoadMaster);

            if (request.getAttribute("message") == null && (search != null && search.equals("get"))) {
                if (session.getAttribute("uninoncommonList") != null) {
                    session.removeAttribute("uninoncommonList");
                }

                if (session.getAttribute("universaldefaultRate") != null) {
                    session.removeAttribute("universaldefaultRate");
                }
                portsDAO = new PortsDAO();
                if (destObj != null) {
                    Ports portstt = portsDAO.findById(destObj.getId());
                    if (portstt.getLclPortConfigSet() != null) {
                        Iterator iter = portstt.getLclPortConfigSet().iterator();
                        while (iter.hasNext()) {
                            LCLPortConfiguration lcl = (LCLPortConfiguration) iter.next();
                            if (lcl.getDefaultRate() != null) {
                                defaultRate = lcl.getDefaultRate();

                                break;
                            }
                        }
                        session.setAttribute("universaldefaultRate", defaultRate);
                    }
                }
                if (terminalNumber != null && destPort != null && (comCode == null || comCode.equals(""))) {
                    boolean b = false;
                    searchLCLColoadList = universalMasterDAO.findForSearchUniRatesmatch(terminalNumber, destPort, comCode, match);
                    session.setAttribute("uninoncommonList", searchLCLColoadList);
                    session.setAttribute("nonunivercommonList", "Ocean Freight Rates");

                } else if (terminalNumber != null && destPort != null && comCode != null && !comCode.equals("")) {
                    searchLCLColoadList = universalMasterDAO.findForSearchUniRatesstarts(terminalNumber, destPort, comCode, null);
                    session.setAttribute("uninoncommonList", searchLCLColoadList);
                    session.setAttribute("nonunivercommonList", "Ocean Freight Rates");

                }


                if (session.getAttribute("message") != null) {
                    session.removeAttribute("message");

                }
                session.setAttribute("addUniversalMaster", universalMaster);

                forwardName = "searchpage";

                session.setAttribute("univerCollaps", "univerCollaps");
            }
        }
        if (buttonValue != null && buttonValue.equals("clear")) {
            if (session.getAttribute("universalUpdateRecords") != null) {
                session.removeAttribute("universalUpdateRecords");
            }
            if (session.getAttribute("universaldefaultRate") != null) {
                session.removeAttribute("universaldefaultRate");
            }
            if (session.getAttribute("univerCollaps") != null) {
                session.removeAttribute("univerCollaps");
            }
            if (session.getAttribute("uninoncommonList") != null) {
                session.removeAttribute("uninoncommonList");
            }
            if (session.getAttribute("searchuniMaster") != null) {
                session.removeAttribute("searchuniMaster");
            }
            if (session.getAttribute("nonunivercommonList") != null) {
                session.removeAttribute("nonunivercommonList");
            }
            if (session.getAttribute("unimessage") != null) {
                session.removeAttribute("unimessage");
            }
            forwardName = "searchpage";
        }
        if (buttonValue != null && buttonValue.equals("popupsearch")) {
            universalMaster = new UniversalMaster();
            if (session.getAttribute("searchuniMaster") != null) {
                universalMaster = (UniversalMaster) session.getAttribute("searchuniMaster");

            } else {
                universalMaster = new UniversalMaster();
            }
            if (terminalNumber != null && !terminalNumber.equals("")) {
                refTerminal1 = refTerminalDAO1.findById1(terminalNumber);
                if (refTerminal1 != null && universalMaster != null) {
                    universalMaster.setOriginTerminal(refTerminal1);
                    session.setAttribute("searchuniMaster", universalMaster);
                }

            }

            if (tername != null && !tername.equals("")) {

                RefTerminalTemp refTerminalobj = null;
                List terminal = refTerminalDAO1.findForManagement(null, tername, null, null);
                Iterator iter = terminal.iterator();
                if (terminal.size() > 0) {
                    refTerminalobj = (RefTerminalTemp) terminal.get(0);
                }
                if (refTerminalobj != null) {
                    universalMaster.setOriginTerminal(refTerminalobj);
                    session.setAttribute("searchuniMaster", universalMaster);
                }

            }

            if (destPort != null && !destPort.equals("")) {

                List portsList = portDAO.findPortCode(destPort, "0001");
                if (portsList != null && portsList.size() > 0) {
                    destObj = (PortsTemp) portsList.get(0);
                    universalMaster.setDestinationPort(destObj);
                    session.setAttribute("searchuniMaster", universalMaster);
                }

            }
            if (portName != null && !portName.equals("")) {

                List portsList = portDAO.findPierCode(null, portName);
                if (portsList != null && portsList.size() > 0) {

                    destObj = (PortsTemp) portsList.get(0);
                    universalMaster.setDestinationPort(destObj);
                    session.setAttribute("searchuniMaster", universalMaster);
                }
            }
            if (comCode != null && !comCode.equals("") && !comCode.equals("%")) {

                List comList = genericCodeDAO1.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {

                    comObj = (GenericCode) comList.get(0);

                    universalMaster.setCommodityCode(comObj);
                    session.setAttribute("searchuniMaster", universalMaster);
                }
            }

            if (comCodeDesc != null && !comCodeDesc.equals("")) {

                List codeList = genericCodeDAO1.findForAirRates(null, comCodeDesc);
                if (codeList != null && codeList.size() > 0) {
                    comObj = (GenericCode) codeList.get(0);
                    universalMaster.setCommodityCode(comObj);
                    session.setAttribute("searchuniMaster", universalMaster);
                }
            }

            forwardName = "searchpage";
        }

        //------------------------------------------------------------------------------------------------------

        if (request.getParameter("paramid") != null) {


            universalMaster = universalMasterDAO.findById(Integer.parseInt(request.getParameter("paramid")));

            User userid = null;
            UserDAO user1 = new UserDAO();
            if (session.getAttribute("view") != null) {

                session.removeAttribute("view");
            }
            if (session.getAttribute("loginuser") != null) {
                userid = (User) session.getAttribute("loginuser");
            }
            ProcessInfoDAO processinfoDAO = new ProcessInfoDAO();
            ProcessInfo pi = new ProcessInfo();
            String programid = null;
            programid = (String) session.getAttribute("processinfoforuniversal");
            String recordid = "";
            if (universalMaster.getId() != null) {
                recordid = universalMaster.getId().toString();
            }
            ProcessInfo processinfoobj = null;
            String editstatus = "startedited";
            String deletestatus = "startdeleted";
            if (programid != null && !programid.equals("")) {
                processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus, editstatus);

            }
            if (processinfoobj != null) {

                String view = "3";
                User loginuser = user1.findById(processinfoobj.getUserid());
                loginName = loginuser.getLoginName();
                msg = "This record is being used by ";
                message = msg + loginName;
                session.setAttribute("message", message);
                session.setAttribute("view", view);
                forwardName = "lclFrame";
            } else {
                if (userid != null) {
                    pi.setUserid(userid.getUserId());
                }
                if (programid != null && !programid.equals("")) {
                    pi.setProgramid(Integer.parseInt(programid));
                }
                java.util.Date currdate = new java.util.Date();
                pi.setProcessinfodate(currdate);
                pi.setEditstatus(editstatus);
                pi.setRecordid(recordid);
                processinfoDAO.save(pi);
                if (session.getAttribute("view") != null) {
                    session.removeAttribute("view");
                }

            }
            forwardName = "uniFrame";

            if (universalMaster.getUniversalCommodity() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalCommodity().iterator();
                while (iter.hasNext()) {
                    UniverseCommodityChrg airStd = (UniverseCommodityChrg) iter.next();
                    airStdList.add(airStd);
                }

            }

            if (universalMaster.getUniversalFlat() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalFlat().iterator();
                while (iter.hasNext()) {
                    UniverseFlatRate airdoc = (UniverseFlatRate) iter.next();
                    airStdList1.add(airdoc);
                }

            }

            if (universalMaster.getUniversalImport() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalImport().iterator();
                while (iter.hasNext()) {
                    UniverseAirFreight airff = (UniverseAirFreight) iter.next();
                    airStdList3.add(airff);
                }

            }
            if (universalMaster.getUniversalInsurance() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalInsurance().iterator();

                while (iter.hasNext()) {
                    UniverseInsuranceChrg universeInsuranceChrg = (UniverseInsuranceChrg) iter.next();
                    airStdList4.add(universeInsuranceChrg);

                }

            }
            if (universalMaster.getDestinationPort() != null && universalMaster.getDestinationPort().getId() != null) {
                Ports ports = portDAO.findById(universalMaster.getDestinationPort().getId());
                if (ports.getLclPortConfigSet() != null) {

                    Iterator iter = ports.getLclPortConfigSet().iterator();
                    while (iter.hasNext()) {
                        LCLPortConfiguration lcl = (LCLPortConfiguration) iter.next();
                        if (lcl.getDefaultRate() != null) {
                            defaultRate = lcl.getDefaultRate();
                            session.setAttribute("unibverdefaultRate", defaultRate);
                            break;

                        }
                    }
                }
            }
            session.setAttribute("uniinsurancelist", airStdList4);
            session.setAttribute("uniarifrightlist", airStdList3);
            session.setAttribute("unifaltratelist", airStdList1);
            session.setAttribute("unicssList", airStdList);
            session.setAttribute("addUniversalMaster", universalMaster);

            session.setAttribute("universaltabs", "edit");
            if (session.getAttribute("universalUpdateRecords") != null) {
                session.removeAttribute("universalUpdateRecords");
            }
            forwardName = "uniFrame";
        } //---------------Bottom line rates calculation-----------
        else if (request.getParameter("rates") != null && !request.getParameter("rates").equals("")) {
            universalMaster = universalMasterDAO.findById(Integer.parseInt(request.getParameter("rates")));
            //FTFDetails ftfDetails=new FTFDetails();
            UniverseCommodityChrg universalComm = new UniverseCommodityChrg();
            double rate = 0.0;   //----------rate/1000kgs
            double rcbm = 0.0;   //-------rate/cbm
            double cft = 0.0;
            double lbs = 0.0;
            double scft = 0.0;
            double slbs = 0.0;
            double amtkg = 0.0;
            double cbm = 0.0;
            double res1 = 0.0;
            double res2 = 0.0;
            double res3 = 0.0;
            double res4 = 0.0;

            if (universalMaster.getUniversalCommodity() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalCommodity().iterator();
                while (iter.hasNext()) {
                    universalComm = (UniverseCommodityChrg) iter.next();


                    if (universalComm.getStandard() != null && !universalComm.getStandard().equals("") && universalComm.getStandard().equals("Y")) {
                        if (universalComm.getAmtPer1000kg() != null) {
                            amtkg += universalComm.getAmtPer1000kg();
                        }
                        if (universalComm.getAmtPerCbm() != null) {

                            cbm += universalComm.getAmtPerCbm();

                        }
                        if (universalComm.getAmtPer100lbs() != null) {
                            slbs += universalComm.getAmtPer100lbs();
                        }
                        if (universalComm.getAmtPerCft() != null) {
                            scft += universalComm.getAmtPerCft();
                        }
                    }

                }
            }
            res1 = amtkg;
            res2 = cbm;
            res3 = slbs;
            res4 = scft;

            session.setAttribute("SearchUnirateforKg", res1);
            session.setAttribute("SearchUnirateforCbm", res2);
            session.setAttribute("SearchUnirateforLbs", res3);
            session.setAttribute("SearchUnirateforCft", res4);

            request.setAttribute("btl", "btl"); //forwarding to manageairrates.jsp
            forwardName = "searchpage";
        } else if (request.getParameter("param") != null) {

            universalMaster = universalMasterDAO.findById(Integer.parseInt(request.getParameter("param")));

            String view = "3";
            session.setAttribute("view", view);

            if (universalMaster.getUniversalCommodity() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalCommodity().iterator();
                while (iter.hasNext()) {
                    UniverseCommodityChrg airStd = (UniverseCommodityChrg) iter.next();
                    airStdList.add(airStd);
                }

            }

            if (universalMaster.getUniversalFlat() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalFlat().iterator();
                while (iter.hasNext()) {
                    UniverseFlatRate airdoc = (UniverseFlatRate) iter.next();
                    airStdList1.add(airdoc);
                }

            }

            if (universalMaster.getUniversalImport() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalImport().iterator();
                while (iter.hasNext()) {
                    UniverseAirFreight airff = (UniverseAirFreight) iter.next();
                    airStdList3.add(airff);
                }

            }
            if (universalMaster.getUniversalInsurance() != null) {

                Iterator iter = (Iterator) universalMaster.getUniversalInsurance().iterator();

                while (iter.hasNext()) {
                    UniverseInsuranceChrg universeInsuranceChrg = (UniverseInsuranceChrg) iter.next();
                    airStdList4.add(universeInsuranceChrg);

                }

            }
            session.setAttribute("uniinsurancelist", airStdList4);
            session.setAttribute("uniarifrightlist", airStdList3);
            session.setAttribute("unifaltratelist", airStdList1);
            session.setAttribute("unicssList", airStdList);
            session.setAttribute("addUniversalMaster", universalMaster);

            session.setAttribute("universaltabs", "edit");
            forwardName = "uniFrame";
            if (session.getAttribute("message") != null) {
                session.removeAttribute("message");

            }
        }
        //---------------------------------------------------------------------------------------------------------


        request.setAttribute("LclbuttonValue", buttonValue);

        return mapping.findForward(forwardName);
    }
}