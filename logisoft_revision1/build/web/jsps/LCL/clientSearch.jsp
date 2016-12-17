<%-- 
    Document   : clientSearch
    Created on : Jan 27, 2013, 11:30:36 AM
    Author     : Mahalakshmi
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
                <cong:option value="Contact">Contact</cong:option>
		<cong:option value="Email">Email</cong:option>
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
        }else if ($('#searchByValue').val() === 'Vendor') {
            $('#clientState').val(parent.$('#vendorSearchState').val());
            $('#clientZipCode').val(parent.$('#vendorSearchZip').val());
            $('#clientSalesCode').val(parent.$('#vendorSearchSalesCode').val());
            $('#clientCountry').val(parent.$('#vendorSearchCountry').val());
        }else if ($('#searchClientBy').val() === 'IMPORT_CFS') {
            $('#clientState').val(parent.$('#ipiSearchState').val());
        } else if ($('#searchClientBy').val() === 'IMPORT_WHSE') {
            var cityStateZip = parent.$('#cityStateZip').val().split('/');
            var defaultState = parent.$('#whseFilterState').val();
            var whseFilterState = defaultState.replace(/'/g, "");
            if (whseFilterState === cityStateZip[1]) {
                $('#clientState').val(cityStateZip[1]);
            } else if (whseFilterState !== "" && cityStateZip[1] !== "" && whseFilterState !== cityStateZip[1]) {
                $('#clientState').val(whseFilterState);
            } else {
                if (parent.$('#filterButtonValue').val() === 'CF') {
                    $('#clientState').val('');
                } else {
                    $('#clientState').val(cityStateZip[1]);
                }
            }
        } else if ($('#searchClientBy').val() === 'Notify') {
            $('#searchClientBy').val('Notify');
            $('#clientState').val(parent.$('#notifySearchState').val());
            $('#clientZipCode').val(parent.$('#notifySearchZip').val());
            $('#clientSalesCode').val(parent.$('#notifySearchSalesCode').val());
            $('#clientCountry').val(parent.$('#notifySearchCountry').val());
        } else if ($('#searchClientBy').val() === '2nd Notify Party') {
            $('#searchClientBy').val('2nd Notify Party');
            $('#clientState').val(parent.$('#notify2SearchState').val());
            $('#clientZipCode').val(parent.$('#notify2SearchZip').val());
            $('#clientSalesCode').val(parent.$('#notify2SearchSalesCode').val());
            $('#clientCountry').val(parent.$('#notify2SearchCountry').val());
        } else if ($('#searchClientBy').val() === 'Coloader') {
            $('#searchClientBy').val('Coloader');
            parent.$('#coloaderSearchCity').val($('#clientCity').val());
            parent.$('#coloaderSearchState').val($('#clientState').val());
            parent.$('#coloaderSearchZip').val($('#clientZipCode').val());
        } else if ($('#searchClientBy').val() === 'Coloader Devanning Warehouse') {
            $('#searchClientBy').val('Coloader Devanning Warehouse');
            parent.$('#coloaderDevngSearchCity').val($('#clientCity').val());
            parent.$('#coloaderDevngSearchState').val($('#clientState').val());
            parent.$('#coloaderDevngSearchZip').val($('#clientZipCode').val());
        } else if($('#searchClientBy').val() === 'Ship/Supplier'){
             $('#searchClientBy').val('Ship/Supplier');
            $('#clientState').val(parent.$('#shipSupSearchState').val());
            $('#clientZipCode').val(parent.$('#shipSupSearchZip').val());
            $('#clientSalesCode').val(parent.$('#shipSupSearchSalesCode').val());
            $('#clientCountry').val(parent.$('#shipSupSearchCountry').val());
        }
        else {
            $('#searchClientBy').val('Client');
            $('#clientState').val(parent.$('#clientSearchState').val());
            $('#clientZipCode').val(parent.$('#clientSearchZip').val());
            $('#clientSalesCode').val(parent.$('#clientSearchSalesCode').val());
            $('#clientCountry').val(parent.$('#clientSearchCountry').val());
        }
        var clientSta = $('#clientState').val();
        var replaceValue = clientSta.replace(/'/g, "");
        $('#clientState').val(replaceValue);
    });

    function closeClientSearchOption() {
        
     var valueforclient = $('#searchByValue').val();
     var valueforcontact = $('#searchClientBy').val();
        parent.$('#searchClientBy').val($('#searchClientBy').val());
        var state = $('#clientState').val();
        var multiState = "";
        if (state != "") {
            var st = state.split(","), part;
            for (var i = 0; i < (st.length); i++) {
                multiState += "'" + st[i] + "',";
            }
            var len = multiState.length;
            var data = multiState.substring(0, len - 1);
            $('#clientState').val(data);
        } else {
            $('#clientState').val("''");
        }
        if ($('#searchClientBy').val() === 'Client') {
            parent.$('#clientSearchState').val($('#clientState').val());
            parent.$('#clientSearchZip').val($('#clientZipCode').val());
            parent.$('#clientSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#clientSearchCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#clientSearchCountry').val($('#clientCountry').val());

           if($('#searchByValue').val() === 'Client'){
             parent.$('#client').attr('readonly',false);
             parent.$('#contactName').attr('readonly',true); 
            }

            if($('#clientState').val()!=="''" || $('#CountryUnLocCode').val() !=="" || $('#clientZipCode').val()!=="" || $('#clientSalesCode').val()!=="" ){
                parent.$('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_SEARCH');
            } else {
                parent.$('#client').attr('alt', 'CLIENT_NO_CONSIGNEE');
            }
            parent.$('#client').focus();
            if (parent.$('#clientWithConsignee').is(":checked")) {
                parent.$('#clientCons').attr('alt', 'CLIENT_WITH_CONSIGNEE_SEARCH');
            } else {
                parent.$('#client').attr('alt', 'CLIENT_NO_CONSIGNEE_SEARCH');
            }
            if (parent.$('#clientWithConsignee').is(":checked")) {
                parent.$('#clientCons').focus();
            } else {
                parent.$('#client').focus();
            }
          
        } else if ($('#searchClientBy').val() === 'Shipper') {
            parent.$('#shipperSearchState').val($('#clientState').val());
            parent.$('#shipperSearchZip').val($('#clientZipCode').val());
            parent.$('#shipperSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#shipperCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#shipperSearchCountry').val($('#clientCountry').val());
            
            if($('#searchByValue').val() === 'Shipper'){
             parent.$('#shipperNameClient').attr('readonly',false);
             parent.$('#shipperContactClient').attr('readonly',true); 
            }
            
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" || $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#shipperNameClient').attr('alt', 'SHIPPER_SEARCH');
                //for main search screen Shipper********************
                parent.$('#shipperName').attr('alt', 'SHIPPER_SEARCH');
                //for eculine shipper*******************************
                parent.$('#shipperContact').attr('alt', 'SHIPPER_SEARCH');
            } else {
                //parent.$('#shipperNameClient').attr('alt', 'TRADING_PARTNER_IMPORTS');
                //parent.$('#shipperName').attr('alt', 'TRADING_PARTNER_IMPORTS');
                parent.$('#shipperNameClient').attr('alt', 'TP_IMPORTS_NOTE');
                parent.$('#shipperName').attr('alt', 'TP_IMPORTS_NOTE');
            }
            parent.$('#shipperNameClient').focus();
            parent.$('#shipperName').focus();
        } else if ($('#searchClientBy').val() === 'Consignee') {
            parent.$('#consigneeSearchState').val($('#clientState').val());
            parent.$('#consigneeSearchZip').val($('#clientZipCode').val());
            parent.$('#consigneeSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#consigneeCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#consigneeSearchCountry').val($('#clientCountry').val());
            
             if($('#searchByValue').val() === 'Consignee'){
             parent.$('#consigneeNameClient').attr('readonly',false);
             parent.$('#consigneeContactName').attr('readonly',true); 
            }
            
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" || $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#consigneeNameClient').attr('alt', 'CONSIGNEE_SEARCH');
                parent.$('#consigneeName').attr('alt', 'CONSIGNEE_SEARCH');
                parent.$('#consignee').attr('alt', 'CONSIGNEE_SEARCH');
                //For eculine consignee*****************************
                parent.$('#consContact').attr('alt', 'CONSIGNEE_SEARCH');
            } else {
                // parent.$('#consigneeNameClent').attr('alt', 'TRADING_PARTNER_IMPORTS');
                //  parent.$('#consigneeName').attr('alt', 'TRADING_PARTNER_IMPORTS');
                parent.$('#consigneeNameClient').attr('alt', 'IMPORTS_TP_NOTE');
                parent.$('#consigneeName').attr('alt', 'IMPORTS_TP_NOTE');
            }
            parent.$('#consigneeNameClient').focus();
            parent.$('#consigneeName').focus();
        } else if ($('#searchClientBy').val() === 'Forwarder') {
            parent.$('#frwdSearchState').val($('#clientState').val());
            parent.$('#frwdSearchZip').val($('#clientZipCode').val());
            parent.$('#frwdSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#frwdSearchCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#frwdSearchCountry').val($('#clientCountry').val());
            
             if($('#searchByValue').val() === 'Forwarder'){
                
             parent.$('#forwarderNameClient').attr('readonly',false);
             parent.$('#forwardercontactClient').attr('readonly',true); 
            }
            
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" || $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#forwarderNameClient').attr('alt', 'FORWARDER_SEARCH');
            } else {
                parent.$('#forwarderNameClient').attr('alt', 'TRADING_PARTNER_IMPORTS');
            }
            parent.$('#forwarderNameClient').focus();
        } else if ($('#searchClientBy').val() === 'IMPORT_CFS') {
            parent.$('#ipiSearchState').val($('#clientState').val());
            if (parent.$('#ipiSearchState').val() !== "" && parent.$('#ipiSearchState').val() !== null && parent.$('#ipiSearchState').val() !== "''") {
                parent.$('#stGeorgeAccount').attr('alt', 'IMPORT_CFS_STATE');
            } else {
                parent.$('#stGeorgeAccount').attr('alt', 'IMPORT_CFS');
            }
            parent.$('#stGeorgeAccount').focus();
        } else if ($('#searchClientBy').val() === 'IMPORT_WHSE') {
            parent.$('#defaultWhseState').val($('#clientState').val());
            parent.$('#whseFilterState').val($('#clientState').val());
            if (parent.$('#newWhse').is(":checked") == false) {
                if (parent.$('#defaultWhseState').val() !== "" && parent.$('#defaultWhseState').val() !== "''") {
                    parent.$("#whsecompanyName").show();
                    parent.$('#whsecompanyName').attr('alt', 'IMPORT_CFS_STATE');
                } else {
                    //                    var finalDestn = parent.parent.$("#finalDestinationR").val();
                    //                    var state = finalDestn.substring(finalDestn.lastIndexOf("/") + 1, finalDestn.lastIndexOf("("));
                    //                    state = "'" + state + "'";
                    //                    parent.$('#defaultWhseState').val(state);
                    parent.$("#whsecompanyName").show();
                    parent.$('#whsecompanyName').attr('alt', 'IMPORT_CFS');
                }
            } else {
                $("#manualWhse").show();
            }
            parent.$('#whsecompanyName').focus();
        } else if ($('#searchClientBy').val() === 'Notify') {
            parent.$('#notifySearchState').val($('#clientState').val());
            parent.$('#notifySearchZip').val($('#clientZipCode').val());
            parent.$('#notifySearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#notifyCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#notifySearchCountry').val($('#clientCountry').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" || $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#notifyName').attr('alt', 'CONSIGNEE_SEARCH');
                //For eculine notify 1******************************
                parent.$('#notify1Contact').attr('alt', 'CONSIGNEE_SEARCH');
            } else {
                //parent.$('#notifyName').attr('alt', 'TRADING_PARTNER_IMPORTS');
                parent.$('#notifyName').attr('alt', 'TP_IMPORTS_NOTE');
            }
            parent.$('#notifyName').focus();
        } else if ($('#searchClientBy').val() === '2nd Notify Party') {
            parent.$('#notify2SearchState').val($('#clientState').val());
            parent.$('#notify2SearchZip').val($('#clientZipCode').val());
            parent.$('#notify2SearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#notify2CountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#notify2SearchCountry').val($('#clientCountry').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" || $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#notify2Name').attr('alt', 'CONSIGNEE_SEARCH');
                //For eculine notify 2******************************
                parent.$('#notify2Contact').attr('alt', 'CONSIGNEE_SEARCH');
            } else {
                parent.$('#notify2Name').attr('alt', 'TRADING_PARTNER_IMPORTS');
            }
            parent.$('#notify2Name').focus();
        }else if ($('#searchClientBy').val() === 'Vendor') {
            parent.$('#vendorSearchState').val($('#clientState').val());
            parent.$('#vendorSearchZip').val($('#clientZipCode').val());
            parent.$('#vendorSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#vendorCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#vendorSearchCountry').val($('#clientCountry').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" || $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#thirdPartyname').attr('alt', 'VENDOR_SEARCH');
            } else {
                parent.$('#thirdPartyname').attr('alt', 'VENDOR_SEARCH');
            }
            parent.$('#thirdPartyname').focus();
        } else if ($('#searchClientBy').val() === 'Coloader') {
            parent.$('#coloaderSearchCity').val($('#clientCity').val());
            parent.$('#coloaderSearchState').val($('#clientState').val());
            parent.$('#coloaderSearchZip').val($('#clientZipCode').val());
            parent.$('#coloaderAcct').attr('alt', 'COLOADER_SEARCH');
            parent.$('#coloaderAcct').focus();
        } else if ($('#searchClientBy').val() === 'Coloader Devanning Warehouse') {
            parent.$('#coloaderDevngSearchCity').val($('#clientCity').val());
            parent.$('#coloaderDevngSearchState').val($('#clientState').val());
            parent.$('#coloaderDevngSearchZip').val($('#clientZipCode').val());
            if ($('#clientState').val() !== "''" || $('#clientZipCode').val() !== "" || $('#clientSalesCode').val() !== "" || $('#CountryUnLocCode').val() !== "") {
                parent.$('#coloaderDevngAcct').attr('alt', 'COLOADER_SEARCH');
               
            } else {
                parent.$('#coloaderDevngAcct').attr('alt', 'CONSIGNEE_SEARCH');
            }
            parent.$('#coloaderDevngAcct').focus();
        }
        else if ($('#searchClientBy').val() === 'ThirdParty') {
            parent.$('#thirdSearchState').val($('#clientState').val());
            parent.$('#thirdSearchZip').val($('#clientZipCode').val());
            if ($('#clientState').val() !== "''" || $('#clientZipCode').val() !== "" ) {
                parent.$('#thirdPartyname').attr('alt', 'THIRD_PARTY_SEARCH');
            } else {
                parent.$('#thirdPartyname').attr('alt', 'THIRD_PARTY');
            }
            parent.$('#thirdPartyname').focus();
        }
       else if ($('#searchClientBy').val() === 'Ship/Supplier') {
            parent.$('#shipSupSearchState').val($('#clientState').val());
            parent.$('#shipSupSearchZip').val($('#clientZipCode').val());
            parent.$('#shipSupSearchSalesCode').val($('#clientSalesCode').val());
            parent.$('#shipCountryUnLocCode').val($('#CountryUnLocCode').val());
            parent.$('#shipSupSearchCountry').val($('#clientCountry').val());
            if ($('#clientState').val() != "''" || $('#clientZipCode').val() != "" || $('#clientSalesCode').val() != "" || $('#CountryUnLocCode').val() != "") {
                parent.$('#shipSupplier').attr('alt', 'SHIPPER_SEARCH');

            } else {

                parent.$('#shipSupplier').attr('alt', 'TP_IMPORTS_NOTE');

            }
        }
        
       else if(valueforclient === 'Client' && valueforcontact === 'Contact'){
           parent.$('#client').val('');
           parent.$('#client_no').val('');
            parent.$('#contactName').attr('readonly',false); 
            parent.$('#client').attr('readonly',true);
        } 
        
        else if(valueforclient === 'Shipper' && valueforcontact === 'Contact'){
            parent.$('#shipperNameClient').val('');
            parent.$('#shipperCodeClient').val('');
            parent.$('#shipperContactClient').attr('readonly',false);
            parent.$('#shipperNameClient').attr('readonly',true);
        }
        else if(valueforclient === 'Forwarder' && valueforcontact === 'Contact'){
            parent.$('#forwarderCodeClient').val('');
            parent.$('#forwarderNameClient').val('');
             parent.$('#forwardercontactClient').attr('readonly',false);
            parent.$('#forwarderNameClient').attr('readonly',true);
        }
        
        else if(valueforclient === 'Consignee' && valueforcontact === 'Contact'){
            parent.$('#consigneeCodeClient').val('');
            parent.$('#consigneeNameClient').val('');
            parent.$('#consigneeContactName').attr('readonly',false);
           parent.$('#consigneeNameClient').attr('readonly',true);
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
