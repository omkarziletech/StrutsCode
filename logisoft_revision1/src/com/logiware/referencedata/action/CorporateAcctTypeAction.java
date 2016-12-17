/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.referencedata.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CorporateAccountType;
import com.gp.cong.logisoft.hibernate.dao.CorporateAcctTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.logiware.referencedata.form.CorporateAcctTypeForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Wsware
 */
public class CorporateAcctTypeAction extends BaseAction {

    private CorporateAcctTypeDAO corporateAcctTypeDAO = new CorporateAcctTypeDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        CorporateAcctTypeForm corporateAcctTypeForm = (CorporateAcctTypeForm) form;
        setCorporateAcctTypeList(corporateAcctTypeForm);
        return mapping.findForward("success");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CorporateAcctTypeForm corporateAcctTypeForm = (CorporateAcctTypeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        CorporateAccountType corporateAccountType = new CorporateAccountType();
        corporateAccountType.setAcctTypeDescription(corporateAcctTypeForm.getAcctDescription().toUpperCase());
        corporateAccountType.setAcctTypeDisabled(corporateAcctTypeForm.isAcctDisabled());
        corporateAccountType.setUpdatedBy(loginUser.getLoginName());
        corporateAcctTypeDAO.save(corporateAccountType);
        setCorporateAcctTypeList(corporateAcctTypeForm);
        return mapping.findForward("success");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CorporateAcctTypeForm corporateAcctTypeForm = (CorporateAcctTypeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        CorporateAccountType corporateAccountType = corporateAcctTypeDAO.findById(corporateAcctTypeForm.getCorporateAcctId());
        corporateAccountType.setAcctTypeDescription(corporateAcctTypeForm.getAcctDescription().toUpperCase());
        corporateAccountType.setAcctTypeDisabled(corporateAcctTypeForm.isAcctDisabled());
        corporateAccountType.setUpdatedBy(loginUser.getLoginName());
        corporateAcctTypeDAO.update(corporateAccountType);
        setCorporateAcctTypeList(corporateAcctTypeForm);
        return mapping.findForward("success");
    }

    public void setCorporateAcctTypeList(CorporateAcctTypeForm corporateAcctTypeForm) {
        corporateAcctTypeForm.setCorporateAcctTypeList(corporateAcctTypeDAO.getCorporateAcctType(corporateAcctTypeForm.getSearchDescription()));

    }

    public ActionForward showNotes(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        CorporateAcctTypeForm corporateAcctTypeForm = (CorporateAcctTypeForm) form;
        if (CommonUtils.isNotEmpty(corporateAcctTypeForm.getCorporateAcctId())) {
            NotesDAO notesDAO = new NotesDAO();
            request.setAttribute("notesList", notesDAO.findNotes("CORPORATE_ACCOUNT_TYPE", corporateAcctTypeForm.getCorporateAcctId().toString()));
        }
        return mapping.findForward("notes");
    }
}
