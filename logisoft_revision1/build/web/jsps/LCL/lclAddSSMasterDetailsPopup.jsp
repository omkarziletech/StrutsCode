<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<jsp:useBean id="lclAddVoyageForm" scope="request" class="com.gp.cvst.logisoft.struts.form.lcl.LclAddVoyageForm"/>
<cong:javascript src="${path}/js/common.js"></cong:javascript>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <cong:hidden id="headerId" name="headerId" />
    <cong:hidden id="masterId" name="masterId" />
    <cong:hidden id="unitSsIds" name="unitSsIds" />
    <input type="hidden" name="accttype" id="acct_type"/>
    <input type="hidden" name="subtype" id="sub_type"/>
    <table width="100%" border="0"  style="border: 1px solid #dcdcdc">
        <tr>
            <td>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                    <tr class="tableHeadingNew">
                        <td width="20%">
                            ${not empty lclAddVoyageForm.masterId ? 'Edit':'Add'} SS Master Detail
                        </td>
                        <td width="2%">POL:</td>
                        <td width="30%" id="polName" style="color: green;" >${lclUnitsScheduleForm.polName}</td>
                        <td width="2%">POD:</td>
                        <td id="podName" style="color: green;" >${lclUnitsScheduleForm.podName}</td>
                    </tr>
                </table>
                <br/>
                <table>
                    <tr>
                        <td>
                            <table>
                                <tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Booking#</cong:td>
                                    <cong:td>
                                        <cong:text name="bookingNumber" id="bookingNumber" styleClass="mandatory textWidth260 textuppercaseLetter" maxlength="50" />
                                    </cong:td>
                                </tr>
                                <tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Shipper</cong:td>
                                    <cong:td>
                                        <cong:autocompletor  name="shipperAccountNo" styleClass="textWidth260 textlabelsBoldForTextBox textCapsLetter" id="shipperAccountNo"
                                                             fields="shipperAccountNumber,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,shipperEdi"
                                                             shouldMatch="true" width="600" scrollHeight="300px" focusOnNext="true"
                                                             query="SHIPPER_SS_MASTER" template="tradingPartner" container="null" callback="shipper_AccttypeCheck()"/>
                                    </cong:td>
                                </tr>
                                <tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Shipper Acct#</cong:td>
                                    <cong:td>
                                        <cong:text id="shipperAccountNumber" name="shipperAccountNumber" readOnly="true"
                                                   styleClass="textlabelsBoldForTextBoxDisabledLook textWidth260"/>
                                    </cong:td>
                                </tr>
                                <tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Shipper Address</cong:td>
                                    <cong:td>
                                        <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox"
                                                       property="shipperEdi" styleId="shipperEdi" />
                                    </cong:td>
                                </tr>
                                <tr>
                                    <cong:td styleClass="textlabelsBoldforlcl">Notify</cong:td>
                                    <cong:td>
                                    <span id="dojoNotify">
                                        <cong:autocompletor  name="notifyAccountNo" styleClass="textWidth260 textlabelsBoldForTextBox textCapsLetter" id="notifyAccountNo" position="right"
                                                             fields="notifyAccountNumber,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,notifyEdi"
                                                             shouldMatch="true" width="600" scrollHeight="300px" callback="notify_AccttypeCheck()"
                                                             query="CLIENT" template="tradingPartner" container="null" />
                                    </span>
                                    <span id="manualNotify" style="display:none">
                                        <cong:text name="manualNotyName" id="manualNotyName" styleClass="textWidth260 textlabelsBoldForTextBox" style="text-transform: uppercase"/></span>
                                    <input type="checkbox" id="newNotify" name="newNotify" onclick="newNotyName();" style="vertical-align: middle;"
                                           title="New"/>
                                    <input type="checkbox" id="sameasConsignee" name="sameasConsignee" onclick="insertSSMasterNotifyParty();" style="vertical-align: middle;"
                                           title="Same as consignee"/>
                                </cong:td>
                    </tr>
                    <tr>
                        <td class="textlabelsBoldforlcl">Notify Acct#</td>
                        <td>
                            <cong:text id="notifyAccountNumber" name="notifyAccountNumber" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook textWidth260"/>
                        </td>
                    </tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Notify Address</cong:td>
                        <cong:td>
                            <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox"  property="notifyEdi" styleId="notifyEdi"/>
                        </cong:td>
                    </tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Move Type</cong:td>
                        <cong:td>
                            <html:select property="moveType" styleId="moveType"
                                         styleClass="smallDropDown textlabelsBoldforlcl mandatory"
                                         style="width:134px">
                                <html:option value="">Select</html:option>
                                <html:option value="DOOR TO DOOR">DOOR TO DOOR</html:option>
                                <html:option value="DOOR TO PORT">DOOR TO PORT</html:option>
                                <html:option value="PORT TO DOOR">PORT TO DOOR</html:option>
                                <html:option value="PORT TO PORT">PORT TO PORT</html:option></html:select>
                        </cong:td>
                    </tr>
                </table>
            </td>
            <td>
                <table>
                    <tr>
                        <td class="textlabelsBoldforlcl">Master BL#</td>
                        <td>
                            <cong:text name="ssMasterBl" id="ssMasterBl" styleClass="textWidth260 textuppercaseLetter" maxlength="20"/>
                            <input type="checkbox" id="copyMasterBlId" name="copyMasterBl" onclick="copyMasterBls();" style="vertical-align: middle;"
                                   title="Copy Booking#" />
                        </td>
                    </tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Contract#</cong:td>
                        <cong:td>
                            <c:set var="contractNo" value="${empty contractNumber ? lclAddVoyageForm.contractNumber : contractNumber}"/>
                            <cong:text name="contractNumber" id="contractNumber" value="${contractNo}" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook textWidth260" />
                        </cong:td>
                    </tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Consignee/Agent</cong:td>
                        <cong:td>
                            <cong:autocompletor  name="consigneeAccountNo" styleClass="textWidth260 textlabelsBoldForTextBox textCapsLetter" id="consigneeAccountNo"
                                                 fields="consigneeAccountNumber,acct_type,sub_type,NULL,NULL,NULL,NULL,NULL,consigneeEdi"
                                                 shouldMatch="true" width="600" scrollHeight="300px" query="CONSIGNEE_SS_MASTER" template="tradingPartner" container="null"
                                                 callback="consignee_AccttypeCheck()"/>
                        </cong:td>
                    </tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Consignee/Agent Acct#</cong:td>
                        <cong:td>
                            <cong:text id="consigneeAccountNumber" name="consigneeAccountNumber" readOnly="true" styleClass="textlabelsBoldForTextBoxDisabledLook textWidth260"/>
                        </cong:td>
                    </tr>
                    <tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Consignee Address</cong:td>
                        <cong:td>
                            <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox"  property="consigneeEdi" styleId="consigneeEdi"/>
                        </cong:td>
                    </tr>
                    <tr> 
                        <td colspan="2" style="padding-bottom:3.0em;">
                            <%--  <cong:td styleClass="textlabelsBoldforlcl">Special Instructions</cong:td>
                                            <cong:td>
                                                <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox textAreaLimit30"
                                                               onkeyup="limitTextarea(this,15,30)" property="remarks" />
                                            </cong:td> 
                            --%>
                    </tr>
                    <tr>
                        <td colspan="2" style="padding-bottom:5.0em;">
                        </td>
                    </tr>
                </table>
            </td>
            <td style="vertical-align: top;">
                <cong:table>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Prepaid/Collect</cong:td>
                        <cong:td> <html:select property="prepaidCollect" styleId="prepaidCollect" styleClass="smallDropDown" onchange="changeBilling()">
                                <html:optionsCollection name="pcList"/>
                            </html:select> 
                        </cong:td>
                        <input type="hidden" name="existPrepaidCollect" id="existPrepaidCollect" value="${lclAddVoyageForm.prepaidCollect}"/>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Export Reference</cong:td>
                        <cong:td>
                            <html:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox"  property="exportReferenceEdi" />
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl"  width="10%">Release Clause  </cong:td>
                        <cong:td width="5%">
                            <html:select property="releaseClause" styleId="releaseClause"
                                         value="${lclAddVoyageForm.releaseClause}"
                                         styleClass="smallDropDown textlabelsBoldforlcl"
                                         style="width:120px;vertical-align: middle;"  onchange="getclause()">
                                <html:optionsCollection name="clauseList"  />
                            </html:select>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl" width="10%">Clause Description</cong:td>
                        <cong:td width="10%">
                            <cong:textarea cols="5" id="clauseDescription" rows="15"
                                           styleClass="refusedTextarea textlabelsBoldForTextBoxDisabledLook readOnly " readOnly="true"
                                           name="" value="${lclAddVoyageForm.clauseDescription}" >
                            </cong:textarea>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Dest charges PPD/COL</cong:td>
                        <cong:td>
                            <html:select property="destPrepaidCollect" styleId="destPrepaidCollect" styleClass="smallDropDown" >
                                <html:optionsCollection name="pcList"/>
                            </html:select>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td>&nbsp;</cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Print Master with words Dock Receipt&nbsp;
                            <cong:checkbox  name="printSsDockReceipt" id="printSsDockReceipt"
                                            value="${lclAddVoyageForm.printSsDockReceipt}"/>
                        </cong:td>
                    </cong:tr>
                    <cong:tr>
                        <cong:td styleClass="textlabelsBoldforlcl">Print Total of Invoice Values&nbsp;
                            <cong:checkbox  name="masterBlInvoiceValue" id="masterBlInvoiceValue"
                                            value="${lclAddVoyageForm.masterBlInvoiceValue}"/>
                        </cong:td>
                    </cong:tr>
                </cong:table>
            </td>
        </tr>
    </table>
    <br/>
    <cong:table width="100%" border="0" align="center">
        <cong:tr>
            <cong:td width="45%"></cong:td>
            <cong:td colspan="6" align="center">
                <input type="button" value="Save" align="center" class="button-style1" onclick="saveSSMasterDetail();"/>
            </cong:td>
        </cong:tr>
        <cong:tr><cong:td colspan="8"></cong:td></cong:tr>
    </cong:table>
    <br/>
    <c:if test="${not empty unitSsMasterList}">
        <table class="display-table" id="unitSSTable">
            <thead>
                <tr>
                    <th>Unit#</th>
                    <th  width="25%">BL Body</th>
                    <th>Piece</th>
                    <th>CBM</th>
                    <th>KGS</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="master" items="${unitSsMasterList}" varStatus="masterIndex">
                    <c:set var="rowStyle" value="${rowStyle eq 'oddStyle' ? 'evenStyle' : 'oddStyle'}"/>
                    <tr class="${rowStyle}">
                        <td>${master.unitNo}</td>
                        <td><span title="${master.blBody}">${fn:substring(master.blBody,0,40)}</span></td>
                        <td>${master.totalPiece}</td>
                        <td>${master.totalVolumeMetric}</td>
                        <td>${master.totalWeightMetric}</td>
                        <td>
                            <img src="${path}/images/edit.png" width="16" height="16" title="Edit SS Master Detail"  
                                 onClick ="openSsMasterDetails('${path}', '${master.unitSsId}', '${masterIndex.index}');"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table> 
    </c:if>
