<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:hidden name="screenType" id="screenType"/>
<input type="hidden" name="moduleId" id="moduleId" value="${lclQuote.lclFileNumber.fileNumber}"/>
<input type="hidden" name="screenName" id="screenName" value="LCL FILE"/>
<input type="hidden" name="operationType" id="operationType" value="Scan or Attach"/>
<input type="hidden" name="lockMessage" id="lockMessage" value="${lockMessage}"/>
<cong:hidden name="fileNumberId" id="fileNumberId" value="${lclQuote.fileNumberId}"/>
<cong:hidden name="fileNumber" id="fileNumber" value="${lclQuote.lclFileNumber.fileNumber}"/>
<cong:hidden name="moduleName" id="moduleName" value="${lclQuoteForm.moduleName}"/>
<cong:hidden name="copyFnVal" id="copyFnVal" value="${lclQuoteForm.copyFnVal}"/>
<input type="hidden" name="fileState" id="fileState" value="${lclQuote.lclFileNumber.state}"/>
<cong:hidden name="shipperToolTip" id="shipperToolTip" value="${lclQuoteForm.shipperToolTip}"/>
<cong:hidden name="consigneeToolTip" id="consigneeToolTip" value="${lclQuoteForm.consigneeToolTip}"/>
<cong:hidden name="notifyToolTip" id="notifyToolTip" value="${lclQuoteForm.notifyToolTip}"/>
<cong:hidden name="notify2ToolTip" id="notify2ToolTip" value="${lclQuoteForm.notify2ToolTip}"/>
<input type="hidden" id="userRoleId" name="userRoleId" value="${loginuser.role.id}">
<input type="hidden" id="loginUserId" name="loginUserId" value="${loginuser.userId}"><%-- Login User Id--%>
<input type="hidden" id="loginName" name="loginName" value="${loginuser.loginName}"><%-- Login Name--%>
<input type="hidden" name="path" id="path" value="${path}"/>
<cong:hidden name="originalFileNo" id="originalFileNo"/><%-- Quote Copying fileNo--%>
<cong:hidden id="replicateFileNumber" name="replicateFileNumber" value=""/><%-- Replicate Copying fileNo--%>
<cong:hidden id="engmet" name="fdEngmet" value="${lclQuoteForm.fdEngmet}"/><%-- Fd Engmet Values--%>
<cong:hidden id="reportSaveFlag" name="reportSaveFlag" value="${lclQuoteForm.reportSaveFlag}"/><%-- report flag--%>
<table width="100%" border="0">
    <tr>
        <td width="12%">
            <span class="textBoldforlcl">File No :</span>
    <c:if test="${not empty lclQuote.lclFileNumber.fileNumber}">
        <c:choose>
            <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
                <c:set var="originUnCode" value="${fn:substring(lclQuote.portOfOrigin.unLocationCode,2,5)}"/>
                <span class="fileNo" id="fileNumberQuote">${originUnCode}-${lclQuote.lclFileNumber.fileNumber}</span>
            </c:when>
            <c:otherwise>
                <span class="fileNo">IMP-${lclQuote.lclFileNumber.fileNumber}</span>
            </c:otherwise>
        </c:choose>
    </c:if>
</td>
<td>
    <div class="info-box" style="padding-top: 4px;margin-left: 20px;">
        <b class="textlabelsBold">&nbsp;Quote By :</b>
        <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclQuote.enteredBy.loginName}</b>
        <b class="textlabelsBold">On :</b>
        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="qCreatedTime" value="${lclQuote.enteredDatetime}"/>
        <b id="quoteOn" class="headerlabel" style="color:blue">${qCreatedTime}</b>
    </div>
<c:if test="${not empty lclQuote.lclFileNumber.lclBooking}">
    <div class="info-box" style="padding-top: 4px;">
        <b class="textlabelsBold">&nbsp;Booking By :</b>&nbsp;
        <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclQuote.lclFileNumber.lclBooking.enteredBy.loginName}</b>&nbsp
        <b class="textlabelsBold">On :</b>&nbsp;
        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="bkgCreatedTime" value="${lclQuote.lclFileNumber.lclBooking.enteredDatetime}"/>
        <b class="headerlabel" style="color:blue">${bkgCreatedTime}</b>&nbsp
    </div>
