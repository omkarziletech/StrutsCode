<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.RetailStandardCharges,
com.gp.cong.logisoft.domain.GenericCode,
com.gp.cong.logisoft.beans.PortsBean"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();

String records="";
String buttonValue="load";
String msg1="";
String msg="";
String message="";
String modify=null;
List relayList=null;
String terminalNumber = "";
String terminalName = "";
String destSheduleNumber = "";
String destAirportname = "";
String comCode = "";
String comDesc = "",res="";
boolean enable=false;;

			if(session.getAttribute("retailsetTabEnable")!=null)
			{
				
				if(session.getAttribute("retailsetTabEnable").equals("retailenable"))
				{
					enable = true;
				}
				else
				{
					enable = false;
				}
				
			}
RetailStandardCharges airRatesObj1= new RetailStandardCharges();

if(session.getAttribute("retailstandardCharges")!=null )
{
    airRatesObj1=(RetailStandardCharges)session.getAttribute("retailstandardCharges");
		terminalNumber=airRatesObj1.getOrgTerminal();
		terminalName=airRatesObj1.getOrgTerminalName();
		destSheduleNumber=airRatesObj1.getDestPort();
		destAirportname=airRatesObj1.getDestPortName();
		comCode=airRatesObj1.getComCode();
		comDesc=airRatesObj1.getComCodeName();
	
	records="Norecords";
}

if(request.getAttribute("msg")!=null)
{
	message=(String)request.getAttribute("msg");
}
if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
}

if(session.getAttribute("message")!=null)
{
	msg1=(String)session.getAttribute("message");
}




if(session.getAttribute("retailrelayList")!=null)
{
	relayList=(List)session.getAttribute("retailrelayList");
} 
if(request.getAttribute("buttonValue")!=null)
{
			buttonValue=(String)request.getAttribute("buttonValue");
} 


modify = (String) session.getAttribute("modifyforretailRates");

if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");
		}

if (session.getAttribute("retaileditrecord") != null) {
		res = (String) session.getAttribute("retaileditrecord");
		}

//if(request.getParameter("programid")!= null && session.getAttribute("retailprocessinfoforrelay")==null)
//{
  //	 buttonValue="searchall";
  //	session.setAttribute("retailprocessinfoforrelay",request.getParameter("programid"));
//}

//if(buttonValue.equals("searchall"))
//{
 //	relayList=dbUtil.getAllRelay();
 	//session.setAttribute("retailrelayList",relayList);
 	//session.setAttribute("RelayCaption","Relay {All Records}");
//}


if (session.getAttribute("view") != null) {
		modify = (String) session.getAttribute("view");

		}
String editPath=path+"/manageRetailRates.do";


