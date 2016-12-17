<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,java.util.*,java.text.SimpleDateFormat"%>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.FclBlConstants"/>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>FCL B/L</title>
        <%

                    String path = request.getContextPath();
                    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String search = "";
                    String indexValue = "";
                    String unNumber = "";
                    String accountNo = "";
                    String imgPath = "";
                    if (request.getAttribute("BlHazMat") != null) {
                        indexValue = (String) request.getAttribute("BlHazMat");
                    }
                    String manifest = "";
                    if (request.getAttribute("manifest") != null) {
                        manifest = (String) request.getAttribute("manifest");
                    }
                    List hazmatList = new ArrayList();
                    if (request.getAttribute("hazmat") != null) {
                        hazmatList = (List) request.getAttribute("hazmat");
                    }
                    String number = "";
                    if (request.getAttribute("quotationNo") != null) {
                        number = (String) request.getAttribute("quotationNo");
                    }
                    String name = "";
                    if (request.getAttribute("quotationName") != null) {
                        name = (String) request.getAttribute("quotationName");
                    }
                    if (request.getParameter("button") != null) {
                        search = request.getParameter("button");
                    }
                    String buttonValue = "";
                    if (request.getAttribute("buttonValue") != null) {
                        buttonValue = (String) request.getAttribute("buttonValue");
                    }
                    String path1 = "";
                    if (request.getAttribute("path1") != null) {
                        path1 = (String) request.getAttribute("path1");
                    }
                    String fclBolId = "";
                    if (request.getAttribute("fclBlNo") != null) {
                        fclBolId = (String) request.getAttribute("fclBlNo");
                    }
                    String modify = "";
                    if (session.getAttribute("view") != null) {
                        modify = (String) session.getAttribute("view");
                    }
                    String editPath = path + "/fCLHazMat.do";
                    String link = "";
        %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
        <script type="text/javascript">
            function save1(val){
                var screenName = '${quotationName}';
                var mandatory ="";
                if(document.fCLHazMatForm.unHazmatNumber.value==""){
                    mandatory =mandatory+"--> Please Enter Un Number<br>";
                    jQuery("#unHazmatNumber").css("border-color","red");
                }
                if(undefined != screenName && null != screenName && (screenName == 'Quote' || screenName == 'Booking' || (screenName == 'FclBl' && document.fCLHazMatForm.freeFormat[1].checked))){
                    if(document.getElementById('imoClssCode').value == ''){
                        mandatory =mandatory+"--> Please Select Class<br>";
                        jQuery("#imoClssCode").css("border-color","red");
                    }
                    if(document.getElementById('shippingname').value == ''){
                        mandatory =mandatory+"--> Please Enter Shipping Name<br>";
                        jQuery("#shippingname").css("border-color","red");
                    }
                }
                if(undefined != screenName && null != screenName && screenName == 'FclBl' && document.fCLHazMatForm.freeFormat[1].checked){
                    if(document.getElementById('outerPackingPieces').value == ''){
                        mandatory =mandatory+"--> Please Select Outer Packing Pieces<br>";
                        jQuery("#outerPackingPieces").css("border-color","red");
                    }
                    if(document.getElementById('outerPackagingType').value == ''){
                        mandatory =mandatory+"--> Please Select Outer Packaging Type<br>";
                        jQuery("#outerPackagingType").css("border-color","red");
                    }
                    if(document.getElementById('outerPackComposition').value == ''){
                        mandatory =mandatory+"--> Please Select Outer Pack Composition<br>";
                        jQuery("#outerPackComposition").css("border-color","red");
                    }
                    if(document.getElementById('grossWeight').value == '' || document.getElementById('grossWeight').value == '0.00'){
                        mandatory =mandatory+"--> Please Enter Gross Weight<br>";
                        jQuery("#grossWeight").css("border-color","red");
                    }
                    if(document.getElementById('emergRespTelNo').value != ''){
                        var phone = document.getElementById('emergRespTelNo').value;
                        var obj=replaceAll("-","",phone);
                        if(!isNumber(obj)) {
                            mandatory =mandatory+"--> Please Enter Valid Phone Number<br>";
                            document.getElementById('emergRespTelNo').value="";
                            document.getElementById('emergRespTelNo').focus();
                        }
                    }

                }
                if(mandatory != ''){
                    alertNew(mandatory);
                    return false;
                }else if(val==""){
                    document.fCLHazMatForm.buttonValue.value="save";
                }else{
                    document.fCLHazMatForm.index.value=val;
                    document.fCLHazMatForm.buttonValue.value="update";
                }
                document.fCLHazMatForm.submit();
            }
            function replaceAll(stringToFind,stringToReplace,temp){
                var index = temp.indexOf(stringToFind);
                while(index != -1){
                    temp = temp.replace(stringToFind,stringToReplace);
                    index = temp.indexOf(stringToFind);
                }
                return temp;
            }
            function add(){
                document.fCLHazMatForm.buttonValue.value="add";
                document.fCLHazMatForm.submit();
            }
            function isValidPhoneNumber(obj)
            {
                var num = obj.value.replace(/[^\d]/g,'');
                if(num.length == 10) {
                    obj.value = num.substring(0,3) + "-" + num.substring(3, 6) + "-" + num.substring(6,10);
                }
            }
            function disabled(val1){
                if(val1== 3){
                    var input = document.getElementsByTagName("input");
                    for(i=0; i<input.length; i++) {
                        input[i].readOnly=true;
                        input[i].tabIndex = -1;
                        input[i].style.color="blue";
                    }
                    var textarea = document.getElementsByTagName("textarea");
                    for(i=0; i<textarea.length; i++) {
                        textarea[i].readOnly=true;
                        input[i].tabIndex = -1;
                        textarea[i].style.color="blue";
                    }
                    var imgs = document.getElementsByTagName('img');
                    for(var k=0; k<imgs.length; k++) {
                        imgs[k].style.visibility = 'hidden';
                    }
                    var select = document.getElementsByTagName("select");
                    for(i=0; i<select.length; i++)  {
                        select[i].disabled=true;
                        select[i].style.backgroundColor="blue";
                    }
                    document.getElementById("save").style.visibility = 'hidden';
                }
            }
            function setFocus(){
                setTimeout("set()",150);
            }
            function set(){
                if(null!=document.getElementById('unHazmatNumber')){
                    document.getElementById('unHazmatNumber').focus();
                }
            }
            function makeFormBorderless(form) {
                var element;
                for (var i = 0; i < form.elements.length; i++) {
                    element = form.elements[i];
                    if(element.type == "button"){
                        if(element.value=="Add" || element.value=="Save" || element.value=="Delete" ){
                            element.style.visibility="hidden";
                        }
                    }
                }
                return false;
            }
            function selectHazMat(bolId,hazmatId){
                document.fCLHazMatForm.booking.value=bolId;
                document.fCLHazMatForm.unAssignedHazMatId.value=hazmatId;
                document.fCLHazMatForm.buttonValue.value="convertToBlHazMat";
                document.fCLHazMatForm.submit();
                closePopUp();
                document.getElementById('unAssinedHazmat').style.display='none';
            }
            function deleteHazMats(hazmatId){
                document.fCLHazMatForm.unAssignedHazMatId.value=hazmatId;
                document.fCLHazMatForm.buttonValue.value="deleteHazMat";
                document.fCLHazMatForm.submit();
            }
            function deleteRecord(val1){
                document.fCLHazMatForm.index.value=val1;
                confirmYesOrNo("Are you sure to delete this Hazmat ?","hazmatDelete");
            }
            function edit(va1,val2,val3){
                va1=va1+"&fileNo="+val2+"&unitNo="+val3;
                window.location.href=va1;
            }
            function editHazmat(id,fileNo,unitNo,fclBlNo){
                document.fCLHazMatForm.id.value=id;
                document.fCLHazMatForm.fileNo.value=fileNo;
                document.fCLHazMatForm.unitNo.value=unitNo;
                document.fCLHazMatForm.fclBlNo.value=fclBlNo;
                document.fCLHazMatForm.buttonValue.value="editUnassignedHazmat";
                document.fCLHazMatForm.submit();
                closePopUp();
                document.getElementById('unAssinedHazmat').style.display='none';
            }

            function deleteUnassignedHazmat(hazmatId){
                document.fCLHazMatForm.unAssignedHazMatId.value=hazmatId;
                confirmYesOrNo("Are you sure to delete this Hazmat ?","hazmatDeleteFromQB");
            }
            function confirmMessageFunction(id1,id2){
                if(id1=='hazmatDelete' && id2=='yes'){
                    document.fCLHazMatForm.buttonValue.value="deleteHazMatForQuotesAndBooking";
                    document.fCLHazMatForm.submit();
                }else  if(id1=='hazmatDeleteFromQB' && id2=='yes'){
                    document.fCLHazMatForm.buttonValue.value="deleteUnassignedHazmat";
                    document.fCLHazMatForm.submit();
                    closePopUp();
                    document.getElementById('unAssinedHazmat').style.display='none';
                }
            }
            function closePage(){
                if(document.fCLHazMatForm.name.value=='FclBl'){
                    var color;
                    var indexValue=document.fCLHazMatForm.indexValue.value;
                    //indexValue=0;
                    var table=document.getElementById("divtablesty1");
                    if(null!=table){
                        var tableLength=table.getElementsByTagName("tr");
                        if(null!=tableLength && tableLength.length>1){
                            color="red";
                            parent.parent.makeButtonRedColorForPkgs(color,indexValue,'haz');
                        }else{
                            parent.parent.makeButtonRedColorForPkgs('',indexValue,'haz');
                        }
                    }else{
                        parent.parent.makeButtonRedColorForPkgs('',indexValue,'haz');
                    }
                }
                parent.parent.GB_hide();
            }
            function openChargesPopUp(){
                showPopUp();
                document.getElementById('unAssinedHazmat').style.display='block';
            }
            function closePopup(){
                closePopUp();
                document.getElementById('unAssinedHazmat').style.display='none';
            }
            function getPackageType(obj,packageType){
                jQuery.ajaxx({
                    dataType: "json",
                    data: {
                        className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
                        methodName: "getPackageType",
                        param1: obj.value,
                        dataType: "json"
                    },
                    success: function (data) {
                        var options = [];
                        jQuery.each(data, function (index) {
                            options.push("<option value='" + data[index].value + "'>" + data[index].label + "</option>");
                        });
                        jQuery("#"+packageType).html(options.join(''));
                    }
                });
            }
            function allowOnlyWholeNumbers(obj){
                var result;
                if(!/^[1-9]\d*$/.test(obj.value)){
                    result=obj.value.replace(/[^0-9]+/g,'');
                    obj.value=result;
                    return false;
                }
                return true;
            }
            function openFreeFormatDiv(){
                var screenName = '${quotationName}';
                var mandatory ="";
                if(document.fCLHazMatForm.unHazmatNumber.value==""){
                    mandatory =mandatory+"--> Please Enter Un Number<br>";
                }
                if(mandatory != ''){
                    alertNew(mandatory);
                    document.fCLHazMatForm.freeFormat[0].checked = false;
                    document.fCLHazMatForm.freeFormat[1].checked = true;
                    return false;
                }else{
                    var isFreeFormat = document.getElementById("isFreeFormat").value;
                    if(isFreeFormat != 'Y' ){
                        setLineValues();
                    }
                    showPopUp();
                    document.getElementById("freeFormatHazmatDiv").style.display = 'block';
                    var IpopTop = (screen.height - document.getElementById("freeFormatHazmatDiv").offsetHeight)/2;
                    var IpopLeft = (screen.width - document.getElementById("freeFormatHazmatDiv").offsetWidth)/2;
                    document.getElementById("freeFormatHazmatDiv").style.left=IpopLeft + document.body.scrollLeft-200;
                    document.getElementById("freeFormatHazmatDiv").style.top=IpopTop + document.body.scrollTop-280;
                }
            }
            function closeFreeFormatDiv(){
                var isFreeFormat = document.getElementById("isFreeFormat").value;
                closePopUp();
                if(null != isFreeFormat && isFreeFormat != 'Y'){
                    document.fCLHazMatForm.freeFormat[0].checked = false;
                    document.fCLHazMatForm.freeFormat[1].checked = true;
                }
                document.getElementById("freeFormatHazmatDiv").style.display = 'none';
            }
            function addFreeFormatDiv(){
                var isAllValid=true;
                var listObj={};
                var invalidCharSet=['?','!','<','>'];
                // var totInvalidChar=[];
                for(var i=1;i<=7;i++) {
                    var id="line"+i;
                    // console.log(id,document.getElementById(id));
                    listObj[id]={'value':document.getElementById(id).value,'isValid':false,'invalidChar':[]};
                    for(var j=0;j<listObj[id].value.length;j++) {
                        var thisChar=listObj[id].value[j];
                        if(invalidCharSet.indexOf(thisChar)!=-1 && listObj[id]['invalidChar'].indexOf(thisChar)==-1) {
                            listObj[id]['invalidChar'].push(thisChar);
                            /*if(totInvalidChar.indexOf(thisChar)==-1) {
                                                totInvalidChar.push(thisChar);
                               }*/
                            isAllValid=false;
                        }
                    }
                    if(listObj[id].invalidChar.length==0) {
                        listObj[id]['isValid']=true;
                    } else {
                        listObj[id]['isValid']=false;
                    }
                }
                if(isAllValid) {
                    // alertNew("All valid");
                    closePopUp();
                    document.getElementById("freeFormatHazmatDiv").style.display = 'none';
                } else {
                    var alertInfo="";
                    for(var lineId in listObj) {
                        var info=listObj[lineId];
                        if(info['isValid']==false) {
                            alertInfo+=("Please Remove Following Invalid Characters\nin "+lineId + "\n\n" + info['invalidChar'].join("")+"\n");
                        }
                    }
                    alertNew(alertInfo);
                    // alert("Your fields has " +totInvalidChar.join(" "));
                }
               
            }
            function autoFillFreeFormat(){
                var isFreeFormat = document.getElementById("isFreeFormat").value;
                if(isFreeFormat == 'Y' ){
                    if(confirm("Do You Want To Clear The Existing Details")){
                        document.getElementById("editFreeFormat").style.display = 'none';
                        document.getElementById("isFreeFormat").value = 'N';
                        fillFreeFormat();
                    }else{
                        document.fCLHazMatForm.freeFormat[0].checked = true;
                        document.fCLHazMatForm.freeFormat[1].checked = false;
                    }
                }else{
                    fillFreeFormat();
                }
            }
            function setLineValues(){
                var freeFormat = "";
                if(document.fCLHazMatForm.reportableQuantity[0].checked){
                    freeFormat = freeFormat+"REPORTABLE QUANTITY,";
                }
                if(document.fCLHazMatForm.unHazmatNumber.value != ''){
                    freeFormat = freeFormat+" UN "+document.fCLHazMatForm.unHazmatNumber.value;
                }
                if(document.fCLHazMatForm.shippingname.value != ''){
                    freeFormat = freeFormat+", "+document.fCLHazMatForm.shippingname.value;
                    if(document.fCLHazMatForm.technicalName.value != ''){
                        freeFormat = freeFormat+", ("+document.fCLHazMatForm.technicalName.value+")";
                    }
                }
                if(document.fCLHazMatForm.imoClssCode.value != ''){
                    freeFormat = freeFormat+", CLASS "+document.fCLHazMatForm.imoClssCode.value;
                    if(document.fCLHazMatForm.imoSubsidiaryClassCode.value != ''){
                        freeFormat = freeFormat+", ("+document.fCLHazMatForm.imoSubsidiaryClassCode.value+")";
                    }
                    if(document.fCLHazMatForm.imoSecondarySubClass.value != ''){
                        freeFormat = freeFormat+", ("+document.fCLHazMatForm.imoSecondarySubClass.value+")";
                    }
                }
                if(document.fCLHazMatForm.packingGroupCode.value != ''){
                    freeFormat = freeFormat+", PG "+document.fCLHazMatForm.packingGroupCode.value;
                }
                if(document.fCLHazMatForm.flashPointNo.value != ''){
                    freeFormat = freeFormat+", FLASH POINT "+document.fCLHazMatForm.flashPointNo.value+" DEG C";
                }
                if(document.fCLHazMatForm.outerPackingPieces.value != ''){
                    freeFormat = freeFormat+", "+document.fCLHazMatForm.outerPackingPieces.value+" "+document.fCLHazMatForm.outerPackComposition.value+" "+document.fCLHazMatForm.outerPackagingType.value;
                    if(document.fCLHazMatForm.innerPackingPieces.value != ''){
                        freeFormat = freeFormat+", "+document.fCLHazMatForm.innerPackingPieces.value+" "+document.fCLHazMatForm.innerPackComposition.value+" "+document.fCLHazMatForm.innerPackagingType.value;
                    }
                    if(document.fCLHazMatForm.netWeight.value != '' && document.fCLHazMatForm.netWeight.value != '0.00'){
                        freeFormat = freeFormat+", @ "+document.fCLHazMatForm.netWeight.value+" "+document.fCLHazMatForm.netWeightUMO.value+" EACH";
                    }
                }
                if(document.fCLHazMatForm.grossWeight.value != '' && document.fCLHazMatForm.grossWeight.value != '0.00'){
                    freeFormat = freeFormat+", TOTAL GROSS WEIGHT "+document.fCLHazMatForm.grossWeight.value+" KGS";
                }
                if(document.fCLHazMatForm.volume.value != '' && document.fCLHazMatForm.volume.value != '0.00'){
                    freeFormat = freeFormat+", TOTAL VOLUME "+document.fCLHazMatForm.volume.value+" LITER";
                }
                if(document.fCLHazMatForm.marinePollutant[0].checked){
                    freeFormat = freeFormat+", MARINE POLLUTANT";
                }
                if(document.fCLHazMatForm.exceptedQuantity[0].checked){
                    freeFormat = freeFormat+", EXCEPTED QUANTITY";
                }
                if(document.fCLHazMatForm.limitedQuantity[0].checked){
                    freeFormat = freeFormat+", LIMITED QUANTITY";
                }
                if(document.fCLHazMatForm.inhalationHazard[0].checked){
                    freeFormat = freeFormat+", INHALATION HAZARD";
                }
                if(document.fCLHazMatForm.residue[0].checked){
                    freeFormat = freeFormat+", RESIDUE";
                }
                if(document.fCLHazMatForm.emsCode.value != ''){
                    freeFormat = freeFormat+", EMS "+document.fCLHazMatForm.emsCode.value;
                }
                if(document.fCLHazMatForm.contactName.value != ''){
                    freeFormat = freeFormat+", "+document.fCLHazMatForm.contactName.value;
                }
                if(document.fCLHazMatForm.emergRespTelNo.value != ''){
                    freeFormat = freeFormat+", "+document.fCLHazMatForm.emergRespTelNo.value;
                }
                if(freeFormat != ''){
                    var lines =wordwrap( freeFormat, 50).split("<br/>");
                    for(var i=0;i<7;i++){
                        var j = i+1;
                        if(undefined != lines[i] && null != lines[i]){
                            document.getElementById("line"+j).value = lines[i];
                        }
                    }
                }
            }
            function wordwrap( str, width, brk, cut ) {
                brk = brk || '<br/>\n';
                width = width || 40;
                cut = cut || false;
                if (!str) { return str; }
                var regex = '.{1,' +width+ '}(\\s|$)' + (cut ? '|.{' +width+ '}|.+$' : '|\\S+?(\\s|$)');
                return str.match( RegExp(regex, 'g') ).join( brk );
            }
            function fillFreeFormat(){
                document.fCLHazMatForm.line1.value="";
                document.fCLHazMatForm.line2.value="";
                document.fCLHazMatForm.line3.value="";
                document.fCLHazMatForm.line4.value="";
                document.fCLHazMatForm.line5.value="";
                document.fCLHazMatForm.line6.value="";
                document.fCLHazMatForm.line7.value="";
            }

        </script>`
        <style type="text/css">
            #unAssinedHazmat{
                position:fixed;
                _position:absolute;
                border-style: solid solid solid solid;
                background-color: white;
                z-index:99;
                left:20%;
                top:50%;
                bottom:5%;
                right:5%;
                _height:expression(document.body.offset+"px");
            }
            #freeFormatHazmatDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: white;
                _height: expression(document.body.offsetHeight + "px");
            }
        </style>
        <%@include file="../includes/resources.jsp" %>
    </head>

    <div id="cover" style="width: 906px ;height: 1000px;"></div>
    <body class="whitebackgrnd" onload="setFocus();">

        <!--DESIGN FOR NEW ALERT BOX ---->
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" onclick="document.getElementById('AlertBox').style.display='none';	grayOut(false,'');"/>
            </form>
        </div>
        <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmYes()"/>
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="confirmNo()"/>
            </form>
        </div>
        <!--// ALERT BOX DESIGN ENDS -->

        <html:form action="/fCLHazMat" styleId="fclhazmat" name="fCLHazMatForm" type="com.gp.cvst.logisoft.struts.form.FCLHazMatForm" scope="request">
            <html:hidden property="buttonValue"/>

            File No:<b style="color:red;"><c:out value="${fileNo}"></c:out></b>
            <b class="textlabels" style="padding-left:20px;">
	Unit No:<font color="Red" size="2">
                    <c:out value="${fCLHazMatForm.unitNo}"/>
                    <input  type="hidden"  name="unitNo" value="${fCLHazMatForm.unitNo}"/>
                </font></b>

            <c:choose>
                <c:when test="${not empty bookingHazmatList}">
                    <div id="unAssinedHazmat" style="display:none;width:500px;height:200px;">
                        <table width="100%" border="0"  class="tableBorderNew">
                            <tr class="tableHeadingNew">
                                <td>List Of Un assigned HazMat in the Booking</td>
                                <td align="right">
                                    <img src="<%=path%>/img/icons/cross-white.png" alt="Close" style="width:15px;"
                                         onclick="closePopup()"/></td>
                            </tr>
                            <tr><td colspan="2">
                                    <display:table name="${bookingHazmatList}" class="displaytagstyle" pagesize="10"  style="width:100%"
                                                   id="fclHazmat" sort="list" requestURI="/fCLHazMat.do">
                                        <display:setProperty name="paging.banner.some_items_found">
                                            <span class="pagebanner">
                                                <font color="blue">{0}</font> Hazmat displayed,For more Records click on page numbers.
                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.one_item_found">
                                            <span class="pagebanner">
								One {0} displayed. Page Number
                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.all_items_found">
                                            <span class="pagebanner">
								{0} {1} Displayed, Page Number
                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
							No Records Found.
                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.placement" value="bottom" />
                                        <display:setProperty name="paging.banner.item_name" value="Hazmat"/>
                                        <display:setProperty name="paging.banner.items_name" value="Hazmats"/>

                                        <%--<c:set var="attachmentsIcon"><c:url value='/img/icons/delete.gif'/></c:set>
			<display:column title="<img src='${attachmentsIcon}'  alt='Number of attachments.'/>">
			</display:column> --%>

                                        <display:column title="">
                                            <c:choose>
                                                <c:when test="${not empty fclHazmat.mandatory}">
                                                    <img src="<%=path%>/img/icons/arrow-circle-135-left.png" alt="Assign" style="width:15px;"
                                                         onmouseover="tooltip.show('<strong>${fclHazmat.mandatory}</strong>','90',event);" onmouseout="tooltip.hide();"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="<%=path%>/img/icons/arrow-circle-135-left.png" alt="Assign" style="width:15px;"
                                                         onclick="selectHazMat('${fclHazmat.bolId}','${fclHazmat.id}')"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </display:column>
                                        <display:column title="">
                                            <img src="<%=path%>/img/icons/edit.gif" alt="edit"
                                                 onclick="editHazmat('${fclHazmat.id}','${fileNo}','${fCLHazMatForm.unitNo}','${fclBlNo}')"/>
                                        </display:column>

                                        <display:column title="">
                                            <img src="<%=path%>/img/icons/delete.gif" alt="Delete"
                                                 onclick="deleteUnassignedHazmat('${fclHazmat.id}')"/>
                                        </display:column>
                                        <display:column title="UN Number" property="unNumber"></display:column>
                                        <display:column property="propShipingNumber" title="Proper Shipping Name"></display:column>
                                        <display:column property="technicalName" title="Technical Name"></display:column>
                                        <display:column title="IMO Class Code" property="imoClssCode"></display:column>

                                    </display:table></td></tr>
                        </table></div>
                    </c:when>
                </c:choose>
            </div>
            <%
                        if (hazmatList != null && hazmatList.size() > 0) {
                            int i = 0;
            %>

            <div id="divtablesty1">
                <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                    <tr class="tableHeadingNew"><td>List of Hazmats assigned to this Unit</td></tr>
                    <tr><td>
                            <display:table name="<%=hazmatList%>" class="displaytagstyle"  pagesize="10"  style="width:100%" id="arInquiry" sort="list" requestURI="/fCLHazMat.do">

                                <display:setProperty name="paging.banner.some_items_found">
                                    <span class="pagebanner">
                                        <font color="blue">{0}</font> Hazmat displayed,For more Records click on page numbers.
                                    </span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.one_item_found">
                                    <span class="pagebanner">
						One {0} displayed. Page Number
                                    </span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.all_items_found">
                                    <span class="pagebanner">
						{0} {1} Displayed, Page Number

                                    </span>
                                </display:setProperty>
                                <display:setProperty name="basic.msg.empty_list"><span class="pagebanner">
					No Records Found.
                                    </span>
                                </display:setProperty>
                                <display:setProperty name="paging.banner.placement" value="bottom" />
                                <display:setProperty name="paging.banner.item_name" value="Hazmat"/>
                                <display:setProperty name="paging.banner.items_name" value="Hazmats"/>
                                <%
                                                            HazmatMaterial hazmatMaterial = (HazmatMaterial) hazmatList.get(i);
                                                            unNumber = hazmatMaterial.getUnNumber();
                                                            link = editPath + "?paramId=" + hazmatMaterial.getId() + "&name=" + hazmatMaterial.getDocTypeCode()
                                                                    + "&number=" + hazmatMaterial.getBolId() + "&indexValue=" + indexValue + "&manifest=" + manifest;
                                                            if (!fclBolId.equals("")) {
                                                                link = link + "&fclBolId=" + fclBolId;
                                                            }
                                %>
                                <display:column title="">
                                    <%if (hazmatMaterial.getDocTypeCode() != null && hazmatMaterial.getDocTypeCode().
                                                                        equalsIgnoreCase(FclBlConstants.HAZMATQUOTEFORBL)) {%>

                                    <img src="<%=path%>/img/icons/arrow-circle-135.png" alt="Unassign/Delete" style="width:15px;"
                                         onclick="deleteHazMats('<%=hazmatMaterial.getId().toString()%>')"/>
                                    <%}%>
                                </display:column>
                                <display:column title="Actions" >
                                    <img src="<%=path%>/img/icons/edit.gif" alt="edit" onclick="edit('<%=link%>','${fileNo}','${fCLHazMatForm.unitNo}')"/>
                                    <c:if test="${manifest != 'M'}">
                                        <img src="<%=path%>/img/icons/delete.gif" alt="Delete" onclick="deleteRecord('<%=hazmatMaterial.getId().toString()%>')"/>
                                    </c:if>
                                </display:column>
                                <display:column title="UN Number"><%=unNumber%></display:column>
                                <display:column property="propShipingNumber" title="Proper Shipping Name"></display:column>
                                <display:column property="technicalName" title="Technical Name"></display:column>
                                <display:column title="IMO Class Code" property="imoClssCode"></display:column>

                                <%i++;%>
                            </display:table>
                            <%}%></td></tr> </table>
            </div>
            <br/>
            <table  width="100%"border="0" class="tableBorderNew" cellpadding="0" cellspacing="0">
                <tr class="tableHeadingNew">
                    <%
                                if (buttonValue.equals("editQuotation") || buttonValue.equals("save") || buttonValue.equals("update")
                                        || buttonValue.equals("fclbl") || buttonValue.equals("Booking") || buttonValue.equals("Quotation") || buttonValue.equals("convertToBlHazMat")
                                        || buttonValue.equals("deleteHazMat") || buttonValue.equals("deleteHazMatForQuotesAndBooking")
                                        || buttonValue.equals("deleteUnassignedHazmat")) {%>
                    <td align="right" colspan="2">
                        <c:if test="${not empty bookingHazmatList}">
                            <input type="button" value="UnAssigned Hazmat" style="width:120px;" id="unAssigned"
                                   onclick="openChargesPopUp()" class="buttonColor" align="right"/>
                        </c:if>
                        <c:if test="${manifest != 'M'}">
                            <input type="button" value="Add" id="Add" onclick="add()" class="buttonStyleNew" align="right"/>
                        </c:if>
                        <input type="button" value="Close" class="buttonStyleNew" onclick="closePage()" align="right" />
                    </td>
                    <%}%>
                    <%if (buttonValue.equals("add") || buttonValue.equals("") || buttonValue.equals("editUnassignedHazmat")) {%>
                    <td align="right" colspan="2">
                        <input type="button" value="Save" id="save" onclick="save1('${hazmatMaterial.id}')" class="buttonStyleNew" align="right"
                               onmouseover="tooltip.show('<strong><%=mandatoryFieldForHazMat%></strong>','90',event);" onmouseout="tooltip.hide();"/>
                        <input type="button" value="Close" class="buttonStyleNew" onclick="closePage()" align="right"/>
                    </td>
                    <%}%>

                </tr>
                <tr>
                    <%if (buttonValue.equals("add") || buttonValue.equals("") || buttonValue.equals("editUnassignedHazmat")) {%>
                    <td>
                        <table cellpadding="2" cellspacing="0" border="0" width="100%" class="hazmat-table">
                            <tr class="textlabelsBold">
                                <td>UN1 #:</td>
                                <td><html:text property="unHazmatNumber" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox mandatory"
                                           styleId="unHazmatNumber" value="${hazmatMaterial.unNumber}" maxlength="4"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Proper Shipping Name:</td>
                                <td><html:text property="shippingname" styleId="shippingname" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox mandatory" value="${hazmatMaterial.propShipingNumber}" maxlength="50"/>
                                </td>
                                <td>Tech. Chemical Name:</td>
                                <td><html:text property="technicalName" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox" value="${hazmatMaterial.technicalName}" maxlength="300"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Class:</td>
                                <td>
                                    <div class="mandatory" style="float:left">
                                        <html:select property="imoClssCode" styleId="imoClssCode"  styleClass="dropdown_accounting" value="${hazmatMaterial.imoClssCode}">
                                            <html:optionsCollection name="hazmatPrimaryList"/></html:select>
                                        </div>
                                    </td>
                                    <td>IMO Subsidiary Class:</td>
                                    <td><html:text property="imoSubsidiaryClassCode" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"
                                           value="${hazmatMaterial.imoSubsidiaryClassCode}" maxlength="3" />
                                </td>
                                <td>IMO Secondary Class:</td>
                                <td><html:text property="imoSecondarySubClass" style="text-transform: uppercase"
                                           styleClass="textlabelsBoldForTextBox" value="${hazmatMaterial.imoSecondarySubClass}"
                                           maxlength="3"/>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Packing Group Code:</td>
                                <td><html:select property="packingGroupCode" value="${hazmatMaterial.packingGroupCode}"
                                             styleClass="dropdown_accounting">
                                        <html:optionsCollection name="packingGroupCode"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Flash Point:</td>
                                <td><html:text property="flashPointNo" styleClass="textlabelsBoldForTextBox"
                                           value="${hazmatMaterial.flashPointUMO}" maxlength="10" size="8"/>(Celsius)
                                </td>
                            </tr>
                        </table>
                        <table cellpadding="2" cellspacing="0" border="0"  class="hazmat-table">
                            <tr>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>Outer Packaging Pieces:</td>
                                            <td><html:text property="outerPackingPieces" styleId="outerPackingPieces" onkeyup="allowOnlyWholeNumbers(this);"
                                                       styleClass="textlabelsBoldForTextBox mandatory" style="text-transform: uppercase" value="${hazmatMaterial.outerPackingPieces}"
                                                       maxlength="5"  onchange="getPackageType(this,'outerPackagingType');"/>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Outer Packaging Type:</td>
                                            <td><div class="mandatory" style="float:left"><html:select property="outerPackagingType" styleId="outerPackagingType" value="${hazmatMaterial.outerPackagingType}"
                                                         styleClass="dropdown_accounting mandatory">
                                                        <html:optionsCollection name="packageTypeList"/>
                                                    </html:select></div>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Outer Pkg Composition:</td>
                                            <td>
                                                <div class="mandatory" style="float:left"><html:select property="outerPackComposition" value="${hazmatMaterial.outerPackComposition}"
                                                             styleClass="dropdown_accounting"  styleId="outerPackComposition">
                                                        <html:optionsCollection name="packCompositionTypeList"/>
                                                    </html:select></div>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>Inner Packing Pieces:</td>
                                            <td><html:text property="innerPackingPieces" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox" styleId="innerPackingPieces"
                                                       value="${hazmatMaterial.innerPackingPieces}" maxlength="5" onchange="getPackageType(this,'innerPackagingType')" onkeyup="allowOnlyWholeNumbers(this);"/>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Inner Packaging Type:</td>
                                            <td><html:select property="innerPackagingType" styleId="innerPackagingType" value="${hazmatMaterial.innerPackagingType}"
                                                         styleClass="dropdown_accounting">
                                                    <html:optionsCollection name="packageTypeList"/>
                                                </html:select>
                                            </td>
                                            <%--<td>Flash Point UOM</td>
                                                <td><html:radio property="flashPoint" value="C" name="FCLHazMatForm"/>C
                                                <html:radio property="flashPoint" value="F" name="FCLHazMatForm"/>F</td>--%>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Inner Pkg Composition:</td>
                                            <td><html:select property="innerPackComposition"  value="${hazmatMaterial.innerPackComposition}"
                                                         styleClass="dropdown_accounting" >
                                                    <html:optionsCollection name="packCompositionTypeList"/>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Inner Pkg UOM:</td>
                                            <td>
                                                <html:select property="netWeightUMO" styleId="netWeightUMO" styleClass="dropdown_accounting">
                                                    <html:option value="">Select</html:option>
                                                    <html:option value="KGS">Kilograms</html:option>
                                                    <html:option value="LTR">Liters</html:option>
                                                    <html:option value="OZ">Ounces</html:option>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Inner Pkg Wt/Vol Per Piece:</td>
                                            <fmt:formatNumber type="number"  var="netWeight"  value="${hazmatMaterial.netWeight}" pattern="########0.00" />
                                            <td><html:text property="netWeight"  styleClass="textlabelsBoldForTextBox" value="${netWeight}" onkeydown="check(this,7)" onblur="checkdec(this)" maxlength="10"/></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                        <table cellpadding="2" cellspacing="0" border="0" class="hazmat-table">
                            <tr>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>Total Net Weight/KGS:</td>
                                            <fmt:formatNumber type="number"  var="totalNetWeight"  value="${hazmatMaterial.totalNetWeight}" pattern="########0.00" />
                                            <td><html:text property="totalNetWeight"  styleId="totalNetWeight" styleClass="textlabelsBoldForTextBox" value="${totalNetWeight}"
                                                       onkeypress="check(this,7)" onblur="checkdec(this)" maxlength="10"/>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Total Volume:</td>
                                            <fmt:formatNumber type="number"  var="volume"   value="${hazmatMaterial.volume}" pattern="########0.00" />
                                            <td><html:text property="volume"  styleClass="textlabelsBoldForTextBox" value="${volume}" onkeypress="check(this,7)" onblur="checkdec(this)" maxlength="10"/></td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Total Gross Weight/KGS:</td>
                                            <fmt:formatNumber type="number"  var="grossWeight"  value="${hazmatMaterial.grossWeight}" pattern="########0.00" />
                                            <td><html:text property="grossWeight"  styleId="grossWeight" styleClass="textlabelsBoldForTextBox mandatory" value="${grossWeight}"
                                                       onkeypress="check(this,7)" onblur="checkdec(this)" maxlength="10"/></td>
                                                <%--<fmt:formatNumber type="number"  var="grossWeight1"  value="${hazmatMaterial.grossWeightUMO}"
                                                        pattern="########0.00" />--%>
                                                <%--<td><html:text property="grossWeightUMO"  styleClass="BackgrndColorForTextBox" value="KGS" readonly="true" maxlength="3" size="3" /></td>--%>
                                                <%-- onkeypress="check(this,7)" style="text-transform: uppercase" onkeydown="verifyKgLB('gross') onblur="checkdec(this)"--%>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table>
                                        <tr class="textlabelsBold">
                                            <td>Emergency Contact:</td>
                                            <td><html:text property="contactName" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox"  value="${hazmatMaterial.contactName}" maxlength="40"/></td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Emergency Phone Number:</td>
                                            <td><html:text property="emergRespTelNo" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox" value="${hazmatMaterial.emerreprsNum}"
                                                       maxlength="12" onchange="isValidPhoneNumber(this)" styleId="emergRespTelNo"/></td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>EMS Code:</td>
                                            <td><html:text property="emsCode" style="text-transform: uppercase" styleClass="textlabelsBoldForTextBox" value="${hazmatMaterial.emsCode}" maxlength="20"/></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td valign="top">
                        <table>
                            <tr class="textlabelsBold">
                                <td>Reportable Quantity:</td>
                                <td><html:radio property="reportableQuantity" value="Y" name="FCLHazMatForm"/>Y
                                    <html:radio property="reportableQuantity" value="N" />N
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Marine Pollutant:</td>
                                <td><html:radio property="marinePollutant" value="Y" name="FCLHazMatForm"/>Y
                                    <html:radio property="marinePollutant" value="N" name="FCLHazMatForm"/>N
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Excepted Quantity:</td>
                                <td><html:radio property="exceptedQuantity" value="Y" name="FCLHazMatForm"/>Y
                                    <html:radio property="exceptedQuantity" value="N" name="FCLHazMatForm"/>N
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Limited Quantity:</td>
                                <td><html:radio property="limitedQuantity" value="Y" name="FCLHazMatForm"/>Y
                                    <html:radio property="limitedQuantity" value="N" name="FCLHazMatForm"/>N
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Inhalation Hazard:</td>
                                <td><html:radio property="inhalationHazard" value="Y" name="FCLHazMatForm"/>Y
                                    <html:radio property="inhalationHazard" value="N" name="FCLHazMatForm"/>N
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td>Residue:</td>
                                <td><html:radio property="residue" value="Y" name="FCLHazMatForm"/>Y
                                    <html:radio property="residue" value="N" name="FCLHazMatForm"/>N
                                </td>
                            </tr>
                            <tr class="textlabelsBold">
                                <td><span style="float: left">Free Format</span> &nbsp;
                                    <c:if test="${FCLHazMatForm.freeFormat == 'Y'}">
                                        <span style="padding:10px 0 0 0;" id="editFreeFormat">
                                            <img src="<%=path%>/img/icons/edit.gif" alt="edit" onclick="openFreeFormatDiv()" />
                                        </span>
                                    </c:if>
                                </td>
                                <td><html:radio property="freeFormat" value="Y" name="FCLHazMatForm" onclick="openFreeFormatDiv()"/>Y
                                    <html:radio property="freeFormat" value="N" name="FCLHazMatForm" onclick="autoFillFreeFormat('${FCLHazMatForm.freeFormat}')"/>N
                                </td>
                            </tr>
                        </table>
                    </td>
                    <%}%>
                </tr>
            </table>
            <div id="freeFormatHazmatDiv" style="display: none; height:180px;width:400px;"><div align="right">
                </div><table width="100%" border="0" cellpadding="2" cellspacing="0">
                    <tr class="textlabelsBold" >
                        <td>LINE1</td>
                        <td><html:text property="line1" value="${line[0]}" styleClass="textlabelsBoldForTextBox" size="51" maxlength="50" style="text-transform: uppercase" styleId="line1"/></td>
                    </tr>
                    <tr class="textlabelsBold" >
                        <td>LINE2</td>
                        <td><html:text property="line2" value="${line[1]}" styleClass="textlabelsBoldForTextBox" size="51" maxlength="50" style="text-transform: uppercase" styleId="line2"/></td>
                    </tr>
                    <tr class="textlabelsBold" >
                        <td>LINE3</td>
                        <td><html:text property="line3" value="${line[2]}" styleClass="textlabelsBoldForTextBox" size="51" maxlength="50" style="text-transform: uppercase" styleId="line3"/></td>
                    </tr>
                    <tr class="textlabelsBold" >
                        <td>LINE4</td>
                        <td><html:text property="line4" value="${line[3]}" styleClass="textlabelsBoldForTextBox" size="51" maxlength="50" style="text-transform: uppercase" styleId="line4"/></td>
                    </tr>
                    <tr class="textlabelsBold" >
                        <td>LINE5</td>
                        <td><html:text property="line5" value="${line[4]}" styleClass="textlabelsBoldForTextBox" size="51" maxlength="50" style="text-transform: uppercase" styleId="line5"/></td>
                    </tr>
                    <tr class="textlabelsBold" >
                        <td>LINE6</td>
                        <td><html:text property="line6" value="${line[5]}" styleClass="textlabelsBoldForTextBox" size="51" maxlength="50" style="text-transform: uppercase" styleId="line6"/></td>
                    </tr>
                    <tr class="textlabelsBold" >
                        <td>LINE7</td>
                        <td><html:text property="line7" value="${line[6]}" styleClass="textlabelsBoldForTextBox" size="51" maxlength="50" style="text-transform: uppercase" styleId="line7"/></td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                    <tr>

                        <td colspan="2" align="center">
                            <input type="button" value="submit"  class="buttonStyleNew"  onclick="addFreeFormatDiv()"/>
                            <input type="button" value="cancel"  class="buttonStyleNew"  onclick="closeFreeFormatDiv()"/>
                        </td>
                    </tr>
                </table>
            </div>
            <br/>
            <c:choose>
                <c:when test="${not empty unAssigned}">
                    <html:hidden property="quoteName" value="${quotationName}"/>
                    <html:hidden property="quoteNumber"  value="${quotationNo}"/>
                </c:when>
                <c:otherwise>
                    <html:hidden property="quoteName" value=""/>
                    <html:hidden property="quoteNumber"  value=""/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${not empty FCLHazMatForm.name}">
                    <html:hidden property="name" value="${FCLHazMatForm.name}"/>
                </c:when>
                <c:otherwise>
                    <html:hidden property="name" value="<%=name%>"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${not empty FCLHazMatForm.number}">
                    <html:hidden property="number" value="${FCLHazMatForm.number}"/>
                </c:when>
                <c:otherwise>
                    <html:hidden property="number"  value="<%=number%>"/>
                </c:otherwise>
            </c:choose>
            <html:hidden property="button" value="<%=search%>"/>
            <html:hidden property="index" />
            <input type="hidden" name="indexValue" value="<%=indexValue%>" />
            <input type="hidden" name="manifest" value="<%=manifest%>" />
            <html:hidden property="booking" />
            <html:hidden property="unAssignedHazMatId" />

            <html:hidden property="bolId" value="${FCLHazMatForm.bolId}" />
            <input  type="hidden"  name="fileNo" value="${fileNo}"/>
            <input  type="hidden"  name="fclBlNo" />
            <input  type="hidden"  name="id" />
            <input  type="hidden"  id="isFreeFormat" value="${FCLHazMatForm.freeFormat}"/>

        </html:form>
    </body>

    <%if (modify.equals("3")) {%>
    <script>parent.parent.parent.parent.makeFormBorderless(document.getElementById("fclhazmat"))</script>
    <script type="text/javascript">makeFormBorderless(document.getElementById("fclhazmat"));</script>
    <%}%>

    <script>
        if(parent.parent.checkHazmat!=undefined && parent.parent.checkHazmat!=null){
            parent.parent.checkHazmat();
        }
    </script>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>


