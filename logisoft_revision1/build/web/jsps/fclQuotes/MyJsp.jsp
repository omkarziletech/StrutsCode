<%@ page language="java"  import=" com.gp.cong.logisoft.bc.notes.NotesConstants,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.beans.*,com.gp.cvst.logisoft.beans.*,com.gp.cong.logisoft.hibernate.dao.*,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.domain.*"%>
<%@ page import="java.util.*,java.text.NumberFormat,java.text.SimpleDateFormat,java.text.*"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../includes/jspVariables.jsp" %>
 
<html>
<head>
<%
String path = request.getContextPath();
DBUtil dbUtil = new DBUtil();
String quoteId="";
String bookingId="";
String shipCheck="";
String forwCheck="";
String consCheck="";
NumberFormat numformat = new DecimalFormat("##,###,##0.00");
GenericCodeDAO gdDAO = new GenericCodeDAO();
request.setAttribute("RATESLIST",dbUtil.getUnitListForFCLTest(new Integer(38),"yes","Select Unit code" ));
request.setAttribute("NTRLIST",gdDAO.getAllUnitCodeForFCLTestforList(new Integer(38)));
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
DateFormat dateformat=new SimpleDateFormat("MM/dd/yyyy HH:mm");//to include date & time
DateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy HH:mm a");
BookingFcl bookinFCL = new BookingFcl();
String blFlag="";
if(request.getAttribute("bookingValues")!=null){
	bookinFCL = (BookingFcl)request.getAttribute("bookingValues");
	if(bookinFCL.getQuoteNo()!=null){
		quoteId=bookinFCL.getQuoteNo();
	}
	if(bookinFCL.getBookingId()!=null){
		bookingId=bookinFCL.getBookingId().toString();
	}
	if(bookinFCL.getBlFlag()!=null){
		blFlag=bookinFCL.getBlFlag();
	}
}
TransactionBean transactionBean=new TransactionBean();
if(request.getAttribute("transactionBean")!=null){
transactionBean=(TransactionBean)request.getAttribute("transactionBean");
	shipCheck = transactionBean.getShippercheck();
	forwCheck = transactionBean.getForwardercheck();
	consCheck=transactionBean.getConsigneecheck();
}
request.setAttribute("transactionBean",transactionBean);

String msg="";
if (request.getAttribute("message") != null) {
 msg = (String) request.getAttribute("message");
}
String view="";
if(session.getAttribute("view")!=null){
view = (String)session.getAttribute("view");
}
String bookingBy="";
User user1=null;
if(session.getAttribute("loginuser")!=null){
	  user1=(User)session.getAttribute("loginuser");
	  bookingBy = user1.getLoginName();

}
String bookingDate= dateFormat.format(new Date());
request.setAttribute("optionList",dbUtil.getVendortypeIncudingThirdParty());
request.setAttribute("typeOfMoveList", dbUtil.getGenericFCLforTypeOfMove(new Integer(48),"yes","yes"));
request.setAttribute("specialEquipmentList",dbUtil.getUnitListForFCLTest1(new Integer(41),"yes","Select Special Equipments" ));
%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%@include file="../includes/baseResources.jsp" %>
<script language="javascript" src="<%=path%>/js/editBooking.js"></script>
<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
<script language="javascript" src="<%=path%>/js/common.js"></script>
		<script type="text/javascript">
		    dojo.hostenv.setModulePrefix('utils', 'utils');
		    dojo.widget.manager.registerWidgetPackage('utils');
		    dojo.require("utils.AutoComplete");
		    dojo.require("dojo.io.*");
			dojo.require("dojo.event.*");
			dojo.require("dojo.html.*");
			start = function(){
				loadFunction();
			}
		    dojo.addOnLoad(start);
		</script>
<script language="javascript" type="text/javascript">
function call(){
		  var disabledImageZone ;
		  var cvr = document.getElementById("cover");
		  cvr.style.display = "block";
		  disabledImageZone = document.createElement('div');
	      disabledImageZone.style.position = "absolute";
	      disabledImageZone.style.zIndex = "1000";
	      disabledImageZone.style.left = "50%";
	      disabledImageZone.style.top = "100%";
	      disabledImageZone.style.right = "50%";
	      disabledImageZone.style.bottom = "60%";
	      var imageZone = document.createElement('img');
 		  imageZone.setAttribute('src',"/logisoft/img/icons/ajax-loader.gif");
	      imageZone.style.position = "absolute";
	      imageZone.style.width="100";
	      imageZone.style.height="100";
	      disabledImageZone.appendChild(imageZone);
	      document.body.appendChild(disabledImageZone);
	      disabledImageZone.style.visibility = 'visible';
	      
}
function validateDate(){
	if(document.EditBookingsForm.estimatedDate.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.estimatedDate.value;
	 	params['ETA'] = document.EditBookingsForm.estimatedAtten.value;
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

function result(type, data, evt)
{
	if(data){
       if(data.result == 'greater'){
       }else if(data.result == 'lesser' || data.result == 'equal'){
               	 alert("ETA should be greater than ETD");
                 document.EditBookingsForm.estimatedAtten.value="";
                 document.EditBookingsForm.estimatedAtten.select();
       }
	}
}

function validateDate1(){
	if(document.EditBookingsForm.estimatedAtten.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidationReverse";
	 	params['ETD'] = document.EditBookingsForm.estimatedDate.value;
	 	params['ETA'] = document.EditBookingsForm.estimatedAtten.value;
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
                  document.EditBookingsForm.estimatedDate.value="";
                  document.EditBookingsForm.estimatedDate.select();
       }else if(data.result == 'lesser'){
       }
	}
}




function loadFunction(){
if(document.EditBookingsForm.shipper.value==""){
		document.getElementById("shipperContactButton").style.visibility = 'hidden';
	}
	if(document.EditBookingsForm.fowardername.value==""){
		document.getElementById("forwarderContactButton").style.visibility = 'hidden';
	}
if(document.EditBookingsForm.consigneename.value==""){
		document.getElementById("congineeContactButton").style.visibility = 'hidden';
}
if(document.EditBookingsForm.truckerName.value==""){
document.getElementById("truckerContactButton").style.visibility='hidden';
}
	
	//alert(document.EditBookingsForm.localdryage.length);
	for(var i=0;i<document.EditBookingsForm.localdryage.length;i++){
		if(document.EditBookingsForm.localdryage[i].value=="N" && document.EditBookingsForm.localdryage[i].checked){
			document.EditBookingsForm.amount.readOnly=true;
		}
	}
	for(var i=0;i<document.EditBookingsForm.intermodel.length;i++){
		if(document.EditBookingsForm.intermodel[i].value=="N" && document.EditBookingsForm.intermodel[i].checked){
			document.EditBookingsForm.amount1.readOnly=true;
		}
	}
	for(var i=0;i<document.EditBookingsForm.insurance.length;i++){
		if(document.EditBookingsForm.insurance[i].value=="N" && document.EditBookingsForm.insurance[i].checked){
			document.EditBookingsForm.costofgoods.readOnly=true;
		}
	}
	
	
	}
function getWareHouseAdd(ev){ 
        document.EditBookingsForm.emptypickupaddress.value="";
		if(event.keyCode==9 || event.keyCode==13){
		var params = new Array();
	 	params['requestFor'] = "wareHouseAddress";
	 	params['wareHouseName'] = document.EditBookingsForm.exportPositioning.value;
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
	   	    document.EditBookingsForm.emptypickupaddress.value=data.WareHouseAddress;
	    }else{
	   	    document.EditBookingsForm.emptypickupaddress.value="";
   	    }
   	     
   	      if(data.WareHouseAddress){
	   	    document.EditBookingsForm.addressofDelivery.value=data.WareHouseAddress;
	    }else{
	   	    document.EditBookingsForm.addressofDelivery.value="";
   	    }
   	    if(data.WareHouseName==undefined){
   	    	document.EditBookingsForm.emptypickupaddress.value="";
   	    	
   	    }
   	  
    }
}	


	
//function BookingsReport(){
//  document.EditBookingsForm.buttonValue.value="BKGConfReport";
//  document.EditBookingsForm.submit();
//}
function RefenceReport(val){
if(val=='on'){
document.EditBookingsForm.buttonValue.value="RefenceReportwithoutsave";
}else{
  document.EditBookingsForm.buttonValue.value="RefenceReport";
 }
  document.EditBookingsForm.submit();
}
function WorkOrderReport(val){
if(val=='on'){
document.EditBookingsForm.buttonValue.value="WorkOrderReportwithoutsave";
}else{
  document.EditBookingsForm.buttonValue.value="WorkOrderReport";
}
  document.EditBookingsForm.submit();
}
function getAccountName(ev){
if(event.keyCode==9 || event.keyCode==13){
document.EditBookingsForm.buttonValue.value="recalc";
document.EditBookingsForm.submit();
}
}
function BKGConf(val){
if(val=='on'){
document.EditBookingsForm.buttonValue.value="BKGConfReportWithoutSave";
}else{
document.EditBookingsForm.buttonValue.value="BKGConfReport";
}
document.EditBookingsForm.submit();
}
function CostBookingsReport(){
   document.EditBookingsForm.buttonValue.value="CostBookingReport";
   document.EditBookingsForm.submit();
}	
function popup1(mylink, windowname){
 if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string'){
   href=mylink;
}else{
   href=mylink.href;
}
mywindow=window.open(href, windowname, 'width=700,height=350,scrollbars=yes');
mywindow.moveTo(200,180);      
return false;
}
function SAVE(){
/*var bookingNo = document.EditBookingsForm.bookingNo;
if(bookingNo.value==""){
	alert("Enter the Booking Number");
 	bookingNo.focus(); 
	return; 
}*/

var etd = document.EditBookingsForm.estimatedDate;
  if(etd.value=="" && document.EditBookingsForm.bookingComplete[0].checked){
    alert("Enter the Estimated Date (ETD) ");
    etd.focus();
    return;
  }
var bookingdate =   document.EditBookingsForm.bookingDate;
if(bookingdate.value==""){
	alert("Enter the Booking Date");
	bookingdate.focus();
	return;
}
var shipper =  document.EditBookingsForm.shipper;
document.EditBookingsForm.buttonValue.value="update";
document.EditBookingsForm.submit();
}

function selectradio(){ 
  if(special=='Y'){
   document.getElementById('y1').checked=true;
  }else{
  document.getElementById('n1').checked=true;
  }
  if(hazmat=='Y'){
  document.getElementById('y2').checked=true;
  }else{
  document.getElementById('n2').checked=true;
  }
  if(outgage=='Y'){
  document.getElementById('y3').checked=true;
  }else{
  document.getElementById('n3').checked=true;
  }
  if(outgage=='Y'){
  document.getElementById('y3').checked=true;
  }else{
  document.getElementById('n3').checked=true;
  }
  if(localdryage=='Y'){
  document.getElementById('y6').checked=true;
  }else{
  document.getElementById('n6').checked=true;
  }
  if(intermodal=='Y'){
  document.getElementById('y7').checked=true;
  }else{
  document.getElementById('n7').checked=true;
  }
  if(insu=='Y'){
  document.getElementById('y8').checked=true;
  }else{
  document.getElementById('n8').checked=true;
  }
  if(document.EditBookingsForm.prepaidToCollect.value=='P'){
  document.getElementById('y3').checked=true;
  }else{
  document.getElementById('n3').checked=true;
  }
}
function disabled(val1){ 
if(val1==3){
	document.getElementById("charges").style.visibility = 'hidden';
	document.getElementById("copy").style.visibility = 'hidden';
	document.getElementById("conbl").style.visibility = 'hidden';
	document.getElementById("save").style.visibility = 'hidden';
    var imgs = document.getElementsByTagName('img');
    for(var k=0; k<imgs.length; k++){
      if(imgs[k].id != "previous"){
        imgs[k].style.visibility = 'hidden';
      }
    }
	var input = document.getElementsByTagName("input");
	for(i=0; i<input.length; i++){
      if(input[i].id != "buttonValue" && input[i].name !="govSchCode" 
    	&& input[i].name != "termNo" && input[i].name!="scheduleSuffix" && input[i].name!="state" 
    	&& input[i].name!="country"){
      		input[i].readOnly=true;
      		input[i].style.color="blue";
     	}
     }
     var textarea = document.getElementsByTagName("textarea");
     for(i=0; i<textarea.length; i++){
     	textarea[i].readOnly=true;
      	textarea[i].style.color="blue";
     }
     var select = document.getElementsByTagName("select");
     for(i=0; i<select.length; i++){
    	select[i].disabled=true;
   		select[i].style.backgroundColor="blue";
     }
   	}
   var datatableobj = document.getElementById('otherChargestable');
   if(datatableobj!=null){
	for(i=0; i<datatableobj.rows.length; i++){
		var tablerowobj = datatableobj.rows[i];
		if(tablerowobj.cells[12].innerHTML=='Y'){
			tablerowobj.cells[0].style.visibility="visible";
		    tablerowobj.cells[0].innerHTML="*";
		}
	 }
	}
	var datatableobj = document.getElementById('chargesTable');
	if(datatableobj!=null){
	 for(i=0; i<datatableobj.rows.length; i++){
		var tablerowobj = datatableobj.rows[i];
		if(tablerowobj.cells[14].innerHTML=='Y'){
		    tablerowobj.cells[0].style.visibility="visible";
			tablerowobj.cells[0].innerHTML="*";
		}
	 }
	}
   if(document.EditBookingsForm.amount.value=="0.00"){
	document.EditBookingsForm.amount.readOnly=true;
	}
	if(document.EditBookingsForm.amount1.value=="0.00"){
	document.EditBookingsForm.amount1.readOnly=true;
	}
	if(document.EditBookingsForm.costofgoods.value=="0.00"){
	document.EditBookingsForm.costofgoods.readOnly=true;
	}
}
var newwindow = '';
function add1(){
     document.EditBookingsForm.buttonValue.value="bookiingpopup";
  	 document.EditBookingsForm.submit();
     if (!newwindow.closed && newwindow.location){
         newwindow.location.href = "<%=path%>/jsps/fclQuotes/bookingPopUp.jsp";
     }else{
         newwindow=window.open("<%=path%>/jsps/fclQuotes/bookingPopUp.jsp","","width=700,height=300");
         if (!newwindow.opener) newwindow.opener = self;
     }
     if (window.focus){
       newwindow.focus()
      }
     return false;
  }
