<%@ page language="java"  import=" com.gp.cong.logisoft.bc.notes.NotesConstants,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.beans.*,com.gp.cvst.logisoft.beans.TransactionBean,com.gp.cong.logisoft.hibernate.dao.*,com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.domain.*"%>
<%@ page import="java.util.*,java.text.*"%> 
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
<%
String path = request.getContextPath();
CustomerDAO custDAO= new CustomerDAO();
GenericCodeDAO gdDAO = new GenericCodeDAO();
DBUtil dbUtil = new DBUtil();
String msg="";
if(request.getAttribute("msg")!=null){
msg=(String)request.getAttribute("msg");
}
 NumberFormat numformat1 = new DecimalFormat("##,###,##0.00");
SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
DateFormat dateformat=new SimpleDateFormat("MM/dd/yyyy HH:mm a");//to include date & time
NumberFormat number = new DecimalFormat("##,###,##0.00");
List Records = new ArrayList();
String thirdParty="";

if(request.getAttribute("ThirdParty")!=null){
thirdParty=(String)request.getAttribute("ThirdParty");
}
String bookingBy="";
User user1=null;
if(session.getAttribute("loginuser")!=null){
	  user1=(User)session.getAttribute("loginuser");
	  bookingBy = user1.getLoginName();

}
String bookingDate= dateformat.format(new Date()); 
request.setAttribute("optionList",dbUtil.getVendortypeIncudingThirdParty());
 request.setAttribute("typeOfMoveList", dbUtil.getGenericFCL(new Integer(48),"yes"));
%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
 
<%@include file="../includes/baseResources.jsp" %>
<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		     start = function(){
				loadFunction();
			}
		    dojo.addOnLoad(start);
		</script>
<script language="javascript" type="text/javascript">
function validateDate(){
	if(document.NewBookingsForm.estimatedDate.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.NewBookingsForm.estimatedDate.value;
	 	params['ETA'] = document.NewBookingsForm.estimatedAtten.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
    	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "result");
    }
}
function result(type, data, evt){
	if(data){
       if(data.result == 'greater'){
       }else if(data.result == 'lesser' || data.result == 'equal'){
               	 alert("ETA should be greater than ETD");
                 document.NewBookingsForm.estimatedAtten.value="";
                 document.NewBookingsForm.estimatedAtten.select();
       }
	}
}

function validateDate1(){
	if(document.NewBookingsForm.estimatedAtten.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidationReverse";
	 	params['ETD'] = document.NewBookingsForm.estimatedDate.value;
	 	params['ETA'] = document.NewBookingsForm.estimatedAtten.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "result1");
    }
}

function result1(type, data, evt){
	if(data){
       if(data.result == 'greater'  || data.result == 'equal'){
                  alert("ETD should be lesser than ETA");
                  document.NewBookingsForm.estimatedDate.value="";
                  document.NewBookingsForm.estimatedDate.select();
       }else if(data.result == 'lesser'){
       }
	}
}
	function loadFunction(){
     	document.getElementById("shipperContactButton").style.visibility = 'hidden';
		document.getElementById("forwarderContactButton").style.visibility = 'hidden';
		document.getElementById("congineeContactButton").style.visibility = 'hidden';
		document.getElementById("truckerContactButton").style.visibility = 'hidden';
	
	for(var i=0;i<document.NewBookingsForm.localdryage.length;i++){
		if(document.NewBookingsForm.localdryage[i].value=="N" && document.NewBookingsForm.localdryage[i].checked){
			document.NewBookingsForm.amount.readOnly=true;
		}
	}
	for(var i=0;i<document.NewBookingsForm.intermodel.length;i++){
		if(document.NewBookingsForm.intermodel[i].value=="N" && document.NewBookingsForm.intermodel[i].checked){
			document.NewBookingsForm.amount1.readOnly=true;
		}
	}
	for(var i=0;i<document.NewBookingsForm.insurance.length;i++){
		if(document.NewBookingsForm.insurance[i].value=="N" && document.NewBookingsForm.insurance[i].checked){
			document.NewBookingsForm.costofgoods1.readOnly=true;
		}
	}
	}
function SAVE(){
	var bookingDate=   document.NewBookingsForm.bookingDate;
		if(bookingDate.value==""){
			alert("Enter the Booking date");
			bookingDate.focus();
			return;
		}
		if(document.NewBookingsForm.originTerminal.value=="" && document.NewBookingsForm.portOfOrigin.value=="")
		{
			alert("Please Enter Either the Original Terminal Or Port Of Loading");
			document.NewBookingsForm.originTerminal.focus();
			return;
		}
		if(document.NewBookingsForm.portOfDischarge.value=="" && document.NewBookingsForm.destination.value=="")
		{
			alert("Please Enter Either the Destinantion or  Port Of Discharge ");
			document.NewBookingsForm.portOfDischarge.focus();
			return;
		}
		if(document.NewBookingsForm.commcode.value=="")
		{
			alert("Please Enter the Commodity Code");
			document.NewBookingsForm.commcode.focus();
			return;
		}
		var shipper =  document.NewBookingsForm.shipper;
		document.NewBookingsForm.buttonValue.value="save";
		document.NewBookingsForm.submit();
}
function BookingsReport(){
  document.NewBookingsForm.buttonValue.value="BookingsReport";
  document.NewBookingsForm.submit();
}
function getlocaldryage(){
	document.NewBookingsForm.amount.readOnly=false;
}
function getlocaldryage1(){
	document.NewBookingsForm.amount.readOnly=true;
}
function getintermodel(){
	document.NewBookingsForm.amount1.readOnly=false;
}
function getintermodel1(){
	document.NewBookingsForm.amount1.readOnly=true;
}
function getinsurance(){
	document.NewBookingsForm.costofgoods1.readOnly=false;
}
function getinsurance1(){
	document.NewBookingsForm.costofgoods1.readOnly=true;
}
function getHazmat(){
	document.NewBookingsForm.buttonValue.value="hazmat";
	document.NewBookingsForm.submit();
}
function getPopHazmat(){
if(document.NewBookingsForm.hazmat[0].checked){
 GB_show('Hazmat','<%=path%>/fCLHazMat.do?buttonValue=Booking&name=Booking&number='+document.NewBookingsForm.bookingId.value,
  width="600",height="800");
}else{
alert("Please Select Hazmat");
return;
}
	}
function getspcleqpmt(obj){
	if(obj.value=='Y'){
		document.NewBookingsForm.specialequipment.disabled=false;
	}
}
function getspcleqpmt1(){
	document.QuotesForm.specialEqpmt.disabled=true;
}
function getAccountName(ev){
	if(event.keyCode==9 || event.keyCode==13){
		document.NewBookingsForm.buttonValue.value="recalc";
		document.NewBookingsForm.submit();
	}
}
function recalcfunction() {
       document.NewBookingsForm.buttonValue.value="recalc";
	   document.NewBookingsForm.submit();
}
function getNumbersChanged(obj) {
	   var rowindex=obj.parentNode.parentNode.rowIndex;
	  document.NewBookingsForm.numbIdx.value=rowindex-1;
	  document.NewBookingsForm.numbers1.value=obj.value;
	  document.NewBookingsForm.buttonValue.value="numbersChanged";
	  document.NewBookingsForm.submit();
  }
function GB_show(mylink, windowname){
	if (!window.focus)return true;
	var href;
	if (typeof(mylink) == 'string')
	   href=mylink;
	else
	   href=mylink.href;
	window.open(href, windowname, 'width=500,height=200,scrollbars=yes');
	 document.NewBookingsForm.buttonValue.value="recalc";
		document.NewBookingsForm.submit();
	return false;
}
function getlocaldryage(){
	document.NewBookingsForm.amount.readOnly=false;
}
function getlocaldryage1(){
	document.NewBookingsForm.amount.readOnly=true;
}
function getintermodel(){
	document.NewBookingsForm.amount1.readOnly=false;
}
function getintermodel1(){
	document.NewBookingsForm.amount1.readOnly=true;
}
function getinsurance(){
	document.NewBookingsForm.costofgoods1.readOnly=false;
}
function getinsurance1(){
	document.NewBookingsForm.costofgoods1.readOnly=true;
}
function load(){
if(document.NewBookingsForm.shipper.value==""){
document.getElementById("shipperbutton").style.visibility='hidden';
}
if(document.NewBookingsForm.fowardername.value==""){
document.getElementById("fowarderbutton").style.visibility='hidden';
}
if(document.NewBookingsForm.consigneename.value==""){
document.getElementById("consigneebutton").style.visibility='hidden';
}
if(document.NewBookingsForm.truckerName.value==""){
document.getElementById("truckerContactButton").style.visibility='hidden';
}
	if(document.NewBookingsForm.amount.value=="0.00")
	{
		document.NewBookingsForm.amount.readOnly=true;
	}
	if(document.NewBookingsForm.amount1.value=="0.00")
	{
		document.NewBookingsForm.amount1.readOnly=true;
	}
	if(document.NewBookingsForm.costofgoods1.value=="0.00")
	{
		document.NewBookingsForm.costofgoods1.readOnly=true;
	}
}

