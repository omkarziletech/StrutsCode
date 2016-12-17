<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.StandardCharges,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.beans.PortsBean,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
	 <%
response.addHeader("Pragma", "No-cache");
response.addHeader("Cache-Control", "no-cache");
%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
 

String buttonValue="load";
String msg="";
String message="";
String modify=null;
String terminalNumber = "";
String destSheduleNumber = "";
String comCode = "";
String terminalName = "";
String destAirportname = "";
String comDesc = "";
StandardCharges airRatesObj1= null;
String messge="";
String airRates="";
AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setCommon("");
StandardCharges newss=new  StandardCharges();
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
}
request.setAttribute("airRatesBean",airRatesBean);
if(request.getAttribute("message")!=null){
messge=(String)request.getAttribute("message");	
}

if(request.getParameter("airrates")!=null)
{
	if(session.getAttribute("manageairrates")!=null)
	{
		
		StandardCharges sc1=(StandardCharges)session.getAttribute("manageairrates");
		newss. setOrgTerminal(sc1.getOrgTerminal());
	    newss.setDestPort(sc1.getDestPort());
	    newss.setComCode(sc1.getComCode());
		
		session.setAttribute("standardCharges",newss);
	}
	
}
if(request.getAttribute("airrate")!=null)
{
	
	airRates=(String)request.getAttribute("airrate");
	
	
	}
	
boolean b=false;

if(session.getAttribute("standardCharges")!=null )
{
    airRatesObj1=(StandardCharges)session.getAttribute("standardCharges");
	if(airRatesObj1.getOrgTerminal()!=null)
	{
		terminalNumber=airRatesObj1.getOrgTerminal().getCode();
		terminalName=airRatesObj1.getOrgTerminal(). getCodedesc();
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
}

List list=new ArrayList();
DBUtil dbutil=new DBUtil();
if(airRatesObj1!=null){
	if(airRatesObj1.getOrgTerminal()!=null && airRatesObj1.getDestPort()!=null && airRatesObj1.getComCode()!=null)
			{
				list=dbutil.getAirDetails(airRatesObj1.getOrgTerminal(),airRatesObj1.getDestPort(),airRatesObj1.getComCode());
				if(list!=null&&list.size()>0){
				if(request.getParameter("airrates")!=null)
				{
				}else{
				messge="PLease Select another Commodity or else change Origin number or Destination port ";
				}
				comCode="";
				comDesc="";
				}
				}
				}
if(session.getAttribute("exist")!=null){
session.removeAttribute("exist");
}	
if(request.getAttribute("msg")!=null)
{
	message=(String)request.getAttribute("msg");
}
if(request.getAttribute("message")!=null)
{
	msg=(String)request.getAttribute("message");
}
if(request.getParameter("modify")!= null)
{
	session.setAttribute("modifyforrelay",request.getParameter("modify"));
 	modify=(String)session.getAttribute("modifyforrelay");
}
else
{
 	modify=(String)session.getAttribute("modifyforrelay");
}


if(messge.equals("")){
if(airRates.equals("addairrate"))
{
%>
	<script>
	
	   // parent.parent.getAddAirRates();
		//parent.parent.GB_hide();
<%--		self.close();--%>
<%--		opener.location.href="<%=path%>/jsps/ratemanagement/airRatesFrame.jsp";--%>
	</script>
<%
}
}

%>



 
<html> 
	<head>
	<%@include file="../includes/resources.jsp" %>
		<title>Add Air Rates</title>
		<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		   dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		<script language="javascript" type="text/javascript">
		function searchform()
		{
			if(document.addAirRatesPopupForm.terminalNumber.value=="" || document.addAirRatesPopupForm.destSheduleNumber.value=="")
		    {
			   alert("Please select OriginTerminal ,  Dest Airport and Com Code ");
			   return;
		    }
		   if(document.addAirRatesPopupForm.comCode.value=="")
			{
				alert("Please select Commodity Code Port Code");
				return;
			}
			document.addAirRatesPopupForm.buttonValue.value="search";
			document.addAirRatesPopupForm.submit();
		}
	   function popup1(mylink, windowname)
		{
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
   			href=mylink;
			else
   			href=mylink.href;
			mywindow=window.open(href, windowname, 'width=500,height=250,scrollbars=yes');
			mywindow.moveTo(200,180);
			document.addAirRatesPopupForm.buttonValue.value="search1";
			document.addAirRatesPopupForm.submit();
			return false;
		}
    function titleLetter(ev)
	{
		if(event.keyCode==9){
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
	    document.addAirRatesPopupForm.submit();
	
	}
	}
	function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.addAirRatesPopupForm.terminalName.value="";
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
		document.addAirRatesPopupForm.submit();
	    
	    }
	}
	function getDestination(ev)
	{
		if(event.keyCode==9){
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
	    document.addAirRatesPopupForm.submit();
	
	}
	}
	function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.addAirRatesPopupForm.destAirportname.value="";
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
		document.addAirRatesPopupForm.submit();
	
	}
	}
	function getComCode(ev)
	{
		if(event.keyCode==9){
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
	    document.addAirRatesPopupForm.submit();
	
	}
	}
	function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addAirRatesPopupForm.comDescription.value="";
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
		document.addAirRatesPopupForm.submit();
	
	}
	}
	function titleLetterName(ev)
	{
		if(event.keyCode==9){
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
	    document.addAirRatesPopupForm.submit();
	
	}
	}
	function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addAirRatesPopupForm.terminalNumber.value="";
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
		document.addAirRatesPopupForm.submit();
	
	}
	}
	function getDestinationName(ev)
	{
		if(event.keyCode==9){
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
	    document.addAirRatesPopupForm.submit();
	
	}
	}
