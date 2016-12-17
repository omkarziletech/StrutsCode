<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
	    <title>SS Masters Approved</title>
        <%@include file="../includes/jspVariables.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
    </head>
    <body class="whitebackgrnd">
        <%@include file="../preloader.jsp"%>
	<c:if test="${not empty param.message}">
	    <c:set var="message" value="${param.message}"/>
	</c:if>
	<c:if test="${not empty message}">
	    <div class="message">${message}</div>
	</c:if>
        <html:form action="/ssMastersApproved" name="ssMastersApprovedForm" styleId="ssMastersApprovedForm"
                   type="com.gp.cvst.logisoft.struts.form.SSMastersApprovedForm" scope="request" onsubmit="showPreloading()">
            <html:hidden property="action" styleId="action"></html:hidden>
            <html:hidden property="pageNo" styleId="pageNo"/>
            <html:hidden property="noOfPages" styleId="noOfPages"/>
            <html:hidden property="currentPageSize" styleId="currentPageSize"/>
            <html:hidden property="totalPageSize" styleId="totalPageSize"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <html:hidden property="orderBy" styleId="orderBy"/>
            <html:hidden property="selectedFileNo" styleId="selectedFileNo"/>
            <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td colspan="8">Search Criteria</td>
                </tr>
                <tr class="textLabelsBold">
                    <td>Module Name</td>
                    <td>
                        <html:select property="moduleName" styleId="moduleName" styleClass="dropdown_accounting" style="width: 125px"  onchange="changeLable(this)">
                            <html:option value="ALL">ALL</html:option>
                            <html:option value="FCLE">FCLE</html:option>
                            <html:option value="FCLI">FCLI</html:option>
                            <html:option value="LCLE">LCLE</html:option>
                            <html:option value="LCLI">LCLI</html:option>
                            <html:option value="AIRE">AIRE</html:option>
                            <html:option value="LCLI">AIRI</html:option>
                        </html:select>
                    </td>
                    <td id="fileLable">File No</td>
                    <td><html:text property="fileNo"  styleId="fileNo" styleClass="textlabelsBoldForTextBox"></html:text></td>
			<td>SSL</td>
			<td><html:text property="sslineName" styleId="sslineName" 
                               styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase;"></html:text>
                        <html:hidden property="sslineNo" styleId="sslineNo" styleClass="textlabelsBoldForTextBox"/>
                        <input type="hidden" name="sslineNameValid" id="sslineNameValid" value="${ssMastersApprovedForm.sslineName}"/>
                        <div id="sslineNameDiv" style="display: none" class="autocomplete"></div>
                    </td>
                    <td>Booking No</td>
                    <td><html:text property="bookingNo" styleId="bookingNo" styleClass="textlabelsBoldForTextBox" 
                               style="text-transform:uppercase;"></html:text></td>
		    </tr>
		    <tr class="textLabelsBold">
			<td>Master No</td>
			<td><html:text property="masterNo" styleId="masterNo" styleClass="textlabelsBoldForTextBox" 
                               style="text-transform:uppercase;"></html:text></td>
			<td>Container No</td>
			<td><html:text property="containerNo" styleId="containerNo" 
                               styleClass="textlabelsBoldForTextBox" style="text-transform:uppercase;"></html:text></td>
			<td>Prepaid/Collect</td>
			<td>
                        <html:select property="prepaidOrCollect" styleId="prepaidOrCollect" styleClass="dropdown_accounting" style="width: 125px">
                            <html:option value="">Select One</html:option>
                            <html:option value="B-Both">B-Both</html:option>
                            <html:option value="P-Prepaid">P-Prepaid</html:option>
                            <html:option value="C-Collect">C-Collect</html:option>
                        </html:select>
                    </td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="8" align="center">
                        <input type="button" class="buttonStyleNew" value="Search" id="search">
                        <input type="button" class="buttonStyleNew" value="Clear" id="clear">
                    </td>
                </tr>
                <tr class="tableHeadingNew">
                    <td colspan="8">List Of Approved SS Master</td>
                </tr>
                <tr>
                    <td colspan="8">
                        <c:choose>
                            <c:when test="${ssMastersApprovedForm.moduleName == 'LCLE'}">
                                <c:import url="lclSsMastersApprovedResults.jsp"></c:import>
                            </c:when>
                            <c:when test="${ssMastersApprovedForm.moduleName == 'ALL'}">
                                <c:import url="ssMastersAllApprovedResults.jsp"></c:import>
                            </c:when>
                            <c:otherwise>
                                <c:import url="ssMastersApprovedResults.jsp"></c:import>
                            </c:otherwise>
                        </c:choose>
			</td>
		    </tr>
		</table>
        </html:form>
    </body>
    <script type="text/javascript" src="${path}/js/Accounting/AccountsPayable/ssMastersApproved.js"></script>
</html>
