<%@ page language="java" pageEncoding="ISO-8859-1" import= "com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerContact,com.gp.cong.logisoft.domain.CustomerAccounting,java.util.*,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.Customer" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>



<%  
CustomerContact customerCont=new CustomerContact();
String path1="";
 if(request.getAttribute("path1")!=null)
 {
   path1=(String)request.getAttribute("path1");
 }
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    
    String btvalue=" ";
    if(request.getParameter("button")!=null)
    {
    btvalue=(String)request.getParameter("button");
    session.setAttribute("button",btvalue);
    }
  
  
    
    List conList=new ArrayList();
    
  
    
    if(btvalue.equals("cont")||btvalue.equals("editcont")||btvalue.equals("mcont")||btvalue.equals("meditcont"))
    {
     
	        if(session.getAttribute("addConfig")!=null )
	 			{
	 			
	 		
					conList=(List)session.getAttribute("addConfig");
	 			}
	 	
	}
	
		
	
  String accnt="";
if(request.getAttribute("close")!=null)
{

accnt=(String)request.getAttribute("close");

}

/*if(request.getAttribute("masteraccounting")!=null)
{
accnt=(String)request.getAttribute("masteraccounting");
}*/

				 
				
if(accnt.equals("close"))
{
%>
<script >

parent.parent.GB_hide();


<%--self.close();--%>
<%--opener.location.href="<%=path%>/<%=path1%>";--%>
</script>
<%
}
String editPath=path+"/contactPopup.do";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Jsp for Contact Information</title>
    
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	
  </head>
  
  <body class="whitebackgrnd" >
<html:form action="/contactPopup" name="contactPopupForm" type="com.gp.cong.logisoft.struts.form.ContactPopupForm" scope="request">
	<font color="blue" size="2"></font>

<table width="100%"  border="1"  align="center" class="tableBorderNew">
<tr class="tableHeadingNew" colspan="2">Contact Details</tr>			
<tr>
  <td>
      <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:300px;">
      <table width="100%" border="0">	
       <%
    	  int i=0;
    	  if((conList!=null && conList.size()>0)) 
    	  {
   	   %>
	
	<display:table name="<%=conList%>" pagesize="<%=pageSize%>" class="displaytagstyle">
	<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Contact Details displayed,For more code click on page numbers.
    				</span>
  			  </display:setProperty>
  			  <display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
						One {0} displayed. Page Number
					</span>
  			  </display:setProperty>
  			   <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
						{0} {1} Displayed, Page Number
						
					</span>
  			  </display:setProperty>
  			 
    			<display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					No Records Found.
				</span>	
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="User"/>
  			<display:setProperty name="paging.banner.items_name" value="Users"/> 
  			 <% 
  			 String firstname="";
  			 String lastname="";
  			 String position="";
  			 String phoneno="";
  			 String extension="";
  			 String fax="";
  			 String email="";
  			 
  			 String iStr=String.valueOf(i);
  			String tempPath=editPath+"?ind="+iStr;
  			if(conList!=null && conList.size()>0)
  			{
	  			 customerCont=(CustomerContact)conList.get(i);
	  			if(customerCont.getFirstName()!=null)
	  			{
	  			firstname=customerCont.getFirstName();
	  			}
	  			if(customerCont.getLastName()!=null)
	  			{
	  			  lastname=customerCont.getLastName();
	  			}
	  			if(customerCont.getPosition()!=null)
	  			{
	  			  position=customerCont.getPosition();
	  			}
	  			if(customerCont.getPhone()!=null)
	  			{
	  			  phoneno=customerCont.getPhone();
	  			}
	  			if(customerCont.getExtension()!=null)
	  			{
	  			  extension=customerCont.getExtension();
	  			}
	  			if(customerCont.getFax()!=null)
	  			{
	  			  fax=customerCont.getFax();
	  			}
	  			if(customerCont.getEmail()!=null)
	  			{
	  			  email=customerCont.getEmail();
	  			}
  			}
  			 %>
        
   
		<display:column title="First Name" ><a href="<%=tempPath%>"><%=firstname%></a></display:column>
		
		<display:column title="&nbsp;&nbsp;Last Name" ><%=lastname%></display:column>
		
		<display:column title="&nbsp;&nbsp;Position" ><%=position%></display:column>
		
		<display:column title="&nbsp;&nbsp;Phone no"><%=phoneno%></display:column>
		
		<display:column title="&nbsp;&nbsp;Extension"><%=extension%></display:column>
		
		<display:column title="&nbsp;&nbsp;Fax" ><%=fax%></display:column>
		
		<display:column title="&nbsp;&nbsp;Email"><%=email%></display:column>
	<%i++; %> 
       </display:table>

  <%}%>
    </table> </div>
  </td></tr>
     <html:hidden property="buttonValue"/>
</table>  
    </html:form>
  </body>
  <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
