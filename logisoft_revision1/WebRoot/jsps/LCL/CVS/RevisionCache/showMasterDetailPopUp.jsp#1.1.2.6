<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp"%>
<%@include file="/taglib.jsp"%>
<%@include file="colorBox.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
 <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <cong:form  action="/lclAddVoyage" name="lclAddVoyageForm" id="lclAddVoyageForm" >
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" align="center">
                <tr class="tableHeadingNew">
                    <td width="20%">SS Master Detail <span class="red">${unitNo}</span></td>
                </tr>
            </table>
            <br/>
            <table border="0" width="100%">
                <td class="textlabelsBoldforlcl">
                    BL Body  : &nbsp;&nbsp;
                </td>
                <td colspan="5">
                    <cong:textarea cols="2" rows="15" styleClass="refusedTextarea textlabelsBoldForTextBox"  name="blbody"  value="${lclAddVoyageForm.blbody}"/>
                </td>
                <td width="35%"></td>
            </tr>
            <tr>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em;" >Show On Master :</td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> Pieces&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="totalPieces" name="totalPieces"  styleClass="drAndBLValues" 
                                onkeyup="numberValidate(this)" value="${lclAddVoyageForm.totalPieces}" style=" width: 55px;"/> 
                </td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> CBM&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="volumeMetric" name="volumeMetric"  styleClass="drAndBLValues" 
                                onkeyup="numberValidate(this)" value="${lclAddVoyageForm.volumeMetric}" style=" width: 55px;" onchange="calculateVolumeImperial(this)"/> 
                </td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> KGS&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="weightMetric" name="weightMetric"  styleClass="drAndBLValues" 
                                onkeyup="numberValidate(this)" value="${lclAddVoyageForm.weightMetric}" style=" width: 55px;" onchange="calculateWeightImperial(this)"/> 
                    <label class="textlabelsBoldforlcl">&nbsp;&nbsp;&nbsp;Auto Convert&nbsp;&nbsp;&nbsp;</label>
                    <cong:checkbox name="autoConvert" id="autoConvert" styleClass="drAndBLValues" container="NULL"/>                   
                </td>
                
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> CFT&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text id="volumeImperial" name="volumeImperial" styleClass="drAndBLValues" value="${lclAddVoyageForm.volumeImperial}"
                               onkeyup="numberValidate(this)" style=" width: 55px;" onchange="calculateVolumeMetric(this)"/>                    
                </td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> LBS&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text id="weightImperial" name="weightImperial" styleClass="drAndBLValues" value="${lclAddVoyageForm.weightImperial}"
                               onkeyup="numberValidate(this)" style=" width: 55px;" onchange="calculateWeightMetric(this)"/>
                </td>
            </tr>
            <tr>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em;" >Totals from DRs loaded :</td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> Pieces&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="pieceDRTotal" name="pieceDRTotal"  styleClass="textlabelsBoldForTextBoxDisabledLook text"
                                onkeyup="numberValidate(this)" readOnly="true" value="${lclAddVoyageForm.pieceDRTotal}" style=" width: 55px;"/> 
                </td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> CBM&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="CBMDRTotal" name="CBMDRTotal"  styleClass="textlabelsBoldForTextBoxDisabledLook text" 
                                onkeyup="numberValidate(this)" readOnly="true" value="${lclAddVoyageForm.CBMDRTotal}" style=" width: 55px;"/> 
                </td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> KGS&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="KGSDRTotal" name="KGSDRTotal"  styleClass="textlabelsBoldForTextBoxDisabledLook text" 
                                onkeyup="numberValidate(this)"  readOnly="true" value="${lclAddVoyageForm.KGSDRTotal}" style=" width: 55px;"/> 
                    <input type="checkbox" id="totalDr" name="totalDr"  style="vertical-align: middle;" title="Apply these to show on master" onclick="copyTotalDR(this)"/>
                </td>
                <cong:hidden id="CFTDRTotal" name="CFTDRTotal" />
                <cong:hidden id="LBSDRTotal" name="LBSDRTotal" />
            </tr>
            <tr>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em;" >Totals from House BLs :</td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> Pieces&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="pieceBLTotal" name="pieceBLTotal"  styleClass="textlabelsBoldForTextBoxDisabledLook text" 
                                onkeyup="numberValidate(this)"  readOnly="true" value="${lclAddVoyageForm.pieceBLTotal}" style=" width: 55px;"/> 
                </td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> CBM&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="CBMBLTotal" name="CBMBLTotal"  styleClass="textlabelsBoldForTextBoxDisabledLook text" 
                                onkeyup="numberValidate(this)" readOnly="true" value="${lclAddVoyageForm.CBMBLTotal}" style=" width: 55px;"/> 
                </td>
                <td class="textlabelsBoldforlcl" style="padding-top:1.7em; "> KGS&nbsp;&nbsp;&nbsp; </td>
                <td  style="padding-top:1.7em;">
                    <cong:text  id="KGSBLTotal" name="KGSBLTotal"  styleClass="textlabelsBoldForTextBoxDisabledLook text"
                                onkeyup="numberValidate(this)" readOnly="true" value="${lclAddVoyageForm.KGSBLTotal}" style=" width: 55px;"/> 
                    <input type="checkbox" id="totalHouseDr" name="totalHouseDr" onclick="copyHouseDR(this);" style="vertical-align: middle;" title="Apply these to show on master"/>
                </td>
                <cong:hidden id="CFTBLTotal" name="CFTBLTotal" />
                <cong:hidden id="LBSBLTotal" name="LBSBLTotal" />
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td style="padding-top:1.7em;"> 
                    <div class="button-style1" id="save" onclick="saveMaster('${path}', '${lclAddVoyageForm.unitssId}', '${lclAddVoyageForm.masterId}');">Save</div>
                </td>
                <td style="padding-top:1.7em;">
                    <div class="button-style1" id="Cancel" onclick="closePopUp();">Cancel</div>
                </td>
            </tr>
        </table>
        <cong:hidden id ="methodName" name="methodName"/>
    </cong:form>
