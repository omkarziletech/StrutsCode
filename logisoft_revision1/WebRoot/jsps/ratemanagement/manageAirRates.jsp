<%@ page language="java"  import="java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.util.DBUtil,
java.text.DecimalFormat,java.util.*,com.gp.cong.logisoft.hibernate.dao.StandardChargesDAO,com.gp.cong.logisoft.domain.StandardCharges,
com.gp.cong.logisoft.domain.AirWeightRangesRates,com.gp.cong.logisoft.domain.AirStandardCharges,
com.gp.cong.logisoft.domain.AirCommodityCharges"%>
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
	<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
	<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
	<%@include file="../ratemanagement/CalculateBottomLineRates.jsp"%>
	<%@include file="../includes/jspVariables.jsp" %>
	
	 <%
response.addHeader("Pragma", "No-cache");
response.addHeader("Cache-Control", "no-cache");
%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String editPath=path+"/airRates.do";
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat dateFormat1 = new SimpleDateFormat("hh:mm aaa");
	DecimalFormat df2 = new DecimalFormat("########0.00");
	DecimalFormat per = new DecimalFormat("########0.000");
	DBUtil dbUtil=new DBUtil();
	List airRatesList=new ArrayList();
	StandardCharges airRatesObj1= null;
	List aircommon=new ArrayList();
	List noncommon=new ArrayList();
	Map airStdMap = new HashMap(); 
	String buttonValue="";
	String message="";
	String search="";
	String modify="";
	String terminalNumber = "";
	String terminalName = "";
	String destSheduleNumber = "";
	String destAirportname = "";
	String comCode = "";
	String comDesc = "";
	String noncom="";
	String procced="";
	String coll="";
	String noncaps="Air Rates ";
	String comcaps="Common Accessorial Charges";
	if(session.getAttribute("airRatescaption")!=null)
	{
		noncaps=(String)session.getAttribute("airRatescaption");
	}
	if(session.getAttribute("airRatescaptionCaps")!=null)
	{
		comcaps=(String)session.getAttribute("airRatescaptionCaps");
	}
	if(session.getAttribute("AirCollaps")!=null)
	{
		coll=(String)session.getAttribute("AirCollaps");
	}
	if(session.getAttribute("trade")!=null)
	{
		session.removeAttribute("trade");
	}
	if(session.getAttribute("commonList")!=null)
	{
		aircommon=(List)session.getAttribute("commonList");
	}
	if(session.getAttribute("noncommonList")!=null)
	{
		noncommon=(List)session.getAttribute("noncommonList");
	}
	if(request.getAttribute("message")!=null)
	{
		message=(String)request.getAttribute("message");
	}
	if(session.getAttribute("manageairrates")!=null )
	{
	    airRatesObj1=(StandardCharges)session.getAttribute("manageairrates");
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
	}
	if(request.getParameter("modify")!= null)
	{
	 modify=(String)request.getParameter("modify");
	 session.setAttribute("modifyforairRates",modify);
	}
	else
	{
 		modify=(String)session.getAttribute("modifyforairRates");
	}
	if(request.getAttribute("AirbuttonValue")!=null)
	{
		buttonValue=(String)request.getAttribute("AirbuttonValue");
	} 
	if(request.getParameter("programid")!=null)
	{
	String programId=request.getParameter("programid");
  	session.setAttribute("processinfoforairRates",programId);
	}

	if(request.getAttribute("btl")!=null)
	{
%>
	<script language="javascript" type="text/javascript">
	
	 mywindow=window.open("<%=path%>/jsps/ratemanagement/bottomlineRate.jsp?flaterate=koko","","width=400,height=200");
 	 mywindow.moveTo(200,180);		
	</script>
<%
   }
