<table border="0" cellpadding="0" cellspacing="0" width="100%" >
    <tr>
        <td valign="top">
            <table class="tableBorderNew" border="0" width="100%" cellspacing="0" cellpadding="3" >
                <tr class="tableHeadingNew">
                    <td colspan="2">Release Clause</td>
                    <td>Special Provisions</td>
                </tr>
                <tr>
                    <td class="textlabelsBold">Release Clause</td>
                    <td>
                <html:text styleClass="textlabelsBoldForTextBox" property="fclBl.fclblClause" styleId="blClause"
                           size="5"/>
                <div id="blClause_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
        </td>
        <td class="textlabelsBold">
    <c:if test="${not empty addchargeslist}">
        Insurance &nbsp;
        <html:radio property="fclBl.insurance" value="Y"  styleId="insuranceYes" onclick="addInsuranceRate()" name="fclBlForm"></html:radio> Yes
        <html:radio property="fclBl.insurance" value="N" styleId="insuranceNo" onclick="removeInsurance()" name="fclBlForm" ></html:radio> No &nbsp;&nbsp;
    </c:if>
</td>
</tr>
<tr class="textlabelsBold">
    <td valign="top">Description</td>
    <td>
<html:textarea styleId="clauseDescription" styleClass="BackgrndColorForTextBox" readonly="true" tabindex="-1"
               cols="55" rows="3"  onkeypress="return checkTextAreaLimit(this, 500)" property="fclBl.fclblClauseDescription"  ></html:textarea>
<script type="text/javascript">
        initOPSAutocomplete("blClause","blClause_choices","clauseDescription","",
        "${path}/actions/getChargeCode.jsp?tabName=FCL_BILL_LADDING&from=0&isDojo=false","");
</script>
</td>
<td>Cost of Goods
<fmt:formatNumber pattern="##,###,##0" var="costOfGoods" value="${fclBlForm.fclBl.costOfGoods}" />
<html:text property="fclBl.costOfGoods" value="${costOfGoods}" styleClass="BackgrndColorForTextBox" readonly="true" tabindex="-1" size="7"/>
</td>
</tr>
</table>

        <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tableBorderNew">
            <tr valign="top">
                <td>
                    <!-- this date of yard -->
            <c:if test="${fclBlForm.fclBl.importFlag ne 'I'}">
                <table width="100%" border="0" style="height:100px " cellpadding="1" cellspacing="0">
                    <tr class="tableHeadingNew"><td colspan="2" height="10">Equipment Control Dates</td></tr>
                    <tr>
                        <td class="textlabelsBold" width="20%">Date out of Yard</td>
                        <td>
                    <html:text property="dateInYard"  styleClass="textlabelsBoldForTextBox" size="23"  styleId="txtcaldateOutYard"
                               onchange="validateDateOutYard(this)" />
                    <img src="${path}/img/CalendarIco.gif" alt="cal" name="caldateOutYard" width="16" height="16"
                         id="caldateOutYard" onmousedown="insertDateFromCalendar(this.id,0);" />
                    </td>
                    </tr>
                    <tr valign="top">
                        <td class="textlabelsBold" >Date Back into Yard</td>
                        <td class="textlabelsBold">
                    <html:text property="dateOutYard" styleClass="textlabelsBoldForTextBox" size="23"  styleId="txtcaldateIntoYard"
                               onchange="validateDateInYard(this)"/>
                    <img src="${path}/img/CalendarIco.gif" alt="cal" name="caldateIntoYard" width="16" height="16"
                         id="caldateIntoYard" onmousedown="insertDateFromCalendar(this.id,0);" />
                    </td>
                    </tr>
                </table>
            </c:if>
        </td>
    </tr>
</table>
</td>
<td valign="top">
    <table border="0" width="100%"  cellpadding="3" cellspacing="0" class="tableBorderNew">
        <tr class="tableHeadingNew"><td colspan="2">
        <c:choose>
            <c:when test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
                Import References
            </c:when>
            <c:otherwise>
                Export Reference
            </c:otherwise>
        </c:choose>
</td></tr>
<tr>
    <td>
        <table border="0" width="100%" cellpadding="0" cellspacing="0">
            <tr>
                <td class="textlabelsBold">
            <c:choose>
                <c:when test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
                    Import References/Predefined Remarks
                </c:when>
                <c:otherwise>
                    Export Reference/Predefined Remarks
                    <span style="padding-left:10px;">
                        <img src="${path}/img/icons/display.gif" onclick="goToRemarksLookUp()"/></span>
                </c:otherwise>
            </c:choose>
    </td>
</tr>
<tr>
<c:choose>
    <c:when test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
        <td><html:textarea styleClass="BackgrndColorForTextBox" styleId="exportReference" readonly="true" tabindex="-1"
                           property="fclBl.exportreference" onkeypress="return checkTextAreaLimit(this, 250)" rows="3" cols="80"
                           style="text-transform: uppercase"/>
        </td>
    </c:when>
    <c:otherwise>
        <td><html:textarea styleClass="textlabelsBoldForTextBox" styleId="exportReference"
                           property="fclBl.exportreference" onkeypress="return checkTextAreaLimit(this, 250)" rows="3" cols="80"
                           style="text-transform: uppercase"/>
        </td>
    </c:otherwise>
</c:choose>
</tr>
<tr>
<c:choose>
    <c:when test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
        <td class="textlabelsBold">Domestic Routing/Predefined Remarks
            <span style="padding-left:10px;">
                <img src="${path}/img/icons/display.gif" onclick="goToRemarksLookUpforImport(true)"/></span>
        </td>
    </c:when>
    <c:otherwise>
        <td class="textlabelsBold">Domestic Routing </td>
    </c:otherwise>
</c:choose>
</tr>
<tr>
    <td valign="top">
<html:textarea styleClass="textlabelsBoldForTextBox"  property="fclBl.domesticrouting"
               rows="3" cols="80" onkeyup="limitTextarea(this,3,42)"
               styleId="domesticRouting" />
</td>
</tr>
<tr>
    <td class="textlabelsBold">
<c:choose>
    <c:when test="${null ne fclBlForm.fclBl.importFlag && fclBlForm.fclBl.importFlag eq 'I'}">
        Onward Inland Routing/Cargo Location &nbsp;&nbsp;&nbsp;
        <img src="${path}/img/icons/display.gif" onclick="openImportWareHousePopUp()"/>
    </c:when>
    <c:otherwise>
        Onward Inland Routing
    </c:otherwise>
</c:choose>
</td>
</tr>
<tr>
    <td class="textlabelsBold" valign="top">
        <span class="textlabelsBold">
            <html:textarea property="fclBl.onwardInlandRouting" styleClass="textlabelsBoldForTextBox" styleId="onwardInlandRouting"
                           onkeypress="return checkTextAreaLimit(this, 100)" rows="3" cols="80"
                           style="text-transform: uppercase" ></html:textarea>
        </span>
    </td>
</tr>
</table>
</td>
</tr>
</table>
</td>
</tr>
</table>