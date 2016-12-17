<%@page import="com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <base href="${basePath}"/>
        <title>Scan/Attach</title>
        <style type="text/css">
            #docListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border: 1px solid;
                background-color: whitesmoke;
                left: 5px;
                right: 0;
                top: 0%;
                border: 3px outset rgb(148, 167, 150);
                padding:3px;
                margin: 15px 0px;
                box-shadow: 0 0 5px #888;
                border-radius: .5em;
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

            #masterBlDiv {
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
            div.autocomplete ul{
                width: 200px !important;
                height: 200px;
            }
        </style>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
        <script type="text/javascript" src="${path}/dwr/interface/UploadDownloaderDWR.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <%@include file="../includes/baseResourcesForJS.jsp" %>
        <script type="text/javascript" src="${path}/js/script.aculo.us/builder.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
    </head>
    <body>
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">

            </p>
            <div style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                           grayOut(false, '');">
            </div>
        </div>
        <div id="cover"></div>
        <div id="masterBlDiv" style="display: none;width:300px;height:60px;">
            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="1" class="tableBorderNew">
                <tr>
                <tr class="tableHeadingNew">
                    <td colspan="2">Master BL</td>
                </tr>
                <td class="textlabelsBold">MASTER BL # </td>
                <td>
                    <input type="text" class="textlabelsBoldForTextBox" maxlength="20" name="newMasterBL"
                           size="22" style="text-transform: uppercase" id="newMasterBL"/>
                    <div id="newMasterBL_choices" class="autocomplete"></div>
                </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <input type="button" name='Ok' value="Submit" class="buttonStyleNew"
                               style='width: 60px' onclick="saveMasterBl();"/>
                        <input type="button" name='Cancel' class="buttonStyleNew" value='Cancel'
                               style='width: 50px' onclick="closeMasterBl();" />
                    </td>
                </tr>
            </table>
        </div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <div style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="${path}/img/icons/newprogress_bar.gif" >
            </div>
        </div>
        <html:form enctype="multipart/form-data" name="scanForm" type="com.gp.cong.logisoft.struts.form.ScanForm" action="/scan" scope="request">
            <%--<c:if test="${scanForm.screenName=='FCLFILE' && !empty sessionScope.loginuser && sessionScope.loginuser.role.roleDesc !='Admin'}">
                <span style="float: left"><font color="black" size="2"><b>FCL File Number:${masterBl}</b> </font>
                    <font color="red" size="4" ><b><c:out value="04-${scanForm.fileNumber}"/></b></font></span>
                </c:if>--%>
            <c:if test="${empty scanForm.fileNumber && !empty sessionScope.loginuser &&
                          sessionScope.loginuser.role.roleDesc =='Admin'}">
                  <input type="button"  class="buttonStyleNew" value="Add"
                         style="width: 40px;float: right" onclick="addDocument('add')"/>
            </c:if>
            <table align="center" width="100%" border="0" cellpadding="0"  cellspacing="0" >
                <tr>
                    <td align="center">
                        <font color="blue" size="3"><b><span
                                    id="attachMessage"></span> </b> </font>
                        <font color="red" size="3"><b><span
                                    id="attachError"></span> </b> </font>
                    </td>
                </tr>
            </table>
            <table align="center" id="mainTable" width="100%" border="0" cellpadding="0"  cellspacing="0" class="tableBorderNew">
                <tr>
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" >
                            <tr class="tableHeadingNew">
                                <td>Document List for
                                    <c:choose>
                                        <c:when test="${scanForm.screenName == 'INVOICE' || scanForm.screenName == 'AR BATCH'}">
                                            ${scanForm.screenName} : <font color="red">${scanForm.fileNumber}</font>
                                        </c:when>
                                        <c:when test="${scanForm.screenName == 'LCL UNITS' }">
                                            ${scanForm.screenName} : <font color="red">${scanForm.unitNo}</font>
                                        </c:when>
                                        <c:otherwise>
                                            File No ${scanForm.screenName} <font color="red">${scanForm.fileNumber}</font>
                                        </c:otherwise>
                                    </c:choose>
                                <td align="right">
                                    <input type="button" value="View All"  class="buttonStyleNew" onclick="viewAll('${rowIndex}')"/>
                                </td>
                                <c:if test="${empty scanForm.fileNumber && !empty sessionScope.loginuser && 
                                              sessionScope.loginuser.role.roleDesc =='Admin'}">
                                      <td align="right">
                                          <html:select property="screenNameFilter"  style="width:220px;" styleId="screenNameFilter"
                                                       styleClass="dropdown_accounting" value="${scanForm.screenName}" onchange="getByScreenName(this.value)">
                                              <html:optionsCollection name="filterScreenNameList" />
                                          </html:select>
                                      </td>
                                </c:if>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="reportdiv">
                            <c:set var="rowIndex" value="0"/>
                            <c:choose>
                                <c:when test="${not empty scanSubList1 || not empty scanSubList2}">
                                    <table width="100%" border="0" cellpadding="0"  cellspacing="0">
                                        <tr>
                                            <c:if test="${not empty scanSubList1}">
                                                <td valign="top">
                                                    <display:table name="${scanSubList1}" class="displaytagstyleNew" id="scanSubList1" style="width:100%;">
                                                        <display:setProperty name="paging.banner.placement" value="none"/>
                                                        <display:column title="Document Name" property="documentName" sortable="true" headerClass="sortable"/>
                                                        <c:if test="${empty scanForm.fileNumber && !empty loginuser && loginuser.role.roleDesc =='Admin'}">
                                                            <display:column title="Screen Name" property="screenName" sortable="true" headerClass="sortable"/>
                                                        </c:if>
                                                        <display:column title="Action">
                                                            <c:choose>
                                                                <c:when test="${!empty scanForm.fileNumber}">
                                                                    <c:choose>
                                                                        <c:when test="${roleDuty.viewAccountingScanAttach && (scanForm.screenName == 'INVOICE' || scanForm.screenName == 'AR BATCH')}">
                                                                            <img alt="View" src="${path}/img/icons/preview.gif" border="0" onclick="view('${rowIndex}')"
                                                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>View</strong>', null, event);"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img alt="Scan" src="${path}/img/icons/printer.gif" border="0" onclick="scan('${rowIndex}');"
                                                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Scan</strong>', null, event);"/>&nbsp;&nbsp;
                                                                            <img alt="Attach" src="${path}/img/icons/attach.gif" border="0" onclick="attachFiles('${rowIndex}');"
                                                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Attach</strong>', null, event);"/>&nbsp;&nbsp;
                                                                            <img alt="View" src="${path}/img/icons/preview.gif" border="0" onclick="view('${rowIndex}')"
                                                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>View</strong>', null, event);"/>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <c:if test="${scanSubList1.totalScan+scanSubList1.totalAttach !='0'}">
                                                                        <span class="hotspot">
                                                                            <c:out value="(${scanSubList1.totalScan+scanSubList1.totalAttach})"/>
                                                                        </span>
                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:if test="${!empty loginuser && loginuser.role.roleDesc =='Admin'}">
                                                                        <img alt="Edit" src="${path}/img/icons/edit.gif" border="0" onclick="edit('${rowIndex}');"
                                                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);"/>&nbsp;&nbsp;
                                                                        <img alt="Delete" src="${path}/img/icons/delete.gif" border="0" onclick="deleteScan('${rowIndex}');"
                                                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);"/>
                                                                    </c:if>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <input type="hidden" name="docType${rowIndex}" id="docType${rowIndex}" value="${scanSubList1.documentType}"/>
                                                            <input type="hidden" name="docName${rowIndex}" id="docName${rowIndex}" value="${scanSubList1.documentName}"/>
                                                            <input type="hidden" name="docId${rowIndex}" id="docId${rowIndex}" value="${scanSubList1.id}"/>
                                                            <input type="hidden" name="listScreenName${rowIndex}" id="listScreenName${rowIndex}" value="${scanSubList1.screenName}"/>
                                                        </display:column>
                                                        <c:set var="rowIndex" value="${rowIndex+1}"/>
                                                    </display:table>
                                                </td>
                                            </c:if>
                                            <c:if test="${not empty scanSubList2}">
                                                <td valign="top">
                                                    <display:table name="${scanSubList2}" class="displaytagstyleNew" id="scanSubList2" style="width:100%;">
                                                        <display:setProperty name="paging.banner.placement" value="none"/>
                                                        <display:column title="Document Name" property="documentName" sortable="true" headerClass="sortable"/>
                                                        <c:if test="${empty scanForm.fileNumber && !empty loginuser && loginuser.role.roleDesc =='Admin'}">
                                                            <display:column title="Screen Name" property="screenName" sortable="true" headerClass="sortable"/>
                                                        </c:if>
                                                        <display:column title="Action">
                                                            <c:choose>
                                                                <c:when test="${!empty scanForm.fileNumber}">
                                                                    <c:choose>
                                                                        <c:when test="${roleDuty.viewAccountingScanAttach && (scanForm.screenName == 'INVOICE' || scanForm.screenName == 'AR BATCH')}">
                                                                            <img alt="View" src="${path}/img/icons/preview.gif" border="0" onclick="view('${rowIndex}')"
                                                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>View</strong>', null, event);"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img alt="Scan" src="${path}/img/icons/printer.gif" border="0" onclick="scan('${rowIndex}');"
                                                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Scan</strong>', null, event);"/>&nbsp;&nbsp;
                                                                            <img alt="Attach" src="${path}/img/icons/attach.gif" border="0" onclick="attachFiles('${rowIndex}');"
                                                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Attach</strong>', null, event);"/>&nbsp;&nbsp;
                                                                            <img alt="View" src="${path}/img/icons/preview.gif" border="0" onclick="view('${rowIndex}')"
                                                                                 onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>View</strong>', null, event);"/>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <c:if test="${scanSubList2.totalScan+scanSubList2.totalAttach !='0'}">
                                                                        <span class="hotspot">
                                                                            <c:out value="(${scanSubList2.totalScan+scanSubList2.totalAttach})"/>
                                                                        </span>
                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:if test="${!empty loginuser && loginuser.role.roleDesc =='Admin'}">
                                                                        <img alt="Edit" src="${path}/img/icons/edit.gif" border="0" onclick="edit('${rowIndex}');"
                                                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Edit</strong>', null, event);"/>&nbsp;&nbsp;
                                                                        <img alt="Delete" src="${path}/img/icons/delete.gif" border="0" onclick="deleteScan('${rowIndex}');"
                                                                             onmouseout="tooltip.hide();" onmouseover="tooltip.show('<strong>Delete</strong>', null, event);"/>
                                                                    </c:if>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <input type="hidden" name="docType${rowIndex}" id="docType${rowIndex}" value="${scanSubList2.documentType}"/>
                                                            <input type="hidden" name="docName${rowIndex}" id="docName${rowIndex}" value="${scanSubList2.documentName}"/>
                                                            <input type="hidden" name="docId${rowIndex}" id="docId${rowIndex}" value="${scanSubList2.id}"/>
                                                            <input type="hidden" name="listScreenName${rowIndex}" id="listScreenName${rowIndex}" value="${scanSubList2.screenName}"/>
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
                        </div>
                    </td>
                </tr>
            </table>
            <html:hidden property="fileLocation" styleId="fileLocation"/>
            <html:hidden property="pageAction" styleId="pageAction"/>
            <html:hidden property="documentType" styleId="documentType"/>
            <html:hidden property="documentName" styleId="documentName"/>
            <html:hidden property="systemRule" styleId="systemRule" value="Documents"/>
            <html:hidden property="screenName" styleId="screenName" value="${scanForm.screenName}"/>
            <html:hidden property="fileNumber" styleId="fileNumber" value="${scanForm.fileNumber}"/>
            <html:hidden property="fileNo" styleId="fileNo" value="${scanForm.fileNo}"/>
            <html:hidden property="comments" styleId="comments"/>
            <html:hidden property="ssMasterStatus" styleId="ssMasterStatus"/>
            <html:hidden property="status" styleId="status" value="${scanForm.status}"/>
            <html:hidden property="ssMasterBl" styleId="ssMasterBl" value="${masterBl}"/>
            <html:hidden property="houseBl" styleId="houseBl" value="${scanForm.houseBl}"/>
        </html:form>
        <div id="addDocumentListDiv" style="display: none; height:150px;width:350px;background-color: #E6F2FF">
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
                        <html:select property="addScreenName"  style="width:220px;" styleId="addScreenName"
                                     styleClass="textlabelsBoldForTextBox" value="${scanForm.screenName}">
                            <html:optionsCollection name="scanScreenNameList" /></html:select>
                        </td>
                    </tr>
                <%--<tr class="textLabels">
                    <td align="right">Document Type<font color="red">*</font></td>
                    <td>
                    <html:select property="addDocumentType"  style="width:205px;" styleId="addDocumentType"
                                 styleClass="textlabelsBoldForTextBox" value="${scanForm.documentType}">
                        <html:optionsCollection name="scanScreenDocumentTypeList" /></html:select>
                    </td>
                </tr>--%>
                <tr class="textLabels">
                    <td align="right">Document Name<font color="red">*</font></td>
                    <td><input type="text" name="addDocumentName" id="addDocumentName" size="30" style="text-transform: uppercase;" onkeyup="validateDocumentName(this)"></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="hidden" id="documentId"/>
                        <div id="submitButton">
                            <input type="button" class="buttonStyleNew" value="submit" onclick="submitDocument('add')" >
                            <input type="button" class="buttonStyleNew" value="cancel" onclick="closeDocumentDiv()">
                        </div>
                        <div id="updateButton">
                            <input type="button" value="update" onclick="submitDocument('update')">
                            <input type="button" value="cancel" onclick="closeDocumentDiv()">
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </body>
    <script language="javascript">

        function scan(index) {
            var masterBl = parent.parent.document.getElementById('newMasterBL') ? parent.parent.document.getElementById('newMasterBL').value : "";
            document.getElementById("attachMessage").innerHTML = "";
            document.getElementById("attachError").innerHTML = "";
            document.getElementById('pageAction').value = "Scan";
            document.getElementById('documentType').value = document.getElementById('docType' + index).value;
            document.getElementById('documentName').value = document.getElementById('docName' + index).value;
            var fileNumber = document.getElementById('fileNumber').value;
            var document_name = document.getElementById('docName' + index).value;
            var screenName = document.getElementById('listScreenName' + index).value;
            if (trim(masterBl) == "" && document_name == "SS LINE MASTER BL") {
                showPopUp();
                document.getElementById("masterBlDiv").style.display = "block";
                floatDiv("masterBlDiv", document.body.offsetWidth / 3, document.body.offsetHeight / 4).floatIt();
            } else {
                DwrUtil.getCommentsForScanOrAttach("Scan", index, document_name, fileNumber, screenName, function(data) {
                    if (data) {
                        showPopUp();
                        var attachListDiv = createHTMLElement("div", "attachListDiv", "60%", "30%", document.body);
                        dwr.util.setValue("attachListDiv", data, {escapeHtml: false});
                    }
                });
            }
        }

        function saveScan(selectedDocument) {
            var masterStatus = "";
            document.getElementById('comments').value = document.getElementById('comment').value;
            var comments = document.getElementById('comments').value;
            if (null == comments || trim(comments) == "" &&
                (scanForm.screenName.value != 'INVOICE' && scanForm.screenName.value != 'AR BATCH' && scanForm.screenName.value != 'JOURNAL ENTRY')) {
                alert("Please Enter Comments");
                return false;
            }
            else {
                if (document.scanAttachForm.ssMasterStatus) {
                    for (var i = 0; i < document.scanAttachForm.ssMasterStatus.length; i++) {
                        if (document.scanAttachForm.ssMasterStatus[i].checked) {
                            document.scanForm.ssMasterStatus.value = document.scanAttachForm.ssMasterStatus[i].value;
                            masterStatus = document.scanAttachForm.ssMasterStatus[i].value;
                        }
                    }
                    if (trim(document.scanForm.ssMasterStatus.value) == "") {
                        alert("Please Select Status");
                        return false;
                    }
                }
                closeAttachList();
                document.scanForm.submit();
                if (scanForm.screenName.value.length > 3 && scanForm.screenName.value.substring(0, 3) === 'LCL') {
                    setLclButtonColorChange(scanForm.screenName.value);
                } else {
                    if (scanForm.screenName.value != "INVOICE" && scanForm.screenName.value != "AR BATCH" && scanForm.screenName.value != "JOURNAL ENTRY") {
                        parent.parent.changeScanButtonColor(masterStatus, selectedDocument,1);
                    }
                }
            }
        }
        function view(index) {
            document.getElementById("attachMessage").innerHTML = "";
            document.getElementById("attachError").innerHTML = "";
            var scanForm = new Object();
            scanForm.fileNumber = document.getElementById('fileNumber').value;
            scanForm.screenName = document.getElementById('screenName').value;
            scanForm.documentType = document.getElementById('docType' + index).value;
            scanForm.documentName = document.getElementById('docName' + index).value;
            DwrUtil.getDocumentStoreLog(scanForm, function(data) {
                showPopUp();
                var docListDiv = createHTMLElement("div", "docListDiv", "98%", "70%", document.body);
                dwr.util.setValue("docListDiv", data, {escapeHtml: false});
                jQuery("#documentListItem").tablesorter({widgets: ['zebra']});
            });
        }
        function viewAll(index) {
            document.getElementById("attachMessage").innerHTML = "";
            document.getElementById("attachError").innerHTML = "";
            var scanForm = new Object();
            scanForm.fileNumber = document.getElementById('fileNumber').value;
            scanForm.screenName = document.getElementById('screenName').value;
            DwrUtil.getDocumentStoreLogForViewAll(scanForm, function(data) {
                showPopUp();
                var docListDiv = createHTMLElement("div", "docListDiv", "98%", "70%", document.body);
                dwr.util.setValue("docListDiv", data, {escapeHtml: false});
                jQuery("#documentListItem").tablesorter({widgets: ['zebra']});
            });
        }

        function attachFiles(index) {
            var masterBl = parent.parent.document.getElementById('newMasterBL') ? parent.parent.document.getElementById('newMasterBL').value : "";
            document.getElementById("attachMessage").innerHTML = "";
            document.getElementById("attachError").innerHTML = "";
            document.getElementById('pageAction').value = "Attach";
            document.getElementById('documentType').value = document.getElementById('docType' + index).value;
            document.getElementById('documentName').value = document.getElementById('docName' + index).value;
            var document_name = document.getElementById('docName' + index).value;
            var screenName = document.getElementById('listScreenName' + index).value;
            var fileNumber = document.getElementById('fileNumber').value;
            if (trim(masterBl) == "" && document_name == "SS LINE MASTER BL") {
                showPopUp();
                document.getElementById("masterBlDiv").style.display = "block";
                floatDiv("masterBlDiv", document.body.offsetWidth / 3, document.body.offsetHeight / 4).floatIt();
            } else {
                DwrUtil.getCommentsForScanOrAttach("Attach", index, document_name, fileNumber, screenName, function(data) {
                    if (data) {
                        showPopUp();
                        var attachListDiv = createHTMLElement("div", "attachListDiv", "60%", "40%", document.body);
                        dwr.util.setValue("attachListDiv", data, {escapeHtml: false});
                        floatDiv("attachListDiv", document.body.offsetWidth / 4, document.body.offsetHeight / 4).floatIt();
                    }
                });
            }
        }
        function copyFile(selectedDocument) {
            var scanForm = new Object();
            var masterStatus = "";
            if (document.scanAttachForm.ssMasterStatus) {
                for (var i = 0; i < document.scanAttachForm.ssMasterStatus.length; i++) {
                    if (document.scanAttachForm.ssMasterStatus[i].checked) {
                        scanForm.ssMasterStatus = document.scanAttachForm.ssMasterStatus[i].value;
                        masterStatus = document.scanAttachForm.ssMasterStatus[i].value;
                    }
                }
            }
            var comments = dwr.util.getValue('comment');
            var file = dwr.util.getValue('theFile');
            scanForm.comments = dwr.util.getValue('comment');
            scanForm.systemRule = dwr.util.getValue('systemRule');
            scanForm.screenName = dwr.util.getValue('screenName');
            scanForm.documentType = dwr.util.getValue('documentType');
            scanForm.documentName = dwr.util.getValue('documentName');
            scanForm.fileNumber = dwr.util.getValue('fileNumber');
            scanForm.fileNo = dwr.util.getValue('fileNo');
            if (null == file || trim(file.value) == '') {
                alert("Please Upload File");
                return false;
            } else if (trim(comments) == "" && (scanForm.screenName != 'INVOICE' && scanForm.screenName != 'AR BATCH' && scanForm.screenName != 'JOURNAL ENTRY')) {
                alert("Please Enter Comments");
                return false;
            } else if (document.scanAttachForm.ssMasterStatus) {
                if (trim(masterStatus) == "") {
                    alert("Please Select Status");
                    return false;
                }
            }
            closeAttachList();
            UploadDownloaderDWR.uploadFiles(file, scanForm, function(result) {
                if (null != result && trim(result) != "Error") {
                    DwrUtil.getScanList(scanForm, function(data) {
                        if (data) {
                            dwr.util.setValue("reportdiv", data, {escapeHtml: false});
                            jQuery("#scanSubList1").tablesorter({widgets: ['zebra'], textExtraction: ['complex']});
                            jQuery("#scanSubList2").tablesorter({widgets: ['zebra'], textExtraction: ['complex']});
                            dwr.util.setValue("attachMessage", result + " is successfully attached");
                            if (scanForm.screenName.length > 3 && scanForm.screenName.substring(0, 3) === 'LCL') {
                                setLclButtonColorChange(scanForm.screenName);
                            } else if (scanForm.screenName != "INVOICE" && scanForm.screenName != "AR BATCH" && scanForm.screenName != "JOURNAL ENTRY" && scanForm.screenName != "ARCONFIG") {
                                parent.parent.changeScanButtonColor(masterStatus, selectedDocument,1);
                            } else if (parent.parent.document.getElementById("scanAttach")) {
                                parent.parent.document.getElementById("scanAttach").style.backgroundColor = "lightGreen";
                            }
                            if(parent.parent.document.getElementById("arScan")){
                                parent.parent.document.getElementById("arScan").className = "buttonColor";
                            }
                        }
                    });
                } else {
                    dwr.util.setValue("attachError", "File not attached. Please try again....");
                }
            });
        }
        function setLclButtonColorChange(screenName,documentCount) {
            var cssClassName=documentCount!=0 ? "green-background":"button-style1";
            if (screenName === "LCL IMPORTS UNIT") {
                parent.parent.document.getElementById("scan-attach").className =cssClassName;
            }else{
                parent.parent.document.getElementById("scanAttach").className =cssClassName;
                parent.parent.document.getElementById("scan-Attach").className = cssClassName;
            }
        }

        function closeAttachList() {
            document.body.removeChild(document.getElementById("attachListDiv"));
            closePopUp();
        }

        function closeDocList() {
            var scanForm = new Object();
            scanForm.screenName = dwr.util.getValue('screenName');
            scanForm.fileNumber = dwr.util.getValue('fileNumber');
            DwrUtil.getScanList(scanForm, function(data) {
                if (data) {
                    dwr.util.setValue("reportdiv", data, {escapeHtml: false});
                }
            });
            document.body.removeChild(document.getElementById("docListDiv"));
            closePopUp();
        }
         
        function edit(index) {
            addDocument("update");
        <%--dwr.util.setValue("addDocumentType",dwr.util.getValue('docType'+index));--%>
                dwr.util.setValue("addDocumentName", dwr.util.getValue('docName' + index));
                dwr.util.setValue("documentId", dwr.util.getValue('docId' + index));
                dwr.util.setValue("addScreenName", dwr.util.getValue('listScreenName' + index));
            }
            function deleteScan(index) {
                dwr.util.setValue("documentId", dwr.util.getValue('docId' + index));
                submitDocument('delete');
            }

            function addDocument(submitType) {
                showPopUp();
                document.getElementById("addDocumentListDiv").style.display = 'block';
                if (submitType == 'add') {
                    document.getElementById("submitButton").style.display = 'block';
                    document.getElementById("updateButton").style.display = 'none';
        <%--dwr.util.setValue("addDocumentType","");--%>
                    dwr.util.setValue("addDocumentName", "");
                    dwr.util.setValue("addScreenName", dwr.util.getValue("screenName"));
                    var addScreenName = document.getElementById("addScreenName");
                    if (dwr.util.getValue('fileNumber') == "") {
                        addScreenName.disabled = false;
                        addScreenName.focus();
                    }
                } else if (submitType == 'update') {
                    document.getElementById("submitButton").style.display = 'none';
                    document.getElementById("updateButton").style.display = 'block';
                    document.getElementById("addScreenName").disabled = true;
                }
            }
            function submitDocument(pageAction) {
                if (pageAction != 'delete' && dwr.util.getValue("addScreenName") == "") {
                    alert("Please select Screen Name");
                    return false;
                } else if (pageAction != 'delete' && dwr.util.getValue("addDocumentName") == "") {
                    alert("Please enter Document Name");
                    return false;
                } else {
                    var scanForm = new Object();
                    if (pageAction == 'add') {
                        scanForm.screenName = dwr.util.getValue('addScreenName');
                    } else {
                        scanForm.screenName = dwr.util.getValue('screenNameFilter');
                    }
        <%--scanForm.documentType = dwr.util.getValue('addDocumentType');--%>
                    scanForm.documentName = dwr.util.getValue('addDocumentName');
                    scanForm.id = dwr.util.getValue('documentId');
                    scanForm.pageAction = pageAction;
                    scanForm.fileNumber = dwr.util.getValue('fileNumber');
                    if (null != pageAction && pageAction == 'delete') {
                        if (confirm("Are you sure Do you want to delete the Document")) {
                            DwrUtil.deleteScanConfig(scanForm, function(data) {
                                if (null != data && data == "Deleted Successfully") {
                                    closeDocumentDiv();
                                    if (scanForm.screenName != null) {
                                        DwrUtil.getScanList(scanForm, function(newScanList) {
                                            if (null != newScanList && newScanList != "") {
                                                dwr.util.setValue("reportdiv", newScanList, {escapeHtml: false});
                                                jQuery("#scanSubList1").tablesorter({widgets: ['zebra'], textExtraction: ['complex']});
                                                jQuery("#scanSubList2").tablesorter({widgets: ['zebra'], textExtraction: ['complex']});
        <%--dwr.util.setValue("addDocumentType","");--%>
                                                dwr.util.setValue("addDocumentName", "");
                                            } else {
                                                dwr.util.setValue("reportdiv", null, {escapeHtml: false});
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else {
                        DwrUtil.addScanConfig(scanForm, function(data) {
                            if (null != data && data == "Added Successfully") {
                                closeDocumentDiv();
                                if (pageAction == 'add') {
                                    scanForm.screenName = "ALL";
                                    dwr.util.setValue("screenNameFilter", "ALL");
                                }
                                if (scanForm.screenName != null) {
                                    DwrUtil.getScanList(scanForm, function(newScanList) {
                                        if (null != newScanList && newScanList != "") {
                                            dwr.util.setValue("reportdiv", newScanList, {escapeHtml: false});
                                            jQuery("#scanSubList1").tablesorter({widgets: ['zebra'], textExtraction: ['complex']});
                                            jQuery("#scanSubList2").tablesorter({widgets: ['zebra'], textExtraction: ['complex']});
        <%--dwr.util.setValue("addDocumentType","");--%>
                                            dwr.util.setValue("addDocumentName", "");
                                        }
                                    });
                                }
                            } else if (null != data) {
                                alert(data);
                                showPopUp();
                            }
                        });
                    }
                }
            }
            function closeDocumentDiv() {
                document.getElementById("addDocumentListDiv").style.display = 'none';
                closePopUp();
            }
            function validateDocumentName(obj) {
                obj.value = obj.value.replace(/\\/g, "");
            }
            function viewFile(fileName) {
                if (fileName.indexOf(".xls") > 0 || fileName.indexOf(".doc") > 0
                    || fileName.indexOf(".mht") > 0 || fileName.indexOf(".msg") > 0
                    || fileName.indexOf(".csv") > 0 || fileName.indexOf(".ppt") > 0) {
                    window.open("${path}/servlet/FileViewerServlet?fileName=" + fileName, "", "resizable=1,location=1,status=1,scrollbars=1, width=600,height=400");
                } else {
                    window.parent.parent.parent.parent.showGreyBox("", "${path}/servlet/FileViewerServlet?fileName=" + fileName);
                }
            }
            function getByScreenName(obj) {
                document.scanForm.screenName.value = obj;
                document.scanForm.submit();
            }
            function validateMaxLength(field, maxChars) {
                if (field.value.trim().length > maxChars) {
                    alertNew("More Than " + maxChars + " Characters Should Not Be Allowed");
                    var res = field.value;
                    field.value = res.trim().slice().substr(0, maxChars);
                    return;
                }
            }
            function deleteAttachment(ele,id,fileName){
        if(confirm("Are you sure want to delete this document - "+fileName+"?")){
            jQuery.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "deleteAttachedDocuments",
                    dataType: "json",
                    param1: id
                },
                success: function(data) {
                    jQuery(".message").html(data[0]);
                    jQuery(ele).parents("tr").remove();
                    if (scanForm.screenName.value.length > 3 && scanForm.screenName.value.substring(0, 3) === 'LCL') {
                        setLclButtonColorChange(scanForm.screenName.value,data[2]);
                    } else if(scanForm.screenName.value != "ARCONFIG" && scanForm.screenName.value != "INVOICE" && scanForm.screenName.value != "AR BATCH" && scanForm.screenName.value != "JOURNAL ENTRY"){
                        parent.parent.changeScanButtonColor(data[1],data[3],data[2]);
                    }
                }
            });
        }
    }
                
            function saveMasterBl() {
                if (trim(document.getElementById("newMasterBL").value) == "") {
                    alert("Please  Enter MASTER BL # to Attach or Scan");
                } else {
                    parent.parent.document.getElementById("newMasterBL").value = document.getElementById("newMasterBL").value;
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cvst.logisoft.hibernate.dao.FclBlDAO",
                            methodName: "saveMasterBlNo",
                            param1: parent.parent.document.getElementById("bol").value,
                            param2: document.getElementById("newMasterBL").value
                        }
                    });
                    closeMasterBl();
                }
            }
            function closeMasterBl() {
                document.getElementById("masterBlDiv").style.display = "none";
                document.getElementById("newMasterBL").value = "";
                closePopUp();
            }
            AjaxAutocompleter("newMasterBL", "newMasterBL_choices", "", "", "${path}/actions/BookingNo.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false", "");
            useLogisoftLodingMessageNew();
    </script>
    <script type="text/javascript" src="${path}/js/tablesorter/jquery-latest.js"></script>
    <script type="text/javascript" src="${path}/js/tablesorter/jquery.tablesorter.js"></script>
    <script type="text/javascript" src="${path}/js/tablesorter/jquery.tablesorter.pager.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    <script type="text/javascript">
        jQuery.noConflict();
        jQuery(document).ready(function() {
            jQuery("#scanSubList1").tablesorter({widgets: ['zebra'], textExtraction: ['complex']});
            jQuery("#scanSubList2").tablesorter({widgets: ['zebra'], textExtraction: ['complex']});
        });
    </script>
</html>

