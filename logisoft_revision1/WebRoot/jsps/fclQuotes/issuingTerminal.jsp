<%@ page language="java" pageEncoding="ISO-8859-1"
	import="com.gp.cong.logisoft.domain.RefTerminal"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%
	String path = request.getContextPath();
	String link = "";
	String button = "";
	if (session.getAttribute("buttonValue") != null) {
		button = (String) session.getAttribute("buttonValue");
	}
	String editPath = path + "/issuingTerminal.do";
	List issuingList = null;
	if (session.getAttribute("issueTerminalList") != null) {
		issuingList = (List) session.getAttribute("issueTerminalList");
	}
	if (request.getAttribute("buttonValue") != null
			&& request.getAttribute("buttonValue").equals(
			"QuotationIssuing")) {
%>
<script type="text/javascript">
	parent.parent.GB_hide();
	parent.parent.getIssuingTerminal('${issueList[0]}');
</script>
<%
}
%>


<html>
	<head>
		<title>JSP for IssuingTerminalForm form</title>
	</head>
	<%@include file="../includes/baseResources.jsp"%>
	<script type="text/javascript">
	function getClose(){
		parent.parent.GB_hide();
	}
	function getGo(){
		if(document.issuingTerminalForm.issuingTerminal.value=="" ){
			alert("Please enter IssuingTerminal");
			return;
		}
		document.issuingTerminalForm.button.value="Go";
		document.issuingTerminalForm.submit();
	}
	function setFocus(){
		 setTimeout("set()",150);
		}
		function set(){
			document.getElementById('issuingTerminal').focus();
		}
	</script>

	<body class="whitebackgrnd" onload="setFocus()">
		<html:form action="/issuingTerminal" scope="request">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew ">
				<tr class="tableHeadingNew">
					<td width="100%">
						<table>
							<tr>
								<td width="100%" class="textlabelsBold">Terminal Location Search</td>
							</tr>
						</table>
					</td>
					<td>
						<table>
							<tr></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr class="textlabelsBold">
								<td>
									Terminal Location
								</td>
								<td>
									<html:text property="issuingTerminal" styleClass="textlabelsBoldForTextBox" styleId="issuingTerminal"></html:text>
								</td>
								<td>
									<input type="button" class="buttonStyleNew" value="Go" onclick="getGo()">
								</td>
							</tr>
						</table>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew ">
				<tr class="tableHeadingNew">
					<td width="100%">
						<table>
							<tr>
								<td width="100%" class="textlabelsBold">
									List of Terminals Locations
								</td>
							</tr>
						</table>
					</td>
					<td>
						<table>
							<tr>
							</tr>
						</table>
					</td>
				</tr>
<%
	if(issuingList!=null && issuingList.size()>0){
 	int i=0; 
 
%>
<div id="divtablesty1" style="height:80%;">
<display:table name="<%=issuingList%>" class="displaytagstyleNew" pagesize="30"  style="width:100%" id="arInquiry" sort="list" >

<display:setProperty name="paging.banner.some_items_found">
    				<span class="pagebanner">
    					<font color="blue">{0}</font> Issuing Terminals displayed,For more Records click on page numbers.
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
			<display:setProperty name="paging.banner.item_name" value="ssuing Terminals"/>
  			<display:setProperty name="paging.banner.items_name" value="ssuing Terminals"/>
  			<%
  			String issuingTerminal="";
  			RefTerminal refTerminal=(RefTerminal)issuingList.get(i);
  				if(refTerminal.getTerminalLocation()!=null){
  					issuingTerminal=refTerminal.getTerminalLocation();
  				}
  			link=editPath+"?paramId="+i+"&button="+button+"&issuinTerminal="+issuingTerminal;
  			 %>
  	<display:column title="Issuing Terminal" ><a href="<%=link%>" ><%=issuingTerminal%></a></display:column>
  
<%i++; %>
</display:table>  
<%} %>
  </div>


</table>
<html:hidden property="button"/>
 <input type="hidden" name="action" value="<%=button%>"/>
		</html:form>
	</body>
	
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

