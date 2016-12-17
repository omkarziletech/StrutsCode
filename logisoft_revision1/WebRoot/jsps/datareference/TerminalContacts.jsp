<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.User,java.util.*,com.gp.cong.logisoft.domain.TerminalContacts,com.gp.cong.logisoft.domain.RefTerminal,com.gp.cong.logisoft.beans.TerminalBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List contactAddList=null;
List contactList=new ArrayList();
User user=new User();
String buttonValue="";
String terminalType="";
RefTerminal terminal=new RefTerminal();
RefTerminal terminal1=null;
TerminalBean terminalBean=new TerminalBean();

if(request.getAttribute("buttonValue")!=null)
{
buttonValue=(String)request.getAttribute("buttonValue");
}


if(session.getAttribute("contactAddList")!=null)
{
contactAddList=(List)session.getAttribute("contactAddList");
}

String modify="";
	if(session.getAttribute("contactmodify")!=null)
	{
	modify=(String)session.getAttribute("contactmodify");
	}
	
%>
<html> 
	<head>
		 <base href="<%=basePath%>">
    
    <title>My JSP 'TerminalContact.jsp' starting page</title>
    
<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	
  
   function cancel1(val)
   {
  
  if(val!=null && val!='' && (val == 3 || val == 0))
  {
  document.terminalContactsForm.buttonValue.value="viewcancel";
  }
  else if(val!=null && val!='' && val == 2)
  {
  document.terminalContactsForm.buttonValue.value="editcancel";
  }
   else
   { 
   document.terminalContactsForm.buttonValue.value="addcancel";
   }
   document.terminalContactsForm.submit();
   }
  
   function disabled(val)
   {
    
	if( val!=null && val!='' && (val == 3 || val == 0) )
	{		
      
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		
   		 if(imgs[k].id == "add" ||imgs[k].id=="delete")
   		 {
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		//if(input[i].id != "buttonValue" || input[i].id != "previous")
	 		//
	  		//{
	  		//   input[i].disabled = true;
	  		//}
  	 	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled = true;
	  		
  	 	}
  	 }
  	 //displaytagcolor();
   	 //initRowHighlighting();
   }
   function displaytagcolor()
		  {
				var datatableobj = document.getElementById('terminalcontacttable');
				if(datatableobj!=null)
				{
				for(i=0; i<datatableobj.rows.length; i++)
				{
					var tablerowobj = datatableobj.rows[i];
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
				}
				}
		 }
		 function initRowHighlighting()
  		{
			if (!document.getElementById('terminalcontacttable')){ return; }
			var tables = document.getElementById('terminalcontacttable');
			attachRowMouseEvents(tables.rows);
		}

		function attachRowMouseEvents(rows)
		{
			for(var i =1; i < rows.length; i++)
			{
				var row = rows[i];
				row.onmouseover =	function() 
								{ 
									this.className = 'rowin';
								}
				row.onmouseout =	function() 
								{ 
									this.className = '';
								}
            	row.onclick= function() 
								{ 
							 	}
			}
		
		}	
	</script>
  </head>
  
  <body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/terminalContacts" scope="request">
	
  
<div align="right">
   <input type="button" class="buttonStyleNew"  id="previous" value="Previous" onclick="cancel1('<%=modify%>')"/>
</div>
<table width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
  <tr class="tableHeadingNew" >
    Terminal Contacts 
  </tr>
<tr>
  <td class="textlabels">
           <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%;">
           <%
           int i=0;
           int j=0;
            %>
          <table  border="0" cellpadding="0" cellspacing="0">
		<display:table name="<%=contactAddList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="terminalcontacttable" style="width:100%"> 
			<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="User"/>
  			<display:setProperty name="paging.banner.items_name" value="Users"/>
  			<%
  			String firstName="";
  			String email="";
  			if(contactAddList!=null && contactAddList.size()>0)
  			{
  			  User termContacts=(User)contactAddList.get(i);
  		      
  		       firstName=termContacts.getFirstName()+"     "+termContacts.getLastName();
  		       email=termContacts.getEmail();
  			 } 
  			 %>
		<display:column title="Contact Name" ><%=firstName%></display:column>
		
		<display:column title="Contact Email" property="email"></display:column>
		
		<%
		i++;
		
		 %>
		 
       </display:table>
              </table></div></td>
            </tr>
          


          

   
    
 <html:hidden property="buttonValue" styleId="buttonValue"/>
 <html:hidden property="index" />
 <html:hidden property="newterm" styleId="newterm"/>
   </html:form> 
  </body>
  	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>	 

