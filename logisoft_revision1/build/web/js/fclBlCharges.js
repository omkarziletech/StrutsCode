function checkbox(){
	 var readyToPostCheck="";
	 if(document.fclBillLaddingform.readyToPost!=undefined){
		if(document.fclBillLaddingform.readyToPost.length!=undefined){
			for(var i=0;i<document.fclBillLaddingform.readyToPost.length;i++){
				if(document.fclBillLaddingform.readyToPost[i].checked){
					readyToPostCheck=readyToPostCheck+"1,";
				}else{
					readyToPostCheck=readyToPostCheck+"0,";
				}
			}
		}else{
			if(document.fclBillLaddingform.readyToPost.checked){
				readyToPostCheck=readyToPostCheck+"1,";
			}else{
				readyToPostCheck=readyToPostCheck+"0,";
			}
		}
	  }
	var readyToPostCostCheck="";
	if(document.fclBillLaddingform.readyToPostForCost!=undefined){
	    if(document.fclBillLaddingform.readyToPostForCost.length!=undefined){
			for(var i=0;i<document.fclBillLaddingform.readyToPostForCost.length;i++){
				if(document.fclBillLaddingform.readyToPostForCost[i].checked){
					readyToPostCostCheck=readyToPostCostCheck+"1,";
				}else{
					readyToPostCostCheck=readyToPostCostCheck+"0,";
				}
			}
		}else{
			if(document.fclBillLaddingform.readyToPostForCost.checked){
				readyToPostCostCheck=readyToPostCostCheck+"1,";
			}else{
				readyToPostCostCheck=readyToPostCostCheck+"0,";
			}
		}
	}
	document.fclBillLaddingform.readyToPostCheck.value=readyToPostCheck;
	document.fclBillLaddingform.readyToPostCostCheck.value=readyToPostCostCheck;
}
function disabled(val1) { 
   if(val1==1){
      var update = document.getElementById('update');
      update.style.visibility = 'hidden';
   }else if(val1==2){
   	  var save = document.getElementById('save');
      save.style.visibility = 'hidden';
   }
}
function addCharges(){
	checkbox();
	document.fclBillLaddingform.buttonValue.value="addCharges";
	document.fclBillLaddingform.submit();
}
function submitCorrections(){
	checkbox();
	document.fclBillLaddingform.buttonValue.value="correctionsCharges";
	document.fclBillLaddingform.submit();
}
function saveCharges(){
	checkbox();
	if(document.fclBillLaddingform.billTo!=undefined){
		for(var i=0;i<document.fclBillLaddingform.billTo.length;i++){
			if(document.fclBillLaddingform.billTo[i]!=undefined && document.fclBillLaddingform.billTo[i].value=='ThirdParty'){
				if(document.fclBillLaddingform.billThirdPartyName.value==""){
					alertNew("Please Enter the Third Party Name");
					return;
				}
			}
		}
	}
	document.fclBillLaddingform.buttonValue.value="saveCharges";
	document.fclBillLaddingform.submit();
}
function update(){
	checkbox();
	document.fclBillLaddingform.buttonValue.value="editCharges";
	document.fclBillLaddingform.submit();
}
function getmanifest(val){
		if(document.fclBillLaddingform.billTo.length==undefined){
			if(document.fclBillLaddingform.billTo.value=='ThirdParty'){
				if(document.fclBillLaddingform.accountname.value==""){
					alertNew("Please Enter the Third Party Name");
					return;
				 }
			}
		}else{
			for(var i=0;i<document.fclBillLaddingform.billTo.length;i++){
				if(document.fclBillLaddingform.billTo[i].value=='ThirdParty'){
					if(document.fclBillLaddingform.accountname.value==""){
						alertNew("Please Enter the Third Party Name");
						return;
					}
				}
			}
		}
}
function getcostmanifest(obj){
	var rowindex=obj.parentNode.parentNode.rowIndex;
	var i=rowindex-1;
	var textstr="document.fclBillLaddingform.accountname"+(i);
	var textobj = eval(textstr);
	if(textobj.value==''){
		 alertNew("Please Select Account Name");
		 document.fclBillLaddingform.readyToPostForCost[i].checked=false;
		 return;
	 }
}
function getUpdatedCompleteBL(){
  checkbox();
  document.fclBillLaddingform.buttonValue.value="getUpdatedBLInfo";
  document.fclBillLaddingform.submit();
}
function disabled(val1){ 
  if(val1== 3){
       var imgs = document.getElementsByTagName('img');
       for(var k=0; k<imgs.length; k++){
         if(imgs[k].id != "previous" && imgs[k].id!="marks"){
           imgs[k].style.visibility = 'hidden';
         }
       }
       var input = document.getElementsByTagName("input");
       for(i=0; i<input.length; i++) {
           input[i].readOnly=true;
           input[i].tabIndex=-1;
           input[i].style.color="blue";
       }
       var textarea = document.getElementsByTagName("textarea");
       for(i=0; i<textarea.length; i++){
          textarea[i].readOnly=true;
          input[i].tabIndex=-1;
          textarea[i].style.color="blue";
       }
       var select = document.getElementsByTagName("select");
       for(i=0; i<select.length; i++) {
    	  select[i].disabled=true;
   		  select[i].style.backgroundColor="blue";
       }
       document.getElementById("save").style.visibility = 'hidden';
       document.getElementById("add").style.visibility = 'hidden';
       document.getElementById("add1").style.visibility = 'hidden';
       document.getElementById("correction").style.visibility = 'hidden';
       document.getElementById("correction1").style.visibility = 'hidden';
  }
}
function makeFormBorderless(form) {
	var element;
	for (var i = 0; i < form.elements.length; i++) {
		element = form.elements[i];
		if(element.type == "button"){
			if(element.value=="Add" || element.value=="Delete" ){
				element.style.visibility="hidden";
			}
		}
	}
return false;
}	
function checkAll(){
   if(document.fclBillLaddingform.chargesCheckAll.checked){
      if(document.fclBillLaddingform.readyToPost.length!=undefined){
      	 for(var j=0;j<document.fclBillLaddingform.readyToPost.length;j++){
  			 document.fclBillLaddingform.readyToPost[j].checked = true;
  		 }
  	  }else{
  		 document.fclBillLaddingform.readyToPost.checked = true;
  	  }
   }else{
      if(document.fclBillLaddingform.readyToPost.length!=undefined){
         for(var j=0;j<document.fclBillLaddingform.readyToPost.length;j++){
  		   document.fclBillLaddingform.readyToPost[j].checked = false;
  		 }
  	  }else{
  		   document.fclBillLaddingform.readyToPost.checked = false;
  	  }
   }
}
function costCodeCheckAll(){
   if(document.fclBillLaddingform.fclCostCodeCheckAll.checked){
   	  for(var j=0;j<document.fclBillLaddingform.readyToPostForCost.length;j++){
  		 document.fclBillLaddingform.readyToPostForCost[j].checked = true;
  	  }
   }else{
      for(var j=0;j<document.fclBillLaddingform.readyToPostForCost.length;j++){
  		 document.fclBillLaddingform.readyToPostForCost[j].checked = false;
  	  }
   }
}
function load1(val){
	if(val!=""){
		var a=val.replace(/=/g,"\n");
	}
}
