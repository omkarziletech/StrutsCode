/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.Customer;
import com.gp.cong.logisoft.domain.CustomerAssociation;
import com.gp.cong.logisoft.struts.form.CustomerForm;
import com.gp.cong.logisoft.util.DBUtil;

/** 
 * MyEclipse Struts
 * Creation date: 12-22-2007
 * 
 * XDoclet definition:
 * @struts.action path="/customer" name="customerForm" input="/jsps/Tradingpartnermaintainance/Customer.jsp" scope="request" validate="true"
 */
public class CustomerAction extends Action {
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
			HttpServletRequest request, HttpServletResponse response) {
		CustomerForm customerForm = (CustomerForm) form;// TODO Auto-generated method stub
		String buttonValue=customerForm.getButtonValue();
		HttpSession session = ((HttpServletRequest)request).getSession();
        
		Set configset=new HashSet<CustomerAssociation>();
		String coName=customerForm.getCoName();
		
		String address1=customerForm.getAddress1();
		String portName=customerForm.getPortName();
		//String schNum=customerForm.getSchnum();
		
		String portid=customerForm.getPortid();
		
		String phone=customerForm.getPhone();
		String contactName=customerForm.getContactName();
		String zip=customerForm.getZip();
		
		String extension=customerForm.getExtension();
		
		String fax=customerForm.getFax();
		String email1=customerForm.getEmail1();
		String email2=customerForm.getEmail2();
		String schNum=customerForm.getSchNum();
   		String accountMaster=customerForm.getMasterAddress();
		DBUtil dbUtil=new DBUtil();

		if(request.getParameter("ind")!=null)
		{
			
			List addressList=new ArrayList();
			int ind = Integer.parseInt(request.getParameter("ind"));
			addressList=(List)session.getAttribute("addressList");
			if(session.getAttribute("customerbean")!=null)
			{
				session.removeAttribute("customerbean");
			}
			Customer cust=(Customer)addressList.get(ind);
			session.setAttribute("adressCust", cust);
			String edit="edit";
			request.setAttribute("edit",edit);
		}
		else
		{
		    Customer accountdetails=null;
		
			if(session.getAttribute("customer")!=null)
			{
				accountdetails=(Customer)session.getAttribute("customer");
			}
			else
			{
				accountdetails=new Customer();
			}
			
			accountdetails.setCoName(coName);
			accountdetails.setAddress1(address1);
			accountdetails.setPhone(phone);
			accountdetails.setContactName(contactName);
			accountdetails.setZip(zip);
			accountdetails.setFax(fax);
			accountdetails.setExtension(extension);
			accountdetails.setEmail1(email1);
			accountdetails.setEmail2(email2);
			accountdetails.setCity2(customerForm.getCity());
			
			session.setAttribute("customer",accountdetails);
			
			List addressList=new ArrayList();
			int index=0;
			if(buttonValue!=null && buttonValue.equals("add"))
			{
				if(session.getAttribute("addressList")!=null)
				{
					addressList=(List)session.getAttribute("addressList");
					for(int i=0;i<addressList.size();i++)
					{
						Customer cust=(Customer)addressList.get(i);
						if(cust.getIndex()!=null)
						{
							if(cust.getIndex()>index)
							{
								index=cust.getIndex();
							}
						}
					}
					index++;
				}
				else
				{
					addressList=new ArrayList();
					index++;
				}
				accountdetails.setIndex(index);
				
				addressList.add(accountdetails);
				session.setAttribute("addressList",addressList);
				
				
				if(session.getAttribute("customer")!=null)
				{
					session.removeAttribute("customer");
				}
				
			}
		}	
			request.setAttribute("buttonValue",buttonValue);
			
			return mapping.findForward("addcustomer");
	}
	
}	
