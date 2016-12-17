<%-- 
    Document   : multieCitySelectPopUp
    Created on : Sep 9, 2015, 2:42:36 PM
    Author     : aravindhan.v
--%>
<%@include file="../../init.jsp" %>
<%@include file="../../../includes/jspVariables.jsp"%>
<%@include file="../../../../jsps/includes/baseResources.jsp" %>
<%@include file="../../../includes/resources.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <cong:form id="lclSearchForm" name="lclSearchForm" method="post" action="lclSearch.do">
            <html:hidden property="moduleName" styleId="moduleNameId" value="${lclSearchForm.moduleName}"/>
            <html:hidden property="methodName" styleId="methodNameId" value="${lclSearchForm.methodName}"/>
            <table width="100%">
                <tr class="tableHeadingNew">
                    <td width="15%" colspan="2">Select Country</td>
                </tr>
            </table>
            <br/><br/>
            <table width="100%" border="0">
                <tr>
                    <td class="textlabelsBoldforlcl" width="40%">${labelName}</td>
                    <td width="60%"><cong:autocompletor name="multieOrigin" id="multieOrigin" template="one" width="250" container="NULL" query="PORT"
                                        styleClass="textlabelsLclBoldForMainScreenTextBox" shouldMatch="true" scrollHeight="300"
                                        fields="null,NULL,multieOrigin,null" multiSelect="true" callback="combineValue('multieOriginCode','multieOrigin')"/>
                        <cong:hidden name="multieOriginCode" id="multieOriginCode"/>
                    </td>
                </tr>
                <tr>
                    <td width="40%"></td>
                    <td width="60%">
                        <div class="button-style1" 
                             onclick="submit('${field1}', '${field2}');">Submit</div>
                    </td>
                </tr>
            </table>
        </cong:form>
    </body>
    <script>
        $(document).ready(function () {
            $('#multieOrigin').keyup(function () {
                var origin = $('#multieOrigin').val();
                if (origin === "") {
                    $('#multieOrigin').val('');
                    $('#multieOriginCode').val('');
                }
                if ($('#multieOrigin').val().length < $("#multieOriginCode").val().length) {
                    $("#multieOriginCode").val($('#multieOrigin').val());
                }
            });
        });
        function combineValue(var1, var2) {
            var originCode = $('#' + var1).val();
            originCode = originCode.substring(0, originCode.lastIndexOf(",") + 1);
            $('#' + var1).val(originCode += $('#' + var2).val() + ',');
            $('#' + var2).val($('#' + var1).val());
        }
        function submit(var1, var2) {
            var origin = parent.$("#" + var2).val() + $("#multieOrigin").val();
            var unLoc = origin.split(",");
            var unLocCode = unLoc.filterUnigueArray();
            if ($("#multieOrigin").val() !== '' && $("#multieOriginCode").val() !== '') {
                parent.$("#" + var1).val(unLocCode);
                parent.$("#" + var2).val(unLocCode);
            }
            parent.$.fn.colorbox.close();
        }

        Array.prototype.checkArrayExit = function (v) {
            for (var i = 0; i < this.length; i++) {
                if (this[i] === v)
                    return true;
            }
            return false;
        };
        Array.prototype.filterUnigueArray = function () {
            var resultArray = [];
            for (var i = 0; i < this.length; i++) {
                if (!resultArray.checkArrayExit(this[i])) {
                    resultArray.push(this[i]);
                }
            }
            return resultArray;
        };
    </script>
</html>
