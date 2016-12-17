var junk = false;
var myArray = new Array();
var myArray1 = new Array();
var result = false;
var importFlag = document.getElementById('fileType') && null !== document.getElementById('fileType').value && document.getElementById('fileType').value === 'I' ? true : false;
function loadData() {
    if (junk == false) {
        for (var i = 0; i < document.QuotesForm.elements.length; i++) {
            if (document.QuotesForm.elements[i].type == 'radio' ||
                    document.QuotesForm.elements[i].type == 'checkbox') {
                if (document.QuotesForm.elements[i].checked) {
                    myArray[i] = document.QuotesForm.elements[i].value;
                }
            } else {
                myArray[i] = document.QuotesForm.elements[i].value;
            }
        }
        junk = true;
    }
}
function compareWithOldArray() {
    for (var i = 0; i < document.QuotesForm.elements.length; i++) {
        if (document.QuotesForm.elements[i].type == 'radio' ||
                document.QuotesForm.elements[i].type == 'checkbox') {
            if (document.QuotesForm.elements[i].checked) {
                myArray1[i] = document.QuotesForm.elements[i].value;
            }
        } else {
            myArray1[i] = document.QuotesForm.elements[i].value;
        }
    }
    for (var j = 0; j < myArray1.length; j++) {
        if (myArray[j] != myArray1[j]) {
            result = true;
        }
    }
    if (result == true) {
        goback();

    } else {
        goBackCall();
    }
}
function sendToDojoJsp() {
    var optionValue = "";
    var length = "";
    var i = 0;
    if (document.QuotesForm.zip.value != "" && document.getElementById("doorDestination").value == "") {
        optionValue = document.getElementById("move").options[document.getElementById("move").selectedIndex].text;
        if (optionValue != "DOOR TO DOOR" && optionValue != "DOOR TO RAIL" && optionValue != "DOOR TO PORT"
                && optionValue != "RAMP TO DOOR" && optionValue != "RAMP TO RAIL" && optionValue != "RAMP TO PORT") {
            length = document.getElementById("move").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("move").options[i].text == "DOOR TO PORT") {
                    document.getElementById("move").selectedIndex = i;
                }
            }
            alertNew("Please select the option that starts from DOOR");
        }
    } else if (document.QuotesForm.zip.value == "" && document.getElementById("doorDestination").value != "") {
        optionValue = document.getElementById("move").options[document.getElementById("move").selectedIndex].text;
        if (optionValue != "PORT TO DOOR" && optionValue != "RAIL TO DOOR" && optionValue != "DOOR TO DOOR"
                && optionValue != "RAMP TO DOOR" && optionValue != "RAMP TO RAIL" && optionValue != "RAMP TO PORT") {
            length = document.getElementById("move").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("move").options[i].text == "PORT TO DOOR") {
                    document.getElementById("move").selectedIndex = i;
                }
            }
            alertNew("Please select the option that ends from DOOR");
        }
    } else if (document.QuotesForm.zip.value != "" && document.getElementById("doorDestination").value != ""
            && optionValue != "RAMP TO DOOR" && optionValue != "RAMP TO RAIL" && optionValue != "RAMP TO PORT") {
        optionValue = document.getElementById("move").options[document.getElementById("move").selectedIndex].text;
        if (optionValue != "DOOR TO DOOR") {
            length = document.getElementById("move").length;
            for (i = 0; i < length; i++) {
                if (document.getElementById("move").options[i].text == "DOOR TO DOOR") {
                    document.getElementById("move").selectedIndex = i;
                }
            }
            alertNew("Please select the option that start and ends from DOOR");
        }
    }
}
function setDefaultAgent(companyCode) {
    if (companyCode == "02") {
        document.QuotesForm.defaultAgent[1].checked = true;
    } else {
        document.QuotesForm.defaultAgent[0].checked = true;
    }
}
function getMultipleCarrierContactNameAndEmailfromPopup(val1, val2, val3, val4) {
    document.QuotesForm.carrierContact.value = val1;
    document.QuotesForm.carrierEmail.value = val2;
    if (val3 != "" && val3 != undefined && val3 != null) {
        document.QuotesForm.carrierPhone.value = val3;
    }
    if (val4 != "" && val4 != undefined && val4 != null) {
        document.QuotesForm.carrierFax.value = val4;
    }
}
function updateContactInfoAfterDeletion(contName, contEmail) {
    var contactName = document.getElementById('contactName').value;
    var contactEmail = document.getElementById('email').value;
    var contactNameArray = contactName.split(",");
    var newContactName = '';
    for (var i = 0; i < contactNameArray.length; i++) {
        var temp = contactNameArray[i];
        if (temp == contName) {

        } else {
            if (newContactName == '') {
                newContactName = temp;
            } else {
                newContactName = newContactName + "," + temp;
            }
        }
    }
    document.getElementById('contactName').value = newContactName;

    //--UPDATING CONTACT EMAIL---
    var contactEmailArray = contactEmail.split(",");
    var newContactEmail = '';
    for (i = 0; i < contactEmailArray.length; i++) {
        temp = contactEmailArray[i];
        if (temp == contEmail) {

        } else {
            if (newContactEmail == '') {
                newContactEmail = temp;
            } else {
                newContactEmail = newContactEmail + "," + temp;
            }
        }
    }
    document.getElementById('email').value = newContactEmail;
}
function updateCarrierContactAfterDeletion(contName, contEmail) {
    var carrierContact = document.getElementById('carrierContact').value;
    var carrierEmail = document.getElementById('carrierEmail').value;

    var carrierContactArray = carrierContact.split(",");
    var newContactName = '';
    for (var i = 0; i < carrierContactArray.length; i++) {
        var temp = carrierContactArray[i];
        if (temp == contName) {

        } else {
            if (newContactName == '') {
                newContactName = temp;
            } else {
                newContactName = newContactName + "," + temp;
            }
        }
    }
    document.getElementById('carrierContact').value = newContactName;

    //--UPDATING CARRIER EMAIL---
    var carrierEmailArray = carrierEmail.split(",");
    var newContactEmail = '';
    for (i = 0; i < carrierEmailArray.length; i++) {
        temp = carrierEmailArray[i];
        if (temp == contEmail) {

        } else {
            if (newContactEmail == '') {
                newContactEmail = temp;
            } else {
                newContactEmail = newContactEmail + "," + temp;
            }
        }
    }
    document.getElementById('carrierEmail').value = newContactEmail;
}
function disableAutoFF(setFocus) {
    if (undefined != document.getElementById('portofDischarge') && null != document.getElementById('portofDischarge')
            && '' != document.getElementById('portofDischarge').value) {
        defaultAgentforDesc();
        if (importFlag == 'false') {
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "checkForTheRegion",
                    param1: document.getElementById('portofDischarge').value
                },
                success: function (data) {
                    if (jQuery.trim(data) === "true") {
                        checkforCommodity();
                    } else {
                        document.getElementById('n5').checked = true;
                        document.getElementById('n5').disabled = true;
                        document.getElementById('y5').disabled = true;
                    }
                }
            });
        }
    }
    if (undefined != setFocus && null != setFocus && '' != setFocus) {
        document.getElementById(setFocus).focus();
    }
    var portname = document.getElementById('portofDischarge').value;
    var unloc = portname.substring(portname.lastIndexOf("(") + 1, portname.lastIndexOf(")"));
    var contains = (insuranceAllowed.indexOf(unloc) != -1);
    if (portname !== null && portname !== '') {
        if (contains) {
            document.getElementById('insuranceYes').disabled = true;
            document.getElementById('insurance').checked = true;
            document.getElementById('insurance').disabled = true;
        } else {
            document.getElementById('insuranceYes').disabled = false;
            document.getElementById('insurance').disabled = false;
        }
    }
}

function checkforCommodity() {
    var commcode = document.getElementById("commcode").value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForCommodity",
            param1: commcode
        },
        success: function(data) {
            if (jQuery.trim(data) === "true") {
                document.getElementById('n5').disabled = false;
                document.getElementById('y5').disabled = false;
            } else {
                document.getElementById('n5').checked = true;
                document.getElementById('n5').disabled = true;
                document.getElementById('y5').disabled = true;
            }
        }
    });
}

