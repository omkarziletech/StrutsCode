<%@ page language="java" pageEncoding="ISO-8859-1"
	import="com.gp.cong.logisoft.util.DBUtil,java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.FTFMaster,com.gp.cong.logisoft.beans.AirRatesBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FTFMaster ftfMaster = new FTFMaster();
	String terminalNumber = "";
	String terminalName = "";
	String destSheduleNumber = "";
	String destAirportname = "";
	String comCode = "";
	String comDesc = "";
	String message = "";
	String msg = "";
	String modify = "";
	AirRatesBean airRatesBean = new AirRatesBean();
	FTFMaster lclNew = new FTFMaster();
	airRatesBean.setCommon("");
	if (request.getAttribute("airRatesBean") != null) {
		airRatesBean = (AirRatesBean) request
		.getAttribute("airRatesBean");
	}
	request.setAttribute("airRatesBean", airRatesBean);
	if (request.getParameter("ftfrates") != null) {
		if (session.getAttribute("searchftfMaster") != null) {

			FTFMaster sc1 = (FTFMaster) session
			.getAttribute("searchftfMaster");
			lclNew.setOrgTerminal(sc1.getOrgTerminal());
			lclNew.setOrgTerminalName(sc1.getOrgTerminalName());
			lclNew.setDestPort(sc1.getDestPort());
			lclNew.setDestPortName(sc1.getDestPortName());
			//lclNew.setCommodityCode(sc1.getCommodityCode());

			session.setAttribute("addftfMaster", lclNew);
		}
	}
	if (session.getAttribute("addftfMaster") != null) {
		ftfMaster = (FTFMaster) session.getAttribute("addftfMaster");
		
			terminalNumber = ftfMaster.getOrgTerminal();
			terminalName = ftfMaster.getOrgTerminalName();
			destSheduleNumber = ftfMaster.getDestPort();
			destAirportname = ftfMaster.getOrgTerminalName();
			
			
		
	}
	List list = new ArrayList();
	DBUtil dbutil = new DBUtil();
	if (ftfMaster != null) {
		if (ftfMaster.getOrgTerminal() != null
		&& ftfMaster.getDestPort() != null
		&& ftfMaster.getComCode() != null) {
			list = dbutil.getftfDetails(ftfMaster.getOrgTerminal(),
			ftfMaster.getDestPort(), ftfMaster
			.getComCode());
			if (list != null && list.size() > 0) {
		message = "PLease Select another Commodity or else change Origin number or Destination port ";
		comCode = "";
		comDesc = "";
			}
		}
	}
	if (session.getAttribute("exist") != null) {
		session.removeAttribute("exist");
	}
	String ftfrate = "";
	if (request.getAttribute("ftfrate") != null) {
		ftfrate = (String) request.getAttribute("ftfrate");
	}
	if (request.getAttribute("msg") != null) {
		msg = (String) request.getAttribute("msg");
	}
	if (request.getAttribute("update") != null) {
		message = (String) request.getAttribute("update");

	}
	if (request.getParameter("modify") != null) {
		session.setAttribute("modifyforrelay", request
		.getParameter("modify"));
		modify = (String) session.getAttribute("modifyforrelay");
	} else {
		modify = (String) session.getAttribute("modifyforrelay");
	}

	if (message.equals("")) {
		if (ftfrate.equals("addftfrate")) {
%>
<script>
	     parent.parent.getFTF();
	     parent.parent.GB_hide();
	     reload_on_close = fasle;
		//self.close();
		//opener.location.href="<%=path%>/jsps/ratemanagement/FTFFrame.jsp";
	</script>
<%
	}
	}
%>



