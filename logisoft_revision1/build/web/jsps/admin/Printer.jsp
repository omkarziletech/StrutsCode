<%@ page language="java" pageEncoding="ISO-8859-1"
         import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.User,com.gp.cong.logisoft.bc.admin.AdminConstants,com.gp.cong.logisoft.domain.Printer,com.gp.cong.logisoft.domain.UserPrinterAssociation,com.gp.cong.logisoft.beans.PrinterBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String modify = "";
            if (session.getAttribute("view") != null) {
                modify = (String) session.getAttribute("view");
            }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">
        <title>Printer Page</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/css/default/style.css">
        <link type="text/css" rel="stylesheet" media="screen" href="${path}/jsps/LCL/css/lable-fields.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <%@include file="../../jsps/preloader.jsp"%>
        <script type="text/javascript">
            jQuery(document).ready(function() {
                jQuery("[title != '']").not("link").tooltip();
            });
            function save() {
                if (document.printerForm.documentId.options[document.printerForm.documentId.selectedIndex].value == "0") {
                    $.prompt("Please select the Document Name",{
                        callback: function() {
                            jQuery("#documentId").val("").callFocus();
                        }
                    });
                    return false;
                } else if (document.printerForm.printerName.options[document.printerForm.printerName.selectedIndex].value == "0") {
                    $.prompt("Please select the Printer Name",{
                        callback: function() {
                            jQuery("#printerName").val("").callFocus();
                        }
                    });
                    return false;
                }
                showLoading();
                document.printerForm.buttonValue.value = "save";
                document.printerForm.submit();
            }
            function refreshTray(){
                showLoading();
                document.printerForm.buttonValue.value = "tray";
                document.printerForm.submit();
            }
            function confirmdelete(val1) {
                $.prompt("Are you sure you want to delete this Document", {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v === 1) {
                            showLoading();
                            document.printerForm.printerId.value = val1;
                            document.printerForm.buttonValue.value = "delete";
                            document.printerForm.submit();
                            $.prompt.close();
                        }else if (v === 2) {
                            $.prompt.close();
                        }
                    }
                });
            }
            function disabled(val) {
                if ((val == 0 || val == 3) && val != "") {
                    var imgs = document.getElementsByTagName('img');
                    for (var k = 0; k < imgs.length; k++) {
                        if (imgs[k].id != "cancel") {
                            imgs[k].style.visibility = 'hidden';
                        }
                    }
                    var input = document.getElementsByTagName("input");
                    for (i = 0; i < input.length; i++) {
                        if (input[i].id != "buttonValue") {
                            input[i].disabled = true;
                        }
                    }
                    var select = document.getElementsByTagName("select");
                    for (i = 0; i < select.length; i++) {
                        select[i].disabled = true;
                    }
                }
            }
        </script>
    </head>
    <body>
        <html:form action="/printer" scope="request">
            <div align="right">
                <input type="button" class="buttonStyleNew" value="Save" id="note" onclick="save()">
                <input type="button" class="buttonStyleNew" value="Go Back" id="cancel"	onClick="parent.parent.GB_hide();">
            </div>
            <table width="100%" border="0" class="tableBorderNew" style="margin-top: 4px">
                <tr><td class="tableHeadingNew" colspan="5">Printer Details</td></tr>
                <tr><td style="padding-top: 3px;" colspan="5"></td></tr>
                <tr>
                    <td id="labelText">Document Name</td>
                    <td>
                        <html:select property="documentId" styleId="documentId" styleClass="selectboxstyle" style="width:250px">
                            <html:optionsCollection name="documentList" />
                        </html:select>
                    </td>
                    <td>&nbsp;</td>
                    <td id="labelText">&nbsp;Printer Name</td>
                    <td>
                        <html:select property="printerName" styleId="printerName"  onchange="refreshTray()"
                                     styleClass="selectboxstyle" style="width:210px">
                            <html:optionsCollection name="printerList" />
                        </html:select>
                    </td>

                </tr>
                <tr>
                    <td id="labelText">Printer Tray</td>
                    <td>
                        <html:select property="printerTray" styleId="printerTray"
                                     styleClass="selectboxstyle" style="width:210px">
                            <html:optionsCollection name="trayList" />
                        </html:select>
                    </td>
                    <td colspan="3"></td>
                </tr>
                <tr><td style="padding-top: 3px;" colspan="5"></td></tr>
            </table>
            <br>
            <div id="divtablesty1" style="overflow:auto;">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                    <tr><td class="tableHeadingNew">List of Printers</td></tr>
                    <tr><td>
                            <table class="dataTable" id="printertable">
                                <thead>
                                    <tr>
                                        <th>Printer Type</th>
                                        <th>Module</th>
                                        <th>Printer Name</th>
                                        <th>Printer Tray</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${PRINTER}" var="print">
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'oddStyle'}">
                                                <c:set var="rowStyle" value="evenStyle"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="oddStyle"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <td>${print.documentId.documentName}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${print.documentId.screenName eq 'LCLBooking'}">LCL Export</c:when>
                                                    <c:when test="${print.documentId.screenName eq 'LCLIMPBooking'}">LCL Import</c:when>
                                                    <c:otherwise>${print.documentId.screenName}</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${print.printerName}</td>
                                            <td>${print.printerTray}</td>
                                            <td>
                                                <img src="${path}/img/icons/delete.gif" title="Delete" class="hotspot"
                                                     alt="deletePrinterIcon" id="note" onclick="confirmdelete('${print.documentId.id}')"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                        </td></tr>
                </table>
            </div>
            <html:hidden property="buttonValue" styleId="buttonValue" />
            <html:hidden property="index" />
            <html:hidden property="printerId"/>
        </html:form>
        <script>disabled('<%=modify%>')</script>
    </body>
</html>

