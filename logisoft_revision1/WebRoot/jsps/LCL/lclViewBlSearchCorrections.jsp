<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/tooltip/tooltip.js" ></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclCorrection.js"></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/checkLock.js"/>
<cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="lclCorrection.do">
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="correctionId" name="correctionId"/>
    <cong:hidden id="fileId" name="fileId"/>
    <cong:hidden id="fileNo" name="fileNo"/>
    <cong:hidden id="blNo" name="blNo"/>
    <cong:hidden id="concatenatedBlNo" name="concatenatedBlNo"/>
    <cong:hidden id="notesBlNo" name="notesBlNo"/>
    <cong:hidden id="selectedMenu" name="selectedMenu"/>
    <cong:hidden id="buttonValue"  name="buttonValue"/>
    <cong:hidden id="noticeNo" name="noticeNo" />
    <cong:hidden id="screenName"  name="screenName"/>
    <cong:hidden id="shipperName"  name="shipperName"/>
    <cong:hidden name="shipperNo" id="shipperNo" />
    <cong:hidden name="forwarder" id="forwarder" />
    <cong:hidden name="forwarderNo" id="forwarderNo" />
    <cong:hidden name="thirdPartyAcctNo" id="thirdPartyAcctNo" />
    <cong:hidden name="thirdPartyName" id="thirdPartyName" />
    <cong:hidden name="dateFilter" id="dateFilter" />
    <cong:hidden name="origin" id="origin" />
    <cong:hidden name="portOfOriginId" id="portOfOriginId"/>
    <cong:hidden name="pol" id="pol"  />
    <cong:hidden name="polId" id="polId" />
    <cong:hidden name="pod" id="pod" />
    <cong:hidden name="podId"  id="podId"/>
    <cong:hidden name="destination" id="destination"/>
    <cong:hidden name="finalDestinationId" id="finalDestinationId"/>
    <cong:hidden name="createdBy" id="createdBy"/>
    <cong:hidden name="createdByUserId" id="createdByUserId"/>
    <cong:hidden name="approvedBy" id="approvedBy"/>
    <cong:hidden name="approvedByUserId" id="approvedByUserId"/>
    <cong:hidden name="userId"  id="userId"/>
    <cong:hidden name="filterBy" id="filterBy"/>
    <cong:hidden name="searchCorrectionCode" id="searchCorrectionCode"/>
    <cong:hidden name="searchShipperNo" id="searchShipperNo"/>
    <cong:hidden name="searchForwarderNo" id="searchForwarderNo"/>
    <cong:hidden name="searchThirdPartyAcctNo" id="searchThirdPartyAcctNo"/>

    <input type="hidden" name="message" value="${message}"/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="99%">
            </td>
            <td width="1%">
                <input type="button" value="Go Back" align="center" class="button-style1" onclick="searchAllCorrections();"/>
            </td>
        </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr>
            <td width="80%" colspan="9">
                <div  style="height:80%;">
                    <display:table name="${lclBlCorrectionSearchList}" id="correctionDisplayTable" pagesize="100"
                                   class="dataTable" sort="list"  style="width:100%" requestURI="/lclCorrection.do?">
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
                        <display:setProperty name="paging.banner.item_name" value="Quotations"/>
                        <display:setProperty name="paging.banner.items_name" value="Quotations"/>
                        <display:column title="FileNo">
                            <div class="datatableFileNo" style="" onclick="checkLock('${path}', '${correctionDisplayTable.fileNo}', '${correctionDisplayTable.fileId}', '${correctionDisplayTable.fileState}', '${lclCorrectionForm.selectedMenu}');">
                                ${correctionDisplayTable.fileNo}
                            </div>
                        </display:column>
                        <c:if test="${lclCorrectionForm.selectedMenu=='Exports'}">
                            <display:column title="BL No" property="blNo"  ></display:column>
                        </c:if>
                        <display:column title="CN #" property="noticeNo"  ></display:column>
                        <display:column title="User"    property="userName"  ></display:column>
                        <display:column title="P" property="prepaidCollect"  ></display:column>
                        <display:column title="Sail Date">
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${correctionDisplayTable.sailDate}"/>
                            ${sailDate}
                        </display:column>
                        <display:column title="Notice Date" property="noticeDate"></display:column>
                        <c:if test="${lclCorrectionForm.selectedMenu=='Exports'}">
                            <display:column title="Current Profit" property="currentProfit"></display:column>
                            <display:column title="Profit After C/N" property="profitAfterCN"></display:column>
                        </c:if>
                        <display:column  title="C/N Code" property="correctionCode"/>
                        <display:column title="Approval" property="approval" style="color:Green;font-weight: bolder;"/>
                        <display:column title="F" ></display:column>
                        <display:column title="P" ></display:column>
                        <display:column title="C-Type">
                            <c:out value="${correctionDisplayTable.correctionType}"></c:out>&nbsp;&nbsp;
                            <c:if test="${correctionDisplayTable.status =='Q' && disabledFilter ne 'D'}">
                                <img src="${path}/img/icons/Quickcn.gif"  style="cursor:pointer" width="13" height="13" title="Quick CN"/>
                            </c:if>
                        </display:column>
                        <display:column title="Actions">
                            <c:choose>
                                <c:when test="${disabledFilter ne 'D'}">
                                    <c:if test="${correctionDisplayTable.status eq 'A' || correctionDisplayTable.status =='Q'}">
                                        <img src="${path}/img/icons/container_obj.gif" border="0" title="View" onclick="viewLclBlSearchCorrection('${correctionDisplayTable.correctionId}', '${correctionDisplayTable.fileId}');"/>   
                                    </c:if>
                                    <c:if test="${correctionDisplayTable.status eq 'A' && empty correctionDisplayTable.postedDate &&  correctionDisplayTable.status ne 'Q'}">
                                        <img src="${path}/img/icons/unapp.gif" border="0" id="disapprove${index}"
                                             onclick="unApproveUser('${correctionDisplayTable.correctionId}', '${correctionDisplayTable.blNo}',
                                                 '${correctionDisplayTable.fileId}', '${correctionDisplayTable.noticeNo}')" title="Un Approve"/>
                                    </c:if>
                                    <c:if test="${correctionDisplayTable.status ne 'A' && correctionDisplayTable.status ne 'Q' && (correctionDisplayTable.status eq 'O' && correctionDisplayTable.voidStatus ne 1)}">
                                        <img src="${path}/images/edit.png"  style="cursor:pointer" class="correctionCharge" width="13" height="13" alt="edit"
                                             title="Edit" onclick="editLclBlSearchCorrection('${correctionDisplayTable.correctionId}', '${correctionDisplayTable.fileId}');"/>
                                    </c:if>
                                    <c:if test="${correctionDisplayTable.status ne 'A' && correctionDisplayTable.status ne 'Q' && (correctionDisplayTable.status eq 'O' && correctionDisplayTable.voidStatus ne 1)}">
                                        <img src="${path}/img/icons/pa.gif" border="0"
                                             onclick="approveUser('${path}', '${correctionDisplayTable.blNo}', '${correctionDisplayTable.correctionId}',
                                                 '${correctionDisplayTable.fileId}', '${correctionDisplayTable.noticeNo}')" title="Pending Approval" />
                                    </c:if>
                                    <c:if test="${showAllFilter eq 'All' && correctionDisplayTable.voidStatus eq 1}">
                                        <img src="${path}/img/icons/lockon.ico"   border="0" title="Disabled" />
                                    </c:if>
                                </c:when>
                                <c:when test="${correctionDisplayTable.voidStatus eq 1}">
                                    <img src="${path}/img/icons/lockon.ico"   border="0" title="Disabled" />
                                </c:when>           
                            </c:choose>
                        </display:column>
                    </display:table>
                </div>
            </td></tr>
    </table>
    <br/>
</cong:form>
<script>
    jQuery(document).ready(function () {
        if (document.lclCorrectionForm.message.value != null && document.lclCorrectionForm.message.value != "")
        {
            sampleAlert(document.lclCorrectionForm.message.value);
            document.lclCorrectionForm.message.value = "";
        }
    });
    document.oncontextmenu = mischandler;
    document.onmousedown = mousehandler;
    document.onmouseup = mousehandler;
</script>