function checkIfRatesAreFromGetRates() {
    var val;
    if (document.getElementById("ratesExpandTable") != null) {
        var expandTable = document.getElementById("ratesExpandTable");
        var rowObj = expandTable.rows[1];
        var chargecode = rowObj.cells[3].innerHTML;
        var redStarIndex = chargecode.indexOf("*");
        if (redStarIndex != -1) {
            val = 'true';
            return val;
        }
    } else {
        val = 'true';
        return val;
    }

}
function showRateGrid(route, path) {
    var origin = "";
    var destination = "";
    var selectedList = "";
    var distance = "";
    var distanceList = "";
    var fileType = "";
    var doorOrigin = document.QuotesForm.doorOrigin;
    var doorDestination = document.QuotesForm.zip;
    var checkBoxes = document.getElementsByName("originDestination");
    var importFlag = document.getElementById('fileType') && null !== document.getElementById('fileType').value && document.getElementById('fileType').value === 'I' ? true : false;
     if (importFlag === true) {
           fileType = document.getElementById("fileType").value;
        }else{
           fileType = null;
        }
    for (i = 0; i < checkBoxes.length; i++) {
        if (checkBoxes[i].checked) {
            distance = "";
            distance = null != checkBoxes[i].parentNode.childNodes[1] ? checkBoxes[i].parentNode.childNodes[1].id : "";
            if (selectedList == "") {
                selectedList = checkBoxes[i].value;
                if (route == "Destination" && null != doorOrigin && trim(doorOrigin.value) != "") {
                    distanceList = "" != trim(distance) && null != trim(distance) ? checkBoxes[i].value + "=" + distance : checkBoxes[i].value;
                } else if (route === "Origin" && null !== doorDestination && trim(doorDestination.value) != "") {
                    distanceList = "" != trim(distance) && null != trim(distance) ? checkBoxes[i].value + "=" + distance : checkBoxes[i].value;
                    
                }
            } else {
                selectedList = selectedList + "," + checkBoxes[i].value;
                if (route == "Destination" && null != doorOrigin && trim(doorOrigin.value) != "") {
                    distanceList = "" != trim(distance) && null != trim(distance) ? distanceList + "," + checkBoxes[i].value + "=" + distance : distanceList + "," + checkBoxes[i].value;
                } else if (route == "Origin" && null != doorDestination && trim(doorDestination.value) != "") {
                    distanceList = "" != trim(distance) && null != trim(distance) ? checkBoxes[i].value + "=" + distance : checkBoxes[i].value;
                }
            }
        }
    }
    if (selectedList == "") {
        alert("Please Select atleast One");
        return;
    }
    if ("Origin" == route) {
        origin = document.QuotesForm.isTerminal.value;
        destination = selectedList;
    } else {
        destination = document.QuotesForm.portofDischarge.value;
        origin = selectedList;
    }
    var haz = "N";
    if (document.QuotesForm.hazmat[0].checked) {
        haz = "Y";
    }
    var region = document.getElementsByName("region");
    var selectedRegion = "";
    for (i = 0; i < region.length; i++) {
        if (region[i].checked) {
            selectedRegion = selectedRegion != "" ? selectedRegion + ', ' + region[i].id : region[i].id;
        }
    }
    var imsChecked = "";
    if (jQuery('#enableIMS').attr("checked")) {
        imsChecked = "checked";
    }
    var destinationPort = document.QuotesForm.portofDischarge.value;
    var originPort = "";
    if (trim(document.QuotesForm.isTerminal.value) != "") {
        originPort = document.QuotesForm.isTerminal.value;
    } else {
        originPort = document.QuotesForm.doorOrigin.value;
    }
    url = path + '/rateGrid.do?action=' + route + '&origin=' + origin +
            "&destination=" + destination + "&doorOrigin=" + document.getElementById('doorOrigin').value + "&commodity="
            + document.QuotesForm.commcode.value + '&hazardous=' + haz + "&zip=" + document.QuotesForm.zip.value + "&doorOrigin="
            + doorOrigin.value + "&region=" + selectedRegion + "&distances=" + distanceList + "&imsChecked=" + imsChecked + "&destinationPort=" + destinationPort + "&originPort=" + originPort+"&fileType="+fileType+"&doorDestination="+doorDestination.value;
    GB_show('FCL Rates Comparison Grid', url, document.body.offsetHeight - 20, document.body.offsetWidth - 100);

}
function closeOriginDestinationList() {
    if (null != document.getElementById("originAndDestinationDiv")) {
        document.body.removeChild(document.getElementById("originAndDestinationDiv"));
        document.QuotesForm.portofDischarge.focus();
    }
    closePopUp();
}

function checkShowAllCity() {
    if (document.getElementById("showAllCity") && document.getElementById("showAllCity").checked
            && (trim(document.QuotesForm.portofDischarge.value) == "" || trim(document.QuotesForm.isTerminal.value) == "")) {
        alertNew("PLEASE SELECT DESTINATION PORT AND ORIGIN");
        jQuery("#customerName").css("border-color", "red");
        jQuery("#portofDischarge").css("border-color", "red");
        jQuery("#isTerminal").css("border-color", "red");
        document.getElementById("showAllCity").checked = false;
        return;
    }
}
function allowOnlyWholeNumbers(obj) {
    var result;
    if (!/^[1-9 . ]\d*$/.test(obj.value)) {
        result = obj.value.replace(/[^0-9 . ]+/g, '');
        obj.value = result;
        return false;
    }
    return true;
}
function checkClientConsignee() {
    var path = "";
    if (document.getElementById("clientConsigneeCheck")) {
        if (document.getElementById("clientConsigneeCheck").checked) {
            path = "&consigneeCheck=true";
        } else {
            path = "&consigneeCheck=false";
        }
    }
    appendEncodeUrl(path);
}
function tabMoveClient(importFlag) {
    if (importFlag == 'true') {
        document.QuotesForm.isTerminal.focus();
    } else {
        document.QuotesForm.portofDischarge.focus();
    }
}
function tabMoveDoordest() {
    document.getElementById("routedAgentCheck").focus();
}
function tabMoveErt() {
    document.QuotesForm.goodsdesc.focus();
}
function tabMoveRemark() {
    document.getElementById("save1").focus();
}
function ImportDoorOrgin() {
    document.QuotesForm.zip.focus();
}
function clearNewClient() {
    if (document.getElementById("clientConsigneeCheck").checked) {
        document.getElementById('newClient').checked = false;
        document.getElementById('clientNumber').value = "";
        document.getElementById('clienttype').value = "";
        document.getElementById('contactName').value = "";
        document.getElementById('email').value = "";
        document.getElementById('phone').value = "";
        document.getElementById('fax').value = "";
        document.getElementById("contactButton").style.visibility = 'hidden';
        document.getElementById('newerClient').style.display = "none";
        document.getElementById('existingClient').style.display = "block";
    } else {
        document.getElementById('customerName').value = "";
    }
}

function getCreditStatus() {
    var accountNumber = document.getElementById("clientNumber").value;
    if (trim(accountNumber) != '') {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getCreditStatus",
                param1: accountNumber
            },
            success: function (data) {
                if (data === "Suspended/See Accounting") {
                    alertNew("SUSPENDED CREDIT");
                    document.getElementById("creditStatusCol").style.display = "block";
                    document.getElementById("creditStatus").innerHTML = "SUSPENDED CREDIT";
                } else {
                    document.getElementById("creditStatusCol").style.display = "none";
                    document.getElementById("creditStatus").innerHTML = "";
                }
            }
        });
    } else {
        document.getElementById("creditStatusCol").style.display = "none";
        document.getElementById("creditStatus").innerHTML = "";
    }
}

function alertNewForDefaultValues(text) {
    DisplayAlerts("AlertBoxDefaultValues", 200, 300, text, window.center({
        width: 100,
        height: 100
    }));
}
function DisplayAlerts(id, left, top, text, point) {
    document.getElementById(id).style.left = left + "px";
    document.getElementById(id).style.top = top + "px";
    jQuery("#AlertBoxDefaultValues").find("#innerText").html(text);
    document.getElementById(id).style.display = "block";
    document.getElementById(id).style.left = point.x - 100 + "px";
    document.getElementById(id).style.top = point.y + "px";
    document.getElementById(id).style.zIndex = "1000";
    grayOut(true, "");
}
function getEmptyClient() {
    if (document.getElementById('customerName1').value == "") {
        document.QuotesForm.clientNumber.value = "";
        document.QuotesForm.clienttype.value = "";
        document.QuotesForm.contactName.value = "";
        document.QuotesForm.phone.value = "";
        document.QuotesForm.fax.value = "";
        document.QuotesForm.email.value = "";
        document.getElementById("contactButton").style.visibility = 'hidden';
    }
}
function newClientEQ() {
    if (document.getElementById('newClient').checked) {
        if (document.getElementById("clientConsigneeCheck")) {
            document.getElementById("clientConsigneeCheck").checked = false;
        }
        document.getElementById('newerClient').style.display = "block";
        document.getElementById('existingClient').style.display = "none";
        onCustomerBlur("newClient");
    } else {
        document.getElementById('newerClient').style.display = "none";
        document.getElementById('existingClient').style.display = "block";
        document.getElementById('customerName1').value = "";
    }
}
jQuery(document).ready(function () {
    window.parent.closePreloading();
});

