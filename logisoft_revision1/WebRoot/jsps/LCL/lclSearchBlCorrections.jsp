<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclCorrection.js"></cong:javascript>
<cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="lclCorrection.do">
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="correctionId" name="correctionId"/>
    <cong:hidden id="selectedMenu" name="selectedMenu" value="${lclCorrectionForm.selectedMenu}"/>
    <br/>
    <table align="center" cellpadding="0" cellspacing="0" width="98%" border="0" style="border:1px solid #dcdcdc;">
        <tr class="tableHeadingNew">
            <td>Search Criteria</td>
            <td width="80%" align="right">
            </td>
        </tr>
        <tr><td colspan="2">
                <table width="100%" border="0" cellpadding="2" cellspacing="0">
                    <tr class="textlabelsBold">
                        <td>File No</td>
                        <td>
                            <cong:text name="fileNo" id="fileNo" styleClass="textlabelsLclBoldForMainScreenTextBox"
                                       style="text-transform: uppercase" />
                        </td>
                        <td>BL No</td>
                        <td>
                            <cong:text name="blNo" id="blNo" styleClass="textlabelsBoldForTextBox"/>
                        </td>
                        <td>Correction Code</td>
                        <td>
                            <html:select property="searchCorrectionCode" styleClass="dropdown_accounting" >
                                <html:optionsCollection name="correctionCodeList"/>
                            </html:select>
                        </td>
                        <td>Date</td>
                        <td>
                            <cong:calendarNew  id="dateFilter" name="dateFilter"  styleClass="textlabelsLclBoldForMainScreenTextBox" />
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        <td>Shipper</td>
                        <td>
                            <cong:autocompletor name="shipperName" id="shipperName" template="tradingPartner" query="SHIPPER"
                                                fields="searchShipperNo,accttype,subtype" width="600"
                                                scrollHeight="300px" container="NULL" shouldMatch="true"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox" callback="shipper_AccttypeCheck()" />
                            <cong:hidden name="searchShipperNo" id="searchShipperNo" />
                            <input type="hidden" name="subtype" id="subtype" />
                            <input type="hidden" name="accttype" id="accttype" />
                        </td>
                        <td>Forwarder</td>
                        <td>
                            <cong:autocompletor name="forwarder" template="tradingPartner" query="MAIN_SCREEN_FORWARDER" fields="searchForwarderNo"
                                                scrollHeight="330px" width="550" container="NULL" shouldMatch="true"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <cong:hidden name="searchForwarderNo" id="searchForwarderNo" />
                        </td>
                        <td>Third Party</td>
                        <td>
                            <cong:autocompletor name="thirdPartyName" template="tradingPartner" query="THIRD_PARTY" fields="searchThirdPartyAcctNo"
                                                scrollHeight="300px" width="600" container="NULL" shouldMatch="true"
                                                styleClass="textlabelsLclBoldForMainScreenTextBox"/>
                            <cong:hidden name="searchThirdPartyAcctNo" id="searchThirdPartyAcctNo" />
                        </td>
                        <td>Notice_#</td>
                        <td>
                            <cong:text name="noticeNo" id="noticeNo"  styleClass="textlabelsBoldForTextBox" />
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${lclCorrectionForm.selectedMenu eq 'Imports'}">
                            <c:set var="origin_Query" value="ORIGIN_UNLOC"/>
                            <c:set var="pol_Query" value="CONCAT_RELAY_NAME_FD"/>
                            <c:set var="pod_Query" value="RELAYNAME"/>
                            <c:set var="fd_Query" value="DEST_UNLOC"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="origin_Query" value="RELAYNAME"/>
                            <c:set var="pol_Query" value="ORIGIN_UNLOC"/>
                            <c:set var="pod_Query" value="ORIGIN_UNLOC"/>
                            <c:set var="fd_Query" value="CONCAT_RELAY_NAME_FD"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="textlabelsBold">
                        <td>Origin</td>
                        <td>
                            <cong:autocompletor id="origin" name="origin" template="one" fields="NULL,NULL,NULL,portOfOriginId" query="${origin_Query}"
                                                width="250" container="NULL"  shouldMatch="true" />
                            <cong:hidden id="portOfOriginId" name="portOfOriginId"/>
                        </td>
                        <td>POL</td>
                        <td>
                            <cong:autocompletor id="pol" name="pol" template="one" fields="NULL,NULL,NULL,polId" query="${pol_Query}"
                                                width="250" container="NULL"  shouldMatch="true" />
                            <cong:hidden id="polId" name="polId"/>
                        </td>
                        <td>POD</td>
                        <td>
                            <cong:autocompletor id="pod" name="pod" template="one"  fields="NULL,NULL,unlocationCode,podId"
                                                query="${pod_Query}" width="350" container="NULL"
                                                shouldMatch="true" />
                            <cong:hidden id="podId" name="podId" />
                            <br></td>
                        <td>Destination</td>
                        <td>
                            <cong:autocompletor id="destination" name="destination" template="one"  fields="NULL,NULL,unlocationCode,finalDestinationId"
                                                query="${fd_Query}"  width="350" container="NULL" position="left"
                                                shouldMatch="true" />
                            <cong:hidden id="finalDestinationId" name="finalDestinationId" />
                        </td>
                    </tr>
                    <tr>
                        <td class="textlabelsBold">Created By<br></td>
                        <td class="textlabelsBold">
                            <cong:autocompletor name="createdBy" id="createdBy" template="one" query="SALES_PERSON"  width="300" container="NULL"
                                                fields="createdByUserId" styleClass="textlabelsLclBoldForMainScreenCheckBox" scrollHeight="200px"/>
                            <html:checkbox property="chkCreatedBy" onmouseover="tooltip.showSmall('<strong>Me</strong>');"
                                           onmouseout="tooltip.hide();" onclick="setDefaultLoginName(this,'createdBy','createdByUserId')"/>
                            <input type="hidden" id="loginName" name="login" value="${loginName}"/>
                            <input type="hidden" id="userId" name="login" value="${userId}"/>
                            <cong:hidden name="createdByUserId" id="createdByUserId"/>
                        </td>
                        <td class="textlabelsBold">Approved By</td>
                        <td class="textlabelsBold" >
                            <cong:autocompletor name="approvedBy" id="approvedBy" template="one" query="SALES_PERSON"  width="300" container="NULL"
                                                fields="approvedByUserId" styleClass="textlabelsLclBoldForMainScreenCheckBox" scrollHeight="200px"/>
                            <html:checkbox property="chkApprovedBy" onmouseover="tooltip.showSmall('<strong>Me</strong>');"
                                           onmouseout="tooltip.hide();" onclick="setDefaultLoginName(this,'approvedBy','approvedByUserId')"/>
                            <cong:hidden name="approvedByUserId" id="approvedByUserId"/>

                        </td>
                        <td  class="textlabelsBold">Filter By</td>
                        <td>
                            <html:select property="filterBy" style="width: 144px;" styleClass="dropdown_accounting">
                                <html:option value="All">All</html:option>
                                <html:option value="A">Approved</html:option>
                                <html:option value="O">Not Approved</html:option>
                                <html:option value="Q">Quick CN</html:option>
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
                            <input type="button" class="buttonStyleNew" value="Search" onclick="searchCorrections()"/>
                        </td>
                    </tr>
                </table>
            </td></tr>
    </table>
    <br/>
</cong:form>
