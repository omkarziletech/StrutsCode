<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.domain.HazmatMaterial"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
List hazmatList=new ArrayList();
if(session.getAttribute("bookinghazmat")!=null){
hazmatList=(List)session.getAttribute("bookinghazmat");
}
String containerId="";
if(request.getParameter("containerId")!=null){
containerId=request.getParameter("containerId");
}
String bolid="";
if(request.getParameter("bolid")!=null){
bolid=request.getParameter("bolid");
}
if(request.getAttribute("buttonvalue")!=null && request.getAttribute("buttonvalue").equals("completed")){
%>
<script type="text/javascript">
self.close();
</script>
<%
}
%> 
<html> 
	<head>
		<title>JSP for AddBookingHazmatForm form</title>
	</head>
	<%@include file="../includes/baseResources.jsp" %>
		<script type="text/javascript">
		function addHazmat(){
		document.addBookingHazmatForm.buttonValue.value="addHazmat";
		document.addBookingHazmatForm.submit();
		}
		</script>
<body class="whitebackgrnd" >
		<html:form action="/addBookingHazmat" scope="request">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew ">
		 <tr class="tableHeadingNew"><td width="100%">
		   <table><tr><td width="100%" class="style2">List of Hazmats</td>
		   <td><input type="button" value="Add" onclick="addHazmat()"></td>
		   </tr>
		   </table></td>
		   </tr>
		   <tr>
		   <table>
		   <%
if(hazmatList!=null && hazmatList.size()>0)
 {
 int i=0; 
 
%>
<div id="divtablesty1" style="height:80%;">
<display:table name="<%=hazmatList%>" class="displaytagstyle" pagesize="10"  style="width:100%" id="arInquiry" sort="list" >

<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Hazmat displayed,For more Records click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Hazmat"/>
  			<display:setProperty name="paging.banner.items_name" value="Hazmats"/>
  			<%
  			String unNumber="";
  			String accountNo="";
  			HazmatMaterial hazmatMaterial=(HazmatMaterial)hazmatList.get(i);
  			unNumber=hazmatMaterial.getUnNumber();
  			 %>
  	<display:column title="Hazmat Number" ><%=unNumber%></display:column>
  	<display:column property="propShipingNumber" title="Proper Shipping Name"></display:column>
  	<display:column property="technicalName" title="Technical Name"></display:column>
  	<display:column title="IMO Class Code" property="imoClssCode"></display:column>
  	<display:column><html:checkbox property="bookcheckbox" value="<%=String.valueOf(hazmatMaterial.getId())%>"/></display:column>
<%i++;

 %>
</display:table>  
<%} %>
  </div>
		   </table>
		   </tr>
		   </table>
		   <html:hidden property="buttonValue"/>
		   <html:hidden property="containerId" value="<%=containerId%>"/>
		   <html:hidden property="bolid" value="<%=bolid%>"/>
		</html:form>
	</body>
</html>

