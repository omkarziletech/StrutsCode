<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/js/tooltip/tooltip.js" ></cong:javascript>
<cong:javascript src="${path}/jsps/LCL/js/lclCorrection.js"></cong:javascript>
<cong:form name="lclCorrectionForm" id="lclCorrectionForm" action="lclCorrection.do">
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="selectedMenu" name="selectedMenu"/>
    <cong:hidden id="correctionId" name="correctionId"/>
    <cong:hidden id="fileId" name="fileId" />
    <cong:hidden id="fileNo" name="fileNo" />
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="80%">
                VOIDED Correction List FileNo:- ${lclCorrectionForm.fileNo}
            </td>
            <td width="20%">
            </td>
        </tr>
        <tr>
            <td width="80%" colspan="9">
                <div  style="height:80%;">
                    <c:set var="index" value="1"/>
                    <display:table name="${lclVoidBlCorrectionList}" id="correctionDisplayTable" pagesize="100"
                                   class="dataTable" sort="list"  style="width:100%" defaultorder="descending"
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
                        <display:column title="CN #" property="noticeNo"  ></display:column>
                        <display:column title="User"    property="userName"  ></display:column>
                        <display:column title="P" property="prepaidCollect"  ></display:column>
                        <display:column title="Sail Date">
                            <fmt:formatDate pattern="dd-MMM-yyyy" var="sailDate" value="${correctionDisplayTable.sailDate}"/>
                            ${sailDate}
                        </display:column>
                        <display:column title="Notice Date" property="noticeDate"/>
                        <display:column  title="C/N Code" property="correctionCode"/>
                        <display:column title="Approval" property="approval" style="color:Green;font-weight: bolder;"/>
                        <display:column title="F" />
                        <display:column title="P" />
                        <display:column title="C-Type" property="correctionType"/>
                        <display:column title="Who Voided"  property="whoVoided"/>
                        <display:column title="Voided Date" property="voidedDate"/>
                        <display:column title="Actions">
                                     <img src="${path}/img/icons/container_obj.gif" border="0" title="View" onclick="viewVoidCorrection('${correctionDisplayTable.correctionId}');"/>
                                     <img src="${path}/img/icons/send.gif" border="0" title="Print/Fax/Email"
                                          onclick="PrintReportsOpenSeperately('${correctionDisplayTable.correctionId}','${correctionDisplayTable.noticeNo}','','V')"/>
                        </display:column>
                    <c:set var="index" value="${index+1}"/>
                    </display:table>
                </div>
            </td></tr>
    </table>
    <br/>
</cong:form>