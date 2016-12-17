/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.lcl.comparator.ReplicateQuoteComparator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.gp.cvst.logisoft.struts.form.lcl.LclClientDetailsForm;
import java.util.Collections;
import java.util.List;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author Owner
 */
public class LclClientDetailsAction extends DispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclClientDetailsForm lclClientDetailsForm = (LclClientDetailsForm) form;
        request.setAttribute("lclClientDetailsForm", lclClientDetailsForm);
        request.setAttribute("bookingList", new LCLBookingDAO().getAllBookingsByClient(lclClientDetailsForm.getAccountNo()));
        return mapping.findForward("success");
    }

public ActionForward displayQuote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclClientDetailsForm lclClientDetailsForm = (LclClientDetailsForm) form;
        request.setAttribute("lclClientDetailsForm", lclClientDetailsForm);
        //List bList=new LCLBookingDAO().getAllBookingsByClient(lclClientDetailsForm.getAccountNo());
        List qList=new LCLQuoteDAO().getAllQuotesByClient(lclClientDetailsForm.getAccountNo());      
        //qList.addAll(bList);
       // Collections.sort(qList,new ReplicateQuoteComparator());
        request.setAttribute("quoteList",qList );
        return mapping.findForward("successQuote");
    }

    /*
    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclContactDetailsForm lclContactDetailsForm = (LclContactDetailsForm) form;
        String accountName = lclContactDetailsForm.getAccountName();
        String accountNo = lclContactDetailsForm.getAccountNo();
        String contactName = lclContactDetailsForm.getContactName();
        request.setAttribute("accountName",accountName);
        request.setAttribute("accountNo", accountNo);
        String str = "from CustomerContact WHERE accountNo like '" + accountNo + "%' and firstName like '" + contactName + "%' order by id desc";
        request.setAttribute("contactList", new LclContactDetailsDAO().executeQuery(str, 10));
        return mapping.findForward("success");
    }

    public ActionForward closeContact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclContactDetailsForm lclContactDetailsForm = (LclContactDetailsForm) form;
        CustomerContact customerContact = new LclContactDetailsDAO().findById(lclContactDetailsForm.getId());
        new LclContactDetailsDAO().delete(lclContactDetailsForm.getId());
        request.setAttribute("contactList", new LclContactDetailsDAO().executeQuery("from CustomerContact order by id desc", 10));
        return mapping.findForward("success");
    }*/
}
