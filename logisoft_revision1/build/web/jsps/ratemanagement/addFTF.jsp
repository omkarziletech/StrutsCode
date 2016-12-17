<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.FTFMaster"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
 <%
 String records="";
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
boolean enable=false;;
if(session.getAttribute("setftfTabEnable")!=null)
{
				
	if(session.getAttribute("setftfTabEnable").equals("enable"))
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
FTFMaster ftfMaster=new FTFMaster();

	if(session.getAttribute("addftfMaster")!=null)
{
ftfMaster=(FTFMaster)session.getAttribute("addftfMaster");
	
	terminalNumber=ftfMaster.getOrgTerminal();
	terminalName=ftfMaster.getOrgTerminalName();
	destSheduleNumber=ftfMaster.getDestPort();
	destAirportname=ftfMaster.getDestPortName();
	comCode=ftfMaster.getComCode();
	comDesc=ftfMaster.getComCodeName();

records="Norecords";
}

String result="";
String message="";
String modify="";
if(session.getAttribute("message")!=null){
message=(String)session.getAttribute("message");
}

if(session.getAttribute("ftfrate")!=null){
	result=(String)session.getAttribute("ftfrate");
	}
	modify = (String) session.getAttribute("modifyforftfRates");
	if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}
%> 
 
<html> 
	<head>
		<title>JSP for AddFTFForm form</title>