function loadFunction() {
    /*---hidding glass icon for origin and destination also making
     Pol & pod read Only before rates are added for rated Quote -----*/
    if (document.getElementById("rates").checked) {
        document.getElementById("placeofReceipt").readOnly = true;
        document.getElementById('placeofReceipt').tabIndex = -1;
        document.getElementById("placeofReceipt").style.border = 0;
        document.getElementById("placeofReceipt").style.backgroundColor = '#CCEBFF';
        document.getElementById("finalDestination").readOnly = true;
        document.getElementById('finalDestination').tabIndex = -1;
        document.getElementById("finalDestination").style.border = 0;
        document.getElementById("finalDestination").style.backgroundColor = '#CCEBFF';
        document.getElementById("polLookUp").style.visibility = 'hidden';
        document.getElementById("podLookUp").style.visibility = 'hidden';
    } else {
    }
}
function call() {
    var cvr = document.getElementById("cover");
    if (document.body && (document.body.scrollWidth || document.body.scrollHeight)) {
        var pageWidth1 = document.body.scrollWidth + 'px';
    } else if (document.body.offsetWidth) {
        pageWidth1 = document.body.offsetWidth + 'px';
    } else {
        pageWidth1 = '100%';
    }
    cvr.style.width = pageWidth1;
    cvr.style.height = '100%';
    cvr.style.display = "block";
    document.getElementById('newProgressBar').style.display = "block";
}
function call2() {
    var cvr = document.getElementById("cover");
    cvr.style.display = "none";
    document.getElementById('newProgressBar').style.display = "none";
}
function numericTextbox(text) {
    var dateval = text.value
    if (dateval != "") {
        if ((!isNaN(parseFloat(dateval)) && isFinite(dateval)) == false) {
            alertNew("Please Enter Numeric Value For Transit Days");
            document.getElementById("noOfDays").value = "";
            document.getElementById("noOfDays").focus();
            return;
        }
    }
}
function SAVE(importFlag) {
    checkbox();
    var displayAlert = "";
    if (document.QuotesForm.quotationDate.value == "") {
        alertNew("Please enter the Quotation Date");
        return;
    }
    if (document.QuotesForm.isTerminal.value == "") {
        displayAlert += "--->Please select Origin<br>";
        jQuery("#isTerminal").css("border-color", "red");
    }
    if (document.QuotesForm.commcode.value == "") {
        displayAlert += "--->Please select Commodity Code<br>";
        jQuery("#commcode").css("border-color", "red");
    }
    if (document.QuotesForm.portofDischarge.value == "") {
        displayAlert += "--->Please select Destination<br>";
        jQuery("#portofDischarge").css("border-color", "red");
    }
    if (document.getElementById("newClient").checked) {
        if (document.QuotesForm.customerName1 && document.QuotesForm.customerName1.value == "") {
            displayAlert += "--->Please select Client<br>";
            jQuery("#customerName1").css("border-color", "red");
        }
    } else {
        if (document.QuotesForm.customerName && document.QuotesForm.customerName.value == "") {
            displayAlert += "--->Please select Client<br>";
            jQuery("#customerName").css("border-color", "red")
        }
    }
    if ("" != displayAlert) {
        alertNew(displayAlert);
        return;
    }
    if (document.QuotesForm.issuingTerminal.value == "") {
        alertNew("Please select issuing teminal");
        jQuery("#issuingTerminal").css("border-color", "red");
        return;
    }
    if (document.QuotesForm.goodsdesc.value.length > 500) {
        alertNew("Please donot enter more than 500 characters");
        return;
    }
    if (document.QuotesForm.comment.value.length > 500) {
        alertNew("Please donot enter more than 500 characters");
        return;
    }
    if (document.QuotesForm.goodsdesc.value == "" && importFlag != 'true') {
        alertNew("Please enter Goods Description");
        return;
    }
    if (document.QuotesForm.isTerminal.value == document.QuotesForm.doorOrigin.value) {
        alertNew("Please change either Origin or Door Origin, both cannot be same");
        return;
    }
    if (document.QuotesForm.ratesNonRates[1].checked) {
        alertNew("Please Choose Non-Rated Option");
        return;
    }
    if (document.getElementById("zip").value != "" && document.getElementById("rampCheck").checked == true) {
        length = document.getElementById("move").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("move").options[i].text == "RAMP TO PORT") {
                document.getElementById("move").selectedIndex = i;
            }
        }
    }
    if (document.QuotesForm.zip.value == "" && document.QuotesForm.doorDestination.value == "" && document.getElementById("rampCheck").checked == false) {
        var length = document.getElementById('move').length;
        for (var i = 0; i < length; i++) {
            if (document.getElementById('move').options[i].text == 'PORT TO PORT') {
                document.getElementById('move').selectedIndex = i;
            }
        }
    }
    if (document.QuotesForm.zip.value != "" && document.QuotesForm.doorDestination.value == "" && document.getElementById("rampCheck").checked == false) {
        length = document.getElementById("move").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("move").options[i].text == "DOOR TO PORT") {
                document.getElementById("move").selectedIndex = i;
            }
        }
    }
    document.getElementById("save").disabled = true;
    document.getElementById("save1").disabled = true;
    document.QuotesForm.buttonValue.value = "save";
    window.parent.showPreloading();
    document.QuotesForm.submit();
}
function copyValPol() {
    if (document.QuotesForm.ratesNonRates[0].checked) {
        var pol = document.getElementById("isTerminal_check").value;
        document.getElementById("placeofReceipt").value = pol;

    } else {
        document.getElementById("placeofReceipt").value = "";
    }
}
function copyValPod() {
    if (document.QuotesForm.ratesNonRates[0].checked) {
        var pod = document.getElementById("portofDischarge_check").value;
        document.getElementById("finalDestination").value = pod;

    } else {
        document.getElementById("finalDestination").value = "";
    }
}
function numberChanged(obj) {
    checkbox();
    var rowindex = obj.parentNode.parentNode.rowIndex;
    --rowindex;
    var num1 = document.getElementsByName("numbers");
    var n = num1[rowindex].value;
    document.QuotesForm.numbIdx.value = rowindex;
    document.QuotesForm.numbers1.value = n;
    document.QuotesForm.buttonValue.value = "numberChanged";
    document.QuotesForm.submit();
}
function deleteInland() {
    var message = "";
    var importFlag =document.getElementById("importFlag").value;
    if (document.QuotesForm.inland[0].checked) {
        if (document.getElementById("rampCheck").checked) {
            message = "Intermodal Ramp Charges,Door Origin And Zip will be deleted, and all the Quotation changes will be saved<br><br> " +
                    "Please ensure that NVO Move is Change from ramp !   Are you sure?";
        } else {
            if (importFlag === 'true') {
                message = "Delivery Charges,Door Origin And Zip will be deleted, and all the Quotation changes will be saved<br><br> " +
                        "Please ensure that NVO Move is Change from Door !   Are you sure?";
            } else {
                message = "Inland Charges,Door Origin And Zip will be deleted, and all the Quotation changes will be saved<br><br> " +
                        "Please ensure that NVO Move is Change from Door !   Are you sure?";
            }
        }
        confirmNew(message, "inland");
    }
}
function getintermodel1() {
    confirmNew("Do you want to delete Intermodel Charges?", "intermodal");
}
function getinsurances() {
    document.QuotesForm.costofgoods.readOnly = false;
}
function getinsurance1() {
    document.QuotesForm.costofgoods.readOnly = true;
    document.QuotesForm.buttonValue.value = "deleteInsuranceToBl";
    document.QuotesForm.submit();
}
function checkbox() {
    var check = "";
    if (document.QuotesForm.include != undefined) {
        if (document.QuotesForm.include.length != undefined) {
            for (var i = 0; i < document.QuotesForm.include.length; i++) {
                if (document.QuotesForm.include[i].checked) {
                    check = check + "1,";
                } else {
                    check = check + "0,";
                }
            }
        } else {
            if (document.QuotesForm.include.checked) {
                check = check + "1,";
            } else {
                check = check + "0,";
            }
        }
    }
    var check1 = "";
    if (document.QuotesForm.print != undefined) {
        if (document.QuotesForm.print.length != undefined) {
            for (i = 0; i < document.QuotesForm.print.length; i++) {
                if (document.QuotesForm.print[i].checked) {
                    check1 = check1 + "1,";
                } else {
                    check1 = check1 + "0,";
                }
            }
        } else {
            if (document.QuotesForm.print.checked) {
                check1 = check1 + "1,";
            } else {
                check1 = check1 + "0,";
            }
        }
    }
    var check2 = "";
    if (document.QuotesForm.otherinclude != undefined) {
        if (document.QuotesForm.otherinclude.length != undefined) {
            for (i = 0; i < document.QuotesForm.otherinclude.length; i++) {
                if (document.QuotesForm.otherinclude[i].checked) {
                    check2 = check2 + "1,";
                } else {
                    check2 = check2 + "0,";
                }
            }
        } else {
            if (document.QuotesForm.otherinclude.checked) {
                check2 = check2 + "1,";
            } else {
                check2 = check2 + "0,";
            }
        }
    }
    var check3 = "";
    if (document.QuotesForm.otherprint != undefined) {
        if (document.QuotesForm.otherprint.length != undefined) {
            for (i = 0; i < document.QuotesForm.otherprint.length; i++) {
                if (document.QuotesForm.otherprint[i].checked) {
                    check3 = check3 + "1,";
                } else {
                    check3 = check3 + "0,";
                }
            }
        } else {
            if (document.QuotesForm.otherprint.checked) {
                check3 = check3 + "1,";
            } else {
                check3 = check3 + "0,";
            }
        }
    }
    document.QuotesForm.check.value = check;
    document.QuotesForm.check1.value = check1;
    document.QuotesForm.check2.value = check2;
    document.QuotesForm.check3.value = check3;
}
function recalcfunction() {
    checkbox();
    document.QuotesForm.buttonValue.value = "recalc";
    document.QuotesForm.submit();
}
function getspcleqpmt(obj) {
    if (obj.value == 'Y') {
        document.QuotesForm.specialEqpmt.disabled = false;
    }
}
function getspcleqpmt1() {
    document.QuotesForm.specialEqpmt.disabled = true;
}

