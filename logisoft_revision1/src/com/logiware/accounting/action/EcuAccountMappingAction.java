package com.logiware.accounting.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.User;
import com.logiware.accounting.dao.EcuAccountMappingDAO;
import com.logiware.accounting.domain.EcuAccountMapping;
import com.logiware.accounting.form.EcuAccountMappingForm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class EcuAccountMappingAction extends BaseAction {

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession().getAttribute("loginuser");
        EcuAccountMappingForm ecuAccountMappingForm = (EcuAccountMappingForm) form;
        EcuAccountMapping ecuAccountMappingNew = ecuAccountMappingForm.getEcuAccountMapping();
        EcuAccountMappingDAO ecuAccountMappingDAO = new EcuAccountMappingDAO();
        if (CommonUtils.isEmpty(ecuAccountMappingForm.getEcuAccountMapping().getId())) {
            ecuAccountMappingNew.setCreatedOn(new Date());
            ecuAccountMappingNew.setCreatedBy(user.getLoginName());
            ecuAccountMappingNew.setUpdatedOn(new Date());
            ecuAccountMappingNew.setUpdatedBy(user.getLoginName());
            ecuAccountMappingDAO.save(ecuAccountMappingNew);
            request.setAttribute("message", "Report category - " + ecuAccountMappingNew.getReportCategory() + " is added successfully.");
        } else {
            EcuAccountMapping ecuAccountMappingOld = ecuAccountMappingDAO.findById(ecuAccountMappingNew.getId());
            ecuAccountMappingOld.setReportCategory(ecuAccountMappingNew.getReportCategory());
            ecuAccountMappingOld.setAccountType(ecuAccountMappingNew.getAccountType());
            ecuAccountMappingOld.setUpdatedOn(new Date());
            ecuAccountMappingOld.setUpdatedBy(user.getLoginName());
            new EcuAccountMappingDAO().update(ecuAccountMappingOld);
            request.setAttribute("message", "Report category - " + ecuAccountMappingNew.getReportCategory() + " is updated successfully.");
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EcuAccountMappingForm ecuAccountMappingForm = (EcuAccountMappingForm) form;
        EcuAccountMapping ecuAccountMapping = ecuAccountMappingForm.getEcuAccountMapping();
        EcuAccountMappingDAO ecuAccountMappingDAO = new EcuAccountMappingDAO();
        if (ecuAccountMappingDAO.verifyNoGLAccountMapped(ecuAccountMapping.getReportCategory())) {
            request.setAttribute("error", "One or more GL Accounts are mapped with report category - " + ecuAccountMapping.getReportCategory() + ", cannot delete it.");
        } else {
            ecuAccountMappingDAO.delete(ecuAccountMapping);
            request.setAttribute("message", "Report category - " + ecuAccountMapping.getReportCategory() + " is deleted successfully.");
        }
        return mapping.findForward(SUCCESS);
    }
}
