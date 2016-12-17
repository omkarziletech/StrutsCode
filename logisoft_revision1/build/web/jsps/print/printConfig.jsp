<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
            <base href="${basePath}"/>
        <title>Print Config</title>
        <style type="text/css">
            #docListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 0;
                right: 0;
                top: 10%;
            }

            #attachListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 25%;
                top: 15%;
                _height: expression(document.body.offsetHeight + "px");
            }
            #addDocumentListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 25%;
                right: 70%;
                top: 10%;
                bottom: 90%;
                _height: expression(document.body.offsetHeight + "px");
            }
        </style>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <%@include file="../includes/baseResourcesForJS.jsp" %>
        <script type="text/javascript" src="${path}/js/script.aculo.us/builder.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>

    </head>
    <body class="whitebackgrnd">
        <div id="cover"></div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="${path}/img/icons/newprogress_bar.gif" >
            </div>
        </div>
        <html:form name="printConfigForm" type="com.gp.cong.logisoft.struts.form.PrintConfigForm" action="/printConfig" scope="request">
            <c:choose>
                <c:when test="${not empty printList}">
                    <table align="center" id="mainTable" width="100%" border="0" cellpadding="0"  cellspacing="0" class="tableBorderNew">
                        <tr class="tableHeadingNew">
                            <td>Document List </td>
                        </tr>
                        <tr>
                            <c:set var="rowIndex" value="0"/>
                            <c:if test="${not empty printList}">
                                <td valign="top">
                                    <display:table name="${printList}" class="displaytagstyleNew" id="printList" style="width:100%;">
                                        <display:setProperty name="paging.banner.placement" value="none"/>
                                        <display:column title="Document Name" property="documentName" sortable="true" headerClass="sortable"/>
                                        <display:column title="Screen Name" property="screenName" sortable="true" headerClass="sortable"/>
                                        <display:column title="Allow Print" property="allowPrint" sortable="true" headerClass="sortable" value="${printList.allowPrint}"/>
                                        <display:column title="Action">

                                            <img alt="Edit" src="${path}/img/icons/edit.gif" border="0" onclick="edit('${rowIndex}');"
                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);"/>&nbsp;&nbsp;
                                            <img alt="Delete" src="${path}/img/icons/delete.gif" border="0" onclick="deletePrint('${rowIndex}');"
                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);"/>

                                            <input type="hidden" name="docType${rowIndex}" id="docType${rowIndex}" value="${printList.documentType}"/>
                                            <input type="hidden" name="docName${rowIndex}" id="docName${rowIndex}" value="${printList.documentName}"/>
                                            <input type="hidden" name="docId${rowIndex}" id="docId${rowIndex}" value="${printList.id}"/>
                                            <input type="hidden" name="listScreenName${rowIndex}" id="listScreenName${rowIndex}" value="${printList.screenName}"/>
                                            <input type="hidden" name="allowPrint${rowIndex}" id="allowPrint${rowIndex}" value="${printList.allowPrint}"/>
                                        </display:column>
                                        <c:set var="rowIndex" value="${rowIndex+1}"/>
                                    </display:table>
                                </td>
                            </c:if>
                        </tr>
                    </table>
                </c:when>
                <c:otherwise>
                    No Documents Found
                </c:otherwise>
            </c:choose>
            <input type="hidden" id="id" name="id"/>
            <html:hidden property="fileLocation"/>
            <html:hidden property="pageAction"/>
            <html:hidden property="documentType"/>
            <html:hidden property="fileNumber" value="${printConfigForm.fileNumber}"/>

            <div id="addDocumentListDiv" style="display: none; height:150px;width:350px;">
                <table width="100%" border="0" cellpadding="2" cellspacing="0">
                    <tr class="tableHeadingNew" >
                        <td>Document List</td>
                        <td align="right">
                            <div style="vertical-align: top">
                                <a id="lightBoxClose" href="javascript:closeDocumentDiv();">
                                    <img src="${path}/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                                </a>
                            </div>
                        </td>
                    </tr>
                    <tr class="textLabels">
                        <td align="right">Screen Name<font color="red">*</font></td>
                        <td>
                            <input type="text" name="screenName" id="screenName" class="BackgrndColorForTextBox" readonly="true" value="${printConfigForm.screenName}">
                        </td>
                    </tr>
                    <tr class="textLabels">
                        <td align="right">Document Name<font color="red">*</font></td>
                        <td><input type="text" name="documentName" style="width:200px" id="documentName" class="BackgrndColorForTextBox" readonly="true" value="${printConfigForm.documentName}"></td>
                    </tr>
                    <tr class="textLabels">
                        <td align="right">Allow Print<font color="red">*</font></td>
                        <td>
                            <html:checkbox property="allowPrint" name="printList" styleId="allowPrint"/>
                        </td>
                    </tr>
                    <%--   <tr class="textLabels">
                        <td align="right">Printer Name<font color="red">*</font></td>
                        <td>
                            <html:select property="printerName" style="width:200px" styleClass="textlabelsBoldForTextBox" value="${printConfigForm.printerName}" styleId="printerName">
                                <html:optionsCollection name="printersList"/>
                            </html:select>
                        </td>
                    </tr>  --%>
                    <tr>
                        <td colspan="2" align="center">
                            <div id="updateButton">
                                <input type="button" value="update" class="buttonStyleNew" onclick="submitDocument()">
                                <input type="button" value="cancel" class="buttonStyleNew" onclick="closeDocumentDiv()">
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </html:form>
    </body>
    <script language="javascript">
        function edit(index) {
            showPopUp();
            var allowPrint = jQuery("#allowPrint" + index).val();
            if (undefined != allowPrint && allowPrint == 'Yes') {
                document.getElementById("allowPrint").checked = true;
            } else {
                document.getElementById("allowPrint").checked = false;
            }
            document.getElementById("addDocumentListDiv").style.display = 'block';
            jQuery("#documentName").val(jQuery("#docName" + index).val());
            jQuery("#id").val(jQuery("#docId" + index).val());
            jQuery("#screenName").val(jQuery("#listScreenName" + index).val());
        }
        function deletePrint(index) {
            if (confirm("Are you sure Do you want to delete the Document")) {
                jQuery("#id").val(jQuery("#docId" + index).val());
                document.printConfigForm.pageAction.value = "deletePrintConfig";
                document.printConfigForm.submit();
            }
        }

        function submitDocument() {
            document.printConfigForm.pageAction.value = "UpdatePrintConfig";
            document.printConfigForm.submit();
        }
        function closeDocumentDiv() {
            document.getElementById("addDocumentListDiv").style.display = 'none';
            closePopUp();
        }

    </script>

</html>