function printcheck() {
    if (document.QuotesForm.include.length == null) {
        if (!document.QuotesForm.include.checked) {
            document.QuotesForm.print.checked = false;
        }
    }
    for (var i = 0; i < document.QuotesForm.include.length; i++) {
        if (!document.QuotesForm.include[i].checked) {
            document.QuotesForm.print[i].checked = false;
        }
    }
}
function printcheck1() {
    if (document.QuotesForm.otherinclude.length == null) {
        if (!document.QuotesForm.otherinclude.checked) {
            document.QuotesForm.otherprint.checked = false;
        }
    }
    for (var i = 0; i < document.QuotesForm.otherinclude.length; i++) {
        if (!document.QuotesForm.otherinclude[i].checked) {
            document.QuotesForm.otherprint[i].checked = false;
        }
    }
}
function getorigincheck() {
    if (document.QuotesForm.polCheck.checked) {
        document.QuotesForm.polCheck.checked = false;
    }
}
function getpolcheck() {
    if (document.QuotesForm.originCheck.checked) {
        document.QuotesForm.originCheck.checked = false;
    }
}
function getpodcheck() {
    if (document.QuotesForm.destinationCheck.checked) {
        document.QuotesForm.destinationCheck.checked = false;
    }
}
function getdestinationcheck() {
    if (document.QuotesForm.podCheck.checked) {
        document.QuotesForm.podCheck.checked = false;
    }
}
function load() {
    getRatesNonRatesY();
    getspcleqpmt1();
    loadData();

    if (document.QuotesForm.ratesNonRates[1].checked) {
        document.getElementById("getRates").style.visibility = "visible";
        if (document.getElementById("bulletRates")) {
            document.getElementById("bulletRates").style.visibility = "visible";
        }
        document.getElementById("breakBulk").style.visibility = "hidden";
    } else {
        document.getElementById("getRates").style.visibility = "hidden";
        if (document.getElementById("bulletRates")) {
            document.getElementById("bulletRates").style.visibility = "hidden";
        }
        document.getElementById("breakBulk").style.visibility = "visible";
    }

    if (document.QuotesForm.customerName.value == "") {
        document.getElementById("contactButton").style.visibility = 'hidden';
    }
    if (document.QuotesForm.sslDescription.value == "") {
        document.getElementById("contactNameButton").style.visibility = 'hidden';
    }

    if (document.QuotesForm.insurance[1].checked) {
        document.QuotesForm.costofgoods.readOnly = true;
    }
    if (document.getElementById('newClient').checked) {
        document.getElementById('newerClient').style.display = "block";
        document.getElementById('existingClient').style.display = "none";
    } else {
        document.getElementById('newerClient').style.display = "none";
        document.getElementById('existingClient').style.display = "block";
    }
    var a = document.getElementById("collapseRates").innerHTML;
    var b = document.getElementById("expandRates").innerHTML;
    document.getElementById("collapseRates").innerHTML = b;
    document.getElementById("expandRates").innerHTML = a;
    document.getElementById("collapseRates").style.visibility = 'hidden';
    document.getElementById("expandRates").style.visibility = 'visible';
    if (document.QuotesForm.commcode.value == '006100') {
        document.QuotesForm.description.style.visibility = 'hidden';
        document.getElementById("descriptionLabel").style.visibility = 'hidden';
    } else {
        document.QuotesForm.description.style.visibility = 'visible';
        document.getElementById("descriptionLabel").style.visibility = 'visible';
    }

}
function getExpand() {
    var a = document.getElementById("collapseRates").innerHTML;
    var b = document.getElementById("expandRates").innerHTML;
    document.getElementById("collapseRates").innerHTML = b;
    document.getElementById("expandRates").innerHTML = a;
    document.getElementById("collapseRates").style.visibility = 'hidden';
    document.getElementById("expandRates").style.visibility = 'visible';
}
function getComCodeDesc(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getCommodityCode",
                param1: document.QuotesForm.commcode.value
            },
            success: function(data) {
                populateComCodeDesc(data);
            }
        });
    }
}
function populateComCodeDesc(data) {
    if (data != "" || data != undefined) {
        document.QuotesForm.description.value = data.codedesc;
    }
    if (document.QuotesForm.commcode.value == '006100') {
        document.QuotesForm.description.style.visibility = 'hidden';
        document.getElementById("descriptionLabel").style.visibility = 'hidden';
    } else {
        document.QuotesForm.description.style.visibility = 'visible';
        document.getElementById("descriptionLabel").style.visibility = 'visible';
    }
}
function setDojoAction() {
    var path = "";
    if (!document.getElementById('nonRated').checked) {
        if (document.getElementById('destinationCity').checked) {
            path = "&origin="
                    + document.QuotesForm.isTerminal.value + "&radio=city";

        } else {
            path = "&origin="
                    + document.QuotesForm.isTerminal.value + "&radio=country";

            ;
        }
    } else {
        if (document.getElementById('destinationCity').checked) {
            path = "&nonRated="
                    + "yes" + "&radio=city";
        } else {
            path = "&nonRated="
                    + "yes" + "&radio=country";
            ;
        }
    }
    appendEncodeUrl(path);
}
function getAgentforDestinationonBlur() {
    var pod = document.QuotesForm.portofDischarge.value;
    if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
        var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getDefaultAgent",
                param1: podNew,
                param2: "true" === importFlag ? "I" : "F"
            },
            success: function (data) {
                populateAgentDojo1(data);
            }
        });
    }
}
function getOriginUrl() {
    var path = "";
    if (!document.getElementById('nonRated').checked) {
        if (document.getElementById('originCountry').checked) {
            path = "&destination="
                    + document.QuotesForm.portofDischarge.value + "&radio=city";
        } else {
            path = "&destination="
                    + document.QuotesForm.portofDischarge.value + "&radio=country";
        }
    } else {
        if (document.getElementById('originCountry').checked) {
            path = "&nonRated="
                    + "yes" + "&radio=city";
        } else {
            path = "&nonRated="
                    + "yes" + "&radio=country";
        }
    }
    appendEncodeUrl(path);
}
function typeOfMove(data) {
    document.QuotesForm.typeofMove.value = data;
}
function getTypeofMoveList(move) {
    if (document.getElementById("rampCheck").checked) {
        document.getElementById("inlandVal").innerHTML = "Intermodal Ramp";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getrampNvoMoveList",
                dataType: "json"
            },
            success: function(data) {
                jQuery("#" + move).empty();
                jQuery.each(data, function(index, item) {
                    jQuery("#" + move).append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                setNVOmove();
            }
        });
    }
    if (!document.getElementById("rampCheck").checked) {
        document.getElementById("inlandVal").innerHTML = "Inland";
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getNvoMoveList",
                dataType: "json"
            },
            success: function (data) {
                jQuery("#" + move).empty();
                jQuery.each(data, function (index, item) {
                    jQuery("#" + move).append("<option value='" + item.value + "'>" + item.label + "</option>");
                });
                setNVOmove();
            }
        });
    }
}
var remarksCityName;
function getAgentforPod(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        var pod = document.QuotesForm.finalDestination.value;
        if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
            var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
            jQuery.ajaxx({
                data: {
                    className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                    methodName: "getDefaultAgent",
                    param1: podNew,
                    param2: "true" === importFlag ? "I" : "F"
                },
                success: function (data) {
                    populateAgentDojo2(data);
                }
            });
        }
    }
    if (event.keyCode == 9 || event.keyCode == 13) {
        var city = document.QuotesForm.finalDestination.value;
        var index = city.indexOf('/');
        var cityName = city.substring(0, index);
        remarksCityName = cityName;
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.hibernate.dao.PortsDAO",
                methodName: "getSpecialRemarks",
                param1: cityName
            },
            success: function (data) {
                showSpecialRemarks1(data);
            }
        });
    }
}
function populateAgentDojo2(data) {
    if (data.accountName != undefined && data.accountName != "") {
        document.getElementById("agent").value = data.accountName;
    }
    if (data.accountNo != undefined && data.accountNo != "") {
        document.getElementById("agentNo").value = data.accountNo;
    }
}

function trim(stringToTrim) {
    return stringToTrim.replace(/^\s+|\s+$/g, "");
}
function routedAgentName() {
    if (document.QuotesForm.routedAgentCheck.value == "yes") {
        document.QuotesForm.routedbymsg.value = document.QuotesForm.agentNo.value;
    } else {
        document.QuotesForm.routedbymsg.value = "";
    }
}
function setDefaultRouteAgent(importFlag) {
    var importFlg = importFlag;
    var agentNo = document.QuotesForm.agentNo.value;
    if (document.QuotesForm.routedAgentCheck.value == "yes") {
        if (null != agentNo && agentNo != '') {
            document.QuotesForm.routedbymsg.value = document.QuotesForm.agentNo.value;
            tabMoveErt();
        } else {
            if (importFlg == false) {
                document.getElementById("routedAgentCheck").selectedIndex = 0;
                alertNew("You must first select an agent");
            }
        }
    } else {
        document.QuotesForm.routedbymsg.value = "";
        document.QuotesForm.routedbymsg.className = "textlabelsBoldForTextBox";
        document.QuotesForm.routedbymsg.readOnly = false;
        document.QuotesForm.routedbymsg.tabIndex = -1;
        tabMoveErt();
    }
}
function setFocusForssl() {
    document.getElementById('sslDescription').focus();
}
function getCustomer(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            } else {
                document.getElementById("contactButton").style.visibility = 'visible';
                val1 = val1.replace(":", "'");
                customerTemp = val1;
                document.QuotesForm.customerName.value = val3;
                document.QuotesForm.clientNumber.value = val2;
                clientNumberHidden = val2;
                document.QuotesForm.clienttype.value = val4;
                document.QuotesForm.contactName.value = "";
                document.QuotesForm.phone.value = val6;
                document.QuotesForm.fax.value = val7;
                document.QuotesForm.email.value = val8;
                setTimeout("set()", 1000);
                document.getElementById('portofDischarge').focus();
            }
        }
    });
}
function set() {
    document.getElementById('portofDischarge').focus();
    document.QuotesForm.customerName.value = customerTemp;
    document.getElementById('clientNumber').value = clientNumberHidden;
    document.getElementById('clientNumberHidden').value = clientNumberHidden;
}
function getIssuingTerminal(val1) {
    document.QuotesForm.issuingTerminal.value = val1;
    document.getElementById('agent').focus();
}
function getContactNamefromPopup(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11) {
    val1 = val1.replace(":", "'");
    document.QuotesForm.customerName.value = val1;
    document.QuotesForm.clientNumber.value = val2;
    document.QuotesForm.contactName.value = val4;
    if (val5 != "") {
        document.QuotesForm.phone.value = val5;
    }
    if (val6 != "") {
        document.QuotesForm.fax.value = val6;
    }
    if (val7 != "") {
        document.QuotesForm.email.value = val7;
    }
    setTimeout("setFocus1()", 150);
}
function setFocus1() {
    document.getElementById('move').focus();
}
function getCarrierContactNamefromPopup(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11) {
    val1 = val1.replace(":", "'");
    document.QuotesForm.sslDescription.value = val1;
    document.QuotesForm.sslcode.value = val2;
    document.QuotesForm.carrierContact.value = val4;
    if (val5 != "") {
        document.QuotesForm.carrierPhone.value = val5;
    }
    if (val6 != "") {
        document.QuotesForm.carrierFax.value = val6;
    }
    if (val7 != "") {
        document.QuotesForm.carrierEmail.value = val7;
    }
    setTimeout("setFC()", 150);
}
function setFC() {
    document.getElementById('commcode').focus();
}
function getCarrier(val1, val2, val3, val4, val5, val6, val7, val8) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            } else {
                val1 = val1.replace(":", "'");
                document.getElementById("contactNameButton").style.visibility = 'visible';
                document.QuotesForm.sslDescription.value = val1;
                document.QuotesForm.sslcode.value = val2;
                document.QuotesForm.carrierContact.value = "";
                document.QuotesForm.carrierPhone.value = val6;
                document.QuotesForm.carrierFax.value = val7;
                if (val8 !== undefined) {
                    document.QuotesForm.carrierEmail.value = val8;
                }
                setTimeout("setF()", 150);
            }
        }
    });
}
function setF() {
    document.getElementById('carrierContact').focus();
}
function selectedMenu(val1, val2, val3, val4, val5, val6) {
    var importflag = document.getElementById('fileType') && null !== document.getElementById('fileType').value && document.getElementById('fileType').value === 'I' ? true : false;
    document.QuotesForm.selectedCheck.value = val3;
    document.QuotesForm.unitselected.value = val2;
    document.QuotesForm.ssline.value = val1.replace(/&amp;/g, '&');
    document.QuotesForm.selectedOrigin.value = val4;
    document.QuotesForm.selectedDestination.value = val5;
    document.QuotesForm.selectedComCode.value = val6;
    document.QuotesForm.importFlag.value = importflag;
    document.QuotesForm.buttonValue.value = "newgetRates";
    document.QuotesForm.submit();
}
function refreshPage(val1) {
    checkbox();
    document.QuotesForm.quoteId.value = val1;
    document.QuotesForm.buttonValue.value = "addCharges";
    document.QuotesForm.submit();
}
function setDojoPathForAgent() {
    var path = "";
    var portOfDischarge = document.QuotesForm.finalDestination.value;
    var destination = document.QuotesForm.portofDischarge.value;
    path = "&portOfDischarge=" + portOfDischarge + "&destination=" + destination;
    appendEncodeUrl(path);
}
function getAgentInfo(val1, val2) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: val2
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
            } else {
                document.QuotesForm.agent.value = val1;
                document.QuotesForm.agentNo.value = val2;
                document.getElementById('routedbymsg').focus();
            }
        }
    });
}

