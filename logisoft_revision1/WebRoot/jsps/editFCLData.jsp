<%-- 
    Document   : editFCLData
    Created on : Jul 26, 2010, 5:51:31 PM
    Author     : Vinay
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="includes/jspVariables.jsp" %>
<%@include file="includes/baseResources.jsp" %>
<%@include file="includes/resources.jsp" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit FCL Data</title>
        <%@include file="fragment/formSerialize.jspf"  %>
        <script type="text/javascript" language="javascript">
            start = function(){
                serializeForm();
              }
            window.onload = start;
        </script>
    </head>
    <body class="whitebackgrnd" >
        <html:form action="/fclDataSearch" type="com.gp.cong.logisoft.struts.form.FCLSearchForm" name="fclSearchForm" method="post" scope="request">
            <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%" >
                <thead>
                    <tr class="tableHeadingNew">Edit FCL Data</tr>
                </thead>
                <tbody>
                    <tr class="textlabelsBold">
                        <td>&nbsp;Origin Terminal</td>
                        <td>
                            <input class="textlabelsreadOnlyForTextBox" type="text" name="origTerm" value="${currentFCLData.origTerm}" readonly />
                        </td>
                        <td>Destination Port</td>
                        <td>
                            <input class="textlabelsreadOnlyForTextBox" type="text" name="desPort" value="${currentFCLData.desPort}" readonly />
                        </td>
                        <td>SSLine Name</td>
                        <td>
                            <input class="textlabelsreadOnlyForTextBox" type="text" name="ssl" value="${currentFCLData.sslineName}" readonly />
                        </td>
                        <td>Remarks</td>
                        <td>
                            <textarea class="textlabelsBoldForTextBox" rows="3" cols="30" type="text" name="remarks">${currentFCLData.remarks}</textarea>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>&nbsp;Days in Transit</td>
                        <td>
                            <input class="textlabelsBoldForTextBox" type="text" name="daysInTransit" value="${currentFCLData.daysInTransit}" />
                        </td>
                        <td>Port of Exit</td>
                        <td>
                            <input class="textlabelsBoldForTextBox" type="text" name="portOfExit" value="${currentFCLData.portOfExit}" />
                        </td>
                        <td></td>
                        <td>
                            <html:checkbox property="locD" name="currentFCLData" >
                                <span>Local Drayage</span>
                            </html:checkbox>
                        </td>
                        <td></td>
                        <td>
                            <html:checkbox property="haz" name="currentFCLData" >
                                <span>Hazardous</span>
                            </html:checkbox>
                        </td>
                    </tr>
                    <tr>
                        <td align="center" colspan="6">
                            <input type="button" value="Save" class="buttonStyleNew" onclick="validate()" />
                            <input type="reset" value="Reset" class="buttonStyleNew" />
                        </td>
                    </tr>
                </tbody>
            </table>
            <input type="hidden" name="action" value="edit" />
            <script type="text/javascript">
                function validate() {
                    if(isNaN(document.fclSearchForm.daysInTransit.value)) {
                        alert('Please enter only numbers');
                        document.fclSearchForm.daysInTransit.value = '';
                        document.fclSearchForm.daysInTransit.focus();
                    } else {
                        document.fclSearchForm.submit();
                    }
                }
            </script>
        </html:form>
    </body>
</html>
