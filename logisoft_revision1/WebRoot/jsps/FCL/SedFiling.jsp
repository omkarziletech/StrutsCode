<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<% pageContext.setAttribute("singleQuotes", "'"); %>
<% pageContext.setAttribute("doubleQuotes", "\""); %>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
    </head>
    <body class="whitebackgrnd">
        <table width="100%" border="0" cellpadding="0"  class="tableBorderNew" cellspacing="0">
            <tr class="tableHeadingNew">
                <td>AES Details<td>
                <td align="right"><input type="button" class="buttonStyleNew" value="Add New" onclick="addAesDetails('${fclBl.bol}')"/></td>
            </tr>
            <tr>
                <td colspan="3">
                    <c:set var="index" value="0"></c:set>
                    <c:if test="${not empty sedFilingsList}">
                        <display:table name="${sedFilingsList}" class="displaytagstyleNew"  pagesize="50"
                                       id="sedFilingTable" sort="list" requestURI="/sedFiling.do?">
                            <display:setProperty name="paging.banner.some_items_found">
                                <span class="pagebanner">
                                    <font color="blue">{0}</font>Filenames Displayed,For more Data click on Page Numbers.
                                    <br>
                                </span>
                            </display:setProperty>
                            <display:setProperty name="paging.banner.one_items_found">
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
                            <display:setProperty name="paging.banner.placement" value="bottom" />
                            <display:setProperty name="paging.banner.item_name" value="Filename"/>
                            <display:setProperty name="paging.banner.items_name" value="Filenames"/>
                            <display:column>
                                <input type="hidden" name="transactionRefId" value="${sedFilingTable.trnref}" id="transactionRefId${index}"/>
                            </display:column>
                            <c:set var="index" value="${index+1}"></c:set>
                            <display:column  property="expnam"  title="Shipper"></display:column>
                            <display:column  property="connam"  title="Consignee"></display:column>
                            <display:column  property="trnref"  title="Transaction Ref#"></display:column>
                            <display:column  property="unitno"  title="Unit No"></display:column>
                            <display:column  property="itn"  title="ITN" maxLength="50"></display:column>
                            <display:column title="Status" >
                                <c:choose>
                                    <c:when test="${not empty sedFilingTable.itn}">
                                        <c:out value="Completed"/>
                                    </c:when>
                                    <c:when test="${sedFilingTable.status == 'N'}">
                                        <c:out value="New"/>
                                    </c:when>
                                    <c:when test="${sedFilingTable.status == 'S'}">
                                        <c:out value="Sent"/>
                                    </c:when>
                                    <c:when test="${sedFilingTable.status == 'C'}">
                                        <c:out value="Completed"/>
                                    </c:when>
                                    <c:when test="${sedFilingTable.status == 'P'}">
                                        <c:out value="Pending"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="Error"/>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="History">
                                <c:set var="itnStatus" value="${fn:replace(fn:replace(sedFilingTable.itnStatus,doubleQuotes,''),singleQuotes,'')}"/>
                                <c:if test="${not empty sedFilingTable.itnStatus}">
                                    <c:choose>
                                        <c:when test="${not empty sedFilingTable.itn || sedFilingTable.itnStatus == 'SHIPMENT ADDED' || sedFilingTable.itnStatus == 'Shipment Added' ||
                                                        sedFilingTable.itnStatus == 'SHIPMENT REPLACED' || sedFilingTable.itnStatus == 'Shipment Replaced'}">
                                                <span class="linkSpan"  style="font-weight: bold;background:#00FF00" border="0" onmouseover="tooltip.showSmall('<strong>${itnStatus}</strong>',null,event);"
                                                      onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','${path}/aesHistory.do?fileNumber=${sedFilingTable.trnref}');" >AES</span>
                                        </c:when>
                                        <c:when test="${fn:containsIgnoreCase(sedFilingTable.itnStatus,'verify')}">
                                            <span class="linkSpan"  style="font-weight: bold;background:#00FFFF" border="0" onmouseover="tooltip.showSmall('<strong>${itnStatus}</strong>',null,event);"
                                                  onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','${path}/aesHistory.do?fileNumber=${sedFilingTable.trnref}');" >AES</span>
                                        </c:when>
                                        <c:when test="${fn:containsIgnoreCase(sedFilingTable.itnStatus,'shipment rejected')}">
                                            <span class="linkSpan"  style="font-weight: bold;background:#FF0000" border="0" onmouseover="tooltip.showSmall('<strong>${itnStatus}</strong>',null,event);"
                                                  onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','${path}/aesHistory.do?fileNumber=${sedFilingTable.trnref}');" >AES</span>
                                        </c:when>
                                        <c:when test="${fn:containsIgnoreCase(sedFilingTable.itnStatus,'SUCCESSFULLY PROCESSED')}">
                                            <span class="linkSpan"  style="font-weight: bold;background:yellow" border="0" onmouseover="tooltip.showSmall('<strong>${itnStatus}</strong>',null,event);"
                                                  onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','${path}/aesHistory.do?fileNumber=${sedFilingTable.trnref}');" >AES</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="linkSpan"  style="font-weight: bold;background:#FF0000" border="0" onmouseover="tooltip.showSmall('<strong>${itnStatus}</strong>',null,event);"
                                                  onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','${path}/aesHistory.do?fileNumber=${sedFilingTable.trnref}');" >AES</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </display:column>
                            <display:column>
                                <input type="button"
                                       <c:choose>
                                           <c:when test="${sedFilingTable.sched}">
                                               class="buttoncolor"
                                           </c:when>
                                           <c:otherwise>
                                               class="buttonStyleNew"
                                           </c:otherwise>
                                       </c:choose>
                                       value="SchedB" id="schedB${index}"
                                       onclick="addSchedBDetails('${index}','${sedFilingTable.trnref}')" onmouseover="tooltip.show('<strong>Add SchedB</strong>',null,event);" onmouseout="tooltip.hide();"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <c:if test="${sedFilingTable.aesDisabledFlag !='D'}">
                                    <c:choose>
                                        <c:when test="${roleDuty.resendAes}">
                                            <input type="button" class="buttonStyleNew" value="Send AES" onclick="fileSed('${roleDuty.resendAes}','${sedFilingTable.trnref}','${index}',this,'${fclBl.fileNo}')" id="aesFileId${index}"
                                            </c:when>
                                            <c:otherwise>
                                                <input type="button" class="buttonStyleNew" value="Send AES" onclick="fileSed('','${sedFilingTable.trnref}','${index}',this,'${fclBl.fileNo}')" id="aesFileId${index}"
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${not empty sedFilingTable.itn || sedFilingTable.itnStatus == 'SHIPMENT ADDED' || sedFilingTable.itnStatus == 'Shipment Added' ||
                                                            sedFilingTable.itnStatus == 'SHIPMENT REPLACED' || sedFilingTable.itnStatus == 'Shipment Replaced'}">
                                                    style="background:#00FF00"
                                            </c:when>
                                            <c:when test="${fn:containsIgnoreCase(sedFilingTable.itnStatus,'verify')}">
                                                style="background-color:#00FFFF;"
                                            </c:when>
                                            <c:when test="${fn:containsIgnoreCase(sedFilingTable.itnStatus,'shipment rejected')}">
                                                style="background-color: #FF0000;"
                                            </c:when>
                                            <c:when test="${fn:containsIgnoreCase(sedFilingTable.itnStatus,'SUCCESSFULLY PROCESSED')}">
                                                style="background-color: yellow"
                                            </c:when>
                                            <c:when test="${! empty sedFilingTable.itnStatus}">
                                                style="background-color: #FF0000"
                                            </c:when>
                                            <c:when test="${sedFilingTable.status == 'N'}">
                                                style=""
                                            </c:when>
                                            <c:when test="${sedFilingTable.status == 'S' || sedFilingTable.status == 'P'}">
                                                style="background-color: yellow;"
                                            </c:when>
                                            <c:when test="${sedFilingTable.status == 'C'}">
                                                style="background-color: #008000;"
                                            </c:when>
                                            <c:otherwise>
                                                style=""
                                            </c:otherwise>
                                        </c:choose>
                                        onmouseover="tooltip.show('<strong>Send AES</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                </c:if>
                            </display:column>
                            <display:column title="ACTION" >
                                <img src="<c:url value='/img/icons/edit.gif'/>" border="0" onclick="GB_showFullScreen('Sed Filing', '${path}/sedFiling.do?buttonValue=editSedFiling&trnref=${sedFilingTable.trnref}')"
                                     onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                <c:choose>
                                    <c:when test="${sedFilingTable.status != 'S'}">
                                        <img id="deleteAesDetails" src="${path}/img/icons/delete.gif"  onclick="deleteAesFiling('${sedFilingTable.trnref}')" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                    </c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${sedFilingTable.status ne null and sedFilingTable.status ne '' }">
                                    <c:choose>
                                            <c:when test="${sedFilingTable.aesDisabledFlag == 'D'}">
                                                <img src="${path}/img/icons/key.ico"  onmouseover="tooltip.show('Click to Enable AES Submission',null,event);" onmouseout="tooltip.hide();"
                                                     onclick="disableOrEnableForAes('${sedFilingTable.id}','enableForAes')"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img id="aesCommentLock" src="${path}/img/icons/lockon.ico"  onclick="disableOrEnableForAes('${sedFilingTable.id}','disableForAes')"
                                                     onmouseover="tooltip.show('<strong>Click to VOID AES Submission</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <img id="aesCommentLock" src="${path}/img/icons/lockon.ico"  onclick="disableOrEnableForAes('${sedFilingTable.id}','disableForAes')"
                                             onmouseover="tooltip.show('<strong>Click to VOID AES Submission</strong>',null,event);" onmouseout="tooltip.hide();" style="display:none"/>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${not empty sedFilingTable.aesComment}">
                                    <c:set var="aesComments" value="${fn:replace(sedFilingTable.aesComment,newLineChar,' ')}"/>
                                    <span class="hotspot" onmouseover="tooltip.showComments('<strong>${aesComments}</strong>',150,event);"
                                          onmouseout="tooltip.hideComments();" style="color:black;">
                                        <img id="viewgif4"  src="${path}/img/icons/view.gif"/></span>
                                    </c:if>
                                </display:column>
                            </display:table>
                        </c:if>
                </td>
            </tr>
        </table>
    </body>
</html>
