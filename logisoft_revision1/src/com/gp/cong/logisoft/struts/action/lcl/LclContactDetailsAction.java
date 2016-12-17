/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.CustomerContact;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CustomerContactDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclContactDetailsForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Owner
 */
public class LclContactDetailsAction extends DispatchAction {

    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
    CustomerContactDAO customerContactDAO = new CustomerContactDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclContactDetailsForm lclContactDetailsForm = (LclContactDetailsForm) form;
        if (CommonUtils.isNotEmpty(lclContactDetailsForm.getVendorNo())) {
            request.setAttribute("contactList", customerContactDAO.findByAcctNo(lclContactDetailsForm.getVendorNo()));
        }
        request.setAttribute("lclContactDetailsForm", lclContactDetailsForm);
        if("true".equals(lclContactDetailsForm.getIsVoyageContact()) && lclContactDetailsForm.getIsVoyageContact()!=null){
            return mapping.findForward("lclExportVoyageContact");
        }else{
            return mapping.findForward("success");
        }
    }

    public ActionForward saveContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclContactDetailsForm lclContactDetailsForm = (LclContactDetailsForm) form;
        CustomerContact customerContact = null;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        if (CommonUtils.isNotEmpty(lclContactDetailsForm.getCustContactId())) {
            customerContact = customerContactDAO.findById(Integer.parseInt(lclContactDetailsForm.getCustContactId()));
        } else {
            customerContact = new CustomerContact();
            customerContact.setCreatedDate(new Date());
            customerContact.setEnteredDatetime(new Date());
        }
        customerContact.setFirstName(null != lclContactDetailsForm.getFirstName() ? lclContactDetailsForm.getFirstName().toUpperCase().trim() : lclContactDetailsForm.getFirstName());
        customerContact.setLastName(null != lclContactDetailsForm.getLastName() ? lclContactDetailsForm.getLastName().toUpperCase().trim() : lclContactDetailsForm.getLastName());
        customerContact.setPosition(null != lclContactDetailsForm.getPosition() ? lclContactDetailsForm.getPosition().toUpperCase() : lclContactDetailsForm.getPosition());
        customerContact.setPhone(null != lclContactDetailsForm.getPhone() ? lclContactDetailsForm.getPhone().toUpperCase() : lclContactDetailsForm.getPhone());
        customerContact.setFax(null != lclContactDetailsForm.getFax() ? lclContactDetailsForm.getFax().toUpperCase() : lclContactDetailsForm.getFax());
        customerContact.setEmail(null != lclContactDetailsForm.getEmail() ? lclContactDetailsForm.getEmail().toUpperCase() : lclContactDetailsForm.getEmail());
        customerContact.setComment(null != lclContactDetailsForm.getComment() ? lclContactDetailsForm.getComment().toUpperCase() : lclContactDetailsForm.getComment());
        customerContact.setExtension(null != lclContactDetailsForm.getExtension() ? lclContactDetailsForm.getExtension().toUpperCase() : lclContactDetailsForm.getExtension());
        customerContact.setAccountNo(lclContactDetailsForm.getVendorNo());
        //set Contact Details
        if (lclContactDetailsForm.getCodea() != null && !lclContactDetailsForm.getCodea().equals("")) {
            GenericCode codeA = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodea(), "A");
            customerContact.setCodea(codeA);
        }else{
            customerContact.setCodea(null);
        }
        if (lclContactDetailsForm.getCodeb() != null && !lclContactDetailsForm.getCodeb().equals("")) {
            GenericCode codeB = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodeb(), "B");
            customerContact.setCodeb(codeB);
        }else{
            customerContact.setCodeb(null);
        }
        if (lclContactDetailsForm.getCodec() != null && !lclContactDetailsForm.getCodec().equals("")) {
            GenericCode codeC = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodec(), "C");
            customerContact.setCodec(codeC);
        }else{
            customerContact.setCodec(null);
        }
        if (lclContactDetailsForm.getCoded() != null && !lclContactDetailsForm.getCoded().equals("")) {
            GenericCode codeD = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCoded(), "D");
            customerContact.setCoded(codeD);
        }else{
            customerContact.setCoded(null);
        }
        if (lclContactDetailsForm.getCodee() != null && !lclContactDetailsForm.getCodee().equals("")) {
            GenericCode codeE = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodee(), "E");
            customerContact.setCodee(codeE);
        }else{
            customerContact.setCodee(null);
        }
        if (lclContactDetailsForm.getCodef() != null && !lclContactDetailsForm.getCodef().equals("")) {
            GenericCode codeF = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodef(), "F");
            customerContact.setCodef(codeF);
        }else{
            customerContact.setCodef(null);
        }
        if (lclContactDetailsForm.getCodeg() != null && !lclContactDetailsForm.getCodeg().equals("")) {
            GenericCode codeG = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodeg(), "G");
            customerContact.setCodeg(codeG);
        }else{
            customerContact.setCodeg(null);
        }
        if (lclContactDetailsForm.getCodeh() != null && !lclContactDetailsForm.getCodeh().equals("")) {
            GenericCode codeH = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodeh(), "H");
            customerContact.setCodeh(codeH);
        }else{
            customerContact.setCodeh(null);
        }
        if (lclContactDetailsForm.getCodei() != null && !lclContactDetailsForm.getCodei().equals("")) {
            GenericCode codeI = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodei(), "I");
            customerContact.setCodei(codeI);
        }else{
            customerContact.setCodei(null);
        }
        if (lclContactDetailsForm.getCodej() != null && !lclContactDetailsForm.getCodej().equals("")) {
            GenericCode codeJ = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodej(), "J");
            customerContact.setCodej(codeJ);
        }else{
            customerContact.setCodej(null);
        }
        if (lclContactDetailsForm.getCodek() != null && !lclContactDetailsForm.getCodek().equals("")) {
            GenericCode codeK = genericCodeDAO.getSpecialCode(lclContactDetailsForm.getCodek(), "K");
            customerContact.setCodek(codeK);
        }else{
            customerContact.setCodek(null);
        }
        customerContact.setUpdateBy(loginUser.getLoginName());
        customerContact.setUpdatedDate(new Date());
        customerContactDAO.saveOrUpdate(customerContact);
        request.setAttribute("contactList", customerContactDAO.findByAcctNo(lclContactDetailsForm.getVendorNo()));
        if("true".equals(lclContactDetailsForm.getIsVoyageContact()) && lclContactDetailsForm.getIsVoyageContact()!=null){
            return mapping.findForward("lclExportVoyageContact");
        }else{
            return mapping.findForward("success");
        }
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclContactDetailsForm lclContactDetailsForm = (LclContactDetailsForm) form;
        request.setAttribute("contactList", customerContactDAO.findByAcctNo(lclContactDetailsForm.getVendorNo()));
       if("true".equals(lclContactDetailsForm.getIsVoyageContact()) && lclContactDetailsForm.getIsVoyageContact()!=null){
            return mapping.findForward("lclExportVoyageContact");
        }else{
            return mapping.findForward("success");
        }
    }

    public ActionForward deleteContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclContactDetailsForm lclContactDetailsForm = (LclContactDetailsForm) form;
        CustomerContact customerContact = customerContactDAO.findById(Integer.parseInt(lclContactDetailsForm.getCustContactId()));
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        customerContact.setUpdateBy(loginUser.getLoginName());
        customerContact.setUpdatedDate(new Date());
        customerContactDAO.saveOrUpdate(customerContact);
        customerContactDAO.getCurrentSession().flush();
        customerContactDAO.delete(customerContact);
        request.setAttribute("contactList", customerContactDAO.findByAcctNo(lclContactDetailsForm.getVendorNo()));
        lclContactDetailsForm.setCustContactId("");
        request.setAttribute("lclContactDetailsForm", lclContactDetailsForm);
         if("true".equals(lclContactDetailsForm.getIsVoyageContact()) && lclContactDetailsForm.getIsVoyageContact()!=null){
            return mapping.findForward("lclExportVoyageContact");
        }else{
            return mapping.findForward("success");
        }
    }

    public ActionForward codeDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclContactDetailsForm lclContactDetailsForm = (LclContactDetailsForm) form;
        request.setAttribute("genericCodeList", new GenericCodeDAO().getGenericCodeDesc("22", lclContactDetailsForm.getField1()));
        request.setAttribute("lclContactDetailsForm", lclContactDetailsForm);
        return mapping.findForward("code");
    }
}
