// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/17/2008 11:24:36 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LoadRecords.java
package com.gp.cong.logisoft.util;

import com.gp.cong.logisoft.domain.CustomerTemp;
import com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates;
import com.gp.cong.logisoft.domain.FclBuyCost;
import com.gp.cong.logisoft.domain.FclBuy;
import com.gp.cong.logisoft.domain.RefTerminalTemp;
import com.gp.cong.logisoft.domain.PortsTemp;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.CarriersOrLineTemp;
import com.gp.cong.logisoft.domain.TradingPartnerTemp;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.*;
import java.io.File;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import jxl.*;
import org.apache.commons.lang3.StringUtils;

// Referenced classes of package com.gp.cong.logisoft.util:
//            DBUtil
public class FutureLoadRecords {

    public FutureLoadRecords() {
        searchFclrecords = new ArrayList();
        secondunittype = new ArrayList();
        notinsert = new ArrayList();
        loginName = "";
        msg = "";
        forwardName = "";
        message = "";
    }

    public void getExcelRecords(List records, int sheetNo, int j) throws Exception{
        List fclBuyList = new ArrayList();
        DBUtil dbUtil = new DBUtil();
        Set set = new HashSet();
        UnLocation refTerminal = null;
        GenericCode gen = null;
        UnLocation ports = null;
        FclBuy fclBuy = new FclBuy();
        String org = "";
        String dest = "";
        String comcode = "";
        String sslineNO = "";
        FclBuyCost fclBuyCost = null;
        String contact = "";
        List insertList = new ArrayList();
        String startdate = "";
        String enddate = "";
        Set fclbuySet = new HashSet();
        Set fclbuyratesSet = null;
        DBUtil dBUtil = new DBUtil();
        CustomerDAO carriersOrLineDAO = new CustomerDAO();
        TradingPartnerTemp carriersOrLineTemp = null;
        RefTerminalDAO refTerminalDAO = new RefTerminalDAO();
        UnLocationDAO portsDAO = new UnLocationDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        CarriersOrLineTemp carries = null;
        FclBuyCostDAO fclBuyCostDAO = new FclBuyCostDAO();
        GenericCode genericCode = null;
        GenericCode genObj = null;
        int con = 0;
        List commonList = new ArrayList();
        FclBuyDAO fclBuyDAO = new FclBuyDAO();
        org = (String) records.get(0);
        if (org != null && !org.equals("")) {

            List unLocationList = portsDAO.findForManagement(org, null);

            if (unLocationList != null && unLocationList.size() > 0) {
                refTerminal = (UnLocation) unLocationList.get(0);
                fclBuy.setOriginTerminal(refTerminal);
            }
        }
        dest = (String) records.get(3);
        if (dest != null && !dest.equals("")) {
            String d[] = StringUtils.splitPreserveAllTokens(dest, '-');
            String control = null;
            String port = null;
            if (d.length > 1) {
                port = d[0];
                control = d[1];
            } else {
                port = dest;
            }
            List portsList = portsDAO.findForManagement(port, control);
            if (portsList != null && portsList.size() > 0) {
                ports = (UnLocation) portsList.get(0);
                fclBuy.setDestinationPort(ports);
            }
        }
        comcode = (String) records.get(5);
        if (comcode != null && !comcode.equals("")) {
            List comList = genericCodeDAO.findForGenericCode(comcode);
            if (comList != null && comList.size() > 0) {
                gen = (GenericCode) comList.get(0);
                fclBuy.setComNum(gen);
            }
        }
        sslineNO = (String) records.get(6);
        if (sslineNO != null && !sslineNO.equals("")) {
            List SSNo = carriersOrLineDAO.findAccountNo1(sslineNO);
            if (SSNo != null && SSNo.size() > 0) {
                carriersOrLineTemp = (TradingPartnerTemp) SSNo.get(0);
                fclBuy.setSslineNo(carriersOrLineTemp);
            }
        }
        contact = (String) records.get(7);
        if (contact != null) {
            fclBuy.setContract(contact);
        }
        startdate = (String) records.get(8);
        if (startdate != null && startdate != "") {
            java.util.Date start = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                start = sdf.parse(startdate);
            fclBuy.setStartDate(start);
        }
        enddate = (String) records.get(9);
        if (enddate != null && enddate != "") {
            java.util.Date end = null;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                end = sdf.parse(enddate);
            fclBuy.setEndDate(end);
        }
        String costType = "";
        String contentType = "";
        String amount = "";
        String currency = "";
        String minamt = "";
        String mintype = "";
        List tokens = new ArrayList();
        fclBuy.setFclBuyCostsSet(fclbuySet);
        List list = new ArrayList();
        DBUtil dbutil = new DBUtil();
        boolean escape = false;

        if (refTerminal != null && ports != null && gen != null && carriersOrLineTemp != null) {
            if (fclBuy != null) {
                list = dbutil.getFCLDetails(fclBuy.getOriginTerminal(), fclBuy.getDestinationPort(), fclBuy.getComNum(), fclBuy.getSslineNo(), fclBuy.getOriginalRegion(), fclBuy.getDestinationRegion());
                for (int i = 0; i < list.size(); i++) {
                    FclBuy b = (FclBuy) list.get(i);
                    FclBuy fcl = fclBuyDAO.findById(b.getFclStdId());
                    if (fcl == null) {
                        continue;
                    }
                    fclBuyDAO.delete(fcl);
                    break;
                }

            }
            fclBuyDAO.save(fclBuy);
            for (int i = 10; i < records.size(); i++) {
                String test = (String) records.get(i);
                if (test != null && !test.equals("")) {
                    fclBuyCost = new FclBuyCost();
                    FclBuyCostTypeFutureRates fclBuyCostTypeRates = new FclBuyCostTypeFutureRates();
                    costType = null;
                    contentType = null;
                    amount = null;
                    currency = null;
                    minamt = null;
                    mintype = null;
                    String cellValues[] = StringUtils.splitPreserveAllTokens(test, '/');
                    if (cellValues.length > 2 && cellValues[2] != null && !cellValues[2].trim().equals("")) {

                        if (cellValues.length == 3) {
                            costType = cellValues[0];
                            contentType = cellValues[1];
                            amount = cellValues[2];
                        } else if (cellValues.length == 4) {
                            costType = cellValues[0];
                            contentType = cellValues[1];
                            amount = cellValues[2];
                            currency = cellValues[3];
                        } else if (cellValues.length == 5) {
                            costType = cellValues[0];
                            contentType = cellValues[1];
                            amount = cellValues[2];
                            mintype = cellValues[3];
                            minamt = cellValues[4];
                        }
                        genericCode = dBUtil.getCostID(costType.trim(), 36);
                        if (genericCode == null) {

                            escape = true;
                            break;

                        }
                        GenericCode gcForCont = dBUtil.getContentTypeID(contentType, 38);
                        boolean found = false;
                        if (fclbuySet != null) {
                            for (Iterator it = fclbuySet.iterator(); it.hasNext();) {
                                FclBuyCost fclBCost = (FclBuyCost) it.next();
                                if (fclBCost.getCostId() != null && fclBCost.getCostId().equals(genericCode)) {
                                    found = true;
                                }
                            }

                        }
                        if (!found) {
                            fclBuyCost.setCostId(genericCode);
                            if (contentType == null || contentType.equals("")) {
                                genObj = genericCodeDAO.findById(Integer.valueOf(Integer.parseInt("11460")));//11460 PRIMARY FREIGHT //11516 BAL SHIPPING  // SEACORP 11460 Per Container size
                            } else if (contentType != null && contentType.equals("%")) {
                                genObj = genericCodeDAO.findById(Integer.valueOf(Integer.parseInt("11516")));//SEACOPR 11516 PER CANTAGE OFR
                            } else if (contentType != null && contentType.equals("*")) {
                                genObj = genericCodeDAO.findById(Integer.valueOf(Integer.parseInt("11314")));//SEACOPR 11516//PER BL
                            } else if (contentType != null && contentType.equals("#")) {
                                genObj = genericCodeDAO.findById(Integer.valueOf(Integer.parseInt("11304")));
                            } else {
                                genObj = genericCodeDAO.findById(Integer.valueOf(Integer.parseInt("11300")));
                            }
                            fclBuyCost.setContType(genObj);
                            fclbuySet.add(fclBuyCost);
                        }
                    }
                }
            }

            for (Iterator it = fclbuySet.iterator(); it.hasNext(); fclBuyCost.setFclStdId(fclBuy.getFclStdId())) {
                fclBuyCost = (FclBuyCost) it.next();
            }

            fclBuy.setFclBuyCostsSet(fclbuySet);

            if (!escape) {
                for (int i = 10; i < records.size(); i++) {
                        FclBuyCostTypeFutureRates fclBuyCostTypeRates = new FclBuyCostTypeFutureRates();
                        costType = null;
                        contentType = null;
                        amount = null;
                        currency = null;
                        minamt = null;
                        mintype = null;
                        String test = (String) records.get(i);
                        if (test != null && !test.equals("")) {
                            String cellValues[] = StringUtils.splitPreserveAllTokens(test, '/');
                            if (cellValues.length > 2 && cellValues[2] != null && !cellValues[2].trim().equals("")) {
                                if (cellValues.length == 3) {
                                    costType = cellValues[0];
                                    contentType = cellValues[1];
                                    amount = cellValues[2];
                                } else if (cellValues.length == 4) {
                                    costType = cellValues[0];
                                    contentType = cellValues[1];
                                    amount = cellValues[2];
                                    currency = cellValues[3];
                                } else if (cellValues.length == 5) {
                                    costType = cellValues[0];
                                    contentType = cellValues[1];
                                    amount = cellValues[2];
                                    mintype = cellValues[3];
                                    minamt = cellValues[4];
                                }
                                genericCode = dBUtil.getCostID(costType.trim(), 36);
                                GenericCode gcForCont = dBUtil.getContentTypeID(contentType, 38);
                                fclBuyCostTypeRates.setUnitType(gcForCont);
                                if (gcForCont != null) {
                                    if (amount != null && !amount.trim().equals("")) {
                                        fclBuyCostTypeRates.setActiveAmt(Double.valueOf(Double.parseDouble(amount)));
                                    } else {
                                        fclBuyCostTypeRates.setActiveAmt(Double.valueOf(0.0D));
                                    }
                                } else if (amount != null && !amount.trim().equals("")) {
                                    fclBuyCostTypeRates.setRatAmount(Double.valueOf(Double.parseDouble(amount)));
                                } else {
                                    fclBuyCostTypeRates.setRatAmount(Double.valueOf(0.0D));
                                }
                                if (currency != null && !currency.equals("")) {
                                    genObj = dBUtil.getCurrency(currency, 32);
                                } else {
                                    genObj = dBUtil.getCurrency("USD", 32);
                                }
                                if (minamt != null && !minamt.equals("")) {
                                    fclBuyCostTypeRates.setMinimumAmt(Double.valueOf(Double.parseDouble(minamt)));
                                }

                                fclBuyCostTypeRates.setCurrency(genObj);
                                fclBuyCostTypeRates.setStandard("Y");
                                Set s = fclBuy.getFclBuyCostsSet();
                                for (Iterator it1 = s.iterator(); it1.hasNext();) {
                                    fclBuyCost = (FclBuyCost) it1.next();
                                    if (fclBuyCost != null && fclBuyCost.getCostId() != null && fclBuyCost.getCostId().equals(genericCode)) {
                                        fclBuyCostTypeRates.setFclCostId(fclBuyCost.getFclCostId());
                                        fclbuyratesSet = new HashSet();
                                        fclbuyratesSet.add(fclBuyCostTypeRates);
                                        fclBuyCost.setFclBuyUnitTypesSet(fclbuyratesSet);
                                        break;
                                    }
                                }

                            }
                        }
                }

                fclBuy.setFclBuyCostsSet(fclbuySet);
                searchFclrecords.add(fclBuy.getOriginTerminal().getId());
                fclBuy = null;
                fclbuySet.clear();
            } else {
                fclBuyDAO.delete(fclBuy);

                escape = false;
                notinsert.add((new StringBuilder(String.valueOf(j))).toString());
            }
        } else {
            notinsert.add((new StringBuilder(String.valueOf(j))).toString());

        }
    }

    public List loadEcxellSheet(File file)throws Exception{
            Workbook workbook = null;
            Sheet sheet = null;
            workbook = Workbook.getWorkbook(file);
            Sheet sheets[] = workbook.getSheets();

            for (int i = 0; i < sheets.length; i++) {
                sheet = workbook.getSheet(i);
                String name = sheet.getName();
                int rows = sheet.getRows();
                int cols = sheet.getColumns();
                int sheetNo = i;
                Cell a1 = null;
                String contents = "";
                List colname = new ArrayList();
                for (int j = 1; j < rows; j++) {
                    for (int k = 0; k < cols; k++) {
                        a1 = sheet.getCell(k, j);
                        contents = a1.getContents();
                        colname.add(contents);
                    }

                    getExcelRecords(colname, sheetNo, j);
                    colname.clear();
                }

            }

            workbook.close();
        if (notinsert != null) {
            searchFclrecords.add(0, notinsert);
        }


        return searchFclrecords;
    }
    List searchFclrecords;
    List secondunittype;
    List notinsert;
    String loginName;
    String msg;
    String forwardName;
    String message;
}