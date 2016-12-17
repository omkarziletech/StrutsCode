<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.gp.cong.common.DateUtils"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*,java.text.NumberFormat,java.text.DecimalFormat,com.gp.cong.logisoft.dwr.DwrUtil"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<%    List<FclBlMarks> fclBlMarksList = null;
    String index = "", markNos = "", descPkg = "", noOfPkg = "", lbs = "0.000", kgs = "0.000", cbm = "0.000", indexValue = null;
    String cft = "0.000", view = "", netLbs = "0.000", netKgs = "0.000", uom = "", descForMasterBl = "", useHouseDesc = "";
    String tareWeightKgs = "0.000", tareWeightLbs = "0.000", bottomLineVgmWeightKgs = "0.000", bottomLineVgmWeightLbs = "0.000", verificationSignature = null, verificationDate = null;
    if (session.getAttribute("view") != null) {
        view = (String) session.getAttribute("view");
    }
    NumberFormat numberFormat = new DecimalFormat("###,###,##0.000");
    if (request.getParameter("index1") != null) {
        index = request.getParameter("index1");
        session.setAttribute("index", index);
    }
    String bol = "";
    String marksid = "";
    String containerId = "";
    if (request.getAttribute("bol") != null) {
        bol = (String) request.getAttribute("bol");
    }
    if (request.getAttribute("containerId") != null) {
        containerId = (String) request.getAttribute("containerId");
    }
    if (request.getAttribute("containerMarks") != null) {
        markNos = (String) request.getAttribute("containerMarks");
    }
    if (request.getAttribute("index1") != null) {
        indexValue = (String) request.getAttribute("index1");
    }
    FclBlMarks fclBlMarks = new FclBlMarks();
    if (request.getAttribute("fclBlMarks") != null) {
        fclBlMarks = (FclBlMarks) request.getAttribute("fclBlMarks");
        if (fclBlMarks.getId() != null) {
            marksid = fclBlMarks.getId().toString();
        }
        if (fclBlMarks.getDescForMasterBl() != null) {
            descForMasterBl = fclBlMarks.getDescForMasterBl();
        }
        if (fclBlMarks.getDescPckgs() != null) {
            descPkg = fclBlMarks.getDescPckgs();
        }
        if (fclBlMarks.getUom() != null) {
            uom = fclBlMarks.getUom().toString();
        }
        if (fclBlMarks.getNoOfPkgs() != null) {
            noOfPkg = fclBlMarks.getNoOfPkgs().toString();
        }
        if (fclBlMarks.getNetweightKgs() != null) {
            netKgs = numberFormat.format(fclBlMarks.getNetweightKgs());
        }
        if (fclBlMarks.getNetweightLbs() != null) {
            netLbs = numberFormat.format(fclBlMarks.getNetweightLbs());
        }
        if (fclBlMarks.getMeasureCbm() != null) {
            cbm = numberFormat.format(fclBlMarks.getMeasureCbm());
        }
        if (fclBlMarks.getMeasureCft() != null) {
            cft = numberFormat.format(fclBlMarks.getMeasureCft());
        }
        if (fclBlMarks.getCopyDescription() != null) {
            useHouseDesc = fclBlMarks.getCopyDescription();
        }
        if (fclBlMarks.getNetweightLbs() != null) {
            lbs = numberFormat.format(fclBlMarks.getNetweightLbs());
        }
        if (fclBlMarks.getNetweightKgs() != null) {
            kgs = numberFormat.format(fclBlMarks.getNetweightKgs());
        }
        if (fclBlMarks.getTareWeightKgs() != null) {
            tareWeightKgs = numberFormat.format(fclBlMarks.getTareWeightKgs());
        }
        if (fclBlMarks.getTareWeightLbs() != null) {
            tareWeightLbs = numberFormat.format(fclBlMarks.getTareWeightLbs());
        }
        if (fclBlMarks.getBottomLineVgmWeightKgs() != null) {
            bottomLineVgmWeightKgs = numberFormat.format(fclBlMarks.getBottomLineVgmWeightKgs());
        }
        if (fclBlMarks.getBottomLineVgmWeightLbs() != null) {
            bottomLineVgmWeightLbs = numberFormat.format(fclBlMarks.getBottomLineVgmWeightLbs());
        }
        if (fclBlMarks.getVerificationSignature() != null) {
            verificationSignature = fclBlMarks.getVerificationSignature();
        }
        if (fclBlMarks.getVerificationDate() != null) {
            verificationDate = DateUtils.parseDateToDateTimeString(fclBlMarks.getVerificationDate());
        }
    }

//if(request.getAttribute("closemarks")!=null){
//%>    
<script>
//    parent.parent.submit1();
//		parent.parent.GB_hide();
</script>
<%//} 
    if (request.getAttribute("fclBlMarksList") != null) {
        fclBlMarksList = (List) request.getAttribute("fclBlMarksList");
    }
%>
<div id="ConfirmYesOrNo" class="alert">
    <p class="alertHeader"><b>Confirmation</b></p>
    <p id="innerText2" class="containerForAlert"></p>
    <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
        <input type="button"  class="buttonStyleForAlert" value="OK"
               onclick="confirmYes()">
        <input type="button"  class="buttonStyleForAlert" value="Cancel"
               onclick="confirmNo()">
    </form>
