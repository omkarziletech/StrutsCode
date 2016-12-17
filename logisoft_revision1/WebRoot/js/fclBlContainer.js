
function popup1(val,val1,val2,val3) {
	if(val3!='3'){
    	document.fclBillLaddingform.index.value=val;
   		document.fclBillLaddingform.buttonValue.value="popup";
		document.fclBillLaddingform.submit();  
	}   
    if (!newwindow.closed && newwindow.location){
          newwindow.location.href = "<%=path%>/fclmarksnumber.do?button="+'NewFCLBL&index='+ val+'&containerId='+val1+'&bolid='+val2;
    }else  {
          GB_show('Marks/Description','<%=path%>/fclmarksnumber.do?button='+'NewFCLBL&index='+val+'&containerId='+val1+'&bolid='+val2,width="750",height="600");
          if (!newwindow.opener) newwindow.opener = self;
    }
    if (window.focus) {newwindow.focus()}
      return false;
} 

function popup2(val,val1,val2,val3){
	if(val3!='3'){
      document.fclBillLaddingform.index.value=val;
      document.fclBillLaddingform.buttonValue.value="hazmat";
      document.fclBillLaddingform.submit();  
  	}   
    if (!newwindow1.closed && newwindow1.location){
          newwindow1.location.href = "<%=path%>/fCLHazMat.do?buttonValue=fclbl&index="+val+'&containerId='+val1+'&bolid='+val2+'&name=FclBl';
    }else {
          newwindow1=window.open("<%=path%>/fCLHazMat.do?buttonValue=fclbl&index="+val+'&containerId='+val1+'&bolid='+val2+'&name=FclBl',"Hi",'width=800,height=600,scrollbars=yes');
          if (!newwindow1.opener) newwindow1.opener = self;
    }
    if (window.focus) {newwindow1.focus()}
      return false;
}
var newwindow2 = '';
function addAES(val,val1,val2,val3){
	if(val3!='3'){
 		document.fclBillLaddingform.index.value=val;
 		document.fclBillLaddingform.buttonValue.value="addAES";
 		document.fclBillLaddingform.submit();  
	}
    if (!newwindow1.closed && newwindow1.location){
          newwindow1.location.href = "<%=path%>/fclAESDetails.do?buttonValue=fclbl&index="+val+'&containerId='+val1+'&bolid='+val2;
    }else{
          newwindow1=window.open("<%=path%>/fclAESDetails.do?buttonValue=fclbl&index="+val+'&containerId='+val1+'&bolid='+val2,"","width=800,height=600");
          if (!newwindow1.opener) newwindow1.opener = self;
    }
    if (window.focus) {newwindow1.focus()}
      return false;  
}

function addBookingHazmat(val,val1,val2,val3){
	if(val3!='3'){
 		document.fclBillLaddingform.index.value=val;
		document.fclBillLaddingform.buttonValue.value="addBookingHazmat";
		document.fclBillLaddingform.submit();  
    }
    if(!newwindow1.closed && newwindow1.location){
          newwindow1.location.href = "<%=path%>/jsps/fclQuotes/AddBookingHazmat.jsp?containerId="+val1+'&bolid='+val2;
    }else{
          newwindow1=window.open("<%=path%>/jsps/fclQuotes/AddBookingHazmat.jsp?containerId="+val1+'&bolid='+val2,"","width=800,height=600");
          if (!newwindow1.opener) newwindow1.opener = self;
    }
    if (window.focus) {newwindow1.focus()}
      return false;
}
function submit1(){
	document.fclBillLaddingform.buttonValue.value="saveContainer";
	document.fclBillLaddingform.submit();
}
function update(){
	document.fclBillLaddingform.buttonValue.value="edit";
	document.fclBillLaddingform.submit();
}
function submitAdd1(){
	document.fclBillLaddingform.buttonValue.value="add";
	document.fclBillLaddingform.submit();
}
function makeFormBorderless(form) {
			var element;
			for (var i = 0; i < form.elements.length; i++) {
			element = form.elements[i];
			if(element.type == "button"){
			if(element.value=="Add" ){
				element.style.visibility="hidden";
				}
			}
		}
  return false;
}
function getContainerData(){
	document.fclBillLaddingform.buttonValue.value="getContainerData";
	document.fclBillLaddingform.submit();
}
function enable(){
	var element;
	var trailer;
	var d = new Date();
	var curr_date = d.getDate();
	var curr_month = d.getMonth()+1;
	var curr_year = d.getFullYear();
	var now=curr_month+"/"+curr_date+"/"+curr_year;
	if(document.fclBillLaddingform.lastUpdate.length!=undefined){
		document.fclBillLaddingform.lastUpdate[val.id].value=now;
	}else{
		document.fclBillLaddingform.lastUpdate.value=now;
	}
	for (var i = 0; i < document.fclBillLaddingform.elements.length; i++) {
    	element=document.fclBillLaddingform.elements[i];
    	if(element.name == 'trailerNo'){
    		trailer = element.value;
    	}
    	alert(trailer);
		if(element.type == "button"){
	        if(trailer != ''){
	  			element.style.visibility="visible";
	  		}
	 	}
	
	}
}
function getDate(val){
	var d = new Date();
	var curr_date = d.getDate();
	var curr_month = d.getMonth()+1;
	var curr_year = d.getFullYear();
	var now=curr_month+"/"+curr_date+"/"+curr_year;
	document.fclBillLaddingform.lastUpdate[val.id].value=now;
}
function checkNumeric(value){		
	var anum=/(^\d+$)|(^\d+\.\d+$)/		
	if (anum.test(value))			
	return true;		
	return false;   
}
function check(){
	var element;
	var trailer;
	for (var i = 0; i < document.fclBillLaddingform.elements.length; i++) {
    	element=document.fclBillLaddingform.elements[i];
    	if(element.name == 'trailerNo'){
    		trailer = element.value;
    	}
		if(element.type == "button"){
	        if(trailer != '' && trailer!='undefined'){
	  			element.style.visibility="visible";
	  		}
	 	}
	}
}
function allowFreeFormat(val){
    formatTrialNo(val);
}



