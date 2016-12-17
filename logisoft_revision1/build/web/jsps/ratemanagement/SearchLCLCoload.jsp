<%@ page language="java"  import="java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.hibernate.dao.LCLColoadMasterDAO,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.LCLColoadMaster,com.gp.cong.logisoft.domain.LCLColoadDetails,com.gp.cong.logisoft.domain.LCLColoadStandardCharges,com.gp.cong.logisoft.domain.LCLColoadCommodityCharges"%>
<jsp:directive.page import="com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO"/>
<jsp:directive.page import="com.gp.cong.logisoft.domain.GenericCode"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../ratemanagement/CalculateBottomLineRates.jsp"%>
<jsp:useBean id="airRatesBean" class="com.gp.cong.logisoft.beans.AirRatesBean" scope="request"/>   
<%
	String path = request.getContextPath();
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat dateFormat1 = new SimpleDateFormat("hh:mm aaa");
 	DecimalFormat df = new DecimalFormat("0.00");
 	DecimalFormat per = new DecimalFormat("0.000");
	LCLColoadMaster lclColoadMaster=new LCLColoadMaster();
	List searchLCLColoadList=new ArrayList();
	airRatesBean.setMatch("match");
	List Stdcommon=new ArrayList();
	String trmNum="";
	String trmNam="";
	String destAirPortNo="";
	String destAirPortName="";
	String comCode="";
	String comDesc="";
	String editPath=path+"/searchLCLCoload.do";
	String message="";
	String modify="";
	String buttonValue="";
	String coll="";
	String Port="";
	String defaultRate="";
	String noncomCaps="Ocean Freight Rates";
	String comCaps="Common Accessorial Charges";
	
	if(session.getAttribute("serachlclcdefaultRate")!=null)
	{
		defaultRate=(String)session.getAttribute("serachlclcdefaultRate");
	}
	  if(defaultRate!=null && defaultRate.equals("E"))
	{
		Port="English";
		airRatesBean.setMatch("starts");
	}
	else if(defaultRate!=null && defaultRate.equals("M"))
	{
		Port="Metric";
		airRatesBean.setMatch("starts");
	}
	if(session.getAttribute("lclcommonListCaps")!=null)
	{
		comCaps=(String)session.getAttribute("lclcommonListCaps");
	}
	if(session.getAttribute("lclcoloadRatescaption")!=null)
	{
		noncomCaps=(String)session.getAttribute("lclcoloadRatescaption");
	}
	if(session.getAttribute("llcommonList")!=null)
	{
		Stdcommon=(List)session.getAttribute("llcommonList");
	}
	if(session.getAttribute("llnoncommonList")!=null)
	{
		searchLCLColoadList=(List)session.getAttribute("llnoncommonList");
	}
	if(session.getAttribute("LclbuttonValue")!=null)
	{
		buttonValue=(String)session.getAttribute("LclbuttonValue");
	}
	if(session.getAttribute("lclcollaps")!=null)
	{
		 coll=(String)session.getAttribute("lclcollaps");	
	}
	if(session.getAttribute("searchlclColoadMaster")!=null)
	{
		lclColoadMaster=(LCLColoadMaster)session.getAttribute("searchlclColoadMaster");
		trmNum=lclColoadMaster.getOriginTerminal();
		trmNam=lclColoadMaster.getOriginTerminalName();
		destAirPortNo=lclColoadMaster.getDestinationPort();
		destAirPortName=lclColoadMaster.getDestinationPortName();
		comCode=lclColoadMaster.getCommodityCode();
		comDesc=lclColoadMaster.getCommodityCodeName();
		
	}
	if(session.getAttribute("lclmessage")!=null)
	{
		message=(String)session.getAttribute("lclmessage");
	}
	if(session.getAttribute("message")!=null)
	{
		message=(String)session.getAttribute("message");
	}
	if(request.getParameter("modify")!= null)
	{
		 modify=(String)request.getParameter("modify");
		 session.setAttribute("modifyforlclcoloadRates",modify);
	}else
	{
 		modify=(String)session.getAttribute("modifyforlclcoloadRates");
	}
	if(request.getParameter("programid")!=null)
	{
		String programId=request.getParameter("programid");
		session.setAttribute("processinfoforcoLoad",programId);
	}
	if(session.getAttribute("view")!=null)
	{
		session.removeAttribute("view");
	}
	if(request.getAttribute("btl")!=null)
	{
%>
	<script language="javascript" type="text/javascript">
	 mywindow=window.open("<%=path%>/jsps/ratemanagement/LclcoloadBottomLine.jsp?flaterate=koko","","width=400,height=200");
 	 mywindow.moveTo(200,180);		
	 </script>
<%
	}
