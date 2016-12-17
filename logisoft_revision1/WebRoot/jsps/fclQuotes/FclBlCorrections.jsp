
<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,java.util.*,java.text.SimpleDateFormat,com.gp.cvst.logisoft.domain.*,com.gp.cong.struts.LoadLogisoftProperties,com.gp.cvst.logisoft.hibernate.dao.*"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.FclBlConstants"/>
<jsp:directive.page import="java.text.DateFormat"/>
<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/>
<jsp:directive.page import="com.gp.cong.logisoft.util.CommonFunctions"/>
<jsp:directive.page import="com.gp.cong.logisoft.bc.notes.NotesConstants"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<un:useConstants className="com.logiware.constants.ItemConstants" var="itemConstants"/>
<bean:define id="defaultAgent" type="String">
    <bean:message key="defaultAgent"/>
</bean:define>
<%    String fileNo1 = "", fileBolNumber = "";
    if (request.getAttribute("fileNo") != null) {
        fileNo1 = "04-" + (String) request.getAttribute("fileNo");
    }
    if (CommonFunctions.isNotNull(request.getAttribute("displayBlNumber"))) {
        fileBolNumber = (String) request.getAttribute("displayBlNumber");
        fileNo1 = fileBolNumber.substring(fileBolNumber.indexOf("04-") + 3);
    }
    DBUtil dbUtil = new DBUtil();
    request.setAttribute("correctionCodeList", dbUtil.getGenericCodeList(52, "no", "Select Correction Code"));
