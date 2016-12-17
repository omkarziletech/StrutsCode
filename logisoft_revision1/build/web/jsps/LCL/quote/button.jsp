<table width="100%">
    <tr>
        <td>
            <div class="button-style1" onclick="goBackSearch('Are you sure you want to abort this Quote?', '${path}', '${lclQuote.lclFileNumber.id}', 'mainScreen', '${lclQuoteForm.moduleName}');">
                Go Back
            </div>
    <c:choose>
        <c:when test="${lclQuoteForm.moduleName!='Imports'}">
            <c:set var="mandatoryField" value="Mandatory Fields Needed<br> 1)origin CFS<BR>2)Destination<br>3)CTC/RETAIL/FTF<br>4)Term to do BL<br>5)ERT"/>
            <c:set var="convertButtonName" value="Convert To Booking"/>
            <c:set var ="scanScreenName" value="LCL EXPORTS DR"/>
        </c:when>
        <c:otherwise>
            <c:set var ="scanScreenName" value="LCL IMPORTS DR"/>
            <c:set var="mandatoryField" value=""/>
            <c:set var="convertButtonName" value="Convert To DR"/></c:otherwise>
    </c:choose>
    <c:if test="${(moduleId=='Quotes' && lclQuote.lclFileNumber.state=='Q')!=true}">
        <div class="button-style1" onclick="validateQuoteform('${lclQuoteForm.moduleName}');" id="Qsave" title="${mandatoryField}">
            Save Quote
        </div>
        <c:choose>
            <c:when test="${not empty lclQuote.lclFileNumber.fileNumber}">
                <c:set var="hideShow" value="showButtons"/>
            </c:when>
            <c:otherwise>
                <c:set var="hideShow" value="hideButtons"/>
            </c:otherwise>
        </c:choose>
        <cong:div id="buttonWithFile" styleClass="${hideShow}">
            <c:if test="${empty lclQuote.lclFileNumber.lclBooking}">
                <div class="button-style1 convertToBooking" id="QconvertBook" onclick="convertToBooking('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}', '${lclQuoteForm.moduleName}');">
                    ${convertButtonName}
                </div>
            </c:if>
            <c:set var="scanAttachStyle" value="${(null!=scanAttachCount && scanAttachCount!='0') ? 'green-background' :'button-style1'}"/>
            <div  class="${scanAttachStyle}" id="scanAttach" onclick="scan('${lclQuote.lclFileNumber.fileNumber}', '${scanScreenName}', 'QUOTE', 'false');">
                Scan/Attach
            </div>
            <c:if test="${lclQuote.quoteComplete}">
                <c:choose>
                    <c:when test="${lclQuoteForm.moduleName eq 'Imports'}">
                        <c:set var="abbrValues" value="IMP-"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="abbrValues" value="${originUnCode}-"/>
                    </c:otherwise>
                </c:choose>
                <div  class="button-style1" id="PrintFaxEmail" onclick="printreport('${path}', '${lclQuote.lclFileNumber.id}', '${abbrValues}${lclQuote.lclFileNumber.fileNumber}', 'LCLQuotation');">
                    Print/Fax/Email
                </div>
            </c:if>
            <c:set var="inbondStyle" value="${not empty lclInbond ? 'green-background' :'button-style1'}"/>
            <div class="${inbondStyle} inbondButton" id="inbond" onclick="openInbond('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}');">Inbonds</div>
            <c:set var="notesStyle" value="${manualNotesFlag ? 'green-background' :'button-style1'}"/>
            <div id="notes" class="${notesStyle} notes" onclick="displayNotesPopUp('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}','${lclQuoteForm.moduleName}');">Notes</div>
            <div id="Qcopy" onclick="copyQuote('The Current Quote is being Copied  to new Quote. Only the Voyage Details are not carried over', '${path}', '${lclQuote.lclFileNumber.id}');"  class="button-style1">
                Copy
            </div>
            <c:set var="aesQtStyle" value="${aesCount ? 'green-background':'button-style1'}"/>
            <div  class="${aesQtStyle}" id="Qaes" onclick="openAes('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}');">AES/ITN</div>
            <c:if test="${lclQuote.quoteComplete eq false}">
                <div  class="button-style1" id="reportPreview" onclick="quotePreviewReport();">Preview</div>
            </c:if>

            <div style="vertical-align: middle;" class="osdValuesDisplay">
                <c:if test="${lclQuote.overShortDamaged}">
                    <span>*** OSD ***</span>
                </c:if>
                <span id="hazmatFound" class="hazmatLabelVal"></span>
                <c:if test="${not empty lclHotCodeList}">
                    <span>&nbsp;&nbsp;*** HOT CODES ***</span>
                </c:if>
            </div>
        </cong:div>
    </c:if>
</td>
<td>
<c:choose>
    <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
        <div style="float: right" class="textBoldforlcl">
            &nbsp;&nbsp;CFCL
            <c:set var="cfclFlag" value="${lclBookingExport.cfcl}"/>
            <input type="radio" id ="cfclYes" name="cfcl" value="Y" onclick="cfclVoyage()" ${cfclFlag ? 'checked' :''} /> Yes
            <input type="radio" id ="cfclNo" name="cfcl" value="N" onclick="cfclVoyage()" ${!cfclFlag ? 'checked' :''} /> No
            &nbsp;/&nbsp;CFCL Account
            <cong:autocompletor name="cfclAcctName" template="tradingPartner" id="cfclAccount"
                                fields="cfclAccountNo,NULL,NULL,NULL,NULL,NULL,NULL,disabledAccount,forwardAccount" position="left"
                                styleClass="textlabelsBoldForTextBox" query="CFCL_ACCOUNT"  width="600" container="NULL" shouldMatch="true" value="${lclBookingExport.cfclAcctNo.accountName}"
                                callback="cfcl_AccttypeCheck();" scrollHeight="300px"/>
            Acct No
            <cong:text name="cfclAcctNo" id="cfclAccountNo" value="${lclBookingExport.cfclAcctNo.accountno}" style="align:right;width:80px"
                       container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
            <input type="hidden" name="disabledAccount" id="disabledAccount"/>
            <input type="hidden" name="forwardAccount" id="forwardAccount"/>
        </div>
    </c:when>
    <c:otherwise>
        <div style="float: right" class="textBoldforlcl">
            TransShipment
            <c:choose>
                <c:when test="${lclQuoteImport.transShipment==true}">
                    <cong:radio id ="transShipMentYes" container="null" name="transShipMent" value="Y" checked="yes" onclick="checkTranshipment();deleteAutoRates();"/> Yes
                    <cong:radio id ="transShipMentNo" container="null" name="transShipMent" value="N"  onclick="checkTranshipment();calculateCharge('','','');"/> No
                </c:when>
                <c:otherwise>
                    <cong:radio id ="transShipMentYes" container="null" name="transShipMent" value="Y" onclick="checkTranshipment();deleteAutoRates();"/> Yes
                    <cong:radio id ="transShipMentNo" container="null" name="transShipMent" value="N"  checked="yes" onclick="checkTranshipment();calculateCharge('','','');"/> No
                </c:otherwise>
            </c:choose>
        </div>
    </c:otherwise>
</c:choose>
</tr>
</table>