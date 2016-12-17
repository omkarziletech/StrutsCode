<%-- 
    Document   : terminalGlMapping
    Created on : Jul 19, 2010, 12:32:07 PM
    Author     : Lakshmi Naryanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<html>
    <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
    <head>
        <base href="${basePath}"/>
        <title>Terminal Gl Mapping</title>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/terminalGlMapping" method="post" enctype="multipart/form-data" name="terminalGlMappingForm" type="com.logiware.form.TerminalGlMappingForm" scope="request">
            <c:if test="${not empty terminalGlMappingForm.message}">
                <c:choose>
                    <c:when test="${fn:contains(terminalGlMappingForm.message,'failed')}">
                        <div style="color: red;font-weight: bolder;">
                            <c:out value="${terminalGlMappingForm.message}"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div style="color: blue;font-weight: bolder;">
                            <c:out value="${terminalGlMappingForm.message}"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <html:hidden property="action"/>
            <c:choose>
                <c:when test="${terminalGlMappingForm.action=='add' || terminalGlMappingForm.action=='edit'}">
                    <div>
                        <table width="100%" border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">
                                <td>Terminal to Gl Mapping</td>
                                <td colspan="5" align="right">
                                    <input type="button" class="buttonStyleNew" value="Go Back" onclick="goBack()"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Terminal</td>
                                <td colspan="5">
                                    <c:choose>
                                        <c:when test="${terminalGlMappingForm.action=='add'}">
                                            <html:text property="terminalGlMapping.terminal" styleId="terminalId" styleClass="textlabelsBoldForTextBox"/>
                                        </c:when>
                                        <c:otherwise>
                                            <html:text property="terminalGlMapping.terminal" styleId="terminalId" styleClass="textlabelsBoldForTextBoxDisabledLook" readonly="true"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>LCLE-B</td>
                                <td><html:text property="terminalGlMapping.lclExportBilling" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>LCLE-L</td>
                                <td><html:text property="terminalGlMapping.lclExportLoading" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>LCLE-D</td>
                                <td><html:text property="terminalGlMapping.lclExportDockreceipt" styleClass="textlabelsBoldForTextBox"/></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>FCLE-B</td>
                                <td><html:text property="terminalGlMapping.fclExportBilling" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>FCLE-L</td>
                                <td><html:text property="terminalGlMapping.fclExportLoading" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>FCLE-D</td>
                                <td><html:text property="terminalGlMapping.fclExportDockreceipt" styleClass="textlabelsBoldForTextBox"/></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>AIRE-B</td>
                                <td><html:text property="terminalGlMapping.airExportBilling" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>AIRE-L</td>
                                <td><html:text property="terminalGlMapping.airExportLoading" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>AIRE-D</td>
                                <td><html:text property="terminalGlMapping.airExportDockreceipt" styleClass="textlabelsBoldForTextBox"/></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>LCLI-B</td>
                                <td><html:text property="terminalGlMapping.lclImportBilling" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>LCLI-L</td>
                                <td><html:text property="terminalGlMapping.lclImportLoading" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>FCLI-B</td>
                                <td><html:text property="terminalGlMapping.fclImportBilling" styleClass="textlabelsBoldForTextBox"/></td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>FCLI-L</td>
                                <td><html:text property="terminalGlMapping.fclImportLoading" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>AIRI-B</td>
                                <td><html:text property="terminalGlMapping.airImportBilling" styleClass="textlabelsBoldForTextBox"/></td>
                                <td>AIRI-L</td>
                                <td><html:text property="terminalGlMapping.airImportLoading" styleClass="textlabelsBoldForTextBox"/></td>                              
                            </tr>
                            <tr class="textlabelsBold">
                                 <td>INLE-L</td>
                                <td><html:text property="terminalGlMapping.inlandExportLoading" styleClass="textlabelsBoldForTextBox"/></td>
                            </tr>
                            <tr>
                                <td align="center" colspan="6">
                                    <c:choose>
                                        <c:when test="${terminalGlMappingForm.action=='add'}">
                                            <input type="button" class="buttonStyleNew" value="Save" onclick="save()"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="button" class="buttonStyleNew" value="Update" onclick="update()"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div>
                        <html:hidden property="terminal"/>
                        <table cellspacing="1" cellpadding="0" width="100%" class="tableBorderNew">
                            <tr class="tableHeadingNew">
                                <td>Terminal to GL Mapping</td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>
                                    Terminal Mapping Sheet
                                    <html:file property="terminalGlMappingSheet" styleClass="textlabelsBoldForTextBox"/>
                                    <input type="button" value="Import" onclick="importTerminalGlMapping()" class="buttonStyleNew"/>
                                    <input type="button" value="Add" onclick="add()" class="buttonStyleNew"/>
                                    <input type="button" value="Search" onclick="search()" class="buttonStyleNew"/>
                                    <input type="button" value="Clear" onclick="refresh()" class="buttonStyleNew"/>
                                </td>
                            </tr>
                            <tr class="tableHeadingNew">
                                <td>List of Terminals</td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="scrolldisplaytable">
                                        <display:table name="${terminalGlMappingForm.terminalGlMappings}" id="terminalGlMapping" pagesize="${pageSize}" class="displaytagstyleNew" style="width:100%">
                                            <display:setProperty name="paging.banner.placement" value="none"/>
                                            <display:column title="Terminal" property="terminal"/>
                                            <display:column title="LCLE-B" property="lclExportBilling"/>
                                            <display:column title="LCLE-L" property="lclExportLoading"/>
                                            <display:column title="LCLE-D" property="lclExportDockreceipt"/>
                                            <display:column title="FCLE-B" property="fclExportBilling"/>
                                            <display:column title="FCLE-L" property="fclExportLoading"/>
                                            <display:column title="FCLE-D" property="fclExportDockreceipt"/>
                                            <display:column title="AIRE-B" property="airExportBilling"/>
                                            <display:column title="AIRE-L" property="airExportLoading"/>
                                            <display:column title="AIRE-D" property="airExportDockreceipt"/>
                                            <display:column title="LCLI-B" property="lclImportBilling"/>
                                            <display:column title="LCLI-L" property="lclImportLoading"/>
                                            <display:column title="FCLI-B" property="fclImportBilling"/>
                                            <display:column title="FCLI-L" property="fclImportLoading"/>
                                            <display:column title="AIRI-B" property="airImportBilling"/>
                                            <display:column title="AIRI-L" property="airImportLoading"/>
                                            <display:column title="INLE-L" property="inlandExportLoading"/>
                                            <display:column title="Action">
                                                <img alt="" title="Edit" src="${path}/img/icons/edit.gif" onclick="editTerminalGlMapping('${terminalGlMapping.terminal}')"/>
                                                <img alt="" title="Delete" src="${path}/img/icons/delete.gif" onclick="deleteTerminalGlMapping('${terminalGlMapping.terminal}')"/>
                                                <img alt="" title="Notes" src="${path}/img/icons/info1.gif" onclick="GB_show('Notes', '${path}/notes.do?moduleId=TERMINAL_MAPPING&moduleRefId=${terminalGlMapping.terminal}',300,900);"/>
                                            </display:column>
                                        </display:table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </html:form>
    </body>
    <script type="text/javascript">
        function importTerminalGlMapping(){
            if(trim(document.terminalGlMappingForm.terminalGlMappingSheet.value)==""){
                alert("Please Include Terminal Mapping Sheet");
                return;
            }
            document.terminalGlMappingForm.action.value="importTerminalGlMapping";
            document.terminalGlMappingForm.submit();
        }
        function search(){
            document.terminalGlMappingForm.action.value="search";
            document.terminalGlMappingForm.submit();
        }
        function refresh(){
            document.terminalGlMappingForm.action.value="refresh";
            document.terminalGlMappingForm.submit();
        }
        function goBack(){
            document.terminalGlMappingForm.action.value="search";
            document.terminalGlMappingForm.submit();
        }
        function add(){
            document.terminalGlMappingForm.action.value="add";
            document.terminalGlMappingForm.submit();
        }
        function editTerminalGlMapping(terminal){
            document.terminalGlMappingForm.terminal.value=terminal;
            document.terminalGlMappingForm.action.value="edit";
            document.terminalGlMappingForm.submit();
        }
        function deleteTerminalGlMapping(terminal){
            document.terminalGlMappingForm.terminal.value=terminal;
            document.terminalGlMappingForm.action.value="delete";
            document.terminalGlMappingForm.submit();
        }
        function save(){
            if(trim(document.getElementById("terminalId").value)==""){
                alert("Please Enter the Terminal");
                return;
            }
            document.terminalGlMappingForm.action.value="save";
            document.terminalGlMappingForm.submit();
        }
        function update(){
            if(trim(document.getElementById("terminalId").value)==""){
                alert("Please Enter the Terminal");
                return;
            }
            document.terminalGlMappingForm.action.value="update";
            document.terminalGlMappingForm.submit();
        }
        jQuery(document).ready(function(){
            jQuery(document).keypress(function(event) {
                var keycode = (event.keyCode ? event.keyCode : event.which);
                if(keycode == 13) {
                    search();
                }
            });
        });
    </script>
</html>
