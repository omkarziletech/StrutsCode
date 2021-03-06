/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.FclBuyAirFreightCharges;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyCostFutureRatesDAO;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.SerchFutureFCLForm;
import com.gp.cong.logisoft.util.DBUtil;

/**
 * MyEclipse Struts Creation date: 09-23-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/serchFutureFCL" name="serchFutureFCLForm"
 *                input="/form/serchFutureFCL.jsp" scope="request"
 */
public class SerchFutureFCLAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        SerchFutureFCLForm searchFCLForm = (SerchFutureFCLForm) form;// TODO
        // Auto-generated
        // method
        // stub
        String button = searchFCLForm.getButtonValue();
        String buttonValue = searchFCLForm.getButtonValue();
        String get = searchFCLForm.getIndex();
        String match = searchFCLForm.getMatch();
        // match=searchFCLForm.get
        String trmNum = searchFCLForm.getTerminalNumber();
        String portNum = searchFCLForm.getDestSheduleNumber();
        String comCode = searchFCLForm.getComCode();
        String tername = searchFCLForm.getTerminalName();
        String portName = searchFCLForm.getDestAirportname();
        String comCodedesc = searchFCLForm.getComDescription();
        String sslineNO = searchFCLForm.getSslinenumber();
        String ssName = searchFCLForm.getSslinename();
        String ocean = searchFCLForm.getMatch();
        CustomerDAO carriersOrLineDAO = new CustomerDAO();
        TradingPartnerTemp carriersOrLineTemp = null;
        UnLocationDAO refTerminalDAO = new UnLocationDAO();
        PortsDAO portsDAO = new PortsDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        // RefTerminalTemp origin=null;
        // PortsTemp dest=null;
        TradingPartnerTemp carries = null;
        FclBuyCostDAO fclBuyCostDAO = new FclBuyCostDAO();
        GenericCode genericCode = null;
        GenericCode genObj = null;
        int con = 0;
        List commonList = new ArrayList();
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        List searchFclrecords = new ArrayList();
        List secondunittype = new ArrayList();
        String loginName = "";
        String msg = "";
        String forwardName = "";
        DBUtil dbUtil = new DBUtil();
        String message = "";
        List fclBuyList = new ArrayList();
        FclBuyCost fclBuyCost = new FclBuyCost();
        Set set = new HashSet();
        UnLocation refTerminal = null;
        GenericCode gen = null;
        UnLocation ports = null;
        int ki = 0;
        List unittypelist = new ArrayList();
        HttpSession session = ((HttpServletRequest) request).getSession();
        FclBuy fclBuy = new FclBuy();

        if (buttonValue != null && buttonValue.equals("popupsearch")) {
            if (session.getAttribute("futuresearchfclrecords") != null) {
                fclBuy = (FclBuy) session.getAttribute("futuresearchfclrecords");
            } else {
                fclBuy = new FclBuy();
            }
            if (trmNum != null && !trmNum.equals("")) {

                List unLocationList = refTerminalDAO.findForManagement(trmNum, null);
                if (unLocationList != null && unLocationList.size() > 0) {
                    refTerminal = (UnLocation) unLocationList.get(0);
                    fclBuy.setOriginTerminal(refTerminal);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                }

            }
            if (tername != null && !tername.equals("")) {
                UnLocation refTerminalobj = null;
                List terminal = refTerminalDAO.findForManagement(null, tername);

                if (terminal.size() > 0) {
                    refTerminalobj = (UnLocation) terminal.get(0);
                }
                if (refTerminalobj != null) {
                    fclBuy.setOriginTerminal(refTerminalobj);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                }

            }

            if (portNum != null && !portNum.equals("")) {

                List portsList = refTerminalDAO.findForManagement(portNum, null);
                if (portsList != null && portsList.size() > 0) {
                    ports = (UnLocation) portsList.get(0);
                    fclBuy.setDestinationPort(ports);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                }
            }

            if (portName != null && !portName.equals("")) {

                List portsList = refTerminalDAO.findForManagement(null, portName);
                if (portsList != null && portsList.size() > 0) {

                    ports = (UnLocation) portsList.get(0);
                    fclBuy.setDestinationPort(ports);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                }

            }

            if (comCode != null && !comCode.equals("") && !comCode.equals("%")) {

                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    gen = (GenericCode) comList.get(0);
                    fclBuy.setComNum(gen);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                }
            }

            if (comCodedesc != null && !comCodedesc.equals("")) {

                List codeList = genericCodeDAO.findForAirRates(null,
                        comCodedesc);
                if (codeList != null && codeList.size() > 0) {
                    gen = (GenericCode) codeList.get(0);
                    fclBuy.setComNum(gen);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                }

            }

            if (sslineNO != null && !sslineNO.equals("")) {

                List codeList = carriersOrLineDAO.findAccountNo1(sslineNO);
                if (codeList != null && codeList.size() > 0) {
                    carries = (TradingPartnerTemp) codeList.get(0);
                    fclBuy.setSslineNo(carries);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                }
            }

            if (ssName != null && !ssName.equals("")) {

                List codeList = carriersOrLineDAO.findAccountName1(ssName);
                if (codeList != null && codeList.size() > 0) {
                    carries = (TradingPartnerTemp) codeList.get(0);
                    fclBuy.setSslineNo(carries);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                }
            }

            forwardName = "fclsearch";
        }

        if (buttonValue != null && buttonValue.equals("search")) {

            if (session.getAttribute("futuresearchfclrecords") != null) {
                fclBuy = (FclBuy) session.getAttribute("futuresearchfclrecords");
            }

            if (trmNum != null && !trmNum.equals("")) {
                List unLocationList = refTerminalDAO.findForManagement(trmNum, null);
                if (unLocationList != null && unLocationList.size() > 0) {
                    refTerminal = (UnLocation) unLocationList.get(0);
                    fclBuy.setOriginTerminal(refTerminal);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                } else {
                    // request.setAttribute("worning", "Please enter proper
                    // terminal number");
                }
            } else {
                UnLocation ref = null;
                fclBuy.setOriginTerminal(ref);
                session.setAttribute("futuresearchfclrecords", fclBuy);
            }

            if (portNum != null && !portNum.equals("")) {
                List portsList = refTerminalDAO.findForManagement(portNum, null);
                if (portsList != null && portsList.size() > 0) {
                    ports = (UnLocation) portsList.get(0);
                    fclBuy.setDestinationPort(ports);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                } else {
                    // request.setAttribute("worning", "Please enter proper
                    // Destination Port");
                }
            } else {
                UnLocation p1 = null;
                fclBuy.setDestinationPort(p1);
                session.setAttribute("futuresearchfclrecords", fclBuy);
            }
            if (comCode != null && !comCode.equals("") && !comCode.equals("%")) {
                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    gen = (GenericCode) comList.get(0);
                    fclBuy.setComNum(gen);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                } else {
                    // request.setAttribute("worning", "Please enter proper
                    // Commodity Code");
                }
            } else {
                GenericCode g1 = null;
                fclBuy.setComNum(g1);
                session.setAttribute("futuresearchfclrecords", fclBuy);
            }
            if (sslineNO != null && !sslineNO.equals("")) {
                List SSNo = carriersOrLineDAO.findAccountNo1(sslineNO);
                if (SSNo != null && SSNo.size() > 0) {
                    carriersOrLineTemp = (TradingPartnerTemp) SSNo.get(0);
                    fclBuy.setSslineNo(carriersOrLineTemp);
                    session.setAttribute("futuresearchfclrecords", fclBuy);
                } else {

                    // request.setAttribute("worning", "Please enter proper SS
                    // LINE number Code");
                }
            } else {
                TradingPartnerTemp c1 = null;
                fclBuy.setSslineNo(c1);
                session.setAttribute("futuresearchfclrecords", fclBuy);
            }
            session.setAttribute("futureaddfclrecords", fclBuy);
            if (session.getAttribute("searchunittypelist") != null) {

                session.removeAttribute("searchunittypelist");
            }

            if (session.getAttribute("fclfuturemessage") != null) {
                session.removeAttribute("fclfuturemessage");
            }
            String comm = "";
            if (get != null && !get.equals("")) {
                if (match != null && match.equals("match")) {

                    if (session.getAttribute("searchFutureFclcodelist") != null) {
                        session.removeAttribute("searchFutureFclcodelist");
                    }
                    if (session.getAttribute("fclcommonList") != null) {
                        session.removeAttribute("fclcommonList");
                    }

                    if (request.getAttribute("worning") == null) {
                        if (trmNum != null || portNum != null || comCode != null || sslineNO != null) {
                            searchFclrecords = fclBuyDAO.findForSearchFclBuyRatesMatch(trmNum,
                                    portNum, comCode, sslineNO, null, null);

                            for (int i = 0; i < searchFclrecords.size(); i++) {
                                fclBuy = (FclBuy) searchFclrecords.get(i);
                                List newList = fclBuyCostDAO.findAllUsers(fclBuy.getFclStdId());
                                for (int li = 0; li < newList.size(); li++) {
                                    fclBuyCost = (FclBuyCost) newList.get(li);

                                    if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equalsIgnoreCase("OFR")) {
                                        set = fclBuyCost.getFclBuyFutureTypesSet();
                                        Iterator it = set.iterator();
                                        while (it.hasNext()) {
                                            fclBuyList.add(fclBuyCost);
                                            // commonList.add(fclBuyCost);
                                            break;
                                        }

                                    }
                                }
                                for (int li = 0; li < newList.size(); li++) {
                                    fclBuyCost = (FclBuyCost) newList.get(li);

                                    if (fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equalsIgnoreCase("OFR")) {
                                    } else {
                                        set = fclBuyCost.getFclBuyFutureTypesSet();
                                        Iterator it = set.iterator();
                                        while (it.hasNext()) {

                                            fclBuyList.add(fclBuyCost);
                                            break;
                                        }
                                    }
                                }

                            }
                            session.setAttribute("searchFutureFclcodelist",
                                    fclBuyList);

                            session.setAttribute("futureaddfclrecords",
                                    fclBuy);
                            if (fclBuyList != null && fclBuyList.size() < 1) {

                                session.setAttribute("futureaddfclrecords",
                                        fclBuy);

                            }
                            session.setAttribute("fclFuturecaption",
                                    "FCL Future Match only Rates");

                        }
                    }

                } else if (ocean != null && ocean.equals("ocean")) {
                    if (session.getAttribute("searchFutureFclcodelist") != null) {
                        session.removeAttribute("searchFutureFclcodelist");
                    }

                    if (request.getAttribute("worning") == null) {
                        if (trmNum != null || portNum != null || comCode != null || sslineNO != null) {
                            searchFclrecords = fclBuyDAO.findForSearchFclBuyRatesMatch(trmNum,
                                    portNum, comCode, sslineNO, null, null);

                            for (int i = 0; i < searchFclrecords.size(); i++) {
                                fclBuy = (FclBuy) searchFclrecords.get(i);

                                set = fclBuy.getFclBuyCostsSet();
                                Iterator it = set.iterator();
                                while (it.hasNext()) {
                                    fclBuyCost = (FclBuyCost) it.next();
                                    if (fclBuyCost != null && fclBuyCost.getCostId() != null && fclBuyCost.getCostId().getCode().equalsIgnoreCase("OFR")) {

                                        set = fclBuyCost.getFclBuyFutureTypesSet();
                                        Iterator it1 = set.iterator();
                                        while (it1.hasNext()) {
                                            fclBuyList.add(fclBuyCost);
                                            break;
                                        }
                                    }
                                // }
                                }
                            }

                            session.setAttribute("searchFutureFclcodelist",
                                    fclBuyList);
                            session.setAttribute("futureaddfclrecords", fclBuy);
                            session.setAttribute("fclFuturecaption",
                                    "  FCL Future Ocean Freight Rates");

                        }
                    }
                } else if (match != null && match.equals("starts")) {
                }

                if (session.getAttribute("searchunittypelist") == null) {
                    List value = new ArrayList();
                    unittypelist = dbUtil.getUnitListForFCLTest(
                            new Integer(38), "yes", "Select Unit code");
                    for (int i = 0; i < unittypelist.size(); i++) {
                        LabelValueBean removegenCo = (LabelValueBean) unittypelist.get(i);
                        if (!removegenCo.getValue().equals("0")) {
                            value.add(removegenCo.getLabel());
                        }
                    }
                    session.setAttribute("searchunittypelist", value);// FOR
                // OCEN
                // FRIGHT
                // RIGHT
                // COST
                // CODE
                // FLATE
                // RATE
                // PER
                // CUBIC

                }
            }
            if (session.getAttribute("message") != null) {
                session.removeAttribute("message");

            }
            if (session.getAttribute("view") != null) {

                session.removeAttribute("view");
            }
            session.setAttribute("fclFutureCollaps", "fclFutureCollaps");
            forwardName = "fclsearch";

        }

        if (request.getParameter("paramid") != null) {
            fclBuy = fclBuyDAO.findById(Integer.parseInt(request.getParameter("paramid")));
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
            programid = (String) session.getAttribute("processinfoforfl");
            String recordid = "";
            if (fclBuy != null && fclBuy.getFclStdId() != null) {
                recordid = fclBuy.getFclStdId().toString();
            }

            String editstatus = "startedited";
            String deletestatus = "startdeleted";
            ProcessInfo processinfoobj = null;
            if (programid != null && !programid.equals("")) {
                processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus,
                        editstatus);
            }

            if (processinfoobj != null) {
                String view = "3";
                User loginuser = user1.findById(processinfoobj.getUserid());
                loginName = loginuser.getLoginName();
                msg = "This record is being used by ";
                message = msg + loginName;
                session.setAttribute("usermessage", message);
                session.setAttribute("view", view);
                forwardName = "addfclrecords";
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
            List unitlistedit = new ArrayList();
            Set editCostType = new HashSet<FclBuyCost>();
            Set editBuyCostType = new HashSet<FclBuyCostTypeFutureRates>();
            Set editBuyAirFirght = new HashSet<FclBuyAirFreightCharges>();
            // Set editBuyCostTypeRatecharge=new HashSet();
            List reoveList = new ArrayList();
            List fclCostList = new ArrayList();
            List fclCostListType = new ArrayList();
            List editBuyAirFirghtlist = new ArrayList();
            FclBuyCostTypeFutureRates fclBuyCostTypeRates = new FclBuyCostTypeFutureRates();
            FclBuyAirFreightCharges fclBuyAirFreightCharges = new FclBuyAirFreightCharges();
            editCostType = fclBuy.getFclBuyCostsSet();
            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator it = fclBuy.getFclBuyCostsSet().iterator();
                while (it.hasNext()) {
                    fclBuyCost = (FclBuyCost) it.next();

                    FclBuyCostFutureRatesDAO fclBuyCostFutureRatesDAO = new FclBuyCostFutureRatesDAO();
                    FclBuyCostTypeFutureRates fclBuyCostTypeFutureRates = fclBuyCostFutureRatesDAO.findFclBuyFutureByCostId(fclBuyCost.getFclCostId());
                    if (fclBuyCost.getFclBuyFutureTypesSet() != null) {
                        Iterator fclBuyCostIterator = fclBuyCost.getFclBuyFutureTypesSet().iterator();
                        while (fclBuyCostIterator.hasNext()) {
                            fclBuyCostTypeRates = (FclBuyCostTypeFutureRates) fclBuyCostIterator.next();
                            if (fclBuyCost.getFclCostId() != null && fclBuyCostTypeRates.getFclCostId() != null && fclBuyCost.getFclCostId().equals(
                                    fclBuyCostTypeRates.getFclCostId())) {
                                fclBuyCostTypeRates.setCostCode(fclBuyCost.getCostId().getCodedesc());
                                fclBuyCostTypeRates.setCostType(fclBuyCost.getContType().getCodedesc());
                                fclBuyCostTypeRates.setCostId(fclBuyCost.getContType().getId().toString());
                                fclBuyCostTypeRates.setTypeId(fclBuyCost.getCostId().getId().toString());
                                fclCostListType.add(fclBuyCostTypeRates);
                            }
                        }
                        Iterator it2 = fclBuyCost.getFclBuyFutureTypesSet().iterator();
                        while (it2.hasNext()) {
                            fclBuyCostTypeRates = (FclBuyCostTypeFutureRates) it2.next();
                            if (fclBuyCost.getFclCostId() != null && fclBuyCostTypeRates.getFclCostId() != null && fclBuyCost.getFclCostId().equals(
                                    fclBuyCostTypeRates.getFclCostId())) {
                                fclCostList.add(fclBuyCost);
                                break;
                            // getFclrecords.put(fclBuyCost.getFclStdId(),fclBuyCost);

                            }
                        }
                    }// END OF fclBuyCost.getFclBuyFutureTypesSet();
                // //Need to getting clarifiction weather this set should be
                // include in Future Rates


                }
                if (session.getAttribute("message") != null) {
                    session.removeAttribute("message");

                }
                // session.setAttribute("fclfrightrecords",editBuyAirFirghtlist);
                session.setAttribute("fclrecordsfuture", fclCostListType);
                session.setAttribute("costFutureCodeList", fclCostList);
                session.setAttribute("editrecords", "edit");
                session.setAttribute("futureaddfclrecords", fclBuy);
            }


            forwardName = "addfclrecords";

        }

        if (request.getParameter("param") != null) {
            String view = "3";
            session.setAttribute("view", view);
            fclBuy = fclBuyDAO.findById(Integer.parseInt(request.getParameter("param")));
            List unitlistedit = new ArrayList();
            Set editCostType = new HashSet<FclBuyCost>();
            Set editBuyCostType = new HashSet<FclBuyCostTypeFutureRates>();
            Set editBuyAirFirght = new HashSet<FclBuyAirFreightCharges>();
            List reoveList = new ArrayList();
            List fclCostList = new ArrayList();
            List fclCostListType = new ArrayList();
            List editBuyAirFirghtlist = new ArrayList();
            FclBuyCostTypeFutureRates fclBuyCostTypeRates = new FclBuyCostTypeFutureRates();

            FclBuyAirFreightCharges fclBuyAirFreightCharges = new FclBuyAirFreightCharges();

            editCostType = fclBuy.getFclBuyCostsSet();

            if (fclBuy.getFclBuyCostsSet() != null) {
                Iterator it = fclBuy.getFclBuyCostsSet().iterator();
                while (it.hasNext()) {
                    fclBuyCost = (FclBuyCost) it.next();
                    fclCostList.add(fclBuyCost);

                }
            // session.setAttribute("fclCostList", fclCostList);
            }
            if (fclCostList != null) {
                for (int i = 0; i < fclCostList.size(); i++) {
                    fclBuyCost = (FclBuyCost) fclCostList.get(i);
                    if (fclBuyCost.getFclBuyAirFreightSet() != null) {

                        Iterator it = fclBuyCost.getFclBuyAirFreightSet().iterator();
                        while (it.hasNext()) {
                            fclBuyAirFreightCharges = (FclBuyAirFreightCharges) it.next();

                            if (fclBuyCost.getCostId() != null && fclBuyCost.getContType() != null) {
                                fclBuyAirFreightCharges.setCostCode(fclBuyCost.getCostId().getCodedesc());
                                fclBuyAirFreightCharges.setCostType(fclBuyCost.getContType().getCodedesc());
                                fclBuyAirFreightCharges.setCostId(fclBuyCost.getContType().getId().toString());
                                fclBuyAirFreightCharges.setTypeId(fclBuyCost.getContType().getId().toString());
                            }
                            editBuyAirFirghtlist.add(fclBuyAirFreightCharges);
                        }
                    }
                    if (fclBuyCost.getFclBuyFutureTypesSet() != null) {
                        Iterator it = fclBuyCost.getFclBuyFutureTypesSet().iterator();
                        while (it.hasNext()) {
                            fclBuyCostTypeRates = (FclBuyCostTypeFutureRates) it.next();

                            if (fclBuyCost.getCostId() != null) {
                                fclBuyCostTypeRates.setCostCode(fclBuyCost.getCostId().getCodedesc());
                                fclBuyCostTypeRates.setCostType(fclBuyCost.getContType().getCodedesc());
                                fclBuyCostTypeRates.setCostId(fclBuyCost.getContType().getId().toString());
                            }
                            fclCostListType.add(fclBuyCostTypeRates);

                        }
                    }

                // editBuyCostType=fclBuyCostsSet.getFclBuyUnitTypesSet();
                }
                session.setAttribute("fclfrightrecords", editBuyAirFirghtlist);
                session.setAttribute("fclrecordsfuture", fclCostListType);

                if (session.getAttribute("message") != null) {
                    session.removeAttribute("message");

                }
            }

            session.setAttribute("costFutureCodeList", fclCostList);
            session.setAttribute("editreacords", "edit");
            session.setAttribute("futureaddfclrecords", fclBuy);

            forwardName = "addfclrecords";

        }
        if (button != null && button.equals("clear")) {
            if (session.getAttribute("searchFutureFclcodelist") != null) {
                session.removeAttribute("searchFutureFclcodelist");
            }
            if (session.getAttribute("futuresearchfclrecords") != null) {
                session.removeAttribute("futuresearchfclrecords");
            }
            if (session.getAttribute("fclcommonList") != null) {
                session.removeAttribute("fclcommonList");
            }
            if (session.getAttribute("fclfuturemessage") != null) {
                session.removeAttribute("fclfuturemessage");
            }
            if (session.getAttribute("futureaddfclrecords") != null) {
                session.removeAttribute("futureaddfclrecords");
            }
            if (session.getAttribute("fclFutureCollaps") != null) {
                session.removeAttribute("fclFutureCollaps");
            }
            if (session.getAttribute("fclrecordsfuture") != null) {
                session.removeAttribute("fclrecordsfuture");
            }
            if (session.getAttribute("fclfrightrecords") != null) {
                session.removeAttribute("fclfrightrecords");
            }
            if (session.getAttribute("searchunittypelist") != null) {
                session.removeAttribute("searchunittypelist");
            }

            forwardName = "fclsearch";
        }

        return mapping.findForward(forwardName);
    }
}