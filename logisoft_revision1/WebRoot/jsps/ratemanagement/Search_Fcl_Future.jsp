<%@ page import="java.text.DateFormat,java.text.DecimalFormat,java.text.ParseException,
java.util.*, java.text.SimpleDateFormat,com.gp.cong.logisoft.hibernate.dao.AuditLogRecordDAO,
com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO,com.gp.cong.logisoft.hibernate.dao.FclBuyDAO,
com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.beans.AirRatesBean,
com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FclBuy,
com.gp.cong.logisoft.domain.FclBuyCost,com.gp.cong.logisoft.domain.GenericCode,
com.gp.cong.logisoft.domain.FclBuyCostTypeFutureRates,java.text.DecimalFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<jsp:useBean id="airRatesBean" class="com.gp.cong.logisoft.beans.AirRatesBean" scope="request"/>   
<%
String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//airRatesBean.setMatch("match");
airRatesBean.setMatch("ocean");
String editpath="";
FclBuy fclBuy=new FclBuy();
String trmNum="";
String trmNam="";
String destAirPortNo="";
String destAirPortName="";
String comCode="";
String comDesc="";
String message="";
String modify="";
String sslineno="";
String sslinename="";
String orgCap="";
String destCap="";
String comCap="";; 
String ssLineCap="";
double d=0.0;
List records=new ArrayList();
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
AuditLogRecordDAO auditLog=new AuditLogRecordDAO();
DecimalFormat df=new DecimalFormat("0.00");
Map fclStdMap = new HashMap(); 
List searchLCLColoadList=new ArrayList();
if(session.getAttribute("trade")!=null) {
	session.removeAttribute("trade");
}
if(request.getAttribute("airRatesBean")!=null){
	airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
}
request.setAttribute("airRatesBean",airRatesBean);
if(request.getAttribute("worning")!=null){
	message=(String)request.getAttribute("worning");
}
if(session.getAttribute("futuresearchfclrecords")!=null){
	fclBuy=(FclBuy)session.getAttribute("futuresearchfclrecords");
	if(fclBuy.getOriginTerminal()!=null){
		trmNum=fclBuy.getOriginTerminal().getUnLocationCode();
		trmNam=fclBuy.getOriginTerminal().getUnLocationName();
		orgCap="Origin";
	}
	if(fclBuy.getDestinationPort()!=null){
		destAirPortNo=fclBuy.getDestinationPort().getUnLocationCode();
		destAirPortName=fclBuy.getDestinationPort().getUnLocationName();
		destCap = "Destination";
	}
	if(fclBuy.getComNum()!=null){
		comCode=fclBuy.getComNum().getCode();
		comDesc=fclBuy.getComNum().getCodedesc();
		comCap = "Commodity";
	}
	if(fclBuy.getSslineNo()!=null){
		sslineno=fclBuy.getSslineNo().getAccountno();
		sslinename=fclBuy.getSslineNo().getAccountName();
		ssLineCap = "SSLineNumber";
	}
}
if(session.getAttribute("fclfuturemessage")!=null){
	message=(String)session.getAttribute("fclfuturemessage");
}
if(request.getParameter("programid")!= null){
	modify=(String)request.getParameter("programid");
	session.setAttribute("processinfoforfl",modify);
}
		/*if(session.getAttribute("futureaddfclrecords")!=null){
		fclBuy=(FclBuy)session.getAttribute("futureaddfclrecords");
		FclBuyCostDAO fclBuyDAO=new FclBuyCostDAO();
		fclBuyDAO.removeRecords(fclBuy);
		}*/
		List commonList=new ArrayList();
if(session.getAttribute("searchFutureFclcodelist")!=null){
	records=(List)session.getAttribute("searchFutureFclcodelist");
}
editpath=path+"/serchFutureFCL.do";	
%>	
<html> 
<head>
<title>JSP for Search_Fcl_Future form</title>
<%@include file="../includes/baseResources.jsp" %>
<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
<script type="text/javascript">
    dojo.hostenv.setModulePrefix('utils', 'utils');
    dojo.widget.manager.registerWidgetPackage('utils');
    dojo.require("utils.AutoComplete");
    dojo.require("dojo.io.*");
	dojo.require("dojo.event.*");
	dojo.require("dojo.html.*");
</script>
<script language="javascript" type="text/javascript">
 var newwindow = '';  
