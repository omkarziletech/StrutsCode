<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java"
	import="java.text.DateFormat,java.text.DecimalFormat,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.VoyageMaster,com.gp.cong.logisoft.domain.VoyageInland,java.text.ParseException,java.util.*, java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.FclContainertypeCharges,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,java.util.List,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,com.gp.cong.logisoft.domain.FclBuyCost"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@include file="../includes/jspVariables.jsp"%>
<%DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");


String origTerminal="";
String origTerminalName="";
String destTerminal="";
String destTerminalName="";
String terArrvlDate="";
String inlandVoyNo="";
String loadedDate="";
String deparDate="";
String origin="";
String originName="";
String destination="";
String destinationName="";
String optITPort="";
String portName="";
String index="";
String vesselNum="";
String vesselName="";
String ssVoyageNo="";
String buttonValue="";
List unitlist=new ArrayList();
List unittypelist=new ArrayList();
List vyge=new ArrayList();
String modify="";	
String message="";
String result="";
String mesg="";
String mesg1="";
DecimalFormat df=new DecimalFormat("0.00");
VoyageInland voyageInland=new VoyageInland();





//for edit

if(session.getAttribute("voyageInland1")!=null){

voyageInland=(VoyageInland)session.getAttribute("voyageInland1");

if(voyageInland.getClosed()!=null)
{
String s=voyageInland.getClosed();
if(s.equals("c"))
{
mesg="CLOSED";

}

}
if(voyageInland.getAudited()!=null)
{
{
String w=voyageInland.getAudited();
if(w.equals("A"))
{
mesg1="AUDITED";
}

}
}
if(voyageInland.getScheduleDkDestination()!=null)
		{
			destination=voyageInland.getScheduleDkDestination().getShedulenumber().toString();

			destinationName=voyageInland.getScheduleDkDestination().getPortname();

		}
if(voyageInland.getDestTerminal()!=null)
{
destTerminal=voyageInland.getDestTerminal().getShedulenumber();

destTerminalName=voyageInland.getDestTerminal().getPortname();
}
if(voyageInland.getDateTerminalArrival()!=null){
try
	{	Date terArrvlDt=voyageInland.getDateTerminalArrival();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		terArrvlDate=df1.format(terArrvlDt);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}
	
}
if(voyageInland.getDateOfDeparture()!=null){
try
	{	
	
	   Date departure=voyageInland.getDateOfDeparture();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		deparDate=df1.format(departure);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}	

	
}
if(voyageInland.getSslVoyageNo()!=null){
	ssVoyageNo=voyageInland.getSslVoyageNo().toString();
}

if(voyageInland.getInlandVoyageNo()!=null){
	inlandVoyNo=voyageInland.getInlandVoyageNo().toString();
	}
	
	if(voyageInland.getDateLoaded()!=null){
	
	try
	{	Date Dtld=voyageInland.getDateLoaded();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		loadedDate=df1.format(Dtld);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}
	
}
}
if(voyageInland.getScheduleDkDestination()!=null)
		{
			destination=voyageInland.getScheduleDkDestination().getShedulenumber().toString();

			destinationName=voyageInland.getScheduleDkDestination().getPortname();

		}
		if(voyageInland.getVesselNo()!=null)
		{
		vesselNum=voyageInland.getVesselNo().getCode();

		vesselName=voyageInland.getVesselNo().getCodedesc();

		}
		if(voyageInland.getOriginTerminal()!=null)
{
origTerminal=voyageInland.getOriginTerminal().getTrmnum();
origTerminalName=voyageInland.getOriginTerminal().getTerminalLocation();

}
if(voyageInland.getScheduleDkOrigin()!=null)
		{
			origin=voyageInland.getScheduleDkOrigin().getTrmnum();

	originName=voyageInland.getScheduleDkOrigin().getTerminalLocation();
		}
		if(voyageInland.getOptItPortNo()!=null)
		{
			optITPort=voyageInland.getOptItPortNo().getShedulenumber();

			portName=voyageInland.getOptItPortNo().getPortname();

		}
		if(voyageInland.getDateTerminalArrival()!=null){
		try
	{	Date terArrvlDt=voyageInland.getDateTerminalArrival();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		terArrvlDate=df1.format(terArrvlDt);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}

	
}
if(voyageInland.getDateOfDeparture()!=null){
try
	{	
	
	   Date departure=voyageInland.getDateOfDeparture();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		deparDate=df1.format(departure);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}	
	
}
if(voyageInland.getSslVoyageNo()!=null){
	ssVoyageNo=voyageInland.getSslVoyageNo().toString();
}


