<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.LCLColoadMaster,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
LCLColoadMaster lclColoadMaster=new LCLColoadMaster();
String terminalNumber="";
String terminalName="";
String destSheduleNumber="";
String destAirportname="";
String comCode="";
String comDesc="";
String message="";
String msg="";
String modify="";
AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setCommon("");
LCLColoadMaster lclNew=new LCLColoadMaster();
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
}
request.setAttribute("airRatesBean",airRatesBean);
if(request.getParameter("fclcorates")!=null)
	{
		
	if(session.getAttribute("searchlclColoadMaster")!=null)
	{
		
		LCLColoadMaster sc1=(LCLColoadMaster)session.getAttribute("searchlclColoadMaster");
		lclNew. setOriginTerminal(sc1.getOriginTerminal());
		lclNew. setOriginTerminalName(sc1.getOriginTerminalName());
	    lclNew.setDestinationPort(sc1.getDestinationPort());
	    lclNew.setDestinationPortName(sc1.getDestinationPortName());
	   //lclNew.setCommodityCode(sc1.getCommodityCode());
	    //lclNew.setCommodityCodeName(sc1.getCommodityCodeName());
		
		session.setAttribute("addlclColoadMaster",lclNew);
	}
	}
if(session.getAttribute("addlclColoadMaster")!=null)
{
lclColoadMaster=(LCLColoadMaster)session.getAttribute("addlclColoadMaster");
	terminalNumber=lclColoadMaster.getOriginTerminal();
	terminalName=lclColoadMaster.getOriginTerminal();
	destSheduleNumber=lclColoadMaster.getDestinationPort();
	destAirportname=lclColoadMaster.getDestinationPort();
	
}

	
 List list=new ArrayList();
 DBUtil dbutil=new DBUtil();
if(lclColoadMaster!=null){ 
	if(lclColoadMaster.getOriginTerminal()!=null && lclColoadMaster.getDestinationPort()!=null && lclColoadMaster.getCommodityCode()!=null)
			{
				list=dbutil.getCoLoadDetails(lclColoadMaster.getOriginTerminal(),lclColoadMaster.getDestinationPort(),lclColoadMaster.getCommodityCode());
				if(list!=null&&list.size()>0)
				{
					message="PLease Select another Commodity or else change Origin number or Destination port ";
					comCode="";
					comDesc="";
				}
					
				}
				}
				
