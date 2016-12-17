package com.gp.cong.logisoft.bc.ratemanagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.struts.util.LabelValueBean;

public class PortsBC {

    RefTerminalTemp refTerminalTemp = new RefTerminalTemp();
    PortsTemp portsTemp = new PortsTemp();
    RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
    PortsDAO portDAO = new PortsDAO();

    public RefTerminalTemp getTerminalName(String terminalNumber) throws Exception {
        refTerminalTemp = refTerminalDAO.findById1(terminalNumber);
        return refTerminalTemp;
    }

    public RefTerminalTemp getTerminalNumber(String terminalId, String terminalName, String terminaltype, String city) throws Exception {
        List terminalNumberList = refTerminalDAO.findForManagement(null, terminalName, null, null);
        if (terminalNumberList != null && terminalNumberList.size() > 0) {
            refTerminalTemp = (RefTerminalTemp) terminalNumberList.get(0);
        }
        return refTerminalTemp;
    }

    public PortsTemp getDestinationPortName(String scheduleCode, String controlNo) throws Exception {
        List destinationNameList = portDAO.findPortCode(scheduleCode, "0001");
        if (destinationNameList != null && destinationNameList.size() > 0) {
            portsTemp = (PortsTemp) destinationNameList.get(0);
        }
        return portsTemp;
    }

    public PortsTemp getDestinationPortNumber(String pierCode, String portName) throws Exception {
        List portsList = portDAO.findPierCode(null, portName);
        if (portsList != null && portsList.size() > 0) {
            portsTemp = (PortsTemp) portsList.get(0);
        }
        return portsTemp;
    }
//-----for Voyage Management-----

    public PortsTemp getPortName(String code, String codedesc) throws Exception {
        List portNumberList = portDAO.findForExport(code, codedesc);
        if (portNumberList != null && portNumberList.size() > 0) {
            portsTemp = (PortsTemp) portNumberList.get(0);
        }
        return portsTemp;
    }

    public PortsTemp getScheduleCode(String scheduleCode) throws Exception {
        PortsDAO portsDAO = new PortsDAO();
        List portslist = portsDAO.findForschcodeandcity(scheduleCode, null);
        if (portslist != null && portslist.size() > 0) {
            portsTemp = (PortsTemp) portslist.get(0);
        }
        return portsTemp;
    }

        //initCapitalize method is to make the String into title case
    //Added check to initcap the String, immediately after the delimeter / (eg.) if String contains Asia/Far East
    //Added check to initcap the String, immediately after the delimeter . (eg.) if String contains U.S
    public String initCapitalize(String stringToInitCap) {
        String initCapitalString = WordUtils.capitalize(stringToInitCap.toLowerCase());
        String subStrBefore;
        String subStrAfter;
        String finalInitCapString = initCapitalString;
        if (StringUtils.contains(initCapitalString, "/")) {
            subStrBefore = StringUtils.substringBefore(initCapitalString, "/") + "/";
            subStrAfter = WordUtils.capitalize(StringUtils.substringAfter(initCapitalString, "/"));
            finalInitCapString = subStrBefore + subStrAfter;
        }
        if (StringUtils.contains(initCapitalString, ".")) {
            subStrBefore = StringUtils.substringBefore(initCapitalString, ".") + ".";
            subStrAfter = WordUtils.capitalize(StringUtils.substringAfter(initCapitalString, "."));
            finalInitCapString = subStrBefore + subStrAfter;
        }
        return finalInitCapString;
    }

    public List getAllRegion(Integer regionCode) throws Exception {
        return portDAO.getAllCountryByRegion("" + regionCode);
    }

    public List getAllRegion1() throws Exception {
        List regionList = new ArrayList();
        for (Object regions : portDAO.getAllRegion()) {
            Object[] region = (Object[]) regions;
            regionList.add(new LabelValueBean(initCapitalize(region[1].toString()), region[0].toString()));
        }
        return regionList;
    }

    public List getPortNameAndUnLocCode(Integer regionCode) throws Exception {
        return portDAO.getPortNameAndUnLocCode(regionCode);
    }

    public List getDestionationPort(String desTination) throws Exception {
        return portDAO.getDestionationPort(desTination);
    }

    public List getNonRatedPorts(List portsList, String flag) throws Exception {
        List nonRatedList = new ArrayList();
        nonRatedList.addAll(portsList);
        String message = "";

        if (portsList.size() > 0) {
            for (Iterator iter = portsList.iterator(); iter.hasNext();) {
                PortsTemp portsTemp = (PortsTemp) iter.next();
                //--GET UNLOCATIONID OF THE PORT----
                UnLocation unLocation = new UnLocationDAO().getUnlocation(portsTemp.getUnLocationCode());
                //--CHECK THE UNLOCATIONID IN FCL_BUY TABLE FOR ORIGINAL TERMINAL & DESTINATION PORT Based on "FLAG" value---
                message = new FclBuyDAO().getTerminal(unLocation.getId().toString(), flag);
                if (!message.equals("") && message.equals("rates present")) {
                    nonRatedList.remove(portsTemp);
                }
            }
        }
        return nonRatedList;
    }

}
