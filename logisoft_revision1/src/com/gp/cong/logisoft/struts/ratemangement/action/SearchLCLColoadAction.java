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
import com.gp.cong.logisoft.domain.LCLColoadCommodityCharges;
import com.gp.cong.logisoft.domain.LCLColoadDocumentCharges;
import com.gp.cong.logisoft.domain.LCLColoadStandardCharges;
import com.gp.cong.logisoft.domain.LCLColoadDetails;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.LCLColoadMaster;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.LCLColoadMasterDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchLCLColoadForm;
import com.gp.cong.logisoft.util.DBUtil;

public class SearchLCLColoadAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchLCLColoadForm searchLCLColoadForm = (SearchLCLColoadForm) form;// TODO
        // Auto-generated
        // method
        // stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = searchLCLColoadForm.getButtonValue();
        String match = searchLCLColoadForm.getMatch();
        String common = searchLCLColoadForm.getCommon();
        String search = searchLCLColoadForm.getSearch();
        LCLColoadDetails lcldetails = null;
        List commonStandardList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        List commonList = new ArrayList();
        List noncommonList = new ArrayList();
        String refObj = null;
        String destObj = null;
        GenericCode comObj = null;
        String loginName = "";
        String message = "";
        DBUtil dbutil = new DBUtil();
        String msg = "";
        String forwardName = "";
        List airStdList = new ArrayList();
        List airStdList1 = new ArrayList();
        List airStdList3 = new ArrayList();
        List airStdList4 = new ArrayList();
        String terminalNumber = searchLCLColoadForm.getTerminalNumber();
        String tername = searchLCLColoadForm.getTerminalName();
        String destPort = searchLCLColoadForm.getDestSheduleNumber();
        String portName = searchLCLColoadForm.getDestAirportname();
        String comCode = searchLCLColoadForm.getComCode();
        String comCodedesc = searchLCLColoadForm.getComDescription();
        AirRatesBean airRatesBean = new AirRatesBean();
        LCLColoadMasterDAO lCLColoadMasterDAO = new LCLColoadMasterDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        UnLocation unLocation = new UnLocation();
        LCLColoadMaster lCLColoadMaster = new LCLColoadMaster();
        List searchLCLColoadList = new ArrayList();
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        PortsDAO portsDAO = new PortsDAO();
        RefTerminalTemp refTerminal = null;
        GenericCode gen = null;

        if (session.getAttribute("searchlclColoadMaster") != null) {
            lCLColoadMaster = (LCLColoadMaster) session.getAttribute("searchlclColoadMaster");
            if (lCLColoadMaster.getOriginTerminal() != null) {
                refObj = lCLColoadMaster.getOriginTerminal();
            }
            if (lCLColoadMaster.getDestinationPort() != null) {
                destObj = lCLColoadMaster.getDestinationPort();
            }

        } else {
            lCLColoadMaster = new LCLColoadMaster();
        }

        if (buttonValue != null && buttonValue.equals("search")) {

            if (terminalNumber != null && !terminalNumber.equals("")) {
                List unLocationList = unLocationDAO.findForManagement(terminalNumber, tername);
                if (unLocationList != null && unLocationList.size() > 0) {
                    unLocation = (UnLocation) unLocationList.get(0);
                    lCLColoadMaster.setOriginTerminal(unLocation.getUnLocationCode());
                    lCLColoadMaster.setOriginTerminalName(unLocation.getUnLocationName());
                }
            } else if (tername != null && !tername.equals("")) {
                String stringTokens[] = StringUtils.splitPreserveAllTokens(tername, '-');
                if (stringTokens != null && stringTokens.length > 1) {
                    tername = stringTokens[0];
                    terminalNumber = stringTokens[1];
                }
                List terminal = unLocationDAO.findForManagement(terminalNumber, tername);
                if (terminal.size() > 0) {
                    unLocation = (UnLocation) terminal.get(0);
                }
                if (unLocation != null) {
                    lCLColoadMaster.setOriginTerminal(unLocation.getUnLocationCode());
                }
            }
            if (destPort != null && !destPort.equals("")) {
                List portsList = unLocationDAO.findForManagement(destPort, null);
                if (portsList != null && portsList.size() > 0) {
                    unLocation = (UnLocation) portsList.get(0);
                    lCLColoadMaster.setDestinationPort(unLocation.getUnLocationCode());
                    lCLColoadMaster.setDestinationPortName(unLocation.getUnLocationName());
                }
            } else if (portName != null && !portName.equals("")) {
                String stringTokens[] = StringUtils.splitPreserveAllTokens(portName, '-');
                if (stringTokens != null && stringTokens.length > 1) {
                    portName = stringTokens[0];
                    destPort = stringTokens[1];
                }
                List portsList = unLocationDAO.findForManagement(destPort, portName);
                if (portsList != null && portsList.size() > 0) {
                    unLocation = (UnLocation) portsList.get(0);
                    lCLColoadMaster.setDestinationPort(unLocation.getUnLocationCode());
                }
            }
            if (comCode != null && !comCode.equals("") && !comCode.equals("%")) {
                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    comObj = (GenericCode) comList.get(0);
                    lCLColoadMaster.setCommodityCode(comObj.getCode());
                    lCLColoadMaster.setCommodityCodeName(comObj.getCodedesc());
                    if (comObj.getCode().equals("000000")) {
                        session.setAttribute("setLCLColoadTabEnable", "enable");
                    } else {
                        session.removeAttribute("setLCLColoadTabEnable");
                    }
                }
            }
            session.setAttribute("searchlclColoadMaster", lCLColoadMaster);
            if (session.getAttribute("message") == null && (search != null && search.equals("get"))) {
                String defaultRate = "";

                if (terminalNumber != null && destPort != null && (comCode == null || comCode.equals(""))) {
                    searchLCLColoadList = lCLColoadMasterDAO.findForSearchlclcoRatesmatch(terminalNumber,
                            destPort, comCode, match);
                    for (int lcl = 0; lcl < searchLCLColoadList.size(); lcl++) {
                        LCLColoadMaster lCLColoadMaster1 = (LCLColoadMaster) searchLCLColoadList.get(lcl);
                        if (lCLColoadMaster1.getCommodityCode() != null && lCLColoadMaster1.getCommodityCode() != null && lCLColoadMaster1.getCommodityCode().equals("000000")) {
                            commonStandardList.add(lCLColoadMaster1);
                            searchLCLColoadList.remove(lcl);
                        }
                    }
                    for (int i = 0; i < commonStandardList.size(); i++) {
                        LCLColoadMaster lclparentObject = (LCLColoadMaster) commonStandardList.get(i);
                        boolean flag = false;
                        if (lclparentObject.getLclColoadStdChgSet() != null) {
                            Iterator iter = (Iterator) lclparentObject.getLclColoadStdChgSet().iterator();
                            while (iter.hasNext()) {
                                LCLColoadStandardCharges retailStdChild = (LCLColoadStandardCharges) iter.next();
                                if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals(
                                        "Y")) {
                                    commonList.add(retailStdChild);
                                }
                                flag = true;
                            }

                        }// ----code to display only standard records
                        // ends----
                        if (!flag) {
                            session.setAttribute("lclcommonListCaps", "No Accessorial Add");
                        } else {
                            session.setAttribute("llcommonList", commonList);
                            session.setAttribute("lclcommonListCaps", "Common Accessorial Charges (Standard Only)");
                        }
                        break;
                    }

                } else if (terminalNumber != null && destPort != null && comCode != null) {
                    searchLCLColoadList = lCLColoadMasterDAO.findForSearchlclcoRatesstarts(terminalNumber,
                            destPort, comCode, match);
                    boolean flag = false;
                    boolean commonflag = false;
                    for (int lcl = 0; lcl < searchLCLColoadList.size(); lcl++) {
                        LCLColoadMaster lCLColoadMaster1 = (LCLColoadMaster) searchLCLColoadList.get(lcl);
                        if (lCLColoadMaster1.getCommodityCode() != null && lCLColoadMaster1.getCommodityCode() != null && lCLColoadMaster1.getCommodityCode().equals("000000")) {
                            commonStandardList.add(lCLColoadMaster1);
                            searchLCLColoadList.remove(lcl);
                            commonflag = true;
                        }
                    }
                    if (!commonflag) {
                        commonStandardList = lCLColoadMasterDAO.findForSearchlclcoRatesmatch(terminalNumber,
                                destPort, "000000", match);
                    }
                    for (int i = 0; i < commonStandardList.size(); i++) {
                        LCLColoadMaster lclparentObject = (LCLColoadMaster) commonStandardList.get(i);
                        if (lclparentObject.getLclColoadStdChgSet() != null) {
                            Iterator iter = (Iterator) lclparentObject.getLclColoadStdChgSet().iterator();
                            while (iter.hasNext()) {
                                LCLColoadStandardCharges retailStdChild = (LCLColoadStandardCharges) iter.next();
                                if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals(
                                        "Y")) {
                                    commonList.add(retailStdChild);
                                }
                                flag = true;

                            }

                        }// ----code to display only standard records
                        // ends----
                        if (!flag) {
                            session.setAttribute("lclcommonListCaps",
                                    "No Accessorial Add");
                        } else {
                            session.setAttribute("llcommonList", commonList);
                            session.setAttribute("lclcommonListCaps",
                                    "Common Accessorial Charges (Standard Only)");
                        }
                        break;
                    }
                }

                session.setAttribute("llnoncommonList", searchLCLColoadList);
                session.setAttribute("lclcoloadRatescaption",
                        "Ocean Freight Rates");

            }
            session.setAttribute("lclcollaps", "lclcollaps");
            forwardName = "searchlclframe";
        }
        if (buttonValue != null && buttonValue.equals("searchall")) {
            if (refObj != null && destObj != null) {
                searchLCLColoadList = lCLColoadMasterDAO.findForSearchlclcoRatesmatch(refObj, destObj, "000000", match);
                for (int i = 0; i < searchLCLColoadList.size(); i++) {
                    LCLColoadMaster lclparentObject = (LCLColoadMaster) searchLCLColoadList.get(i);
                    if (lclparentObject.getLclColoadStdChgSet() != null) {
                        Iterator iter = (Iterator) lclparentObject.getLclColoadStdChgSet().iterator();
                        while (iter.hasNext()) {
                            LCLColoadStandardCharges retailStdChild = (LCLColoadStandardCharges) iter.next();
                            commonList.add(retailStdChild);
                        }
                    }// ----code to display only standard records ends----
                }
                session.setAttribute("llcommonList", commonList);
                session.setAttribute("lclcommonListCaps",
                        "Common Accessorial Charges (All)");
            }
            forwardName = "searchlclframe";
        }// search All
        if (buttonValue != null && buttonValue.equals("searchStarndard")) {
            if (refObj != null && destObj != null) {
                searchLCLColoadList = lCLColoadMasterDAO.findForSearchlclcoRatesmatch(refObj, destObj,
                        "000000", match);
                for (int i = 0; i < searchLCLColoadList.size(); i++) {
                    LCLColoadMaster lclparentObject = (LCLColoadMaster) searchLCLColoadList.get(i);
                    if (lclparentObject.getLclColoadStdChgSet() != null) {
                        Iterator iter = (Iterator) lclparentObject.getLclColoadStdChgSet().iterator();
                        while (iter.hasNext()) {
                            LCLColoadStandardCharges retailStdChild = (LCLColoadStandardCharges) iter.next();
                            if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals("Y")) {
                                commonList.add(retailStdChild);
                            }

                        }
                    }// ----code to display only standard records ends----
                } // for
                session.setAttribute("llcommonList", commonList);
                session.setAttribute("lclcommonListCaps",
                        "Common Accessorial Charges (Standard Only)");
            }
            forwardName = "searchlclframe";
        }// standrad

        // ------------------------------------------------------------------------------------------------------

        if (request.getParameter("paramid") != null || (buttonValue != null && buttonValue.equals("paramid")) || (buttonValue != null && buttonValue.equals("paramidDoc"))) {
            if (buttonValue != null && buttonValue.equals("paramid")) {
                if (session.getAttribute("searchlclColoadMaster") != null) {
                    List list = new ArrayList();
                    GenericCode genObj = genericCodeDAO.findById(11292);
                    LCLColoadMaster retailRatesObj1 = (LCLColoadMaster) session.getAttribute("searchlclColoadMaster");
                    list = dbutil.getCoLoadDetails(retailRatesObj1.getOriginTerminal(), retailRatesObj1.getDestinationPort(), genObj.getCode());
                    if (list != null && list.size() > 0) {
                        lCLColoadMaster = (LCLColoadMaster) list.get(0);
                    }
                    session.setAttribute("getAgsss", "Doc");
                }
                if (session.getAttribute("coStandardCharges") != null) {
                    session.removeAttribute("coStandardCharges");
                }
            } else if (buttonValue != null && buttonValue.equals("paramidDoc")) {
                List list = new ArrayList();
                GenericCode genObj = genericCodeDAO.findById(11292);
                LCLColoadMaster retailRatesObj1 = (LCLColoadMaster) session.getAttribute("searchlclColoadMaster");
                list = dbutil.getCoLoadDetails(retailRatesObj1.getOriginTerminal(), retailRatesObj1.getDestinationPort(), genObj.getCode());
                if (list != null && list.size() > 0) {
                    lCLColoadMaster = (LCLColoadMaster) list.get(0);
                }
                session.setAttribute("getDoc", "Doc");
            }

            if (request.getParameter("paramid") != null && !request.getParameter("paramid").equals("")) {
                lCLColoadMaster = lCLColoadMasterDAO.findById(Integer.parseInt(request.getParameter("paramid")));
            }

            if (request.getParameter("ind") != null) {
                LCLColoadStandardCharges retailStandard = new LCLColoadStandardCharges();
                int ind = Integer.parseInt(request.getParameter("ind"));
                List codeList1 = (List) session.getAttribute("llcommonList");
                retailStandard = (LCLColoadStandardCharges) codeList1.get(ind);
                session.setAttribute("coStandardCharges", retailStandard);
                // forwardName="agssEdit";
                session.setAttribute("getEdit", "getEdit");

            }
            if (lCLColoadMaster != null) {
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
                programid = (String) session.getAttribute("processinfoforcoLoad");
                String recordid = "";
                if (lCLColoadMaster.getId() != null) {
                    recordid = lCLColoadMaster.getId().toString();
                }

                String editstatus = "startedited";
                String deletestatus = "startdeleted";

                ProcessInfo processinfoobj = processinfoDAO.findById(
                        lCLColoadMaster.getId(), recordid, deletestatus,
                        editstatus);

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

                session.setAttribute("addlclColoadMaster", lCLColoadMaster);

                forwardName = "lclFrame";
                if (session.getAttribute("setLCLColoadTabEnable") != null) {
                    session.removeAttribute("setLCLColoadTabEnable");
                }
                //
				/*if (session.getAttribute("lcldefaultRate") != null) {
                session.removeAttribute("lcldefaultRate");
                }
                PortsDAO portDAO = new PortsDAO();
                Ports ports = portDAO.findById(lCLColoadMaster
                .getDestinationPort().getId());
                String defaultRate = "";
                if (ports.getLclPortConfigSet() != null) {
                Iterator iter = ports.getLclPortConfigSet().iterator();
                while (iter.hasNext()) {
                LCLPortConfiguration lcl = (LCLPortConfiguration) iter
                .next();
                if (lcl.getDefaultRate() != null) {
                defaultRate = lcl.getDefaultRate();
                session.setAttribute("lcldefaultRate", defaultRate);
                break;
                }
                }
                }*/

                if (lCLColoadMaster.getCommodityCode() != null && lCLColoadMaster.getCommodityCode() != null) {
                    if (lCLColoadMaster.getCommodityCode().equals(
                            "000000")) {
                        session.setAttribute("setLCLColoadTabEnable", "enable");
                    }
                }

                if (lCLColoadMaster.getLclColoadStdChgSet() != null) {

                    Iterator iter = (Iterator) lCLColoadMaster.getLclColoadStdChgSet().iterator();
                    while (iter.hasNext()) {
                        LCLColoadStandardCharges airStd = (LCLColoadStandardCharges) iter.next();
                        airStdList.add(airStd);
                    }

                }

                if (lCLColoadMaster.getLclColoadDocumentSet() != null) {

                    Iterator iter = (Iterator) lCLColoadMaster.getLclColoadDocumentSet().iterator();
                    while (iter.hasNext()) {
                        LCLColoadDocumentCharges airdoc = (LCLColoadDocumentCharges) iter.next();
                        airStdList1.add(airdoc);
                    }

                }

                if (lCLColoadMaster.getLclColoadCommChgSet() != null) {

                    Iterator iter = (Iterator) lCLColoadMaster.getLclColoadCommChgSet().iterator();
                    while (iter.hasNext()) {
                        LCLColoadCommodityCharges airff = (LCLColoadCommodityCharges) iter.next();
                        airStdList3.add(airff);
                    }

                }
                if (lCLColoadMaster.getLclColoadDetailsSet() != null) {

                    Iterator iter = (Iterator) lCLColoadMaster.getLclColoadDetailsSet().iterator();

                    while (iter.hasNext()) {
                        lcldetails = (LCLColoadDetails) iter.next();
                        airStdList4.add(lcldetails);

                    }

                }
                if (lCLColoadMaster.getOriginTerminal() != null && !lCLColoadMaster.getOriginTerminal().equals("")) {
                    List unLocationList = unLocationDAO.findForManagement(lCLColoadMaster.getOriginTerminal(), tername);
                    if (unLocationList != null && unLocationList.size() > 0) {
                        unLocation = (UnLocation) unLocationList.get(0);
                        lCLColoadMaster.setOriginTerminalName(unLocation.getUnLocationName());
                    }
                }

                if (lCLColoadMaster.getDestinationPort() != null && !lCLColoadMaster.getDestinationPort().equals("")) {
                    List portsList = unLocationDAO.findForManagement(lCLColoadMaster.getDestinationPort(), null);
                    if (portsList != null && portsList.size() > 0) {
                        unLocation = (UnLocation) portsList.get(0);
                        lCLColoadMaster.setDestinationPortName(unLocation.getUnLocationName());
                    }
                }
                if (lCLColoadMaster.getCommodityCode() != null && !lCLColoadMaster.getCommodityCode().equals("")) {
                    List comList = genericCodeDAO.findForGenericCode(lCLColoadMaster.getCommodityCode());
                    if (comList != null && comList.size() > 0) {
                        gen = (GenericCode) comList.get(0);
                        lCLColoadMaster.setCommodityCodeName(gen.getCodedesc());
                    }
                }
                session.setAttribute("lclcoloaddetails", lcldetails);
                session.setAttribute("lclcoloadcssList", airStdList3);
                session.setAttribute("lcldocChargesAdd", airStdList1);
                session.setAttribute("coagssAdd", airStdList);
                session.setAttribute("lCLColoadMaster", lCLColoadMaster);

                session.setAttribute("lclcoloadrate", "edit");

                if (session.getAttribute("includedList") != null) {
                    session.removeAttribute("includedList");
                }
                if (session.getAttribute("applylclcoloadcharges") != null) {
                    session.removeAttribute("applylclcoloadcharges");
                }
                session.setAttribute("editrecord", "edit");
            }
            forwardName = "lclFrame";
        } //		 delete standard charges
        else if (request.getParameter("standardId") != null && !request.getParameter("standardId").trim().equals("")) {
            if (request.getParameter("parentId") != null && !request.getParameter("parentId").equals("")) {
                if (session.getAttribute("llcommonList") != null) {
                    session.removeAttribute("llcommonList");

                }
                int standardid = Integer.parseInt(request.getParameter("standardId"));
                lCLColoadMaster = lCLColoadMasterDAO.findById(Integer.parseInt(request.getParameter("parentId")));
                Set<LCLColoadStandardCharges> setLCLColoadStandardCharges = new HashSet();
                setLCLColoadStandardCharges.addAll(lCLColoadMaster.getLclColoadStdChgSet());
                for (Iterator iter = setLCLColoadStandardCharges.iterator(); iter.hasNext();) {
                    LCLColoadStandardCharges lCLColoadStandardCharges = (LCLColoadStandardCharges) iter.next();
                    if (lCLColoadStandardCharges.getId() != null && lCLColoadStandardCharges.getId().equals(standardid)) {
                        lCLColoadMaster.getLclColoadStdChgSet().remove(lCLColoadStandardCharges);
                    }
                }
                commonStandardList = new ArrayList();
                commonStandardList.addAll(lCLColoadMaster.getLclColoadStdChgSet());
                session.setAttribute("llcommonList", commonStandardList);
            }
            forwardName = "searchlclcoload";
        } // ---------------Bottom line rates calculation-----------
        else if (request.getParameter("rates") != null && !request.getParameter("rates").equals("")) {
            lCLColoadMaster = lCLColoadMasterDAO.findById(Integer.parseInt(request.getParameter("rates")));
            LCLColoadDetails lclColoadDetails = new LCLColoadDetails();
            LCLColoadCommodityCharges lCLColoadCommodityCharges = new LCLColoadCommodityCharges();
            double rate = 0.0; // ----------rate/1000kgs
            double rcbm = 0.0; // -------rate/cbm
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
            if (lCLColoadMaster.getLclColoadDetailsSet() != null) {
                Iterator iter = (Iterator) lCLColoadMaster.getLclColoadDetailsSet().iterator();
                // inside="+scharge.getRetailWeightRangeSet().size());
                while (iter.hasNext()) {
                    lclColoadDetails = (LCLColoadDetails) iter.next();

                    if (lclColoadDetails.getMetric1000kg() != null) {
                        rate += lclColoadDetails.getMetric1000kg();// 1000kg
                    }
                    if (lclColoadDetails.getMetricCbm() != null) {
                        rcbm += lclColoadDetails.getMetricCbm();// cbm
                    }
                    if (lclColoadDetails.getEnglishCft() != null) {
                        cft += lclColoadDetails.getEnglishCft();

                    }
                    if (lclColoadDetails.getEnglish100lb() != null) {
                        lbs += lclColoadDetails.getEnglish100lb();
                    }
                }
            }
            if (lCLColoadMaster.getLclColoadCommChgSet() != null) {

                Iterator iter = (Iterator) lCLColoadMaster.getLclColoadCommChgSet().iterator();
                while (iter.hasNext()) {
                    lCLColoadCommodityCharges = (LCLColoadCommodityCharges) iter.next();

                    if (lCLColoadCommodityCharges.getStandard() != null && !lCLColoadCommodityCharges.getStandard().equals(
                            "") && lCLColoadCommodityCharges.getStandard().equals(
                            "Y")) {
                        if (lCLColoadCommodityCharges.getAmtPer1000kg() != null) {
                            amtkg += lCLColoadCommodityCharges.getAmtPer1000kg();
                        }
                        if (lCLColoadCommodityCharges.getAmtPerCbm() != null) {

                            cbm += lCLColoadCommodityCharges.getAmtPerCbm();

                        }
                        if (lCLColoadCommodityCharges.getAmtPer100lbs() != null) {
                            slbs += lCLColoadCommodityCharges.getAmtPer100lbs();
                        }
                        if (lCLColoadCommodityCharges.getAmtPerCft() != null) {
                            scft += lCLColoadCommodityCharges.getAmtPerCft();
                        }
                    }

                }
            }
            res1 = rate + amtkg;
            res2 = rcbm + cbm;
            res3 = lbs + slbs;
            res4 = scft + cft;

            session.setAttribute("LclcoloadrateforKg", res1);
            session.setAttribute("LclcoloadrateforCbm", res2);
            session.setAttribute("LclcoloadrateforLbs", res3);
            session.setAttribute("LclcoloadrateforCft", res4);
            request.setAttribute("btl", "btl"); // forwarding to
            // manageairrates.jsp
            forwardName = "searchlclcoload";
        } else if (request.getParameter("param") != null) {

            if (session.getAttribute("searchlclColoadMaster") != null) {
                List list = new ArrayList();
                GenericCode genObj = genericCodeDAO.findById(11292);
                LCLColoadMaster retailRatesObj1 = (LCLColoadMaster) session.getAttribute("searchlclColoadMaster");
                list = dbutil.getCoLoadDetails(retailRatesObj1.getOriginTerminal(), retailRatesObj1.getDestinationPort(), genObj.getCode());
                if (list != null && list.size() > 0) {
                    lCLColoadMaster = (LCLColoadMaster) list.get(0);
                }
            }

            if (request.getParameter("param") != null && !request.getParameter("param").equals("")) {
                lCLColoadMaster = lCLColoadMasterDAO.findById(Integer.parseInt(request.getParameter("param")));
            }
            if (request.getParameter("ind") != null && !request.getParameter("ind").equals("")) {
                LCLColoadStandardCharges retailStandard = new LCLColoadStandardCharges();
                int ind = Integer.parseInt(request.getParameter("ind"));
                List codeList1 = (List) session.getAttribute("llcommonList");
                retailStandard = (LCLColoadStandardCharges) codeList1.get(ind);
                session.setAttribute("coStandardCharges", retailStandard);
                // forwardName="agssEdit";
                session.setAttribute("getEdit", "getEdit");

            }
            if (lCLColoadMaster != null) {
                String view = "3";
                session.setAttribute("view", view);
                session.setAttribute("lCLColoadMaster", lCLColoadMaster);
                if (session.getAttribute("setLCLColoadTabEnable") != null) {
                    session.removeAttribute("setLCLColoadTabEnable");
                }

                if (lCLColoadMaster.getCommodityCode() != null && lCLColoadMaster.getCommodityCode() != null) {

                    if (lCLColoadMaster.getCommodityCode().equals(
                            "000000")) {
                        session.setAttribute("setLCLColoadTabEnable", "enable");
                    }
                }

                if (lCLColoadMaster.getLclColoadStdChgSet() != null) {

                    Iterator iter = (Iterator) lCLColoadMaster.getLclColoadStdChgSet().iterator();
                    while (iter.hasNext()) {
                        LCLColoadStandardCharges airStd = (LCLColoadStandardCharges) iter.next();
                        airStdList.add(airStd);
                    }

                }

                if (lCLColoadMaster.getLclColoadDocumentSet() != null) {

                    Iterator iter = (Iterator) lCLColoadMaster.getLclColoadDocumentSet().iterator();
                    while (iter.hasNext()) {
                        LCLColoadDocumentCharges airdoc = (LCLColoadDocumentCharges) iter.next();
                        airStdList1.add(airdoc);
                    }

                }

                if (lCLColoadMaster.getLclColoadCommChgSet() != null) {

                    Iterator iter = (Iterator) lCLColoadMaster.getLclColoadCommChgSet().iterator();
                    while (iter.hasNext()) {
                        LCLColoadCommodityCharges airff = (LCLColoadCommodityCharges) iter.next();
                        airStdList3.add(airff);
                    }

                }
                if (lCLColoadMaster.getLclColoadDetailsSet() != null) {

                    Iterator iter = (Iterator) lCLColoadMaster.getLclColoadDetailsSet().iterator();
                    while (iter.hasNext()) {
                        LCLColoadDetails airff = (LCLColoadDetails) iter.next();
                        session.setAttribute("lclcoloaddetails", airff);
                    // airStdList4.add(airff);
                    }

                }
                if (lCLColoadMaster.getOriginTerminal() != null && !lCLColoadMaster.getOriginTerminal().equals("")) {
                    List unLocationList = unLocationDAO.findForManagement(lCLColoadMaster.getOriginTerminal(), null);
                    if (unLocationList != null && unLocationList.size() > 0) {
                        unLocation = (UnLocation) unLocationList.get(0);
                        lCLColoadMaster.setOriginTerminalName(unLocation.getUnLocationName());
                    }
                }

                if (lCLColoadMaster.getDestinationPort() != null && !lCLColoadMaster.getDestinationPort().equals("")) {
                    List portsList = unLocationDAO.findForManagement(lCLColoadMaster.getDestinationPort(), null);
                    if (portsList != null && portsList.size() > 0) {
                        unLocation = (UnLocation) portsList.get(0);
                        lCLColoadMaster.setDestinationPortName(unLocation.getUnLocationName());
                    }
                }
                if (lCLColoadMaster.getCommodityCode() != null && !lCLColoadMaster.getCommodityCode().equals("")) {
                    List comList = genericCodeDAO.findForGenericCode(lCLColoadMaster.getCommodityCode());
                    if (comList != null && comList.size() > 0) {
                        gen = (GenericCode) comList.get(0);
                        lCLColoadMaster.setCommodityCodeName(gen.getCodedesc());
                    }
                }

                session.setAttribute("lclcoloadcssList", airStdList3);
                session.setAttribute("lcldocChargesAdd", airStdList1);
                session.setAttribute("coagssAdd", airStdList);
                session.setAttribute("addlclColoadMaster", lCLColoadMaster);

                if (session.getAttribute("applylclcoloadcharges") != null) {
                    session.removeAttribute("applylclcoloadcharges");
                }
                if (session.getAttribute("includedList") != null) {
                    session.removeAttribute("includedList");
                }
                session.setAttribute("editrecord", "edit");
                session.setAttribute("lclcoloadrate", "edit");
                forwardName = "lclFrame";
                if (session.getAttribute("message") != null) {
                    session.removeAttribute("message");

                }
            }
            session.setAttribute("editrecord", "edit");
        }
        session.setAttribute("LclbuttonValue", buttonValue);
        // ---------------------------------------------------------------------------------------------------------
        if (buttonValue != null && buttonValue.equals("clear")) {

            if (session.getAttribute("lclcollaps") != null) {
                session.removeAttribute("lclcollaps");
            }

            if (session.getAttribute("lcldefaultRate") != null) {
                session.removeAttribute("lcldefaultRate");
            }
            if (session.getAttribute("searchlclColoadMaster") != null) {
                session.removeAttribute("searchlclColoadMaster");
            }
            if (session.getAttribute("lCLColoadMaster") != null) {
                session.removeAttribute("lCLColoadMaster");
            }
            if (session.getAttribute("lcldocChargesAdd") != null) {
                session.removeAttribute("lcldocChargesAdd");
            }
            if (session.getAttribute("lclcoloaddetails") != null) {
                session.removeAttribute("lclcoloaddetails");
            }
            if (session.getAttribute("coStandardCharges") != null) {
                session.removeAttribute("coStandardCharges");
            }

            if (session.getAttribute("cssList") != null) {
                session.removeAttribute("cssList");
            }

            if (session.getAttribute("applylclcoloadcharges") != null) {
                session.removeAttribute("applylclcoloadcharges");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("coStandardCharges") != null) {

                session.removeAttribute("coStandardCharges");
            }

            if (session.getAttribute("lclmessage") != null) {
                session.removeAttribute("lclmessage");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("addlclColoadMaster") != null) {
                session.removeAttribute("addlclColoadMaster");
            }
            if (session.getAttribute("llcommonList") != null) {
                session.removeAttribute("llcommonList");
            }
            if (session.getAttribute("llnoncommonList") != null) {
                session.removeAttribute("llnoncommonList");
            }
            if (session.getAttribute("lclcommonListCaps") != null) {
                session.removeAttribute("lclcommonListCaps");
            }

            if (session.getAttribute("lclcoloadRatescaption") != null) {
                session.removeAttribute("lclcoloadRatescaption");
            }
            if (session.getAttribute("message") != null) {
                session.removeAttribute("message");
            }

            forwardName = "searchlclframe";
        }

        return mapping.findForward(forwardName);
    }
}