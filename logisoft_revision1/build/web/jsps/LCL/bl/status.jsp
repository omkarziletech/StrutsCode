<%-- 
    Document   : status
    Created on : Oct 24, 2012, 11:11:38 AM
    Author     : logiware
--%>
<div id="booking-info">
    <table border="0">
        <tr>
            <td width="10%">
                <table>
                    <tr class="textBoldforlcl">
                        <td>File No :
                            <span class="fileNo">
                                ${fileNumberPrefix}
                            </span>
                        </td>
                    </tr>
                </table>
            </td>
            <td>
        <c:if test="${not empty lclBl.lclFileNumber.lclQuote}">
            <div class="info-box" style="padding-top: 4px;">
                <b class="textlabelsBold">&nbsp;Quote By :</b>&nbsp;
                <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclBl.lclFileNumber.lclQuote.enteredBy.loginName}</b>&nbsp
                <b class="textlabelsBold">On :</b>&nbsp;
                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="qtCreatedTime" value="${lclBl.lclFileNumber.lclQuote.enteredDatetime}"/>
                <b class="headerlabel" style="color:blue">${qtCreatedTime}</b>&nbsp
            </div>
        </c:if>
        <c:if test="${not empty lclBl.lclFileNumber.lclBooking}">
            <div class="info-box" style="padding-top: 4px;">
                <b class="textlabelsBold">&nbsp;Booking By :</b>&nbsp;
                <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclBl.lclFileNumber.lclBooking.enteredBy.loginName}</b>&nbsp
                <b class="textlabelsBold">On :</b>&nbsp;
                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="bkgCreatedTime" value="${lclBl.lclFileNumber.lclBooking.enteredDatetime}"/>
                <b class="headerlabel" style="color:blue">${bkgCreatedTime}</b>&nbsp
            </div>
        </c:if>
        <c:if test="${not empty lclBl.lclFileNumber}">
            <div class="info-box" style="padding-top: 4px;">
                <b class="textlabelsBold">&nbsp;BL By :</b>&nbsp;
                <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclBl.enteredBy.loginName}</b>&nbsp
                <b class="textlabelsBold">On :</b>&nbsp;
                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="blCreatedTime" value="${lclBl.enteredDatetime}"/>
                <b class="headerlabel" style="color:blue">${blCreatedTime}</b>&nbsp
            </div>
        </c:if>
        </td>

        <td width="5%">
            <table class="tableBorderNewForLcl">
                <tr class="textBoldforlcl">
                <cong:td styleClass="textlabelsBoldforlcl">Term to do BL</cong:td>
                <cong:td>
                    <cong:div>
                        <cong:autocompletor id="terminal" name="terminal" template="commTempFormat" styleClass="mandatory textlabelsBoldForTextBox" query="TERM_BL" fields="NULL,trmnum"
                                            width="500" container="NULL"   value="${lclBlForm.terminal}" shouldMatch="true" scrollHeight="200px" callback="updateTerminal();"/>
                        <cong:hidden name="trmnum" id="trmnum" value="${lclBlForm.trmnum}"/>
                    </cong:div>
                </cong:td>
                <td style="color:green;font-weight: bold;font-size: 12px" width="5%">BL Owner :
                    <b  class="headerlabel">
                        <cong:autocompletor name="blOwnerName" fields="blOwnerId" id="blOwner" template="one" query="BL_PERSON" position="left"
                                            width="200" container="NULL" scrollHeight="200px" styleClass="textLCLuppercase textlabelsLclBoldForBl"
                                            value="${lclBl.blOwner.loginName}" shouldMatch="true"/>
                        <cong:hidden name="blOwnerId" id="blOwnerId" value="${lclBl.blOwner.id}"/>
                    </b>
                </td>
                </tr>
            </table>
        </td>

        </tr>
    </table>
    <table border="0" width="100%">
        <tr>
            <td class="textBoldforlcl" width="10%">Brand : 
                <b  class="headerlabel" style="color:red;font-size: 12px">${brandName}
                    </span>
            </td>

        <c:if test="${lclBl.lclFileNumber.status eq 'M'}">
            <td width="90%">
                <table class="tableBorderNewForLcl">
                    <tr class="textBoldforlcl">
                        <td>Manifested By :<b  class="headerlabel" style="color:blue;text-transform: uppercase">${lclBlForm.manifestedBy}</b></td>
                        <td style="padding-left:5px;">On :
                            <b class="headerlabel" style="color:blue;text-transform: uppercase"><c:out value="${lclBlForm.manifestedOn}"></c:out></b></td>
                        <td>
                            <b class="headerlabel" style="color:black;text-transform: uppercase">Voyage#:</b><b class="headerlabel" style="color:blue;text-transform: uppercase">${lclBlForm.voyageNumber}</b>
                        </td>
                        <td>
                            <b class="headerlabel" style="color:black;text-transform: uppercase">Unit#:</b><b class="headerlabel" style="color:blue;text-transform: uppercase">${lclBlForm.unitNumber}</b>
                        </td>

                    </tr>
                </table>
            </td>
        </c:if>

        </tr>
    </table>
</div>
