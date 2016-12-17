/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cvst.logisoft.struts.form.lcl.LclExportBlueToLogiForm;
//import com.gp.cong.logisoft.bc.referenceDataManagement.*;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Admin
 */
public class LclExportBlueToLogiAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception, SQLException {
        String load = "";
        String delete = "";
        request.setAttribute("loadMsg", load);
        request.setAttribute("deleteMsg", delete);
        return mapping.findForward("display");
    }

    public ActionForward deleteLogiRecord(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception, SQLException {
        LclExportBlueToLogiForm logiForm = (LclExportBlueToLogiForm) form;
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        lclFileNumberDAO.deleteBlueToLogi(logiForm.getDestId());
        request.setAttribute("deleteMsg", "Deleted succesfully");
        request.setAttribute("loadMsg", "");
        return mapping.findForward("display");
    }

    private void delete(Integer destId) throws Exception{
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        lclFileNumberDAO.deleteBlueToLogi(destId);
    }

    public ActionForward uploadLogiRecord(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception, SQLException {
        LclExportBlueToLogiForm logiForm = (LclExportBlueToLogiForm) form;
        LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
        delete(logiForm.getDestId());
        lclFileNumberDAO.uploadBlueToLogi(logiForm.getDestId());
        request.setAttribute("loadMsg", "Loaded succesfully");
        request.setAttribute("deleteMsg", "");
        return mapping.findForward("display");
    }
}
