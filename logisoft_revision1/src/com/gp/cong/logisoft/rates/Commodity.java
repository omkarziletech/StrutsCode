/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.rates;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.FclOrgDestMiscData;
import com.gp.cong.logisoft.hibernate.dao.FclBuyDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.rates.charge.ChargeCode;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.domain.Zipcode;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.struts.form.QuotesForm;
import com.gp.cvst.logisoft.struts.form.RateGridForm;
import com.logiware.ims.client.IMSQuote;
import com.logiware.ims.client.ImsModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import org.apache.struts.util.LabelValueBean;

/**
 *
 * @author Administrator
 */
public class Commodity extends ArrayList<FCLRates> implements Comparator<FCLRates> {

    private String commodityCode;
    private String commodityName;
    private int headerTdLength = 0;
    private String normalMode;
    private String collapseMode;
    private String expandMode;
    private boolean hazardous = false;
    private boolean quickRates = false;
    private boolean multiRates = false;
    private int  idValue = 0; 
    private boolean imsRates = false;
    private String service;
    private String cityMap;
    private String cityDetails;
    private String normalCityDetails;
    private String expandCityDetails;
    private List<String> selectedList;
    private List<FCLRates> sortedList;
    private String path;
    NumberFormat nf = new DecimalFormat("#0");
    CustAddressDAO custAddressDAO = new CustAddressDAO();
    private static List<String> bulletRateSslines = new ArrayList<String>();
    private static List<LabelValueBean> bulletRatesCommodities = new ArrayList<LabelValueBean>();
    private static GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    private static FclBuyDAO fclBuyDAO = new FclBuyDAO();

    public List<FCLRates> getSortedList() {
        return sortedList;
    }

    public void setSortedList(List<FCLRates> sortedList) {
        this.sortedList = sortedList;
    }

    public Commodity() {
    }

    public Commodity(List queryObj, Map<String, Double> distances, String service, Map<String, ImsModel> imsQuoteMap, boolean hazardous, Zipcode doorOrigin, String path,String fileType) throws Exception {
        this.path = path;
        UnitCost.resetCostTypes();
        this.service = service;
        fillRatesListFromQueryObject(queryObj, distances, doorOrigin,fileType);
        setCommodityCode(queryObj);
        setImsQuote(imsQuoteMap, hazardous);
        //sortFclRatesByDistance(distances);
        setSortedList(this);
        Collections.sort(sortedList, this);
    }

    private void setImsQuote(Map<String, ImsModel> imsQuoteMap, boolean hazardous) {
        for (Map.Entry<String, ImsModel> entry : imsQuoteMap.entrySet()) {
            for (FCLRates fclRates : this) {
                if (null != entry.getKey()
                        && service.equals("Origin") ? fclRates.getDestinationName().contains(entry.getKey())
                                : fclRates.getOriginName().contains(entry.getKey())) {
                    ImsModel imsModel = entry.getValue();
                    imsModel.setHazardous(hazardous);
                    fclRates.setImsModel(imsModel);
                }
            }
        }
    }

    private void sortFclRatesByDistance(Map<String, Double> distances) {
        List<FCLRates> rates = new ArrayList<FCLRates>();
        Collection<Double> dist = distances.values();
        if (null != dist && dist.size() > 0) {
            Double[] distanceArray = new Double[dist.size()];
            dist.toArray(distanceArray);
            Arrays.sort(distanceArray);
            for (Double d : distanceArray) {
                for (FCLRates fclRates : this) {
                    if (d.equals(fclRates.getDistance()) && !fclRates.isIncluded()) {
                        rates.add(fclRates);
                        fclRates.setIncluded(true);
                    }
                }
            }
        } else {
            rates = this;
        }
        setSortedList(rates);
    }

    private void fillRatesListFromQueryObject(List queryObj, Map<String, Double> distances, Zipcode doorOrigin,String fileType) throws Exception {
        FCLRates previousFCLRates = new FCLRates("origin", "destination");
        for (Object rows : queryObj) {
            Object[] row = (Object[]) rows;
            //row[0] = nvo_number
            if (row[0] != null) {
                //if FCLRates is same as previous then add SSLine else create new FCLRates
                if (row[Rates.ORIGIN_TERMINAL_ID].toString().equals(previousFCLRates.getOriginId()) && row[Rates.DESTINATION_PORT_ID].toString().equals(previousFCLRates.getDestinationId())) {
                    previousFCLRates.addSsLine(row);
                } else {
                    FCLRates fCLRates = new FCLRates(row);
                    Double distance = null;
                    if (null != doorOrigin && !CommonUtils.isEmpty(doorOrigin.getLat())) {
                        if(null != fCLRates.getLat()){
                        distance = fCLRates.distFromDoor(doorOrigin);
                        }else if("I".equalsIgnoreCase(fileType) && null == fCLRates.getLat() && null == fCLRates.getLng()){
                          distance = fCLRates.distFromDoorForNullLat(doorOrigin,row[Rates.DESTINATION_PORT_NAME].toString());   
                        }
                        if (distance != null) {
                            fCLRates.setDistance(distance);
                        }
                    }
                    add(fCLRates);
                    previousFCLRates = fCLRates;
                }
            }
        }
    }

