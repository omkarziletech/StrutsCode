<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>

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
                <input type="button" value="Yes" class="button-style2" onclick="approvingVoy('${voyNo}', '${id}', '${cntrnNo}', 'Y', '${screenFlag}')"/>
                <input type="button" value="No" class="button-style2" onclick="approvingVoy('${voyNo}', '${id}', '${cntrnNo}', 'N', '${screenFlag}')"/>
            </td></tr>
    </table>
</div>
<script type="text/javascript">
    function approvingVoy(voyNo, id, cntrNo, flag, screenFlag) {
        showLoading();
        $("#voyNo").val(voyNo);
        if (screenFlag === 'container') {
            $("#ecuId").val(id);
        } else {
            $("#id").val(id);
        }
        $("#containerNo").val(cntrNo);
        $("#methodName").val('approveVoy');
        $("#autoCostFlag").val(flag);
        $("#lclEculineEdiForm").submit();
    }
</script>
