<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
 %>
<html> 
	<head>
		<title>JSP for RateQuickReviewForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
		<script language="javascript" src="<%=path%>/js/common.js"></script>

<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    dojo.require("dojo.io.*");
			dojo.require("dojo.event.*");
			dojo.require("dojo.html.*");
			start = function(){
				loadFunction();
			}
		    dojo.addOnLoad(start);
		</script>
		<script type="text/javascript">
		function popupAddRates(windows){
		document.rateQuickReviewform.buttonValue.value="go";
		document.rateQuickReviewform.submit();
		}
		</script>
		<%@include file="../includes/resources.jsp" %> 
	</head>
	<body>
		<html:form action="/rateQuickReview" scope="request">
			<table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew">
			Rates Quick Review
			</tr>
			<tr class="textlabelsBold">
			<td>
			<table border="0" width="100%" cellpadding="3" > 
			<tr class="textlabelsBold">
			<td>Destination</td>
			<td>Origin</td>
			<td>Commodity</td></tr>
			
			<tr class="textlabelsBold"><td>
			   		 <input Class="textlabelsBoldForTextBox" name="portofDischarge" id="portofDischarge" size="22" 
					 />    
		 			 <span class="hotspot" onmouseover="tooltip.show('<strong>Search Destination</strong>',null,event);" onmouseout="tooltip.hide();">
				       <%--<img src="<%=path%>/img/icons/display.gif" 
				          onclick="return GB_show('Destination Search','<%=path%>/searchquotation.do?buttonValue=searchPort&textName=portofDischarge&from=destination&fclOrigin='+document.rateQuickReviewForm.isTerminal.value+'&NonRated='+'',250,600);" />
				     --%><dojo:autoComplete formId="rateQuickReviewform" id="destiAutoTextbox" textboxId="portofDischarge" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7"/></span>
	      	    	</td>
	      	    	<td id="isTerminal1">
        	           <input Class="textlabelsBoldForTextBox" name="isTerminal" id="isTerminal" size="22" />    
         		       <span class="hotspot" onmouseover="tooltip.show('<strong>Search Orgin</strong>',null,event);" onmouseout="tooltip.hide();"><%--
					   <img src="<%=path%>/img/icons/display.gif" onclick="displayOrigins()" />
					  
    		           --%><dojo:autoComplete formId="rateQuickReviewform" id="autoTextbox" textboxId="isTerminal" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6"/></span>
    		    	</td>
    		    	 <td><input Class="textlabelsBoldForTextBox" name="commcode" id="commcode" maxlength= "7" size="22" />
                    	<input type="button" id="getRates" onClick="return popupAddRates('windows')" Value="Rates" style="width: 52px;" class="buttonStyleNew"/>     
                    	<dojo:autoComplete formId="rateQuickReviewform" textboxId="commcode" action="<%=path%>/actions/getChargeCode.jsp?tabName=QUOTE"/>
             
                	</td></tr>
			</table>
			</td>
			</tr>
			</table>
			<html:hidden property="buttonValue"/>
		</html:form>
	</body>
</html>

