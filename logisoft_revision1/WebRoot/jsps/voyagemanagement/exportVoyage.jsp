<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java"
	import="java.text.DateFormat,com.gp.cong.logisoft.domain.*,java.text.DecimalFormat,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.VoyageMaster,com.gp.cong.logisoft.domain.VoyageExport,java.text.ParseException,java.util.*, java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.FclContainertypeCharges,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,java.util.List,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,com.gp.cong.logisoft.domain.FclBuyCost"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<jsp:useBean id="fclrecordform"
	class="com.gp.cong.logisoft.struts.ratemangement.form.AddFCLRecordsForm"
	scope="request" />
<%
DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");


VoyageExport voyageExport=new VoyageExport();

String eciVoyage="";
String flightSsVoyage="";
String SslBookingNo="";
String transShipments="";
String currentSequenceNo="";
String terminalNumber="";
String terminalName="";
String destSheduleNumber="";
String destAirportname="";
String agentForVoyage="";
String truckingInfo="";
String podNo="";
String podName="";
String pierNo="";
String vesselNo="";
String vesselName="";
String lineNo="";
String lineName="";
String time="";
String result="";
String modify="";
String message="";
String unitType="";
String records="";
List unittypelist=new ArrayList();
List carriertypelist=new ArrayList();
//List recordsList=new ArrayList();
List vyge=new ArrayList();
String deliveryCutoffDate="";
String sailDate="";
String showOnSailingSchedule="";
String transitDaysOverride="";
List unitlist=new ArrayList();
String mesg="";
String mesg1="";
String unitType1="";
//for edit
AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setShowOnSailingSchedule("Y");
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airRatesBean.setShowOnSailingSchedule(airRatesBean.getShowOnSailingSchedule());
}

request.setAttribute("airRatesBean",airRatesBean);
if(session.getAttribute("voyageExport1")!=null){

voyageExport=(VoyageExport)session.getAttribute("voyageExport1");





if(voyageExport.getClosed()!=null)
{
String s=voyageExport.getClosed();
if(s.equals("c"))
{
mesg="CLOSED";

}

}
if(voyageExport.getAudited()!=null)
{
{
String w=voyageExport.getAudited();
if(w.equals("A"))
{
mesg1="AUDITED";
}

}
}




if(voyageExport.getInternalVoyage()!=null){
	eciVoyage=voyageExport.getInternalVoyage();
}
if(voyageExport.getFligtSsVoyage()!=null){
	flightSsVoyage=voyageExport.getFligtSsVoyage();
}

if(voyageExport.getSslBookingNo()!=null){
	SslBookingNo=voyageExport.getSslBookingNo();
}
if(voyageExport.getTransShipments()!=null){
	transShipments=voyageExport.getTransShipments();
}
if(voyageExport.getCurrentSequenceNo()!=null){
			currentSequenceNo=(voyageExport.getCurrentSequenceNo().toString());
			
		}
		if(voyageExport.getTime()!=null){
			time=voyageExport.getTime();
		}
		if(voyageExport.getVesselNo()!=null)
		{
		vesselNo=voyageExport.getVesselNo().getCode();	
		vesselName=voyageExport.getVesselNo().getCodedesc();
		}
if(voyageExport.getShowSailingSchedule()!=null){
		
			airRatesBean.setShowOnSailingSchedule(voyageExport.getShowSailingSchedule());
		}
		if(voyageExport.getTransitDaysOverride()!=null){
			transitDaysOverride=voyageExport.getTransitDaysOverride();
		}
		if(voyageExport.getAgentForVoyage()!=null){
			agentForVoyage=voyageExport.getAgentForVoyage();
		}
		if(voyageExport.getTruckingInfo()!=null){		  
			truckingInfo=voyageExport.getTruckingInfo();
		}
		if(voyageExport.getPierNo()!=null){
			pierNo=voyageExport.getPierNo();
		}	
		if(voyageExport.getDeliveryCutOffDate()!=null){
			
			try
{ 
Date deparDate=voyageExport.getDeliveryCutOffDate();

DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
deliveryCutoffDate=df1.format(deparDate);

}
catch (Exception e)
{
e.printStackTrace();
} 
			
		}
		if(voyageExport.getSailDate()!=null){
			
			try
{ 
Date deparDat=voyageExport.getSailDate();

DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
sailDate=df1.format(deparDat);

}
catch (Exception e)
{
e.printStackTrace();
} 
		}	
		
		if(voyageExport.getUnitType()!=null){
			unitType=voyageExport.getUnitType();
		}
		
		if(voyageExport.getOriginTerminal()!=null)
	{
	terminalNumber=voyageExport.getOriginTerminal().getTrmnum();	
	terminalName=voyageExport.getOriginTerminal().getTerminalLocation();
	}
	if(voyageExport.getDestinationPort()!=null)
	{
	destSheduleNumber=voyageExport.getDestinationPort().getShedulenumber();
	destAirportname=voyageExport.getDestinationPort().getPortname();
	}
	
	/*if(voyageMaster.getComNum()!=null)
	{
	comCode=voyageMaster.getComNum().getCode();
	comDesc=voyageMaster.getComNum().getCodedesc();
	}*/
	if(voyageExport.getLineNo()!=null){
	lineNo=voyageExport.getLineNo().getCarriercode();
	
	lineName=voyageExport.getLineNo().getCarriername();
	
	}
	
	if(voyageExport.getVesselNo()!=null)
		{
		vesselNo=voyageExport.getVesselNo().getCode();
		vesselName=voyageExport.getVesselNo().getCodedesc();
		}
		
		if(voyageExport.getPortOfDischarge()!=null)
		{
			podNo=voyageExport.getPortOfDischarge().getShedulenumber();
			podName=voyageExport.getPortOfDischarge().getPortname();
		}
}



