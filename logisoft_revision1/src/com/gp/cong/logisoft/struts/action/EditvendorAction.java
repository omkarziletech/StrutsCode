/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.NoteBean;
import com.gp.cong.logisoft.beans.customerBean;
import com.gp.cong.logisoft.domain.AuditLogRecordVendor;
import com.gp.cong.logisoft.domain.Customer;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.TradingPartner;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.Vendor;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.EditvendorForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 09-19-2008
 * 
 * XDoclet definition:
 * @struts.action path="/editvendor" name="editvendorForm" input="/jsps/Tradingpartnermaintainance/Editvendor.jsp" scope="request" validate="true"
 */
public class EditvendorAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        EditvendorForm editvendorForm = (EditvendorForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        Vendor vendorDomainObj = new Vendor();
        customerBean customerbean = new customerBean();
        String E_legalname = editvendorForm.getLegalname();
        String E_dba = editvendorForm.getDba();
        String E_tin = editvendorForm.getTin();
        String E_wfile = editvendorForm.getWfile();
        String E_apname = editvendorForm.getApname();
        String E_phoneno = editvendorForm.getPhone();
        String E_fax = editvendorForm.getFax();
        String E_email = editvendorForm.getEmail();
        String E_web = editvendorForm.getWeb();
        String E_ecomanager = editvendorForm.getEamanager();
        String E_creditstatus = editvendorForm.getCredit();
        String E_crlimit = editvendorForm.getClimit();
        String E_crterms = editvendorForm.getCterms();
        String E_bankacct = editvendorForm.getBaccount();
        String E_deact = editvendorForm.getDeactivate();
        String buttonValue = editvendorForm.getButtonValue();
        DBUtil dbUtil = new DBUtil();
        String forward = "";
        if (session.getAttribute("VendorInfoList") != null) {
            vendorDomainObj = (Vendor) session.getAttribute("VendorInfoList");
        } else {
            vendorDomainObj = new Vendor();
        }
        vendorDomainObj.setLegalname(E_legalname);
        vendorDomainObj.setDba(E_dba);
        vendorDomainObj.setTin(E_tin);
        vendorDomainObj.setApname(E_apname);
        vendorDomainObj.setPhone(E_phoneno);
        vendorDomainObj.setFax(E_fax);
        vendorDomainObj.setEmail(E_email);
        vendorDomainObj.setWeb(E_web);
        vendorDomainObj.setBaccount(E_bankacct);
        customerbean.setWfile(E_wfile);
        customerbean.setCredit(E_creditstatus);
        customerbean.setDeactivate(E_deact);
        session.setAttribute("customerbean", customerbean);

        UserDAO userDAO = new UserDAO();
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        GenericCode creditRt = null;
        User arContact = new User();
        if (E_ecomanager != null && E_ecomanager != "0") {
            arContact = userDAO.findById(Integer.parseInt(E_ecomanager));
        //vendorDomainObj.setEamanager(arContact);
        }


        if (E_crlimit != null && E_crlimit != "") {
            Double creditLmt = Double.parseDouble(E_crlimit);
            vendorDomainObj.setClimit(creditLmt);
        }

        if (E_crterms != null && E_crterms != "0") {
            creditRt = genericCodeDAO.findById(Integer.parseInt(E_crterms));
            vendorDomainObj.setCterms(creditRt);
        }

        if (E_wfile != null) {
            vendorDomainObj.setWfile("on");
        } else {
            vendorDomainObj.setWfile("off");
        }
        if (E_creditstatus != null) {
            vendorDomainObj.setCredit("on");
        } else {
            vendorDomainObj.setCredit("off");
        }
        if (E_deact != null) {
            vendorDomainObj.setDeactivate("on");
        } else {
            vendorDomainObj.setDeactivate("off");
        }

        session.setAttribute("VendorInfoList", vendorDomainObj);
        forward = "toeditjsp";

        if (buttonValue != null && buttonValue.equals("checked")) {

            TradingPartner tradingPartner = (TradingPartner) session.getAttribute("TradingPartner");
            CustomerDAO customerDAO = new CustomerDAO();

            String accountNo = customerDAO.masterName(tradingPartner.getAccountName());

            if (accountNo != null) {
                List id = customerDAO.find1(accountNo);

                Iterator it = id.iterator();
                List list = new ArrayList();
                while (it.hasNext()) {

                    Customer customer = customerDAO.findById2(new Integer(it.next().toString()));
                    session.setAttribute("addressMaster", customer);
                    list.add(customer);

                }

                session.setAttribute("masteraddressList", list);//-------to display address-------
                request.setAttribute("openwindow", "openwindow");
            }

        } else if (buttonValue != null && buttonValue.equals("note")) {
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";
            if (session.getAttribute("processinfoforcustomer") != null) {
                String itemId = (String) session.getAttribute("processinfoforcustomer");
                item = itemDAO.findById(Integer.parseInt(itemId));
                itemName = item.getItemDesc();
            }

            forward = "note";

            AuditLogRecordVendor auditLogRecord = new AuditLogRecordVendor();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            noteBean.setPageName("cancelvendor");
            String noteId = "";
            if (vendorDomainObj != null && vendorDomainObj.getId() != null && !vendorDomainObj.getId().equals("")) {
                noteId = vendorDomainObj.getId().toString();
                noteBean.setNoteId(noteId);
                noteBean.setReferenceId(noteId);
            }
            List auditList = null;
            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            noteBean.setVoidednote("");
            request.setAttribute("noteBean", noteBean);
        }

        return mapping.findForward(forward);

    }
}