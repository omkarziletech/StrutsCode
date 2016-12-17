<%@ page language="java"
	import="java.util.ArrayList,com.gp.cong.logisoft.util.DBUtil,java.util.List,com.gp.cong.logisoft.domain.FclBuy"%>
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
	FclBuy fclBuy = new FclBuy();
	String terminalNumber = "";
	String terminalName = "";
	String destSheduleNumber = "";
	String destAirportname = "";
	String comCode = "";
	String comDesc = "";
	String message = "";
	String msg = "";
	String modify = "";
	String sslineno = "";
	String sslinename = "";
	String contact = "";
	String orgRegion="";
	String destRegion="";
	boolean flag = false;

	/*if(session.getAttribute("searchFclcodelist")!=null){

	 List li=(List)session.getAttribute("searchFclcodelist");
	 
	 if(li.size()>0){
	 session.removeAttribute("searchFclcodelist");
	 
	 }

	 }*/
	FclBuy lclNew = new FclBuy();
	if (request.getParameter("fclcorates") != null) {
		if (session.getAttribute("searchfclrecords") != null) {

			FclBuy sc1 = (FclBuy) session
			.getAttribute("searchfclrecords");
			lclNew.setOriginTerminal(sc1.getOriginTerminal());
			lclNew.setDestinationPort(sc1.getDestinationPort());
			lclNew.setComNum(sc1.getComNum());
			lclNew.setSslineNo(sc1.getSslineNo());
			lclNew.setOriginalRegion(sc1.getOriginalRegion());
			lclNew.setDestinationRegion(sc1.getDestinationRegion());
			session.setAttribute("addfclrecords", lclNew);
			flag = true;
		}

	}
	if (request.getAttribute("message") != null) {
		message = (String) request.getAttribute("message");

	}
	if (session.getAttribute("costcode") != null) {
		session.removeAttribute("costcode");
	}
	if (session.getAttribute("costCodeList") != null) {
		session.removeAttribute("costCodeList");
	}

	if (session.getAttribute("addfclrecords") != null) {

		fclBuy = (FclBuy) session.getAttribute("addfclrecords");
		if (fclBuy.getOriginTerminal() != null) {
			terminalNumber = fclBuy.getOriginTerminal().getUnLocationCode();
			terminalName = fclBuy.getOriginTerminal()
			.getUnLocationName();

		}
		
		if(fclBuy.getOriginalRegion()!=null)
		{
		orgRegion=fclBuy.getOriginalRegion();
		}
		if(fclBuy.getDestinationRegion()!=null)
		{
		destRegion=fclBuy.getDestinationRegion();
		}
		if (fclBuy.getDestinationPort() != null) {
			destSheduleNumber = fclBuy.getDestinationPort()
			.getUnLocationCode();
			destAirportname = fclBuy.getDestinationPort().getUnLocationName();
		}
		if (fclBuy.getComNum() != null) {
			comCode = fclBuy.getComNum().getCode();
			comDesc = fclBuy.getComNum().getCodedesc();
		}
		
		if (fclBuy.getSslineNo() != null) {
			sslineno = fclBuy.getSslineNo().getAccountno();
			sslinename = fclBuy.getSslineNo().getAccountName();
			//contact = fclBuy.getSslineNo().getFclContactNumber();
		}
	}
	List list = new ArrayList();
	DBUtil dbutil = new DBUtil();
	if (fclBuy != null) {
		list = dbutil.getFCLDetails(fclBuy.getOriginTerminal(), fclBuy
		.getDestinationPort(), fclBuy.getComNum(), fclBuy
		.getSslineNo(),fclBuy.getOriginalRegion(),fclBuy.getDestinationRegion());
		if (session.getAttribute("exist") == null) {
			if (list != null && list.size() > 0) {
						message="PLease Select another Commodity or else change Origin number or Destination port ";
		sslineno = "";
		sslinename = "";
		comCode = "";
		comDesc = "";
			}
		}
	}
	String address = "";
	if (session.getAttribute("exist") != null) {
		session.removeAttribute("exist");
	}
	if (request.getAttribute("sendfclcontrol") != null) {
		address = (String) request.getAttribute("sendfclcontrol");
	}
	if (request.getAttribute("msg") != null) {
		msg = (String) request.getAttribute("msg");
	}
	/*if(request.getAttribute("update")!=null)
	 {
	 message=(String)request.getAttribute("update");
	 }*/
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
	if (!terminalNumber.equals("") && !destSheduleNumber.equals("")
			&& !comCode.equals("") && !sslineno.equals("")) {
		if (message.equals("")) {

			if (address.equals("sendfclcontrol")) {
%>
<script>
	
		parent.parent.getFclCurrent();
		parent.parent.GB_hide();
		//opener.location.href="<%=path%>/jsps/ratemanagement/AddFCLRecords.jsp";
	</script>
<%
		}
		}
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
			document.addFCLPopupForm.buttonValue.value="search";
			document.addFCLPopupForm.submit();
			return false;
		}
		function searchform()
		{
		if(document.addFCLPopupForm.terminalNumber.value=="")
		{
		alert("Please select Terminal Number");
		return;
		}
		if(document.addFCLPopupForm.destSheduleNumber.value=="")
		{
		alert("Please select Destination Airport Code");
		return;
		}
		if(document.addFCLPopupForm.comCode.value=="")
		{
		alert("Please select Commodity Code");
		return;
		}
		if(document.addFCLPopupForm.sslinenumber.value=="")
		{
		alert("Please select SSLine Number Code");
		return;
		}
			document.addFCLPopupForm.buttonValue.value="search";
			document.addFCLPopupForm.submit();
		}
		function getSSLine(ev)
	{
	
		if(event.keyCode==9){
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button=searchfcl&ssline=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	
	function getSSLineName(ev)
	{
		if(event.keyCode==9){
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button=searchfcl&sslinename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function titleLetter(ev)
	{
		if(event.keyCode==9)
		{
		document.addFCLPopupForm.terminalName.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
 function getDestination(ev)
	{
		if(event.keyCode==9)
		{
		document.addFCLPopupForm.destAirportname.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCode(ev)
	{
		if(event.keyCode==9)
		{

		document.addFCLPopupForm.comDescription.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function titleLetterName(ev)
	{
		if(event.keyCode==9)
		{
		document.addFCLPopupForm.terminalNumber.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getDestinationName(ev)
	{
		if(event.keyCode==9)
		{
		document.addFCLPopupForm.destSheduleNumber.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeName(ev)
	{
		if(event.keyCode==9)
		{		
		document.addFCLPopupForm.comCode.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	 function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFCLPopupForm.terminalName.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
 function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFCLPopupForm.destAirportname.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFCLPopupForm.comDescription.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFCLPopupForm.terminalNumber.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.addFCLPopupForm.destSheduleNumber.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.addFCLPopupForm.comCode.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}	
	function getsslineno(ev)
	{
	  if(event.keyCode==9){
		document.addFCLPopupForm.sslinename.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	
	}
	}
	
	function getsslinenopress(ev)
	{
	  if(event.keyCode==13){
		document.addFCLPopupForm.sslinename.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	
	}
	}
	 
	 function getsslinename(ev)
	 {
	  if(event.keyCode==9){
		document.addFCLPopupForm.sslinenumber.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	
	}
	 }
	 function getsslinenamepress(ev)
	 {
	  if(event.keyCode==13){
		document.addFCLPopupForm.sslinenumber.value="";
		document.addFCLPopupForm.buttonValue.value="popupsearch";
		document.addFCLPopupForm.submit();
	
	}
	 }
	 
//type 2 dojo implementation
	 
	 
	 		 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.addFCLPopupForm.terminalNumber.value;
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
	var termName = '';
	function getTerminalNumber(ev){ 
		document.getElementById("terminalNumber").value="";
    	if(event.keyCode==9 || event.keyCode==13){
    		if(ev.indexOf("-")!=-1){
    	    	var string  = ev;
    	   		var index = ev.indexOf("-");
    	   		var orgTrm = string.substring(index+1,string.length);
    	    	var orgTrmName=string.substring(0,index);
    	  		document.addFCLPopupForm.terminalName.value=orgTrmName;
    	    	document.getElementById("terminalNumber").value=orgTrm;
    	    }else{
    	    	var params = new Array();
			  	params['requestFor'] = "OrgTerminalNumber";
			  	params['terminalName'] = document.addFCLPopupForm.terminalName.value;
			  	termName = document.addFCLPopupForm.terminalName.value;
			  	var bindArgs = {
	  		  		url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  		  		error: function(type, data, evt){alert("error");},
	 		  		mimetype: "text/json",
	  				content: params
				};
				
				document.addFCLPopupForm.terminalName.value=document.addFCLPopupForm.terminalName.value;
				var req = dojo.io.bind(bindArgs);
				dojo.event.connect(req, "load", this, "populateTerminalNumber");
			}
			
		}
	}
	function populateTerminalNumber(type, data, evt) {
  		if(data){
  		   	document.getElementById("terminalNumber").value=data.terminalNumber;
   			document.addFCLPopupForm.terminalName.value=termName;
   		}
	}
  function getdestPort(ev){ 
	 	document.getElementById("destAirportname").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.addFCLPopupForm.destSheduleNumber.value;
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
	var destPortName='';
	function getDestPortName(ev){ 
		document.getElementById("destSheduleNumber").value="";
    	if(event.keyCode==9 || event.keyCode==13){
    		if( ev.indexOf("-")!= -1){
				var string  = ev;
    	   		var index = ev.indexOf("-");
    	   		var destTrm = string.substring(index+1,string.length);
    	    	var destTrmName=string.substring(0,index);
				document.addFCLPopupForm.destAirportname.value = destTrmName;
				document.getElementById("destSheduleNumber").value= destTrm;
			}else{
				var params = new Array();
				destPortName = document.addFCLPopupForm.destAirportname.value;
				params['requestFor'] = "OrgTerminalNumber";
				params['terminalName'] = document.addFCLPopupForm.destAirportname.value;
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
	}
	function populateDestPortName(type, data, evt) {
  		if(data){
   			document.getElementById("destSheduleNumber").value=data.terminalNumber;
   			document.addFCLPopupForm.destAirportname.value = destPortName;
   		}
	}	 
		function getCodeDesc(ev){ 
		document.getElementById("comDescription").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.addFCLPopupForm.comCode.value;
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
			params['codeDesc'] = document.addFCLPopupForm.comDescription.value;
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
	
		function getSslineName(ev){ 
			document.getElementById("sslinename").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "SSLineName";
			params['custNo'] = document.addFCLPopupForm.sslinenumber.value;
			var bindArgs = {
	  			url: "<%=path%>/actions/getCustDetails.jsp",
	  			error: function(type, data, evt){alert("error");},
	 			mimetype: "text/json",
	  			content: params
			};
			var req = dojo.io.bind(bindArgs);
			dojo.event.connect(req, "load", this, "populateSslineName");
		}
	}
	function populateSslineName(type, data, evt) {
  		if(data){
   			document.getElementById("sslinename").value=data.custName;
   		}
	}
	
			function getSslineNo(ev){ 
			document.getElementById("sslinenumber").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "SSLineNo";
			params['custName'] = document.addFCLPopupForm.sslinename.value;
			var bindArgs = {
	  			url: "<%=path%>/actions/getCustDetails.jsp",
	  			error: function(type, data, evt){alert("error");},
	 			mimetype: "text/json",
	  			content: params
			};
			var req = dojo.io.bind(bindArgs);
			dojo.event.connect(req, "load", this, "populateSslineNo");
		}
	}
	function populateSslineNo(type, data, evt) {
  		if(data){
   			document.getElementById("sslinenumber").value=data.custNumber;
   		}
	}
	
 		
	</script>
	<%@include file="../includes/resources.jsp" %>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/addFCLPopup" scope="request">
	<% if(!flag){%>
		<font color="blue"><b><%=message%></b></font>
	<%} %>
			<table width=100%  border="0" cellpadding="0" cellspacing="0"  class="tableBorderNew">
			 <tr class="tableHeadingNew" height="90%"><b>Add Current FCL Rates</b> </tr>
			<tr>
    			<td><table width="100%" border="0"><tr class="style2"><td>Org Trm</td></tr></table></td>
    			<td>
<%--    			<html:text property="terminalNumber" size="2" onkeydown ="titleLetter(this.value)" maxlength="2" value="<%=terminalNumber%>"  />--%>
    			<input name="terminalNumber" id="terminalNumber" onkeydown="getTerminalName(this.value)" value="<%=terminalNumber%>"/>
	            <dojo:autoComplete formId="addFCLPopupForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=ADD_FCL&from=0"/>
    			</td>
    			
    			<td><span class="textlabels">Org Trm Name</span></td>
    			<td>
<%--    			<html:text property="terminalName" value="<%=terminalName%>" onkeydown="titleLetterName(this.value)"/>--%>
    			<input  name="terminalName" id="terminalName" value="<%=terminalName%>" onkeydown="getTerminalNumber(this.value)"/>
                <dojo:autoComplete formId="addFCLPopupForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=ADD_FCL&from=0"/>
    			</td>
    		</tr>
    		<tr>	
    			<td class="textlabels"><table width="100%" border="0"><tr class="style2"><td>Dest port</td></tr></table> </td>
    			<td>
<%--    			<html:text property="destSheduleNumber"  onkeydown="getDestination(this.value)" maxlength="5" value="<%=destSheduleNumber%>" />--%>
    			<input  name="destSheduleNumber" id="destSheduleNumber"  maxlength="5"    value="<%=destSheduleNumber%>"  onkeydown="getdestPort(this.value)" />
	            <dojo:autoComplete  formId="addFCLPopupForm" textboxId="destSheduleNumber"  action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=ADD_FCL&from=1"/>
    			</td>
    			
    			<td><span class="textlabels">Dest Port Name</span></td>
    			<td>
<%--    			<html:text property="destAirportname" value="<%=destAirportname%>"  onkeydown="getDestinationName(this.value)"/>--%>
    			<input name="destAirportname"  id="destAirportname" value="<%=destAirportname%>" onkeydown="getDestPortName(this.value)" />
                <dojo:autoComplete formId="addFCLPopupForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=ADD_FCL&from=1"/>
    			</td>
    			
    		</tr>
    		 <tr>
 			 <td class="style2">Org Region</td>
 			 <td><input name="orgRegion" id="orgRegion" value="<%=orgRegion%>">
 			<dojo:autoComplete formId="addFCLPopupForm" textboxId="orgRegion" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=ADD_FCL&from=0"/>
 			 </td>
 			 <td class="style2">Dest Region</td>
 			 <td><input name="destRegion" id="destRegion" value="<%=destRegion%>">
 			<dojo:autoComplete formId="addFCLPopupForm" textboxId="destRegion" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=ADD_FCL&from=1"/>
 			 </td>
 			 </tr>
    		<tr>	
    			<td><table width="100%" border="0"><tr class="style2"><td>Com Code</td></tr></table> </td>
    			<td>
<%--    			<html:text property="comCode" maxlength="6"  onkeydown="getComCode(this.value)" value="<%=comCode%>"  />--%>
    			  <input name="comCode"  id="comCode" maxlength="6"  onkeydown="getCodeDesc(this.value)" value="<%=comCode%>"/>
    			  <dojo:autoComplete formId="addFCLPopupForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=ADD_FCL"/>
    			</td>
    			
    			<td class="textlabels">Com Description </td>
    			<td>
<%--    			<html:text property="comDescription" value="<%=comDesc%>" onkeydown="getComCodeName(this.value)"/>--%>
    			  <input name="comDescription" id="comDescription" value="<%=comDesc%>" onkeydown="getCode(this.value)"/>
    			  <dojo:autoComplete formId="addFCLPopupForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=ADD_FCL&from=2"/>
    			</td>
    		</tr>
    		<tr>	
    			<td><table width="100%" border="0"><tr class="style2"><td>SS Line Number</td></tr></table> </td>
    			<td>
<%--    			<html:text property="sslinenumber"  size="6" onkeydown="getSSLine(this.value)" maxlength="5" value="<%=sslineno%>"  />--%>
    			<input name="sslinenumber" id="sslinenumber" maxlength="5" value="<%=sslineno%>" onkeydown="getSslineName(this.value)"/>
    			<dojo:autoComplete formId="addFCLPopupForm" textboxId="sslinenumber" action="<%=path%>/actions/getCustomer.jsp?tabName=ADD_FCL_POPUP&from=0"/>
    			</td>
    			
    			<td class="textlabels">SS Line Name </td>
    			<td>
<%--    			<html:text property="sslinename" value="<%=sslinename%>" onkeydown="getSSLineName(this.value)"/>--%>
    			<input name="sslinename" id="sslinename"  value="<%=sslinename%>" onkeydown="getSslineNo(this.value)"/>
    			<dojo:autoComplete formId="addFCLPopupForm" textboxId="sslinename" action="<%=path%>/actions/getCustomerName.jsp?tabName=ADD_FCL_POPUP&from=0"/>
    			</td>
    		</tr>
    		
    		
    		<tr>
    		<td>&nbsp;</td>
    		<td>&nbsp;</td>
    		<td>
    		    <input type="button" value="Submit" class="buttonStyleNew" onclick="searchform()"  id="search"/>
    		   
    		 </td>
    		<td>&nbsp;</td>
    		</tr>	
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue" />
    		<script>parent.parent.parent.parent.makeFormBorderless(document.getElementById("AddFclPopup"));</script>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


