<%-- 
    Document   : lclInlandVoyagePopup
    Created on : Jun 26, 2013, 3:27:05 PM
    Author     : VinothS
--%>

<%@include file="init.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<body>
    <cong:form  id="lclBookingForm"  name="lclBookingForm" action="/lclBooking.do" >
        <cong:table id="inlandInfo" cellpadding="0" cellspacing="0" width="100%" border="0">
            <cong:tr styleClass="tableHeadingNew">
                <cong:td width="90%">Inland Voyage Information</cong:td>
            </cong:tr>
        </cong:table>
        <cong:div style="width:99%; float:left; height:220px; overflow-y:scroll; border:1px solid #dcdcdc">
            <display:table name="${inlandVoyInfo}"  id="inlandVoyInfo" class="dataTable" requestURI="/lclBooking.do" style="width:100%">
                <display:column title="ECI Voy#">
                    ${inlandVoyInfo.scheduleNo}
                </display:column>
                <display:column title="Sail Date">
                    <fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${inlandVoyInfo.sailDate}"></fmt:formatDate>
                    <span> ${sailDate}</span>
                </display:column>
                <display:column title="SS Line">
                    ${inlandVoyInfo.acctName}
                </display:column>
                <display:column title="Vessel Name">
                    ${inlandVoyInfo.vesselName}
                </display:column>
                <display:column title="SS Voy">
                    ${inlandVoyInfo.ssVoy}
                </display:column>
                <display:column title="Pier">
                    ${inlandVoyInfo.departurePier}
                </display:column>
                <display:column title="Unit#">
                    ${inlandVoyInfo.unitNo}
                </display:column>
                <display:column title="Size">
                    ${inlandVoyInfo.size}
                </display:column>
            </display:table>
        </cong:div>
    </cong:form>
</body>

