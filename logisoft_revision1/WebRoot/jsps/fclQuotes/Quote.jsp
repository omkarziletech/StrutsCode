<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil,com.gp.cvst.logisoft.domain.Charges,com.gp.cong.logisoft.bc.notes.NotesConstants,com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO,
com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO,com.gp.cong.logisoft.domain.User,com.gp.cvst.logisoft.beans.TransactionBean,com.gp.cvst.logisoft.struts.form.QuotesForm,
com.gp.cong.logisoft.hibernate.dao.PortsDAO"%>
<%@ page import="java.util.*,java.text.*,com.gp.cong.logisoft.util.CommonFunctions "%>
<jsp:directive.page import="jxl.write.DateTime"/>
<%@page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="un"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@include file="../includes/jspVariables.jsp"  %>
  <bean:define id="defaultAgent" type="String">
    <bean:message key="defaultAgent"/>
  </bean:define>
<%
boolean importFlag=false;
boolean hasUserLevelAccess = roleDuty.isShowDetailedCharges();
String path = request.getContextPath();
Date date = new Date();
DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
String todaysDate = format.format(date);
User userid=null;
String userName="";
String companyCode = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
request.setAttribute("companyCode", companyCode);
QuotesForm quoteform = new  QuotesForm();
 if(session.getAttribute("loginuser")!=null){
     userid=(User)session.getAttribute("loginuser");
     userName = userid.getLoginName();
 }
String editPath=path+"/quotes.do";
String mandatoryFieldForQuotes = "";
NumberFormat numformat = new DecimalFormat("##,###,##0.00");
DBUtil dbUtil = new DBUtil();
GenericCodeDAO gdDAO= new GenericCodeDAO();
PortsDAO portsDAO = new PortsDAO();
String msg="";
if(request.getAttribute("msg")!=null){
msg=(String)request.getAttribute("msg");

}
String message="";
if(request.getAttribute("message")!=null){
message=(String)request.getAttribute("message");
}
String quotationNo="";
if(request.getAttribute("quotationNo")!=null){
quotationNo=(String)request.getAttribute("quotationNo");
}
List quotationList=null;
if(request.getAttribute("quotationList")!=null){
quotationList=(List)request.getAttribute("quotationList");
}
List rateslist=new ArrayList();
if(request.getAttribute("rateslist")!=null){
 rateslist=(List)request.getAttribute("rateslist");
}

String quoteBy="";
User user1=null;
if(session.getAttribute("loginuser")!=null){
	  user1=(User)session.getAttribute("loginuser");
	  quoteBy = (user1.getLoginName()).toUpperCase();
}
String noOfDays="30";
SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
SimpleDateFormat sdf1=new SimpleDateFormat("dd-MMM-yyyy HH:mm");
String quoteDate= sdf1.format(new Date());
request.setAttribute("insuranceAllowed",portsDAO.getInsuranceAllowed());
request.setAttribute("spclList",dbUtil.getUnitListForFCLTest1(new Integer(41),"yes","Select Special Equipments" ));
String buttonValue="";
if(request.getAttribute("buttonValue")!=null){
buttonValue=(String)request.getAttribute("buttonValue");
}
  String sslName = "";
if(null != request.getAttribute("quotationForm")){
	QuotesForm form = 	(QuotesForm)request.getAttribute("quotationForm");
	sslName = (null != form.getSslDescription())?form.getSslDescription():"";
	sslName = sslName.replaceAll("'","\\\\'").replaceAll("\"","&quot;");
        if(form.getRampCheck()!= null && form.getRampCheck().equalsIgnoreCase("on")){
            request.setAttribute("typeOfMoveList", dbUtil.getGenericFCLforTypeOfMove(new Integer(48),"yes","yes"));
        }else{
            request.setAttribute("typeOfMoveList", dbUtil.getGenericFCLforTypeOfMovebooking(new Integer(48),"yes","yes"));
        }
            importFlag=form.isImportFlag();
}else{
        importFlag=(null!=session.getAttribute(ImportBc.sessionName))?true:false;
}
    String star=(importFlag)?"":"*";
   if(importFlag){
        mandatoryFieldForQuotes = "Mandatory Fields Needed<br>1)Client<BR>2)Destination<br>3)Origin"
        + "<br>4)Rates button<BR>5)Type of Move<br>6)Goods Description";
    }else{
        mandatoryFieldForQuotes = "Mandatory Fields Needed<br>1)Client<BR>2)Destination<br>3)Origin"
        + "<br>4)Rates button<BR>5)Type of Move<br>6)ERT<br>7)Goods Description";
    }
   request.setAttribute("importFlag", importFlag);

%>
<html>
<head>
<%@include file="../includes/resources.jsp" %>
<title>Quotation</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<%@include file="../includes/baseResources.jsp" %>
<%@include file="../fragment/formSerialize.jspf"  %>
<script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
<script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
<script language="javascript" src="<%=path%>/js/quote.js"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
<script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
<script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
<script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
<script language="javascript" src="<%=path%>/js/common.js"></script>
<script language="javascript" src="<%=path%>/js/rates.js"></script>
	<script type="text/javascript">
            start = function(){
            loadFunction();
            displayToolTipDiv();
            serializeForm();
	    getCreditStatus();
            tabMoveAfterDeleteRates('${importFlag}');
            inlandVal();
            if (document.getElementById('newClient').checked) {        
        document.getElementById('newerClient').style.display = "block";
        document.getElementById('existingClient').style.display = "none";
        onCustomerBlur("newClient");
    }
        }
    window.onload=start;
</script>

<script language="javascript" type="text/javascript">
var disableMessage = "This Customer is Disabled";
var insuranceAllowed="${insuranceAllowed}";


