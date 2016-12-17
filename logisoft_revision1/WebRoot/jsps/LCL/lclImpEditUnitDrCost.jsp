<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<cong:javascript  src="${path}/jsps/LCL/js/common.js"/>
<cong:javascript src="${path}/jsps/LCL/js/lclImportsUnitsSchedule.js"></cong:javascript>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        $('#saveCode').click(function() {
            var chargesCode = $('#chargesCode').val();
            if (chargesCode == null || chargesCode == "")
            {
                sampleAlert('Code is required');
                $("#chargesCode").css("border-color", "red");
                return false;
            }
            var costAmount = $('#costAmount').val();
            if (costAmount == null || costAmount == "" || costAmount == '0.00') {
                sampleAlert('Cost Amount is required and must be greater than 0.00');
                $('#costAmount').css("border-color", "red");
                return false;
            }
            var thirdPartyname = $('#thirdPartyname').val();
            if (thirdPartyname == null || thirdPartyname == undefined || thirdPartyname == "") {
                sampleAlert('Vendor is required');
                $("#thirdPartyname").css("border-color", "red");
            } else {
                submitAjaxForm('saveDrCost','#lclUnitCostChargeForm', '#fullDiv');
               
            }
        });
    });
    function allowNegativeNumbers(obj){
        var result;
        if (!/^-?\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            sampleAlert("This field should be Numeric");
        }
    }

