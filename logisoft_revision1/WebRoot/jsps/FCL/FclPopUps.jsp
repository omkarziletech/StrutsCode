<div id="contactConfigDetails" style="display:none;height:200px;overflow-y: auto;">
    <table class="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
        <tr><td style="color: red">${invoiceMessage}</td></tr>
        <c:if test="${not empty ContactConfigE1andE2}">
            <tr class="tableHeadingNew"><font style="font-weight: bold">Freight Invoice has been sent to</font></tr>
            <tr><td>
            <display:table name="${ContactConfigE1andE2}" id="configTableId" class="displaytagstyle" pagesize="20">
                <display:setProperty name="basic.msg.empty_list">
                    <span style="display:none;" class="pagebanner" />
                </display:setProperty>
                <display:setProperty name="paging.banner.placement" >
                    <span style="display:none;"></span>
                </display:setProperty>

                <c:choose>
                    <c:when test="${not empty configTableId.accountNo}">
                        <display:column title="AccountName" property="accountName"></display:column>
                        <display:column title="AccountNo" property="accountNo"></display:column>
                        <display:column title="AccountType">${configTableId.accountType}
                            <c:if test="${not empty configTableId.subType}">
                                (${configTableId.subType})
                            </c:if>
                        </display:column>
                        <display:column title="FirstName" property="firstName"></display:column>
                        <display:column title="LastName" property="lastName"></display:column>
                        <display:column title="Email" property="email"></display:column>
                        <display:column title="Fax" property="fax"></display:column>
                        <display:column title="CodeK" property="codek.code"></display:column>
                        <display:column title="Action">
			    <c:choose>
				<c:when test="${configTableId.accountingSelected}">
				    <input type="checkbox" name="freightInvoiceContactIds" value="${configTableId.id}" checked disabled/>
				</c:when>
				<c:otherwise>
				    <input type="checkbox" name="freightInvoiceContactIds" value="${configTableId.id}" checked/>
				</c:otherwise>
			    </c:choose>
			</display:column>
                    </c:when>
                    <c:otherwise>
                        <display:column title="AccountName" style="font-weight:bolder;background:pink" property="accountName"></display:column>
                        <display:column title="AccountNo" style="font-weight:bolder;background:pink" property="accountNo"></display:column>
                        <display:column title="AccountType" style="font-weight:bolder;background:pink">
                            ${configTableId.accountType}${configTableId.subType}
                            <c:if test="${not empty configTableId.subType}">
                                (${configTableId.subType})
                            </c:if>
                        </display:column>
                        <display:column title="FirstName" style="font-weight:bolder;background:pink" property="firstName"></display:column>
                        <display:column title="LastName" style="font-weight:bolder;background:pink" property="lastName"></display:column>
                        <display:column title="Email" style="font-weight:bolder;background:pink" property="email"></display:column>
                        <display:column title="Fax" style="font-weight:bolder;background:pink" property="fax"></display:column>
                        <display:column title="CodeK" style="font-weight:bolder;background:pink">BookingEmail</display:column>
                        <display:column title="Action" style="font-weight:bolder;background:pink">
			</display:column>
                    </c:otherwise>
                </c:choose>
            </display:table>
            </td></tr>
            <tr><td>&nbsp;</td>
            </tr>
            <tr>
                <td align="center">
                    <input class="buttonStyleNew" type="button" value="Ok" onclick="sendFrieghtInvoice()">
<!--                    <input class="buttonStyleNew" type="button" value="Do Not Send" onclick="doNotSendFrieghtInvoice()">-->
                </td>
            </tr>
        </c:if>
        <c:if test="${empty ContactConfigE1andE2}">
            <tr>
                <td align="center">
                    <input class="buttonStyleNew" type="button" value="OK" onclick="frieghtInvoiceWillNotSent()">
                </td>
            </tr>
        </c:if>
    </table>
</div>
<div id="containerCommentBox" style="display:none;width:300px;height:150px;" align="center">
     <table class="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
         <tr class="tableHeadingNew"> Comments</tr>
             <tr>
                    <td align="center">
                       <html:textarea property="comment" styleId="comment" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"  cols="30" rows="3"/>
                    </td>
             </tr>
             <tr><td>&nbsp;</td></tr>
             <tr>
                <td align="center">
                    <input class="buttonStyleNew" type="button" value="Save" onclick="enableDisableContainer()">
                    <input class="buttonStyleNew" type="button" value="Cancel" onclick="closeCommentPopUp()">
                </td>
            </tr>
    </table>
</div>
<div id="containerCommentBoxForAes" class="static-popup" style="display:none;width:300px;height:150px;" align="center">
     <table class="tableBorderNew" cellpadding="3" cellspacing="0"  width="100%">
         <tr class="tableHeadingNew"> Comments</tr>
             <tr>
                    <td align="center">
                       <html:textarea property="aesComment" styleId="aesComment" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"  cols="30" rows="3"/>
                    </td>
             </tr>
             <tr><td>&nbsp;</td></tr>
             <tr>
                <td align="center">
                    <input class="buttonStyleNew" type="button" value="Ok" onclick="enableDisableContainerForAes()">
                    <input class="buttonStyleNew" type="button" value="Cancel" onclick="closeCommentPopUpForAes()">
                </td>
            </tr>
    </table>
</div>