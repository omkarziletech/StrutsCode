<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>

<script type="text/javascript">
    $(document).ready(function () {
        $('#saveCode').click(function () {
            var chargesCode = $('#chargesCode').val();
            if (chargesCode == null || chargesCode == "") {
                $.prompt('Code is required');
                $("#chargesCode").css("border-color", "red");
                return false;
            }
            var costAmount = $('#costAmount').val();
            if (costAmount == null || costAmount == "" || costAmount == '0.00') {
                $.prompt('Cost Amount is required and must be greater than 0.00');
                $('#costAmount').css("border-color", "red");
                return false;
            }
            var thirdPartyname = $('#thirdPartyname').val();
            if (thirdPartyname == null || thirdPartyname == undefined || thirdPartyname == "") {
                $.prompt('Vendor is required');
                $("#thirdPartyname").css("border-color", "red");
            } else if ($("#moduleName").val() === 'Exports' && !validateGlMapping(chargesCode)) {
                return false;
            } else {
                showLoading();
                if ($("#saveDrCostFlag").val() == "true") {
                    submitForm('saveDrCost');
                } else if ($("#saveDrCostFlag").val() == "false") {
                    submitForm('saveCost');
                }
            }
        });
        var costStatus = $('#costStatus').val();
        if (costStatus === 'AP') {
            $("#costAmount").addClass("textlabelsBoldForTextBoxDisabledLook");
            $('#costAmount').attr("readonly", true);
        } else {
            $("#valueOfGoods").removeClass("textlabelsBoldForTextBoxDisabledLook");
            $('#costAmount').attr("readonly", false);
        }
    });

    function allowNegativeNumbers(obj) {
        var result;
        if (!/^-?\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }

</script>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left; " id="fullDiv">
        <cong:form name="lclUnitCostChargeForm" id="lclUnitCostChargeForm" action="/lclImpUnitCostCharge">
            <input type="hidden" id="headerId" name="headerId" value="${lclUnitCostChargeForm.headerId}"/>
            <input type="hidden" id="unitSsId" name="unitSsId" value="${lclUnitCostChargeForm.unitSsId}"/>
            <input type="hidden" id="unitId" name="unitId" value="${lclUnitCostChargeForm.unitId}"/>
            <input type="hidden" id="autoCostFlag" name="autoCostFlag" value="${lclUnitCostChargeForm.autoCostFlag}"/>
            <input type="hidden" id="cfsWarehouseId" name="cfsWarehouseId" value="${lclUnitCostChargeForm.cfsWarehouseId}"/>
            <input type="hidden" id="unitTypeId" name="unitTypeId" value="${lclUnitCostChargeForm.unitTypeId}"/>
            <input type="hidden" id="closedTime" name="closedTime" value="${lclUnitCostChargeForm.closedTime}"/>
            <input type="hidden" id="auditedTime" name="auditedTime" value="${lclUnitCostChargeForm.auditedTime}"/>
            <cong:hidden id="cobStatus" name="cobStatus" value="${lclUnitCostChargeForm.cobStatus}"/>
            <cong:hidden id="hazFlag" name="hazFlag" value="${lclUnitCostChargeForm.hazFlag}"/>
            <cong:hidden id="moduleName" name="moduleName" value="${lclUnitCostChargeForm.moduleName}"/>
            <cong:hidden id="unitNo" name="unitNo" />
            <cong:hidden id="unitSSAcId" name="unitSSAcId" />
            <cong:hidden id="bookingAcId" name="bookingAcId" />
            <cong:hidden id="saveDrCostFlag" name="saveDrCostFlag" />
            <cong:hidden name="costStatus"  id="costStatus" value='${lclUnitCostChargeForm.costStatus}' />
            <c:choose>
                <c:when test="${lclUnitCostChargeForm.moduleName eq 'Exports'}">
                    <c:set var="shipmentType" value="LCLE"/>
                </c:when>
                <c:otherwise> 
                    <c:set var="shipmentType" value="LCLI"/>
                </c:otherwise>
            </c:choose>
            <cong:table border="0">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td colspan="9"> Cost for Unit# <span class="fileNo">${lclUnitCostChargeForm.unitNo}</span> </cong:td>
                    <cong:td colspan="3">
                        <c:if test="${shipmentType eq 'LCLE'}">
                            <input type="button" id="faeCost" class="button-style1" 
                                   value="Calc FAE" onclick="showFAECost('${path}')"/>
                        </c:if>  
                        <c:if test="${lclUnitCostChargeForm.closedTime eq '' && lclUnitCostChargeForm.auditedTime eq ''}">
                            <input type="button" id="autoCost" class="button-style1" value="Auto Cost" onclick="calculateUnitAutoCost()"/>
                        </c:if>
                        <c:choose>
                            <c:when test="${groupByInvoiceFlag eq 'false'}">
                                <div style="margin-right:30px" class="button-style1" id="groupByInvoice"  onclick="showGroupByInvoice('true');">Group by Invoice#</div>
                            </c:when>
                            <c:otherwise>
                                <div  style="margin-right:30px" class="button-style1" id="groupByDR" onclick="showGroupByDR('false');">Group by D/R </div>
                            </c:otherwise>
                        </c:choose>

                    </cong:td>
                </cong:tr>
                <cong:tr><cong:td colspan="12"></cong:td></cong:tr>
                <c:choose>
                    <c:when test="${lclUnitCostChargeForm.moduleName eq 'Exports'}">
                       <%-- <c:if test="${lclUnitCostChargeForm.auditedTime eq ''}"> --%>
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Code</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclUnitCostChargeForm.saveDrCostFlag eq 'true' }">
                                            <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="COST_CODE_UNIT" fields="NULL,chargesCodeId"
                                                                params="${shipmentType}" container="NULL" styleClass="text mandatory require text-readonly" disabled="true" width="500" shouldMatch="true" scrollHeight="150"/>
                                        </c:when>
                                        <c:otherwise> 
                                            <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="COST_CODE_UNIT" fields="NULL,chargesCodeId"
                                                                params="${shipmentType}" container="NULL" styleClass="text mandatory require" width="500" shouldMatch="true" scrollHeight="150"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <cong:hidden name="chargesCodeId" id="chargesCodeId"/>
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Cost Amount</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text twoDigitDecFormat mandatory" style="width:76px" name="costAmount" id="costAmount" onkeyup="allowNegativeNumbers(this);"/>
                                </cong:td>
                                <cong:td></cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Vendor Name</cong:td>
                                <cong:autocompletor  name="thirdPartyname"  id="thirdPartyname" fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdpartyDisabled,NULL,NULL,NULL,NULL,NULL,thirdparyDisableAcct"
                                                     callback="vendorTypeCheck();" shouldMatch="true" width="530" query="VENDOR"
                                                     template="tradingPartner" scrollHeight="150" styleClass="text mandatory require"
                                                     paramFields="vendorSearchState,vendorSearchZip,vendorSearchSalesCode,vendorCountryUnLocCode">

                                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle;"
                                         title="Click here to edit House Consignee Search options" onclick="showClientSearchOption('${path}', 'Vendor');"/>
                                </cong:autocompletor>
                                <input type="hidden" name="thirdpartyDisabled" id="thirdpartyDisabled"/>
                                <input type="hidden" name="thirdparyDisableAcct" id="thirdparyDisableAcct"/>
                                <input type="hidden" name="vendorSearchState" id="vendorSearchState"/>
                                <input type="hidden" name="vendorSearchZip" id="vendorSearchZip"/>
                                <input type="hidden" name="vendorSearchSalesCode" id="vendorSearchSalesCode"/>
                                <input type="hidden" name="vendorSearchCountry" id="vendorSearchCountry"/>
                                <input type="hidden" name="vendorCountryUnLocCode" id="vendorCountryUnLocCode"/>
                                <cong:td styleClass="textlabelsBoldforlcl">Vendor#</cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">
                                    <cong:text  name="thirdpartyaccountNo" id="thirdpartyaccountNo" style="width:80px" styleClass="text-readonly" readOnly="true"/>
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Invoice Number</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclUnitCostChargeForm.saveDrCostFlag eq 'true' }">
                                            <cong:text styleClass="text textuppercaseLetter text-readonly"  maxlength="50" disabled="true"  name="invoiceNumber" id="invoiceNumber" />
                                        </c:when>
                                        <c:otherwise>
                                            <cong:text styleClass="text textuppercaseLetter"  maxlength="50"  name="invoiceNumber" id="invoiceNumber" />
                                            <input type="checkbox" name="lastInvoice" id="lastInvoice" style="vertical-align: middle;"
                                                   title="Repeat Last Invoice#" onclick="lastInvoiceNo()"/>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                                <cong:hidden  name="lastInvoiceNumber" id="lastInvoiceNumber" value='${lclUnitCostChargeForm.lastInvoiceNumber}' />
                                <cong:hidden  name="lastVendorName" id="lastVendorName" value='${lclUnitCostChargeForm.lastVendorName}'  />
                                <cong:hidden  name="lastVendorNumber" id="lastVendorNumber" value='${lclUnitCostChargeForm.lastVendorNumber}'  />
                                <cong:td width="4%"></cong:td>
                            </cong:tr>
                                        <%--  </c:if> --%>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${lclUnitCostChargeForm.closedTime eq '' && lclUnitCostChargeForm.auditedTime eq ''}">
                            <cong:tr>
                                <cong:td styleClass="textlabelsBoldforlcl">Code</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclUnitCostChargeForm.saveDrCostFlag eq 'true' }">
                                            <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="COST_CODE" fields="NULL,chargesCodeId"
                                                                params="${shipmentType}" container="NULL" styleClass="text mandatory require text-readonly" disabled="true" width="500" shouldMatch="true" scrollHeight="150"/>
                                        </c:when>
                                        <c:otherwise> 
                                            <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="COST_CODE" fields="NULL,chargesCodeId"
                                                                params="${shipmentType}" container="NULL" styleClass="text mandatory require" width="500" shouldMatch="true" scrollHeight="150"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <cong:hidden name="chargesCodeId" id="chargesCodeId"/>
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Cost Amount</cong:td>
                                <cong:td>
                                    <cong:text styleClass="text twoDigitDecFormat mandatory" style="width:76px" name="costAmount" id="costAmount" onkeyup="allowNegativeNumbers(this);"/>
                                </cong:td>
                                <cong:td></cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Vendor Name</cong:td>
                                <cong:autocompletor  name="thirdPartyname"  id="thirdPartyname" fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdpartyDisabled,NULL,NULL,NULL,NULL,NULL,thirdparyDisableAcct"
                                                     callback="vendorTypeCheck();" shouldMatch="true" width="530" query="VENDOR"
                                                     template="tradingPartner" scrollHeight="150" styleClass="text mandatory require"
                                                     paramFields="vendorSearchState,vendorSearchZip,vendorSearchSalesCode,vendorCountryUnLocCode">

                                    <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit" style="vertical-align: middle;"
                                         title="Click here to edit House Consignee Search options" onclick="showClientSearchOption('${path}', 'Vendor');"/>
                                </cong:autocompletor>
                                <input type="hidden" name="thirdpartyDisabled" id="thirdpartyDisabled"/>
                                <input type="hidden" name="thirdparyDisableAcct" id="thirdparyDisableAcct"/>
                                <input type="hidden" name="vendorSearchState" id="vendorSearchState"/>
                                <input type="hidden" name="vendorSearchZip" id="vendorSearchZip"/>
                                <input type="hidden" name="vendorSearchSalesCode" id="vendorSearchSalesCode"/>
                                <input type="hidden" name="vendorSearchCountry" id="vendorSearchCountry"/>
                                <input type="hidden" name="vendorCountryUnLocCode" id="vendorCountryUnLocCode"/>
                                <cong:td styleClass="textlabelsBoldforlcl">Vendor#</cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">
                                    <cong:text  name="thirdpartyaccountNo" id="thirdpartyaccountNo" style="width:80px" styleClass="text-readonly" readOnly="true"/>
                                </cong:td>
                                <cong:td styleClass="textlabelsBoldforlcl">Invoice Number</cong:td>
                                <cong:td>
                                    <c:choose>
                                        <c:when test="${lclUnitCostChargeForm.saveDrCostFlag eq 'true' }">
                                            <cong:text styleClass="text textuppercaseLetter text-readonly"  maxlength="50" disabled="true"  name="invoiceNumber" id="invoiceNumber" />
                                        </c:when>
                                        <c:otherwise>
                                            <cong:text styleClass="text textuppercaseLetter"  maxlength="50"  name="invoiceNumber" id="invoiceNumber" />
                                            <input type="checkbox" name="lastInvoice" id="lastInvoice" style="vertical-align: middle;"
                                                   title="Repeat Last Invoice#" onclick="lastInvoiceNo()"/>
                                        </c:otherwise>
                                    </c:choose>
                                </cong:td>
                                <cong:hidden  name="lastInvoiceNumber" id="lastInvoiceNumber" value='${lclUnitCostChargeForm.lastInvoiceNumber}' />
                                <cong:hidden  name="lastVendorName" id="lastVendorName" value='${lclUnitCostChargeForm.lastVendorName}'  />
                                <cong:hidden  name="lastVendorNumber" id="lastVendorNumber" value='${lclUnitCostChargeForm.lastVendorNumber}'  />
                                <cong:td width="4%"></cong:td>
                            </cong:tr></c:if>
                    </c:otherwise>                
                </c:choose>
            </cong:table><br>
            <c:choose>
                <c:when test="${lclUnitCostChargeForm.moduleName eq 'Exports'}">
                   <%-- <c:if test="${lclUnitCostChargeForm.auditedTime eq ''}"> --%>
                        <cong:table align="center" width="100%" border="0">
                            <cong:tr>
                                <cong:td width="42%" > </cong:td>
                                <cong:td width="50%" >
                                    <input type="button" class="button-style1" value="Save Cost" id="saveCode"/>
                                    <input type="button" class="button-style1" value="Cancel" id="cancel" onclick="resetForm();"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    <%--</c:if>--%>
                </c:when>
                <c:otherwise>
                    <c:if test="${lclUnitCostChargeForm.closedTime eq '' && lclUnitCostChargeForm.auditedTime eq ''}">
                        <cong:table align="center" width="100%" border="0">
                            <cong:tr>
                                <cong:td width="42%" > </cong:td>
                                <cong:td width="50%" >
                                    <input type="button" class="button-style1" value="Save Cost" id="saveCode"/>
                                    <input type="button" class="button-style1" value="Cancel" id="cancel" onclick="resetForm();"/>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </c:if>   
                </c:otherwise>
            </c:choose>

            <c:set var="unitCostTotal" value="${0}"/>
            <c:if test="${not empty lclUnitSSAcList && groupByInvoiceFlag ne 'true'}">
                <div id="search-results" class="head-tag font-14px">
                    Unit Cost
                </div>
                <div style="width:100%;overflow:auto;" id="drDiv">
                    <table class="dataTable">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Cost Code</th>
                                <th>Cost Desc</th>
                                <th>Auto Cost</th>
                                <th>Vendor</th>
                                <th>Invoice#</th>
                                <th>Status</th>
                                <th>Date</th>
                                <th>Cost Amount</th>
                                <th>User</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="lclUnitSSAc" items="${lclUnitSSAcList}">
                                <c:choose>
                                    <c:when test="${rowStyle eq 'oddStyle'}">
                                        <c:set var="rowStyle" value="evenStyle"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="rowStyle" value="oddStyle"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr class="${rowStyle}">
                                    <td>
                                        <c:if test="${lclUnitSSAc.manualEntry}">
                                            <img alt="Manual Charge" title="Manual Charge"style="vertical-align: middle" src="${path}/img/icons/asterikPurple.png" width="10" height="10" />
                                        </c:if>
                                    </td>
                                    <td>${lclUnitSSAc.apGlMappingId.chargeCode}</td>
                                    <td>${lclUnitSSAc.apGlMappingId.chargeDescriptions}</td>
                                    <td>
                                        ${lclUnitSSAc.manualEntry ? "No" :"Yes"}
                                    </td>

                                    <td>${lclUnitSSAc.apAcctNo.accountno}</td>

                                    <td>
                                        <span class="purpleBold11px">
                                            ${lclUnitSSAc.apReferenceNo}
                                        </span>
                                    </td>
                                    <td>${lclUnitSSAc.apTransType}</td>
                                    <td style="text-transform: uppercase;">
                                        <fmt:formatDate value="${lclUnitSSAc.modifiedDatetime}"
                                                        pattern="dd-MMM-yyyy hh:mm" var="modfieddate"/>
                                        ${modfieddate}
                                    </td>
                                    <td>
                                        ${lclUnitSSAc.apAmount}
                                        <c:set var="unitCostTotal" value="${unitCostTotal+lclUnitSSAc.apAmount}"/>
                                        <span style="color: #008000">${lclUnitSSAc.totalCostByInvoiceNo}</span>
                                    </td>
                                    <td style="text-transform: uppercase;">${lclUnitSSAc.modifiedByUserId.loginName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${lclUnitCostChargeForm.moduleName eq 'Exports'}">
                                                <img alt="Scan/Attach" title="Scan View" class="scanView" style="vertical-align: top"
                                                     src="${path}/img/icons/preview.gif" onclick="showScanOrAttach('${path}', '${lclUnitSSAc.apAcctNo.accountno}', '${lclUnitSSAc.apReferenceNo}', '')"/>
                                                <%-- <c:if test="${lclUnitCostChargeForm.auditedTime eq ''}"> --%>
                                                    <c:if test="${lclUnitSSAc.apTransType=='AC'}">
                                                        <img src="${path}/images/edit.png"  style="cursor:pointer"  style="vertical-align: middle" class="cost" alt="edit"
                                                             onclick="editCost('${lclUnitSSAc.id}', '${lclUnitSSAc.apTransType}');"
                                                             title="Edit Cost" width="13px" height="13px"/>
                                                    </c:if>
                                                    <c:if test="${lclUnitSSAc.apTransType=='AC' && lclUnitSSAc.apTransType!='DS'}">
                                                        <img src="${path}/images/error.png" style="cursor:pointer" style="vertical-align: middle" alt="delete"
                                                             onclick="deleteCost('Are you sure you want to delete?', '${lclUnitSSAc.id}');"
                                                             title="Delete Cost" width="13px" height="13px"/>
                                                    </c:if>
                                             <%--   </c:if>  --%>
                                            </c:when>
                                            <c:otherwise>
                                                <img alt="Scan/Attach" title="Scan View" class="scanView" style="vertical-align: top"
                                                     src="${path}/img/icons/preview.gif" onclick="showScanOrAttach('${path}', '${lclUnitSSAc.apAcctNo.accountno}', '${lclUnitSSAc.apReferenceNo}', '')"/>
                                                <c:if test="${lclUnitCostChargeForm.closedTime eq '' && lclUnitCostChargeForm.auditedTime eq ''}">
                                                    <c:if test="${lclUnitSSAc.apTransType=='AC'}">
                                                        <img src="${path}/images/edit.png"  style="cursor:pointer"  style="vertical-align: middle" class="cost" alt="edit"
                                                             onclick="editCost('${lclUnitSSAc.id}', '${lclUnitSSAc.apTransType}');"
                                                             title="Edit Cost" width="13px" height="13px"/>
                                                    </c:if>
                                                    <c:if test="${lclUnitSSAc.apTransType=='AC' && lclUnitSSAc.apTransType!='DS'}">
                                                        <img src="${path}/images/error.png" style="cursor:pointer" style="vertical-align: middle" alt="delete"
                                                             onclick="deleteCost('Are you sure you want to delete?', '${lclUnitSSAc.id}');"
                                                             title="Delete Cost" width="13px" height="13px"/>
                                                    </c:if>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <c:if test='${not empty unitCostTotal}'>
                            <tr>
                                <td colspan="8"></td>
                                <td>-----------</td>
                                <td colspan="2"></td>
                            </tr>
                            <tr>
                                <td colspan="7"></td>
                                <td><b id="blueBold">Total Unit Cost($-USD)</b></td>
                                <td><b id="greenBold">${unitCostTotal}</b></td>
                                <td colspan="2"></td>
                            </tr>
                        </c:if>
                    </table>
                </div>
            </c:if>
            <br/>
            <c:set var="drCostTotal" value="${0}"/>
            <c:if test="${not empty drCostList}">
                <cong:table align="center" id="" cellpadding="0" cellspacing="0" width="99%" border="0" style="border:1px solid #dcdcdc">
                    <cong:tr styleClass="tableHeadingNew">
                        <cong:td width="100%" >
                            <div id="search-results" >
                                <c:choose>
                                    <c:when test="${groupByInvoiceFlag eq 'true'}">
                                        Group By Invoice
                                    </c:when>
                                    <c:otherwise>
                                        DR Cost
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </cong:td>
                    </cong:tr>

                    <cong:tr>
                        <cong:td colspan="2">
                            <div style="width:100%;overflow:auto;" id="drDiv">


                                <table class="dataTable">
                                    <thead>
                                        <tr>
                                            <th>File No</th>
                                            <th>Cost Code</th>
                                            <th>Cost Desc</th>
                                            <th>Vendor</th>
                                            <th>Invoice#</th>
                                            <th>Status</th>
                                            <th>Cost Amount</th>
                                                <c:if test="${groupByInvoiceFlag ne 'true'}">
                                                <th>Action</th>
                                                </c:if>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="drCost" items="${drCostList}">
                                            <c:choose>
                                                <c:when test="${rowStyle eq 'oddStyle'}">
                                                    <c:set var="rowStyle" value="evenStyle"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="rowStyle" value="oddStyle"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <tr class="${rowStyle}">
                                                <td>${drCost.fileNo}</td>
                                                <td>${drCost.chargeCode}</td>
                                                <td>${drCost.agentNo}</td>
                                                <td>${drCost.vandorAcct}</td>
                                                <td>${drCost.invoiceNo}</td>
                                                <td>${drCost.transType}</td>
                                                <td>
                                                    <c:set var="drCostTotal" value="${drCostTotal+drCost.apAmount}"/>
                                                    <span>${drCost.apAmount}<span style="color: #008000;font-weight: bold">${drCost.totalCostByInvoiceNo}</span></span>
                                                </td>
                                                <c:if test="${groupByInvoiceFlag ne 'true'}">
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${lclUnitCostChargeForm.moduleName eq 'Exports'}">
                                                                <c:if test="${drCost.transType eq 'AC'}">
                                                                    <img src="${path}/images/edit.png"  style="cursor:pointer" class="drCost"                                                          width="13" height="13" alt="edit"
                                                                         onclick="editDrCost('${path}', '${drCost.bookingAcId}', '${drCost.transType}', '${lclUnitCostChargeForm.headerId}', '${lclUnitCostChargeForm.unitSsId}', '${lclUnitCostChargeForm.unitId}');"
                                                                         title="Edit Cost"/>
                                                                </c:if>    
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:if test="${lclUnitCostChargeForm.closedTime eq ''
                                                                              && lclUnitCostChargeForm.auditedTime eq '' && drCost.transType eq 'AC'}">
                                                                      <img src="${path}/images/edit.png"  style="cursor:pointer" class="drCost"                                                          width="13" height="13" alt="edit"
                                                                           onclick="editDrCost('${path}', '${drCost.bookingAcId}', '${drCost.transType}', '${lclUnitCostChargeForm.headerId}', '${lclUnitCostChargeForm.unitSsId}', '${lclUnitCostChargeForm.unitId}');"
                                                                           title="Edit Cost"/>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <c:if test='${not empty drCostTotal}'>
                                        <tr>
                                            <td colspan="6"></td>
                                            <td>-------------</td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td colspan="4"></td>
                                            <td colspan="2" align="center">
                                                <b id="blueBold">Total DR Cost($-USD)</b></td>
                                            <td><b id="greenBold">${drCostTotal}</b></td>
                                            <td></td>
                                        </tr>
                                    </c:if>
                                </table>
                            </div>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </c:if>
            <c:set var="grandTotal" value="${unitCostTotal+drCostTotal}"/>
            <c:if test='${not empty grandTotal && grandTotal ne 0}'>
                <table>
                    <br>
                    <br>
                    <br>
                    <tr>
                        <td width="71%"></td>
                        <td><b id="blueBold">Grand Total Cost($-USD)&nbsp;&nbsp;</b></td>
                        <td><b id="greenBold">${grandTotal}</b></td>
                    </tr>
                </table>
            </c:if>
            <cong:hidden name="methodName" id="methodName"/>
            <cong:hidden name="groupByInvoiceFlag" id="groupByInvoiceFlag"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function submitForm(methodName) {
            showLoading();
            $("#methodName").val(methodName);
            $('#lastInvoiceNumber').val($('#invoiceNumber').val());
            $('#lastVendorName').val($('#thirdPartyname').val());
            $('#lastVendorNumber').val($('#thirdpartyaccountNo').val());
            $("#lclUnitCostChargeForm").submit();
        }
        function lastInvoiceNo() {
            if ($('#lastInvoice').is(":checked")) {
                $('#invoiceNumber').val($('#lastInvoiceNumber').val());
                $('#thirdPartyname').val($('#lastVendorName').val());
                $('#thirdpartyaccountNo').val($('#lastVendorNumber').val());
            } else {
                $('#invoiceNumber').val('');
                $('#thirdPartyname').val('');
                $('#thirdpartyaccountNo').val('');
            }
        }
        function sampleAlert(txt) {
            $.prompt(txt);
        }
        function showValue() {
            var buttonValue = 'addCharge';
            if ($('#metric').is(":checked")) {
                if (buttonValue == 'addCharge') {
                    document.getElementById('wei').innerHTML = "/1000 KGS"
                    document.getElementById('msr').innerHTML = "/CBM"
                }
                document.getElementById('wei1').innerHTML = "/1000 KGS"
                document.getElementById('msr1').innerHTML = "/CBM"
            }
            if ($('#imperial').is(":checked")) {
                if (buttonValue == 'addCharge') {
                    document.getElementById('wei').innerHTML = "/100 LBS"
                    document.getElementById('msr').innerHTML = "/CFT"
                }
                document.getElementById('wei1').innerHTML = "/100 LBS"
                document.getElementById('msr1').innerHTML = "/CFT"
            }
        }

        $(document).ready(function () {
            var moduleName = $('#moduleName').val();
            if (moduleName === 'Exports') {
                if ($('#autoCostFlag').val() === 'true' && $('#cobStatus').val() === 'true') {
                    $('#autoCost').attr("disabled", true);
                    $('#autoCost').removeClass("gray-background");
                    $('#autoCost').addClass("gray-background");
                } else if ($('#cobStatus').val() === 'false') {
                    $('#autoCost').removeClass("gray-background");
                    $('#autoCost').addClass("gray-background");
                    $('#autoCost').attr("disabled", true);
                }
                var isCob = parent.$(".cob" + $("#unitSsId").val()).val();
                if (isCob === 'false') {
                    $("#faeCost").hide();
                }
            }

            $(document).keydown(function (e) {
                if ($(e.target).attr("readonly")) {
                    if (e.keyCode === 8) {
                        return false;
                    }
                }
            });
            if ($('#costStatus').val() == 'DS')
            {
                $('#thirdPartyname').addClass('text-readonly');
                $('#thirdPartyname').attr('readonly', true);
                $('#thirdPartyname').attr('tabindex', -1);
                $('#invoiceNumber').addClass('text-readonly');
                $('#invoiceNumber').attr('readonly', true);
                $('#invoiceNumber').attr('tabindex', -1);
            }
        });
        function deleteCost(txt, id) {
            $.prompt(txt, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v == 1) {
                        showLoading();
                        $.prompt.close();
                        $("#unitSSAcId").val(id);
                        submitForm('deleteCost')
                    }
                    else if (v == 2) {
                        $.prompt.close();
                    }
                }
            });
        }
        function editCost(id, costStatus) {
            showLoading();
            $("#unitSSAcId").val(id);
            $("#costStatus").val(costStatus);
            $("#saveDrCostFlag").val("false");
            submitForm('editCost')
        }
        function editDrCost(path, id, costStatus, headerId, unitSsId, unitId) {
            var href = path + "/lclImpUnitCostCharge.do?methodName=editDrCost&bookingAcId=" + id + "&costStatus=" + costStatus + "&saveDrCostFlag=true" + "&headerId=" + headerId + "&unitSsId=" + unitSsId + "&unitId=" + unitId;
            $(".drCost").attr("href", href);
            $(".drCost").colorbox({
                iframe: true,
                width: "90%",
                height: "90%",
                title: "Cost"
            });
        }
        function resetForm() {
            $('#chargesCode').val('');
            $('#costAmount').val('');
            $('#thirdPartyname').val('');
            $('#thirdpartyaccountNo').val('');
            $('#unitSSAcId').val('');
            $('#invoiceNumber').val('');
        }
        function calculateUnitAutoCost() {
            if ($('#moduleName').val() === 'Exports') {
                calculateAutoCostLCLE();
            } else {
                calculateAutoCostLCLI();
            }
        }
        function calculateAutoCostLCLE() {
            showLoading();
            $("#methodName").val('calculateAutoCostByLCLE');
            $("#lclUnitCostChargeForm").submit();
        }
        function calculateAutoCostLCLI() {
            if (parent.$('#originalOriginId').val() != "" && parent.$('#originalDestinationId').val() != "" && $('#cfsWarehouseId').val() != "" && $('#unitTypeId').val() != "") {
                showLoading();
                $.ajaxx({
                    data: {
                        className: "com.gp.cong.lcl.dwr.LclDwr",
                        methodName: "checkUnitAutoCostByImp",
                        forward: "/jsps/LCL/lclUnitAutoCostTemplate.jsp",
                        param1: parent.$('#originalOriginId').val(),
                        param2: parent.$('#originalDestinationId').val(),
                        param3: $('#cfsWarehouseId').val(),
                        param4: $('#unitTypeId').val(),
                        param5: "UNIT_COST",
                        request: true
                    },
                    async: false,
                    success: function (data) {
                        if ($.trim(data) !== "") {
                            showMask();
                            jQuery("<div style='top:50px; margin-left : 250px; width:500px;height:150px'></div>").html(data).addClass("popup").appendTo("body");
                        } else {
                            $.prompt("Auto Cost Rates are not Found");
                        }
                        closePreloading();
                    }
                });
            }
        }
        function showGroupByInvoice(groupByInvoiceFlag) {
            $("#groupByDR").hide();
            showLoading();
            $("#groupByInvoiceFlag").val(groupByInvoiceFlag);
            $("#methodName").val('displayCost');
            $("#lclUnitCostChargeForm").submit();
            $("#searchresultsasa").html('Group By Invoice');
        }
        function showGroupByDR(groupByInvoiceFlag) {
            $("#groupByInvoice").hide();
            showLoading();
            $("#groupByInvoiceFlag").val(groupByInvoiceFlag);
            $("#methodName").val('displayCost');
            $("#lclUnitCostChargeForm").submit();
        }
        function showScanOrAttach(path, vendorNo, invoiceNo, fileNumber) {
            var href = path + "/lclScanViewDetails.do?methodName=displayScanDetails&vendorNo=" + vendorNo + "&invoiceNo=" + invoiceNo;
            href = href + "&fileNumber=" + fileNumber;
            $(".scanView").attr("href", href);
            $(".scanView").colorbox({
                iframe: true,
                width: "70%",
                height: "60%",
                title: "Scan View"
            });
        }
        function showFAECost(path) {
            if (parent.$("#exportAgentAcctNo").val() === '') {
                $.prompt("Please fill the agent before calculating FAE cost");
                return false;
            } else {
                var href = path + "/lclImpUnitCostCharge.do?methodName=showFAECost&unitSsId="
                        + $("#unitSsId").val() + "&unitNo=" + $("#unitNo").val() + "&headerId=" + $("#headerId").val();
                $.colorbox({
                    iframe: true,
                    href: href,
                    width: "90%",
                    height: "80%",
                    title: "FAE Cost"
                });
            }
        }
        function validateGlMapping(chargeCode) {
            var flag = true;
            var originId = parent.$("#originId").val();
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO",
                    methodName: "validateLclExportGlAccount",
                    param1: chargeCode,
                    param2: "0",
                    param3: "",
                    param4: originId,
                    param5: "AC",
                    dataType: "json"
                },
                async: false,
                success: function (data) {
                    if (data !== '') {
                        flag = false;
                        $.prompt(data);
                    }
                }
            });
            return flag;
        }
    </script>
</body>
