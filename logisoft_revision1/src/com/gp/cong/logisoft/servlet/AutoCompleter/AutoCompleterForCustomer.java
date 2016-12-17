package com.gp.cong.logisoft.servlet.AutoCompleter;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;

public class AutoCompleterForCustomer {

    public void setCustomerDetails(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            String importFlag = request.getParameter("importFlag");
            String actType = request.getParameter("actType");
            if (textFieldId == null) {
                return;
            }
            actType=null!=actType?actType:"";
            String customer = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            TradingPartnerDAO tradingPartnerDAO=new TradingPartnerDAO();
            stringBuilder.append("<ul>");
            if (null != customer && !customer.trim().equals("")) {
                    List tradingPartners = tradingPartnerDAO.getTradingPartners(customer,actType,false);
                    if(CommonUtils.isNotEmpty(tradingPartners)){
                        String delimiter = " <--> ";
                        for(Object row : tradingPartners){
                            Object[] col = (Object[])row;
                            String acctName = (String)col[0];
                            String acctNo = (String)col[1];
                            String acctType = (String)col[2];
                            String subType = (String)col[3];
                            String type = (String)col[4];
                            String address = (String)col[5];
                            String city = (String)col[6];
                            String state = (String)col[7];
                            String country = (String)col[8];
                            stringBuilder.append("<li id='").append(acctNo).append("'><b><font class='blue-70'>").append(acctName).append("</font><font class='green'>").append(delimiter);
                            stringBuilder.append(acctNo).append("</font>");
                            if(CommonUtils.isNotEmpty(acctType)){
                                stringBuilder.append(" <font class='red'> , ").append(acctType).append("</font>");
                                if(CommonUtils.isNotEmpty(subType)){
                                    stringBuilder.append("<font class='red-90'> - ").append(subType).append("</font>");
                                }
                            }
                            if(CommonUtils.isEqualIgnoreCase(type,"MASTER")){
                                stringBuilder.append(" (<font class='green'>Master</font>)");
                            }
                            if (importFlag.equals("true") && tradingPartnerDAO.isImportCreadit(acctNo)) {
                                stringBuilder.append(",<font class='red' size='2' style='font-weight: bold;'> $ </font>");
                            }
                            stringBuilder.append("</b><br/>");
                            boolean commaRequired = false;
                            if(CommonUtils.isNotEmpty(address)){
                                stringBuilder.append(address);
                                commaRequired = true;
                            }
                            if(CommonUtils.isNotEmpty(city)){
                                if(commaRequired){
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(city);
                                commaRequired = true;
                            }
                            if(CommonUtils.isNotEmpty(state)){
                                if(commaRequired){
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(state);
                            }
                            if(CommonUtils.isNotEmpty(country)){
                                if(commaRequired){
                                    stringBuilder.append(", ");
                                }
                                commaRequired = true;
                                stringBuilder.append(country);
                            }
                            stringBuilder.append("</li>");
                        }
                    }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
}
