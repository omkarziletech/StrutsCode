<%-- 
    Document   : sendReleaseReport
    Created on : Aug 17, 2016, 12:32:51 PM
    Author     : NambuRajasekar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="init.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/taglib.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <cong:form action="/lclUnitsSchedule" name="lclUnitsScheduleForm" id="lclUnitsScheduleForm">
            <div class="result-container">
                <table width="100%" class="tableBorderNew"  border="0">
                    <tr class="tableHeadingNew">
                        <td colspan="2">
                            Compose Mail
                        </td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">TO:</td>
                        <td>
                            <cong:text name="toEmailAddress"  id="toEmailAddress"  style="width:300px;" container="null"  />
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">CC:</td>
                        <td>
                            <cong:text name="ccEmailAddress"  id="ccEmailAddress"  style="width:300px;" container="null" />
                        </td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">BCC:</td>
                        <td>
                            <cong:text name="bccEmailAddress"  id="bccEmailAddress"  style="width:300px;"  container="null"  />
                        </td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">Subject:</td>
                        <td>
                            <cong:text name="emailSubject"  id="emailSubject"  style="width:300px;" value="${unlocation}"  container="null"/>
                        </td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">Message:</td>
                        <td>
                            <textarea name="emailMessage"  id="emailMessage"  rows="5" cols="35" container="null"  ></textarea>
                        </td>
                    </tr>
                    <tr><td><br/></td></tr>
                    <tr>
                        <td></td>
                        <td  align="center">
                            <input type="button"  value="Submit" id="saveEmail" align="center" class="button" onclick="submitEmail();"/>
                            <input type="button"  value="EmailMe" id="emailMeId" align="center" class="button" onclick="submitEmailMe();"/>

                        </td>
                    </tr>
                </table>
            </div>
            <cong:hidden name="origin" id="origin" value="${origin}"/>
            <cong:hidden name="emailMe" id="emailMe"/>
            <cong:hidden name="methodName" id="methodName"/>
        </cong:form>
    </body>
    <script type="text/javascript">
    function submitEmail() {
            var email = $("#toEmailAddress").val();
            if (email !== "") {
                parent.$.colorbox.close();
                $("#methodName").val("sendReleaseReport");
                $("#lclUnitsScheduleForm").submit();

            } else {
                $.prompt("ToEmail Required");
            }
        }
        function submitEmailMe() {
            parent.$.colorbox.close();
            $("#emailMe").val(true);
            $("#methodName").val("sendReleaseReport");
            $("#lclUnitsScheduleForm").submit();
        }

    </script>
</html>
