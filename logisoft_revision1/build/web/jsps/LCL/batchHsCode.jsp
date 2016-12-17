
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@include file="/jsps/preloader.jsp" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <html>
        <body style="background:#ffffff" id="batchHsCodeBody">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="22%">Batch HS Codes for File No:
                        <span class="fileNo" style="margin-right: 30px">${fileNumber}</span></td>
            </tr>
        </table>
        <br/>
        <table style="width:70%;"  border="0">
            <tr>
                <td class="textlabelsBoldforlcl" width="40%">HS Code#&nbsp;&nbsp;&nbsp;</td>
                <td><input type="text" class="text" name="batchHsCode" id="batchHsCode" /></td>
                <td><input type="button" class="button-style1" value="Add" 
                           onclick="saveBatchHsCode('${fileId}');"/></td>
            </tr>
        </table>
        <br/>
        <c:if test="${not empty lclBookingHsCodes}">
            <table boder="2" width="100%">
                <tr>
                    <td style="width:10%"></td>
                    <td>
                        <table class="dataTable">
                            <thead>
                                <tr>
                                    <th width="5%">Hs Code</th>
                                    <th width="2%">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="bookingHsCode" items="${lclBookingHsCodes}" >
                                    <c:set var="zebra" value="${zebra eq 'odd' ? 'even' : 'odd'}"/>
                                    <tr class="${zebra}" style="text-transform: uppercase">
                                        <td width="5%" class="hsCode" style="text-align: center;">${bookingHsCode.codes}</td>
                                        <td width="2" style="text-align: center;">
                                            <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12" 
                                                 onclick="deleteBatchHsCode('${bookingHsCode.codes}');"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </td>
                    <td style="width:20%"></td>
                </tr>
            </table>
            <br/>
            <table  border="0">
                <tr>
                    <td style="width:70%"></td>
                    <td>
                        <input type="button" class="button-style3" value="Ok" onclick="submitBatchHsCode();"/>
                        <input type="button" class="button-style3" value="Cancel" onclick="closeBatchHsPopup();"/>
                    </td>
                <tr>
            </table>
        </c:if>
    </body>
</html>
<script type="text/javascript">
    function saveBatchHsCode(fileNumberId) {
        var regexp = /^\d{4}\.\d{2}$/i;
        var regexp1 = /^\d{4}\.\d{2}\.\d{4}$/i;
        if ($('#batchHsCode').val() === null || $('#batchHsCode').val() === "") {
            $.prompt('Batch HS Code Required');
            $("#batchHsCode").css("border-color", "red");
        } else if (!regexp.exec($('#batchHsCode').val()) && !regexp1.exec($('#batchHsCode').val())) {
            $.prompt('HS Code should be in format NNNN.NN or NNNN.NN.NNNN');
            $("#hsCode").css("border-color", "red");
        } else {
            parent.$("#methodName").val("addOrRemoveBatchCode");
            var params = parent.$("#lclBookingForm").serialize();
            params += "&batchHsCode=" + $("#batchHsCode").val() + "&button=checkBatchHsCode";
            $.post(parent.$("#lclBookingForm").attr("action"), params, function (data) {
                if (data === 'true') {
                    $.prompt("This Batch HS code already Exists");
                } else {
                    var batchHsCode = $("#batchHsCode").val();
                    $("#batchHsCode").val("");
                    parent.$("#methodName").val("addOrRemoveBatchCode");
                    var param = parent.$("#lclBookingForm").serialize();
                    param += "&fileNumber=" + $('#fileNumber').val() + "&batchHsCode=" + batchHsCode + "&button=addBatchHsCode";
                    $.post(parent.$("#lclBookingForm").attr("action"), param, function (data) {
                        $("#batchHsCodeBody").html(data);
                        $("#batchHsCodeBody", window.parent.document).html(data);
                    });
                }
            });
        }
    }

    function deleteBatchHsCode(batchHsCode) {
        parent.$("#methodName").val("addOrRemoveBatchCode");
        var param = parent.$("#lclBookingForm").serialize();
        param += "&batchHsCode=" + batchHsCode + "&button=deleteBatchHsCode";
        $.post(parent.$("#lclBookingForm").attr("action"), param, function (data) {
            $("#batchHsCodeBody").html(data);
            $("#batchHsCodeBody", window.parent.document).html(data);
        });
    }

    function submitBatchHsCode() {
        parent.$("#methodName").val("addOrRemoveBatchCode");
        var param = parent.$("#lclBookingForm").serialize();
        param += "&button=saveBatchHsCode";
        $.post(parent.$("#lclBookingForm").attr("action"), param, function (data) {
            $("#hsCodeList").html(data);
            $("#hsCodeList", window.parent.document).html(data);
            parent.$.fn.colorbox.close();
        });
    }
    function closeBatchHsPopup() {
        parent.$.fn.colorbox.close();
    }
</script>