<%-- 
    Document   : search
    Created on : Aug 24, 2015, 10:46:22 AM
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
        <title>Corporate Account</title>
    </head>
    <body>
        <div id="pane">
            <%@include file="../../../jsps/preloader.jsp"%>
            <html:form action="/corporateAccount" type="com.logiware.referencedata.form.CorporateAccountForm"
                       name="corporateAccountForm" styleId="corporateAccountForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <table class="table" style="margin: 0;overflow: hidden">
                    <tr class="tableHeadingNew"><td colspan="5">Search</td></tr>
                    <tr>
                        <td colspan="3"></td>
                    </tr>
                    <tr>
                        <td class="label" style="float: right">Account Name</td>
                        <td>
                            <html:text property="searchAcctName" styleId="searchAcctName" styleClass="textbox" />
                        </td>
                        <td class="label" style="float: right">Commodity Code</td>
                        <td>
                            <cong:autocompletor  name="searchCommName" styleClass="textWidth"  position="right" id="commodityNameId"
                                                 fields="searchCommCode" value="${corporateAccountForm.searchCommName}"
                                                 shouldMatch="true" width="600"  query="FCL_RATE_COMMODITY" template="two" container="null" scrollHeight="300"/>
                            <html:text styleClass="textlabelsBoldForTextBoxDisabledLook"  styleId="searchCommCode" property="searchCommCode" style="width:70px" readonly="true"/>
                        </td>
                        <td width="50%">
                            <input type="button"  value="Search" id="search"
                                   align="middle" class="button" onclick="searchCorporate();"/>
                            <input type="button"  value="Reset" id="reset"
                                   align="middle" class="button" onclick="resetCorporate();"/>
                            <input type="button"  value="Add New" id="addNew"
                                   align="middle" class="button" onclick="showAddorEditCoporateAcct('${path}',0);"/>
                        </td>
                    </tr>
                </table>
                <br/>
                <div id="results">
                    <c:import url="/jsp/referencedata/corporateAccount/result.jsp"/>
                </div>
            </html:form>
        </div>
    </body>
</html>

<script type="text/javascript">
    $(document).ready(function() {
        $("[title != '']").not("link").tooltip();
    });
    function showAddorEditCoporateAcct(path,corporateAcctId) {
        var url = path + "/corporateAccount.do?action=addOrEdit&corporateAcctId="+corporateAcctId;
        $.colorbox({
            iframe: true,
            href: url,
            width: "50%",
            height: "40%",
            title: "Add Corporate Account"
        });
    }
    function showUpNotes(path,corporateAcctId) {
        var url = path + "/corporateAccount.do?action=showNotes&corporateAcctId="+corporateAcctId;
        $.colorbox({
            iframe: true,
            href: url,
            width: "50%",
            height: "70%",
            title: "Notes"
        });
    }
    function searchCorporate(){
        showLoading();
        $("#action").val("display");
        $("#corporateAccountForm").submit();
    }
    function resetCorporate(){
        $('#searchAcctName').val('');
        $('#commodityNameId').val('');
        $('#searchCommCode').val('');
    }
</script>