function calculateInsurance(val1, val2) {
    document.QuotesForm.costofgoods.value = val1;
    document.QuotesForm.insuranceAmount.value = val2;
    document.QuotesForm.buttonValue.value = "insurance"
    document.QuotesForm.focusValue.value = "soc";
    document.QuotesForm.submit();
}
function getDestination() {
    if (document.QuotesForm.portofDischarge.value == "") {
        var pod = document.QuotesForm.finalDestination.value;
        var index = pod.indexOf("/");
        var podNew = pod.substring(0, index);
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getPortsForAgentInfo",
                param1: podNew
            },
            success: function (data) {
                populateAgentDojo(data);
            }
        });
    } else if (document.QuotesForm.finalDestination.value == "") {
        document.getElementById("agent").value = "";
        document.getElementById("agentNo").value = "";
    }
                }
function populateAgentDojo(data) {
    if (data == "empty" || document.QuotesForm.routedAgentCheck.value == "yes") {
        document.getElementById("agent").value = "";
        document.getElementById("agentNo").value = "";
        document.getElementById("routedbymsg").value = "";
    }
}
function getPod() {
    if (document.QuotesForm.finalDestination.value == "") {
        var pod = document.QuotesForm.portofDischarge.value;
        var index = pod.indexOf("/");
        var podNew = pod.substring(0, index);
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getPortsForAgentInfo",
                param1: podNew
            },
            success: function (data) {
                populateAgent1Dojo(data);
            }
        });
    }
}
function populateAgent1Dojo(type, data, evt) {
    if (data == "empty") {
        document.getElementById("agent").value = "";
        document.getElementById("agentNo").value = "";
        document.getElementById("routedbymsg").value = "";
        document.getElementById("agentlookup").style.visibility = 'hidden';
    }
}
function makeClientTypeBorderLess() {
    var element = document.getElementById("clienttype");
    element.style.border = 0;
    element.readOnly = true;
    element.className = "whitebackgrnd";
}
function getEmptySsline() {
    if (document.QuotesForm.sslDescription.value == "") {
        document.QuotesForm.sslcode.value = "";
        document.QuotesForm.carrierContact.value = "";
        document.QuotesForm.carrierPhone.value = "";
        document.QuotesForm.carrierFax.value = "";
        document.QuotesForm.carrierEmail.value = "";
        document.getElementById("contactNameButton").style.visibility = 'hidden';
    }
}
function deleteArCharges(val1) {
    checkbox();
    document.QuotesForm.buttonValue.value = "deleteCharge";
    document.QuotesForm.numbIdx.value = val1;
    confirmNew("Are you sure you want to delete this Charge", "deleteArCharges");
}
function goback() {
    document.getElementById("confirmNo").style.width = "88" + "px";
    document.getElementById("confirmNo").value = "Exit without Save";
    confirmNew("Do you want to save the Quote changes?", "goBack");

}
function yesFunction() {
    checkbox();
    if (document.QuotesForm.quotationDate.value == "") {
        alertNew("Please enter the Quotation Date");
        return;
    }

    if (document.QuotesForm.isTerminal.value == "") {
        alertNew("PLEASE SELECT ORIGIN");
        jQuery("#isTerminal").css("border-color", "red");
        return;

    }
    if (document.QuotesForm.commcode.value == "") {
        alertNew("PLEASE SELECT COMMODITY CODE");
        jQuery("#commcode").css("border-color", "red");
        return;

    }
    if (document.QuotesForm.portofDischarge.value == "") {
        alertNew("PLEASE SELECT DESTINATION PORT");
        jQuery("#portofDischarge").css("border-color", "red");
        return;
    }
    if (document.getElementById('newClient').checked) {
        if (document.QuotesForm.customerName1.value == "") {
            alertNew("PLEASE ENTER CLIENT");
            jQuery("#customerName").css("border-color", "red");
            return;
        }
    } else {
        if (document.QuotesForm.customerName.value == "") {
            alertNew("PLEASE ENTER CLIENT");
            jQuery("#customerName").css("border-color", "red");
            return;
        }
    }
    if (document.QuotesForm.issuingTerminal.value == "") {
        alertNew("PLEASE SELECT ISSUING TEMINAL");
        jQuery("#issuingTerminal").css("border-color", "red");
        return;
    }
    if (document.QuotesForm.goodsdesc.value == "") {
        alertNew("Please enter Goods Description");
        return;
    }
    if (document.QuotesForm.goodsdesc.value.length > 500) {
        alertNew("Please donot enter more than 500 characters");
        return;
    }
    if (document.QuotesForm.comment.value.length > 500) {
        alertNew("Please donot enter more than 500 characters");
        return;
    }
    if (document.QuotesForm.ratesNonRates[1].checked) {
        alertNew("Please Choose Non-Rated Option");
        return;
    }
    if (document.QuotesForm.isTerminal.value == document.QuotesForm.doorOrigin.value) {
        alertNew("Please change either Origin or Door Origin, both cannot be same");
        return;
    }
    document.getElementById("save").disabled = true;
    document.QuotesForm.buttonValue.value = "confirmSave";
    document.QuotesForm.submit();
}
function noFunction() {
    document.QuotesForm.buttonValue.value = "goBack";
    document.QuotesForm.submit();
}
function cancelFunction() {
    return;
}
function goBackCall() {

    if (document.QuotesForm.buttonValue.value == 'newgetRates' || document.QuotesForm.buttonValue.value == 'hazmat'
            || document.QuotesForm.buttonValue.value == 'addCharges') {
        goback();
    } else {
        document.QuotesForm.buttonValue.value = "goBack";
        document.QuotesForm.submit();
    }
}
function getAccountName(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        checkbox();
        document.QuotesForm.buttonValue.value = "recalc";
        document.QuotesForm.focusValue.value = "accountNumber";
        document.QuotesForm.submit();
    }
}
function addToBl() {
    document.QuotesForm.buttonValue.value = "addLocalDrayageToBl";
    document.QuotesForm.focusValue.value = "intermodel";
    document.QuotesForm.submit();
}
function addIntermodelToBl() {
    document.QuotesForm.buttonValue.value = "addIntermodelToBl";
    document.QuotesForm.focusValue.value = "insurance";
    document.QuotesForm.submit();
}
function addInsuranceToBl() {
    document.QuotesForm.buttonValue.value = "addInsuranceToBl";
    document.QuotesForm.submit();
}
function onBlurForSSL() {
    document.getElementById('sslDescription').value = "";
    document.getElementById('sslcode').value = "";
    document.QuotesForm.carrierPhone.value = "";
    document.QuotesForm.carrierFax.value = "";
    document.getElementById('carrierContact').value = "";
    document.getElementById('carrierEmail').value = "";
    document.getElementById("contactNameButton").style.visibility = 'hidden';
}

function onCustomerBlur(clear) {
    if (clear == 'client') {
        document.getElementById('customerName1').value = document.getElementById('customerName').value;
    }
    document.getElementById('customerName').value = "";
    document.getElementById('clientNumber').value = "";
    document.getElementById('clienttype').value = "";
    document.getElementById('contactName').value = "";
    document.getElementById('email').value = "";
    document.getElementById('phone').value = "";
    document.getElementById('fax').value = "";
    document.getElementById("contactButton").style.visibility = 'hidden';
    jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
    getCreditStatus();
}
function inlandVal() {
    if (document.getElementById('rampCheck').checked) {
        document.getElementById("inlandVal").innerHTML = "Intermodal Ramp";
    } else {
        var importFlag = document.getElementById("importFlag").value;
        if (importFlag === 'true') {
            document.getElementById("inlandVal").innerHTML = "Delivery";
        } else {
            document.getElementById("inlandVal").innerHTML = "Inland";
        }
    }
}
function newDestinationEQ() {
    var path = "";
    if (document.getElementById('newDestination').checked) {
        document.getElementById('doorDestination').value = "";
        path = "&from=10&isDojo=false&checkDoor=true";
        Event.stopObserving("doorDestination", "blur");
    } else {
        document.getElementById('doorDestination').value = "";
        path = "&from=10&isDojo=false&checkDoor=false";
        Event.observe("doorDestination", "blur", function (event) {
            var element = Event.element(event);
            if (element.value != $("doorDestination_check").value) {
                element.value = '';
                $("doorDestination_check").value = '';
            }
        }
        );
    }
    appendEncodeUrl(path);
}
function enableAgentLookUp() {
    if (document.QuotesForm.defaultAgent[0].checked) {
        document.getElementById("agentlookup").style.visibility = 'hidden';
    } else {
        document.getElementById("agentlookup").style.visibility = 'visible';
    }
}
function getFFCommission() {
    if (document.QuotesForm.portofDischarge.value == '') {
        alertNew("Please enter Destination");
        document.getElementById('n5').checked = true;
        return;
    }
    confirmNew("Commission Amount will be subtracted. Are you sure?", "getFFCommission");
}
function deleteFFCommission() {
    confirmNew("Commission Amount will be deleted. Are you sure?", "deleteFFCommission");

}
function setZipCode(val) {
    document.QuotesForm.zip.value = val;
    document.getElementById('noOfDays').focus();
    document.getElementById('typeofMove').style.visibility = 'hidden';
}
var stat;
var button;
function setFocus(ev1, ev2) {
    stat = ev1;
    button = ev2;
    setTimeout("setFocuss()", 800);
}
function setFocuss() {
    if (stat == "") {
        if (button == 'assignRemarks') {
            document.getElementById("isTerminal").focus();
            document.getElementById("isTerminal").select();
        }
    } else if (document.getElementById(stat) != null) {
        document.getElementById(stat).focus();
        document.getElementById(stat).select();
    }
}
function nextTab() {
    document.getElementById('HazmatButton').focus();
}
function nextTab1() {
    if (event.keyCode == 9 || event.keyCode == 13) {
        document.getElementById('insuranceCharge').focus();
    }
}
function getUppercase(val) {
    var text;
    text = val;
    if (document.getElementById('newClient').checked) {
        document.getElementById('customerName1').value = text.toUpperCase();
    } else {
        document.getElementById('customerName').value = text.toUpperCase();
    }
}
function focusSetting(isContact) {
    if (document.getElementById('newClient').checked) {
        setFocusByElementId('customerName1');
    } else {
        testing(isContact);
    }
}
var temp1 = "", temp2 = "";
function testing(isContact) {
    document.getElementById('clienttype').value = "";
    document.getElementById('phone').value = "";
    document.getElementById('fax').value = "";
    if (!isContact) {
        document.getElementById('contactName').value = "";
        document.getElementById('email').value = "";
    }
    var account = document.getElementById('customerName').value;
    temp1 = document.getElementById('customerName').value;
    var accountNumber = document.getElementById('clientNumber').value;
    temp2 = document.getElementById('clientNumber').value;
    var importFlag =document.getElementById("importFlag").value;
    
    if (importFlag === 'false') {
        addBrandvalueForanAccount(temp2);
    }
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: accountNumber
        },
        success: function(data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.getElementById('clienttype').value = "";
                document.getElementById('contactName').value = "";
                document.getElementById('phone').value = "";
                document.getElementById('fax').value = "";
                document.getElementById('email').value = "";
                document.getElementById('customerName').value = "";
                document.getElementById('clientNumber').value = "";
            } else {
                jQuery.ajaxx({
                    dataType : "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getClientDetails",
                        param1: account,
                        param2: accountNumber,
                        dataType : "json"
                    },
                    success: function(result) {
                        populateClientDetails(result, isContact);
                    }
                });
            }
        }
    });
}

