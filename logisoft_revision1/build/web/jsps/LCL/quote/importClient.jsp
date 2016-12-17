<table>
    <tr class="caption">
        <td colspan="6"  align="left"></td>
        <td align="left">Client</td>
        <td  align="right"  colspan="5"></td>
    </tr>
    <tr>
    <cong:td styleClass="textlabelsBoldforlcl">Client</cong:td>
    <cong:td width="9%">
        <cong:table id="clientAutoCom">
            <jsp:include page="/jsps/LCL/ajaxload/clientQuote.jsp"/>
        </cong:table>
        <cong:hidden name="city" id="city"/>
        <cong:hidden name="state" id="state"/>
        <cong:hidden name="zip" id="zip"/>
    </cong:td>
    <cong:td>
        <input type="checkbox" name="clientWithConsignee" id="clientWithConsignee" style="vertical-align: middle;"
               onclick="showConsigneeForClient();" title="Checked=Consignee Listed <br/>unChecked=consignee not Listed"/>
        <input type="checkbox" name="clientWithoutConsignee" style="vertical-align: middle;" id="clientManual" onclick="newClient();" title="New"/>
        <c:choose>
            <c:when test="${lclQuoteForm.clientIcon}">
                <img src="${path}/img/icons/e_contents_view1.gif" style="vertical-align: middle;" width="14" height="14" title="Click here to see Client Notes"
                     id="clntNotes" class="clntNotes" onclick="displayNotes('${path}', $('#client_no').val(), 'clntNotes', $('#fileId').val(), $('#fileNo').val());"/>
            </c:when>
            <c:otherwise>
                <img src="${path}/img/icons/e_contents_view.gif" style="vertical-align: middle;" width="14" height="14" title="Click here to see Client Notes"
                     id="clntNotes" class="clntNotes" onclick="displayNotes('${path}', $('#client_no').val(), 'clntNotes', $('#fileId').val(), $('#fileNo').val());"/>
            </c:otherwise>
        </c:choose>
        <img alt="" src="${path}/images/icons/search_filter.png" style="vertical-align: middle;" class="clientSearchEdit" title="Click here to Client Search options" onclick="showClientSearchOption('${path}', 'Client')"/>
        <c:if test="${empty lclQuote.lclFileNumber}">
            <img src="${path}/img/icons/copy.gif" onclick="openClient('${path}', $('#client').val(), $('#client_no').val());"
                 alt="Look Up" width="16" height="16" style="vertical-align: middle;" id="replicateClient"/>
        </c:if>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">Acct No </cong:td>
    <cong:td>
        <cong:text name="clientAcct" id="client_no" value="${lclQuote.clientAcct.accountno}" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">Contact Email</cong:td>
    <cong:td>
        <cong:text name="clientContact.email1" id="email" styleClass="textlabelsBoldForTextBox" container="NULL" value="${lclQuote.clientContact.email1}" maxlength="100"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">Address </cong:td>
    <cong:td>
        <cong:text name="clientContact.address" id="address" styleClass="textlabelsBoldForTextBox" container="NULL" value="${lclQuote.clientContact.address}"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">OTI #</cong:td>
    <cong:td>
        <cong:text name="otiNumber" id="otiNumber" container="NULL"  maxlength="250" styleClass="textlabelsBoldForTextBox" value="${lclQuoteForm.otiNumber}"/>
    </cong:td>
</tr>
<tr>
<cong:td styleClass="textlabelsBoldforlcl">Contact Name</cong:td>
<cong:td>
    <cong:text id="contactName" value="${lclQuote.clientContact.contactName}" styleClass="textlabelsBoldForTextBox" name="clientContact.contactName" maxlength="250"/>
</cong:td>
<cong:td>
    <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="contactR" 
         onclick="openImportContact('${path}', $('#client').val(), $('#client_no').val(), $('#contactName').val(), 'LCL_IMPORT_CLIENT');"/>
</cong:td>
<cong:td styleClass="textlabelsBoldforlcl">Phone</cong:td>
<cong:td>
    <cong:text name="clientContact.phone1" id="phone" container="NULL"  maxlength="250" styleClass="textlabelsBoldForTextBox" value="${lclQuote.clientContact.phone1}"/>
</cong:td>
<cong:td styleClass="textlabelsBoldforlcl"> Fax</cong:td>
<cong:td>
    <cong:text name="clientContact.fax1" id="fax" value="${lclQuote.clientContact.fax1}" container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox"/>
</cong:td>
<cong:td styleClass="textlabelsBoldforlcl">FMC#</cong:td>
<cong:td>
    <cong:text name="fwdFmcNo" id="fmcNumber" container="NULL" maxlength="50" value="${lclQuote.fwdFmcNo}" styleClass="textlabelsBoldForTextBox"/>
</cong:td>
<cong:td styleClass="textlabelsBoldforlcl">Commodity#</cong:td>
<td>
<cong:text name="commodityNumber" id="commodityNumber" container="NULL" maxlength="50" value="${lclQuoteForm.commodityNumber}" styleClass="textlabelsBoldForTextBox"/>
</td>
</tr>
</table>