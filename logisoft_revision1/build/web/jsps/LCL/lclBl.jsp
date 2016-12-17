<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <%@include file="init.jsp" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@include file="colorBox.jsp" %>
    <%@include file="/jsps/preloader.jsp" %>
    <%@include file="../includes/baseResources.jsp" %>
    <%@include file="../includes/resources.jsp" %>
    <%@include file="/jsps/includes/jspVariables.jsp" %>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <%@include file="../fragment/lclFormSerialize.jspf"  %>
    <cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
    <cong:javascript  src="${path}/jsps/LCL/js/lclBL.js"/>
    <cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
    <%@include file="/taglib.jsp" %>
    <body>
        <div style="overflow:hidden" id="pane">
            <c:if test="${lockMessage!='' && lockMessage!=null}">
                <center>
                    <font color="#FF4A4A" size="2">
                        <b style="margin-left:150px;color: #000080;font-size: 15px;" >${lockMessage}</b>
                    </font>
                </center>
            </c:if>
            <cong:form name="lclBlForm" id="lclBlForm" action="lclBl.do">
                <cong:hidden name="bookingType" id="bookingType" value="E"/>
                <cong:hidden name="screenType" id="screenType"/>
                <cong:hidden name="blNumber" id="blNumber"/>
                <c:set var="postedBy" value="${not empty lclBl.postedByUser ? true : false}"/>
                <c:choose>
                    <c:when test="${lclBl.terminal.unLocationCode1!='' && lclBl.terminal.unLocationCode1!=null}">
                        <c:set var="unloccode" value="${fn:substring(lclBl.terminal.unLocationCode1,2,5)}"/></c:when>
                    <c:otherwise>
                        <c:set var="unloccode" value="${fn:substring(lclBl.portOfOrigin.unLocationCode,2,5)}"/>
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="moduleId" id="moduleId" value="${lclBl.lclFileNumber.fileNumber}"/>
                <input type="hidden" name="fileId" id="fileId" value="${lclBl.lclFileNumber.id}"/>
                <input type="hidden" name="path" id="path" value="${path}"/>
                <input type="hidden" name="screenName" id="screenName" value="LCL FILE"/>
                <input type="hidden" name="operationType" id="operationType" value="Scan or Attach"/>
                <input type="hidden" name="lockMessage" id="lockMessage" value="${lockMessage}"/>
                <input type="hidden" name="enteredUser" id="enteredUser"  value="${lclBl.enteredBy.id}"/>
                <input type="hidden" name="postedByUser" id="postedByUser"  value="${lclBl.postedByUser}"/>
                <input type="hidden" name="postedDate" id="postedDate"  value="${lclBl.postedDate}"/>
                <input type="hidden" id="loginUser" value="${loginuser.loginName}"><%-- Login Name--%>
                <input type="hidden" id="loginUserId" value="${loginuser.userId}"><%-- Login User Id--%>
                <input type="hidden" id="loginUserPassword" name="loginUserPassword" value="${loginuser.password}"><%-- Login User Id--%>
                <input type="hidden" id="userRoleId" name="userRoleId" value="${loginuser.role.id}"><%-- Login User Role Id--%>
                <input type="hidden" id="enteredByRoleId" name="enteredByRoleId" value="${lclBl.enteredBy.role.id}">
                <input type="hidden" id="enteredByUserId" name="enteredByUserId" value="${lclBl.enteredBy.userId}">
                <input type="hidden" id="unPostRole" name="unPostRole" value="${roleDutyForUnPost}">
                <input type="hidden" id="editBlOwnerRole" name="editBlOwnerRole" value="${editBlOwner}">
                <input type="hidden" id="bkgPoo" name="bkgPoo" value="${bkgPooUnloc}">
                <input type="hidden" id="cfcl" name="cfcl" value="${lclBl.lclFileNumber.lclBookingExport.cfcl}">
                <input type="hidden" id="printFlag" name="printFlag"/>
                <input type="hidden" id="postComment" name="postComment"/>

                <c:set var="companyName" value="${companyMnemoniceName eq 'ECI' ? 'ECCI' :'OTIC'}"/>
                <input type="hidden" id="fileNumberPrefix" name="fileNumberPrefix" value="${fn:substring(lclBl.portOfOrigin.unLocationCode,2,5)}-${lclBl.lclFileNumber.fileNumber}"/>
                <%-- <c:choose>
                     <c:when test="${isConsolidate}">
                 <c:set var="fileNumberPrefix" value="${lclBlForm.consolidatedBlFileNo}"/>
                     </c:when>
                     <c:otherwise> 
                     </c:otherwise>
                 </c:choose>--%>
                <c:choose>
                    <c:when test="${lclBl.lclFileNumber.shortShip}">
                        <c:set var="fileNumberPrefix" value="ZZ${lclBl.lclFileNumber.shortShipSequence}-${lclBl.lclFileNumber.fileNumber}"/>    
                    </c:when>
                    <c:when test="${lclBl.bookingType eq 'T'}">
                        <c:set var="fileNumberPrefix" value="IMP-${lclBl.lclFileNumber.fileNumber}"/>    
                    </c:when>
                    <c:otherwise>
                        <c:set var="fileNumberPrefix" value="${fn:substring(bkgPooUnloc,2,5)}-${lclBl.lclFileNumber.fileNumber}"/>    
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="isTranshipmentFile" id="isTranshipmentFile" value="${lclBl.lclFileNumber.lclBooking.bookingType}"/>
                <input type="hidden" name="aesRequiredForPostingBLs" id="aesRequiredForPostingBLs" value="${roleDuty.aesRequiredForPostingBLs}"/>
                <input type="hidden" name="file_status" id="file_status" value="${lclBl.lclFileNumber.status}"/> 
                <input type="hidden" id="exportDisposition" name="exportDisposition" value="${lclBl.lclFileNumber.status}"/>
                <cong:hidden name="fileNumberId" id="fileNumberId" value="${lclBl.fileNumberId}"/>
                <cong:hidden name="fileNumber" id="fileNumber" value="${lclBl.lclFileNumber.fileNumber}"/>
                <cong:hidden name="bookingFileNumberId" id="bookingFileNumberId"/>
                <input type="hidden" name="filterByChanges" id="filterByChanges"/> 
                <cong:hidden name="index" id="index" />
                <cong:hidden name="relayOverride" id ="relayOverride" value="${lclBl.relayOverride}"/>
                <!--   Fields for Voided Bl -->
                <cong:hidden name="unitSsId" id="unitSsId" value="${lclBlForm.unitSsId}"/>
                <cong:hidden name="blUnitCob" id="blUnitCob" value="${lclBlForm.blUnitCob}"/>
                <input type="hidden" name="role_duty_for_voidBl" id="role_duty_for_voidBl" value="${roleDuty.voidLCLBLafterCOB}"/> 
                <input type="hidden" name="preventExpRel" id="preventExpRel" value="${roleDuty.preventExpRelease}"/> 
                <cong:hidden name="voyageNumber" id="voyageNumber" value="${lclBlForm.voyageNumber}"/> 
                <cong:hidden name="unitNumber" id="unitNumber" value="${lclBlForm.unitNumber}"/>
                <cong:hidden name="voyageClosedUser" id="voyageClosedUser" value="${lclBlForm.voyageClosedUser}"/> 
                <cong:hidden name="rateType" id="rateType" value="${lclBlForm.rateType}"/><%--ratetype--%>
                <input type="hidden" name="spotRateCommNo" id="spotRateCommNo" value="<bean:message key="application.spotRate.commodityCode"/>"/>
                <%@include file="bl/status.jsp" %>
                <%@include file="bl/button.jsp" %>
                <div id="process-block">
                    <div class="table-block">
                        <%@include file="bl/tradingPartners.jsp" %>
                        <%@include file="bl/tradeRoute.jsp" %>
                        <cong:table align="center" id="noteTable" cellpadding="0" cellspacing="0" width="100%" border="1" style="border:1px solid #dcdcdc">
                            <cong:tr styleClass="tableHeadingNew">
                                <cong:td width="100%">Commodity</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td id="commodityDesc">
                                    <c:import url="/jsps/LCL/commodityBlDesc.jsp">
                                        <c:param name="fileNumberId" value="${lclBl.lclFileNumber.id}"/>
                                        <c:param name="fileNumber" value="${lclBl.lclFileNumber.fileNumber}"/>
                                    </c:import>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </div>
                </div>
                <div class="table-block">
                    <div class="tab-block">
                        <div class="tab-box">
                            <a href="javascript:;" class="tabLink activeLink" id="cont-1"><span>Charges</span></a>
                            <a href="javascript:;" class="tabLink " id="cont-2"><span>Miscellaneous</span></a>
                            <a href="javascript:;" class="tabLink " id="cont-3"><span>Print Options</span></a>
                        </div>
                        <div class="tabcontent" id="cont-1-1">
                            <cong:div id="chargeBlDesc">
                                <c:import url="/jsps/LCL/ajaxload/chargeBlDesc.jsp">
                                    <c:param name="fileNumberId" value="${lclBl.lclFileNumber.id}"/>
                                    <c:param name="fileNumber" value="${lclBl.lclFileNumber.fileNumber}"/>
                                </c:import>
                            </cong:div>
                        </div>
                        <div class="tabcontent hide" id="cont-2-1">
                            <%@include file="/jsps/LCL/ajaxload/miscellaneousBL.jsp" %>
                        </div>
                        <div class="tabcontent hide" id="cont-3-1">
                            <%@include file="/jsps/LCL/ajaxload/printOptions.jsp" %>
                        </div>
                    </div>
                    <table>
                        <tr class="textBoldforlcl">
                            <td>File No :
                                <span class="fileNo">${fileNumberPrefix}</span>
                            </td>
                        </tr>
                    </table>
                </div>
                <%@include file="bl/buttonBottom.jsp" %>
                <input type="hidden" name="methodName" id="methodName"/>
            </cong:form>
        </div>
    </body>