</c:if>
<c:if test="${not empty lclQuote.lclFileNumber.lclBl}">
    <div class="info-box" style="padding-top: 4px;">
        <b class="textlabelsBold">&nbsp;BL By :</b>&nbsp;
        <b class="headerlabel" style="color:blue;text-transform: uppercase">${lclQuote.lclFileNumber.lclBl.enteredBy.loginName}</b>&nbsp
        <b class="textlabelsBold">On :</b>&nbsp;
        <fmt:formatDate pattern="dd-MMM-yyyy hh:mm" var="blCreatedTime" value="${lclQuote.lclFileNumber.lclBl.enteredDatetime}"/>
        <b class="headerlabel" style="color:blue">${blCreatedTime}</b>&nbsp
    </div>
</c:if>
</td>
<c:set var="qtComplete" value="${lclQuote.quoteComplete}"/>
<td class="textlabelsBoldforlcl" valign="middle" id="quoteComplt">Quote Complete
    <input type="radio" name="quoteComplete" id="quoteCompleteY" value="Y" ${qtComplete  ? "checked":""} 
           onclick="checkQuoteCompletion('${lclQuoteForm.moduleName}')"/>Yes
    <input type="radio" name="quoteComplete" id="quoteCompleteN" value="N" ${!qtComplete ? "checked":""} />No
</td>
</tr>
<tr>
<c:choose>
    <c:when test="${lclQuoteForm.moduleName ne 'Imports'}">
        <td class="textBoldforlcl">
        <c:set var="ECI_OTI_label" value ="${lclQuoteForm.companyCode eq 'ECU' ? 'Econo' : 'OTI'}"/>
        <c:set var="ECI_OTI_value" value ="${lclQuoteForm.companyCode eq 'ECU' ? 'ECI' : 'OTI'}"/>
        Brand: <cong:radio container="null" name="businessUnit" value='${ECI_OTI_value}' 
                           id="econo" onclick="confirmBussinessUnitInQuote(this, 'eculine');" styleClass="businessUnit"/><span class="econo">${ECI_OTI_label}</span>
        <cong:radio container="null" name="businessUnit" value='ECU' id="eculine"  
                    onclick="confirmBussinessUnitInQuote(this, 'econo');" styleClass="businessUnit"/><span class="eculine">ECU Worldwide</span>
        </td>
    </c:when>
    <c:otherwise>
        <td class="textBoldforlcl">
        <c:set var="ECU_OTI_Label" value ="${lclQuoteForm.companyCode eq '03' ? 'ECI' : 'OTI'}"/>
        Brand: <cong:radio container="null" name="businessUnit" value='${lclQuoteForm.businessUnit}' 
                           id="${fn:trim(ECU_OTI_Label)}" onclick="confirmBussinessUnitInQt('${lclQuote.lclFileNumber.id}', '${ECU_OTI_Label}');"/>
        ${ECU_OTI_Label eq 'ECI' ? 'Econo' : 'OTI'}
        <cong:radio container="null" name="businessUnit" value='ECU'  id="ECU"  
                    onclick="confirmBussinessUnitInQt('${lclQuote.lclFileNumber.id}', 'ECU');"/>Ecu Worldwide
        <input hidden="oldBrand" id="oldBrand" value="${lclQuoteForm.businessUnit}"/>
        </td>
    </c:otherwise>
</c:choose>    
<td id="fileNo" class="textBoldforlcl" align="center">
    Inventory Status:<b style="color: red;" class="headerlabel">
        <label id="statuslabel">
            ${not empty lclQuote.quoteType ? "Quote" :""}
        </label>
    </b>
</td>
<td class="textBoldforlcl" style="float: right">
    Unknown Dest
<cong:radio container="null" name="nonRated" id="nonRatedY" value="Y" onclick="checkNonRated('NL')"/>Yes
<cong:radio container="null" name="nonRated" id="nonRatedN" value="N" onclick="checkNonRated('NL')"/>No
</td>
</tr>
</table>
