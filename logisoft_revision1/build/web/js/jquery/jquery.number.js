function allowOnlyNumbers(event,obj){
    var number = jQuery(obj).val();
    var firstDigit = number.substr(0, 1);
    if(number.length==0 && event.keyCode==190){
	event.preventDefault();
    }else if((firstDigit=="0" && number.length==1 && event.keyCode!=190 && event.keyCode!=110
	&& (event.keyCode!=8 && event.keyCode!=9 && event.keyCode!=13 && event.keyCode!=46)
	&& (event.keyCode<33 || event.keyCode>40))){
	event.preventDefault();
    }else if((number.length==8 && number.indexOf(".")<0 && event.keyCode!=190
	&& (event.keyCode!=8 && event.keyCode!=9 && event.keyCode!=13 && event.keyCode!=46 && (event.keyCode<33 || event.keyCode>40)))
    || ((event.keyCode<48 || event.keyCode>57) && (event.keyCode<96 || event.keyCode>105)
	&& (event.keyCode!=8 && event.keyCode!=9 && event.keyCode!=13 && event.keyCode!=46 && event.keyCode!=110)
	&& (event.keyCode<33 || event.keyCode>40) 
	&& ((number.indexOf("-")>-1 && (event.keyCode==189 || event.keyCode==109)) || (event.keyCode!=189 && event.keyCode!=109))
	&& ((number.indexOf(".")>-1 && event.keyCode==190)|| event.keyCode!=190))){
	event.preventDefault();
    }
}

function validateAmount(event,obj){
    if((event.keyCode<33 && event.keyCode>40) && event.keyCode!=46 && event.keyCode!=8 && event.keyCode!=9){
	var number = jQuery(obj).val();
	if(number.length>0){
	    var firstDigit = number.substr(0, 1);
	    if(firstDigit=="-"){
		number = "-"+number.replace(/[^0-9.]+/g,"");
		if(number.indexOf(".")>=0){
		    var negative = number.split(".");
		    var negativeValue = negative[0]+"."+negative[1].substr(0, 2);
		    number = negativeValue.substr(0,11);
		}else{
		    number = number.substr(0,8);
		}
	    }else{
		number = number.replace(/[^0-9.]+/g,"");
		if(number.indexOf(".")>=0){
		    var positive = number.split(".");
		    var positiveValue = positive[0]+"."+positive[1].substr(0, 2);
		    number = positiveValue.substr(0,11);
		}else{
		    number = number.substr(0,8);
		}
	    }
	    if(number.length==1 && number.indexOf("-")==0){
		jQuery(obj).val("-")
	    }else if(number.indexOf("-")==0 && number.indexOf("0")==1){
		if(number.indexOf(".")==(number.length-1)){
		    jQuery(obj).val("-"+Number(number)+".");
		}else{
		    jQuery(obj).val("-"+Number(number));
		}
	    }else{
		if(number.indexOf(".")==(number.length-1)){
		    jQuery(obj).val(Number(number)+".");
		}else if(number.substr((number.indexOf(".")+1),1)=="0" || number.substr((number.indexOf(".")+1),2)=="00"){
		    jQuery(obj).val(number)
		}else{
		    jQuery(obj).val(Number(number));
		}
	    }
	}
    }
}

var pad = function(n,l) {
    return (n.toString().length>n)?n:('00000000000000000000'+n).slice(-l);
}

