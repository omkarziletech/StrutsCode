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

public class AutoCompleterForVendor {

    public void setVendorDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            if (null != tabName && tabName.trim().equals("APPAYMENT")) {
                HttpSession session = request.getSession();
                User loginuser = (User) session.getAttribute("loginuser");
                TransactionDAO transactionDAO = new TransactionDAO();
                List<CustAddress> customerList = new ArrayList<CustAddress>();
                if (loginuser.getRole().getRoleDesc().equalsIgnoreCase(CommonConstants.ROLE_NAME_APSPECIALIST)) {
                    customerList = transactionDAO.getCustomersWithStatusPayByUserId(vendor, loginuser);
                }
                if (loginuser.getRole().getRoleDesc().equalsIgnoreCase(CommonConstants.ROLE_NAME_SUPERVISOR)) {
                    customerList = transactionDAO.getCustomersWithStatusPay(vendor);
                }
                if (loginuser.getRole().getRoleDesc().equals(CommonConstants.ROLE_NAME_ADMIN)) {
                    customerList = transactionDAO.getCustomersWithStatusPay(vendor);
                }
                if (null != customerList && !customerList.isEmpty()) {
                    String delimiter = " <--> ";
                    for (CustAddress custAddress : customerList) {
                        String city = custAddress.getCity1();
                        String state = custAddress.getState();
                        String type = custAddress.getAcctType();
                        String addressLine = custAddress.getAddress1();
                        if (null != city && !city.trim().equals("")
                                && null != state && !state.trim().equals("")
                                && null != type && !type.trim().equals("")
                                && null != addressLine && !addressLine.trim().equals("")) {
                            stringBuilder.append("<li id='").append(custAddress.getAcctNo()).append("'><b><font class='blue-70'>").append(custAddress.getAcctName()).append("</font><font class='green'>").append(delimiter).append(custAddress.getAcctNo()).append(" </font><font class='red'>").append(custAddress.getAcctType()).append("</font></b><br/>").append(custAddress.getAddress1()).append(",  ").append(custAddress.getCity1()).append(",  ").append(custAddress.getState()).append(";   "
                                    + "</li>");
                        } else if (null != city && !city.trim().equals("")
                                && null != state && !state.trim().equals("")
                                && null != type && !type.trim().equals("")) {
                            stringBuilder.append("<li id='").append(custAddress.getAcctNo()).append("'><b><font class='blue-70'>").append(custAddress.getAcctName()).append("</font><font class='green'>").append(delimiter).append(custAddress.getAcctNo()).append(" </font><font class='red'>").append(custAddress.getAcctType()).append("</font></b><br/>").append(custAddress.getCity1()).append(",  ").append(custAddress.getState()).append(";   "
                                    + "</li>");
                        } else if (null != city && !city.trim().equals("")
                                && null != state && !state.trim().equals("")
                                && null != addressLine && !addressLine.trim().equals("")) {
                            stringBuilder.append("<li id='").append(custAddress.getAcctNo()).append("'><b><font class='blue-70'>").append(custAddress.getAcctName()).append("</font><font class='green'>").append(delimiter).append(custAddress.getAcctNo()).append(" </font> </b><br/>").append(custAddress.getAddress1()).append(",  ").append(custAddress.getCity1()).append(",  ").append(custAddress.getState()).append("</li>");
                        } else if (null != city && !city.trim().equals("")
                                && null != addressLine && !addressLine.trim().equals("")) {
                            stringBuilder.append("<li id='").append(custAddress.getAcctNo()).append("'><b><font class='blue-70'>").append(custAddress.getAcctName()).append("</font><font class='green'>").append(delimiter).append(custAddress.getAcctNo()).append(" </font></b><br/>").append(custAddress.getAddress1()).append(",  ").append(custAddress.getCity1()).append("</li>");
                        } else if (null != city && !city.trim().equals("")
                                && null != state && !state.trim().equals("")) {
                            stringBuilder.append("<li id='").append(custAddress.getAcctNo()).append("'><b><font class='blue-70'>").append(custAddress.getAcctName()).append("</font><font class='green'>").append(delimiter).append(custAddress.getAcctNo()).append(" </font></b><br/>").append(custAddress.getCity1()).append(",  ").append(custAddress.getState()).append("</li>");
                        } else if (null != city && !city.trim().equals("")) {
                            stringBuilder.append("<li id='").append(custAddress.getAcctNo()).append("'><b><font class='blue-70'>").append(custAddress.getAcctName()).append("</font><font class='green'>").append(delimiter).append(custAddress.getAcctNo()).append(" </font></b><br/>").append(custAddress.getCity1()).append("</li>");
                        } else {
                            stringBuilder.append("<li id='").append(custAddress.getAcctNo()).append("'><b><font class='blue-70'>").append(custAddress.getAcctName()).append("</font><font class='green'>").append(delimiter).append(custAddress.getAcctNo()).append(" </font></b><br/>");
                        }
                    }
                }
            } else {
                boolean openInvoice = CommonUtils.isNotEmpty(request.getParameter("openInvoice")) ? Boolean.valueOf(request.getParameter("openInvoice")) : false;
                boolean disabled = CommonUtils.isNotEmpty(request.getParameter("disabled")) ? Boolean.valueOf(request.getParameter("disabled")) : false;
                List tradingPartners = new TradingPartnerDAO().getTradingPartners(vendor, disabled, openInvoice);
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
                            if (textFieldId.contains("forwardAccount")) {
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
        }
        stringBuilder.append("</ul>");
        out.println(stringBuilder.toString());
    }
}
