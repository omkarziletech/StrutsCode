<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.StandardCharges,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.beans.PortsBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();

String records="";
String buttonValue="load";
String msg="";
String message="";
String modify=null;
List relayList=null;
String terminalNumber = "";
String terminalName = "";
String destSheduleNumber = "";
String destAirportname = "";
String comCode = "";
String comDesc = "",result="";
boolean enable=false;;
			if(session.getAttribute("setTabEnable")!=null)
			{
				
				if(session.getAttribute("setTabEnable").equals("enable"))
				{
					enable = true;
				}
				else
				{
					enable = false;
				}
				
			}
StandardCharges airRatesObj1= new StandardCharges();

if(session.getAttribute("standardCharges")!=null )
{
    airRatesObj1=(StandardCharges)session.getAttribute("standardCharges");
   

	if(airRatesObj1.getOrgTerminal()!=null)
	{
		terminalNumber=airRatesObj1.getOrgTerminal().getCode();
		terminalName=airRatesObj1.getOrgTerminal().getCodedesc();
	}
	if(airRatesObj1.getDestPort() != null)
	{
		destSheduleNumber=airRatesObj1.getDestPort().getCode();
		destAirportname=airRatesObj1.getDestPort().getCodedesc();
		
	}
	if(airRatesObj1.getComCode() != null)
	{
		comCode=airRatesObj1.getComCode().getCode();
		comDesc=airRatesObj1.getComCode().getCodedesc();
	}
	records="Norecords";
}

		
			
if(request.getAttribute("msg")!=null)
{
	message=(String)request.getAttribute("msg");
}
if(session.getAttribute("message")!=null)
{
	msg=(String)session.getAttribute("message");
}

if(request.getAttribute("buttonValue")!=null)
{
			buttonValue=(String)request.getAttribute("buttonValue");
} 

modify = (String) session.getAttribute("modifyforairRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
		
	
		}

if (session.getAttribute("editrecord") != null) {
		result = (String) session.getAttribute("editrecord");
		
		
		}



%>
 
<html> 
	<head>
		<title>Add Air Rates</title>
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
			document.addAirRatesForm.buttonValue.value="edit";
	 		document.addAirRatesForm.submit();
	 }
	function save1(val,val1)
	 {
			if(val=="true")
   			{
	   			if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.aGSSEditForm!=undefined)
			 	{
			 			parent.mainFrame.document.agssFrame.aGSSEditForm.buttonValue.value="edit";
			 			parent.mainFrame.document.agssFrame.aGSSEditForm.submit();
			 	}
	 			if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.aGSSForm!=undefined &&  parent.mainFrame.document.agssFrame.aGSSForm.charge!=undefined && parent.mainFrame.document.agssFrame.aGSSForm.charge.value!="" )
	 			{
	 				alert("Please select Accessorial & General Standard Charges  Add button");
		 			return;
	 			}
	 			if(parent.mainFrame.document.documentFrame!=undefined &&  parent.mainFrame.document.documentFrame.documentChargesForm!=undefined && parent.mainFrame.document.documentFrame.documentChargesForm.charge!=undefined && parent.mainFrame.document.documentFrame.documentChargesForm.charge.value!="" )
	 			{
	 				alert("Please select Document Charges_commission Add button");
	 				return;
	 			}
 			}
 			else
 			{
 				if(parent.mainFrame.document.csssFrame!=undefined &&  parent.mainFrame.document.csssFrame.cSSSForm!=undefined && parent.mainFrame.document.csssFrame.cSSSForm.charge!=undefined && parent.mainFrame.document.csssFrame.cSSSForm.charge.value!="" )
	 			{
	 				alert("Please select Commodity specific Accessorial Charges Add button");
	 				return;
	 			}
	 			if(parent.mainFrame.document.airDetailsFrame!=undefined && parent.mainFrame.document.airDetailsFrame.airDetailsForm!=undefined && 
	 			(parent.mainFrame.document.airDetailsFrame.airDetailsForm.generalRate!=undefined && parent.mainFrame.document.airDetailsFrame.airDetailsForm.generalRate.value!="") 
	 			||(parent.mainFrame.document.airDetailsFrame.airDetailsForm.generalAmt!=undefined && parent.mainFrame.document.airDetailsFrame.airDetailsForm.generalAmt.value!="")
	 			|| (parent.mainFrame.document.airDetailsFrame.airDetailsForm.expressRate!=undefined && parent.mainFrame.document.airDetailsFrame.airDetailsForm.expressRate.value!="")
	 			|| (parent.mainFrame.document.airDetailsFrame.airDetailsForm.expressAmt!=undefined && parent.mainFrame.document.airDetailsFrame.airDetailsForm.expressAmt.value!="")
	 			|| (parent.mainFrame.document.airDetailsFrame.airDetailsForm.deferredRate!=undefined && parent.mainFrame.document.airDetailsFrame.airDetailsForm.deferredRate.value!="")
	 			|| (parent.mainFrame.document.airDetailsFrame.airDetailsForm.deferredAmt!=undefined && parent.mainFrame.document.airDetailsFrame.airDetailsForm.deferredAmt.value!=""))
				{
	 				//alert(parent.mainFrame.document.airDetailsFrame.airDetailsForm);
	 				alert("Please select Air firght Rates  button");
	 				return;
	 			}
 			}
 			
			var netie=navigator.appName;
			/*if(netie=="Microsoft Internet Explorer")
   			{
   			if(val=="true")
   			{
 				parent.mainFrame.document.agssFrame.aGSSForm.submit();
 				parent.mainFrame.document.documentFrame.documentChargesForm.submit();
 				parent.mainFrame.document.flightFrame.flightSheduleForm.submit();
 				
 			}
 			else
 			{
 			parent.mainFrame.document.airDetailsFrame.airDetailsForm.submit();
 			
 			}
 				
 			}
 			else if(netie=="Netscape")
   			{
   				if(val=="false")
   			{
 			parent.mainFrame.document.getElementById('airDetailsFrame').contentDocument.airDetailsForm.submit();
 			
 			}
   				  				
 			}
 			*/
 			if(val1=="edit")
 			{
 			document.addAirRatesForm.buttonValue.value="edit";

 			}else if(val1=="save")
 			{
 			document.addAirRatesForm.buttonValue.value="save";
 			}
 			document.addAirRatesForm.submit();
	 }
	function cancel1(val)
	{
		if(val==0 || val==3){
		document.addAirRatesForm.buttonValue.value="cancelview";
		}else{
			document.addAirRatesForm.buttonValue.value="cancel";
		}
 			document.addAirRatesForm.submit();
	}
    function cancel2(val1,val2,val3)
	 {
			 if(val1!=""){
			 	 var result = confirm("Do  you want to Add Rates ??");
			 	  if(result){
						save1(val2,val3);
		   		   }else{
		   				document.addAirRatesForm.buttonValue.value="savecancel";
		   				document.addAirRatesForm.submit();
				  }
		     }
	}
   function delete1()
	{
		document.addAirRatesForm.buttonValue.value="delete";
 		document.addAirRatesForm.submit();
	}
   function confirmnote()
	{
		document.addAirRatesForm.buttonValue.value="note";
    	document.addAirRatesForm.submit();
   	}	
	</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/addAirRates" styleId="addAirRatesId" scope="request">

			<table width="100%" border="0">
			<tr>
    			<td class="textlabels"><b>Org Trm </td>
    			<td><html:text property="terminalNumber" size="2" value="<%=terminalNumber%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td  class="textlabels"><b>OrgTrm Name</td>
    			<td><html:text property="terminalName" value="<%=terminalName%>" readonly="true" styleClass="areahighlightgrey"/></td>
