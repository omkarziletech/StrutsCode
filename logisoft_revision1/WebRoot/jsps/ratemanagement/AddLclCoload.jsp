<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.LCLColoadMaster"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%
String path = request.getContextPath();
String records="";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
boolean enable=false;;
if(session.getAttribute("setLCLColoadTabEnable")!=null)
{
				
	if(session.getAttribute("setLCLColoadTabEnable").equals("enable"))
	{
		enable = true;
	}
	else
	{
		enable = false;
	}		
}
String terminalNumber="";
String terminalName="";
String destSheduleNumber="";
String destAirportname="";
String comCode="";
String comDesc="";
LCLColoadMaster lclColoadMaster=new LCLColoadMaster();

if(session.getAttribute("addlclColoadMaster")!=null)
{
	lclColoadMaster=(LCLColoadMaster)session.getAttribute("addlclColoadMaster");
	terminalNumber=lclColoadMaster.getOriginTerminal();
	terminalName=lclColoadMaster.getOriginTerminalName();
	destSheduleNumber=lclColoadMaster.getDestinationPort();
	destAirportname=lclColoadMaster.getDestinationPortName();
	comCode=lclColoadMaster.getCommodityCode();
	comDesc=lclColoadMaster.getCommodityCodeName();
	records="Norecords";

}
String result="";
String message="";
String modify="";
if(session.getAttribute("message")!=null){
message=(String)session.getAttribute("message");
}

if(session.getAttribute("lclcoloadrate")!=null){
	result=(String)session.getAttribute("lclcoloadrate");
	}
	modify = (String) session.getAttribute("modifyforlclcoloadRates");
	if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}
