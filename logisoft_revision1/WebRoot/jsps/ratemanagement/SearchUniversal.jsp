<%@ page language="java"  import="java.text.DateFormat,java.text.SimpleDateFormat,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.hibernate.dao.UniversalMasterDAO,com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.UniversalMaster,com.gp.cong.logisoft.domain.UniverseCommodityChrg,com.gp.cong.logisoft.domain.LCLColoadStandardCharges"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<jsp:directive.page import="java.text.DecimalFormat"/>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../ratemanagement/CalculateBottomLineRates.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	AirRatesBean airRatesBean=new AirRatesBean();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat per = new DecimalFormat("0.000");
	airRatesBean.setMatch("match");
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	DateFormat dateFormat1 = new SimpleDateFormat("hh:mm aaa");
	List noncommon=new ArrayList();
	DBUtil dbUtil=new DBUtil();
	UniversalMaster universalMaster=new UniversalMaster();
	String trmNum="";
	String trmNam="";
	String destAirPortNo="";
	String destAirPortName="";
	String comCode="";
	String comDesc="";
	String editPath="";
	String buttonValue="";
	String message="";
	String Port="";
	editPath=path+"/serachUniversal.do";
	String modify="";
	String coll="";
	if(session.getAttribute("universaldefaultRate")!=null)
	{
		Port=(String)session.getAttribute("universaldefaultRate");
		if(Port!=null && Port.equals("M"))
		{
		Port="Metric";
		}
		if(Port!=null && Port.equals("M"))
		{
		Port="Eglish";
		}
		
	}
	
	if(session.getAttribute("univerCollaps")!=null)
	{
		coll=(String)session.getAttribute("univerCollaps");
	}
	if(session.getAttribute("uninoncommonList")!=null)
	{
		noncommon=(List)session.getAttribute("uninoncommonList");
	}
	if(session.getAttribute("trade")!=null)
	{
		session.removeAttribute("trade");
	}
	if(request.getAttribute("LclbuttonValue")!=null)
	{
		buttonValue=(String)request.getAttribute("LclbuttonValue");
	}
	if(session.getAttribute("searchuniMaster")!=null)
	{
	universalMaster=(UniversalMaster)session.getAttribute("searchuniMaster");
	
	if(universalMaster.getOriginTerminal()!=null)
	{
	trmNum=universalMaster.getOriginTerminal().getTrmnum();
	trmNam=universalMaster.getOriginTerminal().getTerminalLocation();
	
	}
	if(universalMaster.getDestinationPort()!=null)
	{
	destAirPortNo=universalMaster.getDestinationPort().getShedulenumber();
	destAirPortName=universalMaster.getDestinationPort().getPortname();
	}
	if(universalMaster.getCommodityCode()!=null)
	{
	comCode=universalMaster.getCommodityCode().getCode();
	comDesc=universalMaster.getCommodityCode().getCodedesc();
	}
	}
	if(session.getAttribute("unimessage")!=null)
	{
		message=(String)session.getAttribute("unimessage");
	}
	if(request.getAttribute("message")!=null)
	{
		message=(String)request.getAttribute("message");
	}
	if(request.getParameter("modify")!= null)
	{
		 modify=(String)request.getParameter("modify");
		 session.setAttribute("modifyforlclcoloadRates",modify);
	}
	else
	{
 		modify=(String)session.getAttribute("modifyforlclcoloadRates");
	}
	if(request.getParameter("programid")!=null)
	{
		String programId=request.getParameter("programid");
	session.setAttribute("processinfoforuniversal",programId);
  	}
  	if(session.getAttribute("view")!=null)
  	{
		session.removeAttribute("view");
	}
	
	
	
	if(request.getAttribute("btl")!=null)
	{
%>
	<script language="javascript" type="text/javascript">
	 mywindow=window.open("<%=path%>/jsps/ratemanagement/UniversalBottomLine.jsp?flaterate=koko","","width=400,height=200");
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
		var airRatesVerticalSlide;
		var commonChargesVerticalSlide;
		window.addEvent('domready', function() {
				commonChargesVerticalSlide=new Fx.Slide('common_Charges_vertical_slide', {mode: 'vertical'});
				commonChargesVerticalSlide.toggle();
				$('commonChargesToggle').addEvent('click', function(e){
					commonChargesVerticalSlide.toggle();
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
		document.serachUniversalForm.buttonValue.value="search";
	    document.serachUniversalForm.submit();
		return false;
	}
	function searchform()
	{
	if(document.serachUniversalForm.terminalNumber.value=="" || document.serachUniversalForm.destSheduleNumber.value=="")
		{
			alert("Please select OriginTerminal or Dest Airport  ");
			return;
		}
		
	document.serachUniversalForm.buttonValue.value="search";
	document.serachUniversalForm.search.value="get";
	document.serachUniversalForm.submit();
	}
	
	var newwindow = '';
           function addform() {
           if (!newwindow.closed && newwindow.location)
		   {
             	newwindow.location.href = "<%=path%>/jsps/ratemanagement/universalAddPop.jsp";
           }
           else 
           {
         		newwindow=window.open("<%=path%>/jsps/ratemanagement/universalAddPop.jsp?universal="+"add","","width=700,height=150");
         		if (!newwindow.opener) newwindow.opener = self;
           }
           if (window.focus) {newwindow.focus()}
           return false;
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
  		 var tables1 = document.getElementById('commonTable');
		
  		 if(tables1!=null)
  		 {
  	 	 //displaytagcolor();
   		 //initRowHighlighting();
   		 setWarehouseStyle();
   		 }
  		 var tables = document.getElementById('noncommonTable');
  		 if(tables!=null)
  		 {
  	 	 //displaytagcolornon();
   		 //initRowHighlightingnon();
   		 setWarehouseStylenon();
   		 }
   	}
   	 function displaytagcolornon()
	{
		 	 	var datatableobj1 = document.getElementById('noncommonTable');
		
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
					
					}else if(tablerowobj.cells[0].innerHTML=="a")
					{
					tablerowobj.bgColor='pink';
					
					}
		 	 	}
	}
    function displaytagcolor()
	{
		 var datatableobj = document.getElementById('commonTable');
		
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
					if(tablerowobj.cells[0].innerHTML=="com")
					{
//					tablerowobj.bgColor='#FFB7DB';
					
					}
					else if(tablerowobj.cells[0].innerHTML=="acc")
					{
					tablerowobj.bgColor='#FF3300';
					
					}
		 	 	}
		 	 	
	}
	function initRowHighlighting()
  	{
			if (!document.getElementById('commonTable')){ return; }
			 var tables = document.getElementById('commonTable');
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
		function initRowHighlightingnon()
  	{
			if (!document.getElementById('noncommonTable')){ return; }
			 var tables = document.getElementById('noncommonTable');
			attachRowMouseEventsnon(tables.rows);
		
	}
	function attachRowMouseEventsnon(rows)
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
		function setWarehouseStylenon()
	{

		if(document.serachUniversalForm.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('noncommonTable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		} 
		if(document.serachUniversalForm.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('noncommonTable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('noncommonTable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('noncommonTable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
	function setWarehouseStyle()
	{

		if(document.serachUniversalForm.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('commonTable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		} 
		if(document.serachUniversalForm.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('commonTable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('commonTable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('commonTable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }

//---------------DOJO CODE-----------------------------------
	 
	 function getOriginalTerminalName(ev)
	{
		if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
				 params['requestFor'] = "OrgTerminalNameInCodes";
				 params['terminalNumber'] = document.serachUniversalForm.terminalNumber.value;
				  var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateTerminalNameDojo");
		//document.serachUniversalForm.buttonValue.value="popupsearch";
	   // document.serachUniversalForm.submit();
	  //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	  }
	}
	 function populateTerminalNameDojo(type, data, evt) {
		if(data){
		   		document.getElementById("terminalName").value=data.terminalNameInCodes
		 }
	 }
	 
	function getOriginalterminal(ev)
	{
		if(event.keyCode==9 || event.keyCode==13){
		var params = new Array();
				 params['requestFor'] = "OrgTerminalInCodes";
				 params['terminalName'] = document.serachUniversalForm.terminalName.value;
				
				  var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateOrgTerminalNumberDojo"); 
		
		 //document.serachUniversalForm.buttonValue.value="popupsearch";
	    // document.serachUniversalForm.submit();
	   //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=searchlclcoload&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	   }
	}
	function populateOrgTerminalNumberDojo(type, data, evt) {
		 if(data){
		   		   document.getElementById("terminalNumber").value=data.terminalNumberInCodes
		 }
	}
	
	
	function getDestinationPortName(ev)
	{
		if(event.keyCode==9){
		var params = new Array();
					 params['requestFor'] = "DestinationPortNameInCodes";
					 params['destSheduleNumber'] = document.serachUniversalForm.destSheduleNumber.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateDestinationPortNameDojo");
		
		//document.serachUniversalForm.buttonValue.value="popupsearch";
	   // document.serachUniversalForm.submit();
	  //window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function populateDestinationPortNameDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("destAirportname").value=data.DestinationNameInCodes
	  }
	}
	
	function getDestinationPort(ev)
	{
		if(event.keyCode==9){
		var params = new Array();
					 params['requestFor'] = "DestinationPortNumberInCodes";
					 params['destinationPortName'] = document.serachUniversalForm.destAirportname.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateDestinationPortNumberDojo");
		
	   //document.serachUniversalForm.buttonValue.value="popupsearch";
	  //document.serachUniversalForm.submit();
	 //window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=searchlclcoload&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function populateDestinationPortNumberDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("destSheduleNumber").value=data.DestinationNumberInCodes
		   	    }
	 }
	
	function getComCodeDesc(ev)
	{
		if(event.keyCode==9){
		var params = new Array();
					 params['requestFor'] = "CommodityCodeDescription";
					 params['commodityCode'] = document.serachUniversalForm.comCode.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateCommodityDescriptionDojo");
		
		
		//document.serachUniversalForm.buttonValue.value="popupsearch";
	  //document.serachUniversalForm.submit();
	  //window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=searchlclcoload&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	 function populateCommodityDescriptionDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("comDescription").value=data.commodityDescription
		 }
	}
	
	function getComCode(ev)
	{
		if(event.keyCode==9){
		 var params = new Array();
					 params['requestFor'] = "CommodityCode";
					 params['commodityCodeDescription'] = document.serachUniversalForm.comDescription.value;
					  var bindArgs = {
					  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
					  error: function(type, data, evt){alert("error");},
					  mimetype: "text/json",
					  content: params
					 };
					 var req = dojo.io.bind(bindArgs);
					 dojo.event.connect(req, "load", this, "populateCommodityCodeDojo");
			
		//document.serachUniversalForm.buttonValue.value="popupsearch";
	    // document.serachUniversalForm.submit();
	   //window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=searchlclcoload&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function populateCommodityCodeDojo(type, data, evt) {
		    	if(data){
		   		       document.getElementById("comCode").value=data.commodityCode
		 }
	}  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	function titleLetterPress(ev)
	{
		if(event.keyCode==13)
		{
		document.serachUniversalForm.terminalName.value="";
		document.serachUniversalForm.buttonValue.value="popupsearch";
		document.serachUniversalForm.submit();
	     //window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    }
	}
	
	function getDestinationPress(ev)
	{
		if(event.keyCode==13)
		{
		document.serachUniversalForm.destAirportname.value="";
		document.serachUniversalForm.buttonValue.value="popupsearch";
		document.serachUniversalForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function getComCodePress(ev)
	{
		if(event.keyCode==13)
		{
		document.serachUniversalForm.comDescription.value="";
		document.serachUniversalForm.buttonValue.value="popupsearch";
		document.serachUniversalForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcode=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function titleLetterNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.serachUniversalForm.terminalNumber.value="";
		document.serachUniversalForm.buttonValue.value="popupsearch";
		document.serachUniversalForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=retailsearchot&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	
	function getDestinationNamePress(ev)
	{
		if(event.keyCode==13)
		{
		document.serachUniversalForm.destSheduleNumber.value="";
		document.serachUniversalForm.buttonValue.value="popupsearch";
		document.serachUniversalForm.submit();
	//window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=retailsearchpairport&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
 function getComCodeNamePress(ev)
	{
		if(event.keyCode==13){
		document.serachUniversalForm.comCode.value="";
		document.serachUniversalForm.buttonValue.value="popupsearch";
		document.serachUniversalForm.submit();
	//window.open("<%=path%>/jsps/ratemanagement/searchComCode.jsp?button=retailsearchpcomcode&comcodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function ClearScreen()
	 {
	    document.serachUniversalForm.buttonValue.value="clear";
		document.serachUniversalForm.submit();
	 }
	 function getUniversal()
	 {
	   window.location.href="<%=path%>/jsps/ratemanagement/universal_frame.jsp";
	 }
     
     		 function getTerminalName(ev){ 
	 	document.getElementById("terminalName").value="";
     	if(event.keyCode==9 || event.keyCode==13){
			var params = new Array();
			params['requestFor'] = "OrgTerminalName";
			params['terminalNumber'] = document.serachUniversalForm.terminalNumber.value;
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
			params['terminalName'] = document.serachUniversalForm.terminalName.value;
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
			params['terminalNumber'] = document.serachUniversalForm.destSheduleNumber.value;
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
			params['terminalName'] = document.serachUniversalForm.destAirportname.value;
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
			params['code'] = document.serachUniversalForm.comCode.value;
			params['codeType'] = "14";
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
			params['codeDesc'] = document.serachUniversalForm.comDescription.value;
			params['codeType'] = "14";
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
		<html:form action="/serachUniversal" scope="request">
		<font color="blue" size="4"><%=message%></font>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="padding-left:10px;padding-top:10px;">
			
			
  		<%
 		 if(coll!=null && !coll.equals("")){
 %>   
        <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew" ><td colspan="3">Universal/Import Changes</td>
			 <td  align="right" colspan="2"> <input type="button" value="Go Back" class="buttonStyleNew" id="cancel" onclick="ClearScreen()"/>
   		         <input type="button" value="AddNew" class="buttonStyleNew" id="addnew" onclick="return GB_show('Universal/Import','<%=path%>/jsps/ratemanagement/universalAddPop.jsp?universal='+'add',170,600)" />   
             </td>   			  
			</tr>
        <tr  class="textlabels">
        <td width="100%" colspan="5">
            <table width="100%" border="0" cellpadding="0" cellspacing="2" class="displaytagstyle">
            <thead>
            <tr >
					<td class="textlabels"><b>
						Org Trm No
					</td>
					<td class="textlabels"><b>
						Org Trm Name
					</td>
					<td class="textlabels"><b>
						Destination No			
					</td>
					<td class="textlabels"><b>
						Destination Name
					</td>
					<td class="textlabels"  width="100%"><b>
						&nbsp;
					</td>
				</tr>
				</thead>
			<tr class="even">
			<td><%=trmNum%></td>
   			<td ><%=trmNam%></td>
   			<td ><%=destAirPortNo%></td>
   			<td ><%=destAirPortName%></td>
		    <td width="100%"> <%=Port%></td>
		   </tr>
   		</table>	
<%
}
else{
%>	
      <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
			<tr class="tableHeadingNew" ><td>Search Universal/Import Changes</td>
			</tr>
			<tr>
			<td>
			  <table width="100%" border="0" cellpadding="0" cellspacing="4" class="displaytagstyle">
  			<tr class="textlabels">
    			<td>Org Trm</td>
    			<td >
    			<input name="terminalNumber" id="terminalNumber"  value="<%=trmNum %>" onkeydown="getTerminalName(this.value)"  maxlength="2" size="30" />    
    			<dojo:autoComplete formId="serachUniversalForm" textboxId="terminalNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=SEARCH_UNIVERSAL&from=0"/>                
    			</td>
    			<td>OriginTerminal Name</td>
    			<td >
    			 <input name="terminalName" id="terminalName" value="<%=trmNam %>" onkeydown="getTerminalNumber(this.value)" size="30"/>    
    			   <dojo:autoComplete formId="serachUniversalForm" textboxId="terminalName" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_UNIVERSAL&from=0"/>
    			</td>
			</tr>
    		<tr class="textlabels">	
    			<td >Dest Port</td>
    			<td>
    			<input name="destSheduleNumber" id="destSheduleNumber" value="<%=destAirPortNo %>" onkeydown="getdestPort(this.value)" size="30" maxlength="5"/>    
    			<dojo:autoComplete formId="serachUniversalForm" textboxId="destSheduleNumber" action="<%=path%>/actions/getUnlocationCodeForOldDojo.jsp?tabName=SEARCH_UNIVERSAL&from=1"/>
    			</td>
    			
    			<td>Dest Port Name</td>
    			<td >
    			<input name="destAirportname" id="destAirportname"   value="<%=destAirPortName %>" onkeydown="getDestPortName(this.value)" size="30"/>    
    		    <dojo:autoComplete formId="serachUniversalForm" textboxId="destAirportname" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=SEARCH_UNIVERSAL&from=1"/>
    			</td>
    		<td><table><tr>
				 <td  align="left" class="headerbluelarge"></td>
				</tr></table></td>	
    		</tr>
    		<tr class="textlabels">	
    			<td>Com Code</td>
    			<td >
    			<input name="comCode" id="comCode"  value="<%=comCode %>" onkeydown="getCodeDesc(this.value)"   maxlength="6" size="30"/>    
    		    <dojo:autoComplete formId="serachUniversalForm" textboxId="comCode" action="<%=path%>/actions/getChargeCode.jsp?tabName=SEARCH_UNIVERSAL"/>
    			</td>
    			<td >Com Description </td>
    			<td >
    			<input name="comDescription" id="comDescription"  value="<%=comDesc %>" onkeydown="getCode(this.value)" size="30" />    
    		    <dojo:autoComplete formId="serachUniversalForm" textboxId="comDescription" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=SEARCH_UNIVERSAL"/>
    			</td>
    			
    		</tr>
    		<tr>
    		        <td valign="top" colspan="6" align="center" style="padding-top:10px;">
    		           <input type="button" value="Search" class="buttonStyleNew" onclick="searchform()"/>
    		           <input type="button" value="Clear" class="buttonStyleNew" onclick="ClearScreen()" id="cancel"/>
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
			<tr class="tableHeadingNew" ><td><%="Univesal Import Rates" %></td>
			<td align="right"><a id="commonChargesToggle" href="#" style="" ><img src="<%=path%>/img/icons/up.gif" border="0" /></a> </td>
						
			</tr> 
 			
  			<tr>
        		<td colspan="2">
        		<div id="common_Charges_vertical_slide">
        		<div id="divtablesty1" class="scrolldisplaytable" >
          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<%
         			 int k=0;

          	
		  if(noncommon!=null)		 
		  {
		  
		 
		 %>
		 <display:table name="<%=noncommon%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="noncommonTable" sort="list"> 
			 	
			 	<display:setProperty name="paging.banner.some_items_found">
    			<span class="pagebanner">
    				<font color="blue">{0}</font> Universal Rates Displayed,For more Data click on Page Numbers.
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
				<display:setProperty name="paging.banner.item_name" value="Universal"/>
  				<display:setProperty name="paging.banner.items_name" value="Universal"/>
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
  					String user="";
  					String efdate="";
  					String time="";
  					String chnaged="";
  					String chargeDesc="";
  					String link ="",lBsAmount="",cFtAmount="",kGAmount="",cBAmount="";
  					if(noncommon!=null && noncommon.size()>0)
  					{
  						UniversalMaster lclcoloadRates=(UniversalMaster)noncommon.get(k); 	
  						if(lclcoloadRates.getCommodityCode()!=null)
  						{
  							comCodeDisplay=lclcoloadRates.getCommodityCode().getCode();
  							comDescDisplay=lclcoloadRates.getCommodityCode().getCodedesc();
  						}
  						 if(session.getAttribute("universalUpdateRecords")!=null)
  						 {
								UniversalMaster rr=(UniversalMaster)session.getAttribute("universalUpdateRecords");
								if(rr.getCommodityCode()!=null && lclcoloadRates.getCommodityCode()!=null && 
								rr.getCommodityCode().getId().equals(lclcoloadRates.getCommodityCode().getId()))
								{
									chnaged="a";
								
									}
							}
  						if(lclcoloadRates.getUniversalCommodity()!=null)
  						{
  						Iterator iter=lclcoloadRates.getUniversalCommodity().iterator();
  						while(iter.hasNext())
  						{
  						UniverseCommodityChrg retailRate=(UniverseCommodityChrg)iter.next();
  						
  						    if(retailRate.getChargeCode()!=null)
  							{
  							chargeCode=retailRate.getChargeCode().getCode();
  							chargeDesc=retailRate.getChargeCode().getCodedesc();
  							}
  							if(retailRate.getWhoChanged()!=null)
  							{
  							user=retailRate.getWhoChanged();
  							}
  							if(retailRate.getEffectiveDate()!=null)
  							{
  							efdate=dateFormat.format(retailRate.getEffectiveDate());
  							}
  							if(retailRate.getEffectiveDate()!=null)
  							{
  							time=dateFormat1.format(retailRate.getEffectiveDate());
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
  							if(retailRate.getAmtPer1000kg()!=null)
  							{
  							amtPerKg=df.format(retailRate.getAmtPer1000kg());
  							}
  							
  							if(retailRate.getPercentage()!=null)
  							{
  							percentage=retailRate.getPercentage().toString();
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
  						map = getUniversalBottmLineRate(lclcoloadRates.getId());
									if(map!=null){
									kGAmount=map.get("kgs").toString();
									cBAmount=map.get("cbm").toString();
									}
  						amount=amtPerCbm+"/"+amtPerKg;
  						}
  						else if(Port!=null && Port.equals("English"))
  						{
  						map = getUniversalBottmLineRate(lclcoloadRates
									.getId());
										if(map!=null){
									lBsAmount=(String)map.get("lbs");
									cFtAmount=(String)map.get("cft");
		  						}
  						amount=amtPerCft+"/"+amtlbs;
  						}
  						}
  						}
  					}
  					link = editPath +"?param=" + lclcoloadRates.getId();
  				 }	
  		 		%>
  		 		<display:column style="width:2%;visibility:hidden;"><%=chnaged%></display:column>
  		 		<display:column href="<%=editPath%>" paramId="paramid"  sortable="true" paramProperty="id" title="#Comm"><%=comCodeDisplay%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Chrg Code" >&nbsp;&nbsp;&nbsp;&nbsp;<%=chargeCode%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Chrg Des" >&nbsp;&nbsp;&nbsp;&nbsp;<%=chargeDesc%></display:column>
				<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Chrg Type">&nbsp;&nbsp;&nbsp;&nbsp;<%=chargeType%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Std">&nbsp;&nbsp;&nbsp;&nbsp;<%=standard%></display:column>
			 	<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;Amt">&nbsp;&nbsp;&nbsp;<%=amount%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;MinAmt" >&nbsp;&nbsp;&nbsp;&nbsp;<%=minAmt%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Effctive Date" >&nbsp;&nbsp;&nbsp;&nbsp;<%=efdate%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;Time" >&nbsp;&nbsp;&nbsp;&nbsp;<%=time%></display:column>
  		 		<display:column sortable="true" title="&nbsp;&nbsp;&nbsp;&nbsp;User" >&nbsp;&nbsp;&nbsp;&nbsp;<%=user%></display:column>
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
<%k++; %>
 	<display:column>
    <span class="hotspot" onmouseover="tooltip.show('<strong>MoreInfo</strong>',null,event);" onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" border="0"  onclick="window.location.href='<%=link %>'" /></span>
	</display:column>
 </display:table>
<%} %>
 </table>
 </div>
 </div>
 </td>
</tr>	
</table>
<%} %>
<html:hidden property="buttonValue" styleId="buttonValue" />
<html:hidden property="index"/>
<html:hidden property="search" />
</html:form>
</body>
<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
