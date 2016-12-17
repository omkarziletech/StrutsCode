<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
    <cong:hidden id="methodName" name="methodName"/>
    <input type="hidden" id="isManifestRole" name="isManifestRole" value="${isManifestRole}"/>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
        <tr class="tableHeadingNew">
            <td width="20%">
                Manifest
            </td>
        </tr>
    </table>
    <table border="0" class="dataTable" id="manifestDr">
        <thead>
            <tr>
                <th>File#</th>
                <th>BL#</th>
                <th>Status</th>
            </tr>
        </thead>
        <c:forEach items="${pickedDrList}" var="manifestBean">
            <tbody>
                <c:choose>
                    <c:when test="${zebra eq 'odd'}">
                        <c:set var="zebra" value="even"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="zebra" value="odd"/>
                    </c:otherwise>
                </c:choose>
                <tr class="${zebra}" >
                    <td>${manifestBean.fileNo}</td>
                    <td>${manifestBean.blNo}</td>
                    <td>
                        <c:choose>
                            <c:when test="${isManifestRole && manifestBean.state ne 'BL' && manifestBean.isNoBLRequired}">
                                 <span class="purpleBold">NO B/L Required</span>
                            </c:when>                                                       
                            <c:when test="${manifestBean.state ne 'BL'}"><span class="redBold14Px">NoBL</span></c:when>
                            <c:when test="${not empty manifestBean.postedByUserId}"><span class="purpleBold">POSTED</span></c:when>
                            <c:when test="${manifestBean.status eq 'M'}"><span class="greenBold14px">MANIFESTED</span></c:when>
                            <c:when test="${empty manifestBean.postedByUserId}"><span class="redBold14Px">POOL</span></c:when>
                        </c:choose>
                    </td>
                </tr>
            </tbody>
        </c:forEach>
    </table>
    <br/>
    <table width="100%" border="0">
        <tr>
            <td colspan="2"  align="center">
                <span class="blackBold">Are you sure you want to manifest?  </span>
            </td>
        </tr>
        <tr>
            <td colspan="2"  align="center">
                <span class="blackBold"> UNIT#</span><span class="greenBold14px">${lclAddVoyageForm.unitNo}</span>
                <span class="blackBold"> and VOYAGE# </span>
                <span class="greenBold14px">${lclAddVoyageForm.scheduleNo}</span>
            </td>
        </tr>
        <tr>
            <td width="40%">
            </td>
            <td>
                <input type="button" value="Yes"  class="button-style1"
                       onclick="manifest('${lclAddVoyageForm.unitId}', '${lclAddVoyageForm.unitssId}', '${lclAddVoyageForm.filterByChanges}');"/>
                <input type="button" value="No" id="closePopup" class="button-style1"  onclick="parent.$.colorbox.close();"/>
            </td>
        </tr>
    </table>
    <br/>
