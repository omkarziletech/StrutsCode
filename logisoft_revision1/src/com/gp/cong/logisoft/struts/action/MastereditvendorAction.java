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
import com.gp.cong.logisoft.struts.form.MastereditvendorForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 09-23-2008
 * 
 * XDoclet definition:
 * @struts.action path="/mastereditvendor" name="mastereditvendorForm" input="/jsps/Tradingpartnermaintainance/masterEditvendor.jsp" scope="request" validate="true"
 */
public class MastereditvendorAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        MastereditvendorForm mastereditvendorForm = (MastereditvendorForm) form;// TODO Auto-generated method stub

        HttpSession session = ((HttpServletRequest) request).getSession();
        Vendor masterEditvendorObj = new Vendor();
        customerBean customerbean = new customerBean();
        String E_legalname = mastereditvendorForm.getMlegalname();
        String E_dba = mastereditvendorForm.getMdba();
        String E_tin = mastereditvendorForm.getMtin();
        String E_wfile = mastereditvendorForm.getWfile();
        String E_apname = mastereditvendorForm.getMapname();
        String E_phoneno = mastereditvendorForm.getMphone();
        String E_fax = mastereditvendorForm.getMfax();
        String E_email = mastereditvendorForm.getMemail();
        String E_web = mastereditvendorForm.getMweb();
        String E_ecomanager = mastereditvendorForm.getMeamanager();
        String E_creditstatus = mastereditvendorForm.getCredit();
        String E_crlimit = mastereditvendorForm.getMclimit();
        String E_crterms = mastereditvendorForm.getMcterms();
        String E_bankacct = mastereditvendorForm.getMbaccount();
        String E_deact = mastereditvendorForm.getDeactivate();
        String buttonValue = mastereditvendorForm.getButtonValue();
        DBUtil dbUtil = new DBUtil();
        String forward = "";

        if (session.getAttribute("MasterVendorInfoList") != null) {
            masterEditvendorObj = (Vendor) session.getAttribute("MasterVendorInfoList");
        } else {
            masterEditvendorObj = new Vendor();
        }

        masterEditvendorObj.setLegalname(E_legalname);
        masterEditvendorObj.setDba(E_dba);
        masterEditvendorObj.setTin(E_tin);
        masterEditvendorObj.setApname(E_apname);
        masterEditvendorObj.setPhone(E_phoneno);
        masterEditvendorObj.setFax(E_fax);
        masterEditvendorObj.setEmail(E_email);
        masterEditvendorObj.setWeb(E_web);
        masterEditvendorObj.setBaccount(E_bankacct);
        customerbean.setWfile(E_wfile);//------------CUSTOMER BEAN SETS FOR CHECKBOXES-------------
        customerbean.setCredit(E_creditstatus);
        customerbean.setDeactivate(E_deact);
        session.setAttribute("customerbean", customerbean);


        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        UserDAO userDAO = new UserDAO();
        GenericCode creditRt = null;
        User arContact = new User();
        if (E_ecomanager != null && E_ecomanager != "0") {
            arContact = userDAO.findById(Integer.parseInt(E_ecomanager));
        //masterEditvendorObj.setEamanager(arContact);
        }

        if (E_crlimit != null && E_crlimit != "") {
            Double creditLmt = Double.parseDouble(E_crlimit);
            masterEditvendorObj.setClimit(creditLmt);
        }
        if (E_crterms != null && E_crterms != "0") {
            creditRt = genericCodeDAO.findById(Integer.parseInt(E_crterms));
            masterEditvendorObj.setCterms(creditRt);
        }

        if (E_wfile != null) {
            masterEditvendorObj.setWfile("on");
        } else {
            masterEditvendorObj.setWfile("off");
        }

        if (E_creditstatus != null) {
            masterEditvendorObj.setCredit("on");
        } else {
            masterEditvendorObj.setCredit("off");
        }

        if (E_deact != null) {
            masterEditvendorObj.setDeactivate("on");
        } else {
            masterEditvendorObj.setDeactivate("off");
        }

        session.setAttribute("MasterVendorInfoList", masterEditvendorObj);
        forward = "toeditmaster";
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
            //noteBean.setUser(user);
            noteBean.setPageName("cancelmastervendor");
            String noteId = "";
            if (masterEditvendorObj != null && masterEditvendorObj.getId() != null && !masterEditvendorObj.getId().equals("")) {
                noteId = masterEditvendorObj.getId().toString();
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