//ends for edit
if(request.getAttribute("voyageExprt")!=null){

voyageExport=(VoyageExport)request.getAttribute("voyageExprt");


if(voyageExport.getInternalVoyage()!=null){
	eciVoyage=voyageExport.getInternalVoyage();
}
if(voyageExport.getFligtSsVoyage()!=null){
	flightSsVoyage=voyageExport.getFligtSsVoyage();
}

if(voyageExport.getSslBookingNo()!=null){
	SslBookingNo=voyageExport.getSslBookingNo();
}
if(voyageExport.getTransShipments()!=null){
	transShipments=voyageExport.getTransShipments();
}
if(voyageExport.getCurrentSequenceNo()!=null){
			currentSequenceNo=(voyageExport.getCurrentSequenceNo().toString());
		
		}
		if(voyageExport.getTime()!=null){
			time=voyageExport.getTime();
		}
if(voyageExport.getShowSailingSchedule()!=null){
		
			showOnSailingSchedule=voyageExport.getShowSailingSchedule();
		}
		if(voyageExport.getTransitDaysOverride()!=null){
			transitDaysOverride=voyageExport.getTransitDaysOverride();
		}
		if(voyageExport.getAgentForVoyage()!=null){
			agentForVoyage=voyageExport.getAgentForVoyage();
		}
		if(voyageExport.getTruckingInfo()!=null){
		
			truckingInfo=voyageExport.getTruckingInfo();
		}
		if(voyageExport.getPierNo()!=null){
			pierNo=voyageExport.getPierNo();
		}	
		if(voyageExport.getDeliveryCutOffDate()!=null){
			
			try
{ 
Date deparDate=voyageExport.getDeliveryCutOffDate();

DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
deliveryCutoffDate=df1.format(deparDate);

}
catch (Exception e)
{
e.printStackTrace();
} 
			
		}
		if(voyageExport.getSailDate()!=null){
			
			try
{ 
Date deparDat=voyageExport.getSailDate();

DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
sailDate=df1.format(deparDat);

}
catch (Exception e)
{
e.printStackTrace();
} 
		}	
		
		if(voyageExport.getUnitType()!=null){
			unitType=voyageExport.getUnitType();
		}
		if(voyageExport.getVesselNo()!=null)
		{
		vesselNo=voyageExport.getVesselNo().getCode();
		vesselName=voyageExport.getVesselNo().getCodedesc();
		}
		
		if(voyageExport.getDestinationPort()!=null)
		{
			podNo=voyageExport.getPortOfDischarge().getShedulenumber();
			podName=voyageExport.getPortOfDischarge().getPortname();
		}
}


if(request.getAttribute("voyagemessage")!=null){
message=(String)request.getAttribute("voyagemessage");
}

if(session.getAttribute("message")!=null){
message=(String)session.getAttribute("message");
}
DecimalFormat df=new DecimalFormat("0.00");

modify = (String) request.getAttribute("modifyforvoyageRates");




DBUtil dbutil=new DBUtil();
if(session.getAttribute("voyagerate")!=null){
	result=(String)session.getAttribute("voyagerate");	
	}


