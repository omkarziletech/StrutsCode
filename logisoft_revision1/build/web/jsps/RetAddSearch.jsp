<%-- 
    Document   : RetAddSearch
    Created on : Jul 9, 2010, 3:14:06 PM
    Author     : Vinay
--%>

<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="com.gp.cong.logisoft.util.CommonFunctions"%>
<%@page import="java.util.Collections"%>
<%@page import="org.hibernate.mapping.Collection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/jsps/includes/jspVariables.jsp"%>
<%@include file="/jsps/includes/baseResources.jsp"%>
<%@include file="/jsps/includes/resources.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>RetAdd Search</title>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/default/style.css">
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css">
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"/>
    </head>
    <body>
        <html:form action="/retAddSearch" type="com.gp.cong.logisoft.struts.form.RetAddSearchForm" name="retAddSearchForm"
                   method="post" scope="request">
            <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%" >
                <tr class="tableHeadingNew"><td colspan="6">Search Criteria</td></tr>
                <tr><td colspan="6"></td></tr>
                <tr>
                    <td id="labelText">&nbsp; Origin Terminal</td>
                    <td>
                        <input type="text" class="textlabelsBoldForTextBox" name="oTerm" id="oTerm" />
                        <input name="ot_check" id="ot_check" type="hidden" />
                        <input name="origTerm" id="origTerm" type="hidden"/>
                        <div id="ot_choices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("oTerm","ot_choices","origTerm","ot_check","${path}/servlet/AutoCompleterServlet?action=OTerm&textFieldId=oTerm","");
                        </script>
                    </td>
                    <td id="labelText">&nbsp; Destination Port</td>
                    <td>
                        <input type="text" class="textlabelsBoldForTextBox" name="desPort" id="desPort"/>
                        <input name="destination" id="destination" type="hidden"/>
                        <input name="dp_check" id="dp_check" type="hidden"/>
                        <div id="destin_choices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("desPort","destin_choices","destination","dp_check","${path}/servlet/AutoCompleterServlet?action=Destin&textFieldId=desPort","");
                        </script>
                    </td>
                    <td id="labelText">&nbsp; User Name</td>
                    <td>
                        <input type="text" class="textlabelsBoldForTextBox" name="usrName" id="usrName"/>
                        <html:hidden property="userName" styleId="userName"/>
                        <input name="usr_check" id="usr_check" type="hidden"/>
                        <div id="usr_choices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocomplete("usrName", "usr_choices", "userName", "usr_check", "${path}/servlet/AutoCompleterServlet?action=RetAddUser&textFieldId=usrName", "");
                        </script>
                    </td>
                </tr>
                <tr>
                    <td colspan="3" align="right"><input class="buttonStyleNew" type="submit" value="Search" >&nbsp;</td>
                    <td colspan="3" align="left"><input class="buttonStyleNew" type="reset" value="Reset"></td>
                </tr>
                <tr><td colspan="6"></td></tr>
            </table>
            &nbsp;
            <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%" >
                <c:if test="${param.action=='update' || param.action=='edit' || param.action=='cancel'}">
                    <tr class="tableHeadingNew"><td>Search Results</td></tr>
                    <tr>
                        <td align="center">
                            <display:table name="${searchResults}" defaultorder="descending" class="dataTable"
                                           id="retAdd" style="width:100%" sort="list" requestURI="/retAddSearch.do">
                                <display:column title="Origin Terminal" property="originTerminal" sortable="true" headerClass="sortable" />
                                <display:column title="Destination Port" property="destinationPort" sortable="true" headerClass="sortable" />
                                <display:column title="Code" property="code" sortable="true" headerClass="sortable" />
                                <display:column title="User Name" property="userName" sortable="true" headerClass="sortable" />
                                <display:column title="E-Mail" property="email" sortable="true" headerClass="sortable" />
                                <display:column title="Department" property="dept" sortable="true" headerClass="sortable" />
                                <display:column title="Login ID" property="loginID" sortable="true" headerClass="sortable" />
                                <display:column title="Action" >
                                    <span title="Edit">
                                        <img src="${path}/img/icons/edit.gif" alt="Edit"
                                             onclick="edit('${retAdd.retAddId}')" >
                                    </span>
                                </display:column>
                            </display:table>
                        </td>
                    </tr>
                </c:if>
            </table>
            <input type="hidden" name="action" value="update"/>
            <html:hidden property="retAddId" styleId="retAddId" value="${retAddSearchForm.retAddId}"/>
        </html:form>
    </body>
    <script type="text/javascript">
        jQuery(document).ready(function() {
            jQuery("[title != '']").not("link").tooltip();
        });
        function edit(retAddId) {
            jQuery('#retAddId').val(retAddId)
            document.retAddSearchForm.action.value = "get";
            document.retAddSearchForm.submit();
        }
       
    </script>
</html>