%>

 
<html> 
	<head>
		<title>JSP for AddRetailRatesForm form</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" type="text/javascript">
		
		
	function save1(val,val1)
	 {
			var netie=navigator.appName;
			if(netie=="Microsoft Internet Explorer")
   			{
   			   flag=false;
   				if(val=="true")
   				{ 
   					
 				if(!flag)
 					{
 					if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.retailEditAGSCForm!=undefined)
		 			{
		 			parent.mainFrame.document.agssFrame.retailEditAGSCForm.buttonValue.value="edit";
		 			parent.mainFrame.document.agssFrame.retailEditAGSCForm.submit();
		 			}
 				if(parent.mainFrame.document.agssFrame!=undefined && parent.mainFrame.document.agssFrame.retailAGSCForm!=undefined)
 						{
 							if(parent.mainFrame.document.agssFrame.retailAGSCForm.charge!=undefined && parent.mainFrame.document.agssFrame.retailAGSCForm.charge.value!="")
   							{
					 			alert("Please select Accessorial & General Standard Charges  AddToList  button");
					 			return;
		 			 		}
 							parent.mainFrame.document.agssFrame.retailAGSCForm.submit();
 						}
 				if(parent.mainFrame.document.documentFrame!=undefined && parent.mainFrame.document.documentFrame.retailDocumentChargesForm!=undefined)
 						{
				 					parent.mainFrame.document.documentFrame.retailDocumentChargesForm.submit();
 						}

 					}
 				
 				}
 				else
 				{
 					if(parent.mainFrame.document.csssFrame.retailCSSCForm.charge!=undefined && parent.mainFrame.document.csssFrame.retailCSSCForm.charge.value!="" )
 					{
		 			alert("Please select Commodity Specific Accesorial Charges  AddToList button");
		 			return;
		 			}
 					parent.mainFrame.document.airDetailsFrame.retailDetailsForm.submit();
 				}
 				
 			}
 			
 			
 			
 			if(val1=="edit")
 			{
 				document.addRetailRatesForm.buttonValue.value="edit";
 			}
 			else if(val1=="save")
 			{
 				document.addRetailRatesForm.buttonValue.value="save";
 			
 			}
 			//modified on 160608
 			if(netie=="Netscape" && val=="false")
 			{
 				parent.mainFrame.document.getElementById('airDetailsFrame').contentDocument.retailDetailsForm.submit();
 				//parent.mainFrame.document.airDetailsFrame.retailDetailsForm.submit();
 			}
 			else if(netie=="Microsoft Internet Explorer" && val=="false")
 			{
 				parent.mainFrame.document.airDetailsFrame.retailDetailsForm.submit();
 			}
 		
 			setTimeout("aa()",1500);	
		}
	function aa()
	{
			document.addRetailRatesForm.submit();
	}
	function cancel1(val)
	 {
		if(val==0 || val==3)
		{
		document.addRetailRatesForm.buttonValue.value="cancelview";
		}
		else
		{
			document.addRetailRatesForm.buttonValue.value="cancel";
		}
 			document.addRetailRatesForm.submit();
	 }
	function cancel2(val1,val2,val3)
		{ 
		if(val1!=""){
		 	var result = confirm("Do you want to Add Rates ??");
		 	
		 	 if(result)
				{
				save1(val2,val3);
	   			}	
			 else
			  {
					document.addRetailRatesForm.buttonValue.value="savecancel";
					document.addRetailRatesForm.submit();
	   		  
			  }
			  }
			
		}
	function popup1(mylink, windowname)
		{
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=800,height=250,scrollbars=yes');
			mywindow.moveTo(200,180);
			return false;
		}
	
    function confirmnote(){
		document.addRetailRatesForm.buttonValue.value="note";
    	document.addRetailRatesForm.submit();
   	}	
	 function delete1()
	{
		document.addRetailRatesForm.buttonValue.value="delete";
    	document.addRetailRatesForm.submit();
   	}
	</script>
		
	</head>
	<body class="whitebackgrnd">
		<html:form action="/addRetailRates" styleId="addRetailRatesId" scope="request">
		<table width=600 >
			<tr>
    			<td  class="textlabels">Org Trm</td>
    			<td><html:text property="terminalNumber" size="2" value="<%=terminalNumber%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td class="textlabels">Org Trm Name</td>
    			<td ><html:text property="terminalName" value="<%=terminalName%>" readonly="true" styleClass="areahighlightgrey"/></td>
    		</tr>
    		<tr>	
    			<td class="textlabels">Dest port </td>
    			<td><html:text property="destSheduleNumber" size="5" value="<%=destSheduleNumber%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td class="textlabels">Dest Port Name</td>
    			<td><html:text property="destAirportname" value="<%=destAirportname%>" readonly="true" styleClass="areahighlightgrey"/></td>
    		
    		</tr>
    		<tr>	
    			<td class="textlabels"><span class="textlabels"> Com Code</span> </td>
    			<td><html:text property="comCode"  value="<%=comCode%>" size="6" readonly="true" styleClass="areahighlightgrey"/></td>
    			<td class="textlabels"><span class="textlabels">Com Description</span> </td>
    			<td ><html:text property="comDescription" value="<%=comDesc%>" readonly="true" styleClass="areahighlightgrey"/></td>
    			</tr><tr>
    			<td colspan="4" align="center">
 <%if(modify!=null && modify.equals("3"))
 {  			
  %>
		<input type="button" value="Go Back" id="cancel" class="buttonStyleNew" onclick="cancel1('<%=modify%>')"/>
		<input type="button" value="Note" id="note" class="buttonStyleNew" onclick="confirmnote()"/>
<%
}else
	{
		if(res!=null&&res.equals("save"))
		{
%>		
                 <input type="button" value="Save" id="save" class="buttonStyleNew" onclick="save1('<%=enable%>','<%=res%>')"/>
		        <input type="button" value="Go Back" id="cancel" class="buttonStyleNew" onclick="cancel2('<%=records%>','<%=enable%>','<%=res%>')"/>
    	<% }
    	else if(res!=null&&res.equals("edit"))
    	{ %>
    			<input type="button" value="Save" id="save" class="buttonStyleNew" onclick="save1('<%=enable%>','<%=res%>')"/>
     			<input type="button" value="Delete" id="delete" class="buttonStyleNew" onclick="delete1()"/>
     			<input type="button" value="Go Back" id="cancel" class="buttonStyleNew" onclick="cancel1('<%=modify%>')"/>
     
<%} 
}%>   		
</td>
</tr>
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue"/>	
<%--    		<script>parent.parent.parent.parent.makeFormBorderless(document.getElementById("addRetailRatesId"));</script>--%>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