//for edit ends

if(session.getAttribute("voyageInlnd")!=null){
voyageInland=(VoyageInland)session.getAttribute("voyageInlnd");

if(voyageInland.getDateTerminalArrival()!=null){
	try
	{	Date terArrvlDt=voyageInland.getDateTerminalArrival();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		terArrvlDate=df1.format(terArrvlDt);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}
	
}
if(voyageInland.getDateOfDeparture()!=null){
try
	{	
	
	   Date departure=voyageInland.getDateOfDeparture();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		deparDate=df1.format(departure);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}	
	
}
if(voyageInland.getSslVoyageNo()!=null){
	ssVoyageNo=voyageInland.getSslVoyageNo().toString();
}
}
if(session.getAttribute("inlandrate")!=null){
	result=(String)session.getAttribute("inlandrate");

	}

if(session.getAttribute("message")!=null){
message=(String)session.getAttribute("message");
}

DBUtil dbutil=new DBUtil();
 String records="";

AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setShowOnSailingSchedule("Y");



if(session.getAttribute("inlandvoyages")!=null){
		voyageInland=(VoyageInland)session.getAttribute("inlandvoyages");
		
}	

if(session.getAttribute("addinlandrecords")!=null)
{

voyageInland=(VoyageInland)session.getAttribute("addinlandrecords");
if(voyageInland.getOriginTerminal()!=null)
{
origTerminal=voyageInland.getOriginTerminal().getTrmnum();
origTerminalName=voyageInland.getOriginTerminal().getTerminalLocation();

}
if(voyageInland.getDestTerminal()!=null)
{
destTerminal=voyageInland.getDestTerminal().getShedulenumber();
destTerminalName=voyageInland.getDestTerminal().getPortname();
}
if(voyageInland.getInlandVoyageNo()!=null){
	inlandVoyNo=voyageInland.getInlandVoyageNo().toString();
	}
if(voyageInland.getDateLoaded()!=null){
    DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		loadedDate=df1.format(voyageInland.getDateLoaded());
	
}
records="Norecords";
}



	if(voyageInland.getSslVoyageNo()!=null){
	ssVoyageNo=voyageInland.getSslVoyageNo();

	}
if(session.getAttribute("inlandvesselrecords")!=null)
	{    
		voyageInland=(VoyageInland)session.getAttribute("inlandvesselrecords");
		if(voyageInland.getVesselNo()!=null)
		{
		vesselNum=voyageInland.getVesselNo().getCode();

		vesselName=voyageInland.getVesselNo().getCodedesc();

		}
		}	 	

if(session.getAttribute("inlandport")!=null)
	{
		voyageInland=(VoyageInland)session.getAttribute("inlandport");

		if(voyageInland.getOptItPortNo()!=null)
		{
			optITPort=voyageInland.getOptItPortNo().getShedulenumber();

			portName=voyageInland.getOptItPortNo().getPortname();

		}
	}	
	if(session.getAttribute("inlandports")!=null)
	{
		voyageInland=(VoyageInland)session.getAttribute("inlandports");

		if(voyageInland.getScheduleDkDestination()!=null)
		{
			destination=voyageInland.getScheduleDkDestination().getShedulenumber().toString();

			destinationName=voyageInland.getScheduleDkDestination().getPortname();

		}
	}	
	
	if(session.getAttribute("inlandports")!=null)
	{
		voyageInland=(VoyageInland)session.getAttribute("inlandports");

		if(voyageInland.getScheduleDkOrigin()!=null)
		{
			origin=voyageInland.getScheduleDkOrigin().getTrmnum();

	originName=voyageInland.getScheduleDkOrigin().getTerminalLocation();
		}
	}	
		   	   
if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}

if(session.getAttribute("voyagepopup")!=null)
{

voyageInland=(VoyageInland)session.getAttribute("voyagepopup");
if(voyageInland.getDateTerminalArrival()!=null)
{
	
	try
	{	Date terArrvlDt=voyageInland.getDateTerminalArrival();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		terArrvlDate=df1.format(terArrvlDt);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}	
					
}
		
if(voyageInland.getDateOfDeparture()!=null)
{
try
	{	
	
	   Date departure=voyageInland.getDateOfDeparture();
	      DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		deparDate=df1.format(departure);
					
	}
	catch (Exception e)
	{
				e.printStackTrace();
	}	
					
}
if(voyageInland.getSslVoyageNo()!=null)
{
ssVoyageNo=voyageInland.getSslVoyageNo();
}

}
if(session.getAttribute("yes")!=null)
{

mesg="CLOSED";
}

