<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.*,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.domain.AirWeightRangesRates,com.gp.cong.logisoft.domain.GenericCode,java.text.DateFormat,java.text.SimpleDateFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();

java.util.Date date = new java.util.Date();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat df = new DecimalFormat("0.00");
String dateStr="";
String userdisplay="";
AirWeightRangesRates airWeightRangesRates=null;

List weightRangeList=new ArrayList();
List airDetailsList = null;
if (session.getAttribute("airdetailsAdd") != null)
{
	airDetailsList = (List) session.getAttribute("airdetailsAdd");
}
dateStr = dateFormat.format(date);
User user=null;
String weight="0";
String GR="",GM="",ER="",EM="",DR="",DM="",WC="",CD="";

if(session.getAttribute("loginuser")!=null)
{
	user=(User)session.getAttribute("loginuser");
}
if(userdisplay != null)
{
	userdisplay=user.getFirstName();
}

if (session.getAttribute("airrangedetails") != null) {


airWeightRangesRates=(AirWeightRangesRates)session.getAttribute("airrangedetails");
if(airWeightRangesRates.getWeightRange()!=null)
{
weight=airWeightRangesRates.getWeightRange().getId().toString();
if(airWeightRangesRates.getGeneralRate()!=null){
GR=df.format(airWeightRangesRates.getGeneralRate());
}
if(airWeightRangesRates.getGeneralMinAmt()!=null){
GM=df.format(airWeightRangesRates.getGeneralMinAmt());
}
if(airWeightRangesRates.getExpressRate()!=null){
ER=df.format(airWeightRangesRates.getExpressRate());
}
if(airWeightRangesRates.getExpressMinAmt()!=null){
EM=df.format(airWeightRangesRates.getExpressMinAmt());
}
if(airWeightRangesRates.getDeferredRate()!=null){
DR=df.format(airWeightRangesRates.getDeferredRate());
}
if(airWeightRangesRates.getDeferredMinAmt()!=null){
DM=df.format(airWeightRangesRates.getDeferredMinAmt());
}
if(airWeightRangesRates.getChangedDate()!=null)
{
CD=dateFormat.format(airWeightRangesRates.getChangedDate());
}
if(airWeightRangesRates.getWhoChanged()!=null){
WC=airWeightRangesRates.getWhoChanged();
}
}
	
		}
		
if(weightRangeList != null)
{	
	

	weightRangeList=dbUtil.getWeightRangeListforedit(new Integer(31),"yes","Select Weight Range",airDetailsList,weight);
	request.setAttribute("weightRangeList",weightRangeList);
}
String modify="";
modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}

%>
 