</html>

<div id="add-void-bl" class="static-popup" style="display: none;width: 600px;height: 150px;">
    <table class="table" style="margin: 2px;width: 598px;">
        <tr>
            <th>
                <div class="float-left">
                    <label id="headingComments">BL Void Comments</label>
                </div>
            </th>
        </tr>
        <tr>
            <td></td>
        </tr>
        <tr>
            <td class="label">
                <textarea id="voidComments" name="voidComments" cols="85" rows="5" class="textBoldforlcl"
                          style="resize:none;text-transform: uppercase"></textarea>
            </td>
        </tr>
        <tr>
            <td align="center">
                <input type="button"  value="Save" id="saveHotCode"
                       align="center" class="button" onclick="addBlVoidComments();"/>
            </td>
        </tr>
    </table>
</div>
<div id="add-void-approvePassword" class="static-popup" style="display: none;width: 600px;height: 150px;">
    <table class="table" style="margin: 2px;width: 598px;">
        <tr>
            <th>
                <div class="float-left">
                    <label id="headingComments">Approve Password</label>
                </div>
            </th>
        </tr>
        <tr>
            <td align="center" class="textBoldforlcl">Enter Password</td>
        </tr>
        <tr>
            <td class="label" align="center">
                <input type="password" id="approvePassword" name="approvePassword" />
            </td>
        </tr>
        <tr>
            <td align="center">
                <input type="button"  value="Submit" id="saveHotCode"
                       align="center" class="button" onclick="approveCobPassword();"/>
            </td>
        </tr>
    </table>
</div>

