<%@ page import="com.gp.cong.logisoft.hibernate.dao.FclBuyCostDAO,com.gp.cong.logisoft.hibernate.dao.FclBuyDAO,java.text.*,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.FclBuy,com.gp.cong.logisoft.domain.FclBuyCost,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,java.text.DecimalFormat"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<jsp:useBean id="airRatesBean" class="com.gp.cong.logisoft.beans.AirRatesBean" scope="request"/>   

<%
String path = request.getContextPath();
String temp1="";
	FclBuy fclBuy=new FclBuy();
	airRatesBean.setMatch("match");
	String editpath="",trmNum="",trmNam="",destAirPortNo="",destAirPortName="",comCode="006100",comDesc="DEPARTMENT STORE MERCHANDISE",modify="";
	String sslineno="",sslinename="",orgCap="",destCap="",comCap="",ssLineCap=""; 
	String orgRegion="";
	String destRegion="";
	List records=new ArrayList();
	DecimalFormat df=new DecimalFormat("0.00");
	Map fclStdMap = new HashMap(); 
		Map fclStdMap1 = new HashMap(); 
	if(session.getAttribute("trade")!=null)
	{
		session.removeAttribute("trade");
	}
	Map displayList=new HashMap();
if(session.getAttribute("compressList")!=null){
displayList=(Map)session.getAttribute("compressList");
}
	request.setAttribute("airRatesBean",airRatesBean);
	
	if(session.getAttribute("searchfclrecords")!=null){
		fclBuy=(FclBuy)session.getAttribute("searchfclrecords");
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
		if(fclBuy.getOriginalRegion()!=null)
		{
		orgRegion=fclBuy.getOriginalRegion();
		}
		if(fclBuy.getDestinationRegion()!=null)
		{
		destRegion=fclBuy.getDestinationRegion();
		}
	}
	NumberFormat numb = new DecimalFormat("###,###,##0.00");
	if(session.getAttribute("fclmessage")!=null){
			//message=(String)session.getAttribute("fclmessage");
		}
	if(request.getParameter("programid")!= null){
			 modify=(String)request.getParameter("programid");
			 session.setAttribute("processinfoforfl",modify);
		}
	if(session.getAttribute("searchFclcodelist")!=null){
			records=(List)session.getAttribute("searchFclcodelist");
		}
	editpath=path+"/searchFCL.do";	
  %>	
  	
<html> 
	<head>
		<title>JSP for SearchLCLColoadForm form</title>
		<%@include file="../includes/baseResources.jsp" %>
	<script language="javascript" src="<%=path%>/js/tooltip/tooltip.js"></script>
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    dojo.addOnLoad( function() {
		    window.refresh });
		</script>
