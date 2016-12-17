 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.logisoft.domain.lcl.TemplateOrder;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LclTemplateForm;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author lakshh
 */
public class LclTemplateAction extends LogiwareDispatchAction {

    private static final String DISPLAY = "display";

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTemplateForm lclTemplateForm = (LclTemplateForm) form;
        request.setAttribute("lclTemplateForm", lclTemplateForm);
        return mapping.findForward(DISPLAY);
    }

    public ActionForward saveTemplate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTemplateForm lclTemplateForm = (LclTemplateForm) form;
        LclSearchTemplate lclSearchTemplate = lclTemplateForm.getLclSearchTemplate();
        if (CommonUtils.isNotEmpty(lclTemplateForm.getTemplateId())) {
            lclSearchTemplate.setId(Integer.parseInt(lclTemplateForm.getTemplateId()));
            new LclSearchTemplateDAO().deleteTemplateOrderedList(Integer.parseInt(lclTemplateForm.getTemplateId()));
        }
        new LclSearchTemplateDAO().saveOrUpdate(lclSearchTemplate);
        int count = 0;
        for (String str : lclTemplateForm.getOrderedData().split(",")) {
            TemplateOrder template = new TemplateOrder();
            template.setLclSearchTemplate(lclSearchTemplate);
            template.setTemplateKey(str);
            template.setSortedOrder(count++);
            new BaseHibernateDAO<TemplateOrder>().saveOrUpdate(template);
        }
        request.setAttribute("lclSearchTemplate", lclSearchTemplate);
        return mapping.findForward(DISPLAY);
    }

    public ActionForward showTemplate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTemplateForm lclTemplateForm = (LclTemplateForm) form;
        if (CommonUtils.isNotEmpty(lclTemplateForm.getTemplateId())) {
            LclSearchTemplate lclSearchTemplate = new LclSearchTemplateDAO().findById(Integer.parseInt(lclTemplateForm.getTemplateId()));
            request.setAttribute("lclSearchTemplate", lclSearchTemplate);
            List<TemplateOrder> orderedTemplateList = new LclSearchTemplateDAO()
                    .getTemplateOrderedList(lclSearchTemplate.getId());
            request.setAttribute("orderedTemplateList", orderedTemplateList);
        }
        return mapping.findForward(DISPLAY);
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTemplateForm lclTemplateForm = (LclTemplateForm) form;
        lclTemplateForm.reset(mapping, request);
        request.setAttribute("templateList", new LclSearchTemplateDAO().getAllTemplate());
        return mapping.findForward(DISPLAY);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclTemplateForm lclTemplateForm = (LclTemplateForm) form;
        LclSearchTemplate lclSearchTemplate = lclTemplateForm.getLclSearchTemplate();
        new LclSearchTemplateDAO().delete(lclSearchTemplate);
        request.setAttribute("templateList", new LclSearchTemplateDAO().getAllTemplate());
        return mapping.findForward(DISPLAY);
    }
}
