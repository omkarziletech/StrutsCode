<%-- 
    Document   : buttonBottom
    Created on : Oct 24, 2012, 11:19:28 AM
    Author     : logiware
--%>
<div class="button-style1" onclick="goBackSearch('${path}', '${lclBlForm.bookingFileNumberId}', 'mainScreen');">
    GO Back
</div>
<div class="button-style1" onclick="submitBlForm('saveBl');" id="saveR">
    Save BL
</div>
<div class="button-style1" onclick="revertToBooking('${path}', '${lclBlForm.bookingFileNumberId}', 'B');" id="revertR">
    Revert To Booking
</div>
<c:if test="${lclBlForm.blUnitCob=='true'}">
    <div class="${correctionClassName} disabledBtn" id="correctionNoticeBottom"
         onclick="blCorrection('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}', '${lclBlForm.blNumber}', '${moduleId}');">
        LCLBLCorrection
    </div>
</c:if>
<div  class="${scanAttachFlag ? "green-background":"button-style1"}"
      id="scanAttach1" onclick="scan('${lclBl.lclFileNumber.fileNumber}', 'LCL FILE', 'BL', 'false')">
    Scan/Attach
</div>
<div  class="button-style1" id="PrintFaxEmail" onclick=" printExportBLReport('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}', 'LCLBL', '', '')">
    Print/Fax/Email
</div>
<div class="${not empty lclInbond ? "green-background":"button-style1"} inbondButton" id="inbonds"
     onclick="openInbond('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}');">
    Inbonds
</div>
<div class="button-style1 notes" id="notes"
     onclick="displayNotesPopUp('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}', 'Exports');">
    Notes
</div>
<div id="invoiceR" class="${arInvoiceClass} invoice" onclick="openLclArInvoice('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}', true)">
    AR Invoice
</div>

<%--  Wrongly added this copydr button  in  bl section.
   <div id="copy" onclick="copyDr('The Current DR is being Copied  to new DR. Only the Voyage Details are not carried over',
       '${path}', '${lclBl.lclFileNumber.id}')"  class="button-style1">
       Copy
   </div>--%>
<div class="${not empty aesList ? 'green-background':'button-style1'} aes_button" 
     onclick="openAes('${path}', '${lclBl.lclFileNumber.id}', '${lclBl.lclFileNumber.fileNumber}')">
    AES/ITN
</div>
<input type="button" name="noEeiAes" id="noEeiAesbottom" value="NOEEI-LV" class="button-style1" onclick="noEeiAestoAdd('${lclBl.lclFileNumber.id}');"/>
<input type="button" id="bottom_postButton" class="${postedBy ? 'green-background': 'button-style1'}"
       onclick="processPostedunPosted('${roleDutyForUnPost}');" value="Post"/>
<c:if test="${voidBtnFlag}">
    <input type="button" id="bottom_voidButton" class="button-style1 void_Button"
           onclick="Voided_And_UnVoided()" value="Void"/>
</c:if>

<%--  <div class="button-style1" id="sendLclEdi" style="visibility: hidden;" onclick="generate304XmlLCL()">
Send EDI
</div>--%>
<div>
    <span class="textBoldforlcl">BL No :</span>
    <span class="fileNo concatedBlNo">${lclBlForm.blNumber}</span>&nbsp;
</div>