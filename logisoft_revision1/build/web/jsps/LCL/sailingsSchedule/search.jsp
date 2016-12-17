<%-- 
    Document   : search
    Created on : Aug 26, 2015, 6:15:13 PM
    Author     : Wsware
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sailing Schedules</title>
    </head>
    <body>
        <div id="pane" style="overflow: auto;">
            <html:form action="/lclSailingSchedules" type="com.logiware.lcl.sailings.form.SailingsScheduleForm"
                       name="sailingScheduleForm" styleId="sailingScheduleForm" scope="request" method="post">
                <html:hidden property="methodName" styleId="methodName"/>
                <table width="100%" border="0" cellpadding="1" cellspacing="1" class="table">
                    <tr class="tableHeadingNew" >
                        <td colspan="4">Sailing Schedules Search</td>
                    </tr>
                    <tr><td colspan="4"></td></tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">Origin</td>
                        <td ><cong:autocompletor id="pooName" name="pooName" template="concatOrigin" fields="NULL,NULL,NULL,pooId"
                                            query="RELAYNAME" styleClass="textuppercaseLetter" width="250" value="${sailingScheduleForm.pooName}"
                                            container="NULL"  shouldMatch="true"/>
                        </td>
                        <html:hidden property="pooId" styleId="pooId"/>
                        <td class="textlabelsBoldforlcl">Destination</td>
                        <td>
                            <cong:autocompletor id="fdName" name="fdName" template="one"  fields="NULL,NULL,NULL,fdId"
                                                query="CONCAT_RELAY_NAME_FD"  styleClass="textuppercaseLetter" width="350"
                                                value="${sailingScheduleForm.fdName}"  container="NULL" shouldMatch="true"/>
                            <html:hidden property="fdId" styleId="fdId"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center"></td>
                        <td colspan="2" align="center">
                            <input type="button" class="button-style1" id="addnew" value='Search' onclick="search();"/>
                            <input type="button" class="button-style1" id="restart" value='Restart' onclick="resetAllFields();"/>
                        </td>
                    </tr>
                </table>
                <br/>
                <div>
                    <%@include  file="/jsps/LCL/sailingsSchedule/result.jsp" %>
                </div>

            </html:form>
        </div>
    </body>
</html>
<script type="text/javascript">
    function search(){
        showLoading();
        $('#methodName').val("search");
        $('#sailingScheduleForm').submit();
    }
    function resetAllFields(){
        $('#pooName').val('');
        $('#fdName').val('');
        $('#pooId').val('');
        $('#fdId').val('');
        $('#polName').text('');
        $('#podName').text('');
        for (var i = document.getElementById("sailingScheduleTable").rows.length; i > 0; i--) {
            document.getElementById("sailingScheduleTable").deleteRow(i - 1);
        }
    }
</script>