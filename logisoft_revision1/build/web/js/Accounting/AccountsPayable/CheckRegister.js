var checkRegisterForm;
dwr.engine.setTextHtmlHandler(dwrSessionError);
jQuery(document).ready(function(){
    jQuery(document).keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == 13) {
            search();
        }
    });
})
function search() {
    checkRegisterForm = this.getCheckRegisterForm();
    if(null!=checkRegisterForm){
        checkRegisterForm.pageNo=1;
        checkRegisterForm.currentPageSize=100;
        checkRegisterForm.buttonValue="doSearch";
        jQuery(":button").attr("disabled", true);
	var canEdit = dwr.util.getValue("canEdit");
        DwrUtil.getCheckRegisterList(checkRegisterForm,canEdit,function (data){
            if(data){
                dwr.util.setValue("checkRegisterListDiv", data, {
                    escapeHtml:false
                });
                changeTableSortingPath();
            }
            jQuery(":button").attr("disabled", false);
        });
    }
}

function exportToExcel(checkNo,custNo,paymentDate,paymentMethod,transactionIds) {
    document.checkRegisterForm.checkNo.value=checkNo;
    document.checkRegisterForm.custNo.value=custNo;
    document.checkRegisterForm.paymentDate.value=paymentDate;
    document.checkRegisterForm.paymentMethod.value=paymentMethod;
    document.checkRegisterForm.transactionIds.value=transactionIds;
    document.checkRegisterForm.buttonValue.value="exportToExcel";
    document.checkRegisterForm.submit();
}
function print(checkNo,custNo,paymentDate,paymentMethod,transactionIds) {
    var checkRegisterForm = new Object();
    checkRegisterForm.checkNo=checkNo;
    checkRegisterForm.custNo=custNo;
    checkRegisterForm.paymentDate=paymentDate;
    checkRegisterForm.paymentMethod=paymentMethod;
    checkRegisterForm.transactionIds=transactionIds;
    DwrUtil.printCheckRegistgerDetails(checkRegisterForm,function(fileName){
        window.parent.showGreyBox("Payment Details",rootPath+"/servlet/FileViewerServlet?fileName="+fileName);
    });
}

function checkSelected(name,obj,id) {
    var rowIn=obj.parentNode.parentNode.rowIndex;
    rowIn--;
    if(name == "void") {
        var reprint=document.getElementsByName("reprint");
        reprint[rowIn].checked = false;
    }else if(name == "reprint") {
        var voids = document.getElementsByName("void");
        voids[rowIn].checked=false;
    }
}
function save(){
    var transaction = document.getElementsByName("transactionId");
    var voids = document.getElementsByName("void");
    var reprint = document.getElementsByName("reprint");
    var voidTransaction = "";
    var reprintTransaction = "";
    var j=0;
    var k=0;
    for(var i=0; i < transaction.length ; i++) {
        if(voids[i].checked && !voids[i].disabled) {
            voidTransaction = voidTransaction+voids[i].value+":-";
            j++;
        }else if(reprint[i].checked && !voids[i].disabled && !reprint[i].disabled) {
            reprintTransaction = reprintTransaction+reprint[i].value+":-";
            k++;
        }
    }
    if(j==0 && k==0){
        alert("Please select either void or reprint");
        return;
    }else{
        jQuery(":button").attr("disabled", true);;
        DwrUtil.saveCheckRegister(voidTransaction,reprintTransaction,function(data){
            search();
            dwr.util.setValue("checkRegisterMessage", data);
            jQuery(":button").attr("disabled", false);
        });
    }
}

function trim(str) {
    return str.replace(/^\s+|\s+$/g, "");
}