String lclcoloadrate="";
if(request.getAttribute("lclcoloadrate")!=null)
{
lclcoloadrate=(String)request.getAttribute("lclcoloadrate");
}
if(request.getAttribute("msg")!=null)
{
	msg=(String)request.getAttribute("msg");
}
if(request.getAttribute("update")!=null)
{
	message=(String)request.getAttribute("update");
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
if(message.equals("")){
//if(lclcoloadrate.equals("addlclcoloadrate"))
//{
%>
<%--	<script>--%>
	
	
	         
<%--	         parent.parent.getLCLCoload();--%>
<%--	         parent.parent.GB_hide();--%>

<%--	</script>--%>
<%
//}

}
%>

<html> 
	<head>
		<title>JSP for AddLclColoadPopupForm form</title>
<%@include file="../includes/baseResources.jsp" %>

		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>
		<script language="javascript" type="text/javascript">
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
				document.addLclColoadPopupForm.buttonValue.value="search1";
			document.addLclColoadPopupForm.submit();
			return false;
		}
		
		function searchform()
		{
		if(document.addLclColoadPopupForm.terminalNumber.value=="")
		{
		alert("Please select Terminal Number");
		return;
		}
		if(document.addLclColoadPopupForm.destSheduleNumber.value=="")
		{
		alert("Please select Destination Airport Code");
		return;
		}
		if(document.addLclColoadPopupForm.comCode.value=="")
			{
			alert("Please select Commodity Code Port Code");
			return;
			}
			document.addLclColoadPopupForm.buttonValue.value="search";
			document.addLclColoadPopupForm.submit();
		}
		
		function titleLetter(ev)
	{
	if(event.keyCode==9){
	        document.addLclColoadPopupForm.buttonValue.value="popupsearch";
			document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=addlclcoload&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.addLclColoadPopupForm.terminalName.value="";
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addLclColoadPopupForm.terminalNumber.value="";
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestination(ev)
	{
		if(event.keyCode==9){
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=addlclcoload&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.addLclColoadPopupForm.destAirportname.value="";
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCode(ev)
	{
		if(event.keyCode==9){
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=addlclcoload&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterName(ev)
	{
	if(event.keyCode==9){
	       document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		   document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=addlclcoload&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationName(ev)
	{
		if(event.keyCode==9){
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=addlclcoload&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addLclColoadPopupForm.destSheduleNumber.value="";
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodeName(ev)
	{
		if(event.keyCode==9){
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=addlclcoload&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addLclColoadPopupForm.comDescription.value="";
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.addLclColoadPopupForm.comCode.value="";
		document.addLclColoadPopupForm.buttonValue.value="popupsearch";
		document.addLclColoadPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}	
	
	//type 2 dojo implemetation
	
	  		 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.addLclColoadPopupForm.terminalNumber.value;
			var bindArgs = {
	  			url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  			error: function(type, data, evt){alert("error");},
	  			mimetype: "text/json",
	  			content: params
			};
			var req = dojo.io.bind(bindArgs);
			dojo.event.connect(req, "load", this, "populateTerminalName");
		}
		}
	function populateTerminalName(type, data, evt) {
  		if(data){
   			document.getElementById("terminalName").value=data.terminalName;
   			}
	}
	function getTerminalNumber(ev){ 
		document.getElementById("terminalNumber").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalNumber";
			params['terminalName'] = document.addLclColoadPopupForm.terminalName.value;
			var bindArgs = {
	  			url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  			error: function(type, data, evt){alert("error");},
	 			mimetype: "text/json",
	  			content: params
			};
			var req = dojo.io.bind(bindArgs);
			dojo.event.connect(req, "load", this, "populateTerminalNumber");
		}
	}
	function populateTerminalNumber(type, data, evt) {
  		if(data){
   			document.getElementById("terminalNumber").value=data.terminalNumber;
   		}
	}
  function getdestPort(ev){ 
	 	document.getElementById("destAirportname").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.addLclColoadPopupForm.destSheduleNumber.value;
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
   			document.getElementById("destAirportname").value=data.terminalName;
   			}
	}
	function getDestPortName(ev){ 
		document.getElementById("destSheduleNumber").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalNumber";
			params['terminalName'] = document.addLclColoadPopupForm.destAirportname.value;
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
   			document.getElementById("destSheduleNumber").value=data.terminalNumber;
   		}
	}	 
		function getCodeDesc(ev){ 
		document.getElementById("comDescription").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.addLclColoadPopupForm.comCode.value;
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
			params['codeDesc'] = document.addLclColoadPopupForm.comDescription.value;
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
		<html:form action="/addLclColoadPopup" scope="request">
		<font color="blue"><b><%=message%></b></font>
			
  <table width="100%" class="tableBorderNew" border="0">
    <tr class="tableHeadingNew"> Add New </tr>
    <tr>
       <td> 
          
    			
			<table width=100%  border="0" cellpadding="0" cellspacing="0">
			<tr class="textlabels">
    			<td>Org Trm</td>
    			<td>
    			
    			    <input name="terminalNumber" id="terminalNumber" size="15" onkeydown="getTerminalName(this.value)"    value="<%=terminalNumber %>" maxlength="2"/>    
    				<dojo:autoComplete formId="addLclColoadPopupForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=ADD_LCL_COLOAD_POPUP&from=0"/>
    			
    			</td>
    			
    			<td>Org Trm Name</td>
    			<td>
    			  <input name="terminalName" id="terminalName" size="15"  onkeydown="getTerminalNumber(this.value)" value="<%=terminalName %>"  />    
    			   <dojo:autoComplete formId="addLclColoadPopupForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=ADD_LCL_COLOAD_POPUP&from=0"/>   			
     			</td>
    		</tr>
    		<tr class="textlabels">	
    			<td >Dest Port</td>
    			<td>
    		       <input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getdestPort(this.value)"  value="<%=destSheduleNumber%>" size="15" maxlength="5"/>    
    			   <dojo:autoComplete formId="addLclColoadPopupForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=ADD_LCL_COLOAD_POPUP&from=1"/>
    			</td>
    			
    			<td>Dest Port Name</td>
    			<td>
    			<input name="destAirportname" id="destAirportname"  onkeydown="getDestPortName(this.value)"  value="<%=destAirportname %>"  size="15"/>    
    		    	<dojo:autoComplete formId="addLclColoadPopupForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=ADD_LCL_COLOAD_POPUP&from=1"/>
    			</td>
    			
    		</tr>
    		<tr class="textlabels">	
    			<td>Com Code</td>
    			<td>
    			 <input name="comCode" id="comCode"  onkeydown="getCodeDesc(this.value)" value="<%=comCode %>" maxlength="6" size="15"/>    
    		    <dojo:autoComplete formId="addLclColoadPopupForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=ADD_LCL_COLOAD_POPUP"/>
    			</td>
    			
    			<td>Com Description </td>
    			<td>
                 <input name="comDescription" id="comDescription" size="15" onkeydown="getCode(this.value)" value="<%=comDesc %>" />    
    		    <dojo:autoComplete formId="addLclColoadPopupForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=ADD_LCL_COLOAD_POPUP"/>
    			</td>
    		</tr>
    		
    		<tr>
    		<td>&nbsp;</td>
    		<td>&nbsp;</td>
    		<td>
    		    <input type="button" value="Submit" onclick="searchform()" id="search" class="buttonStyleNew" />
    		 </td>
    		<td>&nbsp;</td>
    		</tr>	
    		</table>
    		
      </td>
    </tr>
  </table> 		
    		
    		<html:hidden property="buttonValue" styleId="buttonValue" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


