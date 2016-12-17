<%-- 
    Document   : upcomingSailingResult
    Created on : Jun 22, 2015, 8:25:49 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BL Upcoming sailing List</title>
        <%@include file="../init.jsp" %>
        <%@include file="../../includes/jspVariables.jsp"%>
        <%@include file="../../../jsps/includes/baseResources.jsp" %>
        <%@include file="../../includes/resources.jsp" %>
    </head>
    <body>
        <div id="pane" style="overflow: auto;">
           <input type="hidden" id="existSailingsId" value="${sailingsId}"/>
            <table class="widthFull" style="margin: 0;overflow: hidden"  border="0">
                <tr class="tableHeadingNew"><td>Upcoming Sailings
                    </td>
                </tr>
                <tr>
                    <td>
                        <table class="dataTable" id="upcomingSailing" border="0">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Vessel</th>
                                    <th>Line</th>
                                    <th>ECI Voy</th>
                                    <th>SSL Voy</th>
                                    <th>Origin LRD</th>
                                    <th>Load LRD</th>
                                    <th>ETD at POL</th>
                                    <th>Pier</th>
                                    <th>ETA at POD</th>
                                    <th>ETA FD</th>
                                    <th>TT(POL-POD)</th>
                                    <th>TT(POO-FD)</th>
                                </tr>
                            </thead>
                            <c:if test="${not empty voyageList}">
                                <tbody>
                                    <c:forEach var="voyage" items="${voyageList}">
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="pooLrdt" value="${voyage.originLrd}"/>
                                            <fmt:formatDate pattern="dd-MMM-yyyy" var="polLrdt" value="${voyage.loadLrd}"/>
                                            <fmt:formatDate pattern="dd-MMM-yyyy" var="polEtd" value="${voyage.sailingDate}"/>
                                            <fmt:formatDate pattern="dd-MMM-yyyy" var="podEta" value="${voyage.podAtEta}"/>
                                            <fmt:formatDate pattern="dd-MMM-yyyy" var="fdEta" value="${voyage.fdEta}"/>
                                            <td>
                                                <input type="radio" name="sailings" id="sailingsId${voyage.ssHeaderId}"
                                                       ${sailingsId eq voyage.ssHeaderId ? "checked" :""}
                                                       value="${voyage.ssHeaderId}==${voyage.voyageNo}==${voyage.carrierName}==${voyage.ssVoyage}==${polEtd}==${voyage.vesselName}" onclick="setUpcomingSailings('${path}');">
                                            </td>
                                            <td>${voyage.vesselName}</td>
                                            <td>${voyage.carrierName}</td>
                                            <td>${voyage.voyageNo}</td>
                                            <td>${voyage.ssVoyage}</td>
                                            <td>${pooLrdt}</td>
                                            <td>${polLrdt}</td>
                                            <td>${polEtd}</td>
                                            <td>${voyage.departPier}&nbsp;${voyage.scheduleK}</td>
                                            <td>${podEta}</td>
                                            <td>${fdEta}</td>
                                            <td>${voyage.ttPolPod}</td>
                                            <td>${voyage.ttPooFd}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </c:if>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
    </body>
    <script type="text/javascript">
        function setUpcomingSailings(path){
            var checkedVoySailings=$('input:radio[name=sailings]:checked').val();
            var existSailingsVal=$('#existSailingsId').val();
            var sailingsVal=checkedVoySailings.split("==");
            if(existSailingsVal!==sailingsVal[0]){
                $.prompt("Are you sure?.you want to change upcoming sailings", {
                    buttons: {
                        Ok: 1,
                        Cancel: 2
                    },
                    submit: function(v) {
                        if (v === 1) {
                            showProgressBar();
                            var params="&fileId="+parent.$("#fileId").val()+"&sailingsId="+sailingsVal[0];
                            $.ajax({url: path + "/lclBl.do?methodName=updateSailings" + params, success: function (result) {
                                    if(result){
                                        parent.$('#masterScheduleNo').val(sailingsVal[0]);
                                        parent.$('#existSailingsId').val(sailingsVal[0]);
                                        parent.$('#eciVoyage').val(sailingsVal[1]);
                                        parent.$('#ssLine').val(sailingsVal[2]);
                                        parent.$('#ssVoyage').val(sailingsVal[3]);
                                        parent.$('#sailDate').val(sailingsVal[4]);
                                        parent.$('#vesselName').val(sailingsVal[5]);
                                        hideProgressBar();
                                        $.prompt.close();
                                        parent.$.fn.colorbox.close();
                                    }
                                }});

                        } else if (v === 2) {
                            $('#sailingsId'+existSailingsVal).attr('checked', true);
                            $.prompt.close();
                        }
                    }
                });

           
               
              
            }
        }
    </script>
</html>