function popupAddRates(windowname,val){
	var haz="";
	if(document.QuotesForm.hazmat[0].checked){
            haz="Y";
	}else{
            haz="N";
	}
        var fileType ="";
         var importFlag = ${importFlag}
         if (importFlag === true) {
           fileType = document.getElementById("fileType").value;
        }else{
           fileType = null;
        }
        if(document.QuotesForm.commcode.value==""){
            alertNew("PLEASE SELECT COMMODITY CODE");
            jQuery("#commcode").css("border-color","red");
            //document.QuotesForm.commcode.focus();
            return;
        }
      if((trim(document.QuotesForm.portofDischarge.value) == "" || trim(document.QuotesForm.isTerminal.value)=="")
          && document.getElementById("bulletRates") && document.getElementById('bulletRates').checked){
                    alertNew("Please Enter BOTH Origin and Destination when looking up Bullet Rates");
                    jQuery("#portofDischarge").css("border-color","red");
                    jQuery("#isTerminal").css("border-color","red");
                    return;
            }


        if((trim(document.QuotesForm.portofDischarge.value) == "" && trim(document.QuotesForm.isTerminal.value)=="")
            ||trim(document.QuotesForm.customerName.value)==""){
            if(document.QuotesForm.newClient.checked){
                if( trim(document.QuotesForm.customerName1.value)==""){
                    alertNew("PLEASE ENTER CLIENT");
                    jQuery("#customerName1").css("border-color","red");
                    // document.QuotesForm.customerName1.focus();
                    return;
                }else if(trim(document.QuotesForm.portofDischarge.value) == "" && trim(document.QuotesForm.isTerminal.value)==""){
                    alertNew("PLEASE SELECT DESTINATION PORT OR ORIGIN");
                    jQuery("#portofDischarge").css("border-color","red");
                    jQuery("#isTerminal").css("border-color","red");
                    return;
                }
            }else if(trim(document.QuotesForm.customerName.value)==""){
                alertNew("PLEASE ENTER CLIENT");
                jQuery("#customerName").css("border-color","red");
                // document.QuotesForm.customerName.focus();
                return;
            }else{
                alertNew("PLEASE SELECT DESTINATION PORT OR ORIGIN");
                jQuery("#portofDischarge").css("border-color","red");
                jQuery("#isTerminal").css("border-color","red");
                return;
            }

        }
        if(document.QuotesForm.commcode.value!=""){
            jQuery.ajaxx({
                data: {
                    className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName : "validateCommodityCode",
                    param1 : document.QuotesForm.commcode.value
                },
                success: function (data) {
                    if(data === "true"){
                        alertNew("Please Change Commodity Code for FCL");
                    }else{
                        //get all the origin for given destination (or) get all destination for given origin
                        if(!document.getElementById('nonRated').checked && (trim(document.QuotesForm.portofDischarge.value) == "" || trim(document.QuotesForm.isTerminal.value)=="")){
                            var searchBy = "city";
                            if(trim(document.QuotesForm.portofDischarge.value) != "") {
                                if(!document.getElementById('destinationCity').checked){
                                    searchBy="country";
                                }
                                var doorOrigin = document.getElementById("doorOrigin");
                                jQuery.ajaxx({
                                    data: {
                                        className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                        methodName : "getOriginsForDestination",
                                        forward : "/jsps/fclQuotes/OriginAndDestination.jsp",
                                        param1 : document.QuotesForm.isTerminal.value,
                                        param2 : document.QuotesForm.portofDischarge.value,
                                        param3 : searchBy,
                                        param4 : document.QuotesForm.zip.value,
                                        param5 : document.QuotesForm.commcode.value
                                    },
                                    preloading : true,
                                    success: function (data) {
                                        if(jQuery.trim(data) === ""){
                                            alert("No Data Found!");
                                        }else if(data.length > 100) {
                                            showOriginDestinationList(data);
                                            if(null != doorOrigin && doorOrigin.value != '') {
                                                document.getElementById("doorOriginDisplay").innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;<b>'+doorOrigin.value+'</b>';
                                                document.getElementById("doorOriginDisplay").style.display = "block";
                                            }else {
                                                document.getElementById("top10Span").style.display="none";
                                            }
                                        }else {
                                            document.QuotesForm.isTerminal.value = data[0];
                                            document.QuotesForm.isTerminal_check.value = data[0];
                                            openGetRates(haz, val, fileType);
                                        }
                                    }
                                });
                            }else {
                                if(!document.getElementById('originCountry').checked){
                                    searchBy="country";
                                }
                                jQuery.ajaxx({
                                    data: {
                                        className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                        methodName : "getDestinationsForOrigin",
                                        forward : "/jsps/fclQuotes/OriginAndDestination.jsp",
                                        param1 : document.QuotesForm.portofDischarge.value,
                                        param2 : document.QuotesForm.isTerminal.value,
                                        param3 : searchBy,
                                        param4 : document.QuotesForm.zip.value,
                                        param5 : document.QuotesForm.commcode.value
                                    },
                                    preloading : true,
                                    success: function (data) {
                                        if(data.length > 100) {
                                            showOriginDestinationList(data);
                                            var doorDestination = document.getElementById("doorDestination");
                                            if(null != doorDestination && doorDestination.value != '') {
                                                document.getElementById("doorOriginDisplay").innerHTML = '&nbsp;&nbsp;&nbsp;&nbsp;<b>'+doorDestination.value+'</b>';
                                                document.getElementById("doorOriginDisplay").style.display = "block";
                                            }
                                        }else {
                                            document.QuotesForm.portofDischarge.value = jQuery.trim(data);
                                            document.QuotesForm.portofDischarge_check.value = jQuery.trim(data);
                                            openGetRates(haz, val, fileType);
                                        }
                                    }
                                });
                            }
                        }else if(document.getElementById("showAllCity") &&  document.getElementById("showAllCity").checked) {
                            //get list all the country cities
                            jQuery.ajaxx({
                                data: {
                                    className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                    methodName : "getAllCountryPorts",
                                    param1 : document.QuotesForm.portofDischarge.value
                                },
                                preloading : true,
                                success: function (data) {
                                    var portOfDisch = document.QuotesForm.portofDischarge.value;
                                    var doorDestination = document.getElementById("doorDestination");
                                    var country = portOfDisch.split("/")[1];
                                    var origin = null;
                                    var arry = null;
                                    var passedValue = null;
                                    if(null != data && data.length > 1){
                                        passedValue = data.split('||');
                                        var allCity = new Array();
                                        for(i=0;i<passedValue.length;i++) {
                                            arry = passedValue[i].split('==');
                                            if(null == origin) {
                                                origin = arry[0];
                                                allCity[i] = arry[1];
                                            }else {
                                                origin = origin + ',' +arry[0];
                                                allCity[i] = arry[1];
                                            }
                                        }
                                        if(null != doorDestination && doorDestination.value != '') {
                                            getDistancefromFDAndDestination(allCity,doorDestination.value,country, origin, haz,passedValue, document.QuotesForm);
                                        }
                                        else {
                                            window.parent.hideProgressBar();
                                            var destinationPort = document.QuotesForm.portofDischarge.value;
                                            var originPort = "";
                                            if(trim(document.QuotesForm.isTerminal.value)!=""){
                                                originPort = document.QuotesForm.isTerminal.value;
                                            }else{
                                                originPort = document.QuotesForm.doorOrigin.value;
                                            }
                                            var url = '<%=path%>/rateGrid.do?action=Origin&origin='+document.QuotesForm.isTerminal.value;
                                            url+="&destination="+origin+"&doorOrigin="+doorDestination.value+"&commodity="+document.QuotesForm.commcode.value;
                                            url+='&hazardous='+haz+"&destinationPort="+destinationPort+"&originPort="+originPort;
                                            url+="&bulletRates="+jQuery("#bulletRates").is(":checked")+"&originalCommodity="+document.QuotesForm.commcode.value+"&fileType="+fileType;
                                            var height = jQuery(window).height() - 40;
                                            var width = jQuery(window).width() - 100;
                                            GB_show('FCL Rates Comparison Grid',url,height,width);
                                        }
                                    }else{
                                        openGetRates(haz, val, fileType);
                                    }
                                }
                            });
                        }else {
                            var destinationPort = document.QuotesForm.portofDischarge.value;
                            var originPort = "";
                            if(trim(document.QuotesForm.isTerminal.value)!=""){
                                originPort = document.QuotesForm.isTerminal.value;
                            }else{
                                originPort = document.QuotesForm.doorOrigin.value;
                            }
                            if(document.getElementById("bulletRates") && jQuery("#bulletRates").is(":checked")){
                                openGetRates(haz, val, fileType);
                            }else{
                                //get list all the country cities
                                jQuery.ajaxx({
                                    dataType : "json",
                                    data: {
                                        className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                        methodName : "getAllSynonymousCity",
                                        param1 : document.QuotesForm.portofDischarge.value,
                                        param2 : document.QuotesForm.isTerminal.value,
                                        dataType : "json"
                                    },
                                    preloading : true,
                                    success: function (data) {
                                        if(jQuery.trim(data) !== ""){
                                            if(null != data[0] && data[0] != '' && data[0].split(",").length > 1){
                                                if(null != data[1] && data[1] != '' && data[1].split(",").length > 1){
                                                    var url = '<%=path%>/rateGrid.do?action=Origin&origin='+data[1];
                                                    url+="&destination="+data[0]+"&originName="+document.QuotesForm.isTerminal.value;
                                                    url+="&doorOrigin="+document.getElementById('doorOrigin').value;
                                                    url+="&commodity="+document.QuotesForm.commcode.value+'&hazardous='+haz;
                                                    url+="&destinationPort="+destinationPort+"&originPort="+originPort;
                                                    url+="&bulletRates="+jQuery("#bulletRates").is(":checked")+"&originalCommodity="+document.QuotesForm.commcode.value+"&fileType="+fileType;
                                                    var height = jQuery(window).height() - 40;
                                                    var width = jQuery(window).width() - 100;
                                                    GB_show('FCL Rates Comparison Grid',url,height,width);
                                                }else{
                                                    var url = '<%=path%>/rateGrid.do?action=Origin&origin='+document.QuotesForm.isTerminal.value;
                                                    url+="&destination="+data[0]+"&doorOrigin="+document.getElementById('doorOrigin').value;
                                                    url+="&commodity="+document.QuotesForm.commcode.value+'&hazardous='+haz;
                                                    url+="&destinationPort="+destinationPort+"&originPort="+originPort;
                                                    url+="&bulletRates="+jQuery("#bulletRates").is(":checked")+"&originalCommodity="+document.QuotesForm.commcode.value+"&fileType="+fileType;
                                                    var height = jQuery(window).height() - 40;
                                                    var width = jQuery(window).width() - 100;
                                                    GB_show('FCL Rates Comparison Grid',url,height,width);
                                                }
                                            }else if(null != data[1] && data[0] != '' && data[1].split(",").length > 1){
                                                var url = '<%=path%>/rateGrid.do?action=Origin&origin='+data[1];
                                                url+="&destination="+document.QuotesForm.portofDischarge.value;
                                                url+="&originName="+document.QuotesForm.isTerminal.value;
                                                url+="&doorOrigin="+document.getElementById('doorOrigin').value;
                                                url+="&commodity="+document.QuotesForm.commcode.value+'&hazardous='+haz;
                                                url+="&destinationPort="+destinationPort+"&originPort="+originPort;
                                                url+="&bulletRates="+jQuery("#bulletRates").is(":checked")+"&originalCommodity="+document.QuotesForm.commcode.value+"&fileType="+fileType;
                                                var height = jQuery(window).height() - 40;
                                                var width = jQuery(window).width() - 100;
                                                GB_show('FCL Rates Comparison Grid',url,height,width);
                                            }else{
                                                openGetRates(haz, val, fileType);
                                            }
                                        }else{
                                            openGetRates(haz, val, fileType);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }
function openGetRates(haz, val, fileType) {
	var destinationPort = document.QuotesForm.portofDischarge.value;
	var originPort = "";
	if(trim(document.QuotesForm.isTerminal.value)!=""){
	     originPort = document.QuotesForm.isTerminal.value;
	}else{
	     originPort = document.QuotesForm.doorOrigin.value;
	}
 	var url = '<%=path%>/rateGrid.do?action=getrates&origin='+document.QuotesForm.isTerminal.value;
         url+="&destination="+document.QuotesForm.portofDischarge.value+"&commodity="+document.QuotesForm.commcode.value;
         url+="&noOfDays="+document.QuotesForm.noOfDays.value+"&hazardous="+haz+"&fileNo="+val;
         url+="&doorOrigin="+document.getElementById('doorOrigin').value+"&zip="+document.QuotesForm.zip.value;
         url+="&destinationPort="+destinationPort+"&originPort="+originPort;
         url+="&bulletRates="+jQuery("#bulletRates").is(":checked")+"&originalCommodity="+document.QuotesForm.commcode.value+"&fileType="+fileType;
         var height = jQuery(window).height() - 40;
         var width = jQuery(window).width() - 100;
         GB_show('FCL Rates Comparison Grid',url,height,width);
}
function popupQuoteCharge(windowname,val){
	if (!window.focus)return true;
	var hazmat="";
	if(document.QuotesForm.hazmat[0].checked){
		hazmat='Y';
	}else{
		hazmat='N';
	}

	var spcleqpt="";
	if(document.QuotesForm.specialequipment[0].checked){
		spcleqpt='Y';
	}else{
		spcleqpt='N';
	}

	var breakBulk="";
	if(document.QuotesForm.breakBulk[0].checked){
		breakBulk='Y';
	}else{
		breakBulk='N';
	}
	document.getElementById('scroll').scrollIntoView(true);
	var ratedOption='';
	if(undefined != document.getElementById('nonRated') && true == document.getElementById('nonRated').checked){
		ratedOption='NonRated';
	}else{
		ratedOption='rated';
	}

GB_show('Input Rates Manually','<%=path%>/jsps/fclQuotes/QuoteCharge.jsp?hazmat='+hazmat+'&quoteNo='+document.QuotesForm.quoteId.value
+'&spcleqpmt='+spcleqpt+'&breakBulk='+breakBulk+'&button='+'quote'+'&fileNo='+val+'&ratedOption='+ratedOption,400,800);

}
function getDocCharge(){
     GB_show('get Input Rates','<%=path%>/jsps/fclQuotes/QuoteCharge.jsp?markup=docCharge&quoteNo='+document.QuotesForm.quoteId.value+'&ratedOption='+ratedOption,400,820);
}
function getInland(){
 var importFlag = ${importFlag}
  var val=checkIfRatesAreFromGetRates();
  if(val==='true'){
      if(document.getElementById('rampCheck').checked){
          alertNew("Intermodal Ramp Charges can be added only if rates are from getRates");
      }else{
          if (importFlag === true) {
          alertNew("Delivery Charges can be added only if rates are from getRates");
          }
          else{
          alertNew("Inland Charges can be added only if rates are from getRates");
          }
      }
     document.QuotesForm.inland[1].checked=true;
     return;
  }
  if(document.QuotesForm.zip.value!=""){
  		var ratedOption='';
		if(undefined != document.getElementById('nonRated') && true == document.getElementById('nonRated').checked){
			ratedOption='NonRated';
		}else{
			ratedOption='rated';
		}
		GB_show('get Input Rates','<%=path%>/jsps/fclQuotes/QuoteCharge.jsp?markup=inland&quoteNo='+document.QuotesForm.quoteId.value+'&ratedOption='+ratedOption,400,820);
  } else{
	  if(document.QuotesForm.zip.value==""){
              if(${importFlag} === 'true'){
		alertNew("Please Enter Zip Code In Order to select Delivery");
              }else{
                  alertNew("Please Enter Zip Code In Order to select Inland");
              }
              document.QuotesForm.inland[0].checked=false;
		document.QuotesForm.inland[1].checked=true;
		return;
	  }
  }
}
function getintermodel(){
    var val=checkIfRatesAreFromGetRates();
    if(val=='true'){
     	alertNew("Intermodal and Local Drayage Charges can be added only if rates are from getRates");
     	document.QuotesForm.intermodel[1].checked=true;
     	return;
  	}

	if(document.QuotesForm.zip.value!=""){
		var ratedOption='';
		if(undefined != document.getElementById('nonRated') && true == document.getElementById('nonRated').checked){
			ratedOption='NonRated';
		}else{
			ratedOption='rated';
		}
		GB_show('get Input Rates','<%=path%>/jsps/fclQuotes/QuoteCharge.jsp?markup=intermodal&quoteNo='+document.QuotesForm.quoteId.value+'&ratedOption='+ratedOption,400,820);
	} else{
		if(document.QuotesForm.zip.value==""){
			alertNew("Please Enter ZIp Code In Order to select Local Drayage Or InterModal");
			document.QuotesForm.intermodel[0].checked=false;
			document.QuotesForm.intermodel[1].checked=true;
			return;
		}
	}
}
function getHazmat(val){
	if(document.QuotesForm.hazmat[0].checked){
	 GB_show('Hazmat','<%=path%>/fCLHazMat.do?buttonValue=Quotation&name=Quote&number='+'${quotationForm.quoteId}'+'&fileNo='+val,
	  width="500",height="1200");
	}else{
	alertNew("Please Change Hazmat to Yes");
	return;
	}
        document.getElementById("commentRemark").value="Hazardous Cargo";
}
var cityNameForRemarks1;
var unlocationCode;
function populateAgentDojo1(data) {
	var defaultAgentCheck = "<%=defaultAgent%>";
	if(null!=data && null!=data.accountName && data.accountName!=undefined && data.accountName!="" && defaultAgentCheck=='ECI'){
		 document.getElementById("agent").value=data.accountName;
	}else{
		 document.getElementById("agent").value="";
	}
	if(null!=data && null!=data.accountno && data.accountno!=undefined && data.accountno!="" && defaultAgentCheck=='ECI'){
		 document.getElementById("agentNo").value=data.accountno;
	}else{
		 document.getElementById("agentNo").value="";
	}
	if(document.QuotesForm.routedAgentCheck.value == "yes"){
		 document.QuotesForm.routedbymsg.value="";
	}
	var city = document.QuotesForm.portofDischarge.value;
	var index = city.indexOf('/');
	var cityName = city.substring(0,index);
	remarksCityName1 = cityName;
	unlocationCode=city.substring(index+1,city.length);
        jQuery.ajaxx({
            data: {
                className : "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName : "getSpecialRemarks",
                param1 : cityName
            },
            success: function (data) {
                showSpecialRemarks(data);
            }
        });
}
var data1="";
function showSpecialRemarks(data) {
    data1 = jQuery.trim(data);
    jQuery.ajaxx({
        data: {
            className : "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
            methodName : "getCountryName",
            param1: unlocationCode
        },
        success: function (data) {
            getCountry(data);
        }
    });
}
function getCountry(data){
   remarksCityName1=remarksCityName1;
   if(data1 != "") {
     GB_show('','<%=path%>/jsps/fclQuotes/remarksFromDojo.jsp?remarks='+remarksCityName1,
  					width="200",height="600");
   }
}
var remarksCityName;
function showSpecialRemarks1(data) {
	 if(data != "") {
	    	var data1=trim(data);
	    		if(data1!=""){
	    			GB_show('Remarks','<%=path%>/jsps/fclQuotes/remarksFromDojo.jsp?remarks='+remarksCityName,
  					width="200",height="600");
  				}
	}
}
function getSslCode(){
	 var cust=document.QuotesForm.sslDescription.value;
	 var customer=cust.replace("&","amp;");
	 if(customer=='%'){
		customer = 'percent';
	}
 	GB_show('Client Info','<%=path%>/quoteCustomer.do?buttonValue=CarrierQuotation&clientName='+customer,
  		width="400",height="700");
}
function getIssTerm(){
	var issuingTerm=document.QuotesForm.issuingTerminal.value;
	var index=issuingTerm.indexOf("-");
	var newIssuingTerm= issuingTerm.substring(0,index)
	var issuTerm=newIssuingTerm.replace("&","amp;");
	if(issuTerm=='%'){
		 issuTerm = 'percent';
	}
 	GB_show('Issuing Termial Info','<%=path%>/issuingTerminal.do?buttonValue=Quotation&issuingTerminal='+issuTerm,
  			width="400",height="400");
}
function getClientInfo(){
	var cust=document.getElementById('customerName').value;
	var customer=cust.replace("&","amp;");
	if(customer=='%'){
		customer = 'percent';
	}
	GB_show('Client Info','<%=path%>/quoteCustomer.do?buttonValue=Quotation&clientName='+customer,
  			width="400",height="870");
}
function getCarrierContactInfo(){
	var cust=document.QuotesForm.sslDescription.value;
	var customerName=cust.replace("&","amp;");
	var customer=document.QuotesForm.sslcode.value;
	var carrierContact = document.QuotesForm.carrierContact.value;
	if(carrierContact=='%'){
		  carrierContact = 'percent';
	}
	GB_show('Carrier Contact','<%=path%>/customerAddress.do?buttonValue=CarrierQuotation&clientNo='+customer+ '&clientName='+customerName+'&contactName='+carrierContact,
  			width="400",height="700");
}
function getContactInfo(){
	var cust="";
	if(document.getElementById('customerName').value!= undefined){
   		cust=document.getElementById('customerName').value;
   	}else{
   		cust=document.getElementById('customerName1').value;
   	}
	var customerName=cust.replace("&","amp;");
	var customer=document.getElementById("clientNumber").value;
	var contactName = document.QuotesForm.contactName.value;
	if(contactName=='%'){
		  contactName = 'percent';
	}
	 GB_show('Contact','<%=path%>/customerAddress.do?buttonValue=Quotation&custNo='+customer+ '&custName='+customerName+'&contactName='+contactName,
  		width="400",height="1100");
}
function getRoutedByAgent(){
	var customerName=document.QuotesForm.routedbymsg.value;
	if(customerName=='%'){
		 customerName = 'percent';
	}
	GB_show('Routed By Agent','<%=path%>/quoteCustomer.do?buttonValue=RoutedQuotation&clientName='+customerName,
  		width="400",height="700");
}

function getRoutedByAgentFromPopup(val1,val2){
        jQuery.ajaxx({
            data: {
                className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName : "checkForDisable",
                param1 : val2
            },
            success: function (data) {
                if(jQuery.trim(data) !== ""){
                    alertNew(data);
		}else{
                    val1 = val1.replace(":","'");
                    document.QuotesForm.routedbymsg.value = val1;
		}
            }
        });
}

var customerTemp = "";
var clientNumberHidden = "";
function getAgent(){
	 var portOfDischarge=document.QuotesForm.portofDischarge.value;
	 var destination=document.QuotesForm.finalDestination.value
	 GB_show('Agent','<%=path%>/quoteAgent.do?buttonValue=Agent&portOfDischarge='+portOfDischarge+'&destination='+destination,
  		width="400",height="600");
}
function getInsurance(val){
	checkbox();
	if(document.QuotesForm.insurance[1].checked){
		alertNew("Please Select Insurance");
		return;
	}

	GB_show('Insurance','<%=path%>/jsps/fclQuotes/calculateInsurance.jsp?costOfGoods='+document.QuotesForm.costofgoods.value
	          ,width="200",height="300");
}
function newOriginEQ(){
var path="";
	if(document.getElementById('newOrigin').checked){
			   //var num = 100000;
			   document.getElementById('doorOrigin').value="";
			path="&from=5&isDojo=false&check=false";
                        Event.stopObserving("doorOrigin","blur");
	}else{
		       document.getElementById('doorOrigin').value="";
			path="&from=5&isDojo=false&check=true";
                        Event.observe("doorOrigin", "blur", function (event){
                        var element = Event.element(event);
                        if(element.value!=$("doorOrigin_check").value){
                            element.value = '';
                            $("doorOrigin_check").value = '';
                        }
                    }
                    );
	}
	 appendEncodeUrl(path);
}
function newRampCity1(){
		   if(document.getElementById('newRampCity').checked){
		      document.getElementById('rampCity').value="";
		      document.getElementById('rampLookUp').style.visibility="hidden";
		      dojo.widget.byId('autoRampCity').action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=88";
		   }else{
		      document.getElementById('rampCity').value="";
		      document.getElementById('rampLookUp').style.visibility="visible";
		      dojo.widget.byId('autoRampCity').action="<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=8";
		   }
}
function displayOrigins(){
	var selectBoxValue=document.QuotesForm.typeofMove.selectedIndex;
			//return GB_show('Orgin Search', '<%=path%>/searchquotation.do?buttonValue=searchPort&textName=isTerminal&agent=agent&from=terminal',250,600);
	GB_show('Orgin Search', '<%=path%>/searchquotation.do?buttonValue=searchPort&textName=isTerminal&agent=agent&from=terminal&setFocus=commcode&typeOfmove='+selectBoxValue+'&fclDestination='+document.QuotesForm.portofDischarge.value+'&NonRated='+document.getElementById('nonRated').checked,width="250",height="670");
}
function displayRampCity(){
	var selectBoxValue=document.QuotesForm.typeofMove.selectedIndex;
 	GB_show('RampCity Search', '<%=path%>/searchquotation.do?buttonValue=searchPort&setFocus=placeofReceipt&textName=rampCity&from=pol&typeOfmove='+selectBoxValue,width="250",height="600");
}
function goToRemarksLookUp(){
    document.getElementById("scroll").scrollIntoView(true);
	GB_show('Pre-defined Remarks','<%=path%>/remarksLookUp.do?buttonValue=Quotation&importFlag='+'${importFlag}',
  			 width="400",height="820");
}
function getGoogleMap(){
if(document.QuotesForm.zip.value==""){
alertNew("Please Enter Zip");
return;
}
var addr=document.QuotesForm.zip.value;
array1=addr.split("-");
if(array1[1]==undefined){
array1[1]=array1[0];
}
GB_show('GoogleMap','<%=path%>/jsps/fclQuotes/GoogleMap.jsp?address='+array1[1],600,600);
}
</script>
<style type="text/css">
                  #QuoteOPtions{
		    position:fixed;
		    _position:absolute;
		    border-style: solid solid solid solid;
		    background-color: white;
		    z-index:99;
		    left:30%;
		    top:40%;
		    bottom:5%;
		    right:5%;
		    _height:expression(document.body.offset+"px");
		  }
</style>
</head>

<body class="whitebackgrnd"  topmargin="0">
    <%@include file="../preloader.jsp"%>
    <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
<div id="commentDiv"   class="comments">
			<table border="1" id="commentTableInfo">
                            <tbody border="0"></tbody>
			</table>
</div>
<div id="cover" style=" width: 100%  ;height: 1000px;"></div>

	 <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
		<p class="progressBarHeader" style="width: 100%;padding-left:45px;"><b>Processing......Please Wait</b></p>
		<form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
			<input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
		</form>
	</div>
<!--DESIGN FOR NEW ALERT BOX ---->
<div id="AlertBox" class="alert">
	<p class="alertHeader"><b>Alert</b></p>
	<p id="innerText" class="containerForAlert">

	</p>
	<form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
	<input type="button"  class="buttonStyleForAlert" value="OK"
	onclick="document.getElementById('AlertBox').style.display='none';
	grayOut(false,'');">
	</form>
</div>

<div id="ConfirmBox" class="alert">
	<p class="alertHeader"><b>Confirmation</b></p>
        <p id="innerText1" class="containerForAlert">

	</p>
	<form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
	<input type="button" id="confirmYes" class="buttonStyleForAlert" value="OK"
	onclick="yes()">
	<input type="button" id="confirmNo"  class="buttonStyleForAlert" value="Cancel"
	onclick="No()">
	</form>
</div>
<div id="ConfirmYesOrNo" class="alert">
	<p class="alertHeader"><b>Confirmation</b></p>
	<p id="innerText2" class="containerForAlert"></p>
	<form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
	<input type="button"  class="buttonStyleForAlert" value="Yes"
	onclick="confirmYes()">
	<input type="button"  class="buttonStyleForAlert" value="No"
	onclick="confirmNo()">
	</form>
</div>
<div id="AlertBoxDefaultValues" class="alert">
    <p class="alertHeader"><b>Important Notes</b></p>
    <p id="innerText7" class="containerForAlert"></p>
    <div style="text-align:right;padding-right:4px;padding-bottom:4px;">
    <input type="button"  class="buttonStyleForAlert" value="OK"
                                     onclick="onclickAlertOk()">
    </div>
</div>
<!--// ALERT BOX DESIGN ENDS -->
<html:form action ="/quotes"  name="QuotesForm" type="com.gp.cvst.logisoft.struts.form.QuotesForm" scope="request">&nbsp;
    <%@include file="../fclQuotes/fragment/clientSearchOptions.jsp"%>
 <div id="QuoteOPtions" style="display:none;width:400px;height:150px;  top: 200px !important" align="center">
     <table class="tableBorderNew" width="100%">
                <tr class="textlabelsBold">
                    <td>
                      Carrier Print&nbsp;
                    </td>
                    <td>
                        <html:radio property="carrierPrint" value="on" name="quotationForm"  styleId="carrierPrintOn" onclick="carrierOPition(this)"/>Yes
                        <html:radio property="carrierPrint" value="off" name="quotationForm" styleId="carrierPrintOff" onclick="carrierOPition(this)"/>No
                    </td>
                </tr>

                <tr class="textlabelsBold" id="disclosure">
                    <td>
                       Important Disclosures Print&nbsp;
                    </td>
                    <td>
                        <html:radio property="importantDisclosures" value="on" name="quotationForm"  styleId="importantDisclosuresOn"  onclick="disclosureOPition(this)"/>Yes
                        <html:radio property="importantDisclosures" value="off" name="quotationForm" styleId="importantDisclosuresOff" onclick="disclosureOPition(this)"/>No
                    </td>
                </tr>

                 <tr class="textlabelsBold">
                    <td>
                        Use Alternate email address for Docs/Inquiries&nbsp;
                    </td>
                    <td>
                        <html:radio property="docsInquiries" value="on" name="quotationForm"  styleId="docsInquiriesOn" onclick="docsInquiriesOPition(this)"/>Yes
                        <html:radio property="docsInquiries" value="off" name="quotationForm" styleId="docsInquiriesOff" onclick="docsInquiriesOPition(this)"/>No
                    </td>
                </tr>
                    <tr class="textlabelsBold" id ="printGRIPortRemarks">
                        <td>
                            Print Port Remarks&nbsp;
                        </td>
                        <td>
                            <html:radio property="printPortRemarks" value="on" name="quotationForm"  styleId="printPortRemarksOn" onclick="printPortRemarksOPition(this)"/>Yes
                            <html:radio property="printPortRemarks" value="off" name="quotationForm" styleId="printPortRemarksOff" onclick="printPortRemarksOPition(this)"/>No
                        </td>
                    </tr>
                    <c:if test="${importFlag}">
                        <tr class="textlabelsBold">
                            <td>
                                Use Terminal Info for change in Issuing Terminal&nbsp;
                            </td>
                            <td>
                                <html:radio property="changeIssuingTerminal" value="Y" name="quotationForm"  styleId="changeIssuingTerminalY" onclick="printPortRemarksOPition(this)"/>Yes
                                <html:radio property="changeIssuingTerminal" value="N" name="quotationForm" styleId="changeIssuingTerminalN" onclick="printPortRemarksOPition(this)"/>No
                            </td>
                        </tr>
                    </c:if>
                <tr class="textlabelsBold" align="center">
                    <td colspan="2">
                        <input type="button" value="Submit" class="buttonStyleNew" onclick="submitQuoteOPtions()"/>
                        <input type="button" value="Cancel" class="buttonStyleNew" onclick="closeDivs()"/>
                    </td>
                </tr>
            </table>
 </div>

<table width="100%" border="0" cellpadding="2" cellspacing="0" >
	<tr>
	    <td class="textlabelsBold">
		<table   border="0">
		    <tr>
			<td>
			    <table>
				<tr class="textlabelsBold">
				    <td id="scroll">File No :<span class="fileNo"> <%=quotationNo%></span></td>
				</tr>
			    </table>
			</td>
			<td>
			    <table class="tableBorderNew">
				<tr class="textlabelsBold">
				    <td>Quote By :<b  class="headerlabel"><%=quoteBy%></b></td>
				    <td style="padding-left:5px;">On :
					<b class="headerlabel"><c:out value="<%=quoteDate%>" ></c:out></b></td>
				</tr>
			    </table>
			</td>
			<td id="creditStatusCol" style="display:none">
			    <table class="tableBorderNew">
				<tr class="textlabelsBold">
				    <td>
					<div id="creditStatus" class="red bold"/>
				    </td>
				</tr>
			    </table>
			</td>
		</table>
	    </td>
	  </tr>
	  <tr class="textlabelsBold">
	  <td>
	  <input type="button" id="cancel" value="Go Back" onclick="compareWithOldArray()" class="buttonStyleNew" />
          <span class="hotspot" onmouseover="tooltip.show('<strong><%=mandatoryFieldForQuotes%></strong>',null,event);" onmouseout="tooltip.hide();">
            <input type="button" id="save" value="Save" onclick="SAVE('${importFlag}')"   class="buttonStyleNew"/>
          </span>
          <input type="button" id="Options" value="Options" onclick="quoteOptions()" class="buttonStyleNew" />
	  </td>
          <c:choose>
              <c:when test="${importFlag}">
                  <td align="right">
	  	Non-Rated<html:radio property="ratesNonRates" value="N" name="quotationForm" styleId="nonRated"
		  onclick="getRatesNonRatesNimport();"/>&nbsp;&nbsp;
	 	Rated<html:radio property="ratesNonRates" styleId="rates" value="R" name="quotationForm" onclick="getRatesNonRatesYimport()"/>
		&nbsp;&nbsp;&nbsp;
                <span id="breakBulk">Break Bulk<html:radio property="breakBulk" value="Y" name="quotationForm"/>Y
		 &nbsp;&nbsp;&nbsp;<html:radio property="breakBulk" value="N" name="quotationForm"/>N</span>
                 Brand
                <c:choose>
                <c:when test="${companyCode == '03'}">
                <html:radio property="brand" value="Econo"  name="quotationForm" styleId="brandEcono" onclick="checkBrand('Econo','${quotationForm.quoteId}','${companyCode}')"/>Econo
                </c:when>
                <c:otherwise>
                <html:radio property="brand" value="OTI"  name="quotationForm" styleId="brandOti" onclick="checkBrand('OTI','${quotationForm.quoteId}','${companyCode}')"/>OTI
               </c:otherwise>
                </c:choose>
                <html:radio property="brand" value="${commonConstants.ECU_Worldwide}" name="quotationForm" styleId="brandEcuworldwide" onclick="checkBrand('Ecu Worldwide','${quotationForm.quoteId}','${companyCode}')"/>Ecu Worldwide
                </td>
              </c:when>
              <c:otherwise>
                  <td align="right">
                      <c:if test="${roleDuty.allowtoEnterSpotRate}">
                Spot/Bullet Rate
                <html:radio property="spotRate" value="Y" disabled="true" name="quotationForm" styleId="spotRateY"/>Y
                      <html:radio property="spotRate" value="N" disabled="true" name="quotationForm" styleId="spotRateN"/>N &nbsp;&nbsp;
                </c:if>
	  	Non-Rated<html:radio property="ratesNonRates" value="N" name="quotationForm" styleId="nonRated"
		  onclick="getRatesNonRatesN();changePolPodForNonRated();"/>&nbsp;&nbsp;
	 	Rated<html:radio property="ratesNonRates" styleId="rates" value="R" name="quotationForm" onclick="getRatesNonRatesY();changePolPodForNonRated();"/>
		&nbsp;&nbsp;&nbsp;
                <span id="breakBulk">Break Bulk<html:radio property="breakBulk" value="Y" name="quotationForm"/>Y
		 &nbsp;&nbsp;&nbsp;<html:radio property="breakBulk" value="N" name="quotationForm"/>N</span>
                Brand
                <c:choose>
                <c:when test="${companyCode == '03'}">
                <html:radio property="brand" value="Econo"  name="quotationForm" styleId="brandEcono" onclick="checkBrand('Econo','${companyCode}')"/>Econo
                </c:when>
                <c:otherwise>
                <html:radio property="brand" value="OTI"  name="quotationForm" styleId="brandOti" onclick="checkBrand('OTI','${companyCode}')"/>OTI
               </c:otherwise>
                </c:choose>
                <html:radio property="brand" value="${commonConstants.ECU_Worldwide}" name="quotationForm" styleId="brandEcuworldwide" onclick="checkBrand('Ecu Worldwide','${companyCode}')"/>Ecu Worldwide
                </td>
              </c:otherwise>
          </c:choose>

	  </tr>
	 </table>
<table  border="0"  width="100%" cellpadding="0"  cellspacing="0" class="tableBorderNew">
<tr><td>
	<table width="100%" border="0" cellpadding="0"  cellspacing="0" class="tableBorderNew">
	    <tr class="tableHeadingNew"><td colspan="8">Client</td></tr>
	    <tr>
		<td valign="top">
                    <table width="100%" border="0">
			<tr class="textlabelsBold">
			    <td align="right" style="padding-top: 4px;">Client&nbsp;</td>
			</tr>
			<tr class="textlabelsBold">
			    <td align="right" style="padding-top: 4px;">Contact Name&nbsp;</td>
			</tr>
		    </table>
		</td>
		<td>
		    <table border="0">
			<tr class="textlabelsBold">
			    <td id="existingClient" align="left">
				<input type="text" name="customerName" id="customerName"  size="22"
				       value="${quotationForm.customerName}" onchange="tabMoveClient('${importFlag}');" maxlength="50" style="text-transform: uppercase"
                                       class=" textlabelsBoldForTextBox mandatory" onfocus="checkClientConsignee()"/>
				<input id="custname_check" type="hidden" value="${quotationForm.customerName}"/>
				<div id="custname_choices" style="display: none" class="autocomplete"></div>
			    </td>
			    <td id="newerClient" align="left">
				<input type="text" name="customerName1" id="customerName1"  size="22"
				       value="${quotationForm.customerName1}" Class="textlabelsBoldForTextBox mandatory"
				       onkeydown="focusSetting(false);" onchange="getEmptyClient()"
				       style="text-transform: uppercase"  maxlength="50" />
				<input name="dup" type="hidden" Class="textlabelsBoldForTextBox"/>
			    </td>
			    <td>
				<img src="${path}/images/icons/search_filter.png" id="clientSearchEdit"
				     onmouseover="tooltip.show('<strong>Click here to edit Client Search options</strong>',null,event);" onmouseout="tooltip.hide();" onclick="showClientSearchOption()"/>
                                <img src="${path}/img/icons/e_contents_view.gif" width="14" height="14" title="Click here to see Client Notes"
                                             id="clientIcon" onclick="openBlueScreenNotesInfo(jQuery('#clientNumber').val(), jQuery('#customerName').val());"/>
				<html:checkbox property="clientConsigneeCheck" styleId="clientConsigneeCheck"  name="quotationForm"
					       onmouseover="tooltip.show('<strong>Checked=Consignee Listed <p> UnChecked=Consignee Not Listed</strong>',null,event);" onmouseout="tooltip.hide();"
					       onclick="clearNewClient()"></html:checkbox>
				<html:checkbox property="newClient" styleId="newClient" onclick="newClientEQ();setbrandvalueBasedONDestination('${companyCode}');" name="quotationForm" />New
			    </td>
			</tr>
			<tr class="textlabelsBold">
			    <td>
				<html:text property="contactName" styleId="contactName" style="text-transform: uppercase"
					   value="${quotationForm.contactName}" size="22" maxlength="200" styleClass="textlabelsBoldForTextBox"/>
				<div id="contactName_choices" style="display: none" class="autocomplete"></div>
				<img src="<%=path%>/img/icons/display.gif" alt="Look Up" align="middle" id="contactButton" onclick="getContactInfo()"/>
			    </td>
			</tr>
		    </table></td>
		<td width="8%">
		    <table width="100%" border="0">
			<tr class="textlabelsBold">
			    <td align="right" style="padding-bottom: 4px;">Acct No&nbsp;</td>
			</tr>
			<tr class="textlabelsBold">
			    <td align="right">Email&nbsp;</td>
			</tr>
		    </table>
		</td>
		<td width="16%">
		    <table width="100%" border="0">
			<tr class="textlabelsBold">
			    <td class="textBoxBorder">
				<input type="text" name="clientNumber" id="clientNumber" value="${quotationForm.clientNumber}" tabindex="-1"
				       maxlength="15" readonly="true" class="textlabelsBoldForTextBoxDisabledLook" size="22" />
		    </td>
			</tr>
			<tr>
			    <td>
				<html:text property="email" styleId="email" value="${quotationForm.email}" maxlength="500" onblur="emailValidate(this)" size="22" styleClass="textlabelsBoldForTextBox" tabindex="-1"/>
				<div id="email_choices" style="display: none" class="autocomplete"></div>
			    </td>
			</tr>
		    </table>
		</td>
		<td width="10.5%">
		    <table width="100%" border="0">
			<tr class="textlabelsBold">
			    <td align="right">Phone&nbsp;</td>
			</tr>
			<tr class="textlabelsBold">
			    <td align="right">Fax&nbsp;</td>
			</tr>
		    </table>
		</td>
		<td width="12%">
		    <table cellpadding="3"width="100%" border="0">
			<tr class="textlabelsBold">
			    <td class="textlabelsBold">
                                <html:text property="phone" maxlength="30" styleId="phone" onblur="checkForNumberAndDecimal(this)" value="${quotationForm.phone}"  styleClass="textlabelsBoldForTextBox" size="22" onkeyup="getIt(this)"/>
				<input type="checkbox"  style="visibility: hidden;" />
				<font class="textlabelsBold" style="visibility: hidden;">New</font>
			    </td>
			</tr>
			<tr class="textlabelsBold">
			    <td>
				<html:text property="fax" maxlength="30" styleId="fax" onblur="checkForNumberAndDecimal(this)" value="${quotationForm.fax}" styleClass="textlabelsBoldForTextBox" size="22" onkeyup="getIt(this)"/>
			    </td>
			</tr>
		    </table>
		</td>
		<td width="12%">
		    <table width="100%" border="0">
			<tr class="textlabelsBold">
			    <td align="right">Client Type:&nbsp;</td>
			</tr>
			<tr>
			    <td>&nbsp;</td>
			</tr>
		    </table>
		</td>
		<td width="18%">
		    <table width="100%" border="0">
			<tr class="textlabelsBold">
			    <td align="center">
				<html:text property="clienttype" styleId="clienttype" styleClass="whitebackgrnd" value="${quotationForm.clienttype}"
					   style="color:black;font:10;width:100%;font-weight:bold;border:0px;" readonly="true" tabindex="-1"/>
			    </td>
			</tr>
			<tr>
			    <td>&nbsp;</td>
			</tr>
		    </table>
		</td>
	    </tr>
	</table>
</td></tr>
<tr><td >

	<table width="100%" border="0" cellpadding="0"  cellspacing="0"  class="tableBorderNew">
        <tr class="tableHeadingNew"><td colspan="7">Trade Route</td></tr>
		<tr>

				 <td valign="top">
                                <table border="0" cellpadding="1" >
                                    <c:choose>
                                        <c:when test="${importFlag}">
                                             <tr  class="textlabelsBold">
                                     <td align="right">&nbsp;&nbsp;&nbsp;Origin&nbsp;</td>
                                     <td id="isTerminal1">
                                         <input name="isTerminal" id="isTerminal" class="textlabelsBoldForTextBox mandatory" value="${quotationForm.isTerminal}"
                                                               size="22" onblur="copyValPol();" onkeydown="getOriginUrl()"/>
                                                        <input type="hidden" id="isTerminal_check" value="${quotationForm.isTerminal}"/>
						    <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
                                                        <input type="checkbox" checked="checked" id="originCountry">   </span>

							<div id="isTerminal_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
							<script type="text/javascript">
								initAutocomplete("isTerminal","isTerminal_choices","","isTerminal_check",
									"<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6&importFlag=true&typeOfmove=&isDojo=false","setFocusFromDojo('portofDischarge')");
							</script>
        				</td>
        			</tr>
                                <tr class="textlabelsBold">
                                    <td align="right">&nbsp;&nbsp;&nbsp;Destination&nbsp;</td>
          			<td>
                                    <input Class="textlabelsBoldForTextBox mandatory" name="portofDischarge" onblur="copyValPod();" id="portofDischarge" value="${quotationForm.portofDischarge}" size="22"
					           onkeyup="getDestination()"
					           onkeydown="setDojoAction()"/>
                                                 <input type="hidden" id="portofDischarge_check" value="${quotationForm.portofDischarge}"/>
              	 			 <div id="destination_port_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                             <script type="text/javascript">
                				initAutocomplete("portofDischarge","destination_port_choices","","portofDischarge_check",
 								"<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7","disableAutoFF('getRates')");
                	 		 </script>
				             <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
				             <input type="checkbox" id="destinationCity" checked="checked" tabindex="-1"></span>

                             <div id="portofDischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                             <script type="text/javascript">
                                initAutocomplete("portofDischarge","portofDischarge_choices","","portofDischarge_check",
                                "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=76&radio=city&isDojo=false","");
                             </script>
	      	    	  </td>
       				 </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <tr class="textlabelsBold">
                                    <td align="right">&nbsp;&nbsp;&nbsp;Destination&nbsp;</td>
          			<td>
			   		         <input Class="textlabelsBoldForTextBox mandatory" name="portofDischarge" id="portofDischarge" value="${quotationForm.portofDischarge}" size="22"
					           onkeyup="getDestination()"
					           onkeydown="setDojoAction()"/>
                                                 <input id="portofDischarge_check" type="hidden" value="${quotationForm.portofDischarge}"/>
              	 			 <div id="destination_port_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                             <script type="text/javascript">
                				initAutocomplete("portofDischarge","destination_port_choices","","portofDischarge_check",
 								"<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7","disableAutoFF('isTerminal');setbrandvalueBasedONDestination('${companyCode}');");
                	 		 </script>
				             <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
				             <input type="checkbox" id="destinationCity" checked="checked" ></span>
                             <span onmouseover="tooltip.show('<strong>Show Rates for Entire Country</strong>',null,event);" onmouseout="tooltip.hide();">
				             <input type="checkbox" id="showAllCity" onclick="checkShowAllCity();" ></span>

                             <div id="portofDischarge_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                             <script type="text/javascript">
                                initAutocomplete("portofDischarge","portofDischarge_choices","","portofDischarge_check",
                                "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=76&radio=city&isDojo=false","setbrandvalueBasedONDestination('${companyCode}');");
                             </script>
	      	    	  </td>
       				 </tr>
       				 <tr  class="textlabelsBold">
                                     <td align="right">&nbsp;&nbsp;&nbsp;Origin&nbsp;</td>
                                     <td id="isTerminal1">
			 				<input name="isTerminal" id="isTerminal" class="textlabelsBoldForTextBox mandatory" value="${quotationForm.isTerminal}"
			 					size="22" onkeydown="getOriginUrl()"/>
                                                        <input id="isTerminal_check" type="hidden" value="${quotationForm.isTerminal}"/>
						    <span onmouseover="tooltip.show('<strong>Checked=Look by City Name <p> UnChecked=Look by Country</strong>',null,event);" onmouseout="tooltip.hide();">
				            <input type="checkbox" checked="checked" id="originCountry" >   </span>

							<div id="isTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
							<script type="text/javascript">
								initAutocomplete("isTerminal","isTerminal_choices","","isTerminal_check",
									"<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6&typeOfmove=&isDojo=false","setFocusFromDojo('getRates')");
							</script>
        				</td>
        			</tr>
                                        </c:otherwise>
                                    </c:choose>
        			<tr class="textlabelsBold">
                                    <td align="right">&nbsp;&nbsp;&nbsp;CommCode&nbsp;</td>
                                        <td><input name="commcode" class="textlabelsBoldForTextBox mandatory"  id="commcode" value="${quotationForm.commcode}"
	         		          maxlength="6" size="22" onkeydown="bulletRatesStauts();"/>
                                       <input id="commcode_check" type="hidden" value="${quotationForm.commcode}"/>
					<div id="commcode_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                            <script type="text/javascript">
                                                    initAutocomplete("commcode","commcode_choices","","commcode_check",
									"<%=path%>/actions/getChargeCode.jsp?tabName=QUOTE&isDojo=false","disableAutoFF('isTerminal')");
                                            </script>
                                            <html:checkbox property="bulletRatesCheck" onclick="bulletRatesClick()" styleId="bulletRates" name="quotationForm"
                                            onmouseover="tooltip.show('<strong>Bullet Rates</strong>',null,event);"
                                               onmouseout="tooltip.hide();"/>
					   <input type="button" value="Rates" id="getRates" class="buttonStyleNew"
	         		          onClick="return popupAddRates('windows','<%=quotationNo%>')" style="width:52px"/>
					   </td>
	                </tr>
	  				<tr class="textlabelsBold">
                                            <td align="right" id="descriptionLabel" style="display: none;">Description :&nbsp;</td>
	                <td style="display: none;"><html:text property="description"  value="${QuotesForm.description}" style="color:black;width:100%;border:0;font:10;font-weight:bold;"
	 					styleId="description" styleClass="textlabelsBoldForTextBox" readonly="true" tabindex="-1"></html:text>
	                </td>
                        <td align="right">&nbsp;&nbsp;&nbsp;Iss Term&nbsp;</td>
                        <c:choose>
                            <c:when test="${importFlag}">
                                <td>
         				<input type="text" name="issuingTerminal" class="textlabelsBoldForTextBox mandatory"
         				value="${quotationForm.issuingTerminal}" id="issuingTerminal" size="22"/>
                                        <input type="hidden" value="${quotationForm.issuingTerminal}" id="issuingTerminal_check"/>
						<div id="issuingTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
						 <script type="text/javascript">
                                                        initAutocomplete("issuingTerminal","issuingTerminal_choices","","issuingTerminal_check",
                                                        "<%=path%>/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false&importFlag=true","setFocusFromDojo('hazmatYes')");
						 </script>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td>
         				<input type="text" name="issuingTerminal" class="textlabelsBoldForTextBox mandatory"
         				value="${quotationForm.issuingTerminal}" id="issuingTerminal" size="22"/>
                                        <input type="hidden" value="${quotationForm.issuingTerminal}" id="issuingTerminal_check"/>
						<div id="issuingTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
						 <script type="text/javascript">
                                                        initAutocomplete("issuingTerminal","issuingTerminal_choices","","issuingTerminal_check",
                                                        "<%=path%>/actions/getTerminalName.jsp?tabName=QUOTE&isDojo=false","setFocusFromDojo('hazmatYes')");
						 </script>
 		 			</td>
                            </c:otherwise>
                        </c:choose>
 		 			</tr>
                                        <tr class="textlabelsBold">
                                                <td align="right">Hazmat&nbsp;</td>
                                                <td>
                                                    <html:radio property="hazmat" value="Y" styleId="hazmatYes" onclick="getHazmat()" name="quotationForm"/>Yes
                                                    <html:radio property="hazmat" value="N" styleId="hazMatNo" onclick="deleteHazmat()" name="quotationForm"/>No
                                                </td>
                                        </tr>

          		</table>
          	</td>
          	 <td valign="top">
                     <table border="0" width="100%" cellpadding="1">
                        <tr class="textlabelsBold">
                            <td align="right">POL</td>
                            <td>
                                <input name="placeofReceipt" class="textlabelsBoldForTextBox" id="placeofReceipt" value="${quotationForm.placeofReceipt}" size="22" tabindex="-1" />
                                <img src="<%=path%>/img/icons/display.gif" id="polLookUp" onmouseover="tooltip.show('<strong>Search POL</strong>',null,event);" onmouseout="tooltip.hide();"
                                  onclick="return GB_show('POL Search','<%=path%>/searchquotation.do?buttonValue=searchPort&setFocus=finalDestination&textName=placeofReceipt&from=pol',250,670);"/>
                                  <input id="placeofReceipt_check" type="hidden" value="${quotationForm.finalDestination}"/>
                                <div id="placeofReceipt_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                 <script type="text/javascript">
                                                initAutocomplete("placeofReceipt","placeofReceipt_choices","","placeofReceipt_check",
                                                "<%=path%>/actions/getUnlocationCode.jsp?tabName=QUOTE&from=3&isDojo=false","");
                                 </script>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">POD</td>
                            <td>
                                <input name="finalDestination" id="finalDestination" class="textlabelsBoldForTextBox" value="${quotationForm.finalDestination}"
                                        size="22" onkeyup="getPod()" onkeydown="getAgentforPod(this.value,'${importFlag}')" tabindex="-1" />
                                <input id="finalDestination_check" type="hidden" value="${quotationForm.finalDestination}"/>
                                 <img src="<%=path%>/img/icons/display.gif" id="podLookUp" onmouseover="tooltip.show('<strong>Search POD</strong>',null,event);" onmouseout="tooltip.hide();"
                                  onclick="return GB_show('POD Search','<%=path%>/searchquotation.do?buttonValue=searchPort&setFocus=portofDischarge&textName=finalDestination&from=pod',250,670);"/>
                                <div id="finalDestination_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
                                 <script type="text/javascript">
                                                initAutocomplete("finalDestination","finalDestination_choices","","finalDestination_check",
                                                "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=4&isDojo=false","");
                                 </script>
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">Transit Days</td>
                            <td>
                                <html:text property="noOfDays" styleId="noOfDays" onchange="numericTextbox(this)" maxlength="3" styleClass="textlabelsBoldForTextBox" value="${quotationForm.noOfDays}" size="3"/>
                            </td>
                        </tr>
                            <tr class="textlabelsBold">
                                <c:choose>
                                    <c:when test="${importFlag}">
                                        <td align="right">NVO Move</td>
                            <td id="typeofMove"  colspan="5">
                                <html:select property="typeofMove"  styleId="move" styleClass="dropdown_accounting"
                                    onchange="sendToDojoJsp()" style="width:130px;">
                                <html:optionsCollection  name="typeOfMoveList"/></html:select>
                                <img src="<%=path%>/img/icons/help-icon.gif" onmouseover="tooltip.show('If NVO move is from \'DOOR\', then spotting address is required on Booking','60',event);"
                                 onmouseout="tooltip.hide()"/>
				    <input value="off" type="checkbox" name="rampCheck"  id="rampCheck" style="display: none"/>
                                </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td align="right">NVO Move</td>
                                <td id="typeofMove"  colspan="5">
                                <div class="mandatory" style="float:left">
                                <html:select property="typeofMove"  styleId="move" styleClass="dropdown_accounting"
                                    onchange="sendToDojoJsp()" style="width:130px;">
                                    <html:optionsCollection name="typeOfMoveList"/></html:select></div>
                                <img src="<%=path%>/img/icons/help-icon.gif" onmouseover="tooltip.show('If NVO move is from \'DOOR\', then spotting address is required on Booking','60',event);"
                                 onmouseout="tooltip.hide()"/>
                                <c:choose>
                                    <c:when test="${!empty quotationForm.zip}">
                                        <html:checkbox property="rampCheck" styleId="rampCheck"  name="quotationForm" style="visibility: visible" tabindex="-1"
					       onmouseover="tooltip.show('<strong>Check here to change Door Origin to Ramp Origin</strong>',null,event);" onmouseout="tooltip.hide();"
                                               onclick="getTypeofMoveList(document.getElementById('move'));"></html:checkbox>
                                    </c:when>
                                    <c:otherwise>
                                        <html:checkbox property="rampCheck" styleId="rampCheck"  name="quotationForm" style="visibility: hidden" tabindex="-1"
					       onmouseover="tooltip.show('<strong>Check here to change Door Origin to Ramp Origin</strong>',null,event);" onmouseout="tooltip.hide();"
					       onclick="getTypeofMoveList(document.getElementById('move'));"></html:checkbox>
                                    </c:otherwise>
                                </c:choose>
                                </td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        <tr class="textlabelsBold">
                            <td align="right">File Type</td>
                            <td>
                                <c:choose>
                                    <c:when test="${importFlag}">
                                        <input  value="IMPORT" class="BackgrndColorForTextBox" size="4" readonly="true" tabindex="-1" />
                                        <input type="hidden" name="fileType" value="I" id="fileType"/>
                                    </c:when>
                                    <c:otherwise>
                                        <html:radio property="fileType" value="S" name="quotationForm"  styleId="fileTypeS" tabindex="-1">
                                        <span onmouseover="tooltip.show('<strong>Standard</strong>',null,event);"
                                           onmouseout="tooltip.hide();">S</span>
                                     </html:radio>
                                     <html:radio property="fileType" value="C" name="quotationForm"  styleId="fileTypeC" tabindex="-1">
                                        <span onmouseover="tooltip.show('<strong>CFCL</strong>',null,event);"
                                           onmouseout="tooltip.hide();">C</span>
                                     </html:radio>
                                     <html:radio property="fileType" value="P" name="quotationForm"  styleId="fileTypeP" tabindex="-1">
                                        <span onmouseover="tooltip.show('<strong>Project</strong>',null,event);"
                                           onmouseout="tooltip.hide();">P</span>
                                     </html:radio>
                                    </c:otherwise>
                                </c:choose>
                          </td>
                        </tr>
                </table>
                 </td>
                 <td valign="top">
                     <table border="0" width="100%" cellpadding="1">

                         <c:choose>
                             <c:when test="${importFlag}">
                                 <tr class="textlabelsBold">
                                     <td align="right">Door at Origin</td>
                                     <td>
                                         <input name="doorDestination" id="doorDestination" onchange="ImportDoorOrgin();" class="textlabelsBoldForTextBox" onkeydown="getTypeOfMove1()" value="${quotationForm.doorDestination}" size="22"/>
                                         <input type="hidden" id="doorDestination_check" value="${quotationForm.doorDestination}"/>
                                                <div id="doorDestination_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                         <script type="text/javascript">
                                             initAutocomplete("doorDestination","doorDestination_choices","","doorDestination_check",
                                             "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=10&isDojo=false","setFocusFromDojo('intermodel')");
                                         </script>
                                         <html:checkbox property="newDestination" styleId="newDestination" onclick="newDestinationEQ()" name="quotationForm"></html:checkbox>
                                         New
                                     </td>
                                 </tr>
                                 <tr class="textlabelsBold">
                                     <td align="right">Door Dest Zip/City</td>
                                     <td  id="originZip">
                                         <html:text property="zip" styleId="zip" styleClass="textlabelsBoldForTextBox"
                                                    onkeydown="setNVOmove();if(event.keyCode==9){tabMoveDoordest()}" value="${quotationForm.zip}" size="22" />
                                         <input type="hidden" id="zip_check" value="${quotationForm.zip}"/>
                                         <div id="zip_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                         <script type="text/javascript">
                                             initAutocompleteWithFormClear("zip","zip_choices","doorOrigin","zip_check",
                                             "<%=path%>/actions/getZipCode.jsp?tabName=QUOTE&from=1","updateDoorOrigin('doorOrigin')","");
                                         </script>
                                          <img src="<%=path%>/img/map.png" id="zipLookUp" align="middle" onmouseover="tooltip.show('<strong>Google Map Search</strong>',null,event);" onmouseout="tooltip.hide();"
                                              onclick="getGoogleMap()" />
                                     </td >
                                 </tr>
                                 <tr class="textlabelsBold">
                                     <td align="right">Door Dest</td>
                                     <td>

                                         <input name="doorOrigin" id="doorOrigin" class="textlabelsBoldForTextBox" tabindex="-1"
                                                onblur="getTypeOfMove()" onkeydown="getTypeOfMove()" value="${quotationForm.doorOrigin}" size="22"/>
                                         <input type="hidden" id="doorOrigin_check" value="${quotationForm.doorOrigin}"/>
                                         <div id="doorOrigin_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                         <script type="text/javascript">
                                             initAutocompleteWithFormClear("doorOrigin","doorOrigin_choices","","doorOrigin_check",
                                             "<%=path%>/actions/getUnlocationCode.jsp?tabName=QUOTE&from=5&isDojo=false","setFocusFromDojo('zip')","hideOrgZip();");
                                         </script>

                                     </td>
                                     <td style="display:none;">
                                         <html:checkbox property="newOrigin" tabindex="-1" styleId="newOrigin" onclick="newOriginEQ()" name="quotationForm"></html:checkbox>
                                             New
                                         </td>
                                     </tr>
                             </c:when>
                             <c:otherwise>
                                 <tr class="textlabelsBold">
                                     <td align="right">Origin Zip/City</td>
                                     <td  id="originZip">
                                         <html:text property="zip" styleId="zip"  styleClass="textlabelsBoldForTextBox"
                                                    onkeydown="setNVOmove()" value="${quotationForm.zip}" size="22" /><!---->
                                         <input type="hidden" id="zip_check" value="${quotationForm.zip}"/>
                                         <div id="zip_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                         <script type="text/javascript">
                                             initAutocompleteWithFormClear("zip","zip_choices","doorOrigin","zip_check",
                                             "<%=path%>/actions/getZipCode.jsp?tabName=QUOTE&from=1","updateDoorOrigin('doorOrigin')","hiderampCheckbox();");
                                         </script>
                                         <img src="<%=path%>/img/map.png" id="zipLookUp" align="middle" onmouseover="tooltip.show('<strong>Google Map Search</strong>',null,event);" onmouseout="tooltip.hide();"
                                              onclick="getGoogleMap()" />
                                     </td >
                                 </tr>
                                 <tr class="textlabelsBold">
                                     <td align="right">Door Org</td>
                                     <td>

                                         <input name="doorOrigin" id="doorOrigin" class="textlabelsBoldForTextBoxDisabledLook" readonly="true"
                                                onblur="getTypeOfMove()" onkeydown="getTypeOfMove()" tabindex="-1" value="${quotationForm.doorOrigin}" size="22"/>
                                         <input type="hidden" id="doorOrigin_check" value="${quotationForm.doorOrigin}"/>
                                         <div id="doorOrigin_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                         <script type="text/javascript">
                                             initAutocompleteWithFormClear("doorOrigin","doorOrigin_choices","","doorOrigin_check",
                                             "<%=path%>/actions/getUnlocationCode.jsp?tabName=QUOTE&from=5&isDojo=false","setFocusFromDojo('zip')","hideOrgZip();");
                                         </script>

                                     </td>
                                     <td style="display:none;">
                                         <html:checkbox property="newOrigin" styleId="newOrigin" onclick="newOriginEQ()" name="quotationForm"></html:checkbox>
                                             New
                                         </td>
                                     </tr>
                                 <tr class="textlabelsBold">
                                     <td align="right">Door at Dest</td>
                                     <td>
                                         <input name="doorDestination" id="doorDestination" onkeydown="if(event.keyCode==9){tabMoveDoordest()}" class="textlabelsBoldForTextBox" onkeydown="getTypeOfMove1();" value="${quotationForm.doorDestination}" size="22"/>
                                         <input type="hidden" id="doorDestination_check" value="${quotationForm.doorDestination}"/>
                                                <div id="doorDestination_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                         <script type="text/javascript">
                                             initAutocomplete("doorDestination","doorDestination_choices","","doorDestination_check",
                                             "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=10&isDojo=false","setFocusFromDojo('intermodel')");
                                         </script>
                                             <input type="checkbox" name="onCarriage" id="onCarriage" title="On-Carriage" />
                                             <html:checkbox property="newDestination" styleId="newDestination" tabindex="-1" onclick="newDestinationEQ()" name="quotationForm"></html:checkbox>
                                             New
                                     </td>
                                 </tr>
                             </c:otherwise>
                         </c:choose>
                         <tr class="textlabelsBold">
                             <td align="right"><div id="inlandVal" ></div></td>
                             <td colspan="2">
                                 <html:radio property="inland" value="Y" onclick="getInland()" name="quotationForm" tabindex="-1"/>Yes
                                 <html:radio property="inland" value="N" onclick="deleteInland()" name="quotationForm" tabindex="-1"/>No
                                 <html:hidden  property="intermodelComments"/>
                                 <div id="localDrayageCommentToolTipDiv" style="display: none;">
                                     <span class="hotspot" onmouseover="showToolTip()"
                                           onmouseout="tooltip.hideComments();" style="color:black;">
                                         <img src="<%=path%>/img/icons/view.gif"/></span>
                                 </div>
                             </td>
                         </tr>

                         <tr class="textlabelsBold">
                             <td align="right" style="display: none" id="override">Override</td>
                             <td colspan="2"><div id="localDrayageCommentToolTipDiv" style="display: none;">
                                     <span class="textlabelsBold" onmouseover="showToolTip()" tabindex="-1"  onmouseout="tooltip.hideComments();" style="color:black;padding-left: 10px;" >
                                         <img src="<%=path%>/img/icons/view.gif" id="localDrayageCommentImg"/></span>
                                 </div>
                             </td>
                         </tr>

                     </table>
                 </td>

                <td valign="top">
                    <table width="100%" class="tableBorderNew" cellpadding="1"
                           style="border-left-width: 1px;border-left-color:grey;border-top-width: 0px;
					border-bottom-width: 0px;border-right-width: 0px;">
                        <tr class="textlabelsBold">
                            <td align="right">Default Agent&nbsp;</td>
                            <td>
                                <html:radio property="defaultAgent" styleId="defaultAgentY" onclick="fillDefaultAgent();"
                                            value="Y" name="quotationForm" tabindex="-1"/>Yes
                                <html:radio property="defaultAgent" styleId="defaultAgentN" onclick="clearValues();"
                                            value="N" name="quotationForm" tabindex="-1"/>No
                            </td>
                        </tr>
                         <tr class="textlabelsBold">
                            <td align="right">Direct Consignment&nbsp;</td>
                            <td>
                                <html:radio property="directConsignmntCheck" styleId="directConsignmentY" onclick="directConsignmnt();"
                                            value="on" name="quotationForm" tabindex="-1"/>Yes
                                <html:radio property="directConsignmntCheck" styleId="directConsignmentN" onclick="directConsignmntNo();"
                                            value="off"  name="quotationForm" tabindex="-1"/>No
                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">Agent Name&nbsp;</td>
                            <td>
                                <html:text property="agent" styleId="agent" styleClass="textlabelsBoldForTextBox" value="${quotationForm.agent}"
                                           size="22" onkeydown="setDojoPathForAgent()"/>
                                <input name="agent_check" id="agent_check" type="hidden" value="${QuoteValues.agent}"/>
                                <div id="agent_choices" style="display:none"  class="autocomplete"></div>
                                <script type="text/javascript">
                                    initOPSAutocomplete("agent","agent_choices","agentNo","agent_check",
                                    "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=3&importFlag=${importFlag}","checkDisableForAgent('routedbymsg')");
                                                                </script>
                                                         </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">Agent Number&nbsp;</td>
                            <td>
                                <html:text property="agentNo" styleId="agentNo" styleClass="textlabelsBoldForTextBoxDisabledLook"
                                           size="22" value="${quotationForm.agentNo}" readonly="true" tabindex="-1" ></html:text>
                            </td>

                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right" valign="middle">ERT&nbsp;</td>
                            <td >

                                    <c:choose>
                                        <c:when test="${importFlag}">
                                            <div class="textlabelsBold" style="float: left">
                                                <html:select property="routedAgentCheck"  onkeydown="if(event.keyCode==9){tabMoveErt();}"  style="width:130px;" styleClass="dropdown_accounting"
                                                             onchange="setDefaultRouteAgent('${importFlag}')" styleId="routedAgentCheck" value = "no">
                                                    <html:option value="">Select</html:option>
                                                    <html:option value="yes">Yes</html:option>
                                                    <html:option value="no">No</html:option>
                                                </html:select>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="mandatory" style="float: left">
                                                <html:select property="routedAgentCheck" onkeydown="if(event.keyCode==9){tabMoveErt();}"  style="width:130px;" styleClass="dropdown_accounting"
                                                             onchange="setDefaultRouteAgent('${importFlag}')" styleId="routedAgentCheck">
                                                    <html:option value="">Select</html:option>
                                                    <html:option value="yes">Yes</html:option>
                                                    <html:option value="no">No</html:option>
                                                </html:select>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>

                            </td>
                        </tr>
                        <tr class="textlabelsBold">
                            <td align="right">Routed By Agent&nbsp;</td>
                            <td  valign="top">
                                <input type="text" Class="textlabelsBoldForTextBox" name="routedbymsg" id="routedbymsg"  size="22" value="${quotationForm.routedbymsg}"  maxlength="15"/>
                                <input Class="textlabelsBoldForTextBox"  name="routedbymsg_check"
                                       id="routedbymsg_check" type="hidden" />
                                <div id="Routed_Choices" style="display: none" class="autocomplete"></div>
                                <script type="text/javascript">
                                    initOPSAutocomplete("routedbymsg","Routed_Choices","routedNo","routedbymsg_check",
                                    "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=2","checkDisable()");
                                </script>
                                <input name="routedNo" id="routedNo" type="text" style="display: none;"/>
                            </td>
                        </tr>
                    </table>
                </td>
          </tr>
          	<tr>
			<td colspan="8" >
                            <table width="100%" height="100%" border="0" class="tableBorderNew" cellpadding="1"
                                style="border-top-width: 1px;border-top-color:grey;border-left-width: 0px;
                                border-bottom-width: 0px;border-right-width: 0px;">


				<tr class="textlabelsBold">
				<td valign="top">Remarks for <c:out value="${quotationForm.portofDischarge}"/>:</td>
  		 	<td width="100%">
  		 	    <%
  		 	    	if(request.getAttribute("quotationForm")!=null){
  		 	    		QuotesForm quotesForm=(QuotesForm)request.getAttribute("quotationForm");
  		 	    		String remarks = quotesForm.getRemarks()!=null?quotesForm.getRemarks():"";
  		 	    		String[] remarksDup = remarks.split("\\n");
  		 	    		out.println("<ul style='margin-left: 0px;'>");
  		 	    		for(int i=0;i<remarksDup.length;i++){
  		 	    			out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>"+remarksDup[i]+"</li>");
  		 	    		}
  		 	    		out.println("</ul>");
					}
  		 	    %>
  		 	</td>

       		</tr>
    	<tr class="textlabels">
    			<td>&nbsp;</td>
    			<td valign="top"  >
    			<%
    				if(request.getAttribute("quotationForm")!=null){
  		 	    		QuotesForm quotesForm=(QuotesForm)request.getAttribute("quotationForm");
  		 	    		String rateRemarks = quotesForm.getRatesRemarks()!=null?quotesForm.getRatesRemarks():"";
  		 	    		String[] rateRemarksDup = rateRemarks.split("\\n");

  		 	    		out.println("<ul style='margin-left: 0px;'>");
						for(int i=0;i<rateRemarksDup.length;i++){
  		 	    			out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>"+rateRemarksDup[i]+"</li>");
  		 	    		}
  		 	    		out.println("</ul>");

  		 	    	}
  		 	    	%>
  		 	    	</td>
				</tr>
                                <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td width="100%">
                                                <%
                                                    if(request.getAttribute("QuoteValues")!=null){
                                                    QuotesForm quotesForm=(QuotesForm)request.getAttribute("quotationForm");
                                                    String remarks = quotesForm.getFclTempRemarks() != null ? quotesForm.getFclTempRemarks() : "";
                                                    String[] remarksDup = remarks.split("\\n");
                                                    out.println("<ul style='margin-left: 0px;'>");
                                                    for(int j=0;j<remarksDup.length;j++){
                                                        out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>"+remarksDup[j]+"</li>");
                                                    }
                                                    out.println("</ul>");
                                                    }
                                                %>
                                            </td>
                               </tr>
                                <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td width="100%">
                                                <%
                                                    if(request.getAttribute("QuoteValues")!=null){
                                                    QuotesForm quotesForm=(QuotesForm)request.getAttribute("quotationForm");
                                                    String remarksGRI = quotesForm.getFclGRIRemarks() != null ? quotesForm.getFclGRIRemarks() : "";
                                                    String[] remarksGRIDup = remarksGRI.split("\\n");
                                                    out.println("<ul style='margin-left: 0px;'>");
                                                    for(int j=0;j<remarksGRIDup.length;j++){
                                                        out.println("<li  style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>"+remarksGRIDup[j]+"</li>");
                                                    }
                                                    out.println("</ul>");
                                                    }
                                                %>
                                            </td>
                               </tr>
                                <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td width="100%" style='color: #FF4A4A;text-align: left;list-style-type:none;list-style: none;'>
                                               ${remarks}
                                            </td>
                               </tr>
			</table></td>
         </tr>
         <tr>
                            </tr>
     </table>
</td></tr>
<tr>
    <td>
        <table border="0" width="100%"  cellpadding="0"  cellspacing="0"  class="tableBorderNew">
            <tr>
                <td class="tableHeadingNew" width="50%" style="border-right:1px solid #dcdcdc">Carrier</td>
                <td class="tableHeadingNew" width="50%">Other Charges</td>
            </tr>
            <tr>
                <td width="50%">
                   <table border="0" width="100%" cellpadding="0"  cellspacing="0" style="border-right:1px solid #dcdcdc">
                        <tr><td colspan="10">
                                <table ><tr>
                                        <td id="message"><font color="#006400" size="2" id="message"><b><%=message%></b></font></td>
                                    </tr></table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table cellpadding="3" width="100%">
                                    <tr class="textlabelsBold">
                                        <td align="right">SSL Name&nbsp;</td>
                                    </tr>
                                    <tr class="textlabelsBold"><td align="right">Phone&nbsp;</td></tr>
                                </table>
                            </td>
                            <td>
                                <table width="100%" border="0">
                                    <tr class="textlabelsBold">
                                        <td>
                                            <input type="text" Class="textlabelsBoldForTextBox" name="sslDescription" id="sslDescription"  size="22" value="<%=sslName%>"  maxlength="50"/>
                                            <input id="sslname_check" type="hidden" value="<%=sslName%>" />
                                            <div id="sslname_choices" style="display: none" class="autocomplete"></div>
                                            <script type="text/javascript">
                                                            initAutocompleteWithFormClear("sslDescription","sslname_choices","sslcode","sslname_check",
                                                            "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=1","focusSettingForSSl();","onBlurForSSL();");
                                            </script>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td class="textlabelsBold">
                                            <html:text property="carrierPhone" styleId="carrierPhone" styleClass="textlabelsBoldForTextBox" onblur="checkForNumberAndDecimal(this)"
                                                       value="${quotationForm.carrierPhone}" size="22" maxlength="50" onkeyup="getIt(this)"></html:text>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table cellpadding="3" width="100%">
                                        <tr class="textlabelsBold"><td align="right">SSL Acct&nbsp;
                                            </td></tr>
                                        <tr class="textlabelsBold"><td align="right">Fax&nbsp;</td></tr>
                                    </table>
                                </td>
                                <td>
                                    <table width="100%">
                                        <tr class="textlabelsBold"><td>
                                                    <input name="sslcode" id="sslcode" readonly="readonly" class="textlabelsBoldForTextBoxDisabledLook" size="22" value="${quotationForm.sslcode}" tabindex="-1"  />
                                                <img src="<%=path%>/img/icons/display.gif" style="visibility: hidden;" />
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>
                                                <html:text property="carrierFax" styleId="carrierFax" styleClass="textlabelsBoldForTextBox" onblur="checkForNumberAndDecimal(this)" value="${quotationForm.carrierFax}" size="22" maxlength="50" onkeyup="getIt(this)"></html:text>

                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table cellpadding="3" width="100%">
                                        <tr class="textlabelsBold" valign="top"><td align="right">Cont Name&nbsp;</td></tr>
                                        <tr class="textlabelsBold" valign="top"><td align="right">Email&nbsp;</td></tr>
                                    </table>
                                </td>
                                <td><table width="100%">
                                        <tr class="textlabelsBold" valign="top"><td class="textlabelsBold">
                                            <html:text property="carrierContact" styleClass="textlabelsBoldForTextBox" styleId="carrierContact"
                                                       value="${quotationForm.carrierContact}" size="22" maxlength="50"   ></html:text>
                                            <img src="<%=path%>/img/icons/display.gif"  id="contactNameButton"
                                                 onclick="getCarrierContactInfo()" />
                                            <input type="checkbox"  style="visibility: hidden;"/>
                                            <font class="textlabelsBold" style="visibility: hidden;">New</font>
                                        </td></tr>
                                        <tr class="textlabelsBold" valign="top">
                                            <td class="textlabelsBold">
                                                <html:text property="carrierEmail" styleClass="textlabelsBoldForTextBox" styleId="carrierEmail" onblur="emailValidate(this)"
                                                           maxlength="500" value="${quotationForm.carrierEmail}" size="22"></html:text>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                </td>
                <td width="50%">
                    <table  width="100%" style="margin-left: 0px;">
                             <tr>
                                <td>
                                    <table width="100%"  border ="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <c:if test="${importFlag eq false}">
                                                <td class="textlabelsBold"> Auto Deduct FF Commission
                                                <html:radio property="deductFFcomm" value="Y" styleId="y5" name="quotationForm" onclick="getFFCommission()" tabindex="-1"/>Yes
                                                    <html:radio property="deductFFcomm" value="N" styleId="n5" name="quotationForm" onclick="deleteFFCommission()" tabindex="-1"/>No
                                                </td>
                                            </c:if>
                                            <td class="textlabelsBold"> Document Charge
                                                <html:radio property="docCharge" value="Y" tabindex="-1" styleId="docChargeY" name="quotationForm" onclick="getDocCharge()"/>Yes
                                                <html:radio property="docCharge" value="N" tabindex="-1" styleId="docChargeN" name="quotationForm"/>No
                                            </td>
                                            
                                            <c:if test="${importFlag eq false}">
                                             <td class="textlabelsBold"> Chassis
                                                <html:radio property="chassisCharge" value="Y" tabindex="-1" styleId="chassisChargeY" name="quotationForm" disabled="true"/>Yes
                                                <html:radio property="chassisCharge" value="N" tabindex="-1" styleId="chassisChargeN" name="quotationForm" disabled="true"/>No
                                            </td>
                                            </c:if>
                                            
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <table style="border-top:1px solid #dcdcdc;" width="100%">
                                        <tr class="textlabelsBold">
                                            <td>Insurance:</td>
                                            <td>
                                                <html:radio property="insurance" tabindex="-1"  value="Y" styleId="insuranceYes" onclick="getInsurance('${quotationForm.totalCharges}')" name="quotationForm"/>Yes
                                                <html:radio property="insurance"  tabindex="-1" styleId="insurance" value="N" onclick="getinsurance1()" name="quotationForm"/>No
                                            </td>
                                            <td valign="bottom" colspan="2" style="padding-left:20px;">Cost of Goods
                                                <html:text property="costofgoods" readonly="true" onkeypress="return false;" onkeydown="return false;" value="${quotationForm.costofgoods}" size="15" styleClass="textlabelsBoldForTextBox"/>
                                            </td>
                                            <td valign="bottom" style="padding-left:20px;">Pier Pass:
                                                <html:radio property="pierPass" tabindex="-1"  value="Y"  name="quotationForm" disabled="true"/>Yes
                                                <html:radio property="pierPass" tabindex="-1"  value="N"  name="quotationForm" disabled="true"/>No
                                            </td> 
                                            <td valign="bottom" style="visibility:hidden">Insurance Amt:
                                                <html:text property="insuranceCharge" styleId="insuranceCharge" styleClass="textlabelsBoldForTextBox" value="${quotationForm.insuranceCharge}" size="15"
                                                           readonly="true" onchange="addInsuranceToBl()"/>
                                            </td>
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
<tr>
    <td>
	<table border="0" width="100%"  cellpadding="0"  cellspacing="0"  class="tableBorderNew">
            <tr>
                <td valign="top">
                    <table class="tableBorderNew" height="100%"  border="0"
                           style="border-left-width: 0px;border-right-width: 0px;border-bottom-width: 0px;"
                           cellpadding="0" cellspacing="0" width="100%">
                        <tr class="tableHeadingNew">
                            <td width="50%"> Goods Description<font class="mandatoryStarColor"><%=star%></font></td>
                            <td width="50%" style="border-left:#dcdcdc 1px solid;padding-left: 3px">
                                <div style="float:left">Remarks  </div>
                                <div style="float:right" align="right">
                                    Predefined Remarks<img src="<%=path%>/img/icons/display.gif" value="Look Up"  onclick="goToRemarksLookUp()"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="50%">
                                <html:textarea  property="goodsdesc" styleClass="textlabelsBoldForTextBox"
                                                value="${quotationForm.goodsdesc}" cols="100"
                                                style="text-transform: uppercase; width:100%" rows="6"
                                                onkeypress="return checkTextAreaLimit(this, 500)">
                                </html:textarea>
                            </td>
                            <td width="50%">
                                <html:textarea  property="comment" style="text-transform: uppercase;  width:100%"
                                                styleClass="textlabelsBoldForTextBox" styleId="commentRemark"
                                                cols="100"  rows="6" onblur="tabMoveRemark();" value="${quotationForm.comment}"
                                                onkeypress="return checkTextAreaLimit(this, 500)">
                                </html:textarea>
                            </td>
                        </tr>
                    </table>
                </td>

            </tr>
        </table>
    </td>
</tr>
<tr><td>
	<table width="100%" border="0"  cellpadding="0"  cellspacing="0"  class="tableBorderNew">
    <tr class="tableHeadingNew"><td colspan="2">Special Provision</td></tr>
	<tr>
		<td height="100%"><table width="100%" height="100%" class="tableBorderNew"
                            style="border-right-width: 0px;border-right-color: grey;border-left-width: 0px;
                            border-bottom-width: 0px;border-top-width: 0px;">
			<tr>
                            <td class="textlabelsBold" align="top" width="115px">Special Equipment :</td>
                            <td class="textlabelsBold" width="100px">
                                <html:radio property="specialequipment"  value="Y" tabindex="-1" name="quotationForm" onclick="getspcleqpmt(this)" styleId="y3" />Yes
                            <html:radio property="specialequipment" value="N" tabindex="-1" name="quotationForm" onclick="getspcleqpmt1()"/>No</td>
                            <td colspan="2">
                            <html:select property="specialEqpmt" styleClass="textlabelsBoldForTextBox" value="${quotationForm.specialEqpmt}">
                                <html:optionsCollection name="spclList"/>
                            </html:select>
                            </td>
                            <td>&nbsp;</td>
                        </tr>
             <tr>
					<td class="textlabelsBold">Out of Gauge:</td>
                    <td class="textlabelsBold">
                    <html:radio property="outofgate" tabindex="-1" value="Y" name="quotationForm"/>Yes
             					  <html:radio property="outofgate" value="N" name="quotationForm"/>No
                    </td>
                    <td class="textlabelsBold">Is ${companyMnemonicCode} Filing AES&nbsp;
                        <html:radio property="aesFilling" tabindex="-1" name="quotationForm" value="true">
                                            Yes
                                        </html:radio>
                                         <html:radio property="aesFilling" name="quotationForm" value="false">
                                            No
                                        </html:radio>
                    </td>
                    <td>&nbsp;</td>
             </tr>
         </table></td>

     </tr>
     </table>
</td></tr>
<tr>
    <td align="center">
    </td>
</tr>
</table>

<br style="margin-top:5px"/>
<table width="100%"  border="0" class="tableBorderNew" cellpadding="0" cellspacing="0" >
   <tr>

  </tr>
 <%int i=0;
 String unitName="";
 String numbers="";
 String chargeCode="";
 String chargeCodeDesc="";
 String costType="";
 String currency="";
 String total="";
TransactionBean transactionBean=new TransactionBean();
transactionBean.setPrint("off");
String effectiveDate="";
String vendorName="";
String className="";
String vendorNo="";
String comment="";
String id="";
String amt="";
String markUp="";
String temp="";
String newFlag="";
Double totalAmount=0.00;
String chargeCodeCostType="";
List fcllist=new ArrayList();
if (request.getAttribute("transactionbean") != null) {
transactionBean = (TransactionBean) request.getAttribute("transactionbean");
}
%>

 <tr>
    <td align="left" id="expandRates"><div id="divtablesty1" style="border:thin;width:100%;">
    	  <%

    	 fcllist=(List)request.getAttribute("fclRates");
    	  if(fcllist!=null &&  fcllist.size()>0){ %>
            <table width="100%" border="0" cellpadding="3" cellspacing="0" class="displaytagstyle" id="ratesExpandTable">
            <thead><tr>
            	<td>
            		<%
				  if(userid!=null && hasUserLevelAccess){%>
				  <img src="<%=path%>/img/icons/up.gif" border="0" onclick="getExpand()"/>
				  <%} %>
                </td>
            	<td width="4%">Unit Select</td>
            	<td>Number</td>
            	<td>ChargeCode/CostType</td>
            	<td>Cost</td>
            	<td>MarkUp</td>
            	<td width="25%">Adjustment</td>
            	<td >Sell</td>
            	<td>Bundle<br> into OFR</td>
            	<td width="25%">Effective Date</td>
            	<td>Vendor Name</td>
            	<td width="25%">Vendor Number</td>
            	<td width="10%">Actions</td>
            	<td width="1%" class="bodybackgrnd"></td>
            	<td width="1%" class="bodybackgrnd"></td>
            	<td width="1%" class="bodybackgrnd"></td>
            <td width="1%" class="bodybackgrnd"></td></tr>
            </thead>
            <%
           className="odd";
            while(i<fcllist.size()){
             Charges charges=(Charges)fcllist.get(i);
             if(charges.getUnitName()!=null){
            	unitName=charges.getUnitName();
            }
            if(charges.getNumber()!=null){
            	numbers=charges.getNumber();
            }
            if(charges.getChargeCodeDesc()!=null){
            	chargeCode=charges.getChargeCodeDesc();
            }
            if(charges.getChgCode()!=null){
            	chargeCodeDesc=charges.getChgCode();
            }
            if(charges.getCostType()!=null){
            	costType=charges.getCostType();
            }
            if(charges.getCurrecny()!=null){
            	currency=charges.getCurrecny();
            }
            	total=numformat.format(charges.getAmount()+charges.getMarkUp());
            if(charges.getPrint()!=null){
            	transactionBean.setPrint(charges.getPrint());
            }
            	request.setAttribute("transactionbean",transactionBean);
            if(charges.getEfectiveDate()!=null){
            	effectiveDate=sdf.format(charges.getEfectiveDate());
            }
            if(charges.getAccountName()!=null){
            	vendorName=charges.getAccountName();
            }
            if(charges.getAccountNo()!=null){
            	vendorNo=charges.getAccountNo();
            }
            if(charges.getComment()!=null){
					comment=charges.getComment();
					}
            if(charges.getId()!=null){
            	id=charges.getId().toString();
            }
            if(charges.getAmount()!=null){
            	amt=numformat.format(charges.getAmount());
            }
            if(charges.getMarkUp()!=null){
            	markUp=numformat.format(charges.getMarkUp());
            }
            	if(charges.getChargeFlag()!=null && charges.getChargeFlag().equals("M")){
            		newFlag="new";
            	}else{
            		newFlag="";
            	}
            	chargeCodeCostType=chargeCode+"/"+chargeCodeDesc;
            	request.setAttribute("chargeCodeCostType",chargeCodeCostType);
            %>
            <tbody>
              <%if(!temp.equals(unitName) && !totalAmount.equals(0.0)){ %>
              <tr>
              <td></td>
              <td><b style="font-size:15;color:blue;">Total:</b></td>
              <td><b style="color:blue;">----------<b></td>
               <td><b style="font-size:15;color:blue;">----------------------------------<%=numformat.format(totalAmount)%><b></td>
              </tr>
              <%totalAmount=0.00;} %>
            <tr class="<%=className%>">
            	<td class="bodybackgrnd"></td>

            <%if(!temp.equals(unitName)){ %>
            	<td width="4%"><html:text property="unitType" value="<%=unitName%>" style="width:75%"/></td>
            	<td width="4%"><html:text property="numbers" value="<%=numbers%>" style="width:75%" onchange="numberChanged(this)"/></td>
            <%}else{ %>
           	 	<td style="visibility:hidden;width:12%"><html:text property="unitType" style="width:75%" value="<%=unitName%>"/></td>
           		<td style="visibility:hidden;width:12%"><html:text property="numbers" style="width:75%" value="<%=numbers%>"/></td>
            <%} %>
            <%if(newFlag.equals("new")){ %>
                <td><font color="red" size="3">*</font>
                	<span style="font-style: italic"><c:out value="${chargeCodeCostType}"/></span>
                </td>
            <%}else{ %>
            	<td><c:out value="${chargeCodeCostType}"/></td>
           	 <%} %>
     <td><html:text property="chargeAmount" value="<%=amt%>" readonly="true" size="5"/> </td>
	<td><html:text property="chargeMarkUp" maxlength="15" size="5" value="<%=markUp%>"/></td>
            <%totalAmount+=charges.getAmount()+charges.getMarkUp(); %>
                  	<td width="80%"><html:text property="adjestment"   value="" size="5" maxlength="8"></html:text></td>
            	<td width="80%"><html:text property="total" readonly="true"  value="<%=total%>" size="5"></html:text></td>
            	<td><html:checkbox property="print" name="transactionbean"/></td>
            	<td width="85%"><html:text property="effectiveDate" value="<%=effectiveDate%>" style="width:85%"/></td>
            	  <%if(newFlag.equals("new")){ %>
            	  <td><input type="text" name="accountname<%=i%>" id="accountname<%=i%>" onkeydown="getAccountName(this.value)"
      value="<%=vendorName%>" style="font-style: italic"/>

		   <div id="accountname<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("accountname<%=i%>","accountname<%=i%>_choices","","",
					"<%=path%>/actions/getCustomerName.jsp?tabName=QUOTE_NEW&from=0&index=<%=i%>&isDojo=false","");
			 </script>

		   </td>
		   <td width="90%"><input type="text" name="accountno<%=i%>" id="accountno<%=i%>" onkeydown="getAccountName(this.value)"
      value="<%=vendorNo%>" style="font-style: italic;width:90%"/>
		    <div id="accountno<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("accountno<%=i%>","accountno<%=i%>_choices","","",
					"<%=path%>/actions/getCustomer.jsp?tabName=QUOTE&from=0&index=<%=i%>&isDojo=false","");
			 </script>

		   </td>
            	  <%}else{ %>
            	<td> <input type="text" name="accountname<%=i%>" id="accountname<%=i%>" onkeydown="getAccountName(this.value)"
      value="<%=vendorName%>"/>
		    <div id="accountname<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("accountname<%=i%>","accountname<%=i%>_choices","","",
					"<%=path%>/actions/getCustomerName.jsp?tabName=QUOTE_NEW&from=0&index=<%=i%>&isDojo=false","");
			 </script>

		   </td>
		   <td width="90%"><input type="text" name="accountno<%=i%>" id="accountno<%=i%>" onkeydown="getAccountName(this.value)"
      value="<%=vendorNo%>" style="width:90%"/>

		     <div id="accountno<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("accountno<%=i%>","accountno<%=i%>_choices","","",
					"<%=path%>/actions/getCustomer.jsp?tabName=QUOTE&from=0&index=<%=i%>&isDojo=false","");
			 </script>

		   </td>
		   <%} %>


	<%if(chargeCode.equals("DRAY")||chargeCode.equals("INTMDL")||chargeCode.equals("INSURE")||chargeCode.equals("005")){%>
      <td></td>
	<%}else{ %>
	 <td><span class="hotspot" onmouseover="tooltip.show('<strong><%=comment %></strong>',null,event);"
         		 onmouseout="tooltip.hide();">
				<img src="<%=path%>/img/icons/delete.gif"/></span> </td>
	<td><span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);"
	onmouseout="tooltip.hide(); " style="color:black;">
	<img src="<%=path%>/img/icons/delete.gif" onclick="deleteArCharges('<%=id%>')"/></span></td>
	<%} %>


    <td class="bodybackgrnd" width="1%"><html:text property="chargeCddesc" style="font-style: italic;visibility:hidden" value="<%=chargeCode%>"></html:text></td>
    <td class="bodybackgrnd" width="1%"><html:text property="chargeCodes" style="font-style: italic;visibility:hidden" value="<%=chargeCodeDesc%>"></html:text></td>
    <td class="bodybackgrnd" width="1%"><html:text property="costType" style="font-style: italic;visibility:hidden" value="<%=costType%>"></html:text></td>
    <td class="bodybackgrnd" width="1%"><html:text property="currecny" value="<%=currency%>" style="width:60%;visibility:hidden"></html:text></td>
            </tr>
            </tbody>
            <%temp=unitName;
             if(className=="even"){
           className="odd";
          }else{
           className="even";
          }
            i++; }%>
             <tr>
              <td></td>
              <td><b style="font-size:15;color:blue;">Total:</b></td>
              <td><b style="color:blue;">----------<b></td>
               <td><b style="font-size:15;color:blue;">----------------------------------<%=numformat.format(totalAmount)%><b></td>
              </tr>
            </table>
            <%} %>
        </div></td>

 </tr>
 <tr>
    	<td align="left" id="collapseRates"><div id="divtablesty1" style="border:thin;overflow:scroll;width:100%;height:80%">
    	  <%
    	    temp="";
    	 i=0;
    	 totalAmount=0.00;
    	  	fcllist=(List)request.getAttribute("consolidatorList");
    	  if(fcllist!=null &&  fcllist.size()>0){ %>
            <table width="100%" border="0" cellpadding="3" cellspacing="0" class="displaytagstyle">
            <thead><tr>
            	<td>
            	   <%
				  if(userid!=null && hasUserLevelAccess){%>
				  <img src="<%=path%>/img/icons/up.gif" border="0" onclick="getExpand()"/>
				  <%} %>
            	</td>
            	<td width="4%">Unit Select</td>
            	<td>Number</td>
            	<td>ChargeCode/CostType</td>
            	<td width="25%">Sell</td>
            	<td>Bundle<br> into OFR</td>
            	<td width="25%">Effective Date</td>
            	<td>Vendor Name</td>
            	<td width="25%">Vendor Number</td>
            	<td width="10%">Actions</td>
            	<td width="1%" class="bodybackgrnd"></td>
            	<td width="1%" class="bodybackgrnd"></td>
            	<td width="1%" class="bodybackgrnd"></td>
            	<td width="1%" class="bodybackgrnd"></td>
            	<td width="1%" class="bodybackgrnd"></td>
            <td width="1%" class="bodybackgrnd"></td></tr>
            </thead>
            <%
           className="odd";
            while(i<fcllist.size()){
             Charges charges=(Charges)fcllist.get(i);
             if(charges.getUnitName()!=null){
            	unitName=charges.getUnitName();
            }
            if(charges.getNumber()!=null){
            	numbers=charges.getNumber();
            }
            if(charges.getChargeCodeDesc()!=null){
            	chargeCode=charges.getChargeCodeDesc();
            }
            if(charges.getChgCode()!=null){
            	chargeCodeDesc=charges.getChgCode();
            }
            if(charges.getCostType()!=null){
            	costType=charges.getCostType();
            }
            if(charges.getCurrecny()!=null){
            	currency=charges.getCurrecny();
            }
            	total=numformat.format(charges.getAmount()+charges.getMarkUp());
            if(charges.getPrint()!=null){
            	transactionBean.setPrint(charges.getPrint());
            }
            	request.setAttribute("transactionbean",transactionBean);
            if(charges.getEfectiveDate()!=null){
            	effectiveDate=sdf.format(charges.getEfectiveDate());
            }
            if(charges.getAccountName()!=null){
            	vendorName=charges.getAccountName();
            }
            if(charges.getAccountNo()!=null){
            	vendorNo=charges.getAccountNo();
            }
            if(charges.getComment()!=null){
					comment=charges.getComment();
					}
            if(charges.getId()!=null){
            	id=charges.getId().toString();
            }
            if(charges.getAmount()!=null){
            	amt=numformat.format(charges.getAmount());
            }
            if(charges.getMarkUp()!=null){
            	markUp=numformat.format(charges.getMarkUp());
            }
            	if(charges.getChargeFlag()!=null && charges.getChargeFlag().equals("M")){
            		newFlag="new";
            	}else{
            		newFlag="";
            	}
            	chargeCodeCostType=chargeCode+"/"+chargeCodeDesc;
            	request.setAttribute("chargeCodeCostType",chargeCodeCostType);
            %>
            <tbody>
              <%if(!temp.equals(unitName) && !totalAmount.equals(0.0)){ %>
              <tr>
              <td></td>
              <td><b style="font-size:15;color:blue;">Total:</b></td>
              <td><b style="color:blue;">----------<b></td>
               <td><b style="font-size:15;color:blue;">----------------------------------<%=numformat.format(totalAmount)%><b></td>
              </tr>
              <%totalAmount=0.00;} %>
            <tr class="<%=className%>">
               <td class="bodybackgrnd"></td>

            <%if(!temp.equals(unitName)){ %>
            	<td width="4%"><html:text property="unitType" value="<%=unitName%>" style="width:75%"/></td>
            	<td width="4%"><html:text property="numbers" value="<%=numbers%>" style="width:75%" onchange="numberChanged(this)"/></td>
            <%}else{ %>
           	 	<td style="visibility:hidden;width:12%"><html:text property="unitType" style="width:75%" value="<%=unitName%>"/></td>
           		 <td style="visibility:hidden;width:12%"><html:text property="numbers" style="width:75%" value="<%=numbers%>"/></td>
            <%} %>
            <%if(newFlag.equals("new")){ %>
            <td>
            	<font color="red" size="3">*</font>
            	<span style="font-style: italic"><c:out value="${chargeCodeCostType}"/></span>
            </td>

            <%}else{ %>
            	<td><c:out value="${chargeCodeCostType}"/></td>
           	 <%} %>
            <%totalAmount+=charges.getAmount()+charges.getMarkUp(); %>
            	<td width="80%"><html:text property="total" readonly="true" value="<%=total%>" style="width:80%"></html:text></td>
            	<td><html:checkbox property="print" name="transactionbean"/></td>
            	<td width="85%"><html:text property="effectiveDate" value="<%=effectiveDate%>" style="width:85%"/></td>
            	  <%if(newFlag.equals("new")){ %>
            	  <td><input type="text" name="accountname<%=i%>" id="accountname<%=i%>" onkeydown="getAccountName(this.value)"
      value="<%=vendorName%>" style="font-style: italic"/>
		    <div id="accountname<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("accountname<%=i%>","accountname<%=i%>_choices","","",
					"<%=path%>/actions/getCustomerName.jsp?tabName=QUOTE_NEW&from=0&index=<%=i%>&isDojo=false","");
			 </script>

		   </td>
		   <td width="90%"><input type="text" name="accountno<%=i%>" id="accountno<%=i%>" onkeydown="getAccountName(this.value)"
      value="<%=vendorNo%>" style="font-style: italic;width:90%"/>
		   <div id="accountno<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("accountno<%=i%>","accountno<%=i%>_choices","","",
					"<%=path%>/actions/getCustomer.jsp?tabName=QUOTE&from=0&index=<%=i%>&isDojo=false","");
			 </script>

		   </td>
            	  <%}else{ %>
            	<td> <input type="text" name="accountname<%=i%>" id="accountname<%=i%>" onkeydown="getAccountName(this.value)"
      value="<%=vendorName%>"/>
		   <div id="accountname<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("accountname<%=i%>","accountname<%=i%>_choices","","",
					"<%=path%>/actions/getCustomerName.jsp?tabName=QUOTE_NEW&from=0&index=<%=i%>&isDojo=false","");
			 </script>
		   </td>
		   <td width="90%"><input type="text" name="accountno<%=i%>" id="accountno<%=i%>" onkeydown="getAccountName(this.value)"
      value="<%=vendorNo%>" style="width:90%"/>
		    <div id="accountno<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("accountno<%=i%>","accountno<%=i%>_choices","","",
					"<%=path%>/actions/getCustomer.jsp?tabName=QUOTE&from=0&index=<%=i%>&isDojo=false","");
			 </script>

		   </td>
		   <%} %>


	<%if(chargeCode.equals("DRAY")||chargeCode.equals("INTMDL")||chargeCode.equals("INSURE")||chargeCode.equals("005")){%>
      <td></td>
	<%}else{ %>
	 <td><span class="hotspot" onmouseover="tooltip.show('<strong><%=comment %></strong>',null,event);"
         		 onmouseout="tooltip.hide();">
				<img src="<%=path%>/img/icons/delete.gif"/></span> </td>
	<td><span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" style="color:black;" onmouseout="tooltip.hide();">
	<img src="<%=path%>/img/icons/delete.gif" onclick="deleteArCharges('<%=id%>')"/></span></td>
	<%} %>

	<td class="bodybackgrnd" width="1%"><html:text property="chargeAmount" value="<%=amt%>" readonly="true" size="1" style="visibility:hidden"/> </td>
	<td class="bodybackgrnd" width="1%"><html:text property="hiddenchargeMarkUp" maxlength="15" size="1" value="<%=markUp%>" style="visibility:hidden"/></td>
    <td class="bodybackgrnd" width="1%"><html:text property="chargeCddesc" style="font-style: italic;visibility:hidden" value="<%=chargeCode%>"></html:text></td>
    <td class="bodybackgrnd" width="1%"><html:text property="chargeCodes" style="font-style: italic;visibility:hidden" value="<%=chargeCodeDesc%>"></html:text></td>
    <td class="bodybackgrnd" width="1%"><html:text property="costType" style="font-style: italic;visibility:hidden" value="<%=costType%>"></html:text></td>
    <td class="bodybackgrnd" width="1%"><html:text property="currecny" value="<%=currency%>" style="width:60%;visibility:hidden"></html:text></td>
            </tr>
            </tbody>
            <%temp=unitName;
             if(className=="even"){
           className="odd";
          }else{
           className="even";
          }
            i++; }%>
             <tr>
              <td></td>
              <td><b style="font-size:15;color:blue;">Total:</b></td>
              <td><b style="color:blue;">----------<b></td>
               <td><b style="font-size:15;color:blue;">----------------------------------<%=numformat.format(totalAmount)%><b></td>
              </tr>
            </table>
            <%} %>
        </div></td>


 </tr>
</table>
 <table width="100%">
 <% List perkglbsList=(List)request.getAttribute("perkglbsList");
	i=0;
	newFlag="";
	chargeCode="";
	chargeCodeDesc="";
	costType="";
	amt="";
	markUp="";
	currency="";
	chargeCodeCostType="";
	className="";
%>
<%
	if(perkglbsList!=null && perkglbsList.size()>0){
%>
    <tr>
		<td align="left" >
			<div id="divtablesty1" style="border:thin;overflow:auto;width:100%;height:100%"					pagesize="<%=pageSize%>">
			<table width="80%" border="0" cellpadding="0" cellspacing="0" class="displaytagstyle">
				<thead>
					<td></td>
					<td>Charge Code/Cost type</td>
					<td width="25%">Rate</td>
					<td width="25%">Minimum</td>
					<td class="bodybackgrnd"></td>
					<td class="bodybackgrnd"></td>
					<td class="bodybackgrnd"></td>
					<td class="bodybackgrnd"></td>
				</thead>
<%
			className="odd";
            while(i<perkglbsList.size()){
            Charges charges=(Charges)perkglbsList.get(i);
            if(charges.getChargeFlag()!=null && charges.getChargeFlag().equals("M")){
            newFlag="new";
            }
            if(charges.getChargeCodeDesc()!=null){
            chargeCode=charges.getChargeCodeDesc();
            }
            if(charges.getChgCode()!=null){
            chargeCodeDesc=charges.getChgCode();
            }
            if(charges.getCostType()!=null){
            costType=charges.getCostType();
            }
            if(charges.getRetail()!=null){
            amt=numformat.format(charges.getRetail());
            }
            if(charges.getMinimum()!=null){
            markUp=numformat.format(charges.getMinimum());
            }
            if(charges.getCurrecny()!=null){
            currency=charges.getCurrecny();
            }
            chargeCodeCostType=chargeCode+"/"+chargeCodeDesc;
            request.setAttribute("chargeCodeCostType",chargeCodeCostType);
%>
           <tbody>
           <tr class="<%=className%>">
           <td class="bodybackgrnd"></td>
<%
	if(newFlag.equals("new")){
%>
           <td><font color="red" size="3">*</font>
            	<span style="font-style: italic"><c:out value="${chargeCodeCostType}"/></span>
           </td>
<%
	}else{
%>
            <td><c:out value="${chargeCodeCostType}"/></td>
<%
	}
%>

           <td width="50%"><html:text property="perActiveAmt" value="<%=amt%>" readonly="true" style="width:50%"/></td>
           <td width="50%"><html:text property="perMinimum" value="<%=markUp%>" readonly="true" style="width:50%"/></td>
           <td><html:text property="percurrecny" value="<%=currency%>" style="visibility:hidden"/></td>
           <td><html:text property="perChargeCode" value="<%=chargeCode%>" style="visibility:hidden"/></td>
           <td><html:text property="perChargeCodeDesc" value="<%=chargeCodeDesc%>" style="visibility:hidden"/></td>
           <td><html:text property="perCostType" value="<%=costType%>" style="visibility:hidden"/></td>
           </tr>
            </tbody>
<%
     if(className=="even"){
           className="odd";
     }else{
           className="even";
     }
     i++;
	 }
%>
    </table>
 	</div>
	</td>
    </tr>
<%
	}
%>
</table>

 <table>
  <%i=0;
  newFlag="";
  chargeCode="";
  chargeCodeDesc="";
  costType="";
  currency="";
  amt="";
  effectiveDate="";
  vendorName="";
  vendorNo="";
  id="";
   TransactionBean transBean=new TransactionBean();
   transBean.setOtherprint("on");
   List otherChargesList=(List)request.getAttribute("otherChargesList");
   %>
   <%if(otherChargesList != null && otherChargesList.size()>0) {%>
  <tr  class="tableHeadingNew">File Level Charges</tr>
   <tr>
          <td align="left" ><div id="divtablesty1" style="border:thin;overflow:auto;width:100%;height:80%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="displaytagstyle">
            <thead><tr>
            	<td></td>
           		<td>Charge Code/Cost Type</td>
            	<td width="35%">Retail</td>
              	<td>Bundle <br>into OFR</br></td>
             	<td width="85%">Effective Date</td>
            	<td>Vendor Name</td>
             	<td width="25%">Vendor Number</td>
             	<td>Actions</td>
              	<td class="bodybackgrnd"></td>
               	<td class="bodybackgrnd"></td>
           		<td class="bodybackgrnd"></td>
            <td class="bodybackgrnd"></td></tr>
            </thead>
           <%
            className="odd";
           while(i<otherChargesList.size()){
            Charges charges=(Charges)otherChargesList.get(i);
             if(charges.getChargeFlag()!=null && charges.getChargeFlag().equals("M")){
            newFlag="new";
            }
            if(charges.getChargeCodeDesc()!=null){
            chargeCode=charges.getChargeCodeDesc();
            }
            if(charges.getChgCode()!=null){
            chargeCodeDesc=charges.getChgCode();
            }
            if(charges.getCostType()!=null){
            costType=charges.getCostType();
            }
            if(charges.getOtherprint()!=null){
             transBean.setOtherprint(charges.getOtherprint());
            }
            if(charges.getEfectiveDate()!=null){
            effectiveDate=sdf.format(charges.getEfectiveDate());
            }
            if(charges.getAccountName()!=null){
            vendorName=charges.getAccountName();
            }
            if(charges.getAccountNo()!=null){
            vendorNo=charges.getAccountNo();
            }
            if(charges.getComment()!=null){
					comment=charges.getComment();
					}
            if(charges.getCurrecny()!=null){
            currency=charges.getCurrecny();
            }
    		 request.setAttribute("transactionbean",transBean);
    		 if(charges.getRetail()!=null){
    		 amt=numformat.format(charges.getRetail());
    		 }
    		 if(charges.getId()!=null){
    		 id=charges.getId().toString();
    		 }
             chargeCodeCostType=chargeCode+"/"+chargeCodeDesc;
            request.setAttribute("chargeCodeCostType",chargeCodeCostType);
            %>
           <tbody>
           <tr class="<%=className%>">
           		<td class="bodybackgrnd"></td>
            <%if(newFlag.equals("new")){ %>
            <td>
            	<font color="red" size="3">*</font>
            	<c:out value="${chargeCodeCostType}"/>
            </td>

            <%}else{ %>
            	<td><c:out value="${chargeCodeCostType}"/></td>
           	 <%} %>
           	 <td width="70%"><html:text property="retail" value="<%=amt%>" style="width:70%"/></td>
           	<td><html:checkbox property="otherprint" name="transactionbean"/></td>
           	<td width="85%"><html:text property="otherEffectiveDate" value="<%=effectiveDate%>" style="width:85%"></html:text></td>
           	  <%if(newFlag.equals("new")){ %>
           	  <td><input type="text" name="otheraccountname<%=i%>" id="otheraccountname<%=i%>"
      onkeydown="getAccountName(this.value)" value="<%=vendorName%>" style="font-style: italic"/>

	  <div id="otheraccountname<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
			 <script type="text/javascript">
					initOPSAutocomplete("otheraccountname<%=i%>","otheraccountname<%=i%>_choices","","",
					"<%=path%>/actions/getCustomerName.jsp?tabName=QUOTE_NEW&from=5&index=<%=i%>&isDojo=false","");
			 </script>

       </td>
           	  <td width="90%"> <input type="text" name="otheraccountno<%=i%>" id="otheraccountno<%=i%>"   value="<%=vendorNo%>"
      onkeydown="getAccountName(this.value)" style="font-style: italic;width:85%"/>
	   <div id="otheraccountno<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
		 <script type="text/javascript">
				initOPSAutocomplete("otheraccountno<%=i%>","otheraccountno<%=i%>_choices","","",
				"<%=path%>/actions/getCustomer.jsp?tabName=QUOTE&from=5&index=<%=i%>&isDojo=false","");
		 </script>

       </td>
           	  <%}else{ %>
           	  <td><input type="text" name="otheraccountname<%=i%>" id="otheraccountname<%=i%>"
      onkeydown="getAccountName(this.value)" value="<%=vendorName%>" />
	   <div id="otheraccountname<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
		 <script type="text/javascript">
				initOPSAutocomplete("otheraccountname<%=i%>","otheraccountname<%=i%>_choices","","",
				"<%=path%>/actions/getCustomerName.jsp?tabName=QUOTE_NEW&from=5&index=<%=i%>&isDojo=false","");
		 </script>

       </td>
           	  <td width="90%"> <input type="text" name="otheraccountno<%=i%>" id="otheraccountno<%=i%>"   value="<%=vendorNo%>"
      onkeydown="getAccountName(this.value)" style="width:85%"/>

	   <div id="otheraccountno<%=i%>_choices"  style="display: none;width: 5px;"  align="right"  class="autocomplete"></div>
		 <script type="text/javascript">
				initOPSAutocomplete("otheraccountno<%=i%>","otheraccountno<%=i%>_choices","","",
				"<%=path%>/actions/getCustomer.jsp?tabName=QUOTE&from=5&index=<%=i%>&isDojo=false","");
		 </script>

       </td>
           	  <%} %>
           	   <td><span class="hotspot" onmouseover="tooltip.show('<strong><%=comment %></strong>',null,event);"
         		 onmouseout="tooltip.hide();">
				<img src="<%=path%>/img/icons/delete.gif"/></span> </td>

           	  <td><span class="hotspot" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" style="color:black;"  onmouseout="tooltip.hide();">
	<img src="<%=path%>/img/icons/delete.gif" onclick="deleteArCharges('<%=id%>')" id="deleteimg" /></span></td>
	<td><html:text property="othercurrecny" value="<%=currency%>" style="visibility:hidden"/></td>
           	  <td><html:text property="otherchargeCddesc" value="<%=chargeCode%>" style="visibility:hidden"/></td>
           	<td><html:text property="chargeCd" value="<%=chargeCodeDesc%>" style="visibility:hidden"/></td>
           	<td><html:text property="costType1" value="<%=costType%>" style="visibility:hidden"/></td>
           </tr>
           </tbody>
           <% if(className=="even"){
           className="odd";
          }else{
           className="even";
          }
          i++;
          } %>
            </table>
           </div></td>
           </tr>
           <%} %>
  </table>
  <table   border="0" cellpadding="0"  cellspacing="0"  class="tableBorderNew" width="100%">
<tr class="tableHeadingNew" ><td>End of Quote</td></tr>
  </table>
  	<html:hidden property="numbIdx" />
  	<html:hidden property="numbers1" />
  	<html:hidden property="check"/>
    <html:hidden property="check1"/>
    <html:hidden property="check2"/>
    <html:hidden property="selectedCheck"/>
    <html:hidden property="unitSelect"/>
		<html:hidden property="number"/>
		<html:hidden property="chargeCode"/>
		<html:hidden property="chargeCodeDesc"/>
		<html:hidden property="costSelect"/>
		<html:hidden property="currency"/>
		<html:hidden property="chargeAmt"/>
		<html:hidden property="minimumAmt"/>
   <html:hidden property="check3"/>
   <html:hidden property="buttonValue" styleId="buttonValue"/>
   <html:hidden property="imsTrucker" styleId="imsTrucker"/>
    <html:hidden property="imsBuy" styleId="imsBuy"/>
    <html:hidden property="imsSell" styleId="imsSell"/>
    <html:hidden property="imsQuoteNo" styleId="imsQuoteNo"/>
    <html:hidden property="imsLocation" styleId="imsLocation"/>
   <html:hidden property="quotationDate" value="<%=quoteDate%>"/>
   <html:hidden property="quoteBy" value="<%=quoteBy%>"/>
   <html:hidden property="button" value="<%=buttonValue%>"/>
   <html:hidden property="quotationNo" value="<%=quotationNo%>"/>
   <html:hidden property="quoteId" value="${quotationForm.quoteId}"/>
   <html:hidden property="focusValue" />
   <html:hidden property="remarks" value="${quotationForm.remarks}"/>
   <html:hidden property="ratesRemarks" value="${quotationForm.ratesRemarks}"/>
   <html:hidden property="docChargeAmount" value="${quotationForm.docChargeAmount}" styleId="docChargeAmount"/>
   <html:hidden property="ssline"/>
   <html:hidden property="unitselected"/>
   <html:hidden property="insuranceAmount"/>
   <html:hidden property="selectedOrigin"/>
   <html:hidden property="selectedDestination"/>
   <html:hidden property="selectedComCode"/>
   <html:hidden property="imsOrigin"/>
   <html:hidden property="localdryage"/>
   <html:hidden property="importFlag" />
   <html:hidden property="clientNumber" styleId="clientNumberHidden" value="${quotationForm.clientNumber}"/>
   <input type="hidden" id="importFlag" value="${importFlag}"/>
   <input type="hidden" id="companyCode" value="${companyCode}"/>
   <script type="text/javascript">
    load();
    makeClientTypeBorderLess();
    setFocus('${focusValue}','${buttonValue}');
    tabMoveAfterDeleteRates('${importFlag}');
    </script>

</html:form>

<table width="100%" border="0" cellpadding="0" cellspacing="0" >
<tr><td colspan="8">
	 <input type="button" id="cancel" value="Go Back" onclick="compareWithOldArray()" class="buttonStyleNew" />
	 <span class="hotspot"
		onmouseover="tooltip.showTopText('<strong><%=mandatoryFieldForQuotes%></strong>',null,event);" onmouseout="tooltip.hide();">
	 <input type="button" id="save1" value="Save" onclick="SAVE('${importFlag}')" class="buttonStyleNew"/>
          <input type="button" id="Options" value="Options" onclick="quoteOptions()" class="buttonStyleNew" />
	 </span>
</td></tr>
</table>
</body>

 <script type="text/javascript">
 	function setFocusForBlurToWork(){
 		if(event.keyCode == 9 || event.keyCode == 13 || event.keyCode==0){
 			setTimeout("secondFocus()",150);

 		}
 	}
 	function secondFocus(){
 		document.getElementById('isTerminal').focus();
 		document.getElementById('isTerminal').select();
 	}
 	function submitComments(quoteId,userName,date,commentType){
	 	var comments = document.getElementById(commentType).innerHTML;
	 	if(trim(comments)!=""){
                        if(comments.length <= 460 && comments!=''){
                            comments=comments+'('+userName+'-'+date+').';
                        }
                        jQuery.ajaxx({
                            data: {
                                className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                methodName : "saveIntermodelComments",
                                param1 : quoteId,
                                param2 : comments,
                                param3 : commentType
                            },
                            success: function (data) {
                                if(jQuery.trim(data) !== "" && jQuery.trim(data) !== "null"){
                                    document.QuotesForm.intermodelComments.value=data;
                                    document.getElementById('localDrayageCommentToolTipDiv').style.display="block";
                                    document.getElementById('override').style.display="block";
                                    closeCommentDiv();
                                    var commentType=document.getElementById("commentType").value;
                                    if(commentType=='localdrayage'){
                                        document.QuotesForm.inland[1].checked=true;
                                    }else{
                                        document.QuotesForm.inland[1].checked=true;
                                    }
                                }
                            }
                        });
	 	}else{
	 		alert("Please Enter Comments!!!");
	 	}
 	}
 	function showToolTip(chargeType){
            var comment;
            if(chargeType == 'intermodel'){
 		 comment = document.QuotesForm.intermodelComments.value;
            }else{
 		 comment = document.QuotesForm.intermodelComments.value;
            }
            tooltip.showComments(comment);
 	}
 	function closeCommentDiv(){
 		closePopUp();
 		document.getElementById("localDrayageCommentDiv").style.display = 'none';
 	}
 	function displayToolTipDiv(){
 	    var quoteId = '${quotationForm.quoteId}';
 	    if(null!=quoteId){
                jQuery.ajaxx({
                    data: {
                        className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName : "getIntermodelComments",
                        param1 : quoteId
                    },
                    success: function(data) {
                        if(jQuery.trim(data) !== "" && jQuery.trim(data) !== "null"){
                            document.QuotesForm.intermodelComments.value = data;
                            document.getElementById('localDrayageCommentToolTipDiv').style.display="block";
                            document.getElementById('override').style.display="block";
                        }
                    }
                });
 	    }
 	}
        function defaultAgentforDesc(){
            if(document.QuotesForm.defaultAgent[0].checked){
                fillDefaultAgent();
            }
        }
 	function fillDefaultAgent(){
               document.getElementById("agent").className = "textlabelsBoldForTextBox";
               document.getElementById("routedAgentCheck").className = "dropdown_accounting";
               document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";
                jQuery("#directConsignmentN").attr("checked",true);
                jQuery("#directConsignmentY").attr("checked",false);
                jQuery("#agent").attr("disabled",false);
                jQuery("#routedAgentCheck").attr("disabled",false);
                jQuery("#routedbymsg").attr("disabled",false);
		var pod=document.QuotesForm.portofDischarge.value;
		if(pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1){
                    var podNew = pod.substring(pod.lastIndexOf("(")+1, pod.lastIndexOf(")"));
                    jQuery.ajaxx({
                        dataType : "json",
                        data: {
                            className : "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                            methodName : "getDefaultAgent",
                            param1 : podNew,
                            param2 : "${importFlag}" === "true" ? "I" : "F",
                            dataType : "json"
                        },
                        success: function(data) {
                            defaultAgentFill(data);
                        }
                    });
                }
	}
    function defaultFromCustgeneral(ert){
        var pod=document.QuotesForm.portofDischarge.value;
        if(pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1){
            var podNew = pod.substring(pod.lastIndexOf("(")+1, pod.lastIndexOf(")"));
            jQuery.ajaxx({
                dataType : "json",
                data: {
                    className : "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                    methodName : "getDefaultAgent",
                    param1 : podNew,
                    param2 : "${importFlag}" === "true" ? "I" : "F",
                    dataType : "json"
                },
                success: function(data) {
                    if(null !== data && null!== data.accountno && jQuery.trim(data.accountno) !== ""){
                        jQuery.ajaxx({
                            data: {
                                className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                methodName : "checkForDisable",
                                param1 : data.accountno
                            },
                            success: function(dataNew) {
                                if(jQuery.trim(dataNew) !== ""){
                                    if(document.getElementById("nonRated").checked){
                                        document.QuotesForm.buttonValue.value="applyDefaultValues";
                                        document.QuotesForm.submit();
                                    }else{
                                        alertNew("Default Agent customer is disabled");
                                    }
                                    document.QuotesForm.defaultAgent[1].checked = true;
                                }else{
                                    if (data.accountName != undefined && data.accountName != "" && data.accountName != null) {
                                        document.getElementById("agent").value = data.accountName;
                                        document.getElementById("agent_check").value = data.accountName;
                                        document.QuotesForm.defaultAgent[0].checked = true;
                                    } else {
                                        document.getElementById("agent").value = "";
                                        document.QuotesForm.defaultAgent[0].checked = false;
                                    }
                                    if (data.accountno != undefined && data.accountno != "" && data.accountno != null) {
                                        document.getElementById("agentNo").value = data.accountno;
                                    } else {
                                        document.getElementById("agentNo").value = "";
                                    }
                                     if(ert == 'yes'){
                                        document.getElementById('routedbymsg').value = data.accountno;
                                        document.getElementById('routedbymsg_check').value = data.accountno;
                                    }else{
                                        document.getElementById('routedbymsg').value = "";
                                        document.getElementById('routedbymsg_check').value = "";
                                    }
                                    if(document.getElementById("nonRated").checked){
                                        document.QuotesForm.buttonValue.value="applyDefaultValues";
                                        document.QuotesForm.submit();
                                    }
                                }
                            }
                    });
                    document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
                    document.getElementById("agent").readOnly=true;
                }else{
                    if(document.getElementById("nonRated").checked){
                        document.QuotesForm.buttonValue.value="applyDefaultValues";
                        document.QuotesForm.submit();
                    }
                }
                }
            });
        }else{
             if(document.getElementById("nonRated").checked){
                document.QuotesForm.buttonValue.value="applyDefaultValues";
                document.QuotesForm.submit();
            }
        }
    }
         function directConsignmnt(){
               document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
               document.getElementById("routedAgentCheck").className = "dropdown_accountingDisabled";
               document.getElementById("routedbymsg").className = "textlabelsBoldForTextBoxDisabledLook";
                jQuery("#defaultAgentN").attr("checked", true);
                jQuery("#routedAgentCheck").val("");
                jQuery("#routedbymsg").val("");
                jQuery("#agent").val("");
                jQuery("#agentNo").val("");
                jQuery("#agent").attr("disabled",true);
                jQuery("#routedAgentCheck").attr("disabled",true);
                jQuery("#routedbymsg").attr("disabled",true);
            }
        function directConsignmntNo(){
               document.getElementById("agent").className = "textlabelsBoldForTextBox";
               document.getElementById("routedAgentCheck").className = "dropdown_accounting";
               document.getElementById("routedbymsg").className = "textlabelsBoldForTextBox";               
                jQuery("#defaultAgentY").attr("checked", true);
                jQuery("#directConsignmentN").attr("checked", true);
                fillDefaultAgent();
            }
    function defaultAgentFill(data) {
	if(null !== data && null !== data.accounno && jQuery.trim(data.accountno) !== ""){
            jQuery.ajaxx({
                data: {
                    className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName : "checkForDisable",
                    param1 : data.accountno
                },
                success: function(dataNew) {
                    if(jQuery.trim(dataNew) !== ""){
                       alertNew(dataNew.replace('This','Default Agent'));
                        document.QuotesForm.defaultAgent[1].checked = true;
                    }else{
                        document.getElementById("agent").value = data.accountName;
                        document.getElementById("agent_check").value = data.accountName;
                        document.getElementById("agentNo").value = data.accountno;
                    }
                }
            });
            document.getElementById("agent").className = "textlabelsBoldForTextBoxDisabledLook";
            document.getElementById("agent").readOnly=true;
            var selectedErt = document.QuotesForm.routedAgentCheck.value;
            jQuery.ajaxx({
                dataType : "json",
                data: {
                    className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName : "getDefaultDetails",
                    param1 : document.getElementById("clientNumber").value,
                    dataType : "json"
                },
                success: function(obj) {
                    if((document.QuotesForm.routedAgentCheck.value == "yes"||document.QuotesForm.routedAgentCheck.value == "no")
                        && !(null != obj && obj.ert != '' && selectedErt == obj.ert)){
                        document.getElementById("routedbymsg").value="";
                        document.getElementById("routedbymsg").className="textlabelsBoldForTextBox";
                        document.QuotesForm.routedAgentCheck.value = "";
                    }
                }
            });
	}
}

function clearValues() {
    document.getElementById("agent").value = "";
    document.getElementById("agent").readOnly=false;
    document.getElementById("agentNo").value = "";
    document.getElementById("agent").className = "textlabelsBoldForTextBox";
    document.getElementById("routedbymsg").readOnly=false;
    var selectedErt = document.QuotesForm.routedAgentCheck.value;
    jQuery.ajaxx({
        dataType : "json",
        data: {
            className : "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName : "getDefaultDetails",
            param1 : document.getElementById("clientNumber").value,
            dataType : "json"
        },
        success: function(obj) {
            if(null != obj && obj.ert != '' && selectedErt == obj.ert){
                document.getElementById("routedbymsg").value="";
                document.getElementById("routedbymsg").className="textlabelsBoldForTextBox";
            }else if(document.QuotesForm.routedAgentCheck.value == "yes"||document.QuotesForm.routedAgentCheck.value == "no") {
                document.getElementById("agent").readOnly=false;
                document.getElementById("routedbymsg").value="";
                document.getElementById("routedbymsg").className="textlabelsBoldForTextBox";
                document.QuotesForm.routedAgentCheck.value = "";
            }
        }
    });

}
 	setDefaultAgent('<%=companyCode%>');
 	disableAutoFF();
 </script>
 <style>
	#localDrayageCommentDiv {
	 position: fixed;
	  _position: absolute;
	  z-index: 99;
	  border-style:solid solid solid solid;
      background-color: white;
 	  left: 25%;
      right: 70%;
      top: 75%;
      bottom: 25%;
	  _height: expression(document.body.offsetHeight + "px");
	}

	</style>
    <style>
	#originAndDestinationDiv {
	position: fixed;
    _position: absolute;
    z-index: 99;
    border-style:solid;
    border-width:2px;
    border-color:#808080;
    padding:0px 0px 0px 0px;
    background-color: #FFFFFF;
    left:10px;
    right:5px;
    top:0;
    margin:0 auto;
	}

	</style>
    <script type="text/javascript" src="<%=path%>/js/fcl/clientSearch.js"></script>
</html>