<%--		<script type="text/javascript" src="<%=path%>/js/mootools/mootools.js"></script>--%>
		<script type="text/javascript">
		var airRatesVerticalSlide;
		var commonChargesVerticalSlide;
		window.addEvent('domready', function() {
				commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
				commonChargesVerticalSlide.toggle();
				$('commonChargesToggle').addEvent('click', function(e){
					commonChargesVerticalSlide.toggle();
				});				

			});
			
		 var newwindow = '';  
		function uploadfile(){
	       if (!newwindow.closed && newwindow.location) {
             	newwindow.location.href = "<%=path%>/jsps/ratemanagement/LoadFCLPopup.jsp";
           }else {
         		newwindow=window.open("<%=path%>/jsps/ratemanagement/LoadFCLPopup.jsp?futureratesload="+"add","","width=400,height=250");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           }
		function searchform(){
		if(document.searchFCLForm.comCode.value=="")
		{
		alert("Please Enter the Commodity Code");
		return;
		}
			if((document.searchFCLForm.terminalNumber.value=="" && document.searchFCLForm.sslinenumber.value=="") || 
			(document.searchFCLForm.terminalNumber.value=="" && document.searchFCLForm.destSheduleNumber.value=="") ||
			(document.searchFCLForm.sslinenumber.value=="" && document.searchFCLForm.destSheduleNumber.value=="")){
					alert("Kindly please select either \n OriginTerminal or Dest Airport or Commodity or SSLine ");
					return;
				}
			document.searchFCLForm.buttonValue.value="search";
			document.searchFCLForm.index.value="get";
			document.searchFCLForm.submit();
			}
		function searchallform(){
			document.searchFCLForm.buttonValue.value="showall";
			document.searchFCLForm.submit();
			}
		
       function getSSLine(ev){
			if(event.keyCode==9){
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	   function getSSLineName(ev){
			if(event.keyCode==9){
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	 function titleLetter(ev){
			if(event.keyCode==9){
				document.searchFCLForm.terminalName.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			    }
			}
	 function getDestination(ev){
		if(event.keyCode==9){
				document.searchFCLForm.destAirportname.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	 function getComCode(ev){
			if(event.keyCode==9){
				document.searchFCLForm.comDescription.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	 function titleLetterName(ev){
			if(event.keyCode==9){
				document.searchFCLForm.terminalNumber.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	function getDestinationName(ev){
		if(event.keyCode==9){
				document.searchFCLForm.destSheduleNumber.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	 function getComCodeName(ev){
			if(event.keyCode==9){		
				document.searchFCLForm.comCode.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
					}
		}
	 function titleLetterPress(ev)	{
			if(event.keyCode==13){
				document.searchFCLForm.terminalName.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			    }
		}
	 function getDestinationPress(ev){
			if(event.keyCode==13){
				document.searchFCLForm.destAirportname.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
				}
			}
	 function getComCodePress(ev){
			if(event.keyCode==13){
				document.searchFCLForm.comDescription.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	 function titleLetterNamePress(ev){
			if(event.keyCode==13){
				document.searchFCLForm.terminalNumber.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	 function getDestinationNamePress(ev){
			if(event.keyCode==13){
				document.searchFCLForm.destSheduleNumber.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	 function getComCodeNamePress(ev){
			if(event.keyCode==13){
				document.searchFCLForm.comCode.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}	
	function getsslineno(ev){
		  if(event.keyCode==9){
			document.searchFCLForm.sslinename.value="";
			document.searchFCLForm.buttonValue.value="popupsearch";
			document.searchFCLForm.submit();
			}
		}
	function getsslinenopress(ev){
		  if(event.keyCode==13){
				document.searchFCLForm.sslinename.value="";
				document.searchFCLForm.buttonValue.value="popupsearch";
				document.searchFCLForm.submit();
			}
		}
	 function getsslinename(ev) {
		  if(event.keyCode==9){
			document.searchFCLForm.sslinenumber.value="";
			document.searchFCLForm.buttonValue.value="popupsearch";
			document.searchFCLForm.submit();
			}
	 	}
	 function getsslinenamepress(ev) {
		  if(event.keyCode==13){
			document.searchFCLForm.sslinenumber.value="";
			document.searchFCLForm.buttonValue.value="popupsearch";
			document.searchFCLForm.submit();
		}
	 }
	 function ClearScreen() {
	    document.searchFCLForm.buttonValue.value="clear";
		document.searchFCLForm.submit();
	 }
	 function getFclCurrent() {
	           window.location.href="<%=path%>/jsps/ratemanagement/AddFCLRecords.jsp";
	 }
	 //type 2 dojo implementation
	 
	 
	 		 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.searchFCLForm.terminalNumber.value;
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
    	  		document.searchFCLForm.terminalName.value=orgTrmName;
    	    	document.getElementById("terminalNumber").value=orgTrm;
    	    }else{
    	    	var params = new Array();
			  	params['requestFor'] = "OrgTerminalNumber";
			  	params['terminalName'] = document.searchFCLForm.terminalName.value;
			  	termName = document.searchFCLForm.terminalName.value;
			  	var bindArgs = {
	  		  		url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  		  		error: function(type, data, evt){alert("error");},
	 		  		mimetype: "text/json",
	  				content: params
				};
				
				document.searchFCLForm.terminalName.value=document.searchFCLForm.terminalName.value;
				var req = dojo.io.bind(bindArgs);
				dojo.event.connect(req, "load", this, "populateTerminalNumber");
			}
			
		}
	}
	function populateTerminalNumber(type, data, evt) {
  		if(data){
   			document.getElementById("terminalNumber").value=data.terminalNumber;
   			document.searchFCLForm.terminalName.value=termName;
   		}
	}
  function getdestPort(ev){ 
	 	document.getElementById("destAirportname").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.searchFCLForm.destSheduleNumber.value;
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
				document.searchFCLForm.destAirportname.value = destTrmName;
				document.getElementById("destSheduleNumber").value= destTrm;
			}else{
				var params = new Array();
				destPortName = document.searchFCLForm.destAirportname.value;
				params['requestFor'] = "OrgTerminalNumber";
				params['terminalName'] = document.searchFCLForm.destAirportname.value;
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
   			document.searchFCLForm.destAirportname.value = destPortName;
   		}
	}	 
		function getCodeDesc(ev){ 
		document.getElementById("comDescription").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.searchFCLForm.comCode.value;
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
			params['codeDesc'] = document.searchFCLForm.comDescription.value;
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
			params['custNo'] = document.searchFCLForm.sslinenumber.value;
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
			params['custName'] = document.searchFCLForm.sslinename.value;
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
		
function getExpand(){
  //document.getElementById("Expand").checked=true;
    // document.getElementById("Collapse").checked=false;
     var a=document.getElementById("expandRates").innerHTML;
var b=document.getElementById("bundle").innerHTML;
document.getElementById("bundle").innerHTML=a;
document.getElementById("expandRates").innerHTML=b;
document.getElementById("expandRates").style.visibility='hidden';
 document.getElementById("bundle").style.visibility='visible';
 }
 function getCollapse(){
     var a=document.getElementById("expandRates").innerHTML;
     var b=document.getElementById("bundle").innerHTML;
     document.getElementById("bundle").innerHTML=a;
     document.getElementById("expandRates").innerHTML=b;
 document.getElementById("expandRates").style.visibility='hidden';
  document.getElementById("bundle").style.visibility='visible';
 }		
	</script>
	<%@include file="../includes/resources.jsp" %>
	</head>
<%
	if(session.getAttribute("fclCollapas")!=null){ 
%>
	<body class="whitebackgrnd" onLoad="getdisabled()">
<%  
	}else{   
%>
	<body class="whitebackgrnd">
<%
	}
%>
		<html:form action="/searchFCL" scope="request">
<table width="100%"  border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
  
<%
 if(session.getAttribute("fclCollapas")==null){
%>
        <table width="100%" class="tableBorderNew" border="0" cellpadding="0" cellspacing="0">
  		<tr class="tableHeadingNew"><td colspan="3"><b>Search Current FCL Rates</b></td>
  			<td align="right"><input type="button" value="Import Excel" class="buttonStyleNew" onclick="GB_show('Import Excel','<%=path%>/jsps/ratemanagement/LoadFCLPopup.jsp?futureratesload='+'add',200,600)" id="addnew"/>
	  				</td>
  		  </tr><tr><td>
           <table width="100%" border="0" cellpadding="0" cellspacing="4">
  			<tr>
 			 <td class="style2">Org Region</td>
 			 <td><input name="orgRegion" id="orgRegion" value="<%=orgRegion%>" size="30">
 			<dojo:autoComplete formId="searchFCLForm" textboxId="orgRegion" action="<%=path%>/actions/getChargeCodeDesc.jsp?from=0&tabName=SEARCH_FCL"/>
 			 </td>
 			 <td class="style2">Dest Region</td>
 			 <td><input name="destRegion" id="destRegion" value="<%=destRegion%>" size="30">
 			<dojo:autoComplete formId="searchFCLForm" textboxId="destRegion" action="<%=path%>/actions/getChargeCodeDesc.jsp?from=1&tabName=SEARCH_FCL"/>
 			 </td>
 			 </tr>
  	 	    <tr>
	    	  <td class="style2">Org Trm</td>
	    		<td >
<%--    		  <html:text property="terminalNumber" size="2" onkeydown="titleLetter(this.value)" maxlength="2" styleId="terminalNumber" value="<%=trmNum%>"/>--%>
                  <input name="terminalNumber" id="terminalNumber" onkeydown="getTerminalName(this.value)" value="<%=trmNum%>" size="30"/>
		          <dojo:autoComplete formId="searchFCLForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=SEARCH_FCL&from=0"/>
             	</td>
				<td><span class="textlabels">Org Trm Name</span></td>
	    		<td >
<%--	    	  <html:text property="terminalName"  onkeydown="titleLetterName(this.value)" value="<%=trmNam%>"/>--%>
	    		  <input  name="terminalName" id="terminalName" value="<%=trmNam%>" onkeydown="getTerminalNumber(this.value)" size="30"/>
                  <dojo:autoComplete formId="searchFCLForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_FCL&from=0"/>
	    		</td>
<%--					<td><html:radio property="match" value="starts" name="airRatesBean"></html:radio></td>--%>
<%--					<td class="style2">Start list at</td>--%>
 			 </tr>
 			
		     <tr>	
    		   <td class="textlabels">
    		  	 Dest port</td>
		    	<td >
<%--		    			<html:text property="destSheduleNumber"  size="5" onkeydown="getDestination(this.value)" maxlength="5" value="<%=destAirPortNo%>"></html:text><br>--%>
		    			<input  name="destSheduleNumber" id="destSheduleNumber"  maxlength="5"    value="<%=destAirPortNo%>"  onkeydown="getdestPort(this.value)" size="30"/>
	                	<dojo:autoComplete  formId="searchFCLForm" textboxId="destSheduleNumber"  action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=SEARCH_FCL&from=1"/>
		    			</td>
		    			<td><span class="textlabels">Dest Port Name</span></td>
		    			<td>
<%--		    			<html:text property="destAirportname" onkeydown="getDestinationName(this.value)" value="<%=destAirPortName%>"/><br>--%>
		    			<input name="destAirportname"  id="destAirportname" value="<%=destAirPortName%>" onkeydown="getDestPortName(this.value)" size="30"/>
                    	<dojo:autoComplete formId="searchFCLForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_FCL&from=1"/>
		    			</td>
		    		</tr>
		    		 
	    			<tr>	
    				<td class="textlabels">
    					Com Code</td>
			    			<td >
<%--			    			<html:text property="comCode" maxlength="6" size="6" onkeydown="getComCode(this.value)" value="<%=comCode%>"></html:text><br>--%>
			     		<input name="comCode"  id="comCode" maxlength="6"  onkeydown="getCodeDesc(this.value)" value="<%=comCode%>" size="30"/>
    			       <dojo:autoComplete formId="searchFCLForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=SEARCH_FCL"/>
			    			</td>
			    			
			    			<td class="textlabels">Com Description </td>
			    			<td >
<%--			    			<html:text property="comDescription" onkeydown="getComCodeName(this.value)" value="<%=comDesc%>"/>--%>
			    	<input name="comDescription" id="comDescription" value="<%=comDesc%>" onkeydown="getCode(this.value)" size="30"/>
    			    <dojo:autoComplete formId="searchFCLForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=SEARCH_FCL&from=2"/>
			    			</td>
			    			
			    		</tr>	
    		
    				<TR>
  					<td class="textlabels">
  					SS Line Code</td>
    			<td >
<%--    			<html:text property="sslinenumber"  size="6" onkeydown="getSSLine(this.value)" maxlength="5" value="<%=sslineno%>"/>--%>
    				<input name="sslinenumber" id="sslinenumber"  maxlength="5" value="<%=sslineno%>" onkeydown="getSslineName(this.value)" size="30"/>
    			    <dojo:autoComplete formId="searchFCLForm" textboxId="sslinenumber" action="<%=path%>/actions/getCustomer.jsp?tabName=SEARCH_FCL&from=0"/>
    			</td>
    			
    			<td class="textlabels">SS Line Name</td>
    			<td >
<%--    			<html:text property="sslinename"  onkeydown="getSSLineName(this.value)" value="<%=sslinename%>"/>--%>
    			<input name="sslinename" id="sslinename"  value="<%=sslinename%>" onkeydown=" getSslineNo(this.value)" size="30"/>
    			 <dojo:autoComplete formId="searchFCLForm" textboxId="sslinename" action="<%=path%>/actions/getCustomerName.jsp?tabName=SEARCH_FCL&from=0"/>
    			</td>
    		</TR>
  			<tr><TD colspan="5">
  			<table border="0"><tr>
				

					<td>
						<html:radio property="match" value="match" name="airRatesBean"></html:radio>
					</td>
					<td class="style2">
						Match Only
					</td>

				</tr>
				</table></TD>
  			</tr>
  			<tr>
	  				<td colspan="5" align="CENTER">
	  				   
	  				   <input type="button" value="Search" class="buttonStyleNew" onclick="searchform()"  />
	  				   <input type="button" value="Clear" class="buttonStyleNew" id="cancel" onclick="ClearScreen()"/>
					</td>
  			</tr>
  			</table>
  			</td>
  			</tr>
  			</table>
  	<%}else{ %>	
    <table width="100%" class="tableBorderNew" border="0" cellpadding="0" cellspacing="0">
  		<tr class="tableHeadingNew"><td ><b>Current FCL Rates</b></td>
  		
		  <td align="right">
		          <input type="button" value="Search" class="buttonStyleNew" id="cancel" onclick="ClearScreen()"  />
	  			  <input type="button" value="AddNew" class="buttonStyleNew" id="addnew"	
	  			  onclick="GB_show('FCL Current','<%=path%>/jsps/ratemanagement/AddFCLPopup.jsp?fclcorates='+'add',200,600)"/>
		</td>
		
  		  </tr><tr><td width="100%" colspan="2">
           <table width="100%" class="displaytagstyle" border="0" cellpadding="0" cellspacing="2">
				<thead >
				<tr>
					<td ><b>
						<%=orgCap%>
					</td>
					<td><b>
						<%=destCap%>
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
					<td class="textlabels"><b><%=trmNam%></b></td>
	  				<td class="textlabels"><b><%=destAirPortName%></b></td>
	   		   		<td class="textlabels"><b><%=comDesc%></b></td>
	   		   		<td class="textlabels"><b><%=sslinename%>
   			   </tr>
   			</table>
	</td>
	</tr>
	</table>
	<br>

	<table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0">
  		<tr class="tableHeadingNew"><td><%=session.getAttribute("fclatescaption")%></td>
  		 <td><img src="<%=path%>/img/icons/up.gif" border="0" onclick="getExpand()"/></td>
  		</tr>	
		<tr>
		<td width="100%" colspan="2" id="bundle">
		     <div id="common_Charges_vertical_slide">
        		<div id="divtablesty1" class="scrolldisplaytable" style="width:1000px">
          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int i=0;
         			 	String temp="";
          		%>
          	 	<display:table name="<%=records%>" pagesize="<%=pageSize%>"  class="displaytagstyle" id="lclcoloadratestable" sort="list"> 
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> FCL Buy Rates Displayed,For more Data click on Page Numbers.
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
						No Records Found.
					</span>
				</display:setProperty>
				<display:setProperty name="paging.banner.placement" value="bottom" />
				<display:setProperty name="paging.banner.item_name" value="FCL BUY Rates"/>
				<display:setProperty name="paging.banner.items_name" value="FCL BUY Rates"/>
  				
 <%
 	String unitycode="", code="",codetype="",retail="",org="",dest="",codeNum="",ssline="",sslineDesc="",destDesc="";
	String CodeDesc="",costcode="",retot="",link="",orgDesc="",aircurr="";
  	double rate=0.0,ctc=0.0, ftf=0.0,min=0.0;
  	int count=0;
  	
  	String ratAmount[]=new String[50];
  	String Markup[]=new String[50];
  	String Total[]=new String [50];
  	String curr[]=new String[50];
  	List unittypelist=new ArrayList();
  	FclBuyCost fclBuyCost=new FclBuyCost();
	FclBuy fclbuy=new FclBuy();
  	FclBuyDAO fcl=new FclBuyDAO();
  	String moreInfo="";
  	String link1 ="";
  
  	if(session.getAttribute("searchunittypelist")!=null){
			unittypelist=(List)session.getAttribute("searchunittypelist");
			}
	if (records != null && records.size() > 0){
		 fclBuyCost=(FclBuyCost)records.get(i);
		 fclbuy=fcl.findById(fclBuyCost.getFclStdId());
		 if(fclbuy!=null ){
		 	
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
		if( fclBuyCost.getCostId()!=null){
		 		costcode=fclBuyCost.getCostId().getCodedesc();
				code=fclBuyCost.getCostId().getCode(). toString();
				if(fclBuyCost.getContType()!=null){
					codetype=fclBuyCost.getContType().getCode();
				}
		}
		if(fclBuyCost.getRetail()!=null){
		retail=numb.format(fclBuyCost.getRetail());
		}
		if(fclBuyCost.getCurrency()!=null){
		aircurr=fclBuyCost.getCurrency();
		}
			String iStr=String.valueOf(i);
			link=editpath+"?ind="+iStr+"&paramid="+fclBuyCost.getFclStdId();
			link1=editpath+"?param="+fclBuyCost.getFclStdId();	
		}
		
%>
 	<%if(!temp.equals(sslineDesc)){
 	
 	 %>
		<display:column title="Origin"  >
				<span class="hotspot" onmouseover="tooltip.show('<strong><%=orgDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=org%></a></span> 
</display:column>
		<display:column title="Destination">
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=destDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=dest%></span> 
		</display:column>
		<display:column  title="Commodity">
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=CodeDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=codeNum%></span> 
		</display:column>				
		<display:column  title="Carrier"> <a href="<%=link %>">
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=sslineDesc%></strong>',null,event);" onmouseout="tooltip.hide();"><%=ssline%></span> 
		</display:column>	
		<%}else{ %>	
<display:column title="Origin"></display:column> >
<display:column title="Destination"></display:column>	
<display:column title="Commodity"></display:column>	
<display:column title="Carrier"></display:column>	
		<%} %>		
		<display:column  title="Code"> 
			<span class="hotspot" onmouseover="tooltip.show('<strong><%=costcode%></strong>',null,event);" onmouseout="tooltip.hide();"><%=code+"/"+codetype%></span> 
		</display:column>
		<display:column  title="Currency"> <%=aircurr%></display:column>
		<display:column  title="Retail"> <%=retail%></display:column>
		
 <%
		Double total=0.00;
				 String total1="";
				 String markUp="";	 
				 String title="";
				  String amount="";
if(fclBuyCost.getUnitTypeList()!=null){

				 for(int j=0;j<fclBuyCost.getUnitTypeList().size();j++){ 
				  FclBuyCostTypeRates fclBuyCostTypeRates=(FclBuyCostTypeRates)fclBuyCost.getUnitTypeList().get(j);
				title=fclBuyCostTypeRates.getUnitname();
				
				 if(fclBuyCostTypeRates.getMarkup()!=null){
				 markUp=numb.format(fclBuyCostTypeRates.getMarkup());
				 }
				 if(markUp.contains(",")){
				 markUp=markUp.replace(",","");
				 }
				 
				  if(fclBuyCostTypeRates.getActiveAmt()!=null){
				  amount=numb.format(fclBuyCostTypeRates.getActiveAmt());
				  }
				  if(amount.contains(",")){
				  amount=amount.replace(",","");
				  }
				   
				 if(!amount.trim().equals("") || !markUp.trim().equals("")){
				 total=Double.parseDouble(amount)+Double.parseDouble(markUp);
				total1=numb.format(total);
				 }
				 %>
			
				 <display:column title="<%=title%>" > <%=amount%></display:column>
				<display:column title="MarkUp"  style="text-align:right;"><%=markUp%> </display:column>
				<display:column title="Total" style="text-align:right;"><%=total1%></display:column>
			
				<%}}
				temp=sslineDesc;
		i++; %>
		  <display:column title="Actions">
		   
		    <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);" 
		    onmouseout="tooltip.hide();">  <img src="<%=path%>/img/icons/pubserv.gif" 
		    border="0"  onclick="window.location.href ='<%=link1%>'" /></span>
		  </display:column>
		</display:table>
	   	 </table>
	   	 </div>
	   	 </div>
   		 </td>
  	 </tr>
  	 <tr>
  	<td>
</table>
	
	
	
	<td width="100%">
     <div id="expandRates">
         <table class="displaytagstyle" width="100%">
         <%
         String sslineName="";
         Integer ssline;
         List rateAndHazardList=new ArrayList();
         Map rateMap=new HashMap<String,Double>();
          Map hazardsMap=new HashMap<String,Double>();
         Set ssLineKeySet = displayList.keySet();
	Iterator ssLineIt = ssLineKeySet.iterator(); 
	List sortedList=new ArrayList();
	while(ssLineIt.hasNext()) {
		Object key = ssLineIt.next();
		ssline=(Integer)key;
		rateAndHazardList =(List) displayList.get(key);
		rateMap = (Map<String,Double>)rateAndHazardList.get(0);
		hazardsMap = (Map<String,Double>)rateAndHazardList.get(1);
		
		Set keySet=new HashSet<List>();
		keySet = rateMap.keySet();
		sortedList=new ArrayList(keySet);
		Collections.sort(sortedList);
		Set hazardKeySet=new HashSet<List>();
		hazardKeySet = hazardsMap.keySet();
	%>
	
         <thead>
          <td>Origin Terminal</td>
        <td>Destination Port</td>
        <td>Commodity</td>
        
         <td width="25%">SSline</td>
       
         <td width="25%">Charge Code</td>
         
          <%for(Iterator iter = sortedList.iterator(); iter.hasNext();){
      	
         String title=(String)iter.next();
         request.setAttribute("title",title);%>
         <td width="25%"><%=title%></td>
         <%} %>
         </thead>
         <% break;} 
        %>
      
<tbody >
         <%
          Map fclBuyMap=(Map)session.getAttribute("fclBuyMap");
         
       sslineName="";
       String orgDesc="";
       String org="";
       String destDesc="";
       String dest="";
       String CodeDesc="";
       String codeNum="";
       String sslcode="";
       String link="";
       String link1="";
      i=0;
         rateAndHazardList=new ArrayList();
         rateMap=new HashMap<String,Double>();
         hazardsMap=new HashMap<String,Double>();
       ssLineKeySet = displayList.keySet();
	ssLineIt = ssLineKeySet.iterator(); 
	String className="odd";
	while(ssLineIt.hasNext()) {
		Object key = ssLineIt.next();
		ssline=(Integer)key;
		
		if(fclBuyMap.containsKey(ssline)){
		FclBuy newfclBuy=(FclBuy)fclBuyMap.get(ssline);
		
		sslineName=newfclBuy.getSslineNo().getAccountName();
		orgDesc=newfclBuy.getOriginTerminal().getUnLocationName();
		 
		org=newfclBuy.getOriginTerminal().getUnLocationCode();
		destDesc=newfclBuy.getDestinationPort().getUnLocationName();
		dest=newfclBuy.getDestinationPort().getUnLocationCode();
		CodeDesc=newfclBuy.getComNum().getCodedesc();
		codeNum=newfclBuy.getComNum().getCode();
		sslcode=newfclBuy.getSslineNo().getAccountno();
		String iStr=String.valueOf(i);
			link=editpath+"?ind="+iStr+"&paramid="+newfclBuy.getFclStdId();
			link1=editpath+"?param="+newfclBuy.getFclStdId();	
		}
		rateAndHazardList =(List) displayList.get(key);
		rateMap = (Map<String,Double>)rateAndHazardList.get(0);
		hazardsMap = (Map<String,Double>)rateAndHazardList.get(1);
		Set keySet=new HashSet<List>();
		keySet = rateMap.keySet();
		Set hazardKeySet=new HashSet<List>();
		hazardKeySet = hazardsMap.keySet();
	%>
          <% if(sortedList.size()>0){%>  
          <tr class="<%=className%>">
       
        <td><%=orgDesc%></td>
        <td><%=destDesc%></td>
        <td><%=CodeDesc%></td>
        <td><a href="<%=link1%>"><%=sslineName%></a></td>
         <td width="25%">Ocean Freight</td>
        
        
          <%for(Iterator iter = sortedList.iterator(); iter.hasNext();){
          String amount=numb.format(rateMap.get(iter.next()));
        %>
         <td width="25%"><%=amount%></td>
         <%} %>
          </tr>
           <%} %>
           
          <%
          if(hazardsMap.size()>0){
           if(className=="even"){
           className="odd";
          }else{
           className="even";
          }  %>
          <tr class="<%=className%>">
         <td></td>
        <td></td>
        <td></td>
        <td></td>
         
         <%if(hazardKeySet.size()>0){ %>
           <td width="25%">Hazardous</td>
         <%} %>
      
           <%Iterator it2 = hazardKeySet.iterator();
         while(it2.hasNext()){
         String amount=numb.format(hazardsMap.get(it2.next()));%>
         <td width="25%"><%=amount%></td>
         <%} %>
          </tr>
          <%} %>
         <%
          if(className=="even"){
           className="odd";
          }else{
           className="even";
          }
         
          i++;
         } %>
        
         </tbody>
         </table>
         </div>
         </td>
<table width="100%">
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
</td>
</tr>
</table>
<%} %>
<html:hidden property="buttonValue" styleId="buttonValue" />
<html:hidden property="index"  />
<script>getCollapse()</script>
</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


