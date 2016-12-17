$.fn.insertFromCalendar = function(options) {
    if($(this).hasClass("calendar-img")){
	$(this).mousedown(function(){
	    Calendar.setup({
		inputField	: options.inputField,    // id of the input field
		ifFormat	: options.format,	// the date format
		button	: $(this).attr("id")	// id of the button
	    });
	});
    }
    return $(this);
};

function isDate(obj){
    var dateArray = jQuery(obj).val().split("/");
    if(dateArray.length!= 3){
	return false;
    }
    dateArray[2] = dateArray[2].length == 2 ?("20" + dateArray[2]) : dateArray[2];
    if(dateArray[0].length!=2 || dateArray[1].length!=2 || dateArray[2].length!=4){
	return false;
    }
    var date = new Date(dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2]);
    if(date.getMonth()+1 == Number(dateArray[0])  && date.getDate() == Number(dateArray[1]) && date.getFullYear() == Number(dateArray[2])){
	jQuery(obj).val(dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2])
	return true;
    }else{
	return false;
    }
}

function validateDate(obj){
    var dateVal = jQuery(obj).val();
    if(dateVal.indexOf("/")>-1){
	return isDate(obj);
    }else if(dateVal.length==8){
	jQuery(obj).val(dateVal.substring(0,2) + "/" + dateVal.substring(2,4) + "/" + dateVal.substring(4,8))
	return true;
    }else {
	return false;
    }
}

function isDateEmpty(obj){
    return jQuery.trim(jQuery(obj).val())=="";
}

function isGreaterThan(obj1,obj2){
    return Date.parse(jQuery.trim(jQuery(obj1).val())) > Date.parse(jQuery.trim(jQuery(obj2).val()));
}

function parseDate(input) {
    var parts = input.match(/(\d+)/g);
    // new Date(year, month [, date [, hours[, minutes[, seconds[, ms]]]]])
    return null!=parts && parts.length==3?new Date(parts[2], parts[0]-1, parts[1]):-1; // months are 0-based
}