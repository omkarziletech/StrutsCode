package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.hibernate.FclBlContainerDtls;
import com.gp.cong.hibernate.FclBlNew;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.logiware.form.FclBlContainerForm;
import com.logiware.hibernate.dao.FclBlContainerDAO;
import com.logiware.hibernate.dao.FclDAO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class FclBlContainerAction extends LogiwareEventAction {
    
    public ActionForward editContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlContainerForm fclBlContainerForm = (FclBlContainerForm) form;
        if(CommonUtils.isNotEmpty(request.getParameter("id")) && CommonUtils.isNotEmpty(fclBlContainerForm.getBol())){
            FclBlNew fclBl = new FclDAO().findById(Integer.parseInt(fclBlContainerForm.getBol()));
            FclBlContainerDtls fclBlContainerDtls = new FclBlContainerDAO().findById(Integer.parseInt(request.getParameter("id")));
            fclBlContainerForm.setFclBlContainerDtls(fclBlContainerDtls);
            if(CommonUtils.isEmpty(fclBlContainerDtls.getMarksNo()) && null == fclBlContainerDtls.getLastUpdate()){
                String destination = fclBl.getPort();
                if (null != destination && !destination.equals("")) {
                    String unlocCode = destination.substring(destination.lastIndexOf("(") + 1, destination.length() - 1);
                    if (unlocCode.startsWith("CO")) {
                        fclBlContainerDtls.setMarksNo("FCL/FCL");
                    }
                }
            }
            request.setAttribute("fclBlContainerForm", fclBlContainerForm);
        }
        return mapping.findForward("success");
    }
    public ActionForward updateContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlContainerForm fclBlContainerForm = (FclBlContainerForm) form;
        HttpSession session = request.getSession(true);
        User user =(User)session.getAttribute("loginuser");
        FclBlContainerDtls fclBlContainerDtls = new FclBlContainerDAO().findById(fclBlContainerForm.getFclBlContainerDtls().getTrailerNoId());
        fclBlContainerForm.getFclBlContainerDtls().setFclBl(new FclDAO().findById(Integer.parseInt(fclBlContainerForm.getBol())));
        fclBlContainerDtls.setLastUpdate(new Date());
        fclBlContainerDtls.setUsername(user.getLoginName());
        fclBlContainerDtls.setSealNo(fclBlContainerForm.getFclBlContainerDtls().getSealNo());
        fclBlContainerDtls.setSizeLegend(fclBlContainerForm.getFclBlContainerDtls().getSizeLegend());
        fclBlContainerDtls.setMarksNo(fclBlContainerForm.getFclBlContainerDtls().getMarksNo());
        if(fclBlContainerDtls.getTrailerNo() != null && !(fclBlContainerForm.getFclBlContainerDtls().getTrailerNo()).equals(fclBlContainerDtls.getTrailerNo()) 
               && "processed".equalsIgnoreCase(fclBlContainerDtls.getProcessStatus())){
             fclBlContainerDtls.setTrailerNoOld(fclBlContainerDtls.getTrailerNo());
             fclBlContainerDtls.setProcessStatus("");
        }
        fclBlContainerDtls.setTrailerNo(fclBlContainerForm.getFclBlContainerDtls().getTrailerNo());
        fclBlContainerForm.setFclBlContainerDtls(fclBlContainerDtls);
        new FclBlContainerDAO().saveOrUpdate(fclBlContainerForm.getFclBlContainerDtls());
        request.setAttribute("closeFclContainer", fclBlContainerForm);
        return mapping.findForward("success");
    }
    public ActionForward saveContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlContainerForm fclBlContainerForm = (FclBlContainerForm) form;
        HttpSession session = request.getSession(true);
        User user =(User)session.getAttribute("loginuser");
        fclBlContainerForm.getFclBlContainerDtls().setFclBl(new FclDAO().findById(Integer.parseInt(fclBlContainerForm.getBol())));
        fclBlContainerForm.getFclBlContainerDtls().setLastUpdate(new Date());
        fclBlContainerForm.getFclBlContainerDtls().setUsername(user.getLoginName());
        new FclBlContainerDAO().save(fclBlContainerForm.getFclBlContainerDtls());
        request.setAttribute("closeFclContainer", fclBlContainerForm);
        return mapping.findForward("success");
    }
    public ActionForward addContainer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FclBlContainerForm fclBlContainerForm = (FclBlContainerForm) form;
        if("Y".equalsIgnoreCase(fclBlContainerForm.getBreakBulk())){
             fclBlContainerForm.getFclBlContainerDtls().setTrailerNo("BBLK-999999-9");
             fclBlContainerForm.setSizeLegend( new GenericCodeDAO().getFieldByCodeAndCodetypeId("Unit Sizes","Z","id"));
             request.setAttribute("fclBlContainerForm", fclBlContainerForm);
         }
        return mapping.findForward("success");
    }
}