function CheckedShip() {
	    if(document.NewBookingsForm.shippercheck.checked)
	    {
	      document.NewBookingsForm.forwardercheck.checked=false;
	      document.NewBookingsForm.consigneecheck.checked=false;
	    }
}
function CheckedForwarder(){
	   if(document.NewBookingsForm.forwardercheck.checked)
	    {
	      document.NewBookingsForm.shippercheck.checked=false;
	      document.NewBookingsForm.consigneecheck.checked=false;
	    }
}
function CheckedConsignee(){
	  if(document.NewBookingsForm.consigneecheck.checked)
	    {
	      document.NewBookingsForm.shippercheck.checked=false;
	      document.NewBookingsForm.forwardercheck.checked=false;
	    }
}
function popupcharges(mylink, windowname){
	if (!window.focus)return true;
	var href;
	if (typeof(mylink) == 'string')
	   href=mylink;
	  
	else
	   href=mylink.href;
	window.open(href, windowname, 'width=700,height=200,scrollbars=yes');
	 document.NewBookingsForm.buttonValue.value="recalc";
		document.NewBookingsForm.submit();
	return false;
}
function AlertMessage(){
    alert(" Enter Date and Time in this format --> MM/DD/YY  HH:MM ");
}
//--------DOJO CODES------------
function getComCodeDesc(ev){  
			if(event.keyCode==9 || event.keyCode==13){
			 document.getElementById("comdesc").value="";
			    
				 var params = new Array();
				 params['requestFor'] = "CommodityCodeDescription";
				 params['commodityCode'] = document.NewBookingsForm.commcode.value;
			
				  var bindArgs = {
				  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateComCodeDescDojo");
		 }
}		 
function populateComCodeDescDojo(type, data, evt) {
	if(data){
	   		document.getElementById("comdesc").value=data.commodityDescription;
	   	}
}
function getComCode(ev){
   
	if(event.keyCode==9 || event.keyCode==13){
	 document.NewBookingsForm.commcode.value=""; 
		 var params = new Array();
		 params['requestFor'] = "CommodityCode";
		 params['commodityCodeDescription'] = document.NewBookingsForm.comdesc.value;
	
		  var bindArgs = {
		  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
		 };
		 var req = dojo.io.bind(bindArgs);
		 dojo.event.connect(req, "load", this, "populateComCodeDojo");
	 }
}
function populateComCodeDojo(type, data, evt) {
   	if(data){
   		document.getElementById("commcode").value=data.commodityCode;
   	}
}
function getShipperInfo(ev){ 
		jQuery(document).ready(function(){
		 document.getElementById("shipper").value="";
			 var params = new Array();
			 params['requestFor'] = "CustAddress";
			 params['custName'] = document.NewBookingsForm.shipperName.value;
		
			  var bindArgs = {
			  url: "<%=path%>/actions/getCustDetails.jsp",
			  error: function(type, data, evt){alert("error");},
			  mimetype: "text/json",
			  content: params
			 };
			 var req = dojo.io.bind(bindArgs);
			 dojo.event.connect(req, "load", this, "populateShipperInfo");
	 });
 }
function populateShipperInfo(type, data, evt) {
 document.getElementById("shipperbutton").style.visibility='visible';
	if(data){
  //	    document.getElementById("shipperName").value=data.custName;
  	    if(data.custNumber){
  	    document.getElementById("shipper").value=data.custNumber;
  	    }else{
  	    document.getElementById("shipper").value="";
  	    }
  	    if(data.custAddress){
  	    document.getElementById("shipperAddress").value=data.custAddress;
  	    }else{
  	    document.getElementById("shipperAddress").value="";
  	    }
  	    if(data.custCity){
  	    document.getElementById("shipperCity").value=data.custCity;
  	    }else{
  	    document.getElementById("shipperCity").value="";
  	    }
  	    if(data.custState){
  	    document.getElementById("shipperState").value=data.custState;
  	    }else{
  	    document.getElementById("shipperState").value="";
  	    }
  	    if(data.custZip){
  	    document.getElementById("shipperZip").value=data.custZip;
  	    }else{
  	    document.getElementById("shipperZip").value="";
  	    }
  	    if(data.custCountry){
  	    document.getElementById("shipperCountry").value=data.custCountry;
  	    }else{
  	    document.getElementById("shipperCountry").value="";
  	    }
  	    if(data.custPhone){
  		document.getElementById("shipPho").value=data.custPhone;
  		}else{
  		document.getElementById("shipPho").value="";
  		}
  		if(data.custFax){
  		document.getElementById("shipperFax").value=data.custFax;
  		}else{
  		document.getElementById("shipperFax").value="";
  		}
  		if(data.custEmail){
  		document.getElementById("shipEmai").value=data.custEmail;
  		}else{
  		document.getElementById("shipEmai").value="";
  		}
	}
 }
function getShipperInfo1(ev){  
	if(event.keyCode==9 || event.keyCode==13){
	document.getElementById("shipperName").value="";
		 var params = new Array();
		 params['requestFor'] = "CustAddressForNo";
		 params['custNo'] = document.NewBookingsForm.shipper.value;
	
		  var bindArgs = {
		  url: "<%=path%>/actions/getCustDetails.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
		 };
		 var req = dojo.io.bind(bindArgs);
		 dojo.event.connect(req, "load", this, "populateShipperInfo1");
	}
}
function populateShipperInfo1(type, data, evt) {
 document.getElementById("shipperbutton").style.visibility='visible';
   	if(data){
   	
        document.getElementById("shipperName").value=data.custName;
   	    if(data.custNumber){
   	    document.getElementById("shipper").value=data.custNumber;
   	    }else{
   	    document.getElementById("shipper").value="";
   	    }
   	    if(data.custAddress){
   	    document.getElementById("shipperAddress").value=data.custAddress;
   	    }else{
   	    document.getElementById("shipperAddress").value="";
   	    }
   	    if(data.custCity){
   	    document.getElementById("shipperCity").value=data.custCity;
   	    }else{
   	    document.getElementById("shipperCity").value="";
   	    }
   	    if(data.custState){
   	    document.getElementById("shipperState").value=data.custState;
   	    }else{
   	    document.getElementById("shipperState").value="";
   	    }
   	    if(data.custZip){
   	    document.getElementById("shipperZip").value=data.custZip;
   	    }else{
   	    document.getElementById("shipperZip").value="";
   	    }
   	    if(data.custCountry){
   	    document.getElementById("shipperCountry").value=data.custCountry;
   	    }else{
   	    document.getElementById("shipperCountry").value="";
   	    }
   	    if(data.custPhone){
   		document.getElementById("shipPho").value=data.custPhone;
   		}else{
   		document.getElementById("shipPho").value="";
   		}
   		if(data.custFax){
   		document.getElementById("shipperFax").value=data.custFax;
   		}else{
   		document.getElementById("shipperFax").value="";
   		}
   		if(data.custEmail){
   		document.getElementById("shipEmai").value=data.custEmail;
   		}else{
   		document.getElementById("shipEmai").value="";
   		}
	}
}
function getForwarderInfo(ev){ 
	 jQuery(document).ready(function(){
	  document.getElementById("forwarder").value="";
		 var params = new Array();
		 params['requestFor'] = "CustAddress";
		 params['custName'] = document.NewBookingsForm.fowardername.value;
	
		  var bindArgs = {
		  url: "<%=path%>/actions/getCustDetails.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
		 };
		 var req = dojo.io.bind(bindArgs);
		 dojo.event.connect(req, "load", this, "populateForwarderInfo");
	    });
}
function populateForwarderInfo(type, data, evt) {
 document.getElementById("fowarderbutton").style.visibility='visible';
   	if(data){
        document.getElementById("fowardername").value=data.custName;
   	    if(data.custNumber){
   	    document.getElementById("forwarder").value=data.custNumber;
   	    }else{
   	    document.getElementById("forwarder").value="";
   	    }
   	    if(data.custAddress){
   	    document.getElementById("forwarderAddress").value=data.custAddress;
   	    }else{
   	    document.getElementById("forwarderAddress").value="";
   	    }
   	    if(data.custCity){
   	    document.getElementById("forwarderCity").value=data.custCity;
   	    }else{
   	    document.getElementById("forwarderCity").value="";
   	    }
   	    if(data.custState){
   	    document.getElementById("forwarderState").value=data.custState;
   	    }else{
   	    document.getElementById("forwarderState").value="";
   	    }
   	    if(data.custZip){
   	    document.getElementById("forwarderZip").value=data.custZip;
   	    }else{
   	    document.getElementById("forwarderZip").value="";
   	    }
   	    if(data.custCountry){
   	    document.getElementById("forwarderCountry").value=data.custCountry;
   	    }else{
   	    document.getElementById("forwarderCountry").value="";
   	    }
   	    if(data.custPhone){
   		document.getElementById("forwarderPhone").value=data.custPhone;
   		}else{
   		document.getElementById("forwarderPhone").value="";
   		}
   		if(data.custFax){
   		document.getElementById("forwarderFax").value=data.custFax;
   		}else{
   		document.getElementById("forwarderFax").value="";
   		}
   		if(data.custEmail){
   		document.getElementById("forwarderEmail").value=data.custEmail;
   		}else{
   		document.getElementById("forwarderEmail").value="";
   		}
	 }
}
function getForwarderInfo1(ev){ 
		if(event.keyCode==9 || event.keyCode==13){
		 document.getElementById("fowardername").value="";
		 var params = new Array();
		 params['requestFor'] = "CustAddressForNo";
		 params['custNo'] = document.NewBookingsForm.forwarder.value;
	
		  var bindArgs = {
		  url: "<%=path%>/actions/getCustDetails.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
		 };
		 var req = dojo.io.bind(bindArgs);
		 dojo.event.connect(req, "load", this, "populateForwarderInfo1");
	    }
}		 
function populateForwarderInfo1(type, data, evt) {
 document.getElementById("fowarderbutton").style.visibility='visible';
   	if(data){
        document.getElementById("fowardername").value=data.custName;
   	    if(data.custNumber){
   	    document.getElementById("forwarder").value=data.custNumber;
   	    }else{
   	    document.getElementById("forwarder").value="";
   	    }
   	    if(data.custAddress){
   	    document.getElementById("forwarderAddress").value=data.custAddress;
   	    }else{
   	    document.getElementById("forwarderAddress").value="";
   	    }
   	    if(data.custCity){
   	    document.getElementById("forwarderCity").value=data.custCity;
   	    }else{
   	    document.getElementById("forwarderCity").value="";
   	    }
   	    if(data.custState){
   	    document.getElementById("forwarderState").value=data.custState;
   	    }else{
   	    document.getElementById("forwarderState").value="";
   	    }
   	    if(data.custZip){
   	    document.getElementById("forwarderZip").value=data.custZip;
   	    }else{
   	    document.getElementById("forwarderZip").value="";
   	    }
   	    if(data.custCountry){
   	    document.getElementById("forwarderCountry").value=data.custCountry;
   	    }else{
   	    document.getElementById("forwarderCountry").value="";
   	    }
   	    if(data.custPhone){
   		document.getElementById("forwarderPhone").value=data.custPhone;
   		}else{
   		document.getElementById("forwarderPhone").value="";
   		}
   		if(data.custFax){
   		document.getElementById("forwarderFax").value=data.custFax;
   		}else{
   		document.getElementById("forwarderFax").value="";
   		}
   		if(data.custEmail){
   		document.getElementById("forwarderEmail").value=data.custEmail;
   		}else{
   		document.getElementById("forwarderEmail").value="";
   		}
	 }
}
function getConsigneeInfo(ev){ 
	jQuery(document).ready(function(){
	document.getElementById("consignee").value="";
		 var params = new Array();
		 params['requestFor'] = "CustAddress";
		 params['custName'] = document.NewBookingsForm.consigneename.value;
	
		  var bindArgs = {
		  url: "<%=path%>/actions/getCustDetails.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
		 };
		 var req = dojo.io.bind(bindArgs);
		 dojo.event.connect(req, "load", this, "populateConsigneeInfo");
	    });
}		 
function populateConsigneeInfo(type, data, evt) {
document.getElementById("consigneebutton").style.visibility='visible';
   	if(data){
        document.getElementById("consigneename").value=data.custName;
   	    if(data.custNumber){
   	    document.getElementById("consignee").value=data.custNumber;
   	    }else{
   	    document.getElementById("consignee").value="";
   	    }
   	    if(data.custAddress){
   	    document.getElementById("consigneeAddress").value=data.custAddress;
   	    }else{
   	    document.getElementById("consigneeAddress").value="";
   	    }
   	    if(data.custCity){
   	    document.getElementById("consigneeCity").value=data.custCity;
   	    }else{
   	    document.getElementById("consigneeCity").value="";
   	    }
   	    if(data.custState){
   	    document.getElementById("consigneeState").value=data.custState;
   	    }else{
   	    document.getElementById("consigneeState").value="";
   	    }
   	    if(data.custZip){
   	    document.getElementById("consigneeZip").value=data.custZip;
   	    }else{
   	    document.getElementById("consigneeZip").value="";
   	    }
   	    if(data.custCountry){
   	    document.getElementById("consigneeCountry").value=data.custCountry;
   	    }else{
   	    document.getElementById("consigneeCountry").value="";
   	    }
   	    if(data.custPhone){
   		document.getElementById("consigneePhone").value=data.custPhone;
   		}else{
   		document.getElementById("consigneePhone").value="";
   		}
   		if(data.custFax){
   		document.getElementById("consigneeFax").value=data.custFax;
   		}else{
   		document.getElementById("consigneeFax").value="";
   		}
   		if(data.custEmail){
   		document.getElementById("consigneeEmail").value=data.custEmail;
   		}else{
   		document.getElementById("consigneeEmail").value="";
   		}
	}
}   
function getConsigneeInfo1(ev){ 
	if(event.keyCode==9 || event.keyCode==13){
	document.getElementById("consigneename").value="";
	 var params = new Array();
	 params['requestFor'] = "CustAddressForNo";
	 params['custNo'] = document.NewBookingsForm.consignee.value;
	 var bindArgs = {
		  url: "<%=path%>/actions/getCustDetails.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
	 };
	 var req = dojo.io.bind(bindArgs);
 	 dojo.event.connect(req, "load", this, "populateConsigneeInfo1");
   }
}
function populateConsigneeInfo1(type, data, evt) {
document.getElementById("consigneebutton").style.visibility='visible';
 	if(data){
     document.getElementById("consigneename").value=data.custName;
	    if(data.custNumber){
	    document.getElementById("consignee").value=data.custNumber;
	    }else{
	    document.getElementById("consignee").value="";
	    }
	    if(data.custAddress){
	    document.getElementById("consigneeAddress").value=data.custAddress;
	    }else{
	    document.getElementById("consigneeAddress").value="";
	    }
	    if(data.custCity){
	    document.getElementById("consigneeCity").value=data.custCity;
	    }else{
	    document.getElementById("consigneeCity").value="";
	    }
	    if(data.custState){
	    document.getElementById("consigneeState").value=data.custState;
	    }else{
	    document.getElementById("consigneeState").value="";
	    }
	    if(data.custZip){
	    document.getElementById("consigneeZip").value=data.custZip;
	    }else{
	    document.getElementById("consigneeZip").value="";
	    }
	    if(data.custCountry){
	    document.getElementById("consigneeCountry").value=data.custCountry;
	    }else{
	    document.getElementById("consigneeCountry").value="";
	    }
	    if(data.custPhone){
		document.getElementById("consigneePhone").value=data.custPhone;
		}else{
		document.getElementById("consigneePhone").value="";
		}
		if(data.custFax){
		document.getElementById("consigneeFax").value=data.custFax;
		}else{
		document.getElementById("consigneeFax").value="";
		}
		if(data.custEmail){
		document.getElementById("consigneeEmail").value=data.custEmail;
		}else{
		document.getElementById("consigneeEmail").value="";
		}
	 }
}
function getTruckerInfo(){
	if(event.keyCode==9 || event.keyCode==13){
	 document.getElementById("truckerCode").value="";
	 var params = new Array();
	 params['requestFor'] = "CustAddress";
	 params['custName'] = document.NewBookingsForm.truckerName.value;
	  var bindArgs = {
		  url: "<%=path%>/actions/getCustDetails.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
	 };
	 var req = dojo.io.bind(bindArgs);
	 dojo.event.connect(req, "load", this, "populateTruckerInfo");
    }
}
function populateTruckerInfo(type, data, evt) {
 document.getElementById("truckerContactButton").style.visibility='visible';
  	if(data){
       document.getElementById("truckerName").value=data.custName;
 	    if(data.custNumber){
     	    document.getElementById("truckerCode").value=data.custNumber;
   	    }else{
	   	    document.getElementById("truckerCode").value="";
   	    }
   	    if(data.custAddress){
	   	    document.getElementById("addressoftrucker").value=data.custAddress;
   	    }else{
	   	    document.getElementById("addressoftrucker").value="";
   	    }
   	    if(data.custPhone){
	   		document.getElementById("truckerPhone").value=data.custPhone;
   		}else{
	   		document.getElementById("truckerPhone").value="";
   		}
   		if(data.custEmail){
	   		document.getElementById("truckerEmail").value=data.custEmail;
 		}else{
	   		document.getElementById("truckerEmail").value="";
 		}
   }
}
function getTruckerInfo1(ev){ 
	if(event.keyCode==9 || event.keyCode==13){
	document.getElementById("truckerName").value="";
	 var params = new Array();
	 params['requestFor'] = "CustAddressForNo";
	 params['custNo'] = document.NewBookingsForm.truckerCode.value;
  var bindArgs = {
  url: "<%=path%>/actions/getCustDetails.jsp",
  error: function(type, data, evt){alert("error");},
  mimetype: "text/json",
  content: params
 };
var req = dojo.io.bind(bindArgs);
dojo.event.connect(req, "load", this, "populateTruckerInfo1");
}
}
function populateTruckerInfo1(type, data, evt) {
	 document.getElementById("truckerContactButton").style.visibility = 'visible';
 	if(data){
       document.getElementById("truckerName").value=data.custName;
 	    if(data.custNumber){
	   	    document.getElementById("truckerCode").value=data.custNumber;
	    }else{
	   	    document.getElementById("truckerCode").value="";
   	    }
   	    if(data.custAddress){
	   	    document.getElementById("addressoftrucker").value=data.custAddress;
   	    }else{
	   	    document.getElementById("addressoftrucker").value="";
   	    }
   	    if(data.custPhone){
	   		document.getElementById("truckerPhone").value=data.custPhone;
   		}else{
	   		document.getElementById("truckerPhone").value="";
   		}
   		if(data.custEmail){
	   		document.getElementById("truckerEmail").value=data.custEmail;
   		}else{
	   		document.getElementById("truckerEmail").value="";
   		}
    }
}		   
function getShipper(){  
		       var cust=document.NewBookingsForm.shipperName.value;
		       if(cust=='%'){
		         cust = 'percent';
		       }
		       var customer=cust.replace("&","amp;");
		       GB_show('Shipper','<%=path%>/quoteCustomer.do?buttonValue=BookingShipper&clientName='+customer,
  width="600",height="800");
			 }
function getForwarder(){  
		       var cust=document.NewBookingsForm.fowardername.value;
		       if(cust=='%'){
		         cust = 'percent';
		       }
		       var customer=cust.replace("&","amp;");
		       GB_show('Forwarder','<%=path%>/quoteCustomer.do?buttonValue=BookingForwarder&clientName='+customer,
  width="600",height="800");
			 }
	function getTrukerName(){  
		       var cust=document.NewBookingsForm.truckerName.value;
		       if(cust=='%'){
		         cust = 'percent';
		       }
		       var customer=cust.replace("&","amp;");
		       GB_show('Trucker','<%=path%>/quoteCustomer.do?buttonValue=BookingTruker&clientName='+customer,
  width="600",height="800");
			 }		 
			 
function getConsignee(){
 var cust=document.NewBookingsForm.consigneename.value;
 if(cust=='%'){
	cust = 'percent';
}
		       var customer=cust.replace("&","amp;");
		       GB_show('Consignee','<%=path%>/quoteCustomer.do?buttonValue=BookingConsignee&clientName='+customer,
  width="600",height="800");
}
 function getBookingCustomer(val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11){
	 if(val11=='BookingTruker'){
	 document.getElementById("truckerContactButton").style.visibility = 'visible';
		document.NewBookingsForm.truckerName.value=val1;
		document.NewBookingsForm.truckerCode.value=val2;
		document.NewBookingsForm.addressoftrucker.value=val3;
		document.NewBookingsForm.truckerPhone.value=val8;
		document.NewBookingsForm.truckerEmail.value=val10;
}
 if(val11=='BookingShipper'){
 document.getElementById("shipperbutton").style.visibility='visible';
		document.NewBookingsForm.shipperName.value=val1;
		document.NewBookingsForm.shipper.value=val2;
		document.NewBookingsForm.shipperAddress.value=val3;
		document.NewBookingsForm.shipperCity.value=val4;
		document.NewBookingsForm.shipperState.value=val5;
		document.NewBookingsForm.shipperZip.value=val6;
		document.NewBookingsForm.shipperCountry.value=val7;
		document.NewBookingsForm.shipPho.value=val8;
		document.NewBookingsForm.shipperFax.value=val9;
		document.NewBookingsForm.shipEmai.value=val10;
}
if(val11=='BookingForwarder'){
 document.getElementById("fowarderbutton").style.visibility='visible';
		document.NewBookingsForm.fowardername.value=val1;
		document.NewBookingsForm.forwarder.value=val2;
		document.NewBookingsForm.forwarderAddress.value=val3;
		document.NewBookingsForm.forwarderCity.value=val4;
		document.NewBookingsForm.forwarderState.value=val5;
		document.NewBookingsForm.forwarderZip.value=val6;
		document.NewBookingsForm.forwarderCountry.value=val7;
		document.NewBookingsForm.forwarderPhone.value=val8;
		document.NewBookingsForm.forwarderFax.value=val9;
		document.NewBookingsForm.forwarderEmail.value=val10;
}if(val11=='BookingConsignee'){
document.getElementById("consigneebutton").style.visibility='visible';
		document.NewBookingsForm.consigneename.value=val1;
		document.NewBookingsForm.consignee.value=val2;
		document.NewBookingsForm.consigneeAddress.value=val3;
		document.NewBookingsForm.consigneeCity.value=val4;
		document.NewBookingsForm.consigneeState.value=val5;
		document.NewBookingsForm.consigneeZip.value=val6;
		document.NewBookingsForm.consigneeCountry.value=val7;
		document.NewBookingsForm.consigneePhone.value=val8;
		document.NewBookingsForm.consigneeFax.value=val9;
		document.NewBookingsForm.consigneeEmail.value=val10;
} if(val11=='AccountNameShipper'){
		document.NewBookingsForm.accountName.value=val1;
		document.NewBookingsForm.accountNumber.value=val2;
}if(val11=='AccountNameForwarder'){
		document.NewBookingsForm.accountName.value=val1;
		document.NewBookingsForm.accountNumber.value=val2;
	
}if(val11=='AccountNameThirdParty'){
		document.NewBookingsForm.accountName.value=val1;
		document.NewBookingsForm.accountNumber.value=val2;
}
}
function getShipperAddress(){
	if(document.NewBookingsForm.shipper.value==""){
	alert("Please Select Account Name");
		return;
	}
	var addr=document.NewBookingsForm.shipperAddress.value;
	if(addr=='%'){
		         addr = 'percent';
		       }
	GB_show('Shipper','<%=path%>/contactAddress.do?buttonValue=BookingShipper&clientNo='
	+document.NewBookingsForm.shipper.value+
	'&address='+addr,width="600",height="800");
	}
function getForwarderAddress(){
if(document.NewBookingsForm.fowardername.value==""){
alert("Please Select Account Name");
return;
}
var addr=document.NewBookingsForm.forwarderAddress.value;
if(addr=='%'){
		         addr = 'percent';
		       }
GB_show('Forwarder','<%=path%>/contactAddress.do?buttonValue=BookingForwarder&clientNo='+document.NewBookingsForm.forwarder.value+
'&address='+addr,width="600",height="800");
}
function getConsigneeAddress(){
if(document.NewBookingsForm.consigneename.value==""){
alert("Please Select Account Name");
return;
}
var addr=document.NewBookingsForm.consigneeAddress.value;
if(addr=='%'){
		         addr = 'percent';
		       }
GB_show('Consignee','<%=path%>/contactAddress.do?buttonValue=BookingConsignee&clientNo='+document.NewBookingsForm.consignee.value+
'&address='+addr,width="600",height="800");
}
function getTruckerAddress(){
if(document.NewBookingsForm.truckerName.value==""){
alert("Please Select Account Name");
return;
}
var addr=document.NewBookingsForm.addressoftrucker.value;
if(addr=='%'){
	addr = 'percent';
}
GB_show('Trucker','<%=path%>/contactAddress.do?buttonValue=BookingTruker&clientNo='+document.NewBookingsForm.truckerCode.value+
'&address='+addr,width="600",height="800");
}
function refreshPage(val1,val2,val3,val4,val5,val6,val7,val8){
	  document.NewBookingsForm.unitSelect.value=val1;
	  document.NewBookingsForm.number.value=val2;
	  document.NewBookingsForm.chargeCode.value=val3;
	  document.NewBookingsForm.chargeCodeDesc.value=val4;
	  document.NewBookingsForm.costSelect.value=val5;
	  document.NewBookingsForm.currency1.value=val6;
	  document.NewBookingsForm.chargeAmt.value=val7;
	  document.NewBookingsForm.minimumAmt.value=val8;
	  document.NewBookingsForm.buttonValue.value="addCharges"
	  document.NewBookingsForm.submit();
	}
	function getWareHouseAdd(ev){ 
	 document.NewBookingsForm.emptypickupaddress.value="";
		if(event.keyCode==9 || event.keyCode==13){
		var params = new Array();
	 	params['requestFor'] = "wareHouseAddress";
	 	params['wareHouseName'] = document.NewBookingsForm.exportPositioning.value;
  		var bindArgs = {
  				url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
  				error: function(type, data, evt){alert("error");},
 				mimetype: "text/json",
  				content: params
 			};
			var req = dojo.io.bind(bindArgs);

			dojo.event.connect(req, "load", this, "populateAddress");
		}
}
function populateAddress(type, data, evt) {
 	if(data){
       if(data.WareHouseAddress){
	   	    document.NewBookingsForm.emptypickupaddress.value=data.WareHouseAddress;
	    }else{
	   	    document.NewBookingsForm.emptypickupaddress.value="";
   	    }
   	     if(data.WareHouseName){
   	     document.NewBookingsForm.loadLocation.value=data.WareHouseName;
   	     }
   	      if(data.WareHouseAddress){
	   	    document.NewBookingsForm.addressofDelivery.value=data.WareHouseAddress;
	    }else{
	   	    document.NewBookingsForm.addressofDelivery.value="";
   	    }
   	    if(data.WareHouseName==undefined){
   	    	document.NewBookingsForm.emptypickupaddress.value="";
   	    	document.NewBookingsForm.loadLocation.value=document.NewBookingsForm.exportPositioning.value;
   	    }
   	  
    }
}	

function getWareHouseAdd1(ev){ 
	document.NewBookingsForm.addressofDelivery.value="";
	if(event.keyCode==9 || event.keyCode==13){
	
	 var params = new Array();
	 params['requestFor'] = "wareHouseAddress";
	 params['wareHouseName'] = document.NewBookingsForm.loadLocation.value;
  var bindArgs = {
  url: "<%=path%>/actions/getChargeCodeDesc1.jsp",
  error: function(type, data, evt){alert("error");},
  mimetype: "text/json",
  content: params
 };
var req = dojo.io.bind(bindArgs);

dojo.event.connect(req, "load", this, "populateAddress1");
}
}
function populateAddress1(type, data, evt) {
 	if(data){
       if(data.WareHouseAddress){
	   	    document.NewBookingsForm.addressofDelivery.value=data.WareHouseAddress;
	    }else{
	   	    document.NewBookingsForm.addressofDelivery.value="";
   	    }
   	     if(data.WareHouseName){
   	     document.NewBookingsForm.loadLocation.value=data.WareHouseName;
   	     }
    }
}
function getAccountNumberDojo(ev){
	 if(event.keyCode==9 || event.keyCode==13){
	     document.getElementById("accountNumber").value="";
		 var params = new Array();
		 params['requestFor'] = "CustAddress";
		 params['custName'] = document.NewBookingsForm.accountName.value;
		  var bindArgs = {
		  url: "<%=path%>/actions/getCustDetails.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
		 };
		 var req = dojo.io.bind(bindArgs);
		 dojo.event.connect(req, "load", this, "populateAccountNumber");
	    }
}
function populateAccountNumber(type, data, evt) {
   	if(data){
         document.getElementById("accountName").value=data.custName;
   	    if(data.custNumber){
   	     document.getElementById("accountNumber").value=data.custNumber;
   	    }
 }	
}
function test(){
     var tt;
	 if(document.NewBookingsForm.prepaidToCollect[0].checked){
	    	tt="P";
	  		document.getElementById("optionId").disabled=false;
	 }else if(document.NewBookingsForm.prepaidToCollect[2].checked){
	        document.NewBookingsForm.option.value='ThirdParty';
	        tt="T";
	        document.getElementById("optionId").disabled=true;
	 }
  //dojo.widget.byId('autoTextbox').action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=7&pcollect="+tt;
}
function test2(){
    	if(document.NewBookingsForm.prepaidToCollect[1].checked){
      		document.getElementById("optionId").disabled=false;	
		}
}
function getAccountDetails(val2)
{

   var option =document.NewBookingsForm.option.value;
  
   if(option=="00")
   {
         alert("Please select the option");
         return;
   }
   if(option == 'Shipper'){
       var cust=document.NewBookingsForm.accountName.value;
       if(cust=='%'){
		         cust = 'percent';
		       }
       var customer=cust.replace("&","amp;");
         GB_show('Shipper','<%=path%>/quoteCustomer.do?buttonValue=AccountNameShipper&clientName='+customer,width="600",height="800");
	 }else if(option == 'Forwarder'){
       var cust=document.NewBookingsForm.accountName.value;
       if(cust=='%'){
		         cust = 'percent';
		       }
       var customer=cust.replace("&","amp;");
         GB_show('Forwarder','<%=path%>/quoteCustomer.do?buttonValue=AccountNameForwarder&clientName='+customer,width="600",height="800");
	 }else if(option == 'ThirdParty'){
       var cust=document.NewBookingsForm.accountName.value;
       if(cust=='%'){
		         cust = 'percent';
		       }
       var customer=cust.replace("&","amp;");
         GB_show('ThirdParty','<%=path%>/quoteCustomer.do?buttonValue=AccountNameThirdParty&clientName='+customer,width="600",height="800");
	 }
 }
function getInsurance(){
if(document.NewBookingsForm.insurance[1].checked){
	 alert("Please select Insurance");
	 return;
	 }
	if(document.NewBookingsForm.costofgoods1.value==0.00){
	alert("Please enter Cost Of Goods");
	return;
	}
	 document.NewBookingsForm.buttonValue.value="insurance"
	 document.NewBookingsForm.submit();
}
  function getAgent(){
	 var portOfDischarge=document.NewBookingsForm.portOfDischarge.value;
	 var destination=document.NewBookingsForm.destination.value
	GB_show('Agent','<%=path%>/quoteAgent.do?buttonValue=Agent&portOfDischarge='+portOfDischarge+'&destination='+destination,
  					width="400",height="600");
	}
	function getAgentInfo(val1){
	  document.NewBookingsForm.agent.value=val1;
	}
	function getDestination(){
				 var params = new Array();
				 params['requestFor'] = "AGENT";
				 params['finalDestination'] = document.NewBookingsForm.destination.value;
				 var bindArgs = {
				  
				  url: "<%=path%>/jsps/fclQuotes/getAgent.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateAgentDojo");
			    }
function populateAgentDojo(type, data, evt) {
		   	if(data){
		   	if(data.Agent!=undefined){
		   		document.getElementById("agent").value=data.Agent;
		   		}
		   	}
}
function getPod(){
   var params = new Array();
		 params['requestFor'] = "PODAGENT";
		 params['portofDischarge'] = document.NewBookingsForm.portOfDischarge.value;
		 var bindArgs = {
		  
		  url: "<%=path%>/jsps/fclQuotes/getAgent.jsp",
		  error: function(type, data, evt){alert("error");},
		  mimetype: "text/json",
		  content: params
		 };
		 
		 var req = dojo.io.bind(bindArgs);
		 dojo.event.connect(req, "load", this, "populateAgent1Dojo");
}
function populateAgent1Dojo(type, data, evt) {
		   	if(data){
	if(data.Agent!=undefined){
		  document.getElementById("agent").value=data.Agent;}
	}
}
function getSslCode(){ 
	var cust=document.NewBookingsForm.sslDescription.value;
	var customer=cust.replace("&","amp;");
	if(customer=='%'){
		customer = 'percent';
	}
 	GB_show('Client Info','<%=path%>/quoteCustomer.do?buttonValue=CarrierQuotation&clientName='+customer,
  					width="400",height="600");
}
function getCarrier(val1,val2,val3,val4,val5,val6,val7){
 document.NewBookingsForm.sslDescription.value=val1;
}
function setAddress(ev){
	if(document.NewBookingsForm.emptypickupaddress.value){
	  document.NewBookingsForm.addressofDelivery.value=document.NewBookingsForm.emptypickupaddress.value;
	}
}
function goBack(){
  	  document.NewBookingsForm.buttonValue.value="goBack";
	  document.NewBookingsForm.submit();
}
</script>
<%@include file="../includes/resources.jsp" %>
 </head>

<body class="whitebackgrnd">
<html:form action="/newBookings" name="NewBookingsForm" type="com.gp.cvst.logisoft.struts.form.NewBookingsForm" scope="request">
<font color="blue" size="4"><%=msg%></font>
<table border="0" width="100%">
<tr class="textlabels">
<td>
<table>
<tr>
   <td class="textlabels" >File No :
  		${bookingValues.fileNo}</td>
  		 <td class="textlabels" ><b>File Start Date : </b>
  		 <c:choose>
  		 <c:when  test="${bookingValues.bookingDate!=null}">
		  		 <fmt:formatDate pattern="MM/dd/yyyy hh:mm a" value= "${bookingValues.bookingDate}"/> 
  		 </c:when>
  		 <c:otherwise>
	  		<%=bookingDate%>
  		 </c:otherwise>
  		 </c:choose>
  		<c:if test="${bookingValues.bookingDate!=null}">
  		</c:if>
  		</td>
  		  <td class="textlabels"><b>Booking By : </b><%=bookingBy%></td>
  	</tr>
  </table>
  </td>
</tr>
 <tr>
   <td >
         <input type="button" value="Go Back" onclick="goBack()" class="buttonStyleNew"/>
         <input type="button" id="saveDraft" value="Save To Draft" onclick="parent.parent.gatherFormData(this.form,'BOOKING')" class="buttonStyleNew" style="width:80px;"/>
 		 <input type="button" id="restore" value="Restore" onclick="parent.parent.restoreFormData(this.form,'BOOKING')" class="buttonStyleNew"/>
    <input type="button" value="Save" onclick="SAVE()" class="buttonStyleNew"/>
     <input type="button" class="buttonStyleNew" value="Insurance" onclick="getInsurance()"></input>
<input type="button"  class="buttonStyleNew" id="charge" value="Charges" 
onClick="return GB_show('Quote Charges','<%=path%>/jsps/fclQuotes/BookingCharges.jsp?hazmat='+document.NewBookingsForm.hazmat.value+
'&spcleqpmt='+document.NewBookingsForm.specialequipment.value+'&soc='+document.NewBookingsForm.soc.value+'&button='+'quote',300,600)"/>
	 <input type="button" class="buttonStyleNew" value="Hazmat" onclick="getPopHazmat()">		
	 <input type="button" class="buttonStyleNew" id="note" onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId='+'<%=NotesConstants.BOOKING%>',300,700);" id="note" name="search" value="Note"/>
		</td>
  </tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"  style="padding-left:0px;">
    <tr><td valign='top'>
         <table width="100%" border="0" cellpadding="0" cellspacing="0" >
            <tr>
		       <td width="100%">
                      <table width="100%" border="0" cellpadding="0" cellspacing="0"  >	
						<tr>
						   <td width="25%" valign="top">
						       	<table width="100%" border="0" cellpadding="13" cellspacing="0" class="tableBorderNew" style="padding-left:0px;">
		 							<tr class="tableHeadingNew">Booking Information</tr>
		 	  					   <tr class="textlabels">
		 	  					    <td ><span onmouseover="tooltip.show('<strong>SS LineBkg Repre</strong>',null,event);" onmouseout="tooltip.hide();">SSLBkg Rep</span></td>
		                             <td align="left"><html:text property="SSLineBookingRep" size="12"  value="${bookingForm.SSLineBookingRep}" /></td>
						          </tr>
		 	              		  <tr>	
		 	              			   <td class="textlabels">SS Bkg#</td>
		 	               			   <td><html:text property="SSBooking" size="12" value="${bookingForm.SSBooking}" /></td>
		 	              				<td class="textlabels">Tel # </td>
              			 				<td><html:text property="telePho" size="12" value="${bookingForm.telePho}" onkeypress="getIt(this)" maxlength="13"/></td>
		 	              		   </tr>
		 	  					   <tr class="textlabels">
