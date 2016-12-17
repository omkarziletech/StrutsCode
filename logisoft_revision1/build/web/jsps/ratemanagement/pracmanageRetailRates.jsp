<% 
String path = request.getContextPath();%>
<html> 
	<head>
		
		<title>JSP for ManageRetailRatesForm form</title>
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		
	</head>
	<body class="whitebackgrnd">
		<form action="/manageRetailRates" name="manageRetailRatesForm">
	<tr>
  	</tr>
  	<tr>&nbsp;</tr>
  	<tr>
      <td>
         <table width="100%" border="0"><tr class="style2"><td>Org Trm&nbsp;   </td>
    </tr>
   </table>
   </td>
      <td>
   	      <input name="terminalNumber" id="terminalNumber" size="2"   maxlength="2" "/>
	      <dojo:autoComplete formId="manageRetailRatesForm" textboxId="terminalNumber" action="<%=path%>/actions/getTerminal.jsp?tabName=PRAC_MANAGE_RETAIL_RATES&from=0"/>
    	 <br>
    	   <input  name="terminalName"  />
		<br>
    		<input  name="destSheduleNumber" id="destSheduleNumber" size="5" maxlength="5"   />
	      	<dojo:autoComplete  formId="manageRetailRatesForm" textboxId="destSheduleNumber"  action="<%=path%>/actions/getPorts.jsp?tabName=PRAC_MANAGE_RETAIL_RATES"/>
	    <br>
    	<input name="destAirportname" /></td>
    	<br>
    	<td ><input name="comCode"  id="comCode" maxlength="6"  size="6"   />
    			<dojo:autoComplete formId="manageRetailRatesForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=PRAC_MANAGE_RETAIL_RATES"/></td>
    <br>
   			<input  name="comDescription"  />
  	<form>
	</body>
</html>

