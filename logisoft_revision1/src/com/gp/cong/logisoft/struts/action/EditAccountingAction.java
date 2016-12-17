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
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.AuditLogRecordAccounting;
import com.gp.cong.logisoft.domain.Customer;
import com.gp.cong.logisoft.domain.CustomerAccounting;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.struts.form.EditAccountingForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 01-02-2008
 * 
 * XDoclet definition:
 * @struts.action path="/editAccounting" name="editAccountingForm" input="/jsps/Tradingpartnermaintainance/EditAccounting.jsp" scope="request" validate="true"
 */
public class EditAccountingAction extends Action {
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
        EditAccountingForm editAccountingForm = (EditAccountingForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = editAccountingForm.getButtonValue();
        String arPhone = editAccountingForm.getArPhone();
        String arFax = editAccountingForm.getArFax();
        String acctReceive = editAccountingForm.getAcctReceive();
        String statement = editAccountingForm.getStatements();
        String creditLimit = editAccountingForm.getCreditLimit();
        DBUtil dbUtil = new DBUtil();
        String creditRate = editAccountingForm.getCreditRate();
        String creditStatus = editAccountingForm.getCreditStatus();
        String forwardName = "";
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        UserDAO userDAO = new UserDAO();
        GenericCode creditRt = null;
        GenericCode creditSt = null;
        String holdList = editAccountingForm.getHoldList();
        String suspendCredit = editAccountingForm.getSuspendCredit();
        String legal = editAccountingForm.getLegal();
        String extendCredit = editAccountingForm.getExtendCredit();
        String arcode = editAccountingForm.getArCode();
        String zipCode = editAccountingForm.getZipCode();
        String companyMaster = editAccountingForm.getCompanyMaster();
        String addressMaster = editAccountingForm.getAddressMaster();
        String ffZip = editAccountingForm.getFfzipCode();
        String agent = editAccountingForm.getIncludeagent();
        String cbalance = editAccountingForm.getCreditbalance();
        String cinvoice = editAccountingForm.getCreditinvoice();
        String schedule_Stmt = editAccountingForm.getSchedulestmt();
        CustomerAccounting accounting = null;
        customerBean customerbean = new customerBean();
        if (session.getAttribute("accounting") != null) {
            accounting = (CustomerAccounting) session.getAttribute("accounting");
        } else {
            accounting = new CustomerAccounting();
        }
        if (buttonValue != null && !buttonValue.equals("note")) {
            customerbean.setStatements(statement);
            customerbean.setHoldList(holdList);
            customerbean.setSuspendCredit(suspendCredit);
            customerbean.setLegal(legal);
            customerbean.setExtendCredit(extendCredit);
            customerbean.setIncludeagent(agent);
            customerbean.setCreditbalance(cbalance);
            customerbean.setCreditinvoice(cinvoice);
            customerbean.setAddressMaster(addressMaster);
            customerbean.setCompanyMaster(companyMaster);

            session.setAttribute("customerbean", customerbean);//-----------CUSTOMER BEAN SESSION---------------


            if (creditRate != null && creditRate != "0") {
                creditRt = genericCodeDAO.findById(Integer.parseInt(creditRate));
                accounting.setCreditRate(creditRt);
            }
            if (creditStatus != null && creditStatus != "0") {
                creditSt = genericCodeDAO.findById(Integer.parseInt(creditStatus));
                accounting.setCreditStatus(creditSt);
            }
            GenericCode stmt = new GenericCode();
            if (statement != null && statement != "0") {
                stmt = genericCodeDAO.findById(Integer.parseInt(statement));
                accounting.setStatements(stmt);
            }
            User arContact = new User();
            if (arcode != null && arcode != "0") {
                arContact = userDAO.findById(Integer.parseInt(arcode));
                accounting.setArcode(arContact);
            }
            if (creditLimit != null && creditLimit != "") {
                accounting.setCreditLimit(Double.parseDouble(creditLimit));
            }
            if (addressMaster != null && addressMaster.equals("on")) {
                accounting.setAddressMaster("Y");
            } else {
                accounting.setAddressMaster("N");
            }
            if (companyMaster != null && companyMaster.equals("on")) {
                accounting.setCompanyMaster("Y");
            } else {
                accounting.setCompanyMaster("N");
            }
            if (extendCredit != null && extendCredit.equalsIgnoreCase("on")) {
                accounting.setExtendCredit("Y");
            } else {
                accounting.setExtendCredit("N");
            }
            if (holdList != null && holdList.equalsIgnoreCase("on")) {
                accounting.setHoldList("Y");
            } else {
                accounting.setHoldList("N");
            }

            if (suspendCredit != null && suspendCredit.equalsIgnoreCase("on")) {
                accounting.setSuspendCredit("Y");
            } else if (suspendCredit != null && suspendCredit.equalsIgnoreCase("off")) {
                accounting.setSuspendCredit("N");
            }

            if (legal != null && legal.equalsIgnoreCase("on")) {
                accounting.setLegal("Y");
            } else if (legal != null && legal.equalsIgnoreCase("off")) {
                accounting.setLegal("N");
            }
            if (agent != null && agent.equalsIgnoreCase("on")) {
                accounting.setIncludeagent("Y");
            } else {
                accounting.setIncludeagent("N");
            }
            if (cbalance != null && cbalance.equalsIgnoreCase("on")) {
                accounting.setCreditbalance("Y");
            } else {
                accounting.setCreditbalance("N");
            }
            if (cinvoice != null && cinvoice.equalsIgnoreCase("on")) {
                accounting.setCreditinvoice("Y");
            } else {
                accounting.setCreditinvoice("N");
            }
            accounting.setComment(acctReceive);
            accounting.setZip(zipCode);
            accounting.setPayZip(ffZip);
            accounting.setSchedulestmt(schedule_Stmt);
            session.setAttribute("accounting", accounting);
            forwardName = "editaccount";
        } else if (buttonValue != null && buttonValue.equals("note")) {
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";
            if (session.getAttribute("processinfoforcustomer") != null) {
                String itemId = (String) session.getAttribute("processinfoforcustomer");
                item = itemDAO.findById(Integer.parseInt(itemId));
                itemName = item.getItemDesc();
            }

            forwardName = "note";
            AuditLogRecord auditLogRecord = new AuditLogRecordAccounting();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            //noteBean.setUser(user);
            noteBean.setPageName("cancelaccounting");
            String noteId = "";
            if (accounting != null && accounting.getId() != null && !accounting.getId().equals("")) {
                noteId = accounting.getId().toString();
                noteBean.setNoteId(noteId);
                noteBean.setReferenceId(noteId);
            }

            List auditList = null;
            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            noteBean.setVoidednote("");
            request.setAttribute("noteBean", noteBean);

            request.setAttribute("customernotes", "customernotes");
            forwardName = "note";

        }
        if (buttonValue != null && buttonValue.equals("editchecked")) {
            CustomerDAO customerDAO = new CustomerDAO();
            List id = customerDAO.findId();
            Iterator it = id.iterator();
            List list = new ArrayList();
            while (it.hasNext()) {
                Customer customer = customerDAO.findById2(new Integer(it.next().toString()));
                session.setAttribute("addressMaster", customer);
                list.add(customer);
            }
            session.setAttribute("masteraddressList", list);
            request.setAttribute("openwindow", "openwindow");//----FOR ADDRESS POPUP PAGE-----
        }
        return mapping.findForward(forwardName);
    }
}