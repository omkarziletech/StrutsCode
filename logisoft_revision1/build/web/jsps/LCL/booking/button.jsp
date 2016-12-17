<cong:hidden name="copyRates" id="copyRates"/>
<cong:hidden name="copyRatesDrNo" id="copyRatesDrNo"/>
<div class="button-group">
    <div id="gobackbkg1" class="button-style1" onclick="goBackSearch('Are you sure you want to abort this booking?', '${path}',
                    '${lclBooking.lclFileNumber.id}', 'mainScreen', '${lclBookingForm.moduleName}');">
        Go Back
    </div>
    <c:if test="${(moduleId=='Quotes' && lclBooking.lclFileNumber.state=='B')!=true}">
        <c:choose>
            <c:when test="${lclBookingForm.moduleName!='Imports'}">
                <c:set var="mandatoryField" value="Mandatory Fields Needed<br> 1)origin CFS<BR>2)Destination<br>3)CTC/RETAIL/FTF<br>4)Term to do BL<br>5)ERT"/>
                <c:set var="saveButtonName" value="Save Bkg"/>
                <c:set var ="scanScreenName" value="LCL EXPORTS DR"/>
            </c:when>
            <c:otherwise>
                <c:set var ="scanScreenName" value="LCL IMPORTS DR"/>
                <c:set var="saveButtonName" value="Save DR"/>
                <c:set var="mandatoryField" value=""/></c:otherwise>
        </c:choose>
        <div class="button-style1" onclick="validateform('${lclBookingForm.moduleName}');" id="save" title="${mandatoryField}">
            ${saveButtonName}
        </div>
        <c:choose>
            <c:when test="${not empty lclBooking.lclFileNumber.fileNumber}">
                <c:set var="hideShow" value="showButtons"/>
            </c:when>
            <c:otherwise>
                <c:set var="hideShow" value="hideButtons"/>
            </c:otherwise>
        </c:choose>
        <cong:div id="buttonWithFile" styleClass="${hideShow}">
            <c:if test="${not empty lclBooking.lclFileNumber.lclQuote && lclBooking.lclFileNumber.state ne 'BL'}">
                <div class="button-style1" id="revert" onclick="revertToQuote('${path}', '${lclBooking.lclFileNumber.id}', 'Q');">
                    Revert To Quote
                </div>
            </c:if>
            <c:if test="${empty lclBooking.lclFileNumber.lclBl && lclBookingForm.moduleName ne 'Imports' && lclBooking.lclFileNumber.state ne 'BL'}">
                <div class="button-style1" id="convertBl" onclick="convertToBL('${path}', '${lclBooking.lclFileNumber.id}', 'BL');">
                    Convert To BL
                </div>
            </c:if>
            <c:if test="${lclBookingForm.moduleName eq 'Imports' && lclBooking.lclFileNumber.status eq 'M' && lclBooking.bookingType ne 'T'}">
                <div class="${correctionStyle eq 'true' ? "green-background":"button-style1"}" id="correctionNotice"
                     onclick="correctionNotice('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', '${lclBookingForm.moduleName}', '${lclBookingForm.headerId}');">
                    LCLCorrection
                </div>
            </c:if>
            <div  class=" ${scanAttachFlag ? "green-background":"button-style1"} disabledButton" id="scanAttach"
                  onclick="scan('${lclBooking.lclFileNumber.fileNumber}', '${scanScreenName}', 'BOOKING', 'false','${lclBooking.bookingType}');">
                Scan/Attach
            </div>
            <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                <div class="button-style1" id="lclCargoButton" onclick="showCargo('${path}', this)" >
                    Cargo Received
                </div>
                <input type="hidden" id="currentLocationId" value="${lclBooking.portOfOrigin.id}">

            </c:if>
            <div  class="button-style1 disabledButton" id="PrintFaxEmail" onclick="bkgPrint();">
                Print/Fax/Email
            </div>
            <cong:hidden name="checkDRChange" id="checkDRChange"/>
            <c:if test="${lclBooking.lclFileNumber.state eq 'Q'}">
                <div class="button-style1" id="convertBook">Convert To Booking</div>
            </c:if>
            <div class="${not empty lclInbond ? "green-background":"button-style1"} inbondButton" id="inbond" 
                 onclick="openInbond('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}');">
                Inbonds
            </div>
            <div id="notes" class="${manualNotesFlag ? "green-background":"button-style1"} notes" 
                 onclick="displayNotesPopUp('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', '${lclBookingForm.moduleName}');">
                Notes
            </div>
            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                <div class="button-style1" id="unitNotes" onclick="importUnitNotes('${path}');">Unit Notes</div>
            </c:if>
            <c:if test="${arInvoiceFlag eq 'Posted'}">
                <c:set var="arInvoiceClass" value="green-background"/>
            </c:if>
            <c:if test="${arInvoiceFlag eq 'Open'}">
                <c:set var="arInvoiceClass" value="red-background"/>
            </c:if>
            <c:if test="${arInvoiceFlag eq 'Empty' or empty arInvoiceFlag}">
                <c:set var="arInvoiceClass" value="button-style1"/>
            </c:if>
            <div id="arInvoice" class="${arInvoiceClass} invoice"
                 onclick="openLclArInvoice('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', true, '${lclBookingForm.moduleName}', '${lclBookingForm.headerId}');">
                AR Invoice
            </div>
            <c:if test="${!lclBooking.lclFileNumber.shortShip}">
                <div id="copy" onclick="copyDr('The Current DR is being Copied to new DR. Do you want to keep the same voyage info?', '${path}', '${lclBooking.lclFileNumber.id}');"
                     class="button-style1">
                    Copy
                </div>
            </c:if>
            <c:set var="aesBkgStyle" value="${aesCount ? 'green-background':'button-style1'}"/>
            <div class="${aesBkgStyle}"  id="Baes"
                 onclick="openAes('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}')">
                AES/ITN
            </div>
            <div class="button-style1 disabledButton" onclick="openTrackingPopUp('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}')">
                Tracking
            </div>
            <div class="${outsourceStyle ? "green-background":"button-style1"}" id="lcloutsourceButton"
                 onclick="outsourceUserEmail('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', '${loginuser.userId}');">
                Outsource
            </div>
            <div class="button-style1" id="lclDomTermination"
                 onclick="openTerminatepopup('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}');">
                Terminate
            </div>
            <div class="button-style1" id="lclUnTermination"
                 onclick="lclUnTerminationStatus('Are you sure you want to Restore this Booking?', '${lclBooking.lclFileNumber.id}');">
                Restore
            </div>
            <c:if test="${!lclBooking.lclFileNumber.shortShip && lclBookingForm.moduleName ne 'Imports'}">
                <div class="button-style1 shortShipBtn" id="lclShortShip" onclick="createShortShip('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}');">
                    Short Ship
                </div>
            </c:if>
            <c:if test="${lclBookingForm.moduleName ne 'Imports'}">

                <c:choose>
                    <c:when test="${lclBooking.hold == 'Y'}">
                        <div id="holdButton1" class="red-background" onclick="holdUnHold('${lclBooking.lclFileNumber.id}')">UnHold</div>
                    </c:when>
                    <c:when test="${lclBooking.hold == 'N'}">
                        <div id="holdButton1" class="green-background" onclick="holdUnHold('${lclBooking.lclFileNumber.id}')">Hold</div>
                    </c:when>
                    <c:otherwise>
                        <div id="holdButton1" class="button-style1" onclick="holdUnHold('${lclBooking.lclFileNumber.id}')">Hold</div>
                    </c:otherwise>
                </c:choose>

                <c:set var="OriginORDestination" value="${empty lclBooking.portOfOrigin.unLocationCode?lclBooking.portOfLoading.unLocationCode:
                                                          lclBooking.portOfOrigin.unLocationCode}"/>
                <!--  Regarding mantis item# 10057 please verify before remove  -->
                <div id="lclPreReleaseButton1" class="${lclBookingExport.prereleaseDatetime ne null ? 'green-background':'button-style1'}"
                     onclick="updatePreRelease('${lclBooking.lclFileNumber.id}', '${OriginORDestination}', 'PR',this);">
                    Pre-Release
                </div>
                <div id="lclReleaseButton1" class="${lclBookingExport.releasedDatetime ne null ? 'green-background':'button-style1'}" 
                     onclick="updateRelease('${lclBooking.lclFileNumber.id}', '${OriginORDestination}', 'R',this);">
                    Release
                </div>
            </c:if>
            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                <div class="button-style1" id="lclImpRrelease">Release</div>
                <c:choose>
                    <c:when test="${lclBookingPadList eq 'false'}">
                        <div class="button-style1" id="routing" class="routingInstruction"
                             onclick="openRoutingPopUp('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', 'routing');">
                            Routing Instruction
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="button-style1" id="routing" class="routingInstruction" style="background:green;color:white;"
                             onclick="openRoutingPopUp('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', 'routing');">
                            Routing Instruction</div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <div id="osdValues" style="vertical-align: right;">
                <c:if test="${lclBookingForm.moduleName eq 'Imports' and not empty lclBookingSegregation}">
                    <span class="osdValuesDisplay">SEGREGATION DR</span>
                </c:if>
                <c:if test="${lclBooking.overShortdamaged}">
                    <span class="osdValuesDisplay">&nbsp;&nbsp;*** OSD ***</span>
                </c:if>
                <span id="hazmatFound"></span>
                <c:if test="${not empty lclHotCodeList}">
                    <span class="osdValuesDisplay">&nbsp;&nbsp;*** HOT CODES ***</span>
                </c:if>
            </div>
        </cong:div>
    </c:if>
    <c:choose>
        <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
            <div style="float: right" class="textlabelsBoldforlcl">                                
                <c:set var="cfclFlag" value="${lclBookingExport.cfcl}"/>
                <lable id="cfclLabel">
                    &nbsp;&nbsp;CFCL
                </lable>
                <input type="radio" id ="cfclYes" name="cfcl"  value="Y" onclick="cfclVoyage('${path}');
                        setCfclLabelColor();" ${cfclFlag ? 'checked' :''} /> Yes
                <input type="radio" id ="cfclNo" name="cfcl"  value="N" onclick="cfclVoyage('${path}');
                        setCfclLabelColor();" ${!cfclFlag ? 'checked' :''} /> No
                &nbsp;/&nbsp;CFCL Account
                <cong:autocompletor name="cfclAcctName" template="tradingPartner" id="cfclAccount"
                                    fields="cfclAccountNo,NULL,NULL,NULL,NULL,NULL,NULL,disabledAccount,forwardAccount" position="left"
                                    styleClass="textlabelsBoldForTextBox" query="CFCL_ACCOUNT"
                                    width="600" container="NULL" shouldMatch="true" value="${lclBookingExport.cfclAcctNo.accountName}"
                                    callback="cfclAccttypeCheck();" scrollHeight="300px"/>
                Acct No
                <cong:text name="cfclAcctNo" id="cfclAccountNo" value="${lclBookingExport.cfclAcctNo.accountno}" style="align:right;width:80px"
                           container="NULL" styleClass="textlabelsBoldForTextBoxDisabledLook" readOnly="true"/>
                <input type="hidden" name="disabledAccount" id="disabledAccount"/>
                <input type="hidden" name="forwardAccount" id="forwardAccount"/>
            </div>
        </c:when>
        <c:otherwise>
            <div style="float: right" class="textlabelsBoldforlcl">
                TransShipment
                <c:choose>
                    <c:when test="${lclBookingImport.transShipment}">
                        <input type="radio" id ="transShipMentYes" name="transShipMent" value="Y" checked="yes" 
                               onchange="transhipment();toggleDoorDeliveryComment();"/>
                        Yes
                        <input type="radio" id ="transShipMentNo" name="transShipMent" value="N" 
                               onchange="transhipment();toggleDoorDeliveryComment();"/> No
                    </c:when>
                    <c:otherwise>
                        <input type="radio" id ="transShipMentYes" name="transShipMent" value="Y"
                               onchange="transhipment();toggleDoorDeliveryComment();"/> Yes
                        <input type="radio" id ="transShipMentNo" name="transShipMent" value="N"  checked="yes"
                               onchange="transhipment();toggleDoorDeliveryComment();"/> No
                    </c:otherwise>
                </c:choose>
                <input type="hidden" name="hiddenTranshipment" id="hiddenTranshipment" value="${lclBookingImport.transShipment}"/>
                <input type="hidden" name="transShipMent" id="transShipMent"/>
                <input type="hidden" name="auditedBy" value="${lclssheader.auditedBy}" id="auditedBy"/>
                <input type="hidden" name="closedBy" value="${lclssheader.closedBy}" id="closedBy"/>
            </div>
        </c:otherwise>
    </c:choose>
</div>
