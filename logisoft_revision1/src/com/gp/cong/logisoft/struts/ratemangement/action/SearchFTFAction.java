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

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.AirRatesBean;
import com.gp.cong.logisoft.domain.FTFCommodityCharges;
import com.gp.cong.logisoft.domain.FTFDetails;
import com.gp.cong.logisoft.domain.FTFDocumentCharges;
import com.gp.cong.logisoft.domain.FTFMaster;
import com.gp.cong.logisoft.domain.FTFStandardCharges;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.ProcessInfo;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.FTFMasterDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.SearchFTFForm;

public class SearchFTFAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        SearchFTFForm searchFTFForm = (SearchFTFForm) form;// TODO
        // Auto-generated
        // method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = searchFTFForm.getButtonValue();
        String match = searchFTFForm.getMatch();
        String search = searchFTFForm.getSearch();
        // String search=searchFTFForm.getSearch();
        FTFDetails ftfdetails = null;
        List commonList = new ArrayList();
        String loginName = "";
        String message = "";
        String msg = "";
        String forwardName = "";
        List ftfStdList = new ArrayList();
        List ftfStdList1 = new ArrayList();
        List ftfStdList3 = new ArrayList();
        List ftfStdList4 = new ArrayList();
        AirRatesBean airRatesBean = new AirRatesBean();
        FTFMasterDAO ftfMasterDAO = new FTFMasterDAO();
        UnLocationDAO unLocationDAO = new UnLocationDAO();
        UnLocation unLocation = null;

        FTFMaster ftfMaster = new FTFMaster();
        List searchftfList = new ArrayList();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        String terminalNumber = searchFTFForm.getTerminalNumber();
        String tername = searchFTFForm.getTerminalName();
        String destPort = searchFTFForm.getDestSheduleNumber();
        String portName = searchFTFForm.getDestAirportname();
        String comCode = searchFTFForm.getComCode();
        // PortsDAO portsDAO = new PortsDAO();
        GenericCode genericCode = null;
        // airRatesBean.setCommon(common);
        if (session.getAttribute("searchftfMaster") != null) {
            ftfMaster = (FTFMaster) session.getAttribute("searchftfMaster");
            terminalNumber = ftfMaster.getOrgTerminal();
            destPort = ftfMaster.getDestPort();
        } else {
            ftfMaster = new FTFMaster();
        }
        if (buttonValue != null && buttonValue.equals("search")) {
            if (terminalNumber != null && !terminalNumber.equals("")) {
                List unLocationList = unLocationDAO.findForManagement(
                        terminalNumber, tername);
                if (unLocationList != null && unLocationList.size() > 0) {
                    unLocation = (UnLocation) unLocationList.get(0);
                    ftfMaster.setOrgTerminal(unLocation.getUnLocationCode());
                    ftfMaster.setOrgTerminalName(unLocation.getUnLocationName());
                }
            } else if (tername != null && !tername.equals("")) {
                String stringTokens[] = StringUtils.splitPreserveAllTokens(
                        tername, '-');
                if (stringTokens != null && stringTokens.length > 1) {
                    tername = stringTokens[0];
                    terminalNumber = stringTokens[1];
                }
                List terminal = unLocationDAO.findForManagement(terminalNumber,
                        tername);
                if (terminal.size() > 0) {
                    unLocation = (UnLocation) terminal.get(0);
                }
                if (unLocation != null) {
                    ftfMaster.setOrgTerminal(unLocation.getUnLocationCode());
                    ftfMaster.setOrgTerminalName(unLocation.getUnLocationName());
                }
            }
            if (destPort != null && !destPort.equals("")) {
                List portsList = unLocationDAO.findForManagement(destPort, null);
                if (portsList != null && portsList.size() > 0) {
                    unLocation = (UnLocation) portsList.get(0);
                    ftfMaster.setDestPort(unLocation.getUnLocationCode());
                    ftfMaster.setDestPortName(unLocation.getUnLocationName());
                }
            } else if (portName != null && !portName.equals("")) {
                String stringTokens[] = StringUtils.splitPreserveAllTokens(
                        portName, '-');
                if (stringTokens != null && stringTokens.length > 1) {
                    portName = stringTokens[0];
                    destPort = stringTokens[1];
                }
                List portsList = unLocationDAO.findForManagement(destPort,
                        portName);
                if (portsList != null && portsList.size() > 0) {
                    unLocation = (UnLocation) portsList.get(0);
                    ftfMaster.setDestPort(unLocation.getUnLocationCode());
                }
            }
            if (comCode != null && !comCode.equals("") && !comCode.equals("%")) {
                List comList = genericCodeDAO.findForGenericCode(comCode);
                if (comList != null && comList.size() > 0) {
                    genericCode = (GenericCode) comList.get(0);
                    ftfMaster.setComCode(genericCode.getCode());
                    ftfMaster.setComCodeName(genericCode.getCodedesc());
                    if (genericCode.getCode().equals("000000")) {
                        session.setAttribute("setftfTabEnable", "enable");
                    } else {
                        session.removeAttribute("setftfTabEnable");
                    }
                }
            }

            session.setAttribute("searchftfMaster", ftfMaster);
            if (request.getAttribute("message") == null && (search != null && search.equals("get"))) {
                String defaultRate = "";
                List CommonListSTD = new ArrayList();
                if (terminalNumber != null && destPort != null && (comCode == null || comCode.equals(""))) {
                    searchftfList = ftfMasterDAO.findForSearchftfRatesmatch(
                            terminalNumber, destPort, comCode, match);

                    for (int i = 0; i < searchftfList.size(); i++) {
                        FTFMaster ftfMaster2 = (FTFMaster) searchftfList.get(i);
                        if (ftfMaster2.getComCode() != null && ftfMaster2.getComCode() != null && ftfMaster2.getComCode().equals("000000")) {
                            CommonListSTD.add(ftfMaster2);
                            searchftfList.remove(i);
                            break;
                        }
                    }

                    for (int i = 0; i < CommonListSTD.size(); i++) {
                        FTFMaster ftfMaster2 = (FTFMaster) CommonListSTD.get(i);
                        boolean flag = true;
                        if (ftfMaster2.getComCode() != null && ftfMaster2.getComCode() != null && ftfMaster2.getComCode().equals("000000")) {
                            FTFStandardCharges retailStdChild = new FTFStandardCharges();
                            if (ftfMaster2.getFtfStdChgSet() != null) {
                                Iterator iter = (Iterator) ftfMaster2.getFtfStdChgSet().iterator();
                                while (iter.hasNext()) {
                                    retailStdChild = (FTFStandardCharges) iter.next();
                                    if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals("Y")) {
                                        commonList.add(retailStdChild);
                                    }
                                    flag = false;
                                }
                            }
                            if (!flag) {
                                session.setAttribute("ffcommonListCaps",
                                        "Common Accessorial Charges (Standard Only)");
                            } else {
                                session.setAttribute("ffcommonListCaps",
                                        "No Common Accessorial Added");
                            }

                        }

                    }
                } else if (terminalNumber != null && destPort != null && comCode != null) {
                    searchftfList = ftfMasterDAO.findForSearchftfRatesstarts(
                            terminalNumber, destPort, comCode, match);
                    boolean com = false;
                    boolean flag = true;
                    for (int i = 0; i < searchftfList.size(); i++) {
                        FTFMaster ftfMaster2 = (FTFMaster) searchftfList.get(i);
                        if (ftfMaster2.getComCode() != null && ftfMaster2.getComCode() != null && ftfMaster2.getComCode().equals("000000")) {
                            CommonListSTD.add(ftfMaster2);
                            searchftfList.remove(i);
                            com = true;
                            break;
                        }
                    }
                    if (!com) {
                        CommonListSTD = ftfMasterDAO.findForSearchftfRatesmatch(terminalNumber,
                                destPort, "000000", match);
                    }
                    for (int i = 0; i < CommonListSTD.size(); i++) {
                        FTFMaster ftfMaster2 = (FTFMaster) CommonListSTD.get(i);
                        if (ftfMaster2.getComCode() != null && ftfMaster2.getComCode() != null && ftfMaster2.getComCode().equals("000000")) {
                            FTFStandardCharges retailStdChild = new FTFStandardCharges();
                            if (ftfMaster2.getFtfStdChgSet() != null) {
                                Iterator iter = (Iterator) ftfMaster2.getFtfStdChgSet().iterator();
                                while (iter.hasNext()) {
                                    retailStdChild = (FTFStandardCharges) iter.next();
                                    if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals("Y")) {
                                        commonList.add(retailStdChild);
                                    }
                                    flag = false;
                                }
                            }
                            if (!flag) {
                                session.setAttribute("ffcommonListCaps",
                                        "Common Accessorial Charges (Standard Only)");
                            } else {
                                session.setAttribute("ffcommonListCaps",
                                        "No Common Accessorial Added");
                            }
                        }
                    }
                }

                session.setAttribute("ftfcollaps", "ftfcollaps");
                session.setAttribute("ftfcommonList", commonList);

                session.setAttribute("ftfnoncommonList", searchftfList);
                session.setAttribute("ftfRatescaption", "Ocean Freight Rates");
            }

            forwardName = "searchftfframe";
        }
        if (buttonValue != null && buttonValue.equals("searchall")) {

            if (terminalNumber != null && destPort != null) {

                searchftfList = ftfMasterDAO.findForSearchftfRatesmatch(
                        terminalNumber, destPort, "000000", match);

                for (int i = 0; i < searchftfList.size(); i++) {

                    FTFMaster lclparentObject = (FTFMaster) searchftfList.get(i);
                    if (lclparentObject.getFtfStdChgSet() != null) {
                        FTFStandardCharges retailStdChild = new FTFStandardCharges();
                        Iterator iter = (Iterator) lclparentObject.getFtfStdChgSet().iterator();
                        while (iter.hasNext()) {
                            retailStdChild = (FTFStandardCharges) iter.next();
                            commonList.add(retailStdChild);
                        }
                    }
                }
                session.setAttribute("ftfcommonList", commonList);
                session.setAttribute("ffcommonListCaps",
                        "Common Accessorial Charges (All)");

            }
            forwardName = "searchftfframe";
        }// search All
        if (buttonValue != null && buttonValue.equals("searchStarndard")) {
            if (terminalNumber != null && destPort != null) {
                searchftfList = ftfMasterDAO.findForSearchftfRatesmatch(
                        terminalNumber, destPort, "000000", match);
                for (int i = 0; i < searchftfList.size(); i++) {
                    FTFMaster lclparentObject = (FTFMaster) searchftfList.get(i);
                    if (lclparentObject.getFtfStdChgSet() != null) {
                        FTFStandardCharges retailStdChild = new FTFStandardCharges();
                        Iterator iter = (Iterator) lclparentObject.getFtfStdChgSet().iterator();
                        while (iter.hasNext()) {
                            retailStdChild = (FTFStandardCharges) iter.next();
                            if (retailStdChild.getStandard() != null && retailStdChild.getStandard().equals("Y")) {
                                commonList.add(retailStdChild);
                            }
                        }
                    }
                    session.setAttribute("ftfcommonList", commonList);
                    session.setAttribute("ffcommonListCaps",
                            "Common Accessorial Charges (Standard Only)");
                }
                forwardName = "searchftfframe";
            }
        } // --------------------------------------------------------------------------------------
        else if (request.getParameter("standardId") != null && !request.getParameter("standardId").trim().equals("")) {
            if (request.getParameter("parentId") != null && !request.getParameter("parentId").equals("")) {
                if (session.getAttribute("ftfcommonList") != null) {
                    session.removeAttribute("ftfcommonList");

                }
                int standardid = Integer.parseInt(request.getParameter("standardId"));
                ftfMaster = ftfMasterDAO.findById(Integer.parseInt(request.getParameter("parentId")));
                Set<FTFStandardCharges> setFTFStandardCharges = new HashSet();
                setFTFStandardCharges.addAll(ftfMaster.getFtfStdChgSet());
                for (Iterator iter = setFTFStandardCharges.iterator(); iter.hasNext();) {
                    FTFStandardCharges fTFStandardCharges = (FTFStandardCharges) iter.next();
                    if (fTFStandardCharges.getId() != null && fTFStandardCharges.getId().equals(standardid)) {
                        ftfMaster.getFtfStdChgSet().remove(fTFStandardCharges);
                    }
                }
                List commonStandardList = new ArrayList();
                commonStandardList.addAll(ftfMaster.getFtfStdChgSet());
                session.setAttribute("ftfcommonList", commonStandardList);
            }
            forwardName = "searchftf";
        }
        if (request.getParameter("paramid") != null || (buttonValue != null && buttonValue.equals("paramid")) || (buttonValue != null && buttonValue.equals("paramIdDoc"))) {
            ftfMaster = null;
            if (buttonValue != null && buttonValue.equals("paramid")) {
                if (session.getAttribute("searchftfMaster") != null) {
                    List list = new ArrayList();
                    GenericCode genObj = genericCodeDAO.findById(11292);
                    FTFMaster retailRatesObj1 = (FTFMaster) session.getAttribute("searchftfMaster");

                    list = ftfMasterDAO.findAllDetails(retailRatesObj1.getOrgTerminal(), retailRatesObj1.getDestPort(),
                            genObj.getCode());
                    if (list != null && list.size() > 0) {
                        ftfMaster = (FTFMaster) list.get(0);
                    }
                }
                session.setAttribute("getAddssEdit", "getEdit");
            }
            if (buttonValue != null && buttonValue.equals("paramIdDoc")) {
                if (session.getAttribute("searchftfMaster") != null) {
                    List list = new ArrayList();
                    GenericCode genObj = genericCodeDAO.findById(11292);
                    FTFMaster retailRatesObj1 = (FTFMaster) session.getAttribute("searchftfMaster");
                    list = ftfMasterDAO.findAllDetails(retailRatesObj1.getOrgTerminal(), retailRatesObj1.getDestPort(),
                            genObj.getCode());
                    if (list != null && list.size() > 0) {
                        ftfMaster = (FTFMaster) list.get(0);
                    }
                }
                session.setAttribute("getDocEdit", "getEdit");
            }
            if (request.getParameter("paramid") != null && !request.getParameter("paramid").equals("")) {
                ftfMaster = ftfMasterDAO.findById(Integer.parseInt(request.getParameter("paramid")));
            }
            if (request.getParameter("ind") != null) {
                FTFStandardCharges retailStandard = new FTFStandardCharges();
                int ind = Integer.parseInt(request.getParameter("ind"));
                List codeList1 = (List) session.getAttribute("ftfcommonList");
                retailStandard = (FTFStandardCharges) codeList1.get(ind);
                session.setAttribute("ftfStandardCharges", retailStandard);
                session.setAttribute("getFtfEdit", "getEdit");
            }
            if (ftfMaster != null) {
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
                programid = (String) session.getAttribute("processinfoforftf");
                String recordid = "";

                if (ftfMaster.getId() != null && !ftfMaster.getId().equals("")) {
                    recordid = ftfMaster.getId().toString();
                }

                String editstatus = "startedited";
                String deletestatus = "startdeleted";
                ProcessInfo processinfoobj = processinfoDAO.findById(Integer.parseInt(programid), recordid, deletestatus,
                        editstatus);
                if (processinfoobj != null) {

                    String view = "3";
                    User loginuser = user1.findById(processinfoobj.getUserid());
                    loginName = loginuser.getLoginName();
                    msg = "This record is being used by ";
                    message = msg + loginName;
                    session.setAttribute("message", message);
                    session.setAttribute("view", view);
                    forwardName = "ftfFrame";
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

                session.setAttribute("addftfMaster", ftfMaster);

                forwardName = "ftfFrame";
                if (session.getAttribute("setftfTabEnable") != null) {
                    session.removeAttribute("setftfTabEnable");
                }
                //
				/*
                 * String defaultRate = ""; if
                 * (session.getAttribute("ftfdefaultRate") != null) {
                 * session.removeAttribute("ftfdefaultRate"); }
                 *
                 * if (ftfMaster.getDestinationPort() != null) { Ports ports =
                 * portsDAO.findById(ftfMaster .getDestinationPort().getId());
                 *
                 * if (ports.getLclPortConfigSet() != null) { Iterator iter =
                 * ports.getLclPortConfigSet().iterator(); while
                 * (iter.hasNext()) { LCLPortConfiguration lcl =
                 * (LCLPortConfiguration) iter .next(); if (lcl.getDefaultRate() !=
                 * null) { defaultRate = lcl.getDefaultRate(); System.out
                 * .println("lcl.getDefaultRate()$$$$$$$$$$$$$" +
                 * lcl.getDefaultRate()); session.setAttribute("ftfdefaultRate",
                 * defaultRate); break; } } } }
                 */
                //
                if (ftfMaster.getComCode() != null && ftfMaster.getComCode() != null) {
                    if (ftfMaster.getComCode().equals("000000")) {
                        session.setAttribute("setftfTabEnable", "enable");
                    }
                }

                if (ftfMaster.getFtfStdChgSet() != null) {

                    Iterator iter = (Iterator) ftfMaster.getFtfStdChgSet().iterator();
                    while (iter.hasNext()) {
                        FTFStandardCharges ftfStd = (FTFStandardCharges) iter.next();
                        ftfStdList.add(ftfStd);
                    }

                }

                if (ftfMaster.getFtfDocumentSet() != null) {

                    Iterator iter = (Iterator) ftfMaster.getFtfDocumentSet().iterator();
                    while (iter.hasNext()) {
                        FTFDocumentCharges airdoc = (FTFDocumentCharges) iter.next();
                        ftfStdList1.add(airdoc);
                    }

                }

                if (ftfMaster.getFtfCommChgSet() != null) {

                    Iterator iter = (Iterator) ftfMaster.getFtfCommChgSet().iterator();
                    while (iter.hasNext()) {
                        FTFCommodityCharges airff = (FTFCommodityCharges) iter.next();
                        ftfStdList3.add(airff);
                    }

                }
                if (ftfMaster.getFtfDetailsSet() != null) {

                    Iterator iter = (Iterator) ftfMaster.getFtfDetailsSet().iterator();
                    while (iter.hasNext()) {
                        ftfdetails = (FTFDetails) iter.next();
                        ftfStdList4.add(ftfdetails);

                    }

                }
                session.setAttribute("ftfdetails", ftfdetails);
                session.setAttribute("ftfcssList", ftfStdList3);
                session.setAttribute("ftfdocChargesAdd", ftfStdList1);
                session.setAttribute("ftfagssAdd", ftfStdList);
                session.setAttribute("ftfMaster", ftfMaster);
                session.setAttribute("addftfMaster", ftfMaster);
            }

            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("applyftfcharges") != null) {
                session.removeAttribute("applyftfcharges");
            }
            session.setAttribute("ftfeditrecord", "edit");
            forwardName = "ftfFrame";
            session.setAttribute("ftfrate", "edit");
        } // ---------------Bottom line rates calculation-----------
        else if (request.getParameter("rates") != null && !request.getParameter("rates").equals("")) {
            ftfMaster = ftfMasterDAO.findById(Integer.parseInt(request.getParameter("rates")));
            FTFDetails ftfDetails = new FTFDetails();
            FTFCommodityCharges ftfCommodityCharges = new FTFCommodityCharges();
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
            if (ftfMaster.getFtfDetailsSet() != null) {
                Iterator iter = (Iterator) ftfMaster.getFtfDetailsSet().iterator();
                while (iter.hasNext()) {
                    ftfDetails = (FTFDetails) iter.next();

                    if (ftfDetails.getMetric1000kg() != null) {
                        rate += ftfDetails.getMetric1000kg();// 1000kg
                    }
                    if (ftfDetails.getMetricCbm() != null) {
                        rcbm += ftfDetails.getMetricCbm();// cbm
                    }
                    if (ftfDetails.getEnglishCft() != null) {
                        cft += ftfDetails.getEnglishCft();

                    }
                    if (ftfDetails.getEnglish100lb() != null) {
                        lbs += ftfDetails.getEnglish100lb();
                    }
                }
            }
            if (ftfMaster.getFtfCommChgSet() != null) {

                Iterator iter = (Iterator) ftfMaster.getFtfCommChgSet().iterator();
                while (iter.hasNext()) {
                    ftfCommodityCharges = (FTFCommodityCharges) iter.next();

                    if (ftfCommodityCharges.getStandard() != null && !ftfCommodityCharges.getStandard().equals("") && ftfCommodityCharges.getStandard().equals("Y")) {
                        if (ftfCommodityCharges.getAmtPer1000kg() != null) {
                            amtkg += ftfCommodityCharges.getAmtPer1000kg();
                        }
                        if (ftfCommodityCharges.getAmtPerCbm() != null) {

                            cbm += ftfCommodityCharges.getAmtPerCbm();

                        }
                        if (ftfCommodityCharges.getAmtPer100lbs() != null) {
                            slbs += ftfCommodityCharges.getAmtPer100lbs();
                        }
                        if (ftfCommodityCharges.getAmtPerCft() != null) {
                            scft += ftfCommodityCharges.getAmtPerCft();
                        }
                    }

                }
            }
            res1 = rate + amtkg;
            res2 = rcbm + cbm;
            res3 = lbs + slbs;
            res4 = scft + cft;

            session.setAttribute("FTFrateforKg", res1);
            session.setAttribute("FTFrateforCbm", res2);
            session.setAttribute("FTFrateforLbs", res3);
            session.setAttribute("FTFrateforCft", res4);
            request.setAttribute("btl", "btl"); // forwarding to
            // manageairrates.jsp
            forwardName = "searchftf";
        } else if (request.getParameter("param") != null) {
            ftfMaster = ftfMasterDAO.findById(Integer.parseInt(request.getParameter("param")));
            if (request.getParameter("ind") != null) {
                FTFStandardCharges retailStandard = new FTFStandardCharges();
                int ind = Integer.parseInt(request.getParameter("ind"));
                List codeList1 = (List) session.getAttribute("ftfcommonList");
                retailStandard = (FTFStandardCharges) codeList1.get(ind);
                session.setAttribute("ftfStandardCharges", retailStandard);
                session.setAttribute("getFtfEdit", "getEdit");
            }
            String view = "3";
            session.setAttribute("view", view);
            session.setAttribute("ftfMaster", ftfMaster);
            if (session.getAttribute("setftfTabEnable") != null) {
                session.removeAttribute("setftfTabEnable");
            }

            if (ftfMaster.getComCode() != null && ftfMaster.getComCode() != null && !ftfMaster.getComCode().equals("") && !ftfMaster.getComCode().equals("")) {

                if (ftfMaster.getComCode().equals("000000")) {
                    session.setAttribute("setftfTabEnable", "enable");
                }
            }
            /*
             * String defaultRate = ""; if
             * (session.getAttribute("ftfdefaultRate") != null) {
             * session.removeAttribute("ftfdefaultRate"); }
             *
             * Ports ports = portsDAO.findById(ftfMaster.getDestinationPort()
             * .getId()); if (ports.getLclPortConfigSet() != null) { Iterator
             * iter = ports.getLclPortConfigSet().iterator(); while
             * (iter.hasNext()) { LCLPortConfiguration lcl =
             * (LCLPortConfiguration) iter .next(); if (lcl.getDefaultRate() !=
             * null) { defaultRate = lcl.getDefaultRate();
             * session.setAttribute("ftfdefaultRate", defaultRate); break; } } }
             */

            if (ftfMaster.getFtfStdChgSet() != null) {

                Iterator iter = (Iterator) ftfMaster.getFtfStdChgSet().iterator();
                while (iter.hasNext()) {
                    FTFStandardCharges ftfStd = (FTFStandardCharges) iter.next();
                    ftfStdList.add(ftfStd);
                }

            }

            if (ftfMaster.getFtfDocumentSet() != null) {

                Iterator iter = (Iterator) ftfMaster.getFtfDocumentSet().iterator();
                while (iter.hasNext()) {
                    FTFDocumentCharges ftfdoc = (FTFDocumentCharges) iter.next();
                    ftfStdList1.add(ftfdoc);
                }

            }

            if (ftfMaster.getFtfCommChgSet() != null) {

                Iterator iter = (Iterator) ftfMaster.getFtfCommChgSet().iterator();
                while (iter.hasNext()) {
                    FTFCommodityCharges ftfff = (FTFCommodityCharges) iter.next();
                    ftfStdList3.add(ftfff);
                }

            }
            if (ftfMaster.getFtfDetailsSet() != null) {

                Iterator iter = (Iterator) ftfMaster.getFtfDetailsSet().iterator();
                while (iter.hasNext()) {
                    FTFDetails ftfff = (FTFDetails) iter.next();
                    session.setAttribute("ftfdetails", ftfff);
                // airStdList4.add(airff);
                }

            }

            session.setAttribute("ftfcssList", ftfStdList3);
            session.setAttribute("ftfdocChargesAdd", ftfStdList1);
            session.setAttribute("ftfagssAdd", ftfStdList);
            session.setAttribute("addftfMaster", ftfMaster);
            session.setAttribute("ftfMaster", ftfMaster);
            if (session.getAttribute("applyftfcharges") != null) {
                session.removeAttribute("applyftfcharges");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            session.setAttribute("ftfeditrecord", "edit");
            forwardName = "ftfFrame";
            if (session.getAttribute("message") != null) {
                session.removeAttribute("message");

            }

            /*
             * if(buttonValue.equals("searchall") && buttonValue!=null &&
             * !buttonValue.equals("")) {
             * if(session.getAttribute("message")!=null) {
             * session.removeAttribute("message"); } //airRatesBean.set("");
             * //customerbean.setName(""); //customerbean.setAccountPrefix("");
             *
             * //session.setAttribute("customerbean", customerbean);
             * //forwardName="searchcustomer"; }
             */
            session.setAttribute("ftfrate", "edit");
        }
        // ---------------------------------------------------------------------------------------------------------
        if (buttonValue != null && buttonValue.equals("clear")) {
            if (session.getAttribute("ftfdefaultRate") != null) {
                session.removeAttribute("ftfdefaultRate");
            }
            if (session.getAttribute("ftfMaster") != null) {
                session.removeAttribute("ftfMaster");
            }
            if (session.getAttribute("ftfdocChargesAdd") != null) {
                session.removeAttribute("ftfdocChargesAdd");
            }
            if (session.getAttribute("ftfdetails") != null) {
                session.removeAttribute("ftfdetails");
            }
            if (session.getAttribute("ftfStandardCharges") != null) {
                session.removeAttribute("ftfStandardCharges");
            }

            if (session.getAttribute("ftfcssList") != null) {
                session.removeAttribute("ftfcssList");
            }

            if (session.getAttribute("applyftfcharges") != null) {
                session.removeAttribute("applyftfcharges");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("ftfStandardCharges") != null) {

                session.removeAttribute("ftfStandardCharges");
            }

            if (session.getAttribute("ftfmessage") != null) {
                session.removeAttribute("ftfmessage");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("addftfMaster") != null) {

                session.removeAttribute("addftfMaster");
            }
            if (session.getAttribute("searchftfMaster") != null) {

                session.removeAttribute("searchftfMaster");
            }
            if (session.getAttribute("ftfcollaps") != null) {

                session.removeAttribute("ftfcollaps");
            }
            if (session.getAttribute("serachftfdefaultRate") != null) {

                session.removeAttribute("serachftfdefaultRate");
            }
            if (session.getAttribute("ftfcommonList") != null) {
                session.removeAttribute("ftfcommonList");
            }
            if (session.getAttribute("ftfnoncommonList") != null) {
                session.removeAttribute("ftfnoncommonList");
            }

            if (session.getAttribute("ffcommonListCaps") != null) {
                session.removeAttribute("ffcommonListCaps");
            }
            if (session.getAttribute("ftfRatescaption") != null) {
                session.removeAttribute("ftfRatescaption");
            }

            if (session.getAttribute("getFtfEdit") != null) {
                session.removeAttribute("getFtfEdit");

            }
            if (session.getAttribute("listAgsCoitem") != null) {
                session.removeAttribute("listAgsCoitem");

            }
            if (session.getAttribute("listCoFtfitem") != null) {
                session.removeAttribute("listCoFtfitem");

            }
            if (session.getAttribute("getFtfEdit") != null) {
                session.removeAttribute("getFtfEdit");
            }
            if (session.getAttribute("getAddssEdit") != null) {
                session.removeAttribute("getAddssEdit");
            }
            if (session.getAttribute("getDocEdit") != null) {
                session.removeAttribute("getDocEdit");
            }
            if (session.getAttribute("ftfeditrecord") != null) {
                session.removeAttribute("ftfeditrecord");
            }
            forwardName = "searchftfframe";
        }

        session.setAttribute("buttonValue", buttonValue);

        return mapping.findForward(forwardName);

    }
}