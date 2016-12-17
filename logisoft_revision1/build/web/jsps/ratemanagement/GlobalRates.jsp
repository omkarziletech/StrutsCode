<%@ page language="java" import="java.text.DateFormat,java.text.DecimalFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.util.DBUtil" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %> 
<%
String path = request.getContextPath();
DBUtil dBUtil=new DBUtil();
List costcodelist=dBUtil.getGenericFCLforTypeOfMove(new Integer(36),"no","yes");
request.setAttribute("costcodelist",costcodelist);
request.setAttribute("unitTypeList",dBUtil.uniTypeList());
request.setAttribute("amendTypeList",dBUtil.getAmendmentTypeList());
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
		<title>JSP for GlobalRatesForm form</title>
	</head>
	<body>
		<html:form action="/globalRates" scope="request">
		<table cellpadding="0" cellspacing="0" class="tableBorderNew" border="0" width="100%">
		<tr class="tableHeadingNew">Global Rates</tr>
			<tr class="textlabels">
				<td style="padding-top: 5px;padding-left: 3px;">
					<table  cellpadding="2" cellspacing="0" border="0" width="100%">
					  	<tr class="textlabels">
					  		<td>Origin Region</td>
					  		<td><input  name="originRegion" id="originRegion"  size="20"/>
					  		    <dojo:autoComplete formId="globalRatesForm" textboxId="originRegion" 
					  		    action="<%=path%>/actions/getChargeCodeDesc.jsp?from=0&tabName=GLOBAL_RATES"/>
					  		</td>
					  		<td>Destination Region</td>
					  		<td><input  name="destinationRegion" id="destinationRegion"  size="20"/>
					  		    <dojo:autoComplete formId="globalRatesForm" textboxId="destinationRegion" 
					  		    action="<%=path%>/actions/getChargeCodeDesc.jsp?from=1&tabName=GLOBAL_RATES"/></td>
					  		<td>Origin</td>
					  		<td><input name="origin"  id="origin"  size="20"   />
					  		 	<dojo:autoComplete formId="globalRatesForm"  textboxId="origin" 
					  		 	 action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=GLOBAL_RATES&from=0"/></td>
					  		
						</tr>
						<tr class="textlabels">
							<td>Destination</td>
					  		<td><input name="destination"  id="destination"  size="20"  />
					  		    <dojo:autoComplete formId="globalRatesForm"  textboxId="destination" 
					  		 	 action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=GLOBAL_RATES&from=1"/></td>
					  		<td>Contact Number</td>
					  		<td><html:text property="contactNumber"></html:text></td>
					  		<td>Commodity Code</td>
					  		<td><input id="commodityCode" name="commodityCode"/>
					  		 <dojo:autoComplete formId="globalRatesForm"  textboxId="commodityCode" 
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
					  		<td>Amount</td>
					  		<td><html:text property="amount"/></td>

						</tr>
						<tr class="textlabels">
							<td>Amendment Type</td>
<%--					  		<td><html:text property="amendmentType"></html:text></td>--%>
					  		<td><html:select style="width:145px;" property="amendmentType" >
					  				<html:optionsCollection name="amendTypeList"/>
					  		    </html:select></td>
					  		<td>Date Effective</td>
					  		<td><html:text property="dateEffective" styleId="txtItemcreated" size="17"></html:text>
					  			<img src="<%=path%>/img/CalendarIco.gif" alt="cal"
								align="middle" id="itemcreated"
								onmousedown="insertDateFromCalendar(this.id,0);" /></td>
					  		<td>Expired Date</td>
					  		<td><html:text property="expiredDate" styleId="txtItemcreated1" size="17"></html:text>
					  			<img src="<%=path%>/img/CalendarIco.gif" alt="cal"
								align="middle" id="itemcreated1"
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

