/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cvst.logisoft.struts.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cvst.logisoft.domain.HazmatMaterial;
import com.gp.cvst.logisoft.hibernate.dao.HazmatMaterialDAO;
import com.gp.cvst.logisoft.struts.form.AddBookingHazmatForm;
import org.apache.commons.beanutils.PropertyUtils;

/** 
 * MyEclipse Struts
 * Creation date: 05-25-2009
 * 
 * XDoclet definition:
 * @struts.action path="/addBookingHazmat" name="addBookingHazmatForm" input="/jsps/fclQuotes/AddBookingHazmat.jsp" scope="request" validate="true"
 */
public class AddBookingHazmatAction extends Action {
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
			HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, Exception {
		AddBookingHazmatForm addBookingHazmatForm = (AddBookingHazmatForm) form;// TODO Auto-generated method stub
		HttpSession session = ((HttpServletRequest)request).getSession();
		String buttonValue=addBookingHazmatForm.getButtonValue();
		String containerId=addBookingHazmatForm.getContainerId();
		String bookcheckbox[]=addBookingHazmatForm.getBookcheckbox();
		HazmatMaterialDAO hazmatMaterialDAO=new HazmatMaterialDAO();
		List hazmatList=new ArrayList();
		if(buttonValue.equals("addHazmat")){
			if(session.getAttribute("bookinghazmat")!=null){
				hazmatList=(List)session.getAttribute("bookinghazmat");
			}
			HashMap hashMap=new HashMap<Integer,HazmatMaterial>();
			for (Iterator iter = hazmatList.iterator(); iter.hasNext();) {
				HazmatMaterial hazmatMaterial = (HazmatMaterial) iter.next();
				hashMap.put(hazmatMaterial.getId(), hazmatMaterial);
			}
			for (int i = 0; i < bookcheckbox.length; i++) {
				if(hashMap.containsKey(Integer.parseInt(bookcheckbox[i]))){
				HazmatMaterial hazmatMaterial=(HazmatMaterial)hashMap.get(Integer.parseInt(bookcheckbox[i]));
				HazmatMaterial hazmat=new HazmatMaterial();
                                PropertyUtils.copyProperties(hazmat, hazmatMaterial);
				hazmat.setId(null);
				hazmat.setBolId(Integer.parseInt(containerId));
				hazmat.setDocTypeCode("fclbl");
				hazmat.setDate(new Date());
				hazmat.setDocTypeId(containerId);
				hazmatMaterialDAO.save(hazmat);
				
				}
			}
			request.setAttribute("buttonvalue", "completed");
		}
		return mapping.getInputForward();
	}
}