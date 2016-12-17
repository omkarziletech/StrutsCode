<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp"%>
<%@include file="../includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ include file="/jsps/preloader.jsp"%>
<%@include file="/taglib.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
    <html>
        <body style="background:#ffffff" id="batchhotCodesBody">
            <table width="100%" border="0" class="tableBorderNew" align="center" cellspacing="0" cellpadding="0">
                <tr class="tableHeadingNew">
                    <td width="22%">Batch HOT Codes For File No
                        <span class="fileNo" style="margin-right: 30px">${fileNumber}</span>
                </td>
            </tr>
        </table>
        <br/>      
        <table width="70%" border="0">
            <tr>
                <td class="textlabelsBoldforlcl"> HOT Codes &nbsp;&nbsp;</td>
                <td>
                    <cong:autocompletor name="batchHotCode" id="batchHotCode" fields="NULL,NULL,NULL,NULL,genCodefield1" query="CONCAT_HOTCODE_TYPE"  width="300"
                                        shouldMatch="true" styleClass="text" container="NULL" template="concatHTC" scrollHeight="200px" value="" callback="checkValidHotCode('batchHotCode','hotcode')"/>
                    <input type="hidden" id="genCodefield1" name="genCodefield1"/>
                </td>
                <td><input type="button" value="Add" class="button-style3" onclick="saveBatchHotCode('${fileId}');"/></td>   
            </tr>
        </table>  
        <br/>
        <c:if test="${not empty lclHotCodeList}">
            <table border="0" width="100%">     
                <tr>
                    <td style="width:10%"></td>
                    <td>
                        <table class="dataTable">
                            <thead>
                                <tr>
                                    <th width="5%">Hot Codes</th>
                                    <th width="2%">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="hotcodes" items="${lclHotCodeList}">
                                    <c:set var="zebra" value="${zebra eq 'odd' ? 'even' : 'odd'}"/>
                                    <tr class="${zebra}" style="text-transform: uppercase">
                                        <c:if test="${hotcodes ne ''}">
                                            <td width="5%" class="hotcode" style="text-align: center">${hotcodes.code}</td>   
                                            <td width="2%" style="text-align: center">
                                                <img src="${path}/jsps/LCL/images/close1.png" alt="delete" height="12" width="12" 
                                                     onclick="deleteBatchHotCode('${hotcodes.code}');"/>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </td>
                    <td style="width:20%"></td>
                </tr>
            </table>
            <br/>
            <table border="0">
                <tr>
                    <td style="width: 70%"></td>
                    <td>
                        <input type="button" class="button-style3" value="Ok" onclick="submitBatchHotCode();"/>
                        <input type="button" class="button-style3" value="Cancel" onclick="closeBatchHotPopup();"/>
                    </td>
                </tr> 
            </table>
        </c:if>
        <div id="add-hotBatchCodeComments-container" class="static-popup" style="display: none;width: 600px;height: 150px;">
            <table class="table" style="margin: 2px;width: 598px;">
                <tr>
                    <th>
                        <div class="float-left">
                            <label id="headingComments"></label>
                        </div>
                    </th>
                </tr>
                <tr>
                    <td></td>
                </tr>
                <tr>
                    <td class="label">
                        <textarea id="batchhotCodeComments" name="hotCodeComments" cols="85" rows="5" class="textBoldforlcl"
                                  style="resize:none;text-transform: uppercase"></textarea>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <input type="button"  value="Save" id="saveHotCode"
                               align="center" class="button" onclick="saveBatchHotCodeXXX('${fileId}');"/>
                        <input type="button"  value="Cancel" id="cancelHotCode"
                               align="center" class="button" onclick="cancelHotCodeXXXComments();"/>
                    </td>
                </tr>
            </table>
        </div>
        <input type="hidden" id="hotCodeFlagIdNotes" name="hotCodeFlagIdNotes" value="${hotCodeFlagIdNotes}"/>
        <input type="hidden" id="prehotCodeComments" name="prehotCodeComments"/>
        <input type="hidden" id="isPreHotCodeRemarks" name="isPreHotCodeRemarks" value="${isPreHotCodeRemarks}"/>
    </body>
</html>

