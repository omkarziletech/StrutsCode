<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<c:set var="fileNumberId" value="${fileNumberId}"></c:set>
<c:set var="fileNumber" value="${fileNumber}"></c:set>
<cong:div id="contactConfigDetails">
    <cong:table styleClass="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
        <c:if test="${not empty contactList}">
            <cong:tr styleClass="tableHeadingNew">Following contacts will automatically receive copy of the invoice</cong:tr>
            <cong:tr>&nbsp;</cong:tr>
            <cong:tr>&nbsp;</cong:tr>
            <cong:tr>
                <display:table name="${contactList}" id="configTableId" class="dataTable" pagesize="50">
                    <display:setProperty name="basic.msg.empty_list">
                        <span style="display:none;" class="pagebanner" />
                    </display:setProperty>
                    <display:setProperty name="paging.banner.placement" >
                        <span style="display:none;"></span>
                    </display:setProperty>
                    <display:column title="AccountName">${arRedInvoice.customerName}</display:column>
                    <display:column title="AccountNo">${arRedInvoice.customerNumber}</display:column>
                    <display:column title="AccountType">${arRedInvoice.customerType}
                        <c:if test="${not empty configTableId.subType}">
                            (${configTableId.subType})
                        </c:if>
                    </display:column>
                    <display:column title="Contact" property="firstName"></display:column>
                    <display:column title="Email" property="email"></display:column>
                    <display:column title="Fax" property="fax"></display:column>
                </display:table>
            </cong:tr>
        </c:if>
        <br/>
        <br/>
        <cong:tr>
            <cong:td width="40%"></cong:td>
            <cong:td align="center">
                <span class="button-style1" style="margin-right: 20px" onclick="sendMail('postArRedInvoice');" >Send</span>
                <span class="button-style1" onclick="doNotSendMail('postInvoiceWithoutEmail');" >Do Not Send</span>
            </cong:td>
        </cong:tr>
        <input type="hidden" name="notification" value="Send" id="notification"/>
    </cong:table>
</cong:div>
<script type="text/javascript">
    function sendMail(methodName){
        parent.$("#methodName").val(methodName);
        parent.$("#lclArInvoiceForm").submit();
        parent.$.colorbox.close();
    }
    function backToInvoiceList(path,fileId,fileNumber,listFlag){
        var url=path+"/lclArInvoice.do?methodName=display&fileNumberId="+fileId+"&fileNumber="+fileNumber+"&listFlag="+listFlag;
        window.location=url;
    }

    function doNotSendMail(methodName){
        parent.$("#methodName").val(methodName);
        parent.$("#lclArInvoiceForm").submit();
        parent.$.colorbox.close();
    }
</script>