<%--		 	  					       <td class="textlabels">Client Ref</td>--%>
<%--		 	               			   <td><html:text property="contractReference" size="12" value="${bookingForm.contractReference}" /> </td>--%>
		 	               			   <td align="right"> Email</td>
		 	        	 	    	   <td><html:text property="bookingemail" value="${bookingForm.bookingemail}" size="12"></html:text></td>
		 	              		   </tr>
		 	              		   <tr>
		 	              			   <td align="left" class="style2">Quote Id</td>
		 	                  		   <td><html:text property="quoteId" readonly="true" size="12"/></td>
		 	     	 				   <td class="textlabels" style="padding-right:20px;">Contact </td>
		 	        <td  valign="top" align="left"><html:text property="attenName" size="12" value="${bookingForm.attenName}" /></td>
		 	        	 	   	  </tr>
		 	   					  <tr class="textlabels">
		 	                 		  <td><span onmouseover="tooltip.show('<strong>Sales Represent Code</strong>',null,event);" onmouseout="tooltip.hide();">SR Code</span></td>
              		   				 <td><html:text property="slaesRepCode" size="12" value="${bookingForm.slaesRepCode}" styleId="radio4"/></td> 
		 	                   		
		 	        	 	      </tr>
		 	        	 	    </table>
						   </td>
						   <td width="1%" valign="top">&nbsp;</td>
						   <td width="74%" valign="top" valign='top'>
						       <table width="100%" border="0" width="100%">
								<tr>
									<td width="70%" valign="top">
									     <table width="100%" border="0" cellpadding="4" cellspacing="0" class="tableBorderNew">
			 								<tr class="tableHeadingNew" >Carrier Information</tr>
			 								<tr>
		 	            						  <td class="textlabels">Carrier</td>
        										  <td ><span class="textlabels">
         				 						      <input name="sslDescription" id="sslDescription" maxlength= "7" size="10" value="${bookingForm.sslDescription}"  /> 
              		    							  <input type="button" value="Look Up"  class="buttonStyleNew" onclick="getSslCode()" style="width:50px">
        				         				  </td>      
		 	             						  <td align="right" class="textlabels">Vessel</td>
              									  <td ><span class="textlabels">		     
           					                     <input name="vessel" id="vessel" maxlength= "7" size="12" value="${bookingForm.vessel}" /> 
              		                             <dojo:autoComplete formId="NewBookingsForm" textboxId="vessel" action="<%=path%>/actions/getVesselName.jsp?tabName=BOOKING&from=0"/></span></td>
		 	     							</tr>
		 	     							<tr>
		 	     							<td colspan="4"> </td>
		 	     							</tr>
		 	     						    <tr>
		 	             					  <td align="left" ><span class="textlabels">CommCode</span><font color="red">*</font></td>
              		    					   <td><span class="textlabels">
				<input name="commcode" id="commcode" maxlength= "7" size="10" value="${bookingForm.commcode}" onkeydown="getComCodeDesc(this.value)"  /> 
            <dojo:autoComplete formId="NewBookingsForm" textboxId="commcode" action="<%=path%>/actions/getChargeCode.jsp?tabName=BOOKING"/>
              		    					   </td> 
					<td align="right"><span class="textlabels">Descr</span></td>
		 	                <td class="headerbluelarge" align="left"> 
		 	             <input name="comdesc" id="comdesc" value="${bookingForm.comdesc}"  size="12" onkeydown="getComCode(this.value)" />
          		<dojo:autoComplete formId="NewBookingsForm" textboxId="comdesc" action="<%=path%>/actions/getChargeCodeDesc.jsp?tabName=BOOKING"/> 
	     </tr>
		<tr>
	<td align="left" class="textlabels" align="center">Voy</td>
		 	   <td align="left"><html:text property="voyage" size="10" value="${bookingForm.voyage}"/></td>
		 	   <td class="textlabels" align="right">ETD</td>
	<td class="textlabels"><fmt:formatDate pattern="MM/dd/yyyy" var="estimatedDate" value="${bookingValues.etd}"/>
              	<html:text property="estimatedDate" size="9" value="${estimatedDate}" styleId="txtcal22" onchange="validateDate1()" readonly="true"/>
              	<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal22" onmousedown="insertDateFromCalendar(this.id,0);"/></td>
	 </tr> 
	 <tr>
		 <td class="textlabels">Voyage Int</td>
		 <td><html:text property="vaoyageInternational" size="10" value="${bookingForm.vaoyageInternational}"/></td>      
	 	 <td align="right" class="textlabels">ETA</td>
	     <td  valign="top"><fmt:formatDate pattern="MM/dd/yyyy" var="estimatedAtten" value="${bookingValues.eta}"/>
	          <html:text property="estimatedAtten" size="9" value= "${estimatedAtten}" styleId="txtcal5" onchange="validateDate()" readonly="true"/>
	          <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal5" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
	 </tr>
		</table>
	</td>					
		<td width="1%" valign="top">&nbsp;</td>
	<td width="29%" valign="top">
			<table width="100%" border="0" cellpadding="1" cellspacing="0" class="tableBorderNew" style="padding-left:10px;">
			<tr class="tableHeadingNew" height="90%">Cut-Off Dates</tr>
		  <tr  class="textlabels">
	 	<td><span onmouseover="tooltip.show('<strong>Rail Cutoff</strong>',null,event);" onmouseout="tooltip.hide();">Rail</span></td>
			<td  valign="top"><fmt:formatDate pattern="MM/dd/yyyy hh:mm" var="railCutOff" value="${bookingValues.railCutOff}"/>
		 <html:text property="railCutOff" value="${railCutOff}"  size="10" styleId="txtcal81"/>
		  <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal81" onmousedown="insertDateFromCalendar(this.id,0);" />&nbsp;</td>
		 </tr>
	<tr class="textlabels"> 
		 <td><span onmouseover="tooltip.show('<strong>Port CutOff</strong>',null,event);" onmouseout="tooltip.hide();">Port</span></td>
		 	  <td class="textlabels" align="left">
		 	  <fmt:formatDate pattern="MM/dd/yyyy hh:mm" var="portCutOff" value="${bookingValues.portCutOff}"/>
		 	  <html:text property="portCutOff"  size="10" value="${portCutOff}" styleId="txtcal33"/>
		<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal33" onmousedown="insertDateFromCalendar(this.id,0);"/></td>
		  </tr>
 <tr  class="textlabels">
		 <td><span onmouseover="tooltip.show('<strong>Doc Cutoff</strong>',null,event);" onmouseout="tooltip.hide();">Doc</span></td>