if(session.getAttribute("no")!=null)
{
mesg1="AUDITED";
}


 %>
<%String path = request.getContextPath(); %>
<html>
	<head>
		<title>JSP for InlandVoyageForm form</title>
		<%@include file="../includes/baseResources.jsp"%>
		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils','utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		</script>

		<script language="javascript" type="text/javascript">
				
       	
       		function submit1()
		{
			alert("save");
			document.inlandVoyageForm.buttonValue.value="save";
			document.inlandVoyageForm.submit();
		}
       	
       	function save1(val1)
		{
			var netie=navigator.appName;//skg
			if(val1=="edit"){
				document.inlandVoyageForm.buttonValue.value="edit";
 			}
 			else if(val1=="save"){
 				document.inlandVoyageForm.buttonValue.value="save";
 			}
			//document.inlandVoyageForm.buttonValue.value="save";
			document.inlandVoyageForm.submit();
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
			document.inlandVoyageForm.buttonValue.value="popup";
			document.inlandVoyageForm.submit();
			return false;
			}
			
		function costtypechange()
		{
		
			if(document.inlandVoyageForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else{
				document.inlandVoyageForm.buttonValue.value="costType";
				document.inlandVoyageForm.submit();
			}
		}
		
		function addForm(val1)
		{
			//alert(val1);
			if(document.inlandVoyageForm.costcode.value!=null&&document.inlandVoyageForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else if(document.inlandVoyageForm.costtype.value!=null&&document.inlandVoyageForm.costtype.value=="0")
			{
				alert("Please select Cost Type !");
			}
			else if(val1!=null&&val1==11300)
			{
				 if(document.inlandVoyageForm.unittype.value!=null&&document.inlandVoyageForm.unittype.value=="0")
					{
					alert("Please select Unit Type !");
					}
					else{
					document.inlandVoyageForm.buttonValue.value="add";
					document.inlandVoyageForm.submit();
					}
			}
			else if(val1!=null&&val1==11306)
			{
					if(document.inlandVoyageForm.range.value!=null&&document.inlandVoyageForm.range.value=="0")
					{
						alert("Please select Wight Range !");
					}else{
					document.inlandVoyageForm.buttonValue.value="add";
					document.inlandVoyageForm.submit();
					}
			}
			else{
			
				document.inlandVoyageForm.buttonValue.value="add";
				document.inlandVoyageForm.submit();
			}
		}
		
		function delete1()
		{
			document.inlandVoyageForm.buttonValue.value="delete";
			document.inlandVoyageForm.submit();
		}
		
		function saveForm1(val,val2)
		{
		
			
			if(val!=null && val!="save")
			{
			document.inlandVoyageForm.buttonValue.value="edit";
			}
			else{
				document.inlandVoyageForm.buttonValue.value="save";
			}
			if(val2!=""){
			alert(val2);
			
			}
			else{
			
			document.inlandVoyageForm.submit();
			}
		}
		
		function cancelForm(val1,val2)
		{
			
			if(val1!="edit"){
				var1=confirm("Do u want to save this record");
				if(var1){
				saveForm1(val1,val2);
				}
			
				else{
				document.inlandVoyageForm.buttonValue.value="cancel";
				document.inlandVoyageForm.submit();
				}
			}else{
			document.inlandVoyageForm.buttonValue.value="cancel";
			document.inlandVoyageForm.submit();
			}
		}
		
		function noteForm()
		{
			document.inlandVoyageForm.buttonValue.value="note";
			document.inlandVoyageForm.submit();
		}
		
		
    		 function displaytagcolornon()
			{
		 	 	var datatableobj1 = document.getElementById('includedcomtable');
		
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

					
					}else if(tablerowobj.cells[0].innerHTML=="acc")
					{
					tablerowobj.bgColor='#FF3300';
					
					}
		 	 	}
	}
	
    function displaytagcolor()
	{
		 var datatableobj = document.getElementById('includedtable');
		
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
					
					}else if(tablerowobj.cells[0].innerHTML=="acc")
					{
					tablerowobj.bgColor='#FF3300';
					
					}
		 	 	}
		 	 	
	}
	
	function initRowHighlighting()
  	{
			if (!document.getElementById('includedtable')){ return; }
			 var tables = document.getElementById('includedtable');
			attachRowMouseEvents(tables.rows);
		
	}
	
	function attachRowMouseEvents(rows)
	{
		for(var i =1; i < rows.length; i++)
		{
			var row = rows[i];
			row.onmouseover =	function() 
			{ 
				this.className = 'CCFFFF';
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
			if (!document.getElementById('includedcomtable')){ return; }
			 var tables = document.getElementById('includedcomtable');
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

		if(document.inlandVoyageForm.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('includedcomtable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		} 
		if(document.inlandVoyageForm.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('includedcomtable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('includedcomtable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('includedcomtable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
	 
	function setWarehouseStyle()
	{

		if(document.inlandVoyageForm.buttonValue.value=="searchall")
		{
		  	 var x=document.getElementById('includedtable').rows[0].cells;
		     x[0].className="sortable sorted order1";
		} 
		if(document.inlandVoyageForm.buttonValue.value=="search")
		{
		  	var input = document.getElementsByTagName("input");
		  	
	  	    if(!input[0].value=="")
	  		{
	  		 	var x=document.getElementById('includedtable').rows[0].cells;	
	  		   	x[0].className="sortable sorted order1";
	  		}
	  		else if(!input[4].value=="")
	  		{	
	  		 	var x=document.getElementById('includedtable').rows[0].cells;	
	  		   	x[2].className="sortable sorted order1";
	  		}
	  		else if(!input[6].value=="")
	  		{	
	  		 	var x=document.getElementById('includedtable').rows[0].cells;	
	  		   	x[4].className="sortable sorted order1";
	  		}
	  		
	  	  }
	 }
	 
		function nonTest(val1){
			
			document.inlandVoyageForm.buttonValue.value="index";
			document.inlandVoyageForm.index.value=val1;
			document.inlandVoyageForm.buy.value="buy";
			document.inlandVoyageForm.submit();
			
			mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+val1+"&buy=buy","","width=800,height=200");
        mywindow.moveTo(200,180);		
		}
		
	
		function comTest(val1){
			
			document.inlandVoyageForm.buttonValue.value="index";
			document.inlandVoyageForm.index.value=val1;
			document.inlandVoyageForm.submit();
			mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+val1,"","width=800,height=200");
       		 mywindow.moveTo(200,180);		
		}
		
		function titleLetter(ev)
	{
		if(event.keyCode==9){
		//document.inlandVoyageForm.buttonValue.value="search";
			document.inlandVoyageForm.submit();
	window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=inlandvoyage&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	
	function disabled(val1,val2) {
			alert(val1);
			if(val1!=""&&(val1 == 0 || val1== 3))
			{
		  	  	 var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	{
	   		 			 if(imgs[k].id != "cancel"&&imgs[k].id != "note")	 {
	   		 			     imgs[k].style.visibility = 'hidden';
	   						 }
	   					}
		   		var input = document.getElementsByTagName("input");
			   		for(i=0; i<input.length; i++)	{
			  			input[i].readOnly=true;
					 	}
		  	 	var textarea = document.getElementsByTagName("textarea");
			  	 	for(i=0; i<textarea.length; i++){
			 			textarea[i].readOnly=true;		 					
					  	}
	   			var select = document.getElementsByTagName("select");
	   		   		for(i=0; i<select.length; i++)	{
				 		select[i].disabled=true;
						select[i].style.backgroundColor="blue";
		  		  	 	}
		  		  	 	 document.getElementById("save").style.visibility = 'hidden';
		  		  	 	  document.getElementById("delete").style.visibility = 'hidden';
		  		  	 	   document.getElementById("audit").style.visibility = 'hidden';
   		  		  	 	   document.getElementById("close").style.visibility = 'hidden';
	  	 		}
		  	 if(val1 == 1)	 {
	  	 		 	  document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 	if(val1==3 && val2!=""){
				//alert(val2);
				}		
    		}
    		
    		function cancel2(val1,val3)
		{
			 if(val1!=""){
		 	var result = confirm("Do  you want to Add Rates ??");
		 	
		 	 if(result)
				{ //alert("inside");
					save1(val3);//skg
	   			}	
			 else
			  {
			  		
	   				document.inlandVoyageForm.buttonValue.value="savecancel";
	   				document.inlandVoyageForm.submit();
			  }
			  }
				
		
 			
		}
		
		function cancel1(val){
		
			document.inlandVoyageForm.buttonValue.value="cancel";
			document.inlandVoyageForm.submit();
			}
			function getOrigin(ev)
			{
			  
		 		if(event.keyCode==9)
				 {
	
	  					 window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=inlandports&trname="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 			         document.inlandVoyageForm.buttonValue.value="popup";
			             document.inlandVoyageForm.submit();	
	 			}
			
			}
			function getOrigin1(ev)
			{
			 	if(event.keyCode==9)
				 {
	
	  					 window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=inlandports&tername="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 				    document.inlandVoyageForm.buttonValue.value="popup";
			             document.inlandVoyageForm.submit();
	 				
	 				
	 				}
			
			}
			function getDest(ev)
			{
			
			        if(event.keyCode==9)
				 {
	
	  					 window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=inlandports&percode="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 				    document.inlandVoyageForm.buttonValue.value="popup";
			             document.inlandVoyageForm.submit();
	 				
	 				
	 				}
			
			}
			function getDest1(ev)
			{
			
			        if(event.keyCode==9)
				 {
	
	  					 window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=inlandports&percodename="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 				  document.inlandVoyageForm.buttonValue.value="popup";
			             document.inlandVoyageForm.submit();
	 				
	 				
	 				}
			
			}
			function getitPort(ev)
			{
			
			      if(event.keyCode==9)
				 {
	
	  					 window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=inlandport&percode="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 				     document.inlandVoyageForm.buttonValue.value="popup";
			             document.inlandVoyageForm.submit();
	 				
	 				
	 				}
			
			}
						
			function getitPort2(ev)
			{
			     
			      if(event.keyCode==9)
				 {
	
	  					 window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=inlandport&percodename="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 				        document.inlandVoyageForm.buttonValue.value="popup";
			             document.inlandVoyageForm.submit();
	 				
	 				
	 				}
			
			}
			function vessel(ev)
			{
			      if(event.keyCode==9)
				 {
	
	  					 window.open("<%=path%>/jsps/voyagemanagement/searchVessel.jsp?button=inlandvessel&vesselno="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 				 document.inlandVoyageForm.buttonValue.value="popup";
			             document.inlandVoyageForm.submit();
	 				
	 				}
			
			
			}
				function vessel1(ev)
			{
			      if(event.keyCode==9)
				 {
	
	  					 window.open("<%=path%>/jsps/voyagemanagement/searchVessel.jsp?button=inlandvessel&vesselname="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 				     document.inlandVoyageForm.buttonValue.value="popup";
			             document.inlandVoyageForm.submit();
	 				
	 				}
			
			
			}
				function closed()
	{
	    var closedstr = document.getElementById('tdaudit').innerHTML;
		var index = closedstr.indexOf('CLOSED');
		if(index!=-1)
		{
			alert("This Voyage is already closed.");
		}
		else
		{
		    if(confirm('Do u really want to close this voyage'))
		    {
		    	 document.inlandVoyageForm.buttonValue.value="closed";
		    	document.inlandVoyageForm.submit();
		    }
		}    
	   
	   
	
	}
	function audited()
	{
		var auditstr = document.getElementById('tdaudit').innerHTML;
		var index = auditstr.indexOf('AUDITED');
		if(index!=-1)
		{
			alert("This Voyage is already audited.");
		}
		else
		{
			  if(confirm('Do u really want to Audit this voyage'))
		    {
		    	 document.inlandVoyageForm.buttonValue.value="audit";
		    	document.inlandVoyageForm.submit();
		    }
		}    
	
	}

		function getOriginTerminalName(ev)
	{document.getElementById("originName").value="";
		if(event.keyCode==13 || event.keyCode==9)
		{
		   var params = new Array();
				 params['requestFor'] = "OrgTerminalName";
				 params['terminalNumber'] = document.inlandVoyageForm.origin.value;
				  var bindArgs = {
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateTerminalNameDojo");
		}
	}
	 function populateTerminalNameDojo(type, data, evt) {
		   	if(data){
		   		document.getElementById("originName").value=data.terminalName;
		   	}
	 }
	
	function getOriginTerminalNumber(ev)
	{document.getElementById("origin").value="";
		if(event.keyCode==9 || event.keyCode==13){
		
		 var params = new Array();
				 params['requestFor'] = "OrgTerminalNumber";
				 params['terminalName'] = document.inlandVoyageForm.originName.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateTerminalNumberDojo");
		}
	}
	 function populateTerminalNumberDojo(type, data, evt) {
		   	if(data){
		   		document.getElementById("origin").value=data.terminalNumber;
		   	}
	   }



	 function getDestinationName(ev)
	{document.getElementById("destinationName").value="";
		if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "DestAirportname";
				 params['scheduleNumber'] = document.inlandVoyageForm.destination.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 
				 dojo.event.connect(req, "load", this, "populateDestAirportname1");
			    }
	}
	  function populateDestAirportname1(type, data, evt) {
		   	if(data){
		   		document.getElementById("destinationName").value=data.destAirportname;
		   	}
		   }
	function getDestinationNumber(ev)
	{document.getElementById("destination").value="";
		if(event.keyCode==13 || event.keyCode==9)
		{
		    var params = new Array();
				 params['requestFor'] = "ScheduleNumber";
				 params['destAirportName'] = document.inlandVoyageForm.destinationName.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateDestAirportname");
		 }
	}
	
	 function populateDestAirportname(type, data, evt) {
		   	if(data){
		   		document.getElementById("destination").value=data.destSheduleNumber;
		   	}
		   }		
  
     function getItPortName(ev)
	{document.getElementById("portName").value="";
		if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "DestAirportname";
				 params['scheduleNumber'] = document.inlandVoyageForm.optITPort.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 
				 dojo.event.connect(req, "load", this, "populateItPortName");
			    }
	}
	  function populateItPortName(type, data, evt) {
		   	if(data){
		   		document.getElementById("portName").value=data.destAirportname;
		   	}
		   }
	function getItPortNo(ev)
	{document.getElementById("optITPort").value="";
		if(event.keyCode==13 || event.keyCode==9)
		{
		    var params = new Array();
				 params['requestFor'] = "ScheduleNumber";
				 params['destAirportName'] = document.inlandVoyageForm.portName.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateItPortNo");
		 }
	}
	
	 function populateItPortNo(type, data, evt) {
		   	if(data){
		   		document.getElementById("optITPort").value=data.destSheduleNumber;
		   	}
		   }		
  function getVesselName(ev){ 
   	 		document.getElementById("vesselName").value="";
   			if(event.keyCode==9 || event.keyCode==13){
	 			var params = new Array();
	 			params['requestFor'] = "VesselName";
	 			params['vesselNo'] = document.inlandVoyageForm.vesselNum.value;
	  			var bindArgs = {
		  		url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
		  		error: function(type, data, evt){alert("error");},
		  		mimetype: "text/json",
		  		content: params
	 		};
	 	var req = dojo.io.bind(bindArgs);
	 	dojo.event.connect(req, "load", this, "populateVesselName");
    	}
	}
    function populateVesselName(type, data, evt) {
  		if(data){
       		document.getElementById("vesselName").value=data.VesselName;
       		}
       	}
         function getVesselNo(ev){ 
   	 		document.getElementById("vesselNum").value="";
   			if(event.keyCode==9 || event.keyCode==13){
	 			var params = new Array();
	 			params['requestFor'] = "VesselNumber";
	 			params['vesselName'] = document.inlandVoyageForm.vesselName.value;
	  			var bindArgs = {
		  		url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
		  		error: function(type, data, evt){alert("error");},
		  		mimetype: "text/json",
		  		content: params
	 		};
	 	var req = dojo.io.bind(bindArgs);
	 	dojo.event.connect(req, "load", this, "populateVesselNo");
    	}
	}
    function populateVesselNo(type, data, evt) {
  		if(data){
       		document.getElementById("vesselNum").value=data.VesselNo;
       		}
       	}
  
		</script>
	</head>
	<body class="whitebackgrnd">
		<br>
		<html:form action="/inlandVoyage" name="inlandVoyageForm"
			type="com.gp.cong.logisoft.struts.voyagemanagement.form.InlandVoyageForm" scope="request">


			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				id="records">

				<tr class="textlabels">
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							id="records">
							<tr>

								<td width="540" align="left" class="headerbluelarge">
									Create Inland Voyage
									<br>
								</td>
								<% 
                       if(result!=null&&result.equals("save"))
                       {

                   %>
								<td width="78" align="left" class="headerbluelarge">
									<input type="button" value="Save"
										onclick="save1('<%=result%>')" class="buttonStyleNew" />

									<br>
								</td>
								<td>
									<input type="button" value="Go Back"
										onclick="cancel2('<%=records%>','<%=result%>')"
										class="buttonStyleNew" />

									<br>
								</td>
								<% 
                   }
                   if(result!=null&&result.equals("edit"))
                   {
                   %>

								<td>
									<input type="button" value="Audit" id="audit"
										onclick="audited()" class="buttonStyleNew" />

									<br>
								</td>
								<td>
									<input type="button" value="Close" id="close"
										onclick="closed()" class="buttonStyleNew" />
									<br>
								</td>
								<td>
									<input type="button" value="Save" id="save"
										onclick="save1('<%=result%>')" class="buttonStyleNew" />
									<br>
								</td>

								<td>
									<input type="button" value="Delete" id="delete"
										onclick="delete1()" class="buttonStyleNew" />
									<br>
								</td>
								<td>
									<input type="button" value="Go Back"
										onclick="cancel1('<%=modify%>')" class="buttonStyleNew" />
									<br>
								</td>
								<td align="right">
									<input type="button" value="Note" onclick="noteForm()"
										class="buttonStyleNew" />
									<br>
								</td>
								<%
                    }
                    if(session.getAttribute("message")!=null)
                    {
                             session.removeAttribute("message");
                    }
                 %>
							</tr>
						</table>
						<br>
					</td>

				</tr>
				<tr>
					<td id="tdaudit">
						<font color="blue" size="4"><%=mesg%>
							&nbsp;&nbsp;&nbsp;&nbsp; <%=mesg1%></font>
						<br>
					</td>

				</tr>

				<table border="0" cellpadding="0" cellspacing="0"
					class="tableBorderNew" width="100%">
					<tr class="tableHeadingNew">
						Inland Voyage Details
					</tr>
					<tr>
						<td>





							<table border="0">
								<tr class="textlabels">
									<td>
										Origin Terminal
										<br>
									</td>
									<td>
										<html:text property="origTerminal" size="15" readonly="true"
											styleId="loadingTerminal" disabled="disabled"
											value="<%=origTerminal%>"></html:text>
										<br>
									</td>

									<td align="right">
										Origin Terminal Name
										<br>
									</td>
									<td>
										<html:text property="origTerminalName" size="15"
											readonly="true" styleId="loadingTerminalName"
											disabled="disabled" value="<%=origTerminalName%>"></html:text>
										<br>
									</td>
								</tr>
								<tr class="textlabels">
									<td>
										Destination Terminal
										<br>
									</td>
									<td>
										<html:text property="destTerminal" readonly="true" size="15"
											value="<%=destTerminal%>"></html:text>
										<br>
									</td>
									<td align="right">
										Destination Terminal Name
										<br>
									</td>
									<td>
										<html:text property="destTerminalName" readonly="true"
											size="15" value="<%=destTerminalName%>"></html:text>
										<br>
									</td>
								</tr>
								<tr class="textlabels">

									<td>
										Inland Voyage#
										<br>
									</td>
									<td>
										<html:text property="inlandVoyNo" readonly="true" size="15"
											disabled="disabled" value="<%=inlandVoyNo%>"></html:text>
										<br>
									</td>
									<td align="right">
										Date Loaded
										<br>
									</td>
									<td>
										<html:text property="loadedDate" readonly="true"
											styleId="txtItemcreatedo" size="15" disabled="disabled"
											value="<%=loadedDate%>"></html:text>
										<br>
									</td>
								</tr>
								<tr class="textlabels">
									<td>
										Date of Departure
										<br>
									</td>
									<td>
										<html:text property="deparDate" styleId="txtItemcreatedon"
											size="15" disabled="disabled" value="<%=deparDate%>"></html:text>
										<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
											id="itemcreatedon"
											onmousedown="insertDateFromCalendar(this.id,0);"
											style="margin-top: 3px" />
										<br>
									</td>
									<td align="right">
										Date Terminal arrival
										<br>
									</td>
									<td>
										<html:text property="terArrvlDate" styleId="txtItemcreated"
											size="15" value="<%=terArrvlDate%>"></html:text>
										<img src="<%=path%>/img/CalendarIco.gif" alt="cal" align="top"
											id="itemcreated"
											onmousedown="insertDateFromCalendar(this.id,0);"
											style="margin-top: 3px" />
										<br>
									</td>
								</tr>
							</table>

							<br style="margin-top: 5px" />




							<table width="100%">
								<tr class="tableHeadingNew">
									For Imports Only
								</tr>
								<tr class="textlabels">
									<td style="font: bold">
										Goverment Sched D/K Code
										<br>
									</td>
								</tr>


								<table border="0">
									<tr class="textlabels">
										<td width="26%">
											Origin
											<br>
										</td>
										<td>
											<input name="origin" id="origin" size="15"
												value="<%=origin%>"  onkeydown="getOriginTerminalName(this.value)"/>
											<dojo:autoComplete formId="inlandVoyageForm"
												textboxId="origin"
												action="<%=path%>/actions/getTerminal.jsp?tabName=INLAND_VOYAGE&from=1" />

										</td>
										<td align="right">
											Origin Name
											<br>
										</td>
										<td>
											<input name="originName" id="originName" size="15"
												value="<%=originName%>" onkeydown="getOriginTerminalNumber(this.value)"/>
											<dojo:autoComplete formId="inlandVoyageForm"
												textboxId="originName"
												action="<%=path%>/actions/getTerminalName.jsp?tabName=INLAND_VOYAGE&from=1" />

										</td>
									</tr>



									<tr class="textlabels">
										<td>
											Destination
											<br>
										</td>
										<td>
											<input name="destination" id="destination"
												value="<%=destination%>" size="15" onkeydown="getDestinationName(this.value)" />
											<dojo:autoComplete formId="inlandVoyageForm"
												textboxId="destination"
												action="<%=path%>/actions/getPorts.jsp?tabName=INLAND_VOYAGE&from=4" />
										</td>
										<td align="right">
											Destination Name
											<br>
										</td>
										<td>

											<input name="destinationName" id="destinationName" size="15"
												onkeydown="getDestinationNumber(this.value)"
												value="<%=destinationName%>" />
											<dojo:autoComplete formId="inlandVoyageForm"
												textboxId="destinationName"
												action="<%=path%>/actions/getPorts.jsp?tabName=INLAND_VOYAGE&from=5" />
										
										</td>
									</tr>
								</table>



								<table>
									<tr class="textlabels">
										<td style="font: bold">
											I.T.Port to print on Arrival Notice if any other Destination
											above
											<br>
										</td>
									</tr>
								</table>


								<table border="0" width="53%">
									<tr class="textlabels">
										<td width="28%">
											Opt. I.T.Port
											<br>
										</td>
										<td>
										
										    <input name="optITPort" id="optITPort"
												value="<%=optITPort%>" size="15" onkeydown="getItPortName(this.value)" />
											<dojo:autoComplete formId="inlandVoyageForm"
												textboxId="optITPort"
												action="<%=path%>/actions/getPorts.jsp?tabName=INLAND_VOYAGE&from=6" />
										</td>
										<td align="right">
											Port Name
											<br>
										</td>
										<td>
										         <input name="portName" id="portName"
												value="<%=portName%>" size="15" onkeydown="getItPortNo(this.value)" />
											<dojo:autoComplete formId="inlandVoyageForm"
												textboxId="portName"
												action="<%=path%>/actions/getPorts.jsp?tabName=INLAND_VOYAGE&from=7" />
										
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											Vessel#
											<br>
										</td>
										<td>
										    <input type="text" name="vesselNum" id="vesselNum"
												 value="<%=vesselNum%>"
												size="15" onkeydown="getVesselName(this.value)"/>
											<dojo:autoComplete formId="inlandVoyageForm"
												textboxId="vesselNum"
												action="<%=path%>/actions/getVesselNo.jsp?tabName=INLAND_VOYAGE&from=1" />
										   
										</td>
										<td align="right">
											Vessel Name
											<br>
										</td>
										<td>
										    <input type="text" name="vesselName" id="vesselName"
												 value="<%=vesselName%>" onkeydown="getVesselNo(this.value)"
												size="15" />
											<dojo:autoComplete formId="inlandVoyageForm"
												textboxId="vesselName"
												action="<%=path%>/actions/getVesselName.jsp?tabName=INLAND_VOYAGE&from=0" />
										   
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											S.S. Voyage#
											<br>
										</td>
										<td>
											<html:text property="ssVoyageNo" size="15"
												value="<%=ssVoyageNo%>"></html:text>
											<br>
										</td>
									</tr>
								</table>
							</table>
						</td>
					</tr>
				</table>
			</table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
			<script>disabled('<%=modify%>','<%=message%>')</script>
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

