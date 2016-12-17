<div class="button-group">
    <div id="gobackbkg" class="button-style1" onclick="goBackSearch('Are you sure you want to abort this booking?', '${path}',
                    '${lclBooking.lclFileNumber.id}', 'mainScreen', '${lclBookingForm.moduleName}');">
        Go Back
    </div>
    <c:if test="${(moduleId=='Quotes' && lclBooking.lclFileNumber.state=='B')!=true}">
        <c:choose>
            <c:when test="${lclBookingForm.moduleName ne 'Imports'}">
                <c:set var="mandatoryField" value="Mandatory Fields Needed<br> 1)origin CFS<BR>2)Destination<br>3)CTC/RETAIL/FTF<br>4)Term to do BL<br>5)ERT"/>
                <c:set var="saveButtonName" value="Save Bkg"/>
                <c:set var ="scanScreenName" value="LCL EXPORTS DR"/>
            </c:when>
            <c:otherwise>
                <c:set var ="scanScreenName" value="LCL IMPORTS DR"/>
                <c:set var="saveButtonName" value="Save DR"/>
                <c:set var="mandatoryField" value=""/>
            </c:otherwise>
        </c:choose>
        <div class="button-style1 disabledButton" onclick="validateform('${lclBookingForm.moduleName}');" id="save" title="${mandatoryField}">${saveButtonName}</div>
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
            <c:if test="${empty lclBooking.lclFileNumber.lclBl && lclBookingForm.moduleName ne 'Imports'}">
                <div class="button-style1 bottomConvertBl" id="convertBl" onclick="convertToBL('${path}', '${lclBooking.lclFileNumber.id}', 'BL');">
                    Convert To BL
                </div>
            </c:if>
            <c:if test="${lclBookingForm.moduleName eq 'Imports' && lclBooking.lclFileNumber.status eq 'M' && lclBooking.bookingType ne 'T'}">
                <div class="${correctionStyle eq 'true' ? "green-background":"button-style1"}" id="correctionNoticeBottom"
                     onclick="correctionNotice('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', '${lclBookingForm.moduleName}', '${lclBookingForm.headerId}');">
                    LCLCorrection
                </div>
            </c:if>
            <div  class=" ${scanAttachFlag ? "green-background":"button-style1"} disabledButton" id="scan-Attach"
                  onclick="scan('${lclBooking.lclFileNumber.fileNumber}', '${scanScreenName}', 'BOOKING', 'false');">
                Scan/Attach
            </div>
            <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                <div class="button-style1" id="lclCargoButtonBottom" onclick="showCargo('${path}', this)" >
                    Cargo Received
                </div>
                <input type="hidden" id="currentLocationId" value="${lclBooking.portOfOrigin.id}">
            </c:if>
            <div  class="button-style1 disabledButton" id="PrintFaxEmail" onclick="bkgPrint();">
                Print/Fax/Email
            </div>
            <c:if test="${lclBooking.lclFileNumber.state eq 'Q'}">
                <div class="button-style1" id="convert">
                    Convert To Booking
                </div>
            </c:if>
            <div class="${not empty lclInbond ? "green-background":"button-style1"} inbondButton" id="inbonds" 
                 onclick="openInbond('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}');">
                Inbonds
            </div>
            <div id="notesBottom" class="${manualNotesFlag ? "green-background":"button-style1"} notes" 
                 onclick="displayNotesPopUp('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', '${lclBookingForm.moduleName}');">
                Notes
            </div>
            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                <div class="button-style1" id="unitNotes" onclick="importUnitNotes('${path}');">
                    Unit Notes
                </div>
            </c:if>
            <c:if test="${arInvoiceFlag eq 'Posted'}">
                <c:set var="arInvoiceClass" value="green-background"/>
            </c:if>
            <c:if test="${arInvoiceFlag eq 'Open'}">
                <c:set var="arInvoiceClass" value="red-background"/>
            </c:if>
            <c:if test="${arInvoiceFlag eq 'Empty'}">
                <c:set var="arInvoiceClass" value="button-style1"/>
            </c:if>
            <div id="arinvoice" class="${arInvoiceClass} invoice disabledButton"
                 onclick="openLclArInvoice('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', true, '${lclBookingForm.moduleName}', '${lclBookingForm.headerId}');">
                AR Invoice
            </div>
            <!--
    <div onclick="showBarrel('${path}','${lclBooking.lclFileNumber.id}','${lclBooking.lclFileNumber.fileNumber}')" class="button-style1 barrel">
        Barrels
    </div>-->
            <c:if test="${!lclBooking.lclFileNumber.shortShip}">
                <div id="copys" class="button-style1 disabledButton"
                     onclick="copyDr('The Current DR is being Copied  to new DR. Only the Voyage Details are not carried over', '${path}', '${lclBooking.lclFileNumber.id}');">
                    Copy
                </div>
            </c:if>
            <div  class="${aesBkgStyle}" id="aesB"
                  onclick="openAes('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}')" >
                AES/ITN
            </div>
            <div class="button-style1 disabledButton" onclick="openTrackingPopUp('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}')">
                Tracking
            </div>
            <div class="${outsourceStyle ? "green-background":"button-style1"} disabledButton" id="lcloutsourceButton"
                 onclick="outsourceUserEmail('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', '${loginuser.userId}');">
                Outsource
            </div>
            <div class="button-style1 disabledButton" id="lclDomTermination1"
                 onclick="openTerminatepopup('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}');">
                Terminate
            </div>
            <div class="button-style1" id="lclUnTermination1"
                 onclick="lclUnTerminationStatus('Are you sure you want to Restore this Booking?', '${lclBooking.lclFileNumber.id}');">
                Restore
            </div>
            <c:if test="${lclBookingForm.moduleName ne 'Imports'}">
                <c:if test="${!lclBooking.lclFileNumber.shortShip}">
                    <div class="button-style1 shortShipBtn" id="lclShortShip1"
                         onclick="createShortShip('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}');">
                        Short Ship
                    </div>
                </c:if>              
                <c:choose>
                    <c:when test="${lclBooking.hold == 'Y'}">
                        <div id="holdButton2" class="red-background" onclick="holdUnHold('${lclBooking.lclFileNumber.id}')">UnHold</div>
                    </c:when>
                    <c:when test="${lclBooking.hold == 'N'}">
                        <div id="holdButton2" class="green-background" onclick="holdUnHold('${lclBooking.lclFileNumber.id}')">Hold</div>
                    </c:when>
                    <c:otherwise>
                        <div id="holdButton2" class="button-style1" onclick="holdUnHold('${lclBooking.lclFileNumber.id}')">Hold</div>
                    </c:otherwise>
                </c:choose>
                <c:set var="OriginORDestination" value="${empty lclBooking.portOfOrigin.unLocationCode?lclBooking.portOfDestination.unLocationCode:
                                                          lclBooking.portOfOrigin.unLocationCode}"/>
                <!--  Regarding mantis item# 10057 please verify before remove  -->
                <div id="lclPreReleaseButton2" class="${lclBookingExport.prereleaseDatetime ne null ? 'green-background':'button-style1'}"
                     onclick="updatePreRelease('${lclBooking.lclFileNumber.id}', '${OriginORDestination}', 'PR',this);">
                    Pre-Release
                </div>
                <div id="lclReleaseButton2" class="${lclBookingExport.releasedDatetime ne null ? 'green-background':'button-style1'}"
                     onclick="updateRelease('${lclBooking.lclFileNumber.id}', '${OriginORDestination}', 'R',this);">
                    Release
                </div>
            </c:if>
            <c:if test="${lclBookingForm.moduleName eq 'Imports'}">
                <div class="button-style1" id="lclImpRreleasebot">Release</div>
                <c:choose>
                    <c:when test="${lclBookingPadList eq 'false'}">
                        <div class="button-style1" id="belowrouting" class="routingInstruction"
                             onclick="openRoutingPopUp('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', 'belowrouting');">
                            Routing Instruction
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="button-style1" id="belowrouting" class="routingInstruction" style="background:green;color:white;"
                             onclick="openRoutingPopUp('${path}', '${lclBooking.lclFileNumber.id}', '${lclBooking.lclFileNumber.fileNumber}', 'belowrouting');">
                            Routing Instruction</div>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <div id="osdValues" style="vertical-align: middle;">
                <c:if test="${lclBookingForm.moduleName eq 'Imports' and not empty lclBookingSegregation}">
                    <span class="osdValuesDisplay">SEGREGATION DR</span>
                </c:if>
                <c:if  test="${lclBooking.overShortdamaged}">
                    <span class="osdValuesDisplay">&nbsp;&nbsp;*** OSD ***</span>
                </c:if>
                <span id="hazmatFound1"></span>
                <c:if test="${not empty lclHotCodeList}">
                    <span class="osdValuesDisplay">&nbsp;&nbsp;*** HOT CODES ***</span>
                </c:if>
            </div>
        </cong:div>
    </c:if>
</div>