<td align="left">
 <fmt:formatDate pattern="MM/dd/yyyy hh:mm" var="docCutOff" value="${bookingValues.docCutOff}"/>
<html:text property="docCutOff" value="${docCutOff}" styleClass="error" size="10" styleId="txtcal60" onclick="AlertMessage()"/>
	<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16"  height="16" id="cal60" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
	</tr>
		  <tr  class="textlabels">			
   <td ><span onmouseover="tooltip.show('<strong>Barge CutOff</strong>',null,event);" onmouseout="tooltip.hide();">Barge</span></td>
      <td valign="top">
       <fmt:formatDate pattern="MM/dd/yyyy hh:mm" var="voyageDocCutOff" value="${bookingValues.voyDocCutOff}"/>
      <html:text property="voyageDocCutOff"   size="10"   value="${voyageDocCutOff}" styleId="txtcal98"/>
   <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal98" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
	 </tr>
     <tr  class="textlabels">		
       	<td ><span onmouseover="tooltip.show('<strong>Auto CutOff</strong>',null,event);" onmouseout="tooltip.hide();">Auto</span></td>	
		<td>
		 <fmt:formatDate pattern="MM/dd/yyyy hh:mm" var="cutoffDate" value="${bookingValues.cutofDate}"/>
		<html:text styleId="txtcal4" property="cutoffDate" size="10" value="${cutoffDate}" />
	<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal4" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
		</tr>
		</table>
	</td>
	</tr>		
	<tr>
		<td colspan="3" style="padding-top:2px">
		<table width="100%" class="tableBorderNew" border="0" cellspacing="0" cellpadding="0">  
		<tr class="tableHeadingNew">Billing</tr>
			<tr class="textlabels">
					 <td>Bill To Code</td>
					 <td><html:radio property="billToCode" value="F" name="bookingForm"/>F
		      			 <html:radio property="billToCode" value="S" name="bookingForm"/>S
			  			 <html:radio property="billToCode" value="T" name="bookingForm"/>T 
			  		 </td>
		   			 <td>&nbsp;Booking Complete </td>
		    		 <td><html:radio property="bookingComplete" value="Y" name="bookingForm"/>Y
		    			 <html:radio property="bookingComplete" value="N" name="bookingForm"/>N
		    		 </td>
			 </tr>
		     <tr class="textlabels">
 				 	<td>Routed By Agent</td>
  			     	<td><html:text property="routedByAgent" value="${bookingValues.routedByAgent}" size="10" ></html:text></td>
      			 	<td>Alternate Agent</td>
  			     	<td><html:text property="alternateAgent"  value="${bookingValues.alternateAgent}" size="10" ></html:text></td>
  			     	
		     </tr>
		     <tr class="textlabels">
		       <td>Prepaid / Collect / Third Party</td>
				      <td><html:radio property="prepaidToCollect" value="P" name="bookingForm"  onclick="test()"/>P
		    			<html:radio property="prepaidToCollect" value="C" name="bookingForm" onclick="test2()"/>C
		    			<html:radio property="prepaidToCollect" value="T" name="bookingForm" onclick="test()"/>T
		   			 </td>
		   			<td>Select </td>
				      <td valign="top"><html:select property="option"  styleClass="textdatestyle" styleId="optionId" style="width:85px;">
	    							<html:optionsCollection name="optionList"  />
	    			</html:select></td>
		     </tr>
		     <tr>
		     <td colspan="2" class="textlabels">Account Name<input name="accountName" id="accountName" size="10"  value="${bookingValues.accountName}"   /> 
