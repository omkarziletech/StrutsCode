<%@include file="../includes/jspVariables.jsp" %>
<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String description="";
if(request.getParameter("paramid")!=null)
{
description=(String)request.getParameter("paramid");
}
%>
<html> 

	<head>
		<title>JSP for NotePageForm form</title>
		<link rel="stylesheet" type="text/css" href="<%=path%>/css/cal/skins/aqua/theme.css" title="Aqua" />
<link rel="alternate stylesheet" type="text/css" media="all" href="<%=path%>/css/cal/calendar-win2k-cold-2.css" title="win2k-cold-2" />
<script type="text/javascript" src="<%=path%>/js/caljs/calendar.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
<script type="text/javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
<script language="Javascript" type="text/javascript" src="<%=path%>/js/common.js"></script>
<script type="text/javascript">
function cancel()
{
document.DescriptionForm.buttonValue.value="cancel";
document.DescriptionForm.submit();
}
</script>
<%@include file="../includes/baseResources.jsp" %>

	</head>
		<body class="whitebackgrnd">
		<html:form  action="/descriptionAction" name="DescriptionForm" type="com.gp.cong.logisoft.struts.form.DescriptionForm" scope="request">
		<div align="right">
		<input type="button" class="buttonStyleNew"  value="Go Back" onclick="cancel()"/>

		</div>
		
  			<td><table width="100%" border="0" cellspacing="0" cellpadding="0"class="tableBorderNew">
  		
    	
  		<tr class="tableHeadingNew">
    		Description Details 
    		
  		</tr>
         <tr height="100">
     
     <td>
     <table width="600" border="0" cellspacing="0">
            <tr> 
                 <td width="115" class="textlabels">Description</td>
                 
             <td width="144" align="left"><html:textarea property="notes" value="<%=description%>"  rows="5" cols="72" readonly="true" styleClass="textareastyleNotes"/></td>
             <td width="500"></td>
          
             </tr>
           
         
        </table></td>
      </tr>
      </table>
     
     
      <html:hidden property="buttonValue"/>
</html:form>
	</body>
	
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

