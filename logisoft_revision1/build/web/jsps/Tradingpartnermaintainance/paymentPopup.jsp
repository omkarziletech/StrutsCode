<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.Vendor,com.gp.cong.logisoft.domain.PaymentPopUp,com.gp.cong.logisoft.util.DBUtil"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/jspVariables.jsp" %>
 
<%
String path = request.getContextPath();
%>


<html> 
	<head>
		<title>JSP for PaymentInformation form</title>
		<%@include file="../includes/baseResources.jsp" %>
		<%@include file="../includes/resources.jsp" %>
	<script language="javascript" src="<%=path%>/js/common.js" ></script>
	<script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
	<script type="text/javascript">
	
	function add1()	{
	alert();
	  var value=document.tradingPartnerForm.rfax.value;
	
			for(var i=0;i< value.length;i++){
				if(value.indexOf(" ") == 0){
					alert("Please dont start with white space");
					return;
				}
			}
<%--        if(IsNumeric(document.tradingPartnerForm.rfax.value)==false) {--%>
<%--	    	alert("Fax should be Numeric.");--%>
<%--	  		document.tradingPartnerForm.rfax.value="";--%>
<%--	   	 	document.tradingPartnerForm.rfax.focus();--%>
<%--	   	 	return;--%>
<%--	     }--%>
	     alert();
	  document.tradingPartnerForm.buttonValue.value="addPayementInfo";
	  document.tradingPartnerForm.submit();
	  parent.parent.GB_hide();
	}
	
	function toUppercase(obj){
		obj.value = obj.value.toUpperCase();
	 }
	 function disable()  {
	     var state= document.paymentPopupForm.paymethod;
	     var opt=document.paymentPopupForm.swift;
	        (state.value==1)?opt.disabled=true:opt.disabled=false;
     }
		
    function limitText(limitField, limitCount, limitNum) 
	 {
	   limitField.value = limitField.value.toUpperCase();
       if (limitField.value.length > limitNum)
        {
           limitField.value = limitField.value.substring(0, limitNum);
        } 
       else
        {
			limitCount.value = limitNum - limitField.value.length;
 	    }
 
     }
    		   
	</script>
	</head>
	<body class="whitebackgrnd" >
		<html:form action="/tradingPartner" name="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">

<table width="100%" border="0" class="tableBorderNew">
<tr class="tableHeadingNew" height="90%">Payment Information</tr>
 <tr class="style2"><td>&nbsp;</td></tr>
 <tr class="style2">
  <td><table border="0" cellpadding="3">
        <tr>
	    	<td class="style2" align="right">Payment Method</td>
	    	<td><html:select property="paymethod"  styleClass="verysmalldropdownStyle2"  onchange="disable()">
<%--            	<html:optionsCollection name="Paymentlist"/>               --%>
                         </html:select></td>
        </tr>
        
 	    <tr>
         	 <td class="style2" valign="top" align="right"> Bank Address</td>
          	 <td><html:textarea property="baddr"   styleClass="textareastyle" cols="31" rows="4" onkeyup="limitText(this.form.address1,this.form.countdown,100)"/></td>
         </tr>
         
        <tr>
      		<td class="style2" align="right">Account No </td>
      		<td ><html:text property="vacctno" size="25"   onkeyup="toUppercase(this)" maxlength="10" onkeypress="titleLetter()"></html:text></td>
        <tr>
        
        <tr>
       		<td class="style2" align="right">Swift</td>
       		<td>
	    		<html:text property="swift" size="25" onkeyup="toUppercase(this)" maxlength="20" />
			</td> 
        </tr>
         
        <tr>
            <td class="style2" align="right">Remit Fax</td>
    		<td><html:text property="rfax" size="25"  maxlength="13" onkeypress="getIt(this)"/></td>
        </tr>
        <tr>
            <td colspan="5">&nbsp;</td>
        </tr>
 </table></td>
 
 <td valign="top"><table border="0" align="center" cellpadding="3">
       	 <tr>                
			<td class="style2" align="right">Bank Name</td>
	    	<td align="left"><html:text property="bankname"  onkeyup="toUppercase(this)" size="25" /></td>
        </tr>
	       
         <tr>	
          	<td class="style2" align="right">Account Name</td>
	      	<td ><html:text property="vacctname" size="25"  maxlength="20" onkeyup="toUppercase(this)"/></td>
         </tr>
         
        <tr>
      		<td class="style2" align="right">ABA Routing</td>
      		<td><html:text property="aba" size="25"  maxlength="20" onkeyup="toUppercase(this)" /></td>
  		</tr>
       	
        <tr>
       		 <td class="style2" align="right">Remit Email</td>
          	 <td><html:text property="remail" size="25"   maxlength="20"/></td>
        </tr>
        
        <tr>
            <td class="style2" align="right">Email</td>
    		<td><html:text property="payemail" size="25"  maxlength="20"  /></td>
        </tr>
        <tr>
            <td colspan="5">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="5">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="5">&nbsp;</td>
        </tr>
  </table></td>
  <tr> 
     	<td width="90%" align="right" >
     	  <input type="button" class="buttonStyleNew" value="Add Payment"  id="add" onclick="add1()"/>
     	</td>
  </tr>    
 
  </table>
  <html:hidden property="buttonValue" styleId="buttonValue"/>
		</html:form>
	</body>
	<%@include file="../includes/baseResourcesForJS.jsp" %>
</html>