</td>
</tr>
</table>

</cong:form>
<script type="text/javascript">
    jQuery(document).ready(function () {
        if ($('#masterId').val() != "" && $('#bookingNumber').val() === $('#ssMasterBl').val()) {
            $('#copyMasterBlId').attr("checked", true);
        }
        if ($('#consigneeAccountNo').val() !== "" || $('#consigneeAccountNumber').val() !== "") {
            if ($('#notifyAccountNo').val() === $('#consigneeAccountNo').val() && $('#notifyAccountNumber').val() === $('#consigneeAccountNumber').val() && $('#notifyEdi').val() === $('#consigneeEdi').val()) {
                $('#sameasConsignee').attr('checked', true);
            } else {
                $('#sameasConsignee').attr('checked', false);
            }
        }
        $('#shipperAccountNo').keyup(function () {
            var shipperAccountNo = $('#shipperAccountNo').val();
            if (shipperAccountNo == "") {
                $('#shipperAccountNumber').val('');
                $('#shipperEdi').val('');
            }
        })
        $('#consigneeAccountNo').keyup(function () {
            var consigneeAccountNo = $('#consigneeAccountNo').val();
            if (consigneeAccountNo == "") {
                $('#consigneeAccountNumber').val('');
                $('#consigneeEdi').val('');
            }
        })
        $('#notifyAccountNo').keyup(function () {
            var notifyAccountNo = $('#notifyAccountNo').val();
            if (notifyAccountNo == "") {
                $('#notifyAccountNumber').val('');
                $('#notifyEdi').val('');
            }
        })
        document.getElementById("polName").innerHTML = parent.document.getElementById("pooOrigin").value;
        document.getElementById("podName").innerHTML = parent.document.getElementById("podDestination").value;
        showNotyDetails();

        $("#bookingNumber").blur(function () {
            validateBookingNo(this);
        });
    });
    function showNotyDetails() {
        var manualNotyName = $('#manualNotyName').val();
        if (manualNotyName != "") {
            $('#newNotify').attr("checked", true);
            $('#manualNotify').show();
            $('#dojoNotify').hide();
        }
        else {
            $('#newNotify').attr("checked", false);
            $('#manualNotify').hide();
            $('#dojoNotify').show();
        }
    }
    function newNotyName() {
        if ($('#newNotify').is(":checked")) {
            $("#dojoNotify").hide();
            $("#manualNotify").show();
            $('#notifyAccountNumber').val('')
            $('#notifyEdi').val('')
            $('#notifyAccountNo').val('')
        } else {
            $("#dojoNotify").show();
            $("#manualNotify").hide();
            $('#manualNotyName').val('')
            $('#notifyEdi').val('')
        }
    }
    function insertSSMasterNotifyParty() {
        if ($('#sameasConsignee').is(":checked")) {
            $('#notifyAccountNo').val($('#consigneeAccountNo').val());
            $('#notifyAccountNumber').val($('#consigneeAccountNumber').val());
            $('#notifyEdi').val($('#consigneeEdi').val());
//            setCreditStatus('notyCreditStatusValue', 'consStatus', 'notyCreditLimitValue', 'consLimit');
        } else {
            $('#notifyAccountNo').val('');
            $('#notifyAccountNumber').val('');
            $('#notifyEdi').val('');
        }
    }
    function saveSSMasterDetail() {
        if (document.getElementById("bookingNumber") == null || document.getElementById("bookingNumber").value == "") {
            $.prompt("Booking# is required");
            $("#bookingNumber").css("border-color", "red");
            $("#warning").show();
            return false;
        }
        if ($('#moveType').val() == null || $('#moveType').val() == "") {
            $.prompt("MoveType is required");
            $("#moveType").css("border-color", "red");
            $("#warning").show();
            return false;
        }
        var headerId = parent.$("#headerId").val();
        var masterId = $("#masterId").val();
        if (headerId != null && headerId != "" && headerId != "0") {
            saveSSMasterValues(masterId);
        }
    }

    function saveSSMasterValues(masterId) {
        showLoading();
        parent.$("#bookingNumber").val($("#bookingNumber").val());
        parent.$("#contractNumber").val($("#contractNumber").val());
        parent.$("#prepaidCollect").val(document.lclAddVoyageForm.prepaidCollect.value);
        parent.$("#destPrepaidCollect").val(document.lclAddVoyageForm.destPrepaidCollect.value);
        parent.$("#shipperAccountNumber").val($("#shipperAccountNumber").val());
        parent.$("#shipperAccountNo").val($("#shipperAccountNo").val());
        parent.document.getElementById("shipperEdi").value = document.getElementById("shipperEdi").value;
        parent.document.getElementById("consigneeAccountNumber").value = document.getElementById("consigneeAccountNumber").value;
        parent.$("#consigneeAccountNo").val($("#consigneeAccountNo").val());
        parent.document.getElementById("consigneeEdi").value = document.getElementById("consigneeEdi").value;
        parent.document.getElementById("notifyAccountNumber").value = document.getElementById("notifyAccountNumber").value;
        parent.$("#notifyAccountNo").val($("#notifyAccountNo").val());
        parent.$("#manualNotyName").val($("#manualNotyName").val());
        parent.$("#moveType").val($("#moveType").val());
        parent.$("#releaseClause").val($("#releaseClause").val());
        parent.document.getElementById("notifyEdi").value = document.getElementById("notifyEdi").value;
        parent.document.getElementById("exportReferenceEdi").value = document.lclAddVoyageForm.exportReferenceEdi.value;
        parent.document.getElementById("remarks").value = "";
        parent.document.getElementById("masterId").value = masterId;
        parent.$("#printSsDockReceipt").val($("#printSsDockReceipt").is(":checked"));
        parent.$("#masterBlInvoiceValue").val($("#masterBlInvoiceValue").is(":checked"));
        parent.$("#ssMasterBl").val($("#ssMasterBl").val());
        parent.$("#unitSsIds").val($("#unitSsIds").val());
        parent.$("#methodName").val('saveSSMasterDetail');
        parent.$("#lclAddVoyageForm").submit();
    }
    function getclause() {
        var releaseClause = $("#releaseClause").val();
        $.ajaxx({
            data: {
                className: "com.gp.cong.lcl.dwr.LclDwr",
                methodName: "getClauseDescription",
                param1: releaseClause
            },
            success: function (data) {
                $("#clauseDescription").val(data);
            }
        });
    }
    function copyMasterBls() {
        var masterBlValue = $('#copyMasterBlId').is(":checked") ? $('#bookingNumber').val() : "";
        $('#ssMasterBl').val(masterBlValue);
    }
    function validateBookingNo(ele) {
        $.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO",
                methodName: "isMasterBookingAvailable",
                param1: parent.$("#headerId").val(),
                param2: $(ele).val()},
            success: function (data) {
                if (data === 'true') {
                    $.prompt("Already Booking# <font style='color:red;'> " + $(ele).val().toUpperCase() + " </font> exists in this voyage");
                    $(ele).val('');
                    return false;
                }
            }
        });
    }
    function changeBilling() {
        if ($('#masterId').val() != '') {
            $.ajaxx({
                dataType: "json",
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclSSMasterBlDAO",
                    methodName: "getCostByChangeBill",
                    param1: parent.$("#headerId").val(),
                    param2: $('#bookingNumber').val(),
                    dataType: "json"
                },
                success: function (data) {
                    if (data != null && data != '') {
                        $.prompt("Unit costs are already in accounting. Changing MBL payment terms will affect this.Do you want to continue?", {
                            buttons: {
                                Yes: 1,
                                No: 2
                            },
                            submit: function (v) {
                                if (v === 1) {
                                    $('#unitSsIds').val(data);
                                } else {
                                    $('#unitSsIds').val('');
                                    $('#prepaidCollect').val([$('#existPrepaidCollect').val()]);
                                }
                            }
                        });

                    } else {
                        $('#unitSsIds').val('');
                    }
                }
            });
        }
    }



    function openSsMasterDetails(path, unitSsId, index) {
        var href = path + "/lclAddVoyage.do?methodName=showSsMasterDetailPopUp&masterId="
                + $("#masterId").val() + "&headerId=" + $("#headerId").val() + "&unitssId=" + unitSsId + "&buttonValue=editMaster";
        $.colorbox({
            iframe: true,
            href: href,
            width: "70%",
            height: "70%",
            title: "SS Master Piece Detail"
        });
    }

</script>
