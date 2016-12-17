<%-- 
    Document   : clientSearchBL
    Created on : Oct 10, 2015, 2:56:18 PM
    Author     : priyanka.rachote
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<cong:table styleClass="tableBorderNew" width="97%">
    <cong:tr styleClass="tableHeadingNew">
        <cong:td colspan="2">${searchByValue} Search Options</cong:td>
    </cong:tr>
    <input type="hidden" id="searchByValue" name="searchByValue" value="${searchByValue}"/>
    <cong:tr styleClass="textlabelsBold">
        <cong:td>Search By</cong:td>
        <cong:td>
            <cong:select name="searchClientBy" id="searchClientBy" styleClass="dropdown_accounting" style="width: 125px;">
                <cong:option value="${searchByValue}">${searchByValue}</cong:option>
            </cong:select>
        </cong:td>
    </cong:tr>
    <c:if test="${searchByValue eq 'Coloader' || searchByValue eq 'Coloader Devanning Warehouse'}">
        <cong:tr styleClass="textlabelsBold">
            <cong:td>City</cong:td>
            <cong:td>
                <cong:text name="clientCity" id="clientCity" styleClass="textlabelsBoldForTextBox textCap"/>
            </cong:td>
        </cong:tr>
    </c:if>
    <cong:tr styleClass="textlabelsBold">
        <cong:td>State</cong:td>
        <cong:td>
            <cong:text name="clientState" id="clientState" styleClass="textlabelsBoldForTextBox textCap"/>
        </cong:td>
    </cong:tr>
    <c:if test="${searchByValue ne 'Coloader' && searchByValue ne 'Coloader Devanning Warehouse' && searchByValue ne 'ThirdParty'}">
        <cong:tr styleClass="textlabelsBold">
            <cong:td>Country</cong:td>
            <cong:td>
                <cong:autocompletor name="clientCountry" template="tradingPartner" id="clientCountry" fields="CountryUnLocCode"
                                    styleClass="textlabelsBoldForTextBox textCap" paramFields=""  query="COUNTRY"  width="200" container="NULL" shouldMatch="true" value=""
                                    scrollHeight="85px"/>
                <input type="hidden" name="CountryUnLocCode" id="CountryUnLocCode"/>
                <img src="${path}/img/icons/help-icon.gif" border="0" title="Please Enter Country Name <br/> or Country Code" align="middle"/>
            </cong:td>
        </cong:tr>
    </c:if>
    <c:if test="${searchByValue ne 'IMPORT_CFS'  && searchByValue ne 'IMPORT_WHSE' && searchByValue ne 'Coloader Devanning Warehouse'}">
        <cong:tr styleClass="textlabelsBold">
            <cong:td>Zip Code</cong:td>
            <cong:td>
                <cong:text name="clientZipCode" id="clientZipCode" styleClass="textlabelsBoldForTextBox uppercase"/>
            </cong:td>
        </cong:tr>
        <c:if test="${searchByValue ne 'Coloader'  && searchByValue ne 'Coloader Devanning Warehouse' && searchByValue ne 'ThirdParty'}">
            <cong:tr styleClass="textlabelsBold">
                <cong:td>Sales Code</cong:td>
                <cong:td>
                    <cong:text name="clientSalesCode" id="clientSalesCode" styleClass="textlabelsBoldForTextBox uppercase"/>
                </cong:td>
            </cong:tr>
        </c:if>
    </c:if>
    <%--   <cong:tr styleClass="textlabelsBold">
           <cong:td colspan="2">
               <input type="checkbox" name="displayClientOneLine" id="displayClientOneLine"/>
                Display Name and Address in one line
           </cong:td>
       </cong:tr>--%>
    <cong:tr styleClass="textlabelsBold">
        <cong:td colspan="2" align="center">
            <input type="button" value="Submit" onclick="closeClientSearchOption();" class="buttonStyleNew"/>
            <input type="button" value="Clear Filter" onclick="clearClientSearchOption();" class="buttonStyleNew"/>
        </cong:td>
    </cong:tr>
