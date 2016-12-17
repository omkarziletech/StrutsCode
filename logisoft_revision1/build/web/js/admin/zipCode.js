function addZipCode(){
    document.getElementById("addnew").style.display="none";
     document.getElementById("cancel").style.visibility="visible";
    document.getElementById("zipCodeDiv").style.display="none";
    document.getElementById("buttonName").value="Save";
    clearFormValues();
}
function zipCodeAction(action){
    if(action.value == 'Save' || action.value == 'Update'){
        if(document.zipCodeForm.zip.value ==''){
            alert('Please Enter Zip Code');
            return;
        }else if(document.zipCodeForm.city.value ==''){
            alert('Please Enter City');
             return;
        }
    }
    document.zipCodeForm.buttonValue.value=action.value;
    document.zipCodeForm.submit();
}
function editZipcode(code,city,state,id){
    document.getElementById("addnew").style.display="none";
    document.getElementById("zipCodeDiv").style.display="none";
    document.getElementById("cancel").style.visibility="visible";
    document.getElementById("buttonName").value="Update";
    document.zipCodeForm.id.value=id;
    document.zipCodeForm.city.value=city;
    document.zipCodeForm.state.value=state;
    document.zipCodeForm.zip.value=code;
}
function clearFormValues(){
     document.zipCodeForm.city.value="";
    document.zipCodeForm.state.value="";
    document.zipCodeForm.zip.value="";
}
function cancelZipCode(){
    clearFormValues();
    document.zipCodeForm.submit();
}
var zipId;
function deleteZipCode(id){
  zipId=id;
  confirmNew("Are you sure want to Delete this Record","deleteZip");
}
function confirmMessageFunction(id1,id2){
    if(id1=='deleteZip' && id2=='ok'){
        document.zipCodeForm.id.value=zipId;
        document.zipCodeForm.buttonValue.value="Delete";
        document.zipCodeForm.submit();
    }
}
function fillState(){
     setTimeout("fillCity()", 100);
}
function fillCity(){
     var city = document.getElementById("city").value;
    if(city.indexOf('/') != -1){
        document.getElementById("state").value = city.substring(city.indexOf('/')+1);
        document.getElementById("city").value =  city.substring(0, city.indexOf('/'));
     }
}