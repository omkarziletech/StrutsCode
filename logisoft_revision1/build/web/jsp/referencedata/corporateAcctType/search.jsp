<%-- 
    Document   : search
    Created on : Aug 24, 2015, 11:03:01 AM
    Author     : Mei
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../../jsps/LCL/init.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Corporate Account Type</title>
    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
        <div id="pane">
            <html:form action="/corporateAccountType" type="com.logiware.referencedata.form.CorporateAcctTypeForm"
                       name="corporateAcctTypeForm" styleId="corporateAcctTypeForm" scope="request" method="post">
                <html:hidden property="action" styleId="action"/>
                <html:hidden property="corporateAcctId" styleId="corporateAcctId"/>
                <table class="table" style="margin: 0;overflow: hidden">
                    <tr class="tableHeadingNew"><td colspan="3">Search</td></tr>
                    <tr>
                        <td colspan="3"></td>
                    </tr>
                    <tr>
                        <td class="label" style="float: right">Description</td>
                        <td>
                            <html:text property="searchDescription" styleId="searchDescription" styleClass="textbox" />
                        </td>
                        <td width="50%">
                            <input type="button"  value="Search" id="search"
                                   align="middle" class="button" onclick="searchCorporate();"/>
                            <input type="button"  value="Reset" id="reset"
                                   align="middle" class="button" onclick="resetCorporate();"/>
                            <input type="button"  value="Add New" id="addNew"
                                   align="middle" class="button" onclick="showAddCorporateType();"/>
                        </td>
                    </tr>
                </table>
                <br/>
                <div id="results">
                    <c:import url="/jsp/referencedata/corporateAcctType/result.jsp"/>
                </div>
                <div id="add-corporateType-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
                    <table class="table" style="margin: 1px;width: 598px;">
                        <tr>
                            <th colspan="4">
                                <div class="float-left">Add Corporate Account Type</div>
                                <div class="float-right">
                                    <a href="javascript: closeCorporateType()">
                                        <img alt="Close Accrual" src="${path}/images/icons/close.png"/>
                                    </a>
                                </div>
                            </th>
                        </tr>
                        <tr>
                            <td colspan="4"></td>
                        </tr>
                        <tr>
                            <td class="label" style="float: right">Description</td>
                            <td class="label">
                                <html:text property="acctDescription" styleId="acctDescription" styleClass="textbox" onchange="validateAcctName();"/>
                            </td>
                            <td class="label" style="float: right">
                                Disable
                            </td>
                            <td class="label">
                                <html:radio property="acctDisabled" styleId="acctDisabledY"  value="true" />Yes
                                <html:radio property="acctDisabled" styleId="acctDisabledN"  value="false" />No
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4"></td>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <input type="button"  value="Update" id="updateCoporateAcctType" style="display: none;"
                                       align="middle" class="button" onclick="updateCorporateAcctType();"/>
                                <input type="button"  value="Add" id="addCoporateAcctType" style="display: none;"
                                       align="middle" class="button" onclick="addCorporateAcctType();"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </html:form>
        </div>
    </body>
</html>
<script type="text/javascript">
    $(document).ready(function() {
        $("[title != '']").not("link").tooltip();
    });
    function showAddCorporateType(){
        showAlternateMask();
        $("#add-corporateType-container").center().show(500,function(){
            $('#acctDescription').val('');
            $('#updateCoporateAcctType').hide();
            $('#addCoporateAcctType').show();
            $('#acctDisabledN').attr('checked','true');
        });
    }
    function editCorporate(id,description,disabled){
        showAlternateMask();
        $("#add-corporateType-container").center().show(500,function(){
            $('#corporateAcctId').val(id);
            $('#acctDescription').val(description);
            $('#updateCoporateAcctType').show();
            $('#addCoporateAcctType').hide();
            if(disabled=='true'){
                $('#acctDisabledY').attr('checked','true');
            }else{
                $('#acctDisabledN').attr('checked','true');
            }
        });
    }
    function closeCorporateType(){
        $("#add-corporateType-container").center().hide(500,function(){
            hideAlternateMask();
        });
    }
    function addCorporateAcctType(){
        showLoading();
        $("#action").val("add");
        $("#corporateAcctTypeForm").submit();
    }
    function updateCorporateAcctType(){
        showLoading();
        $("#action").val("update");
        $("#corporateAcctTypeForm").submit();
    }
    function searchCorporate(){
        if($('#searchDescription').val()===""){
            $('#searchDescription').val("").focus();
            $.prompt('Description is required');
            return false;
        }
        showLoading();
        $("#action").val("display");
        $("#corporateAcctTypeForm").submit();
    }
    function validateAcctName(){
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.CorporateAcctTypeDAO",
                methodName: "isValidateDescription",
                param1: $('#acctDescription').val(),
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if(data){
                    $.prompt('Description is already Exists.');
                    $('#acctDescription').val('');
                }
            }
        });
    }
    function resetCorporate(){
        $('#searchDescription').val('');
        showLoading();
        $("#action").val("display");
        $("#corporateAcctTypeForm").submit();
    }
    function showUpNotes(path,corporateAcctId) {
        var url = path + "/corporateAccountType.do?action=showNotes&corporateAcctId="+corporateAcctId;
        $.colorbox({
            iframe: true,
            href: url,
            width: "50%",
            height: "70%",
            title: "Notes"
        });
    }
</script>