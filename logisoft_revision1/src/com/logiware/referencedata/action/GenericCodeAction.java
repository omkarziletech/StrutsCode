package com.logiware.referencedata.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericcodelabelsDAO;
import com.logiware.referencedata.form.GenericCodeForm;
import com.logiware.referencedata.form.SessionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class GenericCodeAction extends BaseAction {

    private static final String ADD = "add";
    private static final String EDIT = "edit";

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GenericCodeForm gcForm = (GenericCodeForm) form;
        SessionForm oldGenericCodeForm = new SessionForm();
        PropertyUtils.copyProperties(oldGenericCodeForm, gcForm);
        request.getSession().setAttribute("oldGenericCodeForm", oldGenericCodeForm);
        gcForm.setCodeType(new CodetypeDAO().findById(gcForm.getGenericCode().getCodetypeid()));
        gcForm.setGenericCodeLabels(new GenericcodelabelsDAO().getGenericCodeLabels(gcForm.getGenericCode().getCodetypeid()));
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        Integer totalRows = genericCodeDAO.getRowCount(gcForm.getGenericCode());
        if (totalRows > 0) {
            int limit = gcForm.getLimit();
            int start = limit * (gcForm.getSelectedPage() - 1);
            gcForm.setGenericCodes(genericCodeDAO.getGenericCodes(gcForm.getGenericCode(), gcForm.getSortBy(), gcForm.getOrderBy(), start, limit));
            gcForm.setSelectedRows(gcForm.getGenericCodes().size());
            int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
            gcForm.setTotalPages(totalPages);
            gcForm.setTotalRows(totalRows);
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward sortingAndPaging(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionForm oldGenericCodeForm = (SessionForm) request.getSession().getAttribute("oldGenericCodeForm");
        if(null != oldGenericCodeForm){
            GenericCodeForm gcForm = (GenericCodeForm) form;
            PropertyUtils.copyProperties(gcForm, oldGenericCodeForm);
            if(CommonUtils.isNotEmpty(request.getParameter("sortBy"))){
                gcForm.setSortBy(request.getParameter("sortBy"));
                gcForm.setOrderBy(request.getParameter("orderBy"));
                gcForm.setSelectedPage(Integer.parseInt(request.getParameter("selectedPage")));
                oldGenericCodeForm.setSortBy(request.getParameter("sortBy"));
                oldGenericCodeForm.setOrderBy(request.getParameter("orderBy"));
                oldGenericCodeForm.setSelectedPage(Integer.parseInt(request.getParameter("selectedPage")));
                request.getSession().setAttribute("oldGenericCodeForm", oldGenericCodeForm);
            }
            gcForm.setCodeType(new CodetypeDAO().findById(gcForm.getGenericCode().getCodetypeid()));
            gcForm.setGenericCodeLabels(new GenericcodelabelsDAO().getGenericCodeLabels(gcForm.getGenericCode().getCodetypeid()));
            GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
            Integer totalRows = genericCodeDAO.getRowCount(gcForm.getGenericCode());
            if (totalRows > 0) {
                int limit = gcForm.getLimit();
                int start = limit * (gcForm.getSelectedPage() - 1);
                gcForm.setGenericCodes(genericCodeDAO.getGenericCodes(gcForm.getGenericCode(), gcForm.getSortBy(), gcForm.getOrderBy(), start, limit));
                gcForm.setSelectedRows(gcForm.getGenericCodes().size());
                int totalPages = (totalRows / limit) + (totalRows % limit > 0 ? 1 : 0);
                gcForm.setTotalPages(totalPages);
                gcForm.setTotalRows(totalRows);
            }
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward clearAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("genericCodeForm", new GenericCodeForm());
        request.getSession().removeAttribute("oldGenericCodeForm");
        return mapping.findForward(SUCCESS);
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GenericCodeForm gcForm = (GenericCodeForm) form;
        gcForm.setGenericCodeLabels(new GenericcodelabelsDAO().getGenericCodeLabels(gcForm.getGenericCode().getCodetypeid()));
        gcForm.setCodeType(new CodetypeDAO().findById(gcForm.getGenericCode().getCodetypeid()));
        return mapping.findForward(ADD);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GenericCodeForm gcForm = (GenericCodeForm) form;
        gcForm.setGenericCodeLabels(new GenericcodelabelsDAO().getGenericCodeLabels(gcForm.getGenericCode().getCodetypeid()));
        gcForm.setGenericCode(new GenericCodeDAO().findById(gcForm.getGenericCode().getId()));
        gcForm.setCodeType(new CodetypeDAO().findById(gcForm.getGenericCode().getCodetypeid()));
        return mapping.findForward(EDIT);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GenericCodeForm gcForm = (GenericCodeForm) form;
        gcForm.setGenericCodeLabels(new GenericcodelabelsDAO().getGenericCodeLabels(gcForm.getGenericCode().getCodetypeid()));
        gcForm.setGenericCode(new GenericCodeDAO().findById(gcForm.getGenericCode().getId()));
        gcForm.setCodeType(new CodetypeDAO().findById(gcForm.getGenericCode().getCodetypeid()));
        return mapping.findForward(EDIT);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GenericCodeForm gcForm = (GenericCodeForm) form;
        new GenericCodeDAO().save(gcForm.getGenericCode());
        return sortingAndPaging(mapping, form, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GenericCodeForm gcForm = (GenericCodeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        GenericCodeDAO genericCodeDAO = new GenericCodeDAO();
        genericCodeDAO.delete(genericCodeDAO.findById(gcForm.getGenericCode().getId()));
        genericCodeDAO.getSession().flush();
        genericCodeDAO.getSession().clear();
        genericCodeDAO.updateGenericcodeDupDeleted(gcForm.getGenericCode().getId(), loginUser);
        return sortingAndPaging(mapping, form, request, response);
    }
}
