<%@ page language="java" pageEncoding="ISO-8859-1"%>
<%@ page language="java"
	import="java.text.DateFormat,java.text.DecimalFormat,com.gp.cong.logisoft.beans.AirRatesBean,com.gp.cong.logisoft.domain.VoyageMaster,com.gp.cong.logisoft.domain.VoyageExport,java.text.ParseException,java.util.*, java.text.SimpleDateFormat,com.gp.cong.logisoft.domain.FclContainertypeCharges,com.gp.cong.logisoft.domain.GenericCode,com.gp.cong.logisoft.domain.FclBuyAirFreightCharges,com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.FclBuy,java.util.ArrayList,java.util.List,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.FclBuyCostTypeRates,com.gp.cong.logisoft.domain.FclBuyCost"%>
<%@ page language="java"
	import="com.gp.cong.logisoft.domain.ChangeVoyage"%>
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
	String eciVoyage="",flightSsVoyage="",SslBookingNo="",transShipments="",currentSequenceNo="",terminalNumber="",
	terminalName="",destSheduleNumber="",destAirportname="",agentForVoyage="",truckingInfo="", podNo="", podName="",
	pierNo="", vesselNo="",vesselName="",lineNo="",lineName="",time="",result="", modify="",message="",unitType="",
	records="",deliveryCutoffDate="",sailDate="",showOnSailingSchedule="",arrivalOrDeparture="",transitDaysOverride="",
	notify="",eci="",voyageReasonId="",vesselNo1="",vesselName1="",sailDate1="",pierNo1="",flightSsVoyage1="",lineNo1="",reasonCode="",reasonDesc="",lineName1="";;
	List unitlist=new ArrayList();
	List unittypelist=new ArrayList();
	List vyge=new ArrayList();
	List carriertypelist=new ArrayList();
	if(session.getAttribute("changeVoyageList")!=null){
		session.removeAttribute("changeVoyageList");
	}   
	if(session.getAttribute("voyageExport1")!=null){
		voyageExport=(VoyageExport)session.getAttribute("voyageExport1");
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
			try{ 
				Date deparDate=voyageExport.getDeliveryCutOffDate();
				DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
				deliveryCutoffDate=df1.format(deparDate);
			}catch (Exception e){
				e.printStackTrace();
			} 
		}
		if(voyageExport.getSailDate()!=null){
			try{ 
				Date deparDat=voyageExport.getSailDate();
				DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
				sailDate=df1.format(deparDat);
			}catch (Exception e){
				e.printStackTrace();
			} 
		}	
	  if(voyageExport.getUnitType()!=null){
			unitType=voyageExport.getUnitType();
		}
	  if(voyageExport.getOriginTerminal()!=null){
			terminalNumber=voyageExport.getOriginTerminal().getTrmnum();

			terminalName=voyageExport.getOriginTerminal().getTerminalLocation();
	}
	if(voyageExport.getDestinationPort()!=null){
	destSheduleNumber=voyageExport.getDestinationPort().getShedulenumber();
	destAirportname=voyageExport.getDestinationPort().getPortname();
	}
	if(voyageExport.getLineNo()!=null){
	lineNo=voyageExport.getLineNo().getCarriercode();
	lineName=voyageExport.getLineNo().getCarriername();
	}
	if(voyageExport.getVesselNo()!=null){
		vesselNo=voyageExport.getVesselNo().getCode();
		vesselName=voyageExport.getVesselNo().getCodedesc();
		}
	if(voyageExport.getDestinationPort()!=null){
		podNo=voyageExport.getDestinationPort().getShedulenumber();
		podName=voyageExport.getDestinationPort().getPortname();
		}
	}
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
			try{ 
	Date deparDate=voyageExport.getDeliveryCutOffDate();
	DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
	deliveryCutoffDate=df1.format(deparDate);
	}catch (Exception e){
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
			podNo=voyageExport.getDestinationPort().getShedulenumber();
			podName=voyageExport.getDestinationPort().getPortname();
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

AirRatesBean airRatesBean=new AirRatesBean();
airRatesBean.setShowOnSailingSchedule("Y");
if(request.getAttribute("airRatesBean")!=null)
{
airRatesBean=(AirRatesBean)request.getAttribute("airRatesBean");
airRatesBean.setShowOnSailingSchedule(airRatesBean.getShowOnSailingSchedule());
}
//VoyageExport voyageExport=new VoyageExport();
request.setAttribute("airRatesBean",airRatesBean);
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

			
		if(session.getAttribute("voyagepopup1")!=null)
{


voyageExport=(VoyageExport)session.getAttribute("voyagepopup1");
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
		

}
ChangeVoyage changevoyage = new ChangeVoyage();


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
	/*if(session.getAttribute("exportvoyagepier")!=null)
	{
		voyageMaster=(VoyageMaster)session.getAttribute("exportvoyagepier");
		if(voyageMaster.getDestinationPort()!=null)
		{
			pierNo=voyageMaster.getDestinationPort().getShedulenumber();
			
		}
	}*/
	
	if(session.getAttribute("addvesselrecords1")!=null)
	{
		voyageExport=(VoyageExport)session.getAttribute("addvesselrecords1");
		if(voyageExport.getVesselNo()!=null)
		{
		vesselNo1=voyageExport.getVesselNo().getCode();
		vesselName1=voyageExport.getVesselNo().getCodedesc();
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

if(session.getAttribute("VoyageReason")!=null)
{

changevoyage=(ChangeVoyage)session.getAttribute("VoyageReason");
if(changevoyage.getCodetypeid()!=null)
{
reasonCode=String.valueOf(changevoyage.getCodetypeid());
}
if(changevoyage.getCodeDescription()!=null)
{

reasonDesc=changevoyage.getCodeDescription();
}
}
if(session.getAttribute("changevoyage")!=null)
{       
     modify="0";
    changevoyage = (ChangeVoyage)session.getAttribute("changevoyage");
	if(changevoyage.getOldvesselNo()!=null)
	{   
			vesselNo=changevoyage.getOldvesselNo().getCode();
			vesselName = changevoyage.getOldvesselNo().getCodedesc();
	}
	if(changevoyage.getNewvesselNo()!=null)
	{
	        vesselNo1=changevoyage.getNewvesselNo().getCode();
	        vesselName1=changevoyage.getNewvesselNo().getCodedesc();
	
	}
	if(changevoyage.getOldSailDate()!=null)
	{        
			DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
			sailDate=df1.format(changevoyage.getOldSailDate());
	        
	}
	if(changevoyage.getNewSailDate()!=null)
	{        
			DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
			sailDate1=df1.format(changevoyage.getNewSailDate());
	        
	}
	if(changevoyage.getOldPier()!=null)
	{
	    pierNo=changevoyage.getOldPier();
	}
	if(changevoyage.getNewPier()!=null)
	{
	    pierNo1=changevoyage.getNewPier();
	}
	if(changevoyage.getOldFlightSsVoyage()!=null)
	{
	   flightSsVoyage=changevoyage.getOldFlightSsVoyage();
	
	}   
	if(changevoyage.getNewFlightSsVoyage()!=null)
	{
	   flightSsVoyage1=changevoyage.getNewFlightSsVoyage();
	
	}
	if(changevoyage.getOldLineNo()!=null)
	{
	   lineNo=changevoyage.getOldLineNo().getCarriercode();
	   lineName=changevoyage.getOldLineNo().getCarriername();
	 
	}
	if(changevoyage.getNewLineNo()!=null)
	{
	   lineNo1=changevoyage.getNewLineNo().getCarriercode();
	   lineName1=changevoyage.getNewLineNo().getCarriername();

	 
	}
	if(changevoyage.getCodetypeid()!=null)
	{
	        reasonCode=String.valueOf(changevoyage.getCodetypeid());
	}
	if(changevoyage.getCodeDescription()!=null)
	{
	        reasonDesc=changevoyage.getCodeDescription();
	}
	if(changevoyage.getNotifyCustomer()!=null)
	{   
	    if(changevoyage.getNotifyCustomer().equals("y"))
	    {
	       notify="y";
	    }
	    else
	    {
	           notify="n";
	    
	    }
	    
	}
	if(changevoyage.getNotifyEci()!=null)
	{   if(changevoyage.getNotifyEci().equals("y"))
	    {
	        eci="y";
	    }
	    else
	    {
	             eci="n";
	    }
	
	}
	
}


if(session.getAttribute("popup1")!=null)
{
   changevoyage=(ChangeVoyage)session.getAttribute("popup1");
    if(changevoyage.getNewvesselNo()!=null)
    {
        vesselNo1=changevoyage.getNewvesselNo().getCode();
        vesselName1=changevoyage.getNewvesselNo().getCodedesc();
    }
	if(changevoyage.getNewSailDate()!=null)
	{
		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");
		sailDate1=df1.format(changevoyage.getNewSailDate());
	}
	if(changevoyage.getNewPier()!=null)
	{
	   pierNo1=changevoyage.getNewPier();
	}
	if(changevoyage.getNewFlightSsVoyage()!=null)
	{
	     flightSsVoyage1=changevoyage.getNewFlightSsVoyage();
	}
if(changevoyage.getNewLineNo()!=null)
	{
	   lineNo1=changevoyage.getNewLineNo().getCarriercode();
	   lineName=changevoyage.getNewLineNo().getCarriername();

	 
	}
}
	String path1=null;

	if(request.getAttribute("path1")!=null)
	{
	       path1=(String)request.getAttribute("path1");
	      
	}
	if(session.getAttribute("searchvoyagerecords1")!=null)
	{    voyageExport=(VoyageExport)session.getAttribute("searchvoyagerecords1");
		lineNo1 =voyageExport.getLineNo().getCarriercode();
		lineName1=voyageExport.getLineNo().getCarriername();

	}
	String mesg="";
	if(request.getAttribute("message6")!=null)
	{
	       mesg=request.getAttribute("message6").toString();
	}
	
	String editPath=path+"/exportVoyage.do";    
	if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("completed"))
{
	%>

<script type="text/javascript">
	          parent.parent.GB_hide();
	          parent.parent.getExport();
			//self.close();
			//opener.location.href="<%=path%>/<%=path1%>";
	</script>
<%
}
 
 if(request.getAttribute("accountDetails")!=null){
 GenericCode accountDetails=(GenericCode)request.getAttribute("accountDetails");
 voyageReasonId=String.valueOf(accountDetails.getId());
 }

	
%>

<html>
	<head>
		<title>JSP for exportVoyagesForm form</title>
		<%@include file="../includes/baseResources.jsp"%>

		<script language="javascript" src="<%=path%>/js/dojo/dojo.js"
			type="text/javascript"></script>
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
			
			document.exportVoyagesForm.buttonValue.value="save";
			//document.exportVoyagesForm.costtype.value="0";
			document.exportVoyagesForm.submit();
		}
		function submit2()
		{
			
			document.exportVoyagesForm.buttonValue.value="edit";
			//document.exportVoyagesForm.costtype.value="0";
			document.exportVoyagesForm.submit();
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
			
			document.exportVoyagesForm.buttonValue.value="popup";
			
			document.exportVoyagesForm.submit();
			return false;
			}
		function costtypechange()
		{
		
			if(document.exportVoyagesForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else{
				document.exportVoyagesForm.buttonValue.value="costType";
				document.exportVoyagesForm.submit();
			}
		}
		
		function addForm(val1)
		{
			
			if(document.exportVoyagesForm.costcode.value!=null&&document.exportVoyagesForm.costcode.value=="0")
			{
				alert("Please select cost code !");
			}
			else if(document.exportVoyagesForm.costtype.value!=null&&document.exportVoyagesForm.costtype.value=="0")
			{
				alert("Please select Cost Type !");
			}
			else if(val1!=null&&val1==11300)
			{
				 if(document.exportVoyagesForm.unittype.value!=null&&document.exportVoyagesForm.unittype.value=="0")
					{
					alert("Please select Unit Type !");
					}
					else{
					document.exportVoyagesForm.buttonValue.value="add";
					document.exportVoyagesForm.submit();
					}
			}
			else if(val1!=null&&val1==11306)
			{
					if(document.exportVoyagesForm.range.value!=null&&document.exportVoyagesForm.range.value=="0")
					{
						alert("Please select Wight Range !");
					}else{
					document.exportVoyagesForm.buttonValue.value="add";
					document.exportVoyagesForm.submit();
					}
			}
			else{
			
				document.exportVoyagesForm.buttonValue.value="add";
				document.exportVoyagesForm.submit();
			}
		}
		function delete1()
		{
			document.exportVoyagesForm.buttonValue.value="delete";
			document.exportVoyagesForm.submit();
		}
		function saveForm1(val,val2)
		{
		
			
			if(val!=null && val!="save")
			{
			document.exportVoyagesForm.buttonValue.value="edit";
			}
			else{
				document.exportVoyagesForm.buttonValue.value="save";
			}
			if(val2!=""){
	
			
			}
			else{
			
			document.exportVoyagesForm.submit();
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
				document.exportVoyagesForm.buttonValue.value="cancel";
				document.exportVoyagesForm.submit();
				}
			}else{
			document.exportVoyagesForm.buttonValue.value="cancel";
			document.exportVoyagesForm.submit();
			}
		}
		
		//ExportVoyage
		function save1(val1){
		
			
			var netie=navigator.appName;
			
 			 if(val1=='edit'){
				document.exportVoyagesForm.buttonValue.value="edit";
 			}
 			else if(val1=='save'){
 				document.exportVoyagesForm.buttonValue.value="save";
 			}
			//document.exportVoyagesForm.buttonValue.value="save";
			document.exportVoyagesForm.submit();
 			/*if(netie=="Microsoft Internet Explorer"){
 			//alert(netie);
	   			if(val=="true"){
	   			//alert(parent.mainFrame.document.agssFrame.agscFTFForm);
	 				parent.mainFrame.document.agssFrame.agscFTFForm.submit();
	 				parent.mainFrame.document.documentFrame.documentChargesFTFForm.submit();
 					
 					}
 					
	 			else if(val=="false")
	 			{
	 				//alert(parent.mainFrame.document.airDetailsFrame);
		 			parent.mainFrame.document.airDetailsFrame.addFTFDetailsForm.submit();
		 			
 					}
 			}*/
 			/*else if(netie=="Netscape"){
 			if(val=="false"){
   				parent.mainFrame.document.getElementById('airDetailsFrame').contentDocument.addFTFDetailsForm.submit();
   				}
   				}*/
   				
 				
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
			  		
	   				document.exportVoyagesForm.buttonValue.value="savecancel";
	   				document.exportVoyagesForm.submit();
			  }
			  }
				
		
 			
		}
		function confirmnote()
	{
		
		document.exportVoyagesForm.buttonValue.value="note";
    	document.exportVoyagesForm.submit();
   	}	
   	
   	function delete1()	{
   			
			document.exportVoyagesForm.buttonValue.value="delete";
 			document.exportVoyagesForm.submit();
			}
			
			function cancel1(val)
			{      
			       document.exportVoyagesForm.buttonValue.value="cancel";
			       document.exportVoyagesForm.submit();
			       
			}
		//ExportVoyage Ends
		
		function noteForm()
		{
			document.exportVoyagesForm.buttonValue.value="note";
			document.exportVoyagesForm.submit();
		}
		
		function disabled(val1,val2,val3,val4) {
		   if(val3!="" && val3=="y")  {
		   
		   document.exportVoyagesForm.singleSelect1.selectedIndex=1;
		    }
		   else
		   {
		        document.exportVoyagesForm.singleSelect1.selectedIndex=0;
		   }
		     if(val4!="" && val4=="y")
		   {
		   
		   
		   document.exportVoyagesForm.singleSelect2.selectedIndex=1;
		    }
		   else
		   {
		        document.exportVoyagesForm.singleSelect2.selectedIndex=0;
		   }
		    
		    
		    
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
			   		    if(input[i].id=="save")
			   		    {
			   		         input[i].style.visibility='hidden';
			   		    }
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
	  	 		}
		  	 if(val1 == 1)	 {
	  	 		 	  document.getElementById("delete").style.visibility = 'hidden';
	  	 		}
	  	 	if(val1==3 && val2!=""){
				//alert(val2);
				}		
    
	}
		function nonTest(val1){
			
			document.exportVoyagesForm.buttonValue.value="index";
			document.exportVoyagesForm.index.value=val1;
			document.exportVoyagesForm.buy.value="buy";
			document.exportVoyagesForm.submit();
			
			mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+val1+"&buy=buy","","width=800,height=200");
        mywindow.moveTo(200,180);		
		}
	
		function comTest(val1){
			
			document.exportVoyagesForm.buttonValue.value="index";
			document.exportVoyagesForm.index.value=val1;
			document.exportVoyagesForm.submit();
			mywindow=window.open("<%=path%>/jsps/ratemanagement/fcladdedit.jsp?flaterate="+val1,"","width=800,height=200");
       		 mywindow.moveTo(200,180);		
		}
		function titleLetter(ev)
	{
		if(event.keyCode==9){
		//document.exportVoyagesForm.buttonValue.value="search";
			document.exportVoyagesForm.submit();
	window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=exportvoyage&trname=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getSSLineName(ev)
	{
		if(event.keyCode==9){
		
			document.exportVoyagesForm.submit();
	window.open("<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button=lineNo&sslinename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
	}
	function getVoyage(ev)
	{
	
	 if(event.keyCode==9)
	 {
	
	   window.open("<%=path%>/jsps/voyagemanagement/searchVessel.jsp?button=addvessel&vesselno="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 }
	}
	function getVoyage1(ev)
	{
	
	 if(event.keyCode==9)
	 {
	
	   window.open("<%=path%>/jsps/voyagemanagement/searchVessel.jsp?button=addvessel1&vesselno="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    document.exportVoyagesForm.buttonValue.value="popup1";
	   	 document.exportVoyagesForm.submit();
	 
	 }
	}
		function getVoyage2(ev)
	{
	
	 if(event.keyCode==9)
	 {
	
	   window.open("<%=path%>/jsps/voyagemanagement/searchVessel.jsp?button=addvessel1&vesselname="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    document.exportVoyagesForm.buttonValue.value="popup1";
	   	 document.exportVoyagesForm.submit();
	 }
	}
	
	function  getPod(ev)
	{
		 if(event.keyCode==9)
	 {
	
	   window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=exportvoyageport&percode="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 }
	
	}
		function  getPod1(ev)
	{
		 if(event.keyCode==9)
	 {
	
	   window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=exportvoyageport&percodename="+ ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	 }
	
	}
	function save2()
	{
	   
	    if(document.exportVoyagesForm.lineNo1.value=="")
	    {
	        alert("please eneter the line number");
	        return;
	     }
	     if(document.exportVoyagesForm.vessel1.value=="")
	     {
	             alert("please enter the vessel number");
	             return;
	     }
	    document.exportVoyagesForm.buttonValue.value="save";
	    document.exportVoyagesForm.submit();
	
	}
	function getVoyageReason(ev)
	{
	 if(event.keyCode==9)
	 {
	   
	   window.open("<%=path%>/jsps/voyagemanagement/voyageCodePopUp.jsp?button=addreason&codeid="+ ev ,"","toolbar=no,scrollBars=true,resizable=no,status=no,width=600,height=400");
	   document.exportVoyagesForm.buttonValue.value="popup1";
	   document.exportVoyagesForm.submit();
	 }
	
	}
	function getSSLine(ev)
	{
		if(event.keyCode==9)
		{
		
			//document.exportVoyagesForm.submit();
	     window.open("<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button=searchvoyage1&ssline=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
		 document.exportVoyagesForm.buttonValue.value="popup1";
	   	 document.exportVoyagesForm.submit();
		}
	}
	function getSSLine1(ev)
	{
		if(event.keyCode==9)
		{
		
			//document.exportVoyagesForm.submit();
	     window.open("<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button=searchvoyage1&sslinename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
		 document.exportVoyagesForm.buttonValue.value="popup1";
	   	 document.exportVoyagesForm.submit();
		}
	}
	function getVesselName(ev){ 
   	 		document.getElementById("vesselName1").value="";
   			if(event.keyCode==9 || event.keyCode==13){
	 			var params = new Array();
	 			params['requestFor'] = "VesselName";
	 			params['vesselNo'] = document.exportVoyagesForm.vessel1.value;
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
       		document.getElementById("vesselName1").value=data.VesselName;
       		}
       	}
         function getVesselNo(ev){ 
   	 		document.getElementById("vessel1").value="";
   			if(event.keyCode==9 || event.keyCode==13){
	 			var params = new Array();
	 			params['requestFor'] = "VesselNumber";
	 			params['vesselName'] = document.exportVoyagesForm.vesselName1.value;
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
       		document.getElementById("vessel1").value=data.VesselNo;
       		}
       	}
	
	function getSslineName(ev){
      document.getElementById("lineName1").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "SslineName";
				 params['sslinenumber'] = document.exportVoyagesForm.lineNo1.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
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
		   		document.getElementById("lineName1").value=data.sslinename;
		   	}
		   }
       function getSslineNumber(ev){
       document.getElementById("lineNo1").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "SslineNumber";
				 params['sslinename'] = document.exportVoyagesForm.lineName1.value;
				  var bindArgs = { 
				  url: "<%=path%>/actions/getExportVoyageDetailsForDojo.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateSslineNumber");
			    }
			 }
	   function populateSslineNumber(type, data, evt) {
		   	if(data){
		   		document.getElementById("lineNo1").value=data.sslinenumber;
		   	}
		   }
		   
	        function getVoyageReason(ev){
       document.getElementById("reasonDescription").value="";
			if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "CommodityDetails";
				 params['code'] = document.exportVoyagesForm.reasonCode.value;
				 params['codeType'] = '15';
				  var bindArgs = { 
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateVoyageReason");
			    }
			 }
	   function populateVoyageReason(type, data, evt) {
		   	if(data){
		   		document.getElementById("reasonDescription").value=data.commodityDesc;
		   	}
		   }
	
		</script>
		<%@include file="../includes/resources.jsp"%>
	</head>
	<body class="whitebackgrnd">
		<br>
		<html:form action="/exportVoyages" scope="request">

			<font color="blue" size="4"><%=mesg %></font>


		<table width="100%" border="2" cellpadding="0" cellspacing="0" class="tableBorderNew">
		<tr class="tableHeadingNew">Voyage Info</tr>
			<td>
				<table width="700" border="2" cellpadding="0" cellspacing="0" id="records">
					<tr class="textlabels">
						
									<td align="right" ><input type="button" class="buttonStyleNew" value="Update" onclick="save2()" id="save">
									<input type="button" class="buttonStyleNew" value="Go Back" onclick="cancel1()" id="cancel"></td>
							
					</tr>
		       </table>
		       	<table width="700" border="2" cellpadding="0" cellspacing="0" id="records">
		       	   <tr>
						<td>
							<%        
								List changeVoyageList = null;
       							if(session.getAttribute("changeVoyageList")==null){   
  									Integer id=null;
  		    						if(session.getAttribute("voyageExport1")!=null){
										voyageExport=(VoyageExport)session.getAttribute("voyageExport1");
				      					id=  voyageExport.getId();
				     				}
  									changeVoyageList = dbutil.getChangeVoyagesByID(id);
  									session.setAttribute("changeVoyageList",changeVoyageList);
  								}
  								else{  
  									changeVoyageList = (List)session.getAttribute("changeVoyageList");
  								}
  								request.setAttribute("changeVoyageList",changeVoyageList);
  								String editpath=path+"/exportVoyages.do";	
  								int i=0;
							%>
							<div id="divtablesty1" class="scrolldisplaytable">
							<display:table name="changeVoyageList" pagesize="<%=pageSize%>"	class="displaytagstyle" sort="list">
							<%i++; %>
							<display:setProperty name="paging.banner.some_items_found">
							<span class="pagebanner"> <font color="blue">{0}</font>Vessel Details displayed,For more Vessel click on page numbers. </span>
							</display:setProperty>
							<display:setProperty name="paging.banner.one_item_found">
									<span class="pagebanner"> One {0} displayed. Page Number </span>
							</display:setProperty>
							<display:setProperty name="paging.banner.all_items_found">
							<span class="pagebanner"> {0} {1} Displayed, Page Number </span>
							</display:setProperty>
							<display:setProperty name="basic.msg.empty_list">
							<span class="pagebanner"> No Records Found. </span>
							</display:setProperty>
							<display:column title="Cntrl" href="<%=editpath%>" paramId="param" sortable="true" paramProperty="id"><%=i%></display:column>
							<display:column property="userName" title="By User" sortable="true" headerClass="sortable" ></display:column>
  							<display:column property="timeModified" title="Time" sortable="true" headerClass="sortable"></display:column>
  							<display:column property="dateModified" title="Date" sortable="true" headerClass="sortable"></display:column>
  							<display:column property="oldvesselNo.codedesc" title="Old Vessel Name" sortable="true" headerClass="sortable"></display:column>
		 					<display:column property="newvesselNo.codedesc" title="New Vessel Name" sortable="true" headerClass="sortable"></display:column>
  		 					<display:column property="oldSailDate" title="Old Sail Date" sortable="true" headerClass="sortable"></display:column>
		 					<display:column property="newSailDate" title="New Sail Date" sortable="true" headerClass="sortable"></display:column>
							</display:table></div>
								<br>
						</td>
					</tr>
					
				</table>
			</td>
	</table>
			<br />
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="tableBorderNew">
				<tr class="tableHeadingNew">
					Voyage Details
				</tr>

				<td>
					<br />
					<table width="100%" border="0" cellpadding="0" cellspacing="0">


						<tr>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr class="tableHeadingNew">
										old Voyage Info
									</tr>
									<tr class="textlabels">
										<td>
											Vessel#
											<br>
										</td>
										<td>
											<html:text property="vessel"
												onkeydown="getVoyage(this.value)" size="12"
												disabled="disabled" value="<%=vesselNo %>" readonly="true"></html:text>
											<br>
										</td>
										<td align="right">
											Vessel Name&nbsp;
											<br>
										</td>
										<td>
											<html:text property="vesselName"
												onkeydown="getVoyage1(this.value)" size="12" styleId=""
												disabled="disabled" value="<%=vesselName %>" readonly="true"></html:text>
											<br>
										</td>

									</tr>

									<tr class="textlabels">
										<td>
											Sail Date
											<br>
										</td>
										<td>
											<html:text property="sailDate" styleId="txtItemcreated"
												size="12" value="<%=sailDate%>" readonly="true"></html:text>
											<br>
										</td>
										<td align="right">
											Pier#&nbsp;
											<br>
										</td>
										<td>
											<html:text property="pierNo" size="12" value="<%=pierNo %>"
												readonly="true"></html:text>
											<br>
										</td>


									</tr>

									<tr class="textlabels">
										<td>
											Line#
											<br>
										</td>
										<td>
											<html:text property="lineNo" readonly="true"
												value="<%=lineNo %>" size="12"></html:text>
											<br>
										</td>
										<td align="right">
											Line Name&nbsp;
											<br>
										</td>
										<td>
											<html:text property="lineName" readonly="true"
												value="<%=lineName %>" size="12"></html:text>
											<br>
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											<span
												onmouseover="tooltip.show('<strong>Flight/SS Voyage</strong>',null,event);"
												onmouseout="tooltip.hide();">FL/SS</span>
										</td>
										<td>
											<html:text property="flightSsVoyage" size="12"
												value="<%=flightSsVoyage %>" readonly="true"></html:text>
											<br>
										</td>

									</tr>
								</table>
								<br>
							</td>

							<td>
								&nbsp;

								<br>
							</td>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr class="tableHeadingNew">
										new Voyage Info
									</tr>
									<tr class="textlabels">
										<td>
											Vessel#
											<br>
										</td>
										<td valign="bottom">
											<input type="text" name="vessel1" id="vessel1"
												value="<%=vesselNo1 %>" size="10"
												onkeydown="getVesselName(this.value)" />
											<dojo:autoComplete formId="exportVoyagesForm"
												textboxId="vessel1"
												action="<%=path%>/actions/getVesselNo.jsp?tabName=EXPORT_VOYAGE&amp;from=2">
											</dojo:autoComplete>
										</td>
										<td align="right">
											Vessel Name&nbsp;
											<br>
										</td>
										<td>
											<input type="text" name="vesselName1" id="vesselName1"
												value="<%=vesselName1 %>" size="10"
												onkeydown="getVesselNo(this.value)" />
											<dojo:autoComplete formId="exportVoyagesForm"
												textboxId="vesselName1"
												action="<%=path%>/actions/getVesselName.jsp?tabName=EXPORT_VOYAGE&amp;from=1" />
										</td>
									</tr>
									<tr class="textlabels">
										<td>
											Sail Date
											<br>
										</td>
										<td>
											<html:text property="sailDate1" styleId="txtcal2" size="10"
												value="<%=sailDate1%>"></html:text>
											<img width="16" height="16" align="top"
												src="<%=path%>/img/CalendarIco.gif" alt="cal2" id="cal2"
												onmousedown="insertDateFromCalendar(this.id,0);" />
											<br>
										</td>
										<td align="right">
											Pier#&nbsp;
											<br>
										</td>
										<td>
											<html:text property="pierNo1" size="10" value="<%=pierNo1 %>"></html:text>
											<br>
										</td>


									</tr>

									<tr class="textlabels">
										<td>
											Line#
											<br>
										</td>
										<td>
											<input name="lineNo1" id="lineNo1" size="10" maxlength="5"
												value="<%=lineNo1 %>" onkeydown="getSslineName(this.value)" />
											<dojo:autoComplete formId="exportVoyagesForm"
												textboxId="lineNo1"
												action="<%=path%>/actions/getSsLineNo.jsp?tabName=EXPORT_VOYAGE&amp;from=1" />
										</td>
										<td align="right">
											Line Name&nbsp;
											<br>
										</td>
										<td>
											<input name="lineName1" id="lineName1"
												value="<%=lineName1 %>"
												onkeydown="getSslineNumber(this.value)" size="10" />
											<dojo:autoComplete formId="exportVoyagesForm"
												textboxId="lineName1"
												action="<%=path%>/actions/getSsLineName.jsp?tabName=EXPORT_VOYAGE&amp;from=1" />
										</td>

									</tr>
									<tr class="textlabels">
										<td>
											<span
												onmouseover="tooltip.show('<strong>Flight/SS Voyage</strong>',null,event);"
												onmouseout="tooltip.hide();">FL/SS</span>
										</td>
										<td>
											<html:text property="flightSsVoyage1" size="10"
												value="<%=flightSsVoyage1 %>"></html:text>
											<br>
										</td>

									</tr>
								</table>
								<br>
							</td>
						</tr>
					</table>


					<br />
					<table width="100%" border="0" cellpadding="0" cellspacing="0">


						<tr>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr class="textlabels">
										<td>
											Reason Code:
											<br>
										</td>
										<td>
											<input name="reasonCode" id="reasonCode"
												value="<%=reasonCode%>" size="5"
												onkeydown="getVoyageReason(this.value)" />
											<dojo:autoComplete formId="exportVoyagesForm"
												textboxId="reasonCode"
												action="<%=path%>/actions/getChargeCode.jsp?tabName=EXPORT_VOYAGE&amp;from=0" />

										</td>
										<td>
											Reason Description:
											<br>
										</td>
										<td>
											<html:textarea name="reasonDescription"
												property="reasonDescription" onchange="trigger(this.value)"
												readonly="true" value="<%=reasonDesc%>" />
										</td>

									</tr>
									<tr class="textlabels">
										<td>
											Notify Customer Of this Change?
										</td>
										<td id="notify">
											<html:select property="singleSelect1">
												<html:option value="n">No</html:option>
												<html:option value="y">Yes</html:option>

											</html:select>
										</td>
										<td>
											Notify ECI Employee?
										</td>
										<td>
											<html:select property="singleSelect2">
												<html:option value="n">No</html:option>
												<html:option value="y">Yes</html:option>

											</html:select>
										</td>

									</tr>
								</table>
							</td>
						</tr>
					</table>


				</td>
			</table>
			<html:hidden property="buttonValue" styleId="buttonValue" />
			<script type="text/javascript">disabled('<%=modify%>','<%=message%>','<%=notify %>','<%=eci %>')</script>
		</html:form>
	</body>
	 <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

