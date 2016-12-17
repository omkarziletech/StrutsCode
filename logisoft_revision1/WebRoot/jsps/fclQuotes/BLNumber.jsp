<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.BookingfclUnits,java.text.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

 DBUtil dbUtil=new DBUtil();
 request.setAttribute("unitTypeList",dbUtil.uniTypeList());
%>
<html> 
	<head>
		<title>JSP for QuoteChargeForm form</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
 
<%@include file="../includes/baseResources.jsp" %>
  	<script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
  	<script type='text/javascript' src='/logisoft/dwr/util.js'></script>
	<script type='text/javascript' src='/logisoft/dwr/interface/FclBlDAO.js'></script>
	<script language="javascript" src="<%=path%>/js/common.js" ></script>
<script language="javascript" type="text/javascript">
	function converttobl(){
		if(document.EditBookingsForm.blNumber.value==""){
			alert("Please Enter BL Number...");
			return;
		}else{
			var itemCode = document.getElementById('blNumber').value;
	  		FclBlDAO.findBolIdObject(itemCode,loadItemCode);
		}
}
function loadItemCode(data) {
	 	if(data){
		 	 document.getElementById('blNumber').value="";
	 		 document.getElementById('a').style.visibility="visible";
	 		 document.getElementById('a').className="bodybackgrnd";
	 	}else{
	 	    var val1= document.EditBookingsForm.blNumber.value; 
	 		parent.parent.converttobl(val1);
	 	  //document.EditBookingsForm.buttonValue.value="converttobl";
		 // document.EditBookingsForm.submit();
	 	}
	 }
</script>

	</head>
	<body class="whitebackgrnd">
<html:form  action="/editBooks" styleId="editbook" name="EditBookingsForm" 
type="com.gp.cvst.logisoft.struts.form.EditBookingsForm" scope="request">

<table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0">
	<tr class="tableHeadingNew"><td>FCL BL NUMBER </td>
	<td align="right" colspan="2">
	
	 <input type="button" value="Save" id="conbl" class="buttonStyleNew" onclick="converttobl()" style="width:80px"/>
	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
	<td width="20%" class="style2">
		Bl Number
	</td>
	<td>
	
		<html:text property="blNumber" value=""></html:text>
	</td>
	<td class="style2"><div id="a" style="visibility:hidden;border:0;"><font color="blue"><b>Please give different BL Number</b></font>
</div></td>
	</tr>
</table>
<html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
		
	</body>
		<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

