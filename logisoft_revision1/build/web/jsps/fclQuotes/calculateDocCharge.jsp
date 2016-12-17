<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@include file="../includes/jspVariables.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<head>

<title>FCL Quotation Rates</title>
<%@include file="../includes/baseResources.jsp" %>
<script language="javascript" src="<%=path%>/js/common.js"></script>
<script language="javascript" type="text/javascript">
function getDocumentCharge(){
 parent.parent.call();
 parent.parent.GB_hide();
 parent.parent.calculateDocumentCharge(document.getElementById("documentCharge").value);
}
function load(){
 setTimeout("set()",150);
}
function set(){
  document.getElementById('documentCharge').focus();
}
function checkForNumberAndDecimal(obj){
                     if(!/^([0-9]+(\.[0-9]{1,4})?)$/.test(obj.value)){
                        obj.value="";
                        alertNew("The amount you entered is not a valid");
                        return;
                     }
		}
function disableBackSpace(){
	if(event.keyCode==8) return false;	
	return true;
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
               if(!/^[1-9 . ]\d*$/.test(obj.value)){
                   result=obj.value.replace(/[^0-9 . ]+/g,'');
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
		     <td colspan="2">Calculate Doc Charge</td>
		  </tr>
                  <tr><td>&nbsp;</td></tr>
		  <tr><td>&nbsp;</td></tr>
		  <tr class="textlabelsBold">
                      <td align="center">Sell</td>
			<td><input type="text" name="documentCharge" size="10" align="right"  id="documentCharge"  maxlength="8"
		      		value="0.00" class="textlabelsBoldForTextBox" onchange="checkForNumberAndDecimal(this)" /></td>
		  </tr>
		  <tr><td>&nbsp;</td></tr>
		  <tr><td>&nbsp;</td></tr>
		  <tr>
		     <td align="center" colspan="2"><input type="button" class="buttonStyleNew" value="Submit" onclick="getDocumentCharge()" /></td>
		  </tr>
	</table>
</form>
</body>