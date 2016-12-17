<%-- 
    Document   : blCorrectionSearchPool
    Created on : Jan 11, 2016, 7:29:36 PM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="../../colorBox.jsp" %>
<%@include file="../../../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/export/blCorrectionSearchPool.js"></cong:javascript>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="/blCorrection.do">
            <cong:hidden id="methodName" name="methodName"/>
            <cong:hidden id="correctionId" name="correctionId"/>
            <input type="hidden" id="loginName" name="loginName" value="${loginuser.loginName}"/>
            <input type="hidden" id="loginUserId" name="loginUserId" value="${loginuser.userId}"/>
            <cong:hidden id="selectedMenu" name="selectedMenu" value="${lclCorrectionForm.selectedMenu}"/>
            <div id="pane" style="overflow: auto;">
                <table align="center" cellpadding="0" cellspacing="0" width="100%" border="0" class="tableBorderNew">
                    <tr class="tableHeadingNew">
                        <td>Search Criteria</td>
                        <td width="80%" align="right">
                        </td>
                    </tr>
                    <tr><td colspan="2">
                            <table width="100%" border="0" cellpadding="2" cellspacing="0">
                                <tr>
                                    <td class="textlabelsBoldforlcl">File No</td>
                                    <td>
                                        <cong:text name="searchFileNo" id="searchFileNo" styleClass="textlabelsLclBoldForMainScreenTextBox"
                                                   style="text-transform: uppercase" />
                                    </td>
                                    <td class="textlabelsBoldforlcl">BL No</td>
                                    <td>
                                        <cong:text name="searchBlNo" id="searchBlNo" styleClass="textlabelsBoldForTextBox"/>
                                    </td>
                                    <td class="textlabelsBoldforlcl">Correction Code</td>
                                    <td>
                                        <html:select property="searchCorrectionCode" styleClass="dropdown_accounting" >
                                            <html:optionsCollection name="correctionCodeList"/>
                                        </html:select>
                                    </td>
                                    <td class="textlabelsBoldforlcl">Date</td>
                                    <td>
                                        <cong:calendarNew  id="searchDate" name="searchDate"  styleClass="textlabelsLclBoldForMainScreenTextBox" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textlabelsBoldforlcl">Shipper</td>
                                    <td>
                                        <cong:autocompletor name="searchShipName" id="searchShipName" template="tradingPartner" query="SHIPPER"
                                                            fields="searchShipperNo,accttype,subtype" width="600"
                                                            scrollHeight="300px" container="NULL" shouldMatch="true"
                                                            styleClass="textlabelsLclBoldForMainScreenTextBox" />
                                        <cong:hidden name="searchShipperNo" id="searchShipperNo" />
                                        <input type="hidden" name="subtype" id="subtype" />
                                        <input type="hidden" name="accttype" id="accttype" />
                                    </td>
                                    <td class="textlabelsBoldforlcl">Forwarder</td>
                                    <td>
                                        <cong:autocompletor name="searchForwarderName" template="tradingPartner" query="MAIN_SCREEN_FORWARDER" fields="searchForwarderNo"
                                                            id="searchForwarderName" scrollHeight="330px" width="550" container="NULL" shouldMatch="true"
                                                            styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                                        <cong:hidden name="searchForwarderNo" id="searchForwarderNo" />
                                    </td>
                                    <td class="textlabelsBoldforlcl">Third Party</td>
                                    <td>
                                        <cong:autocompletor name="searchThirdPartyAcctName" template="tradingPartner" query="THIRD_PARTY" fields="searchThirdPartyAcctNo"
                                                            id="searchThirdPartyAcctName"  scrollHeight="300px" width="600" container="NULL" shouldMatch="true"
                                                            styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                                        <cong:hidden name="searchThirdPartyAcctNo" id="searchThirdPartyAcctNo" />
                                    </td>
                                    <td class="textlabelsBoldforlcl">NoticeNo</td>
                                    <td>
                                        <cong:text name="searchNoticeNo" id="searchNoticeNo"  styleClass="textlabelsBoldForTextBox" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textlabelsBoldforlcl">Origin</td>
                                    <td>
                                        <cong:autocompletor id="searchPooName" name="searchPooName" template="one" fields="NULL,NULL,NULL,searchPooId"
                                                            query="RELAYNAME" width="250" container="NULL"  shouldMatch="true" />
                                        <cong:hidden id="searchPooId" name="searchPooId"/>
                                    </td>
                                    <td class="textlabelsBoldforlcl">POL</td>
                                    <td>
                                        <cong:autocompletor id="searchPolName" name="searchPolName" template="one"
                                                            fields="NULL,NULL,NULL,searchPolId" query="ORIGIN_UNLOC"
                                                            width="250" container="NULL"  shouldMatch="true" />
                                        <cong:hidden id="searchPolId" name="searchPolId"/>
                                    </td>
                                    <td class="textlabelsBoldforlcl">POD</td>
                                    <td>
                                        <cong:autocompletor id="searchPodName" name="searchPodName" template="one"  fields="NULL,NULL,NULL,searchPodId"
                                                            query="ORIGIN_UNLOC" width="350" container="NULL"
                                                            shouldMatch="true" />
                                        <cong:hidden id="searchPodId" name="searchPodId" />
                                        <br></td>
                                    <td class="textlabelsBoldforlcl">Destination</td>
                                    <td>
                                        <cong:autocompletor id="searchFdName" name="searchFdName" template="one"
                                                            fields="NULL,NULL,NULL,searchFdId"
                                                            query="CONCAT_RELAY_NAME_FD"  width="350" container="NULL" position="left"
                                                            shouldMatch="true" />
                                        <cong:hidden id="searchFdId" name="searchFdId" />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textlabelsBoldforlcl">Created By<br></td>
                                    <td>
                                        <cong:autocompletor name="searchCreatedBy" id="searchCreatedBy" template="one" query="SALES_PERSON"  width="300" container="NULL"
                                                            fields="searchCreatedByUserId" styleClass="textlabelsLclBoldForMainScreenCheckBox" scrollHeight="200px"/>
                                        <html:checkbox property="chkCreatedBy" title="Me"
                                                       onclick="defaultLoginName(this,'searchCreatedBy','searchCreatedByUserId')"/>
                                        <cong:hidden name="searchCreatedByUserId" id="searchCreatedByUser"/>
                                    </td>
                                    <td class="textlabelsBoldforlcl">Approved By</td>
                                    <td>
                                        <cong:autocompletor name="searchApprovedBy" id="searchApprovedBy" template="one" query="SALES_PERSON"  width="300" container="NULL"
                                                            fields="searchApproveByUserId" styleClass="textlabelsLclBoldForMainScreenCheckBox" scrollHeight="200px"/>
                                        <html:checkbox property="chkApprovedBy" title="Me"
                                                       onclick="defaultLoginName(this,'searchApprovedBy','searchApproveByUserId')"/>
                                        <cong:hidden name="searchApproveByUserId" id="searchApproveByUserId"/>

                                    </td>
                                    <td  class="textlabelsBoldforlcl">Filter By</td>
                                    <td>
                                        <html:select property="filterBy" style="width: 144px;" styleClass="dropdown_accounting">
                                            <html:option value="All">All</html:option>
                                            <html:option value="A">Approved</html:option>
                                            <html:option value="O">Not Approved</html:option>
                                            <html:option value="1">Disabled</html:option>
                                        </html:select>
                                    </td>
                                    <td></td><td></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>&nbsp;</td><td>&nbsp;</td>
                                </tr>
                                <tr class="textlabelsBold" style="padding-top:60px;">
                                    <td>&nbsp;</td><td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td align="center">
                                        <input type="button" class="buttonStyleNew" value="Search" onclick="searchPoolResult()"/>
                                    </td>
                                </tr>
                            </table>
                        </td></tr>
                </table>
            </div>
        </cong:form>
    </body>
</html>
