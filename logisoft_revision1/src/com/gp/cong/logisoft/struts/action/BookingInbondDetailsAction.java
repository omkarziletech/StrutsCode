
/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.BookingInbondDetails;
import com.gp.cong.logisoft.hibernate.dao.BookingInbondDetailsDAO;
import com.gp.cong.logisoft.struts.form.InbondDetailsForm;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.gp.cvst.logisoft.domain.BookingFcl;
import com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO;
import java.util.Date;
import org.apache.struts.util.LabelValueBean;

/**
 * MyEclipse Struts Creation date: 04-15-2009
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/fclInbondDetails" name="InbondDetailsForm"
 *                input="/jsps/fclQuotes/FclInbondDetails.jsp" scope="request"
 *                validate="true"
 */
public class BookingInbondDetailsAction extends Action {
	/*
	 * Generated Methods
	 */

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		InbondDetailsForm inbondDetailsForm = (InbondDetailsForm) form;
		String buttonValue = inbondDetailsForm.getButtonValue();
		if (null!=request.getParameter("bookingId")) {
			inbondDetailsForm.setBolId(request.getParameter("bookingId"));
		}
		if(null!=request.getParameter("fileNo")){
			inbondDetailsForm.setFileNo(request.getParameter("fileNo"));
		}
		if (request.getParameter("buttonValue") != null && request.getParameter("buttonValue").equals("booking")) {
			setRequest(inbondDetailsForm ,request,getInbondDetailsList(inbondDetailsForm));
		}else{
                   if(buttonValue.equals("Save")) {
                         setRequest(inbondDetailsForm ,request,saveInbondDetails(inbondDetailsForm));
                    }else if (buttonValue.equals("delete")) {
                         setRequest(inbondDetailsForm ,request,deleteInbondDetails(inbondDetailsForm));
                    }else if (buttonValue.equals("Update")) {
                         setRequest(inbondDetailsForm ,request,updateInbondDetails(inbondDetailsForm));
                    }
		}
                request.setAttribute("inbondTypeList", inbondTypeList());
		 return mapping.findForward("inbond");
	}
	public void setRequest(InbondDetailsForm inbondDetailsForm ,HttpServletRequest request,List list)throws Exception {
		request.setAttribute("inbondDetailsList", list);
		request.setAttribute("fileNo", inbondDetailsForm.getFileNo());
		request.setAttribute("bolId", inbondDetailsForm.getBolId());
	}
	public List<BookingInbondDetails> getInbondDetailsList(InbondDetailsForm inbondDetailsForm)throws Exception {
		List<BookingInbondDetails> inbondDetailsList= null;
		Integer bookingid = (CommonFunctions.isNotNull(inbondDetailsForm.getBolId())) ? new Integer(inbondDetailsForm.getBolId()) : 0;
                BookingFclDAO bookingFclDAO = new BookingFclDAO();
                BookingFcl bookingFcl = bookingFclDAO.findById(bookingid);
		if (CommonFunctions.isNotNullOrNotEmpty(bookingFcl.getBookingInbondDetails())) {
			inbondDetailsList = new ArrayList(bookingFcl.getBookingInbondDetails());
		}
		return inbondDetailsList;
	}
	public List<BookingInbondDetails> saveInbondDetails(InbondDetailsForm inbondDetailsForm)throws Exception {
                BookingInbondDetailsDAO bookingInbondDetailsDAO = new BookingInbondDetailsDAO();
		Integer bookingid = (CommonFunctions.isNotNull(inbondDetailsForm.getBolId())) ? new Integer(inbondDetailsForm.getBolId()) : 0;
		BookingInbondDetails inbondDetails = new BookingInbondDetails();
		        //This is for Inbond Details
                String ports = inbondDetailsForm.getInbondPort();
                if (CommonUtils.isNotEmpty(inbondDetailsForm.getInbondPort())
                        && inbondDetailsForm.getInbondPort().indexOf("/") != -1) {
                    ports = inbondDetailsForm.getInbondPort().substring(0, inbondDetailsForm.getInbondPort().indexOf("/"));
                }
                Date date = null;
                if (CommonUtils.isNotEmpty(inbondDetailsForm.getInbondDate())) {
                    date = DateUtils.parseToDateForMonthMMM(inbondDetailsForm.getInbondDate());
                    inbondDetails.setInbondDate(date);
                }
                inbondDetails.setInbondNumber(inbondDetailsForm.getInbondNumber());
                inbondDetails.setInbondType(inbondDetailsForm.getInbondType());
                inbondDetails.setInbondPort(ports);
                inbondDetails.setBolId(bookingid);
                bookingInbondDetailsDAO.save(inbondDetails);
                return bookingInbondDetailsDAO.findByProperty("bolId", bookingid);
	}
        public List<BookingInbondDetails> updateInbondDetails(InbondDetailsForm inbondDetailsForm)throws Exception {
            BookingInbondDetailsDAO bookingInbondDetailsDAO = new BookingInbondDetailsDAO();
            if(CommonUtils.isNotEmpty(inbondDetailsForm.getInbondId())){
                BookingInbondDetails inbondDetails =bookingInbondDetailsDAO.findById(Integer.parseInt(inbondDetailsForm.getInbondId()));
                String ports = inbondDetailsForm.getInbondPort();
                if (CommonUtils.isNotEmpty(inbondDetailsForm.getInbondPort())
                        && inbondDetailsForm.getInbondPort().indexOf("/") != -1) {
                    ports = inbondDetailsForm.getInbondPort().substring(0, inbondDetailsForm.getInbondPort().indexOf("/"));
                }
                Date date = null;
                if (CommonUtils.isNotEmpty(inbondDetailsForm.getInbondDate())) {
                    date = DateUtils.parseToDateForMonthMMM(inbondDetailsForm.getInbondDate());
                    inbondDetails.setInbondDate(date);
                }
                inbondDetails.setInbondNumber(inbondDetailsForm.getInbondNumber());
                inbondDetails.setInbondType(inbondDetailsForm.getInbondType());
                inbondDetails.setInbondPort(ports);
            }
            Integer bol = (CommonFunctions.isNotNull(inbondDetailsForm.getBolId())) ? new Integer(inbondDetailsForm.getBolId()) : 0;
            return bookingInbondDetailsDAO.findByProperty("bolId", bol);
	}
	public List<BookingInbondDetails> deleteInbondDetails(InbondDetailsForm inbondDetailsForm)throws Exception {
            BookingInbondDetailsDAO bookingInbondDetailsDAO = new BookingInbondDetailsDAO();
             if(CommonUtils.isNotEmpty(inbondDetailsForm.getInbondId())){
                BookingInbondDetails inbondDetails =bookingInbondDetailsDAO.findById(Integer.parseInt(inbondDetailsForm.getInbondId()));
                bookingInbondDetailsDAO.delete(inbondDetails);
             }
            Integer bol = (CommonFunctions.isNotNull(inbondDetailsForm.getBolId())) ? new Integer(inbondDetailsForm.getBolId()) : 0;
            return bookingInbondDetailsDAO.findByProperty("bolId", bol);
	}
        public List inbondTypeList()throws Exception {
                List inbondTypeList = new ArrayList();
                inbondTypeList.add(new LabelValueBean("Select Inbond Type",""));
                inbondTypeList.add(new LabelValueBean("IT","IT"));
                inbondTypeList.add(new LabelValueBean("IE","IE"));
                inbondTypeList.add(new LabelValueBean("TE","TE"));
                inbondTypeList.add(new LabelValueBean("WDT","WDT"));
                inbondTypeList.add(new LabelValueBean("WDIE","WDIE"));
                inbondTypeList.add(new LabelValueBean("WDTE","WDTE"));
                inbondTypeList.add(new LabelValueBean("IT/QP","IT/QP"));
                inbondTypeList.add(new LabelValueBean("IE/QP","IE/QP"));
                inbondTypeList.add(new LabelValueBean("TE/QP","TE/QP"));
            return inbondTypeList;
        }
}