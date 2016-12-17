<table width="100%">
    <tr><td align="center">
            <input type="button" class="buttonStyleNew" value="Submit" onclick="selectContactsFromPopup();"/>
            <input type="button" class="buttonStyleNew" value="Cancel" onclick="closeContactsPopup();"/>
            <input type="button" class="buttonStyleNew showContacts" value="Show All Contacts" onclick="showAllContact(this);" id="showAll"/>
            <input type="button" value="Code Definitions" class="buttonStyleNew questIcon" onclick="openContactCodePdf('${contextPath}');"/>
        </td>
    </tr>
    <tr>
        <td id="customerCodeCContact" style="display: none">
    <display:table name="${codeCContactList}"  class="displaytagstyleNew" id="codeCContactList"
                   style="width:100%">
        <display:setProperty name="basic.msg.empty_list"> <span style="display:none;" class="pagebanner" /></display:setProperty>
        <display:setProperty name="paging.banner.placement" value="false"><span style="display:none;"></span></display:setProperty>
        <!-- this wil not display any message when table is empty , not number of recrds -->
        <display:column title="">
            <input type="checkBox" id="customerContact" name="customerContact"
                   value="${codeCContactList.email} == ${codeCContactList.fax}"/>
        </display:column>
        <display:column title="CustomerName" maxLength="20">
            ${codeCContactList.accountName} / ${codeCContactList.accountNo}
        </display:column>
        <display:column title="CustType">
            ${codeCContactList.accountType}
            <c:if test="${!empty codeCContactList.subType}">
                (${codeCContactList.subType})
            </c:if>
        </display:column>
        <display:column title="FirstName" property="firstName"></display:column>
        <display:column title="LastName" property="lastName"></display:column>
        <display:column title="Email" property="email"></display:column>
        <display:column title="Fax" property="fax"></display:column>
        <display:column title="CodeC" property="codec.code"></display:column>
    </display:table>
</td>
</tr>
<tr>
    <td id="customerCodeJContact" style="display: none">
<display:table name="${codeJContactList}"  class="displaytagstyleNew" id="codeJContactList"
               style="width:100%">
    <display:setProperty name="basic.msg.empty_list"> <span style="display:none;" class="pagebanner" /></display:setProperty>
    <display:setProperty name="paging.banner.placement" value="false"><span style="display:none;"></span></display:setProperty>
    <!-- this wil not display any message when table is empty , not number of recrds -->
    <display:column title="">
        <input type="checkBox" id="customerContact" name="customerContact"
               value="${codeJContactList.email} == ${codeJContactList.fax}"/>
    </display:column>
    <display:column title="CustomerName" maxLength="20">
        ${codeJContactList.accountName} / ${codeJContactList.accountNo}
    </display:column>
    <display:column title="CustType">
        ${codeJContactList.accountType}
        <c:if test="${!empty codeJContactList.subType}">
            (${codeJContactList.subType})
        </c:if>
    </display:column>
    <display:column title="FirstName" property="firstName"></display:column>
    <display:column title="LastName" property="lastName"></display:column>
    <display:column title="Email" property="email"></display:column>
    <display:column title="Fax" property="fax"></display:column>
    <display:column title="CodeJ" property="codej.code"></display:column>
</display:table>
</td>
</tr>
<tr>
    <td id="customerCodeFContact" style="display: none">
<display:table name="${codeFContactList}"  class="displaytagstyleNew" id="codeFContactList" style="width:100%">
    <display:setProperty name="basic.msg.empty_list"> <span style="display:none;" class="pagebanner" /></display:setProperty>
    <display:setProperty name="paging.banner.placement" value="false"><span style="display:none;"></span></display:setProperty>
    <!-- this wil not display any message when table is empty , not number of recrds -->
    <display:column title="">
        <input type="checkBox" id="customerContact" name="customerContact"
               value="${codeFContactList.email} == ${codeFContactList.fax}"/>
    </display:column>
    <display:column title="CustomerName" maxLength="25">
        ${codeFContactList.accountName} / ${codeFContactList.accountNo}</display:column>
    <display:column title="CustType">
        ${codeJContactList.accountType}
        <c:if test="${!empty codeFContactList.subType}">
            (${codeFContactList.subType})
        </c:if>
    </display:column>
    <display:column title="FirstName" maxLength="10">${codeFContactList.firstName}</display:column>
    <display:column title="LastName" maxLength="10">${codeFContactList.lastName}</display:column>
    <display:column title="Email" property="email" style="width: 200px"></display:column>
    <display:column title="Fax" property="fax"></display:column>
    <display:column title="CodeF" property="codef.code"></display:column>
</display:table>
</td>
</tr>
<tr>
    <td id="customerAllContact" style="display: none">
<display:table name="${customerContactList}"  class="displaytagstyleNew" id="customerContactList" style="width:100%">
    <display:setProperty name="basic.msg.empty_list"> <span style="display:none;" class="pagebanner" /></display:setProperty>
    <display:setProperty name="paging.banner.placement" value="false"><span style="display:none;"></span></display:setProperty>
    <!-- this wil not display any message when table is empty , not number of recrds -->
    <display:column title="">
        <input type="checkBox" id="customerContact" name="customerContact"
               value="${customerContactList.email} == ${customerContactList.fax}"/>
    </display:column>
    <display:column title="CustomerName" maxLength="20" style="width: 100px;">
        ${customerContactList.accountName} / ${customerContactList.accountNo}
    </display:column>
    <display:column title="CustType">
        ${customerContactList.accountType}
        <c:if test="${!empty customerContactList.subType}">
            (${customerContactList.subType})
        </c:if>
    </display:column>
    <display:column title="FirstName" maxLength="10">${customerContactList.firstName}</display:column>
    <display:column title="LastName" maxLength="10">${customerContactList.lastName}</display:column>
    <display:column title="Email" property="email"></display:column>
    <display:column title="Fax" property="fax"></display:column>
    <c:choose>
        <c:when test="${screenName eq 'LCLBooking' || screenName eq 'LCLQuotation'}">
            <display:column title="CodeJ" property="codej.code"></display:column>
        </c:when>
        <c:otherwise>
            <c:choose><c:when test="${screenName eq 'LCLIMPBooking' || screenName eq 'LCLUnits'}">
                    <display:column title="CodeF" property="codef.code"></display:column>
                </c:when><c:otherwise>
                    <display:column title="CodeC" property="codec.code"></display:column>
                </c:otherwise> </c:choose>
        </c:otherwise>
    </c:choose>
</display:table>
</td>
</tr>
<tr><td align="center">
        <input type="button" class="buttonStyleNew" value="Submit" onclick="selectContactsFromPopup();"/>
        <input type="button" class="buttonStyleNew" value="Cancel" onclick="closeContactsPopup();"/>
        <input type="button" class="buttonStyleNew showContacts" value="Show All Contacts" onclick="showAllContact(this);" id="showAll1"/>
        <input type="button" value="Code Definitions" class="buttonStyleNew questIcon" onclick="openContactCodePdf('${contextPath}');"/>
    </td>
</tr>
</table>