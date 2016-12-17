package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.BankDetails;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.BankDetailsDAO;
import com.gp.cvst.logisoft.util.DBUtil;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

public class AutoCompleterForGLAccount {

    public void setGLAccounts(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            String glAccountNo = "";
            String from = request.getParameter("tabName");
            if (textFieldId == null) {
                return;
            }
            if ((CommonUtils.isEqualIgnoreCase(from, "SUBLEDGER")) && (null != request.getParameter("index"))) {
                String index = request.getParameter("index");
                if (null != request.getParameter(textFieldId + index)) {
                    glAccountNo = request.getParameter(textFieldId + index);
                }
            } else {
                glAccountNo = request.getParameter(textFieldId);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isEqualIgnoreCase(from, "CHECK_REGISTER")) {
                BankDetailsDAO bankDetailsDAO = new BankDetailsDAO();
	     glAccountNo = glAccountNo.replaceAll("-", "");
                if (StringUtils.isNumeric(glAccountNo)) {
                    StringBuilder glAccountValue = new StringBuilder(StringUtils.substring(glAccountNo, 0, 2));
                    glAccountValue.append(glAccountNo.length() >= 2 ? "-" : "");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 2, 6));
                    glAccountValue.append(glAccountNo.length() >= 6 ? "-" : "");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 6));
                    glAccountNo = glAccountValue.toString();
                }
                List<BankDetails> bankDetailsList = bankDetailsDAO.findByGLAccountNo("%" + glAccountNo + "%");
                if (CommonUtils.isNotEmpty(bankDetailsList)) {
                    for (BankDetails bankDetails : bankDetailsList) {
                        if (CommonUtils.isNotEmpty(bankDetails.getGlAccountno())) {
                            String delimiter = " <--> ";
                            glAccountNo = bankDetails.getGlAccountno();
                            stringBuilder.append("<li id='").append(bankDetails.getBankAcctNo()).append("'>");
                            stringBuilder.append("<b>");
                            stringBuilder.append("<font class='red-90'>");
                            stringBuilder.append(bankDetails.getGlAccountno());
                            stringBuilder.append("</font><font class='blue-70'>");
                            stringBuilder.append(delimiter);
                            stringBuilder.append(bankDetails.getBankName());
                            stringBuilder.append("</font>");
                            if ((CommonUtils.isNotEmpty(bankDetails.getAcctName())) || (CommonUtils.isNotEmpty(bankDetails.getBankAddress()))) {
                                stringBuilder.append("<br/>");
                                boolean delim = false;
                                if (CommonUtils.isNotEmpty(bankDetails.getAcctName())) {
                                    stringBuilder.append(bankDetails.getAcctName());
                                    delim = true;
                                }
                                if (CommonUtils.isNotEmpty(bankDetails.getBankAddress())) {
                                    if (delim) {
                                        stringBuilder.append(delimiter);
                                    }
                                    stringBuilder.append(bankDetails.getBankAddress());
                                }
                            }
                            stringBuilder.append("</b>");
                            stringBuilder.append("</li>");
                        }
                    }
                }
            } else if (CommonUtils.isEqualIgnoreCase(from, "GLMAPPING")) {
                List<AccountDetails> accountDetailses = new AccountDetailsDAO().findByProperty("account", "%" + glAccountNo + "%");
                for (AccountDetails accountDetails : accountDetailses) {
                    String acct = accountDetails.getAccount();
                    String[] glAccts = StringUtils.split(accountDetails.getAccount(), "-");
                    if (glAccts.length > 1) {
                        for (String glAcct : glAccts) {
                            if (glAcct.length() > 2) {
                                acct = glAcct;
                            }
                        }
                    } else {
                        acct = glAccts[0];
                    }
                    stringBuilder.append("<li id='").append(acct).append("'>");
                    stringBuilder.append("<b><font class='blue-70'>").append(acct).append("</font><font class='red-90'> <--> ").append(accountDetails.getAcctDesc()).append("</font></b></li>");
                }
            } else if (CommonUtils.isEqualIgnoreCase(from, "JournalEntry")) {
                glAccountNo = glAccountNo.replaceAll("-", "");
                if (StringUtils.isNumeric(glAccountNo)) {
                    StringBuilder glAccountValue = new StringBuilder(StringUtils.substring(glAccountNo, 0, 2));
                    glAccountValue.append(glAccountNo.length() >= 2 ? "-" : "");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 2, 6));
                    glAccountValue.append(glAccountNo.length() >= 6 ? "-" : "");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 6));
                    glAccountNo = glAccountValue.toString();
                }

                List<AccountDetails> accountList = new DBUtil().getAccountNumber(glAccountNo);
                for (AccountDetails accountDetails : accountList) {
                    stringBuilder.append("<li id='").append(accountDetails.getAcctDesc()).append("'>");
                    stringBuilder.append("<b><font class='red-90'>").append(accountDetails.getAccount()).append("</font><font class='blue-70'> <--> ");
                    stringBuilder.append(accountDetails.getAcctDesc()).append("</font></b></li>");
                }
            } else if (CommonUtils.isEqualIgnoreCase(from, "GL_CODE_REPORT")) {
                glAccountNo = glAccountNo.replaceAll("-", "");
                if (StringUtils.isNumeric(glAccountNo)) {
                    StringBuilder glAccountValue = new StringBuilder(StringUtils.substring(glAccountNo, 0, 2));
                    glAccountValue.append(glAccountNo.length() >= 2 ? "-" : "");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 2, 6));
                    glAccountValue.append(glAccountNo.length() >= 6 ? "-" : "");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 6));
                    glAccountNo = glAccountValue.toString();
                }

                List<AccountDetails> accountList = new DBUtil().getAccountNumber(glAccountNo);
                glAccountNo = glAccountNo.replaceAll("-", "");
                if (glAccountNo.length() >= 6) {
                    StringBuilder glAccountValue = new StringBuilder();
                    glAccountValue.append(StringUtils.substring(glAccountNo, 0, 2));
                    glAccountValue.append("-");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 2, 6));
                    glAccountValue.append("-");
                    glAccountValue.append("all");
                    stringBuilder.append("<li id='").append(glAccountValue.toString()).append("'>");
                    stringBuilder.append("<b>").append(glAccountValue.toString()).append(" <--> ");
                    stringBuilder.append("<font class='red-90'>All Suffixes</font></b></li>");
                }
                for (AccountDetails accountDetails : accountList) {
                    stringBuilder.append("<li id='").append(accountDetails.getAccount()).append("'>");
                    stringBuilder.append("<b><font class='red-90'>").append(accountDetails.getAccount());
                    stringBuilder.append("</font><font class='blue-70'> <--> ").append(accountDetails.getAcctDesc()).append("</font></b></li>");
                }
            } else {
                glAccountNo = glAccountNo.replaceAll("-", "");
                if (StringUtils.isNumeric(glAccountNo)) {
                    StringBuilder glAccountValue = new StringBuilder(StringUtils.substring(glAccountNo, 0, 2));
                    glAccountValue.append(glAccountNo.length() >= 2 ? "-" : "");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 2, 6));
                    glAccountValue.append(glAccountNo.length() >= 6 ? "-" : "");
                    glAccountValue.append(StringUtils.substring(glAccountNo, 6));
                    glAccountNo = glAccountValue.toString();
                }

                List<AccountDetails> accountList = new DBUtil().getAccountNumber(glAccountNo);
                for (AccountDetails accountDetails : accountList) {
                    stringBuilder.append("<li id='").append(accountDetails.getAccount()).append("'>");
                    stringBuilder.append("<b><font class='blue-70'>").append(accountDetails.getAccount());
                    stringBuilder.append("</font><font class='red-90'> <--> ").append(accountDetails.getAcctDesc()).append("</font></b></li>");
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
}