<html> 
	<head>
		<title>JSP for AirDetailsForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function addAirDetails()
		{
		if(document.airDetailsForm.weightRange.value=="0")
		{
			alert("Please select Wight Range");
			return;
		}
		
			document.airDetailsForm.buttonValue.value="update";
  			document.airDetailsForm.submit();
		}
		function delete1()
		{
			
			document.airDetailsForm.buttonValue.value="delete";
 			document.airDetailsForm.submit();
		}
		function cancel1()
		{
			document.airDetailsForm.buttonValue.value="cancel";
 			document.airDetailsForm.submit();
		}
		function confirmdelete(obj)
		{
		 	var rowindex=obj.parentNode.parentNode.rowIndex;
	 		var x=document.getElementById('airweightratestable').rows[rowindex].cells;	
	   		document.airDetailsForm.index.value=rowindex-1;
			document.airDetailsForm.buttonValue.value="delete";
    		var result = confirm("Are you sure you want to delete this Weight Range");
			if(result)
			{
   				document.airDetailsForm.submit();
   			}	
   		}
   		function disabled(val1)
   {
	if(val1 == 0 || val1== 3)
	{
        var imgs = document.getElementsByTagName('img');
       
   		for(var k=0; k<imgs.length; k++)
   		{
   		if(imgs[k].id!="cancel")
   			{
   		    imgs[k].style.visibility = 'hidden';
   		 }
   		}
   		var input = document.getElementsByTagName("input");
   		for(i=0; i<input.length; i++)
	 	{
	  			if(input[i].name!="buttonValue")
	 			   {
	 				if(input[i].name=="exclude" || input[i].name=="asFrfgted" || input[i].name=="standard"){
	 					input[i].disabled=true;
	 				}
	 				else{
	 				input[i].className="areahighlightgreysmall";
	  				input[i].readOnly=true;
	  				}
	  			}	
	  			
	  		
  	 	}
  	 	var textarea = document.getElementsByTagName("textarea");
  	 	for(i=0; i<textarea.length; i++)
	 	{
	 			textarea[i].readOnly=true;
	  			
	 					
	  	}
   		var select = document.getElementsByTagName("select");
   		
   		for(i=0; i<select.length; i++)
	 	{
	 		select[i].disabled=true;
			select[i].style.backgroundColor="blue";
	  		
  	 	}
  	 	document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 if(val1 == 1)
  	 {
  	 	  document.getElementById("delete").style.visibility = 'hidden';
  	 }
  	 		
    }
	</script>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<html:form action="/airDetails" name="airDetailsForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AirDetailsForm" scope="request">
		<div align="right">
	<%if(modify!=null && modify.equals("3")){ %>
		
		   <input type="button" value="Go Back" class="buttonStyleNew" id="cancel" onclick="cancel1()"/>
	<%}else {%>
  		   <input type="button" value="Go Back" class="buttonStyleNew" id="cancel" onclick="addAirDetails()"/>
  			<%} %>
  		   <input type="button" value="Delete" class="buttonStyleNew" id="delete" onclick="delete1()"/>
  	</div>		   
  			
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew" height="90%">Air Details</tr> 
	<tr>
 	<td>
		
		<table width="100%">
    	<tr class="textlabels">
      		<td></td>
      		<td ><div align="center">General</div></td>
      		
      		<td ><div align="center">Express</div></td>
      		
      		<td><div align="center">Deferred</div></td>
      		<td > <div align="center"></div></td>
      		<td ><div align="center"></div></td>
      		
      		<td ></td>
    	</tr>
    	<tr class="textlabels">

      		<tr class="textlabels">
      		<td ><div align="center">Weight Range</div></td>
      		<td><table ><tr class="textlabels">
      			<td >Rate/LB</td>
      			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      			<td >Min Amt </td>
      		</tr> </table> </td>
      		<td><table><tr class="textlabels">
      			<td >Rate/LB</td>
      			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      			<td >Min Amt </td>
      		</tr> </table> </td>
      		<td><table><tr class="textlabels">
      			<td >Rate/LB</td>
      			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      			<td >Min Amt </td>
      		</tr> </table> </td>
      		
      		
      		
      		<td></td>
    	</tr>
    	<tr class="textlabels" >
    	<TD><html:select property="weightRange" styleClass="verysmalldropdownStyle" value="<%=weight%>">
      								   <html:optionsCollection name="weightRangeList"/>     
		</html:select></TD>
      		<td><table><tr>
      			<td ><html:text property="generalRate" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"   value="<%=GR%>"/></td>
      			<td ><html:text property="generalAmt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  value="<%=GM%>"/></td>
      		</tr> </table> </td>
      		<td><table><tr>
      			<td ><html:text property="expressRate" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7" value="<%=ER%>"/></td>
      			<td ><html:text property="expressAmt" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7" value="<%=EM%>"/></td>
      		</tr> </table> </td>
      		<td><table><tr>
      			<td ><html:text property="deferredRate" onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7"  value="<%=DR%>"/></td>
      			<td><html:text property="deferredAmt"  onkeypress="check(this,4)" onblur="checkdec(this)" maxlength="7" size="7" value="<%=DM%>"/></td>
      		</tr> </table> </td>
      		
      		
      		
  


      		</tr>
    	
		</table>

    	
	    <html:hidden property="buttonValue" styleId="buttonValue"/>	
		<html:hidden property="index" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