<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		
		function deleteAgss1()
		{
			
			if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.editAgscFTFForm!=undefined)
 			{
	 			
	 			parent.mainFrame.document.agssFrame.editAgscFTFForm.buttonValue.value="delete";
	 			parent.mainFrame.document.agssFrame.editAgscFTFForm.submit();
 			}
			document.addFTFForm.buttonValue.value="edit";
	 		document.addFTFForm.submit();	
	 	}
		
		function save1(val,val1){
		 flag=false;
			var netie=navigator.appName;
			if(val1=="edit"){
				document.addFTFForm.buttonValue.value="edit";
 			}
 			else if(val1=="save"){
 				document.addFTFForm.buttonValue.value="save";
 			}
 			if(netie=="Microsoft Internet Explorer")
 			{
 			//alert(netie);
	   			if(val=="true")
	   			{
	   			if(!flag)
 					{
 					//alert(parent.mainFrame.document.agssFrame);
 					
 					if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.editAgscFTFForm!=undefined)
		 			{
		 			parent.mainFrame.document.agssFrame.editAgscFTFForm.buttonValue.value="edit";
		 			parent.mainFrame.document.agssFrame.editAgscFTFForm.submit();
		 			}
		 			
 					if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.agscFTFForm!=undefined)
 						{
 							if(parent.mainFrame.document.agssFrame.agscFTFForm.charge!=undefined && parent.mainFrame.document.agssFrame.agscFTFForm.charge.value!="")
   							{
					 			alert("Please select Accessorial & General Standard Charges  AddToList  button");
					 			return;
		 			 		}
 								parent.mainFrame.document.agssFrame.agscFTFForm.submit();
 						}
 					if(parent.mainFrame.document.documentFrame!=undefined && parent.mainFrame.document.documentFrame.documentChargesFTFForm!=undefined)
 						{
 						parent.mainFrame.document.documentFrame.documentChargesFTFForm.submit();
 						}
 					}
	 			
	 			
 					
 					}
 					
	 			else if(val=="false")
	 			{
	 				
		 			if(parent.mainFrame.document.csssFrame.addFTFCommodityForm.charge!=undefined && parent.mainFrame.document.csssFrame.addFTFCommodityForm.charge.value!="" )
 					{
		 			alert("Please select Commodity Specific Accesorial Charges  AddToList button");
		 			return;
		 			}
		 			parent.mainFrame.document.airDetailsFrame.addFTFDetailsForm.submit();
		 			
 					}
 			}
 			else if(netie=="Netscape"){
 			if(val=="false"){
   				parent.mainFrame.document.getElementById('airDetailsFrame').contentDocument.addFTFDetailsForm.submit();
   				
   				}
   				}
   			setTimeout("aa()",1500);
   			}
		function aa()
		{
		document.addFTFForm.submit();	
		}
		
		function disabled(val1,val2) {
		
			if(val1!=""&&(val1 == 0 || val1== 3))
			{
		  	  	 var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	
	       	     	{
	   		 			 if(imgs[k].id != "cancel"&&imgs[k].id != "note")
	   		 			 	 {
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
				{ //alert("inside");
					save1(val2,val3);
	   			}	
			 else
			  {
			  		
	   				document.addFTFForm.buttonValue.value="savecancel";
	   				document.addFTFForm.submit();
			  }
			  }
				
		
 			
		}
		function delete1()	{
			document.addFTFForm.buttonValue.value="delete";
 			document.addFTFForm.submit();
			}
		function cancel1(val){
			if(val==0 || val==3){
			
			document.addFTFForm.buttonValue.value="cancelview";
			}
			else{
			document.addFTFForm.buttonValue.value="cancel";
				}
 			document.addFTFForm.submit();
			}
			function confirmnote()
	{
		
		document.addFTFForm.buttonValue.value="note";
    	document.addFTFForm.submit();
   	}	
 			</script>
		<style type="text/css">
		.disableInput {
           border:0;
           readonly:true;
           disabled:disabled;
           class:bodybackgrnd;
        }
		</style>
	</head>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>','<%=message%>')">
		<html:form  action="/addFTF" name="addFTFForm" type="com.gp.cong.logisoft.struts.ratemangement.form.AddFTFForm" scope="request">
	<table width=100% border="0" class="tableBorderNew">
			<tr>
    			<td  class="textlabels">Org Trm</td>
    			<td ><html:text property="terminalNumber" value="<%=terminalNumber%>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
    			<td  class="textlabels">OriginTrm Name</td>
    			<td ><html:text property="terminalName" value="<%=terminalName%>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
    		</tr>
    		<tr>	
    			<td  class="textlabels">Dest Port </td>
    			<td ><html:text property="destSheduleNumber" value="<%=destSheduleNumber%>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
    			<td  class="textlabels">Dest Port Name</td>
    			<td ><html:text property="destAirportname" value="<%=destAirportname%>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
    		</tr>
    		<tr>	
    			<td  class="textlabels">Com Code</td>
    			<td ><html:text property="comCode"  value="<%=comCode%>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
    			<td  class="textlabels">Com Description</td>
    			<td ><html:text property="comDescription" value="<%=comDesc%>" readonly="true" styleClass="disableInput bodybackgrnd"/></td>
            </tr>
            <tr  align="center" > 
               <td colspan="4">
                  <table>
                     <tr>
                       <td>
 
<%
 if(result!=null && result.equals("save")){

%>
 			<td>
 			    <input type="button" value="Save" onclick="save1('<%=enable%>','<%=result%>')" id="save" class="buttonStyleNew" />
 		   </td>
    		<td>
    	 	  <input type="button" value="Go Back" onclick="cancel2('<%=records%>','<%=enable%>','<%=result%>')" id="cancel" class="buttonStyleNew" />
    	    </td>

<% }
else if(result!=null&&result.equals("edit"))
{
%>
	<td>
	     <input type="button" value="Save" onclick="save1('<%=enable%>','<%=result%>')" id="save" class="buttonStyleNew" />
	 </td>
	 <td>
	  <input value="Note" type="button" class="buttonStyleNew" id="note" onclick="confirmnote()">
	 </td>
    <td>
        <input type="button" value="Go Back" onclick="cancel1('<%=modify%>')" id="cancel" class="buttonStyleNew" />
    </td>
	<td>
	 <input value="Delete" type="button" class="buttonStyleNew" onclick="delete1()" id="delete">
	</td>
	<%} %>
<%
if(session.getAttribute("message")!=null){
session.removeAttribute("message");}%>
  
		
                      
                     </tr>
                  </table>
               </td>  
    		</tr>
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue"/>	
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

