/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.bc.fcl.ImportBc;
import com.gp.cong.logisoft.bc.fcl.QuotationBC;
import com.gp.cong.logisoft.bc.fcl.QuotationConstants;
import com.gp.cong.logisoft.bc.ratemanagement.PortsBC;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.UserAgentInformation;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.domain.Charges;
import com.gp.cvst.logisoft.domain.FclBl;
import com.gp.cvst.logisoft.domain.Quotation;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import com.gp.cvst.logisoft.hibernate.dao.ChargesDAO;
import com.gp.cvst.logisoft.hibernate.dao.FclBlDAO;
import com.gp.cvst.logisoft.hibernate.dao.QuotationDAO;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.gp.cvst.logisoft.struts.form.QuotesForm;
import com.gp.cvst.logisoft.struts.form.SearchQuotationForm;
import java.util.Date;

/**
 * MyEclipse Struts Creation date: 07-17-2008
 *
 * XDoclet definition:
 *
 * @struts.action path="/searchquotation" name="searchQuotationform"
 * input="/jsps/Accounting/SearchQuotation.jsp" scope="request"
 * @struts.action-forward name="success"
 * path="/jsps/Accounting/SearchQuotation.jsp"
 */
public class SearchQuotationAction extends Action {
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
        SearchQuotationForm searchQuotationform = (SearchQuotationForm) form;// TODO Auto-generated method stub
        HttpSession session = request.getSession(true);
        BookingFclDAO bookingFclDAO = new BookingFclDAO();
        String action = searchQuotationform.getButtonValue();
        String forwardName = "";
        String quoteno = searchQuotationform.getQuoteno();
        String plor = searchQuotationform.getPlor();
        String pol = searchQuotationform.getPol();
        String pod = searchQuotationform.getPod();
        String clienttype = searchQuotationform.getClienttype();
        String plod = searchQuotationform.getPlod();
        String quotestartdate = searchQuotationform.getQuotestartdate();
        String quoteendate = searchQuotationform.getQuoteendate();
        String quoteBY = searchQuotationform.getQuoteBy();
        String clientName = searchQuotationform.getClient();
        String issuingTerminal = searchQuotationform.getIssuingTerminal();
        String carier = searchQuotationform.getCarrier();
        MessageResources messageResources = getResources(request);
        QuotationBC quotationBC = new QuotationBC();
        FclBl fclBl = new FclBl();
        List QuotationList = null;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        boolean importFlag = (null != request.getParameter("importFlag") && request.getParameter("importFlag").equalsIgnoreCase("true"));
        if (!importFlag) {
            importFlag = null != session.getAttribute(ImportBc.sessionName);
        }
        session.setAttribute("companyMnemonicCode", new SystemRulesDAO().getSystemRulesByCode("CompanyNameMnemonic"));
        request.setAttribute("enableIms", LoadLogisoftProperties.getProperty("ims.enable"));
        User user = null;
        Integer userId = 0;
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
            userId = user.getUserId();
        }
        if ("fileSearch".equals(action)) {
            search(searchQuotationform, request, mapping);
            return mapping.findForward("fileNumberPage");
        }
        if (request.getParameter("paramid1") != null) {
            if (session.getAttribute("view") != null) {
                session.removeAttribute("view");
            }
            String paramid = request.getParameter("paramid1");
            QuotationDAO quotationDAO = new QuotationDAO();
            Quotation editQuatation = quotationDAO.findById(Integer.parseInt(paramid));
            ProcessInfoBC processInfoBC = new ProcessInfoBC();
            //======== not in used.........................
            List list = quotationBC.getQuatationInfo(editQuatation.getQuoteId());
            QuatationInfo quatationInfo = new QuatationInfo(list);
            if (CommonFunctions.isNotNull(editQuatation.getFileNo())) {
                String returnResult = "";
                if (quotationBC.findConvertedRecords(editQuatation.getFileNo(), true)) {
                    returnResult = processInfoBC.cheackFileNumberForLoack(editQuatation.getFileNo().toString(), userId.toString(), QuotationConstants.QUOTE);
                    if (CommonFunctions.isNotNull(returnResult)) {
                        // setting request.
                        request.setAttribute(QuotationConstants.LOCK, "This Record is Used by " + returnResult);
                    }
                } else {
                    returnResult = processInfoBC.cheackFileINDB(editQuatation.getFileNo().toString(), userId.toString()); //bookingInfo.getUsedBy();
                    //returnResult = quatationInfo.getUsedBy();
                    String processInfoDate = DateUtils.formatDate(quatationInfo.getProcessInfoDate(), "MM/dd/yyyy hh:mm a");
                    if (CommonFunctions.isNotNull(returnResult) && !returnResult.equals("sameUser")) {
                        // setting request.
                        request.setAttribute(QuotationConstants.LOCK, "This Record is Used by " + returnResult + " " + processInfoDate);
                    }
                }
            }
            editQuatation.setQuoteNo(editQuatation.getQuoteId().toString());
            ChargesDAO cDao = new ChargesDAO();
            List fclRates = (List) cDao.getChargesforQuotation1(editQuatation.getQuoteId());

            String num = "";
            Double myr = 0.00;
            Double nht = 0.00;
            Double pkr = 0.00;
            Double rm = 0.00;
            Double spo = 0.00;
            Double vnd = 0.00;
            Double inr = 0.00;
            List unitsList = new ArrayList();
            String currency[] = messageResources.getMessage("currency").split(",");

            for (int i = 0; i < fclRates.size(); i++) {
                //set the unit descriptions
                Charges charges = (Charges) fclRates.get(i);
                GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(charges.getUnitType()));
                if (genericCode != null) {
                    charges.setUnitName(genericCode.getCodedesc());
                }
                charges.setNewFlag(null);

                //calculate the unit
                if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currency[0])) {
                    myr = myr + charges.getAmount();
                    myr = myr + charges.getMarkUp();
                }
                if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currency[1])) {
                    nht = nht + charges.getAmount();
                    nht = nht + charges.getMarkUp();
                }
                if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currency[2])) {
                    pkr = pkr + charges.getAmount();
                    pkr = pkr + charges.getMarkUp();
                }
                if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currency[3])) {
                    rm = rm + charges.getAmount();
                    rm = rm + charges.getMarkUp();
                }
                if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currency[4])) {
                    spo = spo + charges.getAmount();
                    spo = spo + charges.getMarkUp();
                }
                if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currency[5])) {
                    vnd = vnd + charges.getAmount();
                    vnd = vnd + charges.getMarkUp();
                }
                if (charges.getCurrecny() != null && charges.getCurrecny().trim().equals(currency[6])) {
                    inr = inr + charges.getAmount();
                    inr = inr + charges.getMarkUp();
                }
                if (charges.getCurrecny() != null && charges.getNumber() != null) {
                    num = charges.getUnitType();
                    unitsList.add(num);
                }
            }
            if ("N".equalsIgnoreCase(editQuatation.getRatesNonRates())) {
                request.setAttribute("consolidatorList", new QuotationBC().orderNonRatedList(fclRates));
                request.setAttribute(QuotationConstants.FCLRATES, new QuotationBC().orderNonRatedList(fclRates));
            } else {
                List consolidatorList = quotationBC.consolidateRates(fclRates, messageResources,importFlag);
                request.setAttribute("consolidatorList", consolidatorList);
                request.setAttribute(QuotationConstants.FCLRATES, new QuotationBC().orderExpandList(fclRates));
            }

            //get the charges which doesn't have the Unit type.
            List otherChargesList = new ArrayList();
            List chargesList = (List) cDao.getChargesforQuotation8(editQuatation.getQuoteId());
            List perkglbsList = new ArrayList();
            for (Object o : chargesList) {
                Charges c1 = (Charges) o;
                c1.setNewFlag(null);
                String costType = c1.getCostType().trim();
                if (costType.equalsIgnoreCase(messageResources.getMessage("per1000kg"))
                        || costType.equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                    perkglbsList.add(c1);
                } else {
                    otherChargesList.add(c1);
                    if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[0])) {
                        myr = myr + c1.getRetail();
                    }
                    if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[1])) {
                        nht = nht + c1.getRetail();
                    }
                    if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[2])) {
                        pkr = pkr + c1.getRetail();
                    }
                    if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[3])) {
                        rm = rm + c1.getRetail();
                    }
                    if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[4])) {
                        spo = spo + c1.getRetail();
                    }
                    if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[5])) {
                        vnd = vnd + c1.getRetail();
                    }
                    if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[6]) && c1.getRetail() != null) {
                        inr = inr + c1.getRetail();
                    }
                }

            }
            editQuatation.setMyr(myr);
            editQuatation.setNht(nht);
            editQuatation.setPkr(pkr);
            editQuatation.setRm(rm);
            editQuatation.setSpo(spo);
            editQuatation.setVnd(vnd);
            editQuatation.setInr(inr);
            //--TO DISPLAY "HAZARDOUS CARGO" MESSAGE---            

            if ("Quote".equals(quatationInfo.getDocTypeCode())) {
                String msg = "HAZARDOUS CARGO";
                if (request.getAttribute("msg") == null) {
                    request.setAttribute("msg", msg);
                }
            }
            //In quotation right now if the hazmat radio button is clicked to yes then we will consider it as hazardous
            //so no need to check in the database for hazmat material's
            if ("Y".equalsIgnoreCase(editQuatation.getHazmat())) {
                String msg = "";
                msg = "HAZARDOUS CARGO";
                if (request.getAttribute("msg") == null) {
                    request.setAttribute("msg", msg);
                }
            }
            //---to display booking by and booked date-------            
            editQuatation.setBookedBy(quatationInfo.getBookedBy());
            editQuatation.setBookedDate(quatationInfo.getBookingDate());
            //--TO DISPLAY blby and bl on date-----            
            editQuatation.setBlBy(quatationInfo.getBlBy());
            editQuatation.setBlDate(quatationInfo.getBolDate());

            String originCode = null;
            String propRemarks = null;
            if (editQuatation.getOrigin_terminal().lastIndexOf("(") != -1) {
                originCode = editQuatation.getOrigin_terminal().substring(editQuatation.getOrigin_terminal().lastIndexOf("(") + 1,
                        editQuatation.getOrigin_terminal().lastIndexOf(")"));
            }
            if (editQuatation.getZip() != null && !editQuatation.getZip().trim().equals("")) {
                propRemarks = new UnLocationDAO().getpropertyRemarks(originCode);
            }
            if (propRemarks != null) {
                request.setAttribute("remarks", propRemarks);
            }
            request.setAttribute(QuotationConstants.OTHERCHARGESLIST, otherChargesList);
            request.setAttribute(QuotationConstants.QUOTEVALUES, editQuatation);
            request.setAttribute("specialEquipmentUnitList", new ChargesDAO().getGroupByUnitType(editQuatation.getQuoteId()));
            session.setAttribute(QuotationConstants.QUOTATIONOLD, editQuatation);
            request.setAttribute(QuotationConstants.PERKGLBSLIST, perkglbsList);
            request.setAttribute("TotalScan", quatationInfo.getOperationCount());
            if (session.getAttribute("transactionbean") != null) {
                session.removeAttribute("transactionbean");
            }
            return mapping.findForward("viewQuotation");// page name
        } else if (request.getParameter("paramid2") != null) {
            String paramid = request.getParameter("paramid2");
            Quotation editdetails = new Quotation();
            QuotationDAO acdao1 = new QuotationDAO();
            String view = "3";
            // session.setAttribute("view", view);
            editdetails = acdao1.findById(Integer.parseInt(paramid));

            editdetails.setQuoteNo(editdetails.getQuoteId().toString());
            Double myr = 0.00;
            Double nht = 0.00;
            Double pkr = 0.00;
            Double rm = 0.00;
            Double spo = 0.00;
            Double vnd = 0.00;
            Double inr = 0.00;
            ChargesDAO cDao = new ChargesDAO();

            List fclRates = (List) cDao.getChargesforQuotation1(editdetails.getQuoteId());
            for (int i = 0; i < fclRates.size(); i++) {
                Charges charges = (Charges) fclRates.get(i);
                if (charges.getUnitType() != null && !charges.getUnitType().equals("") && !charges.getUnitType().equals("0.00")) {
                    GenericCode genericCode = genericCodeDAO.findById(Integer.parseInt(charges.getUnitType()));
                    charges.setUnitName(genericCode.getCodedesc());
                }
            }
            String currency[] = messageResources.getMessage("currency").split(",");
            for (int i = 0; i < fclRates.size(); i++) {
                Charges c1 = (Charges) fclRates.get(i);
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[0])) {
                    myr = myr + c1.getAmount();
                    myr = myr + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[1])) {
                    nht = nht + c1.getAmount();
                    nht = nht + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[2])) {
                    pkr = pkr + c1.getAmount();
                    pkr = pkr + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[3])) {
                    rm = rm + c1.getAmount();
                    rm = rm + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[4])) {
                    spo = spo + c1.getAmount();
                    spo = spo + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[5])) {
                    vnd = vnd + c1.getAmount();
                    vnd = vnd + c1.getMarkUp();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[6])) {
                    inr = inr + c1.getAmount();
                    inr = inr + c1.getMarkUp();
                }
            }
            List consolidatorList = quotationBC.consolidateRates(fclRates, messageResources,importFlag);
            request.setAttribute("consolidatorList", consolidatorList);
            request.setAttribute(QuotationConstants.FCLRATES, new QuotationBC().orderExpandList(fclRates));
            List otherChargesList = new ArrayList();
            List otherChargesLIst = (List) cDao.getChargesforQuotation8(editdetails.getQuoteId());
            List perkglbsList = new ArrayList();
            for (int i = 0; i < otherChargesLIst.size(); i++) {
                Charges c1 = (Charges) otherChargesLIst.get(i);
                if (c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per1000kg")) || c1.getCostType().trim().equalsIgnoreCase(messageResources.getMessage("per2000lbs"))) {
                    perkglbsList.add(c1);
                } else {
                    otherChargesList.add(c1);
                }
            }
            for (int i = 0; i < otherChargesList.size(); i++) {
                Charges c1 = (Charges) otherChargesList.get(i);
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[0])) {
                    myr = myr + c1.getRetail();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[1])) {
                    nht = nht + c1.getRetail();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[2])) {
                    pkr = pkr + c1.getRetail();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[3])) {
                    rm = rm + c1.getRetail();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[4])) {
                    spo = spo + c1.getRetail();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[5])) {
                    vnd = vnd + c1.getRetail();
                }
                if (c1.getCurrecny() != null && c1.getCurrecny().trim().equals(currency[6])) {
                    inr = inr + c1.getRetail();
                }
            }
            editdetails.setMyr(myr);
            editdetails.setNht(nht);
            editdetails.setPkr(pkr);
            editdetails.setRm(rm);
            editdetails.setSpo(spo);
            editdetails.setVnd(vnd);
            editdetails.setInr(inr);
            //to show booked by and booked date
            BookingFcl bookingFcl = bookingFclDAO.getFileNoObject(editdetails.getFileNo());
            if (bookingFcl != null) {
                if (bookingFcl.getBookedBy() != null) {
                    editdetails.setBookedBy(bookingFcl.getBookedBy());
                }
                if (bookingFcl.getBookingDate() != null) {
                    editdetails.setBookedDate(bookingFcl.getBookingDate());
                }
            }

            //--TO DISPLAY blby and bl on date-----
            fclBl = quotationBC.getfclby(editdetails.getFileNo());
            if (fclBl != null) {
                editdetails.setBlBy(fclBl.getBlBy());
                editdetails.setBlDate(fclBl.getBolDate());
            }
            request.setAttribute(QuotationConstants.OTHERCHARGESLIST, otherChargesList);
            request.setAttribute(QuotationConstants.QUOTEVALUES, editdetails);
            request.setAttribute(QuotationConstants.PERKGLBSLIST, perkglbsList);
            request.setAttribute("specialEquipmentUnitList", new ChargesDAO().getGroupByUnitType(editdetails.getQuoteId()));
            return mapping.findForward("viewQuotation");
        } else if (request.getParameter("addQuote") != null) {
            QuotesForm quotesForm = new QuotesForm();
            quotesForm = quotationBC.getQuotesForm(quotesForm);
            if (!importFlag && user.getTerminal() != null && !"09".equalsIgnoreCase(user.getTerminal().getTrmnum())
                    && !"19".equalsIgnoreCase(user.getTerminal().getTrmnum())) {
                quotesForm.setIssuingTerminal(user.getTerminal().getTerminalLocation() + "-" + user.getTerminal().getTrmnum());
            } else {
                if (importFlag && null != user.getImportTerminal()) {
                    quotesForm.setIssuingTerminal(null != user.getImportTerminal() ? user.getImportTerminal().getTerminalLocation() + "-" + user.getImportTerminal().getTrmnum() : "");
                }
            }
            quotesForm.setCostofgoods("0.00");
            if (importFlag) {
                quotesForm.setCommcode("006205");
            } else {
                quotesForm.setCommcode("006100");
            }
            quotesForm.setImportFlag(importFlag);
            request.setAttribute(QuotationConstants.QUOTATIONFORM, quotesForm);
            request.setAttribute("quotationNo", "NEW");
            return mapping.findForward("addQuote");
        } else {
            if (action != null && action.equals("correctedBL")) {
                // for testing corrected Bl
                request.setAttribute("blList", new FclBlDAO().correctedBL());
                forwardName = "correctedBL";
            } else if (action != null && action.equals("showall")) {
                QuotationDAO quotationdao = new QuotationDAO();
                QuotationList = (List) quotationdao.showfordeatils1();
                request.setAttribute(QuotationConstants.SEARCHQUOTATIONLIST, QuotationList);
                session.removeAttribute("QuoteOrigin");
                session.removeAttribute("QuoteDiscaharge");
                forwardName = "success";
            } else if (action != null && action.equals("search")) {
                forwardName = "success";
            } else if (action != null && action.equals("searchquotation")) {
                Quotation quotationdomain = new Quotation();
                QuotationDAO quotationdao = new QuotationDAO();
                QuotationList = (List) quotationdao.showsearchquot(quoteno, plor, quotestartdate, quoteendate, plod, pol, pod, clienttype, clientName, carier, quoteBY, issuingTerminal);
                quotationdomain.setQuoteNo(quoteno);
                quotationdomain.setClienttype(clienttype);
                quotationdomain.setPlor(plor);
                quotationdomain.setPlod(plod);
                quotationdomain.setOrigin_terminal(pol);
                quotationdomain.setDestination_port(pod);
                quotationdomain.setClientname(clientName);
                quotationdomain.setCarrier(carier);
                quotationdomain.setIssuingTerminal(issuingTerminal);
                request.setAttribute("quotecollapse", "quotecollapse");
                request.setAttribute("searchQuotationValues", quotationdomain);
                request.setAttribute(QuotationConstants.SEARCHQUOTATIONLIST, QuotationList);
                request.setAttribute("quotestartdate", quotestartdate);
                request.setAttribute("quoteendate", quoteendate);
                if (pol.equals("")) {
                    session.removeAttribute("QuoteOrigin");
                }
                if (pod.equals("")) {
                    session.removeAttribute("QuoteDiscaharge");
                }
                forwardName = "success";
            } else if (action != null && (action.equals("addQuote") || request.getParameter("addQuote") != null)) {
                QuotesForm quotesForm = new QuotesForm();
                quotesForm = quotationBC.getQuotesForm(quotesForm);
                quotesForm.setCostofgoods("0.00");
                if (request.getParameter("commcode") != null && request.getParameter("commcode").equals("006100")) {
                    quotesForm.setCommcode(request.getParameter("commcode"));
                }
                request.setAttribute(QuotationConstants.QUOTATIONFORM, quotesForm);
                forwardName = "addQuote";
            } else if ("searchPort".equals(action)) {
                PortsBC portsBC = new PortsBC();
                request.setAttribute("regions", portsBC.getAllRegion1());
                request.setAttribute("textName", request.getParameter("textName"));
                request.setAttribute("agent", request.getParameter("agent"));
                request.setAttribute("from", request.getParameter("from"));
                request.setAttribute("typeOfmove", request.getParameter("typeOfmove"));
                request.setAttribute("setFocus", request.getParameter("setFocus"));
                request.setAttribute("fclOrigin", request.getParameter("fclOrigin"));
                request.setAttribute("fclDestination", request.getParameter("fclDestination"));
                request.setAttribute("NonRated", request.getParameter("NonRated"));
                forwardName = "portSearchPage";
            } else if (action != null && action.equals("refresh")) {
                if (session.getAttribute(QuotationConstants.FILESEARCHLIST) != null) {
                    session.removeAttribute(QuotationConstants.FILESEARCHLIST);
                }
                forwardName = "fileNumberPage";
            } else if ("reset".equals(action)) {
                if (session.getAttribute("searchQuotationForm") != null) {
                    session.removeAttribute("searchQuotationForm");
                }
                forwardName = "fileNumberPage";
            }
        }
        return mapping.findForward(forwardName);
    }

    public void search(SearchQuotationForm searchQuotationform, HttpServletRequest request, ActionMapping mapping) throws Exception {
        HttpSession session = request.getSession();
        QuotationBC quotationBC = new QuotationBC();
        String loginName = getUserName(session);
        boolean importFlag = (null != session.getAttribute(ImportBc.sessionName)) ? true : false;
        User user = ((User) session.getAttribute("loginuser"));
        StringBuilder queryString = new StringBuilder();
        StringBuilder agentDestQuery = new StringBuilder();
        if (null != user && null != user.getRole() && "Agent".equalsIgnoreCase(user.getRole().getRoleDesc())) {
            List userAgentList = new UserDAO().findByUserAgentProperty("userId.userId", user.getUserId());
            boolean isFirst = false;
            for (Object object : userAgentList) {
                UserAgentInformation agent = (UserAgentInformation) object;
                String value = "";
                if (null != agent.getPortId()) {
                    if (CommonUtils.isNotEmpty(agent.getPortId().getStateCode())) {
                        if (importFlag) {
                            value = agent.getPortId().getPortname() + "/" + agent.getPortId().getStateCode() + "/" + agent.getPortId().getCountryName() + "/(" + agent.getPortId().getUnLocationCode() + ")";
                        } else {
                            value = agent.getPortId().getPortname() + "/" + agent.getPortId().getStateCode() + "/" + agent.getPortId().getCountryName() + "(" + agent.getPortId().getUnLocationCode() + ")";
                        }
                    } else {
                        if (importFlag) {
                            value = agent.getPortId().getPortname() + "/" + agent.getPortId().getCountryName() + "/(" + agent.getPortId().getUnLocationCode() + ")";
                        } else {
                            value = agent.getPortId().getPortname() + "/" + agent.getPortId().getCountryName() + "(" + agent.getPortId().getUnLocationCode() + ")";
                        }
                    }
                    if (!isFirst) {
                        agentDestQuery.append(" q.destination_port like '").append(value).append("%'");
                        isFirst = true;
                    } else {
                        agentDestQuery.append(" or q.destination_port like '").append(value).append("%'");
                    }
                }
            }
            if (CommonUtils.isNotEmpty(agentDestQuery)) {
                queryString.append("(").append(agentDestQuery).append(")");
            } else {
                queryString.append(" q.destination_port = No Agent Destination found");
            }
        }
        session.setAttribute(QuotationConstants.FILESEARCHLIST, quotationBC.getSearchListByFileNumber(searchQuotationform, importFlag, queryString.toString()));
        request.setAttribute(QuotationConstants.SEARCHQUOTATIONFORM, searchQuotationform);
        if (new QuotationDAO().IsSearchScreenReset(loginName)) {
            session.removeAttribute(QuotationConstants.SEARCHQUOTATIONFORM);
        } else {
            session.setAttribute(QuotationConstants.SEARCHQUOTATIONFORM, searchQuotationform);
        }
        if (searchQuotationform.getQuoteBy() != null && !loginName.equalsIgnoreCase(searchQuotationform.getQuoteBy())) {
            request.setAttribute("NotloginName", "NotloginName");
        }
    }

    /**
     * Get the user Name from session
     *
     * @param session - HttpSession Object
     * @return - Current User Name
     */
    public String getUserName(HttpSession session) {
        String loginName = null;
        if (session.getAttribute("loginuser") != null) {
            loginName = ((User) session.getAttribute("loginuser")).getLoginName();
        }
        return loginName;
    }
}

