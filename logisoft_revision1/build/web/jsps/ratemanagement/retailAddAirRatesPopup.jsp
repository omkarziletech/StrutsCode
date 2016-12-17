<%@ page language="java"%>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,
java.util.List,java.util.ArrayList,com.gp.cong.logisoft.domain.RetailStandardCharges,
com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.beans.PortsBean,
com.gp.cong.logisoft.beans.AirRatesBean"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
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
String retailRates="";
List list=new ArrayList();
DBUtil dbutil=new DBUtil();
String exitretailRates="";
AirRatesBean airRatesBean=new AirRatesBean();
RetailStandardCharges retailRatesObj1= null;
RetailStandardCharges retailRatesObj2= new RetailStandardCharges();;
String messge="";

	if(request.getAttribute("message")!=null)
	{
	messge=(String)request.getAttribute("message");	
	}
	airRatesBean.setCommon("");
	if(request.getAttribute("airRatesBean")!=null)
	{
		airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
	}
	request.setAttribute("airRatesBean",airRatesBean);

	if(request.getAttribute("retailrate")!=null)
	{
		retailRates=(String)request.getAttribute("retailrate");
	}
	if(request.getParameter("retailsrates")!=null)
	{
		exitretailRates=(String)request.getParameter("retailsrates");
	if(session.getAttribute("retailmanage")!=null)
	{
		
		RetailStandardCharges sc1=(RetailStandardCharges)session.getAttribute("retailmanage");
		retailRatesObj2.setOrgTerminal(sc1.getOrgTerminal());
		retailRatesObj2.setOrgTerminalName(sc1.getOrgTerminalName());
	    retailRatesObj2.setDestPort(sc1.getDestPort());
	    retailRatesObj2.setDestPortName(sc1.getDestPortName());
	    retailRatesObj2.setComCode(sc1.getComCode());
	    retailRatesObj2.setComCodeName(sc1.getComCodeName());
		
		session.setAttribute("retailstandardCharges",retailRatesObj2);
	}
	}
	if(session.getAttribute("retailstandardCharges")!=null )
	{
    	retailRatesObj1=(RetailStandardCharges)session.getAttribute("retailstandardCharges");
		terminalNumber=retailRatesObj1.getOrgTerminal();
		terminalName=retailRatesObj1.getOrgTerminalName();
		destSheduleNumber=retailRatesObj1.getDestPort();
		destAirportname=retailRatesObj1.getDestPortName();
		/*if(retailRatesObj1.getComCode()!=null){
				comCode=retailRatesObj1.getComCode();
		}
		if(retailRatesObj1.getComCodeName()!=null){
			 comDesc=retailRatesObj1.getComCodeName();
		}*/
		
	
		
	}
	if(retailRatesObj1!=null)
	{
		
		if(retailRatesObj1.getOrgTerminal()!=null && retailRatesObj1.getDestPort()!=null && retailRatesObj1.getComCode()!=null)
			{
				list=dbutil.getRetailDetails(retailRatesObj1.getOrgTerminal(),retailRatesObj1.getDestPort(),retailRatesObj1.getComCode());
				if(list!=null&&list.size()>0){
				if(exitretailRates!=null && !exitretailRates.equals(""))
				{
				}
				else
				{
					messge="PLease Select another Commodity or else change Origin number or Destination port ";
					comCode="";
					comDesc="";
				}
				}
				}
				}
				if(session.getAttribute("exist")!=null){
		//session.removeAttribute("exist");
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
		session.setAttribute("retailmodifyforrelay",request.getParameter("modify"));
 		modify=(String)session.getAttribute("retailmodifyforrelay");
	}
	else
	{
 		modify=(String)session.getAttribute("retailmodifyforrelay");
	}

	if(messge.equals(""))
	{
	if(retailRates.equals("addretailrate"))
		{
%>
		<script>
		
	    parent.parent.getAddRetailRate();
		parent.parent.GB_hide();
<%--			self.close();--%>
<%--			opener.location.href="<%=path%>/jsps/ratemanagement/RetailRatesFrame.jsp";--%>
		</script>
<%
		}
	}
