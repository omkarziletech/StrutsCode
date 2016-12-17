<%-- 
    Document   : button
    Created on : Oct 24, 2012, 11:12:04 AM
    Author     : logiware
--%>
<table border="0" width="100%" >
    <tr>
        <td width="60%">
            <div class="button-style1" onclick="goBackSearch('${path}', '${lclBlForm.bookingFileNumberId}', 'mainScreen');">
                Go Back
            </div>
            <div class="button-style1" onclick="submitBlForm('saveBl');" id="save">
                Save BL
            </div>
            <div class="button-style1" id="revert" onclick="revertToBooking('${path}', '${lclBlForm.bookingFileNumberId}', 'B');">
                Revert To Booking
            </div>
            <c:if test="${lclBlForm.blUnitCob=='true'}">
                <div class="${correctionClassName} disabledBtn" id="correctionNotice"
                     onclick="blCorrection('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}', '${lclBlForm.blNumber}', '${moduleId}');">
                    LCLBLCorrection
                </div>
            </c:if>
            <div  class="${scanAttachFlag ? "green-background":"button-style1"}"  id="scan-attach"
                  onclick="scan('${lclBl.lclFileNumber.fileNumber}', 'LCL FILE', 'BL', 'false')">
                Scan/Attach
            </div>
            <div  class="button-style1" id="PrintFaxEmail" onclick=" printExportBLReport('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}', 'LCLBL', '', '');">
                Print/Fax/Email
            </div>
            <div class="${not empty lclInbond ? "green-background":"button-style1"} inbondButton" id="inbond"
                 onclick="openInbond('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}');">
                Inbonds
            </div>
            <div class="button-style1 notes" id="notes"
                 onclick="displayNotesPopUp('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}', 'Exports' );">
                Notes
            </div>
            <c:if test="${arInvoiceFlag eq 'Posted'}">
                <c:set var="arInvoiceClass" value="green-background"/>
            </c:if>
            <c:if test="${arInvoiceFlag eq 'Open'}">
                <c:set var="arInvoiceClass" value="red-background"/>
            </c:if>
            <c:if test="${arInvoiceFlag eq 'Empty' or empty arInvoiceFlag}">
                <c:set var="arInvoiceClass" value="button-style1"/>
            </c:if>
            <div id="arInvoice" class="${arInvoiceClass} invoice" onclick="openLclArInvoice('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}', true, 'Exports')">
                AR Invoice
            </div>

            <%--  Wrongly added this copydr button  in  bl section.
             <div id="copy" onclick="copyDr('The Current DR is being Copied  to new DR. Only the Voyage Details are not carried over',
                 '${path}', '${lclBl.lclFileNumber.id}')"  class="button-style1">
                 Copy
             </div>--%>
            <c:set var="voidBtnFlag" value="false"/>
            <c:choose>
                <c:when test="${lclBl.lclFileNumber.status eq 'M'}">
                    <c:if test="${role.voidLCLBLafterCOB}">
                        <c:set var="voidBtnFlag" value="true"/>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <c:set var="voidBtnFlag" value="true"/>
                </c:otherwise>
            </c:choose>
            <div  class="${not empty aesList ? 'green-background':'button-style1'} aes_button"
                  onclick="openAes('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}')">
                AES/ITN
            </div>
         <input type="button" name="noEeiAes" id="noEeiAes" value="NOEEI-LV" class="button-style1" onclick="noEeiAestoAdd('${lclBl.lclFileNumber.id}');"/>
            <input type="button" id="postButton" class="${postedBy ? 'green-background': 'button-style1'}"
                   onclick="processPostedunPosted('${roleDutyForUnPost}');" value="Post"/>
            <c:if test="${voidBtnFlag}">
                <input type="button" id="voidButton" class="button-style1 void_Button"
                       onclick="Voided_And_UnVoided()" value="Void"/>
            </c:if>
        </td>

        <td width="30%">
            <span class="textBoldforlcl">BL No :</span>
            <span class="fileNo concatedBlNo">${lclBlForm.blNumber}</span>&nbsp;

            <span class="textBoldforlcl">Rate Type :</span><span id="selectedRateType" style='color:blue;font-weight:bold;'>
                ${lclBl.rateType eq 'R' ? "Retail" : lclBl.rateType eq 'C' ? "Coload" :"Foreign to Foreign"}
                ${rateType}</span>
        </td>
        <td width="10%" align="center">
            <input type="hidden" id="manifestFlagId" value="${lclBl.lclFileNumber.status}" />
            <c:set var="poolPostedStatus" value="${lclBl.lclFileNumber.status eq 'M' ? 'MANIFESTED' : lclBl.lclFileNumber.status eq 'V' ? 'Voided'
                                                   :  postedBy ? 'POSTED':'POOL'}"/>
            <c:set var="poolPostedStyle" value="${lclBl.lclFileNumber.status eq 'M' ? 'greenBold14px' : lclBl.lclFileNumber.status eq 'V' ? 'greenBold14px'
                                                  :postedBy ? 'purpleBold' : 'orangeBold'}"/>
            <span class="${poolPostedStyle}" id="poolPostedData" >${poolPostedStatus}
                ${lclBlForm.blUnitCob eq 'true' && lclBl.lclFileNumber.status eq 'M' ? '/COB':''}
            </span>

        </td>
    </tr>
</table>