<html>
	<head>
		<title>JSP for AddFTFpopupForm form</title>
		<%@include file="../includes/baseResources.jsp"%>


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
			document.addFTFpopupForm.submit();
			return false;
		}
		
		function searchform()
		{
			if(document.addFTFpopupForm.terminalNumber.value=="")
			{
			alert("Please select Terminal Number");
			return;
			}
			if(document.addFTFpopupForm.destSheduleNumber.value=="")
			{
			alert("Please select Destination Port Code");
			return;
			}
			if(document.addFTFpopupForm.comCode.value=="")
			{
			alert("Please select Commodity Code Port Code");
			return;
			}
			  document.addFTFpopupForm.buttonValue.value="search";
				document.addFTFpopupForm.submit();
		}
		function titleLetter(ev)
	{
	if(event.keyCode==13)
	{
	document.addFTFpopupForm.terminalName.value="";
	document.addFTFpopupForm.buttonValue.value="popsearch";
	document.addFTFpopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailaddot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterDown(ev)
	{
	if(event.keyCode==9)
	{
	document.addFTFpopupForm.terminalName.value="";
	document.addFTFpopupForm.buttonValue.value="popsearch";
	document.addFTFpopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailaddot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestination(ev)
	{
		if(event.keyCode==13){
	document.addFTFpopupForm.destAirportname.value="";  	
	document.addFTFpopupForm.buttonValue.value="popsearch";
	document.addFTFpopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailaddairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCode(ev)
	{
	
		if(event.keyCode==13)
		{
		document.addFTFpopupForm.comDescription.value="";  	
		document.addFTFpopupForm.buttonValue.value="popsearch";
		document.addFTFpopupForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailaddcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	function titleLetterName(ev)
	{
		if(event.keyCode==13){
		document.addFTFpopupForm.terminalNumber.value="";	
		document.addFTFpopupForm.buttonValue.value="popsearch";
		document.addFTFpopupForm.submit();
	  //  window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailaddot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationName(ev)
	{
		if(event.keyCode==13)
		{
		document.addFTFpopupForm.destSheduleNumber.value="";  	
		document.addFTFpopupForm.buttonValue.value="popsearch";
		document.addFTFpopupForm.submit();
		//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailaddairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 }
	}
	function getComCodeName(ev)
	{
		if(event.keyCode==13)
		{
		document.addFTFpopupForm.comCode.value="";  	
		document.addFTFpopupForm.buttonValue.value="popsearch";
		document.addFTFpopupForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailaddcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationDown(ev)
	{
		if(event.keyCode==9){
		document.addFTFpopupForm.destAirportname.value="";  	
	document.addFTFpopupForm.buttonValue.value="popsearch";
	document.addFTFpopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailaddairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodeDown(ev)
	{
	
		if(event.keyCode==9)
		{
		document.addFTFpopupForm.comDescription.value="";  	
		document.addFTFpopupForm.buttonValue.value="popsearch";
		document.addFTFpopupForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailaddcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	function titleLetterNameDown(ev)
	{
		
		if(event.keyCode==9){
		document.addFTFpopupForm.terminalNumber.value="";
		document.addFTFpopupForm.buttonValue.value="popsearch";
		document.addFTFpopupForm.submit();
	  //  window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailaddot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationNameDown(ev)
	{
		if(event.keyCode==9)
		{
		document.addFTFpopupForm.destSheduleNumber.value="";  	
		document.addFTFpopupForm.buttonValue.value="popsearch";
		document.addFTFpopupForm.submit();
		//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailaddairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 }
	}
	function getComCodeNameDown(ev)
	{
		if(event.keyCode==9)
		{
		document.addFTFpopupForm.comCode.value="";  	
		document.addFTFpopupForm.buttonValue.value="popsearch";
		document.addFTFpopupForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailaddcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
		//new dojo implementation
	
	
			 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.addFTFpopupForm.terminalNumber.value;
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
			params['terminalName'] = document.addFTFpopupForm.terminalName.value;
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
			params['terminalNumber'] = document.addFTFpopupForm.destSheduleNumber.value;
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
			params['terminalName'] = document.addFTFpopupForm.destAirportname.value;
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
			params['code'] = document.addFTFpopupForm.comCode.value;
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
			params['codeDesc'] = document.addFTFpopupForm.comDescription.value;
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
		<%@include file="../includes/resources.jsp"%>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/addFTFpopup" scope="request">
			<font color="blue"><b><%=message%>
			</b>
			</font>
			<table width=100% border="0" cellpadding="0" cellspacing="4"
				class="tableBorderNew">
				<tr class="tableHeadingNew">
					Add New
				</tr>
				<tr>
					<td class="textlabels">
						Org Trm
					</td>
					<td>
						<input name="terminalNumber" id="terminalNumber" size="29"
							onkeydown="getTerminalName(this.value)"
							value="<%=terminalNumber%>" maxlength="2" />
						<dojo:autoComplete formId="addFTFpopupForm"
							textboxId="terminalNumber"
							action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=ADD_FTF_POPUP&from=0" />
					</td>
					<td class="textlabels">
						Org Trm Name
					</td>
					<td>
						<input name="terminalName" id="terminalName"
							onkeydown="getTerminalNumber(this.value)"
							value="<%=terminalName%>" size="29" />
						<dojo:autoComplete formId="addFTFpopupForm"
							textboxId="terminalName"
							action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=ADD_FTF_POPUP&from=0" />
					</td>
				</tr>
				<tr>
					<td class="textlabels">
						Dest Port
					</td>
					<td>
						<input name="destSheduleNumber" id="destSheduleNumber"
							onkeydown="getdestPort(this.value)"
							value="<%=destSheduleNumber%>" size="29" maxlength="5" />
						<dojo:autoComplete formId="addFTFpopupForm"
							textboxId="destSheduleNumber"
							action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=ADD_FTF_POPUP&from=1" />
					</td>
					<td class="textlabels">
						Dest Port Name
					</td>
					<td>
						<input name="destAirportname" id="destAirportname"
							onkeydown="getDestPortName(this.value)"
							value="<%=destAirportname%>" size="29" />
						<dojo:autoComplete formId="addFTFpopupForm"
							textboxId="destAirportname"
							action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=ADD_FTF_POPUP&from=1" />
					</td>
				</tr>
				<tr>
					<td class="textlabels">
						Com Code
					</td>
					<td>
						<input name="comCode" id="comCode"
							onkeydown="getCodeDesc(this.value)" maxlength="6"
							value="<%=comCode%>" size="29" />
						<dojo:autoComplete formId="addFTFpopupForm" textboxId="comCode"
							action="<%=path%>/actions/getChargeCode.jsp?tabName=ADD_FTF_POPUP" />
					</td>
					<td class="textlabels">
						Com Description
					</td>
					<td>
						<input name="comDescription" id="comDescription"
							value="<%=comDesc%>" onkeydown="getCode(this.value)" size="29" />
						<dojo:autoComplete formId="addFTFpopupForm"
							textboxId="comDescription"
							action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=ADD_FTF_POPUP" />
					</td>
				</tr>
				<tr>
					<td align="center" colspan="4">
						<input type="button" value="Submit" class="buttonStyleNew"
							onclick="searchform()" />
					<td>
						&nbsp;
					</td>
				</tr>
			</table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
