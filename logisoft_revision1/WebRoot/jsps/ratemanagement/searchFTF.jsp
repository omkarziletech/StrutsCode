<%@ page language="java"  import="java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.hibernate.dao.FTFMasterDAO,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FTFMaster,com.gp.cong.logisoft.domain.FTFCommodityCharges,com.gp.cong.logisoft.domain.FTFDetails"%>
<jsp:directive.page import="com.gp.cong.logisoft.domain.FTFStandardCharges"/>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../ratemanagement/CalculateBottomLineRates.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat per = new DecimalFormat("0.000");
	DateFormat dateFormat1 = new SimpleDateFormat("hh:mm aaa");
	List Stdcommon=new ArrayList();
	List noncommon=new ArrayList();
	FTFMaster ftfMaster=new FTFMaster();
	List searchftfList=new ArrayList();
	DBUtil dbUtil=new DBUtil();
	String Port="";
	String trmNum="";
	String trmNam="";
	String destAirPortNo="";
	String destAirPortName="";
	String comCode="";
	String comDesc="";
	String editPath=path+"/searchFTF.do";
	String buttonValue="load";
	String message="";
	String modify="";
	String coll="";
	String defaultRate="";
	String noncomCaps="Ocean Freight Rates";
	String comCaps="Common Accessorial Charges";
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	if(session.getAttribute("serachftfdefaultRate")!=null)
	{
		defaultRate=(String)session.getAttribute("serachftfdefaultRate");
	}
	  if(defaultRate!=null && defaultRate.equals("E"))
	{
		Port="English";
		
	}
	else if(defaultRate!=null && defaultRate.equals("M"))
	{
		Port="Metric";
		
	}
	if(session.getAttribute("ffcommonListCaps")!=null)
	{
		comCaps=(String)session.getAttribute("ffcommonListCaps");
		
	}
	if(session.getAttribute("ftfRatescaption")!=null)
	{
		noncomCaps=(String)session.getAttribute("ftfRatescaption");
	}
	if(session.getAttribute("ftfcommonList")!=null)
	{
		Stdcommon=(List)session.getAttribute("ftfcommonList");
	
	}
	if(session.getAttribute("ftfcollaps")!=null)
	{
	coll=(String)session.getAttribute("ftfcollaps");	
	}
	if(session.getAttribute("ftfnoncommonList")!=null)
	{
		noncommon=(List)session.getAttribute("ftfnoncommonList");
	
	}
	if(session.getAttribute("trade")!=null)
	{
	session.removeAttribute("trade");
	}
	if(session.getAttribute("buttonValue")!=null)
	{
		buttonValue=(String)session.getAttribute("buttonValue");
	}
	if(session.getAttribute("searchftfMaster")!=null)
	{
		ftfMaster=(FTFMaster)session.getAttribute("searchftfMaster");
				trmNum=ftfMaster.getOrgTerminal();
				trmNam=ftfMaster.getOrgTerminalName();
				destAirPortNo=ftfMaster.getDestPort();
				destAirPortName=ftfMaster.getDestPortName();
				comCode=ftfMaster.getComCode();
				comDesc=ftfMaster.getComCodeName();
		session.setAttribute("searchftfMaster",ftfMaster);
	}
	if(session.getAttribute("ftfmessage")!=null)
	{
		message=(String)session.getAttribute("ftfmessage");
	}
	if(session.getAttribute("message")!=null)
	{
		message=(String)session.getAttribute("message");
	}
	if(request.getParameter("modify")!= null)
	{
	
	 modify=(String)request.getParameter("modify");
	 session.setAttribute("modifyforftfRates",modify);
	}
	else
	{
	 	modify=(String)session.getAttribute("modifyforftfRates");
	}
	if(request.getParameter("programid")!=null)
	{
		String programId=request.getParameter("programid");
	  	session.setAttribute("processinfoforftf",programId);
  	}
  	if(request.getParameter("programid")!= null && session.getAttribute("processinfoforftf")==null)
	{
		buttonValue="searchall";
  	}
	if(buttonValue.equals("searchall"))
	{
	 	searchftfList=dbUtil.getAllftfRates();
	 	
	 	session.setAttribute("ftfRatescaption","Foreign to Foreign Rates {All Records}");
	 	
	  	session.setAttribute("searchftfList",searchftfList);
	}
	if(session.getAttribute("view")!=null)
	{
		session.removeAttribute("view");
	}
	if(buttonValue.equals("show"))
	{
	session.setAttribute("ftfRatescaption","Foreign to Foreign Rates {Record}");
	}
  
	if(request.getAttribute("btl")!=null)
	{
%>
	<script language="javascript" type="text/javascript">
	 mywindow=window.open("<%=path%>/jsps/ratemanagement/FTFBottomLine.jsp?flaterate=koko","","width=400,height=200");
 	 mywindow.moveTo(200,180);		
	
 	</script>
<%
	}
