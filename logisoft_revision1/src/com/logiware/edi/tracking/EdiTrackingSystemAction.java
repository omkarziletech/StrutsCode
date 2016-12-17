package com.logiware.edi.tracking;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.util.EdiUtil;
import com.oreilly.servlet.ServletUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.util.LabelValueBean;

public class EdiTrackingSystemAction extends Action{

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
            HttpServletRequest request, HttpServletResponse response)throws Exception {
                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                cal.add(Calendar.DATE, -30);
    		HttpSession session = ((HttpServletRequest) request).getSession();
    		EdiTrackingSystemForm ediTrackingForm = (EdiTrackingSystemForm) form;
                String fromDate = ediTrackingForm.getFromDate();
                String toDate = ediTrackingForm.getToDate();
                String fromDateStr="";
                String toDateStr="";
                if(null != fromDate && !fromDate.equals("") && null != toDate && !toDate.equals("")){
                        fromDateStr = DateUtils.formatDate(dateFormat.parse(fromDate), "yyyyMMdd");
                        toDateStr = DateUtils.formatDate(dateFormat.parse(toDate), "yyyyMMdd");
                }
    		EdiSystemBean ediBean = new EdiSystemBean();
    		EdiTrackingSystemDAO logFileEdiDAO = new EdiTrackingSystemDAO();
    		List ediFileList = new ArrayList();
    		List<EdiSystemBean> ediAckList = new ArrayList<EdiSystemBean>();
    		EdiUtil ediUtil =new EdiUtil();
    		ediBean.setDrNumberTxt(ediTrackingForm.getDrNumber());
    		ediBean.setEdiCompanyTxt(ediTrackingForm.getEdiCompany());
    		ediBean.setMessageTypeTxt(ediTrackingForm.getMessageType());
            ediBean.setPortOfLoad(ediTrackingForm.getPortOfLoad());
    		ediBean.setPortOfDischarge(ediTrackingForm.getPortOfDischarge());
    		ediBean.setPlaceOfReceipt(ediTrackingForm.getPlaceOfReceipt());
            ediBean.setPlaceOfDelivery(ediTrackingForm.getPlaceOfDelivery());
            ediBean.setBookingNumber(ediTrackingForm.getBookingNo());
            ediBean.setEdiStatus(ediTrackingForm.getEdiStatus());
            ediBean.setTransportService(ediTrackingForm.getTransportService());
            ediBean.setTransactionStatus(ediTrackingForm.getTransactionStatus());
    		String buttonValue=ediTrackingForm.getButtonValue();
    		String drNumber ="";
    		if(null != request.getParameter("fileNumber")){
    			drNumber= request.getParameter("fileNumber");
    			ediFileList=logFileEdiDAO.findByDrNumber(drNumber);
    			ediAckList = ediUtil.getEdiTrackingBeanList(ediFileList);
    			request.setAttribute("ediBean",ediBean);
    			request.setAttribute("logFileList",ediAckList);
    		}else if(null != buttonValue && buttonValue.equals("search")){
    			ediFileList=logFileEdiDAO.findAllEdi(ediTrackingForm.getDrNumber(), ediTrackingForm.getMessageType(), 
                        ediTrackingForm.getEdiCompany(),ediTrackingForm.getPlaceOfReceipt(),ediTrackingForm.getPlaceOfDelivery(),
                        ediTrackingForm.getPortOfLoad(),ediTrackingForm.getPortOfDischarge(),ediTrackingForm.getBookingNo(),
                                fromDateStr,toDateStr);
    			ediAckList = ediUtil.getEdiTrackingBeanList(ediFileList);
    			request.setAttribute("logFileList",ediAckList);
    			request.setAttribute("ediBean",ediBean);
    		}else if(null != buttonValue && buttonValue.equals("exportToExcel")){
                        String fileName = new EdiTrackingSystemBC().exportEdiTracking(ediTrackingForm);
                        if (CommonUtils.isNotEmpty(fileName)) {
                            response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(fileName));
                            response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
                            ServletUtils.returnFile(fileName, response.getOutputStream());
                            return null;
                        }
                }
    		request.setAttribute("edicompanylist",ediUtil.getCompanyTypeList());
            List<LabelValueBean> messageList = ediUtil.getMessageTypeList();
            for(LabelValueBean value : messageList) {
                if("997".equals(value.getLabel())) {
                    messageList.remove(value);
                    break;
                }
            }
    		request.setAttribute("messagetypelist",messageList);
                request.setAttribute("toDate", DateUtils.formatStringDateToAppFormatMMM(new Date()));
                request.setAttribute("fromDate", DateUtils.formatStringDateToAppFormatMMM(cal.getTime()));
     		return mapping.findForward("searchEdi");
    }
}