function clearDetails(){
    dwr.util.setValues({
        glAccountNo:"",
        bankAccountNo:"",
        startCheckNumber:"",
        vendorName:"",
        vendorNumber:"",
        bankReconcileDate:"",
        endCheckNumber:"",
        invoiceAmount:"",
        batchNumber:"",
        checkRegisterMessage:"",
        showStatus:"All",
        payMethod:"All",
        invoiceOperator:"<>",
        checkRegisterListDiv:""
    });
}
function showDetails(transactionId){
    var canEdit = dwr.util.getValue("canEdit");
   DwrUtil.getCheckRegisterDetailsList(transactionId,canEdit,function(data){
        showPopUp();
        var checkListDiv = createHTMLElement("div","checkListDiv","90%","224px",document.body);
        dwr.util.setValue("checkListDiv", data, {
            escapeHtml:false
        });
        jQuery("#checkRegisterDetails").tablesorter({
            widgets: ['zebra']
        });
        floatDiv("checkListDiv", document.body.offsetLeft+60,document.body.offsetTop+40).floatIt();
    });
}

function closeDetails(){
    document.body.removeChild(document.getElementById("checkListDiv"));
    closePopUp();
}
function searchResults(){
    if(navigator.appName == "Netscape"){
        search();
    }else if(event.keyCode==13 || event.keyCode==9 || event.button==0){
        search();
    }
}
/**
 * create CheckRegisterForm javascript object which will be
 * used as CheckRegisterForm java bean Object in DWR java method
 * this object is converted to bean object by DWR bean converter defined in dwr.xml
 */
function getCheckRegisterForm() {
    var checkRegisterForm = new Object();
    checkRegisterForm.glAccountNo = dwr.util.getValue("glAccountNo");
    checkRegisterForm.bankAccountNo = dwr.util.getValue("bankAccountNo");
    checkRegisterForm.startCheckNumber = dwr.util.getValue("startCheckNumber");
    checkRegisterForm.endCheckNumber = dwr.util.getValue("endCheckNumber");
    checkRegisterForm.vendorName = dwr.util.getValue("vendorName");
    checkRegisterForm.vendorNumber = dwr.util.getValue("vendorNumber");
    checkRegisterForm.invoiceAmount = dwr.util.getValue("invoiceAmount");
    checkRegisterForm.bankReconcileDate = dwr.util.getValue("bankReconcileDate");
    checkRegisterForm.invoiceOperator = dwr.util.getValue("invoiceOperator");
    checkRegisterForm.batchNumber = dwr.util.getValue("batchNumber");
    if(trim(checkRegisterForm.glAccountNo)=="" &&
        trim(checkRegisterForm.startCheckNumber)=="" && trim(checkRegisterForm.endCheckNumber)==""
        && trim(checkRegisterForm.vendorName)=="" && trim(checkRegisterForm.invoiceAmount)==""
        && trim(checkRegisterForm.bankReconcileDate)=="" && trim(checkRegisterForm.batchNumber)==""){
        alert("Please Enter Account or any of one filters");
        return null;
    }else{
        var showStatus = document.getElementsByName('showStatus');
        var status="";
        for(var i=0;i<showStatus.length;i++){
            if(showStatus[i].checked) {
                status = showStatus[i].value;
            }
        }
        checkRegisterForm.showStatus = status;
        checkRegisterForm.payMethod =document.getElementById('payMethod').value;
        document.getElementById('checkRegisterMessage').innerHTML="";
        return checkRegisterForm;
    }
}
/**
 * Pagination using DWR which displaying partial List
 * @author Lakshmi Narayanan V
 */
function gotoPage(pageNo){
    //var checkRegisterForm = this.getCheckRegisterForm();
    if(null!=checkRegisterForm){
        document.getElementById("pageNo").value=pageNo;
        checkRegisterForm.pageNo=pageNo;
        checkRegisterForm.currentPageSize=100;
        checkRegisterForm.buttonValue="gotoPage";
        checkRegisterForm.sortBy = document.getElementById("sortBy").value;
        checkRegisterForm.sortOrder = document.getElementById("sortOrder").value;
        jQuery(":button").attr("disabled", true);
        var transaction = document.getElementsByName("transactionId");
        var voids = document.getElementsByName("void");
        var reprint = document.getElementsByName("reprint");
        var voidIds = "";
        var reprintIds = "";
        for(var i=0; i < transaction.length ; i++) {
            if(voids[i].checked && !voids[i].disabled) {
                voidIds = voidIds+voids[i].value+":-";
            }else if(reprint[i].checked && !voids[i].disabled && !reprint[i].disabled) {
                reprintIds = reprintIds+reprint[i].value+":-";
            }
        }
        checkRegisterForm.voidIds = voidIds;
        checkRegisterForm.reprintIds = reprintIds;
	var canEdit = dwr.util.getValue("canEdit");
        DwrUtil.getCheckRegisterList(checkRegisterForm,canEdit,function (data){
            jQuery(":button").attr("disabled", false);
            if(data){
                dwr.util.setValue("checkRegisterListDiv", data, {
                    escapeHtml:false
                });
                changeTableSortingPath();
            }
        });
    }
}
/**
 * Sorting using DWR which displaying partial List
 * @author Lakshmi Narayanan V
 */