%>

 
<html> 
	<head>
	<%@include file="../includes/resources.jsp" %>
		<title>JSP for RetailAddAirRatesPopupForm form</title>
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
		if(document.retailAddAirRatesPopupForm.terminalNumber.value=="" || document.retailAddAirRatesPopupForm.destSheduleNumber.value=="" )
		{
			alert("Please select OriginTerminal , DestinationAirport & CommodityCode ");
			return;
		}
			if(document.retailAddAirRatesPopupForm.comCode.value=="")
			{
			alert("Please select Commodity Code Port Code");
			return;
			}
			document.retailAddAirRatesPopupForm.buttonValue.value="search";
			document.retailAddAirRatesPopupForm.submit();
		}
	function titleLetter(ev)
	{
	if(event.keyCode==13)
	{
	document.retailAddAirRatesPopupForm.terminalName.value="";
	document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
	document.retailAddAirRatesPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailaddot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function titleLetterDown(ev)
	{
	if(event.keyCode==9)
	{
	document.retailAddAirRatesPopupForm.terminalName.value="";
	document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
	document.retailAddAirRatesPopupForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailaddot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestination(ev)
	{
		if(event.keyCode==13){
	document.retailAddAirRatesPopupForm.destAirportname.value="";  	
	document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
	document.retailAddAirRatesPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailaddairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCode(ev)
	{
	
		if(event.keyCode==13)
		{
		document.retailAddAirRatesPopupForm.comDescription.value="";  	
		document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
		document.retailAddAirRatesPopupForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailaddcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	function titleLetterName(ev)
	{
		if(event.keyCode==13){
		document.retailAddAirRatesPopupForm.terminalNumber.value="";	
		document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
		document.retailAddAirRatesPopupForm.submit();
	  //  window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailaddot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationName(ev)
	{
		if(event.keyCode==13)
		{
		document.retailAddAirRatesPopupForm.destSheduleNumber.value="";  	
		document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
		document.retailAddAirRatesPopupForm.submit();
		//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailaddairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 }
	}
	function getComCodeName(ev)
	{
		if(event.keyCode==13)
		{
		document.retailAddAirRatesPopupForm.comCode.value="";  	
		document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
		document.retailAddAirRatesPopupForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailaddcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationDown(ev)
	{
		if(event.keyCode==9){
		document.retailAddAirRatesPopupForm.destAirportname.value="";  	
	document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
	document.retailAddAirRatesPopupForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailaddairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getComCodeDown(ev)
	{
	
		if(event.keyCode==9)
		{
		document.retailAddAirRatesPopupForm.comDescription.value="";  	
		document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
		document.retailAddAirRatesPopupForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailaddcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	function titleLetterNameDown(ev)
	{
		
		if(event.keyCode==9){
		document.retailAddAirRatesPopupForm.terminalNumber.value="";
		document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
		document.retailAddAirRatesPopupForm.submit();
	  //  window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailaddot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getDestinationNameDown(ev)
	{
		if(event.keyCode==9)
		{
		document.retailAddAirRatesPopupForm.destSheduleNumber.value="";  	
		document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
		document.retailAddAirRatesPopupForm.submit();
		//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailaddairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 }
	}
	function getComCodeNameDown(ev)
	{
		if(event.keyCode==9)
		{
		document.retailAddAirRatesPopupForm.comCode.value="";  	
		document.retailAddAirRatesPopupForm.buttonValue.value="popsearch";
		document.retailAddAirRatesPopupForm.submit();
		//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailaddcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	//type 2 dojo implementation
	
	
			 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.retailAddAirRatesPopupForm.terminalNumber.value;
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
    	  		document.retailAddAirRatesPopupForm.terminalName.value=orgTrmName;
    	    	document.getElementById("terminalNumber").value=orgTrm;
    	    }else{
    	    	var params = new Array();
			  	params['requestFor'] = "OrgTerminalNumber";
			  	params['terminalName'] = document.retailAddAirRatesPopupForm.terminalName.value;
			  	termName = document.retailAddAirRatesPopupForm.terminalName.value;
			  	var bindArgs = {
	  		  		url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  		  		error: function(type, data, evt){alert("error");},
	 		  		mimetype: "text/json",
	  				content: params
				};
				
				termName=document.retailAddAirRatesPopupForm.terminalName.value;
				var req = dojo.io.bind(bindArgs);
				dojo.event.connect(req, "load", this, "populateTerminalNumber");
			}
			
		}
	}
	function populateTerminalNumber(type, data, evt) {
  		if(data){
   			document.getElementById("terminalNumber").value=data.terminalNumber;
   			document.retailAddAirRatesPopupForm.terminalName.value=termName;
   		}
	}
  function getdestPort(ev){ 
	 	document.getElementById("destAirportname").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.retailAddAirRatesPopupForm.destSheduleNumber.value;
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
	function getDestinationPortName(ev){ 
		document.getElementById("destSheduleNumber").value="";
    	if(event.keyCode==9 || event.keyCode==13){
    		if( ev.indexOf("-")!= -1){
				var string  = ev;
    	   		var index = ev.indexOf("-");
    	   		var destTrm = string.substring(index+1,string.length);
    	    	var destTrmName=string.substring(0,index);
				document.retailAddAirRatesPopupForm.destAirportname.value = destTrmName;
				document.getElementById("destSheduleNumber").value= destTrm;
			}else{
				var params = new Array();
				destPortName = document.retailAddAirRatesPopupForm.destAirportname.value;
				params['requestFor'] = "OrgTerminalNumber";
				params['terminalName'] = document.retailAddAirRatesPopupForm.destAirportname.value;
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
   			document.retailAddAirRatesPopupForm.destAirportname.value = destPortName;
   		}
	}	 
		function getCodeDesc(ev){ 
		document.getElementById("comDescription").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.retailAddAirRatesPopupForm.comCode.value;
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
			params['codeDesc'] = document.retailAddAirRatesPopupForm.comDescription.value;
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
		<html:form action="/retailAddAirRatesPopup" name="retailAddAirRatesPopupForm" type="com.gp.cong.logisoft.struts.ratemangement.form.RetailAddAirRatesPopupForm" scope="request">
		<font color="blue"><b><%=messge%></b></font>
			<table width=100%  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
			<tr class="tableHeadingNew"> Add New</tr>
			<tr class="textlabels">
    			<td>Org Trm</td>
    			<td>
    			
    			   <input name="terminalNumber" id="terminalNumber" size="15" onkeydown="getTerminalName(this.value)" maxlength="2" value="<%=terminalNumber%>" />
						<dojo:autoComplete formId="retailAddAirRatesPopupForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp" />
    				
				</td>    			
    			<td>Org Trm Name</td>
     			<td >
                <input name="terminalName" id="terminalName" size="15" value="<%=terminalName%>" onkeydown="getTerminalNumber(this.value)" />
				<dojo:autoComplete formId="retailAddAirRatesPopupForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=RETAIL_ADD_AIR_RATES_POPUP&from=0" />   			
    			
    		</tr>
    		<tr class="textlabels">	
    			<td >Dest Port&nbsp;</td>
    			
    			<td >
    			<input name="destSheduleNumber" id="destSheduleNumber" size="15" maxlength="5" value="<%=destSheduleNumber%>" onkeydown="getdestPort(this.value)" />
				<dojo:autoComplete formId="retailAddAirRatesPopupForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=RETAIL_ADD_AIR_RATES_POPUP&from=1" />
    			<td><span class="textlabels">Dest Port Name</span></td>
    			<td >
    			<input name="destAirportname" size="15" id="destAirportname" value="<%=destAirportname%>" onkeydown="getDestinationPortName(this.value)"  />
				<dojo:autoComplete formId="retailAddAirRatesPopupForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=RETAIL_ADD_AIR_RATES_POPUP&from=1" />
    			</td>
    		</tr>
    		<tr class="textlabels">	
    			<td>Com Code</td>
    			<td ><input name="comCode"  id="comCode" size="15"  onkeydown="getCodeDesc(this.value)" maxlength="6" value="<%=comCode%>"/>
    			<dojo:autoComplete formId="retailAddAirRatesPopupForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=RETAIL_ADD_AIR_RATES_POPUP"/>
    			</td>
    			
    			<td >Com Description </td>
    			<td><input name="comDescription" id="comDescription" value="<%=comDesc%>" size="15" onkeydown="getCode(this.value)"/>
    				<dojo:autoComplete formId="retailAddAirRatesPopupForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=RETAIL_ADD_AIR_RATES_POPUP"/></td>
    		</tr>
    		<tr style="padding-top:10px">
    			<td>&nbsp;</td>
    			<td  align="right"  style="padding-left:90px">
    		   		<input type="button" value="Submit" id="search" class="buttonStyleNew" onclick="searchform()"/>
    		 	</td>
    		</tr>	
    		</table>
    		<html:hidden property="buttonValue" styleId="buttonValue" />
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

