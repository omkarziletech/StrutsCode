<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String insuranceCharge="0.80";
String costOfGoods="";
String markUp="";
String totalCostOfGoods="";
String cost = request.getParameter("costOfGoods");
String insAmt = request.getParameter("insuranceAmount");
NumberFormat numberFormat = new DecimalFormat("########0.00");
NumberFormat numberFormat1 = new DecimalFormat("########0");
if(cost!=null && !cost.equals("")){
  costOfGoods=numberFormat1.format(Double.parseDouble(cost.replace(",", "")));
}
if(insAmt!=null && !insAmt.equals("0.0") && !insAmt.equals("0.00") && !insAmt.equals("")){
    insuranceCharge=numberFormat.format(Double.parseDouble(insAmt.replace(",", "")));
}
if(costOfGoods.contains(",")){
  costOfGoods=costOfGoods.replace(",","");
}
if(request.getParameter("markUp")!=null){
  markUp=request.getParameter("markUp");
}
if(markUp.contains(",")){
  markUp=markUp.replace(",","");
}
if(!costOfGoods.equals("")){
    if(null != markUp && !markUp.equals("")){
      totalCostOfGoods=numberFormat.format((Double.parseDouble(costOfGoods)+Double.parseDouble(markUp)));
    }else{
      totalCostOfGoods=numberFormat1.format(Double.parseDouble(costOfGoods));
    }
}
%>
<head>

<title>FCL Quotation Rates</title>
<%@include file="../includes/baseResources.jsp" %>
<script language="javascript" src="<%=path%>/js/common.js"></script>
<script language="javascript" type="text/javascript">
function getInsurance(){
 parent.parent.GB_hide();
 parent.parent.calculateInsurance(document.getElementById("costOfGoods").value,document.getElementById("insuranceCharge").value);
}
function load(){
 setTimeout("set()",150);
}
function set(){
  document.getElementById('insuranceCharge').focus();
}
function disableBackSpace(){
	if(event.keyCode==8) return false;	
	return true;
}
function checkForNumberAndDecimal(obj){
                     if(!/^([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)){
                        obj.value="";
                        alertNew("The amount you entered is not a valid");
                        return;
                     }
		}
function checkDecimal(ev){
	if(ev.value  > 100){
		ev.value = ev.value.substring(0,2)+"."+ev.value.substring(2,4);
	}else if(ev.value  == 100){
		ev.value = ev.value.substring(0,3)+"."+ev.value.substring(4,6);
	}else if(ev.value.indexOf(".")!=-1){
		ev.value = ev.value.substring(0,ev.value.indexOf("."))+"."+ev.value.substring(ev.value.indexOf(".")+1,ev.value.indexOf(".")+3);
	}
}
function allowOnlyWholeNumbers(obj){
                var result;
               if(!/^[1-9]\d*$/.test(obj.value)){
                   result=obj.value.replace(/[^0-9]+/g,'');
                   obj.value=result;
                    return false;
                }
                return true;
            }
</script>
</head>
  <%@include file="../includes/resources.jsp" %>
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
<form name="portSearch">
	<table align="center" width="90%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
		  <tr class="tableHeadingNew">
		     <td colspan="2">Calculate Insurance</td>
		  </tr>
		  <tr class="textlabelsBold">
		 	<td>Insurance Rate</td>
			<td><input type="text" name="insuranceCharge" size="10" align="right"  id="insuranceCharge"  maxlength="8"
		      		value="<%=insuranceCharge%>" class="textlabelsBoldForTextBox" onchange="checkForNumberAndDecimal(this)"/></td>
		  </tr>
		  <tr><td>&nbsp;</td></tr>
		  <tr class="textlabelsBold">
			<td>Cost of Goods</td>
			<td><input type="text" name="costOfGoods" align="right" size="10" id="costOfGoods" value="<%=totalCostOfGoods%>"
		     	 onkeypress="return disableBackSpace();"  class="textlabelsBoldForTextBox" maxlength="7" onkeyup="allowOnlyWholeNumbers(this)"/></td>
		  </tr>
		  <tr><td>&nbsp;</td></tr>
		  <tr>
		     <td align="center" colspan="2"><input type="button" class="buttonStyleNew" value="Submit" onclick="getInsurance()" /></td>
		  </tr>
	</table>
</form>
</body>