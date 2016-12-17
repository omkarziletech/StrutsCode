package com.gp.cvst.logisoft.struts.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.beans.accthistorybean;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.JournalEntry;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.LineItemDAO;
import com.gp.cvst.logisoft.struts.form.AcctHistoryForm;
import com.itextpdf.text.ExceptionConverter;

/** 
 * MyEclipse Struts
 * Creation date: 03-08-2008
 * 
 * XDoclet definition:
 * @struts.action input="/jsps/Accounting/AccountHistory.jsp" validate="true"
 */
public class AcctHistoryAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response)throws Exception {
        // TODO Auto-generated method stub
        HttpSession session = request.getSession();
        AcctHistoryForm accform = (AcctHistoryForm) form;
        AccountBalanceDAO abdao = new AccountBalanceDAO();
        String account = accform.getAccount();
        String desc = accform.getDesc();
        String year = accform.getYear();
        List accountdetail = null;
        String buttonValue = accform.getButtonValue();
        String forwardName = "";
        BatchDAO batchDao = new BatchDAO();
        Batch batch = new Batch();
        if (request.getParameter("reference") != null && !request.getParameter("reference").equals("")) {
            String jEId = request.getParameter("reference").trim();
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            String endAcct = request.getParameter("endingAcct");

            String batchId = "";
            String journalEntryNo = "";
            String getBatchId[] = StringUtils.splitPreserveAllTokens(jEId, '-');
            if (getBatchId != null && getBatchId.length > 1) {
                batchId = getBatchId[0];
                journalEntryNo = getBatchId[1];
            } else {
                batchId = getBatchId[0];
            }

            batch = batchDao.findById(batchId);

            Set journalEntySet = batch.getJournalEntrySet();
            Iterator iterator = journalEntySet.iterator();
            while (iterator.hasNext()) {
                JournalEntry journalEntry = (JournalEntry) iterator.next();

                if (journalEntry.getJournalEntryId().equals(jEId)) {

                    session.setAttribute("journalEntry", journalEntry);
                    session.setAttribute("journal", "journalEntry");
                    break;
                }
            }

            session.setAttribute("batch", batch);
            buttonValue = "detail";
            session.setAttribute("buttonValue", buttonValue);
            session.setAttribute("trade", "journalentry");
            List transHistoryList = new ArrayList();
            transHistoryList.add(dateFrom);
            transHistoryList.add(dateTo);
            transHistoryList.add(endAcct);
            session.setAttribute("dateFrom", transHistoryList);

            forwardName = "toJEPage";
        }
        if (request.getParameter("fiscalCompare") != null && !request.getParameter("fiscalCompare").equals("")) {
            int index = Integer.parseInt(request.getParameter("fiscalCompare"));
            List aclist = (List) session.getAttribute("aclist");
            String acct = "";
            if (aclist != null && aclist.size() > 0) {
                acct = (String) aclist.get(0);
            }
            FiscalPeriodDAO fpdao = new FiscalPeriodDAO();
            NumberFormat number = new DecimalFormat("00");
            String period = number.format(index);
            Integer fisalYear = Integer.parseInt(request.getParameter("year"));
            FiscalPeriod currentFiscalPeriod = fpdao.findYear(fisalYear, period);
            session.removeAttribute("AHitemdetails");
            session.removeAttribute("itemdetails");
            List itemdetails = null;
            LineItemDAO lineItemDAO = new LineItemDAO();
            itemdetails = lineItemDAO.getTransactions(acct, null, currentFiscalPeriod, currentFiscalPeriod, "no");
            session.setAttribute("itemdetails", itemdetails);
            session.setAttribute("navigateTransHistory", "navigateTransHistory");
            forwardName = "transHistory";
        }

        if (request.getParameter("index") != null && !request.getParameter("index").equals("")) {
            try {
                int ind = Integer.parseInt(request.getParameter("index"));
                List acctList = (List) session.getAttribute("ad");
                List Abc = (List) session.getAttribute("aclist");
                accthistorybean ahb = (accthistorybean) acctList.get(ind);
                String period = ahb.getPeriod();
                String dateto = ahb.getEnddate();
                StringTokenizer st = new StringTokenizer(dateto, "/");
                if (st.hasMoreTokens()) {
                    String mon = st.nextToken();
                    String d = st.nextToken();
                    year = st.nextToken();
                }
                FiscalPeriodDAO fpDAO = new FiscalPeriodDAO();
                String datefrom = fpDAO.getStartDate(year, period);
                request.setAttribute("periodFrom", year + period);
                request.setAttribute("periodTo", year + period);

                request.setAttribute("startDate", datefrom);
                request.setAttribute("endDate", dateto);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                session.removeAttribute("AHitemdetails");
                session.removeAttribute("itemdetails");
                session.setAttribute("navigateTransHistory", "navigateTransHistory");
                if (datefrom != null & dateto != null) {
                    LineItemDAO ldao = new LineItemDAO();
                    List itemdetails = null;
                    account = (String) Abc.get(0);

                    FiscalPeriod fiscalPeriodObjectOne = fpDAO.getFiscalPeriodForDate(simpleDateFormat.parse(datefrom));
                    FiscalPeriod fiscalPeriodObjectTwo = fpDAO.getFiscalPeriodForDate(simpleDateFormat.parse(dateto));
                    if (fiscalPeriodObjectOne != null || fiscalPeriodObjectTwo != null) {
                        itemdetails = ldao.getTransactions(account, null, fiscalPeriodObjectOne, fiscalPeriodObjectTwo);
                    }
                    session.setAttribute("AHitemdetails", itemdetails);
                    session.setAttribute("itemdetails", itemdetails);
                }
                forwardName = "transHistory";

                List fiscalPeriodList = fpDAO.getAllFiscalPeriods();
                request.setAttribute("fiscalPeriods", fiscalPeriodList);
                request.setAttribute("currentFiscalPeriod", fpDAO.getPeriodForCurrentDate());


            } catch (NumberFormatException e) {
                if (buttonValue.equals("go")) {
                    accountdetail = abdao.findforSearch(year, account);
                    request.setAttribute("accountdetail", accountdetail);
                    request.setAttribute("account", account);
                    request.setAttribute("desc", desc);
                    forwardName = "success";
                }
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        } else {
            if (buttonValue != null && buttonValue.equals("go")) {
                int y = Integer.parseInt(year);
                // y=y-1;

                accountdetail = abdao.findforSearch(String.valueOf(y), account);
                request.setAttribute("accountdetail", accountdetail);
                request.setAttribute("account", account);
                request.setAttribute("desc", desc);
                forwardName = "success";
            } else if (buttonValue != null && buttonValue.equals("loadAccountHistory")) {
                //This was added by Vasan to load the accounts by passing the accountNo.
                AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
                String accountNo = request.getParameter("accountNo");
                List accountDetailsList = accountDetailsDAO.findAccoutNo(accountNo);
                if (!accountDetailsList.isEmpty()) {
                    AccountDetails accountDetails = (AccountDetails) accountDetailsList.get(0);
                    List aclist = new ArrayList();
                    aclist.add(accountDetails.getAccount());
                    aclist.add(accountDetails.getAcctDesc());
                    session.setAttribute("aclist", aclist);
                }
                forwardName = "accountHistory";
            }
        }
        return mapping.findForward(forwardName);
    }
}