%> 
<html> 
	<head>
		<title>JSP for AddLclColoadForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function deleteAgss1()
		{
			alert(parent.mainFrame.document.agssFrame);
			if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.aGSSEditForm!=undefined)
 			{
	
		 			parent.mainFrame.document.agssFrame.aGSSEditForm.buttonValue.value="delete";
		 			parent.mainFrame.document.agssFrame.aGSSEditForm.submit();
	
	
 			}
			document.addLclColoadForm.buttonValue.value="edit";
	 		document.addLclColoadForm.submit();
	 	}
		function save1(val,val1){
		   flag=false;
			var netie=navigator.appName;
			if(val1=="edit"){
				document.addLclColoadForm.buttonValue.value="edit";
 			}
 			else if(val1=="save"){
 				document.addLclColoadForm.buttonValue.value="save";
 			}
 			if(netie=="Microsoft Internet Explorer"){
	   			if(val=="true")
	   			 {
	 				
	 				if(!flag)
 					{
 					if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.aGSSEditForm!=undefined)
		 			{
		 			parent.mainFrame.document.agssFrame.aGSSEditForm.buttonValue.value="edit";
		 			parent.mainFrame.document.agssFrame.aGSSEditForm.submit();
		 			}
		 			
 					if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.aGSSForm!=undefined)
 						{
 							if(parent.mainFrame.document.agssFrame.aGSSForm.charge!=undefined && parent.mainFrame.document.agssFrame.aGSSForm.charge.value!="")
   							{
					 			alert("Please select Accessorial & General Standard Charges  AddToList  button");
					 			return;
		 			 		}
 							parent.mainFrame.document.agssFrame.aGSSForm.submit();
 						}
 					if(parent.mainFrame.document.documentFrame!=undefined && parent.mainFrame.document.documentFrame.documentChargesForm!=undefined)
		 			{
		 			parent.mainFrame.document.documentFrame.documentChargesForm.submit();
		 			}
 					
 					}
	 				//parent.mainFrame.document.agssFrame.aGSSForm.submit();
	 				//parent.mainFrame.document.documentFrame.documentChargesForm.submit();
 					
 					}
	 			else{
		 			
		 			if(parent.mainFrame.document.csssFrame.addLclColoadCommodityForm.charge!=undefined && parent.mainFrame.document.csssFrame.addLclColoadCommodityForm.charge.value!="" )
 					{
		 			alert("Please select Commodity Specific Accesorial Charges  AddToList button");
		 			return;
		 			}
		 			parent.mainFrame.document.airDetailsFrame.addLclColaodDetailsForm.submit();
 					}
 			}
 			else if(netie=="Netscape")
 			{
 			if(val=="false")
 			   {
   				parent.mainFrame.document.getElementById('airDetailsFrame').contentDocument.addLclColaodDetailsForm.submit();
   				}
   			}
   				setTimeout("aa()",1500);
 				//document.addLclColoadForm.submit();
			}
		
		function aa()
		{
		document.addLclColoadForm.submit();
		}
		
		
		function cancel2(val1,val2,val3)
		{
			 if(val1!=""){
		 	var result = confirm("Do  you want to Add Rates ??");
		 	
		 	 if(result)
				{   
					save1(val2,val3);
	   			}	
			 else
			  {
			  		
	   				document.addLclColoadForm.buttonValue.value="savecancel";
	   				document.addLclColoadForm.submit();
			  }
			  }
			
		}
		function delete1()	{
			document.addLclColoadForm.buttonValue.value="delete";
 			document.addLclColoadForm.submit();
			}
		function cancel1(val){
			if(val==0 || val==3){
			
			document.addLclColoadForm.buttonValue.value="cancelview";
			}
			else{
			document.addLclColoadForm.buttonValue.value="cancel";
				}
 			document.addLclColoadForm.submit();
			}
	  function confirmnote()
	   {
		 document.addLclColoadForm.buttonValue.value="note";
    	 document.addLclColoadForm.submit();
   	   }	
 			
 	</script>
	</head>
	<body class="whitebackgrnd" onLoad="">
		<html:form action="/addLclCoload"  styleId="addLclColoadId"  name="addLclColoadForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AddLclColoadForm" scope="request">
			
			<table width=100%>
			<tr>
    			<td  class="textlabels">Org Trm</td>
    			<td ><html:text property="terminalNumber" value="<%=terminalNumber%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td  class="textlabels">OriginTrm Name</td>
    			<td ><html:text property="terminalName" value="<%=terminalName%>" readonly="true" styleClass="areahighlightgrey"/></td>
    		
    		</tr>
    		<tr>	
    			<td  class="textlabels">Dest Port </td>
    			<td ><html:text property="destSheduleNumber" value="<%=destSheduleNumber%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td  class="textlabels">Dest Port Name</td>
    			<td ><html:text property="destAirportname" value="<%=destAirportname%>" readonly="true" styleClass="areahighlightgrey"/></td>
    	  </tr>
    	  <tr>	
    			<td  class="textlabels"><span class="textlabels"> Com Code</span> </td>
    			<td><html:text property="comCode"  value="<%=comCode%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td  class="textlabels"><span class="textlabels">Com Description</span> </td>
    			<td><html:text property="comDescription" value="<%=comDesc%>" readonly="true" styleClass="areahighlightgrey"/></td>
                 </tr><tr>
    			<td colspan="4" align="center" style="padding-top:15px;">
<%
 if(result!=null&&result.equals("save")){

%>
     <input type="button" value="Save" onclick="save1('<%=enable%>','<%=result%>')" id="save" class="buttonStyleNew" />
	 <input type="button" value="Go Back" onclick="cancel2('<%=records%>','<%=enable%>','<%=result%>')" id="cancel"  class="buttonStyleNew" />
		

<% }else if(result!=null&&result.equals("edit")){
%>		
	
	    <input type="button" value="Save" onclick="save1('<%=enable%>','<%=result%>')" id="save" class="buttonStyleNew" />
	    <input type="button" value="Go Back" onclick="cancel1('<%=modify%>')" id="cancel""  class="buttonStyleNew" />
		 <input type="button" class="buttonStyleNew" value="Notes" onclick="confirmnote()" />
		 <input type="button" value="Delete" onclick="delete1()" id="delete" class="buttonStyleNew"/>
<%
	}
	
if(session.getAttribute("message")!=null){
session.removeAttribute("message");}%>
    		</tr>
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue"/>
    		<%--<script>parent.parent.parent.parent.makeFormBorderless(document.getElementById("addLclColoadId"));</script>	
		--%></html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


