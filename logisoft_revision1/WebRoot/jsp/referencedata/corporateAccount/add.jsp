<%-- 
    Document   : add
    Created on : Aug 24, 2015, 10:46:34 AM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../../jsps/LCL/init.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add/Edit Corporate Account</title>
    </head>
    <body>
        <div id="pane">
            <%@include file="../../../jsps/preloader.jsp"%>
            <html:form action="/corporateAccount" type="com.logiware.referencedata.form.CorporateAccountForm"
                       name="corporateAccountForm" styleId="corporateAccountForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="corporateAcctId" styleId="corporateAcctId"/>
                <table class="widthFull" width="100%">
                    <tr class="tableHeadingNew">
                        <td colspan="4">
                            Add Corporate Account
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4"></td>
                    </tr>
                    <tr>
                        <td class="label" style="float: right">Account Name</td>
                        <td class="label">
                            <html:text property="acctName" styleId="acctName" styleClass="textbox" onchange="validateAcctName();"/>
                        </td>
                        <td class="label" style="float: right">
                            Commodity
                        </td>
                        <td class="label">
                            <cong:autocompletor  name="searchCommName" styleClass="textWidth"  position="left" id="commodityNameId"
                                                 fields="commCode" shouldMatch="true" width="300" value="${corporateAccountForm.searchCommName}"
                                                 query="FCL_RATE_COMMODITY" template="two" container="null" scrollHeight="300"/>
                            <html:text styleClass="textlabelsBoldForTextBoxDisabledLook"  styleId="commCode"
                                       property="commCode" style="width:70px" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4"></td>
                    </tr>
                    <tr>
                        <td class="label" style="float: right">Account Type</td>
                        <td>  <html:select property="corporateAcctType" styleId="corporateAcctType"
                                     styleClass="smallDropDown textlabelsBoldforlcl" style="width:134px">
                                <html:optionsCollection property="corporateAcctTypeList" label="label" value="value"/>
                            </html:select></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <input type="button"  value="Save" id="updateCoporateAcctType"
                                   align="middle" class="button" onclick="saveCorporateAcct();"/>
                        </td>
                    </tr>
                </table>
            </html:form>
        </div>
    </body>
</html>
<script type="text/javascript">
    function saveCorporateAcct(){
        if($('#acctName').val()===""){
            $('#acctName').val("").focus();
            $.prompt('Account Name is required');
            $("#acctName").css("border-color", "red");
            return false;
        }
        if($('#corporateAcctType').val()===""){
            $("#corporateAcctType").css("border-color", "red");
            $('#corporateAcctType').val("").focus();
            $.prompt('Account Name is required');
            return false;
        }
        $("#action").val('saveOrUpdate');
        var params=$("#corporateAccountForm").serialize();;
        $.post($("#corporateAccountForm").attr("action"), params,
        function (data) {
            showProgressBar();
            $('#results').html(data);
            $('#results', window.parent.document).html(data);
            hideProgressBar();
            parent.$.fn.colorbox.close();
        });
    }
    function validateAcctName(){
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.CorporateAccountDAO",
                methodName: "isValidateAcctName",
                param1: $('#acctName').val(),
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if(data){
                    $.prompt('Account Name is already Exists.');
                    $('#acctName').val('');
                }
            }
        });
    }
</script>
