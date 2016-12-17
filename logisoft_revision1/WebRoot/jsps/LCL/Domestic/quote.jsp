<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="../init.jsp" %>
        <%@include file="../colorBox.jsp" %>
        <%@include file="../../includes/baseResources.jsp" %>
        <%@include file="../../includes/resources.jsp" %>
        <%@include file="../../includes/jspVariables.jsp" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Misc Invoice Pool</title>
        <%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript">
            var path = "${path}";
        </script>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>

    </head>
    <body>
        <%@include file="../../../jsps/preloader.jsp"%>
         <html:form action="/rateQuote" name="rateQuoteForm"
                   styleId="rateQuoteForm" type="com.logiware.domestic.form.rateQuoteForm" scope="request" method="post">
            <input type="hidden" name="methodName" id="methodName"/>
            <input type="hidden" name="quoteId" id="quoteId"/>
            <table class="table" style="margin: 0">
                <cong:tr>
                    <th colspan="10">Search Criteria</th>
                </cong:tr>
                <cong:tr>

                    <cong:td>From Zip</cong:td>
                    <cong:td align="left">
                        <cong:autocompletor styleClass="textlabelsBoldForTextBox "  id="fromZip" name="fromZip" scrollHeight="200px"
                                query="CONCAT_CITY" fields="NULL,NULL,NULL" template="concatOrigin" container="NULL" width="500" shouldMatch="true" />
                    </cong:td>
                    <cong:td></cong:td>
                    <cong:td>To Zip</cong:td>
                    <cong:td>
                        <cong:autocompletor styleClass="textlabelsBoldForTextBox "  id="toZip" name="toZip" scrollHeight="200px"
                                query="CONCAT_CITY" fields="NULL,NULL,NULL" template="concatOrigin" container="NULL" width="500" shouldMatch="true" />
                    </cong:td>
                    <cong:td styleClass="label">Shipment Id</cong:td>
                    <cong:td>
                        <cong:autocompletor styleClass="textlabelsBoldForTextBox "  id="shipmentId" name="shipmentId" scrollHeight="200px"
                                            query="RATE_QUOTE" fields="NULL"  container="NULL"  shouldMatch="true" />
                    </cong:td>
                    <cong:td styleClass="label">User</cong:td>
                    <cong:td>
                        <c:choose>
                            <c:when test="${loginuser.role.roleDesc == 'Admin' || loginuser.role.roleDesc == 'SALES'}">
                                <html:select property="userId" styleId="userId"
                                                         style="width:125px;" styleClass="textlabelsBoldForTextBox dropdown_accounting" >
                                                         <html:optionsCollection  name="userNameList" />
                                            </html:select>
                            </c:when>
                            <c:otherwise>
                                <html:text property="userName" styleId="userName" styleClass="textbox" value="${loginuser.firstName}"/>
                                <html:hidden property="userId" styleId="userId" styleClass="textbox" value="${loginuser.userId}"/>
                            </c:otherwise>
                        </c:choose>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <td colspan="10" class="align-center">
                        <input type="button" value="Search" class="button" onclick="searchQuote()"/>
                        <input type="button" value="New" class="button" onclick="newQuote()"/>
                    </td>
                </cong:tr>
                <cong:tr>
                    <th colspan="10">Quote List</th>
                </cong:tr>
                <tr>
                    <td colspan="10">
                        <div id="results">
                            <c:choose>
                                <c:when test="${not empty quoteList}">
                                    <div class="result-container">
                                        <table width="100%" cellpadding="0" cellspacing="1" class="display-table">
                                            <thead>
                                                <tr>
                                                    <th>Q</th>
                                                    <th>BK</th>
                                                    <th>BL</th>
                                                    <th>Shipment Id</th>
                                                    <th>Origin</th>
                                                    <th>Destination</th>
                                                    <th>Distance</th>
                                                    <th>Ship Date</th>
                                                    <th>CBM/CFT</th>
                                                    <th>Quote By</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:set var="zebra" value="odd"/>
                                                <c:forEach var="quote" items="${quoteList}">
                                                    <tr class="${zebra}">
                                                        <td>
                                                            <c:if test="${empty quote.bookedOn}">
                                                                <img src="${path}/img/icons/yellow.gif" border="0" />
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:if test="${not empty quote.bookedOn}">
                                                                <img src="${path}/img/icons/yellow.gif" border="0" />
                                                            </c:if>
                                                        </td>
                                                        <td></td>
                                                        <td>${quote.shipmentId}</td>
                                                        <td>${quote.originCity},${quote.originState},${quote.originZip}</td>
                                                        <td>${quote.destinationCity},${quote.destinationState},${quote.destinationZip}</td>
                                                        <td>${quote.miles}</td>
                                                        <td>${quote.shipDate}</td>
                                                        <td>${quote.cube}</td>
                                                        <td>${quote.rateBy.firstName}</td>
                                                        <td class="align-center">
                                                            <img src="${path}/images/icons/edit.gif" title="Edit" onclick="editQuote('${quote.id}')"/>
                                                        </td>
                                                    </tr>
                                                    <c:choose>
                                                        <c:when test="${zebra=='odd'}">
                                                            <c:set var="zebra" value="even"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="zebra" value="odd"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="table-banner green" style="background-color: #D1E6EE;">No Quote found</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>
<script type="text/javascript">
    function editQuote(id){
        $('#quoteId').val(id);
        $('#methodName').val("editQuote");
        $('#rateQuoteForm').submit();
    }
    function searchQuote(){
        $('#methodName').val("searchQuote");
        $('#rateQuoteForm').submit();
    }
    function newQuote(){
        $('#methodName').val("newQuote");
        $('#rateQuoteForm').submit();
    }
</script>