%>
<html> 
    <head>
        <title>JSP for FclBlCorrectionsForm form</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <%@include file="../includes/baseResources.jsp"%>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script language="javascript"  src="${path}/js/common.js"/></script>
    <script type="text/javascript" src="${path}/js/caljs/calendar.js"></script>
    <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
    <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
    <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
    <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
    <script language="javascript">
        start = function() {
            visibilityOfButtonsBasedOnUserRole();
        };
        window.onload = start;
    </script>
    <script language="javascript" type="text/javascript">

        function SearchCorrections() {
            evencodeEmpty();
            document.fclBlCorrectionsForm.buttonValue.value = "search";
            document.fclBlCorrectionsForm.submit();
        }
        function AddPage(quickCn) {
            var blNumber = document.fclBlCorrectionsForm.blNumber.value;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.FclBlCorrectionsBC",
                    methodName: "approvedExistingCorrectionBeforeCreateNew",
                    param1: blNumber,
                    param2: "Correction"
                },
                success: function(data) {
                    if (data !== "null" && data !== null) {
                        alertNew("Please Post Last Correction before creating new Correction for this BL");
                    } else {
                        document.fclBlCorrectionsForm.quickCn.value = quickCn;
                        document.fclBlCorrectionsForm.buttonValue.value = "displayingCharges";
                        document.fclBlCorrectionsForm.submit();
                    }
                }
            });
        }
        function evencodeEmpty() {
            document.getElementById('eventCode').value = '';
            document.getElementById('eventDesc').value = '';
            document.getElementById('moduleRefId').value = '';
        }
        function editCorrectionRecord(val1, val2, val3,val4) {
            document.fclBlCorrectionsForm.noticeNo.value = val1;
            document.fclBlCorrectionsForm.blNumber.value = val2;
            document.fclBlCorrectionsForm.fileNo.value = val4;
            if (val3 === "approved") {
                document.fclBlCorrectionsForm.approval.value = val3;
            }
            document.fclBlCorrectionsForm.buttonValue.value = "edit";
            document.fclBlCorrectionsForm.submit();
        }
        function viewCorrectionRecordDup(val1, val2,val3) {
            document.fclBlCorrectionsForm.noticeNo.value = val1;
            document.fclBlCorrectionsForm.blNumber.value = val2;
            document.fclBlCorrectionsForm.fileNo.value = val3;
            document.fclBlCorrectionsForm.buttonValue.value = "edit";
            document.fclBlCorrectionsForm.viewMode.value = "view";
            document.fclBlCorrectionsForm.submit();
        }
        function checkLockAndNavigateToBl(fileNumber, path, moduleId) {
            var userId = '${userId}';
            var itemId = '${itemId}';
            var selectedMenu = '${selectedMenu}';
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC",
                    methodName: "cheackFileINDB",
                    param1: fileNumber,
                    param2: userId
                },
                async: false,
                success: function(data) {
                    if (data !== null && data !== "") {
                        if (data === 'sameUser') {
                            alert("File " + fileNumber + " is already opened in another window");
                            return;
                        } else {
                            alert(fileNumber + " This record is being used by " + data);
                        }
                    } else {
                        window.parent.changeChildsFromDisputedBl(path, fileNumber, moduleId, itemId, selectedMenu);
                    }
                }
            });
        }
        function approveUse(val1, val2, fileNo) {
            GB_show('Approval', '${path}/jsps/fclQuotes/authanticatePassword.jsp?notice=' + val1 + '&blId=' + val2 + '&fileNo=' + fileNo, width = "200", height = "400");
        }
        function searchBlock() {
            //window.location.href="${path}/jsps/fclQuotes/FclBlCorrections.jsp";
            document.fclBlCorrectionsForm.buttonValue.value = "clearSearch";
            document.fclBlCorrectionsForm.submit();
        }
        var delete1 = '';
        var delete2 = '';
        var fileNumber = '';
        function deleteBlCorrection(val1, val2, fileNo) {
            fileNumber = fileNo;
            delete1 = val1;
            delete2 = val2;
            confirmYesOrNo("Are you sure Y/N", "deleteBlCorrections");
        }
        function validateDate(date) {
            if (date.value !== "") {
                date.value = date.value.getValidDateTime("/", "", false);
                if (date.value === "") {
                    alertNew("Please enter valid date");
                    document.getElementById(date.id).focus();
                }
            }
        }
        function closePage() {
            parent.parent.GB_hide();
            parent.parent.makeFclBlCorrectionButtonRed();
        }
        function DisApproveUse(ev1, ev2, fileNo) {
            confirmYesOrNo("Are you sure Y/N", "disApprove");
            fileNumber = fileNo;
            delete1 = ev1;
            delete2 = ev2;

        }
        function execUpdate(data) {
            document.fclBlCorrectionsForm.buttonValue.value = "search";
            document.fclBlCorrectionsForm.submit();
        }
        function confirmMessageFunction(id1, id2) {
            if (id1 === 'deleteBlCorrections' && id2 === 'yes') {
                document.getElementById('eventCode').value = '100016';
                document.getElementById('eventDesc').value = "(" + delete2 + "-" + delete1 + ")  deleted";
                document.getElementById('moduleRefId').value = fileNumber;
                document.fclBlCorrectionsForm.testBoxNoticeNumber.value = delete1;
                document.fclBlCorrectionsForm.testBoxBlNumber.value = delete2;
                document.fclBlCorrectionsForm.buttonValue.value = "deleteCorrectionRecord";
                document.fclBlCorrectionsForm.submit();
            } else if (id1 === 'disApprove' && id2 === 'yes') {
                document.getElementById('eventCode').value = '100016';
                document.getElementById('eventDesc').value = "(" + delete2 + "-" + delete1 + ") Disapprove";
                document.getElementById('moduleRefId').value = fileNumber;
                alert("(" + delete2 + "-" + delete1 + ") Unapproved");
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
                        methodName: "disAproveCorrectedBL",
                        param1: delete1,
                        param2: delete2
                    },
                    success: function(data) {
                        execUpdate(data);
                    }
                });
            }
        }
        function userNameSetToTextBox(flag) {
            var val = '${loginuser.loginName}'
            if (flag === 'created') {
                if (document.getElementById("loginCheck").checked) {
                    document.fclBlCorrectionsForm.createdBy.value = val;
                } else {
                    document.fclBlCorrectionsForm.createdBy.value = "";
                }
            }
            if (flag === 'approved') {
                if (document.getElementById("approvedByCheck").checked && flag === 'approved') {
                    document.fclBlCorrectionsForm.approvedBy.value = val;
                } else {
                    document.fclBlCorrectionsForm.approvedBy.value = "";
                }
            }
        }
        function PrintReports(fileNo, noticeNo, id, bolNo) {//BlMainPrint1()
            GB_show('Fcl BL Correction Report', '${path}/printConfig.do?screenName=Corrections&bolNo=' + bolNo + '&fileNo=' + fileNo + "&noticeNo=" + noticeNo, 400, 600);
        }

        function PrintReportsOpenSeprately(fileNo, noticeNo, id, bolNo, debitOrCredit) {//BlMainPrint1()
            var href = '';
            if (debitOrCredit !== '') {
                href = '${path}/printConfig.do?screenName=CreditDebitNote&CreditDebitNotePrint=' + bolNo + '&fileNo=' + fileNo + "&noticeNo=" + noticeNo;
            } else {
                href = '${path}/printConfig.do?screenName=Corrections&bolNo=' + bolNo + '&fileNo=' + fileNo + "&noticeNo=" + noticeNo;
            }
            mywindow = window.open(href, '', 'width=800,height=500,scrollbars=yes');
            mywindow.moveTo(200, 180);
        }

        function freightInvoices(fileNo, bolNo, noticeNo, customerNumber) {
            var href = "${path}/printConfig.do?screenName=BL&action=CorrectedFreightInvoice&fileNo=" + fileNo + "&blId=" + bolNo + "&noticeNumber=" + noticeNo + "&cutomerNumber=" + customerNumber;
            mywindow = window.open(href, '', 'width=800,height=500,scrollbars=yes');
            mywindow.moveTo(200, 180);
        }

        function openNotes() {//BlMainPrint1()
            var href = '${path}/notes.do?moduleId=' + '<%=NotesConstants.MODULE_ID_CORRECTION%>&moduleRefId=' + '<%=fileNo1%>';
            //&actions=deletedCorrectionEventNotes
            mywindow = window.open(href, '', 'width=900,height=400,scrollbars=yes');
            mywindow.moveTo(200, 180);
        }

        function visibilityOfButtonsBasedOnUserRole() {
            evencodeEmpty();
            if (null !== document.getElementById("voidCorrectionDomain")) {
                var tableLength = document.getElementById("voidCorrectionDomain").rows.length;
                if (tableLength > 1) {
                    document.getElementById("viewVoidButton").className = "buttonColor";
                }
            }


        }
        function closeVoidPopUp() {
            closePopUp();
            document.getElementById('voidedCorrections').style.display = 'none';
        }
        function openVoidPopUp() {
            showPopUp();
            if (null !== document.getElementById('voidedCorrections')) {
                floatDiv("voidedCorrections", 100, document.body.offsetHeight / 4).floatIt();
            }
            document.getElementById('voidedCorrections').style.display = 'block';
        }
        document.oncontextmenu = mischandler;
        document.onmousedown = mousehandler;
        document.onmouseup = mousehandler;
    </script>
    <style type="text/css">
        #voidedCorrections{
            position:fixed;
            _position:absolute;
            border-style: solid solid solid solid;
            background-color: white;
            z-index:99;
            left:20%;
            top:50%;
            bottom:5%;
            right:5%;
            _height:expression(document.body.offset+"px");
        }
    </style>
    <%@include file="../includes/resources.jsp" %>