</div>
<html> 
    <head>
        <title>JSP for FclmarksnumberForm form</title>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.util.js"></script>
        <script language="javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
        <script language="javascript" type="text/javascript">
                   function submit1() {
                       document.fclmarksnumberForm.buttonValue.value = "save";
                       document.fclmarksnumberForm.submit();
                   }
                   function getlbskgs() {
                       document.fclmarksnumberForm.buttonValue.value = "lbskgs";
                       document.fclmarksnumberForm.submit();
                   }
                   function getcbmcft() {
                       document.fclmarksnumberForm.buttonValue.value = "cbmcft";
                       document.fclmarksnumberForm.submit();
                   }
                   function makeFormBorderless(form) {
                       var element;
                       for (var i = 0; i < form.elements.length; i++) {
                           element = form.elements[i];
                           if (element.type == "button") {
                               if (element.value == "Add New" || element.value == "Save" || element.value == "Delete") {
                                   element.style.visibility = "hidden";
                               }
                           }
                       }
                       return false;
                   }
                   function addPackages() {
                       jQuery("#descpkgs").val(jQuery("#descpkgs").val().replace(/\t/g, ' '));
                       jQuery("#descForMasterBl").val(jQuery("#descForMasterBl").val().replace(/\t/g, ' '));

                       var errorsForHouse = jQuery.validateXML(jQuery("#descpkgs").val());
                       var errorsForMaster = jQuery.validateXML(jQuery("#descForMasterBl").val());
                       if (jQuery.trim(errorsForHouse) !== "") {
                           alertNew("Please Remove Following Invalid Characters\nin Description of Packages for House BL : \n" + errorsForHouse);
                           return false;
                       }
                       if (jQuery.trim(errorsForMaster) !== "") {
                           alertNew("Please Remove Following Invalid Characters\nin Description of Packages for Master BL : \n" + errorsForMaster);
                           return false;
                       }
                       document.fclmarksnumberForm.buttonValue.value = "add";
                       document.getElementById("savePackages").disabled = true;
                       document.fclmarksnumberForm.submit();
                   }
                   function updatePackages(val) {
                       jQuery("#descpkgs").val(jQuery("#descpkgs").val().replace(/\t/g, ' '));
                       jQuery("#descForMasterBl").val(jQuery("#descForMasterBl").val().replace(/\t/g, ' '));

                       var errorsForHouse = jQuery.validateXML(jQuery("#descpkgs").val());
                       var errorsForMaster = jQuery.validateXML(jQuery("#descForMasterBl").val());
                       if (jQuery.trim(errorsForHouse) !== "") {
                           alertNew("Please Remove Following Invalid Characters\nin Description of Packages for House BL : \n" + errorsForHouse);
                           return false;
                       }
                       if (jQuery.trim(errorsForMaster) !== "") {
                           alertNew("Please Remove Following Invalid Characters\nin Description of Packages for Master BL : \n" + errorsForMaster);
                           return false;
                       }

                       document.fclmarksnumberForm.index.value = val;
                       document.getElementById("updatePackageValues").disabled = true;
                       document.fclmarksnumberForm.buttonValue.value = "update";
                       document.fclmarksnumberForm.submit();
                   }
                   function copyDesc() {
                       if (document.fclmarksnumberForm.copyDescription.checked) {
                           document.fclmarksnumberForm.descForMasterBl.value = document.fclmarksnumberForm.descpkgs.value;
                       } else {
                           document.fclmarksnumberForm.descForMasterBl.value = "";
                       }
                   }
                   function marksdelete(val) {
                       document.fclmarksnumberForm.index.value = val;
                       document.fclmarksnumberForm.buttonValue.value = "delete";
                       confirmYesOrNo("Are you sure you want to delete this Marks/Descriptions", "deletemarks");
                       return;
                   }
                   function marksEdit(val) {
                       document.fclmarksnumberForm.index.value = val;
                       document.fclmarksnumberForm.buttonValue.value = "edit";
                       document.fclmarksnumberForm.submit();
                   }
                   function RestrictInt(val) {
                       if (document.fclmarksnumberForm.weightLbs.value == '0.000' || document.fclmarksnumberForm.weightLbs.value == "") {
                           document.fclmarksnumberForm.weightKgs.readOnly = false;
                       }
                       if (document.fclmarksnumberForm.measureCft.value == '0.000' || document.fclmarksnumberForm.measureCft.value == "") {
                           document.fclmarksnumberForm.measureCbm.readOnly = false;
                       }
                       if (document.fclmarksnumberForm.weightKgs.value == '0.000' || document.fclmarksnumberForm.weightKgs.value == "") {
                           document.fclmarksnumberForm.weightLbs.readOnly = false;
                       }
                       if (document.fclmarksnumberForm.measureCbm.value == '0.000' || document.fclmarksnumberForm.measureCbm.value == "") {
                           document.fclmarksnumberForm.measureCft.readOnly = false;
                       }
                       //if(isNaN(val)){
                       //val = val.substring(0, val.length-1);
                       //document.fclmarksnumberForm.weightLbs.value = val;
                       //return false;
                       // }
                       return true;
                   }
                   function display() {
                       if (document.getElementById("addDescription") != null) {
                           document.getElementById("addDescription").style.display = "block";
                           document.getElementById("noOfpkgs").focus();
                       }
                       //document.getElementById("save").style.visibility="hidden";
                   }
                   function closePage() {
                       //document.fclmarksnumberForm.buttonValue.value="closeThePage";
                       //document.fclmarksnumberForm.submit();
                       var table = document.getElementById("divtablesty1");
                       var tableLength = table.getElementsByTagName("tr");
                       var color;
                       if (tableLength.length > 1) {
                           color = "red";
                           parent.parent.makeButtonRedColorForPkgs(color, '<%=indexValue%>', 'PKG');
                       } else {
                           parent.parent.makeButtonRedColorForPkgs(color, '<%=indexValue%>', 'PKG');
                       }
                       parent.parent.GB_hide();
                   }
                   function watchTextarea(id) {
                       document.getElementById(id).onkeyup()
                   }
                   function onPkgBlur() {
                       document.getElementById("uom").value = "";
                       document.getElementById("weightKgs").focus();
                   }
                   function changeFocus(nextEle) {
                       document.getElementById(nextEle).focus();
                   }
                   function limitTextarea(textarea, maxLines, maxChar) {
                       var lines = textarea.value.replace(/\r/g, '').split('\n'),
                               lines_removed,
                               char_removed,
                               i;
                       if (maxLines && lines.length > maxLines) {
                           alertNew('You can not enter\nmore than ' + maxLines + ' lines');
                           lines = lines.slice(0, maxLines);
                           lines_removed = 1
                       }
                       if (maxChar) {
                           i = lines.length;
                           while (i-- > 0)
                               if (lines[i].length > maxChar) {
                                   lines[i] = lines[i].slice(0, maxChar);
                                   char_removed = 1;
                               }
                           if (char_removed)
                               alertNew('You can not enter more\nthan ' + maxChar + ' characters per line');
                       }
                       if (char_removed || lines_removed)
                           textarea.value = lines.join('\n');
                   }
                   function allowOnlyWholeNumbers(obj) {
                       var result;
                       if (!/^[1-9]\d*$/.test(obj.value)) {
                           //alertNew("This field accepts numbers only");
                           result = obj.value.replace(/[^0-9]+/g, '');
                           document.fclmarksnumberForm.noOfpkgs.value = result;
                           return false;
                       }
                       return true;
                   }
                   function convertion() {
                       //--LBS----
                       if (document.fclmarksnumberForm.weightLbs.value != "" && document.fclmarksnumberForm.weightLbs.value != '0.000') {
                           if (document.fclmarksnumberForm.weightLbs.value != '0.000') {
                               //document.fclmarksnumberForm.weightKgs.readOnly=true;
                           }
                           var lbsValue = document.fclmarksnumberForm.weightLbs.value;
                           var convertKG = lbsValue * 0.45359237;
                           //document.fclmarksnumberForm.weightKgs.value=Math.round(parseFloat(convertKG)*100 )/100;
                           document.fclmarksnumberForm.weightKgs.value = convertKG.toFixed(3);
                       }
                       //---CFT---
                       if (document.fclmarksnumberForm.measureCft.value != "" && document.fclmarksnumberForm.measureCft.value != '0.000') {
                           if (document.fclmarksnumberForm.measureCft.value != '0.000') {
                               // document.fclmarksnumberForm.measureCbm.readOnly=true;
                           }
                           var cftValue = document.fclmarksnumberForm.measureCft.value;
                           var convertCBM = cftValue * 0.028316847;
                           document.fclmarksnumberForm.measureCbm.value = convertCBM.toFixed(3);
                       }
                       //---KGS---
                       if (document.fclmarksnumberForm.weightKgs.value != "" && document.fclmarksnumberForm.weightKgs.value != '0.000') {
                           if (document.fclmarksnumberForm.weightKgs.value != '0.000') {
                               //document.fclmarksnumberForm.weightLbs.readOnly=true;
                           }
                           if (!document.fclmarksnumberForm.weightKgs.readOnly) {
                               var kgValue = document.fclmarksnumberForm.weightKgs.value;
                               var convertLBS = kgValue * 2.20462262185;
                               //document.fclmarksnumberForm.weightLbs.value=Math.round(parseFloat(convertLBS)*100 )/100;
                               document.fclmarksnumberForm.weightLbs.value = convertLBS.toFixed(3);
                           }
                       }
                       //---CBM---
                       if (document.fclmarksnumberForm.measureCbm.value != "" && document.fclmarksnumberForm.measureCbm.value != '0.000') {
                           if (document.fclmarksnumberForm.measureCbm.value != '0.000') {
                               //document.fclmarksnumberForm.measureCft.readOnly=true;
                           }
                           if (!document.fclmarksnumberForm.measureCbm.readOnly) {
                               var cbmValue = document.fclmarksnumberForm.measureCbm.value;
                               var convertCFT = cbmValue / 0.028316847;
                               document.fclmarksnumberForm.measureCft.value = convertCFT.toFixed(3);
                           }
                       }
                   }
                   function getWeightLbs() {
                       if (document.getElementById("convertionCheck").checked) {
                           var lbsValueDup = document.fclmarksnumberForm.weightLbs.value;
                           var lbsValue = lbsValueDup.replace(/,/g, '');
                           var convertKG = lbsValue * 0.45359237;
                           document.fclmarksnumberForm.weightKgs.value = convertKG.toFixed(3);
                       }
                   }
                   function getTareWeightLbs() {
                       if (document.getElementById("convertionCheck").checked) {
                           var lbsValueDup = document.fclmarksnumberForm.tareWeightLbs.value;
                           var lbsValue = lbsValueDup.replace(/,/g, '');
                           var convertKG = lbsValue * 0.45359237;
                           document.fclmarksnumberForm.tareWeightKgs.value = convertKG.toFixed(3);
                       }
                   }
                   function getBottomLineVgmWeightLbs() {
                       if (document.getElementById("convertionCheck").checked) {
                           var lbsValueDup = document.fclmarksnumberForm.bottomLineVgmWeightLbs.value;
                           var lbsValue = lbsValueDup.replace(/,/g, '');
                           var convertKG = lbsValue * 0.45359237;
                           document.fclmarksnumberForm.bottomLineVgmWeightKgs.value = convertKG.toFixed(3);
                       }
                   }
                   function getMeasureCft() {
                       if (document.getElementById("convertionCheck").checked) {
                           var cftValueDup = document.fclmarksnumberForm.measureCft.value;
                           var cftValue = cftValueDup.replace(/,/g, '');
                           var convertCBM = cftValue * 0.02832; // 35.314;  //0.028316847(old value)
                           document.fclmarksnumberForm.measureCbm.value = convertCBM.toFixed(3);
                       }
                   }
                   function getWeightKgs() {
                       if (document.getElementById("convertionCheck").checked) {
                           var kgValueDup = document.fclmarksnumberForm.weightKgs.value;
                           var kgValue = kgValueDup.replace(/,/g, '');
                           var convertLBS = kgValue * 2.20462262185;
                           document.fclmarksnumberForm.weightLbs.value = convertLBS.toFixed(3);
                       }
                   }
                   function getTareWeightKgs() {
                       if (document.getElementById("convertionCheck").checked) {
                           var kgValueDup = document.fclmarksnumberForm.tareWeightKgs.value;
                           var kgValue = kgValueDup.replace(/,/g, '');
                           var convertLBS = kgValue * 2.20462262185;
                           document.fclmarksnumberForm.tareWeightLbs.value = convertLBS.toFixed(3);
                       }
                   }
                   function getBottomLineVgmWeightKgs() {
                       if (document.getElementById("convertionCheck").checked) {
                           var kgValueDup = document.fclmarksnumberForm.bottomLineVgmWeightKgs.value;
                           var kgValue = kgValueDup.replace(/,/g, '');
                           var convertLBS = kgValue * 2.20462262185;
                           document.fclmarksnumberForm.bottomLineVgmWeightLbs.value = convertLBS.toFixed(3);
                       }
                   }
                   function getMeasureCbm() {
                       if (document.getElementById("convertionCheck").checked) {
                           var cbmValueDup = document.fclmarksnumberForm.measureCbm.value;
                           var cbmValue = cbmValueDup.replace(/,/g, '');
                           var convertCFT = cbmValue * 35.314; //0.028316847 (old value)
                           document.fclmarksnumberForm.measureCft.value = convertCFT.toFixed(3);
                       }
                   }
                   function addWeight() {
                       var tareWeight = "0.000", grossWeight = "0.000", totalWeightKgs = "0.000";
                       var tareWeightLbs = "0.000", grossWeightLbs = "0.000", totalWeightLbs = "0.000";
                       if (document.fclmarksnumberForm.tareWeightKgs.value == "") {
                           tareWeight = "0.000";
                       } else {
                           tareWeight = document.fclmarksnumberForm.tareWeightKgs.value;
                       }
                       if (document.fclmarksnumberForm.weightKgs.value == "") {
                           grossWeight = "0.000";
                       } else {
                           grossWeight = document.fclmarksnumberForm.weightKgs.value;
                       }
                       if (document.fclmarksnumberForm.tareWeightLbs.value == "") {
                           tareWeightLbs = "0.000";
                       } else {
                           tareWeightLbs = document.fclmarksnumberForm.tareWeightLbs.value;
                       }
                       if (document.fclmarksnumberForm.weightLbs.value == "") {
                           grossWeightLbs = "0.000";
                       } else {
                           grossWeightLbs = document.fclmarksnumberForm.weightLbs.value;
                       }
                       tareWeight = tareWeight.replace(/,/g, '');
                       tareWeight = parseFloat(tareWeight);

                       grossWeight = grossWeight.replace(/,/g, '');
                       grossWeight = parseFloat(grossWeight);

                       totalWeightKgs = tareWeight + grossWeight;

                       tareWeightLbs = tareWeightLbs.replace(/,/g, '');
                       tareWeightLbs = parseFloat(tareWeightLbs);

                       grossWeightLbs = grossWeightLbs.replace(/,/g, '');
                       grossWeightLbs = parseFloat(grossWeightLbs);

                       document.fclmarksnumberForm.bottomLineVgmWeightKgs.value = totalWeightKgs.toFixed(3);
                       totalWeightLbs = tareWeightLbs + grossWeightLbs;
                       document.fclmarksnumberForm.bottomLineVgmWeightLbs.value = totalWeightLbs.toFixed(3);
                       jQuery("#calculator").hide();
                   }
                   function insertHouseBlDescription() {
                       if (document.fclmarksnumberForm.descForMasterBl.value != "" && document.fclmarksnumberForm.copyDescription.checked) {
                           confirmNew("This action will remove the Master Description", "masterDescription");
                       }
                       //if(document.fclmarksnumberForm.copyDescription.checked){
                       // document.fclmarksnumberForm.descForMasterBl.value=document.fclmarksnumberForm.descpkgs.value;
                       // document.fclmarksnumberForm.descForMasterBl.readOnly=true;
                       //}
                   }
                   function confirmMessageFunction(id1, id2) {
                       if (id1 == 'masterDescription' && id2 == 'ok') {
                           document.fclmarksnumberForm.descForMasterBl.value = document.fclmarksnumberForm.descpkgs.value;
                       } else if (id1 == 'masterDescription' && id2 == 'cancel') {
                           document.fclmarksnumberForm.copyDescription.checked = false;
                       }
                   }
                   function checkForNumberAndDecimal(obj) {
                       var result;
                       if (!/^\d*(\.\d{0,6})?$/.test(obj.value)) {
                           result = obj.value.replace(/[^0-9.]+/g, '');
                           obj.value = result;
                       }
                       if (obj.value.indexOf(".") >= 0) {
                           var num = obj.value.split(".");
                           if (num.length > 2) {
                               obj.value = obj.value.substring(0, obj.value.length - 1);
                           } else if (num.length == 2) {
                               if (num[1].length > 3) {
                                   obj.value = Number(obj.value.substring(0, obj.value.length - 1)).toFixed(3);
                               }
                           }
                       } else if (obj.value.length > 8) {
                           obj.value = Number(obj.value.substring(0, 8));
                       }

                   }
                   function getSearch() {
                       closeRemarks();
                   }
                   function closeRemarks() {
                       closePopUp();
                       document.body.removeChild(document.getElementById("attachListDiv"));
                   }
                   function addRemarks() {
                       closePopUp();
                       document.body.removeChild(document.getElementById("attachListDiv"));
                       jQuery.ajaxx({
                           data: {
                               className: "com.gp.cong.logisoft.dwr.DwrUtil",
                               methodName: "addRemarks",
                               forward: "/jsps/fclQuotes/AddImportRemarks.jsp",
                               param1: "",
                               request: true
                           },
                           success: function(data) {
                               if (data) {
                                   showPopUp();
                                   var attachListDiv = createHTMLElement("div", "attachListDiv", "400px", "30%", document.body);
                                   jQuery("#attachListDiv").html(data);
                                   floatDiv("attachListDiv", document.body.offsetWidth / 4, document.body.offsetHeight / 4).floatIt();
                               }
                           }
                       });
                   }
                   function closeAddRemarks() {
                       var remarkVal = "";
                       closePopUp();
                       document.body.removeChild(document.getElementById("attachListDiv"));
                       goToRemarksLookUp(remarkVal, "1");
                   }
                   function saveRemarks() {
                       var remarks = document.getElementById("addRemarks").value;
                       document.body.removeChild(document.getElementById("attachListDiv"));
                       jQuery.ajaxx({
                           data: {
                               className: "com.gp.cong.logisoft.dwr.DwrUtil",
                               methodName: "saveImportRemarks",
                               param1: remarks
                           },
                           success: function(data) {
                               if (null != data) {
                                   goToRemarksLookUp("", "1");
                               }
                           }
                       });
                   }
                   function submitRemarks() {
                       var remarksSize = document.getElementById("remarksSize").value;
                       var remarks = "";
                       if (document.getElementById("descpkgs").value != '') {
                           remarks = document.getElementById("descpkgs").value;
                       }
                       for (var i = 0; i < remarksSize; i++) {
                           if (document.getElementById('remarksCheck' + i)) {
                               var isChecked = document.getElementById('remarksCheck' + i).checked;
                               if (isChecked) {
                                   if (remarks != '') {
                                       remarks += "\n";
                                   }
                                   remarks += document.getElementById('preRemark' + i).value;
                               }
                           }
                       }
                       if (remarks != '') {
                           document.getElementById("descpkgs").value = remarks;
                           closePopUp();
                           document.body.removeChild(document.getElementById("attachListDiv"));
                       } else {
                           alert("Please select remarks");
                       }
                   }
                   function searchRemarks() {
                       var remarkVal = document.getElementById("remarks").value;
                       closePopUp();
                       document.body.removeChild(document.getElementById("attachListDiv"));
                       goToRemarksLookUp(remarkVal, "1");
                   }
                   function getGoRemarks() {
                       var checkedValues = "";
                       for (var i = 0; i < document.getElementById("rcheck").length; i++) {
                           if (document.getElementById("rcheck" + i).checked)
                               checkedValues += i + ","
                       }
                   }
                   function goToRemarksLookUp(val, pageNo, importFlag) {
                       jQuery.ajaxx({
                           data: {
                               className: "com.gp.cong.logisoft.dwr.DwrUtil",
                               methodName: "getRemarksLookUp",
                               forward: "/jsps/fclQuotes/RemarksLookUpforPakages.jsp",
                               param1: val,
                               param2: pageNo,
                               param3: importFlag,
                               request: true
                           },
                           success: function(data) {
                               if (data) {
                                   showPopUp();
                                   var attachListDiv = createHTMLElement("div", "attachListDiv", "700px", "350px", document.body);
                                   jQuery("#attachListDiv").html(data);
                               }
                           }
                       });
                   }
                   function gotoRemarksPage(page) {
                       closePopUp();
                       document.body.removeChild(document.getElementById("attachListDiv"));
                       goToRemarksLookUp("", page);
                   }
                   function confirmMessageFunction(id1, id2) {
                       if (id1 == 'deletemarks' && id2 == 'yes') {
                           document.fclmarksnumberForm.submit();
                       } else if (id1 == 'deleteInvoice' && id2 == 'no') {

                       }
                   }
                   function DisplayConfirm(id, left, top, text, point) {
                       document.getElementById(id).style.left = left + "px";
                       document.getElementById(id).style.top = top + "px";
                       document.getElementById("innerText1").innerHTML = text;
                       document.getElementById(id).style.display = "block";
                       document.getElementById(id).style.left = point.x - 100 + "px";
                       document.getElementById(id).style.top = point.y + "px";
                       document.getElementById(id).style.zIndex = "1000";
                       grayOut(true, "");
                   }
                   function confirmNew(text, jam) {
                       returnValue = jam;
                       DisplayConfirm("ConfirmBox", 100, 50, text, window.center({width: 100, height: 100}));
                   }
                   function yes() {
                       document.getElementById("ConfirmBox").style.display = "none";
                       //document.getElementById('cover').style.display='none';
                       grayOut(false, "");
                       confirmMessageFunction(returnValue, "ok");
                   }
                   function No() {
                       document.getElementById("ConfirmBox").style.display = "none";
                       //document.getElementById('cover').style.display='none';
                       grayOut(false, "");
                       confirmMessageFunction(returnValue, "cancel");
                   }
        </script>
        <style>
            #attachListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 7%;
                top: 7%;
                _height: expression(document.body.offsetHeight + "px");
            }
        </style>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body class="whitebackgrnd" />
    <div id="cover" ></div>
    <!--DESIGN FOR NEW ALERT BOX ---->
    <div id="AlertBox" class="alert">
        <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Alert</b></p>
        <p id="innerText" class="containerForAlert" style="width: 100%;padding-left: 3px;">

        </p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button"  class="buttonStyleForAlert" value="OK" 
                   onclick="document.getElementById('AlertBox').style.display = 'none';
                           grayOut(false, '');">
        </form>
    </div>

    <div id="ConfirmBox" class="alert">
        <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation(</b></p>
        <p id="innerText1" class="containerForAlert" style="width: 100%;padding-left: 3px;">

        </p>
        <form style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
            <input type="button"  class="buttonStyleForAlert" value="OK" 
                   onclick="yes()">
            <input type="button"  class="buttonStyleForAlert" value="Cancel" 
                   onclick="No()">
        </form>
    </div>
    <!--// ALERT BOX DESIGN ENDS -->

    <html:form action="/fclmarksnumber" name="fclmarksnumberForm" styleId="fcllblmarks" type="com.gp.cvst.logisoft.struts.form.fclmarksnumberForm" scope="request">

        <b class="textlabels">File No&nbsp;:<font color="Red" size="2" style="padding-left:5px;">
            <c:out value="${FileNo}"/></font></b>
        <b class="textlabels" style="padding-left:20px;">Unit No&nbsp;:<font color="Red" size="2" style="padding-left:5px;">
            <c:out value="${UnitNo}"/></font></b>

        <table width="100%" border="0" cellpadding="0" cellspacing="0" id="records" class="tableBorderNew">
            <tr class="textLabels">
                <td>
                    <table width="100%">
                        <%--			    <tr>--%>
                        <%--                 	<td valign="top"><html:textarea property="marksNo" rows="6" cols="16" value="<%=markNos%>" /> --%>
                        <%--                  	</td>--%>
                        <%--               	</tr>--%>
                        <tr>
                            <td>
                                <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew" 
                                       height="100%">
                                    <tr class="tableHeadingNew" >
                                        <td>List of Packages<br></td>
                                        <td align="right">
                                            <c:if test="${manifest != 'M'}">
                                                <input type="button"  value="Add New" class="buttonStyleNew" onclick="display()" align="right"/>
                                            </c:if>
                                            <%--<input type="button" value="Save" class="buttonStyleNew" onclick="submit1()" align="right" id="save"/>--%>
                                            <input type="button" value="Close" class="buttonStyleNew" onclick="closePage()" align="right"/>
                                            <br></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <c:if test="<%=fclBlMarksList.size() == 1%>">
                                                <c:out value="<%=fclBlMarksList.size()%>"/> Item Found.
                                            </c:if>
                                            <c:if test="<%=fclBlMarksList.size() > 1%>">
                                                <c:out value="<%=fclBlMarksList.size()%>"/> Items Found.
                                            </c:if>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" >
                                            <div id="divtablesty1" style="border:thin;overflow:scroll;height:115px;">
                                                <table width="100%"><tr><td>
                                                            <%
                                                                int i = 0;
                                                            %>
                                                            <display:table name="<%=fclBlMarksList%>" pagesize="30" 
                                                                           class="displaytagstyle"  id="acctst">

                                                                <display:setProperty name="paging.banner.placement"
                                                                                     value="none" />
                                                                <%
                                                                    String netweightKgs = "";
                                                                    String netweightLbs = "";
                                                                    String measureCft = "";
                                                                    String measureCbm = "";
                                                                    String descPkgs = "";
                                                                    String pieces = "";
                                                                    String pckgs = "";
                                                                    String descForMaster = "";
                                                                    FclBlMarks blMarks = null;

                                                                    int id = 0;
                                                                    if (fclBlMarksList != null && fclBlMarksList.size() > 0) {
                                                                        blMarks = fclBlMarksList.get(i);
                                                                    }
                                                                    if (null != blMarks && blMarks.getId() != null) {
                                                                        id = blMarks.getId();
                                                                    }
                                                                    if (null != blMarks && blMarks.getNoOfPkgs() != null) {
                                                                        pieces = blMarks.getNoOfPkgs().toString();
                                                                    }
                                                                    if (null != blMarks && blMarks.getUom() != null) {
                                                                        pckgs = blMarks.getUom();
                                                                    }
                                                                    if (blMarks != null && blMarks.getNetweightKgs() != null) {
                                                                        netweightKgs = numberFormat.format(blMarks.getNetweightKgs());
                                                                        if (netweightKgs.equals("0.000")) {
                                                                            //netweightKgs="";
                                                                        }
                                                                    }
                                                                    if (blMarks != null && blMarks.getNetweightLbs() != null) {
                                                                        netweightLbs = numberFormat.format(blMarks.getNetweightLbs());
                                                                        if (netweightLbs.equals("0.000")) {
                                                                            //netweightLbs="";
                                                                        }
                                                                    }
                                                                    if (blMarks != null && blMarks.getMeasureCft() != null) {
                                                                        measureCft = numberFormat.format(blMarks.getMeasureCft());
                                                                        if (measureCft.equals("0.000")) {
                                                                            //measureCft="";
                                                                        }
                                                                    }
                                                                    if (blMarks != null && blMarks.getMeasureCbm() != null) {
                                                                        measureCbm = numberFormat.format(blMarks.getMeasureCbm());
                                                                        if (measureCbm.equals("0.000")) {
                                                                            //measureCbm="";
                                                                        }
                                                                    }
                                                                    if (blMarks != null && blMarks.getDescPckgs() != null) {
                                                                        descPkgs = blMarks.getDescPckgs();
                                                                        //descPackage = StringUtils.abbreviate(descPkgs,25);
                                                                    }
                                                                    if (blMarks != null && blMarks.getDescForMasterBl() != null) {
                                                                        descForMaster = blMarks.getDescForMasterBl();
                                                                        // abbrForMaster =  StringUtils.abbreviate(descForMaster,25);
                                                                    }

                                                                %>

                                                                <display:column title="Pieces"><%=pieces%></display:column>
                                                                <display:column title="Packages"><%=pckgs%></display:column>
                                                                <display:column  title="Gross wgt<p> Kgs" ><%=netweightKgs%></display:column>
                                                                <display:column  title="Gross wgt<p> Lbs"><%=netweightLbs%></display:column>
                                                                <display:column  title="Measure<p> Cft"><%=measureCft%></display:column>
                                                                <display:column  title="Measure<p> Cbm"><%=measureCbm%></display:column>
                                                                <display:column title="Desc of Pack for House BL">
                                                                    <html:textarea property="tempDescpkgs" styleClass="textlabelsBoldForTextBox" style="border:0px;"
                                                                    readonly="true" rows="3" cols="60" value="<%=descPkgs%>" />
                                                                </display:column>
                                                                <%--<display:column title="Desc of Pack for Master BL">
                                                                        <html:textarea property="tempDescForMasterBl" styleClass="textlabelsBoldForTextBox"  style="border:0px;" readonly="true" rows="3" cols="37" value="<%= descForMaster%>"/>
                                                                </display:column>--%>	
                                                                <display:column title="Action">
                                                                    <span class="hotspot" onmouseover="tooltip.show('Edit', null, event);" 
                                                                          onmouseout="tooltip.hide();">
                                                                        <img src="${path}/img/icons/edit.gif" border="0" onclick="marksEdit(<%=id%>)"/>
                                                                    </span>
                                                                    <c:if test="${manifest != 'M'}">
                                                                        <span class="hotspot" onmouseover="tooltip.show('Delete', null, event);" onmouseout="tooltip.hide();">
                                                                            <img src="${path}/img/icons/delete.gif" id="delete" border="0" onclick="marksdelete(<%=id%>)" />
                                                                        </span>
                                                                    </c:if>
                                                                </display:column> 
                                                                <%i++; %>
                                                            </display:table>  <br></td></tr></table>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <c:choose>
                            <c:when test="${importFlag eq false}">
                                <tr>
                                    <td style="padding-top: 0px;">
                                        <div id="addDescription" style="display:none;">
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tableBorderNew">
                                                <tr class="tableHeadingNew">
                                                    <td colspan="4">
                                                        Add Description of Packages
                                                    </td>
                                                    <td colspan="2">
                                                        SOLAS/VGM Information
                                                    </td>
                                                    <td align="right">
                                                        <%if (request.getAttribute("display") != null) {%>
                                                        <input type="button" value="Update" id="updatePackageValues" class="buttonStyleNew" onclick="updatePackages('<%=marksid%>')" style="text-transform: uppercase"/>
                                                        <%} else {%>
                                                        <input type="button" value="Save" id="savePackages" class="buttonStyleNew" onclick="addPackages()"/>
                                                        <%}%>
                                                    </td>
                                                </tr>
                                                <tr class="textlabels">	
                                                    <td>Pieces</td>
                                                    <td>Pkg Type</td>
                                                    <td>Gross Weight</td>
                                                    <td>Measure</td>
                                                    <td>Tare Weight</td>
                                                    <td>Bottom Line VGM Weight</td>
                                                    <td>&nbsp;</td>
                                                </tr>	
                                                <tr class="textlabels">
                                                    <td style="vertical-align: top">
                                                        <html:text styleClass="textlabelsBoldForTextBox" property="noOfpkgs" styleId="noOfpkgs" size="5" maxlength="8"
                                                        value="<%=noOfPkg%>" onkeyup="allowOnlyWholeNumbers(this)" name="noOfpkgs"/></td>
                                                    <td style="vertical-align: top"><input name="uom" class="textlabelsBoldForTextBox" value="<%=uom%>" size="15" maxlength="15" id="uom"/>
                                                        <input Class="textlabelsBoldForTextBox"  name="uom_check"
                                                               id="uom_check" type="hidden" value="<%=uom%>"/>
                                                        <div id="uom_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            function addNoOfPkgsInRequest() {
                                                                return "&noOfpkgs=" + document.getElementById("noOfpkgs").value;
                                                            }
                                                            AjaxAutocompleter("uom", "uom_choices", "", "uom_check",
                                                                    "${path}/actions/getPackageType.jsp?tabName=MARKS_NUMBER&from=0&isDojo=false", "changeFocus('weightKgs')", "addNoOfPkgsInRequest()", "onPkgBlur()");
                                                        </script>   
                                                    </td>
                                                    <td style="vertical-align: top">
                                                        <html:text property="weightKgs" styleId="weightKgs" styleClass="textlabelsBoldForTextBox" value="<%=kgs%>"
                                                                   size="15" maxlength="12"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getWeightKgs()" onchange="getWeightKgs()"/>KGS

                                                    </td>
                                                    <td style="vertical-align: top">
                                                        <html:text property="measureCbm" styleId="measureCbm" styleClass="textlabelsBoldForTextBox" value="<%=cbm%>"
                                                                   size="15" maxlength="12"  onkeyup="checkForNumberAndDecimal(this);"  onkeydown="getMeasureCbm()" onchange="getMeasureCbm()"/>CBM
                                                    </td>
                                                    <td style="vertical-align: top">
                                                        <html:text property="tareWeightKgs" styleId="tareWeightKgs" styleClass="textlabelsBoldForTextBox" value="<%=tareWeightKgs%>"
                                                                   size="15" maxlength="12"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getTareWeightKgs()" onchange="getTareWeightKgs()"/>KGS

                                                    </td>
                                                    <td style="vertical-align: top">
                                                        <html:text property="bottomLineVgmWeightKgs" styleId="bottomLineVgmWeightKgs" styleClass="textlabelsBoldForTextBox" value="<%=bottomLineVgmWeightKgs%>"
                                                                   size="15" maxlength="12"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getBottomLineVgmWeightKgs()" onchange="getBottomLineVgmWeightKgs()"/>KGS
                                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                                        <img src="${path}/img/icons/calc.png" id="calculator" onclick="addWeight()" title="Calculate bottom line weight"/>
                                                    </td>
                                                    <td>
                                                        Auto Convert
                                                        <input type="checkbox" name="convertionCheck" id="convertionCheck" onkeydown="setFcs()" align="right"/>

                                                        <%--<input type="button" value="Conversion" class="buttonStyleNew"  style="width:60px;"
                                                              onclick="convertion()" align="right"/>--%>
                                                    </td>
                                                </tr>
                                                <tr class="textlabels"> 
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td>
                                                        <html:text property="weightLbs" styleId="weightLbs" styleClass="textlabelsBoldForTextBox" value="<%=lbs%>"
                                                                   size="15" maxlength="10"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getWeightLbs()" onchange="getWeightLbs()"/>LBS
                                                    </td>
                                                    <td>
                                                        <html:text property="measureCft" styleId="measureCft" styleClass="textlabelsBoldForTextBox" value="<%=cft%>"
                                                                   size="15" maxlength="11"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getMeasureCft()" onchange="getMeasureCft()"/>CFT
                                                    </td>
                                                    <td>
                                                        <html:text property="tareWeightLbs" styleId="tareWeightLbs" styleClass="textlabelsBoldForTextBox" value="<%=tareWeightLbs%>"
                                                                   size="15" maxlength="10"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getTareWeightLbs()" onchange="getTareWeightLbs()"/>LBS
                                                    </td>
                                                    <td>
                                                        <html:text property="bottomLineVgmWeightLbs" styleId="bottomLineVgmWeightLbs" styleClass="textlabelsBoldForTextBox" value="<%=bottomLineVgmWeightLbs%>"
                                                                   size="15" maxlength="10"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getBottomLineVgmWeightLbs()" onchange="getBottomLineVgmWeightLbs()"/>LBS
                                                    </td>
                                                    <td>&nbsp;</td>
                                                </tr>
                                                <tr class="textlabels">
                                                    <td colspan="7">
                                                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                            <tr class="textlabelsBold">
                                                                <td width="38%">
                                                                    Description of Packages for House BL<b class="mandatoryStarColor">*</b>

                                                                    <span>Predefined Remarks
                                                                        <img src="${path}/img/icons/display.gif" onclick="goToRemarksLookUp('', '1', '${importFlag}')"/>
                                                                    </span>

                                                                </td>
                                                                <td width="2%"></td>
                                                                <td width="38%">
                                                                    Description of Packages for Master BL
                                                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Use House Description for SS Master</strong>', null, event);"
                                                                          onmouseout="tooltip.hide();" style="padding-left:20px;color:black;">
                                                                        Use House
                                                                        <html:checkbox  property="copyDescription"></html:checkbox>
                                                                        </span>
                                                                    </td>
                                                                    <td width="30%"> 
                                                                    </td>
                                                                </tr>
                                                                <tr class="textlabels">
                                                                    <td>
                                                                    <html:textarea styleClass="textlabelsBoldForTextBox" property="descpkgs" styleId="descpkgs" value="<%=descPkg%>"
                                                                                   style="text-transform: uppercase" onkeypress="return checkTextAreaLimit(this, 3750)" onkeyup="limitTextarea(this,75,50)" rows="15" cols="62"
                                                                                   onfocus="focus_watch=setInterval('watchTextarea(descpkgs)',3750)" onblur="clearInterval(focus_watch)"/>
                                                                </td>
                                                                <td>&nbsp;</td>
                                                                <td>
                                                                    <html:textarea styleClass="textlabelsBoldForTextBox" property="descForMasterBl" value="<%=descForMasterBl%>" styleId="descForMasterBl"
                                                                                   style="text-transform: uppercase" onkeypress="return checkTextAreaLimit(this, 500)" onkeyup="limitTextarea(this,15,50)" rows="15" cols="62"
                                                                                   onfocus="focus_watch=setInterval('watchTextarea(descForMasterBl)',250)" onblur="clearInterval(focus_watch)"/>
                                                                </td>
                                                                <td width="30%" style="vertical-align: top" align="right"> Verification Signature &nbsp;&nbsp;&nbsp;
                                                                    <html:text property="verificationSignature" styleId="verificationSignature" value="<%=verificationSignature%>" style="text-transform: uppercase" 
                                                                               styleClass="textlabelsBoldForTextBox" maxlength="40" size="25"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br><br>
                                                                    Verification Date&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                                    <fmt:formatDate pattern="MM/dd/yyyy HH:mm a"  var="verificationDate" value="${verificationDate}"/>
                                                                    <html:text property="verificationDate" styleId="txtcal314" styleClass="textlabelsBoldForTextBox" size="19"
                                                                    style="color:red;text-transform: uppercase" value="<%=verificationDate%>"/>
                                                                    <img src="${path}/img/CalendarIco.gif" alt="cal" name="cal1" width="16" height="16" id="cal314"
                                                                         onmousedown="insertDateFromCalendar(this.id, 9);"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </c:when>   
                            <c:otherwise>
                                <tr>
                                    <td style="padding-top: 0px;">
                                        <div id="addDescription" style="display:none;">
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tableBorderNew">
                                                <tr class="tableHeadingNew">
                                                    <td colspan="2">
                                                        Add Description of Packages
                                                    </td>
                                                    <td align="right"  colspan="3">
                                                        <%if (request.getAttribute("display") != null) {%>
                                                        <input type="button" value="Update" id="updatePackageValues" class="buttonStyleNew" onclick="updatePackages('<%=marksid%>')" style="text-transform: uppercase"/>
                                                        <%} else {%>
                                                        <input type="button" value="Save" id="savePackages" class="buttonStyleNew" onclick="addPackages()"/>
                                                        <%}%>
                                                    </td>
                                                </tr>
                                                <tr class="textlabels">	
                                                    <td>Pieces</td>
                                                    <td>Pkg Type</td>
                                                    <td>Gross Weight</td>
                                                    <td>Measure</td>
                                                    <td>&nbsp;</td>
                                                </tr>	
                                                <tr class="textlabels">
                                                    <td style="vertical-align: top">
                                                        <html:text styleClass="textlabelsBoldForTextBox" property="noOfpkgs" styleId="noOfpkgs" size="5" maxlength="8"
                                                        value="<%=noOfPkg%>" onkeyup="allowOnlyWholeNumbers(this)" name="noOfpkgs"/></td>
                                                    <td style="vertical-align: top"><input name="uom" class="textlabelsBoldForTextBox" value="<%=uom%>" size="15" maxlength="15" id="uom"/>
                                                        <input Class="textlabelsBoldForTextBox"  name="uom_check"
                                                               id="uom_check" type="hidden" value="<%=uom%>"/>
                                                        <div id="uom_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                        <script type="text/javascript">
                                                            function addNoOfPkgsInRequest() {
                                                                return "&noOfpkgs=" + document.getElementById("noOfpkgs").value;
                                                            }
                                                            AjaxAutocompleter("uom", "uom_choices", "", "uom_check",
                                                                    "${path}/actions/getPackageType.jsp?tabName=MARKS_NUMBER&from=0&isDojo=false", "changeFocus('weightKgs')", "addNoOfPkgsInRequest()", "onPkgBlur()");
                                                        </script>   
                                                    </td>
                                                    <td style="vertical-align: top">
                                                        <html:text property="weightKgs" styleId="weightKgs" styleClass="textlabelsBoldForTextBox" value="<%=kgs%>"
                                                                   size="15" maxlength="12"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getWeightKgs()" onchange="getWeightKgs()"/>KGS

                                                    </td>
                                                    <td style="vertical-align: top">
                                                        <html:text property="measureCbm" styleId="measureCbm" styleClass="textlabelsBoldForTextBox" value="<%=cbm%>"
                                                                   size="15" maxlength="12"  onkeyup="checkForNumberAndDecimal(this);"  onkeydown="getMeasureCbm()" onchange="getMeasureCbm()"/>CBM
                                                    </td>
                                                    <td>
                                                        Auto Convert
                                                        <input type="checkbox" name="convertionCheck" id="convertionCheck" onkeydown="setFcs()" align="right"/>

                                                        <%--<input type="button" value="Conversion" class="buttonStyleNew"  style="width:60px;"
                                                              onclick="convertion()" align="right"/>--%>
                                                    </td>
                                                </tr>
                                                <tr class="textlabels"> 
                                                    <td>&nbsp;</td>
                                                    <td>&nbsp;</td>
                                                    <td>
                                                        <html:text property="weightLbs" styleId="weightLbs" styleClass="textlabelsBoldForTextBox" value="<%=lbs%>"
                                                                   size="15" maxlength="10"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getWeightLbs()" onchange="getWeightLbs()"/>LBS
                                                    </td>
                                                    <td>
                                                        <html:text property="measureCft" styleId="measureCft" styleClass="textlabelsBoldForTextBox" value="<%=cft%>"
                                                                   size="15" maxlength="11"  onkeyup="checkForNumberAndDecimal(this);" onkeydown="getMeasureCft()" onchange="getMeasureCft()"/>CFT
                                                    </td>
                                                    <td>&nbsp;</td>
                                                </tr>
                                                <tr class="textlabels">
                                                    <td colspan="5">
                                                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                            <tr class="textlabelsBold">
                                                                <td width="48%">
                                                                    Description of Packages for House BL<b class="mandatoryStarColor">*</b>

                                                                    <span>Predefined Remarks
                                                                        <img src="${path}/img/icons/display.gif" onclick="goToRemarksLookUp('', '1', '${importFlag}')"/>
                                                                    </span>

                                                                </td>
                                                                <td width="2%"></td>
                                                                <td width="48%">
                                                                    Description of Packages for Master BL
                                                                    <span class="hotspot" onmouseover="tooltip.show('<strong>Use House Description for SS Master</strong>', null, event);"
                                                                          onmouseout="tooltip.hide();" style="padding-left:20px;color:black;">
                                                                        Use House
                                                                        <html:checkbox  property="copyDescription"></html:checkbox>
                                                                        </span>
                                                                    </td>
                                                                </tr>
                                                                <tr class="textlabels">
                                                                    <td>
                                                                    <html:textarea styleClass="textlabelsBoldForTextBox" property="descpkgs" styleId="descpkgs" value="<%=descPkg%>"
                                                                                   style="text-transform: uppercase" onkeypress="return checkTextAreaLimit(this, 3750)" onkeyup="limitTextarea(this,75,50)" rows="15" cols="70"
                                                                                   onfocus="focus_watch=setInterval('watchTextarea(descpkgs)',3750)" onblur="clearInterval(focus_watch)"/>
                                                                </td>
                                                                <td>&nbsp;</td>
                                                                <td>
                                                                    <html:textarea styleClass="textlabelsBoldForTextBox" property="descForMasterBl" value="<%=descForMasterBl%>" styleId="descForMasterBl"
                                                                                   style="text-transform: uppercase" onkeypress="return checkTextAreaLimit(this, 500)" onkeyup="limitTextarea(this,15,50)" rows="15" cols="70"
                                                                                   onfocus="focus_watch=setInterval('watchTextarea(descForMasterBl)',250)" onblur="clearInterval(focus_watch)"/>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </table>
                </td>
            </tr>
        </table>
        <html:hidden property="buttonValue" styleId="buttonValue" />
        <html:hidden property="bol" value="<%=bol%>"/>
        <html:hidden property="containerId" value="<%=containerId%>"/>
        <html:hidden property="index"/>
        <input type="hidden" name="fileNo" value="${fileNo}"/>
        <input type="hidden" name="completeFileNo" value="${FileNo}"/>
        <input type="hidden" name="UnitNumber" value="${UnitNo}"/>
        <input type="hidden" name="manifest" value="${manifest}"/>
        <input type="hidden" name="index1" value="<%=indexValue%>"/>
    </html:form>
</body>

<script>document.getElementById("convertionCheck").checked = true;</script>
<%if (useHouseDesc != null && !useHouseDesc.equals("N")) { %>
<script type="text/javascript">
    document.fclmarksnumberForm.copyDescription.checked = true;
</script>
<%} else { %>
<script type="text/javascript">
    document.fclmarksnumberForm.copyDescription.checked = false;
</script>
<%}%>
<%if (request.getAttribute("display") != null) {%>
<script type="text/javascript">document.getElementById("addDescription").style.display = "block";
    //document.getElementById("save").style.visibility="hidden";</script>
    <%}%>
    <%if (view.equals("3")) {%>
<script type="text/javascript">
    onload = "disabled('<%=view%>')";
</script>
<script type="text/javascript">makeFormBorderless(document.getElementById("fcllblmarks"));</script>
<%}%>
<%--<%@include file="../includes/baseResourcesForJS.jsp" %>--%>
</html>

