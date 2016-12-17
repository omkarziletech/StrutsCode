dwr.engine.setTextHtmlHandler(dwrSessionError);
if(document.getElementById("mainAccount")){
    AjaxAutocompleter("mainAccount", "mainAccountDiv","","mainAccountValid",rootPath+"/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=mainAccount&tabName=ACCOUNT_HISTORY","getMainAccountBudget()","");
}
if(document.getElementById("copyAccount")){
    AjaxAutocompleter("copyAccount", "copyAccountDiv","","copyAccountValid",rootPath+"/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=copyAccount&tabName=ACCOUNT_HISTORY","getCopyAccountBudget()","");
}

function getMainAccountBudget(){
    if((event.keyCode==9) || (event.keyCode==13) || (event.button==0)){
        if(trim(document.budgetForm.mainAccount.value)==''){
            alert("Please Enter Main Account");
            return;
        }else{
            document.budgetForm.action.value="getMainAccountBudget";
            document.budgetForm.submit();
        }
    }
}
function getCopyAccountBudget(){
    if((event.keyCode==9) || (event.keyCode==13) || (event.button==0)){
        if(trim(document.budgetForm.copyAccount.value)==''){
            alert("Please Enter Copy Account");
            return;
        }else{
            document.budgetForm.action.value="getCopyAccountBudget";
            document.budgetForm.submit();
        }
    }
}
function editBudget(){
    if(document.getElementById("budget")){
        document.budgetForm.action.value="editBudget";
        document.budgetForm.submit();
    }else{
        alert("No Budget items found to edit");
        return;
    }
}
function saveBudget(){
    if(document.getElementById("budget")){
        document.budgetForm.action.value="saveBudget";
        document.budgetForm.submit();
    }else{
        alert("No Budget items found to save");
        return;
    }
}

function updateBudget(){
    if(document.getElementById("budget")){
        if(document.budgetForm.amount.value==""){
            alert("Please enter amount to update");
            return;
        }else if((document.budgetForm.budgetMethod.value=="Base,Amount Increase"
            || document.budgetForm.budgetMethod.value=="Base,Percent Increase")
        && document.budgetForm.increaseBy.value==""){
            alert("Please enter increase by amount");
            return;
        }else{
            document.budgetForm.action.value="updateBudget";
            document.budgetForm.submit();
        }
    }else{
        alert("No Budget items found to update");
        return;
    }
}

function copyBudget(){
    if(trim(document.budgetForm.copyAccount.value)==''){
        alert("Please Enter Copy Account");
        return;
    }else if(document.getElementById("budget")){
        document.budgetForm.action.value="copyBudget";
        document.budgetForm.submit();
    }else{
        alert("No Budget items found to Copy");
        return;
    }
}
function importBudget(){
    if(trim(document.budgetForm.budgetSheet.value)==''){
        alert("Please Include Budget Sheet");
        return;
    }else{
        document.budgetForm.action.value="importBudget";
        document.budgetForm.submit();
    }
}
function displayIncreaseBy(ele){
    if(ele.value=="Base,Amount Increase" || ele.value=="Base,Percent Increase"){
        document.getElementById("increaseByLabel").className="showField";
        document.getElementById("increaseByValue").className="showField";
    }else{
        document.getElementById("increaseByLabel").className="hideField";
        document.getElementById("increaseByValue").className="hideField";
    }
}