</head>


<div id="cover" style="width: 906px ;height: 1000px;"></div>
<body class="whitebackgrnd">
    <!--DESIGN FOR NEW ALERT BOX ---->
    <div id="AlertBox" class="alert">
        <p class="alertHeader"><b>Alert</b></p>
        <p id="innerText" class="containerForAlert">

        </p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button"  class="buttonStyleForAlert" value="OK"
                   onclick="document.getElementById('AlertBox').style.display = 'none';
                           grayOut(false, '');">
        </form>
    </div>
    <div id="ConfirmYesOrNo" class="alert">
        <p class="alertHeader"><b>Confirmation</b></p>
        <p id="innerText2" class="containerForAlert"></p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button"  class="buttonStyleForAlert" value="Yes"
                   onclick="confirmYes()">
            <input type="button"  class="buttonStyleForAlert" value="No"
                   onclick="confirmNo()">
        </form>
    </div>
    <!--// ALERT BOX DESIGN ENDS -->

    <html:form action="/fclBlCorrections?accessMode=${param.accessMode}" scope="request">
        <div id="voidedCorrections" style="display:none;width:700px;height:200px;">
            <table width="100%" class="tableBorderNew"><tr class="tableHeadingNew">
                    <td>
                        VOIDED Correction List FileNo:-- <font color="Red" size="2"><%=fileNo1%></font>
                    </td>
                    <td align="right">
                        <input type="button" value="Close" class="buttonStyleNew" onclick="closeVoidPopUp()"/>
                    </td></tr><tr><td colspan="2">
                    <display:table name="${voidedCorrectionList}" id="voidCorrectionDomain" pagesize="100"
                                   class="displaytagstyle" sort="list"  style="width:100%" defaultorder="descending"
                                   defaultsort="1">
                        <display:column title="CN #" property="noticeNo"/>
                        <display:column title="User"    property="userName"/>
                        <fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${voidCorrectionDomain.sailDate}"/>
                        <display:column title="SailDate"   > ${sailDate}</display:column>
                        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm a" var="correctionDate" value="${voidCorrectionDomain.date}"/>
                        <display:column title="NoticeDate"    > ${correctionDate}</display:column>
                        <display:column title="P" property="prepaidCollect"/>
                        <display:column  title="C/N Code" property="correctionCode.code"/>
                        <display:column title="C-Type" property="correctionType.code"/>
                        <display:column title="Who Voided" property="whoPosted"></display:column>
                        <fmt:formatDate pattern="dd-MMM-yyyy HH:mm a" var="voidedDate" value="${voidCorrectionDomain.postedDate}"/>
                        <display:column title="Voided Date">${voidedDate}</display:column>
                        <display:column title="Action">
                                <img src="${path}/img/icons/container_obj.gif" border="0" onmouseover="tooltip.show('view', null, event);" onmouseout="tooltip.hide();"
                                     onclick="viewCorrectionRecordDup('${voidCorrectionDomain.noticeNo}', '${voidCorrectionDomain.blNumber}','${voidCorrectionDomain.fileNo}')"/>
                            </display:column>
                        </display:table>
                    </td></tr></table>
        </div>
        <font color="Blue" size="3"><b>${msg}</b></font>

        <c:if test="${not empty displayBlNumber && not empty FclBlCorrectionList}">
            &nbsp; <span class="textlabelsBold">BL No:--<font color="Red" size="3" style="padding-left:5px;">
                <c:out value="${companyCode}"/><c:choose><c:when test="${importFlag=='false'}">-</c:when><c:otherwise> </c:otherwise></c:choose>${displayBlNumber}</font></span>
            </c:if>     
            <c:set var="manualNotesCount" value="buttonStyleNew"/>
            <c:if test="${ManualNotes}">
                <c:set var="manualNotesCount" value="buttonColor"/>
            </c:if>
            <c:choose>
                <c:when test="${empty FclBlCorrectionList}">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                    <tr class="tableHeadingNew">
                        <c:choose>
                            <c:when test="${empty FclBlCorrectionForm.temp}">
                                <td>Search Criteria</td>
                            </c:when>
                            <c:otherwise>
                                <td>Add New</td>
                                <td width="80%" align="right">
                                    <c:if test="${FclBlCorrectionForm.quickCn}">
                                        <input type="button" class="buttonStyleNew" value="Quick CN"  onclick="AddPage(true)"/>
                                    </c:if>
                                    <input type="button" class="buttonStyleNew" value="Add New"  onclick="AddPage(false)"/>
                                    <input type="button" class="buttonStyleNew" id="viewVoidButton"  name="search" value="View Void"
                                           onclick="openVoidPopUp()"/>
                                    <input type="button" class="${manualNotesCount}" id="note"  name="search" value="Notes"
                                           onclick="openNotes()"/>
                                    <input type="button" class="buttonStyleNew" value="Close"  onclick="closePage()"/>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    <c:choose>
                        <c:when test="${empty FclBlCorrectionForm.temp}">
                            <tr><td colspan="2">
                                    <table width="100%" border="0" cellpadding="2" cellspacing="0">
                                        <tr class="textlabelsBold">
                                            <td>File No</td>
                                            <td><html:text property="fileNo"  styleClass="textlabelsBoldForTextBox"
                                                       value="${FclBlCorrectionForm.fileNo}"/></td>
                                            <td>BL No</td>
                                            <%if (request.getAttribute("BlNumber") != null) { %>
                                            <td>
                                                <input name="blNumber"  class="textlabelsBoldForTextBox" id="blNumber" value="${BlNumber}"/>
                                            </td>
                                            <%} else {%>
                                            <td>
                                                <input name="blNumber"  class="textlabelsBoldForTextBox" id="blNumber" value="${FclBlCorrectionForm.blNumber}"/>
                                            </td>
                                            <%}%>
                                            <td>Correction Code</td>
                                            <td>
                                                <html:select property="correctionCode"
                                                             styleClass="dropdown_accounting" value="${FclBlCorrectionForm.correctionCode}">
                                                    <html:optionsCollection name="correctionCodeList"/>
                                                </html:select>
                                            </td>
                                            <td>Date</td>
                                            <td>
                                                <html:text property="date" styleClass="textlabelsBoldForTextBox" onchange="return validateDate(this)" value="${FclBlCorrectionForm.date}" styleId="txtcal"></html:text>
                                                <img src="${path}/img/CalendarIco.gif" alt="cal" width="16" height="16" align="top" id="cal"
                                                     onmousedown="insertDateFromCalendar(this.id, 0);" style="padding-top:2px;"/>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Shipper</td>
                                            <td>
                                                <input name="shipper" id="shipper"   class="textlabelsBoldForTextBox" value="${FclBlCorrectionForm.shipper}"/>
                                            </td>
                                            <td>Forwarder</td>
                                            <td>
                                                <input name="forwarder" id="forwarder"  class="textlabelsBoldForTextBox" value="${FclBlCorrectionForm.forwarder}"/>
                                            </td>
                                            <td>Third Party</td>
                                            <td>
                                                <input name="thirdParty" id="thirdParty"  class="textlabelsBoldForTextBox" value="${FclBlCorrectionForm.thirdParty}"/>
                                            </td>
                                            <td>Notice_#</td>
                                            <td><html:text property="noticeNo"  styleClass="textlabelsBoldForTextBox" value="${FclBlCorrectionForm.noticeNo}"></html:text></td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Origin</td>
                                                <td><input name="origin" id="origin" size="15"   maxlength="12" class="textlabelsBoldForTextBox"/>
                                                    <input name="origin_check" id="origin_check_id"  type="hidden">
                                                    <div id="origin_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                    <script type="text/javascript">
                                                        initAutocompleteWithFormClear("origin", "origin_choices", "", "origin_check",
                                                                "${path}/actions/getUnlocationCode.jsp?tabName=QUOTE&from=6&isDojo=false&countryflag=false", "");
                                                </script>
                                            </td>
                                            <td>POL</td>
                                            <td><input name="pol" id="pol" size="15"      maxlength="12" class="textlabelsBoldForTextBox"/>
                                                <input name="pol_check" id="pol_check_id"  type="hidden">
                                                <div id="pol_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("pol", "pol_choices", "", "pol_check",
                                                            "${path}/actions/getUnlocationCode.jsp?tabName=QUOTE&from=4&isDojo=false", "");
                                                </script>
                                                <br></td>
                                            <td>POD</td>
                                            <td><input name="pod" id="pod" size="15"    maxlength="12"
                                                       class="textlabelsBoldForTextBox"/>
                                                <input name="pod_check" id="pod_check_id"  type="hidden">
                                                <div id="pod_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("pod", "pod_choices", "", "pod_check",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=9&isDojo=false", "");
                                                </script>
                                                <br></td>
                                            <td>Destination</td>
                                            <td><input name="destination" id="destination" size="15"   maxlength="12" class="textlabelsBoldForTextBox"/>
                                                <input name="destination_check" id="destination_check_id"  type="hidden">
                                                <div id="destination_choices"  style="display: none;width: 5px;" class="autocomplete"></div>

                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("destination", "destination_choices", "", "destination_check",
                                                            "${path}/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=1&isDojo=false", "");
                                                </script>
                                                <br></td>
                                        </tr>
                                        <tr>
                                            <td class="textlabelsBold">Created By<br></td>
                                            <td class="textlabelsBold">
                                                <input type="text" name="createdBy"  id="createdBy"  style="width:110px;"
                                                       value="${FclBlCorrectionForm.createdBy}" class="textlabelsBoldForTextBox"/>
                                                <html:checkbox styleId="loginCheck"  property="loginCheck"
                                                               onclick="userNameSetToTextBox('created')"/>Me
                                                <div id="createdBy_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("createdBy", "createdBy_choices", "notInUse", "notInUse2",
                                                            "${path}/actions/getUserDetails.jsp?tabName=FCLBLCORRECTION&from=0&isDojo=false", "");
                                                </script>
                                                <input type="text" id="notInUse" style="display: none;"/>
                                                <input type="text" id="notInUse2" style="display: none;"/>
                                            </td>
                                            <td class="textlabelsBold">Approved By</td>
                                            <td class="textlabelsBold" >
                                                <input type="text" name="approvedBy"  id="approvedBy"
                                                       value="${FclBlCorrectionForm.approvedBy}"  style="width:110px;" class="textlabelsBoldForTextBox"/>
                                                <html:checkbox styleId="approvedByCheck" property="approvedByCheck"
                                                               onclick="userNameSetToTextBox('approved')"/>Me
                                                <div id="approvedBy_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("approvedBy", "approvedBy_choices", "notInUse3", "notInUse4",
                                                            "${path}/actions/getUserDetails.jsp?tabName=FCLBLCORRECTION&from=1&isDojo=false", "");
                                                </script>
                                                <input type="text" id="notInUse3" style="display: none;"/>
                                                <input type="text" id="notInUse4" style="display: none;"/>
                                                <br>
                                            </td>
                                            <td  class="textlabelsBold">Filter By</td>
                                            <td>
                                                <select name="filerBy"  style="width: 144px;" class="dropdown_accounting">
                                                    <option value="All">All</option>
                                                    <option value="Approved">Approved</option>
                                                    <option value="UnApproved">Not Approved</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>&nbsp;</td><td>&nbsp;</td>
                                            <%--<td>Include Posted</td>
                                                        <td><html:checkbox property="includePosted" value="${FclBlCorrectionForm.includePosted}"/></td>--%>
                                        </tr>
                                        <tr class="textlabelsBold" style="padding-top:60px;">
                                            <td>&nbsp;</td><td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td align="center">
                                                <input type="button" class="buttonStyleNew" value="Search" onclick="SearchCorrections()"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td></tr>

                        </c:when>
                    </c:choose>
                </table>

                <br style="padding-top:5px;">	
            </c:when>	
            <c:otherwise>
                <table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew ">
                    <tr class="tableHeadingNew">
                        <c:choose>
                            <c:when test="${empty FclBlCorrectionForm.temp}">
                            <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew " >
                                <tr class="tableHeadingNew">
                                    <td>
                                        <c:if test="${not empty FclBlCorrectionForm.blNumber}">
                                            <b class="textlabelsBoldForTextBox">BL Number-><c:out value="${FclBlCorrectionForm.blNumber}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.fileNo}">
                                            <b class="textlabelsBoldForTextBox">File Number-><c:out value="${FclBlCorrectionForm.fileNo}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.correctionCode}">
                                            <b class="textlabelsBoldForTextBox">C/N No-><c:out value="${FclBlCorrectionForm.correctionCode}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.noticeNo}">
                                            <b class="textlabelsBoldForTextBox">Notice_#-><c:out value="${FclBlCorrectionForm.noticeNo}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.date}">
                                            <b class="textlabelsBoldForTextBox">Date-><c:out value="${FclBlCorrectionForm.date}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.shipper}">
                                            <b class="textlabelsBoldForTextBox">Shipper-><c:out value="${FclBlCorrectionForm.shipper}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.forwarder}">
                                            <b class="textlabelsBoldForTextBox">Forwarder-><c:out value="${FclBlCorrectionForm.forwarder}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.thirdParty}">
                                            <b class="textlabelsBoldForTextBox">ThirdParty-><c:out value="${FclBlCorrectionForm.thirdParty}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.origin}">
                                            <b class="textlabelsBoldForTextBox">Origin-><c:out value="${FclBlCorrectionForm.origin}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.pol}">
                                            <b class="textlabelsBoldForTextBox">POL-><c:out value="${FclBlCorrectionForm.pol}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.pod}">
                                            <b class="textlabelsBoldForTextBox">POD-><c:out value="${FclBlCorrectionForm.pod}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty FclBlCorrectionForm.destination}">
                                            <b class="textlabelsBoldForTextBox">Destination-><c:out value="${FclBlCorrectionForm.destination}"></c:out></b>,
                                        </c:if>

                                    </td>
                                    <td align="right"><input type="button" class="buttonStyleNew" value="Search"  onclick="searchBlock()"/> </td>
                                </tr></table>


                        </c:when>
                        <c:otherwise>
                            <td>Add New</td>
                            <td width="80%" align="right">
                                <c:if test="${FclBlCorrectionForm.quickCn}">
                                    <input type="button" class="buttonStyleNew" value="Quick CN"  onclick="AddPage(true)"/>
                                </c:if>
                                <input type="button" class="buttonStyleNew" value="Add New"  onclick="AddPage(false)"/>
                                <input type="button" class="buttonStyleNew" id="viewVoidButton"  name="search" value="View Void"
                                       onclick="openVoidPopUp()"/>
                                <input type="button" class="${manualNotesCount}" id="noteButtonDown"  name="search" value="Notes"
                                       onclick="openNotes()"/>
                                <input type="button" class="buttonStyleNew" value="Close"  onclick="closePage()"/>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr><td colspan="2">
                        <c:set var="index" value="0"/>
                        <div  style="height:80%;">
                            <display:table name="${FclBlCorrectionList}" id="correctionDisplyTable" pagesize="100"
                                           class="displaytagstyle" sort="list"  style="width:100%" defaultorder="descending"
                                           defaultsort="1" requestURI="/fclBlCorrections.do?">
                                <display:setProperty name="paging.banner.some_items_found">
                                    <span class="pagebanner">
                                        <font color="blue">{0}</font> Search Quotation details displayed,For more code click on page numbers.
                                    </span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.one_item_found">
                                    <span class="pagebanner">
                                        One {0} displayed. Page Number
                                    </span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.all_items_found">
                                    <span class="pagebanner">
                                        {0} {1} Displayed, Page Number
                                    </span>
                                </display:setProperty>
                                <display:setProperty name="basic.msg.empty_list">
                                    <span class="pagebanner">
                                        No Records Found.
                                    </span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.placement" value="bottom"/>
                                <display:setProperty name="paging.banner.item_name" value="Corrections"/>
                                <display:setProperty name="paging.banner.items_name" value="Corrections"/>
                                <display:column title="" paramName="id" property="id" style="visibility:hidden"/>
                                <c:choose>
                                    <c:when test="${empty FclBlCorrectionForm.temp}">
                                        <display:column title="FileNo"  >
                                            <span style="color:blue;cursor: pointer;text-decoration: underline" onclick="checkLockAndNavigateToBl('${correctionDisplyTable.fileNo}', 'blId=${correctionDisplyTable.bolId}&bookingId=${correctionDisplyTable.bookingId}&quoteId=${correctionDisplyTable.quoteId}&fileNumber=${correctionDisplyTable.fileNo}', 'FCLBL')">
                                                ${correctionDisplyTable.fileNo}
                                            </span>
                                        </display:column>
                                        <display:column title="BL No" property="blNumber" ></display:column>
                                    </c:when>
                                </c:choose>
                                <display:column title="CN #" property="noticeNo"  ></display:column>
                                <display:column title="User"    property="userName"  ></display:column>
                                <display:column title="P" property="prepaidCollect"  ></display:column>
                                <display:column title="Sail Date">
                                    <fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${correctionDisplyTable.sailDate}"/>
                                    ${sailDate}
                                </display:column>
                                <display:column title="Notice Date" >
                                    <fmt:formatDate pattern="dd-MMM-yyyy HH:mm a" var="correctionDate" value="${correctionDisplyTable.date}"/>
                                    ${correctionDate}
                                </display:column>
                                <c:choose>
                                    <c:when test="${correctionDisplyTable.currentProfit eq null or correctionDisplyTable.currentProfit eq 0.0}">
                                        <c:set var="currentPro" value="0.00"></c:set>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="currentPro" value="${correctionDisplyTable.currentProfit}"></c:set>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${correctionDisplyTable.profitAfterCn eq null or correctionDisplyTable.profitAfterCn eq 0.0}">
                                        <c:set var="profitAftrCn" value="0.00"></c:set>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="profitAftrCn" value="${correctionDisplyTable.profitAfterCn}"></c:set>
                                    </c:otherwise>
                                </c:choose>
                                <display:column  title="Current Profit" >${currentPro}</display:column>
                                <display:column  title="Profit After C/N" >${profitAftrCn}</display:column>
                                <display:column  title="C/N Code" property="correctionCode.code"/>
                                <display:column title="Approval" property="approval" style="color:Green;font-weight: bolder;"/>
                                <display:column title="F" property="isFax"></display:column>
                                <display:column title="P" property="isPost"></display:column>
                                <display:column title="C-Type" property="correctionType.code"/>
                                <display:column title="Actions">
                                    <c:choose>
                                        <c:when test="${not empty FclBlCorrectionForm.temp}">
                                            <display:column title="Who Posted" property="whoPosted"></display:column>
                                            <display:column title="Posted Date">
                                                <fmt:formatDate pattern="dd-MMM-yyyy HH:mm a" var="postedDate" value="${correctionDisplyTable.postedDate}"/>
                                                ${postedDate}
                                            </display:column>
                                            <display:column title="Posted">
                                                <c:choose>
                                                    <c:when test="${not empty correctionDisplyTable.manifest}">
                                                        <font color="red"><b>Yes</b></font>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <font color="red"><b>No</b></font>
                                                    </c:otherwise>
                                                </c:choose>
                                            </display:column>
                                            <c:choose>
                                                <c:when test="${correctionDisplyTable.status != 'Approved' && correctionDisplyTable.status != 'Disable'}">
                                                    <img src="${path}/img/icons/edit.gif" border="0" onclick="editCorrectionRecord('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}', '','${correctionDisplyTable.fileNo}')"
                                                         onmouseover="tooltip.show('Edit', null, event);" onmouseout="tooltip.hide();"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${path}/img/icons/container_obj.gif" border="0" onmouseover="tooltip.show('view', null, event);" onmouseout="tooltip.hide();"
                                                         onclick="viewCorrectionRecordDup('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}','${correctionDisplyTable.fileNo}')"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${empty correctionDisplyTable.status}">
                                                <c:choose>
                                                    <c:when test="${loginuser.loginName==correctionDisplyTable.userName}">
                                                        <img src="${path}/img/icons/delete.gif"   border="0"  onmouseover="tooltip.show('Delete', null, event);" onmouseout="tooltip.hide();"
                                                             onclick="deleteBlCorrection('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}', '${correctionDisplyTable.fileNo}')" />
                                                    </c:when>
                                                    <c:when test="${roleDuty.postCorrections}">
                                                        <img src="${path}/img/icons/delete.gif"   border="0"  onmouseover="tooltip.show('Delete', null, event);" onmouseout="tooltip.hide();"
                                                             onclick="deleteBlCorrection('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}', '${correctionDisplyTable.fileNo}')" />
                                                    </c:when>
                                                </c:choose>
                                            </c:if>
                                            <c:if test="${correctionDisplyTable.status == 'Disable'}">
                                                <img src="${path}/img/icons/lockon.ico"   border="0"   onmouseover="tooltip.show('Delete', null, event);" onmouseout="tooltip.hide();"/>
                                            </c:if>
                                            <c:if test="${roleDuty.accessCorrectionPrintFax && not empty correctionDisplyTable.status && not empty correctionDisplyTable.manifest}">
                                                <c:set var="query" value="select if(send_debit_credit_notes = 'Y', 'true', 'false') as result"/>
                                                <c:set var="query" value="${query} from cust_accounting"/>
                                                <c:set var="query" value="${query} where acct_no = '${correctionDisplyTable.accountNumber}'"/>
                                                <c:set var="result" value="${dao:getUniqueResult(query)}"></c:set>
                                                <c:choose>
                                                    <c:when test="${result == 'true'}">
                                                        <img src="${path}/img/icons/dbcr.jpg" border="0" onmouseover="tooltip.show('Credit/Debit Note', null, event);" id="email${index}"
                                                             onmouseout="tooltip.hide();" onclick="PrintReportsOpenSeprately('${correctionDisplyTable.fileNo}', '${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.id}', '${correctionDisplyTable.blNumber}', 'debitorCredit')"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img title="Corrected Freight Invoices" src="${path}/images/icons/preview.png" 
                                                             onclick="freightInvoices('${correctionDisplyTable.fileNo}', '${correctionDisplyTable.blNumber}', '${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.accountNumber}');"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:when>
                                        <%-- this else part wil display on search page of correction pool--%>
                                        <c:otherwise>
                                            <c:if test="${empty correctionDisplyTable.status && roleDuty.postCorrections}">
                                                <img src="${path}/img/icons/edit.gif" border="0" onclick="editCorrectionRecord('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}', '','${correctionDisplyTable.fileNo}')"
                                                     onmouseover="tooltip.show('Edit', null, event);" onmouseout="tooltip.hide();"/>
                                            </c:if>
                                            <img src="${path}/img/icons/container_obj.gif" border="0" id="view${index}"
                                                 onclick="viewCorrectionRecordDup('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}','${correctionDisplyTable.fileNo}')" onmouseover="tooltip.show('view', null, event);" onmouseout="tooltip.hide();"/>
                                            <c:if test="${empty correctionDisplyTable.manifest}">
                                                <c:choose>
                                                    <c:when test="${empty correctionDisplyTable.status}">
                                                        <c:choose>
                                                            <c:when test="${roleDuty.postCorrections}">
                                                                <img src="${path}/img/icons/pa.gif" border="0" id="approve${index}"
                                                                     onclick="approveUse('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}', '${correctionDisplyTable.fileNo}')" onmouseover="tooltip.show('Pending Approval', null, event);" onmouseout="tooltip.hide();"/>
                                                            </c:when>
                                                            <c:when test="${loginuser.loginName==correctionDisplyTable.userName}">
                                                                <img src="${path}/img/icons/delete.gif" border="0" onmouseover="tooltip.show('Delete', null, event);" onmouseout="tooltip.hide();"
                                                                     onclick="deleteBlCorrection('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}', '${correctionDisplyTable.fileNo}')"/>
                                                            </c:when>
                                                            <c:when test="${roleDuty.postCorrections}">
                                                                <img src="${path}/img/icons/delete.gif" border="0" onmouseover="tooltip.show('Delete', null, event);" onmouseout="tooltip.hide();"
                                                                     onclick="deleteBlCorrection('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}', '${correctionDisplyTable.fileNo}')"/>
                                                            </c:when>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${correctionDisplyTable.status == 'Disable'}">
                                                                <img src="${path}/img/icons/lockon.ico"   border="0"   onmouseover="tooltip.show('Disabled', null, event);" onmouseout="tooltip.hide();"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:if test="${roleDuty.postCorrections}">
                                                                    <img src="${path}/img/icons/unapp.gif" border="0" id="disapprove${index}"
                                                                         onclick="DisApproveUse('${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.blNumber}', '${correctionDisplyTable.fileNo}')"  onmouseover="tooltip.show('Un Approve', null, event);" onmouseout="tooltip.hide();"/>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <c:if test="${roleDuty.accessCorrectionPrintFax && not empty correctionDisplyTable.status && not empty correctionDisplyTable.manifest}">
                                                <c:set var="query" value="select if(send_debit_credit_notes = 'Y', 'true', 'false') as result"/>
                                                <c:set var="query" value="${query} from cust_accounting"/>
                                                <c:set var="query" value="${query} where acct_no = '${correctionDisplyTable.accountNumber}'"/>
                                                <c:set var="result" value="${dao:getUniqueResult(query)}"></c:set>
                                                <c:choose>
                                                    <c:when test="${result == 'true'}">
                                                        <img src="${path}/img/icons/dbcr.jpg" border="0" onmouseover="tooltip.show('Credit/Debit Note', null, event);" id="email${index}"
                                                             onmouseout="tooltip.hide();" onclick="PrintReportsOpenSeprately('${correctionDisplyTable.fileNo}', '${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.id}', '${correctionDisplyTable.blNumber}', 'debitorCredit')"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img title="Corrected Freight Invoices" src="${path}/images/icons/preview.png"
                                                             onclick="freightInvoices('${correctionDisplyTable.fileNo}', '${correctionDisplyTable.blNumber}', '${correctionDisplyTable.noticeNo}', '${correctionDisplyTable.accountNumber}');"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>


                                </display:column>
                                <c:set var="index" value="${index+1}"/>
                            </display:table>
                        </div>
                    </td></tr>
            </table>
        </c:otherwise>
    </c:choose>
    <html:hidden property="buttonValue"/>  
    <html:hidden property="index1"/>
    <html:hidden property="blNumber" value="${param.blNumber}"/>	
    <html:hidden property="temp" value="${FclBlCorrectionForm.temp}"/>
    <html:hidden property="fileNo" value="${param.fileNo}"/> 	

    <html:hidden property="eventCode" styleId="eventCode"/>
    <html:hidden property="eventDesc" styleId="eventDesc"/>
    <html:hidden property="moduleId" value="<%=NotesConstants.MODULE_ID_CORRECTION%>"/>
    <html:hidden property="moduleRefId" styleId="moduleRefId"/>

    <html:hidden property="approval"/>	
    <html:hidden property="viewMode"/>	
    <html:hidden property="id"/>
    <html:hidden property="noticeNo"/>

    <html:hidden property="testBoxBlNumber"/>	
    <html:hidden property="testBoxNoticeNumber"/>
    <html:hidden property="quickCn" styleId="quickCn" value="${FclBlCorrectionForm.quickCn}"/>
    <html:hidden property="importFlag" styleId="importFlag" value="${importFlag}"/>

</html:form>
</body>
<c:if test="${action=='postQuickCn'}">
    <script type="text/javascript">
        function afterPostQuickCn() {
            window.parent.parent.GB_hide();
            var url = "blId=${blId}&bookingId=${bookingId}&quoteId=${quoteId}&fileNumber=${fileNumber}";
            window.parent.parent.parent.changeChilds(url, "${fileNumber}", "FCLBL");
        }
        setTimeout('afterPostQuickCn()', 1000)
    </script>
</c:if>
</html>