function doSort(sortBy){
    document.getElementById("sortBy").value=sortBy;
    if(document.getElementById("sortOrder").value=="desc"){
        document.getElementById("sortOrder").value="asc";
    }else{
        document.getElementById("sortOrder").value="desc";
    }
    //var checkRegisterForm = this.getCheckRegisterForm();
    if(null!=checkRegisterForm){
        checkRegisterForm.pageNo=document.getElementById("pageNo").value;
        checkRegisterForm.currentPageSize=100;
        checkRegisterForm.buttonValue="doSort";
        checkRegisterForm.sortBy = document.getElementById("sortBy").value;
        checkRegisterForm.sortOrder = document.getElementById("sortOrder").value;
        jQuery(":button").attr("disabled", true);
        var transaction = document.getElementsByName("transactionId");
        var voids = document.getElementsByName("void");
        var reprint = document.getElementsByName("reprint");
        var voidIds = "";
        var reprintIds = "";
        for(var i=0; i < transaction.length ; i++) {
            if(voids[i].checked && !voids[i].disabled) {
                voidIds = voidIds+voids[i].value+":-";
            }else if(reprint[i].checked && !voids[i].disabled && !reprint[i].disabled) {
                reprintIds = reprintIds+reprint[i].value+":-";
            }
        }
        checkRegisterForm.voidIds = voidIds;
        checkRegisterForm.reprintIds = reprintIds;
	var canEdit = dwr.util.getValue("canEdit");
        DwrUtil.getCheckRegisterList(checkRegisterForm,canEdit,function (data){
            jQuery(":button").attr("disabled", false);
            if(data){
                dwr.util.setValue("checkRegisterListDiv", data, {
                    escapeHtml:false
                });
                changeTableSortingPath();
            }
        });
    }
}
function changeTableSortingPath(){
    var hrefs = document.getElementsByTagName("a");
    for (var i=0; i < hrefs.length; i++){
        var refs = hrefs[i];
        var xt = getURLParam(refs.href);
        if (xt.length > 0){
            hrefs[i].href = xt;
        }
    }
}
function getURLParam(oldHref){
    var newRef = "javascript:doSort('";
    if (oldHref.indexOf("d-") > -1 ){
        if (oldHref.indexOf("-s") > -1  && oldHref.indexOf("-o") > -1){
            var tempStr = oldHref.split("&");
            for(var i=0;i<tempStr.length;i++){
                if (tempStr[i].indexOf("d-") > -1 ){
                    if (tempStr[i].indexOf("-s") > -1 ){
                        var sortBy = tempStr[i].split("=");
                        newRef=newRef+sortBy[1];
                    }
                }
            }
        }
        newRef = newRef+"')"
    }else{
        newRef="" ;
    }
    return newRef;
}
useLogisoftLodingMessageNew();
if(document.getElementById("glAccountNo")){
    AjaxAutocompleter("glAccountNo", "glAccountNoChoices","bankAccountNo", "glAccountNoCheck", rootPath+"/servlet/AutoCompleterServlet?action=GlAccount&textFieldId=glAccountNo&tabName=CHECK_REGISTER","searchResults()","");
}
if(document.getElementById("vendorName")){
    AjaxAutocompleter("vendorName", "vendorNameChoices","vendorNumber", "vendorNameCheck", rootPath+"/servlet/AutoCompleterServlet?action=Vendor&textFieldId=vendorName&accountType=V","","");
}