<%--              			<dojo:autoComplete formId="NewBookingsForm" id="autoTextbox" textboxId="accountName"  action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=7"/>--%>
        			<input type="button" id="toggle" value="LookUp" class="buttonStyleNew" onclick="getAccountDetails(this.value)" style="width:50px">
        			</td>
  			     	<td class="textlabels">Account Number</td>
  			     	<td><html:text property="accountNumber" styleId="accountNumber1" size="10" value="${bookingValues.accountNumber}"></html:text></td>
		     </tr>
			</table>
			</td>
			</tr>     
			</table>	
			</td>
			</tr>   
	     </table>	
        </td>
       </tr>   
  </table>
           
     <br style="margin-top:5px"/> 
     
<table width="100%" border=0" cellpadding="4" cellspacing="0" class="tableBorderNew" style="padding-left:10px;"> 
   <tr class="tableHeadingNew" >Trade Route</tr>    
   <tr>
        <td class="textlabels" width="7%">Origin </td>
		<td><input  name="originTerminal" id="originTerminal" value="${bookingForm.originTerminal}" Class="textlabelsBoldForTextBox mandatory"  size="14" />
		      <span class="hotspot" onmouseover="tooltip.show('<strong>Search Orgin</strong>',null,event);" onmouseout="tooltip.hide();">
                          <img src="<%=path%>/img/icons/web.gif" border="0"
                            onClick="return GB_show('Orgin Search', '<%=path%>/newBookings.do?buttonValue=searchPort&textName=originTerminal',250,600);" />
	        <dojo:autoComplete formId="NewBookingsForm" textboxId="originTerminal" 
	        action="<%=path%>/actions/getUnlocationCode.jsp?tabName=BOOKING&from=0"/>
        </td>
		<td  width="4%"class="textlabels">POL&nbsp;</td>
		<td><input  name="portOfOrigin" id="portOfOrigin" value="${bookingForm.portOfOrigin}"  size="14" />   
	        <dojo:autoComplete formId="NewBookingsForm" textboxId="portOfOrigin" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=1"/>
        </td>
		<td  class="textlabels">POE<br/> </td>
		<td><input  name="exportToDelivery" id="exportToDelivery" value="${bookingForm.exportToDelivery}"  size="14" />   
	        <dojo:autoComplete formId="NewBookingsForm" textboxId="exportToDelivery" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=0"/>
        </td>
	    <td  width="4%" class="textlabels">POD&nbsp;</td>
	    <td><input  name="destination" id="destination" value="${bookingForm.destination}"  size="14" onchange="getPod()"/>   
	        <dojo:autoComplete formId="NewBookingsForm" textboxId="destination" action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=BOOKING&from=3"/>
        </td>
		<td class="textlabels"  width="7%">Destination</td>
		<td><input  name="portOfDischarge" id="portOfDischarge" Class="textlabelsBoldForTextBox mandatory" value="${bookingForm.portOfDischarge}"  size="14" onchange="getDestination()"/>
		    <span class="hotspot"
							onmouseover="tooltip.show('<strong>Search Orgin</strong>',null,event);" onmouseout="tooltip.hide();"><img
								src="<%=path%>/img/icons/web.gif" border="0"
								onClick="return GB_show('Orgin Search', '<%=path%>/newBookings.do?buttonValue=searchPort&textName=portOfDischarge',250,600);" /> 
		     <dojo:autoComplete formId="NewBookingsForm" textboxId="portOfDischarge" action="<%=path%>/actions/getUnlocationCode.jsp?tabName=BOOKING&from=2"/>
        </td>
    </tr>
    <tr class="textlabels">
		<td>Move Type</td>
        <td><html:select property="moveType"  value="${bookingForm.moveType}" >
         <html:optionsCollection name="typeOfMoveList"/></html:select>
        </td>
  
 		<td>Agent</td>
 		<td colspan="2"><html:text property="agent"  value="${bookingForm.agent}" size="15"/>
 	    <input type="button" value="Look Up" class="buttonStyleNew" onclick="getAgent()" style="width:50px"></td>
 	</tr> 
</table>

 <br style="margin-top:5px"/> 
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" > 
	<tr>
		<td width="25%" valign="top">
		     <table width="100%" border="0" cellpadding="0" cellspacing="0" style="padding-left:10px;">
		 	   <tr class="tableHeadingNew" height="90%">Shipper</tr>
		       <tr>
		           <td class="textlabels" > Name&nbsp;</td>
		           <td class="textlabels">
		 	             <input name="shipperName" id="shipperName" value="${bookingForm.shipperName}" maxlength="15" size="22" onkeydown="getShipperInfo(this.value)" />   
		 	             <dojo:autoComplete formId="NewBookingsForm" textboxId="shipperName" action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=4"/>
					<input type="button" id="toggle" value="Look Up" class="buttonStyleNew" onclick="getShipper(this.value)"  style="width:56px" >
               </td>
		      </tr>
		       <tr>
		 	         <td class="textlabels" width="10%">Code</td>
		 	         <td class="textlabels">
		 	             <input  name="shipper" id="shipper" value="${bookingForm.shipper}" maxlength="15" size="22" onkeydown="getShipperInfo1(this.value)" />   
		 	             <dojo:autoComplete formId="NewBookingsForm" textboxId="shipper" action="<%=path%>/actions/getCustomer.jsp?tabName=BOOKING&from=3"/>
		 	         </td>
		 	   </tr>
		      <tr>
		 	     <td  class="textlabels" valign="top"> Address&nbsp;</td>
		 	     <td><html:textarea property="shipperAddress" cols="11" rows="3" value="${bookingForm.shipperAddress}" ></html:textarea>
                 <input type="button" id="shipperbutton" value="Look Up" class="buttonStyleNew" onclick="getShipperAddress(this.value)"  style="width:46px"/> </td>
		 	   </tr>
		      <tr>          
		 	       <td class="textlabels"> City&nbsp;</td>
		 	       <td class="textlabels"><html:text property="shipperCity"  size="8"    value="${bookingForm.shipperCity}"/>State
		 	       <html:text size="4" property="shipperState" value="${bookingForm.shipperState}"/></td>
		 	  </tr>
		 	  <tr>
                  <td class="textlabels" > Zip&nbsp;</td>
                  <td><html:text property="shipperZip"   size="22"   value="${bookingForm.shipperZip}" onkeypress="getzip(this)" maxlength="10"/></td>
              </tr>
              <tr>
		 	     <td class="textlabels" > Country&nbsp;</td>
		 	    <td><html:text property="shipperCountry"   size="22"     value="${bookingForm.shipperCountry}"/></td>
		 	  </tr>
		
		     <tr>
		         <td class="textlabels"> Phone</td>
			    <td class="textlabels"><html:text property="shipPho" size="22" value="${bookingForm.shipPho}" onkeypress="getIt(this)" maxlength="13"/></td>
 		    </tr>
		    <tr>
		         <td  class="textlabels"> Fax&nbsp;</td>
		 	    <td align="left"><html:text property="shipperFax" size="22" value="${bookingForm.shipperFax}"  onkeypress="getIt(this)" maxlength="13"/></td>
		 	</tr>
		 	<tr>
                <td class="textlabels"> Email&nbsp;</td>
                <td class="textlabels"><html:text property="shipEmai" size="22" value="${bookingForm.shipEmai}" /></td>
            </tr>
            <tr>
                <td class="textlabels">Client Ref</td>
                <td><html:text property="shipperClientReference" size="12" value="${bookingForm.shipperClientReference}" /> </td>
            </tr>
            <tr class="textlabels">
            	<td>Client</td>
            	<td><html:checkbox property="shippercheck" name="bookingForm" onclick="CheckedShip()"></html:checkbox></td>
            </tr>
		</table>
		</td>
		<td width="25%" valign="top"> 
		   <table width="100%" border="0" cellpadding="0" cellspacing="0">
		 	 <tr class="tableHeadingNew" height="90%">Forwarder</tr>
		 	  <tr>
                     <td  class="textlabels" align="right"> Name&nbsp;</td>
           			     <td class="textlabels">
                         <input  name="fowardername" id="fowardername" value="${bookingForm.fowardername}" maxlength="15" size="22" onkeydown="getForwarderInfo(this.value)" />   
	                     <dojo:autoComplete formId="NewBookingsForm" textboxId="fowardername" action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=2"/>
