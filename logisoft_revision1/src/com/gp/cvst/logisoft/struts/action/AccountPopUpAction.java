/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.LineItem;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.struts.form.AccountPopUpForm;

/** 
 * MyEclipse Struts
 * Creation date: 03-29-2008
 * 
 * XDoclet definition:
 * @struts.action path="/accountPopUp" name="accountPopUpForm" input="/jsps/Accounting/AccountPopUp.jsp" scope="request" validate="true"
 */
public class AccountPopUpAction extends Action {
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
        AccountPopUpForm accountPopUpForm = (AccountPopUpForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = accountPopUpForm.getButtonValue();
        String account = accountPopUpForm.getAccount();
        String acctDesc = accountPopUpForm.getAcctDesc();
        LineItem line = null;
        String search = "";
        String path1 = "";
        String itemNo = "";
        List lineItemList = new ArrayList();
        AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
        if (session.getAttribute("itemNo") != null) {
            itemNo = (String) session.getAttribute("itemNo");
        }


        if (request.getParameter("index") != null) {
            int ind = Integer.parseInt(request.getParameter("index"));
            List acctList = (List) session.getAttribute("accountList");

            if (session.getAttribute("search1") != null) {
                search = (String) session.getAttribute("search1");
            }


            AccountDetails acct = (AccountDetails) acctList.get(ind);
            if (search.equals("lineItem")) {
                if (itemNo == null || itemNo.equals("")) {

                    if (session.getAttribute("line") != null) {
                        line = (LineItem) session.getAttribute("line");
                    } else {
                        line = new LineItem();
                    }
                    line.setAccount(acct.getAccount());
                    line.setAccountDesc(acct.getAcctDesc());
                    session.setAttribute("line", line);

                } else {
                    if (session.getAttribute("lineItemList") != null) {
                        lineItemList = (List) session.getAttribute("lineItemList");
                    }

                    for (int i = 0; i < lineItemList.size(); i++) {
                        LineItem l1 = (LineItem) lineItemList.get(i);

                        if (l1.getLineItemId().equals(itemNo)) {
                            l1.setAccount(acct.getAccount());

                            l1.setAccountDesc(acct.getAcctDesc());


                        }
                    }
                    session.setAttribute("lineItemList", lineItemList);

                }
                if (search.equals("lineItem")) {
                    path1 = "jsps/Accounting/JournalEntry.jsp";
                }
            } else if (search.equals("chartofAcct")) {

                if (session.getAttribute("line") != null) {
                    line = (LineItem) session.getAttribute("line");
                } else {
                    line = new LineItem();
                }
                line.setAccount(acct.getAccount());
                line.setAccountDesc(acct.getAcctDesc());
                session.setAttribute("line", line);
                path1 = "jsps/Accounting/ChartOfAccounts.jsp";
                //session.setAttribute("StAcct",line);
                session.setAttribute("acct", line.getAccount());

            //session.removeAttribute("EdAcct");
            } else if (search.equals("chartofAcct2")) {

                if (session.getAttribute("line") != null) {
                    line = (LineItem) session.getAttribute("line");
                } else {
                    line = new LineItem();
                }
                line.setAccount(acct.getAccount());
                line.setAccountDesc(acct.getAcctDesc());
                session.setAttribute("line", line);
                path1 = "jsps/Accounting/ChartOfAccounts.jsp";
                //session.setAttribute("EdAcct",line);
                session.setAttribute("acct1", line.getAccount());
            //session.removeAttribute("StAcct");
            }

            request.setAttribute("path1", path1);
            request.setAttribute("buttonValue", "completed");
        } else {
            if (buttonValue.equals("search")) {
                List accountList = accountDetailsDAO.findAccountNo(account, acctDesc);
                session.setAttribute("accountList", accountList);
            }
        }

        return mapping.findForward("accountpopup");
    }
}