%>			
  	
  	
<html> 
	<head>
		<title>JSP for SearchLCLColoadForm form</title>
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
		var oceanFreightColoadVerticalSlide;
		var commonChargesVerticalSlide;
		window.addEvent('domready', function() {
				oceanFreightColoadVerticalSlide = new Fx.Slide('oceanFrieght_Coload_vertical_slide', {mode: 'vertical'});
				commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
				
				oceanFreightColoadVerticalSlide.toggle();
				commonChargesVerticalSlide.toggle();
				
				$('oceanFrieghtColoadToggle').addEvent('click', function(e){
					oceanFreightColoadVerticalSlide.toggle();
				});
				$('commonChargesToggle').addEvent('click', function(e){
					commonChargesVerticalSlide.toggle();
				});				

			});
		
	function searchform(){
		if(document.searchLCLColoadForm.terminalNumber.value=="" || document.searchLCLColoadForm.destSheduleNumber.value=="")
		{
			alert("Please select OriginTerminal or Dest Airport");
			return;
		}
		document.searchLCLColoadForm.search.value="get";
		document.searchLCLColoadForm.buttonValue.value="search";
		document.searchLCLColoadForm.submit();
 	}
   function searchallform(){
	document.searchLCLColoadForm.buttonValue.value="showall";
	document.searchLCLColoadForm.submit();
	}
   var newwindow = '';
   function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/ratemanagement/AddLclColoadPopup.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/ratemanagement/AddLclColoadPopup.jsp?fclcorates="+"add","","width=600,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
   } 
  function disabled(val){
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
  		 var tables1 = document.getElementById('nonlclcoloadratestable');
  		 if(tables1!=null)
  		 {
  	 	 //displaytagcolor();
  	 	 displaytagcolorcom()
   		 initRowHighlightingcom();
   		 setWarehouseStyle();
   		 }
  		 var tables = document.getElementById('lclcoloadratestable');
  		 if(tables!=null)
  		 {
  	 	 //displaytagcolor();
   		 //initRowHighlighting();
   		 setWarehouseStyle();
   		 }
   	}
   function titleLetter(ev){
		if(event.keyCode==9){
		document.searchLCLColoadForm.buttonValue.value="popupsearch";
	    document.searchLCLColoadForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	   }
   }
   function titleLetterPress(ev){
		if(event.keyCode==13)
		{
		  document.searchLCLColoadForm.terminalName.value="";
		  document.searchLCLColoadForm.buttonValue.value="popupsearch";
		  document.searchLCLColoadForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
   function getDestination(ev){
		if(event.keyCode==9){
		document.searchLCLColoadForm.buttonValue.value="popupsearch";
	    document.searchLCLColoadForm.submit();
	    //window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	   }
	}
   function getDestinationPress(ev){
		if(event.keyCode==13){
		  document.searchLCLColoadForm.destAirportname.value="";
		  document.searchLCLColoadForm.buttonValue.value="popupsearch";
		  document.searchLCLColoadForm.submit();
	  //window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	   }
   }
  function getComCode(ev){
		if(event.keyCode==9){
		  document.searchLCLColoadForm.buttonValue.value="popupsearch";
	      document.searchLCLColoadForm.submit();
	    //window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=searchlclcoload&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	  }
   }
  function getComCodePress(ev){
		if(event.keyCode==13){
		  document.searchLCLColoadForm.comDescription.value="";
		  document.searchLCLColoadForm.buttonValue.value="popupsearch";
		  document.searchLCLColoadForm.submit();
	    //window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 }
   }
  function titleLetterName(ev){
		if(event.keyCode==9){
		document.searchLCLColoadForm.buttonValue.value="popupsearch";
	    document.searchLCLColoadForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	  }
   }
  function titleLetterNamePress(ev){
		if(event.keyCode==13){
		 document.searchLCLColoadForm.terminalNumber.value="";
		 document.searchLCLColoadForm.buttonValue.value="popupsearch";
		 document.searchLCLColoadForm.submit();
	    //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	   }
   }
  function getDestinationName(ev){
		if(event.keyCode==9){
		  document.searchLCLColoadForm.buttonValue.value="popupsearch";
	      document.searchLCLColoadForm.submit();
	      //window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
  function getComCodeName(ev){
		if(event.keyCode==9){
		document.searchLCLColoadForm.buttonValue.value="popupsearch";
	    document.searchLCLColoadForm.submit();
	    //window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=searchlclcoload&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	   }
   }
  function getDestinationNamePress(ev){
		if(event.keyCode==13){
		document.searchLCLColoadForm.destSheduleNumber.value="";
		document.searchLCLColoadForm.buttonValue.value="popupsearch";
		document.searchLCLColoadForm.submit();
	   //window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	   }
   }
  function getComCodeNamePress(ev){
		if(event.keyCode==13){
		 document.searchLCLColoadForm.comCode.value="";
		 document.searchLCLColoadForm.buttonValue.value="popupsearch";
		 document.searchLCLColoadForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
  }	
  function ClearScreen() {
	    document.searchLCLColoadForm.buttonValue.value="clear";
	    document.searchLCLColoadForm.submit();
   }
  function searchallform(){	
		alert();
		document.searchLCLColoadForm.buttonValue.value="searchall";
	    document.searchLCLColoadForm.submit();
   }
  function searchStarndardform(){	
		document.searchLCLColoadForm.buttonValue.value="searchStarndard";
	    document.searchLCLColoadForm.submit();
   }
  function openwindow() {
	 	document.searchLCLColoadForm.buttonValue.value="paramid";
	    document.searchLCLColoadForm.submit();
  }
  function openwindowDoc(){
	 	document.searchLCLColoadForm.buttonValue.value="paramidDoc";
	    document.searchLCLColoadForm.submit();
  }
  function getLCLCoload(){
	   window.location.href="<%=path%>/jsps/ratemanagement/LCLColoadFrame.jsp";
  }
  	
  	
  	//type2 dojo implementation
  	
  	
  		 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.searchLCLColoadForm.terminalNumber.value;
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
			params['terminalName'] = document.searchLCLColoadForm.terminalName.value;
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
			params['terminalNumber'] = document.searchLCLColoadForm.destSheduleNumber.value;
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
			params['terminalName'] = document.searchLCLColoadForm.destAirportname.value;
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
			params['code'] = document.searchLCLColoadForm.comCode.value;
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
			params['codeDesc'] = document.searchLCLColoadForm.comDescription.value;
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
    <%}%>
    
	<html:form action="/searchLCLCoload" scope="request">
	<font color="blue" size="4"><%=message%></font>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" style="padding-left:10px;padding-top:10px;">
	
	<%if(coll!=null && !coll.equals("")){%>
	<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
  		    <tr class="tableHeadingNew"><td>LCL CoLoad Rates</td>
  				 <td align="right">
   		           <input type="button" value="Go Back" id="cancel" onclick="ClearScreen()" class="buttonStyleNew"/> 
   		           <input type="button" value="Add New" id="addnew" onclick="GB_show('LCL Co Load','<%=path%>/jsps/ratemanagement/AddLclColoadPopup.jsp?fclcorates='+'add',200,600)" class="buttonStyleNew"/> 
   			     </td>
  		    </tr>
  		    <td colspan="2">
  		    <table width="100%" border="0" cellpadding="0" cellspacing="2" class="displaytagstyle">
			<thead>
			<tr class="textlabels">
				 <td><b>Org Trm No</td>
				 <td><b>Org Trm Name</td>
				 <td><b>Destination No</td>
				 <td><b>Destination Name</td>
		    </tr>
		    </thead>
		   <tr class="even">
				 <td><%=trmNum%></td>
   				 <td><%=trmNam%></td>
   				 <td><%=destAirPortNo%></td>
   				 <td><%=destAirPortName%></td>
   			     <td class="style2"><%=Port%></td>
   	      </tr>	
<%}else{%>

		<table width="100%"  border="0" cellpadding="0" cellspacing="3" class="tableBorderNew" >
  		<tr class="tableHeadingNew">Search LCL CoLoad Rates</tr>
  		<tr class="textlabels">
    			<td>&nbsp;&nbsp;Org Trm</td>
    			<td>
    				<input name="terminalNumber" id="terminalNumber" size="30" onkeydown="getTerminalName(this.value)"   value="<%=trmNum %>"/>    
    				<dojo:autoComplete formId="searchLCLColoadForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=SEARCH_LCL_COLOAD&from=0"/>
    			</td>
    			<td>&nbsp;</td>
    			<td>Org Trm Name</td>
    			<td>
                   <input name="terminalName" id="terminalName" size="30"  onkeydown="getTerminalNumber(this.value)" value="<%=trmNam %>" />    
    			   <dojo:autoComplete formId="searchLCLColoadForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_LCL_COLOAD&from=0"/>
    			</td>
    	</tr>
    	<tr class="textlabels" >	
    			<td>&nbsp;&nbsp;Dest Port </td>
    			<td>
                   <input name="destSheduleNumber" id="destSheduleNumber"  onkeydown="getdestPort(this.value)"  value="<%=destAirPortNo %>" size="30" maxlength="5"/>    
    			   <dojo:autoComplete formId="searchLCLColoadForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=SEARCH_LCL_COLOAD&from=1"/>
    			</td>
    			<td>&nbsp;</td>
    			<td>Dest Port Name</td>
    			<td>
                	<input name="destAirportname" id="destAirportname"  onkeydown="getDestPortName(this.value)"  value="<%=destAirPortName %>"  size="30"/>    
    		    	<dojo:autoComplete formId="searchLCLColoadForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_LCL_COLOAD&from=1"/>
    			</td>
    	</tr>
    	<tr class="textlabels">	
    			<td>&nbsp;&nbsp;Com Code</td>
    			<td>
               	    <input name="comCode" id="comCode"  onkeydown="getCodeDesc(this.value)"  value="<%=comCode %>" maxlength="6" size="30"/>    
    		    	<dojo:autoComplete formId="searchLCLColoadForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=SEARCH_LCL_COLOAD"/>
    			</td>
    			<td>&nbsp;</td>
    			<td>Com Description </td>
    			<td>
    				<input name="comDescription" id="comDescription" size="30" onkeydown="getCode(this.value)" value="<%=comDesc %>" />    
    		   	    <dojo:autoComplete formId="searchLCLColoadForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=SEARCH_LCL_COLOAD"/>
    			</td>
    	</tr>
  	    <tr>
 			  <td valign="top" colspan="6" align="center" style="padding-top:10px;">
 				  <input type="button" value="Search" onclick="searchform()" class="buttonStyleNew"/>
  				  <input type="button" value="Clear" id="cancel" onclick="ClearScreen()" class="buttonStyleNew"/>
 			  </td>
  	    </tr>
    	
   <%} %> 		

  	</table>
  </table>
  <%if(coll!=null && !coll.equals("")){%>			
  			<br/>
  			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew" ><td><%=noncomCaps %></td>
				<td align="right"><a id="oceanFrieghtColoadToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a></td>				
			</tr>
  			<tr>
        		<td align="left">
        		<div id="oceanFrieght_Coload_vertical_slide">
        		<div id="divtablesty1" class="scrolldisplaytable">
          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
   <% int i=0;
      if(searchLCLColoadList!=null && searchLCLColoadList.size()>0){
   %> 
          		
        <display:table name="<%=searchLCLColoadList%>" defaultsort="3" pagesize="<%=pageSize%>" class="displaytagstyle" id="lclcoloadratestable" sort="list"> 
	 	<display:setProperty name="paging.banner.some_items_found">
  			<span class="pagebanner">
  			
  				<font color="blue">{0}</font> LCL Coload Rates Displayed,For more Data click on Page Numbers.
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
		<display:setProperty name="paging.banner.item_name" value="LCL Coload Rates"/>
		<display:setProperty name="paging.banner.items_name" value="LCL Coload Rates"/>
  		<display:footer>
			  	<tr>
			  		
			  		<td colspan="8"><font color="red" size=2>* </font> Commodity Specific Accesorial Charges</td>
			  	<tr>
			  </display:footer>
  		<%
					String ofMinAmt = "",ratekgs="", rateCBM="", ratelb="", rateCFT="", englishMinAmt="",sizeAOF="", 
					sizeATT="", sizeBOF="", sizeBTT="", sizeCOF="",sizeCTT="",sizeDOF="",  sizeDTT="", measureType="",
					comCodeDisplay="",comDescDisplay="", exist="", effdate="",cBAmount="",   nonuname="", changed="", 
					amount="",temPath="",moreinfo="",lBsAmount="",cFtAmount="",kGAmount="";
					  Map map =new HashMap();
			
					if(searchLCLColoadList!=null && searchLCLColoadList.size()>0)
					{
						LCLColoadMaster lclcoloadRates=(LCLColoadMaster)searchLCLColoadList.get(i); 			
						if(lclcoloadRates.getCommodityCode()!=null)
						{
							GenericCodeDAO genericCodeDAO =new GenericCodeDAO();
					
							List comList = genericCodeDAO.findForGenericCode(lclcoloadRates.getCommodityCode());
							if (comList != null && comList.size() > 0) {
								GenericCode genObj = (GenericCode) comList.get(0);
								comCodeDisplay = genObj.getCode();
								comDescDisplay = genObj.getCodedesc();
							}
						}
						if(session.getAttribute("lclUpdateRecords")!=null)
						{
						  
						LCLColoadMaster ll=(LCLColoadMaster)session.getAttribute("lclUpdateRecords");
						if(ll.getCommodityCode()!=null && lclcoloadRates.getCommodityCode()!=null 
							&& ll.getCommodityCode().equals(lclcoloadRates.getCommodityCode()))
							{
							
							changed="c";
							}
						
						}
						Set set=new HashSet();
						if(lclcoloadRates.getLclColoadCommChgSet()!=null){
						 set=lclcoloadRates.getLclColoadCommChgSet();
						if(set!=null && set.size()>0){
							exist="*";
						}
						}
						
						if(lclcoloadRates.getLclColoadDetailsSet()!=null)
						{
						Iterator iter=lclcoloadRates.getLclColoadDetailsSet().iterator();
						while(iter.hasNext())
						{
						LCLColoadDetails retailRate=(LCLColoadDetails)iter.next();
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
							if(retailRate.getEffectiveDate()!=null){
							effdate=dateFormat.format(retailRate.getEffectiveDate());
							}
							if(retailRate. getWhoChanged()!=null){
							nonuname=retailRate. getWhoChanged();
							}
							if(retailRate.getMeasureType()!=null)
							{
							measureType=retailRate.getMeasureType();
							}
							if(Port!=null && Port.equals("Metric"))
 							{
 							map = getLclBottmLineRate(lclcoloadRates
							.getId());
							if(map!=null){
					
							kGAmount=map.get("kgs").toString();
							cBAmount=map.get("cbm").toString();
							}
  						amount=ratekgs+"/"+rateCBM;
  						}
  						else if(Port!=null && Port.equals("English"))
  						{
  						map = getLclBottmLineRate(lclcoloadRates
							.getId());
								if(map!=null){
							lBsAmount=(String)map.get("lbs");
							cFtAmount=(String)map.get("cft");
  						}
  						amount=ratelb+"/"+rateCFT;
						}
						}
						   String iStr=String.valueOf(i);
 					   temPath=editPath+"?ind="+iStr+"&paramid="+lclcoloadRates.getId();	
				 		   moreinfo=editPath+"?param="+lclcoloadRates.getId();
				 			
					}
  			}
		%>
  		 <display:column style="color:red"><%=exist%></display:column>
  		 <display:column  style="width:2%;visibility:hidden"><%=changed%></display:column>
		 <display:column title="Comm#"  href="<%=editPath%>"  paramId="paramid" sortable="true" paramProperty="id"><%=comCodeDisplay%></display:column>
		 <display:column sortable="true" title="Comm Desc" ><%=comDescDisplay%></display:column>
	<%if(Port.equals("English")){ %>
  		 <display:column sortable="true" title="Rate/100LB/CFT" ><%=amount%></display:column>
	<%}else if(Port.equals("Metric")){ %>
		 <display:column sortable="true" title="Rate/1000kGS/CBM"><%=amount%></display:column>
	<%}%>
		 <display:column sortable="true" title="MinAmt" ><%=ofMinAmt%></display:column>
		 <display:column sortable="true" title="Effective_Date" ><%=effdate%></display:column>
		 <display:column sortable="true" title="User Name" ><%=nonuname%></display:column>
    <%if (Port != null && Port.equals("English")) {%>
		<display:column title="BottomLineRate">
		   <span onmouseover="tooltip.show('<strong>Rates/lbs</strong><%=lBsAmount  %><br/><strong>Rates/cft</strong><%=cFtAmount%> ');" 
			  onmouseout="tooltip.hide();"><%="BL Rates"%></span> 
	    </display:column>
    <%} else if (Port != null && Port.equals("Metric")) {%>
		<display:column title="BottomLineRate">
			<span onmouseover="tooltip.show('<strong>Rates/1000KG</strong> <%=kGAmount %><br/><strong>Rates/Cbm</strong><%=cBAmount %> ');" 
				  onmouseout="tooltip.hide();"><%="BL Rates"%></span> 
	    </display:column>
    <%}%>
	
		<display:column title="Actions">
  		  <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>')" 
  		 		   onmouseout="tooltip.hide()" ><img src="<%=path%>/img/icons/pubserv.gif" border="0" 
  		 		   onclick="window.location.href ='<%=moreinfo %>'" /></span>
  		</display:column>
   <%i++; %>
		 </display:table>
   <%} %>
		 </table></div>
		 </div>
		 </td></tr>
   </table>
   <br>
<table width="100%">
<tr class="tabHead"><td align="center">Common Accessorial Charges</td></tr>
</table>
<br>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew"><td><%=comCaps %></td>
	 <td align="right"> 
               <%
	   			 int k=0;
				String all=comCaps;
				String getAll=null;
				if(all!=null){
					int ty=all.indexOf("All");
						if(ty > -1){
							getAll="all";
						}
			     }
				if(session.getAttribute("lclcommonListCaps")!=null)
				 { 
				   String getresult=(String)session.getAttribute("lclcommonListCaps");
				   if(getresult!=null && getresult.equals("No Accessorial Add"))
				   { 
			       %>
			         <input type="button" class="buttonStyleNew" style="width:90px" value="Add Accessorial" onclick="openwindow()" >
			 	   <%}else{
		 			 if(getAll!=null) { %>
			   	 	  <input type="button" class="buttonStyleNew" style="width:120px" value="Show Standard Only" onclick="searchStarndardform()" >
				    <%}else{%>
			   		  <input type="button" class="buttonStyleNew" value="Show All" onclick="searchallform()" >
				    <%}%>
					  <input type="button" class="buttonStyleNew" style="width:90px" value="Add  Accessorial" onclick="openwindow()" >
					  <input type="button" class="buttonStyleNew" style="width:90px" value="Doc/Com/Mis" onclick="openwindowDoc()" >
		  	</td>
<% 
		}
		}
%>
	
		<td align="right"><a id="commonChargesToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a> </td>	
	</tr> 
  	<tr> 
        <td align="left" colspan="2">
        <div id="common_Charges_vertical_slide">
        <div id="divtablesty1" class="scrolldisplaytable" >
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
          	
		  
       
       <% if(Stdcommon!=null && Stdcommon.size()>0){ %>
        
		 <display:table name="<%=Stdcommon%>" pagesize="<%=pageSize%>" defaultsort="1" class="displaytagstyle" id="nonlclcoloadratestable" sort="list" > 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Retail Rates Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="LCLCoload"/>
  				<display:setProperty name="paging.banner.items_name" value="LCLCoload"/>
  				<%
  					String comCodeDisplay = "",comDescDisplay = "",chargeCode = "",chargeType="",standard="",
  					amtPerCft="",amtlbs="",amtPerCbm="",amtPerKg="",amount="",percentage="",minAmt="";
  					String insuranceRate="",insuranceAmt="",asFrfgted="",temPath="",moreinfo="",effdate="",
  					username="",chnaged="",time="",deleteStandard="";
  			//.....to retrive only standard records.....
  					if(Stdcommon!=null && Stdcommon.size()>0)
  					{
  						LCLColoadStandardCharges retailRate=(LCLColoadStandardCharges)Stdcommon.get(k); //child records	
						LCLColoadMaster parentObject=new LCLColoadMaster();//parent record
  						LCLColoadMasterDAO lCLColoadMasterDAO = new LCLColoadMasterDAO();
  						parentObject=lCLColoadMasterDAO.findById(retailRate.getLclCoLoadId());
  						if(parentObject.getCommodityCode()!=null){

  							//comCodeDisplay=parentObject.getCommodityCode().getCode();
  							//comDescDisplay=parentObject.getCommodityCode().getCodedesc();
  						}
  						if(session.getAttribute("getLclAgsss")!=null){
								LCLColoadStandardCharges rr=(LCLColoadStandardCharges)session.getAttribute("getLclAgsss");
								if(rr.getChargeCode()!=null && retailRate.getChargeCode()!=null && 
								rr.getChargeCode().getId().equals(retailRate.getChargeCode().getId()))
								{
									chnaged="a";
								}
							}
  						    if(retailRate.getChargeCode()!=null)
  							{
  							 chargeCode=retailRate.getChargeCode().getCode();
  							 comDescDisplay=retailRate.getChargeCode().getCodedesc();
  							}
  							if(retailRate.getChargeType()!=null)
  							{
  							 chargeType=retailRate.getChargeType().getCodedesc();
  							}
  							if(retailRate.getEffectiveDate()!=null)
  							{
  							 effdate=dateFormat.format(retailRate.getEffectiveDate());
  							 time=dateFormat1.format(retailRate.getEffectiveDate());
  							}
  							if(retailRate. getWhoChanged()!=null){
  							 username=retailRate. getWhoChanged();
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
  						if(chargeType!=null && chargeType.equals("Weight or measure")){
  						if(Port!=null && Port.equals("Metric"))
  						{
  						amount=amtPerCbm+"/"+amtPerKg;
  						}
  						else if(Port!=null && Port.equals("English"))
  						{
  						amount=amtPerCft+"/"+amtlbs;
  						}
  							  							
  						}
  					   	String iStr=String.valueOf(k);
  				      temPath=editPath+"?ind="+iStr+"&paramid="+parentObject.getId();	
  				      moreinfo=editPath+"?ind="+iStr+"&param="+parentObject.getId();	
  				      deleteStandard=editPath + "?parentId=" + parentObject.getId() + "&standardId="
														+ retailRate.getId();
  			     }
  				%>
  		 		<display:column  style="width:2%;visibility:hidden"><%=chargeCode%></display:column>
  		 		<display:column sortable="true" title="Chrg Code" ><a href="<%=temPath%>"><%=chargeCode%></a></display:column>
				<display:column sortable="true" title="Chrg Code" ><%=comDescDisplay%></display:column>
				<display:column sortable="true" title="Chrg Type"><%=chargeType%></display:column>
  		 		<display:column sortable="true" title="Std"><%=standard%></display:column>
				<display:column sortable="true" title="Amt"><%=amount%></display:column>
			
			 	<display:column sortable="true" title="MinAmt" ><%=minAmt%></display:column>
			 	<display:column sortable="true" title="EffDate" ><%=effdate%></display:column>
			 	<display:column sortable="true" title="Time" ><%=time%></display:column>
			 	<display:column sortable="true" title="Username" ><%=username%></display:column>
			 	<display:column title="Actions">
  		 		   <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>')" 
  		 		   onmouseout="tooltip.hide()" ><img src="<%=path%>/img/icons/pubserv.gif" border="0" 
  		 		   onclick="window.location.href ='<%=moreinfo %>'" /></span>
  		 		   <span class="hotspot"
												onmouseover="tooltip.show('<strong>Delete</strong>');"
												onmouseout="tooltip.hide();"><img
													src="<%=path%>/img/icons/delete.gif" border="0"
													onclick="window.location.href ='<%=deleteStandard%>'" />
											</span>
  		 		</display:column>
			 	<%k++; %>
		 </display:table>
		 <%} %>
   	 </tr>
   	 </table>
   	 </div></div></td>
   	 </tr></table>
   	 <%} %>
   	 </table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
			<html:hidden property="search" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