<input type="button" id="toggle" value="Look Up" class="buttonStyleNew" onclick="getForwarder(this.value)" style="width:56px">
    </td>
         	    </tr>
		 	    <tr>
		 	       <td  class="textlabels" width="10%" align="right">Code&nbsp;</td>
		 	        <td class="textlabels">
                         <input  name="forwarder" id="forwarder" value="${bookingForm.forwarder}" maxlength="15" size="22" onkeydown="getForwarderInfo1(this.value)" />   
	                     <dojo:autoComplete formId="NewBookingsForm" textboxId="forwarder" action="<%=path%>/actions/StreamShipForwarder.jsp?tabName=BOOKING&from=0"/>
	                </td>
		          </tr>
	       	   
           	    <tr>
	      <td  class="textlabels" valign="top" align="right"> Address&nbsp;</td>
	          <td> <html:textarea property="forwarderAddress" cols="11" rows="3" value="${bookingForm.forwarderAddress}"></html:textarea>       
            <input type="button" id="fowarderbutton" value="Look Up" class="buttonStyleNew"  onclick="getForwarderAddress(this.value)" style="width:46px" >
           </td>
	            </tr>
		         <tr>
		<td  class="textlabels" align="right"> City&nbsp;</td>
	<td class="textlabels"><html:text property="forwarderCity" size="8" value="${bookingForm.forwarderCity}"/>State
	<html:text property="forwarderState" size="4" value="${bookingForm.forwarderState}"/></td>
		 	    </tr>		 	             
              	<tr>
        <td  class="textlabels" align="right" > Zip&nbsp;</td>
     <td><html:text property="forwarderZip" size="22" value="${bookingForm.forwarderZip}" onkeypress="getzip(this)" maxlength="10"/></td>
              	</tr>
              	<tr>
		 	          <td   class="textlabels" align="right"> Country&nbsp;</td>
		 	          <td><html:text property="forwarderCountry" size="22" value="${bookingForm.forwarderCountry}"/></td>
		 	    </tr>
		 	    <tr>
		 	          <td class="textlabels" align="right"> Phone</td>
 <td class="textlabels"><html:text property="forwarderPhone" size="22" value="${bookingForm.forwarderPhone}" onkeypress="getIt(this)" maxlength="13"/></td>
		 	    </tr>
		 	    <tr>
     <td  class="textlabels" align="right"> Fax &nbsp;</td>
     <td><html:text property="forwarderFax"  size="22" value="${bookingForm.forwarderFax}" onkeypress="getIt(this)" maxlength="13"/></td>
		 	    </tr>
		 	    <tr>
      			 <td class="textlabels" align="right" > Email&nbsp;</td>
      			 <td class="textlabels"><html:text property="forwarderEmail" size="22" value="${bookingForm.forwarderEmail}" /></td>
              	</tr>
              	<tr>
                	<td class="textlabels">Client Ref</td>
                	<td>
                		<html:text property="forwarderClientReference" size="12" value="${bookingForm.forwarderClientReference}" /> 
                	</td>
           		 </tr>
              	<tr class="textlabels">
            <td align="right">Client</td>
      <td><html:checkbox property="forwardercheck" name="bookingForm" onclick="CheckedForwarder()"></html:checkbox></td>
               </tr>
		    </table>
		</td>   
		
      <td width="25%" valign="top">
	  	<table width="100%" border="0" cellpadding="0" cellspacing="0">
	       <tr class="tableHeadingNew" height="90%">Consignee</tr>
	         
	        <tr>
                  <td  class="textlabels" align="right" > Name&nbsp;</td>
                  <td class="textlabels">
             <input  name="consigneename" id="consigneename" value="${bookingForm.consigneename}" maxlength="15" size="22" onkeydown="getConsigneeInfo()" />   
         <dojo:autoComplete formId="NewBookingsForm" textboxId="consigneename" action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=3"/>       
           <input type="button" id="toggle" value="Look Up" class="buttonStyleNew" onclick="getConsignee(this.value)" style="width:56px">
           </td>                
      	    </tr>
      	    <tr>
               <td class="textlabels" width="10%" align="right">Code&nbsp;</td>
               <td class="textlabels">
                      <input  name="consignee" id="consignee" value="${bookingForm.consignee}" maxlength="15" size="22" onkeydown="getConsigneeInfo1()"/>   
                      <dojo:autoComplete formId="NewBookingsForm" textboxId="consignee" action="<%=path%>/actions/getCustomer.jsp?tabName=BOOKING&from=2"/>       
               </td>
            </tr>
      	     <tr>
	             <td   class="textlabels" valign="top" align="right"> Address&nbsp;</td>
	             <td> <html:textarea property="consigneeAddress" cols="11" rows="3" value="${bookingForm.consigneeAddress}"></html:textarea> 
					<input type="button" id="consigneebutton" value="Look Up" class="buttonStyleNew" onclick="getConsigneeAddress(this.value)" style="width:46px">
   				 </td>	             
	         </tr> 
	         <tr>
	            <td  class="textlabels" align="right"> City&nbsp;</td>
	            <td class="textlabels"><html:text property="consigneeCity" size="8" value="${bookingForm.consigneeCity}"/>State
	            <html:text property="consigneeState" size="4" value="${bookingForm.consigneeState}"/></td>
	         </tr> 
	  	    <tr>
                 <td  class="textlabels" align="right"> Zip&nbsp;</td>
                 <td><html:text property="consigneeZip" value="${bookingForm.consigneeZip}" size="22"  onkeypress="getzip(this)" maxlength="10"/></td>
      	    </tr> 
             <tr>
	             <td  class="textlabels" align="right"> Country&nbsp;</td>
	            <td><html:text property="consigneeCountry" size="22" value="${bookingForm.consigneeCountry}"/></td>
		    </tr>
		   <tr>
	<td class="textlabels" align="right"> Phone</td>
    <td class="textlabels"><html:text property="consigneePhone" size="22"  value="${bookingForm.consigneePhone}" onkeypress="getIt(this)" maxlength="13"/></td>
		   </tr>
		   <tr>
	<td  class="textlabels" align="right"> Fax &nbsp;</td>
     <td align="left"><html:text property="consigneeFax" size="22"  value="${bookingForm.consigneeFax}" maxlength="13" onkeypress="getIt(this)"/></td>
            </tr>
         	
         		<tr>
                	<td class="textlabels">Client Ref</td>
                	<td>
                		<html:text property="consigneeClientReference" size="12" value="${bookingForm.consigneeClientReference}" /> 
                	</td>
           		 </tr>
           		 <tr>
        			<td class="textlabels" align="right"> Email&nbsp;</td>
      				<td class="textlabels"><html:text property="consigneeEmail" size="22"  value="${bookingForm.consigneeEmail}" /></td>
                </tr>
            <tr class="textlabels">
     <td align="right">Client</td>
       <td><html:checkbox property="consigneecheck" name="bookingForm" onclick="CheckedConsignee()"></html:checkbox></td>
            </tr>
         </table>
		</td>
		
		<td width="25%" valign="top">
		   <table width="100%" border="0" cellpadding="3" cellspacing="0" style="padding-left:10px;">
		      <tr class="tableHeadingNew" height="90%" align="right">Trucker</tr>
              <tr class="textlabels">   
		<td align="right" align="right">Name</td>
	 <td class="textlabels">
        <input  name="truckerName" id="truckerName" value="${bookingForm.truckerName}" maxlength="15" size="22" onkeydown="getTruckerInfo(this.value)" />   
       <dojo:autoComplete formId="NewBookingsForm" textboxId="truckerName" action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=6"/>       
      <input type="button" id="toggle" value="Look Up" class="buttonStyleNew" onclick="getTrukerName(this.value)" style="width:56px"/> </td>
	</tr>
		      <tr class="textlabels">
		         <td align="right">Code</td>
				 <td class="textlabels">
<input  name="truckerCode" id="truckerCode" value="${bookingForm.truckerCode}" maxlength="15" size="22" onkeydown="getTruckerInfo1(this.value)" />   
	<dojo:autoComplete formId="NewBookingsForm" textboxId="truckerCode" action="<%=path%>/actions/getCustomer.jsp?tabName=BOOKING&from=11"/>       
		 	    </td>              
              </tr>
		      <tr class="textlabels">    
     <td valign="top" align="right">Address</td>
    <td><html:textarea property="addressoftrucker" value="${bookingForm.addressoftrucker}" cols="11" rows="3"></html:textarea>
	<input type="button"  value="Look Up" id="truckerContactButton" class="buttonStyleNew" onclick="getTruckerAddress(this.value)" style="width:46px"/></td>
		      </tr>  
		     <tr class="textlabels">
		         <td align="right">Phone</td>
      <td><html:text property="truckerPhone" size="22" value="${bookingForm.truckerPhone}" onkeypress="getIt(this)" maxlength="13"></html:text><br /></td>
       		</tr>
       		<tr class="textlabels">	 
	<td align="right">Email</td>
	<td><html:text property="truckerEmail" size="22" value="${bookingForm.truckerEmail}"></html:text><br /></td>
			</tr>
			<tr>
                	<td class="textlabels">Client Ref</td>
                	<td>
                		<html:text property="truckerClientReference" size="12" value="${bookingForm.truckerClientReference}" /> 
                	</td>
           		 </tr>
		  </table>
		</td>      
	</tr>	
 </table>
 
 <br style="margin-top:5px"/> 
 
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="">
<tr>
	<td width="25%" valign="top" align="left" >
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
		 	   <tr class="tableHeadingNew">Equipment Pickup</tr>
	               <tr>      
	        <td class="textlabels" style="padding-left:10px;"> Date </td>
		 	  <td class="textlabels">
		 	  <fmt:formatDate pattern="MM/dd/yyyy" var="empPickupDate" value="${bookingValues.emptyPickUpDate}"/>
		 	  <html:text property="empPickupDate" size="16" value="${empPickupDate}" styleId="txtcal9"/>
		<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal9" onmousedown="insertDateFromCalendar(this.id,0)" /> </td>
				  </tr>
				  <tr>	 
						 <td class="textlabels" style="padding-left:10px;">Earliest Date</td>
<td>
 <fmt:formatDate pattern="MM/dd/yyyy" var="earlierPickUpDate" value="${bookingValues.earliestPickUpDate}"/>
         <html:text property="earlierPickUpDate" size="16" value="${earlierPickUpDate}" styleId="txtcal99"/>
         <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal10" width="16" height="16" id="cal99" onmousedown="insertDateFromCalendar(this.id,0)" /></td>
	              </tr>
	              <tr>
	     <td  align="left" class="textlabels" style="padding-left:10px;">Location</td>
		 <td>
<%--		 <html:text property="exportPositioning"   size="22"  value="${bookingForm.exportPositioning}"></html:text>--%>
		     <input name="exportPositioning" id="exportPositioning" value="${bookingForm.exportPositioning}"  size="16" onkeydown="getWareHouseAdd(this.value)" />
             <dojo:autoComplete formId="NewBookingsForm" textboxId="exportPositioning" action="<%=path%>/actions/getWareHouseName.jsp?tabName=BOOKING&from=0"/> 
		 </td>
	  </tr>       
	  <tr>
	      <td valign="top" class="textlabels" style="padding-left:10px;"> Address</td>
	      <td><html:textarea property="emptypickupaddress" cols="15" rows="2" value="${bookingForm.emptypickupaddress}" onkeydown="setAddress(this.value)"></html:textarea></td>
	  </tr>	
	 </table>
    </td>
	    
	<td width="25%" valign="top" align="left">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
		 	   <tr class="tableHeadingNew"  style="padding-left:10px;">Load Address</tr>       		
	              <tr>	
	          <td><span class="textlabels"> Date &nbsp;</span></td>
		 <td>
 <fmt:formatDate pattern="MM/dd/yyyy" var="postioningDate" value="${bookingValues.positioningDate}"/>
		 <html:text  property="postioningDate" value="${postioningDate}" size="16" styleId="txtcal53" />
	 <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal53" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
	              </tr>       
	              <tr>
	              	   <td class="textlabels">Location</td>
	              	   <td><html:text property="positionlocation" size="16" value="${bookingForm.positionlocation}"></html:text></td>
	              </tr>
	              <tr>
	              		<td class="textlabels">Contact</td>
	              		<td><html:text property="loadcontact" size="16" value="${bookingForm.loadcontact}" ></html:text></td>
	              </tr>
	              <tr>
	              		<td class="textlabels">Phone</td>
	              		<td><html:text property="loadphone" size="16" value="${bookingForm.loadphone}" onkeypress="getIt(this)" maxlength="13"></html:text></td>
	              </tr>				
	              <tr>
	      <td valign="top" ><span class="textlabels" valign="top" >Address&nbsp;</span></td>
      <td><html:textarea property="addressofExpPosition" cols="15" rows="2" value="${bookingForm.addressofExpPosition}" /><br /></td>    
		 	      </tr> 
		 	      <tr>
		 	      		<td>&nbsp;&nbsp;</td>
		 	      </tr>                    
	     </table>
	</td>
	<td width="25%" valign="top" align="left">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		 	<tr class="tableHeadingNew"  style="padding-left:10px;">Equipment Return</tr>   
			<tr>	
			<td class="textlabels">Date</td>
     		 <td>
       			<fmt:formatDate pattern="MM/dd/yyyy" var="loaddate" value="${bookingValues.loadDate}"/>
      			<html:text property="loaddate" size="16"  value="${bookingForm.loaddate}"  styleId="txtcal44"/>
    			<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal44" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
		    </tr>
			<tr>	
 				<td class="textlabels"> Location</td>
				<td><input name="loadLocation" id="loadLocation" value="${bookingForm.loadLocation}"  size="16" onkeydown="getWareHouseAdd1(this.value)" />
          		<dojo:autoComplete formId="NewBookingsForm" textboxId="loadLocation" action="<%=path%>/actions/getWareHouseName.jsp?tabName=BOOKING&from=1"/> 
				</td>
			</tr>
			<tr>	
				<td  valign="top"><span class="textlabels" >Address </span></td>
				<td><html:textarea property="addressofDelivery" cols="15" value="${bookingForm.addressofDelivery}"/><br /></td>
		 	</tr>
		</table>
	</td> 
	<td width="25%" valign="top" align="left">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		 	<tr class="tableHeadingNew"  style="padding-left:10px;">Equipment Control</tr>   
			<tr>	
				<td class="textlabels">Date out of Yard</td>
      			<td>
      				<fmt:formatDate pattern="MM/dd/yyyy" var="dateOutYard" value="${bookingValues.dateoutYard}"/>
      				<html:text property="dateOutYard" size="16"  value="${bookingForm.dateOutYard}"  styleId="txtcal55"/>
    				<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal55" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
		   </tr>  
		   <tr>	
				<td class="textlabels">Date taken back into Yard</td>
      			<td>
      				 <fmt:formatDate pattern="MM/dd/yyyy" var="dateInYard" value="${bookingValues.dateInYard}"/>
      				<html:text property="dateInYard" size="16"  value="${bookingForm.dateInYard}"  styleId="txtcal66"/>
    				<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal66" onmousedown="insertDateFromCalendar(this.id,0);" /></td>
		  </tr>
	</table>
	</td>	                      
 </tr>
