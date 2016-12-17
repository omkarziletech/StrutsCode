<%@page import="com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>

<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://cong.logiwareinc.com/dao" prefix="dao"%>
<%@include file="../includes/jspVariables.jsp"%>
<%    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String faxTemplatePath = path + "/jsps/print/FaxTemplate.jsp";
    String emailTemplatePath = path + "/jsps/fclQuotes/enterEmailId.jsp";
    int docCount = 1;
    String fileNo = null;
    String fileId = null;
    String moduleId = "";
    String invoice = "";
    String bolNo = "";
    Object brand = null;
    String lclImpBrand = "";
    Map<String, String> requestMap = (Map<String, String>) session.getAttribute("requestMap");
    fileNo = requestMap.get("fileNo");
    fileId = requestMap.get("fileId");
    invoice = requestMap.get("arInvoice");
    fileNo = null != fileNo ? fileNo : "";
    String screenName = (String) request.getAttribute("screenName");
    String spEmailId = (String) request.getAttribute("spEmailId");
    if (null != invoice) {
        moduleId = requestMap.get("arInvoice");
        fileNo = requestMap.get("arInvoice");
    } else {
        moduleId = null != fileNo ? fileNo.substring(-1 != fileNo.indexOf("-") ? fileNo.indexOf("-") + 1 : 0) : "";
    }
    if (null != request.getAttribute("bolNo")) {
        bolNo = (String) request.getAttribute("bolNo");
    }
