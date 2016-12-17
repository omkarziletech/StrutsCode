<%-- 
    Document   : fclDataSearch
    Created on : Jul 3, 2010, 4:29:25 PM
    Author     : Vinay
--%>

<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="com.gp.cong.logisoft.util.CommonFunctions"%>
<%@page import="java.util.Collections"%>
<%@page import="org.hibernate.mapping.Collection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="includes/jspVariables.jsp" %>
<%@include file="includes/baseResources.jsp" %>
<%@include file="includes/resources.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search FCL Entries</title>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
    </head>
    <body class="whitebackgrnd" >
        <%@ page language="java" import="java.util.List, java.util.ArrayList, com.gp.cong.logisoft.hibernate.dao.FCLData;"%>
        <html:form action="/fclDataSearch" type="com.gp.cong.logisoft.struts.form.FCLSearchForm" name="fclSearchForm" method="post" >
            <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%" >
                <tr class="tableHeadingNew" >Search Criteria</tr>
                <tr class="textlabelsBold">
                    <td>&nbsp;Origin Terminal</td>
                    <td>
                        <input type="text" class="textlabelsBoldForTextBox" name="origTerm" id="origTerm" size="20"/>
                        <input name="originTerminal" id="originTerminal" type="hidden"/>
                        <input name="ot_check" id="ot_check" type="hidden"/>
                        <div id="origterm_choices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("origTerm","origterm_choices","originTerminal","ot_check","${path}/servlet/AutoCompleterServlet?action=OriginalTerminal&textFieldId=origTerm","");
                        </script>
                    </td>
                    <td>&nbsp;Destination Port</td>
                    <td>
                        <input type="text" class="textlabelsBoldForTextBox" name="desPort" id="desPort" size="20"/>
                        <input name="destinationPort" id="destinationPort" type="hidden"/>
                        <input name="dp_check" id="dp_check" type="hidden"/>
                        <div id="desport_choices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("desPort","desport_choices","destinationPort","dp_check","${path}/servlet/AutoCompleterServlet?action=DestinationPort&textFieldId=desPort","");
                        </script>
                    </td>
                    <td>&nbsp;Port of Exit</td>
                    <td>
                        <input type="text" class="textlabelsBoldForTextBox" name="portOfExit">
                    </td>
                    <td>&nbsp;SSL Name</td>
                    <td>
                        <input type="text" class="textlabelsBoldForTextBox" name="sslineName" id="sslineName" size="20"/>
                        <input name="sslineNo" id="sslineNo" type="hidden"/>
                        <input name="ssl_check" id="ssl_check" type="hidden"/>
                        <div id="ssl_choices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("sslineName","ssl_choices","sslineNo","ssl_check","${path}/servlet/AutoCompleterServlet?action=SSLineName&textFieldId=sslineName","");
                        </script>
                    </td>
                </tr>
                <tr>
                    <td colspan="8" align="center">
                        <input class="buttonStyleNew" type="submit" value="Search" onclick="fill()">
                        <input class="buttonStyleNew" type="reset" value="Reset">
                    </td>
                </tr>
            </table>
            &nbsp;
            <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%" >
                <tr class="tableHeadingNew" >Search Results</tr>
                <tr>
                    <td align="center">
                        <c:if test="${param.action=='update'||param.action=='edit'}">
                            <div class="scrolldisplaytable" style="width: 100%;height: 100%">
                                <display:table name="${searchResults}" defaultorder="descending" class="displaytagstyleNew"
                                               id="documentListItem" style="width:100%" sort="list" pagesize="50" requestURI="/fclDataSearch.do">
                                    <display:column title="ID" property="id" sortable="true" headerClass="sortable" />
                                    <display:column title="Origin Terminal" property="origTerm" sortable="true" headerClass="sortable" />
                                    <display:column title="Destination Port" property="desPort" sortable="true" headerClass="sortable" />
                                    <display:column title="Days in Transit" property="daysInTransit" sortable="true" headerClass="sortable" />
                                    <display:column title="SSLine" property="sslineName" sortable="true" headerClass="sortable" />
                                    <display:column title="SSLine #" property="sslineNo" sortable="true" headerClass="sortable" />
                                    <display:column title="Port of Exit" property="portOfExit" sortable="true" headerClass="sortable" />
                                    <display:column title="Remarks" property="remarks" maxLength="50" sortable="true" headerClass="sortable" />
                                    <display:column title="" class="buttonStyleNew"  sortable="false" >
                                        <input type="submit" name="currfdId" id="currfdId" class="buttonStyleNew" value="Edit"
                                       onclick="edit('${documentListItem.id}')" />
                                    </display:column>
                                </display:table>
                            </div>
                        </c:if>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="action" value="update"/>
            <input type="hidden" name="id" id="id" />
        </html:form>

    </body>

    <script type="text/javascript">
        function fill() {
            if(document.originTerminal.value.equals(null)) {
                document.originTerminal.value = '0';
            }
            if(document.destinationPort.value.equals(null)) {
                document.destinationPort.value = '0';
            }
        }

        function edit(id) {
            document.fclSearchForm.id.value = id;
            document.fclSearchForm.action.value = "get"
        }
    </script>
</html>