<%--	function getComCodeName(ev)--%>
<%--	{--%>
<%--		if(event.keyCode==9){--%>
<%--		document.addAirRatesPopupForm.buttonValue.value="popupsearch";--%>
<%--	    document.addAirRatesPopupForm.submit();--%>
<%--	--%>
<%--	}--%>
<%--	}--%>
	function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addAirRatesPopupForm.destSheduleNumber.value="";
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
		document.addAirRatesPopupForm.submit();
	
	}
	}
   function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.addAirRatesPopupForm.comCode.value="";
		document.addAirRatesPopupForm.buttonValue.value="popupsearch";
		document.addAirRatesPopupForm.submit();
	
	}
	}
	
	<%--type 2 dojo implementation--%>

		   function getComCodeName(ev){
		   document.getElementById("terminalName").value="";
			if(event.keyCode==9 || event.keyCode==13){	
			
			       var params = new Array();
					 params['requestFor'] = "CommodityDetails";
					 params['code'] = document.addAirRatesPopupForm.terminalNumber.value;
					 params['codeType'] = "1";
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateCommodityCodeDojo");
					 
		    
		    } 
		    }
		  function populateCommodityCodeDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("terminalName").value=data.commodityDesc
		   	     }
		     } 		
		
		function getComCode(ev){
		 document.getElementById("terminalNumber").value="";
			if(event.keyCode==9 || event.keyCode==13){	
		 var params = new Array();
					 params['requestFor'] = "CommodityDetails";
					 params['codeDesc'] = document.addAirRatesPopupForm.terminalName.value;
					 params['codeType'] = "1";
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateCommodityCodeDojo1");
					 
		    
		    } 
		    }
		  function populateCommodityCodeDojo1(type, data, evt) {
		    	if(data){
		   		       document.getElementById("terminalNumber").value=data.commodityCode
		   	     }
		     } 		
		     

		     		   function getDestPortName(ev){
		     		   document.getElementById("destAirportname").value="";
			if(event.keyCode==9 || event.keyCode==13){	
			 var params = new Array();
					 params['requestFor'] = "CommodityDetails";
					 params['code'] = document.addAirRatesPopupForm.destSheduleNumber.value;
					 params['codeType'] = "1";
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateDestPortName");
					 
		    
		    } 
		    }
		  function populateDestPortName(type, data, evt) {
		    	if(data){
		   		       document.getElementById("destAirportname").value=data.commodityDesc
		   	     }
		     } 		
		
		function getDestPort(ev){
		document.getElementById("destSheduleNumber").value="";
			if(event.keyCode==9 || event.keyCode==13){
		 var params = new Array();
					 params['requestFor'] = "CommodityDetails";
					 params['codeDesc'] = document.addAirRatesPopupForm.destAirportname.value;
					 params['codeType'] = "1";
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateDestPort");
					 
		    
		    } 
		    }
		  function populateDestPort(type, data, evt) {
		    	if(data){
		   		       document.getElementById("destSheduleNumber").value=data.commodityCode
		   	     }
		     } 
		     		function getCodeDesc(ev){ 
		document.getElementById("comDescription").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.addAirRatesPopupForm.comCode.value;
			params['codeType'] = "4";
			var bindArgs = {
	  			url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  			error: function(type, data, evt){alert("error");},
	 			mimetype: "text/json",
	  			content: params
			};
			var req = dojo.io.bind(bindArgs);
			dojo.event.connect(req, "load", this, "populateCodeDesc");
		}
	}
	function populateCodeDesc(type, data, evt) {
  		if(data){
   			document.getElementById("comDescription").value=data.commodityDesc;
   		}
	}
	function getCode(ev){ 
		document.getElementById("comCode").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['codeDesc'] = document.addAirRatesPopupForm.comDescription.value;
			params['codeType'] = "4";
			var bindArgs = {
	  			url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  			error: function(type, data, evt){alert("error");},
	 			mimetype: "text/json",
	  			content: params
			};
			var req = dojo.io.bind(bindArgs);
			dojo.event.connect(req, "load", this, "populateCode");
		}
	}
	function populateCode(type, data, evt) {
  		if(data){
   			document.getElementById("comCode").value=data.commodityCode;
   		}
	}
	</script>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/addAirRatesPopup" scope="request">
		
		<font color="blue"><b><%=messge %></b></font>
		
			<table width=100%  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew"> Add New</tr>
			<tr class="textlabels">
    			<td><table width="100%" border="0" ><tr class="style2"><td>Org Trm</td></tr></table></td>
    			<td>

    			<input name="terminalNumber" id="terminalNumber" size="15" onkeydown="getComCodeName(this.value)" value="<%=terminalNumber %>" maxlength="2"/>    
    			<dojo:autoComplete formId="addAirRatesPopupForm" textboxId="terminalNumber" action="<%=path%>/actions/getChargeCode.jsp?from=0&tabName=AIR_RATES"/>                
    			</td>
    			
    			<td>Org Trm Name</td>
    			<td >
    			<input name="terminalName" id="terminalName" size="15"  onkeydown="getComCode(this.value)" value="<%=terminalName %>" />    
    			<dojo:autoComplete formId="addAirRatesPopupForm" textboxId="terminalName" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=AIR_RATES&from=0"/>
    			</td>
    		</tr>
    		<tr class="textlabels">	
    			<td><table width="100%" border="0"><tr class="style2"><td>Dest port</td></tr></table> </td>
    			<td >
    			<input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getDestPortName(this.value)" value="<%=destSheduleNumber %>" size="15" maxlength="5"/>    
    			<dojo:autoComplete formId="addAirRatesPopupForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getChargeCode.jsp?from=1&tabName=AIR_RATES"/>
    			</td>
    			
    			<td><span class="textlabels">Dest Port Name</span></td>
    			<td  >
    			<input name="destAirportname" id="destAirportname"  onkeydown="getDestPort(this.value)" value="<%=destAirportname %>" size="15" />    
    		    <dojo:autoComplete formId="addAirRatesPopupForm" textboxId="destAirportname" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=AIR_RATES&from=1"/>
    			</td>
    			
    		</tr>
    		<tr class="textlabels">	
    			<td><table width="100%" border="0"><tr class="style2"><td>Com Code</td></tr></table> </td>
    			<td>
    			<input name="comCode" id="comCode"  onkeydown="getCodeDesc(this.value)" value="<%=comCode %>" maxlength="6" size="15"/>    
    		    <dojo:autoComplete formId="addAirRatesPopupForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=AIR_RATES&from=3"/>
    			</td>
    			
    			<td >Com Description </td>
    			<td >
    			<input name="comDescription" id="comDescription"  onkeydown="getCode(this.value)" value="<%=comDesc %>"  size="15"/>    
    		    <dojo:autoComplete formId="addAirRatesPopupForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=AIR_RATES&from=2"/>
    			</td>
    		</tr>
    		
    		
    		<tr>
    		
    		<td align="center" colspan="5">
    		     <input type="button" value="Submit" class="buttonStyleNew" onclick="searchform()"/>
    		     
    		 </td>
    		<td>&nbsp;</td>
    		</tr>	
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