</script>
<body style="background:#ffffff">
    <cong:div style="width:100%; float:left; ">
        <cong:form name="lclUnitCostChargeForm" id="lclUnitCostChargeForm" action="/lclImpUnitCostCharge">
            <input type="hidden" id="headerId" name="headerId" value="${lclUnitCostChargeForm.headerId}"/>
            <input type="hidden" id="unitSsId" name="unitSsId" value="${lclUnitCostChargeForm.unitSsId}"/>
            <input type="hidden" id="unitId" name="unitId" value="${lclUnitCostChargeForm.unitId}"/>
            <input type="hidden" id="autoCostFlag" name="autoCostFlag" value="${lclUnitCostChargeForm.autoCostFlag}"/>
            <input type="hidden" id="cfsWarehouseId" name="cfsWarehouseId" value="${lclUnitCostChargeForm.cfsWarehouseId}"/>
            <input type="hidden" id="unitTypeId" name="unitTypeId" value="${lclUnitCostChargeForm.unitTypeId}"/>
            <input type="hidden" id="closedTime" name="closedTime" value="${lclUnitCostChargeForm.closedTime}"/>
            <input type="hidden" id="auditedTime" name="auditedTime" value="${lclUnitCostChargeForm.auditedTime}"/>
            <cong:hidden id="unitNo" name="unitNo" />
            <cong:hidden id="unitSSAcId" name="unitSSAcId" />
            <cong:hidden id="bookingAcId" name="bookingAcId" />
            <cong:hidden id="saveDrCostFlag" name="saveDrCostFlag" />
            <cong:hidden name="costStatus"  id="costStatus" value='${lclUnitCostChargeForm.costStatus}' />
            <cong:table border="0">
                <cong:tr styleClass="tableHeadingNew">
                    <cong:td colspan="9"> Cost for Unit# <span class="fileNo">${lclUnitCostChargeForm.unitNo}</span> </cong:td>
                </cong:tr>
                <cong:tr><cong:td colspan="12"></cong:td></cong:tr>
                <cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Code</cong:td>
                    <cong:td>
                        <c:choose>
                            <c:when test="${lclUnitCostChargeForm.saveDrCostFlag eq 'true' }">
                                <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="COST_CODE" fields="NULL,chargesCodeId"
                                                    params="LCLI" container="NULL" styleClass="text mandatory require text-readonly" disabled="true" width="500" shouldMatch="true" scrollHeight="150"/>
                            </c:when>
                            <c:otherwise>
                                <cong:autocompletor name="chargesCode" id="chargesCode" template="two" query="COST_CODE" fields="NULL,chargesCodeId"
                                                    params="LCLI" container="NULL" styleClass="text mandatory require" width="500" shouldMatch="true" scrollHeight="150"/>
                            </c:otherwise>
                        </c:choose>
                        <cong:hidden name="chargesCodeId" id="chargesCodeId"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldforlcl">Cost Amount</cong:td>
                    <cong:td>
                        <cong:text styleClass="text twoDigitDecFormat mandatory" style="width:76px" name="costAmount" id="costAmount" onkeyup="allowNegativeNumbers(this);"/>
                    </cong:td>
                    <cong:td></cong:td></cong:tr><cong:tr>
                    <cong:td styleClass="textlabelsBoldforlcl">Vendor Name</cong:td>
                    <cong:autocompletor  name="thirdPartyname"  id="thirdPartyname" fields="thirdpartyaccountNo,NULL,NULL,NULL,NULL,NULL,NULL,thirdpartyDisabled,NULL,NULL,NULL,NULL,NULL,thirdparyDisableAcct"
                                         callback="vendorTypeCheck();" shouldMatch="true" width="530" query="VENDOR"
                                         template="tradingPartner" scrollHeight="150" styleClass="text mandatory require"
                                         paramFields="vendorSearchState,vendorSearchZip,vendorSearchSalesCode,vendorCountryUnLocCode">

                        <img src="${path}/images/icons/search_filter.png" class="clientSearchEdit"
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
                                <input type="checkbox" name="lastInvoice" id="lastInvoice" title="Repeat Last Invoice#" container="null" onclick="lastInvoiceNo()"/>
                            </c:otherwise>
                        </c:choose>
                    </cong:td>
                    <cong:hidden  name="lastInvoiceNumber" id="lastInvoiceNumber" value='${lclUnitCostChargeForm.lastInvoiceNumber}' />
                    <cong:hidden  name="lastVendorName" id="lastVendorName" value='${lclUnitCostChargeForm.lastVendorName}'  />
                    <cong:hidden  name="lastVendorNumber" id="lastVendorNumber" value='${lclUnitCostChargeForm.lastVendorNumber}'  />
                    <cong:td width="4%"></cong:td>
                </cong:tr>
            </cong:table>
            <br>
            <cong:table align="center" width="100%" border="0">
                <cong:tr>
                    <cong:td width="42%" > </cong:td>
                    <cong:td width="50%" >
                        <input type="button" class="button-style1" value="Save Cost" id="saveCode"/>
                        <input type="button" class="button-style1" value="Cancel" id="cancel" onclick="cancelPopup();"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <br/>
            <cong:hidden name="methodName" id="methodName"/>
        </cong:form>
    </cong:div>
    <script type="text/javascript">
        function submitAjaxForm(methodName, formName, selector) {
            showLoading();
            $("#methodName").val(methodName);
            var params = $(formName).serialize();
            var lastInvoiceNumber = $('#invoiceNumber').val();
            var lastVendorName = $('#thirdPartyname').val();
            var lastVendorNumber = $('#thirdpartyaccountNo').val();
            var headerId = $('#headerId').val();
            var unitSsId = $('#unitSsId').val();
            var unitId = $('#unitId').val();
            params += "&lastInvoiceNumber=" + lastInvoiceNumber + "&lastVendorName=" + lastVendorName+ "&lastVendorNumber=" + lastVendorNumber+ "&headerId=" + headerId+ "&unitSsId=" + unitSsId+ "&unitId=" + unitId+ "&groupByInvoiceFlag=false";
            $.post($(formName).attr("action"), params,
            function(data) {
                $(selector).html(data);
                $(selector, window.parent.document).html(data);
                parent.$.colorbox.close();
            });
        }
            function cancelPopup() {
            parent.$.colorbox.close();
        }
     
        function sampleAlert(txt) {
            $.prompt(txt);
        }
        
        $(document).ready(function() {
            $(document).keydown(function(e) {
                if ($(e.target).attr("readonly")) {
                    if (e.keyCode === 8) {
                        return false;
                    }
                }
            });
        });
       
    </script>
</body>
