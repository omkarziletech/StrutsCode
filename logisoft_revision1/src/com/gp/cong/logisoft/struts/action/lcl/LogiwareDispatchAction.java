/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.struts.action.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.SessionConstant;
import com.gp.cong.hibernate.BaseHibernateDAO;
import com.gp.cong.hibernate.Domain;
import com.gp.cong.lcl.common.constant.ActionForwardConstant;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.RoleDuty;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.hibernate.dao.DocumentStoreLogDAO;
import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author lakshh
 */
public class LogiwareDispatchAction extends DispatchAction implements SessionConstant, ActionForwardConstant {

    protected static final String INPUT = "input";
    private static final String LOCK = "lock";
    protected BaseHibernateDAO baseHibernateDAO = new BaseHibernateDAO();

    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward(SUCCESS);
    }

    public void save(Domain instance) throws Exception {
        baseHibernateDAO.save(instance);
    }

    public void saveOrUpdate(Domain instance) throws Exception {
        baseHibernateDAO.saveOrUpdate(instance);
    }

    public void reattach(Domain domain) throws Exception {
        baseHibernateDAO.reattach(domain);
    }

    public User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("loginuser");
    }
    public RoleDuty checkRoleDuty(HttpServletRequest request) {
        return (RoleDuty) request.getSession().getAttribute("roleDuty");
    }
    
    @Override
    protected ActionForward dispatchMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String name) throws Exception {
        LogiwareActionForm logiwareActionForm = (LogiwareActionForm) form;
        DocumentStoreLogDAO documentStoreLogDAO = new DocumentStoreLogDAO();
        String moduleName = request.getParameter("moduleName");
        String callBackFlag = request.getParameter("callBackFlag");
        String scanScreenName = "";
        String bookingType = "";
        if (logiwareActionForm.getScreenName() != null && "LCL FILE".equalsIgnoreCase(logiwareActionForm.getScreenName())) {
            if (LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(moduleName)) {
                scanScreenName = LclCommonConstant.LCL_IMPORTS_SCREENNAME;
            } else {
                scanScreenName = LclCommonConstant.LCL_EXPORTS_SCREENNAME;
                bookingType = new LCLBookingDAO().getBookingTypeScanOrAttach(logiwareActionForm.getModuleId());
            }
        }
        if (CommonUtils.isNotEmpty(logiwareActionForm.getModuleId()) && CommonUtils.isNotEmpty(scanScreenName)) {
            request.setAttribute("scanAttachFlag", documentStoreLogDAO.isScanCountChecked(scanScreenName, logiwareActionForm.getModuleId()));
        } 
        if (CommonUtils.isNotEmpty(logiwareActionForm.getModuleId()) && CommonUtils.isNotEmpty(scanScreenName)
             && CommonUtils.isNotEmpty(bookingType) && null != bookingType && bookingType.equalsIgnoreCase("T") && CommonUtils.isEqual(LclCommonConstant.LCL_EXPORTS_SCREENNAME, scanScreenName)) {
            request.setAttribute("scanAttachFlag", documentStoreLogDAO.isScanCountChecked("LCL IMPORTS DR", logiwareActionForm.getModuleId()));
        }
        if ("editQuote".equals(request.getParameter("methodName")) || "editBooking".equals(request.getParameter("methodName"))) {
            String moduleId = new ProcessInfoBC().cheackFileNumberForLoack(request.getParameter("moduleId"), getCurrentUser(request).getUserId().toString(), request.getParameter("screenName"));
            if (CommonFunctions.isNotNull(moduleId)) {
                // setting request.
                request.setAttribute(LCL_LOCK_MESSAGE, "This Record is Used by " + moduleId);
            }
        }
        return super.dispatchMethod(mapping, form, request, response, name);
    }
}