function populateClientDetails(data, isContact) {
    if (data != null && data.acctType != null && data.subType != null && (data.acctType == 'V' || data.acctType == 'V,SS' || data.acctType == 'V,O' || data.acctType == 'O,V') && ((data.subType).toLowerCase()) != 'forwarder') {
        alertNew("Please select the Customers  with Account Type S,C,O and V with Sub Type Forwarder");
        document.getElementById('customerName').value = "";
        document.getElementById('clienttype').value = "";
        document.getElementById('phone').value = "";
        document.getElementById('fax').value = "";
        document.getElementById('email').value = "";
        document.getElementById("clientNumber").value = "";
        jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
        document.getElementById("customerName").focus();
    } else if (data != null && data.acctNo != null && data.acctNo != ""){
        document.getElementById("contactNameButton").style.visibility = 'visible';
        document.getElementById('customerName').value = data.acctName;
        temp1 = data.acctName;
        document.getElementById("clientNumber").value = data.acctNo;
        temp2 = data.acctNo;
        if (data.clientTypeForDwr != undefined && data.clientTypeForDwr != null) {
            document.getElementById('clienttype').value = data.clientTypeForDwr;
        } else {
            document.getElementById('clienttype').value = "";
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "isCommodityChangeApplyForThisCustomer",
                param1: data.acctNo
            },
            success: function(data) {
                if (null!==data && jQuery.trim(data) !== "") {
                    var codIndx = data.indexOf(',');
                    var comCod = data.substring(0, codIndx);
                    var comCodDesc = data.substring(codIndx+1);
                    document.QuotesForm.commcode_check.value = comCod;
                    document.QuotesForm.commcode.value = comCod;
                    document.QuotesForm.description.value = comCodDesc;
                } else {
                    var importFlag =document.getElementById("importFlag").value;
                    if (importFlag == 'true') {
                        document.QuotesForm.commcode_check.value = '006205';
                        document.QuotesForm.commcode.value = '006205';
                    } else {
                        document.QuotesForm.commcode_check.value = '006100';
                        document.QuotesForm.commcode.value = '006100';
                    }
                }
            }
        });
        if (!isContact) {
            document.QuotesForm.contactName.value = "";
        }
        if (data.phone != undefined && data.phone != null) {
            document.getElementById('phone').value = data.phone;
        } else {
            document.getElementById('phone').value = "";
        }
        if (data.fax != undefined && data.fax != null) {
            document.getElementById('fax').value = data.fax;
        } else {
            document.getElementById('fax').value = "";
        }
        if (!isContact) {
            if (data.email1 != undefined && data.email1 != null) {
                document.getElementById('email').value = data.email1;
            } else {
                document.getElementById('email').value = "";
            }
        }
        isCustomerNotes(data.acctNo);
        document.getElementById('contactButton').style.visibility = "visible";
        if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
            document.getElementById('isTerminal').focus();
        } else {
            document.getElementById('portofDischarge').focus();
        }
        if (document.getElementById('fileTypeS')) {
            jQuery.ajaxx({
                dataType : "json",
                data: {
                    className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                    methodName: "getDefaultDetails",
                    param1: data.acctNo,
                    dataType : "json"
                },
                success: function(result) {
                    if(null !== result){
                        fillDefaultCustomerData();
                    }else{
                        jQuery.ajaxx({
                            dataType : "json",
                            data: {
                                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                                methodName: "getDefaultDetailsAlert",
                                param1: data.acctNo,
                                dataType : "json"
                            },
                            success: function(data) {
                                if(jQuery.trim(data) !== ""){
                                    alertNewForDefaultValue(data.replace(/\n/g, "<br/>"));
                                }
                            }
                        });
                        setTimeout("setI()", 20);
                    }
                }
            });
        } else {
            setTimeout("setI()", 20);
        }
    }
}
function fillDefaultCustomerValues() {
    document.getElementById('portofDischarge').value = defaultCustomerData.destination;
    document.getElementById('portofDischarge_check').value = defaultCustomerData.destination;
    document.getElementById('isTerminal').value = defaultCustomerData.origin;
    document.getElementById('isTerminal_check').value = defaultCustomerData.origin;
    if (null !== defaultCustomerData.commodityCode && defaultCustomerData.commodityCode !== '') {
        document.getElementById('commcode').value = defaultCustomerData.commodityCode;
        document.getElementById('commcode_check').value = defaultCustomerData.commodityCode;
    }
    if (defaultCustomerData.issuingTerminal !== '' && null !== defaultCustomerData.issuingTerminal) {
        document.getElementById('issuingTerminal_check').value = defaultCustomerData.issuingTerminal;
        document.getElementById('issuingTerminal').value = defaultCustomerData.issuingTerminal;
    }

    document.getElementById('placeofReceipt_check').value = defaultCustomerData.pol;
    document.getElementById('finalDestination_check').value = defaultCustomerData.pod;
    document.getElementById('move').value = defaultCustomerData.nvoMove;
    document.getElementById('zip').value = defaultCustomerData.originZip;
    document.getElementById('zip_check').value = defaultCustomerData.originZip;
    document.getElementById('doorOrigin').value = defaultCustomerData.doorOrigin;
    document.getElementById('doorOrigin_check').value = defaultCustomerData.doorOrigin;
    document.QuotesForm.goodsdesc.value = defaultCustomerData.goodsDesc;
    document.QuotesForm.docChargeAmount.value = defaultCustomerData.documentAmount;
    if (document.getElementById("nonRated").checked) {
        document.getElementById('placeofReceipt').value = defaultCustomerData.origin;
        document.getElementById('finalDestination').value = defaultCustomerData.destination;
    } else {
        document.getElementById('placeofReceipt').value = defaultCustomerData.pol;
        document.getElementById('finalDestination').value = defaultCustomerData.pod;
    }
    if (defaultCustomerData.ffComm == 'Y') {
        document.QuotesForm.deductFFcomm[0].checked = true;
    } else {
        document.QuotesForm.deductFFcomm[1].checked = true;
    }
    if (defaultCustomerData.documentCharge == 'Y') {
        document.QuotesForm.docCharge[0].checked = true;
    } else {
        document.QuotesForm.docCharge[1].checked = true;
    }
    document.getElementById('routedAgentCheck').value = defaultCustomerData.ert;
    if (null != defaultCustomerData.destination && defaultCustomerData.destination != '') {
        defaultFromCustgeneral(defaultCustomerData.ert);
    } else {
        document.getElementById('routedbymsg').value = "";
        document.getElementById('routedbymsg_check').value = "";
        document.getElementById('agent').value = "";
        document.getElementById('agent_check').value = "";
        document.getElementById('agentNo').value = "";
        if (document.getElementById("nonRated").checked) {
            document.QuotesForm.buttonValue.value = "applyDefaultValues";
            document.QuotesForm.submit();
        }
    }
}

