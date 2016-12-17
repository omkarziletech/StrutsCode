<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@include file="/jsps/LCL/init.jsp" %>
<%@include file="/jsps/LCL/colorBox.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/common.js"/>
<script type="text/javascript">
    function setColor() {
        if ($('#aestable tr').length > 2) {
            parent.$('#aesB').removeClass("button-style1");
            parent.$('#Baes').removeClass("button-style1");
            parent.$('#Baes').addClass("green-background");
            parent.$('#aesB').addClass("green-background");
        } else {
            parent.$('#aesB').removeClass("green-background");
            parent.$('#Baes').removeClass("green-background");
            parent.$('#aesB').addClass("button-style1");
            parent.$('#Baes').addClass("button-style1");
        }
    }

    function goToRemarksLookUp() {
        //var remarksCode=document.QuotesForm.commentTemp.value;
        //if(remarksCode=='%'){
        //remarksCode = 'percent';
        //}
        document.getElementById('aesException').value = "";
        var href = '${path}/remarksLookUp.do?buttonValue=aesDetails';
        mywindow = window.open(href, '', 'width=700,height=400,scrollbars=yes');
        mywindow.moveTo(200, 180);
        //GB_show('Remarks Info','${path}/remarksLookUp.do?buttonValue=Quotation',
        //width="200",height="200");
    }

    function setException(exceptionValues) {
        //document.fclAESDetailsForm.exception.value=exceptionValues.toUpperCase();
        var commentVal = document.getElementById('aesException').value;
        var totalLength = commentVal.length + exceptionValues.length;
        if (totalLength > 500) {
            $.prompt('More than 500 characters are not allowed');
            return;
        }
        var oldarray = document.getElementById('aesException').value;
        var splittedArray;
        if (oldarray.length == 0) {
            splittedArray = oldarray;
        } else {
            splittedArray = oldarray.split("\n");
        }
        var newarray = exceptionValues.split(">>");
        var resultarray = new Array();
        var flag = false;
        for (var k = 0; k < newarray.length; k++) {
            flag = false;
            for (var l = 0; l < splittedArray.length; l++) {
                if (newarray[k].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ") == splittedArray[l].replace(/^[\s]+/, "").replace(/[\s]+$/, "").replace(/[\s]{2,}/, " ")) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                if (oldarray == "") {
                    oldarray = newarray[k];
                } else {
                    oldarray += "\n" + newarray[k];
                }
            }
        }
        document.getElementById('aesException').value = oldarray.replace(/>/g, "");
    }

    function saveAesITN() {
        var aesType = "";
        var result = true;
        var aesItnNumber = $('#aesItnNumber').val();
        var aesException = $('#aesException').val();
        if (aesItnNumber !== '' && aesException !== '') {
            $.prompt("Please Enter Either ITN Number or Exception");
            $("#aesItnNumber").val("");
            $("#aesException").val("");
            result = false;
        }
        else if (aesItnNumber === '' && aesException === '') {
            $.prompt("Please Enter ITN Number or Exception");
            result = false;
        }
        else if (aesItnNumber !== '' && aesException === '') {
            aesType = "AES_ITNNUMBER";
        }
        else if (aesItnNumber === '' && aesException !== '') {
            aesType = "AES_EXCEPTION";
        }
        if (result) {
            var aesTypes="";
            $(".AES_ITNNUMBER").each(function(){
                if($(this).html().trim().trim()!==null && $(this).html().trim().trim()!==""){
                    aesTypes="AES_ITNNUMBER";
                }
            });
            $(".AES_EXCEPTION").each(function(){
                if($(this).html().trim().trim()!==null && $(this).html().trim().trim()!==""){
                    aesTypes="AES_EXCEPTION";
                }
            });
            if (aesTypes === "" || aesTypes === aesType) {
                submitFormAES('addAES');
            }
            else if(aesTypes === "AES_ITNNUMBER") {
                $("#aesItnNumber").css("border-color", "red");
                $.prompt("Please Enter ITN Number only");
            }
            else if (aesTypes === "AES_EXCEPTION") {
                $("#aesException").css("border-color", "red");
                $.prompt("Please Enter Exception only");
            }
            $("#aesItnNumber").val("");
            $("#aesException").val("");
        }
    }
    function submitFormAES(methodName) {
        showProgressBar();
        document.lclBookingForm.methodName.value = methodName;
        document.lclBookingForm.submit();
    }
    function deleteAesITN(txt, id,type) {
        $.prompt(txt, {
            buttons: {
                Yes: 1,
                No: 2
            },
            submit: function(v) {
                if (v === 1) {
                    showProgressBar();
                    $('#methodName').val('deleteLcl3pReference');
                    $('#lcl3pRefId').val(id);
                    $('#thirdPName').val(type);
                    $('#lclBookingForm').submit();
                    $.prompt.close();
                }
                else if (v === 2) {
                    $.prompt.close();
                }
            }
        });
    }