if(session.getAttribute("exportvoyages")!=null){

		voyageExport=(VoyageExport)session.getAttribute("exportvoyages");	
		if(voyageExport.getInternalVoyage()!=null){
			eciVoyage=voyageExport.getInternalVoyage();
			
		}
		if(voyageExport.getShowSailingSchedule()!=null){
		
			showOnSailingSchedule=voyageExport.getShowSailingSchedule();
		}
		if(voyageExport.getFligtSsVoyage()!=null){
			flightSsVoyage=voyageExport.getFligtSsVoyage();
		}
		
		if(voyageExport.getSslBookingNo()!=null){
			SslBookingNo=voyageExport.getSslBookingNo();
	
		}
	
		if(voyageExport.getCurrentSequenceNo()!=null){
			currentSequenceNo=(voyageExport.getCurrentSequenceNo().toString());	
		}
		if(voyageExport.getTime()!=null){
			time=voyageExport.getTime();
		}
		if(voyageExport.getTransShipments()!=null){
			transShipments=voyageExport.getTransShipments();
		}
		if(voyageExport.getTransitDaysOverride()!=null){
			transitDaysOverride=voyageExport.getTransitDaysOverride();
		}
		if(voyageExport.getAgentForVoyage()!=null){
			agentForVoyage=voyageExport.getAgentForVoyage();
		}
		if(voyageExport.getSailDate()!=null)
		{
		     DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
			sailDate=df1.format(voyageExport.getSailDate());
		}
		if(voyageExport.getDeliveryCutOffDate()!=null)
		{
		         DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
				deliveryCutoffDate=df1.format(voyageExport.getDeliveryCutOffDate());
		
		}
		if(voyageExport.getUnitType()!=null)
		{

		      unitType1=voyageExport.getUnitType();
		       if(voyageExport.getUnitType()=="B")
		       {
		              unitType1="B";
		       }
		       else if(voyageExport.getUnitType()=="C")
		       {
		              				unitType1="C";
		       			
		       }
		      else if(voyageExport.getUnitType()=="T")
		       {
		              				unitType1="T";
		       			
		       }
		}
		if(voyageExport.getShowSailingSchedule()!=null)
		 {
		 String yes=voyageExport.getShowSailingSchedule();
		 if(yes.equals("Y"))
		 {
		 
		 airRatesBean.setShowOnSailingSchedule("Y");
		 }
		 else
		 {
		      airRatesBean.setShowOnSailingSchedule("N");
		 }
		}
		if(voyageExport.getTruckingInfo()!=null){
			truckingInfo=voyageExport.getTruckingInfo();
		}
		if(voyageExport.getPierNo()!=null){
			pierNo=voyageExport.getPierNo();
		}
		/*if(unittypelist != null)
		{
		unittypelist=dbUtil.getUnitFCLUnitypeTest(new Integer(38),"yes","Select Unit code",li);
		request.setAttribute("unittypelist",unittypelist);
		}*/
}	

	/*if(session.getAttribute("exportvoyages")!=null)
		{
		
		//voyageExport=(VoyageExport)session.getAttribute("exportvoyages");
		//List recordsList=(List)voyageExport;
		List recordsList = Arrays.asList(session.getAttribute("exportvoyages"));
		for(int i=0;i<recordsList.size();i++){
			   
   				VoyageExport voyage=(VoyageExport)recordsList.get(i);
   				
   				vyge.add(voyage);
   				
   				}
	 	   }
	 	   
	 	   if(unittypelist != null)
		{
		unittypelist=dbutil.getUnitypeTest(new Integer(38),"yes","Select Unit code",vyge);
		request.setAttribute("unittypelist",unittypelist);
		}*/
		



%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


	//String editPath=path+"/exportVoyage.do";
	

	if(session.getAttribute("addvoyagerecords")!=null)
	{
	
	voyageExport=(VoyageExport)session.getAttribute("addvoyagerecords");
	if(voyageExport.getOriginTerminal()!=null)
	{
	terminalNumber=voyageExport.getOriginTerminal().getTrmnum();

	terminalName=voyageExport.getOriginTerminal().getTerminalLocation();
	}
	if(voyageExport.getDestinationPort()!=null)
	{
	destSheduleNumber=voyageExport.getDestinationPort().getShedulenumber();
	destAirportname=voyageExport.getDestinationPort().getPortname();
	}
	
	/*if(voyageMaster.getComNum()!=null)
	{
	comCode=voyageMaster.getComNum().getCode();
	comDesc=voyageMaster.getComNum().getCodedesc();
	}*/
	if(voyageExport.getLineNo()!=null){
	lineNo=voyageExport.getLineNo().getCarriercode();
	lineName=voyageExport.getLineNo().getCarriername();
	
	}
	records="Norecords";
	}
	if(session.getAttribute("exportvoyageport")!=null)
	{
		voyageExport=(VoyageExport)session.getAttribute("exportvoyageport");
		if(voyageExport.getDestinationPort()!=null)
		{
			podNo=voyageExport.getDestinationPort().getShedulenumber();
			podName=voyageExport.getDestinationPort().getPortname();
		}
	}
	
	
	if(session.getAttribute("addvesselrecords")!=null)
	{
		voyageExport=(VoyageExport)session.getAttribute("addvesselrecords");
		if(voyageExport.getVesselNo()!=null)
		{
		vesselNo=voyageExport.getVesselNo().getCode();
		vesselName=voyageExport.getVesselNo().getCodedesc();
		}
		}
	if(session.getAttribute("voyagerecords")!=null)
	{
		voyageExport=(VoyageExport)session.getAttribute("voyagerecords");
		if(voyageExport.getLineNo()!=null)
		{
		lineNo=voyageExport.getLineNo().getCarriercode();
		lineName=voyageExport.getLineNo().getCarriername();
		}
		}
	
if(session.getAttribute("view")!=null){
modify=(String)session.getAttribute("view");
}
if(session.getAttribute("closed")!=null)
{

mesg="CLOSED";
}