function popup1(mylink, windowname){
			if (!window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
	   		href=mylink;
			else
	   		href=mylink.href;
			mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
			mywindow.moveTo(200,180);
			document.serchFutureFCLForm.buttonValue.value="search";
			document.serchFutureFCLForm.submit();
			return false;
}
function searchform(){
	if(document.serchFutureFCLForm.terminalNumber.value=="" && document.serchFutureFCLForm.destSheduleNumber.value=="" && document.serchFutureFCLForm.comCode.value=="" && document.serchFutureFCLForm.sslinenumber.value=="" ){
		alert("Please select EitherOrg Trm or Dest port or Com Code or SS Line number	");
		return;
	}
	var selected = false;
	for(var i=0;i<document.serchFutureFCLForm.match.length;i++){
		if(document.serchFutureFCLForm.match[i].checked){
			selected = true;
		}
	}
	if(!selected){
		alert("Please select either Ocean or Match Only or StartsList");
		return;
	}
	document.serchFutureFCLForm.buttonValue.value="search";
	document.serchFutureFCLForm.index.value="get";
	document.serchFutureFCLForm.submit();
}
function searchallform(){
	document.serchFutureFCLForm.buttonValue.value="showall";
	document.serchFutureFCLForm.submit();
}
function addform() {
 if (!newwindow.closed && newwindow.location){
  	newwindow.location.href = "<%=path%>/jsps/ratemanagement/addFutureFclPop.jsp";
 }else{
	newwindow=window.open("<%=path%>/jsps/ratemanagement/addFutureFclPop.jsp?futurefclcorates="+"add","","width=700,height=200");
    if (!newwindow.opener) newwindow.opener = self;
 }
 if (window.focus) {newwindow.focus()}
  return false;
 } 
 function uploadfile() {
	 if (!newwindow.closed && newwindow.location){
         newwindow.location.href = "<%=path%>/jsps/ratemanagement/LoadFCLPopup.jsp";
      } else  {
          newwindow=window.open("<%=path%>/jsps/ratemanagement/LoadFCLPopup.jsp?futureratesload="+"add","","width=400,height=250");
          if (!newwindow.opener) newwindow.opener = self;
      }
      if (window.focus) {newwindow.focus()}
          return false;
      }
      function  disabled(val1){
	          /*if(val == 0)
				{		
	        	var imgs = document.getElementsByTagName('img');
	   			for(var k=0; k<imgs.length; k++)
	   			{
	   		 		if(imgs[k].id != "showall" && imgs[k].id!="search")
	   		 		{
	   		 			imgs[k].style.visibility = 'hidden';
	   		 		}
	   			}
	  		 }*/
	    var tables1 = document.getElementById('lclcoloadratestable');
	  	if(tables1!=null){
	  	 	 //displaytagcolor();
	   		 //initRowHighlighting();
	   		 //setWarehouseStyle();
	   	}
	    var tables2 = document.getElementById('comlclcoloadratestable');
	  		 if(tables2!=null){
	  	 	 //displaytagcolorcom();
	   		 //initRowHighlightingcom();
	   		 //setWarehouseStyle();
	   		 }
	  		  
          }

function displaytagcolor(){
	var datatableobj = document.getElementById('lclcoloadratestable');
    for(i=0; i<datatableobj.rows.length; i++){
	var tablerowobj = datatableobj.rows[i];
	if(i%2==0){
		tablerowobj.bgColor='#FFFFFF';
	}else{
		tablerowobj.bgColor='#E6F2FF';
	}
	if(tablerowobj.cells[1].innerHTML=="yes"){
		//alert(tablerowobj.cells[1].innerHTML);
		//tablerowobj.cells[1].innerHTML="*";
		//	tablerowobj.cells[1].style.color='#FF3300';
		//tablerowobj.cells[1].style.fontSize=20;
		//
	}
 }
}
function initRowHighlighting()
{
if (!document.getElementById('lclcoloadratestable')){ return; }
 var tables = document.getElementById('lclcoloadratestable');
 attachRowMouseEvents(tables.rows);
}
function attachRowMouseEvents(rows){
for(var i =1; i < rows.length; i++){
	var row = rows[i];
	row.onmouseover =	function() 
	{ 
	this.className = 'rowin';
	}
	row.onmouseout =	function() 
	{ 
	this.className = '';
	}
	row.onclick= function() 
	{ 
	}
	}
}
function displaytagcolorcom(){
	var datatableobj1 = document.getElementById('comlclcoloadratestable');
	for(i=0; i<datatableobj1.rows.length; i++){
	var tablerowobj = datatableobj1.rows[i];
	if(i%2==0){
		tablerowobj.bgColor='#FFFFFF';
	}else{
		tablerowobj.bgColor='#E6F2FF';
	}
 	}
}
function initRowHighlightingcom(){
if (!document.getElementById('comlclcoloadratestable')){ return; }
 var tables = document.getElementById('comlclcoloadratestable');
 attachRowMouseEventscom(tables.rows);
}
function attachRowMouseEventscom(rows){
for(var i =1; i < rows.length; i++){
	var row = rows[i];
	row.onmouseover =	function() 
	{ 
	this.className = 'rowin';
	}
	row.onmouseout =	function() 
	{ 
	this.className = '';
	}
	row.onclick= function() 
	{ 
	}
	}
}
function setWarehouseStyle(){
if(document.searchFTFForm.buttonValue.value=="searchall"){
	var x=document.getElementById('lclcoloadratestable').rows[0].cells;
    x[0].className="sortable sorted order1";
}
if(document.searchFTFForm.buttonValue.value=="search"){
	var input = document.getElementsByTagName("input");
    if(!input[0].value==""){
		  		 	var x=document.getElementById('lclcoloadratestable').rows[0].cells;	
		  		   	x[0].className="sortable sorted order1";
		  		}
		  		else if(!input[4].value=="")
		  		{	
		  		 	var x=document.getElementById('lclcoloadratestable').rows[0].cells;	
		  		   	x[2].className="sortable sorted order1";
		  		}
		  		else if(!input[6].value=="")
		  		{	
		  		 	var x=document.getElementById('lclcoloadratestable').rows[0].cells;	
		  		   	x[4].className="sortable sorted order1";
		  		}
		  		
		  	  }
	 }
   
  //--DOJO CODES----
   
   function getOrgTerminalName(ev)
	{
		if(event.keyCode==9 ||event.keyCode==13)
		{
			 var params = new Array();
				 params['requestFor'] = "OrgTerminalName";
				 alert(params['requestFor']);
				 params['terminalNumber'] = document.serchFutureFCLForm.terminalNumber.value;
				  var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateOrgTerminalNameDojo");
		
			//document.serchFutureFCLForm.terminalName.value="";
			//document.serchFutureFCLForm.buttonValue.value="popupsearch";
			//document.serchFutureFCLForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
    function populateOrgTerminalNameDojo(type, data, evt) {
		   	if(data){
		   		document.getElementById("terminalName").value=data.terminalName
		   	}
		   }
   
   
   
   function getOrgTerminalNumber(ev)
	{
		if(event.keyCode==9 ||event.keyCode==13)
		{
		     var params = new Array();
				 params['requestFor'] = "OrgTerminalNumber";
				 params['terminalName'] = document.serchFutureFCLForm.terminalName.value;
				
				  var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateOrgTerminalNumberDojo"); 
		
		
		//document.serchFutureFCLForm.terminalNumber.value="";
		//document.serchFutureFCLForm.buttonValue.value="popupsearch";
		//document.serchFutureFCLForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
   function populateOrgTerminalNumberDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("terminalNumber").value=data.terminalNumber
		   	    }
		   }
		   
   function getDestinationPortName(ev)
	{
		if(event.keyCode==9 ||event.keyCode==13)
		{
			 var params = new Array();
					 params['requestFor'] = "DestinationPortName";
					 params['destSheduleNumber'] = document.serchFutureFCLForm.destSheduleNumber.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateDestinationPortNameDojo");
		
		//document.serchFutureFCLForm.destAirportname.value="";
		//document.serchFutureFCLForm.buttonValue.value="popupsearch";
		//document.serchFutureFCLForm.submit();
	   //window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	  }
	}
    function populateDestinationPortNameDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("destAirportname").value=data.destAirportname
		   	    }
		    }
  function getDestinationPortNumber(ev)
	{
		if(event.keyCode==9 ||event.keyCode==13)
		{
			var params = new Array();
					 params['requestFor'] = "DestinationPortNumber";
					 params['destinationPortName'] = document.serchFutureFCLForm.destAirportname.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateDestinationPortNumberDojo");
			
		//document.serchFutureFCLForm.destSheduleNumber.value="";
		//document.serchFutureFCLForm.buttonValue.value="popupsearch";
		//document.serchFutureFCLForm.submit();
	    //window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	  }
	}
	function populateDestinationPortNumberDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("destSheduleNumber").value=data.destPortNumber
		   	    }
		    } 
   function getComCodeDescription(ev)
	{
		if(event.keyCode==9 ||event.keyCode==13)
		{
			var params = new Array();
					 params['requestFor'] = "CommodityCodeDescription";
					 params['commodityCode'] = document.serchFutureFCLForm.comCode.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateCommodityDescriptionDojo");
		
		//document.serchFutureFCLForm.comDescription.value="";
		//document.serchFutureFCLForm.buttonValue.value="popupsearch";
		//document.serchFutureFCLForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	} 
	function populateCommodityDescriptionDojo(type, data, evt) {
		if(data){
		   		  document.getElementById("comDescription").value=data.commodityDescription
		   }
	}
	
   function getComCode(ev)
	{
		if(event.keyCode==9 ||event.keyCode==13)
		{	
				 var params = new Array();
					 params['requestFor'] = "CommodityCode";
					 params['commodityCodeDescription'] = document.serchFutureFCLForm.comDescription.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateCommodityCodeDojo");
		
		  //document.serchFutureFCLForm.comCode.value="";
		 //document.serchFutureFCLForm.buttonValue.value="popupsearch";
		 //document.serchFutureFCLForm.submit();
	    //window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
		}
	}
	function populateCommodityCodeDojo(type, data, evt) {
		if(data){
		   		document.getElementById("comCode").value=data.commodityCode
		 }
	 }   
		     
   function getSSlineName(ev)
	{
	  if(event.keyCode==9 ||event.keyCode==13){
		var params = new Array();
					 params['requestFor'] = "SSLineName";
					 params['ssLineNumber'] = document.serchFutureFCLForm.sslinenumber.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateSSLineNameDojo");
		
		//document.serchFutureFCLForm.sslinename.value="";
		//document.serchFutureFCLForm.buttonValue.value="popupsearch";
		//document.serchFutureFCLForm.submit();
	
		}
	}
	function populateSSLineNameDojo(type, data, evt) {
		if(data){
		   		document.getElementById("sslinename").value=data.ssLineName
		}
	}
		     
    function getSSlineNumber(ev)
	 {
	 	if(event.keyCode==9 ||event.keyCode==13){
		var params = new Array();
					 params['requestFor'] = "SSLineNo";
					 params['ssLineName'] = document.serchFutureFCLForm.sslinename.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateSSLineNoDojo");
		
		//document.serchFutureFCLForm.sslinenumber.value="";
		//document.serchFutureFCLForm.buttonValue.value="popupsearch";
		//document.serchFutureFCLForm.submit();
	
		}
	 }
   function populateSSLineNoDojo(type, data, evt) {
	if(data){
		   	  document.getElementById("sslinenumber").value=data.ssLineNo
		}
	}
   
	 function ClearScreen()
	 {
	    document.serchFutureFCLForm.buttonValue.value="clear";
		document.serchFutureFCLForm.submit();
	 }
	
	 function getFclFuture()
	 {
	  window.location.href="<%=path%>/jsps/ratemanagement/addFutureFcl.jsp";
	 }
		</script>
				<%@include file="../includes/resources.jsp" %>
	</head>
	<%if(session.getAttribute("fclFutureCollaps")!=null){ %>
	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
		<%}else{ %>
	
	<body class="whitebackgrnd">
		<%} %>
	
		<html:form action="/serchFutureFCL" scope="request">
		<font color="blue" size="4"><%=message%></font>
	<%
 		 if(session.getAttribute("fclFutureCollaps")==null){
%>
<table width="100%" border="0" cellpadding="1" cellspacing="0"  class="tableBorderNew">
<tr class="tableHeadingNew" height="100%"><td ><b>Search Future FCL Rates </b></td>
<td align="right"><input type="button" value="Import Excel" onclick="GB_show('Import Excel','<%=path%>/jsps/ratemanagement/LoadFCLPopup.jsp?futureratesload='+'add',150,600)" id="addnew" class="buttonStyleNew" style="width=80px"/></td>
  		
</tr>
<tr>
<td colspan="2">
<table width="100%" border="0" cellpadding="0" cellspacing="4">
<tr>
	<td class="textlabels">Org Trm</td>	  			
 	<td class="textlabels"><input name="terminalNumber" id="terminalNumber" onkeydown="titleLetter(this.value)" value="<%=trmNum%>" onkeypress="getOrgTerminalName(this.value)" maxlength="2" size="30"/>
	    <dojo:autoComplete formId="serchFutureFCLForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCode.jsp?tabName=SEARCH_FCL_FUTURE&from=0"/></td>	
     <td class="textlabels">Org Trm Name</td>
	<td class="textlabels" ><input  name="terminalName" id="terminalName" value="<%=trmNam%>" onkeydown="getOrgTerminalNumber(this.value)" size="30"/>
        <dojo:autoComplete formId="serchFutureFCLForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_FCL_FUTURE&from=0"/></td>
    
</tr>
<tr>
    <td class="textlabels">Dest port</td>
    <td class="textlabels"><input  name="destSheduleNumber" id="destSheduleNumber" maxlength="5"    value="<%=destAirPortNo%>"  onkeydown="getDestinationPortName(this.value)" size="30"/>
	    <dojo:autoComplete  formId="serchFutureFCLForm" textboxId="destSheduleNumber"  action="<%=path%>/actions/getUnlocationCode.jsp?tabName=SEARCH_FCL_FUTURE&from=1"/></td>
    <td class="textlabels">Dest Port Name</td>
    <td class="textlabels"><input name="destAirportname"  id="destAirportname" value="<%=destAirPortName%>" onkeydown="getDestinationPortNumber(this.value)" size="30" />
        <dojo:autoComplete formId="serchFutureFCLForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_FCL_FUTURE&from=1"/></td>
	</tr>
<tr>
    <td class="textlabels">Com Code</td>
    <td class="textlabels"><input name="comCode"  id="comCode" maxlength="6"   value="<%=comCode%>"  onkeydown="getComCodeDescription(this.value)" size="30"/>
    	<dojo:autoComplete formId="serchFutureFCLForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=SEARCH_FCL_FUTURE"/></td>
    <td class="textlabels">Com Description </td>
	<td ><input name="comDescription" id="comDescription" value="<%=comDesc%>" onkeydown="getComCode(this.value)" size="30"/>
    	<dojo:autoComplete formId="serchFutureFCLForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=SEARCH_FCL_FUTURE"/></td>    			

</tr>
<tr>

    <td class="textlabels">SS Line Code</td>
    <td class="textlabels"><input name="sslinenumber" id="sslinenumber"  maxlength="5" value="<%=sslineno%>" onkeydown="getSSlineName(this.value)" size="30"/>
    	<dojo:autoComplete formId="serchFutureFCLForm" textboxId="sslinenumber" action="<%=path%>/actions/getSsLineNo.jsp?tabName=SEARCH_FCL_FUTURE&from=0"/></td>
	<td class="textlabels">SS Line Name</td>
    <td ><input name="sslinename" id="sslinename"  value="<%=sslinename%>" onkeydown="getSSlineNumber(this.value)" size="30"/>
        <dojo:autoComplete formId="serchFutureFCLForm" textboxId="sslinename" action="<%=path%>/actions/getSsLineName.jsp?tabName=SEARCH_FCL_FUTURE&from=0"/></td>
</tr>
<tr>
    <td class="textlabels"><html:radio property="match" value="ocean" name="airRatesBean" />Ocean Freight</td>
	<td class="textlabels"> <html:radio property="match" value="match"  name="airRatesBean" />Match Only</td>			

</tr>
<tr>
    <td align="Center" colspan="6"><input type="button" value="Search" onclick="searchform()" id="search" class="buttonStyleNew" size="30"/>
    <input type="button" value="Clear" onclick="ClearScreen()" id="cancel" class="buttonStyleNew" /></td>
</tr>
</table>
</td>
</tr>
</table>
<br style="margin-top:5px"/>
	<%}else{ %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
		<td align="right">
		           <input type="button" value="Search" onclick="ClearScreen()" id="cancel" class="buttonStyleNew" />
  				   <input type="button" value="AddNew" onclick="return GB_show('FCL Future','<%=path%>/jsps/ratemanagement/addFutureFclPop.jsp?futurefclcorates='+'add',200,600)" id="addnew" class="buttonStyleNew" />
		</td>
</tr>
		<tr>
			<td>
	
    	<table width="100%" class="displaytagstyle" border="0" cellpadding="0" cellspacing="1">
    		<thead>
				<tr>
					<td ><b>
						<%=orgCap%>
					</td>
					<td>
						<b><%=destCap%>
					</td>
					<td><b>
						<%=comCap%>				
					</td>
					<td><b>
						<%=ssLineCap%>				
					</td>
				</tr>
			</thead>	
				<tr class="even">
					<td class="textlabels"><%=trmNam%></td>
	  				<td class="textlabels"><%=destAirPortName%></td>
	   		   		<td class="textlabels"><%=comDesc%></td>
	   		   		<td class="textlabels"><%=sslinename%>
   			</tr>
     	</table>
   	</td>
   	</tr>
   </table>
  <br style="margin-top:5px"/> 
  			
	<table width="100%" class="tableBorderNew" border="0" cellpadding="0" cellspacing="0">
  		<tr class=textlabelsWhite bgcolor="#0099ff" ><%=session.getAttribute("fclFuturecaption")%> </tr>	
		<tr><td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;
          		%>
          	 	<display:table name="<%=records%>"  pagesize="<%=pageSize%>"  class="displaytagstyle"  id="lclcoloadratestable" sort="list"  style="width:100%" > 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> FCL Buy Rates Displayed,For more Data click on Page Numbers.
    				<br>
    			</span>
  			  	</display:setProperty>
  				<display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
						1 {0} displayed. Page Number
					</span>
  			 	</display:setProperty>
  			    <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
						{0} {1} Displayed, Page Number
				</span>
  			    </display:setProperty>
    			<display:setProperty name="basic.msg.empty_list">
    			    <span class="pagebanner">
						
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="FCL BUY Rates"/>
				<display:setProperty name="paging.banner.items_name" value="FCL BUY Rates"/>
  				
  				<%
  				 String unitycode="";
				   	String code="";
  					String codedesc="";
  					String codetype="";
  					String costcode="";
  					String Commodity="";
  					String retail="";
  					String org="";
  					String dest="";
  					String codeNum="";
  					String ssline="";
  					double rate=0.0;
  					double ctc=0.0;
  					double ftf=0.0;
  					double min=0.0;
  					String aircurr="";
  					String ratAmount[]=new String[50];
  					String Markup[]=new String[50];
  					String Total[]=new String [50];
  					String curr[]=new String[50];
  					String efdate[]=new String[50];
  					List li=new ArrayList();
  					int count=0;
  					String retot="";
  					String link="";
  					String orgDesc="";
  					String sslineDesc="";
  					String destDesc="";
  					String CodeDesc="";
  					String moreInfo="";
  					String link1 ="";
  					boolean fclflag=false;
  					List unittypelist=new ArrayList();
  					//List recordsList=new ArrayList();
  					Set set=new HashSet();
  					int k=0;
  					String note="";
  					String efdaterat="";
					if(session.getAttribute("searchunittypelist")!=null){
					unittypelist=(List)session.getAttribute("searchunittypelist");
					}
  					FclBuyCost fclBuyCost=new FclBuyCost();
					FclBuy fclbuy=new FclBuy();
  					FclBuyDAO fcl=new FclBuyDAO();
  					if (records != null && records.size() > 0)
  					{
  					
  						 fclBuyCost=(FclBuyCost)records.get(i);
  						 if(fclBuyCost!=null){
  						 fclbuy=fcl.findById(fclBuyCost.getFclStdId());
  						 
  						List audit=auditLog.findAllFclRecc(fclBuyCost.getFclStdId().toString());
  						if(audit!=null && audit.size()>0){
  						//note="yes";
  						}
  						 }
  						 if(fclbuy!=null && fclStdMap.containsKey(fclBuyCost.getFclStdId())==false ){
  						 fclStdMap.put(fclBuyCost.getFclStdId(),fclBuyCost.getFclStdId());
  						 
  						 moreInfo = "More Info";
  						 org=fclbuy.getOriginTerminal().getUnLocationCode();
  						 orgDesc = fclbuy.getOriginTerminal().getUnLocationName();
  						 dest=fclbuy.getDestinationPort().getUnLocationCode();
  						 destDesc=fclbuy.getDestinationPort().getUnLocationName();
  						 codeNum=fclbuy.getComNum().getCode();
  						 CodeDesc=fclbuy.getComNum().getCodedesc();
  						 ssline=fclbuy.getSslineNo().getAccountno();
  						 sslineDesc=fclbuy.getSslineNo().getAccountName();
  						 
  						 }
  						if(session.getAttribute("futureaddfclrecords")!=null){
  						fclBuy=(FclBuy)session.getAttribute("futureaddfclrecords");
  						if(fclBuy.getFclStdId()!=null){
  						if(fclBuyCost.getFclStdId()==fclBuy.getFclStdId()){
  						Commodity=fclBuy.getComNum().getCode();
  						}
  						}
  						
  						}
						
					 	if( fclBuyCost.getCostId()!=null)
						 	{

						 		costcode=fclBuyCost.getCostId().getCodedesc();
		  						code=fclBuyCost.getCostId().getCode(). toString();
		  						codetype=fclBuyCost.getContType().getCode();
		  						  					
		  						}

						if(fclBuyCost.getFclBuyFutureTypesSet()!=null)
					 		{
					 				
					 			 FclBuyCostTypeFutureRates fclBuyCostTypeRates1=null;
								 Iterator iter=fclBuyCost.getFclBuyFutureTypesSet().iterator();
				    	 		while(iter.hasNext())
					 				{
										 count++;
										 fclBuyCostTypeRates1=(FclBuyCostTypeFutureRates)iter.next();
										 if(fclBuyCostTypeRates1.getRatAmount()!=null){
										 retail=df.format(fclBuyCostTypeRates1.getRatAmount());
										 rate=fclBuyCostTypeRates1.getRatAmount();
										
										}
										if(fclBuyCostTypeRates1.getCtcAmt()!=null){
										ctc=fclBuyCostTypeRates1.getCtcAmt();
										}
										if(fclBuyCostTypeRates1.getFtfAmt()!=null){
										ftf=fclBuyCostTypeRates1.getFtfAmt();
										}
										if(fclBuyCostTypeRates1.getMinimumAmt()!=null){
										min=fclBuyCostTypeRates1.getMinimumAmt();
										}
										retot=df.format(rate+ctc+ftf+min);
										
									 if(fclBuyCostTypeRates1.getUnitType()!=null)
							 	     {
										unitycode=fclBuyCostTypeRates1.getUnitType().getCodedesc();
										if(fclBuyCostTypeRates1.getCurrency()!=null){
												 if(unitycode!=null){
												 for(int j=0;j<unittypelist.size();j++){
												 	String unity=(String)unittypelist.get(j);
													 if(unity.equals(unitycode)){
														 String cur=fclBuyCostTypeRates1.getCurrency().getCodedesc();
														 curr[j]=cur.substring(0,3);
													 }
												   }
												  }
												 }
							 		 }
							 		  else{
							 		  		if(fclBuyCostTypeRates1.getCurrency()!=null){
							 		    	String cur1=fclBuyCostTypeRates1.getCurrency().getCodedesc();
							 			    aircurr=cur1.substring(0,3);
							 				 }
							 				}	 
							 					
									 if(fclBuyCostTypeRates1.getUnitType()!=null)
							 	     {
										unitycode=fclBuyCostTypeRates1.getUnitType().getCodedesc();
										if(fclBuyCostTypeRates1.getEffectiveDate()!=null){
												 if(unitycode!=null){
												 for(int j=0;j<unittypelist.size();j++){
												 	String unity=(String)unittypelist.get(j);
													 if(unity.equals(unitycode)){
														efdate[j]=dateFormat.format(fclBuyCostTypeRates1.getEffectiveDate());
														 
														 
													 }
												   }
												  }
												 }
							 		 }
							 		  else{
							 		  		if(fclBuyCostTypeRates1.getEffectiveDate()!=null){
							 		    	efdaterat=dateFormat.format(fclBuyCostTypeRates1.getEffectiveDate());
							 				 }
							 				}		
										 if(fclBuyCostTypeRates1.getActiveAmt()!=null)
												 {
												 if(unitycode!=null){
												 for(int j=0;j<unittypelist.size();j++){
												 String unity=(String)unittypelist.get(j);
												 if(unity.equals(unitycode)){
												 ratAmount[j]=df.format(fclBuyCostTypeRates1.getActiveAmt());
												 }
												 }
												 }
												 }
												 if(fclBuyCostTypeRates1.getMarkup()!=null){
												 if(unitycode!=null){
												 for(int j=0;j<unittypelist.size();j++){
												 String unity=(String)unittypelist.get(j);
												 if(unity.equals(unitycode)){
												 //String activeAmt=df.format(fclBuyCostTypeRates1.getMarkup());
												 Markup[j]=df.format(fclBuyCostTypeRates1.getMarkup());
												 }
												 }
												 }
												 }
												 //THIS CODE IS FOR DISPLAY CURRENCY CODDE
												
												 if(unitycode!=null){
												 double active=0;
												 double mark=0;
												 boolean b=false;
												 for(int j=0;j<unittypelist.size();j++){
												 if(Markup[j]!=null && !Markup[j].equals("")){
												 mark=Double.parseDouble(Markup[j]);
												 b=true;
												 }
												 if(ratAmount[j]!=null && !ratAmount[j].equals("")){
												 active=Double.parseDouble(ratAmount[j]);
												 b=true;
												 }
												 Double.toString(mark+active);
											     double sum=mark+active;
												 if(b){
												 Total[j]=df.format(sum);
												 b=false;
												 }
												 }
												 }
												 
												
										
		  							}
  							}
  							String iStr=String.valueOf(i);
  							
	  						link=editpath+"?ind="+iStr+"&paramid="+fclBuyCost.getFclStdId();
	  						link1 = editpath+"?param="+fclBuyCost.getFclStdId();
  						}
  						
  		 		%>
  		 		
  		 		
  		 		
				<display:column   title="Origin" ><a href="<%=link %>" >
					<span class="hotspot" onmouseover="tooltip.show('<strong><%=orgDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=org%></a>
					</span></display:column>
				<display:column title="Destination">
					<span class="hotspot" onmouseover="tooltip.show('<strong><%=destDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=dest%>
				</span></display:column>
				<display:column title="Commodity">
				<span class="hotspot" onmouseover="tooltip.show('<strong><%=CodeDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=codeNum%>
				</span></display:column>				
				<display:column title="Carrier">
				<span class="hotspot" onmouseover="tooltip.show('<strong><%=sslineDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=ssline%></span> </display:column>				
				<display:column title="Code">
				<span class="hotspot" onmouseover="tooltip.show('<strong><%=costcode%></strong>',null,event);" onmouseout="tooltip.hide();"><%=code+"/"+codetype%></span> 
				</display:column>
				<display:column title="Currency"><%=aircurr%></display:column>
				<display:column title="Retail"><%=retail%></display:column>
				<display:column title="Total"><%=retot%></display:column>
				
			   <display:column  title="&nbsp;&nbsp;Eff Date">&nbsp;&nbsp;<%=efdaterat%></display:column>
			
				<%
				if(unittypelist!=null){
				for(int j=0;j<unittypelist.size();j++)
				{
				
				%>
				<display:column  title="Currency"><%=curr[j]%></display:column>
				<display:column  title="<%=(String)unittypelist.get(j)%>"><%=ratAmount[j]%></display:column>
				<display:column title="Markup"><%=Markup[j]%></display:column>
				<display:column  title="Eff Date "><%=efdate[j]%></display:column>
				<display:column  title="Total "><%=Total[j]%></display:column>
				<%}
				}i++; %>
				
				<display:column title="Actions">
				   <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);" onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" border="0" onclick="window.location.href='<%=link1 %>'" /></span>
				</display:column>
		 		</display:table>
		 
   	
</td></tr></table>

<table>
<%--<tr><td colspan="5"></td><hr color="#0099ff"></tr>--%>
<tr class="style2">

<td>A->Flat Rate Per Container Size</td>
<td>B->Per Cubic Foot</td>
<td>C->Per CBM</td>
<td>D->per LBS</td>
<td>E->Per 1000KG</td>
</tr>
<tr class="style2">
<td>F->Per Dock Receipts</td>
<td>G->Air Freight Costs</td>
<td>H->Per BL Charges</td>
<td>I->Flat Rate Per Container</td>
<td>K->PER 2000 LBS</td>
</tr>
<tr class="style2">
<td>Per->Percent Of OFR</td>
</tr>
</table>
<%} %>
<html:hidden property="buttonValue" styleId="buttonValue" />
<html:hidden property="index"  />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


