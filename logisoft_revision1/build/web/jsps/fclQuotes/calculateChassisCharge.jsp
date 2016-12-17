<%@page language="java"
	import="java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%> 
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../fragment/formSerialize.jspf"  %>
<%@include file="../includes/jspVariables.jsp"  %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
 

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
<script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
<script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
<script language="javascript" src="<%=path%>/js/common.js"></script>
 <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function(){
   var valueInlandForQuote = parent.parent.jQuery("#quoteId").val();
   var valueInlandForBooking = parent.parent.jQuery("#bkgId").val();
   var valueInland = parent.parent.jQuery("#moduleRefId").val();
 
        if(valueInlandForQuote !==undefined && valueInlandForBooking !==undefined) {
            toGetInlandVendorForBooking(valueInland);
        } else {
            toGetInlandVendorForQoute(valueInland);
        }
   
    });
    function toGetInlandVendorForQoute(valueInland){
           jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkvendorForChassis",
            param1: valueInland,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            
            if (data !== '' || data !== null) {
              jQuery("#vendorName").val(data[0]);
              jQuery("#vendorNumber").val(data[1]);
            }
        }
    });
    }
     function toGetInlandVendorForBooking(valueInland){
           jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
            methodName: "checkvendorForChassis",
            param1: valueInland,
            dataType: "json"
        },
        async: false,
        success: function (data) {
            
            if (data !== '' || data !== null) {
              jQuery("#vendorName").val(data[0]);
              jQuery("#vendorNumber").val(data[1]);
            }
        }
    });
    }
function getChassisCharge(){
  var costValue = document.getElementById("chassisCostCharge").value;
  var sellValue = document.getElementById("chassissellCharge").value;
  var vendornam = document.getElementById("vendorName").value;
  var vendoracct = document.getElementById("vendorNumber").value;
  
  if(vendornam === '' || vendoracct === ''|| sellValue === '' || costValue === ''){
      alertNew("Enter Vendor name");
  }else{
  parent.parent.call();
 parent.parent.GB_hide();
 
 parent.parent.calculateChassisCharge(costValue,sellValue,vendornam,vendoracct);
  }
}
function load(){
 setTimeout("set()",150);
}
function set(){
  document.getElementById('chassis').focus();
}
function checkForNumberAndDecimal(obj){
                     if(!/^([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)){
                        obj.value="";
                        alertNew("The amount you entered is not a valid");
                        return;
        }
    }
    function disableBackSpace() {
        if (event.keyCode == 8)
            return false;
        return true;
    }
    function checkDecimal(ev) {
        if (ev.value > 100) {
            ev.value = ev.value.substring(0, 2) + "." + ev.value.substring(2, 4);
        } else if (ev.value == 100) {
            ev.value = ev.value.substring(0, 3) + "." + ev.value.substring(4, 6);
        } else if (ev.value.indexOf(".") != -1) {
            ev.value = ev.value.substring(0, ev.value.indexOf(".")) + "." + ev.value.substring(ev.value.indexOf(".") + 1, ev.value.indexOf(".") + 3);
        }
    }
    function allowOnlyWholeNumbers(obj) {
        var result;
        if (!/^[1-9 . ]\d*$/.test(obj.value)) {
            result = obj.value.replace(/[^0-9 . ]+/g, '');
            obj.value = result;
            return false;
        }
        return true;
    }

    
</script>
<%
request.setAttribute("chassiscost",LoadLogisoftProperties.getProperty("chassis.cost"));
request.setAttribute("chassissell",LoadLogisoftProperties.getProperty("chassis.sell"));

%>
</head>

 
  <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;">
                <b>Alert</b>
            </p>
            <p id="innerText" class="containerForAlert"
               style="width: 100%;padding-left: 3px;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display='none';
                           grayOut(false,'');">
            </form>
        </div>
<body class="whitebackgrnd" onload="load()">
<form name="portSearch" type="com.gp.cvst.logisoft.struts.form.SearchQuotationForm">

	<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		  <tr class="tableHeadingNew">
		     <td colspan="2">Calculate Chassis Charge</td>
		  </tr>
                  <tr><td>&nbsp;</td></tr>
		  <tr><td>&nbsp;</td></tr>
		  <tr class="textlabelsBold">


                                <td id="text1" width="6%">
                                        Vendor Name

                                        </td>
                                            <td id="vendorName1">
                                            <input type="text" size="20" name="vendorName" id="vendorName" class="textlabelsBoldForTextBox"
                                                   value="" />
                                    <input name="custname_check" id="custname_check"   type="hidden" />
                                    <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                    <script type="text/javascript">
                                                initAutocompleteWithFormClear("vendorName", "custname_choices", "vendorNumber", "custname_check",
                                    "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=5&acctTyp=V","checkForDisable()");
                                            </script>
                                        </td>
                                     <td id="text2" style="padding-left:5px;">Vendor Number</td>
                                    <td>
                                        <input name="vendorNumber" Class="BackgrndColorForTextBox"
                                               size="11"  readonly="readonly" tabindex="-1" id="vendorNumber"/>
                                    </td>

                          <td>Cost</td>
			<td><input type="text" name="amount" size="10" align="right"  id="chassisCostCharge"  maxlength="8"
		      		value="${chassiscost}" class="textlabelsBoldForTextBox" onchange="checkForNumberAndDecimal(this)" /></td>
                      <td>Sell</td>
			<td><input type="text" name="markup" size="10" align="right"  id="chassissellCharge"  maxlength="8"
		      		value="${chassissell}" class="textlabelsBoldForTextBox" onchange="checkForNumberAndDecimal(this)" /></td>
		  </tr>

		  <tr>
                      <td>&nbsp;</td>
		  <td>&nbsp;</td>
		     <td align="center" colspan="2"><input type="button" class="buttonStyleNew" value="Submit" onclick="getChassisCharge()" /></td>
		  </tr>
	</table>
</form>
</body>