class QuatationInfo {

    private String fileNumber;
    private int quoteId;
    private int bookingId;
    private int bol;
    private int processId;
    private String bookedBy;
    private Date bookingDate;
    private String blBy;
    private Date bolDate;
    private int operationCount;
    private String docTypeCode;
    private String usedBy;
    private Date processInfoDate;

    QuatationInfo(List list) {
        if (list != null && !list.isEmpty()) {
            Object quoteInfo[] = (Object[]) list.get(0);
            fileNumber = quoteInfo[0] == null ? null : quoteInfo[0].toString();
            quoteId = quoteInfo[1] == null ? -1 : (Integer) quoteInfo[1];
            bookingId = quoteInfo[2] == null ? -1 : (Integer) quoteInfo[2];
            bol = quoteInfo[3] == null ? -1 : (Integer) quoteInfo[3];
            processId = quoteInfo[4] == null ? -1 : (Integer) quoteInfo[4];
            bookedBy = quoteInfo[5] == null ? null : (String) quoteInfo[5];
            bookingDate = quoteInfo[6] == null ? null : (Date) quoteInfo[6];
            blBy = quoteInfo[7] == null ? null : (String) quoteInfo[7];
            bolDate = quoteInfo[8] == null ? null : (Date) quoteInfo[8];
            operationCount = quoteInfo[9] == null ? -1 : new Integer(quoteInfo[9].toString());
            docTypeCode = quoteInfo[10] == null ? null : (String) quoteInfo[10];
            usedBy = quoteInfo[11] == null ? null : (String) quoteInfo[11];
            processInfoDate = quoteInfo[12] == null ? null : (Date) quoteInfo[12];
        }
    }

    public String getBlBy() {
        return blBy;
    }

    public int getBol() {
        return bol;
    }

    public Date getBolDate() {
        return bolDate;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getDocTypeCode() {
        return docTypeCode;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public int getProcessId() {
        return processId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public Date getProcessInfoDate() {
        return processInfoDate;
    }
}