if(session.getAttribute("audited")!=null)
{
mesg1="AUDITED";
}	
	
	
	
	String editPath=path+"/exportVoyage.do";    
	
if(session.getAttribute("addvesselrecords1")!=null)
{
session.removeAttribute("addvesselrecords1");

}


if(session.getAttribute("changevoyage")!=null)
{
	ChangeVoyage changevoyage=(ChangeVoyage)session.getAttribute("changevoyage");
	if(changevoyage.getNewvesselNo()!=null)
	{
		GenericCode vessel=(GenericCode)changevoyage.getNewvesselNo();
		vesselNo=vessel.getCode();
		vesselName=vessel.getCodedesc();
	}
	if(changevoyage.getNewSailDate()!=null)
	{

		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		sailDate=df1.format(changevoyage.getNewSailDate());

	}
	if(changevoyage.getNewPier()!=null)
	{
		pierNo=changevoyage.getNewPier();
	}
	if(changevoyage.getNewFlightSsVoyage()!=null)
	{
		flightSsVoyage=changevoyage.getNewFlightSsVoyage();
	}
	if(changevoyage.getNewLineNo()!=null)
		{

			lineNo=changevoyage.getNewLineNo().getCarriercode();
			lineName=changevoyage.getNewLineNo().getCarriername();
		}

}

if(session.getAttribute("voyageExportPopup")!=null)
{
    VoyageExport export=new VoyageExport();
    
    if(export.getInternalVoyage()!=null)
    {
        eciVoyage=export.getInternalVoyage();
    
    }
    //deliverycuttoff date
    if(export.getDeliveryCutOffDate()!=null)
    { 
        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		deliveryCutoffDate=df1.format(export.getDeliveryCutOffDate());
    }
    //time
    if(export.getTime()!=null)
    {
         time=export.getTime();       
    }
    //sail date
    if(export.getSailDate()!=null)
    {   
        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		sailDate=df1.format(export.getSailDate());
    }
    //pier
    if(export.getPierNo()!=null)
    {
           pierNo=export.getPierNo();
    
    }
    //fllight ss voyage
    if(export.getFligtSsVoyage()!=null)
    {
            flightSsVoyage=export.getFligtSsVoyage();
    }
    //ssl booking number
    if(export.getSslBookingNo()!=null)
    {
              SslBookingNo=export.getFligtSsVoyage();
    }
    //current sequence
    if(export.getCurrentSequenceNo()!=null)
    {
              currentSequenceNo=String.valueOf(export.getCurrentSequenceNo());
    }
    //transhipment
    if(export.getTransShipments()!=null)
    {
              transShipments=export.getTransShipments();
    }
    //trucking information
    if(export.getTruckingInfo()!=null)
    {     
              truckingInfo=export.getTruckingInfo();
    }
}

if(session.getAttribute("voy")!=null)
{
VoyageExport voy=(VoyageExport)session.getAttribute("voy");
String s=voy.getShowSailingSchedule();

	if(s.equals("Y")){
		airRatesBean.setShowOnSailingSchedule("Y");
	}else{
		airRatesBean.setShowOnSailingSchedule("N");
	}
}

%>