</cong:form>
<script type="text/javascript">
    function manifest(unitId, unitssId, filterByChanges) {
        var isManifestRole = $('#isManifestRole').val() === 'true' ? true : false;
        if (!isManifestRole) {
            var validateBl = validateBLStatus(unitssId);
            if (validateBl !== null && validateBl !== "") {
                $.prompt(validateBl);
                return false;
            }
        }
        
        if (isManifestRole) {
        var isCheckedNoBLRequired = checkNoBLRequiredisChecked(unitssId);
         if (isCheckedNoBLRequired !== null && isCheckedNoBLRequired !== "") {
                $.prompt(isCheckedNoBLRequired);
                return false;
            }
        }
        var termToDoBLRateType = checkTermToDoBLAndRateTypes(unitssId);
        if (!termToDoBLRateType) {
            return false;
        }
        var validateTerms = validateLclBlTerms(unitssId);
        if (validateTerms !== null && validateTerms !== "") {
                $.prompt(validateTerms);
                return false;
            }
            var validateFwdAcct = validateForwardAcct(unitssId);
            if (validateFwdAcct !== null && validateFwdAcct !== "") {
                    $.prompt(validateFwdAcct);
                    return false;
                }
        if (getManifestValueAlert(unitssId)) {
            showProgressBar();
            $("#methodName").val("validateChargesBillToParty");
            $.post($("#lclAddVoyageForm").attr("action"), $("#lclAddVoyageForm").serialize()
                    + "&unitId=" + unitId + "&unitssId=" + unitssId + "&filterByChanges=" + filterByChanges,
                    function (data) {
                        if (data !== "") {
                            $.prompt(data)
                            hideProgressBar();
                        } else {
                            hideProgressBar();
                            $.prompt("Are you sure want to Manifest this Unit?", {
                                buttons: {
                                    Ok: 1,
                                    Cancel: 2
                                },
                                submit: function (v) {
                                    if (v === 1) {
                                        $.prompt.close();
                                        submitForm(unitId, unitssId);
                                    }
                                    else if (v == 2) {
                                        $.prompt.close();
                                    }
                                }
                            });
                        }
                    });
        }
    }
    function submitForm(unitId, unitssId) {
        showProgressBar();
        parent.$("#methodName").val('manifest');
        parent.$("#unitssId").val(unitssId);
        parent.$("#unitId").val(unitId);
        parent.$("#lclAddVoyageForm").submit();
    }
    function validateBLStatus(unitssId) {
        var txt = "";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
                methodName: "isValidateManifestFiles",
                param1: unitssId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[0] !== null && data[0] !== "") {
                    txt = "File Numbers <span style=color:red>" + data[0] + "</span> not converted to BL.Please convert into BL in POSTED Status."
                } else if (data[1] !== null && data[1] !== "") {
                    txt = "File Numbers <span style=color:red>" + data[1] + "</span> not converted to BL POSTED Status.";
                } 
            }
        });
        return txt;
    }
    function checkTermToDoBLAndRateTypes(unitssId) {
        var flag = true;
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
                methodName: "isValidateTermsToDoBLAndRateType",
                param1: unitssId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[1] === 'alert') {
                    flag = false;
                }
                if (!flag) {
                    $.prompt("For File Numbers <span style=color:red>" + data[0] + "</span> Term To Do BL does not belong to the Rate Type selected (Retail/Coload/FTF).  Please correct this problem.");
                }
            }
        });
        return flag;
    }
    
    function checkNoBLRequiredisChecked(unitssId) {
        var txt = "";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
                methodName: "checkNoBlRequired",
                param1: unitssId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                if (data[0] !== null && data[0] !== "") {
                    var lengthOfData=data[0].length;
                    var oneOrMoreFiles= lengthOfData >6 ? "File Numbers ": "File Number "; 
                    txt = " For Manifest, \"NO B/L Required\" field needs to be checked for "+oneOrMoreFiles+"<span style=color:red>" + data[0] + "</span>";
                } 
            }
        });
        return txt;
    }
    
    function getManifestValueAlert(unitssId) {
        var fileFlag = true;
        $.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitDAO",
                methodName: "isCobValidation",
                param1: unitssId,
                dataType: "json"

            },
            async: false,
            success: function (data) {
                if (data !== null) {
                    $.prompt("Please check Piece,CFT,CBM,LBS and KGS  for the following files<span class='red'> " + data + "</span> it should greater than zero");
                    fileFlag = false;
                }
            }
        });
        return fileFlag;
    }
    // Mantis item: 14117
    function validateLclBlTerms(unitssId) {
        var txt = "";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
                methodName: "isValidateLclBlTerms",
                param1: unitssId,
                dataType: "json"
            },
            async: false,
            success: function (data) {
                 if (data[0] !== null && data[0] !== "" && (data[1]=== null || data[1]=== "")) {
                    txt = "BL can not be Collect for file numbers  <span style=color:red>" + data[0] + "</span>."
                } else if (data[1] !== null && data[1] !== "" && (data[0] !== null && data[0] !== "") ) {
                    txt = "BL can not be Collect .Please ensure that charges are not bill to Agent for file numbers <span style=color:red>" + data[0]+data[1] + "</span> .";
                } else if (data[1] !== null && data[1] !== "" && (data[0] === null || data[0] === "") ) {
                    txt = "BL can not be Collect .Please ensure that charges are not bill to Agent for file numbers <span style=color:red>" + data[1] + "</span> .";
                }
            }
        });
        return txt;
    }
//  Mantis item:14545
            function validateForwardAcct(unitssId) {
                var txt = "";
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO",
                        methodName: "validateForwardAccount",
                        param1: unitssId,
                        dataType: "json"
                    },
                    async: false,
                    success: function (data) {
                        if (data !== null && data !== "") {
                            txt = "Forwarder Account is required for the file numbers<span style=color:red>" + data + "</span>.";
                        }
                    }
                });
                return txt;
            }

</script>