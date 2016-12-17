/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.logisoft.bc.accounting.ReportConstants;
import com.gp.cong.logisoft.bc.accounting.TransactionHistoryBC;
import com.gp.cong.logisoft.reports.dto.TransactionHistoryReportDTO;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.beans.ChartOfAccountBean;
import com.gp.cvst.logisoft.domain.AccountDetails;
import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.domain.FiscalYear;
import com.gp.cvst.logisoft.hibernate.dao.AccountBalanceDAO;
import com.gp.cvst.logisoft.hibernate.dao.AccountDetailsDAO;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;
import com.gp.cvst.logisoft.hibernate.dao.LineItemDAO;
import com.gp.cvst.logisoft.struts.form.TransactionHistoryForm;
import com.logiware.accounting.dao.ChartOfAccountsDAO;
import com.logiware.accounting.excel.TransactionHistoryExcelCreator;
import com.logiware.accounting.model.AccountModel;
import com.oreilly.servlet.ServletUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 03-17-2008
 *
 * XDoclet definition:
 *
 * @struts.action input="/jsps/Accounting/TransactionHistory.jsp"
 * validate="true"
 */
public class TransactionHistoryAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub

        HttpSession session = request.getSession();
        TransactionHistoryForm transactionHistoryForm = (TransactionHistoryForm) form;
        String account = transactionHistoryForm.getAccount();
        String account1 = transactionHistoryForm.getAccount1();
        String desc = transactionHistoryForm.getDesc();
        String datefrom = transactionHistoryForm.getDatefrom();
        String dateto = transactionHistoryForm.getDateto();
        String sourcecode = transactionHistoryForm.getSourcecode();
        String buttonValue = transactionHistoryForm.getButtonValue();
        LineItemDAO ldao = new LineItemDAO();
        List<ChartOfAccountBean> itemdetails = null;
        String forwardName = "";
        FiscalPeriodDAO fiscalPeriodDAO = new FiscalPeriodDAO();
        if (buttonValue.equals("go")) {
            FiscalPeriod periodFrom = null;
            if (fiscalPeriodDAO.findByPeriodDis(datefrom) != null && !fiscalPeriodDAO.findByPeriodDis(datefrom).isEmpty()) {
                periodFrom = (FiscalPeriod) fiscalPeriodDAO.findByPeriodDis(datefrom).get(0);
            }
            FiscalPeriod periodTo = null;
            if (fiscalPeriodDAO.findByPeriodDis(dateto) != null && !fiscalPeriodDAO.findByPeriodDis(dateto).isEmpty()) {
                periodTo = (FiscalPeriod) fiscalPeriodDAO.findByPeriodDis(dateto).get(0);
            }
            if (periodTo == null) {
                periodTo = periodFrom;
            }
            searchTransactions(account, account1, periodFrom.getPeriodDis(), periodTo.getPeriodDis(), request);
            List<FiscalPeriod> fiscalPeriodList = new ArrayList<FiscalPeriod>();
            //fiscalPeriodList.add((FiscalPeriod)fiscalPeriodDAO.findByPeriodDis(datefrom).get(0));
            List fPeriodList = fiscalPeriodDAO.getAllFiscalPeriods();
            fiscalPeriodList.addAll(fPeriodList);
            request.setAttribute("fiscalPeriods", fiscalPeriodList);
            request.setAttribute("account", account);
            request.setAttribute("account1", account1);
            /*FiscalPeriod toFiscalPeriod = (FiscalPeriod)fiscalPeriodDAO.findByPeriodDis(dateto).get(0);
             FiscalPeriod fromFiscalPeriod = (FiscalPeriod)fiscalPeriodDAO.findByPeriodDis(datefrom).get(0);
             */
            request.setAttribute("toFiscalPeriod", periodTo);
            request.setAttribute("fromFiscalPeriod", periodFrom);
            forwardName = "success";
        } else if (buttonValue.equals("createExcel")) {
            String fileName = new TransactionHistoryExcelCreator(transactionHistoryForm).create();
            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
            ServletUtils.returnFile(fileName, response.getOutputStream());
            return null;
        } else if (buttonValue.equals("AHgo")) {
            itemdetails = ldao.getTransactions(account, account1, (FiscalPeriod) fiscalPeriodDAO.findByPeriodDis(datefrom).get(0), (FiscalPeriod) fiscalPeriodDAO.findByPeriodDis(dateto).get(0), sourcecode);
            session.setAttribute("AHitemdetails", itemdetails);
            forwardName = "AHsuccess";
        } else if (buttonValue.equals("showall")) {
            itemdetails = ldao.getAllTransactions(account, account1, null, null);
            session.setAttribute("itemdetails", itemdetails);
            forwardName = "success";
        } else if (buttonValue.equals("loadTransactionForAccount")) {
            String accountNo = request.getParameter("accountNo");
            String accountNo1 = "";
            FiscalPeriod currentFiscalPeriod = fiscalPeriodDAO.getPeriodForCurrentDate();
            List transactionList = ldao.getTransactions(accountNo, accountNo1, currentFiscalPeriod, currentFiscalPeriod);
            session.removeAttribute("itemdetails");
            session.setAttribute("itemdetails", transactionList);
            AccountDetailsDAO accountDetailsDAO = new AccountDetailsDAO();
            List accountDetailsList = accountDetailsDAO.findAccoutNo(accountNo);
            if (!accountDetailsList.isEmpty()) {
                AccountDetails accountDetails = (AccountDetails) accountDetailsList.get(0);
                List aclist = new ArrayList();
                aclist.add(accountDetails.getAccount());
                aclist.add(accountDetails.getAcctDesc());
                session.setAttribute("aclist", aclist);
            }
            forwardName = "success";
        } else if (buttonValue.equals("print")) {
            TransactionHistoryBC transactionHistoryBC = new TransactionHistoryBC();
            LoadLogisoftProperties loadLogisoftProperties = new LoadLogisoftProperties();
            String outputFileName = loadLogisoftProperties.getProperty("reportLocation");
            File file = new File(outputFileName + "/" + ReportConstants.TRANSACTIONHISTORY);
            if (!file.exists()) {
                file.mkdir();
            }
            outputFileName = outputFileName + "/" + ReportConstants.TRANSACTIONHISTORY + "/" + account + "_" + account1 + ".pdf";

            String realPath = this.getServlet().getServletContext().getRealPath("/");
            if (session.getAttribute("itemdetails") != null) {
                itemdetails = (List) session.getAttribute("itemdetails");
            }
            //we are printing the current instance of the screen, So we are creating the object here.
            TransactionHistoryReportDTO transactionHistoryDTO = new TransactionHistoryReportDTO(account, account1, desc, sourcecode, datefrom, dateto, itemdetails);
            transactionHistoryBC.createTransactionHistoryReport(outputFileName, realPath, transactionHistoryDTO);
            request.setAttribute("fileName", outputFileName);
            forwardName = "success";
        } else if (buttonValue.equals("goBack")) {
            forwardName = "goBack";
        }

        List fiscalPeriodList = fiscalPeriodDAO.getAllFiscalPeriods();
        request.setAttribute("fiscalPeriods", fiscalPeriodList);
        request.setAttribute("currentFiscalPeriod", fiscalPeriodDAO.getPeriodForCurrentDate());

        return mapping.findForward(forwardName);
    }

    public void searchTransactions(String startAccount, String endAccount, String startPeriod, String endPeriod, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        com.logiware.accounting.dao.FiscalPeriodDAO fiscalPeriodDAO = new com.logiware.accounting.dao.FiscalPeriodDAO();
        com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO fisPeriodDAO = new com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO();
        AccountBalanceDAO accountBalanceDAO = new AccountBalanceDAO();
        ChartOfAccountsDAO chartOfAccountsDAO = new ChartOfAccountsDAO();
        LineItemDAO ldao = new LineItemDAO();
        List<ChartOfAccountBean> itemdetails = null;
        FiscalYear fiscalYear = fiscalPeriodDAO.getLastClosedYear(startPeriod);
        Integer sPeriod = Integer.parseInt(startPeriod);
        Integer ePeriod = Integer.parseInt(fiscalYear.getEndPeriod());
        List<String> periods = fiscalPeriodDAO.getPeriods(startPeriod, endPeriod, ePeriod <= sPeriod);
        FiscalPeriod previousPeriod = fiscalPeriodDAO.getPreviousPeriod(startPeriod);
        Map<String, Double> closingBalances = accountBalanceDAO.getClosingBalances(startAccount, endAccount, previousPeriod.getPeriodDis());
        request.setAttribute("closingBalances", closingBalances);
        String whereClause = chartOfAccountsDAO.buildTransactionsWhere(startAccount, endAccount, periods);
        List<AccountModel> transactions = chartOfAccountsDAO.getTransactions(whereClause);
        request.setAttribute("transactions", transactions);
        itemdetails = ldao.getTransactions(startAccount, endAccount, (FiscalPeriod) fisPeriodDAO.findByPeriodDis(startPeriod).get(0), (FiscalPeriod) fisPeriodDAO.findByPeriodDis(endPeriod).get(0), null);
        session.removeAttribute("itemdetails");
        session.setAttribute("itemdetails", itemdetails);
    }
}
