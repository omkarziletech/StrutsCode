<%-- 
    Document   : search
    Created on : Mar 18, 2014, 12:48:43 PM
    Author     : palraj.p
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>KN Booking Search</title>
        <%@include file="/jsps/LCL/init.jsp" %>
        <%@include file="/jsps/LCL/colorBox.jsp" %>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="/taglib.jsp" %>
        <%@include file="/jsps/includes/baseResources.jsp" %>
        <%@include file="/jsps/includes/resources.jsp" %>
        <%@include file="/jsps/includes/jspVariables.jsp" %>

        <link rel="stylesheet" type="text/css" href="${path}/css/pikaday.css"/>
        <script type="text/javascript" src="${path}/jsps/LCL/js/knBooking.js"></script>
        <script type="text/javascript" src="${path}/jsps/LCL/js/pickaday.js"></script>
        <script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/datejs/1.0/date.min.js"></script>
        <script type="text/javascript" src="http://dbushell.github.io/Pikaday/plugins/pikaday.jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>

    </head>
    <body>
        <br/>
        <div id="search-criteria" class="head-tag font-14px">
            Search Criteria
        </div>
        <br/>
        <html:form action="/kn/search.do" name="searchForm" styleId="searchForm"
                   type="com.gp.cvst.logisoft.struts.lcl.kn.form.SearchForm" scope="request" method="post">
            <table class="no-border ">
                <tr>
                    <td class="label">
                        Booking Number
                    </td>
                    <td>
                        <html:text property="bkgNo"  styleId="bkgNo" styleClass="textbox" onchange="search();"/>
                    </td>
                    <td class="label space">
                        Start Date
                    </td>
                    <td>
                        <html:text property="startDate" styleId="startDate" styleClass="textbox picker"/>
                        <input type="hidden" id="sDateHidden" value="${searchForm.startDate}"/>
                    </td>
                    <td class="label space">
                        End Date
                    </td>
                    <td>
                        <html:text  property="endDate" styleId="endDate" styleClass="textbox picker" />
                        <input type="hidden" id="eDateHidden" value="${searchForm.endDate}"/>
                    </td>
                    <td class="label space">
                        Limit
                    </td>
                    <td>
                        <html:select property="limitRecord" styleClass="dropdown" value="${searchForm.limitRecord}">
                            <html:option value="100"></html:option>
                            <html:option value="250"></html:option>
                            <html:option value="500"></html:option>
                            <html:option value="1000"></html:option>
                        </html:select>
                    </td>
                    <td colspan="3" class="space">
                        <input type="button" value="Search" class="button" onclick="search();" />
                        <input type="submit" value="Show All" class="button" onclick="showAll();" />
                        <input type="button"  value="Reset" class="button" onclick="doClear();">
                        <input type="button"  value="Export To Excel" class="button" onclick="exportToExcel();">
                    </td>
            </table>
            <br/>
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="bookingId" styleId="bookingId"/>
            <html:hidden property="sortBy" styleId="sortBy"/>
            <html:hidden property="searchBy" styleId="searchBy"/>
            <html:hidden property="bookingEnvelopeId" styleId="bookingEnvelopeId"/>
            <html:hidden property="searchType" styleId="searchType" value="${searchForm.searchType}"/>
            <div id="search-criteria" class="head-tag font-14px">
                Search Result
            </div>
            <%@include file="/jsps/LCL/kn/searchResults.jsp" %>
            <br/>
        </html:form>
    </body>
</html>



