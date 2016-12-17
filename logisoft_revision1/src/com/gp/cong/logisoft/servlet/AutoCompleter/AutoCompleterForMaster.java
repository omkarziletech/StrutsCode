/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.logisoft.servlet.AutoCompleter;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author priyanka.rachote
 */
public class AutoCompleterForMaster {

 public void setMasterDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String textFieldId = request.getParameter("textFieldId");
        if (textFieldId == null) {
            return;
        }
        String vendor = request.getParameter(textFieldId);
        String tabName = request.getParameter("tabName");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>");
        if (null != vendor && !vendor.trim().equals("")) {
             
                boolean openInvoice = CommonUtils.isNotEmpty(request.getParameter("openInvoice")) ? Boolean.valueOf(request.getParameter("openInvoice")) : false;
                boolean disabled = CommonUtils.isNotEmpty(request.getParameter("disabled")) ? Boolean.valueOf(request.getParameter("disabled")) : false;
                List tradingPartners = new TradingPartnerDAO().getTradingPartnersForMaster(vendor, disabled, openInvoice);
                String excludeAccountNo = request.getParameter("excludeAccountNo");
                if (CommonUtils.isNotEmpty(tradingPartners)) {
                    String delimiter = " <--> ";
                    for (Object row : tradingPartners) {
                        Object[] col = (Object[]) row;
                        String acctName = (String) col[0];
                        String acctNo = (String) col[1];
                        if (CommonUtils.isNotEqualIgnoreCase(excludeAccountNo, acctNo)) {
                            String acctType = (String) col[2];
                            String subType = (String) col[3];
                            String type = (String) col[4];
                            String address = (String) col[5];
                            String city = (String) col[6];
                            String state = (String) col[7];
                            String country = (String) col[8];
                            if (textFieldId.contains("master")) {
                                stringBuilder.append("<li id='").append(acctName).append("'><b><font class='red-90'>").append(acctNo).append("</font>");
                                stringBuilder.append(" <font class='blue-70'>").append(delimiter).append(acctName).append("</font>");
                            } else {
                                stringBuilder.append("<li id='").append(acctNo).append("'><b><font class='blue-70'>").append(acctName).append("</font>");
                                stringBuilder.append(" <font class='red-90'>").append(delimiter).append(acctNo).append("</font>");
                            }
                            if (CommonUtils.isNotEmpty(acctType)) {
                                stringBuilder.append(" <font class='red'>").append(acctType);
                                if (CommonUtils.isNotEmpty(subType)) {
                                    stringBuilder.append(" - ").append(subType);
                                }
                                stringBuilder.append("</font>");
                            }
                            if (CommonUtils.isEqualIgnoreCase(type, "MASTER")) {
                                stringBuilder.append(" (<font class='green'>Master</font>)");
                            }
                            stringBuilder.append("</b><br/>");
                            boolean commaRequired = false;
                            if (CommonUtils.isNotEmpty(address)) {
                                stringBuilder.append(address);
                                commaRequired = true;
                            }
                            if (CommonUtils.isNotEmpty(city)) {
                                if (commaRequired) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(city);
                                commaRequired = true;
                            }
                            if (CommonUtils.isNotEmpty(state)) {
                                if (commaRequired) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(state);
                            }
                            if (CommonUtils.isNotEmpty(country)) {
                                if (commaRequired) {
                                    stringBuilder.append(", ");
                                }
                                commaRequired = true;
                                stringBuilder.append(country);
                            }
                            stringBuilder.append("</li>");
                        }
                    }
                }
            
        }
        stringBuilder.append("</ul>");
        out.println(stringBuilder.toString());
    }
}