</body>
<script type="text/javascript">
jQuery(document).ready(function () {
    if ($('#autoConvert').is(":checked")) {          
          if (!isNaN(parseFloat($('#CBMDRTotal').val())* 35.3146)) {
               $('#CFTDRTotal').val((parseFloat($('#CBMDRTotal').val()) * 35.314 ).toFixed(3));
            }
          if (!isNaN(parseFloat($('#KGSDRTotal').val()) * 2.2046)) {
               $('#LBSDRTotal').val((parseFloat($('#KGSDRTotal').val())  * 2.2046 ).toFixed(3));
            }  
           if (!isNaN(parseFloat($('#CBMBLTotal').val())* 35.3146)) {
               $('#CFTBLTotal').val((parseFloat($('#CBMBLTotal').val()) * 35.314 ).toFixed(3));
            }
          if (!isNaN(parseFloat($('#KGSBLTotal').val()) * 2.2046)) {
               $('#LBSBLTotal').val((parseFloat($('#KGSBLTotal').val())  * 2.2046 ).toFixed(3));
            }   
            
        }
        
        });
   function  calculateVolumeMetric(obj) {
      //  alert(" CftToCbm ");
        if ($('#autoConvert').is(":checked")) {
            if (!isNaN(parseFloat($('#volumeImperial').val())/ 35.3146  )) {
                $('#volumeMetric').val((parseFloat($('#volumeImperial').val())  / 35.314 ).toFixed(3));
            }
        }
    }
    function  calculateVolumeImperial(obj) {
       // alert(" CbmToCft ");
        if ($('#autoConvert').is(":checked")) {          
          if (!isNaN(parseFloat($('#volumeMetric').val())* 35.3146)) {
               $('#volumeImperial').val((parseFloat($('#volumeMetric').val()) * 35.314 ).toFixed(3));
            }
        }
    }
   
    function calculateWeightImperial(obj) {
      //  alert(" kgsToLbs ");
        if ($('#autoConvert').is(":checked")) {
          if (!isNaN(parseFloat($("#weightMetric").val()) * 2.2046)) {
                $('#weightImperial').val((parseFloat($("#weightMetric").val()) * 2.2046).toFixed(3));
          }
        }
    }
    function calculateWeightMetric(obj) {
     //   alert(" lbsToKgs "); //working fine 
        if ($('#autoConvert').is(":checked")) {
            if (!isNaN(parseFloat($('#weightImperial').val()) / 2.2046)) {
                $('#weightMetric').val((parseFloat($('#weightImperial').val()) / 2.2046).toFixed(3));
            }
        }
    }
   


    function copyTotalDR(ele) {
        if ($('#' + ele.id).is(':checked')) {
            $("#totalPieces").val($("#pieceDRTotal").val());
            $("#volumeMetric").val($("#CBMDRTotal").val());
            $("#weightMetric").val($("#KGSDRTotal").val());
            $("#volumeImperial").val($("#CFTDRTotal").val());
            $("#weightImperial").val($("#LBSDRTotal").val());
            $("#totalHouseDr").attr("checked", false);
        } else {
            $(".drAndBLValues").val("");
        }
    }

    function copyHouseDR(ele) {
        if ($('#' + ele.id).is(':checked')) {
            $("#totalPieces").val($("#pieceBLTotal").val());
            $("#volumeMetric").val($("#CBMBLTotal").val());
            $("#weightMetric").val($("#KGSBLTotal").val());
            $("#volumeImperial").val($("#CFTBLTotal").val());
            $("#weightImperial").val($("#LBSBLTotal").val());
            $("#totalDr").attr("checked", false);
        } else {
            $(".drAndBLValues").val("");
        }
    }

    function removeVolImp() {
        if ($("#volumeMetric").val() === "") {
            $("#volumeImperial").val('');
        }
    }

    function removeWtImp() {
        if ($("#weightMetric").val() === "") {
            $("#weightImperial").val('');
        }
    }

    function numberValidate(obj) {
        if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
            obj.value = "";
            $.prompt("This field should be Numeric");
        }
    }
    function closePopUp() {
        parent.$.fn.colorbox.close();
    }

    function saveMaster(path, unitSsId, masterId) {
        //alert("Save Master");
        $("#methodName").val("showSsMasterDetailPopUp");
        var params = $("#lclAddVoyageForm").serialize();
        params += "&unitssId=" + unitSsId + "&buttonValue=saveMaster";
        $.post($("#lclAddVoyageForm").attr("action"), params, function (data) {
            closePopUp();
            var href = path + "/lclAddVoyage.do?methodName=editSSMasterDetail&masterId="
                    + masterId + "&finalDestinationId=" + parent.parent.$("#finalDestinationId").val();
            parent.document.location.href = href;
        });
    }
</script>
</html>