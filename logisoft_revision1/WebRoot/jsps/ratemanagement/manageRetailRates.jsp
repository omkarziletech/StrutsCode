<%@ page language="java"%>
<%@ page language="java"
	import="com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.GenericCode,
	com.gp.cong.logisoft.domain.RetailCommodityCharges,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.RetailStandardCharges,com.gp.cong.logisoft.beans.AirRatesBean,java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.RetailWeightRangesRates,com.gp.cong.logisoft.domain.RetailStandardCharges1,com.gp.cong.logisoft.hibernate.dao.StandardChargesDAO"%>
<jsp:directive.page import="java.text.DecimalFormat" />
<jsp:directive.page import="com.gp.cong.logisoft.domain.RetailOceanDetailsRates"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../ratemanagement/CalculateBottomLineRates.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	DBUtil dbUtil = new DBUtil();
	List airStandardComm = new ArrayList();
	List noncommon = new ArrayList();
	String com = "";
	String noncom = "";
	String procced = "";
	String Port = "";
	String coll = "";
	List retailRatesList = null;
	String noncomCaps = "Ocean Freight Rates";
	String comCaps = "Common Accessorial Charges";
	if (session.getAttribute("reatilcommonList") != null) {
		comCaps = (String) session.getAttribute("reatilcommonList");
	}
	if (session.getAttribute("RetailRateCaption") != null) {
		noncomCaps = (String) session.getAttribute("RetailRateCaption");
	}
	String defaultRate = "";
	Map RetailsStdMap = new HashMap();
	AirRatesBean airRatesBean = new AirRatesBean();

	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat dateFormat1 = new SimpleDateFormat("hh:mm aaa");
	if (session.getAttribute("serachretaildefaultRate") != null) {
		defaultRate = (String) session
		.getAttribute("serachretaildefaultRate");
	}
	if (defaultRate != null && defaultRate.equals("E")) {
		Port = "English";
		airRatesBean.setMatch("starts");
	} else if (defaultRate != null && defaultRate.equals("M")) {
		Port = "Metric";
		airRatesBean.setMatch("starts");
	}

	if (session.getAttribute("collaps") != null) {
		coll = (String) session.getAttribute("collaps");
	}
	if (session.getAttribute("rerecords") != null) {
		procced = (String) session.getAttribute("rerecords");
	}
	if (session.getAttribute("trade") != null) {
		session.removeAttribute("trade");
	}
	if (session.getAttribute("recommonList") != null) {
		airStandardComm = (List) session.getAttribute("recommonList");
	}
	if (session.getAttribute("renoncommonList") != null) {
		retailRatesList = (List) session
		.getAttribute("renoncommonList");
	}
	if (request.getAttribute("noncommon") != null) {
		noncom = (String) request.getAttribute("noncommon");
	}
	if (request.getAttribute("common") != null) {
		com = (String) request.getAttribute("common");
	}
	String buttonValue = "load";
	String msg = "";
	String message = "";
	String modify = "";

	String terminalNumber = "";
	String terminalName = "";
	String destSheduleNumber = "";
	String destAirportname = "";
	String comCode = "";
	String comDesc = "";
	RetailStandardCharges retailRatesObj1 = null;
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat per = new DecimalFormat("0.000");

	if (session.getAttribute("retmessage") != null) {
		message = (String) session.getAttribute("retmessage");
	}

	if (request.getAttribute("message") != null) {
		message = (String) request.getAttribute("message");
	}

	airRatesBean.setCommon("");

	if (session.getAttribute("reairRatesBean") != null) {
		airRatesBean = (AirRatesBean) session
		.getAttribute("reairRatesBean");
	}
	airRatesBean.setMatch("match");
	request.setAttribute("airRatesBean", airRatesBean);

	if (session.getAttribute("retailmanage") != null) {
		retailRatesObj1 = (RetailStandardCharges) session
		.getAttribute("retailmanage");
		
			terminalNumber = retailRatesObj1.getOrgTerminal();
			terminalName = retailRatesObj1.getOrgTerminalName();
			destSheduleNumber = retailRatesObj1.getDestPort();
			destAirportname = retailRatesObj1.getDestPortName();
			comCode = retailRatesObj1.getComCode();
			comDesc = retailRatesObj1.getDestPortName();
		
	}

	if (request.getParameter("modify") != null) {
		modify = (String) request.getParameter("modify");
		session.setAttribute("modifyforretailRates", modify);
	} else {
		modify = (String) session.getAttribute("modifyforretailRates");
	}

	if (request.getAttribute("buttonValue") != null) {
		buttonValue = (String) request.getAttribute("buttonValue");
	}
	if (request.getParameter("programid") != null) {
		String programId = request.getParameter("programid");
		session.setAttribute("processinfoforretailRates", programId);
	}
	String editPath = path + "/manageRetailRates.do";

	//--------For BOTTOMLINE RATES------
	if (request.getAttribute("btl") != null) {
%>

<script language="javascript" type="text/javascript">
	
	 mywindow=window.open("<%=path%>/jsps/ratemanagement/RetailBottomLine.jsp?flaterate=koko","","width=400,height=200");
 	 mywindow.moveTo(200,180);		
	
 </script>
<%
}
%>
<html>
	<head>

		<title>JSP for ManageRetailRatesForm form</title>
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
		var oceanFreightRatesVerticalSlide;
		var commonChargesVerticalSlide;
		window.addEvent('domready', function() {
				oceanFreightRatesVerticalSlide = new Fx.Slide('oceanFrieght_Rates_vertical_slide', {mode: 'vertical'});
				commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
				oceanFreightRatesVerticalSlide.toggle();
				commonChargesVerticalSlide.toggle();
				
				$('oceanFrieghtRatesToggle').addEvent('click', function(e){
					oceanFreightRatesVerticalSlide.toggle();
				});
				$('commonChargesToggle').addEvent('click', function(e){
					commonChargesVerticalSlide.toggle();
				});				

			});
		
		 var newwindow = '';
           function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/ratemanagement/retailAddAirRatesPopup.jsp?retailsrates="+"add";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/ratemanagement/retailAddAirRatesPopup.jsp?retailsrates="+"add","","width=600,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
           } 
	
	function searchform()
	{
//    alert(document.manageRetailRatesForm.terminalNumber.id);
		
		if(document.manageRetailRatesForm.terminalNumber.value=="" || document.manageRetailRatesForm.destSheduleNumber.value=="")
		{
			alert("Please select Original Terminal and Destination Airport");
			return;
		}
		document.manageRetailRatesForm.buttonValue.value="search";
		document.manageRetailRatesForm.search.value="get";
		document.manageRetailRatesForm.submit();
	}
	function searchallform(val1)
	{	
		/*if(document.manageRetailRatesForm.terminalNumber.value=="" || document.manageRetailRatesForm.destSheduleNumber.value=="")
		{
			alert("Please select Original Terminal and Destination Airport");
			return;
		}*/
		
		document.manageRetailRatesForm.buttonValue.value="searchall";
		document.manageRetailRatesForm.search.value=val1;
		document.manageRetailRatesForm.submit();
	}
	function searchStarndardform(val1)
	{	
		
		
		document.manageRetailRatesForm.buttonValue.value="searchStarndard";
		document.manageRetailRatesForm.search.value=val1;
		document.manageRetailRatesForm.submit();
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
		document.manageRetailRatesForm.buttonValue.value="search";
		document.manageRetailRatesForm.submit();
		return false;
	}
	
	function openwindow()
	 {
	  document.manageRetailRatesForm.buttonValue.value="paramid";
	  document.manageRetailRatesForm.submit();
     }
	
	function disabled(val)
   	{
		
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
  		  var tables1 = document.getElementById('nonretailtable');
  		 if(tables1!=null)
  		 {
  	 	 //displaytagcolor();
   		 //initRowHighlightingcom();
   		 setWarehouseStylecom();
   		 }
  		 var tables = document.getElementById('retailtable');
  		 if(tables!=null)
  		 {
  	 	 //displaytagcolor();
   		 //initRowHighlighting();
   		 setWarehouseStyle();
   		 }
   	}
    function displaytagcolor()
	{
		 
		 var datatableobj = document.getElementById('retailtable');
		if(datatableobj!=null)
		{
		 for(i=0; i<datatableobj.rows.length; i++)
				{
					var tablerowobj = datatableobj.rows[i];
					
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
					//alert("tablerowobj.cells[7].innerHTML");
					if(tablerowobj.cells[1].innerHTML=="c")
					{
					//alert(tablerowobj.rows[i]);
					//tablerowobj.rows[i].style.color='red';
						tablerowobj.bgColor='pink';
					}
					if(tablerowobj.cells[0].innerHTML=="acc")
					{
					
					tablerowobj.cells[0].innerHTML="*";
					tablerowobj.cells[0].style.color='#FF3300';
						
					}
		 	 	}
		 	 	}
		 	 
		 	 	var datatableobj1 = document.getElementById('nonretailtable');
		 	 	
		if(datatableobj1!=null)
		{
			 for(i=0; i<datatableobj1.rows.length; i++)
				{
					var tablerowobj = datatableobj1.rows[i];
					
					if(i%2==0)
					{
						tablerowobj.bgColor='#FFFFFF';
					}
					else
					{
						tablerowobj.bgColor='#E6F2FF';
					}
					if(tablerowobj.cells[0].innerHTML=="com")
					{
//					tablerowobj.bgColor='#FFB7DB';
					
					}
					else if(tablerowobj.cells[0].innerHTML=="a")
					{
					tablerowobj.bgColor='pink';
					
					}
		 	 	}
		 	 }
		
	}
	function initRowHighlighting()
  	{
			if (!document.getElementById('retailtable')){ return; }
			 var tables = document.getElementById('retailtable');
			attachRowMouseEvents(tables.rows);
		
	}
	function initRowHighlightingcom()
  	{
			if (!document.getElementById('nonretailtable')){ return; }
			 var tables = document.getElementById('nonretailtable');
			attachRowMouseEvents(tables.rows);
		
	}
	function attachRowMouseEvents(rows)
	{
		for(var i =1; i < rows.length; i++)
		{
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
	function setWarehouseStyle()
	{

		if(document.manageRetailRatesForm.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('retailtable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		}
		if(document.manageRetailRatesForm.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('retailtable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('retailtable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('retailtable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
	 function setWarehouseStylecom()
	{

		if(document.manageRetailRatesForm.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('nonretailtable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		}
		if(document.manageRetailRatesForm.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('nonretailtable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('nonretailtable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('nonretailtable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
 function titleLetter(ev)
	{
		if(event.keyCode==9)
		{
		document.manageRetailRatesForm.terminalName.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
 function getDestination(ev)
	{
		if(event.keyCode==9)
		{
		document.manageRetailRatesForm.destAirportname.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCode(ev)
	{
		if(event.keyCode==9)
		{

		document.manageRetailRatesForm.comDescription.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function titleLetterName(ev)
	{
		if(event.keyCode==9)
		{
		document.manageRetailRatesForm.terminalNumber.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getDestinationName(ev)
	{
		if(event.keyCode==9)
		{
		document.manageRetailRatesForm.destSheduleNumber.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeName(ev)
	{
		if(event.keyCode==9)
		{		
		document.manageRetailRatesForm.comCode.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	 function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.manageRetailRatesForm.terminalName.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
 function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.manageRetailRatesForm.destAirportname.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.manageRetailRatesForm.comDescription.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.manageRetailRatesForm.terminalNumber.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.manageRetailRatesForm.destSheduleNumber.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.manageRetailRatesForm.comCode.value="";
		document.manageRetailRatesForm.buttonValue.value="popupsearch";
		document.manageRetailRatesForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}	
 function ClearScreen()
	 {
	 document.manageRetailRatesForm.buttonValue.value="clear";
	 document.manageRetailRatesForm.submit();
	 
	 }
	 function openwindowDoc()
	 {
	    
	 	document.manageRetailRatesForm.buttonValue.value="paramidDoc";
	    document.manageRetailRatesForm.submit();
	 	
	}
		function getAddRetailRate(){
			window.location.href="<%=path%>/jsps/ratemanagement/RetailRatesFrame.jsp";
		 }
		 
		 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.manageRetailRatesForm.terminalNumber.value;
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
    	  		document.manageRetailRatesForm.terminalName.value=orgTrmName;
    	    	document.getElementById("terminalNumber").value=orgTrm;
    	    }else{
    	    	var params = new Array();
			  	params['requestFor'] = "OrgTerminalNumber";
			  	params['terminalName'] = document.manageRetailRatesForm.terminalName.value;
			  	termName = document.manageRetailRatesForm.terminalName.value;
			  	var bindArgs = {
	  		  		url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
	  		  		error: function(type, data, evt){alert("error");},
	 		  		mimetype: "text/json",
	  				content: params
				};
				
				document.manageRetailRatesForm.terminalName.value=document.manageRetailRatesForm.terminalName.value;
				var req = dojo.io.bind(bindArgs);
				dojo.event.connect(req, "load", this, "populateTerminalNumber");
			}
			
		}
	}
	function populateTerminalNumber(type, data, evt) {
  		if(data){
   			document.getElementById("terminalNumber").value=data.terminalNumber;
   			document.manageRetailRatesForm.terminalName.value=termName;
   		}
	}
  function getdestPort(ev){ 
	 	document.getElementById("destAirportname").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.manageRetailRatesForm.destSheduleNumber.value;
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
				document.manageRetailRatesForm.destAirportname.value = destTrmName;
				document.getElementById("destSheduleNumber").value= destTrm;
			}else{
				var params = new Array();
				destPortName = document.manageRetailRatesForm.destAirportname.value;
				params['requestFor'] = "OrgTerminalNumber";
				params['terminalName'] = document.manageRetailRatesForm.destAirportname.value;
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
   			document.manageRetailRatesForm.destAirportname.value = destPortName;
   		}
	}	 
		function getCodeDesc(ev){ 
		document.getElementById("comDescription").value="";
    	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "CommodityDetails";
			params['code'] = document.manageRetailRatesForm.comCode.value;
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
			params['codeDesc'] = document.manageRetailRatesForm.comDescription.value;
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
	<%
	if (coll != null && !coll.equals("")) {
	%>
	<body class="whitebackgrnd" onLoad="">
		<%
		} else {
		%>
	
	<body class="whitebackgrnd">
		<%
		}
		%>
	
	<html:form action="/manageRetailRates" name="manageRetailRatesForm"	type="com.gp.cong.logisoft.struts.ratemangement.form.ManageRetailRatesForm" scope="request">
	<%if (coll != null && !coll.equals("")) {%>
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
			<tr class="tableHeadingNew"><td>LCL Retail Rates</td>
				<td align="right"><input type="button" value="Go Back" class="buttonStyleNew" id="cancel" onclick="ClearScreen()" />
				   <input type="button" value="AddNew" class="buttonStyleNew" id="addnew"
					onclick="return GB_show('RetailRates', '<%=path%>/jsps/ratemanagement/retailAddAirRatesPopup.jsp?retailsrates='+'add',200,600)" />
				</td>
			</tr>
			<td colspan="2">
			<font color="blue" size="4"><%=message%></font>
			<table width="100%" border="0" cellpadding="0" cellspacing="2" class="displaytagstyle">
				<thead>
				<tr class="textlabels">
				     <td><b> Org Trm No</td>
					 <td><b> Org Trm Name</td>
					 <td><b> Destination No</td>
					 <td><b> Destination Name</td>
				</tr>
				</thead>
				<tr class="even">
					 <td><%=terminalNumber%></td>
					 <td><%=terminalName%></td>
					 <td><%=destSheduleNumber%></td>
					 <td><%=destAirportname%></td>
					 <td><%=Port%></td>
			    </tr>
		   
		 <%	}else {	%>
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px;padding-top:10px;">
			    <tr class="tableHeadingNew"> Search LCL Retail Rates</tr>
				<tr>
					<td>
						<table width="100%" border="0">
							<tr class="style2">
								<td>Org Trm&nbsp;</td>
							</tr>
						</table>
					</td>
					<td><input name="terminalNumber" id="terminalNumber" size="30" onkeydown="getTerminalName(this.value)"
						 maxlength="2" value="<%=terminalNumber%>" />
						<dojo:autoComplete formId="manageRetailRatesForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCode.jsp?tabName=MANAGE_RETAIL_RATES&from=0" />
					</td>
					<td><span class="textlabels">Org Trm Name</span></td>
					<td><input name="terminalName" id="terminalName" size="30" value="<%=terminalName%>"
						onkeydown="getTerminalNumber(this.value)" />
						<dojo:autoComplete formId="manageRetailRatesForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=MANAGE_RETAIL_RATES&from=0" />
				    </td>
			   </tr>
			   <tr class="textlabels">
			        <td class="textlabels">
							<table width="100%" border="0">
								<tr class="style2">
								     <td>Dest Port&nbsp;<!--<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/datareference/SearchPierCode.jsp?button='+'retailsearchpairport&percode='+document.manageRetailRates.destSheduleNumber.value,'windows')">-->
									 </td>
								</tr>
							</table>
					</td>
					<td><input name="destSheduleNumber" id="destSheduleNumber" size="30" maxlength="5" value="<%=destSheduleNumber%>"
						 onkeydown="getdestPort(this.value)" />
						<dojo:autoComplete formId="manageRetailRatesForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=MANAGE_RETAIL_RATES&from=1" />
				    </td>
				    <td>Dest PortName</td>
					<td><input name="destAirportname" size="30" id="destAirportname" value="<%=destAirportname%>"
						onkeydown="getDestinationPortName(this.value)"  />
						<dojo:autoComplete formId="manageRetailRatesForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=MANAGE_RETAIL_RATES&from=1" />
					</td>
			   </tr>
			   <tr class="textlabels">
						<td>
							<table width="100%" border="0">
								<tr class="style2">
								     <td>Com Code&nbsp;<!--<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/searchComCode.jsp?button='+'retailsearchpcomcode&comcode='+document.manageRetailRates.comCode.value,'windows')">-->
									 </td>
								</tr>
							 </table> 
						</td>
						<td><input name="comCode" id="comCode" maxlength="6" size="30" onkeydown="getCodeDesc(this.value)"
						 value="<%=comCode%>" />
							<dojo:autoComplete formId="manageRetailRatesForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=MANAGE_RETAIL_RATES" />
						</td>
						<td class="textlabels">Com Description</td>
						<td><input name="comDescription" id="comDescription" size="30" value="<%=comDesc%>" onkeydown="getCode(this.value)"
							 />
							<dojo:autoComplete formId="manageRetailRatesForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=MANAGE_RETAIL_RATES" />
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
			   </tr>
			   <tr>
			   			<td valign="top" colspan="5" align="center" style="padding-top:10px;">
			   			   <input type="button" value="Search" onclick="searchform()" class="buttonStyleNew" />
			   			   <input type="button" value="Clear" onclick="ClearScreen()" class="buttonStyleNew" id="cancel" />
						</td>
			  </tr>
  		<%}%>
		</table>
	</table>
	</td>
 </table>
	
    <%if (coll != null && !coll.equals("")) {%>
	<br/>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
	<tr class="tableHeadingNew"><td><%=noncomCaps%></td>
		<td align="right"><a id="oceanFrieghtRatesToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a></td>	
	</tr>
	<tr>
		<td align="left">
			<div id="oceanFrieght_Rates_vertical_slide">
			<div id="divtablesty1" class="scrolldisplaytable">
			<table width="60%" border="0" cellpadding="0" cellspacing="0">
				<%int i = 0;%>
				<%if (retailRatesList != null) {%>
			<display:table name="<%=retailRatesList%>"pagesize="<%=pageSize%>" defaultsort="2" class="displaytagstyle" id="retailtable" sort="list">
			
			<display:setProperty name="paging.banner.some_items_found">
				<span class="pagebanner">
											 </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    			<span class="pagebanner"><font color="blue">{0}</font>
											Retail Rates Displayed,For more Data click on Page Numbers. <br></span>
			</display:setProperty>
			<display:setProperty name="paging.banner.one_item_found">
										<span class="pagebanner"> 
											 </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    			<span class="pagebanner">  One {0} displayed. Page Number</span>
			</display:setProperty>
			<display:setProperty name="paging.banner.all_items_found">
				<span class="pagebanner"> Page Number
											</span>
    			<span class="pagebanner"> </span>
    		</display:setProperty>	
			<display:setProperty name="basic.msg.empty_list">
										<span class="pagebanner"> No Records Found.</span>
			</display:setProperty>
			<display:setProperty name="paging.banner.placement" value="bottom" />
			<display:setProperty name="paging.banner.item_name" value="Retail" /> 
			<display:setProperty name="paging.banner.items_name" value="Retails" />
			 <display:footer>
			  	<tr>
			  		
			  		<td colspan="8"><font color="red" size=2>* </font> Commodity Specific Accesorial Charges</td>
			  	<tr>
			  </display:footer>

		<%
				String comCodeDisplay = "", comDescDisplay = "", ofMinAmt = "", ratekgs = "", 
				rateCBM = "", user = "", ratelb = "", rateCFT = "", englishMinAmt = "", sizeAOF = "", 
				sizeATT = "", sizeBOF = "", sizeBTT = "", sizeCOF = "", sizeCTT = "", sizeDOF = "", 
				sizeDTT = "", measureType = "", nonuname = "", effdate = "", amount = "", exist = "",Oceannonuname="", 
				getchange = "", link = "", kGAmount = "", cBAmount = "", lBsAmount = "", cFtAmount = "",Oceaneffdate="";
				Map map = new HashMap();

				if (retailRatesList != null
						&& retailRatesList.size() > 0) {
					RetailStandardCharges manageRetailRates = (RetailStandardCharges) retailRatesList
					.get(i);
					if (manageRetailRates.getComCode() != null) {
					GenericCodeDAO genericCodeDAO =new GenericCodeDAO();
					
					List comList = genericCodeDAO.findForGenericCode(manageRetailRates.getComCode());
					if (comList != null && comList.size() > 0) {
						GenericCode genObj = (GenericCode) comList.get(0);
						comCodeDisplay = genObj.getCode();
						comDescDisplay = genObj.getCodedesc();
					}
					}
					if (session.getAttribute("getchangedcolore") != null) {
						RetailStandardCharges manageRetailRates1 = (RetailStandardCharges) session
						.getAttribute("getchangedcolore");
						if (manageRetailRates1.getComCode() != null
						&& manageRetailRates.getComCode() != null
						&& manageRetailRates1.getComCode().equals(manageRetailRates.getComCode()
											)) {
								getchange = "c";
									}
	
								}
								Set set = new HashSet();
								if (manageRetailRates.getRetailCommoditySet() != null) {
									set = manageRetailRates.getRetailCommoditySet();
									if (set != null && set.size() < 1) {
								      //exist="com";
									} else {
								exist = "*";
									}
								}
								if (manageRetailRates.getRetailCommoditySet() != null) {
									Iterator iter = manageRetailRates
									.getRetailCommoditySet().iterator();
									while (iter.hasNext()) {
								RetailCommodityCharges res1 = (RetailCommodityCharges) iter
										.next();
								if (res1.getEffectiveDate() != null) {
									effdate = dateFormat.format(res1
									.getEffectiveDate());
								}
								if (res1.getWhoChanged() != null) {
									nonuname = res1.getWhoChanged();
								}
									}
	
								}
								
								if (manageRetailRates.getRetailWeightRangeSet() != null) {
									Iterator iter = manageRetailRates
									.getRetailWeightRangeSet().iterator();
									while (iter.hasNext()) {
								RetailOceanDetailsRates retailRate = (RetailOceanDetailsRates) iter
										.next();
								if (retailRate.getMetric1000kg() != null) {
									ratekgs = df.format(retailRate
									.getMetric1000kg());
								} else {
									ratekgs = ("0.00");
								}
							if (retailRate.getEffectiveDate() != null) {
									Oceaneffdate = dateFormat.format(retailRate
									.getEffectiveDate());
								}
								if (retailRate.getWhoChanged() != null) {
									Oceannonuname = retailRate.getWhoChanged();
								}
								if (retailRate.getMetricCmb() != null) {
									rateCBM = df.format(retailRate
									.getMetricCmb());
								} else {
									rateCBM = ("0.00");
								}
								if (retailRate.getMetricMinAmt() != null) {
									ofMinAmt = df.format(retailRate
									.getMetricMinAmt());
								} else {
									ofMinAmt = ("0.00");
								}
								if (retailRate.getEnglish1000lb() != null) {
									ratelb = df.format(retailRate
									.getEnglish1000lb());
								} else {
									ratelb = ("0.00");
								}
								if (retailRate.getEnglishLbs() != null) {
									rateCFT = df.format(retailRate
									.getEnglishLbs());
								} else {
									rateCFT = ("0.00");
								}
								if (retailRate.getEnglishMinAmt() != null) {
									englishMinAmt = df.format(retailRate
									.getEnglishMinAmt());
								} else {
									englishMinAmt = ("0.00");
								}
								if (retailRate.getAOcean() != null) {
									sizeAOF = df.format(retailRate
									.getAOcean());
								} else {
									sizeAOF = ("0.00");
								}
								if (retailRate.getATt() != null) {
									sizeATT = df.format(retailRate
									.getATt());
								} else {
									sizeATT = ("0.00");
								}
								if (retailRate.getBOcean() != null) {
									sizeBOF = df.format(retailRate
									.getBOcean());
								} else {
									sizeBOF = ("0.00");
								}
								if (retailRate.getBTt() != null) {
									sizeBTT = df.format(retailRate
									.getBTt());
								} else {
									sizeBTT = ("0.00");
								}
								if (retailRate.getCOcean() != null) {
									sizeCOF = df.format(retailRate
									.getCOcean());
								} else {
									sizeCTT = ("0.00");
								}
								if (retailRate.getCTt() != null) {
									sizeCTT = df.format(retailRate
									.getCTt());
								} else {
									sizeCTT = ("0.00");
								}
								if (retailRate.getDOcean() != null) {
									sizeDOF = df.format(retailRate
									.getDOcean());
								} else {
									sizeDOF = ("0.00");
								}
								if (retailRate.getDTt() != null) {
									sizeDTT = df.format(retailRate
									.getDTt());
								} else {
									sizeDTT = ("0.00");
								}
								if (retailRate.getMeasureType() != null) {
									measureType = retailRate
									.getMeasureType();
								}
								if (Port != null && Port.equals("Metric")) {
									map = getBottomLineRates(manageRetailRates
									.getId());
									if(map!=null){
								
									kGAmount=map.get("kgs").toString();
									cBAmount=map.get("cbm").toString();
									}
								amount =  rateCBM + "/" +ratekgs;
							} else if (Port != null
									&& Port.equals("English")) {
									map = getBottomLineRates(manageRetailRates
									.getId());
										if(map!=null){
									lBsAmount=(String)map.get("lbs");
									cFtAmount=(String)map.get("cft");
									}
								amount = rateCFT + "/" + ratelb;
								}
									}
								}
								link = editPath + "?param="
								+ manageRetailRates.getId();
							}
					%>
			<display:column style="color:red"><%=exist%></display:column>
			<display:column title="Comm#" sortable="true" href="<%=editPath%>" paramId="paramid" paramProperty="id">
				<%=comCodeDisplay%></display:column>
			<display:column sortable="true" title="Comm# Desc"><%=comDescDisplay%></display:column>
	<%if (Port != null && Port.equals("English")) {%>
			<display:column sortable="true" title="CFT/Rate/100LB/"><%=amount%></display:column>
	<%} else if (Port != null && Port.equals("Metric")) {%>
			<display:column sortable="true" title="CBM/Rate/1000kGS/"><%=amount%></display:column>
	<%}%>
	<%//}%>
			<display:column title="MinAmt" sortable="true"><%=ofMinAmt%></display:column>
			<display:column title="Effective date" sortable="true"><%=Oceaneffdate%></display:column>
			<display:column sortable="true" title="&nbsp;&nbsp; UserName ">&nbsp;&nbsp;<%=Oceannonuname%></display:column>
	<%if (Port != null && Port.equals("English")) {%>
			<display:column title="BottomLineRate">
			<span onmouseover="tooltip.show('<strong>Rates/lbs</strong><%=lBsAmount  %><br/><strong>Rates/cft</strong><%=cFtAmount%> ',null,event);" 
				onmouseout="tooltip.hide();"><%="BL Rates"%></span> 
			</display:column>
	<%} else if (Port != null && Port.equals("Metric")) {%>
			<display:column title="BottomLineRate">
			<span onmouseover="tooltip.show('<strong>Rates/1000KG</strong> <%=kGAmount %><br/><strong>Rates/Cbm</strong><%=cBAmount %> ',null,event);" 
				  onmouseout="tooltip.hide();"><%="BL Rates"%></span> 
	        </display:column>
	<%}%>
			<display:column><span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);"
				onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" border="0"
				onclick="window.location.href ='<%=link%>'" /></span></display:column>
	<%i++;%>
	
	</display:table>
	<%}%>
	</table>
	</div>
	</div>
	</td>
	</tr>
	
</table>
<br>
<table width="100%">
<tr class="tabHead"><td align="center">Common Accessorial Charges</td></tr>
</table>
<br>
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew">
				<tr class="tableHeadingNew"><td><%=comCaps%></td>
									<%
									int k = 0;
									%>
									<td align="right">
										<%
											//if(airRatesBean.getCommon()!=null && comCode!=null && (airRatesBean.getNoncommon()!=null && airRatesBean.getNoncommon().equals("noncommon")) || comCode.equals("000000")||(com!=null && com.equals("common"))||(procced!=null && !procced.equals("")))
											// { 
											String set = (String) session.getAttribute("RetailRateCaption");
											String type = null;
											if (set != null) {
												int ty = set.indexOf("Match");

												if (ty > -1) {
													type = "match";
												} else {
													type = "starts";
												}
											}
											String all = (String) session.getAttribute("reatilcommonList");
											String getAll = null;
											if (all != null) {
												int ty = all.indexOf("All");
												if (ty > -1) {
													getAll = "all";
												}

											}
											if (session.getAttribute("reatilcommonList") != null) {

												String getresult = (String) session
												.getAttribute("reatilcommonList");
												if (getresult != null && getresult.equals("No Accessorial Add")) {
										%>

										<input type="button" value="Add Accessorial"
											class="buttonStyleNew" style="width:110px"
											onclick="openwindow()" />

										<%
													} else {
													if (getAll != null) {
										%>

										<input  type="button" value="ShowStandard-Only"
											class="buttonStyleNew" id="searchall" style="width:110px"
											onclick="searchStarndardform('<%=type%>')" />


										<%
										} else {
										%>

										<input   type="button" value="Show All" class="buttonStyleNew"
											onclick="searchallform('<%=type%>')" id="searchall" />

										<%
										}
										%>
										<input  type="button" value="Add Accessorial"
											class="buttonStyleNew" onclick="openwindow()"
											style="width:110px" />

										<input  type="button" value="Dom/Com/Mis" class="buttonStyleNew"
											onclick="openwindowDoc()" style="width:110px" />

									</td>

									<%
									}}
									%>
													   <td align="right"><a id="commonChargesToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a> </td>
				</tr>

				<tr>
					<td colspan="2">
					    <div id="common_Charges_vertical_slide">
						<div id="divtablesty1" class="scrolldisplaytable">
							<table border="0" width=100% cellpadding="0" cellspacing="0">
								
								

									<display:table name="<%=airStandardComm%>"
										pagesize="<%=pageSize%>" defaultsort="1"
										class="displaytagstyle" id="nonretailtable" sort="list">

										<display:setProperty name="paging.banner.some_items_found">
											<span class="pagebanner"> <font color="blue">{0}</font>
												Retail Rates Displayed,For more Data click on Page Numbers.
												<br> </span>
										</display:setProperty>

										<display:setProperty name="paging.banner.one_item_found">
											<span class="pagebanner"> One {0} displayed. Page
												Number </span>
										</display:setProperty>
										<display:setProperty name="paging.banner.all_items_found">
											<span class="pagebanner"> {0} {1} Displayed, Page
												Number </span>
										</display:setProperty>
										<display:setProperty name="basic.msg.empty_list">
											<span class="pagebanner"> No Records Found. </span>
										</display:setProperty>
										<display:setProperty name="paging.banner.placement"
											value="bottom" />
										<display:setProperty name="paging.banner.item_name"
											value="Retail" />
										<display:setProperty name="paging.banner.items_name"
											value="Retails" />
										<%
					String comCodeDisplay = "",comDescDisplay = "",deleteStandard="",chargeCode = "",chargeType = "";
					String standard = "",amtPerCft = "", amtlbs = "", amtPerCbm = "",amtPerKg = "",amount = "",percentage = "";
					String minAmt = "",insuranceRate = "",insuranceAmt = "",asFrfgted = "",effdate = "",uname = "";
					String pramid = "",cost_cbm = "",code_desc = "",chr_type_code = "",temPath = "",time = "";
					String moreinfo = "",chnaged = "";
													if (airStandardComm != null && airStandardComm.size() > 0) {
												RetailStandardCharges1 retailRate = (RetailStandardCharges1) airStandardComm
														.get(k);//child record		

												RetailStandardCharges parentObject = new RetailStandardCharges();//parent record
												if (retailRate.getStandardId() != null) {
													StandardChargesDAO StandardCharges = new StandardChargesDAO();
													parentObject = StandardCharges.findById1(retailRate
													.getStandardId());
													if (retailRate != null
													&& RetailsStdMap.containsKey(retailRate
															.getStandardId()) == false) {
														RetailsStdMap.put(retailRate.getStandardId(),
														retailRate.getStandardId());
														if (parentObject.getComCode() != null) {
													comCodeDisplay = parentObject.getComCode();															
													comDescDisplay = parentObject.getComCode();
															
														}
														if (parentObject.getCostCbm() != null) {
													cost_cbm = df.format(parentObject
															.getCostCbm());
														}
														pramid = retailRate.getStandardId().toString();
													}
												}

												if (retailRate.getChargeCode() != null) {
													chargeCode = retailRate.getChargeCode().getCode();
													code_desc = retailRate.getChargeCode()
													.getCodedesc();
												}
												if (session.getAttribute("getChangedagss") != null) {
													RetailStandardCharges1 rr = (RetailStandardCharges1) session
													.getAttribute("getChangedagss");
													if (rr.getChargeCode() != null
													&& retailRate.getChargeCode() != null
													&& rr.getChargeCode().getId().equals(
															retailRate.getChargeCode().getId())) {
														chnaged = "a";
													}
												}
												if (retailRate.getEffectiveDate() != null) {
													effdate = dateFormat.format(retailRate
													.getEffectiveDate());
													time = dateFormat1.format(retailRate
													.getEffectiveDate());
												}
												if (retailRate.getWhoChanged() != null) {
													uname = retailRate.getWhoChanged();
												}

												if (retailRate.getStandard() != null) {
													standard = retailRate.getStandard();
												}
												if (retailRate.getAmtPerCft() != null) {
													amtPerCft = df.format(retailRate.getAmtPerCft());
												}
												if (retailRate.getAmtPer100lbs() != null) {
													amtlbs = df.format(retailRate.getAmtPer100lbs());
												}
												if (retailRate.getAmtPerCbm() != null) {
													amtPerCbm = df.format(retailRate.getAmtPerCbm());
												}
												if (retailRate.getAmtPer1000kg() != null) {
													amtPerKg = df.format(retailRate.getAmtPer1000kg());
												}

												if (retailRate.getMinAmt() != null) {
													minAmt = df.format(retailRate.getMinAmt());
												}
												if (retailRate.getInsuranceRate() != null) {
													insuranceRate = df.format(retailRate
													.getInsuranceRate());
												}
												if (retailRate.getInsuranceAmt() != null) {
													insuranceAmt = df.format(retailRate
													.getInsuranceAmt());
												}
												if (retailRate.getAsFrfgted() != null) {
													asFrfgted = retailRate.getAsFrfgted();
												}

												if (retailRate.getChargeType() != null) {
													chargeType = retailRate.getChargeType()
													.getCodedesc();
													chr_type_code = retailRate.getChargeType()
													.getCode();
												}
												if (chargeType != null
														&& chargeType.equals("Flat rate")) {
													if (retailRate.getAmount() != null) {
														amount = df.format(retailRate.getAmount());
													}
												}
												if (chargeType != null
														&& chargeType.equals("Charge percent")) {
													if (retailRate.getPercentage() != null) {
														amount = per.format(retailRate.getPercentage());
													}
												}
												if (chargeType != null
														&& chargeType.equals("As Freighted")) {
													amount = insuranceAmt + "/" + insuranceRate;
												}
												if (chargeType != null
														&& chargeType.equals("Weight or measure")) {
													if (Port != null && Port.equals("Metric")) {
														amount = amtPerCbm + "/" + amtPerKg;
													} else if (Port != null && Port.equals("English")) {
														amount = amtPerCft + "/" + amtlbs;
													}
												}
												String iStr = String.valueOf(k);
												temPath = editPath + "?ind=" + iStr + "&paramid="
														+ retailRate.getStandardId();
												moreinfo = editPath + "?ind=" + iStr + "&param="
														+ retailRate.getStandardId();
												 deleteStandard = editPath + "?parentId=" + retailRate.getStandardId() + "&standardId="
														+ retailRate.getRetailStdId();	
													}
													
										%>
										<display:column style="width:1%;visibility:hidden">
											<%=chargeCode%>
										</display:column>
										<display:column sortable="true" title="Chrg Code">
											<a href="<%=temPath%>"><%=chargeCode%>
											</a>
										</display:column>
										<display:column sortable="true" title="Chrg_desc">
											<%=code_desc%>
										</display:column>
										<display:column sortable="true" title="Chrg Type">
											<%=chargeType%>
										</display:column>
										<display:column sortable="true" title="Std">
											<%=standard%>
										</display:column>
										<display:column sortable="true" title="Amount">
											<%=amount%>
										</display:column>
										<display:column sortable="true" title="MinAmt">
											<%=minAmt%>
										</display:column>

										<display:column sortable="true" title="EffDate">
											<%=effdate%>
										</display:column>
										<display:column sortable="true" title="Time">
											<%=time%>
										</display:column>
										<display:column sortable="true" title="User Name">
											<%=uname%>
										</display:column>

										<display:column>
											<span class="hotspot"
												onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);"
												onmouseout="tooltip.hide();"><img
													src="<%=path%>/img/icons/pubserv.gif" border="0"
													onclick="window.location.href ='<%=moreinfo%>'" />
											</span>
											<span class="hotspot"
												onmouseover="tooltip.show('<strong>Delete</strong>',null,event);"
												onmouseout="tooltip.hide();"><img
													src="<%=path%>/img/icons/delete.gif" border="0"
													onclick="window.location.href ='<%=deleteStandard%>'" />
											</span>
										</display:column>
										<%
										k++;
										%>
									</display:table>
									<%
									//}
									%>
							</table>
						</div>
						</div>
					</td>
				</tr>
			</table>
	<%}%>
			<html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>" />
			<html:hidden property="search" />
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

