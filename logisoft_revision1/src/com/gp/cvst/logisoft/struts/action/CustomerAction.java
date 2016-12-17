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





import com.gp.cvst.logisoft.hibernate.dao.ApAgeingDAO;
import com.gp.cvst.logisoft.hibernate.dao.ArAgeingDAO;
import com.gp.cvst.logisoft.hibernate.dao.CustAddressDAO;
import com.gp.cvst.logisoft.hibernate.dao.TransactionDAO;
import com.gp.cvst.logisoft.struts.form.CustomerSearchForm;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.CustAddress;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.domain.Transaction;

/** 
 * MyEclipse Struts
 * Creation date: 05-11-2008
 * 
 * XDoclet definition:
 * @struts.action validate="true"
 */
public class CustomerAction extends Action {
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
        HttpSession session = ((HttpServletRequest) request).getSession();
        //this is for session values

        CustomerSearchForm custsearchform = (CustomerSearchForm) form; // TODO Auto-generated method stub
        String buttonValue = custsearchform.getButtonValue();
        String acctNumber = "";
        String acctName = custsearchform.getAcctName();
        String index = custsearchform.getIndex();

        List findage = null;
        ArAgeingDAO arage = new ArAgeingDAO();
        String path1 = "";
        List master = null;
        String creditHold = "";
        CustAddressDAO custaddressDAO = new CustAddressDAO();
        String search1 = "";
        if (session.getAttribute("search") != null) {
            search1 = (String) session.getAttribute("search");
        }
        if (request.getParameter("index") != null && !request.getParameter("index").equals("")) {
            int ind = 0;
            if (request.getParameter("index") != null && !request.getParameter("index").equals("")) {
                ind = Integer.parseInt(request.getParameter("index"));
            } else {
                ind = Integer.parseInt(index);
            }
            List acctnameList1 = (List) session.getAttribute("acctNameList");
            CustAddress bBean = new CustAddress();
            bBean = (CustAddress) acctnameList1.get(ind);
            String acct = bBean.getAcctName();
            acctNumber = bBean.getAcctNo();
            //acctNumber=custaddressDAO.getCustomerNumber(acct);
            master = custaddressDAO.master(acctNumber);
            findage = arage.fimdAge(acctNumber);
            creditHold = custaddressDAO.credit(acctNumber);
            session.removeAttribute("transList");
            if (search1.equals("applyPayments")) {
                path1 = "jsps/AccountsRecievable/arApplyPayments.jsp";
                session.setAttribute("batch", bBean);
                session.removeAttribute("ApplyPayForm");
            } else if (search1.equals("searchCust")) {

                session.setAttribute("accountNameAr", "ccountNameAr");
                path1 = "jsps/AccountsRecievable/accountRecievableInquiry.jsp";
                session.setAttribute("master", master);
                session.setAttribute("agemaster", findage);
                session.setAttribute("creadithold", creditHold);
                session.setAttribute("batch", bBean);
                TransactionDAO transactionDAO = new TransactionDAO();
                List aptransactionList = transactionDAO.findByAr(bBean.getAcctNo(), "AP");
                Double apAmount = 0.00;
                for (int i = 0; i < aptransactionList.size(); i++) {
                    Transaction t1 = (Transaction) aptransactionList.get(i);
                    if (t1.getBalance() != null) {
                        apAmount = apAmount + t1.getBalance();
                    }
                }
                session.setAttribute("apAmount", apAmount.toString());
                List artransactionList = transactionDAO.findByAr(bBean.getAcctNo(), "AR");
                Double arAmount = 0.00;
                for (int i = 0; i < artransactionList.size(); i++) {
                    Transaction t1 = (Transaction) artransactionList.get(i);
                    if (t1.getBalance() != null) {
                        arAmount = arAmount + t1.getBalance();
                    }
                }
                session.setAttribute("arAmount", arAmount.toString());
                List actransactionList = transactionDAO.findByAr(bBean.getAcctNo(), "AC");
                Double acAmount = 0.00;
                for (int i = 0; i < actransactionList.size(); i++) {
                    Transaction t1 = (Transaction) actransactionList.get(i);
                    if (t1.getBalance() != null) {
                        acAmount = acAmount + t1.getBalance();
                    }
                }
                session.setAttribute("acAmount", acAmount.toString());

            } else if (search1.equals("credithold")) {
                session.setAttribute("accountNameAr", "ccountNameAr");
                path1 = "jsps/AccountsRecievable/ArCreditHold.jsp";
                session.setAttribute("master", master);
                session.setAttribute("agemaster", findage);
                session.setAttribute("creadithold", creditHold);
                session.setAttribute("batch", bBean);
            } else if (search1.equals("accruals")) {
                path1 = "jsps/AccountsPayable/Accruals.jsp";
                session.setAttribute("batch", bBean);
            } else if (search1.equals("accruals2")) {
                path1 = "jsps/AccountsPayable/Accruals.jsp";
                session.setAttribute("Cust2", bBean);
            } else if (search1.equals("searchArInvoice")) {
                session.setAttribute("batch", bBean);
                path1 = "jsps/AccountsRecievable/ARInvoice.jsp";
            } else if (search1.equals("customerstatement")) {
                session.setAttribute("batch", bBean);
                path1 = "jsps/AgingReports/CustomerStatement.jsp";
            } else if (search1.equals("AgingReportCust")) {
                session.setAttribute("batch", bBean);
                path1 = "jsps/AgingReports/agingReports.jsp";
            } else if (search1.equals("searchCustomers")) {
                session.setAttribute("batch", bBean);
                path1 = "jsps/AgingReports/SearchCustomerSample.jsp";
            } else if (search1.equals("searchVendor")) {
                session.removeAttribute("apdetails");
                ApAgeingDAO ApAge = new ApAgeingDAO();
                List findAPage = null;

                findAPage = ApAge.findAge(acctNumber);
                session.setAttribute("findAPage", findAPage);
                session.setAttribute("batch", bBean);
                session.setAttribute("agemaster", findage);
                path1 = "jsps/AccountsPayable/APInquiry.jsp";
            } else if (search1.equals("searchAPCust")) {
                List findAPage = null;
                ApAgeingDAO ApAge = new ApAgeingDAO();
                findAPage = ApAge.findAge(acctNumber);
                session.setAttribute("findAPage", findAPage);
                session.setAttribute("batch", bBean);
                path1 = "jsps/AccountsPayable/AccountPayable.jsp";
            } else if (search1.equals("searchapIn")) {
                session.setAttribute("batch", bBean);
                path1 = "jsps/AccountsPayable/ApInvoice.jsp";
            } else if (search1.equals("manifest4tl")) {

                session.setAttribute("batch", bBean);
                path1 = "jsps/AccountsPayable/manifestForTL.jsp";
            } else if (search1.equals("searchQuoteCust")) {

                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");

                    String cANo = ca.getAcctNo();
                    String cAName = ca.getAcctName();
                    Quotation quotes = null;
                    if (session.getAttribute("QuoteValues") != null) {
                        quotes = (Quotation) session.getAttribute("QuoteValues");
                    } else {
                        quotes = new Quotation();
                    }
                    quotes.setClientname(cAName);
                    quotes.setClientnumber(cANo);
                    if (ca.getContactName() != null) {
                        quotes.setContactname(ca.getContactName());
                    }
                    if (ca.getPhone() != null) {
                        quotes.setPhone(ca.getPhone());
                    }
                    if (ca.getFax() != null) {
                        quotes.setFax(ca.getFax());
                    }
                    if (ca.getEmail1() != null) {
                        quotes.setEmail1(ca.getEmail1());
                    }
                    session.setAttribute("custAdd", quotes);

                }
                path1 = "jsps/fclQuotes/Quote.jsp";
            } else if (search1.equals("editQuoteCust")) {
                session.setAttribute("batch", bBean);


                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cANo = ca.getAcctNo();
                    String cAName = ca.getAcctName();
                    Quotation quotes = null;
                    if (session.getAttribute("QuoteValues") != null) {
                        quotes = (Quotation) session.getAttribute("QuoteValues");
                    } else {
                        quotes = new Quotation();
                    }
                    quotes.setClientname(cAName);
                    quotes.setClientnumber(cANo);
                    if (ca.getContactName() != null) {
                        quotes.setContactname(ca.getContactName());
                    }
                    if (ca.getPhone() != null) {
                        quotes.setPhone(ca.getPhone());
                    }
                    if (ca.getFax() != null) {
                        quotes.setFax(ca.getFax());
                    }
                    if (ca.getEmail1() != null) {
                        quotes.setEmail1(ca.getEmail1());
                    }

                }
                path1 = "jsps/fclQuotes/EditQuote.jsp";

            } //added by chandu for NewBookings.jsp
            else if (search1.equals("searchCustShipper")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    BookingFcl bookingfcl = null;
                    if (session.getAttribute("bookingValues") != null) {
                        bookingfcl = (BookingFcl) session.getAttribute("bookingValues");

                    } else {
                        bookingfcl = new BookingFcl();
                    }
                    if (ca.getPhone() != null) {
                        bookingfcl.setShipperPhone(ca.getPhone());
                    }
                    if (ca.getEmail1() != null) {
                        bookingfcl.setshipperEmail(ca.getEmail1());
                    }
                    if (ca.getAcctName() != null) {
                        bookingfcl.setShipper(ca.getAcctName());
                    }
                    if (ca.getAcctNo() != null) {
                        bookingfcl.setShipNo(ca.getAcctNo());
                    }
                    if (ca.getAddress1() != null) {
                        bookingfcl.setAddressforShipper(ca.getAddress1());
                    }
                    if (ca.getCuntry() != null) {
                        bookingfcl.setShipperCountry(ca.getCuntry().getCodedesc());
                    }
                    if (ca.getState() != null) {
                        bookingfcl.setShipperState(ca.getState());
                    }
                    if (ca.getCity1() != null) {
                        bookingfcl.setShipperCity(ca.getCity1());
                    }
                    if (ca.getZip() != null) {
                        bookingfcl.setShipperZip(ca.getZip());
                    }
                    if (ca.getFax() != null) {
                        bookingfcl.setShipperFax(ca.getFax());
                    }
                    session.setAttribute("bookingValues", bookingfcl);
                    path1 = "jsps/fclQuotes/newBookings.jsp";
                }

            } else if (search1.equals("EditsearchCustShipper")) {

                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    BookingFcl bookingfcl = null;
                    if (session.getAttribute("bookingValues") != null) {
                        bookingfcl = (BookingFcl) session.getAttribute("bookingValues");

                    } else {
                        bookingfcl = new BookingFcl();
                    }

                    if (ca.getAcctName() != null) {
                        bookingfcl.setShipper(ca.getAcctName());
                    }
                    if (ca.getAcctNo() != null) {
                        bookingfcl.setShipNo(ca.getAcctNo());
                    }
                    if (ca.getAddress1() != null) {
                        bookingfcl.setAddressforShipper(ca.getAddress1());
                    }
                    if (ca.getPhone() != null) {
                        bookingfcl.setShipperPhone(ca.getPhone());
                    }
                    if (ca.getEmail1() != null) {
                        bookingfcl.setshipperEmail(ca.getEmail1());
                    }
                    if (ca.getCuntry() != null) {
                        bookingfcl.setShipperCountry(ca.getCuntry().getCodedesc());
                    }
                    if (ca.getState() != null) {
                        bookingfcl.setShipperState(ca.getState());
                    }
                    if (ca.getCity1() != null) {
                        bookingfcl.setShipperCity(ca.getCity1());
                    }
                    if (ca.getZip() != null) {
                        bookingfcl.setShipperZip(ca.getZip());
                    }
                    if (ca.getFax() != null) {
                        bookingfcl.setShipperFax(ca.getFax());
                    }
                    session.setAttribute("bookingValues", bookingfcl);
                    path1 = "jsps/fclQuotes/EditBookings.jsp";
                }
            } //searchCustForwarder
            else if (search1.equals("searchCustForwarder")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    BookingFcl bookingfcl = null;
                    if (session.getAttribute("bookingValues") != null) {
                        bookingfcl = (BookingFcl) session.getAttribute("bookingValues");

                    } else {
                        bookingfcl = new BookingFcl();
                    }

                    if (ca.getAcctName() != null) {
                        bookingfcl.setForward(ca.getAcctName());
                    }
                    if (ca.getAcctNo() != null) {
                        bookingfcl.setForwNo(ca.getAcctNo());
                    }
                    if (ca.getPhone() != null) {
                        bookingfcl.setForwarderPhone(ca.getPhone());
                    }
                    if (ca.getEmail1() != null) {
                        bookingfcl.setForwarderEmail(ca.getEmail1());
                    }
                    if (ca.getAddress1() != null) {
                        bookingfcl.setAddressforForwarder(ca.getAddress1());
                    }
                    if (ca.getCuntry() != null) {
                        bookingfcl.setForwarderCountry(ca.getCuntry().getCodedesc());
                    }
                    if (ca.getState() != null) {
                        bookingfcl.setForwarderState(ca.getState());
                    }
                    if (ca.getCity1() != null) {
                        bookingfcl.setForwarderCity(ca.getCity1());
                    }
                    if (ca.getZip() != null) {
                        bookingfcl.setForwarderZip(ca.getZip());
                    }
                    if (ca.getFax() != null) {
                        bookingfcl.setForwarderFax(ca.getFax());
                    }
                    session.setAttribute("bookingValues", bookingfcl);
                    path1 = "jsps/fclQuotes/newBookings.jsp";
                }
            } else if (search1.equals("EditsearchCustForwarder")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    BookingFcl bookingfcl = null;
                    if (session.getAttribute("bookingValues") != null) {
                        bookingfcl = (BookingFcl) session.getAttribute("bookingValues");

                    } else {
                        bookingfcl = new BookingFcl();
                    }

                    if (ca.getAcctName() != null) {
                        bookingfcl.setForward(ca.getAcctName());
                    }
                    if (ca.getAcctNo() != null) {
                        bookingfcl.setForwNo(ca.getAcctNo());
                    }
                    if (ca.getPhone() != null) {
                        bookingfcl.setForwarderPhone(ca.getPhone());
                    }
                    if (ca.getEmail1() != null) {
                        bookingfcl.setForwarderEmail(ca.getEmail1());
                    }
                    if (ca.getAddress1() != null) {
                        bookingfcl.setAddressforForwarder(ca.getAddress1());
                    }
                    if (ca.getCuntry() != null) {
                        bookingfcl.setForwarderCountry(ca.getCuntry().getCodedesc());
                    }
                    if (ca.getState() != null) {
                        bookingfcl.setForwarderState(ca.getState());
                    }
                    if (ca.getCity1() != null) {
                        bookingfcl.setForwarderCity(ca.getCity1());
                    }
                    if (ca.getZip() != null) {
                        bookingfcl.setForwarderZip(ca.getZip());
                    }
                    if (ca.getFax() != null) {
                        bookingfcl.setForwarderFax(ca.getFax());
                    }
                    session.setAttribute("bookingValues", bookingfcl);
                    path1 = "jsps/fclQuotes/EditBookings.jsp";
                }
            } //searchCustConsignee
            else if (search1.equals("searchCustConsignee")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    BookingFcl bookingfcl = null;
                    if (session.getAttribute("bookingValues") != null) {
                        bookingfcl = (BookingFcl) session.getAttribute("bookingValues");

                    } else {
                        bookingfcl = new BookingFcl();
                    }

                    if (ca.getAcctName() != null) {
                        bookingfcl.setConsignee(ca.getAcctName());
                    }
                    if (ca.getAcctNo() != null) {
                        bookingfcl.setConsNo(ca.getAcctNo());
                    }

                    if (ca.getPhone() != null) {
                        bookingfcl.setConsingeePhone(ca.getPhone());
                    }
                    if (ca.getEmail1() != null) {
                        bookingfcl.setConsingeeEmail(ca.getEmail1());
                    }
                    if (ca.getAddress1() != null) {
                        bookingfcl.setAddressforConsingee(ca.getAddress1());
                    }
                    if (ca.getCuntry() != null) {
                        bookingfcl.setConsigneeCountry(ca.getCuntry().getCodedesc());
                    }
                    if (ca.getState() != null) {
                        bookingfcl.setConsigneeState(ca.getState());
                    }
                    if (ca.getCity1() != null) {
                        bookingfcl.setConsigneeCity(ca.getCity1());
                    }
                    if (ca.getZip() != null) {
                        bookingfcl.setConsigneeZip(ca.getZip());
                    }
                    if (ca.getFax() != null) {
                        bookingfcl.setConsigneeFax(ca.getFax());
                    }
                    session.setAttribute("bookingValues", bookingfcl);
                    path1 = "jsps/fclQuotes/newBookings.jsp";
                }
            } else if (search1.equals("EditsearchCustConsignee")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    BookingFcl bookingfcl = null;
                    if (session.getAttribute("bookingValues") != null) {
                        bookingfcl = (BookingFcl) session.getAttribute("bookingValues");

                    } else {
                        bookingfcl = new BookingFcl();
                    }

                    if (ca.getAcctName() != null) {
                        bookingfcl.setConsignee(ca.getAcctName());
                    }
                    if (ca.getAcctNo() != null) {
                        bookingfcl.setConsNo(ca.getAcctNo());
                    }

                    if (ca.getPhone() != null) {
                        bookingfcl.setConsingeePhone(ca.getPhone());
                    }
                    if (ca.getEmail1() != null) {
                        bookingfcl.setConsingeeEmail(ca.getEmail1());
                    }
                    if (ca.getAddress1() != null) {
                        bookingfcl.setAddressforConsingee(ca.getAddress1());
                    }
                    if (ca.getCuntry() != null) {
                        bookingfcl.setConsigneeCountry(ca.getCuntry().getCodedesc());
                    }
                    if (ca.getState() != null) {
                        bookingfcl.setConsigneeState(ca.getState());
                    }
                    if (ca.getCity1() != null) {
                        bookingfcl.setConsigneeCity(ca.getCity1());
                    }
                    if (ca.getZip() != null) {
                        bookingfcl.setConsigneeZip(ca.getZip());
                    }
                    if (ca.getFax() != null) {
                        bookingfcl.setConsigneeFax(ca.getFax());
                    }
                    session.setAttribute("bookingValues", bookingfcl);
                    path1 = "jsps/fclQuotes/EditBookings.jsp";
                }
            } else if (search1.equals("searchshipper")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    FclBl fclbl = null;
                    if (session.getAttribute("fclBillValues") != null) {
                        fclbl = (FclBl) session.getAttribute("fclBillValues");
                    } else {
                        fclbl = new FclBl();
                    }

                    if (ca.getAcctNo() != null) {
                        fclbl.setShipperNo(ca.getAcctNo());
                    }
                    session.setAttribute("fclBillValues", fclbl);
                    path1 = "jsps/fclQuotes/FclBillLadding.jsp";
                }
            } else if (search1.equals("searchconsignee")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    FclBl fclbl = null;
                    if (session.getAttribute("fclBillValues") != null) {
                        fclbl = (FclBl) session.getAttribute("fclBillValues");
                    } else {
                        fclbl = new FclBl();
                    }

                    if (ca.getAcctNo() != null) {
                        fclbl.setConsigneeNo(ca.getAcctNo());
                    }

                    session.setAttribute("fclBillValues", fclbl);
                    path1 = "jsps/fclQuotes/FclBillLadding.jsp";
                }
            } else if (search1.equals("searchhouseShipper")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    FclBl fclbl = null;
                    if (session.getAttribute("fclBillValues") != null) {
                        fclbl = (FclBl) session.getAttribute("fclBillValues");
                    } else {
                        fclbl = new FclBl();
                    }

                    if (ca.getAcctNo() != null) {
                        fclbl.setHouseShipper(ca.getAcctNo());
                    }

                    session.setAttribute("fclBillValues", fclbl);
                    path1 = "jsps/fclQuotes/FclBillLadding.jsp";
                }
            } else if (search1.equals("searchhouseConsignee")) {
                List custList = custaddressDAO.getCustomerNamesList1(acctNumber);
                for (int i = 0; i < custList.size(); i++) {
                    CustAddress cust = (CustAddress) custList.get(i);
                    if (cust.getPrimeAddress().equalsIgnoreCase("on")) {
                        request.setAttribute("cust", cust);
                        break;
                    }
                }
                if (request.getAttribute("cust") != null) {
                    CustAddress ca = (CustAddress) request.getAttribute("cust");
                    String cNAME = ca.getAcctName();
                    String cANO = ca.getAcctNo();
                    String Address = ca.getAddress1();
                    FclBl fclbl = null;
                    if (session.getAttribute("fclBillValues") != null) {
                        fclbl = (FclBl) session.getAttribute("fclBillValues");
                    } else {
                        fclbl = new FclBl();
                    }

                    if (ca.getAcctNo() != null) {
                        fclbl.setHouseConsignee(ca.getAcctNo());
                    }
                    session.setAttribute("fclBillValues", fclbl);
                    path1 = "jsps/fclQuotes/FclBillLadding.jsp";
                }
            } else if (search1.equals("searchShipper1")) {
                session.setAttribute("batch", bBean);
                path1 = "jsps/fclQuotes/FclBl.jsp";
            } else if (search1.equals("searchQuotation")) {
                session.setAttribute("cAName", acct);
                path1 = "jsps/fclQuotes/SearchQuotation.jsp";


            }


            request.setAttribute("path1", path1);
            request.setAttribute("buttonValue", "completed");
        } else {
            if (buttonValue.equals("search")) {

                if (session.getAttribute("acctNameList") != null) {
                    session.removeAttribute("acctNameList");
                }
                String address = "";

                String acctType = "";
                List acctNameList = new ArrayList();
                if (search1.equals("searchCustShipper") || search1.equals("EditsearchCustShipper")) {
                    acctType = "S";
                    acctNameList = custaddressDAO.findBy(acctName, acctNumber, address, acctType);
                    session.setAttribute("acctNameList", acctNameList);
                } else if (search1.equals("searchCustForwarder") || search1.equals("EditsearchCustForwarder")) {

                    acctType = "F";
                    acctNameList = custaddressDAO.findBy(acctName, acctNumber, address, acctType);
                    session.setAttribute("acctNameList", acctNameList);
                } else if (search1.equals("searchCustConsignee") || search1.equals("EditsearchCustConsignee")) {
                    acctType = "C";
                    acctNameList = custaddressDAO.findBy(acctName, acctNumber, address, acctType);
                    session.setAttribute("acctNameList", acctNameList);
                } else {
                    //commented by vasan because the customer search should return only the primary address.
                    //acctNameList =custaddressDAO.findBy(acctName, acctNumber,address,acctType);
                    acctNameList = custaddressDAO.getPrimaryCustomerAddress(acctName);
                    session.setAttribute("acctNameList", acctNameList);
                    if (acctNameList.size() == 1) {
                        request.setAttribute("acctName", acctName);
                    }
                }


            }
        }

        return mapping.findForward("success");
    }
}