</cong:table>
<script type="text/javascript">
    $(document).ready(function() {
        if ($('#searchByValue').val() === 'Consignee') {
            $('#clientState').val(parent.$('#consigneeSearchState').val());
            $('#clientZipCode').val(parent.$('#consigneeSearchZip').val());
            $('#clientSalesCode').val(parent.$('#consigneeSearchSalesCode').val());
            $('#clientCountry').val(parent.$('#consigneeSearchCountry').val());
        } else if ($('#searchByValue').val() === 'Shipper') {
            $('#clientState').val(parent.$('#shipperSearchState').val());
            $('#clientZipCode').val(parent.$('#shipperSearchZip').val());
            $('#clientSalesCode').val(parent.$('#shipperSearchSalesCode').val());
            $('#clientCountry').val(parent.$('#shipperSearchCountry').val());
        } else if ($('#searchClientBy').val() === 'ThirdParty') {
            $('#clientState').val(parent.$('#thirdSearchState').val());
            $('#clientZipCode').val(parent.$('#thirdSearchZip').val());
        } else if ($('#searchClientBy').val() === 'Forwarder') {
            $('#clientState').val(parent.$('#frwdSearchState').val());
            $('#clientZipCode').val(parent.$('#frwdSearchZip').val());
            $('#clientSalesCode').val(parent.$('#frwdSearchSalesCode').val());
            $('#clientCountry').val(parent.$('#frwdSearchCountry').val());
        }else if ($('#searchClientBy').val() === 'Notify') {
            $('#searchClientBy').val('Notify');
            $('#clientState').val(parent.$('#notifySearchState').val());
            $('#clientZipCode').val(parent.$('#notifySearchZip').val());
            $('#clientSalesCode').val(parent.$('#notifySearchSalesCode').val());
            $('#clientCountry').val(parent.$('#notifySearchCountry').val());
        }
        var clientSta = $('#clientState').val();
        var replaceValue = clientSta.replace(/'/g, "");
        $('#clientState').val(replaceValue);
    });

    function closeClientSearchOption() {
        parent.$('#searchClientBy').val($('#searchClientBy').val());
        var filterName=$('#searchClientBy').val();
        var state = $('#clientState').val();
        var multiState = "";
        if (state != "") {
            var st = state.split(",");
            for (var i = 0; i < (st.length); i++) {
                multiState += "'" + st[i] + "',";
            }
            var len = multiState.length;
            var data = multiState.substring(0, len - 1);
            $('#clientState').val(data);
        } else {
            $('#clientState').val("''");
        }
        if (filterName === 'Shipper') {
            parent.$('#shipperSearchState').val($('#clientState').val());
            parent.$('#shipperSearchZip').val($('#clientZipCode').val());
            parent.$('#shipperSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#shipperCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#shipperSearchCountry').val($('#clientCountry').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" ||
                $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#shipperName').attr('alt', 'SHIPPER_SEARCH');
            } else {
                parent.$('#shipperName').attr('alt', 'TP_IMPORTS_NOTE');
            }
            parent.jQuery('#clientSearchEditBlS').attr("src","${path}/images/icons/search_filter_green.png");
            parent.$('#shipperName').focus();
        } else if (filterName === 'Consignee') {
            parent.$('#consigneeSearchState').val($('#clientState').val());
            parent.$('#consigneeSearchZip').val($('#clientZipCode').val());
            parent.$('#consigneeSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#consigneeCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#consigneeSearchCountry').val($('#clientCountry').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" ||
                $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#consigneeName').attr('alt', 'CONSIGNEE_SEARCH');
            } else {
                parent.$('#consigneeName').attr('alt', 'IMPORTS_TP_NOTE');
            }
            parent.jQuery('#clientSearchEditBlC').attr("src","${path}/images/icons/search_filter_green.png");
            parent.$('#consigneeName').focus();
        } else if (filterName === 'Forwarder') {
            parent.$('#frwdSearchState').val($('#clientState').val());
            parent.$('#frwdSearchZip').val($('#clientZipCode').val());
            parent.$('#frwdSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#frwdSearchCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#frwdSearchCountry').val($('#clientCountry').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" ||
                $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#forwarderName').attr('alt', 'FORWARDER_BL_FILTER');
            } else {
                parent.$('#forwarderName').attr('alt', 'FORWARDER_BL');
            }
            parent.jQuery('#clientSearchEditBlSF').attr("src","${path}/images/icons/search_filter_green.png");
            parent.$('#forwarderName').focus();
        } else if (filterName === 'Notify') {
            parent.$('#notifySearchState').val($('#clientState').val());
            parent.$('#notifySearchZip').val($('#clientZipCode').val());
            parent.$('#notifySearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#notifyCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#notifySearchCountry').val($('#clientCountry').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" ||
                $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#notifyName').attr('alt', 'CONSIGNEE_SEARCH');
            } else {
                parent.$('#notifyName').attr('alt', 'TP_IMPORTS_NOTE');
            }
            parent.jQuery('#clientSearchEditBlN').attr("src","${path}/images/icons/search_filter_green.png");
            parent.$('#notifyName').focus();
        }else if (filterName === 'ThirdParty') {
            parent.$('#thirdSearchState').val($('#clientState').val());
            parent.$('#thirdSearchZip').val($('#clientZipCode').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" ||
                $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#thirdPartyname').attr('alt', 'THIRD_PARTY_BL_SEARCH');
            } else {
                parent.$('#thirdPartyname').attr('alt', 'THIRD_PARTY_BL');
            }
            parent.jQuery("#clientSearchEditBlT").attr("src","${path}/images/icons/search_filter_green.png");
            parent.$('#thirdPartyname').focus();
        }
        parent.$.fn.colorbox.close();
    }


    function clearClientSearchOption() {
        jQuery("#clientState").val('');
        jQuery("#clientCountry").val('');
        jQuery("#clientZipCode").val('');
        jQuery("#clientSalesCode").val('');
        parent.jQuery("#whseFilterState").val('');
        parent.jQuery("#filterButtonValue").val('CF');
        closeClientSearchOption();
        parent.$.fn.colorbox.close();
    }

</script>

