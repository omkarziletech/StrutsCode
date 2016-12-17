package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.beans.EdiBean;
import com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO;
import com.gp.cong.logisoft.struts.form.EdiTrackingForm;
import com.gp.cong.logisoft.util.EdiUtil;

public class EdiTrackingAction extends Action{

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
    		EdiTrackingForm ediTrackingForm = (EdiTrackingForm) form;
    		EdiBean ediBean = new EdiBean();
    		LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
    		List ediFileList = new ArrayList();
    		List ediAckList = new ArrayList();
    		EdiUtil ediUtil =new EdiUtil();
    		ediBean.setDrNumberTxt(ediTrackingForm.getDrNumber());
    		ediBean.setEdiCompanyTxt(ediTrackingForm.getEdiCompany());
    		ediBean.setMessageTypeTxt(ediTrackingForm.getMessageType());
    		String buttonValue=ediTrackingForm.getButtonValue();
    		String drNumber ="";
    		if( null != request.getParameter("fileNumber")){
    			drNumber= request.getParameter("fileNumber");
    			String docTyp= request.getParameter("docTyp");
    			ediFileList=logFileEdiDAO.findByDrNumber(drNumber,docTyp);
    			ediAckList = ediUtil.getEdiBeanList(ediFileList);   
                        ediBean.setDocTyp(docTyp);
    			request.setAttribute("ediBean",ediBean);
    			request.setAttribute("logFileList",ediAckList);
    		}else if(null != buttonValue && buttonValue.equals("search")){
    			ediFileList=logFileEdiDAO.findAllEdi(ediTrackingForm.getDrNumber(), ediTrackingForm.getMessageType(), ediTrackingForm.getEdiCompany());
    			ediAckList = ediUtil.getEdiBeanList(ediFileList);    
    			request.setAttribute("logFileList",ediAckList);
    			request.setAttribute("ediBean",ediBean);
    		}
    		request.setAttribute("edicompanylist",ediUtil.getCompanyTypeList());
    		request.setAttribute("messagetypelist",ediUtil.getMessageTypeList());
     		return mapping.findForward("searchEdi");
    }
}
