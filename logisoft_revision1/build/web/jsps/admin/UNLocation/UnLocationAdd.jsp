<%-- 
    Document   : UNLocationAdd
    Created on : Sep 3, 2010, 7:14:02 PM
    Author     : gayatri
--%>
<%@ page language="java"  import="com.gp.cong.logisoft.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="../../includes/baseResources.jsp"%>
<%@include file="../../includes/resources.jsp"%>
<%@include file="../../includes/jspVariables.jsp"%>
<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil"%>
<html>
    <head>
        <%DBUtil db = new DBUtil();
            request.setAttribute("group", db.getGenericFCLCode(new Integer(78), ""));%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../../fragment/formSerialize.jspf"  %>
        <title>JSP for UNLocationAdd Form</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>

        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery.autocomplete.css"/>
        <script type="text/javascript" src="${path}/autocompleter/js/jquery.autocomplete.js"></script>

        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript">
            start = function () {
                serializeForm();
            }
            window.onload = start;
        </script>
        <style type="text/css">
            div.autocomplete ul{
                width: 400px;
                height: 200px;
            }
        </style>
    </head>
    <body class="whitebackgrnd">      
        <html:form action="/unlocation.do" name="searchUnLocationForm"
                   type="com.logiware.form.SearchUnLocationForm" scope="request">
            <table cellpadding="2" cellspacing="2" border="0" class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew">
                    <td colspan="8">Add UnLocationCode</td>
                </tr>
                <tr>
                    <td colspan="8">&nbsp;</td>
                </tr>
                <tr>
                    <td class="textlabelsBold">UnLocationCode&nbsp;</td>
                    <td valign="top">
                        <html:text property="unLocationCode" size="20" styleId="unLocationCode" onchange="toUppercase(this);duplicateCheck();"
                                   styleClass="textlabelsBoldForTextBox mandatory" maxlength="5"
                                   value="${searchUnLocationForm.unLocationCode}"/>
                    </td>
                    <td class="textlabelsBold">UNLocation Name&nbsp;</td>
                    <td valign="top">
                        <html:text property="unLocationName" size="20"  
                                   styleClass="textlabelsBoldForTextBox mandatory" maxlength="50"
                                   value="${searchUnLocationForm.unLocationName}"/>
                    </td>
                    <td class="textlabelsBold">Country&nbsp;</td>
                    <td valign="top">
                        <%--<c:choose>
                            <c:when test="${update == 'update'}">
                                <html:text property="countryName" size="20" styleId="countryName"  styleClass="textlabelsBoldForTextBoxDisabledLook" maxlength="50" value="${searchUnLocationForm.codeDesc}" readonly="true"/>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>--%>
                        <html:text property="countryName" size="20" styleId="countryName"  styleClass="textlabelsBoldForTextBox mandatory" maxlength="50" value="${searchUnLocationForm.codeDesc}"/>
                        <html:hidden property="countryId" styleId="countryId"/>
                        <input type="hidden" name="countryNameCheck" id="countryNameCheck" value="${searchUnLocationForm.codeDesc}"/>
                        <div id="countryNameChoices" style="display: none" class="autocomplete"></div>
                    </td>
                    <td class="textlabelsBold">State&nbsp;</td>
                    <td valign="top">
                        <%--<c:choose>
                           <c:when test="${update == 'update'}">
                               <html:text property="stateName" size="20" styleId="stateName"  styleClass="textlabelsBoldForTextBoxDisabledLook" maxlength="50" value="${searchUnLocationForm.stateCodeDesc}" readonly="true"/>
                           </c:when>
                           <c:otherwise>
                           </c:otherwise>
                       </c:choose>--%>
                        <html:text property="stateName" size="20" styleId="stateName"  styleClass="textlabelsBoldForTextBox" maxlength="50" value="${searchUnLocationForm.stateCodeDesc}"/>
                        <html:hidden property="stateId" styleId="stateId"/>
                        <input type="hidden" name="stateNameCheck" id="stateNameCheck" value="${searchUnLocationForm.stateCodeDesc}"/>
                        <div id="stateNameChoices" style="display: none" class="autocomplete"></div>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBold">Synonymous City&nbsp;</td>
                    <td>
                        <div class="textlabelsBold" style="float: left">
                            <html:select property="addSynonymousCity"  styleClass="dropdown_accounting"
                                         styleId="addSynonymousCity" value="${searchUnLocationForm.addSynonymousCity}" onchange="hideSynonymousGroup();">
                                <html:option value="N">No</html:option>
                                <html:option value="Y">Yes</html:option>

                            </html:select>
                        </div>
                    </td>
                    <c:choose>
                        <c:when test="${not empty searchUnLocationForm.addSynonymousCity && searchUnLocationForm.addSynonymousCity!='N'}">
                            <c:set var="disable" value="false"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="disable" value="true"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="textlabelsBold">Synonymous Group&nbsp;</td>
                    <td>
                        <div class="textlabelsBold" style="float: left">
                            <html:select property="addSynonymousGroup"  styleClass="dropdown_accounting"
                                         styleId="addSynonymousGroup" disabled="${disable}" value="${searchUnLocationForm.addSynonymousGroup}">
                                <html:optionsCollection name="group" />
                            </html:select>
                        </div>
                    </td>
                    <td class="textlabelsBold">CY Yard&nbsp;</td>
                    <td>
                        <div class="textlabelsBold" style="float: left">
                            <html:select property="cyYard"  styleClass="dropdown_accounting"
                                         styleId="cyYard" value="${searchUnLocationForm.cyYard}" >
                                <html:option value="N">No</html:option>
                                <html:option value="Y">Yes</html:option>
                            </html:select>
                        </div>
                        <input  name="cityId" type="hidden" id="cityId" value="${searchUnLocationForm.cityId}"/>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBold">
                        Grouped City
                    </td>
                    <td>
                        <div id="groupCityBox" class="smallInputPopupBox">
                            <input name="groupCity" Class="textlabelsBoldForTextBox" id="groupCity" size="25" value="${searchUnLocationForm.groupCity}" onkeyup="getUppercase(this.value)"/>
                            <input Class="textlabelsBoldForTextBox"  name="groupCity_check" id="groupCity_check" type="hidden" value="${searchUnLocationForm.groupCity_check}" />
                            <div id="groupCity_choices" style="display: none" class="autocomplete"></div>
                            <script type="text/javascript">
                                initAutocomplete("groupCity", "groupCity_choices", "", "groupCity_check",
                                        "${path}/actions/unlocation.jsp?tabName=UNLOCATION&cityId=" + document.searchUnLocationForm.cityId.value
                                        + "&countryId=" + document.searchUnLocationForm.countryId.value + "&from=0", "");
                            </script>
                            <input type="hidden" name="groupCityId" id="groupCityId" value="${searchUnLocationForm.groupCityId}"/>
                            <input type="button" class="buttonStyleNew" value="Add" onclick="add()"/>
                        </div>
                    </td>
                    <td class="textlabelsBold">Alternate Port Name</td>
                    <td>
                        <html:text property="alternatePortName" styleId="alternatePortName" style="text-transform: uppercase"
                                   styleClass="textlabelsBoldForTextBox" value="${searchUnLocationForm.alternatePortName}"></html:text>
                        </td>
                        <td colspan="2">
                        <html:checkbox property="lclExportService" styleId="lclExportService" name="searchUnLocationForm">
                            <span class="textlabelsBold">Activate city for use by LCL exports Service</span>
                        </html:checkbox>
                    </td>
                    <td class="textlabelsBold">WareHouse Terminal#</td>
                    <td>
                        <html:text property="terminalnum" styleId="terminalnum" style="text-transform: uppercase"
                                   styleClass="textlabelsBoldForTextBox" value="${searchUnLocationForm.terminalnum}"></html:text>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td id="trackingList">
                            <div style="height: auto;border: 2px solid #F8F8FF" >

                            <%-- <c:if test="${!empty CityList}">
                                 <c:forEach items="${CityList}" var="city">
                                     <tr style="text-transform: uppercase;font-size: 10px" >
                                         <td>
                                             <c:choose>
                                                 <c:when test="${!empty city.city.stateId}">
                                                     ${city.city.unLocationName}/${city.city.stateId.code}(${city.city.unLocationCode})
                                                 </c:when>
                                                 <c:otherwise>
                                                     ${city.city.unLocationName}/${city.city.countryId.codedesc}(${city.city.unLocationCode})
                                                 </c:otherwise>
                                             </c:choose><img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12" onclick="deleteCity(${groupCity.id},${groupCity.city.id})"/>
                                         </td>
                                     </tr>
                                 </c:forEach>
                             </c:if>--%>
                            <c:if test="${!empty groupCityList}">
                                <table>
                                    <c:forEach items="${groupCityList}" var="groupCity">
                                        <tr style="text-transform: uppercase;font-size: 10px" >
                                            <td>
                                                <c:choose>
                                                    <c:when test="${!empty groupCity.groupCity.stateId}">
                                                        ${groupCity.groupCity.unLocationName}/${groupCity.groupCity.stateId.code}(${groupCity.groupCity.unLocationCode})
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${groupCity.groupCity.unLocationName}/${groupCity.groupCity.countryId.codedesc}(${groupCity.groupCity.unLocationCode})
                                                    </c:otherwise>
                                                </c:choose>
                                                <img src="${path}/jsps/LCL/images/close1.png" alt="delete" title="Delete" style="vertical-align: middle"
                                                     height="12" width="12" onclick="deleteCity(${groupCity.id},${groupCity.city.id})"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="textlabelsBold">Pier Pass&nbsp;</td>
                    <td>
                        <div class="textlabelsBold" style="float: left">
                            <html:select property="pierPass"  styleClass="dropdown_accounting"
                                         styleId="pierPass" value="${searchUnLocationForm.pierPass}">
                                <html:option value="N">No</html:option>
                                <html:option value="Y">Yes</html:option>

                            </html:select>
                        </div>
                    </td>
                    <td class="textlabelsBold">Mirror LCL rates from city&nbsp;</td>
                    <td>
                        <html:text property="mirrorLclCity" styleId="mirrorLclCity" style="text-transform: uppercase"
                                   styleClass="textlabelsBoldForTextBox" value="${searchUnLocationForm.mirrorLclCity}">
                        </html:text>
                    </td>
                    <td class="textlabelsBold">Origin Terminal#&nbsp;</td>
                    <td>
                        <html:text property="originTerminal" styleId="originTerminal" style="text-transform: uppercase"
                                   styleClass="textlabelsBoldForTextBox" value="${searchUnLocationForm.originTerminal}">
                        </html:text>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="8" style="padding-top: 10px">
                        <c:choose>
                            <c:when test="${update == 'update'}">
                                <input type="button" class="buttonStyleNew" style="width: 100px;" value='Update' onclick="updateUnlocations()"/>
                            </c:when>
                            <c:otherwise>
                                <input type="button" class="buttonStyleNew" style="width: 100px;" value='Save' onclick="saveUnlocations()"/>
                            </c:otherwise>
                        </c:choose>
                        <input type="button" class="buttonStyleNew" style="width: 100px;" value='Go Back' onclick="cancel()"/>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue"/>
            <html:hidden property="userId" value="${searchUnLocationForm.userId}"/>
        </html:form>
    </body>
    <script type="text/javascript">
        var path = "${path}";
        jQuery(document).ready(function () {
            initAutoCompleteFields();
        });
        function saveUnlocations() {
            if (trim(document.searchUnLocationForm.unLocationCode.value) == "") {
                alert("Please Enter UnLocationCode");
                document.searchUnLocationForm.unLocationCode.focus();
                return false;
            } else if (document.searchUnLocationForm.unLocationCode.value.length != 5) {
                alert("UnLocationCode Should contain 5 Characters");
                document.searchUnLocationForm.unLocationCode.focus();
                return false;
            } else if (!isAlphaNumeric(document.searchUnLocationForm.unLocationCode.value)) {
                alert("UnLocationCode Should contain only AlphaNumeric");
                document.searchUnLocationForm.unLocationCode.focus();
                return false;
            }
            else if (trim(document.searchUnLocationForm.unLocationName.value) == "") {
                alert("Please Enter UnLocationName");
                document.searchUnLocationForm.unLocationName.focus();
                return false;
            }
            else if (trim(document.searchUnLocationForm.countryName.value) == "") {
                alert("Please Enter Country");
                document.searchUnLocationForm.countryName.focus();
                return false;
            }
            else if (document.searchUnLocationForm.addSynonymousCity.value == "select") {
                alert("Please Select Synonymous City");
                document.searchUnLocationForm.addSynonymousCity.focus();
                return false;
            }
            else if ((document.searchUnLocationForm.addSynonymousCity.value == "Y") && (document.searchUnLocationForm.addSynonymousGroup.value == "")) {
                alert("Please Select Synonymous Group");
                document.searchUnLocationForm.addSynonymousGroup.focus();
                return false;
            }
            document.searchUnLocationForm.buttonValue.value = "saveUnlocations";
            document.searchUnLocationForm.submit();
        }

        function add() {
            if (trim(document.searchUnLocationForm.groupCity.value) == "") {
                alert("Please Enter Grouped City");
                document.searchUnLocationForm.groupCity.focus();
                return false;
            }
            document.searchUnLocationForm.buttonValue.value = "add";
            document.searchUnLocationForm.submit();
        }

        function deleteCity(groupCityId, cityId) {
            document.searchUnLocationForm.buttonValue.value = "deleteCity";
            document.searchUnLocationForm.groupCityId.value = groupCityId;
            document.searchUnLocationForm.cityId.value = cityId;
            document.searchUnLocationForm.submit();
        }

        function hideSynonymousGroup() {
            if (document.getElementById("addSynonymousCity").value != "Y") {
                document.getElementById("addSynonymousGroup").value = "";
                document.getElementById("addSynonymousGroup").disabled = true;
            } else {
                document.getElementById("addSynonymousGroup").disabled = false;
            }
        }
        function duplicateCheck() {
            var uncode = document.getElementById("unLocationCode").value;
            jQuery.ajaxx({
                datatype: "json",
                data: {
                    className: "com.gp.cong.logisoft.dwr.DwrUtil",
                    methodName: "checkUnLoc",
                    param1: uncode,
                    datatype: "json"
                },
                success: function (data) {
                    if (data != 'null' && data != "") {
                        alert("Please Enter Different UnLocationCode");
                        document.getElementById("unLocationCode").value = "";
                        document.getElementById("unLocationCode").focus();
                    }
                }
            });
        }
        function cancel() {
            document.searchUnLocationForm.buttonValue.value = "cancel";
            document.searchUnLocationForm.submit();
        }

        function getUppercase(val) {
            val = val.toUpperCase();
            document.searchUnLocationForm.groupCity.value = val;
        }

        function updateUnlocations() {
            if (trim(document.searchUnLocationForm.unLocationName.value) == "") {
                alert("Please Enter UnLocationName");
                document.searchUnLocationForm.unLocationName.focus();
                return false;
            }
            else if (document.searchUnLocationForm.addSynonymousCity.value == "select") {
                alert("Please Select Synonymous City");
                document.searchUnLocationForm.addSynonymousCity.focus();
                return false;
            } else if (document.searchUnLocationForm.unLocationCode.value.length != 5) {
                alert("UnLocationCode Should contain 5 Characters");
                document.searchUnLocationForm.unLocationCode.focus();
                return false;
            }
            else if ((document.searchUnLocationForm.addSynonymousCity.value == "Y") && (document.searchUnLocationForm.addSynonymousGroup.value == "")) {
                alert("Please Select Synonymous Group");
                document.searchUnLocationForm.addSynonymousGroup.focus();
                return false;
            }
            document.searchUnLocationForm.buttonValue.value = "updateUnlocations";
            document.searchUnLocationForm.submit();
        }
        AjaxAutocompleter("countryName", "countryNameChoices", "countryId", "countryNameCheck", rootPath + "/servlet/AutoCompleterServlet?action=GenericCode&textFieldId=countryName&codeTypeId=11", null, null);
        AjaxAutocompleter("stateName", "stateNameChoices", "stateId", "stateNameCheck", rootPath + "/servlet/AutoCompleterServlet?action=State&textFieldId=stateName&codeTypeId=10", null, null);
        function initAutoCompleteFields() {
            jQuery("#terminalnum").initautocomplete({
                url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=UN_TERMINAL&template=one&fieldIndex=1&",
                width: "100px",
                resultsClass: "ac_results z-index",
                resultPosition: "absolute",
                scroll: true,
                scrollHeight: 200
            });

            jQuery("#mirrorLclCity").initautocomplete({
                url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=LCL_MIRROR_CITY&template=one&fieldIndex=1&",
                width: "130px",
                resultsClass: "ac_results z-index",
                resultPosition: "absolute",
                scroll: true,
                scrollHeight: 200
            });

            jQuery("#originTerminal").initautocomplete({
                url: path + "/autocompleter/fcl/action/getAutocompleterResults.jsp?query=UN_TERMINAL&template=one&fieldIndex=1&",
                width: "100px",
                resultsClass: "ac_results z-index",
                resultPosition: "absolute",
                scroll: true,
                scrollHeight: 200
            });
        }
    </script>
</html>