function getlocaldryage(){
if(document.EditBookingsForm.zip!=undefined){
	if(document.EditBookingsForm.zip.value ==''){
		alert("zip is not present");
		document.getElementById('n6').checked=true;
	 	return ;
	}
}
	document.EditBookingsForm.amount.readOnly=false;
}
function getlocaldryage1(){
	document.EditBookingsForm.amount.readOnly=true;
	document.EditBookingsForm.amount.value="0.00";
	document.EditBookingsForm.buttonValue.value="deleteLocalDrayage";
	document.EditBookingsForm.submit();
}
function getintermodel(){
if(document.EditBookingsForm.zip!=undefined){
	if(document.EditBookingsForm.zip.value ==''){
		alert("zip is not present");
		document.getElementById('n7').checked=true;
	 	return ;
	}
}
	document.EditBookingsForm.amount1.readOnly=false;
}
function getintermodel1(){
	document.EditBookingsForm.amount1.readOnly=true;
	document.EditBookingsForm.amount1.value="0.00";
	document.EditBookingsForm.buttonValue.value="deleteInterModal";
	document.EditBookingsForm.submit();
}
function getinsurance(){
	document.EditBookingsForm.costofgoods.readOnly=false;
}
function getinsurance1(){
	document.EditBookingsForm.costofgoods.readOnly=true;
	document.EditBookingsForm.buttonValue.value="deleteinsurance";
	document.EditBookingsForm.submit();
}
function getHazmat(){
	document.EditBookingsForm.buttonValue.value="hazmat";
	document.EditBookingsForm.submit();
}
function recalcfunction(){
    document.EditBookingsForm.buttonValue.value="recalc";
	document.EditBookingsForm.submit();
}
function getNumbersChanged(obj){
  var rowindex=obj.parentNode.parentNode.rowIndex;
  document.EditBookingsForm.numbIdx.value=rowindex-1;
  document.EditBookingsForm.numbers1.value=obj.value;
  document.EditBookingsForm.buttonValue.value="numbersChanged";
  document.EditBookingsForm.submit();
}
function popupcharges(mylink, windowname){
  document.EditBookingsForm.buttonValue.value="recalc";
  document.EditBookingsForm.submit();
if (!window.focus)return true;
var href;
if (typeof(mylink) == 'string')
   href=mylink;
else
   href=mylink.href;
window.open(href, windowname, 'width=500,height=200,scrollbars=yes');
return false;
}
function titleLetter(ev){
   if(event.keyCode==9){
	    window.open("<%=path%>/jsps/AccountsRecievable/customerSearch.jsp?button=EditsearchCustShipper&customersearch="+ ev,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    document.EditBookingsForm.buttonValue.value="recalc";
	    document.EditBookingsForm.submit();
    }
} 
function titleLetter1(ev) {
   if(event.keyCode==9){
	    window.open("<%=path%>/jsps/AccountsRecievable/customerSearch.jsp?button=EditsearchCustForwarder&customersearch="+ ev,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    document.EditBookingsForm.buttonValue.value="recalc";
	    document.EditBookingsForm.submit();
    }
} 
function titleLetter2(ev){
   if(event.keyCode==9){
	    window.open("<%=path%>/jsps/AccountsRecievable/customerSearch.jsp?button=EditsearchCustConsignee&customersearch="+ ev,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	    document.EditBookingsForm.buttonValue.value="recalc";
	    document.EditBookingsForm.submit();
    }
} 
function titleLetterName(ev){
	if(event.keyCode==9){
	document.EditBookingsForm.buttonValue.value="recalc";
	document.EditBookingsForm.submit();
	window.open("<%=path%>/jsps/ratemanagement/searchTerminal.jsp?button=EditNewBookingFCLs&tername=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
}
function getDestinationName(ev){
	if(event.keyCode==9){
	 document.EditBookingsForm.buttonValue.value="recalc";
	 document.EditBookingsForm.submit();
	 window.open("<%=path%>/jsps/datareference/SearchPierCode.jsp?button=EditNewBookingFCLs&percodename=" + ev ,"","toolbar=no,scrollBars=auto,resizable=no,status=no,width=600,height=400");
	}
}
function getForwarder(){
	if(event.keyCode==9){
	 document.EditBookingsForm.buttonValue.value="popupsearch";
	 document.EditBookingsForm.submit();
	 }
}
function getForwarderPress(){
	if(event.keyCode==13){
	 document.EditBookingsForm.buttonValue.value="popupsearch";
	 document.EditBookingsForm.submit();
	 }
}
function getConsignee(){
	if(event.keyCode==9){
	 document.EditBookingsForm.buttonValue.value="popupsearch";
	 document.EditBookingsForm.submit();
	}
}
function getConsigneePress(){
	if(event.keyCode==13){
	 document.EditBookingsForm.buttonValue.value="popupsearch";
	 document.EditBookingsForm.submit();
	}
}
function getShipper(){
	if(event.keyCode==9){
	 document.EditBookingsForm.buttonValue.value="popupsearch";
	 document.EditBookingsForm.submit();
	}
}
function getShipperPress(){
	if(event.keyCode==13){
	 document.EditBookingsForm.buttonValue.value="popupsearch";
	 document.EditBookingsForm.submit();
	}
}
function getComCode(){	
	if(event.keyCode==9){
	 document.EditBookingsForm.buttonValue.value="popupsearch";
	 document.EditBookingsForm.submit();
	}
}
function getComCodePress(){
   if(event.keyCode==13){
	document.EditBookingsForm.buttonValue.value="popupsearch";
	document.EditBookingsForm.submit();
	}
}
function copy1(){
    document.EditBookingsForm.buttonValue.value="copy";
	document.EditBookingsForm.submit();
}
function converttobl(){
if(!document.EditBookingsForm.bookingComplete[0].checked){
alert("Please Complete Booking");
return;
}
if(document.EditBookingsForm.estimatedDate.value==""){
alert("Please Select ETD");
document.EditBookingsForm.estimatedDate.focus();
return;
}
 	    var charges=confirm("Are you sure you want to convert this Booking to BL \n <c:out value="${bookingValues.username}"></c:out> \n <c:out value="${bookingValues.bookingDate}"></c:out>");
	if(charges){
    document.EditBookingsForm.buttonValue.value="converttobl";
	document.EditBookingsForm.submit();
	}
}
function CostBookingsReport(val){
if(val=='on'){
document.EditBookingsForm.buttonValue.value="CostBookingReportWithoutSave";
}else{
document.EditBookingsForm.buttonValue.value="CostBookingReport";
}
    
    document.EditBookingsForm.submit();
}
function getOrigin(){
   if(event.keyCode==9){
    document.EditBookingsForm.buttonValue.value="popupsearch";
	document.EditBookingsForm.submit();
}
}    
function getOriginPress(){
   if(event.keyCode==13){
	document.EditBookingsForm.buttonValue.value="popupsearch";
	document.EditBookingsForm.submit();
	}
}  
function getDestination(){
    if(event.keyCode==9){
	  document.EditBookingsForm.buttonValue.value="popupsearch";
      document.EditBookingsForm.submit();
    }
}    
function getDestinationPress(){	
	if(event.keyCode==13){
	  document.EditBookingsForm.buttonValue.value="popupsearch";
	  document.EditBookingsForm.submit();
	}
}
function CheckedShip(){
	
    if(document.EditBookingsForm.forwardercheck.disabled || document.EditBookingsForm.consigneecheck.disabled){
    	document.EditBookingsForm.shippercheck.checked = true;
    	return ;
    }else if(document.EditBookingsForm.shippercheck.checked){
	  document.EditBookingsForm.forwardercheck.checked=false;
	  document.EditBookingsForm.consigneecheck.checked=false;
	}
}
function CheckedForwarder(){
   if(document.EditBookingsForm.shippercheck.disabled || document.EditBookingsForm.consigneecheck.disabled){
    	document.EditBookingsForm.forwardercheck.checked = true;
    	return ;
    }else if(document.EditBookingsForm.forwardercheck.checked) {
	  document.EditBookingsForm.shippercheck.checked=false;
	  document.EditBookingsForm.consigneecheck.checked=false;
	}
}
function CheckedConsignee(){
   if(document.EditBookingsForm.shippercheck.disabled || document.EditBookingsForm.forwardercheck.disabled){
    	document.EditBookingsForm.consigneecheck.checked = true;
    	return ;
    }else if(document.EditBookingsForm.consigneecheck.checked){
	  document.EditBookingsForm.shippercheck.checked=false;
	  document.EditBookingsForm.forwardercheck.checked=false;
   }
}
function AlertMessage(){
   alert(" Enter Date and Time in this format --> MM/DD/YY  HH:MM ");
}
function getComCodeDesc(ev){  
   if(event.keyCode==9 || event.keyCode==13){
	  document.getElementById("comdesc").value="";
	  var params = new Array();
	  params['requestFor'] = "CommodityCodeDescription";
	  params['commodityCode'] = document.EditBookingsForm.commcode.value;
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
	var params = new Array();
	params['requestFor'] = "CommodityCode";
	params['commodityCodeDescription'] = document.EditBookingsForm.comdesc.value;
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
   document.getElementById("shipper").value="";
   jQuery(document).ready(function(){
	 var params = new Array();
	 params['requestFor'] = "CustAddress";
	 params['custName'] = document.EditBookingsForm.shipperName.value;
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
 document.getElementById("shipperContactButton").style.visibility = 'visible';
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
   document.getElementById("shipperName").value="";
   if(event.keyCode==9 || event.keyCode==13){
   var params = new Array();
   params['requestFor'] = "CustAddressForNo";
   params['custNo'] = document.EditBookingsForm.shipper.value;
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
 document.getElementById("shipperContactButton").style.visibility = 'visible';
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
   document.getElementById("forwarder").value="";
   jQuery(document).ready(function(){
	 var params = new Array();
	 params['requestFor'] = "CustAddress";
	 params['custName'] = document.EditBookingsForm.fowardername.value;
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
 document.getElementById("forwarderContactButton").style.visibility = 'visible';
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
    document.getElementById("fowardername").value="";
	if(event.keyCode==9 || event.keyCode==13){
		 var params = new Array();
		 params['requestFor'] = "CustAddressForNo";
     	 params['custNo'] = document.EditBookingsForm.forwarder.value;
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
 document.getElementById("forwarderContactButton").style.visibility = 'visible';
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
   document.getElementById("consignee").value="";
	jQuery(document).ready(function(){
	 var params = new Array();
	 params['requestFor'] = "CustAddress";
	 params['custName'] = document.EditBookingsForm.consigneename.value;
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
	document.getElementById("congineeContactButton").style.visibility = 'visible';
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
    document.getElementById("consigneename").value="";
	if(event.keyCode==9 || event.keyCode==13){
	 var params = new Array();
	 params['requestFor'] = "CustAddressForNo";
	 params['custNo'] = document.EditBookingsForm.consignee.value;
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
	document.getElementById("congineeContactButton").style.visibility = 'visible';
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
 document.getElementById("truckerCode").value="";
	if(event.keyCode==9 || event.keyCode==13){
	 var params = new Array();
	 params['requestFor'] = "CustAddress";
	 params['custName'] = document.EditBookingsForm.truckerName.value;
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
function getTruckerInfo1(ev){ 
    document.getElementById("truckerName").value="";
	if(event.keyCode==9 || event.keyCode==13){
	 var params = new Array();
	 params['requestFor'] = "CustAddressForNo";
	 params['custNo'] = document.EditBookingsForm.truckerCode.value;
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
function makeFormBorderless(form) {
			var element;
			for (var i = 0; i < form.elements.length; i++) {
			element = form.elements[i];
			if(element.type == "button"){
			if(element.value=="Copy" || element.value=="Charges" || element.value=="Recalc" || element.value=="ConvertToBl"
			|| element.value=="Look Up" || element.value=="Insurance" || element.value=="Hazmat"
			|| element.value=="Get Rates" || element.value=="Input Rates Manually"){
				element.style.visibility="hidden";
				}
			}
		}
		return false;
	}  
	function getShipper(){  
		       var cust=document.EditBookingsForm.shipperName.value;
		       if(cust=='%'){
	cust = 'percent';
}
		       cust=cust.replace("&","amp;");
		       GB_show('Shipper','<%=path%>/quoteCustomer.do?buttonValue=BookingShipper&clientName='+cust,
  width="600",height="810");
			 }
function getForwarder(){  
		       var cust=document.EditBookingsForm.fowardername.value;
		       if(cust=='%'){
	cust = 'percent';
}
		      cust=cust.replace("&","amp;");
		       GB_show('Forwarder','<%=path%>/quoteCustomer.do?buttonValue=BookingForwarder&clientName='+cust,
  width="600",height="810");
			 }
function getConsignee(){
 var cust=document.EditBookingsForm.consigneename.value;
 if(cust=='%'){
	cust = 'percent';
}
cust=cust.replace("&","amp;");
		       GB_show('Consignee','<%=path%>/quoteCustomer.do?buttonValue=BookingConsignee&clientName='+cust,
  width="600",height="810");
}			 
function getTrukerName(){ 
		       var cust=document.EditBookingsForm.truckerName.value;
		       var customer=cust.replace("&","amp;");
		       GB_show('Trucker','<%=path%>/quoteCustomer.do?buttonValue=BookingTruker&clientName='+customer,
  width="600",height="810");
			 }	
function getBookingCustomer(val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11){
 if(val11=='BookingTruker'){
 document.getElementById("truckerContactButton").style.visibility = 'visible';
  val1=val1.replace(":","'");
		document.EditBookingsForm.truckerName.value=val1;
		document.EditBookingsForm.truckerCode.value=val2;
		document.EditBookingsForm.addressoftrucker.value=val3;
		document.EditBookingsForm.truckerPhone.value=val8;
		document.EditBookingsForm.truckerEmail.value=val10;
}
 if(val11=='BookingShipper'){
 document.getElementById("shipperContactButton").style.visibility = 'visible';
		 val1=val1.replace(":","'");
		document.EditBookingsForm.shipperName.value=val1;
		document.EditBookingsForm.shipper.value=val2;
		document.EditBookingsForm.shipperAddress.value=val3;
		document.EditBookingsForm.shipperCity.value=val4;
		document.EditBookingsForm.shipperState.value=val5;
		document.EditBookingsForm.shipperZip.value=val6;
		document.EditBookingsForm.shipperCountry.value=val7;
		document.EditBookingsForm.shipPho.value=val8;
		document.EditBookingsForm.shipperFax.value=val9;
		document.EditBookingsForm.shipEmai.value=val10;
}
if(val11=='BookingForwarder'){
 document.getElementById("forwarderContactButton").style.visibility = 'visible';
	 val1=val1.replace(":","'");
		document.EditBookingsForm.fowardername.value=val1;
		document.EditBookingsForm.forwarder.value=val2;
		document.EditBookingsForm.forwarderAddress.value=val3;
		document.EditBookingsForm.forwarderCity.value=val4;
		document.EditBookingsForm.forwarderState.value=val5;
		document.EditBookingsForm.forwarderZip.value=val6;
		document.EditBookingsForm.forwarderCountry.value=val7;
		document.EditBookingsForm.forwarderPhone.value=val8;
		document.EditBookingsForm.forwarderFax.value=val9;
		document.EditBookingsForm.forwarderEmail.value=val10;
}if(val11=='BookingConsignee'){
		document.getElementById("congineeContactButton").style.visibility = 'visible';
		 val1=val1.replace(":","'");
		document.EditBookingsForm.consigneename.value=val1;
		document.EditBookingsForm.consignee.value=val2;
		document.EditBookingsForm.consigneeAddress.value=val3;
		document.EditBookingsForm.consigneeCity.value=val4;
		document.EditBookingsForm.consigneeState.value=val5;
		document.EditBookingsForm.consigneeZip.value=val6;
		document.EditBookingsForm.consigneeCountry.value=val7;
		document.EditBookingsForm.consigneePhone.value=val8;
		document.EditBookingsForm.consigneeFax.value=val9;
		document.EditBookingsForm.consigneeEmail.value=val10;
}if(val11=='AccountNameShipper'){
 val1=val1.replace(":","'");
		document.EditBookingsForm.accountName.value=val1;
		document.EditBookingsForm.accountNumber.value=val2;
	
}if(val11=='AccountNameForwarder'){
 val1=val1.replace(":","'");
		document.EditBookingsForm.accountName.value=val1;
		document.EditBookingsForm.accountNumber.value=val2;
	
}if(val11=='AccountNameThirdParty'){
 val1=val1.replace(":","'");
		document.EditBookingsForm.accountName.value=val1;
		document.EditBookingsForm.accountNumber.value=val2;
	
}if(val11=='AccountNameAgent'){
 val1=val1.replace(":","'");
		document.EditBookingsForm.accountName.value=val1;
		document.EditBookingsForm.accountNumber.value=val2;
	
}

 }
function getShipperAddress(){
var cust=document.EditBookingsForm.shipperName.value;
cust=cust.replace("&","amp;");
if(document.EditBookingsForm.shipper.value==""){
alert("Please Select Account Name");
return;
}
var addr= 'percent';
GB_show('Shipper','<%=path%>/contactAddress.do?buttonValue=BookingShipper&clientNo='+
document.EditBookingsForm.shipper.value+
'&address='+addr,width="600",height="800");
}
function getForwarderAddress(){
var cust=document.EditBookingsForm.fowardername.value;
 var customer=cust.replace("&","amp;");
if(document.EditBookingsForm.fowardername.value==""){
alert("Please Select Account Name");
return;
}
var addr= 'percent';
GB_show('Forwarder','<%=path%>/contactAddress.do?buttonValue=BookingForwarder&clientNo='+document.EditBookingsForm.forwarder.value+
'&address='+addr,width="600",height="800");
}
function getConsigneeAddress(){
var cust=document.EditBookingsForm.consigneename.value;
 var customer=cust.replace("&","amp;");
if(document.EditBookingsForm.consigneename.value==""){
alert("Please Select Account Name");
return;
}
var addr= 'percent';
GB_show('Consignee','<%=path%>/contactAddress.do?buttonValue=BookingConsignee&clientNo='+document.EditBookingsForm.consignee.value+
'&address='+addr,width="600",height="800");
}
function getTruckerAddress(){
if(document.EditBookingsForm.truckerName.value==""){
alert("Please Select Account Name");
return;
}
var addr='percent';
GB_show('Trucker','<%=path%>/contactAddress.do?buttonValue=BookingTruker&clientNo='+document.EditBookingsForm.truckerCode.value+
'&address='+addr,width="600",height="800");
}
function refreshPage(val1,val2,val3,val4,val5,val6,val7,val8){
	  document.EditBookingsForm.unitSelect.value=val1;
	  document.EditBookingsForm.number.value=val2;
	  document.EditBookingsForm.chargeCode.value=val3;
	  document.EditBookingsForm.chargeCodeDesc.value=val4;
	  document.EditBookingsForm.costSelect.value=val5;
	  document.EditBookingsForm.currency1.value=val6;
	  document.EditBookingsForm.chargeAmt.value=val7;
	  document.EditBookingsForm.minimumAmt.value=val8;
	  document.EditBookingsForm.buttonValue.value="addCharges"
	  document.EditBookingsForm.submit();
	}
function getPopHazmat(){
	if(document.EditBookingsForm.hazmat[0].checked){
	 GB_show('Hazmat','<%=path%>/fCLHazMat.do?buttonValue=Booking&name=Booking&number='+document.EditBookingsForm.bookingId.value,
	  width="600",height="800");
	}else{
	alert("Please Select Hazmat");
	return;
	}
}
function getAccountNumberDojo(ev){
	 if(event.keyCode==9 || event.keyCode==13){
	     document.getElementById("accountNumber").value="";
		 var params = new Array();
		 params['requestFor'] = "CustAddress";
		 params['custName'] = document.EditBookingsForm.accountName.value;
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
	 if(document.EditBookingsForm.billToCode[2].checked){
	 		     document.getElementById('accountName').disabled =false;
				 document.getElementById('accountNumber1').disabled = false;
				 document.getElementById('toggle').style.visibility ="visible";
	        //document.EditBookingsForm.option.value='ThirdParty';
	       // document.getElementById("optionId").disabled=true;
	       // tt="T";
	 }
  //dojo.widget.byId('autoTextbox').action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=7&pcollect="+tt;
}
function getAccountDetails(val2)
{
       var cust=document.EditBookingsForm.accountName.value;
       if(cust=='%'){
		  cust = 'percent';
		}
       cust=cust.replace("&","amp;");
         GB_show('ThirdParty','<%=path%>/quoteCustomer.do?buttonValue=AccountNameThirdParty&clientName='+cust,width="600",height="810");
	

   /*if(document.getElementById('cRadio').checked){
          	var cust=document.EditBookingsForm.accountName.value;
        if(cust=='%'){
		  	cust = 'percent';
		}
       	cust=cust.replace("&","amp;");
        GB_show('Agent','<%=path%>/quoteCustomer.do?buttonValue=AccountNameAgent&clientName='+cust,width="600",height="800");
          return;
   }
   var option =document.EditBookingsForm.option.value;
   if(option=="00")
   {
         alert("Please select the option");
         return;
   }
   if(option == 'Shipper'){
       var cust=document.EditBookingsForm.accountName.value;
        if(cust=='%'){
		  cust = 'percent';
		}
       cust=cust.replace("&","amp;");
         GB_show('Shipper','<%=path%>/quoteCustomer.do?buttonValue=AccountNameShipper&clientName='+cust,width="600",height="800");
	 }else if(option == 'Forwarder'){
       var cust=document.EditBookingsForm.accountName.value;
        if(cust=='%'){
		  cust = 'percent';
		}
       cust=cust.replace("&","amp;");
         GB_show('Forwarder','<%=path%>/quoteCustomer.do?buttonValue=AccountNameForwarder&clientName='+cust,width="600",height="800");
	 }*/
  
 }

function getInsurance(){
 if(document.EditBookingsForm.insurance[1].checked){
	 alert("Please select Insurance");
	 return;
	 }
	if(document.EditBookingsForm.costofgoods.value==0.00){
	alert("Please enter Cost Of Goods");
	return;
	}
 GB_show('Insurance','<%=path%>/jsps/fclQuotes/calculateInsurance.jsp?costOfGoods='+document.EditBookingsForm.costofgoods.value,
  					width="200",height="300");
	 //document.EditBookingsForm.buttonValue.value="insurance"
	 //document.EditBookingsForm.submit();
}
function calculateInsurance(val1,val2){
	document.EditBookingsForm.costofgoods.value=val1;
	document.EditBookingsForm.insuranceAmount.value=val2;
	document.EditBookingsForm.buttonValue.value="insurance";
	document.EditBookingsForm.submit();
}
function getAgent(){
	 var portOfDischarge=document.EditBookingsForm.portOfDischarge.value;
	 var destination=document.EditBookingsForm.destination.value
	GB_show('Agent','<%=path%>/quoteAgent.do?buttonValue=Agent&portOfDischarge='+portOfDischarge+'&destination='+destination,
  					width="400",height="600");
	}
	function getAgentInfo(val1,val2){
	  document.EditBookingsForm.agent.value=val1;
	  document.EditBookingsForm.agentNo.value=val2;
	}
	
	function getDestination(){
	if(document.EditBookingsForm.portOfDischarge.value==""){
				 var params = new Array();
				 params['requestFor'] = "AGENT";
				  var pod=document.EditBookingsForm.destination.value;
				 var index=pod.indexOf("/");
				 var podNew= pod.substring(0,index)
				 params['finalDestination'] = podNew;
				 var bindArgs = {
				  
				 url: "<%=path%>/actions/getAgent.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateAgentDojo");
		}
			    }
	function populateAgentDojo(type, data, evt) {
		   	if(data){
		   	if(data.Agent!=undefined && data.Agent==""){
		   		document.getElementById("agent").value=data.Agent;
		   		}
		   		if(data.AgentNo!=undefined && data.AgentNo==""){
		   		document.getElementById("agentNo").value=data.AgentNo;}
		   		
		   		document.getElementById("agentlookup").style.visibility='hidden';
		   	
		   	}
		   }
	
	function getPod(){
		   if(document.EditBookingsForm.destination.value==""){
		   var params = new Array();
				 params['requestFor'] = "PODAGENT";
				  var pod=document.EditBookingsForm.portOfDischarge.value;
				 var index=pod.indexOf("/");
				 var podNew= pod.substring(0,index)
				 params['portofDischarge'] = podNew;
				 var bindArgs = {
				  
				   url: "<%=path%>/actions/getAgent.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateAgent1Dojo");
				}
		   }
		   function populateAgent1Dojo(type, data, evt) {
		   	if(data){
		  if(data.Agent!=undefined && data.Agent==""){
		   		document.getElementById("agent").value=data.Agent;}
		   	if(data.AgentNo!=undefined && data.AgentNo==""){
		   		document.getElementById("agentNo").value=data.AgentNo;}
		   		document.getElementById("agentlookup").style.visibility='hidden';
		   	}
		   	
		   }
		   
function getSslCode(){ 
	var cust=document.EditBookingsForm.sslDescription.value;
	var customer=cust.replace("&","amp;");
	if(customer=='%'){
		customer = 'percent';
	}
 	GB_show('Client Info','<%=path%>/quoteCustomer.do?buttonValue=CarrierQuotation&clientName='+customer,
  					width="400",height="810");
} 
function getVesselName(){
var cust=document.EditBookingsForm.vessel.value;
	if(cust=='%'){
		cust = 'percent';
	}	
 	GB_show('Vessel Name','<%=path%>/vesselLookUp.do?buttonValue=bookingVessel&clientName='+cust,
  					width="400",height="400");
}
function getCarrier(val1,val2,val3,val4,val5,val6,val7){
 document.EditBookingsForm.sslDescription.value=val1+'/'+val2;
}
function setAddress(ev){
	if(document.EditBookingsForm.emptypickupaddress.value){
	  document.EditBookingsForm.addressofDelivery.value=document.EditBookingsForm.emptypickupaddress.value;
	}
} 
function goBack(){
	var conf = confirm("Do u want to save the Booking changes?");
	if(conf ==  true){
		var etd = document.EditBookingsForm.estimatedDate;
  		if(etd.value=="" && document.EditBookingsForm.bookingComplete[0].checked){
    		alert("Enter the Estimated Date (ETD) ");
    		etd.focus();
    		return;
  		}
  		var bookingdate =  document.EditBookingsForm.bookingDate;
		if(bookingdate.value==""){
			alert("Enter the Booking Date");
			bookingdate.focus();
			return;
		}
		var shipper =  document.EditBookingsForm.shipper;
		
		document.EditBookingsForm.buttonValue.value="goBackSave";
		
		document.EditBookingsForm.submit();
	}else{
    	document.EditBookingsForm.buttonValue.value="goBack"
    	document.EditBookingsForm.submit();
    }
}
function getIssTerm(){ 
     var issuingTerm=document.EditBookingsForm.issuingTerminal.value;
     var index=issuingTerm.indexOf("-");
	 var newIssuingTerm= issuingTerm.substring(0,index)
     var issuTerm=newIssuingTerm.replace("&","amp;");
     if(issuTerm=='%'){
         		issuTerm = 'percent';
     }
     GB_show('Issuing Termial Info','<%=path%>/issuingTerminal.do?buttonValue=Quotation&issuingTerminal='+issuTerm,
			 width="400",height="700");
}
function getIssuingTerminal(val1){
   	document.EditBookingsForm.issuingTerminal.value=val1;
}

function validateDateForPort(){
	if(document.EditBookingsForm.railCutOff.value != 'null'
	&& document.EditBookingsForm.railCutOff.value != undefined
	&& document.EditBookingsForm.railCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.railCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.portCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultPort");
    }
    if(document.EditBookingsForm.docCutOff.value != 'null'
    && document.EditBookingsForm.docCutOff.value != undefined
    && document.EditBookingsForm.docCutOff.value!=''){
       var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.docCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.portCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultPort1");
    }
    if(document.EditBookingsForm.voyageDocCutOff.value != 'null'
    && document.EditBookingsForm.voyageDocCutOff.value!= undefined
    && document.EditBookingsForm.voyageDocCutOff.value!=''){
       var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.voyageDocCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.portCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultPort2");
    }
}
function goBackCall(){
	document.EditBookingsForm.buttonValue.value="goBack"
    document.EditBookingsForm.submit();
}
function resultPort(type, data, evt){
	if(data){
       if(data.result == 'lesser'|| data.result == 'equal'){
                  alert("Port should be Greater than Rail");
                  document.EditBookingsForm.portCutOff.value="";
                  document.EditBookingsForm.portCutOff.select();
                   
       }else if(data.result == 'greater'  ){
               
       }
	}
}

function resultPort1(type, data, evt){
	if(data){
       if(data.result == 'greater' || data.result == 'equal'){
                  alert("Port should be Lesser than Doc");
                  document.EditBookingsForm.portCutOff.value="";
                  document.EditBookingsForm.portCutOff.select();
                   
       }else if(data.result == 'lesser' ){ 
               
       }
	}
}
function resultPort2(type, data, evt){
	if(data){
       if(data.result == 'greater' || data.result == 'equal'){
                  alert("Port should be Lesser than Barge");
                  document.EditBookingsForm.portCutOff.value="";
                  document.EditBookingsForm.portCutOff.select();
                   
       }else if(data.result == 'lesser' ){ 
               
       }
	}
}
function validateDateForRail(){
	if(document.EditBookingsForm.portCutOff.value != 'null'
	&& document.EditBookingsForm.portCutOff.value!= undefined 
	&& document.EditBookingsForm.portCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.portCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.railCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultRail");
    }
    if(document.EditBookingsForm.docCutOff.value != 'null'
    && document.EditBookingsForm.docCutOff.value!= undefined  
    && document.EditBookingsForm.docCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.docCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.railCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultRail1");
    }
    if(document.EditBookingsForm.voyageDocCutOff.value != "null"
    && document.EditBookingsForm.voyageDocCutOff.value!= undefined 
    && document.EditBookingsForm.voyageDocCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.voyageDocCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.railCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultRail2");
    }
    
}

function resultRail(type, data, evt){
	if(data){
       if(data.result == 'greater' || data.result == 'equal'){
                  alert("Rail should be Lesser than Port");
                  document.EditBookingsForm.railCutOff.value="";
                  document.EditBookingsForm.railCutOff.select();
                   
       }else if( data.result == 'lesser' ){
               
       }
	}
}
function resultRail1(type, data, evt){
	if(data){
       if(data.result == 'greater' || data.result == 'equal'){
                  alert("Rail should be Lesser than Doc");
                  document.EditBookingsForm.railCutOff.value="";
                  document.EditBookingsForm.railCutOff.select();
                   
       }else if( data.result == 'lesser' ){
               
       }
	}
}
function resultRail2(type, data, evt){
	if(data){
       if(data.result == 'greater' || data.result == 'equal'){
                  alert("Rail should be Lesser than Barge");
                  document.EditBookingsForm.railCutOff.value="";
                  document.EditBookingsForm.railCutOff.select();
                   
       }else if( data.result == 'lesser' ){
               
       }
	}
}

function validateDateForDoc(){
	if(document.EditBookingsForm.voyageDocCutOff.value != 'null'
	&& document.EditBookingsForm.portCutOff.value != undefined
	&& document.EditBookingsForm.portCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.portCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.docCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultDoc");
    }
    if(document.EditBookingsForm.voyageDocCutOff.value != 'null'
     && document.EditBookingsForm.voyageDocCutOff.value != undefined
     && document.EditBookingsForm.voyageDocCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.docCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.voyageDocCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultDoc1");
    }
    if(document.EditBookingsForm.railCutOff.value != 'null'
    && document.EditBookingsForm.railCutOff.value != undefined
    && document.EditBookingsForm.railCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidation";
	 	params['ETD'] = document.EditBookingsForm.railCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.docCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultDoc2");
    }
}

function resultDoc(type, data, evt){
	if(data){
       if( data.result == 'lesser' || data.result == 'equal'){
                  alert("Doc should be Greater than Port");
                  document.EditBookingsForm.docCutOff.value="";
                  document.EditBookingsForm.docCutOff.select();
                   
       }else if(data.result == 'greater'  ){
               
       }
	}
}
function resultDoc1(type, data, evt){
	if(data){
       if( data.result == 'lesser' || data.result == 'equal'){
                  alert("Doc should be Lesser than Barge");
                  document.EditBookingsForm.docCutOff.value="";
                  document.EditBookingsForm.docCutOff.select();
                   
       }else if(data.result == 'greater'  ){
               
       }
	}
}
function resultDoc2(type, data, evt){
	if(data){
       if( data.result == 'lesser' || data.result == 'equal'){
                  alert("Doc should be Greater than Rail");
                  document.EditBookingsForm.docCutOff.value="";
                  document.EditBookingsForm.docCutOff.select();
                   
       }else if(data.result == 'greater'  ){
               
       }
	}
}
function validateDateBarge(){
	if(document.EditBookingsForm.docCutOff.value!= 'null'
	&& document.EditBookingsForm.docCutOff.value!= undefined
	&& document.EditBookingsForm.docCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidationReverse";
	 	params['ETD'] = document.EditBookingsForm.voyageDocCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.docCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultBarge");
    }
   if(document.EditBookingsForm.portCutOff.value!= 'null'
   && document.EditBookingsForm.portCutOff.value != undefined
   && document.EditBookingsForm.portCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidationReverse";
	 	params['ETD'] = document.EditBookingsForm.portCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.voyageDocCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultBarge1");
    }
    if(document.EditBookingsForm.portCutOff.value!='null'
    && document.EditBookingsForm.railCutOff.value!= undefined
    && document.EditBookingsForm.railCutOff.value!=''){
	 	var params = new Array();
	 	params['requestFor'] = "dateValidationReverse";
	 	params['ETD'] = document.EditBookingsForm.railCutOff.value;
	 	params['ETA'] = document.EditBookingsForm.voyageDocCutOff.value;
	 	var bindArgs = {
      		url: "<%=path%>/actions/dateValidation.jsp",
      		error: function(type, data, evt){alert("error");},
      		mimetype: "text/json",
      		content: params
     	};
   	 	var req = dojo.io.bind(bindArgs);
		dojo.event.connect(req, "load", this, "resultBarge2");
    }
    
}

function resultBarge(type, data, evt){
	if(data){
       if(data.result == 'lesser'  || data.result == 'equal'){
                  alert("Barge should be Greater than Doc");
                  document.EditBookingsForm.voyageDocCutOff.value="";
                  document.EditBookingsForm.voyageDocCutOff.select();
       }else if(data.result == 'greater'){
       }
	}
}

function resultBarge1(type, data, evt){
	if(data){
       if(data.result == 'greater'  || data.result == 'equal'){
                  alert("Barge should be Greater than Port");
                  document.EditBookingsForm.voyageDocCutOff.value="";
                  document.EditBookingsForm.voyageDocCutOff.select();
       }else if(data.result == 'lesser'){
       }
	}
}

function resultBarge2(type, data, evt){
	if(data){
       if(data.result == 'greater'  || data.result == 'equal'){
                  alert("Barge should be Greater than Rail");
                  document.EditBookingsForm.voyageDocCutOff.value="";
                  document.EditBookingsForm.voyageDocCutOff.select();
       }else if(data.result == 'lesser'){
       }
	}
}
function getRoutedByAgent(){
	var customerName=document.EditBookingsForm.routedByAgent.value;
	if(customerName=='%'){
		customerName = 'percent';
	}
	 GB_show('Routed By Agent','<%=path%>/quoteCustomer.do?buttonValue=RoutedBooking&clientName='+customerName,
  					width="400",height="810");
}
function getRoutedByAgentFromPopup(val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12,val13,val14){
	 val1=val1.replace(":","'");
	document.EditBookingsForm.routedByAgent.value = val1;
	document.EditBookingsForm.routedAgentCountry.value = val13
} 
function getAgentforDestination(ev){
   if(event.keyCode==9 || event.keyCode==13){
	 var params = new Array();
	 params['requestFor'] = "DESTAGENT";
	 var pod=document.EditBookingsForm.portOfDischarge.value;
	 var index=pod.indexOf("/");
	 var podNew= pod.substring(0,index)
	 params['destination'] = podNew;
	
	 
	   var bindArgs = {
	  
	  url: "<%=path%>/actions/getAgent.jsp",
	  error: function(type, data, evt){alert("error");},
	  mimetype: "text/json",
	  content: params
	 };
	 var req = dojo.io.bind(bindArgs);
	 dojo.event.connect(req, "load", this, "populateAgentDojo1");
    }
}
function populateAgentDojo1(type, data, evt) {
	if(data){
		if(data.agent!=undefined){
				document.getElementById("agent").value=data.agent;
				document.getElementById("agentlookup").style.visibility='hidden';
		}else{
	    		document.getElementById("agentlookup").style.visibility='visible';
		}
		if(data.agentNo!=undefined){
				document.getElementById("agentNo").value=data.agentNo;
		}
	}
}	
function getAgentforPod(ev){
		    if(document.getElementById("agent").value==""){
		    if(event.keyCode==9 || event.keyCode==13){
				 var params = new Array();
				 params['requestFor'] = "DESTAGENT";
				 var pod=document.EditBookingsForm.destination.value;
				 var index=pod.indexOf("/");
				 var podNew= pod.substring(0,index)
				 params['destination'] = podNew;
				 

				   var bindArgs = {
				  
				  url: "<%=path%>/actions/getAgent.jsp",
				  error: function(type, data, evt){alert("error");},
				  mimetype: "text/json",
				  content: params
				 };
				 var req = dojo.io.bind(bindArgs);
				 dojo.event.connect(req, "load", this, "populateAgentDojo2");
			    }
			    }
}
function populateAgentDojo2(type, data, evt) {
	if(data){
		   if(data.agent!=undefined){
		   		document.getElementById("agent").value=data.agent;
		   		document.getElementById("agentlookup").style.visibility='hidden';
		   	}else{
		   		document.getElementById("agentlookup").style.visibility='visible';
		   	}
		   	if(data.agentNo!=undefined){
		   		document.getElementById("agentNo").value=data.agentNo;
		   	}
		   		
	}
}
function enableAgentLookUp(){
		   if(document.EditBookingsForm.alternateAgent[0].checked){
		   		document.getElementById("agentlookup").style.visibility = 'hidden';
		   	}else{
		        document.getElementById("agentlookup").style.visibility = 'visible';
		   }
}
function load(e1,e2,e3){
parent.parent.call2();
if(!document.EditBookingsForm.billToCode[2].checked){
		document.getElementById('accountName').disabled =true;
	    document.getElementById('accountNumber1').disabled = true;
	    document.getElementById('toggle').style.visibility ="hidden";
}
        enableAgentLookUp();
		if(e3=='on'){
			document.getElementById('shippercheck').disabled = "disabled";
			document.getElementById('forwardercheck').disabled = "disabled";
		}else if(e1=='on'){
			document.getElementById('consigneecheck').disabled = "disabled";
   			document.getElementById('forwardercheck').disabled = "disabled";
		}else if(e2=='on'){
			document.getElementById('shippercheck').disabled  = "disabled";
			document.getElementById('consigneecheck').disabled  = "disabled";
		}
	var remarks1 = document.getElementById("remarks");
    remarks1.style.border=0;
 	remarks1.readOnly = true;
 	remarks1.className="bodybackgrnd";
 
 	var remarks2 = document.getElementById("remarks1");
	remarks2.style.border=0;
 	remarks2.readOnly = true;
 	remarks2.className="bodybackgrnd";
 	
 	var ratesRemarks = document.getElementById('ratesRemarks');
	ratesRemarks.style.border=0;
	ratesRemarks.readOnly = true;
	ratesRemarks.className="bodybackgrnd";
}
function getWareHouseForEquipPick(ev){
		  	GB_show('Equipment Pickup','<%=path%>/wareHouselookUp.do?buttonValue=BookingPositionLoc&wareHouse='+document.getElementById('exportPositioning').value,width="400",height="700");
}
function getWareHouseDetailsForPos(ev1,ev2){
			document.getElementById('exportPositioning').value = ev1;
			document.getElementById('emptypickupaddress').value = ev2;
			document.getElementById('addressofDelivery').value = ev2;
}
function getWareHouseForLoad(){
			GB_show('Equipment Pickup','<%=path%>/wareHouselookUp.do?buttonValue=BookingLoadLoc&wareHouse='+document.getElementById('positionlocation').value,width="400",height="700");
}
function getWareHouseDetailsForLoad(ev1,ev2){
			document.getElementById('positionlocation').value = ev1;
			document.getElementById('addressofExpPosition').value = ev2;
}
function getWarehouseForReturn(){
			GB_show('Equipment Pickup','<%=path%>/wareHouselookUp.do?buttonValue=BookingEquipReturn&wareHouse='+document.getElementById('loadLocation').value,width="400",height="700");
}
function getWareHouseDetailsForEquipReturn(ev1,ev2){
			document.getElementById('loadLocation').value = ev1;
			document.getElementById('addressofDelivery').value = ev2;
}
function setZipCode(ev){
		    document.EditBookingsForm.zip.value = ev;
}
function displayRampCity(){
	  	  var selectBoxValue=document.EditBookingsForm.moveType.selectedIndex;
 	  	  GB_show('RampCity Search', '<%=path%>/searchquotation.do?buttonValue=searchPort&textName=rampCity&from=terminal&typeOfmove='+selectBoxValue,width="250",height="600");
}
function disableThirdParty(){
			 document.getElementById('accountName').disabled =true;
			 document.getElementById('accountNumber1').disabled = true;
			 document.getElementById('toggle').style.visibility ="hidden";
}
function setVesselName(val){
   	document.EditBookingsForm.vessel.value=val;
}
function getFFCommission(){
		document.EditBookingsForm.buttonValue.value="FFCommssion";
		document.EditBookingsForm.submit();
}
function deleteFFCommission(){
		document.EditBookingsForm.buttonValue.value="deleteFFCommssion";
		document.EditBookingsForm.submit();
}
function addLocalDrayage(ev){
	document.EditBookingsForm.buttonValue.value = "addLocalDrayage";
	document.EditBookingsForm.submit();
}
function addInterModal(ev){
	document.EditBookingsForm.buttonValue.value = "addInterModal";
	document.EditBookingsForm.submit();
}
function deleteCharges(val1){
	document.EditBookingsForm.buttonValue.value="deleteCharge";
	document.EditBookingsForm.numbIdx.value=val1;
	var result = confirm("Are you sure you want to delete this Charge");
	if(result){
		document.EditBookingsForm.submit();
	}		
}
function deleteCharges1(val1){
	document.EditBookingsForm.buttonValue.value="deleteBLCharge";
	document.EditBookingsForm.numbIdx.value=val1;
	var result = confirm("Are you sure you want to delete this Charge");
	if(result){
		document.EditBookingsForm.submit();
	}		
}
function popupAddRates(windowname){
	if (!window.focus)return true;
	var href;
	var Origin;
	var Destination;
	var portOrigin=document.EditBookingsForm.originTerminal.value;
	var portDestionation = document.EditBookingsForm.portOfDischarge.value;
	var pol=document.EditBookingsForm.portOfOrigin.value; 
	var pod=document.EditBookingsForm.destination.value; 
	var but="editQuotes";
	var a="";
	var b="";
	var flag=false;
	var flag2=false;
	if(document.EditBookingsForm.moveType.selectedIndex=="0" || document.EditBookingsForm.moveType.selectedIndex=="1" || 
	   document.EditBookingsForm.moveType.selectedIndex=="2"){
		 a=document.EditBookingsForm.originTerminal.value;
		 b=document.EditBookingsForm.rampCity.value;
		 if(a==""){
		   flag=true;
		 }
		 if(b==""){
		   flag2=true;
		 }
		 if(flag==true && flag2==true){
		   alert("PLEASE SELECT ORIGIN TERMINAL OR RAMPCITY");
		   return;
		 }
	}else if(document.EditBookingsForm.moveType.selectedIndex=="3" || document.EditBookingsForm.moveType.selectedIndex=="4" || 
	  document.EditBookingsForm.moveType.selectedIndex=="5"){
		 b=document.EditBookingsForm.portOfOrigin.value;
		 if(b==""){
		   alert("PLEASE SELECT POL");
		   return;
		 }
	}else{	  
		 a=document.EditBookingsForm.rampCity.value;
		 if(a==""){
		   alert("PLEASE SELECT RAMPCITY");
		   return;
		 }
	}
	var haz="";
	if(document.EditBookingsForm.hazmat[0].checked){
		haz="Y";
	}else{
		haz="N";
	}
   if(document.EditBookingsForm.portOfDischarge.value=="" || document.EditBookingsForm.commcode.value==""){
		 	alert("PLEASE SELECT  DESTIONATION PORT, COMMODITY CODE");
	    	document.EditBookingsForm.portofDischarge.focus();
	    	return;
	}
	
	GB_show('Get Rates','<%=path%>/fclQuotes.do?buttonValue=getRates&origin='+document.EditBookingsForm.originTerminal.value+"&rampCity="+document.EditBookingsForm.rampCity.value
		+"&pol="+pol+"&pod="+pod+"&destn="+portDestionation+"&comid="+document.EditBookingsForm.commcode.value
		+"&hazmat="+haz,500,850);
}
function selectedMenu(val1,val2,val3,val4,val5,val6) {
     		document.EditBookingsForm.selectedCheck.value=val3; 
      		document.EditBookingsForm.unitselected.value=val2; 
      		document.EditBookingsForm.ssline.value=val1;
      		document.EditBookingsForm.selectedOrigin.value=val4;
      		document.EditBookingsForm.selectedDestination.value=val5;
      		document.EditBookingsForm.selectedComCode.value=val6;
 			document.EditBookingsForm.buttonValue.value="newgetRates";
 			document.EditBookingsForm.submit();
}
function setTabName(tabName){
	document.getElementById("selectedTab").value=tabName;
}
</script>          
 	<%@include file="../includes/resources.jsp" %>
 	<script src="<%=path%>/js/jquery/jquery-1.1.3.1.pack.js" type="text/javascript"></script>
    <script src="<%=path%>/js/jquery/jquery.history_remote.pack.js" type="text/javascript"></script>
    <script src="<%=path%>/js/jquery/jquery.tabs.pack.js" type="text/javascript"></script>
      <script type="text/javascript">
            $(function(){
               $('#booking').tabs();
               });
               
   	  </script>
   	   <link rel="stylesheet" href="<%=path%>/css/jquery-tabs/jquery.tabs.css" type="text/css" media="print, projection, screen" />

</head>

<div id="cover" style="width: 906px ;height: 1000px;"></div>
<body class="whitebackgrnd"><br />

<html:form  action="/editBooks" styleId="editbook" name="EditBookingsForm" type="com.gp.cvst.logisoft.struts.form.EditBookingsForm" scope="request">
<font color="red" size="4"><b><%=msg%></b></font>

<table width="100%" border="0" cellspacing="0" cellpadding="0"   style="padding-left:0px;">
  <tr class="textlabels">
	<td>
	   <table  width="70%" border="0">
	  	  <tr>
	  		  <td>
	  		     <table>
	  		         <tr>
	  		            <td><b>File No : </b><font color="red" size="4">${bookingValues.fileNo}</font><br /><br /></td>   
					 </tr>
				 </table>
			  <br /></td>
	  		  <td>
	  		     <table class="tableBorderNew">
	  		         <tr>
	  		            <td><b>Quote By :</b><br /><br /></td>   
					    <td style="padding-left: 5px;"><b>On :</b><br /><br /></td>   
					 </tr>
				 </table>
			  <br /></td>
			  <td>
				 <table class="tableBorderNew">
	  		          <tr>
	  		             <td>
	  		                  <c:choose> 
	  		                     <c:when  test="${bookingValues.username!=null}">
			  							<b>Booking By : </b>"${fn:toUpperCase(bookingValues.username)}"
	  		 					 </c:when>
  		 	 					 <c:otherwise>
			     						<b>Booking By : </b><%=bookingBy%>
		     					 </c:otherwise>
  		 					  </c:choose>
  		 				 <br /></td>
	  		             <td style="padding-left: 5px;"> On : 
  							 <c:choose>
  									<c:when  test="${bookingValues.bookingDate!=null}">
 									   <fmt:formatDate pattern="MM/dd/yyyy hh:mm a" var="dateBook"  value= "${bookingValues.bookingDate}"/> 
  									    <c:out value="${dateBook}" ></c:out>
  									</c:when>
  									<c:otherwise>
  										<%=bookingDate%>
  									</c:otherwise>
  							</c:choose>
  		 				 <br /></td>
  		 				 <td style="padding-left: 5px;">
  		 					    <c:choose>
		        					<c:when  test="${bookingValues.bookingCompletedBy!=null}">
  	        							<b>Completed By : </b>"${fn:toUpperCase(bookingValues.bookingCompletedBy)}"
		        					</c:when>
		 							<c:otherwise>
									    <b>	Completed By : </b>
								    </c:otherwise>
  		 						</c:choose>
  		 				 <br /></td>
  		                 <td style="padding-left: 5px;">
  		                           <b>On :</b>
  		                 <br /></td>
  		 			 </tr>
				   </table>
			  <br /></td>
			  <td>
				   <table class="tableBorderNew">
		  		       <tr>
		  		          <td><b>BL By :</b><br /><br /></td>   
						  <td style="padding-left: 5px;"><b>On :</b><br /><br /></td>   
					   </tr>
				  </table>
			  <br /></td>
	      </tr>
	  </table>
    <br /></td>
 </tr><%--1st row ends--%>

<tr><%--2nd row--%>
  <td>
	 <table width="100%" cellspacing="0" border="0"  cellpadding="0">
        <tr class="textlabels"></tr>
  	    <tr>
  			<td>
  				<%--<input type="button" value="Go Back" id="cancel" class="buttonStyleNew" onclick="goBack()"/>--%>
  				<input type="button" value="Go Back" id="cancel" class="buttonStyleNew" onclick="compareWithOldArray()"/>
  				<input type="button" value="Save" id="save" class="buttonStyleNew" onclick="SAVE()" style="width:50px;"/>
 			    <%--<input type="button" value="Copy" id="copy" class="buttonStyleNew" onclick="copy1()" style="width:50px;"/>
  				<input type="button" value="ConvertToBl" id="conbl" class="buttonStyleNew" onclick="converttobl()" style="width:80px"/>--%>
                <input type="button"  class="buttonStyleNew" id="charge" value="ConvertToBl" 
  				onclick="converttobl()" style="width:80px"/>
  				<%--<input type="button" value="Print" id="print" class="buttonStyleNew" onclick="BookingsReport()" style="width:50px;"/>--%>
  				<input type="button" id="bConf" value="Bkg Confirmation" class="buttonStyleNew" onclick="BKGConf('<%=blFlag%>')" style="width:100px"/>
  				<input type="button" id="refNo" value="Ref No" class="buttonStyleNew" onclick="RefenceReport('<%=blFlag%>')"/>
 			    <input type="button" id="worOr" value="WorkOrder" class="buttonStyleNew" onclick="WorkOrderReport('<%=blFlag%>')"/>
  				<input type="button" id="worOr" value="Cost" class="buttonStyleNew" onclick="CostBookingsReport('<%=blFlag%>')" style="width:50px;"/>   
  				<input type="button" class="buttonStyleNew" id="note" name="search" value="Note" style="width:50px;"
  				    onclick="return GB_show('Notes', '<%=path%>/notes.do?moduleId='+'<%=NotesConstants.FILE%>&moduleRefId='+'${bookingValues.fileNo}',300,700);" /><br />
  		    <br /></td>
        </tr>
     </table>
  <br /></td>
</tr><%--2nd ends--%>

<tr><%--3rd row--%>
	<td width="100%" style="padding-top:4px;">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" ><%--ii --%>	
			<tr>
				<td width="100%">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"  >	
						<tr><%--hh --%>	
							<td width="25%">
		 						<table width="100%" border="0" cellpadding="6" cellspacing="0" class="tableBorderNew" >
		 							<tr class="tableHeadingNew">Booking Information</tr>
		 	     					<tr class="textlabels" >
						  				<td>SS Bkg #<br /><br /></td>
		 	               				<td align="left" class="textlabels">Tel #<br /><br /></td>
		 	     					</tr>
        		 					<tr>	
        		         				<td><html:text property="SSBooking" size="15" value="${bookingValues.SSBookingNo}"/><br /><br /></td>	   
        				 				<td><html:text property="telePho"  value="${bookingValues.telNo}"    size="15"   maxlength="30"/><br /><br /></td>
		 	     					</tr>
		 	     					<tr class="textlabels">         
		 	              				<td><span onmouseover="tooltip.show('<strong>SS Line Booking Represent</strong>',null,event);" onmouseout="tooltip.hide();">SSLBRep</span><br /><br /></td>
		 	               				<td> Email<br /><br /></td>
		 	    					</tr>
		 	     					<tr>          
		 	            				<td><html:text property="SSLineBookingRep" size="15" value="${bookingValues.SSLineBookingRepresentative}" /><br /><br /></td>
		 	        					<td><html:text property="bookingemail" value="${bookingValues.bookingemail}" size="15"></html:text><br /><br /></td>
		 	     					</tr><%--           
		 	     					<tr class="textlabels">
		 	     		 			    <td><span onmouseover="tooltip.show('<strong>Client Contact</strong>');" onmouseout="tooltip.hide();">Contact</span></td>		 	    		  
		 	              				<td><span onmouseover="tooltip.show('<strong>Sales Represent Code</strong>');" onmouseout="tooltip.hide();">SR Code</span></td>
		 	     					</tr>
		 	     					<tr>		  
		 	              				<td><html:text property="attenName" size="15" value="${bookingValues.attenName}" /></td>
              		      				<td><html:text property="slaesRepCode"  size="15"  value="${bookingValues.salesRepCode}" styleId="radio4"/></td>
		 	     					</tr>--%>
		 	     					<tr><td colspan="2">&nbsp;<br /><br /></td></tr>
				 					<tr><td colspan="2">&nbsp;<br /><br /></td></tr>
									<tr><td colspan="2">&nbsp;<br /><br /></td></tr>
				 					<tr><td colspan="2">&nbsp;<br /><br /></td></tr>
		 	 					</table>
						<br /><br /></td>
						<td width="1%" valign="top">&nbsp;<br /><br /></td>
						<td width="70%" valign="top"><%--gg --%>	
						   <table border="0" width="100%">
							   <tr>
									<td width="70%" valign="top">
			 							<table width="100%" border="0" cellpadding="3" cellspacing="2" class="tableBorderNew">
			 								<tr class="tableHeadingNew">Carrier Information</tr>
			 	     						<tr>
				 	         					<td  class="textlabels">Vessel<br /><br /></td>
							 					<td><span class="textlabels">		     
						            					<input name="vessel" id="vessel" maxlength= "7" size="17" value="${bookingValues.vessel}"  />
						            					<input type="button" value="Look Up"  class="buttonStyleNew" onclick="getVesselName()" style="width:50px" />
						            				</span>
						     					<br /><br /></td>
			 	             					<td class="textlabels">Carrier<br /><br /></td>
						    					<%if(quoteId!=null && quoteId.equals("")){%>
	        				  					<td><span class="textlabels">
							  							<%--<html:text property="sslcode"  value="<%=ssline%>" maxlength="7" size="12" onkeydown="getSSLine(this.value)"/>&nbsp;&nbsp;&nbsp;<img border="0" src="<%=path%>/img/search1.gif" onclick="return popup1('<%=path%>/jsps/ratemanagement/SearchSSline.jsp?button='+'NewBookingFCLs&amp;customersearch='+document.EditBookingsForm.sslcode.value,'windows3')"></img></span>--%>
	        				      						<input name="sslDescription" id="sslDescription" maxlength= "7" size="25" value="${bookingValues.sslname}"  /> 
	   							  						<input type="button" value="Look Up"  class="buttonStyleNew" onclick="getSslCode()" style="width:50px" />
	              		      						</span><br />
	              		      					<br /></td> 
	        			    					<%}else{%>
	        				   					<td><span class="textlabels">
	        				      						<input name="sslDescription" id="sslDescription" maxlength= "7" size="25" value="${bookingValues.sslname}" readonly="readonly" /> 
	              		       						</span><br />
	              		       					<br /></td>
	        									<%}%>  
			 	    					   </tr>
			 	     					   <tr class="textlabels">
			 	               					<td align="left" ><span class="textlabels">ComCode<font color="red">*</font></span><br /><br /></td>
	              		       					<%if(quoteId!=null && quoteId.equals("")){%>
	              		       					<td><span class="textlabels">
													<%--<html:text  property ="commcode"  maxlength= "7" size="12" value="<%=comcode%>" readonly="true"/>&nbsp;&nbsp;&nbsp;--%>
	                                				<input name="commcode" id="commcode"  size="14" value="${bookingValues.comcode}" onkeydown="getComCodeDesc(this.value)"  /> 
							   						</span><br />
							   					<br /></td> 
							   					<%}else{ %>
							   					<td><span class="textlabels">
							   						<input name="commcode" id="commcode" size="14" value="${bookingValues.comcode}" onkeydown="getComCodeDesc(this.value)" readonly="readonly" /> 
							   						</span><br />
							   					<br /></td>
							  					<%}%>
							   					<td align="right"><span class="textlabels">Descr</span><br /><br /></td>
							   					<td>
								  					<input name="comdesc" id="comdesc" readonly="readonly" value="${bookingValues.comdesc}"  size="25" onkeydown="getComCode(this.value)" />
			                  					<br /><br /></td>
					        					<%--<td  class="headerbluelarge" > <html:text styleId="comdesc" property="comdesc"  size="12"  value="<%=desc %>" readonly="true" ></html:text></td> --%>
			 	  						  </tr>
			 	  						   <tr>
			 	          						<td align="left" class="textlabels" align="center"> SS Voy<br /><br /></td>
			 	         					    <td><html:text property="voyage" size="14" value="${bookingValues.voyageCarrier}"/><br /><br /></td>
			 	          						<td class="textlabels" align="right">ETD<br /><br /></td>
						  						<td class="textlabels">
	      				  							<html:text property="estimatedDate" size="14" value="${estimatedDate}" readonly="true" onchange="validateDate1()" styleId="txtcal22"/>
	      				  							<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal22" onmousedown="insertDateFromCalendar(this.id,0);"/><br />
	      				  						<br /></td>
			 	  						  </tr>
			 	 						  <tr>
			 	        					 <td class="textlabels">Voy Internal <br /><br /></td>
			 	        					 <td><html:text property="vaoyageInternational"  size="14"   value="${bookingValues.voyageInternal}" /><br /><br /></td>      
			 	         					 <td align="right" class="textlabels">ETA<br /><br /></td>
											 <td>
												 <html:text property="estimatedAtten" size="14" value= "${estimatedAtten}" readonly="true" onchange="validateDate()" styleId="txtcal5"/>
												 <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal5" onmousedown="insertDateFromCalendar(this.id,0);" /><br />
											<br /></td>
			 	 						 </tr>
			                        </table>
								<br /><br /></td>
								<td width="1%" valign="top">&nbsp;<br /><br /></td>
								<td width="33%" valign="top">
			 						<table width="100%" border="0" cellpadding="1" cellspacing="0" class="tableBorderNew" style="padding-left:10px;">
			 							<tr class="tableHeadingNew">Cut-Off Dates</tr>
			 	          				<tr class="textlabels">
			 	          					<td><span onmouseover="tooltip.show('<strong>Rail Cutoff</strong>',null,event);" onmouseout="tooltip.hide();">Rail</span><br /><br /></td>
	                       					<td>
	                       						<html:text property="railCutOff" size="14" value="${railCutOff}" readonly="true" onchange="validateDateForRail()" styleId="txtcal70" />
	                       						<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal70" onmousedown="insertDateFromCalendar(this.id,1);" /><br />
	                       					<br /></td>
	                      				</tr>
	                      				<tr class="textlabels"> 
	                      					<td><span onmouseover="tooltip.show('<strong>Port CutOff</strong>',null,event);" onmouseout="tooltip.hide();">Port</span><br /><br /></td>
	                          				<td>
	          									<html:text property="portCutOff" size="14" readonly="true" value="${portCutOff}" onchange="validateDateForPort()" styleId="txtcal33"/>
	          									<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal33" onmousedown="insertDateFromCalendar(this.id,1);"/><br />
	          								<br /></td>
	                      				</tr>
	                      				<tr class="textlabels"> 
	                       					<td><span onmouseover="tooltip.show('<strong>Doc Cutoff</strong>',null,event);" onmouseout="tooltip.hide();">Doc</span><br /><br /></td>
	                      					<td align="left">
	                        					<html:text property="docCutOff" size="14" readonly="true" value="${docCutOff}" onchange="validateDateForDoc()" style="color:red" styleId="txtcal71" onclick="AlertMessage()" />
	                        					<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal71" onmousedown="insertDateFromCalendar(this.id,1);" /><br />
	                        				<br /></td> 
	                      				</tr>
	                      				<tr class="textlabels"> 
	                        				<td><span onmouseover="tooltip.show('<strong>Barge CutOff</strong>',null,event);" onmouseout="tooltip.hide();">Barge</span><br /><br /></td>
											<td valign="top">
												<html:text property="voyageDocCutOff" readonly="true"  size="14"   value="${voyDocCutOff}" onchange="validateDateBarge()" styleId="txtcal98" />
												<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal98" onmousedown="insertDateFromCalendar(this.id,1);" /><br />
											<br /></td>
			 	          				</tr>
			 			  				<tr class="textlabels">	
			 			   					<td><span onmouseover="tooltip.show('<strong>Auto CutOff</strong>',null,event);" onmouseout="tooltip.hide();">Auto</span><br /><br /></td>	
											<td>
							  					<html:text styleId="txtcal4" readonly="true" property="cutoffDate" size="14" value="${cutDate}" />
							  					<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal4" onmousedown="insertDateFromCalendar(this.id,1);" /><br />
							  				<br /></td>
						 				</tr>
			 						</table>
								<br /><br /></td>
							</tr>
							<tr>
								<td colspan="3" style="padding-top:4px">
									<table width="100%" class="tableBorderNew" border="0" cellpadding="1">     
			  							<tr class="tableHeadingNew">Billing</tr>
			  							<tr class="textlabels">
			  		 						<td>Prepaid/Collect<br /><br /></td>
				     						<td align="left">
				     							<html:radio property="prepaidToCollect" value="P" name="transactionBean" />P
		    									<html:radio property="prepaidToCollect" styleId="cRadio" value="C" name="transactionBean" />C
												<%--<html:radio property="prepaidToCollect" value="T" name="transactionBean" onclick="test()"/>T--%>
		   			 						<br /><br /></td>
		   			 						<td>Bkg Complete <br /><br /></td>
		    		 						<td>
		    		 						    <html:radio property="bookingComplete" value="Y" name="transactionBean"/>Y
		    			 						<html:radio property="bookingComplete" value="N" name="transactionBean"/>N
		    		 						<br /><br /></td>
			   						   </tr>
		       						   <tr class="textlabels">
		   			 						<td width="15%">Bill To Code <br /><br /></td>
					 						<td width="15%" align="left"><html:radio property="billToCode" value="F" onclick="disableThirdParty()" name="transactionBean"/>F
		      									 <html:radio property="billToCode" value="S"  name="transactionBean" onclick="disableThirdParty()"/>S
			  			 						 <html:radio property="billToCode" value="T" name="transactionBean" onclick="test()"/>T 
			  		                         <br /><br /></td>
			  		 						<td>&nbsp;<br /><br /></td><td>&nbsp;<br /><br /></td>
		   			 						<%--<td>Select </td>
				     						<td valign="top"><html:select property="option"  styleClass="textdatestyle" styleId="optionId" style="width:112px">
	    			 							<html:optionsCollection name="optionList" styleClass="unfixedtextfiledstyle" />
	    			 						</html:select></td>--%>
	    	   						   </tr>
	    	   						   <tr class="textlabels">
		       								<td> Third Party Account Name<br /><br /></td>
		       								<td align="left"><input name="accountName" id="accountName" size="25"  value="${bookingValues.accountName}"  /> 
					     						<%--<dojo:autoComplete formId="EditBookingsForm" id="autoTextbox" textboxId="accountName"  action="<%=path%>/actions/getCustomerName.jsp?tabName=BOOKING&from=7"/>--%>
        			     						<input type="button" id="toggle" value="LookUp" class="buttonStyleNew" onclick="getAccountDetails(this.value)" style="width:56px"/>
        									<br /><br /></td>
        									<td style="padding-left:10px;">Account #<br /><br /></td>
  			     							<td><html:text property="accountNumber" styleId="accountNumber1" size="15" value="${bookingValues.accountNumber}"></html:text><br /><br /></td>
		     						  </tr>
		    						  <tr class="textlabels"></tr>
								 </table>
			                   <br /><br /></td>
		                  </tr>
					  </table>
					<br /><br /></td>	<%--gg --%>	
				</tr><%--hh --%>	
    		</table>
 			<br /><br /></td>
		  </tr>
     </table><%--ii --%>	

	<br style="margin-top:5px"/> <%--jj --%>
  <div id="booking" style="width:99%;padding-left:5px;">
  			<ul>
               <li><a href="#tradeRoute"><span>Trade Route</span></a><br /></li>
  	 		</ul>
  	 		
  <div id="tradeRoute"  style='background-color:#E6F2FF;'>		
	<table width="100%" border="0" cellpadding="1" cellspacing="0" class="tableBorderNew" style="padding-left:10px;"> 
      <tr class="tableHeadingNew" >Trade Route</tr>
	  <tr class="textlabels">
		<td>
			<table width="100%">
				<tr class="textlables">
					<td>Move Type<br /><br /></td>
        			<td><html:select property="moveType" disabled="true" value="${bookingValues.moveType}" styleId="moveType"  style="width: 147px;"><html:optionsCollection name="typeOfMoveList" /></html:select>
        			<br /></td>
        			<td colspan="4">&nbsp;<br /></td>
				</tr>
				<tr class="textlables">
					 <td>Origin<font color="red">*</font></td>
					 <%if(quoteId!=null && quoteId.equals("")){%>
		    			<td><input  name="originTerminal" id="originTerminal" value="${bookingValues.originTerminal}" readonly="readonly" size="20" /> 
						<%--		       <span class="hotspot" onmouseover="tooltip.show('<strong>Search Orgin</strong>');" onmouseout="tooltip.hide();">--%>
						<%--		       <img src="<%=path%>/img/icons/web.gif" border="0" onClick="return GB_show('Orgin Search', '<%=path%>/editBooks.do?buttonValue=searchPort&textName=originTerminal',250,600);" /> --%>
						<%--               <dojo:autoComplete formId="EditBookingsForm" textboxId="originTerminal" action="<%=path%>/actions/getUnlocationCode.jsp?tabName=BOOKING&from=0"/>--%>
            			</td> 
					 <%}else{ %>
			 			<td>
			 			   <input  name="originTerminal" id="originTerminal" value="${bookingValues.originTerminal}"  size="20" readonly="readonly" /> 
			 			</td> 
					 <%}%>
					  <c:choose>
					     <c:when test="${bookingValues.moveType == 'DD' || bookingValues.moveType == 'DR' || bookingValues.moveType == 'DP'}">
						     <td  id="originZip">Origin Zip 
	   		 			     	<span style="padding-left:65px;"><html:text property="zip"  styleClass="varysizeareahighlightwhite"  value="${bookingValues.zip}" onkeyup="getzip(this)" style="width:140px" /></span>
	   						    <input type="button" id="zipLookUp" value="Look Up" class="buttonStyleNew" 
	   						    onclick="return GB_show('Zip Search', '<%=path%>/searchZipCode.do?zip='+document.EditBookingsForm.zip.value,450,450);" style="width:50px"/>
	   					    </td>
	   				     </c:when>
	   				     <c:otherwise>
	   				        <td>&nbsp;</td>
	   				     </c:otherwise>
   				      </c:choose>
   					   <td class="textlabels">Default Agent</td>
        			   <td class="textlabels">
        			  		<html:radio property="alternateAgent" onclick="enableAgentLookUp()" value="Y" name="transactionBean"/>Yes
      						<html:radio property="alternateAgent" onclick="enableAgentLookUp()" value="N" name="transactionBean"/>No<br />
      				   </td>
				 </tr>
				 <tr class="textlables">
						<%--<td>New--%>
						<%-- <html:checkbox property="newRampCity" styleId="newRampCity" onclick="newRampCity1()" name="EditBookingsForm"></html:checkbox>--%>
						<%-- Ramp City<font color="red">*</font></td>--%>
						<td>Ramp/CY<font color="red">*</font><br /></td>
 						<td id="rampCity1">
        					<input name="rampCity" id="rampCity" value="${bookingValues.rampCity}" size="20" readonly="readonly"/>    
         					<span class="hotspot" onmouseover="tooltip.show('<strong>Search Orgin</strong>',null,event);" onmouseout="tooltip.hide();">
								<%--<img src="<%=path%>/img/icons/web.gif" border="0" onClick="return GB_show('Orgin Search', '<%=path%>/searchquotation.do?buttonValue=searchPort&textName=isTerminal',250,600);" />
          						<input type="button" id="rampLookUp"  value="Look Up" class="buttonStyleNew" onclick="displayRampCity()" style="width:50px" />--%>
       	    				</span>	
       	    			<br /></td>
       	    			<td>Transit Days
     	 					<span style="padding-left:50px;"><html:text property="noOfDays" value="${bookingValues.noOfDays}"  size="20"/></span>
    					<br /></td>
    					<td>Agent<br /></td>
 		    			<td colspan="1"><html:text property="agent"  value="${bookingValues.agent}" size="20"/>
 	        				<input type="button" value="Look Up" class="buttonStyleNew" onclick="getAgent()" style="width:50px" id="agentlookup"/>
 	        			<br /></td>
    			 </tr>
    			 <tr>
    					<td>POL&nbsp;<br /></td>
			 			<td><input  name="portOfOrigin" id="portOfOrigin" value="${bookingValues.portofOrgin}"  size="20" readonly="readonly"/>   
	       					<%--<input type="button" class="buttonStyleNew" value="Look Up" style="width:50px"  onClick="return GB_show('Orgin Search', '<%=path%>/editBooks.do?buttonValue=searchPort&textName=portOfOrigin',250,600);" /> --%>
       				 	<br/></td>
       					<td>Issuing Terminal
       					<%if(quoteId!=null && quoteId.equals("")){%>
       						<span class="textlabels"  style="padding-left:25px;">
				 				<input type="text" name="issuingTerminal" value="${bookingValues.issuingTerminal}" id="issuingTerminal" size="20"/>
  								<input type="button" value="Look Up"  class="buttonStyleNew" onclick="getIssTerm()" style="width:50px"/>
  							</span>
				 		<%}else{%>
				   			<span class="textlabels"  style="padding-left:25px;">
	        	     			<input type="text" name="issuingTerminal" value="${bookingValues.issuingTerminal}" id="issuingTerminal" size="20" readonly="readonly" /> 
								<input type="button" value="Look Up"  class="buttonStyleNew" onclick="getIssTerm()" style="width:50px"/>
							</span>
	        	 		<%}%> 
	        	 		<br /></td>
						<td>Agent Number<br /></td>
 						<td><html:text property="agentNo" readonly="readonly" value="${bookingValues.agentNo}" size="20"/><br /></td>
    			 </tr>
    			 <tr>
    				 	<td>POD&nbsp;<br /></td>
			 		 	<td><input  name="destination" id="destination" value="${bookingValues.destination}"  size="20" 
				 			onkeyup="getPod()" onkeydown="getAgentforPod(this.value)" readonly="readonly"/>   
	      					<%--<input type="button" class="buttonStyleNew" value="Look Up" style="width:50px"  onClick="return GB_show('Orgin Search', '<%=path%>/editBooks.do?buttonValue=searchPort&textName=destination&from=pod',250,600);" />--%> 
             		 	<br /></td>
             		 	<td colspan="1">&nbsp;<br /></td>
             		 	<td>Routed By Agent<br /></td>
  			     		<td align="left"><html:text property="routedByAgent" value="${bookingValues.routedByAgent}" size="20" ></html:text>
  			     	 			<input type="button" value="Look Up"  class="buttonStyleNew" onclick="getRoutedByAgent()" style="width:50px"/>
  			     		<br /></td>
             	 </tr>
             	 <tr>
             	 	  <td>Destination<font color="red">*</font><br /></td>
	   			 	  <%if(quoteId!=null && quoteId.equals("")){%>
			 	 		<td><input  name="portOfDischarge" id="portOfDischarge" readonly="readonly" value="${bookingValues.portofDischarge}"  size="20" 
			 		 		onkeyup="getDestination()" onkeydown="getAgentforDestination(this.value)"/>   
							<%--		 	     <span class="hotspot" onmouseover="tooltip.show('<strong>Search Orgin</strong>');" onmouseout="tooltip.hide();">--%>
							<%--		 	     <img src="<%=path%>/img/icons/web.gif" border="0" onClick="return GB_show('Orgin Search', '<%=path%>/editBooks.do?buttonValue=searchPort&textName=portOfDischarge&from=destination',250,600);" /> --%>
							<%--		 	     <dojo:autoComplete formId="EditBookingsForm" textboxId="portOfDischarge" action="<%=path%>/actions/getUnlocationCode.jsp?tabName=BOOKING&from=2"/>--%>
        	 			<br /></td>
       				 <%}else{ %>
			 			<td>
			 				 <input name="portOfDischarge" id="portOfDischarge"  value="${bookingValues.portofDischarge}"  size="20" readonly="readonly" onkeyup="getDestination()"/>   
        	 			<br /></td>
       				<%}%>
       				 <td colspan="1">&nbsp;<br /></td>
       				 <td>Country&nbsp;<br /></td>
             		 <td><html:text property="routedAgentCountry" value="${bookingValues.routedbyAgentsCountry}"></html:text><br /></td>
       			</tr>
       			<%--<tr>
       				 <td>Remarks:</td>
 	   				 <td><html:text property="portremarks" style="color:Red;width:100%;" styleId="remarks" value="${bookingValues.destRemarks}">
   						 				</html:text></td>
       			</tr>--%>
       			<tr class="textlabels">
  		 			<td valign="top" >Remarks:<br /></td>
       					<c:choose>
	      					<c:when test="${not empty bookingValues.destRemarks}">
	      						<td width="50%" valign="top">
	      						<html:textarea property="portremarks" cols="40" rows="4"  style="color: Red;height : 75px;  overflow:hidden;" styleId="remarks"  value="${bookingValues.destRemarks}" /><br /></td>
	 						</c:when>
	 						<c:otherwise>
	 							<td width="50%" valign="top">
	 							<html:textarea property="portremarks"   style="color: Red; height: 15px; overflow:hidden;" styleId="remarks"  value="${bookingValues.destRemarks}" /><br /></td>
	 						</c:otherwise>
 						</c:choose>
 						<c:choose>
	      					<c:when test="${not empty bookingValues.destRemarks1}">
	      						<td width="46%" valign="top" >
	      						<html:textarea property="portremarks1" cols="40" rows="4"  style="color: Red; height : 75px; overflow:hidden;" styleId="remarks1"  value="${bookingValues.destRemarks1}" /><br /></td>
	 						</c:when>
	 						<c:otherwise>
	 							<td width="46%" valign="top" >
	 							<html:textarea property="portremarks1"   style="color: Red; height: 15px; overflow:hidden;" styleId="remarks1"  value="${bookingValues.destRemarks1}" /><br /></td>
	 						</c:otherwise>
 						</c:choose>
    			 </tr>
    			 <tr class="textlabels">
	    			   <td valign="top"><br /></td>
	    				<c:choose>
		    				<c:when test="${not empty bookingValues.ratesRemarks}">
		    	        		<td><html:textarea property="ratesRemarks" cols="40" rows="1"    style="color: Red; overflow:hidden;" styleId="ratesRemarks" value="${bookingValues.ratesRemarks}" /><br /></td>
		    	       	 	</c:when>
		    	        	<c:otherwise>
		    	            	<td><html:textarea property="ratesRemarks"    style="color: Red; overflow:hidden;height:10px;" styleId="ratesRemarks" value="${bookingValues.ratesRemarks}" /><br /></td>
		    	        	</c:otherwise>
	    	        	</c:choose>
    	        </tr>
    	    </table>
		   <br /></td>
	   </tr>
    </table><%--jj --%>
  </div>
  </div>

	<br style="margin-top:5px"/><%--kk --%> 
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" > 
	  <tr>
		 <td width="25%" valign="top">
		 	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
		 		 <tr class="tableHeadingNew">Shipper</tr>
		 	 	 <tr>
		              <td class="textlabels" > Name<br /></td>
		              <td class="textlabels">
		 	             	<input name="shipperName" id="shipperName" value="${bookingValues.shipper}" maxlength="20" 
		 	             	size="23" onkeydown="getShipperInfo(this.value)" />   
		 	            <br /></td>	 	         
		         </tr>
				 <tr>
					  <td>&nbsp;<br /></td>
					  <td><input type="button" id="toggle" value="Look Up" class="buttonStyleNew" onclick="getShipper(this.value)" style="width:56px"/><br /></td>
		 	     </tr>
		 	     <tr class="textlabels">
		 	          <td class="textlabels" width="10%">Code<br /></td>
		 	          <td class="textlabels">
		 	              <input  name="shipper" id="shipper" value="${bookingValues.shipNo }" maxlength="15" size="23" onkeydown="getShipperInfo1(this.value)" />   
		 	          <br /></td>
		 	     </tr>
		         <tr>
		 	      	  <td  class="textlabels" valign="top"> Address<br /></td>
		 	   		  <td><html:textarea property="shipperAddress" cols="18" rows="3" value="${bookingValues.addressforShipper}"></html:textarea>
 						   <input type="button" id="shipperContactButton" value="Look Up" class="buttonStyleNew" onclick="getShipperAddress(this.value)" style="width:46px"/>
    				  <br /></td>		 	      
		 	     </tr>
		         <tr>          
		 	           <td class="textlabels"> City<br /></td>
		 	           <td class="textlabels">
		 	             <html:text property="shipperCity"  size="8" value="${bookingValues.shipperCity}"/>State
		 	             <html:text size="4" property="shipperState" value="${bookingValues.shipperState}"/><br />
		 	           </td>
		 	     </tr>
		 	     <tr>
              		  <td class="textlabels" > Zip<br /></td>
              		  <td><html:text property="shipperZip"   size="23" value="${bookingValues.shipperZip}" onkeypress="getzip(this)" maxlength="10"/><br /></td>
              	 </tr>
              	 <tr>
		 	     	 <td class="textlabels" > Country<br /></td>
		 	     	 <td><html:text property="shipperCountry"   size="23"  value="${bookingValues.shipperCountry}"/><br /></td>
		 	     </tr>
		 	     <tr>
		 	         <td class="textlabels"> Phone<br /></td>
        			 <td class="textlabels">
        			 <html:text property="shipPho" size="23" value="${bookingValues.shipperPhone}" /><br /></td>
		 	     </tr>
		 	     <tr>
		 	        <td  class="textlabels"> Fax<br /></td>
         			<td><html:text property="shipperFax" size="23" value="${bookingValues.shipperFax}" /><br /></td>
		 	     </tr>
		 	     <tr>
              		 <td class="textlabels"> Email<br /></td>
              		 <td class="textlabels">
              		 	<html:text property="shipEmai" size="23" value="${bookingValues.shipperEmail}" /><br />
              		 </td>
           	    </tr>
           	    <tr>
               		<td class="textlabels">Client Ref<br /></td>
                	<td><html:text property="shipperClientReference" maxlength="20" size="23" value="${bookingValues.shipperClientReference}" /> <br /></td>
                </tr>
                <tr class="textlabels">
            	    <td>Client<br /></td>
					<td><html:checkbox property="shippercheck" styleId="shippercheck" name="transactionBean" onclick="CheckedShip()"></html:checkbox><br /></td>
                </tr>
		  </table>
	 <br /></td>
	 <td width="25%" valign="top"> 
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		 	 <tr class="tableHeadingNew">Forwarder</tr>
		 	 <tr>
              		<td  class="textlabels" align="right"> Name<br /></td>
     				<td class="textlabels">
                 		<input  name="fowardername" id="fowardername" value="${bookingValues.forward}" maxlength="15" size="23" onkeydown="getForwarderInfo(this.value)" />   
	            	<br /></td>          
            </tr>
            <tr>
            	  <td>&nbsp;<br /></td>
           		  <td> <input type="button" id="toggle" value="Look Up" class="buttonStyleNew" onclick="getForwarder(this.value)" style="width:56px"/><br /></td>
            </tr>
		 	<tr>
		 	       <td  class="textlabels" width="10%" align="right">Code<br /></td>
		 	       <td class="textlabels">
		 	         <input  name="forwarder" id="forwarder" value="${bookingValues.forwNo }" maxlength="15" size="23" onkeydown="getForwarderInfo1(this.value)" />   
		 	       <br /></td>
		 	</tr>
            <tr>
		 	      <td  class="textlabels" align="right" valign="top"> Address<br /></td>
		 	      <td><html:textarea property="forwarderAddress" cols="18" rows="3" value="${bookingValues.addressforForwarder}"></html:textarea>
                  		<input type="button" id="forwarderContactButton" value="Look Up" class="buttonStyleNew" onclick="getForwarderAddress(this.value)" style="width:46px" />
    			  <br /></td>		 	                            
		 	</tr>
		 	<tr>
		 	      <td  class="textlabels" align="right"> City<br /></td>
				  <td class="textlabels"><html:text property="forwarderCity" size="8" value="${bookingValues.forwarderCity}"/>State
						<html:text property="forwarderState" size="5" value="${bookingValues.forwarderState}"/><br /></td>
		 	</tr>
            <tr>
              	  <td  class="textlabels" align="right"> Zip<br /></td>
   				  <td><html:text property="forwarderZip" size="23" value="${bookingValues.forwarderZip}" onkeypress="getzip(this)" maxlength="10"/><br /></td>
            </tr>
            <tr>
		 	       <td   class="textlabels" align="right"> Country<br /></td>
		 		   <td>
		 				<html:text property="forwarderCountry" size="23" value="${bookingValues.forwarderCountry}"/>
		 		   <br /></td>
		 	</tr>
		 	<tr>
		 	      <td  class="textlabels" align="right"> Phone<br /></td>
        		  <td  class="textlabels">
        			<html:text property="forwarderPhone" size="23" value="${bookingValues.forwarderPhone}" /><br />
        		  </td>
		 	</tr>
		 	<tr>       
		 	      <td  class="textlabels" align="right"> Fax<br /></td>
  				  <td><html:text property="forwarderFax" value="${bookingValues.forwarderFax}" size="23"/><br /></td>
		 	</tr>
		 	<tr>
             	  <td class="textlabels" align="right" > Email<br /></td>
 				  <td  class="textlabels"><html:text property="forwarderEmail" size="23" value="${bookingValues.forwarderEmail}" /><br /></td>
            </tr>
            <tr>
                 <td class="textlabels">Client Ref<br /></td>
                 <td><html:text property="forwarderClientReference" maxlength="20" size="23" value="${bookingValues.forwarderClientReference}" /> <br /></td>
           </tr>
		   <tr class="textlabels">
        		<td align="right">Client<br /></td>
      			<td><html:checkbox  property="forwardercheck" styleId="forwardercheck" name="transactionBean" onclick="CheckedForwarder()"></html:checkbox><br /></td>
          </tr>    
		</table>
	<br /></td>
	<td width="25%" valign="top">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		 	<tr class="tableHeadingNew" height="90%">Consignee</tr>
		 	 <tr>
              		<td  class="textlabels" align="right" > Name<br /></td> 
              		<td class="textlabels">
          				<input  name="consigneename" id="consigneename" value="${bookingValues.consignee }" maxlength="15" size="23" onkeydown="getConsigneeInfo(this.value)" />   
         			<br /></td>             
             </tr>
             <tr>
                   <td>&nbsp;<br /></td>
               	   <td><input type="button" id="toggle" value="Look Up" class="buttonStyleNew" onclick="getConsignee(this.value)" style="width:56px"/><br /></td>
             </tr>
             <tr>
		 	       <td class="textlabels" width="10%" align="right">Code<br /></td>
		 	       <td class="textlabels">
<%--		 	      <html:text property="consignee" value="<%=consigneeNo %>"  size="14" maxlength="15"  onkeydown="titleLetter2(this.value)"/>  <img src="<%=path%>/img/search1.gif" height="16"  onclick="return popup1('<%=path%>/jsps/AccountsRecievable/customerSearch.jsp?button='+'EditsearchCustConsignee&amp;customersearch='+document.EditBookingsForm.consignee.value,'windows3')"/>--%>
		 				<input  name="consignee" id="consignee" value="${bookingValues.consNo}" maxlength="15" size="23" onkeydown="getConsigneeInfo1(this.value)" />   
		 	       <br /></td>         
		 	 </tr>
             <tr>
		 	         <td   class="textlabels" valign="top" align="right"> Address<br /></td>
	 				 <td><html:textarea property="consigneeAddress" cols="18" rows="3" value="${bookingValues.addressforConsingee}"></html:textarea>  
      				 	<input type="button" id="congineeContactButton" value="Look Up" class="buttonStyleNew" onclick="getConsigneeAddress(this.value)" style="width:46px" />
    				 <br /></td>			 	
		 	 </tr>
		 	 <tr>
		 	        <td  class="textlabels" align="right"> City<br /></td>
					<td class="textlabels"><html:text property="consigneeCity" size="8" value="${bookingValues.consigneeCity}"/>State
					<html:text property="consigneeState" size="5" value="${bookingValues.consigneeState}"/><br /></td>
		 	 </tr> 
             <tr>
       				<td  class="textlabels" align="right"> Zip<br /></td>
					<td><html:text property="consigneeZip" value="${bookingValues.consigneeZip}" size="23"  onkeypress="getzip(this)" maxlength="10"/><br /></td>
             </tr> 
             <tr>
		 	        <td  class="textlabels" align="right"> Country<br /></td>
					<td><html:text property="consigneeCountry" size="23" value="${bookingValues.consigneeCountry}"/><br /></td>
		     </tr>
		     <tr>
		 	        <td  class="textlabels" align="right"> Phone<br /></td>
     				<td class="textlabels">
     				<html:text property="consigneePhone" value="${bookingValues.consingeePhone}" size="23" /><br /></td>
		     </tr>
		     <tr>
		 	      <td  class="textlabels" align="right"> Fax<br /></td>
   				  <td><html:text property="consigneeFax" value="${bookingValues.consigneeFax}" size="23" /><br /></td>
             </tr>
             <tr>
              	  <td  class="textlabels" align="right"> Email<br /></td>
				  <td class="textlabels"><html:text property="consigneeEmail"  size="23"   value="${bookingValues.consingeeEmail}" /><br /></td>
             </tr>
             <tr>
                  <td class="textlabels">Client Ref<br /></td>
                  <td><html:text property="consigneeClientReference" maxlength="20" size="23" value="${bookingValues.consigneeClientReference}" /> <br /></td>
             </tr>
            <tr class="textlabels">
            	<td align="right">Client<br /></td>
            	<td><html:checkbox property="consigneecheck" styleId="consigneecheck" name="transactionBean" onclick="CheckedConsignee()"></html:checkbox><br /></td>
            </tr>   
	  </table>
    <br /></td>
    <td width="25%" valign="top">
	   <table width="100%" border="0" cellpadding="3" cellspacing="0" style="padding-left:10px;">
		 	<tr class="tableHeadingNew" height="90%">Trucker</tr>
		    <tr class="textlabels">  
				<td align="right">Name<br /></td>
				<%--<td><html:text property="truckerName" size="30" value="<%=truckerName%>"></html:text><br /></td>--%>
				<td class="textlabels">
                      <input  name="truckerName" id="truckerName" value="${bookingValues.name}" maxlength="15" size="23" onkeydown="getTruckerInfo(this.value)" />
                <br /></td> 
			</tr>
			<tr>
				<td>&nbsp;<br /></td>
				<td>
					<input type="button" id="toggle" value="Look Up" class="buttonStyleNew" onclick="getTrukerName(this.value)" style="width:56px"/><br />
				</td>
			</tr>
			<tr class="textlabels">  
		        <td align="right">Code<br /></td>
				<%--<td><html:text property="truckerCode" size="30" value="<%=truckerCode%>"></html:text><br /></td>--%>
		        <td class="textlabels">
		 	        <input  name="truckerCode" id="truckerCode" value="${bookingValues.truckerCode}" maxlength="15" size="23" onkeydown="getTruckerInfo1(this.value)" />   
		 	    <br /></td>
		    </tr>	
			<tr class="textlabels">
				<td align="right" valign="top">Address<br /></td>
				<td><html:textarea property="addressoftrucker" cols="18" rows="3" value="${bookingValues.address}">
					</html:textarea><input type="button" id="truckerContactButton" value="Look Up" id="truckerContactButton" class="buttonStyleNew"  onclick="getTruckerAddress(this.value)"  style="width:46px"/>
				<br /></td>		
		    </tr>
		    <tr class="textlabels">  
				<td align="right">Phone<br /></td>
				<td><html:text property="truckerPhone" size="23" value="${bookingValues.truckerPhone}" ></html:text><br /><br /></td>
			</tr>	
		    <tr class="textlabels">  
			    <td align="right">Email<br /></td>
			    <td><html:text property="truckerEmail" size="23" value="${bookingValues.truckerEmail}"></html:text><br /><br /></td>
		    </tr>
		    <tr>
                <td class="textlabels">Client Ref<br /></td>
                <td><html:text property="truckerClientReference" maxlength="20" size="23" value="${bookingValues.truckerClientReference}" /> <br /></td>
           </tr>	
		</table>
	<br /></td>
    </tr>
   </table><%--kk --%>

  <br style="margin-top:5px"/> <%--ll--%>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
    <tr>
	    <td width="25%" valign="top" align="left" >
		   <table width="100%" border="0" cellpadding="0" cellspacing="0" >
		 	   <tr class="tableHeadingNew">Equipment Pickup</tr>
		 	   <tr>
	       				<td class="textlabels" style="padding-left:10px;"> Date <br /></td>
						<td>
							<html:text property="empPickupDate" size="16" readonly="true" value="${emptyPickUpDate}" styleId="txtcal9"  />
							<img src="<%=path%>/img/CalendarIco.gif"  alt="cal" name="cal1"  id="cal9" onmousedown="insertDateFromCalendar(this.id,0)" /> <br />
						</td>
			   </tr>
			   <tr>
					<td class="textlabels" style="padding-left:10px;" >Earliest Date<br /></td>
				    <td>
 						<html:text property="earlierPickUpDate" readonly="true" size="16" value="${earliestPickUpDate}" styleId="txtcal99"  />
 						<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal10" width="16" height="16" id="cal99" onmousedown="insertDateFromCalendar(this.id,0)" /><br />
 					</td>
	          </tr> 
	           <tr>
	            	<td  align="left" class="textlabels" style="padding-left:10px;">Location<br /></td>
					<td>
	 	 				<input name="exportPositioning" id="exportPositioning"  value="${bookingValues.exportPositoningPickup}"  
	 	  					size="16" onkeydown="getWareHouseAdd(this.value)" maxlength="50"/>
		 				<input type="button" class="buttonStyleNew" value="Look Up" style="width: 50px;" 
								onclick="getWareHouseForEquipPick(this.value)"/>
					<br /></td>
	         </tr>
	          <tr>      
	           	  <td valign="top" class="textlabels" style="padding-left:10px;"> Address<br /></td>
                  <td><html:textarea property="emptypickupaddress" styleId="emptypickupaddress" cols="17" rows="3" value="${bookingValues.emptypickupaddress}" 
                  		onkeydown="setAddress(this.value)" onkeyup="limitText(this.form.emptypickupaddress,this.form.countdown,200)">
                      </html:textarea><br />
                  </td>
	         </tr>
	       	 <tr class="textlabels">
	       		<td valign="top">&nbsp;&nbsp;Remarks<br /></td>
	       		<td><html:textarea property="pickUpRemarks" styleId="pickUpRemarks" cols="17" rows="3" 
	       		    value="${bookingValues.pickUpRemarks}" onkeyup="limitText(this.form.pickUpRemarks,this.form.countdown,50)"></html:textarea><br /></td>
	         </tr>	
	    </table> 
	 <br /></td>
	 <td width="25%" valign="top" align="left">
		<table width="100%" border="0" cellpadding="0" cellspacing="0" >
		 	    <tr class="tableHeadingNew">Spotting Address</tr>  
	            <tr class="textlabels">
	            	<td><span class="textlabels">Date</span><br /></td>
					<td>
					     <html:text  property="postioningDate" readonly="true" size="16" value="${positioningDate}" styleId="txtcal53" />
						 <img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal53" onmousedown="insertDateFromCalendar(this.id,0);" />
					<br /></td>
	            </tr>
	            <tr>
	              		<td class="textlabels" >Contact<br /></td>
	              		<td><html:text property="loadcontact" size="16" value="${bookingValues.loadcontact}" maxlength="50"></html:text><br /></td>
	            </tr>
	            <tr>
	              		<td class="textlabels">Phone<br /></td>
						<td><html:text property="loadphone" size="16" value="${bookingValues.loadphone}" maxlength="30"></html:text><br /></td>
	            </tr>
	            <tr><%--
	              	   <td class="textlabels" >Location</td>
						<td><html:text property="positionlocation" styleId="positionlocation" size="16" value="${bookingValues.positionlocation}" maxlength="50"/>
						<input type="button" class="buttonStyleNew" value="Look Up" style="width:50px; "  onclick="getWareHouseForLoad()"/>
						</td> --%>
	           </tr> 
			   <tr>
	            	<td valign="top" ><span class="textlabels"  >Address</span><br /></td>
					<td><html:textarea property="addressofExpPosition" styleId="addressofExpPosition" cols="17" rows="3" value="${bookingValues.addressForExpPositioning}" 
						onkeyup="limitText(this.form.addressofExpPosition,this.form.countdown,200)"/><br /><br /></td>    
		 	  </tr>
		 	  <tr class="textlabels">
	       			<td valign="top">Remarks<br /></td>
	       			<td><html:textarea property="loadRemarks" styleId="loadRemarks" cols="17" rows="3" value="${bookingValues.loadRemarks}" 
	       			onkeyup="limitText(this.form.loadRemarks,this.form.countdown,50)"></html:textarea><br /></td>
	          </tr>                        
	   </table>
	 <br /></td>      
	 <td width="25%" valign="top" align="left">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		 	    <tr class="tableHeadingNew"  >Equipment Return</tr>                 
	            <tr class="textlabels" >  
						<td>Date<br /></td>
					<td>
					<html:text property="loaddate" size="16" readonly="true"   value="${loaddate}" styleId="txtcal12" />
 					<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal12" onmousedown="insertDateFromCalendar(this.id,0);" /><br /></td>
		        </tr>
	            <%--<tr class="textlabels" >
		        		 <td>Location</td>
						 <td>
						 <input name="loadLocation" id="loadLocation" value="${bookingValues.loadLocation}"  size="16" onkeydown="getWareHouseAdd1(this.value)" maxlength="50"/>
          				<dojo:autoComplete formId="EditBookingsForm" textboxId="loadLocation" action="<%=path%>/actions/getWareHouseName.jsp?tabName=BOOKING&from=1"/> 
						<input type="button" class="buttonStyleNew" value="Look Up" style="width: 50px;" onclick="getWarehouseForReturn()"/>
						 </td>
				</tr>--%>
	            <tr>
	                   <td  valign="top"><span class="textlabels" >Address</span><br /></td>
					   <td><html:textarea property="addressofDelivery" styleId="addressofDelivery" cols="17" rows="3" value="${bookingValues.addessForExpDelivery}"
					   onkeyup="limitText(this.form.addressofDelivery,this.form.countdown,200)"/><br /><br /></td>
		 	    </tr>
		 	    <tr class="textlabels">
	       			<td valign="top">Remarks<br /></td>
	       			<td><html:textarea property="returnRemarks" styleId="returnRemarks" cols="17" rows="3" value="${bookingValues.returnRemarks}" 
	       			onkeyup="limitText(this.form.returnRemarks,this.form.countdown,50)"></html:textarea><br /></td>
	           </tr>
		 </table>
     <br /></td>
     <td width="25%" valign="top" align="left">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		 	  <tr class="tableHeadingNew">Equipment Control</tr>   
			  <tr>	
					<td class="textlabels">Date out of Yard<br /></td>
      				<td>
      					<html:text property="dateOutYard" size="16" readonly="true"   value="${dateOutOfYard}"  styleId="txtcal13"/>
    					<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal13" 
    						onmousedown="insertDateFromCalendar(this.id,0);" /><br />
    				</td>
		      </tr>  
		      <tr>	
		   		 	<td class="textlabels"><span onmouseover="tooltip.show('<strong>Date Back into Yard</strong>',null,event);" onmouseout="tooltip.hide();">Date Back into Yard</span><br /></td>
      				<td class="textlabels">
      				<html:text property="dateInYard" size="16" readonly="true"   value="${dateIntoYard}"  styleId="txtcal14"/>
    				<img src="<%=path%>/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal14" onmousedown="insertDateFromCalendar(this.id,0);" /><br /></td>
		     </tr>
	    </table>
	<br /></td>
  </tr>
  </table><%--ll--%>

  <br style="margin-top:5px"/> <%--mm--%>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
    <tr>
   		<td>
			<table>
	  			<tr class="textlabels">
	  	  			<td>Goods Description <br /></td>
	      			<td>Remarks<br /></td>
	  			</tr> 
      			<tr>
          			 <td><html:textarea property="goodsDescription" value="${bookingValues.goodsDescription}" cols="65"  rows="5"
           				 onkeyup="limitText(this.form.goodsDescription,this.form.countdown,500)"/><br /></td>
           			<td><html:textarea property="remarks" value="${bookingValues.remarks}" cols="65"  rows="5" 
            			onkeyup="limitText(this.form.remarks,this.form.countdown,500)"/><br /></td>
      			</tr>
      			<tr class="textlabels">
      	  			<td><html:checkbox property="printGoodsDescription"  name="transactionBean" ></html:checkbox>Print Goods Description<br /></td>
      	  			<td><html:checkbox property="printRemarks"  name="transactionBean">Print Remarks</html:checkbox><br /></td>
      			</tr>
			</table>
		<br /></td>
  	 </tr>
	</table><%--mm--%>

   <br style="margin-top:5px"/> <%--nn--%>
   <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
	   <td>
		  <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew" style="padding-left:10px;">
		 	<tr class="tableHeadingNew">Special Provisions</tr>
	      	<tr class="textlabels">
		        	<td width="10%">Special Equipment<br /></td>
			    	<td width="20%"><html:radio property="specialequipment" value="Y" styleId="y1" name="transactionBean"/>Yes
			        	<html:radio property="specialequipment" value="N" styleId="n1"  name="transactionBean"/>No
			    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    		<html:select property="specialEqpmtSelectBox" value="${bookingValues.specialEqpmtSelectBox}">
	             			<html:optionsCollection name="specialEquipmentList"/></html:select>
		       	    <br /></td>
		            <td width="10%">SOC <br /></td>
		            <td><html:radio property="soc" value="Y"  name="transactionBean"/>Yes
			           <html:radio property="soc" value="N"   name="transactionBean"/>No
		            <br /></td>
	        </tr>
	        <tr class="textlabels">
	            <td>Hazmat<br /></td>
		       	<td><label><html:radio property="hazmat" value="Y"  onclick="getHazmat()" name="transactionBean"/> Yes</label>
		        	<label><html:radio property="hazmat" value="N"  name="transactionBean"/>No</label>
	          	 <br /></td>
	         	<td>Customer Provides Hard Copy SED</td>
       	  	  	<td><html:radio property="customertoprovideSED" name="transactionBean" value="Y" />Yes
				  <html:radio property="customertoprovideSED"  name="transactionBean" value="N" />No
			  	<br /></td>
	        </tr>
	        <tr class="textlabels">
	         	<td>Out of Gage<br /></td>
		    	<td><label><html:radio property="outofgate" value="Y" styleId="y3" name="transactionBean"/>Yes </label>
		         	<label><html:radio property="outofgate" value="N" styleId="n3" name="transactionBean"/>No</label>
	         	<br /></td>
	         	<td>Auto Deduct FF Commission</td>
       	     	<td><html:radio property="deductFFcomm" value="Y"  name="transactionBean" onclick="getFFCommission()" />Yes
     				<html:radio property="deductFFcomm" value="N" name="transactionBean"  onclick="deleteFFCommission()"/>No
     			 <br /></td>
	     	</tr>
	  	 	<tr class="textlabels">
	  			<td style="padding-top:4px;">Local Drayage : <br /></td>
	  			<td>
	  			   <table  border="0">
			      	  <tr class="style2">
			        		<td><label><html:radio property="localdryage" value="Y" styleId="y6" onclick="getlocaldryage()" name="transactionBean"/>Yes</label>
			        			<label><html:radio property="localdryage" value="N" styleId="n6" onclick="getlocaldryage1()" name="transactionBean"/>No</label>
			        			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <br /></td>
			        		<td style="padding-top:4px;">Amount<br /></td>
			        		<td ><html:text property="amount" value="${amount}" onchange="addLocalDrayage(this.value)" size="5"/><br /></td>
			     	 </tr>
		          </table> 
	  		   <br /></td>
	  	   </tr>	
	  	   <tr class="textlabels">
	  		  	<td style="padding-top:4px;">Intermodal:<br /></td>
	  		  	<td>
					<table>
			 			<tr class="style2">
		        			<td><label> 
		          				<html:radio property="intermodel" value="Y" styleId="y7" onclick="getintermodel()" name="transactionBean"/>Yes</label>
		          				<label><html:radio property="intermodel" value="N" styleId="n7" onclick="getintermodel1()" name="transactionBean"/>No</label>
									 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br /></td>
		          			<td class="textlabels" style="padding-top:4px;">Amount<br /></td>
		          			<td ><html:text property="amount1"  value="${amount1}" onchange="addInterModal(this.value)" size="5"/><br /></td>
		        		</tr>  
      				</table>
	  			<br /></td>
	  	   </tr>
	  	   <tr>
	  			<td class="textlabels" style="padding-top:4px;">Insurance:<br /></td>
	  			<td style="padding-bottom:8px">
					<table>
					   <tr class="style2">
		        			<td class="textlabels">  
		         				<html:radio property="insurance" value="Y" styleId="y8" onclick="getinsurance()" name="transactionBean">Yes</html:radio>
		        				<html:radio property="insurance"  value="N" styleId="n8" onclick="getinsurance1()" name="transactionBean">No</html:radio>
		         					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br /></td>	
		        			<td class="textlabels" style="padding-top:4px;">Cost of Goods <br /></td>
		        			<td><html:text property="costofgoods" value="${costofgoods}" size="5"/><br /></td>
		        			<td class="textlabels" style="padding-top:4px;">Insurance Amt <br /></td>
		       			    <td><html:text property="insurancamt" value="${insurancamt}" size="5" readonly="true"/> <br /></td>
		     				<td><input type="button" class="buttonStyleNew" value="Insurance" onclick="getInsurance()"></input><br /></td>
		     		   </tr>  
      		       </table>
	  		   <br /></td>
	  	  </tr>	  
	  </table>
  	<br /></td>		    
  </tr>
   <tr style="padding-top: 10px;">
          <td align="center">
          	  <input type="button" class="buttonStyleNew" value="Hazmat" onclick="getPopHazmat()" />
    	      <input type="button"  class="buttonStyleNew" id="charge"  value="Input Rates Manually" style="width: 108px;"
			     onclick=" return GB_show('Booking Charges','<%=path%>/jsps/fclQuotes/BookingCharges.jsp?hazmat='+ 
 				 document.EditBookingsForm.hazmat.value + '&soc='+document.EditBookingsForm.soc.value + '&spcleqpmt='+
 				 document.EditBookingsForm.specialequipment.value +'&button='+'quote&bkgNo='+${bookingValues.bookingNumber},325,640)"/>
   		      <input type="button" onClick="return popupAddRates('windows')" Value="Get Rates" class="buttonStyleNew"/>
          <br /></td>
   </tr>
 </table><%--nn--%>

  <br style="margin-top:5px"/> <%--oo--%>
  <table width="90%" class="tableBorderNew">
    <tr class="tableHeadingNew">Rates</tr>
    <tr>
    	<td align="left" >
    		<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:80%">
       			<table width="90%" border="0" cellpadding="0" cellspacing="0">
       			 <%
 					List fcllist=(List)request.getAttribute("fclRates");
 					CostBean costbean=new CostBean();
 					int l=0;
 					String unittemp="";
         		 %>   
         		<display:table name="<%=fcllist%>"  class="displaytagstyle"  id="chargesTable" sort="list" style="width:100%" pagesize="<%=pageSize%>" > 
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
										String accoutnNo="";
										String accountName="";
										String newRate="";
										String effectiveDate="";
										String newFlag="";
					  					String id="";
					    	if((fcllist!=null && fcllist.size()>0))
					 			{
					             NumberFormat numformat1 = new DecimalFormat("##,###,##0.00");
					    		BookingfclUnits book=new BookingfclUnits();
					    		book=(BookingfclUnits)fcllist.get(l);
					    		id=String.valueOf(book.getId());
					    		if(book.getUnitType()!=null)
					    		{
					            unit=book.getUnitType().getCodedesc().toString();
					            }
					    		chargecode=book.getChgCode();
					    		costType=book.getCostType();
					    		if(book.getAmount()!=null && !book.getAmount().equals("")){
					    			amt=numformat1.format(book.getAmount());
					    		}
					    		if(book.getMarkUp()!=null)
					    		{
					    		markUp1=numformat1.format(book.getMarkUp());
					    		}
					    		if(book.getNewFlag()!=null){
					    		newFlag=book.getNewFlag();
					    		}else{
					    		newFlag="";
					    		}
					    		currency=book.getCurrency();
					    		num=book.getNumbers();
					    		chargecodedesc=book.getChargeCodeDesc();
					    		if(book.getSellRate()!=null)
					    		{
					    		sellRate=numformat1.format(book.getSellRate());
					    		}
					    		if(book.getAccountNo()!=null)
					    		{
					    		accoutnNo=book.getAccountNo();
					    		}
					    		else
					    		{
					    		accoutnNo="";
					    		}
					    		if(book.getAccountName()!=null)
					    		{
					    		accountName=book.getAccountName();
					    		}
					    		else
					    		{
					    		accountName="";
					    		}
					    		if(book.getFutureRate()!=null)
					    		{
					    		newRate=numformat1.format(book.getFutureRate());
					    		}
					    		else
					    		{
					    		newRate="";
					    		}
					    		if(book.getEfectiveDate()!=null)
					    		{
					    		effectiveDate=sdf.format(book.getEfectiveDate());
					    		}
					    		else
					    		{
					    		effectiveDate="";
					    		}
					    		}
    				 %>
      	<%if(newFlag.equals("new")){ %>
 			<display:column style="color: red;">*</display:column>
 		<%}else{ %>
 		<%} %>
     	<%if(!unittemp.equals(unit)){%>
      		<display:column title="UnitSelect" style="visibility:visible">
      			<html:text property="unitType" value="<%=unit%>" disabled="true" size="3"></html:text>
      		</display:column>
     	    <display:column title="Number" style="visibility:visible">
     	    	<html:text property="numbers" value="<%=num%>" size="3" maxlength="3"  onchange="getNumbersChanged(this)"/>
     	    </display:column>
       <%}else{ %>
     		<display:column title="UnitSelect" style="visibility:hidden">
     			<html:text property="unitType" value="<%=unit%>" disabled="true" style="visibility:hidden" size="3"></html:text>
     		</display:column>
     		<display:column title="Number" style="visibility:hidden"><html:text property="numbers" value="<%=num %>" size="3" maxlength="3" style="visibility:hidden" onchange="getNumbersChanged(this)"/></display:column>
       <%}unittemp=unit;%>
       <%if(newFlag.equals("new")){ %>
     		<display:column title="ChgCode"><html:text property="chargeCddesc" value="<%=chargecodedesc%>" 
     			readonly ="true" size="3" style="font-style: italic"/></display:column>
     		<display:column title="ChgCodeDesc"><html:text property="chargeCodes" value="<%=chargecode%>" 
     			readonly ="true" size="8" style="font-style: italic"/></display:column>
     		<display:column title="CostType"><html:text property="costType"  maxlength="30" size="7" 
     			value="<%=costType%>" readonly ="true" style="font-style: italic"/></display:column>
       <%}else{ %>
     		<display:column title="ChgCode"><html:text property="chargeCddesc" value="<%=chargecodedesc%>" readonly ="true" size="3"/></display:column>
     		<display:column title="ChgCodeDesc"><html:text property="chargeCodes" value="<%=chargecode%>" readonly ="true" size="8"/></display:column>
    	    <display:column title="CostType"><html:text property="costType"  maxlength="30" size="7" value="<%=costType%>" readonly ="true"/></display:column>
       <%}%>
     		<display:column title="Currency"><html:text property="currency"  maxlength="30" size="3" value="<%=currency%>" readonly ="true" /></display:column>
     		<display:column title="Cost">   <html:text property="sellRate" maxlength="15" size="6" value="<%=sellRate%>"/> </display:column>
     		<display:column title="New Rate">   <html:text property="newRate" maxlength="15" size="6" value="<%=newRate%>" readonly="true"/> </display:column>
    	    <display:column title="Effective Date">   <html:text property="effectiveDate" maxlength="15" size="7" value="<%=effectiveDate%>" readonly="true"/> </display:column>
       <%if(newFlag.equals("new")){ %>
     		<display:column title="Vendor Name" style="width:200px;" >
      			<input type="text"  size="30" name="accountname<%=l%>" id="accountname<%=l%>" 
      			onkeydown="getAccountName(this.value)" value="<%=accountName%>" style="font-style: italic"/>
      		</display:column>
      		<display:column title="Vendor No" >
      				<input type="text"  size="15" name="accountno<%=l%>" id="accountno<%=l%>" value="<%=accoutnNo%>" 
      				onkeydown="getAccountName(this.value)" style="font-style: italic"/>
     		</display:column>
     <%}else{ %>
      		 <display:column title="Vendor Name" style="width:200px;" >
      			<input type="text"  size="30" name="accountname<%=l%>" id="accountname<%=l%>" 
      				onkeydown="getAccountName(this.value)" value="<%=accountName%>"/>
      		</display:column>
      		<display:column title="Vendor No" >
      			<input type="text"  size="15" name="accountno<%=l%>" id="accountno<%=l%>" value="<%=accoutnNo%>" onkeydown="getAccountName(this.value)" />
     		</display:column>
     <%} %>
     <%if(chargecodedesc.equals("DRAY")||chargecodedesc.equals("INTMDL")||chargecodedesc.equals("INSURE")||chargecodedesc.equals("005")){%>
    
     <%}else{ %>
     		 <display:column title="Actions">
					<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
					<img src="<%=path%>/img/icons/delete.gif" onclick="deleteCharges('<%=id%>')"/></span>
			 </display:column>	
	 <%} %>
	<%l++; %>
     		<display:column title="" style="visibility:hidden">
    			 <html:text property="chargeAmount" value="<%=amt%>" style="visibility:hidden" size="1"/> 
    		</display:column>
      		<display:column title="" style="visibility:hidden">
      			<html:text property="chargeMarkUp" value="<%=markUp1%>" style="visibility:hidden" size="1"/>
      		</display:column>
      		
        </display:table>
       </table>
    </div>
    <br /></td>
   </tr>
 </table><%--oo--%>
 
  <table width="100%"><%--pp--%>
     <tr>
         <td align="left" >
         	<div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:80%" pagesize="<%=pageSize%>">
            	<table width="100%" border="0" cellpadding="0" cellspacing="0">
            		<%
 						List perkglbsList=(List)request.getAttribute("perKgLbsList");
 						NumberFormat numformat1 = new DecimalFormat("##,###,##0.00");

						  //String amt[]=new String[50];
						 if((perkglbsList!=null && perkglbsList.size()>0))
						 {
						 int k=0;
						
						 String chargecode="";
						 String costType="";
						 BookingfclUnits cb=new BookingfclUnits();
						
						 List amtindex=null;
						 
						 String activeAmt="";
						 String ftf="";
						 String ctc="";
						 String minimum="";
						 String chargeCd="";
						 String currecny1="";
						 String effectiveDate="";
						 String otherNewRate="";
						 String newFlag="";
						 int ai=0;
        		   %>   
          <display:table name="<%=perkglbsList%>"  class="displaytagstyle" id="lclcoloadratestable" 
                    sort="list" style="width:100%"> 
      				<%
					        cb=(BookingfclUnits)perkglbsList.get(k);
					        chargeCd=cb.getChargeCodeDesc();
					       chargecode=cb.getChgCode();
					       costType=cb.getCostType();
					       if(cb.getNewFlag()!=null){
					       newFlag=cb.getNewFlag();
					       }else{
					       newFlag="";
					       }
					   		if(cb.getAmount()!=null)
					   		{
					   		activeAmt=numformat1.format(cb.getAmount());
					   		}
					   		
					    
					     if(cb.getMinimum()!=null)
					     {
					     minimum=numformat1.format(cb.getMinimum());
					    
					     }
					     if(cb.getCurrency()!=null)
					     {
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
        
       <%if(newFlag.equals("new")){ %>
 			<display:column style="color: red;">*</display:column>
 	   <%}else{ %>
       <%} %>
       <%if(newFlag.equals("new")){ %>
    		<display:column title="ChargeCode"><html:text property="perChargeCode" value="<%=chargeCd%>" 
    			readonly="true" size="3" style="font-style: italic"/></display:column>
      		<display:column title="ChargeCodeDesc"><html:text property="perChargeCodeDesc" value="<%=chargecode%>" 
      			readonly="true" size="8" style="font-style: italic"/></display:column>
     	    <display:column title="CostType"><html:text property="perCostType"  maxlength="30" size="7" 
      			value="<%=costType%>" readonly="true" style="font-style: italic"/></display:column>
 	  <%}else{ %>    
 			<display:column title="ChargeCode"><html:text property="perChargeCode" value="<%=chargeCd%>" readonly="true" size="3"/></display:column>
      		<display:column title="ChargeCodeDesc"><html:text property="perChargeCodeDesc" value="<%=chargecode%>" readonly="true" size="8"/></display:column>
      		<display:column title="CostType"><html:text property="perCostType"  maxlength="30" size="7" value="<%=costType%>" readonly="true"/></display:column>
 	 <%} %>
      		<display:column title="Rate">   <html:text property="perActiveAmt" maxlength="15" size="6" value="<%=activeAmt%>" readonly="true"/> </display:column>
      		<display:column title="Minimum">   <html:text property="perMinimum" maxlength="15" size="6" value="<%=minimum%>" readonly="true"/> </display:column>
     		 <display:column title="Currency"><html:text property="percurrecny"  maxlength="30" size="3" value="<%=currecny1%>" readonly="true"/></display:column>
    <%k++; %>
     </display:table>
  <%}%>
   </table> 
  </div>
 </td>
 </tr>
 </table><%--pp--%>  
   
   
   <table width="100%"><%--qq--%>
       <tr>
          <td align="left" >
              <div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:100%">
                 <table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <%
 					     List otherChargesList=(List)request.getAttribute("otherChargesList");
 
						  //String amt[]=new String[50];
						  String chargeCd="";
						  String chargecode="";
						  String costType="";
						  String retail="0.00";
						  String currecny="";
						  String otherNewRate="";
						  String otherEffectiveDate="";
						  String othermarkUp="";
						  String otherSellRate="";
						  String otherAccountNo="";
						  String otherAccountName="";  
						  String newFlag="";
						  String id="";  
						 if((otherChargesList!=null && otherChargesList.size()>0))
						 { 
						    int i=0;
                    %>   
               <display:table name="<%=otherChargesList%>"  class="displaytagstyle" id="otherChargestable" sort="list" style="width:100%" pagesize="<%=pageSize%>"> 
    
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
    
   				 <%
   					 if((otherChargesList!=null && otherChargesList.size()>0)){
    					BookingfclUnits b=new BookingfclUnits();
						b=(BookingfclUnits)otherChargesList.get(i);
						id=String.valueOf(b.getId());
						chargeCd=b.getChargeCodeDesc();
						chargecode=b.getChgCode();
						costType=b.getCostType();
						if(b.getNewFlag()!=null){
						newFlag=b.getNewFlag();
						}else{
						newFlag="";
						}
						if(b.getAmount()!=null){
						retail=numformat1.format(b.getAmount());
						}
						currecny=b.getCurrency();
						if(b.getMarkUp()!=null)
						{
						othermarkUp=numformat1.format(b.getMarkUp());
						}
						if(b.getSellRate()!=null)
						{
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
						otherEffectiveDate=sdf.format(b.getEfectiveDate());
						}else{
						otherEffectiveDate="";
						}
					}
    			 %>
      		<%if(newFlag.equals("new")){ %>
 				 <display:column style="color: red;">*</display:column>
		    <%}else{ %>
 
            <%} %>
    			 <display:column style="visibility:hidden" title=""><%=""%></display:column>
      		 <%if(newFlag.equals("new")){ %>
     			 <display:column title="ChgCode"><html:text property="otherchargeCddesc" value="<%=chargeCd%>" 
     					readonly="true" size="3" style="font-style: italic"/></display:column>
     			 <display:column title="ChgCodeDesc"><html:text property="chargeCd" value="<%=chargecode%>" 
     					readonly="true" size="8" style="font-style: italic"/></display:column>
     			 <display:column title="CostType"><html:text property="costType1"  maxlength="30" size="7" 
    					 value="<%=costType%>" readonly="true" style="font-style: italic"/></display:column>
    		<%}else{ %>
    			 <display:column title="ChgCode"><html:text property="otherchargeCddesc" value="<%=chargeCd%>" readonly="true" size="3"/></display:column>
     			 <display:column title="ChgCodeDesc"><html:text property="chargeCd" value="<%=chargecode%>" readonly="true" size="8"/></display:column>
     			 <display:column title="CostType"><html:text property="costType1"  maxlength="30" size="7" value="<%=costType%>" readonly="true"/></display:column>
    		<%} %>
     			<display:column title="Currency"><html:text property="othercurrecny"  maxlength="30" size="3" value="<%=currecny%>" readonly="true"/></display:column>
     			<display:column title="Cost"><html:text property="otherSellRate"  maxlength="30" size="6" value="<%=otherSellRate%>" /></display:column>
     			<display:column title="New Rate"><html:text property="otherNewRate"  maxlength="30" size="6" value="<%=otherNewRate%>" readonly="true"/></display:column>
     			<display:column title="Effective Date"><html:text property="otherEffectiveDate"  maxlength="30" size="7" value="<%=otherEffectiveDate%>" readonly="true"/></display:column>
      			<display:column title="Vendor Name" style="width:200px;">
      				<input type="text" size="30" name="otheraccountname<%=i%>" id="otheraccountname<%=i%>" onkeydown="getAccountName(this.value)" value="<%=otherAccountName%>"/>
     			</display:column>
     			<display:column title="Vendor No">
     		 		<input type="text"  size="15" name="otheraccountno<%=i%>" id="otheraccountno<%=i%>" value="<%=otherAccountNo%>" onkeydown="getAccountName(this.value)" />
     			</display:column>
      			<display:column title="Actions">
					<span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();">
					<img src="<%=path%>/img/icons/delete.gif" onclick="deleteCharges1('<%=id%>')" id="deleteimg" /></span>
				</display:column>
			    <display:column title="" style="visibility:hidden">
	    			<html:text property="retail" maxlength="15" size="1" value="<%=retail%>" style="visibility:hidden"/> </display:column>
    		    <display:column title="" style="visibility:hidden">
     				<html:text property="othermarkUp"  maxlength="30" size="1" value="<%=othermarkUp%>" style="visibility:hidden"/></display:column>	
    		<%i++; %>
 		</display:table>
     <%} %>
   </table>
 </div>
 </td>
 </tr>
 </table><%--qq--%>
 
 
 <table><%--last--%>
  <tr>
    <td height="102">
       <table width="100%" border="0" cellpadding="0" cellspacing="0">
         <c:if test="${bookingValues.totalCharges!='0.00'}">
             <tr>
        		<td width="5%">&nbsp;<br /></td>
        		<td width="20%" class="textlabels">Total Charges(USD)<br /></td>
        		 <td width="15%">
        		 <br /></td>
        		<td align="center" width="20%"><input type="button" class="buttonStyleNew" value="Recalc" onclick="recalcfunction()"/> <br /></td>
       			<td width="10%" class="textlabels">&nbsp;<br /></td>
    			<td width="10%" class="textlabels"><br /></td>
        		<td width="20%">&nbsp;<br /></td>
            </tr>
         </c:if>
         <c:if test="${bookingValues.baht!='0.00'}">
          <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(BAHT)<br /></td>
        		 
        		<td>
        		
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
          </tr>
       </c:if>
       <c:if test="${bookingValues.bdt!='0.00'}">
          <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(BDT)<br /></td>
        		 <td><br /></td>
        		<td width="11">&nbsp;<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
          </tr>
       </c:if>
       <c:if test="${bookingValues.cyp!='0.00'}">
           <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(CYP)<br /></td>
        		   <td>
        		   <br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
          </tr>
	  </c:if>
      <c:if test="${bookingValues.eur!='0.00'}">
          <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(EUR)<br /></td>
        		 <td>
        		  
        		 <br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
          </tr>
       </c:if>
       <c:if test="${bookingValues.hkd!='0.00'}">
            <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(HKD)<br /></td>
        		 <td>
        		 
        		 <br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
           </tr>
      </c:if>
      <c:if test="${bookingValues.lkr!='0.00'}">
            <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(LKR)<br /></td>
        		 <td>
        		 
        		 <br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
            </tr>
     </c:if>
     <c:if test="${bookingValues.nt!='0.00'}">
             <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(NT)<br /></td>
        		<td>
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
           </tr>
     </c:if>
     <c:if test="${bookingValues.prs!='0.00'}">
             <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(PRS)<br /></td>
        		 <td>
        		 <br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
           </tr>
    </c:if>
    <c:if test="${bookingValues.rmb!='0.00'}">
           <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(RMB)<br /></td>
        		<td>
        		 
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
           </tr>
     </c:if>
     <c:if test="${bookingValues.won!='0.00'}">
           <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(WON)<br /></td>
        		<td>
        		 
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
           </tr>
     </c:if>
     <c:if test="${bookingValues.yen!='0.00'}">
            <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(YEN)<br /></td>
        		 <td>
        		 
        		 <br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
            </tr>
     </c:if>
     <c:if test="${bookingValues.myr!='0.00'}">
            <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(MYR)<br /></td>
        		<td> 
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
            </tr>
    </c:if>
    <c:if test="${bookingValues.nht!='0.00'}">
        	<tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(NHT)<br /></td>
        		<td>
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
       	 </tr>
    </c:if>
    <c:if test="${bookingValues.pkr!='0.00'}">
          <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(PKR)<br /></td>
        		<td>
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
          </tr>
   </c:if>
   <c:if test="${bookingValues.rm!='0.00'}">
        	<tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(RM)<br /></td>
        		<td>
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
       		 </tr>
   </c:if>
   <c:if test="${bookingValues.spo!='0.00'}">
    	    <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(SPO)<br /></td>
        		<td>
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
        </tr>
   </c:if>
   <c:if test="${bookingValues.vnd!='0.00'}">
       	 <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(VND)<br /></td>
        		<td>
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels"><br /></td>
        		<td width="149">&nbsp;<br /></td>
       	 </tr>
    </c:if>
    <c:if test="${bookingValues.inr!='0.00'}">
     	   <tr>
        		<td width="11">&nbsp;<br /></td>
        		<td width="320" class="textlabels">Total Charges(INR)<br /></td>
        		<td>
        		<br /></td>
       			<td width="112" class="textlabels">&nbsp;<br /></td>
    			<td width="160" class="textlabels">&gt;<br /></td>
        		<td width="149">&nbsp;</td>
       	 </tr>
    </c:if>
    
       </table>
     </td>
   </tr> 
 </table><%--last--%>

     </td>   
   </tr><%--3row ends--%>
   
</table><%--maintable--%>


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
<html:hidden property="insuranceAmount"/>
<html:hidden property="blNumber"/>
<html:hidden property="selectedCheck"/>
<html:hidden property="unitselected"/>
<html:hidden property="ssline"/>
<html:hidden property="selectedOrigin"/>
<html:hidden property="selectedDestination"/>
<html:hidden property="selectedComCode"/>
<html:hidden property="quoteBy" value="${bookingValues.quoteBy}"/>
<html:hidden property="fileNo" value="${bookingValues.fileNo}"/>
<html:hidden property="blBy" value="${bookingValues.blBy}"/>
<html:hidden property="bookingId" value="<%=bookingId%>"/>
<html:hidden property="quoteId" value="<%=quoteId%>"/>

	<c:if test="${not empty bookingValues.quoteDate}">
			<fmt:formatDate pattern="MM/dd/yyyy hh:mm a"  value= "${bookingValues.quoteDate}" var="date1"/>
			<html:hidden property="quoteDate" value="${date1}"/>
	</c:if>
	<c:if test="${not empty bookingValues.blDate}">
		<fmt:formatDate pattern="MM/dd/yyyy hh:mm a"  value= "${bookingValues.blDate}" var="date2"/>
		<html:hidden property="blDate" value="${date2}"/>
	</c:if>
	<c:choose>
  		 <c:when  test="${bookingValues.username!=null}">
		  	<html:hidden property="userName" value="${bookingValues.username}"/>
  		 </c:when>
  		 <c:otherwise>
				<html:hidden property="userName" value="<%=bookingBy%>"/>
		 </c:otherwise>
  	</c:choose>
 	<c:choose>
  		 <c:when  test="${bookingValues.bookingDate!=null}">
  		    <fmt:formatDate pattern="MM/dd/yyyy hh:mm a"  value= "${bookingValues.bookingDate}" var="date"/>
            <html:hidden property="bookingDate" value="${date}"/>
        </c:when>
        <c:otherwise>
             <html:hidden property="bookingDate" value="<%=bookingDate%>"/>
  		 </c:otherwise>
   </c:choose>
   
</html:form>

	<%if(view.equals("3") || blFlag.equals("on")){ %>
		<script>parent.parent.makeFormBorderless(document.getElementById("editbook"))</script>
		<script>makeFormBorderless(document.getElementById("editbook"))</script>
	<%} %>
		<script>load('<%=shipCheck%>','<%=forwCheck%>','<%=consCheck%>');</script>

	
			<script type="text/javascript">loadData();</script>
			<c:if test="${WorkOrder!=null}">
 				<script type="text/javascript">
 					 GB_show('WorkOrderReport','<%=path%>/servlet/PdfServlet?fileName=${WorkOrder}',400,680);
 				</script>
		    </c:if>
 			<c:if test="${Reference!=null}">
 				<script type="text/javascript">
  					GB_show('Reference Report','<%=path%>/servlet/PdfServlet?fileName=${Reference}',400,680);
 				</script>
			</c:if>
 			<c:if test="${CostSheet!=null}">
 				<script type="text/javascript">
  					GB_show('CostSheet Report','<%=path%>/servlet/PdfServlet?fileName=${CostSheet}',400,680);
 				</script>
			</c:if>
			<c:if test="${fileName!=null}">
 				<script type="text/javascript">
  					GB_show('BookingConfirmation Report','<%=path%>/servlet/PdfServlet?fileName=${fileName}',400,680);
				</script>
		    </c:if>

</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
