<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.domain.CustAddress,com.gp.cong.logisoft.domain.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String editPath=path+"/vesselLookUp.do";
String link="";
List vesselNameList=null;
String button="";

if(session.getAttribute("buttonValue")!=null){
  button=(String)session.getAttribute("buttonValue");
}

if(session.getAttribute("vesselList")!=null){
  vesselNameList=(List)session.getAttribute("vesselList");
}

if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("VesselInBooking"))
{
%>
<script type="text/javascript">
parent.parent.GB_hide();
	parent.parent.setVesselName('${NewVesselList[0]}');
</script>
<%
}
%>

<html> 
	<head>
		<title>JSP for VesselLookUp form</title>
	</head>
	<%@include file="../includes/baseResources.jsp" %>
	<script type="text/javascript">
	
function getClose(){
		parent.parent.GB_hide();
}

function getGo(){
	if(document.vesselLookUpForm.vesselName.value==""){
		alert("Please enter Vessel Name ");
		return;
	}
	document.vesselLookUpForm.button.value="Go";
	document.vesselLookUpForm.submit();
}
</script>
<body class="whitebackgrnd" >
<html:form action="/vesselLookUp" scope="request">
  <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
	 <tr class="tableHeadingNew">Vessel Search</tr>
     <tr><td><table>
	     <tr class="textlabelsBold">
			<td>Vessel Name</td>
			<td><html:text property="vesselName" styleClass="textlabelsBoldForTextBox"></html:text></td>
			<td><input type="button" class="buttonStyleNew" value="Go" onclick="getGo()"></td>
		</tr>
	 </table></td></tr>
  </table>
    
   <br/> 	
   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    <tr class="tableHeadingNew">List of Vessels</tr>
    <tr><td>
				<%
					 if(vesselNameList!=null && vesselNameList.size()>0){
					    int i=0;
				%>
<div id="divtablesty1">
<display:table name="<%=vesselNameList%>" class="displaytagstyleNew" pagesize="10"  style="width:100%" sort="list" >

			  <display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Vessel Names displayed,For more Records <br>click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="Customer"/>
  			<display:setProperty name="paging.banner.items_name" value="Customers"/>
  			<%
  			String vessel="";
  			GenericCode genericCode=(GenericCode)vesselNameList.get(i);
	  			if(genericCode.getCodedesc()!=null){
	  			  vessel=genericCode.getCodedesc();
	  			}
  			link=editPath+"?paramId="+i+"&button="+button;
  			 %>
  	<display:column title="Vessel Name" ><a href="<%=link%>"><%=vessel%></a></display:column>
<%i++; %>
</display:table>  
<%}%>
</div>
</td></tr>
</table>
<html:hidden property="button"/>
<input type="hidden" name="action" value="<%=button%>"/>
</html:form>
</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>