%>
<html> 
	<head>
		<title>Air Rates Management</title>
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
		<script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>
		
		<script type="text/javascript">
		var airRatesVerticalSlide;
		var commonChargesVerticalSlide;
		window.addEvent('domready', function() {
				airRatesVerticalSlide = new Fx.Slide('air_Rates_vertical_slide', {mode: 'vertical'});
				commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
				airRatesVerticalSlide.toggle();
				commonChargesVerticalSlide.toggle();
				
				$('airRatesToggle').addEvent('click', function(e){
					airRatesVerticalSlide.toggle();
				});
				$('commonChargesToggle').addEvent('click', function(e){
					commonChargesVerticalSlide.toggle();
				});				

			});
			
		
   	     var newwindow = '';
        function addform() 
        {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/ratemanagement/addAirRatesPopup.jsp?airrates="+"add";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/ratemanagement/addAirRatesPopup.jsp?airrates="+"add","","width=600,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           } 
	
		function searchform()
		{
	
			if(document.airRates.terminalNumber.value=="" || document.airRates.destSheduleNumber.value=="")
			{
				alert("Please select OriginTerminal and Dest Airport");
				return;
			}
			
			document.airRates.buttonValue.value="search";
			document.airRates.search.value="get";
			document.airRates.submit();
		}
		function searchallform()
		{	
			document.airRates.buttonValue.value="searchall";
			document.airRates.submit();
		}
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
			document.airRates.buttonValue.value="search";
			document.airRates.submit();
			return false;
		}
	 function disabled(val)
	   	{
			if(val=="20"){
				window.location.href="<%=path%>/jsps/ratemanagement/airRatesFrame.jsp";
			}
	   	}
	function titleLetter(ev)
		{
			if(event.keyCode==9){
			document.airRates.buttonValue.value="popupsearch";
		    document.airRates.submit();
		
		}
		}
	function titleLetterPress(ev)
		{
			if(event.keyCode==13)
			{
			document.airRates.terminalName.value="";
			document.airRates.buttonValue.value="popupsearch";
			document.airRates.submit();
		    
		    }
		}
	function getDestination(ev)
		{
			if(event.keyCode==9){
			document.airRates.buttonValue.value="popupsearch";
		    document.airRates.submit();
		
		}
		}
	function getDestinationPress(ev)
		{
			if(event.keyCode==13)
			{
			document.airRates.destAirportname.value="";
			document.airRates.buttonValue.value="popupsearch";
			document.airRates.submit();
		
		}
		}
	function getComCode(ev)
		{
			if(event.keyCode==9){
			document.airRates.buttonValue.value="popupsearch";
		    document.airRates.submit();
		
		}
		}
	function getComCodePress(ev)
		{
			if(event.keyCode==13)
			{
			document.airRates.comDescription.value="";
			document.airRates.buttonValue.value="popupsearch";
			document.airRates.submit();
		
		}
		}
	function titleLetterName(ev)
		{
			if(event.keyCode==9){
			document.airRates.buttonValue.value="popupsearch";
		    document.airRates.submit();
		
		}
		}
	function titleLetterNamePress(ev)
		{
			if(event.keyCode==13)
			{
			document.airRates.terminalNumber.value="";
			document.airRates.buttonValue.value="popupsearch";
			document.airRates.submit();
		
		}
		}
	function getDestinationName(ev)
		{
			if(event.keyCode==9){
			document.airRates.buttonValue.value="popupsearch";
		    document.airRates.submit();
		
		}
		}
