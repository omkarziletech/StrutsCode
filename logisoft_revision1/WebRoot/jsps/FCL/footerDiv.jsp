<table>
    <tr>
        <td>
            <input type="button" value="Go Back" class="buttonStyleNew" onclick="backToSearch()"/>
            <input type="button" value="Save" class="buttonStyleNew" id="saveFclBlDown" onmouseover="tooltip.showTopText('<strong>${mandatoryFieldForBl}</strong>',null,event);" onmouseout="tooltip.hide();" onclick="setAction('saveFclBl')"/>
    <c:if test="${fclBlForm.fclBl.readyToPost != 'M' && fn:indexOf(fclBlForm.fclBl.fileNo,'-')<0 && showReverseToBooking}">
        <input type="button" value="ReverseToBooking" id="reverseToBooking" class="buttonStyleNew" onclick="reverseBlToBooking()"/>
    </c:if>
    <c:if test="${fclBlForm.fclBl.readyToPost != 'M' && fn:indexOf(fclBlForm.fclBl.fileNo,'-')>0}">
        <input type="button" value="Delete" id="delete" class="buttonStyleNew" onclick="backToBooking()"/>
    </c:if>
    <c:choose>
        <c:when test="${fclBlForm.fclBl.readyToPost != 'M'}">
            <input type="button" value="Manifest" class="buttonStyleNew" onclick="doManifest()" id="manifestBl"/>
        </c:when>
        <c:otherwise>
            <c:if test="${roleDuty.unmanifest}">
                <input type="button" value="UnManifest" class="buttonStyleNew" onclick="doUnManifest()" id="unManifestBl"/>
            </c:if>
            <c:choose>
                <c:when test="${empty fclBlForm.closedDate}">
                    <c:if test="${roleDuty.closeBl}">
                        <input type="button" value="Close" class="buttonStyleNew" onclick="closeOrAudit('blClose')" id="closeBl"/>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <c:if test="${roleDuty.reopenBl}">
                        <input type="button" value="Open" class="buttonStyleNew" onclick="closeOrAudit('blOpen')" id="openBl"/>
                    </c:if>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${empty fclBl.auditedDate}">
                    <c:if test="${roleDuty.audit}">
                        <input type="button" value="Audit" class="buttonStyleNew" onclick="closeOrAudit('blAudit')" id="auditBl"/>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <c:if test="${roleDuty.cancelAudit}">
                        <input type="button" value="Cancel Audit" class="buttonStyleNew" onclick="closeOrAudit('blCancelAudit')" id="cancelAuditBl"/>
                    </c:if>
                </c:otherwise>
            </c:choose>
            <c:if test="${fn:indexOf(fclBlForm.fclBl.bolId,'==')<0}">
                <input type="button" value="FCLBLCorrection"
                       <c:choose>
                    <c:when test="${not empty correction}">
                        class="buttoncolor"
                    </c:when>
                    <c:otherwise>
                        class="buttonStyleNew"
                    </c:otherwise>
                </c:choose>
                id="correctionButtonDown" onclick="return GB_show('FclBlCorrections','${path}/fclBlCorrections.do?buttonValue=FclBl&blNumber=${fclBlForm.fclBl.bolId}&fileNo=${fclBlForm.fclBl.fileNo}',550,1000);"/>
            </c:if>
        </c:otherwise>
    </c:choose>
    <c:if test="${fn:indexOf(fclBlForm.fclBl.fileNo,'-')<0}">
        <input type="button" value="Create Multi BL File" class="buttonStyleNew" onclick="createMultipleBL()" id="multipleBLDown"/>
    </c:if>
    <c:set var="manualNotesCount" value="buttonStyleNew"/>
    <c:if test="${ManualNotes}">
        <c:set var="manualNotesCount" value="buttonColor"/>
    </c:if>    
    <input type="button" value="Note" class="${manualNotesCount}" 
           onclick="return GB_show('Notes','${path}/notes.do?moduleId=FILE&moduleRefId=${fclBlForm.fclBl.fileNo}',350,750);" id="noteButtonDown"/>
    <input type="button" value="AES/ITN"
           <c:choose>
        <c:when test="${not empty bl.fclAesDetails}">
            class="buttoncolor"
        </c:when>
        <c:otherwise>
            class="buttonStyleNew"
        </c:otherwise>
    </c:choose>
    id="aesbuttonDown" onclick="GB_show('AES/ITN Details','${path}/fclAESDetails.do?buttonValue=fclbl&bolid=${fclBlForm.fclBl.bol}&fileNo=${fclBlForm.fclBl.fileNo}&ready=${fclBlForm.fclBl.readyToPost}',450,900);"/>
    <input type="button" value="Inbond"
           <c:choose>
        <c:when test="${not empty bl.fclInbondDetails}">
            class="buttoncolor"
        </c:when>
        <c:otherwise>
            class="buttonStyleNew"
        </c:otherwise>
    </c:choose>
    id="inbondDown" onclick="GB_show('Inbond Details','${path}/fclInbondDetails.do?buttonValue=fclbl&bolid=${fclBlForm.fclBl.bol}&fileNo=${fclBlForm.fclBl.fileNo}',320,800);"/>
    <input type="button" id="printFax" value="Print/Fax/Email" class="buttonStyleNew" onclick="printFaxEmail()" style="width:100px"/>
    <input type="button" value="ConfirmOnBoard"
           <c:choose>
        <c:when test="${fclBlForm.fclBl.confirmOnBoard == 'Y'}">
            class="buttoncolor"
        </c:when>
        <c:otherwise>
            class="buttonStyleNew"
        </c:otherwise>
    </c:choose>
    id="confirmOnBoardButton" onclick="confirmOnBoard()"/>
    <input type="button" value="Scan/Attach"
           <c:choose>
        <c:when test="${not empty TotalScan && TotalScan!='0'}">
            class="buttoncolor"
        </c:when>
        <c:otherwise>
            class="buttonStyleNew"
        </c:otherwise>
    </c:choose>
    id="scanAttachDown" onclick="scanOrAttach()"/>
    <input type="button" value=" AR Invoice" id="arInvoiceDown" class="buttonStyleNew" onclick="return GB_show('AR Invoice', '${path}/arRedInvoice.do?action=listArInvoice&fileNo=${fclBlForm.fclBl.fileNo}&screenName=BLfileType=${fclBlForm.fclBl.importFlag}', 550,  1100)"/>
    <input type="button" value="Send EDI" id="sendEdi1" style="visibility:  hidden" class="buttonStyleNew" onclick="generate304Xml()"/>
    <c:if test="${loginuser.role.roleDesc == 'Admin'}">
        <input type="button" value="ResendToBlue" id="resend"  class="buttonStyleNew" onclick="resendToBlueScreen()"/>
    </c:if>
</td>
</tr>
</table>
