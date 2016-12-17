<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0"  prefix="un"%>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../includes/resources.jsp"%>
<%@include file="../includes/baseResources.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>Add Trading Partner</title>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js" ></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <style type="text/css">
            #docListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 5px;
                right: 0;
                top: 10%;
            }
        </style>
    </head><div id="cover"></div>
    <body class="whitebackgrnd" onload="init('${param.fromField}', '${param.callFrom}', '${param.accountName}', '${param.city}', '${param.unlocCode}')">
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <html:form action="/tradingPartner"  name="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">
            <div id="msg" class="error"></div>
            <table  class="tableBorderNew" border="0" cellpadding="3" cellspacing="3" width="100%">
                <tr class="tableHeadingNew"><td colspan="4">Customer</td></tr>
                <tr class="textlabelsBold">
                    <td colspan="2">
                        Name
                        <html:text property="accountName" styleId="accountName" styleClass="textlabelsBoldForTextBox" style="text-transform: uppercase;width: 180px;" maxlength="50" value=""/>
                    </td>
                    <c:choose>
                        <c:when test="${param.master=='no'}">
                            <td colspan="2">
                                Master
                                <html:select property="master" styleClass="textlabelsBoldForTextBox" value="">
                                    <html:optionsCollection name="mastertypelist" styleClass="unfixedtextfiledstyle"/>
                                </html:select>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td colspan="2">&nbsp;</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr class="textlabelsBold">
                    <td colspan="2">
                        City&nbsp;&nbsp;&nbsp;
                        <html:text property="city" styleId="city" value="${CUST_ADDRESS.city2}" styleClass="textlabelsBoldforTextBox" style="width:122px;text-transform: uppercase;" size="25"/>
                        <input type="hidden" name="cityCheck" id="cityCheck" value="${CUST_ADDRESS.city2}">
                        <div id="cityChoices" style="display: none" class="autocomplete"></div>&nbsp;&nbsp;
                        <html:text property="unLocCode" styleId="unLocCode" value="${CUST_ADDRESS.unLocCode}" size="5" maxlength="0" tabindex="-1" styleClass="textlabelsBoldForTextBoxDisabledLook" style="text-transform: uppercase;"/>
                        <input type="hidden" name="unLocCodeCheck" id="unLocCodeCheck" value="${CUST_ADDRESS.unLocCode}">
                        <div id="unLocCode_choices" style="display: none"></div>
                    </td>

                </tr>
                <tr class="tableHeadingNew"><td colspan="4">Account Type</td></tr>
                <tr class="textlabelsBold">
                    <td><input type="checkbox" name="accountTypes" value="S">S - Shipper</td>
                    <td><input type="checkbox" name="accountTypes" value="N"  disabled ="true">N - NVOCC</td>
                    <td><input type="checkbox" name="accountTypes" value="C" id="showCoload"  onclick="showColoadRetail(this)">C - Consignee</td>
                    <td>
                        <c:if test="${not empty loginuser && loginuser.role.roleDesc == commonConstants.ROLE_NAME_ADMIN}">
                            <input type="checkbox" name="accountTypes" value="Z">Z - Company
                        </c:if>
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td><input type="checkbox" name="accountTypes" value="I">AI - Import Agent</td>
                    <td><input type="checkbox" name="accountTypes" value="E">AE - Export Agent</td>
                        <c:set  var="nonAccounting" value="true"/>
                        <c:choose>
                            <c:when test="${roleDuty.tpSetVendorType}">
                            <td><input type="checkbox" name="accountTypes" value="V" onclick="showSubType(this)">V - Vendor</td>
                                <c:set  var="nonAccounting" value="false"/>
                            </c:when>
                            <c:otherwise>
                            <td><input type="checkbox" name="accountTypes" value="V" onclick="showSubType(this)" disabled="true">V - Vendor</td>
                            </c:otherwise>
                        </c:choose>
                    <td><input type="checkbox" name="accountTypes" value="O" disabled ="true">O - Others</td>
                </tr>
                <tr class="textlabelsBold">
                    <td>
                        <div style="display:none" id="fmcNo">
                            Frieght FMC# <html:text property="frieghtFmc" maxlength="25" styleClass="textlabelsBoldForTextBox"/>
                        </div>
                    </td>
                    <td>
                        <input type="button" class="buttonStyleNew"  value="Save" onclick="addTradingPartner(${nonAccounting})">
                    </td>
                    <td colspan="1" id="subTypeContent" style="display: none;">
                        SubType
                        <c:choose>
                            <c:when test="${roleDuty.vendorOtherthanFF}">
                                <html:select property="subType" styleId="subType" styleClass="textlabelsBoldForTextBox" onchange="onChangeSubType(${roleDuty.allowImportCfsVendor})">
                                    <html:optionsCollection name="subTypeList" styleClass="unfixedtextfiledstyle"/>
                                </html:select>
                            </c:when>
                            <c:otherwise>
                                <html:text property="subType" styleClass="textlabelsBoldForTextBoxDisabledLook" value="Forwarder" readonly="true"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td colspan="2" id="coloadRetail" style="display: none;">
                        Imports Rating Coload/Retail
                        <input type="radio" class="importQuoteColoadRetail" value="R" id="Retail" name="tradingPartnerForm"  />
                        <span class="hotspot" onmouseover="tooltip.show('<strong>Retail</strong>', null, event)" onmouseout="tooltip.hide()"
                              style="color:black;">R</span>
                        <input type="radio" class="importQuoteColoadRetail" value="C" id="Coload" name="tradingPartnerForm" />
                        <span class="hotspot" onmouseover="tooltip.show('<strong>Co-Load</strong>', null, event)" onmouseout="tooltip.hide()"
                              style="color:black;">C</span>
                    </td>
                </tr>
            </table>
            <input type="hidden" name="masterPage" id="masterPage" value="${param.master}"/>
        </html:form>
    </body>
    <script type="text/javascript">
        function trim(str) {
            return str.replace(/^\s+|\s+$/g, "");
        }
        function onChangeSubType(roleDuty) {
            var subType = document.getElementById('subType').value.toUpperCase();
            if (subType == "IMPORT CFS" && roleDuty == false) {
                alert("You do not have permission to mark this account as Imports CFS, please contact your manager");
                document.getElementById('subType').value = "";
            } else if (subType === "IMPORT CFS") {
                alert("Please enter a valid Firms Code before proceeding");
                document.getElementById('subType').value = "";
            }
        }
        function addTradingPartner(nonAccounting) {
            var accountName = document.tradingPartnerForm.accountName;
            var unLocCode = document.tradingPartnerForm.unLocCode.value;
            var city = document.tradingPartnerForm.city.value;
            if (trim(accountName.value) == "") {
                alert("Please enter the Account Name");
                accountName.value = "";
                accountName.focus();
                return;
            }
            if (trim(city) == "") {
                alert("Please enter the City");
                city.value = "";
                document.tradingPartnerForm.city.focus();
                return;
            }
            var accountTypes = document.getElementsByName("accountTypes");
            var noAccountType = true;
            var accountType = "";
            var subType = "";
            if (nonAccounting) {
                if (document.getElementById("fmcNo").style.display == "none") {
                    if (confirm("Forwarder?")) {
                        accountType = "V";
                        subType = "Forwarders";
                        noAccountType = false;
                    }
                } else {
                    accountType = "V";
                    subType = "Forwarders";
                    noAccountType = false;
                }
            }
            for (var index = 0; index < accountTypes.length; index++) {
                if (accountTypes[index].checked) {
                    noAccountType = false;
                    accountType += accountTypes[index].value + ",";
                }
            }
            if (noAccountType) {
                alert("Please select atleast one Account Type");
                return;
            }
            if (accountType.indexOf("V") > -1) {
                for (var index = 0; index < accountTypes.length; index++) {
                    if (accountTypes[index].value == "V") {
                        accountTypes[index].checked = true;
                    }
                }
                if (trim(subType) == "") {
                    subType = document.tradingPartnerForm.subType.value;
                }
                if (trim(subType) == "") {
                    alert("Please select Sub Type");
                    return;
                }
            }
            var importQuoteColoadRetail = "";
            if (accountType.indexOf("C") > -1) {
                var retail = document.getElementById("Retail").checked;
                var coload = document.getElementById("Coload").checked;
                if (retail) {
                    importQuoteColoadRetail = (document.getElementById("Retail").value !==null && document.getElementById("Retail").value !==undefined && document.getElementById("Retail").value !=="") ? document.getElementById("Retail").value : "";
                }
                else {
                    importQuoteColoadRetail = (document.getElementById("Coload").value !==null && document.getElementById("Coload").value !==undefined && document.getElementById("Retail").value !=="") ? document.getElementById("Coload").value : "";
                }
                if (!retail && !coload) {
                    alert("Please select Imports Rating Coload/Retail");
                    return;
                }
            }
            var frieghtFmc = "";
            if (subType.indexOf("Forwarders") > -1) {
                document.getElementById("fmcNo").style.display = "block";
                frieghtFmc = document.tradingPartnerForm.frieghtFmc.value;
                if (trim(frieghtFmc) == "") {
                    alert("Please select FMC #");
                    return;
                }
            }
            var addTp = false;
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                    methodName: "existingCustomerList",
                    forward: "/jsps/Tradingpartnermaintainance/existsCustomerList.jsp",
                    param1: accountName.value,
                    param2: unLocCode
                },
                async: false,
                success: function (data) {
                    if (null != data && data != "") {
                        showPopUp();
                        var docListDiv = createHTMLElement("div", "docListDiv", "98%", "70%", document.body);
                        jQuery("#docListDiv").html(data);
                    } else {
                        addTp = true;
                    }
                }
            });
            if (addTp) {
                addNewTradingPartner(accountName.value, accountType, subType, frieghtFmc, unLocCode, importQuoteColoadRetail);
            }
        }
        function addNewTradingPartner(accountName, accountType, subType, frieghtFmc, unLocCode, importQuoteColoadRetail) {
            var active = "N";
            var master = "0";
            if (document.tradingPartnerForm.master) {
                master = document.tradingPartnerForm.master.value;
            }
            if ("${param.from}" == "layout") {
//                if(window.parent.parent.homeFrame.document.GB_frame.GB_frame){//
//		      window.parent.parent.homeFrame.document.GB_frame.GB_frame.addNewTradingPartner(accountName,accountType,master,subType,active,frieghtFmc,unLocCode);
                window.parent.parent.addNewTradingPartner(accountName, accountType, master, subType, active, frieghtFmc, unLocCode, importQuoteColoadRetail);
//                }else{
//		     alert("GB_frame");
//                    window.parent.parent.homeFrame.document.GB_frame.addNewTradingPartner(accountName,accountType,master,subType,active,frieghtFmc,unLocCode);
//                }
                window.parent.parent.GB_hide();
            } else {
                window.parent.parent.addNewTradingPartner(accountName, accountType, master, subType, active, frieghtFmc, unLocCode, importQuoteColoadRetail);
                window.parent.parent.GB_hide();
            }
        }
        function proceedToAdd() {
            cancelAdd();
            var nonAccounting =${nonAccounting};
            var accountName = document.tradingPartnerForm.accountName;
            var unLocCode = document.tradingPartnerForm.unLocCode.value;
            var accountTypes = document.getElementsByName("accountTypes");
            var accountType = "";
            var subType = "";
            if (nonAccounting) {
                if (document.getElementById("fmcNo").style.display == "none") {
                    if (confirm("Forwarder?")) {
                        accountType = "V";
                        subType = "Forwarders";
                    }
                } else {
                    accountType = "V";
                    subType = "Forwarders";
                }
            }
            for (var index = 0; index < accountTypes.length; index++) {
                if (accountTypes[index].checked) {
                    accountType += accountTypes[index].value + ",";
                }
            }
            if (accountType.indexOf("V") > -1) {
                for (var index = 0; index < accountTypes.length; index++) {
                    if (accountTypes[index].value == "V") {
                        accountTypes[index].checked = true;
                    }
                }
                if (trim(subType) == "") {
                    subType = document.tradingPartnerForm.subType.value;
                }

            }
            var frieghtFmc = "";
            if (subType.indexOf("Forwarders") > -1) {
                document.getElementById("fmcNo").style.display = "block";
                frieghtFmc = document.tradingPartnerForm.frieghtFmc.value;
            }
            addNewTradingPartner(accountName.value, accountType, subType, frieghtFmc, unLocCode,"");
        }
        function cancelAdd() {
            document.body.removeChild(document.getElementById("docListDiv"));
            closePopUp();
        }
        function checkForExistingRecord(obj) {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                    methodName: "getCustomerOfAccountTypeZ"
                },
                async: false,
                success: function (data) {
                    if (data != "") {
                        alert('Company with account type Z already exists');
                        obj.checked = false;
                    } else {
                        obj.checked = true;
                    }
                }
            });
        }
        function showSubType(obj) {
            if (obj.checked) {
                document.getElementById('subTypeContent').style.display = "block";
            } else {
                document.getElementById('subTypeContent').style.display = "none";
            }
        }
        function showColoadRetail(obj) {
            if (obj.checked) {
                document.getElementById('coloadRetail').style.display = "block";
            } else {
                document.getElementById('coloadRetail').style.display = "none";
            }
        }
        function init(value, callFrom, acctName, city, unlocCode) {
            var lclFlag = "";
            if (callFrom != null && callFrom != "" && callFrom != undefined) {
                var from = callFrom.toUpperCase().indexOf("LCL");
                var to = callFrom.toUpperCase().indexOf("_");
                var obj = document.getElementById('showCoload');
                lclFlag = callFrom.substring(from, to);
            }
            if ("" != lclFlag) {
                lclTPDetails(lclFlag, acctName, city, unlocCode);
            }
            var accountTypes = document.getElementsByName("accountTypes");
            for (var index = 0; index < accountTypes.length; index++) {
                if ((value == 'shipperNameBL' || value == 'shipperNameMasterBL' || value == 'shipperName') && accountTypes[index].value == 'S') {
                    accountTypes[index].checked = true;
                }
                if ((value == 'consigneeNameBL' || value == 'houseConsigneeNameBL' || value == 'consigneename') && accountTypes[index].value == 'C') {
                    accountTypes[index].checked = true;
                    showColoadRetail(obj);
                }
                if ((value == 'notifyPartyNameBL' || value == 'houseNotifyPartyNameBL') && accountTypes[index].value == 'N') {
                    accountTypes[index].checked = true;
                }
            }
        }
        if (document.getElementById("city")) {
            new Ajax.ScrollAutocompleter("city", "cityChoices", rootPath + "/servlet/AutoCompleterServlet?action=City&textFieldId=city", {
                paramName: "city",
                tokens: "/",
                afterUpdateElement: function (text, li) {
                    var list = li.id.split("@");
                    $("city").value = list[0];
                    $("cityCheck").value = list[0];
                    $("unLocCode").value = list[1];
                    $("unLocCodeCheck").value = list[1];
                    $("city").blur();
                }
            });
            Event.observe("city", "blur", function (event) {
                var element = Event.element(event);
                if (element.value !== $("cityCheck").value) {
                    $("city").value = "";
                    $("cityCheck").value = "";
                    $("unLocCode").value = "";
                    $("unLocCodeCheck").value = "";
                }
            });
        }
        function lclTPDetails(lclFlag, acctName, city, unlocCode) {
            if (lclFlag == "LCL") {
                document.getElementById("accountName").value = acctName;
                document.getElementById("city").value = city;
                document.getElementById("unLocCode").value = unlocCode;
            } else {
                document.getElementById("city").value = "";
                document.getElementById("unLocCode").value = "";
                document.getElementById("accountName").value = "";
            }
        }
    </script>
</html>