<%--	function getComCodeName(ev)--%>
<%--		{--%>
<%--			if(event.keyCode==9){--%>
<%--			document.airRates.buttonValue.value="popupsearch";--%>
<%--		    document.airRates.submit();--%>
<%--		--%>
<%--		}--%>
	function getDestinationNamePress(ev)
		{
			if(event.keyCode==13)
			{
			document.airRates.destSheduleNumber.value="";
			document.airRates.buttonValue.value="popupsearch";
			document.airRates.submit();
		
		}
		}
	 function getComCodeNamePress(ev)
		{
			if(event.keyCode==13){
			document.airRates.comCode.value="";
			document.airRates.buttonValue.value="popupsearch";
			document.airRates.submit();
		
		}
		}
	function ClearScreen()
		{
				document.airRates.buttonValue.value="clear";
				document.airRates.submit();
		}
	function searchallform()
	   {	
		document.airRates.buttonValue.value="searchall";
	    document.airRates.submit();
	   }
	function searchStarndardform()
	{	
		alert();
		
		document.airRates.buttonValue.value="searchStarndard";
	   document.airRates.submit();
	}
	function openwindowDoc()
	 {
	 	document.airRates.buttonValue.value="paramidDoc";
	    document.airRates.submit();
	 	
	}
	function openwindow()
	 {
	 	document.airRates.buttonValue.value="paramid";
	    document.airRates.submit();
	 	
	}
		function getAddAirRates(){
			window.location.href="<%=path%>/jsps/ratemanagement/airRatesFrame.jsp";
		}
<%--type 2 dojo implementation--%>

		   function getComCodeName(ev){
		   document.getElementById("terminalName").value="";
			if(event.keyCode==9 || event.keyCode==13){	
			
			       var params = new Array();
					 params['requestFor'] = "CommodityDetails";
					 params['code'] = document.airRates.terminalNumber.value;
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
					 params['codeDesc'] = document.airRates.terminalName.value;
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
					 params['code'] = document.airRates.destSheduleNumber.value;
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
					 params['codeDesc'] = document.airRates.destAirportname.value;
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
			params['code'] = document.airRates.comCode.value;
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
			params['codeDesc'] = document.airRates.comDescription.value;
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
	if(coll!=null && !coll.equals(""))
	{
%> 
		<body class="whitebackgrnd" onLoad="">
<% 
	}
	 else
	{
%>
		<body class="whitebackgrnd">
<%
	}
 %>

	<html:form action="/airRates" name="airRates" type="com.gp.cong.logisoft.struts.ratemangement.form.AirRatesForm" scope="request">
		<%
 			 if(coll!=null && !coll.equals("")){
 		%>
 	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
	  <tr class="tableHeadingNew"><td>Air Rates</td>
	        <td align="right">
   		       <input type="button" value="Go Back" class="buttonStyleNew" id="cancel" onclick="ClearScreen()"/> 
               <input type="button" value="AddNew" class="buttonStyleNew" id="addnew" onclick="return GB_show('AirRates', '<%=path%>/jsps/ratemanagement/addAirRatesPopup.jsp?airrates='+'add',200,600)"/>
            </td>
      </tr>
      <td colspan="2">
	    <font color="blue" size="4"><%=message%></font>
	    <table width="100%"  border="0" cellpadding="0" cellspacing="2" class="displaytagstyle" >
  		   <thead>
  		   <tr>
				<td>Org Trm No</td>
				<td>Org Trm Name</td>
				<td>Destination No</td>
				<td>Destination Name</td>
	      </tr>
	      </thead>
		  <tr class="even">
			    <td class="textlabels"><%=terminalNumber%>
  			    <td class="textlabels">&nbsp;<%=terminalName%></td>
  			    <td class="textlabels" >&nbsp;<%=destSheduleNumber%></td>
  			    <td class="textlabels">&nbsp;<%=destAirportname%></td>
   		  </tr>    
		<%
			}else{
        %>
     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px;padding-top:10px;">
	  <tr class="tableHeadingNew"> Search Air Rates</tr>
	  <td>  
	    <font color="blue" size="4"><%=message%></font>
	    <table width="100%"  border="0" cellpadding="0" cellspacing="2" class="displaytagstyle" >
  			<tr class="textlabels" >
    			<td>Org Trm</td>
    			<td>
				   <input name="terminalNumber" id="terminalNumber" size="30" onkeydown="getComCodeName(this.value)" value="<%=terminalNumber %>" maxlength="3" />    
    			   <dojo:autoComplete formId="airRates" textboxId="terminalNumber" action="<%=path%>/actions/getChargeCode.jsp?tabName=MANAGE_AIR_RATES&from=0"/>                
      			</td> 
    			<td>Org Trm Name</td>
    			<td>
    		       <input name="terminalName" id="terminalName" size="30"  onkeydown="getComCode(this.value)" value="<%=terminalName %>" />    
    			   <dojo:autoComplete formId="airRates" textboxId="terminalName" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=MANAGE_AIR_RATES&from=0"/>
    		   </td>
			</tr>
    		<tr class="textlabels" >	
    			<td>Dest Port</td>
    			<td>
    			   <input name="destSheduleNumber" id="destSheduleNumber"   onkeydown="getDestPortName(this.value)" value="<%=destSheduleNumber %>" size="30" maxlength="5"/>    
    			   <dojo:autoComplete formId="airRates" textboxId="destSheduleNumber" action="<%=path%>/actions/getChargeCode.jsp?tabName=MANAGE_AIR_RATES&from=1"/>
    			</td>
    			<td>Dest Port Name</td>
    			<td>
    	    	   <input name="destAirportname" id="destAirportname"  onkeydown="getDestPort(this.value)" value="<%=destAirportname %>" size="30" />    
    		       <dojo:autoComplete formId="airRates" textboxId="destAirportname" action="<%=path%>/actions/getChargeCode.jsp?tabName=MANAGE_AIR_RATES&from=2"/>
    	    	</td>
    			<td></td>
    		</tr>
    		<tr class="textlabels" >	
    			<td>Com Code</td>
    			<td>
                	 <input name="comCode" id="comCode"  onkeydown="getCodeDesc(this.value)" value="<%=comCode %>" maxlength="6" size="30"/>    
    		   		 <dojo:autoComplete formId="airRates" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=MANAGE_AIR_RATES&from=3"/>
    			</td>
    			<td>Com Description </td>
    			<td>
<%--    			 <html:text property="comDescription" value="<%=comDesc%>" onkeydown="getComCodeName(this.value)" />--%>
    			     <input name="comDescription" id="comDescription"  onkeydown="getCode(this.value)" value="<%=comDesc %>"  size="30"/>    
    		         <dojo:autoComplete formId="airRates" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=MANAGE_AIR_RATES&from=2"/>
    			</td>
    			<td></td>
    		</tr>
    		<tr>
                <td valign="top" colspan="8" align="center" style="padding-top:10px;">
                    <input type="button" value="Search" class="buttonStyleNew"  onclick="searchform()"/>
                    <input type="button" value="Clear" class="buttonStyleNew"  onclick="ClearScreen()"/>
			    </td>   
    		</tr>
    			<td><table><tr>
    	  
    <%} %>
    			</tr></table></td>
    		
  			</table>
  			</td>
  			</table>
  			
  			<br/>
  			
	<%
 		 if(coll!=null && !coll.equals("")){
    %>
    		
  			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
  			
			<tr class="tableHeadingNew">
				<td>
					<table width="100%" cellpadding="0" cellspacing="0" >
						<tr class="tableHeadingNew"><td><%=noncaps%></td>
						<td align="right"><a id="airRatesToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a> </td>
						</tr>
					</table>
				</td>		
			</tr> 
  			<tr>
        		<td align="left" >
        		<div id="air_Rates_vertical_slide">
        		<div id="divtablesty1" class="scrolldisplaytable" >
        		
          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;
				%>
          		<%
          
          if(noncommon!=null && noncommon.size()>0)
          {
			%>
          	 	<display:table name="<%=noncommon%>" pagesize="<%=pageSize%>"  class="displaytagstyle" id="airratestable" sort="list"  defaultsort="2"> 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Air Rates Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="Air Rates"/>
  				<display:setProperty name="paging.banner.items_name" value="Air Rates"/>
  				<%
  			
  					String generalMinAmt = "",expressRate = "", weightRange="", generalRate="", expressMinAmt="", deferredRate="", 
  					 deferredMinAmt="",comCodeDisplay="", comDescDisplay="", exist="", user="",	 date="",ast="",
  					  expmin="", genmin="", defmin="", getchange="",link ="",gRate="",ERate="";
  					if(noncommon!=null && noncommon.size()>0)
  					{
  						StandardCharges airRates=(StandardCharges)noncommon.get(i); 			
  						if(airRates.getComCode()!=null)
  						{
  							comCodeDisplay=airRates.getComCode().getCode();
  							comDescDisplay=airRates.getComCode().getCodedesc();
  						}
  						if(session.getAttribute("AirUpdateRecords")!=null)
  							{
  						StandardCharges manageRetailRates1=(StandardCharges)session.getAttribute("AirUpdateRecords");
  							if(manageRetailRates1.getComCode()!=null && airRates.getComCode()!=null 
  							&& manageRetailRates1.getComCode().getCode().equals(airRates.getComCode().getCode()))
  							{
  							getchange="c";
  							}
  							
  							}
  						Set set=new HashSet();
  						if(airRates.getAirCommoditySet()!=null){
  						set=airRates.getAirCommoditySet();
  						if(set!=null && set.size()>0){
  						exist="*";
  						
  						}
  						  						}
  						if(airRates.getAirCommoditySet()!=null){
  						Iterator iter=airRates.getAirCommoditySet().iterator();
  						while(iter.hasNext())
  						{
  						AirCommodityCharges acc=(AirCommodityCharges)iter.next();
  						if(acc.getEffectiveDate()!=null){
  						date=dateFormat.format(acc.getEffectiveDate());
  						}
  						if(acc.getWhoChanged()!=null){
  						user=acc.getWhoChanged();
  						}
  						}
  						}
  						if(airRates.getAirWeightRangeSet()!=null)
  						{
  						Iterator iter=airRates.getAirWeightRangeSet().iterator();
  						while(iter.hasNext())
  						{
  						AirWeightRangesRates airWeight=(AirWeightRangesRates)iter.next();
  						if(airWeight.getWeightRange()!=null)
  							{
  							weightRange=airWeight.getWeightRange().getCodedesc();
  							}
  							if(airWeight.getGeneralRate()!=null)
  							{
  							generalRate=df2.format(airWeight.getGeneralRate()).toString();
  							}
  							if(airWeight.getGeneralMinAmt()!=null)
  							{
  							generalMinAmt=df2.format(airWeight.getGeneralMinAmt()).toString();
  							}
  							if(airWeight.getExpressRate()!=null)
  							{
  							expressRate=df2.format(airWeight.getExpressRate()).toString();
  							}
  							if(airWeight.getExpressMinAmt()!=null)
  							{
  							expressMinAmt=df2.format(airWeight.getExpressMinAmt()).toString();
  							}
  							if(airWeight.getDeferredRate()!=null)
  							{
  							deferredRate=df2.format(airWeight.getDeferredRate()).toString();
  							}
  							if(airWeight.getDeferredMinAmt()!=null)
  							{
  							deferredMinAmt=df2.format(airWeight.getDeferredMinAmt()).toString();
  							}
  							expmin=expressRate+"/"+expressMinAmt;
  							genmin=generalRate+"/"+generalMinAmt;
  							defmin=deferredRate+"/"+deferredMinAmt;
  							
  						}
  						}
  						Map map=getAirBottomLineRate(airRates.getId());
  							gRate = map.get("generalrate").toString();
  							ERate = map.get("expressrate").toString();
  						link = editPath+"?param="+ airRates.getId();
  						}
  		 		%>
  		 		
  		 		
  		 		
  		 		<display:column title="" style="color:red"><%=exist%></display:column> 
				<display:column title="&nbsp;&nbsp;&nbsp;Comm#"  href="<%=editPath%>" paramId="paramid"  paramProperty="id"><%=comCodeDisplay%></display:column>				
			    <display:column  title="Comm# Desc"><%=comDescDisplay%></display:column>
				<display:column  title="W_Range"><%=weightRange%></display:column>
				<display:column  title="General/Min"><%=genmin%></display:column>
				<display:column  title="Express/Min"><%=expmin%></display:column>
				<display:column  title="Deferred/Min"><%=defmin%></display:column>
				<display:column  title="Effective Date"><%=date%></display:column>
				<display:column  title="User"><%=user%></display:column>
				<display:column title="BottomLineRate">
	<span onmouseover="tooltip.show('<strong>General Rates</strong><%=gRate  %><br/><strong>Deferred Rates</strong><%=ERate%> ',null,event);" 
	onmouseout="tooltip.hide();"><%="BL Rates"%></span> 
	</display:column>
			     <display:column title="Actions">
  		 		   <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event)" onmouseout="tooltip.hide()" ><img src="<%=path%>/img/icons/pubserv.gif" border="0" onclick="window.location.href ='<%=link %>'" /></span>
  		 		</display:column>
			 	<%i++; %>
		 </display:table>
		 <%}  %>
		 </table>
		 
		 </div>
		 </div>
		 </td></tr></table>
	 	 
<%--		 <table width="100%">--%>
<%--		 <tr>--%>
<%--    			<td  colspan ="9" height="15"  class="headerbluesmall">&nbsp;&nbsp;<%=comcaps%> </td>--%>
<%--  			</tr>--%>
       <br/>
       <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
	   <tr class="tableHeadingNew">
				<td>
					<table width="100%" cellpadding="0" cellspacing="0" >
						<tr class="tableHeadingNew"><td><%=comcaps%></td>
						<td align="right">
							<%
         			 int k=0;
			         String all=comcaps;
					 String getAll=null;
					 if(all!=null)
					 {
						int ty=all.indexOf("All");
						if(ty > -1)
						{
							getAll="all";
						}
					}	
					if(session.getAttribute("airRatescaptionCaps")!=null)
					 { 
						String getresult=(String)session.getAttribute("airRatescaptionCaps");
						if(getresult!=null && getresult.equals("No Accessorial Add"))
						{ 
				%>
				
				     <input type="button" value="Add-Accessorial" onclick="openwindow()" class="buttonStyleNew" style="width:110px"/>
				
				<% }else
					{	 if(getAll!=null)
						 {
				%>
				
				     	 <input type="button" value="ShowStandard-Only" onclick="searchStarndardform()" class="buttonStyleNew" style="width:100px"/>
				
				<%
					}//sub if
					else
					{
				%>
			  
				   <input type="button" value="ShowAll" onclick="searchallform()" id="searchall" class="buttonStyleNew" />
			   
				<%
					}//sub else
			     
				%>
				 
					 <input type="button" value="Add-Accessorial" onclick="openwindow()" class="buttonStyleNew" style="width:100px"/>
				
					<input type="button" value="Doc/Com/Mis" onclick="openwindowDoc()" class="buttonStyleNew" style="width:80px"/>
				 
				<% }
	          	  }//if
				%>
				</td>
						<td align="right"><a id="commonChargesToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a> </td>
						</tr>
					</table>
				</td>		
			</tr>
  	    <tr>
           <td align="left" >
           <div id="common_Charges_vertical_slide">
           <div id="divtablesty1" class="scrolldisplaytable">
           <table width="100%" border="0" cellpadding="0" cellspacing="0">
           <tr>
             
	     </tr>
       <% 
	     if(aircommon!=null && aircommon.size()>0)
	      {
       %>
		 <display:table name="<%=aircommon%>" pagesize="<%=pageSize%>"  defaultsort="1" class="displaytagstyle" id="nonairratestable" sort="list" > 
			 	
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
				<display:setProperty name="paging.banner.item_name" value="Air Rates"/>
  				<display:setProperty name="paging.banner.items_name" value="Air Rates"/>
  				<%
  					
  					String user="";
  					String date="";
  					String chargeCode = "";
  					String chargeType="";
  					String standard="";
  					String asFrfgted="";
  					String amount="";
  					String temPath="";
  					String moreinfo="";
  					String time="";
  					String amtlbs="";
  					String minAmt="";
  					String insuranceRate="";
  					String insuranceAmt="";
  					String chnaged="";
  				
  					String chargeDesc="";
  					List chargeCodeList=new ArrayList();
  					if(aircommon!=null && aircommon.size()>0)
  					{
  					 AirStandardCharges retailRate=(AirStandardCharges)aircommon.get(k); 	
  					
  						StandardChargesDAO sDAO=new  StandardChargesDAO();
  						StandardCharges manageRetailRates=sDAO.findById(retailRate.getStandardId());
  						if(manageRetailRates.getComCode()!=null)
  						{
  							if(manageRetailRates!=null && airStdMap.containsKey(manageRetailRates.getId())==false)
  							{
  							
  							
  							}
  						}
  						if(session.getAttribute("getUpdateAss")!=null)
  						{
							AirStandardCharges rr=(AirStandardCharges)session.getAttribute("getUpdateAss");
							if(rr.getChargeCode()!=null && retailRate.getChargeCode()!=null && 
							rr.getChargeCode().getId().equals(retailRate.getChargeCode().getId()))
							{
							chnaged="a";
							}
							}
  						if(retailRate.getChargeCode()!=null)
  							{
  								chargeCode=retailRate.getChargeCode().getCode();
  								chargeDesc=retailRate.getChargeCode().getCodedesc();
  							}
  							if(retailRate.getChargeType()!=null)
  							{
  								chargeType=retailRate.getChargeType().getCodedesc();
  							}
  							if(retailRate.getStandard()!=null)
  							{
  								standard=retailRate.getStandard();
  							}
  							if(retailRate.getAmtPer100lbs()!=null)
  							{
  								amtlbs=df2.format(retailRate.getAmtPer100lbs()).toString();
  							}
  							if(retailRate.getMinAmt()!=null)
  							{
  								minAmt=df2.format(retailRate.getMinAmt()).toString();
  							}
  							if(retailRate.getInsuranceRate()!=null)
  							{
  								insuranceRate=df2.format(retailRate.getInsuranceRate()).toString();
  							
  							}
  							if(retailRate.getInsuranceAmt()!=null)
  							{
  								insuranceAmt=df2.format(retailRate.getInsuranceAmt()).toString();
  							}
  							if(retailRate.getAsFrfgted()!=null)
  							{
  								asFrfgted=retailRate.getAsFrfgted();
  							}
	  						if(retailRate.getEffectiveDate()!=null)
	  						{
	  							date=dateFormat.format(retailRate.getEffectiveDate());
	  							time=dateFormat1.format(retailRate.getEffectiveDate());
	  						}
	  						if(retailRate.getWhoChanged()!=null)
	  						{
	  							user=retailRate.getWhoChanged();
	  						}
		  					if(chargeType!=null && chargeType.equalsIgnoreCase("Flat rate"))
	  						{
	  							if(retailRate.getAmount()!=null)
	  							{
	  							amount=df2.format(retailRate.getAmount());
	  							}
	  						}
	  						if(chargeType!=null && chargeType.equalsIgnoreCase("Charge percent"))
	  						{
	  						if(retailRate.getPercentage()!=null)
	  							{
	  							amount=per.format(retailRate.getPercentage());
	  							}
	  						}
	  						if(chargeType!=null && chargeType.equalsIgnoreCase("As Freighted"))
	  						{
	  						amount=insuranceAmt+"/"+insuranceRate;
	  						}
	  						if(chargeType!=null && chargeType.equalsIgnoreCase("Air rates in Dollars per pound"))
	  						{
	  						amount=amtlbs;
	  						}
	  						String iStr=String.valueOf(k);
	  						temPath=editPath+"?ind="+iStr+"&paramid="+retailRate.getStandardId();	
  				 			 moreinfo=editPath+"?ind="+iStr+"&param="+retailRate.getStandardId();	
  					}
  					
  					
  		 		%>
  		 		
  		 		
  		 		<display:column  style="width:2%;visibility:hidden"><%=chargeCode%></display:column>
  		 		<display:column  title="Chrg Code" ><a href="<%=temPath %>"/> <%=chargeCode%></a></display:column>
  		 		<display:column  title="Chrg Desc" ><%=chargeDesc%></display:column>
				<display:column  title="Chrg Type"><%=chargeType%></display:column>
  		 		<display:column  title="Std"><%=standard%></display:column>
			 	<display:column  title="Amt"><%=amount%></display:column>
  		 		<display:column  title="MinAmt" ><%=minAmt%></display:column>
				<display:column  title="Effective Date" ><%=date%></display:column>
				<display:column  title="Time" ><%=time%></display:column>
				<display:column  title="User"><%=user%></display:column>
				<display:column title="Actions">
  		 		   <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event)" onmouseout="tooltip.hide()" ><img src="<%=path%>/img/icons/pubserv.gif" border="0" onclick="window.location.href ='<%=moreinfo %>'" /></span>
  		 		</display:column>
			 	<%k++; %>
		 </display:table>
		 <%} %>
		 
</table>
</div>
</div>
</td>
</tr>
</table>
<%}%>
	
<html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
<html:hidden property="search"  value="<%=search%>"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

