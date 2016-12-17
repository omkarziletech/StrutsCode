<%@page import="com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@ taglib uri="/WEB-INF/taglibs-unstandard.tld" prefix="uns"%>
<%@taglib uri="/WEB-INF/tlds/dao.tld" prefix="dao"%>
<html>
    <head>
        <title>FCL BL</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <link rel="stylesheet" href="${path}/css/jquery-tabs/jquery-ui-1.8.10.custom.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="${path}/css/default/style.css" type="text/css" media="print, projection, screen" />
        <link rel="alternate stylesheet" type="text/css" media="all" href="${path}/css/cal/calendar-win2k-cold-2.css"  title="win2k-cold-2" />
        <link rel="stylesheet" type="text/css" href="${path}/css/cal/skins/aqua/theme.css" title="Aqua" />
        <script language="javascript" src="${path}/js/common.js"></script>
        <script language="javascript" src="${path}/js/fclBillLanding.js"></script>
        <script language="javascript" src="${path}/js/fcl/fclBl.js"></script>
        <script language="javascript" src="${path}/js/fcl/fclDojo.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script language="javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script src="${path}/js/jquery/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <script language="javascript">
            var insuranceAllowed = "${fclBlForm.insuranceAllowed}"
            start = function() {
                setFocusOnTab('${fclBlForm.selectedTab}');
                getSslBlCollect(${roleDuty.sslPrepaidCollect});
                getBillTOCode();
                disableBillToCodeonLoad();
                changeSelectBoxOnViewMode();
                loadData();
                makeFclBlCorrectionButtonRed();
                makeARInvoiceButtonGreen('${fclBlForm.fclBl.fileNo}','${fclBlForm.fclBl.voyageInternal}');
                disabledFClBL('${view}', '${fclBlForm.fclBl.readyToPost}', '${fclBlForm.fclBl.blClosed}', '${param.accessMode}', '${roleDuty.allowRoutedAgent}');
                setTabName('${fclBlForm.selectedTab}');
                serializeForm();
                modifyAccountDetails();
                closePreloading();
                hideAddAccrual();
                showBrandValuesFromBooking('${fclBlForm.fclBl.brand}');
                
            }
            window.onload = start;
        </script>
        <style>
            .info-box
            {
                border: 1px solid #C4C5C4;float: left;color: #000000;padding:1px;
                margin: 2px;
            }

            .pagelinks{
                float:left;
            }
        </style>
        <%
             String companyCodeValue = new SystemRulesDAO().getSystemRulesByCode("CompanyCode");
            request.setAttribute("companyCodeValue", companyCodeValue);
         %>
    </head>
    <body>
        <%@include file="../../../../jsps/preloader.jsp"%>
         <uns:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <div id="cover" style="width: 906px ;height: 1000px;"></div>
        <div id="bubble_tooltip_forComments" style="display: none;">
            <div class="bubble_top_forComments"><span></span></div>
            <div class="bubble_middle_forComments"><span id="bubble_tooltip_content_forComments"></span></div>
            <div class="bubble_bottom_forComments"></div></div>
        <div id="commentDiv"   class="comments">
            <table border="1" id="commentTableInfo">
                <tbody border="0"></tbody>
            </table>
        </div>
        <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
            <p class="progressBarHeader" style="width: 100%;padding-left: 45px;"><b>Processing......Please Wait</b></p>
            <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
                <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
            </form>
        </div>

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
        <div id="AlertBoxOk" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText3" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="confirmFunction()">
            </form>
        </div>
        <div id="AlertOk" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText4" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="confirmationFunction()">
            </form>
        </div>
        <div id="ConfirmBox" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" id="confirmYes"
                       onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel" id="confirmNo"
                       onclick="No()">
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
        <div id="ConfirmYesNoCancelDiv" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="confirmMessagePara" class="containerForAlert">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmOptionYes()">
                <input type="button" id="confirmNo"  class="buttonStyleForAlert" value="No"
                       onclick="confirmOptionNo()">
                <input type="button" id="confirmCancel"  class="buttonStyleForAlert" value="Cancel"
                       onclick="confirmOptionCancel()">
            </form>
        </div>
        <div id="reminderBox" class="alert">
            <p class="alertHeader"><b>REMINDER:</b></p>
            <p id="innerTextReminder" class="containerForAlert">
            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button" id="confirmYes"  class="buttonStyleForAlert" value="OK"
                       onclick="okForReminderExport()">
            </form>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->
        <html:form action="/fclBlNew" styleId="fclBl" style="padding:10px;float:left;" scope="request">
            <b style="margin-left:200px;color: #000080;font-size: 15px;">${lockRecord}</b>
            <div id="confirmOnBoard" style="display:none;width:750px;height:200px;">
                <table class="tableBorderNew" cellpadding="2" cellspacing="0"  width="100%">
                    <tr class="tableHeadingNew"><font style="font-weight: bold">Confirm On Board</font></tr>
                    <tr class="textlabelsBold">
                        <td>
                            <table>
                                <tr>
                                    <td class="textlabelsBold" style=" border-left: red 2px solid;">Confirm on Board </td>
                                </tr>
                            </table>
                        </td>

                        <td>Y<html:radio property="fclBl.confirmOnBoard" name="fclBlForm" value="Y"
                                    styleId="confirmOnBoardYes" onclick="checkConfirmOnBoard()"/>&nbsp;&nbsp;
                            N<html:radio property="fclBl.confirmOnBoard" name="fclBlForm" value="N"
                                        styleId="confirmOnBoardNo" onclick="checkConfirmOnBoard()"/></td>
                        <td>ETD</td>
                        <td>
                            <fmt:formatDate pattern="MM/dd/yyyy" var="sailDate"  value="${fclBlForm.fclBl.sailDate}"/>
                            <input size="10" class="BackgrndColorForTextBox"  readonly="readonly"  tabindex="-1" value="${sailDate}" id="etdValue"  />
                        </td>
                        <td>ETA</td>
                        <td>
                            <fmt:formatDate pattern="MM/dd/yyyy" var="eta" value="${fclBlForm.fclBl.eta}"/>
                            <input size="10" class="BackgrndColorForTextBox" readonly="readonly"  tabindex="-1" value="${eta}" id="etaValue"  />
                        </td>
                        <td>Verified ETA</td>
                        <td style="padding-bottom:10px;">
                            <fmt:formatDate pattern="MM/dd/yyyy" var="verEta" value="${fclBlForm.fclBl.verfiyETA}" />
                            <c:choose>
                                <c:when test="${fclBlForm.fclBl.confirmOnBoard == 'Y'}">
                                    <html:text styleClass="textlabelsBoldForTextBox mandatroy"  property="verfiyETA" value="${verEta}"
                                               styleId="txtcal4" size="18"  onchange="validateETA(this)" />
                                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="cal4" name="VerfiedETADate"
                                         onmousedown="insertDateFromCalendar(this.id, 0);"  />
                                    <input type="checkbox" name="verifiedEtaCheck" id="verifiedEtaCheck" onclick="copyVerifyEtaDate()" onmouseover="tooltip.show('<strong>Same as ETA</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                </c:when>
                                <c:otherwise>
                                    <html:text styleClass="textlabelsBoldForTextBox"  property="verfiyETA" value="${verEta}"
                                               styleId="txtcal4" size="18"  onchange="validateETA(this)" />
                                    <img src="${path}/img/CalendarIco.gif" alt="cal" id="cal4" name="VerfiedETADate"
                                         onmousedown="insertDateFromCalendar(this.id, 0);" style="visibility: hidden" />
                                    <input type="checkbox" name="verifiedEtaCheck" id="verifiedEtaCheck" disabled="true" onclick="copyVerifyEtaDate()" onmouseover="tooltip.show('<strong>Same as ETA</strong>', null, event);" onmouseout="tooltip.hide();"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold" align="left" >Vessel Name</td>
                        <td colspan="3">
                            <input size="30" class="BackgrndColorForTextBox" value="${fclBlForm.fclBl.vessel.codedesc}" readonly="readonly"  tabindex="-1"/></td>
                        <td class="textlabelsBold"  align="right">Voyage</td>
                        <td colspan="3">
                            <input size="25" class="BackgrndColorForTextBox" value="${fclBlForm.fclBl.voyages}" readonly="readonly"  tabindex="-1"/>
                        </td>
                        <td>
                        </td><td></td><td></td><td></td>
                    </tr>
                    <tr><td colspan="9" align="left" >
                            <table>
                                <tr class="textlabelsBold">
                                    <td valign="top">Comment</td>
                                    <td style="padding-left: 40px;">
                                        <c:choose>
                                            <c:when test="${fclBlForm.fclBl.confirmOnBoard == 'Y'}">
                                                <html:textarea property="fclBl.confOnboardComments"  styleClass="textlabelsBoldForTextBox" styleId="confOnBoardComments"
                                                               value="${fclBlForm.fclBl.confOnboardComments}" rows="3" cols="31" onkeydown="maxLength(this,200)"
                                                               style="text-transform: uppercase">
                                                </html:textarea>
                                            </c:when>
                                            <c:otherwise>
                                                <html:textarea property="fclBl.confOnboardComments"  styleClass="textlabelsBoldForTextBox" styleId="confOnBoardComments"
                                                               value="${fclBlForm.fclBl.confOnboardComments}" rows="3" cols="31" onkeydown="maxLength(this,200)"
                                                               style="text-transform: uppercase" disabled="true">
                                                </html:textarea>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>
                        </td></tr>
                    <tr>
                        <td colspan="9" align="center" >
                            <input type="button" class="buttonStyleNew" value="Save"
                                   onclick="saveConfirmOnBoard('${loginuser.loginName}')" />

                            <input class="buttonStyleNew" type="button" id="manifestCancel" value="Cancel"
                                   onclick="closeConfirmOnBoard()">
                        </td>
                    </tr>
                </table>
            </div>
            <input type="hidden" id="houseBL" value="${fclBlForm.fclBl.houseBL}"/>
            <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="41a390f7a1077ae74371625475760a7a"/>
            <html:hidden property="methodName" styleId="methodName"/>
            <html:hidden property="fclBl.fileNo" styleId="fileNo"/>
            <html:hidden property="fclBl.fileNumber" styleId="fileNumber"/>
            <html:hidden property="fclBl.bolId" styleId="bolId"/>
            <html:hidden property="fclBl.blBy"/>
            <html:hidden property="fclBl.receivedMaster" styleId="receivedMaster"/>
            <html:hidden property="bol"/>
            <html:hidden property="fclBl.overPaidStatus"/>
            <html:hidden property="fclBl.convertedToAp"/>
            <html:hidden property="fclBl.bol" styleId="bol"/>
            <html:hidden property="fclBl.ratesNonRates" styleId="ratesNonRates"/>
            <html:hidden property="bolDate"/>
            <html:hidden property="quoteDate"/>
            <html:hidden property="quoteBy"/>
            <html:hidden property="bookedDate"/>
            <html:hidden property="bookedBy"/>
            <html:hidden property="manifestedDate"/>
            <html:hidden property="manifestedBy"/>
            <html:hidden property="blClosed"/>
            <html:hidden property="closedDate" />
            <html:hidden property="ediCreatedOn" styleId="ediCreatedOn"/>
            <html:hidden property="fclBl.ediCreatedBy" styleId="ediCreatedBy"/>
            <html:hidden property="closedBy" styleId="blClosedBy"/>
            <html:hidden property="blAudited"/>
            <html:hidden property="auditedDate"/>
            <html:hidden property="confirmOn" styleId="confirmOn"/>
            <html:hidden property="confirmBy" styleId="confirmBy"/>
            <html:hidden property="auditedBy" styleId="blAuditedBy"/>
            <html:hidden property="fclBl.costOfGoods" styleId="costOfGoods"/>
            <html:hidden property="fclBl.hazmat" styleId="hazmat"/>
            <html:hidden property="fclBl.billThirdPartyAddress" styleId="billThirdPartyAddress"/>
            <html:hidden property="fclBl.readyToPost" styleId="manifest"/>
            <html:hidden property="fclBl.insuranceRate" styleId="insuranceRate"/>
            <html:hidden property="fclBl.insurance" styleId="insurance"/>
            <html:hidden property="fclBl.updateBy" styleId="updateBy"/>
            <html:hidden property="action" styleId="action"/>
            <html:hidden property="selectedTab" styleId="selectedTab"/>
            <html:hidden property="selectedId" styleId="selectedId"/>
            <html:hidden property="size" styleId="size"/>
            <html:hidden property="fclBl.receivedMaster" styleId="receivedMaster"/>
            <html:hidden property="fclBl.inttra" styleId="inttraGtnexus"/>
            <html:hidden property="fclSsblGoCollect" styleId="fclSsblGoCollect"/>
            <input type="hidden" name="masterScanStatus" id="masterScanStatus" value="${MasterStatus}"/>
            <input type="hidden" name="userName" id="userName" value="${loginuser.loginName}"/>
            <html:hidden property="fclBl.importFlag" styleId="importFlag"/>
            <html:hidden property="eventCode" styleId="eventCode"/>
            <html:hidden property="eventDesc" styleId="eventDesc"/>
            <html:hidden property="moduleId" value="FILE"/>
            <html:hidden property="fclBl.manifestRev"/>
            <html:hidden property="fclBl.localDrayage"/>
            <html:hidden property="fclBl.autoDeductFfcomm"/>
            <html:hidden property="moduleRefId" value="${fclBlForm.fclBl.fileNo}"/>
             <html:hidden property="iIconBillToolTip" styleId="iIconBillToolTip" value="${fclBlForm.iIconBillToolTip}"/>
             <input type="hidden" id="companyCode" value="${companyCodeValue}"/>
             <input type="hidden" id="importFlag1" value="${fclBlForm.fclBl.importFlag}"/>
            <c:if test="${not empty lockRecord}">
                <b style="margin-left:200px;color: #000080;font-size: 15px;"></b>
            </c:if>
            <c:if test="${fclBlForm.fclBl.localDrayage == 'Y'}">
                <b style="color: #FF4A4A;font-size: 15px;">(Local Drayage Included)</b>
            </c:if>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table border="0">
                            <tr>
                                <td>
                                    <table>
                                        <tbody>
                                            <tr class="textlabelsBold">
                                                <td id="fileNo">File BOL No :<font size="4" color="#ff4a4a">${companyCode}-${fclBlForm.fclBl.bolId}</font></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </td>
                                <td>

                                    <div style="float:left">
                                        <c:if test="${fclBlForm.fclBl.readyToPost=='M'}">
                                            <font color="green" size="4"><b id="mes">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Manifested,</b></font>
                                        </c:if>
                                        <c:if test="${not empty fclBlForm.closedBy}">
                                            <font color="green" size="4"><b >Closed,</b></font>
                                        </c:if>
                                        <c:if test="${not empty fclBlForm.auditedBy}">
                                            <font color="green" size="4"><b >Audited,</b></font>
                                        </c:if>
                                    </div>
                                    <div style="float:left">
                                        <c:choose>
                                            <c:when test="${(null!=fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag=='I') && fclBlForm.fclBl.overPaidStatus}">
                                                <c:set var="overPaid" value="Over Paid"/>
                                                <input type="text" value="${overPaid}," readonly="true" style="display: block;color: green;float:left;border: 0;font-size: 18px;width:100px;font-weight: bold"  id="paidStatus"/>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" value="${overPaid}," readonly="true" style="display: none"  id="paidStatus"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>

                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <c:if test="${fclBlForm.fclBl.hazmat=='Y'}">
                                                <td><font color="red" size="2"><b>HAZARDOUS CARGO</b></font>
                                                </td>
                                            </c:if>
                                            <c:if test="${fclBlForm.isBulletRate eq true}">
                                                <td colspan="6" align="center"  style="font-size:large;font-weight: bolder;color: #fd0000"> BULLET RATES </td>
                                            </c:if>
                                            <c:if test="${fclBlForm.fclBl.spotRate eq 'Y'}">
                                                <td></td><td colspan="6" align="right"  style="font-size:large;font-weight: bolder;color: #fd0000"> **SPOT/BULLET RATE** </td>
                                            </c:if>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>
                                                    <c:if test="${fclBlForm.fclBl.readyToPost!='M'}">
                                                        <font color="blue" size="2"><b>${msg}</b></font>
                                                    </c:if>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <c:choose>
                                    <c:when test="${fclBlForm.fclBl.ratesNonRates == 'N'}">
                                        <td align="right"  style="padding-left:250px;" class="textlabelsBold">
                                            Non-Rated<input type="radio" id="nonRated" checked  disabled="true"/>&nbsp;&nbsp;
                                            Break Bulk
                                            <html:radio property="fclBl.breakBulk" value="Y" name="fclBlForm" styleId="breakBulkY"  disabled="true"/>Y
                                            <html:radio property="fclBl.breakBulk" value="N" name="fclBlForm" styleId="breakBulkN"  disabled="true"/>N
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td align="right"  style="padding-left:350px;" class="textlabelsBold">
                                            <span id="rateId">Rated<input type="radio" id="rated" checked disabled="true"/></span>&nbsp;&nbsp;
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                                        <td align="right" class="textlabelsBold">
                                            Brand
                                            <c:choose>
                                                <c:when test="${companyCodeValue == '03'}">
                                                    <html:radio property="fclBl.brand" value="Econo" name="fclBlForm" styleId="brandEcono" onclick="checkBrand('Econo','${fclBlForm.fclBl.bolId}','${companyCodeValue}')"/>Econo
                                                </c:when>
                                                <c:otherwise>
                                                    <html:radio property="fclBl.brand" value="OTI"  name="fclBlForm" styleId="brandOti" onclick="checkBrand('OTI','${fclBlForm.fclBl.bolId}','${companyCodeValue}')"/>OTI
                                                </c:otherwise>
                                            </c:choose>
                                            <html:radio property="fclBl.brand" value="${commonConstants.ECU_Worldwide}" name="fclBlForm" styleId="brandEcuworldwide" onclick="checkBrand('Ecu Worldwide','${fclBlForm.fclBl.bolId}','${companyCodeValue}')"/>Ecu Worldwide

                                        </td> 
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="info-box">
                            <b class="textlabelsBold">Quote By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fn:toUpperCase(fclBlForm.quoteBy)}" /></b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fclBlForm.quoteDate}" /></b>&nbsp
                        </div>
                        <div class="info-box">
                            <b class="textlabelsBold">Booking By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fn:toUpperCase(fclBlForm.bookedBy)}" /></b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fclBlForm.bookedDate}" /></b>&nbsp
                        </div>
                        <div class="info-box">
                            <b class="textlabelsBold">BL By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fn:toUpperCase(fclBlForm.fclBl.blBy)}" /></b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fclBlForm.bolDate}" /></b>&nbsp
                        </div>
                        <div class="info-box">
                            <b class="textlabelsBold">Manifested By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue">
                                ${fn:toUpperCase(fclBlForm.manifestedBy)}
                            </b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue" >${fclBlForm.manifestedDate}</b>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="info-box">
                            <b class="textlabelsBold">EDI By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue" id="ediby">${fn:toUpperCase(fclBlForm.fclBl.ediCreatedBy)}</b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue" id="edion">${fclBlForm.ediCreatedOn}</b>&nbsp
                        </div>
                        <div class="info-box">
                            <b class="textlabelsBold">Confirm on board By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fn:toUpperCase(fclBlForm.confirmBy)}" /></b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fclBlForm.confirmOn}" /></b>&nbsp
                        </div>
                        <div class="info-box">
                            <b class="textlabelsBold">Closed By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fn:toUpperCase(fclBlForm.closedBy)}" /></b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fclBlForm.closedDate}" /></b>&nbsp
                        </div>
                        <div class="info-box">
                            <b class="textlabelsBold">Audited By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fn:toUpperCase(fclBlForm.auditedBy)}" /></b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fclBlForm.auditedDate}" /></b>&nbsp
                        </div>
                        <div class="info-box">
                            <b class="textlabelsBold">Received Master By :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fn:toUpperCase(fclBlForm.fclBl.receivedMasterby)}" /></b>&nbsp
                            <b class="textlabelsBold">On :</b>&nbsp;
                            <b class="headerlabel" style="color:blue"><c:out value="${fclBlForm.receivedMasterdate}" /></b>&nbsp
                        </div>
                          <div class="info-box">
                                <b class="textlabelsBold">AR Balance :</b>&nbsp;
                                <c:set var="fileNo" value="${fclBlForm.fclBl.fileNo}"/> 
                                <c:choose>
                                    <c:when test="${!fn:contains(fclBlForm.fclBl.fileNo,'-')}">
                                         <c:set var="query" value="select format(coalesce(sum(balance), 0.00), 2) as balance from transaction where"/>
                                        <c:set var="query" value="${query} transaction_type = 'AR'"/>
                                        <c:set var="query" value="${query} and drcpt='${fclBlForm.fclBl.fileNo}'"/>
                                        <c:set var="result" value="${dao:getUniqueResult(query)}"></c:set>
                                        <b class="headerlabel" style="color:blue"> ${result}</b>&nbsp;
                                        <img src="${path}/images/icons/currency_blue.png" onmouseover="tooltip.show('<strong>Show Transactions History</strong>', null, event);"
                                             onmouseout="tooltip.hide();"align="top" onclick="showTransactions('${path}', '${fclBlForm.fclBl.importFlag}', '${fileNo}');"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="query" value="select format(coalesce(sum(balance), 0.00), 2) as balance from transaction where"/>
                                        <c:set var="query" value="${query} transaction_type = 'AR'"/>
                                        <c:set var="query" value="${query} and bill_ladding_no='${fclBlForm.fclBl.bolId}'"/>
                                        <c:set var="result" value="${dao:getUniqueResult(query)}"></c:set>
                                        <b class="headerlabel" style="color:blue"> ${result}</b>&nbsp; 
                                        <img src="${path}/images/icons/currency_blue.png" onmouseover="tooltip.show('<strong>Show Transactions History</strong>', null, event);"
                                             onmouseout="tooltip.hide();"align="top" onclick="showTransactions('${path}', '${fclBlForm.fclBl.importFlag}', '${fileNo}');"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <%@include file="../FCL/headerDiv.jsp"%>
                    </td>
                <tr>
            </table>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <table  border="0" width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td valign="top" style="border: 1px solid #C4C5C4;">
                            <table border="0"   width="100%" cellpadding="2" cellspacing="0" >
                                <tr class="tableHeadingNew"><td colspan="4">General Information</td></tr>
                                <tr class="textlabelsBold">
                                    <c:choose>
                                        <c:when test="${fclBlForm.fclBl.importFlag !='I'}">
                                            <td align="right">
                                                SSL BL Prepaid/Collect
                                            </td>
                                            <td align="left">
                                                <input type="hidden" id="sslPrepaidCollectRoleDuty" value="${roleDuty.sslPrepaidCollect}"/>
                                                <input type="hidden" id="sslPrepaidCollectValue" value="${fclBlForm.fclBl.streamShipBL}"/>
                                                <html:radio  property="fclBl.streamShipBL" value="P" styleId="sslBlPrepaid" name="fclBlForm" onclick="onchangeSslBlPrepaidCollect(this,${roleDuty.sslPrepaidCollect})"/>P
                                                <html:radio property="fclBl.streamShipBL" value="C" styleId="sslBlCollect" name="fclBlForm" onclick="onchangeSslBlPrepaidCollect(this,${roleDuty.sslPrepaidCollect})"/>C
                                            </td>
                                            <td align="right" >
                                                House BL Prepaid/Collect
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td align="right" colspan="3">
                                                House BL Prepaid/Collect 
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>
                                        <html:radio  property="fclBl.houseBL" value="P" name="fclBlForm" styleId="houseBlP" onmousedown="onKeyDownForHouseBl()" onclick="checkHouseBlCode('P')"/>P
                                        <html:radio  property="fclBl.houseBL" value="C" name="fclBlForm" styleId="houseBlC" onmousedown="onKeyDownForHouseBl()" onclick="checkHouseBlCode('C')"/>C
                                        <html:radio  property="fclBl.houseBL" value="B" name="fclBlForm" styleId="houseBlB" onmousedown="onKeyDownForHouseBl()" onclick="checkHouseBlCode('B')"/>B
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td align="right">
                                        SSL BL Destination Charges
                                    </td>
                                    <td colspan="2">
                                        <table border="0" width="100%" cellpadding="0" cellspacing="0">
                                            <tr class="textlabelsBold">
                                                <td align="left">
                                                    <html:radio  property="fclBl.destinationChargesPreCol" value="P" name="fclBlForm" styleId="destBlPrepaid"/>P
                                                    <html:radio  property="fclBl.destinationChargesPreCol" value="C" name="fclBlForm" styleId="destBlCollect"/>C
                                                </td>
                                                <td align="right">
                                                    &nbsp;&nbsp;<span id="warningMessage" class="warningStyle" dir="rtl">
                                                    </span>
                                                    &nbsp;&nbsp;<span id="autosCredit" class="warningStyle" dir="rtl"></span>
                                                    <img alt="Credit Status" src="${path}/img/icons/iicon.png" id="creditStatusInfo" onmouseover="tooltip.show('<strong>Click to see all Credit Status</strong>', null, event);"
                                                         onmouseout="tooltip.hide();" onclick="creditStatusBillToBoth();" height="16" width="16">
                                                </td>
                                                <td align="right">
                                                    Bill To Code
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td align="left">
                                        <html:radio  property="fclBl.billToCode" value="F" styleId="billToCodeF" name="fclBlForm" onclick="checkBillToCode()"/>F
                                        <html:radio  property="fclBl.billToCode" value="S" styleId="billToCodeS" name="fclBlForm" onclick="checkBillToCode()"/>S
                                        <html:radio  property="fclBl.billToCode" value="T" styleId="billToCodeT" name="fclBlForm" onclick="checkBillToCode()"/>T
                                        <html:radio  property="fclBl.billToCode" value="A" styleId="billToCodeA" name="fclBlForm" onclick="checkBillToCode()"/>A
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td align="right">
                                        Third Party Account Name</td>
                                    <td>
                                        &nbsp;&nbsp;<html:text property="fclBl.thirdPartyName"  styleClass="textlabelsBoldForTextBox"
                                                   style="color: #4040FF;font-weight: bold;text-transform: uppercase"  styleId="billThirdPartyName" size="27" maxlength="80"/>
                                        <input name="billThirdPartyCheck" id="billThirdPartyCheck" type="hidden" value="${fclBl.thirdPartyName}"/>
                                        <div id="thirdParty_choices"  style="display:none;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("billThirdPartyName", "thirdParty_choices", "billTrePty", "billThirdPartyCheck",
                                                    "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=0&acctTyp=T", "getThirdParty()", "");
                                        </script>
                                        <c:if test="${fclBlForm.fclBl.importFlag !='I'}">
                                            &nbsp;<img src="${path}/img/icons/comparison.gif" alt="Add Contact" align="middle" id="contactNameButtonForT"
                                                       onclick="openContactInfo('T')"/>
                                        </c:if>
                                    </td>
                                    <td style="padding-left:10px;" align="right">
                                        Account#</td>
                                    <td>
                                        &nbsp;<html:text styleClass="BackgrndColorForTextBox" property="fclBl.billTrdPrty"
                                                   styleId="billTrePty" readonly="true"  size="10" tabindex="-1"/>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold" >
                                    <td class="setTopBorderForTable" align="right">Received SSL Master</td>
                                    <td class="setTopBorderForTable" style="color:green;font-weight:bold;font-size:13" id="masterDiv">&nbsp;
                                        <c:choose>
                                            <c:when test="${null!=MasterScan && MasterScan!='0' && null != MasterStatus && not empty MasterStatus}">
                                                <c:out value="Yes (${MasterStatus})"></c:out>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${fclBlForm.fclBl.receivedMaster}"></c:out>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td align="right" class="setTopBorderForTable" >File Type</td>
                                    <td align="left" style="padding-bottom:5px;" class="setTopBorderForTable">
                                        <c:choose>
                                            <c:when test="${fclBlForm.fclBl.importFlag !='I'}">
                                                <html:radio  property="fclBl.filetype" value="S" styleId="fileTypeS" name="fclBlForm"/>
                                                <span onmouseover="tooltip.show('<strong>Standard</strong>', null, event);"
                                                      onmouseout="tooltip.hide();">S</span>
                                                <html:radio property="fclBl.filetype" value="C" styleId="fileTypeC" name="fclBlForm"/>
                                                <span onmouseover="tooltip.show('<strong>CFCL</strong>', null, event);"
                                                      onmouseout="tooltip.hide();">C</span>
                                                <html:radio property="fclBl.filetype" value="P" styleId="fileTypeP" name="fclBlForm"/>
                                                <span onmouseover="tooltip.show('<strong>Project</strong>', null, event);"
                                                      onmouseout="tooltip.hide();">P</span>
                                            </c:when>
                                            <c:otherwise>
                                                <input  value="IMPORT" class="textlabelsBoldForTextBox" size="4" readonly="true" tabindex="-1" />
                                                <input type="hidden" name="fclBl.filetype" value="I" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td colspan="4">
                                        <table border="0" width="100%">
                                            <tr class="textlabelsBold">
                                                <td align="right">
                                                    Booking Contact</td>
                                                <td>
                                                    &nbsp;&nbsp;<html:text property="fclBl.bookingContact"  styleClass="textlabelsBoldForTextBox"
                                                               styleId="bookingContact" size="27" maxlength="80" value="${fclBl.bookingContact}"/>
                                                <td align="right">Ready to send SS Master via EDI</td>
                                                <td>
                                                    <input type="checkbox" id="ediCheckBox" name="ediCheckBox"
                                                           onclick="readyToSendEdi()"/>
                                                </td>
                                                <td align="right">Ready To Post/Manifest</td>
                                                <td>
                                                    <html:checkbox property="readyToPost" name="fclBlForm" styleId="readyToPost" onclick="checkReadyToPost()"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr><tr><td></td></tr>
                            </table>
                        </td>
                        <td valign="top" style="border: 1px solid #C4C5C4;">
                            <table  border="0"  width="100%" cellpadding="2" cellspacing="0" >
                                <tr class="tableHeadingNew"><td colspan="4">Carrier/Voyage/Vessel
                                        <span style="padding-left: 80px;">EDI:<b class="BackgrndColorForTextBox">
                                                <c:choose>
                                                    <c:when test="${fclBlForm.fclBl.inttra == 'I'}">
                                                        INTTRA
                                                    </c:when>
                                                    <c:when test="${fclBlForm.fclBl.inttra == 'G'}">
                                                        GT NEXUS
                                                    </c:when>
                                                    <c:otherwise>
                                                    </c:otherwise>
                                                </c:choose>
                                            </b></span></td>
                                </tr>
                                <tr>
                                    <td align="right" class="textlabelsBold" >
                                        <span onmouseover="tooltip.show('<strong>Steam ship Line Name</strong>', null, event);"
                                              onmouseout="tooltip.hide();">SSLName</span>
                                    </td>
                                    <c:choose>
                                        <c:when test="${null!=fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag=='I'}">
                                            <td>
                                                <input class="textlabelsBoldForTextBox mandatory" name="fclBl.sslineName" id="streamShipName" size="30"/>
                                                <div id="streamShipName_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("streamShipName", "streamShipName_choices", "sslineNo", "sslname_check",
                                                            "${path}/actions/tradingPartner.jsp?tabName=FCL_BL&from=15", "setSSLNameForImportBL()", "");
                                                </script>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>
                                                <html:text styleClass="BackgrndColorForTextBox" property="fclBl.sslineName" styleId="streamShipName" size="30" readonly="true"  tabindex="-1"/>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="textlabelsBold"  align="right">SSL # </td>
                                    <td>
                                        <html:text styleClass="BackgrndColorForTextBox" property="fclBl.sslineNo" styleId="sslinenumber" size="30" readonly="true"  tabindex="-1"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textlabelsBold" align="right" >Vessel Name</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${null!=fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag=='I'}">
                                                <html:text  styleClass="textlabelsBoldForTextBox mandatory" property="vesselName" styleId="vesselname"  size="13"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text  styleClass="textlabelsBoldForTextBox" property="vesselName" styleId="vesselname"  size="13"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <input type="hidden" name="vesselname_check" id="vesselname_check" value="${fclBlForm.vesselName}"/>
                                        <div id="vesselname_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initAutocompleteWithFormClear("vesselname", "vesselname_choices", "vessel", "vesselname_check",
                                                    "${path}/actions/getVesselName.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false", "setFocusTo('voyage')", "");
                                        </script>
                                    </td>
                                    <td class="textlabelsBold"  align="right">Vessel #<font class="mandatoryStarColor"></font></td>
                                    <td>
                                        <html:text styleClass="BackgrndColorForTextBox" readonly="true" property="vessel"  styleId="vessel" size="13"  tabindex="-1"/>
                                    </td>
                                </tr>
                                <tr valign="top">
                                    <td class="textlabelsBold" align="right" valign="baseline">SS Voyage</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${null!=fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag=='I'}">
                                                <html:text styleClass="textlabelsBoldForTextBox mandatory" property="fclBl.voyages" styleId="voyage" size="13" style="text-transform: uppercase" maxlength="50"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text styleClass="textlabelsBoldForTextBox" property="fclBl.voyages" styleId="voyage" size="13" style="text-transform: uppercase" maxlength="50"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <div id="voyage_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initOPSAutocomplete("voyage", "voyage_choices", "", "",
                                                    "${path}/actions/getVoyageNo.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false", "");
                                        </script>
                                    </td>
                                    <td class="textlabelsBold" align="right" valign="baseline">Voy Internal</td>
                                    <td>
                                        <html:text property="fclBl.voyageInternal" styleClass="textlabelsBoldForTextBox capitalize" styleId="voyageInternal" maxlength="20"
                                                   size="13" ></html:text>
                                        </td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td align="right" width="120">Container Cut Off</td>
                                        <td><fmt:formatDate pattern="MM/dd/yyyy HH:mm a"  var="portCutOff" value="${fclBlForm.fclBl.portCutOff}"/>
                                        <html:text property="portCutOff"  value="${portCutOff}" styleClass="textlabelsBoldForTextBox" size="19"
                                                   style="color:red;text-transform: uppercase"      styleId="txtcal313"  onchange="AlertMessage(this)"  />
                                        <input type="hidden" id="dateInYard" value="${fclBlForm.fclBl.dateInYard}"/>
                                        <input type="hidden" id="earlierPickUpDate" value="${fclBlForm.fclBl.earlierPickUpDate}"/>
                                        <img src="${path}/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal313"
                                             onmousedown="insertDateFromCalendar(this.id, 9);"/>
                                    </td>
                                    <td align="right">Doc Cut Off</td>
                                    <td><fmt:formatDate pattern="MM/dd/yyyy HH:mm a" var="docCutOff" value="${fclBlForm.fclBl.docCutOff}"/>
                                        <html:text property="docCutOff" styleClass="textlabelsBoldForTextBox" size="19" value="${docCutOff}"
                                                   style="color:red;text-transform: uppercase" styleId="txtcal71" onchange="AlertMessage(this)" />
                                        <img src="${path}/img/CalendarIco.gif" alt="cal"  name="cal1" width="16" height="16" id="cal71"
                                                          onmousedown="insertDateFromCalendar(this.id, 9);" />
                                    </td>
                                </tr>
                                <tr class="textlabelsBold" valign="top">
                                    <td align="right" valign="baseline">ETD</td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox" property="sailDate"
                                                   styleId="txtetdCal" onchange="validateEtd(this)"
                                                   size="10"  />
                                        <input type="hidden" id="etdDate" value="${fclBlForm.sailDate}"/>
                                        <img src="${path}/img/CalendarIco.gif" alt="cal" id="etdCal"
                                             onmousedown="insertDateFromCalendar(this.id, 0);"  />
                                    </td>
                                    <td align="right" valign="baseline">ETA</td>
                                    <td>
                                        <html:text styleClass="textlabelsBoldForTextBox"  property="eta"
                                                   styleId="txtetaCal" onchange="validateBlETA(this)"
                                                   size="10"  />
                                        <input type="hidden" id="etaDate" onchange="return validateDate(this);" value="${fclBlForm.eta}"/>
                                        <img src="${path}/img/CalendarIco.gif" alt="cal" id="etaCal"
                                             onmousedown="insertDateFromCalendar(this.id, 0);"  />
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td align="right" style="padding-bottom:5px;">SSL Booking # </td>
                                    <td style="padding-bottom:5px;">
                                        <html:text  styleClass="textlabelsBoldForTextBox" property="fclBl.bookingNo" size="13"
                                                    styleId="booking" onkeypress="trimSslNum();" style="text-transform: uppercase" maxlength="20"/>
                                        <div id="booking_choices"  style="display: none;width: 5px;"   class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initOPSAutocomplete("booking", "booking_choices", "notInUse", "notInUse2",
                                                    "${path}/actions/BookingNo.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false", "");
                                        </script>
                                        <input type="text" id="notInUse" style="display: none;" />
                                        <input type="text" id="notInUse2" style="display: none;"/>
                                    </td>
                                    <td align="right" style="padding-bottom:5px;">MASTER BL # </td>
                                    <td style="padding-bottom:5px;">
                                        <html:text  styleClass="textlabelsBoldForTextBox" maxlength="20" property="fclBl.newmasterbL"
                                                    size="22"   style="text-transform: uppercase"
                                                    styleId="newMasterBL" />
                                        <div id="newMasterBL_choices"  style="display: none;width: 5px;"
                                             class="autocomplete"></div>
                                        <script type="text/javascript">
                                            initOPSAutocomplete("newMasterBL", "newMasterBL_choices", "notInUse2", "notInUse4",
                                                    "${path}/actions/BookingNo.jsp?tabName=FCL_BILL_LADDING&from=1&isDojo=false", "");
                                        </script>
                                        <span style="padding-right:10px;" onmouseover="tooltip.show('<strong>Copy SSLBKG# </strong>', '100', event);"onmouseout="tooltip.hide();">
                                            <input  type="checkbox" id="newMasterBLCheckBox" onclick="setSslBookingNo()"/>
                                        </span>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </table>
            <div  id="fclBLContainer" style="width:97%;padding-left:5px;padding-top:10px;float: left;">
                <ul class="newtab">
                    <li>
                        <a href="#trade" onclick='setTabName("tradeRoute");'>
                            <span class="newtabRight"> Trade Route
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#paries" onclick='setTabName("shipperForwarder");'>
                            <span class="newtabRight">
                                Shipper Forwarder Consignee Notify<font color="red"> * &nbsp; </font>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#general" onclick='setTabName("general");'>
                            <span class="newtabRight">
                                General
                            </span>
                        </a>
                    </li>
                    <li><a href="#containerDetails" onclick='setTabName("container");'><span class="newtabRight">ContainerDetails<font color="red">* &nbsp; </font></span></a></li>
                    <li><a href="#costCharges" onclick='setTabName("charges");'><span class="newtabRight">Cost&Charges</span></a></li>
                    <li><a href="#print" onclick='setTabName("print");'><span class="newtabRight">Print Options</span></a></li>
                    <li><a href="#aesFiling" onclick='setTabName("aes");'><span class="newtabRight">AES Details</span></a></li>
                </ul>
                <div id="trade">
                    <%@include file="../FCL/FclTradeRoute.jsp"  %>
                </div>
                <div id="paries">
                    <%@include file="../FCL/FclParties.jsp"  %>
                </div>
                <div id="general">
                    <%@include file="../FCL/FclGeneral.jsp"  %>
                </div>
                <div id="containerDetails">
                    <c:import url="../FCL/fclBlContainer.jsp"/>
                </div>
                <div id="costCharges">
                    <c:import url="../FCL/FclBlCharges.jsp" />
                </div>
                <div id="print">
                    <%@include file="../FCL/PrintOptions.jsp" %>
                </div>
                <div id="aesFiling">
                    <c:import url="../FCL/SedFiling.jsp"/>
                </div>
            </div>
            <table border="0">
                <tr class="textlabelsBold">
                    <td id="fileNo">File BOL No :<font size="4" color="#ff4a4a">${companyCode}-${fclBlForm.fclBl.bolId}</font></td>
                         <c:if test="${fclBlForm.fclBl.spotRate eq 'Y'}">
                         <td></td><td colspan="6" align="center"  style="font-size:large;font-weight: bolder;color: #fd0000"> **SPOT/BULLET RATE** </td>
                        </c:if>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <%@include file="../FCL/footerDiv.jsp" %>
                    </td>
                </tr>
            </table>
            <html:hidden property="freightInvoiceContacts" styleId="freightInvoiceContacts" value=""/>
            <%@include file="../FCL/FclPopUps.jsp" %>
        </html:form>
        <div id="creditStatus" class="static-popup" style="display: none;">
            <table class="table" border="0" style="margin: 1px;width: 500px;">
                <tr>
                    <th colspan="3" >
                <div class="float-left" >Credit Status</div>
                <div class="float-right">
                    <a href="javascript: hideCreditStatus()" style="">
                        <img alt="Close" src="${path}/images/icons/close.png"/>
                    </a>
                </div>
                </th>
                </tr>
                <tr id="shipperId">
                    <td class="label">SHIPPER</td>
                    <td class="label">: <span id="shipperWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="shipperStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
                <tr id="forwarderId">
                    <td class="label">FORWARDER</td>
                    <td class="label">: <span id="forwarderWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="forwarderStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
                <tr id="thirdPartyId">
                    <td class="label">THIRD PARTY</td>
                    <td class="label">: <span id="thirdpartyWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="thirdpartyStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
                <tr id="agentId">
                    <td class="label">AGENT</td>
                    <td class="label">: <span id="agentWarning" class="warningStyle" dir="rtl"/></td>
                    <td><span id="agentStatus" class="warningStyle" dir="rtl"/></td>
                </tr>
            </table>
        </div>
        <c:choose>
            <c:when test="${fclBlForm.action == 'printBookingReport'}">
                <script language="javascript">
                    var path = "${path}/printConfig.do?screenName=BL&fileNo=${fclBlForm.fclBl.fileNo}&blId=${fclBlForm.fclBl.bol}&bolNo=${fclBlForm.fclBl.bolId}&destination=${fclBlForm.fclBl.port}&filesToPrint=booking&billingTerminal=${fclBlForm.fclBl.billingTerminal}&subject=FCL BL";
                    GB_show('Print/Fax/Email FCL BL  FileNumber ${fclBlForm.fclBl.fileNo}', path, 400, 1000);
                    jQuery("#action").val("");
                </script>
            </c:when>
            <c:when test="${fclBlForm.action == 'printBlReport'}">
                <script language="javascript">
                    var path = "${path}/printConfig.do?screenName=BL&fileNo=${fclBlForm.fclBl.fileNo}&blId=${fclBlForm.fclBl.bol}&bolNo=${fclBlForm.fclBl.bolId}&destination=${fclBlForm.fclBl.port}&filesToPrint=all&billingTerminal=${fclBlForm.fclBl.billingTerminal}&subject=FCL BL";
                    GB_show('Print/Fax/Email FCL BL  FileNumber ${fclBlForm.fclBl.fileNo}', path, 400, 1000);
                    jQuery("#action").val("");
                </script>
            </c:when>
            <c:when test="${fclBlForm.action == 'printComfirmOnBoardReport'}">
                <script language="javascript">
                    var importFlag = jQuery("#importFlag").val();
                    
                    var path = "${path}/printConfig.do?screenName=BL&fileNo=${fclBlForm.fclBl.fileNo}&blId=${fclBlForm.fclBl.bol}&bolNo=${fclBlForm.fclBl.bolId}&destination=${fclBlForm.fclBl.port}&filesToPrint=confirmOnBoard&billingTerminal=${fclBlForm.fclBl.billingTerminal}&subject=FCL BL";
                    GB_show('Print/Fax/Email FCL BL ConfirmOnBoard FileNumber ${fclBlForm.fclBl.fileNo}', path, 400, 1000);
                    jQuery("#action").val("");
                </script>
            </c:when>
            <c:when test="${fclBlForm.action == 'sendEdi'}">
                <script language="javascript">
                    validateDataForEdi('validate');
                    jQuery("#action").val("");
                </script>
            </c:when>
            <c:when test="${! empty readyToPost}">
                <script language="javascript">
                    displayAutoNotificationContacts('${manifestWithoutCharges}');
                    jQuery("#action").val("");
                </script>
            </c:when>
        </c:choose>
    </body>
</html>