</table>
<br style="margin-top:5px"/> 
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
         <td width="40%" valign="top" >
		 	<table width="100%" border="0" cellpadding="4" cellspacing="0" class="tableBorderNew" style="padding-left:10px;">
		 	<tr class="tableHeadingNew" height="90%">Special Provisions</tr>
		 
	      <tr class="textlabels">
	  <td  width="20%" class="textlabels">Special Equipment</td>
	<td>  <label> <html:radio property="specialequipment" value="Y" styleId="y1" name="bookingForm"/>Yes</label>
	 <label><html:radio property="specialequipment" value="N" styleId="n1"  name="bookingForm"/>No</label>
	</td>
	      </tr>
	      <tr  class="textlabels">
	          <td  width="20%" align="left" class="textlabels">SOC </td>
	          <td>  <label> <html:radio property="soc" value="Y"  name="bookingForm"/>Yes</label>
		            <label><html:radio property="soc" value="N"   name="bookingForm"/>No</label>
	          </td>
	      </tr>
	      <tr class="textlabels">
	        <td >Hazmat</td>
		     <td> <label><html:radio property="hazmat" value="Y"  onclick="getHazmat()" name="bookingForm"/> Yes</label>
		        <label><html:radio property="hazmat" value="N"  name="bookingForm"/>No</label>
	        </td>
	      </tr>
	      <tr class="textlabels">
	        <td class="textlabels">Out of Gage</td>
		    <td><label> <html:radio property="outofgate" value="Y" styleId="y3" name="bookingForm"/>Yes </label>
		        <label><html:radio property="outofgate" value="N" styleId="n3" name="bookingForm"/>No</label>
	        </td>
	     </tr>
	  	<tr>
	  		<td   class="textlabels" style="padding-top:4px;">Local Drayage : </td>
	  		<td>
	  			<table  border="0">
			      <tr class="style2">
	<td><label> <html:radio property="localdryage" value="Y" styleId="y6" onclick="getlocaldryage()" name="bookingForm"/>Yes</label>
		<label><html:radio property="localdryage" value="N" styleId="n6" onclick="getlocaldryage1()" name="bookingForm"/>No</label>
			        </td>
			        <td class="textlabels" style="padding-top:4px;">Amount</td>
			        <td ><html:text property="amount" value="${bookingForm.amount}" size="5"/></td>
			      </tr>
		     </table> 
	  		
	  		</td>
	  	</tr>	
	  	<tr>
	  		<td   class="textlabels" style="padding-top:4px;">Intermodal:</td>
	  		<td>
			<table>
			 <tr class="style2">
		        <td ><label>
		          <html:radio property="intermodel" value="Y" styleId="y7" onclick="getintermodel()" name="bookingForm"/>Yes
		          <html:radio property="intermodel" value="N" styleId="n7" onclick="getintermodel1()" name="bookingForm"/>No</label>
				</td>
		          <td class="textlabels" style="padding-top:4px;">Amount</td>
		          <td><html:text property="amount1"  value="${bookingForm.amount1}" size="5"/></td>
		        </tr>  
      		</table>
	  		
	  		</td>
	  	</tr>
	  	<tr>
	  		<td class="textlabels" style="padding-top:4px;">Insurance:</td>
	  		<td style="padding-bottom:8px">
			<table>
			 <tr class="style2">
		        <td class="textlabels">  
		   <html:radio property="insurance" value="Y" styleId="y8" onclick="getinsurance()" name="bookingForm">Yes</html:radio>
		<html:radio property="insurance"  value="N" styleId="n8" onclick="getinsurance1()" name="bookingForm">No</html:radio>
		        </td>	
		        <td class="textlabels" style="padding-top:4px;">Cost of Goods </td>
		        <td><html:text property="costofgoods1" value="${bookingForm.costofgoods1}" size="5"/></td>
		        <td class="textlabels" style="padding-top:4px;">Insurance Amt </td>
		        <td><html:text property="insurancamt" value="${bookingForm.insurancamt}" size="5" readonly="true"/> </td>
		     </tr>  
      		</table>
	  		</td>
	  	</tr>	  
	  </table>
  	</td>	
  	   <td width="1%" valign="top">&nbsp;</td>
	     <td width="40%" valign="top">
	        <table>
			 <tr>      
                <td class="textlabels">Goods Description </td>
      		</tr>
      	  <tr>
                <td><html:textarea property="goodsDescription" value="${bookingForm.goodsDescription}" cols="50"  rows="5"/></td>
          </tr>
          <tr>
        		<td class="textlabels">Remarks</td>
         </tr>
         <tr>
        		<td><html:textarea property="remarks" value="${bookingForm.remarks}" cols="50"  rows="5"/></td>
         </tr>
	   </table>
	  </td>
  	</tr>
 </table> 
  <br style="margin-top:5px"/>       
