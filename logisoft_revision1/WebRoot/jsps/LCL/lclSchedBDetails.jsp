<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <%@include file="init.jsp"%>
    <%@include file="../includes/jspVariables.jsp"%>
    <%@include file="/taglib.jsp"%>
    <body class="whitebackgrnd">
        <cong:form name="lclAesDetailsForm" id="lclAesDetailsForm" action="/lclAesDetails.do">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tableBorderNew">
                <c:choose>
                    <c:when test="${newItemFlag==true}">
                        <c:set var="showHide" value="show"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="showHide" value="hide"/>
                    </c:otherwise>
                </c:choose>
                <cong:tr styleClass="tableHeadingNew" style="width:95%">
                    <cong:td width="80%">Add/Edit Schedule B</cong:td>
                    <cong:td align="right">
                        <cong:div styleClass="button-style1" style="float:left" onclick="submitAESForm('closeSched','#lclAesDetailsForm','#aesDesc')">Close</cong:div>
                        <cong:div styleClass="button-style1" style="float:left" id="addNew" onclick="openshedBDiv();">Add SchedB</cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td valign="top">
                        <cong:div id="ShowValues" styleClass="${showHide}">
                            <cong:table width="100%" cellpadding="1" cellspacing="0" border="0">
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Origin D/F</cong:td>
                                    <cong:td colspan="3">
                                        <html:select property="domesticOrForeign" styleClass="dropdown_accounting mandatory textlabelsBoldForTextBox" value="${schedB.domesticOrForeign}"  styleId="domesticOrForeign">
                                            <html:option value="D">Domestic</html:option>
                                            <html:option value="F">Foreign</html:option>
                                        </html:select>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Schedule B Name
                                        <cong:autocompletor name="scheduleBName" styleClass="textlabelsBoldForTextBox mandatory" fields="scheduleB_Number"
                                                            query="SSCHEDULENAME" scrollHeight="200px" template="two" width="450" shouldMatch="true"  value="${schedB.scheduleBName}" id="scheduleB_Name" callback="getUOMValues()"/>
                                    </cong:td>
                                    <cong:td>Schedule B number

                                        <cong:autocompletor name="scheduleBNumber" styleClass="textlabelsBoldForTextBox mandatory" fields="scheduleB_Name"
                                                            query="SSCHEDULENO" scrollHeight="200px" template="two" width="450" shouldMatch="true"  value="${schedB.scheduleBNumber}" id="scheduleB_Number"/>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Description 1</cong:td>
                                    <cong:td colspan="3"><cong:text name="description1" styleClass="textlabelsBoldForTextBox mandatory"  id="description1" value="${schedB.description1}" maxlength="50" style="text-transform: uppercase"/></cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Description 2</cong:td>
                                    <cong:td colspan="3"><cong:text name="description2" styleClass="textlabelsBoldForTextBox"  id="description2" value="${schedB.description2}" maxlength="50" style="text-transform: uppercase"/></cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Quantities 1</cong:td>
                                    <cong:td>
                                        <c:choose>
                                            <c:when test="${ not empty schedB.units1 && schedB.quantities1 != 0}">
                                                <cong:text name="quantities1" styleClass="textlabelsBoldForTextBox mandatory"  id="quantities1" value="${schedB.quantities1}"  onkeyup="checkForNumberAndDecimal(this);" maxlength="10"/>
                                            </c:when>
                                            <c:when test="${schedB.quantities1 != 0}">
                                                <cong:text name="quantities1" styleClass="textlabelsBoldForTextBox mandatory"  id="quantities1" value="${schedB.quantities1}"  onkeyup="checkForNumberAndDecimal(this);" maxlength="10"/>
                                            </c:when>
                                            <c:otherwise>
                                                <cong:text name="quantities1" styleClass="textlabelsBoldForTextBox mandatory"  id="quantities1" value=""  onkeyup="checkForNumberAndDecimal(this);" maxlength="10"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </cong:td>
                                    <cong:td>UOM 1</cong:td>
                                    <cong:td><cong:text name="uom1" styleClass="textlabelsBoldForTextBox"  id="uom1" value="${schedB.uom1}" maxlength="50" style="text-transform: uppercase"/></cong:td>
                                    <cong:hidden name="units1" id="units1" value="${schedB.units1}"/>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Quantities 2</cong:td>
                                    <cong:td>
                                        <c:choose>
                                            <c:when test="${schedB.quantities2 != 0}">
                                                <cong:text name="quantities2" styleClass="textlabelsBoldForTextBox"  id="quantities2" value="${schedB.quantities2}"  onkeyup="checkForNumberAndDecimal(this);" maxlength="10"/>
                                            </c:when>
                                            <c:otherwise>
                                                <cong:text name="quantities2" styleClass="textlabelsBoldForTextBox"  id="quantities2" value=""  onkeyup="checkForNumberAndDecimal(this);" maxlength="10"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </cong:td>
                                    <cong:td> UOM 2</cong:td>
                                    <cong:td><cong:text name="uom2" styleClass="textlabelsBoldForTextBox"  id="uom2" value="${schedB.uom2}" maxlength="50" style="text-transform: uppercase"/></cong:td>
                                    <cong:hidden name="units2" id="units2" value="${schedB.units2}"/>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Weight</cong:td>
                                    <cong:td><cong:text name="weight" styleClass="textlabelsBoldForTextBox mandatory"  id="weight" value="${schedB.weight}"  onkeyup="checkForNumberAndDecimal(this);" maxlength="10"/></cong:td>
                                    <cong:td> Weight Type</cong:td>
                                    <cong:td>
                                        <html:select property="weightType" styleId="weightType" styleClass="dropdown_accounting textlabelsBoldForTextBox" value="${schedB.weightType}">
                                            <html:option value="K">Kilos</html:option>
                                            <html:option value="L">Pounds</html:option>
                                        </html:select>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Value</cong:td>
                                    <cong:td colspan="3"><cong:text name="value" styleClass="textlabelsBoldForTextBox mandatory"  id="value" value="${schedB.value}" maxlength="10"  onkeyup="checkForNumberAndDecimal(this);"/></cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Used Vehicle </cong:td>
                                    <cong:td>
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
                                    </cong:td>
                                    <cong:td>Vehicle ID Type</cong:td>
                                    <cong:td>
                                        <html:select property="vehicleIdType" name="vehicleIdType" styleId="vehicleIdType" styleClass="dropdown_accounting textlabelsBoldForTextBox"  value="${schedB.vehicleIdType}">
                                            <html:option value="1">VIN</html:option>
                                            <html:option value="2">Product ID</html:option>
                                        </html:select>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Vehicle ID Number</cong:td>
                                    <cong:td><cong:text name="vehicleIdNumber" styleClass="textlabelsBoldForTextBox"  id="vehicleIdNumber" value="${schedB.vehicleIdNumber}" maxlength="25"/></cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Vehicle Title Number</cong:td>
                                    <cong:td><cong:text name="vehicleTitleNumber" styleClass="textlabelsBoldForTextBox"  id="vehicleTitleNumber" value="${schedB.vehicleTitleNumber}" maxlength="15" style="text-transform: uppercase"/></cong:td>
                                    <cong:td>Vehicle State</cong:td>
                                    <cong:td>
                                        <%--<cong:text name="vehicleState" styleClass="textlabelsBoldForTextBox"  id="vehicleState" value="${schedB.vehicleState}" style="text-transform: uppercase"/>--%>
                                        <html:select property="vehicleState" styleClass="dropdown_accounting textlabelsBoldForTextBox" style="width:247px;" value="${schedB.vehicleState}" styleId="vehicleState">
                                            <html:optionsCollection name="stateCodeList" />
                                        </html:select>
                                    </cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td> Export Code</cong:td>
                                    <cong:td><div style="float:left">
                                            <%--<cong:text name="exportInformationCode" styleClass="textlabelsBoldForTextBox"  id="exportInformationCode" maxlength="2" value="${schedB.exportInformationCode}" style="text-transform: uppercase"/>--%>
                                            <html:select property="exportInformationCode" styleClass="dropdown_accounting mandatory textlabelsBoldForTextBox" style="width:247px;" value="${schedB.exportInformationCode}" styleId="exportInformationCode" >
                                                <html:optionsCollection name="exportCodeList" /></div>
                                            </html:select>
                                        </cong:td>
                                        <cong:td>ECCN</cong:td>
                                    <cong:td><cong:text name="eccn" styleClass="textlabelsBoldForTextBox"  id="eccn" value="${schedB.eccn}" maxlength="5" style="text-transform: uppercase"/></cong:td>
                                </cong:tr>
                                <cong:tr styleClass="textlabelsBold">
                                    <cong:td>Export Licence</cong:td>
                                    <cong:td><cong:text name="exportLicense" styleClass="textlabelsBoldForTextBox"  id="exportLicense" value="${schedB.exportLicense}" maxlength="12" style="text-transform: uppercase"/></cong:td>
                                    <cong:td>License Code</cong:td>
                                    <cong:td><div style="float:left">
                                            <%--<cong:text name="licenseType" styleClass="textlabelsBoldForTextBox"  id="licenseType" value="${schedB.licenseType}" maxlength="3" style="text-transform: uppercase"/>--%>
                                            <html:select property="licenseType" styleClass="dropdown_accounting mandatory textlabelsBoldForTextBox" style="width:247px;" value="${schedB.licenseType}" styleId="licenseType">
                                                <html:optionsCollection name="licenseCodeList" /></div>
                                            </html:select>
                                        </cong:td>
                                    </cong:tr>
                                <tr>
                                    <td>Total License Value</td>
                                    <td>
                                        <html:text property="totalLicenseValue" styleClass="textlabelsBoldForTextBox mandatory"  styleId="totalLicenseValue" value="${schedB.totalLicenseValue}"
                                                   maxlength="10" onblur="setZeroDefault(this)" onkeyup="onlyNumbers(this)" />
                                    </td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <cong:tr>
                                    <cong:td colspan="6" align="center">
                                        <cong:div id="submitDiv">
                                            <input type="button" value="Submit" class="button-style1" style="float:left" id="saveSchedB" onclick="saveValues('saveSchedBDetails')"/>
                                            <input type="button" value="Cancel" class="button-style1" style="float:left" onclick="closeDiv();"/>
                                        </cong:div>
                                        <cong:div id="updateDiv" style="display: none">
                                            <input type="button" value="Update"  class="buttonStyleNew" onclick="saveValues('saveSchedBDetails')"/>
                                            <input type="button" value="Cancel" class="buttonStyleNew" onclick="closeDiv()"/>
                                        </cong:div>
                                    </cong:td>
                                </cong:tr>
                            </cong:table>
                        </cong:div>
                    </cong:td>
                </cong:tr>
                <cong:tr>
                    <cong:td colspan="2">
                        <cong:table cellspacing="0" cellpadding="1"  width="100%" border="0" styleClass="tableBorderNew" >
                            <cong:tr styleClass="tableHeadingNew">
                                <cong:td>Schedule B Details</cong:td>
                            </cong:tr>
                            <cong:tr>
                                <cong:td>
                                    <c:if test="${!empty schedList}">
                                        <display:table name="${schedList}" class="dataTable" pagesize="15"
                                                       id="sedFiling" sort="list" requestURI="/sedFiling.do?" >
                                            <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner">
                                                    <font color="blue">{0}</font>Filenames Displayed,For more Data click on Page Numbers.
                                                    <br/>
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
                                                    <img src="${path}/images/edit.png" border="0"
                                                         onclick="editInfo('${path}', '${sedFiling.id}', '${sedFiling.trnref}')"/>
                                                </span>
                                                <span style="cursor: pointer;">
                                                    <img src="${path}/img/icons/trash.jpg" border="0"
                                                         onclick="deleteInfo('${path}', '${sedFiling.id}', '${sedFiling.trnref}');"/>
                                                </span>
                                            </display:column>
                                        </display:table>
                                    </c:if>
                                </cong:td>
                            </cong:tr>
                        </cong:table>
                    </cong:td>
                </cong:tr>
            </table>
            <cong:hidden name="schedId" id="schedId" value="${schedB.id}"/>
            <cong:hidden name="shpdr" value="${shpdr}"/>
            <cong:hidden name="trnref1" id="trnref1" value="${trnref}"/>
            <cong:hidden name="trnref" id="trnref" value="${trnref}"/>
            <input type="hidden" id="schedSize" value="${sed}"/>
            <input type="hidden" id="methodName" name="methodName"/>
        </cong:form>
        <script type="text/javascript">
            $(document).ready(function () {
                if($('#totalLicenseValue').val()===""){
                    $('#totalLicenseValue').val("0");
                }
            });


            function congAlert(txt) {
                $.prompt(txt);
            }
            function openshedBDiv() {
                clearValues();
                $('#ShowValues').show();
                $('#submitDiv').show();
            }

            function clearValues() {
                $("#domesticOrForeign").val("D");
                $("#scheduleB_Number").val("");
                $("#scheduleB_Name").val("");
                $("#description1").val("");
                $("#description2").val("");
                $("#quantities1").val("");
                $("#units1").val("");
                $("#units2").val("");
                $("#quantities2").val("");
                $("#weight").val("");
                $("#uom1").val("");
                $("#uom2").val("");
                $("#schedId").val("");
                $("#weightType").val("K");
                $("#value").val("");
                $("#exportInformationCode").val("OS");
                $("#licenseType").val("C33");
                $("#exportLicense").val("");
                $("#usedVehicle").val("");
                $("#vehicleIdType").val("1");
                $("#vehicleIdNumber").val("");
                $("#vehicleTitleNumber").val("");
                $("#vehicleState").val("AK");
                $("#eccn").val("");
            }


            function submitAESForm(methodName, formName, selector) {
                $("#methodName").val(methodName);
                var shpdr = $('#shpdr').val();
                var params = $(formName).serialize();
                params += "&shpdr=" + shpdr;
                $.post($(formName).attr("action"), params,
                function (data) {
                    $(selector).html(data);
                    $(selector, window.parent.document).html(data);
                    parent.$.fn.colorbox.close();
                });
            }

            function closeDiv() {
                $('#ShowValues').hide();
                $('#submitDiv').hide();
            }

            function saveValues(methodName) {
                var mandotary = "";
                if (trim(document.getElementById("scheduleB_Number").value) == '' && document.getElementById("exportInformationCode").value != 'HH') {
                    mandotary = "--> Please Enter Schedule B Number<br>";
                }
                if (trim(document.getElementById("description1").value) == '') {
                    mandotary = mandotary + "--> Please Enter Description1<br>";
                }
                if (trim(document.getElementById("units1").value) == '' && trim(document.getElementById("quantities1").value) == '') {
                    mandotary = mandotary + "--> Please Enter Quantities1<br>";
                }
                if (trim(document.getElementById("weight").value) == '') {
                    mandotary = mandotary + "--> Please Enter Weight<br>";
                }
                if (trim(document.getElementById("value").value) == '') {
                    mandotary = mandotary + "--> Please Enter Value<br>";
                }
                if (trim(document.getElementById("exportInformationCode").value) == '') {
                    mandotary = mandotary + "--> Please Select Export Information Code<br>";
                }
                if (trim(document.getElementById("licenseType").value) == '') {
                    mandotary = mandotary + "--> Please Select License Code<br>";
                }
                if (mandotary != '') {
                    congAlert(mandotary);
                } else {
                    $('#methodName').val(methodName);
                    $('#lclAesDetailsForm').submit();

                }
            }
            function checkForNumberAndDecimal(obj) {
                var result;
                if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
                    obj.value = "";
                    sampleAlert("This field should be Numeric");

                }
            }

            function deleteInfo(path, id, trnref) {
                $.prompt('Are you sure you want to delete?', {
                    buttons: {
                        Yes: 1,
                        No: 2
                    },
                    submit: function (v) {
                        if (v == 1) {
                            showProgressBar();
                            var url = path + "/lclAesDetails.do?methodName=deleteSchedB&id=" + id + "&trnref=" + trnref;
                            window.location = url;
                            hideProgressBar();
                            $.prompt.close();
                        }
                        else if (v == 2) {
                            $.prompt.close();
                        }
                    }
                });
            }


            function editInfo(path, id, trnref) {
                $('#addNew').hide();
                var href = path + "/lclAesDetails.do?methodName=editSchedB&id=" + id + "&trnref=" + trnref;
                window.location = href;
            }

            function getUOMValues() {
                var schedBNo = $('#scheduleB_Number').val();
                $.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.CustAddressBC",
                        methodName: "getSchedBDesc",
                        param1: schedBNo,
                        dataType: "json"
                    },
                    success: function (data) {
                        if (undefined != data[1] && null != data[1] && data[1] != '') {
                            var uom1 = data[1].split("-->");
                            $('#uom1').val(uom1[0]);
                            $('#units1').val(uom1[1]);
                        } else {
                            $('#uom1').val('');
                            $('#units1').val('');
                        }
                        if (undefined != data[2] && null != data[2] && data[2] != '') {
                            var uom2 = data[2].split("-->");
                            $('#uom2').val(uom2[0]);
                            $('#units2').val(uom2[1]);
                        } else {
                            $('#uom2').val('');
                            $('#units2').val('');
                        }
                    }
                });
            }
            function onlyNumbers(val) {
                val.value = val.value.replace(/\s/g, '');
                if (!isInteger(val.value)) {
                    val.value = val.value.substring(0, val.value.length - 1);
                }
            }
        </script>
    </body>
</html>