function onclickAlertOk() {
    document.getElementById('AlertBoxDefaultValues').style.display = 'none';
    grayOut(false, '');
    fillDefaultCustomerValues();
}
var defaultCustomerData;
function fillDefaultCustomerData() {
    defaultCustomerData = null;
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "getDefaultDetails",
            param1: document.getElementById("clientNumber").value,
            dataType: "json"
        },
        success: function (data) {
            if (null !== data) {
                defaultCustomerData = data;
                if (defaultCustomerData.importantNotes !== "" && defaultCustomerData.importantNotes !== undefined && defaultCustomerData.importantNotes !== null) {
                    alertNewForDefaultValue(defaultCustomerData.importantNotes.replace(/\n/g, "<br/>"));
                } else {
                    fillDefaultCustomerValues();
                }
            }
        }
    });
}
function changePolPodForNonRated() {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "getDefaultDetails",
            param1: document.getElementById("clientNumber").value,
            dataType: "json"
        },
        success: function (data) {
            if (null !== data) {
                if (document.getElementById("nonRated").checked) {
                    document.getElementById('placeofReceipt').value = data.origin;
                    document.getElementById('finalDestination').value = data.destination;
                } else {
                    document.getElementById('placeofReceipt').value = data.pol;
                    document.getElementById('finalDestination').value = data.pod;
                }
            }
        }
    });
}
function tabMoveAfterDeleteRates(importflag) {
    if (document.getElementById("customerName").value != '' && document.getElementById("customerName1").value != '') {
        if (importflag == 'true') {
            document.getElementById('isTerminal').focus();
        } else {
            document.getElementById('portofDischarge').focus();
        }
    } else {
        if (document.getElementById("newClient").checked && document.getElementById("customerName1").value == '') {
            document.getElementById("customerName1").focus();
        } else {
            document.getElementById("customerName").focus();
        }
    }
}
function setI() {
    document.getElementById('customerName').value = temp1;
    document.getElementById("clientNumber").value = temp2;
    document.getElementById('clientNumberHidden').value = temp2;
    getCreditStatus();
}
//    function setFocussing() {
//    if (document.getElementById('fileType') && document.getElementById('fileType').value == 'I') {
//        document.getElementById('isTerminal').focus();
//    } else {
//        document.getElementById('portofDischarge').focus();
//    }
//}
function getContactNameAndEmailfromPopup(val1, val2, val3, val4) {
    document.QuotesForm.contactName.value = val1;
    document.QuotesForm.email.value = val2;
    if (val3 != "" && val3 != undefined && val3 != null) {
        document.QuotesForm.phone.value = val3;
    }
    if (val4 != "" && val4 != undefined && val4 != null) {
        document.QuotesForm.fax.value = val4;
    }
}
function assignRemarksValue(ev) {
    document.QuotesForm.remarks.value = ev;
    document.QuotesForm.buttonValue.value = "assignRemarks";
    document.QuotesForm.submit();
}
function getRemark(ev) {
    if (event.keyCode == 13 || event.keyCode == 9 || event.keyCode == 0) {
        var ind = ev.indexOf(":");
        if (ind != -1) {
            var newCode = ev.substring(0, ind);
            var newDesc = ev.substring(ind + 1, ev.length);
            document.getElementById('commentTemp').value = newCode;
            document.getElementById('commentRemark').value = newDesc;
        }
    }
}
function getRatesNonRatesY() {
    document.getElementById("getRates").style.visibility = "visible";
    if (document.getElementById("bulletRates")) {
        document.getElementById("bulletRates").style.visibility = "visible";
    }
    document.getElementById("breakBulk").style.visibility = "hidden";
}
function getRatesNonRatesYimport() {
    document.getElementById("getRates").style.visibility = "visible";
    if (document.getElementById("bulletRates")) {
        document.getElementById("bulletRates").style.visibility = "visible";
    }
    document.getElementById("breakBulk").style.visibility = "hidden";
    copyValPol();
    copyValPod();
}
function getRatesNonRatesN() {
    document.getElementById("getRates").style.visibility = "hidden";
    if (document.getElementById("bulletRates")) {
        document.getElementById("bulletRates").style.visibility = "hidden";
    }
    document.getElementById("breakBulk").style.visibility = "visible";
}
function getRatesNonRatesNimport() {
    document.getElementById("getRates").style.visibility = "hidden";
    if (document.getElementById("bulletRates")) {
        document.getElementById("bulletRates").style.visibility = "hidden";
    }
    document.getElementById("breakBulk").style.visibility = "visible";
    copyValPol();
    copyValPod();
}