    public void setCommodityCode(List queryObj) {
        if (queryObj != null & !queryObj.isEmpty()) {
            Object[] row = (Object[]) queryObj.get(0);
            commodityCode = row[Rates.COMMODITY_CODE].toString();
        }
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public int sizeOfElement(int i) {
        return get(i).size();
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    /**
     * Is OFR having charge for the Unit Type
     * @param unitType
     * @param viaPort
     * @return
     */
    private boolean isOfrHavingCharge(String unitType, SSLine ssLine) {
        boolean flag = false;
        for (ChargeCode chargeCode : ssLine) {
            if ("OFR".equalsIgnoreCase(chargeCode.getChargeCode())) {
                for (UnitCost unitCost : chargeCode) {
                    if (unitCost.getCurrentUnitCodeDesc().equals(unitType)) {
                        return true;
                    }
                }
                break;
            }
        }
        return flag;
    }

    public String getCollapsedTable(String destName, String unlocCode, String destination, String fileType) throws Exception {
        TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
        StringBuilder table = new StringBuilder();
        StringBuilder header = getCollopsedHeader();
        table.append("<div></div>");
        table.append("<table class='scrolldisplaytablenew' id='normalTable' width='100%'>");
        table.append(header);
        String mapCityList = null;
        StringBuffer details = new StringBuffer();
        selectedList = new ArrayList<String>();
        Map<String, Double> lowerCostAllCities = new HashMap<String, Double>();
        for (FCLRates fCLRates : getSortedList()) {
            if (fCLRates.size() >= 1) {
                for (SSLine ssl : fCLRates) {
                    ChargeCode chargeCode = ssl.getCollapsedChargeCode(fileType);
                    if (chargeCode.isEmpty()) {
                        getSortedList().removeAll(ssl);
                    } else {
                        for (String type : UnitCost.getCostTypes()) {
                            int index = chargeCode.indexOf(new UnitCost(type));
                            if (index > -1) {
                                UnitCost unitCost = (UnitCost) chargeCode.get(index);
                                if (unitCost.getActiveAmount() != 0) {
                                    double amount = null != lowerCostAllCities.get(type) ? lowerCostAllCities.get(type) : unitCost.getActiveAmount();
                                    if (amount > unitCost.getActiveAmount()) {
                                        lowerCostAllCities.put(type, unitCost.getActiveAmount());
                                    } else {
                                        lowerCostAllCities.put(type, amount);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        String commodity = "";
        String sslineNos = "";
        for (FCLRates fCLRates : getSortedList()) {
            String _city = null;
            _city = getService().equals("Origin") ? fCLRates.getDestination() : fCLRates.getOrigin();
            commodity = getCommodityCode();
            bulletRateSslines.clear();
            if (null != commodity && !commodity.equals("")) {
                sslineNos = fclBuyDAO.findMultipleSsl(fCLRates.getOriginId(), fCLRates.getDestinationId(), commodity, isHazardous());
                bulletRatesCommodities = genericCodeDAO.findForAllCommodityCode(sslineNos, fCLRates.getOriginId(), fCLRates.getDestinationId());
                for (LabelValueBean bean : bulletRatesCommodities) {
                    bulletRateSslines.add(bean.getValue());
                }
            }
            selectedList.add(_city);
            StringBuffer topHeader = new StringBuffer();
            details.append("<div id=\"").append(_city).append("_collapse\" style='width: 100%;height:200px;overflow:scroll'>");
            details.append("<div><b><font size='2'>");
            details.append(_city);
            details.append("</font></div>");
            details.append("<table class='scrolldisplaytablenew' id='normalTable' width='100%'>");
            details.append(header);
            StringBuilder row = new StringBuilder();
            boolean isFclRate = true;
            if (mapCityList == null) {
                mapCityList = getService().equals("Origin") ? fCLRates.getDestination() : fCLRates.getOrigin();
            } else {
                String city = getService().equals("Origin") ? fCLRates.getDestination() : fCLRates.getOrigin();
                mapCityList = mapCityList + ";" + city;
            }
            topHeader.append("<tr style='background:#99C68E;font-weight:bold'>");
            if (this.imsRates) {
                topHeader.append("<td  align='left' colspan='").append(headerTdLength).append("'>");
            } else {
                topHeader.append("<td  align='left' colspan='").append(headerTdLength).append(1).append("'>");
            }
            String miles = null != fCLRates.getDistance() ? "<font color='#151B8D'>" + nf.format(fCLRates.getDistance()) + " Miles</font>" : "";
            topHeader.append(fCLRates.getOriginName()).append(" to ").append(fCLRates.getDestinationName()).append(" ").append(miles);
            topHeader.append("</td>");
            if (this.imsRates) {
                if (null != fCLRates.getImsModel() && null != fCLRates.getImsModel().getLowestQuote()
                        && CommonUtils.isNotEmpty(fCLRates.getImsModel().getLowestQuote().getAllIn2Rate())) {
                    IMSQuote lowestQuote = fCLRates.getImsModel().getLowestQuote();
                    topHeader.append("<td align='left' colspan='").append(headerTdLength).append(1).append("'>");
                    topHeader.append("<div>");
                    topHeader.append("Inland Quote :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    if (fCLRates.getImsModel().isHazardous()) {
                        topHeader.append("<span onmouseover='showBreakDownWithHaz(").append(lowestQuote.getQuoteAmount()).append(",").append(lowestQuote.getFuelFees()).append(",").append(lowestQuote.getAllInRate()).append(",").append(lowestQuote.getHazardousFees()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                    } else {
                        topHeader.append("<span onmouseover='showBreakDown(").append(lowestQuote.getQuoteAmount()).append(",").append(lowestQuote.getFuelFees()).append(",").append(lowestQuote.getAllInRate()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                    }
                    topHeader.append("Buy:  $").append(lowestQuote.getAllInRate()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    topHeader.append("</span>");
                    if (lowestQuote.isHazardous()) {
                        topHeader.append("<span onmouseover='showBreakDownWithHaz(").append(lowestQuote.getQuote2Amt()).append(",").append(lowestQuote.getFuel2Fees()).append(",").append(lowestQuote.getAllIn2Rate()).append(",").append(lowestQuote.getHazardousFees()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                    } else {
                        topHeader.append("<span onmouseover='showBreakDown(").append(lowestQuote.getQuote2Amt()).append(",").append(lowestQuote.getFuel2Fees()).append(",").append(lowestQuote.getAllIn2Rate()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                    }
                    topHeader.append("Sell:  $").append(lowestQuote.getAllIn2Rate()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    topHeader.append("</span>");
                    if (CommonUtils.isEmpty(lowestQuote.getTrucker())) {
                        lowestQuote.setTruckerName("IMS LLC");
                        lowestQuote.setTrucker(tradingPartnerDAO.getTradingpatnerAccNo("IMS LLC"));
                    }
                    topHeader.append("<span comment='<b style=&quot;color:red&quot;>Account Name:</b> ").append(lowestQuote.getTruckerName()).append("<br/><b style=&quot;color:red&quot;>Address:</b> ").append(custAddressDAO.getAddressAndPhone(lowestQuote.getTrucker()).getAddress1()).append("<br/><b style=&quot;color:red&quot;>Phone:</b> ").append(custAddressDAO.getAddressAndPhone(lowestQuote.getTrucker()).getPhone()).append("<br/>").append("' class='more-info' style='color: blue;cursor: pointer'>");
                    topHeader.append("Trucker:  ").append(lowestQuote.getTrucker()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    topHeader.append("</span>");
                    if (CommonUtils.isNotEmpty(fCLRates.getImsModel().getAdditionalQuotes())) {
                        topHeader.append("<img src='").append(path).append("/img/icons/iicon.png' style='width:16px;height:16px' comment='<b style=&quot;color:red&quot;> Port:</b> ").append(lowestQuote.getOriginName()).append("' class='more-info'>");
                    }
                    topHeader.append("<input type='hidden' value='").append(lowestQuote.getTrucker()).append("' class='imsTrucker' />");
                    topHeader.append("<input type='hidden' value='").append(lowestQuote.getAllInRate()).append("' class='imsBuy' />");
                    topHeader.append("<input type='hidden' value='").append(lowestQuote.getAllIn2Rate()).append("' class='imsSell' />");
                    topHeader.append("<input type='hidden' value='").append(lowestQuote.getQuoteNumber()).append("' class='imsQuoteNo' />");
                    topHeader.append("<input type='hidden' value='").append(fCLRates.getImsModel().getLocation()).append("' class='imsLocation' />");
                    topHeader.append("<input name='imsQuote'  id = 'imsCheck_").append(fCLRates.getOriginId()).append(":").append(fCLRates.getDestinationId()).append("' type='checkbox' class='ims_checkbox' onclick='oneSelected(this)' value='").append(fCLRates.getOriginName()).append("' onmouseover='imsToopTip()' onmouseout='tooltip.hide();'/>");
                    if (CommonUtils.isNotEmpty(fCLRates.getImsModel().getAdditionalQuotes())) {
                        topHeader.append("<img src='").append(path).append("/img/icons/toggle.gif' class='toggle' comment='See Additional Rates' onmouseover='showToolTip(this,null,event)' onmouseout='tooltip.hide()' onclick='showAdditionalRates(this)'/>");
                        topHeader.append("<img src='").append(path).append("/img/icons/toggle_collapse.gif' class='toggle_collapse' comment='Hide Additional Rates' onmouseover='showToolTip(this,null,event)' onmouseout='tooltip.hide()' onclick='hideAdditionalRates(this)' style='display:none'/>");
                        topHeader.append("</div>");
                        topHeader.append("<div class='additionalRates' style='display:none'>");
                        for (IMSQuote additionalQuote : fCLRates.getImsModel().getAdditionalQuotes()) {
                            topHeader.append("<div>");
                            topHeader.append("Inland Quote :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                            if (fCLRates.getImsModel().isHazardous()) {
                                topHeader.append("<span onmouseover='showBreakDownWithHaz(").append(additionalQuote.getQuoteAmount()).append(",").append(additionalQuote.getFuelFees()).append(",").append(additionalQuote.getAllInRate()).append(",").append(additionalQuote.getHazardousFees()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                            } else {
                                topHeader.append("<span onmouseover='showBreakDown(").append(additionalQuote.getQuoteAmount()).append(",").append(additionalQuote.getFuelFees()).append(",").append(additionalQuote.getAllInRate()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                            }
                            topHeader.append("Buy:  $").append(additionalQuote.getAllInRate()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                            topHeader.append("</span>");
                            if (additionalQuote.isHazardous()) {
                                topHeader.append("<span onmouseover='showBreakDownWithHaz(").append(additionalQuote.getQuote2Amt()).append(",").append(additionalQuote.getFuel2Fees()).append(",").append(additionalQuote.getAllIn2Rate()).append(",").append(additionalQuote.getHazardousFees()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                            } else {
                                topHeader.append("<span onmouseover='showBreakDown(").append(additionalQuote.getQuote2Amt()).append(",").append(additionalQuote.getFuel2Fees()).append(",").append(additionalQuote.getAllIn2Rate()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                            }
                            topHeader.append("Sell:  $").append(additionalQuote.getAllIn2Rate()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                            topHeader.append("</span>");
                            if (CommonUtils.isEmpty(additionalQuote.getTrucker())) {
                                additionalQuote.setTruckerName("IMS LLC");
                                additionalQuote.setTrucker(tradingPartnerDAO.getTradingpatnerAccNo("IMS LLC"));
                            }
                            topHeader.append("<span comment='<b style=&quot;color:red&quot;>Account Name:</b> ").append(additionalQuote.getTruckerName()).append("<br/><b style=&quot;color:red&quot;>Address:</b> ").append(custAddressDAO.getAddressAndPhone(additionalQuote.getTrucker()).getAddress1()).append("<br/><b style=&quot;color:red&quot;>Phone:</b> ").append(custAddressDAO.getAddressAndPhone(additionalQuote.getTrucker()).getPhone()).append("' class='more-info' style='color: blue;cursor: pointer'>");
                            topHeader.append("Trucker:  ").append(additionalQuote.getTrucker()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                            topHeader.append("</span>");
                            topHeader.append("<img src='").append(path).append("/img/icons/iicon.png' style='width:16px;height:16px' comment='<b style=&quot;color:red&quot;>Port:</b> ").append(additionalQuote.getOriginName()).append("' class='more-info'>");
                            topHeader.append("<input type='hidden' value='").append(additionalQuote.getTrucker()).append("'class='imsTrucker' />");
                            topHeader.append("<input type='hidden' value='").append(additionalQuote.getAllInRate()).append("' class='imsBuy' />");
                            topHeader.append("<input type='hidden' value='").append(additionalQuote.getAllIn2Rate()).append("' class='imsSell' />");
                            topHeader.append("<input type='hidden' value='").append(additionalQuote.getQuoteNumber()).append("' class='imsQuoteNo' />");
                            topHeader.append("<input type='hidden' value='").append(fCLRates.getImsModel().getLocation()).append("'class='imsLocation' />");
                            topHeader.append("<input name='imsQuote'  id = 'imsCheck_").append(fCLRates.getOriginId()).append(":").append(fCLRates.getDestinationId()).append("' type='checkbox' class='ims_checkbox' onclick='oneSelected(this)' value='").append(fCLRates.getOriginName()).append("' onmouseover='imsToopTip()' onmouseout='tooltip.hide();'/>");
                            topHeader.append("</div>");
                        }
                    }
                    topHeader.append("</div>");
                    topHeader.append("</td>");
                } else {
                    topHeader.append("<td align='left' colspan='").append(headerTdLength).append(1).append("'>");
                    topHeader.append("<font color='red'>NO INLAND RATES FOR THIS CITY.<font>");
                    topHeader.append("</td>");
                }
            }
            topHeader.append("</tr>");
            int lowerTransitTime = fCLRates.getLowestTransitTime();
            Map<String, Double> lowerCost = new HashMap<String, Double>();
            if (fCLRates.size() > 1) {
                for (SSLine ssl : fCLRates) {
                    ChargeCode chargeCode = ssl.getCollapsedChargeCode(fileType);
                    if (chargeCode.isEmpty()) {
                        getSortedList().removeAll(ssl);
                    } else {
                        for (String type : UnitCost.getCostTypes()) {
                            int index = chargeCode.indexOf(new UnitCost(type));
                            if (index > -1) {
                                UnitCost unitCost = (UnitCost) chargeCode.get(index);
                                if (unitCost.getActiveAmount() != 0) {
                                    Double amount = lowerCost.get(type);
                                    amount = null == amount ? unitCost.getActiveAmount() : amount;
                                    if (null != amount) {
                                        if (amount > unitCost.getActiveAmount()) {
                                            lowerCost.put(type, unitCost.getActiveAmount());
                                        } else {
                                            lowerCost.put(type, amount);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (SSLine sSLine : fCLRates) {
                String ssline = sSLine.getSsLineNumber();
                row = new StringBuilder();
                if (isFclRate) {
                    row.append(topHeader);
                    isFclRate = false;
                }

                Quotation quotation = new Quotation();
                UnLocationDAO unLocationDAO = new UnLocationDAO();
                FclOrgDestMiscData tempFclOrgDestMiscData = new FclOrgDestMiscData();

                quotation.setFclGRIRemarks(unLocationDAO.getDestinationGRIRemarks(destination));
                String remarksSpGRI = quotation.getFclGRIRemarks() != null ? quotation.getFclGRIRemarks().replaceAll("'", "").replaceAll("\"", "&quot;") : "";


                quotation.setFclGRIRemarks(unLocationDAO.getDestinationGRIRemarks(destName, ssline, unlocCode));
                String remarksGRI = quotation.getFclGRIRemarks() != null ? quotation.getFclGRIRemarks().replaceAll("'", "").replaceAll("\"", "&quot;") : "";

                quotation.setFclTempRemarks(unLocationDAO.getDestinationTempRemarks(destination));
                String remarksTemp = quotation.getFclTempRemarks() != null ? quotation.getFclTempRemarks().replaceAll("'", "").replaceAll("\"", "&quot;") : "";

                quotation.setRatesRemarks(tempFclOrgDestMiscData.getRemarks());
                String rateRemarks = quotation.getRatesRemarks() != null ? quotation.getRatesRemarks().replaceAll("'", "").replaceAll("\"", "&quot;") : "";

               
                row.append("<tr class=").append(getStyle()).append(">");
                String remarks = sSLine.getRemarks() != null ? sSLine.getRemarks().replaceAll("'", "").replaceAll("\"", "&quot;").replaceAll("\n", "</br>") : "";
                if (!remarks.equals("") || !remarksGRI.equals("") || !remarksTemp.equals("") || !rateRemarks.equals("") || !remarksSpGRI.equals("")) {
                    row.append("<td>");
                    row.append("<span title='").append(remarks);
                    
                    String[] ratRemarksDup = rateRemarks.split("\\n");
                    for (int j = 0; j < ratRemarksDup.length; j++) {
                        row.append(ratRemarksDup[j]);
                    }
                    if(CommonUtils.isNotEmpty(ratRemarksDup)){
                        row.append("</br>");
                    }
                    
                    String[] remarksTempDup = remarksTemp.split("\\n");
                    for (int j = 0; j < remarksTempDup.length; j++) {
                        row.append(remarksTempDup[j]);
                    }
                   if(CommonUtils.isNotEmpty(remarksTempDup)){
                        row.append("</br>");
                    }
                    String[] remarksSpGRIDup = remarksSpGRI.split("\\n");
                    for (int j = 0; j < remarksSpGRIDup.length; j++) {
                        row.append(remarksSpGRIDup[j]);
                    }
                   if(CommonUtils.isNotEmpty(remarksSpGRIDup)){
                        row.append("</br>");
                    }
                    String[] remarksGRIDup = remarksGRI.split("\\n");
                    for (int j = 0; j < remarksGRIDup.length; j++) {
                        row.append(remarksGRIDup[j]);
                     } 
                    row.append("'>");
                    row.append("<font  class='destinationMarks'>*</font>");
                    row.append("</span>");
                    row.append("</td>");
                } else {
                    row.append("<td></td>");
                }
                if (bulletRateSslines.contains(sSLine.getSsLineNumber())) {
                    row.append("<td valign='middle' align='left' style='color:red;font-weight:bold' comment='Bullet Rates Available' onmouseover='showToolTip(this,null,event)' onmouseout='tooltip.hide()'>");
                } else {
                    row.append("<td valign='middle' align='left' style='color:black'>");
                }
                row.append(sSLine.getSsLineName());
                if (this.multiRates) {                   
                    row.append("<input name='sSLine' class='sSLineBox' tr='sslines'  type='checkbox' ").append("id='key_").append(idValue++).append("' ").append("onclick='selectUnitsMulti(this.value)' value='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                } else if (!this.quickRates) {
                    row.append("<input name='sSLine' class='radio' type='radio' onclick='selectUnits(this.value)' value='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                }
                row.append("</td>");
                if (null != sSLine.getTransitTime() && !sSLine.getTransitTime().equals("0") && sSLine.getTransitTime().equals("" + lowerTransitTime)) {
                    row.append("<td width='10' align='center'><span style='color:#C35617'><b>");
                    row.append(sSLine.getTransitTime()).append("</b>");
                } else {
                    row.append("<td width='10' align='center'>");
                    row.append(sSLine.getTransitTime());
                }
                row.append("</td>");
                row.append("<td width='15' align='center'>");
                row.append(sSLine.getPortOfExit());
                row.append("</td>");
                row.append("<td width='15'>");
                if (null != sSLine.getLocalDrayage() && sSLine.getLocalDrayage().equalsIgnoreCase("Y")) {
                    row.append("<font size='2' class='red'><b>").append(sSLine.getLocalDrayage()).append("</b></font>");
                } else {
                    row.append(sSLine.getLocalDrayage());
                }
                row.append("</td>");
                ChargeCode chargeCode = sSLine.getCollapsedChargeCode(fileType);

                if (chargeCode.isEmpty()) {
                    getSortedList().removeAll(sSLine);
                } else {
                    row.append("<td align='left' width='120'>");
                    row.append(chargeCode.getChargeCodeDesc());
                    row.append("</td>");
                    String availableUnit = "";
                    for (String unitType : CommonConstants.getUnitCostTypeListInOrder()) {
                        for (String type : UnitCost.getCostTypes()) {
                            if (unitType.equals(type)) {
                                int index = chargeCode.indexOf(new UnitCost(type));
                                if (index > -1) {
                                    UnitCost unitCost = (UnitCost) chargeCode.get(index);
                                    row.append("<td>");
                                    if (null != lowerCostAllCities.get(type) && unitCost.getActiveAmount() == lowerCostAllCities.get(type) && !imsRates) {
                                        row.append("<font style='background-color:yellow;color: #C35617'><b>").append(CommonConstants.formatAmount(unitCost.getActiveAmount())).append("</b></font>");
                                    } else if (null != lowerCost.get(type) && unitCost.getActiveAmount() == lowerCost.get(type)) {
                                        row.append("<font color='#C35617'><b>").append(CommonConstants.formatAmount(unitCost.getActiveAmount())).append("</b></font>");
                                    } else {
                                        row.append(CommonConstants.formatAmount(unitCost.getActiveAmount()));
                                    }
                                    row.append("</td>");
                                    if (availableUnit.equals("")) {
                                        availableUnit = unitType;
                                    } else {
                                        availableUnit = availableUnit + "," + unitType;
                                    }
                                } else {
                                    row.append("<td></td>");
                                }
                                break;
                            }
                        }
                    }

                    row.append("<input type='hidden' value='").append(availableUnit).append("' id='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                    row.append("<span style='display:none' id='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("_OLD' >");
                    row.append(sSLine.getSsLineName()).append("-;").append(sSLine.getSsLineNumber()).append("-;").append(fCLRates.getOriginName()).append("-;").append(fCLRates.getDestinationName()).append("-;").append(this.commodityName).append("-;").append(sSLine.getLocalDrayage());
                    row.append("</span>");
                    row.append("</tr>");
                    table.append(row);
                    details.append(row);
                }
            }
            details.append("</table></div>");
        }
        table.append("</table>");
        setCollapseMode(table.toString());
        setCityMap(mapCityList);
        setCityDetails(details.toString());
        return table.toString();
    }

    public String getImsCollapsedTable(String fileType) throws Exception {
        StringBuilder table = new StringBuilder();
        StringBuilder header = getCollopsedHeader();
        table.append("<div></div>");
        table.append("<table class='scrolldisplaytablenew' id='normalTable' width='100%'>");
        table.append(header);
        String mapCityList = null;
        StringBuffer details = new StringBuffer();
        selectedList = new ArrayList<String>();
        Map<String, Double> imsLowerCost = new HashMap<String, Double>();
        double imsSellAmount = 0d;
        for (FCLRates fCLRates : getSortedList()) {
            if (this.imsRates) {
                if (null != fCLRates.getImsModel()
                        && null != fCLRates.getImsModel().getLowestQuote() && CommonUtils.isNotEmpty(fCLRates.getImsModel().getLowestQuote().getAllIn2Rate())) {
                    imsSellAmount = Double.parseDouble(fCLRates.getImsModel().getLowestQuote().getAllIn2Rate().replace(",", ""));
                } else {
                    imsSellAmount = 0d;
                }
            }
            if (fCLRates.size() >= 1 && null != fCLRates.getImsModel() && null != fCLRates.getImsModel().getLowestQuote()) {
                for (SSLine ssl : fCLRates) {
                    ChargeCode chargeCode = ssl.getCollapsedChargeCode(fileType);
                    if (chargeCode.isEmpty()) {
                        getSortedList().removeAll(ssl);
                    } else {
                        for (String type : UnitCost.getCostTypes()) {
                            int index = chargeCode.indexOf(new UnitCost(type));
                            if (index > -1) {
                                UnitCost unitCost = (UnitCost) chargeCode.get(index);
                                if (unitCost.getActiveAmount() != 0) {
                                    Double amount = imsLowerCost.get(type);
                                    amount = null == amount ? unitCost.getActiveAmount() + imsSellAmount : amount;
                                    if (null != amount) {
                                        if (amount > unitCost.getActiveAmount() + imsSellAmount) {
                                            imsLowerCost.put(type, unitCost.getActiveAmount() + imsSellAmount);
                                        } else {
                                            imsLowerCost.put(type, amount);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        String commodity = "";
        String sslineNos = "";
        for (FCLRates fCLRates : getSortedList()) {
            String _city = null;
            _city = getService().equals("Origin") ? fCLRates.getDestination() : fCLRates.getOrigin();
            selectedList.add(_city);
            commodity = getCommodityCode();
            bulletRateSslines.clear();
            if (null != commodity && !commodity.equals("")) {
                sslineNos = fclBuyDAO.findMultipleSsl(fCLRates.getOriginId(), fCLRates.getDestinationId(), commodity, isHazardous());
                bulletRatesCommodities = genericCodeDAO.findForAllCommodityCode(sslineNos, fCLRates.getOriginId(), fCLRates.getDestinationId());
                for (LabelValueBean bean : bulletRatesCommodities) {
                    bulletRateSslines.add(bean.getValue());
                }
            }
            StringBuffer topHeader = new StringBuffer();
            details.append("<div id=\"").append(_city).append("_collapse\" style='width: 100%;height:200px;overflow:scroll'>");
            details.append("<div><b><font size='2'>");
            details.append(_city);
            details.append("</font></div>");
            details.append("<table class='scrolldisplaytablenew' id='normalTable' width='100%'>");
            details.append(header);
            StringBuilder row = new StringBuilder();
            boolean isFclRate = true;
            if (mapCityList == null) {
                mapCityList = getService().equals("Origin") ? fCLRates.getDestination() : fCLRates.getOrigin();
            } else {
                String city = getService().equals("Origin") ? fCLRates.getDestination() : fCLRates.getOrigin();
                mapCityList = mapCityList + ";" + city;
            }
            topHeader.append("<tr style='background:#99C68E;font-weight:bold'>");
            if (this.imsRates) {
                topHeader.append("<td  align='left' colspan='").append(headerTdLength).append("'>");
            } else {
                topHeader.append("<td  align='left' colspan='").append(headerTdLength).append(1).append("'>");
            }
            String miles = null != fCLRates.getDistance() ? "<font color='#151B8D'>" + nf.format(fCLRates.getDistance()) + " Miles</font>" : "";
            topHeader.append(fCLRates.getOriginName()).append(" to ").append(fCLRates.getDestinationName()).append(" ").append(miles);
            topHeader.append("</td>");
            if (this.imsRates) {
                if (null != fCLRates.getImsModel() && null != fCLRates.getImsModel().getLowestQuote() && CommonUtils.isNotEmpty(fCLRates.getImsModel().getLowestQuote().getAllIn2Rate())) {
                    TradingPartnerDAO tradingPartnerDAO = new TradingPartnerDAO();
                    IMSQuote lowestQuote = fCLRates.getImsModel().getLowestQuote();
                    topHeader.append("<td align='left' colspan='").append(headerTdLength).append(1).append("'>");
                    topHeader.append("<div>");
                    topHeader.append("Inland Quote :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    if (fCLRates.getImsModel().isHazardous()) {
                        topHeader.append("<span onmouseover='showBreakDownWithHaz(").append(lowestQuote.getQuoteAmount()).append(",").append(lowestQuote.getFuelFees()).append(",").append(lowestQuote.getAllInRate()).append(",").append(lowestQuote.getHazardousFees()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                    } else {
                        topHeader.append("<span onmouseover='showBreakDown(").append(lowestQuote.getQuoteAmount()).append(",").append(lowestQuote.getFuelFees()).append(",").append(lowestQuote.getAllInRate()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                    }
                    topHeader.append("Buy:  $").append(lowestQuote.getAllInRate()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    topHeader.append("</span>");
                    if (lowestQuote.isHazardous()) {
                        topHeader.append("<span onmouseover='showBreakDownWithHaz(").append(lowestQuote.getQuote2Amt()).append(",").append(lowestQuote.getFuel2Fees()).append(",").append(lowestQuote.getAllIn2Rate()).append(",").append(lowestQuote.getHazardousFees()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                    } else {
                        topHeader.append("<span onmouseover='showBreakDown(").append(lowestQuote.getQuote2Amt()).append(",").append(lowestQuote.getFuel2Fees()).append(",").append(lowestQuote.getAllIn2Rate()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                    }
                    topHeader.append("Sell:  $").append(lowestQuote.getAllIn2Rate()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    topHeader.append("</span>");
                    if (CommonUtils.isEmpty(lowestQuote.getTrucker())) {
                        lowestQuote.setTruckerName("IMS LLC");
                        lowestQuote.setTrucker(tradingPartnerDAO.getTradingpatnerAccNo("IMS LLC"));
                    }
                    topHeader.append("<span comment='<b style=&quot;color:red&quot;>Account Name:</b> ").append(lowestQuote.getTruckerName()).append("<br/><b style=&quot;color:red&quot;>Address:</b> ").append(custAddressDAO.getAddressAndPhone(lowestQuote.getTrucker()).getAddress1()).append("<br/><b style=&quot;color:red&quot;>Phone:</b> ").append(custAddressDAO.getAddressAndPhone(lowestQuote.getTrucker()).getPhone()).append("<br/>").append("' class='more-info' style='color: blue;cursor: pointer'>");
                    topHeader.append("Trucker:  ").append(lowestQuote.getTrucker()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    topHeader.append("</span>");
                    if (CommonUtils.isNotEmpty(fCLRates.getImsModel().getAdditionalQuotes())) {
                        topHeader.append("<img src='").append(path).append("/img/icons/iicon.png' style='width:16px;height:16px' comment='<b style=&quot;color:red&quot;>Port:</b> ").append(lowestQuote.getOriginName()).append("' class='more-info'>");
                    }
                    topHeader.append("<input type='hidden' value='").append(lowestQuote.getTrucker()).append("' class='imsTrucker' />");
                    topHeader.append("<input type='hidden' value='").append(lowestQuote.getAllInRate()).append("' class='imsBuy' />");
                    topHeader.append("<input type='hidden' value='").append(lowestQuote.getAllIn2Rate()).append("' class='imsSell' />");
                    topHeader.append("<input type='hidden' value='").append(lowestQuote.getQuoteNumber()).append("' class='imsQuoteNo' />");
                    topHeader.append("<input type='hidden' value='").append(fCLRates.getImsModel().getLocation()).append("' class='imsLocation' />");
                    topHeader.append("<input name='imsQuote'  id = 'imsCheck_").append(fCLRates.getOriginId()).append(":").append(fCLRates.getDestinationId()).append("' type='checkbox' class='ims_checkbox' onclick='oneSelected(this)' value='").append(fCLRates.getOriginName()).append("' onmouseover='imsToopTip()' onmouseout='tooltip.hide();'/>");
                    if (CommonUtils.isNotEmpty(fCLRates.getImsModel().getAdditionalQuotes())) {
                        topHeader.append("<img src='").append(path).append("/img/icons/toggle.gif' class='toggle' comment='See Additional Rates' onmouseover='showToolTip(this,null,event)' onmouseout='tooltip.hide()' onclick='showAdditionalRates(this)'/>");
                        topHeader.append("<img src='").append(path).append("/img/icons/toggle_collapse.gif' class='toggle_collapse' comment='Hide Additional Rates' onmouseover='showToolTip(this,null,event)' onmouseout='tooltip.hide()' onclick='hideAdditionalRates(this)' style='display:none'/>");
                        topHeader.append("</div>");
                        topHeader.append("<div class='additionalRates' style='display:none'>");
                        for (IMSQuote additionalQuote : fCLRates.getImsModel().getAdditionalQuotes()) {
                            topHeader.append("<div>");
                            topHeader.append("Inland Quote :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                            if (fCLRates.getImsModel().isHazardous()) {
                                topHeader.append("<span onmouseover='showBreakDownWithHaz(").append(additionalQuote.getQuoteAmount()).append(",").append(additionalQuote.getFuelFees()).append(",").append(additionalQuote.getAllInRate()).append(",").append(additionalQuote.getHazardousFees()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                            } else {
                                topHeader.append("<span onmouseover='showBreakDown(").append(additionalQuote.getQuoteAmount()).append(",").append(additionalQuote.getFuelFees()).append(",").append(additionalQuote.getAllInRate()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                            }
                            topHeader.append("Buy:  $").append(additionalQuote.getAllInRate()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                            topHeader.append("</span>");
                            if (additionalQuote.isHazardous()) {
                                topHeader.append("<span onmouseover='showBreakDownWithHaz(").append(additionalQuote.getQuote2Amt()).append(",").append(additionalQuote.getFuel2Fees()).append(",").append(additionalQuote.getAllIn2Rate()).append(",").append(additionalQuote.getHazardousFees()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                            } else {
                                topHeader.append("<span onmouseover='showBreakDown(").append(additionalQuote.getQuote2Amt()).append(",").append(additionalQuote.getFuel2Fees()).append(",").append(additionalQuote.getAllIn2Rate()).append(")' onmouseout='tooltip.hide();' style='color: blue;cursor: pointer'>");
                            }
                            topHeader.append("Sell:  $").append(additionalQuote.getAllIn2Rate()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                            topHeader.append("</span>");
                            if (CommonUtils.isEmpty(additionalQuote.getTrucker())) {
                                additionalQuote.setTruckerName("IMS LLC");
                                additionalQuote.setTrucker(tradingPartnerDAO.getTradingpatnerAccNo("IMS LLC"));
                            }
                            topHeader.append("<span comment='<b style=&quot;color:red&quot;>Account Name:</b> ").append(additionalQuote.getTruckerName()).append("<br/><b style=&quot;color:red&quot;>Address:</b> ").append(custAddressDAO.getAddressAndPhone(additionalQuote.getTrucker()).getAddress1()).append("<br/><b style=&quot;color:red&quot;>Phone:</b> ").append(custAddressDAO.getAddressAndPhone(additionalQuote.getTrucker()).getPhone()).append("<br/>").append("' class='more-info' style='color: blue;cursor: pointer'>");
                            topHeader.append("Trucker:  ").append(additionalQuote.getTrucker()).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                            topHeader.append("</span>");
                            topHeader.append("<img src='").append(path).append("/img/icons/iicon.png' style='width:16px;height:16px' comment='<b style=&quot;color:red&quot;>Port:</b> ").append(additionalQuote.getOriginName()).append("' class='more-info'>");
                            topHeader.append("<input type='hidden' value='").append(additionalQuote.getTrucker()).append("'class='imsTrucker' />");
                            topHeader.append("<input type='hidden' value='").append(additionalQuote.getAllInRate()).append("' class='imsBuy' />");
                            topHeader.append("<input type='hidden' value='").append(additionalQuote.getAllIn2Rate()).append("' class='imsSell' />");
                            topHeader.append("<input type='hidden' value='").append(additionalQuote.getQuoteNumber()).append("' class='imsQuoteNo' />");
                            topHeader.append("<input type='hidden' value='").append(fCLRates.getImsModel().getLocation()).append("'class='imsLocation' />");
                            topHeader.append("<input name='imsQuote'  id = 'imsCheck_").append(fCLRates.getOriginId()).append(":").append(fCLRates.getDestinationId()).append("' type='checkbox' class='ims_checkbox' onclick='oneSelected(this)' value='").append(fCLRates.getOriginName()).append("' onmouseover='imsToopTip()' onmouseout='tooltip.hide();'/>");
                            topHeader.append("</div>");
                        }
                    }
                    topHeader.append("</div>");
                    topHeader.append("</td>");
                } else {
                    topHeader.append("<td align='left' colspan='").append(headerTdLength).append(1).append("'>");
                    topHeader.append("<font color='red'>NO INLAND RATES FOR THIS CITY.<font>");
                    topHeader.append("</td>");
                }
            }
            topHeader.append("</tr>");
            int lowerTransitTime = fCLRates.getLowestTransitTime();
            Map<String, Double> lowerCost = new HashMap<String, Double>();
            if (fCLRates.size() > 1) {
                for (SSLine ssl : fCLRates) {
                    ChargeCode chargeCode = ssl.getCollapsedChargeCode(fileType);
                    if (chargeCode.isEmpty()) {
                        getSortedList().removeAll(ssl);
                    } else {
                        for (String type : UnitCost.getCostTypes()) {
                            int index = chargeCode.indexOf(new UnitCost(type));
                            if (index > -1) {
                                UnitCost unitCost = (UnitCost) chargeCode.get(index);
                                if (unitCost.getActiveAmount() != 0) {
                                    Double amount = lowerCost.get(type);
                                    amount = null == amount ? unitCost.getActiveAmount() : amount;
                                    if (null != amount) {
                                        if (amount > unitCost.getActiveAmount()) {
                                            lowerCost.put(type, unitCost.getActiveAmount());
                                        } else {
                                            lowerCost.put(type, amount);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (SSLine sSLine : fCLRates) {
                row = new StringBuilder();
                if (isFclRate) {
                    row.append(topHeader);
                    isFclRate = false;
                }
                row.append("<tr class=").append(getStyle()).append(">");
                String remarks = sSLine.getRemarks() != null ? sSLine.getRemarks().replaceAll("'", "").replaceAll("\"", "&quot;").replaceAll("\n", "</br>") : "";
                if (!remarks.equals("")) {
                    row.append("<td>");
                    row.append("<span title='").append(remarks).append("'>");
                    row.append("<font  class='destinationMarks'>*</font>");
                    row.append("</span>");
                    row.append("</td>");
                } else {
                    row.append("<td></td>");
                }
                if (bulletRateSslines.contains(sSLine.getSsLineNumber())) {
                    row.append("<td valign='middle' align='left' style='color:red;font-weight:bold' comment='Bullet Rates Available' onmouseover='showToolTip(this,null,event)' onmouseout='tooltip.hide()'>");
                } else {
                    row.append("<td valign='middle' align='left' style='color:black'>");
                }
                row.append(sSLine.getSsLineName());
                if (this.multiRates) {
                    row.append("<input name='sSLine' class='sSLineBox' id='sSLine' type='checkbox' onclick='selectUnitsMulti(this.value)' value='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                } else if (!this.quickRates) {
                    row.append("<input name='sSLine' class='radio' type='radio' onclick='selectUnits(this.value)' value='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                }
                row.append("</td>");
                if (null != sSLine.getTransitTime() && !sSLine.getTransitTime().equals("0") && sSLine.getTransitTime().equals("" + lowerTransitTime)) {
                    row.append("<td width='10' align='center'><span style='color:#C35617'><b>");
                    row.append(sSLine.getTransitTime()).append("</b>");
                } else {
                    row.append("<td width='10' align='center'>");
                    row.append(sSLine.getTransitTime());
                }
                row.append("</td>");
                row.append("<td width='15' align='center'>");
                row.append(sSLine.getPortOfExit());
                row.append("</td>");
                row.append("<td width='15'>");
                if (null != sSLine.getLocalDrayage() && sSLine.getLocalDrayage().equalsIgnoreCase("Y")) {
                    row.append("<font size='2' class='red'><b>").append(sSLine.getLocalDrayage()).append("</b></font>");
                } else {
                    row.append(sSLine.getLocalDrayage());
                }
                row.append("</td>");
                ChargeCode chargeCode = sSLine.getCollapsedChargeCode(fileType);
               if(chargeCode.isEmpty()){
                    getSortedList().removeAll(sSLine);
                } else {
                row.append("<td align='left'>");
                row.append(chargeCode.getChargeCodeDesc());
                row.append("</td>");
                String availableUnit = "";
                for (String unitType : CommonConstants.getUnitCostTypeListInOrder()) {
                    for (String type : UnitCost.getCostTypes()) {
                        if (unitType.equals(type)) {
                            int index = chargeCode.indexOf(new UnitCost(type));
                            if (index > -1) {
                                UnitCost unitCost = (UnitCost) chargeCode.get(index);
                                row.append("<td>");
                                if (null != fCLRates.getImsModel() && null != fCLRates.getImsModel().getLowestQuote() && CommonUtils.isNotEmpty(fCLRates.getImsModel().getLowestQuote().getAllIn2Rate())) {
                                    imsSellAmount = Double.parseDouble(fCLRates.getImsModel().getLowestQuote().getAllIn2Rate().replace(",", ""));
                                    if (null != imsLowerCost.get(type) && unitCost.getActiveAmount() + imsSellAmount == imsLowerCost.get(type)) {
                                        row.append("<font style='background-color:yellow;' color='#C35617'><b>").append(CommonConstants.formatAmount(unitCost.getActiveAmount() + imsSellAmount)).append("</b></font>");
                                    } else {
                                        if (null != lowerCost.get(type) && unitCost.getActiveAmount() == lowerCost.get(type)) {
                                            row.append("<font color='#C35617'><b>").append(CommonConstants.formatAmount(unitCost.getActiveAmount() + imsSellAmount)).append("</b></font>");
                                        } else {
                                            row.append(CommonConstants.formatAmount(unitCost.getActiveAmount() + imsSellAmount));
                                        }
                                    }
                                } else {
                                    if (null != lowerCost.get(type) && unitCost.getActiveAmount() == lowerCost.get(type)) {
                                        row.append("<font color='#C35617'><b>").append(CommonConstants.formatAmount(unitCost.getActiveAmount())).append("</b></font>");
                                    } else {
                                        row.append(CommonConstants.formatAmount(unitCost.getActiveAmount()));
                                    }
                                }
                                row.append("</td>");
                                if (availableUnit.equals("")) {
                                    availableUnit = unitType;
                                } else {
                                    availableUnit = availableUnit + "," + unitType;
                                }
                            } else {
                                row.append("<td></td>");
                            }
                            break;
                        }
                    }
                }
                row.append("<input type='hidden' value='").append(availableUnit).append("' id='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                row.append("<span style='display:none' id='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("_OLD' >");
                row.append(sSLine.getSsLineName()).append("-;").append(sSLine.getSsLineNumber()).append("-;").append(fCLRates.getOriginName()).append("-;").append(fCLRates.getDestinationName()).append("-;").append(this.commodityName).append("-;").append(sSLine.getLocalDrayage());
                row.append("</span>");
                row.append("</tr>");
                table.append(row);
                details.append(row);
            }
            }
            details.append("</table></div>");
        }
        table.append("</table>");
        setCollapseMode(table.toString());
        setCityMap(mapCityList);
        setCityDetails(details.toString());
        return table.toString();
    }

    public String getExpandedTableNormal(String fileType) throws Exception {
        StringBuilder table = new StringBuilder();
        StringBuilder header = getHeader();
        String commodity = "";
        String sslineNos = "";
        table.append("<div></div>");
        table.append("<table class='scrolldisplaytablenew' id='normalTable' width='100%'>");
        table.append(header);
        StringBuffer details = new StringBuffer();
        for (FCLRates fCLRates : getSortedList()) {
            StringBuilder row = new StringBuilder();
            boolean isFclRate = true;
            StringBuffer topHeader = new StringBuffer();
            String _city = getService().equals("Origin") ? fCLRates.getDestination() : fCLRates.getOrigin();
            commodity = getCommodityCode();
            bulletRateSslines.clear();
            if (null != commodity && !commodity.equals("")) {
                sslineNos = fclBuyDAO.findMultipleSsl(fCLRates.getOriginId(), fCLRates.getDestinationId(), commodity, isHazardous());
                bulletRatesCommodities = genericCodeDAO.findForAllCommodityCode(sslineNos, fCLRates.getOriginId(), fCLRates.getDestinationId());
                for (LabelValueBean bean : bulletRatesCommodities) {
                    bulletRateSslines.add(bean.getValue());
                }
            }
            details.append("<div id=\"").append(_city).append("_expand\" style='width: 100%;height:200px;overflow:scroll'>");
            details.append("<div><b><font size='2'>");
            details.append(_city);
            details.append("</font></div>");
            details.append("<table class='scrolldisplaytablenew' id='normalTable' width='100%'>");
            details.append(getMapHeader(fCLRates.getOriginId() + ";" + fCLRates.getDestinationId() + ";" + this.commodityCode).toString());
            topHeader.append("<tr style='background:#99C68E;font-weight:bold'>");
            String miles = null != fCLRates.getDistance() ? "<font color='#151B8D'>" + nf.format(fCLRates.getDistance()) + " Miles</font>" : "";
            topHeader.append("<td width='100%' align='left' colspan='").append(headerTdLength).append(1).append("'>");
            topHeader.append(fCLRates.getOriginName()).append(" to ").append(fCLRates.getDestinationName()).append(" ").append(miles);
            topHeader.append("</td>");
            topHeader.append("</tr>");
            for (SSLine sSLine : fCLRates) {
                boolean isSsLine = true;
                row = new StringBuilder();
                if (isFclRate) {
                    row.append(topHeader);
                    isFclRate = false;
                } else {
                    row.append("<tr class=").append(getStyle()).append(">");
                    row.append("<td width='100%' colspan='").append(headerTdLength).append(1).append("' class='borderStyleBottom'>");
                    row.append("&nbsp;");
                    row.append("</td>");
                    row.append("</tr>");
                }
                row.append("<tr class=").append(getStyle()).append(">");
                String remarks = sSLine.getRemarks() != null ? sSLine.getRemarks().replaceAll("'", "").replaceAll("\"", "&quot;").replaceAll("\n", "</br>") : "";
                if (!remarks.equals("")) {
                    row.append("<td width='5'>");
                    row.append("<span title='").append(remarks).append("'>");
                    row.append("<font  class='destinationMarks'>*</font>");
                    row.append("</span>");
                    row.append("</td>");
                } else {
                    row.append("<td></td>");
                }
                if (bulletRateSslines.contains(sSLine.getSsLineNumber())) {
                    row.append("<td width='180' valign='middle' align='left' style='color:red;font-weight:bold' comment='Bullet Rates Available' onmouseover='showToolTip(this,null,event)' onmouseout='tooltip.hide()'>");
                } else {
                    row.append("<td width='180' valign='middle' align='left' style='color:black'>");
                }
                row.append(sSLine.getSsLineName());
                if (this.multiRates) {
                    row.append("<input name='sSLine' class='sSLineBox' id='sSLine' type='checkbox' onclick='selectUnitsMulti(this.value)' value='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                }else if (!this.quickRates) {
                    row.append("<input name='sSLine' type='radio' class='radio' onclick='selectUnits(this.value)' value='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                }
                row.append("</td>");
                row.append("<td width='10' align='center'>");
                row.append(sSLine.getTransitTime());
                row.append("</td>");
                row.append("<td width='15' align='center'>");
                row.append(sSLine.getPortOfExit());
                row.append("</td>");
                String availableUnit = "";
                for (ChargeCode chargeCode : sSLine.getNormalChargeList(fileType)) {
                    if(chargeCode.isEmpty()){
                       getSortedList().removeAll(sSLine);
                    }
                    else {
                            if (isSsLine) {
                                isSsLine = false;
                            } else {
                                row = new StringBuilder();
                                row.append("<tr class=").append(getStyle()).append(">");
                                row.append("<td width='5'></td>");
                                row.append("<td width='180'></td>");
                                row.append("<td width='10'></td>");
                                row.append("<td width='15'></td>");
                            }

                            row.append("<td align='left' width='120'>");
                            row.append(chargeCode.getChargeCodeDesc());
                            row.append("</td>");
                            for (String unitType : CommonConstants.getUnitCostTypeListInOrder()) {
                                for (String type : UnitCost.getCostTypes()) {
                                    if (unitType.equals(type)) {
                                        int index = chargeCode.indexOf(new UnitCost(type));
                                        if (index > -1) {
                                            UnitCost unitCost = (UnitCost) chargeCode.get(index);
                                            row.append("<td>");
                                            row.append(CommonConstants.formatAmount(unitCost.getCurrentTotal()));
                                            row.append("</td>");
                                            row.append("<td>");
                                            row.append(CommonConstants.formatAmount(unitCost.getCurrentMarkup()));
                                            row.append("</td>");
                                            row.append("<td class='borderStyleRight'>");
                                            row.append(CommonConstants.formatAmount(unitCost.getCurrentMarkup() + unitCost.getCurrentTotal()));
                                            row.append("</td>");
                                            if (availableUnit.equals("")) {
                                                availableUnit = unitType;
                                            } else {
                                                availableUnit = availableUnit + "," + unitType;
                                            }
                                        } else {
                                            row.append("<td colspan='3'></td>");
                                        }
                                        break;
                                    }
                                }
                            }
                            row.append("<input type='hidden' value='").append(availableUnit).append("' id='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                            row.append("<span style='display:none' id='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("_OLD' >");
                            row.append(sSLine.getSsLineName()).append("-;").append(sSLine.getSsLineNumber()).append("-;").append(fCLRates.getOriginName()).append("-;").append(fCLRates.getDestinationName()).append("-;").append(this.commodityName).append("-;").append(sSLine.getLocalDrayage());
                            row.append("</span>");
                            row.append("</tr>");
                            table.append(row);
                            details.append(row);
                        }
                    }
                }
            details.append("</table></div>");
        }
        table.append("</table>");
        setExpandMode(table.toString());
        setExpandCityDetails(details.toString());
        return table.toString();
    }

    public String getNormalTable(String fileType) throws Exception {
        StringBuilder table = new StringBuilder();
        StringBuilder header = getCollopsedHeader();
        String commodity = "";
        String sslineNos = "";
        table.append("<div></div>");
        table.append("<table class='scrolldisplaytablenew' id='normalTable' width='100%'>");
        table.append(header);
        StringBuffer details = new StringBuffer();
        for (FCLRates fCLRates : getSortedList()) {
            StringBuilder row = new StringBuilder();
            boolean isFclRate = true;
            StringBuffer topHeader = new StringBuffer();
            String _city = getService().equals("Origin") ? fCLRates.getDestination() : fCLRates.getOrigin();
            details.append("<div id=\"").append(_city).append("_normal\" style='width: 100%;height:200px;overflow:scroll'>");
            details.append("<div><b><font size='2'>");
            details.append(_city);
            commodity = getCommodityCode();
            bulletRateSslines.clear();
            if (null != commodity && !commodity.equals("")) {
                sslineNos = fclBuyDAO.findMultipleSsl(fCLRates.getOriginId(), fCLRates.getDestinationId(), commodity, isHazardous());
                bulletRatesCommodities = genericCodeDAO.findForAllCommodityCode(sslineNos, fCLRates.getOriginId(), fCLRates.getDestinationId());
                for (LabelValueBean bean : bulletRatesCommodities) {
                    bulletRateSslines.add(bean.getValue());
                }
            }
            details.append("</font></div>");
            details.append("<table class='scrolldisplaytablenew' id='normalTable' width='100%'>");
            details.append(header);
            topHeader.append("<tr style='background:#99C68E;font-weight:bold'>");
            topHeader.append("<td width='100%' align='left' colspan='").append(headerTdLength).append(1).append("'>");
            String miles = null != fCLRates.getDistance() ? "<font color='#151B8D'>" + nf.format(fCLRates.getDistance()) + " Miles</font>" : "";
            topHeader.append(fCLRates.getOrigin()).append(" to ").append(fCLRates.getDestination()).append(" ").append(miles);
            topHeader.append("</td>");
            topHeader.append("</tr>");
            for (SSLine sSLine : fCLRates) {
                boolean isSsLine = true;
                row = new StringBuilder();
                if (isFclRate) {
                    row.append(topHeader);
                    isFclRate = false;
                } else {
                    row.append("<tr class=").append(getStyle()).append(">");
                    row.append("<td width='100%' colspan='").append(headerTdLength).append(1).append("' class='borderStyleBottom'>");
                    row.append("&nbsp;");
                    row.append("</td>");
                    row.append("</tr>");
                }
                row.append("<tr class=").append(getStyle()).append(">");
                String remarks = sSLine.getRemarks() != null ? sSLine.getRemarks().replaceAll("'", "").replaceAll("\"", "&quot;").replaceAll("\n", "</br>") : "";
                if (!remarks.equals("")) {
                    row.append("<td width='5'>");
                    row.append("<span style='cursor: hand;' title='").append(remarks).append("'>");
                    row.append("<font  class='destinationMarks'>*</font>");
                    row.append("</span>");
                    row.append("</td>");
                } else {
                    row.append("<td></td>");
                }
                if (bulletRateSslines.contains(sSLine.getSsLineNumber())) {
                    row.append("<td width='250' valign='middle' align='left' style='color:red;font-weight:bold' comment='Bullet Rates Available' onmouseover='showToolTip(this,null,event)' onmouseout='tooltip.hide()'>");
                } else {
                    row.append("<td width='250' valign='middle' align='left' style='color:black'>");
                }
                row.append(sSLine.getSsLineName());
                if (this.multiRates) {
                    row.append("<input name='sSLine' class='sSLineBox' id='sSLine' type='checkbox' onclick='selectUnitsMulti(this.value)' value='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                } else if (!this.quickRates) {
                    row.append("<input name='sSLine' type='radio' class='radio' onclick='selectUnits(this.value)' value='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                }
                row.append("</td>");
                row.append("<td width='10' align='center'>");
                row.append(sSLine.getTransitTime());
                row.append("</td>");
                row.append("<td width='15' align='center'>");
                row.append(sSLine.getPortOfExit());
                row.append("</td>");
                row.append("<td width='15'>");
                if (null != sSLine.getLocalDrayage() && sSLine.getLocalDrayage().equalsIgnoreCase("Y")) {
                    row.append("<font size='2' class='red'><b>").append(sSLine.getLocalDrayage()).append("</b></font>");
                } else {
                    row.append(sSLine.getLocalDrayage());
                }
                row.append("</td>");
                for (ChargeCode chargeCode : sSLine.getExpandedChargeList(fileType)) {
                    
                    if(chargeCode.isEmpty()){
                       getSortedList().removeAll(sSLine);
                    }
                    else {
                            if (isSsLine) {
                                isSsLine = false;
                            } else {
                                row = new StringBuilder();
                                row.append("<tr class=").append(getStyle()).append(">");
                                row.append("<td width='5'></td>");
                                row.append("<td width='250'></td>");
                                row.append("<td width='10'></td>");
                                row.append("<td width='15'></td>");
                                row.append("<td width='15'></td>");
                            }

                            row.append("<td align='left' width='120'>");
                            row.append(chargeCode.getChargeCodeDesc());
                            row.append("</td>");
                            String availableUnit = "";
                            for (String unitType : CommonConstants.getUnitCostTypeListInOrder()) {
                                for (String type : UnitCost.getCostTypes()) {
                                    if (unitType.equals(type)) {
                                        int index = chargeCode.indexOf(new UnitCost(type));
                                        if (index > -1) {
                                            UnitCost unitCost = (UnitCost) chargeCode.get(index);
                                            row.append("<td>");
                                            //if(ChargeCode.OCEAN_FREIGHT.equalsIgnoreCase(chargeCode.getChargeCodeDesc())) {
                                            row.append(CommonConstants.formatAmount(unitCost.getActiveTotalAmount()));
                                            //}else {
                                            //row.append(CommonConstants.formatAmount(unitCost.getCurrentTotal() + unitCost.getCurrentMarkup()));
                                            //}
                                            row.append("</td>");
                                            if (availableUnit.equals("")) {
                                                availableUnit = unitType;
                                            } else {
                                                availableUnit = availableUnit + "," + unitType;
                                            }
                                        } else {
                                            row.append("<td></td>");
                                        }
                                        break;
                                    }
                                }
                            }
                            row.append("<input type='hidden' value='").append(availableUnit).append("' id='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("' />");
                            row.append("<span style='display:none' id='").append(fCLRates.getOriginId()).append(";").append(fCLRates.getDestinationId()).append(";").append(sSLine.getSsLineNumber()).append(";").append(this.commodityCode).append("_OLD' >");
                            row.append(sSLine.getSsLineName()).append("-;").append(sSLine.getSsLineNumber()).append("-;").append(fCLRates.getOriginName()).append("-;").append(fCLRates.getDestinationName()).append("-;").append(this.commodityName).append("-;").append(sSLine.getLocalDrayage());
                            row.append("</span>");
                            row.append("</tr>");
                            table.append(row);
                            details.append(row);
                        }
                    }
                }
            details.append("</table></div>");
        }
        table.append("</table>");
        setNormalMode(table.toString());
        setNormalCityDetails(details.toString());
        return table.toString();
    }

    private StringBuilder getHeader() throws Exception {
        StringBuilder header = new StringBuilder();
        header.append("<thead>");
        header.append("<tr align='center'>");
        header.append("<th width='5'></th>");
        header.append("<th width='190' align='left'>SSLine</th>");
        header.append("<th width='15'>TT</th>");
        header.append("<th width='15'>POE</th>");
        header.append("<th width='140' align='center'>ChargeCode</th>");
        header.append("</th>");
        headerTdLength = 0;
        for (String unitType : CommonConstants.getUnitCostTypeListInOrder()) {
            for (String type : UnitCost.getCostTypes()) {
                if (unitType.equals(type)) {
                    header.append("<th>");
                    if (!this.quickRates) {
                        header.append(type).append("<input type='checkbox' name='unitType' class='unitTypeBox' value='").append(type).append("' />");
                    } else {
                        header.append(type);
                    }
                    header.append("</th>");
                    header.append("<th>MarkUp</th>");
                    header.append("<th>Total</th>");
                    headerTdLength++;
                    break;
                }
            }
        }
        header.append("</tr>");
        header.append("</thead>");
        return header;
    }

    private StringBuilder getMapHeader(String name) throws Exception {
        StringBuilder header = new StringBuilder();
        header.append("<thead>");
        header.append("<tr align='center'>");
        header.append("<th width='5'></th>");
        header.append("<th width='160' align='left'>SSLine</th>");
        header.append("<th width='15'>TT</th>");
        header.append("<th width='20'>POE</th>");
        header.append("<th width='120' align='left'>ChargeCode</th>");
        header.append("</th>");
        headerTdLength = 0;
        for (String unitType : CommonConstants.getUnitCostTypeListInOrder()) {
            for (String type : UnitCost.getCostTypes()) {
                if (unitType.equals(type)) {
                    header.append("<th>");
                    if (!this.quickRates) {
                        header.append(type).append("<input type='checkbox' name='unitType' value='").append(type).append("' />");
                    } else {
                        header.append(type);
                    }
                    header.append("</th>");
                    header.append("<th>M</th>");
                    header.append("<th>T</th>");
                    headerTdLength++;
                    break;
                }
            }
        }
        header.append("</tr>");
        header.append("</thead>");
        return header;
    }

    private StringBuilder getCollopsedHeader() throws Exception {
        StringBuilder header = new StringBuilder();
        header.append("<thead>");
        header.append("<tr align='center'>");
        header.append("<th></th>");
        header.append("<th align='left'>SSLine</th>");
        header.append("<th width='15'>TT</th>");
        header.append("<th width='15'>POE</th>");
        header.append("<th width='15'>DRAY</th>");
        header.append("<th align='center'>ChargeCode</th>");
        header.append("</th>");
        headerTdLength = 0;
        for (String unitType : CommonConstants.getUnitCostTypeListInOrder()) {
            for (String type : UnitCost.getCostTypes()) {
                if (unitType.equals(type)) {
                    header.append("<th>");
                    if (!this.quickRates) {
                        header.append(type).append("<input type='checkbox' name='unitType' value='").append(type).append("' />");
                    } else {
                        header.append(type);
                    }
                    header.append("</th>");
                    headerTdLength++;
                    break;
                }
            }
        }
        header.append("</tr>");
        header.append("</thead>");
        return header;
    }
    private int style;
    private int previousStyle;

    private String getStyle() {
        if (style == 0) {
            style = 1;
            return "odd";
        } else if (style == 2) {
            style = previousStyle;
            return "inland";
        } else {
            style = 0;
            return "even";
        }
    }

    private String setRadioValue(String val) {
        val = "<input type='radio' name='ssLineSelected' value='" + val + "'/>";
        return val;
    }

    /**
     * @param viaport
     * @return Sort List 1.OFR,(.....charges),GRI(at End)
     */
    public SSLine sortChargeCodes(SSLine ssLine) {
        SSLine sortedViaPortCharges = new SSLine();
        sortedViaPortCharges.setSsLineName(ssLine.getSsLineName());
        sortedViaPortCharges.setSsLineNumber(ssLine.getSsLineNumber());
        List<ChargeCode> otherCharges = new ArrayList<ChargeCode>();
        for (ChargeCode charge : ssLine) {
            if ("11694".equalsIgnoreCase(charge.getChargeCode())) {
                sortedViaPortCharges.add(charge);
            } else {
                otherCharges.add(charge);
            }
        }
        //If OFR is not in the list of charge then do not add any charge in the list.
        if (sortedViaPortCharges.isEmpty()) {
            return sortedViaPortCharges;
        }

        return sortedViaPortCharges;
    }

    public String getCollapseMode() {
        return collapseMode;
    }

    public void setCollapseMode(String collapseMode) {
        this.collapseMode = collapseMode;
    }

    public String getExpandMode() {
        return expandMode;
    }

    public void setExpandMode(String expandMode) {
        this.expandMode = expandMode;
    }

    public String getNormalMode() {
        return normalMode;
    }

    public void setNormalMode(String normalMode) {
        this.normalMode = normalMode;
    }

    public boolean isHazardous() {
        return hazardous;
    }

    public void setHazardous(boolean hazardous) {
        this.hazardous = hazardous;
    }

    public boolean isQuickRates() {
        return quickRates;
    }

    public void setQuickRates(boolean quickRates) {
        this.quickRates = quickRates;
    }

    public boolean isMultiRates() {
        return multiRates;
    }

    public void setMultiRates(boolean multiRates) {
        this.multiRates = multiRates;
    } 
    
    public boolean isImsRates() {
        return imsRates;
    }

    public void setImsRates(boolean imsRates) {
        this.imsRates = imsRates;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCityMap() {
        return cityMap;
    }

    public void setCityMap(String cityMap) {
        this.cityMap = cityMap;
    }

    public String getCityDetails() {
        return cityDetails;
    }

    public void setCityDetails(String cityDetails) {
        this.cityDetails = cityDetails;
    }

    public String getExpandCityDetails() {
        return expandCityDetails;
    }

    public void setExpandCityDetails(String expandCityDetails) {
        this.expandCityDetails = expandCityDetails;
    }

    public String getNormalCityDetails() {
        return normalCityDetails;
    }

    public void setNormalCityDetails(String normalCityDetails) {
        this.normalCityDetails = normalCityDetails;
    }

    public List<String> getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(List<String> selectedList) {
        this.selectedList = selectedList;
    }

    public int compare(FCLRates o1, FCLRates o2) {
        if (null != o1.getDistance() && null != o2.getDistance()) {
            return (int) Math.signum(o1.getDistance() - o2.getDistance());
        }
        return 0;
    }
}