</script>
<head>
</head>
<body onload="setColor()">
    <cong:form name="lclBookingForm" id="lclBookingForm" action="lclBooking.do">
        <cong:hidden name="fileNumberId" value="${lclBookingForm.fileNumberId}"/>
        <cong:hidden name="fileNumber" value="${lclBookingForm.fileNumber}"/>
        <cong:table style="width:100%">
            <cong:tr styleClass="tableHeadingNew" >
                <cong:td width="90%">AES/ITN  For File No:<span class="fileNo">${lclBookingForm.fileNumber}</span></cong:td>
                <cong:td><cong:div styleClass="button-style1" style="float:left"
                                   onclick="saveAesITN();"> Save</cong:div> </cong:td>
            </cong:tr>
            <cong:table width="100%" border="0" cellpadding="3" cellspacing="0" id="records" styleClass="tableBorderNew">
                <br/>
                <cong:tr styleClass="textlabels">
                    <cong:td styleClass="textlabelsBoldleftforlcl" valign="middle" width="7%">ITN Number</cong:td>
                    <cong:td valign="middle" width="30%">
                        <html:textarea property="aesItnNumber" styleClass="textlabelsBoldForTextBox" name="aesItnNumber" styleId="aesItnNumber"
                                       cols="45" rows="1" value="" onkeypress="return checkTextAreaLimit(this, 25)"  style="text-transform: uppercase"/>
                    </cong:td>
                    <cong:td styleClass="textlabelsBoldleftforlcl" valign="middle" width="6%">Exception</cong:td>
                    <cong:td valign="middle" width="25%">
                        <html:textarea property="aesException" onkeypress="return checkTextAreaLimit(this, 50)"  styleClass="textlabelsBoldForTextBox" name="aesException" styleId="aesException"
                                       cols="90" rows="1" value="" style="text-transform: uppercase;" /></cong:td>
                    <cong:td colspan="1"> <img src="${path}/img/icons/display.gif" onclick="goToRemarksLookUp();"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
            <br>
            <cong:tr>
                <cong:td>
                    <cong:table width="100%" border="1" style="border-collapse: collapse; border: 1px solid #dcdcdc" id="aestable">
                    <tr class="tableHeadingNew">
                        <td colspan="3">AES/ITN View Details</td>
                    </tr>
                    <tr class="tableHeading2">
                        <td width="40%">ITN Number</td>
                        <td  width="50%">Exception</td>
                        <td  width="10%">Action</td>
                    </tr>
                    <c:forEach items="${lcl3PList}" var="aes">
                        <c:if test="${aes.type eq 'AES_ITNNUMBER' || aes.type eq 'AES_EXCEPTION'}">
                            <c:choose>
                                <c:when test="${zebra eq 'odd'}">
                                    <c:set var="zebra" value="even"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="zebra" value="odd"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${zebra}" style="text-transform: uppercase">
                                <td style="width:5%" class="AES_ITNNUMBER">
                                    <c:if test="${aes.type eq 'AES_ITNNUMBER'}">
                                        ${aes.reference}</c:if></td>
                                    <td style="width:5%" class="AES_EXCEPTION">
                                    <c:if test="${aes.type eq 'AES_EXCEPTION'}">
                                        ${aes.reference}
                                    </c:if>
                                </td>
                                <td>
                                    <img src="${path}/img/icons/trash.jpg" alt="delete" height="12px" width="12px"
                                         onclick="deleteAesITN('Are you sure you want to delete?', '${aes.id}','${aes.type}');" style="cursor: pointer"/>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </cong:table>
            </cong:td>
        </cong:tr>
        <cong:hidden name="lcl3pRefId" id="lcl3pRefId"/>
        <cong:hidden name="thirdPName" id="thirdPName"/>
        <input type="hidden" name="methodName" id="methodName"/>
    </cong:table>
</cong:form>
</body>