<%if(result!=null && result.equals("edit") && comCode!=null && comCode.equals("000000") && modify!=null && !modify.equals("3")&& session.getAttribute("getAirEdit")!=null)
  {
%>
    		<td colspan="4" align="right"><img src="<%=path%>/img/delete_POL_POD.gif" border="0" onclick="delete1()" id="delete"/></td>
<% } %>
    		</tr>
    		<tr>	
    			<td class="textlabels"><b>Dest port</td>
    			<td><html:text property="destSheduleNumber" size="5" value="<%=destSheduleNumber%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td  class="textlabels"><b>Dest Port Name</td>
    			<td><html:text property="destAirportname" value="<%=destAirportname%>" readonly="true" styleClass="areahighlightgrey"/></td>
    		</tr>
    		<tr>	
    			<td class="textlabels"><span class="textlabels"><b> Com Code </span> </td>
    			<td><html:text property="comCode" size="6" value="<%=comCode%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td  class="textlabels"><span class="textlabels"><b>Com Description </span> </td>
    			<td><html:text property="comDescription" value="<%=comDesc%>" readonly="true" styleClass="areahighlightgrey"/></td>
            </tr>
            <tr>
<% if(result!=null&&result.equals("save")){%>
 			 <td valign="top" colspan="8" align="center" style="padding-top:10px;">
 			    <input type="button" value="Save" class="buttonStyleNew" onclick="save1('<%=enable%>','<%=result%>')" id="save"/>
                <input type="button" value="Go Back" class="buttonStyleNew" onclick="cancel2('<%=records%>','<%=enable%>','<%=result%>')" id="cancel"/>
        	</td>

<% }else if(result!=null&&result.equals("edit")){%>
		    <td valign="top" colspan="8" align="center" >
		      <input type="button" value="Save" class="buttonStyleNew" onclick="save1('<%=enable%>','<%=result%>')" id="save"/>
		      <input type="button" value="Go Back" class="buttonStyleNew" onclick="cancel1('<%=modify%>')" id="cancel"/>
		      <input type="button" value="Note" class="buttonStyleNew" id="note"	onclick="confirmnote()"/>
		   </td>
<%
 if(comCode!=null && comCode.equals("000000")&& session.getAttribute("getAirEdit")!=null && !modify.equals("3"))
{ %>	
	          <input type="button" value="Delete" class="buttonStyleNew" id="delete1" onclick="deleteAgss1()" />
	
<%}else{ %>    
	          <input type="button" value="Delete/POL/POD" class="buttonStyleNew" onclick="delete1()" id="delete" style="width:100px"/>
       
<%}%>
<%}%>
    		</tr>
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue"/>	
    		<script>parent.parent.parent.parent.makeFormBorderless(document.getElementById("addAirRatesId"));</script>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

