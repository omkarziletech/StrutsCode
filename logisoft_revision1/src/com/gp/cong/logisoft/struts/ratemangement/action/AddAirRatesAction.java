/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.ratemangement.action;

import java.util.ArrayList;
import java.util.Date;
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

import com.gp.cong.hibernate.HibernateSessionFactory;
import com.gp.cong.logisoft.beans.AirRatesBean;
import com.gp.cong.logisoft.beans.NoteBean;
import com.gp.cong.logisoft.domain.AirCommodityCharges;
import com.gp.cong.logisoft.domain.AirFreightDocumentCharges;
import com.gp.cong.logisoft.domain.AirFreightFlightShedules;
import com.gp.cong.logisoft.domain.AirStandardCharges;
import com.gp.cong.logisoft.domain.AirWeightRangesRates;
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.AuditLogRecordAirRates;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.HistoryLogInterceptor;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.StandardCharges;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.StandardChargesDAO;
import com.gp.cong.logisoft.struts.ratemangement.form.AddAirRatesForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 03-06-2008
 * 
 * XDoclet definition:
 * @struts.action path="/addAirRates" name="addAirRatesForm" input="/jsps/ratemanagement/addAirRates.jsp" scope="request" validate="true"
 */
public class AddAirRatesAction extends Action {
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
        AddAirRatesForm addAirRatesForm = (AddAirRatesForm) form;// TODO Auto-generated method stub
        String buttonValue = addAirRatesForm.getButtonValue();
        HttpSession session = ((HttpServletRequest) request).getSession();
        String forwardName = "";
        List airWeightRangeList = new ArrayList();
        List airCommodityList = new ArrayList();
        Set airWeightRangeSet = new HashSet<AirStandardCharges>();
        Set airCommoditySet = new HashSet<AirCommodityCharges>();
        StandardCharges standardChrg = new StandardCharges();
        StandardChargesDAO standardChargesDAO = new StandardChargesDAO();
        DBUtil dbUtil = new DBUtil();
        HistoryLogInterceptor historyLogInterceptor = new HistoryLogInterceptor();
        if (session.getAttribute("standardCharges") != null) {
            standardChrg = (StandardCharges) session.getAttribute("standardCharges");
        }
        if (buttonValue.equals("edit")) {
            List docList = new ArrayList();
            Set documentCharges = new HashSet<AirFreightDocumentCharges>();
            if (session.getAttribute("docChargesAdd") != null) {
                docList = (List) session.getAttribute("docChargesAdd");
                for (int i = 0; i < docList.size(); i++) {
                    AirFreightDocumentCharges document1 = (AirFreightDocumentCharges) docList.get(i);
                    documentCharges.add(document1);
                }
            }
            standardChrg.setAirDocumentCharges(documentCharges);
            List agssList = new ArrayList();
            Set airStandard = new HashSet<AirStandardCharges>();
            if (session.getAttribute("agssAdd") != null) {

                agssList = (List) session.getAttribute("agssAdd");
                for (int j = 0; j < agssList.size(); j++) {
                    AirStandardCharges airStd = (AirStandardCharges) agssList.get(j);
                    historyLogInterceptor.setSessionFactory(HibernateSessionFactory.getSessionFactory());

                    if (airStd.getAirStdId() != null) {
                        User userId = null;
                        if (session.getAttribute("loginuser") != null) {
                            userId = (User) session.getAttribute("loginuser");
                        }
                        historyLogInterceptor.setUserName(userId.getLoginName());
                        historyLogInterceptor.setStandardId(standardChrg.getId());
                        boolean flag = historyLogInterceptor.onFlushDirty(airStd, airStd.getAirStdId(), null, null, null, null);
                        if (flag) {
                            airStd.setChangedDate(new Date());
                            airStd.setWhoChanged(userId.getLoginName());
                        }
                    }
                    airStandard.add(airStd);
                }
            }
            standardChrg.setAirStandardCharges(airStandard);
            List flightSheduleList = new ArrayList();
            Set flightSchedule = new HashSet<AirFreightFlightShedules>();

            if (session.getAttribute("flightShedulesAdd") != null) {
                flightSheduleList = (List) session.getAttribute("flightShedulesAdd");
                for (int i = 0; i < flightSheduleList.size(); i++) {
                    AirFreightFlightShedules airFreight = (AirFreightFlightShedules) flightSheduleList.get(i);
                    flightSchedule.add(airFreight);
                }
            }


            standardChrg.setAirFlightSchedules(flightSchedule);

            if (session.getAttribute("airdetailsAdd") != null) {

                airWeightRangeList = (List) session.getAttribute("airdetailsAdd");

                if (airWeightRangeList.size() > 0) {
                    for (int i = 0; i < airWeightRangeList.size(); i++) {
                        AirWeightRangesRates airWeightRangeObj = (AirWeightRangesRates) airWeightRangeList.get(i);
                        historyLogInterceptor.setSessionFactory(HibernateSessionFactory.getSessionFactory());
                        if (airWeightRangeObj.getId() != null) {
                            User userId = null;
                            if (session.getAttribute("loginuser") != null) {
                                userId = (User) session.getAttribute("loginuser");
                            }
                            historyLogInterceptor.setUserName(userId.getLoginName());
                            historyLogInterceptor.setStandardId(standardChrg.getId());
                            boolean flag = historyLogInterceptor.onFlushDirty(airWeightRangeObj, airWeightRangeObj.getId(), null, null, null, null);
                        }
                        airWeightRangeSet.add(airWeightRangeObj);
                        standardChrg.setAirWeightRangeSet(airWeightRangeSet);
                    }
                } else {
                    standardChrg.setAirWeightRangeSet(airWeightRangeSet);
                }
            } else {
                standardChrg.setAirWeightRangeSet(airWeightRangeSet);
            }
            if (session.getAttribute("cssList") != null) {

                airCommodityList = (List) session.getAttribute("cssList");
                for (int i = 0; i < airCommodityList.size(); i++) {
                    AirCommodityCharges airCommodityChargesObj = (AirCommodityCharges) airCommodityList.get(i);
                    historyLogInterceptor.setSessionFactory(HibernateSessionFactory.getSessionFactory());
                    if (airCommodityChargesObj.getAirCmdId() != null) {
                        User userId = null;
                        if (session.getAttribute("loginuser") != null) {
                            userId = (User) session.getAttribute("loginuser");
                        }
                        historyLogInterceptor.setUserName(userId.getLoginName());
                        historyLogInterceptor.setStandardId(standardChrg.getId());
                        boolean flag = historyLogInterceptor.onFlushDirty(airCommodityChargesObj, airCommodityChargesObj.getAirCmdId(), null, null, null, null);
                        if (flag) {

                            airCommodityChargesObj.setChangedDate(new Date());
                            airCommodityChargesObj.setWhoChanged(userId.getLoginName());
                        }
                    }
                    airCommoditySet.add(airCommodityChargesObj);
                }
                if (airCommoditySet != null && airCommoditySet.size() > 0) {
                    standardChrg.setAirCommoditySet(airCommoditySet);
                } else {
                    standardChrg.setAirCommoditySet(null);
                }
            }
            List airRatesList = new ArrayList();
            String programid = null;
            programid = (String) session.getAttribute("processinfoforairRates");
            String recordid = "";
            if (standardChrg != null && standardChrg.getId() != null) {
                recordid = standardChrg.getId().toString();
            }
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            standardChargesDAO.update(standardChrg, userId.getLoginName());
            String message = "";
            List newList = new ArrayList();
            if (programid != null) {
                dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
            }
            message = "Airrates details Edited successfully";
            session.setAttribute("Airmessage", message);
            if (standardChrg.getComCode() != null && !standardChrg.getComCode().getCode().equals("000000")) {
                newList = (List) session.getAttribute("noncommonList");
                for (int i = 0; i < newList.size(); i++) {
                    StandardCharges aStandardCharges11 = (StandardCharges) newList.get(i);//parent
                    if (aStandardCharges11.getComCode() != null && standardChrg.getComCode() != null &&
                            aStandardCharges11.getComCode().getCode() != null && standardChrg.getComCode().getCode() != null && aStandardCharges11.getComCode().getCode().equals(standardChrg.getComCode().getCode())) {
                        newList.set(i, standardChrg);
                    }
                }
                session.setAttribute("noncommonList", newList);
                session.setAttribute("AirUpdateRecords", standardChrg);

            } else {
                GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
                GenericCode genObj = genericCodeDAO.findById(11292);
                List list = standardChargesDAO.findAllDetails(standardChrg.getOrgTerminal(), standardChrg.getDestPort(), genObj);
                if (list != null && list.size() > 0) {
                    StandardCharges lclGetStd = (StandardCharges) list.get(0);

                    if (lclGetStd.getAirStandardCharges() != null) {
                        Iterator iter = (Iterator) lclGetStd.getAirStandardCharges().iterator();
                        while (iter.hasNext()) {
                            AirStandardCharges retailStdChild = (AirStandardCharges) iter.next();
                            retailStdChild.setStandardId(lclGetStd.getId());
                            newList.add(retailStdChild);
                        }
                        session.setAttribute("airRatescaptionCaps", "Common Accessorial Charges (All)");
                        session.setAttribute("commonList", newList);
                    }
                }

            }

            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }

