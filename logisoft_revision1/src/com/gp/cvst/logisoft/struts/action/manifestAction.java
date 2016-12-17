/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.CustomerAddress;
import com.gp.cvst.logisoft.domain.Transaction;
import com.gp.cvst.logisoft.hibernate.dao.ArAgeingDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.struts.form.manifestForm;
import org.apache.commons.lang3.StringUtils;

/** 
 * MyEclipse Struts
 * Creation date: 06-07-2008
 * 
 * XDoclet definition:
 * @struts.action path="/manifest" name="manifestform" input="/jsps/Accounting/Manifest.jsp" scope="request"
 */
public class manifestAction extends Action {
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
            HttpServletRequest request, HttpServletResponse response)  throws Exception{
        manifestForm manifestform = (manifestForm) form;// TODO Auto-generated method stub
        String forwardName = "";
        String buttonValue = manifestform.getButtonValue();
        String blno = manifestform.getBlno();
        String invnumber = manifestform.getInvno();
        String glAccountNumber = manifestform.getGlAccountNumber();
        String subledgerSourceCode = manifestform.getSubledgerSourceCode();
        String blTerms = manifestform.getBlTerms();
        String vesselNo = manifestform.getVesselNo();
        String custNo = manifestform.getCustNo();
        String chargeCode = manifestform.getChargeCode();
        String currencyCode = manifestform.getCurrencyCode();
        String journalEntryNumber = manifestform.getJournalEntryNumber();
        String subHouseBl = manifestform.getSubHouseBl();
        String containerNo = manifestform.getContainerNo();
        String transactionDate = manifestform.getTransactionDate();
        String chequeNumber = manifestform.getChequeNumber();
        String lineItemNumber = manifestform.getLineItemNumber();
        String orgTerminal = manifestform.getOrgTerminal();
        String destination = manifestform.getDestination();
        String voyageNo = manifestform.getVoyageNo();
        String masterbalance = manifestform.getM();
        String transactionType = manifestform.getTranType();
        String status = manifestform.getStatus();
        String custReference = manifestform.getCustReference();
        Double transactionAmt = manifestform.getTransactionAmt();
        String custName = manifestform.getVendor();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        if (buttonValue.equals("save")) {
            Transaction transcactiondomain = new Transaction();
            transcactiondomain.setChargeCode(chargeCode);
            transcactiondomain.setGlAccountNumber(glAccountNumber);
            transcactiondomain.setSubledgerSourceCode(subledgerSourceCode);
            transcactiondomain.setBlTerms(blTerms);
            transcactiondomain.setVoyageNo(voyageNo);
            transcactiondomain.setCustNo(custNo);
            transcactiondomain.setCurrencyCode(currencyCode);
            transcactiondomain.setJournalEntryNumber(journalEntryNumber);
            transcactiondomain.setSubHouseBl(subHouseBl);
            transcactiondomain.setContainerNo(containerNo);
            transcactiondomain.setOrgTerminal(orgTerminal);
            transcactiondomain.setDestination(destination);
            transcactiondomain.setTransactionAmt(transactionAmt);
            if (chequeNumber != null && !chequeNumber.trim().equals("")) {
                transcactiondomain.setChequeNumber(chequeNumber);
            }
            transcactiondomain.setLineItemNumber(lineItemNumber);
            transcactiondomain.setVesselNo(vesselNo);
            transcactiondomain.setMasterBl(masterbalance);
            transcactiondomain.setCustomerReferenceNo(StringUtils.left(custReference, 500));
            transcactiondomain.setBillLaddingNo(blno);
            transcactiondomain.setInvoiceNumber(invnumber);
            transcactiondomain.setCustName(custName);
            transcactiondomain.setBalance(transactionAmt);
            transcactiondomain.setTransactionType(transactionType);
            transcactiondomain.setStatus(status);
                date = (Date) format.parse(transactionDate);
            transcactiondomain.setTransactionDate(date);
            TransactionDAO transactionDAO = new TransactionDAO();
            transactionDAO.save(transcactiondomain);
            manifestform.reset(mapping, request);
            forwardName = "success";
        }
        if (buttonValue.equals("getCustomer")) {
            setBasicParams(custName, request);
            forwardName = "success";
        }
        return mapping.findForward(forwardName);
    }

    private void setBasicParams(String customerName, HttpServletRequest request) throws Exception {
        CustAddressDAO custAddressDAO = new CustAddressDAO();
        CustomerAddress customerAddress = null;
        ArAgeingDAO arage = new ArAgeingDAO();
        List customer = null;
        List master1 = null;
        List findage = null;
        //String creditHold1= null;
        String acctNumber = null;
        customer = custAddressDAO.findAcctName(customerName);
        if (customer != null) {
            customerAddress = (CustomerAddress) customer.get(0);
        }
        acctNumber = customerAddress.getAccountNo();
        master1 = custAddressDAO.master(acctNumber);
        findage = arage.fimdAge(acctNumber);
        // String creditHold = custAddressDAO.credit(acctNumber);
        request.setAttribute("master", master1);
        request.setAttribute("agemaster", findage);
        // request.setAttribute("creadithold", creditHold);
        request.setAttribute("batch", customerAddress);
    }
}