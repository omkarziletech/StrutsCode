function alertNew(text) {
	var something = false;
	if (something) {
        // do this section
	} else {
		DisplayAlert("AlertBox", 100, 50, text,window.center({width:100,height:100}));
	}
}
function call(){
    var cvr = document.getElementById("cover");
    if( document.body && ( document.body.scrollWidth || document.body.scrollHeight ) ) {
        var pageWidth1 = document.body.scrollWidth+'px';
    } else if( document.body.offsetWidth ) {
        pageWidth1 = document.body.offsetWidth+'px';
    } else {
        pageWidth1='100%';
    }
    cvr.style.width= pageWidth1;
    cvr.style.height= '100%';
    cvr.style.display = "block";
    document.getElementById('newProgressBar').style.display="block";
}
function validateDate(date){
    if(date.value!=""){
        date.value = date.value.getValidDateTime("/","",false);
        if(date.value=="" ||date.value.length>10){
            alertNew("Please enter valid date");
            date.value="";
            document.getElementById(date.id).focus();
        }
    }
}
function call2(){
    var cvr = document.getElementById("cover");
    cvr.style.display = "none";
    document.getElementById('newProgressBar').style.display="none";
}
function showall(){
    document.searchQuotationform.buttonValue.value="fileSearch";
    document.searchQuotationform.submit();
}
function searchquotation(){
    document.getElementById('quickRates').disabled = false;
    if(document.searchQuotationform.fileNumber.value!="" || document.searchQuotationform.ssBkgNo.value!=""
            || document.searchQuotationform.masterBL.value!="" || document.searchQuotationform.inbondNumber.value!=""
            || document.searchQuotationform.container.value!="" || (document.searchQuotationform.aesItn && document.searchQuotationform.aesItn.value!="")
    || document.searchQuotationform.filerBy.value == 'Closed' || document.searchQuotationform.filerBy.value == 'Audited'){
        document.searchQuotationform.quotestartdate.value="";
        document.searchQuotationform.toDate.value="";
        call();
        document.searchQuotationform.buttonValue.value="fileSearch";
        document.searchQuotationform.submit();
    }else if(document.searchQuotationform.quotestartdate.value=="" &&document.searchQuotationform.toDate.value!=""){
        alertNew("Please Enter From Date");
        return;
    }else if(document.searchQuotationform.toDate.value=="" &&document.searchQuotationform.quotestartdate.value!=""){
        alertNew("Please Enter To Date");
        return;
    }else{
        call();
            document.searchQuotationform.buttonValue.value="fileSearch";
            document.searchQuotationform.submit();
    }
}
function resetForm(){
     document.searchQuotationform.buttonValue.value="reset";
     document.searchQuotationform.submit();
}
function searchByFileNumber(){
    var fileNumber = document.getElementById("fileNumberSearch").value;
    if(null != fileNumber && fileNumber != ''){
        document.searchQuotationform.fileNumber.value=fileNumber;
        document.searchQuotationform.limit.value="250";
        document.searchQuotationform.filerBy.value="All";
        document.searchQuotationform.sortByDate.value="";
        call();
        document.searchQuotationform.buttonValue.value="fileSearch";
        document.searchQuotationform.submit();
    }
}
function searchquotationEnterKey(){
    if(document.searchQuotationform.fileNumber.value!="" || document.searchQuotationform.ssBkgNo.value!="" 
            || document.searchQuotationform.masterBL.value != "" || document.searchQuotationform.inbondNumber.value != ""
            || document.searchQuotationform.container.value != "" || (document.searchQuotationform.aesItn && document.searchQuotationform.aesItn.value != "")){
        document.searchQuotationform.quotestartdate.value="";
        document.searchQuotationform.toDate.value="";
    }
    call();
    document.searchQuotationform.buttonValue.value="fileSearch";
    document.searchQuotationform.submit();
}
function disabled(val){
    if(val == 0)
    {
        var imgs = document.getElementsByTagName('img');
        for(var k=0; k<imgs.length; k++)
        {
            if(imgs[k].id != "showall" && imgs[k].id!="search")
            {
                imgs[k].style.visibility = 'hidden';
            }
        }

    }
}
function refereshPage(){
    document.searchQuotationform.buttonValue.value="refresh";
    document.searchQuotationform.fileNumber.value="";
    document.searchQuotationform.submit();
}


function getLoginName(val){
    if(document.getElementById("loginCheck").checked){
        document.getElementById("loginCheck").value = 'on';
        document.searchQuotationform.quoteBy.value=val;
    }else{
        document.searchQuotationform.quoteBy.value="";
    }
}
function getLoginName1(val){
    if(document.getElementById("loginCheck1").checked){
        document.getElementById("loginCheck1").checked = 'on';
               // document.searchQuotationform.bookedBy.value=val;
        document.getElementById("bookedBy").value=val;
    }else{
        //document.searchQuotationform.bookedBy.value="";
        document.getElementById("bookedBy").value="";
    }
}
function ediTrack(dockReceipt){
 	GB_showFullScreen('Edi Tracking','/logisoft/ediTracking.do?fileNumber='+dockReceipt);
}
function makeAesBorderless(form){
    window.parent.makeFormBorderless(form);
}
