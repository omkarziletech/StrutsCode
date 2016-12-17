<%-- 
    Document   : lclScacCode
    Created on : Jun 25, 2014, 4:12:52 PM
    Author     : mohanapriya.a
--%>
<%@include file="init.jsp"%>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <cong:form name="lclPickupInfoForm" id="lclPickupInfoForm" action="/lclPickupInfo.do">
            <table width="100%"><tr><td>
                        SCAC Code<input type="text" style="width: 50px" maxlength="4" class="textlabelsBoldForTextBox" name="scac" id="scac" value="${scac}"/>
                    </td><td>
                        Carrier<input type="text" name="carrier" class="textlabelsBoldForTextBox" id="carrier" value="${carrier}"/>
                    </td><td>
                        <input type="submit" value="Search" class="button-style1" id="search" onclick="searchSscacCodeList('getScacCodeList')">
                    </td><td>
                        <input type="button" value="Submit" class="button-style1" id="submit" onclick="submitScacCode()">
                    </td></tr></table>
            <table width="100%" class="dataTable">
                <thead>
                    <tr>
                        <th></th>
                        <th>SCAC CODE</th>
                        <th>CARRIER NAME</th>
                    </tr>
                </thead>
                <c:forEach items="${scacCodelist}" var="scac">
                    <c:choose>
                        <c:when test="${zebra=='odd'}">
                            <c:set var="zebra" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="zebra" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tbody>
                        <tr class="${zebra}">
                            <td><input type="radio" name="scacRadio" id="scacRadio" value="${scac[0]}"></td>
                            <td>${scac[0]}</td>
                            <td>${scac[1]}</td>
                        </tr>
                    </tbody>
                </c:forEach>
            </table>
            <input type="hidden" name="methodName" id="methodName"/>
        </cong:form>
        <script type="text/javascript">
            function searchSscacCodeList(methodName) {
                $('#methodName').val(methodName);
                $('#lclPickupInfoForm').submit();
            }
            function submitScacCode() {
                var scac = $("input:radio[name='scacRadio']:checked").val();
                if(scac === '' || scac === undefined || scac === null){
                    sampleAlert("Please Select SCAC Code");
                }else{
                    parent.$('#scacCode').val(scac);
                    parent.$.colorbox.close();
                }
            }
        </script>
    </body>
</html>
