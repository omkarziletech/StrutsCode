<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,java.util.Iterator,java.util.Set,java.util.HashSet,com.gp.cong.logisoft.domain.CarriersOrLine,com.gp.cong.logisoft.domain.Ports,com.gp.cong.logisoft.domain.CarrierOceanEqptRates,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.beans.SearchCarriersBean"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
List eqptList=new ArrayList();
DecimalFormat df = new DecimalFormat("0.00");

List eqptListforDisplay=null;
if(session.getAttribute("eqptList") != null)
{
	eqptListforDisplay=(List)session.getAttribute("eqptList");
}

request.setAttribute("eqptList",dbUtil.getGenericCodeList1(new Integer(6),"yes","Select Equipment Type",eqptListforDisplay));



SearchCarriersBean scBean=new SearchCarriersBean();
if(request.getAttribute("scBean")!=null)
{
scBean=(SearchCarriersBean)request.getAttribute("scBean");
}
String modify = null;
if(session.getAttribute("airocean")!=null)
{
modify=(String)session.getAttribute("airocean");
}

%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>Ocean </title>
	<%@include file="../includes/baseResources.jsp" %>

	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script language="javascript" type="text/javascript">
	
	function addports()
	{
	if(document.oceanExceptionForm.eqpttype.value=="0")
	{
	alert("Please select EQPT type");
	return;
	}
		var specialrate=document.oceanExceptionForm.specialrate;
		if(specialrate.value=="")
 		{  
 		  	 alert("Please enter Special Rate ");
  		 	 specialrate.focus();
  			 return;
  		}  
  		if(isFloat(document.oceanExceptionForm.specialrate.value)==false)
  {
    alert("specialrate should be Numeric.");
  	document.oceanExceptionForm.specialrate.value="";
    document.oceanExceptionForm.specialrate.focus();
    return;
   } 
		document.oceanExceptionForm.buttonvalue.value="add";
		document.oceanExceptionForm.submit();
	}
	function confirmdelete(obj)
	{
    	var rowindex=obj.parentNode.parentNode.rowIndex;
		var x=document.getElementById('eqpttable').rows[rowindex].cells;	
		document.oceanExceptionForm.index.value=obj.name;
    	document.oceanExceptionForm.buttonvalue.value="delete";
   	    var result = confirm("Are you sure you want to delete this port "+x[0].innerHTML);
		if(result)
		{
			document.oceanExceptionForm.submit();
   		}	
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
	 		if(input[i].id != "buttonValue")
	 		
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
  	
   }
   	</script>
</head>
<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
<html:form action="/oceanException" scope="request">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
					<tr class="tableHeadingNew">
  						Add Special Equipment Rates for FCL
					</tr>
    <tr class="style2">
      	<td >EQPT Type* </td>
      	<td ><html:select property="eqpttype" styleClass="selectboxstyle"  value="<%=scBean.getEqptType()%>">
      					<html:optionsCollection name="eqptList"/>          
                		</html:select>
        </td>
      	<td >Special Rate </td>
      	<td ><html:text property="specialrate" value="<%=scBean.getSpclRate()%>" onkeypress="check(this,8)" maxlength="11"/></td>
      	<td >
      	<input type="button" class="buttonStyleNew" value="Add" name="add" onclick="addports()" /> 
      	
    </tr>
    </table>
   <br>
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
   <tr class="tableHeadingNew">List of Special Equipment Rates for FCL </tr>
		<tr><td>
		<div id="divtablesty1" >
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <% 
        int i=0;        
        %> 
        <display:table name="<%=eqptListforDisplay%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="eqpttable">
        <display:setProperty name="basic.msg.empty_list"><span class="pagebanner"></display:setProperty> 
        <display:setProperty name="paging.banner.placement" value="bottom" />
		<display:setProperty name="paging.banner.item_name" value="Printer"/>
  		<display:setProperty name="paging.banner.items_name" value="Printers"/>
  		<%	String eqpt=null;
  		  
  			if(eqptListforDisplay != null && eqptListforDisplay.size()>0)
  			{
  				
  				CarrierOceanEqptRates oceanEqptobj= (CarrierOceanEqptRates)eqptListforDisplay.get(i);
  				GenericCode eqptypeobj=oceanEqptobj.getEqpttype();
  				if(eqptypeobj != null)
  				{
  					eqpt=eqptypeobj.getCodedesc();
  				}
  			
  			}
  		 %>
	    <display:column title="EQPT Type" ><%=eqpt%></display:column>
	    <display:column/>
		<display:column property="specialrate"  title="Special Rate" />
		<display:column/>
		<display:column>
				<input type="button" class="buttonStyleNew" name="<%=i%>" value="Delete" id="delete" onclick="confirmdelete(this)" /> 
		</display:column>
  		<% i++;%>
		</display:table>
        </table></div> </table>  
    
  <html:hidden property="buttonvalue" styleId="buttonvalue"/>
<html:hidden property="index" />
</html:form>
</body>

<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

