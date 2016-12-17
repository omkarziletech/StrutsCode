<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.UniversalMaster"%>
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
UniversalMaster universalMaster=new UniversalMaster();

if(session.getAttribute("addUniversalMaster")!=null)
{
universalMaster=(UniversalMaster)session.getAttribute("addUniversalMaster");
if(universalMaster.getOriginTerminal()!=null)
{
terminalNumber=universalMaster.getOriginTerminal().getTrmnum();
terminalName=universalMaster.getOriginTerminal().getTerminalLocation();
}
if(universalMaster.getDestinationPort()!=null)
{
destSheduleNumber=universalMaster.getDestinationPort().getShedulenumber();
destAirportname=universalMaster.getDestinationPort().getPortname();
}
if(universalMaster.getCommodityCode()!=null)
{
comCode=universalMaster.getCommodityCode().getCode();
comDesc=universalMaster.getCommodityCode().getCodedesc();
}
records="Norecords";
}

String result="";
String message="";
String modify="";
if(session.getAttribute("message")!=null){
message=(String)session.getAttribute("message");
}

if(session.getAttribute("universaltabs")!=null){
	result=(String)session.getAttribute("universaltabs");
	}
	modify = (String) session.getAttribute("modifyforlclcoloadRates");
	if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}
String view="";
if(session.getAttribute("view")!=null){
view = (String)session.getAttribute("view");
}
%> 
<html> 
	<head>
		<title>JSP for AddLclColoadForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		function save1(value,value1){
		   alert("ttt");
			var netie=navigator.appName;
			if(value1=="edit"){
				document.addUniversalForm.buttonValue.value="edit";
			
 			}
 			else if(value1=="save"){
 			alert("iiiii");
 			document.addUniversalForm.buttonValue.value="save";
 			
 			}
 			/*if(netie=="Microsoft Internet Explorer")
   			{
 					parent.uniFrame.document.flateFrame.universalFlateRateForm.submit();
	 				parent.uniFrame.document.unicommFrame.universalCommodityForm.submit();
	 				parent.uniFrame.document.importAir.universalAirRateForm.submit();
	 				parent.uniFrame.document.insurancechrg.universalInsuranceForm.submit();
	 			
			}
			else if(netie=="Netscape")
			{
			        parent.uniFrame.document.flateFrame.universalFlateRateForm.submit();
	 				parent.uniFrame.document.commFrame.universalCommodityForm.submit();
	 				parent.uniFrame.document.importAir.universalAirRateForm.submit();
	 				parent.uniFrame.document.insurancechrg.universalInsuranceForm.submit();
			}*/
				document.addUniversalForm.submit();
		}

		function disabled(val1,val2) {
		
			if(val1!="" &&(val1 == 0 || val1== 3))
			{
		  	  	 var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id != "cancel"&&imgs[k].id != "note")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
		   		var input = document.getElementsByTagName("input");
			   		for(i=0; i<input.length; i++)	{
			  			input[i].readOnly=true;
					 	}
		  	 	var textarea = document.getElementsByTagName("textarea");
			  	 	for(i=0; i<textarea.length; i++){
			 			textarea[i].readOnly=true;		 					
					  	}
	   			var select = document.getElementsByTagName("select");
	   		   		for(i=0; i<select.length; i++)	{
				 		select[i].disabled=true;
						select[i].style.backgroundColor="blue";
		  		  	 	}
		  		  	 	document.getElementById("delete").style.visibility = 'hidden';
		  		  	 	document.getElementById("save").style.visibility = 'hidden';
	  	 		}
		  	 if(val1 == 1)	 {
	  	 		 	  document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 	if(val1==3 && val2!=""){
				alert(val2);
				}		
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
			  		
	   				document.addUniversalForm.buttonValue.value="savecancel";
	   				document.addUniversalForm.submit();
			  }
			  }
				
		
 			
		}
		function delete1()	{
			document.addUniversalForm.buttonValue.value="delete";
 			document.addUniversalForm.submit();
			}
		function cancel1(val){
			if(val==0 || val==3){
			
			document.addUniversalForm.buttonValue.value="cancelview";
			}
			else{
			document.addUniversalForm.buttonValue.value="cancel";
				}
 			document.addUniversalForm.submit();
			}
			function confirmnote()
	{
		
		document.addUniversalForm.buttonValue.value="note";
    	document.addUniversalForm.submit();
   	}	
 			</script>

			
		    
		
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
		<html:form action="/addUniversal" styleId="AddUniversal" name="addUniversalForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AddUniversalForm" scope="request">
			
			<table width=100% class="tableBorderNew" cellpadding="0" cellspacing="0">
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
    			<td ><html:text property="comCode"  value="<%=comCode%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td  class="textlabels"><span class="textlabels">Com Description</span> </td>
    			<td ><html:text property="comDescription" value="<%=comDesc%>" readonly="true" styleClass="areahighlightgrey"/></td>
            </tr>
            <tr>
                <td colspan="4" align="center">
                   <table>
                     <tr>
                       <td>
                       
                       
<% if(result!=null&&result.equals("save")){

%>
 			<td>
 			   <input type="button" value="Save" onclick="save1('<%=enable%>','<%=result%>')" id="save" class="buttonStyleNew" />
 			</td>
    		<td>
    		    <input type="button" value="Go Back" onclick="cancel2('<%=records%>','<%=enable%>','<%=result%>')" id="cancel" class="buttonStyleNew" />
    		</td>

<% }else if(result!=null&&result.equals("edit")){%>
		<td>
		        <input type="button" value="Save" onclick="save1('<%=enable%>','<%=result%>')" id="save" class="buttonStyleNew" />
		</td>
    	<td>
    	 	<input type="button" value="Go Back" onclick="cancel1('<%=modify%>')" id="cancel" class="buttonStyleNew" />
    	 </td>
  		<td>
  		    <input type="button" value="Delete" onclick="delete1()" id="delete" class="buttonStyleNew" />
  		 </td>
  			<td align="right">
  			 	<input type="button" value="Note" onclick="confirmnote()" id="note" class="buttonStyleNew" />
				
			</td>
<%
}if(session.getAttribute("message")!=null){
session.removeAttribute("message");}%>

                     </tr>
                   </table>
                    </td>
    		</tr>
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue"/>	
    		<script>parent.parent.parent.parent.makeFormBorderless(document.getElementById("AddUniversal"));</script>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


