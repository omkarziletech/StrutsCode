<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<cong:form  id="lclTrackingForm"  name="lclTrackingForm" action="lclTracking.do" >
    <cong:table width="100%" style="margin:5px 0; float:left">
        <cong:tr>
            <cong:td styleClass="tableHeadingNew" colspan="4">
                <cong:div styleClass="floatLeft">Tracking For File No# <span style="color: red">${lclTrackingForm.fileNumber}</span>
                </cong:div>
            </cong:td>
        </cong:tr>
    </cong:table>
    <div style="width:99%; float:left; height:310px; overflow-y:scroll; border:1px solid #dcdcdc">
        <table class="dataTable" border="0">
            <thead>
                <tr>
                    <th width="20%">Description</th>
                    <th width="6%">Entered Date & Time</th>
                    <th width="6%">User</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${remarksList}" var="remarks">
                    <c:choose>
                        <c:when test="${zebra eq 'odd'}">
                            <c:set var="zebra" value="even"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="zebra" value="odd"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${zebra}">
                        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm:ss" var="entereddatetime"  value="${remarks.enteredDatetime}"/>
                        <td>${remarks.remarks}</td>
                        <td>${entereddatetime}</td>
                        <td>${remarks.enteredBy.loginName}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</cong:form>

