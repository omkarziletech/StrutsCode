<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.struts.form.TradingPartnerForm,com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerConstants"%>
<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%><%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String moveto = "frmMasterCustom";
            if (request.getAttribute("tradingPartnerAcctType") != null) {
                request.setAttribute("tradingPartnerForm", request.getAttribute("tradingPartnerAcctType"));
            }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>JSP for CustomerForm form</title>

        <%@include file="../includes/baseResources.jsp" %>

        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <script language="javascript" src="<%=path%>/js/FormChek.js" ></script>
        <script type="text/javascript">


            function cancel(){
                document.tradingPartnerForm.buttonValue.value="cancelCustomer";
                document.tradingPartnerForm.submit();
            }
            function cancel(){
        	var eciAcctNo = document.tradingPartnerForm.eciAccountNo.value.toUpperCase();
        	var eciFwdNo = document.tradingPartnerForm.eciAccountNo2.value.toUpperCase();
        	var eciVendNo = document.tradingPartnerForm.eciAccountNo3.value.toUpperCase();
        	var acct_type = getAccountType();
        	var sslinNumber='';
        	var subType='';
        	var acctNo = '<%=accountNo%>';

        	if(document.tradingPartnerForm.accountType10.checked){
        		sslinNumber = document.tradingPartnerForm.sslineNumber.value.toUpperCase();
        		subType = document.tradingPartnerForm.subType.value;
        	}
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                        methodName: "updateTPCustAddGenInfo",
                        param1: eciAcctNo,
                        param2: eciFwdNo,
                        param3: eciVendNo,
                        param4: null,
                        param5: acct_type,
                        param6: sslinNumber,
                        param7: subType,
                        param8: acctNo,
                        request: true
                    },
                    async: false,
                    success: function (data) {
        		document.tradingPartnerForm.buttonValue.value="cancelCustomer";
            		document.tradingPartnerForm.submit();
                    }
                });
        }
        function getAccountType(){
            var acct_type = '';
            if(document.tradingPartnerForm.accountType1.checked){
        		if(acct_type != ''){
        			acct_type = acct_type + "," + "S";
        		}else{
        			acct_type = "S";
        		}
        	}
        	if(document.tradingPartnerForm.accountType3.checked){
        		if(acct_type != ''){
        			acct_type = acct_type + "," + "N";
        		}else{
        			acct_type = "N";
        		}
        	}
        	if(document.tradingPartnerForm.accountType4.checked){
        		if(acct_type != ''){
        			acct_type = acct_type + "," + "C";
        		}else{
        			acct_type = "C";
        		}
        	}
        	if(document.tradingPartnerForm.accountType8.checked){
        		if(acct_type != ''){
        			acct_type = acct_type + "," + "I";
        		}else{
        			acct_type = "I";
        		}
        	}
        	if(document.tradingPartnerForm.accountType9.checked){
        		if(acct_type != ''){
        			acct_type = acct_type + "," + "E";
        		}else{
        			acct_type = "E";
        		}
        	}
        	if(document.tradingPartnerForm.accountType10.checked){
        		if(acct_type != ''){
        			acct_type = acct_type + "," + "V";
        		}else{
        			acct_type = "V";
        		}
        	}
        	if(document.tradingPartnerForm.accountType11.checked){
        		if(acct_type != ''){
        			acct_type = acct_type + "," + "O";
        		}else{
        			acct_type = "O";
        		}
        	}
        	if(document.tradingPartnerForm.accountType13 && document.tradingPartnerForm.accountType13.checked){
        	   if(acct_type != ''){
        			acct_type = acct_type + "," + "Z";
        	   }else{
        			acct_type = "Z";
        	  }
        	}
        	return acct_type
        }
        function showSubType() {
                obj = document.getElementById('accountType10');
                if(obj.checked) {
                    if(null!=document.vendorframe && null!=document.vendorframe.document.getElementById('subTypeValue')){
                        document.vendorframe.document.getElementById('subTypeValue').style.display = "block";
                        document.vendorframe.document.getElementById('subTypeLabel').style.display = "block";
                    }
                    document.getElementById('subTypeContent').style.display = "block";
                    document.getElementById('ssLineId').style.display = "block";
                }else {
                    document.getElementById('subTypeContent').style.display = "none";
                    document.getElementById('ssLineId').style.display = "none";
                    if(null!=document.vendorframe && null!=document.vendorframe.document.getElementById('subTypeValue')){
                        document.vendorframe.document.getElementById('subTypeValue').style.display = "none";
                        document.vendorframe.document.getElementById('subTypeLabel').style.display = "none";
                    }
                }
            }
            function getNotify(){
                if(parent.mainFrame.document.geniframe.tradingPartnerForm!=undefined){
                    if(document.tradingPartnerForm.accountType4.checked){
                        parent.mainFrame.document.geniframe.getNotifyParty();
                        //parent.mainFrame.document.geniframe.tradingPartnerForm.notifyParty.checked=true;
                    }else{
                        parent.mainFrame.document.geniframe.getNotifyParty();
                        //parent.mainFrame.document.geniframe.tradingPartnerForm.notifyParty.checked=false;
                    }
                }
            }
        </script>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/tradingPartner" name="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">
            <font id="msg" color="red" size="2" style="font-weight:bold;"></font>
            <table width="50%" cellpadding="0" cellspacing="0">
                <tr class="textlabels">
                    <td><b>Account Name:&nbsp;</b><%=accountName%>&nbsp;&nbsp;</td>
                    <td><b>Account No:&nbsp;</b><%=accountNo%>&nbsp;&nbsp;</td>
                    <td colspan="2">
                        <input type="button" class="buttonStyleNew" value="Go Back"  onclick="cancel()"/>
                        <input type="button" class="buttonStyleNew" value="Save"  onclick="Save()"/>
                    </td>
                </tr>
                <tr class="textlabels">
                    <td><b>ECI Shpr/FF#:&nbsp;</b><html:text property="eciAccountNo" value="${tradingPartnerForm.eciAccountNo}" size="5"/></td>
                    <td><b>ECI Consignee:&nbsp;</b><html:text property="eciAccountNo2" value="${tradingPartnerForm.eciAccountNo2}" size="5"/></td>
                    <td><b>ECI Vendor:&nbsp;</b><html:text property="eciAccountNo3" value="${tradingPartnerForm.eciAccountNo3}" size="5"/></td>
                    <td id="ssLineId">
                        <b>SSLine Number :</b>
                        <html:text property="sslineNumber" style="text-transform: uppercase;" maxlength="5"  styleClass="textlabelsBoldForTextBox" value="${tradingPartnerForm.sslineNumber}" size="5"/>
                    </td>
                </tr>
                <tr class="textlabels">
                    <td colspan="2">Account Type &nbsp;
                        <html:checkbox property="accountType1" name="tradingPartnerForm"></html:checkbox>S&nbsp;
                        <html:checkbox property="accountType3" name="tradingPartnerForm" />N&nbsp;
                        <html:checkbox property="accountType4" name="tradingPartnerForm" onclick="getNotify()" />C&nbsp;
                        <html:checkbox property="accountType8" name="tradingPartnerForm" />AI&nbsp;
                        <html:checkbox property="accountType9" name="tradingPartnerForm" />AE&nbsp;
                        <html:checkbox property="accountType10" name="tradingPartnerForm" onclick="showSubType()"/>V&nbsp;
                        <html:checkbox property="accountType11" name="tradingPartnerForm" />O&nbsp;
                        <c:if test="${not empty loginuser && loginuser.role.roleDesc=='Admin'}">
                            <html:checkbox property="accountType13" name="tradingPartnerForm" />Z&nbsp;
                        </c:if>
                    </td>
                    <td id="subTypeContent"colspan="2">
                        Sub Type&nbsp;&nbsp;
                            <input type="text" name="subType" id="subType" value="${tradingPartnerForm.subType}" readonly="readonly" class="textlabelsBoldForTextBoxDisabledLook">
                    </td>
                </tr>
            </table>

            <html:hidden property="tradingPartnerId" value="<%=accountNo%>"/>
            <html:hidden property="buttonValue" styleId="buttonValue"/>
            <html:hidden property="fromMaster" value="<%=moveto%>"/>
            <script>showSubType();</script>
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


