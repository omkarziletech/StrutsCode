jQuery(document).ready(function () {
    jQuery("[title != '']").not("link").tooltip();
    if (jQuery('#saveFlag').val() !== '') {
        parent.parent.GB_hide();
    }
    disableDeliveryToDojo();
});
function disableDeliveryToDojo() {
    if (jQuery('#manualDeliveryTo').is(":checked")) {
        jQuery('#newDeliveryTo').show();
        jQuery('#existingDoorDeliveryTo').hide();
        clearDeliveryFields();
    } else {
        jQuery('#newDeliveryTo').hide();
        jQuery('#existingDoorDeliveryTo').show();
        jQuery('#manualDeliveryTo').val("");
    }
}
function onDeliveryToBlur() {
    clearDeliveryFields();
    hideDeliveryTo();
}
function checkDisableAcctForDelivery() {
    var accountNumber = jQuery("#deliveryToNumber").val();
    var account = jQuery("#deliveryTo").val();
    jQuery.ajaxx({
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.FclBlBC",
            methodName: "checkForDisable",
            param1: accountNumber
        },
        success: function (data) {
            if (data != "") {
                jQuery.prompt(data);
                clearDeliveryFields();
            }else{
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getClientDetails",
                        param1: account,
                        param2: accountNumber,
                        dataType: "json"
                    },
                    success: function(result) {
                        populateDeliveryDetails(result);
                    }
                });
            }
        }
    });
}
function submitForm() {
    var deliveryContactName = jQuery("#deliveryContactName").val();
    var deliveryPhone = jQuery("#deliveryPhone").val();
    if (document.getElementById('manualDeliveryTo').checked) {
        jQuery("#manualDeliveryTo").val("on");
    }
    if (deliveryContactName == null || deliveryContactName == "") {
        jQuery.prompt("Contact Name is Required")
        jQuery("#deliveryContactName").css("border-color", "red");
        jQuery("#deliveryContactName").show();
        return false;
    }
    if (deliveryPhone == null || deliveryPhone == "") {
        jQuery.prompt("Phone Number is Required")
        jQuery("#deliveryPhone").css("border-color", "red");
        jQuery("#deliveryPhone").show();
        return false;
    }
    showLoading();
    document.fclDoorDeliveryInfoform.methodName.value = "save";
    document.fclDoorDeliveryInfoform.submit();
}
function validateDate(val, data) {
    if (data.value != "") {
        data.value = data.value.getValidDateTime("/", "", false);
        if (data.value == "" || data.value.length > 10) {
            jQuery.prompt("Please enter valid date");
            data.value = "";
            document.getElementById(data.id).focus();
            return false;
        }
    }
}
function validateDateTime(val, ele) {
    var validateDate = ele.value;
    if (validateDate === "" || trim(validateDate).length > 19) {
        jQuery.prompt("Please enter valid date");
        ele.value = "";
        document.getElementById(ele.id).focus();
    } else if (ele.value !== "") {
        if (validateDate.length < 19) {
            if (ele.value.toUpperCase().indexOf("PM") > -1 && ele.value.toUpperCase().indexOf(" PM") === -1) {
                ele.value = ele.value.toUpperCase().replace("PM", " PM");
            } else if (ele.value.toUpperCase().indexOf("AM") > -1 && ele.value.toUpperCase().indexOf(" AM") === -1) {
                ele.value = ele.value.toUpperCase().replace("AM", " AM");
            } else {
                ele.value = validateDate + " 12:00 PM";
            }
        }
        jQuery.ajaxx({
            data: {
                className: "com.gp.cong.logisoft.bc.fcl.BookingDwrBC",
                methodName: "dateTimeValidation",
                param1: ele.value,
                param2: "MM/dd/yyyy HH:mm a"
            },
            success: function (data) {
                if ("null" == data) {
                    jQuery.prompt("Please enter valid date");
                    ele.value = "";
                    document.getElementById(ele.id).focus();
                }
            }
        });
    }
}
function populateDeliveryDetails(result){
    jQuery("#deliveryEmail").val(result.email1);
    jQuery("#deliveryAddress").val(result.address1);
    jQuery("#deliveryContactName").val(result.contactName);
    jQuery("#deliveryCity").val(result.city1);
    jQuery("#deliveryPhone").val(result.phone);
    jQuery("#deliveryZip").val(result.zip);
    jQuery("#deliveryFax").val(result.fax);
    jQuery("#deliveryState").val(result.state);
}
function clearDeliveryFields(){
    jQuery("#deliveryEmail").val('');
    jQuery("#deliveryAddress").val('');
    jQuery("#deliveryContactName").val('');
    jQuery("#deliveryCity").val('');
    jQuery("#deliveryPhone").val('');
    jQuery("#deliveryZip").val('');
    jQuery("#deliveryFax").val('');
    jQuery("#deliveryState").val('');
}
function validateNumeric(val, id) {
    if (val.value != null && val.value != "") {
        if (IsNumeric(val.value) == false) {
            jQuery.prompt("Value should be Numeric");
            jQuery('#' + id).val('');
        }
    }
}
function validateEmail(emailId) {
    var email = "^[A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}(?:[;][A-Za-z0-9\._%-]+@[A-Za-z0-9\.-]+\.[A-Za-z]{2,4}?)*";
    if (trim(emailId.value) != "" && (String(emailId.value).search(email) == -1)) {
        jQuery.prompt("Enter a valid Email address");
        emailId.value = "";
    }
}