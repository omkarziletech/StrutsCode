<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ include file="../includes/jspVariables.jsp" %>
<%@ include file="../includes/resources.jsp" %>
<%@ include file="../includes/baseResources.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <base href="${basePath}">
        <title>My JSP 'SedFiling.jsp' starting page</title>
        <link rel="stylesheet" href="<c:url value="/css/script-aculo-us.tabs.css"/>" title="script-aculo-us.tabs">
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/script-aculo-us.tabs.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/fcl/sedFiling.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script language="javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script language="javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script language="javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
    </head>
    <body class="whitebackgrnd" onload="gotoSelectedTab()">
        <div id="cover" style="width: expression(document.body.offsetWidth+'px');height: expression(document.body.offsetHeight+120+'px');display: none"></div>
         <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                  onclick="document.getElementById('AlertBox').style.display='none';
       					grayOut(false,'');">
            </form>
        </div>
        <html:form action="/sedFiling" type="com.gp.cong.logisoft.struts.form.SedFilingForm" name="sedFilingForm" styleId="sedFilingPage" scope="request">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                        <td >Add/Edit Schedule B</td>
                         <td align="right">
                             <input type="button"  value="Close" class="buttonStyleNew" onclick="closeSchedB('${index}')">
                             <input type="button"  value="Add SchedB" class="buttonStyleNew" onclick="openSchedDiv()">
                         </td>
                </tr>
                <tr>
                    <td valign="top">
                        <div id="ShowValues" style="display: none;">
                            <table width="100%" cellpadding="1" cellspacing="0" border="0">
                                <tr class="textlabelsBold">
                                    <td>Origin D/F</td>
                                    <td colspan="3">
                                        <html:select property="domesticOrForeign" styleClass="dropdown_accounting mandatory" value="${schedB.domesticOrForeign}"  styleId="domesticOrForeign">
                                            <html:option value="D">Domestic</html:option>
                                            <html:option value="F">Foreign</html:option>
                                        </html:select>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Schedule B Name</td>
                                    <td>
                                        <c:choose>
                                        <c:when test="${schedB.exportInformationCode== 'HH'}">
                                            <input name="scheduleB_Name" style="font-weight: bold;" Class="textlabelsBoldForTextBox"
                                               style="text-transform: uppercase"  value="${schedB.scheduleBName}" id="scheduleB_Name" size="80" maxlength="200"/>
                                        <input name="schedBName_check" id="schedBName_check1" type="hidden"
                                               value="${schedB.scheduleBName}"/>
                                        <div id="schedBName_choices"  style="display:none;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                                initAutocompleteWithFormClear("scheduleB_Name","schedBName_choices","scheduleB_Number","schedBName_check",
                                                "${path}/actions/aesFiling.jsp?searchBy=Name","fillSchedBInfo()","");
                                        </script>
                                        </c:when>
                                        <c:otherwise>
                                            <input name="scheduleB_Name" style="font-weight: bold;" class="textlabelsBoldForTextBox mandatory"
                                               style="text-transform: uppercase"  value="${schedB.scheduleBName}" id="scheduleB_Name" size="80" maxlength="200"/>
                                        <input name="schedBName_check" id="schedBName_check" type="hidden"
                                               value="${schedB.scheduleBName}"/>
                                        <div id="schedBName_choices"  style="display:none;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                                initAutocompleteWithFormClear("scheduleB_Name","schedBName_choices","scheduleB_Number","schedBName_check",
                                                "${path}/actions/aesFiling.jsp?searchBy=Name","fillSchedBInfo()","");
                                        </script>
                                        </c:otherwise>
                                        </c:choose>
                                        
                                    </td>
                                    <td>Schedule B number</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${schedB.exportInformationCode== 'HH'}">
                                               <input name="scheduleB_Number" style="font-weight: bold;" Class="textlabelsBoldForTextBox"
                                               style="text-transform: uppercase"  value="${schedB.scheduleBNumber}" id="scheduleB_Number1"/>
                                        <input name="schedB_check" id="schedB_check" type="hidden"
                                               value="${schedB.scheduleBNumber}"/>
                                        <div id="schedB_choices"  style="display:none;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                                initAutocompleteWithFormClear("scheduleB_Number","schedB_choices","scheduleB_Name","schedB_check",
                                                "${path}/actions/aesFiling.jsp?searchBy=Number","fillSchedBInfo()","");
                                        </script> 
                                            </c:when>
                                        <c:otherwise>
                                        <input name="scheduleB_Number" style="font-weight: bold;" class="textlabelsBoldForTextBox mandatory"
                                               style="text-transform: uppercase"  value="${schedB.scheduleBNumber}" id="scheduleB_Number"/>
                                        <input name="schedB_check" id="schedB_check" type="hidden"
                                               value="${schedB.scheduleBNumber}"/>
                                        <div id="schedB_choices"  style="display:none;" class="autocomplete"></div>
                                        <script type="text/javascript">
                                                initAutocompleteWithFormClear("scheduleB_Number","schedB_choices","scheduleB_Name","schedB_check",
                                                "${path}/actions/aesFiling.jsp?searchBy=Number","fillSchedBInfo()","");
                                        </script>
                                        </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Description 1</td>
                                    <td colspan="3"><html:text property="description1" styleClass="textlabelsBoldForTextBox mandatory"  styleId="description1" value="${schedB.description1}" maxlength="50" style="text-transform: uppercase" size="50"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                   <td>Description 2</td>
                                   <td colspan="3"><html:text property="description2" styleClass="textlabelsBoldForTextBox"  styleId="description2" value="${schedB.description2}" maxlength="50" style="text-transform: uppercase" size="50"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Quantities 1</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${ not empty schedB.units1 && schedB.quantities1 != 0}">
                                                <html:text property="quantities1" styleClass="textlabelsBoldForTextBox mandatory"  styleId="quantities1" value="${schedB.quantities1}"  onkeyup="onlyNumbers(this)" maxlength="10"/>
                                            </c:when>
                                            <c:when test="${schedB.quantities1 != 0}">
                                                <html:text property="quantities1" styleClass="textlabelsBoldForTextBox"  styleId="quantities1" value="${schedB.quantities1}"  onkeyup="onlyNumbers(this)" maxlength="10"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text property="quantities1" styleClass="textlabelsBoldForTextBox"  styleId="quantities1" value=""  onkeyup="onlyNumbers(this)" maxlength="10"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>UOM 1</td>
                                    <td><html:text property="uom1" styleClass="textlabelsBoldForTextBox"  styleId="uom1" value="${schedB.uom1}" maxlength="50"  size="20" style="text-transform: uppercase"/></td>
                                    <html:hidden property="units1" styleId="units1" value="${schedB.units1}"/>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Quantities 2</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${schedB.quantities2 != 0}">
                                                <html:text property="quantities2" styleClass="textlabelsBoldForTextBox"  styleId="quantities2" value="${schedB.quantities2}"  onkeyup="onlyNumbers(this)" maxlength="10"/>
                                            </c:when>
                                            <c:otherwise>
                                                <html:text property="quantities2" styleClass="textlabelsBoldForTextBox"  styleId="quantities2" value=""  onkeyup="onlyNumbers(this)" maxlength="10"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td> UOM 2</td>
                                    <td><html:text property="uom2" styleClass="textlabelsBoldForTextBox"  styleId="uom2" value="${schedB.uom2}" maxlength="50"  size="20" style="text-transform: uppercase"/></td>
                                    <html:hidden property="units2" styleId="units2" value="${schedB.units2}"/>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Weight</td>
                                    <td><html:text property="weight" styleClass="textlabelsBoldForTextBox mandatory"  styleId="weight" value="${schedB.weight}"  onkeyup="onlyNumbers(this)" maxlength="10"/></td>
                                    <td> Weight Type</td>
                                    <td>
                                        <html:select property="weightType" styleId="weightType" styleClass="dropdown_accounting " name="sedFilingForm" value="${schedB.weightType}">
                                            <html:option value="K">Kilos</html:option>
                                            <html:option value="L">Pounds</html:option>
                                        </html:select>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Value</td>
                                    <td colspan="3"><html:text property="value" styleClass="textlabelsBoldForTextBox mandatory"  styleId="value" value="${schedB.value}" maxlength="10"  onkeyup="onlyNumbers(this)"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Used Vehicle </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${schedB.usedVehicle == 'Y'}">
                                                <input type="radio" name="usedVehicle" id="usedVehicle" value="Y" checked/>Yes
                                                <input type="radio" name="usedVehicle" id="usedVehicle" value="N" />No
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="usedVehicle" id="usedVehicle" value="Y" />Yes
                                                <input type="radio" name="usedVehicle" id="usedVehicle" value="N" checked/>No
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>Vehicle ID Type</td>
                                    <td>
                                        <html:select property="vehicleIdType" styleId="vehicleIdType" styleClass="dropdown_accounting " name="sedFilingForm" value="${schedB.vehicleIdType}">
                                            <html:option value="1">VIN</html:option>
                                            <html:option value="2">Product ID</html:option>
                                        </html:select>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Vehicle ID Number</td>
                                    <td><html:text property="vehicleIdNumber" styleClass="textlabelsBoldForTextBox"  styleId="vehicleIdNumber" value="${schedB.vehicleIdNumber}" maxlength="25"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Vehicle Title Number</td>
                                    <td><html:text property="vehicleTitleNumber" styleClass="textlabelsBoldForTextBox"  styleId="vehicleTitleNumber" value="${schedB.vehicleTitleNumber}" maxlength="15" style="text-transform: uppercase"/></td>
                                    <td>Vehicle State</td>
                                    <td>
                                        <%--<html:text property="vehicleState" styleClass="textlabelsBoldForTextBox"  styleId="vehicleState" value="${schedB.vehicleState}" style="text-transform: uppercase"/>--%>
                                        <html:select property="vehicleState" styleClass="dropdown_accounting" style="width:247px;" value="${schedB.vehicleState}" styleId="vehicleState">
                                            <html:optionsCollection name="stateCodeList" />
                                        </html:select>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td> Export Code</td>
                                    <td><div class="mandatory" style="float:left">
                                        <%--<html:text property="exportInformationCode" styleClass="textlabelsBoldForTextBox"  styleId="exportInformationCode" maxlength="2" value="${schedB.exportInformationCode}" style="text-transform: uppercase"/>--%>
                                        <html:select property="exportInformationCode" styleClass="dropdown_accounting" style="width:247px;" value="${schedB.exportInformationCode}" styleId="exportInformationCode" onchange="removemandatory()">
                                            <html:optionsCollection name="exportCodeList" /></div>
                                        </html:select>
                                    </td>
                                    <td>ECCN</td>
                                    <td><html:text property="eccn" styleClass="textlabelsBoldForTextBox"  styleId="eccn" value="${schedB.eccn}" maxlength="5" style="text-transform: uppercase"/></td>
                                </tr>
                                <tr class="textlabelsBold">
                                    <td>Export Licence</td>
                                    <td><html:text property="exportLicense" styleClass="textlabelsBoldForTextBox"  styleId="exportLicense" value="${schedB.exportLicense}" maxlength="12" style="text-transform: uppercase"/></td>
                                    <td>License Code</td>
                                    <td><div class="mandatory" style="float:left">
                                        <html:select property="licenseType" styleClass="dropdown_accounting" style="width:247px;" value="${schedB.licenseType}" styleId="licenseType">
                                            <html:optionsCollection name="licenseCodeList" /></div>
                                        </html:select>
                                    </td>
                                </tr>
                                <tr class="textlabelsBold">
                                <td>Total License Value</td>
                                <td><html:text property="totalLicenseValue" styleClass="textlabelsBoldForTextBox mandatory"  styleId="totalLicenseValue" value="${schedB.totalLicenseValue}" maxlength="10" onblur="setZeroDefault(this)" onkeyup="onlyNumbers(this)" /></td>   
                                </tr>
                                <tr>
                                    <td colspan="6" align="center">
                                        <div id="submitDiv">
                                            <input type="button" value="Submit" id="SubmitSchB"  class="buttonStyleNew" onclick="saveValues('submit')">
                                            <input type="button" value="Cancel" class="buttonStyleNew" onclick="closeDiv()">
                                        </div>
                                        <div id="updateDiv" style="display: none">
                                            <input type="button" value="Update" id="updateSchB"  class="buttonStyleNew" onclick="saveValues('update')">
                                            <input type="button" value="Cancel" class="buttonStyleNew" onclick="closeDiv()">
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
                 <tr>
                     <td colspan="2">
                        <table cellspacing="0" cellpadding="1"  width="100%" border="0" class="tableBorderNew" >
                            <tr class="tableHeadingNew">
                                <td>Schedule B Details<td>
                            </tr>
                            <tr>
                                <td>
                                    <c:set var="sed" value="0"></c:set>
                                    <c:if test="${!empty schedList}">
                                        <display:table name="${schedList}" class="displaytagstyleNew" pagesize="15"
                                                       id="sedFiling" sort="list" requestURI="/sedFiling.do?" >
                                            <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner">
                                                    <font color="blue">{0}</font>Filenames Displayed,For more Data click on Page Numbers.
                                                    <br>
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.one_items_found">
                                                <span class="pagebanner">
                                                                One {0} displayed. Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.all_items_found">
                                                <span class="pagebanner">
                                                                {0} {1} Displayed, Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="basic.msg.empty_list">
                                                <span class="pagebanner">
                                                                    No Records Found.
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.placement" value="bottom" />
                                            <display:setProperty name="paging.banner.item_name" value="Filename"/>
                                            <display:setProperty name="paging.banner.items_name" value="Filenames"/>
                                            <display:column  property="scheduleBNumber" sortable="true" title="SCHEDB #"></display:column>
                                            <display:column  property="scheduleBName" sortable="true" title="SCHEDB NAME"></display:column>
                                            <display:column  sortable="true" title="QUANTITY1">
                                                <c:if test="${sedFiling.quantities1 != 0}">
                                                    ${sedFiling.quantities1}
                                                </c:if>
                                            </display:column>
                                            <display:column  sortable="true" title="QUANTITY2">
                                                <c:if test="${sedFiling.quantities2 != 0}">
                                                    ${sedFiling.quantities2}
                                                </c:if>
                                            </display:column>
                                            <display:column property="units1" title="UNIT1" sortable="true"></display:column>
                                            <display:column property="units2" title="UNIT2" sortable="true" ></display:column>
                                            <display:column property="weight" sortable="true" title="WEIGHT"></display:column>
                                            <display:column property="value" sortable="true" title="VALUE"></display:column>
                                            <display:column title="ACTION">
                                                <span style="cursor: pointer;">
                                                    <img src="<c:url value="/img/icons/edit.gif"/>" border="0"
                                                         onclick="showMoreInfo('${sedFiling.id}')"/>
                                                </span>
                                                <span style="cursor: pointer;">
                                                    <img src="<c:url value="/img/icons/delete.gif"/>" border="0"
                                                         onclick="deleteInfo('${sedFiling.id}')"/>
                                                </span>
                                            </display:column>
                                            <c:set var="sed" value="${sed+1}"></c:set>
                                        </display:table>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" value="${sedFilingForm.buttonValue}"/>
            <html:hidden property="schedId" value="${schedId}"/>
            <html:hidden property="shpdr" value="${shpdr}"/>
            <html:hidden property="trnref" value="${sedFilingForm.trnref}"/>
            <input type="hidden" id="schedSize" value="${sed}"/>
            <input type="hidden" id="index" name="index" value="${index}"/>
        </html:form>
    </body>
    <script type="text/javascript">
        if(document.getElementById('totalLicenseValue').value===""){
            document.getElementById('totalLicenseValue').value="0";
        }
    </script>
</html>
