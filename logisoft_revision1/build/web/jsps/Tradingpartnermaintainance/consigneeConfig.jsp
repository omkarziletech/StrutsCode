<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.ConsigneeConfig"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String modify="";
List addConfig=null;
if(session.getAttribute("addConfig")!=null)
{
addConfig=(List)session.getAttribute("addConfig");
}

modify = (String) session.getAttribute("modifyforcustomer");
if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
    
	}
	String editPath=path+"/consigneeConfig.do";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>JSP for ConsigneeForm form</title>
    <%@include file="../includes/baseResources.jsp" %>


	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
    <script type="text/javascript">
    function addform1()
    {
    document.consigneeConfigForm.buttonValue.value="addconfig";
    document.consigneeConfigForm.submit();
    }
    function editform1(obj)
    {
    
     var rowindex=obj.parentNode.parentNode.rowIndex;
	 var x=document.getElementById('customertable').rows[rowindex].cells;	
	 document.consigneeConfigForm.name.value=x[0].innerHTML;
  document.consigneeConfigForm.index.value=rowindex-1;
  
    document.consigneeConfigForm.buttonValue.value="editconfig";
    
    document.consigneeConfigForm.submit();
    }
    function deleteform1(obj)
    {
    var rowindex=obj.parentNode.parentNode.rowIndex;
	 var x=document.getElementById('customertable').rows[rowindex].cells;	
	 document.consigneeConfigForm.name.value=x[0].innerHTML;
  document.consigneeConfigForm.index.value=rowindex-1;
  
    document.consigneeConfigForm.buttonValue.value="deleteconfig";
    var result = confirm("Are you sure you want to delete this Contact "+x[0].innerHTML);
	if(result)
	{
   		document.consigneeConfigForm.submit();
   	}
    
    }
    function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		 
   		    imgs[k].style.visibility = 'hidden';
   		 
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	 		if(input[i].id != "buttonValue" || imput[i].id!="index")
	 		{
	  			input[i].disabled = true;
	  		}
  	 	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled = true;
	  		
  	 	}
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 	if(val1==3)
		{
			alert(val2);
			
		}		
    }
    </script>
	</head>
	 <body class="whitebackgrnd" >
		<html:form action="/consigneeConfig" scope="request">
				   
			<table width="811" border="0" cellpadding="0" cellspacing="0">

<tr class="textlabels">
  <td width="535" align="left" class="headerbluelarge"><bean:message key="form.customerForm.contactconfig" /> </td>
  <td><img src="<%=path%>/img/addnew.gif" border="0" onclick="addform1()" id="addnew"/></td>
</tr>
 <table width="600" height="6" border="0" cellpadding="0" cellspacing="0">
<tr class="textlabels">
    <td colspan="2"align="left" class="headerbluelarge">&nbsp;</td>

  </tr>
  <tr>
  <td colspan="2"></td></tr>
     <tr>
    <td height="12" colspan="2"  class="headerbluesmall">&nbsp;&nbsp;<bean:message key="form.customerForm.contactdetail" /> </td> 
  </tr>
  </table>
  <%
  if(addConfig!=null && addConfig.size()>0)
  {
  int i=0;
   %>
  <tr>
  <td>
    <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:250px;">
          <table width="580" border="0" cellpadding="0" cellspacing="0">
       
         
    
         <display:table name="<%=addConfig%>" class="displaytagstyle" id="customertable">
       
			<display:setProperty name="paging.banner.item_name" value="CustomerContact"/>
  			<display:setProperty name="paging.banner.items_name" value="CustomerContacts"/>
  			<%
  			String iStr=String.valueOf(i);
  			String editPath1=editPath+"?paramid="+iStr;
  			 %>
  			 
	     <display:column  property="firstName" href="<%=editPath1%>"   title="FirstName" /></dispaly:column>
	    
		    <display:column property="lastName" title="LastName" /></dispaly:column>
		
		    <display:column property="position" title="Position" /></dispaly:column>
		 

		    <display:column property="phone" title="Phone Number" /></dispaly:column>
		  
		   <display:column property="fax" title="Fax" /></dispaly:column>
		 
		   <display:column property="email" title="Email" /></dispaly:column>
		  
	    <%i++; %>
	    
 
		</display:table>
        </table></div>   
         </td>
  		</tr>  
  		<%} %>   
</table>

<html:hidden property="buttonValue" styleId="buttonValue"/>
<html:hidden property="name" styleId="name"/>
<html:hidden property="index" styleId="index"/>
		</html:form>
		
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

