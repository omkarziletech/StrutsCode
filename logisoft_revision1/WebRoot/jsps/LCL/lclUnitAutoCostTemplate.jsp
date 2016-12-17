<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${unitFlag eq 'UNIT_AGENT_CHR'}">
        <table width="100%">
            <tbody>
                <tr>
                    <td class="lightBoxHeader" style="background-color: #AAAAAA;">List of Unit Level Charges</td>
                </tr>
            </tbody>
        </table>
        <div class="scrolldisplaytable" style="width:100%;height:270px;overflow: auto">
            <display:table name="${autoCostList}" class="dataTable" style="width:100%;align:left" id="charge" sort="list">
                <display:setProperty name="paging.banner.some_items_found">
                    <span class="pagebanner"><font color="blue">{0}</font>
		Charges displayed,for more charges click on page numbers.
                    </span>
                </display:setProperty>
                <display:setProperty name="paging.banner.one_item_found">
                    <span class="pagebanner">One {0} displayed. page number</span>
                </display:setProperty>
                <display:setProperty name="paging.banner.all_items_found">
                    <span class="pagebanner">{0} {1} displayed, page number</span>
                </display:setProperty>
                <display:setProperty name="basic.msg.empty_list">
                    <span class="pagebanner">No Charges Found.</span>
                </display:setProperty>
                <display:setProperty name="paging.banner.placement" value="top" />
                <display:setProperty name="paging.banner.item_name" value="Charge" />
                <display:setProperty name="paging.banner.items_name" value="Charges" />
                <display:column title="Cost Code" property="chargeCode" style="text-transform:uppercase;"/>
                <display:column title="Cost Amount" property="totalCharges" style="text-transform:uppercase;"/>
                <display:column title="Vendor No" property="agentNo" style="text-transform:uppercase;"/>
            </display:table>
            <table width="100%">
                <tr><td align="center" style="font-weight: bold;font-size: 12px;color: red">Do you want to include Unit Level Charges?</td></tr>
                <tr><td align="center">
                        <input type="button" value="Yes"  class="button-style2" onclick="agentInvoicePopUp('${path}',$('#fileNumberId').val(),$('#voyageId').val(),'Yes');closePopUpDiv()"/>
                        <input type="button" value="No"  class="button-style2" onclick="agentInvoice('${path}',$('#fileNumberId').val(),$('#voyageId').val(),'No');closePopUpDiv()"/>
                    </td></tr>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <table width="100%">
            <tbody>
                <tr>
                    <td class="lightBoxHeader" style="background-color: #AAAAAA;">Auto Costing - List of Costs</td>
                </tr>
            </tbody>
        </table>
        <div class="scrolldisplaytable" style="width:100%;height:270px;overflow: auto">
            <display:table name="${autoCostList}" class="dataTable" style="width:100%;align:left" id="charge" sort="list">
                <display:setProperty name="paging.banner.some_items_found">
                    <span class="pagebanner"><font color="blue">{0}</font>
		Charges displayed,for more charges click on page numbers.
                    </span>
                </display:setProperty>
                <display:setProperty name="paging.banner.one_item_found">
                    <span class="pagebanner">One {0} displayed. page number</span>
                </display:setProperty>
                <display:setProperty name="paging.banner.all_items_found">
                    <span class="pagebanner">{0} {1} displayed, page number</span>
                </display:setProperty>
                <display:setProperty name="basic.msg.empty_list">
                    <span class="pagebanner">No Charges Found.</span>
                </display:setProperty>
                <display:setProperty name="paging.banner.placement" value="top" />
                <display:setProperty name="paging.banner.item_name" value="Charge" />
                <display:setProperty name="paging.banner.items_name" value="Charges" />
                <display:column title="Cost Code" property="chargeCode" style="text-transform:uppercase;"/>
                <display:column title="Cost Amount" property="totalIPI" style="text-transform:uppercase;"/>
                <display:column title="Vendor No" property="agentNo" style="text-transform:uppercase;"/>
            </display:table>
            <table width="100%">
                <tr><td align="center" style="font-weight: bold;font-size: 12px;color: red">Do you want to convert these to Actual Cost?</td></tr>
                <tr><td align="center">
                        <c:choose>
                            <c:when test="${unitFlag eq 'UNIT_COST'}">
                                <input type="button" value="Yes"  class="button-style2" onclick="submitForm('saveUnitAutoCost')"/>
                                <input type="button" value="No"  class="button-style2" onclick="closePopUpDiv()"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" value="Yes"  class="button-style2" onclick="saveDetailValues($('#detailId').val(),'V','AUTOCOST_YES');"/>
                                <input type="button" value="No"  class="button-style2" onclick="saveDetailValues($('#detailId').val(),'V','AUTOCOST_NO');"/>
                            </c:otherwise>
                        </c:choose>
                    </td></tr>
            </table>
        </div>
    </c:otherwise>
</c:choose>
