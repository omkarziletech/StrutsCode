package com.gp.cong.logisoft.servlet.AutoCompleter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.accounting.APPaymentBC;
import com.gp.cong.logisoft.domain.User;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.util.DBUtil;
import org.apache.commons.lang3.StringUtils;

public class AutoCompleterForBank {

    public void setBankDetails(HttpServletRequest request, HttpServletResponse response)throws Exception {
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            String bankAccount = "";
            String tabName = request.getParameter("tabName");
            if (textFieldId == null) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            List<BankDetails> bankDetailsList = new ArrayList<BankDetails>();
            if (CommonUtils.isEqualIgnoreCase(tabName, "BANKACCOUNT")) {
                String bankName = request.getParameter(textFieldId);
                if (CommonUtils.isNotEmpty(bankName)) {
                    HttpSession session = request.getSession();
                    User loginUser = (User) session.getAttribute("loginuser");
                    if (null != loginUser && null != loginUser.getRole() && null != loginUser.getRole().getRoleDesc()
                            && (loginUser.getRole().getRoleDesc().trim().equalsIgnoreCase(CommonConstants.ROLE_NAME_SUPERVISOR)
                            || loginUser.getRole().getRoleDesc().trim().equalsIgnoreCase(CommonConstants.ROLE_NAME_ADMIN))) {
                        APPaymentBC aPPaymentBC = new APPaymentBC();
                        bankDetailsList = aPPaymentBC.getBankNameList(loginUser, bankName);
                    } else {
                        DBUtil dbUtil = new DBUtil();
                        bankDetailsList = dbUtil.getBankAccountList(loginUser.getUserId(), "bankAccount");
                    }
                    if (CommonUtils.isNotEmpty(bankDetailsList)) {
                        for (BankDetails bankDetails : bankDetailsList) {
                            if (null != bankDetails.getBankAcctNo() && !bankDetails.getBankAcctNo().trim().equals("")
                                    && null != bankDetails.getBankAddress() && !bankDetails.getBankAddress().trim().equals("")) {
                                stringBuilder.append("<li id='").append(bankDetails.getBankAcctNo()).append("'><b><font class='blue-70'>").append(bankDetails.getBankName()).append("</font><font class='red-90'> <--> ").append(bankDetails.getBankAcctNo()).append("</font></b><br/>").append(bankDetails.getBankAddress()).append("</li>");
                            } else if (null != bankDetails.getBankAcctNo() && !bankDetails.getBankAcctNo().trim().equals("")) {
                                stringBuilder.append("<li id='").append(bankDetails.getBankAcctNo()).append("'><b><font class='blue-70'>").append(bankDetails.getBankName()).append("</font><font class='red-90'> <--> ").append(bankDetails.getBankAcctNo()).append("</font></b><br/></li>");
                            }
                        }
                    }
                }
            } else if (CommonUtils.isEqual(tabName, "Reconcile")) {
                String glAccountNo = request.getParameter(textFieldId);
                glAccountNo = glAccountNo.replaceAll("-", "");
                if (StringUtils.isNumeric(glAccountNo)) {
                    StringBuilder glAccountValue = new StringBuilder(StringUtils.substring(glAccountNo,0, 2));
		    glAccountValue.append(glAccountNo.length() >= 2 ? "-" : "");
		    glAccountValue.append(StringUtils.substring(glAccountNo,2,6));
		    glAccountValue.append(glAccountNo.length() >= 6 ? "-" : "");
		    glAccountValue.append(StringUtils.substring(glAccountNo,6));
		    glAccountNo = glAccountValue.toString();
                }
                BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
                bankDetailsList = bankDetailsDAO.findByGLAccountNo(glAccountNo + "%");
                if (CommonUtils.isNotEmpty(bankDetailsList)) {
                    for (BankDetails bankDetails : bankDetailsList) {
                        if (CommonUtils.isNotEmpty(bankDetails.getBankAddress())) {
                            stringBuilder.append("<li id='").append(bankDetails.getBankAcctNo()).append("'><b><font class='blue-70'>").append(bankDetails.getGlAccountno()).append("</font><font class='green'> <--> ").append(bankDetails.getBankName()).append("</font><font class='red-90'> <==> ").append(bankDetails.getBankAcctNo()).append("</font></b><br/>").append(bankDetails.getBankAddress()).append("</li>");
                        } else {
                            stringBuilder.append("<li id='").append(bankDetails.getBankAcctNo()).append("'><b><font class='blue-70'>").append(bankDetails.getGlAccountno()).append("</font><font class='green'> <--> ").append(bankDetails.getBankName()).append("</font><font class='red-90'> <==> ").append(bankDetails.getBankAcctNo()).append("</font></b><br/></li>");
                        }
                    }
                }
            } else {
                bankAccount = request.getParameter(textFieldId);
                BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
                if (CommonUtils.isNotEmpty(bankAccount)) {
                    bankDetailsList = bankDetailsDAO.findByBankAcctNo(bankAccount + "%");
                    if (CommonUtils.isNotEmpty(bankDetailsList)) {
                        for (BankDetails bankDetails : bankDetailsList) {
                            if (CommonUtils.isNotEmpty(bankDetails.getGlAccountno())
                                    && CommonUtils.isNotEmpty(bankDetails.getBankAddress())) {
                                stringBuilder.append("<li id='").append(bankDetails.getGlAccountno()).append("'><b><font class='red-90'>").append(bankDetails.getBankAcctNo()).append("</font><font class='blue-70'> <--> ").append(bankDetails.getBankName()).append("</font></b><br/>").append(bankDetails.getBankAddress()).append("</li>");
                            } else if (CommonUtils.isNotEmpty(bankDetails.getGlAccountno())) {
                                stringBuilder.append("<li id='").append(bankDetails.getGlAccountno()).append("'><b><font class='red-90'>").append(bankDetails.getBankAcctNo()).append("</font><font class='blue-70'> <--> ").append(bankDetails.getBankName()).append("</font></b><br/></li>");
                            }
                        }
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
}
