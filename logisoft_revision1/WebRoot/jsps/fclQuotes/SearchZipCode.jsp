<%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*,com.gp.cvst.logisoft.domain.Zipcode"%>
<%@page import="com.gp.cvst.logisoft.hibernate.dao.ZipCodeDAO"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
 <%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List zipCodeList=new ArrayList(); 
if(session.getAttribute("zipCodeList")!=null){
zipCodeList=(List)session.getAttribute("zipCodeList");
}
String link="";
ZipCodeDAO zipCodeDAO=new ZipCodeDAO();
String editPath=path+"/searchZipCode.do";
String originName=(String)request.getParameter("doorOriginName");

if(request.getParameter("doorOriginName")!=null){
String[] originNameArray=originName.split("/");
originName=originNameArray[0];
String state = originNameArray.length > 2 ? originNameArray[1] : "";
zipCodeList=zipCodeDAO.findZipCodeByState(originName,state);
session.setAttribute("zipCodeList",zipCodeList);
}
if(request.getAttribute("zipCode")!=null){
%>
<script type="text/javascript">
parent.parent.GB_hide();
parent.parent.setZipCode('${zipCode}');
</script>
<%
}
%>
<html> 
	<head>
		<title>JSP for SearchZipCodeForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
		<script language="javascript" type="text/javascript">
		function searchZip(){
			document.searchZipCodeForm.buttonValue.value="search";
			document.searchZipCodeForm.submit();
		}
		function setFocus(){
		 setTimeout("set()",150);
		}
		function set(){
			document.getElementById('city').focus();
		}
		</script>
	</head>
	 <%@include file="../includes/resources.jsp" %>
	<body class="whitebackgrnd" onload="setFocus()">
		<html:form action="/searchZipCode" type="com.gp.cvst.logisoft.struts.form.SearchZipCodeForm" scope="request">
		<table cellspacing="0" cellpadding="0"  width="100%" border="0" class="tableBorderNew">
		     <tr class="tableHeadingNew">Search ZipCode</tr>
		     <tr>
		     	  <td style="padding-top: 5px;padding-bottom: 5px;">
		     	       <table  cellspacing="0" cellpadding="0"  width="100%" border="0">
		     	           <tr class="textlabelsBold">
		     	           		<td style="padding-left: 5px;">City</td>
		     	           		<td><html:text property="city" styleClass="textlabelsBold" value="<%=originName %>"></html:text></td>
		     	           		<td>Zip Code</td>
		     	           		<td ><html:text property="zipCode" size="5" styleClass="textlabelsBold"/></td>
		     	           		<td align="justify"><input type="button" class="buttonStyleNew" value="Search" name="search" onclick="searchZip()" align="right"></td>
		     	           </tr>
		     	       </table>
		     	  </td>
		     </tr>
	   </table>
	   <td style="height: 5px;">&nbsp;</td>
	   <table cellspacing="0" cellpadding="0"  width="100%" border="0" class="tableBorderNew" >
		     <tr class="tableHeadingNew">List of ZipCodes</tr>
		     <tr>
		     	  <td>
		     	       <table  cellspacing="0" cellpadding="0"  width="100%" border="0">
		     	       
		     	        <tr >
		     	       		<td style="padding-top: 5px;padding-bottom: 5px;">
		     	       			
		     	       	
				     	          <%
										if(zipCodeList!=null && zipCodeList.size()>0){
		 								int i=0; 
								  %>
									<div id="divtablesty1" style="width: 50%;">
									<display:table name="<%=zipCodeList%>" class="displaytagstyleNew" pagesize="30"  id="arInquiry" sort="list" >
									<display:setProperty name="paging.banner.some_items_found">
		    							<span class="pagebanner">
		    								<font color="blue">{0}</font> ZipCode displayed,For more Records click on page numbers.
		    								
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
		  			 				<display:setProperty name="basic.msg.empty_list">
		  			 						<span class="pagebanner">
												No Records Found.
											</span>	
									</display:setProperty>
									<display:setProperty name="paging.banner.placement" value="bottom" />
									<display:setProperty name="paging.banner.item_name" value="ZipCode"/>
		  							<display:setProperty name="paging.banner.items_name" value="ZipCodes"/>
		  							<%
		  									String city="";
		  									Zipcode zipcode=(Zipcode)zipCodeList.get(i);
		  									if(zipcode.getCity()!=null){
		  										city=zipcode.getCity();
		  									}
		  									
		  									link=editPath+"?paramId="+i;
		  			 				%>
		  							<display:column title="City" ><a href="<%=link%>" ><%=city%></a></display:column>
		  							<display:column property="zip" title="Zip"></display:column>
		  							<display:column property="state" title="State"></display:column>
									<%
											i++;
									%>
									</display:table>  
									<%
										} 
									%>
		  							</div>
		  					 </td>
		     	          </tr>
		     	       </table>
		     	  </td>
		     </tr>
		
		</table>

		<html:hidden property="buttonValue"/>
		</html:form>
	</body>
</html>