<table width="100%" class="tableBorderNew">
   <tr class="tableHeadingNew" height="90%">Rates</tr>
   <tr><td align="left" ><div id="divtablesty1" style="border:thin;overflow:scroll;width:98%;height:80%">
       <table width="80%" border="0" cellpadding="0" cellspacing="0">
       <%
 			List fcllist=(List)request.getAttribute("fclRates");
 			int l=0;
 			String unittemp="";
         %>   
         <display:table name="<%=fcllist%>"  class="displaytagstyle" id="chargesTable" sort="list" style="width:100%" pagesize="<%=pageSize%>"> 
     <display:setProperty name="paging.banner.some_items_found">
      <span class="pagebanner">
       			<font color="blue">{0}</font> 
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
      <display:setProperty name="paging.banner.item_name" value="FCL BUY Rates"/>
      <display:setProperty name="paging.banner.items_name" value="FCL BUY Rates"/>
      <%
					String amt="";
   					String markUp1="";
 					String unit="";
 					String chargecode="";
  					String costType="";
  					String num="";
  					String currency="";
  					String chargecodedesc="";
  					String sellRate="";
  String buyRate="";
  String profit="";
  String newRate="";
  String effectiveDate="";
  String accountNo="";
  String accountName="";
    	if((fcllist!=null && fcllist.size()>0)) {
    		BookingfclUnits book=new BookingfclUnits();
    		book.getMinimum();
    		book=(BookingfclUnits)fcllist.get(l);
    		if(book.getUnitType()!=null) {
            unit=book.getUnitType().getCodedesc().toString();
            }
    		chargecode=book.getChgCode();
    		costType=book.getCostType();
    		amt=numformat1.format(book.getAmount());
    		if(book.getMarkUp()!=null) {
    		markUp1=numformat1.format(book.getMarkUp());
    		}
    		currency=book.getCurrency();
    		num=book.getNumbers();
    		chargecodedesc=book.getChargeCodeDesc();
    		if(book.getSellRate()!=null) {
    		sellRate=numformat1.format(book.getSellRate());
    		}
    		if(book.getAccountNo()!=null) {
    		accountNo=book.getAccountNo();
    		}
    		if(book.getAccountName()!=null) {
    		accountName=book.getAccountName();
    		}
    		if(book.getFutureRate()!=null) {
    		newRate=numformat1.format(book.getFutureRate());
    		} else {
    		newRate="";
    		}
    		if(book.getEfectiveDate()!=null) {
    		effectiveDate=book.getEfectiveDate().toString();
    		} else{
    		effectiveDate="";
    		}
    		}
     %>
      <%
      if(!unittemp.equals(unit)){
    %>
     <display:column style="visibility:hidden" title=""><%=""%></display:column>
      <display:column title="UnitSelect" style="visibility:visible"><html:text property="unitType" value="<%=unit%>"styleClass="textdatestyle" disabled="true">
      </html:text> </display:column>
      <display:column title="Number" style="visibility:visible"><html:text property="numbers" value="<%=num%>" size="3" maxlength="3"  onchange="getNumbersChanged(this)"/></display:column>
      <display:column title="ChargeCode"><html:text property="chargeCddesc" value="<%=chargecodedesc%>" readonly ="true" size="3"/></display:column>
      <display:column title="ChargeCodeDesc"><html:text property="chargeCodes" value="<%=chargecode%>" readonly ="true" size="6"/></display:column>
      <display:column title="CostType"><html:text property="costType"  maxlength="30" size="9" value="<%=costType%>" readonly ="true" /></display:column>
       <display:column title="Currency"><html:text property="currency"  maxlength="30" size="3" value="<%=currency%>" readonly ="true" /></display:column>
      <display:column title="Amounts">   <html:text property="chargeAmount" value="<%=amt%>" size="6"/> </display:column>
      <display:column title="MarkUp">   <html:text property="chargeMarkUp" maxlength="15" size="6" value="<%=markUp1%>"/> </display:column>
      <display:column title="Sell Rate">   <html:text property="sellRate" maxlength="15" size="6" value="<%=sellRate%>"/> </display:column>
      <display:column title="New Rate">   <html:text property="newRate" maxlength="15" size="6" value="<%=newRate%>" readonly="true"  /> </display:column>
      <display:column title="Effective Date">   <html:text property="effectiveDate" maxlength="15" size="7" value="<%=effectiveDate%>" readonly="true"/> </display:column>
       <display:column title="Account No">
      <input type="text" name="accountno<%=l%>" id="accountno<%=l%>" onkeydown="getAccountName(this.value)" value="<%=accountNo%>"/>
      <dojo:autoComplete formId="NewBookingsForm" textboxId="accountno<%=l%>" action="<%=path%>/actions/getCustomer.jsp?tabName=BOOKING&from=0&index=<%=l%>"/>
       </display:column>
     <display:column title="Account Name">
      <input type="text" name="accountname<%=l%>" id="accountname<%=l%>" onkeydown="getAccountName(this.value)" value="<%=accountName%>"/>
      <dojo:autoComplete formId="NewBookingsForm"  textboxId="accountname<%=l%>" action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=0&index=<%=l%>"/>
       </display:column>
       <display:column style="visibility:hidden" title="" property="manualCharges"></display:column>
       <%
       }else{  %>
     <display:column style="visibility:hidden" title=""><%=""%></display:column>
     <display:column title="UnitSelect" style="visibility:hidden"><html:text property="unitType" value="<%=unit%>"styleClass="textdatestyle" disabled="true" style="visibility:hidden">
      </html:text> </display:column>
     <display:column title="Number" style="visibility:hidden"><html:text property="numbers" value="<%=num %>" size="3" maxlength="3" style="visibility:hidden" onchange="getNumbersChanged(this)"/></display:column>
    <display:column title="ChargeCode"><html:text property="chargeCddesc" value="<%=chargecodedesc%>" readonly ="true" size="3"/></display:column>
     <display:column title="ChargeCodeDesc"><html:text property="chargeCodes" value="<%=chargecode%>" readonly ="true" size="6"/></display:column>
     <display:column title="CostType"><html:text property="costType"  maxlength="30" size="9" value="<%=costType%>" readonly ="true"/></display:column>
     <display:column title="Currency"><html:text property="currency"  maxlength="30" size="3" value="<%=currency%>" readonly ="true" /></display:column>
     <display:column title="Amounts">   <html:text property="chargeAmount" value="<%=amt%>" size="6"/> </display:column>
     <display:column title="MarkUp">   <html:text property="chargeMarkUp" maxlength="15"  value="<%=markUp1%>" size="6"/> </display:column>
       <display:column title="Sell Rate">   <html:text property="sellRate" maxlength="15" size="6" value="<%=sellRate%>"/> </display:column>
      <display:column title="New Rate">   <html:text property="newRate" maxlength="15" size="6" value="<%=newRate%>" readonly="true"/> </display:column>
      <display:column title="Effective Date">   <html:text property="effectiveDate" maxlength="15" size="7" value="<%=effectiveDate%>" readonly="true"/> </display:column>
    <display:column title="Account Name">
      <input type="text" name="accountname<%=l%>" id="accountname<%=l%>" onkeydown="getAccountName(this.value)" value="<%=accountName%>"/>
      <dojo:autoComplete formId="NewBookingsForm"
		   textboxId="accountname<%=l%>"
		   action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=0&index=<%=l%>"/>
       </display:column>
    <display:column title="Account No">

      <input type="text" name="accountno<%=l%>" id="accountno<%=l%>" onkeydown="getAccountName(this.value)" value="<%=accountNo%>"/>
      <dojo:autoComplete formId="NewBookingsForm"
		   textboxId="accountno<%=l%>"
		   action="<%=path%>/actions/getCustomer.jsp?tabName=BOOKING&from=0&index=<%=l%>"/>
       </display:column>
    <display:column style="visibility:hidden" title="" property="manualCharges"></display:column>
     <%}l++;unittemp=unit;
    %>
    </display:table>
    </table></div>
    </td></tr>
    </table>
 <table width="100%">
     <tr>
          <td align="left" ><div id="divtablesty1" style="border:thin;width:100%;" 
          pagesize="<%=pageSize%>">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <%
 List perkglbsList=(List)request.getAttribute("perKgLbsList");
  //String amt[]=new String[50];
 if((perkglbsList!=null && perkglbsList.size()>0)) {
 int k=0;
 BookingfclUnits cb=new BookingfclUnits();
 String activeAmt="";
 String minimum="";
 String chargeCd="";
 String currecny1="";
 String chargecode1="";
 String costType1="";
         %>   
             <display:table name="<%=perkglbsList%>"  class="displaytagstyle" id="lclcoloadratestable" 
             sort="list" style="width:100%"> 
      <%
        cb=(BookingfclUnits)perkglbsList.get(k);
       chargeCd=cb.getChargeCodeDesc();
       chargecode1=cb.getChgCode();
       costType1=cb.getCostType();
   		if(cb.getAmount()!=null){
   		activeAmt=numformat1.format(cb.getAmount());
   		}
     if(cb.getMinimum()!=null){
     minimum=numformat1.format(cb.getMinimum());
     }
     if(cb.getCurrency()!=null){
     currecny1=cb.getCurrency();
     }
      %>
     <display:setProperty name="paging.banner.some_items_found">
       <span class="pagebanner">
        <font color="blue">{0}</font> 
        
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
      <display:column title="ChargeCode"><html:text property="perChargeCode" value="<%=chargeCd%>" readonly="true" size="3"/></display:column>
      <display:column title="ChargeCodeDesc"><html:text property="perChargeCodeDesc" value="<%=chargecode1%>" readonly="true" size="6"/></display:column>
      <display:column title="CostType"><html:text property="perCostType"  maxlength="30" size="9" value="<%=costType1%>" readonly="true"/></display:column>
      <display:column title="Rate">   <html:text property="perActiveAmt" maxlength="15" size="6" value="<%=activeAmt%>" readonly="true"/> </display:column>
      <display:column title="Minimum">   <html:text property="perMinimum" maxlength="15" size="6" value="<%=minimum%>" readonly="true"/> </display:column>
     <display:column title="Currency"><html:text property="percurrecny"  maxlength="30" size="3" value="<%=currecny1%>" readonly="true"/></display:column>
  
    <%k++; %>
     </display:table>
  <%} %>
  </table> 
      <table width="100%">
     <tr>
          <td align="left" ><div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:80%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <%
 List otherChargesList=(List)request.getAttribute("otherChargesList");

  //String amt[]=new String[50];
  String chargeCd="";
  String retail="";
  String currecny="";
  String otherNewRate="";
  String otherEffectiveDate="";
  String othermarkUp="";
  String otherSellRate="";
  String otherBuyRate="";
  String otherProfit="";
  String otherAccountNo="";
  String otherAccountName="";
  String chargecode2="";
  String costType2="";
 if((otherChargesList!=null && otherChargesList.size()>0)){
 int i=0;
         %>   
             <display:table name="<%=otherChargesList%>"  class="displaytagstyle" id="otherChargestable" 
             sort="list" style="width:100%" pagesize="<%=pageSize%>"> 
     <display:setProperty name="paging.banner.some_items_found">
       <span class="pagebanner">
        <font color="blue">{0}</font> 
      
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
    <display:setProperty name="paging.banner.item_name" value="FCL BUY Rates"/>
    <display:setProperty name="paging.banner.items_name" value="FCL BUY Rates"/>
    <%
if((otherChargesList!=null && otherChargesList.size()>0)){
BookingfclUnits b=new BookingfclUnits();
b=(BookingfclUnits)otherChargesList.get(i);
chargeCd=b.getChargeCodeDesc();
chargecode2=b.getChgCode();
costType2=b.getCostType();
retail=numformat1.format(b.getAmount());
currecny=b.getCurrency();
if(b.getMarkUp()!=null){
othermarkUp=numformat1.format(b.getMarkUp());
}
if(b.getSellRate()!=null){
otherSellRate=numformat1.format(b.getSellRate());
}
if(b.getAccountNo()!=null){
otherAccountNo=b.getAccountNo();
}else{
otherAccountNo="";
}
if(b.getAccountName()!=null){
otherAccountName=b.getAccountName();
}else{
otherAccountName="";
}
if(b.getFutureRate()!=null){
otherNewRate=numformat1.format(b.getFutureRate());
}else{
otherNewRate="";
}
if(b.getEfectiveDate()!=null){
otherEffectiveDate=b.getEfectiveDate().toString();
}else{
otherEffectiveDate="";
}
}
     %>
      <display:column style="visibility:hidden" title=""><%=""%></display:column>
    <display:column title="ChargeCode"><html:text property="otherchargeCddesc" value="<%=chargeCd%>" readonly="true" size="3"/></display:column>
      <display:column title="ChargeCodeDesc"><html:text property="chargeCd" value="<%=chargecode2%>" readonly="true" size="6"/></display:column>
      <display:column title="CostType"><html:text property="costType1"  maxlength="30" size="9" value="<%=costType2%>" readonly="true"/></display:column>
       <display:column title="Currency"><html:text property="othercurrecny"  maxlength="30" size="3" value="<%=currecny%>" readonly="true"/></display:column>
      <display:column title="Retail">   <html:text property="retail" maxlength="15" size="6" value="<%=retail%>" /> </display:column>
      <display:column title="Mark Up"><html:text property="othermarkUp"  maxlength="30" size="6" value="<%=othermarkUp%>" /></display:column>
       <display:column title="Sell Rate"><html:text property="otherSellRate"  maxlength="30" size="6" value="<%=otherSellRate%>" /></display:column>
       <display:column title="New Rate" ><html:text property="otherNewRate"  maxlength="30" size="6" value="<%=otherNewRate%>" readonly="true"/></display:column>
        <display:column title="Effective Date"><html:text property="otherEffectiveDate"  maxlength="30" size="7" value="<%=otherEffectiveDate%>" readonly="true"/></display:column>
      <display:column title="Account Name">

      <input type="text" name="otheraccountname<%=i%>" id="otheraccountname<%=i%>" onkeydown="getAccountName(this.value)" value="<%=otherAccountName%>"/>
      <dojo:autoComplete formId="NewBookingsForm" textboxId="otheraccountname<%=i%>" action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=5&index=<%=i%>" />
       </display:column>
      <display:column title="Account No">

      <input type="text" name="otheraccountno<%=i%>" id="otheraccountno<%=i%>" value="<%=otherAccountNo%>" onkeydown="getAccountName(this.value)"/>
      <dojo:autoComplete formId="NewBookingsForm" textboxId="otheraccountno<%=i%>" action="<%=path%>/actions/getCustomer.jsp?tabName=BOOKING&from=5&index=<%=i%>"/>
       </display:column>
     
    
    <display:column style="visibility:hidden" title="" property="manualCharges"></display:column>
    <%i++; %>
    
  
     </display:table>
   
     <%} %>
  <table>
  <tr>
    <td height="102"><table width="752" border="0" cellpadding="0" cellspacing="0">
  <c:if test="${bookingForm.totalCharges!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(USD)</td>
        		<td><c:out value="${bookingForm.totalCharges}"/></td>
        		<td align="center" valign="center"><input type="button" value="Recalc" onclick="recalcfunction()" class="buttonStyleNew"/></td>
       			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.baht!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(BAHT)</td>
        		<td><c:out value="${bookingForm.baht}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.bdt!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(BDT)</td>
        		<td><c:out value="${bookingForm.bdt}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
        <c:if test="${bookingForm.cyp!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(CYP)</td>
        		<td><c:out value="${bookingForm.cyp}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
        </c:if>
          <c:if test="${bookingForm.eur!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(EUR)</td>
        		<td><c:out value="${bookingForm.eur}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
     </c:if>
          <c:if test="${bookingForm.hkd!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(HKD)</td>
        		<td><c:out value="${bookingForm.hkd}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
      </c:if>
       <c:if test="${bookingForm.lkr!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(LKR)</td>
        		<td><c:out value="${bookingForm.lkr}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.nt!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(NT)</td>
        		<td><c:out value="${bookingForm.nt}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.prs!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(PRS)</td>
        		<td><c:out value="${bookingForm.prs}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
        </c:if>
        <c:if test="${bookingForm.rmb!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(RMB)</td>
        		<td><c:out value="${bookingForm.rmb}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
      <c:if test="${bookingForm.won!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(WON)</td>
        		<td><c:out value="${bookingForm.won}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
        </c:if>
      <c:if test="${bookingForm.yen!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(YEN)</td>
        		<td><c:out value="${bookingForm.yen}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.myr!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(MYR)</td>
        		<td><c:out value="${bookingForm.myr}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.nht!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(NHT)</td>
        		<td><c:out value="${bookingForm.nht}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.pkr!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(PKR)</td>
        		<td><c:out value="${bookingForm.pkr}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.rm!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(RM)</td>
        		<td><c:out value="${bookingForm.rm}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.spo!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(SPO)</td>
        		<td><c:out value="${bookingForm.spo}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.vnd!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(VND)</td>
        		<td><c:out value="${bookingForm.vnd}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
       <c:if test="${bookingForm.inr!='0.00'}">
        <tr>
        		<td width="11">&nbsp;</td>
        		<td width="320" class="textlabels">Total Charges(INR)</td>
        		<td><c:out value="${bookingForm.inr}"/></td>
       			<td width="112" class="textlabels">&nbsp;</td>
    			<td width="160" class="textlabels"></td>
        		<td width="149">&nbsp;</td>
        </tr>
       </c:if>
</table>
</td></tr></table></table></div></td></tr></table>
  
</table></div></td></tr></table>
<%--<html:hidden property="shipperHidden" value="<%=originId %>"/>--%>
<%--<html:hidden property="forwardedHidden" value="<%=portid %>"/>--%>
<%--<html:hidden property="consigneeHidden" value="<%=carrierid %>"/>--%>
<html:hidden property="buttonValue" styleId="buttonValue"/>
<html:hidden property="numbIdx"/>
<html:hidden property="numbers1"/>
<html:hidden property="unitSelect"/>
<html:hidden property="number"/>
<html:hidden property="chargeCode"/>
<html:hidden property="chargeCodeDesc"/>
<html:hidden property="costSelect"/>
<html:hidden property="currency1"/>
<html:hidden property="chargeAmt"/>
<html:hidden property="minimumAmt"/>
<html:hidden property="bookingId" value="${bookingForm.bookingId}"/>
<html:hidden property="fieNo" value="${bookingForm.fieNo}"/>
 <c:choose>
  		 <c:when  test="${bookingValues.bookingDate!=null}">
<html:hidden property="bookingDate" value="${bookingValues.bookingDate}"/>
</c:when>
<c:otherwise>
<html:hidden property="bookingDate" value="<%=bookingDate%>"/>
  		 </c:otherwise>
  		 </c:choose>
<c:choose>
  		 <c:when  test="${bookingValues.username!=null}">
		  <html:hidden property="userName" value="${bookingValues.username}"/>
  		 </c:when>
  		 <c:otherwise>
		<html:hidden property="userName" value="<%=bookingBy%>"/>
				 </c:otherwise>
  		 </c:choose>
<script>load()</script>
</html:form>
</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