%>
<html>
    <head>
        <title>Print</title>
        <%@include file="../includes/baseResources.jsp"%>
        <%@include file="../includes/baseResourcesForJS.jsp"%>
        <c:set var="contextPath" value="${pageContext.request.contextPath}" />
        <c:set var="path" value="${pageContext.request.contextPath}" />
        <%--  <%@include file="../includes/resources.jsp"%>--%>
        <script type="text/javascript">
            var GB_ROOT_DIR = "${pageContext.request.contextPath}/js/greybox/";
        </script>
        <script type="text/javascript" src="${contextPath}/js/greybox/AJS.js"></script>
        <script type="text/javascript" src="${contextPath}/js/greybox/AJS_fx.js"></script>
        <script type="text/javascript" src="${contextPath}/js/greybox/gb_scripts.js"></script>
        <link href="${contextPath}/js/greybox/gb_styles.css" rel="stylesheet" type="text/css" media="all" />
        <script type="text/javascript" src="${contextPath}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${contextPath}/js/common.js"></script>
        <script language="JavaScript" type="text/javascript" src="${contextPath}/js/cbrte/html2xhtml.js"></script>
        <script language="JavaScript" type="text/javascript" src="${contextPath}/js/cbrte/richtext_compressed.js"></script>
        <link type="text/css" rel="stylesheet" href="${contextPath}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${contextPath}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${contextPath}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${contextPath}/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="${contextPath}/js/jquery/jquery.tooltip.new.js"></script>
        <script type="text/javascript" src="${contextPath}/js/printDocument.js"></script>
        <c:set var ="concatedUserName" value="${user.firstName} ${user.lastName}"/>
        <link rel="stylesheet" type="text/css" media="all"
              href="${contextPath}/css/cal/skins/aqua/theme.css" title="Aqua"/>
    </head>
    <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
        <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
        <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
            <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
        </form>
    </div>
    <body class="whitebackgrnd">
        <%@include file="../preloader.jsp"%>
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display = 'none';
                grayOut(false, '');">
            </form>
        </div>
        <div id="cover" style="width: 906px ;height: 1000px;"></div>
        <html:form enctype="multipart/form-data" name="printConfigForm" type="com.gp.cong.logisoft.struts.form.PrintConfigForm"
                   action="/printConfig" styleId="printForm" scope="request">
            <html:hidden property="postedLclBl" styleId="postedLclBl" value="${printConfigForm.postedLclBl}"/>
            <html:hidden property="exportFileCob" styleId="exportFileCob" value="${printConfigForm.exportFileCob}"/>
            <html:hidden property="blCorrectedId" styleId="blCorrectedId"/>

            <div id="customerContactDiv" style="display:none;width:900px;height:250px; azimuth:left-side;overflow-x:  hidden;">
                <%@include file="../print/customerList.jsp"%>
            </div>
            <div id="displayMessage" style="color: green;font-weight: bold;font-size: 14px;"></div>
            <div id="printConfig" style="display: block">
                <div>
                    <table align="center" width="100%" border="0" cellpadding="0"
                           cellspacing="0">
                        <tr>
                            <td align="center">
                                <font color="blue" size="3">
                                <b>
                                    <c:if test="${null!=displayMessage && displayMessage!=''}">
                                        <span id="displayMessage">
                                            <c:out value="${displayMessage}"></c:out>
                                            </span>
                                    </c:if>
                                </b>
                                </font>
                                <font color="red" size="3">
                                <b>
                                    <c:if test="${null!=errorMessage && errorMessage!=''}">
                                        <span id="errorMessage">
                                            <c:out value="${errorMessage}"></c:out>
                                            </span>
                                    </c:if>
                                </b>
                                </font>
                            </td>
                        </tr>
                    </table>
                    <table align="center" id="mainTable" width="100%" border="0"
                           cellpadding="0" cellspacing="0" class="tableBorderNew">
                        <tr>
                            <td>
                                <table width="100%" cellpadding="0" cellspacing="0">
                                    <tr class="tableHeadingNew">
                                        <td>
                                            Document List for FileNo ${printForm.screenName eq 'LCLIMPBooking' ? 'IMP-' : ''} <%=fileNo%>
                                        </td>
                                        <td align="right">
                                            <input type="button" class="buttonStyleNew" value="Email Cover Page" id="emailFormId" onclick="openEmailForm()"/>
                                            <input type="button" class="buttonStyleNew" value="Fax Cover Page" id="faxFormId" onclick="openFaxForm()"/>
                                            <input type="button" class="buttonStyleNew" id="status" value="Status" style="width:70px" onclick="openPrintStatusPopUp()"/>
                                            <c:if test="${customerContactList!=null }">
                                                <input type="button" id="contactButton" style="visibility: hidden" class="buttonStyleNew" value="Contacts" onclick="showContactsPopup();"/>
                                            </c:if>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="divtablesty1"
                                     style="height: 100%; overflow: auto; width: 100%"
                                     class="scrolldisplaytable">
                                    <table border="0" cellpadding="0" cellspacing="0">
                                        <display:table name="${printList}" defaultsort="1"
                                                       class="displaytagstyleNew"
                                                       id="printList" style="width:100%" sort="external">
                                            <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner"> <font color="blue">{0}</font>
                                                    Print Details Displayed,For more Scan Details click on Page
                                                    Numbers. <br> </span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.one_item_found">
                                                <span class="pagebanner"> One {0} displayed. Page
                                                    Number </span>
                                                </display:setProperty>
                                                <display:setProperty name="paging.banner.all_items_found">
                                                <span class="pagebanner"> {0} {1} Displayed, Page
                                                    Number </span>
                                                </display:setProperty>
                                                <display:setProperty name="basic.msg.empty_list">
                                                <span class="pagebanner"> No Records Found. </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.placement"
                                                                 value="bottom"/>
                                            <display:setProperty name="paging.banner.item_name"
                                                                 value="document"/>
                                            <display:setProperty name="paging.banner.items_name"
                                                                 value="documents"/>
                                            <display:column title="">
                                                <c:set var="documentBillTo" value=""/>
                                                <input type="hidden" id="documentName" name="documentName" value="${printList.documentName}"/>
                                                <c:out value="${printList.documentName}"></c:out>
                                                <c:if test="${screenName eq 'LCLBL'}">
                                                    <c:if test="${printList.documentName eq 'LCL Freight Invoice' 
                                                                  || printList.documentName eq 'Freight Invoice'}">
                                                          <span>(${printList.exportBillToParty})</span>
                                                          <c:set var="documentBillTo" value="#${printList.exportBillToParty}"/>
                                                    </c:if>
                                                    <c:if test="${printList.documentName eq 'Bill Of Lading'}">
                                                        <span>(${printConfigForm.postedLclBl eq 'postBl' ? "Non-Negotiable":"Edit"})</span>
                                                    </c:if>
                                                </c:if>
                                            </display:column>
                                            <display:column title="<br/>Print">
                                                <c:choose>
                                                    <c:when test="${printList.enableDisablePrint == 'Yes'}">
                                                        <input type="checkbox" id="print<%=docCount%>"
                                                               name="print<%=docCount%>" value="print" onclick="changeChecked(this,<%=docCount%>, '${printList.documentName}')"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" id="print<%=docCount%>"
                                                               name="print<%=docCount%>" value="print" disabled="true" onclick="changeChecked(this,<%=docCount%>, '${printList.documentName}')"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <input type="text" maxlength="2" size="1" id="printCopy<%=docCount%>"
                                                       style="text-align: center;"
                                                       value="${!empty printList.printCopy ? printList.printCopy :'1'}"
                                                       onchange="validateLabelPrint(this, '${printList.screenName}', '${printList.documentName}')" Class="textlabelsBoldForTextBox"/>
                                                Copies
                                            </display:column>
                                            <display:column title="<br/>Email" >
                                                <c:choose>
                                                    <c:when test="${printList.documentName ne 'Voyage Notification'
                                                                    && printList.documentName ne 'Label Print' && printList.documentName ne 'Barrel D/R'}">
                                                            <input type="checkbox" id="email<%=docCount%>" name="email<%=docCount%>" value="email"
                                                                   onclick="changeChecked(this,<%=docCount%>, '${printList.documentName}')"/>
                                                    </c:when>
                                                    <c:when test="${printList.documentName eq 'Voyage Notification'}">
                                                        <input type="checkbox" id="email${printList_rowNum}" name="email${printList_rowNum}" value="email"
                                                               class="voyageEmail" onclick="openPopup(this, '${printList_rowNum}');"/>
                                                        <input type="hidden" id="voyageIndex" value="${printList_rowNum}"/>
                                                    </c:when>
                                                </c:choose>
                                            </display:column>
                                            <display:column title="<br/>Fax">
                                                <c:if test="${printList.documentName ne 'Label Print' && printList.documentName ne 'Voyage Notification'
                                                              && printList.documentName ne 'Barrel D/R'}">
                                                    <c:choose><c:when test="${printList.documentName eq 'Release Order'}">
                                                            <input type="checkbox" id="fax<%=docCount%>" name="fax" value="fax"
                                                                   onclick="changeChecked(this,<%=docCount%>, '${printList.documentName}')"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="checkbox" id="fax<%=docCount%>" name="fax" value="fax" disabled="true"
                                                                   onclick="changeChecked(this,<%=docCount%>, '${printList.documentName}')"/>
                                                        </c:otherwise></c:choose></c:if>
                                            </display:column>
                                            <display:column title="<br/>Email Me">
                                                <c:if test="${printList.documentName ne 'Label Print' && printList.documentName ne 'Voyage Notification'
                                                              && printList.documentName ne 'Barrel D/R'}">
                                                      <input type="checkbox" id="emailMe<%=docCount%>"
                                                             name="emailMe<%=docCount%>" value="emailMe"
                                                             onclick="changeChecked(this,<%=docCount%>, '${printList.documentName}')"/>
                                                </c:if>
                                            </display:column>
                                            <c:if test="${screenName eq 'LCLIMPBooking'}">
                                                <display:column title="<br/>Email All">
                                                    <c:if test="${printList.documentName ne 'Label Print'}">
                                                        <input type="checkbox" id="emailAll<%=docCount%>"
                                                               name="emailAll<%=docCount%>" value="emailAll"
                                                               onclick="changeChecked(this, <%=docCount%>, '${printList.documentName}')"/>
                                                        <span title="<font size='2' color=#008000><b>Email Details</b></font></br> ${codeFContactNameAndEmail} </br>">
                                                            <img id="report<%=docCount%>" src="<%=path%>/img/icons/iicon.png" width="16" height="16" border="0"
                                                                 /> </span></c:if>
                                                    </display:column>
                                                </c:if>
                                                <c:if test="${fn:contains(printList.documentName, 'Quotation')}">
                                                    <display:column title="<br/>Email SP" headerClass="sortable">
                                                        <c:choose>
                                                            <c:when test="${not empty salesPerson.field3}">
                                                            <input type="checkbox" id="emailSp<%=docCount%>"
                                                                   name="emailSp" value="emailSp"
                                                                   onclick="changeChecked(this,<%=docCount%>, '${printList.documentName}')"/>
                                                            <span class="label">${salesPerson.code} - ${salesPerson.codedesc}</span>
                                                        </c:when>
                                                        <c:when test="${emailSp}">
                                                            <input type="checkbox" id="emailSp<%=docCount%>"
                                                                   name="emailSp" value="emailSp"
                                                                   onclick="changeChecked(this,<%=docCount%>, '${printList.documentName}')"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="checkbox" id="emailSp<%=docCount%>"
                                                                   name="emailSp" value="emailSp" disabled="true"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </display:column>
                                            </c:if>
                                            <display:column title="<br/>Preview">
                                                <c:if test="${printList.documentName ne 'Voyage Notification' && printList.documentName ne 'Bill of Lading (Original)' && printList.documentName ne 'Bill of Lading (Original UNSIGNED)'
                                                              && printList.documentName ne 'Unrated Bill Of Lading (Original)' && printList.documentName ne 'Unrated Bill of Lading (Original UNSIGNED)'
                                                              && printList.documentName ne 'Label Print' && printList.documentName ne 'Barrel D/R'}">
                                                    <c:choose>
                                                        <c:when test="${'LclCreditDebitNote' eq printList.screenName}">
                                                            <span class="hotspot" title="Preview">
                                                                <img id="report<%=docCount%>" src="${path}/img/icons/search_over.gif" border="0"
                                                                     onClick="previewReport('${printList.screenName}', '${printList.documentName}#${printList.id}',<%=docCount%>)"/> </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                            <span class="hotspot" title="Preview">
                                                                <img id="report<%=docCount%>" src="${path}/img/icons/search_over.gif" border="0"
                                                                     onClick="previewReport('${printList.screenName}', '${printList.documentName}${documentBillTo}',<%=docCount%>)"/> </span>
                                                            </c:otherwise>
                                                        </c:choose> 
                                                    </c:if>
                                                    <c:if test="${printList.documentName eq 'Freight Invoice' and printList.screenName eq 'LCLIMPBooking'}"> 
                                                    <span class="hotspot" title="xlsx Format">
                                                        <img id="report<%=docCount%>" src="${path}/images/icons/excel.png" border="0"
                                                             onClick="previewReport('${printList.screenName}', 'FreightInvoiceWorksheet', <%=docCount%>)"/>
                                                    </span>
                                                </c:if>  
                                            </display:column>
                                            <display:column style="display: none;">
                                                <input type="hidden" id="fileLocation<%=docCount%>" value=""/>
                                                <input type="hidden" id="previewLocation<%=docCount%>" class="previewLocation" value=""/>
                                                <input type="hidden" id="fileNumber<%=docCount%>" value="${printList.id}"/>
                                                <input type="hidden" id="screenName<%=docCount%>" value="${printList.screenName}"/>
                                                <input type="hidden" id="documentName<%=docCount%>" value="${printList.documentName}${documentBillTo}"/>
                                                <input type="hidden" id="printerName<%=docCount%>" value="${printList.printerName}"/>
                                                <input type="text" id="printerTray<%=docCount%>" value="${printList.printerTray}"/>
                                                <input type="hidden" id="printCopy<%=docCount%>" value="${printList.printCopy}"/>

                                            </display:column>
                                            <%
                                                docCount++;
                                            %>
                                        </display:table>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table width="100%" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td align="center">
                                            <input type="button" id="submitButton" value="Submit"
                                                   onclick="submitPrint()" class="buttonStyleNew" >
                                            <input type="button" value="Cancel" onclick="cancel()"
                                                   class="buttonStyleNew"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <html:hidden property="systemRule" value="${printForm.systemRule}"/>
                    <html:hidden property="screenName" value="${printForm.screenName}" styleId="screenNameForRef"/>
                    <html:hidden property="fileNumber" value="${printForm.fileNumber}"/>
                    <input type="hidden" id="fileNo" value="<%=fileNo%>"/>
                    <input type="hidden" id="fileId" value="<%=fileId%>"/>
                    <input type="hidden" id="unitNo" value="${unitNo}"/>
                    <input type="hidden" name="userName" id="userName" value="${user.loginName}"/>
                    <input type="hidden" name="spEmailId" id="spEmailId" value="<%=spEmailId%>"/>
                </div>
            </div>

            <%--  This is for Fax Template --%>
            <%@include file="/jsps/print/faxTemplate.jsp" %>
            <%--  This is for EMail Template --%>
            <%@include file="/jsps/print/emailTemplate.jsp" %>
            <%--  This is for FCL --%>
            <%@include file="/jsps/print/fclCorrectionList.jsp" %>
            <%--  This is for LCL Exports --%>
            <%@include file="/jsps/print/lclCorrectionList.jsp" %>

            <div id="commentsPopup" style="display: none">
                Customer Email
                <div class="divstyleThin">
                    <c:forEach var="emailE1F1" items="${emailE1F1Map}" >
                        <table>
                            <tr class="textlabelsBold"><td>
                                    <input type="checkbox" class="emailValues" checked="yes" value="${emailE1F1.value}">${emailE1F1.value}
                                </td>
                                <td style="font-size: 10px;color: green">${emailE1F1.key}<br/></td>
                            </tr></table>
                        </c:forEach>
                </div>
                <label class="label">Enter voyage notification remarks</label>
                <br/><br/>
                <html:textarea property="comments" styleId="comments" styleClass="textarea" cols="170" rows="10"/>
                <br/><br/>
                <input type="button" class="button" value="Cancel" onclick="doCancel();"/>
                <input type="button" class="button" value="Submit and Generate PDF" onclick="generatePDFReport();"/>
            </div>
            <input type="hidden" id="faxIndex" name="faxIndex">
            <input type="hidden" id="emailIndex" name="emailIndex">
            <input type="hidden" id="comment" name="comment" value="${printForm.comment}">
            <input type="hidden" id="femailSubject" name="femailSubject" value="${printForm.emailSubject}">
            <input type="hidden" id="fsubject" name="fsubject" value="${printForm.subject}">
            <input type="hidden" id="coloaderEmail1" name="coloaderEmail1" value="${coloaderEmail1}">
            <input type="hidden" id="cFSDevWarhseManagerEmail" name="cFSDevWarhseManagerEmail" value="${cFSDevWarhseManagerEmail}">
            <input type="hidden" id="houseConsigneeEmail1" name="houseConsigneeEmail1" value="${houseConsigneeEmail1}">
            <input type="hidden" id="notifyPartyEmail1" name="notifyPartyEmail1" value="${notifyPartyEmail1}">
            <input type="hidden" id="notify2PartyEmail1" name="notify2PartyEmail1" value="${notify2PartyEmail1}">
            <input type="hidden" id="defaultEmailAddress" name="defaultEmailAddress">
            <input type="hidden" id="coloaderFax" name="coloaderFax" value="${coloaderFax}">
            <input type="hidden" id="houseConsigneeFax" name="houseConsigneeFax" value="${houseConsigneeFax}">
            <input type="hidden" id="notifyPartyFax" name="notifyPartyFax" value="${notifyPartyFax}">
            <input type="hidden" id="notify2PartyFax" name="notify2PartyFax" value="${notify2PartyFax}">
            <input type="hidden" id="filesToPrint" value="${param.filesToPrint}"/>
            <input type="hidden" id="docdeptemailto" value="${billingTerminal}"/>
            <input type="hidden" id="getdocemail" value="${getdocemail}"/>
            <input type="hidden" id="brand" name="brand" value="${printForm.brand}"/>
            <%-- <input type="hidden"  id="myEmail" value="${myEmail}">
            <input type="hidden"  id="myEmail1" value="${myEmail1}"> 

            <input type="hidden" id="getcustServiceemail" value="${custseremail}"/>
            <input type="hidden" id="phonefromTerminal" value="${phonefromTerminal}"/>
            <input type="hidden" id="faxfromTerminal" value="${faxfromTerminal}"/>
            <input type="hidden" id="docnamefromTerminal" value="${docnamefromTerminal}"/>
            <input type="hidden" id="customerNamefromTerminal" value="${customerNamefromTerminal}"/>--%>

            <input type="hidden" id="bolid1" value="${bolId}"/>
            <input type="hidden" id="emailCheckedDoc"/>

            <input type="hidden"  id="userFirstName" name="userFirstName" value="${concatedUserName}">
            <input type="hidden"  id="loginUserEmailId" name="loginUserEmail" value="${loginUserEmail}">
            <input type="hidden" id="docTerminalEmail" name="docTerminalEmail" value="${docTerminalEmail}"/>
            <input type="hidden" id="docTerminalName" name="docTerminalName" value="${docTerminalName}"/>
            <input type="hidden" id="terminalCustomerEmail" name="terminalCustomerEmail" value="${terminalCustomerEmail}"/>
            <input type="hidden" id="terminalCustomerName" name="terminalCustomerName" value="${terminalCustomerName}"/>
            <input type="hidden" id="phoneTerminalNo" name="phoneTerminalNo" value="${phoneTerminalNo}"/>
            <input type="hidden" id="faxTerminalNo" name="faxTerminalNo" value="${faxTerminalNo}"/>
            <input type="hidden" id="userTerminalPhoneNo" name="userTerminalPhoneNo" value="${userTerminalPhoneNo}"/>
            <input type="hidden" id="userTerminalFaxNo" name="userTerminalFaxNo" value="${userTerminalFaxNo}"/>
            <input type="hidden" id="tempPhoneNo" name="tempPhoneNo"/>
            <input type="hidden" id="tempFaxNo" name="tempFaxNo"/>


            <input type="hidden" id="nameFromTerminal" name="nameFromTerminal"/>



            <input type="hidden" id="importflagForExport" name="importflagForExport" value="${importflag}"/>
            <input type="hidden" id="moduleForExport" name="moduleForExport" value="${module}"/>
            <input type="hidden" id="screenNameForExport" name="screenNameForExport" value="${screenName}"/>
            <input type="hidden" id="isEmailOrFax" name="isEmailOrFax" value=""/>
            <html:hidden property="pageAction"/>
        </html:form>
    </body>
    <script language="javascript">
        start = function () {
            // makeEmailStatusButtonGreen();
        };
        window.onload = start;
    </script>
    <script type="text/javascript">
        $(document).ready(function () {
            var docTerminalEmail = jQuery("#docTerminalEmail").val() !== null ? jQuery("#docTerminalEmail").val() : "";
            var custTerminalEmail = jQuery("#terminalCustomerEmail").val() !== null ? jQuery("#terminalCustomerEmail").val() : "";
            //var docdeptemailto = jQuery("#getdocemail").val() !== null ? jQuery("#getdocemail").val() : "";
            var loginUserEmail = jQuery("#loginUserEmailId").val() !== null ? jQuery("#loginUserEmailId").val() : "";
            //  var getcustServiceemail = jQuery("#getcustServiceemail").val() !== null ? jQuery("#getcustServiceemail").val() : "";

            if (docTerminalEmail == '' && docTerminalEmail !== undefined) {
                jQuery("#pullEmailDocs").attr('disabled', true);
            }
            if (custTerminalEmail == '' && custTerminalEmail !== undefined) {
                jQuery("#customerserviceemail").attr('disabled', true);
            }
            if (loginUserEmail !== '') {
                jQuery("#fromEmailAddress").val(loginUserEmail);
                jQuery("#nameFromTerminal").val(jQuery("#userFirstName").val());
            }

            var contactName = (parent.parent.document.getElementById('contactName') != null && parent.parent.document.getElementById('contactName').value !== null) ?
                    parent.parent.document.getElementById('contactName').value : "";
            var contactFax = (parent.parent.document.getElementById('fax') != null && parent.parent.document.getElementById('fax').value !== null)
                    ? parent.parent.document.getElementById('fax').value : "";
            if (contactName !== '' && contactFax !== '') {
                var faxCheckBox = document.getElementsByName("fax");
                if (null !== faxCheckBox && faxCheckBox.length > 0) {
                    for (i = 0; i < faxCheckBox.length; i++) {
                        faxCheckBox[i].disabled = false;
                    }
                }
            }
            if (document.getElementById("pullEmailUser").checked) {
                jQuery("#tempPhoneNo").val(jQuery("#phoneTerminalNo").val());
                jQuery("#tempFaxNo").val(jQuery("#faxTerminalNo").val());
                jQuery("#phoneTerminalNo").val(jQuery("#userTerminalPhoneNo").val());
                jQuery("#faxTerminalNo").val(jQuery("#userTerminalFaxNo").val());
            }
        });
        function makeEmailStatusButtonGreen(displayCorrection) {
            var fileNumber = "";
            var screenName = jQuery("#screenNameForRef").val();
            if (screenName === "LCLImpUnits" || screenName === "LCLQuotation") {
                fileNumber = jQuery('#fileNo').val();
            } else {
                fileNumber = '<%=moduleId%>';
            }
            if (null !== fileNumber && fileNumber !== '') {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.dwr.PrintUtil",
                        methodName: "getEmailStatus",
                        param1: screenName,
                        param2: fileNumber,
                        dataType: "json"
                    },
                    success: function (data) {
                        if (data) {
                            document.getElementById("status").className = "buttonColor";
                        }
                    }
                });
            }
            if (document.getElementById('contactButton')) {
                document.getElementById('contactButton').style.visibility = 'visible';
            }
            if (displayCorrection === 'Correction') {
                checkDefault();
            }
        }

        function  selectContactsFromPopup() {
            var email = '';
            var fax = '';
            var toAddress = '';
            var customerContacts = document.getElementsByName("customerContact");
            if (customerContacts) {
                for (var i = 0; i < customerContacts.length; i++) {
                    if (customerContacts[i].checked) {
                        var emailFax = customerContacts[i].value;
                        var emailFaxArray = emailFax.split("==");
                        if (emailFaxArray[0].trim() != '') {
                            email += emailFaxArray[0].trim() + ",";
                        }
                        if (emailFaxArray[1].trim() != '') {
                            fax += emailFaxArray[1].trim() + ",";
                        }
                    }
                }
            }
            document.printConfigForm.toAddress.value = email;
            document.getElementById('toFaxNumber').value = fax;
            if (trim(fax) != '') {
                document.getElementById("faxFormId").className = "buttonColor";
            }
            if (trim(email) != '') {
                document.getElementById("emailFormId").className = "buttonColor";
            }
            closePopUp();
            document.getElementById('customerContactDiv').style.display = 'none';
        }
        function closeContactsPopup() {
            closePopUp();
            document.getElementById('customerContactDiv').style.display = 'none';
        }

        function showContactsPopup() {
            if (document.printConfigForm.customerContact != undefined && document.printConfigForm.customerContact.length != undefined) {
                for (var i = 0; i < document.printConfigForm.customerContact.length; i++) {
                    document.printConfigForm.customerContact[i].checked = false;
                }
            } else if (document.printConfigForm.customerContact != undefined && document.printConfigForm.customerContact.length == undefined) {
                document.printConfigForm.customerContact.checked = false;
            } else {
                jQuery.prompt("No contacts to display", {
                    callback: function () {
                        return false;
                    }
                });
            }
            showPopUp();
            var screenName = '<%=screenName%>';
            if (document.getElementById("customerContactDiv")) {
                floatDiv("customerContactDiv", 50, document.body.offsetHeight / 10).floatIt();
            }
            if (screenName === "LCLBooking" || screenName === "LCLQuotation") {
                jQuery('.showContacts').val("Show CodeJ Contacts");
                //    document.getElementById('showAll').value = 'Show CodeJ Contacts';
                //  document.getElementById('showAll1').value = 'Show CodeJ Contacts';
                document.getElementById('customerCodeJContact').style.display = 'none';
                document.getElementById('customerAllContact').style.display = 'block';
                document.getElementById('customerContactDiv').style.display = 'block';
            } else if (screenName === "LCLIMPBooking") {
                jQuery('.showContacts').val("Show All Contacts");
                //                document.getElementById('showAll').value = 'Show All Contacts';
                //                document.getElementById('showAll1').value = 'Show All Contacts';
                document.getElementById('customerCodeFContact').style.display = 'block';
                document.getElementById('customerAllContact').style.display = 'none';
                document.getElementById('customerContactDiv').style.display = 'block';
            } else {
                jQuery('.questIcon').hide();//this icon only for Imports
                jQuery('.showContacts').val("Show CodeC Contacts");
                //                document.getElementById('showAll').value = 'Show CodeC Contacts';
                //                document.getElementById('showAll1').value = 'Show CodeC Contacts';
                document.getElementById('customerCodeCContact').style.display = 'block';
                document.getElementById('customerAllContact').style.display = 'none';
                document.getElementById('customerContactDiv').style.display = 'block';
            }
        }
        function showAllContact(obj) {
            var screenName = '<%=screenName%>';
            if (screenName == "LCLBooking" || screenName == "LCLQuotation") {
                if (obj.value == 'Show CodeJ Contacts') {
                    document.getElementById('showAll').value = 'Show All Contacts';
                    document.getElementById('showAll1').value = 'Show All Contacts';
                    document.getElementById('customerCodeJContact').style.display = 'block';
                    document.getElementById('customerAllContact').style.display = 'none';
                } else {
                    document.getElementById('showAll').value = 'Show CodeJ Contacts';
                    document.getElementById('showAll1').value = 'Show CodeJ Contacts';
                    document.getElementById('customerCodeJContact').style.display = 'none';
                    document.getElementById('customerAllContact').style.display = 'block';
                }
            } else if (screenName == "LCLIMPBooking") {
                if (obj.value == 'Show CodeF Contacts') {
                    document.getElementById('showAll').value = 'Show All Contacts';
                    document.getElementById('showAll1').value = 'Show All Contacts';
                    document.getElementById('customerCodeFContact').style.display = 'block';
                    document.getElementById('customerAllContact').style.display = 'none';
                } else {
                    document.getElementById('showAll').value = 'Show CodeF Contacts';
                    document.getElementById('showAll1').value = 'Show CodeF Contacts';
                    document.getElementById('customerCodeFContact').style.display = 'none';
                    document.getElementById('customerAllContact').style.display = 'block';
                }
            }
            else {
                if (obj.value == 'Show All Contacts') {
                    document.getElementById('showAll').value = 'Show CodeC Contacts';
                    document.getElementById('showAll1').value = 'Show CodeC Contacts';
                    document.getElementById('customerCodeCContact').style.display = 'none';
                    document.getElementById('customerAllContact').style.display = 'block';
                } else {
                    document.getElementById('showAll').value = 'Show All Contacts';
                    document.getElementById('showAll1').value = 'Show All Contacts';
                    document.getElementById('customerCodeCContact').style.display = 'block';
                    document.getElementById('customerAllContact').style.display = 'none';
                }
            }
        }

        function changeChecked(obj, rowIndex) {
            var documentName = document.getElementById('documentName' + rowIndex);
            if (obj.checked) {
                if (obj.value == "fax") {
                    if (trim(document.getElementById('toFaxNumber').value) == "") {
                        if (parent.parent.document.getElementById("fax")) {
                            var fax = parent.parent.document.getElementById("fax").value;
                            if (undefined != fax && fax != null) {
                                document.getElementById("toFaxNumber").value = fax;
                            }
                        }
                        if (trim(document.getElementById('toName').value) == "") {
                            if (parent.parent.document.getElementById("contactName")) {
                                var toFaxName = parent.parent.document.getElementById("contactName").value;
                                if (undefined != toFaxName && toFaxName != null) {
                                    document.getElementById("toName").value = toFaxName.toUpperCase();
                                }
                            }
                        }
                        if (documentName.value === "Release Order") {
                            defaultFaxNumberForReleaseOrder();
                            document.getElementById('releaseOrdrFaxDivCons').style.display = 'block';
                            document.getElementById('releaseOrdrFaxDivCfs').style.display = 'block';
                            document.getElementById('releaseOrdrFaxDivNotify').style.display = 'block';
                            document.getElementById('releaseOrdrFaxDivNotify2').style.display = 'block';
                            document.getElementById('releaseOrdrFaxDivIpi').style.display = 'block';
                        } else {
                            document.getElementById('releaseOrdrFaxDivCons').style.display = "none";
                            document.getElementById('releaseOrdrFaxDivCfs').style.display = 'none';
                            document.getElementById('releaseOrdrFaxDivNotify').style.display = 'none';
                            document.getElementById('releaseOrdrFaxDivNotify2').style.display = 'none';
                            document.getElementById('releaseOrdrFaxDivIpi').style.display = 'none';
                        }
                        obj.checked = false;
                        Effect.SlideUp('printConfig');
                        Effect.SlideDown('faxTemplate');
                    }
                    setDefaultSubject("subject", rowIndex);
                } else if (obj.value == "email") {
                    var screenName = document.getElementById('screenName' + rowIndex);
                    var importflagforExport = jQuery("#importflagForExport").val();
                    var moduleforExport = jQuery("#moduleForExport").val();
                    var screennameforExport = jQuery("#screenNameForExport").val();
                    if (screenName.value === "LCLIMPBooking" && documentName.value === "Release Order") {
                        var report = document.getElementById('report' + rowIndex);
                        defaultEmailAddressForReleaseOrder();
                        document.getElementById('releaseOrdrEmailDivCons').style.display = 'block';
                        document.getElementById('releaseOrdrEmailDivCfs').style.display = 'block';
                        document.getElementById('releaseOrdrEmailDivNotify').style.display = 'block';
                        document.getElementById('releaseOrdrEmailDiv2ndNotify').style.display = 'block';
                        document.getElementById('releaseOrdrEmailDivIpi').style.display = 'block';
                        generateReport(screenName.value, documentName.value, rowIndex);
                        setDefaultSubject("emailSubject", rowIndex);
                        Effect.SlideUp('printConfig');
                        Effect.SlideDown('emailTemplate');
                    } else {
                        if (trim(document.getElementById('toAddress').value) == "") {
                            var email = parent.parent.document.getElementById("email") ? parent.parent.document.getElementById("email").value : "";
                            if (trim(email) != "") {
                                document.getElementById("toAddress").value = email;
                                var documentName = document.getElementById('documentName' + rowIndex);
                                var screenName = document.getElementById('screenName' + rowIndex);
                                var report = document.getElementById('report' + rowIndex);
                                generateReport(screenName.value, documentName.value, rowIndex);
                                obj.checked = true;
                            } else {
                                obj.checked = false;
                                var report = document.getElementById('report' + rowIndex);
                                if (screenName.value === "LCLIMPBooking") {
                                    if (documentName.value === "Release Order") {
                                        defaultEmailAddressForReleaseOrder();
                                        document.getElementById('releaseOrdrEmailDivCons').style.display = 'block';
                                        document.getElementById('releaseOrdrEmailDivCfs').style.display = 'block';
                                        document.getElementById('releaseOrdrEmailDivNotify').style.display = 'block';
                                        document.getElementById('releaseOrdrEmailDiv2ndNotify').style.display = 'block';
                                        document.getElementById('releaseOrdrEmailDivIpi').style.display = 'block';
                                    } else {
                                        document.getElementById('releaseOrdrEmailDivCons').style.display = "none";
                                        document.getElementById('releaseOrdrEmailDivCfs').style.display = 'none';
                                        document.getElementById('releaseOrdrEmailDivNotify').style.display = 'none';
                                        document.getElementById('releaseOrdrEmailDiv2ndNotify').style.display = 'none';
                                        document.getElementById('releaseOrdrEmailDivIpi').style.display = 'none';
                                    }
                                }
                                if (importflagforExport === 'false' && moduleforExport === 'FCL' && (screennameforExport === 'BL' || screennameforExport === 'Booking' || screennameforExport === 'Quotation')) {
                                    if (obj.checked === 'true') {
                                    } else {
                                        setDefaultSubject("emailSubject", rowIndex);
                                        Effect.SlideUp('printConfig');
                                        Effect.SlideDown('emailTemplate');
                                    }

                                } else {
                                    generateReport(screenName.value, documentName.value, rowIndex);
                                    setDefaultSubject("emailSubject", rowIndex);
                                    Effect.SlideUp('printConfig');
                                    Effect.SlideDown('emailTemplate');
                                }
                            }
                        }
                    }
                } else {
                    var documentName = document.getElementById('documentName' + rowIndex);
                    setDefaultSubject("emailSubject", rowIndex);
                }
                var fileLocation = document.getElementById('fileLocation' + rowIndex).value;

                if (trim(fileLocation) == "") {
                    if (importflagforExport === 'false' && moduleforExport === 'FCL' && (screennameforExport === 'BL' || screennameforExport === 'Booking' || screennameforExport === 'Quotation')) {
                        if (obj.checked === 'true') {
                        } else if (obj.checked === 'false') {
                            setDefaultSubject("emailSubject", rowIndex);
                            Effect.SlideUp('printConfig');
                            Effect.SlideDown('emailTemplate');
                        }
                    } else {
                        var documentName = document.getElementById('documentName' + rowIndex);
                        var screenName = document.getElementById('screenName' + rowIndex);
                        var report = document.getElementById('report' + rowIndex);
                        generateReport(screenName.value, documentName.value, rowIndex);
                    }
                }
            }
        }

        function submitPrint() {
            var screenName = document.getElementById('screenName1').value;
            var fileId = document.getElementById('fileId').value;
            var checked = false;
            for (var i = 1; i < <%=docCount%>; i++) {
                var print = false;
                var email = false;
                var fax = false;
                var emailMe = false;
                if (document.getElementById('print' + i)) {
                    print = document.getElementById('print' + i).checked;
                }
                if (document.getElementById('email' + i)) {
                    email = document.getElementById('email' + i).checked;
                }
                if (document.getElementById('fax' + i)) {
                    fax = document.getElementById('fax' + i).checked;
                    var documentValues = document.getElementById('documentName' + i).value;
                    if (documentValues === "Pre Advice/Arrival Notice/Status Update") {
                        documentValues = '${lclDocumentName}';
                    }
                }
                if (document.getElementById('emailMe' + i)) {
                    emailMe = document.getElementById('emailMe' + i).checked;
                }
                var emailSp = false;
                if (document.getElementById('emailSp' + i)) {
                    emailSp = document.getElementById('emailSp' + i).checked;
                }
                var fileNo = document.getElementById('fileNo').value;
                var subject = "";
                var fName = document.getElementById('screenName' + i).value;
                var documentName = document.getElementById('documentName' + i);
                var fileNumber = document.getElementById('fileNo').value;
                var screenName = document.getElementById("screenNameForRef").value;//ScreenName
                var unitNo = document.getElementById('unitNo').value;
                if (jQuery("#documentName").val() === "Invoice") {
                    subject = '${applicationEmailCompanyName}' + ' Invoice ' + fileNumber;
                } else if (screenName.length > 3 && screenName.substring(0, 3) === 'LCL') {
                    if (documentName.value !== undefined && (screenName === "LCLIMPBooking" || screenName === "LCLBooking" || fName === "LCLBL")) {
                        var documentV = documentName.value;
                        if (documentName === "Pre Advice/Arrival Notice/Status Update") {
                            documentV = '${lclDocumentName}';
                        }
                        if (fName === "LCLIMPBooking") {
                            if (documentName.value === "Pre Advice/Arrival Notice/Status Update" && unitNo !== "") {
                                subject = '${applicationEmailCompanyName}' + ' ' + documentV + ' ' + fileNumber.replace("IMP-", "") + ' (' + unitNo + ')';
                            } else {
                                subject = '${applicationEmailCompanyName}' + ' ' + documentV + ' ' + fileNumber.replace("IMP-", "");
                            }
                        } else {
                            if (fName === "LCLBooking" && parent.parent.$("#exportDisposition").val() === 'OBKG') {
                                subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "") + ' ' + 'BOOKING';
                            } else if (fName === "LCLBL") {
                                subject = '${applicationEmailCompanyName}' + '  ' + parent.parent.$("#fileNumberPrefix").val() + '  ' + documentV;
                            } else if (fName === "LCLBooking") {
                                subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "") + ' ' + 'DOCK RECEIPT';
                            }
                        }
                    } else if (documentName.value === undefined) {
                        if (screenName === "LCLIMPBooking") {
                            subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "");
                        } else {
                            if (fName === "LCLBooking" && parent.parent.$("#exportDisposition").val() === 'OBKG') {
                                subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "") + ' ' + 'BOOKING';
                            } else if (fName === "LCLBL") {
                                subject = '${applicationEmailCompanyName}' + '  ' + parent.parent.$("#fileNumberPrefix").val();
                            } else if (fName === "LCLBooking") {
                                subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "") + ' ' + 'DOCK RECEIPT';
                            }
                        }
                    }
                    if (documentName.value !== undefined && fName === "LCLQuotation") {
                        if (parent.parent.$("#quoteOriginDestinationPrefix").val() !== undefined
                                && parent.parent.$("#quoteOriginDestinationPrefix").val() !== null && parent.parent.$("#quoteOriginDestinationPrefix").val() !== '') {
                            subject = '${applicationEmailCompanyName}' + ' ' + parent.parent.$("#moduleId").val() + ' QUOTE For ' + parent.parent.$("#quoteOriginDestinationPrefix").val();
                        } else {
                            subject = '${applicationEmailCompanyName}' + '-LCL-' + documentName.value + ' For-' + fileNumber.replace("IMP-", "");
                        }
                    } else if (documentName.value === undefined && fName === "LCLQuotation") {
                        if (parent.parent.$("#quoteOriginDestinationPrefix").val() !== undefined &&
                                parent.parent.$("#quoteOriginDestinationPrefix").val() !== null && parent.parent.$("#quoteOriginDestinationPrefix").val() !== '') {
                            subject = '${applicationEmailCompanyName}' + ' ' + parent.parent.$("#moduleId").val() + ' QUOTE For ' + parent.parent.$("#quoteOriginDestinationPrefix").val();
                        } else {
                            subject = '${applicationEmailCompanyName}' + '-LCL-Quotation For-' + fileNumber.replace("IMP-", "");
                        }
                    }
                    if (documentName.value !== undefined && screenName === "LCLImpUnits") {
                        subject = '${applicationEmailCompanyName}' + ' ' + documentName.value + ' ' + fileNumber;
                    } else if (documentName.value === undefined && screenName === "LCLImpUnits") {
                        subject = '${applicationEmailCompanyName}' + ' ' + fileNumber;
                    }
                } else if (fileNumber.indexOf('-') != -1) {
                    if (nameOfScreen == "QUOTE") {
                        subject = '${applicationEmailCompanyName}' + '-FCL-Quote-' + fileNumber + ' For ' + emailDestination;
                    } else if (nameOfScreen == "BOOKING") {
                        subject = '${applicationEmailCompanyName}' + '-FCL-Booking-' + documentName.value + '-' + '04-' + fileNumber + ' For ' + emailDestination;
                    } else if (nameOfScreen == "BL") {
                        subject = '${applicationEmailCompanyName}' + '-FCL-BL-' + documentName.value + '-' + '04-' + fileNumber + ' For ' + emailDestination;
                    } else {
                        subject = '${applicationEmailCompanyName}' + '-FCL-' + fileNumber + '-' + documentName.value + ' For ' + emailDestination;
                    }
                } else if (documentName.value !== undefined && screenName === "LclCreditDebitNote") {
                    var array = document.getElementById('documentName' + i).value.split('=');
                    if (array != null && array != undefined && array.length > 2) {
                        var accountName = array[0];
                        var number = array[1];
                        var note = array[2];
                        note = note.replace(')', '');
                        note = note.replace('(', '');
                        subject = '${applicationEmailCompanyName}' + ' ' + fileNumber + '-' + note + ' ' + 'FOR ' + accountName + ' ' + number;
                    } else {
                        subject = '${applicationEmailCompanyName}' + ' ' + fileNumber;
                    }
                } else {
                    if (document.getElementById('documentName' + i).value.indexOf(')=') > -1) {
                        var array = document.getElementById('documentName' + i).value.split('=');
                        if (array != null && array != undefined && array.length > 2) {
                            var accountName = array[0];
                            var number = array[1];
                            var note = array[2];
                            note = note.replace(')', '');
                            note = note.replace('(', '');
                            subject = '${applicationEmailCompanyName}' + '-FCL-04-' + fileNumber + '-' + note + ' ' + 'FOR ' + accountName + ' ' + number;
                        } else {
                            if (documentName.value != 'Freighted Non-neg House BL' || documentName.value != 'Freighted Original House BL'
                                    || documentName.value != 'UnFreighted Non-neg House BL' || documentName.value != 'UnFreighted Original House BL'
                                    || documentName.value != 'Unmarked House Bill of Lading') {
                                subject = '${applicationEmailCompanyName}' + '-FCL-BL-' + documentName.value + '-' + '04-' + fileNumber + ' For ' + emailDestination;
                            }
                        }
                    } else if (documentName.value != 'Freighted Non-neg House BL' || documentName.value != 'Freighted Original House BL'
                            || documentName.value != 'UnFreighted Non-neg House BL' || documentName.value != 'UnFreighted Original House BL'
                            || documentName.value != 'Unmarked House Bill of Lading') {
                        subject = '${applicationEmailCompanyName}' + '-FCL-BL-' + documentName.value + '-' + '04-' + fileNumber + ' For ' + emailDestination;
                    }
                }
                fillDefaultMessage(document.getElementById('comment').value);
                var emailSubjectText = "";
                if (document.getElementById('subject').value != '') {
                    emailSubjectText = document.getElementById('subject').value;
                } else {
                    emailSubjectText = subject;
                }
                var emailSubjectData = "";
                if (emailSubjectChanged && document.getElementById('emailSubject').value != '') {
                    emailSubjectData = subject;
                } else if (subject != '') {
                    emailSubjectData = subject;
                } else if (fName.length > 3 && fName.substring(0, 3) == 'LCL' && document.getElementById('femailSubject').value != '') {
                    emailSubjectText = document.getElementById('femailSubject').value;
                }
                if (screenName == 'LCLIMPBooking') {
                    var emailAll = false;
                    if (document.getElementById('emailAll' + i) != null) {
                        emailAll = document.getElementById('emailAll' + i).checked;
                    }
                    if (print || email || fax || emailMe || emailSp || emailAll) {
                        checked = true;
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.dwr.PrintUtil",
                                methodName: "savePrintConfiguration",
                                formName: "com.gp.cong.logisoft.struts.form.PrintConfigForm",
                                documentName: documentName.value,
                                id: fileId,
                                fileNumber: fileNo,
                                print: print,
                                email: email,
                                fax: fax,
                                emailMe: emailMe,
                                emailAll: emailAll,
                                emailSp: emailSp,
                                screenName: document.getElementById('screenName' + i).value,
                                fileLocation: document.getElementById('fileLocation' + i).value,
                                toName: document.getElementById('toName').value,
                                toFaxNumber: document.getElementById('toFaxNumber').value,
                                fromName: document.getElementById('fromName').value,
                                fromFaxNumber: document.getElementById('fromFaxNumber').value,
                                businessPhone: document.getElementById('businessPhone').value,
                                homePhone: document.getElementById('homePhone').value,
                                comment: document.getElementById('emailComment').value,
                                subject: emailSubjectText,
                                message: document.getElementById('message').value,
                                toAddress: document.getElementById('toAddress').value,
                                ccAddress: document.getElementById('ccAddress').value,
                                bccAddress: document.getElementById('bccAddress').value,
                                emailSubject: emailSubjectData,
                                emailMessage: document.getElementById('emailComment').value,
                                //emailMessageOti: document.getElementById('emailComment').value,
                                printerName: document.getElementById('printerName' + i).value,
                                printerTray: document.getElementById('printerTray' + i).value,
                                printCopy: document.getElementById('printCopy' + i).value,
                                contactId: localStorage.getItem('lclContactId') !== null ? localStorage.getItem('lclContactId') : "",
                                accountNo: localStorage.getItem('lclAccountNo') !== null ? localStorage.getItem('lclAccountNo') : "",
                                brand: document.getElementById('brand').value,
                                dataType: "json",
                                request: "true"
                            },
                            preloading: true,
                            success: function (data) {
                                checked = data;
                            }
                        });
                    }
                } else {
                    if (print || email || fax || emailMe || emailSp) {
                        checked = true;
                        jQuery.ajaxx({
                            dataType: "json",
                            data: {
                                className: "com.gp.cong.logisoft.dwr.PrintUtil",
                                methodName: "savePrintConfiguration",
                                formName: "com.gp.cong.logisoft.struts.form.PrintConfigForm",
                                id: fileId,
                                fileNumber: fileNo,
                                print: print,
                                email: email,
                                fax: fax,
                                emailMe: emailMe,
                                emailSp: emailSp,
                                screenName: document.getElementById('screenName' + i).value,
                                documentName: documentName.value,
                                fileLocation: document.getElementById('fileLocation' + i).value,
                                toName: document.getElementById('toName').value,
                                toFaxNumber: document.getElementById('toFaxNumber').value,
                                fromName: document.getElementById('fromName').value,
                                fromFaxNumber: document.getElementById('fromFaxNumber').value,
                                businessPhone: document.getElementById('businessPhone').value,
                                homePhone: document.getElementById('homePhone').value,
                                comment: document.getElementById('emailComment').value,
                                subject: emailSubjectText,
                                message: document.getElementById('message').value,
                                toAddress: document.getElementById('toAddress').value,
                                ccAddress: document.getElementById('ccAddress').value,
                                bccAddress: document.getElementById('bccAddress').value,
                                emailSubject: emailSubjectData,
                                emailMessage: document.getElementById('emailComment').value,
                                //                                emailMessageOti: document.getElementById('emailComment').value,
                                printerName: document.getElementById('printerName' + i).value,
                                printerTray: document.getElementById('printerTray' + i).value,
                                printCopy: document.getElementById('printCopy' + i).value,
                                filesToPrint: document.getElementById('filesToPrint').value,
                                contactId: localStorage.getItem('fclContactId') !== null ? localStorage.getItem('fclContactId') : "",
                                accountNo: localStorage.getItem('fclAccountNo') !== null ? localStorage.getItem('fclAccountNo') : "",
                                fromEmailAddress: jQuery("#fromEmailAddress").val() !== '' ? jQuery("#fromEmailAddress").val() : "",
                                nameFromTerminal: jQuery("#nameFromTerminal").val() !== '' ? jQuery("#nameFromTerminal").val() : "",
                                importflagForExport: jQuery("#importflagForExport").val() !== '' ? jQuery("#importflagForExport").val() : "",
                                moduleForExport: jQuery("#moduleForExport").val() !== '' ? jQuery("#moduleForExport").val() : "",
                                screenNameForExport: jQuery("#screenNameForExport").val() !== '' ? jQuery("#screenNameForExport").val() : "",
                                brand: document.getElementById('brand').value,
                                terminalPhone: jQuery("#phoneTerminalNo").val() !== '' ? jQuery("#phoneTerminalNo").val() : "",
                                terminalFax: jQuery("#faxTerminalNo").val() !== '' ? jQuery("#faxTerminalNo").val() : "",
                                userTerminalPhoneNo: jQuery("#userTerminalPhoneNo").val()!== '' ? jQuery("#userTerminalPhoneNo").val(): "",
                                userTerminalFaxNo: jQuery("#userTerminalFaxNo").val()!== '' ? jQuery("#userTerminalFaxNo").val(): "",
                                dataType: "json",
                                request: "true"
                            },
                            preloading: true,
                            success: function (data) {
                                checked = data;
                            }
                        });
                    }
                }
                if (document.getElementById('print' + i)) {
                    document.getElementById('print' + i).checked = false;
                }
                if (document.getElementById('email' + i)) {
                    document.getElementById('email' + i).checked = false;
                }
                if (document.getElementById('fax' + i)) {
                    document.getElementById('fax' + i).checked = false;
                }
                if (document.getElementById('emailMe' + i)) {
                    document.getElementById('emailMe' + i).checked = false;
                }
            }
            if (checked) {
                jQuery.prompt("Your Request has been sent Successfully", {
                    callback: function () {
                        parent.parent.GB_hide();
                    }
                });
            } else {
                jQuery.prompt("You must check a box in order to submit");
            }
        }



        function validateToFaxNumber(obj) {
            var faxNumbers = obj.value.split(",");
            for (var i = 0; i < faxNumbers.length; i++) {
                if (faxNumbers[i].indexOf("-") > -1) {
                    var faxNumber = faxNumbers[i].split("-");
                    for (var j = 0; j < faxNumber.length; j++) {
                        if (isNaN(faxNumber[j])) {
                            obj.value = "";
                            obj.focus();
                            jQuery.prompt("Invalid Fax Number, Please Enter digits only", {
                                callback: function () {
                                    return false;
                                }
                            });
                        }
                    }
                } else {
                    if (isNaN(faxNumbers[i]) || Number(faxNumbers[i]) == 0) {
                        obj.value = "";
                        obj.focus();
                        jQuery.prompt("Invalid Fax Number, Please Enter digits only", {
                            callback: function () {
                                return false;
                            }
                        });
                    }
                }
            }
        }
        function validateFromFaxNumber(obj) {
            if (obj.value.indexOf("-") > -1) {
                var faxNumber = obj.value.split("-");
                for (var j = 0; j < faxNumber.length; j++) {
                    if (isNaN(faxNumber[j])) {
                        obj.value = "";
                        obj.focus();
                        jQuery.prompt("Invalid Fax Number, Please Enter digits only", {
                            callback: function () {
                                return false;
                            }
                        });
                    }
                }
            } else {
                if (isNaN(obj.value) || Number(obj.value) == 0) {
                    obj.value = "";
                    obj.focus();
                    jQuery.prompt("Invalid Fax Number, Please Enter digits only", {
                        callback: function () {
                            return false;
                        }
                    });
                }
            }
        }

        function validatePhoneNumber(obj) {
            if (obj.value.indexOf("-") >= 0) {
                var phoneNumber = obj.value.split("-");
                for (var i = 0; i < phoneNumber.length; i++) {
                    if (isNaN(phoneNumber[i])) {
                        obj.value = "";
                        obj.focus();
                        jQuery.prompt("Invalid Phone Number, Please Enter digits only", {
                            callback: function () {
                                return false;
                            }
                        });
                    }
                }
            } else {
                if (isNaN(obj.value) || Number(obj.value) == 0) {
                    obj.value = "";
                    obj.focus();
                    jQuery.prompt("Invalid Phone Number, Please Enter digits only", {
                        callback: function () {
                            return false;
                        }
                    });
                }
            }
        }

        function viewFile(fileName) {
            if (fileName.indexOf(".xls") > 0 || fileName.indexOf(".doc") > 0
                    || fileName.indexOf(".mht") > 0 || fileName.indexOf(".msg") > 0
                    || fileName.indexOf(".csv") > 0 || fileName.indexOf(".ppt") > 0) {
                window.open("<%=path%>/servlet/FileViewerServlet?fileName=" + fileName, "", "resizable=1,location=1,status=1,scrollbars=1, width=600,height=400");
            } else {
                window.open('<%=path%>/servlet/FileViewerServlet?fileName=' + fileName, '_blank', 'resizable=1,width=1200,height=600,top=40,left=40,directories=no,location=no,linemenubar=no,toolbar=no,status=no');
            }
        }

        function cancel() {
            if (undefined != window.close()) {
                window.close();
            } else {
                parent.parent.GB_hide();
            }
        }
        function cancelFax() {
            Effect.SlideUp('faxTemplate');
            Effect.toggle('printConfig');
        }
        function cancelEmail() {
            document.getElementById('emailSubject').value = "";
            Effect.SlideUp('emailTemplate');
            Effect.toggle('printConfig');
            document.getElementById("emailFormId").className = "buttonStyleNew";
        }

        function generateReport(screenName, documentName, index) {
            var fromAddress = jQuery('#fromEmailAddress').val();
            fromAddress = undefined != fromAddress && null != fromAddress ? fromAddress : "";
            var fromName = jQuery('#nameFromTerminal').val();
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.PrintUtil",
                    methodName: "printReport",
                    param1: screenName,
                    param2: documentName,
                    param3: "false",
                    param4: fromAddress,
                    param5: fromName,
                    request: "true"
                },
                preloading: true,
                success: function (data) {
                    document.getElementById('fileLocation' + index).value = data;
                }
            });
        }

        function setFaxFormValues() {
            if (trim(document.getElementById('toFaxNumber').value) == "") {
                alert("Please enter 'TO Fax Number'");
                document.getElementById('toFaxNumber').focus();
                return false;
            }
            Effect.SlideUp('faxTemplate');
            Effect.toggle('printConfig');
            document.getElementById("faxFormId").className = "buttonColor";
            var faxCheckBox = document.getElementsByName("fax");
            if (null != faxCheckBox && faxCheckBox.length > 0) {
                for (i = 0; i < faxCheckBox.length; i++) {
                    faxCheckBox[i].disabled = false;
                }
            }

        }
        function openEmailForm() {
            var spEmailId = "";
            for (var i = 1; i <<%=docCount%>; i++) {
                var email = document.getElementById('email' + i) ? document.getElementById('email' + i).checked : false;
                var emailSp = document.getElementById('emailSp' + i) ? document.getElementById('emailSp' + i).checked : false;
                var toAddress = trim(document.getElementById("toAddress").value);
                if (trim(toAddress) == "" && document.getElementById("email")) {
                    toAddress = trim(parent.parent.document.getElementById("email").value);
                }
                spEmailId = document.getElementById("spEmailId").value;
                if (email && emailSp) {
                    document.printConfigForm.toAddress.value = toAddress;
                    document.printConfigForm.ccAddress.value = spEmailId;
                } else if (email && !emailSp) {
                    if (toAddress != "") {
                        var result = toAddress.replace(spEmailId, "");
                        document.printConfigForm.toAddress.value = result;
                    }
                } else if (!email && emailSp) {
                    if (spEmailId != "") {
                        document.printConfigForm.toAddress.value = spEmailId;
                    }
                }
            }
            if (trim(document.getElementById('toAddress').value) == "") {
                if (parent.parent.document.getElementById("email")) {
                    var email = parent.parent.document.getElementById("email").value;
                    if (undefined != email && email != null) {
                        document.getElementById("toAddress").value = email;
                    }
                }
            }
            setDefaultSubject("emailSubject", "");
            Effect.SlideUp('printConfig');
            Effect.SlideDown('emailTemplate');
        }
        function openFaxForm() {
            if (trim(document.getElementById('toFaxNumber').value) == "") {
                if (parent.parent.document.getElementById("fax")) {
                    var fax = parent.parent.document.getElementById("fax").value;
                    if (undefined != fax && fax != null) {
                        document.getElementById("toFaxNumber").value = fax;
                    }
                }
            }
            if (trim(document.getElementById('toName').value) == "") {
                if (parent.parent.document.getElementById("contactName")) {
                    var toFaxName = parent.parent.document.getElementById("contactName").value;
                    if (undefined != toFaxName && toFaxName != null) {
                        document.getElementById("toName").value = toFaxName;
                    }
                }
            }
            document.getElementById('releaseOrdrFaxDivCons').style.display = "none";
            document.getElementById('releaseOrdrFaxDivCfs').style.display = 'none';
            document.getElementById('releaseOrdrFaxDivNotify').style.display = 'none';
            document.getElementById('releaseOrdrFaxDivNotify2').style.display = 'none';
            document.getElementById('releaseOrdrFaxDivIpi').style.display = 'none';
            setDefaultSubject("subject", "");
            Effect.SlideUp('printConfig');
            Effect.SlideDown('faxTemplate');
        }

        var emailSubject = "";
        var emailDestination = "";
        var nameOfScreen = "";

        function setDefaultSubject(id, rowIndex) {
            var subject = "";
            var documentName = "";
            if (rowIndex && rowIndex !== "") {
                documentName = document.getElementById('documentName' + rowIndex);
            } else {
                documentName.value = "";
            }
            var fName = jQuery('#screenName1').val();
            var fileNumber = jQuery('#fileNo').val();
            var unitNo = jQuery('#unitNo').val();
            if (jQuery("#documentName").val() === "Invoice") {
                subject = '${applicationEmailCompanyName}' + ' Invoice ' + fileNumber;
                jQuery('#' + id).val(subject);
                fillDefaultMessage(jQuery('#comment').val());
            } else if (fName.length > 3 && fName.substring(0, 3) === 'LCL') {
                if (documentName.value !== undefined && (fName === "LCLIMPBooking" || fName === "LCLBooking" || fName === "LCLBL")) {
                    var documentV = documentName.value;
                    if (documentName === "Pre Advice/Arrival Notice/Status Update") {
                        documentV = '${lclDocumentName}';
                    }
                    if (fName === "LCLIMPBooking") {
                        if (documentName.value === "Pre Advice/Arrival Notice/Status Update" && unitNo !== "") {
                            subject = '${applicationEmailCompanyName}' + ' ' + documentV + ' ' + fileNumber.replace("IMP-", "") + ' (' + unitNo + ')';
                        } else {
                            subject = '${applicationEmailCompanyName}' + ' ' + documentV + ' ' + fileNumber.replace("IMP-", "");
                        }
                    } else {
                        if (fName === "LCLBooking" && parent.parent.$("#exportDisposition").val() === 'OBKG') {
                            subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "") + ' ' + 'BOOKING';
                        } else if (fName === "LCLBL") {
                            subject = '${applicationEmailCompanyName}' + '  ' + parent.parent.$("#fileNumberPrefix").val() + '  ' + documentV;
                        } else if (fName === "LCLBooking") {
                            subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "") + ' ' + 'DOCK RECEIPT';
                        }
                    }

                } else if (documentName.value === undefined) {
                    if (fName === "LCLIMPBooking") {
                        subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "");
                    } else {
                        if (fName === "LCLBooking" && parent.parent.$("#exportDisposition").val() === 'OBKG') {
                            subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "") + ' ' + 'BOOKING';
                        } else if (fName === "LCLBL") {
                            subject = '${applicationEmailCompanyName}' + ' ' + parent.parent.$("#fileNumberPrefix").val();
                        } else if (fName === "LCLBooking") {
                            subject = '${applicationEmailCompanyName}' + ' ' + fileNumber.replace("IMP-", "") + ' ' + 'DOCK RECEIPT';
                        }
                    }
                }
                if (documentName.value !== undefined && fName === "LCLQuotation") {
                    if (parent.parent.$("#quoteOriginDestinationPrefix").val() !== undefined
                            && parent.parent.$("#quoteOriginDestinationPrefix").val() !== null && parent.parent.$("#quoteOriginDestinationPrefix").val() !== '') {
                        subject = '${applicationEmailCompanyName}' + ' ' + parent.parent.$("#moduleId").val() + ' QUOTE For ' + parent.parent.$("#quoteOriginDestinationPrefix").val();
                    } else {
                        subject = '${applicationEmailCompanyName}' + 'QUOTE For ' + fileNumber.replace("IMP-", "");
                    }
                } else if (documentName.value === undefined && fName === "LCLQuotation") {
                    if (parent.parent.$("#quoteOriginDestinationPrefix").val() !== undefined &&
                            parent.parent.$("#quoteOriginDestinationPrefix").val() !== null && parent.parent.$("#quoteOriginDestinationPrefix").val() !== '') {
                        subject = '${applicationEmailCompanyName}' + ' ' + parent.parent.$("#moduleId").val() + ' QUOTE For ' + parent.parent.$("#quoteOriginDestinationPrefix").val();
                    } else {
                        subject = '${applicationEmailCompanyName}' + 'QUOTE For ' + fileNumber.replace("IMP-", "");
                    }
                }
                if (documentName.value !== undefined && fName === "LCLImpUnits") {
                    subject = '${applicationEmailCompanyName}' + ' ' + documentName.value + ' ' + fileNumber;
                } else if (documentName.value === undefined && fName === "LCLImpUnits") {
                    subject = '${applicationEmailCompanyName}' + ' ' + fileNumber;
                }
                jQuery('#' + id).val(subject);
                fillDefaultMessage(jQuery('#comment').val());
            } else if (undefined != fName && fName == 'Quotation') {
                if (document.getElementById('emailSubject').value != '') {
                    subject = document.getElementById('emailSubject').value;
                } else if (parent.parent.document.searchQuotationform && parent.parent.document.searchQuotationform.portofDischarge) {
                    portofDischarge = parent.parent.document.searchQuotationform.portofDischarge.value;
                    dest = portofDischarge.split('/');
                    if (undefined != dest[0] && dest[0] != null) {
                        subject = '${applicationEmailCompanyName}' + '-FCL-Quote-' + fileNumber + ' For ' + dest[0];
                        document.getElementById(id).value = subject;
                        nameOfScreen = "QUOTE";
                    }
                }
            } else if (fName == 'BL' || fName == 'Booking' || documentName.value == 'FreightInvoice(Forwarder)' || documentName.value == 'ConfirmOnBoardNotice'
                    || (documentName && documentName != "" && documentName.value.indexOf("FCLArrivalNotice") != -1)) {
                if (document.getElementById('emailSubject').value != '') {
                    subject = document.getElementById('emailSubject').value;
                } else if (parent.parent.document.getElementById("finalDestination") && parent.parent.document.getElementById("readyToPost")) {
                    var portofDischarge = parent.parent.document.getElementById("finalDestination").value;
                    var dest = portofDischarge.split('/');
                    if (undefined != dest[0] && dest[0] != null) {
                        if ((undefined != documentName.value) && (documentName.value != 'Freighted Non-neg House BL' || documentName.value != 'Freighted Original House BL'
                                || documentName.value != 'UnFreighted Non-neg House BL' || documentName.value != 'UnFreighted Original House BL'
                                || documentName.value != 'Unmarked House Bill of Lading' || documentName.value != 'Booking Confirmation With Rate'
                                || documentName.value != 'Booking Confirmation Without Rate')) {
                            subject = '${applicationEmailCompanyName}' + '-FCL-BL-' + documentName.value + '-' + '04-' + fileNumber + ' For ' + dest[0];
                            nameOfScreen = "BL";
                        } else if ((undefined != documentName.value) && (documentName.value == 'Booking Confirmation With Rate' || documentName.value == 'Booking Confirmation Without Rate')) {
                            subject = '${applicationEmailCompanyName}' + '-FCL-Booking-' + documentName.value + '-' + '04-' + fileNumber + ' For ' + dest[0];
                            nameOfScreen = "BOOKING";
                        } else {
                            nameOfScreen = "BL";
                            subject = '${applicationEmailCompanyName}' + '-FCL-BL-' + '04-' + fileNumber + ' For ' + dest[0];
                        }
                        document.getElementById(id).value = subject;
                    }
                } else if (parent.parent.document.EditBookingsForm && parent.parent.document.EditBookingsForm.portOfDischarge) {
                    portofDischarge = parent.parent.document.EditBookingsForm.portOfDischarge.value;
                    dest = portofDischarge.split('/');
                    if (undefined != dest[0] && dest[0] != null) {
                        if (parent.parent.document.getElementById("bookingCompleteY")) {
                            if (document.getElementById('emailSubject').value != '') {
                                subject = document.getElementById('emailSubject').value;
                            } else if (undefined != documentName.value && documentName.value == 'Booking Confirmation With Rate') {
                                subject = '${applicationEmailCompanyName}' + '-FCL-Booking Confirmation With Rate-' + '04-' + fileNumber + ' For ' + dest[0];
                            } else if (undefined != documentName.value && documentName.value == 'Booking Confirmation Without Rate') {
                                subject = '${applicationEmailCompanyName}' + '-FCL-Booking Confirmation Without Rate-' + '04-' + fileNumber + ' For ' + dest[0];
                            } else {
                                subject = '${applicationEmailCompanyName}' + '-FCL-Booking-' + '04-' + fileNumber + ' For ' + dest[0];
                            }
                            nameOfScreen = "BOOKING";
                        }
                        document.getElementById(id).value = subject;
                    }
                }
            }
            if (emailSubject == "") {
                emailSubject = subject;
            }
            if (undefined != dest && dest != null) {
                emailDestination = dest[0];
            }
        }

        var emailSubjectChanged = false;
        var emailCoverOk = false;



        function resetSession(ev) {
            document.getElementById('fileLocation' + '1').value = "";
            document.getElementById('fileLocation' + '2').value = "";
            if (document.getElementById('fileLocation' + '3')) {
                document.getElementById('fileLocation' + '3').value = "";
            }
            if (document.getElementById('fileLocation' + '4')) {
                document.getElementById('fileLocation' + '4').value = "";
            }
            if (document.getElementById('fileLocation' + '5')) {
                document.getElementById('fileLocation' + '5').value = "";
            }
            if (document.getElementById('fileLocation' + '6')) {
                document.getElementById('fileLocation' + '6').value = "";
            }
            if (document.getElementById('fileLocation' + '7')) {
                document.getElementById('fileLocation' + '7').value = "";
            }
            if (document.getElementById('fileLocation' + '8')) {
                document.getElementById('fileLocation' + '8').value = "";
            }
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.dwr.PrintUtil",
                    methodName: "changeFileNo",
                    param1: ev,
                    request: "true"
                },
                success: function () {
                    //                    document.printConfigForm.pageAction.value = "doNothing";
                    //                    document.getElementById('newProgressBar').style.display = 'block';
                    //                    document.getElementById('cover').style.display = 'block';
                    //                    document.printConfigForm.submit();
                }
            });
        }

        function checkDefault() {
            var datatableobj = document.getElementById('fclBlCorrections');
            if (datatableobj != null) {
                //                    var length = datatableobj.rows.length - 1;
                var tablerowobj = datatableobj.rows[1];
                var obj1 = tablerowobj.cells[0];
                var obj2 = tablerowobj.cells[1];
                var obj3 = obj1.innerHTML + "==" + obj2.innerHTML;
                resetSession(obj3);
            }
        }

        function fillDefaultMessage(message) {
            document.getElementById('emailComment').className = "textbox";
            if (trim(document.getElementById('emailComment').value) == '') {
                document.getElementById('emailComment').value = message;
            }
            if (trim(document.getElementById('message').value) == '') {
                document.getElementById('message').value = message;
            }
        }
        initRTE("<%=path%>/js/cbrte/images/", "<%=path%>/js/cbrte/", "", true);

        if ($('#screenNameForRef').val() === 'LCLIMPBooking') {
            if (parent.parent.$("#lockMessage").val() !== undefined && parent.parent.$("#lockMessage").val() !== '') {
                var printFormValues = document.getElementById('printForm');
                var element;
                for (var i = 0; i < printFormValues.elements.length; i++) {
                    element = printFormValues.elements[i];
                    if (element.type == 'checkbox' || element.type == 'button' || element.type == 'text') {
                        element.style.visibility = 'hidden';
                        $('#contactButton').hide();

                    }
                }
            }
        }

        function populateDocDeptEmail() {
            if (jQuery("#docDeptEmail").is(":checked")) {
                jQuery("#bccAddress").val(jQuery("#docDeptEmail").val())
            } else {
                jQuery("#bccAddress").val("")
            }
        }

    </script>
    <%!public static String rteSafe(String strText) {
            //returns safe code for preloading in the RTE
            String tmpString = strText;

            //convert all types of single quotes
            tmpString = tmpString.replace((char) 145, (char) 39);
            tmpString = tmpString.replace((char) 146, (char) 39);
            tmpString = tmpString.replace("'", "&#39;");

            //convert all types of double quotes
            tmpString = tmpString.replace((char) 147, (char) 34);
            tmpString = tmpString.replace((char) 148, (char) 34);
            //	tmpString = tmpString.replace("\"", "\"");

            //replace carriage returns & line feeds
            tmpString = tmpString.replace((char) 10, (char) 32);
            tmpString = tmpString.replace((char) 13, (char) 32);

            return tmpString;
        }%>
    <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript" src="<%=path%>/js/autocomplete.js"></script>
    <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>

    <c:choose>
        <c:when test="${!empty noticeNumberList && fn:length(noticeNumberList)>0 && noReload == null}">
            <script type="text/javascript">makeEmailStatusButtonGreen('Correction');</script>
        </c:when><c:otherwise>
            <script type="text/javascript">makeEmailStatusButtonGreen('');</script>
        </c:otherwise>
    </c:choose>
    <style type="text/css">
        #customerContactDiv{
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
    </style>
</html>

