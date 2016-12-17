<div class="button-style1" onclick="goBackSearch('Are you sure you want to abort this Quote?', '${path}', '${lclQuote.lclFileNumber.id}', 'mainScreen', '${lclQuoteForm.moduleName}');">
    Go Back
</div>
<c:if test="${(moduleId=='Quotes' && lclQuote.lclFileNumber.state=='Q')!=true}">
    <div class="button-style1" id="saveQ" onclick="validateQuoteform('${lclQuoteForm.moduleName}');" title="${mandatoryField}">
        Save Quote
    </div>
    <cong:div id="buttonWithFile" styleClass="${hideShow}">
        <c:if test="${empty lclQuote.lclFileNumber.lclBooking}">
            <div class="button-style1 convertToBooking" onclick="convertToBooking('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}', '${lclQuoteForm.moduleName}');">
                ${convertButtonName}
            </div>
        </c:if>
        <div  class="${scanAttachStyle}" id="scan-Attach" onclick="scan('${lclQuote.lclFileNumber.fileNumber}', '${scanScreenName}', 'QUOTE', 'false');">Scan/Attach</div>
        <c:if test="${lclQuote.quoteComplete}">
            <div  class="button-style1" id="PrintFaxEmail" onclick="printreport('${path}', '${lclQuote.lclFileNumber.id}', '${abbrValues}${lclQuote.lclFileNumber.fileNumber}', 'LCLQuotation');">Print/Fax/Email
            </div>
        </c:if>
        <div class="${inbondStyle} inbondButton" id="inbonds" onclick="openInbond('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}');">Inbonds</div>
        <div id="notesBottom" class="${notesStyle} notes" onclick="displayNotesPopUp('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}','${lclQuoteForm.moduleName}');">
            Notes
        </div>
        <div id="copyQ"  onclick="copyQuote('The Current Quote is being Copied to new Quote. Only the Voyage Details are not carried over', '${path}', '${lclQuote.lclFileNumber.id}');"  class="button-style1">
            Copy
        </div>
        <div  class="${aesQtStyle}" id="aesQ" onclick="openAes('${path}', '${lclQuote.lclFileNumber.id}', '${lclQuote.lclFileNumber.fileNumber}');">
            AES/ITN
        </div>
        <c:if test="${lclQuote.quoteComplete eq false}">
            <div  class="button-style1" id="reportPreview" onclick="quotePreviewReport();">Preview</div>
        </c:if>
        <div style="vertical-align: middle;" class="osdValuesDisplay">
            <c:if test="${lclQuote.overShortDamaged}">
                <span>&nbsp;&nbsp;*** OSD ***</span><%-- OSD LABEL --%>
            </c:if>
            <span id="hazmatFound1" class="hazmatLabelVal"></span><%-- Hazmet LABEL --%>
            <c:if test="${not empty lclHotCodeList}"><%-- HOT CODE LABEL --%>
                <span>&nbsp;&nbsp;*** HOT CODES ***</span>
            </c:if>
        </div>
    </cong:div>

</c:if>