<cong:table border="0">
    <tr class="caption">
        <td colspan="6"  align="left"></td>
        <td align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Client</td>
        <td  align="right"  colspan="5"></td>
    </tr>
    <tr>
    <cong:td styleClass="textlabelsBoldforlcl">Client</cong:td>
    <cong:td width="10%">
        <cong:table id="clientAutoCom">
            <jsp:include page="/jsps/LCL/ajaxload/client.jsp"/>
        </cong:table>
        <cong:hidden name="acctType" id="acct_type"/>
        <cong:hidden name="subType" id="sub_type"/>
        <cong:hidden name="city" id="city"/>
        <cong:hidden name="state" id="state"/>
        <cong:hidden name="zip" id="zip"/>
    </cong:td>
    <cong:td>
        <input type="checkbox" name="clientWithConsignee" style="vertical-align: middle;"
               id="clientWithConsignee" onclick="showConsigneeForClient()"
               title="Checked=Consignee Listed <br> unChecked=consignee not Listed"/>
        <input type="checkbox" name="clientWithoutConsignee" style="vertical-align: middle;"
               id="clientManual" onclick="newClient();"/>New
        <c:if test="${empty lclBooking.lclFileNumber}">
            <img src="${path}/img/icons/copy.gif" alt="Look Up" width="16" height="16" style="vertical-align: middle;"
                 class="replicateBkgClient" title="Replicate Client Booking"
                 id="replicateClient" onclick="openReplicateBkg('${path}', $('#client').val(), $('#client_no').val())"/>
        </c:if>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">Acct No </cong:td>
    <cong:td>
        <cong:text name="clientAcct" id="client_no" value="${lclBooking.clientAcct.accountno}" container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">Contact Email</cong:td>
    <cong:td>
        <cong:text name="clientContact.email1" id="email" styleClass="textlabelsBoldForTextBox" container="NULL" value="${lclBooking.clientContact.email1}" maxlength="100"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl ">Address </cong:td>
    <cong:td>
        <cong:text name="clientContact.address" id="address" styleClass="textlabelsBoldForTextBox  textuppercaseLetter" container="NULL" value="${lclBooking.clientContact.address}"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">OTI #</cong:td>
    <cong:td>
        <cong:text name="otiNumber" id="otiNumber" container="NULL"  maxlength="250" styleClass="textlabelsBoldForTextBox" value="${lclBookingForm.otiNumber}"/>
    </cong:td>
</tr>
<cong:tr>
    <cong:td styleClass="textlabelsBoldforlcl">Contact Name</cong:td>
    <cong:td>
        <cong:text id="contactName" value="${lclBooking.clientContact.contactName}" styleClass="textlabelsBoldForTextBox" name="clientContact.contactName" maxlength="250"  container="NULL"/>
    </cong:td>
    <cong:td>
        <img src="${path}/jsps/LCL/images/display.gif" alt="Look Up" width="16" height="16" id="contactR" onclick="openImportContact('${path}', $('#client').val(), $('#client_no').val(), $('#contactName').val(), 'LCL_IMPORT_CLIENT')"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">Phone</cong:td>
    <cong:td>
        <cong:text name="clientContact.phone1" id="phone" container="NULL"  maxlength="250" styleClass="textlabelsBoldForTextBox" value="${lclBooking.clientContact.phone1}"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl"> Fax</cong:td>
    <cong:td>
        <cong:text name="clientContact.fax1" id="fax" value="${lclBooking.clientContact.fax1}" container="NULL" maxlength="50" styleClass="textlabelsBoldForTextBox"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">FMC#</cong:td>
    <cong:td>
        <cong:text name="fwdFmcNo" id="fmcNumber" container="NULL" maxlength="50" value="${lclBooking.fwdFmcNo}" styleClass="textlabelsBoldForTextBox"/>
    </cong:td>
    <cong:td styleClass="textlabelsBoldforlcl">Commodity#</cong:td>
    <cong:td>
        <cong:text name="commodityNumber" id="commodityNumber" container="NULL" maxlength="50" value="${lclBookingForm.commodityNumber}" styleClass="textlabelsBoldForTextBox"/>
    </cong:td>
</cong:tr>
</cong:table>