<script type="text/javascript">
    function closeBatchHotPopup() {
        parent.$.fn.colorbox.close();
    }
    function submitBatchHotCode() {
        showProgressBar();
        var comments = $('#prehotCodeComments').val();
        var isPreHotCodeRemarks = $('#isPreHotCodeRemarks').val();
        var releaseClass = parent.$("#lclReleaseButton1").attr('class') === 'green-background' ? true : false;
        parent.$("#methodName").val('addORemoveBatchHotCode');
        var params = parent.$("#lclBookingForm").serialize();
        params += "&button=saveBatchHotCode" + "&hotCodeXXXComments=" + comments;
        $.post(parent.$("#lclBookingForm").attr("action"), params, function (data) {
        $("#hotCodesList").html(data);    
        $("#hotCodesList", window.parent.document).html(data);
            if ((comments !== '' || isPreHotCodeRemarks!=='')  && !releaseClass) {
                parent.$("#holdButton1").removeClass("button-style1").addClass("red-background");
                parent.$("#holdButton2").removeClass("button-style1").addClass("red-background");
                parent.$("#holdButton1").text("UnHold");
                parent.$("#holdButton2").text("UnHold");
                parent.$("#hold").val("Y");
            }
            if ($('#hotCodeFlagIdNotes').val() === 'true') {
                parent.$('.notes').removeClass('buttonok-style1');
                parent.$('.notes').addClass('green-background');
            } else {
                parent.$('.notes').removeClass('green-background');
                parent.$('.notes').addClass('button-style1');
            }
            hideProgressBar();
            parent.$.fn.colorbox.close();
            
        });
    }
    function deleteBatchHotCode(code) {
        parent.$("#methodName").val('addORemoveBatchHotCode');
        var params = parent.$("#lclBookingForm").serialize();
        params += "&batchHotCode=" + code + "&button=deleteBatchHotCode";
        $.post(parent.$("#lclBookingForm").attr("action"), params, function (data) {

            $("#batchhotCodesBody").html(data);
            $("#batchhotCodesBody", window.parent.document).html(data);
        });
    }

    function checkValidHotCode(id1, id2) {
        $("." + id2).each(function () {
            if ($(this).val() === $("#" + id1).val()) {
                $.prompt("Hot code already exists.");
                $("#batchHotCode").val('');
                return false;
            }
        });
    }

    function saveBatchHotCode(fileNumberId) {
        var hotcodeArray = $('#batchHotCode').val().split("/");
        var moduleName = parent.$('#moduleName').val();
        if ($('#batchHotCode').val() === null || $('#batchHotCode').val() === "") {
            $.prompt('Batch Hot Code Required');
            $("#batchHotCode").css("border-color", "red");
        } else if (hotcodeArray[0] === "XXX" && moduleName === "Exports") {
            showAlternateMask();
            $("#add-hotBatchCodeComments-container").center().show(500, function () {
                $('#headingComments').text('Enter a required explanation for adding the value "XXX" Hot Code');
                $('#batchhotCodeComments').val('');
            });
        } else {
            parent.$("#methodName").val("addORemoveBatchHotCode");
            var params = parent.$("#lclBookingForm").serialize();
            params += "&batchHotCode=" + $("#batchHotCode").val() + "&button=checkBatchHotCode";
            $.post(parent.$("#lclBookingForm").attr("action"), params, function (data) {
                if (data === 'true') {
                    $.prompt("This Batch HS code already Exists");
                } else {
                    var batchHotCode = $("#batchHotCode").val();
                    $("#batchHotCode").val("");
                    parent.$("#methodName").val("addORemoveBatchHotCode");
                    var param = parent.$("#lclBookingForm").serialize();
                    param += "&fileNumber=" + $('#fileNumber').val() + "&batchHotCode=" + batchHotCode + "&button=addBatchHotCodes";
                    $.post(parent.$("#lclBookingForm").attr("action"), param, function (data) {
                        $("#batchhotCodesBody").html(data);
                        $("#batchhotCodesBody", window.parent.document).html(data);
                    });
                }
            });
        }
    }
    function saveBatchHotCodeXXX() {
        var comments = $("#batchhotCodeComments").val();
        parent.$("#methodName").val("addORemoveBatchHotCode");
        var params = parent.$("#lclBookingForm").serialize();
        params += "&batchHotCode=" + $("#batchHotCode").val() + "&button=checkBatchHotCode";
        $.post(parent.$("#lclBookingForm").attr("action"), params, function (data) {
            if (data === 'true') {
                $.prompt("This Batch HOT code already Exists");
            } else {
                var batchHotCode = $("#batchHotCode").val();
                $("#batchHotCode").val("");
                parent.$("#methodName").val("addORemoveBatchHotCode");
                var param = parent.$("#lclBookingForm").serialize();
                param += "&fileNumber=" + $('#fileNumber').val() + "&batchHotCode=" + batchHotCode + "&button=addBatchHotCodes" + "&hotCodeXXXComments=" + comments;
                $.post(parent.$("#lclBookingForm").attr("action"), param, function (data) {
                    $("#batchhotCodesBody").html(data);
                    $("#batchhotCodesBody", window.parent.document).html(data);
                    $("#prehotCodeComments").val(comments);
                });
            }
        });
    }
    function cancelHotCodeXXXComments() {
        $("#add-hotBatchCodeComments-container").center().hide(500, function () {
            $('#batchHotCode').val('');
            hideAlternateMask();
        });
    }
     
</script>