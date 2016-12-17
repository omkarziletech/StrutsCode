/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.referencedata.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.CorporateAccount;
import com.gp.cong.logisoft.hibernate.dao.CorporateAccountDAO;
import com.gp.cong.logisoft.hibernate.dao.CorporateAcctTypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.logiware.referencedata.form.CorporateAccountForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mei
 */
public class CorporateAccountAction extends BaseAction {

    private CorporateAccountDAO corporateAccountDAO = new CorporateAccountDAO();

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CorporateAccountForm corporateAccountForm = (CorporateAccountForm) form;
        getCorporateAcctList(corporateAccountForm);
        return mapping.findForward("success");
    }

    public ActionForward addOrEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CorporateAccountForm corporateAccountForm = (CorporateAccountForm) form;
        if (CommonUtils.isNotEmpty(corporateAccountForm.getCorporateAcctId())) {
            CorporateAccount corporateAccount = corporateAccountDAO.findById(corporateAccountForm.getCorporateAcctId());
            corporateAccountForm.setAcctName(corporateAccount.getCorporateName());
            if (CommonUtils.isNotEmpty(corporateAccount.getEliteCommodityCode())) {
                corporateAccountForm.setCommCode(corporateAccount.getEliteCommodityCode());
                Integer commcode = new CodetypeDAO().getCodeTypeId("Commodity Codes");
                GenericCode code = new GenericCodeDAO().findByCodeName(corporateAccount.getEliteCommodityCode(), commcode);
                corporateAccountForm.setSearchCommName(code.getCodedesc());
            }
            corporateAccountForm.setCorporateAcctType(corporateAccount.getCorporateAccountType().getId().toString());
        }
        return mapping.findForward("add");
    }

    public ActionForward saveOrUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CorporateAccountForm corporateAccountForm = (CorporateAccountForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        CorporateAccount corporateAccount = null;
        if (CommonUtils.isNotEmpty(corporateAccountForm.getCorporateAcctId())) {
            corporateAccount = corporateAccountDAO.findById(corporateAccountForm.getCorporateAcctId());
        } else {
            corporateAccount = new CorporateAccount();
        }
        corporateAccount.setCorporateAccountType(new CorporateAcctTypeDAO().findById(Long.parseLong(corporateAccountForm.getCorporateAcctType())));
        corporateAccount.setCorporateName(corporateAccountForm.getAcctName().toUpperCase());
        corporateAccount.setEliteCommodityCode(CommonUtils.isNotEmpty(corporateAccountForm.getCommCode()) ? corporateAccountForm.getCommCode() : null);
        corporateAccount.setUpdatedBy(loginUser.getLoginName());
        corporateAccountDAO.saveOrUpdate(corporateAccount);
        getCorporateAcctList(corporateAccountForm);
        return mapping.findForward("result");
    }

    public void getCorporateAcctList(CorporateAccountForm corporateAccountForm) throws Exception {
        corporateAccountForm.setCorporateAcctList(corporateAccountDAO.getCorporateAcctType(corporateAccountForm.getSearchAcctName(), corporateAccountForm.getSearchCommCode()));
    }

    public ActionForward showNotes(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        CorporateAccountForm corporateAccountForm = (CorporateAccountForm) form;
        if (CommonUtils.isNotEmpty(corporateAccountForm.getCorporateAcctId())) {
            NotesDAO notesDAO = new NotesDAO();
            request.setAttribute("notesList", notesDAO.findNotes("CORPORATE_ACCOUNT", corporateAccountForm.getCorporateAcctId().toString()));
        }
        return mapping.findForward("notes");
    }
}
