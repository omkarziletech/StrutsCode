/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.beans.BatchesBean;
import com.gp.cvst.logisoft.domain.Batch;
import com.gp.cvst.logisoft.hibernate.dao.BatchDAO;
import com.gp.cvst.logisoft.struts.form.BatchCopyForm;

/** 
 * MyEclipse Struts
 * Creation date: 04-01-2008
 * 
 * XDoclet definition:
 * @struts.action path="/batchCopy" name="batchCopyForm" input="/jsps/Accounting/BatchCopy.jsp" scope="request" validate="true"
 */
public class BatchCopyAction extends Action {
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
        BatchCopyForm batchCopyForm = (BatchCopyForm) form;// TODO Auto-generated method stub
        HttpSession session = ((HttpServletRequest) request).getSession();
        String buttonValue = batchCopyForm.getButtonValue();
        String copy = batchCopyForm.getCopy();
        Integer batchno = 0;
        BatchesBean batchesBean = new BatchesBean();
        batchesBean.setCopy(copy);
        if (batchCopyForm.getBatchno() != null && !batchCopyForm.getBatchno().equals("")) {
            batchno = Integer.parseInt(batchCopyForm.getBatchno());
        }
        String desc = batchCopyForm.getDesc();
        BatchDAO batchDAO = new BatchDAO();
        if (request.getParameter("index") != null) {
            int ind = Integer.parseInt(request.getParameter("index"));
            List batchList1 = (List) session.getAttribute("copyList");
            Batch bBean = new Batch();
            bBean = (Batch) batchList1.get(ind);
            session.setAttribute("batch", bBean);
            request.setAttribute("buttonValue", "completed");
        } else {
            if (buttonValue.equals("search")) {
                List batchList = batchDAO.findBatch(batchCopyForm.getBatchno(), desc);
                session.setAttribute("copyList", batchList);
            }
        }
        request.setAttribute("batchesBean", batchesBean);
        return mapping.findForward("searchbatch");
    }
}
