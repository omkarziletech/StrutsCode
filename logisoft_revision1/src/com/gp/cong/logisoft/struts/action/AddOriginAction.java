/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.ConsigneeConfig;
import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.domain.RelayInquiry;
import com.gp.cong.logisoft.domain.RelayOrigin;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import com.gp.cong.logisoft.hibernate.dao.RelayInquiryDAO;
import com.gp.cong.logisoft.hibernate.dao.RelayOriginDAO;
import com.gp.cong.logisoft.struts.form.AddOriginForm;

/** 
 * MyEclipse Struts
 * Creation date: 12-15-2007
 * 
 * XDoclet definition:
 * @struts.action path="/addOrigin" name="addOriginForm" input="/jsps/datareference/addOrigin.jsp" scope="request" validate="true"
 */
public class AddOriginAction extends Action {
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
		AddOriginForm addOriginForm = (AddOriginForm) form;// TODO Auto-generated method stub
		HttpSession session = ((HttpServletRequest)request).getSession();
		
		String originCode=addOriginForm.getOriginCode();
		String originName=addOriginForm.getOriginName();
		String ttToPol=addOriginForm.getTtToPol();
		String cutOffDayOfWeek=addOriginForm.getCutOffDayOfWeek();
		String cutOffTime=addOriginForm.getCutOffTime();
		String buttonValue=addOriginForm.getButtonValue();
		String forwardName="";
		int index=0;
		GenericCode week=new GenericCode();
		GenericCodeDAO genericCodeDAO=new GenericCodeDAO();
		RelayOrigin relayOrigin=null;
		if(!buttonValue.equals("cancel") && !buttonValue.equals("delete") && !buttonValue.equals("update"))
		{
			if(session.getAttribute("relayOrigin")!=null)
			{
				relayOrigin=(RelayOrigin)session.getAttribute("relayOrigin");
			}
			else
			{
				relayOrigin=new RelayOrigin();
			}
		    
			Ports originobj= null;
			RelayOriginDAO relayOriginDAO=new RelayOriginDAO();
			PortsDAO portsDAO=new PortsDAO();
			
			//return mapping.findForward("addorigin");
			if(cutOffDayOfWeek!=null && !cutOffDayOfWeek.equals("0"))
			{	
				week=genericCodeDAO.findById(Integer.parseInt(cutOffDayOfWeek));
				relayOrigin.setCutOffDayOfWeek(week);
			}
			if(ttToPol!=null && !ttToPol.equals(""))
			{	
				relayOrigin.setTtToPol(Integer.parseInt(ttToPol));
			}
			if(cutOffTime!=null && !cutOffTime.equals(""))
			{	
				relayOrigin.setCutOffTime(Integer.parseInt(cutOffTime));
			}
			session.setAttribute("relayOrigin",relayOrigin);
			forwardName="addorigin";
			
		}
		if(buttonValue.equals("add"))
		{
			List originList=null;
			if(session.getAttribute("originList")!=null)
			{
				originList=(List)session.getAttribute("originList");
				for(int i=0;i<originList.size();i++)
				{
					
					RelayOrigin custTemp=(RelayOrigin)originList.get(i);
					if(custTemp.getIndex()>index)
					{
						index=custTemp.getIndex();
					}
				}
				index++;
				
			}
			else
			{
				originList=new ArrayList();
				index++;
			}
			boolean flag=false;
			for(int i=0;i<originList.size();i++)
			{
				RelayOrigin port1=(RelayOrigin)originList.get(i);
				if(port1.getOriginId().getShedulenumber().equals(relayOrigin.getOriginId().getShedulenumber()))
				{
					if(port1.getOriginId().getControlNo().equals(relayOrigin.getOriginId().getControlNo()))
					{
						flag=true;
						break;
					}
				}
			}
			
				if(flag)
				{
					String msg="This port is already added";
					request.setAttribute("msg",msg);
					buttonValue="";
				}
				else
				{
			
			relayOrigin.setIndex(index);
			originList.add(relayOrigin);
			session.setAttribute("originList",originList);
			session.removeAttribute("relayOrigin");
				}
		}
		if(buttonValue.equals("update"))
		{
			if(session.getAttribute("relayOrigin")!=null)
			{
				relayOrigin=(RelayOrigin)session.getAttribute("relayOrigin");
				relayOrigin.setOriginId(relayOrigin.getOriginId());
				relayOrigin.setTtToPol(Integer.parseInt(addOriginForm.getTtToPol()));
				if(cutOffDayOfWeek!=null && !cutOffDayOfWeek.equals("0"))
				{	
					week=genericCodeDAO.findById(Integer.parseInt(cutOffDayOfWeek));
					relayOrigin.setCutOffDayOfWeek(week);
				}
				relayOrigin.setCutOffTime(Integer.parseInt(addOriginForm.getCutOffTime()));
			}
			forwardName="addorigin";
			session.removeAttribute("relayOrigin");
		}
		if(buttonValue.equals("cancel"))
		{
		    forwardName="cancel";
		    session.removeAttribute("relayOrigin");
		}
		if(buttonValue.equals("delete"))
		{
			if(session.getAttribute("relayOrigin")!=null)
			{
				relayOrigin=(RelayOrigin)session.getAttribute("relayOrigin");
			}
			if(session.getAttribute("originList")!=null)
			{
				List originList=(List)session.getAttribute("originList");
				originList.remove(relayOrigin);
			}
			forwardName="addorigin";
			session.removeAttribute("relayOrigin");
		}
		
		request.setAttribute("buttonValue",buttonValue);
		
		return mapping.findForward(forwardName);
	}
}