var formatNumber = function(number, pattern){ 
    if (!pattern || isNaN(+number)) {
	return number; //return as it is.
    }
    //convert any string to number according to formation sign.
    number = pattern.charAt(0) == '-'? -number: +number;
    var isNegative = number<0? number= -number: 0; //process only abs(), and turn on flag.
    //search for separator for grp & decimal, anything not digit, not +/- sign, not #.
    var result = pattern.match(/[^\d\-\+#]/g);
    var decimal = (result && result[result.length-1]) || '.'; //treat the right most symbol as decimal 
    var group = (result && result[1] && result[0]) || ',';  //treat the left most symbol as group separator
    //split the decimal for the format string if any.
    pattern = pattern.split( decimal);
    //Fix the decimal first, toFixed will auto fill trailing zero.
    number = number.toFixed( pattern[1] && pattern[1].length);
    number = +(number) + ''; //convert number to string to trim off *all* trailing decimal zero(es)
    //fill back any trailing zero according to format
    var pos_trail_zero = pattern[1] && pattern[1].lastIndexOf('0'); //look for last zero in format
    var part = number.split('.');
    //integer will get !part[1]
    if (!part[1] || part[1] && part[1].length <= pos_trail_zero) {
	number = (+number).toFixed( pos_trail_zero+1);
    }
    var szSep = pattern[0].split( group); //look for separator
    pattern[0] = szSep.join(''); //join back without separator for counting the pos of any leading 0.
    var pos_lead_zero = pattern[0] && pattern[0].indexOf('0');
    if (pos_lead_zero > -1 ) {
	while (part[0].length < (pattern[0].length - pos_lead_zero)) {
	    part[0] = '0' + part[0];
	}
    }
    else if (+part[0] == 0){
	part[0] = '';
    }
    number = number.split('.');
    number[0] = part[0];
    //process the first group separator from decimal (.) only, the rest ignore.
    //get the length of the last slice of split result.
    var pos_separator = ( szSep[1] && szSep[ szSep.length-1].length);
    if (pos_separator) {
	var integer = number[0];
	var str = '';
	var offset = integer.length % pos_separator;
	for (var i=0, l=integer.length; i<l; i++) { 
					
	    str += integer.charAt(i); //ie6 only support charAt for sz.
	    //-pos_separator so that won't trail separator on full length
	    if (!((i-offset+1)%pos_separator) && i<l-pos_separator ) {
		str += group;
	    }
	}
	number[0] = str;
    }
    number[1] = (pattern[1] && number[1])? decimal+number[1] : "";
    return (isNegative?'-':'') + number[0] + number[1]; //put back any negation and combine integer and fraction.
};

$.fn.currencyFormat = function(fixedTo,pattern) {
    this.each(function() {
	if(!isNaN(parseFloat(this.value))){
	    this.value =formatNumber(parseFloat(this.value).toFixed(fixedTo),pattern);
	}else if(!isNaN(parseFloat(this.innerHTML))){
	    this.innerHTML=formatNumber(parseFloat(this.innerHTML).toFixed(fixedTo),pattern);
	}
    });
    return this;
};

$.fn.numeric = function(){
    var numLock = false;
    function isNotAllowed(event,ele){
	var number = $(ele).val();
	var firstDigit = number.substr(0, 1);
	var cursor = $(ele).caret().start;
	return (event.keyCode == 32 // backspace
	    || (event.keyCode >= 65 && event.keyCode <= 90) // alphabets a-z
	    || (event.keyCode >= 219 && event.keyCode <= 222) // special characters
	    || (event.shiftKey && (event.keyCode >= 48 && event.keyCode <= 57)) // special characters
	    || (event.shiftKey && (event.keyCode == 189 || event.keyCode == 190)) // special characters
	    || ((event.keyCode >= 186 && event.keyCode <= 192) && event.keyCode != 189 && event.keyCode != 190) // special characters
	    || ((event.keyCode >= 106 && event.keyCode <= 111) && event.keyCode != 109 && event.keyCode != 110) // special characters
	    || ((number.length <= 0 || cursor == 0) && (event.keyCode == 190 || event.keyCode == 110)) // dot at first
	    || (firstDigit == "0" && number.length == 1 && cursor == 1
		&& ((event.keyCode >= 48 && event.keyCode <= 57) 
		    || (numLock && (event.keyCode >= 96 && event.keyCode <= 111) && event.keyCode != 110)))) // first and second digits zero
    }
    
    $(this).bind("keydown", function(event) {
	var number = $(this).val();
	if((number.indexOf("-") < 0 && (event.keyCode == 189 || event.keyCode == 109))){
	    $(this).attr("maxlength","12");
	}
	if(event.keyCode == 144){
	    numLock = numLock ? false : true;
	}else if(isNotAllowed(event,this)){
	    event.preventDefault();
	}else{
	    if((number.indexOf("-") >= 0 && (event.keyCode == 189 || event.keyCode == 109)) // one minus only
		|| (number.indexOf(".") >= 0 && (event.keyCode == 190 || event.keyCode == 110))){ // one dot only
		event.preventDefault();
	    }else if(number.indexOf("-") == 0 && $(this).caret().start == 0 
		&& ((event.keyCode >= 48 && event.keyCode <= 57) || (numLock && event.keyCode >= 96 && event.keyCode <= 111))){ // numbers before minus
		event.preventDefault();
	    }else if(number.length>0 && ((event.keyCode >= 48 && event.keyCode <= 57) || (numLock && event.keyCode >= 96 && event.keyCode <= 111))){
		var length = number.indexOf("-") >=0 ? 9 : 8;
		length += number.indexOf(".") >=0 ? 3 : 0;
		if(number.length >= length){
		    event.preventDefault();
		}else{
		    var cursor = $(this).caret().start;
		    var decimal = number.split(".");
		    if(decimal.length==2 && decimal[1].length==2 && cursor > number.length-3){
			event.preventDefault();
		    }
		}
	    }
	}
    });
    
    $(this).bind("keyup", function(event) {
	var number = $(this).val();
	if(number.indexOf("-") >= 0){
	    $(this).attr("maxlength","12");
	}else{
	    $(this).attr("maxlength","11");
	}
	if(!numLock && event.keyCode >= 96 && event.keyCode <= 105){
	    var cursor = $(this).caret().start;
	    var digit = cursor==0?number:number.substr(cursor-1, 1);
	    if(digit>=0 && digit<=9){
		numLock	= true;
	    }
	}
    });
    
}