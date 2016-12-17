dwr.engine.setTextHtmlHandler(dwrSessionError);

function costCodeAutoCompleter(index){
    var url = rootPath+"/servlet/AutoCompleterServlet?action=ChargeCode&textFieldId=costCode"+index+"&from=Accruals";
    new Ajax.ScrollAutocompleter("costCode"+index, "costCodeDiv"+index,url, {
        paramName: "costCode"+index,
        afterUpdateElement : function (text, li) {
            var list = li.id.split("@");
            $("blueScreenChargeCode"+index).value = list[0];
            $("costCode"+index).value = list[1];
            $("costCodeValid"+index).value = list[1];
            $("shipmentType"+index).value = list[2];
            $("deriveYn"+index).value = list[3];
            $("costCode"+index).blur();
            showOrHideTerminal(index);
        }
    });

    Event.observe("costCode"+index, "blur", function (event){
        var element = Event.element(event);
        if(element.value!=$("costCodeValid"+index).value){
            $("blueScreenChargeCode"+index).value = "";
            $("costCode"+index).value = "";
            $("costCodeValid"+index).value = "";
            $("shipmentType"+index).value = "";
            $("deriveYn"+index).value = "";
            $("glAccount"+index).value = "";
            $("terminal"+index).value = "";
            $("costCode"+index).focus();
            document.getElementById("terminal"+index).style.display="none";
            document.getElementById("updateBtn"+index).style.display="block";
        }
    });
}

function showOrHideTerminal(index){
    var deriveYn = document.getElementById("deriveYn"+index).value;
    if(trim(deriveYn)=='B' || trim(deriveYn)=='L' || trim(deriveYn)=='D'){
        var deriveYnType = "";
        if(trim(deriveYn)=='B'){
            deriveYnType = "Billing";
        }else if(trim(deriveYn)=='L'){
            deriveYnType = "Loading";
        }else if(trim(deriveYn)=='D'){
            deriveYnType = "Dock Receipt";
        }
        deriveYnType+=" Terminal Number";
        alert("Please Select "+deriveYnType);
        document.getElementById("terminal"+index).style.display="block";
        document.getElementById("terminal"+index).focus();
        document.getElementById("updateBtn"+index).style.display="none";
    }else{
        document.getElementById("updateBtn"+index).style.display="block";
        deriveGlAccount(index);
    }
}

function deriveGlAccount(index){
    var costCode = dwr.util.getValue("costCode"+index);
    var shipmentType = dwr.util.getValue("shipmentType"+index);
    var terminal = dwr.util.getValue("terminal"+index);
    var deriveYn = document.getElementById("deriveYn"+index).value;
    if(trim(costCode)!=""){
        if((trim(deriveYn)=='B' || trim(deriveYn)=='L' || trim(deriveYn)=='D') && trim(terminal)==""){
        }
        //do nothing
        else{
            AccrualsBC.deriveGlAccount(costCode,shipmentType,terminal,function(glAccount){
                if(null!=glAccount){
                    dwr.util.setValue("glAccount"+index,glAccount);
                    document.getElementById("terminal"+index).style.display="none";
                    document.getElementById("updateBtn"+index).style.display = "block";
                }else{
                    alert("No Gl Account available");
                    dwr.util.setValue("glAccount"+index,"");
                    dwr.util.setValue("terminal"+index,"");
                    document.getElementById("terminal"+index).focus();
                }
            });
        }
    }
}

var costCodes = document.getElementsByName("costCode"); 
if(costCodes){
    for(var index=0;index<costCodes.length;index++){
        costCodeAutoCompleter(index);
    }
}

function updateAccruals(transactionId,index){
    var costCodes = document.getElementsByName("costCode");
    var blueScreenChargeCodes = document.getElementsByName("blueScreenChargeCode");
    var shipmentTypes = document.getElementsByName("shipmentType");
    var glAccounts = document.getElementsByName("glAccount");
    var transactionBean = new Object();
    transactionBean.transactionId = transactionId;
    transactionBean.chargeCode = costCodes[index].value;
    transactionBean.blueScreenChargeCode = blueScreenChargeCodes[index].value;
    transactionBean.shipmentType = shipmentTypes[index].value;
    transactionBean.glAcctNo = glAccounts[index].value;
    if(trim(transactionBean.chargeCode) == ""){
        alert("Please Enter Cost code");
        costCodes[index].focus();
        return;
    }else if(trim(transactionBean.glAcctNo) == ""){
        alert("Please Enter GL Account");
        costCodes[index].focus();
        return;
    }else{
        AccrualsBC.updateAccruals(transactionBean,function(result){
            alert(result);
        });
    }
}

jQuery(document).ready(function(){
    var height = jQuery(document).height()-50;
    var width = jQuery(document).width()-100;
    jQuery(".scrolldisplaytable").height(height).width(width);
});