<html>
	<head>
		<title>Export Voyage</title>
		<%@include file="../includes/baseResources.jsp"%>

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
				
       	function submit1()
		{
			//alert("save");
			document.exportVoyageForm.buttonValue.value="save";
			//document.exportVoyageForm.costtype.value="0";
			document.exportVoyageForm.submit();
		}
		function submit2()
		{
			//alert("save");
			document.exportVoyageForm.buttonValue.value="edit";
			//document.exportVoyageForm.costtype.value="0";
			document.exportVoyageForm.submit();
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
			
			document.exportVoyageForm.buttonValue.value="popup";
			
			document.exportVoyageForm.submit();
			return false;
			}
		function costtypechange()
		{
		
			if(document.exportVoyageForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else{
				document.exportVoyageForm.buttonValue.value="costType";
				document.exportVoyageForm.submit();
			}
		}
		
		function addForm(val1)
		{
			//alert(val1);
			if(document.exportVoyageForm.costcode.value!=null&&document.exportVoyageForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else if(document.exportVoyageForm.costtype.value!=null&&document.exportVoyageForm.costtype.value=="0")
			{
				alert("Please select Cost Type !");
			}
			else if(val1!=null&&val1==11300)
			{
				 if(document.exportVoyageForm.unittype.value!=null&&document.exportVoyageForm.unittype.value=="0")
					{
					alert("Please select Unit Type !");
					}
					else{
					document.exportVoyageForm.buttonValue.value="add";
					document.exportVoyageForm.submit();
					}
			}
			else if(val1!=null&&val1==11306)
			{
					if(document.exportVoyageForm.range.value!=null&&document.exportVoyageForm.range.value=="0")
					{
						alert("Please select Wight Range !");
					}else{
					document.exportVoyageForm.buttonValue.value="add";
					document.exportVoyageForm.submit();
					}
			}
			else{
			
				document.exportVoyageForm.buttonValue.value="add";
				document.exportVoyageForm.submit();
			}
		}
		function delete1()
		{
			document.exportVoyageForm.buttonValue.value="delete";
			document.exportVoyageForm.submit();
		}
		function saveForm1(val,val2)
		{
		
			
			if(val!=null && val!="save")
			{
			document.exportVoyageForm.buttonValue.value="edit";
			}
			else{
				document.exportVoyageForm.buttonValue.value="save";
			}
			if(val2!=""){
			//alert(val2);
			
			}
			else{
			
			document.exportVoyageForm.submit();
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
				document.exportVoyageForm.buttonValue.value="cancel";
				document.exportVoyageForm.submit();
				}
			}else{
			document.exportVoyageForm.buttonValue.value="cancel";
			document.exportVoyageForm.submit();
			}
		}
		
		//ExportVoyage
		function save1(val1){
		
			
			if(document.exportVoyageForm.eciVoyage.value=="")
			{
				alert("Please Enter ECI Voyage");
				return false;
			}
			var netie=navigator.appName;
			
 			 if(val1=='edit'){
				document.exportVoyageForm.buttonValue.value="edit";
 			}
 			else if(val1=='save'){
 				document.exportVoyageForm.buttonValue.value="save";
 			}
			
			document.exportVoyageForm.submit();
 		
   				
 				
			}
		
		
		function cancel2(val1,val3)
		{
			 if(val1!=""){
		 	var result = confirm("Do  you want to Add Rates ??");
		 	
		 	 if(result)
				{ 
					save1(val3);//skg
	   			}	
			 else
			  {
			  		
	   				document.exportVoyageForm.buttonValue.value="savecancel";
	   				document.exportVoyageForm.submit();
			  }
			  }
				
		
 			
		}
		function confirmnote()
	{
		
		document.exportVoyageForm.buttonValue.value="note";
    	document.exportVoyageForm.submit();
   	}	
   	
   	function delete1()	{
   			//alert("inside delete");
			document.exportVoyageForm.buttonValue.value="delete";
 			document.exportVoyageForm.submit();
			}
			
			function cancel1(val){
			//alert("ABC"+val);
			if(val==0 || val==3){
			//alert("ABC"+val);
			document.exportVoyageForm.buttonValue.value="cancel";
			}
			else{
			document.exportVoyageForm.buttonValue.value="cancel";
				}
 			document.exportVoyageForm.submit();
			}
		//ExportVoyage Ends
		
		function noteForm()
		{
			document.exportVoyageForm.buttonValue.value="note";
			document.exportVoyageForm.submit();
		}
		
		function disabled(val1,val2,val3)
		 
		{
			
			if(val3!="" && val3=="C")
			{
			    document.exportVoyageForm.singleSelect.selectedIndex=1;
			
			}
			else if(val3!="" && val3=="T")
			{
			 document.exportVoyageForm.singleSelect.selectedIndex=0;
			}
			else if(val3!="" && val3=="B")
			{
			 document.exportVoyageForm.singleSelect.selectedIndex=2;
			}
		
			if(val1!=""&&(val1 == 0 || val1== 3))
				{
		  	  	 	var imgs = document.getElementsByTagName('img');
	       	     	for(var k=0; k<imgs.length; k++)	
	       	     	{
	   		 			if(imgs[k].id != "cancel"&&imgs[k].id != "note")	 
	   		 			{
	   		 				imgs[k].style.visibility = 'hidden';
	   					}
	   				}
		   			var input = document.getElementsByTagName("input");
			   		for(i=0; i<input.length; i++)	
			   		{
			  			input[i].readOnly=true;
					 }
		  	 		var textarea = document.getElementsByTagName("textarea");
			  	 	for(i=0; i<textarea.length; i++)
			  	 	{
			 			textarea[i].readOnly=true;		 					
					}
	   				var select = document.getElementsByTagName("select");
	   		   		for(i=0; i<select.length; i++)	
	   		   		{
				 		select[i].disabled=true;
						select[i].style.backgroundColor="blue";
		  		  	}
		  		  	 document.getElementById("save").style.visibility = 'hidden';
		  		  	 	  document.getElementById("delete").style.visibility = 'hidden';
		  		  	 	   document.getElementById("audit").style.visibility = 'hidden';
   		  		  	 	   document.getElementById("close").style.visibility = 'hidden';
	  	 		}
		  	 	if(val1 == 1)	 
		  	 	{
	  	 		 	  document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 		if(val1==3 && val2!="")
	  	 		{
					//alert(val2);
				}		
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
//					tablerowobj.bgColor='#FFB7DB';
					
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

				if(document.exportVoyageForm.buttonValue.value=="searchall")
				{
		  	 		var x=document.getElementById('includedcomtable').rows[0].cells;
		    		x[0].className="sortable sorted order1";
				} 
				if(document.exportVoyageForm.buttonValue.value=="search")
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

				if(document.exportVoyageForm.buttonValue.value=="searchall")
				{
		  	 		var x=document.getElementById('includedtable').rows[0].cells;
		     		x[0].className="sortable sorted order1";
				} 
				if(document.exportVoyageForm.buttonValue.value=="search")
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

	
		function comTest(val1){
			
			document.exportVoyageForm.buttonValue.value="index";
			document.exportVoyageForm.index.value=val1;
			document.exportVoyageForm.submit();
			mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+val1,"","width=800,height=200");
       		 mywindow.moveTo(200,180);		
		}
		function titleLetter(ev)
	{
		if(event.keyCode==9){
		//document.exportVoyageForm.buttonValue.value="search";
			document.exportVoyageForm.submit();
	window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=exportvoyage&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getSSLineName(ev)
	{
		if(event.keyCode==9){
		
			document.exportVoyageForm.submit();
	window.open("<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button=lineNo&sslinename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getVoyage(ev)
	{
	
	 if(event.keyCode==9)
	 {
	
	   window.open("<%=path%>/jsps/voyagemanagement/searchVessel.jsp?button=addvessel&vesselno="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	     document.exportVoyageForm.buttonValue.value="popup";
	     document.exportVoyageForm.submit();
	 }
	 
	}
	function getVoyage1(ev)
	{
        
	 if(event.keyCode==9)
	 {
	
	   window.open("<%=path%>/jsps/voyagemanagement/searchVessel.jsp?button=addvessel&vesselname="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	     document.exportVoyageForm.buttonValue.value="popup";
	     document.exportVoyageForm.submit();
	 }
	    
	}
	function  getPod(ev)
	{
		 if(event.keyCode==9)
	 {
	
	   window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=exportvoyageport&percode="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	   document.exportVoyageForm.buttonValue.value="popup";
	     document.exportVoyageForm.submit();
	 
	 }
	     
	
	}
		function  getPod1(ev)
	{
		 if(event.keyCode==9)
	 {
	
	    window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=exportvoyageport&percodename="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	      document.exportVoyageForm.buttonValue.value="popup";
	     document.exportVoyageForm.submit();
          	 
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
		    	 document.exportVoyageForm.buttonValue.value="closed";
		    	document.exportVoyageForm.submit();
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
		    	document.exportVoyageForm.buttonValue.value="audit";
		    	document.exportVoyageForm.submit();
		    }
		}
	}
		function hello()
	{
	   
	   mywindow=window.open("<%=path%>/jsps/voyagemanagement/exportVoyages.jsp?","","width=800,height=400,scrollbars=yes");
       	mywindow.moveTo(200,180);		
	
	
	}
	function add1()
	{
	    
	    if(IsNumeric(document.exportVoyageForm.currentSequenceNo.value)==false)
  		{
    	alert("current sequence number should be Numeric.");
  		document.exportVoyageForm.currentSequenceNo.value="";
   	 	document.exportVoyageForm.currentSequenceNo.focus();
   	 	return;
   		}
	
	}
     function getExport()
     {
     window.location.href="<%=path%>/jsps/voyagemanagement/exportVoyage.jsp";
     }
     
     function getVesselName(ev){ 
   	 		document.getElementById("vesselName").value="";
   			if(event.keyCode==9 || event.keyCode==13){
	 			var params = new Array();
	 			params['requestFor'] = "VesselName";
	 			params['vesselNo'] = document.exportVoyageForm.vessel.value;
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
   	 		document.getElementById("vessel").value="";
   			if(event.keyCode==9 || event.keyCode==13){
	 			var params = new Array();
	 			params['requestFor'] = "VesselNumber";
	 			params['vesselName'] = document.exportVoyageForm.vesselName.value;
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
       		document.getElementById("vessel").value=data.VesselNo;
       		}
       	}
    
             function getPodName(ev){ 
   	 		document.getElementById("podName").value="";
   			if(event.keyCode==9 || event.keyCode==13){
	 			var params = new Array();
	 			params['requestFor'] = "DestAirportname";
	 			params['scheduleNumber'] = document.exportVoyageForm.podNo.value;
	  			var bindArgs = {
		  		url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
		  		error: function(type, data, evt){alert("error");},
		  		mimetype: "text/json",
		  		content: params
	 		};
	 		
	 	var req = dojo.io.bind(bindArgs);
	 	
	 	dojo.event.connect(req, "load", this, "populatePodName");
    	}
	}
    function populatePodName(type, data, evt) {
  		if(data){
  			document.getElementById("podName").value=data.destAirportname;
       		}
       	}
                 function getPodName1(ev){ 
   	 		document.getElementById("podNo").value="";
   			if(event.keyCode==9 || event.keyCode==13){
	 			var params = new Array();
	 			params['requestFor'] = "ScheduleNumber";
	 			params['destAirportName'] = document.exportVoyageForm.podName.value;
	  			var bindArgs = {
		  		url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
		  		error: function(type, data, evt){alert("error");},
		  		mimetype: "text/json",
		  		content: params
	 		};
	 		
	 	var req = dojo.io.bind(bindArgs);
	 	
	 	dojo.event.connect(req, "load", this, "populatePodName1");
    	}
	}
    function populatePodName1(type, data, evt) {
  		if(data){
  			document.getElementById("podNo").value=data.destSheduleNumber;
       		}
       	}
     
     
		</script>
		<%@include file="../includes/resources.jsp"%>
	</head>
	<body class="whitebackgrnd">
		<html:form action="/exportVoyage" scope="request">
			<font color="blue" size="4"></font>

			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew">
				<tr class="tableHeadingNew">
					Voyage Details
				</tr>
				<td>

					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						id="records">

						<tr class="textlabels">
							<td width="20%" align="left">
								&nbsp;
							</td>

							<% 
 
  if(result!=null&&result.equals("save")){

%>
							<td width="78" align="left" class="headerbluelarge">
								<input type="button" value="Save" onclick="save1('<%=result%>')"
									class="buttonStyleNew" />
							</td>
							<td>
								<input type="button" value="Go Back"
									onclick="cancel2('<%=records%>','<%=result%>')"
									class="buttonStyleNew" />
							</td>

							<% }
  
  if(result!=null&&result.equals("edit")){%>
							<td align="right">
								<input type="button" id="audit" value="Audit"
									onclick="audited()" class="buttonStyleNew" />


								<input type="button" id="close" value="Close" onclick="closed()"
									class="buttonStyleNew" />


								<%--            <input type="button" value="ChangeVoyageInfo" onclick="hello()" class="buttonStyleNew" style="width:110px"/>--%>
								<input type="button" id="save" value="Save"
									onclick="save1('<%=result%>')" class="buttonStyleNew" />
								<input type="button" id="delete" value="Delete"
									onclick="delete1()" class="buttonStyleNew" />


								<input type="button" value="ChangeVoyageInfo"
									onclick="return GB_show('Change Voyage Info','<%=path%>/jsps/voyagemanagement/exportVoyages.jsp?',350,690)"
									<%if(session.getAttribute("changevoyage")!=null){session.removeAttribute("changevoyage");} %>
									class="buttonStyleNew" style="width: 110px" />


								<input type="button" id="cancel" value="Go Back"
									onclick="cancel1('<%=modify%>')" class="buttonStyleNew" />




								<input type="button" value="Note" onclick="confirmnote()"
									class="buttonStyleNew" />

							</td>
							<%
}if(session.getAttribute("message")!=null){
session.removeAttribute("message");}%>


						</tr>
						<tr>

							<td id="tdaudit">
								<font color="blue"><%=mesg%> &nbsp;&nbsp;&nbsp;&nbsp; <%=mesg1%></font>
							</td>
						</tr>

					</table>


					<table width="100%" border="0" cellpadding="0" cellspacing="0">

						<tr class="tableHeadingNew">
							Voyage
						</tr>
						<td height="12" colspan="3">
							&nbsp;&nbsp;
						</td>
						<tr></tr>



					</table>


					<table width="600" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<table width="200" border="0" cellpadding="0" cellspacing="0">


									<tr class="textlabels">
										<td>
											ECI Voyage &nbsp;&nbsp;
										</td>
										<td align="left">
											<html:text property="eciVoyage" size="12" disabled="disabled"
												value="<%=eciVoyage%>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											Delivery CutOff Date
										</td>
										<td>
											<html:text property="deliveryCutoffDate"
												styleId="txtitemcreatedon" size="9"
												value="<%=deliveryCutoffDate%>"></html:text>
											<img src="<%=path%>/img/CalendarIco.gif" alt="cal"
												align="top" id="itemcreatedon"
												onmousedown="insertDateFromCalendar(this.id,0);" />
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											Time
										</td>
										<td>
											<html:text property="time" size="12" value="<%=time %>"></html:text>
										</td>

									</tr>
									<tr class="textlabels">
										<td>
											Sail Date
										</td>
										<td>
											<html:text property="sailDate" styleId="txtItemcreated"
												size="9" value="<%=sailDate%>"></html:text>
											<img src="<%=path%>/img/CalendarIco.gif" alt="cal"
												align="top" id="itemcreated"
												onmousedown="insertDateFromCalendar(this.id,0);" />
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											Flight/SS Voyage
										</td>
										<td>
											<html:text property="flightSsVoyage" size="12"
												value="<%=flightSsVoyage %>"></html:text>
										</td>
									</tr>

									<tr class="textlabels">
										<td>
											Current Sequence#
										</td>
										<td>
											<html:text property="currentSequenceNo" size="12"
												onkeyup="add1()" value="<%=currentSequenceNo%>"></html:text>
										</td>
									</tr>

									<tr class="textlabels">

										<td>
											Transit Days Override
										</td>
										<td valign="top">
											<html:text property="transitDaysOverride" size="12"
												maxlength="2" value="<%=transitDaysOverride%>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											Pier#
										</td>


										<td valign="top">
											<html:text property="pierNo" size="12" value="<%=pierNo %>"></html:text>
										</td>

									</tr>


								</table>
							</td>
							<td>
								<table width="200" border="0" cellpadding="0" cellspacing="0">


									<tr class="textlabels">
										<td align="right" width="14%">
											Loading Trm&nbsp;&nbsp;&nbsp;
										</td>

										<td>
											<html:text property="loadingTerminal" size="12"
												readonly="true" styleId="loadingTerminal"
												disabled="disabled" value="<%= terminalNumber%>"></html:text>
										</td>

									</tr>
									<tr class="textlabels">
										<td align="right">
											Port#&nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:text property="port" readonly="true" size="12"
												value="<%=destSheduleNumber %>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											Vessel#&nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<input type="text" name="vessel" id="vessel"
												onkeydown="getVesselName(this.value)" value="<%=vesselNo %>"
												size="12" />
											<dojo:autoComplete formId="exportVoyageForm"
												textboxId="vessel"
												action="<%=path%>/actions/getVesselNo.jsp?tabName=EXPORT_VOYAGE&amp;from=0" />
										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											Line#&nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:text size="12" property="lineNo" readonly="true"
												value="<%=lineNo %>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											Unit Type&nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:select property="singleSelect" value="<%= unitType%>">
												<html:option value="T">T=Trailer</html:option>
												<html:option value="C">C=Container</html:option>
												<html:option value="B">B=Both</html:option>
											</html:select>
										</td>
									</tr>

									<tr class="textlabels">
										<td align="right">
											POD#&nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<input type="text" name="podNo" id="podNo" value="<%=podNo%>"
												size="12" onkeydown="getPodName(this.value)"/>
											<dojo:autoComplete formId="exportVoyageForm"
												textboxId="podNo"
												action="<%=path%>/actions/getPorts.jsp?tabName=EXPORT_VOYAGE&from=2" />
										</td>
									</tr>

									<tr class="textlabels">
										<td align="right">
											Agent for this Voyage &nbsp;&nbsp;&nbsp;
										</td>
										<td valign="top">
											<html:text property="agentForVoyage" size="12"
												value="<%=agentForVoyage%>"></html:text>
										</td>

									</tr>
									<tr class="textlabels">
										<td>
											Show On Sailing Schedule&nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:radio property="showOnSailingSchedule" value="Y"
												name="airRatesBean"></html:radio>
											Y
											<html:radio property="showOnSailingSchedule" value="N"
												name="airRatesBean"></html:radio>
											N
										</td>

									</tr>

								</table>
							</td>
							<td>
								<table width="200" border="0" cellpadding="0" cellspacing="0">

									<tr class="textlabels">
										<td align="right">
											Loading Trm Name &nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:text property="loadingTerminal" size="14"
												readonly="true" styleId="loadingTerminalName"
												disabled="disabled" value="<%= terminalName%>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											port Name &nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:text property="portName" readonly="true"
												disabled="disabled" size="14" value="<%=destAirportname %>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											Vessel Name &nbsp;&nbsp;&nbsp;
										</td>
										<td>

											<input type="text" name="vesselName" id="vesselName"
												value="<%=vesselName %>" onkeydown="getVesselNo(this.value)"
												size="14" />
											<dojo:autoComplete formId="exportVoyageForm"
												textboxId="vesselName"
												action="<%=path%>/actions/getVesselName.jsp?tabName=EXPORT_VOYAGE&from=0" />

										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											Line Name &nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:text property="lineName" size="14" readonly="true"
												value="<%=lineName %>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											SSL Booking# &nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:text property="sslBookingNo" size="14"
												value="<%=SslBookingNo %>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											POD Name &nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<input type="text" name="podName" id="podName"
												value="<%=podName%>" size="14" onkeydown="getPodName1(this.value)" />
											<dojo:autoComplete formId="exportVoyageForm"
												textboxId="podName"
												action="<%=path%>/actions/getPorts.jsp?tabName=EXPORT_VOYAGE&from=3" />
										</td>
									</tr>
									<tr class="textlabels">
										<td align="right">
											Trans Shipments &nbsp;&nbsp;&nbsp;
										</td>
										<td>
											<html:text property="transShipments" size="14"
												value="<%=transShipments %>"></html:text>
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											&nbsp;
										</td>

									</tr>


								</table>
							</td>
						</tr>
					</table>
					<table width="600" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<table width="200" border="0" cellpadding="0" cellspacing="0">

									<tr class="textlabels">
										<td valign="top">
											Trucking Information&nbsp;&nbsp;
										</td>


										<td>
											<html:textarea property="truckingInfo" cols="20" rows="3"
												value="<%=truckingInfo%>"></html:textarea>
										</td>

									</tr>
								</table>
							</td>





						</tr>
					</table>
				</td>
			</table>

			<html:hidden property="buttonValue" styleId="buttonValue" />
			<script>disabled('<%=modify%>','<%=message%>','<%=unitType1%>')</script>
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

