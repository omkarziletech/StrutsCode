<%@ page language="java" pageEncoding="ISO-8859-1" import="java.text.DateFormat,java.text.DecimalFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.util.DBUtil"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
DBUtil dBUtil=new DBUtil();
List costcodelist=dBUtil.getGenericCodeCostList(new Integer(36),"no","Select Cost Code");
request.setAttribute("costcodelist",costcodelist);
request.setAttribute("unitTypeList",dBUtil.uniTypeList());
%> 
<%@include file="../includes/baseResources.jsp" %>
	<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    dojo.addOnLoad( function() {
		    window.refresh });
		</script>
<script type="text/javascript">
</script>
<%@include file="../includes/resources.jsp" %>
<html> 
	<head>
		<title>JSP for HistoryRatesForm form</title>
	</head>
	<body>
		<html:form action="/historyRates" scope="request">
			<table cellpadding="0" cellspacing="0" class="tableBorderNew" border="0" width="100%">
		<tr class="tableHeadingNew">History Rates</tr>
			<tr class="textlabels">
				<td style="padding-top: 5px;padding-left: 3px;">
					<table  cellpadding="2" cellspacing="0" border="0" width="100%">
					  	<tr class="textlabels">
					  		<td>Origin Region</td>
					  		<td><input  name="originRegion" id="originRegion"  size="20"/>
					  		    <dojo:autoComplete formId="historyRatesForm" textboxId="originRegion" 
					  		    action="<%=path%>/actions/getChargeCodeDesc.jsp?from=0&tabName=GLOBAL_RATES"/>
					  		</td>
					  		<td>Destination Region</td>
					  		<td><input  name="destinationRegion" id="destinationRegion"  size="20"/>
					  		    <dojo:autoComplete formId="historyRatesForm" textboxId="destinationRegion" 
					  		    action="<%=path%>/actions/getChargeCodeDesc.jsp?from=1&tabName=GLOBAL_RATES"/></td>
					  		<td>Origin</td>
					  		<td><input name="origin"  id="origin"  size="20"   />
					  		 	<dojo:autoComplete formId="historyRatesForm"  textboxId="origin" 
					  		 	 action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=GLOBAL_RATES&from=0"/></td>
					  		
						</tr>
						<tr class="textlabels">
							<td>Destination</td>
					  		<td><input name="destination"  id="destination"  size="20"  />
					  		    <dojo:autoComplete formId="historyRatesForm"  textboxId="destination" 
					  		 	 action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=GLOBAL_RATES&from=1"/></td>
					  		<td>Contact Number</td>
					  		<td><html:text property="contactNumber"></html:text></td>
					  		<td>Commodity Code</td>
					  		<td><input id="commodityCode" name="commodityCode"/>
					  		 	<dojo:autoComplete formId="historyRatesForm"  textboxId="commodityCode" 
					  		 	 action="<%=path%>/actions/getChargeCode.jsp?tabName=GLOBAL_RATES&from=0"/></td>
					  		
						</tr>
						<tr class="textlabels">
							<td>Unit Type</td>
					  		<td><html:select property="unitType"  style="width:145px;">
      							<html:optionsCollection name="unitTypeList" />
      							</html:select></td>
					  		<td>Cost Code</td>
					  		<td><html:select property="costCode"   style="width:145px;">
		   						<html:optionsCollection name="costcodelist"/>    
		  						</html:select></td>
					  		<td>Expired Date</td>
					  		<td><html:text property="expiredDate" styleId="txtItemcreated2" size="17"></html:text>
					  			<img src="<%=path%>/img/CalendarIco.gif" alt="cal"
								align="middle" id="itemcreated2"
								onmousedown="insertDateFromCalendar(this.id,0);" /></td>

						</tr>
						
						<tr class="textlabels">
					  		<td colspan="8" align="center" style="padding-top: 20px;"><input type="button" class="buttonStyleNew" value="Add">
					  			<input type="button" class="buttonStyleNew" value="Search">
					  		</td>
					  		
						</tr>
		         	</table>
				</td>
			</tr>
		</table>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