function hideOrgZip() {
    document.getElementById("originZip").value = "";
}
function getTypeOfMove() {
    if (event.keyCode == 13 || event.keyCode == 9 || event.keyCode == 0) {

        var length = document.getElementById('move').length;
        for (var i = 0; i < length; i++) {
            if (document.getElementById('move').options[i].text == 'DOOR TO PORT') {
                document.getElementById('move').selectedIndex = i;
            }
        }
    } else {
        var path = "";
        if (document.getElementById('newOrigin').checked) {
            path = "&from=5&isDojo=false&check=false";
        } else {
            path = "&from=5&isDojo=false&check=true";
        }
        appendEncodeUrl(path);
    }
}
function getTypeOfMove1() {
    var path = "";
    if (document.QuotesForm.zip.value != "" && !document.getElementById('rampCheck').checked) {
        var length = document.getElementById('move').length;
        for (var i = 0; i < length; i++) {
            if (document.getElementById('move').options[i].text == 'DOOR TO DOOR') {
                document.getElementById('move').selectedIndex = i;
            }
        }

    } else if (document.QuotesForm.zip.value == "") {
        length = document.getElementById('move').length;
        for (i = 0; i < length; i++) {
            if (document.getElementById('move').options[i].text == 'PORT TO DOOR') {
                document.getElementById('move').selectedIndex = i;
            }
        }

    }
    if (document.getElementById('newDestination').checked) {
        path = "&checkDoor=true";
    } else {
        path = "&checkDoor=false";
    }
    appendEncodeUrl(path);
}
function getAllRemarksFromPopUp(val) {
    var oldarray = document.QuotesForm.comment.value;
    var splittedArray;
    if (oldarray.length == 0) {
        splittedArray = oldarray;
    } else {
        splittedArray = oldarray.split("\n");
    }
    var newarray = val.split(">>");
    var flag = false;
    for (var k = 0; k < newarray.length; k++) {
        flag = false;
        for (var l = 0; l < splittedArray.length; l++) {
            if (newarray[k].replace(/^[\s]+/, '').replace(/[\s]+$/, '').replace(/[\s]{2,}/, ' ') ==
                    splittedArray[l].replace(/^[\s]+/, '').replace(/[\s]+$/, '').replace(/[\s]{2,}/, ' ')) {
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
    document.QuotesForm.comment.value = oldarray;
}
function getSSlineAcctNo(ev) {
    if (event.keyCode == 9 || event.keyCode == 13) {
        jQuery.ajaxx({
            dataType: "json",
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "getSSlineAcctNo",
                param1: ev,
                dataType: "json"
            },
            success: function (data) {
                populateSSlineAcctNo(data);
            }
        });
    }
}
function populateSSlineAcctNo(data) {
    document.QuotesForm.carrierContact.value = "";
    document.getElementById("contactNameButton").style.visibility = 'visible';
    document.QuotesForm.sslcode.value = data.acctNo != null ? data.acctNo : '';
    document.QuotesForm.carrierPhone.value = data.phone != null ? data.phone : '';
    document.QuotesForm.carrierFax.value = data.fax != null ? data.fax : '';
    document.QuotesForm.carrierEmail.value = data.email1 != null ? data.email1 : '';
    setTimeout("setF()", 150);
}
function confirmMessageFunction(id1, id2) {
    if (id1 == 'localDrayage' && id2 == 'ok') {
        document.QuotesForm.buttonValue.value = "deleteLocalDrayageToBl";
        document.QuotesForm.submit();
    } else if (id1 == 'intermodal' && id2 == 'ok') {
        document.QuotesForm.buttonValue.value = "deleteIntermodelToBl";
        document.QuotesForm.submit();
    } else if (id1 == 'inland' && id2 == 'ok') {
        document.QuotesForm.buttonValue.value = "deleteInlandToBl";
        document.QuotesForm.submit();
    } else if (id1 == 'localDrayage' && id2 == 'no') {
        showPopUp();
        document.getElementById("localDrayageCommentDiv").style.display = 'block';
        document.getElementById("commentType").value = "localdrayage";
    } else if (id1 == 'intermodel' && id2 == 'no') {
        showPopUp();
        document.getElementById("localDrayageCommentDiv").style.display = 'block';
        document.getElementById("commentType").value = "intermodal";
    } else if (id1 == 'intermodel' && id2 == 'yes') {
        document.QuotesForm.intermodel[0].checked = false;
        document.QuotesForm.intermodel[1].checked = true;
    } else if (id1 == 'deleteArCharges' && id2 == 'ok') {
        document.QuotesForm.submit();
    } else if (id1 == 'getHazmatForRatesN' && id2 == 'ok') {
        document.QuotesForm.buttonValue.value = "hazmat";
        document.QuotesForm.submit();
    } else if (id1 == 'getHazmatForRatesN' && id2 == 'cancel') {
        document.QuotesForm.hazmat[0].checked = true;
    } else if (id1 == 'getHazmatForRatesY' && id2 == 'ok') {
        checkbox();
        document.QuotesForm.buttonValue.value = "hazmat";
        document.QuotesForm.focusValue.value = "HazmatButton";
        document.QuotesForm.submit();
    } else if (id1 == 'getHazmatForRatesY' && id2 == 'cancel') {
        document.QuotesForm.hazmat[1].checked = true;
    } else if (id1 == 'getFFCommission' && id2 == 'ok') {
        document.QuotesForm.buttonValue.value = "FFCommssion";
        document.QuotesForm.submit();
    } else if (id1 == 'deleteFFCommission' && id2 == 'ok') {
        document.QuotesForm.buttonValue.value = "deleteFFCommssion";
        document.QuotesForm.submit();
    } else if (id1 == 'getFFCommission' && id2 == 'cancel') {
        document.getElementById('n5').checked = true;
    } else if (id1 == 'deleteFFCommission' && id2 == 'cancel') {
        document.getElementById('y5').checked = true;
    } else if (id1 == 'goBack' && id2 == 'ok') {
        yesFunction();
    } else if (id1 == 'goBack' && id2 == 'cancel') {
        noFunction();
    } else if (id1 == 'applyDefaultValues' && id2 == 'ok') {
        fillDefaultCustomerData();
    }  

}

function focusSettingForSSl() {
    if (event.keyCode == 9 || event.keyCode == 13 || event.keyCode == 0) {
        setTimeout("testingForSSl()", 200);
    }
}

var temp3 = "", temp4 = "";
function testingForSSl() {
    document.QuotesForm.carrierContact.value = "";
    document.QuotesForm.carrierPhone.value = "";
    document.QuotesForm.carrierFax.value = "";
    document.QuotesForm.carrierEmail.value = "";
    var account = document.getElementById('sslDescription').value;
    temp3 = document.getElementById('sslDescription').value;
    var accountNumber = document.getElementById('sslcode').value;
    temp4 = document.getElementById('sslcode').value;
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: accountNumber
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.QuotesForm.carrierContact.value = "";
                document.QuotesForm.carrierPhone.value = "";
                document.QuotesForm.carrierFax.value = "";
                document.QuotesForm.carrierEmail.value = "";
                document.getElementById('sslDescription').value = "";
                document.getElementById('sslcode').value = "";
            } else {
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getClientDetails",
                        param1: account,
                        param2: accountNumber,
                        dataType: "json"
                    },
                    success: function (data) {
                        populateClientDetails1(data);
                    }
                });
            }
        }
    });
}
function populateClientDetails1(data) {
    if (document.QuotesForm.ratesNonRates[0].checked) {
        if (null != data && (null == data.sslineNumber || data.sslineNumber == '' || data.sslineNumber == '00000')) {
            alertNew("Please select Steamship Line with 5 digit Ssline Number");
            document.QuotesForm.carrierContact.value = "";
            document.QuotesForm.carrierPhone.value = "";
            document.QuotesForm.carrierFax.value = "";
            document.QuotesForm.carrierEmail.value = "";
            document.getElementById('sslDescription').value = "";
            document.getElementById('sslcode').value = "";
            return;
        }
    }
    document.getElementById("contactNameButton").style.visibility = 'visible';
    document.QuotesForm.sslDescription.value = data.acctName;
    temp3 = data.acctName;
    document.QuotesForm.sslcode.value = data.acctNo;
    temp4 = data.acctNo;

    document.QuotesForm.carrierContact.value = "";
    if (data.phone != undefined && data.phone != null) {
        document.QuotesForm.carrierPhone.value = data.phone;
    } else {
        document.QuotesForm.carrierPhone.value = "";
    }
    if (data.fax != undefined && data.fax != null) {
        document.QuotesForm.carrierFax.value = data.fax;
    } else {
        document.QuotesForm.carrierFax.value = "";
    }
    if (data.email1 != undefined && data.email1 != null) {
        document.QuotesForm.carrierEmail.value = data.email1;
    } else {
        document.QuotesForm.carrierEmail.value = "";
    }
    document.getElementById('carrierContact').focus();
    setTimeout("set1()", 20);
}
function set1() {
    document.QuotesForm.sslDescription.value = temp3;
    document.QuotesForm.sslcode.value = temp4;
}
function checkEmail() {
    if (document.QuotesForm.email.value == "") {
        document.QuotesForm.ccEmail.checked = false;
        alertNew("Please Enter Email Id");
        return;
    }
}
function setNVOmove() {
    var length = 0;
    var i = 0;
    if (document.getElementById("zip").value != "" && document.getElementById("rampCheck").checked) {
        length = document.getElementById("move").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("move").options[i].text == "RAMP TO PORT") {
                document.getElementById("move").selectedIndex = i;
            }
        }
    } else if (document.getElementById("doorDestination").value == "" && document.getElementById("zip").value == "") {
        length = document.getElementById("typeofMoveSelect").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("typeofMoveSelect").options[i].text == "PORT TO PORT") {
                document.getElementById("typeofMoveSelect").selectedIndex = i;
            }
        }

    } else if (document.getElementById("doorDestination").value != "" && document.getElementById("zip").value != "") {
        length = document.getElementById("move").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("move").options[i].text == "DOOR TO DOOR") {
                document.getElementById("move").selectedIndex = i;
            }
        }

    } else if (document.getElementById("doorDestination").value == "") {
        length = document.getElementById("move").length;
        for (i = 0; i < length; i++) {
            if (document.getElementById("move").options[i].text == "DOOR TO PORT") {
                document.getElementById("move").selectedIndex = i;
            }
        }

    }

}
function checkDisable() {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: document.getElementById('routedNo').value
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.getElementById('routedbymsg').value = "";
            } else {
                document.getElementById('routedbymsg').value = document.getElementById('routedNo').value;
            }
        }
    });
}
function checkDisableForAgent(focusTo) {
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkForDisable",
            param1: document.getElementById('agentNo').value
        },
        success: function (data) {
            if (jQuery.trim(data) !== "") {
                alertNew(data);
                document.getElementById('agentNo').value = "";
                document.getElementById('agent').value = "";
            }
        }
    });
    setFocusFromDojo(focusTo);
}
function setFocusFromDojo(focusTo) {
    if (document.getElementById(focusTo)) {
        document.getElementById(focusTo).focus();
    }
}
function setFocusFromorigin() {
    if (document.getElementById('getRates')) {
        document.getElementById('getRates').focus();
    }
}
function hiderampCheckbox() {
    document.getElementById("rampCheck").checked = false;
    document.getElementById("inlandVal").innerHTML = "Inland";
    document.getElementById("rampCheck").style.visibility = "hidden";
}
function updateDoorOrigin(elementId) {
    setValue('doorOrigin_check', getValue(elementId));
    document.getElementById("rampCheck").style.visibility = "visible";
    document.getElementById("doorDestination").focus();
}
function quoteOptions() {
    showPopUp();
    if (document.getElementById("nonRated").checked == true) {
        document.getElementById("printGRIPortRemarks").style.visibility = "visible";
    } else {
        document.getElementById("printGRIPortRemarks").style.visibility = "hidden";
    }
    document.getElementById("QuoteOPtions").style.display = "block";
    floatDiv2("QuoteOPtions", document.body.offsetWidth / 3, document.body.offsetHeight / 3).floatIt();
}
function submitQuoteOPtions() {
    closePopUp();
    document.getElementById("QuoteOPtions").style.display = "none";
}
function carrierOPition(obj) {
    document.QuotesForm.carrierPrint.value = obj.value;
}
function disclosureOPition(obj) {
    document.QuotesForm.importantDisclosures.value = obj.value;
}
function docsInquiriesOPition(obj) {
    document.QuotesForm.docsInquiries.value = obj.value;
}
function printPortRemarksOPition(obj) {
    document.QuotesForm.printPortRemarks.value = obj.value;
}
function closeDivs() {
    closePopUp();
    document.getElementById("QuoteOPtions").style.display = "none";
}
function bulletRatesClick() {
    document.getElementById("commcode").value = "";
    document.getElementById("commcode").focus();
}
function bulletRatesStauts() {
    var path = "";
    if (document.getElementById("bulletRates") && document.getElementById("bulletRates").checked) {
        path = "&bulletRates=true";
    } else {
        path = "&bulletRates=false";
    }
    appendEncodeUrl(path);
}
function getHazmat() {
    alertNew("Please Select Rates before selecting Hazmat");
    document.QuotesForm.hazmat[1].checked = true;
    return;
}
function deleteHazmat() {
    confirmNew("This option will delete any Hazardous descriptions you might have entered. Are you sure?",
            "getHazmatForRatesN");
}
function openBlueScreenNotesInfo(customerNo, customerName) {
    GB_show("Client Notes", rootPath + "/bluescreenCustomerNotes.do?methodName=displayNotes&customerNo=" + customerNo + "&customerName=" + customerName, 400, 1000);
}
function isCustomerNotes(acctNo) {
    jQuery.ajaxx({
        dataType: "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "checkCustomerNotes",
            param1: acctNo,
            dataType: "json"
        },
        success: function (data) {
            if (data) {
                jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view1.gif");
            } else {
                jQuery('#clientIcon').attr("src", rootPath + "/img/icons/e_contents_view.gif");
            }
        }
    });
}
function deleteHazmat() {
    confirmNew("This option will delete any Hazardous descriptions you might have entered. Are you sure?",
            "getHazmatForRatesN");
    document.getElementById("commentRemark").value = "";
}

function brandValueLogic(){
  var pod = document.QuotesForm.portofDischarge.value;
      
        if (pod.lastIndexOf("(") > -1 && pod.lastIndexOf(")") > -1) {
            var podNew = pod.substring(pod.lastIndexOf("(") + 1, pod.lastIndexOf(")"));
            if (importFlag === false) {
                jQuery.ajaxx({
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "checkBrandForDestination",
                        param1: podNew
                    },
                    success: function (data) {
                         
                        if (data === "Ecu Worldwide") {
                            document.getElementById('brandEcuworldwide').checked = true;
                            document.getElementById('brandEcono').checked = false;
                        } else if (data === "OTI") {
                            document.getElementById('brandOti').checked = true;
                            document.getElementById('brandEcuworldwide').checked = false;
                        }
                        else if (data === "Econocaribe") {
                            document.getElementById('brandEcono').checked = true;
                            document.getElementById('brandEcuworldwide').checked = false;
                        }
                    }
                });

            }
          
}
}
    function setbrandvalueBasedONDestination(companyCode) {
    var clientname = document.getElementById('clientNumber').value;
   
    if (undefined !== clientname && null !== clientname && '' !== clientname) {
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "checkBrandForClient",
                param1: clientname
            },
            success: function (data) {
                
                if (data === "None") {
                    brandValueLogic();
                }
            }
        });

    } else if (undefined !== document.getElementById('portofDischarge') && null !== document.getElementById('portofDischarge')
            && '' !== document.getElementById('portofDischarge').value) {
       
        brandValueLogic();
    }
    }

function validateBrandFields(data) {
  
    if (data === "Ecu Worldwide") {
        
        document.getElementById('brandEcuworldwide').checked = true;
        document.getElementById('brandEcono').checked = false;
    } else if (data === "Econo") {
        document.getElementById('brandEcono').checked = true;
        document.getElementById('brandEcuworldwide').checked = false;
    }else if(data === "OTI"){
       document.getElementById('brandOti').checked = true;
        document.getElementById('brandEcuworldwide').checked = false; 
    } 
    else if (data === "None") {
       var companyCode= document.getElementById('companyCode').value;
        setbrandvalueBasedONDestination(companyCode);
    }
}
function addBrandvalueForanAccount(acctno) {
  
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                methodName: "checkBrandForClient",
                param1: acctno
            },
            success: function (data) {
             validateBrandFields(data);
            }
        });

}

