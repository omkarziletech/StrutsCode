/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.logisoft.beans.NoteBean;
import com.gp.cong.logisoft.domain.AuditLogRecord;
import com.gp.cong.logisoft.domain.AuditLogRecordContactConfig;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.struts.form.AddConfigForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 12-26-2007
 * 
 * XDoclet definition:
 * @struts.action path="/addConfig" name="addConfigForm" input="/jsps/Tradingpartnermaintainance/AddConfig.jsp" scope="request" validate="true"
 */
public class AddConfigAction extends Action {
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
        AddConfigForm addConfigForm = (AddConfigForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = addConfigForm.getButtonValue();
        String firstName = addConfigForm.getFirstName();
        String lastName = addConfigForm.getLastName();
        String position = addConfigForm.getPosition();
        String phone = addConfigForm.getPhone();
        String extension = addConfigForm.getExtension();
        String fax = addConfigForm.getFax();
        String email = addConfigForm.getEmail();
        String comment = addConfigForm.getComment();
        String codea = addConfigForm.getCodea();
        String codeb = addConfigForm.getCodeb();
        String codec = addConfigForm.getCodec();
        String coded = addConfigForm.getCoded();
        String codee = addConfigForm.getCodee();
        String codef = addConfigForm.getCodef();
        String codeg = addConfigForm.getCodeg();
        String codeh = addConfigForm.getCodeh();
        String codei = addConfigForm.getCodei();
        CustomerContact custCont = null;
        GenericCode codeA = new GenericCode();
        GenericCode codeB = new GenericCode();
        GenericCode codeC = new GenericCode();
        GenericCode codeD = new GenericCode();
        GenericCode codeE = new GenericCode();
        GenericCode codeF = new GenericCode();
        GenericCode codeG = new GenericCode();
        GenericCode codeH = new GenericCode();
        GenericCode codeI = new GenericCode();
        List addConfig = null;
        DBUtil dbUtil = new DBUtil();
        String message = "";
        String forwardName = "";
        String phone1 = "";
        String fax1 = "";
        int index = 0;
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        if (buttonValue.equals(CommonConstants.ADD_ACTION)) {
            if (session.getAttribute("custConfig") != null) {
                custCont = (CustomerContact) session.getAttribute("custConfig");
            } else {
                custCont = new CustomerContact();
            }
        }
        if (buttonValue.equals("add")) {
            phone1 = dbUtil.stringtokenizer(phone);
            fax1 = dbUtil.stringtokenizer(fax);
            custCont.setFirstName(firstName);
            custCont.setLastName(lastName);
            custCont.setPosition(position);
            custCont.setPhone(phone1);
            custCont.setExtension(extension);
            custCont.setFax(fax1);
            custCont.setEmail(email);
            custCont.setComment(comment);
            if (codea != null && !codea.equals("0")) {
                codeA = genericCodeDAO.findById(Integer.parseInt(codea));
                custCont.setCodea(codeA);
            }
            if (codeb != null && !codeb.equals("0")) {
                codeB = genericCodeDAO.findById(Integer.parseInt(codeb));
                custCont.setCodeb(codeB);
            }
            if (codec != null && !codec.equals("0")) {
                codeC = genericCodeDAO.findById(Integer.parseInt(codec));
                custCont.setCodec(codeC);
            }
            if (coded != null && !coded.equals("0")) {
                codeD = genericCodeDAO.findById(Integer.parseInt(coded));
                custCont.setCoded(codeD);
            }
            if (codee != null && !codee.equals("0")) {
                codeE = genericCodeDAO.findById(Integer.parseInt(codee));
                custCont.setCodee(codeE);
            }
            if (codef != null && !codef.equals("0")) {
                codeF = genericCodeDAO.findById(Integer.parseInt(codef));
                custCont.setCodef(codeF);
            }
            if (codeg != null && !codeg.equals("0")) {
                codeG = genericCodeDAO.findById(Integer.parseInt(codeg));
                custCont.setCodeg(codeG);
            }
            if (codeh != null && !codeh.equals("0")) {
                codeH = genericCodeDAO.findById(Integer.parseInt(codeh));
                custCont.setCodeh(codeH);
            }
            if (codei != null && !codei.equals("0")) {
                codeI = genericCodeDAO.findById(Integer.parseInt(codei));
                custCont.setCodei(codeI);
            }
            if (session.getAttribute("addConfig") != null) {
                addConfig = (List) session.getAttribute("addConfig");
                for (int i = 0; i < addConfig.size(); i++) {
                    CustomerContact custTemp = (CustomerContact) addConfig.get(i);
                    if (custTemp.getIndex() != null) {
                        if (custTemp.getIndex() > index) {
                            index = custTemp.getIndex();
                        }
                    } else {
                        index = addConfig.size() - 1;
                    }
                }
                index++;
            } else {
                addConfig = new ArrayList();
                index++;
            }
            custCont.setIndex(index);
            addConfig.add(custCont);

            message = "Contact Details Saved Successfully";
            session.setAttribute("addConfig", addConfig);
            request.setAttribute("message", message);
            forwardName = "contactconfig";
        }
        if (buttonValue.equals("edit")) {
            if (session.getAttribute("custConfig") != null) {
                custCont = (CustomerContact) session.getAttribute("custConfig");
            }
            phone1 = dbUtil.stringtokenizer(phone);
            fax1 = dbUtil.stringtokenizer(fax);
            custCont.setFirstName(firstName);
            custCont.setLastName(lastName);
            custCont.setPosition(position);
            custCont.setPhone(phone1);
            custCont.setExtension(extension);
            custCont.setFax(fax1);
            custCont.setEmail(email);
            custCont.setComment(comment);
            CustomerContactDAO customerContactDAO = new CustomerContactDAO();
            if (codea != null && codea != "0") {
                codeA = genericCodeDAO.findById(Integer.parseInt(codea));
                custCont.setCodea(codeA);
            }
            if (codeb != null && codeb != "0") {
                codeB = genericCodeDAO.findById(Integer.parseInt(codeb));
                custCont.setCodeb(codeB);
            }
            if (codec != null && !codec.equals("0")) {
                codeC = genericCodeDAO.findById(Integer.parseInt(codec));
                custCont.setCodec(codeC);
            }
            if (coded != null && !coded.equals("0")) {
                codeD = genericCodeDAO.findById(Integer.parseInt(coded));
                custCont.setCoded(codeD);
            }
            if (codee != null && !codee.equals("0")) {
                codeE = genericCodeDAO.findById(Integer.parseInt(codee));
                custCont.setCodee(codeE);
            }
            if (codef != null && !codef.equals("0")) {
                codeF = genericCodeDAO.findById(Integer.parseInt(codef));
                custCont.setCodef(codeF);
            }
            if (codeg != null && !codeg.equals("0")) {
                codeG = genericCodeDAO.findById(Integer.parseInt(codeg));
                custCont.setCodeg(codeG);
            }
            if (codeh != null && !codeh.equals("0")) {
                codeH = genericCodeDAO.findById(Integer.parseInt(codeh));
                custCont.setCodeh(codeH);
            }
            if (codei != null && !codei.equals("0")) {
                codeI = genericCodeDAO.findById(Integer.parseInt(codei));
                custCont.setCodei(codeI);
            }
            User userId = null;
            if (session.getAttribute("loginuser") != null) {
                userId = (User) session.getAttribute("loginuser");
            }
            addConfig = (List) session.getAttribute("addConfig");
            for (int i = 0; i < addConfig.size(); i++) {
                CustomerContact custContact = (CustomerContact) addConfig.get(i);
                if (custContact.getIndex() == custCont.getIndex()) {
                    custCont.setIndex(custContact.getIndex());
                    addConfig.remove(custContact);
                }
            }

            addConfig.add(custCont);
            //addConfig.add(custCont);
            message = "Contact Details Saved Successfully";
            session.setAttribute("addConfig", addConfig);
            request.setAttribute("message", message);
            forwardName = "contactconfig";
        } else if (buttonValue.equals("deleteconfig")) {
            addConfig = (List) session.getAttribute("addConfig");
            if (session.getAttribute("custConfig") != null) {
                custCont = (CustomerContact) session.getAttribute("custConfig");
                for (int i = 0; i < addConfig.size(); i++) {
                    CustomerContact custContact = (CustomerContact) addConfig.get(i);
                    if (custContact.getIndex() == custCont.getIndex()) {
                        addConfig.remove(custContact);
                    }
                    session.setAttribute("addConfig", addConfig);
                }
            }

            forwardName = "contactconfig";
        } else if (buttonValue.equals("cancel")) {
            forwardName = "contactconfig";
        } else if (buttonValue.equals("note")) {
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item();
            String itemName = "";
            if (session.getAttribute("processinfoforcustomer") != null) {
                String itemId = (String) session.getAttribute("processinfoforcustomer");
                item = itemDAO.findById(Integer.parseInt(itemId));
                itemName = item.getItemDesc();
            }


            AuditLogRecord auditLogRecord = new AuditLogRecordContactConfig();
            NoteBean noteBean = new NoteBean();
            noteBean.setItemName(itemName);
            noteBean.setAuditLogRecord(auditLogRecord);
            noteBean.setButtonValue(buttonValue);
            noteBean.setPageName("cancelconfig");
            String noteId = "";
            if (custCont != null && custCont.getId() != null) {
                noteId = custCont.getId().toString();
            }
            noteBean.setNoteId(noteId);
            noteBean.setReferenceId(noteId);
            List auditList = null;
            auditList = dbUtil.getNoteInformation(noteId, auditLogRecord);
            noteBean.setAuditList(auditList);
            noteBean.setVoidednote("");
            session.setAttribute("noteBean", noteBean);
            forwardName = "note";

        }
        request.setAttribute("buttonValue", buttonValue);
        return mapping.findForward(forwardName);
    }
}