            if (session.getAttribute("flightShedulesAdd") != null) {
                session.removeAttribute("flightShedulesAdd");
            }

            if (session.getAttribute("airFrieght") != null) {
                session.removeAttribute("airFrieght");
            }
            if (session.getAttribute("docChargesAdd") != null) {
                session.removeAttribute("docChargesAdd");
            }
            if (session.getAttribute("airStandardCharges") != null) {
                session.removeAttribute("airStandardCharges");
            }

            if (session.getAttribute("standardChargaes") != null) {
                session.removeAttribute("standardChargaes");
            }
            if (session.getAttribute("agssAdd") != null) {
                session.removeAttribute("agssAdd");
            }
            if (session.getAttribute("documentCharges") != null) {
                session.removeAttribute("documentCharges");
            }
            if (session.getAttribute("cssList") != null) {
                session.removeAttribute("cssList");
            }
            if (session.getAttribute("airdetailsAdd") != null) {
                session.removeAttribute("airdetailsAdd");
            }
            if (session.getAttribute("applyGeneralStandardList") != null) {
                session.removeAttribute("applyGeneralStandardList");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("airCommodityCharges") != null) {
                session.removeAttribute("airCommodityCharges");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }

            session.setAttribute("trade", "addairrates");
        }


        //-----------------------------------save records---------------------------------------

        if (buttonValue.equals("save")) {

            List docList = new ArrayList();
            Set documentCharges = new HashSet<AirFreightDocumentCharges>();
            if (session.getAttribute("docChargesAdd") != null) {
                docList = (List) session.getAttribute("docChargesAdd");
                for (int i = 0; i < docList.size(); i++) {
                    AirFreightDocumentCharges document1 = (AirFreightDocumentCharges) docList.get(i);
                    documentCharges.add(document1);
                }
            }
            standardChrg.setAirDocumentCharges(documentCharges);
            List agssList = new ArrayList();
            Set airStandard = new HashSet<AirStandardCharges>();
            if (session.getAttribute("agssAdd") != null) {
                agssList = (List) session.getAttribute("agssAdd");
                for (int j = 0; j < agssList.size(); j++) {
                    AirStandardCharges airStd = (AirStandardCharges) agssList.get(j);
                    airStandard.add(airStd);
                }
            }
            standardChrg.setAirStandardCharges(airStandard);
            List flightSheduleList = new ArrayList();
            Set flightSchedule = new HashSet<AirFreightFlightShedules>();
            if (session.getAttribute("flightShedulesAdd") != null) {
                flightSheduleList = (List) session.getAttribute("flightShedulesAdd");
                for (int i = 0; i < flightSheduleList.size(); i++) {
                    AirFreightFlightShedules airFreight = (AirFreightFlightShedules) flightSheduleList.get(i);
                    flightSchedule.add(airFreight);
                }
            }
            standardChrg.setAirFlightSchedules(flightSchedule);
            if (session.getAttribute("airdetailsAdd") != null) {
                airWeightRangeList = (List) session.getAttribute("airdetailsAdd");
                for (int i = 0; i < airWeightRangeList.size(); i++) {
                    AirWeightRangesRates airWeightRangeObj = (AirWeightRangesRates) airWeightRangeList.get(i);
                    airWeightRangeSet.add(airWeightRangeObj);
                }
                standardChrg.setAirWeightRangeSet(airWeightRangeSet);
            }

            if (session.getAttribute("cssList") != null) {

                airCommodityList = (List) session.getAttribute("cssList");
                for (int i = 0; i < airCommodityList.size(); i++) {
                    AirCommodityCharges airCommodityChargesObj = (AirCommodityCharges) airCommodityList.get(i);
                    airCommoditySet.add(airCommodityChargesObj);
                }
                if (airCommoditySet != null && airCommoditySet.size() > 0) {
                    standardChrg.setAirCommoditySet(airCommoditySet);
                }
            }
            List airRatesList = new ArrayList();
            standardChrg.setOrgTerminal(standardChrg.getOrgTerminal());
            standardChrg.setDestPort(standardChrg.getDestPort());
            standardChrg.setComCode(standardChrg.getComCode());
            standardChrg.setScheduleTerminal(standardChrg.getScheduleTerminal());
            standardChrg.setMaxDocCharge(standardChrg.getMaxDocCharge());
            standardChrg.setBlBottomLine(standardChrg.getBlBottomLine());
            standardChrg.setFfCommission(standardChrg.getFfCommission());
            standardChrg.setAirWeightRangeSet(standardChrg.getAirWeightRangeSet());
            standardChrg.setAirCommoditySet(standardChrg.getAirCommoditySet());
            standardChrg.setAirStandardCharges(standardChrg.getAirStandardCharges());
            standardChrg.setAirDocumentCharges(standardChrg.getAirDocumentCharges());
            standardChrg.setAirFlightSchedules(standardChrg.getAirFlightSchedules());
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            standardChargesDAO.save(standardChrg, userId.getLoginName());
            airRatesList = new ArrayList();
            airRatesList.add(standardChrg);
            GenericCode genObj = null;
            AirRatesBean srBean = new AirRatesBean();
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            String message = "Airrates details Saved successfully";
            session.setAttribute("Airmessage", message);
            List newList = new ArrayList();
            if (standardChrg.getComCode() != null && !standardChrg.getComCode().getCode().equals("000000")) {
                if (session.getAttribute("noncommonList") != null) {
                    newList = (List) session.getAttribute("noncommonList");
                } else {
                    newList = new ArrayList();
                }
                newList.add(standardChrg);
                session.setAttribute("noncommonList", newList);
                session.setAttribute("AirUpdateRecords", standardChrg);

            } else {

                genObj = genericCodeDAO.findById(11292);
                List list = standardChargesDAO.findAllDetails(standardChrg.getOrgTerminal(), standardChrg.getDestPort(), genObj);
                if (list != null && list.size() > 0) {
                    StandardCharges lclGetStd = (StandardCharges) list.get(0);

                    if (lclGetStd.getAirStandardCharges() != null) {
                        Iterator iter = (Iterator) lclGetStd.getAirStandardCharges().iterator();
                        while (iter.hasNext()) {
                            AirStandardCharges retailStdChild = (AirStandardCharges) iter.next();
                            retailStdChild.setStandardId(lclGetStd.getId());
                            newList.add(retailStdChild);
                        }
                        session.setAttribute("airRatescaptionCaps", "Common Accessorial Charges (All)");
                        session.setAttribute("commonList", newList);
                    }
                }

            }


            if (session.getAttribute("flightShedulesAdd") != null) {
                session.removeAttribute("flightShedulesAdd");
            }
            if (session.getAttribute("airFrieght") != null) {
                session.removeAttribute("airFrieght");
            }
            if (session.getAttribute("docChargesAdd") != null) {
                session.removeAttribute("docChargesAdd");
            }
            if (session.getAttribute("airStandardCharges") != null) {
                session.removeAttribute("airStandardCharges");
            }
            if (session.getAttribute("agssAdd") != null) {
                session.removeAttribute("agssAdd");
            }
            if (session.getAttribute("documentCharges") != null) {
                session.removeAttribute("documentCharges");
            }
            if (session.getAttribute("cssList") != null) {
                session.removeAttribute("cssList");
            }
            if (session.getAttribute("airdetailsAdd") != null) {
                session.removeAttribute("airdetailsAdd");
            }
            if (session.getAttribute("applyGeneralStandardList") != null) {
                session.removeAttribute("applyGeneralStandardList");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("airCommodityCharges") != null) {
                session.removeAttribute("airCommodityCharges");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("standardChargaes") != null) {
                session.removeAttribute("standardChargaes");
            }
            session.setAttribute("trade", "addairrates");
        } else if (buttonValue.equals("delete")) {

            String programid = null;
            programid = (String) session.getAttribute("processinfoforairRates");

            String recordid = "";
            if (standardChrg != null && standardChrg.getId() != null) {
                recordid = standardChrg.getId().toString();
            }
            if (session.getAttribute("standardCharges") != null) {
                standardChrg = (StandardCharges) session.getAttribute("standardCharges");
            }
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            String message = "";
            dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");
            message = "Airrates details Deleted successfully";
            session.setAttribute("Airmessage", message);
            List newList = new ArrayList();
            if (standardChrg.getComCode() != null && !standardChrg.getComCode().getCode().equals("000000")) {
                newList = (List) session.getAttribute("noncommonList");
                for (int i = 0; i < newList.size(); i++) {
                    StandardCharges aStandardCharges11 = (StandardCharges) newList.get(i);//parent
                    if (aStandardCharges11.getComCode() != null && standardChrg.getComCode() != null &&
                            aStandardCharges11.getComCode().getCode() != null && standardChrg.getComCode().getCode() != null && aStandardCharges11.getComCode().getCode().equals(standardChrg.getComCode().getCode())) {
                        newList.remove(i);
                    }

                }
                session.setAttribute("noncommonList", newList);
                session.setAttribute("AirUpdateRecords", standardChrg);

            } else {
                if (session.getAttribute("commonList") != null) {
                    session.removeAttribute("commonList");
                }
                if (session.getAttribute("airRatescaptionCaps") != null) {
                    session.removeAttribute("airRatescaptionCaps");
                }

            }
            standardChargesDAO.delete(standardChrg, userId.getLoginName());
            if (session.getAttribute("flightShedulesAdd") != null) {
                session.removeAttribute("flightShedulesAdd");
            }
            if (session.getAttribute("airFrieght") != null) {
                session.removeAttribute("airFrieght");
            }

            if (session.getAttribute("docChargesAdd") != null) {
                session.removeAttribute("docChargesAdd");
            }
            if (session.getAttribute("airStandardCharges") != null) {
                session.removeAttribute("airStandardCharges");
            }
            if (session.getAttribute("agssAdd") != null) {
                session.removeAttribute("agssAdd");
            }
            if (session.getAttribute("documentCharges") != null) {
                session.removeAttribute("documentCharges");
            }
            if (session.getAttribute("cssList") != null) {
                session.removeAttribute("cssList");
            }
            if (session.getAttribute("airdetailsAdd") != null) {
                session.removeAttribute("airdetailsAdd");
            }
            if (session.getAttribute("applyGeneralStandardList") != null) {
                session.removeAttribute("applyGeneralStandardList");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("airCommodityCharges") != null) {
                session.removeAttribute("airCommodityCharges");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("airRatesList") != null) {
                session.removeAttribute("airRatesList");

            }


            session.setAttribute("trade", "addairrates");
        } else if (buttonValue.equals("cancel")) {

            if (session.getAttribute("Airmessage") != null) {
                session.removeAttribute("Airmessage");
            }
            String programid = null;
            programid = (String) session.getAttribute("processinfoforairRates");

            String recordid = "";
            if (standardChrg != null && standardChrg.getId() != null) {
                recordid = standardChrg.getId().toString();
            }


            if (session.getAttribute("flightShedulesAdd") != null) {
                session.removeAttribute("flightShedulesAdd");
            }
            if (session.getAttribute("airFrieght") != null) {
                session.removeAttribute("airFrieght");
            }
            if (session.getAttribute("docChargesAdd") != null) {
                session.removeAttribute("docChargesAdd");
            }
            if (session.getAttribute("airStandardCharges") != null) {
                session.removeAttribute("airStandardCharges");
            }
            if (session.getAttribute("agssAdd") != null) {
                session.removeAttribute("agssAdd");
            }
            if (session.getAttribute("documentCharges") != null) {
                session.removeAttribute("documentCharges");
            }
            if (session.getAttribute("cssList") != null) {
                session.removeAttribute("cssList");
            }
            if (session.getAttribute("airdetailsAdd") != null) {
                session.removeAttribute("airdetailsAdd");
            }
            if (session.getAttribute("applyGeneralStandardList") != null) {
                session.removeAttribute("applyGeneralStandardList");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("airCommodityCharges") != null) {
                session.removeAttribute("airCommodityCharges");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }

            String message = "";
            dbUtil.getProcessInfo(programid, recordid, "deleted", "edited");

        } else if (buttonValue.equals("cancelview")) {

            if (session.getAttribute("airRatesList") != null) {
                session.removeAttribute("airRatesList");
            }
            if (session.getAttribute("flightShedulesAdd") != null) {
                session.removeAttribute("flightShedulesAdd");
            }
            if (session.getAttribute("airFrieght") != null) {
                session.removeAttribute("airFrieght");
            }
            if (session.getAttribute("docChargesAdd") != null) {
                session.removeAttribute("docChargesAdd");
            }
            if (session.getAttribute("airStandardCharges") != null) {
                session.removeAttribute("airStandardCharges");
            }
            if (session.getAttribute("agssAdd") != null) {
                session.removeAttribute("agssAdd");
            }
            if (session.getAttribute("documentCharges") != null) {
                session.removeAttribute("documentCharges");
            }
            if (session.getAttribute("cssList") != null) {
                session.removeAttribute("cssList");
            }
            if (session.getAttribute("airdetailsAdd") != null) {
                session.removeAttribute("airdetailsAdd");
            }
            if (session.getAttribute("applyGeneralStandardList") != null) {
                session.removeAttribute("applyGeneralStandardList");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("airCommodityCharges") != null) {
                session.removeAttribute("airCommodityCharges");
            }
            if (session.getAttribute("Airmessage") != null) {
                session.removeAttribute("Airmessage");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }


        } else if (buttonValue.equals("savecancel")) {

            if (session.getAttribute("flightShedulesAdd") != null) {
                session.removeAttribute("flightShedulesAdd");
            }
            if (session.getAttribute("airFrieght") != null) {
                session.removeAttribute("airFrieght");
            }
            if (session.getAttribute("docChargesAdd") != null) {
                session.removeAttribute("docChargesAdd");
            }
            if (session.getAttribute("airStandardCharges") != null) {
                session.removeAttribute("airStandardCharges");
            }
            if (session.getAttribute("agssAdd") != null) {
                session.removeAttribute("agssAdd");
            }
            if (session.getAttribute("documentCharges") != null) {
                session.removeAttribute("documentCharges");
            }
            if (session.getAttribute("cssList") != null) {
                session.removeAttribute("cssList");
            }
            if (session.getAttribute("airdetailsAdd") != null) {
                session.removeAttribute("airdetailsAdd");
            }
            if (session.getAttribute("applyGeneralStandardList") != null) {
                session.removeAttribute("applyGeneralStandardList");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }
            if (session.getAttribute("airCommodityCharges") != null) {
                session.removeAttribute("airCommodityCharges");
            }
            if (session.getAttribute("Airmessage") != null) {
                session.removeAttribute("Airmessage");
            }
            if (session.getAttribute("includedList") != null) {
                session.removeAttribute("includedList");
            }

        } else if (buttonValue != null && buttonValue.equals("note")) {
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";

            if (session.getAttribute("processinfoforairRates") != null) {
                String itemId = (String) session.getAttribute("processinfoforairRates");
                item = itemDAO.findById(Integer.parseInt(itemId));
                itemName = item.getItemDesc();
            }

            forwardName = "note";
            AuditLogRecord auditLogRecord = new AuditLogRecordAirRates();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            //noteBean.setUser(user);
            noteBean.setPageName("cancelairdetails");
            String noteId = "";
            if (standardChrg.getId() != null) {
                noteId = standardChrg.getId().toString();
                noteBean.setNoteId(noteId);
                noteBean.setReferenceId(noteId);
            }


            List auditList = null;

            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            noteBean.setVoidednote("");
            request.setAttribute("noteBean", noteBean);
            String documentName = "User";
            request.setAttribute("buttonValue", buttonValue);
            return mapping.findForward("note");
        }

        session.setAttribute("trade", "addairrates");

        forwardName = "addairrates";
        if (session.getAttribute("getAirEdit") != null) {
            session.removeAttribute("getAirEdit");
        }
        if (session.getAttribute("getDocFreight") != null) {
            session.removeAttribute("getDocFreight");
        }
        if (session.getAttribute("getAirAgsss") != null) {
            session.removeAttribute("getAirAgsss");
        }

        if (session.getAttribute("listOfAgssItem") != null) {
            session.removeAttribute("listOfAgssItem");
        }
        if (session.getAttribute("airaddcsslist") != null) {
            session.removeAttribute("airaddcsslist");
        }
        if (session.getAttribute("addDetailItem") != null) {
            session.removeAttribute("addDetailItem");
        }

        return mapping.findForward("addairrates");
    }
}