%>	

 
<html> 
	<head>
		<title>JSP for SearchFTFForm form</title>
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
<%--		<script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>--%>
		<script type="text/javascript">
		var FTFAccessorialslide;
		var commonChargesVerticalSlide;
		window.addEvent('domready', function() {
				commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
				FTFAccessorialslide=new Fx.Slide('FTF_Accessorial_vertical_slide', {mode: 'vertical'});
				
				commonChargesVerticalSlide.toggle();
				$('commonChargesToggle').addEvent('click', function(e){
					commonChargesVerticalSlide.toggle();
				});				
                FTFAccessorialslide.toggle();
				$('FTFAccessorialToggle').addEvent('click', function(e){
					FTFAccessorialslide.toggle();
				});		
			});
function popup1(mylink, windowname)
	{
		if (!window.focus)return true;
		var href;
		if (typeof(mylink) == 'string')
   		href=mylink;
		else
   		href=mylink.href;
		mywindow=window.open(href, windowname, 'width=600,height=400,scrollbars=yes');
		mywindow.moveTo(200,180);
		document.searchFTFForm.buttonValue.value="search";
		document.searchFTFForm.submit();
		return false;
	}
	
	function searchform()
	{
	
		if(document.searchFTFForm.terminalNumber.value=="" || document.searchFTFForm.destSheduleNumber.value=="")
		{
			alert("Please select Original Terminal and Destination Port");
			return;
		}
	
		document.searchFTFForm.buttonValue.value="search";
		document.searchFTFForm.search.value="get";
		document.searchFTFForm.submit();
	}
	function searchallform()
	{
	document.searchFTFForm.buttonValue.value="show";
	document.searchFTFForm.submit();
	}
	var newwindow = '';
           function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/ratemanagement/addFTFpopup.jsp?ftfrates="+"add";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/ratemanagement/addFTFpopup.jsp?ftfrates="+"add","","width=600,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}

           return false;
           } 
    function disabled(val)
   	{
	
	if(document.searchFTFForm.search!=undefined){
	//window.location.hash="bottom";
	}
	
		if(val == 0)
		{		
        	var imgs = document.getElementsByTagName('img');
   			for(var k=0; k<imgs.length; k++)
   			{
   		 		if(imgs[k].id != "showall" && imgs[k].id!="search")
   		 		{
   		 			imgs[k].style.visibility = 'hidden';
   		 		}
   			}
  		 }
  		 
   	}
	function ClearScreen()
	{
		document.searchFTFForm.buttonValue.value="clear";
		document.searchFTFForm.submit();
	}
	function searchallform()
	{	
		
		
		document.searchFTFForm.buttonValue.value="searchall";
		document.searchFTFForm.submit();
	}
	function searchStarndardform()
	{	
		document.searchFTFForm.buttonValue.value="searchStarndard";
		document.searchFTFForm.submit();
	}
	
	function openwindow()
	 {
	 	document.searchFTFForm.buttonValue.value="paramid";
		document.searchFTFForm.submit();
	 
	}
	function openwindowDoc()
	 {
	 	document.searchFTFForm.buttonValue.value="paramIdDoc";
		document.searchFTFForm.submit();
	 
	}
	
	 function titleLetter(ev)
	{
		if(event.keyCode==9){
		document.searchFTFForm.buttonValue.value="popupsearch";
	    document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.searchFTFForm.terminalName.value="";
		document.searchFTFForm.buttonValue.value="popupsearch";
		document.searchFTFForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	function getDestination(ev)
	{
		if(event.keyCode==9){
		document.searchFTFForm.buttonValue.value="popupsearch";
	    document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.searchFTFForm.destAirportname.value="";
		document.searchFTFForm.buttonValue.value="popupsearch";
		document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCode(ev)
	{
		if(event.keyCode==9){
		document.searchFTFForm.buttonValue.value="popupsearch";
	    document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=searchlclcoload&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.searchFTFForm.comDescription.value="";
		document.searchFTFForm.buttonValue.value="popupsearch";
		document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterName(ev)
	{
		if(event.keyCode==9){
		document.searchFTFForm.buttonValue.value="popupsearch";
	    document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.searchFTFForm.terminalNumber.value="";
		document.searchFTFForm.buttonValue.value="popupsearch";
		document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationName(ev)
	{
		if(event.keyCode==9){
		document.searchFTFForm.buttonValue.value="popupsearch";
	    document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodeName(ev)
	{
		if(event.keyCode==9){
		document.searchFTFForm.buttonValue.value="popupsearch";
	    document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=searchlclcoload&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.searchFTFForm.destSheduleNumber.value="";
		document.searchFTFForm.buttonValue.value="popupsearch";
		document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.searchFTFForm.comCode.value="";
		document.searchFTFForm.buttonValue.value="popupsearch";
		document.searchFTFForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getFTF()
	{
	     window.location.href="<%=path%>/jsps/ratemanagement/FTFFrame.jsp";
	}
	
	//new dojo implementation
	
	
			 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.searchFTFForm.terminalNumber.value;
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
			params['terminalName'] = document.searchFTFForm.terminalName.value;
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
			params['terminalNumber'] = document.searchFTFForm.destSheduleNumber.value;
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
			params['terminalName'] = document.searchFTFForm.destAirportname.value;
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
			params['code'] = document.searchFTFForm.comCode.value;
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
			params['codeDesc'] = document.searchFTFForm.comDescription.value;
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
		<%@include file="../includes/resources.jsp" %>
	</head>
	<%
 		 if(coll!=null && !coll.equals("")){
 %>
	 	<body class="whitebackgrnd" onLoad="disabled('<%=modify%>')">
 <%}else{ %>
	<body class="whitebackgrnd" >
<%} %>
<html:form action="/searchFTF" scope="request">
<font color="blue" size="4"><%=message%></font>
<table width="100%" border="0" cellpadding="0" cellspacing="0" >
<tr>
<td width="100%">		
		
<%	
	if(coll!=null && !coll.equals(""))
{
 %>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew" ><td>LCL Foreign To Foreign Rates</td>
		<td align="right"><input type="button" class="buttonStyleNew" value="Go Back" id="cancel" onclick="ClearScreen()">
		     <input type="button" class="buttonStyleNew" value="AddNew" id="addnew" onclick="return GB_show('Foreign To Foreign','<%=path%>/jsps/ratemanagement/addFTFpopup.jsp?ftfrates='+'add',190,610)">
		</tr>
		<tr>
         <td width="100%" colspan="2">
			<table width="100%" class="displaytagstyle"  border="0" cellpadding="0" cellspacing="2">
  			
  			
       <thead>
		<tr  class="textlabels"  >
					<td ><b>
						Org Trm No
					</td>
					<td><b>
						Org Trm Name
					</td>
					<td><b>
						Destination No			
					</td>
					<td><b>
						Destination Name
					</td>
					<td ><b>
						
					</td>
				</tr>
				</thead>
			<tr class="even">
			<td><%=trmNum%></td>
   			<td ><%=trmNam%></td>
   			<td ><%=destAirPortNo%></td>
   			<td ><%=destAirPortName%></td>
			<td class="style2"  > <%=Port%></td>
   			</tr>
   		</table>
   		</td>
   		</tr>
   		</table>	
<%
}else{ %>
	
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew" ><td>Search LCL Foreign To Foreign Rates</td>
		</tr>
		<tr>
         <td width="100%">
			<table width="100%" class="displaytagstyle"  border="0" cellpadding="0" cellspacing="4">
  			
  			
              <tr class="textlabels">
				<td>Org Trm</td>
    			<td ><input name="terminalNumber" id="terminalNumber" size="30" onkeydown="getTerminalName(this.value)"   value="<%=trmNum %>" />    
    			<dojo:autoComplete formId="searchFTFForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=SEARCH_FTF&from=0"/>                </td>
    			<td>Org Trm Name</td>
    			<td >
                 <input name="terminalName" id="terminalName"  onkeydown="getTerminalNumber(this.value)"  value="<%=trmNam %>" size="30"/>    
    			   <dojo:autoComplete formId="searchFTFForm" textboxId="terminalName"  action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_FTF&from=0"/>
    			</td>
             </tr>
    		<tr class="textlabels">	
    			<td class="textlabels">Dest Port</td>
    			<td > <input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getdestPort(this.value)" value="<%=destAirPortNo %>" size="30" maxlength="5"/>    
    			<dojo:autoComplete formId="searchFTFForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=SEARCH_FTF&from=1"/>
    			</td>
    			<td><span class="textlabels">Dest Port Name</span></td>
    			<td>
                <input name="destAirportname" id="destAirportname" onkeydown="getDestPortName(this.value)"  value="<%=destAirPortName %>" size="30"/>    
    		    <dojo:autoComplete formId="searchFTFForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_FTF&from=1"/>
    			</td>
    			<td></td>
    			<td><table><tr>
    			<td></td>
				<td  align="left" class="headerbluelarge"></td>
    			</tr></table></td>
    		</tr>
    		<tr class="textlabels">	
    			<td>Com Code</td>
    			<td >
    			<input name="comCode" id="comCode"  onkeydown="getCodeDesc(this.value)"  value="<%=comCode %>" maxlength="6" size="30"/>    
    		    <dojo:autoComplete formId="searchFTFForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=SEARCH_FTF"/>
    			</td>
    			<td>Com Description </td>
    			<td >
    			<input name="comDescription" id="comDescription" size="30" onkeydown="getCode(this.value)"  value="<%=comDesc %>" />    
    		    <dojo:autoComplete formId="searchFTFForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=SEARCH_FTF"/>
    			</td>
    			<td><table><tr>
    			</tr>
    			</table></td>
    		</tr>	
    			<tr>
    		   <td valign="top" colspan="6" align="center" style="padding-top:10px;">
    		      <input type="button" value="Search" onclick="searchform()"  class="buttonStyleNew" />
    		      <input type="button" value="Clear" onclick="ClearScreen()"  class="buttonStyleNew" />
    		   </td>
    		</tr>
<%} %>	
		</table>
  		</td>
  		</tr>
  		</table>
  		<br/>
  		
  		<%
 		 if(coll!=null && !coll.equals("")){
 %>
  		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew" height="100%"><td><%=noncomCaps%></td>
		<td align="right"><a id="commonChargesToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a> </td>
		</tr> 
		
          		<td align="left" colspan="2">
        		<div id="common_Charges_vertical_slide">
        		<div id="divtablesty1" class="scrolldisplaytable" >
          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;
          		%>
          		<%if(noncommon!=null)
          		{ 
          		%>
          	 	<display:table  name="<%=noncommon%>" defaultsort="2" pagesize="<%=pageSize%>"  class="displaytagstyle" id="ftfratestable" sort="list" > 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> LCL Foreign to Foreign Rates Displayed,For more Data click on Page Numbers.
    				<br>
    			</span>
  			  	</display:setProperty>
  				<display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner">
    				
						One {0} displayed. Page Number
					</span>
  			 	</display:setProperty>
  			    <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
    				
						{0} {1} Displayed, Page Number
				</span>
  			    </display:setProperty>
    			<display:setProperty name="basic.msg.empty_list">
    			    <span class="pagebanner">
						No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="Foreign to Foreign Rate"/>
				<display:setProperty name="paging.banner.items_name" value="Foreign to Foreign Rates"/>
  				<display:footer>
			  	<tr>
			  		
			  		<td colspan="8"><font color="red" size=2>* </font> Commodity Specific Accesorial Charges</td>
			  	<tr>
			  </display:footer>
  				<%
  					String ofMinAmt = "",ratekgs="",rateCBM="",ratelb="",rateCFT="",englishMinAmt="",sizeAOF=""
  					,sizeATT="",sizeBOF="",sizeBTT="",sizeCOF="",sizeCTT="",sizeDOF="",sizeDTT="",measureType="";
  					String comCodeDisplay="",comDescDisplay="",exist="",effdate="",nonuname="",changed="",
  					amount="",link = "",lBsAmount="",cFtAmount="",kGAmount="",cBAmount="";
  					Map map = new HashMap();
  					if(noncommon!=null && noncommon.size()>0)
  					{
  						FTFMaster ftfRates=(FTFMaster)noncommon.get(i); 			
  						
  						if(ftfRates.getComCode()!=null){
  							comCodeDisplay=ftfRates.getComCode();
  							
  							comDescDisplay=ftfRates.getComCode();
  						}
  						Set set=new HashSet();
  						if(ftfRates.getFtfCommChgSet()!=null){
  						 set=ftfRates.getFtfCommChgSet();
  						if(set!=null && set.size()>0){
  						exist="*";
  						
  						}
  						}
  						if(session.getAttribute("FtfUpdateRecords")!=null)
  						{
  						  
  						FTFMaster ll=(FTFMaster)session.getAttribute("FtfUpdateRecords");
						if(ll.getComCode()!=null && ftfRates.getComCode()!=null 
  							&& ll.getComCode().equals(ftfRates.getComCode()))
  							{
  							
  							changed="c";
  							}
  						
  						}
  						
  						if(ftfRates.getFtfDetailsSet()!=null)
  						{
  						Iterator iter=ftfRates.getFtfDetailsSet().iterator();
  						while(iter.hasNext())
  						{
  						FTFDetails retailRate=(FTFDetails)iter.next();
  						if(retailRate.getEffectiveDate()!=null){
  							effdate=dateFormat.format(retailRate.getEffectiveDate());
  							}
  							if(retailRate. getWhoChanged()!=null){
  							nonuname=retailRate. getWhoChanged();
  							}
  						if(retailRate.getMetric1000kg()!=null)
  							{
  							ratekgs=df.format(retailRate.getMetric1000kg());
  							}
  							else{
  							ratekgs=("0.00");
  							}
  							if(retailRate.getMetricCbm()!=null)
  							{
  							rateCBM=df.format(retailRate.getMetricCbm());
  							}
  							else{
  							rateCBM=("0.00");
  							}
  							if(retailRate.getMetricOfMinamt()!=null)
  							{
  							ofMinAmt=df.format(retailRate.getMetricOfMinamt());
  							}
  							else{
  							ofMinAmt=("0.00");
  							}
  							if(retailRate.getEnglish100lb()!=null)
  							{
  							ratelb=df.format(retailRate.getEnglish100lb());
  							}
  							else{
  							ratelb=("0.00");
  							}
  							if(retailRate.getEnglishCft()!=null)
  							{
  							rateCFT=df.format(retailRate.getEnglishCft());
  							}
  							else{
  							rateCFT=("0.00");
  							}
  							if(retailRate.getEnglishOfMinamt()!=null)
  							{
  							englishMinAmt=df.format(retailRate.getEnglishOfMinamt());
  							}
  							else{
  							englishMinAmt=("0.00");
  							}
  							if(retailRate.getSizeAOf()!=null)
  							{
  							sizeAOF=df.format(retailRate.getSizeAOf());
  							}
  							else{
  							sizeAOF=("0.00");
  							}
  							if(retailRate.getSizeATt()!=null)
  							{
  							sizeATT=df.format(retailRate.getSizeATt());
  							}
  							else{
  							sizeATT=("0.00");
  							}
  							if(retailRate.getSizeBOf()!=null)
  							{
  							sizeBOF=df.format(retailRate.getSizeBOf());
  							}
  							else{
  							sizeBOF=("0.00");
  							}
  							if(retailRate.getSizeBTt()!=null)
  							{
  							sizeBTT=df.format(retailRate.getSizeBTt());
  							}
  							else{
  							sizeBTT=("0.00");
  							}
  							if(retailRate.getSizeCOf()!=null)
  							{
  							sizeCOF=df.format(retailRate.getSizeCOf());
  							}
  							else{
  							sizeCOF=("0.00");
  							}
  							if(retailRate.getSizeCTt()!=null)
  							{
  							sizeCTT=df.format(retailRate.getSizeCTt());
  							}
  							else{
  							sizeCTT=("0.00");
  							}
  							if(retailRate.getSizeDOf()!=null)
  							{
  							sizeDOF=df.format(retailRate.getSizeDOf());
  							}
  							else{
  							sizeDOF=("0.00");
  							}
  							if(retailRate.getSizeDTt()!=null)
  							{
  							sizeDTT=df.format(retailRate.getSizeDTt());
  							}
  							else{
  							sizeDTT=("0.00");
  							}
  							if(retailRate.getMeasureType()!=null)
  							{
  							measureType=retailRate.getMeasureType();
  							}
  							if(Port!=null && Port.equals("Metric"))
	  							{
	  							map = getFtfBottmLineRate(ftfRates.getId());
									if(map!=null){
									kGAmount=map.get("kgs").toString();
									cBAmount=map.get("cbm").toString();
									}
		  						amount=ratekgs+"/"+rateCBM;
		  						}
		  						else if(Port!=null && Port.equals("English"))
		  						{
		  						map = getFtfBottmLineRate(ftfRates
									.getId());
										if(map!=null){
									lBsAmount=(String)map.get("lbs");
									cFtAmount=(String)map.get("cft");
		  						}
		  						amount=ratelb+"/"+rateCFT;
		  						}
  						}
  						}
  						link = editPath+"?param=" + ftfRates.getId(); 
  					}
  					
  		 		%>
  		 		
  		 		
  		 		<display:column title="" style="color:red" ><%=exist%></display:column> 
				<display:column title="Comm#" sortable="true" href="<%=editPath%>"  paramId="paramid" paramProperty="id"><%=comCodeDisplay%></display:column>
				<display:column sortable="true" title="ComDesc" ><%=comDescDisplay%></display:column>
				
				
				<%if(Port!=null && Port.equals("Metric"))
				{ %>
  		 		<display:column sortable="true" title="Rate/1000kGS/CBM"><%=amount%></display:column>
  		 		<%}
				if(Port!=null && Port.equals("English"))
				{ %>
				<display:column sortable="true" title="Rate/100LB/CFT" ><%=amount%></display:column>
  		 		<%} %>
			 	<display:column sortable="true" title="MinAmt" ><%=ofMinAmt%></display:column>
			 	<display:column sortable="true" title="EffDate" ><%=effdate%></display:column>
				<display:column sortable="true" title="UserName"><%=nonuname%></display:column>
  		 		<%
		if (Port != null && Port.equals("English")) {
%>
		<display:column title="BottomLineRate">
	<span onmouseover="tooltip.show('<strong>Rates/lbs</strong><%=lBsAmount  %><br/><strong>Rates/cft</strong><%=cFtAmount%> ',null,event);" 
	onmouseout="tooltip.hide();"><%="BL Rates"%></span> 
	</display:column>
<%
	} else if (Port != null && Port.equals("Metric")) {
%>
		<display:column title="BottomLineRate">
	<span onmouseover="tooltip.show('<strong>Rates/1000KG</strong> <%=kGAmount %><br/><strong>Rates/Cbm</strong><%=cBAmount %> ',null,event);" 
	onmouseout="tooltip.hide();"><%="BL Rates"%></span> 
	</display:column>
<%
	}
%>
			 	<display:column title="Actions">
			 	   <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);"  onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" onclick="window.location.href ='<%=link %>'"/> </span>
			 	</display:column>
			 	<%i++; %>
		 </display:table>
		 <%} %>
		  </table>
   </div>
   </div>
   </td>
   </tr>
   </table>
<br/>
<table width="100%">
<tr class="tabHead"><td align="center">Common Accessorial Charges</td></tr>
</table>
<br>

			
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew"><td><%=comCaps%></td>
			 <td align="right">
		  <%
		 	 int j=0;
         	String all=comCaps;
		String getAll=null;
		if(all!=null){
		int ty=all.indexOf("All");
		if(ty > -1)
		{
		getAll="all";
		}
		
		}
				
		if(session.getAttribute("ffcommonListCaps")!=null)
		 { 
			
			String getresult=(String)session.getAttribute("ffcommonListCaps");

			if(getresult!=null && getresult.equals("No Accessorial Add"))
			{ %>			
			     <input type="button" value="Add Accessorial" onclick="openwindow()"  class="buttonStyleNew" style="width:100px"/>
<%		    }
		else
		{
			 if(getAll!=null)
			 {
			   %>
			  
			       <input type="button" value="ShowStandard-Only" id="searchall" onclick="searchStarndardform()"  class="buttonStyleNew" style="width:100px" />  
			  
<%
				}
				else{
				%>
			   
			      <input type="button" value="ShowAll" id="searchall" onclick="searchallform()"  class="buttonStyleNew" />
			      
              
<%
				}
		     
%>
	 		
	   			 <input type="button" value="Add Accessorial"  onclick="openwindow()"  class="buttonStyleNew" style="width:100px"/>
	   			 <input type="button" value="Doc/Com/Mis" id="searchall" onclick="openwindowDoc()"  class="buttonStyleNew" style="width:100px"/>
        	
<% 
         		
		   }%>
		 
				
<%
		  }%>
		  </td>
		  <td align="right"><a id="FTFAccessorialToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a> </td>
			 </tr> 
 		
			<tr>
				
        		<td align="left" colspan="3">
        		<div id="FTF_Accessorial_vertical_slide">
        		<div id="divtablesty1" class="scrolldisplaytable">
          		
          		<%
          			if(Stdcommon!=null && Stdcommon.size()>0)
		 { 
		 
		 %>
		 <display:table name="<%=Stdcommon%>" pagesize="<%=pageSize%>" defaultsort="1" class="displaytagstyle" id="nonftfratestable" sort="list" > 
			 	
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Foreign to Foreign Rates Displayed,For more Data click on Page Numbers.
    				<br>
    			</span>
  			  	</display:setProperty>
  			  	
  				<display:setProperty name="paging.banner.one_item_found">
    				<span class="pagebanner" >
						One {0} displayed. Page Number
					</span>
  			 	</display:setProperty>
  			    <display:setProperty name="paging.banner.all_items_found">
    				<span class="pagebanner">
						{0} {1} Displayed, Page Number
				</span>
  			    </display:setProperty>
    			<display:setProperty name="basic.msg.empty_list">
    			    <span class="pagebanner">
						No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement"  value="bottom"  />
				<display:setProperty name="paging.banner.item_name" value="Foreign to Foreign Rate"/>
  				<display:setProperty name="paging.banner.items_name" value="Foreign to Foreign Rates"/>
  				
  				<%
  					String comCodeDisplay = "";
  					String comDescDisplay = "";
  					
  					String chargeCode = "";
  					String chargeType="";
  					String standard="";
  					String amtPerCft="";
  					String amtlbs="";
  					String amtPerCbm="";
  					String amtPerKg="";
  					String amount="";
  					String percentage="";
  					String minAmt="";
  					String insuranceRate="";
  					String insuranceAmt="";
  					String asFrfgted="";
  					String userName="";
  					String effdate="";
  					String  temPath="";
  					String moreinfo="";
  					String time="";
  					String chnaged="",deleteStandard="";
  					//.....to retrive only standard records for commmon commodity.....
  					if(Stdcommon!=null && Stdcommon.size()>0)
  					{
  						FTFStandardCharges retailRate=(FTFStandardCharges)Stdcommon.get(j); //child record
  						
  						FTFMaster parentObject=new FTFMaster();//parent record
  						FTFMasterDAO ftfMasterDAO=new FTFMasterDAO(); 
  						parentObject=ftfMasterDAO.findById(retailRate.getFtfId());
  						if(parentObject.getComCode()!=null)
  						{
  							comDescDisplay=parentObject.getComCode();
  						}
  						   if(retailRate.getChargeCode()!=null)
  							{
  							chargeCode=retailRate.getChargeCode().getCode();
  							comCodeDisplay=retailRate.getChargeCode().getCodedesc();
  							}
  							 if(session.getAttribute("displayftfagss")!=null)
  						 {
								FTFStandardCharges rr=(FTFStandardCharges)session.getAttribute("displayftfagss");
								if(rr.getChargeCode()!=null && retailRate.getChargeCode()!=null && 
								rr.getChargeCode().getId().equals(retailRate.getChargeCode().getId()))
								{
									chnaged="a";
									}
							}
  							if(retailRate.getChargeType()!=null)
  							{
  							chargeType=retailRate.getChargeType().getCodedesc();
  							}
  							if(retailRate.getStandard()!=null)
  							{
  							standard=retailRate.getStandard();
  							}
  							if(retailRate.getAmtPerCft()!=null)
  							{
  							amtPerCft=df.format(retailRate.getAmtPerCft());
  							}
  							if(retailRate.getAmtPer100lbs()!=null)
  							{
  							amtlbs=df.format(retailRate.getAmtPer100lbs());
  							}
  							if(retailRate.getAmtPerCbm()!=null)
  							{
  							amtPerCbm=df.format(retailRate.getAmtPerCbm());
  							}
  							if(retailRate.getAmtPer1000Kg()!=null)
  							{
  							amtPerKg=df.format(retailRate.getAmtPer1000Kg());
  							}
  							
  							
  							if(retailRate.getMinAmt()!=null)
  							{
  							minAmt=df.format(retailRate.getMinAmt());
  							}
  							if(retailRate.getInsuranceRate()!=null)
  							{
  							insuranceRate=df.format(retailRate.getInsuranceRate());
  							}
  							if(retailRate.getInsuranceAmt()!=null)
  							{
  							insuranceAmt=df.format(retailRate.getInsuranceAmt());
  							}
  							if(retailRate.getAsFrfgted()!=null)
  							{
  							asFrfgted=retailRate.getAsFrfgted();
  							}
  						  if(retailRate.getChargeType()!=null)
  							{
  								chargeType=retailRate.getChargeType().getCodedesc();
  															
  							}
	  						if(chargeType!=null && chargeType.equals("Flat rate"))
  						{
  						if(retailRate.getAmount()!=null)
  							{
  							amount=df.format(retailRate.getAmount());
  							}
  						
  						}
  						if(chargeType!=null && chargeType.equals("Charge percent"))
  						{
  						if(retailRate.getPercentage()!=null)
  							{
  							amount=per.format(retailRate.getPercentage());
  							}
  						}
  						if(chargeType!=null && chargeType.equals("As Freighted"))
  						{
  						amount=insuranceAmt+"/"+insuranceRate;
  						}
  						if(chargeType!=null && chargeType.equals("Weight or measure"))
  						{
	  						if(Port!=null && Port.equals("Metric"))
	  						{
	  						amount=amtPerCbm+"/"+amtPerKg;
	  						}
	  						else if(Port!=null && Port.equals("English"))
	  						{
	  						amount=amtPerCft+"/"+amtlbs;
	  						}
  						}	  	
	  						if(retailRate.getEffectiveDate()!=null)
  							{
  									effdate=dateFormat.format(retailRate.getEffectiveDate());
  									time=dateFormat1.format(retailRate.getEffectiveDate());
  							}
  							if(retailRate. getWhoChanged()!=null)
  							{
  								userName=retailRate. getWhoChanged();
  							}
  							
  					 String iStr=String.valueOf(j);
  				      temPath=editPath+"?ind="+iStr+"&paramid="+parentObject.getId();	
  				      moreinfo=editPath+"?ind="+iStr+"&param="+parentObject.getId();	
  				      deleteStandard = editPath + "?parentId=" + parentObject.getId() + "&standardId="
														+ retailRate.getId();	
  					}	
  					
  		 		%>
  		 		
  		 		<display:column  style="width:2%;visibility:hidden"><%=chargeCode%></display:column>
  		 		<display:column sortable="true" title="Chrg Code" ><a href="<%=temPath%>"><%=chargeCode%></a></display:column>
				<display:column sortable="true" title="Chrg Code" ><%=comCodeDisplay%></display:column>
				<display:column sortable="true" title="Chrg Type"><%=chargeType%></display:column>
  		 		<display:column sortable="true" title="Std"><%=standard%></display:column>
			 	<display:column sortable="true" title="Amt"><%=amount%></display:column>
  		 		<display:column sortable="true" title="MinAmt" >&nbsp;<%=minAmt%></display:column>
  		 		<display:column sortable="true" title="EffDate"><%=effdate%></display:column>
  		 		<display:column sortable="true" title="EffDate"><%=time%></display:column>
  		 		<display:column sortable="true" title="User Name" >&nbsp;<%=userName%></display:column>
  		 		
                <display:column title="Actions">
			 	   <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);"  onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" onclick="window.location.href ='<%=moreinfo %>'"/> </span>
				   <span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);"  onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/delete.gif" onclick="window.location.href ='<%=deleteStandard %>'"/> </span>
			 	</display:column>
			 	<%j++; %>
		 </display:table>
		 <%} %>
   	
</table>
</div>
</div>
</td>
</tr>
</table>
<%} %>	

<a name="bottom"></a> 
<html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
<html:hidden property="search" />
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
